use pydb;

-- first line of script
insert into SchemaStatus (scriptName, schemaStatus) values ('2016_11_25_PhasingProgramEnumeration.sql','ONGOING');


ALTER TABLE `pydb`.`PhasingProgramAttachment` 
CHANGE COLUMN `fileType` `fileType` ENUM('IMAGE','Map','Logfile','PDB','CSV','INS','RES','TXT') NULL DEFAULT NULL COMMENT 'file type' ;

-- last line of script
update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2016_11_25_PhasingProgramEnumeration.sql';

