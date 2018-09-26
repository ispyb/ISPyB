USE `pydb`;

INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2018_09_04_Added_anisotropic_fields.sql', 'ONGOING');

ALTER TABLE AutoProcScalingStatistics 
  ADD completenessSpherical float NULL DEFAULT NULL COMMENT 'Completeness calculated assuming isotropic diffraction',
  ADD completenessEllipsoidal float NULL DEFAULT NULL COMMENT 'Completeness calculated allowing for anisotropic diffraction',
  ADD anomalousCompletenessSpherical float NULL DEFAULT NULL COMMENT 'Anomalous completeness calculated assuming isotropic diffraction',
  ADD anomalousCompletenessEllipsoidal float NULL DEFAULT NULL COMMENT 'Anisotropic completeness calculated allowing for anisotropic diffraction';
  
ALTER TABLE AutoProcScaling 
  ADD resolutionEllipsoidAxis11 float NULL DEFAULT NULL COMMENT 'Eigenvector for first diffraction limit, coord 1',
  ADD resolutionEllipsoidAxis12 float NULL DEFAULT NULL COMMENT 'Eigenvector for first diffraction limit, coord 2',
  ADD resolutionEllipsoidAxis13 float NULL DEFAULT NULL COMMENT 'Eigenvector for first diffraction limit, coord 3',
  ADD resolutionEllipsoidAxis21 float NULL DEFAULT NULL COMMENT 'Eigenvector for second diffraction limit, coord 1',
  ADD resolutionEllipsoidAxis22 float NULL DEFAULT NULL COMMENT 'Eigenvector for second diffraction limit, coord 2',
  ADD resolutionEllipsoidAxis23 float NULL DEFAULT NULL COMMENT 'Eigenvector for second diffraction limit, coord 3',
  ADD resolutionEllipsoidAxis31 float NULL DEFAULT NULL COMMENT 'Eigenvector for third diffraction limit, coord 1',
  ADD resolutionEllipsoidAxis32 float NULL DEFAULT NULL COMMENT 'Eigenvector for third diffraction limit, coord 2',
  ADD resolutionEllipsoidAxis33 float NULL DEFAULT NULL COMMENT 'Eigenvector for third diffraction limit, coord 3',
  ADD resolutionEllipsoidValue1 float NULL DEFAULT NULL COMMENT 'First (anisotropic) diffraction limit',
  ADD resolutionEllipsoidValue2 float NULL DEFAULT NULL COMMENT 'Second (anisotropic) diffraction limit',
  ADD resolutionEllipsoidValue3 float NULL DEFAULT NULL COMMENT 'Third (anisotropic) diffraction limit';

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2018_09_04_Added_anisotropic_fields.sql';
