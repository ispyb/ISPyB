USE pydb;

INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2018_02_13_BLSample_subLocation.sql', 'ONGOING');

ALTER TABLE BLSample ADD subLocation SMALLINT UNSIGNED COMMENT 'Indicates the sample\'s location on a multi-sample pin, where 1 is closest to the pin base';

UPDATE SchemaStatus SET schemaStatus = 'DONE' where scriptName = '2018_02_13_BLSample_subLocation.sql';
