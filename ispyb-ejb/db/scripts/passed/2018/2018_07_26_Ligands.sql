USE `pydb`;

INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2018_07_26_Ligands.sql', 'ONGOING');

ALTER TABLE `pydb`.`Structure` 
DROP FOREIGN KEY `StructureToMacromolecule`;
ALTER TABLE `pydb`.`Structure` 
CHANGE COLUMN `macromoleculeId` `macromoleculeId` INT(10) NULL ;
ALTER TABLE `pydb`.`Structure` 
ADD CONSTRAINT `StructureToMacromolecule`
  FOREIGN KEY (`macromoleculeId`)
  REFERENCES `pydb`.`Macromolecule` (`macromoleculeId`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `pydb`.`Structure` 
ADD COLUMN `crystalId` INT(10) UNSIGNED NULL AFTER `macromoleculeId`,
ADD INDEX `StructureToCrystal_idx` (`crystalId` ASC);
ALTER TABLE `pydb`.`Structure` 
ADD CONSTRAINT `StructureToCrystal`
  FOREIGN KEY (`crystalId`)
  REFERENCES `pydb`.`Crystal` (`crystalId`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `pydb`.`Structure` 
ADD COLUMN `groupName` VARCHAR(45) NULL AFTER `multiplicity`;
  
ALTER TABLE `pydb`.`Structure` 
ADD COLUMN `blSampleId` INT(10) UNSIGNED NULL AFTER `crystalId`,
ADD INDEX `StructureToBlSample_idx` (`blSampleId` ASC);
ALTER TABLE `pydb`.`Structure` 
ADD CONSTRAINT `StructureToBlSample`
  FOREIGN KEY (`blSampleId`)
  REFERENCES `pydb`.`BLSample` (`blSampleId`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

  
UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2018_07_26_Ligands.sql';