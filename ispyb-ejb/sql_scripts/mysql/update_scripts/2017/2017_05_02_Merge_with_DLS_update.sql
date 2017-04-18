insert into SchemaStatus (scriptName, schemaStatus) values ('2017_05_02_Merge_with_DLS_update.sql','ONGOING');

/**********************************************/
/** start of existing tables modifications  **/
/**********************************************/


/** JAVA MODIFICATION IS NEEDED **/
ALTER TABLE `pydb`.`AutoProcProgram`
ADD COLUMN `dataCollectionId` INT(11) UNSIGNED NULL DEFAULT NULL AFTER `autoProcProgramId`,
ADD INDEX `fk_AutoProcProgram_1_idx` (`dataCollectionId` ASC);

ALTER TABLE `pydb`.`AutoProcProgram`
   ADD CONSTRAINT `AutoProcProgram_FK1` FOREIGN KEY
          (`dataCollectionId`)
          REFERENCES `pydb`.`DataCollection`(`dataCollectionId`)
             ON DELETE NO ACTION
             ON UPDATE NO ACTION;

ALTER TABLE `AutoProcStatus`
   MODIFY `bltimeStamp`timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE `BLSample`
  MODIFY `crystalId` int(10) UNSIGNED DEFAULT NULL,

  ADD `positionId` int(11) UNSIGNED DEFAULT NULL,
  ADD `blSubSampleId` int(11) UNSIGNED DEFAULT NULL,
  ADD `screenComponentGroupId` int(11) UNSIGNED DEFAULT NULL,
  ADD `volume` float DEFAULT NULL,
  ADD `dimension1` double DEFAULT NULL,
  ADD `dimension2` double DEFAULT NULL,
  ADD `dimension3` double DEFAULT NULL,
  ADD `shape` varchar(15) DEFAULT NULL,

  ADD KEY `BLSampleImage_idx1` (`blSubSampleId`),
  ADD KEY `BLSample_fk5` (`screenComponentGroupId`);



