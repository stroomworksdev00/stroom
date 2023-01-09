package stroom.db.util;

import stroom.config.common.AbstractDbConfig;
import stroom.util.logging.LambdaLogger;
import stroom.util.logging.LambdaLoggerFactory;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.MigrationInfo;

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.sql.DataSource;

/**
 * @param <T_CONFIG>    A config class that extends {@link AbstractDbConfig}
 * @param <T_CONN_PROV> A class that extends {@link HikariDataSource}
 */
public abstract class AbstractFlyWayDbModule<T_CONFIG extends AbstractDbConfig, T_CONN_PROV extends DataSource>
        extends AbstractDataSourceProviderModule<T_CONFIG, T_CONN_PROV> {

    private static final LambdaLogger LOGGER = LambdaLoggerFactory.getLogger(AbstractFlyWayDbModule.class);

    protected abstract String getFlyWayTableName();

    protected abstract String getFlyWayLocation();

    @Override
    protected void configure() {
        super.configure();
        LOGGER.debug(() -> "Configure() called on " + this.getClass().getCanonicalName());
    }

    @Override
    protected void performMigration(final DataSource dataSource) {
        LOGGER.info(""
                + "\n-----------------------------------------------------------"
                + "\n  Migrating database module: " + getModuleName()
                + "\n-----------------------------------------------------------");

        final Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations(getFlyWayLocation())
                .table(getFlyWayTableName())
                .baselineOnMigrate(true)
                .load();

        final String statesInfo = Arrays.stream(flyway.info().all())
                .collect(Collectors.groupingBy(MigrationInfo::getState))
                .entrySet()
                .stream()
                .sorted(Entry.comparingByKey())
                .map(entry -> entry.getKey() + ":" + entry.getValue().size())
                .collect(Collectors.joining(", "));

        try {
            LOGGER.info("{} - Validating existing and pending Flyway DB migration(s) ({}) " +
                            "using history table '{}' from path {}",
                    getModuleName(),
                    statesInfo,
                    getFlyWayTableName(),
                    getFlyWayLocation());

            // This will see if anything needs doing
            final int migrationsApplied = flyway.migrate();

            if (migrationsApplied > 0) {
                LOGGER.info("{} - Successfully applied {} Flyway DB migrations using history table '{}'",
                        getModuleName(),
                        migrationsApplied,
                        getFlyWayTableName());
            } else {
                LOGGER.info("{} - No Flyway DB migration(s) applied in path {}",
                        getModuleName(),
                        getFlyWayLocation());
            }

        } catch (FlywayException e) {
            LOGGER.error("{} - Error migrating database: {}", getModuleName(), e.getMessage(), e);
            throw e;
        }
    }
}
