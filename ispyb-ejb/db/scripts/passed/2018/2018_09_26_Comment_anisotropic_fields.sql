USE `pydb`;

INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2018_09_26_Comment_anisotropic_fields.sql', 'ONGOING');

ALTER TABLE AutoProcScalingStatistics 
 CHANGE completenessSpherical completenessSpherical FLOAT NULL DEFAULT NULL COMMENT 'Completeness calculated assuming isotropic diffraction',
  CHANGE completenessEllipsoidal completenessEllipsoidal float NULL DEFAULT NULL COMMENT 'Completeness calculated allowing for anisotropic diffraction',
  CHANGE anomalousCompletenessSpherical anomalousCompletenessSpherical float NULL DEFAULT NULL COMMENT 'Anomalous completeness calculated assuming isotropic diffraction',
  CHANGE anomalousCompletenessEllipsoidal anomalousCompletenessEllipsoidal float NULL DEFAULT NULL COMMENT 'Anisotropic completeness calculated allowing for anisotropic diffraction';
  
ALTER TABLE AutoProcScaling 
  CHANGE resolutionEllipsoidAxis11 resolutionEllipsoidAxis11 float NULL DEFAULT NULL COMMENT 'Eigenvector for first diffraction limit, coord 1',
  CHANGE resolutionEllipsoidAxis12 resolutionEllipsoidAxis12 float NULL DEFAULT NULL COMMENT 'Eigenvector for first diffraction limit, coord 2',
  CHANGE resolutionEllipsoidAxis13 resolutionEllipsoidAxis13 float NULL DEFAULT NULL COMMENT 'Eigenvector for first diffraction limit, coord 3',
  CHANGE resolutionEllipsoidAxis21 resolutionEllipsoidAxis21 float NULL DEFAULT NULL COMMENT 'Eigenvector for second diffraction limit, coord 1',
  CHANGE resolutionEllipsoidAxis22 resolutionEllipsoidAxis22 float NULL DEFAULT NULL COMMENT 'Eigenvector for second diffraction limit, coord 2',
  CHANGE resolutionEllipsoidAxis23 resolutionEllipsoidAxis23 float NULL DEFAULT NULL COMMENT 'Eigenvector for second diffraction limit, coord 3',
  CHANGE resolutionEllipsoidAxis31 resolutionEllipsoidAxis31 float NULL DEFAULT NULL COMMENT 'Eigenvector for third diffraction limit, coord 1',
  CHANGE resolutionEllipsoidAxis32 resolutionEllipsoidAxis32 float NULL DEFAULT NULL COMMENT 'Eigenvector for third diffraction limit, coord 2',
  CHANGE resolutionEllipsoidAxis33 resolutionEllipsoidAxis33 float NULL DEFAULT NULL COMMENT 'Eigenvector for third diffraction limit, coord 3',
  CHANGE resolutionEllipsoidValue1 resolutionEllipsoidValue1 float NULL DEFAULT NULL COMMENT 'First (anisotropic) diffraction limit',
  CHANGE resolutionEllipsoidValue2 resolutionEllipsoidValue2 float NULL DEFAULT NULL COMMENT 'Second (anisotropic) diffraction limit',
  CHANGE resolutionEllipsoidValue3 resolutionEllipsoidValue3 float NULL DEFAULT NULL COMMENT 'Third (anisotropic) diffraction limit';

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2018_09_26_Comment_anisotropic_fields.sql';
