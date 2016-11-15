-- phpMyAdmin SQL Dump
-- version 3.4.11.1deb2+deb7u2
-- http://www.phpmyadmin.net
--
-- Host: pyproserv
-- Generation Time: Nov 15, 2016 at 09:48 AM
-- Server version: 5.5.40
-- PHP Version: 5.4.45-0+deb7u2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `pydb`
--

-- --------------------------------------------------------

--
-- Table structure for table `AbInitioModel`
--

CREATE TABLE IF NOT EXISTS `AbInitioModel` (
  `abInitioModelId` int(10) NOT NULL AUTO_INCREMENT,
  `modelListId` int(10) DEFAULT NULL,
  `averagedModelId` int(10) DEFAULT NULL,
  `rapidShapeDeterminationModelId` int(10) DEFAULT NULL,
  `shapeDeterminationModelId` int(10) DEFAULT NULL,
  `comments` varchar(512) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  PRIMARY KEY (`abInitioModelId`),
  KEY `AbInitioModelToModelList` (`modelListId`),
  KEY `AverageToModel` (`averagedModelId`),
  KEY `AbInitioModelToRapid` (`rapidShapeDeterminationModelId`),
  KEY `SahpeDeterminationToAbiniti` (`shapeDeterminationModelId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=31961 ;

-- --------------------------------------------------------

--
-- Table structure for table `Additive`
--

CREATE TABLE IF NOT EXISTS `Additive` (
  `additiveId` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `additiveType` varchar(45) DEFAULT NULL,
  `comments` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`additiveId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `AdminActivity`
--

CREATE TABLE IF NOT EXISTS `AdminActivity` (
  `adminActivityId` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL DEFAULT '',
  `action` varchar(45) DEFAULT NULL,
  `comments` varchar(100) DEFAULT NULL,
  `dateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`adminActivityId`),
  UNIQUE KEY `username` (`username`),
  KEY `AdminActivity_FKAction` (`action`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1988 ;

-- --------------------------------------------------------

--
-- Table structure for table `AdminVar`
--

CREATE TABLE IF NOT EXISTS `AdminVar` (
  `varId` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  `value` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`varId`),
  KEY `AdminVar_FKIndexName` (`name`),
  KEY `AdminVar_FKIndexValue` (`value`(767))
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='ISPyB administration values' AUTO_INCREMENT=12 ;

-- --------------------------------------------------------

--
-- Table structure for table `Assembly`
--

CREATE TABLE IF NOT EXISTS `Assembly` (
  `assemblyId` int(10) NOT NULL AUTO_INCREMENT,
  `macromoleculeId` int(10) NOT NULL,
  `creationDate` datetime DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`assemblyId`),
  KEY `AssemblyToMacromolecule` (`macromoleculeId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

-- --------------------------------------------------------

--
-- Table structure for table `AssemblyHasMacromolecule`
--

CREATE TABLE IF NOT EXISTS `AssemblyHasMacromolecule` (
  `AssemblyHasMacromoleculeId` int(10) NOT NULL AUTO_INCREMENT,
  `assemblyId` int(10) NOT NULL,
  `macromoleculeId` int(10) NOT NULL,
  PRIMARY KEY (`AssemblyHasMacromoleculeId`),
  KEY `AssemblyHasMacromoleculeToAssembly` (`assemblyId`),
  KEY `AssemblyHasMacromoleculeToAssemblyRegion` (`macromoleculeId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=12 ;

-- --------------------------------------------------------

--
-- Table structure for table `AssemblyRegion`
--

CREATE TABLE IF NOT EXISTS `AssemblyRegion` (
  `assemblyRegionId` int(10) NOT NULL AUTO_INCREMENT,
  `assemblyHasMacromoleculeId` int(10) NOT NULL,
  `assemblyRegionType` varchar(45) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `fromResiduesBases` varchar(45) DEFAULT NULL,
  `toResiduesBases` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`assemblyRegionId`),
  KEY `AssemblyRegionToAssemblyHasMacromolecule` (`assemblyHasMacromoleculeId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProc`
--

CREATE TABLE IF NOT EXISTS `AutoProc` (
  `autoProcId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `autoProcProgramId` int(10) unsigned DEFAULT NULL COMMENT 'Related program item',
  `spaceGroup` varchar(45) DEFAULT NULL COMMENT 'Space group',
  `refinedCell_a` float DEFAULT NULL COMMENT 'Refined cell',
  `refinedCell_b` float DEFAULT NULL COMMENT 'Refined cell',
  `refinedCell_c` float DEFAULT NULL COMMENT 'Refined cell',
  `refinedCell_alpha` float DEFAULT NULL COMMENT 'Refined cell',
  `refinedCell_beta` float DEFAULT NULL COMMENT 'Refined cell',
  `refinedCell_gamma` float DEFAULT NULL COMMENT 'Refined cell',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`autoProcId`),
  KEY `AutoProc_FKIndex1` (`autoProcProgramId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1269269 ;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcIntegration`
--

CREATE TABLE IF NOT EXISTS `AutoProcIntegration` (
  `autoProcIntegrationId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `dataCollectionId` int(11) unsigned NOT NULL COMMENT 'DataCollection item',
  `autoProcProgramId` int(10) unsigned DEFAULT NULL COMMENT 'Related program item',
  `startImageNumber` int(10) unsigned DEFAULT NULL COMMENT 'start image number',
  `endImageNumber` int(10) unsigned DEFAULT NULL COMMENT 'end image number',
  `refinedDetectorDistance` float DEFAULT NULL COMMENT 'Refined DataCollection.detectorDistance',
  `refinedXBeam` float DEFAULT NULL COMMENT 'Refined DataCollection.xBeam',
  `refinedYBeam` float DEFAULT NULL COMMENT 'Refined DataCollection.yBeam',
  `rotationAxisX` float DEFAULT NULL COMMENT 'Rotation axis',
  `rotationAxisY` float DEFAULT NULL COMMENT 'Rotation axis',
  `rotationAxisZ` float DEFAULT NULL COMMENT 'Rotation axis',
  `beamVectorX` float DEFAULT NULL COMMENT 'Beam vector',
  `beamVectorY` float DEFAULT NULL COMMENT 'Beam vector',
  `beamVectorZ` float DEFAULT NULL COMMENT 'Beam vector',
  `cell_a` float DEFAULT NULL COMMENT 'Unit cell',
  `cell_b` float DEFAULT NULL COMMENT 'Unit cell',
  `cell_c` float DEFAULT NULL COMMENT 'Unit cell',
  `cell_alpha` float DEFAULT NULL COMMENT 'Unit cell',
  `cell_beta` float DEFAULT NULL COMMENT 'Unit cell',
  `cell_gamma` float DEFAULT NULL COMMENT 'Unit cell',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  `anomalous` tinyint(1) DEFAULT '0' COMMENT 'boolean type:0 noanoum - 1 anoum',
  PRIMARY KEY (`autoProcIntegrationId`),
  KEY `AutoProcIntegrationIdx1` (`dataCollectionId`),
  KEY `AutoProcIntegration_FKIndex1` (`autoProcProgramId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1416389 ;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcProgram`
--

CREATE TABLE IF NOT EXISTS `AutoProcProgram` (
  `autoProcProgramId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `processingCommandLine` varchar(255) DEFAULT NULL COMMENT 'Command line for running the automatic processing',
  `processingPrograms` varchar(255) DEFAULT NULL COMMENT 'Processing programs (comma separated)',
  `processingStatus` tinyint(1) DEFAULT NULL COMMENT 'success (1) / fail (0)',
  `processingMessage` varchar(255) DEFAULT NULL COMMENT 'warning, error,...',
  `processingStartTime` datetime DEFAULT NULL COMMENT 'Processing start time',
  `processingEndTime` datetime DEFAULT NULL COMMENT 'Processing end time',
  `processingEnvironment` varchar(255) DEFAULT NULL COMMENT 'Cpus, Nodes,...',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`autoProcProgramId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1275782 ;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcProgramAttachment`
--

CREATE TABLE IF NOT EXISTS `AutoProcProgramAttachment` (
  `autoProcProgramAttachmentId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `autoProcProgramId` int(10) unsigned NOT NULL COMMENT 'Related autoProcProgram item',
  `fileType` enum('Log','Result','Graph') DEFAULT NULL COMMENT 'Type of file Attachment',
  `fileName` varchar(255) DEFAULT NULL COMMENT 'Attachment filename',
  `filePath` varchar(255) DEFAULT NULL COMMENT 'Attachment filepath to disk storage',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`autoProcProgramAttachmentId`),
  KEY `AutoProcProgramAttachmentIdx1` (`autoProcProgramId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=13444570 ;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcScaling`
--

CREATE TABLE IF NOT EXISTS `AutoProcScaling` (
  `autoProcScalingId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `autoProcId` int(10) unsigned DEFAULT NULL COMMENT 'Related autoProc item (used by foreign key)',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`autoProcScalingId`),
  KEY `AutoProcScalingFk1` (`autoProcId`),
  KEY `AutoProcScalingIdx1` (`autoProcScalingId`,`autoProcId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1269280 ;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcScalingStatistics`
--

CREATE TABLE IF NOT EXISTS `AutoProcScalingStatistics` (
  `autoProcScalingStatisticsId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `autoProcScalingId` int(10) unsigned DEFAULT NULL COMMENT 'Related autoProcScaling item (used by foreign key)',
  `scalingStatisticsType` enum('overall','innerShell','outerShell') NOT NULL DEFAULT 'overall' COMMENT 'Scaling statistics type',
  `comments` varchar(255) DEFAULT NULL COMMENT 'Comments...',
  `resolutionLimitLow` float DEFAULT NULL COMMENT 'Low resolution limit',
  `resolutionLimitHigh` float DEFAULT NULL COMMENT 'High resolution limit',
  `rMerge` float DEFAULT NULL COMMENT 'Rmerge',
  `rMeasWithinIPlusIMinus` float DEFAULT NULL COMMENT 'Rmeas (within I+/I-)',
  `rMeasAllIPlusIMinus` float DEFAULT NULL COMMENT 'Rmeas (all I+ & I-)',
  `rPimWithinIPlusIMinus` float DEFAULT NULL COMMENT 'Rpim (within I+/I-) ',
  `rPimAllIPlusIMinus` float DEFAULT NULL COMMENT 'Rpim (all I+ & I-)',
  `fractionalPartialBias` float DEFAULT NULL COMMENT 'Fractional partial bias',
  `nTotalObservations` int(10) DEFAULT NULL COMMENT 'Total number of observations',
  `nTotalUniqueObservations` int(10) DEFAULT NULL COMMENT 'Total number unique',
  `meanIOverSigI` float DEFAULT NULL COMMENT 'Mean((I)/sd(I))',
  `completeness` float DEFAULT NULL COMMENT 'Completeness',
  `multiplicity` float DEFAULT NULL COMMENT 'Multiplicity',
  `anomalousCompleteness` float DEFAULT NULL COMMENT 'Anomalous completeness',
  `anomalousMultiplicity` float DEFAULT NULL COMMENT 'Anomalous multiplicity',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  `anomalous` tinyint(1) DEFAULT '0' COMMENT 'boolean type:0 noanoum - 1 anoum',
  `ccHalf` float DEFAULT NULL COMMENT 'information from XDS',
  PRIMARY KEY (`autoProcScalingStatisticsId`),
  KEY `AutoProcScalingStatisticsIdx1` (`autoProcScalingId`),
  KEY `AutoProcScalingStatistics_FKindexType` (`scalingStatisticsType`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3882871 ;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcScaling_has_Int`
--

CREATE TABLE IF NOT EXISTS `AutoProcScaling_has_Int` (
  `autoProcScaling_has_IntId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `autoProcScalingId` int(10) unsigned DEFAULT NULL COMMENT 'AutoProcScaling item',
  `autoProcIntegrationId` int(10) unsigned NOT NULL COMMENT 'AutoProcIntegration item',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`autoProcScaling_has_IntId`),
  KEY `AutoProcScl_has_IntIdx1` (`autoProcScalingId`),
  KEY `AutoProcScal_has_IntIdx2` (`autoProcIntegrationId`),
  KEY `AutoProcScalingHasInt_FKIndex3` (`autoProcScalingId`,`autoProcIntegrationId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1269220 ;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcStatus`
--

CREATE TABLE IF NOT EXISTS `AutoProcStatus` (
  `autoProcStatusId` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `autoProcIntegrationId` int(10) unsigned NOT NULL,
  `step` enum('Indexing','Integration','Correction','Scaling','Importing') NOT NULL COMMENT 'autoprocessing step',
  `status` enum('Launched','Successful','Failed') NOT NULL COMMENT 'autoprocessing status',
  `comments` varchar(1024) DEFAULT NULL COMMENT 'comments',
  `bltimeStamp` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`autoProcStatusId`),
  KEY `AutoProcStatus_FKIndex1` (`autoProcIntegrationId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='AutoProcStatus table is linked to AutoProcIntegration' AUTO_INCREMENT=1960768 ;

-- --------------------------------------------------------

--
-- Table structure for table `BeamLineSetup`
--

CREATE TABLE IF NOT EXISTS `BeamLineSetup` (
  `beamLineSetupId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `synchrotronMode` varchar(255) DEFAULT NULL,
  `undulatorType1` varchar(45) DEFAULT NULL,
  `undulatorType2` varchar(45) DEFAULT NULL,
  `undulatorType3` varchar(45) DEFAULT NULL,
  `focalSpotSizeAtSample` float DEFAULT NULL,
  `focusingOptic` varchar(255) DEFAULT NULL,
  `beamDivergenceHorizontal` float DEFAULT NULL,
  `beamDivergenceVertical` float DEFAULT NULL,
  `polarisation` float DEFAULT NULL,
  `monochromatorType` varchar(255) DEFAULT NULL,
  `setupDate` datetime DEFAULT NULL,
  `synchrotronName` varchar(255) DEFAULT NULL,
  `maxExpTimePerDataCollection` double DEFAULT NULL,
  `minExposureTimePerImage` double DEFAULT NULL,
  `goniostatMaxOscillationSpeed` double DEFAULT NULL,
  `goniostatMinOscillationWidth` double DEFAULT NULL,
  `minTransmission` double DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`beamLineSetupId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1194287 ;

-- --------------------------------------------------------

--
-- Table structure for table `BLSample`
--

CREATE TABLE IF NOT EXISTS `BLSample` (
  `blSampleId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `diffractionPlanId` int(10) unsigned DEFAULT NULL,
  `crystalId` int(10) unsigned NOT NULL DEFAULT '0',
  `containerId` int(10) unsigned DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL,
  `location` varchar(45) DEFAULT NULL,
  `holderLength` double DEFAULT NULL,
  `loopLength` double DEFAULT NULL,
  `loopType` varchar(45) DEFAULT NULL,
  `wireWidth` double DEFAULT NULL,
  `comments` varchar(1024) DEFAULT NULL,
  `completionStage` varchar(45) DEFAULT NULL,
  `structureStage` varchar(45) DEFAULT NULL,
  `publicationStage` varchar(45) DEFAULT NULL,
  `publicationComments` varchar(255) DEFAULT NULL,
  `blSampleStatus` varchar(20) DEFAULT NULL,
  `isInSampleChanger` tinyint(1) DEFAULT NULL,
  `lastKnownCenteringPosition` varchar(255) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  `SMILES` varchar(400) DEFAULT NULL COMMENT 'the symbolic description of the structure of a chemical compound',
  `lastImageURL` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`blSampleId`),
  KEY `BLSample_FKIndex1` (`containerId`),
  KEY `BLSample_FKIndex2` (`crystalId`),
  KEY `BLSample_FKIndex3` (`diffractionPlanId`),
  KEY `crystalId` (`crystalId`,`containerId`),
  KEY `BLSample_Index1` (`name`) USING BTREE,
  KEY `BLSample_FKIndex_Status` (`blSampleStatus`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=570032 ;

-- --------------------------------------------------------

--
-- Table structure for table `BLSession`
--

CREATE TABLE IF NOT EXISTS `BLSession` (
  `sessionId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `expSessionPk` int(11) unsigned DEFAULT NULL COMMENT 'smis session Pk ',
  `beamLineSetupId` int(10) unsigned DEFAULT NULL,
  `proposalId` int(10) unsigned NOT NULL DEFAULT '0',
  `projectCode` varchar(45) DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `beamLineName` varchar(45) DEFAULT NULL,
  `scheduled` tinyint(1) DEFAULT NULL,
  `nbShifts` int(10) unsigned DEFAULT NULL,
  `comments` varchar(2000) DEFAULT NULL,
  `beamLineOperator` varchar(45) DEFAULT NULL,
  `visit_number` int(10) unsigned DEFAULT '0',
  `bltimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usedFlag` tinyint(1) DEFAULT NULL COMMENT 'indicates if session has Datacollections or XFE or EnergyScans attached',
  `sessionTitle` varchar(255) DEFAULT NULL COMMENT 'fx accounts only',
  `structureDeterminations` float DEFAULT NULL,
  `dewarTransport` float DEFAULT NULL,
  `databackupFrance` float DEFAULT NULL COMMENT 'data backup and express delivery France',
  `databackupEurope` float DEFAULT NULL COMMENT 'data backup and express delivery Europe',
  `operatorSiteNumber` varchar(10) DEFAULT NULL COMMENT 'matricule site',
  `lastUpdate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'last update timestamp: by default the end of the session, the last collect...',
  `protectedData` varchar(1024) DEFAULT NULL COMMENT 'indicates if the data are protected or not',
  `externalId` binary(16) DEFAULT NULL,
  PRIMARY KEY (`sessionId`),
  KEY `Session_FKIndex1` (`proposalId`),
  KEY `Session_FKIndex2` (`beamLineSetupId`),
  KEY `Session_FKIndexStartDate` (`startDate`),
  KEY `Session_FKIndexEndDate` (`endDate`),
  KEY `Session_FKIndexBeamLineName` (`beamLineName`),
  KEY `BLSession_FKIndexOperatorSiteNumber` (`operatorSiteNumber`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=56129 ;

-- --------------------------------------------------------

--
-- Table structure for table `BLSession_has_SCPosition`
--

CREATE TABLE IF NOT EXISTS `BLSession_has_SCPosition` (
  `blsessionhasscpositionid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `blsessionid` int(11) unsigned NOT NULL,
  `scContainer` smallint(5) unsigned DEFAULT NULL COMMENT 'Position of container within sample changer',
  `containerPosition` smallint(5) unsigned DEFAULT NULL COMMENT 'Position of sample within container',
  PRIMARY KEY (`blsessionhasscpositionid`),
  KEY `blsession_has_scposition_FK1` (`blsessionid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `BLSubSample`
--

CREATE TABLE IF NOT EXISTS `BLSubSample` (
  `blSubSampleId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `blSampleId` int(10) unsigned NOT NULL COMMENT 'sample',
  `diffractionPlanId` int(10) unsigned DEFAULT NULL COMMENT 'eventually diffractionPlan',
  `positionId` int(11) unsigned DEFAULT NULL COMMENT 'position of the subsample',
  `position2Id` int(11) unsigned DEFAULT NULL,
  `blSubSampleUUID` varchar(45) DEFAULT NULL COMMENT 'uuid of the blsubsample',
  `imgFileName` varchar(255) DEFAULT NULL COMMENT 'image filename',
  `imgFilePath` varchar(1024) DEFAULT NULL COMMENT 'url image',
  `comments` varchar(1024) DEFAULT NULL COMMENT 'comments',
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`blSubSampleId`),
  KEY `BLSubSample_FKIndex1` (`blSampleId`),
  KEY `BLSubSample_FKIndex2` (`diffractionPlanId`),
  KEY `BLSubSample_FKIndex3` (`positionId`),
  KEY `BLSubSample_FKIndex5` (`position2Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `Buffer`
--

CREATE TABLE IF NOT EXISTS `Buffer` (
  `bufferId` int(10) NOT NULL AUTO_INCREMENT,
  `proposalId` int(10) NOT NULL DEFAULT '-1',
  `safetyLevelId` int(10) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `acronym` varchar(45) DEFAULT NULL,
  `pH` varchar(45) DEFAULT NULL,
  `composition` varchar(45) DEFAULT NULL,
  `comments` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`bufferId`),
  KEY `BufferToSafetyLevel` (`safetyLevelId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5658 ;

-- --------------------------------------------------------

--
-- Table structure for table `BufferHasAdditive`
--

CREATE TABLE IF NOT EXISTS `BufferHasAdditive` (
  `bufferHasAdditiveId` int(10) NOT NULL AUTO_INCREMENT,
  `bufferId` int(10) NOT NULL,
  `additiveId` int(10) NOT NULL,
  `measurementUnitId` int(10) DEFAULT NULL,
  `quantity` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`bufferHasAdditiveId`),
  KEY `BufferHasAdditiveToBuffer` (`bufferId`),
  KEY `BufferHasAdditiveToAdditive` (`additiveId`),
  KEY `BufferHasAdditiveToUnit` (`measurementUnitId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `Container`
--

CREATE TABLE IF NOT EXISTS `Container` (
  `containerId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `dewarId` int(10) unsigned DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL,
  `containerType` varchar(20) DEFAULT NULL,
  `capacity` int(10) unsigned DEFAULT NULL,
  `beamlineLocation` varchar(20) DEFAULT NULL,
  `sampleChangerLocation` varchar(20) DEFAULT NULL,
  `containerStatus` varchar(45) DEFAULT NULL,
  `bltimeStamp` datetime DEFAULT NULL,
  `barcode` varchar(45) DEFAULT NULL,
  `sessionId` int(10) unsigned DEFAULT NULL,
  `ownerId` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`containerId`),
  UNIQUE KEY `Container_UNIndex1` (`barcode`),
  KEY `Container_FKIndex1` (`dewarId`),
  KEY `Container_FKIndex` (`beamlineLocation`),
  KEY `Container_FKIndexStatus` (`containerStatus`),
  KEY `Container_ibfk6` (`sessionId`),
  KEY `Container_ibfk5` (`ownerId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=336871 ;

-- --------------------------------------------------------

--
-- Table structure for table `Crystal`
--

CREATE TABLE IF NOT EXISTS `Crystal` (
  `crystalId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `diffractionPlanId` int(10) unsigned DEFAULT NULL,
  `proteinId` int(10) unsigned NOT NULL DEFAULT '0',
  `crystalUUID` varchar(45) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `spaceGroup` varchar(20) DEFAULT NULL,
  `morphology` varchar(255) DEFAULT NULL,
  `color` varchar(45) DEFAULT NULL,
  `size_X` double DEFAULT NULL,
  `size_Y` double DEFAULT NULL,
  `size_Z` double DEFAULT NULL,
  `cell_a` double DEFAULT NULL,
  `cell_b` double DEFAULT NULL,
  `cell_c` double DEFAULT NULL,
  `cell_alpha` double DEFAULT NULL,
  `cell_beta` double DEFAULT NULL,
  `cell_gamma` double DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `pdbFileName` varchar(255) DEFAULT NULL COMMENT 'pdb file name',
  `pdbFilePath` varchar(1024) DEFAULT NULL COMMENT 'pdb file path',
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`crystalId`),
  KEY `Crystal_FKIndex1` (`proteinId`),
  KEY `Crystal_FKIndex2` (`diffractionPlanId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=536508 ;

-- --------------------------------------------------------

--
-- Table structure for table `Crystal_has_UUID`
--

CREATE TABLE IF NOT EXISTS `Crystal_has_UUID` (
  `crystal_has_UUID_Id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `crystalId` int(10) unsigned NOT NULL,
  `UUID` varchar(45) DEFAULT NULL,
  `imageURL` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`crystal_has_UUID_Id`),
  KEY `Crystal_has_UUID_FKIndex1` (`crystalId`),
  KEY `Crystal_has_UUID_FKIndex2` (`UUID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=93158 ;

-- --------------------------------------------------------

--
-- Table structure for table `DataAcquisition`
--

CREATE TABLE IF NOT EXISTS `DataAcquisition` (
  `dataAcquisitionId` int(10) NOT NULL AUTO_INCREMENT,
  `sampleCellId` int(10) NOT NULL,
  `framesCount` varchar(45) DEFAULT NULL,
  `energy` varchar(45) DEFAULT NULL,
  `waitTime` varchar(45) DEFAULT NULL,
  `detectorDistance` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`dataAcquisitionId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `DataCollection`
--

CREATE TABLE IF NOT EXISTS `DataCollection` (
  `dataCollectionId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `dataCollectionGroupId` int(11) NOT NULL COMMENT 'references DataCollectionGroup table',
  `strategySubWedgeOrigId` int(10) unsigned DEFAULT NULL COMMENT 'references ScreeningStrategySubWedge table',
  `detectorId` int(11) DEFAULT NULL COMMENT 'references Detector table',
  `blSubSampleId` int(11) unsigned DEFAULT NULL,
  `startPositionId` int(11) unsigned DEFAULT NULL,
  `endPositionId` int(11) unsigned DEFAULT NULL,
  `dataCollectionNumber` int(10) unsigned DEFAULT NULL,
  `startTime` datetime DEFAULT NULL COMMENT 'Start time of the dataCollection',
  `endTime` datetime DEFAULT NULL COMMENT 'end time of the dataCollection',
  `runStatus` varchar(45) DEFAULT NULL,
  `axisStart` float DEFAULT NULL,
  `axisEnd` float DEFAULT NULL,
  `axisRange` float DEFAULT NULL,
  `overlap` float DEFAULT NULL,
  `numberOfImages` int(10) unsigned DEFAULT NULL,
  `startImageNumber` int(10) unsigned DEFAULT NULL,
  `numberOfPasses` int(10) unsigned DEFAULT NULL,
  `exposureTime` float DEFAULT NULL,
  `imageDirectory` varchar(255) DEFAULT NULL,
  `imagePrefix` varchar(100) DEFAULT NULL,
  `imageSuffix` varchar(45) DEFAULT NULL,
  `fileTemplate` varchar(255) DEFAULT NULL,
  `wavelength` float DEFAULT NULL,
  `resolution` float DEFAULT NULL,
  `detectorDistance` float DEFAULT NULL,
  `xBeam` float DEFAULT NULL,
  `yBeam` float DEFAULT NULL,
  `xBeamPix` float DEFAULT NULL COMMENT 'Beam size in pixels',
  `yBeamPix` float DEFAULT NULL COMMENT 'Beam size in pixels',
  `comments` varchar(1024) DEFAULT NULL,
  `printableForReport` tinyint(1) unsigned DEFAULT '1',
  `slitGapVertical` float DEFAULT NULL,
  `slitGapHorizontal` float DEFAULT NULL,
  `transmission` float DEFAULT NULL,
  `synchrotronMode` varchar(20) DEFAULT NULL,
  `xtalSnapshotFullPath1` varchar(255) DEFAULT NULL,
  `xtalSnapshotFullPath2` varchar(255) DEFAULT NULL,
  `xtalSnapshotFullPath3` varchar(255) DEFAULT NULL,
  `xtalSnapshotFullPath4` varchar(255) DEFAULT NULL,
  `rotationAxis` enum('Omega','Kappa','Phi') DEFAULT NULL,
  `phiStart` float DEFAULT NULL,
  `kappaStart` float DEFAULT NULL,
  `omegaStart` float DEFAULT NULL,
  `resolutionAtCorner` float DEFAULT NULL,
  `detector2Theta` float DEFAULT NULL,
  `undulatorGap1` float DEFAULT NULL,
  `undulatorGap2` float DEFAULT NULL,
  `undulatorGap3` float DEFAULT NULL,
  `beamSizeAtSampleX` float DEFAULT NULL,
  `beamSizeAtSampleY` float DEFAULT NULL,
  `centeringMethod` varchar(255) DEFAULT NULL,
  `averageTemperature` float DEFAULT NULL,
  `actualCenteringPosition` varchar(255) DEFAULT NULL,
  `beamShape` varchar(45) DEFAULT NULL,
  `flux` double DEFAULT NULL,
  `flux_end` double DEFAULT NULL COMMENT 'flux measured after the collect',
  `totalAbsorbedDose` double DEFAULT NULL COMMENT 'expected dose delivered to the crystal, EDNA',
  `bestWilsonPlotPath` varchar(255) DEFAULT NULL,
  `imageQualityIndicatorsPlotPath` varchar(512) DEFAULT NULL,
  `imageQualityIndicatorsCSVPath` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`dataCollectionId`),
  KEY `DataCollection_FKIndex1` (`dataCollectionGroupId`),
  KEY `DataCollection_FKIndex2` (`strategySubWedgeOrigId`),
  KEY `DataCollection_FKIndex3` (`detectorId`),
  KEY `DataCollection_FKIndexStartTime` (`startTime`),
  KEY `DataCollection_FKIndexImageDirectory` (`imageDirectory`),
  KEY `DataCollection_FKIndexDCNumber` (`dataCollectionNumber`),
  KEY `DataCollection_FKIndexImagePrefix` (`imagePrefix`),
  KEY `startPositionId` (`startPositionId`),
  KEY `endPositionId` (`endPositionId`),
  KEY `blSubSampleId` (`blSubSampleId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1914439 ;

-- --------------------------------------------------------

--
-- Table structure for table `DataCollectionGroup`
--

CREATE TABLE IF NOT EXISTS `DataCollectionGroup` (
  `dataCollectionGroupId` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `blSampleId` int(10) unsigned DEFAULT NULL COMMENT 'references BLSample table',
  `sessionId` int(10) unsigned NOT NULL COMMENT 'references Session table',
  `workflowId` int(11) unsigned DEFAULT NULL,
  `experimentType` enum('SAD','SAD - Inverse Beam','OSC','Collect - Multiwedge','MAD','Helical','Multi-positional','Mesh','Burn','MAD - Inverse Beam','Characterization','Dehydration') DEFAULT NULL COMMENT 'Experiment type flag',
  `startTime` datetime DEFAULT NULL COMMENT 'Start time of the dataCollectionGroup',
  `endTime` datetime DEFAULT NULL COMMENT 'end time of the dataCollectionGroup',
  `crystalClass` varchar(20) DEFAULT NULL COMMENT 'Crystal Class for industrials users',
  `comments` varchar(1024) DEFAULT NULL COMMENT 'comments',
  `detectorMode` varchar(255) DEFAULT NULL COMMENT 'Detector mode',
  `actualSampleBarcode` varchar(45) DEFAULT NULL COMMENT 'Actual sample barcode',
  `actualSampleSlotInContainer` int(10) unsigned DEFAULT NULL COMMENT 'Actual sample slot number in container',
  `actualContainerBarcode` varchar(45) DEFAULT NULL COMMENT 'Actual container barcode',
  `actualContainerSlotInSC` int(10) unsigned DEFAULT NULL COMMENT 'Actual container slot number in sample changer',
  `xtalSnapshotFullPath` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`dataCollectionGroupId`),
  KEY `DataCollectionGroup_FKIndex1` (`blSampleId`),
  KEY `DataCollectionGroup_FKIndex2` (`sessionId`),
  KEY `workflowId` (`workflowId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='a dataCollectionGroup is a group of dataCollection for a spe' AUTO_INCREMENT=1586254 ;

-- --------------------------------------------------------

--
-- Table structure for table `DatamatrixInSampleChanger`
--

CREATE TABLE IF NOT EXISTS `DatamatrixInSampleChanger` (
  `datamatrixInSampleChangerId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `proposalId` int(10) unsigned NOT NULL DEFAULT '0',
  `beamLineName` varchar(45) DEFAULT NULL,
  `datamatrixCode` varchar(45) DEFAULT NULL,
  `locationInContainer` int(11) DEFAULT NULL,
  `containerLocationInSC` int(11) DEFAULT NULL,
  `containerDatamatrixCode` varchar(45) DEFAULT NULL,
  `bltimeStamp` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`datamatrixInSampleChangerId`),
  KEY `DatamatrixInSampleChanger_FKIndex1` (`proposalId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=19089089 ;

-- --------------------------------------------------------

--
-- Table structure for table `Detector`
--

CREATE TABLE IF NOT EXISTS `Detector` (
  `detectorId` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `detectorType` varchar(255) DEFAULT NULL,
  `detectorManufacturer` varchar(255) DEFAULT NULL,
  `detectorModel` varchar(255) DEFAULT NULL,
  `detectorPixelSizeHorizontal` float DEFAULT NULL,
  `detectorPixelSizeVertical` float DEFAULT NULL,
  `detectorSerialNumber` float DEFAULT NULL,
  `detectorDistanceMin` double DEFAULT NULL,
  `detectorDistanceMax` double DEFAULT NULL,
  `trustedPixelValueRangeLower` double DEFAULT NULL,
  `trustedPixelValueRangeUpper` double DEFAULT NULL,
  `sensorThickness` float DEFAULT NULL,
  `overload` float DEFAULT NULL,
  `XGeoCorr` varchar(255) DEFAULT NULL,
  `YGeoCorr` varchar(255) DEFAULT NULL,
  `detectorMode` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`detectorId`),
  KEY `Detector_FKIndex1` (`detectorType`,`detectorManufacturer`,`detectorModel`,`detectorPixelSizeHorizontal`,`detectorPixelSizeVertical`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='Detector table is linked to a dataCollection' AUTO_INCREMENT=81 ;

-- --------------------------------------------------------

--
-- Table structure for table `Dewar`
--

CREATE TABLE IF NOT EXISTS `Dewar` (
  `dewarId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `shippingId` int(10) unsigned DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL,
  `comments` tinytext,
  `storageLocation` varchar(45) DEFAULT NULL,
  `dewarStatus` varchar(45) DEFAULT NULL,
  `bltimeStamp` datetime DEFAULT NULL,
  `isStorageDewar` tinyint(1) DEFAULT '0',
  `barCode` varchar(45) DEFAULT NULL,
  `firstExperimentId` int(10) unsigned DEFAULT NULL,
  `customsValue` int(11) unsigned DEFAULT NULL,
  `transportValue` int(11) unsigned DEFAULT NULL,
  `trackingNumberToSynchrotron` varchar(30) DEFAULT NULL,
  `trackingNumberFromSynchrotron` varchar(30) DEFAULT NULL,
  `facilityCode` varchar(20) DEFAULT NULL COMMENT 'Unique barcode assigned to each dewar',
  `type` enum('Dewar','Toolbox') NOT NULL DEFAULT 'Dewar',
  PRIMARY KEY (`dewarId`),
  UNIQUE KEY `barCode` (`barCode`),
  KEY `Dewar_FKIndex1` (`shippingId`),
  KEY `Dewar_FKIndex2` (`firstExperimentId`),
  KEY `Dewar_FKIndexStatus` (`dewarStatus`),
  KEY `Dewar_FKIndexCode` (`code`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=311309 ;

-- --------------------------------------------------------

--
-- Table structure for table `DewarLocation`
--

CREATE TABLE IF NOT EXISTS `DewarLocation` (
  `eventId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `dewarNumber` varchar(128) NOT NULL COMMENT 'Dewar number',
  `userId` varchar(128) DEFAULT NULL COMMENT 'User who locates the dewar',
  `dateTime` datetime DEFAULT NULL COMMENT 'Date and time of locatization',
  `locationName` varchar(128) DEFAULT NULL COMMENT 'Location of the dewar',
  `courierName` varchar(128) DEFAULT NULL COMMENT 'Carrier name who''s shipping back the dewar',
  `courierTrackingNumber` varchar(128) DEFAULT NULL COMMENT 'Tracking number of the shippment',
  PRIMARY KEY (`eventId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='ISPyB Dewar location table' AUTO_INCREMENT=13254 ;

-- --------------------------------------------------------

--
-- Table structure for table `DewarLocationList`
--

CREATE TABLE IF NOT EXISTS `DewarLocationList` (
  `locationId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `locationName` varchar(128) NOT NULL DEFAULT '' COMMENT 'Location',
  PRIMARY KEY (`locationId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='List of locations for dewars' AUTO_INCREMENT=11 ;

-- --------------------------------------------------------

--
-- Table structure for table `DewarTransportHistory`
--

CREATE TABLE IF NOT EXISTS `DewarTransportHistory` (
  `DewarTransportHistoryId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `dewarId` int(10) unsigned DEFAULT NULL,
  `dewarStatus` varchar(45) NOT NULL,
  `storageLocation` varchar(45) NOT NULL,
  `arrivalDate` datetime NOT NULL,
  PRIMARY KEY (`DewarTransportHistoryId`),
  KEY `DewarTransportHistory_FKIndex1` (`dewarId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=34371 ;

-- --------------------------------------------------------

--
-- Table structure for table `DiffractionPlan`
--

CREATE TABLE IF NOT EXISTS `DiffractionPlan` (
  `diffractionPlanId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `xmlDocumentId` int(10) unsigned DEFAULT NULL,
  `experimentKind` enum('Default','MXPressE','MXPressO','MXPressI','MXPressE_SAD','MXScore','MXPressM','MAD','SAD','Fixed','Ligand binding','Refinement','OSC','MAD - Inverse Beam','SAD - Inverse Beam') DEFAULT NULL,
  `observedResolution` float DEFAULT NULL,
  `minimalResolution` float DEFAULT NULL,
  `exposureTime` float DEFAULT NULL,
  `oscillationRange` float DEFAULT NULL,
  `maximalResolution` float DEFAULT NULL,
  `screeningResolution` float DEFAULT NULL,
  `radiationSensitivity` float DEFAULT NULL,
  `anomalousScatterer` varchar(255) DEFAULT NULL,
  `preferredBeamSizeX` float DEFAULT NULL,
  `preferredBeamSizeY` float DEFAULT NULL,
  `preferredBeamDiameter` float DEFAULT NULL,
  `comments` varchar(1024) DEFAULT NULL,
  `aimedCompleteness` double DEFAULT NULL,
  `aimedIOverSigmaAtHighestRes` double DEFAULT NULL,
  `aimedMultiplicity` double DEFAULT NULL,
  `aimedResolution` double DEFAULT NULL,
  `anomalousData` tinyint(1) DEFAULT '0',
  `complexity` varchar(45) DEFAULT NULL,
  `estimateRadiationDamage` tinyint(1) DEFAULT '0',
  `forcedSpaceGroup` varchar(45) DEFAULT NULL,
  `requiredCompleteness` double DEFAULT NULL,
  `requiredMultiplicity` double DEFAULT NULL,
  `requiredResolution` double DEFAULT NULL,
  `strategyOption` varchar(45) DEFAULT NULL,
  `kappaStrategyOption` varchar(45) DEFAULT NULL,
  `numberOfPositions` int(11) DEFAULT NULL,
  `minDimAccrossSpindleAxis` double DEFAULT NULL COMMENT 'minimum dimension accross the spindle axis',
  `maxDimAccrossSpindleAxis` double DEFAULT NULL COMMENT 'maximum dimension accross the spindle axis',
  `radiationSensitivityBeta` double DEFAULT NULL,
  `radiationSensitivityGamma` double DEFAULT NULL,
  `minOscWidth` float DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`diffractionPlanId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=603770 ;

-- --------------------------------------------------------

--
-- Table structure for table `EnergyScan`
--

CREATE TABLE IF NOT EXISTS `EnergyScan` (
  `energyScanId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sessionId` int(10) unsigned NOT NULL,
  `blSampleId` int(10) unsigned DEFAULT NULL,
  `fluorescenceDetector` varchar(255) DEFAULT NULL,
  `scanFileFullPath` varchar(255) DEFAULT NULL,
  `choochFileFullPath` varchar(255) DEFAULT NULL,
  `jpegChoochFileFullPath` varchar(255) DEFAULT NULL,
  `element` varchar(45) DEFAULT NULL,
  `startEnergy` float DEFAULT NULL,
  `endEnergy` float DEFAULT NULL,
  `transmissionFactor` float DEFAULT NULL,
  `exposureTime` float DEFAULT NULL,
  `synchrotronCurrent` float DEFAULT NULL,
  `temperature` float DEFAULT NULL,
  `peakEnergy` float DEFAULT NULL,
  `peakFPrime` float DEFAULT NULL,
  `peakFDoublePrime` float DEFAULT NULL,
  `inflectionEnergy` float DEFAULT NULL,
  `inflectionFPrime` float DEFAULT NULL,
  `inflectionFDoublePrime` float DEFAULT NULL,
  `xrayDose` float DEFAULT NULL,
  `startTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `edgeEnergy` varchar(255) DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `beamSizeVertical` float DEFAULT NULL,
  `beamSizeHorizontal` float DEFAULT NULL,
  `crystalClass` varchar(20) DEFAULT NULL,
  `comments` varchar(1024) DEFAULT NULL,
  `flux` double DEFAULT NULL COMMENT 'flux measured before the energyScan',
  `flux_end` double DEFAULT NULL COMMENT 'flux measured after the energyScan',
  `workingDirectory` varchar(45) DEFAULT NULL,
  `blSubSampleId` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`energyScanId`),
  KEY `EnergyScan_FKIndex2` (`sessionId`),
  KEY `ES_ibfk_2` (`blSampleId`),
  KEY `ES_ibfk_3` (`blSubSampleId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=13853 ;

-- --------------------------------------------------------

--
-- Table structure for table `Experiment`
--

CREATE TABLE IF NOT EXISTS `Experiment` (
  `experimentId` int(11) NOT NULL AUTO_INCREMENT,
  `sessionId` int(10) unsigned DEFAULT NULL,
  `proposalId` int(10) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `experimentType` varchar(128) DEFAULT NULL,
  `sourceFilePath` varchar(256) DEFAULT NULL,
  `dataAcquisitionFilePath` varchar(256) DEFAULT NULL COMMENT 'The file path pointing to the data acquisition. Eventually it may be a compressed file with all the files or just the folder',
  `status` varchar(45) DEFAULT NULL,
  `comments` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`experimentId`),
  KEY `fk_Experiment_To_session_idx` (`sessionId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=13750 ;

-- --------------------------------------------------------

--
-- Table structure for table `ExperimentKindDetails`
--

CREATE TABLE IF NOT EXISTS `ExperimentKindDetails` (
  `experimentKindId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `diffractionPlanId` int(10) unsigned NOT NULL,
  `exposureIndex` int(10) unsigned DEFAULT NULL,
  `dataCollectionType` varchar(45) DEFAULT NULL,
  `dataCollectionKind` varchar(45) DEFAULT NULL,
  `wedgeValue` float DEFAULT NULL,
  PRIMARY KEY (`experimentKindId`),
  KEY `ExperimentKindDetails_FKIndex1` (`diffractionPlanId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=44 ;

-- --------------------------------------------------------

--
-- Table structure for table `FitStructureToExperimentalData`
--

CREATE TABLE IF NOT EXISTS `FitStructureToExperimentalData` (
  `fitStructureToExperimentalDataId` int(11) NOT NULL AUTO_INCREMENT,
  `structureId` int(10) DEFAULT NULL,
  `subtractionId` int(10) DEFAULT NULL,
  `workflowId` int(10) unsigned DEFAULT NULL,
  `fitFilePath` varchar(255) DEFAULT NULL,
  `logFilePath` varchar(255) DEFAULT NULL,
  `outputFilePath` varchar(255) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `comments` varchar(2048) DEFAULT NULL,
  PRIMARY KEY (`fitStructureToExperimentalDataId`),
  KEY `fk_FitStructureToExperimentalData_1` (`structureId`),
  KEY `fk_FitStructureToExperimentalData_2` (`workflowId`),
  KEY `fk_FitStructureToExperimentalData_3` (`subtractionId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

-- --------------------------------------------------------

--
-- Table structure for table `Frame`
--

CREATE TABLE IF NOT EXISTS `Frame` (
  `frameId` int(10) NOT NULL AUTO_INCREMENT,
  `filePath` varchar(255) DEFAULT NULL,
  `comments` varchar(45) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  PRIMARY KEY (`frameId`),
  KEY `FILE` (`filePath`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1506979 ;

-- --------------------------------------------------------

--
-- Table structure for table `FrameList`
--

CREATE TABLE IF NOT EXISTS `FrameList` (
  `frameListId` int(10) NOT NULL AUTO_INCREMENT,
  `comments` int(10) DEFAULT NULL,
  PRIMARY KEY (`frameListId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=167752 ;

-- --------------------------------------------------------

--
-- Table structure for table `FrameSet`
--

CREATE TABLE IF NOT EXISTS `FrameSet` (
  `frameSetId` int(10) NOT NULL AUTO_INCREMENT,
  `runId` int(10) NOT NULL,
  `frameListId` int(10) DEFAULT NULL,
  `detectorId` int(10) DEFAULT NULL,
  `detectorDistance` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`frameSetId`),
  KEY `FramesetToRun` (`runId`),
  KEY `FrameSetToFrameList` (`frameListId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4209 ;

-- --------------------------------------------------------

--
-- Table structure for table `FrameToList`
--

CREATE TABLE IF NOT EXISTS `FrameToList` (
  `frameToListId` int(10) NOT NULL AUTO_INCREMENT,
  `frameListId` int(10) NOT NULL,
  `frameId` int(10) NOT NULL,
  PRIMARY KEY (`frameToListId`),
  KEY `FrameToLisToFrameList` (`frameListId`),
  KEY `FrameToListToFrame` (`frameId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2163547 ;

-- --------------------------------------------------------

--
-- Table structure for table `GeometryClassname`
--

CREATE TABLE IF NOT EXISTS `GeometryClassname` (
  `geometryClassnameId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `geometryClassname` varchar(45) DEFAULT NULL,
  `geometryOrder` int(2) NOT NULL,
  PRIMARY KEY (`geometryClassnameId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=17 ;

-- --------------------------------------------------------

--
-- Table structure for table `GridInfo`
--

CREATE TABLE IF NOT EXISTS `GridInfo` (
  `gridInfoId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `workflowMeshId` int(11) unsigned DEFAULT NULL,
  `xOffset` double DEFAULT NULL,
  `yOffset` double DEFAULT NULL,
  `dx_mm` double DEFAULT NULL,
  `dy_mm` double DEFAULT NULL,
  `steps_x` double DEFAULT NULL,
  `steps_y` double DEFAULT NULL,
  `meshAngle` double DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  `orientation` enum('vertical','horizontal') DEFAULT 'horizontal',
  `dataCollectionGroupId` int(11) DEFAULT NULL,
  `pixelspermicronX` float DEFAULT NULL,
  `pixelspermicronY` float DEFAULT NULL,
  `snapshot_offsetxpixel` float DEFAULT NULL,
  `snapshot_offsetypixel` float DEFAULT NULL,
  PRIMARY KEY (`gridInfoId`),
  KEY `workflowMeshId` (`workflowMeshId`),
  KEY `GridInfo_ibfk_2` (`dataCollectionGroupId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=95488 ;

-- --------------------------------------------------------

--
-- Table structure for table `Image`
--

CREATE TABLE IF NOT EXISTS `Image` (
  `imageId` int(12) unsigned NOT NULL AUTO_INCREMENT,
  `dataCollectionId` int(11) unsigned NOT NULL DEFAULT '0',
  `motorPositionId` int(11) unsigned DEFAULT NULL,
  `imageNumber` int(10) unsigned DEFAULT NULL,
  `fileName` varchar(255) DEFAULT NULL,
  `fileLocation` varchar(255) DEFAULT NULL,
  `measuredIntensity` float DEFAULT NULL,
  `jpegFileFullPath` varchar(255) DEFAULT NULL,
  `jpegThumbnailFileFullPath` varchar(255) DEFAULT NULL,
  `temperature` float DEFAULT NULL,
  `cumulativeIntensity` float DEFAULT NULL,
  `synchrotronCurrent` float DEFAULT NULL,
  `comments` varchar(1024) DEFAULT NULL,
  `machineMessage` varchar(1024) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`imageId`),
  KEY `Image_FKIndex1` (`dataCollectionId`),
  KEY `Image_FKIndex2` (`imageNumber`),
  KEY `Image_Index3` (`fileLocation`,`fileName`) USING BTREE,
  KEY `motorPositionId` (`motorPositionId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=50560878 ;

-- --------------------------------------------------------

--
-- Table structure for table `ImageQualityIndicators`
--

CREATE TABLE IF NOT EXISTS `ImageQualityIndicators` (
  `imageQualityIndicatorsId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `imageId` int(10) unsigned NOT NULL COMMENT 'Foreign key to the Image table',
  `autoProcProgramId` int(10) unsigned NOT NULL COMMENT 'Foreign key to the AutoProcProgram table',
  `spotTotal` int(10) DEFAULT NULL COMMENT 'Total number of spots',
  `inResTotal` int(10) DEFAULT NULL COMMENT 'Total number of spots in resolution range',
  `goodBraggCandidates` int(10) DEFAULT NULL COMMENT 'Total number of Bragg diffraction spots',
  `iceRings` int(10) DEFAULT NULL COMMENT 'Number of ice rings identified',
  `method1Res` float DEFAULT NULL COMMENT 'Resolution estimate 1 (see publication)',
  `method2Res` float DEFAULT NULL COMMENT 'Resolution estimate 2 (see publication)',
  `maxUnitCell` float DEFAULT NULL COMMENT 'Estimation of the largest possible unit cell edge',
  `pctSaturationTop50Peaks` float DEFAULT NULL COMMENT 'The fraction of the dynamic range being used',
  `inResolutionOvrlSpots` int(10) DEFAULT NULL COMMENT 'Number of spots overloaded',
  `binPopCutOffMethod2Res` float DEFAULT NULL COMMENT 'Cut off used in resolution limit calculation',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  `totalIntegratedSignal` double DEFAULT NULL,
  `dozor_score` double DEFAULT NULL COMMENT 'dozor_score',
  PRIMARY KEY (`imageQualityIndicatorsId`),
  KEY `ImageQualityIndicatorsIdx1` (`imageId`),
  KEY `AutoProcProgramIdx1` (`autoProcProgramId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2854180 ;

-- --------------------------------------------------------

--
-- Table structure for table `InputParameterWorkflow`
--

CREATE TABLE IF NOT EXISTS `InputParameterWorkflow` (
  `inputParameterId` int(10) NOT NULL AUTO_INCREMENT,
  `workflowId` int(10) NOT NULL,
  `parameterType` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `comments` varchar(2048) DEFAULT NULL,
  PRIMARY KEY (`inputParameterId`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

-- --------------------------------------------------------

--
-- Table structure for table `Instruction`
--

CREATE TABLE IF NOT EXISTS `Instruction` (
  `instructionId` int(10) NOT NULL AUTO_INCREMENT,
  `instructionSetId` int(10) NOT NULL,
  `order` int(11) NOT NULL,
  `comments` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`instructionId`),
  KEY `InstructionToInstructionSet` (`instructionSetId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `InstructionSet`
--

CREATE TABLE IF NOT EXISTS `InstructionSet` (
  `instructionSetId` int(10) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`instructionSetId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `IspybAutoProcAttachment`
--

CREATE TABLE IF NOT EXISTS `IspybAutoProcAttachment` (
  `autoProcAttachmentId` int(11) NOT NULL AUTO_INCREMENT,
  `fileName` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `step` enum('XDS','XSCALE','SCALA','SCALEPACK','TRUNCATE','DIMPLE') DEFAULT 'XDS' COMMENT 'step where the file is generated',
  `fileCategory` enum('input','output','log','correction') DEFAULT 'output',
  `hasGraph` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`autoProcAttachmentId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='ISPyB autoProcAttachment files values' AUTO_INCREMENT=60 ;

-- --------------------------------------------------------

--
-- Table structure for table `IspybCrystalClass`
--

CREATE TABLE IF NOT EXISTS `IspybCrystalClass` (
  `crystalClassId` int(11) NOT NULL AUTO_INCREMENT,
  `crystalClass_code` varchar(20) NOT NULL,
  `crystalClass_name` varchar(255) NOT NULL,
  PRIMARY KEY (`crystalClassId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='ISPyB crystal class values' AUTO_INCREMENT=11 ;

-- --------------------------------------------------------

--
-- Table structure for table `IspybReference`
--

CREATE TABLE IF NOT EXISTS `IspybReference` (
  `referenceId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `referenceName` varchar(255) DEFAULT NULL COMMENT 'reference name',
  `referenceUrl` varchar(1024) DEFAULT NULL COMMENT 'url of the reference',
  `referenceBibtext` blob COMMENT 'bibtext value of the reference',
  `beamline` enum('All','ID14-4','ID23-1','ID23-2','ID29','ID30A-1','ID30A-2','XRF','AllXRF','Mesh') DEFAULT NULL COMMENT 'beamline involved',
  PRIMARY KEY (`referenceId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=15 ;

-- --------------------------------------------------------

--
-- Table structure for table `LabContact`
--

CREATE TABLE IF NOT EXISTS `LabContact` (
  `labContactId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `personId` int(10) unsigned NOT NULL,
  `cardName` varchar(40) NOT NULL,
  `proposalId` int(10) unsigned NOT NULL,
  `defaultCourrierCompany` varchar(45) DEFAULT NULL,
  `courierAccount` varchar(45) DEFAULT NULL,
  `billingReference` varchar(45) DEFAULT NULL,
  `dewarAvgCustomsValue` int(10) unsigned NOT NULL DEFAULT '0',
  `dewarAvgTransportValue` int(10) unsigned NOT NULL DEFAULT '0',
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`labContactId`),
  UNIQUE KEY `personAndProposal` (`personId`,`proposalId`),
  UNIQUE KEY `cardNameAndProposal` (`cardName`,`proposalId`),
  KEY `LabContact_FKIndex1` (`proposalId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=247306 ;

-- --------------------------------------------------------

--
-- Table structure for table `Laboratory`
--

CREATE TABLE IF NOT EXISTS `Laboratory` (
  `laboratoryId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `laboratoryUUID` varchar(45) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `organization` varchar(45) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`laboratoryId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=311139 ;

-- --------------------------------------------------------

--
-- Table structure for table `Log4Stat`
--

CREATE TABLE IF NOT EXISTS `Log4Stat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `priority` varchar(15) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `msg` varchar(255) DEFAULT NULL,
  `detail` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=19789 ;

-- --------------------------------------------------------

--
-- Table structure for table `Login`
--

CREATE TABLE IF NOT EXISTS `Login` (
  `loginId` int(11) NOT NULL AUTO_INCREMENT,
  `token` varchar(45) NOT NULL,
  `username` varchar(45) NOT NULL,
  `roles` varchar(1024) NOT NULL,
  `expirationTime` datetime NOT NULL,
  PRIMARY KEY (`loginId`),
  KEY `Token` (`token`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2952 ;

-- --------------------------------------------------------

--
-- Table structure for table `Macromolecule`
--

CREATE TABLE IF NOT EXISTS `Macromolecule` (
  `macromoleculeId` int(11) NOT NULL AUTO_INCREMENT,
  `proposalId` int(10) unsigned DEFAULT NULL,
  `safetyLevelId` int(10) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `acronym` varchar(45) DEFAULT NULL,
  `extintionCoefficient` varchar(45) DEFAULT NULL,
  `molecularMass` varchar(45) DEFAULT NULL,
  `sequence` varchar(1000) DEFAULT NULL,
  `contactsDescriptionFilePath` varchar(255) DEFAULT NULL,
  `symmetry` varchar(45) DEFAULT NULL,
  `comments` varchar(1024) DEFAULT NULL,
  `refractiveIndex` varchar(45) DEFAULT NULL,
  `solventViscosity` varchar(45) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  PRIMARY KEY (`macromoleculeId`),
  KEY `MacromoleculeToSafetyLevel` (`safetyLevelId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=46251 ;

-- --------------------------------------------------------

--
-- Table structure for table `MacromoleculeRegion`
--

CREATE TABLE IF NOT EXISTS `MacromoleculeRegion` (
  `macromoleculeRegionId` int(10) NOT NULL AUTO_INCREMENT,
  `macromoleculeId` int(10) NOT NULL,
  `regionType` varchar(45) DEFAULT NULL,
  `id` varchar(45) DEFAULT NULL,
  `count` varchar(45) DEFAULT NULL,
  `sequence` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`macromoleculeRegionId`),
  KEY `MacromoleculeRegionInformationToMacromolecule` (`macromoleculeId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `Measurement`
--

CREATE TABLE IF NOT EXISTS `Measurement` (
  `measurementId` int(10) NOT NULL AUTO_INCREMENT,
  `specimenId` int(10) NOT NULL,
  `runId` int(10) DEFAULT NULL,
  `code` varchar(100) DEFAULT NULL,
  `priorityLevelId` int(10) DEFAULT NULL,
  `exposureTemperature` varchar(45) DEFAULT NULL,
  `viscosity` varchar(45) DEFAULT NULL,
  `flow` tinyint(1) DEFAULT NULL,
  `extraFlowTime` varchar(45) DEFAULT NULL,
  `volumeToLoad` varchar(45) DEFAULT NULL,
  `waitTime` varchar(45) DEFAULT NULL,
  `transmission` varchar(45) DEFAULT NULL,
  `comments` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`measurementId`),
  KEY `SpecimenToSamplePlateWell` (`specimenId`),
  KEY `MeasurementToRun` (`runId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=224638 ;

-- --------------------------------------------------------

--
-- Table structure for table `MeasurementToDataCollection`
--

CREATE TABLE IF NOT EXISTS `MeasurementToDataCollection` (
  `measurementToDataCollectionId` int(10) NOT NULL AUTO_INCREMENT,
  `dataCollectionId` int(10) DEFAULT NULL,
  `measurementId` int(10) DEFAULT NULL,
  `dataCollectionOrder` int(10) DEFAULT NULL,
  PRIMARY KEY (`measurementToDataCollectionId`),
  KEY `MeasurementToDataCollectionToDataCollection` (`dataCollectionId`),
  KEY `MeasurementToDataCollectionToMeasurement` (`measurementId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=224638 ;

-- --------------------------------------------------------

--
-- Table structure for table `MeasurementUnit`
--

CREATE TABLE IF NOT EXISTS `MeasurementUnit` (
  `measurementUnitId` int(10) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `unitType` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`measurementUnitId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Merge`
--

CREATE TABLE IF NOT EXISTS `Merge` (
  `mergeId` int(10) NOT NULL AUTO_INCREMENT,
  `measurementId` int(10) DEFAULT NULL,
  `frameListId` int(10) DEFAULT NULL,
  `discardedFrameNameList` varchar(1024) DEFAULT NULL,
  `averageFilePath` varchar(255) DEFAULT NULL,
  `framesCount` varchar(45) DEFAULT NULL,
  `framesMerge` varchar(45) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  PRIMARY KEY (`mergeId`),
  KEY `MergeToMeasurement` (`measurementId`),
  KEY `MergeToListOfFrames` (`frameListId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=116949 ;

-- --------------------------------------------------------

--
-- Table structure for table `MixtureToStructure`
--

CREATE TABLE IF NOT EXISTS `MixtureToStructure` (
  `fitToStructureId` int(11) NOT NULL AUTO_INCREMENT,
  `structureId` int(10) NOT NULL,
  `mixtureId` int(10) NOT NULL,
  `volumeFraction` varchar(45) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  PRIMARY KEY (`fitToStructureId`),
  KEY `fk_FitToStructure_1` (`structureId`),
  KEY `fk_FitToStructure_2` (`mixtureId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `Model`
--

CREATE TABLE IF NOT EXISTS `Model` (
  `modelId` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `pdbFile` varchar(255) DEFAULT NULL,
  `fitFile` varchar(255) DEFAULT NULL,
  `firFile` varchar(255) DEFAULT NULL,
  `logFile` varchar(255) DEFAULT NULL,
  `rFactor` varchar(45) DEFAULT NULL,
  `chiSqrt` varchar(45) DEFAULT NULL,
  `volume` varchar(45) DEFAULT NULL,
  `rg` varchar(45) DEFAULT NULL,
  `dMax` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`modelId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=562046 ;

-- --------------------------------------------------------

--
-- Table structure for table `ModelBuilding`
--

CREATE TABLE IF NOT EXISTS `ModelBuilding` (
  `modelBuildingId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `phasingAnalysisId` int(11) unsigned NOT NULL COMMENT 'Related phasing analysis item',
  `phasingProgramRunId` int(11) unsigned NOT NULL COMMENT 'Related program item',
  `spaceGroupId` int(10) unsigned DEFAULT NULL COMMENT 'Related spaceGroup',
  `lowRes` double DEFAULT NULL,
  `highRes` double DEFAULT NULL,
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`modelBuildingId`),
  KEY `ModelBuilding_FKIndex1` (`phasingAnalysisId`),
  KEY `ModelBuilding_FKIndex2` (`phasingProgramRunId`),
  KEY `ModelBuilding_FKIndex3` (`spaceGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `ModelList`
--

CREATE TABLE IF NOT EXISTS `ModelList` (
  `modelListId` int(10) NOT NULL AUTO_INCREMENT,
  `nsdFilePath` varchar(255) DEFAULT NULL,
  `chi2RgFilePath` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`modelListId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=34687 ;

-- --------------------------------------------------------

--
-- Table structure for table `ModelToList`
--

CREATE TABLE IF NOT EXISTS `ModelToList` (
  `modelToListId` int(10) NOT NULL AUTO_INCREMENT,
  `modelId` int(10) NOT NULL,
  `modelListId` int(10) NOT NULL,
  PRIMARY KEY (`modelToListId`),
  KEY `ModelToListToList` (`modelListId`),
  KEY `ModelToListToModel` (`modelId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=466313 ;

-- --------------------------------------------------------

--
-- Table structure for table `MotorPosition`
--

CREATE TABLE IF NOT EXISTS `MotorPosition` (
  `motorPositionId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `phiX` double DEFAULT NULL,
  `phiY` double DEFAULT NULL,
  `phiZ` double DEFAULT NULL,
  `sampX` double DEFAULT NULL,
  `sampY` double DEFAULT NULL,
  `omega` double DEFAULT NULL,
  `kappa` double DEFAULT NULL,
  `phi` double DEFAULT NULL,
  `chi` double DEFAULT NULL,
  `gridIndexY` int(11) DEFAULT NULL,
  `gridIndexZ` int(11) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`motorPositionId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4098632 ;

-- --------------------------------------------------------

--
-- Table structure for table `MXMRRun`
--

CREATE TABLE IF NOT EXISTS `MXMRRun` (
  `mxMRRunId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `autoProcScalingId` int(11) unsigned NOT NULL,
  `success` tinyint(1) DEFAULT '0' COMMENT 'Indicates whether the program completed. 1 for success, 0 for failure.',
  `message` varchar(255) DEFAULT NULL COMMENT 'A short summary of the findings, success or failure.',
  `pipeline` varchar(50) DEFAULT NULL,
  `inputCoordFile` varchar(255) DEFAULT NULL,
  `outputCoordFile` varchar(255) DEFAULT NULL,
  `inputMTZFile` varchar(255) DEFAULT NULL,
  `outputMTZFile` varchar(255) DEFAULT NULL,
  `runDirectory` varchar(255) DEFAULT NULL,
  `logFile` varchar(255) DEFAULT NULL,
  `commandLine` varchar(255) DEFAULT NULL,
  `rValueStart` float DEFAULT NULL,
  `rValueEnd` float DEFAULT NULL,
  `rFreeValueStart` float DEFAULT NULL,
  `rFreeValueEnd` float DEFAULT NULL,
  `starttime` datetime DEFAULT NULL,
  `endtime` datetime DEFAULT NULL,
  PRIMARY KEY (`mxMRRunId`),
  KEY `mxMRRun_FK1` (`autoProcScalingId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `MXMRRunBlob`
--

CREATE TABLE IF NOT EXISTS `MXMRRunBlob` (
  `mxMRRunBlobId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `mxMRRunId` int(11) unsigned NOT NULL,
  `view1` varchar(255) DEFAULT NULL,
  `view2` varchar(255) DEFAULT NULL,
  `view3` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`mxMRRunBlobId`),
  KEY `mxMRRunBlob_FK1` (`mxMRRunId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `PDB`
--

CREATE TABLE IF NOT EXISTS `PDB` (
  `pdbId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `contents` mediumtext,
  `code` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`pdbId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `Person`
--

CREATE TABLE IF NOT EXISTS `Person` (
  `personId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `laboratoryId` int(10) unsigned DEFAULT NULL,
  `siteId` int(11) DEFAULT NULL,
  `personUUID` varchar(45) DEFAULT NULL,
  `familyName` varchar(100) DEFAULT NULL,
  `givenName` varchar(45) DEFAULT NULL,
  `title` varchar(45) DEFAULT NULL,
  `emailAddress` varchar(60) DEFAULT NULL,
  `phoneNumber` varchar(45) DEFAULT NULL,
  `login` varchar(45) DEFAULT NULL,
  `passwd` varchar(45) DEFAULT NULL,
  `faxNumber` varchar(45) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  `externalId` binary(16) DEFAULT NULL,
  PRIMARY KEY (`personId`),
  KEY `Person_FKIndex1` (`laboratoryId`),
  KEY `Person_FKIndexFamilyName` (`familyName`),
  KEY `Person_FKIndex_Login` (`login`),
  KEY `siteId` (`siteId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=396013 ;

-- --------------------------------------------------------

--
-- Table structure for table `Phasing`
--

CREATE TABLE IF NOT EXISTS `Phasing` (
  `phasingId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `phasingAnalysisId` int(11) unsigned NOT NULL COMMENT 'Related phasing analysis item',
  `phasingProgramRunId` int(11) unsigned NOT NULL COMMENT 'Related program item',
  `spaceGroupId` int(10) unsigned DEFAULT NULL COMMENT 'Related spaceGroup',
  `method` enum('solvent flattening','solvent flipping') DEFAULT NULL COMMENT 'phasing method',
  `solventContent` double DEFAULT NULL,
  `enantiomorph` tinyint(1) DEFAULT NULL COMMENT '0 or 1',
  `lowRes` double DEFAULT NULL,
  `highRes` double DEFAULT NULL,
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`phasingId`),
  KEY `Phasing_FKIndex1` (`phasingAnalysisId`),
  KEY `Phasing_FKIndex2` (`phasingProgramRunId`),
  KEY `Phasing_FKIndex3` (`spaceGroupId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=106310 ;

-- --------------------------------------------------------

--
-- Table structure for table `PhasingAnalysis`
--

CREATE TABLE IF NOT EXISTS `PhasingAnalysis` (
  `phasingAnalysisId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`phasingAnalysisId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=135302 ;

-- --------------------------------------------------------

--
-- Table structure for table `PhasingProgramAttachment`
--

CREATE TABLE IF NOT EXISTS `PhasingProgramAttachment` (
  `phasingProgramAttachmentId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `phasingProgramRunId` int(11) unsigned NOT NULL COMMENT 'Related program item',
  `fileType` enum('Map','Logfile','PDB','CSV','INS','RES','TXT') DEFAULT NULL COMMENT 'file type',
  `fileName` varchar(45) DEFAULT NULL COMMENT 'file name',
  `filePath` varchar(255) DEFAULT NULL COMMENT 'file path',
  `recordTimeStamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`phasingProgramAttachmentId`),
  KEY `PhasingProgramAttachment_FKIndex1` (`phasingProgramRunId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=117079 ;

-- --------------------------------------------------------

--
-- Table structure for table `PhasingProgramRun`
--

CREATE TABLE IF NOT EXISTS `PhasingProgramRun` (
  `phasingProgramRunId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `phasingCommandLine` varchar(255) DEFAULT NULL COMMENT 'Command line for phasing',
  `phasingPrograms` varchar(255) DEFAULT NULL COMMENT 'Phasing programs (comma separated)',
  `phasingStatus` tinyint(1) DEFAULT NULL COMMENT 'success (1) / fail (0)',
  `phasingMessage` varchar(255) DEFAULT NULL COMMENT 'warning, error,...',
  `phasingStartTime` datetime DEFAULT NULL COMMENT 'Processing start time',
  `phasingEndTime` datetime DEFAULT NULL COMMENT 'Processing end time',
  `phasingEnvironment` varchar(255) DEFAULT NULL COMMENT 'Cpus, Nodes,...',
  `recordTimeStamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`phasingProgramRunId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=260251 ;

-- --------------------------------------------------------

--
-- Table structure for table `PhasingStatistics`
--

CREATE TABLE IF NOT EXISTS `PhasingStatistics` (
  `phasingStatisticsId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `phasingHasScalingId1` int(11) unsigned DEFAULT NULL COMMENT 'the dataset in question',
  `phasingHasScalingId2` int(11) unsigned DEFAULT NULL COMMENT 'if this is MIT or MAD, which scaling are being compared, null otherwise',
  `phasingStepId` int(10) unsigned DEFAULT NULL,
  `numberOfBins` int(11) DEFAULT NULL COMMENT 'the total number of bins',
  `binNumber` int(11) DEFAULT NULL COMMENT 'binNumber, 999 for overall',
  `lowRes` double DEFAULT NULL COMMENT 'low resolution cutoff of this binfloat',
  `highRes` double DEFAULT NULL COMMENT 'high resolution cutoff of this binfloat',
  `metric` enum('Rcullis','Average Fragment Length','Chain Count','Residues Count','CC','PhasingPower','FOM','<d"/sig>','Best CC','CC(1/2)','Weak CC','CFOM','Pseudo_free_CC','CC of partial model') DEFAULT NULL COMMENT 'metric',
  `statisticsValue` double DEFAULT NULL COMMENT 'the statistics value',
  `nReflections` int(11) DEFAULT NULL,
  `recordTimeStamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`phasingStatisticsId`),
  KEY `PhasingStatistics_FKIndex1` (`phasingHasScalingId1`),
  KEY `PhasingStatistics_FKIndex2` (`phasingHasScalingId2`),
  KEY `fk_PhasingStatistics_phasingStep_idx` (`phasingStepId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=534292 ;

-- --------------------------------------------------------

--
-- Table structure for table `PhasingStep`
--

CREATE TABLE IF NOT EXISTS `PhasingStep` (
  `phasingStepId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `previousPhasingStepId` int(10) unsigned DEFAULT NULL,
  `programRunId` int(10) unsigned DEFAULT NULL,
  `spaceGroupId` int(10) unsigned DEFAULT NULL,
  `autoProcScalingId` int(10) unsigned DEFAULT NULL,
  `phasingAnalysisId` int(10) unsigned DEFAULT NULL,
  `phasingStepType` enum('PREPARE','SUBSTRUCTUREDETERMINATION','PHASING','MODELBUILDING') DEFAULT NULL,
  `method` varchar(45) DEFAULT NULL,
  `solventContent` varchar(45) DEFAULT NULL,
  `enantiomorph` varchar(45) DEFAULT NULL,
  `lowRes` varchar(45) DEFAULT NULL,
  `highRes` varchar(45) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`phasingStepId`),
  KEY `FK_programRun_id` (`programRunId`),
  KEY `FK_spacegroup_id` (`spaceGroupId`),
  KEY `FK_autoprocScaling_id` (`autoProcScalingId`),
  KEY `FK_phasingAnalysis_id` (`phasingAnalysisId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=124948 ;

-- --------------------------------------------------------

--
-- Table structure for table `Phasing_has_Scaling`
--

CREATE TABLE IF NOT EXISTS `Phasing_has_Scaling` (
  `phasingHasScalingId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `phasingAnalysisId` int(11) unsigned NOT NULL COMMENT 'Related phasing analysis item',
  `autoProcScalingId` int(10) unsigned NOT NULL COMMENT 'Related autoProcScaling item',
  `datasetNumber` int(11) DEFAULT NULL COMMENT 'serial number of the dataset and always reserve 0 for the reference',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`phasingHasScalingId`),
  KEY `PhasingHasScaling_FKIndex1` (`phasingAnalysisId`),
  KEY `PhasingHasScaling_FKIndex2` (`autoProcScalingId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=135302 ;

-- --------------------------------------------------------

--
-- Table structure for table `PlateGroup`
--

CREATE TABLE IF NOT EXISTS `PlateGroup` (
  `plateGroupId` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `storageTemperature` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`plateGroupId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=10691 ;

-- --------------------------------------------------------

--
-- Table structure for table `PlateType`
--

CREATE TABLE IF NOT EXISTS `PlateType` (
  `PlateTypeId` int(10) NOT NULL AUTO_INCREMENT,
  `experimentId` int(10) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  `shape` varchar(45) DEFAULT NULL,
  `rowCount` int(11) DEFAULT NULL,
  `columnCount` int(11) DEFAULT NULL,
  PRIMARY KEY (`PlateTypeId`),
  KEY `PlateTypeToExperiment` (`experimentId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

-- --------------------------------------------------------

--
-- Table structure for table `Position`
--

CREATE TABLE IF NOT EXISTS `Position` (
  `positionId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `relativePositionId` int(11) unsigned DEFAULT NULL COMMENT 'relative position, null otherwise',
  `posX` double DEFAULT NULL,
  `posY` double DEFAULT NULL,
  `posZ` double DEFAULT NULL,
  `scale` double DEFAULT NULL,
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`positionId`),
  KEY `Position_FKIndex1` (`relativePositionId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `PreparePhasingData`
--

CREATE TABLE IF NOT EXISTS `PreparePhasingData` (
  `preparePhasingDataId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `phasingAnalysisId` int(11) unsigned NOT NULL COMMENT 'Related phasing analysis item',
  `phasingProgramRunId` int(11) unsigned NOT NULL COMMENT 'Related program item',
  `spaceGroupId` int(10) unsigned DEFAULT NULL COMMENT 'Related spaceGroup',
  `lowRes` double DEFAULT NULL,
  `highRes` double DEFAULT NULL,
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`preparePhasingDataId`),
  KEY `PreparePhasingData_FKIndex1` (`phasingAnalysisId`),
  KEY `PreparePhasingData_FKIndex2` (`phasingProgramRunId`),
  KEY `PreparePhasingData_FKIndex3` (`spaceGroupId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=14499 ;

-- --------------------------------------------------------

--
-- Table structure for table `Proposal`
--

CREATE TABLE IF NOT EXISTS `Proposal` (
  `proposalId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `personId` int(10) unsigned NOT NULL DEFAULT '0',
  `title` varchar(200) DEFAULT NULL,
  `proposalCode` varchar(45) DEFAULT NULL,
  `proposalNumber` varchar(45) DEFAULT NULL,
  `proposalType` varchar(2) DEFAULT NULL COMMENT 'Proposal type: MX, BX',
  `bltimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `externalId` binary(16) DEFAULT NULL,
  PRIMARY KEY (`proposalId`),
  KEY `Proposal_FKIndex1` (`personId`),
  KEY `Proposal_FKIndexCodeNumber` (`proposalCode`,`proposalNumber`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7914 ;

-- --------------------------------------------------------

--
-- Table structure for table `ProposalHasPerson`
--

CREATE TABLE IF NOT EXISTS `ProposalHasPerson` (
  `proposalHasPersonId` int(10) unsigned NOT NULL,
  `proposalId` int(10) unsigned NOT NULL,
  `personId` int(10) unsigned NOT NULL,
  PRIMARY KEY (`proposalHasPersonId`),
  KEY `fk_ProposalHasPerson_Proposal` (`proposalId`),
  KEY `fk_ProposalHasPerson_Personal` (`personId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Protein`
--

CREATE TABLE IF NOT EXISTS `Protein` (
  `proteinId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `proposalId` int(10) unsigned NOT NULL DEFAULT '0',
  `name` varchar(255) DEFAULT NULL,
  `acronym` varchar(45) DEFAULT NULL,
  `molecularMass` double DEFAULT NULL,
  `proteinType` varchar(45) DEFAULT NULL,
  `sequence` varchar(255) DEFAULT NULL,
  `personId` int(10) unsigned DEFAULT NULL,
  `bltimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `isCreatedBySampleSheet` tinyint(1) DEFAULT '0',
  `externalId` binary(16) DEFAULT NULL,
  PRIMARY KEY (`proteinId`),
  KEY `Protein_FKIndex1` (`proposalId`),
  KEY `ProteinAcronym_Index` (`proposalId`,`acronym`),
  KEY `Protein_FKIndex2` (`personId`),
  KEY `Protein_Index2` (`acronym`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=368184 ;

-- --------------------------------------------------------

--
-- Table structure for table `Protein_has_PDB`
--

CREATE TABLE IF NOT EXISTS `Protein_has_PDB` (
  `proteinhaspdbid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `proteinid` int(11) unsigned NOT NULL,
  `pdbid` int(11) unsigned NOT NULL,
  PRIMARY KEY (`proteinhaspdbid`),
  KEY `Protein_Has_PDB_fk1` (`proteinid`),
  KEY `Protein_Has_PDB_fk2` (`pdbid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `RigidBodyModeling`
--

CREATE TABLE IF NOT EXISTS `RigidBodyModeling` (
  `rigidBodyModelingId` int(11) NOT NULL AUTO_INCREMENT,
  `subtractionId` int(11) NOT NULL,
  `fitFilePath` varchar(255) DEFAULT NULL,
  `rigidBodyModelFilePath` varchar(255) DEFAULT NULL,
  `logFilePath` varchar(255) DEFAULT NULL,
  `curveConfigFilePath` varchar(255) DEFAULT NULL,
  `subUnitConfigFilePath` varchar(255) DEFAULT NULL,
  `crossCorrConfigFilePath` varchar(255) DEFAULT NULL,
  `contactDescriptionFilePath` varchar(255) DEFAULT NULL,
  `symmetry` varchar(255) DEFAULT NULL,
  `creationDate` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`rigidBodyModelingId`),
  KEY `fk_RigidBodyModeling_1` (`subtractionId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `Run`
--

CREATE TABLE IF NOT EXISTS `Run` (
  `runId` int(10) NOT NULL AUTO_INCREMENT,
  `timePerFrame` varchar(45) DEFAULT NULL,
  `timeStart` varchar(45) DEFAULT NULL,
  `timeEnd` varchar(45) DEFAULT NULL,
  `storageTemperature` varchar(45) DEFAULT NULL,
  `exposureTemperature` varchar(45) DEFAULT NULL,
  `spectrophotometer` varchar(45) DEFAULT NULL,
  `energy` varchar(45) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `frameAverage` varchar(45) DEFAULT NULL,
  `frameCount` varchar(45) DEFAULT NULL,
  `transmission` varchar(45) DEFAULT NULL,
  `beamCenterX` varchar(45) DEFAULT NULL,
  `beamCenterY` varchar(45) DEFAULT NULL,
  `pixelSizeX` varchar(45) DEFAULT NULL,
  `pixelSizeY` varchar(45) DEFAULT NULL,
  `radiationRelative` varchar(45) DEFAULT NULL,
  `radiationAbsolute` varchar(45) DEFAULT NULL,
  `normalization` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`runId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=104522 ;

-- --------------------------------------------------------

--
-- Table structure for table `SafetyLevel`
--

CREATE TABLE IF NOT EXISTS `SafetyLevel` (
  `safetyLevelId` int(10) NOT NULL AUTO_INCREMENT,
  `code` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`safetyLevelId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

-- --------------------------------------------------------

--
-- Table structure for table `SamplePlate`
--

CREATE TABLE IF NOT EXISTS `SamplePlate` (
  `samplePlateId` int(10) NOT NULL AUTO_INCREMENT,
  `experimentId` int(10) NOT NULL,
  `plateGroupId` int(10) DEFAULT NULL,
  `plateTypeId` int(10) DEFAULT NULL,
  `instructionSetId` int(10) DEFAULT NULL,
  `boxId` int(10) unsigned DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `slotPositionRow` varchar(45) DEFAULT NULL,
  `slotPositionColumn` varchar(45) DEFAULT NULL,
  `storageTemperature` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`samplePlateId`),
  KEY `PlateToPtateGroup` (`plateGroupId`),
  KEY `SamplePlateToType` (`plateTypeId`),
  KEY `SamplePlateToExperiment` (`experimentId`),
  KEY `SamplePlateToInstructionSet` (`instructionSetId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=32071 ;

-- --------------------------------------------------------

--
-- Table structure for table `SamplePlatePosition`
--

CREATE TABLE IF NOT EXISTS `SamplePlatePosition` (
  `samplePlatePositionId` int(10) NOT NULL AUTO_INCREMENT,
  `samplePlateId` int(10) NOT NULL,
  `rowNumber` int(11) DEFAULT NULL,
  `columnNumber` int(11) DEFAULT NULL,
  `volume` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`samplePlatePositionId`),
  KEY `PlatePositionToPlate` (`samplePlateId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=90358 ;

-- --------------------------------------------------------

--
-- Table structure for table `SaxsDataCollection`
--

CREATE TABLE IF NOT EXISTS `SaxsDataCollection` (
  `dataCollectionId` int(10) NOT NULL AUTO_INCREMENT,
  `experimentId` int(10) NOT NULL,
  `comments` varchar(5120) DEFAULT NULL,
  PRIMARY KEY (`dataCollectionId`),
  KEY `SaxsDataCollectionToExperiment` (`experimentId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=74880 ;

-- --------------------------------------------------------

--
-- Table structure for table `Screening`
--

CREATE TABLE IF NOT EXISTS `Screening` (
  `screeningId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `diffractionPlanId` int(10) unsigned DEFAULT NULL COMMENT 'references DiffractionPlan',
  `dataCollectionGroupId` int(11) DEFAULT NULL,
  `dataCollectionId` int(11) unsigned DEFAULT NULL,
  `bltimeStamp` timestamp NULL DEFAULT NULL,
  `programVersion` varchar(45) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `shortComments` varchar(20) DEFAULT NULL,
  `xmlSampleInformation` longblob,
  PRIMARY KEY (`screeningId`),
  KEY `Screening_FKIndexDiffractionPlanId` (`diffractionPlanId`),
  KEY `dcgroupId` (`dataCollectionGroupId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=388467 ;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningInput`
--

CREATE TABLE IF NOT EXISTS `ScreeningInput` (
  `screeningInputId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `screeningId` int(10) unsigned NOT NULL DEFAULT '0',
  `diffractionPlanId` int(10) DEFAULT NULL COMMENT 'references DiffractionPlan table',
  `beamX` float DEFAULT NULL,
  `beamY` float DEFAULT NULL,
  `rmsErrorLimits` float DEFAULT NULL,
  `minimumFractionIndexed` float DEFAULT NULL,
  `maximumFractionRejected` float DEFAULT NULL,
  `minimumSignalToNoise` float DEFAULT NULL,
  `xmlSampleInformation` longblob,
  PRIMARY KEY (`screeningInputId`),
  KEY `ScreeningInput_FKIndex1` (`screeningId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=97571 ;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningOutput`
--

CREATE TABLE IF NOT EXISTS `ScreeningOutput` (
  `screeningOutputId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `screeningId` int(10) unsigned NOT NULL DEFAULT '0',
  `statusDescription` varchar(1024) DEFAULT NULL,
  `rejectedReflections` int(10) unsigned DEFAULT NULL,
  `resolutionObtained` float DEFAULT NULL,
  `spotDeviationR` float DEFAULT NULL,
  `spotDeviationTheta` float DEFAULT NULL,
  `beamShiftX` float DEFAULT NULL,
  `beamShiftY` float DEFAULT NULL,
  `numSpotsFound` int(10) unsigned DEFAULT NULL,
  `numSpotsUsed` int(10) unsigned DEFAULT NULL,
  `numSpotsRejected` int(10) unsigned DEFAULT NULL,
  `mosaicity` float DEFAULT NULL,
  `iOverSigma` float DEFAULT NULL,
  `diffractionRings` tinyint(1) DEFAULT NULL,
  `strategySuccess` tinyint(1) NOT NULL DEFAULT '0',
  `mosaicityEstimated` tinyint(1) NOT NULL DEFAULT '0',
  `rankingResolution` double DEFAULT NULL,
  `program` varchar(45) DEFAULT NULL,
  `doseTotal` double DEFAULT NULL,
  `totalExposureTime` double DEFAULT NULL,
  `totalRotationRange` double DEFAULT NULL,
  `totalNumberOfImages` int(11) DEFAULT NULL,
  `rFriedel` double DEFAULT NULL,
  `indexingSuccess` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`screeningOutputId`),
  KEY `ScreeningOutput_FKIndex1` (`screeningId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=388408 ;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningOutputLattice`
--

CREATE TABLE IF NOT EXISTS `ScreeningOutputLattice` (
  `screeningOutputLatticeId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `screeningOutputId` int(10) unsigned NOT NULL DEFAULT '0',
  `spaceGroup` varchar(45) DEFAULT NULL,
  `pointGroup` varchar(45) DEFAULT NULL,
  `bravaisLattice` varchar(45) DEFAULT NULL,
  `rawOrientationMatrix_a_x` float DEFAULT NULL,
  `rawOrientationMatrix_a_y` float DEFAULT NULL,
  `rawOrientationMatrix_a_z` float DEFAULT NULL,
  `rawOrientationMatrix_b_x` float DEFAULT NULL,
  `rawOrientationMatrix_b_y` float DEFAULT NULL,
  `rawOrientationMatrix_b_z` float DEFAULT NULL,
  `rawOrientationMatrix_c_x` float DEFAULT NULL,
  `rawOrientationMatrix_c_y` float DEFAULT NULL,
  `rawOrientationMatrix_c_z` float DEFAULT NULL,
  `unitCell_a` float DEFAULT NULL,
  `unitCell_b` float DEFAULT NULL,
  `unitCell_c` float DEFAULT NULL,
  `unitCell_alpha` float DEFAULT NULL,
  `unitCell_beta` float DEFAULT NULL,
  `unitCell_gamma` float DEFAULT NULL,
  `bltimeStamp` timestamp NULL DEFAULT NULL,
  `labelitIndexing` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`screeningOutputLatticeId`),
  KEY `ScreeningOutputLattice_FKIndex1` (`screeningOutputId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=266667 ;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningRank`
--

CREATE TABLE IF NOT EXISTS `ScreeningRank` (
  `screeningRankId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `screeningRankSetId` int(10) unsigned NOT NULL DEFAULT '0',
  `screeningId` int(10) unsigned NOT NULL DEFAULT '0',
  `rankValue` float DEFAULT NULL,
  `rankInformation` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`screeningRankId`),
  KEY `ScreeningRank_FKIndex1` (`screeningId`),
  KEY `ScreeningRank_FKIndex2` (`screeningRankSetId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6239 ;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningRankSet`
--

CREATE TABLE IF NOT EXISTS `ScreeningRankSet` (
  `screeningRankSetId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `rankEngine` varchar(255) DEFAULT NULL,
  `rankingProjectFileName` varchar(255) DEFAULT NULL,
  `rankingSummaryFileName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`screeningRankSetId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=993 ;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningStrategy`
--

CREATE TABLE IF NOT EXISTS `ScreeningStrategy` (
  `screeningStrategyId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `screeningOutputId` int(10) unsigned NOT NULL DEFAULT '0',
  `phiStart` float DEFAULT NULL,
  `phiEnd` float DEFAULT NULL,
  `rotation` float DEFAULT NULL,
  `exposureTime` float DEFAULT NULL,
  `resolution` float DEFAULT NULL,
  `completeness` float DEFAULT NULL,
  `multiplicity` float DEFAULT NULL,
  `anomalous` tinyint(1) NOT NULL DEFAULT '0',
  `program` varchar(45) DEFAULT NULL,
  `rankingResolution` float DEFAULT NULL,
  `transmission` float DEFAULT NULL COMMENT 'Transmission for the strategy as given by the strategy program.',
  PRIMARY KEY (`screeningStrategyId`),
  KEY `ScreeningStrategy_FKIndex1` (`screeningOutputId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=210821 ;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningStrategySubWedge`
--

CREATE TABLE IF NOT EXISTS `ScreeningStrategySubWedge` (
  `screeningStrategySubWedgeId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `screeningStrategyWedgeId` int(10) unsigned DEFAULT NULL COMMENT 'Foreign key to parent table',
  `subWedgeNumber` int(10) unsigned DEFAULT NULL COMMENT 'The number of this subwedge within the wedge',
  `rotationAxis` varchar(45) DEFAULT NULL COMMENT 'Angle where subwedge starts',
  `axisStart` float DEFAULT NULL COMMENT 'Angle where subwedge ends',
  `axisEnd` float DEFAULT NULL COMMENT 'Exposure time for subwedge',
  `exposureTime` float DEFAULT NULL COMMENT 'Transmission for subwedge',
  `transmission` float DEFAULT NULL,
  `oscillationRange` float DEFAULT NULL,
  `completeness` float DEFAULT NULL,
  `multiplicity` float DEFAULT NULL,
  `doseTotal` float DEFAULT NULL COMMENT 'Total dose for this subwedge',
  `numberOfImages` int(10) unsigned DEFAULT NULL COMMENT 'Number of images for this subwedge',
  `comments` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`screeningStrategySubWedgeId`),
  KEY `ScreeningStrategySubWedge_FK1` (`screeningStrategyWedgeId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=212731 ;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningStrategyWedge`
--

CREATE TABLE IF NOT EXISTS `ScreeningStrategyWedge` (
  `screeningStrategyWedgeId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `screeningStrategyId` int(10) unsigned DEFAULT NULL COMMENT 'Foreign key to parent table',
  `wedgeNumber` int(10) unsigned DEFAULT NULL COMMENT 'The number of this wedge within the strategy',
  `resolution` float DEFAULT NULL,
  `completeness` float DEFAULT NULL,
  `multiplicity` float DEFAULT NULL,
  `doseTotal` float DEFAULT NULL COMMENT 'Total dose for this wedge',
  `numberOfImages` int(10) unsigned DEFAULT NULL COMMENT 'Number of images for this wedge',
  `phi` float DEFAULT NULL,
  `kappa` float DEFAULT NULL,
  `chi` float DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `wavelength` double DEFAULT NULL,
  PRIMARY KEY (`screeningStrategyWedgeId`),
  KEY `ScreeningStrategyWedge_IBFK_1` (`screeningStrategyId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=205569 ;

-- --------------------------------------------------------

--
-- Table structure for table `Session_has_Person`
--

CREATE TABLE IF NOT EXISTS `Session_has_Person` (
  `sessionId` int(10) unsigned NOT NULL DEFAULT '0',
  `personId` int(10) unsigned NOT NULL DEFAULT '0',
  `role` enum('Local Contact','Local Contact 2','Staff','Team Leader','Co-Investigator','Principal Investigator','Alternate Contact') DEFAULT NULL,
  PRIMARY KEY (`sessionId`,`personId`),
  KEY `Session_has_Person_FKIndex1` (`sessionId`),
  KEY `Session_has_Person_FKIndex2` (`personId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Shipping`
--

CREATE TABLE IF NOT EXISTS `Shipping` (
  `shippingId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `proposalId` int(10) unsigned NOT NULL DEFAULT '0',
  `shippingName` varchar(45) DEFAULT NULL,
  `deliveryAgent_agentName` varchar(45) DEFAULT NULL,
  `deliveryAgent_shippingDate` date DEFAULT NULL,
  `deliveryAgent_deliveryDate` date DEFAULT NULL,
  `deliveryAgent_agentCode` varchar(45) DEFAULT NULL,
  `deliveryAgent_flightCode` varchar(45) DEFAULT NULL,
  `shippingStatus` varchar(45) DEFAULT NULL,
  `bltimeStamp` datetime DEFAULT NULL,
  `laboratoryId` int(10) unsigned DEFAULT NULL,
  `isStorageShipping` tinyint(1) DEFAULT '0',
  `creationDate` datetime DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `sendingLabContactId` int(10) unsigned DEFAULT NULL,
  `returnLabContactId` int(10) unsigned DEFAULT NULL,
  `returnCourier` varchar(45) DEFAULT NULL,
  `dateOfShippingToUser` datetime DEFAULT NULL,
  `shippingType` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`shippingId`),
  KEY `Shipping_FKIndex1` (`proposalId`),
  KEY `laboratoryId` (`laboratoryId`),
  KEY `Shipping_FKIndex2` (`sendingLabContactId`),
  KEY `Shipping_FKIndex3` (`returnLabContactId`),
  KEY `Shipping_FKIndexCreationDate` (`creationDate`),
  KEY `Shipping_FKIndexName` (`shippingName`),
  KEY `Shipping_FKIndexStatus` (`shippingStatus`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=307574 ;

-- --------------------------------------------------------

--
-- Table structure for table `ShippingHasSession`
--

CREATE TABLE IF NOT EXISTS `ShippingHasSession` (
  `shippingId` int(10) unsigned NOT NULL,
  `sessionId` int(10) unsigned NOT NULL,
  PRIMARY KEY (`shippingId`,`sessionId`),
  KEY `ShippingHasSession_FKIndex1` (`shippingId`),
  KEY `ShippingHasSession_FKIndex2` (`sessionId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `SpaceGroup`
--

CREATE TABLE IF NOT EXISTS `SpaceGroup` (
  `spaceGroupId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `geometryClassnameId` int(11) unsigned DEFAULT NULL,
  `spaceGroupNumber` int(10) unsigned DEFAULT NULL COMMENT 'ccp4 number pr IUCR',
  `spaceGroupShortName` varchar(45) DEFAULT NULL COMMENT 'short name without blank',
  `spaceGroupName` varchar(45) DEFAULT NULL COMMENT 'verbose name',
  `bravaisLattice` varchar(45) DEFAULT NULL COMMENT 'short name',
  `bravaisLatticeName` varchar(45) DEFAULT NULL COMMENT 'verbose name',
  `pointGroup` varchar(45) DEFAULT NULL COMMENT 'point group',
  `MX_used` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1 if used in the crystal form',
  PRIMARY KEY (`spaceGroupId`),
  KEY `SpaceGroup_FKShortName` (`spaceGroupShortName`),
  KEY `geometryClassnameId` (`geometryClassnameId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=237 ;

-- --------------------------------------------------------

--
-- Table structure for table `Specimen`
--

CREATE TABLE IF NOT EXISTS `Specimen` (
  `specimenId` int(10) NOT NULL AUTO_INCREMENT,
  `experimentId` int(10) NOT NULL,
  `bufferId` int(10) DEFAULT NULL,
  `macromoleculeId` int(10) DEFAULT NULL,
  `samplePlatePositionId` int(10) DEFAULT NULL,
  `safetyLevelId` int(10) DEFAULT NULL,
  `stockSolutionId` int(10) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `concentration` varchar(45) DEFAULT NULL,
  `volume` varchar(45) DEFAULT NULL,
  `comments` varchar(5120) DEFAULT NULL,
  PRIMARY KEY (`specimenId`),
  KEY `SamplePlateWellToBuffer` (`bufferId`),
  KEY `SamplePlateWellToMacromolecule` (`macromoleculeId`),
  KEY `SamplePlateWellToSamplePlatePosition` (`samplePlatePositionId`),
  KEY `SamplePlateWellToSafetyLevel` (`safetyLevelId`),
  KEY `SamplePlateWellToExperiment` (`experimentId`),
  KEY `SampleToStockSolution` (`stockSolutionId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=92142 ;

-- --------------------------------------------------------

--
-- Table structure for table `StockSolution`
--

CREATE TABLE IF NOT EXISTS `StockSolution` (
  `stockSolutionId` int(10) NOT NULL AUTO_INCREMENT,
  `proposalId` int(10) NOT NULL DEFAULT '-1',
  `bufferId` int(10) NOT NULL,
  `macromoleculeId` int(10) DEFAULT NULL,
  `instructionSetId` int(10) DEFAULT NULL,
  `boxId` int(10) unsigned DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `storageTemperature` varchar(55) DEFAULT NULL,
  `volume` varchar(55) DEFAULT NULL,
  `concentration` varchar(55) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`stockSolutionId`),
  KEY `StockSolutionToBuffer` (`bufferId`),
  KEY `StockSolutionToMacromolecule` (`macromoleculeId`),
  KEY `StockSolutionToInstructionSet` (`instructionSetId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=72 ;

-- --------------------------------------------------------

--
-- Table structure for table `Stoichiometry`
--

CREATE TABLE IF NOT EXISTS `Stoichiometry` (
  `stoichiometryId` int(10) NOT NULL AUTO_INCREMENT,
  `hostMacromoleculeId` int(10) NOT NULL,
  `macromoleculeId` int(10) NOT NULL,
  `ratio` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`stoichiometryId`),
  KEY `StoichiometryToHost` (`hostMacromoleculeId`),
  KEY `StoichiometryToMacromolecule` (`macromoleculeId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=21 ;

-- --------------------------------------------------------

--
-- Table structure for table `Structure`
--

CREATE TABLE IF NOT EXISTS `Structure` (
  `structureId` int(10) NOT NULL AUTO_INCREMENT,
  `macromoleculeId` int(10) NOT NULL,
  `filePath` varchar(2048) DEFAULT NULL,
  `structureType` varchar(45) DEFAULT NULL,
  `fromResiduesBases` varchar(45) DEFAULT NULL,
  `toResiduesBases` varchar(45) DEFAULT NULL,
  `sequence` varchar(45) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `symmetry` varchar(45) DEFAULT NULL,
  `multiplicity` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`structureId`),
  KEY `StructureToMacromolecule` (`macromoleculeId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=31 ;

-- --------------------------------------------------------

--
-- Table structure for table `SubstructureDetermination`
--

CREATE TABLE IF NOT EXISTS `SubstructureDetermination` (
  `substructureDeterminationId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `phasingAnalysisId` int(11) unsigned NOT NULL COMMENT 'Related phasing analysis item',
  `phasingProgramRunId` int(11) unsigned NOT NULL COMMENT 'Related program item',
  `spaceGroupId` int(10) unsigned DEFAULT NULL COMMENT 'Related spaceGroup',
  `method` enum('SAD','MAD','SIR','SIRAS','MR','MIR','MIRAS','RIP','RIPAS') DEFAULT NULL COMMENT 'phasing method',
  `lowRes` double DEFAULT NULL,
  `highRes` double DEFAULT NULL,
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`substructureDeterminationId`),
  KEY `SubstructureDetermination_FKIndex1` (`phasingAnalysisId`),
  KEY `SubstructureDetermination_FKIndex2` (`phasingProgramRunId`),
  KEY `SubstructureDetermination_FKIndex3` (`spaceGroupId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=14495 ;

-- --------------------------------------------------------

--
-- Table structure for table `Subtraction`
--

CREATE TABLE IF NOT EXISTS `Subtraction` (
  `subtractionId` int(10) NOT NULL AUTO_INCREMENT,
  `dataCollectionId` int(10) NOT NULL,
  `rg` varchar(45) DEFAULT NULL,
  `rgStdev` varchar(45) DEFAULT NULL,
  `I0` varchar(45) DEFAULT NULL,
  `I0Stdev` varchar(45) DEFAULT NULL,
  `firstPointUsed` varchar(45) DEFAULT NULL,
  `lastPointUsed` varchar(45) DEFAULT NULL,
  `quality` varchar(45) DEFAULT NULL,
  `isagregated` varchar(45) DEFAULT NULL,
  `concentration` varchar(45) DEFAULT NULL,
  `gnomFilePath` varchar(255) DEFAULT NULL,
  `rgGuinier` varchar(45) DEFAULT NULL,
  `rgGnom` varchar(45) DEFAULT NULL,
  `dmax` varchar(45) DEFAULT NULL,
  `total` varchar(45) DEFAULT NULL,
  `volume` varchar(45) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `kratkyFilePath` varchar(255) DEFAULT NULL,
  `scatteringFilePath` varchar(255) DEFAULT NULL,
  `guinierFilePath` varchar(255) DEFAULT NULL,
  `substractedFilePath` varchar(255) DEFAULT NULL,
  `gnomFilePathOutput` varchar(255) DEFAULT NULL,
  `sampleOneDimensionalFiles` int(10) DEFAULT NULL,
  `bufferOnedimensionalFiles` int(10) DEFAULT NULL,
  `sampleAverageFilePath` varchar(255) DEFAULT NULL,
  `bufferAverageFilePath` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`subtractionId`),
  KEY `EdnaAnalysisToMeasurement` (`dataCollectionId`),
  KEY `fk_Subtraction_1` (`sampleOneDimensionalFiles`),
  KEY `fk_Subtraction_2` (`bufferOnedimensionalFiles`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=48463 ;

-- --------------------------------------------------------

--
-- Table structure for table `SubtractionToAbInitioModel`
--

CREATE TABLE IF NOT EXISTS `SubtractionToAbInitioModel` (
  `subtractionToAbInitioModelId` int(10) NOT NULL AUTO_INCREMENT,
  `abInitioId` int(10) DEFAULT NULL,
  `subtractionId` int(10) DEFAULT NULL,
  PRIMARY KEY (`subtractionToAbInitioModelId`),
  KEY `substractionToAbInitioModelToAbinitioModel` (`abInitioId`),
  KEY `ubstractionToSubstraction` (`subtractionId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=31965 ;

-- --------------------------------------------------------

--
-- Table structure for table `Superposition`
--

CREATE TABLE IF NOT EXISTS `Superposition` (
  `superpositionId` int(11) NOT NULL AUTO_INCREMENT,
  `subtractionId` int(11) NOT NULL,
  `abinitioModelPdbFilePath` varchar(255) DEFAULT NULL,
  `aprioriPdbFilePath` varchar(255) DEFAULT NULL,
  `alignedPdbFilePath` varchar(255) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  PRIMARY KEY (`superpositionId`),
  KEY `fk_Superposition_1` (`subtractionId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `UntrustedRegion`
--

CREATE TABLE IF NOT EXISTS `UntrustedRegion` (
  `untrustedRegionId` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `detectorId` int(11) NOT NULL,
  `x1` int(11) NOT NULL,
  `x2` int(11) NOT NULL,
  `y1` int(11) NOT NULL,
  `y2` int(11) NOT NULL,
  PRIMARY KEY (`untrustedRegionId`),
  KEY `UntrustedRegion_FKIndex1` (`detectorId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Untrsuted region linked to a detector' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Stand-in structure for view `V_AnalysisInfo`
--
CREATE TABLE IF NOT EXISTS `V_AnalysisInfo` (
`experimentCreationDate` datetime
,`timeStart` varchar(45)
,`dataCollectionId` int(10)
,`measurementId` int(10)
,`proposalId` int(10) unsigned
,`proposalCode` varchar(45)
,`proposalNumber` varchar(45)
,`priorityLevelId` int(10)
,`code` varchar(100)
,`exposureTemperature` varchar(45)
,`transmission` varchar(45)
,`measurementComments` varchar(512)
,`experimentComments` varchar(512)
,`experimentId` int(11)
,`experimentType` varchar(128)
,`conc` varchar(45)
,`bufferAcronym` varchar(45)
,`macromoleculeAcronym` varchar(45)
,`bufferId` int(10)
,`macromoleculeId` int(11)
,`subtractedFilePath` varchar(255)
,`rgGuinier` varchar(45)
,`firstPointUsed` varchar(45)
,`lastPointUsed` varchar(45)
,`I0` varchar(45)
,`isagregated` varchar(45)
,`subtractionId` bigint(11)
,`rgGnom` varchar(45)
,`total` varchar(45)
,`dmax` varchar(45)
,`volume` varchar(45)
,`i0stdev` varchar(45)
,`quality` varchar(45)
,`substractionCreationTime` datetime
,`bufferBeforeMeasurementId` bigint(11)
,`bufferAfterMeasurementId` bigint(11)
,`bufferBeforeFramesMerged` varchar(45)
,`bufferBeforeMergeId` bigint(11)
,`bufferBeforeAverageFilePath` varchar(255)
,`sampleMeasurementId` bigint(11)
,`sampleMergeId` bigint(11)
,`averageFilePath` varchar(255)
,`framesMerge` varchar(45)
,`framesCount` varchar(45)
,`bufferAfterFramesMerged` varchar(45)
,`bufferAfterMergeId` bigint(11)
,`bufferAfterAverageFilePath` varchar(255)
,`modelListId1` bigint(11)
,`nsdFilePath` varchar(255)
,`modelListId2` bigint(11)
,`chi2RgFilePath` varchar(255)
,`averagedModel` varchar(255)
,`averagedModelId` bigint(11)
,`rapidShapeDeterminationModel` varchar(255)
,`rapidShapeDeterminationModelId` bigint(11)
,`shapeDeterminationModel` varchar(255)
,`shapeDeterminationModelId` bigint(11)
,`abInitioModelId` bigint(11)
,`comments` varchar(512)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection`
--
CREATE TABLE IF NOT EXISTS `v_datacollection` (
`dataCollectionId` int(11) unsigned
,`dataCollectionGroupId` int(11)
,`strategySubWedgeOrigId` int(10) unsigned
,`detectorId` int(11)
,`blSubSampleId` int(11) unsigned
,`dataCollectionNumber` int(10) unsigned
,`startTime` datetime
,`endTime` datetime
,`runStatus` varchar(45)
,`axisStart` float
,`axisEnd` float
,`axisRange` float
,`overlap` float
,`numberOfImages` int(10) unsigned
,`startImageNumber` int(10) unsigned
,`numberOfPasses` int(10) unsigned
,`exposureTime` float
,`imageDirectory` varchar(255)
,`imagePrefix` varchar(100)
,`imageSuffix` varchar(45)
,`fileTemplate` varchar(255)
,`wavelength` float
,`resolution` float
,`detectorDistance` float
,`xBeam` float
,`yBeam` float
,`xBeamPix` float
,`yBeamPix` float
,`comments` varchar(1024)
,`printableForReport` tinyint(1) unsigned
,`slitGapVertical` float
,`slitGapHorizontal` float
,`transmission` float
,`synchrotronMode` varchar(20)
,`xtalSnapshotFullPath1` varchar(255)
,`xtalSnapshotFullPath2` varchar(255)
,`xtalSnapshotFullPath3` varchar(255)
,`xtalSnapshotFullPath4` varchar(255)
,`rotationAxis` enum('Omega','Kappa','Phi')
,`phiStart` float
,`kappaStart` float
,`omegaStart` float
,`resolutionAtCorner` float
,`detector2Theta` float
,`undulatorGap1` float
,`undulatorGap2` float
,`undulatorGap3` float
,`beamSizeAtSampleX` float
,`beamSizeAtSampleY` float
,`centeringMethod` varchar(255)
,`averageTemperature` float
,`actualCenteringPosition` varchar(255)
,`beamShape` varchar(45)
,`flux` double
,`flux_end` double
,`totalAbsorbedDose` double
,`bestWilsonPlotPath` varchar(255)
,`imageQualityIndicatorsPlotPath` varchar(512)
,`imageQualityIndicatorsCSVPath` varchar(512)
,`sessionId` int(10) unsigned
,`proposalId` int(10) unsigned
,`workflowId` int(11) unsigned
,`rankingResolution` double
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection_autoprocintegration`
--
CREATE TABLE IF NOT EXISTS `v_datacollection_autoprocintegration` (
`v_datacollection_summary_phasing_autoProcIntegrationId` int(10) unsigned
,`v_datacollection_summary_phasing_dataCollectionId` int(11) unsigned
,`v_datacollection_summary_phasing_cell_a` float
,`v_datacollection_summary_phasing_cell_b` float
,`v_datacollection_summary_phasing_cell_c` float
,`v_datacollection_summary_phasing_cell_alpha` float
,`v_datacollection_summary_phasing_cell_beta` float
,`v_datacollection_summary_phasing_cell_gamma` float
,`v_datacollection_summary_phasing_anomalous` tinyint(1)
,`v_datacollection_summary_phasing_autoproc_space_group` varchar(45)
,`v_datacollection_summary_phasing_autoproc_autoprocId` int(10) unsigned
,`v_datacollection_summary_phasing_autoProcScalingId` int(10) unsigned
,`v_datacollection_processingPrograms` varchar(255)
,`v_datacollection_summary_phasing_autoProcProgramId` int(10) unsigned
,`v_datacollection_processingStatus` tinyint(1)
,`v_datacollection_summary_session_sessionId` int(10) unsigned
,`v_datacollection_summary_session_proposalId` int(10) unsigned
,`AutoProcIntegration_dataCollectionId` int(11) unsigned
,`AutoProcIntegration_autoProcIntegrationId` int(10) unsigned
,`PhasingStep_phasing_phasingStepType` enum('PREPARE','SUBSTRUCTUREDETERMINATION','PHASING','MODELBUILDING')
,`SpaceGroup_spaceGroupShortName` varchar(45)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection_phasing`
--
CREATE TABLE IF NOT EXISTS `v_datacollection_phasing` (
`phasingStepId` int(10) unsigned
,`previousPhasingStepId` int(10) unsigned
,`phasingAnalysisId` int(10) unsigned
,`autoProcIntegrationId` int(10) unsigned
,`dataCollectionId` int(11) unsigned
,`anomalous` tinyint(1)
,`spaceGroup` varchar(45)
,`autoProcId` int(10) unsigned
,`phasingStepType` enum('PREPARE','SUBSTRUCTUREDETERMINATION','PHASING','MODELBUILDING')
,`method` varchar(45)
,`solventContent` varchar(45)
,`enantiomorph` varchar(45)
,`lowRes` varchar(45)
,`highRes` varchar(45)
,`autoProcScalingId` int(10) unsigned
,`spaceGroupShortName` varchar(45)
,`processingPrograms` varchar(255)
,`processingStatus` tinyint(1)
,`phasingPrograms` varchar(255)
,`phasingStatus` tinyint(1)
,`phasingStartTime` datetime
,`phasingEndTime` datetime
,`sessionId` int(10) unsigned
,`proposalId` int(10) unsigned
,`blSampleId` int(10) unsigned
,`name` varchar(100)
,`code` varchar(45)
,`acronym` varchar(45)
,`proteinId` int(10) unsigned
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection_phasing_program_run`
--
CREATE TABLE IF NOT EXISTS `v_datacollection_phasing_program_run` (
`phasingStepId` int(10) unsigned
,`previousPhasingStepId` int(10) unsigned
,`phasingAnalysisId` int(10) unsigned
,`autoProcIntegrationId` int(10) unsigned
,`dataCollectionId` int(11) unsigned
,`autoProcId` int(10) unsigned
,`phasingStepType` enum('PREPARE','SUBSTRUCTUREDETERMINATION','PHASING','MODELBUILDING')
,`method` varchar(45)
,`autoProcScalingId` int(10) unsigned
,`spaceGroupShortName` varchar(45)
,`phasingPrograms` varchar(255)
,`phasingStatus` tinyint(1)
,`sessionId` int(10) unsigned
,`proposalId` int(10) unsigned
,`blSampleId` int(10) unsigned
,`name` varchar(100)
,`code` varchar(45)
,`acronym` varchar(45)
,`proteinId` int(10) unsigned
,`phasingProgramAttachmentId` int(11) unsigned
,`fileType` enum('Map','Logfile','PDB','CSV','INS','RES','TXT')
,`fileName` varchar(45)
,`filePath` varchar(255)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection_summary`
--
CREATE TABLE IF NOT EXISTS `v_datacollection_summary` (
`DataCollectionGroup_dataCollectionGroupId` int(11)
,`DataCollectionGroup_blSampleId` int(10) unsigned
,`DataCollectionGroup_sessionId` int(10) unsigned
,`DataCollectionGroup_workflowId` int(11) unsigned
,`DataCollectionGroup_experimentType` enum('SAD','SAD - Inverse Beam','OSC','Collect - Multiwedge','MAD','Helical','Multi-positional','Mesh','Burn','MAD - Inverse Beam','Characterization','Dehydration')
,`DataCollectionGroup_startTime` datetime
,`DataCollectionGroup_endTime` datetime
,`DataCollectionGroup_comments` varchar(1024)
,`DataCollectionGroup_actualSampleBarcode` varchar(45)
,`DataCollectionGroup_xtalSnapshotFullPath` varchar(255)
,`BLSample_blSampleId` int(10) unsigned
,`BLSample_crystalId` int(10) unsigned
,`BLSample_name` varchar(100)
,`BLSample_code` varchar(45)
,`BLSample_location` varchar(45)
,`BLSample_blSampleStatus` varchar(20)
,`BLSample_comments` varchar(1024)
,`Container_containerId` int(10) unsigned
,`BLSession_sessionId` int(10) unsigned
,`BLSession_proposalId` int(10) unsigned
,`BLSession_protectedData` varchar(1024)
,`Dewar_dewarId` int(10) unsigned
,`Dewar_code` varchar(45)
,`Dewar_storageLocation` varchar(45)
,`Container_containerType` varchar(20)
,`Container_capacity` int(10) unsigned
,`Container_beamlineLocation` varchar(20)
,`Container_sampleChangerLocation` varchar(20)
,`Protein_proteinId` int(10) unsigned
,`Protein_name` varchar(255)
,`Protein_acronym` varchar(45)
,`DataCollection_dataCollectionId` int(11) unsigned
,`DataCollection_dataCollectionGroupId` int(11)
,`DataCollection_startTime` datetime
,`DataCollection_endTime` datetime
,`DataCollection_runStatus` varchar(45)
,`DataCollection_numberOfImages` int(10) unsigned
,`DataCollection_startImageNumber` int(10) unsigned
,`DataCollection_numberOfPasses` int(10) unsigned
,`DataCollection_exposureTime` float
,`DataCollection_imageDirectory` varchar(255)
,`DataCollection_wavelength` float
,`DataCollection_resolution` float
,`DataCollection_detectorDistance` float
,`DataCollection_xBeam` float
,`transmission` float
,`DataCollection_yBeam` float
,`DataCollection_imagePrefix` varchar(100)
,`DataCollection_comments` varchar(1024)
,`DataCollection_xtalSnapshotFullPath1` varchar(255)
,`DataCollection_xtalSnapshotFullPath2` varchar(255)
,`DataCollection_xtalSnapshotFullPath3` varchar(255)
,`DataCollection_xtalSnapshotFullPath4` varchar(255)
,`DataCollection_phiStart` float
,`DataCollection_kappaStart` float
,`DataCollection_omegaStart` float
,`DataCollection_flux` double
,`DataCollection_flux_end` double
,`DataCollection_resolutionAtCorner` float
,`DataCollection_bestWilsonPlotPath` varchar(255)
,`DataCollection_dataCollectionNumber` int(10) unsigned
,`DataCollection_axisRange` float
,`DataCollection_axisStart` float
,`DataCollection_axisEnd` float
,`DataCollection_rotationAxis` enum('Omega','Kappa','Phi')
,`Workflow_workflowTitle` varchar(255)
,`Workflow_workflowType` enum('Characterisation','Undefined','BioSAXS Post Processing','EnhancedCharacterisation','LineScan','MeshScan','Dehydration','KappaReorientation','BurnStrategy','XrayCentering','DiffractionTomography','TroubleShooting','VisualReorientation','HelicalCharacterisation','GroupedProcessing','MXPressE','MXPressO','MXPressL','MXScore','MXPressI','MXPressM','MXPressA','CollectAndSpectra','LowDoseDC')
,`Workflow_status` varchar(255)
,`Workflow_workflowId` int(11) unsigned
,`AutoProcIntegration_dataCollectionId` int(11) unsigned
,`autoProcScalingId` int(10) unsigned
,`cell_a` float
,`cell_b` float
,`cell_c` float
,`cell_alpha` float
,`cell_beta` float
,`cell_gamma` float
,`scalingStatisticsType` enum('overall','innerShell','outerShell')
,`resolutionLimitHigh` float
,`resolutionLimitLow` float
,`completeness` float
,`AutoProc_spaceGroup` varchar(45)
,`autoProcId` int(10) unsigned
,`rMerge` float
,`AutoProcIntegration_autoProcIntegrationId` int(10) unsigned
,`AutoProcProgram_processingPrograms` varchar(255)
,`AutoProcProgram_processingStatus` tinyint(1)
,`AutoProcProgram_autoProcProgramId` int(10) unsigned
,`Screening_screeningId` int(10) unsigned
,`Screening_dataCollectionId` int(11) unsigned
,`Screening_dataCollectionGroupId` int(11)
,`ScreeningOutput_strategySuccess` tinyint(1)
,`ScreeningOutput_indexingSuccess` tinyint(1)
,`ScreeningOutput_rankingResolution` double
,`ScreeningOutput_mosaicity` float
,`ScreeningOutputLattice_spaceGroup` varchar(45)
,`ScreeningOutputLattice_unitCell_a` float
,`ScreeningOutputLattice_unitCell_b` float
,`ScreeningOutputLattice_unitCell_c` float
,`ScreeningOutputLattice_unitCell_alpha` float
,`ScreeningOutputLattice_unitCell_beta` float
,`ScreeningOutputLattice_unitCell_gamma` float
,`Shipping_shippingId` int(10) unsigned
,`Shipping_shippingName` varchar(45)
,`Shipping_shippingStatus` varchar(45)
,`diffractionPlanId` int(10) unsigned
,`experimentKind` enum('Default','MXPressE','MXPressO','MXPressI','MXPressE_SAD','MXScore','MXPressM','MAD','SAD','Fixed','Ligand binding','Refinement','OSC','MAD - Inverse Beam','SAD - Inverse Beam')
,`observedResolution` float
,`minimalResolution` float
,`exposureTime` float
,`oscillationRange` float
,`maximalResolution` float
,`screeningResolution` float
,`radiationSensitivity` float
,`anomalousScatterer` varchar(255)
,`preferredBeamSizeX` float
,`preferredBeamSizeY` float
,`preferredBeamDiameter` float
,`DiffractipnPlan_comments` varchar(1024)
,`aimedCompleteness` double
,`aimedIOverSigmaAtHighestRes` double
,`aimedMultiplicity` double
,`aimedResolution` double
,`anomalousData` tinyint(1)
,`complexity` varchar(45)
,`estimateRadiationDamage` tinyint(1)
,`forcedSpaceGroup` varchar(45)
,`requiredCompleteness` double
,`requiredMultiplicity` double
,`requiredResolution` double
,`strategyOption` varchar(45)
,`kappaStrategyOption` varchar(45)
,`numberOfPositions` int(11)
,`minDimAccrossSpindleAxis` double
,`maxDimAccrossSpindleAxis` double
,`radiationSensitivityBeta` double
,`radiationSensitivityGamma` double
,`minOscWidth` float
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection_summary_autoprocintegration`
--
CREATE TABLE IF NOT EXISTS `v_datacollection_summary_autoprocintegration` (
`AutoProcIntegration_dataCollectionId` int(11) unsigned
,`cell_a` float
,`cell_b` float
,`cell_c` float
,`cell_alpha` float
,`cell_beta` float
,`cell_gamma` float
,`AutoProcIntegration_autoProcIntegrationId` int(10) unsigned
,`v_datacollection_summary_autoprocintegration_processingPrograms` varchar(255)
,`AutoProcProgram_autoProcProgramId` int(10) unsigned
,`v_datacollection_summary_autoprocintegration_processingStatus` tinyint(1)
,`AutoProcIntegration_phasing_dataCollectionId` int(11) unsigned
,`PhasingStep_phasing_phasingStepType` enum('PREPARE','SUBSTRUCTUREDETERMINATION','PHASING','MODELBUILDING')
,`SpaceGroup_spaceGroupShortName` varchar(45)
,`autoProcId` int(10) unsigned
,`AutoProc_spaceGroup` varchar(45)
,`scalingStatisticsType` enum('overall','innerShell','outerShell')
,`resolutionLimitHigh` float
,`resolutionLimitLow` float
,`rMerge` float
,`completeness` float
,`autoProcScalingId` int(10) unsigned
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection_summary_datacollectiongroup`
--
CREATE TABLE IF NOT EXISTS `v_datacollection_summary_datacollectiongroup` (
`DataCollectionGroup_dataCollectionGroupId` int(11)
,`DataCollectionGroup_blSampleId` int(10) unsigned
,`DataCollectionGroup_sessionId` int(10) unsigned
,`DataCollectionGroup_workflowId` int(11) unsigned
,`DataCollectionGroup_experimentType` enum('SAD','SAD - Inverse Beam','OSC','Collect - Multiwedge','MAD','Helical','Multi-positional','Mesh','Burn','MAD - Inverse Beam','Characterization','Dehydration')
,`DataCollectionGroup_startTime` datetime
,`DataCollectionGroup_endTime` datetime
,`DataCollectionGroup_comments` varchar(1024)
,`DataCollectionGroup_actualSampleBarcode` varchar(45)
,`DataCollectionGroup_xtalSnapshotFullPath` varchar(255)
,`BLSample_blSampleId` int(10) unsigned
,`BLSample_crystalId` int(10) unsigned
,`BLSample_name` varchar(100)
,`BLSample_code` varchar(45)
,`BLSession_sessionId` int(10) unsigned
,`BLSession_proposalId` int(10) unsigned
,`BLSession_protectedData` varchar(1024)
,`Protein_proteinId` int(10) unsigned
,`Protein_name` varchar(255)
,`Protein_acronym` varchar(45)
,`DataCollection_dataCollectionId` int(11) unsigned
,`DataCollection_dataCollectionGroupId` int(11)
,`DataCollection_startTime` datetime
,`DataCollection_endTime` datetime
,`DataCollection_runStatus` varchar(45)
,`DataCollection_numberOfImages` int(10) unsigned
,`DataCollection_startImageNumber` int(10) unsigned
,`DataCollection_numberOfPasses` int(10) unsigned
,`DataCollection_exposureTime` float
,`DataCollection_imageDirectory` varchar(255)
,`DataCollection_wavelength` float
,`DataCollection_resolution` float
,`DataCollection_detectorDistance` float
,`DataCollection_xBeam` float
,`DataCollection_yBeam` float
,`DataCollection_comments` varchar(1024)
,`DataCollection_xtalSnapshotFullPath1` varchar(255)
,`DataCollection_xtalSnapshotFullPath2` varchar(255)
,`DataCollection_xtalSnapshotFullPath3` varchar(255)
,`DataCollection_xtalSnapshotFullPath4` varchar(255)
,`DataCollection_phiStart` float
,`DataCollection_kappaStart` float
,`DataCollection_omegaStart` float
,`DataCollection_resolutionAtCorner` float
,`DataCollection_bestWilsonPlotPath` varchar(255)
,`DataCollection_dataCollectionNumber` int(10) unsigned
,`DataCollection_axisRange` float
,`DataCollection_axisStart` float
,`DataCollection_axisEnd` float
,`Workflow_workflowTitle` varchar(255)
,`Workflow_workflowType` enum('Characterisation','Undefined','BioSAXS Post Processing','EnhancedCharacterisation','LineScan','MeshScan','Dehydration','KappaReorientation','BurnStrategy','XrayCentering','DiffractionTomography','TroubleShooting','VisualReorientation','HelicalCharacterisation','GroupedProcessing','MXPressE','MXPressO','MXPressL','MXScore','MXPressI','MXPressM','MXPressA','CollectAndSpectra','LowDoseDC')
,`Workflow_status` varchar(255)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection_summary_phasing`
--
CREATE TABLE IF NOT EXISTS `v_datacollection_summary_phasing` (
`v_datacollection_summary_phasing_autoProcIntegrationId` int(10) unsigned
,`v_datacollection_summary_phasing_dataCollectionId` int(11) unsigned
,`v_datacollection_summary_phasing_cell_a` float
,`v_datacollection_summary_phasing_cell_b` float
,`v_datacollection_summary_phasing_cell_c` float
,`v_datacollection_summary_phasing_cell_alpha` float
,`v_datacollection_summary_phasing_cell_beta` float
,`v_datacollection_summary_phasing_cell_gamma` float
,`v_datacollection_summary_phasing_anomalous` tinyint(1)
,`v_datacollection_summary_phasing_autoproc_space_group` varchar(45)
,`v_datacollection_summary_phasing_autoproc_autoprocId` int(10) unsigned
,`v_datacollection_summary_phasing_autoProcScalingId` int(10) unsigned
,`v_datacollection_summary_phasing_processingPrograms` varchar(255)
,`v_datacollection_summary_phasing_autoProcProgramId` int(10) unsigned
,`v_datacollection_summary_phasing_processingStatus` tinyint(1)
,`v_datacollection_summary_session_sessionId` int(10) unsigned
,`v_datacollection_summary_session_proposalId` int(10) unsigned
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection_summary_screening`
--
CREATE TABLE IF NOT EXISTS `v_datacollection_summary_screening` (
`Screening_screeningId` int(10) unsigned
,`Screening_dataCollectionId` int(11) unsigned
,`Screening_dataCollectionGroupId` int(11)
,`ScreeningOutput_strategySuccess` tinyint(1)
,`ScreeningOutput_indexingSuccess` tinyint(1)
,`ScreeningOutput_rankingResolution` double
,`ScreeningOutput_mosaicityEstimated` tinyint(1)
,`ScreeningOutput_mosaicity` float
,`ScreeningOutputLattice_spaceGroup` varchar(45)
,`ScreeningOutputLattice_unitCell_a` float
,`ScreeningOutputLattice_unitCell_b` float
,`ScreeningOutputLattice_unitCell_c` float
,`ScreeningOutputLattice_unitCell_alpha` float
,`ScreeningOutputLattice_unitCell_beta` float
,`ScreeningOutputLattice_unitCell_gamma` float
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_dewar`
--
CREATE TABLE IF NOT EXISTS `v_dewar` (
`proposalId` int(10) unsigned
,`shippingId` int(10) unsigned
,`shippingName` varchar(45)
,`dewarId` int(10) unsigned
,`dewarName` varchar(45)
,`dewarStatus` varchar(45)
,`proposalCode` varchar(45)
,`proposalNumber` varchar(45)
,`creationDate` datetime
,`shippingType` varchar(45)
,`barCode` varchar(45)
,`shippingStatus` varchar(45)
,`beamLineName` varchar(45)
,`nbEvents` bigint(21)
,`storesin` bigint(21)
,`nbSamples` bigint(21)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_dewarBeamline`
--
CREATE TABLE IF NOT EXISTS `v_dewarBeamline` (
`beamLineName` varchar(45)
,`COUNT(*)` bigint(21)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_dewarBeamlineByWeek`
--
CREATE TABLE IF NOT EXISTS `v_dewarBeamlineByWeek` (
`Week` varchar(16)
,`ID14` bigint(21)
,`ID23` bigint(21)
,`ID29` bigint(21)
,`BM14` bigint(21)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_dewarByWeek`
--
CREATE TABLE IF NOT EXISTS `v_dewarByWeek` (
`Week` varchar(16)
,`Dewars Tracked` bigint(21)
,`Dewars Non-Tracked` bigint(21)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_dewarByWeekTotal`
--
CREATE TABLE IF NOT EXISTS `v_dewarByWeekTotal` (
`Week` varchar(16)
,`Dewars Tracked` bigint(21)
,`Dewars Non-Tracked` bigint(21)
,`Total` bigint(21)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_dewarList`
--
CREATE TABLE IF NOT EXISTS `v_dewarList` (
`proposal` varchar(90)
,`shippingName` varchar(45)
,`dewarName` varchar(45)
,`barCode` varchar(45)
,`creationDate` varchar(10)
,`shippingType` varchar(45)
,`nbEvents` bigint(21)
,`dewarStatus` varchar(45)
,`shippingStatus` varchar(45)
,`nbSamples` bigint(21)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_dewarProposalCode`
--
CREATE TABLE IF NOT EXISTS `v_dewarProposalCode` (
`proposalCode` varchar(45)
,`COUNT(*)` bigint(21)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_dewarProposalCodeByWeek`
--
CREATE TABLE IF NOT EXISTS `v_dewarProposalCodeByWeek` (
`Week` varchar(16)
,`MX` bigint(21)
,`FX` bigint(21)
,`BM14U` bigint(21)
,`BM161` bigint(21)
,`BM162` bigint(21)
,`Others` bigint(21)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_dewar_summary`
--
CREATE TABLE IF NOT EXISTS `v_dewar_summary` (
`shippingName` varchar(45)
,`deliveryAgent_agentName` varchar(45)
,`deliveryAgent_shippingDate` date
,`deliveryAgent_deliveryDate` date
,`deliveryAgent_agentCode` varchar(45)
,`deliveryAgent_flightCode` varchar(45)
,`shippingStatus` varchar(45)
,`bltimeStamp` datetime
,`laboratoryId` int(10) unsigned
,`isStorageShipping` tinyint(1)
,`creationDate` datetime
,`Shipping_comments` varchar(255)
,`sendingLabContactId` int(10) unsigned
,`returnLabContactId` int(10) unsigned
,`returnCourier` varchar(45)
,`dateOfShippingToUser` datetime
,`shippingType` varchar(45)
,`dewarId` int(10) unsigned
,`shippingId` int(10) unsigned
,`dewarCode` varchar(45)
,`comments` tinytext
,`storageLocation` varchar(45)
,`dewarStatus` varchar(45)
,`isStorageDewar` tinyint(1)
,`barCode` varchar(45)
,`firstExperimentId` int(10) unsigned
,`customsValue` int(11) unsigned
,`transportValue` int(11) unsigned
,`trackingNumberToSynchrotron` varchar(30)
,`trackingNumberFromSynchrotron` varchar(30)
,`type` enum('Dewar','Toolbox')
,`sessionId` int(10) unsigned
,`beamlineName` varchar(45)
,`sessionStartDate` datetime
,`sessionEndDate` datetime
,`beamLineOperator` varchar(45)
,`proposalId` int(10) unsigned
,`containerId` int(10) unsigned
,`containerType` varchar(20)
,`capacity` int(10) unsigned
,`beamlineLocation` varchar(20)
,`sampleChangerLocation` varchar(20)
,`containerStatus` varchar(45)
,`containerCode` varchar(45)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_energyScan`
--
CREATE TABLE IF NOT EXISTS `v_energyScan` (
`energyScanId` int(10) unsigned
,`sessionId` int(10) unsigned
,`blSampleId` int(10) unsigned
,`fluorescenceDetector` varchar(255)
,`scanFileFullPath` varchar(255)
,`choochFileFullPath` varchar(255)
,`jpegChoochFileFullPath` varchar(255)
,`element` varchar(45)
,`startEnergy` float
,`endEnergy` float
,`transmissionFactor` float
,`exposureTime` float
,`synchrotronCurrent` float
,`temperature` float
,`peakEnergy` float
,`peakFPrime` float
,`peakFDoublePrime` float
,`inflectionEnergy` float
,`inflectionFPrime` float
,`inflectionFDoublePrime` float
,`xrayDose` float
,`startTime` datetime
,`endTime` datetime
,`edgeEnergy` varchar(255)
,`filename` varchar(255)
,`beamSizeVertical` float
,`beamSizeHorizontal` float
,`crystalClass` varchar(20)
,`comments` varchar(1024)
,`flux` double
,`flux_end` double
,`BLSample_sampleId` int(10) unsigned
,`name` varchar(100)
,`code` varchar(45)
,`acronym` varchar(45)
,`BLSession_proposalId` int(10) unsigned
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_hour`
--
CREATE TABLE IF NOT EXISTS `v_hour` (
`num` varchar(18)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_Log4Stat`
--
CREATE TABLE IF NOT EXISTS `v_Log4Stat` (
`id` int(11)
,`priority` varchar(15)
,`timestamp` datetime
,`msg` varchar(255)
,`detail` varchar(255)
,`value` varchar(255)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_logonByHour`
--
CREATE TABLE IF NOT EXISTS `v_logonByHour` (
`Hour` varchar(7)
,`Distinct logins` bigint(21)
,`Total logins` bigint(22)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_logonByMonthDay`
--
CREATE TABLE IF NOT EXISTS `v_logonByMonthDay` (
`Day` varchar(5)
,`Distinct logins` bigint(21)
,`Total logins` bigint(22)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_logonByWeek`
--
CREATE TABLE IF NOT EXISTS `v_logonByWeek` (
`Week` varchar(16)
,`Distinct logins` bigint(21)
,`Total logins` bigint(22)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_logonByWeekDay`
--
CREATE TABLE IF NOT EXISTS `v_logonByWeekDay` (
`Day` varchar(64)
,`Distinct logins` bigint(21)
,`Total logins` bigint(22)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_monthDay`
--
CREATE TABLE IF NOT EXISTS `v_monthDay` (
`num` varchar(10)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_mx_sample`
--
CREATE TABLE IF NOT EXISTS `v_mx_sample` (
`BLSample_blSampleId` int(10) unsigned
,`BLSample_diffractionPlanId` int(10) unsigned
,`BLSample_crystalId` int(10) unsigned
,`BLSample_containerId` int(10) unsigned
,`BLSample_name` varchar(100)
,`BLSample_code` varchar(45)
,`BLSample_location` varchar(45)
,`BLSample_holderLength` double
,`BLSample_loopLength` double
,`BLSample_loopType` varchar(45)
,`BLSample_wireWidth` double
,`BLSample_comments` varchar(1024)
,`BLSample_completionStage` varchar(45)
,`BLSample_structureStage` varchar(45)
,`BLSample_publicationStage` varchar(45)
,`BLSample_publicationComments` varchar(255)
,`BLSample_blSampleStatus` varchar(20)
,`BLSample_isInSampleChanger` tinyint(1)
,`BLSample_lastKnownCenteringPosition` varchar(255)
,`BLSample_recordTimeStamp` timestamp
,`BLSample_SMILES` varchar(400)
,`Protein_proteinId` int(10) unsigned
,`Protein_name` varchar(255)
,`Protein_acronym` varchar(45)
,`Protein_proteinType` varchar(45)
,`Protein_proposalId` int(10) unsigned
,`Person_personId` int(10) unsigned
,`Person_familyName` varchar(100)
,`Person_givenName` varchar(45)
,`Person_emailAddress` varchar(60)
,`Container_containerId` int(10) unsigned
,`Container_code` varchar(45)
,`Container_containerType` varchar(20)
,`Container_containerStatus` varchar(45)
,`Dewar_code` varchar(45)
,`Dewar_dewarId` int(10) unsigned
,`Dewar_storageLocation` varchar(45)
,`Dewar_dewarStatus` varchar(45)
,`Dewar_barCode` varchar(45)
,`Shipping_shippingId` int(10) unsigned
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_phasing`
--
CREATE TABLE IF NOT EXISTS `v_phasing` (
`BLSample_blSampleId` int(10) unsigned
,`AutoProcIntegration_autoProcIntegrationId` int(10) unsigned
,`AutoProcIntegration_dataCollectionId` int(11) unsigned
,`AutoProcIntegration_autoProcProgramId` int(10) unsigned
,`AutoProcIntegration_startImageNumber` int(10) unsigned
,`AutoProcIntegration_endImageNumber` int(10) unsigned
,`AutoProcIntegration_refinedDetectorDistance` float
,`AutoProcIntegration_refinedXBeam` float
,`AutoProcIntegration_refinedYBeam` float
,`AutoProcIntegration_rotationAxisX` float
,`AutoProcIntegration_rotationAxisY` float
,`AutoProcIntegration_rotationAxisZ` float
,`AutoProcIntegration_beamVectorX` float
,`AutoProcIntegration_beamVectorY` float
,`AutoProcIntegration_beamVectorZ` float
,`AutoProcIntegration_cell_a` float
,`AutoProcIntegration_cell_b` float
,`AutoProcIntegration_cell_c` float
,`AutoProcIntegration_cell_alpha` float
,`AutoProcIntegration_cell_beta` float
,`AutoProcIntegration_cell_gamma` float
,`AutoProcIntegration_recordTimeStamp` datetime
,`AutoProcIntegration_anomalous` tinyint(1)
,`SpaceGroup_spaceGroupId` int(10) unsigned
,`SpaceGroup_geometryClassnameId` int(11) unsigned
,`SpaceGroup_spaceGroupNumber` int(10) unsigned
,`SpaceGroup_spaceGroupShortName` varchar(45)
,`SpaceGroup_spaceGroupName` varchar(45)
,`SpaceGroup_bravaisLattice` varchar(45)
,`SpaceGroup_bravaisLatticeName` varchar(45)
,`SpaceGroup_pointGroup` varchar(45)
,`SpaceGroup_MX_used` tinyint(1)
,`PhasingStep_phasingStepId` int(10) unsigned
,`PhasingStep_previousPhasingStepId` int(10) unsigned
,`PhasingStep_programRunId` int(10) unsigned
,`PhasingStep_spaceGroupId` int(10) unsigned
,`PhasingStep_autoProcScalingId` int(10) unsigned
,`PhasingStep_phasingAnalysisId` int(10) unsigned
,`PhasingStep_phasingStepType` enum('PREPARE','SUBSTRUCTUREDETERMINATION','PHASING','MODELBUILDING')
,`PhasingStep_method` varchar(45)
,`PhasingStep_solventContent` varchar(45)
,`PhasingStep_enantiomorph` varchar(45)
,`PhasingStep_lowRes` varchar(45)
,`PhasingStep_highRes` varchar(45)
,`PhasingStep_recordTimeStamp` timestamp
,`DataCollection_dataCollectionId` int(11) unsigned
,`DataCollection_dataCollectionGroupId` int(11)
,`DataCollection_strategySubWedgeOrigId` int(10) unsigned
,`DataCollection_detectorId` int(11)
,`DataCollection_blSubSampleId` int(11) unsigned
,`DataCollection_dataCollectionNumber` int(10) unsigned
,`DataCollection_startTime` datetime
,`DataCollection_endTime` datetime
,`DataCollection_runStatus` varchar(45)
,`DataCollection_axisStart` float
,`DataCollection_axisEnd` float
,`DataCollection_axisRange` float
,`DataCollection_overlap` float
,`DataCollection_numberOfImages` int(10) unsigned
,`DataCollection_startImageNumber` int(10) unsigned
,`DataCollection_numberOfPasses` int(10) unsigned
,`DataCollection_exposureTime` float
,`DataCollection_imageDirectory` varchar(255)
,`DataCollection_imagePrefix` varchar(100)
,`DataCollection_imageSuffix` varchar(45)
,`DataCollection_fileTemplate` varchar(255)
,`DataCollection_wavelength` float
,`DataCollection_resolution` float
,`DataCollection_detectorDistance` float
,`DataCollection_xBeam` float
,`DataCollection_yBeam` float
,`DataCollection_xBeamPix` float
,`DataCollection_yBeamPix` float
,`DataCollection_comments` varchar(1024)
,`DataCollection_printableForReport` tinyint(1) unsigned
,`DataCollection_slitGapVertical` float
,`DataCollection_slitGapHorizontal` float
,`DataCollection_transmission` float
,`DataCollection_synchrotronMode` varchar(20)
,`DataCollection_xtalSnapshotFullPath1` varchar(255)
,`DataCollection_xtalSnapshotFullPath2` varchar(255)
,`DataCollection_xtalSnapshotFullPath3` varchar(255)
,`DataCollection_xtalSnapshotFullPath4` varchar(255)
,`DataCollection_rotationAxis` enum('Omega','Kappa','Phi')
,`DataCollection_phiStart` float
,`DataCollection_kappaStart` float
,`DataCollection_omegaStart` float
,`DataCollection_resolutionAtCorner` float
,`DataCollection_detector2Theta` float
,`DataCollection_undulatorGap1` float
,`DataCollection_undulatorGap2` float
,`DataCollection_undulatorGap3` float
,`DataCollection_beamSizeAtSampleX` float
,`DataCollection_beamSizeAtSampleY` float
,`DataCollection_centeringMethod` varchar(255)
,`DataCollection_averageTemperature` float
,`DataCollection_actualCenteringPosition` varchar(255)
,`DataCollection_beamShape` varchar(45)
,`DataCollection_flux` double
,`DataCollection_flux_end` double
,`DataCollection_totalAbsorbedDose` double
,`DataCollection_bestWilsonPlotPath` varchar(255)
,`DataCollection_imageQualityIndicatorsPlotPath` varchar(512)
,`DataCollection_imageQualityIndicatorsCSVPath` varchar(512)
,`PhasingProgramRun_phasingProgramRunId` int(11) unsigned
,`PhasingProgramRun_phasingCommandLine` varchar(255)
,`PhasingProgramRun_phasingPrograms` varchar(255)
,`PhasingProgramRun_phasingStatus` tinyint(1)
,`PhasingProgramRun_phasingMessage` varchar(255)
,`PhasingProgramRun_phasingStartTime` datetime
,`PhasingProgramRun_phasingEndTime` datetime
,`PhasingProgramRun_phasingEnvironment` varchar(255)
,`PhasingProgramRun_recordTimeStamp` timestamp
,`Protein_proteinId` int(10) unsigned
,`BLSession_sessionId` int(10) unsigned
,`BLSession_proposalId` int(10) unsigned
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_sample`
--
CREATE TABLE IF NOT EXISTS `v_sample` (
`proposalId` int(10) unsigned
,`shippingId` int(10) unsigned
,`dewarId` int(10) unsigned
,`containerId` int(10) unsigned
,`blSampleId` int(10) unsigned
,`proposalCode` varchar(45)
,`proposalNumber` varchar(45)
,`creationDate` datetime
,`shippingType` varchar(45)
,`barCode` varchar(45)
,`shippingStatus` varchar(45)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_sampleByWeek`
--
CREATE TABLE IF NOT EXISTS `v_sampleByWeek` (
`Week` varchar(16)
,`Samples` bigint(21)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_session`
--
CREATE TABLE IF NOT EXISTS `v_session` (
`sessionId` int(10) unsigned
,`expSessionPk` int(11) unsigned
,`beamLineSetupId` int(10) unsigned
,`proposalId` int(10) unsigned
,`projectCode` varchar(45)
,`BLSession_startDate` datetime
,`BLSession_endDate` datetime
,`beamLineName` varchar(45)
,`scheduled` tinyint(1)
,`nbShifts` int(10) unsigned
,`comments` varchar(2000)
,`beamLineOperator` varchar(45)
,`visit_number` int(10) unsigned
,`bltimeStamp` timestamp
,`usedFlag` tinyint(1)
,`sessionTitle` varchar(255)
,`structureDeterminations` float
,`dewarTransport` float
,`databackupFrance` float
,`databackupEurope` float
,`operatorSiteNumber` varchar(10)
,`BLSession_lastUpdate` timestamp
,`BLSession_protectedData` varchar(1024)
,`Proposal_title` varchar(200)
,`Proposal_proposalCode` varchar(45)
,`Proposal_ProposalNumber` varchar(45)
,`Proposal_ProposalType` varchar(2)
,`Person_personId` int(10) unsigned
,`Person_familyName` varchar(100)
,`Person_givenName` varchar(45)
,`Person_emailAddress` varchar(60)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_week`
--
CREATE TABLE IF NOT EXISTS `v_week` (
`num` varchar(7)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_weekDay`
--
CREATE TABLE IF NOT EXISTS `v_weekDay` (
`day` varchar(10)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_xfeFluorescenceSpectrum`
--
CREATE TABLE IF NOT EXISTS `v_xfeFluorescenceSpectrum` (
`xfeFluorescenceSpectrumId` int(10) unsigned
,`sessionId` int(10) unsigned
,`blSampleId` int(10) unsigned
,`fittedDataFileFullPath` varchar(255)
,`scanFileFullPath` varchar(255)
,`jpegScanFileFullPath` varchar(255)
,`startTime` datetime
,`endTime` datetime
,`filename` varchar(255)
,`energy` float
,`exposureTime` float
,`beamTransmission` float
,`annotatedPymcaXfeSpectrum` varchar(255)
,`beamSizeVertical` float
,`beamSizeHorizontal` float
,`crystalClass` varchar(20)
,`comments` varchar(1024)
,`flux` double
,`flux_end` double
,`workingDirectory` varchar(512)
,`BLSample_sampleId` int(10) unsigned
,`BLSession_proposalId` int(10) unsigned
);
-- --------------------------------------------------------

--
-- Table structure for table `Workflow`
--

CREATE TABLE IF NOT EXISTS `Workflow` (
  `workflowId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `workflowTitle` varchar(255) DEFAULT NULL,
  `workflowType` enum('Characterisation','Undefined','BioSAXS Post Processing','EnhancedCharacterisation','LineScan','MeshScan','Dehydration','KappaReorientation','BurnStrategy','XrayCentering','DiffractionTomography','TroubleShooting','VisualReorientation','HelicalCharacterisation','GroupedProcessing','MXPressE','MXPressO','MXPressL','MXScore','MXPressI','MXPressM','MXPressA','CollectAndSpectra','LowDoseDC') DEFAULT NULL,
  `workflowTypeId` int(11) DEFAULT NULL,
  `comments` varchar(1024) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `resultFilePath` varchar(255) DEFAULT NULL,
  `logFilePath` varchar(255) DEFAULT NULL,
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`workflowId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=54157 ;

-- --------------------------------------------------------

--
-- Table structure for table `WorkflowDehydration`
--

CREATE TABLE IF NOT EXISTS `WorkflowDehydration` (
  `workflowDehydrationId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `workflowId` int(11) unsigned NOT NULL COMMENT 'Related workflow',
  `dataFilePath` varchar(255) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`workflowDehydrationId`),
  KEY `WorkflowDehydration_FKIndex1` (`workflowId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `WorkflowMesh`
--

CREATE TABLE IF NOT EXISTS `WorkflowMesh` (
  `workflowMeshId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `workflowId` int(11) unsigned NOT NULL COMMENT 'Related workflow',
  `bestPositionId` int(11) unsigned DEFAULT NULL,
  `bestImageId` int(12) unsigned DEFAULT NULL,
  `value1` double DEFAULT NULL,
  `value2` double DEFAULT NULL,
  `value3` double DEFAULT NULL COMMENT 'N value',
  `value4` double DEFAULT NULL,
  `cartographyPath` varchar(255) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`workflowMeshId`),
  KEY `WorkflowMesh_FKIndex1` (`workflowId`),
  KEY `bestPositionId` (`bestPositionId`),
  KEY `bestImageId` (`bestImageId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=95506 ;

-- --------------------------------------------------------

--
-- Table structure for table `WorkflowStep`
--

CREATE TABLE IF NOT EXISTS `WorkflowStep` (
  `workflowStepId` int(11) NOT NULL AUTO_INCREMENT,
  `workflowId` int(11) unsigned NOT NULL,
  `workflowStepType` varchar(45) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `folderPath` varchar(1024) DEFAULT NULL,
  `imageResultFilePath` varchar(1024) DEFAULT NULL,
  `htmlResultFilePath` varchar(1024) DEFAULT NULL,
  `resultFilePath` varchar(1024) DEFAULT NULL,
  `comments` varchar(2048) DEFAULT NULL,
  `crystalSizeX` varchar(45) DEFAULT NULL,
  `crystalSizeY` varchar(45) DEFAULT NULL,
  `crystalSizeZ` varchar(45) DEFAULT NULL,
  `maxDozorScore` varchar(45) DEFAULT NULL,
  `recordTimeStamp` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`workflowStepId`),
  KEY `step_to_workflow_fk_idx` (`workflowId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=58364 ;

-- --------------------------------------------------------

--
-- Table structure for table `WorkflowType`
--

CREATE TABLE IF NOT EXISTS `WorkflowType` (
  `workflowTypeId` int(11) NOT NULL AUTO_INCREMENT,
  `workflowTypeName` varchar(45) DEFAULT NULL,
  `comments` varchar(2048) DEFAULT NULL,
  `recordTimeStamp` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`workflowTypeId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=24 ;

-- --------------------------------------------------------

--
-- Table structure for table `XFEFluorescenceSpectrum`
--

CREATE TABLE IF NOT EXISTS `XFEFluorescenceSpectrum` (
  `xfeFluorescenceSpectrumId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sessionId` int(10) unsigned NOT NULL,
  `blSampleId` int(10) unsigned DEFAULT NULL,
  `fittedDataFileFullPath` varchar(255) DEFAULT NULL,
  `scanFileFullPath` varchar(255) DEFAULT NULL,
  `jpegScanFileFullPath` varchar(255) DEFAULT NULL,
  `startTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `energy` float DEFAULT NULL,
  `exposureTime` float DEFAULT NULL,
  `beamTransmission` float DEFAULT NULL,
  `annotatedPymcaXfeSpectrum` varchar(255) DEFAULT NULL,
  `beamSizeVertical` float DEFAULT NULL,
  `beamSizeHorizontal` float DEFAULT NULL,
  `crystalClass` varchar(20) DEFAULT NULL,
  `comments` varchar(1024) DEFAULT NULL,
  `flux` double DEFAULT NULL COMMENT 'flux measured before the xrfSpectra',
  `flux_end` double DEFAULT NULL COMMENT 'flux measured after the xrfSpectra',
  `workingDirectory` varchar(512) DEFAULT NULL,
  `blSubSampleId` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`xfeFluorescenceSpectrumId`),
  KEY `XFEFluorescnceSpectrum_FKIndex1` (`blSampleId`),
  KEY `XFEFluorescnceSpectrum_FKIndex2` (`sessionId`),
  KEY `XFE_ibfk_3` (`blSubSampleId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5118 ;

-- --------------------------------------------------------

--
-- Structure for view `V_AnalysisInfo`
--
DROP TABLE IF EXISTS `V_AnalysisInfo`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'V_AnalysisInfo')

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection`
--
DROP TABLE IF EXISTS `v_datacollection`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_datacollection')

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_autoprocintegration`
--
DROP TABLE IF EXISTS `v_datacollection_autoprocintegration`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_datacollection_autoprocintegration')

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_phasing`
--
DROP TABLE IF EXISTS `v_datacollection_phasing`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_datacollection_phasing')

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_phasing_program_run`
--
DROP TABLE IF EXISTS `v_datacollection_phasing_program_run`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_datacollection_phasing_program_run')

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_summary`
--
DROP TABLE IF EXISTS `v_datacollection_summary`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_datacollection_summary')

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_summary_autoprocintegration`
--
DROP TABLE IF EXISTS `v_datacollection_summary_autoprocintegration`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_datacollection_summary_autoprocintegration')

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_summary_datacollectiongroup`
--
DROP TABLE IF EXISTS `v_datacollection_summary_datacollectiongroup`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_datacollection_summary_datacollectiongroup')

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_summary_phasing`
--
DROP TABLE IF EXISTS `v_datacollection_summary_phasing`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_datacollection_summary_phasing')

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_summary_screening`
--
DROP TABLE IF EXISTS `v_datacollection_summary_screening`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_datacollection_summary_screening')

-- --------------------------------------------------------

--
-- Structure for view `v_dewar`
--
DROP TABLE IF EXISTS `v_dewar`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_dewar')

-- --------------------------------------------------------

--
-- Structure for view `v_dewarBeamline`
--
DROP TABLE IF EXISTS `v_dewarBeamline`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_dewarBeamline')

-- --------------------------------------------------------

--
-- Structure for view `v_dewarBeamlineByWeek`
--
DROP TABLE IF EXISTS `v_dewarBeamlineByWeek`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_dewarBeamlineByWeek')

-- --------------------------------------------------------

--
-- Structure for view `v_dewarByWeek`
--
DROP TABLE IF EXISTS `v_dewarByWeek`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_dewarByWeek')

-- --------------------------------------------------------

--
-- Structure for view `v_dewarByWeekTotal`
--
DROP TABLE IF EXISTS `v_dewarByWeekTotal`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_dewarByWeekTotal')

-- --------------------------------------------------------

--
-- Structure for view `v_dewarList`
--
DROP TABLE IF EXISTS `v_dewarList`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_dewarList')

-- --------------------------------------------------------

--
-- Structure for view `v_dewarProposalCode`
--
DROP TABLE IF EXISTS `v_dewarProposalCode`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_dewarProposalCode')

-- --------------------------------------------------------

--
-- Structure for view `v_dewarProposalCodeByWeek`
--
DROP TABLE IF EXISTS `v_dewarProposalCodeByWeek`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_dewarProposalCodeByWeek')

-- --------------------------------------------------------

--
-- Structure for view `v_dewar_summary`
--
DROP TABLE IF EXISTS `v_dewar_summary`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_dewar_summary')

-- --------------------------------------------------------

--
-- Structure for view `v_energyScan`
--
DROP TABLE IF EXISTS `v_energyScan`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_energyScan')

-- --------------------------------------------------------

--
-- Structure for view `v_hour`
--
DROP TABLE IF EXISTS `v_hour`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_hour')

-- --------------------------------------------------------

--
-- Structure for view `v_Log4Stat`
--
DROP TABLE IF EXISTS `v_Log4Stat`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_Log4Stat')

-- --------------------------------------------------------

--
-- Structure for view `v_logonByHour`
--
DROP TABLE IF EXISTS `v_logonByHour`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_logonByHour')

-- --------------------------------------------------------

--
-- Structure for view `v_logonByMonthDay`
--
DROP TABLE IF EXISTS `v_logonByMonthDay`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_logonByMonthDay')

-- --------------------------------------------------------

--
-- Structure for view `v_logonByWeek`
--
DROP TABLE IF EXISTS `v_logonByWeek`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_logonByWeek')

-- --------------------------------------------------------

--
-- Structure for view `v_logonByWeekDay`
--
DROP TABLE IF EXISTS `v_logonByWeekDay`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_logonByWeekDay')

-- --------------------------------------------------------

--
-- Structure for view `v_monthDay`
--
DROP TABLE IF EXISTS `v_monthDay`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_monthDay')

-- --------------------------------------------------------

--
-- Structure for view `v_mx_sample`
--
DROP TABLE IF EXISTS `v_mx_sample`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_mx_sample')

-- --------------------------------------------------------

--
-- Structure for view `v_phasing`
--
DROP TABLE IF EXISTS `v_phasing`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_phasing')

-- --------------------------------------------------------

--
-- Structure for view `v_sample`
--
DROP TABLE IF EXISTS `v_sample`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_sample')

-- --------------------------------------------------------

--
-- Structure for view `v_sampleByWeek`
--
DROP TABLE IF EXISTS `v_sampleByWeek`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_sampleByWeek')

-- --------------------------------------------------------

--
-- Structure for view `v_session`
--
DROP TABLE IF EXISTS `v_session`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_session')

-- --------------------------------------------------------

--
-- Structure for view `v_week`
--
DROP TABLE IF EXISTS `v_week`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_week')

-- --------------------------------------------------------

--
-- Structure for view `v_weekDay`
--
DROP TABLE IF EXISTS `v_weekDay`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_weekDay')

-- --------------------------------------------------------

--
-- Structure for view `v_xfeFluorescenceSpectrum`
--
DROP TABLE IF EXISTS `v_xfeFluorescenceSpectrum`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxreader'@'myadminvm.esrf.fr' for table 'v_xfeFluorescenceSpectrum')

--
-- Constraints for dumped tables
--

--
-- Constraints for table `AbInitioModel`
--
ALTER TABLE `AbInitioModel`
  ADD CONSTRAINT `AbInitioModelToModelList` FOREIGN KEY (`modelListId`) REFERENCES `ModelList` (`modelListId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `AbInitioModelToRapid` FOREIGN KEY (`rapidShapeDeterminationModelId`) REFERENCES `Model` (`modelId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `AverageToModel` FOREIGN KEY (`averagedModelId`) REFERENCES `Model` (`modelId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `SahpeDeterminationToAbiniti` FOREIGN KEY (`shapeDeterminationModelId`) REFERENCES `Model` (`modelId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Assembly`
--
ALTER TABLE `Assembly`
  ADD CONSTRAINT `AssemblyToMacromolecule` FOREIGN KEY (`macromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `AssemblyHasMacromolecule`
--
ALTER TABLE `AssemblyHasMacromolecule`
  ADD CONSTRAINT `AssemblyHasMacromoleculeToAssembly` FOREIGN KEY (`assemblyId`) REFERENCES `Assembly` (`assemblyId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `AssemblyHasMacromoleculeToAssemblyRegion` FOREIGN KEY (`macromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `AssemblyRegion`
--
ALTER TABLE `AssemblyRegion`
  ADD CONSTRAINT `AssemblyRegionToAssemblyHasMacromolecule` FOREIGN KEY (`assemblyHasMacromoleculeId`) REFERENCES `AssemblyHasMacromolecule` (`AssemblyHasMacromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `AutoProcIntegration`
--
ALTER TABLE `AutoProcIntegration`
  ADD CONSTRAINT `AutoProcIntegration_ibfk_1` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `AutoProcIntegration_ibfk_2` FOREIGN KEY (`autoProcProgramId`) REFERENCES `AutoProcProgram` (`autoProcProgramId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `AutoProcProgramAttachment`
--
ALTER TABLE `AutoProcProgramAttachment`
  ADD CONSTRAINT `AutoProcProgramAttachmentFk1` FOREIGN KEY (`autoProcProgramId`) REFERENCES `AutoProcProgram` (`autoProcProgramId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `AutoProcScaling`
--
ALTER TABLE `AutoProcScaling`
  ADD CONSTRAINT `AutoProcScalingFk1` FOREIGN KEY (`autoProcId`) REFERENCES `AutoProc` (`autoProcId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `AutoProcScalingStatistics`
--
ALTER TABLE `AutoProcScalingStatistics`
  ADD CONSTRAINT `AutoProcScalingStatisticsFk1` FOREIGN KEY (`autoProcScalingId`) REFERENCES `AutoProcScaling` (`autoProcScalingId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `AutoProcScaling_has_Int`
--
ALTER TABLE `AutoProcScaling_has_Int`
  ADD CONSTRAINT `AutoProcScaling_has_IntFk1` FOREIGN KEY (`autoProcScalingId`) REFERENCES `AutoProcScaling` (`autoProcScalingId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `AutoProcScaling_has_IntFk2` FOREIGN KEY (`autoProcIntegrationId`) REFERENCES `AutoProcIntegration` (`autoProcIntegrationId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `AutoProcStatus`
--
ALTER TABLE `AutoProcStatus`
  ADD CONSTRAINT `AutoProcStatus_ibfk_1` FOREIGN KEY (`autoProcIntegrationId`) REFERENCES `AutoProcIntegration` (`autoProcIntegrationId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `BLSample`
--
ALTER TABLE `BLSample`
  ADD CONSTRAINT `BLSample_ibfk_1` FOREIGN KEY (`containerId`) REFERENCES `Container` (`containerId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `BLSample_ibfk_2` FOREIGN KEY (`crystalId`) REFERENCES `Crystal` (`crystalId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `BLSample_ibfk_3` FOREIGN KEY (`diffractionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `BLSession`
--
ALTER TABLE `BLSession`
  ADD CONSTRAINT `BLSession_ibfk_1` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `BLSession_ibfk_2` FOREIGN KEY (`beamLineSetupId`) REFERENCES `BeamLineSetup` (`beamLineSetupId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `BLSession_has_SCPosition`
--
ALTER TABLE `BLSession_has_SCPosition`
  ADD CONSTRAINT `blsession_has_scposition_FK1` FOREIGN KEY (`blsessionid`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `BLSubSample`
--
ALTER TABLE `BLSubSample`
  ADD CONSTRAINT `BLSubSample_blSamplefk_1` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `BLSubSample_diffractionPlanfk_1` FOREIGN KEY (`diffractionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `BLSubSample_positionfk_1` FOREIGN KEY (`positionId`) REFERENCES `Position` (`positionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `BLSubSample_positionfk_2` FOREIGN KEY (`position2Id`) REFERENCES `Position` (`positionId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Buffer`
--
ALTER TABLE `Buffer`
  ADD CONSTRAINT `BufferToSafetyLevel` FOREIGN KEY (`safetyLevelId`) REFERENCES `SafetyLevel` (`safetyLevelId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `BufferHasAdditive`
--
ALTER TABLE `BufferHasAdditive`
  ADD CONSTRAINT `BufferHasAdditiveToAdditive` FOREIGN KEY (`additiveId`) REFERENCES `Additive` (`additiveId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `BufferHasAdditiveToBuffer` FOREIGN KEY (`bufferId`) REFERENCES `Buffer` (`bufferId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `BufferHasAdditiveToUnit` FOREIGN KEY (`measurementUnitId`) REFERENCES `MeasurementUnit` (`measurementUnitId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Container`
--
ALTER TABLE `Container`
  ADD CONSTRAINT `Container_ibfk5` FOREIGN KEY (`ownerId`) REFERENCES `Person` (`personId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `Container_ibfk6` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `Container_ibfk_1` FOREIGN KEY (`dewarId`) REFERENCES `Dewar` (`dewarId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Crystal`
--
ALTER TABLE `Crystal`
  ADD CONSTRAINT `Crystal_ibfk_1` FOREIGN KEY (`proteinId`) REFERENCES `Protein` (`proteinId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Crystal_ibfk_2` FOREIGN KEY (`diffractionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Crystal_has_UUID`
--
ALTER TABLE `Crystal_has_UUID`
  ADD CONSTRAINT `ibfk_1` FOREIGN KEY (`crystalId`) REFERENCES `Crystal` (`crystalId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `DataCollection`
--
ALTER TABLE `DataCollection`
  ADD CONSTRAINT `DataCollection_ibfk_1` FOREIGN KEY (`strategySubWedgeOrigId`) REFERENCES `ScreeningStrategySubWedge` (`screeningStrategySubWedgeId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `DataCollection_ibfk_2` FOREIGN KEY (`detectorId`) REFERENCES `Detector` (`detectorId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `DataCollection_ibfk_3` FOREIGN KEY (`dataCollectionGroupId`) REFERENCES `DataCollectionGroup` (`dataCollectionGroupId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `DataCollection_ibfk_8` FOREIGN KEY (`blSubSampleId`) REFERENCES `BLSubSample` (`blSubSampleId`);

--
-- Constraints for table `DataCollectionGroup`
--
ALTER TABLE `DataCollectionGroup`
  ADD CONSTRAINT `DataCollectionGroup_ibfk_1` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `DataCollectionGroup_ibfk_2` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `DataCollectionGroup_ibfk_3` FOREIGN KEY (`workflowId`) REFERENCES `Workflow` (`workflowId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Dewar`
--
ALTER TABLE `Dewar`
  ADD CONSTRAINT `Dewar_ibfk_1` FOREIGN KEY (`shippingId`) REFERENCES `Shipping` (`shippingId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Dewar_ibfk_2` FOREIGN KEY (`firstExperimentId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `DewarTransportHistory`
--
ALTER TABLE `DewarTransportHistory`
  ADD CONSTRAINT `DewarTransportHistory_ibfk_1` FOREIGN KEY (`dewarId`) REFERENCES `Dewar` (`dewarId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `EnergyScan`
--
ALTER TABLE `EnergyScan`
  ADD CONSTRAINT `ES_ibfk_3` FOREIGN KEY (`blSubSampleId`) REFERENCES `BLSubSample` (`blSubSampleId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `ES_ibfk_1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ES_ibfk_2` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Experiment`
--
ALTER TABLE `Experiment`
  ADD CONSTRAINT `fk_Experiment_To_session` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `ExperimentKindDetails`
--
ALTER TABLE `ExperimentKindDetails`
  ADD CONSTRAINT `EKD_ibfk_1` FOREIGN KEY (`diffractionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `FitStructureToExperimentalData`
--
ALTER TABLE `FitStructureToExperimentalData`
  ADD CONSTRAINT `fk_FitStructureToExperimentalData_1` FOREIGN KEY (`structureId`) REFERENCES `Structure` (`structureId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_FitStructureToExperimentalData_2` FOREIGN KEY (`workflowId`) REFERENCES `Workflow` (`workflowId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_FitStructureToExperimentalData_3` FOREIGN KEY (`subtractionId`) REFERENCES `Subtraction` (`subtractionId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `FrameSet`
--
ALTER TABLE `FrameSet`
  ADD CONSTRAINT `FrameSetToFrameList` FOREIGN KEY (`frameListId`) REFERENCES `FrameList` (`frameListId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FramesetToRun` FOREIGN KEY (`runId`) REFERENCES `Run` (`runId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `FrameToList`
--
ALTER TABLE `FrameToList`
  ADD CONSTRAINT `FrameToLisToFrameList` FOREIGN KEY (`frameListId`) REFERENCES `FrameList` (`frameListId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FrameToListToFrame` FOREIGN KEY (`frameId`) REFERENCES `Frame` (`frameId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `GridInfo`
--
ALTER TABLE `GridInfo`
  ADD CONSTRAINT `GridInfo_ibfk_1` FOREIGN KEY (`workflowMeshId`) REFERENCES `WorkflowMesh` (`workflowMeshId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `GridInfo_ibfk_2` FOREIGN KEY (`dataCollectionGroupId`) REFERENCES `DataCollectionGroup` (`dataCollectionGroupId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Image`
--
ALTER TABLE `Image`
  ADD CONSTRAINT `Image_ibfk_1` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Image_ibfk_3` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `ImageQualityIndicators`
--
ALTER TABLE `ImageQualityIndicators`
  ADD CONSTRAINT `AutoProcProgramFk1` FOREIGN KEY (`autoProcProgramId`) REFERENCES `AutoProcProgram` (`autoProcProgramId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Instruction`
--
ALTER TABLE `Instruction`
  ADD CONSTRAINT `InstructionToInstructionSet` FOREIGN KEY (`instructionSetId`) REFERENCES `InstructionSet` (`instructionSetId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `LabContact`
--
ALTER TABLE `LabContact`
  ADD CONSTRAINT `LabContact_ibfk_1` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`),
  ADD CONSTRAINT `LabContact_ibfk_2` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`);

--
-- Constraints for table `Macromolecule`
--
ALTER TABLE `Macromolecule`
  ADD CONSTRAINT `MacromoleculeToSafetyLevel` FOREIGN KEY (`safetyLevelId`) REFERENCES `SafetyLevel` (`safetyLevelId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `MacromoleculeRegion`
--
ALTER TABLE `MacromoleculeRegion`
  ADD CONSTRAINT `MacromoleculeRegionInformationToMacromolecule` FOREIGN KEY (`macromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Measurement`
--
ALTER TABLE `Measurement`
  ADD CONSTRAINT `MeasurementToRun` FOREIGN KEY (`runId`) REFERENCES `Run` (`runId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `SpecimenToSamplePlateWell` FOREIGN KEY (`specimenId`) REFERENCES `Specimen` (`specimenId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `MeasurementToDataCollection`
--
ALTER TABLE `MeasurementToDataCollection`
  ADD CONSTRAINT `MeasurementToDataCollectionToDataCollection` FOREIGN KEY (`dataCollectionId`) REFERENCES `SaxsDataCollection` (`dataCollectionId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `MeasurementToDataCollectionToMeasurement` FOREIGN KEY (`measurementId`) REFERENCES `Measurement` (`measurementId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Merge`
--
ALTER TABLE `Merge`
  ADD CONSTRAINT `MergeToListOfFrames` FOREIGN KEY (`frameListId`) REFERENCES `FrameList` (`frameListId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `MergeToMeasurement` FOREIGN KEY (`measurementId`) REFERENCES `Measurement` (`measurementId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `MixtureToStructure`
--
ALTER TABLE `MixtureToStructure`
  ADD CONSTRAINT `fk_FitToStructure_1` FOREIGN KEY (`structureId`) REFERENCES `Structure` (`structureId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_FitToStructure_2` FOREIGN KEY (`mixtureId`) REFERENCES `FitStructureToExperimentalData` (`fitStructureToExperimentalDataId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `ModelBuilding`
--
ALTER TABLE `ModelBuilding`
  ADD CONSTRAINT `ModelBuilding_phasingAnalysisfk_1` FOREIGN KEY (`phasingAnalysisId`) REFERENCES `PhasingAnalysis` (`phasingAnalysisId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ModelBuilding_phasingProgramRunfk_1` FOREIGN KEY (`phasingProgramRunId`) REFERENCES `PhasingProgramRun` (`phasingProgramRunId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ModelBuilding_spaceGroupfk_1` FOREIGN KEY (`spaceGroupId`) REFERENCES `SpaceGroup` (`spaceGroupId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ModelToList`
--
ALTER TABLE `ModelToList`
  ADD CONSTRAINT `ModelToListToList` FOREIGN KEY (`modelListId`) REFERENCES `ModelList` (`modelListId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `ModelToListToModel` FOREIGN KEY (`modelId`) REFERENCES `Model` (`modelId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `MXMRRun`
--
ALTER TABLE `MXMRRun`
  ADD CONSTRAINT `mxMRRun_FK1` FOREIGN KEY (`autoProcScalingId`) REFERENCES `AutoProcScaling` (`autoProcScalingId`);

--
-- Constraints for table `MXMRRunBlob`
--
ALTER TABLE `MXMRRunBlob`
  ADD CONSTRAINT `mxMRRunBlob_FK1` FOREIGN KEY (`mxMRRunId`) REFERENCES `MXMRRun` (`mxMRRunId`);

--
-- Constraints for table `Person`
--
ALTER TABLE `Person`
  ADD CONSTRAINT `Person_ibfk_1` FOREIGN KEY (`laboratoryId`) REFERENCES `Laboratory` (`laboratoryId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Phasing`
--
ALTER TABLE `Phasing`
  ADD CONSTRAINT `Phasing_phasingAnalysisfk_1` FOREIGN KEY (`phasingAnalysisId`) REFERENCES `PhasingAnalysis` (`phasingAnalysisId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Phasing_phasingProgramRunfk_1` FOREIGN KEY (`phasingProgramRunId`) REFERENCES `PhasingProgramRun` (`phasingProgramRunId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Phasing_spaceGroupfk_1` FOREIGN KEY (`spaceGroupId`) REFERENCES `SpaceGroup` (`spaceGroupId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `PhasingProgramAttachment`
--
ALTER TABLE `PhasingProgramAttachment`
  ADD CONSTRAINT `Phasing_phasingProgramAttachmentfk_1` FOREIGN KEY (`phasingProgramRunId`) REFERENCES `PhasingProgramRun` (`phasingProgramRunId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `PhasingStatistics`
--
ALTER TABLE `PhasingStatistics`
  ADD CONSTRAINT `fk_PhasingStatistics_phasingStep` FOREIGN KEY (`phasingStepId`) REFERENCES `PhasingStep` (`phasingStepId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `PhasingStatistics_phasingHasScalingfk_1` FOREIGN KEY (`phasingHasScalingId1`) REFERENCES `Phasing_has_Scaling` (`phasingHasScalingId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `PhasingStatistics_phasingHasScalingfk_2` FOREIGN KEY (`phasingHasScalingId2`) REFERENCES `Phasing_has_Scaling` (`phasingHasScalingId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `PhasingStep`
--
ALTER TABLE `PhasingStep`
  ADD CONSTRAINT `FK_autoprocScaling` FOREIGN KEY (`autoProcScalingId`) REFERENCES `AutoProcScaling` (`autoProcScalingId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_program` FOREIGN KEY (`programRunId`) REFERENCES `PhasingProgramRun` (`phasingProgramRunId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_spacegroup` FOREIGN KEY (`spaceGroupId`) REFERENCES `SpaceGroup` (`spaceGroupId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Phasing_has_Scaling`
--
ALTER TABLE `Phasing_has_Scaling`
  ADD CONSTRAINT `PhasingHasScaling_autoProcScalingfk_1` FOREIGN KEY (`autoProcScalingId`) REFERENCES `AutoProcScaling` (`autoProcScalingId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `PhasingHasScaling_phasingAnalysisfk_1` FOREIGN KEY (`phasingAnalysisId`) REFERENCES `PhasingAnalysis` (`phasingAnalysisId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Position`
--
ALTER TABLE `Position`
  ADD CONSTRAINT `Position_relativePositionfk_1` FOREIGN KEY (`relativePositionId`) REFERENCES `Position` (`positionId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `PreparePhasingData`
--
ALTER TABLE `PreparePhasingData`
  ADD CONSTRAINT `PreparePhasingData_phasingAnalysisfk_1` FOREIGN KEY (`phasingAnalysisId`) REFERENCES `PhasingAnalysis` (`phasingAnalysisId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `PreparePhasingData_phasingProgramRunfk_1` FOREIGN KEY (`phasingProgramRunId`) REFERENCES `PhasingProgramRun` (`phasingProgramRunId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `PreparePhasingData_spaceGroupfk_1` FOREIGN KEY (`spaceGroupId`) REFERENCES `SpaceGroup` (`spaceGroupId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Proposal`
--
ALTER TABLE `Proposal`
  ADD CONSTRAINT `Proposal_ibfk_1` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ProposalHasPerson`
--
ALTER TABLE `ProposalHasPerson`
  ADD CONSTRAINT `fk_ProposalHasPerson_Personal` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_ProposalHasPerson_Proposal` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Protein`
--
ALTER TABLE `Protein`
  ADD CONSTRAINT `Protein_ibfk_1` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Protein_has_PDB`
--
ALTER TABLE `Protein_has_PDB`
  ADD CONSTRAINT `Protein_Has_PDB_fk1` FOREIGN KEY (`proteinid`) REFERENCES `Protein` (`proteinId`),
  ADD CONSTRAINT `Protein_Has_PDB_fk2` FOREIGN KEY (`pdbid`) REFERENCES `PDB` (`pdbId`);

--
-- Constraints for table `SamplePlate`
--
ALTER TABLE `SamplePlate`
  ADD CONSTRAINT `PlateToPtateGroup` FOREIGN KEY (`plateGroupId`) REFERENCES `PlateGroup` (`plateGroupId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `SamplePlateToExperiment` FOREIGN KEY (`experimentId`) REFERENCES `Experiment` (`experimentId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `SamplePlateToInstructionSet` FOREIGN KEY (`instructionSetId`) REFERENCES `InstructionSet` (`instructionSetId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `SamplePlateToType` FOREIGN KEY (`plateTypeId`) REFERENCES `PlateType` (`plateTypeId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `SamplePlatePosition`
--
ALTER TABLE `SamplePlatePosition`
  ADD CONSTRAINT `PlatePositionToPlate` FOREIGN KEY (`samplePlateId`) REFERENCES `SamplePlate` (`samplePlateId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `SaxsDataCollection`
--
ALTER TABLE `SaxsDataCollection`
  ADD CONSTRAINT `SaxsDataCollectionToExperiment` FOREIGN KEY (`experimentId`) REFERENCES `Experiment` (`experimentId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Screening`
--
ALTER TABLE `Screening`
  ADD CONSTRAINT `Screening_ibfk_1` FOREIGN KEY (`dataCollectionGroupId`) REFERENCES `DataCollectionGroup` (`dataCollectionGroupId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `ScreeningInput`
--
ALTER TABLE `ScreeningInput`
  ADD CONSTRAINT `ScreeningInput_ibfk_1` FOREIGN KEY (`screeningId`) REFERENCES `Screening` (`screeningId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ScreeningOutput`
--
ALTER TABLE `ScreeningOutput`
  ADD CONSTRAINT `ScreeningOutput_ibfk_1` FOREIGN KEY (`screeningId`) REFERENCES `Screening` (`screeningId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ScreeningOutputLattice`
--
ALTER TABLE `ScreeningOutputLattice`
  ADD CONSTRAINT `ScreeningOutputLattice_ibfk_1` FOREIGN KEY (`screeningOutputId`) REFERENCES `ScreeningOutput` (`screeningOutputId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ScreeningRank`
--
ALTER TABLE `ScreeningRank`
  ADD CONSTRAINT `ScreeningRank_ibfk_1` FOREIGN KEY (`screeningId`) REFERENCES `Screening` (`screeningId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ScreeningRank_ibfk_2` FOREIGN KEY (`screeningRankSetId`) REFERENCES `ScreeningRankSet` (`screeningRankSetId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ScreeningStrategy`
--
ALTER TABLE `ScreeningStrategy`
  ADD CONSTRAINT `ScreeningStrategy_ibfk_1` FOREIGN KEY (`screeningOutputId`) REFERENCES `ScreeningOutput` (`screeningOutputId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ScreeningStrategySubWedge`
--
ALTER TABLE `ScreeningStrategySubWedge`
  ADD CONSTRAINT `ScreeningStrategySubWedge_FK1` FOREIGN KEY (`screeningStrategyWedgeId`) REFERENCES `ScreeningStrategyWedge` (`screeningStrategyWedgeId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ScreeningStrategyWedge`
--
ALTER TABLE `ScreeningStrategyWedge`
  ADD CONSTRAINT `ScreeningStrategyWedge_IBFK_1` FOREIGN KEY (`screeningStrategyId`) REFERENCES `ScreeningStrategy` (`screeningStrategyId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Session_has_Person`
--
ALTER TABLE `Session_has_Person`
  ADD CONSTRAINT `Session_has_Person_ibfk_1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Session_has_Person_ibfk_2` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Shipping`
--
ALTER TABLE `Shipping`
  ADD CONSTRAINT `Shipping_ibfk_1` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Shipping_ibfk_2` FOREIGN KEY (`sendingLabContactId`) REFERENCES `LabContact` (`labContactId`),
  ADD CONSTRAINT `Shipping_ibfk_3` FOREIGN KEY (`returnLabContactId`) REFERENCES `LabContact` (`labContactId`);

--
-- Constraints for table `ShippingHasSession`
--
ALTER TABLE `ShippingHasSession`
  ADD CONSTRAINT `ShippingHasSession_ibfk_1` FOREIGN KEY (`shippingId`) REFERENCES `Shipping` (`shippingId`),
  ADD CONSTRAINT `ShippingHasSession_ibfk_2` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`);

--
-- Constraints for table `SpaceGroup`
--
ALTER TABLE `SpaceGroup`
  ADD CONSTRAINT `SpaceGroup_ibfk_1` FOREIGN KEY (`geometryClassnameId`) REFERENCES `GeometryClassname` (`geometryClassnameId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Specimen`
--
ALTER TABLE `Specimen`
  ADD CONSTRAINT `SamplePlateWellToBuffer` FOREIGN KEY (`bufferId`) REFERENCES `Buffer` (`bufferId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `SamplePlateWellToExperiment` FOREIGN KEY (`experimentId`) REFERENCES `Experiment` (`experimentId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `SamplePlateWellToMacromolecule` FOREIGN KEY (`macromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `SamplePlateWellToSafetyLevel` FOREIGN KEY (`safetyLevelId`) REFERENCES `SafetyLevel` (`safetyLevelId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `SamplePlateWellToSamplePlatePosition` FOREIGN KEY (`samplePlatePositionId`) REFERENCES `SamplePlatePosition` (`samplePlatePositionId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `SampleToStockSolution` FOREIGN KEY (`stockSolutionId`) REFERENCES `StockSolution` (`stockSolutionId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `StockSolution`
--
ALTER TABLE `StockSolution`
  ADD CONSTRAINT `StockSolutionToBuffer` FOREIGN KEY (`bufferId`) REFERENCES `Buffer` (`bufferId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `StockSolutionToInstructionSet` FOREIGN KEY (`instructionSetId`) REFERENCES `InstructionSet` (`instructionSetId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `StockSolutionToMacromolecule` FOREIGN KEY (`macromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Stoichiometry`
--
ALTER TABLE `Stoichiometry`
  ADD CONSTRAINT `StoichiometryToHost` FOREIGN KEY (`hostMacromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `StoichiometryToMacromolecule` FOREIGN KEY (`macromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Structure`
--
ALTER TABLE `Structure`
  ADD CONSTRAINT `StructureToMacromolecule` FOREIGN KEY (`macromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `SubstructureDetermination`
--
ALTER TABLE `SubstructureDetermination`
  ADD CONSTRAINT `SubstructureDetermination_phasingAnalysisfk_1` FOREIGN KEY (`phasingAnalysisId`) REFERENCES `PhasingAnalysis` (`phasingAnalysisId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `SubstructureDetermination_phasingProgramRunfk_1` FOREIGN KEY (`phasingProgramRunId`) REFERENCES `PhasingProgramRun` (`phasingProgramRunId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `SubstructureDetermination_spaceGroupfk_1` FOREIGN KEY (`spaceGroupId`) REFERENCES `SpaceGroup` (`spaceGroupId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Subtraction`
--
ALTER TABLE `Subtraction`
  ADD CONSTRAINT `EdnaAnalysisToMeasurement0` FOREIGN KEY (`dataCollectionId`) REFERENCES `SaxsDataCollection` (`dataCollectionId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Subtraction_1` FOREIGN KEY (`sampleOneDimensionalFiles`) REFERENCES `FrameList` (`frameListId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Subtraction_2` FOREIGN KEY (`bufferOnedimensionalFiles`) REFERENCES `FrameList` (`frameListId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `SubtractionToAbInitioModel`
--
ALTER TABLE `SubtractionToAbInitioModel`
  ADD CONSTRAINT `substractionToAbInitioModelToAbinitioModel` FOREIGN KEY (`abInitioId`) REFERENCES `AbInitioModel` (`abInitioModelId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `substractionToSubstraction` FOREIGN KEY (`subtractionId`) REFERENCES `Subtraction` (`subtractionId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `UntrustedRegion`
--
ALTER TABLE `UntrustedRegion`
  ADD CONSTRAINT `UntrustedRegion_ibfk_1` FOREIGN KEY (`detectorId`) REFERENCES `Detector` (`detectorId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `WorkflowDehydration`
--
ALTER TABLE `WorkflowDehydration`
  ADD CONSTRAINT `WorkflowDehydration_workflowfk_1` FOREIGN KEY (`workflowId`) REFERENCES `Workflow` (`workflowId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `WorkflowMesh`
--
ALTER TABLE `WorkflowMesh`
  ADD CONSTRAINT `WorkflowMesh_ibfk_2` FOREIGN KEY (`bestImageId`) REFERENCES `Image` (`imageId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `WorkflowMesh_workflowfk_1` FOREIGN KEY (`workflowId`) REFERENCES `Workflow` (`workflowId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `WorkflowStep`
--
ALTER TABLE `WorkflowStep`
  ADD CONSTRAINT `step_to_workflow_fk` FOREIGN KEY (`workflowId`) REFERENCES `Workflow` (`workflowId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `XFEFluorescenceSpectrum`
--
ALTER TABLE `XFEFluorescenceSpectrum`
  ADD CONSTRAINT `XFE_ibfk_1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `XFE_ibfk_2` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `XFE_ibfk_3` FOREIGN KEY (`blSubSampleId`) REFERENCES `BLSubSample` (`blSubSampleId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
