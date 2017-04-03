-- MySQL dump 10.13  Distrib 5.5.53, for debian-linux-gnu (x86_64)
--
-- Host: pydevserv    Database: pydb
-- ------------------------------------------------------
-- Server version	5.5.40-0+wheezy1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `AbInitioModel`
--

DROP TABLE IF EXISTS `AbInitioModel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AbInitioModel` (
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
  KEY `SahpeDeterminationToAbiniti` (`shapeDeterminationModelId`),
  CONSTRAINT `AbInitioModelToModelList` FOREIGN KEY (`modelListId`) REFERENCES `ModelList` (`modelListId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `AbInitioModelToRapid` FOREIGN KEY (`rapidShapeDeterminationModelId`) REFERENCES `Model` (`modelId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `AverageToModel` FOREIGN KEY (`averagedModelId`) REFERENCES `Model` (`modelId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `SahpeDeterminationToAbiniti` FOREIGN KEY (`shapeDeterminationModelId`) REFERENCES `Model` (`modelId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=30724 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Additive`
--

DROP TABLE IF EXISTS `Additive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Additive` (
  `additiveId` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `additiveType` varchar(45) DEFAULT NULL,
  `comments` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`additiveId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AdminActivity`
--

DROP TABLE IF EXISTS `AdminActivity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AdminActivity` (
  `adminActivityId` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL DEFAULT '',
  `action` varchar(45) DEFAULT NULL,
  `comments` varchar(100) DEFAULT NULL,
  `dateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`adminActivityId`),
  UNIQUE KEY `username` (`username`),
  KEY `AdminActivity_FKAction` (`action`)
) ENGINE=InnoDB AUTO_INCREMENT=1972 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AdminVar`
--

DROP TABLE IF EXISTS `AdminVar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AdminVar` (
  `varId` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  `value` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`varId`),
  KEY `AdminVar_FKIndexName` (`name`),
  KEY `AdminVar_FKIndexValue` (`value`(767))
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1 COMMENT='ISPyB administration values';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Assembly`
--

DROP TABLE IF EXISTS `Assembly`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Assembly` (
  `assemblyId` int(10) NOT NULL AUTO_INCREMENT,
  `macromoleculeId` int(10) NOT NULL,
  `creationDate` datetime DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`assemblyId`),
  KEY `AssemblyToMacromolecule` (`macromoleculeId`),
  CONSTRAINT `AssemblyToMacromolecule` FOREIGN KEY (`macromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AssemblyHasMacromolecule`
--

DROP TABLE IF EXISTS `AssemblyHasMacromolecule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AssemblyHasMacromolecule` (
  `AssemblyHasMacromoleculeId` int(10) NOT NULL AUTO_INCREMENT,
  `assemblyId` int(10) NOT NULL,
  `macromoleculeId` int(10) NOT NULL,
  PRIMARY KEY (`AssemblyHasMacromoleculeId`),
  KEY `AssemblyHasMacromoleculeToAssembly` (`assemblyId`),
  KEY `AssemblyHasMacromoleculeToAssemblyRegion` (`macromoleculeId`),
  CONSTRAINT `AssemblyHasMacromoleculeToAssembly` FOREIGN KEY (`assemblyId`) REFERENCES `Assembly` (`assemblyId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `AssemblyHasMacromoleculeToAssemblyRegion` FOREIGN KEY (`macromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AssemblyRegion`
--

DROP TABLE IF EXISTS `AssemblyRegion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AssemblyRegion` (
  `assemblyRegionId` int(10) NOT NULL AUTO_INCREMENT,
  `assemblyHasMacromoleculeId` int(10) NOT NULL,
  `assemblyRegionType` varchar(45) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `fromResiduesBases` varchar(45) DEFAULT NULL,
  `toResiduesBases` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`assemblyRegionId`),
  KEY `AssemblyRegionToAssemblyHasMacromolecule` (`assemblyHasMacromoleculeId`),
  CONSTRAINT `AssemblyRegionToAssemblyHasMacromolecule` FOREIGN KEY (`assemblyHasMacromoleculeId`) REFERENCES `AssemblyHasMacromolecule` (`AssemblyHasMacromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AutoProc`
--

DROP TABLE IF EXISTS `AutoProc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AutoProc` (
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
) ENGINE=InnoDB AUTO_INCREMENT=1233805 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AutoProcIntegration`
--

DROP TABLE IF EXISTS `AutoProcIntegration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AutoProcIntegration` (
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
  KEY `AutoProcIntegration_FKIndex1` (`autoProcProgramId`),
  CONSTRAINT `AutoProcIntegration_ibfk_1` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `AutoProcIntegration_ibfk_2` FOREIGN KEY (`autoProcProgramId`) REFERENCES `AutoProcProgram` (`autoProcProgramId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1375257 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AutoProcProgram`
--

DROP TABLE IF EXISTS `AutoProcProgram`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AutoProcProgram` (
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
) ENGINE=InnoDB AUTO_INCREMENT=1240318 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AutoProcProgramAttachment`
--

DROP TABLE IF EXISTS `AutoProcProgramAttachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AutoProcProgramAttachment` (
  `autoProcProgramAttachmentId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `autoProcProgramId` int(10) unsigned NOT NULL COMMENT 'Related autoProcProgram item',
  `fileType` enum('Log','Result','Graph') DEFAULT NULL COMMENT 'Type of file Attachment',
  `fileName` varchar(255) DEFAULT NULL COMMENT 'Attachment filename',
  `filePath` varchar(255) DEFAULT NULL COMMENT 'Attachment filepath to disk storage',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`autoProcProgramAttachmentId`),
  KEY `AutoProcProgramAttachmentIdx1` (`autoProcProgramId`),
  CONSTRAINT `AutoProcProgramAttachmentFk1` FOREIGN KEY (`autoProcProgramId`) REFERENCES `AutoProcProgram` (`autoProcProgramId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13081998 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AutoProcScaling`
--

DROP TABLE IF EXISTS `AutoProcScaling`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AutoProcScaling` (
  `autoProcScalingId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `autoProcId` int(10) unsigned DEFAULT NULL COMMENT 'Related autoProc item (used by foreign key)',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`autoProcScalingId`),
  KEY `AutoProcScalingFk1` (`autoProcId`),
  KEY `AutoProcScalingIdx1` (`autoProcScalingId`,`autoProcId`),
  CONSTRAINT `AutoProcScalingFk1` FOREIGN KEY (`autoProcId`) REFERENCES `AutoProc` (`autoProcId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1233816 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AutoProcScalingStatistics`
--

DROP TABLE IF EXISTS `AutoProcScalingStatistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AutoProcScalingStatistics` (
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
  KEY `AutoProcScalingStatistics_FKindexType` (`scalingStatisticsType`),
  CONSTRAINT `AutoProcScalingStatisticsFk1` FOREIGN KEY (`autoProcScalingId`) REFERENCES `AutoProcScaling` (`autoProcScalingId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3776479 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AutoProcScaling_has_Int`
--

DROP TABLE IF EXISTS `AutoProcScaling_has_Int`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AutoProcScaling_has_Int` (
  `autoProcScaling_has_IntId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `autoProcScalingId` int(10) unsigned DEFAULT NULL COMMENT 'AutoProcScaling item',
  `autoProcIntegrationId` int(10) unsigned NOT NULL COMMENT 'AutoProcIntegration item',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`autoProcScaling_has_IntId`),
  KEY `AutoProcScl_has_IntIdx1` (`autoProcScalingId`),
  KEY `AutoProcScal_has_IntIdx2` (`autoProcIntegrationId`),
  KEY `AutoProcScalingHasInt_FKIndex3` (`autoProcScalingId`,`autoProcIntegrationId`),
  CONSTRAINT `AutoProcScaling_has_IntFk1` FOREIGN KEY (`autoProcScalingId`) REFERENCES `AutoProcScaling` (`autoProcScalingId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `AutoProcScaling_has_IntFk2` FOREIGN KEY (`autoProcIntegrationId`) REFERENCES `AutoProcIntegration` (`autoProcIntegrationId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1233756 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AutoProcStatus`
--

DROP TABLE IF EXISTS `AutoProcStatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AutoProcStatus` (
  `autoProcStatusId` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `autoProcIntegrationId` int(10) unsigned NOT NULL,
  `step` enum('Indexing','Integration','Correction','Scaling','Importing') NOT NULL COMMENT 'autoprocessing step',
  `status` enum('Launched','Successful','Failed') NOT NULL COMMENT 'autoprocessing status',
  `comments` varchar(1024) DEFAULT NULL COMMENT 'comments',
  `bltimeStamp` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`autoProcStatusId`),
  KEY `AutoProcStatus_FKIndex1` (`autoProcIntegrationId`),
  CONSTRAINT `AutoProcStatus_ibfk_1` FOREIGN KEY (`autoProcIntegrationId`) REFERENCES `AutoProcIntegration` (`autoProcIntegrationId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1891658 DEFAULT CHARSET=latin1 COMMENT='AutoProcStatus table is linked to AutoProcIntegration';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BLSample`
--

DROP TABLE IF EXISTS `BLSample`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BLSample` (
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
  PRIMARY KEY (`blSampleId`),
  KEY `BLSample_FKIndex1` (`containerId`),
  KEY `BLSample_FKIndex2` (`crystalId`),
  KEY `BLSample_FKIndex3` (`diffractionPlanId`),
  KEY `crystalId` (`crystalId`,`containerId`),
  KEY `BLSample_Index1` (`name`) USING BTREE,
  KEY `BLSample_FKIndex_Status` (`blSampleStatus`),
  CONSTRAINT `BLSample_ibfk_1` FOREIGN KEY (`containerId`) REFERENCES `Container` (`containerId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `BLSample_ibfk_2` FOREIGN KEY (`crystalId`) REFERENCES `Crystal` (`crystalId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `BLSample_ibfk_3` FOREIGN KEY (`diffractionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=562578 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BLSample_has_EnergyScan`
--

DROP TABLE IF EXISTS `BLSample_has_EnergyScan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BLSample_has_EnergyScan` (
  `blSampleHasEnergyScanId` int(10) NOT NULL AUTO_INCREMENT,
  `blSampleId` int(10) unsigned NOT NULL DEFAULT '0',
  `energyScanId` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`blSampleHasEnergyScanId`),
  KEY `BLSample_has_EnergyScan_FKIndex1` (`blSampleId`),
  KEY `BLSample_has_EnergyScan_FKIndex2` (`energyScanId`),
  CONSTRAINT `BLSample_has_EnergyScan_ibfk_1` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `BLSample_has_EnergyScan_ibfk_2` FOREIGN KEY (`energyScanId`) REFERENCES `EnergyScan` (`energyScanId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=473 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BLSession`
--

DROP TABLE IF EXISTS `BLSession`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BLSession` (
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
  KEY `BLSession_FKIndexOperatorSiteNumber` (`operatorSiteNumber`),
  CONSTRAINT `BLSession_ibfk_1` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `BLSession_ibfk_2` FOREIGN KEY (`beamLineSetupId`) REFERENCES `BeamLineSetup` (`beamLineSetupId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=54661 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BLSubSample`
--

DROP TABLE IF EXISTS `BLSubSample`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BLSubSample` (
  `blSubSampleId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `blSampleId` int(10) unsigned NOT NULL COMMENT 'sample',
  `diffractionPlanId` int(10) unsigned DEFAULT NULL COMMENT 'eventually diffractionPlan',
  `positionId` int(11) unsigned DEFAULT NULL COMMENT 'position of the subsample',
  `position2Id` int(11) unsigned DEFAULT NULL,
  `motorPositionId` int(11) unsigned DEFAULT NULL COMMENT 'motor position',
  `blSubSampleUUID` varchar(45) DEFAULT NULL COMMENT 'uuid of the blsubsample',
  `imgFileName` varchar(255) DEFAULT NULL COMMENT 'image filename',
  `imgFilePath` varchar(1024) DEFAULT NULL COMMENT 'url image',
  `comments` varchar(1024) DEFAULT NULL COMMENT 'comments',
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`blSubSampleId`),
  KEY `BLSubSample_FKIndex1` (`blSampleId`),
  KEY `BLSubSample_FKIndex2` (`diffractionPlanId`),
  KEY `BLSubSample_FKIndex3` (`positionId`),
  KEY `BLSubSample_FKIndex4` (`motorPositionId`),
  KEY `BLSubSample_FKIndex5` (`position2Id`),
  CONSTRAINT `BLSubSample_blSamplefk_1` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `BLSubSample_diffractionPlanfk_1` FOREIGN KEY (`diffractionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `BLSubSample_motorPositionfk_1` FOREIGN KEY (`motorPositionId`) REFERENCES `MotorPosition` (`motorPositionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `BLSubSample_positionfk_1` FOREIGN KEY (`positionId`) REFERENCES `Position` (`positionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `BLSubSample_positionfk_2` FOREIGN KEY (`position2Id`) REFERENCES `Position` (`positionId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BeamLineSetup`
--

DROP TABLE IF EXISTS `BeamLineSetup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BeamLineSetup` (
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
) ENGINE=InnoDB AUTO_INCREMENT=1155429 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Buffer`
--

DROP TABLE IF EXISTS `Buffer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Buffer` (
  `bufferId` int(10) NOT NULL AUTO_INCREMENT,
  `proposalId` int(10) NOT NULL DEFAULT '-1',
  `safetyLevelId` int(10) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `acronym` varchar(45) DEFAULT NULL,
  `pH` varchar(45) DEFAULT NULL,
  `composition` varchar(45) DEFAULT NULL,
  `comments` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`bufferId`),
  KEY `BufferToSafetyLevel` (`safetyLevelId`),
  CONSTRAINT `BufferToSafetyLevel` FOREIGN KEY (`safetyLevelId`) REFERENCES `SafetyLevel` (`safetyLevelId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5394 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BufferHasAdditive`
--

DROP TABLE IF EXISTS `BufferHasAdditive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BufferHasAdditive` (
  `bufferHasAdditiveId` int(10) NOT NULL AUTO_INCREMENT,
  `bufferId` int(10) NOT NULL,
  `additiveId` int(10) NOT NULL,
  `measurementUnitId` int(10) DEFAULT NULL,
  `quantity` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`bufferHasAdditiveId`),
  KEY `BufferHasAdditiveToBuffer` (`bufferId`),
  KEY `BufferHasAdditiveToAdditive` (`additiveId`),
  KEY `BufferHasAdditiveToUnit` (`measurementUnitId`),
  CONSTRAINT `BufferHasAdditiveToAdditive` FOREIGN KEY (`additiveId`) REFERENCES `Additive` (`additiveId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `BufferHasAdditiveToBuffer` FOREIGN KEY (`bufferId`) REFERENCES `Buffer` (`bufferId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `BufferHasAdditiveToUnit` FOREIGN KEY (`measurementUnitId`) REFERENCES `MeasurementUnit` (`measurementUnitId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Container`
--

DROP TABLE IF EXISTS `Container`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Container` (
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
  KEY `Container_FKIndex1` (`dewarId`),
  KEY `Container_FKIndex` (`beamlineLocation`),
  KEY `Container_FKIndexStatus` (`containerStatus`),
  KEY `Container_ibfk6` (`sessionId`),
  KEY `Container_ibfk5` (`ownerId`),
  CONSTRAINT `Container_ibfk5` FOREIGN KEY (`ownerId`) REFERENCES `Person` (`personId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `Container_ibfk6` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `Container_ibfk_1` FOREIGN KEY (`dewarId`) REFERENCES `Dewar` (`dewarId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=335809 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Crystal`
--

DROP TABLE IF EXISTS `Crystal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Crystal` (
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
  KEY `Crystal_FKIndex2` (`diffractionPlanId`),
  CONSTRAINT `Crystal_ibfk_1` FOREIGN KEY (`proteinId`) REFERENCES `Protein` (`proteinId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Crystal_ibfk_2` FOREIGN KEY (`diffractionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=533845 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Crystal_has_UUID`
--

DROP TABLE IF EXISTS `Crystal_has_UUID`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Crystal_has_UUID` (
  `crystal_has_UUID_Id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `crystalId` int(10) unsigned NOT NULL,
  `UUID` varchar(45) DEFAULT NULL,
  `imageURL` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`crystal_has_UUID_Id`),
  KEY `Crystal_has_UUID_FKIndex1` (`crystalId`),
  KEY `Crystal_has_UUID_FKIndex2` (`UUID`),
  CONSTRAINT `ibfk_1` FOREIGN KEY (`crystalId`) REFERENCES `Crystal` (`crystalId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=93158 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DataAcquisition`
--

DROP TABLE IF EXISTS `DataAcquisition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DataAcquisition` (
  `dataAcquisitionId` int(10) NOT NULL AUTO_INCREMENT,
  `sampleCellId` int(10) NOT NULL,
  `framesCount` varchar(45) DEFAULT NULL,
  `energy` varchar(45) DEFAULT NULL,
  `waitTime` varchar(45) DEFAULT NULL,
  `detectorDistance` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`dataAcquisitionId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DataCollection`
--

DROP TABLE IF EXISTS `DataCollection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DataCollection` (
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
  KEY `blSubSampleId` (`blSubSampleId`),
  CONSTRAINT `DataCollection_ibfk_1` FOREIGN KEY (`strategySubWedgeOrigId`) REFERENCES `ScreeningStrategySubWedge` (`screeningStrategySubWedgeId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `DataCollection_ibfk_2` FOREIGN KEY (`detectorId`) REFERENCES `Detector` (`detectorId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `DataCollection_ibfk_3` FOREIGN KEY (`dataCollectionGroupId`) REFERENCES `DataCollectionGroup` (`dataCollectionGroupId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `DataCollection_ibfk_6` FOREIGN KEY (`startPositionId`) REFERENCES `MotorPosition` (`motorPositionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `DataCollection_ibfk_7` FOREIGN KEY (`endPositionId`) REFERENCES `MotorPosition` (`motorPositionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `DataCollection_ibfk_8` FOREIGN KEY (`blSubSampleId`) REFERENCES `BLSubSample` (`blSubSampleId`)
) ENGINE=InnoDB AUTO_INCREMENT=1876635 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DataCollectionGroup`
--

DROP TABLE IF EXISTS `DataCollectionGroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DataCollectionGroup` (
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
  KEY `workflowId` (`workflowId`),
  CONSTRAINT `DataCollectionGroup_ibfk_1` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `DataCollectionGroup_ibfk_2` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `DataCollectionGroup_ibfk_3` FOREIGN KEY (`workflowId`) REFERENCES `Workflow` (`workflowId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1571486 DEFAULT CHARSET=latin1 COMMENT='a dataCollectionGroup is a group of dataCollection for a spe';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DatamatrixInSampleChanger`
--

DROP TABLE IF EXISTS `DatamatrixInSampleChanger`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DatamatrixInSampleChanger` (
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
) ENGINE=InnoDB AUTO_INCREMENT=19089089 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Detector`
--

DROP TABLE IF EXISTS `Detector`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Detector` (
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
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=latin1 COMMENT='Detector table is linked to a dataCollection';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Dewar`
--

DROP TABLE IF EXISTS `Dewar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Dewar` (
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
  KEY `Dewar_FKIndexCode` (`code`),
  CONSTRAINT `Dewar_ibfk_1` FOREIGN KEY (`shippingId`) REFERENCES `Shipping` (`shippingId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Dewar_ibfk_2` FOREIGN KEY (`firstExperimentId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=311011 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DewarLocation`
--

DROP TABLE IF EXISTS `DewarLocation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DewarLocation` (
  `eventId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `dewarNumber` varchar(128) NOT NULL COMMENT 'Dewar number',
  `userId` varchar(128) DEFAULT NULL COMMENT 'User who locates the dewar',
  `dateTime` datetime DEFAULT NULL COMMENT 'Date and time of locatization',
  `locationName` varchar(128) DEFAULT NULL COMMENT 'Location of the dewar',
  `courierName` varchar(128) DEFAULT NULL COMMENT 'Carrier name who''s shipping back the dewar',
  `courierTrackingNumber` varchar(128) DEFAULT NULL COMMENT 'Tracking number of the shippment',
  PRIMARY KEY (`eventId`)
) ENGINE=InnoDB AUTO_INCREMENT=12896 DEFAULT CHARSET=latin1 COMMENT='ISPyB Dewar location table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DewarLocationList`
--

DROP TABLE IF EXISTS `DewarLocationList`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DewarLocationList` (
  `locationId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `locationName` varchar(128) NOT NULL DEFAULT '' COMMENT 'Location',
  PRIMARY KEY (`locationId`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1 COMMENT='List of locations for dewars';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DewarTransportHistory`
--

DROP TABLE IF EXISTS `DewarTransportHistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DewarTransportHistory` (
  `DewarTransportHistoryId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `dewarId` int(10) unsigned DEFAULT NULL,
  `dewarStatus` varchar(45) NOT NULL,
  `storageLocation` varchar(45) NOT NULL,
  `arrivalDate` datetime NOT NULL,
  PRIMARY KEY (`DewarTransportHistoryId`),
  KEY `DewarTransportHistory_FKIndex1` (`dewarId`),
  CONSTRAINT `DewarTransportHistory_ibfk_1` FOREIGN KEY (`dewarId`) REFERENCES `Dewar` (`dewarId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=33323 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DiffractionPlan`
--

DROP TABLE IF EXISTS `DiffractionPlan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DiffractionPlan` (
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
) ENGINE=InnoDB AUTO_INCREMENT=587479 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EnergyScan`
--

DROP TABLE IF EXISTS `EnergyScan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EnergyScan` (
  `energyScanId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sessionId` int(10) unsigned NOT NULL,
  `blSampleId` int(10) DEFAULT NULL,
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
  PRIMARY KEY (`energyScanId`),
  KEY `EnergyScan_FKIndex2` (`sessionId`),
  CONSTRAINT `ES_ibfk_1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13804 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Experiment`
--

DROP TABLE IF EXISTS `Experiment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Experiment` (
  `experimentId` int(11) NOT NULL AUTO_INCREMENT,
  `sessionId` int(10) DEFAULT NULL,
  `proposalId` int(10) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `experimentType` varchar(128) DEFAULT NULL,
  `sourceFilePath` varchar(256) DEFAULT NULL,
  `dataAcquisitionFilePath` varchar(256) DEFAULT NULL COMMENT 'The file path pointing to the data acquisition. Eventually it may be a compressed file with all the files or just the folder',
  `status` varchar(45) DEFAULT NULL,
  `comments` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`experimentId`)
) ENGINE=InnoDB AUTO_INCREMENT=13174 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ExperimentKindDetails`
--

DROP TABLE IF EXISTS `ExperimentKindDetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ExperimentKindDetails` (
  `experimentKindId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `diffractionPlanId` int(10) unsigned NOT NULL,
  `exposureIndex` int(10) unsigned DEFAULT NULL,
  `dataCollectionType` varchar(45) DEFAULT NULL,
  `dataCollectionKind` varchar(45) DEFAULT NULL,
  `wedgeValue` float DEFAULT NULL,
  PRIMARY KEY (`experimentKindId`),
  KEY `ExperimentKindDetails_FKIndex1` (`diffractionPlanId`),
  CONSTRAINT `EKD_ibfk_1` FOREIGN KEY (`diffractionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FitStructureToExperimentalData`
--

DROP TABLE IF EXISTS `FitStructureToExperimentalData`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FitStructureToExperimentalData` (
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
  KEY `fk_FitStructureToExperimentalData_3` (`subtractionId`),
  CONSTRAINT `fk_FitStructureToExperimentalData_1` FOREIGN KEY (`structureId`) REFERENCES `Structure` (`structureId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_FitStructureToExperimentalData_2` FOREIGN KEY (`workflowId`) REFERENCES `Workflow` (`workflowId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_FitStructureToExperimentalData_3` FOREIGN KEY (`subtractionId`) REFERENCES `Subtraction` (`subtractionId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Frame`
--

DROP TABLE IF EXISTS `Frame`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Frame` (
  `frameId` int(10) NOT NULL AUTO_INCREMENT,
  `filePath` varchar(255) DEFAULT NULL,
  `comments` varchar(45) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  PRIMARY KEY (`frameId`),
  KEY `FILE` (`filePath`)
) ENGINE=InnoDB AUTO_INCREMENT=1456931 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FrameList`
--

DROP TABLE IF EXISTS `FrameList`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FrameList` (
  `frameListId` int(10) NOT NULL AUTO_INCREMENT,
  `comments` int(10) DEFAULT NULL,
  PRIMARY KEY (`frameListId`)
) ENGINE=InnoDB AUTO_INCREMENT=158831 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FrameSet`
--

DROP TABLE IF EXISTS `FrameSet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FrameSet` (
  `frameSetId` int(10) NOT NULL AUTO_INCREMENT,
  `runId` int(10) NOT NULL,
  `frameListId` int(10) DEFAULT NULL,
  `detectorId` int(10) DEFAULT NULL,
  `detectorDistance` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`frameSetId`),
  KEY `FramesetToRun` (`runId`),
  KEY `FrameSetToFrameList` (`frameListId`),
  CONSTRAINT `FrameSetToFrameList` FOREIGN KEY (`frameListId`) REFERENCES `FrameList` (`frameListId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FramesetToRun` FOREIGN KEY (`runId`) REFERENCES `Run` (`runId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4209 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FrameToList`
--

DROP TABLE IF EXISTS `FrameToList`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FrameToList` (
  `frameToListId` int(10) NOT NULL AUTO_INCREMENT,
  `frameListId` int(10) NOT NULL,
  `frameId` int(10) NOT NULL,
  PRIMARY KEY (`frameToListId`),
  KEY `FrameToLisToFrameList` (`frameListId`),
  KEY `FrameToListToFrame` (`frameId`),
  CONSTRAINT `FrameToLisToFrameList` FOREIGN KEY (`frameListId`) REFERENCES `FrameList` (`frameListId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FrameToListToFrame` FOREIGN KEY (`frameId`) REFERENCES `Frame` (`frameId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2060770 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `GeometryClassname`
--

DROP TABLE IF EXISTS `GeometryClassname`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GeometryClassname` (
  `geometryClassnameId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `geometryClassname` varchar(45) DEFAULT NULL,
  `geometryOrder` int(2) NOT NULL,
  PRIMARY KEY (`geometryClassnameId`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `GridInfo`
--

DROP TABLE IF EXISTS `GridInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GridInfo` (
  `gridInfoId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `workflowMeshId` int(11) unsigned NOT NULL,
  `xOffset` double DEFAULT NULL,
  `yOffset` double DEFAULT NULL,
  `dx_mm` double DEFAULT NULL,
  `dy_mm` double DEFAULT NULL,
  `steps_x` double DEFAULT NULL,
  `steps_y` double DEFAULT NULL,
  `meshAngle` double DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`gridInfoId`),
  KEY `workflowMeshId` (`workflowMeshId`),
  CONSTRAINT `GridInfo_ibfk_1` FOREIGN KEY (`workflowMeshId`) REFERENCES `WorkflowMesh` (`workflowMeshId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=88656 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Image`
--

DROP TABLE IF EXISTS `Image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Image` (
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
  KEY `motorPositionId` (`motorPositionId`),
  CONSTRAINT `Image_ibfk_1` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Image_ibfk_3` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `Image_ibfk_4` FOREIGN KEY (`motorPositionId`) REFERENCES `MotorPosition` (`motorPositionId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=50175966 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ImageQualityIndicators`
--

DROP TABLE IF EXISTS `ImageQualityIndicators`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ImageQualityIndicators` (
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
  KEY `AutoProcProgramIdx1` (`autoProcProgramId`),
  CONSTRAINT `AutoProcProgramFk1` FOREIGN KEY (`autoProcProgramId`) REFERENCES `AutoProcProgram` (`autoProcProgramId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2602654 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `InputParameterWorkflow`
--

DROP TABLE IF EXISTS `InputParameterWorkflow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `InputParameterWorkflow` (
  `inputParameterId` int(10) NOT NULL AUTO_INCREMENT,
  `workflowId` int(10) NOT NULL,
  `parameterType` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `comments` varchar(2048) DEFAULT NULL,
  PRIMARY KEY (`inputParameterId`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Instruction`
--

DROP TABLE IF EXISTS `Instruction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Instruction` (
  `instructionId` int(10) NOT NULL AUTO_INCREMENT,
  `instructionSetId` int(10) NOT NULL,
  `order` int(11) NOT NULL,
  `comments` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`instructionId`),
  KEY `InstructionToInstructionSet` (`instructionSetId`),
  CONSTRAINT `InstructionToInstructionSet` FOREIGN KEY (`instructionSetId`) REFERENCES `InstructionSet` (`instructionSetId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `InstructionSet`
--

DROP TABLE IF EXISTS `InstructionSet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `InstructionSet` (
  `instructionSetId` int(10) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`instructionSetId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IspybAutoProcAttachment`
--

DROP TABLE IF EXISTS `IspybAutoProcAttachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IspybAutoProcAttachment` (
  `autoProcAttachmentId` int(11) NOT NULL AUTO_INCREMENT,
  `fileName` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `step` enum('XDS','XSCALE','SCALA','SCALEPACK','TRUNCATE','DIMPLE') DEFAULT 'XDS' COMMENT 'step where the file is generated',
  `fileCategory` enum('input','output','log','correction') DEFAULT 'output',
  `hasGraph` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`autoProcAttachmentId`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=latin1 COMMENT='ISPyB autoProcAttachment files values';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IspybCrystalClass`
--

DROP TABLE IF EXISTS `IspybCrystalClass`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IspybCrystalClass` (
  `crystalClassId` int(11) NOT NULL AUTO_INCREMENT,
  `crystalClass_code` varchar(20) NOT NULL,
  `crystalClass_name` varchar(255) NOT NULL,
  PRIMARY KEY (`crystalClassId`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1 COMMENT='ISPyB crystal class values';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IspybReference`
--

DROP TABLE IF EXISTS `IspybReference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IspybReference` (
  `referenceId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `referenceName` varchar(255) DEFAULT NULL COMMENT 'reference name',
  `referenceUrl` varchar(1024) DEFAULT NULL COMMENT 'url of the reference',
  `referenceBibtext` blob COMMENT 'bibtext value of the reference',
  `beamline` enum('All','ID14-4','ID23-1','ID23-2','ID29','ID30A-1','ID30A-2','XRF','AllXRF','Mesh') DEFAULT NULL COMMENT 'beamline involved',
  PRIMARY KEY (`referenceId`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `LabContact`
--

DROP TABLE IF EXISTS `LabContact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LabContact` (
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
  KEY `LabContact_FKIndex1` (`proposalId`),
  CONSTRAINT `LabContact_ibfk_1` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`),
  CONSTRAINT `LabContact_ibfk_2` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`)
) ENGINE=InnoDB AUTO_INCREMENT=240749 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Laboratory`
--

DROP TABLE IF EXISTS `Laboratory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Laboratory` (
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
) ENGINE=InnoDB AUTO_INCREMENT=310920 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Log4Stat`
--

DROP TABLE IF EXISTS `Log4Stat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Log4Stat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `priority` varchar(15) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `msg` varchar(255) DEFAULT NULL,
  `detail` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=19789 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Login`
--

DROP TABLE IF EXISTS `Login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Login` (
  `loginId` int(11) NOT NULL AUTO_INCREMENT,
  `token` varchar(45) NOT NULL,
  `username` varchar(45) NOT NULL,
  `roles` varchar(1024) NOT NULL,
  `expirationTime` datetime NOT NULL,
  PRIMARY KEY (`loginId`),
  KEY `Token` (`token`)
) ENGINE=InnoDB AUTO_INCREMENT=884 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Macromolecule`
--

DROP TABLE IF EXISTS `Macromolecule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Macromolecule` (
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
  KEY `MacromoleculeToSafetyLevel` (`safetyLevelId`),
  CONSTRAINT `MacromoleculeToSafetyLevel` FOREIGN KEY (`safetyLevelId`) REFERENCES `SafetyLevel` (`safetyLevelId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=44641 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MacromoleculeRegion`
--

DROP TABLE IF EXISTS `MacromoleculeRegion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MacromoleculeRegion` (
  `macromoleculeRegionId` int(10) NOT NULL AUTO_INCREMENT,
  `macromoleculeId` int(10) NOT NULL,
  `regionType` varchar(45) DEFAULT NULL,
  `id` varchar(45) DEFAULT NULL,
  `count` varchar(45) DEFAULT NULL,
  `sequence` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`macromoleculeRegionId`),
  KEY `MacromoleculeRegionInformationToMacromolecule` (`macromoleculeId`),
  CONSTRAINT `MacromoleculeRegionInformationToMacromolecule` FOREIGN KEY (`macromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Measurement`
--

DROP TABLE IF EXISTS `Measurement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Measurement` (
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
  KEY `MeasurementToRun` (`runId`),
  CONSTRAINT `MeasurementToRun` FOREIGN KEY (`runId`) REFERENCES `Run` (`runId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `SpecimenToSamplePlateWell` FOREIGN KEY (`specimenId`) REFERENCES `Specimen` (`specimenId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=214099 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MeasurementToDataCollection`
--

DROP TABLE IF EXISTS `MeasurementToDataCollection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MeasurementToDataCollection` (
  `measurementToDataCollectionId` int(10) NOT NULL AUTO_INCREMENT,
  `dataCollectionId` int(10) DEFAULT NULL,
  `measurementId` int(10) DEFAULT NULL,
  `dataCollectionOrder` int(10) DEFAULT NULL,
  PRIMARY KEY (`measurementToDataCollectionId`),
  KEY `MeasurementToDataCollectionToDataCollection` (`dataCollectionId`),
  KEY `MeasurementToDataCollectionToMeasurement` (`measurementId`),
  CONSTRAINT `MeasurementToDataCollectionToDataCollection` FOREIGN KEY (`dataCollectionId`) REFERENCES `SaxsDataCollection` (`dataCollectionId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `MeasurementToDataCollectionToMeasurement` FOREIGN KEY (`measurementId`) REFERENCES `Measurement` (`measurementId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=214099 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MeasurementUnit`
--

DROP TABLE IF EXISTS `MeasurementUnit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MeasurementUnit` (
  `measurementUnitId` int(10) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `unitType` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`measurementUnitId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Merge`
--

DROP TABLE IF EXISTS `Merge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Merge` (
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
  KEY `MergeToListOfFrames` (`frameListId`),
  CONSTRAINT `MergeToListOfFrames` FOREIGN KEY (`frameListId`) REFERENCES `FrameList` (`frameListId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `MergeToMeasurement` FOREIGN KEY (`measurementId`) REFERENCES `Measurement` (`measurementId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=112010 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MixtureToStructure`
--

DROP TABLE IF EXISTS `MixtureToStructure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MixtureToStructure` (
  `fitToStructureId` int(11) NOT NULL AUTO_INCREMENT,
  `structureId` int(10) NOT NULL,
  `mixtureId` int(10) NOT NULL,
  `volumeFraction` varchar(45) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  PRIMARY KEY (`fitToStructureId`),
  KEY `fk_FitToStructure_1` (`structureId`),
  KEY `fk_FitToStructure_2` (`mixtureId`),
  CONSTRAINT `fk_FitToStructure_1` FOREIGN KEY (`structureId`) REFERENCES `Structure` (`structureId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_FitToStructure_2` FOREIGN KEY (`mixtureId`) REFERENCES `FitStructureToExperimentalData` (`fitStructureToExperimentalDataId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Model`
--

DROP TABLE IF EXISTS `Model`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Model` (
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
) ENGINE=InnoDB AUTO_INCREMENT=550001 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ModelBuilding`
--

DROP TABLE IF EXISTS `ModelBuilding`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ModelBuilding` (
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
  KEY `ModelBuilding_FKIndex3` (`spaceGroupId`),
  CONSTRAINT `ModelBuilding_phasingAnalysisfk_1` FOREIGN KEY (`phasingAnalysisId`) REFERENCES `PhasingAnalysis` (`phasingAnalysisId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ModelBuilding_phasingProgramRunfk_1` FOREIGN KEY (`phasingProgramRunId`) REFERENCES `PhasingProgramRun` (`phasingProgramRunId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ModelBuilding_spaceGroupfk_1` FOREIGN KEY (`spaceGroupId`) REFERENCES `SpaceGroup` (`spaceGroupId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ModelList`
--

DROP TABLE IF EXISTS `ModelList`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ModelList` (
  `modelListId` int(10) NOT NULL AUTO_INCREMENT,
  `nsdFilePath` varchar(255) DEFAULT NULL,
  `chi2RgFilePath` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`modelListId`)
) ENGINE=InnoDB AUTO_INCREMENT=33440 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ModelToList`
--

DROP TABLE IF EXISTS `ModelToList`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ModelToList` (
  `modelToListId` int(10) NOT NULL AUTO_INCREMENT,
  `modelId` int(10) NOT NULL,
  `modelListId` int(10) NOT NULL,
  PRIMARY KEY (`modelToListId`),
  KEY `ModelToListToList` (`modelListId`),
  KEY `ModelToListToModel` (`modelId`),
  CONSTRAINT `ModelToListToList` FOREIGN KEY (`modelListId`) REFERENCES `ModelList` (`modelListId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `ModelToListToModel` FOREIGN KEY (`modelId`) REFERENCES `Model` (`modelId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=458009 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MotorPosition`
--

DROP TABLE IF EXISTS `MotorPosition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MotorPosition` (
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
) ENGINE=InnoDB AUTO_INCREMENT=4098632 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Person`
--

DROP TABLE IF EXISTS `Person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Person` (
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
  KEY `siteId` (`siteId`),
  CONSTRAINT `Person_ibfk_1` FOREIGN KEY (`laboratoryId`) REFERENCES `Laboratory` (`laboratoryId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=393529 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Phasing`
--

DROP TABLE IF EXISTS `Phasing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Phasing` (
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
  KEY `Phasing_FKIndex3` (`spaceGroupId`),
  CONSTRAINT `Phasing_phasingAnalysisfk_1` FOREIGN KEY (`phasingAnalysisId`) REFERENCES `PhasingAnalysis` (`phasingAnalysisId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Phasing_phasingProgramRunfk_1` FOREIGN KEY (`phasingProgramRunId`) REFERENCES `PhasingProgramRun` (`phasingProgramRunId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Phasing_spaceGroupfk_1` FOREIGN KEY (`spaceGroupId`) REFERENCES `SpaceGroup` (`spaceGroupId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=106310 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PhasingAnalysis`
--

DROP TABLE IF EXISTS `PhasingAnalysis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PhasingAnalysis` (
  `phasingAnalysisId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`phasingAnalysisId`)
) ENGINE=InnoDB AUTO_INCREMENT=135302 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PhasingProgramAttachment`
--

DROP TABLE IF EXISTS `PhasingProgramAttachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PhasingProgramAttachment` (
  `phasingProgramAttachmentId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `phasingProgramRunId` int(11) unsigned NOT NULL COMMENT 'Related program item',
  `fileType` enum('Map','Logfile','PDB','CSV','INS','RES','TXT') DEFAULT NULL COMMENT 'file type',
  `fileName` varchar(45) DEFAULT NULL COMMENT 'file name',
  `filePath` varchar(255) DEFAULT NULL COMMENT 'file path',
  `recordTimeStamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`phasingProgramAttachmentId`),
  KEY `PhasingProgramAttachment_FKIndex1` (`phasingProgramRunId`),
  CONSTRAINT `Phasing_phasingProgramAttachmentfk_1` FOREIGN KEY (`phasingProgramRunId`) REFERENCES `PhasingProgramRun` (`phasingProgramRunId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=117020 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PhasingProgramRun`
--

DROP TABLE IF EXISTS `PhasingProgramRun`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PhasingProgramRun` (
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
) ENGINE=InnoDB AUTO_INCREMENT=260180 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PhasingStatistics`
--

DROP TABLE IF EXISTS `PhasingStatistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PhasingStatistics` (
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
  KEY `fk_PhasingStatistics_phasingStep_idx` (`phasingStepId`),
  CONSTRAINT `fk_PhasingStatistics_phasingStep` FOREIGN KEY (`phasingStepId`) REFERENCES `PhasingStep` (`phasingStepId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `PhasingStatistics_phasingHasScalingfk_1` FOREIGN KEY (`phasingHasScalingId1`) REFERENCES `Phasing_has_Scaling` (`phasingHasScalingId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PhasingStatistics_phasingHasScalingfk_2` FOREIGN KEY (`phasingHasScalingId2`) REFERENCES `Phasing_has_Scaling` (`phasingHasScalingId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=534238 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PhasingStep`
--

DROP TABLE IF EXISTS `PhasingStep`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PhasingStep` (
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
  KEY `FK_phasingAnalysis_id` (`phasingAnalysisId`),
  CONSTRAINT `FK_autoprocScaling` FOREIGN KEY (`autoProcScalingId`) REFERENCES `AutoProcScaling` (`autoProcScalingId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_program` FOREIGN KEY (`programRunId`) REFERENCES `PhasingProgramRun` (`phasingProgramRunId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_spacegroup` FOREIGN KEY (`spaceGroupId`) REFERENCES `SpaceGroup` (`spaceGroupId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=124877 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Phasing_has_Scaling`
--

DROP TABLE IF EXISTS `Phasing_has_Scaling`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Phasing_has_Scaling` (
  `phasingHasScalingId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `phasingAnalysisId` int(11) unsigned NOT NULL COMMENT 'Related phasing analysis item',
  `autoProcScalingId` int(10) unsigned NOT NULL COMMENT 'Related autoProcScaling item',
  `datasetNumber` int(11) DEFAULT NULL COMMENT 'serial number of the dataset and always reserve 0 for the reference',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`phasingHasScalingId`),
  KEY `PhasingHasScaling_FKIndex1` (`phasingAnalysisId`),
  KEY `PhasingHasScaling_FKIndex2` (`autoProcScalingId`),
  CONSTRAINT `PhasingHasScaling_autoProcScalingfk_1` FOREIGN KEY (`autoProcScalingId`) REFERENCES `AutoProcScaling` (`autoProcScalingId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PhasingHasScaling_phasingAnalysisfk_1` FOREIGN KEY (`phasingAnalysisId`) REFERENCES `PhasingAnalysis` (`phasingAnalysisId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=135302 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PlateGroup`
--

DROP TABLE IF EXISTS `PlateGroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PlateGroup` (
  `plateGroupId` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `storageTemperature` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`plateGroupId`)
) ENGINE=InnoDB AUTO_INCREMENT=10218 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PlateType`
--

DROP TABLE IF EXISTS `PlateType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PlateType` (
  `PlateTypeId` int(10) NOT NULL AUTO_INCREMENT,
  `experimentId` int(10) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  `shape` varchar(45) DEFAULT NULL,
  `rowCount` int(11) DEFAULT NULL,
  `columnCount` int(11) DEFAULT NULL,
  PRIMARY KEY (`PlateTypeId`),
  KEY `PlateTypeToExperiment` (`experimentId`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Position`
--

DROP TABLE IF EXISTS `Position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Position` (
  `positionId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `relativePositionId` int(11) unsigned DEFAULT NULL COMMENT 'relative position, null otherwise',
  `posX` double DEFAULT NULL,
  `posY` double DEFAULT NULL,
  `posZ` double DEFAULT NULL,
  `scale` double DEFAULT NULL,
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`positionId`),
  KEY `Position_FKIndex1` (`relativePositionId`),
  CONSTRAINT `Position_relativePositionfk_1` FOREIGN KEY (`relativePositionId`) REFERENCES `Position` (`positionId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PreparePhasingData`
--

DROP TABLE IF EXISTS `PreparePhasingData`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PreparePhasingData` (
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
  KEY `PreparePhasingData_FKIndex3` (`spaceGroupId`),
  CONSTRAINT `PreparePhasingData_phasingAnalysisfk_1` FOREIGN KEY (`phasingAnalysisId`) REFERENCES `PhasingAnalysis` (`phasingAnalysisId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PreparePhasingData_phasingProgramRunfk_1` FOREIGN KEY (`phasingProgramRunId`) REFERENCES `PhasingProgramRun` (`phasingProgramRunId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PreparePhasingData_spaceGroupfk_1` FOREIGN KEY (`spaceGroupId`) REFERENCES `SpaceGroup` (`spaceGroupId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14499 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Proposal`
--

DROP TABLE IF EXISTS `Proposal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Proposal` (
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
  KEY `Proposal_FKIndexCodeNumber` (`proposalCode`,`proposalNumber`),
  CONSTRAINT `Proposal_ibfk_1` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7864 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ProposalHasPerson`
--

DROP TABLE IF EXISTS `ProposalHasPerson`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ProposalHasPerson` (
  `proposalHasPersonId` int(10) unsigned NOT NULL,
  `proposalId` int(10) unsigned NOT NULL,
  `personId` int(10) unsigned NOT NULL,
  PRIMARY KEY (`proposalHasPersonId`),
  KEY `fk_ProposalHasPerson_Proposal` (`proposalId`),
  KEY `fk_ProposalHasPerson_Personal` (`personId`),
  CONSTRAINT `fk_ProposalHasPerson_Personal` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ProposalHasPerson_Proposal` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Protein`
--

DROP TABLE IF EXISTS `Protein`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Protein` (
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
  KEY `Protein_Index2` (`acronym`) USING BTREE,
  CONSTRAINT `Protein_ibfk_1` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=366595 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RigidBodyModeling`
--

DROP TABLE IF EXISTS `RigidBodyModeling`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RigidBodyModeling` (
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
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Run`
--

DROP TABLE IF EXISTS `Run`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Run` (
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
) ENGINE=InnoDB AUTO_INCREMENT=99848 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SafetyLevel`
--

DROP TABLE IF EXISTS `SafetyLevel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SafetyLevel` (
  `safetyLevelId` int(10) NOT NULL AUTO_INCREMENT,
  `code` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`safetyLevelId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SamplePlate`
--

DROP TABLE IF EXISTS `SamplePlate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SamplePlate` (
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
  KEY `SamplePlateToInstructionSet` (`instructionSetId`),
  CONSTRAINT `PlateToPtateGroup` FOREIGN KEY (`plateGroupId`) REFERENCES `PlateGroup` (`plateGroupId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `SamplePlateToExperiment` FOREIGN KEY (`experimentId`) REFERENCES `Experiment` (`experimentId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `SamplePlateToInstructionSet` FOREIGN KEY (`instructionSetId`) REFERENCES `InstructionSet` (`instructionSetId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `SamplePlateToType` FOREIGN KEY (`plateTypeId`) REFERENCES `PlateType` (`plateTypeId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=30652 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SamplePlatePosition`
--

DROP TABLE IF EXISTS `SamplePlatePosition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SamplePlatePosition` (
  `samplePlatePositionId` int(10) NOT NULL AUTO_INCREMENT,
  `samplePlateId` int(10) NOT NULL,
  `rowNumber` int(11) DEFAULT NULL,
  `columnNumber` int(11) DEFAULT NULL,
  `volume` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`samplePlatePositionId`),
  KEY `PlatePositionToPlate` (`samplePlateId`),
  CONSTRAINT `PlatePositionToPlate` FOREIGN KEY (`samplePlateId`) REFERENCES `SamplePlate` (`samplePlateId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=85915 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SaxsDataCollection`
--

DROP TABLE IF EXISTS `SaxsDataCollection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SaxsDataCollection` (
  `dataCollectionId` int(10) NOT NULL AUTO_INCREMENT,
  `experimentId` int(10) NOT NULL,
  `comments` varchar(5120) DEFAULT NULL,
  PRIMARY KEY (`dataCollectionId`),
  KEY `SaxsDataCollectionToExperiment` (`experimentId`),
  CONSTRAINT `SaxsDataCollectionToExperiment` FOREIGN KEY (`experimentId`) REFERENCES `Experiment` (`experimentId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=71367 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Screening`
--

DROP TABLE IF EXISTS `Screening`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Screening` (
  `screeningId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `diffractionPlanId` int(10) unsigned DEFAULT NULL COMMENT 'references DiffractionPlan',
  `dataCollectionGroupId` int(11) DEFAULT NULL,
  `dataCollectionId` int(11) unsigned NOT NULL DEFAULT '0',
  `bltimeStamp` timestamp NULL DEFAULT NULL,
  `programVersion` varchar(45) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `shortComments` varchar(20) DEFAULT NULL,
  `xmlSampleInformation` longblob,
  PRIMARY KEY (`screeningId`),
  KEY `DNAScreening_FKIndex1` (`dataCollectionId`),
  KEY `Screening_FKIndexDiffractionPlanId` (`diffractionPlanId`)
) ENGINE=InnoDB AUTO_INCREMENT=379748 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ScreeningInput`
--

DROP TABLE IF EXISTS `ScreeningInput`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ScreeningInput` (
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
  KEY `ScreeningInput_FKIndex1` (`screeningId`),
  CONSTRAINT `ScreeningInput_ibfk_1` FOREIGN KEY (`screeningId`) REFERENCES `Screening` (`screeningId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=97571 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ScreeningOutput`
--

DROP TABLE IF EXISTS `ScreeningOutput`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ScreeningOutput` (
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
  KEY `ScreeningOutput_FKIndex1` (`screeningId`),
  CONSTRAINT `ScreeningOutput_ibfk_1` FOREIGN KEY (`screeningId`) REFERENCES `Screening` (`screeningId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=379689 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ScreeningOutputLattice`
--

DROP TABLE IF EXISTS `ScreeningOutputLattice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ScreeningOutputLattice` (
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
  KEY `ScreeningOutputLattice_FKIndex1` (`screeningOutputId`),
  CONSTRAINT `ScreeningOutputLattice_ibfk_1` FOREIGN KEY (`screeningOutputId`) REFERENCES `ScreeningOutput` (`screeningOutputId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=261035 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ScreeningRank`
--

DROP TABLE IF EXISTS `ScreeningRank`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ScreeningRank` (
  `screeningRankId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `screeningRankSetId` int(10) unsigned NOT NULL DEFAULT '0',
  `screeningId` int(10) unsigned NOT NULL DEFAULT '0',
  `rankValue` float DEFAULT NULL,
  `rankInformation` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`screeningRankId`),
  KEY `ScreeningRank_FKIndex1` (`screeningId`),
  KEY `ScreeningRank_FKIndex2` (`screeningRankSetId`),
  CONSTRAINT `ScreeningRank_ibfk_1` FOREIGN KEY (`screeningId`) REFERENCES `Screening` (`screeningId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ScreeningRank_ibfk_2` FOREIGN KEY (`screeningRankSetId`) REFERENCES `ScreeningRankSet` (`screeningRankSetId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6239 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ScreeningRankSet`
--

DROP TABLE IF EXISTS `ScreeningRankSet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ScreeningRankSet` (
  `screeningRankSetId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `rankEngine` varchar(255) DEFAULT NULL,
  `rankingProjectFileName` varchar(255) DEFAULT NULL,
  `rankingSummaryFileName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`screeningRankSetId`)
) ENGINE=InnoDB AUTO_INCREMENT=993 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ScreeningStrategy`
--

DROP TABLE IF EXISTS `ScreeningStrategy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ScreeningStrategy` (
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
  KEY `ScreeningStrategy_FKIndex1` (`screeningOutputId`),
  CONSTRAINT `ScreeningStrategy_ibfk_1` FOREIGN KEY (`screeningOutputId`) REFERENCES `ScreeningOutput` (`screeningOutputId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=206857 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ScreeningStrategySubWedge`
--

DROP TABLE IF EXISTS `ScreeningStrategySubWedge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ScreeningStrategySubWedge` (
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
  KEY `ScreeningStrategySubWedge_FK1` (`screeningStrategyWedgeId`),
  CONSTRAINT `ScreeningStrategySubWedge_FK1` FOREIGN KEY (`screeningStrategyWedgeId`) REFERENCES `ScreeningStrategyWedge` (`screeningStrategyWedgeId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=208729 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ScreeningStrategyWedge`
--

DROP TABLE IF EXISTS `ScreeningStrategyWedge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ScreeningStrategyWedge` (
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
  KEY `ScreeningStrategyWedge_IBFK_1` (`screeningStrategyId`),
  CONSTRAINT `ScreeningStrategyWedge_IBFK_1` FOREIGN KEY (`screeningStrategyId`) REFERENCES `ScreeningStrategy` (`screeningStrategyId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=201574 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Session_has_Person`
--

DROP TABLE IF EXISTS `Session_has_Person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Session_has_Person` (
  `sessionId` int(10) unsigned NOT NULL DEFAULT '0',
  `personId` int(10) unsigned NOT NULL DEFAULT '0',
  `role` enum('Local Contact','Local Contact 2','Staff','Team Leader','Co-Investigator','Principal Investigator','Alternate Contact') DEFAULT NULL,
  PRIMARY KEY (`sessionId`,`personId`),
  KEY `Session_has_Person_FKIndex1` (`sessionId`),
  KEY `Session_has_Person_FKIndex2` (`personId`),
  CONSTRAINT `Session_has_Person_ibfk_1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Session_has_Person_ibfk_2` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Shipping`
--

DROP TABLE IF EXISTS `Shipping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Shipping` (
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
  KEY `Shipping_FKIndexStatus` (`shippingStatus`),
  CONSTRAINT `Shipping_ibfk_1` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Shipping_ibfk_2` FOREIGN KEY (`sendingLabContactId`) REFERENCES `LabContact` (`labContactId`),
  CONSTRAINT `Shipping_ibfk_3` FOREIGN KEY (`returnLabContactId`) REFERENCES `LabContact` (`labContactId`)
) ENGINE=InnoDB AUTO_INCREMENT=307429 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ShippingHasSession`
--

DROP TABLE IF EXISTS `ShippingHasSession`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ShippingHasSession` (
  `shippingId` int(10) unsigned NOT NULL,
  `sessionId` int(10) unsigned NOT NULL,
  PRIMARY KEY (`shippingId`,`sessionId`),
  KEY `ShippingHasSession_FKIndex1` (`shippingId`),
  KEY `ShippingHasSession_FKIndex2` (`sessionId`),
  CONSTRAINT `ShippingHasSession_ibfk_1` FOREIGN KEY (`shippingId`) REFERENCES `Shipping` (`shippingId`),
  CONSTRAINT `ShippingHasSession_ibfk_2` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SpaceGroup`
--

DROP TABLE IF EXISTS `SpaceGroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SpaceGroup` (
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
  KEY `geometryClassnameId` (`geometryClassnameId`),
  CONSTRAINT `SpaceGroup_ibfk_1` FOREIGN KEY (`geometryClassnameId`) REFERENCES `GeometryClassname` (`geometryClassnameId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=237 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Specimen`
--

DROP TABLE IF EXISTS `Specimen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Specimen` (
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
  KEY `SampleToStockSolution` (`stockSolutionId`),
  CONSTRAINT `SamplePlateWellToBuffer` FOREIGN KEY (`bufferId`) REFERENCES `Buffer` (`bufferId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `SamplePlateWellToExperiment` FOREIGN KEY (`experimentId`) REFERENCES `Experiment` (`experimentId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `SamplePlateWellToMacromolecule` FOREIGN KEY (`macromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `SamplePlateWellToSafetyLevel` FOREIGN KEY (`safetyLevelId`) REFERENCES `SafetyLevel` (`safetyLevelId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `SamplePlateWellToSamplePlatePosition` FOREIGN KEY (`samplePlatePositionId`) REFERENCES `SamplePlatePosition` (`samplePlatePositionId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `SampleToStockSolution` FOREIGN KEY (`stockSolutionId`) REFERENCES `StockSolution` (`stockSolutionId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=87779 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `StockSolution`
--

DROP TABLE IF EXISTS `StockSolution`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `StockSolution` (
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
  KEY `StockSolutionToInstructionSet` (`instructionSetId`),
  CONSTRAINT `StockSolutionToBuffer` FOREIGN KEY (`bufferId`) REFERENCES `Buffer` (`bufferId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `StockSolutionToInstructionSet` FOREIGN KEY (`instructionSetId`) REFERENCES `InstructionSet` (`instructionSetId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `StockSolutionToMacromolecule` FOREIGN KEY (`macromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Stoichiometry`
--

DROP TABLE IF EXISTS `Stoichiometry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Stoichiometry` (
  `stoichiometryId` int(10) NOT NULL AUTO_INCREMENT,
  `hostMacromoleculeId` int(10) NOT NULL,
  `macromoleculeId` int(10) NOT NULL,
  `ratio` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`stoichiometryId`),
  KEY `StoichiometryToHost` (`hostMacromoleculeId`),
  KEY `StoichiometryToMacromolecule` (`macromoleculeId`),
  CONSTRAINT `StoichiometryToHost` FOREIGN KEY (`hostMacromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `StoichiometryToMacromolecule` FOREIGN KEY (`macromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Structure`
--

DROP TABLE IF EXISTS `Structure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Structure` (
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
  KEY `StructureToMacromolecule` (`macromoleculeId`),
  CONSTRAINT `StructureToMacromolecule` FOREIGN KEY (`macromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SubstructureDetermination`
--

DROP TABLE IF EXISTS `SubstructureDetermination`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SubstructureDetermination` (
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
  KEY `SubstructureDetermination_FKIndex3` (`spaceGroupId`),
  CONSTRAINT `SubstructureDetermination_phasingAnalysisfk_1` FOREIGN KEY (`phasingAnalysisId`) REFERENCES `PhasingAnalysis` (`phasingAnalysisId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `SubstructureDetermination_phasingProgramRunfk_1` FOREIGN KEY (`phasingProgramRunId`) REFERENCES `PhasingProgramRun` (`phasingProgramRunId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `SubstructureDetermination_spaceGroupfk_1` FOREIGN KEY (`spaceGroupId`) REFERENCES `SpaceGroup` (`spaceGroupId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14495 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Subtraction`
--

DROP TABLE IF EXISTS `Subtraction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Subtraction` (
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
  KEY `fk_Subtraction_2` (`bufferOnedimensionalFiles`),
  CONSTRAINT `EdnaAnalysisToMeasurement0` FOREIGN KEY (`dataCollectionId`) REFERENCES `SaxsDataCollection` (`dataCollectionId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Subtraction_1` FOREIGN KEY (`sampleOneDimensionalFiles`) REFERENCES `FrameList` (`frameListId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Subtraction_2` FOREIGN KEY (`bufferOnedimensionalFiles`) REFERENCES `FrameList` (`frameListId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=46340 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SubtractionToAbInitioModel`
--

DROP TABLE IF EXISTS `SubtractionToAbInitioModel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SubtractionToAbInitioModel` (
  `subtractionToAbInitioModelId` int(10) NOT NULL AUTO_INCREMENT,
  `abInitioId` int(10) DEFAULT NULL,
  `subtractionId` int(10) DEFAULT NULL,
  PRIMARY KEY (`subtractionToAbInitioModelId`),
  KEY `substractionToAbInitioModelToAbinitioModel` (`abInitioId`),
  KEY `ubstractionToSubstraction` (`subtractionId`),
  CONSTRAINT `substractionToAbInitioModelToAbinitioModel` FOREIGN KEY (`abInitioId`) REFERENCES `AbInitioModel` (`abInitioModelId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `substractionToSubstraction` FOREIGN KEY (`subtractionId`) REFERENCES `Subtraction` (`subtractionId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=30728 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Superposition`
--

DROP TABLE IF EXISTS `Superposition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Superposition` (
  `superpositionId` int(11) NOT NULL AUTO_INCREMENT,
  `subtractionId` int(11) NOT NULL,
  `abinitioModelPdbFilePath` varchar(255) DEFAULT NULL,
  `aprioriPdbFilePath` varchar(255) DEFAULT NULL,
  `alignedPdbFilePath` varchar(255) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  PRIMARY KEY (`superpositionId`),
  KEY `fk_Superposition_1` (`subtractionId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UntrustedRegion`
--

DROP TABLE IF EXISTS `UntrustedRegion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UntrustedRegion` (
  `untrustedRegionId` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `detectorId` int(11) NOT NULL,
  `x1` int(11) NOT NULL,
  `x2` int(11) NOT NULL,
  `y1` int(11) NOT NULL,
  `y2` int(11) NOT NULL,
  PRIMARY KEY (`untrustedRegionId`),
  KEY `UntrustedRegion_FKIndex1` (`detectorId`),
  CONSTRAINT `UntrustedRegion_ibfk_1` FOREIGN KEY (`detectorId`) REFERENCES `Detector` (`detectorId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Untrsuted region linked to a detector';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `V_AnalysisInfo`
--

DROP TABLE IF EXISTS `V_AnalysisInfo`;
/*!50001 DROP VIEW IF EXISTS `V_AnalysisInfo`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `V_AnalysisInfo` (
  `experimentCreationDate` tinyint NOT NULL,
  `timeStart` tinyint NOT NULL,
  `dataCollectionId` tinyint NOT NULL,
  `measurementId` tinyint NOT NULL,
  `proposalId` tinyint NOT NULL,
  `proposalCode` tinyint NOT NULL,
  `proposalNumber` tinyint NOT NULL,
  `priorityLevelId` tinyint NOT NULL,
  `code` tinyint NOT NULL,
  `exposureTemperature` tinyint NOT NULL,
  `transmission` tinyint NOT NULL,
  `measurementComments` tinyint NOT NULL,
  `experimentComments` tinyint NOT NULL,
  `experimentId` tinyint NOT NULL,
  `experimentType` tinyint NOT NULL,
  `conc` tinyint NOT NULL,
  `bufferAcronym` tinyint NOT NULL,
  `macromoleculeAcronym` tinyint NOT NULL,
  `bufferId` tinyint NOT NULL,
  `macromoleculeId` tinyint NOT NULL,
  `subtractedFilePath` tinyint NOT NULL,
  `rgGuinier` tinyint NOT NULL,
  `firstPointUsed` tinyint NOT NULL,
  `lastPointUsed` tinyint NOT NULL,
  `I0` tinyint NOT NULL,
  `isagregated` tinyint NOT NULL,
  `subtractionId` tinyint NOT NULL,
  `rgGnom` tinyint NOT NULL,
  `total` tinyint NOT NULL,
  `dmax` tinyint NOT NULL,
  `volume` tinyint NOT NULL,
  `i0stdev` tinyint NOT NULL,
  `quality` tinyint NOT NULL,
  `substractionCreationTime` tinyint NOT NULL,
  `bufferBeforeMeasurementId` tinyint NOT NULL,
  `bufferAfterMeasurementId` tinyint NOT NULL,
  `bufferBeforeFramesMerged` tinyint NOT NULL,
  `bufferBeforeMergeId` tinyint NOT NULL,
  `bufferBeforeAverageFilePath` tinyint NOT NULL,
  `sampleMeasurementId` tinyint NOT NULL,
  `sampleMergeId` tinyint NOT NULL,
  `averageFilePath` tinyint NOT NULL,
  `framesMerge` tinyint NOT NULL,
  `framesCount` tinyint NOT NULL,
  `bufferAfterFramesMerged` tinyint NOT NULL,
  `bufferAfterMergeId` tinyint NOT NULL,
  `bufferAfterAverageFilePath` tinyint NOT NULL,
  `modelListId1` tinyint NOT NULL,
  `nsdFilePath` tinyint NOT NULL,
  `modelListId2` tinyint NOT NULL,
  `chi2RgFilePath` tinyint NOT NULL,
  `averagedModel` tinyint NOT NULL,
  `averagedModelId` tinyint NOT NULL,
  `rapidShapeDeterminationModel` tinyint NOT NULL,
  `rapidShapeDeterminationModelId` tinyint NOT NULL,
  `shapeDeterminationModel` tinyint NOT NULL,
  `shapeDeterminationModelId` tinyint NOT NULL,
  `abInitioModelId` tinyint NOT NULL,
  `comments` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `Workflow`
--

DROP TABLE IF EXISTS `Workflow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Workflow` (
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
) ENGINE=InnoDB AUTO_INCREMENT=45537 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `WorkflowDehydration`
--

DROP TABLE IF EXISTS `WorkflowDehydration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `WorkflowDehydration` (
  `workflowDehydrationId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `workflowId` int(11) unsigned NOT NULL COMMENT 'Related workflow',
  `dataFilePath` varchar(255) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`workflowDehydrationId`),
  KEY `WorkflowDehydration_FKIndex1` (`workflowId`),
  CONSTRAINT `WorkflowDehydration_workflowfk_1` FOREIGN KEY (`workflowId`) REFERENCES `Workflow` (`workflowId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `WorkflowMesh`
--

DROP TABLE IF EXISTS `WorkflowMesh`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `WorkflowMesh` (
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
  KEY `bestImageId` (`bestImageId`),
  CONSTRAINT `WorkflowMesh_ibfk_1` FOREIGN KEY (`bestPositionId`) REFERENCES `MotorPosition` (`motorPositionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `WorkflowMesh_ibfk_2` FOREIGN KEY (`bestImageId`) REFERENCES `Image` (`imageId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `WorkflowMesh_workflowfk_1` FOREIGN KEY (`workflowId`) REFERENCES `Workflow` (`workflowId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=88671 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `WorkflowStep`
--

DROP TABLE IF EXISTS `WorkflowStep`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `WorkflowStep` (
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
  KEY `step_to_workflow_fk_idx` (`workflowId`),
  CONSTRAINT `step_to_workflow_fk` FOREIGN KEY (`workflowId`) REFERENCES `Workflow` (`workflowId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=40841 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `WorkflowType`
--

DROP TABLE IF EXISTS `WorkflowType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `WorkflowType` (
  `workflowTypeId` int(11) NOT NULL AUTO_INCREMENT,
  `workflowTypeName` varchar(45) DEFAULT NULL,
  `comments` varchar(2048) DEFAULT NULL,
  `recordTimeStamp` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`workflowTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `XFEFluorescenceSpectrum`
--

DROP TABLE IF EXISTS `XFEFluorescenceSpectrum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `XFEFluorescenceSpectrum` (
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
  PRIMARY KEY (`xfeFluorescenceSpectrumId`),
  KEY `XFEFluorescnceSpectrum_FKIndex1` (`blSampleId`),
  KEY `XFEFluorescnceSpectrum_FKIndex2` (`sessionId`),
  CONSTRAINT `XFE_ibfk_1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `XFE_ibfk_2` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5061 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `v_Log4Stat`
--

DROP TABLE IF EXISTS `v_Log4Stat`;
/*!50001 DROP VIEW IF EXISTS `v_Log4Stat`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_Log4Stat` (
  `id` tinyint NOT NULL,
  `priority` tinyint NOT NULL,
  `timestamp` tinyint NOT NULL,
  `msg` tinyint NOT NULL,
  `detail` tinyint NOT NULL,
  `value` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_datacollection`
--

DROP TABLE IF EXISTS `v_datacollection`;
/*!50001 DROP VIEW IF EXISTS `v_datacollection`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_datacollection` (
  `dataCollectionId` tinyint NOT NULL,
  `dataCollectionGroupId` tinyint NOT NULL,
  `strategySubWedgeOrigId` tinyint NOT NULL,
  `detectorId` tinyint NOT NULL,
  `blSubSampleId` tinyint NOT NULL,
  `dataCollectionNumber` tinyint NOT NULL,
  `startTime` tinyint NOT NULL,
  `endTime` tinyint NOT NULL,
  `runStatus` tinyint NOT NULL,
  `axisStart` tinyint NOT NULL,
  `axisEnd` tinyint NOT NULL,
  `axisRange` tinyint NOT NULL,
  `overlap` tinyint NOT NULL,
  `numberOfImages` tinyint NOT NULL,
  `startImageNumber` tinyint NOT NULL,
  `numberOfPasses` tinyint NOT NULL,
  `exposureTime` tinyint NOT NULL,
  `imageDirectory` tinyint NOT NULL,
  `imagePrefix` tinyint NOT NULL,
  `imageSuffix` tinyint NOT NULL,
  `fileTemplate` tinyint NOT NULL,
  `wavelength` tinyint NOT NULL,
  `resolution` tinyint NOT NULL,
  `detectorDistance` tinyint NOT NULL,
  `xBeam` tinyint NOT NULL,
  `yBeam` tinyint NOT NULL,
  `xBeamPix` tinyint NOT NULL,
  `yBeamPix` tinyint NOT NULL,
  `comments` tinyint NOT NULL,
  `printableForReport` tinyint NOT NULL,
  `slitGapVertical` tinyint NOT NULL,
  `slitGapHorizontal` tinyint NOT NULL,
  `transmission` tinyint NOT NULL,
  `synchrotronMode` tinyint NOT NULL,
  `xtalSnapshotFullPath1` tinyint NOT NULL,
  `xtalSnapshotFullPath2` tinyint NOT NULL,
  `xtalSnapshotFullPath3` tinyint NOT NULL,
  `xtalSnapshotFullPath4` tinyint NOT NULL,
  `rotationAxis` tinyint NOT NULL,
  `phiStart` tinyint NOT NULL,
  `kappaStart` tinyint NOT NULL,
  `omegaStart` tinyint NOT NULL,
  `resolutionAtCorner` tinyint NOT NULL,
  `detector2Theta` tinyint NOT NULL,
  `undulatorGap1` tinyint NOT NULL,
  `undulatorGap2` tinyint NOT NULL,
  `undulatorGap3` tinyint NOT NULL,
  `beamSizeAtSampleX` tinyint NOT NULL,
  `beamSizeAtSampleY` tinyint NOT NULL,
  `centeringMethod` tinyint NOT NULL,
  `averageTemperature` tinyint NOT NULL,
  `actualCenteringPosition` tinyint NOT NULL,
  `beamShape` tinyint NOT NULL,
  `flux` tinyint NOT NULL,
  `flux_end` tinyint NOT NULL,
  `totalAbsorbedDose` tinyint NOT NULL,
  `bestWilsonPlotPath` tinyint NOT NULL,
  `imageQualityIndicatorsPlotPath` tinyint NOT NULL,
  `imageQualityIndicatorsCSVPath` tinyint NOT NULL,
  `sessionId` tinyint NOT NULL,
  `proposalId` tinyint NOT NULL,
  `workflowId` tinyint NOT NULL,
  `rankingResolution` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_datacollection_autoprocintegration`
--

DROP TABLE IF EXISTS `v_datacollection_autoprocintegration`;
/*!50001 DROP VIEW IF EXISTS `v_datacollection_autoprocintegration`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_datacollection_autoprocintegration` (
  `v_datacollection_summary_phasing_autoProcIntegrationId` tinyint NOT NULL,
  `v_datacollection_summary_phasing_dataCollectionId` tinyint NOT NULL,
  `v_datacollection_summary_phasing_cell_a` tinyint NOT NULL,
  `v_datacollection_summary_phasing_cell_b` tinyint NOT NULL,
  `v_datacollection_summary_phasing_cell_c` tinyint NOT NULL,
  `v_datacollection_summary_phasing_cell_alpha` tinyint NOT NULL,
  `v_datacollection_summary_phasing_cell_beta` tinyint NOT NULL,
  `v_datacollection_summary_phasing_cell_gamma` tinyint NOT NULL,
  `v_datacollection_summary_phasing_anomalous` tinyint NOT NULL,
  `v_datacollection_summary_phasing_autoproc_space_group` tinyint NOT NULL,
  `v_datacollection_summary_phasing_autoproc_autoprocId` tinyint NOT NULL,
  `v_datacollection_summary_phasing_autoProcScalingId` tinyint NOT NULL,
  `v_datacollection_processingPrograms` tinyint NOT NULL,
  `v_datacollection_summary_phasing_autoProcProgramId` tinyint NOT NULL,
  `v_datacollection_processingStatus` tinyint NOT NULL,
  `v_datacollection_summary_session_sessionId` tinyint NOT NULL,
  `v_datacollection_summary_session_proposalId` tinyint NOT NULL,
  `AutoProcIntegration_dataCollectionId` tinyint NOT NULL,
  `AutoProcIntegration_autoProcIntegrationId` tinyint NOT NULL,
  `PhasingStep_phasing_phasingStepType` tinyint NOT NULL,
  `SpaceGroup_spaceGroupShortName` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_datacollection_phasing`
--

DROP TABLE IF EXISTS `v_datacollection_phasing`;
/*!50001 DROP VIEW IF EXISTS `v_datacollection_phasing`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_datacollection_phasing` (
  `phasingStepId` tinyint NOT NULL,
  `previousPhasingStepId` tinyint NOT NULL,
  `phasingAnalysisId` tinyint NOT NULL,
  `autoProcIntegrationId` tinyint NOT NULL,
  `dataCollectionId` tinyint NOT NULL,
  `anomalous` tinyint NOT NULL,
  `spaceGroup` tinyint NOT NULL,
  `autoProcId` tinyint NOT NULL,
  `phasingStepType` tinyint NOT NULL,
  `method` tinyint NOT NULL,
  `solventContent` tinyint NOT NULL,
  `enantiomorph` tinyint NOT NULL,
  `lowRes` tinyint NOT NULL,
  `highRes` tinyint NOT NULL,
  `autoProcScalingId` tinyint NOT NULL,
  `spaceGroupShortName` tinyint NOT NULL,
  `processingPrograms` tinyint NOT NULL,
  `processingStatus` tinyint NOT NULL,
  `phasingPrograms` tinyint NOT NULL,
  `phasingStatus` tinyint NOT NULL,
  `phasingStartTime` tinyint NOT NULL,
  `phasingEndTime` tinyint NOT NULL,
  `sessionId` tinyint NOT NULL,
  `proposalId` tinyint NOT NULL,
  `blSampleId` tinyint NOT NULL,
  `name` tinyint NOT NULL,
  `code` tinyint NOT NULL,
  `acronym` tinyint NOT NULL,
  `proteinId` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_datacollection_phasing_program_run`
--

DROP TABLE IF EXISTS `v_datacollection_phasing_program_run`;
/*!50001 DROP VIEW IF EXISTS `v_datacollection_phasing_program_run`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_datacollection_phasing_program_run` (
  `phasingStepId` tinyint NOT NULL,
  `previousPhasingStepId` tinyint NOT NULL,
  `phasingAnalysisId` tinyint NOT NULL,
  `autoProcIntegrationId` tinyint NOT NULL,
  `dataCollectionId` tinyint NOT NULL,
  `autoProcId` tinyint NOT NULL,
  `phasingStepType` tinyint NOT NULL,
  `method` tinyint NOT NULL,
  `autoProcScalingId` tinyint NOT NULL,
  `spaceGroupShortName` tinyint NOT NULL,
  `phasingPrograms` tinyint NOT NULL,
  `phasingStatus` tinyint NOT NULL,
  `sessionId` tinyint NOT NULL,
  `proposalId` tinyint NOT NULL,
  `blSampleId` tinyint NOT NULL,
  `name` tinyint NOT NULL,
  `code` tinyint NOT NULL,
  `acronym` tinyint NOT NULL,
  `proteinId` tinyint NOT NULL,
  `phasingProgramAttachmentId` tinyint NOT NULL,
  `fileType` tinyint NOT NULL,
  `fileName` tinyint NOT NULL,
  `filePath` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_datacollection_summary`
--

DROP TABLE IF EXISTS `v_datacollection_summary`;
/*!50001 DROP VIEW IF EXISTS `v_datacollection_summary`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_datacollection_summary` (
  `DataCollectionGroup_dataCollectionGroupId` tinyint NOT NULL,
  `DataCollectionGroup_blSampleId` tinyint NOT NULL,
  `DataCollectionGroup_sessionId` tinyint NOT NULL,
  `DataCollectionGroup_workflowId` tinyint NOT NULL,
  `DataCollectionGroup_experimentType` tinyint NOT NULL,
  `DataCollectionGroup_startTime` tinyint NOT NULL,
  `DataCollectionGroup_endTime` tinyint NOT NULL,
  `DataCollectionGroup_comments` tinyint NOT NULL,
  `DataCollectionGroup_actualSampleBarcode` tinyint NOT NULL,
  `DataCollectionGroup_xtalSnapshotFullPath` tinyint NOT NULL,
  `containerType` tinyint NOT NULL,
  `beamlineLocation` tinyint NOT NULL,
  `sampleChangerLocation` tinyint NOT NULL,
  `barCode` tinyint NOT NULL,
  `BLSample_blSampleId` tinyint NOT NULL,
  `BLSample_crystalId` tinyint NOT NULL,
  `BLSample_name` tinyint NOT NULL,
  `BLSample_code` tinyint NOT NULL,
  `BLSession_sessionId` tinyint NOT NULL,
  `BLSession_proposalId` tinyint NOT NULL,
  `BLSession_protectedData` tinyint NOT NULL,
  `Protein_proteinId` tinyint NOT NULL,
  `Protein_name` tinyint NOT NULL,
  `Protein_acronym` tinyint NOT NULL,
  `DataCollection_dataCollectionId` tinyint NOT NULL,
  `DataCollection_dataCollectionGroupId` tinyint NOT NULL,
  `DataCollection_startTime` tinyint NOT NULL,
  `DataCollection_endTime` tinyint NOT NULL,
  `DataCollection_runStatus` tinyint NOT NULL,
  `DataCollection_numberOfImages` tinyint NOT NULL,
  `DataCollection_startImageNumber` tinyint NOT NULL,
  `DataCollection_numberOfPasses` tinyint NOT NULL,
  `DataCollection_exposureTime` tinyint NOT NULL,
  `DataCollection_imageDirectory` tinyint NOT NULL,
  `DataCollection_wavelength` tinyint NOT NULL,
  `DataCollection_resolution` tinyint NOT NULL,
  `DataCollection_detectorDistance` tinyint NOT NULL,
  `DataCollection_xBeam` tinyint NOT NULL,
  `transmission` tinyint NOT NULL,
  `DataCollection_yBeam` tinyint NOT NULL,
  `DataCollection_imagePrefix` tinyint NOT NULL,
  `DataCollection_comments` tinyint NOT NULL,
  `DataCollection_xtalSnapshotFullPath1` tinyint NOT NULL,
  `DataCollection_xtalSnapshotFullPath2` tinyint NOT NULL,
  `DataCollection_xtalSnapshotFullPath3` tinyint NOT NULL,
  `DataCollection_xtalSnapshotFullPath4` tinyint NOT NULL,
  `DataCollection_phiStart` tinyint NOT NULL,
  `DataCollection_kappaStart` tinyint NOT NULL,
  `DataCollection_omegaStart` tinyint NOT NULL,
  `DataCollection_flux` tinyint NOT NULL,
  `DataCollection_flux_end` tinyint NOT NULL,
  `DataCollection_resolutionAtCorner` tinyint NOT NULL,
  `DataCollection_bestWilsonPlotPath` tinyint NOT NULL,
  `DataCollection_dataCollectionNumber` tinyint NOT NULL,
  `DataCollection_axisRange` tinyint NOT NULL,
  `DataCollection_axisStart` tinyint NOT NULL,
  `DataCollection_axisEnd` tinyint NOT NULL,
  `DataCollection_rotationAxis` tinyint NOT NULL,
  `Workflow_workflowTitle` tinyint NOT NULL,
  `Workflow_workflowType` tinyint NOT NULL,
  `Workflow_status` tinyint NOT NULL,
  `Workflow_workflowId` tinyint NOT NULL,
  `AutoProcIntegration_dataCollectionId` tinyint NOT NULL,
  `autoProcScalingId` tinyint NOT NULL,
  `cell_a` tinyint NOT NULL,
  `cell_b` tinyint NOT NULL,
  `cell_c` tinyint NOT NULL,
  `cell_alpha` tinyint NOT NULL,
  `cell_beta` tinyint NOT NULL,
  `cell_gamma` tinyint NOT NULL,
  `scalingStatisticsType` tinyint NOT NULL,
  `resolutionLimitHigh` tinyint NOT NULL,
  `resolutionLimitLow` tinyint NOT NULL,
  `completeness` tinyint NOT NULL,
  `AutoProc_spaceGroup` tinyint NOT NULL,
  `autoProcId` tinyint NOT NULL,
  `rMerge` tinyint NOT NULL,
  `AutoProcIntegration_autoProcIntegrationId` tinyint NOT NULL,
  `AutoProcProgram_processingPrograms` tinyint NOT NULL,
  `AutoProcProgram_processingStatus` tinyint NOT NULL,
  `Screening_screeningId` tinyint NOT NULL,
  `Screening_dataCollectionId` tinyint NOT NULL,
  `ScreeningOutput_strategySuccess` tinyint NOT NULL,
  `ScreeningOutput_indexingSuccess` tinyint NOT NULL,
  `ScreeningOutput_rankingResolution` tinyint NOT NULL,
  `ScreeningOutput_mosaicity` tinyint NOT NULL,
  `ScreeningOutputLattice_spaceGroup` tinyint NOT NULL,
  `ScreeningOutputLattice_unitCell_a` tinyint NOT NULL,
  `ScreeningOutputLattice_unitCell_b` tinyint NOT NULL,
  `ScreeningOutputLattice_unitCell_c` tinyint NOT NULL,
  `ScreeningOutputLattice_unitCell_alpha` tinyint NOT NULL,
  `ScreeningOutputLattice_unitCell_beta` tinyint NOT NULL,
  `ScreeningOutputLattice_unitCell_gamma` tinyint NOT NULL,
  `shippingId` tinyint NOT NULL,
  `detectorType` tinyint NOT NULL,
  `detectorModel` tinyint NOT NULL,
  `detectorManufacturer` tinyint NOT NULL,
  `detectorPixelSizeHorizontal` tinyint NOT NULL,
  `detectorPixelSizeVertical` tinyint NOT NULL,
  `detectorMode` tinyint NOT NULL,
  `observedResolution` tinyint NOT NULL,
  `minimalResolution` tinyint NOT NULL,
  `exposureTime` tinyint NOT NULL,
  `oscillationRange` tinyint NOT NULL,
  `maximalResolution` tinyint NOT NULL,
  `screeningResolution` tinyint NOT NULL,
  `radiationSensitivity` tinyint NOT NULL,
  `anomalousScatterer` tinyint NOT NULL,
  `preferredBeamSizeX` tinyint NOT NULL,
  `preferredBeamSizeY` tinyint NOT NULL,
  `preferredBeamDiameter` tinyint NOT NULL,
  `forcedSpaceGroup` tinyint NOT NULL,
  `numberOfPositions` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_datacollection_summary_autoprocintegration`
--

DROP TABLE IF EXISTS `v_datacollection_summary_autoprocintegration`;
/*!50001 DROP VIEW IF EXISTS `v_datacollection_summary_autoprocintegration`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_datacollection_summary_autoprocintegration` (
  `AutoProcIntegration_dataCollectionId` tinyint NOT NULL,
  `cell_a` tinyint NOT NULL,
  `cell_b` tinyint NOT NULL,
  `cell_c` tinyint NOT NULL,
  `cell_alpha` tinyint NOT NULL,
  `cell_beta` tinyint NOT NULL,
  `cell_gamma` tinyint NOT NULL,
  `AutoProcIntegration_autoProcIntegrationId` tinyint NOT NULL,
  `v_datacollection_summary_autoprocintegration_processingPrograms` tinyint NOT NULL,
  `v_datacollection_summary_autoprocintegration_processingStatus` tinyint NOT NULL,
  `AutoProcIntegration_phasing_dataCollectionId` tinyint NOT NULL,
  `PhasingStep_phasing_phasingStepType` tinyint NOT NULL,
  `SpaceGroup_spaceGroupShortName` tinyint NOT NULL,
  `autoProcId` tinyint NOT NULL,
  `AutoProc_spaceGroup` tinyint NOT NULL,
  `scalingStatisticsType` tinyint NOT NULL,
  `resolutionLimitHigh` tinyint NOT NULL,
  `resolutionLimitLow` tinyint NOT NULL,
  `rMerge` tinyint NOT NULL,
  `completeness` tinyint NOT NULL,
  `autoProcScalingId` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_datacollection_summary_datacollectiongroup`
--

DROP TABLE IF EXISTS `v_datacollection_summary_datacollectiongroup`;
/*!50001 DROP VIEW IF EXISTS `v_datacollection_summary_datacollectiongroup`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_datacollection_summary_datacollectiongroup` (
  `DataCollectionGroup_dataCollectionGroupId` tinyint NOT NULL,
  `DataCollectionGroup_blSampleId` tinyint NOT NULL,
  `DataCollectionGroup_sessionId` tinyint NOT NULL,
  `DataCollectionGroup_workflowId` tinyint NOT NULL,
  `DataCollectionGroup_experimentType` tinyint NOT NULL,
  `DataCollectionGroup_startTime` tinyint NOT NULL,
  `DataCollectionGroup_endTime` tinyint NOT NULL,
  `DataCollectionGroup_comments` tinyint NOT NULL,
  `DataCollectionGroup_actualSampleBarcode` tinyint NOT NULL,
  `DataCollectionGroup_xtalSnapshotFullPath` tinyint NOT NULL,
  `BLSample_blSampleId` tinyint NOT NULL,
  `BLSample_crystalId` tinyint NOT NULL,
  `BLSample_name` tinyint NOT NULL,
  `BLSample_code` tinyint NOT NULL,
  `BLSession_sessionId` tinyint NOT NULL,
  `BLSession_proposalId` tinyint NOT NULL,
  `BLSession_protectedData` tinyint NOT NULL,
  `Protein_proteinId` tinyint NOT NULL,
  `Protein_name` tinyint NOT NULL,
  `Protein_acronym` tinyint NOT NULL,
  `DataCollection_dataCollectionId` tinyint NOT NULL,
  `DataCollection_dataCollectionGroupId` tinyint NOT NULL,
  `DataCollection_startTime` tinyint NOT NULL,
  `DataCollection_endTime` tinyint NOT NULL,
  `DataCollection_runStatus` tinyint NOT NULL,
  `DataCollection_numberOfImages` tinyint NOT NULL,
  `DataCollection_startImageNumber` tinyint NOT NULL,
  `DataCollection_numberOfPasses` tinyint NOT NULL,
  `DataCollection_exposureTime` tinyint NOT NULL,
  `DataCollection_imageDirectory` tinyint NOT NULL,
  `DataCollection_wavelength` tinyint NOT NULL,
  `DataCollection_resolution` tinyint NOT NULL,
  `DataCollection_detectorDistance` tinyint NOT NULL,
  `DataCollection_xBeam` tinyint NOT NULL,
  `DataCollection_yBeam` tinyint NOT NULL,
  `DataCollection_comments` tinyint NOT NULL,
  `DataCollection_xtalSnapshotFullPath1` tinyint NOT NULL,
  `DataCollection_xtalSnapshotFullPath2` tinyint NOT NULL,
  `DataCollection_xtalSnapshotFullPath3` tinyint NOT NULL,
  `DataCollection_xtalSnapshotFullPath4` tinyint NOT NULL,
  `DataCollection_phiStart` tinyint NOT NULL,
  `DataCollection_kappaStart` tinyint NOT NULL,
  `DataCollection_omegaStart` tinyint NOT NULL,
  `DataCollection_resolutionAtCorner` tinyint NOT NULL,
  `DataCollection_bestWilsonPlotPath` tinyint NOT NULL,
  `DataCollection_dataCollectionNumber` tinyint NOT NULL,
  `DataCollection_axisRange` tinyint NOT NULL,
  `DataCollection_axisStart` tinyint NOT NULL,
  `DataCollection_axisEnd` tinyint NOT NULL,
  `Workflow_workflowTitle` tinyint NOT NULL,
  `Workflow_workflowType` tinyint NOT NULL,
  `Workflow_status` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_datacollection_summary_phasing`
--

DROP TABLE IF EXISTS `v_datacollection_summary_phasing`;
/*!50001 DROP VIEW IF EXISTS `v_datacollection_summary_phasing`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_datacollection_summary_phasing` (
  `v_datacollection_summary_phasing_autoProcIntegrationId` tinyint NOT NULL,
  `v_datacollection_summary_phasing_dataCollectionId` tinyint NOT NULL,
  `v_datacollection_summary_phasing_cell_a` tinyint NOT NULL,
  `v_datacollection_summary_phasing_cell_b` tinyint NOT NULL,
  `v_datacollection_summary_phasing_cell_c` tinyint NOT NULL,
  `v_datacollection_summary_phasing_cell_alpha` tinyint NOT NULL,
  `v_datacollection_summary_phasing_cell_beta` tinyint NOT NULL,
  `v_datacollection_summary_phasing_cell_gamma` tinyint NOT NULL,
  `v_datacollection_summary_phasing_anomalous` tinyint NOT NULL,
  `v_datacollection_summary_phasing_autoproc_space_group` tinyint NOT NULL,
  `v_datacollection_summary_phasing_autoproc_autoprocId` tinyint NOT NULL,
  `v_datacollection_summary_phasing_autoProcScalingId` tinyint NOT NULL,
  `v_datacollection_summary_phasing_processingPrograms` tinyint NOT NULL,
  `v_datacollection_summary_phasing_autoProcProgramId` tinyint NOT NULL,
  `v_datacollection_summary_phasing_processingStatus` tinyint NOT NULL,
  `v_datacollection_summary_session_sessionId` tinyint NOT NULL,
  `v_datacollection_summary_session_proposalId` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_datacollection_summary_screening`
--

DROP TABLE IF EXISTS `v_datacollection_summary_screening`;
/*!50001 DROP VIEW IF EXISTS `v_datacollection_summary_screening`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_datacollection_summary_screening` (
  `Screening_screeningId` tinyint NOT NULL,
  `Screening_dataCollectionId` tinyint NOT NULL,
  `ScreeningOutput_strategySuccess` tinyint NOT NULL,
  `ScreeningOutput_indexingSuccess` tinyint NOT NULL,
  `ScreeningOutput_rankingResolution` tinyint NOT NULL,
  `ScreeningOutput_mosaicityEstimated` tinyint NOT NULL,
  `ScreeningOutput_mosaicity` tinyint NOT NULL,
  `ScreeningOutputLattice_spaceGroup` tinyint NOT NULL,
  `ScreeningOutputLattice_unitCell_a` tinyint NOT NULL,
  `ScreeningOutputLattice_unitCell_b` tinyint NOT NULL,
  `ScreeningOutputLattice_unitCell_c` tinyint NOT NULL,
  `ScreeningOutputLattice_unitCell_alpha` tinyint NOT NULL,
  `ScreeningOutputLattice_unitCell_beta` tinyint NOT NULL,
  `ScreeningOutputLattice_unitCell_gamma` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_dewar`
--

DROP TABLE IF EXISTS `v_dewar`;
/*!50001 DROP VIEW IF EXISTS `v_dewar`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_dewar` (
  `proposalId` tinyint NOT NULL,
  `shippingId` tinyint NOT NULL,
  `shippingName` tinyint NOT NULL,
  `dewarId` tinyint NOT NULL,
  `dewarName` tinyint NOT NULL,
  `dewarStatus` tinyint NOT NULL,
  `proposalCode` tinyint NOT NULL,
  `proposalNumber` tinyint NOT NULL,
  `creationDate` tinyint NOT NULL,
  `shippingType` tinyint NOT NULL,
  `barCode` tinyint NOT NULL,
  `shippingStatus` tinyint NOT NULL,
  `beamLineName` tinyint NOT NULL,
  `nbEvents` tinyint NOT NULL,
  `storesin` tinyint NOT NULL,
  `nbSamples` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_dewarBeamline`
--

DROP TABLE IF EXISTS `v_dewarBeamline`;
/*!50001 DROP VIEW IF EXISTS `v_dewarBeamline`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_dewarBeamline` (
  `beamLineName` tinyint NOT NULL,
  `COUNT(*)` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_dewarBeamlineByWeek`
--

DROP TABLE IF EXISTS `v_dewarBeamlineByWeek`;
/*!50001 DROP VIEW IF EXISTS `v_dewarBeamlineByWeek`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_dewarBeamlineByWeek` (
  `Week` tinyint NOT NULL,
  `ID14` tinyint NOT NULL,
  `ID23` tinyint NOT NULL,
  `ID29` tinyint NOT NULL,
  `BM14` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_dewarByWeek`
--

DROP TABLE IF EXISTS `v_dewarByWeek`;
/*!50001 DROP VIEW IF EXISTS `v_dewarByWeek`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_dewarByWeek` (
  `Week` tinyint NOT NULL,
  `Dewars Tracked` tinyint NOT NULL,
  `Dewars Non-Tracked` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_dewarByWeekTotal`
--

DROP TABLE IF EXISTS `v_dewarByWeekTotal`;
/*!50001 DROP VIEW IF EXISTS `v_dewarByWeekTotal`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_dewarByWeekTotal` (
  `Week` tinyint NOT NULL,
  `Dewars Tracked` tinyint NOT NULL,
  `Dewars Non-Tracked` tinyint NOT NULL,
  `Total` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_dewarList`
--

DROP TABLE IF EXISTS `v_dewarList`;
/*!50001 DROP VIEW IF EXISTS `v_dewarList`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_dewarList` (
  `proposal` tinyint NOT NULL,
  `shippingName` tinyint NOT NULL,
  `dewarName` tinyint NOT NULL,
  `barCode` tinyint NOT NULL,
  `creationDate` tinyint NOT NULL,
  `shippingType` tinyint NOT NULL,
  `nbEvents` tinyint NOT NULL,
  `dewarStatus` tinyint NOT NULL,
  `shippingStatus` tinyint NOT NULL,
  `nbSamples` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_dewarProposalCode`
--

DROP TABLE IF EXISTS `v_dewarProposalCode`;
/*!50001 DROP VIEW IF EXISTS `v_dewarProposalCode`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_dewarProposalCode` (
  `proposalCode` tinyint NOT NULL,
  `COUNT(*)` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_dewarProposalCodeByWeek`
--

DROP TABLE IF EXISTS `v_dewarProposalCodeByWeek`;
/*!50001 DROP VIEW IF EXISTS `v_dewarProposalCodeByWeek`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_dewarProposalCodeByWeek` (
  `Week` tinyint NOT NULL,
  `MX` tinyint NOT NULL,
  `FX` tinyint NOT NULL,
  `BM14U` tinyint NOT NULL,
  `BM161` tinyint NOT NULL,
  `BM162` tinyint NOT NULL,
  `Others` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_dewar_summary`
--

DROP TABLE IF EXISTS `v_dewar_summary`;
/*!50001 DROP VIEW IF EXISTS `v_dewar_summary`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_dewar_summary` (
  `shippingName` tinyint NOT NULL,
  `deliveryAgent_agentName` tinyint NOT NULL,
  `deliveryAgent_shippingDate` tinyint NOT NULL,
  `deliveryAgent_deliveryDate` tinyint NOT NULL,
  `deliveryAgent_agentCode` tinyint NOT NULL,
  `deliveryAgent_flightCode` tinyint NOT NULL,
  `shippingStatus` tinyint NOT NULL,
  `bltimeStamp` tinyint NOT NULL,
  `laboratoryId` tinyint NOT NULL,
  `isStorageShipping` tinyint NOT NULL,
  `creationDate` tinyint NOT NULL,
  `Shipping_comments` tinyint NOT NULL,
  `sendingLabContactId` tinyint NOT NULL,
  `returnLabContactId` tinyint NOT NULL,
  `returnCourier` tinyint NOT NULL,
  `dateOfShippingToUser` tinyint NOT NULL,
  `shippingType` tinyint NOT NULL,
  `dewarId` tinyint NOT NULL,
  `shippingId` tinyint NOT NULL,
  `dewarCode` tinyint NOT NULL,
  `comments` tinyint NOT NULL,
  `storageLocation` tinyint NOT NULL,
  `dewarStatus` tinyint NOT NULL,
  `isStorageDewar` tinyint NOT NULL,
  `barCode` tinyint NOT NULL,
  `firstExperimentId` tinyint NOT NULL,
  `customsValue` tinyint NOT NULL,
  `transportValue` tinyint NOT NULL,
  `trackingNumberToSynchrotron` tinyint NOT NULL,
  `trackingNumberFromSynchrotron` tinyint NOT NULL,
  `type` tinyint NOT NULL,
  `sessionId` tinyint NOT NULL,
  `beamlineName` tinyint NOT NULL,
  `sessionStartDate` tinyint NOT NULL,
  `sessionEndDate` tinyint NOT NULL,
  `beamLineOperator` tinyint NOT NULL,
  `proposalId` tinyint NOT NULL,
  `containerId` tinyint NOT NULL,
  `containerType` tinyint NOT NULL,
  `capacity` tinyint NOT NULL,
  `beamlineLocation` tinyint NOT NULL,
  `sampleChangerLocation` tinyint NOT NULL,
  `containerStatus` tinyint NOT NULL,
  `containerCode` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_energyScan`
--

DROP TABLE IF EXISTS `v_energyScan`;
/*!50001 DROP VIEW IF EXISTS `v_energyScan`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_energyScan` (
  `energyScanId` tinyint NOT NULL,
  `sessionId` tinyint NOT NULL,
  `blSampleId` tinyint NOT NULL,
  `fluorescenceDetector` tinyint NOT NULL,
  `scanFileFullPath` tinyint NOT NULL,
  `choochFileFullPath` tinyint NOT NULL,
  `jpegChoochFileFullPath` tinyint NOT NULL,
  `element` tinyint NOT NULL,
  `startEnergy` tinyint NOT NULL,
  `endEnergy` tinyint NOT NULL,
  `transmissionFactor` tinyint NOT NULL,
  `exposureTime` tinyint NOT NULL,
  `synchrotronCurrent` tinyint NOT NULL,
  `temperature` tinyint NOT NULL,
  `peakEnergy` tinyint NOT NULL,
  `peakFPrime` tinyint NOT NULL,
  `peakFDoublePrime` tinyint NOT NULL,
  `inflectionEnergy` tinyint NOT NULL,
  `inflectionFPrime` tinyint NOT NULL,
  `inflectionFDoublePrime` tinyint NOT NULL,
  `xrayDose` tinyint NOT NULL,
  `startTime` tinyint NOT NULL,
  `endTime` tinyint NOT NULL,
  `edgeEnergy` tinyint NOT NULL,
  `filename` tinyint NOT NULL,
  `beamSizeVertical` tinyint NOT NULL,
  `beamSizeHorizontal` tinyint NOT NULL,
  `crystalClass` tinyint NOT NULL,
  `comments` tinyint NOT NULL,
  `flux` tinyint NOT NULL,
  `flux_end` tinyint NOT NULL,
  `BLSample_sampleId` tinyint NOT NULL,
  `name` tinyint NOT NULL,
  `code` tinyint NOT NULL,
  `acronym` tinyint NOT NULL,
  `BLSession_proposalId` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_hour`
--

DROP TABLE IF EXISTS `v_hour`;
/*!50001 DROP VIEW IF EXISTS `v_hour`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_hour` (
  `num` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_logonByHour`
--

DROP TABLE IF EXISTS `v_logonByHour`;
/*!50001 DROP VIEW IF EXISTS `v_logonByHour`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_logonByHour` (
  `Hour` tinyint NOT NULL,
  `Distinct logins` tinyint NOT NULL,
  `Total logins` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_logonByMonthDay`
--

DROP TABLE IF EXISTS `v_logonByMonthDay`;
/*!50001 DROP VIEW IF EXISTS `v_logonByMonthDay`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_logonByMonthDay` (
  `Day` tinyint NOT NULL,
  `Distinct logins` tinyint NOT NULL,
  `Total logins` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_logonByWeek`
--

DROP TABLE IF EXISTS `v_logonByWeek`;
/*!50001 DROP VIEW IF EXISTS `v_logonByWeek`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_logonByWeek` (
  `Week` tinyint NOT NULL,
  `Distinct logins` tinyint NOT NULL,
  `Total logins` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_logonByWeekDay`
--

DROP TABLE IF EXISTS `v_logonByWeekDay`;
/*!50001 DROP VIEW IF EXISTS `v_logonByWeekDay`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_logonByWeekDay` (
  `Day` tinyint NOT NULL,
  `Distinct logins` tinyint NOT NULL,
  `Total logins` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_monthDay`
--

DROP TABLE IF EXISTS `v_monthDay`;
/*!50001 DROP VIEW IF EXISTS `v_monthDay`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_monthDay` (
  `num` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_sample`
--

DROP TABLE IF EXISTS `v_sample`;
/*!50001 DROP VIEW IF EXISTS `v_sample`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_sample` (
  `proposalId` tinyint NOT NULL,
  `shippingId` tinyint NOT NULL,
  `dewarId` tinyint NOT NULL,
  `containerId` tinyint NOT NULL,
  `blSampleId` tinyint NOT NULL,
  `proposalCode` tinyint NOT NULL,
  `proposalNumber` tinyint NOT NULL,
  `creationDate` tinyint NOT NULL,
  `shippingType` tinyint NOT NULL,
  `barCode` tinyint NOT NULL,
  `shippingStatus` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_sampleByWeek`
--

DROP TABLE IF EXISTS `v_sampleByWeek`;
/*!50001 DROP VIEW IF EXISTS `v_sampleByWeek`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_sampleByWeek` (
  `Week` tinyint NOT NULL,
  `Samples` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_session`
--

DROP TABLE IF EXISTS `v_session`;
/*!50001 DROP VIEW IF EXISTS `v_session`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_session` (
  `sessionId` tinyint NOT NULL,
  `expSessionPk` tinyint NOT NULL,
  `beamLineSetupId` tinyint NOT NULL,
  `proposalId` tinyint NOT NULL,
  `projectCode` tinyint NOT NULL,
  `BLSession_startDate` tinyint NOT NULL,
  `BLSession_endDate` tinyint NOT NULL,
  `beamLineName` tinyint NOT NULL,
  `scheduled` tinyint NOT NULL,
  `nbShifts` tinyint NOT NULL,
  `comments` tinyint NOT NULL,
  `beamLineOperator` tinyint NOT NULL,
  `visit_number` tinyint NOT NULL,
  `bltimeStamp` tinyint NOT NULL,
  `usedFlag` tinyint NOT NULL,
  `sessionTitle` tinyint NOT NULL,
  `structureDeterminations` tinyint NOT NULL,
  `dewarTransport` tinyint NOT NULL,
  `databackupFrance` tinyint NOT NULL,
  `databackupEurope` tinyint NOT NULL,
  `operatorSiteNumber` tinyint NOT NULL,
  `BLSession_lastUpdate` tinyint NOT NULL,
  `BLSession_protectedData` tinyint NOT NULL,
  `Proposal_title` tinyint NOT NULL,
  `Proposal_proposalCode` tinyint NOT NULL,
  `Proposal_ProposalNumber` tinyint NOT NULL,
  `Proposal_ProposalType` tinyint NOT NULL,
  `Person_personId` tinyint NOT NULL,
  `Person_familyName` tinyint NOT NULL,
  `Person_givenName` tinyint NOT NULL,
  `Person_emailAddress` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_week`
--

DROP TABLE IF EXISTS `v_week`;
/*!50001 DROP VIEW IF EXISTS `v_week`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_week` (
  `num` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_weekDay`
--

DROP TABLE IF EXISTS `v_weekDay`;
/*!50001 DROP VIEW IF EXISTS `v_weekDay`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_weekDay` (
  `day` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_xfeFluorescenceSpectrum`
--

DROP TABLE IF EXISTS `v_xfeFluorescenceSpectrum`;
/*!50001 DROP VIEW IF EXISTS `v_xfeFluorescenceSpectrum`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `v_xfeFluorescenceSpectrum` (
  `xfeFluorescenceSpectrumId` tinyint NOT NULL,
  `sessionId` tinyint NOT NULL,
  `blSampleId` tinyint NOT NULL,
  `fittedDataFileFullPath` tinyint NOT NULL,
  `scanFileFullPath` tinyint NOT NULL,
  `jpegScanFileFullPath` tinyint NOT NULL,
  `startTime` tinyint NOT NULL,
  `endTime` tinyint NOT NULL,
  `filename` tinyint NOT NULL,
  `energy` tinyint NOT NULL,
  `exposureTime` tinyint NOT NULL,
  `beamTransmission` tinyint NOT NULL,
  `annotatedPymcaXfeSpectrum` tinyint NOT NULL,
  `beamSizeVertical` tinyint NOT NULL,
  `beamSizeHorizontal` tinyint NOT NULL,
  `crystalClass` tinyint NOT NULL,
  `comments` tinyint NOT NULL,
  `flux` tinyint NOT NULL,
  `flux_end` tinyint NOT NULL,
  `workingDirectory` tinyint NOT NULL,
  `BLSample_sampleId` tinyint NOT NULL,
  `BLSession_proposalId` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `V_AnalysisInfo`
--

/*!50001 DROP TABLE IF EXISTS `V_AnalysisInfo`*/;
/*!50001 DROP VIEW IF EXISTS `V_AnalysisInfo`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `V_AnalysisInfo` AS (select `exp`.`creationDate` AS `experimentCreationDate`,(select `r`.`timeStart` from `Run` `r` where (`r`.`runId` = `m`.`runId`)) AS `timeStart`,`dc`.`dataCollectionId` AS `dataCollectionId`,`m`.`measurementId` AS `measurementId`,`p`.`proposalId` AS `proposalId`,`p`.`proposalCode` AS `proposalCode`,`p`.`proposalNumber` AS `proposalNumber`,`m`.`priorityLevelId` AS `priorityLevelId`,`m`.`code` AS `code`,`m`.`exposureTemperature` AS `exposureTemperature`,`m`.`transmission` AS `transmission`,`m`.`comments` AS `measurementComments`,`exp`.`comments` AS `experimentComments`,`exp`.`experimentId` AS `experimentId`,`exp`.`experimentType` AS `experimentType`,`s`.`concentration` AS `conc`,`bu`.`acronym` AS `bufferAcronym`,`ma`.`acronym` AS `macromoleculeAcronym`,`bu`.`bufferId` AS `bufferId`,`ma`.`macromoleculeId` AS `macromoleculeId`,(select `su`.`substractedFilePath` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `subtractedFilePath`,(select `su`.`rgGuinier` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `rgGuinier`,(select `su`.`firstPointUsed` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `firstPointUsed`,(select `su`.`lastPointUsed` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `lastPointUsed`,(select distinct `su`.`I0` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `I0`,(select distinct `su`.`isagregated` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `isagregated`,(select distinct `su`.`subtractionId` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `subtractionId`,(select distinct `su`.`rgGnom` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `rgGnom`,(select distinct `su`.`total` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `total`,(select distinct `su`.`dmax` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `dmax`,(select distinct `su`.`volume` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `volume`,(select `su`.`I0Stdev` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `i0stdev`,(select `su`.`quality` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `quality`,(select `su`.`creationTime` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `substractionCreationTime`,(select `mtd2`.`measurementId` from `MeasurementToDataCollection` `mtd2` where ((`mtd`.`dataCollectionId` = `mtd2`.`dataCollectionId`) and (`mtd2`.`dataCollectionOrder` = 1))) AS `bufferBeforeMeasurementId`,(select `mtd2`.`measurementId` from `MeasurementToDataCollection` `mtd2` where ((`mtd`.`dataCollectionId` = `mtd2`.`dataCollectionId`) and (`mtd2`.`dataCollectionOrder` = 3))) AS `bufferAfterMeasurementId`,(select `m2`.`framesMerge` from `Merge` `m2` where ((`m2`.`measurementId` = `bufferBeforeMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `bufferBeforeMeasurementId`)))) AS `bufferBeforeFramesMerged`,(select `m2`.`mergeId` from `Merge` `m2` where ((`m2`.`measurementId` = `bufferBeforeMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `bufferBeforeMeasurementId`)))) AS `bufferBeforeMergeId`,(select `m2`.`averageFilePath` from `Merge` `m2` where ((`m2`.`measurementId` = `bufferBeforeMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `bufferBeforeMeasurementId`)))) AS `bufferBeforeAverageFilePath`,(select `mtd2`.`measurementId` from `MeasurementToDataCollection` `mtd2` where ((`mtd`.`dataCollectionId` = `mtd2`.`dataCollectionId`) and (`mtd2`.`dataCollectionOrder` = 2))) AS `sampleMeasurementId`,(select `m2`.`mergeId` from `Merge` `m2` where ((`m2`.`measurementId` = `sampleMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `sampleMeasurementId`)))) AS `sampleMergeId`,(select `m2`.`averageFilePath` from `Merge` `m2` where ((`m2`.`measurementId` = `sampleMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `sampleMeasurementId`)))) AS `averageFilePath`,(select `m2`.`framesMerge` from `Merge` `m2` where ((`m2`.`measurementId` = `sampleMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `sampleMeasurementId`)))) AS `framesMerge`,(select `m2`.`framesCount` from `Merge` `m2` where ((`m2`.`measurementId` = `bufferBeforeMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `bufferBeforeMeasurementId`)))) AS `framesCount`,(select `m2`.`framesMerge` from `Merge` `m2` where ((`m2`.`measurementId` = `bufferAfterMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `bufferAfterMeasurementId`)))) AS `bufferAfterFramesMerged`,(select `m2`.`mergeId` from `Merge` `m2` where ((`m2`.`measurementId` = `bufferAfterMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `bufferAfterMeasurementId`)))) AS `bufferAfterMergeId`,(select `m2`.`averageFilePath` from `Merge` `m2` where ((`m2`.`measurementId` = `bufferAfterMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `bufferAfterMeasurementId`)))) AS `bufferAfterAverageFilePath`,(select `modelList`.`modelListId` from (((`ModelList` `modelList` join `AbInitioModel` `ab`) join `SubtractionToAbInitioModel` `subToAb`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`modelList`.`modelListId` = `ab`.`modelListId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and `subToAb`.`subtractionId` and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `modelListId1`,(select `modelList`.`nsdFilePath` from (((`ModelList` `modelList` join `AbInitioModel` `ab`) join `SubtractionToAbInitioModel` `subToAb`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`modelList`.`modelListId` = `ab`.`modelListId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and `subToAb`.`subtractionId` and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `nsdFilePath`,(select `modelList`.`modelListId` from (((`ModelList` `modelList` join `AbInitioModel` `ab`) join `SubtractionToAbInitioModel` `subToAb`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`modelList`.`modelListId` = `ab`.`modelListId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and `subToAb`.`subtractionId` and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `modelListId2`,(select `modelList`.`chi2RgFilePath` from (((`ModelList` `modelList` join `AbInitioModel` `ab`) join `SubtractionToAbInitioModel` `subToAb`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`modelList`.`modelListId` = `ab`.`modelListId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `chi2RgFilePath`,(select `model`.`pdbFile` from (((`AbInitioModel` `ab` join `SubtractionToAbInitioModel` `subToAb`) join `Model` `model`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`model`.`modelId` = `ab`.`averagedModelId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `averagedModel`,(select `model`.`modelId` from (((`AbInitioModel` `ab` join `SubtractionToAbInitioModel` `subToAb`) join `Model` `model`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`model`.`modelId` = `ab`.`averagedModelId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `averagedModelId`,(select `model`.`pdbFile` from (((`AbInitioModel` `ab` join `SubtractionToAbInitioModel` `subToAb`) join `Model` `model`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`model`.`modelId` = `ab`.`rapidShapeDeterminationModelId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `rapidShapeDeterminationModel`,(select `model`.`modelId` from (((`AbInitioModel` `ab` join `SubtractionToAbInitioModel` `subToAb`) join `Model` `model`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`model`.`modelId` = `ab`.`rapidShapeDeterminationModelId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `rapidShapeDeterminationModelId`,(select `model`.`pdbFile` from (((`AbInitioModel` `ab` join `SubtractionToAbInitioModel` `subToAb`) join `Model` `model`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`model`.`modelId` = `ab`.`shapeDeterminationModelId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `shapeDeterminationModel`,(select `model`.`modelId` from (((`AbInitioModel` `ab` join `SubtractionToAbInitioModel` `subToAb`) join `Model` `model`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`model`.`modelId` = `ab`.`shapeDeterminationModelId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `shapeDeterminationModelId`,(select `ab`.`abInitioModelId` from ((`AbInitioModel` `ab` join `SubtractionToAbInitioModel` `subToAb`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `abInitioModelId`,(select `ab`.`comments` from ((`AbInitioModel` `ab` join `SubtractionToAbInitioModel` `subToAb`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `comments` from (((((((`Experiment` `exp` join `Buffer` `bu`) join `SaxsDataCollection` `dc`) join `Macromolecule` `ma`) join `MeasurementToDataCollection` `mtd`) join `Specimen` `s`) join `Measurement` `m`) join `Proposal` `p`) where ((`bu`.`bufferId` = `s`.`bufferId`) and (`exp`.`proposalId` = `p`.`proposalId`) and (`m`.`specimenId` = `s`.`specimenId`) and (`dc`.`dataCollectionId` = `mtd`.`dataCollectionId`) and (`mtd`.`measurementId` = `m`.`measurementId`) and (`s`.`macromoleculeId` = `ma`.`macromoleculeId`) and (`s`.`experimentId` = `exp`.`experimentId`) and (`exp`.`experimentType` <> 'TEMPLATE') and `p`.`proposalId` in (select distinct `prop`.`proposalId` from (`Proposal` `prop` join `Experiment` `exper`) where ((`exper`.`proposalId` = `prop`.`proposalId`) and (`prop`.`proposalId` <> 3374))))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_Log4Stat`
--

/*!50001 DROP TABLE IF EXISTS `v_Log4Stat`*/;
/*!50001 DROP VIEW IF EXISTS `v_Log4Stat`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_Log4Stat` AS select `s`.`id` AS `id`,`s`.`priority` AS `priority`,`s`.`timestamp` AS `timestamp`,`s`.`msg` AS `msg`,`s`.`detail` AS `detail`,`s`.`value` AS `value` from `Log4Stat` `s` where (((`s`.`detail` like 'fx%') or (`s`.`detail` like 'ifx%') or (`s`.`detail` like 'mx%') or (`s`.`detail` like 'ix%') or (`s`.`detail` like 'bm14u%') or (`s`.`detail` like 'bm161%') or (`s`.`detail` like 'bm162%')) and (`s`.`detail` <> 'fx999') and (`s`.`detail` <> 'ifx999') and (`s`.`detail` <> 'mx415')) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_datacollection`
--

/*!50001 DROP TABLE IF EXISTS `v_datacollection`*/;
/*!50001 DROP VIEW IF EXISTS `v_datacollection`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=MERGE */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_datacollection` AS select `DataCollection`.`dataCollectionId` AS `dataCollectionId`,`DataCollection`.`dataCollectionGroupId` AS `dataCollectionGroupId`,`DataCollection`.`strategySubWedgeOrigId` AS `strategySubWedgeOrigId`,`DataCollection`.`detectorId` AS `detectorId`,`DataCollection`.`blSubSampleId` AS `blSubSampleId`,`DataCollection`.`dataCollectionNumber` AS `dataCollectionNumber`,`DataCollection`.`startTime` AS `startTime`,`DataCollection`.`endTime` AS `endTime`,`DataCollection`.`runStatus` AS `runStatus`,`DataCollection`.`axisStart` AS `axisStart`,`DataCollection`.`axisEnd` AS `axisEnd`,`DataCollection`.`axisRange` AS `axisRange`,`DataCollection`.`overlap` AS `overlap`,`DataCollection`.`numberOfImages` AS `numberOfImages`,`DataCollection`.`startImageNumber` AS `startImageNumber`,`DataCollection`.`numberOfPasses` AS `numberOfPasses`,`DataCollection`.`exposureTime` AS `exposureTime`,`DataCollection`.`imageDirectory` AS `imageDirectory`,`DataCollection`.`imagePrefix` AS `imagePrefix`,`DataCollection`.`imageSuffix` AS `imageSuffix`,`DataCollection`.`fileTemplate` AS `fileTemplate`,`DataCollection`.`wavelength` AS `wavelength`,`DataCollection`.`resolution` AS `resolution`,`DataCollection`.`detectorDistance` AS `detectorDistance`,`DataCollection`.`xBeam` AS `xBeam`,`DataCollection`.`yBeam` AS `yBeam`,`DataCollection`.`xBeamPix` AS `xBeamPix`,`DataCollection`.`yBeamPix` AS `yBeamPix`,`DataCollection`.`comments` AS `comments`,`DataCollection`.`printableForReport` AS `printableForReport`,`DataCollection`.`slitGapVertical` AS `slitGapVertical`,`DataCollection`.`slitGapHorizontal` AS `slitGapHorizontal`,`DataCollection`.`transmission` AS `transmission`,`DataCollection`.`synchrotronMode` AS `synchrotronMode`,`DataCollection`.`xtalSnapshotFullPath1` AS `xtalSnapshotFullPath1`,`DataCollection`.`xtalSnapshotFullPath2` AS `xtalSnapshotFullPath2`,`DataCollection`.`xtalSnapshotFullPath3` AS `xtalSnapshotFullPath3`,`DataCollection`.`xtalSnapshotFullPath4` AS `xtalSnapshotFullPath4`,`DataCollection`.`rotationAxis` AS `rotationAxis`,`DataCollection`.`phiStart` AS `phiStart`,`DataCollection`.`kappaStart` AS `kappaStart`,`DataCollection`.`omegaStart` AS `omegaStart`,`DataCollection`.`resolutionAtCorner` AS `resolutionAtCorner`,`DataCollection`.`detector2Theta` AS `detector2Theta`,`DataCollection`.`undulatorGap1` AS `undulatorGap1`,`DataCollection`.`undulatorGap2` AS `undulatorGap2`,`DataCollection`.`undulatorGap3` AS `undulatorGap3`,`DataCollection`.`beamSizeAtSampleX` AS `beamSizeAtSampleX`,`DataCollection`.`beamSizeAtSampleY` AS `beamSizeAtSampleY`,`DataCollection`.`centeringMethod` AS `centeringMethod`,`DataCollection`.`averageTemperature` AS `averageTemperature`,`DataCollection`.`actualCenteringPosition` AS `actualCenteringPosition`,`DataCollection`.`beamShape` AS `beamShape`,`DataCollection`.`flux` AS `flux`,`DataCollection`.`flux_end` AS `flux_end`,`DataCollection`.`totalAbsorbedDose` AS `totalAbsorbedDose`,`DataCollection`.`bestWilsonPlotPath` AS `bestWilsonPlotPath`,`DataCollection`.`imageQualityIndicatorsPlotPath` AS `imageQualityIndicatorsPlotPath`,`DataCollection`.`imageQualityIndicatorsCSVPath` AS `imageQualityIndicatorsCSVPath`,`BLSession`.`sessionId` AS `sessionId`,`BLSession`.`proposalId` AS `proposalId`,`DataCollectionGroup`.`workflowId` AS `workflowId`,`ScreeningOutput`.`rankingResolution` AS `rankingResolution` from ((((`DataCollection` left join `DataCollectionGroup` on((`DataCollectionGroup`.`dataCollectionGroupId` = `DataCollection`.`dataCollectionGroupId`))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`))) left join `Screening` on((`Screening`.`dataCollectionId` = `DataCollection`.`dataCollectionId`))) left join `ScreeningOutput` on((`ScreeningOutput`.`screeningId` = `Screening`.`screeningId`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_datacollection_autoprocintegration`
--

/*!50001 DROP TABLE IF EXISTS `v_datacollection_autoprocintegration`*/;
/*!50001 DROP VIEW IF EXISTS `v_datacollection_autoprocintegration`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=MERGE */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_datacollection_autoprocintegration` AS select `AutoProcIntegration`.`autoProcIntegrationId` AS `v_datacollection_summary_phasing_autoProcIntegrationId`,`AutoProcIntegration`.`dataCollectionId` AS `v_datacollection_summary_phasing_dataCollectionId`,`AutoProcIntegration`.`cell_a` AS `v_datacollection_summary_phasing_cell_a`,`AutoProcIntegration`.`cell_b` AS `v_datacollection_summary_phasing_cell_b`,`AutoProcIntegration`.`cell_c` AS `v_datacollection_summary_phasing_cell_c`,`AutoProcIntegration`.`cell_alpha` AS `v_datacollection_summary_phasing_cell_alpha`,`AutoProcIntegration`.`cell_beta` AS `v_datacollection_summary_phasing_cell_beta`,`AutoProcIntegration`.`cell_gamma` AS `v_datacollection_summary_phasing_cell_gamma`,`AutoProcIntegration`.`anomalous` AS `v_datacollection_summary_phasing_anomalous`,`AutoProc`.`spaceGroup` AS `v_datacollection_summary_phasing_autoproc_space_group`,`AutoProc`.`autoProcId` AS `v_datacollection_summary_phasing_autoproc_autoprocId`,`AutoProcScaling`.`autoProcScalingId` AS `v_datacollection_summary_phasing_autoProcScalingId`,`AutoProcProgram`.`processingPrograms` AS `v_datacollection_processingPrograms`,`AutoProcProgram`.`autoProcProgramId` AS `v_datacollection_summary_phasing_autoProcProgramId`,`AutoProcProgram`.`processingStatus` AS `v_datacollection_processingStatus`,`BLSession`.`sessionId` AS `v_datacollection_summary_session_sessionId`,`BLSession`.`proposalId` AS `v_datacollection_summary_session_proposalId`,`AutoProcIntegration`.`dataCollectionId` AS `AutoProcIntegration_dataCollectionId`,`AutoProcIntegration`.`autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`,`PhasingStep`.`phasingStepType` AS `PhasingStep_phasing_phasingStepType`,`SpaceGroup`.`spaceGroupShortName` AS `SpaceGroup_spaceGroupShortName` from ((((((((((`AutoProcIntegration` left join `DataCollection` on((`DataCollection`.`dataCollectionId` = `AutoProcIntegration`.`dataCollectionId`))) left join `DataCollectionGroup` on((`DataCollection`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`))) left join `AutoProcScaling_has_Int` on((`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`))) left join `AutoProcScaling` on((`AutoProcScaling_has_Int`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `AutoProcProgram` on((`AutoProcProgram`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`))) left join `Phasing_has_Scaling` on((`Phasing_has_Scaling`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `PhasingStep` on((`PhasingStep`.`autoProcScalingId` = `Phasing_has_Scaling`.`autoProcScalingId`))) left join `SpaceGroup` on((`SpaceGroup`.`spaceGroupId` = `PhasingStep`.`spaceGroupId`))) left join `AutoProc` on((`AutoProc`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_datacollection_phasing`
--

/*!50001 DROP TABLE IF EXISTS `v_datacollection_phasing`*/;
/*!50001 DROP VIEW IF EXISTS `v_datacollection_phasing`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=MERGE */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_datacollection_phasing` AS select `PhasingStep`.`phasingStepId` AS `phasingStepId`,`PhasingStep`.`previousPhasingStepId` AS `previousPhasingStepId`,`PhasingStep`.`phasingAnalysisId` AS `phasingAnalysisId`,`AutoProcIntegration`.`autoProcIntegrationId` AS `autoProcIntegrationId`,`AutoProcIntegration`.`dataCollectionId` AS `dataCollectionId`,`AutoProcIntegration`.`anomalous` AS `anomalous`,`AutoProc`.`spaceGroup` AS `spaceGroup`,`AutoProc`.`autoProcId` AS `autoProcId`,`PhasingStep`.`phasingStepType` AS `phasingStepType`,`PhasingStep`.`method` AS `method`,`PhasingStep`.`solventContent` AS `solventContent`,`PhasingStep`.`enantiomorph` AS `enantiomorph`,`PhasingStep`.`lowRes` AS `lowRes`,`PhasingStep`.`highRes` AS `highRes`,`AutoProcScaling`.`autoProcScalingId` AS `autoProcScalingId`,`SpaceGroup`.`spaceGroupShortName` AS `spaceGroupShortName`,`AutoProcProgram`.`processingPrograms` AS `processingPrograms`,`AutoProcProgram`.`processingStatus` AS `processingStatus`,`PhasingProgramRun`.`phasingPrograms` AS `phasingPrograms`,`PhasingProgramRun`.`phasingStatus` AS `phasingStatus`,`PhasingProgramRun`.`phasingStartTime` AS `phasingStartTime`,`PhasingProgramRun`.`phasingEndTime` AS `phasingEndTime`,`DataCollectionGroup`.`sessionId` AS `sessionId`,`BLSession`.`proposalId` AS `proposalId`,`BLSample`.`blSampleId` AS `blSampleId`,`BLSample`.`name` AS `name`,`BLSample`.`code` AS `code`,`Protein`.`acronym` AS `acronym`,`Protein`.`proteinId` AS `proteinId` from (((((((((((((`AutoProcIntegration` left join `AutoProcScaling_has_Int` on((`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`))) left join `AutoProcScaling` on((`AutoProcScaling_has_Int`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `AutoProcProgram` on((`AutoProcProgram`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`))) left join `AutoProc` on((`AutoProc`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`))) left join `PhasingStep` on((`PhasingStep`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `PhasingProgramRun` on((`PhasingProgramRun`.`phasingProgramRunId` = `PhasingStep`.`programRunId`))) left join `DataCollection` on((`DataCollection`.`dataCollectionId` = `AutoProcIntegration`.`dataCollectionId`))) left join `DataCollectionGroup` on((`DataCollectionGroup`.`dataCollectionGroupId` = `DataCollection`.`dataCollectionGroupId`))) left join `BLSample` on((`BLSample`.`blSampleId` = `DataCollectionGroup`.`blSampleId`))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`))) left join `Crystal` on((`Crystal`.`crystalId` = `BLSample`.`crystalId`))) left join `Protein` on((`Crystal`.`proteinId` = `Protein`.`proteinId`))) left join `SpaceGroup` on((`SpaceGroup`.`spaceGroupId` = `PhasingStep`.`spaceGroupId`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_datacollection_phasing_program_run`
--

/*!50001 DROP TABLE IF EXISTS `v_datacollection_phasing_program_run`*/;
/*!50001 DROP VIEW IF EXISTS `v_datacollection_phasing_program_run`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=MERGE */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_datacollection_phasing_program_run` AS select `PhasingStep`.`phasingStepId` AS `phasingStepId`,`PhasingStep`.`previousPhasingStepId` AS `previousPhasingStepId`,`PhasingStep`.`phasingAnalysisId` AS `phasingAnalysisId`,`AutoProcIntegration`.`autoProcIntegrationId` AS `autoProcIntegrationId`,`AutoProcIntegration`.`dataCollectionId` AS `dataCollectionId`,`AutoProc`.`autoProcId` AS `autoProcId`,`PhasingStep`.`phasingStepType` AS `phasingStepType`,`PhasingStep`.`method` AS `method`,`AutoProcScaling`.`autoProcScalingId` AS `autoProcScalingId`,`SpaceGroup`.`spaceGroupShortName` AS `spaceGroupShortName`,`PhasingProgramRun`.`phasingPrograms` AS `phasingPrograms`,`PhasingProgramRun`.`phasingStatus` AS `phasingStatus`,`DataCollectionGroup`.`sessionId` AS `sessionId`,`BLSession`.`proposalId` AS `proposalId`,`BLSample`.`blSampleId` AS `blSampleId`,`BLSample`.`name` AS `name`,`BLSample`.`code` AS `code`,`Protein`.`acronym` AS `acronym`,`Protein`.`proteinId` AS `proteinId`,`PhasingProgramAttachment`.`phasingProgramAttachmentId` AS `phasingProgramAttachmentId`,`PhasingProgramAttachment`.`fileType` AS `fileType`,`PhasingProgramAttachment`.`fileName` AS `fileName`,`PhasingProgramAttachment`.`filePath` AS `filePath` from ((((((((((((((`AutoProcIntegration` left join `AutoProcScaling_has_Int` on((`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`))) left join `AutoProcScaling` on((`AutoProcScaling_has_Int`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `AutoProcProgram` on((`AutoProcProgram`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`))) left join `AutoProc` on((`AutoProc`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`))) left join `PhasingStep` on((`PhasingStep`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `PhasingProgramRun` on((`PhasingProgramRun`.`phasingProgramRunId` = `PhasingStep`.`programRunId`))) left join `DataCollection` on((`DataCollection`.`dataCollectionId` = `AutoProcIntegration`.`dataCollectionId`))) left join `DataCollectionGroup` on((`DataCollectionGroup`.`dataCollectionGroupId` = `DataCollection`.`dataCollectionGroupId`))) left join `BLSample` on((`BLSample`.`blSampleId` = `DataCollectionGroup`.`blSampleId`))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`))) left join `Crystal` on((`Crystal`.`crystalId` = `BLSample`.`crystalId`))) left join `Protein` on((`Crystal`.`proteinId` = `Protein`.`proteinId`))) left join `PhasingProgramAttachment` on((`PhasingProgramAttachment`.`phasingProgramRunId` = `PhasingProgramRun`.`phasingProgramRunId`))) left join `SpaceGroup` on((`SpaceGroup`.`spaceGroupId` = `PhasingStep`.`spaceGroupId`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_datacollection_summary`
--

/*!50001 DROP TABLE IF EXISTS `v_datacollection_summary`*/;
/*!50001 DROP VIEW IF EXISTS `v_datacollection_summary`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_datacollection_summary` AS select `DataCollectionGroup`.`dataCollectionGroupId` AS `DataCollectionGroup_dataCollectionGroupId`,`DataCollectionGroup`.`blSampleId` AS `DataCollectionGroup_blSampleId`,`DataCollectionGroup`.`sessionId` AS `DataCollectionGroup_sessionId`,`DataCollectionGroup`.`workflowId` AS `DataCollectionGroup_workflowId`,`DataCollectionGroup`.`experimentType` AS `DataCollectionGroup_experimentType`,`DataCollectionGroup`.`startTime` AS `DataCollectionGroup_startTime`,`DataCollectionGroup`.`endTime` AS `DataCollectionGroup_endTime`,`DataCollectionGroup`.`comments` AS `DataCollectionGroup_comments`,`DataCollectionGroup`.`actualSampleBarcode` AS `DataCollectionGroup_actualSampleBarcode`,`DataCollectionGroup`.`xtalSnapshotFullPath` AS `DataCollectionGroup_xtalSnapshotFullPath`,`Container`.`containerType` AS `containerType`,`Container`.`beamlineLocation` AS `beamlineLocation`,`Container`.`sampleChangerLocation` AS `sampleChangerLocation`,`Dewar`.`barCode` AS `barCode`,`BLSample`.`blSampleId` AS `BLSample_blSampleId`,`BLSample`.`crystalId` AS `BLSample_crystalId`,`BLSample`.`name` AS `BLSample_name`,`BLSample`.`code` AS `BLSample_code`,`BLSession`.`sessionId` AS `BLSession_sessionId`,`BLSession`.`proposalId` AS `BLSession_proposalId`,`BLSession`.`protectedData` AS `BLSession_protectedData`,`Protein`.`proteinId` AS `Protein_proteinId`,`Protein`.`name` AS `Protein_name`,`Protein`.`acronym` AS `Protein_acronym`,`DataCollection`.`dataCollectionId` AS `DataCollection_dataCollectionId`,`DataCollection`.`dataCollectionGroupId` AS `DataCollection_dataCollectionGroupId`,`DataCollection`.`startTime` AS `DataCollection_startTime`,`DataCollection`.`endTime` AS `DataCollection_endTime`,`DataCollection`.`runStatus` AS `DataCollection_runStatus`,`DataCollection`.`numberOfImages` AS `DataCollection_numberOfImages`,`DataCollection`.`startImageNumber` AS `DataCollection_startImageNumber`,`DataCollection`.`numberOfPasses` AS `DataCollection_numberOfPasses`,`DataCollection`.`exposureTime` AS `DataCollection_exposureTime`,`DataCollection`.`imageDirectory` AS `DataCollection_imageDirectory`,`DataCollection`.`wavelength` AS `DataCollection_wavelength`,`DataCollection`.`resolution` AS `DataCollection_resolution`,`DataCollection`.`detectorDistance` AS `DataCollection_detectorDistance`,`DataCollection`.`xBeam` AS `DataCollection_xBeam`,`DataCollection`.`transmission` AS `transmission`,`DataCollection`.`yBeam` AS `DataCollection_yBeam`,`DataCollection`.`imagePrefix` AS `DataCollection_imagePrefix`,`DataCollection`.`comments` AS `DataCollection_comments`,`DataCollection`.`xtalSnapshotFullPath1` AS `DataCollection_xtalSnapshotFullPath1`,`DataCollection`.`xtalSnapshotFullPath2` AS `DataCollection_xtalSnapshotFullPath2`,`DataCollection`.`xtalSnapshotFullPath3` AS `DataCollection_xtalSnapshotFullPath3`,`DataCollection`.`xtalSnapshotFullPath4` AS `DataCollection_xtalSnapshotFullPath4`,`DataCollection`.`phiStart` AS `DataCollection_phiStart`,`DataCollection`.`kappaStart` AS `DataCollection_kappaStart`,`DataCollection`.`omegaStart` AS `DataCollection_omegaStart`,`DataCollection`.`flux` AS `DataCollection_flux`,`DataCollection`.`flux_end` AS `DataCollection_flux_end`,`DataCollection`.`resolutionAtCorner` AS `DataCollection_resolutionAtCorner`,`DataCollection`.`bestWilsonPlotPath` AS `DataCollection_bestWilsonPlotPath`,`DataCollection`.`dataCollectionNumber` AS `DataCollection_dataCollectionNumber`,`DataCollection`.`axisRange` AS `DataCollection_axisRange`,`DataCollection`.`axisStart` AS `DataCollection_axisStart`,`DataCollection`.`axisEnd` AS `DataCollection_axisEnd`,`DataCollection`.`rotationAxis` AS `DataCollection_rotationAxis`,`Workflow`.`workflowTitle` AS `Workflow_workflowTitle`,`Workflow`.`workflowType` AS `Workflow_workflowType`,`Workflow`.`status` AS `Workflow_status`,`Workflow`.`workflowId` AS `Workflow_workflowId`,`v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_dataCollectionId` AS `AutoProcIntegration_dataCollectionId`,`v_datacollection_summary_autoprocintegration`.`autoProcScalingId` AS `autoProcScalingId`,`v_datacollection_summary_autoprocintegration`.`cell_a` AS `cell_a`,`v_datacollection_summary_autoprocintegration`.`cell_b` AS `cell_b`,`v_datacollection_summary_autoprocintegration`.`cell_c` AS `cell_c`,`v_datacollection_summary_autoprocintegration`.`cell_alpha` AS `cell_alpha`,`v_datacollection_summary_autoprocintegration`.`cell_beta` AS `cell_beta`,`v_datacollection_summary_autoprocintegration`.`cell_gamma` AS `cell_gamma`,`v_datacollection_summary_autoprocintegration`.`scalingStatisticsType` AS `scalingStatisticsType`,`v_datacollection_summary_autoprocintegration`.`resolutionLimitHigh` AS `resolutionLimitHigh`,`v_datacollection_summary_autoprocintegration`.`resolutionLimitLow` AS `resolutionLimitLow`,`v_datacollection_summary_autoprocintegration`.`completeness` AS `completeness`,`v_datacollection_summary_autoprocintegration`.`AutoProc_spaceGroup` AS `AutoProc_spaceGroup`,`v_datacollection_summary_autoprocintegration`.`autoProcId` AS `autoProcId`,`v_datacollection_summary_autoprocintegration`.`rMerge` AS `rMerge`,`v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`,`v_datacollection_summary_autoprocintegration`.`v_datacollection_summary_autoprocintegration_processingPrograms` AS `AutoProcProgram_processingPrograms`,`v_datacollection_summary_autoprocintegration`.`v_datacollection_summary_autoprocintegration_processingStatus` AS `AutoProcProgram_processingStatus`,`v_datacollection_summary_screening`.`Screening_screeningId` AS `Screening_screeningId`,`v_datacollection_summary_screening`.`Screening_dataCollectionId` AS `Screening_dataCollectionId`,`v_datacollection_summary_screening`.`ScreeningOutput_strategySuccess` AS `ScreeningOutput_strategySuccess`,`v_datacollection_summary_screening`.`ScreeningOutput_indexingSuccess` AS `ScreeningOutput_indexingSuccess`,`v_datacollection_summary_screening`.`ScreeningOutput_rankingResolution` AS `ScreeningOutput_rankingResolution`,`v_datacollection_summary_screening`.`ScreeningOutput_mosaicity` AS `ScreeningOutput_mosaicity`,`v_datacollection_summary_screening`.`ScreeningOutputLattice_spaceGroup` AS `ScreeningOutputLattice_spaceGroup`,`v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_a` AS `ScreeningOutputLattice_unitCell_a`,`v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_b` AS `ScreeningOutputLattice_unitCell_b`,`v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_c` AS `ScreeningOutputLattice_unitCell_c`,`v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_alpha` AS `ScreeningOutputLattice_unitCell_alpha`,`v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_beta` AS `ScreeningOutputLattice_unitCell_beta`,`v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_gamma` AS `ScreeningOutputLattice_unitCell_gamma`,`Shipping`.`shippingId` AS `shippingId`,`Detector`.`detectorType` AS `detectorType`,`Detector`.`detectorModel` AS `detectorModel`,`Detector`.`detectorManufacturer` AS `detectorManufacturer`,`Detector`.`detectorPixelSizeHorizontal` AS `detectorPixelSizeHorizontal`,`Detector`.`detectorPixelSizeVertical` AS `detectorPixelSizeVertical`,`Detector`.`detectorMode` AS `detectorMode`,`DiffractionPlan`.`observedResolution` AS `observedResolution`,`DiffractionPlan`.`minimalResolution` AS `minimalResolution`,`DiffractionPlan`.`exposureTime` AS `exposureTime`,`DiffractionPlan`.`oscillationRange` AS `oscillationRange`,`DiffractionPlan`.`maximalResolution` AS `maximalResolution`,`DiffractionPlan`.`screeningResolution` AS `screeningResolution`,`DiffractionPlan`.`radiationSensitivity` AS `radiationSensitivity`,`DiffractionPlan`.`anomalousScatterer` AS `anomalousScatterer`,`DiffractionPlan`.`preferredBeamSizeX` AS `preferredBeamSizeX`,`DiffractionPlan`.`preferredBeamSizeY` AS `preferredBeamSizeY`,`DiffractionPlan`.`preferredBeamDiameter` AS `preferredBeamDiameter`,`DiffractionPlan`.`forcedSpaceGroup` AS `forcedSpaceGroup`,`DiffractionPlan`.`numberOfPositions` AS `numberOfPositions` from (((((((((((((`DataCollectionGroup` left join `DataCollection` on(((`DataCollection`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`) and (`DataCollection`.`dataCollectionId` = (select max(`dc2`.`dataCollectionId`) from `DataCollection` `dc2` where (`dc2`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`)))))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`))) left join `BLSample` on((`BLSample`.`blSampleId` = `DataCollectionGroup`.`blSampleId`))) left join `Container` on((`Container`.`containerId` = `BLSample`.`containerId`))) left join `Dewar` on((`Container`.`dewarId` = `Dewar`.`dewarId`))) left join `Shipping` on((`Shipping`.`shippingId` = `Dewar`.`shippingId`))) left join `Crystal` on((`Crystal`.`crystalId` = `BLSample`.`crystalId`))) left join `Protein` on((`Protein`.`proteinId` = `Crystal`.`proteinId`))) left join `DiffractionPlan` on((`DiffractionPlan`.`diffractionPlanId` = `BLSample`.`diffractionPlanId`))) left join `Detector` on((`Detector`.`detectorId` = `DataCollection`.`detectorId`))) left join `Workflow` on((`DataCollectionGroup`.`workflowId` = `Workflow`.`workflowId`))) left join `v_datacollection_summary_autoprocintegration` on((`v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_dataCollectionId` = `DataCollection`.`dataCollectionId`))) left join `v_datacollection_summary_screening` on((`v_datacollection_summary_screening`.`Screening_dataCollectionId` = `DataCollection`.`dataCollectionId`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_datacollection_summary_autoprocintegration`
--

/*!50001 DROP TABLE IF EXISTS `v_datacollection_summary_autoprocintegration`*/;
/*!50001 DROP VIEW IF EXISTS `v_datacollection_summary_autoprocintegration`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=MERGE */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_datacollection_summary_autoprocintegration` AS select `AutoProcIntegration`.`dataCollectionId` AS `AutoProcIntegration_dataCollectionId`,`AutoProcIntegration`.`cell_a` AS `cell_a`,`AutoProcIntegration`.`cell_b` AS `cell_b`,`AutoProcIntegration`.`cell_c` AS `cell_c`,`AutoProcIntegration`.`cell_alpha` AS `cell_alpha`,`AutoProcIntegration`.`cell_beta` AS `cell_beta`,`AutoProcIntegration`.`cell_gamma` AS `cell_gamma`,`AutoProcIntegration`.`autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`,`AutoProcProgram`.`processingPrograms` AS `v_datacollection_summary_autoprocintegration_processingPrograms`,`AutoProcProgram`.`processingStatus` AS `v_datacollection_summary_autoprocintegration_processingStatus`,`AutoProcIntegration`.`dataCollectionId` AS `AutoProcIntegration_phasing_dataCollectionId`,`PhasingStep`.`phasingStepType` AS `PhasingStep_phasing_phasingStepType`,`SpaceGroup`.`spaceGroupShortName` AS `SpaceGroup_spaceGroupShortName`,`AutoProc`.`autoProcId` AS `autoProcId`,`AutoProc`.`spaceGroup` AS `AutoProc_spaceGroup`,`AutoProcScalingStatistics`.`scalingStatisticsType` AS `scalingStatisticsType`,`AutoProcScalingStatistics`.`resolutionLimitHigh` AS `resolutionLimitHigh`,`AutoProcScalingStatistics`.`resolutionLimitLow` AS `resolutionLimitLow`,`AutoProcScalingStatistics`.`rMerge` AS `rMerge`,`AutoProcScalingStatistics`.`completeness` AS `completeness`,`AutoProcScaling`.`autoProcScalingId` AS `autoProcScalingId` from ((((((((`AutoProcIntegration` left join `AutoProcProgram` on((`AutoProcIntegration`.`autoProcProgramId` = `AutoProcProgram`.`autoProcProgramId`))) left join `AutoProcScaling_has_Int` on((`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`))) left join `AutoProcScaling` on((`AutoProcScaling`.`autoProcScalingId` = `AutoProcScaling_has_Int`.`autoProcScalingId`))) left join `AutoProcScalingStatistics` on((`AutoProcScaling`.`autoProcScalingId` = `AutoProcScalingStatistics`.`autoProcScalingId`))) left join `AutoProc` on((`AutoProc`.`autoProcId` = `AutoProcScaling`.`autoProcId`))) left join `Phasing_has_Scaling` on((`Phasing_has_Scaling`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `PhasingStep` on((`PhasingStep`.`autoProcScalingId` = `Phasing_has_Scaling`.`autoProcScalingId`))) left join `SpaceGroup` on((`SpaceGroup`.`spaceGroupId` = `PhasingStep`.`spaceGroupId`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_datacollection_summary_datacollectiongroup`
--

/*!50001 DROP TABLE IF EXISTS `v_datacollection_summary_datacollectiongroup`*/;
/*!50001 DROP VIEW IF EXISTS `v_datacollection_summary_datacollectiongroup`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=MERGE */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_datacollection_summary_datacollectiongroup` AS select `DataCollectionGroup`.`dataCollectionGroupId` AS `DataCollectionGroup_dataCollectionGroupId`,`DataCollectionGroup`.`blSampleId` AS `DataCollectionGroup_blSampleId`,`DataCollectionGroup`.`sessionId` AS `DataCollectionGroup_sessionId`,`DataCollectionGroup`.`workflowId` AS `DataCollectionGroup_workflowId`,`DataCollectionGroup`.`experimentType` AS `DataCollectionGroup_experimentType`,`DataCollectionGroup`.`startTime` AS `DataCollectionGroup_startTime`,`DataCollectionGroup`.`endTime` AS `DataCollectionGroup_endTime`,`DataCollectionGroup`.`comments` AS `DataCollectionGroup_comments`,`DataCollectionGroup`.`actualSampleBarcode` AS `DataCollectionGroup_actualSampleBarcode`,`DataCollectionGroup`.`xtalSnapshotFullPath` AS `DataCollectionGroup_xtalSnapshotFullPath`,`BLSample`.`blSampleId` AS `BLSample_blSampleId`,`BLSample`.`crystalId` AS `BLSample_crystalId`,`BLSample`.`name` AS `BLSample_name`,`BLSample`.`code` AS `BLSample_code`,`BLSession`.`sessionId` AS `BLSession_sessionId`,`BLSession`.`proposalId` AS `BLSession_proposalId`,`BLSession`.`protectedData` AS `BLSession_protectedData`,`Protein`.`proteinId` AS `Protein_proteinId`,`Protein`.`name` AS `Protein_name`,`Protein`.`acronym` AS `Protein_acronym`,`DataCollection`.`dataCollectionId` AS `DataCollection_dataCollectionId`,`DataCollection`.`dataCollectionGroupId` AS `DataCollection_dataCollectionGroupId`,`DataCollection`.`startTime` AS `DataCollection_startTime`,`DataCollection`.`endTime` AS `DataCollection_endTime`,`DataCollection`.`runStatus` AS `DataCollection_runStatus`,`DataCollection`.`numberOfImages` AS `DataCollection_numberOfImages`,`DataCollection`.`startImageNumber` AS `DataCollection_startImageNumber`,`DataCollection`.`numberOfPasses` AS `DataCollection_numberOfPasses`,`DataCollection`.`exposureTime` AS `DataCollection_exposureTime`,`DataCollection`.`imageDirectory` AS `DataCollection_imageDirectory`,`DataCollection`.`wavelength` AS `DataCollection_wavelength`,`DataCollection`.`resolution` AS `DataCollection_resolution`,`DataCollection`.`detectorDistance` AS `DataCollection_detectorDistance`,`DataCollection`.`xBeam` AS `DataCollection_xBeam`,`DataCollection`.`yBeam` AS `DataCollection_yBeam`,`DataCollection`.`comments` AS `DataCollection_comments`,`DataCollection`.`xtalSnapshotFullPath1` AS `DataCollection_xtalSnapshotFullPath1`,`DataCollection`.`xtalSnapshotFullPath2` AS `DataCollection_xtalSnapshotFullPath2`,`DataCollection`.`xtalSnapshotFullPath3` AS `DataCollection_xtalSnapshotFullPath3`,`DataCollection`.`xtalSnapshotFullPath4` AS `DataCollection_xtalSnapshotFullPath4`,`DataCollection`.`phiStart` AS `DataCollection_phiStart`,`DataCollection`.`kappaStart` AS `DataCollection_kappaStart`,`DataCollection`.`omegaStart` AS `DataCollection_omegaStart`,`DataCollection`.`resolutionAtCorner` AS `DataCollection_resolutionAtCorner`,`DataCollection`.`bestWilsonPlotPath` AS `DataCollection_bestWilsonPlotPath`,`DataCollection`.`dataCollectionNumber` AS `DataCollection_dataCollectionNumber`,`DataCollection`.`axisRange` AS `DataCollection_axisRange`,`DataCollection`.`axisStart` AS `DataCollection_axisStart`,`DataCollection`.`axisEnd` AS `DataCollection_axisEnd`,`Workflow`.`workflowTitle` AS `Workflow_workflowTitle`,`Workflow`.`workflowType` AS `Workflow_workflowType`,`Workflow`.`status` AS `Workflow_status` from ((((((`DataCollectionGroup` left join `DataCollection` on(((`DataCollection`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`) and (`DataCollection`.`dataCollectionId` = (select max(`dc2`.`dataCollectionId`) from `DataCollection` `dc2` where (`dc2`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`)))))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`))) left join `BLSample` on((`BLSample`.`blSampleId` = `DataCollectionGroup`.`blSampleId`))) left join `Crystal` on((`Crystal`.`crystalId` = `BLSample`.`crystalId`))) left join `Protein` on((`Protein`.`proteinId` = `Crystal`.`proteinId`))) left join `Workflow` on((`DataCollectionGroup`.`workflowId` = `Workflow`.`workflowId`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_datacollection_summary_phasing`
--

/*!50001 DROP TABLE IF EXISTS `v_datacollection_summary_phasing`*/;
/*!50001 DROP VIEW IF EXISTS `v_datacollection_summary_phasing`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=MERGE */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_datacollection_summary_phasing` AS select `AutoProcIntegration`.`autoProcIntegrationId` AS `v_datacollection_summary_phasing_autoProcIntegrationId`,`AutoProcIntegration`.`dataCollectionId` AS `v_datacollection_summary_phasing_dataCollectionId`,`AutoProcIntegration`.`cell_a` AS `v_datacollection_summary_phasing_cell_a`,`AutoProcIntegration`.`cell_b` AS `v_datacollection_summary_phasing_cell_b`,`AutoProcIntegration`.`cell_c` AS `v_datacollection_summary_phasing_cell_c`,`AutoProcIntegration`.`cell_alpha` AS `v_datacollection_summary_phasing_cell_alpha`,`AutoProcIntegration`.`cell_beta` AS `v_datacollection_summary_phasing_cell_beta`,`AutoProcIntegration`.`cell_gamma` AS `v_datacollection_summary_phasing_cell_gamma`,`AutoProcIntegration`.`anomalous` AS `v_datacollection_summary_phasing_anomalous`,`AutoProc`.`spaceGroup` AS `v_datacollection_summary_phasing_autoproc_space_group`,`AutoProc`.`autoProcId` AS `v_datacollection_summary_phasing_autoproc_autoprocId`,`AutoProcScaling`.`autoProcScalingId` AS `v_datacollection_summary_phasing_autoProcScalingId`,`AutoProcProgram`.`processingPrograms` AS `v_datacollection_summary_phasing_processingPrograms`,`AutoProcProgram`.`autoProcProgramId` AS `v_datacollection_summary_phasing_autoProcProgramId`,`AutoProcProgram`.`processingStatus` AS `v_datacollection_summary_phasing_processingStatus`,`BLSession`.`sessionId` AS `v_datacollection_summary_session_sessionId`,`BLSession`.`proposalId` AS `v_datacollection_summary_session_proposalId` from (((((((`AutoProcIntegration` left join `DataCollection` on((`DataCollection`.`dataCollectionId` = `AutoProcIntegration`.`dataCollectionId`))) left join `DataCollectionGroup` on((`DataCollection`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`))) left join `AutoProcScaling_has_Int` on((`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`))) left join `AutoProcScaling` on((`AutoProcScaling_has_Int`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `AutoProcProgram` on((`AutoProcProgram`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`))) left join `AutoProc` on((`AutoProc`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_datacollection_summary_screening`
--

/*!50001 DROP TABLE IF EXISTS `v_datacollection_summary_screening`*/;
/*!50001 DROP VIEW IF EXISTS `v_datacollection_summary_screening`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=MERGE */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_datacollection_summary_screening` AS select `Screening`.`screeningId` AS `Screening_screeningId`,`Screening`.`dataCollectionId` AS `Screening_dataCollectionId`,`ScreeningOutput`.`strategySuccess` AS `ScreeningOutput_strategySuccess`,`ScreeningOutput`.`indexingSuccess` AS `ScreeningOutput_indexingSuccess`,`ScreeningOutput`.`rankingResolution` AS `ScreeningOutput_rankingResolution`,`ScreeningOutput`.`mosaicityEstimated` AS `ScreeningOutput_mosaicityEstimated`,`ScreeningOutput`.`mosaicity` AS `ScreeningOutput_mosaicity`,`ScreeningOutputLattice`.`spaceGroup` AS `ScreeningOutputLattice_spaceGroup`,`ScreeningOutputLattice`.`unitCell_a` AS `ScreeningOutputLattice_unitCell_a`,`ScreeningOutputLattice`.`unitCell_b` AS `ScreeningOutputLattice_unitCell_b`,`ScreeningOutputLattice`.`unitCell_c` AS `ScreeningOutputLattice_unitCell_c`,`ScreeningOutputLattice`.`unitCell_alpha` AS `ScreeningOutputLattice_unitCell_alpha`,`ScreeningOutputLattice`.`unitCell_beta` AS `ScreeningOutputLattice_unitCell_beta`,`ScreeningOutputLattice`.`unitCell_gamma` AS `ScreeningOutputLattice_unitCell_gamma` from ((`Screening` left join `ScreeningOutput` on((`Screening`.`screeningId` = `ScreeningOutput`.`screeningId`))) left join `ScreeningOutputLattice` on((`ScreeningOutput`.`screeningOutputId` = `ScreeningOutputLattice`.`screeningOutputId`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_dewar`
--

/*!50001 DROP TABLE IF EXISTS `v_dewar`*/;
/*!50001 DROP VIEW IF EXISTS `v_dewar`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_dewar` AS select `p`.`proposalId` AS `proposalId`,`s`.`shippingId` AS `shippingId`,`s`.`shippingName` AS `shippingName`,`d`.`dewarId` AS `dewarId`,`d`.`code` AS `dewarName`,`d`.`dewarStatus` AS `dewarStatus`,`p`.`proposalCode` AS `proposalCode`,`p`.`proposalNumber` AS `proposalNumber`,`s`.`creationDate` AS `creationDate`,`s`.`shippingType` AS `shippingType`,`d`.`barCode` AS `barCode`,`s`.`shippingStatus` AS `shippingStatus`,`ss`.`beamLineName` AS `beamLineName`,count(distinct `h1`.`DewarTransportHistoryId`) AS `nbEvents`,count(distinct `h2`.`DewarTransportHistoryId`) AS `storesin`,count(if((`bls`.`code` is not null),1,NULL)) AS `nbSamples` from (((((((`Proposal` `p` join `Shipping` `s` on((`s`.`proposalId` = `p`.`proposalId`))) join `Dewar` `d` on((`d`.`shippingId` = `s`.`shippingId`))) left join `Container` `c` on((`c`.`dewarId` = `d`.`dewarId`))) left join `BLSession` `ss` on((`ss`.`sessionId` = `d`.`firstExperimentId`))) left join `BLSample` `bls` on((`bls`.`containerId` = `c`.`containerId`))) left join `DewarTransportHistory` `h1` on(((`h1`.`dewarId` = `d`.`dewarId`) and ((`h1`.`dewarStatus` = 'at ESRF') or (`h1`.`dewarStatus` = 'sent to User'))))) left join `DewarTransportHistory` `h2` on(((`h2`.`dewarId` = `d`.`dewarId`) and (`h2`.`storageLocation` = 'STORES-IN')))) where (((`p`.`proposalCode` like 'MX%') or (`p`.`proposalCode` like 'FX%') or (`p`.`proposalCode` like 'IFX%') or (`p`.`proposalCode` like 'IX%') or (`p`.`proposalCode` like 'BM14U%') or (`p`.`proposalCode` like 'bm161%') or (`p`.`proposalCode` like 'bm162%')) and ((`p`.`proposalCode` <> 'FX') or (`p`.`proposalNumber` <> '999')) and ((`p`.`proposalCode` <> 'IFX') or (`p`.`proposalNumber` <> '999')) and ((`p`.`proposalCode` <> 'MX') or (`p`.`proposalNumber` <> '415')) and (`d`.`type` = 'Dewar') and (`s`.`creationDate` is not null)) group by `d`.`dewarId` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_dewarBeamline`
--

/*!50001 DROP TABLE IF EXISTS `v_dewarBeamline`*/;
/*!50001 DROP VIEW IF EXISTS `v_dewarBeamline`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_dewarBeamline` AS select `d`.`beamLineName` AS `beamLineName`,count(0) AS `COUNT(*)` from `v_dewar` `d` where (`d`.`beamLineName` is not null) group by `d`.`beamLineName` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_dewarBeamlineByWeek`
--

/*!50001 DROP TABLE IF EXISTS `v_dewarBeamlineByWeek`*/;
/*!50001 DROP VIEW IF EXISTS `v_dewarBeamlineByWeek`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_dewarBeamlineByWeek` AS select substr(`w`.`num`,6) AS `Week`,count(if((`d`.`beamLineName` like 'ID14%'),1,NULL)) AS `ID14`,count(if((`d`.`beamLineName` like 'ID23%'),1,NULL)) AS `ID23`,count(if((`d`.`beamLineName` like 'ID29%'),1,NULL)) AS `ID29`,count(if((`d`.`beamLineName` like 'BM14%'),1,NULL)) AS `BM14` from (`v_week` `w` left join `v_dewar` `d` on((`w`.`num` = date_format(`d`.`creationDate`,'%x-%v')))) group by `w`.`num` order by `w`.`num` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_dewarByWeek`
--

/*!50001 DROP TABLE IF EXISTS `v_dewarByWeek`*/;
/*!50001 DROP VIEW IF EXISTS `v_dewarByWeek`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_dewarByWeek` AS select substr(`w`.`num`,6) AS `Week`,count(if((`d`.`shippingType` = 'DewarTracking'),1,NULL)) AS `Dewars Tracked`,count(if(((`d`.`proposalCode` is not null) and ((`d`.`shippingType` <> 'DewarTracking') or isnull(`d`.`shippingType`))),1,NULL)) AS `Dewars Non-Tracked` from (`v_week` `w` left join `v_dewar` `d` on((`w`.`num` = date_format(`d`.`creationDate`,'%Y-%v')))) group by substr(`w`.`num`,6) order by `w`.`num` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_dewarByWeekTotal`
--

/*!50001 DROP TABLE IF EXISTS `v_dewarByWeekTotal`*/;
/*!50001 DROP VIEW IF EXISTS `v_dewarByWeekTotal`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_dewarByWeekTotal` AS select substr(`w`.`num`,6) AS `Week`,count(if((`d`.`shippingType` = 'DewarTracking'),1,NULL)) AS `Dewars Tracked`,count(if(((`d`.`proposalCode` is not null) and ((`d`.`shippingType` <> 'DewarTracking') or isnull(`d`.`shippingType`))),1,NULL)) AS `Dewars Non-Tracked`,count(if((`d`.`proposalCode` is not null),1,NULL)) AS `Total` from (`v_week` `w` left join `v_dewar` `d` on((`w`.`num` = date_format(`d`.`creationDate`,'%Y-%v')))) group by substr(`w`.`num`,6) order by substr(`w`.`num`,6) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_dewarList`
--

/*!50001 DROP TABLE IF EXISTS `v_dewarList`*/;
/*!50001 DROP VIEW IF EXISTS `v_dewarList`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_dewarList` AS select concat(`d`.`proposalCode`,`d`.`proposalNumber`) AS `proposal`,`d`.`shippingName` AS `shippingName`,`d`.`dewarName` AS `dewarName`,`d`.`barCode` AS `barCode`,date_format(`d`.`creationDate`,'%d/%m/%Y') AS `creationDate`,`d`.`shippingType` AS `shippingType`,count(distinct `h`.`DewarTransportHistoryId`) AS `nbEvents`,`d`.`dewarStatus` AS `dewarStatus`,`d`.`shippingStatus` AS `shippingStatus`,count(if((`bls`.`blSampleId` is not null),1,NULL)) AS `nbSamples` from (((`v_dewar` `d` left join `Container` `c` on((`c`.`dewarId` = `d`.`dewarId`))) left join `BLSample` `bls` on((`bls`.`containerId` = `c`.`containerId`))) left join `DewarTransportHistory` `h` on((`h`.`dewarId` = `d`.`dewarId`))) group by `d`.`dewarId` order by `d`.`creationDate` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_dewarProposalCode`
--

/*!50001 DROP TABLE IF EXISTS `v_dewarProposalCode`*/;
/*!50001 DROP VIEW IF EXISTS `v_dewarProposalCode`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_dewarProposalCode` AS select `d`.`proposalCode` AS `proposalCode`,count(0) AS `COUNT(*)` from `v_dewar` `d` group by `d`.`proposalCode` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_dewarProposalCodeByWeek`
--

/*!50001 DROP TABLE IF EXISTS `v_dewarProposalCodeByWeek`*/;
/*!50001 DROP VIEW IF EXISTS `v_dewarProposalCodeByWeek`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_dewarProposalCodeByWeek` AS select substr(`w`.`num`,6) AS `Week`,count(if((`d`.`proposalCode` = 'MX'),1,NULL)) AS `MX`,count(if((`d`.`proposalCode` = 'FX'),1,NULL)) AS `FX`,count(if((`d`.`proposalCode` = 'BM14U'),1,NULL)) AS `BM14U`,count(if((`d`.`proposalCode` = 'BM161'),1,NULL)) AS `BM161`,count(if((`d`.`proposalCode` = 'BM162'),1,NULL)) AS `BM162`,count(if(((`d`.`proposalCode` <> 'MX') and (`d`.`proposalCode` <> 'FX') and (`d`.`proposalCode` <> 'BM14U') and (`d`.`proposalCode` <> 'BM161') and (`d`.`proposalCode` <> 'BM162')),1,NULL)) AS `Others` from (`v_week` `w` left join `v_dewar` `d` on((`w`.`num` = date_format(`d`.`creationDate`,'%Y-%v')))) group by substr(`w`.`num`,6) order by substr(`w`.`num`,6) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_dewar_summary`
--

/*!50001 DROP TABLE IF EXISTS `v_dewar_summary`*/;
/*!50001 DROP VIEW IF EXISTS `v_dewar_summary`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=MERGE */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_dewar_summary` AS select `Shipping`.`shippingName` AS `shippingName`,`Shipping`.`deliveryAgent_agentName` AS `deliveryAgent_agentName`,`Shipping`.`deliveryAgent_shippingDate` AS `deliveryAgent_shippingDate`,`Shipping`.`deliveryAgent_deliveryDate` AS `deliveryAgent_deliveryDate`,`Shipping`.`deliveryAgent_agentCode` AS `deliveryAgent_agentCode`,`Shipping`.`deliveryAgent_flightCode` AS `deliveryAgent_flightCode`,`Shipping`.`shippingStatus` AS `shippingStatus`,`Shipping`.`bltimeStamp` AS `bltimeStamp`,`Shipping`.`laboratoryId` AS `laboratoryId`,`Shipping`.`isStorageShipping` AS `isStorageShipping`,`Shipping`.`creationDate` AS `creationDate`,`Shipping`.`comments` AS `Shipping_comments`,`Shipping`.`sendingLabContactId` AS `sendingLabContactId`,`Shipping`.`returnLabContactId` AS `returnLabContactId`,`Shipping`.`returnCourier` AS `returnCourier`,`Shipping`.`dateOfShippingToUser` AS `dateOfShippingToUser`,`Shipping`.`shippingType` AS `shippingType`,`Dewar`.`dewarId` AS `dewarId`,`Dewar`.`shippingId` AS `shippingId`,`Dewar`.`code` AS `dewarCode`,`Dewar`.`comments` AS `comments`,`Dewar`.`storageLocation` AS `storageLocation`,`Dewar`.`dewarStatus` AS `dewarStatus`,`Dewar`.`isStorageDewar` AS `isStorageDewar`,`Dewar`.`barCode` AS `barCode`,`Dewar`.`firstExperimentId` AS `firstExperimentId`,`Dewar`.`customsValue` AS `customsValue`,`Dewar`.`transportValue` AS `transportValue`,`Dewar`.`trackingNumberToSynchrotron` AS `trackingNumberToSynchrotron`,`Dewar`.`trackingNumberFromSynchrotron` AS `trackingNumberFromSynchrotron`,`Dewar`.`type` AS `type`,`BLSession`.`sessionId` AS `sessionId`,`BLSession`.`beamLineName` AS `beamlineName`,`BLSession`.`startDate` AS `sessionStartDate`,`BLSession`.`endDate` AS `sessionEndDate`,`BLSession`.`beamLineOperator` AS `beamLineOperator`,`Proposal`.`proposalId` AS `proposalId`,`Container`.`containerId` AS `containerId`,`Container`.`containerType` AS `containerType`,`Container`.`capacity` AS `capacity`,`Container`.`beamlineLocation` AS `beamlineLocation`,`Container`.`sampleChangerLocation` AS `sampleChangerLocation`,`Container`.`containerStatus` AS `containerStatus`,`Container`.`code` AS `containerCode` from ((((`Dewar` left join `Container` on((`Container`.`dewarId` = `Dewar`.`dewarId`))) left join `Shipping` on((`Shipping`.`shippingId` = `Dewar`.`shippingId`))) left join `BLSession` on((`Dewar`.`firstExperimentId` = `BLSession`.`sessionId`))) left join `Proposal` on((`Proposal`.`proposalId` = `BLSession`.`proposalId`))) order by `Dewar`.`dewarId` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_energyScan`
--

/*!50001 DROP TABLE IF EXISTS `v_energyScan`*/;
/*!50001 DROP VIEW IF EXISTS `v_energyScan`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=MERGE */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_energyScan` AS select `EnergyScan`.`energyScanId` AS `energyScanId`,`EnergyScan`.`sessionId` AS `sessionId`,`EnergyScan`.`blSampleId` AS `blSampleId`,`EnergyScan`.`fluorescenceDetector` AS `fluorescenceDetector`,`EnergyScan`.`scanFileFullPath` AS `scanFileFullPath`,`EnergyScan`.`choochFileFullPath` AS `choochFileFullPath`,`EnergyScan`.`jpegChoochFileFullPath` AS `jpegChoochFileFullPath`,`EnergyScan`.`element` AS `element`,`EnergyScan`.`startEnergy` AS `startEnergy`,`EnergyScan`.`endEnergy` AS `endEnergy`,`EnergyScan`.`transmissionFactor` AS `transmissionFactor`,`EnergyScan`.`exposureTime` AS `exposureTime`,`EnergyScan`.`synchrotronCurrent` AS `synchrotronCurrent`,`EnergyScan`.`temperature` AS `temperature`,`EnergyScan`.`peakEnergy` AS `peakEnergy`,`EnergyScan`.`peakFPrime` AS `peakFPrime`,`EnergyScan`.`peakFDoublePrime` AS `peakFDoublePrime`,`EnergyScan`.`inflectionEnergy` AS `inflectionEnergy`,`EnergyScan`.`inflectionFPrime` AS `inflectionFPrime`,`EnergyScan`.`inflectionFDoublePrime` AS `inflectionFDoublePrime`,`EnergyScan`.`xrayDose` AS `xrayDose`,`EnergyScan`.`startTime` AS `startTime`,`EnergyScan`.`endTime` AS `endTime`,`EnergyScan`.`edgeEnergy` AS `edgeEnergy`,`EnergyScan`.`filename` AS `filename`,`EnergyScan`.`beamSizeVertical` AS `beamSizeVertical`,`EnergyScan`.`beamSizeHorizontal` AS `beamSizeHorizontal`,`EnergyScan`.`crystalClass` AS `crystalClass`,`EnergyScan`.`comments` AS `comments`,`EnergyScan`.`flux` AS `flux`,`EnergyScan`.`flux_end` AS `flux_end`,`BLSample`.`blSampleId` AS `BLSample_sampleId`,`BLSample`.`name` AS `name`,`BLSample`.`code` AS `code`,`Protein`.`acronym` AS `acronym`,`BLSession`.`proposalId` AS `BLSession_proposalId` from ((((`EnergyScan` left join `BLSample` on((`BLSample`.`blSampleId` = `EnergyScan`.`blSampleId`))) left join `Crystal` on((`Crystal`.`crystalId` = `BLSample`.`crystalId`))) left join `Protein` on((`Protein`.`proteinId` = `Crystal`.`proteinId`))) left join `BLSession` on((`BLSession`.`sessionId` = `EnergyScan`.`sessionId`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_hour`
--

/*!50001 DROP TABLE IF EXISTS `v_hour`*/;
/*!50001 DROP VIEW IF EXISTS `v_hour`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_hour` AS select date_format((now() - interval 23 hour),_utf8'%Y-%m-%d %H') AS `num` union select date_format((now() - interval 22 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 22 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 21 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 21 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 20 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 20 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 19 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 19 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 18 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 18 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 17 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 17 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 16 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 16 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 15 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 15 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 14 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 14 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 13 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 13 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 12 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 12 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 11 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 11 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 10 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 10 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 9 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 9 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 8 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 8 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 7 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 7 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 6 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 6 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 5 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 5 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 4 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 4 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 3 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 3 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 2 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 2 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 1 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 1 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 0 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 0 HOUR),'%Y-%m-%d %H')` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_logonByHour`
--

/*!50001 DROP TABLE IF EXISTS `v_logonByHour`*/;
/*!50001 DROP VIEW IF EXISTS `v_logonByHour`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_logonByHour` AS select date_format(`w`.`num`,'%H') AS `Hour`,count(distinct `s`.`detail`) AS `Distinct logins`,(count(`s`.`detail`) - count(distinct `s`.`detail`)) AS `Total logins` from (`v_hour` `w` left join `v_Log4Stat` `s` on(((`w`.`num` = date_format(`s`.`timestamp`,'%Y-%m-%d %H')) and (`s`.`msg` = 'LOGON')))) group by date_format(`w`.`num`,'%H') order by `w`.`num` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_logonByMonthDay`
--

/*!50001 DROP TABLE IF EXISTS `v_logonByMonthDay`*/;
/*!50001 DROP VIEW IF EXISTS `v_logonByMonthDay`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_logonByMonthDay` AS select date_format(`w`.`num`,'%d/%m') AS `Day`,count(distinct `s`.`detail`) AS `Distinct logins`,(count(`s`.`detail`) - count(distinct `s`.`detail`)) AS `Total logins` from (`v_monthDay` `w` left join `v_Log4Stat` `s` on(((`w`.`num` = date_format(`s`.`timestamp`,'%Y-%m-%d')) and (`s`.`msg` = 'LOGON')))) group by date_format(`w`.`num`,'%d/%m') order by `w`.`num` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_logonByWeek`
--

/*!50001 DROP TABLE IF EXISTS `v_logonByWeek`*/;
/*!50001 DROP VIEW IF EXISTS `v_logonByWeek`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_logonByWeek` AS select substr(`w`.`num`,6) AS `Week`,count(distinct `s`.`detail`) AS `Distinct logins`,(count(`s`.`detail`) - count(distinct `s`.`detail`)) AS `Total logins` from (`v_week` `w` left join `v_Log4Stat` `s` on(((`w`.`num` = date_format(`s`.`timestamp`,'%Y-%v')) and (`s`.`msg` = 'LOGON')))) group by substr(`w`.`num`,6) order by `w`.`num` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_logonByWeekDay`
--

/*!50001 DROP TABLE IF EXISTS `v_logonByWeekDay`*/;
/*!50001 DROP VIEW IF EXISTS `v_logonByWeekDay`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_logonByWeekDay` AS select date_format(`w`.`day`,'%W') AS `Day`,count(distinct `s`.`detail`) AS `Distinct logins`,(count(`s`.`detail`) - count(distinct `s`.`detail`)) AS `Total logins` from (`v_weekDay` `w` left join `v_Log4Stat` `s` on(((`w`.`day` = date_format(`s`.`timestamp`,'%Y-%m-%d')) and (`s`.`msg` = 'LOGON')))) group by `w`.`day` order by `w`.`day` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_monthDay`
--

/*!50001 DROP TABLE IF EXISTS `v_monthDay`*/;
/*!50001 DROP VIEW IF EXISTS `v_monthDay`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_monthDay` AS select date_format((now() - interval 31 day),_utf8'%Y-%m-%d') AS `num` union select date_format((now() - interval 30 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 30 DAY),'%Y-%m-%d')` union select date_format((now() - interval 29 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 29 DAY),'%Y-%m-%d')` union select date_format((now() - interval 28 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 28 DAY),'%Y-%m-%d')` union select date_format((now() - interval 27 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 27 DAY),'%Y-%m-%d')` union select date_format((now() - interval 26 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 26 DAY),'%Y-%m-%d')` union select date_format((now() - interval 25 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 25 DAY),'%Y-%m-%d')` union select date_format((now() - interval 24 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 24 DAY),'%Y-%m-%d')` union select date_format((now() - interval 23 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 23 DAY),'%Y-%m-%d')` union select date_format((now() - interval 22 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 22 DAY),'%Y-%m-%d')` union select date_format((now() - interval 21 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 21 DAY),'%Y-%m-%d')` union select date_format((now() - interval 20 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 20 DAY),'%Y-%m-%d')` union select date_format((now() - interval 19 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 19 DAY),'%Y-%m-%d')` union select date_format((now() - interval 18 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 18 DAY),'%Y-%m-%d')` union select date_format((now() - interval 17 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 17 DAY),'%Y-%m-%d')` union select date_format((now() - interval 16 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 16 DAY),'%Y-%m-%d')` union select date_format((now() - interval 15 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 15 DAY),'%Y-%m-%d')` union select date_format((now() - interval 14 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 14 DAY),'%Y-%m-%d')` union select date_format((now() - interval 13 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 13 DAY),'%Y-%m-%d')` union select date_format((now() - interval 12 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 12 DAY),'%Y-%m-%d')` union select date_format((now() - interval 11 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 11 DAY),'%Y-%m-%d')` union select date_format((now() - interval 10 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 10 DAY),'%Y-%m-%d')` union select date_format((now() - interval 9 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 9 DAY),'%Y-%m-%d')` union select date_format((now() - interval 8 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 8 DAY),'%Y-%m-%d')` union select date_format((now() - interval 7 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 7 DAY),'%Y-%m-%d')` union select date_format((now() - interval 6 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 6 DAY),'%Y-%m-%d')` union select date_format((now() - interval 5 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 5 DAY),'%Y-%m-%d')` union select date_format((now() - interval 4 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 4 DAY),'%Y-%m-%d')` union select date_format((now() - interval 3 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 3 DAY),'%Y-%m-%d')` union select date_format((now() - interval 2 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 2 DAY),'%Y-%m-%d')` union select date_format((now() - interval 1 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 1 DAY),'%Y-%m-%d')` union select date_format((now() - interval 0 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 0 DAY),'%Y-%m-%d')` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_sample`
--

/*!50001 DROP TABLE IF EXISTS `v_sample`*/;
/*!50001 DROP VIEW IF EXISTS `v_sample`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_sample` AS select `d`.`proposalId` AS `proposalId`,`d`.`shippingId` AS `shippingId`,`d`.`dewarId` AS `dewarId`,`c`.`containerId` AS `containerId`,`bls`.`blSampleId` AS `blSampleId`,`d`.`proposalCode` AS `proposalCode`,`d`.`proposalNumber` AS `proposalNumber`,`d`.`creationDate` AS `creationDate`,`d`.`shippingType` AS `shippingType`,`d`.`barCode` AS `barCode`,`d`.`shippingStatus` AS `shippingStatus` from ((`BLSample` `bls` join `Container` `c` on((`c`.`containerId` = `bls`.`containerId`))) join `v_dewar` `d` on((`d`.`dewarId` = `c`.`dewarId`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_sampleByWeek`
--

/*!50001 DROP TABLE IF EXISTS `v_sampleByWeek`*/;
/*!50001 DROP VIEW IF EXISTS `v_sampleByWeek`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_sampleByWeek` AS select substr(`w`.`num`,6) AS `Week`,if((`w`.`num` <= date_format(now(),'%Y-%v')),count(if((`bls`.`proposalCode` is not null),1,NULL)),NULL) AS `Samples` from (`v_week` `w` left join `v_sample` `bls` on((`w`.`num` = date_format(`bls`.`creationDate`,'%Y-%v')))) group by substr(`w`.`num`,6) order by substr(`w`.`num`,6) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_session`
--

/*!50001 DROP TABLE IF EXISTS `v_session`*/;
/*!50001 DROP VIEW IF EXISTS `v_session`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=MERGE */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_session` AS select `BLSession`.`sessionId` AS `sessionId`,`BLSession`.`expSessionPk` AS `expSessionPk`,`BLSession`.`beamLineSetupId` AS `beamLineSetupId`,`BLSession`.`proposalId` AS `proposalId`,`BLSession`.`projectCode` AS `projectCode`,`BLSession`.`startDate` AS `BLSession_startDate`,`BLSession`.`endDate` AS `BLSession_endDate`,`BLSession`.`beamLineName` AS `beamLineName`,`BLSession`.`scheduled` AS `scheduled`,`BLSession`.`nbShifts` AS `nbShifts`,`BLSession`.`comments` AS `comments`,`BLSession`.`beamLineOperator` AS `beamLineOperator`,`BLSession`.`visit_number` AS `visit_number`,`BLSession`.`bltimeStamp` AS `bltimeStamp`,`BLSession`.`usedFlag` AS `usedFlag`,`BLSession`.`sessionTitle` AS `sessionTitle`,`BLSession`.`structureDeterminations` AS `structureDeterminations`,`BLSession`.`dewarTransport` AS `dewarTransport`,`BLSession`.`databackupFrance` AS `databackupFrance`,`BLSession`.`databackupEurope` AS `databackupEurope`,`BLSession`.`operatorSiteNumber` AS `operatorSiteNumber`,`BLSession`.`lastUpdate` AS `BLSession_lastUpdate`,`BLSession`.`protectedData` AS `BLSession_protectedData`,`Proposal`.`title` AS `Proposal_title`,`Proposal`.`proposalCode` AS `Proposal_proposalCode`,`Proposal`.`proposalNumber` AS `Proposal_ProposalNumber`,`Proposal`.`proposalType` AS `Proposal_ProposalType`,`Person`.`personId` AS `Person_personId`,`Person`.`familyName` AS `Person_familyName`,`Person`.`givenName` AS `Person_givenName`,`Person`.`emailAddress` AS `Person_emailAddress` from ((`BLSession` left join `Proposal` on((`Proposal`.`proposalId` = `BLSession`.`proposalId`))) left join `Person` on((`Person`.`personId` = `Proposal`.`personId`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_week`
--

/*!50001 DROP TABLE IF EXISTS `v_week`*/;
/*!50001 DROP VIEW IF EXISTS `v_week`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_week` AS select date_format((now() - interval 52 week),_utf8'%x-%v') AS `num` union select date_format((now() - interval 51 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 51 WEEK),'%x-%v')` union select date_format((now() - interval 50 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 50 WEEK),'%x-%v')` union select date_format((now() - interval 49 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 49 WEEK),'%x-%v')` union select date_format((now() - interval 48 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 48 WEEK),'%x-%v')` union select date_format((now() - interval 47 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 47 WEEK),'%x-%v')` union select date_format((now() - interval 46 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 46 WEEK),'%x-%v')` union select date_format((now() - interval 45 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 45 WEEK),'%x-%v')` union select date_format((now() - interval 44 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 44 WEEK),'%x-%v')` union select date_format((now() - interval 43 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 43 WEEK),'%x-%v')` union select date_format((now() - interval 42 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 42 WEEK),'%x-%v')` union select date_format((now() - interval 41 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 41 WEEK),'%x-%v')` union select date_format((now() - interval 40 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 40 WEEK),'%x-%v')` union select date_format((now() - interval 39 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 39 WEEK),'%x-%v')` union select date_format((now() - interval 38 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 38 WEEK),'%x-%v')` union select date_format((now() - interval 37 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 37 WEEK),'%x-%v')` union select date_format((now() - interval 36 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 36 WEEK),'%x-%v')` union select date_format((now() - interval 35 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 35 WEEK),'%x-%v')` union select date_format((now() - interval 34 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 34 WEEK),'%x-%v')` union select date_format((now() - interval 33 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 33 WEEK),'%x-%v')` union select date_format((now() - interval 32 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 32 WEEK),'%x-%v')` union select date_format((now() - interval 31 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 31 WEEK),'%x-%v')` union select date_format((now() - interval 30 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 30 WEEK),'%x-%v')` union select date_format((now() - interval 29 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 29 WEEK),'%x-%v')` union select date_format((now() - interval 28 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 28 WEEK),'%x-%v')` union select date_format((now() - interval 27 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 27 WEEK),'%x-%v')` union select date_format((now() - interval 26 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 26 WEEK),'%x-%v')` union select date_format((now() - interval 25 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 25 WEEK),'%x-%v')` union select date_format((now() - interval 24 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 24 WEEK),'%x-%v')` union select date_format((now() - interval 23 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 23 WEEK),'%x-%v')` union select date_format((now() - interval 22 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 22 WEEK),'%x-%v')` union select date_format((now() - interval 21 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 21 WEEK),'%x-%v')` union select date_format((now() - interval 20 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 20 WEEK),'%x-%v')` union select date_format((now() - interval 19 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 19 WEEK),'%x-%v')` union select date_format((now() - interval 18 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 18 WEEK),'%x-%v')` union select date_format((now() - interval 17 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 17 WEEK),'%x-%v')` union select date_format((now() - interval 16 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 16 WEEK),'%x-%v')` union select date_format((now() - interval 15 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 15 WEEK),'%x-%v')` union select date_format((now() - interval 14 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 14 WEEK),'%x-%v')` union select date_format((now() - interval 13 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 13 WEEK),'%x-%v')` union select date_format((now() - interval 12 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 12 WEEK),'%x-%v')` union select date_format((now() - interval 11 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 11 WEEK),'%x-%v')` union select date_format((now() - interval 10 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 10 WEEK),'%x-%v')` union select date_format((now() - interval 9 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 9 WEEK),'%x-%v')` union select date_format((now() - interval 8 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 8 WEEK),'%x-%v')` union select date_format((now() - interval 7 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 7 WEEK),'%x-%v')` union select date_format((now() - interval 6 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 6 WEEK),'%x-%v')` union select date_format((now() - interval 5 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 5 WEEK),'%x-%v')` union select date_format((now() - interval 4 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 4 WEEK),'%x-%v')` union select date_format((now() - interval 3 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 3 WEEK),'%x-%v')` union select date_format((now() - interval 2 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 2 WEEK),'%x-%v')` union select date_format((now() - interval 1 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 1 WEEK),'%x-%v')` union select date_format((now() - interval 0 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 0 WEEK),'%x-%v')` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_weekDay`
--

/*!50001 DROP TABLE IF EXISTS `v_weekDay`*/;
/*!50001 DROP VIEW IF EXISTS `v_weekDay`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_weekDay` AS select date_format((now() - interval 6 day),_utf8'%Y-%m-%d') AS `day` union select date_format((now() - interval 5 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 5 DAY),'%Y-%m-%d')` union select date_format((now() - interval 4 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 4 DAY),'%Y-%m-%d')` union select date_format((now() - interval 3 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 3 DAY),'%Y-%m-%d')` union select date_format((now() - interval 2 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 2 DAY),'%Y-%m-%d')` union select date_format((now() - interval 1 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 1 DAY),'%Y-%m-%d')` union select date_format((now() - interval 0 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 0 DAY),'%Y-%m-%d')` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_xfeFluorescenceSpectrum`
--

/*!50001 DROP TABLE IF EXISTS `v_xfeFluorescenceSpectrum`*/;
/*!50001 DROP VIEW IF EXISTS `v_xfeFluorescenceSpectrum`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=MERGE */
/*!50013 DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_xfeFluorescenceSpectrum` AS select `XFEFluorescenceSpectrum`.`xfeFluorescenceSpectrumId` AS `xfeFluorescenceSpectrumId`,`XFEFluorescenceSpectrum`.`sessionId` AS `sessionId`,`XFEFluorescenceSpectrum`.`blSampleId` AS `blSampleId`,`XFEFluorescenceSpectrum`.`fittedDataFileFullPath` AS `fittedDataFileFullPath`,`XFEFluorescenceSpectrum`.`scanFileFullPath` AS `scanFileFullPath`,`XFEFluorescenceSpectrum`.`jpegScanFileFullPath` AS `jpegScanFileFullPath`,`XFEFluorescenceSpectrum`.`startTime` AS `startTime`,`XFEFluorescenceSpectrum`.`endTime` AS `endTime`,`XFEFluorescenceSpectrum`.`filename` AS `filename`,`XFEFluorescenceSpectrum`.`energy` AS `energy`,`XFEFluorescenceSpectrum`.`exposureTime` AS `exposureTime`,`XFEFluorescenceSpectrum`.`beamTransmission` AS `beamTransmission`,`XFEFluorescenceSpectrum`.`annotatedPymcaXfeSpectrum` AS `annotatedPymcaXfeSpectrum`,`XFEFluorescenceSpectrum`.`beamSizeVertical` AS `beamSizeVertical`,`XFEFluorescenceSpectrum`.`beamSizeHorizontal` AS `beamSizeHorizontal`,`XFEFluorescenceSpectrum`.`crystalClass` AS `crystalClass`,`XFEFluorescenceSpectrum`.`comments` AS `comments`,`XFEFluorescenceSpectrum`.`flux` AS `flux`,`XFEFluorescenceSpectrum`.`flux_end` AS `flux_end`,`XFEFluorescenceSpectrum`.`workingDirectory` AS `workingDirectory`,`BLSample`.`blSampleId` AS `BLSample_sampleId`,`BLSession`.`proposalId` AS `BLSession_proposalId` from ((`XFEFluorescenceSpectrum` left join `BLSample` on((`BLSample`.`blSampleId` = `XFEFluorescenceSpectrum`.`blSampleId`))) left join `BLSession` on((`BLSession`.`sessionId` = `XFEFluorescenceSpectrum`.`sessionId`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-04-03 14:38:42
