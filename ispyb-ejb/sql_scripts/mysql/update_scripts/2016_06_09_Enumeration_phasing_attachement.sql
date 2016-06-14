ALTER TABLE `pydb`.`PhasingProgramAttachment` 
CHANGE COLUMN `fileType` `fileType` ENUM('Map','Logfile', 'PDB', 'CSV') NULL DEFAULT NULL COMMENT 'file type' ;
