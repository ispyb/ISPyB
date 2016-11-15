
-- 

USE `pydb`;
-- first line of script
insert into SchemaStatus (scriptName, schemaStatus) values ('2016_11_17_x_Example_of_script.sql','ONGOING');

-- body of the script
-- ALTER TABLE `Screening` CHANGE `dataCollectionId` `dataCollectionId` INT(11) UNSIGNED NULL DEFAULT NULL;

-- last line of script
update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2016_11_17_x_Example_of_script.sql';

