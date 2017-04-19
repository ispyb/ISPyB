use pydb;

-- update Screening.dataCollectionGroupId
update Screening SC1, DataCollection DC1 
  set SC1.dataCollectionGroupId = DC1.dataCollectionGroupId
  WHERE SC1.dataCollectionId = DC1.dataCollectionId
        ;   
--

ALTER TABLE Screening DROP INDEX DNAScreening_FKIndex1;

ALTER TABLE `Screening` CHANGE `dataCollectionId` `dataCollectionId` INT(11) UNSIGNED NULL DEFAULT '0';

-- 
ALTER TABLE `pydb`.`Screening` ADD INDEX `dcgroupId` (`dataCollectionGroupId`);
ALTER TABLE `Screening` ADD FOREIGN KEY (`dataCollectionGroupId`) 
	REFERENCES `pydb`.`DataCollectionGroup`(`dataCollectionGroupId`) 
	ON DELETE NO ACTION ON UPDATE NO ACTION;
--

-- indexes not dropped previously	
SET foreign_key_checks = 0;
ALTER TABLE DiffractionPlan DROP INDEX DiffractionPlan_FKIndex2;
ALTER TABLE DataAcquisition DROP INDEX DataAcquisitionToCapillary;
SET foreign_key_checks = 1;

