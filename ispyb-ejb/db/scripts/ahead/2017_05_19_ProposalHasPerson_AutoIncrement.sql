USE pydb;

ALTER TABLE ProposalHasPerson MODIFY COLUMN proposalHasPersonId int(10) unsigned NOT NULL AUTO_INCREMENT;

commit;