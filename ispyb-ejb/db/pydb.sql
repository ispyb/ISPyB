-- phpMyAdmin SQL Dump
-- version 3.4.11.1deb2+deb7u2
-- http://www.phpmyadmin.net
--
-- Host: pyproserv
-- Generation Time: Mar 30, 2017 at 10:44 AM
-- Server version: 5.5.40
-- PHP Version: 5.4.45-0+deb7u2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

-- Database: `pydb`  version 17.03.23

--

DELIMITER $$
--
-- Procedures
--
DROP PROCEDURE IF EXISTS `updateCrystalIdForFX`$$
CREATE DEFINER=`pxadmin`@`%` PROCEDURE `updateCrystalIdForFX`()
BEGIN
  DECLARE done INT DEFAULT FALSE;
  DECLARE protId INT;
  DECLARE cur1
    CURSOR FOR
    SELECT DISTINCT proteinId
      FROM Protein, Proposal
        WHERE      Proposal.proposalId = Protein.proposalId
              AND proposalCode IN ( 'FX');

  OPEN cur1;
  CURSOR_LOOP: LOOP

    FETCH cur1 INTO protId;

    UPDATE BLSample,
       (SELECT bls.blSampleId
          FROM Crystal c,
               Crystal cref,
               DiffractionPlan dp,
               BLSample AS bls,
               DiffractionPlan dpref
         WHERE     cref.crystalId IN (SELECT MIN(t.crystalId)
                                        FROM (  SELECT count(c1.crystalId)
                                                          AS nb,
                                                       crystalId
                                                  FROM Crystal c1
                                                 WHERE c1.proteinId = protId
                                                 AND c1.diffractionPlanId IS NOT NULL
                                              GROUP BY spaceGroup , cell_a
                                              ORDER BY crystalId ASC) t
                                       WHERE t.nb > 1)
               AND c.proteinId = protId
               AND c.crystalId != cref.crystalId
               AND c.spaceGroup = cref.spaceGroup
               AND COALESCE(c.cell_a,0) = COALESCE(cref.cell_a,0)
               AND COALESCE(c.cell_b,0) = COALESCE(cref.cell_b,0)
               AND COALESCE(c.cell_c,0) = COALESCE(cref.cell_c,0)
               AND COALESCE(c.cell_alpha,0) = COALESCE(cref.cell_alpha,0)
               AND COALESCE(c.cell_beta,0) = COALESCE(cref.cell_beta,0)
               AND COALESCE(c.cell_gamma,0) = COALESCE(cref.cell_gamma,0)
               AND bls.crystalId = c.crystalId
               AND dpref.diffractionPlanId = cref.diffractionPlanId
               AND dp.diffractionPlanId = c.diffractionPlanId
               AND COALESCE(dp.experimentKind, 'OSC') = COALESCE(dpref.experimentKind, 'OSC') 
               AND COALESCE(dp.observedResolution,0) = COALESCE(dpref.observedResolution,0)
               AND COALESCE(dp.requiredResolution,0) = COALESCE(dpref.requiredResolution,0)
               AND COALESCE(dp.exposureTime,0) = COALESCE(dpref.exposureTime,0)
               AND COALESCE(dp.oscillationRange,0) = COALESCE(dpref.oscillationRange,0)   ) X
      SET crystalId =
          (SELECT MIN(t.crystalId)
             FROM (  SELECT count(c1.crystalId) AS nb, crystalId
                       FROM Crystal c1
                      WHERE c1.proteinId = protId
                      AND c1.diffractionPlanId IS NOT NULL
                   GROUP BY spaceGroup, cell_a
                   ORDER BY crystalId ASC) t
            WHERE t.nb > 1)
    WHERE BLSample.blsampleId = X.blsampleId;

    UPDATE BLSample,
       (SELECT bls.blSampleId
          FROM Crystal c, Crystal cref, BLSample AS bls
         WHERE     cref.crystalId IN (SELECT MIN(t.crystalId)
                                        FROM (  SELECT count(c1.crystalId)
                                                          AS nb,
                                                       crystalId
                                                  FROM Crystal c1
                                                 WHERE c1.proteinId = protId
                                                 AND c1.diffractionPlanId IS NULL
                                              GROUP BY spaceGroup , cell_a
                                              ORDER BY crystalId ASC) t
                                       WHERE t.nb > 1)
               AND c.proteinId = protId
               AND c.crystalId != cref.crystalId
               AND c.spaceGroup = cref.spaceGroup
               AND COALESCE(c.cell_a,0) = COALESCE(cref.cell_a,0)
               AND COALESCE(c.cell_b,0) = COALESCE(cref.cell_b,0)
               AND COALESCE(c.cell_c,0) = COALESCE(cref.cell_c,0)
               AND COALESCE(c.cell_alpha,0) = COALESCE(cref.cell_alpha,0)
               AND COALESCE(c.cell_beta,0) = COALESCE(cref.cell_beta,0)
               AND COALESCE(c.cell_gamma,0) = COALESCE(cref.cell_gamma,0)
              AND bls.crystalId = c.crystalId
               AND c.diffractionPlanId IS NULL
               AND cref.diffractionPlanId IS NULL) X
      SET crystalId =
          (SELECT MIN(t.crystalId)
             FROM (  SELECT count(c1.crystalId) AS nb, crystalId
                       FROM Crystal c1
                      WHERE c1.proteinId = protId
                      AND c1.diffractionPlanId IS NULL
                   GROUP BY spaceGroup , cell_a
                   ORDER BY crystalId ASC) t
            WHERE t.nb > 1)
    WHERE BLSample.blsampleId = X.blsampleId;

   END LOOP;

   DELETE FROM Crystal
      WHERE crystalId NOT IN (     SELECT crystalId FROM BLSample);

  CLOSE cur1;

END$$

DROP PROCEDURE IF EXISTS `updateCrystalIdForMX`$$
CREATE DEFINER=`pxadmin`@`%` PROCEDURE `updateCrystalIdForMX`()
BEGIN
  DECLARE done INT DEFAULT FALSE;
  DECLARE protId INT;
  DECLARE cur1
    CURSOR FOR
    SELECT DISTINCT proteinId
      FROM Protein, Proposal
        WHERE      Proposal.proposalId = Protein.proposalId
              AND proposalCode IN ( 'MX') AND proposalType LIKE 'MX' AND Proposal.bltimeStamp >= '2011-02-01 11:17:17';

  OPEN cur1;
  CURSOR_LOOP: LOOP

    FETCH cur1 INTO protId;

    UPDATE BLSample,
       (SELECT bls.blSampleId
          FROM Crystal c,
               Crystal cref,
               DiffractionPlan dp,
               BLSample AS bls,
               DiffractionPlan dpref
         WHERE     cref.crystalId IN (SELECT MIN(t.crystalId)
                                        FROM (  SELECT count(c1.crystalId)
                                                          AS nb,
                                                       crystalId
                                                  FROM Crystal c1
                                                 WHERE c1.proteinId = protId
                                                 AND c1.diffractionPlanId IS NOT NULL
                                              GROUP BY spaceGroup, cell_a
                                              ORDER BY crystalId ASC) t
                                       WHERE t.nb > 1)
               AND c.proteinId = protId
               AND c.crystalId != cref.crystalId
               AND c.spaceGroup = cref.spaceGroup
               AND COALESCE(c.cell_a,0) = COALESCE(cref.cell_a,0)
               AND COALESCE(c.cell_b,0) = COALESCE(cref.cell_b,0)
               AND COALESCE(c.cell_c,0) = COALESCE(cref.cell_c,0)
               AND COALESCE(c.cell_alpha,0) = COALESCE(cref.cell_alpha,0)
               AND COALESCE(c.cell_beta,0) = COALESCE(cref.cell_beta,0)
               AND COALESCE(c.cell_gamma,0) = COALESCE(cref.cell_gamma,0)
               AND bls.crystalId = c.crystalId
               AND dpref.diffractionPlanId = cref.diffractionPlanId
               AND dp.diffractionPlanId = c.diffractionPlanId
               AND COALESCE(dp.experimentKind, 'OSC') = COALESCE(dpref.experimentKind, 'OSC') 
               AND COALESCE(dp.observedResolution,0) = COALESCE(dpref.observedResolution,0)
               AND COALESCE(dp.requiredResolution,0) = COALESCE(dpref.requiredResolution,0)
               AND COALESCE(dp.exposureTime,0) = COALESCE(dpref.exposureTime,0)
               AND COALESCE(dp.oscillationRange,0) = COALESCE(dpref.oscillationRange,0)   ) X
      SET crystalId =
          (SELECT MIN(t.crystalId)
             FROM (  SELECT count(c1.crystalId) AS nb, crystalId
                       FROM Crystal c1
                      WHERE c1.proteinId = protId
                      AND c1.diffractionPlanId IS NOT NULL
                   GROUP BY spaceGroup, cell_a
                   ORDER BY crystalId ASC) t
            WHERE t.nb > 1)
    WHERE BLSample.blsampleId = X.blsampleId;

    UPDATE BLSample,
       (SELECT bls.blSampleId
          FROM Crystal c, Crystal cref, BLSample AS bls
         WHERE     cref.crystalId IN (SELECT MIN(t.crystalId)
                                        FROM (  SELECT count(c1.crystalId)
                                                          AS nb,
                                                       crystalId
                                                  FROM Crystal c1
                                                 WHERE c1.proteinId = protId
                                                 AND c1.diffractionPlanId IS NULL
                                              GROUP BY spaceGroup, cell_a
                                              ORDER BY crystalId ASC) t
                                       WHERE t.nb > 1)
               AND c.proteinId = protId
               AND c.crystalId != cref.crystalId
               AND c.spaceGroup = cref.spaceGroup
               AND COALESCE(c.cell_a,0) = COALESCE(cref.cell_a,0)
               AND COALESCE(c.cell_b,0) = COALESCE(cref.cell_b,0)
               AND COALESCE(c.cell_c,0) = COALESCE(cref.cell_c,0)
               AND COALESCE(c.cell_alpha,0) = COALESCE(cref.cell_alpha,0)
               AND COALESCE(c.cell_beta,0) = COALESCE(cref.cell_beta,0)
               AND COALESCE(c.cell_gamma,0) = COALESCE(cref.cell_gamma,0)
              AND bls.crystalId = c.crystalId
               AND c.diffractionPlanId IS NULL
               AND cref.diffractionPlanId IS NULL) X
      SET crystalId =
          (SELECT MIN(t.crystalId)
             FROM (  SELECT count(c1.crystalId) AS nb, crystalId
                       FROM Crystal c1
                      WHERE c1.proteinId = protId
                      AND c1.diffractionPlanId IS NULL
                   GROUP BY spaceGroup, cell_a
                   ORDER BY crystalId ASC) t
            WHERE t.nb > 1)
    WHERE BLSample.blsampleId = X.blsampleId;

   END LOOP;

   DELETE FROM Crystal
      WHERE crystalId NOT IN (     SELECT crystalId FROM BLSample);

  CLOSE cur1;

END$$

DROP PROCEDURE IF EXISTS `updateCrystalIdForProposalId`$$
CREATE DEFINER=`pxadmin`@`%` PROCEDURE `updateCrystalIdForProposalId`(IN propId INT )
BEGIN
  DECLARE done INT DEFAULT FALSE;
  DECLARE protId INT;
  DECLARE cur1 
    CURSOR FOR 
    SELECT DISTINCT proteinId FROM Protein WHERE proposalId = propId; 

  OPEN cur1;
  CURSOR_LOOP: LOOP
  
    FETCH cur1 INTO protId;   
       
    UPDATE BLSample,
       (SELECT bls.blSampleId
          FROM Crystal c,
               Crystal cref,
               DiffractionPlan dp,
               BLSample AS bls,
               DiffractionPlan dpref
         WHERE     cref.crystalId IN (SELECT MIN(t.crystalId)
                                        FROM (  SELECT count(c1.crystalId)
                                                          AS nb,
                                                       crystalId
                                                  FROM Crystal c1
                                                 WHERE c1.proteinId = protId
                                                 AND c1.diffractionPlanId IS NOT NULL
                                              GROUP BY spaceGroup  , cell_a
                                              ORDER BY crystalId ASC) t
                                       WHERE t.nb > 1)
               AND c.proteinId = protId
               AND c.crystalId != cref.crystalId
               AND COALESCE(c.spaceGroup, 'Undefined') = COALESCE(cref.spaceGroup, 'Undefined')
               AND COALESCE(c.cell_a,0) = COALESCE(cref.cell_a,0)
               AND COALESCE(c.cell_b,0) = COALESCE(cref.cell_b,0)
               AND COALESCE(c.cell_c,0) = COALESCE(cref.cell_c,0)
               AND COALESCE(c.cell_alpha,0) = COALESCE(cref.cell_alpha,0)
               AND COALESCE(c.cell_beta,0) = COALESCE(cref.cell_beta,0)
               AND COALESCE(c.cell_gamma,0) = COALESCE(cref.cell_gamma,0)
               AND bls.crystalId = c.crystalId
               AND dpref.diffractionPlanId = cref.diffractionPlanId
               AND dp.diffractionPlanId = c.diffractionPlanId
               AND COALESCE(dp.experimentKind, 'OSC') = COALESCE(dpref.experimentKind, 'OSC') 
               -- AND dp.experimentKind = dpref.experimentKind
               AND COALESCE(dp.observedResolution,0) = COALESCE(dpref.observedResolution,0)
               AND COALESCE(dp.requiredResolution,0) = COALESCE(dpref.requiredResolution,0)
               AND COALESCE(dp.exposureTime,0) = COALESCE(dpref.exposureTime,0)
               AND COALESCE(dp.oscillationRange,0) = COALESCE(dpref.oscillationRange,0)   ) X
      SET crystalId =
          (SELECT MIN(t.crystalId)
             FROM (  SELECT count(c1.crystalId) AS nb, crystalId
                       FROM Crystal c1
                      WHERE c1.proteinId = protId
                      AND c1.diffractionPlanId IS NOT NULL
                   GROUP BY spaceGroup , cell_a
                   ORDER BY crystalId ASC) t
            WHERE t.nb > 1)
    WHERE BLSample.blsampleId = X.blsampleId;

    UPDATE BLSample,
       (SELECT bls.blSampleId
          FROM Crystal c, Crystal cref, BLSample AS bls
         WHERE     cref.crystalId IN (SELECT MIN(t.crystalId)
                                        FROM (  SELECT count(c1.crystalId)
                                                          AS nb,
                                                       crystalId
                                                  FROM Crystal c1
                                                 WHERE c1.proteinId = protId
                                                 AND c1.diffractionPlanId IS NULL
                                              GROUP BY spaceGroup , cell_a
                                              ORDER BY crystalId ASC) t
                                       WHERE t.nb > 1)
               AND c.proteinId = protId
               AND c.crystalId != cref.crystalId
               AND COALESCE(c.spaceGroup, 'Undefined') = COALESCE(cref.spaceGroup, 'Undefined')
               AND COALESCE(c.cell_a,0) = COALESCE(cref.cell_a,0)
               AND COALESCE(c.cell_b,0) = COALESCE(cref.cell_b,0)
               AND COALESCE(c.cell_c,0) = COALESCE(cref.cell_c,0)
               AND COALESCE(c.cell_alpha,0) = COALESCE(cref.cell_alpha,0)
               AND COALESCE(c.cell_beta,0) = COALESCE(cref.cell_beta,0)
               AND COALESCE(c.cell_gamma,0) = COALESCE(cref.cell_gamma,0)
              AND bls.crystalId = c.crystalId
               AND c.diffractionPlanId IS NULL
               AND cref.diffractionPlanId IS NULL) X
      SET crystalId =
          (SELECT MIN(t.crystalId)
             FROM (  SELECT count(c1.crystalId) AS nb, crystalId
                       FROM Crystal c1
                      WHERE c1.proteinId = protId
                      AND c1.diffractionPlanId IS NULL
                   GROUP BY spaceGroup , cell_a
                   ORDER BY crystalId ASC) t
            WHERE t.nb > 1)
    WHERE BLSample.blsampleId = X.blsampleId;

   END LOOP;
   
   COMMIT;

   DELETE FROM Crystal
      WHERE crystalId NOT IN (     SELECT crystalId FROM BLSample);
      
  COMMIT;

  CLOSE cur1;

END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `AbInitioModel`
--

DROP TABLE IF EXISTS `AbInitioModel`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=34849 ;

-- --------------------------------------------------------

--
-- Table structure for table `Additive`
--

DROP TABLE IF EXISTS `Additive`;
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

