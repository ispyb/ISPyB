INSERT IGNORE INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2021_07_05_New_EM_tables_and_view.sql', 'ONGOING');


CREATE TABLE ParticlePicker (
  particlePickerId int unsigned auto_increment PRIMARY KEY,
  programId int unsigned,
  firstMotionCorrectionId int,
  particlePickingTemplate varchar(255) COMMENT 'Cryolo model',
  particleDiameter float COMMENT 'Unit: nm',
  numberOfParticles int unsigned,
  summaryImageFullPath varchar(255) DEFAULT NULL COMMENT 'Generated summary micrograph image with highlighted particles',
  CONSTRAINT `ParticlePicker_fk_programId`
    FOREIGN KEY (`programId`)
      REFERENCES `AutoProcProgram` (`autoProcProgramId`)
        ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT `ParticlePicker_fk_motionCorrectionId`
      FOREIGN KEY (`firstMotionCorrectionId`)
        REFERENCES `MotionCorrection` (`motionCorrectionId`)
          ON DELETE NO ACTION ON UPDATE CASCADE
)
COMMENT 'An instance of a particle picker program that was run'; 


CREATE TABLE `ParticleClassificationGroup` (
  `particleClassificationGroupId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `particlePickerId` int(10) unsigned,
  `programId` int(10) unsigned,
  `type` enum('2D','3D') COMMENT 'Indicates the type of particle classification',
  `batchNumber` int(10) unsigned DEFAULT NULL COMMENT 'Corresponding to batch number',
  `numberOfParticlesPerBatch` int(10) unsigned DEFAULT NULL COMMENT 'total number of particles per batch (a large integer)',
  `numberOfClassesPerBatch` int(10) unsigned DEFAULT NULL,
  `symmetry` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`particleClassificationGroupId`),
  KEY `ParticleClassificationGroup_fk_particlePickerId` (`particlePickerId`),
  KEY `ParticleClassificationGroup_fk_programId` (`programId`),
  CONSTRAINT `ParticleClassificationGroup_fk_particlePickerId` 
    FOREIGN KEY (`particlePickerId`) 
      REFERENCES `ParticlePicker` (`particlePickerId`) 
        ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ParticleClassificationGroup_fk_programId` 
    FOREIGN KEY (`programId`) 
      REFERENCES `AutoProcProgram` (`autoProcProgramId`) 
        ON DELETE NO ACTION ON UPDATE CASCADE
) ;


CREATE TABLE `ParticleClassification` (
  `particleClassificationId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `particleClassificationGroupId` int(10) unsigned,
  `classNumber` int(10) unsigned COMMENT 'Identified of the class. A unique ID given by Relion',
  `classImageFullPath` varchar(255) COMMENT 'The PNG of the class',
  `particlesPerClass` int(10) unsigned COMMENT 'Number of particles within the selected class, can then be used together with the total number above to calculate the percentage',
  `classDistribution` float DEFAULT NULL,
  `rotationAccuracy` float unsigned DEFAULT NULL,
  `translationAccuracy` float DEFAULT NULL COMMENT 'Unit: Angstroms',
  `estimatedResolution` float DEFAULT NULL COMMENT 'Unit: Angstroms',
  `overallFourierCompleteness` float DEFAULT NULL,
  PRIMARY KEY (`particleClassificationId`),
  KEY `ParticleClassification_fk_particleClassificationGroupId` (`particleClassificationGroupId`),
  CONSTRAINT `ParticleClassification_fk_particleClassificationGroupId` 
    FOREIGN KEY (`particleClassificationGroupId`) 
      REFERENCES `ParticleClassificationGroup` (`particleClassificationGroupId`) 
        ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT='Results of 2D or 3D classification';

CREATE TABLE CryoemInitialModel (
  cryoemInitialModelId int unsigned auto_increment PRIMARY KEY,
  resolution float COMMENT 'Unit: Angstroms',
  numberOfParticles int unsigned
)
COMMENT 'Initial cryo-EM model generation results'; 

CREATE TABLE `ParticleClassification_has_CryoemInitialModel` (
  `particleClassificationId` int(10) unsigned NOT NULL,
  `cryoemInitialModelId` int(10) unsigned NOT NULL,
  PRIMARY KEY (`particleClassificationId`,`cryoemInitialModelId`),
  KEY `ParticleClassification_has_InitialModel_fk2` (`cryoemInitialModelId`),
  CONSTRAINT `ParticleClassification_has_CryoemInitialModel_fk1` 
    FOREIGN KEY (`particleClassificationId`) 
      REFERENCES `ParticleClassification` (`particleClassificationId`) 
        ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ParticleClassification_has_InitialModel_fk2` 
    FOREIGN KEY (`cryoemInitialModelId`) 
      REFERENCES `CryoemInitialModel` (`cryoemInitialModelId`) 
        ON DELETE CASCADE ON UPDATE CASCADE
) ;