ALTER TABLE `BLSession`
  MODIFY `beamLineOperator` VARCHAR(255) DEFAULT NULL,
  MODIFY `bltimeStamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

 ALTER TABLE `BLSubSample`
  ADD `motorPositionId` INT(11) UNSIGNED DEFAULT NULL COMMENT 'motor position',
  ADD CONSTRAINT `BLSubSample_motorPositionfk_1` FOREIGN KEY (`motorPositionId`) REFERENCES `MotorPosition` (`motorPositionId`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE `Buffer`
   ADD`BLSessionId`INT(11) UNSIGNED DEFAULT NULL;

ALTER TABLE `Container`
  ADD `screenId` INT(11) UNSIGNED DEFAULT NULL,
  ADD `scheduleId` INT(11) UNSIGNED DEFAULT NULL,
  ADD `imagerId` INT(11) UNSIGNED DEFAULT NULL,
  ADD `scLocationUpdated` DATETIME DEFAULT NULL,
  ADD `requestedImagerId` INT(11) UNSIGNED DEFAULT NULL,
  ADD `requestedReturn` TINYINT(1) DEFAULT '0' COMMENT 'True for requesting return, False means container will be disposed',
  ADD `comments` VARCHAR(255) DEFAULT NULL,
  ADD `experimentType` VARCHAR(20) DEFAULT NULL,
  ADD `storageTemperature` FLOAT DEFAULT NULL;

  ALTER TABLE `Crystal`
   ADD `abundance` FLOAT DEFAULT NULL,
   ADD `packingFraction` FLOAT DEFAULT NULL;


ALTER TABLE `DataCollection`
   ADD `blSampleId` INT(11) UNSIGNED DEFAULT NULL,
   ADD `sessionId` INT(11) UNSIGNED DEFAULT '0',
  ADD `experimentType` VARCHAR(24) DEFAULT NULL,
  ADD `crystalClass` VARCHAR(20) DEFAULT NULL,
  ADD `chiStart` FLOAT DEFAULT NULL,
  ADD `detectorMode` VARCHAR(255) DEFAULT NULL,
   ADD `actualSampleBarcode` VARCHAR(45) DEFAULT NULL,
  ADD `actualSampleSlotInContainer` INT(11) UNSIGNED DEFAULT NULL,
  ADD `actualContainerBarcode` VARCHAR(45) DEFAULT NULL,
  ADD `actualContainerSlotInSC` INT(11) UNSIGNED DEFAULT NULL,
   ADD `positionId` INT(11) UNSIGNED DEFAULT NULL,
  ADD `focalSpotSizeAtSampleX` FLOAT DEFAULT NULL,
  ADD `polarisation` FLOAT DEFAULT NULL,
  ADD `focalSpotSizeAtSampleY` FLOAT DEFAULT NULL,
  ADD `apertureId` INT(11) UNSIGNED DEFAULT NULL,
  ADD `screeningOrigId` INT(11) UNSIGNED DEFAULT NULL,
  ADD `processedDataFile` VARCHAR(255) DEFAULT NULL,
  ADD `datFullPath` VARCHAR(255) DEFAULT NULL,
  ADD `magnification` INT(11) DEFAULT NULL COMMENT 'Unit: X',
  -- ADD `totalAbsorbedDose` FLOAT DEFAULT NULL COMMENT 'Unit: e-/A^2 for EM',
  ADD `binning` TINYINT(1) DEFAULT '1' COMMENT '1 or 2. Number of pixels to process as 1. (Use mean value.)',
  ADD `particleDiameter` FLOAT DEFAULT NULL COMMENT 'Unit: nm',
  ADD `boxSize_CTF` FLOAT DEFAULT NULL COMMENT 'Unit: pixels',
  ADD `minResolution` FLOAT DEFAULT NULL COMMENT 'Unit: A',
  ADD `minDefocus` FLOAT DEFAULT NULL COMMENT 'Unit: A',
  ADD `maxDefocus` FLOAT DEFAULT NULL COMMENT 'Unit: A',
  ADD `defocusStepSize` FLOAT DEFAULT NULL COMMENT 'Unit: A',
  ADD `amountAstigmatism` FLOAT DEFAULT NULL COMMENT 'Unit: A',
  ADD `extractSize` FLOAT DEFAULT NULL COMMENT 'Unit: pixels',
  ADD `bgRadius` FLOAT DEFAULT NULL COMMENT 'Unit: nm',
  ADD `voltage` FLOAT DEFAULT NULL COMMENT 'Unit: kV',
  ADD `objAperture` FLOAT DEFAULT NULL COMMENT 'Unit: um',
  ADD `c1aperture` FLOAT DEFAULT NULL COMMENT 'Unit: um',
  ADD `c2aperture` FLOAT DEFAULT NULL COMMENT 'Unit: um',
  ADD `c3aperture` FLOAT DEFAULT NULL COMMENT 'Unit: um',
  ADD `c1lens` FLOAT DEFAULT NULL COMMENT 'Unit: %',
  ADD `c2lens` FLOAT DEFAULT NULL COMMENT 'Unit: %',
  ADD `c3lens` FLOAT DEFAULT NULL COMMENT 'Unit: %';
-- ADD `startPositionId` INT(11) UNSIGNED DEFAULT NULL,
-- ADD `endPositionId` INT(11) UNSIGNED DEFAULT NULL;

  ALTER TABLE `Detector`
  ADD `detectorMaxResolution` FLOAT DEFAULT NULL,
  ADD `detectorMinResolution` FLOAT DEFAULT NULL,
  ADD `CS` FLOAT DEFAULT NULL COMMENT 'Unit: mm',
  ADD `density` FLOAT DEFAULT NULL,
  ADD `composition` VARCHAR(16) DEFAULT NULL;


ALTER TABLE `DewarTransportHistory`

  MODIFY `storageLocation` VARCHAR(45) DEFAULT NULL,
  MODIFY `arrivalDate` DATETIME DEFAULT NULL;

ALTER TABLE `DiffractionPlan`
ADD `diffractionPlanUUID` VARCHAR(1000) DEFAULT NULL,
ADD `dataCollectionPlanGroupId` INT(11) UNSIGNED DEFAULT NULL,
  ADD `detectorId` INT(11) DEFAULT NULL,
  ADD `distance` DOUBLE DEFAULT NULL,
  ADD `orientation` DOUBLE DEFAULT NULL,
  ADD `monoBandwidth` DOUBLE DEFAULT NULL,
  ADD `monochromator` VARCHAR(8) DEFAULT NULL COMMENT 'DMM or DCM',
  ADD `energy` FLOAT DEFAULT NULL COMMENT 'eV',
  ADD `transmission` FLOAT DEFAULT NULL COMMENT 'Decimal fraction in range [0,1]',
  ADD `boxSizeX` FLOAT DEFAULT NULL COMMENT 'microns',
  ADD `boxSizeY` FLOAT DEFAULT NULL COMMENT 'microns',
  ADD `kappaStart` FLOAT DEFAULT NULL COMMENT 'degrees',
  ADD `axisStart` FLOAT DEFAULT NULL COMMENT 'degrees',
  ADD `axisRange` FLOAT DEFAULT NULL COMMENT 'degrees',
  ADD `numberOfImages` MEDIUMINT(9) DEFAULT NULL COMMENT 'The number of images requested',
  ADD `presetForProposalId` INT(10) UNSIGNED DEFAULT NULL COMMENT 'Indicates this plan is available to all sessions on given proposal',
  ADD `beamLineName` VARCHAR(45) DEFAULT NULL COMMENT 'Indicates this plan is available to all sessions on given beamline';


ALTER TABLE `FrameSet`
    ADD `filePath` VARCHAR(255) DEFAULT NULL,
    ADD `internalPath` VARCHAR(255) DEFAULT NULL;

-- not a good idea to modify Image table  : not used anymore by DLS ??
-- ALTER TABLE `image`
-- `BLTIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  ALTER TABLE `ImageQualityIndicators`
    MODIFY `imageId` INT(12) DEFAULT NULL,
    ADD `dataCollectionId` INT(11) UNSIGNED DEFAULT NULL,
    ADD `imageNumber` MEDIUMINT(8) UNSIGNED DEFAULT NULL;

