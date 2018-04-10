use pydb;
insert into `pydb`.SchemaStatus (scriptName, schemaStatus) values ('2018_03_13_updateCollations.sql','ONGOING');

ALTER TABLE `Proposal` CHANGE `title` `title` VARCHAR(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL;
ALTER TABLE `Protein` CHANGE `name` `name` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL;

update `pydb`.SchemaStatus set schemaStatus = 'DONE' where scriptName = '2018_03_13_updateCollations.sql';  
