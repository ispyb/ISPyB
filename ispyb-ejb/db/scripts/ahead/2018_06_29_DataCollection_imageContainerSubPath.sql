INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2018_06_29_DataCollection_imageContainerSubPath.sql', 'ONGOING');

ALTER TABLE DataCollection ADD imageContainerSubPath VARCHAR(255) COMMENT 'Internal path of a HDF5 file pointing to the data for this data collection' AFTER imageSuffix;

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2018_06_29_DataCollection_imageContainerSubPath.sql';
