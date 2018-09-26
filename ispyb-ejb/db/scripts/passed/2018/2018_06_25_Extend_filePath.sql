use pydb;

INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2018_08_25_Extend_filePath.sql', 'ONGOING');


ALTER TABLE `pydb`.`CTF` 
CHANGE COLUMN `spectraImageThumbnailFullPath` `spectraImageThumbnailFullPath` VARCHAR(512) NULL DEFAULT NULL ;

ALTER TABLE `pydb`.`CTF` 
CHANGE COLUMN `spectraImageFullPath` `spectraImageFullPath` VARCHAR(512) NULL DEFAULT NULL ,
CHANGE COLUMN `logFilePath` `logFilePath` VARCHAR(512) NULL DEFAULT NULL ;

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2018_08_25_Extend_filePath.sql';