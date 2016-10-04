
-- 

USE `pydb`;
ALTER TABLE `DiffractionPlan` DROP FOREIGN KEY `DiffractionPlan_ibfk_1` ;
ALTER TABLE `DiffractionPlan` DROP INDEX `DiffractionPlan_FKIndex2` ;

ALTER TABLE `BLSubSample` DROP FOREIGN KEY `BLSubSample_motorPositionfk_1` ;
ALTER TABLE BLSubSample DROP INDEX BLSubSample_FKIndex4;
ALTER TABLE `BLSubSample` DROP `motorPositionId`;

ALTER TABLE Image DROP FOREIGN KEY `Image_ibfk_4` ;

ALTER TABLE DataCollection DROP FOREIGN KEY `DataCollection_ibfk_6` ;
ALTER TABLE DataCollection DROP FOREIGN KEY `DataCollection_ibfk_7` ;

ALTER TABLE WorkflowMesh DROP FOREIGN KEY `WorkflowMesh_ibfk_1` ;