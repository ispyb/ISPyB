use pydb;

ALTER TABLE `Session_has_Person` CHANGE `role` `role` ENUM('Local Contact','Local Contact 2','Staff','Team Leader','Co-Investigator','Principal Investigator','Alternate Contact') CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL


SET foreign_key_checks = 0;

ALTER TABLE `Workflow` DROP FOREIGN KEY `fk_Workflow_1`;
ALTER TABLE `Workflow` DROP proposalId;

SET foreign_key_checks = 1;

