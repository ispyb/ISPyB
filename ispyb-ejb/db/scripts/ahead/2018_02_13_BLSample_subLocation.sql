-- pt-online-schema-change --execute --alter-foreign-keys-method auto --alter "ADD subLocation SMALLINT UNSIGNED COMMENT 'Indicates the sample\'s location on a multi-sample pin, where 1 is closest to the pin base'" D=ispyb,t=BLSample,h=localhost

INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('20180213_BLSample_subLocation.sql', 'ONGOING');

ALTER TABLE BLSample ADD subLocation SMALLINT UNSIGNED COMMENT 'Indicates the sample\'s location on a multi-sample pin, where 1 is closest to the pin base';

UPDATE SchemaStatus SET schemaStatus = 'DONE' where scriptName = '20180213_BLSample_subLocation.sql';