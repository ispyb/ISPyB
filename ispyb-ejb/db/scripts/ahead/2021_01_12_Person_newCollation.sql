INSERT IGNORE INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2021_01_12_Person_new_collation.sql', 'ONGOING');

ALTER TABLE `Person` CHANGE `phoneNumber` `phoneNumber` VARCHAR(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL;

ALTER TABLE `Person` CHANGE `familyName` `familyName` VARCHAR(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL;

ALTER TABLE `Person` CHANGE `givenName` `givenName` VARCHAR(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL;

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2021_01_12_Person_new_collation.sql';
