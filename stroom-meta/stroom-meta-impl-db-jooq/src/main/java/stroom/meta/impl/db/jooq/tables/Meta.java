/*
 * This file is generated by jOOQ.
 */
package stroom.meta.impl.db.jooq.tables;


import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row12;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import stroom.meta.impl.db.jooq.Indexes;
import stroom.meta.impl.db.jooq.Keys;
import stroom.meta.impl.db.jooq.Stroom;
import stroom.meta.impl.db.jooq.tables.records.MetaRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Meta extends TableImpl<MetaRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>stroom.meta</code>
     */
    public static final Meta META = new Meta();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<MetaRecord> getRecordType() {
        return MetaRecord.class;
    }

    /**
     * The column <code>stroom.meta.id</code>.
     */
    public final TableField<MetaRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>stroom.meta.create_time</code>.
     */
    public final TableField<MetaRecord, Long> CREATE_TIME = createField(DSL.name("create_time"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>stroom.meta.effective_time</code>.
     */
    public final TableField<MetaRecord, Long> EFFECTIVE_TIME = createField(DSL.name("effective_time"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>stroom.meta.parent_id</code>.
     */
    public final TableField<MetaRecord, Long> PARENT_ID = createField(DSL.name("parent_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>stroom.meta.status</code>.
     */
    public final TableField<MetaRecord, Byte> STATUS = createField(DSL.name("status"), SQLDataType.TINYINT.nullable(false), this, "");

    /**
     * The column <code>stroom.meta.status_time</code>.
     */
    public final TableField<MetaRecord, Long> STATUS_TIME = createField(DSL.name("status_time"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>stroom.meta.feed_id</code>.
     */
    public final TableField<MetaRecord, Integer> FEED_ID = createField(DSL.name("feed_id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>stroom.meta.type_id</code>.
     */
    public final TableField<MetaRecord, Integer> TYPE_ID = createField(DSL.name("type_id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>stroom.meta.processor_id</code>.
     */
    public final TableField<MetaRecord, Integer> PROCESSOR_ID = createField(DSL.name("processor_id"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>stroom.meta.processor_filter_id</code>.
     */
    public final TableField<MetaRecord, Integer> PROCESSOR_FILTER_ID = createField(DSL.name("processor_filter_id"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>stroom.meta.processor_task_id</code>.
     */
    public final TableField<MetaRecord, Long> PROCESSOR_TASK_ID = createField(DSL.name("processor_task_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>stroom.meta.reprocessed_stream_id</code>.
     */
    public final TableField<MetaRecord, Long> REPROCESSED_STREAM_ID = createField(DSL.name("reprocessed_stream_id"), SQLDataType.BIGINT, this, "");

    private Meta(Name alias, Table<MetaRecord> aliased) {
        this(alias, aliased, null);
    }

    private Meta(Name alias, Table<MetaRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>stroom.meta</code> table reference
     */
    public Meta(String alias) {
        this(DSL.name(alias), META);
    }

    /**
     * Create an aliased <code>stroom.meta</code> table reference
     */
    public Meta(Name alias) {
        this(alias, META);
    }

    /**
     * Create a <code>stroom.meta</code> table reference
     */
    public Meta() {
        this(DSL.name("meta"), null);
    }

    public <O extends Record> Meta(Table<O> child, ForeignKey<O, MetaRecord> key) {
        super(child, key, META);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Stroom.STROOM;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.META_META_CREATE_TIME, Indexes.META_META_FEED_ID_CREATE_TIME, Indexes.META_META_FEED_ID_EFFECTIVE_TIME, Indexes.META_META_PARENT_ID, Indexes.META_META_PROCESSOR_ID_CREATE_TIME, Indexes.META_META_STATUS);
    }

    @Override
    public Identity<MetaRecord, Long> getIdentity() {
        return (Identity<MetaRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<MetaRecord> getPrimaryKey() {
        return Keys.KEY_META_PRIMARY;
    }

    @Override
    public List<ForeignKey<MetaRecord, ?>> getReferences() {
        return Arrays.asList(Keys.META_FEED_ID, Keys.META_TYPE_ID, Keys.META_PROCESSOR_ID);
    }

    private transient MetaFeed _metaFeed;
    private transient MetaType _metaType;
    private transient MetaProcessor _metaProcessor;

    /**
     * Get the implicit join path to the <code>stroom.meta_feed</code> table.
     */
    public MetaFeed metaFeed() {
        if (_metaFeed == null)
            _metaFeed = new MetaFeed(this, Keys.META_FEED_ID);

        return _metaFeed;
    }

    /**
     * Get the implicit join path to the <code>stroom.meta_type</code> table.
     */
    public MetaType metaType() {
        if (_metaType == null)
            _metaType = new MetaType(this, Keys.META_TYPE_ID);

        return _metaType;
    }

    /**
     * Get the implicit join path to the <code>stroom.meta_processor</code>
     * table.
     */
    public MetaProcessor metaProcessor() {
        if (_metaProcessor == null)
            _metaProcessor = new MetaProcessor(this, Keys.META_PROCESSOR_ID);

        return _metaProcessor;
    }

    @Override
    public Meta as(String alias) {
        return new Meta(DSL.name(alias), this);
    }

    @Override
    public Meta as(Name alias) {
        return new Meta(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Meta rename(String name) {
        return new Meta(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Meta rename(Name name) {
        return new Meta(name, null);
    }

    // -------------------------------------------------------------------------
    // Row12 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row12<Long, Long, Long, Long, Byte, Long, Integer, Integer, Integer, Integer, Long, Long> fieldsRow() {
        return (Row12) super.fieldsRow();
    }
}
