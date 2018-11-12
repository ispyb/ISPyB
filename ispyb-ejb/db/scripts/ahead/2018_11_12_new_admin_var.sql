USE `pydb`;

INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2018_11_12_new_admin_var.sql', 'ONGOING');

INSERT INTO `AdminVar` (`varId`, `name`, `value`) VALUES ('6', 'updateProposalNbDaysWindow', '30');

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2018_11_12_new_admin_var.sql';
