use pydb;
INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2018_05_31_ScreeningOutput_alignmentSuccess.sql', 'ONGOING');

ALTER TABLE ScreeningOutput 
  ADD alignmentSuccess boolean NOT NULL DEFAULT 0;

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2018_05_31_ScreeningOutput_alignmentSuccess.sql';
