/*
 * Copyright 2016 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package stroom.proxy.app.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stroom.dictionary.impl.DictionaryResource;
import stroom.proxy.app.ContentSyncConfig;
import stroom.proxy.app.handler.FeedStatusConfig;
import stroom.receive.common.FeedStatusResource;
import stroom.receive.rules.impl.ReceiveDataRuleSetResource;
import stroom.util.guice.ResourcePaths;
import stroom.util.logging.LogUtil;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * <p>
 * Filter to avoid posts to the wrong place (e.g. the root of the app)
 * </p>
 */
public class ProxySecurityFilter implements Filter {
    private static final String IGNORE_URI_REGEX = "ignoreUri";
    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxySecurityFilter.class);

    private final ContentSyncConfig contentSyncConfig;
    private final FeedStatusConfig feedStatusConfig;

    private Pattern pattern = null;

    @Inject
    public ProxySecurityFilter(final ContentSyncConfig contentSyncConfig,
                               final FeedStatusConfig feedStatusConfig) {
        this.contentSyncConfig = contentSyncConfig;
        this.feedStatusConfig = feedStatusConfig;
    }

    @Override
    public void init(final FilterConfig filterConfig) {
        final String regex = filterConfig.getInitParameter(IGNORE_URI_REGEX);
        if (regex != null) {
            pattern = Pattern.compile(regex);
        }
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        if (!(response instanceof HttpServletResponse)) {
            final String message = "Unexpected response type: " + response.getClass().getName();
            LOGGER.error(message);
            return;
        }
        final HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (!(request instanceof HttpServletRequest)) {
            final String message = "Unexpected request type: " + request.getClass().getName();
            LOGGER.error(message);
            httpServletResponse.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, message);
            return;
        }
        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        filter(httpServletRequest, httpServletResponse, chain);
    }

    private void filter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        if (request.getMethod().toUpperCase().equals(HttpMethod.OPTIONS)) {
            // We need to allow CORS preflight requests
            chain.doFilter(request, response);

        } else if (ignoreUri(request.getRequestURI())) {
            // Allow some URIs to bypass authentication checks
            chain.doFilter(request, response);

        } else {
            // We need to distinguish between requests from an API client and from the UI.
            // If a request is from the UI and fails authentication then we need to redirect to the login page.
            // If a request is from an API client and fails authentication then we need to return HTTP 403 UNAUTHORIZED.
            final String requestURI = request.getRequestURI();
            final boolean isApiRequest = requestURI.contains(ResourcePaths.API_PATH);

            if (isApiRequest) {
                try {
                    final String configuredApiKey = getConfiguredApiKey(requestURI);
                    final String requestApiKey = getJWS(request);

                    if (!configuredApiKey.equals(requestApiKey)) {
                        throw new RuntimeException(
                                LogUtil.message(
                                        "Supplied API key from {} to {} is invalid",
                                        request.getRemoteHost(), requestURI));
                    }

                    chain.doFilter(request, response);

                } catch (final RuntimeException e) {
                    LOGGER.error(e.getMessage(), e);
                    response.setStatus(Response.Status.UNAUTHORIZED.getStatusCode());
                }

            } else {
                chain.doFilter(request, response);
            }
        }
    }

    private String getConfiguredApiKey(final String requestUri) {
        // TODO it could be argued that we should have a single API key to use for all of these resources.
        final String apiKey;
        if (requestUri.startsWith(ResourcePaths.API_PATH + FeedStatusResource.BASE_RESOURCE_PATH)) {
            apiKey = feedStatusConfig.getApiKey();
        } else if (requestUri.startsWith(ResourcePaths.API_PATH + DictionaryResource.BASE_RESOURCE_PATH)) {
            apiKey = contentSyncConfig.getApiKey();
        } else if (requestUri.startsWith(ResourcePaths.API_PATH + ReceiveDataRuleSetResource.BASE_RESOURCE_PATH)) {
            apiKey = contentSyncConfig.getApiKey();
        } else {
            throw new RuntimeException(LogUtil.message(
                    "Unable to determine which config to get API key from for requestURI {}", requestUri));
        }
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException(LogUtil.message(
                    "API key is empty, requestURI {}", requestUri));
        }
        return apiKey;
    }

    private boolean ignoreUri(final String uri) {
        return pattern != null && pattern.matcher(uri).matches();
    }

    private String getJWS(final HttpServletRequest request) {
        final String bearerString = request.getHeader(AUTHORIZATION_HEADER);
        String jws = null;
        if (bearerString != null && !bearerString.isEmpty()) {
            if (bearerString.startsWith(BEARER)) {
                // This chops out 'Bearer' so we get just the token.
                jws = bearerString.substring(BEARER.length());
            } else {
                jws = bearerString;
            }
            LOGGER.debug("Found auth header in request. It looks like this: {}", jws);
        }
        return jws;
    }

    @Override
    public void destroy() {
    }
}
