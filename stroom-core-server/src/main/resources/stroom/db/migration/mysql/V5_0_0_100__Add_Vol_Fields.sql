ALTER TABLE VOL DROP FOREIGN KEY VOL_FK_ND_ID;
ALTER TABLE VOL DROP INDEX FK_ND_ID;

ALTER TABLE VOL ADD COLUMN HDFS_URI varchar(255) default NULL;
ALTER TABLE VOL ADD COLUMN RUN_AS varchar(255) default NULL;
ALTER TABLE VOL MODIFY FK_ND_ID int(11) default NULL;

ALTER TABLE VOL ADD CONSTRAINT VOL_FK_ND_ID FOREIGN KEY (FK_ND_ID) REFERENCES ND(ID);
ALTER TABLE VOL ADD CONSTRAINT UNIQUE UK_VOL (VOL_TP,PATH,FK_ND_ID);