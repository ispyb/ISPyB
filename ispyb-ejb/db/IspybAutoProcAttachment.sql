/*******************************************************************************
 * This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ISPyB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, 
 * P. Brenchereau, M. Bodin, A. De Maria Antolinos, O. Svensson, I. Karpics, 
 * A. Nardella, J. Lewis Muir, R. Fogh, D. Sanchez, I. Chado.
 *********************************************************************************/

-- phpMyAdmin SQL Dump
-- version 4.6.6deb4
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Mar 05, 2018 at 09:43 AM
-- Server version: 10.1.23-MariaDB-9+deb9u1
-- PHP Version: 7.0.19-1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pydb` version 5.4.5
--

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

--
-- Dumping data for table `IspybAutoProcAttachment`
--

INSERT INTO `IspybAutoProcAttachment` (`autoProcAttachmentId`, `fileName`, `description`, `step`, `fileCategory`, `hasGraph`) VALUES
(1, 'merged_anom_sc2mtz.mtz.gz', 'Merged data, anomalous pairs treated separately, imported via Scalepack2MTZ', 'TRUNCATE', 'output', 0),
(2, 'merged_noanom_sc2mtz.mtz.gz', 'Merged data, anomalous pairs treated as equivalent imported via Scalepack2MTZ', 'TRUNCATE', 'output', 0),
(3, 'merged_anom_pointless_multirecord.mtz.gz', 'Merged data, anomalous pairs treated separately, imported via pointless, in multirecord MTZ format (for import into SCALA, for example)', 'SCALA', 'output', 0),
(4, 'merged_noanom_pointless_multirecord.mtz.gz', 'Merged data, anomalous pairs treated as equivalent imported via pointless, in multirecord MTZ format (for import into SCALA, for example)', 'SCALA', 'output', 0),
(5, 'merged_anom_pointless.mtz.gz', 'Merged data, anomalous pairs treated separately, imported via pointless, in MTZ format after SCALA', 'SCALA', 'output', 0),
(6, 'merged_noanom_pointless.mtz.gz', 'Merged data, anomalous pairs treated as equivalent, imported via pointless, in MTZ format after SCALA', 'SCALA', 'output', 0),
(7, 'merged_anom_pointless.mtz', 'CCP4 MTZ file, imported via POINTLESS, SCALA and TRUNCATE, anomalous pairs merged ', 'SCALA', 'output', 0),
(8, 'merged_noanom_pointless.mtz', 'CCP4 MTZ file, imported via POINTLESS, SCALA and TRUNCATE, anomalous pairs unmerged', 'SCALA', 'output', 0),
(9, 'merged_anom.sca.gz', 'Merged data, anomalous pairs treated separately, SCALEPACK format', 'SCALEPACK', 'output', 0),
(10, 'merged_noanom.sca.gz', 'Merged data, anomalous pairs treated as equivalent, SCALEPACK format', 'SCALEPACK', 'output', 0),
(11, 'unmerged_anom.sca.gz', 'Unmerged data, anomalous pairs treated separately, SCALEPACK format', 'SCALEPACK', 'output', 0),
(12, 'unmerged_noanom.sca.gz', 'Unmerged data, anomalous pairs treated as equivalent, SCALEPACK format', 'SCALEPACK', 'output', 0),
(13, 'merged_anom_pointless.mtz_scala.log', 'Logfile for Merged data, anomalous pairs treated separately, imported via pointless, in MTZ format after SCALA', 'SCALA', 'log', 0),
(14, 'merged_noanom_pointless.mtz_scala.log', 'Logfile for Merged data, anomalous pairs treated as equivalent, imported via pointless, in MTZ format after SCALA', 'SCALA', 'log', 0),
(15, 'merged_anom_XSCALE.LP', 'XSCALE log file, anomalous pairs treated separately', 'XSCALE', 'log', 1),
(16, 'merged_noanom_XSCALE.LP', 'XSCALE log file, anomalous pairs treated as equivalent', 'XSCALE', 'log', 1),
(17, 'scala_anom.inp', 'SCALA input file, anomalous pairs unmerged', 'XSCALE', 'input', 0),
(18, 'scala_noanom.inp', 'SCALA input file, anomalous pairs merged', 'XSCALE', 'input', 0),
(19, 'XDS.INP', 'XDS input file', 'XDS', 'input', 0),
(24, 'edna_INTEGRATE.LP', 'INTEGRATE.LP', 'XDS', 'log', 0),
(25, 'edna_successful_XDS.INP', 'XDS input for successful indexing and integration', 'XDS', 'input', 0),
(26, 'edna_input_XDS.INP', 'XDS original input file', 'XDS', 'input', 0),
(27, 'edna_merged_anom_XSCALE.LP', 'XSCALE log with MERGE= TRUE and FRIEDEL\'S_LAW= FALSE', 'XSCALE', 'log', 1),
(28, 'edna_merged_noanom_XSCALE.LP', 'XSCALE log with MERGE= TRUE and FRIEDEL\'S_LAW= TRUE', 'XSCALE', 'log', 1),
(29, 'edna_unmerged_anom_XSCALE.LP', 'XSCALE log with MERGE= FALSE and FRIEDEL\'S_LAW= FALSE', 'XSCALE', 'log', 1),
(30, 'edna_unmerged_noanom_XSCALE.LP', 'XSCALE log with MERGE= FALSE and FRIEDEL\'S_LAW= TRUE', 'XSCALE', 'log', 1),
(31, 'XDS_ASCII.HKL', 'XDS_ASCII output from CORRECT and FRIEDEL\'S_LAW= TRUE', 'XDS', 'output', 0),
(32, 'XDS_ANOM.HKL', 'XDS_ASCII output from CORRECT and FRIEDEL\'S_LAW= FALSE', 'XDS', 'output', 0),
(33, 'INTEGRATE.HKL', 'INTEGRATE.HKL output from INTEGRATE', 'XDS', 'output', 0),
(34, 'edna_aimless_anom.log', 'aimless log (from XSCALE with anom)', 'SCALA', 'log', 0),
(35, 'edna_aimless_noanom.log', 'aimless log (from XSCALE with noanom)', 'SCALA', 'log', 0),
(36, 'edna_anom_aimless.mtz', 'mtz from aimless (from XSCALE with anom)', 'SCALA', 'output', 0),
(37, 'edna_noanom_aimless.mtz', 'mtz from aimless (from XSCALE with noanom)', 'SCALA', 'output', 0),
(38, 'edna_anom_truncate.mtz', 'mtz from truncate (from XSCALE with anom)', 'TRUNCATE', 'output', 0),
(39, 'edna_noanom_truncate.mtz', 'mtz from truncate (from XSCALE with noanom)', 'TRUNCATE', 'output', 0),
(40, 'edna_truncate_anom.log', 'truncate log (from XSCALE with anom)', 'TRUNCATE', 'log', 1),
(41, 'edna_truncate_noanom.log', 'truncate log (from XSCALE with noanom)', 'TRUNCATE', 'log', 1),
(42, 'edna_unmerged_anom_pointless_multirecord.mtz', 'unmerged multirecord from pointless with anom', 'SCALA', 'output', 0),
(43, 'edna_unmerged_noanom_pointless_multirecord.mtz', 'unmerged multirecord from pointless with noanom', 'SCALA', 'output', 0),
(44, 'INTEGRATE.LP', 'INTEGRATE.LP', 'XDS', 'log', 0),
(45, 'merged_noanom.truncate.log', 'truncate log', 'TRUNCATE', 'log', 1),
(46, 'merged_anom.truncate.log', 'truncate log', 'TRUNCATE', 'log', 1),
(47, 'merged_anom.sca.truncate.log', 'truncate log', 'TRUNCATE', 'log', 1),
(48, 'merged_noanom.sca.truncate.log', 'truncate log', 'TRUNCATE', 'log', 1),
(49, 'edna_anom_aimless.inp', 'aimless input', 'SCALA', 'input', 0),
(50, 'edna_noanom_aimless.inp', 'aimless input', 'SCALA', 'input', 0),
(51, 'edna_noanom_aimless_unmerged.mtz.gz', 'aimless unmerged', 'SCALA', 'output', 0),
(52, 'edna_anom_aimless_unmerged.mtz.gz', 'aimless unmerged', 'SCALA', 'output', 0),
(53, 'dimple.png', 'image for dimple', 'DIMPLE', 'output', 0),
(54, 'dimple.log', 'log for dimple', 'DIMPLE', 'log', 0),
(55, 'dimple.pdf', 'log for dimple', 'DIMPLE', 'log', 0),
(56, 'dimple.mtz', 'mtz file for dimple', 'DIMPLE', 'output', 0),
(57, 'edna_unmerged_noanom_pointless_multirecord.mtz.gz', 'unmerged multirecord from pointless with noanom', 'SCALA', 'output', 0),
(58, 'dimple.pdb', 'pdb file for dimple', 'DIMPLE', 'output', 0),
(59, 'xtriage_noanom.log', 'log file for xtriage', 'SCALEPACK', 'log', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `IspybAutoProcAttachment`
--
ALTER TABLE `IspybAutoProcAttachment`
  ADD PRIMARY KEY (`autoProcAttachmentId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `IspybAutoProcAttachment`
--
ALTER TABLE `IspybAutoProcAttachment`
  MODIFY `autoProcAttachmentId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=60;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
