USE `pydb`;

INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2018_10_26_new_admin_var.sql', 'ONGOING');

INSERT INTO `AdminVar` (`varId`, `name`, `value`) VALUES ('5', 'automaticUpdateNbDaysWindow', '2');

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2018_10_26_new_admin_var.sql';
