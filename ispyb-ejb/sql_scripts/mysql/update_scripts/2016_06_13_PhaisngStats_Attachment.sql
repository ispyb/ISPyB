use pydb;

ALTER TABLE `pydb`.`PhasingStatistics` 
CHANGE COLUMN `metric` `metric` ENUM('Rcullis','Average Fragmet Length','Chain Count','Residues Count','CC','PhasingPower','FOM','<d"/sig>','Best CC','CC(1/2)','Weak CC','CFOM','Pseudo_free_CC','CC of partial model') NULL DEFAULT NULL COMMENT 'metric' ;

ALTER TABLE `pydb`.`PhasingProgramAttachment` 
CHANGE COLUMN `fileType` `fileType` ENUM('Map','Logfile','PDB','CSV','INS', 'RES', 'TXT') NULL DEFAULT NULL COMMENT 'file type' ;


ALTER TABLE `pydb`.`XFEFluorescenceSpectrum` 
ADD COLUMN `workingDirectory` VARCHAR(512) NULL AFTER `flux_end`;

ALTER TABLE `pydb`.`EnergyScan` 
ADD COLUMN `workingDirectory` VARCHAR(45) NULL AFTER `flux_end`;
