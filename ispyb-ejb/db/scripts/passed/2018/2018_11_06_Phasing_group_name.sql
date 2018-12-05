USE pydb;
INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2018_11_06_Phasing_group_name.sql','ONGOING');

ALTER TABLE `pydb`.`PhasingStep` 
ADD COLUMN `groupName` VARCHAR(45) NULL AFTER `highRes`;

ALTER TABLE `pydb`.`PhasingProgramAttachment` 
ADD COLUMN `input` TINYINT(1) NULL AFTER `filePath`;

update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2018_11_06_Phasing_group_name.sql';