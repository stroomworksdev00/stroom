/*
 * This file is generated by jOOQ.
 */
package stroom.analytics.impl.db.jooq;


import stroom.analytics.impl.db.jooq.tables.AnalyticTracker;
import stroom.analytics.impl.db.jooq.tables.ExecutionHistory;
import stroom.analytics.impl.db.jooq.tables.ExecutionSchedule;
import stroom.analytics.impl.db.jooq.tables.ExecutionTracker;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;

import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Stroom extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>stroom</code>
     */
    public static final Stroom STROOM = new Stroom();

    /**
     * The table <code>stroom.analytic_tracker</code>.
     */
    public final AnalyticTracker ANALYTIC_TRACKER = AnalyticTracker.ANALYTIC_TRACKER;

    /**
     * The table <code>stroom.execution_history</code>.
     */
    public final ExecutionHistory EXECUTION_HISTORY = ExecutionHistory.EXECUTION_HISTORY;

    /**
     * The table <code>stroom.execution_schedule</code>.
     */
    public final ExecutionSchedule EXECUTION_SCHEDULE = ExecutionSchedule.EXECUTION_SCHEDULE;

    /**
     * The table <code>stroom.execution_tracker</code>.
     */
    public final ExecutionTracker EXECUTION_TRACKER = ExecutionTracker.EXECUTION_TRACKER;

    /**
     * No further instances allowed
     */
    private Stroom() {
        super("stroom", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            AnalyticTracker.ANALYTIC_TRACKER,
            ExecutionHistory.EXECUTION_HISTORY,
            ExecutionSchedule.EXECUTION_SCHEDULE,
            ExecutionTracker.EXECUTION_TRACKER
        );
    }
}
