/*
 * This file is generated by jOOQ.
 */
package stroom.security.impl.db.jooq;


import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;

import stroom.security.impl.db.jooq.tables.ApiKey;
import stroom.security.impl.db.jooq.tables.AppPermission;
import stroom.security.impl.db.jooq.tables.DocPermission;
import stroom.security.impl.db.jooq.tables.StroomUser;
import stroom.security.impl.db.jooq.tables.StroomUserGroup;
import stroom.security.impl.db.jooq.tables.records.ApiKeyRecord;
import stroom.security.impl.db.jooq.tables.records.AppPermissionRecord;
import stroom.security.impl.db.jooq.tables.records.DocPermissionRecord;
import stroom.security.impl.db.jooq.tables.records.StroomUserGroupRecord;
import stroom.security.impl.db.jooq.tables.records.StroomUserRecord;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * stroom.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<ApiKeyRecord> KEY_API_KEY_API_KEY_API_KEY_HASH_IDX = Internal.createUniqueKey(ApiKey.API_KEY, DSL.name("KEY_api_key_api_key_api_key_hash_idx"), new TableField[] { ApiKey.API_KEY.API_KEY_HASH }, true);
    public static final UniqueKey<ApiKeyRecord> KEY_API_KEY_API_KEY_OWNER_NAME_IDX = Internal.createUniqueKey(ApiKey.API_KEY, DSL.name("KEY_api_key_api_key_owner_name_idx"), new TableField[] { ApiKey.API_KEY.FK_OWNER_UUID, ApiKey.API_KEY.NAME }, true);
    public static final UniqueKey<ApiKeyRecord> KEY_API_KEY_PRIMARY = Internal.createUniqueKey(ApiKey.API_KEY, DSL.name("KEY_api_key_PRIMARY"), new TableField[] { ApiKey.API_KEY.ID }, true);
    public static final UniqueKey<AppPermissionRecord> KEY_APP_PERMISSION_APP_PERMISSION_USER_UUID_PERMISSION_IDX = Internal.createUniqueKey(AppPermission.APP_PERMISSION, DSL.name("KEY_app_permission_app_permission_user_uuid_permission_idx"), new TableField[] { AppPermission.APP_PERMISSION.USER_UUID, AppPermission.APP_PERMISSION.PERMISSION }, true);
    public static final UniqueKey<AppPermissionRecord> KEY_APP_PERMISSION_PRIMARY = Internal.createUniqueKey(AppPermission.APP_PERMISSION, DSL.name("KEY_app_permission_PRIMARY"), new TableField[] { AppPermission.APP_PERMISSION.ID }, true);
    public static final UniqueKey<DocPermissionRecord> KEY_DOC_PERMISSION_DOC_PERMISSION_FK_USER_UUID_DOC_UUID_PERMISSION_IDX = Internal.createUniqueKey(DocPermission.DOC_PERMISSION, DSL.name("KEY_doc_permission_doc_permission_fk_user_uuid_doc_uuid_permission_idx"), new TableField[] { DocPermission.DOC_PERMISSION.USER_UUID, DocPermission.DOC_PERMISSION.DOC_UUID, DocPermission.DOC_PERMISSION.PERMISSION }, true);
    public static final UniqueKey<DocPermissionRecord> KEY_DOC_PERMISSION_PRIMARY = Internal.createUniqueKey(DocPermission.DOC_PERMISSION, DSL.name("KEY_doc_permission_PRIMARY"), new TableField[] { DocPermission.DOC_PERMISSION.ID }, true);
    public static final UniqueKey<StroomUserRecord> KEY_STROOM_USER_PRIMARY = Internal.createUniqueKey(StroomUser.STROOM_USER, DSL.name("KEY_stroom_user_PRIMARY"), new TableField[] { StroomUser.STROOM_USER.ID }, true);
    public static final UniqueKey<StroomUserRecord> KEY_STROOM_USER_STROOM_USER_NAME_IS_GROUP_IDX = Internal.createUniqueKey(StroomUser.STROOM_USER, DSL.name("KEY_stroom_user_stroom_user_name_is_group_idx"), new TableField[] { StroomUser.STROOM_USER.NAME, StroomUser.STROOM_USER.IS_GROUP }, true);
    public static final UniqueKey<StroomUserRecord> KEY_STROOM_USER_STROOM_USER_UUID_IDX = Internal.createUniqueKey(StroomUser.STROOM_USER, DSL.name("KEY_stroom_user_stroom_user_uuid_idx"), new TableField[] { StroomUser.STROOM_USER.UUID }, true);
    public static final UniqueKey<StroomUserGroupRecord> KEY_STROOM_USER_GROUP_PRIMARY = Internal.createUniqueKey(StroomUserGroup.STROOM_USER_GROUP, DSL.name("KEY_stroom_user_group_PRIMARY"), new TableField[] { StroomUserGroup.STROOM_USER_GROUP.ID }, true);
    public static final UniqueKey<StroomUserGroupRecord> KEY_STROOM_USER_GROUP_STROOM_USER_GROUP_GROUP_UUID_USER_UUID_IDX = Internal.createUniqueKey(StroomUserGroup.STROOM_USER_GROUP, DSL.name("KEY_stroom_user_group_stroom_user_group_group_uuid_user_uuid_IDX"), new TableField[] { StroomUserGroup.STROOM_USER_GROUP.GROUP_UUID, StroomUserGroup.STROOM_USER_GROUP.USER_UUID }, true);
    public static final UniqueKey<StroomUserGroupRecord> KEY_STROOM_USER_GROUP_STROOM_USER_GROUP_USER_UUID_GROUP_UUID_IDX = Internal.createUniqueKey(StroomUserGroup.STROOM_USER_GROUP, DSL.name("KEY_stroom_user_group_stroom_user_group_user_uuid_group_uuid_idx"), new TableField[] { StroomUserGroup.STROOM_USER_GROUP.USER_UUID, StroomUserGroup.STROOM_USER_GROUP.GROUP_UUID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<ApiKeyRecord, StroomUserRecord> API_KEY_FK_OWNER_UUID = Internal.createForeignKey(ApiKey.API_KEY, DSL.name("api_key_fk_owner_uuid"), new TableField[] { ApiKey.API_KEY.FK_OWNER_UUID }, Keys.KEY_STROOM_USER_STROOM_USER_UUID_IDX, new TableField[] { StroomUser.STROOM_USER.UUID }, true);
    public static final ForeignKey<AppPermissionRecord, StroomUserRecord> APP_PERMISSION_USER_UUID = Internal.createForeignKey(AppPermission.APP_PERMISSION, DSL.name("app_permission_user_uuid"), new TableField[] { AppPermission.APP_PERMISSION.USER_UUID }, Keys.KEY_STROOM_USER_STROOM_USER_UUID_IDX, new TableField[] { StroomUser.STROOM_USER.UUID }, true);
    public static final ForeignKey<DocPermissionRecord, StroomUserRecord> DOC_PERMISSION_FK_USER_UUID = Internal.createForeignKey(DocPermission.DOC_PERMISSION, DSL.name("doc_permission_fk_user_uuid"), new TableField[] { DocPermission.DOC_PERMISSION.USER_UUID }, Keys.KEY_STROOM_USER_STROOM_USER_UUID_IDX, new TableField[] { StroomUser.STROOM_USER.UUID }, true);
    public static final ForeignKey<StroomUserGroupRecord, StroomUserRecord> STROOM_USER_GROUP_FK_GROUP_UUID = Internal.createForeignKey(StroomUserGroup.STROOM_USER_GROUP, DSL.name("stroom_user_group_fk_group_uuid"), new TableField[] { StroomUserGroup.STROOM_USER_GROUP.GROUP_UUID }, Keys.KEY_STROOM_USER_STROOM_USER_UUID_IDX, new TableField[] { StroomUser.STROOM_USER.UUID }, true);
    public static final ForeignKey<StroomUserGroupRecord, StroomUserRecord> STROOM_USER_GROUP_FK_USER_UUID = Internal.createForeignKey(StroomUserGroup.STROOM_USER_GROUP, DSL.name("stroom_user_group_fk_user_uuid"), new TableField[] { StroomUserGroup.STROOM_USER_GROUP.USER_UUID }, Keys.KEY_STROOM_USER_STROOM_USER_UUID_IDX, new TableField[] { StroomUser.STROOM_USER.UUID }, true);
}
