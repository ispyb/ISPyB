
insert into SchemaStatus (scriptName, schemaStatus) values ('2017_07_12_remove_0000_timestamp.sql','ONGOING');

UPDATE `pydb`.`Proposal` SET `bltimeStamp` = '2001-01-01 00:00:00' WHERE bltimeStamp = '0000-00-00 00:00:00'; 

update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_07_12_remove_0000_timestamp.sql';  
