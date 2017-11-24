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

ALTER TABLE `pydb`.`DataCollectionGroup` 
CHANGE COLUMN `experimentType` `experimentType` ENUM('EM', 'SAD','SAD - Inverse Beam','OSC','Collect - Multiwedge','MAD','Helical','Multi-positional','Mesh','Burn','MAD - Inverse Beam','Characterization','Dehydration') NULL DEFAULT NULL COMMENT 'Experiment type flag' ;


CREATE 
    ALGORITHM = MERGE 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `pydb`.`v_em_movie` AS
    SELECT 
        `pydb`.`Movie`.`movieId` AS `Movie_movieId`,
        `pydb`.`Movie`.`dataCollectionId` AS `Movie_dataCollectionId`,
        `pydb`.`Movie`.`movieNumber` AS `Movie_movieNumber`,
        `pydb`.`Movie`.`movieFullPath` AS `Movie_movieFullPath`,
        `pydb`.`Movie`.`positionX` AS `Movie_positionX`,
        `pydb`.`Movie`.`positionY` AS `Movie_positionY`,
        `pydb`.`Movie`.`micrographFullPath` AS `Movie_micrographFullPath`,
        `pydb`.`Movie`.`micrographSnapshotFullPath` AS `Movie_micrographSnapshotFullPath`,
        `pydb`.`Movie`.`xmlMetaDataFullPath` AS `Movie_xmlMetaDataFullPath`,
        `pydb`.`Movie`.`dosePerImage` AS `Movie_dosePerImage`,
        `pydb`.`Movie`.`createdTimeStamp` AS `Movie_createdTimeStamp`,
        `pydb`.`MotionCorrection`.`motionCorrectionId` AS `MotionCorrection_motionCorrectionId`,
        `pydb`.`MotionCorrection`.`movieId` AS `MotionCorrection_movieId`,
        `pydb`.`MotionCorrection`.`firstFrame` AS `MotionCorrection_firstFrame`,
        `pydb`.`MotionCorrection`.`lastFrame` AS `MotionCorrection_lastFrame`,
        `pydb`.`MotionCorrection`.`dosePerFrame` AS `MotionCorrection_dosePerFrame`,
        `pydb`.`MotionCorrection`.`doseWeight` AS `MotionCorrection_doseWeight`,
        `pydb`.`MotionCorrection`.`totalMotion` AS `MotionCorrection_totalMotion`,
        `pydb`.`MotionCorrection`.`averageMotionPerFrame` AS `MotionCorrection_averageMotionPerFrame`,
        `pydb`.`MotionCorrection`.`driftPlotFullPath` AS `MotionCorrection_driftPlotFullPath`,
        `pydb`.`MotionCorrection`.`micrographFullPath` AS `MotionCorrection_micrographFullPath`,
        `pydb`.`MotionCorrection`.`micrographSnapshotFullPath` AS `MotionCorrection_micrographSnapshotFullPath`,
        `pydb`.`MotionCorrection`.`correctedDoseMicrographFullPath` AS `MotionCorrection_correctedDoseMicrographFullPath`,
        `pydb`.`MotionCorrection`.`patchesUsed` AS `MotionCorrection_patchesUsed`,
        `pydb`.`MotionCorrection`.`logFileFullPath` AS `MotionCorrection_logFileFullPath`,
        `pydb`.`CTF`.`CTFid` AS `CTF_CTFid`,
        `pydb`.`CTF`.`motionCorrectionId` AS `CTF_motionCorrectionId`,
        `pydb`.`CTF`.`spectraImageThumbnailFullPath` AS `CTF_spectraImageThumbnailFullPath`,
        `pydb`.`CTF`.`spectraImageFullPath` AS `CTF_spectraImageFullPath`,
        `pydb`.`CTF`.`defocusU` AS `CTF_defocusU`,
        `pydb`.`CTF`.`defocusV` AS `CTF_defocusV`,
        `pydb`.`CTF`.`angle` AS `CTF_angle`,
        `pydb`.`CTF`.`crossCorrelationCoefficient` AS `CTF_crossCorrelationCoefficient`,
        `pydb`.`CTF`.`resolutionLimit` AS `CTF_resolutionLimit`,
        `pydb`.`CTF`.`estimatedBfactor` AS `CTF_estimatedBfactor`,
        `pydb`.`CTF`.`logFilePath` AS `CTF_logFilePath`,
        `pydb`.`CTF`.`createdTimeStamp` AS `CTF_createdTimeStamp`,
        `pydb`.`Proposal`.`proposalId` AS `Proposal_proposalId`,
        `pydb`.`BLSession`.`sessionId` AS `BLSession_sessionId`
    FROM
        ((((((`pydb`.`Movie`
        LEFT JOIN `pydb`.`MotionCorrection` ON ((`pydb`.`MotionCorrection`.`movieId` = `pydb`.`Movie`.`movieId`)))
        LEFT JOIN `pydb`.`DataCollection` ON ((`pydb`.`DataCollection`.`dataCollectionId` = `pydb`.`Movie`.`dataCollectionId`)))
        LEFT JOIN `pydb`.`DataCollectionGroup` ON ((`pydb`.`DataCollectionGroup`.`dataCollectionGroupId` = `pydb`.`DataCollection`.`dataCollectionGroupId`)))
        LEFT JOIN `pydb`.`BLSession` ON ((`pydb`.`BLSession`.`sessionId` = `pydb`.`DataCollectionGroup`.`sessionId`)))
        LEFT JOIN `pydb`.`Proposal` ON ((`pydb`.`Proposal`.`proposalId` = `pydb`.`BLSession`.`proposalId`)))
        LEFT JOIN `pydb`.`CTF` ON ((`pydb`.`CTF`.`motionCorrectionId` = `pydb`.`MotionCorrection`.`motionCorrectionId`)));
update `pydb`.SchemaStatus set schemaStatus = 'DONE' where scriptName = '20171022_EM.sql';  