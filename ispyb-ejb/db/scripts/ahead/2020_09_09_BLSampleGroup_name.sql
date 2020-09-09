INSERT IGNORE INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2020_09_09_BLSampleGroup_name.sql', 'ONGOING');

ALTER TABLE BLSampleGroup ADD name varchar(100) COMMENT 'Human-readable name';

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2020_09_09_BLSampleGroup_name.sql';
