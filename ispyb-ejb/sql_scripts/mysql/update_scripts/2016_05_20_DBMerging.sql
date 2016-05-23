use pydb;

alter table Person drop passwd;

SET foreign_key_checks = 0;

DROP TABLE `WorkflowMesh`;
DROP TABLE `MotorPosition`;

ALTER TABLE `Image` DROP `motorPositionId`;
ALTER TABLE pydb.BLSubSample DROP COLUMN motorPositionId;

SET foreign_key_checks = 1;


