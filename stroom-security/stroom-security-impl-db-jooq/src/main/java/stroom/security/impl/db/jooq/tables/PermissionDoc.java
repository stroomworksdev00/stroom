/*
 * This file is generated by jOOQ.
 */
package stroom.security.impl.db.jooq.tables;


import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function4;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row4;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import org.jooq.types.UByte;

import stroom.security.impl.db.jooq.Indexes;
import stroom.security.impl.db.jooq.Keys;
import stroom.security.impl.db.jooq.Stroom;
import stroom.security.impl.db.jooq.tables.records.PermissionDocRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class PermissionDoc extends TableImpl<PermissionDocRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>stroom.permission_doc</code>
     */
    public static final PermissionDoc PERMISSION_DOC = new PermissionDoc();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PermissionDocRecord> getRecordType() {
        return PermissionDocRecord.class;
    }

    /**
     * The column <code>stroom.permission_doc.id</code>.
     */
    public final TableField<PermissionDocRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>stroom.permission_doc.user_uuid</code>.
     */
    public final TableField<PermissionDocRecord, String> USER_UUID = createField(DSL.name("user_uuid"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>stroom.permission_doc.doc_uuid</code>.
     */
    public final TableField<PermissionDocRecord, String> DOC_UUID = createField(DSL.name("doc_uuid"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>stroom.permission_doc.permission_id</code>.
     */
    public final TableField<PermissionDocRecord, UByte> PERMISSION_ID = createField(DSL.name("permission_id"), SQLDataType.TINYINTUNSIGNED.nullable(false), this, "");

    private PermissionDoc(Name alias, Table<PermissionDocRecord> aliased) {
        this(alias, aliased, null);
    }

    private PermissionDoc(Name alias, Table<PermissionDocRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>stroom.permission_doc</code> table reference
     */
    public PermissionDoc(String alias) {
        this(DSL.name(alias), PERMISSION_DOC);
    }

    /**
     * Create an aliased <code>stroom.permission_doc</code> table reference
     */
    public PermissionDoc(Name alias) {
        this(alias, PERMISSION_DOC);
    }

    /**
     * Create a <code>stroom.permission_doc</code> table reference
     */
    public PermissionDoc() {
        this(DSL.name("permission_doc"), null);
    }

    public <O extends Record> PermissionDoc(Table<O> child, ForeignKey<O, PermissionDocRecord> key) {
        super(child, key, PERMISSION_DOC);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Stroom.STROOM;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.PERMISSION_DOC_PERMISSION_DOC_DOC_UUID);
    }

    @Override
    public Identity<PermissionDocRecord, Long> getIdentity() {
        return (Identity<PermissionDocRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<PermissionDocRecord> getPrimaryKey() {
        return Keys.KEY_PERMISSION_DOC_PRIMARY;
    }

    @Override
    public List<UniqueKey<PermissionDocRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.KEY_PERMISSION_DOC_PERMISSION_DOC_USER_UUID_DOC_UUID_IDX);
    }

    @Override
    public List<ForeignKey<PermissionDocRecord, ?>> getReferences() {
        return Arrays.asList(Keys.PERMISSION_DOC_USER_UUID, Keys.PERMISSION_DOC_PERMISSION_ID);
    }

    private transient StroomUser _stroomUser;
    private transient PermissionDocId _permissionDocId;

    /**
     * Get the implicit join path to the
     * <code>stroom_oidc_v7_6.stroom_user</code> table.
     */
    public StroomUser stroomUser() {
        if (_stroomUser == null)
            _stroomUser = new StroomUser(this, Keys.PERMISSION_DOC_USER_UUID);

        return _stroomUser;
    }

    /**
     * Get the implicit join path to the
     * <code>stroom_oidc_v7_6.permission_doc_id</code> table.
     */
    public PermissionDocId permissionDocId() {
        if (_permissionDocId == null)
            _permissionDocId = new PermissionDocId(this, Keys.PERMISSION_DOC_PERMISSION_ID);

        return _permissionDocId;
    }

    @Override
    public PermissionDoc as(String alias) {
        return new PermissionDoc(DSL.name(alias), this);
    }

    @Override
    public PermissionDoc as(Name alias) {
        return new PermissionDoc(alias, this);
    }

    @Override
    public PermissionDoc as(Table<?> alias) {
        return new PermissionDoc(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public PermissionDoc rename(String name) {
        return new PermissionDoc(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PermissionDoc rename(Name name) {
        return new PermissionDoc(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public PermissionDoc rename(Table<?> name) {
        return new PermissionDoc(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<Long, String, String, UByte> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function4<? super Long, ? super String, ? super String, ? super UByte, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function4<? super Long, ? super String, ? super String, ? super UByte, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
