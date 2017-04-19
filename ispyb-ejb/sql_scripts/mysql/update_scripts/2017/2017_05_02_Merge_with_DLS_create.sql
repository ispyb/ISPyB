insert into SchemaStatus (scriptName, schemaStatus) values ('2017_05_02_Merge_with_DLS_create.sql','ONGOING');

/** Documentation needed DLS **/
DROP TABLE IF EXISTS `Aperture`;
CREATE TABLE `Aperture` (
  `apertureId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `sizeX` float DEFAULT NULL,
  PRIMARY KEY (`apertureId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `BF_automationError`
--

DROP TABLE IF EXISTS `BF_automationError`;
                                                                                   
CREATE TABLE `BF_automationError` (
  `automationErrorId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `errorType` varchar(40) NOT NULL,
  `solution` text,
  PRIMARY KEY (`automationErrorId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `BF_automationFault`
--

DROP TABLE IF EXISTS `BF_automationFault`;
                                         
CREATE TABLE `BF_automationFault` (
  `automationFaultId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `automationErrorId` int(10) UNSIGNED DEFAULT NULL,
  `containerId` int(10) UNSIGNED DEFAULT NULL,
  `severity` ENUM('1','2','3') DEFAULT NULL,
  `stacktrace` text,
  `resolved` tinyint(1) DEFAULT NULL,
  `faultTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`automationFaultId`),
  KEY `BF_automationFault_ibfk1` (`automationErrorId`),
  KEY `BF_automationFault_ibfk2` (`containerId`),
  CONSTRAINT `BF_automationFault_ibfk1` FOREIGN KEY (`automationErrorId`) REFERENCES `BF_automationError` (`automationErrorId`),
  CONSTRAINT `BF_automationFault_ibfk2` FOREIGN KEY (`containerId`) REFERENCES `Container` (`containerId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `BF_system`
--

DROP TABLE IF EXISTS `BF_system`;
CREATE TABLE `BF_system` (
  `systemId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`systemId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

--
-- Table structure for table `BF_component`
--

DROP TABLE IF EXISTS `BF_component`;

CREATE TABLE `BF_component` (
  `componentId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `systemId` int(10) UNSIGNED DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`componentId`),
  KEY `bf_component_FK1` (`systemId`),
  CONSTRAINT `bf_component_FK1` FOREIGN KEY (`systemId`) REFERENCES `BF_system` (`SYSTEMID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `BF_component_beamline`
--

DROP TABLE IF EXISTS `BF_component_beamline`;
CREATE TABLE `BF_component_beamline` (
  `component_beamlineId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `componentId` int(10) UNSIGNED DEFAULT NULL,
  `beamlinename` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`component_beamlineId`),
  KEY `bf_component_beamline_FK1` (`componentId`),
  CONSTRAINT `bf_component_beamline_FK1` FOREIGN KEY (`componentId`) REFERENCES `BF_component` (`componentId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `BF_subcomponent`
--

DROP TABLE IF EXISTS `BF_subcomponent`;
                                                                                
CREATE TABLE `BF_subcomponent` (
  `subcomponentId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `componentId` int(10) UNSIGNED DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`subcomponentId`),
  KEY `bf_subcomponent_FK1` (`componentId`),
  CONSTRAINT `bf_subcomponent_FK1` FOREIGN KEY (`componentId`) REFERENCES `BF_component` (`componentId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `BF_fault`
--

DROP TABLE IF EXISTS `BF_fault`;
CREATE TABLE `BF_fault` (
  `faultId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
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
  `assigneeId` int(10) UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`faultId`),
  KEY `bf_fault_FK1` (`sessionId`),
  KEY `bf_fault_FK2` (`subcomponentId`),
  KEY `bf_fault_FK3` (`personId`),
  KEY `bf_fault_FK4` (`assigneeId`),
  CONSTRAINT `bf_fault_FK1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`),
  CONSTRAINT `bf_fault_FK2` FOREIGN KEY (`subcomponentId`) REFERENCES `BF_subcomponent` (`SUBCOMPONENTID`),
  CONSTRAINT `bf_fault_FK3` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`),
  CONSTRAINT `bf_fault_FK4` FOREIGN KEY (`assigneeId`) REFERENCES `Person` (`personId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `BF_subcomponent_beamline`
--

DROP TABLE IF EXISTS `BF_subcomponent_beamline`;
CREATE TABLE `BF_subcomponent_beamline` (
  `subcomponent_beamlineId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `subcomponentId` int(10) UNSIGNED DEFAULT NULL,
  `beamlinename` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`subcomponent_beamlineId`),
  KEY `bf_subcomponent_beamline_FK1` (`subcomponentId`),
  CONSTRAINT `bf_subcomponent_beamline_FK1` FOREIGN KEY (`subcomponentId`) REFERENCES `BF_subcomponent` (`SUBCOMPONENTID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



--
-- Table structure for table `BF_system_beamline`
--

DROP TABLE IF EXISTS `BF_system_beamline`;
CREATE TABLE `BF_system_beamline` (
  `system_beamlineId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `systemId` int(10) UNSIGNED DEFAULT NULL,
  `beamlineName` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`system_beamlineId`),
  KEY `bf_system_beamline_FK1` (`systemId`),
  CONSTRAINT `bf_system_beamline_FK1` FOREIGN KEY (`systemId`) REFERENCES `BF_system` (`SYSTEMID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `BLSampleGroup`
--

DROP TABLE IF EXISTS `BLSampleGroup`;
CREATE TABLE `BLSampleGroup` (
  `blSampleGroupId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`blSampleGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `BLSampleGroup_has_BLSample`
--

DROP TABLE IF EXISTS `BLSampleGroup_has_BLSample`;

CREATE TABLE `BLSampleGroup_has_BLSample` (
  `blSampleGroupId` int(11) UNSIGNED NOT NULL,
  `blSampleId` int(11) UNSIGNED NOT NULL,
  `order` mediumint(9) DEFAULT NULL,
  `type` ENUM('background','container','sample','calibrant') DEFAULT NULL,
  PRIMARY KEY (`blSampleGroupId`,`blSampleId`),
  KEY `BLSampleGroup_has_BLSample_ibfk2` (`blSampleId`),
  CONSTRAINT `BLSampleGroup_has_BLSample_ibfk1` FOREIGN KEY (`blSampleGroupId`) REFERENCES `BLSampleGroup` (`blSampleGroupId`),
  CONSTRAINT `BLSampleGroup_has_BLSample_ibfk2` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `BLSample_has_EnergyScan`
--

DROP TABLE IF EXISTS `BLSample_has_EnergyScan`;

CREATE TABLE `BLSample_has_EnergyScan` (
  `blSampleId` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `energyScanId` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `blSampleHasEnergyScanId` int(10) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`blSampleHasEnergyScanId`),
  KEY `BLSample_has_EnergyScan_FKIndex1` (`blSampleId`),
  KEY `BLSample_has_EnergyScan_FKIndex2` (`energyScanId`),
  CONSTRAINT `BLSample_has_EnergyScan_ibfk_1` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `BLSample_has_EnergyScan_ibfk_2` FOREIGN KEY (`energyScanId`) REFERENCES `EnergyScan` (`energyScanId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `BeamlineStats`;
CREATE TABLE `BeamlineStats` (
  `beamlineStatsId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `beamline` varchar(10) DEFAULT NULL,
  `recordTimeStamp` datetime DEFAULT NULL,
  `ringCurrent` float DEFAULT NULL,
  `energy` float DEFAULT NULL,
  `gony` float DEFAULT NULL,
  `beamW` float DEFAULT NULL,
  `beamH` float DEFAULT NULL,
  `flux` double DEFAULT NULL,
  `scanFileW` varchar(255) DEFAULT NULL,
  `scanFileH` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`beamlineStatsId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;


-- Table structure for table `BeamApertures`
--

DROP TABLE IF EXISTS `BeamApertures`;
CREATE TABLE `BeamApertures` (
  `beamAperturesid` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `beamlineStatsId` int(11) UNSIGNED DEFAULT NULL,
  `flux` double DEFAULT NULL,
  `x` float DEFAULT NULL,
  `y` float DEFAULT NULL,
  `apertureSize` smallint(5) UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`beamAperturesid`),
  KEY `beamapertures_FK1` (`beamlineStatsId`),
  CONSTRAINT `beamapertures_FK1` FOREIGN KEY (`beamlineStatsId`) REFERENCES `BeamlineStats` (`BEAMLINESTATSID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `BeamCentres`
--

DROP TABLE IF EXISTS `BeamCentres`;

CREATE TABLE `BeamCentres` (
  `beamCentresid` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `beamlineStatsId` int(11) UNSIGNED DEFAULT NULL,
  `x` float DEFAULT NULL,
  `y` float DEFAULT NULL,
  `zoom` tinyint(3) UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`beamCentresid`),
  KEY `beamCentres_FK1` (`beamlineStatsId`),
  CONSTRAINT `beamCentres_FK1` FOREIGN KEY (`beamlineStatsId`) REFERENCES `BeamlineStats` (`BEAMLINESTATSID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `BeamlineAction`
--

DROP TABLE IF EXISTS `BeamlineAction`;
CREATE TABLE `BeamlineAction` (
  `beamlineActionId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `sessionId` int(11) UNSIGNED DEFAULT NULL,
  `startTimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `endTimestamp` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `message` varchar(255) DEFAULT NULL,
  `parameter` varchar(50) DEFAULT NULL,
  `value` varchar(30) DEFAULT NULL,
  `loglevel` ENUM('DEBUG','CRITICAL','INFO') DEFAULT NULL,
  `status` ENUM('PAUSED','RUNNING','TERMINATED','COMPLETE','ERROR','EPICSFAIL') DEFAULT NULL,
  PRIMARY KEY (`beamlineActionId`),
  KEY `BeamlineAction_ibfk1` (`sessionId`),
  CONSTRAINT `BeamlineAction_ibfk1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;




--
-- Table structure for table `CalendarHash`
--

DROP TABLE IF EXISTS `CalendarHash`;

CREATE TABLE `CalendarHash` (
  `calendarHashId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `ckey` varchar(50) DEFAULT NULL,
  `hash` varchar(128) DEFAULT NULL,
  `beamline` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`calendarHashId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='Lets people get to their calendars without logging in using a private (hash) url';

--
-- Table structure for table `ComponentSubType`
--

DROP TABLE IF EXISTS `ComponentSubType`;

CREATE TABLE `ComponentSubType` (
  `componentSubTypeId` int(11) UNSIGNED NOT NULL,
  `name` varchar(31) NOT NULL,
  `hasPh` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`componentSubTypeId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Component_has_SubType`
--

DROP TABLE IF EXISTS `Component_has_SubType`;

CREATE TABLE `Component_has_SubType` (
  `componentId` int(10) UNSIGNED NOT NULL,
  `componentSubTypeId` int(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`componentId`,`componentSubTypeId`),
  KEY `component_has_SubType_fk2` (`componentSubTypeId`),
  CONSTRAINT `component_has_SubType_fk1` FOREIGN KEY (`componentId`) REFERENCES `Protein` (`proteinId`) ON DELETE CASCADE,
  CONSTRAINT `component_has_SubType_fk2` FOREIGN KEY (`componentSubTypeId`) REFERENCES `ComponentSubType` (`componentSubTypeId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--
-- Table structure for table `ConcentrationType`
--

DROP TABLE IF EXISTS `ConcentrationType`;

CREATE TABLE `ConcentrationType` (
  `concentrationTypeId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(31) NOT NULL,
  `symbol` varchar(8) NOT NULL,
  PRIMARY KEY (`concentrationTypeId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `ContainerHistory`
--

DROP TABLE IF EXISTS `ContainerHistory`;

CREATE TABLE `ContainerHistory` (
  `containerHistoryId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `containerId` int(10) UNSIGNED DEFAULT NULL,
  `location` varchar(45) DEFAULT NULL,
  `blTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`containerHistoryId`),
  KEY `ContainerHistory_ibfk1` (`containerId`),
  CONSTRAINT `ContainerHistory_ibfk1` FOREIGN KEY (`containerId`) REFERENCES `Container` (`containerId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `CourierTermsAccepted`
--
DROP TABLE IF EXISTS `CourierTermsAccepted`;

CREATE TABLE `CourierTermsAccepted` (
  `courierTermsAcceptedId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `proposalId` int(10) UNSIGNED NOT NULL,
  `personId` int(10) UNSIGNED NOT NULL,
  `shippingName` varchar(100) DEFAULT NULL,
  `timestamp` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`courierTermsAcceptedId`),
  KEY `CourierTermsAccepted_ibfk_1` (`proposalId`),
  KEY `CourierTermsAccepted_ibfk_2` (`personId`),
  CONSTRAINT `CourierTermsAccepted_ibfk_1` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`),
  CONSTRAINT `CourierTermsAccepted_ibfk_2` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Records acceptances of the courier T and C';

--
-- Table structure for table `DataCollectionComment`
--

DROP TABLE IF EXISTS `DataCollectionComment`;

CREATE TABLE `DataCollectionComment` (
  `dataCollectionCommentId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `dataCollectionId` int(11) UNSIGNED NOT NULL,
  `personId` int(10) UNSIGNED NOT NULL,
  `comments` varchar(4000) DEFAULT NULL,
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modTime` date DEFAULT NULL,
  PRIMARY KEY (`dataCollectionCommentId`),
  KEY `dataCollectionComment_fk1` (`dataCollectionId`),
  KEY `dataCollectionComment_fk2` (`personId`),
  CONSTRAINT `dataCollectionComment_fk1` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `dataCollectionComment_fk2` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `DataCollectionPlanGroup`;

CREATE TABLE `DataCollectionPlanGroup` (
  `dataCollectionPlanGroupId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `sessionId` int(11) UNSIGNED DEFAULT NULL,
  `blSampleId` int(11) UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`dataCollectionPlanGroupId`),
  KEY `DataCollectionPlanGroup_ibfk1` (`sessionId`),
  KEY `DataCollectionPlanGroup_ibfk2` (`blSampleId`),
  CONSTRAINT `DataCollectionPlanGroup_ibfk1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON UPDATE CASCADE,
  CONSTRAINT `DataCollectionPlanGroup_ibfk2` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `DataReductionStatus`
--

DROP TABLE IF EXISTS `DataReductionStatus`;
CREATE TABLE `DataReductionStatus` (
  `dataReductionStatusId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `dataCollectionId` int(11) UNSIGNED NOT NULL,
  `status` varchar(15) DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`dataReductionStatusId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `DewarRegistry`
--

DROP TABLE IF EXISTS `DewarRegistry`;

CREATE TABLE `DewarRegistry` (
  `facilityCode` varchar(20) NOT NULL,
  `proposalId` int(11) UNSIGNED NOT NULL,
  `labContactId` int(11) UNSIGNED NOT NULL,
  `purchaseDate` datetime DEFAULT NULL,
  `bltimestamp` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`facilityCode`),
  KEY `DewarRegistry_ibfk_1` (`proposalId`),
  KEY `DewarRegistry_ibfk_2` (`labContactId`),
  CONSTRAINT `DewarRegistry_ibfk_1` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`) ON DELETE CASCADE,
  CONSTRAINT `DewarRegistry_ibfk_2` FOREIGN KEY (`labContactId`) REFERENCES `LabContact` (`labContactId`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--
-- Table structure for table `DewarReport`
--

DROP TABLE IF EXISTS `DewarReport`;

CREATE TABLE `DewarReport` (
  `dewarReportId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `facilityCode` varchar(20) NOT NULL,
  `report` text,
  `attachment` varchar(255) DEFAULT NULL,
  `bltimestamp` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`dewarReportId`),
  KEY `DewarReportIdx1` (`facilityCode`),
  CONSTRAINT `DewarReport_ibfk_1` FOREIGN KEY (`facilityCode`) REFERENCES `DewarRegistry` (`facilityCode`) ON DELETE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

--
-- Table structure for table `DiffractionPlan_has_Detector`
--

DROP TABLE IF EXISTS `DiffractionPlan_has_Detector`;

CREATE TABLE `DiffractionPlan_has_Detector` (
  `diffractionPlanId` int(11) UNSIGNED NOT NULL,
  `detectorId` int(11) NOT NULL,
  `exposureTime` double DEFAULT NULL,
  `distance` double DEFAULT NULL,
  `orientation` double DEFAULT NULL,
  PRIMARY KEY (`diffractionPlanId`,`detectorId`),
  KEY `DiffractionPlan_has_Detector_ibfk2` (`detectorId`),
  CONSTRAINT `DiffractionPlan_has_Detector_ibfk1` FOREIGN KEY (`diffractionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`),
  CONSTRAINT `DiffractionPlan_has_Detector_ibfk2` FOREIGN KEY (`detectorId`) REFERENCES `Detector` (`detectorId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `EMMicroscope`
--

DROP TABLE IF EXISTS `EMMicroscope`;

CREATE TABLE `EMMicroscope` (
  `emMicroscopeId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `instrumentName` varchar(100) NOT NULL,
  `voltage` float DEFAULT NULL,
  `CS` float DEFAULT NULL COMMENT 'Unit: mm',
  `detectorPixelSize` float DEFAULT NULL,
  `C2aperture` float DEFAULT NULL,
  `ObjAperture` float DEFAULT NULL,
  `C2lens` float DEFAULT NULL,
  PRIMARY KEY (`emMicroscopeId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `Imager`;

CREATE TABLE `Imager` (
  `imagerId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `temperature` float DEFAULT NULL,
  `serial` varchar(45) DEFAULT NULL,
  `capacity` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`imagerId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `InspectionType`
--

DROP TABLE IF EXISTS `InspectionType`;

CREATE TABLE `InspectionType` (
  `inspectionTypeId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`inspectionTypeId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

--
-- Table structure for table `PDBEntry`
--

DROP TABLE IF EXISTS `PDBEntry`;

CREATE TABLE `PDBEntry` (
  `pdbEntryId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
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
  `authorMatch` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`pdbEntryId`),
  KEY `pdbEntryIdx1` (`autoProcProgramId`),
  CONSTRAINT `pdbEntry_FK1` FOREIGN KEY (`autoProcProgramId`) REFERENCES `AutoProcProgram` (`autoProcProgramId`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `PDBEntry_has_AutoProcProgram`
--

DROP TABLE IF EXISTS `PDBEntry_has_AutoProcProgram`;

CREATE TABLE `PDBEntry_has_AutoProcProgram` (
  `pdbEntryHasAutoProcId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `pdbEntryId` int(11) UNSIGNED NOT NULL,
  `autoProcProgramId` int(11) UNSIGNED NOT NULL,
  `distance` float DEFAULT NULL,
  PRIMARY KEY (`pdbEntryHasAutoProcId`),
  KEY `pdbEntry_AutoProcProgramIdx1` (`pdbEntryId`),
  KEY `pdbEntry_AutoProcProgramIdx2` (`autoProcProgramId`),
  CONSTRAINT `pdbEntry_AutoProcProgram_FK1` FOREIGN KEY (`pdbEntryId`) REFERENCES `PDBEntry` (`pdbEntryId`) ON DELETE CASCADE,
  CONSTRAINT `pdbEntry_AutoProcProgram_FK2` FOREIGN KEY (`autoProcProgramId`) REFERENCES `AutoProcProgram` (`autoProcProgramId`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `PHPSession`
--

DROP TABLE IF EXISTS `PHPSession`;

CREATE TABLE `PHPSession` (
  `id` varchar(50) NOT NULL,
  `accessDate` datetime DEFAULT NULL,
  `data` varchar(4000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Particle`
--

DROP TABLE IF EXISTS `Particle`;

CREATE TABLE `Particle` (
  `particleId` int(11) UNSIGNED NOT NULL,
  `dataCollectionId` int(11) UNSIGNED NOT NULL,
  `x` float DEFAULT NULL,
  `y` float DEFAULT NULL,
  PRIMARY KEY (`particleId`),
  KEY `Particle_FKIND1` (`dataCollectionId`),
  CONSTRAINT `Particle_FK1` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Permission`
--

DROP TABLE IF EXISTS `Permission`;

CREATE TABLE `Permission` (
  `permissionId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `type` varchar(15) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`permissionId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Project`
--

DROP TABLE IF EXISTS `Project`;

CREATE TABLE `Project` (
  `projectId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `personId` int(11) UNSIGNED DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL,
  `acronym` varchar(100) DEFAULT NULL,
  `owner` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`projectId`),
  KEY `Project_FK1` (`personId`),
  CONSTRAINT `Project_FK1` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Project_has_BLSample`
--

DROP TABLE IF EXISTS `Project_has_BLSample`;

CREATE TABLE `Project_has_BLSample` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `blSampleId` int(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`projectId`,`blSampleId`),
  KEY `Project_has_BLSample_FK2` (`blSampleId`),
  CONSTRAINT `Project_has_BLSample_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Project_has_BLSample_FK2` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `Project_has_DCGroup`
--

DROP TABLE IF EXISTS `Project_has_DCGroup`;

CREATE TABLE `Project_has_DCGroup` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `dataCollectionGroupId` int(11) NOT NULL,
  PRIMARY KEY (`projectId`,`dataCollectionGroupId`),
  KEY `Project_has_DCGroup_FK2` (`dataCollectionGroupId`),
  CONSTRAINT `Project_has_DCGroup_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Project_has_DCGroup_FK2` FOREIGN KEY (`dataCollectionGroupId`) REFERENCES `DataCollectionGroup` (`dataCollectionGroupId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Project_has_EnergyScan`
--

DROP TABLE IF EXISTS `Project_has_EnergyScan`;

CREATE TABLE `Project_has_EnergyScan` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `energyScanId` int(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`projectId`,`energyScanId`),
  KEY `project_has_energyscan_FK2` (`energyScanId`),
  CONSTRAINT `project_has_energyscan_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `project_has_energyscan_FK2` FOREIGN KEY (`energyScanId`) REFERENCES `EnergyScan` (`energyScanId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Project_has_Person`
--

DROP TABLE IF EXISTS `Project_has_Person`;

CREATE TABLE `Project_has_Person` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `personId` int(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`projectId`,`personId`),
  KEY `project_has_person_FK2` (`personId`),
  CONSTRAINT `project_has_person_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE,
  CONSTRAINT `project_has_person_FK2` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Project_has_Protein`
--

DROP TABLE IF EXISTS `Project_has_Protein`;

CREATE TABLE `Project_has_Protein` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `proteinId` int(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`projectId`,`proteinId`),
  KEY `project_has_protein_FK2` (`proteinId`),
  CONSTRAINT `project_has_protein_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE,
  CONSTRAINT `project_has_protein_FK2` FOREIGN KEY (`proteinId`) REFERENCES `Protein` (`proteinId`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Project_has_Session`
--

DROP TABLE IF EXISTS `Project_has_Session`;

CREATE TABLE `Project_has_Session` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `sessionId` int(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`projectId`,`sessionId`),
  KEY `project_has_session_FK2` (`sessionId`),
  CONSTRAINT `project_has_session_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `project_has_session_FK2` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Project_has_Shipping`
--

DROP TABLE IF EXISTS `Project_has_Shipping`;

CREATE TABLE `Project_has_Shipping` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `shippingId` int(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`projectId`,`shippingId`),
  KEY `project_has_shipping_FK2` (`shippingId`),
  CONSTRAINT `project_has_shipping_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE,
  CONSTRAINT `project_has_shipping_FK2` FOREIGN KEY (`shippingId`) REFERENCES `Shipping` (`shippingId`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Project_has_User`
--

DROP TABLE IF EXISTS `Project_has_User`;

CREATE TABLE `Project_has_User` (
  `projecthasuserid` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `projectid` int(11) UNSIGNED NOT NULL,
  `username` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`projecthasuserid`),
  KEY `Project_Has_user_FK1` (`projectid`),
  CONSTRAINT `Project_Has_user_FK1` FOREIGN KEY (`projectid`) REFERENCES `Project` (`projectId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

--
-- Table structure for table `Project_has_XFEFSpectrum`
--

DROP TABLE IF EXISTS `Project_has_XFEFSpectrum`;

CREATE TABLE `Project_has_XFEFSpectrum` (
  `projectId` int(11) UNSIGNED NOT NULL,
  `xfeFluorescenceSpectrumId` int(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`projectId`,`xfeFluorescenceSpectrumId`),
  KEY `project_has_xfefspectrum_FK2` (`xfeFluorescenceSpectrumId`),
  CONSTRAINT `project_has_xfefspectrum_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE,
  CONSTRAINT `project_has_xfefspectrum_FK2` FOREIGN KEY (`xfeFluorescenceSpectrumId`) REFERENCES `XFEFluorescenceSpectrum` (`xfeFluorescenceSpectrumId`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Protein_has_Lattice`
--

DROP TABLE IF EXISTS `Protein_has_Lattice`;

CREATE TABLE `Protein_has_Lattice` (
  `proteinId` int(10) UNSIGNED NOT NULL,
  `cell_a` double DEFAULT NULL,
  `cell_b` double DEFAULT NULL,
  `cell_c` double DEFAULT NULL,
  `cell_alpha` double DEFAULT NULL,
  `cell_beta` double DEFAULT NULL,
  `cell_gamma` double DEFAULT NULL,
  PRIMARY KEY (`proteinId`),
  CONSTRAINT `Protein_has_Lattice_ibfk1` FOREIGN KEY (`proteinId`) REFERENCES `Protein` (`proteinId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `SW_onceToken`
--

DROP TABLE IF EXISTS `SW_onceToken`;

CREATE TABLE `SW_onceToken` (
  `onceTokenId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `token` varchar(128) DEFAULT NULL,
  `personId` int(10) UNSIGNED DEFAULT NULL,
  `proposalId` int(10) UNSIGNED DEFAULT NULL,
  `validity` varchar(200) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`onceTokenId`),
  KEY `SW_onceToken_fk1` (`personId`),
  KEY `SW_onceToken_fk2` (`proposalId`),
  CONSTRAINT `SW_onceToken_fk1` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`),
  CONSTRAINT `SW_onceToken_fk2` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='One-time use tokens needed for token auth in order to grant access to file downloads and webcams (and some images)';


--
-- Table structure for table `ScanParametersService`
--

DROP TABLE IF EXISTS `ScanParametersService`;

CREATE TABLE `ScanParametersService` (
  `scanParametersServiceId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`scanParametersServiceId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `ScanParametersModel`
--

DROP TABLE IF EXISTS `ScanParametersModel`;

CREATE TABLE `ScanParametersModel` (
  `scanParametersModelId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `scanParametersServiceId` int(10) UNSIGNED DEFAULT NULL,
  `dataCollectionPlanId` int(11) UNSIGNED DEFAULT NULL,
  `modelNumber` tinyint(3) UNSIGNED DEFAULT NULL,
  `start` double DEFAULT NULL,
  `stop` double DEFAULT NULL,
  `step` double DEFAULT NULL,
  `array` text,
  PRIMARY KEY (`scanParametersModelId`),
  KEY `PDF_Model_ibfk1` (`scanParametersServiceId`),
  KEY `PDF_Model_ibfk2` (`dataCollectionPlanId`),
  CONSTRAINT `PDF_Model_ibfk1` FOREIGN KEY (`scanParametersServiceId`) REFERENCES `ScanParametersService` (`scanParametersServiceId`) ON UPDATE CASCADE,
  CONSTRAINT `PDF_Model_ibfk2` FOREIGN KEY (`dataCollectionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Schedule`
--

DROP TABLE IF EXISTS `Schedule`;

CREATE TABLE `Schedule` (
  `scheduleId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`scheduleId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

--
-- Table structure for table `ScheduleComponent`
--

DROP TABLE IF EXISTS `ScheduleComponent`;

CREATE TABLE `ScheduleComponent` (
  `scheduleComponentId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `scheduleId` int(11) UNSIGNED NOT NULL,
  `inspectionTypeId` int(11) UNSIGNED DEFAULT NULL,
  `offset_hours` int(11) DEFAULT NULL,
  PRIMARY KEY (`scheduleComponentId`),
  KEY `ScheduleComponent_fk2` (`inspectionTypeId`),
  KEY `ScheduleComponent_idx1` (`scheduleId`),
  CONSTRAINT `ScheduleComponent_fk1` FOREIGN KEY (`scheduleId`) REFERENCES `Schedule` (`SCHEDULEID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ScheduleComponent_fk2` FOREIGN KEY (`inspectionTypeId`) REFERENCES `InspectionType` (`inspectionTypeId`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Screen`
--

DROP TABLE IF EXISTS `Screen`;

CREATE TABLE `Screen` (
  `screenId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `proposalId` int(10) UNSIGNED DEFAULT NULL,
  `global` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`screenId`),
  KEY `Screen_fk1` (`proposalId`),
  CONSTRAINT `Screen_fk1` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `ScreenComponentGroup`
--

DROP TABLE IF EXISTS `ScreenComponentGroup`;

CREATE TABLE `ScreenComponentGroup` (
  `screenComponentGroupId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `screenId` int(11) UNSIGNED NOT NULL,
  `position` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`screenComponentGroupId`),
  KEY `ScreenComponentGroup_fk1` (`screenId`),
  CONSTRAINT `ScreenComponentGroup_fk1` FOREIGN KEY (`screenId`) REFERENCES `Screen` (`screenId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `ScreenComponent`
--

DROP TABLE IF EXISTS `ScreenComponent`;

CREATE TABLE `ScreenComponent` (
  `screenComponentId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `screenComponentGroupId` int(11) UNSIGNED NOT NULL,
  `componentId` int(11) UNSIGNED DEFAULT NULL,
  `concentration` float DEFAULT NULL,
  `pH` float DEFAULT NULL,
  PRIMARY KEY (`screenComponentId`),
  KEY `ScreenComponent_fk1` (`screenComponentGroupId`),
  KEY `ScreenComponent_fk2` (`componentId`),
  CONSTRAINT `ScreenComponent_fk1` FOREIGN KEY (`screenComponentGroupId`) REFERENCES `ScreenComponentGroup` (`screenComponentGroupId`),
  CONSTRAINT `ScreenComponent_fk2` FOREIGN KEY (`componentId`) REFERENCES `Protein` (`proteinId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `SessionType`
--

DROP TABLE IF EXISTS `SessionType`;

CREATE TABLE `SessionType` (
  `sessionTypeId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `sessionId` int(10) UNSIGNED NOT NULL,
  `typeName` varchar(31) NOT NULL,
  PRIMARY KEY (`sessionTypeId`),
  KEY `SessionType_FKIndex1` (`sessionId`),
  CONSTRAINT `SessionType_ibfk_1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

--
-- Table structure for table `UserGroup`
--

DROP TABLE IF EXISTS `UserGroup`;

CREATE TABLE `UserGroup` (
  `userGroupId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(31) NOT NULL,
  PRIMARY KEY (`userGroupId`),
  UNIQUE KEY `UserGroup_idx1` (`name`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;
--
-- Table structure for table `UserGroup_has_Permission`
--

DROP TABLE IF EXISTS `UserGroup_has_Permission`;

CREATE TABLE `UserGroup_has_Permission` (
  `userGroupId` int(11) UNSIGNED NOT NULL,
  `permissionId` int(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`userGroupId`,`permissionId`),
  KEY `UserGroup_has_Permission_fk2` (`permissionId`),
  CONSTRAINT `UserGroup_has_Permission_fk1` FOREIGN KEY (`userGroupId`) REFERENCES `UserGroup` (`userGroupId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `UserGroup_has_Permission_fk2` FOREIGN KEY (`permissionId`) REFERENCES `Permission` (`permissionId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `UserGroup_has_Person`
--

DROP TABLE IF EXISTS `UserGroup_has_Person`;

CREATE TABLE `UserGroup_has_Person` (
  `userGroupId` int(11) UNSIGNED NOT NULL,
  `personId` int(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`userGroupId`,`personId`),
  KEY `userGroup_has_Person_fk2` (`personId`),
  CONSTRAINT `userGroup_has_Person_fk1` FOREIGN KEY (`userGroupId`) REFERENCES `UserGroup` (`userGroupId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `userGroup_has_Person_fk2` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `ContainerInspection`
--

DROP TABLE IF EXISTS `ContainerInspection`;

CREATE TABLE `ContainerInspection` (
  `containerInspectionId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
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
  `completedTimeStamp` datetime DEFAULT NULL,
  PRIMARY KEY (`containerInspectionId`),
  KEY `ContainerInspection_fk4` (`scheduleComponentid`),
  KEY `ContainerInspection_idx1` (`containerId`),
  KEY `ContainerInspection_idx2` (`inspectionTypeId`),
  KEY `ContainerInspection_idx3` (`imagerId`),
  CONSTRAINT `ContainerInspection_fk1` FOREIGN KEY (`containerId`) REFERENCES `Container` (`containerId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ContainerInspection_fk2` FOREIGN KEY (`inspectionTypeId`) REFERENCES `InspectionType` (`inspectionTypeId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `ContainerInspection_fk3` FOREIGN KEY (`imagerId`) REFERENCES `Imager` (`imagerId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `ContainerInspection_fk4` FOREIGN KEY (`scheduleComponentid`) REFERENCES `ScheduleComponent` (`scheduleComponentId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `ContainerQueue`
--

DROP TABLE IF EXISTS `ContainerQueue`;

CREATE TABLE `ContainerQueue` (
  `containerQueueId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `containerId` int(10) UNSIGNED DEFAULT NULL,
  `personId` int(10) UNSIGNED DEFAULT NULL,
  `createdTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `completedTimeStamp` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`containerQueueId`),
  KEY `ContainerQueue_ibfk1` (`containerId`),
  KEY `ContainerQueue_ibfk2` (`personId`),
  CONSTRAINT `ContainerQueue_ibfk1` FOREIGN KEY (`containerId`) REFERENCES `Container` (`containerId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ContainerQueue_ibfk2` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `ContainerQueueSample`
--

DROP TABLE IF EXISTS `ContainerQueueSample`;

CREATE TABLE `ContainerQueueSample` (
  `containerQueueSampleId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `containerQueueId` int(11) UNSIGNED DEFAULT NULL,
  `blSubSampleId` int(11) UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`containerQueueSampleId`),
  KEY `ContainerQueueSample_ibfk1` (`containerQueueId`),
  KEY `ContainerQueueSample_ibfk2` (`blSubSampleId`),
  CONSTRAINT `ContainerQueueSample_ibfk1` FOREIGN KEY (`containerQueueId`) REFERENCES `ContainerQueue` (`containerQueueId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ContainerQueueSample_ibfk2` FOREIGN KEY (`blSubSampleId`) REFERENCES `BLSubSample` (`blSubSampleId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;



--
-- Table structure for table `BLSampleImage`
--

DROP TABLE IF EXISTS `BLSampleImage`;

CREATE TABLE `BLSampleImage` (
  `blSampleImageId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `blSampleId` int(11) UNSIGNED NOT NULL,
  `micronsPerPixelX` float DEFAULT NULL,
  `micronsPerPixelY` float DEFAULT NULL,
  `imageFullPath` varchar(255) DEFAULT NULL,
  `blSampleImageScoreId` int(11) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `blTimeStamp` datetime DEFAULT NULL,
  `containerInspectionId` int(11) UNSIGNED DEFAULT NULL,
  `modifiedTimeStamp` datetime DEFAULT NULL,
  PRIMARY KEY (`blSampleImageId`),
  KEY `BLSampleImage_fk2` (`containerInspectionId`),
  KEY `BLSampleImage_idx1` (`blSampleId`),
  CONSTRAINT `BLSampleImage_fk1` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `BLSampleImage_fk2` FOREIGN KEY (`containerInspectionId`) REFERENCES `ContainerInspection` (`containerInspectionId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

--
-- Table structure for table `BLSampleImageAnalysis`
--

DROP TABLE IF EXISTS `BLSampleImageAnalysis`;

CREATE TABLE `BLSampleImageAnalysis` (
  `blSampleImageAnalysisId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `blSampleImageId` int(11) UNSIGNED DEFAULT NULL,
  `oavSnapshotBefore` varchar(255) DEFAULT NULL,
  `oavSnapshotAfter` varchar(255) DEFAULT NULL,
  `deltaX` int(11) DEFAULT NULL,
  `deltaY` int(11) DEFAULT NULL,
  `goodnessOfFit` float DEFAULT NULL,
  `scaleFactor` float DEFAULT NULL,
  `resultCode` varchar(15) DEFAULT NULL,
  `matchStartTimeStamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `matchEndTimeStamp` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`blSampleImageAnalysisId`),
  KEY `BLSampleImageAnalysis_ibfk1` (`blSampleImageId`),
  CONSTRAINT `BLSampleImageAnalysis_ibfk1` FOREIGN KEY (`blSampleImageId`) REFERENCES `BLSampleImage` (`blSampleImageId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `BLSampleImageScore`
--

DROP TABLE IF EXISTS `BLSampleImageScore`;
CREATE TABLE `BLSampleImageScore` (
  `blSampleImageScoreId` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `score` float DEFAULT NULL,
  `colour` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`blSampleImageScoreId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `BLSampleType_has_Component`
--

DROP TABLE IF EXISTS `BLSampleType_has_Component`;
CREATE TABLE `BLSampleType_has_Component` (
  `blSampleTypeId` int(10) UNSIGNED NOT NULL,
  `componentId` int(10) UNSIGNED NOT NULL,
  `abundance` float DEFAULT NULL,
  PRIMARY KEY (`blSampleTypeId`,`componentId`),
  KEY `blSampleType_has_Component_fk2` (`componentId`),
  CONSTRAINT `blSampleType_has_Component_fk1` FOREIGN KEY (`blSampleTypeId`) REFERENCES `Crystal` (`crystalId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `blSampleType_has_Component_fk2` FOREIGN KEY (`componentId`) REFERENCES `Protein` (`proteinId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `BLSample_has_DiffractionPlan`
--

DROP TABLE IF EXISTS `BLSample_has_DiffractionPlan`;

CREATE TABLE `BLSample_has_DiffractionPlan` (
  `blSampleId` int(11) UNSIGNED NOT NULL,
  `diffractionPlanId` int(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`blSampleId`,`diffractionPlanId`),
  KEY `BLSample_has_DiffractionPlan_ibfk2` (`diffractionPlanId`),
  CONSTRAINT `BLSample_has_DiffractionPlan_ibfk1` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`),
  CONSTRAINT `BLSample_has_DiffractionPlan_ibfk2` FOREIGN KEY (`diffractionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_05_02_Merge_with_DLS_create.sql';

