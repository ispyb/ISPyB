INSERT IGNORE INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2018_12_17_PhasingStatistics_new_metrics.sql', 'ONGOING');

ALTER TABLE `pydb`.`PhasingStatistics` 
CHANGE COLUMN `metric` `metric` ENUM('Rcullis', 'Average Fragment Length', 'Chain Count', 'Residues Count', 'CC', 'PhasingPower', 'FOM', '<d"/sig>', 'Best CC', 'CC(1/2)', 'Weak CC', 'CFOM', 'Pseudo_free_CC', 'CC of partial model', 'Start R-work', 'Start R-free', 'Final R-work', 'Final R-free') NULL DEFAULT NULL COMMENT 'metric' ;

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2018_12_17_PhasingStatistics_new_metrics.sql';
