-- ------------------------------------------------------------------------
-- Copyright 2023 Crown Copyright
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
-- ------------------------------------------------------------------------

-- Stop NOTE level warnings about objects (not)? existing
SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0;

-- --------------------------------------------------

DELIMITER $$

DROP PROCEDURE IF EXISTS security_run_sql_v1$$

-- DO NOT change this without reading the header!
CREATE PROCEDURE security_run_sql_v1 (
    p_sql_stmt varchar(1000)
)
BEGIN

    SET @sqlstmt = p_sql_stmt;

    SELECT CONCAT('Running sql: ', @sqlstmt);

    PREPARE stmt FROM @sqlstmt;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END $$

DROP PROCEDURE IF EXISTS security_rename_column_v1$$

-- DO NOT change this without reading the header!
CREATE PROCEDURE security_rename_column_v1 (
    p_table_name varchar(64),
    p_old_column_name varchar(64),
    p_new_column_name varchar(64),
    p_column_type_info varchar(64) -- e.g. 'varchar(255) default NULL'
)
BEGIN
    DECLARE object_count integer;

    SELECT COUNT(1)
    INTO object_count
    FROM information_schema.columns
    WHERE table_schema = database()
    AND table_name = p_table_name
    AND column_name = p_new_column_name;

    IF object_count = 0 THEN
        CALL security_run_sql_v1(CONCAT(
            'alter table ', database(), '.', p_table_name,
            ' change ', p_old_column_name, ' ',
            p_new_column_name, ' ', p_column_type_info));
    ELSE
        SELECT CONCAT(
            'Column ',
            p_new_column_name,
            ' already exists on table ',
            database(),
            '.',
            p_table_name);
    END IF;
END $$

DELIMITER ;

-- --------------------------------------------------

-- idempotent
-- rename stroom_user.preferred_username => display_name
CALL security_rename_column_v1(
    'stroom_user',
    'preferred_username',
    'display_name',
    'varchar(255) DEFAULT NULL');

-- --------------------------------------------------

DROP PROCEDURE IF EXISTS security_rename_column_v1;

DROP PROCEDURE IF EXISTS security_run_sql_v1;

-- --------------------------------------------------

SET SQL_NOTES=@OLD_SQL_NOTES;

-- vim: set shiftwidth=4 tabstop=4 expandtab:
