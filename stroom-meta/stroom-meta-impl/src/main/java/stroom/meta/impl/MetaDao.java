package stroom.meta.impl;

import stroom.dashboard.expression.v1.ValuesConsumer;
import stroom.data.retention.api.DataRetentionRuleAction;
import stroom.data.retention.shared.DataRetentionDeleteSummary;
import stroom.data.retention.shared.DataRetentionRules;
import stroom.data.retention.shared.FindDataRetentionImpactCriteria;
import stroom.datasource.api.v2.AbstractField;
import stroom.entity.shared.ExpressionCriteria;
import stroom.meta.api.EffectiveMeta;
import stroom.meta.api.EffectiveMetaDataCriteria;
import stroom.meta.api.MetaProperties;
import stroom.meta.shared.FindMetaCriteria;
import stroom.meta.shared.Meta;
import stroom.meta.shared.SelectionSummary;
import stroom.meta.shared.SimpleMeta;
import stroom.meta.shared.Status;
import stroom.util.shared.ResultPage;
import stroom.util.time.TimePeriod;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface MetaDao {

    Long getMaxId();

    Meta create(MetaProperties metaProperties);

    void search(ExpressionCriteria criteria, AbstractField[] fields, ValuesConsumer consumer);

    int count(FindMetaCriteria criteria);

    /**
     * Find meta data records that match the specified criteria.
     *
     * @param criteria The criteria to find matching meta data records with.
     * @return A list of matching meta data records.
     */
    ResultPage<Meta> find(FindMetaCriteria criteria);

    /**
     * Find meta data for reprocessing where child records match the specified criteria.
     *
     * @param criteria The criteria to find matching meta data child records with.
     * @return A list of meta data for reprocessing where child records match the specified criteria.
     */
    ResultPage<Meta> findReprocess(FindMetaCriteria criteria);

    /**
     * Get a summary of the items included by the current selection.
     *
     * @param criteria The selection criteria.
     * @return An object that provides a summary of the current selection.
     */
    SelectionSummary getSelectionSummary(FindMetaCriteria criteria);

    /**
     * Get a summary of the parent items of the current selection for reprocessing purposes.
     *
     * @param criteria The selection criteria.
     * @return An object that provides a summary of the parent items of the current selection for reprocessing purposes.
     */
    SelectionSummary getReprocessSelectionSummary(FindMetaCriteria criteria);

    int updateStatus(FindMetaCriteria criteria, Status currentStatus, Status newStatus, long statusTime);

    /**
     * Physically delete the records from the database.
     */
    int delete(Collection<Long> metaIds);

    List<DataRetentionDeleteSummary> getRetentionDeletionSummary(DataRetentionRules rules,
                                                                 FindDataRetentionImpactCriteria criteria);

    /**
     * @param ruleActions Must be sorted with highest priority rule first
     * @param period
     */
    int logicalDelete(List<DataRetentionRuleAction> ruleActions,
                      TimePeriod period);

    int getLockCount();

    /**
     * Get a distinct list of processor UUIds for meta data matching the supplied criteria.
     *
     * @param criteria The criteria to find matching meta data processor UUIds for.
     * @return A distinct list of processor UUIds for meta data matching the supplied criteria.
     */
    List<String> getProcessorUuidList(FindMetaCriteria criteria);

    List<EffectiveMeta> getEffectiveStreams(EffectiveMetaDataCriteria effectiveMetaDataCriteria);

    Set<Long> findLockedMeta(Collection<Long> metaIdCollection);

    /**
     * Get a batch of logically deleted {@link SimpleMeta} records that are older than {@code deleteThreshold}.
     * Gets a batch of the youngest ones matching that condition.
     */
    List<SimpleMeta> getLogicallyDeleted(Instant deleteThreshold,
                                         int batchSize,
                                         final Set<Long> metaIdExcludeSet);
}
