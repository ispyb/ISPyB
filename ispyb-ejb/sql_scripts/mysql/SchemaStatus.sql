-- phpMyAdmin SQL Dump
-- version 3.4.11.1deb2+deb7u2
-- http://www.phpmyadmin.net
--
-- Host: pyproserv
-- Generation Time: Dec 22, 2016 at 05:45 PM
-- Server version: 5.5.40
-- PHP Version: 5.4.45-0+deb7u2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `pydb`  ISPYB_4.1.5
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

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
(10, '2016_12_16_SAXS_Datacollection.sql', 'ONGOING', '2016-12-22 10:34:59');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
