USE `pydb`;

INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2018_09_04_Added_anisotropic_fields.sql', 'ONGOING');

ALTER TABLE AutoProcScalingStatistics 
  ADD completenessSpherical float NULL DEFAULT NULL,
  ADD completenessEllipsoidal float NULL DEFAULT NULL,
  ADD anomalousCompletenessSpherical float NULL DEFAULT NULL,
  ADD anomalousCompletenessEllipsoidal float NULL DEFAULT NULL;
  
ALTER TABLE AutoProcScaling 
  ADD resolutionEllipsoidAxis11 float NULL DEFAULT NULL,
  ADD resolutionEllipsoidAxis12 float NULL DEFAULT NULL,
  ADD resolutionEllipsoidAxis13 float NULL DEFAULT NULL,
  ADD resolutionEllipsoidAxis21 float NULL DEFAULT NULL,
  ADD resolutionEllipsoidAxis22 float NULL DEFAULT NULL,
  ADD resolutionEllipsoidAxis23 float NULL DEFAULT NULL,
  ADD resolutionEllipsoidAxis31 float NULL DEFAULT NULL,
  ADD resolutionEllipsoidAxis32 float NULL DEFAULT NULL,
  ADD resolutionEllipsoidAxis33 float NULL DEFAULT NULL,
  ADD resolutionEllipsoidValue1 float NULL DEFAULT NULL,
  ADD resolutionEllipsoidValue2 float NULL DEFAULT NULL,
  ADD resolutionEllipsoidValue3 float NULL DEFAULT NULL;

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2018_09_04_Added_anisotropic_fields.sql';