ALTER TABLE `Person`
   ADD`cache`TEXT;


ALTER TABLE `Protein`
  MODIFY `sequence` TEXT,
  ADD `modId` VARCHAR(20) DEFAULT NULL,
  ADD `concentrationTypeId` INT(11) UNSIGNED DEFAULT NULL,
  ADD `global` TINYINT(1) DEFAULT '0';

ALTER TABLE `ScreeningOutput`
   ADD`screeningSuccess`TINYINT(1) DEFAULT '0';


ALTER TABLE `ScreeningOutputLattice`
   MODIFY `bltimeStamp`TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE `ScreeningStrategySubWedge`
   ADD`resolution`FLOAT DEFAULT NULL;

ALTER TABLE `Session_has_Person`
   ADD`remote`TINYINT(1) DEFAULT '0';

ALTER TABLE `Shipping`
   ADD`safetyLevel`VARCHAR(8) DEFAULT NULL;

ALTER TABLE `Frame`
   MODIFY `creationDate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   ADD `frameSetId` INT(11) UNSIGNED DEFAULT NULL;

ALTER TABLE `Phasing`
   MODIFY `recordTimeStamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE `Phasing_has_Scaling`
   MODIFY `recordTimeStamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE `Screening`
   MODIFY `bltimeStamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_05_02_Merge_with_DLS_update.sql';

