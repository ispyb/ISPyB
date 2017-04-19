USE `pydb`;

insert into SchemaStatus (scriptName, schemaStatus) values ('2017_02_22_Bug_add_siteId.sql','ONGOING');

ALTER TABLE Login ADD siteId varchar(45) NULL AFTER roles ;

update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_02_22_Bug_add_siteId.sql';
