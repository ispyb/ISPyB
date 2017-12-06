use pydb;
insert into SchemaStatus (scriptName, schemaStatus) values ('2017_12_05_DataCollectionFileAttachment_fileType.sql','ONGOING');

ALTER TABLE DataCollectionFileAttachment MODIFY fileType enum('snapshot', 'log', 'xy', 'recip') 
COMMENT 'snapshot: image file, usually of the sample. 
log: a text file with logging info. 
xy: x and y data in text format. 
recip: a compressed csv file with reciprocal space coordinates.';

update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_12_05_DataCollectionFileAttachment_fileType.sql';
