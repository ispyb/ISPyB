USE `pydb`;

INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2018_06_28_Fix_month_name_scripts.sql', 'ONGOING');


UPDATE `pydb`.`SchemaStatus` SET `scriptName`='2018_06_25_Extend_filePath.sql' WHERE `scriptName`='2018_08_25_Extend_filePath.sql';
UPDATE `pydb`.`SchemaStatus` SET `scriptName`='2018_07_19_em_stats.sql' WHERE `scriptName`='2018_07_19_em_stats.sql';

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2018_06_28_Fix_month_name_scripts.sql';