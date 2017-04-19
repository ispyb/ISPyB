use pydb;

SET foreign_key_checks = 0;

DROP TABLE `MotorPosition`;
ALTER TABLE `DataCollection` DROP FOREIGN KEY `DataCollection_ibfk_6`;
ALTER TABLE `DataCollection` DROP FOREIGN KEY `DataCollection_ibfk_7`;

SET foreign_key_checks = 1;

alter table DataCollection drop startPositionId;
alter table DataCollection drop endPositionId;



