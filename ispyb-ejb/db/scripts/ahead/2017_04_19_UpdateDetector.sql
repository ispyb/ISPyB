insert into SchemaStatus (scriptName, schemaStatus) values ('2017_04_19_UpdateDetector.sql','ONGOING');

ALTER TABLE Detector MODIFY detectorSerialNumber varchar(30);
CREATE UNIQUE INDEX Detector_ibuk1 ON Detector (detectorSerialNumber);

update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_04_19_UpdateDetector.sql';

