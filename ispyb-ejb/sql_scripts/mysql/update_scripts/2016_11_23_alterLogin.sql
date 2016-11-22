-- first line of script
insert into SchemaStatus (scriptName, schemaStatus) values ('2016_11_23_alterLogin.sql','ONGOING');

-- body of the script 

 ALTER TABLE `Login` ADD `authorized` VARCHAR(1024) NULL DEFAULT NULL AFTER `roles` ;
 
-- last line of script
update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2016_11_23_alterLogin.sql';
