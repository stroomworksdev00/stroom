/*
 * This file is generated by jOOQ.
 */
package stroom.security.impl.db.jooq.tables;


import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function3;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row3;
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

import stroom.security.impl.db.jooq.Keys;
import stroom.security.impl.db.jooq.Stroom;
import stroom.security.impl.db.jooq.tables.records.PermissionAppRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class PermissionApp extends TableImpl<PermissionAppRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>stroom.permission_app</code>
     */
    public static final PermissionApp PERMISSION_APP = new PermissionApp();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PermissionAppRecord> getRecordType() {
        return PermissionAppRecord.class;
    }

    /**
     * The column <code>stroom.permission_app.id</code>.
     */
    public final TableField<PermissionAppRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>stroom.permission_app.user_uuid</code>.
     */
    public final TableField<PermissionAppRecord, String> USER_UUID = createField(DSL.name("user_uuid"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>stroom.permission_app.permission_id</code>.
     */
    public final TableField<PermissionAppRecord, UByte> PERMISSION_ID = createField(DSL.name("permission_id"), SQLDataType.TINYINTUNSIGNED.nullable(false), this, "");

    private PermissionApp(Name alias, Table<PermissionAppRecord> aliased) {
        this(alias, aliased, null);
    }

    private PermissionApp(Name alias, Table<PermissionAppRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>stroom.permission_app</code> table reference
     */
    public PermissionApp(String alias) {
        this(DSL.name(alias), PERMISSION_APP);
    }

    /**
     * Create an aliased <code>stroom.permission_app</code> table reference
     */
    public PermissionApp(Name alias) {
        this(alias, PERMISSION_APP);
    }

    /**
     * Create a <code>stroom.permission_app</code> table reference
     */
    public PermissionApp() {
        this(DSL.name("permission_app"), null);
    }

    public <O extends Record> PermissionApp(Table<O> child, ForeignKey<O, PermissionAppRecord> key) {
        super(child, key, PERMISSION_APP);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Stroom.STROOM;
    }

    @Override
    public Identity<PermissionAppRecord, Long> getIdentity() {
        return (Identity<PermissionAppRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<PermissionAppRecord> getPrimaryKey() {
        return Keys.KEY_PERMISSION_APP_PRIMARY;
    }

    @Override
    public List<UniqueKey<PermissionAppRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.KEY_PERMISSION_APP_PERMISSION_APP_USER_UUID_PERMISSION_ID_IDX);
    }

    @Override
    public List<ForeignKey<PermissionAppRecord, ?>> getReferences() {
        return Arrays.asList(Keys.PERMISSION_APP_USER_UUID, Keys.PERMISSION_APP_PERMISSION_ID);
    }

    private transient StroomUser _stroomUser;
    private transient PermissionAppId _permissionAppId;

    /**
     * Get the implicit join path to the
     * <code>stroom_oidc_v7_6.stroom_user</code> table.
     */
    public StroomUser stroomUser() {
        if (_stroomUser == null)
            _stroomUser = new StroomUser(this, Keys.PERMISSION_APP_USER_UUID);

        return _stroomUser;
    }

    /**
     * Get the implicit join path to the
     * <code>stroom_oidc_v7_6.permission_app_id</code> table.
     */
    public PermissionAppId permissionAppId() {
        if (_permissionAppId == null)
            _permissionAppId = new PermissionAppId(this, Keys.PERMISSION_APP_PERMISSION_ID);

        return _permissionAppId;
    }

    @Override
    public PermissionApp as(String alias) {
        return new PermissionApp(DSL.name(alias), this);
    }

    @Override
    public PermissionApp as(Name alias) {
        return new PermissionApp(alias, this);
    }

    @Override
    public PermissionApp as(Table<?> alias) {
        return new PermissionApp(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public PermissionApp rename(String name) {
        return new PermissionApp(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PermissionApp rename(Name name) {
        return new PermissionApp(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public PermissionApp rename(Table<?> name) {
        return new PermissionApp(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<Long, String, UByte> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function3<? super Long, ? super String, ? super UByte, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function3<? super Long, ? super String, ? super UByte, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
