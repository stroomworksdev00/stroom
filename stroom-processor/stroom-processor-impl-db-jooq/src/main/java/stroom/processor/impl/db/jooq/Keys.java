/*
 * This file is generated by jOOQ.
 */
package stroom.processor.impl.db.jooq;


import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;

import stroom.processor.impl.db.jooq.tables.Processor;
import stroom.processor.impl.db.jooq.tables.ProcessorFeed;
import stroom.processor.impl.db.jooq.tables.ProcessorFilter;
import stroom.processor.impl.db.jooq.tables.ProcessorFilterTracker;
import stroom.processor.impl.db.jooq.tables.ProcessorNode;
import stroom.processor.impl.db.jooq.tables.ProcessorTask;
import stroom.processor.impl.db.jooq.tables.records.ProcessorFeedRecord;
import stroom.processor.impl.db.jooq.tables.records.ProcessorFilterRecord;
import stroom.processor.impl.db.jooq.tables.records.ProcessorFilterTrackerRecord;
import stroom.processor.impl.db.jooq.tables.records.ProcessorNodeRecord;
import stroom.processor.impl.db.jooq.tables.records.ProcessorRecord;
import stroom.processor.impl.db.jooq.tables.records.ProcessorTaskRecord;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * stroom.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<ProcessorRecord> KEY_PROCESSOR_PRIMARY = Internal.createUniqueKey(Processor.PROCESSOR, DSL.name("KEY_processor_PRIMARY"), new TableField[] { Processor.PROCESSOR.ID }, true);
    public static final UniqueKey<ProcessorRecord> KEY_PROCESSOR_PROCESSOR_TASK_TYPE_PIPELINE_UUID = Internal.createUniqueKey(Processor.PROCESSOR, DSL.name("KEY_processor_processor_task_type_pipeline_uuid"), new TableField[] { Processor.PROCESSOR.TASK_TYPE, Processor.PROCESSOR.PIPELINE_UUID }, true);
    public static final UniqueKey<ProcessorRecord> KEY_PROCESSOR_PROCESSOR_UUID = Internal.createUniqueKey(Processor.PROCESSOR, DSL.name("KEY_processor_processor_uuid"), new TableField[] { Processor.PROCESSOR.UUID }, true);
    public static final UniqueKey<ProcessorFeedRecord> KEY_PROCESSOR_FEED_PRIMARY = Internal.createUniqueKey(ProcessorFeed.PROCESSOR_FEED, DSL.name("KEY_processor_feed_PRIMARY"), new TableField[] { ProcessorFeed.PROCESSOR_FEED.ID }, true);
    public static final UniqueKey<ProcessorFeedRecord> KEY_PROCESSOR_FEED_PROCESSOR_FEED_NAME = Internal.createUniqueKey(ProcessorFeed.PROCESSOR_FEED, DSL.name("KEY_processor_feed_processor_feed_name"), new TableField[] { ProcessorFeed.PROCESSOR_FEED.NAME }, true);
    public static final UniqueKey<ProcessorFilterRecord> KEY_PROCESSOR_FILTER_PRIMARY = Internal.createUniqueKey(ProcessorFilter.PROCESSOR_FILTER, DSL.name("KEY_processor_filter_PRIMARY"), new TableField[] { ProcessorFilter.PROCESSOR_FILTER.ID }, true);
    public static final UniqueKey<ProcessorFilterRecord> KEY_PROCESSOR_FILTER_UUID = Internal.createUniqueKey(ProcessorFilter.PROCESSOR_FILTER, DSL.name("KEY_processor_filter_uuid"), new TableField[] { ProcessorFilter.PROCESSOR_FILTER.UUID }, true);
    public static final UniqueKey<ProcessorFilterTrackerRecord> KEY_PROCESSOR_FILTER_TRACKER_PRIMARY = Internal.createUniqueKey(ProcessorFilterTracker.PROCESSOR_FILTER_TRACKER, DSL.name("KEY_processor_filter_tracker_PRIMARY"), new TableField[] { ProcessorFilterTracker.PROCESSOR_FILTER_TRACKER.ID }, true);
    public static final UniqueKey<ProcessorNodeRecord> KEY_PROCESSOR_NODE_PRIMARY = Internal.createUniqueKey(ProcessorNode.PROCESSOR_NODE, DSL.name("KEY_processor_node_PRIMARY"), new TableField[] { ProcessorNode.PROCESSOR_NODE.ID }, true);
    public static final UniqueKey<ProcessorNodeRecord> KEY_PROCESSOR_NODE_PROCESSOR_NODE_NAME = Internal.createUniqueKey(ProcessorNode.PROCESSOR_NODE, DSL.name("KEY_processor_node_processor_node_name"), new TableField[] { ProcessorNode.PROCESSOR_NODE.NAME }, true);
    public static final UniqueKey<ProcessorTaskRecord> KEY_PROCESSOR_TASK_PRIMARY = Internal.createUniqueKey(ProcessorTask.PROCESSOR_TASK, DSL.name("KEY_processor_task_PRIMARY"), new TableField[] { ProcessorTask.PROCESSOR_TASK.ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<ProcessorFilterRecord, ProcessorFilterTrackerRecord> PROCESSOR_FILTER_FK_PROCESSOR_FILTER_TRACKER_ID = Internal.createForeignKey(ProcessorFilter.PROCESSOR_FILTER, DSL.name("processor_filter_fk_processor_filter_tracker_id"), new TableField[] { ProcessorFilter.PROCESSOR_FILTER.FK_PROCESSOR_FILTER_TRACKER_ID }, Keys.KEY_PROCESSOR_FILTER_TRACKER_PRIMARY, new TableField[] { ProcessorFilterTracker.PROCESSOR_FILTER_TRACKER.ID }, true);
    public static final ForeignKey<ProcessorFilterRecord, ProcessorRecord> PROCESSOR_FILTER_FK_PROCESSOR_ID = Internal.createForeignKey(ProcessorFilter.PROCESSOR_FILTER, DSL.name("processor_filter_fk_processor_id"), new TableField[] { ProcessorFilter.PROCESSOR_FILTER.FK_PROCESSOR_ID }, Keys.KEY_PROCESSOR_PRIMARY, new TableField[] { Processor.PROCESSOR.ID }, true);
    public static final ForeignKey<ProcessorTaskRecord, ProcessorFeedRecord> PROCESSOR_TASK_FK_PROCESSOR_FEED_ID = Internal.createForeignKey(ProcessorTask.PROCESSOR_TASK, DSL.name("processor_task_fk_processor_feed_id"), new TableField[] { ProcessorTask.PROCESSOR_TASK.FK_PROCESSOR_FEED_ID }, Keys.KEY_PROCESSOR_FEED_PRIMARY, new TableField[] { ProcessorFeed.PROCESSOR_FEED.ID }, true);
    public static final ForeignKey<ProcessorTaskRecord, ProcessorFilterRecord> PROCESSOR_TASK_FK_PROCESSOR_FILTER_ID = Internal.createForeignKey(ProcessorTask.PROCESSOR_TASK, DSL.name("processor_task_fk_processor_filter_id"), new TableField[] { ProcessorTask.PROCESSOR_TASK.FK_PROCESSOR_FILTER_ID }, Keys.KEY_PROCESSOR_FILTER_PRIMARY, new TableField[] { ProcessorFilter.PROCESSOR_FILTER.ID }, true);
    public static final ForeignKey<ProcessorTaskRecord, ProcessorNodeRecord> PROCESSOR_TASK_FK_PROCESSOR_NODE_ID = Internal.createForeignKey(ProcessorTask.PROCESSOR_TASK, DSL.name("processor_task_fk_processor_node_id"), new TableField[] { ProcessorTask.PROCESSOR_TASK.FK_PROCESSOR_NODE_ID }, Keys.KEY_PROCESSOR_NODE_PRIMARY, new TableField[] { ProcessorNode.PROCESSOR_NODE.ID }, true);
}
