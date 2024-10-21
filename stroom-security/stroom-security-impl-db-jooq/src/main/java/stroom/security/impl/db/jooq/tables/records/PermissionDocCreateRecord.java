/*
 * This file is generated by jOOQ.
 */
package stroom.security.impl.db.jooq.tables.records;


import stroom.security.impl.db.jooq.tables.PermissionDocCreate;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;
import org.jooq.types.UByte;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class PermissionDocCreateRecord extends UpdatableRecordImpl<PermissionDocCreateRecord> implements Record4<Long, String, String, UByte> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>stroom.permission_doc_create.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>stroom.permission_doc_create.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>stroom.permission_doc_create.user_uuid</code>.
     */
    public void setUserUuid(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>stroom.permission_doc_create.user_uuid</code>.
     */
    public String getUserUuid() {
        return (String) get(1);
    }

    /**
     * Setter for <code>stroom.permission_doc_create.doc_uuid</code>.
     */
    public void setDocUuid(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>stroom.permission_doc_create.doc_uuid</code>.
     */
    public String getDocUuid() {
        return (String) get(2);
    }

    /**
     * Setter for <code>stroom.permission_doc_create.doc_type_id</code>.
     */
    public void setDocTypeId(UByte value) {
        set(3, value);
    }

    /**
     * Getter for <code>stroom.permission_doc_create.doc_type_id</code>.
     */
    public UByte getDocTypeId() {
        return (UByte) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<Long, String, String, UByte> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<Long, String, String, UByte> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return PermissionDocCreate.PERMISSION_DOC_CREATE.ID;
    }

    @Override
    public Field<String> field2() {
        return PermissionDocCreate.PERMISSION_DOC_CREATE.USER_UUID;
    }

    @Override
    public Field<String> field3() {
        return PermissionDocCreate.PERMISSION_DOC_CREATE.DOC_UUID;
    }

    @Override
    public Field<UByte> field4() {
        return PermissionDocCreate.PERMISSION_DOC_CREATE.DOC_TYPE_ID;
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
    public String component3() {
        return getDocUuid();
    }

    @Override
    public UByte component4() {
        return getDocTypeId();
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
    public String value3() {
        return getDocUuid();
    }

    @Override
    public UByte value4() {
        return getDocTypeId();
    }

    @Override
    public PermissionDocCreateRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public PermissionDocCreateRecord value2(String value) {
        setUserUuid(value);
        return this;
    }

    @Override
    public PermissionDocCreateRecord value3(String value) {
        setDocUuid(value);
        return this;
    }

    @Override
    public PermissionDocCreateRecord value4(UByte value) {
        setDocTypeId(value);
        return this;
    }

    @Override
    public PermissionDocCreateRecord values(Long value1, String value2, String value3, UByte value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PermissionDocCreateRecord
     */
    public PermissionDocCreateRecord() {
        super(PermissionDocCreate.PERMISSION_DOC_CREATE);
    }

    /**
     * Create a detached, initialised PermissionDocCreateRecord
     */
    public PermissionDocCreateRecord(Long id, String userUuid, String docUuid, UByte docTypeId) {
        super(PermissionDocCreate.PERMISSION_DOC_CREATE);

        setId(id);
        setUserUuid(userUuid);
        setDocUuid(docUuid);
        setDocTypeId(docTypeId);
        resetChangedOnNotNull();
    }
}
