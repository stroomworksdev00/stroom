/*
 * This file is generated by jOOQ.
 */
package stroom.cluster.lock.impl.db.jooq.tables;


import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row3;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import stroom.cluster.lock.impl.db.jooq.Keys;
import stroom.cluster.lock.impl.db.jooq.Stroom;
import stroom.cluster.lock.impl.db.jooq.tables.records.ClusterLockRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ClusterLock extends TableImpl<ClusterLockRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>stroom.cluster_lock</code>
     */
    public static final ClusterLock CLUSTER_LOCK = new ClusterLock();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ClusterLockRecord> getRecordType() {
        return ClusterLockRecord.class;
    }

    /**
     * The column <code>stroom.cluster_lock.id</code>.
     */
    public final TableField<ClusterLockRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>stroom.cluster_lock.version</code>.
     */
    public final TableField<ClusterLockRecord, Integer> VERSION = createField(DSL.name("version"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>stroom.cluster_lock.name</code>.
     */
    public final TableField<ClusterLockRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    private ClusterLock(Name alias, Table<ClusterLockRecord> aliased) {
        this(alias, aliased, null);
    }

    private ClusterLock(Name alias, Table<ClusterLockRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>stroom.cluster_lock</code> table reference
     */
    public ClusterLock(String alias) {
        this(DSL.name(alias), CLUSTER_LOCK);
    }

    /**
     * Create an aliased <code>stroom.cluster_lock</code> table reference
     */
    public ClusterLock(Name alias) {
        this(alias, CLUSTER_LOCK);
    }

    /**
     * Create a <code>stroom.cluster_lock</code> table reference
     */
    public ClusterLock() {
        this(DSL.name("cluster_lock"), null);
    }

    public <O extends Record> ClusterLock(Table<O> child, ForeignKey<O, ClusterLockRecord> key) {
        super(child, key, CLUSTER_LOCK);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Stroom.STROOM;
    }

    @Override
    public Identity<ClusterLockRecord, Integer> getIdentity() {
        return (Identity<ClusterLockRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<ClusterLockRecord> getPrimaryKey() {
        return Keys.KEY_CLUSTER_LOCK_PRIMARY;
    }

    @Override
    public List<UniqueKey<ClusterLockRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.KEY_CLUSTER_LOCK_NAME);
    }

    @Override
    public TableField<ClusterLockRecord, Integer> getRecordVersion() {
        return VERSION;
    }

    @Override
    public ClusterLock as(String alias) {
        return new ClusterLock(DSL.name(alias), this);
    }

    @Override
    public ClusterLock as(Name alias) {
        return new ClusterLock(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public ClusterLock rename(String name) {
        return new ClusterLock(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ClusterLock rename(Name name) {
        return new ClusterLock(name, null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<Integer, Integer, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }
}
