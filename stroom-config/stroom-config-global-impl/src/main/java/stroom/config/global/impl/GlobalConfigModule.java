package stroom.config.global.impl;

import stroom.job.api.ScheduledJobsBinder;
import stroom.util.RunnableWrapper;
import stroom.util.guice.GuiceUtil;
import stroom.util.guice.HasHealthCheckBinder;
import stroom.util.guice.HasSystemInfoBinder;
import stroom.util.guice.RestResourcesBinder;
import stroom.util.validation.ValidationModule;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.dropwizard.lifecycle.Managed;

import javax.inject.Inject;

import static stroom.job.api.Schedule.ScheduleType.PERIODIC;

public class GlobalConfigModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AppConfigMonitor.class).asEagerSingleton();

        // Need to ensure it initialises so any db props can be set on AppConfig
        bind(GlobalConfigService.class).asEagerSingleton();

        HasHealthCheckBinder.create(binder())
                .bind(AppConfigMonitor.class);

        GuiceUtil.buildMultiBinder(binder(), Managed.class)
                .addBinding(AppConfigMonitor.class);

        RestResourcesBinder.create(binder())
                .bind(GlobalConfigResourceImpl.class);

        HasSystemInfoBinder.create(binder())
                .bind(AppConfigSystemInfo.class);

        install(new ValidationModule());

        ScheduledJobsBinder.create(binder())
                .bindJobTo(PropertyCacheReload.class, builder -> builder
                        .name("Property Cache Reload")
                        .description("Reload properties in the cluster")
                        .schedule(PERIODIC, "1m"));
    }

    // START These methods are generated by stroom.config.global.impl.TestGlobalConfigModule
    @Provides
    @SuppressWarnings("unused")
    stroom.pipeline.refdata.ReferenceDataLmdbConfig getReferenceDataLmdbConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.pipeline.refdata.ReferenceDataLmdbConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.explorer.impl.ExplorerConfig getExplorerConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.explorer.impl.ExplorerConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.ui.config.shared.ThemeConfig getThemeConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.ui.config.shared.ThemeConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.node.impl.StatusConfig getStatusConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.node.impl.StatusConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.lmdb.LmdbLibraryConfig getLmdbLibraryConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.lmdb.LmdbLibraryConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.searchable.impl.SearchableConfig getSearchableConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.searchable.impl.SearchableConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.kafka.impl.KafkaConfig getKafkaConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.kafka.impl.KafkaConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.cluster.lock.impl.db.ClusterLockConfig getClusterLockConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.cluster.lock.impl.db.ClusterLockConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.security.impl.ContentSecurityConfig getContentSecurityConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.security.impl.ContentSecurityConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.ui.config.shared.QueryConfig getQueryConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.ui.config.shared.QueryConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.config.app.AppConfig getAppConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.config.app.AppConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.importexport.impl.ContentPackImportConfig getContentPackImportConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.importexport.impl.ContentPackImportConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.index.impl.IndexCacheConfig getIndexCacheConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.index.impl.IndexCacheConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.data.store.impl.fs.DataStoreServiceConfig getDataStoreServiceConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.data.store.impl.fs.DataStoreServiceConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.security.identity.config.TokenConfig getTokenConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.security.identity.config.TokenConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.data.store.impl.fs.FsVolumeConfig getFsVolumeConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.data.store.impl.fs.FsVolumeConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.security.identity.config.IdentityConfig getIdentityConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.security.identity.config.IdentityConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.pipeline.filter.XmlSchemaConfig getXmlSchemaConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.pipeline.filter.XmlSchemaConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.event.logging.impl.LoggingConfig getLoggingConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.event.logging.impl.LoggingConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.config.app.DataConfig getDataConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.config.app.DataConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.security.identity.config.OpenIdConfig getOpenIdConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.security.identity.config.OpenIdConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.core.receive.ProxyAggregationConfig getProxyAggregationConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.core.receive.ProxyAggregationConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.cluster.api.ClusterConfig getClusterConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.cluster.api.ClusterConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.security.identity.config.PasswordPolicyConfig getPasswordPolicyConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.security.identity.config.PasswordPolicyConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.legacy.db.LegacyDbConfig getLegacyDbConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.legacy.db.LegacyDbConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.feed.impl.FeedConfig getFeedConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.feed.impl.FeedConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.config.common.NodeUriConfig getNodeUriConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.config.common.NodeUriConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.docstore.impl.db.DocStoreConfig getDocStoreConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.docstore.impl.db.DocStoreConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.node.impl.HeapHistogramConfig getHeapHistogramConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.node.impl.HeapHistogramConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.statistics.impl.sql.search.SearchConfig getSearchConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.statistics.impl.sql.search.SearchConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.pipeline.refdata.ReferenceDataConfig getReferenceDataConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.pipeline.refdata.ReferenceDataConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.search.impl.shard.IndexShardSearchConfig getIndexShardSearchConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.search.impl.shard.IndexShardSearchConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.importexport.impl.ExportConfig getExportConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.importexport.impl.ExportConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.statistics.impl.hbase.internal.KafkaTopicsConfig getKafkaTopicsConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.statistics.impl.hbase.internal.KafkaTopicsConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.dashboard.impl.DashboardConfig getDashboardConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.dashboard.impl.DashboardConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.index.impl.IndexWriterConfig getIndexWriterConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.index.impl.IndexWriterConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.ui.config.shared.SplashConfig getSplashConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.ui.config.shared.SplashConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.dashboard.impl.datasource.DataSourceUrlConfig getDataSourceUrlConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.dashboard.impl.datasource.DataSourceUrlConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.config.app.PropertyServiceConfig getPropertyServiceConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.config.app.PropertyServiceConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.storedquery.impl.StoredQueryConfig getStoredQueryConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.storedquery.impl.StoredQueryConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.util.xml.ParserConfig getParserConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.util.xml.ParserConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.ui.config.shared.ProcessConfig getProcessConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.ui.config.shared.ProcessConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.servicediscovery.impl.ServiceDiscoveryConfig getServiceDiscoveryConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.servicediscovery.impl.ServiceDiscoveryConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.security.identity.config.EmailConfig getEmailConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.security.identity.config.EmailConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.search.solr.SolrConfig getSolrConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.search.solr.SolrConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.config.app.SecurityConfig getSecurityConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.config.app.SecurityConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.ui.config.shared.ActivityConfig getActivityConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.ui.config.shared.ActivityConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.pipeline.filter.XsltConfig getXsltConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.pipeline.filter.XsltConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.activity.impl.db.ActivityConfig getActivityConfig2(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.activity.impl.db.ActivityConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.search.solr.search.SolrSearchConfig getSolrSearchConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.search.solr.search.SolrSearchConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.security.impl.AuthorisationConfig getAuthorisationConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.security.impl.AuthorisationConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.ui.config.shared.InfoPopupConfig getInfoPopupConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.ui.config.shared.InfoPopupConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.search.impl.SearchConfig getSearchConfig2(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.search.impl.SearchConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.bytebuffer.ByteBufferPoolConfig getByteBufferPoolConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.bytebuffer.ByteBufferPoolConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.index.impl.IndexConfig getIndexConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.index.impl.IndexConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.ui.config.shared.SourceConfig getSourceConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.ui.config.shared.SourceConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.meta.impl.MetaValueConfig getMetaValueConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.meta.impl.MetaValueConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.config.common.UiUriConfig getUiUriConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.config.common.UiUriConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.config.common.CommonDbConfig getCommonDbConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.config.common.CommonDbConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.node.impl.NodeConfig getNodeConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.node.impl.NodeConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.data.retention.api.DataRetentionConfig getDataRetentionConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.data.retention.api.DataRetentionConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.processor.impl.ProcessorConfig getProcessorConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.processor.impl.ProcessorConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.query.common.v2.ResultStoreConfig getResultStoreConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.query.common.v2.ResultStoreConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.index.impl.selection.VolumeConfig getVolumeConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.index.impl.selection.VolumeConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.statistics.impl.InternalStatisticsConfig getInternalStatisticsConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.statistics.impl.InternalStatisticsConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.job.impl.JobSystemConfig getJobSystemConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.job.impl.JobSystemConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.util.io.StroomPathConfig getStroomPathConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.util.io.StroomPathConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.statistics.impl.hbase.internal.HBaseStatisticsConfig getHBaseStatisticsConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.statistics.impl.hbase.internal.HBaseStatisticsConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.security.impl.AuthenticationConfig getAuthenticationConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.security.impl.AuthenticationConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.statistics.impl.sql.SQLStatisticsConfig getSQLStatisticsConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.statistics.impl.sql.SQLStatisticsConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.security.impl.OpenIdConfig getOpenIdConfig2(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.security.impl.OpenIdConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.lifecycle.impl.LifecycleConfig getLifecycleConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.lifecycle.impl.LifecycleConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.core.receive.ReceiveDataConfig getReceiveDataConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.core.receive.ReceiveDataConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.pipeline.destination.AppenderConfig getAppenderConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.pipeline.destination.AppenderConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.meta.impl.MetaServiceConfig getMetaServiceConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.meta.impl.MetaServiceConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.ui.config.shared.UiConfig getUiConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.ui.config.shared.UiConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.query.common.v2.ResultStoreLmdbConfig getResultStoreLmdbConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.query.common.v2.ResultStoreLmdbConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.pipeline.PipelineConfig getPipelineConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.pipeline.PipelineConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.config.common.PublicUriConfig getPublicUriConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.config.common.PublicUriConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.config.app.StatisticsConfig getStatisticsConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.config.app.StatisticsConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.ui.config.shared.UiPreferences getUiPreferences(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.ui.config.shared.UiPreferences.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.config.app.SessionCookieConfig getSessionCookieConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.config.app.SessionCookieConfig.class);
    }

    @Provides
    @SuppressWarnings("unused")
    stroom.annotation.impl.AnnotationConfig getAnnotationConfig(
            final ConfigMapper configMapper) {
        return configMapper.getConfigObject(
                stroom.annotation.impl.AnnotationConfig.class);
    }

    // END These methods are generated by stroom.config.global.impl.TestGlobalConfigModule

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        return o != null && getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    private static class PropertyCacheReload extends RunnableWrapper {

        @Inject
        PropertyCacheReload(final GlobalConfigService globalConfigService) {
            super(globalConfigService::updateConfigObjects);
        }
    }
}
