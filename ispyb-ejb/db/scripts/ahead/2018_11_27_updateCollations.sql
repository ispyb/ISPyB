use pydb;
insert into `pydb`.SchemaStatus (scriptName, schemaStatus) values ('2018_11_27_updateCollations.sql','ONGOING');

ALTER TABLE `Macromolecule` CHANGE `acronym` `acronym` VARCHAR(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL;
ALTER TABLE `Macromolecule` CHANGE `name` `name` VARCHAR(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL;
ALTER TABLE `Macromolecule` CHANGE `comments` `comments` VARCHAR(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL;

update `pydb`.SchemaStatus set schemaStatus = 'DONE' where scriptName = '2018_11_27_updateCollations.sql';  
