use pydb;

INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2023_03_08_Proposal_title_extend.sql', 'ONGOING');


ALTER TABLE `pydb`.`Proposal`
CHANGE COLUMN `title` `title` VARCHAR(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL;

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2023_03_08_Proposal_title_extend.sql';