use pydb;
insert into SchemaStatus (scriptName, schemaStatus) values ('2017_06_06_ProcessingStatus_to_enumeration.sql','ONGOING');

ALTER TABLE `pydb`.`AutoProcProgram` 
CHANGE COLUMN `processingStatus` `processingStatus` ENUM('RUNNING', 'FAILED', 'SUCCESS', '0', '1') NULL DEFAULT NULL COMMENT 'success (1) / fail (0)' ;


update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_06_06_ProcessingStatus_to_enumeration.sql';
