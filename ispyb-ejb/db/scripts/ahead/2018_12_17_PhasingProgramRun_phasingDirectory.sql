INSERT IGNORE INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2018_12_17_PhasingProgramRun_phasingDirectory.sql', 'ONGOING');

ALTER TABLE `pydb`.`PhasingProgramRun` 
ADD COLUMN `phasingDirectory` VARCHAR(255) NULL COMMENT 'Directory of execution' AFTER `phasingEnvironment`;

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2018_12_17_PhasingProgramRun_phasingDirectory.sql';
