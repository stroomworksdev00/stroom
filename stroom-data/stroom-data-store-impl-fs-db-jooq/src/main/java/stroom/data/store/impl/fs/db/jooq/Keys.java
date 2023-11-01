/*
 * This file is generated by jOOQ.
 */
package stroom.data.store.impl.fs.db.jooq;


import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;

import stroom.data.store.impl.fs.db.jooq.tables.FsFeedPath;
import stroom.data.store.impl.fs.db.jooq.tables.FsMetaVolume;
import stroom.data.store.impl.fs.db.jooq.tables.FsOrphanedMetaTracker;
import stroom.data.store.impl.fs.db.jooq.tables.FsTypePath;
import stroom.data.store.impl.fs.db.jooq.tables.FsVolume;
import stroom.data.store.impl.fs.db.jooq.tables.FsVolumeGroup;
import stroom.data.store.impl.fs.db.jooq.tables.FsVolumeState;
import stroom.data.store.impl.fs.db.jooq.tables.records.FsFeedPathRecord;
import stroom.data.store.impl.fs.db.jooq.tables.records.FsMetaVolumeRecord;
import stroom.data.store.impl.fs.db.jooq.tables.records.FsOrphanedMetaTrackerRecord;
import stroom.data.store.impl.fs.db.jooq.tables.records.FsTypePathRecord;
import stroom.data.store.impl.fs.db.jooq.tables.records.FsVolumeGroupRecord;
import stroom.data.store.impl.fs.db.jooq.tables.records.FsVolumeRecord;
import stroom.data.store.impl.fs.db.jooq.tables.records.FsVolumeStateRecord;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * stroom.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<FsFeedPathRecord> KEY_FS_FEED_PATH_NAME = Internal.createUniqueKey(FsFeedPath.FS_FEED_PATH, DSL.name("KEY_fs_feed_path_name"), new TableField[] { FsFeedPath.FS_FEED_PATH.NAME }, true);
    public static final UniqueKey<FsFeedPathRecord> KEY_FS_FEED_PATH_PRIMARY = Internal.createUniqueKey(FsFeedPath.FS_FEED_PATH, DSL.name("KEY_fs_feed_path_PRIMARY"), new TableField[] { FsFeedPath.FS_FEED_PATH.ID }, true);
    public static final UniqueKey<FsMetaVolumeRecord> KEY_FS_META_VOLUME_PRIMARY = Internal.createUniqueKey(FsMetaVolume.FS_META_VOLUME, DSL.name("KEY_fs_meta_volume_PRIMARY"), new TableField[] { FsMetaVolume.FS_META_VOLUME.META_ID, FsMetaVolume.FS_META_VOLUME.FS_VOLUME_ID }, true);
    public static final UniqueKey<FsOrphanedMetaTrackerRecord> KEY_FS_ORPHANED_META_TRACKER_PRIMARY = Internal.createUniqueKey(FsOrphanedMetaTracker.FS_ORPHANED_META_TRACKER, DSL.name("KEY_fs_orphaned_meta_tracker_PRIMARY"), new TableField[] { FsOrphanedMetaTracker.FS_ORPHANED_META_TRACKER.ID }, true);
    public static final UniqueKey<FsTypePathRecord> KEY_FS_TYPE_PATH_NAME = Internal.createUniqueKey(FsTypePath.FS_TYPE_PATH, DSL.name("KEY_fs_type_path_name"), new TableField[] { FsTypePath.FS_TYPE_PATH.NAME }, true);
    public static final UniqueKey<FsTypePathRecord> KEY_FS_TYPE_PATH_PRIMARY = Internal.createUniqueKey(FsTypePath.FS_TYPE_PATH, DSL.name("KEY_fs_type_path_PRIMARY"), new TableField[] { FsTypePath.FS_TYPE_PATH.ID }, true);
    public static final UniqueKey<FsVolumeRecord> KEY_FS_VOLUME_PATH = Internal.createUniqueKey(FsVolume.FS_VOLUME, DSL.name("KEY_fs_volume_path"), new TableField[] { FsVolume.FS_VOLUME.PATH }, true);
    public static final UniqueKey<FsVolumeRecord> KEY_FS_VOLUME_PRIMARY = Internal.createUniqueKey(FsVolume.FS_VOLUME, DSL.name("KEY_fs_volume_PRIMARY"), new TableField[] { FsVolume.FS_VOLUME.ID }, true);
    public static final UniqueKey<FsVolumeGroupRecord> KEY_FS_VOLUME_GROUP_NAME = Internal.createUniqueKey(FsVolumeGroup.FS_VOLUME_GROUP, DSL.name("KEY_fs_volume_group_name"), new TableField[] { FsVolumeGroup.FS_VOLUME_GROUP.NAME }, true);
    public static final UniqueKey<FsVolumeGroupRecord> KEY_FS_VOLUME_GROUP_PRIMARY = Internal.createUniqueKey(FsVolumeGroup.FS_VOLUME_GROUP, DSL.name("KEY_fs_volume_group_PRIMARY"), new TableField[] { FsVolumeGroup.FS_VOLUME_GROUP.ID }, true);
    public static final UniqueKey<FsVolumeStateRecord> KEY_FS_VOLUME_STATE_PRIMARY = Internal.createUniqueKey(FsVolumeState.FS_VOLUME_STATE, DSL.name("KEY_fs_volume_state_PRIMARY"), new TableField[] { FsVolumeState.FS_VOLUME_STATE.ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<FsVolumeRecord, FsVolumeStateRecord> FS_VOLUME_FK_FS_VOLUME_STATE_ID = Internal.createForeignKey(FsVolume.FS_VOLUME, DSL.name("fs_volume_fk_fs_volume_state_id"), new TableField[] { FsVolume.FS_VOLUME.FK_FS_VOLUME_STATE_ID }, Keys.KEY_FS_VOLUME_STATE_PRIMARY, new TableField[] { FsVolumeState.FS_VOLUME_STATE.ID }, true);
    public static final ForeignKey<FsVolumeRecord, FsVolumeGroupRecord> FS_VOLUME_GROUP_FK_FS_VOLUME_GROUP_ID = Internal.createForeignKey(FsVolume.FS_VOLUME, DSL.name("fs_volume_group_fk_fs_volume_group_id"), new TableField[] { FsVolume.FS_VOLUME.FK_FS_VOLUME_GROUP_ID }, Keys.KEY_FS_VOLUME_GROUP_PRIMARY, new TableField[] { FsVolumeGroup.FS_VOLUME_GROUP.ID }, true);
}
