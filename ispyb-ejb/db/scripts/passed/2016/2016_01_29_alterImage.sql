ALTER TABLE `Image` ADD FOREIGN KEY ( `dataCollectionId` ) REFERENCES `pydb`.`DataCollection` (
`dataCollectionId`
) ON DELETE CASCADE ON UPDATE NO ACTION ;

ALTER TABLE `Image` DROP FOREIGN KEY `Image_ibfk_2` ;

ALTER TABLE `Image` ADD FOREIGN KEY ( `motorPositionId` ) REFERENCES `pydb`.`MotorPosition` (
`motorPositionId`
) ON DELETE CASCADE ON UPDATE NO ACTION ;
