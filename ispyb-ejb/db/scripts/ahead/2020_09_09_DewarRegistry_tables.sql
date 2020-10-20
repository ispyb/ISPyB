INSERT IGNORE INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2020_09_09_DewarRegistry_tables.sql', 'ONGOING');

CREATE TABLE `DewarRegistry` (
  `dewarRegistryId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `facilityCode` varchar(20) NOT NULL,
  `proposalId` int(11) unsigned DEFAULT NULL,
  `labContactId` int(11) unsigned DEFAULT NULL,
  `purchaseDate` datetime DEFAULT NULL,
  `bltimestamp` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`dewarRegistryId`),
  UNIQUE KEY `facilityCode` (`facilityCode`),
  KEY `DewarRegistry_ibfk_1` (`proposalId`),
  KEY `DewarRegistry_ibfk_2` (`labContactId`),
  CONSTRAINT `DewarRegistry_ibfk_1` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `DewarRegistry_ibfk_2` FOREIGN KEY (`labContactId`) REFERENCES `LabContact` (`labContactId`) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE `DewarRegistry_has_Proposal` (
  `dewarRegistryHasProposalId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `dewarRegistryId` int(11) unsigned DEFAULT NULL,
  `proposalId` int(10) unsigned DEFAULT NULL,
  `personId` int(10) unsigned DEFAULT NULL COMMENT 'Person registering the dewar',
  `recordTimestamp` datetime DEFAULT current_timestamp(),
  `labContactId` int(11) unsigned DEFAULT NULL COMMENT 'Owner of the dewar',
  PRIMARY KEY (`dewarRegistryHasProposalId`),
  UNIQUE KEY `dewarRegistryId` (`dewarRegistryId`,`proposalId`),
  KEY `DewarRegistry_has_Proposal_ibfk2` (`proposalId`),
  KEY `DewarRegistry_has_Proposal_ibfk3` (`personId`),
  KEY `DewarRegistry_has_Proposal_ibfk4` (`labContactId`),
  CONSTRAINT `DewarRegistry_has_Proposal_ibfk1` FOREIGN KEY (`dewarRegistryId`) REFERENCES `DewarRegistry` (`dewarRegistryId`),
  CONSTRAINT `DewarRegistry_has_Proposal_ibfk2` FOREIGN KEY (`proposalId`) REFERENCES `Proposal` (`proposalId`),
  CONSTRAINT `DewarRegistry_has_Proposal_ibfk3` FOREIGN KEY (`personId`) REFERENCES `Person` (`personId`),
  CONSTRAINT `DewarRegistry_has_Proposal_ibfk4` FOREIGN KEY (`labContactId`) REFERENCES `LabContact` (`labContactId`) ON DELETE NO ACTION ON UPDATE CASCADE
);

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2020_09_09_DewarRegistry_tables.sql';
