/*
 * This file is generated by jOOQ.
 */
package stroom.config.impl.db.jooq;


import stroom.config.impl.db.jooq.tables.Config;
import stroom.config.impl.db.jooq.tables.ConfigUpdateTracker;
import stroom.config.impl.db.jooq.tables.Preferences;
import stroom.config.impl.db.jooq.tables.records.ConfigRecord;
import stroom.config.impl.db.jooq.tables.records.ConfigUpdateTrackerRecord;
import stroom.config.impl.db.jooq.tables.records.PreferencesRecord;

import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * stroom.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<ConfigRecord> KEY_CONFIG_NAME = Internal.createUniqueKey(Config.CONFIG, DSL.name("KEY_config_name"), new TableField[] { Config.CONFIG.NAME }, true);
    public static final UniqueKey<ConfigRecord> KEY_CONFIG_PRIMARY = Internal.createUniqueKey(Config.CONFIG, DSL.name("KEY_config_PRIMARY"), new TableField[] { Config.CONFIG.ID }, true);
    public static final UniqueKey<ConfigUpdateTrackerRecord> KEY_CONFIG_UPDATE_TRACKER_PRIMARY = Internal.createUniqueKey(ConfigUpdateTracker.CONFIG_UPDATE_TRACKER, DSL.name("KEY_config_update_tracker_PRIMARY"), new TableField[] { ConfigUpdateTracker.CONFIG_UPDATE_TRACKER.ID }, true);
    public static final UniqueKey<PreferencesRecord> KEY_PREFERENCES_PRIMARY = Internal.createUniqueKey(Preferences.PREFERENCES, DSL.name("KEY_preferences_PRIMARY"), new TableField[] { Preferences.PREFERENCES.ID }, true);
    public static final UniqueKey<PreferencesRecord> KEY_PREFERENCES_USER_UUID = Internal.createUniqueKey(Preferences.PREFERENCES, DSL.name("KEY_preferences_user_uuid"), new TableField[] { Preferences.PREFERENCES.USER_UUID }, true);
}
