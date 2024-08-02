/*
 * This file is generated by jOOQ.
 */
package stroom.security.impl.db.jooq.tables.records;


import stroom.security.impl.db.jooq.tables.PermissionApp;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;
import org.jooq.types.UByte;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class PermissionAppRecord extends UpdatableRecordImpl<PermissionAppRecord> implements Record3<Long, String, UByte> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>stroom.permission_app.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>stroom.permission_app.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>stroom.permission_app.user_uuid</code>.
     */
    public void setUserUuid(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>stroom.permission_app.user_uuid</code>.
     */
    public String getUserUuid() {
        return (String) get(1);
    }

    /**
     * Setter for <code>stroom.permission_app.permission_id</code>.
     */
    public void setPermissionId(UByte value) {
        set(2, value);
    }

    /**
     * Getter for <code>stroom.permission_app.permission_id</code>.
     */
    public UByte getPermissionId() {
        return (UByte) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row3<Long, String, UByte> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<Long, String, UByte> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return PermissionApp.PERMISSION_APP.ID;
    }

    @Override
    public Field<String> field2() {
        return PermissionApp.PERMISSION_APP.USER_UUID;
    }

    @Override
    public Field<UByte> field3() {
        return PermissionApp.PERMISSION_APP.PERMISSION_ID;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getUserUuid();
    }

    @Override
    public UByte component3() {
        return getPermissionId();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getUserUuid();
    }

    @Override
    public UByte value3() {
        return getPermissionId();
    }

    @Override
    public PermissionAppRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public PermissionAppRecord value2(String value) {
        setUserUuid(value);
        return this;
    }

    @Override
    public PermissionAppRecord value3(UByte value) {
        setPermissionId(value);
        return this;
    }

    @Override
    public PermissionAppRecord values(Long value1, String value2, UByte value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PermissionAppRecord
     */
    public PermissionAppRecord() {
        super(PermissionApp.PERMISSION_APP);
    }

    /**
     * Create a detached, initialised PermissionAppRecord
     */
    public PermissionAppRecord(Long id, String userUuid, UByte permissionId) {
        super(PermissionApp.PERMISSION_APP);

        setId(id);
        setUserUuid(userUuid);
        setPermissionId(permissionId);
        resetChangedOnNotNull();
    }
}
