ALTER TABLE `pydb`.`Experiment` 
CHANGE COLUMN `sessionId` `sessionId` INT(10) UNSIGNED NULL DEFAULT NULL ,
ADD INDEX `fk_Experiment_To_session_idx` (`sessionId` ASC);
ALTER TABLE `pydb`.`Experiment` 
ADD CONSTRAINT `fk_Experiment_To_session`
  FOREIGN KEY (`sessionId`)
  REFERENCES `pydb`.`BLSession` (`sessionId`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
