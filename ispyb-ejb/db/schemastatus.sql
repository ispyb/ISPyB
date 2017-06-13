-- phpMyAdmin SQL Dump
-- version 3.4.11.1deb2+deb7u2
-- http://www.phpmyadmin.net
--
-- Host: pyproserv
-- Generation Time: Jun 09, 2017 at 03:32 PM
-- Server version: 5.5.40
-- PHP Version: 5.4.45-0+deb7u2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `pydb`  version tag 17.06.06
--

-- --------------------------------------------------------

--
-- Table structure for table `SchemaStatus`
--

CREATE TABLE IF NOT EXISTS `SchemaStatus` (
  `schemaStatusId` int(11) NOT NULL AUTO_INCREMENT,
  `scriptName` varchar(100) NOT NULL,
  `schemaStatus` varchar(10) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`schemaStatusId`),
  UNIQUE KEY `scriptName` (`scriptName`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=38 ;

--
-- Dumping data for table `SchemaStatus`
--

INSERT INTO `SchemaStatus` (`schemaStatusId`, `scriptName`, `schemaStatus`, `recordTimeStamp`) VALUES
(1, '2016_11_18_1_CreateComponentType', 'DONE', '2016-11-22 10:09:29'),
(2, '2016_11_18_2_CreateRobotAction', 'DONE', '2016-11-22 10:10:29'),
(3, '2016_11_22_phasing_view.sql', 'DONE', '2016-11-22 10:11:12'),
(4, '2016_11_23_alterLogin.sql', 'DONE', '2016-11-28 14:38:39'),
(5, '2016_12_07_alterLaboratory.sql', 'DONE', '2016-12-06 15:18:28'),
(6, '2016_11_25_PhasingProgramEnumeration.sql', 'DONE', '2016-12-06 15:19:02'),
(7, '2016_12_06_Added_ccAno_sigAno.sql', 'DONE', '2016-12-06 15:41:18'),
(8, '2016_12_09_Screening_view.sql', 'DONE', '2016-12-22 10:34:37'),
(9, '2016_12_16_Anomalous.sql', 'DONE', '2016-12-22 10:34:48'),
(10, '2016_12_16_SAXS_Datacollection.sql', 'DONE', '2016-12-22 10:34:59'),
(11, '2017_01_19_alterProtein.sql', 'DONE', '2017-01-19 10:04:47'),
(12, '2017_01_24_added_siteId_Login.sql', 'DONE', '2017-01-24 13:40:29'),
(13, '2017_01_24_ISA_added_to_statistics.sql', 'DONE', '2017-01-24 13:40:44'),
(14, '2017_01_24_Removed_ranking_from_view.sql', 'DONE', '2017-01-24 13:41:31'),
(15, '2017_01_24_autoprocessing_stats_view.sql', 'DONE', '2017-01-24 13:46:35'),
(16, '2017_02_02_Added_results_dataCollection.sql', 'DONE', '2017-02-07 15:43:35'),
(21, '2017_02_06_v_tracking_transport_history.sql', 'DONE', '2017-02-07 15:45:20'),
(22, '2017_02_07_v_mx_stats_add_isa.sql', 'DONE', '2017-02-07 15:52:20'),
(23, '2017_02_10_New_PhasingStep_types.sql', 'DONE', '2017-02-14 15:22:33'),
(24, '2017_02_16_Dewar_summary_proposalId_modification.sql', 'DONE', '2017-02-16 09:29:04'),
(25, '2017_02_09_Update_datacollection_with_detector.sql', 'DONE', '2017-02-21 11:23:18'),
(27, '2017_03_20_Update_v_datacollection_summary.sql', 'DONE', '2017-03-21 09:51:01'),
(28, '2017_04_05_PhasingAttachement_Enumeration.sql', 'DONE', '2017-04-05 11:02:54'),
(29, '2017_04_18_CreateDataCollectionFileAttachment.sql', 'DONE', '2017-05-02 12:31:12'),
(30, '2017_04_19_UpdateDetector.sql', 'DONE', '2017-05-02 12:33:19'),
(31, '2017_05_02_Merge_with_DLS_update.sql', 'DONE', '2017-05-02 12:47:11'),
(32, '2017_05_02_Merge_with_DLS_create.sql', 'DONE', '2017-05-02 12:47:39'),
(33, '2017_05_04_BeamlineName.sql', 'DONE', '2017-06-09 13:30:33'),
(34, '2017_05_09_Axis_for_subwedge.sql', 'DONE', '2017-06-09 13:30:56'),
(35, '2017_05_11_ProposalId_for_sample.sql', 'DONE', '2017-06-09 13:31:14'),
(36, '2017_05_19_ProposalHasPerson_AutoIncrement.sql', 'DONE', '2017-06-09 13:31:29'),
(37, '2017_06_06_ProcessingStatus_to_enumeration.sql', 'DONE', '2017-06-09 13:31:41');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
