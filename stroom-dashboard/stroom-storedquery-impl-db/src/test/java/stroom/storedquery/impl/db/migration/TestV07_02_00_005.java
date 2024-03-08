package stroom.storedquery.impl.db.migration;

import stroom.util.logging.AsciiTable;
import stroom.util.logging.LambdaLogger;
import stroom.util.logging.LambdaLoggerFactory;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TestV07_02_00_005 extends AbstractStoredQueryMigrationTest {

    private static final LambdaLogger LOGGER = LambdaLoggerFactory.getLogger(TestV07_02_00_005.class);

    private static final String UUID1 = UUID.randomUUID().toString();
    private static final String UUID2 = UUID.randomUUID().toString();
    public static final String NAME1 = "bob";
    public static final String NAME2 = "fred";

    @Override
    protected String getTestDataScript() {

        // We have to create a skeleton stroom_user table to be able to join to in the mig
        return scriptBuilder()
                .append("""
                        CREATE TABLE stroom_user (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `name` varchar(255) NOT NULL,
                          `uuid` varchar(255) NOT NULL,
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `stroom_user_uuid_idx` (`uuid`)
                        ) ENGINE=InnoDB AUTO_INCREMENT=4
                          DEFAULT CHARSET=utf8mb4
                          COLLATE=utf8mb4_0900_ai_ci;
                        """)
                .setParam("name1", NAME1)
                .setParam("name2", NAME2)
                .setParam("uuid1", UUID1)
                .setParam("uuid2", UUID2)
                .append("""
                        insert into stroom_user (
                            name,
                            uuid)
                        values (
                            @name1,
                            @uuid1);
                        """)
                .append("""
                        insert into stroom_user (
                            name,
                            uuid)
                        values (
                            @name2,
                            @uuid2);
                        """)
                .append("""
                        INSERT INTO query (
                            version,
                            create_time_ms,
                            create_user,
                            update_time_ms,
                            update_user,
                            dashboard_uuid,
                            component_id,
                            name,
                            data,
                            favourite)
                        VALUES (
                            1,
                            0,
                            @name1,
                            0,
                            @name1,
                            'my uuid',
                            'my component',
                            'my fav name',
                            'some json',
                            1);""")
                .append("""
                        INSERT INTO query (
                            version,
                            create_time_ms,
                            create_user,
                            update_time_ms,
                            update_user,
                            dashboard_uuid,
                            component_id,
                            name,
                            data,
                            favourite)
                        VALUES (
                            1,
                            0,
                            @name2,
                            0,
                            @name2,
                            'my uuid',
                            'my component',
                            'my fav name',
                            'some json',
                            1);""")
                .build();
    }

    @Test
    void test() {

        final List<Row> rows = getRows("""
                        select
                            create_user,
                            update_user,
                            owner_uuid
                        from query
                        """,
                rec -> new Row(
                        rec.get("create_user", String.class),
                        rec.get("update_user", String.class),
                        rec.get("owner_uuid", String.class)));

        assertThat(rows)
                .containsExactlyInAnyOrderElementsOf(List.of(
                        new Row(NAME1, NAME1, UUID1),
                        new Row(NAME2, NAME2, UUID2)));

        LOGGER.info("rows:\n{}", AsciiTable.fromCollection(rows));
    }


    // --------------------------------------------------------------------------------


    public record Row(String createUser, String updateUser, String ownerUuid) {

    }
}
