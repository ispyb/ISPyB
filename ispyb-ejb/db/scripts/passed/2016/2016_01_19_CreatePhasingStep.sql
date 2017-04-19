CREATE TABLE IF NOT EXISTS `pydb`.`PhasingStep` (
  `phasingStepId` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `previousPhasingStepId` INT UNSIGNED NULL,
  `programRunId` INT UNSIGNED NULL,
  `spaceGroupId` INT UNSIGNED NULL,
  `autoProcScalingId` INT UNSIGNED NULL,
  `phasingAnalysisId` INT UNSIGNED NULL,
  `phasingStepType` enum('Prepare','SubStructDeter','Phasing','ModelBuilding') DEFAULT NULL,
  `method` VARCHAR(45) NULL,
  `solventContent` VARCHAR(45) NULL,
  `enantiomorph` VARCHAR(45) NULL,
  `lowRes` VARCHAR(45) NULL,
  `highRes` VARCHAR(45) NULL,
  `recordTimeStamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`phasingStepId`),
  INDEX `FK_programRun_id` (`programRunId` ASC),
  INDEX `FK_spacegroup_id` (`spaceGroupId` ASC),
  INDEX `FK_autoprocScaling_id` (`autoProcScalingId` ASC),
  INDEX `FK_phasingAnalysis_id` (`phasingAnalysisId` ASC),
  CONSTRAINT `FK_program`
    FOREIGN KEY (`programRunId`)
    REFERENCES `pydb`.`PhasingProgramRun` (`phasingProgramRunId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_spacegroup`
    FOREIGN KEY (`spaceGroupId`)
    REFERENCES `pydb`.`SpaceGroup` (`spaceGroupId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_autoprocScaling`
    FOREIGN KEY (`autoProcScalingId`)
    REFERENCES `pydb`.`AutoProcScaling` (`autoProcScalingId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

