use pydb;
INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2018_06_28_ChangeForeignKeyBLSampleImage.sql', 'ONGOING');

ALTER TABLE `BLSampleImage` DROP FOREIGN KEY `BLSampleImage_fk1`;
ALTER TABLE `BLSampleImage` ADD  CONSTRAINT `BLSampleImage_fk1` FOREIGN KEY (`blSampleId`) REFERENCES `BLSample`(`blSampleId`) ON DELETE CASCADE ON UPDATE CASCADE;

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2018_06_28_ChangeForeignKeyBLSampleImage.sql';
