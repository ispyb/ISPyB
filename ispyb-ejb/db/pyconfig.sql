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
-- version 3.3.7deb7
-- http://www.phpmyadmin.net
--
-- Host: pydevserv:3308
-- Generation Time: Oct 01, 2015 at 04:35 PM
-- Server version: 5.5.40
-- PHP Version: 5.3.3-7+squeeze18

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `pyconfig`
--

-- --------------------------------------------------------

--
-- Table structure for table `Menu`
--

CREATE TABLE IF NOT EXISTS `Menu` (
  `menuId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `parentId` int(10) unsigned DEFAULT NULL,
  `name` varchar(45) NOT NULL DEFAULT '',
  `action` varchar(255) DEFAULT NULL,
  `sequence` int(10) unsigned DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `expType` varchar(10) DEFAULT 'MB' COMMENT 'experiment type : MX or BX or MB',
  PRIMARY KEY (`menuId`),
  KEY `Menu_FKIndex1` (`parentId`),
  KEY `Menu_FKIndexExpType` (`expType`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5014 ;

--
-- Dumping data for table `Menu`
--

INSERT INTO `Menu` (`menuId`, `parentId`, `name`, `action`, `sequence`, `description`, `expType`) VALUES
(1, NULL, 'Help', '/user/help.do', 11, 'Main help for guest/helpMainGuestPage.do', 'MB'),
(2, 1, 'What is ISPyB?', '/helpWhatGuestPage.do', 1, NULL, 'MX'),
(3, 1, 'ISPyB for User', '/helpUserGuestPage.do', 2, NULL, 'MX'),
(4, 1, 'ISPyB for industrial', '/helpIndustrialGuestPage.do', 3, NULL, 'MX'),
(5, NULL, 'Shipment', '/helpShippingGuestPage.do', 2, NULL, 'MX'),
(6, 5, 'Create', NULL, 1, NULL, 'MX'),
(7, 33, 'ShipmentsOld', '/user/viewShippingAction.do?reqCode=display', 1, 'View all Shippings for this proposal', 'MX'),
(8, 41, 'Dewar', '/reader/genericDewarAction.do?reqCode=initSearch', 2, 'Search dewar form', 'MX'),
(9, 999, 'Report', '', 5, 'previously assigned to 5', 'MX'),
(10, NULL, 'Samples', '/user/viewSampleHome.do', 3, 'View all samples attached to this user', 'MX'),
(11, 28, 'All samples', '/user/viewSample.do?reqCode=displayFromMenu', 3, 'List of all samples for this proposal', 'MX'),
(12, 10, 'Update Database', '/updateDB.do?reqCode=userDisplay', 6, NULL, 'MX'),
(13, 999, 'View samplesheet', '/user/viewProteinFromSS.do?reqCode=display', 1, 'Previously assigned to 12', 'MX'),
(14, 999, 'Submit samplesheet', NULL, 2, 'Previously assigned to 12', 'MX'),
(15, 999, 'Report', NULL, 7, 'Build reports (previously assigned to 10)', 'MX'),
(16, NULL, 'Data collection', '/user/viewSession.do?reqCode=displayLast', 5, 'Data collection menu for User', 'MX'),
(17, 16, 'View all sessions', '/user/viewSession.do?reqCode=display', 2, 'View all sessions for User', 'MX'),
(18, 999, 'Report', NULL, 3, 'previously assigned to 16', 'MX'),
(19, 999, 'Reports', '/user/results.do', 5, 'to be added later', 'MX'),
(20, 19, 'Reports (HTML, PDF)', NULL, 3, NULL, 'MX'),
(21, 19, 'Export (XML)', NULL, 4, NULL, 'MX'),
(22, 0, 'Data Collections', '/industrial/viewSession.do', 3, 'View sessions for industrial', 'MX'),
(23, 28, 'Proteins and crystal forms', '/user/viewProteinAndCrystal.do?reqCode=display', 1, 'View proteins with crystaltypes and diffraction plans', 'MX'),
(25, 28, 'Samples and diffraction plans', '/user/viewSample.do?reqCode=displayWithDiffractionPlan', 0, 'View samples&diffraction plans attached', 'MX'),
(26, 28, 'Protein with all parameters', '/user/viewProteinForProposal.do', 0, 'Protein with all parameters', 'MX'),
(27, 16, 'Search data collections', '/user/searchDataCollection.do', 3, 'Search data collections', 'MX'),
(28, 10, 'View', NULL, 2, 'List of all samples for this proposal', 'MX'),
(30, 10, 'Create', NULL, 1, NULL, 'MX'),
(31, 30, 'New sample', '/user/createSampleAction.do?reqCode=displayFromMenu#CreateSample', 2, 'Create Sample', 'MX'),
(32, 30, 'New crystal form', '/user/createCrystal.do?reqCode=displayFromMenu', 2, 'Create Crystal', 'MX'),
(33, 5, 'View', NULL, 2, NULL, 'MX'),
(35, 51, 'Samples in Sample Changer', 'user/viewSample.do?reqCode=displayForSampleChanger', 4, 'Samples in Sample Changer', 'MX'),
(36, 6, 'Upload from file', '/helpUploadGuestPage.do', 3, 'Upload from XLS', 'MX'),
(37, 6, 'Shipment', '/user/createShippingAction.do?reqCode=display', 1, NULL, 'MX'),
(38, 10, 'Search', NULL, 3, NULL, 'MX'),
(39, 38, 'Protein', '/user/searchProtein.do', 1, 'Search form for protein', 'MX'),
(40, 38, 'Sample', '/user/searchSample.do', 2, 'Search form for sample name', 'MX'),
(41, 5, 'Search', NULL, 3, NULL, 'MX'),
(42, 41, 'Shipment', '/reader/genericShippingAction.do?reqCode=initSearch', 1, 'search shipping form', 'MX'),
(44, 6, 'Container', '/user/createContainerAction.do?reqCode=display', 2, NULL, 'MX'),
(46, 30, 'New container', '/user/createContainerAction.do?reqCode=display', 1, 'create a container for sample menu', 'MX'),
(47, 33, 'Unassigned Containers', '/user/viewDewarAction.do?reqCode=displayFreeContainers', 2, 'Containers not linked to a shipment', 'MX'),
(48, 28, 'Unassigned Samples', '/user/viewSample.do?reqCode=displayFree', 2, NULL, 'MX'),
(49, 19, 'Analyse samples', '/user/analyseSample.do?reqCode=displayFromMenu', 1, NULL, 'MX'),
(51, NULL, 'Prepare experiment', '/helpPrepareGuestPage.do', 4, NULL, 'MX'),
(52, 51, 'Select samples', '/user/prepareExpSample.do?reqCode=selectSample', 3, NULL, 'MX'),
(53, 206, 'Last dewars', '/user/prepareExp.do?reqCode=selectDewar&selectAll=false&viewSelected=false', 1, NULL, 'MX'),
(54, 51, 'Fill sample changer', '/user/fillSampleChanger.do?reqCode=display', 2, NULL, 'MX'),
(55, NULL, 'Help', '/industrial/help.do', 11, 'help for industrial: /industrial/welcomeIndustrial.do', 'MX'),
(56, NULL, 'Feedback', '/user/SendMailAction.do?reqCode=display', 6, 'Submit your feedback', 'MX'),
(57, NULL, 'Proposals', '/fedexmanager/viewProposal.do?reqCode=display', 3, 'view all FX proposals', 'MX'),
(58, NULL, 'Shipments', '/user/viewShippingAction.do?reqCode=displayAllFX', 2, 'view all FX shipments', 'MX'),
(60, NULL, 'Update ISPyB database', '/updateDB.do?reqCode=display', 7, NULL, 'MX'),
(61, 16, 'View last sessions', '/user/viewSession.do?reqCode=displayLast', 1, 'View only last sessions', 'MX'),
(62, NULL, 'Proposals', '/manager/viewProposal.do?reqCode=displayAll', 3, 'view all proposals', 'MX'),
(63, NULL, 'Shipments', '/manager/viewShipmentManager.do?reqCode=displayAll', 2, 'view all shipments', 'MX'),
(65, NULL, 'Lab-contacts', '/user/viewLabContactAction.do?reqCode=display', 1, 'View all Lab-contacts cards', 'MB'),
(66, 69, 'LabContact', '/user/loginLabContactAction.do?reqCode=loginDisplay', 1, 'Create a new lab-contact card', 'MB'),
(68, 70, 'LabContacts', '/user/viewLabContactAction.do?reqCode=display', 1, 'View all lab-contact cards', 'MB'),
(69, 65, 'Create', NULL, 1, NULL, 'MB'),
(70, 65, 'View', NULL, 2, NULL, 'MB'),
(71, 33, 'Dewars', '/user/viewDewarAction.do?reqCode=display', 2, 'View all Dewars for this proposal', 'MX'),
(72, 70, 'View all (Store view)', '/user/viewLabContactAction.do?reqCode=display', 2, 'View all lab-contact cards', 'MX'),
(73, 84, 'To be received', '/reader/genericDewarAction.do?reqCode=toBeReceived', 3, 'Search dewar to be received', 'MX'),
(74, 84, 'To be delivered', '/reader/genericDewarAction.do?reqCode=toBeDelivered', 4, 'Search dewar to be delivered', 'MX'),
(75, 84, 'To be picked up', '/reader/genericDewarAction.do?reqCode=toBePickedUp', 5, 'Search dewar to be picked up', 'MX'),
(76, 33, 'Shipments', '/reader/genericShippingAction.do?reqCode=display', 1, 'View all Shippings', 'MX'),
(77, 33, 'Dewars', '/reader/genericDewarAction.do?reqCode=display', 2, 'View all Dewars', 'MX'),
(78, 80, 'All LabContacts', '/store/viewLabContactAction.do?reqCode=displayAll', 1, 'View all lab-contact cards', 'MX'),
(79, NULL, 'Lab-contacts', '/store/viewLabContactAction.do?reqCode=displayAll', 1, 'View all Lab-contacts cards', 'MX'),
(80, 79, 'View', NULL, 1, NULL, 'MX'),
(81, NULL, 'Shipment', '/reader/genericShippingAction.do?reqCode=display', 3, NULL, 'MX'),
(83, 81, 'View', NULL, 2, NULL, 'MX'),
(84, 81, 'Search', NULL, 3, NULL, 'MX'),
(85, 83, 'Shipments', '/reader/genericShippingAction.do?reqCode=display', 1, 'View all Shippings', 'MX'),
(86, 83, 'Dewars', '/reader/genericDewarAction.do?reqCode=display', 2, 'View all Dewars', 'MX'),
(87, 84, 'Shipment', '/reader/genericShippingAction.do?reqCode=initSearch', 1, 'Search shipment', 'MX'),
(88, 84, 'Dewar', '/reader/genericDewarAction.do?reqCode=initSearch', 2, 'Search dewar', 'MX'),
(89, 84, 'Sent to user', '/reader/genericDewarAction.do?reqCode=sentToUser', 6, 'Search dewar sent to users', 'MX'),
(90, NULL, 'Dashboard', '/manager/adminSite.do?reqCode=display', 7, 'Site administration', 'MX'),
(91, 90, 'Who''s online', '/manager/adminSite.do?reqCode=displayOnlineUsers', 1, 'List of connected users', 'MX'),
(92, 90, 'User messages', '/manager/adminSite.do?reqCode=displayMessages', 2, 'User messages administration', 'MX'),
(93, 90, 'Charts', NULL, 3, 'Reports', 'MX'),
(94, 93, 'Logins/week', '/manager/adminSite.do?reqCode=displayChart&chartView=v_logonByWeek&chartType=stacked column&chartTitle=Number of logins per week&chartUnit=Logins', 1, 'chartType=bar, area, line,stacked column, stacked area', 'MX'),
(95, 93, 'Logins/day month', '/manager/adminSite.do?reqCode=displayChart&chartView=v_logonByMonthDay&chartType=stacked column&chartTitle=Number of logins per day&chartUnit=Logins', 2, NULL, 'MX'),
(96, 93, 'Logins/day week', '/manager/adminSite.do?reqCode=displayChart&chartView=v_logonByWeekDay&chartType=stacked column&chartTitle=Number of logins per day&chartUnit=Logins', 3, NULL, 'MX'),
(97, 93, 'Logins/hour', '/manager/adminSite.do?reqCode=displayChart&chartView=v_logonByHour&chartType=stacked column&chartTitle=Number of logins per hour&chartUnit=Logins', 4, NULL, 'MX'),
(98, 93, 'Dewars/week', '/manager/adminSite.do?reqCode=displayChart&chartView=v_dewarByWeek&chartType=stacked column&chartTitle=Number of dewars per week&chartUnit=Dewars', 5, '', 'MX'),
(99, 93, 'Dewars/proposal', '/manager/adminSite.do?reqCode=displayChart&chartView=v_dewarProposalCodeByWeek&chartType=stacked column&chartTitle=Number of dewars per proposal code and per week&chartUnit=Dewars', 6, NULL, 'MX'),
(100, 93, 'Dewars/beamline', '/manager/adminSite.do?reqCode=displayChart&chartView=v_dewarBeamlineByWeek&chartType=stacked column&chartTitle=Number of dewars per beamline and per week&chartUnit=Dewars', 6, NULL, 'MX'),
(101, 93, 'Proposal %', '/manager/adminSite.do?reqCode=displayChart&chartView=v_dewarProposalCode&chartType=pie&chartTitle=Number of dewars per proposal code&chartUnit=Dewars', 8, NULL, 'MX'),
(102, 93, 'Beamline %', '/manager/adminSite.do?reqCode=displayChart&chartView=v_dewarBeamline&chartType=pie&chartTitle=Number of dewars per beamline&chartUnit=Dewars', 8, NULL, 'MX'),
(103, 93, 'Samples/week', '/manager/adminSite.do?reqCode=displayChart&chartView=v_sampleByWeek&chartType=line&chartTitle=Number of samples per week&chartUnit=Samples', 8, '', 'MX'),
(104, 93, 'DataCollections/week', '/manager/adminSite.do?reqCode=displayChart&chartView=v_dataCollectionByWeek&chartType=stacked column&chartTitle=Number of Data Collections (>4 images) per week&chartUnit=Data Collections', 11, NULL, 'MX'),
(201, NULL, 'References', '/viewReference.do?reqCode=display', 9, 'References', 'MX'),
(202, 6, 'Puck', '/user/createPuckAction.do?reqCode=display', 2, 'create simple puck', 'MX'),
(206, 51, 'Select Dewars', NULL, 1, NULL, 'MX'),
(207, 51, 'View', NULL, 3, NULL, 'MX'),
(210, 62, 'View all proposals', '/manager/viewProposal.do?reqCode=displayAll&showAll=yes', 1, 'View all proposals', 'MX'),
(211, 62, 'Update proposal id', '/manager/updateProposalId.do?reqCode=display', 2, 'Update proposal id', 'MX'),
(212, 90, 'Session', '/user/viewSession.do?reqCode=display', 4, 'Sessions planned today', 'MX'),
(213, 206, 'All dewars', '/user/prepareExp.do?reqCode=selectDewar&selectAll=true&viewSelected=false', 2, 'Displays all dewars : may be too long', 'MX'),
(214, 6, 'CSV upload', '/user/createShippingFileAction.do?reqCode=display', 3, 'upload from csv', 'MX'),
(215, 207, 'Selected dewars', '/user/prepareExp.do?reqCode=selectDewar&selectAll=true&viewSelected=true', 1, 'View selected dewars', 'MX'),
(220, 90, 'Last Collects', '/user/viewDataCollection.do?reqCode=displayLastCollect', 5, 'View Last Collects', 'MX'),
(221, 90, 'Data Protection Log', '/user/dataConfidentialityAction.do?reqCode=display', 6, 'view data confidentiality log', 'MX'),
(998, NULL, 'Home', '/user/welcomePerson.do?reqCode=display', 0, 'Home for person', 'MX'),
(999, NULL, 'Help', 'http://www.esrf.fr/UsersAndScience/Experiments/MX/How_to_use_our_beamlines/ISPYB/ISPYBhelp/', 2, 'Help for unlogged users', 'MX'),
(1006, NULL, 'Prepare Experiment', '/user/viewProjectList.do?reqCode=display&menu=prepareexperiment', 4, '', 'BX'),
(1007, 1006, 'Macromolecules', '/user/viewProjectList.do?reqCode=display&menu=macromolecules', 1, NULL, 'BX'),
(1008, 1006, 'Buffers', '/user/viewProjectList.do?reqCode=display&menu=buffers', 2, '', 'BX'),
(1009, NULL, 'Data Acquisition', '/user/viewProjectList.do?reqCode=display', 5, '', 'BX'),
(1010, 1009, 'Acquisition', '/user/viewProjectList.do?reqCode=display', 1, NULL, 'BX'),
(1011, 1010, 'All', '/user/viewProjectList.do?reqCode=display', 5, '', 'BX'),
(5000, NULL, 'Explore Your Results', '/user/viewProjectList.do?reqCode=display&menu=results', 6, NULL, 'BX'),
(5001, NULL, 'Home', '/user/welcomeUser.do', 0, NULL, 'BX'),
(5004, NULL, 'Feedback', '/user/SendMailAction.do?reqCode=display', 8, NULL, 'BX'),
(5005, NULL, 'Shipping', '/user/viewProjectList.do?reqCode=display&menu=list_shipment', 3, NULL, 'BX'),
(5008, 1010, 'Sample Changer', '/user/viewProjectList.do?reqCode=display&menu=samplechanger', 1, NULL, 'BX'),
(5009, 1010, 'HPLC', '/user/viewProjectList.do?reqCode=display&menu=hplc', 2, NULL, 'BX'),
(5010, 1010, 'Calibration', '/user/viewProjectList.do?reqCode=display&menu=calibration', 3, NULL, 'BX'),
(5011, 1006, 'Experiments', '/user/viewProjectList.do?reqCode=display&menu=templates', 3, NULL, 'BX'),
(5012, 5005, 'Stock Solutions', '/user/viewProjectList.do?reqCode=display&menu=stocksolutions', 1, NULL, 'BX'),
(5013, 5005, 'Shipments', '/user/viewProjectList.do?reqCode=display&menu=shipments', 2, NULL, 'BX');

-- --------------------------------------------------------

--
-- Table structure for table `MenuGroup`
--

CREATE TABLE IF NOT EXISTS `MenuGroup` (
  `menuGroupId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL DEFAULT '',
  `welcomePage` varchar(45) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`menuGroupId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=100 ;

--
-- Dumping data for table `MenuGroup`
--

INSERT INTO `MenuGroup` (`menuGroupId`, `name`, `welcomePage`, `description`) VALUES
(1, 'User', '/user/welcomeUser.do', 'Welcome page for user group'),
(2, 'Guest', '/welcomeGuestPage.do', 'Welcome page for all unlogged users'),
(3, 'Manager', '/manager/welcomeManager.do', 'Welcome page to Manager'),
(4, 'Industrial', '/industrial/welcomeIndustrial.do', 'Welcome page to Industrial'),
(5, 'Fedexmanager', '/fedexmanager/welcomeFedexmanager.do', 'Welcome page to Fedexmanager'),
(6, 'Localcontact', '/localcontact/welcomeLocalcontact.do', 'Welcome page to Localcontact'),
(7, 'Blom', '/blom//welcomeBlom.do', 'Welcome page to Blom'),
(8, 'Store', '/store/welcomeStore.do', 'Welcome page to Store'),
(9, 'WebService', '/webservice/welcomeWebService.do', NULL),
(99, 'Not used now', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `Menu_has_MenuGroup`
--

CREATE TABLE IF NOT EXISTS `Menu_has_MenuGroup` (
  `menuId` int(10) unsigned NOT NULL DEFAULT '0',
  `menuGroupId` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`menuId`,`menuGroupId`),
  KEY `Menu_has_MenuGroup_FKINDEX1` (`menuId`),
  KEY `Menu_has_MenuGroup_FKINDEX2` (`menuGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Menu_has_MenuGroup`
--

INSERT INTO `Menu_has_MenuGroup` (`menuId`, `menuGroupId`) VALUES
(1, 1),
(2, 1),
(2, 2),
(2, 9),
(3, 2),
(3, 9),
(4, 2),
(4, 9),
(5, 1),
(5, 4),
(6, 1),
(6, 4),
(8, 1),
(8, 4),
(9, 1),
(10, 1),
(10, 4),
(12, 1),
(12, 4),
(13, 1),
(13, 4),
(16, 1),
(16, 4),
(16, 6),
(17, 1),
(17, 4),
(17, 6),
(18, 1),
(20, 1),
(21, 1),
(23, 1),
(23, 4),
(25, 99),
(26, 99),
(27, 1),
(27, 4),
(28, 1),
(28, 4),
(30, 1),
(30, 4),
(31, 1),
(31, 4),
(32, 1),
(32, 4),
(33, 1),
(33, 4),
(37, 1),
(37, 4),
(38, 1),
(38, 4),
(39, 1),
(39, 4),
(40, 1),
(40, 4),
(41, 1),
(41, 4),
(42, 1),
(42, 4),
(46, 99),
(49, 1),
(49, 4),
(51, 1),
(53, 1),
(54, 1),
(55, 4),
(56, 1),
(56, 3),
(56, 4),
(56, 6),
(57, 5),
(58, 5),
(60, 3),
(60, 5),
(60, 6),
(61, 1),
(61, 4),
(61, 6),
(62, 3),
(65, 1),
(65, 4),
(66, 1),
(66, 4),
(68, 1),
(68, 4),
(69, 1),
(69, 4),
(70, 1),
(70, 4),
(73, 8),
(74, 8),
(75, 8),
(76, 1),
(76, 4),
(77, 1),
(77, 4),
(78, 8),
(79, 8),
(80, 8),
(81, 3),
(81, 5),
(81, 6),
(81, 7),
(81, 8),
(83, 3),
(83, 5),
(83, 6),
(83, 7),
(83, 8),
(84, 3),
(84, 6),
(84, 7),
(84, 8),
(85, 3),
(85, 5),
(85, 6),
(85, 7),
(85, 8),
(86, 3),
(86, 5),
(86, 6),
(86, 7),
(86, 8),
(87, 3),
(87, 6),
(87, 7),
(88, 3),
(88, 5),
(88, 6),
(88, 7),
(88, 8),
(89, 8),
(90, 3),
(90, 6),
(90, 7),
(91, 3),
(91, 7),
(92, 3),
(93, 3),
(93, 7),
(94, 3),
(94, 7),
(95, 3),
(95, 7),
(96, 3),
(96, 7),
(97, 3),
(97, 7),
(98, 3),
(98, 7),
(99, 3),
(99, 7),
(100, 3),
(100, 7),
(101, 3),
(101, 7),
(102, 3),
(102, 7),
(103, 3),
(103, 7),
(201, 1),
(201, 4),
(202, 1),
(202, 4),
(206, 1),
(207, 1),
(210, 3),
(211, 3),
(212, 3),
(213, 1),
(213, 4),
(213, 5),
(214, 1),
(214, 4),
(214, 5),
(215, 1),
(220, 3),
(221, 3),
(221, 6),
(221, 7),
(998, 1),
(998, 4),
(999, 2),
(999, 9),
(1006, 1),
(1006, 4),
(1007, 1),
(1007, 4),
(1008, 1),
(1008, 4),
(1009, 1),
(1009, 4),
(1010, 1),
(1010, 4),
(1011, 1),
(1011, 4),
(5000, 1),
(5000, 4),
(5001, 1),
(5001, 4),
(5004, 1),
(5004, 4),
(5005, 1),
(5005, 4),
(5008, 1),
(5008, 4),
(5009, 1),
(5009, 4),
(5010, 1),
(5010, 4),
(5011, 1),
(5011, 4),
(5012, 1),
(5012, 4),
(5013, 1),
(5013, 4);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Menu_has_MenuGroup`
--
ALTER TABLE `Menu_has_MenuGroup`
  ADD CONSTRAINT `Menu_has_MenuGroup_ibfk_1` FOREIGN KEY (`menuId`) REFERENCES `Menu` (`menuId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `Menu_has_MenuGroup_ibfk_2` FOREIGN KEY (`menuGroupId`) REFERENCES `MenuGroup` (`menuGroupId`) ON DELETE NO ACTION ON UPDATE NO ACTION;
