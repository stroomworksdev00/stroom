package stroom.node.impl.db;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.zaxxer.hikari.HikariConfig;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stroom.db.util.HikariUtil;
import stroom.node.impl.NodeConfig;
import stroom.node.impl.NodeDao;
import stroom.util.guice.GuiceUtil;

import javax.inject.Provider;
import javax.inject.Singleton;
import javax.sql.DataSource;

public class NodeDbModule extends AbstractModule {
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeDbModule.class);
    private static final String MODULE = "stroom-node";
    private static final String FLYWAY_LOCATIONS = "stroom/node/impl/db/migration";
    private static final String FLYWAY_TABLE = "node_schema_history";

    @Override
    protected void configure() {
        bind(NodeDao.class).to(NodeDaoImpl.class);

        // MultiBind the connection provider so we can see status for all databases.
        GuiceUtil.buildMultiBinder(binder(), DataSource.class)
                .addBinding(ConnectionProvider.class);

//        bind(NodeDbService.class).to(NodeDbServiceImpl.class);
//        bind(CurrentNodeDb.class).to(CurrentNodeDbImpl.class);
//
//        TaskHandlerBinder.create(binder())
//                .bind(CreateNodeDbAction.class, CreateNodeDbHandler.class)
//                .bind(UpdateNodeDbAction.class, UpdateNodeDbHandler.class)
//                .bind(DeleteNodeDbAction.class, DeleteNodeDbHandler.class)
//                .bind(FetchNodeDbAction.class, FetchNodeDbHandler.class)
//                .bind(FindNodeDbAction.class, FindNodeDbHandler.class)
//                .bind(SetCurrentNodeDbAction.class, SetCurrentNodeDbHandler.class);

    }

    @Provides
    @Singleton
    ConnectionProvider getConnectionProvider(final Provider<NodeConfig> configProvider) {
        LOGGER.info("Creating connection provider for {}", MODULE);
        final HikariConfig config = HikariUtil.createConfig(configProvider.get());
        final ConnectionProvider connectionProvider = new ConnectionProvider(config);
        flyway(connectionProvider);
        return connectionProvider;
    }

    private Flyway flyway(final DataSource dataSource) {
        final Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations(FLYWAY_LOCATIONS)
                .table(FLYWAY_TABLE)
                .baselineOnMigrate(true)
                .load();
        LOGGER.info("Applying Flyway migrations to {} in {} from {}", MODULE, FLYWAY_TABLE, FLYWAY_LOCATIONS);
        try {
            flyway.migrate();
        } catch (FlywayException e) {
            LOGGER.error("Error migrating {} database", MODULE, e);
            throw e;
        }
        LOGGER.info("Completed Flyway migrations for {} in {}", MODULE, FLYWAY_TABLE);
        return flyway;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
