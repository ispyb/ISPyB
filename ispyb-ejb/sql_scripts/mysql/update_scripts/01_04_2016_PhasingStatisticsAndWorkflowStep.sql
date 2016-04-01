ALTER TABLE `pydb`.`PhasingStatistics` 
ADD COLUMN `phasingStepId` INT(10) UNSIGNED NULL AFTER `phasingHasScalingId2`,
ADD INDEX `fk_PhasingStatistics_phasingStep_idx` (`phasingStepId` ASC);
ALTER TABLE `pydb`.`PhasingStatistics` 
ADD CONSTRAINT `fk_PhasingStatistics_phasingStep`
  FOREIGN KEY (`phasingStepId`)
  REFERENCES `pydb`.`PhasingStep` (`phasingStepId`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `pydb`.`WorkflowStep` 
ADD COLUMN `crystalSizeX` VARCHAR(45) NULL AFTER `comments`,
ADD COLUMN `crystalSizeY` VARCHAR(45) NULL AFTER `crystalSizeX`,
ADD COLUMN `crystalSizeZ` VARCHAR(45) NULL AFTER `crystalSizeY`,
ADD COLUMN `maxDozorScore` VARCHAR(45) NULL AFTER `crystalSizeZ`;