DROP TABLE IF EXISTS `AdminActivity`;
CREATE TABLE IF NOT EXISTS `AdminActivity` (
  `adminActivityId` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL DEFAULT '',
  `action` varchar(45) DEFAULT NULL,
  `comments` varchar(100) DEFAULT NULL,
  `dateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`adminActivityId`),
  UNIQUE KEY `username` (`username`),
  KEY `AdminActivity_FKAction` (`action`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2056 ;

-- --------------------------------------------------------

--
-- Table structure for table `AdminVar`
--

DROP TABLE IF EXISTS `AdminVar`;
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

DROP TABLE IF EXISTS `Assembly`;
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

DROP TABLE IF EXISTS `AssemblyHasMacromolecule`;
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

DROP TABLE IF EXISTS `AssemblyRegion`;
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

DROP TABLE IF EXISTS `AutoProc`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1394360 ;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcIntegration`
--

DROP TABLE IF EXISTS `AutoProcIntegration`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1553786 ;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcProgram`
--

DROP TABLE IF EXISTS `AutoProcProgram`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1416223 ;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcProgramAttachment`
--

DROP TABLE IF EXISTS `AutoProcProgramAttachment`;
CREATE TABLE IF NOT EXISTS `AutoProcProgramAttachment` (
  `autoProcProgramAttachmentId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `autoProcProgramId` int(10) unsigned NOT NULL COMMENT 'Related autoProcProgram item',
  `fileType` enum('Log','Result','Graph') DEFAULT NULL COMMENT 'Type of file Attachment',
  `fileName` varchar(255) DEFAULT NULL COMMENT 'Attachment filename',
  `filePath` varchar(255) DEFAULT NULL COMMENT 'Attachment filepath to disk storage',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`autoProcProgramAttachmentId`),
  KEY `AutoProcProgramAttachmentIdx1` (`autoProcProgramId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=14697767 ;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcScaling`
--

DROP TABLE IF EXISTS `AutoProcScaling`;
CREATE TABLE IF NOT EXISTS `AutoProcScaling` (
  `autoProcScalingId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `autoProcId` int(10) unsigned DEFAULT NULL COMMENT 'Related autoProc item (used by foreign key)',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`autoProcScalingId`),
  KEY `AutoProcScalingFk1` (`autoProcId`),
  KEY `AutoProcScalingIdx1` (`autoProcScalingId`,`autoProcId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1394371 ;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcScalingStatistics`
--

DROP TABLE IF EXISTS `AutoProcScalingStatistics`;
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
  `ccAno` float DEFAULT NULL,
  `sigAno` varchar(45) DEFAULT NULL,
  `isa` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`autoProcScalingStatisticsId`),
  KEY `AutoProcScalingStatisticsIdx1` (`autoProcScalingId`),
  KEY `AutoProcScalingStatistics_FKindexType` (`scalingStatisticsType`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4258117 ;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcScaling_has_Int`
--

DROP TABLE IF EXISTS `AutoProcScaling_has_Int`;
CREATE TABLE IF NOT EXISTS `AutoProcScaling_has_Int` (
  `autoProcScaling_has_IntId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `autoProcScalingId` int(10) unsigned DEFAULT NULL COMMENT 'AutoProcScaling item',
  `autoProcIntegrationId` int(10) unsigned NOT NULL COMMENT 'AutoProcIntegration item',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`autoProcScaling_has_IntId`),
  KEY `AutoProcScl_has_IntIdx1` (`autoProcScalingId`),
  KEY `AutoProcScal_has_IntIdx2` (`autoProcIntegrationId`),
  KEY `AutoProcScalingHasInt_FKIndex3` (`autoProcScalingId`,`autoProcIntegrationId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1394311 ;

-- --------------------------------------------------------

--
-- Table structure for table `AutoProcStatus`
--

DROP TABLE IF EXISTS `AutoProcStatus`;
CREATE TABLE IF NOT EXISTS `AutoProcStatus` (
  `autoProcStatusId` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `autoProcIntegrationId` int(10) unsigned NOT NULL,
  `step` enum('Indexing','Integration','Correction','Scaling','Importing') NOT NULL COMMENT 'autoprocessing step',
  `status` enum('Launched','Successful','Failed') NOT NULL COMMENT 'autoprocessing status',
  `comments` varchar(1024) DEFAULT NULL COMMENT 'comments',
  `bltimeStamp` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`autoProcStatusId`),
  KEY `AutoProcStatus_FKIndex1` (`autoProcIntegrationId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='AutoProcStatus table is linked to AutoProcIntegration' AUTO_INCREMENT=2135959 ;

-- --------------------------------------------------------

--
-- Table structure for table `BeamLineSetup`
--

DROP TABLE IF EXISTS `BeamLineSetup`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1285522 ;

-- --------------------------------------------------------

--
-- Table structure for table `BLSample`
--

DROP TABLE IF EXISTS `BLSample`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=588608 ;

-- --------------------------------------------------------

--
-- Table structure for table `BLSession`
--

DROP TABLE IF EXISTS `BLSession`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=58189 ;

-- --------------------------------------------------------

--
-- Table structure for table `BLSession_has_SCPosition`
--

DROP TABLE IF EXISTS `BLSession_has_SCPosition`;
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

DROP TABLE IF EXISTS `BLSubSample`;
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

DROP TABLE IF EXISTS `Buffer`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6229 ;

-- --------------------------------------------------------

--
-- Table structure for table `BufferHasAdditive`
--

DROP TABLE IF EXISTS `BufferHasAdditive`;
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
-- Table structure for table `ComponentType`
--

DROP TABLE IF EXISTS `ComponentType`;
CREATE TABLE IF NOT EXISTS `ComponentType` (
  `componentTypeId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(31) NOT NULL,
  PRIMARY KEY (`componentTypeId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

-- --------------------------------------------------------

--
-- Table structure for table `Container`
--

DROP TABLE IF EXISTS `Container`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=339487 ;

-- --------------------------------------------------------

--
-- Table structure for table `Crystal`
--

DROP TABLE IF EXISTS `Crystal`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=547049 ;

-- --------------------------------------------------------

--
-- Table structure for table `Crystal_has_UUID`
--

DROP TABLE IF EXISTS `Crystal_has_UUID`;
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

DROP TABLE IF EXISTS `DataAcquisition`;
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

DROP TABLE IF EXISTS `DataCollection`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2004220 ;

-- --------------------------------------------------------

--
-- Table structure for table `DataCollectionGroup`
--

DROP TABLE IF EXISTS `DataCollectionGroup`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='a dataCollectionGroup is a group of dataCollection for a spe' AUTO_INCREMENT=1631402 ;

-- --------------------------------------------------------

--
-- Table structure for table `DatamatrixInSampleChanger`
--

DROP TABLE IF EXISTS `DatamatrixInSampleChanger`;
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

DROP TABLE IF EXISTS `Detector`;
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

DROP TABLE IF EXISTS `Dewar`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=311961 ;

-- --------------------------------------------------------

--
-- Table structure for table `DewarLocation`
--

DROP TABLE IF EXISTS `DewarLocation`;
CREATE TABLE IF NOT EXISTS `DewarLocation` (
  `eventId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `dewarNumber` varchar(128) NOT NULL COMMENT 'Dewar number',
  `userId` varchar(128) DEFAULT NULL COMMENT 'User who locates the dewar',
  `dateTime` datetime DEFAULT NULL COMMENT 'Date and time of locatization',
  `locationName` varchar(128) DEFAULT NULL COMMENT 'Location of the dewar',
  `courierName` varchar(128) DEFAULT NULL COMMENT 'Carrier name who''s shipping back the dewar',
  `courierTrackingNumber` varchar(128) DEFAULT NULL COMMENT 'Tracking number of the shippment',
  PRIMARY KEY (`eventId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='ISPyB Dewar location table' AUTO_INCREMENT=14080 ;

-- --------------------------------------------------------

--
-- Table structure for table `DewarLocationList`
--

DROP TABLE IF EXISTS `DewarLocationList`;
CREATE TABLE IF NOT EXISTS `DewarLocationList` (
  `locationId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `locationName` varchar(128) NOT NULL DEFAULT '' COMMENT 'Location',
  PRIMARY KEY (`locationId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='List of locations for dewars' AUTO_INCREMENT=11 ;

-- --------------------------------------------------------

--
-- Table structure for table `DewarTransportHistory`
--

DROP TABLE IF EXISTS `DewarTransportHistory`;
CREATE TABLE IF NOT EXISTS `DewarTransportHistory` (
  `DewarTransportHistoryId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `dewarId` int(10) unsigned DEFAULT NULL,
  `dewarStatus` varchar(45) NOT NULL,
  `storageLocation` varchar(45) NOT NULL,
  `arrivalDate` datetime NOT NULL,
  PRIMARY KEY (`DewarTransportHistoryId`),
  KEY `DewarTransportHistory_FKIndex1` (`dewarId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=36780 ;

-- --------------------------------------------------------

--
-- Table structure for table `DiffractionPlan`
--

DROP TABLE IF EXISTS `DiffractionPlan`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=652210 ;

-- --------------------------------------------------------

--
-- Table structure for table `EnergyScan`
--

DROP TABLE IF EXISTS `EnergyScan`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=14129 ;

-- --------------------------------------------------------

--
-- Table structure for table `Experiment`
--

DROP TABLE IF EXISTS `Experiment`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=15369 ;

-- --------------------------------------------------------

--
-- Table structure for table `ExperimentKindDetails`
--

DROP TABLE IF EXISTS `ExperimentKindDetails`;
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

DROP TABLE IF EXISTS `FitStructureToExperimentalData`;
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

DROP TABLE IF EXISTS `Frame`;
CREATE TABLE IF NOT EXISTS `Frame` (
  `frameId` int(10) NOT NULL AUTO_INCREMENT,
  `filePath` varchar(255) DEFAULT NULL,
  `comments` varchar(45) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  PRIMARY KEY (`frameId`),
  KEY `FILE` (`filePath`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1609165 ;

-- --------------------------------------------------------

--
-- Table structure for table `FrameList`
--

DROP TABLE IF EXISTS `FrameList`;
CREATE TABLE IF NOT EXISTS `FrameList` (
  `frameListId` int(10) NOT NULL AUTO_INCREMENT,
  `comments` int(10) DEFAULT NULL,
  PRIMARY KEY (`frameListId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=186418 ;

-- --------------------------------------------------------

--
-- Table structure for table `FrameSet`
--

DROP TABLE IF EXISTS `FrameSet`;
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

DROP TABLE IF EXISTS `FrameToList`;
CREATE TABLE IF NOT EXISTS `FrameToList` (
  `frameToListId` int(10) NOT NULL AUTO_INCREMENT,
  `frameListId` int(10) NOT NULL,
  `frameId` int(10) NOT NULL,
  PRIMARY KEY (`frameToListId`),
  KEY `FrameToLisToFrameList` (`frameListId`),
  KEY `FrameToListToFrame` (`frameId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2368133 ;

-- --------------------------------------------------------

--
-- Table structure for table `GeometryClassname`
--

DROP TABLE IF EXISTS `GeometryClassname`;
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

DROP TABLE IF EXISTS `GridInfo`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=114485 ;

-- --------------------------------------------------------

--
-- Table structure for table `Image`
--

DROP TABLE IF EXISTS `Image`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=51408996 ;

-- --------------------------------------------------------

--
-- Table structure for table `ImageQualityIndicators`
--

DROP TABLE IF EXISTS `ImageQualityIndicators`;
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

DROP TABLE IF EXISTS `InputParameterWorkflow`;
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

DROP TABLE IF EXISTS `Instruction`;
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

DROP TABLE IF EXISTS `InstructionSet`;
CREATE TABLE IF NOT EXISTS `InstructionSet` (
  `instructionSetId` int(10) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`instructionSetId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `IspybAutoProcAttachment`
--

DROP TABLE IF EXISTS `IspybAutoProcAttachment`;
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

DROP TABLE IF EXISTS `IspybCrystalClass`;
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

DROP TABLE IF EXISTS `IspybReference`;
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

DROP TABLE IF EXISTS `LabContact`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=255559 ;

-- --------------------------------------------------------

--
-- Table structure for table `Laboratory`
--

DROP TABLE IF EXISTS `Laboratory`;
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
  `laboratoryExtPk` int(10) DEFAULT NULL,
  PRIMARY KEY (`laboratoryId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=312157 ;

-- --------------------------------------------------------

--
-- Table structure for table `Log4Stat`
--

DROP TABLE IF EXISTS `Log4Stat`;
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

DROP TABLE IF EXISTS `Login`;
CREATE TABLE IF NOT EXISTS `Login` (
  `loginId` int(11) NOT NULL AUTO_INCREMENT,
  `token` varchar(45) NOT NULL,
  `username` varchar(45) NOT NULL,
  `roles` varchar(1024) NOT NULL,
  `siteId` varchar(45) DEFAULT NULL,
  `authorized` varchar(1024) DEFAULT NULL,
  `expirationTime` datetime NOT NULL,
  PRIMARY KEY (`loginId`),
  KEY `Token` (`token`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6944 ;

-- --------------------------------------------------------

--
-- Table structure for table `Macromolecule`
--

DROP TABLE IF EXISTS `Macromolecule`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=53295 ;

-- --------------------------------------------------------

--
-- Table structure for table `MacromoleculeRegion`
--

DROP TABLE IF EXISTS `MacromoleculeRegion`;
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

DROP TABLE IF EXISTS `Measurement`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=248149 ;

-- --------------------------------------------------------

--
-- Table structure for table `MeasurementToDataCollection`
--

DROP TABLE IF EXISTS `MeasurementToDataCollection`;
CREATE TABLE IF NOT EXISTS `MeasurementToDataCollection` (
  `measurementToDataCollectionId` int(10) NOT NULL AUTO_INCREMENT,
  `dataCollectionId` int(10) DEFAULT NULL,
  `measurementId` int(10) DEFAULT NULL,
  `dataCollectionOrder` int(10) DEFAULT NULL,
  PRIMARY KEY (`measurementToDataCollectionId`),
  KEY `MeasurementToDataCollectionToDataCollection` (`dataCollectionId`),
  KEY `MeasurementToDataCollectionToMeasurement` (`measurementId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=248149 ;

-- --------------------------------------------------------

--
-- Table structure for table `MeasurementUnit`
--

DROP TABLE IF EXISTS `MeasurementUnit`;
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

DROP TABLE IF EXISTS `Merge`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=128089 ;

-- --------------------------------------------------------

--
-- Table structure for table `MixtureToStructure`
--

DROP TABLE IF EXISTS `MixtureToStructure`;
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

DROP TABLE IF EXISTS `Model`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=590188 ;

-- --------------------------------------------------------

--
-- Table structure for table `ModelBuilding`
--

DROP TABLE IF EXISTS `ModelBuilding`;
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

DROP TABLE IF EXISTS `ModelList`;
CREATE TABLE IF NOT EXISTS `ModelList` (
  `modelListId` int(10) NOT NULL AUTO_INCREMENT,
  `nsdFilePath` varchar(255) DEFAULT NULL,
  `chi2RgFilePath` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`modelListId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=37593 ;

-- --------------------------------------------------------

--
-- Table structure for table `ModelToList`
--

DROP TABLE IF EXISTS `ModelToList`;
CREATE TABLE IF NOT EXISTS `ModelToList` (
  `modelToListId` int(10) NOT NULL AUTO_INCREMENT,
  `modelId` int(10) NOT NULL,
  `modelListId` int(10) NOT NULL,
  PRIMARY KEY (`modelToListId`),
  KEY `ModelToListToList` (`modelListId`),
  KEY `ModelToListToModel` (`modelId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=485737 ;

-- --------------------------------------------------------

--
-- Table structure for table `MotorPosition`
--

DROP TABLE IF EXISTS `MotorPosition`;
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

DROP TABLE IF EXISTS `MXMRRun`;
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

DROP TABLE IF EXISTS `MXMRRunBlob`;
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

DROP TABLE IF EXISTS `PDB`;
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

DROP TABLE IF EXISTS `Person`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=398354 ;

-- --------------------------------------------------------

--
-- Table structure for table `Phasing`
--

DROP TABLE IF EXISTS `Phasing`;
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

DROP TABLE IF EXISTS `PhasingAnalysis`;
CREATE TABLE IF NOT EXISTS `PhasingAnalysis` (
  `phasingAnalysisId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`phasingAnalysisId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=135302 ;

-- --------------------------------------------------------

--
-- Table structure for table `PhasingProgramAttachment`
--

DROP TABLE IF EXISTS `PhasingProgramAttachment`;
CREATE TABLE IF NOT EXISTS `PhasingProgramAttachment` (
  `phasingProgramAttachmentId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `phasingProgramRunId` int(11) unsigned NOT NULL COMMENT 'Related program item',
  `fileType` enum('IMAGE','Map','Logfile','PDB','CSV','INS','RES','TXT') DEFAULT NULL COMMENT 'file type',
  `fileName` varchar(45) DEFAULT NULL COMMENT 'file name',
  `filePath` varchar(255) DEFAULT NULL COMMENT 'file path',
  `recordTimeStamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`phasingProgramAttachmentId`),
  KEY `PhasingProgramAttachment_FKIndex1` (`phasingProgramRunId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=158265 ;

-- --------------------------------------------------------

--
-- Table structure for table `PhasingProgramRun`
--

DROP TABLE IF EXISTS `PhasingProgramRun`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=321301 ;

-- --------------------------------------------------------

--
-- Table structure for table `PhasingStatistics`
--

DROP TABLE IF EXISTS `PhasingStatistics`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=623757 ;

-- --------------------------------------------------------

--
-- Table structure for table `PhasingStep`
--

DROP TABLE IF EXISTS `PhasingStep`;
CREATE TABLE IF NOT EXISTS `PhasingStep` (
  `phasingStepId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `previousPhasingStepId` int(10) unsigned DEFAULT NULL,
  `programRunId` int(10) unsigned DEFAULT NULL,
  `spaceGroupId` int(10) unsigned DEFAULT NULL,
  `autoProcScalingId` int(10) unsigned DEFAULT NULL,
  `phasingAnalysisId` int(10) unsigned DEFAULT NULL,
  `phasingStepType` enum('PREPARE','SUBSTRUCTUREDETERMINATION','PHASING','MODELBUILDING','REFINEMENT','LIGAND_FIT') DEFAULT NULL,
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=185936 ;

-- --------------------------------------------------------

--
-- Table structure for table `Phasing_has_Scaling`
--

DROP TABLE IF EXISTS `Phasing_has_Scaling`;
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

DROP TABLE IF EXISTS `PlateGroup`;
CREATE TABLE IF NOT EXISTS `PlateGroup` (
  `plateGroupId` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `storageTemperature` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`plateGroupId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11765 ;

-- --------------------------------------------------------

--
-- Table structure for table `PlateType`
--

DROP TABLE IF EXISTS `PlateType`;
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

DROP TABLE IF EXISTS `Position`;
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

DROP TABLE IF EXISTS `PreparePhasingData`;
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

DROP TABLE IF EXISTS `Proposal`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8098 ;

-- --------------------------------------------------------

--
-- Table structure for table `ProposalHasPerson`
--

DROP TABLE IF EXISTS `ProposalHasPerson`;
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

DROP TABLE IF EXISTS `Protein`;
CREATE TABLE IF NOT EXISTS `Protein` (
  `proteinId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `proposalId` int(10) unsigned NOT NULL DEFAULT '0',
  `name` varchar(255) DEFAULT NULL,
  `acronym` varchar(45) DEFAULT NULL,
  `safetyLevel` enum('GREEN','YELLOW','RED') DEFAULT NULL,
  `molecularMass` double DEFAULT NULL,
  `proteinType` varchar(45) DEFAULT NULL,
  `sequence` varchar(255) DEFAULT NULL,
  `personId` int(10) unsigned DEFAULT NULL,
  `bltimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `isCreatedBySampleSheet` tinyint(1) DEFAULT '0',
  `externalId` binary(16) DEFAULT NULL,
  `componentTypeId` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`proteinId`),
  KEY `Protein_FKIndex1` (`proposalId`),
  KEY `ProteinAcronym_Index` (`proposalId`,`acronym`),
  KEY `Protein_FKIndex2` (`personId`),
  KEY `Protein_Index2` (`acronym`) USING BTREE,
  KEY `protein_fk3` (`componentTypeId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=371907 ;

-- --------------------------------------------------------

--
-- Table structure for table `Protein_has_PDB`
--

DROP TABLE IF EXISTS `Protein_has_PDB`;
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

DROP TABLE IF EXISTS `RigidBodyModeling`;
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
-- Table structure for table `RobotAction`
--

DROP TABLE IF EXISTS `RobotAction`;
CREATE TABLE IF NOT EXISTS `RobotAction` (
  `robotActionId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `blsessionId` int(11) unsigned NOT NULL,
  `blsampleId` int(11) unsigned DEFAULT NULL,
  `actionType` enum('LOAD','UNLOAD','DISPOSE','STORE','WASH','ANNEAL') DEFAULT NULL,
  `startTimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `endTimestamp` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `status` enum('SUCCESS','ERROR','CRITICAL','WARNING','COMMANDNOTSENT') DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `containerLocation` smallint(6) DEFAULT NULL,
  `dewarLocation` smallint(6) DEFAULT NULL,
  `sampleBarcode` varchar(45) DEFAULT NULL,
  `xtalSnapshotBefore` varchar(255) DEFAULT NULL,
  `xtalSnapshotAfter` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`robotActionId`),
  KEY `RobotAction_FK1` (`blsessionId`),
  KEY `RobotAction_FK2` (`blsampleId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Robot actions as reported by MXCube' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `Run`
--

DROP TABLE IF EXISTS `Run`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=114178 ;

-- --------------------------------------------------------

--
-- Table structure for table `SafetyLevel`
--

DROP TABLE IF EXISTS `SafetyLevel`;
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

DROP TABLE IF EXISTS `SamplePlate`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=35293 ;

-- --------------------------------------------------------

--
-- Table structure for table `SamplePlatePosition`
--

DROP TABLE IF EXISTS `SamplePlatePosition`;
CREATE TABLE IF NOT EXISTS `SamplePlatePosition` (
  `samplePlatePositionId` int(10) NOT NULL AUTO_INCREMENT,
  `samplePlateId` int(10) NOT NULL,
  `rowNumber` int(11) DEFAULT NULL,
  `columnNumber` int(11) DEFAULT NULL,
  `volume` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`samplePlatePositionId`),
  KEY `PlatePositionToPlate` (`samplePlateId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=100266 ;

-- --------------------------------------------------------

--
-- Table structure for table `SaxsDataCollection`
--

DROP TABLE IF EXISTS `SaxsDataCollection`;
CREATE TABLE IF NOT EXISTS `SaxsDataCollection` (
  `dataCollectionId` int(10) NOT NULL AUTO_INCREMENT,
  `experimentId` int(10) NOT NULL,
  `comments` varchar(5120) DEFAULT NULL,
  PRIMARY KEY (`dataCollectionId`),
  KEY `SaxsDataCollectionToExperiment` (`experimentId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=82717 ;

-- --------------------------------------------------------

--
-- Table structure for table `SchemaStatus`
--

DROP TABLE IF EXISTS `SchemaStatus`;
CREATE TABLE IF NOT EXISTS `SchemaStatus` (
  `schemaStatusId` int(11) NOT NULL AUTO_INCREMENT,
  `scriptName` varchar(100) NOT NULL,
  `schemaStatus` varchar(10) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`schemaStatusId`),
  UNIQUE KEY `scriptName` (`scriptName`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=28 ;

-- --------------------------------------------------------

--
-- Table structure for table `Screening`
--

DROP TABLE IF EXISTS `Screening`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=417551 ;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningInput`
--

DROP TABLE IF EXISTS `ScreeningInput`;
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

DROP TABLE IF EXISTS `ScreeningOutput`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=417492 ;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningOutputLattice`
--

DROP TABLE IF EXISTS `ScreeningOutputLattice`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=284590 ;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningRank`
--

DROP TABLE IF EXISTS `ScreeningRank`;
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

DROP TABLE IF EXISTS `ScreeningRankSet`;
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

DROP TABLE IF EXISTS `ScreeningStrategy`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=222895 ;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningStrategySubWedge`
--

DROP TABLE IF EXISTS `ScreeningStrategySubWedge`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=224899 ;

-- --------------------------------------------------------

--
-- Table structure for table `ScreeningStrategyWedge`
--

DROP TABLE IF EXISTS `ScreeningStrategyWedge`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=217726 ;

-- --------------------------------------------------------

--
-- Table structure for table `Session_has_Person`
--

DROP TABLE IF EXISTS `Session_has_Person`;
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

DROP TABLE IF EXISTS `Shipping`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=308010 ;

-- --------------------------------------------------------

--
-- Table structure for table `ShippingHasSession`
--

DROP TABLE IF EXISTS `ShippingHasSession`;
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

DROP TABLE IF EXISTS `SpaceGroup`;
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

DROP TABLE IF EXISTS `Specimen`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=101982 ;

-- --------------------------------------------------------

--
-- Table structure for table `StockSolution`
--

DROP TABLE IF EXISTS `StockSolution`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=85 ;

-- --------------------------------------------------------

--
-- Table structure for table `Stoichiometry`
--

DROP TABLE IF EXISTS `Stoichiometry`;
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

DROP TABLE IF EXISTS `Structure`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=33 ;

-- --------------------------------------------------------

--
-- Table structure for table `SubstructureDetermination`
--

DROP TABLE IF EXISTS `SubstructureDetermination`;
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

DROP TABLE IF EXISTS `Subtraction`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=52885 ;

-- --------------------------------------------------------

--
-- Table structure for table `SubtractionToAbInitioModel`
--

DROP TABLE IF EXISTS `SubtractionToAbInitioModel`;
CREATE TABLE IF NOT EXISTS `SubtractionToAbInitioModel` (
  `subtractionToAbInitioModelId` int(10) NOT NULL AUTO_INCREMENT,
  `abInitioId` int(10) DEFAULT NULL,
  `subtractionId` int(10) DEFAULT NULL,
  PRIMARY KEY (`subtractionToAbInitioModelId`),
  KEY `substractionToAbInitioModelToAbinitioModel` (`abInitioId`),
  KEY `ubstractionToSubstraction` (`subtractionId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=34853 ;

-- --------------------------------------------------------

--
-- Table structure for table `Superposition`
--

DROP TABLE IF EXISTS `Superposition`;
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

DROP TABLE IF EXISTS `UntrustedRegion`;
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
DROP VIEW IF EXISTS `V_AnalysisInfo`;
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
DROP VIEW IF EXISTS `v_datacollection`;
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
,`AutoProcProgram_processingStatus` tinyint(1)
,`AutoProcProgram_autoProcProgramId` int(10) unsigned
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection_autoprocintegration`
--
DROP VIEW IF EXISTS `v_datacollection_autoprocintegration`;
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
,`PhasingStep_phasing_phasingStepType` enum('PREPARE','SUBSTRUCTUREDETERMINATION','PHASING','MODELBUILDING','REFINEMENT','LIGAND_FIT')
,`SpaceGroup_spaceGroupShortName` varchar(45)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection_phasing`
--
DROP VIEW IF EXISTS `v_datacollection_phasing`;
CREATE TABLE IF NOT EXISTS `v_datacollection_phasing` (
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
DROP VIEW IF EXISTS `v_datacollection_phasing_program_run`;
CREATE TABLE IF NOT EXISTS `v_datacollection_phasing_program_run` (
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
,`fileType` enum('IMAGE','Map','Logfile','PDB','CSV','INS','RES','TXT')
,`fileName` varchar(45)
,`filePath` varchar(255)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_datacollection_summary`
--
DROP VIEW IF EXISTS `v_datacollection_summary`;
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
,`ScreeningOutput_totalExposureTime` double
,`ScreeningOutput_totalRotationRange` double
,`ScreeningOutput_totalNumberOfImages` int(11)
,`ScreeningStrategySubWedge_exposureTime` float
,`ScreeningStrategySubWedge_transmission` float
,`ScreeningStrategySubWedge_oscillationRange` float
,`ScreeningStrategySubWedge_numberOfImages` int(10) unsigned
,`ScreeningStrategySubWedge_multiplicity` float
,`ScreeningStrategySubWedge_completeness` float
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
,`Detector_detectorType` varchar(255)
,`Detector_detectorManufacturer` varchar(255)
,`Detector_detectorModel` varchar(255)
,`Detector_detectorPixelSizeHorizontal` float
,`Detector_detectorPixelSizeVertical` float
,`Detector_detectorSerialNumber` float
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
--
DROP VIEW IF EXISTS `v_datacollection_summary_autoprocintegration`;
CREATE TABLE IF NOT EXISTS `v_datacollection_summary_autoprocintegration` (
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
,`v_datacollection_summary_autoprocintegration_processingStatus` tinyint(1)
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
--
DROP VIEW IF EXISTS `v_datacollection_summary_datacollectiongroup`;
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
DROP VIEW IF EXISTS `v_datacollection_summary_phasing`;
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
DROP VIEW IF EXISTS `v_datacollection_summary_screening`;
CREATE TABLE IF NOT EXISTS `v_datacollection_summary_screening` (
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
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_dewar`
--
DROP VIEW IF EXISTS `v_dewar`;
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
DROP VIEW IF EXISTS `v_dewarBeamline`;
CREATE TABLE IF NOT EXISTS `v_dewarBeamline` (
`beamLineName` varchar(45)
,`COUNT(*)` bigint(21)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_dewarBeamlineByWeek`
--
DROP VIEW IF EXISTS `v_dewarBeamlineByWeek`;
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
DROP VIEW IF EXISTS `v_dewarByWeek`;
CREATE TABLE IF NOT EXISTS `v_dewarByWeek` (
`Week` varchar(16)
,`Dewars Tracked` bigint(21)
,`Dewars Non-Tracked` bigint(21)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_dewarByWeekTotal`
--
DROP VIEW IF EXISTS `v_dewarByWeekTotal`;
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
DROP VIEW IF EXISTS `v_dewarList`;
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
DROP VIEW IF EXISTS `v_dewarProposalCode`;
CREATE TABLE IF NOT EXISTS `v_dewarProposalCode` (
`proposalCode` varchar(45)
,`COUNT(*)` bigint(21)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_dewarProposalCodeByWeek`
--
DROP VIEW IF EXISTS `v_dewarProposalCodeByWeek`;
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
DROP VIEW IF EXISTS `v_dewar_summary`;
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
DROP VIEW IF EXISTS `v_energyScan`;
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
DROP VIEW IF EXISTS `v_hour`;
CREATE TABLE IF NOT EXISTS `v_hour` (
`num` varchar(18)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_Log4Stat`
--
DROP VIEW IF EXISTS `v_Log4Stat`;
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
DROP VIEW IF EXISTS `v_logonByHour`;
CREATE TABLE IF NOT EXISTS `v_logonByHour` (
`Hour` varchar(7)
,`Distinct logins` bigint(21)
,`Total logins` bigint(22)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_logonByMonthDay`
--
DROP VIEW IF EXISTS `v_logonByMonthDay`;
CREATE TABLE IF NOT EXISTS `v_logonByMonthDay` (
`Day` varchar(5)
,`Distinct logins` bigint(21)
,`Total logins` bigint(22)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_logonByWeek`
--
DROP VIEW IF EXISTS `v_logonByWeek`;
CREATE TABLE IF NOT EXISTS `v_logonByWeek` (
`Week` varchar(16)
,`Distinct logins` bigint(21)
,`Total logins` bigint(22)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_logonByWeekDay`
--
DROP VIEW IF EXISTS `v_logonByWeekDay`;
CREATE TABLE IF NOT EXISTS `v_logonByWeekDay` (
`Day` varchar(64)
,`Distinct logins` bigint(21)
,`Total logins` bigint(22)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_monthDay`
--
DROP VIEW IF EXISTS `v_monthDay`;
CREATE TABLE IF NOT EXISTS `v_monthDay` (
`num` varchar(10)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_mx_autoprocessing_stats`
--
DROP VIEW IF EXISTS `v_mx_autoprocessing_stats`;
CREATE TABLE IF NOT EXISTS `v_mx_autoprocessing_stats` (
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
--
DROP VIEW IF EXISTS `v_mx_sample`;
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
DROP VIEW IF EXISTS `v_phasing`;
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
,`PhasingProgramRun_recordTimeStamp` timestamp
,`Protein_proteinId` int(10) unsigned
,`BLSession_sessionId` int(10) unsigned
,`BLSession_proposalId` int(10) unsigned
,`PhasingStatistics_phasingStatisticsId` int(11) unsigned
,`PhasingStatistics_metric` enum('Rcullis','Average Fragment Length','Chain Count','Residues Count','CC','PhasingPower','FOM','<d"/sig>','Best CC','CC(1/2)','Weak CC','CFOM','Pseudo_free_CC','CC of partial model')
,`PhasingStatistics_statisticsValue` double
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_sample`
--
DROP VIEW IF EXISTS `v_sample`;
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
DROP VIEW IF EXISTS `v_sampleByWeek`;
CREATE TABLE IF NOT EXISTS `v_sampleByWeek` (
`Week` varchar(16)
,`Samples` bigint(21)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_saxs_datacollection`
--
DROP VIEW IF EXISTS `v_saxs_datacollection`;
CREATE TABLE IF NOT EXISTS `v_saxs_datacollection` (
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
--
DROP VIEW IF EXISTS `v_session`;
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
-- Stand-in structure for view `v_tracking_shipment_history`
--
DROP VIEW IF EXISTS `v_tracking_shipment_history`;
CREATE TABLE IF NOT EXISTS `v_tracking_shipment_history` (
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
--
DROP VIEW IF EXISTS `v_week`;
CREATE TABLE IF NOT EXISTS `v_week` (
`num` varchar(7)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_weekDay`
--
DROP VIEW IF EXISTS `v_weekDay`;
CREATE TABLE IF NOT EXISTS `v_weekDay` (
`day` varchar(10)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_xfeFluorescenceSpectrum`
--
DROP VIEW IF EXISTS `v_xfeFluorescenceSpectrum`;
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

DROP TABLE IF EXISTS `Workflow`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=88516 ;

-- --------------------------------------------------------

--
-- Table structure for table `WorkflowDehydration`
--

DROP TABLE IF EXISTS `WorkflowDehydration`;
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

DROP TABLE IF EXISTS `WorkflowMesh`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=114503 ;

-- --------------------------------------------------------

--
-- Table structure for table `WorkflowStep`
--

DROP TABLE IF EXISTS `WorkflowStep`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=114949 ;

-- --------------------------------------------------------

--
-- Table structure for table `WorkflowType`
--

DROP TABLE IF EXISTS `WorkflowType`;
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

DROP TABLE IF EXISTS `XFEFluorescenceSpectrum`;
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5226 ;

-- --------------------------------------------------------

--
-- Structure for view `V_AnalysisInfo`
--
DROP TABLE IF EXISTS `V_AnalysisInfo`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `V_AnalysisInfo` AS (select `exp`.`creationDate` AS `experimentCreationDate`,(select `r`.`timeStart` from `Run` `r` where (`r`.`runId` = `m`.`runId`)) AS `timeStart`,`dc`.`dataCollectionId` AS `dataCollectionId`,`m`.`measurementId` AS `measurementId`,`p`.`proposalId` AS `proposalId`,`p`.`proposalCode` AS `proposalCode`,`p`.`proposalNumber` AS `proposalNumber`,`m`.`priorityLevelId` AS `priorityLevelId`,`m`.`code` AS `code`,`m`.`exposureTemperature` AS `exposureTemperature`,`m`.`transmission` AS `transmission`,`m`.`comments` AS `measurementComments`,`exp`.`comments` AS `experimentComments`,`exp`.`experimentId` AS `experimentId`,`exp`.`experimentType` AS `experimentType`,`s`.`concentration` AS `conc`,`bu`.`acronym` AS `bufferAcronym`,`ma`.`acronym` AS `macromoleculeAcronym`,`bu`.`bufferId` AS `bufferId`,`ma`.`macromoleculeId` AS `macromoleculeId`,(select `su`.`substractedFilePath` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `subtractedFilePath`,(select `su`.`rgGuinier` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `rgGuinier`,(select `su`.`firstPointUsed` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `firstPointUsed`,(select `su`.`lastPointUsed` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `lastPointUsed`,(select distinct `su`.`I0` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `I0`,(select distinct `su`.`isagregated` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `isagregated`,(select distinct `su`.`subtractionId` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `subtractionId`,(select distinct `su`.`rgGnom` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `rgGnom`,(select distinct `su`.`total` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `total`,(select distinct `su`.`dmax` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `dmax`,(select distinct `su`.`volume` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `volume`,(select `su`.`I0Stdev` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `i0stdev`,(select `su`.`quality` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `quality`,(select `su`.`creationTime` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `substractionCreationTime`,(select `mtd2`.`measurementId` from `MeasurementToDataCollection` `mtd2` where ((`mtd`.`dataCollectionId` = `mtd2`.`dataCollectionId`) and (`mtd2`.`dataCollectionOrder` = 1))) AS `bufferBeforeMeasurementId`,(select `mtd2`.`measurementId` from `MeasurementToDataCollection` `mtd2` where ((`mtd`.`dataCollectionId` = `mtd2`.`dataCollectionId`) and (`mtd2`.`dataCollectionOrder` = 3))) AS `bufferAfterMeasurementId`,(select `m2`.`framesMerge` from `Merge` `m2` where ((`m2`.`measurementId` = `bufferBeforeMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `bufferBeforeMeasurementId`)))) AS `bufferBeforeFramesMerged`,(select `m2`.`mergeId` from `Merge` `m2` where ((`m2`.`measurementId` = `bufferBeforeMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `bufferBeforeMeasurementId`)))) AS `bufferBeforeMergeId`,(select `m2`.`averageFilePath` from `Merge` `m2` where ((`m2`.`measurementId` = `bufferBeforeMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `bufferBeforeMeasurementId`)))) AS `bufferBeforeAverageFilePath`,(select `mtd2`.`measurementId` from `MeasurementToDataCollection` `mtd2` where ((`mtd`.`dataCollectionId` = `mtd2`.`dataCollectionId`) and (`mtd2`.`dataCollectionOrder` = 2))) AS `sampleMeasurementId`,(select `m2`.`mergeId` from `Merge` `m2` where ((`m2`.`measurementId` = `sampleMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `sampleMeasurementId`)))) AS `sampleMergeId`,(select `m2`.`averageFilePath` from `Merge` `m2` where ((`m2`.`measurementId` = `sampleMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `sampleMeasurementId`)))) AS `averageFilePath`,(select `m2`.`framesMerge` from `Merge` `m2` where ((`m2`.`measurementId` = `sampleMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `sampleMeasurementId`)))) AS `framesMerge`,(select `m2`.`framesCount` from `Merge` `m2` where ((`m2`.`measurementId` = `bufferBeforeMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `bufferBeforeMeasurementId`)))) AS `framesCount`,(select `m2`.`framesMerge` from `Merge` `m2` where ((`m2`.`measurementId` = `bufferAfterMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `bufferAfterMeasurementId`)))) AS `bufferAfterFramesMerged`,(select `m2`.`mergeId` from `Merge` `m2` where ((`m2`.`measurementId` = `bufferAfterMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `bufferAfterMeasurementId`)))) AS `bufferAfterMergeId`,(select `m2`.`averageFilePath` from `Merge` `m2` where ((`m2`.`measurementId` = `bufferAfterMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `bufferAfterMeasurementId`)))) AS `bufferAfterAverageFilePath`,(select `modelList`.`modelListId` from (((`ModelList` `modelList` join `AbInitioModel` `ab`) join `SubtractionToAbInitioModel` `subToAb`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`modelList`.`modelListId` = `ab`.`modelListId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and `subToAb`.`subtractionId` and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `modelListId1`,(select `modelList`.`nsdFilePath` from (((`ModelList` `modelList` join `AbInitioModel` `ab`) join `SubtractionToAbInitioModel` `subToAb`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`modelList`.`modelListId` = `ab`.`modelListId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and `subToAb`.`subtractionId` and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `nsdFilePath`,(select `modelList`.`modelListId` from (((`ModelList` `modelList` join `AbInitioModel` `ab`) join `SubtractionToAbInitioModel` `subToAb`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`modelList`.`modelListId` = `ab`.`modelListId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and `subToAb`.`subtractionId` and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `modelListId2`,(select `modelList`.`chi2RgFilePath` from (((`ModelList` `modelList` join `AbInitioModel` `ab`) join `SubtractionToAbInitioModel` `subToAb`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`modelList`.`modelListId` = `ab`.`modelListId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `chi2RgFilePath`,(select `model`.`pdbFile` from (((`AbInitioModel` `ab` join `SubtractionToAbInitioModel` `subToAb`) join `Model` `model`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`model`.`modelId` = `ab`.`averagedModelId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `averagedModel`,(select `model`.`modelId` from (((`AbInitioModel` `ab` join `SubtractionToAbInitioModel` `subToAb`) join `Model` `model`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`model`.`modelId` = `ab`.`averagedModelId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `averagedModelId`,(select `model`.`pdbFile` from (((`AbInitioModel` `ab` join `SubtractionToAbInitioModel` `subToAb`) join `Model` `model`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`model`.`modelId` = `ab`.`rapidShapeDeterminationModelId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `rapidShapeDeterminationModel`,(select `model`.`modelId` from (((`AbInitioModel` `ab` join `SubtractionToAbInitioModel` `subToAb`) join `Model` `model`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`model`.`modelId` = `ab`.`rapidShapeDeterminationModelId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `rapidShapeDeterminationModelId`,(select `model`.`pdbFile` from (((`AbInitioModel` `ab` join `SubtractionToAbInitioModel` `subToAb`) join `Model` `model`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`model`.`modelId` = `ab`.`shapeDeterminationModelId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `shapeDeterminationModel`,(select `model`.`modelId` from (((`AbInitioModel` `ab` join `SubtractionToAbInitioModel` `subToAb`) join `Model` `model`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`model`.`modelId` = `ab`.`shapeDeterminationModelId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `shapeDeterminationModelId`,(select `ab`.`abInitioModelId` from ((`AbInitioModel` `ab` join `SubtractionToAbInitioModel` `subToAb`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `abInitioModelId`,(select `ab`.`comments` from ((`AbInitioModel` `ab` join `SubtractionToAbInitioModel` `subToAb`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `comments` from (((((((`Experiment` `exp` join `Buffer` `bu`) join `SaxsDataCollection` `dc`) join `Macromolecule` `ma`) join `MeasurementToDataCollection` `mtd`) join `Specimen` `s`) join `Measurement` `m`) join `Proposal` `p`) where ((`bu`.`bufferId` = `s`.`bufferId`) and (`exp`.`proposalId` = `p`.`proposalId`) and (`m`.`specimenId` = `s`.`specimenId`) and (`dc`.`dataCollectionId` = `mtd`.`dataCollectionId`) and (`mtd`.`measurementId` = `m`.`measurementId`) and (`s`.`macromoleculeId` = `ma`.`macromoleculeId`) and (`s`.`experimentId` = `exp`.`experimentId`) and (`exp`.`experimentType` <> 'TEMPLATE') and `p`.`proposalId` in (select distinct `prop`.`proposalId` from (`Proposal` `prop` join `Experiment` `exper`) where ((`exper`.`proposalId` = `prop`.`proposalId`) and (`prop`.`proposalId` <> 3374)))));

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection`
--
DROP TABLE IF EXISTS `v_datacollection`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection` AS select `DataCollection`.`dataCollectionId` AS `dataCollectionId`,`DataCollection`.`dataCollectionGroupId` AS `dataCollectionGroupId`,`DataCollection`.`strategySubWedgeOrigId` AS `strategySubWedgeOrigId`,`DataCollection`.`detectorId` AS `detectorId`,`DataCollection`.`blSubSampleId` AS `blSubSampleId`,`DataCollection`.`dataCollectionNumber` AS `dataCollectionNumber`,`DataCollection`.`startTime` AS `startTime`,`DataCollection`.`endTime` AS `endTime`,`DataCollection`.`runStatus` AS `runStatus`,`DataCollection`.`axisStart` AS `axisStart`,`DataCollection`.`axisEnd` AS `axisEnd`,`DataCollection`.`axisRange` AS `axisRange`,`DataCollection`.`overlap` AS `overlap`,`DataCollection`.`numberOfImages` AS `numberOfImages`,`DataCollection`.`startImageNumber` AS `startImageNumber`,`DataCollection`.`numberOfPasses` AS `numberOfPasses`,`DataCollection`.`exposureTime` AS `exposureTime`,`DataCollection`.`imageDirectory` AS `imageDirectory`,`DataCollection`.`imagePrefix` AS `imagePrefix`,`DataCollection`.`imageSuffix` AS `imageSuffix`,`DataCollection`.`fileTemplate` AS `fileTemplate`,`DataCollection`.`wavelength` AS `wavelength`,`DataCollection`.`resolution` AS `resolution`,`DataCollection`.`detectorDistance` AS `detectorDistance`,`DataCollection`.`xBeam` AS `xBeam`,`DataCollection`.`yBeam` AS `yBeam`,`DataCollection`.`xBeamPix` AS `xBeamPix`,`DataCollection`.`yBeamPix` AS `yBeamPix`,`DataCollection`.`comments` AS `comments`,`DataCollection`.`printableForReport` AS `printableForReport`,`DataCollection`.`slitGapVertical` AS `slitGapVertical`,`DataCollection`.`slitGapHorizontal` AS `slitGapHorizontal`,`DataCollection`.`transmission` AS `transmission`,`DataCollection`.`synchrotronMode` AS `synchrotronMode`,`DataCollection`.`xtalSnapshotFullPath1` AS `xtalSnapshotFullPath1`,`DataCollection`.`xtalSnapshotFullPath2` AS `xtalSnapshotFullPath2`,`DataCollection`.`xtalSnapshotFullPath3` AS `xtalSnapshotFullPath3`,`DataCollection`.`xtalSnapshotFullPath4` AS `xtalSnapshotFullPath4`,`DataCollection`.`rotationAxis` AS `rotationAxis`,`DataCollection`.`phiStart` AS `phiStart`,`DataCollection`.`kappaStart` AS `kappaStart`,`DataCollection`.`omegaStart` AS `omegaStart`,`DataCollection`.`resolutionAtCorner` AS `resolutionAtCorner`,`DataCollection`.`detector2Theta` AS `detector2Theta`,`DataCollection`.`undulatorGap1` AS `undulatorGap1`,`DataCollection`.`undulatorGap2` AS `undulatorGap2`,`DataCollection`.`undulatorGap3` AS `undulatorGap3`,`DataCollection`.`beamSizeAtSampleX` AS `beamSizeAtSampleX`,`DataCollection`.`beamSizeAtSampleY` AS `beamSizeAtSampleY`,`DataCollection`.`centeringMethod` AS `centeringMethod`,`DataCollection`.`averageTemperature` AS `averageTemperature`,`DataCollection`.`actualCenteringPosition` AS `actualCenteringPosition`,`DataCollection`.`beamShape` AS `beamShape`,`DataCollection`.`flux` AS `flux`,`DataCollection`.`flux_end` AS `flux_end`,`DataCollection`.`totalAbsorbedDose` AS `totalAbsorbedDose`,`DataCollection`.`bestWilsonPlotPath` AS `bestWilsonPlotPath`,`DataCollection`.`imageQualityIndicatorsPlotPath` AS `imageQualityIndicatorsPlotPath`,`DataCollection`.`imageQualityIndicatorsCSVPath` AS `imageQualityIndicatorsCSVPath`,`BLSession`.`sessionId` AS `sessionId`,`BLSession`.`proposalId` AS `proposalId`,`DataCollectionGroup`.`workflowId` AS `workflowId`,`v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_dataCollectionId` AS `AutoProcIntegration_dataCollectionId`,`v_datacollection_summary_autoprocintegration`.`autoProcScalingId` AS `autoProcScalingId`,`v_datacollection_summary_autoprocintegration`.`cell_a` AS `cell_a`,`v_datacollection_summary_autoprocintegration`.`cell_b` AS `cell_b`,`v_datacollection_summary_autoprocintegration`.`cell_c` AS `cell_c`,`v_datacollection_summary_autoprocintegration`.`cell_alpha` AS `cell_alpha`,`v_datacollection_summary_autoprocintegration`.`cell_beta` AS `cell_beta`,`v_datacollection_summary_autoprocintegration`.`cell_gamma` AS `cell_gamma`,`v_datacollection_summary_autoprocintegration`.`anomalous` AS `anomalous`,`v_datacollection_summary_autoprocintegration`.`scalingStatisticsType` AS `scalingStatisticsType`,`v_datacollection_summary_autoprocintegration`.`resolutionLimitHigh` AS `resolutionLimitHigh`,`v_datacollection_summary_autoprocintegration`.`resolutionLimitLow` AS `resolutionLimitLow`,`v_datacollection_summary_autoprocintegration`.`completeness` AS `completeness`,`v_datacollection_summary_autoprocintegration`.`AutoProc_spaceGroup` AS `AutoProc_spaceGroup`,`v_datacollection_summary_autoprocintegration`.`autoProcId` AS `autoProcId`,`v_datacollection_summary_autoprocintegration`.`rMerge` AS `rMerge`,`v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`,`v_datacollection_summary_autoprocintegration`.`v_datacollection_summary_autoprocintegration_processingPrograms` AS `AutoProcProgram_processingPrograms`,`v_datacollection_summary_autoprocintegration`.`v_datacollection_summary_autoprocintegration_processingStatus` AS `AutoProcProgram_processingStatus`,`v_datacollection_summary_autoprocintegration`.`AutoProcProgram_autoProcProgramId` AS `AutoProcProgram_autoProcProgramId` from ((((`DataCollection` left join `DataCollectionGroup` on((`DataCollectionGroup`.`dataCollectionGroupId` = `DataCollection`.`dataCollectionGroupId`))) left join `Workflow` on((`DataCollectionGroup`.`workflowId` = `Workflow`.`workflowId`))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`))) left join `v_datacollection_summary_autoprocintegration` on((`v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_dataCollectionId` = `DataCollection`.`dataCollectionId`)));

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_autoprocintegration`
--
DROP TABLE IF EXISTS `v_datacollection_autoprocintegration`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection_autoprocintegration` AS select `AutoProcIntegration`.`autoProcIntegrationId` AS `v_datacollection_summary_phasing_autoProcIntegrationId`,`AutoProcIntegration`.`dataCollectionId` AS `v_datacollection_summary_phasing_dataCollectionId`,`AutoProcIntegration`.`cell_a` AS `v_datacollection_summary_phasing_cell_a`,`AutoProcIntegration`.`cell_b` AS `v_datacollection_summary_phasing_cell_b`,`AutoProcIntegration`.`cell_c` AS `v_datacollection_summary_phasing_cell_c`,`AutoProcIntegration`.`cell_alpha` AS `v_datacollection_summary_phasing_cell_alpha`,`AutoProcIntegration`.`cell_beta` AS `v_datacollection_summary_phasing_cell_beta`,`AutoProcIntegration`.`cell_gamma` AS `v_datacollection_summary_phasing_cell_gamma`,`AutoProcIntegration`.`anomalous` AS `v_datacollection_summary_phasing_anomalous`,`AutoProc`.`spaceGroup` AS `v_datacollection_summary_phasing_autoproc_space_group`,`AutoProc`.`autoProcId` AS `v_datacollection_summary_phasing_autoproc_autoprocId`,`AutoProcScaling`.`autoProcScalingId` AS `v_datacollection_summary_phasing_autoProcScalingId`,`AutoProcProgram`.`processingPrograms` AS `v_datacollection_processingPrograms`,`AutoProcProgram`.`autoProcProgramId` AS `v_datacollection_summary_phasing_autoProcProgramId`,`AutoProcProgram`.`processingStatus` AS `v_datacollection_processingStatus`,`BLSession`.`sessionId` AS `v_datacollection_summary_session_sessionId`,`BLSession`.`proposalId` AS `v_datacollection_summary_session_proposalId`,`AutoProcIntegration`.`dataCollectionId` AS `AutoProcIntegration_dataCollectionId`,`AutoProcIntegration`.`autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`,`PhasingStep`.`phasingStepType` AS `PhasingStep_phasing_phasingStepType`,`SpaceGroup`.`spaceGroupShortName` AS `SpaceGroup_spaceGroupShortName` from ((((((((((`AutoProcIntegration` left join `DataCollection` on((`DataCollection`.`dataCollectionId` = `AutoProcIntegration`.`dataCollectionId`))) left join `DataCollectionGroup` on((`DataCollection`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`))) left join `AutoProcScaling_has_Int` on((`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`))) left join `AutoProcScaling` on((`AutoProcScaling_has_Int`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `AutoProcProgram` on((`AutoProcProgram`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`))) left join `Phasing_has_Scaling` on((`Phasing_has_Scaling`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `PhasingStep` on((`PhasingStep`.`autoProcScalingId` = `Phasing_has_Scaling`.`autoProcScalingId`))) left join `SpaceGroup` on((`SpaceGroup`.`spaceGroupId` = `PhasingStep`.`spaceGroupId`))) left join `AutoProc` on((`AutoProc`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`)));

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_phasing`
--
DROP TABLE IF EXISTS `v_datacollection_phasing`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection_phasing` AS select `PhasingStep`.`phasingStepId` AS `phasingStepId`,`PhasingStep`.`previousPhasingStepId` AS `previousPhasingStepId`,`PhasingStep`.`phasingAnalysisId` AS `phasingAnalysisId`,`AutoProcIntegration`.`autoProcIntegrationId` AS `autoProcIntegrationId`,`AutoProcIntegration`.`dataCollectionId` AS `dataCollectionId`,`AutoProcIntegration`.`anomalous` AS `anomalous`,`AutoProc`.`spaceGroup` AS `spaceGroup`,`AutoProc`.`autoProcId` AS `autoProcId`,`PhasingStep`.`phasingStepType` AS `phasingStepType`,`PhasingStep`.`method` AS `method`,`PhasingStep`.`solventContent` AS `solventContent`,`PhasingStep`.`enantiomorph` AS `enantiomorph`,`PhasingStep`.`lowRes` AS `lowRes`,`PhasingStep`.`highRes` AS `highRes`,`AutoProcScaling`.`autoProcScalingId` AS `autoProcScalingId`,`SpaceGroup`.`spaceGroupShortName` AS `spaceGroupShortName`,`AutoProcProgram`.`processingPrograms` AS `processingPrograms`,`AutoProcProgram`.`processingStatus` AS `processingStatus`,`PhasingProgramRun`.`phasingPrograms` AS `phasingPrograms`,`PhasingProgramRun`.`phasingStatus` AS `phasingStatus`,`PhasingProgramRun`.`phasingStartTime` AS `phasingStartTime`,`PhasingProgramRun`.`phasingEndTime` AS `phasingEndTime`,`DataCollectionGroup`.`sessionId` AS `sessionId`,`BLSession`.`proposalId` AS `proposalId`,`BLSample`.`blSampleId` AS `blSampleId`,`BLSample`.`name` AS `name`,`BLSample`.`code` AS `code`,`Protein`.`acronym` AS `acronym`,`Protein`.`proteinId` AS `proteinId` from (((((((((((((`AutoProcIntegration` left join `AutoProcScaling_has_Int` on((`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`))) left join `AutoProcScaling` on((`AutoProcScaling_has_Int`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `AutoProcProgram` on((`AutoProcProgram`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`))) left join `AutoProc` on((`AutoProc`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`))) left join `PhasingStep` on((`PhasingStep`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `PhasingProgramRun` on((`PhasingProgramRun`.`phasingProgramRunId` = `PhasingStep`.`programRunId`))) left join `DataCollection` on((`DataCollection`.`dataCollectionId` = `AutoProcIntegration`.`dataCollectionId`))) left join `DataCollectionGroup` on((`DataCollectionGroup`.`dataCollectionGroupId` = `DataCollection`.`dataCollectionGroupId`))) left join `BLSample` on((`BLSample`.`blSampleId` = `DataCollectionGroup`.`blSampleId`))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`))) left join `Crystal` on((`Crystal`.`crystalId` = `BLSample`.`crystalId`))) left join `Protein` on((`Crystal`.`proteinId` = `Protein`.`proteinId`))) left join `SpaceGroup` on((`SpaceGroup`.`spaceGroupId` = `PhasingStep`.`spaceGroupId`)));

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_phasing_program_run`
--
DROP TABLE IF EXISTS `v_datacollection_phasing_program_run`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection_phasing_program_run` AS select `PhasingStep`.`phasingStepId` AS `phasingStepId`,`PhasingStep`.`previousPhasingStepId` AS `previousPhasingStepId`,`PhasingStep`.`phasingAnalysisId` AS `phasingAnalysisId`,`AutoProcIntegration`.`autoProcIntegrationId` AS `autoProcIntegrationId`,`AutoProcIntegration`.`dataCollectionId` AS `dataCollectionId`,`AutoProc`.`autoProcId` AS `autoProcId`,`PhasingStep`.`phasingStepType` AS `phasingStepType`,`PhasingStep`.`method` AS `method`,`AutoProcScaling`.`autoProcScalingId` AS `autoProcScalingId`,`SpaceGroup`.`spaceGroupShortName` AS `spaceGroupShortName`,`PhasingProgramRun`.`phasingPrograms` AS `phasingPrograms`,`PhasingProgramRun`.`phasingStatus` AS `phasingStatus`,`DataCollectionGroup`.`sessionId` AS `sessionId`,`BLSession`.`proposalId` AS `proposalId`,`BLSample`.`blSampleId` AS `blSampleId`,`BLSample`.`name` AS `name`,`BLSample`.`code` AS `code`,`Protein`.`acronym` AS `acronym`,`Protein`.`proteinId` AS `proteinId`,`PhasingProgramAttachment`.`phasingProgramAttachmentId` AS `phasingProgramAttachmentId`,`PhasingProgramAttachment`.`fileType` AS `fileType`,`PhasingProgramAttachment`.`fileName` AS `fileName`,`PhasingProgramAttachment`.`filePath` AS `filePath` from ((((((((((((((`AutoProcIntegration` left join `AutoProcScaling_has_Int` on((`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`))) left join `AutoProcScaling` on((`AutoProcScaling_has_Int`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `AutoProcProgram` on((`AutoProcProgram`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`))) left join `AutoProc` on((`AutoProc`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`))) left join `PhasingStep` on((`PhasingStep`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `PhasingProgramRun` on((`PhasingProgramRun`.`phasingProgramRunId` = `PhasingStep`.`programRunId`))) left join `DataCollection` on((`DataCollection`.`dataCollectionId` = `AutoProcIntegration`.`dataCollectionId`))) left join `DataCollectionGroup` on((`DataCollectionGroup`.`dataCollectionGroupId` = `DataCollection`.`dataCollectionGroupId`))) left join `BLSample` on((`BLSample`.`blSampleId` = `DataCollectionGroup`.`blSampleId`))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`))) left join `Crystal` on((`Crystal`.`crystalId` = `BLSample`.`crystalId`))) left join `Protein` on((`Crystal`.`proteinId` = `Protein`.`proteinId`))) left join `PhasingProgramAttachment` on((`PhasingProgramAttachment`.`phasingProgramRunId` = `PhasingProgramRun`.`phasingProgramRunId`))) left join `SpaceGroup` on((`SpaceGroup`.`spaceGroupId` = `PhasingStep`.`spaceGroupId`)));

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_summary`
--
DROP TABLE IF EXISTS `v_datacollection_summary`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection_summary` AS select `DataCollectionGroup`.`dataCollectionGroupId` AS `DataCollectionGroup_dataCollectionGroupId`,`DataCollectionGroup`.`blSampleId` AS `DataCollectionGroup_blSampleId`,`DataCollectionGroup`.`sessionId` AS `DataCollectionGroup_sessionId`,`DataCollectionGroup`.`workflowId` AS `DataCollectionGroup_workflowId`,`DataCollectionGroup`.`experimentType` AS `DataCollectionGroup_experimentType`,`DataCollectionGroup`.`startTime` AS `DataCollectionGroup_startTime`,`DataCollectionGroup`.`endTime` AS `DataCollectionGroup_endTime`,`DataCollectionGroup`.`comments` AS `DataCollectionGroup_comments`,`DataCollectionGroup`.`actualSampleBarcode` AS `DataCollectionGroup_actualSampleBarcode`,`DataCollectionGroup`.`xtalSnapshotFullPath` AS `DataCollectionGroup_xtalSnapshotFullPath`,`BLSample`.`blSampleId` AS `BLSample_blSampleId`,`BLSample`.`crystalId` AS `BLSample_crystalId`,`BLSample`.`name` AS `BLSample_name`,`BLSample`.`code` AS `BLSample_code`,`BLSample`.`location` AS `BLSample_location`,`BLSample`.`blSampleStatus` AS `BLSample_blSampleStatus`,`BLSample`.`comments` AS `BLSample_comments`,`Container`.`containerId` AS `Container_containerId`,`BLSession`.`sessionId` AS `BLSession_sessionId`,`BLSession`.`proposalId` AS `BLSession_proposalId`,`BLSession`.`protectedData` AS `BLSession_protectedData`,`Dewar`.`dewarId` AS `Dewar_dewarId`,`Dewar`.`code` AS `Dewar_code`,`Dewar`.`storageLocation` AS `Dewar_storageLocation`,`Container`.`containerType` AS `Container_containerType`,`Container`.`code` AS `Container_code`,`Container`.`capacity` AS `Container_capacity`,`Container`.`beamlineLocation` AS `Container_beamlineLocation`,`Container`.`sampleChangerLocation` AS `Container_sampleChangerLocation`,`Protein`.`proteinId` AS `Protein_proteinId`,`Protein`.`name` AS `Protein_name`,`Protein`.`acronym` AS `Protein_acronym`,`DataCollection`.`dataCollectionId` AS `DataCollection_dataCollectionId`,`DataCollection`.`dataCollectionGroupId` AS `DataCollection_dataCollectionGroupId`,`DataCollection`.`startTime` AS `DataCollection_startTime`,`DataCollection`.`endTime` AS `DataCollection_endTime`,`DataCollection`.`runStatus` AS `DataCollection_runStatus`,`DataCollection`.`numberOfImages` AS `DataCollection_numberOfImages`,`DataCollection`.`startImageNumber` AS `DataCollection_startImageNumber`,`DataCollection`.`numberOfPasses` AS `DataCollection_numberOfPasses`,`DataCollection`.`exposureTime` AS `DataCollection_exposureTime`,`DataCollection`.`imageDirectory` AS `DataCollection_imageDirectory`,`DataCollection`.`wavelength` AS `DataCollection_wavelength`,`DataCollection`.`resolution` AS `DataCollection_resolution`,`DataCollection`.`detectorDistance` AS `DataCollection_detectorDistance`,`DataCollection`.`xBeam` AS `DataCollection_xBeam`,`DataCollection`.`transmission` AS `transmission`,`DataCollection`.`yBeam` AS `DataCollection_yBeam`,`DataCollection`.`imagePrefix` AS `DataCollection_imagePrefix`,`DataCollection`.`comments` AS `DataCollection_comments`,`DataCollection`.`xtalSnapshotFullPath1` AS `DataCollection_xtalSnapshotFullPath1`,`DataCollection`.`xtalSnapshotFullPath2` AS `DataCollection_xtalSnapshotFullPath2`,`DataCollection`.`xtalSnapshotFullPath3` AS `DataCollection_xtalSnapshotFullPath3`,`DataCollection`.`xtalSnapshotFullPath4` AS `DataCollection_xtalSnapshotFullPath4`,`DataCollection`.`phiStart` AS `DataCollection_phiStart`,`DataCollection`.`kappaStart` AS `DataCollection_kappaStart`,`DataCollection`.`omegaStart` AS `DataCollection_omegaStart`,`DataCollection`.`flux` AS `DataCollection_flux`,`DataCollection`.`flux_end` AS `DataCollection_flux_end`,`DataCollection`.`resolutionAtCorner` AS `DataCollection_resolutionAtCorner`,`DataCollection`.`bestWilsonPlotPath` AS `DataCollection_bestWilsonPlotPath`,`DataCollection`.`dataCollectionNumber` AS `DataCollection_dataCollectionNumber`,`DataCollection`.`axisRange` AS `DataCollection_axisRange`,`DataCollection`.`axisStart` AS `DataCollection_axisStart`,`DataCollection`.`axisEnd` AS `DataCollection_axisEnd`,`DataCollection`.`rotationAxis` AS `DataCollection_rotationAxis`,`DataCollection`.`undulatorGap1` AS `DataCollection_undulatorGap1`,`DataCollection`.`undulatorGap2` AS `DataCollection_undulatorGap2`,`DataCollection`.`undulatorGap3` AS `DataCollection_undulatorGap3`,`DataCollection`.`beamSizeAtSampleX` AS `beamSizeAtSampleX`,`DataCollection`.`beamSizeAtSampleY` AS `beamSizeAtSampleY`,`DataCollection`.`slitGapVertical` AS `DataCollection_slitGapVertical`,`DataCollection`.`slitGapHorizontal` AS `DataCollection_slitGapHorizontal`,`DataCollection`.`beamShape` AS `DataCollection_beamShape`,`Workflow`.`workflowTitle` AS `Workflow_workflowTitle`,`Workflow`.`workflowType` AS `Workflow_workflowType`,`Workflow`.`status` AS `Workflow_status`,`Workflow`.`workflowId` AS `Workflow_workflowId`,`v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_dataCollectionId` AS `AutoProcIntegration_dataCollectionId`,`v_datacollection_summary_autoprocintegration`.`autoProcScalingId` AS `autoProcScalingId`,`v_datacollection_summary_autoprocintegration`.`cell_a` AS `cell_a`,`v_datacollection_summary_autoprocintegration`.`cell_b` AS `cell_b`,`v_datacollection_summary_autoprocintegration`.`cell_c` AS `cell_c`,`v_datacollection_summary_autoprocintegration`.`cell_alpha` AS `cell_alpha`,`v_datacollection_summary_autoprocintegration`.`cell_beta` AS `cell_beta`,`v_datacollection_summary_autoprocintegration`.`cell_gamma` AS `cell_gamma`,`v_datacollection_summary_autoprocintegration`.`anomalous` AS `anomalous`,`v_datacollection_summary_autoprocintegration`.`scalingStatisticsType` AS `scalingStatisticsType`,`v_datacollection_summary_autoprocintegration`.`resolutionLimitHigh` AS `resolutionLimitHigh`,`v_datacollection_summary_autoprocintegration`.`resolutionLimitLow` AS `resolutionLimitLow`,`v_datacollection_summary_autoprocintegration`.`completeness` AS `completeness`,`v_datacollection_summary_autoprocintegration`.`AutoProc_spaceGroup` AS `AutoProc_spaceGroup`,`v_datacollection_summary_autoprocintegration`.`autoProcId` AS `autoProcId`,`v_datacollection_summary_autoprocintegration`.`rMerge` AS `rMerge`,`v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`,`v_datacollection_summary_autoprocintegration`.`v_datacollection_summary_autoprocintegration_processingPrograms` AS `AutoProcProgram_processingPrograms`,`v_datacollection_summary_autoprocintegration`.`v_datacollection_summary_autoprocintegration_processingStatus` AS `AutoProcProgram_processingStatus`,`v_datacollection_summary_autoprocintegration`.`AutoProcProgram_autoProcProgramId` AS `AutoProcProgram_autoProcProgramId`,`v_datacollection_summary_screening`.`Screening_screeningId` AS `Screening_screeningId`,`v_datacollection_summary_screening`.`Screening_dataCollectionId` AS `Screening_dataCollectionId`,`v_datacollection_summary_screening`.`Screening_dataCollectionGroupId` AS `Screening_dataCollectionGroupId`,`v_datacollection_summary_screening`.`ScreeningOutput_strategySuccess` AS `ScreeningOutput_strategySuccess`,`v_datacollection_summary_screening`.`ScreeningOutput_indexingSuccess` AS `ScreeningOutput_indexingSuccess`,`v_datacollection_summary_screening`.`ScreeningOutput_rankingResolution` AS `ScreeningOutput_rankingResolution`,`v_datacollection_summary_screening`.`ScreeningOutput_mosaicity` AS `ScreeningOutput_mosaicity`,`v_datacollection_summary_screening`.`ScreeningOutputLattice_spaceGroup` AS `ScreeningOutputLattice_spaceGroup`,`v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_a` AS `ScreeningOutputLattice_unitCell_a`,`v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_b` AS `ScreeningOutputLattice_unitCell_b`,`v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_c` AS `ScreeningOutputLattice_unitCell_c`,`v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_alpha` AS `ScreeningOutputLattice_unitCell_alpha`,`v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_beta` AS `ScreeningOutputLattice_unitCell_beta`,`v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_gamma` AS `ScreeningOutputLattice_unitCell_gamma`,`v_datacollection_summary_screening`.`ScreeningOutput_totalExposureTime` AS `ScreeningOutput_totalExposureTime`,`v_datacollection_summary_screening`.`ScreeningOutput_totalRotationRange` AS `ScreeningOutput_totalRotationRange`,`v_datacollection_summary_screening`.`ScreeningOutput_totalNumberOfImages` AS `ScreeningOutput_totalNumberOfImages`,`v_datacollection_summary_screening`.`ScreeningStrategySubWedge_exposureTime` AS `ScreeningStrategySubWedge_exposureTime`,`v_datacollection_summary_screening`.`ScreeningStrategySubWedge_transmission` AS `ScreeningStrategySubWedge_transmission`,`v_datacollection_summary_screening`.`ScreeningStrategySubWedge_oscillationRange` AS `ScreeningStrategySubWedge_oscillationRange`,`v_datacollection_summary_screening`.`ScreeningStrategySubWedge_numberOfImages` AS `ScreeningStrategySubWedge_numberOfImages`,`v_datacollection_summary_screening`.`ScreeningStrategySubWedge_multiplicity` AS `ScreeningStrategySubWedge_multiplicity`,`v_datacollection_summary_screening`.`ScreeningStrategySubWedge_completeness` AS `ScreeningStrategySubWedge_completeness`,`Shipping`.`shippingId` AS `Shipping_shippingId`,`Shipping`.`shippingName` AS `Shipping_shippingName`,`Shipping`.`shippingStatus` AS `Shipping_shippingStatus`,`DiffractionPlan`.`diffractionPlanId` AS `diffractionPlanId`,`DiffractionPlan`.`experimentKind` AS `experimentKind`,`DiffractionPlan`.`observedResolution` AS `observedResolution`,`DiffractionPlan`.`minimalResolution` AS `minimalResolution`,`DiffractionPlan`.`exposureTime` AS `exposureTime`,`DiffractionPlan`.`oscillationRange` AS `oscillationRange`,`DiffractionPlan`.`maximalResolution` AS `maximalResolution`,`DiffractionPlan`.`screeningResolution` AS `screeningResolution`,`DiffractionPlan`.`radiationSensitivity` AS `radiationSensitivity`,`DiffractionPlan`.`anomalousScatterer` AS `anomalousScatterer`,`DiffractionPlan`.`preferredBeamSizeX` AS `preferredBeamSizeX`,`DiffractionPlan`.`preferredBeamSizeY` AS `preferredBeamSizeY`,`DiffractionPlan`.`preferredBeamDiameter` AS `preferredBeamDiameter`,`DiffractionPlan`.`comments` AS `DiffractipnPlan_comments`,`DiffractionPlan`.`aimedCompleteness` AS `aimedCompleteness`,`DiffractionPlan`.`aimedIOverSigmaAtHighestRes` AS `aimedIOverSigmaAtHighestRes`,`DiffractionPlan`.`aimedMultiplicity` AS `aimedMultiplicity`,`DiffractionPlan`.`aimedResolution` AS `aimedResolution`,`DiffractionPlan`.`anomalousData` AS `anomalousData`,`DiffractionPlan`.`complexity` AS `complexity`,`DiffractionPlan`.`estimateRadiationDamage` AS `estimateRadiationDamage`,`DiffractionPlan`.`forcedSpaceGroup` AS `forcedSpaceGroup`,`DiffractionPlan`.`requiredCompleteness` AS `requiredCompleteness`,`DiffractionPlan`.`requiredMultiplicity` AS `requiredMultiplicity`,`DiffractionPlan`.`requiredResolution` AS `requiredResolution`,`DiffractionPlan`.`strategyOption` AS `strategyOption`,`DiffractionPlan`.`kappaStrategyOption` AS `kappaStrategyOption`,`DiffractionPlan`.`numberOfPositions` AS `numberOfPositions`,`DiffractionPlan`.`minDimAccrossSpindleAxis` AS `minDimAccrossSpindleAxis`,`DiffractionPlan`.`maxDimAccrossSpindleAxis` AS `maxDimAccrossSpindleAxis`,`DiffractionPlan`.`radiationSensitivityBeta` AS `radiationSensitivityBeta`,`DiffractionPlan`.`radiationSensitivityGamma` AS `radiationSensitivityGamma`,`DiffractionPlan`.`minOscWidth` AS `minOscWidth`,`Detector`.`detectorType` AS `Detector_detectorType`,`Detector`.`detectorManufacturer` AS `Detector_detectorManufacturer`,`Detector`.`detectorModel` AS `Detector_detectorModel`,`Detector`.`detectorPixelSizeHorizontal` AS `Detector_detectorPixelSizeHorizontal`,`Detector`.`detectorPixelSizeVertical` AS `Detector_detectorPixelSizeVertical`,`Detector`.`detectorSerialNumber` AS `Detector_detectorSerialNumber`,`Detector`.`detectorDistanceMin` AS `Detector_detectorDistanceMin`,`Detector`.`detectorDistanceMax` AS `Detector_detectorDistanceMax`,`Detector`.`trustedPixelValueRangeLower` AS `Detector_trustedPixelValueRangeLower`,`Detector`.`trustedPixelValueRangeUpper` AS `Detector_trustedPixelValueRangeUpper`,`Detector`.`sensorThickness` AS `Detector_sensorThickness`,`Detector`.`overload` AS `Detector_overload`,`Detector`.`XGeoCorr` AS `Detector_XGeoCorr`,`Detector`.`YGeoCorr` AS `Detector_YGeoCorr`,`Detector`.`detectorMode` AS `Detector_detectorMode`,`BeamLineSetup`.`undulatorType1` AS `BeamLineSetup_undulatorType1`,`BeamLineSetup`.`undulatorType2` AS `BeamLineSetup_undulatorType2`,`BeamLineSetup`.`undulatorType3` AS `BeamLineSetup_undulatorType3`,`BeamLineSetup`.`synchrotronName` AS `BeamLineSetup_synchrotronName`,`BeamLineSetup`.`synchrotronMode` AS `BeamLineSetup_synchrotronMode`,`BeamLineSetup`.`polarisation` AS `BeamLineSetup_polarisation`,`BeamLineSetup`.`focusingOptic` AS `BeamLineSetup_focusingOptic`,`BeamLineSetup`.`beamDivergenceHorizontal` AS `BeamLineSetup_beamDivergenceHorizontal`,`BeamLineSetup`.`beamDivergenceVertical` AS `BeamLineSetup_beamDivergenceVertical`,`BeamLineSetup`.`monochromatorType` AS `BeamLineSetup_monochromatorType` from ((((((((((((((`DataCollectionGroup` left join `DataCollection` on(((`DataCollection`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`) and (`DataCollection`.`dataCollectionId` = (select max(`dc2`.`dataCollectionId`) from `DataCollection` `dc2` where (`dc2`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`)))))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`))) left join `BeamLineSetup` on((`BeamLineSetup`.`beamLineSetupId` = `BLSession`.`beamLineSetupId`))) left join `Detector` on((`Detector`.`detectorId` = `DataCollection`.`detectorId`))) left join `BLSample` on((`BLSample`.`blSampleId` = `DataCollectionGroup`.`blSampleId`))) left join `DiffractionPlan` on((`DiffractionPlan`.`diffractionPlanId` = `BLSample`.`diffractionPlanId`))) left join `Container` on((`Container`.`containerId` = `BLSample`.`containerId`))) left join `Dewar` on((`Container`.`dewarId` = `Dewar`.`dewarId`))) left join `Shipping` on((`Shipping`.`shippingId` = `Dewar`.`shippingId`))) left join `Crystal` on((`Crystal`.`crystalId` = `BLSample`.`crystalId`))) left join `Protein` on((`Protein`.`proteinId` = `Crystal`.`proteinId`))) left join `Workflow` on((`DataCollectionGroup`.`workflowId` = `Workflow`.`workflowId`))) left join `v_datacollection_summary_autoprocintegration` on((`v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_dataCollectionId` = `DataCollection`.`dataCollectionId`))) left join `v_datacollection_summary_screening` on((`v_datacollection_summary_screening`.`Screening_dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`)));

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_summary_autoprocintegration`
--
DROP TABLE IF EXISTS `v_datacollection_summary_autoprocintegration`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection_summary_autoprocintegration` AS select `AutoProcIntegration`.`dataCollectionId` AS `AutoProcIntegration_dataCollectionId`,`AutoProcIntegration`.`cell_a` AS `cell_a`,`AutoProcIntegration`.`cell_b` AS `cell_b`,`AutoProcIntegration`.`cell_c` AS `cell_c`,`AutoProcIntegration`.`cell_alpha` AS `cell_alpha`,`AutoProcIntegration`.`cell_beta` AS `cell_beta`,`AutoProcIntegration`.`cell_gamma` AS `cell_gamma`,`AutoProcIntegration`.`anomalous` AS `anomalous`,`AutoProcIntegration`.`autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`,`AutoProcProgram`.`processingPrograms` AS `v_datacollection_summary_autoprocintegration_processingPrograms`,`AutoProcProgram`.`autoProcProgramId` AS `AutoProcProgram_autoProcProgramId`,`AutoProcProgram`.`processingStatus` AS `v_datacollection_summary_autoprocintegration_processingStatus`,`AutoProcIntegration`.`dataCollectionId` AS `AutoProcIntegration_phasing_dataCollectionId`,`PhasingStep`.`phasingStepType` AS `PhasingStep_phasing_phasingStepType`,`SpaceGroup`.`spaceGroupShortName` AS `SpaceGroup_spaceGroupShortName`,`AutoProc`.`autoProcId` AS `autoProcId`,`AutoProc`.`spaceGroup` AS `AutoProc_spaceGroup`,`AutoProcScalingStatistics`.`scalingStatisticsType` AS `scalingStatisticsType`,`AutoProcScalingStatistics`.`resolutionLimitHigh` AS `resolutionLimitHigh`,`AutoProcScalingStatistics`.`resolutionLimitLow` AS `resolutionLimitLow`,`AutoProcScalingStatistics`.`rMerge` AS `rMerge`,`AutoProcScalingStatistics`.`completeness` AS `completeness`,`AutoProcScaling`.`autoProcScalingId` AS `autoProcScalingId` from ((((((((`AutoProcIntegration` left join `AutoProcProgram` on((`AutoProcIntegration`.`autoProcProgramId` = `AutoProcProgram`.`autoProcProgramId`))) left join `AutoProcScaling_has_Int` on((`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`))) left join `AutoProcScaling` on((`AutoProcScaling`.`autoProcScalingId` = `AutoProcScaling_has_Int`.`autoProcScalingId`))) left join `AutoProcScalingStatistics` on((`AutoProcScaling`.`autoProcScalingId` = `AutoProcScalingStatistics`.`autoProcScalingId`))) left join `AutoProc` on((`AutoProc`.`autoProcId` = `AutoProcScaling`.`autoProcId`))) left join `Phasing_has_Scaling` on((`Phasing_has_Scaling`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `PhasingStep` on((`PhasingStep`.`autoProcScalingId` = `Phasing_has_Scaling`.`autoProcScalingId`))) left join `SpaceGroup` on((`SpaceGroup`.`spaceGroupId` = `PhasingStep`.`spaceGroupId`)));

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_summary_datacollectiongroup`
--
DROP TABLE IF EXISTS `v_datacollection_summary_datacollectiongroup`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection_summary_datacollectiongroup` AS select `DataCollectionGroup`.`dataCollectionGroupId` AS `DataCollectionGroup_dataCollectionGroupId`,`DataCollectionGroup`.`blSampleId` AS `DataCollectionGroup_blSampleId`,`DataCollectionGroup`.`sessionId` AS `DataCollectionGroup_sessionId`,`DataCollectionGroup`.`workflowId` AS `DataCollectionGroup_workflowId`,`DataCollectionGroup`.`experimentType` AS `DataCollectionGroup_experimentType`,`DataCollectionGroup`.`startTime` AS `DataCollectionGroup_startTime`,`DataCollectionGroup`.`endTime` AS `DataCollectionGroup_endTime`,`DataCollectionGroup`.`comments` AS `DataCollectionGroup_comments`,`DataCollectionGroup`.`actualSampleBarcode` AS `DataCollectionGroup_actualSampleBarcode`,`DataCollectionGroup`.`xtalSnapshotFullPath` AS `DataCollectionGroup_xtalSnapshotFullPath`,`BLSample`.`blSampleId` AS `BLSample_blSampleId`,`BLSample`.`crystalId` AS `BLSample_crystalId`,`BLSample`.`name` AS `BLSample_name`,`BLSample`.`code` AS `BLSample_code`,`BLSession`.`sessionId` AS `BLSession_sessionId`,`BLSession`.`proposalId` AS `BLSession_proposalId`,`BLSession`.`protectedData` AS `BLSession_protectedData`,`Protein`.`proteinId` AS `Protein_proteinId`,`Protein`.`name` AS `Protein_name`,`Protein`.`acronym` AS `Protein_acronym`,`DataCollection`.`dataCollectionId` AS `DataCollection_dataCollectionId`,`DataCollection`.`dataCollectionGroupId` AS `DataCollection_dataCollectionGroupId`,`DataCollection`.`startTime` AS `DataCollection_startTime`,`DataCollection`.`endTime` AS `DataCollection_endTime`,`DataCollection`.`runStatus` AS `DataCollection_runStatus`,`DataCollection`.`numberOfImages` AS `DataCollection_numberOfImages`,`DataCollection`.`startImageNumber` AS `DataCollection_startImageNumber`,`DataCollection`.`numberOfPasses` AS `DataCollection_numberOfPasses`,`DataCollection`.`exposureTime` AS `DataCollection_exposureTime`,`DataCollection`.`imageDirectory` AS `DataCollection_imageDirectory`,`DataCollection`.`wavelength` AS `DataCollection_wavelength`,`DataCollection`.`resolution` AS `DataCollection_resolution`,`DataCollection`.`detectorDistance` AS `DataCollection_detectorDistance`,`DataCollection`.`xBeam` AS `DataCollection_xBeam`,`DataCollection`.`yBeam` AS `DataCollection_yBeam`,`DataCollection`.`comments` AS `DataCollection_comments`,`DataCollection`.`xtalSnapshotFullPath1` AS `DataCollection_xtalSnapshotFullPath1`,`DataCollection`.`xtalSnapshotFullPath2` AS `DataCollection_xtalSnapshotFullPath2`,`DataCollection`.`xtalSnapshotFullPath3` AS `DataCollection_xtalSnapshotFullPath3`,`DataCollection`.`xtalSnapshotFullPath4` AS `DataCollection_xtalSnapshotFullPath4`,`DataCollection`.`phiStart` AS `DataCollection_phiStart`,`DataCollection`.`kappaStart` AS `DataCollection_kappaStart`,`DataCollection`.`omegaStart` AS `DataCollection_omegaStart`,`DataCollection`.`resolutionAtCorner` AS `DataCollection_resolutionAtCorner`,`DataCollection`.`bestWilsonPlotPath` AS `DataCollection_bestWilsonPlotPath`,`DataCollection`.`dataCollectionNumber` AS `DataCollection_dataCollectionNumber`,`DataCollection`.`axisRange` AS `DataCollection_axisRange`,`DataCollection`.`axisStart` AS `DataCollection_axisStart`,`DataCollection`.`axisEnd` AS `DataCollection_axisEnd`,`Workflow`.`workflowTitle` AS `Workflow_workflowTitle`,`Workflow`.`workflowType` AS `Workflow_workflowType`,`Workflow`.`status` AS `Workflow_status` from ((((((`DataCollectionGroup` left join `DataCollection` on(((`DataCollection`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`) and (`DataCollection`.`dataCollectionId` = (select max(`dc2`.`dataCollectionId`) from `DataCollection` `dc2` where (`dc2`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`)))))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`))) left join `BLSample` on((`BLSample`.`blSampleId` = `DataCollectionGroup`.`blSampleId`))) left join `Crystal` on((`Crystal`.`crystalId` = `BLSample`.`crystalId`))) left join `Protein` on((`Protein`.`proteinId` = `Crystal`.`proteinId`))) left join `Workflow` on((`DataCollectionGroup`.`workflowId` = `Workflow`.`workflowId`)));

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_summary_phasing`
--
DROP TABLE IF EXISTS `v_datacollection_summary_phasing`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection_summary_phasing` AS select `AutoProcIntegration`.`autoProcIntegrationId` AS `v_datacollection_summary_phasing_autoProcIntegrationId`,`AutoProcIntegration`.`dataCollectionId` AS `v_datacollection_summary_phasing_dataCollectionId`,`AutoProcIntegration`.`cell_a` AS `v_datacollection_summary_phasing_cell_a`,`AutoProcIntegration`.`cell_b` AS `v_datacollection_summary_phasing_cell_b`,`AutoProcIntegration`.`cell_c` AS `v_datacollection_summary_phasing_cell_c`,`AutoProcIntegration`.`cell_alpha` AS `v_datacollection_summary_phasing_cell_alpha`,`AutoProcIntegration`.`cell_beta` AS `v_datacollection_summary_phasing_cell_beta`,`AutoProcIntegration`.`cell_gamma` AS `v_datacollection_summary_phasing_cell_gamma`,`AutoProcIntegration`.`anomalous` AS `v_datacollection_summary_phasing_anomalous`,`AutoProc`.`spaceGroup` AS `v_datacollection_summary_phasing_autoproc_space_group`,`AutoProc`.`autoProcId` AS `v_datacollection_summary_phasing_autoproc_autoprocId`,`AutoProcScaling`.`autoProcScalingId` AS `v_datacollection_summary_phasing_autoProcScalingId`,`AutoProcProgram`.`processingPrograms` AS `v_datacollection_summary_phasing_processingPrograms`,`AutoProcProgram`.`autoProcProgramId` AS `v_datacollection_summary_phasing_autoProcProgramId`,`AutoProcProgram`.`processingStatus` AS `v_datacollection_summary_phasing_processingStatus`,`BLSession`.`sessionId` AS `v_datacollection_summary_session_sessionId`,`BLSession`.`proposalId` AS `v_datacollection_summary_session_proposalId` from (((((((`AutoProcIntegration` left join `DataCollection` on((`DataCollection`.`dataCollectionId` = `AutoProcIntegration`.`dataCollectionId`))) left join `DataCollectionGroup` on((`DataCollection`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`))) left join `AutoProcScaling_has_Int` on((`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`))) left join `AutoProcScaling` on((`AutoProcScaling_has_Int`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `AutoProcProgram` on((`AutoProcProgram`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`))) left join `AutoProc` on((`AutoProc`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`)));

-- --------------------------------------------------------

--
-- Structure for view `v_datacollection_summary_screening`
--
DROP TABLE IF EXISTS `v_datacollection_summary_screening`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection_summary_screening` AS select `Screening`.`screeningId` AS `Screening_screeningId`,`Screening`.`dataCollectionId` AS `Screening_dataCollectionId`,`Screening`.`dataCollectionGroupId` AS `Screening_dataCollectionGroupId`,`ScreeningOutput`.`strategySuccess` AS `ScreeningOutput_strategySuccess`,`ScreeningOutput`.`indexingSuccess` AS `ScreeningOutput_indexingSuccess`,`ScreeningOutput`.`rankingResolution` AS `ScreeningOutput_rankingResolution`,`ScreeningOutput`.`mosaicityEstimated` AS `ScreeningOutput_mosaicityEstimated`,`ScreeningOutput`.`mosaicity` AS `ScreeningOutput_mosaicity`,`ScreeningOutput`.`totalExposureTime` AS `ScreeningOutput_totalExposureTime`,`ScreeningOutput`.`totalRotationRange` AS `ScreeningOutput_totalRotationRange`,`ScreeningOutput`.`totalNumberOfImages` AS `ScreeningOutput_totalNumberOfImages`,`ScreeningOutputLattice`.`spaceGroup` AS `ScreeningOutputLattice_spaceGroup`,`ScreeningOutputLattice`.`unitCell_a` AS `ScreeningOutputLattice_unitCell_a`,`ScreeningOutputLattice`.`unitCell_b` AS `ScreeningOutputLattice_unitCell_b`,`ScreeningOutputLattice`.`unitCell_c` AS `ScreeningOutputLattice_unitCell_c`,`ScreeningOutputLattice`.`unitCell_alpha` AS `ScreeningOutputLattice_unitCell_alpha`,`ScreeningOutputLattice`.`unitCell_beta` AS `ScreeningOutputLattice_unitCell_beta`,`ScreeningOutputLattice`.`unitCell_gamma` AS `ScreeningOutputLattice_unitCell_gamma`,`ScreeningStrategySubWedge`.`exposureTime` AS `ScreeningStrategySubWedge_exposureTime`,`ScreeningStrategySubWedge`.`transmission` AS `ScreeningStrategySubWedge_transmission`,`ScreeningStrategySubWedge`.`oscillationRange` AS `ScreeningStrategySubWedge_oscillationRange`,`ScreeningStrategySubWedge`.`numberOfImages` AS `ScreeningStrategySubWedge_numberOfImages`,`ScreeningStrategySubWedge`.`multiplicity` AS `ScreeningStrategySubWedge_multiplicity`,`ScreeningStrategySubWedge`.`completeness` AS `ScreeningStrategySubWedge_completeness` from (((((`Screening` left join `ScreeningOutput` on((`Screening`.`screeningId` = `ScreeningOutput`.`screeningId`))) left join `ScreeningOutputLattice` on((`ScreeningOutput`.`screeningOutputId` = `ScreeningOutputLattice`.`screeningOutputId`))) left join `ScreeningStrategy` on((`ScreeningStrategy`.`screeningOutputId` = `ScreeningOutput`.`screeningOutputId`))) left join `ScreeningStrategyWedge` on((`ScreeningStrategyWedge`.`screeningStrategyId` = `ScreeningStrategy`.`screeningStrategyId`))) left join `ScreeningStrategySubWedge` on((`ScreeningStrategySubWedge`.`screeningStrategyWedgeId` = `ScreeningStrategyWedge`.`screeningStrategyWedgeId`)));

-- --------------------------------------------------------

--
-- Structure for view `v_dewar`
--
DROP TABLE IF EXISTS `v_dewar`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_dewar` AS select `p`.`proposalId` AS `proposalId`,`s`.`shippingId` AS `shippingId`,`s`.`shippingName` AS `shippingName`,`d`.`dewarId` AS `dewarId`,`d`.`code` AS `dewarName`,`d`.`dewarStatus` AS `dewarStatus`,`p`.`proposalCode` AS `proposalCode`,`p`.`proposalNumber` AS `proposalNumber`,`s`.`creationDate` AS `creationDate`,`s`.`shippingType` AS `shippingType`,`d`.`barCode` AS `barCode`,`s`.`shippingStatus` AS `shippingStatus`,`ss`.`beamLineName` AS `beamLineName`,count(distinct `h1`.`DewarTransportHistoryId`) AS `nbEvents`,count(distinct `h2`.`DewarTransportHistoryId`) AS `storesin`,count(if((`bls`.`code` is not null),1,NULL)) AS `nbSamples` from (((((((`Proposal` `p` join `Shipping` `s` on((`s`.`proposalId` = `p`.`proposalId`))) join `Dewar` `d` on((`d`.`shippingId` = `s`.`shippingId`))) left join `Container` `c` on((`c`.`dewarId` = `d`.`dewarId`))) left join `BLSession` `ss` on((`ss`.`sessionId` = `d`.`firstExperimentId`))) left join `BLSample` `bls` on((`bls`.`containerId` = `c`.`containerId`))) left join `DewarTransportHistory` `h1` on(((`h1`.`dewarId` = `d`.`dewarId`) and ((`h1`.`dewarStatus` = 'at ESRF') or (`h1`.`dewarStatus` = 'sent to User'))))) left join `DewarTransportHistory` `h2` on(((`h2`.`dewarId` = `d`.`dewarId`) and (`h2`.`storageLocation` = 'STORES-IN')))) where (((`p`.`proposalCode` like 'MX%') or (`p`.`proposalCode` like 'FX%') or (`p`.`proposalCode` like 'IFX%') or (`p`.`proposalCode` like 'IX%') or (`p`.`proposalCode` like 'BM14U%') or (`p`.`proposalCode` like 'bm161%') or (`p`.`proposalCode` like 'bm162%')) and ((`p`.`proposalCode` <> 'FX') or (`p`.`proposalNumber` <> '999')) and ((`p`.`proposalCode` <> 'IFX') or (`p`.`proposalNumber` <> '999')) and ((`p`.`proposalCode` <> 'MX') or (`p`.`proposalNumber` <> '415')) and (`d`.`type` = 'Dewar') and (`s`.`creationDate` is not null)) group by `d`.`dewarId`;

-- --------------------------------------------------------

--
-- Structure for view `v_dewarBeamline`
--
DROP TABLE IF EXISTS `v_dewarBeamline`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_dewarBeamline` AS select `d`.`beamLineName` AS `beamLineName`,count(0) AS `COUNT(*)` from `v_dewar` `d` where (`d`.`beamLineName` is not null) group by `d`.`beamLineName`;

-- --------------------------------------------------------

--
-- Structure for view `v_dewarBeamlineByWeek`
--
DROP TABLE IF EXISTS `v_dewarBeamlineByWeek`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_dewarBeamlineByWeek` AS select substr(`w`.`num`,6) AS `Week`,count(if((`d`.`beamLineName` like 'ID14%'),1,NULL)) AS `ID14`,count(if((`d`.`beamLineName` like 'ID23%'),1,NULL)) AS `ID23`,count(if((`d`.`beamLineName` like 'ID29%'),1,NULL)) AS `ID29`,count(if((`d`.`beamLineName` like 'BM14%'),1,NULL)) AS `BM14` from (`v_week` `w` left join `v_dewar` `d` on((`w`.`num` = date_format(`d`.`creationDate`,'%x-%v')))) group by `w`.`num` order by `w`.`num`;

-- --------------------------------------------------------

--
-- Structure for view `v_dewarByWeek`
--
DROP TABLE IF EXISTS `v_dewarByWeek`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_dewarByWeek` AS select substr(`w`.`num`,6) AS `Week`,count(if((`d`.`shippingType` = 'DewarTracking'),1,NULL)) AS `Dewars Tracked`,count(if(((`d`.`proposalCode` is not null) and ((`d`.`shippingType` <> 'DewarTracking') or isnull(`d`.`shippingType`))),1,NULL)) AS `Dewars Non-Tracked` from (`v_week` `w` left join `v_dewar` `d` on((`w`.`num` = date_format(`d`.`creationDate`,'%Y-%v')))) group by substr(`w`.`num`,6) order by `w`.`num`;

-- --------------------------------------------------------

--
-- Structure for view `v_dewarByWeekTotal`
--
DROP TABLE IF EXISTS `v_dewarByWeekTotal`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_dewarByWeekTotal` AS select substr(`w`.`num`,6) AS `Week`,count(if((`d`.`shippingType` = 'DewarTracking'),1,NULL)) AS `Dewars Tracked`,count(if(((`d`.`proposalCode` is not null) and ((`d`.`shippingType` <> 'DewarTracking') or isnull(`d`.`shippingType`))),1,NULL)) AS `Dewars Non-Tracked`,count(if((`d`.`proposalCode` is not null),1,NULL)) AS `Total` from (`v_week` `w` left join `v_dewar` `d` on((`w`.`num` = date_format(`d`.`creationDate`,'%Y-%v')))) group by substr(`w`.`num`,6) order by substr(`w`.`num`,6);

-- --------------------------------------------------------

--
-- Structure for view `v_dewarList`
--
DROP TABLE IF EXISTS `v_dewarList`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_dewarList` AS select concat(`d`.`proposalCode`,`d`.`proposalNumber`) AS `proposal`,`d`.`shippingName` AS `shippingName`,`d`.`dewarName` AS `dewarName`,`d`.`barCode` AS `barCode`,date_format(`d`.`creationDate`,'%d/%m/%Y') AS `creationDate`,`d`.`shippingType` AS `shippingType`,count(distinct `h`.`DewarTransportHistoryId`) AS `nbEvents`,`d`.`dewarStatus` AS `dewarStatus`,`d`.`shippingStatus` AS `shippingStatus`,count(if((`bls`.`blSampleId` is not null),1,NULL)) AS `nbSamples` from (((`v_dewar` `d` left join `Container` `c` on((`c`.`dewarId` = `d`.`dewarId`))) left join `BLSample` `bls` on((`bls`.`containerId` = `c`.`containerId`))) left join `DewarTransportHistory` `h` on((`h`.`dewarId` = `d`.`dewarId`))) group by `d`.`dewarId` order by `d`.`creationDate` desc;

-- --------------------------------------------------------

--
-- Structure for view `v_dewarProposalCode`
--
DROP TABLE IF EXISTS `v_dewarProposalCode`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_dewarProposalCode` AS select `d`.`proposalCode` AS `proposalCode`,count(0) AS `COUNT(*)` from `v_dewar` `d` group by `d`.`proposalCode`;

-- --------------------------------------------------------

--
-- Structure for view `v_dewarProposalCodeByWeek`
--
DROP TABLE IF EXISTS `v_dewarProposalCodeByWeek`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_dewarProposalCodeByWeek` AS select substr(`w`.`num`,6) AS `Week`,count(if((`d`.`proposalCode` = 'MX'),1,NULL)) AS `MX`,count(if((`d`.`proposalCode` = 'FX'),1,NULL)) AS `FX`,count(if((`d`.`proposalCode` = 'BM14U'),1,NULL)) AS `BM14U`,count(if((`d`.`proposalCode` = 'BM161'),1,NULL)) AS `BM161`,count(if((`d`.`proposalCode` = 'BM162'),1,NULL)) AS `BM162`,count(if(((`d`.`proposalCode` <> 'MX') and (`d`.`proposalCode` <> 'FX') and (`d`.`proposalCode` <> 'BM14U') and (`d`.`proposalCode` <> 'BM161') and (`d`.`proposalCode` <> 'BM162')),1,NULL)) AS `Others` from (`v_week` `w` left join `v_dewar` `d` on((`w`.`num` = date_format(`d`.`creationDate`,'%Y-%v')))) group by substr(`w`.`num`,6) order by substr(`w`.`num`,6);

-- --------------------------------------------------------

--
-- Structure for view `v_dewar_summary`
--
DROP TABLE IF EXISTS `v_dewar_summary`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_dewar_summary` AS select `Shipping`.`shippingName` AS `shippingName`,`Shipping`.`deliveryAgent_agentName` AS `deliveryAgent_agentName`,`Shipping`.`deliveryAgent_shippingDate` AS `deliveryAgent_shippingDate`,`Shipping`.`deliveryAgent_deliveryDate` AS `deliveryAgent_deliveryDate`,`Shipping`.`deliveryAgent_agentCode` AS `deliveryAgent_agentCode`,`Shipping`.`deliveryAgent_flightCode` AS `deliveryAgent_flightCode`,`Shipping`.`shippingStatus` AS `shippingStatus`,`Shipping`.`bltimeStamp` AS `bltimeStamp`,`Shipping`.`laboratoryId` AS `laboratoryId`,`Shipping`.`isStorageShipping` AS `isStorageShipping`,`Shipping`.`creationDate` AS `creationDate`,`Shipping`.`comments` AS `Shipping_comments`,`Shipping`.`sendingLabContactId` AS `sendingLabContactId`,`Shipping`.`returnLabContactId` AS `returnLabContactId`,`Shipping`.`returnCourier` AS `returnCourier`,`Shipping`.`dateOfShippingToUser` AS `dateOfShippingToUser`,`Shipping`.`shippingType` AS `shippingType`,`Dewar`.`dewarId` AS `dewarId`,`Dewar`.`shippingId` AS `shippingId`,`Dewar`.`code` AS `dewarCode`,`Dewar`.`comments` AS `comments`,`Dewar`.`storageLocation` AS `storageLocation`,`Dewar`.`dewarStatus` AS `dewarStatus`,`Dewar`.`isStorageDewar` AS `isStorageDewar`,`Dewar`.`barCode` AS `barCode`,`Dewar`.`firstExperimentId` AS `firstExperimentId`,`Dewar`.`customsValue` AS `customsValue`,`Dewar`.`transportValue` AS `transportValue`,`Dewar`.`trackingNumberToSynchrotron` AS `trackingNumberToSynchrotron`,`Dewar`.`trackingNumberFromSynchrotron` AS `trackingNumberFromSynchrotron`,`Dewar`.`type` AS `type`,`BLSession`.`sessionId` AS `sessionId`,`BLSession`.`beamLineName` AS `beamlineName`,`BLSession`.`startDate` AS `sessionStartDate`,`BLSession`.`endDate` AS `sessionEndDate`,`BLSession`.`beamLineOperator` AS `beamLineOperator`,`Proposal`.`proposalId` AS `proposalId`,`Container`.`containerId` AS `containerId`,`Container`.`containerType` AS `containerType`,`Container`.`capacity` AS `capacity`,`Container`.`beamlineLocation` AS `beamlineLocation`,`Container`.`sampleChangerLocation` AS `sampleChangerLocation`,`Container`.`containerStatus` AS `containerStatus`,`Container`.`code` AS `containerCode` from (((((`Dewar` left join `Container` on((`Container`.`dewarId` = `Dewar`.`dewarId`))) left join `Shipping` on((`Shipping`.`shippingId` = `Dewar`.`shippingId`))) left join `ShippingHasSession` on((`Shipping`.`shippingId` = `ShippingHasSession`.`shippingId`))) left join `BLSession` on((`ShippingHasSession`.`sessionId` = `BLSession`.`sessionId`))) left join `Proposal` on((`Proposal`.`proposalId` = `Shipping`.`proposalId`))) order by `Dewar`.`dewarId` desc;

-- --------------------------------------------------------

--
-- Structure for view `v_energyScan`
--
DROP TABLE IF EXISTS `v_energyScan`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_energyScan` AS select `EnergyScan`.`energyScanId` AS `energyScanId`,`EnergyScan`.`sessionId` AS `sessionId`,`EnergyScan`.`blSampleId` AS `blSampleId`,`EnergyScan`.`fluorescenceDetector` AS `fluorescenceDetector`,`EnergyScan`.`scanFileFullPath` AS `scanFileFullPath`,`EnergyScan`.`choochFileFullPath` AS `choochFileFullPath`,`EnergyScan`.`jpegChoochFileFullPath` AS `jpegChoochFileFullPath`,`EnergyScan`.`element` AS `element`,`EnergyScan`.`startEnergy` AS `startEnergy`,`EnergyScan`.`endEnergy` AS `endEnergy`,`EnergyScan`.`transmissionFactor` AS `transmissionFactor`,`EnergyScan`.`exposureTime` AS `exposureTime`,`EnergyScan`.`synchrotronCurrent` AS `synchrotronCurrent`,`EnergyScan`.`temperature` AS `temperature`,`EnergyScan`.`peakEnergy` AS `peakEnergy`,`EnergyScan`.`peakFPrime` AS `peakFPrime`,`EnergyScan`.`peakFDoublePrime` AS `peakFDoublePrime`,`EnergyScan`.`inflectionEnergy` AS `inflectionEnergy`,`EnergyScan`.`inflectionFPrime` AS `inflectionFPrime`,`EnergyScan`.`inflectionFDoublePrime` AS `inflectionFDoublePrime`,`EnergyScan`.`xrayDose` AS `xrayDose`,`EnergyScan`.`startTime` AS `startTime`,`EnergyScan`.`endTime` AS `endTime`,`EnergyScan`.`edgeEnergy` AS `edgeEnergy`,`EnergyScan`.`filename` AS `filename`,`EnergyScan`.`beamSizeVertical` AS `beamSizeVertical`,`EnergyScan`.`beamSizeHorizontal` AS `beamSizeHorizontal`,`EnergyScan`.`crystalClass` AS `crystalClass`,`EnergyScan`.`comments` AS `comments`,`EnergyScan`.`flux` AS `flux`,`EnergyScan`.`flux_end` AS `flux_end`,`BLSample`.`blSampleId` AS `BLSample_sampleId`,`BLSample`.`name` AS `name`,`BLSample`.`code` AS `code`,`Protein`.`acronym` AS `acronym`,`BLSession`.`proposalId` AS `BLSession_proposalId` from ((((`EnergyScan` left join `BLSample` on((`BLSample`.`blSampleId` = `EnergyScan`.`blSampleId`))) left join `Crystal` on((`Crystal`.`crystalId` = `BLSample`.`crystalId`))) left join `Protein` on((`Protein`.`proteinId` = `Crystal`.`proteinId`))) left join `BLSession` on((`BLSession`.`sessionId` = `EnergyScan`.`sessionId`)));

-- --------------------------------------------------------

--
-- Structure for view `v_hour`
--
DROP TABLE IF EXISTS `v_hour`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_hour` AS select date_format((now() - interval 23 hour),_utf8'%Y-%m-%d %H') AS `num` union select date_format((now() - interval 22 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 22 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 21 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 21 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 20 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 20 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 19 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 19 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 18 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 18 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 17 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 17 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 16 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 16 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 15 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 15 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 14 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 14 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 13 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 13 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 12 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 12 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 11 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 11 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 10 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 10 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 9 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 9 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 8 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 8 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 7 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 7 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 6 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 6 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 5 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 5 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 4 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 4 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 3 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 3 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 2 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 2 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 1 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 1 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 0 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 0 HOUR),'%Y-%m-%d %H')`;

-- --------------------------------------------------------

--
-- Structure for view `v_Log4Stat`
--
DROP TABLE IF EXISTS `v_Log4Stat`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_Log4Stat` AS select `s`.`id` AS `id`,`s`.`priority` AS `priority`,`s`.`timestamp` AS `timestamp`,`s`.`msg` AS `msg`,`s`.`detail` AS `detail`,`s`.`value` AS `value` from `Log4Stat` `s` where (((`s`.`detail` like 'fx%') or (`s`.`detail` like 'ifx%') or (`s`.`detail` like 'mx%') or (`s`.`detail` like 'ix%') or (`s`.`detail` like 'bm14u%') or (`s`.`detail` like 'bm161%') or (`s`.`detail` like 'bm162%')) and (`s`.`detail` <> 'fx999') and (`s`.`detail` <> 'ifx999') and (`s`.`detail` <> 'mx415'));

-- --------------------------------------------------------

--
-- Structure for view `v_logonByHour`
--
DROP TABLE IF EXISTS `v_logonByHour`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_logonByHour` AS select date_format(`w`.`num`,'%H') AS `Hour`,count(distinct `s`.`detail`) AS `Distinct logins`,(count(`s`.`detail`) - count(distinct `s`.`detail`)) AS `Total logins` from (`v_hour` `w` left join `v_Log4Stat` `s` on(((`w`.`num` = date_format(`s`.`timestamp`,'%Y-%m-%d %H')) and (`s`.`msg` = 'LOGON')))) group by date_format(`w`.`num`,'%H') order by `w`.`num`;

-- --------------------------------------------------------

--
-- Structure for view `v_logonByMonthDay`
--
DROP TABLE IF EXISTS `v_logonByMonthDay`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_logonByMonthDay` AS select date_format(`w`.`num`,'%d/%m') AS `Day`,count(distinct `s`.`detail`) AS `Distinct logins`,(count(`s`.`detail`) - count(distinct `s`.`detail`)) AS `Total logins` from (`v_monthDay` `w` left join `v_Log4Stat` `s` on(((`w`.`num` = date_format(`s`.`timestamp`,'%Y-%m-%d')) and (`s`.`msg` = 'LOGON')))) group by date_format(`w`.`num`,'%d/%m') order by `w`.`num`;

-- --------------------------------------------------------

--
-- Structure for view `v_logonByWeek`
--
DROP TABLE IF EXISTS `v_logonByWeek`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_logonByWeek` AS select substr(`w`.`num`,6) AS `Week`,count(distinct `s`.`detail`) AS `Distinct logins`,(count(`s`.`detail`) - count(distinct `s`.`detail`)) AS `Total logins` from (`v_week` `w` left join `v_Log4Stat` `s` on(((`w`.`num` = date_format(`s`.`timestamp`,'%Y-%v')) and (`s`.`msg` = 'LOGON')))) group by substr(`w`.`num`,6) order by `w`.`num`;

-- --------------------------------------------------------

--
-- Structure for view `v_logonByWeekDay`
--
DROP TABLE IF EXISTS `v_logonByWeekDay`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_logonByWeekDay` AS select date_format(`w`.`day`,'%W') AS `Day`,count(distinct `s`.`detail`) AS `Distinct logins`,(count(`s`.`detail`) - count(distinct `s`.`detail`)) AS `Total logins` from (`v_weekDay` `w` left join `v_Log4Stat` `s` on(((`w`.`day` = date_format(`s`.`timestamp`,'%Y-%m-%d')) and (`s`.`msg` = 'LOGON')))) group by `w`.`day` order by `w`.`day`;

-- --------------------------------------------------------

--
-- Structure for view `v_monthDay`
--
DROP TABLE IF EXISTS `v_monthDay`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_monthDay` AS select date_format((now() - interval 31 day),_utf8'%Y-%m-%d') AS `num` union select date_format((now() - interval 30 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 30 DAY),'%Y-%m-%d')` union select date_format((now() - interval 29 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 29 DAY),'%Y-%m-%d')` union select date_format((now() - interval 28 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 28 DAY),'%Y-%m-%d')` union select date_format((now() - interval 27 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 27 DAY),'%Y-%m-%d')` union select date_format((now() - interval 26 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 26 DAY),'%Y-%m-%d')` union select date_format((now() - interval 25 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 25 DAY),'%Y-%m-%d')` union select date_format((now() - interval 24 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 24 DAY),'%Y-%m-%d')` union select date_format((now() - interval 23 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 23 DAY),'%Y-%m-%d')` union select date_format((now() - interval 22 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 22 DAY),'%Y-%m-%d')` union select date_format((now() - interval 21 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 21 DAY),'%Y-%m-%d')` union select date_format((now() - interval 20 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 20 DAY),'%Y-%m-%d')` union select date_format((now() - interval 19 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 19 DAY),'%Y-%m-%d')` union select date_format((now() - interval 18 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 18 DAY),'%Y-%m-%d')` union select date_format((now() - interval 17 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 17 DAY),'%Y-%m-%d')` union select date_format((now() - interval 16 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 16 DAY),'%Y-%m-%d')` union select date_format((now() - interval 15 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 15 DAY),'%Y-%m-%d')` union select date_format((now() - interval 14 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 14 DAY),'%Y-%m-%d')` union select date_format((now() - interval 13 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 13 DAY),'%Y-%m-%d')` union select date_format((now() - interval 12 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 12 DAY),'%Y-%m-%d')` union select date_format((now() - interval 11 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 11 DAY),'%Y-%m-%d')` union select date_format((now() - interval 10 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 10 DAY),'%Y-%m-%d')` union select date_format((now() - interval 9 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 9 DAY),'%Y-%m-%d')` union select date_format((now() - interval 8 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 8 DAY),'%Y-%m-%d')` union select date_format((now() - interval 7 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 7 DAY),'%Y-%m-%d')` union select date_format((now() - interval 6 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 6 DAY),'%Y-%m-%d')` union select date_format((now() - interval 5 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 5 DAY),'%Y-%m-%d')` union select date_format((now() - interval 4 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 4 DAY),'%Y-%m-%d')` union select date_format((now() - interval 3 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 3 DAY),'%Y-%m-%d')` union select date_format((now() - interval 2 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 2 DAY),'%Y-%m-%d')` union select date_format((now() - interval 1 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 1 DAY),'%Y-%m-%d')` union select date_format((now() - interval 0 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 0 DAY),'%Y-%m-%d')`;

-- --------------------------------------------------------

--
-- Structure for view `v_mx_autoprocessing_stats`
--
DROP TABLE IF EXISTS `v_mx_autoprocessing_stats`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_mx_autoprocessing_stats` AS select `AutoProcScalingStatistics`.`autoProcScalingStatisticsId` AS `autoProcScalingStatisticsId`,`AutoProcScalingStatistics`.`autoProcScalingId` AS `autoProcScalingId`,`AutoProcScalingStatistics`.`scalingStatisticsType` AS `scalingStatisticsType`,`AutoProcScalingStatistics`.`resolutionLimitLow` AS `resolutionLimitLow`,`AutoProcScalingStatistics`.`resolutionLimitHigh` AS `resolutionLimitHigh`,`AutoProcScalingStatistics`.`rMerge` AS `rMerge`,`AutoProcScalingStatistics`.`rMeasWithinIPlusIMinus` AS `rMeasWithinIPlusIMinus`,`AutoProcScalingStatistics`.`rMeasAllIPlusIMinus` AS `rMeasAllIPlusIMinus`,`AutoProcScalingStatistics`.`rPimWithinIPlusIMinus` AS `rPimWithinIPlusIMinus`,`AutoProcScalingStatistics`.`rPimAllIPlusIMinus` AS `rPimAllIPlusIMinus`,`AutoProcScalingStatistics`.`fractionalPartialBias` AS `fractionalPartialBias`,`AutoProcScalingStatistics`.`nTotalObservations` AS `nTotalObservations`,`AutoProcScalingStatistics`.`nTotalUniqueObservations` AS `nTotalUniqueObservations`,`AutoProcScalingStatistics`.`meanIOverSigI` AS `meanIOverSigI`,`AutoProcScalingStatistics`.`completeness` AS `completeness`,`AutoProcScalingStatistics`.`multiplicity` AS `multiplicity`,`AutoProcScalingStatistics`.`anomalousCompleteness` AS `anomalousCompleteness`,`AutoProcScalingStatistics`.`anomalousMultiplicity` AS `anomalousMultiplicity`,`AutoProcScalingStatistics`.`recordTimeStamp` AS `recordTimeStamp`,`AutoProcScalingStatistics`.`anomalous` AS `anomalous`,`AutoProcScalingStatistics`.`ccHalf` AS `ccHalf`,`AutoProcScalingStatistics`.`ccAno` AS `ccAno`,`AutoProcScalingStatistics`.`sigAno` AS `sigAno`,`AutoProcScalingStatistics`.`isa` AS `ISA`,`DataCollection`.`dataCollectionId` AS `dataCollectionId`,`DataCollection`.`strategySubWedgeOrigId` AS `strategySubWedgeOrigId`,`DataCollection`.`detectorId` AS `detectorId`,`DataCollection`.`blSubSampleId` AS `blSubSampleId`,`DataCollection`.`dataCollectionNumber` AS `dataCollectionNumber`,`DataCollection`.`startTime` AS `startTime`,`DataCollection`.`endTime` AS `endTime`,`BLSession`.`sessionId` AS `sessionId`,`BLSession`.`proposalId` AS `proposalId`,`BLSession`.`beamLineName` AS `beamLineName` from ((((((`AutoProcScalingStatistics` left join `AutoProcScaling` on((`AutoProcScalingStatistics`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `AutoProcScaling_has_Int` on((`AutoProcScaling_has_Int`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `AutoProcIntegration` on((`AutoProcIntegration`.`autoProcIntegrationId` = `AutoProcScaling_has_Int`.`autoProcIntegrationId`))) left join `DataCollection` on((`DataCollection`.`dataCollectionId` = `AutoProcIntegration`.`dataCollectionId`))) left join `DataCollectionGroup` on((`DataCollectionGroup`.`dataCollectionGroupId` = `DataCollection`.`dataCollectionGroupId`))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`)));

-- --------------------------------------------------------

--
-- Structure for view `v_mx_sample`
--
DROP TABLE IF EXISTS `v_mx_sample`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_mx_sample` AS select `BLSample`.`blSampleId` AS `BLSample_blSampleId`,`BLSample`.`diffractionPlanId` AS `BLSample_diffractionPlanId`,`BLSample`.`crystalId` AS `BLSample_crystalId`,`BLSample`.`containerId` AS `BLSample_containerId`,`BLSample`.`name` AS `BLSample_name`,`BLSample`.`code` AS `BLSample_code`,`BLSample`.`location` AS `BLSample_location`,`BLSample`.`holderLength` AS `BLSample_holderLength`,`BLSample`.`loopLength` AS `BLSample_loopLength`,`BLSample`.`loopType` AS `BLSample_loopType`,`BLSample`.`wireWidth` AS `BLSample_wireWidth`,`BLSample`.`comments` AS `BLSample_comments`,`BLSample`.`completionStage` AS `BLSample_completionStage`,`BLSample`.`structureStage` AS `BLSample_structureStage`,`BLSample`.`publicationStage` AS `BLSample_publicationStage`,`BLSample`.`publicationComments` AS `BLSample_publicationComments`,`BLSample`.`blSampleStatus` AS `BLSample_blSampleStatus`,`BLSample`.`isInSampleChanger` AS `BLSample_isInSampleChanger`,`BLSample`.`lastKnownCenteringPosition` AS `BLSample_lastKnownCenteringPosition`,`BLSample`.`recordTimeStamp` AS `BLSample_recordTimeStamp`,`BLSample`.`SMILES` AS `BLSample_SMILES`,`Protein`.`proteinId` AS `Protein_proteinId`,`Protein`.`name` AS `Protein_name`,`Protein`.`acronym` AS `Protein_acronym`,`Protein`.`proteinType` AS `Protein_proteinType`,`Protein`.`proposalId` AS `Protein_proposalId`,`Person`.`personId` AS `Person_personId`,`Person`.`familyName` AS `Person_familyName`,`Person`.`givenName` AS `Person_givenName`,`Person`.`emailAddress` AS `Person_emailAddress`,`Container`.`containerId` AS `Container_containerId`,`Container`.`code` AS `Container_code`,`Container`.`containerType` AS `Container_containerType`,`Container`.`containerStatus` AS `Container_containerStatus`,`Dewar`.`code` AS `Dewar_code`,`Dewar`.`dewarId` AS `Dewar_dewarId`,`Dewar`.`storageLocation` AS `Dewar_storageLocation`,`Dewar`.`dewarStatus` AS `Dewar_dewarStatus`,`Dewar`.`barCode` AS `Dewar_barCode`,`Shipping`.`shippingId` AS `Shipping_shippingId` from ((((((`BLSample` left join `Crystal` on((`Crystal`.`crystalId` = `BLSample`.`crystalId`))) left join `Protein` on((`Protein`.`proteinId` = `Crystal`.`proteinId`))) left join `Person` on((`Person`.`personId` = `Protein`.`personId`))) left join `Container` on((`BLSample`.`containerId` = `Container`.`containerId`))) left join `Dewar` on((`Dewar`.`dewarId` = `Container`.`dewarId`))) left join `Shipping` on((`Dewar`.`shippingId` = `Shipping`.`shippingId`)));

-- --------------------------------------------------------

--
-- Structure for view `v_phasing`
--
DROP TABLE IF EXISTS `v_phasing`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_phasing` AS select `BLSample`.`blSampleId` AS `BLSample_blSampleId`,`AutoProcIntegration`.`autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`,`AutoProcIntegration`.`dataCollectionId` AS `AutoProcIntegration_dataCollectionId`,`AutoProcIntegration`.`autoProcProgramId` AS `AutoProcIntegration_autoProcProgramId`,`AutoProcIntegration`.`startImageNumber` AS `AutoProcIntegration_startImageNumber`,`AutoProcIntegration`.`endImageNumber` AS `AutoProcIntegration_endImageNumber`,`AutoProcIntegration`.`refinedDetectorDistance` AS `AutoProcIntegration_refinedDetectorDistance`,`AutoProcIntegration`.`refinedXBeam` AS `AutoProcIntegration_refinedXBeam`,`AutoProcIntegration`.`refinedYBeam` AS `AutoProcIntegration_refinedYBeam`,`AutoProcIntegration`.`rotationAxisX` AS `AutoProcIntegration_rotationAxisX`,`AutoProcIntegration`.`rotationAxisY` AS `AutoProcIntegration_rotationAxisY`,`AutoProcIntegration`.`rotationAxisZ` AS `AutoProcIntegration_rotationAxisZ`,`AutoProcIntegration`.`beamVectorX` AS `AutoProcIntegration_beamVectorX`,`AutoProcIntegration`.`beamVectorY` AS `AutoProcIntegration_beamVectorY`,`AutoProcIntegration`.`beamVectorZ` AS `AutoProcIntegration_beamVectorZ`,`AutoProcIntegration`.`cell_a` AS `AutoProcIntegration_cell_a`,`AutoProcIntegration`.`cell_b` AS `AutoProcIntegration_cell_b`,`AutoProcIntegration`.`cell_c` AS `AutoProcIntegration_cell_c`,`AutoProcIntegration`.`cell_alpha` AS `AutoProcIntegration_cell_alpha`,`AutoProcIntegration`.`cell_beta` AS `AutoProcIntegration_cell_beta`,`AutoProcIntegration`.`cell_gamma` AS `AutoProcIntegration_cell_gamma`,`AutoProcIntegration`.`recordTimeStamp` AS `AutoProcIntegration_recordTimeStamp`,`AutoProcIntegration`.`anomalous` AS `AutoProcIntegration_anomalous`,`SpaceGroup`.`spaceGroupId` AS `SpaceGroup_spaceGroupId`,`SpaceGroup`.`geometryClassnameId` AS `SpaceGroup_geometryClassnameId`,`SpaceGroup`.`spaceGroupNumber` AS `SpaceGroup_spaceGroupNumber`,`SpaceGroup`.`spaceGroupShortName` AS `SpaceGroup_spaceGroupShortName`,`SpaceGroup`.`spaceGroupName` AS `SpaceGroup_spaceGroupName`,`SpaceGroup`.`bravaisLattice` AS `SpaceGroup_bravaisLattice`,`SpaceGroup`.`bravaisLatticeName` AS `SpaceGroup_bravaisLatticeName`,`SpaceGroup`.`pointGroup` AS `SpaceGroup_pointGroup`,`SpaceGroup`.`MX_used` AS `SpaceGroup_MX_used`,`PhasingStep`.`phasingStepId` AS `PhasingStep_phasingStepId`,`PhasingStep`.`previousPhasingStepId` AS `PhasingStep_previousPhasingStepId`,`PhasingStep`.`programRunId` AS `PhasingStep_programRunId`,`PhasingStep`.`spaceGroupId` AS `PhasingStep_spaceGroupId`,`PhasingStep`.`autoProcScalingId` AS `PhasingStep_autoProcScalingId`,`PhasingStep`.`phasingAnalysisId` AS `PhasingStep_phasingAnalysisId`,`PhasingStep`.`phasingStepType` AS `PhasingStep_phasingStepType`,`PhasingStep`.`method` AS `PhasingStep_method`,`PhasingStep`.`solventContent` AS `PhasingStep_solventContent`,`PhasingStep`.`enantiomorph` AS `PhasingStep_enantiomorph`,`PhasingStep`.`lowRes` AS `PhasingStep_lowRes`,`PhasingStep`.`highRes` AS `PhasingStep_highRes`,`PhasingStep`.`recordTimeStamp` AS `PhasingStep_recordTimeStamp`,`DataCollection`.`dataCollectionId` AS `DataCollection_dataCollectionId`,`DataCollection`.`dataCollectionGroupId` AS `DataCollection_dataCollectionGroupId`,`DataCollection`.`strategySubWedgeOrigId` AS `DataCollection_strategySubWedgeOrigId`,`DataCollection`.`detectorId` AS `DataCollection_detectorId`,`DataCollection`.`blSubSampleId` AS `DataCollection_blSubSampleId`,`DataCollection`.`dataCollectionNumber` AS `DataCollection_dataCollectionNumber`,`DataCollection`.`startTime` AS `DataCollection_startTime`,`DataCollection`.`endTime` AS `DataCollection_endTime`,`DataCollection`.`runStatus` AS `DataCollection_runStatus`,`DataCollection`.`axisStart` AS `DataCollection_axisStart`,`DataCollection`.`axisEnd` AS `DataCollection_axisEnd`,`DataCollection`.`axisRange` AS `DataCollection_axisRange`,`DataCollection`.`overlap` AS `DataCollection_overlap`,`DataCollection`.`numberOfImages` AS `DataCollection_numberOfImages`,`DataCollection`.`startImageNumber` AS `DataCollection_startImageNumber`,`DataCollection`.`numberOfPasses` AS `DataCollection_numberOfPasses`,`DataCollection`.`exposureTime` AS `DataCollection_exposureTime`,`DataCollection`.`imageDirectory` AS `DataCollection_imageDirectory`,`DataCollection`.`imagePrefix` AS `DataCollection_imagePrefix`,`DataCollection`.`imageSuffix` AS `DataCollection_imageSuffix`,`DataCollection`.`fileTemplate` AS `DataCollection_fileTemplate`,`DataCollection`.`wavelength` AS `DataCollection_wavelength`,`DataCollection`.`resolution` AS `DataCollection_resolution`,`DataCollection`.`detectorDistance` AS `DataCollection_detectorDistance`,`DataCollection`.`xBeam` AS `DataCollection_xBeam`,`DataCollection`.`yBeam` AS `DataCollection_yBeam`,`DataCollection`.`xBeamPix` AS `DataCollection_xBeamPix`,`DataCollection`.`yBeamPix` AS `DataCollection_yBeamPix`,`DataCollection`.`comments` AS `DataCollection_comments`,`DataCollection`.`printableForReport` AS `DataCollection_printableForReport`,`DataCollection`.`slitGapVertical` AS `DataCollection_slitGapVertical`,`DataCollection`.`slitGapHorizontal` AS `DataCollection_slitGapHorizontal`,`DataCollection`.`transmission` AS `DataCollection_transmission`,`DataCollection`.`synchrotronMode` AS `DataCollection_synchrotronMode`,`DataCollection`.`xtalSnapshotFullPath1` AS `DataCollection_xtalSnapshotFullPath1`,`DataCollection`.`xtalSnapshotFullPath2` AS `DataCollection_xtalSnapshotFullPath2`,`DataCollection`.`xtalSnapshotFullPath3` AS `DataCollection_xtalSnapshotFullPath3`,`DataCollection`.`xtalSnapshotFullPath4` AS `DataCollection_xtalSnapshotFullPath4`,`DataCollection`.`rotationAxis` AS `DataCollection_rotationAxis`,`DataCollection`.`phiStart` AS `DataCollection_phiStart`,`DataCollection`.`kappaStart` AS `DataCollection_kappaStart`,`DataCollection`.`omegaStart` AS `DataCollection_omegaStart`,`DataCollection`.`resolutionAtCorner` AS `DataCollection_resolutionAtCorner`,`DataCollection`.`detector2Theta` AS `DataCollection_detector2Theta`,`DataCollection`.`undulatorGap1` AS `DataCollection_undulatorGap1`,`DataCollection`.`undulatorGap2` AS `DataCollection_undulatorGap2`,`DataCollection`.`undulatorGap3` AS `DataCollection_undulatorGap3`,`DataCollection`.`beamSizeAtSampleX` AS `DataCollection_beamSizeAtSampleX`,`DataCollection`.`beamSizeAtSampleY` AS `DataCollection_beamSizeAtSampleY`,`DataCollection`.`centeringMethod` AS `DataCollection_centeringMethod`,`DataCollection`.`averageTemperature` AS `DataCollection_averageTemperature`,`DataCollection`.`actualCenteringPosition` AS `DataCollection_actualCenteringPosition`,`DataCollection`.`beamShape` AS `DataCollection_beamShape`,`DataCollection`.`flux` AS `DataCollection_flux`,`DataCollection`.`flux_end` AS `DataCollection_flux_end`,`DataCollection`.`totalAbsorbedDose` AS `DataCollection_totalAbsorbedDose`,`DataCollection`.`bestWilsonPlotPath` AS `DataCollection_bestWilsonPlotPath`,`DataCollection`.`imageQualityIndicatorsPlotPath` AS `DataCollection_imageQualityIndicatorsPlotPath`,`DataCollection`.`imageQualityIndicatorsCSVPath` AS `DataCollection_imageQualityIndicatorsCSVPath`,`PhasingProgramRun`.`phasingProgramRunId` AS `PhasingProgramRun_phasingProgramRunId`,`PhasingProgramRun`.`phasingCommandLine` AS `PhasingProgramRun_phasingCommandLine`,`PhasingProgramRun`.`phasingPrograms` AS `PhasingProgramRun_phasingPrograms`,`PhasingProgramRun`.`phasingStatus` AS `PhasingProgramRun_phasingStatus`,`PhasingProgramRun`.`phasingMessage` AS `PhasingProgramRun_phasingMessage`,`PhasingProgramRun`.`phasingStartTime` AS `PhasingProgramRun_phasingStartTime`,`PhasingProgramRun`.`phasingEndTime` AS `PhasingProgramRun_phasingEndTime`,`PhasingProgramRun`.`phasingEnvironment` AS `PhasingProgramRun_phasingEnvironment`,`PhasingProgramRun`.`recordTimeStamp` AS `PhasingProgramRun_recordTimeStamp`,`Protein`.`proteinId` AS `Protein_proteinId`,`BLSession`.`sessionId` AS `BLSession_sessionId`,`BLSession`.`proposalId` AS `BLSession_proposalId`,`PhasingStatistics`.`phasingStatisticsId` AS `PhasingStatistics_phasingStatisticsId`,`PhasingStatistics`.`metric` AS `PhasingStatistics_metric`,`PhasingStatistics`.`statisticsValue` AS `PhasingStatistics_statisticsValue` from (((((((((((((`DataCollection` left join `AutoProcIntegration` on((`AutoProcIntegration`.`dataCollectionId` = `DataCollection`.`dataCollectionId`))) left join `AutoProcScaling_has_Int` on((`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`))) left join `AutoProcScaling` on((`AutoProcScaling`.`autoProcScalingId` = `AutoProcScaling_has_Int`.`autoProcScalingId`))) left join `PhasingStep` on((`PhasingStep`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `PhasingStatistics` on((`PhasingStatistics`.`phasingStepId` = `PhasingStep`.`phasingStepId`))) left join `SpaceGroup` on((`SpaceGroup`.`spaceGroupId` = `PhasingStep`.`spaceGroupId`))) left join `DataCollectionGroup` on((`DataCollectionGroup`.`dataCollectionGroupId` = `DataCollection`.`dataCollectionGroupId`))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`))) left join `Proposal` on((`Proposal`.`proposalId` = `BLSession`.`proposalId`))) left join `BLSample` on((`BLSample`.`blSampleId` = `DataCollectionGroup`.`blSampleId`))) left join `Crystal` on((`Crystal`.`crystalId` = `BLSample`.`crystalId`))) left join `Protein` on((`Crystal`.`proteinId` = `Protein`.`proteinId`))) left join `PhasingProgramRun` on((`PhasingProgramRun`.`phasingProgramRunId` = `PhasingStep`.`programRunId`)));

-- --------------------------------------------------------

--
-- Structure for view `v_sample`
--
DROP TABLE IF EXISTS `v_sample`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_sample` AS select `d`.`proposalId` AS `proposalId`,`d`.`shippingId` AS `shippingId`,`d`.`dewarId` AS `dewarId`,`c`.`containerId` AS `containerId`,`bls`.`blSampleId` AS `blSampleId`,`d`.`proposalCode` AS `proposalCode`,`d`.`proposalNumber` AS `proposalNumber`,`d`.`creationDate` AS `creationDate`,`d`.`shippingType` AS `shippingType`,`d`.`barCode` AS `barCode`,`d`.`shippingStatus` AS `shippingStatus` from ((`BLSample` `bls` join `Container` `c` on((`c`.`containerId` = `bls`.`containerId`))) join `v_dewar` `d` on((`d`.`dewarId` = `c`.`dewarId`)));

-- --------------------------------------------------------

--
-- Structure for view `v_sampleByWeek`
--
DROP TABLE IF EXISTS `v_sampleByWeek`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_sampleByWeek` AS select substr(`w`.`num`,6) AS `Week`,if((`w`.`num` <= date_format(now(),'%Y-%v')),count(if((`bls`.`proposalCode` is not null),1,NULL)),NULL) AS `Samples` from (`v_week` `w` left join `v_sample` `bls` on((`w`.`num` = date_format(`bls`.`creationDate`,'%Y-%v')))) group by substr(`w`.`num`,6) order by substr(`w`.`num`,6);

-- --------------------------------------------------------

--
-- Structure for view `v_saxs_datacollection`
--
DROP TABLE IF EXISTS `v_saxs_datacollection`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_saxs_datacollection` AS select `Subtraction`.`subtractionId` AS `Subtraction_subtractionId`,`MeasurementToDataCollection`.`dataCollectionId` AS `MeasurementToDataCollection_dataCollectionId`,`MeasurementToDataCollection`.`dataCollectionOrder` AS `MeasurementToDataCollection_dataCollectionOrder`,`MeasurementToDataCollection`.`measurementToDataCollectionId` AS `MeasurementToDataCollection_measurementToDataCollectionId`,`Specimen`.`specimenId` AS `Specimen_specimenId`,`Measurement`.`code` AS `Measurement_code`,`Measurement`.`measurementId` AS `Measurement_measurementId`,`Buffer`.`bufferId` AS `Buffer_bufferId`,`Buffer`.`proposalId` AS `Buffer_proposalId`,`Buffer`.`safetyLevelId` AS `Buffer_safetyLevelId`,`Buffer`.`name` AS `Buffer_name`,`Buffer`.`acronym` AS `Buffer_acronym`,`Buffer`.`pH` AS `Buffer_pH`,`Buffer`.`composition` AS `Buffer_composition`,`Buffer`.`comments` AS `Buffer_comments`,`Macromolecule`.`macromoleculeId` AS `Macromolecule_macromoleculeId`,`Macromolecule`.`proposalId` AS `Macromolecule_proposalId`,`Macromolecule`.`safetyLevelId` AS `Macromolecule_safetyLevelId`,`Macromolecule`.`name` AS `Macromolecule_name`,`Macromolecule`.`acronym` AS `Macromolecule_acronym`,`Macromolecule`.`extintionCoefficient` AS `Macromolecule_extintionCoefficient`,`Macromolecule`.`molecularMass` AS `Macromolecule_molecularMass`,`Macromolecule`.`sequence` AS `Macromolecule_sequence`,`Macromolecule`.`contactsDescriptionFilePath` AS `Macromolecule_contactsDescriptionFilePath`,`Macromolecule`.`symmetry` AS `Macromolecule_symmetry`,`Macromolecule`.`comments` AS `Macromolecule_comments`,`Macromolecule`.`refractiveIndex` AS `Macromolecule_refractiveIndex`,`Macromolecule`.`solventViscosity` AS `Macromolecule_solventViscosity`,`Macromolecule`.`creationDate` AS `Macromolecule_creationDate`,`Specimen`.`experimentId` AS `Specimen_experimentId`,`Specimen`.`bufferId` AS `Specimen_bufferId`,`Specimen`.`samplePlatePositionId` AS `Specimen_samplePlatePositionId`,`Specimen`.`safetyLevelId` AS `Specimen_safetyLevelId`,`Specimen`.`stockSolutionId` AS `Specimen_stockSolutionId`,`Specimen`.`code` AS `Specimen_code`,`Specimen`.`concentration` AS `Specimen_concentration`,`Specimen`.`volume` AS `Specimen_volume`,`Specimen`.`comments` AS `Specimen_comments`,`SamplePlatePosition`.`samplePlatePositionId` AS `SamplePlatePosition_samplePlatePositionId`,`SamplePlatePosition`.`samplePlateId` AS `SamplePlatePosition_samplePlateId`,`SamplePlatePosition`.`rowNumber` AS `SamplePlatePosition_rowNumber`,`SamplePlatePosition`.`columnNumber` AS `SamplePlatePosition_columnNumber`,`SamplePlatePosition`.`volume` AS `SamplePlatePosition_volume`,`SamplePlate`.`samplePlateId` AS `samplePlateId`,`SamplePlate`.`experimentId` AS `experimentId`,`SamplePlate`.`plateGroupId` AS `plateGroupId`,`SamplePlate`.`plateTypeId` AS `plateTypeId`,`SamplePlate`.`instructionSetId` AS `instructionSetId`,`SamplePlate`.`boxId` AS `SamplePlate_boxId`,`SamplePlate`.`name` AS `SamplePlate_name`,`SamplePlate`.`slotPositionRow` AS `SamplePlate_slotPositionRow`,`SamplePlate`.`slotPositionColumn` AS `SamplePlate_slotPositionColumn`,`SamplePlate`.`storageTemperature` AS `SamplePlate_storageTemperature`,`Experiment`.`experimentId` AS `Experiment_experimentId`,`Experiment`.`sessionId` AS `Experiment_sessionId`,`Experiment`.`proposalId` AS `Experiment_proposalId`,`Experiment`.`name` AS `Experiment_name`,`Experiment`.`creationDate` AS `Experiment_creationDate`,`Experiment`.`experimentType` AS `Experiment_experimentType`,`Experiment`.`sourceFilePath` AS `Experiment_sourceFilePath`,`Experiment`.`dataAcquisitionFilePath` AS `Experiment_dataAcquisitionFilePath`,`Experiment`.`status` AS `Experiment_status`,`Experiment`.`comments` AS `Experiment_comments`,`Measurement`.`priorityLevelId` AS `Measurement_priorityLevelId`,`Measurement`.`exposureTemperature` AS `Measurement_exposureTemperature`,`Measurement`.`viscosity` AS `Measurement_viscosity`,`Measurement`.`flow` AS `Measurement_flow`,`Measurement`.`extraFlowTime` AS `Measurement_extraFlowTime`,`Measurement`.`volumeToLoad` AS `Measurement_volumeToLoad`,`Measurement`.`waitTime` AS `Measurement_waitTime`,`Measurement`.`transmission` AS `Measurement_transmission`,`Measurement`.`comments` AS `Measurement_comments`,`Run`.`runId` AS `Run_runId`,`Run`.`timePerFrame` AS `Run_timePerFrame`,`Run`.`timeStart` AS `Run_timeStart`,`Run`.`timeEnd` AS `Run_timeEnd`,`Run`.`storageTemperature` AS `Run_storageTemperature`,`Run`.`exposureTemperature` AS `Run_exposureTemperature`,`Run`.`spectrophotometer` AS `Run_spectrophotometer`,`Run`.`energy` AS `Run_energy`,`Run`.`creationDate` AS `Run_creationDate`,`Run`.`frameAverage` AS `Run_frameAverage`,`Run`.`frameCount` AS `Run_frameCount`,`Run`.`transmission` AS `Run_transmission`,`Run`.`beamCenterX` AS `Run_beamCenterX`,`Run`.`beamCenterY` AS `Run_beamCenterY`,`Run`.`pixelSizeX` AS `Run_pixelSizeX`,`Run`.`pixelSizeY` AS `Run_pixelSizeY`,`Run`.`radiationRelative` AS `Run_radiationRelative`,`Run`.`radiationAbsolute` AS `Run_radiationAbsolute`,`Run`.`normalization` AS `Run_normalization`,`Merge`.`mergeId` AS `Merge_mergeId`,`Merge`.`measurementId` AS `Merge_measurementId`,`Merge`.`frameListId` AS `Merge_frameListId`,`Merge`.`discardedFrameNameList` AS `Merge_discardedFrameNameList`,`Merge`.`averageFilePath` AS `Merge_averageFilePath`,`Merge`.`framesCount` AS `Merge_framesCount`,`Merge`.`framesMerge` AS `Merge_framesMerge`,`Merge`.`creationDate` AS `Merge_creationDate`,`Subtraction`.`dataCollectionId` AS `Subtraction_dataCollectionId`,`Subtraction`.`rg` AS `Subtraction_rg`,`Subtraction`.`rgStdev` AS `Subtraction_rgStdev`,`Subtraction`.`I0` AS `Subtraction_I0`,`Subtraction`.`I0Stdev` AS `Subtraction_I0Stdev`,`Subtraction`.`firstPointUsed` AS `Subtraction_firstPointUsed`,`Subtraction`.`lastPointUsed` AS `Subtraction_lastPointUsed`,`Subtraction`.`quality` AS `Subtraction_quality`,`Subtraction`.`isagregated` AS `Subtraction_isagregated`,`Subtraction`.`concentration` AS `Subtraction_concentration`,`Subtraction`.`gnomFilePath` AS `Subtraction_gnomFilePath`,`Subtraction`.`rgGuinier` AS `Subtraction_rgGuinier`,`Subtraction`.`rgGnom` AS `Subtraction_rgGnom`,`Subtraction`.`dmax` AS `Subtraction_dmax`,`Subtraction`.`total` AS `Subtraction_total`,`Subtraction`.`volume` AS `Subtraction_volume`,`Subtraction`.`creationTime` AS `Subtraction_creationTime`,`Subtraction`.`kratkyFilePath` AS `Subtraction_kratkyFilePath`,`Subtraction`.`scatteringFilePath` AS `Subtraction_scatteringFilePath`,`Subtraction`.`guinierFilePath` AS `Subtraction_guinierFilePath`,`Subtraction`.`substractedFilePath` AS `Subtraction_substractedFilePath`,`Subtraction`.`gnomFilePathOutput` AS `Subtraction_gnomFilePathOutput`,`Subtraction`.`sampleOneDimensionalFiles` AS `Subtraction_sampleOneDimensionalFiles`,`Subtraction`.`bufferOnedimensionalFiles` AS `Subtraction_bufferOnedimensionalFiles`,`Subtraction`.`sampleAverageFilePath` AS `Subtraction_sampleAverageFilePath`,`Subtraction`.`bufferAverageFilePath` AS `Subtraction_bufferAverageFilePath` from ((((((((((`Measurement` left join `Specimen` on((`Specimen`.`specimenId` = `Measurement`.`specimenId`))) left join `MeasurementToDataCollection` on((`MeasurementToDataCollection`.`measurementId` = `Measurement`.`measurementId`))) left join `Macromolecule` on((`Macromolecule`.`macromoleculeId` = `Specimen`.`macromoleculeId`))) left join `Buffer` on((`Buffer`.`bufferId` = `Specimen`.`bufferId`))) left join `SamplePlatePosition` on((`SamplePlatePosition`.`samplePlatePositionId` = `Specimen`.`samplePlatePositionId`))) left join `SamplePlate` on((`SamplePlate`.`samplePlateId` = `SamplePlatePosition`.`samplePlateId`))) left join `Experiment` on((`Experiment`.`experimentId` = `Specimen`.`experimentId`))) left join `Run` on((`Run`.`runId` = `Measurement`.`runId`))) left join `Merge` on((`Merge`.`measurementId` = `Measurement`.`measurementId`))) left join `Subtraction` on((`Subtraction`.`dataCollectionId` = `MeasurementToDataCollection`.`dataCollectionId`)));

-- --------------------------------------------------------

--
-- Structure for view `v_session`
--
DROP TABLE IF EXISTS `v_session`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_session` AS select `BLSession`.`sessionId` AS `sessionId`,`BLSession`.`expSessionPk` AS `expSessionPk`,`BLSession`.`beamLineSetupId` AS `beamLineSetupId`,`BLSession`.`proposalId` AS `proposalId`,`BLSession`.`projectCode` AS `projectCode`,`BLSession`.`startDate` AS `BLSession_startDate`,`BLSession`.`endDate` AS `BLSession_endDate`,`BLSession`.`beamLineName` AS `beamLineName`,`BLSession`.`scheduled` AS `scheduled`,`BLSession`.`nbShifts` AS `nbShifts`,`BLSession`.`comments` AS `comments`,`BLSession`.`beamLineOperator` AS `beamLineOperator`,`BLSession`.`visit_number` AS `visit_number`,`BLSession`.`bltimeStamp` AS `bltimeStamp`,`BLSession`.`usedFlag` AS `usedFlag`,`BLSession`.`sessionTitle` AS `sessionTitle`,`BLSession`.`structureDeterminations` AS `structureDeterminations`,`BLSession`.`dewarTransport` AS `dewarTransport`,`BLSession`.`databackupFrance` AS `databackupFrance`,`BLSession`.`databackupEurope` AS `databackupEurope`,`BLSession`.`operatorSiteNumber` AS `operatorSiteNumber`,`BLSession`.`lastUpdate` AS `BLSession_lastUpdate`,`BLSession`.`protectedData` AS `BLSession_protectedData`,`Proposal`.`title` AS `Proposal_title`,`Proposal`.`proposalCode` AS `Proposal_proposalCode`,`Proposal`.`proposalNumber` AS `Proposal_ProposalNumber`,`Proposal`.`proposalType` AS `Proposal_ProposalType`,`Person`.`personId` AS `Person_personId`,`Person`.`familyName` AS `Person_familyName`,`Person`.`givenName` AS `Person_givenName`,`Person`.`emailAddress` AS `Person_emailAddress` from ((`BLSession` left join `Proposal` on((`Proposal`.`proposalId` = `BLSession`.`proposalId`))) left join `Person` on((`Person`.`personId` = `Proposal`.`personId`)));

-- --------------------------------------------------------

--
-- Structure for view `v_tracking_shipment_history`
--
DROP TABLE IF EXISTS `v_tracking_shipment_history`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_tracking_shipment_history` AS select `Dewar`.`dewarId` AS `Dewar_dewarId`,`Dewar`.`code` AS `Dewar_code`,`Dewar`.`comments` AS `Dewar_comments`,`Dewar`.`dewarStatus` AS `Dewar_dewarStatus`,`Dewar`.`barCode` AS `Dewar_barCode`,`Dewar`.`firstExperimentId` AS `Dewar_firstExperimentId`,`Dewar`.`trackingNumberToSynchrotron` AS `Dewar_trackingNumberToSynchrotron`,`Dewar`.`trackingNumberFromSynchrotron` AS `Dewar_trackingNumberFromSynchrotron`,`Dewar`.`type` AS `Dewar_type`,`Shipping`.`shippingId` AS `Shipping_shippingId`,`Shipping`.`proposalId` AS `Shipping_proposalId`,`Shipping`.`shippingName` AS `Shipping_shippingName`,`Shipping`.`deliveryAgent_agentName` AS `deliveryAgent_agentName`,`Shipping`.`deliveryAgent_shippingDate` AS `Shipping_deliveryAgent_shippingDate`,`Shipping`.`deliveryAgent_deliveryDate` AS `Shipping_deliveryAgent_deliveryDate`,`Shipping`.`shippingStatus` AS `Shipping_shippingStatus`,`Shipping`.`returnCourier` AS `Shipping_returnCourier`,`Shipping`.`dateOfShippingToUser` AS `Shipping_dateOfShippingToUser`,`DewarTransportHistory`.`DewarTransportHistoryId` AS `DewarTransportHistory_DewarTransportHistoryId`,`DewarTransportHistory`.`dewarStatus` AS `DewarTransportHistory_dewarStatus`,`DewarTransportHistory`.`storageLocation` AS `DewarTransportHistory_storageLocation`,`DewarTransportHistory`.`arrivalDate` AS `DewarTransportHistory_arrivalDate` from ((`Shipping` left join `Dewar` on((`Dewar`.`shippingId` = `Shipping`.`shippingId`))) left join `DewarTransportHistory` on((`DewarTransportHistory`.`dewarId` = `Dewar`.`dewarId`)));

-- --------------------------------------------------------

--
-- Structure for view `v_week`
--
DROP TABLE IF EXISTS `v_week`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_week` AS select date_format((now() - interval 52 week),_utf8'%x-%v') AS `num` union select date_format((now() - interval 51 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 51 WEEK),'%x-%v')` union select date_format((now() - interval 50 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 50 WEEK),'%x-%v')` union select date_format((now() - interval 49 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 49 WEEK),'%x-%v')` union select date_format((now() - interval 48 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 48 WEEK),'%x-%v')` union select date_format((now() - interval 47 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 47 WEEK),'%x-%v')` union select date_format((now() - interval 46 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 46 WEEK),'%x-%v')` union select date_format((now() - interval 45 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 45 WEEK),'%x-%v')` union select date_format((now() - interval 44 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 44 WEEK),'%x-%v')` union select date_format((now() - interval 43 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 43 WEEK),'%x-%v')` union select date_format((now() - interval 42 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 42 WEEK),'%x-%v')` union select date_format((now() - interval 41 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 41 WEEK),'%x-%v')` union select date_format((now() - interval 40 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 40 WEEK),'%x-%v')` union select date_format((now() - interval 39 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 39 WEEK),'%x-%v')` union select date_format((now() - interval 38 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 38 WEEK),'%x-%v')` union select date_format((now() - interval 37 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 37 WEEK),'%x-%v')` union select date_format((now() - interval 36 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 36 WEEK),'%x-%v')` union select date_format((now() - interval 35 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 35 WEEK),'%x-%v')` union select date_format((now() - interval 34 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 34 WEEK),'%x-%v')` union select date_format((now() - interval 33 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 33 WEEK),'%x-%v')` union select date_format((now() - interval 32 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 32 WEEK),'%x-%v')` union select date_format((now() - interval 31 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 31 WEEK),'%x-%v')` union select date_format((now() - interval 30 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 30 WEEK),'%x-%v')` union select date_format((now() - interval 29 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 29 WEEK),'%x-%v')` union select date_format((now() - interval 28 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 28 WEEK),'%x-%v')` union select date_format((now() - interval 27 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 27 WEEK),'%x-%v')` union select date_format((now() - interval 26 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 26 WEEK),'%x-%v')` union select date_format((now() - interval 25 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 25 WEEK),'%x-%v')` union select date_format((now() - interval 24 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 24 WEEK),'%x-%v')` union select date_format((now() - interval 23 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 23 WEEK),'%x-%v')` union select date_format((now() - interval 22 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 22 WEEK),'%x-%v')` union select date_format((now() - interval 21 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 21 WEEK),'%x-%v')` union select date_format((now() - interval 20 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 20 WEEK),'%x-%v')` union select date_format((now() - interval 19 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 19 WEEK),'%x-%v')` union select date_format((now() - interval 18 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 18 WEEK),'%x-%v')` union select date_format((now() - interval 17 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 17 WEEK),'%x-%v')` union select date_format((now() - interval 16 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 16 WEEK),'%x-%v')` union select date_format((now() - interval 15 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 15 WEEK),'%x-%v')` union select date_format((now() - interval 14 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 14 WEEK),'%x-%v')` union select date_format((now() - interval 13 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 13 WEEK),'%x-%v')` union select date_format((now() - interval 12 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 12 WEEK),'%x-%v')` union select date_format((now() - interval 11 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 11 WEEK),'%x-%v')` union select date_format((now() - interval 10 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 10 WEEK),'%x-%v')` union select date_format((now() - interval 9 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 9 WEEK),'%x-%v')` union select date_format((now() - interval 8 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 8 WEEK),'%x-%v')` union select date_format((now() - interval 7 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 7 WEEK),'%x-%v')` union select date_format((now() - interval 6 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 6 WEEK),'%x-%v')` union select date_format((now() - interval 5 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 5 WEEK),'%x-%v')` union select date_format((now() - interval 4 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 4 WEEK),'%x-%v')` union select date_format((now() - interval 3 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 3 WEEK),'%x-%v')` union select date_format((now() - interval 2 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 2 WEEK),'%x-%v')` union select date_format((now() - interval 1 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 1 WEEK),'%x-%v')` union select date_format((now() - interval 0 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 0 WEEK),'%x-%v')`;

-- --------------------------------------------------------

--
-- Structure for view `v_weekDay`
--
DROP TABLE IF EXISTS `v_weekDay`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_weekDay` AS select date_format((now() - interval 6 day),_utf8'%Y-%m-%d') AS `day` union select date_format((now() - interval 5 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 5 DAY),'%Y-%m-%d')` union select date_format((now() - interval 4 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 4 DAY),'%Y-%m-%d')` union select date_format((now() - interval 3 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 3 DAY),'%Y-%m-%d')` union select date_format((now() - interval 2 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 2 DAY),'%Y-%m-%d')` union select date_format((now() - interval 1 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 1 DAY),'%Y-%m-%d')` union select date_format((now() - interval 0 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 0 DAY),'%Y-%m-%d')`;

-- --------------------------------------------------------

--
-- Structure for view `v_xfeFluorescenceSpectrum`
--
DROP TABLE IF EXISTS `v_xfeFluorescenceSpectrum`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_xfeFluorescenceSpectrum` AS select `XFEFluorescenceSpectrum`.`xfeFluorescenceSpectrumId` AS `xfeFluorescenceSpectrumId`,`XFEFluorescenceSpectrum`.`sessionId` AS `sessionId`,`XFEFluorescenceSpectrum`.`blSampleId` AS `blSampleId`,`XFEFluorescenceSpectrum`.`fittedDataFileFullPath` AS `fittedDataFileFullPath`,`XFEFluorescenceSpectrum`.`scanFileFullPath` AS `scanFileFullPath`,`XFEFluorescenceSpectrum`.`jpegScanFileFullPath` AS `jpegScanFileFullPath`,`XFEFluorescenceSpectrum`.`startTime` AS `startTime`,`XFEFluorescenceSpectrum`.`endTime` AS `endTime`,`XFEFluorescenceSpectrum`.`filename` AS `filename`,`XFEFluorescenceSpectrum`.`energy` AS `energy`,`XFEFluorescenceSpectrum`.`exposureTime` AS `exposureTime`,`XFEFluorescenceSpectrum`.`beamTransmission` AS `beamTransmission`,`XFEFluorescenceSpectrum`.`annotatedPymcaXfeSpectrum` AS `annotatedPymcaXfeSpectrum`,`XFEFluorescenceSpectrum`.`beamSizeVertical` AS `beamSizeVertical`,`XFEFluorescenceSpectrum`.`beamSizeHorizontal` AS `beamSizeHorizontal`,`XFEFluorescenceSpectrum`.`crystalClass` AS `crystalClass`,`XFEFluorescenceSpectrum`.`comments` AS `comments`,`XFEFluorescenceSpectrum`.`flux` AS `flux`,`XFEFluorescenceSpectrum`.`flux_end` AS `flux_end`,`XFEFluorescenceSpectrum`.`workingDirectory` AS `workingDirectory`,`BLSample`.`blSampleId` AS `BLSample_sampleId`,`BLSession`.`proposalId` AS `BLSession_proposalId` from ((`XFEFluorescenceSpectrum` left join `BLSample` on((`BLSample`.`blSampleId` = `XFEFluorescenceSpectrum`.`blSampleId`))) left join `BLSession` on((`BLSession`.`sessionId` = `XFEFluorescenceSpectrum`.`sessionId`)));

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
  ADD CONSTRAINT `protein_fk3` FOREIGN KEY (`componentTypeId`) REFERENCES `ComponentType` (`componentTypeId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Protein_ibfk_1` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`) ON DELETE CASCADE ON UPDATE CASCADE;

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
