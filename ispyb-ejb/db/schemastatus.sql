-- phpMyAdmin SQL Dump
-- version 4.6.6deb4
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Sep 11, 2019 at 10:46 AM
-- Server version: 10.1.23-MariaDB-9+deb9u1
-- PHP Version: 7.0.19-1

--ISPyB version: 5.20

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pydb`
--

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
(37, '2017_06_06_ProcessingStatus_to_enumeration.sql', 'DONE', '2017-06-09 13:31:41'),
(38, '2017_07_07_SAXS_datacollection.sql', 'DONE', '2017-07-06 08:55:23'),
(39, '2017_07_05_Update_v_datacollection_summary.sql', 'DONE', '2017-07-11 15:18:34'),
(44, '2017_07_07_view_autoproc.sql', 'DONE', '2017-07-11 15:20:16'),
(45, '2017_07_12_remove_0000_timestamp.sql', 'DONE', '2017-07-11 15:21:38'),
(50, '2017_08_31_new_workflow_types.sql', 'DONE', '2017-08-31 07:53:32'),
(51, '2017_19_09_2017_imageDirectory_biosaxs.sql', 'DONE', '2017-09-19 13:20:53'),
(52, '2017_09_21_new_diffractionplan_types.sql', 'DONE', '2017-09-21 07:27:52'),
(54, '20171022_EM.sql', 'DONE', '2017-11-23 14:14:16'),
(55, '2017_12_05_DataCollectionFileAttachment_fileType.sql', 'DONE', '2017-12-06 10:10:10'),
(57, '2017_12_05_fluorescence_maps.sql', 'ONGOING', '2017-12-06 10:13:04'),
(59, '2017_11_24_DataCollectionSummary_view.sql', 'DONE', '2017-12-06 10:16:08'),
(60, '2017_12_08_sample_mx_view_update.sql', 'ONGOING', '2017-12-08 16:49:50'),
(63, '2018_02_03_updateSessionTable.sql', 'DONE', '2018-02-01 15:15:59'),
(65, '2018_02_06_updateDewarTable.sql', 'DONE', '2018-02-06 11:44:04'),
(67, '2018_02_18_Dewar_summary_modification.sql', 'DONE', '2018-02-19 12:57:08'),
(68, '2018_02_23_AutoProcintegrationView.sql', 'DONE', '2018-02-23 10:06:22'),
(69, '2018_02_13_BLSample_subLocation.sql', 'DONE', '2018-02-27 16:02:21'),
(70, '2018_03_13_updateCollations.sql', 'DONE', '2018-03-13 09:47:07'),
(71, '2018_05_02_added_MXPRESSF.sql', 'DONE', '2018-05-02 14:29:37'),
(72, '2018_07_19_em_stats.sql', 'DONE', '2018-06-19 17:45:51'),
(73, '2018_06_25_Extend_filePath.sql', 'DONE', '2018-06-28 07:38:35'),
(74, '2018_06_28_add_EM_xpixel.sql', 'DONE', '2018-06-28 07:46:51'),
(75, '2018_06_28_Fix_month_name_scripts.sql', 'DONE', '2018-06-28 07:50:27'),
(76, '2018_06_28_added_xpixel_to_datacollection.sql', 'DONE', '2018-06-28 09:07:42'),
(77, '2018_07_06_Added_characterisation_start_angle.sql', 'DONE', '2018-07-06 13:50:36'),
(78, '2018_09_04_Added_anisotropic_fields.sql', 'DONE', '2018-09-04 12:03:01'),
(80, '2018_02_27_membranes_update.sql', 'DONE', '2018-09-10 08:08:08'),
(82, '2018_09_13_BeamCalendar.sql', 'DONE', '2018-09-24 15:41:33'),
(83, '2018_09_26_Comment_anisotropic_fields.sql', 'DONE', '2018-09-26 09:56:38'),
(84, '2018_09_26_update_pathToH5.sql', 'DONE', '2018-09-27 08:29:35'),
(85, '2018_10_26_new_admin_var.sql', 'DONE', '2018-10-25 14:22:45'),
(87, '2018_11_06_Phasing_group_name.sql', 'DONE', '2018-11-06 08:53:50'),
(88, '2018_11_12_new_admin_var.sql', 'DONE', '2018-11-12 15:53:05'),
(89, '2018_11_27_updateCollations.sql', 'DONE', '2018-11-28 10:10:06'),
(91, '2018_12_04_EnergyScan_and_XFEFluorescenceSpectrum_add_axisPosition.sql', 'DONE', '2018-12-05 08:33:06'),
(92, '2019_07_24_Add_ProposalId_to_Structure.sql', 'DONE', '2019-07-24 08:42:41'),
(93, '2018_06_29_DataCollection_imageContainerSubPath.sql', 'DONE', '2019-09-11 08:24:22'),
(94, '2018_12_06_still_data_collection.sql', 'DONE', '2019-09-11 08:33:54'),
(95, '2018_12_17_PhasingProgramRun_phasingDirectory.sql', 'DONE', '2019-09-11 08:34:09'),
(97, '2018_12_17_PhasingStatistics_new_metrics.sql', 'DONE', '2019-09-11 08:34:31'),
(98, '2018_12_18_v_phasing_phasingDirectory.sql', 'DONE', '2019-09-11 08:34:50'),
(99, '2019_01_14_Proposal_state.sql', 'DONE', '2019-09-11 08:35:10'),
(100, '2019_01_15_Detector_localName.sql', 'DONE', '2019-09-11 08:35:34');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `SchemaStatus`
--
ALTER TABLE `SchemaStatus`
  ADD PRIMARY KEY (`schemaStatusId`),
  ADD UNIQUE KEY `scriptName` (`scriptName`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `SchemaStatus`
--
ALTER TABLE `SchemaStatus`
  MODIFY `schemaStatusId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=101;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
