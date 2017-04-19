
-- 

USE `pydb`;
SET foreign_key_checks = 0;
ALTER TABLE `DataAcquisition` DROP INDEX `DataAcquisitionToCapillary`;
ALTER TABLE `DataAcquisition` DROP FOREIGN KEY `DataAcquisitionToCapillary`;
SET foreign_key_checks = 1;

