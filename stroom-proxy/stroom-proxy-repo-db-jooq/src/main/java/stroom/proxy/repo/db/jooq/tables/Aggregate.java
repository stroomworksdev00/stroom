/*
 * This file is generated by jOOQ.
 */
package stroom.proxy.repo.db.jooq.tables;


import stroom.proxy.repo.db.jooq.DefaultSchema;
import stroom.proxy.repo.db.jooq.Indexes;
import stroom.proxy.repo.db.jooq.Keys;
import stroom.proxy.repo.db.jooq.tables.records.AggregateRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row7;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Aggregate extends TableImpl<AggregateRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>aggregate</code>
     */
    public static final Aggregate AGGREGATE = new Aggregate();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AggregateRecord> getRecordType() {
        return AggregateRecord.class;
    }

    /**
     * The column <code>aggregate.id</code>.
     */
    public final TableField<AggregateRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>aggregate.create_time_ms</code>.
     */
    public final TableField<AggregateRecord, Long> CREATE_TIME_MS = createField(DSL.name("create_time_ms"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>aggregate.fk_feed_id</code>.
     */
    public final TableField<AggregateRecord, Long> FK_FEED_ID = createField(DSL.name("fk_feed_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>aggregate.byte_size</code>.
     */
    public final TableField<AggregateRecord, Long> BYTE_SIZE = createField(DSL.name("byte_size"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>aggregate.items</code>.
     */
    public final TableField<AggregateRecord, Integer> ITEMS = createField(DSL.name("items"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>aggregate.complete</code>.
     */
    public final TableField<AggregateRecord, Boolean> COMPLETE = createField(DSL.name("complete"), SQLDataType.BOOLEAN.defaultValue(DSL.field("FALSE", SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>aggregate.new_position</code>.
     */
    public final TableField<AggregateRecord, Long> NEW_POSITION = createField(DSL.name("new_position"), SQLDataType.BIGINT, this, "");

    private Aggregate(Name alias, Table<AggregateRecord> aliased) {
        this(alias, aliased, null);
    }

    private Aggregate(Name alias, Table<AggregateRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>aggregate</code> table reference
     */
    public Aggregate(String alias) {
        this(DSL.name(alias), AGGREGATE);
    }

    /**
     * Create an aliased <code>aggregate</code> table reference
     */
    public Aggregate(Name alias) {
        this(alias, AGGREGATE);
    }

    /**
     * Create a <code>aggregate</code> table reference
     */
    public Aggregate() {
        this(DSL.name("aggregate"), null);
    }

    public <O extends Record> Aggregate(Table<O> child, ForeignKey<O, AggregateRecord> key) {
        super(child, key, AGGREGATE);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.NEW_POSITION_AGGREGATE_INDEX);
    }

    @Override
    public UniqueKey<AggregateRecord> getPrimaryKey() {
        return Keys.AGGREGATE__;
    }

    @Override
    public Aggregate as(String alias) {
        return new Aggregate(DSL.name(alias), this);
    }

    @Override
    public Aggregate as(Name alias) {
        return new Aggregate(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Aggregate rename(String name) {
        return new Aggregate(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Aggregate rename(Name name) {
        return new Aggregate(name, null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<Long, Long, Long, Long, Integer, Boolean, Long> fieldsRow() {
        return (Row7) super.fieldsRow();
    }
}