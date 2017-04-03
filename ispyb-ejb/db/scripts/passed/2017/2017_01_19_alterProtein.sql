-- first line of script
insert into SchemaStatus (scriptName, schemaStatus) values ('2017_01_19_alterProtein.sql','ONGOING');

-- body of the script 

ALTER TABLE `Protein` ADD `safetyLevel` ENUM( 'GREEN', 'YELLOW', 'RED' ) NULL AFTER `acronym` ;
 
-- last line of script
update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_01_19_alterProtein.sql';
