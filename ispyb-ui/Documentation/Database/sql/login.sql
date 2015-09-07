CREATE  TABLE `pydb`.`Login` (
  `loginId` INT NOT NULL AUTO_INCREMENT ,
  `token` VARCHAR(45) NOT NULL ,
  `username` VARCHAR(45) NOT NULL ,
  `roles` VARCHAR(1024) NOT NULL ,
  `expirationTime` DATETIME NOT NULL ,
  PRIMARY KEY (`loginId`) ,
  INDEX `Token` (`token` ASC) );