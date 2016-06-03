use pydb;

alter table Person drop passwd;

SET foreign_key_checks = 0;

--DROP TABLE `WorkflowMesh`;
--DROP TABLE `MotorPosition`;
--ALTER TABLE `DataCollection` DROP FOREIGN KEY `DataCollection_ibfk_6`;
--ALTER TABLE `DataCollection` DROP FOREIGN KEY `DataCollection_ibfk_7`;
--ALTER TABLE `GridInfo` DROP FOREIGN KEY `GridInfo_ibfk_1`;

SET foreign_key_checks = 1;


