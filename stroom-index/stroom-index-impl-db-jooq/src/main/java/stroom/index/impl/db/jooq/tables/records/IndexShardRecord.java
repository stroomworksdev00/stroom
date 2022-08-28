/*
 * This file is generated by jOOQ.
 */
package stroom.index.impl.db.jooq.tables.records;


import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record14;
import org.jooq.Row14;
import org.jooq.impl.UpdatableRecordImpl;

import stroom.index.impl.db.jooq.tables.IndexShard;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class IndexShardRecord extends UpdatableRecordImpl<IndexShardRecord> implements Record14<Long, String, Integer, String, Integer, Long, Long, Integer, Long, Byte, String, String, Long, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>stroom.index_shard.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>stroom.index_shard.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>stroom.index_shard.node_name</code>.
     */
    public void setNodeName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>stroom.index_shard.node_name</code>.
     */
    public String getNodeName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>stroom.index_shard.fk_volume_id</code>.
     */
    public void setFkVolumeId(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>stroom.index_shard.fk_volume_id</code>.
     */
    public Integer getFkVolumeId() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>stroom.index_shard.index_uuid</code>.
     */
    public void setIndexUuid(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>stroom.index_shard.index_uuid</code>.
     */
    public String getIndexUuid() {
        return (String) get(3);
    }

    /**
     * Setter for <code>stroom.index_shard.commit_document_count</code>.
     */
    public void setCommitDocumentCount(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>stroom.index_shard.commit_document_count</code>.
     */
    public Integer getCommitDocumentCount() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>stroom.index_shard.commit_duration_ms</code>.
     */
    public void setCommitDurationMs(Long value) {
        set(5, value);
    }

    /**
     * Getter for <code>stroom.index_shard.commit_duration_ms</code>.
     */
    public Long getCommitDurationMs() {
        return (Long) get(5);
    }

    /**
     * Setter for <code>stroom.index_shard.commit_ms</code>.
     */
    public void setCommitMs(Long value) {
        set(6, value);
    }

    /**
     * Getter for <code>stroom.index_shard.commit_ms</code>.
     */
    public Long getCommitMs() {
        return (Long) get(6);
    }

    /**
     * Setter for <code>stroom.index_shard.document_count</code>.
     */
    public void setDocumentCount(Integer value) {
        set(7, value);
    }

    /**
     * Getter for <code>stroom.index_shard.document_count</code>.
     */
    public Integer getDocumentCount() {
        return (Integer) get(7);
    }

    /**
     * Setter for <code>stroom.index_shard.file_size</code>.
     */
    public void setFileSize(Long value) {
        set(8, value);
    }

    /**
     * Getter for <code>stroom.index_shard.file_size</code>.
     */
    public Long getFileSize() {
        return (Long) get(8);
    }

    /**
     * Setter for <code>stroom.index_shard.status</code>.
     */
    public void setStatus(Byte value) {
        set(9, value);
    }

    /**
     * Getter for <code>stroom.index_shard.status</code>.
     */
    public Byte getStatus() {
        return (Byte) get(9);
    }

    /**
     * Setter for <code>stroom.index_shard.index_version</code>.
     */
    public void setIndexVersion(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>stroom.index_shard.index_version</code>.
     */
    public String getIndexVersion() {
        return (String) get(10);
    }

    /**
     * Setter for <code>stroom.index_shard.partition_name</code>.
     */
    public void setPartitionName(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>stroom.index_shard.partition_name</code>.
     */
    public String getPartitionName() {
        return (String) get(11);
    }

    /**
     * Setter for <code>stroom.index_shard.partition_from_ms</code>.
     */
    public void setPartitionFromMs(Long value) {
        set(12, value);
    }

    /**
     * Getter for <code>stroom.index_shard.partition_from_ms</code>.
     */
    public Long getPartitionFromMs() {
        return (Long) get(12);
    }

    /**
     * Setter for <code>stroom.index_shard.partition_to_ms</code>.
     */
    public void setPartitionToMs(Long value) {
        set(13, value);
    }

    /**
     * Getter for <code>stroom.index_shard.partition_to_ms</code>.
     */
    public Long getPartitionToMs() {
        return (Long) get(13);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record14 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row14<Long, String, Integer, String, Integer, Long, Long, Integer, Long, Byte, String, String, Long, Long> fieldsRow() {
        return (Row14) super.fieldsRow();
    }

    @Override
    public Row14<Long, String, Integer, String, Integer, Long, Long, Integer, Long, Byte, String, String, Long, Long> valuesRow() {
        return (Row14) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return IndexShard.INDEX_SHARD.ID;
    }

    @Override
    public Field<String> field2() {
        return IndexShard.INDEX_SHARD.NODE_NAME;
    }

    @Override
    public Field<Integer> field3() {
        return IndexShard.INDEX_SHARD.FK_VOLUME_ID;
    }

    @Override
    public Field<String> field4() {
        return IndexShard.INDEX_SHARD.INDEX_UUID;
    }

    @Override
    public Field<Integer> field5() {
        return IndexShard.INDEX_SHARD.COMMIT_DOCUMENT_COUNT;
    }

    @Override
    public Field<Long> field6() {
        return IndexShard.INDEX_SHARD.COMMIT_DURATION_MS;
    }

    @Override
    public Field<Long> field7() {
        return IndexShard.INDEX_SHARD.COMMIT_MS;
    }

    @Override
    public Field<Integer> field8() {
        return IndexShard.INDEX_SHARD.DOCUMENT_COUNT;
    }

    @Override
    public Field<Long> field9() {
        return IndexShard.INDEX_SHARD.FILE_SIZE;
    }

    @Override
    public Field<Byte> field10() {
        return IndexShard.INDEX_SHARD.STATUS;
    }

    @Override
    public Field<String> field11() {
        return IndexShard.INDEX_SHARD.INDEX_VERSION;
    }

    @Override
    public Field<String> field12() {
        return IndexShard.INDEX_SHARD.PARTITION_NAME;
    }

    @Override
    public Field<Long> field13() {
        return IndexShard.INDEX_SHARD.PARTITION_FROM_MS;
    }

    @Override
    public Field<Long> field14() {
        return IndexShard.INDEX_SHARD.PARTITION_TO_MS;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getNodeName();
    }

    @Override
    public Integer component3() {
        return getFkVolumeId();
    }

    @Override
    public String component4() {
        return getIndexUuid();
    }

    @Override
    public Integer component5() {
        return getCommitDocumentCount();
    }

    @Override
    public Long component6() {
        return getCommitDurationMs();
    }

    @Override
    public Long component7() {
        return getCommitMs();
    }

    @Override
    public Integer component8() {
        return getDocumentCount();
    }

    @Override
    public Long component9() {
        return getFileSize();
    }

    @Override
    public Byte component10() {
        return getStatus();
    }

    @Override
    public String component11() {
        return getIndexVersion();
    }

    @Override
    public String component12() {
        return getPartitionName();
    }

    @Override
    public Long component13() {
        return getPartitionFromMs();
    }

    @Override
    public Long component14() {
        return getPartitionToMs();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getNodeName();
    }

    @Override
    public Integer value3() {
        return getFkVolumeId();
    }

    @Override
    public String value4() {
        return getIndexUuid();
    }

    @Override
    public Integer value5() {
        return getCommitDocumentCount();
    }

    @Override
    public Long value6() {
        return getCommitDurationMs();
    }

    @Override
    public Long value7() {
        return getCommitMs();
    }

    @Override
    public Integer value8() {
        return getDocumentCount();
    }

    @Override
    public Long value9() {
        return getFileSize();
    }

    @Override
    public Byte value10() {
        return getStatus();
    }

    @Override
    public String value11() {
        return getIndexVersion();
    }

    @Override
    public String value12() {
        return getPartitionName();
    }

    @Override
    public Long value13() {
        return getPartitionFromMs();
    }

    @Override
    public Long value14() {
        return getPartitionToMs();
    }

    @Override
    public IndexShardRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public IndexShardRecord value2(String value) {
        setNodeName(value);
        return this;
    }

    @Override
    public IndexShardRecord value3(Integer value) {
        setFkVolumeId(value);
        return this;
    }

    @Override
    public IndexShardRecord value4(String value) {
        setIndexUuid(value);
        return this;
    }

    @Override
    public IndexShardRecord value5(Integer value) {
        setCommitDocumentCount(value);
        return this;
    }

    @Override
    public IndexShardRecord value6(Long value) {
        setCommitDurationMs(value);
        return this;
    }

    @Override
    public IndexShardRecord value7(Long value) {
        setCommitMs(value);
        return this;
    }

    @Override
    public IndexShardRecord value8(Integer value) {
        setDocumentCount(value);
        return this;
    }

    @Override
    public IndexShardRecord value9(Long value) {
        setFileSize(value);
        return this;
    }

    @Override
    public IndexShardRecord value10(Byte value) {
        setStatus(value);
        return this;
    }

    @Override
    public IndexShardRecord value11(String value) {
        setIndexVersion(value);
        return this;
    }

    @Override
    public IndexShardRecord value12(String value) {
        setPartitionName(value);
        return this;
    }

    @Override
    public IndexShardRecord value13(Long value) {
        setPartitionFromMs(value);
        return this;
    }

    @Override
    public IndexShardRecord value14(Long value) {
        setPartitionToMs(value);
        return this;
    }

    @Override
    public IndexShardRecord values(Long value1, String value2, Integer value3, String value4, Integer value5, Long value6, Long value7, Integer value8, Long value9, Byte value10, String value11, String value12, Long value13, Long value14) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        value14(value14);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached IndexShardRecord
     */
    public IndexShardRecord() {
        super(IndexShard.INDEX_SHARD);
    }

    /**
     * Create a detached, initialised IndexShardRecord
     */
    public IndexShardRecord(Long id, String nodeName, Integer fkVolumeId, String indexUuid, Integer commitDocumentCount, Long commitDurationMs, Long commitMs, Integer documentCount, Long fileSize, Byte status, String indexVersion, String partitionName, Long partitionFromMs, Long partitionToMs) {
        super(IndexShard.INDEX_SHARD);

        setId(id);
        setNodeName(nodeName);
        setFkVolumeId(fkVolumeId);
        setIndexUuid(indexUuid);
        setCommitDocumentCount(commitDocumentCount);
        setCommitDurationMs(commitDurationMs);
        setCommitMs(commitMs);
        setDocumentCount(documentCount);
        setFileSize(fileSize);
        setStatus(status);
        setIndexVersion(indexVersion);
        setPartitionName(partitionName);
        setPartitionFromMs(partitionFromMs);
        setPartitionToMs(partitionToMs);
    }
}
