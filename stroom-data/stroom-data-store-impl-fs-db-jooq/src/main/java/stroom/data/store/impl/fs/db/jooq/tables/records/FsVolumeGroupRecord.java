/*
 * This file is generated by jOOQ.
 */
package stroom.data.store.impl.fs.db.jooq.tables.records;


import stroom.data.store.impl.fs.db.jooq.tables.FsVolumeGroup;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FsVolumeGroupRecord extends UpdatableRecordImpl<FsVolumeGroupRecord> implements Record7<Integer, Integer, Long, String, Long, String, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>stroom.fs_volume_group.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>stroom.fs_volume_group.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>stroom.fs_volume_group.version</code>.
     */
    public void setVersion(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>stroom.fs_volume_group.version</code>.
     */
    public Integer getVersion() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>stroom.fs_volume_group.create_time_ms</code>.
     */
    public void setCreateTimeMs(Long value) {
        set(2, value);
    }

    /**
     * Getter for <code>stroom.fs_volume_group.create_time_ms</code>.
     */
    public Long getCreateTimeMs() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>stroom.fs_volume_group.create_user</code>.
     */
    public void setCreateUser(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>stroom.fs_volume_group.create_user</code>.
     */
    public String getCreateUser() {
        return (String) get(3);
    }

    /**
     * Setter for <code>stroom.fs_volume_group.update_time_ms</code>.
     */
    public void setUpdateTimeMs(Long value) {
        set(4, value);
    }

    /**
     * Getter for <code>stroom.fs_volume_group.update_time_ms</code>.
     */
    public Long getUpdateTimeMs() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>stroom.fs_volume_group.update_user</code>.
     */
    public void setUpdateUser(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>stroom.fs_volume_group.update_user</code>.
     */
    public String getUpdateUser() {
        return (String) get(5);
    }

    /**
     * Setter for <code>stroom.fs_volume_group.name</code>.
     */
    public void setName(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>stroom.fs_volume_group.name</code>.
     */
    public String getName() {
        return (String) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<Integer, Integer, Long, String, Long, String, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<Integer, Integer, Long, String, Long, String, String> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return FsVolumeGroup.FS_VOLUME_GROUP.ID;
    }

    @Override
    public Field<Integer> field2() {
        return FsVolumeGroup.FS_VOLUME_GROUP.VERSION;
    }

    @Override
    public Field<Long> field3() {
        return FsVolumeGroup.FS_VOLUME_GROUP.CREATE_TIME_MS;
    }

    @Override
    public Field<String> field4() {
        return FsVolumeGroup.FS_VOLUME_GROUP.CREATE_USER;
    }

    @Override
    public Field<Long> field5() {
        return FsVolumeGroup.FS_VOLUME_GROUP.UPDATE_TIME_MS;
    }

    @Override
    public Field<String> field6() {
        return FsVolumeGroup.FS_VOLUME_GROUP.UPDATE_USER;
    }

    @Override
    public Field<String> field7() {
        return FsVolumeGroup.FS_VOLUME_GROUP.NAME;
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
        return getCreateTimeMs();
    }

    @Override
    public String component4() {
        return getCreateUser();
    }

    @Override
    public Long component5() {
        return getUpdateTimeMs();
    }

    @Override
    public String component6() {
        return getUpdateUser();
    }

    @Override
    public String component7() {
        return getName();
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
        return getCreateTimeMs();
    }

    @Override
    public String value4() {
        return getCreateUser();
    }

    @Override
    public Long value5() {
        return getUpdateTimeMs();
    }

    @Override
    public String value6() {
        return getUpdateUser();
    }

    @Override
    public String value7() {
        return getName();
    }

    @Override
    public FsVolumeGroupRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public FsVolumeGroupRecord value2(Integer value) {
        setVersion(value);
        return this;
    }

    @Override
    public FsVolumeGroupRecord value3(Long value) {
        setCreateTimeMs(value);
        return this;
    }

    @Override
    public FsVolumeGroupRecord value4(String value) {
        setCreateUser(value);
        return this;
    }

    @Override
    public FsVolumeGroupRecord value5(Long value) {
        setUpdateTimeMs(value);
        return this;
    }

    @Override
    public FsVolumeGroupRecord value6(String value) {
        setUpdateUser(value);
        return this;
    }

    @Override
    public FsVolumeGroupRecord value7(String value) {
        setName(value);
        return this;
    }

    @Override
    public FsVolumeGroupRecord values(Integer value1, Integer value2, Long value3, String value4, Long value5, String value6, String value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached FsVolumeGroupRecord
     */
    public FsVolumeGroupRecord() {
        super(FsVolumeGroup.FS_VOLUME_GROUP);
    }

    /**
     * Create a detached, initialised FsVolumeGroupRecord
     */
    public FsVolumeGroupRecord(Integer id, Integer version, Long createTimeMs, String createUser, Long updateTimeMs, String updateUser, String name) {
        super(FsVolumeGroup.FS_VOLUME_GROUP);

        setId(id);
        setVersion(version);
        setCreateTimeMs(createTimeMs);
        setCreateUser(createUser);
        setUpdateTimeMs(updateTimeMs);
        setUpdateUser(updateUser);
        setName(name);
    }
}
