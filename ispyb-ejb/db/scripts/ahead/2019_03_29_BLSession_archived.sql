INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2019_03_29_BLSession_archived.sql', 'ONGOING');

ALTER TABLE BLSession
    ADD archived boolean DEFAULT False 
        COMMENT 'The data for the session is archived and no longer available on disk';

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2019_03_29_BLSession_archived.sql';
