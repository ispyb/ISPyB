
-- 

USE `pydb`;

ALTER TABLE BLSubSample
  ADD position2Id int(11) unsigned after positionId,
  ADD KEY `BLSubSample_FKIndex5` (`position2Id`),
  ADD CONSTRAINT `BLSubSample_positionfk_2` FOREIGN KEY (`position2Id`) REFERENCES `Position` (`positionId`) ON DELETE CASCADE ON UPDATE CASCADE;