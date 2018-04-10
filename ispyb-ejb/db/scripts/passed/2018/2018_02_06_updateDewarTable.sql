insert into `pydb`.SchemaStatus (scriptName, schemaStatus) values ('2018_02_06_updateDewarTable.sql','ONGOING');

ALTER TABLE `pydb`.`Dewar` 
ADD COLUMN `isReimbursed` tinyint(1)  NULL DEFAULT '0' comment 'set this dewar as reimbursed by the user office';


update `pydb`.SchemaStatus set schemaStatus = 'DONE' where scriptName = '2018_02_06_updateDewarTable.sql';  
