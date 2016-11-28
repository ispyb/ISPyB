use pydb;

ALTER TABLE `pydb`.`PhasingProgramAttachment` 
CHANGE COLUMN `fileType` `fileType` ENUM('IMAGE','Map','Logfile','PDB','CSV','INS','RES','TXT') NULL DEFAULT NULL COMMENT 'file type' ;
