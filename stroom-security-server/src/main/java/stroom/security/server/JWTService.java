package stroom.security.server;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import stroom.auth.service.ApiException;
import stroom.auth.service.api.ApiKeyApi;
import stroom.security.server.exception.AuthenticationException;
import stroom.security.shared.UserRef;
import stroom.util.HasHealthCheck;

import javax.inject.Inject;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

@Component
public class JWTService implements HasHealthCheck {
    private static final Logger LOGGER = LoggerFactory.getLogger(JWTService.class);

    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private PublicJsonWebKey jwk;
    private final String authenticationServiceUrl;
    private final String authJwtIssuer;
    private AuthenticationServiceClients authenticationServiceClients;
    private final boolean checkTokenRevocation;
    private final boolean authenticationRequired;
    private Clock clock;
    private String clientId;

    @Inject
    public JWTService(
            @NotNull @Value("#{propertyConfigurer.getProperty('stroom.auth.services.url')}") final String authenticationServiceUrl,
            @NotNull @Value("#{propertyConfigurer.getProperty('stroom.auth.jwt.issuer')}") final String authJwtIssuer,
            @NotNull @Value("#{propertyConfigurer.getProperty('stroom.auth.clientId')}") final String clientId,
            @NotNull @Value("#{propertyConfigurer.getProperty('stroom.auth.jwt.enabletokenrevocationcheck')}") final boolean enableTokenRevocationCheck,
            @NotNull @Value("#{propertyConfigurer.getProperty('stroom.authentication.required')}") final boolean authenticationRequired,
            final AuthenticationServiceClients authenticationServiceClients) {
        this.clientId = clientId;
        this.authenticationServiceUrl = authenticationServiceUrl;
        this.authJwtIssuer = authJwtIssuer;
        this.authenticationServiceClients = authenticationServiceClients;
        this.checkTokenRevocation = enableTokenRevocationCheck;
        this.authenticationRequired = authenticationRequired;

        updatePublicJsonWebKey();

        if (authenticationServiceUrl == null) {
            throw new SecurityException("No authentication service URL is defined");
        }

        this.clock = Clock.systemDefaultZone();
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    private void updatePublicJsonWebKey() {
        if (authenticationRequired) {
            try {
                String jwkAsJson = fetchNewPublicKey();
                jwk = RsaJsonWebKey.Factory.newPublicJwk(jwkAsJson);
            } catch (JoseException | ApiException e) {
                LOGGER.error("Unable to fetch the remote authentication service's public key!", e);
            }
        }
    }

    /**
     * Check to see if the remote authentication service has published a public key.
     * <p>
     * We need this key to verify id tokens.
     * <p>
     * We need to do this if the remote public key changes and verification fails.
     */
    private String fetchNewPublicKey() throws ApiException {
        try {
            // We need to fetch the public key from the remote authentication service.
            final ApiKeyApi apiKeyApi = authenticationServiceClients.newApiKeyApi();
            return apiKeyApi.getPublicKey();
        } catch (final ApiException | RuntimeException e) {
            LOGGER.error("Error fetching new public API key from URL '" + authenticationServiceUrl + "'.");
            if (authenticationServiceUrl != null && authenticationServiceUrl.toLowerCase().contains("https")) {
                LOGGER.error("Are you sure you want to use HTTPS? IF so you will need to configure the trust store or disable SSL verification with 'stroom.auth.services.verifyingSsl=false'");
            }
            throw e;
        }
    }

    public Optional<String> getUserId(final Optional<String> optionalJws) {
        final String jws = optionalJws.orElseThrow(() -> new AuthenticationException("Unable to get JWS"));
        LOGGER.debug("Found auth header in request. It looks like this: {}", jws);

        try {
            LOGGER.debug("Verifying token...");
            final JwtClaims jwtClaims = verifyToken(jws);
            boolean isVerified = jwtClaims != null;
            boolean isRevoked = false;
            if (checkTokenRevocation) {
                LOGGER.debug("Checking token revocation status in remote auth service...");
                final String userId = getUserIdFromToken(jws);
                isRevoked = userId == null;
            }

            if (isVerified && !isRevoked) {
                return Optional.ofNullable(jwtClaims.getSubject());
            }

        } catch (Exception e) {
            LOGGER.error("Unable to verify token: " + e.getMessage(), e);
            LOGGER.warn(e.getMessage());
            throw new AuthenticationException(e.getMessage(), e);
        }

        return Optional.empty();
    }

    public String refreshTokenIfExpired(String jws) {
        if (!authenticationRequired) {
            return null;
        }

        try {
            verifyToken(jws);
            return jws;
        } catch (InvalidJwtException e) {
            try {
                JwtClaims claims = e.getJwtContext().getJwtClaims();
                if (claims.getExpirationTime().getValueInMillis() < Instant.now().toEpochMilli()) {
                    LOGGER.info("The API key for user '{}' has expired. An API key is required, i.e. for queries. Creating a new one.", claims.getSubject());
                    String newJws = authenticationServiceClients.createTokenForUser(claims.getSubject());
                    return newJws;
                } else {
                    return jws;
                }
            } catch (MalformedClaimException | ApiException innerEx) {
                String error = "Unable to get new token! The error was: " + innerEx.getMessage();
                LOGGER.error(error);
                throw new RuntimeException(error, innerEx);
            }
        }
    }

    public Optional<String> getJws(final ServletRequest request) {
        final Optional<String> authHeader = getAuthHeader(request);
        return authHeader.map(bearerString -> {
            String jws;
            if (bearerString.startsWith(BEARER)) {
                // This chops out 'Bearer' so we get just the token.
                jws = bearerString.substring(BEARER.length());
            } else {
                jws = bearerString;
            }
            LOGGER.debug("Found auth header in request. It looks like this: {}", jws);
            return jws;
        });
    }

    private String getUserIdFromToken(final String token) {
        try {
            LOGGER.debug("Checking with the Authentication Service that a token is valid.");
            return authenticationServiceClients.newAuthenticationApi().verifyToken(token);
        } catch (ApiException e) {
            String message = String.format(
                    "Unable to verify token remotely! Message was: %s. HTTP response code was: %s. Response body was: %s",
                    e.getMessage(), e.getCode(), e.getResponseBody());
            LOGGER.debug(message);
            throw new RuntimeException(message, e);
        }
    }

    public JwtClaims verifyToken(final String token) throws InvalidJwtException {
        try {
            return toClaims(token);
        } catch (InvalidJwtException e) {
            LOGGER.warn("Unable to verify token! I'm going to refresh the verification key and try again.");
            updatePublicJsonWebKey();
            return toClaims(token);
        }
    }

    private static Optional<String> getAuthHeader(final ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        return (getAuthHeader(httpServletRequest));
    }

    private static Optional<String> getAuthHeader(final HttpServletRequest httpServletRequest) {
        String authHeader = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        return Strings.isNullOrEmpty(authHeader) ? Optional.empty() : Optional.of(authHeader);
    }

    private JwtClaims toClaims(String token) throws InvalidJwtException {
        final JwtConsumer jwtConsumer = newJwsConsumer();
        return jwtConsumer.processToClaims(token);
    }

    private JwtConsumer newJwsConsumer() {
        // If we don't have a JWK we can't create a consumer to verify anything.
        // Why might we not have one? If the remote authentication service was down when Stroom started
        // then we wouldn't. It might not be up now but we're going to try and fetch it.
        if (jwk == null) {
            updatePublicJsonWebKey();
        }

        JwtConsumerBuilder builder = new JwtConsumerBuilder()
                .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                .setRequireSubject() // the JWT must have a subject claim
                .setVerificationKey(this.jwk.getPublicKey()) // verify the signature with the public key
                .setExpectedAudience(clientId)
                .setRelaxVerificationKeyValidation() // relaxes key length requirement
                .setJwsAlgorithmConstraints( // only allow the expected signature algorithm(s) in the given context
                        new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST, // which is only RS256 here
                                AlgorithmIdentifiers.RSA_USING_SHA256))
                .setExpectedIssuer(authJwtIssuer);
        return builder.build();
    }

    @Override
    public HealthCheck.Result getHealth() {
        // Defaults to healthy
        HealthCheck.ResultBuilder resultBuilder = HealthCheck.Result.builder().healthy();
        this.checkHealthForJwkRetrieval(resultBuilder);
        return resultBuilder.build();
    }

    private void checkHealthForJwkRetrieval(HealthCheck.ResultBuilder resultBuilder) {
        final String KEY = "public_key_retrieval";
        try {
            String publicJsonWebKey = fetchNewPublicKey();
            boolean canGetJwk = StringUtils.isNotBlank(publicJsonWebKey);
            if (!canGetJwk) {
                resultBuilder.withDetail(KEY, "Cannot get stroom-auth-service's public key!\n");
                resultBuilder.unhealthy();
            }
        } catch (ApiException | RuntimeException e) {
            resultBuilder.withDetail(KEY, "Error fetching our identity provider's public key! " +
                    "This means we cannot verify clients' authentication tokens ourselves. " +
                    "This might mean the authentication service is down or unavailable. " +
                    "The error was: [" + e.getMessage() + "]");
            resultBuilder.unhealthy();
        }

    }
}
