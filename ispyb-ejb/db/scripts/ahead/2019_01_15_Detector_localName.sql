INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2019_01_15_Detector_localName.sql', 'ONGOING');

ALTER TABLE Detector
  ADD localName varchar(40) COMMENT 'Colloquial name for the detector', ALGORITHM=INSTANT;

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2019_01_15_Detector_localName.sql';
