insert into `pydb`.SchemaStatus (scriptName, schemaStatus) values ('20171022_EM.sql','ONGOING');

CREATE TABLE IF NOT EXISTS `pydb`.`Movie` (
  `movieId` INT(11) NOT NULL AUTO_INCREMENT,
  `dataCollectionId` INT(11) UNSIGNED NULL DEFAULT NULL,
  `movieNumber` INT NULL,
  `movieFullPath` VARCHAR(255) NULL,
  `positionX` VARCHAR(45) NULL,
  `positionY` VARCHAR(45) NULL,
  `micrographFullPath` VARCHAR(255) NULL,
  `micrographSnapshotFullPath` VARCHAR(255) NULL,
  `xmlMetaDataFullPath` VARCHAR(255) NULL,
  `dosePerImage` VARCHAR(45) NULL,
  `createdTimeStamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`movieId`),
  INDEX `dataCollectionToMovie_idx` (`dataCollectionId` ASC),
  CONSTRAINT `dataCollectionToMovie`
    FOREIGN KEY (`dataCollectionId`)
    REFERENCES `pydb`.`DataCollection` (`dataCollectionId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
   
 
    
    CREATE TABLE `pydb`.`MotionCorrection` (
  `motionCorrectionId` INT(11) NOT NULL AUTO_INCREMENT,
  `movieId` INT(11) NULL,
  `firstFrame` VARCHAR(45) NULL,
  `lastFrame` VARCHAR(45) NULL,
  `dosePerFrame` VARCHAR(45) NULL,
  `doseWeight` VARCHAR(45) NULL,
  `totalMotion` VARCHAR(45) NULL,
  `averageMotionPerFrame` VARCHAR(45) NULL,
  `driftPlotFullPath` VARCHAR(255) NULL,
  `micrographFullPath` VARCHAR(255) NULL,
  `micrographSnapshotFullPath` VARCHAR(255) NULL,
  `correctedDoseMicrographFullPath` VARCHAR(255) NULL,
  `patchesUsed` VARCHAR(45) NULL,
  `logFileFullPath` VARCHAR(255) NULL,
  `createdTimeStamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`motionCorrectionId`),
  INDEX `fk_MotionCorrection_1_idx` (`movieId` ASC),
  CONSTRAINT `fk_MotionCorrection_1`
    FOREIGN KEY (`movieId`)
    REFERENCES `pydb`.`Movie` (`movieId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

    
    
CREATE TABLE `pydb`.`CTF` (
  `CTFid` INT NOT NULL,
  `motionCorrectionId` INT(11) NOT NULL,
  `spectraImageThumbnailFullPath` VARCHAR(255) NULL,
  `spectraImageFullPath` VARCHAR(255) NULL,
  `defocusU` VARCHAR(45) NULL,
  `defocusV` VARCHAR(45) NULL,
  `angle` VARCHAR(45) NULL,
  `crossCorrelationCoefficient` VARCHAR(45) NULL,
  `resolutionLimit` VARCHAR(45) NULL,
  `estimatedBfactor` VARCHAR(45) NULL,
  `logFilePath` VARCHAR(255) NULL,
   `createdTimeStamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`CTFid`));

  
  ALTER TABLE `pydb`.`CTF` 
ADD INDEX `fk_CTF_1_idx` (`motionCorrectionId` ASC);
ALTER TABLE `pydb`.`CTF` 
ADD CONSTRAINT `fk_CTF_1`
  FOREIGN KEY (`motionCorrectionId`)
  REFERENCES `pydb`.`MotionCorrection` (`motionCorrectionId`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

  
ALTER TABLE `pydb`.`CTF` 
CHANGE COLUMN `CTFid` `CTFid` INT(11) NOT NULL AUTO_INCREMENT ;

#ALTER TABLE `pydb`.`DataCollectionGroup` 
#CHANGE COLUMN `experimentType` `experimentType` ENUM('EM', 'SAD','SAD - Inverse Beam','OSC','Collect - Multiwedge','MAD','Helical','Multi-positional','Mesh','Burn','MAD - Inverse Beam','Characterization','Dehydration') NULL DEFAULT NULL COMMENT 'Experiment type flag' ;
    
#ALTER TABLE `pydb`.`Movie` 
#CHANGE COLUMN `createdTimeStamp` `createdTimeStamp` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ;

#ALTER TABLE `pydb`.`Movie` 
#CHANGE COLUMN `createdTimeStamp` `createdTimeStamp` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ;

#ALTER TABLE `pydb`.`MotionCorrection` 
#CHANGE COLUMN `createdTimeStamp` `createdTimeStamp` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ;

ALTER TABLE `pydb`.`MotionCorrection` 
CHANGE COLUMN `driftPlotFullPath` `driftPlotFullPath` VARCHAR(512) NULL DEFAULT NULL ,
CHANGE COLUMN `micrographFullPath` `micrographFullPath` VARCHAR(512) NULL DEFAULT NULL ,
CHANGE COLUMN `micrographSnapshotFullPath` `micrographSnapshotFullPath` VARCHAR(512) NULL DEFAULT NULL ,
CHANGE COLUMN `correctedDoseMicrographFullPath` `correctedDoseMicrographFullPath` VARCHAR(512) NULL DEFAULT NULL ,
CHANGE COLUMN `logFileFullPath` `logFileFullPath` VARCHAR(512) NULL DEFAULT NULL ;


ALTER TABLE `pydb`.`BeamLineSetup` 
ADD COLUMN `CS` FLOAT NULL AFTER `minTransmission`;

update `pydb`.SchemaStatus set schemaStatus = 'DONE' where scriptName = '20171022_EM.sql';  