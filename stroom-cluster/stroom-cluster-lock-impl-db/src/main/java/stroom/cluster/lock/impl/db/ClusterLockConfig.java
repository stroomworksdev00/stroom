package stroom.cluster.lock.impl.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import stroom.config.common.DbConfig;
import stroom.config.common.HasDbConfig;
import stroom.util.shared.IsConfig;

import javax.inject.Singleton;

@Singleton
public class ClusterLockConfig implements IsConfig, HasDbConfig {

    private DbConfig dbConfig;

    public ClusterLockConfig() {
        this.dbConfig = new DbConfig();
    }

    @JsonProperty("db")
    public DbConfig getDbConfig() {
        return dbConfig;
    }

    public void setDbConfig(final DbConfig dbConfig) {
        this.dbConfig = dbConfig;
    }
}
