-- phpMyAdmin SQL Dump
-- version 4.6.6deb4
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Sep 11, 2019 at 10:40 AM
-- Server version: 10.1.23-MariaDB-9+deb9u1
-- PHP Version: 7.0.19-1

-- ISPyB version: 5.20

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pydb`
--
CREATE DATABASE IF NOT EXISTS `pydb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `pydb`;

-- --------------------------------------------------------

--
-- Table structure for table `AbInitioModel`
--

CREATE TABLE `AbInitioModel` (
  `abInitioModelId` int(10) NOT NULL,
  `modelListId` int(10) DEFAULT NULL,
  `averagedModelId` int(10) DEFAULT NULL,
  `rapidShapeDeterminationModelId` int(10) DEFAULT NULL,
  `shapeDeterminationModelId` int(10) DEFAULT NULL,
  `comments` varchar(512) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Additive`
--

CREATE TABLE `Additive` (
  `additiveId` int(11) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `additiveType` varchar(45) DEFAULT NULL,
  `comments` varchar(512) DEFAULT NULL,
  `chemFormulaHead` varchar(25) DEFAULT '',
  `chemFormulaTail` varchar(25) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `AdminActivity`
--

CREATE TABLE `AdminActivity` (
  `adminActivityId` int(11) NOT NULL,
  `username` varchar(45) NOT NULL DEFAULT '',
  `action` varchar(45) DEFAULT NULL,
  `comments` varchar(100) DEFAULT NULL,
  `dateTime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `AdminVar`
--

CREATE TABLE `AdminVar` (
  `varId` int(11) NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `value` varchar(1024) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='ISPyB administration values';

-- --------------------------------------------------------

--
-- Table structure for table `Aperture`
--

CREATE TABLE `Aperture` (
  `apertureId` int(10) UNSIGNED NOT NULL,
  `sizeX` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Assembly`
--

CREATE TABLE `Assembly` (
  `assemblyId` int(10) NOT NULL,
  `macromoleculeId` int(10) NOT NULL,
  `creationDate` datetime DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `AssemblyHasMacromolecule`
--

CREATE TABLE `AssemblyHasMacromolecule` (
  `AssemblyHasMacromoleculeId` int(10) NOT NULL,
  `assemblyId` int(10) NOT NULL,
  `macromoleculeId` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `AssemblyRegion`
--

CREATE TABLE `AssemblyRegion` (
  `assemblyRegionId` int(10) NOT NULL,
  `assemblyHasMacromoleculeId` int(10) NOT NULL,
  `assemblyRegionType` varchar(45) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `fromResiduesBases` varchar(45) DEFAULT NULL,
  `toResiduesBases` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProc`
--

CREATE TABLE `AutoProc` (
  `autoProcId` int(10) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `autoProcProgramId` int(10) UNSIGNED DEFAULT NULL COMMENT 'Related program item',
  `spaceGroup` varchar(45) DEFAULT NULL COMMENT 'Space group',
  `refinedCell_a` float DEFAULT NULL COMMENT 'Refined cell',
  `refinedCell_b` float DEFAULT NULL COMMENT 'Refined cell',
  `refinedCell_c` float DEFAULT NULL COMMENT 'Refined cell',
  `refinedCell_alpha` float DEFAULT NULL COMMENT 'Refined cell',
  `refinedCell_beta` float DEFAULT NULL COMMENT 'Refined cell',
  `refinedCell_gamma` float DEFAULT NULL COMMENT 'Refined cell',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcIntegration`
--

CREATE TABLE `AutoProcIntegration` (
  `autoProcIntegrationId` int(10) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `dataCollectionId` int(11) UNSIGNED NOT NULL COMMENT 'DataCollection item',
  `autoProcProgramId` int(10) UNSIGNED DEFAULT NULL COMMENT 'Related program item',
  `startImageNumber` int(10) UNSIGNED DEFAULT NULL COMMENT 'start image number',
  `endImageNumber` int(10) UNSIGNED DEFAULT NULL COMMENT 'end image number',
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
  `anomalous` tinyint(1) DEFAULT '0' COMMENT 'boolean type:0 noanoum - 1 anoum'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcProgram`
--

CREATE TABLE `AutoProcProgram` (
  `autoProcProgramId` int(10) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `dataCollectionId` int(11) UNSIGNED DEFAULT NULL,
  `processingCommandLine` varchar(255) DEFAULT NULL COMMENT 'Command line for running the automatic processing',
  `processingPrograms` varchar(255) DEFAULT NULL COMMENT 'Processing programs (comma separated)',
  `processingStatus` enum('RUNNING','FAILED','SUCCESS','0','1') DEFAULT NULL COMMENT 'success (1) / fail (0)',
  `processingMessage` varchar(255) DEFAULT NULL COMMENT 'warning, error,...',
  `processingStartTime` datetime DEFAULT NULL COMMENT 'Processing start time',
  `processingEndTime` datetime DEFAULT NULL COMMENT 'Processing end time',
  `processingEnvironment` varchar(255) DEFAULT NULL COMMENT 'Cpus, Nodes,...',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcProgramAttachment`
--

CREATE TABLE `AutoProcProgramAttachment` (
  `autoProcProgramAttachmentId` int(10) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `autoProcProgramId` int(10) UNSIGNED NOT NULL COMMENT 'Related autoProcProgram item',
  `fileType` enum('Log','Result','Graph') DEFAULT NULL COMMENT 'Type of file Attachment',
  `fileName` varchar(255) DEFAULT NULL COMMENT 'Attachment filename',
  `filePath` varchar(255) DEFAULT NULL COMMENT 'Attachment filepath to disk storage',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcScaling`
--

CREATE TABLE `AutoProcScaling` (
  `autoProcScalingId` int(10) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `autoProcId` int(10) UNSIGNED DEFAULT NULL COMMENT 'Related autoProc item (used by foreign key)',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  `resolutionEllipsoidAxis11` float DEFAULT NULL COMMENT 'Eigenvector for first diffraction limit, coord 1',
  `resolutionEllipsoidAxis12` float DEFAULT NULL COMMENT 'Eigenvector for first diffraction limit, coord 2',
  `resolutionEllipsoidAxis13` float DEFAULT NULL COMMENT 'Eigenvector for first diffraction limit, coord 3',
  `resolutionEllipsoidAxis21` float DEFAULT NULL COMMENT 'Eigenvector for second diffraction limit, coord 1',
  `resolutionEllipsoidAxis23` float DEFAULT NULL COMMENT 'Eigenvector for second diffraction limit, coord 3',
  `resolutionEllipsoidAxis31` float DEFAULT NULL COMMENT 'Eigenvector for third diffraction limit, coord 1',
  `resolutionEllipsoidAxis32` float DEFAULT NULL COMMENT 'Eigenvector for third diffraction limit, coord 2',
  `resolutionEllipsoidAxis33` float DEFAULT NULL COMMENT 'Eigenvector for third diffraction limit, coord 3',
  `resolutionEllipsoidValue1` float DEFAULT NULL COMMENT 'First (anisotropic) diffraction limit',
  `resolutionEllipsoidValue2` float DEFAULT NULL COMMENT 'Second (anisotropic) diffraction limit',
  `resolutionEllipsoidValue3` float DEFAULT NULL COMMENT 'Third (anisotropic) diffraction limit',
  `resolutionEllipsoidAxis22` float DEFAULT NULL COMMENT 'Eigenvector for second diffraction limit, coord 2'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcScalingStatistics`
--

CREATE TABLE `AutoProcScalingStatistics` (
  `autoProcScalingStatisticsId` int(10) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `autoProcScalingId` int(10) UNSIGNED DEFAULT NULL COMMENT 'Related autoProcScaling item (used by foreign key)',
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
  `ccAno` float DEFAULT NULL,
  `sigAno` varchar(45) DEFAULT NULL,
  `isa` varchar(45) DEFAULT NULL,
  `completenessSpherical` float DEFAULT NULL COMMENT 'Completeness calculated assuming isotropic diffraction',
  `completenessEllipsoidal` float DEFAULT NULL COMMENT 'Completeness calculated allowing for anisotropic diffraction',
  `anomalousCompletenessSpherical` float DEFAULT NULL COMMENT 'Anomalous completeness calculated assuming isotropic diffraction',
  `anomalousCompletenessEllipsoidal` float DEFAULT NULL COMMENT 'Anisotropic completeness calculated allowing for anisotropic diffraction'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcScaling_has_Int`
--

CREATE TABLE `AutoProcScaling_has_Int` (
  `autoProcScaling_has_IntId` int(10) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `autoProcScalingId` int(10) UNSIGNED DEFAULT NULL COMMENT 'AutoProcScaling item',
  `autoProcIntegrationId` int(10) UNSIGNED NOT NULL COMMENT 'AutoProcIntegration item',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcStatus`
--

CREATE TABLE `AutoProcStatus` (
  `autoProcStatusId` int(11) NOT NULL COMMENT 'Primary key (auto-incremented)',
  `autoProcIntegrationId` int(10) UNSIGNED NOT NULL,
  `step` enum('Indexing','Integration','Correction','Scaling','Importing') NOT NULL COMMENT 'autoprocessing step',
  `status` enum('Launched','Successful','Failed') NOT NULL COMMENT 'autoprocessing status',
  `comments` varchar(1024) DEFAULT NULL COMMENT 'comments',
  `bltimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='AutoProcStatus table is linked to AutoProcIntegration';

-- --------------------------------------------------------

--
-- Table structure for table `BeamApertures`
--

CREATE TABLE `BeamApertures` (
  `beamAperturesid` int(11) UNSIGNED NOT NULL,
  `beamlineStatsId` int(11) UNSIGNED DEFAULT NULL,
  `flux` double DEFAULT NULL,
  `x` float DEFAULT NULL,
  `y` float DEFAULT NULL,
  `apertureSize` smallint(5) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BeamCalendar`
--

CREATE TABLE `BeamCalendar` (
  `beamCalendarId` int(10) UNSIGNED NOT NULL,
  `run` varchar(7) NOT NULL,
  `beamStatus` varchar(24) NOT NULL,
  `startDate` datetime NOT NULL,
  `endDate` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `BeamCentres`
--

CREATE TABLE `BeamCentres` (
  `beamCentresid` int(11) UNSIGNED NOT NULL,
  `beamlineStatsId` int(11) UNSIGNED DEFAULT NULL,
  `x` float DEFAULT NULL,
  `y` float DEFAULT NULL,
  `zoom` tinyint(3) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BeamlineAction`
--

CREATE TABLE `BeamlineAction` (
  `beamlineActionId` int(11) UNSIGNED NOT NULL,
  `sessionId` int(11) UNSIGNED DEFAULT NULL,
  `startTimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `endTimestamp` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `message` varchar(255) DEFAULT NULL,
  `parameter` varchar(50) DEFAULT NULL,
  `value` varchar(30) DEFAULT NULL,
  `loglevel` enum('DEBUG','CRITICAL','INFO') DEFAULT NULL,
  `status` enum('PAUSED','RUNNING','TERMINATED','COMPLETE','ERROR','EPICSFAIL') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BeamLineSetup`
--

CREATE TABLE `BeamLineSetup` (
  `beamLineSetupId` int(10) UNSIGNED NOT NULL,
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
  `CS` float DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BeamlineStats`
--

CREATE TABLE `BeamlineStats` (
  `beamlineStatsId` int(11) UNSIGNED NOT NULL,
  `beamline` varchar(10) DEFAULT NULL,
  `recordTimeStamp` datetime DEFAULT NULL,
  `ringCurrent` float DEFAULT NULL,
  `energy` float DEFAULT NULL,
  `gony` float DEFAULT NULL,
  `beamW` float DEFAULT NULL,
  `beamH` float DEFAULT NULL,
  `flux` double DEFAULT NULL,
  `scanFileW` varchar(255) DEFAULT NULL,
  `scanFileH` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BF_automationError`
--

CREATE TABLE `BF_automationError` (
  `automationErrorId` int(10) UNSIGNED NOT NULL,
  `errorType` varchar(40) NOT NULL,
  `solution` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BF_automationFault`
--

CREATE TABLE `BF_automationFault` (
  `automationFaultId` int(10) UNSIGNED NOT NULL,
  `automationErrorId` int(10) UNSIGNED DEFAULT NULL,
  `containerId` int(10) UNSIGNED DEFAULT NULL,
  `severity` enum('1','2','3') DEFAULT NULL,
  `stacktrace` text,
  `resolved` tinyint(1) DEFAULT NULL,
  `faultTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BF_component`
--

CREATE TABLE `BF_component` (
  `componentId` int(10) UNSIGNED NOT NULL,
  `systemId` int(10) UNSIGNED DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BF_component_beamline`
--

CREATE TABLE `BF_component_beamline` (
  `component_beamlineId` int(10) UNSIGNED NOT NULL,
  `componentId` int(10) UNSIGNED DEFAULT NULL,
  `beamlinename` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BF_fault`
--

CREATE TABLE `BF_fault` (
  `faultId` int(10) UNSIGNED NOT NULL,
  `sessionId` int(10) UNSIGNED NOT NULL,
  `owner` varchar(50) DEFAULT NULL,
  `subcomponentId` int(10) UNSIGNED DEFAULT NULL,
  `starttime` datetime DEFAULT NULL,
  `endtime` datetime DEFAULT NULL,
  `beamtimelost` tinyint(1) DEFAULT NULL,
  `beamtimelost_starttime` datetime DEFAULT NULL,
  `beamtimelost_endtime` datetime DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL,
  `description` text,
  `resolved` tinyint(1) DEFAULT NULL,
  `resolution` text,
  `assignee` varchar(50) DEFAULT NULL,
  `attachment` varchar(200) DEFAULT NULL,
  `eLogId` int(11) DEFAULT NULL,
  `personId` int(10) UNSIGNED DEFAULT NULL,
  `assigneeId` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BF_subcomponent`
--

CREATE TABLE `BF_subcomponent` (
  `subcomponentId` int(10) UNSIGNED NOT NULL,
  `componentId` int(10) UNSIGNED DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BF_subcomponent_beamline`
--

CREATE TABLE `BF_subcomponent_beamline` (
  `subcomponent_beamlineId` int(10) UNSIGNED NOT NULL,
  `subcomponentId` int(10) UNSIGNED DEFAULT NULL,
  `beamlinename` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BF_system`
--

CREATE TABLE `BF_system` (
  `systemId` int(10) UNSIGNED NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BF_system_beamline`
--

CREATE TABLE `BF_system_beamline` (
  `system_beamlineId` int(10) UNSIGNED NOT NULL,
  `systemId` int(10) UNSIGNED DEFAULT NULL,
  `beamlineName` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BLSample`
--

CREATE TABLE `BLSample` (
  `blSampleId` int(10) UNSIGNED NOT NULL,
  `diffractionPlanId` int(10) UNSIGNED DEFAULT NULL,
  `crystalId` int(10) UNSIGNED DEFAULT NULL,
  `containerId` int(10) UNSIGNED DEFAULT NULL,
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
  `positionId` int(11) UNSIGNED DEFAULT NULL,
  `blSubSampleId` int(11) UNSIGNED DEFAULT NULL,
  `screenComponentGroupId` int(11) UNSIGNED DEFAULT NULL,
  `volume` float DEFAULT NULL,
  `dimension1` double DEFAULT NULL,
  `dimension2` double DEFAULT NULL,
  `dimension3` double DEFAULT NULL,
  `shape` varchar(15) DEFAULT NULL,
  `subLocation` smallint(5) UNSIGNED DEFAULT NULL COMMENT 'Indicates the sample''s location on a multi-sample pin, where 1 is closest to the pin base'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BLSampleGroup`
--

CREATE TABLE `BLSampleGroup` (
  `blSampleGroupId` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BLSampleGroup_has_BLSample`
--

CREATE TABLE `BLSampleGroup_has_BLSample` (
  `blSampleGroupId` int(11) UNSIGNED NOT NULL,
  `blSampleId` int(11) UNSIGNED NOT NULL,
  `order` mediumint(9) DEFAULT NULL,
  `type` enum('background','container','sample','calibrant') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BLSampleImage`
--

CREATE TABLE `BLSampleImage` (
  `blSampleImageId` int(11) UNSIGNED NOT NULL,
  `blSampleId` int(11) UNSIGNED NOT NULL,
  `micronsPerPixelX` float DEFAULT NULL,
  `micronsPerPixelY` float DEFAULT NULL,
  `imageFullPath` varchar(255) DEFAULT NULL,
  `blSampleImageScoreId` int(11) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `blTimeStamp` datetime DEFAULT NULL,
  `containerInspectionId` int(11) UNSIGNED DEFAULT NULL,
  `modifiedTimeStamp` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BLSampleImageAnalysis`
--

CREATE TABLE `BLSampleImageAnalysis` (
  `blSampleImageAnalysisId` int(11) UNSIGNED NOT NULL,
  `blSampleImageId` int(11) UNSIGNED DEFAULT NULL,
  `oavSnapshotBefore` varchar(255) DEFAULT NULL,
  `oavSnapshotAfter` varchar(255) DEFAULT NULL,
  `deltaX` int(11) DEFAULT NULL,
  `deltaY` int(11) DEFAULT NULL,
  `goodnessOfFit` float DEFAULT NULL,
  `scaleFactor` float DEFAULT NULL,
  `resultCode` varchar(15) DEFAULT NULL,
  `matchStartTimeStamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `matchEndTimeStamp` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BLSampleImageScore`
--

CREATE TABLE `BLSampleImageScore` (
  `blSampleImageScoreId` int(11) UNSIGNED NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `score` float DEFAULT NULL,
  `colour` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BLSampleType_has_Component`
--

CREATE TABLE `BLSampleType_has_Component` (
  `blSampleTypeId` int(10) UNSIGNED NOT NULL,
  `componentId` int(10) UNSIGNED NOT NULL,
  `abundance` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BLSample_has_DiffractionPlan`
--

CREATE TABLE `BLSample_has_DiffractionPlan` (
  `blSampleId` int(11) UNSIGNED NOT NULL,
  `diffractionPlanId` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BLSample_has_EnergyScan`
--

CREATE TABLE `BLSample_has_EnergyScan` (
  `blSampleId` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `energyScanId` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `blSampleHasEnergyScanId` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BLSession`
--

CREATE TABLE `BLSession` (
  `sessionId` int(10) UNSIGNED NOT NULL,
  `expSessionPk` int(11) UNSIGNED DEFAULT NULL COMMENT 'smis session Pk ',
  `beamLineSetupId` int(10) UNSIGNED DEFAULT NULL,
  `proposalId` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `beamCalendarId` int(10) UNSIGNED DEFAULT NULL,
  `projectCode` varchar(45) DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `beamLineName` varchar(45) DEFAULT NULL,
  `scheduled` tinyint(1) DEFAULT NULL,
  `nbShifts` int(10) UNSIGNED DEFAULT NULL,
  `comments` varchar(2000) DEFAULT NULL,
  `beamLineOperator` varchar(255) DEFAULT NULL,
  `visit_number` int(10) UNSIGNED DEFAULT '0',
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
  `nbReimbDewars` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BLSession_has_SCPosition`
--

CREATE TABLE `BLSession_has_SCPosition` (
  `blsessionhasscpositionid` int(11) UNSIGNED NOT NULL,
  `blsessionid` int(11) UNSIGNED NOT NULL,
  `scContainer` smallint(5) UNSIGNED DEFAULT NULL COMMENT 'Position of container within sample changer',
  `containerPosition` smallint(5) UNSIGNED DEFAULT NULL COMMENT 'Position of sample within container'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BLSubSample`
--

CREATE TABLE `BLSubSample` (
  `blSubSampleId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `blSampleId` int(10) UNSIGNED NOT NULL COMMENT 'sample',
  `diffractionPlanId` int(10) UNSIGNED DEFAULT NULL COMMENT 'eventually diffractionPlan',
  `positionId` int(11) UNSIGNED DEFAULT NULL COMMENT 'position of the subsample',
  `position2Id` int(11) UNSIGNED DEFAULT NULL,
  `blSubSampleUUID` varchar(45) DEFAULT NULL COMMENT 'uuid of the blsubsample',
  `imgFileName` varchar(255) DEFAULT NULL COMMENT 'image filename',
  `imgFilePath` varchar(1024) DEFAULT NULL COMMENT 'url image',
  `comments` varchar(1024) DEFAULT NULL COMMENT 'comments',
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  `motorPositionId` int(11) UNSIGNED DEFAULT NULL COMMENT 'motor position'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Buffer`
--

CREATE TABLE `Buffer` (
  `bufferId` int(10) NOT NULL,
  `proposalId` int(10) NOT NULL DEFAULT '-1',
  `safetyLevelId` int(10) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `acronym` varchar(45) DEFAULT NULL,
  `pH` varchar(45) DEFAULT NULL,
  `composition` varchar(45) DEFAULT NULL,
  `comments` varchar(512) DEFAULT NULL,
  `BLSessionId` int(11) UNSIGNED DEFAULT NULL,
  `electronDensity` float(7,5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `BufferHasAdditive`
--

CREATE TABLE `BufferHasAdditive` (
  `bufferHasAdditiveId` int(10) NOT NULL,
  `bufferId` int(10) NOT NULL,
  `additiveId` int(10) NOT NULL,
  `measurementUnitId` int(10) DEFAULT NULL,
  `quantity` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `CalendarHash`
--

CREATE TABLE `CalendarHash` (
  `calendarHashId` int(10) UNSIGNED NOT NULL,
  `ckey` varchar(50) DEFAULT NULL,
  `hash` varchar(128) DEFAULT NULL,
  `beamline` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Lets people get to their calendars without logging in using a private (hash) url';

-- --------------------------------------------------------

--
-- Table structure for table `ComponentSubType`
--

CREATE TABLE `ComponentSubType` (
  `componentSubTypeId` int(11) UNSIGNED NOT NULL,
  `name` varchar(31) NOT NULL,
  `hasPh` tinyint(1) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ComponentType`
--

CREATE TABLE `ComponentType` (
  `componentTypeId` int(11) UNSIGNED NOT NULL,
  `name` varchar(31) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Component_has_SubType`
--

CREATE TABLE `Component_has_SubType` (
  `componentId` int(10) UNSIGNED NOT NULL,
  `componentSubTypeId` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ConcentrationType`
--

CREATE TABLE `ConcentrationType` (
  `concentrationTypeId` int(11) UNSIGNED NOT NULL,
  `name` varchar(31) NOT NULL,
  `symbol` varchar(8) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Container`
--

CREATE TABLE `Container` (
  `containerId` int(10) UNSIGNED NOT NULL,
  `dewarId` int(10) UNSIGNED DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL,
  `containerType` varchar(20) DEFAULT NULL,
  `capacity` int(10) UNSIGNED DEFAULT NULL,
  `beamlineLocation` varchar(20) DEFAULT NULL,
  `sampleChangerLocation` varchar(20) DEFAULT NULL,
  `containerStatus` varchar(45) DEFAULT NULL,
  `bltimeStamp` datetime DEFAULT NULL,
  `barcode` varchar(45) DEFAULT NULL,
  `sessionId` int(10) UNSIGNED DEFAULT NULL,
  `ownerId` int(10) UNSIGNED DEFAULT NULL,
  `screenId` int(11) UNSIGNED DEFAULT NULL,
  `scheduleId` int(11) UNSIGNED DEFAULT NULL,
  `imagerId` int(11) UNSIGNED DEFAULT NULL,
  `scLocationUpdated` datetime DEFAULT NULL,
  `requestedImagerId` int(11) UNSIGNED DEFAULT NULL,
  `requestedReturn` tinyint(1) DEFAULT '0' COMMENT 'True for requesting return, False means container will be disposed',
  `comments` varchar(255) DEFAULT NULL,
  `experimentType` varchar(20) DEFAULT NULL,
  `storageTemperature` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ContainerHistory`
--

CREATE TABLE `ContainerHistory` (
  `containerHistoryId` int(11) UNSIGNED NOT NULL,
  `containerId` int(10) UNSIGNED DEFAULT NULL,
  `location` varchar(45) DEFAULT NULL,
  `blTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ContainerInspection`
--

CREATE TABLE `ContainerInspection` (
  `containerInspectionId` int(11) UNSIGNED NOT NULL,
  `containerId` int(11) UNSIGNED NOT NULL,
  `inspectionTypeId` int(11) UNSIGNED NOT NULL,
  `imagerId` int(11) UNSIGNED DEFAULT NULL,
  `temperature` float DEFAULT NULL,
  `blTimeStamp` datetime DEFAULT NULL,
  `scheduleComponentid` int(11) UNSIGNED DEFAULT NULL,
  `state` varchar(20) DEFAULT NULL,
  `priority` smallint(6) DEFAULT NULL,
  `manual` tinyint(1) DEFAULT NULL,
  `scheduledTimeStamp` datetime DEFAULT NULL,
  `completedTimeStamp` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ContainerQueue`
--

CREATE TABLE `ContainerQueue` (
  `containerQueueId` int(11) UNSIGNED NOT NULL,
  `containerId` int(10) UNSIGNED DEFAULT NULL,
  `personId` int(10) UNSIGNED DEFAULT NULL,
  `createdTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `completedTimeStamp` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ContainerQueueSample`
--

CREATE TABLE `ContainerQueueSample` (
  `containerQueueSampleId` int(11) UNSIGNED NOT NULL,
  `containerQueueId` int(11) UNSIGNED DEFAULT NULL,
  `blSubSampleId` int(11) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Crystal`
--

CREATE TABLE `Crystal` (
  `crystalId` int(10) UNSIGNED NOT NULL,
  `diffractionPlanId` int(10) UNSIGNED DEFAULT NULL,
  `proteinId` int(10) UNSIGNED NOT NULL DEFAULT '0',
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
  `abundance` float DEFAULT NULL,
  `packingFraction` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Crystal_has_UUID`
--

CREATE TABLE `Crystal_has_UUID` (
  `crystal_has_UUID_Id` int(10) UNSIGNED NOT NULL,
  `crystalId` int(10) UNSIGNED NOT NULL,
  `UUID` varchar(45) DEFAULT NULL,
  `imageURL` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `CTF`
--

CREATE TABLE `CTF` (
  `CTFid` int(11) NOT NULL,
  `motionCorrectionId` int(11) NOT NULL,
  `spectraImageThumbnailFullPath` varchar(512) DEFAULT NULL,
  `spectraImageFullPath` varchar(512) DEFAULT NULL,
  `defocusU` varchar(45) DEFAULT NULL,
  `defocusV` varchar(45) DEFAULT NULL,
  `angle` varchar(45) DEFAULT NULL,
  `crossCorrelationCoefficient` varchar(45) DEFAULT NULL,
  `resolutionLimit` varchar(45) DEFAULT NULL,
  `estimatedBfactor` varchar(45) DEFAULT NULL,
  `logFilePath` varchar(512) DEFAULT NULL,
  `createdTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `DataAcquisition`
--

CREATE TABLE `DataAcquisition` (
  `dataAcquisitionId` int(10) NOT NULL,
  `sampleCellId` int(10) NOT NULL,
  `framesCount` varchar(45) DEFAULT NULL,
  `energy` varchar(45) DEFAULT NULL,
  `waitTime` varchar(45) DEFAULT NULL,
  `detectorDistance` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `DataCollection`
--

CREATE TABLE `DataCollection` (
  `dataCollectionId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `dataCollectionGroupId` int(11) NOT NULL COMMENT 'references DataCollectionGroup table',
  `strategySubWedgeOrigId` int(10) UNSIGNED DEFAULT NULL COMMENT 'references ScreeningStrategySubWedge table',
  `detectorId` int(11) DEFAULT NULL COMMENT 'references Detector table',
  `blSubSampleId` int(11) UNSIGNED DEFAULT NULL,
  `startPositionId` int(11) UNSIGNED DEFAULT NULL,
  `endPositionId` int(11) UNSIGNED DEFAULT NULL,
  `dataCollectionNumber` int(10) UNSIGNED DEFAULT NULL,
  `startTime` datetime DEFAULT NULL COMMENT 'Start time of the dataCollection',
  `endTime` datetime DEFAULT NULL COMMENT 'end time of the dataCollection',
  `runStatus` varchar(45) DEFAULT NULL,
  `axisStart` float DEFAULT NULL,
  `axisEnd` float DEFAULT NULL,
  `axisRange` float DEFAULT NULL,
  `overlap` float DEFAULT NULL,
  `numberOfImages` int(10) UNSIGNED DEFAULT NULL,
  `startImageNumber` int(10) UNSIGNED DEFAULT NULL,
  `numberOfPasses` int(10) UNSIGNED DEFAULT NULL,
  `exposureTime` float DEFAULT NULL,
  `imageDirectory` varchar(255) DEFAULT NULL,
  `imagePrefix` varchar(100) DEFAULT NULL,
  `imageSuffix` varchar(45) DEFAULT NULL,
  `imageContainerSubPath` varchar(255) DEFAULT NULL COMMENT 'Internal path of a HDF5 file pointing to the data for this data collection',
  `fileTemplate` varchar(255) DEFAULT NULL,
  `wavelength` float DEFAULT NULL,
  `resolution` float DEFAULT NULL,
  `detectorDistance` float DEFAULT NULL,
  `xBeam` float DEFAULT NULL,
  `yBeam` float DEFAULT NULL,
  `xBeamPix` float DEFAULT NULL COMMENT 'Beam size in pixels',
  `yBeamPix` float DEFAULT NULL COMMENT 'Beam size in pixels',
  `comments` varchar(1024) DEFAULT NULL,
  `printableForReport` tinyint(1) UNSIGNED DEFAULT '1',
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
  `blSampleId` int(11) UNSIGNED DEFAULT NULL,
  `sessionId` int(11) UNSIGNED DEFAULT '0',
  `experimentType` varchar(24) DEFAULT NULL,
  `crystalClass` varchar(20) DEFAULT NULL,
  `chiStart` float DEFAULT NULL,
  `detectorMode` varchar(255) DEFAULT NULL,
  `actualSampleBarcode` varchar(45) DEFAULT NULL,
  `actualSampleSlotInContainer` int(11) UNSIGNED DEFAULT NULL,
  `actualContainerBarcode` varchar(45) DEFAULT NULL,
  `actualContainerSlotInSC` int(11) UNSIGNED DEFAULT NULL,
  `positionId` int(11) UNSIGNED DEFAULT NULL,
  `focalSpotSizeAtSampleX` float DEFAULT NULL,
  `polarisation` float DEFAULT NULL,
  `focalSpotSizeAtSampleY` float DEFAULT NULL,
  `apertureId` int(11) UNSIGNED DEFAULT NULL,
  `screeningOrigId` int(11) UNSIGNED DEFAULT NULL,
  `processedDataFile` varchar(255) DEFAULT NULL,
  `datFullPath` varchar(255) DEFAULT NULL,
  `magnification` int(11) DEFAULT NULL COMMENT 'Unit: X',
  `binning` tinyint(1) DEFAULT '1' COMMENT '1 or 2. Number of pixels to process as 1. (Use mean value.)',
  `particleDiameter` float DEFAULT NULL COMMENT 'Unit: nm',
  `boxSize_CTF` float DEFAULT NULL COMMENT 'Unit: pixels',
  `minResolution` float DEFAULT NULL COMMENT 'Unit: A',
  `minDefocus` float DEFAULT NULL COMMENT 'Unit: A',
  `maxDefocus` float DEFAULT NULL COMMENT 'Unit: A',
  `defocusStepSize` float DEFAULT NULL COMMENT 'Unit: A',
  `amountAstigmatism` float DEFAULT NULL COMMENT 'Unit: A',
  `extractSize` float DEFAULT NULL COMMENT 'Unit: pixels',
  `bgRadius` float DEFAULT NULL COMMENT 'Unit: nm',
  `voltage` float DEFAULT NULL COMMENT 'Unit: kV',
  `objAperture` float DEFAULT NULL COMMENT 'Unit: um',
  `c1aperture` float DEFAULT NULL COMMENT 'Unit: um',
  `c2aperture` float DEFAULT NULL COMMENT 'Unit: um',
  `c3aperture` float DEFAULT NULL COMMENT 'Unit: um',
  `c1lens` float DEFAULT NULL COMMENT 'Unit: %',
  `c2lens` float DEFAULT NULL COMMENT 'Unit: %',
  `c3lens` float DEFAULT NULL COMMENT 'Unit: %'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `DataCollectionFileAttachment`
--

CREATE TABLE `DataCollectionFileAttachment` (
  `dataCollectionFileAttachmentId` int(11) UNSIGNED NOT NULL,
  `dataCollectionId` int(11) UNSIGNED NOT NULL,
  `fileFullPath` varchar(255) NOT NULL,
  `fileType` enum('snapshot','log','xy','recip') DEFAULT NULL COMMENT 'snapshot: image file, usually of the sample. \r\nlog: a text file with logging info. \r\nxy: x and y data in text format. \r\nrecip: a compressed csv file with reciprocal space coordinates.',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `DataCollectionGroup`
--

CREATE TABLE `DataCollectionGroup` (
  `dataCollectionGroupId` int(11) NOT NULL COMMENT 'Primary key (auto-incremented)',
  `blSampleId` int(10) UNSIGNED DEFAULT NULL COMMENT 'references BLSample table',
  `sessionId` int(10) UNSIGNED NOT NULL COMMENT 'references Session table',
  `workflowId` int(11) UNSIGNED DEFAULT NULL,
  `experimentType` enum('EM','SAD','SAD - Inverse Beam','OSC','Collect - Multiwedge','MAD','Helical','Multi-positional','Mesh','Burn','MAD - Inverse Beam','Characterization','Dehydration','Still') DEFAULT NULL COMMENT 'Experiment type flag',
  `startTime` datetime DEFAULT NULL COMMENT 'Start time of the dataCollectionGroup',
  `endTime` datetime DEFAULT NULL COMMENT 'end time of the dataCollectionGroup',
  `crystalClass` varchar(20) DEFAULT NULL COMMENT 'Crystal Class for industrials users',
  `comments` varchar(1024) DEFAULT NULL COMMENT 'comments',
  `detectorMode` varchar(255) DEFAULT NULL COMMENT 'Detector mode',
  `actualSampleBarcode` varchar(45) DEFAULT NULL COMMENT 'Actual sample barcode',
  `actualSampleSlotInContainer` int(10) UNSIGNED DEFAULT NULL COMMENT 'Actual sample slot number in container',
  `actualContainerBarcode` varchar(45) DEFAULT NULL COMMENT 'Actual container barcode',
  `actualContainerSlotInSC` int(10) UNSIGNED DEFAULT NULL COMMENT 'Actual container slot number in sample changer',
  `xtalSnapshotFullPath` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='a dataCollectionGroup is a group of dataCollection for a spe';

-- --------------------------------------------------------

--
-- Table structure for table `DataCollectionPlanGroup`
--

CREATE TABLE `DataCollectionPlanGroup` (
  `dataCollectionPlanGroupId` int(11) UNSIGNED NOT NULL,
  `sessionId` int(11) UNSIGNED DEFAULT NULL,
  `blSampleId` int(11) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `DatamatrixInSampleChanger`
--

CREATE TABLE `DatamatrixInSampleChanger` (
  `datamatrixInSampleChangerId` int(10) UNSIGNED NOT NULL,
  `proposalId` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `beamLineName` varchar(45) DEFAULT NULL,
  `datamatrixCode` varchar(45) DEFAULT NULL,
  `locationInContainer` int(11) DEFAULT NULL,
  `containerLocationInSC` int(11) DEFAULT NULL,
  `containerDatamatrixCode` varchar(45) DEFAULT NULL,
  `bltimeStamp` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `DataReductionStatus`
--

CREATE TABLE `DataReductionStatus` (
  `dataReductionStatusId` int(11) UNSIGNED NOT NULL,
  `dataCollectionId` int(11) UNSIGNED NOT NULL,
  `status` varchar(15) DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Detector`
--

CREATE TABLE `Detector` (
  `detectorId` int(11) NOT NULL COMMENT 'Primary key (auto-incremented)',
  `detectorType` varchar(255) DEFAULT NULL,
  `detectorManufacturer` varchar(255) DEFAULT NULL,
  `detectorModel` varchar(255) DEFAULT NULL,
  `detectorPixelSizeHorizontal` float DEFAULT NULL,
  `detectorPixelSizeVertical` float DEFAULT NULL,
  `detectorSerialNumber` varchar(30) DEFAULT NULL,
  `detectorDistanceMin` double DEFAULT NULL,
  `detectorDistanceMax` double DEFAULT NULL,
  `trustedPixelValueRangeLower` double DEFAULT NULL,
  `trustedPixelValueRangeUpper` double DEFAULT NULL,
  `sensorThickness` float DEFAULT NULL,
  `overload` float DEFAULT NULL,
  `XGeoCorr` varchar(255) DEFAULT NULL,
  `YGeoCorr` varchar(255) DEFAULT NULL,
  `detectorMode` varchar(255) DEFAULT NULL,
  `detectorMaxResolution` float DEFAULT NULL,
  `detectorMinResolution` float DEFAULT NULL,
  `CS` float DEFAULT NULL COMMENT 'Unit: mm',
  `density` float DEFAULT NULL,
  `composition` varchar(16) DEFAULT NULL,
  `localName` varchar(40) DEFAULT NULL COMMENT 'Colloquial name for the detector'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Detector table is linked to a dataCollection';

-- --------------------------------------------------------

--
-- Table structure for table `Dewar`
--

CREATE TABLE `Dewar` (
  `dewarId` int(10) UNSIGNED NOT NULL,
  `shippingId` int(10) UNSIGNED DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL,
  `comments` tinytext,
  `storageLocation` varchar(45) DEFAULT NULL,
  `dewarStatus` varchar(45) DEFAULT NULL,
  `bltimeStamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `isStorageDewar` tinyint(1) DEFAULT '0',
  `barCode` varchar(45) DEFAULT NULL,
  `firstExperimentId` int(10) UNSIGNED DEFAULT NULL,
  `customsValue` int(11) UNSIGNED DEFAULT NULL,
  `transportValue` int(11) UNSIGNED DEFAULT NULL,
  `trackingNumberToSynchrotron` varchar(30) DEFAULT NULL,
  `trackingNumberFromSynchrotron` varchar(30) DEFAULT NULL,
  `facilityCode` varchar(20) DEFAULT NULL COMMENT 'Unique barcode assigned to each dewar',
  `isReimbursed` tinyint(1) DEFAULT '0' COMMENT 'set this dewar as reimbursed by the user office',
  `type` enum('Dewar','Toolbox') NOT NULL DEFAULT 'Dewar'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `DewarLocation`
--

CREATE TABLE `DewarLocation` (
  `eventId` int(10) UNSIGNED NOT NULL,
  `dewarNumber` varchar(128) NOT NULL COMMENT 'Dewar number',
  `userId` varchar(128) DEFAULT NULL COMMENT 'User who locates the dewar',
  `dateTime` datetime DEFAULT NULL COMMENT 'Date and time of locatization',
  `locationName` varchar(128) DEFAULT NULL COMMENT 'Location of the dewar',
  `courierName` varchar(128) DEFAULT NULL COMMENT 'Carrier name who''s shipping back the dewar',
  `courierTrackingNumber` varchar(128) DEFAULT NULL COMMENT 'Tracking number of the shippment'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='ISPyB Dewar location table';

-- --------------------------------------------------------

--
-- Table structure for table `DewarLocationList`
--

CREATE TABLE `DewarLocationList` (
  `locationId` int(10) UNSIGNED NOT NULL,
  `locationName` varchar(128) NOT NULL DEFAULT '' COMMENT 'Location'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='List of locations for dewars';

-- --------------------------------------------------------

--
-- Table structure for table `DewarTransportHistory`
--

CREATE TABLE `DewarTransportHistory` (
  `DewarTransportHistoryId` int(10) UNSIGNED NOT NULL,
  `dewarId` int(10) UNSIGNED DEFAULT NULL,
  `dewarStatus` varchar(45) NOT NULL,
  `storageLocation` varchar(45) DEFAULT NULL,
  `arrivalDate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `DiffractionPlan`
--

CREATE TABLE `DiffractionPlan` (
  `diffractionPlanId` int(10) UNSIGNED NOT NULL,
  `xmlDocumentId` int(10) UNSIGNED DEFAULT NULL,
  `experimentKind` enum('Default','MXPressE','MXPressF','MXPressO','MXPressP','MXPressP_SAD','MXPressI','MXPressE_SAD','MXScore','MXPressM','MAD','SAD','Fixed','Ligand binding','Refinement','OSC','MAD - Inverse Beam','SAD - Inverse Beam') DEFAULT NULL,
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
  `diffractionPlanUUID` varchar(1000) DEFAULT NULL,
  `dataCollectionPlanGroupId` int(11) UNSIGNED DEFAULT NULL,
  `detectorId` int(11) DEFAULT NULL,
  `distance` double DEFAULT NULL,
  `orientation` double DEFAULT NULL,
  `monoBandwidth` double DEFAULT NULL,
  `monochromator` varchar(8) DEFAULT NULL COMMENT 'DMM or DCM',
  `energy` float DEFAULT NULL COMMENT 'eV',
  `transmission` float DEFAULT NULL COMMENT 'Decimal fraction in range [0,1]',
  `boxSizeX` float DEFAULT NULL COMMENT 'microns',
  `boxSizeY` float DEFAULT NULL COMMENT 'microns',
  `kappaStart` float DEFAULT NULL COMMENT 'degrees',
  `axisStart` float DEFAULT NULL COMMENT 'degrees',
  `axisRange` float DEFAULT NULL COMMENT 'degrees',
  `numberOfImages` mediumint(9) DEFAULT NULL COMMENT 'The number of images requested',
  `presetForProposalId` int(10) UNSIGNED DEFAULT NULL COMMENT 'Indicates this plan is available to all sessions on given proposal',
  `beamLineName` varchar(45) DEFAULT NULL COMMENT 'Indicates this plan is available to all sessions on given beamline'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `DiffractionPlan_has_Detector`
--

CREATE TABLE `DiffractionPlan_has_Detector` (
  `diffractionPlanId` int(11) UNSIGNED NOT NULL,
  `detectorId` int(11) NOT NULL,
  `exposureTime` double DEFAULT NULL,
  `distance` double DEFAULT NULL,
  `orientation` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `EMMicroscope`
--

CREATE TABLE `EMMicroscope` (
  `emMicroscopeId` int(11) UNSIGNED NOT NULL,
  `instrumentName` varchar(100) NOT NULL,
  `voltage` float DEFAULT NULL,
  `CS` float DEFAULT NULL COMMENT 'Unit: mm',
  `detectorPixelSize` float DEFAULT NULL,
  `C2aperture` float DEFAULT NULL,
  `ObjAperture` float DEFAULT NULL,
  `C2lens` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `EnergyScan`
--

CREATE TABLE `EnergyScan` (
  `energyScanId` int(10) UNSIGNED NOT NULL,
  `sessionId` int(10) UNSIGNED NOT NULL,
  `blSampleId` int(10) UNSIGNED DEFAULT NULL,
  `fluorescenceDetector` varchar(255) DEFAULT NULL,
  `scanFileFullPath` varchar(255) DEFAULT NULL,
  `choochFileFullPath` varchar(255) DEFAULT NULL,
  `jpegChoochFileFullPath` varchar(255) DEFAULT NULL,
  `element` varchar(45) DEFAULT NULL,
  `startEnergy` float DEFAULT NULL,
  `endEnergy` float DEFAULT NULL,
  `transmissionFactor` float DEFAULT NULL,
  `exposureTime` float DEFAULT NULL,
  `axisPosition` float DEFAULT NULL,
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
  `blSubSampleId` int(11) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Experiment`
--

CREATE TABLE `Experiment` (
  `experimentId` int(11) NOT NULL,
  `sessionId` int(10) UNSIGNED DEFAULT NULL,
  `proposalId` int(10) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `experimentType` varchar(128) DEFAULT NULL,
  `sourceFilePath` varchar(256) DEFAULT NULL,
  `dataAcquisitionFilePath` varchar(256) DEFAULT NULL COMMENT 'The file path pointing to the data acquisition. Eventually it may be a compressed file with all the files or just the folder',
  `status` varchar(45) DEFAULT NULL,
  `comments` varchar(512) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ExperimentKindDetails`
--

CREATE TABLE `ExperimentKindDetails` (
  `experimentKindId` int(10) UNSIGNED NOT NULL,
  `diffractionPlanId` int(10) UNSIGNED NOT NULL,
  `exposureIndex` int(10) UNSIGNED DEFAULT NULL,
  `dataCollectionType` varchar(45) DEFAULT NULL,
  `dataCollectionKind` varchar(45) DEFAULT NULL,
  `wedgeValue` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `FitStructureToExperimentalData`
--

CREATE TABLE `FitStructureToExperimentalData` (
  `fitStructureToExperimentalDataId` int(11) NOT NULL,
  `structureId` int(10) DEFAULT NULL,
  `subtractionId` int(10) DEFAULT NULL,
  `workflowId` int(10) UNSIGNED DEFAULT NULL,
  `fitFilePath` varchar(255) DEFAULT NULL,
  `logFilePath` varchar(255) DEFAULT NULL,
  `outputFilePath` varchar(255) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `comments` varchar(2048) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Frame`
--

CREATE TABLE `Frame` (
  `frameId` int(10) NOT NULL,
  `filePath` varchar(255) DEFAULT NULL,
  `comments` varchar(45) DEFAULT NULL,
  `creationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `frameSetId` int(11) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `FrameList`
--

CREATE TABLE `FrameList` (
  `frameListId` int(10) NOT NULL,
  `comments` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `FrameSet`
--

CREATE TABLE `FrameSet` (
  `frameSetId` int(10) NOT NULL,
  `runId` int(10) NOT NULL,
  `frameListId` int(10) DEFAULT NULL,
  `detectorId` int(10) DEFAULT NULL,
  `detectorDistance` varchar(45) DEFAULT NULL,
  `filePath` varchar(255) DEFAULT NULL,
  `internalPath` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `FrameToList`
--

CREATE TABLE `FrameToList` (
  `frameToListId` int(10) NOT NULL,
  `frameListId` int(10) NOT NULL,
  `frameId` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `GeometryClassname`
--

CREATE TABLE `GeometryClassname` (
  `geometryClassnameId` int(11) UNSIGNED NOT NULL,
  `geometryClassname` varchar(45) DEFAULT NULL,
  `geometryOrder` int(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `GridInfo`
--

CREATE TABLE `GridInfo` (
  `gridInfoId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `workflowMeshId` int(11) UNSIGNED DEFAULT NULL,
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
  `snapshot_offsetypixel` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Image`
--

CREATE TABLE `Image` (
  `imageId` int(12) UNSIGNED NOT NULL,
  `dataCollectionId` int(11) UNSIGNED NOT NULL DEFAULT '0',
  `motorPositionId` int(11) UNSIGNED DEFAULT NULL,
  `imageNumber` int(10) UNSIGNED DEFAULT NULL,
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
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ImageQualityIndicators`
--

CREATE TABLE `ImageQualityIndicators` (
  `imageQualityIndicatorsId` int(10) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `imageId` int(12) DEFAULT NULL,
  `autoProcProgramId` int(10) UNSIGNED NOT NULL COMMENT 'Foreign key to the AutoProcProgram table',
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
  `dataCollectionId` int(11) UNSIGNED DEFAULT NULL,
  `imageNumber` mediumint(8) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Imager`
--

CREATE TABLE `Imager` (
  `imagerId` int(11) UNSIGNED NOT NULL,
  `name` varchar(45) NOT NULL,
  `temperature` float DEFAULT NULL,
  `serial` varchar(45) DEFAULT NULL,
  `capacity` smallint(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `InputParameterWorkflow`
--

CREATE TABLE `InputParameterWorkflow` (
  `inputParameterId` int(10) NOT NULL,
  `workflowId` int(10) NOT NULL,
  `parameterType` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `comments` varchar(2048) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `InspectionType`
--

CREATE TABLE `InspectionType` (
  `inspectionTypeId` int(11) UNSIGNED NOT NULL,
  `name` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Instruction`
--

CREATE TABLE `Instruction` (
  `instructionId` int(10) NOT NULL,
  `instructionSetId` int(10) NOT NULL,
  `order` int(11) NOT NULL,
  `comments` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `InstructionSet`
--

CREATE TABLE `InstructionSet` (
  `instructionSetId` int(10) NOT NULL,
  `type` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `IspybAutoProcAttachment`
--

CREATE TABLE `IspybAutoProcAttachment` (
  `autoProcAttachmentId` int(11) NOT NULL,
  `fileName` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `step` enum('XDS','XSCALE','SCALA','SCALEPACK','TRUNCATE','DIMPLE') DEFAULT 'XDS' COMMENT 'step where the file is generated',
  `fileCategory` enum('input','output','log','correction') DEFAULT 'output',
  `hasGraph` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='ISPyB autoProcAttachment files values';

-- --------------------------------------------------------

--
-- Table structure for table `IspybCrystalClass`
--

CREATE TABLE `IspybCrystalClass` (
  `crystalClassId` int(11) NOT NULL,
  `crystalClass_code` varchar(20) NOT NULL,
  `crystalClass_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='ISPyB crystal class values';

-- --------------------------------------------------------

--
-- Table structure for table `IspybReference`
--

CREATE TABLE `IspybReference` (
  `referenceId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `referenceName` varchar(255) DEFAULT NULL COMMENT 'reference name',
  `referenceUrl` varchar(1024) DEFAULT NULL COMMENT 'url of the reference',
  `referenceBibtext` blob COMMENT 'bibtext value of the reference',
  `beamline` enum('All','ID14-4','ID23-1','ID23-2','ID29','ID30A-1','ID30A-2','XRF','AllXRF','Mesh') DEFAULT NULL COMMENT 'beamline involved'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `LabContact`
--

CREATE TABLE `LabContact` (
  `labContactId` int(10) UNSIGNED NOT NULL,
  `personId` int(10) UNSIGNED NOT NULL,
  `cardName` varchar(40) NOT NULL,
  `proposalId` int(10) UNSIGNED NOT NULL,
  `defaultCourrierCompany` varchar(45) DEFAULT NULL,
  `courierAccount` varchar(45) DEFAULT NULL,
  `billingReference` varchar(45) DEFAULT NULL,
  `dewarAvgCustomsValue` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `dewarAvgTransportValue` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Laboratory`
--

CREATE TABLE `Laboratory` (
  `laboratoryId` int(10) UNSIGNED NOT NULL,
  `laboratoryUUID` varchar(45) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `organization` varchar(45) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  `laboratoryExtPk` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Log4Stat`
--

CREATE TABLE `Log4Stat` (
  `id` int(11) NOT NULL,
  `priority` varchar(15) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `msg` varchar(255) DEFAULT NULL,
  `detail` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Login`
--

CREATE TABLE `Login` (
  `loginId` int(11) NOT NULL,
  `token` varchar(45) NOT NULL,
  `username` varchar(45) NOT NULL,
  `roles` varchar(1024) NOT NULL,
  `siteId` varchar(45) DEFAULT NULL,
  `authorized` varchar(1024) DEFAULT NULL,
  `expirationTime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Macromolecule`
--

CREATE TABLE `Macromolecule` (
  `macromoleculeId` int(11) NOT NULL,
  `proposalId` int(10) UNSIGNED DEFAULT NULL,
  `safetyLevelId` int(10) DEFAULT NULL,
  `name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `acronym` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `extintionCoefficient` varchar(45) DEFAULT NULL,
  `molecularMass` varchar(45) DEFAULT NULL,
  `sequence` varchar(1000) DEFAULT NULL,
  `contactsDescriptionFilePath` varchar(255) DEFAULT NULL,
  `symmetry` varchar(45) DEFAULT NULL,
  `comments` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `refractiveIndex` varchar(45) DEFAULT NULL,
  `solventViscosity` varchar(45) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `electronDensity` float(7,5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `MacromoleculeRegion`
--

CREATE TABLE `MacromoleculeRegion` (
  `macromoleculeRegionId` int(10) NOT NULL,
  `macromoleculeId` int(10) NOT NULL,
  `regionType` varchar(45) DEFAULT NULL,
  `id` varchar(45) DEFAULT NULL,
  `count` varchar(45) DEFAULT NULL,
  `sequence` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Measurement`
--

CREATE TABLE `Measurement` (
  `measurementId` int(10) NOT NULL,
  `specimenId` int(10) NOT NULL,
  `runId` int(10) DEFAULT NULL,
  `code` varchar(100) DEFAULT NULL,
  `imageDirectory` varchar(512) DEFAULT NULL,
  `priorityLevelId` int(10) DEFAULT NULL,
  `exposureTemperature` varchar(45) DEFAULT NULL,
  `viscosity` varchar(45) DEFAULT NULL,
  `flow` tinyint(1) DEFAULT NULL,
  `extraFlowTime` varchar(45) DEFAULT NULL,
  `volumeToLoad` varchar(45) DEFAULT NULL,
  `waitTime` varchar(45) DEFAULT NULL,
  `transmission` varchar(45) DEFAULT NULL,
  `comments` varchar(512) DEFAULT NULL,
  `pathToH5` varchar(512) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `MeasurementToDataCollection`
--

CREATE TABLE `MeasurementToDataCollection` (
  `measurementToDataCollectionId` int(10) NOT NULL,
  `dataCollectionId` int(10) DEFAULT NULL,
  `measurementId` int(10) DEFAULT NULL,
  `dataCollectionOrder` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `MeasurementUnit`
--

CREATE TABLE `MeasurementUnit` (
  `measurementUnitId` int(10) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `unitType` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Merge`
--

CREATE TABLE `Merge` (
  `mergeId` int(10) NOT NULL,
  `measurementId` int(10) DEFAULT NULL,
  `frameListId` int(10) DEFAULT NULL,
  `discardedFrameNameList` varchar(1024) DEFAULT NULL,
  `averageFilePath` varchar(255) DEFAULT NULL,
  `framesCount` varchar(45) DEFAULT NULL,
  `framesMerge` varchar(45) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `MixtureToStructure`
--

CREATE TABLE `MixtureToStructure` (
  `fitToStructureId` int(11) NOT NULL,
  `structureId` int(10) NOT NULL,
  `mixtureId` int(10) NOT NULL,
  `volumeFraction` varchar(45) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Model`
--

CREATE TABLE `Model` (
  `modelId` int(10) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `pdbFile` varchar(255) DEFAULT NULL,
  `fitFile` varchar(255) DEFAULT NULL,
  `firFile` varchar(255) DEFAULT NULL,
  `logFile` varchar(255) DEFAULT NULL,
  `rFactor` varchar(45) DEFAULT NULL,
  `chiSqrt` varchar(45) DEFAULT NULL,
  `volume` varchar(45) DEFAULT NULL,
  `rg` varchar(45) DEFAULT NULL,
  `dMax` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ModelBuilding`
--

CREATE TABLE `ModelBuilding` (
  `modelBuildingId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `phasingAnalysisId` int(11) UNSIGNED NOT NULL COMMENT 'Related phasing analysis item',
  `phasingProgramRunId` int(11) UNSIGNED NOT NULL COMMENT 'Related program item',
  `spaceGroupId` int(10) UNSIGNED DEFAULT NULL COMMENT 'Related spaceGroup',
  `lowRes` double DEFAULT NULL,
  `highRes` double DEFAULT NULL,
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ModelList`
--

CREATE TABLE `ModelList` (
  `modelListId` int(10) NOT NULL,
  `nsdFilePath` varchar(255) DEFAULT NULL,
  `chi2RgFilePath` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ModelToList`
--

CREATE TABLE `ModelToList` (
  `modelToListId` int(10) NOT NULL,
  `modelId` int(10) NOT NULL,
  `modelListId` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `MotionCorrection`
--

CREATE TABLE `MotionCorrection` (
  `motionCorrectionId` int(11) NOT NULL,
  `movieId` int(11) DEFAULT NULL,
  `firstFrame` varchar(45) DEFAULT NULL,
  `lastFrame` varchar(45) DEFAULT NULL,
  `dosePerFrame` varchar(45) DEFAULT NULL,
  `doseWeight` varchar(45) DEFAULT NULL,
  `totalMotion` varchar(45) DEFAULT NULL,
  `averageMotionPerFrame` varchar(45) DEFAULT NULL,
  `driftPlotFullPath` varchar(512) DEFAULT NULL,
  `micrographFullPath` varchar(512) DEFAULT NULL,
  `micrographSnapshotFullPath` varchar(512) DEFAULT NULL,
  `correctedDoseMicrographFullPath` varchar(512) DEFAULT NULL,
  `patchesUsed` varchar(45) DEFAULT NULL,
  `logFileFullPath` varchar(512) DEFAULT NULL,
  `createdTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `MotorPosition`
--

CREATE TABLE `MotorPosition` (
  `motorPositionId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
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
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Movie`
--

CREATE TABLE `Movie` (
  `movieId` int(11) NOT NULL,
  `dataCollectionId` int(11) UNSIGNED DEFAULT NULL,
  `movieNumber` int(11) DEFAULT NULL,
  `movieFullPath` varchar(255) DEFAULT NULL,
  `positionX` varchar(45) DEFAULT NULL,
  `positionY` varchar(45) DEFAULT NULL,
  `micrographFullPath` varchar(255) DEFAULT NULL,
  `micrographSnapshotFullPath` varchar(255) DEFAULT NULL,
  `xmlMetaDataFullPath` varchar(255) DEFAULT NULL,
  `dosePerImage` varchar(45) DEFAULT NULL,
  `createdTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `MXMRRun`
--

CREATE TABLE `MXMRRun` (
  `mxMRRunId` int(11) UNSIGNED NOT NULL,
  `autoProcScalingId` int(11) UNSIGNED NOT NULL,
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
  `endtime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `MXMRRunBlob`
--

CREATE TABLE `MXMRRunBlob` (
  `mxMRRunBlobId` int(11) UNSIGNED NOT NULL,
  `mxMRRunId` int(11) UNSIGNED NOT NULL,
  `view1` varchar(255) DEFAULT NULL,
  `view2` varchar(255) DEFAULT NULL,
  `view3` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Particle`
--

CREATE TABLE `Particle` (
  `particleId` int(11) UNSIGNED NOT NULL,
  `dataCollectionId` int(11) UNSIGNED NOT NULL,
  `x` float DEFAULT NULL,
  `y` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `PDB`
--

CREATE TABLE `PDB` (
  `pdbId` int(11) UNSIGNED NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `contents` mediumtext,
  `code` varchar(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `PDBEntry`
--

CREATE TABLE `PDBEntry` (
  `pdbEntryId` int(11) UNSIGNED NOT NULL,
  `autoProcProgramId` int(11) UNSIGNED DEFAULT NULL,
  `code` varchar(4) DEFAULT NULL,
  `cell_a` float DEFAULT NULL,
  `cell_b` float DEFAULT NULL,
  `cell_c` float DEFAULT NULL,
  `cell_alpha` float DEFAULT NULL,
  `cell_beta` float DEFAULT NULL,
  `cell_gamma` float DEFAULT NULL,
  `resolution` float DEFAULT NULL,
  `pdbTitle` varchar(255) DEFAULT NULL,
  `pdbAuthors` varchar(600) DEFAULT NULL,
  `pdbDate` datetime DEFAULT NULL,
  `pdbBeamlineName` varchar(50) DEFAULT NULL,
  `beamlines` varchar(100) DEFAULT NULL,
  `distance` float DEFAULT NULL,
  `autoProcCount` smallint(6) DEFAULT NULL,
  `dataCollectionCount` smallint(6) DEFAULT NULL,
  `beamlineMatch` tinyint(1) DEFAULT NULL,
  `authorMatch` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `PDBEntry_has_AutoProcProgram`
--

CREATE TABLE `PDBEntry_has_AutoProcProgram` (
  `pdbEntryHasAutoProcId` int(11) UNSIGNED NOT NULL,
  `pdbEntryId` int(11) UNSIGNED NOT NULL,
  `autoProcProgramId` int(11) UNSIGNED NOT NULL,
  `distance` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Permission`
--

CREATE TABLE `Permission` (
  `permissionId` int(11) UNSIGNED NOT NULL,
  `type` varchar(15) NOT NULL,
  `description` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Person`
--

CREATE TABLE `Person` (
  `personId` int(10) UNSIGNED NOT NULL,
  `laboratoryId` int(10) UNSIGNED DEFAULT NULL,
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
  `cache` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Phasing`
--

CREATE TABLE `Phasing` (
  `phasingId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `phasingAnalysisId` int(11) UNSIGNED NOT NULL COMMENT 'Related phasing analysis item',
  `phasingProgramRunId` int(11) UNSIGNED NOT NULL COMMENT 'Related program item',
  `spaceGroupId` int(10) UNSIGNED DEFAULT NULL COMMENT 'Related spaceGroup',
  `method` enum('solvent flattening','solvent flipping') DEFAULT NULL COMMENT 'phasing method',
  `solventContent` double DEFAULT NULL,
  `enantiomorph` tinyint(1) DEFAULT NULL COMMENT '0 or 1',
  `lowRes` double DEFAULT NULL,
  `highRes` double DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `PhasingAnalysis`
--

CREATE TABLE `PhasingAnalysis` (
  `phasingAnalysisId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `PhasingProgramAttachment`
--

CREATE TABLE `PhasingProgramAttachment` (
  `phasingProgramAttachmentId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `phasingProgramRunId` int(11) UNSIGNED NOT NULL COMMENT 'Related program item',
  `fileType` enum('DSIGMA_RESOLUTION','OCCUPANCY_SITENUMBER','CONTRAST_CYCLE','CCALL_CCWEAK','IMAGE','Map','Logfile','PDB','CSV','INS','RES','TXT') DEFAULT NULL COMMENT 'file type',
  `fileName` varchar(45) DEFAULT NULL COMMENT 'file name',
  `filePath` varchar(255) DEFAULT NULL COMMENT 'file path',
  `input` tinyint(1) DEFAULT NULL,
  `recordTimeStamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `PhasingProgramRun`
--

CREATE TABLE `PhasingProgramRun` (
  `phasingProgramRunId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `phasingCommandLine` varchar(255) DEFAULT NULL COMMENT 'Command line for phasing',
  `phasingPrograms` varchar(255) DEFAULT NULL COMMENT 'Phasing programs (comma separated)',
  `phasingStatus` tinyint(1) DEFAULT NULL COMMENT 'success (1) / fail (0)',
  `phasingMessage` varchar(255) DEFAULT NULL COMMENT 'warning, error,...',
  `phasingStartTime` datetime DEFAULT NULL COMMENT 'Processing start time',
  `phasingEndTime` datetime DEFAULT NULL COMMENT 'Processing end time',
  `phasingEnvironment` varchar(255) DEFAULT NULL COMMENT 'Cpus, Nodes,...',
  `phasingDirectory` varchar(255) DEFAULT NULL COMMENT 'Directory of execution',
  `recordTimeStamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `PhasingStatistics`
--

CREATE TABLE `PhasingStatistics` (
  `phasingStatisticsId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `phasingHasScalingId1` int(11) UNSIGNED DEFAULT NULL COMMENT 'the dataset in question',
  `phasingHasScalingId2` int(11) UNSIGNED DEFAULT NULL COMMENT 'if this is MIT or MAD, which scaling are being compared, null otherwise',
  `phasingStepId` int(10) UNSIGNED DEFAULT NULL,
  `numberOfBins` int(11) DEFAULT NULL COMMENT 'the total number of bins',
  `binNumber` int(11) DEFAULT NULL COMMENT 'binNumber, 999 for overall',
  `lowRes` double DEFAULT NULL COMMENT 'low resolution cutoff of this binfloat',
  `highRes` double DEFAULT NULL COMMENT 'high resolution cutoff of this binfloat',
  `metric` enum('Rcullis','Average Fragment Length','Chain Count','Residues Count','CC','PhasingPower','FOM','<d"/sig>','Best CC','CC(1/2)','Weak CC','CFOM','Pseudo_free_CC','CC of partial model','Start R-work','Start R-free','Final R-work','Final R-free') DEFAULT NULL COMMENT 'metric',
  `statisticsValue` double DEFAULT NULL COMMENT 'the statistics value',
  `nReflections` int(11) DEFAULT NULL,
  `recordTimeStamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `PhasingStep`
--

CREATE TABLE `PhasingStep` (
  `phasingStepId` int(10) UNSIGNED NOT NULL,
  `previousPhasingStepId` int(10) UNSIGNED DEFAULT NULL,
  `programRunId` int(10) UNSIGNED DEFAULT NULL,
  `spaceGroupId` int(10) UNSIGNED DEFAULT NULL,
  `autoProcScalingId` int(10) UNSIGNED DEFAULT NULL,
  `phasingAnalysisId` int(10) UNSIGNED DEFAULT NULL,
  `phasingStepType` enum('PREPARE','SUBSTRUCTUREDETERMINATION','PHASING','MODELBUILDING','REFINEMENT','LIGAND_FIT') DEFAULT NULL,
  `method` varchar(45) DEFAULT NULL,
  `solventContent` varchar(45) DEFAULT NULL,
  `enantiomorph` varchar(45) DEFAULT NULL,
  `lowRes` varchar(45) DEFAULT NULL,
  `highRes` varchar(45) DEFAULT NULL,
  `groupName` varchar(45) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Phasing_has_Scaling`
--

CREATE TABLE `Phasing_has_Scaling` (
  `phasingHasScalingId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `phasingAnalysisId` int(11) UNSIGNED NOT NULL COMMENT 'Related phasing analysis item',
  `autoProcScalingId` int(10) UNSIGNED NOT NULL COMMENT 'Related autoProcScaling item',
  `datasetNumber` int(11) DEFAULT NULL COMMENT 'serial number of the dataset and always reserve 0 for the reference',
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `PHPSession`
--

CREATE TABLE `PHPSession` (
  `id` varchar(50) NOT NULL,
  `accessDate` datetime DEFAULT NULL,
  `data` varchar(4000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `PlateGroup`
--

CREATE TABLE `PlateGroup` (
  `plateGroupId` int(10) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `storageTemperature` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `PlateType`
--

CREATE TABLE `PlateType` (
  `PlateTypeId` int(10) NOT NULL,
  `experimentId` int(10) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  `shape` varchar(45) DEFAULT NULL,
  `rowCount` int(11) DEFAULT NULL,
  `columnCount` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Position`
--

CREATE TABLE `Position` (
  `positionId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `relativePositionId` int(11) UNSIGNED DEFAULT NULL COMMENT 'relative position, null otherwise',
  `posX` double DEFAULT NULL,
  `posY` double DEFAULT NULL,
  `posZ` double DEFAULT NULL,
  `scale` double DEFAULT NULL,
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `PreparePhasingData`
--

CREATE TABLE `PreparePhasingData` (
  `preparePhasingDataId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `phasingAnalysisId` int(11) UNSIGNED NOT NULL COMMENT 'Related phasing analysis item',
  `phasingProgramRunId` int(11) UNSIGNED NOT NULL COMMENT 'Related program item',
  `spaceGroupId` int(10) UNSIGNED DEFAULT NULL COMMENT 'Related spaceGroup',
  `lowRes` double DEFAULT NULL,
  `highRes` double DEFAULT NULL,
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Project`
--

CREATE TABLE `Project` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `personId` int(11) UNSIGNED DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL,
  `acronym` varchar(100) DEFAULT NULL,
  `owner` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Project_has_BLSample`
--

CREATE TABLE `Project_has_BLSample` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `blSampleId` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Project_has_DCGroup`
--

CREATE TABLE `Project_has_DCGroup` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `dataCollectionGroupId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Project_has_EnergyScan`
--

CREATE TABLE `Project_has_EnergyScan` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `energyScanId` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Project_has_Person`
--

CREATE TABLE `Project_has_Person` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `personId` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Project_has_Protein`
--

CREATE TABLE `Project_has_Protein` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `proteinId` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Project_has_Session`
--

CREATE TABLE `Project_has_Session` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `sessionId` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Project_has_Shipping`
--

CREATE TABLE `Project_has_Shipping` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `shippingId` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Project_has_User`
--

CREATE TABLE `Project_has_User` (
  `projecthasuserid` int(11) UNSIGNED NOT NULL,
  `projectid` int(11) UNSIGNED NOT NULL,
  `username` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Project_has_XFEFSpectrum`
--

CREATE TABLE `Project_has_XFEFSpectrum` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `xfeFluorescenceSpectrumId` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Proposal`
--

CREATE TABLE `Proposal` (
  `proposalId` int(10) UNSIGNED NOT NULL,
  `personId` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `proposalCode` varchar(45) DEFAULT NULL,
  `proposalNumber` varchar(45) DEFAULT NULL,
  `proposalType` varchar(2) DEFAULT NULL COMMENT 'Proposal type: MX, BX',
  `bltimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `externalId` binary(16) DEFAULT NULL,
  `state` enum('Open','Closed','Cancelled') DEFAULT 'Open'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ProposalHasPerson`
--

CREATE TABLE `ProposalHasPerson` (
  `proposalHasPersonId` int(10) UNSIGNED NOT NULL,
  `proposalId` int(10) UNSIGNED NOT NULL,
  `personId` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Protein`
--

CREATE TABLE `Protein` (
  `proteinId` int(10) UNSIGNED NOT NULL,
  `proposalId` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `acronym` varchar(45) DEFAULT NULL,
  `safetyLevel` enum('GREEN','YELLOW','RED') DEFAULT NULL,
  `molecularMass` double DEFAULT NULL,
  `proteinType` varchar(45) DEFAULT NULL,
  `sequence` text,
  `personId` int(10) UNSIGNED DEFAULT NULL,
  `bltimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `isCreatedBySampleSheet` tinyint(1) DEFAULT '0',
  `externalId` binary(16) DEFAULT NULL,
  `componentTypeId` int(11) UNSIGNED DEFAULT NULL,
  `modId` varchar(20) DEFAULT NULL,
  `concentrationTypeId` int(11) UNSIGNED DEFAULT NULL,
  `global` tinyint(1) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Protein_has_Lattice`
--

CREATE TABLE `Protein_has_Lattice` (
  `proteinId` int(10) UNSIGNED NOT NULL,
  `cell_a` double DEFAULT NULL,
  `cell_b` double DEFAULT NULL,
  `cell_c` double DEFAULT NULL,
  `cell_alpha` double DEFAULT NULL,
  `cell_beta` double DEFAULT NULL,
  `cell_gamma` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Protein_has_PDB`
--

CREATE TABLE `Protein_has_PDB` (
  `proteinhaspdbid` int(11) UNSIGNED NOT NULL,
  `proteinid` int(11) UNSIGNED NOT NULL,
  `pdbid` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `RigidBodyModeling`
--

CREATE TABLE `RigidBodyModeling` (
  `rigidBodyModelingId` int(11) NOT NULL,
  `subtractionId` int(11) NOT NULL,
  `fitFilePath` varchar(255) DEFAULT NULL,
  `rigidBodyModelFilePath` varchar(255) DEFAULT NULL,
  `logFilePath` varchar(255) DEFAULT NULL,
  `curveConfigFilePath` varchar(255) DEFAULT NULL,
  `subUnitConfigFilePath` varchar(255) DEFAULT NULL,
  `crossCorrConfigFilePath` varchar(255) DEFAULT NULL,
  `contactDescriptionFilePath` varchar(255) DEFAULT NULL,
  `symmetry` varchar(255) DEFAULT NULL,
  `creationDate` varchar(45) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `RobotAction`
--

CREATE TABLE `RobotAction` (
  `robotActionId` int(11) UNSIGNED NOT NULL,
  `blsessionId` int(11) UNSIGNED NOT NULL,
  `blsampleId` int(11) UNSIGNED DEFAULT NULL,
  `actionType` enum('LOAD','UNLOAD','DISPOSE','STORE','WASH','ANNEAL') DEFAULT NULL,
  `startTimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `endTimestamp` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `status` enum('SUCCESS','ERROR','CRITICAL','WARNING','COMMANDNOTSENT') DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `containerLocation` smallint(6) DEFAULT NULL,
  `dewarLocation` smallint(6) DEFAULT NULL,
  `sampleBarcode` varchar(45) DEFAULT NULL,
  `xtalSnapshotBefore` varchar(255) DEFAULT NULL,
  `xtalSnapshotAfter` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Robot actions as reported by MXCube';

-- --------------------------------------------------------

--
-- Table structure for table `Run`
--

CREATE TABLE `Run` (
  `runId` int(10) NOT NULL,
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
  `normalization` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `SafetyLevel`
--

CREATE TABLE `SafetyLevel` (
  `safetyLevelId` int(10) NOT NULL,
  `code` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `SamplePlate`
--

CREATE TABLE `SamplePlate` (
  `samplePlateId` int(10) NOT NULL,
  `experimentId` int(10) NOT NULL,
  `plateGroupId` int(10) DEFAULT NULL,
  `plateTypeId` int(10) DEFAULT NULL,
  `instructionSetId` int(10) DEFAULT NULL,
  `boxId` int(10) UNSIGNED DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `slotPositionRow` varchar(45) DEFAULT NULL,
  `slotPositionColumn` varchar(45) DEFAULT NULL,
  `storageTemperature` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `SamplePlatePosition`
--

CREATE TABLE `SamplePlatePosition` (
  `samplePlatePositionId` int(10) NOT NULL,
  `samplePlateId` int(10) NOT NULL,
  `rowNumber` int(11) DEFAULT NULL,
  `columnNumber` int(11) DEFAULT NULL,
  `volume` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `SaxsDataCollection`
--

CREATE TABLE `SaxsDataCollection` (
  `dataCollectionId` int(10) NOT NULL,
  `experimentId` int(10) NOT NULL,
  `comments` varchar(5120) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ScanParametersModel`
--

CREATE TABLE `ScanParametersModel` (
  `scanParametersModelId` int(11) UNSIGNED NOT NULL,
  `scanParametersServiceId` int(10) UNSIGNED DEFAULT NULL,
  `dataCollectionPlanId` int(11) UNSIGNED DEFAULT NULL,
  `modelNumber` tinyint(3) UNSIGNED DEFAULT NULL,
  `start` double DEFAULT NULL,
  `stop` double DEFAULT NULL,
  `step` double DEFAULT NULL,
  `array` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ScanParametersService`
--

CREATE TABLE `ScanParametersService` (
  `scanParametersServiceId` int(10) UNSIGNED NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Schedule`
--

CREATE TABLE `Schedule` (
  `scheduleId` int(11) UNSIGNED NOT NULL,
  `name` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ScheduleComponent`
--

CREATE TABLE `ScheduleComponent` (
  `scheduleComponentId` int(11) UNSIGNED NOT NULL,
  `scheduleId` int(11) UNSIGNED NOT NULL,
  `inspectionTypeId` int(11) UNSIGNED DEFAULT NULL,
  `offset_hours` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `SchemaStatus`
--

CREATE TABLE `SchemaStatus` (
  `schemaStatusId` int(11) NOT NULL,
  `scriptName` varchar(100) NOT NULL,
  `schemaStatus` varchar(10) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Screen`
--

CREATE TABLE `Screen` (
  `screenId` int(11) UNSIGNED NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `proposalId` int(10) UNSIGNED DEFAULT NULL,
  `global` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ScreenComponent`
--

CREATE TABLE `ScreenComponent` (
  `screenComponentId` int(11) UNSIGNED NOT NULL,
  `screenComponentGroupId` int(11) UNSIGNED NOT NULL,
  `componentId` int(11) UNSIGNED DEFAULT NULL,
  `concentration` float DEFAULT NULL,
  `pH` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ScreenComponentGroup`
--

CREATE TABLE `ScreenComponentGroup` (
  `screenComponentGroupId` int(11) UNSIGNED NOT NULL,
  `screenId` int(11) UNSIGNED NOT NULL,
  `position` smallint(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Screening`
--

CREATE TABLE `Screening` (
  `screeningId` int(10) UNSIGNED NOT NULL,
  `diffractionPlanId` int(10) UNSIGNED DEFAULT NULL COMMENT 'references DiffractionPlan',
  `dataCollectionGroupId` int(11) DEFAULT NULL,
  `dataCollectionId` int(11) UNSIGNED DEFAULT NULL,
  `bltimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `programVersion` varchar(45) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `shortComments` varchar(20) DEFAULT NULL,
  `xmlSampleInformation` longblob
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningInput`
--

CREATE TABLE `ScreeningInput` (
  `screeningInputId` int(10) UNSIGNED NOT NULL,
  `screeningId` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `diffractionPlanId` int(10) DEFAULT NULL COMMENT 'references DiffractionPlan table',
  `beamX` float DEFAULT NULL,
  `beamY` float DEFAULT NULL,
  `rmsErrorLimits` float DEFAULT NULL,
  `minimumFractionIndexed` float DEFAULT NULL,
  `maximumFractionRejected` float DEFAULT NULL,
  `minimumSignalToNoise` float DEFAULT NULL,
  `xmlSampleInformation` longblob
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningOutput`
--

CREATE TABLE `ScreeningOutput` (
  `screeningOutputId` int(10) UNSIGNED NOT NULL,
  `screeningId` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `statusDescription` varchar(1024) DEFAULT NULL,
  `rejectedReflections` int(10) UNSIGNED DEFAULT NULL,
  `resolutionObtained` float DEFAULT NULL,
  `spotDeviationR` float DEFAULT NULL,
  `spotDeviationTheta` float DEFAULT NULL,
  `beamShiftX` float DEFAULT NULL,
  `beamShiftY` float DEFAULT NULL,
  `numSpotsFound` int(10) UNSIGNED DEFAULT NULL,
  `numSpotsUsed` int(10) UNSIGNED DEFAULT NULL,
  `numSpotsRejected` int(10) UNSIGNED DEFAULT NULL,
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
  `screeningSuccess` tinyint(1) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningOutputLattice`
--

CREATE TABLE `ScreeningOutputLattice` (
  `screeningOutputLatticeId` int(10) UNSIGNED NOT NULL,
  `screeningOutputId` int(10) UNSIGNED NOT NULL DEFAULT '0',
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
  `bltimeStamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `labelitIndexing` tinyint(1) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningRank`
--

CREATE TABLE `ScreeningRank` (
  `screeningRankId` int(10) UNSIGNED NOT NULL,
  `screeningRankSetId` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `screeningId` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `rankValue` float DEFAULT NULL,
  `rankInformation` varchar(1024) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningRankSet`
--

CREATE TABLE `ScreeningRankSet` (
  `screeningRankSetId` int(10) UNSIGNED NOT NULL,
  `rankEngine` varchar(255) DEFAULT NULL,
  `rankingProjectFileName` varchar(255) DEFAULT NULL,
  `rankingSummaryFileName` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningStrategy`
--

CREATE TABLE `ScreeningStrategy` (
  `screeningStrategyId` int(10) UNSIGNED NOT NULL,
  `screeningOutputId` int(10) UNSIGNED NOT NULL DEFAULT '0',
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
  `transmission` float DEFAULT NULL COMMENT 'Transmission for the strategy as given by the strategy program.'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningStrategySubWedge`
--

CREATE TABLE `ScreeningStrategySubWedge` (
  `screeningStrategySubWedgeId` int(10) UNSIGNED NOT NULL COMMENT 'Primary key',
  `screeningStrategyWedgeId` int(10) UNSIGNED DEFAULT NULL COMMENT 'Foreign key to parent table',
  `subWedgeNumber` int(10) UNSIGNED DEFAULT NULL COMMENT 'The number of this subwedge within the wedge',
  `rotationAxis` varchar(45) DEFAULT NULL COMMENT 'Angle where subwedge starts',
  `axisStart` float DEFAULT NULL COMMENT 'Angle where subwedge ends',
  `axisEnd` float DEFAULT NULL COMMENT 'Exposure time for subwedge',
  `exposureTime` float DEFAULT NULL COMMENT 'Transmission for subwedge',
  `transmission` float DEFAULT NULL,
  `oscillationRange` float DEFAULT NULL,
  `completeness` float DEFAULT NULL,
  `multiplicity` float DEFAULT NULL,
  `doseTotal` float DEFAULT NULL COMMENT 'Total dose for this subwedge',
  `numberOfImages` int(10) UNSIGNED DEFAULT NULL COMMENT 'Number of images for this subwedge',
  `comments` varchar(255) DEFAULT NULL,
  `resolution` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningStrategyWedge`
--

CREATE TABLE `ScreeningStrategyWedge` (
  `screeningStrategyWedgeId` int(10) UNSIGNED NOT NULL COMMENT 'Primary key',
  `screeningStrategyId` int(10) UNSIGNED DEFAULT NULL COMMENT 'Foreign key to parent table',
  `wedgeNumber` int(10) UNSIGNED DEFAULT NULL COMMENT 'The number of this wedge within the strategy',
  `resolution` float DEFAULT NULL,
  `completeness` float DEFAULT NULL,
  `multiplicity` float DEFAULT NULL,
  `doseTotal` float DEFAULT NULL COMMENT 'Total dose for this wedge',
  `numberOfImages` int(10) UNSIGNED DEFAULT NULL COMMENT 'Number of images for this wedge',
  `phi` float DEFAULT NULL,
  `kappa` float DEFAULT NULL,
  `chi` float DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `wavelength` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `SessionType`
--

CREATE TABLE `SessionType` (
  `sessionTypeId` int(10) UNSIGNED NOT NULL,
  `sessionId` int(10) UNSIGNED NOT NULL,
  `typeName` varchar(31) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Session_has_Person`
--

CREATE TABLE `Session_has_Person` (
  `sessionId` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `personId` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `role` enum('Local Contact','Local Contact 2','Staff','Team Leader','Co-Investigator','Principal Investigator','Alternate Contact') DEFAULT NULL,
  `remote` tinyint(1) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Shipping`
--

CREATE TABLE `Shipping` (
  `shippingId` int(10) UNSIGNED NOT NULL,
  `proposalId` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `shippingName` varchar(45) DEFAULT NULL,
  `deliveryAgent_agentName` varchar(45) DEFAULT NULL,
  `deliveryAgent_shippingDate` date DEFAULT NULL,
  `deliveryAgent_deliveryDate` date DEFAULT NULL,
  `deliveryAgent_agentCode` varchar(45) DEFAULT NULL,
  `deliveryAgent_flightCode` varchar(45) DEFAULT NULL,
  `shippingStatus` varchar(45) DEFAULT NULL,
  `bltimeStamp` datetime DEFAULT NULL,
  `laboratoryId` int(10) UNSIGNED DEFAULT NULL,
  `isStorageShipping` tinyint(1) DEFAULT '0',
  `creationDate` datetime DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `sendingLabContactId` int(10) UNSIGNED DEFAULT NULL,
  `returnLabContactId` int(10) UNSIGNED DEFAULT NULL,
  `returnCourier` varchar(45) DEFAULT NULL,
  `dateOfShippingToUser` datetime DEFAULT NULL,
  `shippingType` varchar(45) DEFAULT NULL,
  `safetyLevel` varchar(8) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ShippingHasSession`
--

CREATE TABLE `ShippingHasSession` (
  `shippingId` int(10) UNSIGNED NOT NULL,
  `sessionId` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `SpaceGroup`
--

CREATE TABLE `SpaceGroup` (
  `spaceGroupId` int(10) UNSIGNED NOT NULL COMMENT 'Primary key',
  `geometryClassnameId` int(11) UNSIGNED DEFAULT NULL,
  `spaceGroupNumber` int(10) UNSIGNED DEFAULT NULL COMMENT 'ccp4 number pr IUCR',
  `spaceGroupShortName` varchar(45) DEFAULT NULL COMMENT 'short name without blank',
  `spaceGroupName` varchar(45) DEFAULT NULL COMMENT 'verbose name',
  `bravaisLattice` varchar(45) DEFAULT NULL COMMENT 'short name',
  `bravaisLatticeName` varchar(45) DEFAULT NULL COMMENT 'verbose name',
  `pointGroup` varchar(45) DEFAULT NULL COMMENT 'point group',
  `MX_used` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1 if used in the crystal form'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Specimen`
--

CREATE TABLE `Specimen` (
  `specimenId` int(10) NOT NULL,
  `experimentId` int(10) NOT NULL,
  `bufferId` int(10) DEFAULT NULL,
  `macromoleculeId` int(10) DEFAULT NULL,
  `samplePlatePositionId` int(10) DEFAULT NULL,
  `safetyLevelId` int(10) DEFAULT NULL,
  `stockSolutionId` int(10) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `concentration` varchar(45) DEFAULT NULL,
  `volume` varchar(45) DEFAULT NULL,
  `comments` varchar(5120) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `StockSolution`
--

CREATE TABLE `StockSolution` (
  `stockSolutionId` int(10) NOT NULL,
  `proposalId` int(10) NOT NULL DEFAULT '-1',
  `bufferId` int(10) NOT NULL,
  `macromoleculeId` int(10) DEFAULT NULL,
  `instructionSetId` int(10) DEFAULT NULL,
  `boxId` int(10) UNSIGNED DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `storageTemperature` varchar(55) DEFAULT NULL,
  `volume` varchar(55) DEFAULT NULL,
  `concentration` varchar(55) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Stoichiometry`
--

CREATE TABLE `Stoichiometry` (
  `stoichiometryId` int(10) NOT NULL,
  `hostMacromoleculeId` int(10) NOT NULL,
  `macromoleculeId` int(10) NOT NULL,
  `ratio` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Structure`
--

CREATE TABLE `Structure` (
  `structureId` int(10) NOT NULL,
  `macromoleculeId` int(10) DEFAULT NULL,
  `crystalId` int(10) UNSIGNED DEFAULT NULL,
  `blSampleId` int(10) UNSIGNED DEFAULT NULL,
  `filePath` varchar(2048) DEFAULT NULL,
  `structureType` varchar(45) DEFAULT NULL,
  `fromResiduesBases` varchar(45) DEFAULT NULL,
  `toResiduesBases` varchar(45) DEFAULT NULL,
  `sequence` varchar(45) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `symmetry` varchar(45) DEFAULT NULL,
  `multiplicity` varchar(45) DEFAULT NULL,
  `groupName` varchar(45) DEFAULT NULL,
  `proposalId` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `SubstructureDetermination`
--

CREATE TABLE `SubstructureDetermination` (
  `substructureDeterminationId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `phasingAnalysisId` int(11) UNSIGNED NOT NULL COMMENT 'Related phasing analysis item',
  `phasingProgramRunId` int(11) UNSIGNED NOT NULL COMMENT 'Related program item',
  `spaceGroupId` int(10) UNSIGNED DEFAULT NULL COMMENT 'Related spaceGroup',
  `method` enum('SAD','MAD','SIR','SIRAS','MR','MIR','MIRAS','RIP','RIPAS') DEFAULT NULL COMMENT 'phasing method',
  `lowRes` double DEFAULT NULL,
  `highRes` double DEFAULT NULL,
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Subtraction`
--

CREATE TABLE `Subtraction` (
  `subtractionId` int(10) NOT NULL,
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
  `bufferAverageFilePath` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `SubtractionToAbInitioModel`
--

CREATE TABLE `SubtractionToAbInitioModel` (
  `subtractionToAbInitioModelId` int(10) NOT NULL,
  `abInitioId` int(10) DEFAULT NULL,
  `subtractionId` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Superposition`
--

CREATE TABLE `Superposition` (
  `superpositionId` int(11) NOT NULL,
  `subtractionId` int(11) NOT NULL,
  `abinitioModelPdbFilePath` varchar(255) DEFAULT NULL,
  `aprioriPdbFilePath` varchar(255) DEFAULT NULL,
  `alignedPdbFilePath` varchar(255) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `SW_onceToken`
--

CREATE TABLE `SW_onceToken` (
  `onceTokenId` int(11) UNSIGNED NOT NULL,
  `token` varchar(128) DEFAULT NULL,
  `personId` int(10) UNSIGNED DEFAULT NULL,
  `proposalId` int(10) UNSIGNED DEFAULT NULL,
  `validity` varchar(200) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='One-time use tokens needed for token auth in order to grant access to file downloads and webcams (and some images)';

-- --------------------------------------------------------

--
-- Table structure for table `UntrustedRegion`
--

CREATE TABLE `UntrustedRegion` (
  `untrustedRegionId` int(11) NOT NULL COMMENT 'Primary key (auto-incremented)',
  `detectorId` int(11) NOT NULL,
  `x1` int(11) NOT NULL,
  `x2` int(11) NOT NULL,
  `y1` int(11) NOT NULL,
  `y2` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Untrsuted region linked to a detector';

-- --------------------------------------------------------

--
-- Table structure for table `UserGroup`
--

CREATE TABLE `UserGroup` (
  `userGroupId` int(11) UNSIGNED NOT NULL,
  `name` varchar(31) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `UserGroup_has_Permission`
--

CREATE TABLE `UserGroup_has_Permission` (
  `userGroupId` int(11) UNSIGNED NOT NULL,
  `permissionId` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `UserGroup_has_Person`
--

CREATE TABLE `UserGroup_has_Person` (
  `userGroupId` int(11) UNSIGNED NOT NULL,
  `personId` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Stand-in structure for view `V_AnalysisInfo`
-- (See below for the actual view)
--
CREATE TABLE `V_AnalysisInfo` (
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
-- (See below for the actual view)
--
CREATE TABLE `v_datacollection` (
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
,`AutoProcIntegration_dataCollectionId` int(11) unsigned
,`autoProcScalingId` int(10) unsigned
,`cell_a` float
,`cell_b` float
,`cell_c` float
,`cell_alpha` float
,`cell_beta` float
,`cell_gamma` float
,`anomalous` tinyint(1)
,`scalingStatisticsType` enum('overall','innerShell','outerShell')
,`resolutionLimitHigh` float
,`resolutionLimitLow` float
,`completeness` float
,`AutoProc_spaceGroup` varchar(45)
,`autoProcId` int(10) unsigned
,`rMerge` float
,`AutoProcIntegration_autoProcIntegrationId` int(10) unsigned
,`AutoProcProgram_processingPrograms` varchar(255)
,`AutoProcProgram_processingStatus` enum('RUNNING','FAILED','SUCCESS','0','1')
,`AutoProcProgram_autoProcProgramId` int(10) unsigned
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection_autoprocintegration`
-- (See below for the actual view)
--
CREATE TABLE `v_datacollection_autoprocintegration` (
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
,`v_datacollection_processingStatus` enum('RUNNING','FAILED','SUCCESS','0','1')
,`v_datacollection_processingStartTime` datetime
,`v_datacollection_processingEndTime` datetime
,`v_datacollection_summary_session_sessionId` int(10) unsigned
,`v_datacollection_summary_session_proposalId` int(10) unsigned
,`AutoProcIntegration_dataCollectionId` int(11) unsigned
,`AutoProcIntegration_autoProcIntegrationId` int(10) unsigned
,`PhasingStep_phasing_phasingStepType` enum('PREPARE','SUBSTRUCTUREDETERMINATION','PHASING','MODELBUILDING','REFINEMENT','LIGAND_FIT')
,`SpaceGroup_spaceGroupShortName` varchar(45)
,`Protein_proteinId` int(10) unsigned
,`Protein_acronym` varchar(45)
,`BLSample_name` varchar(100)
,`DataCollection_dataCollectionNumber` int(10) unsigned
,`DataCollection_imagePrefix` varchar(100)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection_phasing`
-- (See below for the actual view)
--
CREATE TABLE `v_datacollection_phasing` (
`phasingStepId` int(10) unsigned
,`previousPhasingStepId` int(10) unsigned
,`phasingAnalysisId` int(10) unsigned
,`autoProcIntegrationId` int(10) unsigned
,`dataCollectionId` int(11) unsigned
,`anomalous` tinyint(1)
,`spaceGroup` varchar(45)
,`autoProcId` int(10) unsigned
,`phasingStepType` enum('PREPARE','SUBSTRUCTUREDETERMINATION','PHASING','MODELBUILDING','REFINEMENT','LIGAND_FIT')
,`method` varchar(45)
,`solventContent` varchar(45)
,`enantiomorph` varchar(45)
,`lowRes` varchar(45)
,`highRes` varchar(45)
,`autoProcScalingId` int(10) unsigned
,`spaceGroupShortName` varchar(45)
,`processingPrograms` varchar(255)
,`processingStatus` enum('RUNNING','FAILED','SUCCESS','0','1')
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
-- (See below for the actual view)
--
CREATE TABLE `v_datacollection_phasing_program_run` (
`phasingStepId` int(10) unsigned
,`previousPhasingStepId` int(10) unsigned
,`phasingAnalysisId` int(10) unsigned
,`autoProcIntegrationId` int(10) unsigned
,`dataCollectionId` int(11) unsigned
,`autoProcId` int(10) unsigned
,`phasingStepType` enum('PREPARE','SUBSTRUCTUREDETERMINATION','PHASING','MODELBUILDING','REFINEMENT','LIGAND_FIT')
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
,`fileType` enum('DSIGMA_RESOLUTION','OCCUPANCY_SITENUMBER','CONTRAST_CYCLE','CCALL_CCWEAK','IMAGE','Map','Logfile','PDB','CSV','INS','RES','TXT')
,`fileName` varchar(45)
,`filePath` varchar(255)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection_summary`
-- (See below for the actual view)
--
CREATE TABLE `v_datacollection_summary` (
`DataCollectionGroup_dataCollectionGroupId` int(11)
,`DataCollectionGroup_blSampleId` int(10) unsigned
,`DataCollectionGroup_sessionId` int(10) unsigned
,`DataCollectionGroup_workflowId` int(11) unsigned
,`DataCollectionGroup_experimentType` enum('EM','SAD','SAD - Inverse Beam','OSC','Collect - Multiwedge','MAD','Helical','Multi-positional','Mesh','Burn','MAD - Inverse Beam','Characterization','Dehydration','Still')
,`DataCollectionGroup_startTime` datetime
,`DataCollectionGroup_endTime` datetime
,`DataCollectionGroup_comments` varchar(1024)
,`DataCollectionGroup_actualSampleBarcode` varchar(45)
,`DataCollectionGroup_xtalSnapshotFullPath` varchar(255)
,`DataCollectionGroup_crystalClass` varchar(20)
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
,`Container_code` varchar(45)
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
,`DataCollection_undulatorGap1` float
,`DataCollection_undulatorGap2` float
,`DataCollection_undulatorGap3` float
,`beamSizeAtSampleX` float
,`beamSizeAtSampleY` float
,`DataCollection_slitGapVertical` float
,`DataCollection_slitGapHorizontal` float
,`DataCollection_beamShape` varchar(45)
,`DataCollection_voltage` float
,`DataCollection_xBeamPix` float
,`Workflow_workflowTitle` varchar(255)
,`Workflow_workflowType` enum('Characterisation','Undefined','BioSAXS Post Processing','EnhancedCharacterisation','LineScan','MeshScan','Dehydration','KappaReorientation','BurnStrategy','XrayCentering','DiffractionTomography','TroubleShooting','VisualReorientation','HelicalCharacterisation','GroupedProcessing','MXPressE','MXPressO','MXPressL','MXScore','MXPressI','MXPressM','MXPressA','CollectAndSpectra','LowDoseDC','EnergyInterleavedMAD','MXPressF','MXPressH','MXPressP','MXPressP_SAD','MXPressR','MXPressR_180','MXPressR_dehydration','MeshAndCollect','MeshAndCollectFromFile')
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
,`anomalous` tinyint(1)
,`scalingStatisticsType` enum('overall','innerShell','outerShell')
,`resolutionLimitHigh` float
,`resolutionLimitLow` float
,`completeness` float
,`AutoProc_spaceGroup` varchar(45)
,`autoProcId` int(10) unsigned
,`rMerge` float
,`AutoProcIntegration_autoProcIntegrationId` int(10) unsigned
,`AutoProcProgram_processingPrograms` varchar(255)
,`AutoProcProgram_processingStatus` enum('RUNNING','FAILED','SUCCESS','0','1')
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
,`ScreeningOutput_totalExposureTime` double
,`ScreeningOutput_totalRotationRange` double
,`ScreeningOutput_totalNumberOfImages` int(11)
,`ScreeningStrategySubWedge_exposureTime` float
,`ScreeningStrategySubWedge_transmission` float
,`ScreeningStrategySubWedge_oscillationRange` float
,`ScreeningStrategySubWedge_numberOfImages` int(10) unsigned
,`ScreeningStrategySubWedge_multiplicity` float
,`ScreeningStrategySubWedge_completeness` float
,`ScreeningStrategySubWedge_axisStart` float
,`Shipping_shippingId` int(10) unsigned
,`Shipping_shippingName` varchar(45)
,`Shipping_shippingStatus` varchar(45)
,`diffractionPlanId` int(10) unsigned
,`experimentKind` enum('Default','MXPressE','MXPressF','MXPressO','MXPressP','MXPressP_SAD','MXPressI','MXPressE_SAD','MXScore','MXPressM','MAD','SAD','Fixed','Ligand binding','Refinement','OSC','MAD - Inverse Beam','SAD - Inverse Beam')
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
,`Detector_detectorType` varchar(255)
,`Detector_detectorManufacturer` varchar(255)
,`Detector_detectorModel` varchar(255)
,`Detector_detectorPixelSizeHorizontal` float
,`Detector_detectorPixelSizeVertical` float
,`Detector_detectorSerialNumber` varchar(30)
,`Detector_detectorDistanceMin` double
,`Detector_detectorDistanceMax` double
,`Detector_trustedPixelValueRangeLower` double
,`Detector_trustedPixelValueRangeUpper` double
,`Detector_sensorThickness` float
,`Detector_overload` float
,`Detector_XGeoCorr` varchar(255)
,`Detector_YGeoCorr` varchar(255)
,`Detector_detectorMode` varchar(255)
,`BeamLineSetup_undulatorType1` varchar(45)
,`BeamLineSetup_undulatorType2` varchar(45)
,`BeamLineSetup_undulatorType3` varchar(45)
,`BeamLineSetup_synchrotronName` varchar(255)
,`BeamLineSetup_synchrotronMode` varchar(255)
,`BeamLineSetup_polarisation` float
,`BeamLineSetup_focusingOptic` varchar(255)
,`BeamLineSetup_beamDivergenceHorizontal` float
,`BeamLineSetup_beamDivergenceVertical` float
,`BeamLineSetup_monochromatorType` varchar(255)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection_summary_autoprocintegration`
-- (See below for the actual view)
--
CREATE TABLE `v_datacollection_summary_autoprocintegration` (
`AutoProcIntegration_dataCollectionId` int(11) unsigned
,`cell_a` float
,`cell_b` float
,`cell_c` float
,`cell_alpha` float
,`cell_beta` float
,`cell_gamma` float
,`anomalous` tinyint(1)
,`AutoProcIntegration_autoProcIntegrationId` int(10) unsigned
,`v_datacollection_summary_autoprocintegration_processingPrograms` varchar(255)
,`AutoProcProgram_autoProcProgramId` int(10) unsigned
,`v_datacollection_summary_autoprocintegration_processingStatus` enum('RUNNING','FAILED','SUCCESS','0','1')
,`AutoProcIntegration_phasing_dataCollectionId` int(11) unsigned
,`PhasingStep_phasing_phasingStepType` enum('PREPARE','SUBSTRUCTUREDETERMINATION','PHASING','MODELBUILDING','REFINEMENT','LIGAND_FIT')
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
-- (See below for the actual view)
--
CREATE TABLE `v_datacollection_summary_datacollectiongroup` (
`DataCollectionGroup_dataCollectionGroupId` int(11)
,`DataCollectionGroup_blSampleId` int(10) unsigned
,`DataCollectionGroup_sessionId` int(10) unsigned
,`DataCollectionGroup_workflowId` int(11) unsigned
,`DataCollectionGroup_experimentType` enum('EM','SAD','SAD - Inverse Beam','OSC','Collect - Multiwedge','MAD','Helical','Multi-positional','Mesh','Burn','MAD - Inverse Beam','Characterization','Dehydration','Still')
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
,`Workflow_workflowType` enum('Characterisation','Undefined','BioSAXS Post Processing','EnhancedCharacterisation','LineScan','MeshScan','Dehydration','KappaReorientation','BurnStrategy','XrayCentering','DiffractionTomography','TroubleShooting','VisualReorientation','HelicalCharacterisation','GroupedProcessing','MXPressE','MXPressO','MXPressL','MXScore','MXPressI','MXPressM','MXPressA','CollectAndSpectra','LowDoseDC','EnergyInterleavedMAD','MXPressF','MXPressH','MXPressP','MXPressP_SAD','MXPressR','MXPressR_180','MXPressR_dehydration','MeshAndCollect','MeshAndCollectFromFile')
,`Workflow_status` varchar(255)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection_summary_phasing`
-- (See below for the actual view)
--
CREATE TABLE `v_datacollection_summary_phasing` (
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
,`v_datacollection_summary_phasing_processingStatus` enum('RUNNING','FAILED','SUCCESS','0','1')
,`v_datacollection_summary_session_sessionId` int(10) unsigned
,`v_datacollection_summary_session_proposalId` int(10) unsigned
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection_summary_screening`
-- (See below for the actual view)
--
CREATE TABLE `v_datacollection_summary_screening` (
`Screening_screeningId` int(10) unsigned
,`Screening_dataCollectionId` int(11) unsigned
,`Screening_dataCollectionGroupId` int(11)
,`ScreeningOutput_strategySuccess` tinyint(1)
,`ScreeningOutput_indexingSuccess` tinyint(1)
,`ScreeningOutput_rankingResolution` double
,`ScreeningOutput_mosaicityEstimated` tinyint(1)
,`ScreeningOutput_mosaicity` float
,`ScreeningOutput_totalExposureTime` double
,`ScreeningOutput_totalRotationRange` double
,`ScreeningOutput_totalNumberOfImages` int(11)
,`ScreeningOutputLattice_spaceGroup` varchar(45)
,`ScreeningOutputLattice_unitCell_a` float
,`ScreeningOutputLattice_unitCell_b` float
,`ScreeningOutputLattice_unitCell_c` float
,`ScreeningOutputLattice_unitCell_alpha` float
,`ScreeningOutputLattice_unitCell_beta` float
,`ScreeningOutputLattice_unitCell_gamma` float
,`ScreeningStrategySubWedge_exposureTime` float
,`ScreeningStrategySubWedge_transmission` float
,`ScreeningStrategySubWedge_oscillationRange` float
,`ScreeningStrategySubWedge_numberOfImages` int(10) unsigned
,`ScreeningStrategySubWedge_multiplicity` float
,`ScreeningStrategySubWedge_completeness` float
,`ScreeningStrategySubWedge_axisStart` float
,`ScreeningStrategySubWedge_axisEnd` float
,`ScreeningStrategySubWedge_rotationAxis` varchar(45)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_dewar`
-- (See below for the actual view)
--
CREATE TABLE `v_dewar` (
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
-- (See below for the actual view)
--
CREATE TABLE `v_dewarBeamline` (
`beamLineName` varchar(45)
,`COUNT(*)` bigint(21)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_dewarBeamlineByWeek`
-- (See below for the actual view)
--
CREATE TABLE `v_dewarBeamlineByWeek` (
`Week` varchar(23)
,`ID14` bigint(21)
,`ID23` bigint(21)
,`ID29` bigint(21)
,`BM14` bigint(21)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_dewarByWeek`
-- (See below for the actual view)
--
CREATE TABLE `v_dewarByWeek` (
`Week` varchar(23)
,`Dewars Tracked` bigint(21)
,`Dewars Non-Tracked` bigint(21)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_dewarByWeekTotal`
-- (See below for the actual view)
--
CREATE TABLE `v_dewarByWeekTotal` (
`Week` varchar(23)
,`Dewars Tracked` bigint(21)
,`Dewars Non-Tracked` bigint(21)
,`Total` bigint(21)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_dewarList`
-- (See below for the actual view)
--
CREATE TABLE `v_dewarList` (
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
-- (See below for the actual view)
--
CREATE TABLE `v_dewarProposalCode` (
`proposalCode` varchar(45)
,`COUNT(*)` bigint(21)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_dewarProposalCodeByWeek`
-- (See below for the actual view)
--
CREATE TABLE `v_dewarProposalCodeByWeek` (
`Week` varchar(23)
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
-- (See below for the actual view)
--
CREATE TABLE `v_dewar_summary` (
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
,`isReimbursed` tinyint(1)
,`sessionId` int(10) unsigned
,`beamlineName` varchar(45)
,`sessionStartDate` datetime
,`sessionEndDate` datetime
,`beamLineOperator` varchar(255)
,`nbReimbDewars` int(10)
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
-- Stand-in structure for view `v_em_movie`
-- (See below for the actual view)
--
CREATE TABLE `v_em_movie` (
`Movie_movieId` int(11)
,`Movie_dataCollectionId` int(11) unsigned
,`Movie_movieNumber` int(11)
,`Movie_movieFullPath` varchar(255)
,`Movie_positionX` varchar(45)
,`Movie_positionY` varchar(45)
,`Movie_micrographFullPath` varchar(255)
,`Movie_micrographSnapshotFullPath` varchar(255)
,`Movie_xmlMetaDataFullPath` varchar(255)
,`Movie_dosePerImage` varchar(45)
,`Movie_createdTimeStamp` timestamp
,`MotionCorrection_motionCorrectionId` int(11)
,`MotionCorrection_movieId` int(11)
,`MotionCorrection_firstFrame` varchar(45)
,`MotionCorrection_lastFrame` varchar(45)
,`MotionCorrection_dosePerFrame` varchar(45)
,`MotionCorrection_doseWeight` varchar(45)
,`MotionCorrection_totalMotion` varchar(45)
,`MotionCorrection_averageMotionPerFrame` varchar(45)
,`MotionCorrection_driftPlotFullPath` varchar(512)
,`MotionCorrection_micrographFullPath` varchar(512)
,`MotionCorrection_micrographSnapshotFullPath` varchar(512)
,`MotionCorrection_correctedDoseMicrographFullPath` varchar(512)
,`MotionCorrection_patchesUsed` varchar(45)
,`MotionCorrection_logFileFullPath` varchar(512)
,`CTF_CTFid` int(11)
,`CTF_motionCorrectionId` int(11)
,`CTF_spectraImageThumbnailFullPath` varchar(512)
,`CTF_spectraImageFullPath` varchar(512)
,`CTF_defocusU` varchar(45)
,`CTF_defocusV` varchar(45)
,`CTF_angle` varchar(45)
,`CTF_crossCorrelationCoefficient` varchar(45)
,`CTF_resolutionLimit` varchar(45)
,`CTF_estimatedBfactor` varchar(45)
,`CTF_logFilePath` varchar(512)
,`CTF_createdTimeStamp` timestamp
,`DataCollection_xBeamPix` float
,`Proposal_proposalId` int(10) unsigned
,`BLSession_sessionId` int(10) unsigned
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_em_stats`
-- (See below for the actual view)
--
CREATE TABLE `v_em_stats` (
`proposalId` int(10) unsigned
,`sessionId` int(10) unsigned
,`imageDirectory` varchar(255)
,`movieId` int(11)
,`createdTimeStamp` timestamp
,`motionCorrectionId` int(11)
,`dataCollectionId` int(11) unsigned
,`totalMotion` varchar(45)
,`averageMotionPerFrame` varchar(45)
,`lastFrame` varchar(45)
,`dosePerFrame` varchar(45)
,`defocusU` varchar(45)
,`defocusV` varchar(45)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_energyScan`
-- (See below for the actual view)
--
CREATE TABLE `v_energyScan` (
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
-- (See below for the actual view)
--
CREATE TABLE `v_hour` (
`num` varchar(18)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_Log4Stat`
-- (See below for the actual view)
--
CREATE TABLE `v_Log4Stat` (
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
-- (See below for the actual view)
--
CREATE TABLE `v_logonByHour` (
`Hour` varchar(7)
,`Distinct logins` bigint(21)
,`Total logins` bigint(22)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_logonByMonthDay`
-- (See below for the actual view)
--
CREATE TABLE `v_logonByMonthDay` (
`Day` varchar(5)
,`Distinct logins` bigint(21)
,`Total logins` bigint(22)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_logonByWeek`
-- (See below for the actual view)
--
CREATE TABLE `v_logonByWeek` (
`Week` varchar(23)
,`Distinct logins` bigint(21)
,`Total logins` bigint(22)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_logonByWeekDay`
-- (See below for the actual view)
--
CREATE TABLE `v_logonByWeekDay` (
`Day` varchar(64)
,`Distinct logins` bigint(21)
,`Total logins` bigint(22)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_monthDay`
-- (See below for the actual view)
--
CREATE TABLE `v_monthDay` (
`num` varchar(10)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_mx_autoprocessing_stats`
-- (See below for the actual view)
--
CREATE TABLE `v_mx_autoprocessing_stats` (
`autoProcScalingStatisticsId` int(10) unsigned
,`autoProcScalingId` int(10) unsigned
,`scalingStatisticsType` enum('overall','innerShell','outerShell')
,`resolutionLimitLow` float
,`resolutionLimitHigh` float
,`rMerge` float
,`rMeasWithinIPlusIMinus` float
,`rMeasAllIPlusIMinus` float
,`rPimWithinIPlusIMinus` float
,`rPimAllIPlusIMinus` float
,`fractionalPartialBias` float
,`nTotalObservations` int(10)
,`nTotalUniqueObservations` int(10)
,`meanIOverSigI` float
,`completeness` float
,`multiplicity` float
,`anomalousCompleteness` float
,`anomalousMultiplicity` float
,`recordTimeStamp` datetime
,`anomalous` tinyint(1)
,`ccHalf` float
,`ccAno` float
,`sigAno` varchar(45)
,`ISA` varchar(45)
,`dataCollectionId` int(11) unsigned
,`strategySubWedgeOrigId` int(10) unsigned
,`detectorId` int(11)
,`blSubSampleId` int(11) unsigned
,`dataCollectionNumber` int(10) unsigned
,`startTime` datetime
,`endTime` datetime
,`sessionId` int(10) unsigned
,`proposalId` int(10) unsigned
,`beamLineName` varchar(45)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_mx_sample`
-- (See below for the actual view)
--
CREATE TABLE `v_mx_sample` (
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
,`Container_beamlineLocation` varchar(20)
,`Container_sampleChangerLocation` varchar(20)
,`Dewar_code` varchar(45)
,`Dewar_dewarId` int(10) unsigned
,`Dewar_storageLocation` varchar(45)
,`Dewar_dewarStatus` varchar(45)
,`Dewar_barCode` varchar(45)
,`Shipping_shippingId` int(10) unsigned
,`sessionId` int(10) unsigned
,`BLSession_startDate` datetime
,`BLSession_beamLineName` varchar(45)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_phasing`
-- (See below for the actual view)
--
CREATE TABLE `v_phasing` (
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
,`PhasingStep_phasingStepType` enum('PREPARE','SUBSTRUCTUREDETERMINATION','PHASING','MODELBUILDING','REFINEMENT','LIGAND_FIT')
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
,`PhasingProgramRun_phasingDirectory` varchar(255)
,`PhasingProgramRun_recordTimeStamp` timestamp
,`Protein_proteinId` int(10) unsigned
,`BLSession_sessionId` int(10) unsigned
,`BLSession_proposalId` int(10) unsigned
,`PhasingStatistics_phasingStatisticsId` int(11) unsigned
,`PhasingStatistics_metric` enum('Rcullis','Average Fragment Length','Chain Count','Residues Count','CC','PhasingPower','FOM','<d"/sig>','Best CC','CC(1/2)','Weak CC','CFOM','Pseudo_free_CC','CC of partial model','Start R-work','Start R-free','Final R-work','Final R-free')
,`PhasingStatistics_statisticsValue` double
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_sample`
-- (See below for the actual view)
--
CREATE TABLE `v_sample` (
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
-- (See below for the actual view)
--
CREATE TABLE `v_sampleByWeek` (
`Week` varchar(23)
,`Samples` bigint(21)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_saxs_datacollection`
-- (See below for the actual view)
--
CREATE TABLE `v_saxs_datacollection` (
`Subtraction_subtractionId` int(10)
,`MeasurementToDataCollection_dataCollectionId` int(10)
,`MeasurementToDataCollection_dataCollectionOrder` int(10)
,`MeasurementToDataCollection_measurementToDataCollectionId` int(10)
,`Specimen_specimenId` int(10)
,`Measurement_code` varchar(100)
,`Measurement_measurementId` int(10)
,`Buffer_bufferId` int(10)
,`Buffer_proposalId` int(10)
,`Buffer_safetyLevelId` int(10)
,`Buffer_name` varchar(45)
,`Buffer_acronym` varchar(45)
,`Buffer_pH` varchar(45)
,`Buffer_composition` varchar(45)
,`Buffer_comments` varchar(512)
,`Macromolecule_macromoleculeId` int(11)
,`Macromolecule_proposalId` int(10) unsigned
,`Macromolecule_safetyLevelId` int(10)
,`Macromolecule_name` varchar(45)
,`Macromolecule_acronym` varchar(45)
,`Macromolecule_extintionCoefficient` varchar(45)
,`Macromolecule_molecularMass` varchar(45)
,`Macromolecule_sequence` varchar(1000)
,`Macromolecule_contactsDescriptionFilePath` varchar(255)
,`Macromolecule_symmetry` varchar(45)
,`Macromolecule_comments` varchar(1024)
,`Macromolecule_refractiveIndex` varchar(45)
,`Macromolecule_solventViscosity` varchar(45)
,`Macromolecule_creationDate` datetime
,`Specimen_experimentId` int(10)
,`Specimen_bufferId` int(10)
,`Specimen_samplePlatePositionId` int(10)
,`Specimen_safetyLevelId` int(10)
,`Specimen_stockSolutionId` int(10)
,`Specimen_code` varchar(255)
,`Specimen_concentration` varchar(45)
,`Specimen_volume` varchar(45)
,`Specimen_comments` varchar(5120)
,`SamplePlatePosition_samplePlatePositionId` int(10)
,`SamplePlatePosition_samplePlateId` int(10)
,`SamplePlatePosition_rowNumber` int(11)
,`SamplePlatePosition_columnNumber` int(11)
,`SamplePlatePosition_volume` varchar(45)
,`samplePlateId` int(10)
,`experimentId` int(10)
,`plateGroupId` int(10)
,`plateTypeId` int(10)
,`instructionSetId` int(10)
,`SamplePlate_boxId` int(10) unsigned
,`SamplePlate_name` varchar(45)
,`SamplePlate_slotPositionRow` varchar(45)
,`SamplePlate_slotPositionColumn` varchar(45)
,`SamplePlate_storageTemperature` varchar(45)
,`Experiment_experimentId` int(11)
,`Experiment_sessionId` int(10) unsigned
,`Experiment_proposalId` int(10)
,`Experiment_name` varchar(255)
,`Experiment_creationDate` datetime
,`Experiment_experimentType` varchar(128)
,`Experiment_sourceFilePath` varchar(256)
,`Experiment_dataAcquisitionFilePath` varchar(256)
,`Experiment_status` varchar(45)
,`Experiment_comments` varchar(512)
,`Measurement_priorityLevelId` int(10)
,`Measurement_exposureTemperature` varchar(45)
,`Measurement_viscosity` varchar(45)
,`Measurement_flow` tinyint(1)
,`Measurement_extraFlowTime` varchar(45)
,`Measurement_volumeToLoad` varchar(45)
,`Measurement_waitTime` varchar(45)
,`Measurement_transmission` varchar(45)
,`Measurement_comments` varchar(512)
,`Measurement_imageDirectory` varchar(512)
,`Run_runId` int(10)
,`Run_timePerFrame` varchar(45)
,`Run_timeStart` varchar(45)
,`Run_timeEnd` varchar(45)
,`Run_storageTemperature` varchar(45)
,`Run_exposureTemperature` varchar(45)
,`Run_spectrophotometer` varchar(45)
,`Run_energy` varchar(45)
,`Run_creationDate` datetime
,`Run_frameAverage` varchar(45)
,`Run_frameCount` varchar(45)
,`Run_transmission` varchar(45)
,`Run_beamCenterX` varchar(45)
,`Run_beamCenterY` varchar(45)
,`Run_pixelSizeX` varchar(45)
,`Run_pixelSizeY` varchar(45)
,`Run_radiationRelative` varchar(45)
,`Run_radiationAbsolute` varchar(45)
,`Run_normalization` varchar(45)
,`Merge_mergeId` int(10)
,`Merge_measurementId` int(10)
,`Merge_frameListId` int(10)
,`Merge_discardedFrameNameList` varchar(1024)
,`Merge_averageFilePath` varchar(255)
,`Merge_framesCount` varchar(45)
,`Merge_framesMerge` varchar(45)
,`Merge_creationDate` datetime
,`Subtraction_dataCollectionId` int(10)
,`Subtraction_rg` varchar(45)
,`Subtraction_rgStdev` varchar(45)
,`Subtraction_I0` varchar(45)
,`Subtraction_I0Stdev` varchar(45)
,`Subtraction_firstPointUsed` varchar(45)
,`Subtraction_lastPointUsed` varchar(45)
,`Subtraction_quality` varchar(45)
,`Subtraction_isagregated` varchar(45)
,`Subtraction_concentration` varchar(45)
,`Subtraction_gnomFilePath` varchar(255)
,`Subtraction_rgGuinier` varchar(45)
,`Subtraction_rgGnom` varchar(45)
,`Subtraction_dmax` varchar(45)
,`Subtraction_total` varchar(45)
,`Subtraction_volume` varchar(45)
,`Subtraction_creationTime` datetime
,`Subtraction_kratkyFilePath` varchar(255)
,`Subtraction_scatteringFilePath` varchar(255)
,`Subtraction_guinierFilePath` varchar(255)
,`Subtraction_substractedFilePath` varchar(255)
,`Subtraction_gnomFilePathOutput` varchar(255)
,`Subtraction_sampleOneDimensionalFiles` int(10)
,`Subtraction_bufferOnedimensionalFiles` int(10)
,`Subtraction_sampleAverageFilePath` varchar(255)
,`Subtraction_bufferAverageFilePath` varchar(255)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_session`
-- (See below for the actual view)
--
CREATE TABLE `v_session` (
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
,`beamLineOperator` varchar(255)
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
-- Stand-in structure for view `v_tracking_shipment_history`
-- (See below for the actual view)
--
CREATE TABLE `v_tracking_shipment_history` (
`Dewar_dewarId` int(10) unsigned
,`Dewar_code` varchar(45)
,`Dewar_comments` tinytext
,`Dewar_dewarStatus` varchar(45)
,`Dewar_barCode` varchar(45)
,`Dewar_firstExperimentId` int(10) unsigned
,`Dewar_trackingNumberToSynchrotron` varchar(30)
,`Dewar_trackingNumberFromSynchrotron` varchar(30)
,`Dewar_type` enum('Dewar','Toolbox')
,`Shipping_shippingId` int(10) unsigned
,`Shipping_proposalId` int(10) unsigned
,`Shipping_shippingName` varchar(45)
,`deliveryAgent_agentName` varchar(45)
,`Shipping_deliveryAgent_shippingDate` date
,`Shipping_deliveryAgent_deliveryDate` date
,`Shipping_shippingStatus` varchar(45)
,`Shipping_returnCourier` varchar(45)
,`Shipping_dateOfShippingToUser` datetime
,`DewarTransportHistory_DewarTransportHistoryId` int(10) unsigned
,`DewarTransportHistory_dewarStatus` varchar(45)
,`DewarTransportHistory_storageLocation` varchar(45)
,`DewarTransportHistory_arrivalDate` datetime
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_week`
-- (See below for the actual view)
--
CREATE TABLE `v_week` (
`num` varchar(7)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_weekDay`
-- (See below for the actual view)
--
CREATE TABLE `v_weekDay` (
`day` varchar(10)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_xfeFluorescenceSpectrum`
-- (See below for the actual view)
--
CREATE TABLE `v_xfeFluorescenceSpectrum` (
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

CREATE TABLE `Workflow` (
  `workflowId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `workflowTitle` varchar(255) DEFAULT NULL,
  `workflowType` enum('Characterisation','Undefined','BioSAXS Post Processing','EnhancedCharacterisation','LineScan','MeshScan','Dehydration','KappaReorientation','BurnStrategy','XrayCentering','DiffractionTomography','TroubleShooting','VisualReorientation','HelicalCharacterisation','GroupedProcessing','MXPressE','MXPressO','MXPressL','MXScore','MXPressI','MXPressM','MXPressA','CollectAndSpectra','LowDoseDC','EnergyInterleavedMAD','MXPressF','MXPressH','MXPressP','MXPressP_SAD','MXPressR','MXPressR_180','MXPressR_dehydration','MeshAndCollect','MeshAndCollectFromFile') DEFAULT NULL,
  `workflowTypeId` int(11) DEFAULT NULL,
  `comments` varchar(1024) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `resultFilePath` varchar(255) DEFAULT NULL,
  `logFilePath` varchar(255) DEFAULT NULL,
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `WorkflowDehydration`
--

CREATE TABLE `WorkflowDehydration` (
  `workflowDehydrationId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `workflowId` int(11) UNSIGNED NOT NULL COMMENT 'Related workflow',
  `dataFilePath` varchar(255) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `WorkflowMesh`
--

CREATE TABLE `WorkflowMesh` (
  `workflowMeshId` int(11) UNSIGNED NOT NULL COMMENT 'Primary key (auto-incremented)',
  `workflowId` int(11) UNSIGNED NOT NULL COMMENT 'Related workflow',
  `bestPositionId` int(11) UNSIGNED DEFAULT NULL,
  `bestImageId` int(12) UNSIGNED DEFAULT NULL,
  `value1` double DEFAULT NULL,
  `value2` double DEFAULT NULL,
  `value3` double DEFAULT NULL COMMENT 'N value',
  `value4` double DEFAULT NULL,
  `cartographyPath` varchar(255) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `WorkflowStep`
--

CREATE TABLE `WorkflowStep` (
  `workflowStepId` int(11) NOT NULL,
  `workflowId` int(11) UNSIGNED NOT NULL,
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
  `recordTimeStamp` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `WorkflowType`
--

CREATE TABLE `WorkflowType` (
  `workflowTypeId` int(11) NOT NULL,
  `workflowTypeName` varchar(45) DEFAULT NULL,
  `comments` varchar(2048) DEFAULT NULL,
  `recordTimeStamp` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `XFEFluorescenceSpectrum`
--

CREATE TABLE `XFEFluorescenceSpectrum` (
  `xfeFluorescenceSpectrumId` int(10) UNSIGNED NOT NULL,
  `sessionId` int(10) UNSIGNED NOT NULL,
  `blSampleId` int(10) UNSIGNED DEFAULT NULL,
  `fittedDataFileFullPath` varchar(255) DEFAULT NULL,
  `scanFileFullPath` varchar(255) DEFAULT NULL,
  `jpegScanFileFullPath` varchar(255) DEFAULT NULL,
  `startTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `energy` float DEFAULT NULL,
  `exposureTime` float DEFAULT NULL,
  `axisPosition` float DEFAULT NULL,
  `beamTransmission` float DEFAULT NULL,
  `annotatedPymcaXfeSpectrum` varchar(255) DEFAULT NULL,
  `beamSizeVertical` float DEFAULT NULL,
  `beamSizeHorizontal` float DEFAULT NULL,
  `crystalClass` varchar(20) DEFAULT NULL,
  `comments` varchar(1024) DEFAULT NULL,
  `flux` double DEFAULT NULL COMMENT 'flux measured before the xrfSpectra',
  `flux_end` double DEFAULT NULL COMMENT 'flux measured after the xrfSpectra',
  `workingDirectory` varchar(512) DEFAULT NULL,
  `blSubSampleId` int(11) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `XRFFluorescenceMapping`
--

CREATE TABLE `XRFFluorescenceMapping` (
  `xrfFluorescenceMappingId` int(11) UNSIGNED NOT NULL,
  `xrfFluorescenceMappingROIId` int(11) UNSIGNED NOT NULL,
  `dataCollectionId` int(11) UNSIGNED NOT NULL,
  `imageNumber` int(10) UNSIGNED NOT NULL,
  `counts` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `XRFFluorescenceMappingROI`
--

CREATE TABLE `XRFFluorescenceMappingROI` (
  `xrfFluorescenceMappingROIId` int(11) UNSIGNED NOT NULL,
  `startEnergy` float NOT NULL,
  `endEnergy` float NOT NULL,
  `element` varchar(2) DEFAULT NULL,
  `edge` varchar(2) DEFAULT NULL COMMENT 'In future may be changed to enum(K, L)',
  `r` tinyint(3) UNSIGNED DEFAULT NULL COMMENT 'R colour component',
  `g` tinyint(3) UNSIGNED DEFAULT NULL COMMENT 'G colour component',
  `b` tinyint(3) UNSIGNED DEFAULT NULL COMMENT 'B colour component'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure for view `V_AnalysisInfo`
--
DROP TABLE IF EXISTS `V_AnalysisInfo`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'V_AnalysisInfo')

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection`
--
DROP TABLE IF EXISTS `v_datacollection`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_datacollection')

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_autoprocintegration`
--
DROP TABLE IF EXISTS `v_datacollection_autoprocintegration`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_datacollection_autoprocintegration')

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_phasing`
--
DROP TABLE IF EXISTS `v_datacollection_phasing`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_datacollection_phasing')

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_phasing_program_run`
--
DROP TABLE IF EXISTS `v_datacollection_phasing_program_run`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_datacollection_phasing_program_run')

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_summary`
--
DROP TABLE IF EXISTS `v_datacollection_summary`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_datacollection_summary')

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_summary_autoprocintegration`
--
DROP TABLE IF EXISTS `v_datacollection_summary_autoprocintegration`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_datacollection_summary_autoprocintegration')

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_summary_datacollectiongroup`
--
DROP TABLE IF EXISTS `v_datacollection_summary_datacollectiongroup`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_datacollection_summary_datacollectiongroup')

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_summary_phasing`
--
DROP TABLE IF EXISTS `v_datacollection_summary_phasing`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_datacollection_summary_phasing')

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_summary_screening`
--
DROP TABLE IF EXISTS `v_datacollection_summary_screening`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_datacollection_summary_screening')

-- --------------------------------------------------------

--
-- Structure for view `v_dewar`
--
DROP TABLE IF EXISTS `v_dewar`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_dewar')

-- --------------------------------------------------------

--
-- Structure for view `v_dewarBeamline`
--
DROP TABLE IF EXISTS `v_dewarBeamline`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_dewarBeamline')

-- --------------------------------------------------------

--
-- Structure for view `v_dewarBeamlineByWeek`
--
DROP TABLE IF EXISTS `v_dewarBeamlineByWeek`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_dewarBeamlineByWeek')

-- --------------------------------------------------------

--
-- Structure for view `v_dewarByWeek`
--
DROP TABLE IF EXISTS `v_dewarByWeek`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_dewarByWeek')

-- --------------------------------------------------------

--
-- Structure for view `v_dewarByWeekTotal`
--
DROP TABLE IF EXISTS `v_dewarByWeekTotal`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_dewarByWeekTotal')

-- --------------------------------------------------------

--
-- Structure for view `v_dewarList`
--
DROP TABLE IF EXISTS `v_dewarList`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_dewarList')

-- --------------------------------------------------------

--
-- Structure for view `v_dewarProposalCode`
--
DROP TABLE IF EXISTS `v_dewarProposalCode`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_dewarProposalCode')

-- --------------------------------------------------------

--
-- Structure for view `v_dewarProposalCodeByWeek`
--
DROP TABLE IF EXISTS `v_dewarProposalCodeByWeek`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_dewarProposalCodeByWeek')

-- --------------------------------------------------------

--
-- Structure for view `v_dewar_summary`
--
DROP TABLE IF EXISTS `v_dewar_summary`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_dewar_summary')

-- --------------------------------------------------------

--
-- Structure for view `v_em_movie`
--
DROP TABLE IF EXISTS `v_em_movie`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_em_movie')

-- --------------------------------------------------------

--
-- Structure for view `v_em_stats`
--
DROP TABLE IF EXISTS `v_em_stats`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_em_stats')

-- --------------------------------------------------------

--
-- Structure for view `v_energyScan`
--
DROP TABLE IF EXISTS `v_energyScan`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_energyScan')

-- --------------------------------------------------------

--
-- Structure for view `v_hour`
--
DROP TABLE IF EXISTS `v_hour`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_hour')

-- --------------------------------------------------------

--
-- Structure for view `v_Log4Stat`
--
DROP TABLE IF EXISTS `v_Log4Stat`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_Log4Stat')

-- --------------------------------------------------------

--
-- Structure for view `v_logonByHour`
--
DROP TABLE IF EXISTS `v_logonByHour`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_logonByHour')

-- --------------------------------------------------------

--
-- Structure for view `v_logonByMonthDay`
--
DROP TABLE IF EXISTS `v_logonByMonthDay`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_logonByMonthDay')

-- --------------------------------------------------------

--
-- Structure for view `v_logonByWeek`
--
DROP TABLE IF EXISTS `v_logonByWeek`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_logonByWeek')

-- --------------------------------------------------------

--
-- Structure for view `v_logonByWeekDay`
--
DROP TABLE IF EXISTS `v_logonByWeekDay`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_logonByWeekDay')

-- --------------------------------------------------------

--
-- Structure for view `v_monthDay`
--
DROP TABLE IF EXISTS `v_monthDay`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_monthDay')

-- --------------------------------------------------------

--
-- Structure for view `v_mx_autoprocessing_stats`
--
DROP TABLE IF EXISTS `v_mx_autoprocessing_stats`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_mx_autoprocessing_stats')

-- --------------------------------------------------------

--
-- Structure for view `v_mx_sample`
--
DROP TABLE IF EXISTS `v_mx_sample`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_mx_sample')

-- --------------------------------------------------------

--
-- Structure for view `v_phasing`
--
DROP TABLE IF EXISTS `v_phasing`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_phasing')

-- --------------------------------------------------------

--
-- Structure for view `v_sample`
--
DROP TABLE IF EXISTS `v_sample`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_sample')

-- --------------------------------------------------------

--
-- Structure for view `v_sampleByWeek`
--
DROP TABLE IF EXISTS `v_sampleByWeek`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_sampleByWeek')

-- --------------------------------------------------------

--
-- Structure for view `v_saxs_datacollection`
--
DROP TABLE IF EXISTS `v_saxs_datacollection`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_saxs_datacollection')

-- --------------------------------------------------------

--
-- Structure for view `v_session`
--
DROP TABLE IF EXISTS `v_session`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_session')

-- --------------------------------------------------------

--
-- Structure for view `v_tracking_shipment_history`
--
DROP TABLE IF EXISTS `v_tracking_shipment_history`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_tracking_shipment_history')

-- --------------------------------------------------------

--
-- Structure for view `v_week`
--
DROP TABLE IF EXISTS `v_week`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_week')

-- --------------------------------------------------------

--
-- Structure for view `v_weekDay`
--
DROP TABLE IF EXISTS `v_weekDay`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_weekDay')

-- --------------------------------------------------------

--
-- Structure for view `v_xfeFluorescenceSpectrum`
--
DROP TABLE IF EXISTS `v_xfeFluorescenceSpectrum`;
-- in use(#1142 - SHOW VIEW command denied to user 'pxuser'@'localhost' for table 'v_xfeFluorescenceSpectrum')

--
-- Indexes for dumped tables
--

--
-- Indexes for table `AbInitioModel`
--
ALTER TABLE `AbInitioModel`
  ADD PRIMARY KEY (`abInitioModelId`),
  ADD KEY `AbInitioModelToModelList` (`modelListId`),
  ADD KEY `AverageToModel` (`averagedModelId`),
  ADD KEY `AbInitioModelToRapid` (`rapidShapeDeterminationModelId`),
  ADD KEY `SahpeDeterminationToAbiniti` (`shapeDeterminationModelId`);

--
-- Indexes for table `Additive`
--
ALTER TABLE `Additive`
  ADD PRIMARY KEY (`additiveId`);

--
-- Indexes for table `AdminActivity`
--
ALTER TABLE `AdminActivity`
  ADD PRIMARY KEY (`adminActivityId`),
  ADD UNIQUE KEY `username` (`username`),
  ADD KEY `AdminActivity_FKAction` (`action`);

--
-- Indexes for table `AdminVar`
--
ALTER TABLE `AdminVar`
  ADD PRIMARY KEY (`varId`),
  ADD KEY `AdminVar_FKIndexName` (`name`),
  ADD KEY `AdminVar_FKIndexValue` (`value`(767));

--
-- Indexes for table `Aperture`
--
ALTER TABLE `Aperture`
  ADD PRIMARY KEY (`apertureId`);

--
-- Indexes for table `Assembly`
--
ALTER TABLE `Assembly`
  ADD PRIMARY KEY (`assemblyId`),
  ADD KEY `AssemblyToMacromolecule` (`macromoleculeId`);

--
-- Indexes for table `AssemblyHasMacromolecule`
--
ALTER TABLE `AssemblyHasMacromolecule`
  ADD PRIMARY KEY (`AssemblyHasMacromoleculeId`),
  ADD KEY `AssemblyHasMacromoleculeToAssembly` (`assemblyId`),
  ADD KEY `AssemblyHasMacromoleculeToAssemblyRegion` (`macromoleculeId`);

--
-- Indexes for table `AssemblyRegion`
--
ALTER TABLE `AssemblyRegion`
  ADD PRIMARY KEY (`assemblyRegionId`),
  ADD KEY `AssemblyRegionToAssemblyHasMacromolecule` (`assemblyHasMacromoleculeId`);

--
-- Indexes for table `AutoProc`
--
ALTER TABLE `AutoProc`
  ADD PRIMARY KEY (`autoProcId`),
  ADD KEY `AutoProc_FKIndex1` (`autoProcProgramId`);

--
-- Indexes for table `AutoProcIntegration`
--
ALTER TABLE `AutoProcIntegration`
  ADD PRIMARY KEY (`autoProcIntegrationId`),
  ADD KEY `AutoProcIntegrationIdx1` (`dataCollectionId`),
  ADD KEY `AutoProcIntegration_FKIndex1` (`autoProcProgramId`);

--
-- Indexes for table `AutoProcProgram`
--
ALTER TABLE `AutoProcProgram`
  ADD PRIMARY KEY (`autoProcProgramId`),
  ADD KEY `fk_AutoProcProgram_1_idx` (`dataCollectionId`);

--
-- Indexes for table `AutoProcProgramAttachment`
--
ALTER TABLE `AutoProcProgramAttachment`
  ADD PRIMARY KEY (`autoProcProgramAttachmentId`),
  ADD KEY `AutoProcProgramAttachmentIdx1` (`autoProcProgramId`);

--
-- Indexes for table `AutoProcScaling`
--
ALTER TABLE `AutoProcScaling`
  ADD PRIMARY KEY (`autoProcScalingId`),
  ADD KEY `AutoProcScalingFk1` (`autoProcId`),
  ADD KEY `AutoProcScalingIdx1` (`autoProcScalingId`,`autoProcId`);

--
-- Indexes for table `AutoProcScalingStatistics`
--
ALTER TABLE `AutoProcScalingStatistics`
  ADD PRIMARY KEY (`autoProcScalingStatisticsId`),
  ADD KEY `AutoProcScalingStatisticsIdx1` (`autoProcScalingId`),
  ADD KEY `AutoProcScalingStatistics_FKindexType` (`scalingStatisticsType`);

--
-- Indexes for table `AutoProcScaling_has_Int`
--
ALTER TABLE `AutoProcScaling_has_Int`
  ADD PRIMARY KEY (`autoProcScaling_has_IntId`),
  ADD KEY `AutoProcScl_has_IntIdx1` (`autoProcScalingId`),
  ADD KEY `AutoProcScal_has_IntIdx2` (`autoProcIntegrationId`),
  ADD KEY `AutoProcScalingHasInt_FKIndex3` (`autoProcScalingId`,`autoProcIntegrationId`);

--
-- Indexes for table `AutoProcStatus`
--
ALTER TABLE `AutoProcStatus`
  ADD PRIMARY KEY (`autoProcStatusId`),
  ADD KEY `AutoProcStatus_FKIndex1` (`autoProcIntegrationId`);

--
-- Indexes for table `BeamApertures`
--
ALTER TABLE `BeamApertures`
  ADD PRIMARY KEY (`beamAperturesid`),
  ADD KEY `beamapertures_FK1` (`beamlineStatsId`);

--
-- Indexes for table `BeamCalendar`
--
ALTER TABLE `BeamCalendar`
  ADD PRIMARY KEY (`beamCalendarId`);

--
-- Indexes for table `BeamCentres`
--
ALTER TABLE `BeamCentres`
  ADD PRIMARY KEY (`beamCentresid`),
  ADD KEY `beamCentres_FK1` (`beamlineStatsId`);

--
-- Indexes for table `BeamlineAction`
--
ALTER TABLE `BeamlineAction`
  ADD PRIMARY KEY (`beamlineActionId`),
  ADD KEY `BeamlineAction_ibfk1` (`sessionId`);

--
-- Indexes for table `BeamLineSetup`
--
ALTER TABLE `BeamLineSetup`
  ADD PRIMARY KEY (`beamLineSetupId`);

--
-- Indexes for table `BeamlineStats`
--
ALTER TABLE `BeamlineStats`
  ADD PRIMARY KEY (`beamlineStatsId`);

--
-- Indexes for table `BF_automationError`
--
ALTER TABLE `BF_automationError`
  ADD PRIMARY KEY (`automationErrorId`);

--
-- Indexes for table `BF_automationFault`
--
ALTER TABLE `BF_automationFault`
  ADD PRIMARY KEY (`automationFaultId`),
  ADD KEY `BF_automationFault_ibfk1` (`automationErrorId`),
  ADD KEY `BF_automationFault_ibfk2` (`containerId`);

--
-- Indexes for table `BF_component`
--
ALTER TABLE `BF_component`
  ADD PRIMARY KEY (`componentId`),
  ADD KEY `bf_component_FK1` (`systemId`);

--
-- Indexes for table `BF_component_beamline`
--
ALTER TABLE `BF_component_beamline`
  ADD PRIMARY KEY (`component_beamlineId`),
  ADD KEY `bf_component_beamline_FK1` (`componentId`);

--
-- Indexes for table `BF_fault`
--
ALTER TABLE `BF_fault`
  ADD PRIMARY KEY (`faultId`),
  ADD KEY `bf_fault_FK1` (`sessionId`),
  ADD KEY `bf_fault_FK2` (`subcomponentId`),
  ADD KEY `bf_fault_FK3` (`personId`),
  ADD KEY `bf_fault_FK4` (`assigneeId`);

--
-- Indexes for table `BF_subcomponent`
--
ALTER TABLE `BF_subcomponent`
  ADD PRIMARY KEY (`subcomponentId`),
  ADD KEY `bf_subcomponent_FK1` (`componentId`);

--
-- Indexes for table `BF_subcomponent_beamline`
--
ALTER TABLE `BF_subcomponent_beamline`
  ADD PRIMARY KEY (`subcomponent_beamlineId`),
  ADD KEY `bf_subcomponent_beamline_FK1` (`subcomponentId`);

--
-- Indexes for table `BF_system`
--
ALTER TABLE `BF_system`
  ADD PRIMARY KEY (`systemId`);

--
-- Indexes for table `BF_system_beamline`
--
ALTER TABLE `BF_system_beamline`
  ADD PRIMARY KEY (`system_beamlineId`),
  ADD KEY `bf_system_beamline_FK1` (`systemId`);

--
-- Indexes for table `BLSample`
--
ALTER TABLE `BLSample`
  ADD PRIMARY KEY (`blSampleId`),
  ADD KEY `BLSample_FKIndex1` (`containerId`),
  ADD KEY `BLSample_FKIndex2` (`crystalId`),
  ADD KEY `BLSample_FKIndex3` (`diffractionPlanId`),
  ADD KEY `crystalId` (`crystalId`,`containerId`),
  ADD KEY `BLSample_Index1` (`name`) USING BTREE,
  ADD KEY `BLSample_FKIndex_Status` (`blSampleStatus`),
  ADD KEY `BLSampleImage_idx1` (`blSubSampleId`),
  ADD KEY `BLSample_fk5` (`screenComponentGroupId`);

--
-- Indexes for table `BLSampleGroup`
--
ALTER TABLE `BLSampleGroup`
  ADD PRIMARY KEY (`blSampleGroupId`);

--
-- Indexes for table `BLSampleGroup_has_BLSample`
--
ALTER TABLE `BLSampleGroup_has_BLSample`
  ADD PRIMARY KEY (`blSampleGroupId`,`blSampleId`),
  ADD KEY `BLSampleGroup_has_BLSample_ibfk2` (`blSampleId`);

--
-- Indexes for table `BLSampleImage`
--
ALTER TABLE `BLSampleImage`
  ADD PRIMARY KEY (`blSampleImageId`),
  ADD KEY `BLSampleImage_fk2` (`containerInspectionId`),
  ADD KEY `BLSampleImage_idx1` (`blSampleId`);

--
-- Indexes for table `BLSampleImageAnalysis`
--
ALTER TABLE `BLSampleImageAnalysis`
  ADD PRIMARY KEY (`blSampleImageAnalysisId`),
  ADD KEY `BLSampleImageAnalysis_ibfk1` (`blSampleImageId`);

--
-- Indexes for table `BLSampleImageScore`
--
ALTER TABLE `BLSampleImageScore`
  ADD PRIMARY KEY (`blSampleImageScoreId`);

--
-- Indexes for table `BLSampleType_has_Component`
--
ALTER TABLE `BLSampleType_has_Component`
  ADD PRIMARY KEY (`blSampleTypeId`,`componentId`),
  ADD KEY `blSampleType_has_Component_fk2` (`componentId`);

--
-- Indexes for table `BLSample_has_DiffractionPlan`
--
ALTER TABLE `BLSample_has_DiffractionPlan`
  ADD PRIMARY KEY (`blSampleId`,`diffractionPlanId`),
  ADD KEY `BLSample_has_DiffractionPlan_ibfk2` (`diffractionPlanId`);

--
-- Indexes for table `BLSample_has_EnergyScan`
--
ALTER TABLE `BLSample_has_EnergyScan`
  ADD PRIMARY KEY (`blSampleHasEnergyScanId`),
  ADD KEY `BLSample_has_EnergyScan_FKIndex1` (`blSampleId`),
  ADD KEY `BLSample_has_EnergyScan_FKIndex2` (`energyScanId`);

--
-- Indexes for table `BLSession`
--
ALTER TABLE `BLSession`
  ADD PRIMARY KEY (`sessionId`),
  ADD KEY `Session_FKIndex1` (`proposalId`),
  ADD KEY `Session_FKIndex2` (`beamLineSetupId`),
  ADD KEY `Session_FKIndexStartDate` (`startDate`),
  ADD KEY `Session_FKIndexEndDate` (`endDate`),
  ADD KEY `Session_FKIndexBeamLineName` (`beamLineName`),
  ADD KEY `BLSession_FKIndexOperatorSiteNumber` (`operatorSiteNumber`),
  ADD KEY `BLSession_ibfk_3` (`beamCalendarId`);

--
-- Indexes for table `BLSession_has_SCPosition`
--
ALTER TABLE `BLSession_has_SCPosition`
  ADD PRIMARY KEY (`blsessionhasscpositionid`),
  ADD KEY `blsession_has_scposition_FK1` (`blsessionid`);

--
-- Indexes for table `BLSubSample`
--
ALTER TABLE `BLSubSample`
  ADD PRIMARY KEY (`blSubSampleId`),
  ADD KEY `BLSubSample_FKIndex1` (`blSampleId`),
  ADD KEY `BLSubSample_FKIndex2` (`diffractionPlanId`),
  ADD KEY `BLSubSample_FKIndex3` (`positionId`),
  ADD KEY `BLSubSample_FKIndex5` (`position2Id`),
  ADD KEY `BLSubSample_motorPositionfk_1` (`motorPositionId`);

--
-- Indexes for table `Buffer`
--
ALTER TABLE `Buffer`
  ADD PRIMARY KEY (`bufferId`),
  ADD KEY `BufferToSafetyLevel` (`safetyLevelId`);

--
-- Indexes for table `BufferHasAdditive`
--
ALTER TABLE `BufferHasAdditive`
  ADD PRIMARY KEY (`bufferHasAdditiveId`),
  ADD KEY `BufferHasAdditiveToBuffer` (`bufferId`),
  ADD KEY `BufferHasAdditiveToAdditive` (`additiveId`),
  ADD KEY `BufferHasAdditiveToUnit` (`measurementUnitId`);

--
-- Indexes for table `CalendarHash`
--
ALTER TABLE `CalendarHash`
  ADD PRIMARY KEY (`calendarHashId`);

--
-- Indexes for table `ComponentSubType`
--
ALTER TABLE `ComponentSubType`
  ADD PRIMARY KEY (`componentSubTypeId`);

--
-- Indexes for table `ComponentType`
--
ALTER TABLE `ComponentType`
  ADD PRIMARY KEY (`componentTypeId`);

--
-- Indexes for table `Component_has_SubType`
--
ALTER TABLE `Component_has_SubType`
  ADD PRIMARY KEY (`componentId`,`componentSubTypeId`),
  ADD KEY `component_has_SubType_fk2` (`componentSubTypeId`);

--
-- Indexes for table `ConcentrationType`
--
ALTER TABLE `ConcentrationType`
  ADD PRIMARY KEY (`concentrationTypeId`);

--
-- Indexes for table `Container`
--
ALTER TABLE `Container`
  ADD PRIMARY KEY (`containerId`),
  ADD UNIQUE KEY `Container_UNIndex1` (`barcode`),
  ADD KEY `Container_FKIndex1` (`dewarId`),
  ADD KEY `Container_FKIndex` (`beamlineLocation`),
  ADD KEY `Container_FKIndexStatus` (`containerStatus`),
  ADD KEY `Container_ibfk6` (`sessionId`),
  ADD KEY `Container_ibfk5` (`ownerId`);

--
-- Indexes for table `ContainerHistory`
--
ALTER TABLE `ContainerHistory`
  ADD PRIMARY KEY (`containerHistoryId`),
  ADD KEY `ContainerHistory_ibfk1` (`containerId`);

--
-- Indexes for table `ContainerInspection`
--
ALTER TABLE `ContainerInspection`
  ADD PRIMARY KEY (`containerInspectionId`),
  ADD KEY `ContainerInspection_fk4` (`scheduleComponentid`),
  ADD KEY `ContainerInspection_idx1` (`containerId`),
  ADD KEY `ContainerInspection_idx2` (`inspectionTypeId`),
  ADD KEY `ContainerInspection_idx3` (`imagerId`);

--
-- Indexes for table `ContainerQueue`
--
ALTER TABLE `ContainerQueue`
  ADD PRIMARY KEY (`containerQueueId`),
  ADD KEY `ContainerQueue_ibfk1` (`containerId`),
  ADD KEY `ContainerQueue_ibfk2` (`personId`);

--
-- Indexes for table `ContainerQueueSample`
--
ALTER TABLE `ContainerQueueSample`
  ADD PRIMARY KEY (`containerQueueSampleId`),
  ADD KEY `ContainerQueueSample_ibfk1` (`containerQueueId`),
  ADD KEY `ContainerQueueSample_ibfk2` (`blSubSampleId`);

--
-- Indexes for table `Crystal`
--
ALTER TABLE `Crystal`
  ADD PRIMARY KEY (`crystalId`),
  ADD KEY `Crystal_FKIndex1` (`proteinId`),
  ADD KEY `Crystal_FKIndex2` (`diffractionPlanId`);

--
-- Indexes for table `Crystal_has_UUID`
--
ALTER TABLE `Crystal_has_UUID`
  ADD PRIMARY KEY (`crystal_has_UUID_Id`),
  ADD KEY `Crystal_has_UUID_FKIndex1` (`crystalId`),
  ADD KEY `Crystal_has_UUID_FKIndex2` (`UUID`);

--
-- Indexes for table `CTF`
--
ALTER TABLE `CTF`
  ADD PRIMARY KEY (`CTFid`),
  ADD KEY `fk_CTF_1_idx` (`motionCorrectionId`);

--
-- Indexes for table `DataAcquisition`
--
ALTER TABLE `DataAcquisition`
  ADD PRIMARY KEY (`dataAcquisitionId`);

--
-- Indexes for table `DataCollection`
--
ALTER TABLE `DataCollection`
  ADD PRIMARY KEY (`dataCollectionId`),
  ADD KEY `DataCollection_FKIndex1` (`dataCollectionGroupId`),
  ADD KEY `DataCollection_FKIndex2` (`strategySubWedgeOrigId`),
  ADD KEY `DataCollection_FKIndex3` (`detectorId`),
  ADD KEY `DataCollection_FKIndexStartTime` (`startTime`),
  ADD KEY `DataCollection_FKIndexImageDirectory` (`imageDirectory`),
  ADD KEY `DataCollection_FKIndexDCNumber` (`dataCollectionNumber`),
  ADD KEY `DataCollection_FKIndexImagePrefix` (`imagePrefix`),
  ADD KEY `startPositionId` (`startPositionId`),
  ADD KEY `endPositionId` (`endPositionId`),
  ADD KEY `blSubSampleId` (`blSubSampleId`);

--
-- Indexes for table `DataCollectionFileAttachment`
--
ALTER TABLE `DataCollectionFileAttachment`
  ADD PRIMARY KEY (`dataCollectionFileAttachmentId`),
  ADD KEY `dataCollectionFileAttachmentId_fk1` (`dataCollectionId`);

--
-- Indexes for table `DataCollectionGroup`
--
ALTER TABLE `DataCollectionGroup`
  ADD PRIMARY KEY (`dataCollectionGroupId`),
  ADD KEY `DataCollectionGroup_FKIndex1` (`blSampleId`),
  ADD KEY `DataCollectionGroup_FKIndex2` (`sessionId`),
  ADD KEY `workflowId` (`workflowId`);

--
-- Indexes for table `DataCollectionPlanGroup`
--
ALTER TABLE `DataCollectionPlanGroup`
  ADD PRIMARY KEY (`dataCollectionPlanGroupId`),
  ADD KEY `DataCollectionPlanGroup_ibfk1` (`sessionId`),
  ADD KEY `DataCollectionPlanGroup_ibfk2` (`blSampleId`);

--
-- Indexes for table `DatamatrixInSampleChanger`
--
ALTER TABLE `DatamatrixInSampleChanger`
  ADD PRIMARY KEY (`datamatrixInSampleChangerId`),
  ADD KEY `DatamatrixInSampleChanger_FKIndex1` (`proposalId`);

--
-- Indexes for table `DataReductionStatus`
--
ALTER TABLE `DataReductionStatus`
  ADD PRIMARY KEY (`dataReductionStatusId`);

--
-- Indexes for table `Detector`
--
ALTER TABLE `Detector`
  ADD PRIMARY KEY (`detectorId`),
  ADD UNIQUE KEY `Detector_ibuk1` (`detectorSerialNumber`),
  ADD KEY `Detector_FKIndex1` (`detectorType`,`detectorManufacturer`,`detectorModel`,`detectorPixelSizeHorizontal`,`detectorPixelSizeVertical`);

--
-- Indexes for table `Dewar`
--
ALTER TABLE `Dewar`
  ADD PRIMARY KEY (`dewarId`),
  ADD UNIQUE KEY `barCode` (`barCode`),
  ADD KEY `Dewar_FKIndex1` (`shippingId`),
  ADD KEY `Dewar_FKIndex2` (`firstExperimentId`),
  ADD KEY `Dewar_FKIndexStatus` (`dewarStatus`),
  ADD KEY `Dewar_FKIndexCode` (`code`);

--
-- Indexes for table `DewarLocation`
--
ALTER TABLE `DewarLocation`
  ADD PRIMARY KEY (`eventId`);

--
-- Indexes for table `DewarLocationList`
--
ALTER TABLE `DewarLocationList`
  ADD PRIMARY KEY (`locationId`);

--
-- Indexes for table `DewarTransportHistory`
--
ALTER TABLE `DewarTransportHistory`
  ADD PRIMARY KEY (`DewarTransportHistoryId`),
  ADD KEY `DewarTransportHistory_FKIndex1` (`dewarId`);

--
-- Indexes for table `DiffractionPlan`
--
ALTER TABLE `DiffractionPlan`
  ADD PRIMARY KEY (`diffractionPlanId`);

--
-- Indexes for table `DiffractionPlan_has_Detector`
--
ALTER TABLE `DiffractionPlan_has_Detector`
  ADD PRIMARY KEY (`diffractionPlanId`,`detectorId`),
  ADD KEY `DiffractionPlan_has_Detector_ibfk2` (`detectorId`);

--
-- Indexes for table `EMMicroscope`
--
ALTER TABLE `EMMicroscope`
  ADD PRIMARY KEY (`emMicroscopeId`);

--
-- Indexes for table `EnergyScan`
--
ALTER TABLE `EnergyScan`
  ADD PRIMARY KEY (`energyScanId`),
  ADD KEY `EnergyScan_FKIndex2` (`sessionId`),
  ADD KEY `ES_ibfk_2` (`blSampleId`),
  ADD KEY `ES_ibfk_3` (`blSubSampleId`);

--
-- Indexes for table `Experiment`
--
ALTER TABLE `Experiment`
  ADD PRIMARY KEY (`experimentId`),
  ADD KEY `fk_Experiment_To_session_idx` (`sessionId`);

--
-- Indexes for table `ExperimentKindDetails`
--
ALTER TABLE `ExperimentKindDetails`
  ADD PRIMARY KEY (`experimentKindId`),
  ADD KEY `ExperimentKindDetails_FKIndex1` (`diffractionPlanId`);

--
-- Indexes for table `FitStructureToExperimentalData`
--
ALTER TABLE `FitStructureToExperimentalData`
  ADD PRIMARY KEY (`fitStructureToExperimentalDataId`),
  ADD KEY `fk_FitStructureToExperimentalData_1` (`structureId`),
  ADD KEY `fk_FitStructureToExperimentalData_2` (`workflowId`),
  ADD KEY `fk_FitStructureToExperimentalData_3` (`subtractionId`);

--
-- Indexes for table `Frame`
--
ALTER TABLE `Frame`
  ADD PRIMARY KEY (`frameId`),
  ADD KEY `FILE` (`filePath`);

--
-- Indexes for table `FrameList`
--
ALTER TABLE `FrameList`
  ADD PRIMARY KEY (`frameListId`);

--
-- Indexes for table `FrameSet`
--
ALTER TABLE `FrameSet`
  ADD PRIMARY KEY (`frameSetId`),
  ADD KEY `FramesetToRun` (`runId`),
  ADD KEY `FrameSetToFrameList` (`frameListId`);

--
-- Indexes for table `FrameToList`
--
ALTER TABLE `FrameToList`
  ADD PRIMARY KEY (`frameToListId`),
  ADD KEY `FrameToLisToFrameList` (`frameListId`),
  ADD KEY `FrameToListToFrame` (`frameId`);

--
-- Indexes for table `GeometryClassname`
--
ALTER TABLE `GeometryClassname`
  ADD PRIMARY KEY (`geometryClassnameId`);

--
-- Indexes for table `GridInfo`
--
ALTER TABLE `GridInfo`
  ADD PRIMARY KEY (`gridInfoId`),
  ADD KEY `workflowMeshId` (`workflowMeshId`),
  ADD KEY `GridInfo_ibfk_2` (`dataCollectionGroupId`);

--
-- Indexes for table `Image`
--
ALTER TABLE `Image`
  ADD PRIMARY KEY (`imageId`),
  ADD KEY `Image_FKIndex1` (`dataCollectionId`),
  ADD KEY `Image_FKIndex2` (`imageNumber`),
  ADD KEY `Image_Index3` (`fileLocation`,`fileName`) USING BTREE,
  ADD KEY `motorPositionId` (`motorPositionId`);

--
-- Indexes for table `ImageQualityIndicators`
--
ALTER TABLE `ImageQualityIndicators`
  ADD PRIMARY KEY (`imageQualityIndicatorsId`),
  ADD KEY `ImageQualityIndicatorsIdx1` (`imageId`),
  ADD KEY `AutoProcProgramIdx1` (`autoProcProgramId`);

--
-- Indexes for table `Imager`
--
ALTER TABLE `Imager`
  ADD PRIMARY KEY (`imagerId`);

--
-- Indexes for table `InputParameterWorkflow`
--
ALTER TABLE `InputParameterWorkflow`
  ADD PRIMARY KEY (`inputParameterId`);

--
-- Indexes for table `InspectionType`
--
ALTER TABLE `InspectionType`
  ADD PRIMARY KEY (`inspectionTypeId`);

--
-- Indexes for table `Instruction`
--
ALTER TABLE `Instruction`
  ADD PRIMARY KEY (`instructionId`),
  ADD KEY `InstructionToInstructionSet` (`instructionSetId`);

--
-- Indexes for table `InstructionSet`
--
ALTER TABLE `InstructionSet`
  ADD PRIMARY KEY (`instructionSetId`);

--
-- Indexes for table `IspybAutoProcAttachment`
--
ALTER TABLE `IspybAutoProcAttachment`
  ADD PRIMARY KEY (`autoProcAttachmentId`);

--
-- Indexes for table `IspybCrystalClass`
--
ALTER TABLE `IspybCrystalClass`
  ADD PRIMARY KEY (`crystalClassId`);

--
-- Indexes for table `IspybReference`
--
ALTER TABLE `IspybReference`
  ADD PRIMARY KEY (`referenceId`);

--
-- Indexes for table `LabContact`
--
ALTER TABLE `LabContact`
  ADD PRIMARY KEY (`labContactId`),
  ADD UNIQUE KEY `personAndProposal` (`personId`,`proposalId`),
  ADD UNIQUE KEY `cardNameAndProposal` (`cardName`,`proposalId`),
  ADD KEY `LabContact_FKIndex1` (`proposalId`);

--
-- Indexes for table `Laboratory`
--
ALTER TABLE `Laboratory`
  ADD PRIMARY KEY (`laboratoryId`);

--
-- Indexes for table `Log4Stat`
--
ALTER TABLE `Log4Stat`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `Login`
--
ALTER TABLE `Login`
  ADD PRIMARY KEY (`loginId`),
  ADD KEY `Token` (`token`);

--
-- Indexes for table `Macromolecule`
--
ALTER TABLE `Macromolecule`
  ADD PRIMARY KEY (`macromoleculeId`),
  ADD KEY `MacromoleculeToSafetyLevel` (`safetyLevelId`);

--
-- Indexes for table `MacromoleculeRegion`
--
ALTER TABLE `MacromoleculeRegion`
  ADD PRIMARY KEY (`macromoleculeRegionId`),
  ADD KEY `MacromoleculeRegionInformationToMacromolecule` (`macromoleculeId`);

--
-- Indexes for table `Measurement`
--
ALTER TABLE `Measurement`
  ADD PRIMARY KEY (`measurementId`),
  ADD KEY `SpecimenToSamplePlateWell` (`specimenId`),
  ADD KEY `MeasurementToRun` (`runId`);

--
-- Indexes for table `MeasurementToDataCollection`
--
ALTER TABLE `MeasurementToDataCollection`
  ADD PRIMARY KEY (`measurementToDataCollectionId`),
  ADD KEY `MeasurementToDataCollectionToDataCollection` (`dataCollectionId`),
  ADD KEY `MeasurementToDataCollectionToMeasurement` (`measurementId`);

--
-- Indexes for table `MeasurementUnit`
--
ALTER TABLE `MeasurementUnit`
  ADD PRIMARY KEY (`measurementUnitId`);

--
-- Indexes for table `Merge`
--
ALTER TABLE `Merge`
  ADD PRIMARY KEY (`mergeId`),
  ADD KEY `MergeToMeasurement` (`measurementId`),
  ADD KEY `MergeToListOfFrames` (`frameListId`);

--
-- Indexes for table `MixtureToStructure`
--
ALTER TABLE `MixtureToStructure`
  ADD PRIMARY KEY (`fitToStructureId`),
  ADD KEY `fk_FitToStructure_1` (`structureId`),
  ADD KEY `fk_FitToStructure_2` (`mixtureId`);

--
-- Indexes for table `Model`
--
ALTER TABLE `Model`
  ADD PRIMARY KEY (`modelId`);

--
-- Indexes for table `ModelBuilding`
--
ALTER TABLE `ModelBuilding`
  ADD PRIMARY KEY (`modelBuildingId`),
  ADD KEY `ModelBuilding_FKIndex1` (`phasingAnalysisId`),
  ADD KEY `ModelBuilding_FKIndex2` (`phasingProgramRunId`),
  ADD KEY `ModelBuilding_FKIndex3` (`spaceGroupId`);

--
-- Indexes for table `ModelList`
--
ALTER TABLE `ModelList`
  ADD PRIMARY KEY (`modelListId`);

--
-- Indexes for table `ModelToList`
--
ALTER TABLE `ModelToList`
  ADD PRIMARY KEY (`modelToListId`),
  ADD KEY `ModelToListToList` (`modelListId`),
  ADD KEY `ModelToListToModel` (`modelId`);

--
-- Indexes for table `MotionCorrection`
--
ALTER TABLE `MotionCorrection`
  ADD PRIMARY KEY (`motionCorrectionId`),
  ADD KEY `fk_MotionCorrection_1_idx` (`movieId`),
  ADD KEY `movieId` (`movieId`);

--
-- Indexes for table `MotorPosition`
--
ALTER TABLE `MotorPosition`
  ADD PRIMARY KEY (`motorPositionId`);

--
-- Indexes for table `Movie`
--
ALTER TABLE `Movie`
  ADD PRIMARY KEY (`movieId`),
  ADD KEY `dataCollectionToMovie_idx` (`dataCollectionId`);

--
-- Indexes for table `MXMRRun`
--
ALTER TABLE `MXMRRun`
  ADD PRIMARY KEY (`mxMRRunId`),
  ADD KEY `mxMRRun_FK1` (`autoProcScalingId`);

--
-- Indexes for table `MXMRRunBlob`
--
ALTER TABLE `MXMRRunBlob`
  ADD PRIMARY KEY (`mxMRRunBlobId`),
  ADD KEY `mxMRRunBlob_FK1` (`mxMRRunId`);

--
-- Indexes for table `Particle`
--
ALTER TABLE `Particle`
  ADD PRIMARY KEY (`particleId`),
  ADD KEY `Particle_FKIND1` (`dataCollectionId`);

--
-- Indexes for table `PDB`
--
ALTER TABLE `PDB`
  ADD PRIMARY KEY (`pdbId`);

--
-- Indexes for table `PDBEntry`
--
ALTER TABLE `PDBEntry`
  ADD PRIMARY KEY (`pdbEntryId`),
  ADD KEY `pdbEntryIdx1` (`autoProcProgramId`);

--
-- Indexes for table `PDBEntry_has_AutoProcProgram`
--
ALTER TABLE `PDBEntry_has_AutoProcProgram`
  ADD PRIMARY KEY (`pdbEntryHasAutoProcId`),
  ADD KEY `pdbEntry_AutoProcProgramIdx1` (`pdbEntryId`),
  ADD KEY `pdbEntry_AutoProcProgramIdx2` (`autoProcProgramId`);

--
-- Indexes for table `Permission`
--
ALTER TABLE `Permission`
  ADD PRIMARY KEY (`permissionId`);

--
-- Indexes for table `Person`
--
ALTER TABLE `Person`
  ADD PRIMARY KEY (`personId`),
  ADD KEY `Person_FKIndex1` (`laboratoryId`),
  ADD KEY `Person_FKIndexFamilyName` (`familyName`),
  ADD KEY `Person_FKIndex_Login` (`login`),
  ADD KEY `siteId` (`siteId`);

--
-- Indexes for table `Phasing`
--
ALTER TABLE `Phasing`
  ADD PRIMARY KEY (`phasingId`),
  ADD KEY `Phasing_FKIndex1` (`phasingAnalysisId`),
  ADD KEY `Phasing_FKIndex2` (`phasingProgramRunId`),
  ADD KEY `Phasing_FKIndex3` (`spaceGroupId`);

--
-- Indexes for table `PhasingAnalysis`
--
ALTER TABLE `PhasingAnalysis`
  ADD PRIMARY KEY (`phasingAnalysisId`);

--
-- Indexes for table `PhasingProgramAttachment`
--
ALTER TABLE `PhasingProgramAttachment`
  ADD PRIMARY KEY (`phasingProgramAttachmentId`),
  ADD KEY `PhasingProgramAttachment_FKIndex1` (`phasingProgramRunId`);

--
-- Indexes for table `PhasingProgramRun`
--
ALTER TABLE `PhasingProgramRun`
  ADD PRIMARY KEY (`phasingProgramRunId`);

--
-- Indexes for table `PhasingStatistics`
--
ALTER TABLE `PhasingStatistics`
  ADD PRIMARY KEY (`phasingStatisticsId`),
  ADD KEY `PhasingStatistics_FKIndex1` (`phasingHasScalingId1`),
  ADD KEY `PhasingStatistics_FKIndex2` (`phasingHasScalingId2`),
  ADD KEY `fk_PhasingStatistics_phasingStep_idx` (`phasingStepId`);

--
-- Indexes for table `PhasingStep`
--
ALTER TABLE `PhasingStep`
  ADD PRIMARY KEY (`phasingStepId`),
  ADD KEY `FK_programRun_id` (`programRunId`),
  ADD KEY `FK_spacegroup_id` (`spaceGroupId`),
  ADD KEY `FK_autoprocScaling_id` (`autoProcScalingId`),
  ADD KEY `FK_phasingAnalysis_id` (`phasingAnalysisId`);

--
-- Indexes for table `Phasing_has_Scaling`
--
ALTER TABLE `Phasing_has_Scaling`
  ADD PRIMARY KEY (`phasingHasScalingId`),
  ADD KEY `PhasingHasScaling_FKIndex1` (`phasingAnalysisId`),
  ADD KEY `PhasingHasScaling_FKIndex2` (`autoProcScalingId`);

--
-- Indexes for table `PHPSession`
--
ALTER TABLE `PHPSession`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `PlateGroup`
--
ALTER TABLE `PlateGroup`
  ADD PRIMARY KEY (`plateGroupId`);

--
-- Indexes for table `PlateType`
--
ALTER TABLE `PlateType`
  ADD PRIMARY KEY (`PlateTypeId`),
  ADD KEY `PlateTypeToExperiment` (`experimentId`);

--
-- Indexes for table `Position`
--
ALTER TABLE `Position`
  ADD PRIMARY KEY (`positionId`),
  ADD KEY `Position_FKIndex1` (`relativePositionId`);

--
-- Indexes for table `PreparePhasingData`
--
ALTER TABLE `PreparePhasingData`
  ADD PRIMARY KEY (`preparePhasingDataId`),
  ADD KEY `PreparePhasingData_FKIndex1` (`phasingAnalysisId`),
  ADD KEY `PreparePhasingData_FKIndex2` (`phasingProgramRunId`),
  ADD KEY `PreparePhasingData_FKIndex3` (`spaceGroupId`);

--
-- Indexes for table `Project`
--
ALTER TABLE `Project`
  ADD PRIMARY KEY (`projectId`),
  ADD KEY `Project_FK1` (`personId`);

--
-- Indexes for table `Project_has_BLSample`
--
ALTER TABLE `Project_has_BLSample`
  ADD PRIMARY KEY (`projectId`,`blSampleId`),
  ADD KEY `Project_has_BLSample_FK2` (`blSampleId`);

--
-- Indexes for table `Project_has_DCGroup`
--
ALTER TABLE `Project_has_DCGroup`
  ADD PRIMARY KEY (`projectId`,`dataCollectionGroupId`),
  ADD KEY `Project_has_DCGroup_FK2` (`dataCollectionGroupId`);

--
-- Indexes for table `Project_has_EnergyScan`
--
ALTER TABLE `Project_has_EnergyScan`
  ADD PRIMARY KEY (`projectId`,`energyScanId`),
  ADD KEY `project_has_energyscan_FK2` (`energyScanId`);

--
-- Indexes for table `Project_has_Person`
--
ALTER TABLE `Project_has_Person`
  ADD PRIMARY KEY (`projectId`,`personId`),
  ADD KEY `project_has_person_FK2` (`personId`);

--
-- Indexes for table `Project_has_Protein`
--
ALTER TABLE `Project_has_Protein`
  ADD PRIMARY KEY (`projectId`,`proteinId`),
  ADD KEY `project_has_protein_FK2` (`proteinId`);

--
-- Indexes for table `Project_has_Session`
--
ALTER TABLE `Project_has_Session`
  ADD PRIMARY KEY (`projectId`,`sessionId`),
  ADD KEY `project_has_session_FK2` (`sessionId`);

--
-- Indexes for table `Project_has_Shipping`
--
ALTER TABLE `Project_has_Shipping`
  ADD PRIMARY KEY (`projectId`,`shippingId`),
  ADD KEY `project_has_shipping_FK2` (`shippingId`);

--
-- Indexes for table `Project_has_User`
--
ALTER TABLE `Project_has_User`
  ADD PRIMARY KEY (`projecthasuserid`),
  ADD KEY `Project_Has_user_FK1` (`projectid`);

--
-- Indexes for table `Project_has_XFEFSpectrum`
--
ALTER TABLE `Project_has_XFEFSpectrum`
  ADD PRIMARY KEY (`projectId`,`xfeFluorescenceSpectrumId`),
  ADD KEY `project_has_xfefspectrum_FK2` (`xfeFluorescenceSpectrumId`);

--
-- Indexes for table `Proposal`
--
ALTER TABLE `Proposal`
  ADD PRIMARY KEY (`proposalId`),
  ADD KEY `Proposal_FKIndex1` (`personId`),
  ADD KEY `Proposal_FKIndexCodeNumber` (`proposalCode`,`proposalNumber`);

--
-- Indexes for table `ProposalHasPerson`
--
ALTER TABLE `ProposalHasPerson`
  ADD PRIMARY KEY (`proposalHasPersonId`),
  ADD KEY `fk_ProposalHasPerson_Proposal` (`proposalId`),
  ADD KEY `fk_ProposalHasPerson_Personal` (`personId`);

--
-- Indexes for table `Protein`
--
ALTER TABLE `Protein`
  ADD PRIMARY KEY (`proteinId`),
  ADD KEY `Protein_FKIndex1` (`proposalId`),
  ADD KEY `ProteinAcronym_Index` (`proposalId`,`acronym`),
  ADD KEY `Protein_FKIndex2` (`personId`),
  ADD KEY `Protein_Index2` (`acronym`) USING BTREE,
  ADD KEY `protein_fk3` (`componentTypeId`);

--
-- Indexes for table `Protein_has_Lattice`
--
ALTER TABLE `Protein_has_Lattice`
  ADD PRIMARY KEY (`proteinId`);

--
-- Indexes for table `Protein_has_PDB`
--
ALTER TABLE `Protein_has_PDB`
  ADD PRIMARY KEY (`proteinhaspdbid`),
  ADD KEY `Protein_Has_PDB_fk1` (`proteinid`),
  ADD KEY `Protein_Has_PDB_fk2` (`pdbid`);

--
-- Indexes for table `RigidBodyModeling`
--
ALTER TABLE `RigidBodyModeling`
  ADD PRIMARY KEY (`rigidBodyModelingId`),
  ADD KEY `fk_RigidBodyModeling_1` (`subtractionId`);

--
-- Indexes for table `RobotAction`
--
ALTER TABLE `RobotAction`
  ADD PRIMARY KEY (`robotActionId`),
  ADD KEY `RobotAction_FK1` (`blsessionId`),
  ADD KEY `RobotAction_FK2` (`blsampleId`);

--
-- Indexes for table `Run`
--
ALTER TABLE `Run`
  ADD PRIMARY KEY (`runId`);

--
-- Indexes for table `SafetyLevel`
--
ALTER TABLE `SafetyLevel`
  ADD PRIMARY KEY (`safetyLevelId`);

--
-- Indexes for table `SamplePlate`
--
ALTER TABLE `SamplePlate`
  ADD PRIMARY KEY (`samplePlateId`),
  ADD KEY `PlateToPtateGroup` (`plateGroupId`),
  ADD KEY `SamplePlateToType` (`plateTypeId`),
  ADD KEY `SamplePlateToExperiment` (`experimentId`),
  ADD KEY `SamplePlateToInstructionSet` (`instructionSetId`);

--
-- Indexes for table `SamplePlatePosition`
--
ALTER TABLE `SamplePlatePosition`
  ADD PRIMARY KEY (`samplePlatePositionId`),
  ADD KEY `PlatePositionToPlate` (`samplePlateId`);

--
-- Indexes for table `SaxsDataCollection`
--
ALTER TABLE `SaxsDataCollection`
  ADD PRIMARY KEY (`dataCollectionId`),
  ADD KEY `SaxsDataCollectionToExperiment` (`experimentId`);

--
-- Indexes for table `ScanParametersModel`
--
ALTER TABLE `ScanParametersModel`
  ADD PRIMARY KEY (`scanParametersModelId`),
  ADD KEY `PDF_Model_ibfk1` (`scanParametersServiceId`),
  ADD KEY `PDF_Model_ibfk2` (`dataCollectionPlanId`);

--
-- Indexes for table `ScanParametersService`
--
ALTER TABLE `ScanParametersService`
  ADD PRIMARY KEY (`scanParametersServiceId`);

--
-- Indexes for table `Schedule`
--
ALTER TABLE `Schedule`
  ADD PRIMARY KEY (`scheduleId`);

--
-- Indexes for table `ScheduleComponent`
--
ALTER TABLE `ScheduleComponent`
  ADD PRIMARY KEY (`scheduleComponentId`),
  ADD KEY `ScheduleComponent_fk2` (`inspectionTypeId`),
  ADD KEY `ScheduleComponent_idx1` (`scheduleId`);

--
-- Indexes for table `SchemaStatus`
--
ALTER TABLE `SchemaStatus`
  ADD PRIMARY KEY (`schemaStatusId`),
  ADD UNIQUE KEY `scriptName` (`scriptName`);

--
-- Indexes for table `Screen`
--
ALTER TABLE `Screen`
  ADD PRIMARY KEY (`screenId`),
  ADD KEY `Screen_fk1` (`proposalId`);

--
-- Indexes for table `ScreenComponent`
--
ALTER TABLE `ScreenComponent`
  ADD PRIMARY KEY (`screenComponentId`),
  ADD KEY `ScreenComponent_fk1` (`screenComponentGroupId`),
  ADD KEY `ScreenComponent_fk2` (`componentId`);

--
-- Indexes for table `ScreenComponentGroup`
--
ALTER TABLE `ScreenComponentGroup`
  ADD PRIMARY KEY (`screenComponentGroupId`),
  ADD KEY `ScreenComponentGroup_fk1` (`screenId`);

--
-- Indexes for table `Screening`
--
ALTER TABLE `Screening`
  ADD PRIMARY KEY (`screeningId`),
  ADD KEY `Screening_FKIndexDiffractionPlanId` (`diffractionPlanId`),
  ADD KEY `dcgroupId` (`dataCollectionGroupId`);

--
-- Indexes for table `ScreeningInput`
--
ALTER TABLE `ScreeningInput`
  ADD PRIMARY KEY (`screeningInputId`),
  ADD KEY `ScreeningInput_FKIndex1` (`screeningId`);

--
-- Indexes for table `ScreeningOutput`
--
ALTER TABLE `ScreeningOutput`
  ADD PRIMARY KEY (`screeningOutputId`),
  ADD KEY `ScreeningOutput_FKIndex1` (`screeningId`);

--
-- Indexes for table `ScreeningOutputLattice`
--
ALTER TABLE `ScreeningOutputLattice`
  ADD PRIMARY KEY (`screeningOutputLatticeId`),
  ADD KEY `ScreeningOutputLattice_FKIndex1` (`screeningOutputId`);

--
-- Indexes for table `ScreeningRank`
--
ALTER TABLE `ScreeningRank`
  ADD PRIMARY KEY (`screeningRankId`),
  ADD KEY `ScreeningRank_FKIndex1` (`screeningId`),
  ADD KEY `ScreeningRank_FKIndex2` (`screeningRankSetId`);

--
-- Indexes for table `ScreeningRankSet`
--
ALTER TABLE `ScreeningRankSet`
  ADD PRIMARY KEY (`screeningRankSetId`);

--
-- Indexes for table `ScreeningStrategy`
--
ALTER TABLE `ScreeningStrategy`
  ADD PRIMARY KEY (`screeningStrategyId`),
  ADD KEY `ScreeningStrategy_FKIndex1` (`screeningOutputId`);

--
-- Indexes for table `ScreeningStrategySubWedge`
--
ALTER TABLE `ScreeningStrategySubWedge`
  ADD PRIMARY KEY (`screeningStrategySubWedgeId`),
  ADD KEY `ScreeningStrategySubWedge_FK1` (`screeningStrategyWedgeId`);

--
-- Indexes for table `ScreeningStrategyWedge`
--
ALTER TABLE `ScreeningStrategyWedge`
  ADD PRIMARY KEY (`screeningStrategyWedgeId`),
  ADD KEY `ScreeningStrategyWedge_IBFK_1` (`screeningStrategyId`);

--
-- Indexes for table `SessionType`
--
ALTER TABLE `SessionType`
  ADD PRIMARY KEY (`sessionTypeId`),
  ADD KEY `SessionType_FKIndex1` (`sessionId`);

--
-- Indexes for table `Session_has_Person`
--
ALTER TABLE `Session_has_Person`
  ADD PRIMARY KEY (`sessionId`,`personId`),
  ADD KEY `Session_has_Person_FKIndex1` (`sessionId`),
  ADD KEY `Session_has_Person_FKIndex2` (`personId`);

--
-- Indexes for table `Shipping`
--
ALTER TABLE `Shipping`
  ADD PRIMARY KEY (`shippingId`),
  ADD KEY `Shipping_FKIndex1` (`proposalId`),
  ADD KEY `laboratoryId` (`laboratoryId`),
  ADD KEY `Shipping_FKIndex2` (`sendingLabContactId`),
  ADD KEY `Shipping_FKIndex3` (`returnLabContactId`),
  ADD KEY `Shipping_FKIndexCreationDate` (`creationDate`),
  ADD KEY `Shipping_FKIndexName` (`shippingName`),
  ADD KEY `Shipping_FKIndexStatus` (`shippingStatus`);

--
-- Indexes for table `ShippingHasSession`
--
ALTER TABLE `ShippingHasSession`
  ADD PRIMARY KEY (`shippingId`,`sessionId`),
  ADD KEY `ShippingHasSession_FKIndex1` (`shippingId`),
  ADD KEY `ShippingHasSession_FKIndex2` (`sessionId`);

--
-- Indexes for table `SpaceGroup`
--
ALTER TABLE `SpaceGroup`
  ADD PRIMARY KEY (`spaceGroupId`),
  ADD KEY `SpaceGroup_FKShortName` (`spaceGroupShortName`),
  ADD KEY `geometryClassnameId` (`geometryClassnameId`);

--
-- Indexes for table `Specimen`
--
ALTER TABLE `Specimen`
  ADD PRIMARY KEY (`specimenId`),
  ADD KEY `SamplePlateWellToBuffer` (`bufferId`),
  ADD KEY `SamplePlateWellToMacromolecule` (`macromoleculeId`),
  ADD KEY `SamplePlateWellToSamplePlatePosition` (`samplePlatePositionId`),
  ADD KEY `SamplePlateWellToSafetyLevel` (`safetyLevelId`),
  ADD KEY `SamplePlateWellToExperiment` (`experimentId`),
  ADD KEY `SampleToStockSolution` (`stockSolutionId`);

--
-- Indexes for table `StockSolution`
--
ALTER TABLE `StockSolution`
  ADD PRIMARY KEY (`stockSolutionId`),
  ADD KEY `StockSolutionToBuffer` (`bufferId`),
  ADD KEY `StockSolutionToMacromolecule` (`macromoleculeId`),
  ADD KEY `StockSolutionToInstructionSet` (`instructionSetId`);

--
-- Indexes for table `Stoichiometry`
--
ALTER TABLE `Stoichiometry`
  ADD PRIMARY KEY (`stoichiometryId`),
  ADD KEY `StoichiometryToHost` (`hostMacromoleculeId`),
  ADD KEY `StoichiometryToMacromolecule` (`macromoleculeId`);

--
-- Indexes for table `Structure`
--
ALTER TABLE `Structure`
  ADD PRIMARY KEY (`structureId`),
  ADD KEY `StructureToMacromolecule` (`macromoleculeId`),
  ADD KEY `StructureToCrystal_idx` (`crystalId`),
  ADD KEY `StructureToBlSample_idx` (`blSampleId`),
  ADD KEY `StructureToProposal_idx` (`proposalId`);

--
-- Indexes for table `SubstructureDetermination`
--
ALTER TABLE `SubstructureDetermination`
  ADD PRIMARY KEY (`substructureDeterminationId`),
  ADD KEY `SubstructureDetermination_FKIndex1` (`phasingAnalysisId`),
  ADD KEY `SubstructureDetermination_FKIndex2` (`phasingProgramRunId`),
  ADD KEY `SubstructureDetermination_FKIndex3` (`spaceGroupId`);

--
-- Indexes for table `Subtraction`
--
ALTER TABLE `Subtraction`
  ADD PRIMARY KEY (`subtractionId`),
  ADD KEY `EdnaAnalysisToMeasurement` (`dataCollectionId`),
  ADD KEY `fk_Subtraction_1` (`sampleOneDimensionalFiles`),
  ADD KEY `fk_Subtraction_2` (`bufferOnedimensionalFiles`);

--
-- Indexes for table `SubtractionToAbInitioModel`
--
ALTER TABLE `SubtractionToAbInitioModel`
  ADD PRIMARY KEY (`subtractionToAbInitioModelId`),
  ADD KEY `substractionToAbInitioModelToAbinitioModel` (`abInitioId`),
  ADD KEY `ubstractionToSubstraction` (`subtractionId`);

--
-- Indexes for table `Superposition`
--
ALTER TABLE `Superposition`
  ADD PRIMARY KEY (`superpositionId`),
  ADD KEY `fk_Superposition_1` (`subtractionId`);

--
-- Indexes for table `SW_onceToken`
--
ALTER TABLE `SW_onceToken`
  ADD PRIMARY KEY (`onceTokenId`),
  ADD KEY `SW_onceToken_fk1` (`personId`),
  ADD KEY `SW_onceToken_fk2` (`proposalId`);

--
-- Indexes for table `UntrustedRegion`
--
ALTER TABLE `UntrustedRegion`
  ADD PRIMARY KEY (`untrustedRegionId`),
  ADD KEY `UntrustedRegion_FKIndex1` (`detectorId`);

--
-- Indexes for table `UserGroup`
--
ALTER TABLE `UserGroup`
  ADD PRIMARY KEY (`userGroupId`),
  ADD UNIQUE KEY `UserGroup_idx1` (`name`);

--
-- Indexes for table `UserGroup_has_Permission`
--
ALTER TABLE `UserGroup_has_Permission`
  ADD PRIMARY KEY (`userGroupId`,`permissionId`),
  ADD KEY `UserGroup_has_Permission_fk2` (`permissionId`);

--
-- Indexes for table `UserGroup_has_Person`
--
ALTER TABLE `UserGroup_has_Person`
  ADD PRIMARY KEY (`userGroupId`,`personId`),
  ADD KEY `userGroup_has_Person_fk2` (`personId`);

--
-- Indexes for table `Workflow`
--
ALTER TABLE `Workflow`
  ADD PRIMARY KEY (`workflowId`);

--
-- Indexes for table `WorkflowDehydration`
--
ALTER TABLE `WorkflowDehydration`
  ADD PRIMARY KEY (`workflowDehydrationId`),
  ADD KEY `WorkflowDehydration_FKIndex1` (`workflowId`);

--
-- Indexes for table `WorkflowMesh`
--
ALTER TABLE `WorkflowMesh`
  ADD PRIMARY KEY (`workflowMeshId`),
  ADD KEY `WorkflowMesh_FKIndex1` (`workflowId`),
  ADD KEY `bestPositionId` (`bestPositionId`),
  ADD KEY `bestImageId` (`bestImageId`);

--
-- Indexes for table `WorkflowStep`
--
ALTER TABLE `WorkflowStep`
  ADD PRIMARY KEY (`workflowStepId`),
  ADD KEY `step_to_workflow_fk_idx` (`workflowId`);

--
-- Indexes for table `WorkflowType`
--
ALTER TABLE `WorkflowType`
  ADD PRIMARY KEY (`workflowTypeId`);

--
-- Indexes for table `XFEFluorescenceSpectrum`
--
ALTER TABLE `XFEFluorescenceSpectrum`
  ADD PRIMARY KEY (`xfeFluorescenceSpectrumId`),
  ADD KEY `XFEFluorescnceSpectrum_FKIndex1` (`blSampleId`),
  ADD KEY `XFEFluorescnceSpectrum_FKIndex2` (`sessionId`),
  ADD KEY `XFE_ibfk_3` (`blSubSampleId`);

--
-- Indexes for table `XRFFluorescenceMapping`
--
ALTER TABLE `XRFFluorescenceMapping`
  ADD PRIMARY KEY (`xrfFluorescenceMappingId`),
  ADD KEY `XRFFluorescenceMapping_ibfk1` (`xrfFluorescenceMappingROIId`),
  ADD KEY `XRFFluorescenceMapping_ibfk2` (`dataCollectionId`);

--
-- Indexes for table `XRFFluorescenceMappingROI`
--
ALTER TABLE `XRFFluorescenceMappingROI`
  ADD PRIMARY KEY (`xrfFluorescenceMappingROIId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `AbInitioModel`
--
ALTER TABLE `AbInitioModel`
  MODIFY `abInitioModelId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40650;
--
-- AUTO_INCREMENT for table `Additive`
--
ALTER TABLE `Additive`
  MODIFY `additiveId` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `AdminActivity`
--
ALTER TABLE `AdminActivity`
  MODIFY `adminActivityId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2154;
--
-- AUTO_INCREMENT for table `AdminVar`
--
ALTER TABLE `AdminVar`
  MODIFY `varId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT for table `Aperture`
--
ALTER TABLE `Aperture`
  MODIFY `apertureId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Assembly`
--
ALTER TABLE `Assembly`
  MODIFY `assemblyId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `AssemblyHasMacromolecule`
--
ALTER TABLE `AssemblyHasMacromolecule`
  MODIFY `AssemblyHasMacromoleculeId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT for table `AssemblyRegion`
--
ALTER TABLE `AssemblyRegion`
  MODIFY `assemblyRegionId` int(10) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `AutoProc`
--
ALTER TABLE `AutoProc`
  MODIFY `autoProcId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=1712452;
--
-- AUTO_INCREMENT for table `AutoProcIntegration`
--
ALTER TABLE `AutoProcIntegration`
  MODIFY `autoProcIntegrationId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=1909588;
--
-- AUTO_INCREMENT for table `AutoProcProgram`
--
ALTER TABLE `AutoProcProgram`
  MODIFY `autoProcProgramId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=1783661;
--
-- AUTO_INCREMENT for table `AutoProcProgramAttachment`
--
ALTER TABLE `AutoProcProgramAttachment`
  MODIFY `autoProcProgramAttachmentId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=17820261;
--
-- AUTO_INCREMENT for table `AutoProcScaling`
--
ALTER TABLE `AutoProcScaling`
  MODIFY `autoProcScalingId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=1712462;
--
-- AUTO_INCREMENT for table `AutoProcScalingStatistics`
--
ALTER TABLE `AutoProcScalingStatistics`
  MODIFY `autoProcScalingStatisticsId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=5212391;
--
-- AUTO_INCREMENT for table `AutoProcScaling_has_Int`
--
ALTER TABLE `AutoProcScaling_has_Int`
  MODIFY `autoProcScaling_has_IntId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=1712397;
--
-- AUTO_INCREMENT for table `AutoProcStatus`
--
ALTER TABLE `AutoProcStatus`
  MODIFY `autoProcStatusId` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=2526687;
--
-- AUTO_INCREMENT for table `BeamApertures`
--
ALTER TABLE `BeamApertures`
  MODIFY `beamAperturesid` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `BeamCalendar`
--
ALTER TABLE `BeamCalendar`
  MODIFY `beamCalendarId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `BeamCentres`
--
ALTER TABLE `BeamCentres`
  MODIFY `beamCentresid` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `BeamlineAction`
--
ALTER TABLE `BeamlineAction`
  MODIFY `beamlineActionId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `BeamLineSetup`
--
ALTER TABLE `BeamLineSetup`
  MODIFY `beamLineSetupId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1463669;
--
-- AUTO_INCREMENT for table `BeamlineStats`
--
ALTER TABLE `BeamlineStats`
  MODIFY `beamlineStatsId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `BF_automationError`
--
ALTER TABLE `BF_automationError`
  MODIFY `automationErrorId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `BF_automationFault`
--
ALTER TABLE `BF_automationFault`
  MODIFY `automationFaultId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `BF_component`
--
ALTER TABLE `BF_component`
  MODIFY `componentId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `BF_component_beamline`
--
ALTER TABLE `BF_component_beamline`
  MODIFY `component_beamlineId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `BF_fault`
--
ALTER TABLE `BF_fault`
  MODIFY `faultId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `BF_subcomponent`
--
ALTER TABLE `BF_subcomponent`
  MODIFY `subcomponentId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `BF_subcomponent_beamline`
--
ALTER TABLE `BF_subcomponent_beamline`
  MODIFY `subcomponent_beamlineId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `BF_system`
--
ALTER TABLE `BF_system`
  MODIFY `systemId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `BF_system_beamline`
--
ALTER TABLE `BF_system_beamline`
  MODIFY `system_beamlineId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `BLSample`
--
ALTER TABLE `BLSample`
  MODIFY `blSampleId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=631420;
--
-- AUTO_INCREMENT for table `BLSampleGroup`
--
ALTER TABLE `BLSampleGroup`
  MODIFY `blSampleGroupId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `BLSampleImage`
--
ALTER TABLE `BLSampleImage`
  MODIFY `blSampleImageId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `BLSampleImageAnalysis`
--
ALTER TABLE `BLSampleImageAnalysis`
  MODIFY `blSampleImageAnalysisId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `BLSampleImageScore`
--
ALTER TABLE `BLSampleImageScore`
  MODIFY `blSampleImageScoreId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `BLSample_has_EnergyScan`
--
ALTER TABLE `BLSample_has_EnergyScan`
  MODIFY `blSampleHasEnergyScanId` int(10) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `BLSession`
--
ALTER TABLE `BLSession`
  MODIFY `sessionId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=61501;
--
-- AUTO_INCREMENT for table `BLSession_has_SCPosition`
--
ALTER TABLE `BLSession_has_SCPosition`
  MODIFY `blsessionhasscpositionid` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `BLSubSample`
--
ALTER TABLE `BLSubSample`
  MODIFY `blSubSampleId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';
--
-- AUTO_INCREMENT for table `Buffer`
--
ALTER TABLE `Buffer`
  MODIFY `bufferId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7480;
--
-- AUTO_INCREMENT for table `BufferHasAdditive`
--
ALTER TABLE `BufferHasAdditive`
  MODIFY `bufferHasAdditiveId` int(10) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `CalendarHash`
--
ALTER TABLE `CalendarHash`
  MODIFY `calendarHashId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `ComponentType`
--
ALTER TABLE `ComponentType`
  MODIFY `componentTypeId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `ConcentrationType`
--
ALTER TABLE `ConcentrationType`
  MODIFY `concentrationTypeId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Container`
--
ALTER TABLE `Container`
  MODIFY `containerId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=345549;
--
-- AUTO_INCREMENT for table `ContainerHistory`
--
ALTER TABLE `ContainerHistory`
  MODIFY `containerHistoryId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `ContainerInspection`
--
ALTER TABLE `ContainerInspection`
  MODIFY `containerInspectionId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `ContainerQueue`
--
ALTER TABLE `ContainerQueue`
  MODIFY `containerQueueId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `ContainerQueueSample`
--
ALTER TABLE `ContainerQueueSample`
  MODIFY `containerQueueSampleId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Crystal`
--
ALTER TABLE `Crystal`
  MODIFY `crystalId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=556931;
--
-- AUTO_INCREMENT for table `Crystal_has_UUID`
--
ALTER TABLE `Crystal_has_UUID`
  MODIFY `crystal_has_UUID_Id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=93158;
--
-- AUTO_INCREMENT for table `CTF`
--
ALTER TABLE `CTF`
  MODIFY `CTFid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5371;
--
-- AUTO_INCREMENT for table `DataAcquisition`
--
ALTER TABLE `DataAcquisition`
  MODIFY `dataAcquisitionId` int(10) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `DataCollection`
--
ALTER TABLE `DataCollection`
  MODIFY `dataCollectionId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=2180649;
--
-- AUTO_INCREMENT for table `DataCollectionFileAttachment`
--
ALTER TABLE `DataCollectionFileAttachment`
  MODIFY `dataCollectionFileAttachmentId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `DataCollectionGroup`
--
ALTER TABLE `DataCollectionGroup`
  MODIFY `dataCollectionGroupId` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=1721658;
--
-- AUTO_INCREMENT for table `DataCollectionPlanGroup`
--
ALTER TABLE `DataCollectionPlanGroup`
  MODIFY `dataCollectionPlanGroupId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `DatamatrixInSampleChanger`
--
ALTER TABLE `DatamatrixInSampleChanger`
  MODIFY `datamatrixInSampleChangerId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19089089;
--
-- AUTO_INCREMENT for table `DataReductionStatus`
--
ALTER TABLE `DataReductionStatus`
  MODIFY `dataReductionStatusId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Detector`
--
ALTER TABLE `Detector`
  MODIFY `detectorId` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=81;
--
-- AUTO_INCREMENT for table `Dewar`
--
ALTER TABLE `Dewar`
  MODIFY `dewarId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=313852;
--
-- AUTO_INCREMENT for table `DewarLocation`
--
ALTER TABLE `DewarLocation`
  MODIFY `eventId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16070;
--
-- AUTO_INCREMENT for table `DewarLocationList`
--
ALTER TABLE `DewarLocationList`
  MODIFY `locationId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT for table `DewarTransportHistory`
--
ALTER TABLE `DewarTransportHistory`
  MODIFY `DewarTransportHistoryId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42031;
--
-- AUTO_INCREMENT for table `DiffractionPlan`
--
ALTER TABLE `DiffractionPlan`
  MODIFY `diffractionPlanId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=758327;
--
-- AUTO_INCREMENT for table `EMMicroscope`
--
ALTER TABLE `EMMicroscope`
  MODIFY `emMicroscopeId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `EnergyScan`
--
ALTER TABLE `EnergyScan`
  MODIFY `energyScanId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14716;
--
-- AUTO_INCREMENT for table `Experiment`
--
ALTER TABLE `Experiment`
  MODIFY `experimentId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18425;
--
-- AUTO_INCREMENT for table `ExperimentKindDetails`
--
ALTER TABLE `ExperimentKindDetails`
  MODIFY `experimentKindId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;
--
-- AUTO_INCREMENT for table `FitStructureToExperimentalData`
--
ALTER TABLE `FitStructureToExperimentalData`
  MODIFY `fitStructureToExperimentalDataId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `Frame`
--
ALTER TABLE `Frame`
  MODIFY `frameId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1836295;
--
-- AUTO_INCREMENT for table `FrameList`
--
ALTER TABLE `FrameList`
  MODIFY `frameListId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=226501;
--
-- AUTO_INCREMENT for table `FrameSet`
--
ALTER TABLE `FrameSet`
  MODIFY `frameSetId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4209;
--
-- AUTO_INCREMENT for table `FrameToList`
--
ALTER TABLE `FrameToList`
  MODIFY `frameToListId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2805367;
--
-- AUTO_INCREMENT for table `GeometryClassname`
--
ALTER TABLE `GeometryClassname`
  MODIFY `geometryClassnameId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;
--
-- AUTO_INCREMENT for table `GridInfo`
--
ALTER TABLE `GridInfo`
  MODIFY `gridInfoId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=163356;
--
-- AUTO_INCREMENT for table `Image`
--
ALTER TABLE `Image`
  MODIFY `imageId` int(12) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=53491893;
--
-- AUTO_INCREMENT for table `ImageQualityIndicators`
--
ALTER TABLE `ImageQualityIndicators`
  MODIFY `imageQualityIndicatorsId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=2854180;
--
-- AUTO_INCREMENT for table `Imager`
--
ALTER TABLE `Imager`
  MODIFY `imagerId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `InputParameterWorkflow`
--
ALTER TABLE `InputParameterWorkflow`
  MODIFY `inputParameterId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `InspectionType`
--
ALTER TABLE `InspectionType`
  MODIFY `inspectionTypeId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Instruction`
--
ALTER TABLE `Instruction`
  MODIFY `instructionId` int(10) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `InstructionSet`
--
ALTER TABLE `InstructionSet`
  MODIFY `instructionSetId` int(10) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `IspybAutoProcAttachment`
--
ALTER TABLE `IspybAutoProcAttachment`
  MODIFY `autoProcAttachmentId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=60;
--
-- AUTO_INCREMENT for table `IspybCrystalClass`
--
ALTER TABLE `IspybCrystalClass`
  MODIFY `crystalClassId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
--
-- AUTO_INCREMENT for table `IspybReference`
--
ALTER TABLE `IspybReference`
  MODIFY `referenceId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=15;
--
-- AUTO_INCREMENT for table `LabContact`
--
ALTER TABLE `LabContact`
  MODIFY `labContactId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=262205;
--
-- AUTO_INCREMENT for table `Laboratory`
--
ALTER TABLE `Laboratory`
  MODIFY `laboratoryId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=312772;
--
-- AUTO_INCREMENT for table `Log4Stat`
--
ALTER TABLE `Log4Stat`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19789;
--
-- AUTO_INCREMENT for table `Login`
--
ALTER TABLE `Login`
  MODIFY `loginId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29365;
--
-- AUTO_INCREMENT for table `Macromolecule`
--
ALTER TABLE `Macromolecule`
  MODIFY `macromoleculeId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=61336;
--
-- AUTO_INCREMENT for table `MacromoleculeRegion`
--
ALTER TABLE `MacromoleculeRegion`
  MODIFY `macromoleculeRegionId` int(10) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Measurement`
--
ALTER TABLE `Measurement`
  MODIFY `measurementId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=291103;
--
-- AUTO_INCREMENT for table `MeasurementToDataCollection`
--
ALTER TABLE `MeasurementToDataCollection`
  MODIFY `measurementToDataCollectionId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=291103;
--
-- AUTO_INCREMENT for table `Merge`
--
ALTER TABLE `Merge`
  MODIFY `mergeId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=151259;
--
-- AUTO_INCREMENT for table `MixtureToStructure`
--
ALTER TABLE `MixtureToStructure`
  MODIFY `fitToStructureId` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Model`
--
ALTER TABLE `Model`
  MODIFY `modelId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=653393;
--
-- AUTO_INCREMENT for table `ModelBuilding`
--
ALTER TABLE `ModelBuilding`
  MODIFY `modelBuildingId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';
--
-- AUTO_INCREMENT for table `ModelList`
--
ALTER TABLE `ModelList`
  MODIFY `modelListId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43748;
--
-- AUTO_INCREMENT for table `ModelToList`
--
ALTER TABLE `ModelToList`
  MODIFY `modelToListId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=530477;
--
-- AUTO_INCREMENT for table `MotionCorrection`
--
ALTER TABLE `MotionCorrection`
  MODIFY `motionCorrectionId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5413;
--
-- AUTO_INCREMENT for table `MotorPosition`
--
ALTER TABLE `MotorPosition`
  MODIFY `motorPositionId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=4098632;
--
-- AUTO_INCREMENT for table `Movie`
--
ALTER TABLE `Movie`
  MODIFY `movieId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5958;
--
-- AUTO_INCREMENT for table `MXMRRun`
--
ALTER TABLE `MXMRRun`
  MODIFY `mxMRRunId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `MXMRRunBlob`
--
ALTER TABLE `MXMRRunBlob`
  MODIFY `mxMRRunBlobId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `PDB`
--
ALTER TABLE `PDB`
  MODIFY `pdbId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `PDBEntry`
--
ALTER TABLE `PDBEntry`
  MODIFY `pdbEntryId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `PDBEntry_has_AutoProcProgram`
--
ALTER TABLE `PDBEntry_has_AutoProcProgram`
  MODIFY `pdbEntryHasAutoProcId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Permission`
--
ALTER TABLE `Permission`
  MODIFY `permissionId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Person`
--
ALTER TABLE `Person`
  MODIFY `personId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=400671;
--
-- AUTO_INCREMENT for table `Phasing`
--
ALTER TABLE `Phasing`
  MODIFY `phasingId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=106310;
--
-- AUTO_INCREMENT for table `PhasingAnalysis`
--
ALTER TABLE `PhasingAnalysis`
  MODIFY `phasingAnalysisId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=135302;
--
-- AUTO_INCREMENT for table `PhasingProgramAttachment`
--
ALTER TABLE `PhasingProgramAttachment`
  MODIFY `phasingProgramAttachmentId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=337995;
--
-- AUTO_INCREMENT for table `PhasingProgramRun`
--
ALTER TABLE `PhasingProgramRun`
  MODIFY `phasingProgramRunId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=456605;
--
-- AUTO_INCREMENT for table `PhasingStatistics`
--
ALTER TABLE `PhasingStatistics`
  MODIFY `phasingStatisticsId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=821258;
--
-- AUTO_INCREMENT for table `PhasingStep`
--
ALTER TABLE `PhasingStep`
  MODIFY `phasingStepId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=321234;
--
-- AUTO_INCREMENT for table `Phasing_has_Scaling`
--
ALTER TABLE `Phasing_has_Scaling`
  MODIFY `phasingHasScalingId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=135302;
--
-- AUTO_INCREMENT for table `PlateGroup`
--
ALTER TABLE `PlateGroup`
  MODIFY `plateGroupId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13801;
--
-- AUTO_INCREMENT for table `PlateType`
--
ALTER TABLE `PlateType`
  MODIFY `PlateTypeId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `Position`
--
ALTER TABLE `Position`
  MODIFY `positionId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `PreparePhasingData`
--
ALTER TABLE `PreparePhasingData`
  MODIFY `preparePhasingDataId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=14499;
--
-- AUTO_INCREMENT for table `Project`
--
ALTER TABLE `Project`
  MODIFY `projectId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Project_has_User`
--
ALTER TABLE `Project_has_User`
  MODIFY `projecthasuserid` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Proposal`
--
ALTER TABLE `Proposal`
  MODIFY `proposalId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8425;
--
-- AUTO_INCREMENT for table `ProposalHasPerson`
--
ALTER TABLE `ProposalHasPerson`
  MODIFY `proposalHasPersonId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4614;
--
-- AUTO_INCREMENT for table `Protein`
--
ALTER TABLE `Protein`
  MODIFY `proteinId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=376497;
--
-- AUTO_INCREMENT for table `Protein_has_PDB`
--
ALTER TABLE `Protein_has_PDB`
  MODIFY `proteinhaspdbid` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `RigidBodyModeling`
--
ALTER TABLE `RigidBodyModeling`
  MODIFY `rigidBodyModelingId` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `RobotAction`
--
ALTER TABLE `RobotAction`
  MODIFY `robotActionId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `Run`
--
ALTER TABLE `Run`
  MODIFY `runId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=134836;
--
-- AUTO_INCREMENT for table `SafetyLevel`
--
ALTER TABLE `SafetyLevel`
  MODIFY `safetyLevelId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `SamplePlate`
--
ALTER TABLE `SamplePlate`
  MODIFY `samplePlateId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41401;
--
-- AUTO_INCREMENT for table `SamplePlatePosition`
--
ALTER TABLE `SamplePlatePosition`
  MODIFY `samplePlatePositionId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=117475;
--
-- AUTO_INCREMENT for table `SaxsDataCollection`
--
ALTER TABLE `SaxsDataCollection`
  MODIFY `dataCollectionId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=97035;
--
-- AUTO_INCREMENT for table `ScanParametersModel`
--
ALTER TABLE `ScanParametersModel`
  MODIFY `scanParametersModelId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `ScanParametersService`
--
ALTER TABLE `ScanParametersService`
  MODIFY `scanParametersServiceId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Schedule`
--
ALTER TABLE `Schedule`
  MODIFY `scheduleId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `ScheduleComponent`
--
ALTER TABLE `ScheduleComponent`
  MODIFY `scheduleComponentId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `SchemaStatus`
--
ALTER TABLE `SchemaStatus`
  MODIFY `schemaStatusId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=101;
--
-- AUTO_INCREMENT for table `Screen`
--
ALTER TABLE `Screen`
  MODIFY `screenId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `ScreenComponent`
--
ALTER TABLE `ScreenComponent`
  MODIFY `screenComponentId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `ScreenComponentGroup`
--
ALTER TABLE `ScreenComponentGroup`
  MODIFY `screenComponentGroupId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Screening`
--
ALTER TABLE `Screening`
  MODIFY `screeningId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=476320;
--
-- AUTO_INCREMENT for table `ScreeningInput`
--
ALTER TABLE `ScreeningInput`
  MODIFY `screeningInputId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=97571;
--
-- AUTO_INCREMENT for table `ScreeningOutput`
--
ALTER TABLE `ScreeningOutput`
  MODIFY `screeningOutputId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=476260;
--
-- AUTO_INCREMENT for table `ScreeningOutputLattice`
--
ALTER TABLE `ScreeningOutputLattice`
  MODIFY `screeningOutputLatticeId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=324992;
--
-- AUTO_INCREMENT for table `ScreeningRank`
--
ALTER TABLE `ScreeningRank`
  MODIFY `screeningRankId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6239;
--
-- AUTO_INCREMENT for table `ScreeningRankSet`
--
ALTER TABLE `ScreeningRankSet`
  MODIFY `screeningRankSetId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=993;
--
-- AUTO_INCREMENT for table `ScreeningStrategy`
--
ALTER TABLE `ScreeningStrategy`
  MODIFY `screeningStrategyId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=251852;
--
-- AUTO_INCREMENT for table `ScreeningStrategySubWedge`
--
ALTER TABLE `ScreeningStrategySubWedge`
  MODIFY `screeningStrategySubWedgeId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key', AUTO_INCREMENT=267131;
--
-- AUTO_INCREMENT for table `ScreeningStrategyWedge`
--
ALTER TABLE `ScreeningStrategyWedge`
  MODIFY `screeningStrategyWedgeId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key', AUTO_INCREMENT=246849;
--
-- AUTO_INCREMENT for table `SessionType`
--
ALTER TABLE `SessionType`
  MODIFY `sessionTypeId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Shipping`
--
ALTER TABLE `Shipping`
  MODIFY `shippingId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=309231;
--
-- AUTO_INCREMENT for table `SpaceGroup`
--
ALTER TABLE `SpaceGroup`
  MODIFY `spaceGroupId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key', AUTO_INCREMENT=237;
--
-- AUTO_INCREMENT for table `Specimen`
--
ALTER TABLE `Specimen`
  MODIFY `specimenId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=120322;
--
-- AUTO_INCREMENT for table `StockSolution`
--
ALTER TABLE `StockSolution`
  MODIFY `stockSolutionId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=86;
--
-- AUTO_INCREMENT for table `Stoichiometry`
--
ALTER TABLE `Stoichiometry`
  MODIFY `stoichiometryId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;
--
-- AUTO_INCREMENT for table `Structure`
--
ALTER TABLE `Structure`
  MODIFY `structureId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=87;
--
-- AUTO_INCREMENT for table `SubstructureDetermination`
--
ALTER TABLE `SubstructureDetermination`
  MODIFY `substructureDeterminationId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=14495;
--
-- AUTO_INCREMENT for table `Subtraction`
--
ALTER TABLE `Subtraction`
  MODIFY `subtractionId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=62321;
--
-- AUTO_INCREMENT for table `SubtractionToAbInitioModel`
--
ALTER TABLE `SubtractionToAbInitioModel`
  MODIFY `subtractionToAbInitioModelId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40654;
--
-- AUTO_INCREMENT for table `Superposition`
--
ALTER TABLE `Superposition`
  MODIFY `superpositionId` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `SW_onceToken`
--
ALTER TABLE `SW_onceToken`
  MODIFY `onceTokenId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `UntrustedRegion`
--
ALTER TABLE `UntrustedRegion`
  MODIFY `untrustedRegionId` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';
--
-- AUTO_INCREMENT for table `UserGroup`
--
ALTER TABLE `UserGroup`
  MODIFY `userGroupId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Workflow`
--
ALTER TABLE `Workflow`
  MODIFY `workflowId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=153815;
--
-- AUTO_INCREMENT for table `WorkflowDehydration`
--
ALTER TABLE `WorkflowDehydration`
  MODIFY `workflowDehydrationId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)';
--
-- AUTO_INCREMENT for table `WorkflowMesh`
--
ALTER TABLE `WorkflowMesh`
  MODIFY `workflowMeshId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)', AUTO_INCREMENT=163374;
--
-- AUTO_INCREMENT for table `WorkflowStep`
--
ALTER TABLE `WorkflowStep`
  MODIFY `workflowStepId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=237934;
--
-- AUTO_INCREMENT for table `WorkflowType`
--
ALTER TABLE `WorkflowType`
  MODIFY `workflowTypeId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;
--
-- AUTO_INCREMENT for table `XFEFluorescenceSpectrum`
--
ALTER TABLE `XFEFluorescenceSpectrum`
  MODIFY `xfeFluorescenceSpectrumId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5385;
--
-- AUTO_INCREMENT for table `XRFFluorescenceMapping`
--
ALTER TABLE `XRFFluorescenceMapping`
  MODIFY `xrfFluorescenceMappingId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `XRFFluorescenceMappingROI`
--
ALTER TABLE `XRFFluorescenceMappingROI`
  MODIFY `xrfFluorescenceMappingROIId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;
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
-- Constraints for table `AutoProcProgram`
--
ALTER TABLE `AutoProcProgram`
  ADD CONSTRAINT `AutoProcProgram_FK1` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

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
-- Constraints for table `BeamApertures`
--
ALTER TABLE `BeamApertures`
  ADD CONSTRAINT `beamapertures_FK1` FOREIGN KEY (`beamlineStatsId`) REFERENCES `BeamlineStats` (`beamlineStatsId`) ON DELETE CASCADE;

--
-- Constraints for table `BeamCentres`
--
ALTER TABLE `BeamCentres`
  ADD CONSTRAINT `beamCentres_FK1` FOREIGN KEY (`beamlineStatsId`) REFERENCES `BeamlineStats` (`beamlineStatsId`) ON DELETE CASCADE;

--
-- Constraints for table `BeamlineAction`
--
ALTER TABLE `BeamlineAction`
  ADD CONSTRAINT `BeamlineAction_ibfk1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`);

--
-- Constraints for table `BF_automationFault`
--
ALTER TABLE `BF_automationFault`
  ADD CONSTRAINT `BF_automationFault_ibfk1` FOREIGN KEY (`automationErrorId`) REFERENCES `BF_automationError` (`automationErrorId`),
  ADD CONSTRAINT `BF_automationFault_ibfk2` FOREIGN KEY (`containerId`) REFERENCES `Container` (`containerId`);

--
-- Constraints for table `BF_component`
--
ALTER TABLE `BF_component`
  ADD CONSTRAINT `bf_component_FK1` FOREIGN KEY (`systemId`) REFERENCES `BF_system` (`systemId`);

--
-- Constraints for table `BF_component_beamline`
--
ALTER TABLE `BF_component_beamline`
  ADD CONSTRAINT `bf_component_beamline_FK1` FOREIGN KEY (`componentId`) REFERENCES `BF_component` (`componentId`);

--
-- Constraints for table `BF_fault`
--
ALTER TABLE `BF_fault`
  ADD CONSTRAINT `bf_fault_FK1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`),
  ADD CONSTRAINT `bf_fault_FK2` FOREIGN KEY (`subcomponentId`) REFERENCES `BF_subcomponent` (`subcomponentId`),
  ADD CONSTRAINT `bf_fault_FK3` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`),
  ADD CONSTRAINT `bf_fault_FK4` FOREIGN KEY (`assigneeId`) REFERENCES `Person` (`personId`);

--
-- Constraints for table `BF_subcomponent`
--
ALTER TABLE `BF_subcomponent`
  ADD CONSTRAINT `bf_subcomponent_FK1` FOREIGN KEY (`componentId`) REFERENCES `BF_component` (`componentId`);

--
-- Constraints for table `BF_subcomponent_beamline`
--
ALTER TABLE `BF_subcomponent_beamline`
  ADD CONSTRAINT `bf_subcomponent_beamline_FK1` FOREIGN KEY (`subcomponentId`) REFERENCES `BF_subcomponent` (`subcomponentId`);

--
-- Constraints for table `BF_system_beamline`
--
ALTER TABLE `BF_system_beamline`
  ADD CONSTRAINT `bf_system_beamline_FK1` FOREIGN KEY (`systemId`) REFERENCES `BF_system` (`systemId`);

--
-- Constraints for table `BLSample`
--
ALTER TABLE `BLSample`
  ADD CONSTRAINT `BLSample_ibfk_1` FOREIGN KEY (`containerId`) REFERENCES `Container` (`containerId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `BLSample_ibfk_2` FOREIGN KEY (`crystalId`) REFERENCES `Crystal` (`crystalId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `BLSample_ibfk_3` FOREIGN KEY (`diffractionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `BLSampleGroup_has_BLSample`
--
ALTER TABLE `BLSampleGroup_has_BLSample`
  ADD CONSTRAINT `BLSampleGroup_has_BLSample_ibfk1` FOREIGN KEY (`blSampleGroupId`) REFERENCES `BLSampleGroup` (`blSampleGroupId`),
  ADD CONSTRAINT `BLSampleGroup_has_BLSample_ibfk2` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`);

--
-- Constraints for table `BLSampleImage`
--
ALTER TABLE `BLSampleImage`
  ADD CONSTRAINT `BLSampleImage_fk1` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `BLSampleImage_fk2` FOREIGN KEY (`containerInspectionId`) REFERENCES `ContainerInspection` (`containerInspectionId`);

--
-- Constraints for table `BLSampleImageAnalysis`
--
ALTER TABLE `BLSampleImageAnalysis`
  ADD CONSTRAINT `BLSampleImageAnalysis_ibfk1` FOREIGN KEY (`blSampleImageId`) REFERENCES `BLSampleImage` (`blSampleImageId`);

--
-- Constraints for table `BLSampleType_has_Component`
--
ALTER TABLE `BLSampleType_has_Component`
  ADD CONSTRAINT `blSampleType_has_Component_fk1` FOREIGN KEY (`blSampleTypeId`) REFERENCES `Crystal` (`crystalId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `blSampleType_has_Component_fk2` FOREIGN KEY (`componentId`) REFERENCES `Protein` (`proteinId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `BLSample_has_DiffractionPlan`
--
ALTER TABLE `BLSample_has_DiffractionPlan`
  ADD CONSTRAINT `BLSample_has_DiffractionPlan_ibfk1` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`),
  ADD CONSTRAINT `BLSample_has_DiffractionPlan_ibfk2` FOREIGN KEY (`diffractionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`);

--
-- Constraints for table `BLSample_has_EnergyScan`
--
ALTER TABLE `BLSample_has_EnergyScan`
  ADD CONSTRAINT `BLSample_has_EnergyScan_ibfk_1` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `BLSample_has_EnergyScan_ibfk_2` FOREIGN KEY (`energyScanId`) REFERENCES `EnergyScan` (`energyScanId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `BLSession`
--
ALTER TABLE `BLSession`
  ADD CONSTRAINT `BLSession_ibfk_1` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `BLSession_ibfk_2` FOREIGN KEY (`beamLineSetupId`) REFERENCES `BeamLineSetup` (`beamLineSetupId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `BLSession_ibfk_3` FOREIGN KEY (`beamCalendarId`) REFERENCES `BeamCalendar` (`beamCalendarId`);

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
  ADD CONSTRAINT `BLSubSample_motorPositionfk_1` FOREIGN KEY (`motorPositionId`) REFERENCES `MotorPosition` (`motorPositionId`) ON DELETE CASCADE ON UPDATE CASCADE,
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
-- Constraints for table `Component_has_SubType`
--
ALTER TABLE `Component_has_SubType`
  ADD CONSTRAINT `component_has_SubType_fk1` FOREIGN KEY (`componentId`) REFERENCES `Protein` (`proteinId`) ON DELETE CASCADE,
  ADD CONSTRAINT `component_has_SubType_fk2` FOREIGN KEY (`componentSubTypeId`) REFERENCES `ComponentSubType` (`componentSubTypeId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Container`
--
ALTER TABLE `Container`
  ADD CONSTRAINT `Container_ibfk5` FOREIGN KEY (`ownerId`) REFERENCES `Person` (`personId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `Container_ibfk6` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `Container_ibfk_1` FOREIGN KEY (`dewarId`) REFERENCES `Dewar` (`dewarId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ContainerHistory`
--
ALTER TABLE `ContainerHistory`
  ADD CONSTRAINT `ContainerHistory_ibfk1` FOREIGN KEY (`containerId`) REFERENCES `Container` (`containerId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ContainerInspection`
--
ALTER TABLE `ContainerInspection`
  ADD CONSTRAINT `ContainerInspection_fk1` FOREIGN KEY (`containerId`) REFERENCES `Container` (`containerId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ContainerInspection_fk2` FOREIGN KEY (`inspectionTypeId`) REFERENCES `InspectionType` (`inspectionTypeId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `ContainerInspection_fk3` FOREIGN KEY (`imagerId`) REFERENCES `Imager` (`imagerId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `ContainerInspection_fk4` FOREIGN KEY (`scheduleComponentid`) REFERENCES `ScheduleComponent` (`scheduleComponentId`);

--
-- Constraints for table `ContainerQueue`
--
ALTER TABLE `ContainerQueue`
  ADD CONSTRAINT `ContainerQueue_ibfk1` FOREIGN KEY (`containerId`) REFERENCES `Container` (`containerId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ContainerQueue_ibfk2` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON UPDATE CASCADE;

--
-- Constraints for table `ContainerQueueSample`
--
ALTER TABLE `ContainerQueueSample`
  ADD CONSTRAINT `ContainerQueueSample_ibfk1` FOREIGN KEY (`containerQueueId`) REFERENCES `ContainerQueue` (`containerQueueId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ContainerQueueSample_ibfk2` FOREIGN KEY (`blSubSampleId`) REFERENCES `BLSubSample` (`blSubSampleId`) ON DELETE CASCADE ON UPDATE CASCADE;

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
-- Constraints for table `DataCollectionFileAttachment`
--
ALTER TABLE `DataCollectionFileAttachment`
  ADD CONSTRAINT `dataCollectionFileAttachmentId_fk1` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `DataCollectionGroup`
--
ALTER TABLE `DataCollectionGroup`
  ADD CONSTRAINT `DataCollectionGroup_ibfk_1` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `DataCollectionGroup_ibfk_2` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `DataCollectionGroup_ibfk_3` FOREIGN KEY (`workflowId`) REFERENCES `Workflow` (`workflowId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `DataCollectionPlanGroup`
--
ALTER TABLE `DataCollectionPlanGroup`
  ADD CONSTRAINT `DataCollectionPlanGroup_ibfk1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON UPDATE CASCADE,
  ADD CONSTRAINT `DataCollectionPlanGroup_ibfk2` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON UPDATE CASCADE;

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
-- Constraints for table `DiffractionPlan_has_Detector`
--
ALTER TABLE `DiffractionPlan_has_Detector`
  ADD CONSTRAINT `DiffractionPlan_has_Detector_ibfk1` FOREIGN KEY (`diffractionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`),
  ADD CONSTRAINT `DiffractionPlan_has_Detector_ibfk2` FOREIGN KEY (`detectorId`) REFERENCES `Detector` (`detectorId`);

--
-- Constraints for table `EnergyScan`
--
ALTER TABLE `EnergyScan`
  ADD CONSTRAINT `ES_ibfk_1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ES_ibfk_2` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `ES_ibfk_3` FOREIGN KEY (`blSubSampleId`) REFERENCES `BLSubSample` (`blSubSampleId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

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
-- Constraints for table `MotionCorrection`
--
ALTER TABLE `MotionCorrection`
  ADD CONSTRAINT `fk_MotionCorrection_1` FOREIGN KEY (`movieId`) REFERENCES `Movie` (`movieId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Movie`
--
ALTER TABLE `Movie`
  ADD CONSTRAINT `dataCollectionToMovie` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

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
-- Constraints for table `Particle`
--
ALTER TABLE `Particle`
  ADD CONSTRAINT `Particle_FK1` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `PDBEntry`
--
ALTER TABLE `PDBEntry`
  ADD CONSTRAINT `pdbEntry_FK1` FOREIGN KEY (`autoProcProgramId`) REFERENCES `AutoProcProgram` (`autoProcProgramId`) ON DELETE CASCADE;

--
-- Constraints for table `PDBEntry_has_AutoProcProgram`
--
ALTER TABLE `PDBEntry_has_AutoProcProgram`
  ADD CONSTRAINT `pdbEntry_AutoProcProgram_FK1` FOREIGN KEY (`pdbEntryId`) REFERENCES `PDBEntry` (`pdbEntryId`) ON DELETE CASCADE,
  ADD CONSTRAINT `pdbEntry_AutoProcProgram_FK2` FOREIGN KEY (`autoProcProgramId`) REFERENCES `AutoProcProgram` (`autoProcProgramId`) ON DELETE CASCADE;

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
  ADD CONSTRAINT `PhasingStatistics_phasingHasScalingfk_1` FOREIGN KEY (`phasingHasScalingId1`) REFERENCES `Phasing_has_Scaling` (`phasingHasScalingId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `PhasingStatistics_phasingHasScalingfk_2` FOREIGN KEY (`phasingHasScalingId2`) REFERENCES `Phasing_has_Scaling` (`phasingHasScalingId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_PhasingStatistics_phasingStep` FOREIGN KEY (`phasingStepId`) REFERENCES `PhasingStep` (`phasingStepId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

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
-- Constraints for table `Project`
--
ALTER TABLE `Project`
  ADD CONSTRAINT `Project_FK1` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`);

--
-- Constraints for table `Project_has_BLSample`
--
ALTER TABLE `Project_has_BLSample`
  ADD CONSTRAINT `Project_has_BLSample_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Project_has_BLSample_FK2` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Project_has_DCGroup`
--
ALTER TABLE `Project_has_DCGroup`
  ADD CONSTRAINT `Project_has_DCGroup_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Project_has_DCGroup_FK2` FOREIGN KEY (`dataCollectionGroupId`) REFERENCES `DataCollectionGroup` (`dataCollectionGroupId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Project_has_EnergyScan`
--
ALTER TABLE `Project_has_EnergyScan`
  ADD CONSTRAINT `project_has_energyscan_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `project_has_energyscan_FK2` FOREIGN KEY (`energyScanId`) REFERENCES `EnergyScan` (`energyScanId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Project_has_Person`
--
ALTER TABLE `Project_has_Person`
  ADD CONSTRAINT `project_has_person_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE,
  ADD CONSTRAINT `project_has_person_FK2` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON DELETE CASCADE;

--
-- Constraints for table `Project_has_Protein`
--
ALTER TABLE `Project_has_Protein`
  ADD CONSTRAINT `project_has_protein_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE,
  ADD CONSTRAINT `project_has_protein_FK2` FOREIGN KEY (`proteinId`) REFERENCES `Protein` (`proteinId`) ON DELETE CASCADE;

--
-- Constraints for table `Project_has_Session`
--
ALTER TABLE `Project_has_Session`
  ADD CONSTRAINT `project_has_session_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `project_has_session_FK2` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Project_has_Shipping`
--
ALTER TABLE `Project_has_Shipping`
  ADD CONSTRAINT `project_has_shipping_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE,
  ADD CONSTRAINT `project_has_shipping_FK2` FOREIGN KEY (`shippingId`) REFERENCES `Shipping` (`shippingId`) ON DELETE CASCADE;

--
-- Constraints for table `Project_has_User`
--
ALTER TABLE `Project_has_User`
  ADD CONSTRAINT `Project_Has_user_FK1` FOREIGN KEY (`projectid`) REFERENCES `Project` (`projectId`);

--
-- Constraints for table `Project_has_XFEFSpectrum`
--
ALTER TABLE `Project_has_XFEFSpectrum`
  ADD CONSTRAINT `project_has_xfefspectrum_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE,
  ADD CONSTRAINT `project_has_xfefspectrum_FK2` FOREIGN KEY (`xfeFluorescenceSpectrumId`) REFERENCES `XFEFluorescenceSpectrum` (`xfeFluorescenceSpectrumId`) ON DELETE CASCADE;

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
  ADD CONSTRAINT `Protein_ibfk_1` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `protein_fk3` FOREIGN KEY (`componentTypeId`) REFERENCES `ComponentType` (`componentTypeId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Protein_has_Lattice`
--
ALTER TABLE `Protein_has_Lattice`
  ADD CONSTRAINT `Protein_has_Lattice_ibfk1` FOREIGN KEY (`proteinId`) REFERENCES `Protein` (`proteinId`);

--
-- Constraints for table `Protein_has_PDB`
--
ALTER TABLE `Protein_has_PDB`
  ADD CONSTRAINT `Protein_Has_PDB_fk1` FOREIGN KEY (`proteinid`) REFERENCES `Protein` (`proteinId`),
  ADD CONSTRAINT `Protein_Has_PDB_fk2` FOREIGN KEY (`pdbid`) REFERENCES `PDB` (`pdbId`);

--
-- Constraints for table `RobotAction`
--
ALTER TABLE `RobotAction`
  ADD CONSTRAINT `RobotAction_FK1` FOREIGN KEY (`blsessionId`) REFERENCES `BLSession` (`sessionId`),
  ADD CONSTRAINT `RobotAction_FK2` FOREIGN KEY (`blsampleId`) REFERENCES `BLSample` (`blSampleId`);

--
-- Constraints for table `SamplePlate`
--
ALTER TABLE `SamplePlate`
  ADD CONSTRAINT `PlateToPtateGroup` FOREIGN KEY (`plateGroupId`) REFERENCES `PlateGroup` (`plateGroupId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `SamplePlateToExperiment` FOREIGN KEY (`experimentId`) REFERENCES `Experiment` (`experimentId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `SamplePlateToInstructionSet` FOREIGN KEY (`instructionSetId`) REFERENCES `InstructionSet` (`instructionSetId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `SamplePlateToType` FOREIGN KEY (`plateTypeId`) REFERENCES `PlateType` (`PlateTypeId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

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
-- Constraints for table `ScanParametersModel`
--
ALTER TABLE `ScanParametersModel`
  ADD CONSTRAINT `PDF_Model_ibfk1` FOREIGN KEY (`scanParametersServiceId`) REFERENCES `ScanParametersService` (`scanParametersServiceId`) ON UPDATE CASCADE,
  ADD CONSTRAINT `PDF_Model_ibfk2` FOREIGN KEY (`dataCollectionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`) ON UPDATE CASCADE;

--
-- Constraints for table `ScheduleComponent`
--
ALTER TABLE `ScheduleComponent`
  ADD CONSTRAINT `ScheduleComponent_fk1` FOREIGN KEY (`scheduleId`) REFERENCES `Schedule` (`scheduleId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ScheduleComponent_fk2` FOREIGN KEY (`inspectionTypeId`) REFERENCES `InspectionType` (`inspectionTypeId`) ON DELETE CASCADE;

--
-- Constraints for table `Screen`
--
ALTER TABLE `Screen`
  ADD CONSTRAINT `Screen_fk1` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`);

--
-- Constraints for table `ScreenComponent`
--
ALTER TABLE `ScreenComponent`
  ADD CONSTRAINT `ScreenComponent_fk1` FOREIGN KEY (`screenComponentGroupId`) REFERENCES `ScreenComponentGroup` (`screenComponentGroupId`),
  ADD CONSTRAINT `ScreenComponent_fk2` FOREIGN KEY (`componentId`) REFERENCES `Protein` (`proteinId`);

--
-- Constraints for table `ScreenComponentGroup`
--
ALTER TABLE `ScreenComponentGroup`
  ADD CONSTRAINT `ScreenComponentGroup_fk1` FOREIGN KEY (`screenId`) REFERENCES `Screen` (`screenId`);

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
-- Constraints for table `SessionType`
--
ALTER TABLE `SessionType`
  ADD CONSTRAINT `SessionType_ibfk_1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE;

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
  ADD CONSTRAINT `StructureToBlSample` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `StructureToCrystal` FOREIGN KEY (`crystalId`) REFERENCES `Crystal` (`crystalId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `StructureToMacromolecule` FOREIGN KEY (`macromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `StructureToProposal` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

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
-- Constraints for table `SW_onceToken`
--
ALTER TABLE `SW_onceToken`
  ADD CONSTRAINT `SW_onceToken_fk1` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`),
  ADD CONSTRAINT `SW_onceToken_fk2` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`);

--
-- Constraints for table `UntrustedRegion`
--
ALTER TABLE `UntrustedRegion`
  ADD CONSTRAINT `UntrustedRegion_ibfk_1` FOREIGN KEY (`detectorId`) REFERENCES `Detector` (`detectorId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `UserGroup_has_Permission`
--
ALTER TABLE `UserGroup_has_Permission`
  ADD CONSTRAINT `UserGroup_has_Permission_fk1` FOREIGN KEY (`userGroupId`) REFERENCES `UserGroup` (`userGroupId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `UserGroup_has_Permission_fk2` FOREIGN KEY (`permissionId`) REFERENCES `Permission` (`permissionId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `UserGroup_has_Person`
--
ALTER TABLE `UserGroup_has_Person`
  ADD CONSTRAINT `userGroup_has_Person_fk1` FOREIGN KEY (`userGroupId`) REFERENCES `UserGroup` (`userGroupId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `userGroup_has_Person_fk2` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON DELETE CASCADE ON UPDATE CASCADE;

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

--
-- Constraints for table `XRFFluorescenceMapping`
--
ALTER TABLE `XRFFluorescenceMapping`
  ADD CONSTRAINT `XRFFluorescenceMapping_ibfk1` FOREIGN KEY (`xrfFluorescenceMappingROIId`) REFERENCES `XRFFluorescenceMappingROI` (`xrfFluorescenceMappingROIId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `XRFFluorescenceMapping_ibfk2` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
