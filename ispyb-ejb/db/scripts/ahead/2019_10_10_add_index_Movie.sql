INSERT IGNORE INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2019_10_10_Add-index-movie.sql', 'ONGOING');

ALTER TABLE `pydb`.`Movie` ADD INDEX `movieFullPath_idx` (`movieFullPath`);

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2019_10_10_Add-index-movie.sql';
