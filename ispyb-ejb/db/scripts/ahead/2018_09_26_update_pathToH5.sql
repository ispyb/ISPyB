USE pydb;


INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2018_09_26_update_pathToH5.sql','ONGOING');

ALTER TABLE Measurement ADD pathToH5 VARCHAR(512);

update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2018_09_26_update_pathToH5.sql';
