insert into SchemaStatus (scriptName, schemaStatus) values ('2017_04_05_PhasingAttachement_Enumeration.sql','ONGOING');

ALTER TABLE `pydb`.`PhasingProgramAttachment` 
CHANGE COLUMN `fileType` `fileType` ENUM('DSIGMA_RESOLUTION', 'OCCUPANCY_SITENUMBER', 'CONTRAST_CYCLE', 'CCALL_CCWEAK', 'IMAGE','Map','Logfile','PDB','CSV','INS','RES','TXT') NULL DEFAULT NULL COMMENT 'file type' ;

update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_04_05_PhasingAttachement_Enumeration.sql';

