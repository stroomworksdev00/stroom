package stroom.proxy.app.handler;

import stroom.meta.api.AttributeMap;
import stroom.proxy.app.forwarder.ForwarderConfig;
import stroom.proxy.repo.ForwarderDestinations;
import stroom.proxy.repo.ProxyRepoConfig;
import stroom.proxy.repo.ProxyRepositoryStreamHandlers;
import stroom.receive.common.StreamHandler;
import stroom.receive.common.StreamHandlers;

import com.google.common.base.Strings;

import java.util.function.Consumer;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ReceiveStreamHandlers implements StreamHandlers {

    private final StreamHandlers streamHandlers;

    @Inject
    ReceiveStreamHandlers(final ProxyRepoConfig proxyRepoConfig,
                          final ProxyRepositoryStreamHandlers proxyRepositoryStreamHandlerProvider,
                          final ForwarderDestinations forwarderDestinations,
                          final ForwarderConfig forwarderConfig) {
        // Check the config is valid.
        if (proxyRepoConfig.isStoringEnabled()) {
            if (Strings.isNullOrEmpty(proxyRepoConfig.getRepoDir())) {
                throw new RuntimeException("Storing is enabled but no repo directory have been provided in 'repoDir'");
            }
            streamHandlers = proxyRepositoryStreamHandlerProvider;
        } else {
            if (forwarderConfig.getForwardDestinations() == null ||
                    forwarderConfig.getForwardDestinations().size() == 0) {
                throw new RuntimeException("Storing is not enabled and no forward destinations are configured");
            } else if (forwarderConfig.getForwardDestinations().size() > 1) {
                throw new RuntimeException("You must store data to be able to forward to multiple destinations");
            }
            streamHandlers = forwarderDestinations
                    .getProvider(forwarderDestinations.getDestinationNames().get(0));
        }
    }

    @Override
    public void handle(final AttributeMap attributeMap, final Consumer<StreamHandler> consumer) {
        streamHandlers.handle(attributeMap, consumer);
    }
}
