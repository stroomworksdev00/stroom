/*
 * This file is generated by jOOQ.
 */
package stroom.proxy.repo.db.jooq;


import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;

import stroom.proxy.repo.db.jooq.tables.Aggregate;
import stroom.proxy.repo.db.jooq.tables.Feed;
import stroom.proxy.repo.db.jooq.tables.ForwardAggregate;
import stroom.proxy.repo.db.jooq.tables.ForwardDest;
import stroom.proxy.repo.db.jooq.tables.ForwardSource;
import stroom.proxy.repo.db.jooq.tables.Source;
import stroom.proxy.repo.db.jooq.tables.SourceEntry;
import stroom.proxy.repo.db.jooq.tables.SourceItem;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DefaultSchema extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>DEFAULT_SCHEMA</code>
     */
    public static final DefaultSchema DEFAULT_SCHEMA = new DefaultSchema();

    /**
     * The table <code>aggregate</code>.
     */
    public final Aggregate AGGREGATE = Aggregate.AGGREGATE;

    /**
     * The table <code>feed</code>.
     */
    public final Feed FEED = Feed.FEED;

    /**
     * The table <code>forward_aggregate</code>.
     */
    public final ForwardAggregate FORWARD_AGGREGATE = ForwardAggregate.FORWARD_AGGREGATE;

    /**
     * The table <code>forward_dest</code>.
     */
    public final ForwardDest FORWARD_DEST = ForwardDest.FORWARD_DEST;

    /**
     * The table <code>forward_source</code>.
     */
    public final ForwardSource FORWARD_SOURCE = ForwardSource.FORWARD_SOURCE;

    /**
     * The table <code>source</code>.
     */
    public final Source SOURCE = Source.SOURCE;

    /**
     * The table <code>source_entry</code>.
     */
    public final SourceEntry SOURCE_ENTRY = SourceEntry.SOURCE_ENTRY;

    /**
     * The table <code>source_item</code>.
     */
    public final SourceItem SOURCE_ITEM = SourceItem.SOURCE_ITEM;

    /**
     * No further instances allowed
     */
    private DefaultSchema() {
        super("", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            Aggregate.AGGREGATE,
            Feed.FEED,
            ForwardAggregate.FORWARD_AGGREGATE,
            ForwardDest.FORWARD_DEST,
            ForwardSource.FORWARD_SOURCE,
            Source.SOURCE,
            SourceEntry.SOURCE_ENTRY,
            SourceItem.SOURCE_ITEM
        );
    }
}
