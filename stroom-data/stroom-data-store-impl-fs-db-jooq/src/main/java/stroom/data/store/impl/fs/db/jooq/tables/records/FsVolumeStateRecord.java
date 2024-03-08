/*
 * This file is generated by jOOQ.
 */
package stroom.data.store.impl.fs.db.jooq.tables.records;


import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;

import stroom.data.store.impl.fs.db.jooq.tables.FsVolumeState;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FsVolumeStateRecord extends UpdatableRecordImpl<FsVolumeStateRecord> implements Record6<Integer, Integer, Long, Long, Long, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>stroom.fs_volume_state.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>stroom.fs_volume_state.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>stroom.fs_volume_state.version</code>.
     */
    public void setVersion(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>stroom.fs_volume_state.version</code>.
     */
    public Integer getVersion() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>stroom.fs_volume_state.bytes_used</code>.
     */
    public void setBytesUsed(Long value) {
        set(2, value);
    }

    /**
     * Getter for <code>stroom.fs_volume_state.bytes_used</code>.
     */
    public Long getBytesUsed() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>stroom.fs_volume_state.bytes_free</code>.
     */
    public void setBytesFree(Long value) {
        set(3, value);
    }

    /**
     * Getter for <code>stroom.fs_volume_state.bytes_free</code>.
     */
    public Long getBytesFree() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>stroom.fs_volume_state.bytes_total</code>.
     */
    public void setBytesTotal(Long value) {
        set(4, value);
    }

    /**
     * Getter for <code>stroom.fs_volume_state.bytes_total</code>.
     */
    public Long getBytesTotal() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>stroom.fs_volume_state.update_time_ms</code>.
     */
    public void setUpdateTimeMs(Long value) {
        set(5, value);
    }

    /**
     * Getter for <code>stroom.fs_volume_state.update_time_ms</code>.
     */
    public Long getUpdateTimeMs() {
        return (Long) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row6<Integer, Integer, Long, Long, Long, Long> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    public Row6<Integer, Integer, Long, Long, Long, Long> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return FsVolumeState.FS_VOLUME_STATE.ID;
    }

    @Override
    public Field<Integer> field2() {
        return FsVolumeState.FS_VOLUME_STATE.VERSION;
    }

    @Override
    public Field<Long> field3() {
        return FsVolumeState.FS_VOLUME_STATE.BYTES_USED;
    }

    @Override
    public Field<Long> field4() {
        return FsVolumeState.FS_VOLUME_STATE.BYTES_FREE;
    }

    @Override
    public Field<Long> field5() {
        return FsVolumeState.FS_VOLUME_STATE.BYTES_TOTAL;
    }

    @Override
    public Field<Long> field6() {
        return FsVolumeState.FS_VOLUME_STATE.UPDATE_TIME_MS;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public Integer component2() {
        return getVersion();
    }

    @Override
    public Long component3() {
        return getBytesUsed();
    }

    @Override
    public Long component4() {
        return getBytesFree();
    }

    @Override
    public Long component5() {
        return getBytesTotal();
    }

    @Override
    public Long component6() {
        return getUpdateTimeMs();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public Integer value2() {
        return getVersion();
    }

    @Override
    public Long value3() {
        return getBytesUsed();
    }

    @Override
    public Long value4() {
        return getBytesFree();
    }

    @Override
    public Long value5() {
        return getBytesTotal();
    }

    @Override
    public Long value6() {
        return getUpdateTimeMs();
    }

    @Override
    public FsVolumeStateRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public FsVolumeStateRecord value2(Integer value) {
        setVersion(value);
        return this;
    }

    @Override
    public FsVolumeStateRecord value3(Long value) {
        setBytesUsed(value);
        return this;
    }

    @Override
    public FsVolumeStateRecord value4(Long value) {
        setBytesFree(value);
        return this;
    }

    @Override
    public FsVolumeStateRecord value5(Long value) {
        setBytesTotal(value);
        return this;
    }

    @Override
    public FsVolumeStateRecord value6(Long value) {
        setUpdateTimeMs(value);
        return this;
    }

    @Override
    public FsVolumeStateRecord values(Integer value1, Integer value2, Long value3, Long value4, Long value5, Long value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached FsVolumeStateRecord
     */
    public FsVolumeStateRecord() {
        super(FsVolumeState.FS_VOLUME_STATE);
    }

    /**
     * Create a detached, initialised FsVolumeStateRecord
     */
    public FsVolumeStateRecord(Integer id, Integer version, Long bytesUsed, Long bytesFree, Long bytesTotal, Long updateTimeMs) {
        super(FsVolumeState.FS_VOLUME_STATE);

        setId(id);
        setVersion(version);
        setBytesUsed(bytesUsed);
        setBytesFree(bytesFree);
        setBytesTotal(bytesTotal);
        setUpdateTimeMs(updateTimeMs);
    }
}
