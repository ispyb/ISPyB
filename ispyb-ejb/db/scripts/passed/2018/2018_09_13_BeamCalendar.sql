INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2018_09_13_BeamCalendar.sql', 'ONGOING');

CREATE TABLE BeamCalendar (
  beamCalendarId int(10) unsigned auto_increment,
  run varchar(7) NOT NULL,
  beamStatus varchar(24) NOT NULL,
  startDate datetime NOT NULL,
  endDate dateTime NOT NULL,
  PRIMARY KEY (beamCalendarId)
);

ALTER TABLE BLSession 
  ADD COLUMN beamCalendarId int(10) unsigned after `proposalId`, 
  ADD constraint BLSession_ibfk_3 FOREIGN KEY (beamCalendarId) REFERENCES BeamCalendar (beamCalendarId);

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2018_09_13_BeamCalendar.sql';
