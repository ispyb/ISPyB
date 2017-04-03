DROP TABLE `pydb`.`WorkflowStep`;
CREATE TABLE `pydb`.`WorkflowStep` (
  `workflowStepId` INT NOT NULL AUTO_INCREMENT,
  `workflowId` INT(11) NOT NULL,
  `type` VARCHAR(45) NULL,
  `status` VARCHAR(45) NULL,
  `folderPath` VARCHAR(1024) NULL,
  `imageResultFilePath` VARCHAR(1024) NULL,
  `htmlResultFilePath` VARCHAR(1024) NULL,
  `resultFilePath` VARCHAR(1024) NULL,
  `comments` VARCHAR(2048) NULL,
  PRIMARY KEY (`workflowStepId`));


ALTER TABLE `pydb`.`WorkflowStep` 
CHANGE COLUMN `workflowId` `workflowId` INT(11) UNSIGNED NOT NULL ;

ALTER TABLE `pydb`.`WorkflowStep` 
CHANGE COLUMN `workflowId` `workflowId` INT(11) UNSIGNED NOT NULL ,
ADD INDEX `step_to_workflow_fk_idx` (`workflowId` ASC);
ALTER TABLE `pydb`.`WorkflowStep` 
ADD CONSTRAINT `step_to_workflow_fk`
  FOREIGN KEY (`workflowId`)
  REFERENCES `pydb`.`Workflow` (`workflowId`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `pydb`.`WorkflowStep` 
ADD COLUMN `recordTimeStamp` TIMESTAMP NULL AFTER `comments`;
