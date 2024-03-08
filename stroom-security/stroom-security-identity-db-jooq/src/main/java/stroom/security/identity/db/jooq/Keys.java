/*
 * This file is generated by jOOQ.
 */
package stroom.security.identity.db.jooq;


import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;

import stroom.security.identity.db.jooq.tables.Account;
import stroom.security.identity.db.jooq.tables.JsonWebKey;
import stroom.security.identity.db.jooq.tables.OauthClient;
import stroom.security.identity.db.jooq.tables.TokenType;
import stroom.security.identity.db.jooq.tables.records.AccountRecord;
import stroom.security.identity.db.jooq.tables.records.JsonWebKeyRecord;
import stroom.security.identity.db.jooq.tables.records.OauthClientRecord;
import stroom.security.identity.db.jooq.tables.records.TokenTypeRecord;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * stroom.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<AccountRecord> KEY_ACCOUNT_PRIMARY = Internal.createUniqueKey(Account.ACCOUNT, DSL.name("KEY_account_PRIMARY"), new TableField[] { Account.ACCOUNT.ID }, true);
    public static final UniqueKey<AccountRecord> KEY_ACCOUNT_USER_ID = Internal.createUniqueKey(Account.ACCOUNT, DSL.name("KEY_account_user_id"), new TableField[] { Account.ACCOUNT.USER_ID }, true);
    public static final UniqueKey<JsonWebKeyRecord> KEY_JSON_WEB_KEY_PRIMARY = Internal.createUniqueKey(JsonWebKey.JSON_WEB_KEY, DSL.name("KEY_json_web_key_PRIMARY"), new TableField[] { JsonWebKey.JSON_WEB_KEY.ID }, true);
    public static final UniqueKey<OauthClientRecord> KEY_OAUTH_CLIENT_CLIENT_ID = Internal.createUniqueKey(OauthClient.OAUTH_CLIENT, DSL.name("KEY_oauth_client_client_id"), new TableField[] { OauthClient.OAUTH_CLIENT.CLIENT_ID }, true);
    public static final UniqueKey<OauthClientRecord> KEY_OAUTH_CLIENT_NAME = Internal.createUniqueKey(OauthClient.OAUTH_CLIENT, DSL.name("KEY_oauth_client_name"), new TableField[] { OauthClient.OAUTH_CLIENT.NAME }, true);
    public static final UniqueKey<OauthClientRecord> KEY_OAUTH_CLIENT_PRIMARY = Internal.createUniqueKey(OauthClient.OAUTH_CLIENT, DSL.name("KEY_oauth_client_PRIMARY"), new TableField[] { OauthClient.OAUTH_CLIENT.ID }, true);
    public static final UniqueKey<TokenTypeRecord> KEY_TOKEN_TYPE_PRIMARY = Internal.createUniqueKey(TokenType.TOKEN_TYPE, DSL.name("KEY_token_type_PRIMARY"), new TableField[] { TokenType.TOKEN_TYPE.ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<JsonWebKeyRecord, TokenTypeRecord> JSON_WEB_KEY_FK_TOKEN_TYPE_ID = Internal.createForeignKey(JsonWebKey.JSON_WEB_KEY, DSL.name("json_web_key_fk_token_type_id"), new TableField[] { JsonWebKey.JSON_WEB_KEY.FK_TOKEN_TYPE_ID }, Keys.KEY_TOKEN_TYPE_PRIMARY, new TableField[] { TokenType.TOKEN_TYPE.ID }, true);
}
