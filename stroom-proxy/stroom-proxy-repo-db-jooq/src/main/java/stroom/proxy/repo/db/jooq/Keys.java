/*
 * This file is generated by jOOQ.
 */
package stroom.proxy.repo.db.jooq;


import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;

import stroom.proxy.repo.db.jooq.tables.Aggregate;
import stroom.proxy.repo.db.jooq.tables.Feed;
import stroom.proxy.repo.db.jooq.tables.ForwardAggregate;
import stroom.proxy.repo.db.jooq.tables.ForwardDest;
import stroom.proxy.repo.db.jooq.tables.ForwardSource;
import stroom.proxy.repo.db.jooq.tables.Source;
import stroom.proxy.repo.db.jooq.tables.SourceEntry;
import stroom.proxy.repo.db.jooq.tables.SourceItem;
import stroom.proxy.repo.db.jooq.tables.records.AggregateRecord;
import stroom.proxy.repo.db.jooq.tables.records.FeedRecord;
import stroom.proxy.repo.db.jooq.tables.records.ForwardAggregateRecord;
import stroom.proxy.repo.db.jooq.tables.records.ForwardDestRecord;
import stroom.proxy.repo.db.jooq.tables.records.ForwardSourceRecord;
import stroom.proxy.repo.db.jooq.tables.records.SourceEntryRecord;
import stroom.proxy.repo.db.jooq.tables.records.SourceItemRecord;
import stroom.proxy.repo.db.jooq.tables.records.SourceRecord;


/**
 * A class modelling foreign key relationships and constraints of tables in the
 * default schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<AggregateRecord> AGGREGATE__ = Internal.createUniqueKey(Aggregate.AGGREGATE, DSL.name(""), new TableField[] { Aggregate.AGGREGATE.ID }, true);
    public static final UniqueKey<FeedRecord> FEED__ = Internal.createUniqueKey(Feed.FEED, DSL.name(""), new TableField[] { Feed.FEED.ID }, true);
    public static final UniqueKey<ForwardAggregateRecord> FORWARD_AGGREGATE__ = Internal.createUniqueKey(ForwardAggregate.FORWARD_AGGREGATE, DSL.name(""), new TableField[] { ForwardAggregate.FORWARD_AGGREGATE.ID }, true);
    public static final UniqueKey<ForwardDestRecord> FORWARD_DEST__ = Internal.createUniqueKey(ForwardDest.FORWARD_DEST, DSL.name(""), new TableField[] { ForwardDest.FORWARD_DEST.ID, ForwardDest.FORWARD_DEST.NAME }, true);
    public static final UniqueKey<ForwardSourceRecord> FORWARD_SOURCE__ = Internal.createUniqueKey(ForwardSource.FORWARD_SOURCE, DSL.name(""), new TableField[] { ForwardSource.FORWARD_SOURCE.ID }, true);
    public static final UniqueKey<SourceRecord> SOURCE__ = Internal.createUniqueKey(Source.SOURCE, DSL.name(""), new TableField[] { Source.SOURCE.ID }, true);
    public static final UniqueKey<SourceEntryRecord> SOURCE_ENTRY__ = Internal.createUniqueKey(SourceEntry.SOURCE_ENTRY, DSL.name(""), new TableField[] { SourceEntry.SOURCE_ENTRY.ID }, true);
    public static final UniqueKey<SourceItemRecord> SOURCE_ITEM__ = Internal.createUniqueKey(SourceItem.SOURCE_ITEM, DSL.name(""), new TableField[] { SourceItem.SOURCE_ITEM.ID }, true);
}
