USE pydb;
INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2019_07_24_Add_ProposalId_to_Structure.sql','ONGOING');

ALTER TABLE `pydb`.`Structure` 
ADD COLUMN `proposalId` INT(10) UNSIGNED NULL AFTER `groupName`,
ADD INDEX `StructureToProposal_idx` (`proposalId` ASC);
ALTER TABLE `pydb`.`Structure` 
ADD CONSTRAINT `StructureToProposal`
  FOREIGN KEY (`proposalId`)
  REFERENCES `pydb`.`Proposal` (`proposalId`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2019_07_24_Add_ProposalId_to_Structure.sql';

