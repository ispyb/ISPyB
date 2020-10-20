INSERT IGNORE INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2020_09_09_DiffractionPlan_userPath.sql', 'ONGOING');

ALTER TABLE DiffractionPlan 
  ADD userPath varchar(100) 
    COMMENT 'User-specified relative "root" path inside the session directory to be used for holding collected data';

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2020_09_09_DiffractionPlan_userPath.sql';
