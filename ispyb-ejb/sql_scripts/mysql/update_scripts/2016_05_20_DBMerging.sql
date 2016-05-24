use pydb;

alter table Person drop passwd;

SET foreign_key_checks = 0;

DROP TABLE `WorkflowMesh`;
DROP TABLE `MotorPosition`;

SET foreign_key_checks = 1;


