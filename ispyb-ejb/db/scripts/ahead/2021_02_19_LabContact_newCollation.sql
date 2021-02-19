INSERT IGNORE INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2021_02_19_LabContact_newCollation.sql', 'ONGOING');


ALTER TABLE `LabContact` CHANGE `cardName` `cardName` VARCHAR(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL;


UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2021_02_19_LabContact_newCollation.sql';
