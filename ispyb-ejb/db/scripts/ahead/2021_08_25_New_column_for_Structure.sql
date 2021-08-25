INSERT IGNORE INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2021_08_25_New_column_for_Structure.sql', 'ONGOING');

ALTER TABLE `pydb`.`Structure` 

ADD COLUMN `uniprotId` VARCHAR(45) NULL DEFAULT NULL AFTER `proposalId`;

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2021_08_25_New_column_for_Structure.sql';
