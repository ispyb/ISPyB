ALTER TABLE `Person` DROP FOREIGN KEY `Person_ibfk_1` ;

ALTER TABLE `Person` ADD FOREIGN KEY ( `laboratoryId` ) REFERENCES `pydb`.`Laboratory` (
`laboratoryId`
) ON DELETE NO ACTION ON UPDATE NO ACTION ;