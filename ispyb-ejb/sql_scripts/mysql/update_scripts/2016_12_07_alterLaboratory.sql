-- first line of script
insert into SchemaStatus (scriptName, schemaStatus) values ('2016_12_07_alterLaboratory.sql','ONGOING');

-- body of the script 

 ALTER TABLE `Laboratory` ADD `laboratoryExtPk` int(10) NULL DEFAULT NULL;
 
-- last line of script
update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2016_12_07_alterLaboratory.sql';
