package stroom.test;

import stroom.app.guice.CoreModule;
import stroom.app.guice.DbConnectionsModule;
import stroom.app.guice.JerseyModule;
import stroom.app.uri.UriFactoryModule;
import stroom.config.global.impl.GlobalConfigBootstrapModule;
import stroom.config.global.impl.db.GlobalConfigDaoModule;
import stroom.index.VolumeTestConfigModule;
import stroom.index.mock.MockIndexShardWriterExecutorModule;
import stroom.meta.statistics.impl.MockMetaStatisticsModule;
import stroom.resource.impl.ResourceModule;
import stroom.security.mock.MockSecurityContextModule;
import stroom.util.io.DirProvidersModule;

import com.codahale.metrics.MetricRegistry;
import com.google.inject.AbstractModule;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.setup.Environment;

public class CoreTestModule extends AbstractModule {

    @Override
    protected void configure() {
        // Stuff that comes from BootStrapModule
        // TODO: 15/06/2023 This will get replaced by new Environment("TestEnvironment") in the merge up
        //  as that ctor doesn't exist in 7.0
        bind(Environment.class).toInstance(new Environment(
                "Test Environment",
                Jackson.newObjectMapper(),
                Validators.newValidatorFactory().getValidator(),
                new MetricRegistry(),
                ClassLoader.getSystemClassLoader()));
        install(new GlobalConfigBootstrapModule());
        install(new GlobalConfigDaoModule());
        install(new DirProvidersModule());
        install(new DbConnectionsModule());

        install(new AppConfigTestModule());
        install(new UriFactoryModule());
        install(new CoreModule());
        install(new ResourceModule());
        install(new stroom.cluster.impl.MockClusterModule());
        install(new VolumeTestConfigModule());
        install(new MockSecurityContextModule());
        install(new MockMetaStatisticsModule());
        install(new stroom.test.DatabaseTestControlModule());
        install(new JerseyModule());
        install(new MockIndexShardWriterExecutorModule());
    }
}
