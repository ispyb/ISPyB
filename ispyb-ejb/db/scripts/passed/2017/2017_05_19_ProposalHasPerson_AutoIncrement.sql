USE pydb;
insert into SchemaStatus (scriptName, schemaStatus) values ('2017_05_19_ProposalHasPerson_AutoIncrement.sql','ONGOING');

ALTER TABLE ProposalHasPerson MODIFY COLUMN proposalHasPersonId int(10) unsigned NOT NULL AUTO_INCREMENT;

update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_05_19_ProposalHasPerson_AutoIncrement.sql';
commit;