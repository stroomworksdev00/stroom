/*
 * This file is generated by jOOQ.
 */
package stroom.processor.impl.db.jooq.tables;


import stroom.processor.impl.db.jooq.Keys;
import stroom.processor.impl.db.jooq.Stroom;
import stroom.processor.impl.db.jooq.tables.records.ProcessorFeedRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function2;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row2;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class ProcessorFeed extends TableImpl<ProcessorFeedRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>stroom.processor_feed</code>
     */
    public static final ProcessorFeed PROCESSOR_FEED = new ProcessorFeed();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ProcessorFeedRecord> getRecordType() {
        return ProcessorFeedRecord.class;
    }

    /**
     * The column <code>stroom.processor_feed.id</code>.
     */
    public final TableField<ProcessorFeedRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>stroom.processor_feed.name</code>.
     */
    public final TableField<ProcessorFeedRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    private ProcessorFeed(Name alias, Table<ProcessorFeedRecord> aliased) {
        this(alias, aliased, null);
    }

    private ProcessorFeed(Name alias, Table<ProcessorFeedRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>stroom.processor_feed</code> table reference
     */
    public ProcessorFeed(String alias) {
        this(DSL.name(alias), PROCESSOR_FEED);
    }

    /**
     * Create an aliased <code>stroom.processor_feed</code> table reference
     */
    public ProcessorFeed(Name alias) {
        this(alias, PROCESSOR_FEED);
    }

    /**
     * Create a <code>stroom.processor_feed</code> table reference
     */
    public ProcessorFeed() {
        this(DSL.name("processor_feed"), null);
    }

    public <O extends Record> ProcessorFeed(Table<O> child, ForeignKey<O, ProcessorFeedRecord> key) {
        super(child, key, PROCESSOR_FEED);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Stroom.STROOM;
    }

    @Override
    public Identity<ProcessorFeedRecord, Integer> getIdentity() {
        return (Identity<ProcessorFeedRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<ProcessorFeedRecord> getPrimaryKey() {
        return Keys.KEY_PROCESSOR_FEED_PRIMARY;
    }

    @Override
    public List<UniqueKey<ProcessorFeedRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.KEY_PROCESSOR_FEED_PROCESSOR_FEED_NAME);
    }

    @Override
    public ProcessorFeed as(String alias) {
        return new ProcessorFeed(DSL.name(alias), this);
    }

    @Override
    public ProcessorFeed as(Name alias) {
        return new ProcessorFeed(alias, this);
    }

    @Override
    public ProcessorFeed as(Table<?> alias) {
        return new ProcessorFeed(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public ProcessorFeed rename(String name) {
        return new ProcessorFeed(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ProcessorFeed rename(Name name) {
        return new ProcessorFeed(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public ProcessorFeed rename(Table<?> name) {
        return new ProcessorFeed(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<Integer, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function2<? super Integer, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function2<? super Integer, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