CREATE 
    ALGORITHM = MERGE 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `pydb`.`v_em_classification` AS
    SELECT 
        `pydb`.`Proposal`.`proposalId` AS `proposalId`,
        `pydb`.`BLSession`.`sessionId` AS `sessionId`,
        `pydb`.`DataCollection`.`imageDirectory` AS `imageDirectory`,
        `pydb`.`ParticlePicker`.`particlePickerId` AS `particlePickerId`,
        `pydb`.`ParticlePicker`.`numberOfParticles` AS `numberOfParticles`,
        `pydb`.`ParticleClassificationGroup`.`particleClassificationGroupId` AS `particleClassificationGroupId`,
        `pydb`.`ParticleClassification`.`particleClassificationId` AS `particleClassificationId`,
        `pydb`.`ParticleClassification`.`classNumber` AS `classNumber`,
        `pydb`.`ParticleClassification`.`classImageFullPath` AS `classImageFullPath`,
        `pydb`.`ParticleClassification`.`particlesPerClass` AS `particlesPerClass`,
        `pydb`.`ParticleClassification`.`classDistribution` AS `classDistribution`,
        `pydb`.`ParticleClassification`.`rotationAccuracy` AS `rotationAccuracy`,
        `pydb`.`ParticleClassification`.`translationAccuracy` AS `translationAccuracy`,
        `pydb`.`ParticleClassification`.`estimatedResolution` AS `estimatedResolution`,
        `pydb`.`ParticleClassification`.`overallFourierCompleteness` AS `overallFourierCompleteness`
    FROM
        ((((((((`pydb`.`BLSession`
        JOIN `pydb`.`Proposal` ON ((`pydb`.`Proposal`.`proposalId` = `pydb`.`BLSession`.`proposalId`)))
        JOIN `pydb`.`DataCollectionGroup` ON ((`pydb`.`DataCollectionGroup`.`sessionId` = `pydb`.`BLSession`.`sessionId`)))
        JOIN `pydb`.`DataCollection` ON ((`pydb`.`DataCollection`.`dataCollectionGroupId` = `pydb`.`DataCollectionGroup`.`dataCollectionGroupId`)))
        JOIN `pydb`.`Movie` ON ((`pydb`.`Movie`.`dataCollectionId` = `pydb`.`DataCollection`.`dataCollectionId`)))
        JOIN `pydb`.`MotionCorrection` ON ((`pydb`.`MotionCorrection`.`movieId` = `pydb`.`Movie`.`movieId`)))
        JOIN `pydb`.`ParticlePicker` ON ((`pydb`.`ParticlePicker`.`firstMotionCorrectionId` = `pydb`.`MotionCorrection`.`motionCorrectionId`)))
        JOIN `pydb`.`ParticleClassificationGroup` ON ((`pydb`.`ParticleClassificationGroup`.`particlePickerId` = `pydb`.`ParticlePicker`.`particlePickerId`)))
        JOIN `pydb`.`ParticleClassification` ON ((`pydb`.`ParticleClassification`.`particleClassificationGroupId` = `pydb`.`ParticleClassificationGroup`.`particleClassificationGroupId`)));


UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2021_07_05_New_EM_tables_and_view.sql';
