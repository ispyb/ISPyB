--
-- Database: `pydb` version 5.13
--

---------------------------------------------------------

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
) ENGINE=InnoDB AUTO_INCREMENT=40650 DEFAULT CHARSET=latin1;

CREATE TABLE `Additive` (
  `additiveId` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `additiveType` varchar(45) DEFAULT NULL,
  `comments` varchar(512) DEFAULT NULL,
  `chemFormulaHead` varchar(25) DEFAULT '',
  `chemFormulaTail` varchar(25) DEFAULT '',
  PRIMARY KEY (`additiveId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `AdminActivity` (
  `adminActivityId` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL DEFAULT '',
  `action` varchar(45) DEFAULT NULL,
  `comments` varchar(100) DEFAULT NULL,
  `dateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`adminActivityId`),
  UNIQUE KEY `username` (`username`),
  KEY `AdminActivity_FKAction` (`action`)
) ENGINE=InnoDB AUTO_INCREMENT=2154 DEFAULT CHARSET=latin1;

CREATE TABLE `AdminVar` (
  `varId` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  `value` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`varId`),
  KEY `AdminVar_FKIndexName` (`name`),
  KEY `AdminVar_FKIndexValue` (`value`(767))
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1 COMMENT='ISPyB administration values';

CREATE TABLE `Aperture` (
  `apertureId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sizeX` float DEFAULT NULL,
  PRIMARY KEY (`apertureId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Assembly` (
  `assemblyId` int(10) NOT NULL AUTO_INCREMENT,
  `macromoleculeId` int(10) NOT NULL,
  `creationDate` datetime DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`assemblyId`),
  KEY `AssemblyToMacromolecule` (`macromoleculeId`),
  CONSTRAINT `AssemblyToMacromolecule` FOREIGN KEY (`macromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB AUTO_INCREMENT=1711080 DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB AUTO_INCREMENT=1907660 DEFAULT CHARSET=latin1;

CREATE TABLE `AutoProcProgram` (
  `autoProcProgramId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `dataCollectionId` int(11) unsigned DEFAULT NULL,
  `processingCommandLine` varchar(255) DEFAULT NULL COMMENT 'Command line for running the automatic processing',
  `processingPrograms` varchar(255) DEFAULT NULL COMMENT 'Processing programs (comma separated)',
  `processingStatus` enum('RUNNING','FAILED','SUCCESS','0','1') DEFAULT NULL COMMENT 'success (1) / fail (0)',
  `processingMessage` varchar(255) DEFAULT NULL COMMENT 'warning, error,...',
  `processingStartTime` datetime DEFAULT NULL COMMENT 'Processing start time',
  `processingEndTime` datetime DEFAULT NULL COMMENT 'Processing end time',
  `processingEnvironment` varchar(255) DEFAULT NULL COMMENT 'Cpus, Nodes,...',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`autoProcProgramId`),
  KEY `fk_AutoProcProgram_1_idx` (`dataCollectionId`),
  CONSTRAINT `AutoProcProgram_FK1` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1781341 DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB AUTO_INCREMENT=17810881 DEFAULT CHARSET=latin1;

CREATE TABLE `AutoProcScaling` (
  `autoProcScalingId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `autoProcId` int(10) unsigned DEFAULT NULL COMMENT 'Related autoProc item (used by foreign key)',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  `resolutionEllipsoidAxis11` float DEFAULT NULL,
  `resolutionEllipsoidAxis12` float DEFAULT NULL,
  `resolutionEllipsoidAxis13` float DEFAULT NULL,
  `resolutionEllipsoidAxis21` float DEFAULT NULL,
  `resolutionEllipsoidAxis23` float DEFAULT NULL,
  `resolutionEllipsoidAxis31` float DEFAULT NULL,
  `resolutionEllipsoidAxis32` float DEFAULT NULL,
  `resolutionEllipsoidAxis33` float DEFAULT NULL,
  `resolutionEllipsoidValue1` float DEFAULT NULL,
  `resolutionEllipsoidValue2` float DEFAULT NULL,
  `resolutionEllipsoidValue3` float DEFAULT NULL,
  `resolutionEllipsoidAxis22` float DEFAULT NULL,
  PRIMARY KEY (`autoProcScalingId`),
  KEY `AutoProcScalingFk1` (`autoProcId`),
  KEY `AutoProcScalingIdx1` (`autoProcScalingId`,`autoProcId`),
  CONSTRAINT `AutoProcScalingFk1` FOREIGN KEY (`autoProcId`) REFERENCES `AutoProc` (`autoProcId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1711090 DEFAULT CHARSET=latin1;

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
  `ccAno` float DEFAULT NULL,
  `sigAno` varchar(45) DEFAULT NULL,
  `isa` varchar(45) DEFAULT NULL,
  `completenessSpherical` float DEFAULT NULL,
  `completenessEllipsoidal` float DEFAULT NULL,
  `anomalousCompletenessSpherical` float DEFAULT NULL,
  `anomalousCompletenessEllipsoidal` float DEFAULT NULL,
  PRIMARY KEY (`autoProcScalingStatisticsId`),
  KEY `AutoProcScalingStatisticsIdx1` (`autoProcScalingId`),
  KEY `AutoProcScalingStatistics_FKindexType` (`scalingStatisticsType`),
  CONSTRAINT `AutoProcScalingStatisticsFk1` FOREIGN KEY (`autoProcScalingId`) REFERENCES `AutoProcScaling` (`autoProcScalingId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5208275 DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB AUTO_INCREMENT=1711025 DEFAULT CHARSET=latin1;

CREATE TABLE `AutoProcStatus` (
  `autoProcStatusId` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `autoProcIntegrationId` int(10) unsigned NOT NULL,
  `step` enum('Indexing','Integration','Correction','Scaling','Importing') NOT NULL COMMENT 'autoprocessing step',
  `status` enum('Launched','Successful','Failed') NOT NULL COMMENT 'autoprocessing status',
  `comments` varchar(1024) DEFAULT NULL COMMENT 'comments',
  `bltimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`autoProcStatusId`),
  KEY `AutoProcStatus_FKIndex1` (`autoProcIntegrationId`),
  CONSTRAINT `AutoProcStatus_ibfk_1` FOREIGN KEY (`autoProcIntegrationId`) REFERENCES `AutoProcIntegration` (`autoProcIntegrationId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2520127 DEFAULT CHARSET=latin1 COMMENT='AutoProcStatus table is linked to AutoProcIntegration';

CREATE TABLE `BF_automationError` (
  `automationErrorId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `errorType` varchar(40) NOT NULL,
  `solution` text,
  PRIMARY KEY (`automationErrorId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `BF_automationFault` (
  `automationFaultId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `automationErrorId` int(10) unsigned DEFAULT NULL,
  `containerId` int(10) unsigned DEFAULT NULL,
  `severity` enum('1','2','3') DEFAULT NULL,
  `stacktrace` text,
  `resolved` tinyint(1) DEFAULT NULL,
  `faultTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`automationFaultId`),
  KEY `BF_automationFault_ibfk1` (`automationErrorId`),
  KEY `BF_automationFault_ibfk2` (`containerId`),
  CONSTRAINT `BF_automationFault_ibfk1` FOREIGN KEY (`automationErrorId`) REFERENCES `BF_automationError` (`automationErrorId`),
  CONSTRAINT `BF_automationFault_ibfk2` FOREIGN KEY (`containerId`) REFERENCES `Container` (`containerId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `BF_component` (
  `componentId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `systemId` int(10) unsigned DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`componentId`),
  KEY `bf_component_FK1` (`systemId`),
  CONSTRAINT `bf_component_FK1` FOREIGN KEY (`systemId`) REFERENCES `BF_system` (`systemId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `BF_component_beamline` (
  `component_beamlineId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `componentId` int(10) unsigned DEFAULT NULL,
  `beamlinename` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`component_beamlineId`),
  KEY `bf_component_beamline_FK1` (`componentId`),
  CONSTRAINT `bf_component_beamline_FK1` FOREIGN KEY (`componentId`) REFERENCES `BF_component` (`componentId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `BF_fault` (
  `faultId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sessionId` int(10) unsigned NOT NULL,
  `owner` varchar(50) DEFAULT NULL,
  `subcomponentId` int(10) unsigned DEFAULT NULL,
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
  `personId` int(10) unsigned DEFAULT NULL,
  `assigneeId` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`faultId`),
  KEY `bf_fault_FK1` (`sessionId`),
  KEY `bf_fault_FK2` (`subcomponentId`),
  KEY `bf_fault_FK3` (`personId`),
  KEY `bf_fault_FK4` (`assigneeId`),
  CONSTRAINT `bf_fault_FK1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`),
  CONSTRAINT `bf_fault_FK2` FOREIGN KEY (`subcomponentId`) REFERENCES `BF_subcomponent` (`subcomponentId`),
  CONSTRAINT `bf_fault_FK3` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`),
  CONSTRAINT `bf_fault_FK4` FOREIGN KEY (`assigneeId`) REFERENCES `Person` (`personId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `BF_subcomponent` (
  `subcomponentId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `componentId` int(10) unsigned DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`subcomponentId`),
  KEY `bf_subcomponent_FK1` (`componentId`),
  CONSTRAINT `bf_subcomponent_FK1` FOREIGN KEY (`componentId`) REFERENCES `BF_component` (`componentId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `BF_subcomponent_beamline` (
  `subcomponent_beamlineId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `subcomponentId` int(10) unsigned DEFAULT NULL,
  `beamlinename` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`subcomponent_beamlineId`),
  KEY `bf_subcomponent_beamline_FK1` (`subcomponentId`),
  CONSTRAINT `bf_subcomponent_beamline_FK1` FOREIGN KEY (`subcomponentId`) REFERENCES `BF_subcomponent` (`subcomponentId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `BF_system` (
  `systemId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`systemId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `BF_system_beamline` (
  `system_beamlineId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `systemId` int(10) unsigned DEFAULT NULL,
  `beamlineName` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`system_beamlineId`),
  KEY `bf_system_beamline_FK1` (`systemId`),
  CONSTRAINT `bf_system_beamline_FK1` FOREIGN KEY (`systemId`) REFERENCES `BF_system` (`systemId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `BLSample` (
  `blSampleId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `diffractionPlanId` int(10) unsigned DEFAULT NULL,
  `crystalId` int(10) unsigned DEFAULT NULL,
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
  `positionId` int(11) unsigned DEFAULT NULL,
  `blSubSampleId` int(11) unsigned DEFAULT NULL,
  `screenComponentGroupId` int(11) unsigned DEFAULT NULL,
  `volume` float DEFAULT NULL,
  `dimension1` double DEFAULT NULL,
  `dimension2` double DEFAULT NULL,
  `dimension3` double DEFAULT NULL,
  `shape` varchar(15) DEFAULT NULL,
  `subLocation` smallint(5) unsigned DEFAULT NULL COMMENT 'Indicates the sample''s location on a multi-sample pin, where 1 is closest to the pin base',
  PRIMARY KEY (`blSampleId`),
  KEY `BLSample_FKIndex1` (`containerId`),
  KEY `BLSample_FKIndex2` (`crystalId`),
  KEY `BLSample_FKIndex3` (`diffractionPlanId`),
  KEY `crystalId` (`crystalId`,`containerId`),
  KEY `BLSample_Index1` (`name`) USING BTREE,
  KEY `BLSample_FKIndex_Status` (`blSampleStatus`),
  KEY `BLSampleImage_idx1` (`blSubSampleId`),
  KEY `BLSample_fk5` (`screenComponentGroupId`),
  CONSTRAINT `BLSample_ibfk_1` FOREIGN KEY (`containerId`) REFERENCES `Container` (`containerId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `BLSample_ibfk_2` FOREIGN KEY (`crystalId`) REFERENCES `Crystal` (`crystalId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `BLSample_ibfk_3` FOREIGN KEY (`diffractionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=631405 DEFAULT CHARSET=latin1;

CREATE TABLE `BLSampleGroup` (
  `blSampleGroupId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`blSampleGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `BLSampleGroup_has_BLSample` (
  `blSampleGroupId` int(11) unsigned NOT NULL,
  `blSampleId` int(11) unsigned NOT NULL,
  `order` mediumint(9) DEFAULT NULL,
  `type` enum('background','container','sample','calibrant') DEFAULT NULL,
  PRIMARY KEY (`blSampleGroupId`,`blSampleId`),
  KEY `BLSampleGroup_has_BLSample_ibfk2` (`blSampleId`),
  CONSTRAINT `BLSampleGroup_has_BLSample_ibfk1` FOREIGN KEY (`blSampleGroupId`) REFERENCES `BLSampleGroup` (`blSampleGroupId`),
  CONSTRAINT `BLSampleGroup_has_BLSample_ibfk2` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `BLSampleImage` (
  `blSampleImageId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `blSampleId` int(11) unsigned NOT NULL,
  `micronsPerPixelX` float DEFAULT NULL,
  `micronsPerPixelY` float DEFAULT NULL,
  `imageFullPath` varchar(255) DEFAULT NULL,
  `blSampleImageScoreId` int(11) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `blTimeStamp` datetime DEFAULT NULL,
  `containerInspectionId` int(11) unsigned DEFAULT NULL,
  `modifiedTimeStamp` datetime DEFAULT NULL,
  PRIMARY KEY (`blSampleImageId`),
  KEY `BLSampleImage_fk2` (`containerInspectionId`),
  KEY `BLSampleImage_idx1` (`blSampleId`),
  CONSTRAINT `BLSampleImage_fk1` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `BLSampleImage_fk2` FOREIGN KEY (`containerInspectionId`) REFERENCES `ContainerInspection` (`containerInspectionId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

CREATE TABLE `BLSampleImageAnalysis` (
  `blSampleImageAnalysisId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `blSampleImageId` int(11) unsigned DEFAULT NULL,
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

CREATE TABLE `BLSampleImageScore` (
  `blSampleImageScoreId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `score` float DEFAULT NULL,
  `colour` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`blSampleImageScoreId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `BLSampleType_has_Component` (
  `blSampleTypeId` int(10) unsigned NOT NULL,
  `componentId` int(10) unsigned NOT NULL,
  `abundance` float DEFAULT NULL,
  PRIMARY KEY (`blSampleTypeId`,`componentId`),
  KEY `blSampleType_has_Component_fk2` (`componentId`),
  CONSTRAINT `blSampleType_has_Component_fk1` FOREIGN KEY (`blSampleTypeId`) REFERENCES `Crystal` (`crystalId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `blSampleType_has_Component_fk2` FOREIGN KEY (`componentId`) REFERENCES `Protein` (`proteinId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `BLSample_has_DiffractionPlan` (
  `blSampleId` int(11) unsigned NOT NULL,
  `diffractionPlanId` int(11) unsigned NOT NULL,
  PRIMARY KEY (`blSampleId`,`diffractionPlanId`),
  KEY `BLSample_has_DiffractionPlan_ibfk2` (`diffractionPlanId`),
  CONSTRAINT `BLSample_has_DiffractionPlan_ibfk1` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`),
  CONSTRAINT `BLSample_has_DiffractionPlan_ibfk2` FOREIGN KEY (`diffractionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `BLSample_has_EnergyScan` (
  `blSampleId` int(10) unsigned NOT NULL DEFAULT '0',
  `energyScanId` int(10) unsigned NOT NULL DEFAULT '0',
  `blSampleHasEnergyScanId` int(10) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`blSampleHasEnergyScanId`),
  KEY `BLSample_has_EnergyScan_FKIndex1` (`blSampleId`),
  KEY `BLSample_has_EnergyScan_FKIndex2` (`energyScanId`),
  CONSTRAINT `BLSample_has_EnergyScan_ibfk_1` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `BLSample_has_EnergyScan_ibfk_2` FOREIGN KEY (`energyScanId`) REFERENCES `EnergyScan` (`energyScanId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `BLSession` (
  `sessionId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `expSessionPk` int(11) unsigned DEFAULT NULL COMMENT 'smis session Pk ',
  `beamLineSetupId` int(10) unsigned DEFAULT NULL,
  `proposalId` int(10) unsigned NOT NULL DEFAULT '0',
  `beamCalendarId` int(10) unsigned DEFAULT NULL,
  `projectCode` varchar(45) DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `beamLineName` varchar(45) DEFAULT NULL,
  `scheduled` tinyint(1) DEFAULT NULL,
  `nbShifts` int(10) unsigned DEFAULT NULL,
  `comments` varchar(2000) DEFAULT NULL,
  `beamLineOperator` varchar(255) DEFAULT NULL,
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
  `nbReimbDewars` int(10) DEFAULT NULL,
  PRIMARY KEY (`sessionId`),
  KEY `Session_FKIndex1` (`proposalId`),
  KEY `Session_FKIndex2` (`beamLineSetupId`),
  KEY `Session_FKIndexStartDate` (`startDate`),
  KEY `Session_FKIndexEndDate` (`endDate`),
  KEY `Session_FKIndexBeamLineName` (`beamLineName`),
  KEY `BLSession_FKIndexOperatorSiteNumber` (`operatorSiteNumber`),
  KEY `BLSession_ibfk_3` (`beamCalendarId`),
  CONSTRAINT `BLSession_ibfk_1` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `BLSession_ibfk_2` FOREIGN KEY (`beamLineSetupId`) REFERENCES `BeamLineSetup` (`beamLineSetupId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `BLSession_ibfk_3` FOREIGN KEY (`beamCalendarId`) REFERENCES `BeamCalendar` (`beamCalendarId`)
) ENGINE=InnoDB AUTO_INCREMENT=61426 DEFAULT CHARSET=latin1;

CREATE TABLE `BLSession_has_SCPosition` (
  `blsessionhasscpositionid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `blsessionid` int(11) unsigned NOT NULL,
  `scContainer` smallint(5) unsigned DEFAULT NULL COMMENT 'Position of container within sample changer',
  `containerPosition` smallint(5) unsigned DEFAULT NULL COMMENT 'Position of sample within container',
  PRIMARY KEY (`blsessionhasscpositionid`),
  KEY `blsession_has_scposition_FK1` (`blsessionid`),
  CONSTRAINT `blsession_has_scposition_FK1` FOREIGN KEY (`blsessionid`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `BLSubSample` (
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
  `motorPositionId` int(11) unsigned DEFAULT NULL COMMENT 'motor position',
  PRIMARY KEY (`blSubSampleId`),
  KEY `BLSubSample_FKIndex1` (`blSampleId`),
  KEY `BLSubSample_FKIndex2` (`diffractionPlanId`),
  KEY `BLSubSample_FKIndex3` (`positionId`),
  KEY `BLSubSample_FKIndex5` (`position2Id`),
  KEY `BLSubSample_motorPositionfk_1` (`motorPositionId`),
  CONSTRAINT `BLSubSample_blSamplefk_1` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `BLSubSample_diffractionPlanfk_1` FOREIGN KEY (`diffractionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `BLSubSample_motorPositionfk_1` FOREIGN KEY (`motorPositionId`) REFERENCES `MotorPosition` (`motorPositionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `BLSubSample_positionfk_1` FOREIGN KEY (`positionId`) REFERENCES `Position` (`positionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `BLSubSample_positionfk_2` FOREIGN KEY (`position2Id`) REFERENCES `Position` (`positionId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `BeamApertures` (
  `beamAperturesid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `beamlineStatsId` int(11) unsigned DEFAULT NULL,
  `flux` double DEFAULT NULL,
  `x` float DEFAULT NULL,
  `y` float DEFAULT NULL,
  `apertureSize` smallint(5) unsigned DEFAULT NULL,
  PRIMARY KEY (`beamAperturesid`),
  KEY `beamapertures_FK1` (`beamlineStatsId`),
  CONSTRAINT `beamapertures_FK1` FOREIGN KEY (`beamlineStatsId`) REFERENCES `BeamlineStats` (`beamlineStatsId`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `BeamCalendar` (
  `beamCalendarId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `run` varchar(7) NOT NULL,
  `beamStatus` varchar(24) NOT NULL,
  `startDate` datetime NOT NULL,
  `endDate` datetime NOT NULL,
  PRIMARY KEY (`beamCalendarId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `BeamCentres` (
  `beamCentresid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `beamlineStatsId` int(11) unsigned DEFAULT NULL,
  `x` float DEFAULT NULL,
  `y` float DEFAULT NULL,
  `zoom` tinyint(3) unsigned DEFAULT NULL,
  PRIMARY KEY (`beamCentresid`),
  KEY `beamCentres_FK1` (`beamlineStatsId`),
  CONSTRAINT `beamCentres_FK1` FOREIGN KEY (`beamlineStatsId`) REFERENCES `BeamlineStats` (`beamlineStatsId`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
  `CS` float DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`beamLineSetupId`)
) ENGINE=InnoDB AUTO_INCREMENT=1463429 DEFAULT CHARSET=latin1;

CREATE TABLE `BeamlineAction` (
  `beamlineActionId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `sessionId` int(11) unsigned DEFAULT NULL,
  `startTimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `endTimestamp` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `message` varchar(255) DEFAULT NULL,
  `parameter` varchar(50) DEFAULT NULL,
  `value` varchar(30) DEFAULT NULL,
  `loglevel` enum('DEBUG','CRITICAL','INFO') DEFAULT NULL,
  `status` enum('PAUSED','RUNNING','TERMINATED','COMPLETE','ERROR','EPICSFAIL') DEFAULT NULL,
  PRIMARY KEY (`beamlineActionId`),
  KEY `BeamlineAction_ibfk1` (`sessionId`),
  CONSTRAINT `BeamlineAction_ibfk1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `BeamlineStats` (
  `beamlineStatsId` int(11) unsigned NOT NULL AUTO_INCREMENT,
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Buffer` (
  `bufferId` int(10) NOT NULL AUTO_INCREMENT,
  `proposalId` int(10) NOT NULL DEFAULT '-1',
  `safetyLevelId` int(10) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `acronym` varchar(45) DEFAULT NULL,
  `pH` varchar(45) DEFAULT NULL,
  `composition` varchar(45) DEFAULT NULL,
  `comments` varchar(512) DEFAULT NULL,
  `BLSessionId` int(11) unsigned DEFAULT NULL,
  `electronDensity` float(7,5) DEFAULT NULL,
  PRIMARY KEY (`bufferId`),
  KEY `BufferToSafetyLevel` (`safetyLevelId`),
  CONSTRAINT `BufferToSafetyLevel` FOREIGN KEY (`safetyLevelId`) REFERENCES `SafetyLevel` (`safetyLevelId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7480 DEFAULT CHARSET=latin1;

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

CREATE TABLE `CTF` (
  `CTFid` int(11) NOT NULL AUTO_INCREMENT,
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
  `createdTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`CTFid`),
  KEY `fk_CTF_1_idx` (`motionCorrectionId`)
) ENGINE=InnoDB AUTO_INCREMENT=5348 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `CalendarHash` (
  `calendarHashId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ckey` varchar(50) DEFAULT NULL,
  `hash` varchar(128) DEFAULT NULL,
  `beamline` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`calendarHashId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Lets people get to their calendars without logging in using a private (hash) url';

CREATE TABLE `ComponentSubType` (
  `componentSubTypeId` int(11) unsigned NOT NULL,
  `name` varchar(31) NOT NULL,
  `hasPh` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`componentSubTypeId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `ComponentType` (
  `componentTypeId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(31) NOT NULL,
  PRIMARY KEY (`componentTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

CREATE TABLE `Component_has_SubType` (
  `componentId` int(10) unsigned NOT NULL,
  `componentSubTypeId` int(11) unsigned NOT NULL,
  PRIMARY KEY (`componentId`,`componentSubTypeId`),
  KEY `component_has_SubType_fk2` (`componentSubTypeId`),
  CONSTRAINT `component_has_SubType_fk1` FOREIGN KEY (`componentId`) REFERENCES `Protein` (`proteinId`) ON DELETE CASCADE,
  CONSTRAINT `component_has_SubType_fk2` FOREIGN KEY (`componentSubTypeId`) REFERENCES `ComponentSubType` (`componentSubTypeId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `ConcentrationType` (
  `concentrationTypeId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(31) NOT NULL,
  `symbol` varchar(8) NOT NULL,
  PRIMARY KEY (`concentrationTypeId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
  `screenId` int(11) unsigned DEFAULT NULL,
  `scheduleId` int(11) unsigned DEFAULT NULL,
  `imagerId` int(11) unsigned DEFAULT NULL,
  `scLocationUpdated` datetime DEFAULT NULL,
  `requestedImagerId` int(11) unsigned DEFAULT NULL,
  `requestedReturn` tinyint(1) DEFAULT '0' COMMENT 'True for requesting return, False means container will be disposed',
  `comments` varchar(255) DEFAULT NULL,
  `experimentType` varchar(20) DEFAULT NULL,
  `storageTemperature` float DEFAULT NULL,
  PRIMARY KEY (`containerId`),
  UNIQUE KEY `Container_UNIndex1` (`barcode`),
  KEY `Container_FKIndex1` (`dewarId`),
  KEY `Container_FKIndex` (`beamlineLocation`),
  KEY `Container_FKIndexStatus` (`containerStatus`),
  KEY `Container_ibfk6` (`sessionId`),
  KEY `Container_ibfk5` (`ownerId`),
  CONSTRAINT `Container_ibfk5` FOREIGN KEY (`ownerId`) REFERENCES `Person` (`personId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `Container_ibfk6` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `Container_ibfk_1` FOREIGN KEY (`dewarId`) REFERENCES `Dewar` (`dewarId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=345543 DEFAULT CHARSET=latin1;

CREATE TABLE `ContainerHistory` (
  `containerHistoryId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `containerId` int(10) unsigned DEFAULT NULL,
  `location` varchar(45) DEFAULT NULL,
  `blTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`containerHistoryId`),
  KEY `ContainerHistory_ibfk1` (`containerId`),
  CONSTRAINT `ContainerHistory_ibfk1` FOREIGN KEY (`containerId`) REFERENCES `Container` (`containerId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `ContainerInspection` (
  `containerInspectionId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `containerId` int(11) unsigned NOT NULL,
  `inspectionTypeId` int(11) unsigned NOT NULL,
  `imagerId` int(11) unsigned DEFAULT NULL,
  `temperature` float DEFAULT NULL,
  `blTimeStamp` datetime DEFAULT NULL,
  `scheduleComponentid` int(11) unsigned DEFAULT NULL,
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

CREATE TABLE `ContainerQueue` (
  `containerQueueId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `containerId` int(10) unsigned DEFAULT NULL,
  `personId` int(10) unsigned DEFAULT NULL,
  `createdTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `completedTimeStamp` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`containerQueueId`),
  KEY `ContainerQueue_ibfk1` (`containerId`),
  KEY `ContainerQueue_ibfk2` (`personId`),
  CONSTRAINT `ContainerQueue_ibfk1` FOREIGN KEY (`containerId`) REFERENCES `Container` (`containerId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ContainerQueue_ibfk2` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `ContainerQueueSample` (
  `containerQueueSampleId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `containerQueueId` int(11) unsigned DEFAULT NULL,
  `blSubSampleId` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`containerQueueSampleId`),
  KEY `ContainerQueueSample_ibfk1` (`containerQueueId`),
  KEY `ContainerQueueSample_ibfk2` (`blSubSampleId`),
  CONSTRAINT `ContainerQueueSample_ibfk1` FOREIGN KEY (`containerQueueId`) REFERENCES `ContainerQueue` (`containerQueueId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ContainerQueueSample_ibfk2` FOREIGN KEY (`blSubSampleId`) REFERENCES `BLSubSample` (`blSubSampleId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
  `abundance` float DEFAULT NULL,
  `packingFraction` float DEFAULT NULL,
  PRIMARY KEY (`crystalId`),
  KEY `Crystal_FKIndex1` (`proteinId`),
  KEY `Crystal_FKIndex2` (`diffractionPlanId`),
  CONSTRAINT `Crystal_ibfk_1` FOREIGN KEY (`proteinId`) REFERENCES `Protein` (`proteinId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Crystal_ibfk_2` FOREIGN KEY (`diffractionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=556548 DEFAULT CHARSET=latin1;

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

CREATE TABLE `DataAcquisition` (
  `dataAcquisitionId` int(10) NOT NULL AUTO_INCREMENT,
  `sampleCellId` int(10) NOT NULL,
  `framesCount` varchar(45) DEFAULT NULL,
  `energy` varchar(45) DEFAULT NULL,
  `waitTime` varchar(45) DEFAULT NULL,
  `detectorDistance` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`dataAcquisitionId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
  `blSampleId` int(11) unsigned DEFAULT NULL,
  `sessionId` int(11) unsigned DEFAULT '0',
  `experimentType` varchar(24) DEFAULT NULL,
  `crystalClass` varchar(20) DEFAULT NULL,
  `chiStart` float DEFAULT NULL,
  `detectorMode` varchar(255) DEFAULT NULL,
  `actualSampleBarcode` varchar(45) DEFAULT NULL,
  `actualSampleSlotInContainer` int(11) unsigned DEFAULT NULL,
  `actualContainerBarcode` varchar(45) DEFAULT NULL,
  `actualContainerSlotInSC` int(11) unsigned DEFAULT NULL,
  `positionId` int(11) unsigned DEFAULT NULL,
  `focalSpotSizeAtSampleX` float DEFAULT NULL,
  `polarisation` float DEFAULT NULL,
  `focalSpotSizeAtSampleY` float DEFAULT NULL,
  `apertureId` int(11) unsigned DEFAULT NULL,
  `screeningOrigId` int(11) unsigned DEFAULT NULL,
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
  `c3lens` float DEFAULT NULL COMMENT 'Unit: %',
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
  CONSTRAINT `DataCollection_ibfk_8` FOREIGN KEY (`blSubSampleId`) REFERENCES `BLSubSample` (`blSubSampleId`)
) ENGINE=InnoDB AUTO_INCREMENT=2180441 DEFAULT CHARSET=latin1;

CREATE TABLE `DataCollectionFileAttachment` (
  `dataCollectionFileAttachmentId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `dataCollectionId` int(11) unsigned NOT NULL,
  `fileFullPath` varchar(255) NOT NULL,
  `fileType` enum('snapshot','log','xy','recip') DEFAULT NULL COMMENT 'snapshot: image file, usually of the sample. \r\nlog: a text file with logging info. \r\nxy: x and y data in text format. \r\nrecip: a compressed csv file with reciprocal space coordinates.',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`dataCollectionFileAttachmentId`),
  KEY `dataCollectionFileAttachmentId_fk1` (`dataCollectionId`),
  CONSTRAINT `dataCollectionFileAttachmentId_fk1` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `DataCollectionGroup` (
  `dataCollectionGroupId` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `blSampleId` int(10) unsigned DEFAULT NULL COMMENT 'references BLSample table',
  `sessionId` int(10) unsigned NOT NULL COMMENT 'references Session table',
  `workflowId` int(11) unsigned DEFAULT NULL,
  `experimentType` enum('EM','SAD','SAD - Inverse Beam','OSC','Collect - Multiwedge','MAD','Helical','Multi-positional','Mesh','Burn','MAD - Inverse Beam','Characterization','Dehydration') DEFAULT NULL COMMENT 'Experiment type flag',
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
) ENGINE=InnoDB AUTO_INCREMENT=1721508 DEFAULT CHARSET=latin1 COMMENT='a dataCollectionGroup is a group of dataCollection for a spe';

CREATE TABLE `DataCollectionPlanGroup` (
  `dataCollectionPlanGroupId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `sessionId` int(11) unsigned DEFAULT NULL,
  `blSampleId` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`dataCollectionPlanGroupId`),
  KEY `DataCollectionPlanGroup_ibfk1` (`sessionId`),
  KEY `DataCollectionPlanGroup_ibfk2` (`blSampleId`),
  CONSTRAINT `DataCollectionPlanGroup_ibfk1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON UPDATE CASCADE,
  CONSTRAINT `DataCollectionPlanGroup_ibfk2` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `DataReductionStatus` (
  `dataReductionStatusId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `dataCollectionId` int(11) unsigned NOT NULL,
  `status` varchar(15) DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`dataReductionStatusId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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

CREATE TABLE `Detector` (
  `detectorId` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
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
  PRIMARY KEY (`detectorId`),
  UNIQUE KEY `Detector_ibuk1` (`detectorSerialNumber`),
  KEY `Detector_FKIndex1` (`detectorType`,`detectorManufacturer`,`detectorModel`,`detectorPixelSizeHorizontal`,`detectorPixelSizeVertical`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=latin1 COMMENT='Detector table is linked to a dataCollection';

CREATE TABLE `Dewar` (
  `dewarId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `shippingId` int(10) unsigned DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL,
  `comments` tinytext,
  `storageLocation` varchar(45) DEFAULT NULL,
  `dewarStatus` varchar(45) DEFAULT NULL,
  `bltimeStamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `isStorageDewar` tinyint(1) DEFAULT '0',
  `barCode` varchar(45) DEFAULT NULL,
  `firstExperimentId` int(10) unsigned DEFAULT NULL,
  `customsValue` int(11) unsigned DEFAULT NULL,
  `transportValue` int(11) unsigned DEFAULT NULL,
  `trackingNumberToSynchrotron` varchar(30) DEFAULT NULL,
  `trackingNumberFromSynchrotron` varchar(30) DEFAULT NULL,
  `facilityCode` varchar(20) DEFAULT NULL COMMENT 'Unique barcode assigned to each dewar',
  `isReimbursed` tinyint(1) DEFAULT '0' COMMENT 'set this dewar as reimbursed by the user office',
  `type` enum('Dewar','Toolbox') NOT NULL DEFAULT 'Dewar',
  PRIMARY KEY (`dewarId`),
  UNIQUE KEY `barCode` (`barCode`),
  KEY `Dewar_FKIndex1` (`shippingId`),
  KEY `Dewar_FKIndex2` (`firstExperimentId`),
  KEY `Dewar_FKIndexStatus` (`dewarStatus`),
  KEY `Dewar_FKIndexCode` (`code`),
  CONSTRAINT `Dewar_ibfk_1` FOREIGN KEY (`shippingId`) REFERENCES `Shipping` (`shippingId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Dewar_ibfk_2` FOREIGN KEY (`firstExperimentId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=313844 DEFAULT CHARSET=latin1;

CREATE TABLE `DewarLocation` (
  `eventId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `dewarNumber` varchar(128) NOT NULL COMMENT 'Dewar number',
  `userId` varchar(128) DEFAULT NULL COMMENT 'User who locates the dewar',
  `dateTime` datetime DEFAULT NULL COMMENT 'Date and time of locatization',
  `locationName` varchar(128) DEFAULT NULL COMMENT 'Location of the dewar',
  `courierName` varchar(128) DEFAULT NULL COMMENT 'Carrier name who''s shipping back the dewar',
  `courierTrackingNumber` varchar(128) DEFAULT NULL COMMENT 'Tracking number of the shippment',
  PRIMARY KEY (`eventId`)
) ENGINE=InnoDB AUTO_INCREMENT=16069 DEFAULT CHARSET=latin1 COMMENT='ISPyB Dewar location table';

CREATE TABLE `DewarLocationList` (
  `locationId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `locationName` varchar(128) NOT NULL DEFAULT '' COMMENT 'Location',
  PRIMARY KEY (`locationId`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1 COMMENT='List of locations for dewars';

CREATE TABLE `DewarTransportHistory` (
  `DewarTransportHistoryId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `dewarId` int(10) unsigned DEFAULT NULL,
  `dewarStatus` varchar(45) NOT NULL,
  `storageLocation` varchar(45) DEFAULT NULL,
  `arrivalDate` datetime DEFAULT NULL,
  PRIMARY KEY (`DewarTransportHistoryId`),
  KEY `DewarTransportHistory_FKIndex1` (`dewarId`),
  CONSTRAINT `DewarTransportHistory_ibfk_1` FOREIGN KEY (`dewarId`) REFERENCES `Dewar` (`dewarId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=42029 DEFAULT CHARSET=latin1;

CREATE TABLE `DiffractionPlan` (
  `diffractionPlanId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `xmlDocumentId` int(10) unsigned DEFAULT NULL,
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
  `dataCollectionPlanGroupId` int(11) unsigned DEFAULT NULL,
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
  `presetForProposalId` int(10) unsigned DEFAULT NULL COMMENT 'Indicates this plan is available to all sessions on given proposal',
  `beamLineName` varchar(45) DEFAULT NULL COMMENT 'Indicates this plan is available to all sessions on given beamline',
  PRIMARY KEY (`diffractionPlanId`)
) ENGINE=InnoDB AUTO_INCREMENT=754600 DEFAULT CHARSET=latin1;

CREATE TABLE `DiffractionPlan_has_Detector` (
  `diffractionPlanId` int(11) unsigned NOT NULL,
  `detectorId` int(11) NOT NULL,
  `exposureTime` double DEFAULT NULL,
  `distance` double DEFAULT NULL,
  `orientation` double DEFAULT NULL,
  PRIMARY KEY (`diffractionPlanId`,`detectorId`),
  KEY `DiffractionPlan_has_Detector_ibfk2` (`detectorId`),
  CONSTRAINT `DiffractionPlan_has_Detector_ibfk1` FOREIGN KEY (`diffractionPlanId`) REFERENCES `DiffractionPlan` (`diffractionPlanId`),
  CONSTRAINT `DiffractionPlan_has_Detector_ibfk2` FOREIGN KEY (`detectorId`) REFERENCES `Detector` (`detectorId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `EMMicroscope` (
  `emMicroscopeId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `instrumentName` varchar(100) NOT NULL,
  `voltage` float DEFAULT NULL,
  `CS` float DEFAULT NULL COMMENT 'Unit: mm',
  `detectorPixelSize` float DEFAULT NULL,
  `C2aperture` float DEFAULT NULL,
  `ObjAperture` float DEFAULT NULL,
  `C2lens` float DEFAULT NULL,
  PRIMARY KEY (`emMicroscopeId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `EnergyScan` (
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
  KEY `ES_ibfk_3` (`blSubSampleId`),
  CONSTRAINT `ES_ibfk_1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ES_ibfk_2` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `ES_ibfk_3` FOREIGN KEY (`blSubSampleId`) REFERENCES `BLSubSample` (`blSubSampleId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=14716 DEFAULT CHARSET=latin1;

CREATE TABLE `Experiment` (
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
  KEY `fk_Experiment_To_session_idx` (`sessionId`),
  CONSTRAINT `fk_Experiment_To_session` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=18425 DEFAULT CHARSET=latin1;

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

CREATE TABLE `Frame` (
  `frameId` int(10) NOT NULL AUTO_INCREMENT,
  `filePath` varchar(255) DEFAULT NULL,
  `comments` varchar(45) DEFAULT NULL,
  `creationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `frameSetId` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`frameId`),
  KEY `FILE` (`filePath`)
) ENGINE=InnoDB AUTO_INCREMENT=1836295 DEFAULT CHARSET=latin1;

CREATE TABLE `FrameList` (
  `frameListId` int(10) NOT NULL AUTO_INCREMENT,
  `comments` int(10) DEFAULT NULL,
  PRIMARY KEY (`frameListId`)
) ENGINE=InnoDB AUTO_INCREMENT=226501 DEFAULT CHARSET=latin1;

CREATE TABLE `FrameSet` (
  `frameSetId` int(10) NOT NULL AUTO_INCREMENT,
  `runId` int(10) NOT NULL,
  `frameListId` int(10) DEFAULT NULL,
  `detectorId` int(10) DEFAULT NULL,
  `detectorDistance` varchar(45) DEFAULT NULL,
  `filePath` varchar(255) DEFAULT NULL,
  `internalPath` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`frameSetId`),
  KEY `FramesetToRun` (`runId`),
  KEY `FrameSetToFrameList` (`frameListId`),
  CONSTRAINT `FrameSetToFrameList` FOREIGN KEY (`frameListId`) REFERENCES `FrameList` (`frameListId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FramesetToRun` FOREIGN KEY (`runId`) REFERENCES `Run` (`runId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4209 DEFAULT CHARSET=latin1;

CREATE TABLE `FrameToList` (
  `frameToListId` int(10) NOT NULL AUTO_INCREMENT,
  `frameListId` int(10) NOT NULL,
  `frameId` int(10) NOT NULL,
  PRIMARY KEY (`frameToListId`),
  KEY `FrameToLisToFrameList` (`frameListId`),
  KEY `FrameToListToFrame` (`frameId`),
  CONSTRAINT `FrameToLisToFrameList` FOREIGN KEY (`frameListId`) REFERENCES `FrameList` (`frameListId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FrameToListToFrame` FOREIGN KEY (`frameId`) REFERENCES `Frame` (`frameId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2805367 DEFAULT CHARSET=latin1;

CREATE TABLE `GeometryClassname` (
  `geometryClassnameId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `geometryClassname` varchar(45) DEFAULT NULL,
  `geometryOrder` int(2) NOT NULL,
  PRIMARY KEY (`geometryClassnameId`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;

CREATE TABLE `GridInfo` (
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
  KEY `GridInfo_ibfk_2` (`dataCollectionGroupId`),
  CONSTRAINT `GridInfo_ibfk_1` FOREIGN KEY (`workflowMeshId`) REFERENCES `WorkflowMesh` (`workflowMeshId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `GridInfo_ibfk_2` FOREIGN KEY (`dataCollectionGroupId`) REFERENCES `DataCollectionGroup` (`dataCollectionGroupId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=163341 DEFAULT CHARSET=latin1;

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
  CONSTRAINT `Image_ibfk_3` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=53491586 DEFAULT CHARSET=latin1;

CREATE TABLE `ImageQualityIndicators` (
  `imageQualityIndicatorsId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `imageId` int(12) DEFAULT NULL,
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
  `dataCollectionId` int(11) unsigned DEFAULT NULL,
  `imageNumber` mediumint(8) unsigned DEFAULT NULL,
  PRIMARY KEY (`imageQualityIndicatorsId`),
  KEY `ImageQualityIndicatorsIdx1` (`imageId`),
  KEY `AutoProcProgramIdx1` (`autoProcProgramId`),
  CONSTRAINT `AutoProcProgramFk1` FOREIGN KEY (`autoProcProgramId`) REFERENCES `AutoProcProgram` (`autoProcProgramId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2854180 DEFAULT CHARSET=latin1;

CREATE TABLE `Imager` (
  `imagerId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `temperature` float DEFAULT NULL,
  `serial` varchar(45) DEFAULT NULL,
  `capacity` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`imagerId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `InputParameterWorkflow` (
  `inputParameterId` int(10) NOT NULL AUTO_INCREMENT,
  `workflowId` int(10) NOT NULL,
  `parameterType` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `comments` varchar(2048) DEFAULT NULL,
  PRIMARY KEY (`inputParameterId`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

CREATE TABLE `InspectionType` (
  `inspectionTypeId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`inspectionTypeId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Instruction` (
  `instructionId` int(10) NOT NULL AUTO_INCREMENT,
  `instructionSetId` int(10) NOT NULL,
  `order` int(11) NOT NULL,
  `comments` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`instructionId`),
  KEY `InstructionToInstructionSet` (`instructionSetId`),
  CONSTRAINT `InstructionToInstructionSet` FOREIGN KEY (`instructionSetId`) REFERENCES `InstructionSet` (`instructionSetId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `InstructionSet` (
  `instructionSetId` int(10) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`instructionSetId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `IspybAutoProcAttachment` (
  `autoProcAttachmentId` int(11) NOT NULL AUTO_INCREMENT,
  `fileName` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `step` enum('XDS','XSCALE','SCALA','SCALEPACK','TRUNCATE','DIMPLE') DEFAULT 'XDS' COMMENT 'step where the file is generated',
  `fileCategory` enum('input','output','log','correction') DEFAULT 'output',
  `hasGraph` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`autoProcAttachmentId`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=latin1 COMMENT='ISPyB autoProcAttachment files values';

CREATE TABLE `IspybCrystalClass` (
  `crystalClassId` int(11) NOT NULL AUTO_INCREMENT,
  `crystalClass_code` varchar(20) NOT NULL,
  `crystalClass_name` varchar(255) NOT NULL,
  PRIMARY KEY (`crystalClassId`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1 COMMENT='ISPyB crystal class values';

CREATE TABLE `IspybReference` (
  `referenceId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `referenceName` varchar(255) DEFAULT NULL COMMENT 'reference name',
  `referenceUrl` varchar(1024) DEFAULT NULL COMMENT 'url of the reference',
  `referenceBibtext` blob COMMENT 'bibtext value of the reference',
  `beamline` enum('All','ID14-4','ID23-1','ID23-2','ID29','ID30A-1','ID30A-2','XRF','AllXRF','Mesh') DEFAULT NULL COMMENT 'beamline involved',
  PRIMARY KEY (`referenceId`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB AUTO_INCREMENT=262060 DEFAULT CHARSET=latin1;

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
  `laboratoryExtPk` int(10) DEFAULT NULL,
  PRIMARY KEY (`laboratoryId`)
) ENGINE=InnoDB AUTO_INCREMENT=312756 DEFAULT CHARSET=latin1;

CREATE TABLE `Log4Stat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `priority` varchar(15) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `msg` varchar(255) DEFAULT NULL,
  `detail` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=19789 DEFAULT CHARSET=latin1;

CREATE TABLE `Login` (
  `loginId` int(11) NOT NULL AUTO_INCREMENT,
  `token` varchar(45) NOT NULL,
  `username` varchar(45) NOT NULL,
  `roles` varchar(1024) NOT NULL,
  `siteId` varchar(45) DEFAULT NULL,
  `authorized` varchar(1024) DEFAULT NULL,
  `expirationTime` datetime NOT NULL,
  PRIMARY KEY (`loginId`),
  KEY `Token` (`token`)
) ENGINE=InnoDB AUTO_INCREMENT=29197 DEFAULT CHARSET=latin1;

CREATE TABLE `MXMRRun` (
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
  KEY `mxMRRun_FK1` (`autoProcScalingId`),
  CONSTRAINT `mxMRRun_FK1` FOREIGN KEY (`autoProcScalingId`) REFERENCES `AutoProcScaling` (`autoProcScalingId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `MXMRRunBlob` (
  `mxMRRunBlobId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `mxMRRunId` int(11) unsigned NOT NULL,
  `view1` varchar(255) DEFAULT NULL,
  `view2` varchar(255) DEFAULT NULL,
  `view3` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`mxMRRunBlobId`),
  KEY `mxMRRunBlob_FK1` (`mxMRRunId`),
  CONSTRAINT `mxMRRunBlob_FK1` FOREIGN KEY (`mxMRRunId`) REFERENCES `MXMRRun` (`mxMRRunId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
  `electronDensity` float(7,5) DEFAULT NULL,
  PRIMARY KEY (`macromoleculeId`),
  KEY `MacromoleculeToSafetyLevel` (`safetyLevelId`),
  CONSTRAINT `MacromoleculeToSafetyLevel` FOREIGN KEY (`safetyLevelId`) REFERENCES `SafetyLevel` (`safetyLevelId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=60962 DEFAULT CHARSET=latin1;

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

CREATE TABLE `Measurement` (
  `measurementId` int(10) NOT NULL AUTO_INCREMENT,
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
  PRIMARY KEY (`measurementId`),
  KEY `SpecimenToSamplePlateWell` (`specimenId`),
  KEY `MeasurementToRun` (`runId`),
  CONSTRAINT `MeasurementToRun` FOREIGN KEY (`runId`) REFERENCES `Run` (`runId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `SpecimenToSamplePlateWell` FOREIGN KEY (`specimenId`) REFERENCES `Specimen` (`specimenId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=291103 DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB AUTO_INCREMENT=291103 DEFAULT CHARSET=latin1;

CREATE TABLE `MeasurementUnit` (
  `measurementUnitId` int(10) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `unitType` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`measurementUnitId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB AUTO_INCREMENT=151259 DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB AUTO_INCREMENT=653393 DEFAULT CHARSET=latin1;

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

CREATE TABLE `ModelList` (
  `modelListId` int(10) NOT NULL AUTO_INCREMENT,
  `nsdFilePath` varchar(255) DEFAULT NULL,
  `chi2RgFilePath` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`modelListId`)
) ENGINE=InnoDB AUTO_INCREMENT=43748 DEFAULT CHARSET=latin1;

CREATE TABLE `ModelToList` (
  `modelToListId` int(10) NOT NULL AUTO_INCREMENT,
  `modelId` int(10) NOT NULL,
  `modelListId` int(10) NOT NULL,
  PRIMARY KEY (`modelToListId`),
  KEY `ModelToListToList` (`modelListId`),
  KEY `ModelToListToModel` (`modelId`),
  CONSTRAINT `ModelToListToList` FOREIGN KEY (`modelListId`) REFERENCES `ModelList` (`modelListId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `ModelToListToModel` FOREIGN KEY (`modelId`) REFERENCES `Model` (`modelId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=530477 DEFAULT CHARSET=latin1;

CREATE TABLE `MotionCorrection` (
  `motionCorrectionId` int(11) NOT NULL AUTO_INCREMENT,
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
  `createdTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`motionCorrectionId`),
  KEY `fk_MotionCorrection_1_idx` (`movieId`),
  KEY `movieId` (`movieId`),
  CONSTRAINT `fk_MotionCorrection_1` FOREIGN KEY (`movieId`) REFERENCES `Movie` (`movieId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5389 DEFAULT CHARSET=utf8mb4;

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

CREATE TABLE `Movie` (
  `movieId` int(11) NOT NULL AUTO_INCREMENT,
  `dataCollectionId` int(11) unsigned DEFAULT NULL,
  `movieNumber` int(11) DEFAULT NULL,
  `movieFullPath` varchar(255) DEFAULT NULL,
  `positionX` varchar(45) DEFAULT NULL,
  `positionY` varchar(45) DEFAULT NULL,
  `micrographFullPath` varchar(255) DEFAULT NULL,
  `micrographSnapshotFullPath` varchar(255) DEFAULT NULL,
  `xmlMetaDataFullPath` varchar(255) DEFAULT NULL,
  `dosePerImage` varchar(45) DEFAULT NULL,
  `createdTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`movieId`),
  KEY `dataCollectionToMovie_idx` (`dataCollectionId`),
  CONSTRAINT `dataCollectionToMovie` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5917 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `PDB` (
  `pdbId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `contents` mediumtext,
  `code` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`pdbId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `PDBEntry` (
  `pdbEntryId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `autoProcProgramId` int(11) unsigned DEFAULT NULL,
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

CREATE TABLE `PDBEntry_has_AutoProcProgram` (
  `pdbEntryHasAutoProcId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `pdbEntryId` int(11) unsigned NOT NULL,
  `autoProcProgramId` int(11) unsigned NOT NULL,
  `distance` float DEFAULT NULL,
  PRIMARY KEY (`pdbEntryHasAutoProcId`),
  KEY `pdbEntry_AutoProcProgramIdx1` (`pdbEntryId`),
  KEY `pdbEntry_AutoProcProgramIdx2` (`autoProcProgramId`),
  CONSTRAINT `pdbEntry_AutoProcProgram_FK1` FOREIGN KEY (`pdbEntryId`) REFERENCES `PDBEntry` (`pdbEntryId`) ON DELETE CASCADE,
  CONSTRAINT `pdbEntry_AutoProcProgram_FK2` FOREIGN KEY (`autoProcProgramId`) REFERENCES `AutoProcProgram` (`autoProcProgramId`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `PHPSession` (
  `id` varchar(50) NOT NULL,
  `accessDate` datetime DEFAULT NULL,
  `data` varchar(4000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Particle` (
  `particleId` int(11) unsigned NOT NULL,
  `dataCollectionId` int(11) unsigned NOT NULL,
  `x` float DEFAULT NULL,
  `y` float DEFAULT NULL,
  PRIMARY KEY (`particleId`),
  KEY `Particle_FKIND1` (`dataCollectionId`),
  CONSTRAINT `Particle_FK1` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Permission` (
  `permissionId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(15) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`permissionId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
  `cache` text,
  PRIMARY KEY (`personId`),
  KEY `Person_FKIndex1` (`laboratoryId`),
  KEY `Person_FKIndexFamilyName` (`familyName`),
  KEY `Person_FKIndex_Login` (`login`),
  KEY `siteId` (`siteId`),
  CONSTRAINT `Person_ibfk_1` FOREIGN KEY (`laboratoryId`) REFERENCES `Laboratory` (`laboratoryId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=400565 DEFAULT CHARSET=latin1;

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
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`phasingId`),
  KEY `Phasing_FKIndex1` (`phasingAnalysisId`),
  KEY `Phasing_FKIndex2` (`phasingProgramRunId`),
  KEY `Phasing_FKIndex3` (`spaceGroupId`),
  CONSTRAINT `Phasing_phasingAnalysisfk_1` FOREIGN KEY (`phasingAnalysisId`) REFERENCES `PhasingAnalysis` (`phasingAnalysisId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Phasing_phasingProgramRunfk_1` FOREIGN KEY (`phasingProgramRunId`) REFERENCES `PhasingProgramRun` (`phasingProgramRunId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Phasing_spaceGroupfk_1` FOREIGN KEY (`spaceGroupId`) REFERENCES `SpaceGroup` (`spaceGroupId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=106310 DEFAULT CHARSET=latin1;

CREATE TABLE `PhasingAnalysis` (
  `phasingAnalysisId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`phasingAnalysisId`)
) ENGINE=InnoDB AUTO_INCREMENT=135302 DEFAULT CHARSET=latin1;

CREATE TABLE `PhasingProgramAttachment` (
  `phasingProgramAttachmentId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `phasingProgramRunId` int(11) unsigned NOT NULL COMMENT 'Related program item',
  `fileType` enum('DSIGMA_RESOLUTION','OCCUPANCY_SITENUMBER','CONTRAST_CYCLE','CCALL_CCWEAK','IMAGE','Map','Logfile','PDB','CSV','INS','RES','TXT') DEFAULT NULL COMMENT 'file type',
  `fileName` varchar(45) DEFAULT NULL COMMENT 'file name',
  `filePath` varchar(255) DEFAULT NULL COMMENT 'file path',
  `recordTimeStamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`phasingProgramAttachmentId`),
  KEY `PhasingProgramAttachment_FKIndex1` (`phasingProgramRunId`),
  CONSTRAINT `Phasing_phasingProgramAttachmentfk_1` FOREIGN KEY (`phasingProgramRunId`) REFERENCES `PhasingProgramRun` (`phasingProgramRunId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=337490 DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB AUTO_INCREMENT=456485 DEFAULT CHARSET=latin1;

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
  CONSTRAINT `PhasingStatistics_phasingHasScalingfk_1` FOREIGN KEY (`phasingHasScalingId1`) REFERENCES `Phasing_has_Scaling` (`phasingHasScalingId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PhasingStatistics_phasingHasScalingfk_2` FOREIGN KEY (`phasingHasScalingId2`) REFERENCES `Phasing_has_Scaling` (`phasingHasScalingId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_PhasingStatistics_phasingStep` FOREIGN KEY (`phasingStepId`) REFERENCES `PhasingStep` (`phasingStepId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=821230 DEFAULT CHARSET=latin1;

CREATE TABLE `PhasingStep` (
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
  KEY `FK_phasingAnalysis_id` (`phasingAnalysisId`),
  CONSTRAINT `FK_autoprocScaling` FOREIGN KEY (`autoProcScalingId`) REFERENCES `AutoProcScaling` (`autoProcScalingId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_program` FOREIGN KEY (`programRunId`) REFERENCES `PhasingProgramRun` (`phasingProgramRunId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_spacegroup` FOREIGN KEY (`spaceGroupId`) REFERENCES `SpaceGroup` (`spaceGroupId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=321114 DEFAULT CHARSET=latin1;

CREATE TABLE `Phasing_has_Scaling` (
  `phasingHasScalingId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `phasingAnalysisId` int(11) unsigned NOT NULL COMMENT 'Related phasing analysis item',
  `autoProcScalingId` int(10) unsigned NOT NULL COMMENT 'Related autoProcScaling item',
  `datasetNumber` int(11) DEFAULT NULL COMMENT 'serial number of the dataset and always reserve 0 for the reference',
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`phasingHasScalingId`),
  KEY `PhasingHasScaling_FKIndex1` (`phasingAnalysisId`),
  KEY `PhasingHasScaling_FKIndex2` (`autoProcScalingId`),
  CONSTRAINT `PhasingHasScaling_autoProcScalingfk_1` FOREIGN KEY (`autoProcScalingId`) REFERENCES `AutoProcScaling` (`autoProcScalingId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PhasingHasScaling_phasingAnalysisfk_1` FOREIGN KEY (`phasingAnalysisId`) REFERENCES `PhasingAnalysis` (`phasingAnalysisId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=135302 DEFAULT CHARSET=latin1;

CREATE TABLE `PlateGroup` (
  `plateGroupId` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `storageTemperature` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`plateGroupId`)
) ENGINE=InnoDB AUTO_INCREMENT=13801 DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

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

CREATE TABLE `Project` (
  `projectId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `personId` int(11) unsigned DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL,
  `acronym` varchar(100) DEFAULT NULL,
  `owner` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`projectId`),
  KEY `Project_FK1` (`personId`),
  CONSTRAINT `Project_FK1` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Project_has_BLSample` (
  `projectId` int(11) unsigned NOT NULL,
  `blSampleId` int(11) unsigned NOT NULL,
  PRIMARY KEY (`projectId`,`blSampleId`),
  KEY `Project_has_BLSample_FK2` (`blSampleId`),
  CONSTRAINT `Project_has_BLSample_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Project_has_BLSample_FK2` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Project_has_DCGroup` (
  `projectId` int(11) unsigned NOT NULL,
  `dataCollectionGroupId` int(11) NOT NULL,
  PRIMARY KEY (`projectId`,`dataCollectionGroupId`),
  KEY `Project_has_DCGroup_FK2` (`dataCollectionGroupId`),
  CONSTRAINT `Project_has_DCGroup_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Project_has_DCGroup_FK2` FOREIGN KEY (`dataCollectionGroupId`) REFERENCES `DataCollectionGroup` (`dataCollectionGroupId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Project_has_EnergyScan` (
  `projectId` int(11) unsigned NOT NULL,
  `energyScanId` int(11) unsigned NOT NULL,
  PRIMARY KEY (`projectId`,`energyScanId`),
  KEY `project_has_energyscan_FK2` (`energyScanId`),
  CONSTRAINT `project_has_energyscan_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `project_has_energyscan_FK2` FOREIGN KEY (`energyScanId`) REFERENCES `EnergyScan` (`energyScanId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Project_has_Person` (
  `projectId` int(11) unsigned NOT NULL,
  `personId` int(11) unsigned NOT NULL,
  PRIMARY KEY (`projectId`,`personId`),
  KEY `project_has_person_FK2` (`personId`),
  CONSTRAINT `project_has_person_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE,
  CONSTRAINT `project_has_person_FK2` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Project_has_Protein` (
  `projectId` int(11) unsigned NOT NULL,
  `proteinId` int(11) unsigned NOT NULL,
  PRIMARY KEY (`projectId`,`proteinId`),
  KEY `project_has_protein_FK2` (`proteinId`),
  CONSTRAINT `project_has_protein_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE,
  CONSTRAINT `project_has_protein_FK2` FOREIGN KEY (`proteinId`) REFERENCES `Protein` (`proteinId`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Project_has_Session` (
  `projectId` int(11) unsigned NOT NULL,
  `sessionId` int(11) unsigned NOT NULL,
  PRIMARY KEY (`projectId`,`sessionId`),
  KEY `project_has_session_FK2` (`sessionId`),
  CONSTRAINT `project_has_session_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `project_has_session_FK2` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Project_has_Shipping` (
  `projectId` int(11) unsigned NOT NULL,
  `shippingId` int(11) unsigned NOT NULL,
  PRIMARY KEY (`projectId`,`shippingId`),
  KEY `project_has_shipping_FK2` (`shippingId`),
  CONSTRAINT `project_has_shipping_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE,
  CONSTRAINT `project_has_shipping_FK2` FOREIGN KEY (`shippingId`) REFERENCES `Shipping` (`shippingId`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Project_has_User` (
  `projecthasuserid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `projectid` int(11) unsigned NOT NULL,
  `username` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`projecthasuserid`),
  KEY `Project_Has_user_FK1` (`projectid`),
  CONSTRAINT `Project_Has_user_FK1` FOREIGN KEY (`projectid`) REFERENCES `Project` (`projectId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Project_has_XFEFSpectrum` (
  `projectId` int(11) unsigned NOT NULL,
  `xfeFluorescenceSpectrumId` int(11) unsigned NOT NULL,
  PRIMARY KEY (`projectId`,`xfeFluorescenceSpectrumId`),
  KEY `project_has_xfefspectrum_FK2` (`xfeFluorescenceSpectrumId`),
  CONSTRAINT `project_has_xfefspectrum_FK1` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectId`) ON DELETE CASCADE,
  CONSTRAINT `project_has_xfefspectrum_FK2` FOREIGN KEY (`xfeFluorescenceSpectrumId`) REFERENCES `XFEFluorescenceSpectrum` (`xfeFluorescenceSpectrumId`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Proposal` (
  `proposalId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `personId` int(10) unsigned NOT NULL DEFAULT '0',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `proposalCode` varchar(45) DEFAULT NULL,
  `proposalNumber` varchar(45) DEFAULT NULL,
  `proposalType` varchar(2) DEFAULT NULL COMMENT 'Proposal type: MX, BX',
  `bltimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `externalId` binary(16) DEFAULT NULL,
  PRIMARY KEY (`proposalId`),
  KEY `Proposal_FKIndex1` (`personId`),
  KEY `Proposal_FKIndexCodeNumber` (`proposalCode`,`proposalNumber`),
  CONSTRAINT `Proposal_ibfk_1` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8419 DEFAULT CHARSET=latin1;

CREATE TABLE `ProposalHasPerson` (
  `proposalHasPersonId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `proposalId` int(10) unsigned NOT NULL,
  `personId` int(10) unsigned NOT NULL,
  PRIMARY KEY (`proposalHasPersonId`),
  KEY `fk_ProposalHasPerson_Proposal` (`proposalId`),
  KEY `fk_ProposalHasPerson_Personal` (`personId`),
  CONSTRAINT `fk_ProposalHasPerson_Personal` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ProposalHasPerson_Proposal` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4458 DEFAULT CHARSET=latin1;

CREATE TABLE `Protein` (
  `proteinId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `proposalId` int(10) unsigned NOT NULL DEFAULT '0',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `acronym` varchar(45) DEFAULT NULL,
  `safetyLevel` enum('GREEN','YELLOW','RED') DEFAULT NULL,
  `molecularMass` double DEFAULT NULL,
  `proteinType` varchar(45) DEFAULT NULL,
  `sequence` text,
  `personId` int(10) unsigned DEFAULT NULL,
  `bltimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `isCreatedBySampleSheet` tinyint(1) DEFAULT '0',
  `externalId` binary(16) DEFAULT NULL,
  `componentTypeId` int(11) unsigned DEFAULT NULL,
  `modId` varchar(20) DEFAULT NULL,
  `concentrationTypeId` int(11) unsigned DEFAULT NULL,
  `global` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`proteinId`),
  KEY `Protein_FKIndex1` (`proposalId`),
  KEY `ProteinAcronym_Index` (`proposalId`,`acronym`),
  KEY `Protein_FKIndex2` (`personId`),
  KEY `Protein_Index2` (`acronym`) USING BTREE,
  KEY `protein_fk3` (`componentTypeId`),
  CONSTRAINT `Protein_ibfk_1` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `protein_fk3` FOREIGN KEY (`componentTypeId`) REFERENCES `ComponentType` (`componentTypeId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=376114 DEFAULT CHARSET=latin1;

CREATE TABLE `Protein_has_Lattice` (
  `proteinId` int(10) unsigned NOT NULL,
  `cell_a` double DEFAULT NULL,
  `cell_b` double DEFAULT NULL,
  `cell_c` double DEFAULT NULL,
  `cell_alpha` double DEFAULT NULL,
  `cell_beta` double DEFAULT NULL,
  `cell_gamma` double DEFAULT NULL,
  PRIMARY KEY (`proteinId`),
  CONSTRAINT `Protein_has_Lattice_ibfk1` FOREIGN KEY (`proteinId`) REFERENCES `Protein` (`proteinId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Protein_has_PDB` (
  `proteinhaspdbid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `proteinid` int(11) unsigned NOT NULL,
  `pdbid` int(11) unsigned NOT NULL,
  PRIMARY KEY (`proteinhaspdbid`),
  KEY `Protein_Has_PDB_fk1` (`proteinid`),
  KEY `Protein_Has_PDB_fk2` (`pdbid`),
  CONSTRAINT `Protein_Has_PDB_fk1` FOREIGN KEY (`proteinid`) REFERENCES `Protein` (`proteinId`),
  CONSTRAINT `Protein_Has_PDB_fk2` FOREIGN KEY (`pdbid`) REFERENCES `PDB` (`pdbId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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

CREATE TABLE `RobotAction` (
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
  KEY `RobotAction_FK2` (`blsampleId`),
  CONSTRAINT `RobotAction_FK1` FOREIGN KEY (`blsessionId`) REFERENCES `BLSession` (`sessionId`),
  CONSTRAINT `RobotAction_FK2` FOREIGN KEY (`blsampleId`) REFERENCES `BLSample` (`blSampleId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 COMMENT='Robot actions as reported by MXCube';

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
) ENGINE=InnoDB AUTO_INCREMENT=134836 DEFAULT CHARSET=latin1;

CREATE TABLE `SW_onceToken` (
  `onceTokenId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `token` varchar(128) DEFAULT NULL,
  `personId` int(10) unsigned DEFAULT NULL,
  `proposalId` int(10) unsigned DEFAULT NULL,
  `validity` varchar(200) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`onceTokenId`),
  KEY `SW_onceToken_fk1` (`personId`),
  KEY `SW_onceToken_fk2` (`proposalId`),
  CONSTRAINT `SW_onceToken_fk1` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`),
  CONSTRAINT `SW_onceToken_fk2` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='One-time use tokens needed for token auth in order to grant access to file downloads and webcams (and some images)';

CREATE TABLE `SafetyLevel` (
  `safetyLevelId` int(10) NOT NULL AUTO_INCREMENT,
  `code` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`safetyLevelId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

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
  CONSTRAINT `SamplePlateToType` FOREIGN KEY (`plateTypeId`) REFERENCES `PlateType` (`PlateTypeId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=41401 DEFAULT CHARSET=latin1;

CREATE TABLE `SamplePlatePosition` (
  `samplePlatePositionId` int(10) NOT NULL AUTO_INCREMENT,
  `samplePlateId` int(10) NOT NULL,
  `rowNumber` int(11) DEFAULT NULL,
  `columnNumber` int(11) DEFAULT NULL,
  `volume` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`samplePlatePositionId`),
  KEY `PlatePositionToPlate` (`samplePlateId`),
  CONSTRAINT `PlatePositionToPlate` FOREIGN KEY (`samplePlateId`) REFERENCES `SamplePlate` (`samplePlateId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=117475 DEFAULT CHARSET=latin1;

CREATE TABLE `SaxsDataCollection` (
  `dataCollectionId` int(10) NOT NULL AUTO_INCREMENT,
  `experimentId` int(10) NOT NULL,
  `comments` varchar(5120) DEFAULT NULL,
  PRIMARY KEY (`dataCollectionId`),
  KEY `SaxsDataCollectionToExperiment` (`experimentId`),
  CONSTRAINT `SaxsDataCollectionToExperiment` FOREIGN KEY (`experimentId`) REFERENCES `Experiment` (`experimentId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=97035 DEFAULT CHARSET=latin1;

CREATE TABLE `ScanParametersModel` (
  `scanParametersModelId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `scanParametersServiceId` int(10) unsigned DEFAULT NULL,
  `dataCollectionPlanId` int(11) unsigned DEFAULT NULL,
  `modelNumber` tinyint(3) unsigned DEFAULT NULL,
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

CREATE TABLE `ScanParametersService` (
  `scanParametersServiceId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`scanParametersServiceId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Schedule` (
  `scheduleId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`scheduleId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `ScheduleComponent` (
  `scheduleComponentId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `scheduleId` int(11) unsigned NOT NULL,
  `inspectionTypeId` int(11) unsigned DEFAULT NULL,
  `offset_hours` int(11) DEFAULT NULL,
  PRIMARY KEY (`scheduleComponentId`),
  KEY `ScheduleComponent_fk2` (`inspectionTypeId`),
  KEY `ScheduleComponent_idx1` (`scheduleId`),
  CONSTRAINT `ScheduleComponent_fk1` FOREIGN KEY (`scheduleId`) REFERENCES `Schedule` (`scheduleId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ScheduleComponent_fk2` FOREIGN KEY (`inspectionTypeId`) REFERENCES `InspectionType` (`inspectionTypeId`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `SchemaStatus` (
  `schemaStatusId` int(11) NOT NULL AUTO_INCREMENT,
  `scriptName` varchar(100) NOT NULL,
  `schemaStatus` varchar(10) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`schemaStatusId`),
  UNIQUE KEY `scriptName` (`scriptName`)
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=latin1;

CREATE TABLE `Screen` (
  `screenId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `proposalId` int(10) unsigned DEFAULT NULL,
  `global` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`screenId`),
  KEY `Screen_fk1` (`proposalId`),
  CONSTRAINT `Screen_fk1` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `ScreenComponent` (
  `screenComponentId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `screenComponentGroupId` int(11) unsigned NOT NULL,
  `componentId` int(11) unsigned DEFAULT NULL,
  `concentration` float DEFAULT NULL,
  `pH` float DEFAULT NULL,
  PRIMARY KEY (`screenComponentId`),
  KEY `ScreenComponent_fk1` (`screenComponentGroupId`),
  KEY `ScreenComponent_fk2` (`componentId`),
  CONSTRAINT `ScreenComponent_fk1` FOREIGN KEY (`screenComponentGroupId`) REFERENCES `ScreenComponentGroup` (`screenComponentGroupId`),
  CONSTRAINT `ScreenComponent_fk2` FOREIGN KEY (`componentId`) REFERENCES `Protein` (`proteinId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `ScreenComponentGroup` (
  `screenComponentGroupId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `screenId` int(11) unsigned NOT NULL,
  `position` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`screenComponentGroupId`),
  KEY `ScreenComponentGroup_fk1` (`screenId`),
  CONSTRAINT `ScreenComponentGroup_fk1` FOREIGN KEY (`screenId`) REFERENCES `Screen` (`screenId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Screening` (
  `screeningId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `diffractionPlanId` int(10) unsigned DEFAULT NULL COMMENT 'references DiffractionPlan',
  `dataCollectionGroupId` int(11) DEFAULT NULL,
  `dataCollectionId` int(11) unsigned DEFAULT NULL,
  `bltimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `programVersion` varchar(45) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `shortComments` varchar(20) DEFAULT NULL,
  `xmlSampleInformation` longblob,
  PRIMARY KEY (`screeningId`),
  KEY `Screening_FKIndexDiffractionPlanId` (`diffractionPlanId`),
  KEY `dcgroupId` (`dataCollectionGroupId`),
  CONSTRAINT `Screening_ibfk_1` FOREIGN KEY (`dataCollectionGroupId`) REFERENCES `DataCollectionGroup` (`dataCollectionGroupId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=472616 DEFAULT CHARSET=latin1;

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
  `screeningSuccess` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`screeningOutputId`),
  KEY `ScreeningOutput_FKIndex1` (`screeningId`),
  CONSTRAINT `ScreeningOutput_ibfk_1` FOREIGN KEY (`screeningId`) REFERENCES `Screening` (`screeningId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=472556 DEFAULT CHARSET=latin1;

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
  `bltimeStamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `labelitIndexing` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`screeningOutputLatticeId`),
  KEY `ScreeningOutputLattice_FKIndex1` (`screeningOutputId`),
  CONSTRAINT `ScreeningOutputLattice_ibfk_1` FOREIGN KEY (`screeningOutputId`) REFERENCES `ScreeningOutput` (`screeningOutputId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=321974 DEFAULT CHARSET=latin1;

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

CREATE TABLE `ScreeningRankSet` (
  `screeningRankSetId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `rankEngine` varchar(255) DEFAULT NULL,
  `rankingProjectFileName` varchar(255) DEFAULT NULL,
  `rankingSummaryFileName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`screeningRankSetId`)
) ENGINE=InnoDB AUTO_INCREMENT=993 DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB AUTO_INCREMENT=249165 DEFAULT CHARSET=latin1;

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
  `resolution` float DEFAULT NULL,
  PRIMARY KEY (`screeningStrategySubWedgeId`),
  KEY `ScreeningStrategySubWedge_FK1` (`screeningStrategyWedgeId`),
  CONSTRAINT `ScreeningStrategySubWedge_FK1` FOREIGN KEY (`screeningStrategyWedgeId`) REFERENCES `ScreeningStrategyWedge` (`screeningStrategyWedgeId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=257824 DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB AUTO_INCREMENT=244162 DEFAULT CHARSET=latin1;

CREATE TABLE `SessionType` (
  `sessionTypeId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sessionId` int(10) unsigned NOT NULL,
  `typeName` varchar(31) NOT NULL,
  PRIMARY KEY (`sessionTypeId`),
  KEY `SessionType_FKIndex1` (`sessionId`),
  CONSTRAINT `SessionType_ibfk_1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Session_has_Person` (
  `sessionId` int(10) unsigned NOT NULL DEFAULT '0',
  `personId` int(10) unsigned NOT NULL DEFAULT '0',
  `role` enum('Local Contact','Local Contact 2','Staff','Team Leader','Co-Investigator','Principal Investigator','Alternate Contact') DEFAULT NULL,
  `remote` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`sessionId`,`personId`),
  KEY `Session_has_Person_FKIndex1` (`sessionId`),
  KEY `Session_has_Person_FKIndex2` (`personId`),
  CONSTRAINT `Session_has_Person_ibfk_1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Session_has_Person_ibfk_2` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
  `safetyLevel` varchar(8) DEFAULT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=309226 DEFAULT CHARSET=latin1;

CREATE TABLE `ShippingHasSession` (
  `shippingId` int(10) unsigned NOT NULL,
  `sessionId` int(10) unsigned NOT NULL,
  PRIMARY KEY (`shippingId`,`sessionId`),
  KEY `ShippingHasSession_FKIndex1` (`shippingId`),
  KEY `ShippingHasSession_FKIndex2` (`sessionId`),
  CONSTRAINT `ShippingHasSession_ibfk_1` FOREIGN KEY (`shippingId`) REFERENCES `Shipping` (`shippingId`),
  CONSTRAINT `ShippingHasSession_ibfk_2` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB AUTO_INCREMENT=120322 DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;

CREATE TABLE `Structure` (
  `structureId` int(10) NOT NULL AUTO_INCREMENT,
  `macromoleculeId` int(10) DEFAULT NULL,
  `crystalId` int(10) unsigned DEFAULT NULL,
  `blSampleId` int(10) unsigned DEFAULT NULL,
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
  PRIMARY KEY (`structureId`),
  KEY `StructureToMacromolecule` (`macromoleculeId`),
  KEY `StructureToCrystal_idx` (`crystalId`),
  KEY `StructureToBlSample_idx` (`blSampleId`),
  CONSTRAINT `StructureToBlSample` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `StructureToCrystal` FOREIGN KEY (`crystalId`) REFERENCES `Crystal` (`crystalId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `StructureToMacromolecule` FOREIGN KEY (`macromoleculeId`) REFERENCES `Macromolecule` (`macromoleculeId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB AUTO_INCREMENT=62321 DEFAULT CHARSET=latin1;

CREATE TABLE `SubtractionToAbInitioModel` (
  `subtractionToAbInitioModelId` int(10) NOT NULL AUTO_INCREMENT,
  `abInitioId` int(10) DEFAULT NULL,
  `subtractionId` int(10) DEFAULT NULL,
  PRIMARY KEY (`subtractionToAbInitioModelId`),
  KEY `substractionToAbInitioModelToAbinitioModel` (`abInitioId`),
  KEY `ubstractionToSubstraction` (`subtractionId`),
  CONSTRAINT `substractionToAbInitioModelToAbinitioModel` FOREIGN KEY (`abInitioId`) REFERENCES `AbInitioModel` (`abInitioModelId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `substractionToSubstraction` FOREIGN KEY (`subtractionId`) REFERENCES `Subtraction` (`subtractionId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=40654 DEFAULT CHARSET=latin1;

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

CREATE TABLE `UserGroup` (
  `userGroupId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(31) NOT NULL,
  PRIMARY KEY (`userGroupId`),
  UNIQUE KEY `UserGroup_idx1` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `UserGroup_has_Permission` (
  `userGroupId` int(11) unsigned NOT NULL,
  `permissionId` int(11) unsigned NOT NULL,
  PRIMARY KEY (`userGroupId`,`permissionId`),
  KEY `UserGroup_has_Permission_fk2` (`permissionId`),
  CONSTRAINT `UserGroup_has_Permission_fk1` FOREIGN KEY (`userGroupId`) REFERENCES `UserGroup` (`userGroupId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `UserGroup_has_Permission_fk2` FOREIGN KEY (`permissionId`) REFERENCES `Permission` (`permissionId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `UserGroup_has_Person` (
  `userGroupId` int(11) unsigned NOT NULL,
  `personId` int(10) unsigned NOT NULL,
  PRIMARY KEY (`userGroupId`,`personId`),
  KEY `userGroup_has_Person_fk2` (`personId`),
  CONSTRAINT `userGroup_has_Person_fk1` FOREIGN KEY (`userGroupId`) REFERENCES `UserGroup` (`userGroupId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `userGroup_has_Person_fk2` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Workflow` (
  `workflowId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `workflowTitle` varchar(255) DEFAULT NULL,
  `workflowType` enum('Characterisation','Undefined','BioSAXS Post Processing','EnhancedCharacterisation','LineScan','MeshScan','Dehydration','KappaReorientation','BurnStrategy','XrayCentering','DiffractionTomography','TroubleShooting','VisualReorientation','HelicalCharacterisation','GroupedProcessing','MXPressE','MXPressO','MXPressL','MXScore','MXPressI','MXPressM','MXPressA','CollectAndSpectra','LowDoseDC','EnergyInterleavedMAD','MXPressF','MXPressH','MXPressP','MXPressP_SAD','MXPressR','MXPressR_180','MXPressR_dehydration','MeshAndCollect','MeshAndCollectFromFile') DEFAULT NULL,
  `workflowTypeId` int(11) DEFAULT NULL,
  `comments` varchar(1024) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `resultFilePath` varchar(255) DEFAULT NULL,
  `logFilePath` varchar(255) DEFAULT NULL,
  `recordTimeStamp` datetime DEFAULT NULL COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`workflowId`)
) ENGINE=InnoDB AUTO_INCREMENT=153305 DEFAULT CHARSET=latin1;

CREATE TABLE `WorkflowDehydration` (
  `workflowDehydrationId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key (auto-incremented)',
  `workflowId` int(11) unsigned NOT NULL COMMENT 'Related workflow',
  `dataFilePath` varchar(255) DEFAULT NULL,
  `recordTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time',
  PRIMARY KEY (`workflowDehydrationId`),
  KEY `WorkflowDehydration_FKIndex1` (`workflowId`),
  CONSTRAINT `WorkflowDehydration_workflowfk_1` FOREIGN KEY (`workflowId`) REFERENCES `Workflow` (`workflowId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
  CONSTRAINT `WorkflowMesh_ibfk_2` FOREIGN KEY (`bestImageId`) REFERENCES `Image` (`imageId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `WorkflowMesh_workflowfk_1` FOREIGN KEY (`workflowId`) REFERENCES `Workflow` (`workflowId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=163359 DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB AUTO_INCREMENT=237905 DEFAULT CHARSET=latin1;

CREATE TABLE `WorkflowType` (
  `workflowTypeId` int(11) NOT NULL AUTO_INCREMENT,
  `workflowTypeName` varchar(45) DEFAULT NULL,
  `comments` varchar(2048) DEFAULT NULL,
  `recordTimeStamp` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`workflowTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;

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
  `blSubSampleId` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`xfeFluorescenceSpectrumId`),
  KEY `XFEFluorescnceSpectrum_FKIndex1` (`blSampleId`),
  KEY `XFEFluorescnceSpectrum_FKIndex2` (`sessionId`),
  KEY `XFE_ibfk_3` (`blSubSampleId`),
  CONSTRAINT `XFE_ibfk_1` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `XFE_ibfk_2` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample` (`blSampleId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `XFE_ibfk_3` FOREIGN KEY (`blSubSampleId`) REFERENCES `BLSubSample` (`blSubSampleId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5385 DEFAULT CHARSET=latin1;

CREATE TABLE `XRFFluorescenceMapping` (
  `xrfFluorescenceMappingId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `xrfFluorescenceMappingROIId` int(11) unsigned NOT NULL,
  `dataCollectionId` int(11) unsigned NOT NULL,
  `imageNumber` int(10) unsigned NOT NULL,
  `counts` int(10) unsigned NOT NULL,
  PRIMARY KEY (`xrfFluorescenceMappingId`),
  KEY `XRFFluorescenceMapping_ibfk1` (`xrfFluorescenceMappingROIId`),
  KEY `XRFFluorescenceMapping_ibfk2` (`dataCollectionId`),
  CONSTRAINT `XRFFluorescenceMapping_ibfk1` FOREIGN KEY (`xrfFluorescenceMappingROIId`) REFERENCES `XRFFluorescenceMappingROI` (`xrfFluorescenceMappingROIId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `XRFFluorescenceMapping_ibfk2` FOREIGN KEY (`dataCollectionId`) REFERENCES `DataCollection` (`dataCollectionId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `XRFFluorescenceMappingROI` (
  `xrfFluorescenceMappingROIId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `startEnergy` float NOT NULL,
  `endEnergy` float NOT NULL,
  `element` varchar(2) DEFAULT NULL,
  `edge` varchar(2) DEFAULT NULL COMMENT 'In future may be changed to enum(K, L)',
  `r` tinyint(3) unsigned DEFAULT NULL COMMENT 'R colour component',
  `g` tinyint(3) unsigned DEFAULT NULL COMMENT 'G colour component',
  `b` tinyint(3) unsigned DEFAULT NULL COMMENT 'B colour component',
  PRIMARY KEY (`xrfFluorescenceMappingROIId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- views

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `V_AnalysisInfo` AS (select `exp`.`creationDate` AS `experimentCreationDate`,(select `r`.`timeStart` from `Run` `r` where (`r`.`runId` = `m`.`runId`)) AS `timeStart`,`dc`.`dataCollectionId` AS `dataCollectionId`,`m`.`measurementId` AS `measurementId`,`p`.`proposalId` AS `proposalId`,`p`.`proposalCode` AS `proposalCode`,`p`.`proposalNumber` AS `proposalNumber`,`m`.`priorityLevelId` AS `priorityLevelId`,`m`.`code` AS `code`,`m`.`exposureTemperature` AS `exposureTemperature`,`m`.`transmission` AS `transmission`,`m`.`comments` AS `measurementComments`,`exp`.`comments` AS `experimentComments`,`exp`.`experimentId` AS `experimentId`,`exp`.`experimentType` AS `experimentType`,`s`.`concentration` AS `conc`,`bu`.`acronym` AS `bufferAcronym`,`ma`.`acronym` AS `macromoleculeAcronym`,`bu`.`bufferId` AS `bufferId`,`ma`.`macromoleculeId` AS `macromoleculeId`,(select `su`.`substractedFilePath` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `subtractedFilePath`,(select `su`.`rgGuinier` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `rgGuinier`,(select `su`.`firstPointUsed` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `firstPointUsed`,(select `su`.`lastPointUsed` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `lastPointUsed`,(select distinct `su`.`I0` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `I0`,(select distinct `su`.`isagregated` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `isagregated`,(select distinct `su`.`subtractionId` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `subtractionId`,(select distinct `su`.`rgGnom` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `rgGnom`,(select distinct `su`.`total` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `total`,(select distinct `su`.`dmax` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `dmax`,(select distinct `su`.`volume` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `volume`,(select `su`.`I0Stdev` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `i0stdev`,(select `su`.`quality` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `quality`,(select `su`.`creationTime` from `Subtraction` `su` where (`su`.`subtractionId` = (select max(`su2`.`subtractionId`) from `Subtraction` `su2` where (`su2`.`dataCollectionId` = `dc`.`dataCollectionId`)))) AS `substractionCreationTime`,(select `mtd2`.`measurementId` from `MeasurementToDataCollection` `mtd2` where ((`mtd`.`dataCollectionId` = `mtd2`.`dataCollectionId`) and (`mtd2`.`dataCollectionOrder` = 1))) AS `bufferBeforeMeasurementId`,(select `mtd2`.`measurementId` from `MeasurementToDataCollection` `mtd2` where ((`mtd`.`dataCollectionId` = `mtd2`.`dataCollectionId`) and (`mtd2`.`dataCollectionOrder` = 3))) AS `bufferAfterMeasurementId`,(select `m2`.`framesMerge` from `Merge` `m2` where ((`m2`.`measurementId` = `bufferBeforeMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `bufferBeforeMeasurementId`)))) AS `bufferBeforeFramesMerged`,(select `m2`.`mergeId` from `Merge` `m2` where ((`m2`.`measurementId` = `bufferBeforeMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `bufferBeforeMeasurementId`)))) AS `bufferBeforeMergeId`,(select `m2`.`averageFilePath` from `Merge` `m2` where ((`m2`.`measurementId` = `bufferBeforeMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `bufferBeforeMeasurementId`)))) AS `bufferBeforeAverageFilePath`,(select `mtd2`.`measurementId` from `MeasurementToDataCollection` `mtd2` where ((`mtd`.`dataCollectionId` = `mtd2`.`dataCollectionId`) and (`mtd2`.`dataCollectionOrder` = 2))) AS `sampleMeasurementId`,(select `m2`.`mergeId` from `Merge` `m2` where ((`m2`.`measurementId` = `sampleMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `sampleMeasurementId`)))) AS `sampleMergeId`,(select `m2`.`averageFilePath` from `Merge` `m2` where ((`m2`.`measurementId` = `sampleMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `sampleMeasurementId`)))) AS `averageFilePath`,(select `m2`.`framesMerge` from `Merge` `m2` where ((`m2`.`measurementId` = `sampleMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `sampleMeasurementId`)))) AS `framesMerge`,(select `m2`.`framesCount` from `Merge` `m2` where ((`m2`.`measurementId` = `bufferBeforeMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `bufferBeforeMeasurementId`)))) AS `framesCount`,(select `m2`.`framesMerge` from `Merge` `m2` where ((`m2`.`measurementId` = `bufferAfterMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `bufferAfterMeasurementId`)))) AS `bufferAfterFramesMerged`,(select `m2`.`mergeId` from `Merge` `m2` where ((`m2`.`measurementId` = `bufferAfterMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `bufferAfterMeasurementId`)))) AS `bufferAfterMergeId`,(select `m2`.`averageFilePath` from `Merge` `m2` where ((`m2`.`measurementId` = `bufferAfterMeasurementId`) and `m2`.`mergeId` in (select min(`m3`.`mergeId`) from `Merge` `m3` where (`m3`.`measurementId` = `bufferAfterMeasurementId`)))) AS `bufferAfterAverageFilePath`,(select `modelList`.`modelListId` from (((`ModelList` `modelList` join `AbInitioModel` `ab`) join `SubtractionToAbInitioModel` `subToAb`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`modelList`.`modelListId` = `ab`.`modelListId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`subtractionId` <> 0) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `modelListId1`,(select `modelList`.`nsdFilePath` from (((`ModelList` `modelList` join `AbInitioModel` `ab`) join `SubtractionToAbInitioModel` `subToAb`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`modelList`.`modelListId` = `ab`.`modelListId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`subtractionId` <> 0) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `nsdFilePath`,(select `modelList`.`modelListId` from (((`ModelList` `modelList` join `AbInitioModel` `ab`) join `SubtractionToAbInitioModel` `subToAb`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`modelList`.`modelListId` = `ab`.`modelListId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`subtractionId` <> 0) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `modelListId2`,(select `modelList`.`chi2RgFilePath` from (((`ModelList` `modelList` join `AbInitioModel` `ab`) join `SubtractionToAbInitioModel` `subToAb`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`modelList`.`modelListId` = `ab`.`modelListId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `chi2RgFilePath`,(select `model`.`pdbFile` from (((`AbInitioModel` `ab` join `SubtractionToAbInitioModel` `subToAb`) join `Model` `model`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`model`.`modelId` = `ab`.`averagedModelId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `averagedModel`,(select `model`.`modelId` from (((`AbInitioModel` `ab` join `SubtractionToAbInitioModel` `subToAb`) join `Model` `model`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`model`.`modelId` = `ab`.`averagedModelId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `averagedModelId`,(select `model`.`pdbFile` from (((`AbInitioModel` `ab` join `SubtractionToAbInitioModel` `subToAb`) join `Model` `model`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`model`.`modelId` = `ab`.`rapidShapeDeterminationModelId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `rapidShapeDeterminationModel`,(select `model`.`modelId` from (((`AbInitioModel` `ab` join `SubtractionToAbInitioModel` `subToAb`) join `Model` `model`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`model`.`modelId` = `ab`.`rapidShapeDeterminationModelId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `rapidShapeDeterminationModelId`,(select `model`.`pdbFile` from (((`AbInitioModel` `ab` join `SubtractionToAbInitioModel` `subToAb`) join `Model` `model`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`model`.`modelId` = `ab`.`shapeDeterminationModelId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `shapeDeterminationModel`,(select `model`.`modelId` from (((`AbInitioModel` `ab` join `SubtractionToAbInitioModel` `subToAb`) join `Model` `model`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`model`.`modelId` = `ab`.`shapeDeterminationModelId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `shapeDeterminationModelId`,(select `ab`.`abInitioModelId` from ((`AbInitioModel` `ab` join `SubtractionToAbInitioModel` `subToAb`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `abInitioModelId`,(select `ab`.`comments` from ((`AbInitioModel` `ab` join `SubtractionToAbInitioModel` `subToAb`) join `Subtraction` `su`) where ((`su`.`dataCollectionId` = `dc`.`dataCollectionId`) and (`ab`.`abInitioModelId` = `subToAb`.`abInitioId`) and (`subToAb`.`abInitioId` = (select max(`subToAb2`.`abInitioId`) from `SubtractionToAbInitioModel` `subToAb2` where (`subToAb2`.`subtractionId` = `su`.`subtractionId`))))) AS `comments` from (((((((`Experiment` `exp` join `Buffer` `bu`) join `SaxsDataCollection` `dc`) join `Macromolecule` `ma`) join `MeasurementToDataCollection` `mtd`) join `Specimen` `s`) join `Measurement` `m`) join `Proposal` `p`) where ((`bu`.`bufferId` = `s`.`bufferId`) and (`exp`.`proposalId` = `p`.`proposalId`) and (`m`.`specimenId` = `s`.`specimenId`) and (`dc`.`dataCollectionId` = `mtd`.`dataCollectionId`) and (`mtd`.`measurementId` = `m`.`measurementId`) and (`s`.`macromoleculeId` = `ma`.`macromoleculeId`) and (`s`.`experimentId` = `exp`.`experimentId`) and (`exp`.`experimentType` <> 'TEMPLATE') and `p`.`proposalId` in (select distinct `prop`.`proposalId` from (`Proposal` `prop` join `Experiment` `exper`) where ((`exper`.`proposalId` = `prop`.`proposalId`) and (`prop`.`proposalId` <> 3374)))));

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_Log4Stat` AS select `s`.`id` AS `id`,`s`.`priority` AS `priority`,`s`.`timestamp` AS `timestamp`,`s`.`msg` AS `msg`,`s`.`detail` AS `detail`,`s`.`value` AS `value` from `Log4Stat` `s` where (((`s`.`detail` like 'fx%') or (`s`.`detail` like 'ifx%') or (`s`.`detail` like 'mx%') or (`s`.`detail` like 'ix%') or (`s`.`detail` like 'bm14u%') or (`s`.`detail` like 'bm161%') or (`s`.`detail` like 'bm162%')) and (`s`.`detail` <> 'fx999') and (`s`.`detail` <> 'ifx999') and (`s`.`detail` <> 'mx415'));

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection` AS select `DataCollection`.`dataCollectionId` AS `dataCollectionId`,`DataCollection`.`dataCollectionGroupId` AS `dataCollectionGroupId`,`DataCollection`.`strategySubWedgeOrigId` AS `strategySubWedgeOrigId`,`DataCollection`.`detectorId` AS `detectorId`,`DataCollection`.`blSubSampleId` AS `blSubSampleId`,`DataCollection`.`dataCollectionNumber` AS `dataCollectionNumber`,`DataCollection`.`startTime` AS `startTime`,`DataCollection`.`endTime` AS `endTime`,`DataCollection`.`runStatus` AS `runStatus`,`DataCollection`.`axisStart` AS `axisStart`,`DataCollection`.`axisEnd` AS `axisEnd`,`DataCollection`.`axisRange` AS `axisRange`,`DataCollection`.`overlap` AS `overlap`,`DataCollection`.`numberOfImages` AS `numberOfImages`,`DataCollection`.`startImageNumber` AS `startImageNumber`,`DataCollection`.`numberOfPasses` AS `numberOfPasses`,`DataCollection`.`exposureTime` AS `exposureTime`,`DataCollection`.`imageDirectory` AS `imageDirectory`,`DataCollection`.`imagePrefix` AS `imagePrefix`,`DataCollection`.`imageSuffix` AS `imageSuffix`,`DataCollection`.`fileTemplate` AS `fileTemplate`,`DataCollection`.`wavelength` AS `wavelength`,`DataCollection`.`resolution` AS `resolution`,`DataCollection`.`detectorDistance` AS `detectorDistance`,`DataCollection`.`xBeam` AS `xBeam`,`DataCollection`.`yBeam` AS `yBeam`,`DataCollection`.`xBeamPix` AS `xBeamPix`,`DataCollection`.`yBeamPix` AS `yBeamPix`,`DataCollection`.`comments` AS `comments`,`DataCollection`.`printableForReport` AS `printableForReport`,`DataCollection`.`slitGapVertical` AS `slitGapVertical`,`DataCollection`.`slitGapHorizontal` AS `slitGapHorizontal`,`DataCollection`.`transmission` AS `transmission`,`DataCollection`.`synchrotronMode` AS `synchrotronMode`,`DataCollection`.`xtalSnapshotFullPath1` AS `xtalSnapshotFullPath1`,`DataCollection`.`xtalSnapshotFullPath2` AS `xtalSnapshotFullPath2`,`DataCollection`.`xtalSnapshotFullPath3` AS `xtalSnapshotFullPath3`,`DataCollection`.`xtalSnapshotFullPath4` AS `xtalSnapshotFullPath4`,`DataCollection`.`rotationAxis` AS `rotationAxis`,`DataCollection`.`phiStart` AS `phiStart`,`DataCollection`.`kappaStart` AS `kappaStart`,`DataCollection`.`omegaStart` AS `omegaStart`,`DataCollection`.`resolutionAtCorner` AS `resolutionAtCorner`,`DataCollection`.`detector2Theta` AS `detector2Theta`,`DataCollection`.`undulatorGap1` AS `undulatorGap1`,`DataCollection`.`undulatorGap2` AS `undulatorGap2`,`DataCollection`.`undulatorGap3` AS `undulatorGap3`,`DataCollection`.`beamSizeAtSampleX` AS `beamSizeAtSampleX`,`DataCollection`.`beamSizeAtSampleY` AS `beamSizeAtSampleY`,`DataCollection`.`centeringMethod` AS `centeringMethod`,`DataCollection`.`averageTemperature` AS `averageTemperature`,`DataCollection`.`actualCenteringPosition` AS `actualCenteringPosition`,`DataCollection`.`beamShape` AS `beamShape`,`DataCollection`.`flux` AS `flux`,`DataCollection`.`flux_end` AS `flux_end`,`DataCollection`.`totalAbsorbedDose` AS `totalAbsorbedDose`,`DataCollection`.`bestWilsonPlotPath` AS `bestWilsonPlotPath`,`DataCollection`.`imageQualityIndicatorsPlotPath` AS `imageQualityIndicatorsPlotPath`,`DataCollection`.`imageQualityIndicatorsCSVPath` AS `imageQualityIndicatorsCSVPath`,`BLSession`.`sessionId` AS `sessionId`,`BLSession`.`proposalId` AS `proposalId`,`DataCollectionGroup`.`workflowId` AS `workflowId`,`v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_dataCollectionId` AS `AutoProcIntegration_dataCollectionId`,`v_datacollection_summary_autoprocintegration`.`autoProcScalingId` AS `autoProcScalingId`,`v_datacollection_summary_autoprocintegration`.`cell_a` AS `cell_a`,`v_datacollection_summary_autoprocintegration`.`cell_b` AS `cell_b`,`v_datacollection_summary_autoprocintegration`.`cell_c` AS `cell_c`,`v_datacollection_summary_autoprocintegration`.`cell_alpha` AS `cell_alpha`,`v_datacollection_summary_autoprocintegration`.`cell_beta` AS `cell_beta`,`v_datacollection_summary_autoprocintegration`.`cell_gamma` AS `cell_gamma`,`v_datacollection_summary_autoprocintegration`.`anomalous` AS `anomalous`,`v_datacollection_summary_autoprocintegration`.`scalingStatisticsType` AS `scalingStatisticsType`,`v_datacollection_summary_autoprocintegration`.`resolutionLimitHigh` AS `resolutionLimitHigh`,`v_datacollection_summary_autoprocintegration`.`resolutionLimitLow` AS `resolutionLimitLow`,`v_datacollection_summary_autoprocintegration`.`completeness` AS `completeness`,`v_datacollection_summary_autoprocintegration`.`AutoProc_spaceGroup` AS `AutoProc_spaceGroup`,`v_datacollection_summary_autoprocintegration`.`autoProcId` AS `autoProcId`,`v_datacollection_summary_autoprocintegration`.`rMerge` AS `rMerge`,`v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`,`v_datacollection_summary_autoprocintegration`.`v_datacollection_summary_autoprocintegration_processingPrograms` AS `AutoProcProgram_processingPrograms`,`v_datacollection_summary_autoprocintegration`.`v_datacollection_summary_autoprocintegration_processingStatus` AS `AutoProcProgram_processingStatus`,`v_datacollection_summary_autoprocintegration`.`AutoProcProgram_autoProcProgramId` AS `AutoProcProgram_autoProcProgramId` from ((((`DataCollection` left join `DataCollectionGroup` on((`DataCollectionGroup`.`dataCollectionGroupId` = `DataCollection`.`dataCollectionGroupId`))) left join `Workflow` on((`DataCollectionGroup`.`workflowId` = `Workflow`.`workflowId`))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`))) left join `v_datacollection_summary_autoprocintegration` on((`v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_dataCollectionId` = `DataCollection`.`dataCollectionId`)));

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection_autoprocintegration` AS select `AutoProcIntegration`.`autoProcIntegrationId` AS `v_datacollection_summary_phasing_autoProcIntegrationId`,`AutoProcIntegration`.`dataCollectionId` AS `v_datacollection_summary_phasing_dataCollectionId`,`AutoProcIntegration`.`cell_a` AS `v_datacollection_summary_phasing_cell_a`,`AutoProcIntegration`.`cell_b` AS `v_datacollection_summary_phasing_cell_b`,`AutoProcIntegration`.`cell_c` AS `v_datacollection_summary_phasing_cell_c`,`AutoProcIntegration`.`cell_alpha` AS `v_datacollection_summary_phasing_cell_alpha`,`AutoProcIntegration`.`cell_beta` AS `v_datacollection_summary_phasing_cell_beta`,`AutoProcIntegration`.`cell_gamma` AS `v_datacollection_summary_phasing_cell_gamma`,`AutoProcIntegration`.`anomalous` AS `v_datacollection_summary_phasing_anomalous`,`AutoProc`.`spaceGroup` AS `v_datacollection_summary_phasing_autoproc_space_group`,`AutoProc`.`autoProcId` AS `v_datacollection_summary_phasing_autoproc_autoprocId`,`AutoProcScaling`.`autoProcScalingId` AS `v_datacollection_summary_phasing_autoProcScalingId`,`AutoProcProgram`.`processingPrograms` AS `v_datacollection_processingPrograms`,`AutoProcProgram`.`autoProcProgramId` AS `v_datacollection_summary_phasing_autoProcProgramId`,`AutoProcProgram`.`processingStatus` AS `v_datacollection_processingStatus`,`AutoProcProgram`.`processingStartTime` AS `v_datacollection_processingStartTime`,`AutoProcProgram`.`processingEndTime` AS `v_datacollection_processingEndTime`,`BLSession`.`sessionId` AS `v_datacollection_summary_session_sessionId`,`BLSession`.`proposalId` AS `v_datacollection_summary_session_proposalId`,`AutoProcIntegration`.`dataCollectionId` AS `AutoProcIntegration_dataCollectionId`,`AutoProcIntegration`.`autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`,`PhasingStep`.`phasingStepType` AS `PhasingStep_phasing_phasingStepType`,`SpaceGroup`.`spaceGroupShortName` AS `SpaceGroup_spaceGroupShortName`,`Protein`.`proteinId` AS `Protein_proteinId`,`Protein`.`acronym` AS `Protein_acronym`,`BLSample`.`name` AS `BLSample_name`,`DataCollection`.`dataCollectionNumber` AS `DataCollection_dataCollectionNumber`,`DataCollection`.`imagePrefix` AS `DataCollection_imagePrefix` from (((((((((((((`AutoProcIntegration` left join `DataCollection` on((`DataCollection`.`dataCollectionId` = `AutoProcIntegration`.`dataCollectionId`))) left join `DataCollectionGroup` on((`DataCollection`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`))) left join `AutoProcScaling_has_Int` on((`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`))) left join `AutoProcScaling` on((`AutoProcScaling_has_Int`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `AutoProcProgram` on((`AutoProcProgram`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`))) left join `Phasing_has_Scaling` on((`Phasing_has_Scaling`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `PhasingStep` on((`PhasingStep`.`autoProcScalingId` = `Phasing_has_Scaling`.`autoProcScalingId`))) left join `SpaceGroup` on((`SpaceGroup`.`spaceGroupId` = `PhasingStep`.`spaceGroupId`))) left join `AutoProc` on((`AutoProc`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`))) left join `BLSample` on((`BLSample`.`blSampleId` = `DataCollectionGroup`.`blSampleId`))) left join `Crystal` on((`Crystal`.`crystalId` = `BLSample`.`crystalId`))) left join `Protein` on((`Protein`.`proteinId` = `Crystal`.`proteinId`)));

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection_phasing` AS select `PhasingStep`.`phasingStepId` AS `phasingStepId`,`PhasingStep`.`previousPhasingStepId` AS `previousPhasingStepId`,`PhasingStep`.`phasingAnalysisId` AS `phasingAnalysisId`,`AutoProcIntegration`.`autoProcIntegrationId` AS `autoProcIntegrationId`,`AutoProcIntegration`.`dataCollectionId` AS `dataCollectionId`,`AutoProcIntegration`.`anomalous` AS `anomalous`,`AutoProc`.`spaceGroup` AS `spaceGroup`,`AutoProc`.`autoProcId` AS `autoProcId`,`PhasingStep`.`phasingStepType` AS `phasingStepType`,`PhasingStep`.`method` AS `method`,`PhasingStep`.`solventContent` AS `solventContent`,`PhasingStep`.`enantiomorph` AS `enantiomorph`,`PhasingStep`.`lowRes` AS `lowRes`,`PhasingStep`.`highRes` AS `highRes`,`AutoProcScaling`.`autoProcScalingId` AS `autoProcScalingId`,`SpaceGroup`.`spaceGroupShortName` AS `spaceGroupShortName`,`AutoProcProgram`.`processingPrograms` AS `processingPrograms`,`AutoProcProgram`.`processingStatus` AS `processingStatus`,`PhasingProgramRun`.`phasingPrograms` AS `phasingPrograms`,`PhasingProgramRun`.`phasingStatus` AS `phasingStatus`,`PhasingProgramRun`.`phasingStartTime` AS `phasingStartTime`,`PhasingProgramRun`.`phasingEndTime` AS `phasingEndTime`,`DataCollectionGroup`.`sessionId` AS `sessionId`,`BLSession`.`proposalId` AS `proposalId`,`BLSample`.`blSampleId` AS `blSampleId`,`BLSample`.`name` AS `name`,`BLSample`.`code` AS `code`,`Protein`.`acronym` AS `acronym`,`Protein`.`proteinId` AS `proteinId` from (((((((((((((`AutoProcIntegration` left join `AutoProcScaling_has_Int` on((`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`))) left join `AutoProcScaling` on((`AutoProcScaling_has_Int`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `AutoProcProgram` on((`AutoProcProgram`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`))) left join `AutoProc` on((`AutoProc`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`))) left join `PhasingStep` on((`PhasingStep`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `PhasingProgramRun` on((`PhasingProgramRun`.`phasingProgramRunId` = `PhasingStep`.`programRunId`))) left join `DataCollection` on((`DataCollection`.`dataCollectionId` = `AutoProcIntegration`.`dataCollectionId`))) left join `DataCollectionGroup` on((`DataCollectionGroup`.`dataCollectionGroupId` = `DataCollection`.`dataCollectionGroupId`))) left join `BLSample` on((`BLSample`.`blSampleId` = `DataCollectionGroup`.`blSampleId`))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`))) left join `Crystal` on((`Crystal`.`crystalId` = `BLSample`.`crystalId`))) left join `Protein` on((`Crystal`.`proteinId` = `Protein`.`proteinId`))) left join `SpaceGroup` on((`SpaceGroup`.`spaceGroupId` = `PhasingStep`.`spaceGroupId`)));

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection_phasing_program_run` AS select `PhasingStep`.`phasingStepId` AS `phasingStepId`,`PhasingStep`.`previousPhasingStepId` AS `previousPhasingStepId`,`PhasingStep`.`phasingAnalysisId` AS `phasingAnalysisId`,`AutoProcIntegration`.`autoProcIntegrationId` AS `autoProcIntegrationId`,`AutoProcIntegration`.`dataCollectionId` AS `dataCollectionId`,`AutoProc`.`autoProcId` AS `autoProcId`,`PhasingStep`.`phasingStepType` AS `phasingStepType`,`PhasingStep`.`method` AS `method`,`AutoProcScaling`.`autoProcScalingId` AS `autoProcScalingId`,`SpaceGroup`.`spaceGroupShortName` AS `spaceGroupShortName`,`PhasingProgramRun`.`phasingPrograms` AS `phasingPrograms`,`PhasingProgramRun`.`phasingStatus` AS `phasingStatus`,`DataCollectionGroup`.`sessionId` AS `sessionId`,`BLSession`.`proposalId` AS `proposalId`,`BLSample`.`blSampleId` AS `blSampleId`,`BLSample`.`name` AS `name`,`BLSample`.`code` AS `code`,`Protein`.`acronym` AS `acronym`,`Protein`.`proteinId` AS `proteinId`,`PhasingProgramAttachment`.`phasingProgramAttachmentId` AS `phasingProgramAttachmentId`,`PhasingProgramAttachment`.`fileType` AS `fileType`,`PhasingProgramAttachment`.`fileName` AS `fileName`,`PhasingProgramAttachment`.`filePath` AS `filePath` from ((((((((((((((`AutoProcIntegration` left join `AutoProcScaling_has_Int` on((`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`))) left join `AutoProcScaling` on((`AutoProcScaling_has_Int`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `AutoProcProgram` on((`AutoProcProgram`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`))) left join `AutoProc` on((`AutoProc`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`))) left join `PhasingStep` on((`PhasingStep`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `PhasingProgramRun` on((`PhasingProgramRun`.`phasingProgramRunId` = `PhasingStep`.`programRunId`))) left join `DataCollection` on((`DataCollection`.`dataCollectionId` = `AutoProcIntegration`.`dataCollectionId`))) left join `DataCollectionGroup` on((`DataCollectionGroup`.`dataCollectionGroupId` = `DataCollection`.`dataCollectionGroupId`))) left join `BLSample` on((`BLSample`.`blSampleId` = `DataCollectionGroup`.`blSampleId`))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`))) left join `Crystal` on((`Crystal`.`crystalId` = `BLSample`.`crystalId`))) left join `Protein` on((`Crystal`.`proteinId` = `Protein`.`proteinId`))) left join `PhasingProgramAttachment` on((`PhasingProgramAttachment`.`phasingProgramRunId` = `PhasingProgramRun`.`phasingProgramRunId`))) left join `SpaceGroup` on((`SpaceGroup`.`spaceGroupId` = `PhasingStep`.`spaceGroupId`)));

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection_summary` AS select `DataCollectionGroup`.`dataCollectionGroupId` AS `DataCollectionGroup_dataCollectionGroupId`,`DataCollectionGroup`.`blSampleId` AS `DataCollectionGroup_blSampleId`,`DataCollectionGroup`.`sessionId` AS `DataCollectionGroup_sessionId`,`DataCollectionGroup`.`workflowId` AS `DataCollectionGroup_workflowId`,`DataCollectionGroup`.`experimentType` AS `DataCollectionGroup_experimentType`,`DataCollectionGroup`.`startTime` AS `DataCollectionGroup_startTime`,`DataCollectionGroup`.`endTime` AS `DataCollectionGroup_endTime`,`DataCollectionGroup`.`comments` AS `DataCollectionGroup_comments`,`DataCollectionGroup`.`actualSampleBarcode` AS `DataCollectionGroup_actualSampleBarcode`,`DataCollectionGroup`.`xtalSnapshotFullPath` AS `DataCollectionGroup_xtalSnapshotFullPath`,`DataCollectionGroup`.`crystalClass` AS `DataCollectionGroup_crystalClass`,`BLSample`.`blSampleId` AS `BLSample_blSampleId`,`BLSample`.`crystalId` AS `BLSample_crystalId`,`BLSample`.`name` AS `BLSample_name`,`BLSample`.`code` AS `BLSample_code`,`BLSample`.`location` AS `BLSample_location`,`BLSample`.`blSampleStatus` AS `BLSample_blSampleStatus`,`BLSample`.`comments` AS `BLSample_comments`,`Container`.`containerId` AS `Container_containerId`,`BLSession`.`sessionId` AS `BLSession_sessionId`,`BLSession`.`proposalId` AS `BLSession_proposalId`,`BLSession`.`protectedData` AS `BLSession_protectedData`,`Dewar`.`dewarId` AS `Dewar_dewarId`,`Dewar`.`code` AS `Dewar_code`,`Dewar`.`storageLocation` AS `Dewar_storageLocation`,`Container`.`containerType` AS `Container_containerType`,`Container`.`code` AS `Container_code`,`Container`.`capacity` AS `Container_capacity`,`Container`.`beamlineLocation` AS `Container_beamlineLocation`,`Container`.`sampleChangerLocation` AS `Container_sampleChangerLocation`,`Protein`.`proteinId` AS `Protein_proteinId`,`Protein`.`name` AS `Protein_name`,`Protein`.`acronym` AS `Protein_acronym`,`DataCollection`.`dataCollectionId` AS `DataCollection_dataCollectionId`,`DataCollection`.`dataCollectionGroupId` AS `DataCollection_dataCollectionGroupId`,`DataCollection`.`startTime` AS `DataCollection_startTime`,`DataCollection`.`endTime` AS `DataCollection_endTime`,`DataCollection`.`runStatus` AS `DataCollection_runStatus`,`DataCollection`.`numberOfImages` AS `DataCollection_numberOfImages`,`DataCollection`.`startImageNumber` AS `DataCollection_startImageNumber`,`DataCollection`.`numberOfPasses` AS `DataCollection_numberOfPasses`,`DataCollection`.`exposureTime` AS `DataCollection_exposureTime`,`DataCollection`.`imageDirectory` AS `DataCollection_imageDirectory`,`DataCollection`.`wavelength` AS `DataCollection_wavelength`,`DataCollection`.`resolution` AS `DataCollection_resolution`,`DataCollection`.`detectorDistance` AS `DataCollection_detectorDistance`,`DataCollection`.`xBeam` AS `DataCollection_xBeam`,`DataCollection`.`transmission` AS `transmission`,`DataCollection`.`yBeam` AS `DataCollection_yBeam`,`DataCollection`.`imagePrefix` AS `DataCollection_imagePrefix`,`DataCollection`.`comments` AS `DataCollection_comments`,`DataCollection`.`xtalSnapshotFullPath1` AS `DataCollection_xtalSnapshotFullPath1`,`DataCollection`.`xtalSnapshotFullPath2` AS `DataCollection_xtalSnapshotFullPath2`,`DataCollection`.`xtalSnapshotFullPath3` AS `DataCollection_xtalSnapshotFullPath3`,`DataCollection`.`xtalSnapshotFullPath4` AS `DataCollection_xtalSnapshotFullPath4`,`DataCollection`.`phiStart` AS `DataCollection_phiStart`,`DataCollection`.`kappaStart` AS `DataCollection_kappaStart`,`DataCollection`.`omegaStart` AS `DataCollection_omegaStart`,`DataCollection`.`flux` AS `DataCollection_flux`,`DataCollection`.`flux_end` AS `DataCollection_flux_end`,`DataCollection`.`resolutionAtCorner` AS `DataCollection_resolutionAtCorner`,`DataCollection`.`bestWilsonPlotPath` AS `DataCollection_bestWilsonPlotPath`,`DataCollection`.`dataCollectionNumber` AS `DataCollection_dataCollectionNumber`,`DataCollection`.`axisRange` AS `DataCollection_axisRange`,`DataCollection`.`axisStart` AS `DataCollection_axisStart`,`DataCollection`.`axisEnd` AS `DataCollection_axisEnd`,`DataCollection`.`rotationAxis` AS `DataCollection_rotationAxis`,`DataCollection`.`undulatorGap1` AS `DataCollection_undulatorGap1`,`DataCollection`.`undulatorGap2` AS `DataCollection_undulatorGap2`,`DataCollection`.`undulatorGap3` AS `DataCollection_undulatorGap3`,`DataCollection`.`beamSizeAtSampleX` AS `beamSizeAtSampleX`,`DataCollection`.`beamSizeAtSampleY` AS `beamSizeAtSampleY`,`DataCollection`.`slitGapVertical` AS `DataCollection_slitGapVertical`,`DataCollection`.`slitGapHorizontal` AS `DataCollection_slitGapHorizontal`,`DataCollection`.`beamShape` AS `DataCollection_beamShape`,`DataCollection`.`voltage` AS `DataCollection_voltage`,`DataCollection`.`xBeamPix` AS `DataCollection_xBeamPix`,`Workflow`.`workflowTitle` AS `Workflow_workflowTitle`,`Workflow`.`workflowType` AS `Workflow_workflowType`,`Workflow`.`status` AS `Workflow_status`,`Workflow`.`workflowId` AS `Workflow_workflowId`,`v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_dataCollectionId` AS `AutoProcIntegration_dataCollectionId`,`v_datacollection_summary_autoprocintegration`.`autoProcScalingId` AS `autoProcScalingId`,`v_datacollection_summary_autoprocintegration`.`cell_a` AS `cell_a`,`v_datacollection_summary_autoprocintegration`.`cell_b` AS `cell_b`,`v_datacollection_summary_autoprocintegration`.`cell_c` AS `cell_c`,`v_datacollection_summary_autoprocintegration`.`cell_alpha` AS `cell_alpha`,`v_datacollection_summary_autoprocintegration`.`cell_beta` AS `cell_beta`,`v_datacollection_summary_autoprocintegration`.`cell_gamma` AS `cell_gamma`,`v_datacollection_summary_autoprocintegration`.`anomalous` AS `anomalous`,`v_datacollection_summary_autoprocintegration`.`scalingStatisticsType` AS `scalingStatisticsType`,`v_datacollection_summary_autoprocintegration`.`resolutionLimitHigh` AS `resolutionLimitHigh`,`v_datacollection_summary_autoprocintegration`.`resolutionLimitLow` AS `resolutionLimitLow`,`v_datacollection_summary_autoprocintegration`.`completeness` AS `completeness`,`v_datacollection_summary_autoprocintegration`.`AutoProc_spaceGroup` AS `AutoProc_spaceGroup`,`v_datacollection_summary_autoprocintegration`.`autoProcId` AS `autoProcId`,`v_datacollection_summary_autoprocintegration`.`rMerge` AS `rMerge`,`v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`,`v_datacollection_summary_autoprocintegration`.`v_datacollection_summary_autoprocintegration_processingPrograms` AS `AutoProcProgram_processingPrograms`,`v_datacollection_summary_autoprocintegration`.`v_datacollection_summary_autoprocintegration_processingStatus` AS `AutoProcProgram_processingStatus`,`v_datacollection_summary_autoprocintegration`.`AutoProcProgram_autoProcProgramId` AS `AutoProcProgram_autoProcProgramId`,`v_datacollection_summary_screening`.`Screening_screeningId` AS `Screening_screeningId`,`v_datacollection_summary_screening`.`Screening_dataCollectionId` AS `Screening_dataCollectionId`,`v_datacollection_summary_screening`.`Screening_dataCollectionGroupId` AS `Screening_dataCollectionGroupId`,`v_datacollection_summary_screening`.`ScreeningOutput_strategySuccess` AS `ScreeningOutput_strategySuccess`,`v_datacollection_summary_screening`.`ScreeningOutput_indexingSuccess` AS `ScreeningOutput_indexingSuccess`,`v_datacollection_summary_screening`.`ScreeningOutput_rankingResolution` AS `ScreeningOutput_rankingResolution`,`v_datacollection_summary_screening`.`ScreeningOutput_mosaicity` AS `ScreeningOutput_mosaicity`,`v_datacollection_summary_screening`.`ScreeningOutputLattice_spaceGroup` AS `ScreeningOutputLattice_spaceGroup`,`v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_a` AS `ScreeningOutputLattice_unitCell_a`,`v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_b` AS `ScreeningOutputLattice_unitCell_b`,`v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_c` AS `ScreeningOutputLattice_unitCell_c`,`v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_alpha` AS `ScreeningOutputLattice_unitCell_alpha`,`v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_beta` AS `ScreeningOutputLattice_unitCell_beta`,`v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_gamma` AS `ScreeningOutputLattice_unitCell_gamma`,`v_datacollection_summary_screening`.`ScreeningOutput_totalExposureTime` AS `ScreeningOutput_totalExposureTime`,`v_datacollection_summary_screening`.`ScreeningOutput_totalRotationRange` AS `ScreeningOutput_totalRotationRange`,`v_datacollection_summary_screening`.`ScreeningOutput_totalNumberOfImages` AS `ScreeningOutput_totalNumberOfImages`,`v_datacollection_summary_screening`.`ScreeningStrategySubWedge_exposureTime` AS `ScreeningStrategySubWedge_exposureTime`,`v_datacollection_summary_screening`.`ScreeningStrategySubWedge_transmission` AS `ScreeningStrategySubWedge_transmission`,`v_datacollection_summary_screening`.`ScreeningStrategySubWedge_oscillationRange` AS `ScreeningStrategySubWedge_oscillationRange`,`v_datacollection_summary_screening`.`ScreeningStrategySubWedge_numberOfImages` AS `ScreeningStrategySubWedge_numberOfImages`,`v_datacollection_summary_screening`.`ScreeningStrategySubWedge_multiplicity` AS `ScreeningStrategySubWedge_multiplicity`,`v_datacollection_summary_screening`.`ScreeningStrategySubWedge_completeness` AS `ScreeningStrategySubWedge_completeness`,`v_datacollection_summary_screening`.`ScreeningStrategySubWedge_axisStart` AS `ScreeningStrategySubWedge_axisStart`,`Shipping`.`shippingId` AS `Shipping_shippingId`,`Shipping`.`shippingName` AS `Shipping_shippingName`,`Shipping`.`shippingStatus` AS `Shipping_shippingStatus`,`DiffractionPlan`.`diffractionPlanId` AS `diffractionPlanId`,`DiffractionPlan`.`experimentKind` AS `experimentKind`,`DiffractionPlan`.`observedResolution` AS `observedResolution`,`DiffractionPlan`.`minimalResolution` AS `minimalResolution`,`DiffractionPlan`.`exposureTime` AS `exposureTime`,`DiffractionPlan`.`oscillationRange` AS `oscillationRange`,`DiffractionPlan`.`maximalResolution` AS `maximalResolution`,`DiffractionPlan`.`screeningResolution` AS `screeningResolution`,`DiffractionPlan`.`radiationSensitivity` AS `radiationSensitivity`,`DiffractionPlan`.`anomalousScatterer` AS `anomalousScatterer`,`DiffractionPlan`.`preferredBeamSizeX` AS `preferredBeamSizeX`,`DiffractionPlan`.`preferredBeamSizeY` AS `preferredBeamSizeY`,`DiffractionPlan`.`preferredBeamDiameter` AS `preferredBeamDiameter`,`DiffractionPlan`.`comments` AS `DiffractipnPlan_comments`,`DiffractionPlan`.`aimedCompleteness` AS `aimedCompleteness`,`DiffractionPlan`.`aimedIOverSigmaAtHighestRes` AS `aimedIOverSigmaAtHighestRes`,`DiffractionPlan`.`aimedMultiplicity` AS `aimedMultiplicity`,`DiffractionPlan`.`aimedResolution` AS `aimedResolution`,`DiffractionPlan`.`anomalousData` AS `anomalousData`,`DiffractionPlan`.`complexity` AS `complexity`,`DiffractionPlan`.`estimateRadiationDamage` AS `estimateRadiationDamage`,`DiffractionPlan`.`forcedSpaceGroup` AS `forcedSpaceGroup`,`DiffractionPlan`.`requiredCompleteness` AS `requiredCompleteness`,`DiffractionPlan`.`requiredMultiplicity` AS `requiredMultiplicity`,`DiffractionPlan`.`requiredResolution` AS `requiredResolution`,`DiffractionPlan`.`strategyOption` AS `strategyOption`,`DiffractionPlan`.`kappaStrategyOption` AS `kappaStrategyOption`,`DiffractionPlan`.`numberOfPositions` AS `numberOfPositions`,`DiffractionPlan`.`minDimAccrossSpindleAxis` AS `minDimAccrossSpindleAxis`,`DiffractionPlan`.`maxDimAccrossSpindleAxis` AS `maxDimAccrossSpindleAxis`,`DiffractionPlan`.`radiationSensitivityBeta` AS `radiationSensitivityBeta`,`DiffractionPlan`.`radiationSensitivityGamma` AS `radiationSensitivityGamma`,`DiffractionPlan`.`minOscWidth` AS `minOscWidth`,`Detector`.`detectorType` AS `Detector_detectorType`,`Detector`.`detectorManufacturer` AS `Detector_detectorManufacturer`,`Detector`.`detectorModel` AS `Detector_detectorModel`,`Detector`.`detectorPixelSizeHorizontal` AS `Detector_detectorPixelSizeHorizontal`,`Detector`.`detectorPixelSizeVertical` AS `Detector_detectorPixelSizeVertical`,`Detector`.`detectorSerialNumber` AS `Detector_detectorSerialNumber`,`Detector`.`detectorDistanceMin` AS `Detector_detectorDistanceMin`,`Detector`.`detectorDistanceMax` AS `Detector_detectorDistanceMax`,`Detector`.`trustedPixelValueRangeLower` AS `Detector_trustedPixelValueRangeLower`,`Detector`.`trustedPixelValueRangeUpper` AS `Detector_trustedPixelValueRangeUpper`,`Detector`.`sensorThickness` AS `Detector_sensorThickness`,`Detector`.`overload` AS `Detector_overload`,`Detector`.`XGeoCorr` AS `Detector_XGeoCorr`,`Detector`.`YGeoCorr` AS `Detector_YGeoCorr`,`Detector`.`detectorMode` AS `Detector_detectorMode`,`BeamLineSetup`.`undulatorType1` AS `BeamLineSetup_undulatorType1`,`BeamLineSetup`.`undulatorType2` AS `BeamLineSetup_undulatorType2`,`BeamLineSetup`.`undulatorType3` AS `BeamLineSetup_undulatorType3`,`BeamLineSetup`.`synchrotronName` AS `BeamLineSetup_synchrotronName`,`BeamLineSetup`.`synchrotronMode` AS `BeamLineSetup_synchrotronMode`,`BeamLineSetup`.`polarisation` AS `BeamLineSetup_polarisation`,`BeamLineSetup`.`focusingOptic` AS `BeamLineSetup_focusingOptic`,`BeamLineSetup`.`beamDivergenceHorizontal` AS `BeamLineSetup_beamDivergenceHorizontal`,`BeamLineSetup`.`beamDivergenceVertical` AS `BeamLineSetup_beamDivergenceVertical`,`BeamLineSetup`.`monochromatorType` AS `BeamLineSetup_monochromatorType` from ((((((((((((((`DataCollectionGroup` left join `DataCollection` on(((`DataCollection`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`) and (`DataCollection`.`dataCollectionId` = (select max(`dc2`.`dataCollectionId`) from `DataCollection` `dc2` where (`dc2`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`)))))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`))) left join `BeamLineSetup` on((`BeamLineSetup`.`beamLineSetupId` = `BLSession`.`beamLineSetupId`))) left join `Detector` on((`Detector`.`detectorId` = `DataCollection`.`detectorId`))) left join `BLSample` on((`BLSample`.`blSampleId` = `DataCollectionGroup`.`blSampleId`))) left join `DiffractionPlan` on((`DiffractionPlan`.`diffractionPlanId` = `BLSample`.`diffractionPlanId`))) left join `Container` on((`Container`.`containerId` = `BLSample`.`containerId`))) left join `Dewar` on((`Container`.`dewarId` = `Dewar`.`dewarId`))) left join `Shipping` on((`Shipping`.`shippingId` = `Dewar`.`shippingId`))) left join `Crystal` on((`Crystal`.`crystalId` = `BLSample`.`crystalId`))) left join `Protein` on((`Protein`.`proteinId` = `Crystal`.`proteinId`))) left join `Workflow` on((`DataCollectionGroup`.`workflowId` = `Workflow`.`workflowId`))) left join `v_datacollection_summary_autoprocintegration` on((`v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_dataCollectionId` = `DataCollection`.`dataCollectionId`))) left join `v_datacollection_summary_screening` on((`v_datacollection_summary_screening`.`Screening_dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`)));

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection_summary_autoprocintegration` AS select `AutoProcIntegration`.`dataCollectionId` AS `AutoProcIntegration_dataCollectionId`,`AutoProcIntegration`.`cell_a` AS `cell_a`,`AutoProcIntegration`.`cell_b` AS `cell_b`,`AutoProcIntegration`.`cell_c` AS `cell_c`,`AutoProcIntegration`.`cell_alpha` AS `cell_alpha`,`AutoProcIntegration`.`cell_beta` AS `cell_beta`,`AutoProcIntegration`.`cell_gamma` AS `cell_gamma`,`AutoProcIntegration`.`anomalous` AS `anomalous`,`AutoProcIntegration`.`autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`,`AutoProcProgram`.`processingPrograms` AS `v_datacollection_summary_autoprocintegration_processingPrograms`,`AutoProcProgram`.`autoProcProgramId` AS `AutoProcProgram_autoProcProgramId`,`AutoProcProgram`.`processingStatus` AS `v_datacollection_summary_autoprocintegration_processingStatus`,`AutoProcIntegration`.`dataCollectionId` AS `AutoProcIntegration_phasing_dataCollectionId`,`PhasingStep`.`phasingStepType` AS `PhasingStep_phasing_phasingStepType`,`SpaceGroup`.`spaceGroupShortName` AS `SpaceGroup_spaceGroupShortName`,`AutoProc`.`autoProcId` AS `autoProcId`,`AutoProc`.`spaceGroup` AS `AutoProc_spaceGroup`,`AutoProcScalingStatistics`.`scalingStatisticsType` AS `scalingStatisticsType`,`AutoProcScalingStatistics`.`resolutionLimitHigh` AS `resolutionLimitHigh`,`AutoProcScalingStatistics`.`resolutionLimitLow` AS `resolutionLimitLow`,`AutoProcScalingStatistics`.`rMerge` AS `rMerge`,`AutoProcScalingStatistics`.`completeness` AS `completeness`,`AutoProcScaling`.`autoProcScalingId` AS `autoProcScalingId` from ((((((((`AutoProcIntegration` left join `AutoProcProgram` on((`AutoProcIntegration`.`autoProcProgramId` = `AutoProcProgram`.`autoProcProgramId`))) left join `AutoProcScaling_has_Int` on((`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`))) left join `AutoProcScaling` on((`AutoProcScaling`.`autoProcScalingId` = `AutoProcScaling_has_Int`.`autoProcScalingId`))) left join `AutoProcScalingStatistics` on((`AutoProcScaling`.`autoProcScalingId` = `AutoProcScalingStatistics`.`autoProcScalingId`))) left join `AutoProc` on((`AutoProc`.`autoProcId` = `AutoProcScaling`.`autoProcId`))) left join `Phasing_has_Scaling` on((`Phasing_has_Scaling`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `PhasingStep` on((`PhasingStep`.`autoProcScalingId` = `Phasing_has_Scaling`.`autoProcScalingId`))) left join `SpaceGroup` on((`SpaceGroup`.`spaceGroupId` = `PhasingStep`.`spaceGroupId`)));

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection_summary_datacollectiongroup` AS select `DataCollectionGroup`.`dataCollectionGroupId` AS `DataCollectionGroup_dataCollectionGroupId`,`DataCollectionGroup`.`blSampleId` AS `DataCollectionGroup_blSampleId`,`DataCollectionGroup`.`sessionId` AS `DataCollectionGroup_sessionId`,`DataCollectionGroup`.`workflowId` AS `DataCollectionGroup_workflowId`,`DataCollectionGroup`.`experimentType` AS `DataCollectionGroup_experimentType`,`DataCollectionGroup`.`startTime` AS `DataCollectionGroup_startTime`,`DataCollectionGroup`.`endTime` AS `DataCollectionGroup_endTime`,`DataCollectionGroup`.`comments` AS `DataCollectionGroup_comments`,`DataCollectionGroup`.`actualSampleBarcode` AS `DataCollectionGroup_actualSampleBarcode`,`DataCollectionGroup`.`xtalSnapshotFullPath` AS `DataCollectionGroup_xtalSnapshotFullPath`,`BLSample`.`blSampleId` AS `BLSample_blSampleId`,`BLSample`.`crystalId` AS `BLSample_crystalId`,`BLSample`.`name` AS `BLSample_name`,`BLSample`.`code` AS `BLSample_code`,`BLSession`.`sessionId` AS `BLSession_sessionId`,`BLSession`.`proposalId` AS `BLSession_proposalId`,`BLSession`.`protectedData` AS `BLSession_protectedData`,`Protein`.`proteinId` AS `Protein_proteinId`,`Protein`.`name` AS `Protein_name`,`Protein`.`acronym` AS `Protein_acronym`,`DataCollection`.`dataCollectionId` AS `DataCollection_dataCollectionId`,`DataCollection`.`dataCollectionGroupId` AS `DataCollection_dataCollectionGroupId`,`DataCollection`.`startTime` AS `DataCollection_startTime`,`DataCollection`.`endTime` AS `DataCollection_endTime`,`DataCollection`.`runStatus` AS `DataCollection_runStatus`,`DataCollection`.`numberOfImages` AS `DataCollection_numberOfImages`,`DataCollection`.`startImageNumber` AS `DataCollection_startImageNumber`,`DataCollection`.`numberOfPasses` AS `DataCollection_numberOfPasses`,`DataCollection`.`exposureTime` AS `DataCollection_exposureTime`,`DataCollection`.`imageDirectory` AS `DataCollection_imageDirectory`,`DataCollection`.`wavelength` AS `DataCollection_wavelength`,`DataCollection`.`resolution` AS `DataCollection_resolution`,`DataCollection`.`detectorDistance` AS `DataCollection_detectorDistance`,`DataCollection`.`xBeam` AS `DataCollection_xBeam`,`DataCollection`.`yBeam` AS `DataCollection_yBeam`,`DataCollection`.`comments` AS `DataCollection_comments`,`DataCollection`.`xtalSnapshotFullPath1` AS `DataCollection_xtalSnapshotFullPath1`,`DataCollection`.`xtalSnapshotFullPath2` AS `DataCollection_xtalSnapshotFullPath2`,`DataCollection`.`xtalSnapshotFullPath3` AS `DataCollection_xtalSnapshotFullPath3`,`DataCollection`.`xtalSnapshotFullPath4` AS `DataCollection_xtalSnapshotFullPath4`,`DataCollection`.`phiStart` AS `DataCollection_phiStart`,`DataCollection`.`kappaStart` AS `DataCollection_kappaStart`,`DataCollection`.`omegaStart` AS `DataCollection_omegaStart`,`DataCollection`.`resolutionAtCorner` AS `DataCollection_resolutionAtCorner`,`DataCollection`.`bestWilsonPlotPath` AS `DataCollection_bestWilsonPlotPath`,`DataCollection`.`dataCollectionNumber` AS `DataCollection_dataCollectionNumber`,`DataCollection`.`axisRange` AS `DataCollection_axisRange`,`DataCollection`.`axisStart` AS `DataCollection_axisStart`,`DataCollection`.`axisEnd` AS `DataCollection_axisEnd`,`Workflow`.`workflowTitle` AS `Workflow_workflowTitle`,`Workflow`.`workflowType` AS `Workflow_workflowType`,`Workflow`.`status` AS `Workflow_status` from ((((((`DataCollectionGroup` left join `DataCollection` on(((`DataCollection`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`) and (`DataCollection`.`dataCollectionId` = (select max(`dc2`.`dataCollectionId`) from `DataCollection` `dc2` where (`dc2`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`)))))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`))) left join `BLSample` on((`BLSample`.`blSampleId` = `DataCollectionGroup`.`blSampleId`))) left join `Crystal` on((`Crystal`.`crystalId` = `BLSample`.`crystalId`))) left join `Protein` on((`Protein`.`proteinId` = `Crystal`.`proteinId`))) left join `Workflow` on((`DataCollectionGroup`.`workflowId` = `Workflow`.`workflowId`)));

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection_summary_phasing` AS select `AutoProcIntegration`.`autoProcIntegrationId` AS `v_datacollection_summary_phasing_autoProcIntegrationId`,`AutoProcIntegration`.`dataCollectionId` AS `v_datacollection_summary_phasing_dataCollectionId`,`AutoProcIntegration`.`cell_a` AS `v_datacollection_summary_phasing_cell_a`,`AutoProcIntegration`.`cell_b` AS `v_datacollection_summary_phasing_cell_b`,`AutoProcIntegration`.`cell_c` AS `v_datacollection_summary_phasing_cell_c`,`AutoProcIntegration`.`cell_alpha` AS `v_datacollection_summary_phasing_cell_alpha`,`AutoProcIntegration`.`cell_beta` AS `v_datacollection_summary_phasing_cell_beta`,`AutoProcIntegration`.`cell_gamma` AS `v_datacollection_summary_phasing_cell_gamma`,`AutoProcIntegration`.`anomalous` AS `v_datacollection_summary_phasing_anomalous`,`AutoProc`.`spaceGroup` AS `v_datacollection_summary_phasing_autoproc_space_group`,`AutoProc`.`autoProcId` AS `v_datacollection_summary_phasing_autoproc_autoprocId`,`AutoProcScaling`.`autoProcScalingId` AS `v_datacollection_summary_phasing_autoProcScalingId`,`AutoProcProgram`.`processingPrograms` AS `v_datacollection_summary_phasing_processingPrograms`,`AutoProcProgram`.`autoProcProgramId` AS `v_datacollection_summary_phasing_autoProcProgramId`,`AutoProcProgram`.`processingStatus` AS `v_datacollection_summary_phasing_processingStatus`,`BLSession`.`sessionId` AS `v_datacollection_summary_session_sessionId`,`BLSession`.`proposalId` AS `v_datacollection_summary_session_proposalId` from (((((((`AutoProcIntegration` left join `DataCollection` on((`DataCollection`.`dataCollectionId` = `AutoProcIntegration`.`dataCollectionId`))) left join `DataCollectionGroup` on((`DataCollection`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`))) left join `AutoProcScaling_has_Int` on((`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`))) left join `AutoProcScaling` on((`AutoProcScaling_has_Int`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `AutoProcProgram` on((`AutoProcProgram`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`))) left join `AutoProc` on((`AutoProc`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`)));

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_datacollection_summary_screening` AS select `Screening`.`screeningId` AS `Screening_screeningId`,`Screening`.`dataCollectionId` AS `Screening_dataCollectionId`,`Screening`.`dataCollectionGroupId` AS `Screening_dataCollectionGroupId`,`ScreeningOutput`.`strategySuccess` AS `ScreeningOutput_strategySuccess`,`ScreeningOutput`.`indexingSuccess` AS `ScreeningOutput_indexingSuccess`,`ScreeningOutput`.`rankingResolution` AS `ScreeningOutput_rankingResolution`,`ScreeningOutput`.`mosaicityEstimated` AS `ScreeningOutput_mosaicityEstimated`,`ScreeningOutput`.`mosaicity` AS `ScreeningOutput_mosaicity`,`ScreeningOutput`.`totalExposureTime` AS `ScreeningOutput_totalExposureTime`,`ScreeningOutput`.`totalRotationRange` AS `ScreeningOutput_totalRotationRange`,`ScreeningOutput`.`totalNumberOfImages` AS `ScreeningOutput_totalNumberOfImages`,`ScreeningOutputLattice`.`spaceGroup` AS `ScreeningOutputLattice_spaceGroup`,`ScreeningOutputLattice`.`unitCell_a` AS `ScreeningOutputLattice_unitCell_a`,`ScreeningOutputLattice`.`unitCell_b` AS `ScreeningOutputLattice_unitCell_b`,`ScreeningOutputLattice`.`unitCell_c` AS `ScreeningOutputLattice_unitCell_c`,`ScreeningOutputLattice`.`unitCell_alpha` AS `ScreeningOutputLattice_unitCell_alpha`,`ScreeningOutputLattice`.`unitCell_beta` AS `ScreeningOutputLattice_unitCell_beta`,`ScreeningOutputLattice`.`unitCell_gamma` AS `ScreeningOutputLattice_unitCell_gamma`,`ScreeningStrategySubWedge`.`exposureTime` AS `ScreeningStrategySubWedge_exposureTime`,`ScreeningStrategySubWedge`.`transmission` AS `ScreeningStrategySubWedge_transmission`,`ScreeningStrategySubWedge`.`oscillationRange` AS `ScreeningStrategySubWedge_oscillationRange`,`ScreeningStrategySubWedge`.`numberOfImages` AS `ScreeningStrategySubWedge_numberOfImages`,`ScreeningStrategySubWedge`.`multiplicity` AS `ScreeningStrategySubWedge_multiplicity`,`ScreeningStrategySubWedge`.`completeness` AS `ScreeningStrategySubWedge_completeness`,`ScreeningStrategySubWedge`.`axisStart` AS `ScreeningStrategySubWedge_axisStart`,`ScreeningStrategySubWedge`.`axisEnd` AS `ScreeningStrategySubWedge_axisEnd`,`ScreeningStrategySubWedge`.`rotationAxis` AS `ScreeningStrategySubWedge_rotationAxis` from (((((`Screening` left join `ScreeningOutput` on((`Screening`.`screeningId` = `ScreeningOutput`.`screeningId`))) left join `ScreeningOutputLattice` on((`ScreeningOutput`.`screeningOutputId` = `ScreeningOutputLattice`.`screeningOutputId`))) left join `ScreeningStrategy` on((`ScreeningStrategy`.`screeningOutputId` = `ScreeningOutput`.`screeningOutputId`))) left join `ScreeningStrategyWedge` on((`ScreeningStrategyWedge`.`screeningStrategyId` = `ScreeningStrategy`.`screeningStrategyId`))) left join `ScreeningStrategySubWedge` on((`ScreeningStrategySubWedge`.`screeningStrategyWedgeId` = `ScreeningStrategyWedge`.`screeningStrategyWedgeId`)));

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_dewar` AS select `p`.`proposalId` AS `proposalId`,`s`.`shippingId` AS `shippingId`,`s`.`shippingName` AS `shippingName`,`d`.`dewarId` AS `dewarId`,`d`.`code` AS `dewarName`,`d`.`dewarStatus` AS `dewarStatus`,`p`.`proposalCode` AS `proposalCode`,`p`.`proposalNumber` AS `proposalNumber`,`s`.`creationDate` AS `creationDate`,`s`.`shippingType` AS `shippingType`,`d`.`barCode` AS `barCode`,`s`.`shippingStatus` AS `shippingStatus`,`ss`.`beamLineName` AS `beamLineName`,count(distinct `h1`.`DewarTransportHistoryId`) AS `nbEvents`,count(distinct `h2`.`DewarTransportHistoryId`) AS `storesin`,count(if((`bls`.`code` is not null),1,NULL)) AS `nbSamples` from (((((((`Proposal` `p` join `Shipping` `s` on((`s`.`proposalId` = `p`.`proposalId`))) join `Dewar` `d` on((`d`.`shippingId` = `s`.`shippingId`))) left join `Container` `c` on((`c`.`dewarId` = `d`.`dewarId`))) left join `BLSession` `ss` on((`ss`.`sessionId` = `d`.`firstExperimentId`))) left join `BLSample` `bls` on((`bls`.`containerId` = `c`.`containerId`))) left join `DewarTransportHistory` `h1` on(((`h1`.`dewarId` = `d`.`dewarId`) and ((`h1`.`dewarStatus` = 'at ESRF') or (`h1`.`dewarStatus` = 'sent to User'))))) left join `DewarTransportHistory` `h2` on(((`h2`.`dewarId` = `d`.`dewarId`) and (`h2`.`storageLocation` = 'STORES-IN')))) where (((`p`.`proposalCode` like 'MX%') or (`p`.`proposalCode` like 'FX%') or (`p`.`proposalCode` like 'IFX%') or (`p`.`proposalCode` like 'IX%') or (`p`.`proposalCode` like 'BM14U%') or (`p`.`proposalCode` like 'bm161%') or (`p`.`proposalCode` like 'bm162%')) and ((`p`.`proposalCode` <> 'FX') or (`p`.`proposalNumber` <> '999')) and ((`p`.`proposalCode` <> 'IFX') or (`p`.`proposalNumber` <> '999')) and ((`p`.`proposalCode` <> 'MX') or (`p`.`proposalNumber` <> '415')) and (`d`.`type` = 'Dewar') and (`s`.`creationDate` is not null)) group by `d`.`dewarId`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_dewarBeamline` AS select `d`.`beamLineName` AS `beamLineName`,count(0) AS `COUNT(*)` from `v_dewar` `d` where (`d`.`beamLineName` is not null) group by `d`.`beamLineName`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_dewarBeamlineByWeek` AS select substr(`w`.`num`,6) AS `Week`,count(if((`d`.`beamLineName` like 'ID14%'),1,NULL)) AS `ID14`,count(if((`d`.`beamLineName` like 'ID23%'),1,NULL)) AS `ID23`,count(if((`d`.`beamLineName` like 'ID29%'),1,NULL)) AS `ID29`,count(if((`d`.`beamLineName` like 'BM14%'),1,NULL)) AS `BM14` from (`v_week` `w` left join `v_dewar` `d` on((`w`.`num` = date_format(`d`.`creationDate`,'%x-%v')))) group by `w`.`num` order by `w`.`num`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_dewarByWeek` AS select substr(`w`.`num`,6) AS `Week`,count(if((`d`.`shippingType` = 'DewarTracking'),1,NULL)) AS `Dewars Tracked`,count(if(((`d`.`proposalCode` is not null) and ((`d`.`shippingType` <> 'DewarTracking') or isnull(`d`.`shippingType`))),1,NULL)) AS `Dewars Non-Tracked` from (`v_week` `w` left join `v_dewar` `d` on((`w`.`num` = date_format(`d`.`creationDate`,'%Y-%v')))) group by substr(`w`.`num`,6) order by `w`.`num`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_dewarByWeekTotal` AS select substr(`w`.`num`,6) AS `Week`,count(if((`d`.`shippingType` = 'DewarTracking'),1,NULL)) AS `Dewars Tracked`,count(if(((`d`.`proposalCode` is not null) and ((`d`.`shippingType` <> 'DewarTracking') or isnull(`d`.`shippingType`))),1,NULL)) AS `Dewars Non-Tracked`,count(if((`d`.`proposalCode` is not null),1,NULL)) AS `Total` from (`v_week` `w` left join `v_dewar` `d` on((`w`.`num` = date_format(`d`.`creationDate`,'%Y-%v')))) group by substr(`w`.`num`,6) order by substr(`w`.`num`,6);

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_dewarList` AS select concat(`d`.`proposalCode`,`d`.`proposalNumber`) AS `proposal`,`d`.`shippingName` AS `shippingName`,`d`.`dewarName` AS `dewarName`,`d`.`barCode` AS `barCode`,date_format(`d`.`creationDate`,'%d/%m/%Y') AS `creationDate`,`d`.`shippingType` AS `shippingType`,count(distinct `h`.`DewarTransportHistoryId`) AS `nbEvents`,`d`.`dewarStatus` AS `dewarStatus`,`d`.`shippingStatus` AS `shippingStatus`,count(if((`bls`.`blSampleId` is not null),1,NULL)) AS `nbSamples` from (((`v_dewar` `d` left join `Container` `c` on((`c`.`dewarId` = `d`.`dewarId`))) left join `BLSample` `bls` on((`bls`.`containerId` = `c`.`containerId`))) left join `DewarTransportHistory` `h` on((`h`.`dewarId` = `d`.`dewarId`))) group by `d`.`dewarId` order by `d`.`creationDate` desc;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_dewarProposalCode` AS select `d`.`proposalCode` AS `proposalCode`,count(0) AS `COUNT(*)` from `v_dewar` `d` group by `d`.`proposalCode`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_dewarProposalCodeByWeek` AS select substr(`w`.`num`,6) AS `Week`,count(if((`d`.`proposalCode` = 'MX'),1,NULL)) AS `MX`,count(if((`d`.`proposalCode` = 'FX'),1,NULL)) AS `FX`,count(if((`d`.`proposalCode` = 'BM14U'),1,NULL)) AS `BM14U`,count(if((`d`.`proposalCode` = 'BM161'),1,NULL)) AS `BM161`,count(if((`d`.`proposalCode` = 'BM162'),1,NULL)) AS `BM162`,count(if(((`d`.`proposalCode` <> 'MX') and (`d`.`proposalCode` <> 'FX') and (`d`.`proposalCode` <> 'BM14U') and (`d`.`proposalCode` <> 'BM161') and (`d`.`proposalCode` <> 'BM162')),1,NULL)) AS `Others` from (`v_week` `w` left join `v_dewar` `d` on((`w`.`num` = date_format(`d`.`creationDate`,'%Y-%v')))) group by substr(`w`.`num`,6) order by substr(`w`.`num`,6);

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_dewar_summary` AS select `Shipping`.`shippingName` AS `shippingName`,`Shipping`.`deliveryAgent_agentName` AS `deliveryAgent_agentName`,`Shipping`.`deliveryAgent_shippingDate` AS `deliveryAgent_shippingDate`,`Shipping`.`deliveryAgent_deliveryDate` AS `deliveryAgent_deliveryDate`,`Shipping`.`deliveryAgent_agentCode` AS `deliveryAgent_agentCode`,`Shipping`.`deliveryAgent_flightCode` AS `deliveryAgent_flightCode`,`Shipping`.`shippingStatus` AS `shippingStatus`,`Shipping`.`bltimeStamp` AS `bltimeStamp`,`Shipping`.`laboratoryId` AS `laboratoryId`,`Shipping`.`isStorageShipping` AS `isStorageShipping`,`Shipping`.`creationDate` AS `creationDate`,`Shipping`.`comments` AS `Shipping_comments`,`Shipping`.`sendingLabContactId` AS `sendingLabContactId`,`Shipping`.`returnLabContactId` AS `returnLabContactId`,`Shipping`.`returnCourier` AS `returnCourier`,`Shipping`.`dateOfShippingToUser` AS `dateOfShippingToUser`,`Shipping`.`shippingType` AS `shippingType`,`Dewar`.`dewarId` AS `dewarId`,`Dewar`.`shippingId` AS `shippingId`,`Dewar`.`code` AS `dewarCode`,`Dewar`.`comments` AS `comments`,`Dewar`.`storageLocation` AS `storageLocation`,`Dewar`.`dewarStatus` AS `dewarStatus`,`Dewar`.`isStorageDewar` AS `isStorageDewar`,`Dewar`.`barCode` AS `barCode`,`Dewar`.`firstExperimentId` AS `firstExperimentId`,`Dewar`.`customsValue` AS `customsValue`,`Dewar`.`transportValue` AS `transportValue`,`Dewar`.`trackingNumberToSynchrotron` AS `trackingNumberToSynchrotron`,`Dewar`.`trackingNumberFromSynchrotron` AS `trackingNumberFromSynchrotron`,`Dewar`.`type` AS `type`,`Dewar`.`isReimbursed` AS `isReimbursed`,`BLSession`.`sessionId` AS `sessionId`,`BLSession`.`beamLineName` AS `beamlineName`,`BLSession`.`startDate` AS `sessionStartDate`,`BLSession`.`endDate` AS `sessionEndDate`,`BLSession`.`beamLineOperator` AS `beamLineOperator`,`BLSession`.`nbReimbDewars` AS `nbReimbDewars`,`Proposal`.`proposalId` AS `proposalId`,`Container`.`containerId` AS `containerId`,`Container`.`containerType` AS `containerType`,`Container`.`capacity` AS `capacity`,`Container`.`beamlineLocation` AS `beamlineLocation`,`Container`.`sampleChangerLocation` AS `sampleChangerLocation`,`Container`.`containerStatus` AS `containerStatus`,`Container`.`code` AS `containerCode` from (((((`Dewar` left join `Container` on((`Container`.`dewarId` = `Dewar`.`dewarId`))) left join `Shipping` on((`Shipping`.`shippingId` = `Dewar`.`shippingId`))) left join `ShippingHasSession` on((`Shipping`.`shippingId` = `ShippingHasSession`.`shippingId`))) left join `BLSession` on((`ShippingHasSession`.`sessionId` = `BLSession`.`sessionId`))) left join `Proposal` on((`Proposal`.`proposalId` = `Shipping`.`proposalId`))) order by `Dewar`.`dewarId` desc;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_em_movie` AS select `Movie`.`movieId` AS `Movie_movieId`,`Movie`.`dataCollectionId` AS `Movie_dataCollectionId`,`Movie`.`movieNumber` AS `Movie_movieNumber`,`Movie`.`movieFullPath` AS `Movie_movieFullPath`,`Movie`.`positionX` AS `Movie_positionX`,`Movie`.`positionY` AS `Movie_positionY`,`Movie`.`micrographFullPath` AS `Movie_micrographFullPath`,`Movie`.`micrographSnapshotFullPath` AS `Movie_micrographSnapshotFullPath`,`Movie`.`xmlMetaDataFullPath` AS `Movie_xmlMetaDataFullPath`,`Movie`.`dosePerImage` AS `Movie_dosePerImage`,`Movie`.`createdTimeStamp` AS `Movie_createdTimeStamp`,`MotionCorrection`.`motionCorrectionId` AS `MotionCorrection_motionCorrectionId`,`MotionCorrection`.`movieId` AS `MotionCorrection_movieId`,`MotionCorrection`.`firstFrame` AS `MotionCorrection_firstFrame`,`MotionCorrection`.`lastFrame` AS `MotionCorrection_lastFrame`,`MotionCorrection`.`dosePerFrame` AS `MotionCorrection_dosePerFrame`,`MotionCorrection`.`doseWeight` AS `MotionCorrection_doseWeight`,`MotionCorrection`.`totalMotion` AS `MotionCorrection_totalMotion`,`MotionCorrection`.`averageMotionPerFrame` AS `MotionCorrection_averageMotionPerFrame`,`MotionCorrection`.`driftPlotFullPath` AS `MotionCorrection_driftPlotFullPath`,`MotionCorrection`.`micrographFullPath` AS `MotionCorrection_micrographFullPath`,`MotionCorrection`.`micrographSnapshotFullPath` AS `MotionCorrection_micrographSnapshotFullPath`,`MotionCorrection`.`correctedDoseMicrographFullPath` AS `MotionCorrection_correctedDoseMicrographFullPath`,`MotionCorrection`.`patchesUsed` AS `MotionCorrection_patchesUsed`,`MotionCorrection`.`logFileFullPath` AS `MotionCorrection_logFileFullPath`,`CTF`.`CTFid` AS `CTF_CTFid`,`CTF`.`motionCorrectionId` AS `CTF_motionCorrectionId`,`CTF`.`spectraImageThumbnailFullPath` AS `CTF_spectraImageThumbnailFullPath`,`CTF`.`spectraImageFullPath` AS `CTF_spectraImageFullPath`,`CTF`.`defocusU` AS `CTF_defocusU`,`CTF`.`defocusV` AS `CTF_defocusV`,`CTF`.`angle` AS `CTF_angle`,`CTF`.`crossCorrelationCoefficient` AS `CTF_crossCorrelationCoefficient`,`CTF`.`resolutionLimit` AS `CTF_resolutionLimit`,`CTF`.`estimatedBfactor` AS `CTF_estimatedBfactor`,`CTF`.`logFilePath` AS `CTF_logFilePath`,`CTF`.`createdTimeStamp` AS `CTF_createdTimeStamp`,`DataCollection`.`xBeamPix` AS `DataCollection_xBeamPix`,`Proposal`.`proposalId` AS `Proposal_proposalId`,`BLSession`.`sessionId` AS `BLSession_sessionId` from ((((((`Movie` left join `MotionCorrection` on((`MotionCorrection`.`movieId` = `Movie`.`movieId`))) left join `DataCollection` on((`DataCollection`.`dataCollectionId` = `Movie`.`dataCollectionId`))) left join `DataCollectionGroup` on((`DataCollectionGroup`.`dataCollectionGroupId` = `DataCollection`.`dataCollectionGroupId`))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`))) left join `Proposal` on((`Proposal`.`proposalId` = `BLSession`.`proposalId`))) left join `CTF` on((`CTF`.`motionCorrectionId` = `MotionCorrection`.`motionCorrectionId`)));

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_em_stats` AS select `Proposal`.`proposalId` AS `proposalId`,`BLSession`.`sessionId` AS `sessionId`,`DataCollection`.`imageDirectory` AS `imageDirectory`,`Movie`.`movieId` AS `movieId`,`Movie`.`createdTimeStamp` AS `createdTimeStamp`,`MotionCorrection`.`motionCorrectionId` AS `motionCorrectionId`,`DataCollection`.`dataCollectionId` AS `dataCollectionId`,`MotionCorrection`.`totalMotion` AS `totalMotion`,`MotionCorrection`.`averageMotionPerFrame` AS `averageMotionPerFrame`,`MotionCorrection`.`lastFrame` AS `lastFrame`,`MotionCorrection`.`dosePerFrame` AS `dosePerFrame`,`CTF`.`defocusU` AS `defocusU`,`CTF`.`defocusV` AS `defocusV` from ((((((`BLSession` join `Proposal` on((`Proposal`.`proposalId` = `BLSession`.`proposalId`))) join `DataCollectionGroup` on((`DataCollectionGroup`.`sessionId` = `BLSession`.`sessionId`))) join `DataCollection` on((`DataCollection`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`))) join `Movie` on((`Movie`.`dataCollectionId` = `DataCollection`.`dataCollectionId`))) join `MotionCorrection` on((`MotionCorrection`.`movieId` = `Movie`.`movieId`))) join `CTF` on((`CTF`.`motionCorrectionId` = `MotionCorrection`.`motionCorrectionId`)));

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_energyScan` AS select `EnergyScan`.`energyScanId` AS `energyScanId`,`EnergyScan`.`sessionId` AS `sessionId`,`EnergyScan`.`blSampleId` AS `blSampleId`,`EnergyScan`.`fluorescenceDetector` AS `fluorescenceDetector`,`EnergyScan`.`scanFileFullPath` AS `scanFileFullPath`,`EnergyScan`.`choochFileFullPath` AS `choochFileFullPath`,`EnergyScan`.`jpegChoochFileFullPath` AS `jpegChoochFileFullPath`,`EnergyScan`.`element` AS `element`,`EnergyScan`.`startEnergy` AS `startEnergy`,`EnergyScan`.`endEnergy` AS `endEnergy`,`EnergyScan`.`transmissionFactor` AS `transmissionFactor`,`EnergyScan`.`exposureTime` AS `exposureTime`,`EnergyScan`.`synchrotronCurrent` AS `synchrotronCurrent`,`EnergyScan`.`temperature` AS `temperature`,`EnergyScan`.`peakEnergy` AS `peakEnergy`,`EnergyScan`.`peakFPrime` AS `peakFPrime`,`EnergyScan`.`peakFDoublePrime` AS `peakFDoublePrime`,`EnergyScan`.`inflectionEnergy` AS `inflectionEnergy`,`EnergyScan`.`inflectionFPrime` AS `inflectionFPrime`,`EnergyScan`.`inflectionFDoublePrime` AS `inflectionFDoublePrime`,`EnergyScan`.`xrayDose` AS `xrayDose`,`EnergyScan`.`startTime` AS `startTime`,`EnergyScan`.`endTime` AS `endTime`,`EnergyScan`.`edgeEnergy` AS `edgeEnergy`,`EnergyScan`.`filename` AS `filename`,`EnergyScan`.`beamSizeVertical` AS `beamSizeVertical`,`EnergyScan`.`beamSizeHorizontal` AS `beamSizeHorizontal`,`EnergyScan`.`crystalClass` AS `crystalClass`,`EnergyScan`.`comments` AS `comments`,`EnergyScan`.`flux` AS `flux`,`EnergyScan`.`flux_end` AS `flux_end`,`BLSample`.`blSampleId` AS `BLSample_sampleId`,`BLSample`.`name` AS `name`,`BLSample`.`code` AS `code`,`Protein`.`acronym` AS `acronym`,`BLSession`.`proposalId` AS `BLSession_proposalId` from ((((`EnergyScan` left join `BLSample` on((`BLSample`.`blSampleId` = `EnergyScan`.`blSampleId`))) left join `Crystal` on((`Crystal`.`crystalId` = `BLSample`.`crystalId`))) left join `Protein` on((`Protein`.`proteinId` = `Crystal`.`proteinId`))) left join `BLSession` on((`BLSession`.`sessionId` = `EnergyScan`.`sessionId`)));

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_hour` AS select date_format((now() - interval 23 hour),_utf8'%Y-%m-%d %H') AS `num` union select date_format((now() - interval 22 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 22 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 21 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 21 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 20 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 20 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 19 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 19 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 18 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 18 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 17 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 17 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 16 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 16 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 15 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 15 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 14 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 14 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 13 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 13 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 12 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 12 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 11 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 11 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 10 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 10 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 9 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 9 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 8 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 8 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 7 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 7 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 6 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 6 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 5 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 5 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 4 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 4 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 3 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 3 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 2 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 2 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 1 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 1 HOUR),'%Y-%m-%d %H')` union select date_format((now() - interval 0 hour),_utf8'%Y-%m-%d %H') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 0 HOUR),'%Y-%m-%d %H')`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_logonByHour` AS select date_format(`w`.`num`,'%H') AS `Hour`,count(distinct `s`.`detail`) AS `Distinct logins`,(count(`s`.`detail`) - count(distinct `s`.`detail`)) AS `Total logins` from (`v_hour` `w` left join `v_Log4Stat` `s` on(((`w`.`num` = date_format(`s`.`timestamp`,'%Y-%m-%d %H')) and (`s`.`msg` = 'LOGON')))) group by date_format(`w`.`num`,'%H') order by `w`.`num`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_logonByMonthDay` AS select date_format(`w`.`num`,'%d/%m') AS `Day`,count(distinct `s`.`detail`) AS `Distinct logins`,(count(`s`.`detail`) - count(distinct `s`.`detail`)) AS `Total logins` from (`v_monthDay` `w` left join `v_Log4Stat` `s` on(((`w`.`num` = date_format(`s`.`timestamp`,'%Y-%m-%d')) and (`s`.`msg` = 'LOGON')))) group by date_format(`w`.`num`,'%d/%m') order by `w`.`num`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_logonByWeek` AS select substr(`w`.`num`,6) AS `Week`,count(distinct `s`.`detail`) AS `Distinct logins`,(count(`s`.`detail`) - count(distinct `s`.`detail`)) AS `Total logins` from (`v_week` `w` left join `v_Log4Stat` `s` on(((`w`.`num` = date_format(`s`.`timestamp`,'%Y-%v')) and (`s`.`msg` = 'LOGON')))) group by substr(`w`.`num`,6) order by `w`.`num`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_logonByWeekDay` AS select date_format(`w`.`day`,'%W') AS `Day`,count(distinct `s`.`detail`) AS `Distinct logins`,(count(`s`.`detail`) - count(distinct `s`.`detail`)) AS `Total logins` from (`v_weekDay` `w` left join `v_Log4Stat` `s` on(((`w`.`day` = date_format(`s`.`timestamp`,'%Y-%m-%d')) and (`s`.`msg` = 'LOGON')))) group by `w`.`day` order by `w`.`day`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_monthDay` AS select date_format((now() - interval 31 day),_utf8'%Y-%m-%d') AS `num` union select date_format((now() - interval 30 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 30 DAY),'%Y-%m-%d')` union select date_format((now() - interval 29 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 29 DAY),'%Y-%m-%d')` union select date_format((now() - interval 28 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 28 DAY),'%Y-%m-%d')` union select date_format((now() - interval 27 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 27 DAY),'%Y-%m-%d')` union select date_format((now() - interval 26 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 26 DAY),'%Y-%m-%d')` union select date_format((now() - interval 25 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 25 DAY),'%Y-%m-%d')` union select date_format((now() - interval 24 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 24 DAY),'%Y-%m-%d')` union select date_format((now() - interval 23 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 23 DAY),'%Y-%m-%d')` union select date_format((now() - interval 22 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 22 DAY),'%Y-%m-%d')` union select date_format((now() - interval 21 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 21 DAY),'%Y-%m-%d')` union select date_format((now() - interval 20 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 20 DAY),'%Y-%m-%d')` union select date_format((now() - interval 19 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 19 DAY),'%Y-%m-%d')` union select date_format((now() - interval 18 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 18 DAY),'%Y-%m-%d')` union select date_format((now() - interval 17 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 17 DAY),'%Y-%m-%d')` union select date_format((now() - interval 16 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 16 DAY),'%Y-%m-%d')` union select date_format((now() - interval 15 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 15 DAY),'%Y-%m-%d')` union select date_format((now() - interval 14 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 14 DAY),'%Y-%m-%d')` union select date_format((now() - interval 13 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 13 DAY),'%Y-%m-%d')` union select date_format((now() - interval 12 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 12 DAY),'%Y-%m-%d')` union select date_format((now() - interval 11 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 11 DAY),'%Y-%m-%d')` union select date_format((now() - interval 10 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 10 DAY),'%Y-%m-%d')` union select date_format((now() - interval 9 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 9 DAY),'%Y-%m-%d')` union select date_format((now() - interval 8 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 8 DAY),'%Y-%m-%d')` union select date_format((now() - interval 7 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 7 DAY),'%Y-%m-%d')` union select date_format((now() - interval 6 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 6 DAY),'%Y-%m-%d')` union select date_format((now() - interval 5 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 5 DAY),'%Y-%m-%d')` union select date_format((now() - interval 4 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 4 DAY),'%Y-%m-%d')` union select date_format((now() - interval 3 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 3 DAY),'%Y-%m-%d')` union select date_format((now() - interval 2 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 2 DAY),'%Y-%m-%d')` union select date_format((now() - interval 1 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 1 DAY),'%Y-%m-%d')` union select date_format((now() - interval 0 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 0 DAY),'%Y-%m-%d')`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_mx_autoprocessing_stats` AS select `AutoProcScalingStatistics`.`autoProcScalingStatisticsId` AS `autoProcScalingStatisticsId`,`AutoProcScalingStatistics`.`autoProcScalingId` AS `autoProcScalingId`,`AutoProcScalingStatistics`.`scalingStatisticsType` AS `scalingStatisticsType`,`AutoProcScalingStatistics`.`resolutionLimitLow` AS `resolutionLimitLow`,`AutoProcScalingStatistics`.`resolutionLimitHigh` AS `resolutionLimitHigh`,`AutoProcScalingStatistics`.`rMerge` AS `rMerge`,`AutoProcScalingStatistics`.`rMeasWithinIPlusIMinus` AS `rMeasWithinIPlusIMinus`,`AutoProcScalingStatistics`.`rMeasAllIPlusIMinus` AS `rMeasAllIPlusIMinus`,`AutoProcScalingStatistics`.`rPimWithinIPlusIMinus` AS `rPimWithinIPlusIMinus`,`AutoProcScalingStatistics`.`rPimAllIPlusIMinus` AS `rPimAllIPlusIMinus`,`AutoProcScalingStatistics`.`fractionalPartialBias` AS `fractionalPartialBias`,`AutoProcScalingStatistics`.`nTotalObservations` AS `nTotalObservations`,`AutoProcScalingStatistics`.`nTotalUniqueObservations` AS `nTotalUniqueObservations`,`AutoProcScalingStatistics`.`meanIOverSigI` AS `meanIOverSigI`,`AutoProcScalingStatistics`.`completeness` AS `completeness`,`AutoProcScalingStatistics`.`multiplicity` AS `multiplicity`,`AutoProcScalingStatistics`.`anomalousCompleteness` AS `anomalousCompleteness`,`AutoProcScalingStatistics`.`anomalousMultiplicity` AS `anomalousMultiplicity`,`AutoProcScalingStatistics`.`recordTimeStamp` AS `recordTimeStamp`,`AutoProcScalingStatistics`.`anomalous` AS `anomalous`,`AutoProcScalingStatistics`.`ccHalf` AS `ccHalf`,`AutoProcScalingStatistics`.`ccAno` AS `ccAno`,`AutoProcScalingStatistics`.`sigAno` AS `sigAno`,`AutoProcScalingStatistics`.`isa` AS `ISA`,`DataCollection`.`dataCollectionId` AS `dataCollectionId`,`DataCollection`.`strategySubWedgeOrigId` AS `strategySubWedgeOrigId`,`DataCollection`.`detectorId` AS `detectorId`,`DataCollection`.`blSubSampleId` AS `blSubSampleId`,`DataCollection`.`dataCollectionNumber` AS `dataCollectionNumber`,`DataCollection`.`startTime` AS `startTime`,`DataCollection`.`endTime` AS `endTime`,`BLSession`.`sessionId` AS `sessionId`,`BLSession`.`proposalId` AS `proposalId`,`BLSession`.`beamLineName` AS `beamLineName` from ((((((`AutoProcScalingStatistics` left join `AutoProcScaling` on((`AutoProcScalingStatistics`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `AutoProcScaling_has_Int` on((`AutoProcScaling_has_Int`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `AutoProcIntegration` on((`AutoProcIntegration`.`autoProcIntegrationId` = `AutoProcScaling_has_Int`.`autoProcIntegrationId`))) left join `DataCollection` on((`DataCollection`.`dataCollectionId` = `AutoProcIntegration`.`dataCollectionId`))) left join `DataCollectionGroup` on((`DataCollectionGroup`.`dataCollectionGroupId` = `DataCollection`.`dataCollectionGroupId`))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`)));

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_mx_sample` AS select `BLSample`.`blSampleId` AS `BLSample_blSampleId`,`BLSample`.`diffractionPlanId` AS `BLSample_diffractionPlanId`,`BLSample`.`crystalId` AS `BLSample_crystalId`,`BLSample`.`containerId` AS `BLSample_containerId`,`BLSample`.`name` AS `BLSample_name`,`BLSample`.`code` AS `BLSample_code`,`BLSample`.`location` AS `BLSample_location`,`BLSample`.`holderLength` AS `BLSample_holderLength`,`BLSample`.`loopLength` AS `BLSample_loopLength`,`BLSample`.`loopType` AS `BLSample_loopType`,`BLSample`.`wireWidth` AS `BLSample_wireWidth`,`BLSample`.`comments` AS `BLSample_comments`,`BLSample`.`completionStage` AS `BLSample_completionStage`,`BLSample`.`structureStage` AS `BLSample_structureStage`,`BLSample`.`publicationStage` AS `BLSample_publicationStage`,`BLSample`.`publicationComments` AS `BLSample_publicationComments`,`BLSample`.`blSampleStatus` AS `BLSample_blSampleStatus`,`BLSample`.`isInSampleChanger` AS `BLSample_isInSampleChanger`,`BLSample`.`lastKnownCenteringPosition` AS `BLSample_lastKnownCenteringPosition`,`BLSample`.`recordTimeStamp` AS `BLSample_recordTimeStamp`,`BLSample`.`SMILES` AS `BLSample_SMILES`,`Protein`.`proteinId` AS `Protein_proteinId`,`Protein`.`name` AS `Protein_name`,`Protein`.`acronym` AS `Protein_acronym`,`Protein`.`proteinType` AS `Protein_proteinType`,`Shipping`.`proposalId` AS `Protein_proposalId`,`Person`.`personId` AS `Person_personId`,`Person`.`familyName` AS `Person_familyName`,`Person`.`givenName` AS `Person_givenName`,`Person`.`emailAddress` AS `Person_emailAddress`,`Container`.`containerId` AS `Container_containerId`,`Container`.`code` AS `Container_code`,`Container`.`containerType` AS `Container_containerType`,`Container`.`containerStatus` AS `Container_containerStatus`,`Container`.`beamlineLocation` AS `Container_beamlineLocation`,`Container`.`sampleChangerLocation` AS `Container_sampleChangerLocation`,`Dewar`.`code` AS `Dewar_code`,`Dewar`.`dewarId` AS `Dewar_dewarId`,`Dewar`.`storageLocation` AS `Dewar_storageLocation`,`Dewar`.`dewarStatus` AS `Dewar_dewarStatus`,`Dewar`.`barCode` AS `Dewar_barCode`,`Shipping`.`shippingId` AS `Shipping_shippingId`,`BLSession`.`sessionId` AS `sessionId`,`BLSession`.`startDate` AS `BLSession_startDate`,`BLSession`.`beamLineName` AS `BLSession_beamLineName` from ((((((((`BLSample` left join `Crystal` on((`Crystal`.`crystalId` = `BLSample`.`crystalId`))) left join `Protein` on((`Protein`.`proteinId` = `Crystal`.`proteinId`))) left join `Person` on((`Person`.`personId` = `Protein`.`personId`))) left join `Container` on((`BLSample`.`containerId` = `Container`.`containerId`))) left join `Dewar` on((`Dewar`.`dewarId` = `Container`.`dewarId`))) left join `Shipping` on((`Dewar`.`shippingId` = `Shipping`.`shippingId`))) left join `DataCollectionGroup` on((`DataCollectionGroup`.`blSampleId` = `BLSample`.`blSampleId`))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`)));

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_phasing` AS select `BLSample`.`blSampleId` AS `BLSample_blSampleId`,`AutoProcIntegration`.`autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`,`AutoProcIntegration`.`dataCollectionId` AS `AutoProcIntegration_dataCollectionId`,`AutoProcIntegration`.`autoProcProgramId` AS `AutoProcIntegration_autoProcProgramId`,`AutoProcIntegration`.`startImageNumber` AS `AutoProcIntegration_startImageNumber`,`AutoProcIntegration`.`endImageNumber` AS `AutoProcIntegration_endImageNumber`,`AutoProcIntegration`.`refinedDetectorDistance` AS `AutoProcIntegration_refinedDetectorDistance`,`AutoProcIntegration`.`refinedXBeam` AS `AutoProcIntegration_refinedXBeam`,`AutoProcIntegration`.`refinedYBeam` AS `AutoProcIntegration_refinedYBeam`,`AutoProcIntegration`.`rotationAxisX` AS `AutoProcIntegration_rotationAxisX`,`AutoProcIntegration`.`rotationAxisY` AS `AutoProcIntegration_rotationAxisY`,`AutoProcIntegration`.`rotationAxisZ` AS `AutoProcIntegration_rotationAxisZ`,`AutoProcIntegration`.`beamVectorX` AS `AutoProcIntegration_beamVectorX`,`AutoProcIntegration`.`beamVectorY` AS `AutoProcIntegration_beamVectorY`,`AutoProcIntegration`.`beamVectorZ` AS `AutoProcIntegration_beamVectorZ`,`AutoProcIntegration`.`cell_a` AS `AutoProcIntegration_cell_a`,`AutoProcIntegration`.`cell_b` AS `AutoProcIntegration_cell_b`,`AutoProcIntegration`.`cell_c` AS `AutoProcIntegration_cell_c`,`AutoProcIntegration`.`cell_alpha` AS `AutoProcIntegration_cell_alpha`,`AutoProcIntegration`.`cell_beta` AS `AutoProcIntegration_cell_beta`,`AutoProcIntegration`.`cell_gamma` AS `AutoProcIntegration_cell_gamma`,`AutoProcIntegration`.`recordTimeStamp` AS `AutoProcIntegration_recordTimeStamp`,`AutoProcIntegration`.`anomalous` AS `AutoProcIntegration_anomalous`,`SpaceGroup`.`spaceGroupId` AS `SpaceGroup_spaceGroupId`,`SpaceGroup`.`geometryClassnameId` AS `SpaceGroup_geometryClassnameId`,`SpaceGroup`.`spaceGroupNumber` AS `SpaceGroup_spaceGroupNumber`,`SpaceGroup`.`spaceGroupShortName` AS `SpaceGroup_spaceGroupShortName`,`SpaceGroup`.`spaceGroupName` AS `SpaceGroup_spaceGroupName`,`SpaceGroup`.`bravaisLattice` AS `SpaceGroup_bravaisLattice`,`SpaceGroup`.`bravaisLatticeName` AS `SpaceGroup_bravaisLatticeName`,`SpaceGroup`.`pointGroup` AS `SpaceGroup_pointGroup`,`SpaceGroup`.`MX_used` AS `SpaceGroup_MX_used`,`PhasingStep`.`phasingStepId` AS `PhasingStep_phasingStepId`,`PhasingStep`.`previousPhasingStepId` AS `PhasingStep_previousPhasingStepId`,`PhasingStep`.`programRunId` AS `PhasingStep_programRunId`,`PhasingStep`.`spaceGroupId` AS `PhasingStep_spaceGroupId`,`PhasingStep`.`autoProcScalingId` AS `PhasingStep_autoProcScalingId`,`PhasingStep`.`phasingAnalysisId` AS `PhasingStep_phasingAnalysisId`,`PhasingStep`.`phasingStepType` AS `PhasingStep_phasingStepType`,`PhasingStep`.`method` AS `PhasingStep_method`,`PhasingStep`.`solventContent` AS `PhasingStep_solventContent`,`PhasingStep`.`enantiomorph` AS `PhasingStep_enantiomorph`,`PhasingStep`.`lowRes` AS `PhasingStep_lowRes`,`PhasingStep`.`highRes` AS `PhasingStep_highRes`,`PhasingStep`.`recordTimeStamp` AS `PhasingStep_recordTimeStamp`,`DataCollection`.`dataCollectionId` AS `DataCollection_dataCollectionId`,`DataCollection`.`dataCollectionGroupId` AS `DataCollection_dataCollectionGroupId`,`DataCollection`.`strategySubWedgeOrigId` AS `DataCollection_strategySubWedgeOrigId`,`DataCollection`.`detectorId` AS `DataCollection_detectorId`,`DataCollection`.`blSubSampleId` AS `DataCollection_blSubSampleId`,`DataCollection`.`dataCollectionNumber` AS `DataCollection_dataCollectionNumber`,`DataCollection`.`startTime` AS `DataCollection_startTime`,`DataCollection`.`endTime` AS `DataCollection_endTime`,`DataCollection`.`runStatus` AS `DataCollection_runStatus`,`DataCollection`.`axisStart` AS `DataCollection_axisStart`,`DataCollection`.`axisEnd` AS `DataCollection_axisEnd`,`DataCollection`.`axisRange` AS `DataCollection_axisRange`,`DataCollection`.`overlap` AS `DataCollection_overlap`,`DataCollection`.`numberOfImages` AS `DataCollection_numberOfImages`,`DataCollection`.`startImageNumber` AS `DataCollection_startImageNumber`,`DataCollection`.`numberOfPasses` AS `DataCollection_numberOfPasses`,`DataCollection`.`exposureTime` AS `DataCollection_exposureTime`,`DataCollection`.`imageDirectory` AS `DataCollection_imageDirectory`,`DataCollection`.`imagePrefix` AS `DataCollection_imagePrefix`,`DataCollection`.`imageSuffix` AS `DataCollection_imageSuffix`,`DataCollection`.`fileTemplate` AS `DataCollection_fileTemplate`,`DataCollection`.`wavelength` AS `DataCollection_wavelength`,`DataCollection`.`resolution` AS `DataCollection_resolution`,`DataCollection`.`detectorDistance` AS `DataCollection_detectorDistance`,`DataCollection`.`xBeam` AS `DataCollection_xBeam`,`DataCollection`.`yBeam` AS `DataCollection_yBeam`,`DataCollection`.`xBeamPix` AS `DataCollection_xBeamPix`,`DataCollection`.`yBeamPix` AS `DataCollection_yBeamPix`,`DataCollection`.`comments` AS `DataCollection_comments`,`DataCollection`.`printableForReport` AS `DataCollection_printableForReport`,`DataCollection`.`slitGapVertical` AS `DataCollection_slitGapVertical`,`DataCollection`.`slitGapHorizontal` AS `DataCollection_slitGapHorizontal`,`DataCollection`.`transmission` AS `DataCollection_transmission`,`DataCollection`.`synchrotronMode` AS `DataCollection_synchrotronMode`,`DataCollection`.`xtalSnapshotFullPath1` AS `DataCollection_xtalSnapshotFullPath1`,`DataCollection`.`xtalSnapshotFullPath2` AS `DataCollection_xtalSnapshotFullPath2`,`DataCollection`.`xtalSnapshotFullPath3` AS `DataCollection_xtalSnapshotFullPath3`,`DataCollection`.`xtalSnapshotFullPath4` AS `DataCollection_xtalSnapshotFullPath4`,`DataCollection`.`rotationAxis` AS `DataCollection_rotationAxis`,`DataCollection`.`phiStart` AS `DataCollection_phiStart`,`DataCollection`.`kappaStart` AS `DataCollection_kappaStart`,`DataCollection`.`omegaStart` AS `DataCollection_omegaStart`,`DataCollection`.`resolutionAtCorner` AS `DataCollection_resolutionAtCorner`,`DataCollection`.`detector2Theta` AS `DataCollection_detector2Theta`,`DataCollection`.`undulatorGap1` AS `DataCollection_undulatorGap1`,`DataCollection`.`undulatorGap2` AS `DataCollection_undulatorGap2`,`DataCollection`.`undulatorGap3` AS `DataCollection_undulatorGap3`,`DataCollection`.`beamSizeAtSampleX` AS `DataCollection_beamSizeAtSampleX`,`DataCollection`.`beamSizeAtSampleY` AS `DataCollection_beamSizeAtSampleY`,`DataCollection`.`centeringMethod` AS `DataCollection_centeringMethod`,`DataCollection`.`averageTemperature` AS `DataCollection_averageTemperature`,`DataCollection`.`actualCenteringPosition` AS `DataCollection_actualCenteringPosition`,`DataCollection`.`beamShape` AS `DataCollection_beamShape`,`DataCollection`.`flux` AS `DataCollection_flux`,`DataCollection`.`flux_end` AS `DataCollection_flux_end`,`DataCollection`.`totalAbsorbedDose` AS `DataCollection_totalAbsorbedDose`,`DataCollection`.`bestWilsonPlotPath` AS `DataCollection_bestWilsonPlotPath`,`DataCollection`.`imageQualityIndicatorsPlotPath` AS `DataCollection_imageQualityIndicatorsPlotPath`,`DataCollection`.`imageQualityIndicatorsCSVPath` AS `DataCollection_imageQualityIndicatorsCSVPath`,`PhasingProgramRun`.`phasingProgramRunId` AS `PhasingProgramRun_phasingProgramRunId`,`PhasingProgramRun`.`phasingCommandLine` AS `PhasingProgramRun_phasingCommandLine`,`PhasingProgramRun`.`phasingPrograms` AS `PhasingProgramRun_phasingPrograms`,`PhasingProgramRun`.`phasingStatus` AS `PhasingProgramRun_phasingStatus`,`PhasingProgramRun`.`phasingMessage` AS `PhasingProgramRun_phasingMessage`,`PhasingProgramRun`.`phasingStartTime` AS `PhasingProgramRun_phasingStartTime`,`PhasingProgramRun`.`phasingEndTime` AS `PhasingProgramRun_phasingEndTime`,`PhasingProgramRun`.`phasingEnvironment` AS `PhasingProgramRun_phasingEnvironment`,`PhasingProgramRun`.`recordTimeStamp` AS `PhasingProgramRun_recordTimeStamp`,`Protein`.`proteinId` AS `Protein_proteinId`,`BLSession`.`sessionId` AS `BLSession_sessionId`,`BLSession`.`proposalId` AS `BLSession_proposalId`,`PhasingStatistics`.`phasingStatisticsId` AS `PhasingStatistics_phasingStatisticsId`,`PhasingStatistics`.`metric` AS `PhasingStatistics_metric`,`PhasingStatistics`.`statisticsValue` AS `PhasingStatistics_statisticsValue` from (((((((((((((`DataCollection` left join `AutoProcIntegration` on((`AutoProcIntegration`.`dataCollectionId` = `DataCollection`.`dataCollectionId`))) left join `AutoProcScaling_has_Int` on((`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`))) left join `AutoProcScaling` on((`AutoProcScaling`.`autoProcScalingId` = `AutoProcScaling_has_Int`.`autoProcScalingId`))) left join `PhasingStep` on((`PhasingStep`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`))) left join `PhasingStatistics` on((`PhasingStatistics`.`phasingStepId` = `PhasingStep`.`phasingStepId`))) left join `SpaceGroup` on((`SpaceGroup`.`spaceGroupId` = `PhasingStep`.`spaceGroupId`))) left join `DataCollectionGroup` on((`DataCollectionGroup`.`dataCollectionGroupId` = `DataCollection`.`dataCollectionGroupId`))) left join `BLSession` on((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`))) left join `Proposal` on((`Proposal`.`proposalId` = `BLSession`.`proposalId`))) left join `BLSample` on((`BLSample`.`blSampleId` = `DataCollectionGroup`.`blSampleId`))) left join `Crystal` on((`Crystal`.`crystalId` = `BLSample`.`crystalId`))) left join `Protein` on((`Crystal`.`proteinId` = `Protein`.`proteinId`))) left join `PhasingProgramRun` on((`PhasingProgramRun`.`phasingProgramRunId` = `PhasingStep`.`programRunId`)));

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_sample` AS select `d`.`proposalId` AS `proposalId`,`d`.`shippingId` AS `shippingId`,`d`.`dewarId` AS `dewarId`,`c`.`containerId` AS `containerId`,`bls`.`blSampleId` AS `blSampleId`,`d`.`proposalCode` AS `proposalCode`,`d`.`proposalNumber` AS `proposalNumber`,`d`.`creationDate` AS `creationDate`,`d`.`shippingType` AS `shippingType`,`d`.`barCode` AS `barCode`,`d`.`shippingStatus` AS `shippingStatus` from ((`BLSample` `bls` join `Container` `c` on((`c`.`containerId` = `bls`.`containerId`))) join `v_dewar` `d` on((`d`.`dewarId` = `c`.`dewarId`)));

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_sampleByWeek` AS select substr(`w`.`num`,6) AS `Week`,if((`w`.`num` <= date_format(now(),'%Y-%v')),count(if((`bls`.`proposalCode` is not null),1,NULL)),NULL) AS `Samples` from (`v_week` `w` left join `v_sample` `bls` on((`w`.`num` = date_format(`bls`.`creationDate`,'%Y-%v')))) group by substr(`w`.`num`,6) order by substr(`w`.`num`,6);

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_saxs_datacollection` AS select `Subtraction`.`subtractionId` AS `Subtraction_subtractionId`,`MeasurementToDataCollection`.`dataCollectionId` AS `MeasurementToDataCollection_dataCollectionId`,`MeasurementToDataCollection`.`dataCollectionOrder` AS `MeasurementToDataCollection_dataCollectionOrder`,`MeasurementToDataCollection`.`measurementToDataCollectionId` AS `MeasurementToDataCollection_measurementToDataCollectionId`,`Specimen`.`specimenId` AS `Specimen_specimenId`,`Measurement`.`code` AS `Measurement_code`,`Measurement`.`measurementId` AS `Measurement_measurementId`,`Buffer`.`bufferId` AS `Buffer_bufferId`,`Buffer`.`proposalId` AS `Buffer_proposalId`,`Buffer`.`safetyLevelId` AS `Buffer_safetyLevelId`,`Buffer`.`name` AS `Buffer_name`,`Buffer`.`acronym` AS `Buffer_acronym`,`Buffer`.`pH` AS `Buffer_pH`,`Buffer`.`composition` AS `Buffer_composition`,`Buffer`.`comments` AS `Buffer_comments`,`Macromolecule`.`macromoleculeId` AS `Macromolecule_macromoleculeId`,`Macromolecule`.`proposalId` AS `Macromolecule_proposalId`,`Macromolecule`.`safetyLevelId` AS `Macromolecule_safetyLevelId`,`Macromolecule`.`name` AS `Macromolecule_name`,`Macromolecule`.`acronym` AS `Macromolecule_acronym`,`Macromolecule`.`extintionCoefficient` AS `Macromolecule_extintionCoefficient`,`Macromolecule`.`molecularMass` AS `Macromolecule_molecularMass`,`Macromolecule`.`sequence` AS `Macromolecule_sequence`,`Macromolecule`.`contactsDescriptionFilePath` AS `Macromolecule_contactsDescriptionFilePath`,`Macromolecule`.`symmetry` AS `Macromolecule_symmetry`,`Macromolecule`.`comments` AS `Macromolecule_comments`,`Macromolecule`.`refractiveIndex` AS `Macromolecule_refractiveIndex`,`Macromolecule`.`solventViscosity` AS `Macromolecule_solventViscosity`,`Macromolecule`.`creationDate` AS `Macromolecule_creationDate`,`Specimen`.`experimentId` AS `Specimen_experimentId`,`Specimen`.`bufferId` AS `Specimen_bufferId`,`Specimen`.`samplePlatePositionId` AS `Specimen_samplePlatePositionId`,`Specimen`.`safetyLevelId` AS `Specimen_safetyLevelId`,`Specimen`.`stockSolutionId` AS `Specimen_stockSolutionId`,`Specimen`.`code` AS `Specimen_code`,`Specimen`.`concentration` AS `Specimen_concentration`,`Specimen`.`volume` AS `Specimen_volume`,`Specimen`.`comments` AS `Specimen_comments`,`SamplePlatePosition`.`samplePlatePositionId` AS `SamplePlatePosition_samplePlatePositionId`,`SamplePlatePosition`.`samplePlateId` AS `SamplePlatePosition_samplePlateId`,`SamplePlatePosition`.`rowNumber` AS `SamplePlatePosition_rowNumber`,`SamplePlatePosition`.`columnNumber` AS `SamplePlatePosition_columnNumber`,`SamplePlatePosition`.`volume` AS `SamplePlatePosition_volume`,`SamplePlate`.`samplePlateId` AS `samplePlateId`,`SamplePlate`.`experimentId` AS `experimentId`,`SamplePlate`.`plateGroupId` AS `plateGroupId`,`SamplePlate`.`plateTypeId` AS `plateTypeId`,`SamplePlate`.`instructionSetId` AS `instructionSetId`,`SamplePlate`.`boxId` AS `SamplePlate_boxId`,`SamplePlate`.`name` AS `SamplePlate_name`,`SamplePlate`.`slotPositionRow` AS `SamplePlate_slotPositionRow`,`SamplePlate`.`slotPositionColumn` AS `SamplePlate_slotPositionColumn`,`SamplePlate`.`storageTemperature` AS `SamplePlate_storageTemperature`,`Experiment`.`experimentId` AS `Experiment_experimentId`,`Experiment`.`sessionId` AS `Experiment_sessionId`,`Experiment`.`proposalId` AS `Experiment_proposalId`,`Experiment`.`name` AS `Experiment_name`,`Experiment`.`creationDate` AS `Experiment_creationDate`,`Experiment`.`experimentType` AS `Experiment_experimentType`,`Experiment`.`sourceFilePath` AS `Experiment_sourceFilePath`,`Experiment`.`dataAcquisitionFilePath` AS `Experiment_dataAcquisitionFilePath`,`Experiment`.`status` AS `Experiment_status`,`Experiment`.`comments` AS `Experiment_comments`,`Measurement`.`priorityLevelId` AS `Measurement_priorityLevelId`,`Measurement`.`exposureTemperature` AS `Measurement_exposureTemperature`,`Measurement`.`viscosity` AS `Measurement_viscosity`,`Measurement`.`flow` AS `Measurement_flow`,`Measurement`.`extraFlowTime` AS `Measurement_extraFlowTime`,`Measurement`.`volumeToLoad` AS `Measurement_volumeToLoad`,`Measurement`.`waitTime` AS `Measurement_waitTime`,`Measurement`.`transmission` AS `Measurement_transmission`,`Measurement`.`comments` AS `Measurement_comments`,`Measurement`.`imageDirectory` AS `Measurement_imageDirectory`,`Run`.`runId` AS `Run_runId`,`Run`.`timePerFrame` AS `Run_timePerFrame`,`Run`.`timeStart` AS `Run_timeStart`,`Run`.`timeEnd` AS `Run_timeEnd`,`Run`.`storageTemperature` AS `Run_storageTemperature`,`Run`.`exposureTemperature` AS `Run_exposureTemperature`,`Run`.`spectrophotometer` AS `Run_spectrophotometer`,`Run`.`energy` AS `Run_energy`,`Run`.`creationDate` AS `Run_creationDate`,`Run`.`frameAverage` AS `Run_frameAverage`,`Run`.`frameCount` AS `Run_frameCount`,`Run`.`transmission` AS `Run_transmission`,`Run`.`beamCenterX` AS `Run_beamCenterX`,`Run`.`beamCenterY` AS `Run_beamCenterY`,`Run`.`pixelSizeX` AS `Run_pixelSizeX`,`Run`.`pixelSizeY` AS `Run_pixelSizeY`,`Run`.`radiationRelative` AS `Run_radiationRelative`,`Run`.`radiationAbsolute` AS `Run_radiationAbsolute`,`Run`.`normalization` AS `Run_normalization`,`Merge`.`mergeId` AS `Merge_mergeId`,`Merge`.`measurementId` AS `Merge_measurementId`,`Merge`.`frameListId` AS `Merge_frameListId`,`Merge`.`discardedFrameNameList` AS `Merge_discardedFrameNameList`,`Merge`.`averageFilePath` AS `Merge_averageFilePath`,`Merge`.`framesCount` AS `Merge_framesCount`,`Merge`.`framesMerge` AS `Merge_framesMerge`,`Merge`.`creationDate` AS `Merge_creationDate`,`Subtraction`.`dataCollectionId` AS `Subtraction_dataCollectionId`,`Subtraction`.`rg` AS `Subtraction_rg`,`Subtraction`.`rgStdev` AS `Subtraction_rgStdev`,`Subtraction`.`I0` AS `Subtraction_I0`,`Subtraction`.`I0Stdev` AS `Subtraction_I0Stdev`,`Subtraction`.`firstPointUsed` AS `Subtraction_firstPointUsed`,`Subtraction`.`lastPointUsed` AS `Subtraction_lastPointUsed`,`Subtraction`.`quality` AS `Subtraction_quality`,`Subtraction`.`isagregated` AS `Subtraction_isagregated`,`Subtraction`.`concentration` AS `Subtraction_concentration`,`Subtraction`.`gnomFilePath` AS `Subtraction_gnomFilePath`,`Subtraction`.`rgGuinier` AS `Subtraction_rgGuinier`,`Subtraction`.`rgGnom` AS `Subtraction_rgGnom`,`Subtraction`.`dmax` AS `Subtraction_dmax`,`Subtraction`.`total` AS `Subtraction_total`,`Subtraction`.`volume` AS `Subtraction_volume`,`Subtraction`.`creationTime` AS `Subtraction_creationTime`,`Subtraction`.`kratkyFilePath` AS `Subtraction_kratkyFilePath`,`Subtraction`.`scatteringFilePath` AS `Subtraction_scatteringFilePath`,`Subtraction`.`guinierFilePath` AS `Subtraction_guinierFilePath`,`Subtraction`.`substractedFilePath` AS `Subtraction_substractedFilePath`,`Subtraction`.`gnomFilePathOutput` AS `Subtraction_gnomFilePathOutput`,`Subtraction`.`sampleOneDimensionalFiles` AS `Subtraction_sampleOneDimensionalFiles`,`Subtraction`.`bufferOnedimensionalFiles` AS `Subtraction_bufferOnedimensionalFiles`,`Subtraction`.`sampleAverageFilePath` AS `Subtraction_sampleAverageFilePath`,`Subtraction`.`bufferAverageFilePath` AS `Subtraction_bufferAverageFilePath` from ((((((((((`Experiment` left join `Specimen` on((`Specimen`.`experimentId` = `Experiment`.`experimentId`))) left join `Measurement` on((`Measurement`.`specimenId` = `Specimen`.`specimenId`))) left join `MeasurementToDataCollection` on((`MeasurementToDataCollection`.`measurementId` = `Measurement`.`measurementId`))) left join `Macromolecule` on((`Macromolecule`.`macromoleculeId` = `Specimen`.`macromoleculeId`))) left join `Buffer` on((`Buffer`.`bufferId` = `Specimen`.`bufferId`))) left join `SamplePlatePosition` on((`SamplePlatePosition`.`samplePlatePositionId` = `Specimen`.`samplePlatePositionId`))) left join `SamplePlate` on((`SamplePlate`.`samplePlateId` = `SamplePlatePosition`.`samplePlateId`))) left join `Run` on((`Run`.`runId` = `Measurement`.`runId`))) left join `Merge` on((`Merge`.`measurementId` = `Measurement`.`measurementId`))) left join `Subtraction` on((`Subtraction`.`dataCollectionId` = `MeasurementToDataCollection`.`dataCollectionId`)));

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_session` AS select `BLSession`.`sessionId` AS `sessionId`,`BLSession`.`expSessionPk` AS `expSessionPk`,`BLSession`.`beamLineSetupId` AS `beamLineSetupId`,`BLSession`.`proposalId` AS `proposalId`,`BLSession`.`projectCode` AS `projectCode`,`BLSession`.`startDate` AS `BLSession_startDate`,`BLSession`.`endDate` AS `BLSession_endDate`,`BLSession`.`beamLineName` AS `beamLineName`,`BLSession`.`scheduled` AS `scheduled`,`BLSession`.`nbShifts` AS `nbShifts`,`BLSession`.`comments` AS `comments`,`BLSession`.`beamLineOperator` AS `beamLineOperator`,`BLSession`.`visit_number` AS `visit_number`,`BLSession`.`bltimeStamp` AS `bltimeStamp`,`BLSession`.`usedFlag` AS `usedFlag`,`BLSession`.`sessionTitle` AS `sessionTitle`,`BLSession`.`structureDeterminations` AS `structureDeterminations`,`BLSession`.`dewarTransport` AS `dewarTransport`,`BLSession`.`databackupFrance` AS `databackupFrance`,`BLSession`.`databackupEurope` AS `databackupEurope`,`BLSession`.`operatorSiteNumber` AS `operatorSiteNumber`,`BLSession`.`lastUpdate` AS `BLSession_lastUpdate`,`BLSession`.`protectedData` AS `BLSession_protectedData`,`Proposal`.`title` AS `Proposal_title`,`Proposal`.`proposalCode` AS `Proposal_proposalCode`,`Proposal`.`proposalNumber` AS `Proposal_ProposalNumber`,`Proposal`.`proposalType` AS `Proposal_ProposalType`,`Person`.`personId` AS `Person_personId`,`Person`.`familyName` AS `Person_familyName`,`Person`.`givenName` AS `Person_givenName`,`Person`.`emailAddress` AS `Person_emailAddress` from ((`BLSession` left join `Proposal` on((`Proposal`.`proposalId` = `BLSession`.`proposalId`))) left join `Person` on((`Person`.`personId` = `Proposal`.`personId`)));

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_tracking_shipment_history` AS select `Dewar`.`dewarId` AS `Dewar_dewarId`,`Dewar`.`code` AS `Dewar_code`,`Dewar`.`comments` AS `Dewar_comments`,`Dewar`.`dewarStatus` AS `Dewar_dewarStatus`,`Dewar`.`barCode` AS `Dewar_barCode`,`Dewar`.`firstExperimentId` AS `Dewar_firstExperimentId`,`Dewar`.`trackingNumberToSynchrotron` AS `Dewar_trackingNumberToSynchrotron`,`Dewar`.`trackingNumberFromSynchrotron` AS `Dewar_trackingNumberFromSynchrotron`,`Dewar`.`type` AS `Dewar_type`,`Shipping`.`shippingId` AS `Shipping_shippingId`,`Shipping`.`proposalId` AS `Shipping_proposalId`,`Shipping`.`shippingName` AS `Shipping_shippingName`,`Shipping`.`deliveryAgent_agentName` AS `deliveryAgent_agentName`,`Shipping`.`deliveryAgent_shippingDate` AS `Shipping_deliveryAgent_shippingDate`,`Shipping`.`deliveryAgent_deliveryDate` AS `Shipping_deliveryAgent_deliveryDate`,`Shipping`.`shippingStatus` AS `Shipping_shippingStatus`,`Shipping`.`returnCourier` AS `Shipping_returnCourier`,`Shipping`.`dateOfShippingToUser` AS `Shipping_dateOfShippingToUser`,`DewarTransportHistory`.`DewarTransportHistoryId` AS `DewarTransportHistory_DewarTransportHistoryId`,`DewarTransportHistory`.`dewarStatus` AS `DewarTransportHistory_dewarStatus`,`DewarTransportHistory`.`storageLocation` AS `DewarTransportHistory_storageLocation`,`DewarTransportHistory`.`arrivalDate` AS `DewarTransportHistory_arrivalDate` from ((`Shipping` left join `Dewar` on((`Dewar`.`shippingId` = `Shipping`.`shippingId`))) left join `DewarTransportHistory` on((`DewarTransportHistory`.`dewarId` = `Dewar`.`dewarId`)));

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_week` AS select date_format((now() - interval 52 week),_utf8'%x-%v') AS `num` union select date_format((now() - interval 51 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 51 WEEK),'%x-%v')` union select date_format((now() - interval 50 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 50 WEEK),'%x-%v')` union select date_format((now() - interval 49 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 49 WEEK),'%x-%v')` union select date_format((now() - interval 48 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 48 WEEK),'%x-%v')` union select date_format((now() - interval 47 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 47 WEEK),'%x-%v')` union select date_format((now() - interval 46 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 46 WEEK),'%x-%v')` union select date_format((now() - interval 45 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 45 WEEK),'%x-%v')` union select date_format((now() - interval 44 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 44 WEEK),'%x-%v')` union select date_format((now() - interval 43 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 43 WEEK),'%x-%v')` union select date_format((now() - interval 42 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 42 WEEK),'%x-%v')` union select date_format((now() - interval 41 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 41 WEEK),'%x-%v')` union select date_format((now() - interval 40 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 40 WEEK),'%x-%v')` union select date_format((now() - interval 39 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 39 WEEK),'%x-%v')` union select date_format((now() - interval 38 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 38 WEEK),'%x-%v')` union select date_format((now() - interval 37 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 37 WEEK),'%x-%v')` union select date_format((now() - interval 36 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 36 WEEK),'%x-%v')` union select date_format((now() - interval 35 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 35 WEEK),'%x-%v')` union select date_format((now() - interval 34 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 34 WEEK),'%x-%v')` union select date_format((now() - interval 33 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 33 WEEK),'%x-%v')` union select date_format((now() - interval 32 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 32 WEEK),'%x-%v')` union select date_format((now() - interval 31 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 31 WEEK),'%x-%v')` union select date_format((now() - interval 30 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 30 WEEK),'%x-%v')` union select date_format((now() - interval 29 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 29 WEEK),'%x-%v')` union select date_format((now() - interval 28 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 28 WEEK),'%x-%v')` union select date_format((now() - interval 27 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 27 WEEK),'%x-%v')` union select date_format((now() - interval 26 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 26 WEEK),'%x-%v')` union select date_format((now() - interval 25 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 25 WEEK),'%x-%v')` union select date_format((now() - interval 24 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 24 WEEK),'%x-%v')` union select date_format((now() - interval 23 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 23 WEEK),'%x-%v')` union select date_format((now() - interval 22 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 22 WEEK),'%x-%v')` union select date_format((now() - interval 21 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 21 WEEK),'%x-%v')` union select date_format((now() - interval 20 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 20 WEEK),'%x-%v')` union select date_format((now() - interval 19 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 19 WEEK),'%x-%v')` union select date_format((now() - interval 18 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 18 WEEK),'%x-%v')` union select date_format((now() - interval 17 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 17 WEEK),'%x-%v')` union select date_format((now() - interval 16 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 16 WEEK),'%x-%v')` union select date_format((now() - interval 15 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 15 WEEK),'%x-%v')` union select date_format((now() - interval 14 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 14 WEEK),'%x-%v')` union select date_format((now() - interval 13 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 13 WEEK),'%x-%v')` union select date_format((now() - interval 12 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 12 WEEK),'%x-%v')` union select date_format((now() - interval 11 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 11 WEEK),'%x-%v')` union select date_format((now() - interval 10 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 10 WEEK),'%x-%v')` union select date_format((now() - interval 9 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 9 WEEK),'%x-%v')` union select date_format((now() - interval 8 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 8 WEEK),'%x-%v')` union select date_format((now() - interval 7 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 7 WEEK),'%x-%v')` union select date_format((now() - interval 6 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 6 WEEK),'%x-%v')` union select date_format((now() - interval 5 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 5 WEEK),'%x-%v')` union select date_format((now() - interval 4 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 4 WEEK),'%x-%v')` union select date_format((now() - interval 3 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 3 WEEK),'%x-%v')` union select date_format((now() - interval 2 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 2 WEEK),'%x-%v')` union select date_format((now() - interval 1 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 1 WEEK),'%x-%v')` union select date_format((now() - interval 0 week),_utf8'%x-%v') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 0 WEEK),'%x-%v')`;

CREATE ALGORITHM=UNDEFINED DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_weekDay` AS select date_format((now() - interval 6 day),_utf8'%Y-%m-%d') AS `day` union select date_format((now() - interval 5 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 5 DAY),'%Y-%m-%d')` union select date_format((now() - interval 4 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 4 DAY),'%Y-%m-%d')` union select date_format((now() - interval 3 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 3 DAY),'%Y-%m-%d')` union select date_format((now() - interval 2 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 2 DAY),'%Y-%m-%d')` union select date_format((now() - interval 1 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 1 DAY),'%Y-%m-%d')` union select date_format((now() - interval 0 day),_utf8'%Y-%m-%d') AS `DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 0 DAY),'%Y-%m-%d')`;

CREATE ALGORITHM=MERGE DEFINER=`pxadmin`@`%` SQL SECURITY DEFINER VIEW `v_xfeFluorescenceSpectrum` AS select `XFEFluorescenceSpectrum`.`xfeFluorescenceSpectrumId` AS `xfeFluorescenceSpectrumId`,`XFEFluorescenceSpectrum`.`sessionId` AS `sessionId`,`XFEFluorescenceSpectrum`.`blSampleId` AS `blSampleId`,`XFEFluorescenceSpectrum`.`fittedDataFileFullPath` AS `fittedDataFileFullPath`,`XFEFluorescenceSpectrum`.`scanFileFullPath` AS `scanFileFullPath`,`XFEFluorescenceSpectrum`.`jpegScanFileFullPath` AS `jpegScanFileFullPath`,`XFEFluorescenceSpectrum`.`startTime` AS `startTime`,`XFEFluorescenceSpectrum`.`endTime` AS `endTime`,`XFEFluorescenceSpectrum`.`filename` AS `filename`,`XFEFluorescenceSpectrum`.`energy` AS `energy`,`XFEFluorescenceSpectrum`.`exposureTime` AS `exposureTime`,`XFEFluorescenceSpectrum`.`beamTransmission` AS `beamTransmission`,`XFEFluorescenceSpectrum`.`annotatedPymcaXfeSpectrum` AS `annotatedPymcaXfeSpectrum`,`XFEFluorescenceSpectrum`.`beamSizeVertical` AS `beamSizeVertical`,`XFEFluorescenceSpectrum`.`beamSizeHorizontal` AS `beamSizeHorizontal`,`XFEFluorescenceSpectrum`.`crystalClass` AS `crystalClass`,`XFEFluorescenceSpectrum`.`comments` AS `comments`,`XFEFluorescenceSpectrum`.`flux` AS `flux`,`XFEFluorescenceSpectrum`.`flux_end` AS `flux_end`,`XFEFluorescenceSpectrum`.`workingDirectory` AS `workingDirectory`,`BLSample`.`blSampleId` AS `BLSample_sampleId`,`BLSession`.`proposalId` AS `BLSession_proposalId` from ((`XFEFluorescenceSpectrum` left join `BLSample` on((`BLSample`.`blSampleId` = `XFEFluorescenceSpectrum`.`blSampleId`))) left join `BLSession` on((`BLSession`.`sessionId` = `XFEFluorescenceSpectrum`.`sessionId`)));



