CREATE TABLE `pydb`.`SchemaStatus` (
  `schemaStatusId` INT NOT NULL AUTO_INCREMENT,
  `scriptName` VARCHAR(100) UNIQUE NOT NULL,
  `schemaStatus` VARCHAR(10) NULL,
  `recordTimeStamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`schemaStatusId`));

