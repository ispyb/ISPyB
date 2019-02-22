INSERT IGNORE INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2018_12_04_EnergyScan_and_XFEFluorescenceSpectrum_add_axisPosition.sql', 'ONGOING');

ALTER TABLE EnergyScan
  ADD axisPosition float AFTER exposureTime;

ALTER TABLE XFEFluorescenceSpectrum
  ADD axisPosition float AFTER exposureTime;

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2018_12_04_EnergyScan_and_XFEFluorescenceSpectrum_add_axisPosition.sql';
