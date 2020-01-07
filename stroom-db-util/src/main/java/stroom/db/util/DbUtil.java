package stroom.db.util;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stroom.config.common.ConnectionConfig;
import stroom.util.logging.LogUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbUtil.class);
    private static final long MAX_SLEEP_TIME_MS = 30_000;
    private static final int ACCESS_DENIED_BAD_UNAME_OR_PWORD = 1045;
    private static final int ACCESS_DENIED_BAD_DATABASE = 1044;

    private DbUtil() {
    }

    public static void validate(final ConnectionConfig connectionConfig) {
        Preconditions.checkNotNull(connectionConfig.getJdbcDriverClassName(),
            "The JDBC driver class has not been supplied");
        Preconditions.checkNotNull(connectionConfig.getJdbcDriverUrl(),
            "The JDBC URL has not been supplied");
        Preconditions.checkNotNull(connectionConfig.getJdbcDriverUsername(),
            "The JDBC username has not been supplied");
        Preconditions.checkNotNull(connectionConfig.getJdbcDriverPassword(),
            "The JDBC password has not been supplied");

        try {
            Class.forName(connectionConfig.getJdbcDriverClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(LogUtil.message(
                    "Invalid JDBC driver class name {}", connectionConfig.getJdbcDriverClassName()), e);
        }
    }

    public static Connection getSingleConnection(final ConnectionConfig connectionConfig) throws SQLException {
        return DriverManager.getConnection(
                connectionConfig.getJdbcDriverUrl(),
                connectionConfig.getJdbcDriverUsername(),
                connectionConfig.getJdbcDriverPassword());
    }

    /**
     * Attempts to connect to the database using the passed connection details. If it fails
     * it will log a warning, and keep retrying. The retry interval will steadily increase.
     * <p>
     * If the connection could not be established and the reason for the failure makes a
     * retry pointless, e.g. invalid password, then an exception will be thrown.
     */
    public static void waitForConnection(ConnectionConfig connectionConfig) {
        final String jdbcUrl = connectionConfig.getJdbcDriverUrl();
        final String username = connectionConfig.getJdbcDriverUsername();
        LOGGER.info("Ensuring database connection to {} with username {} and driver class {}",
                jdbcUrl, username, connectionConfig.getJdbcDriverClassName());

        long sleepMs = 100;
        Throwable lastThrowable = null;

        while (true) {
            try (Connection connection = getSingleConnection(connectionConfig)) {
                LOGGER.info("Successfully established connection to {} with username {}", jdbcUrl, username);
                break;
            } catch (SQLException e) {
                if (e.getErrorCode() == ACCESS_DENIED_BAD_UNAME_OR_PWORD ||
                        e.getErrorCode() == ACCESS_DENIED_BAD_DATABASE ||
                        (e.getMessage() != null && e.getMessage().startsWith("Unsupported"))) {

                    // These errors are not due to the DB not being up, so throw it
                    throw new RuntimeException(LogUtil.message(
                            "Error connecting to {} with username {}", jdbcUrl, username), e);
                }
                final Throwable cause = e.getCause();
                final String errorMsg = cause != null ? cause.getMessage() : e.getMessage();
                final int vendorCode = e.getErrorCode();
                LOGGER.warn("Unable to establish database connection due to error: [{}] " +
                                "and vendorCode [{}], will try again " +
                                "in {}ms, enable debug to see stack trace",
                        errorMsg, vendorCode, sleepMs);
                if (LOGGER.isDebugEnabled()) {
                    if (lastThrowable == null || !e.getMessage().equals(lastThrowable.getMessage())) {
                        // Only log the stack when it changes, else it fills up the log pretty quickly
                        LOGGER.debug("Unable to establish database connection due to error", e);
                    }
                    lastThrowable = e;
                }
            }
            try {
                Thread.sleep(sleepMs);
            } catch (InterruptedException e) {
                // Nothing to do here as the
                Thread.currentThread().interrupt();
                break;
            }

            // Gradually increase the sleep time up to a maximum
            sleepMs = (long) (sleepMs * 1.3);
            if (sleepMs >= MAX_SLEEP_TIME_MS) {
                sleepMs = MAX_SLEEP_TIME_MS;
            }
        }
    }

    public static int countEntity(final Connection connection, final String tableName) {
        try (final Statement statement = connection.createStatement()) {
            try (final ResultSet resultSet = statement.executeQuery("SELECT count(*) FROM " + tableName)) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return 0;
    }


//    private void executeStatement(final Connection connection, final String sql) throws SQLException {
//        executeStatements(connection, Collections.singletonList(sql));
//    }

}
