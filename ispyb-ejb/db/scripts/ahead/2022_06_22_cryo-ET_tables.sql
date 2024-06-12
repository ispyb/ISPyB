INSERT IGNORE INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2022_06_22_cryo-ET_tables.sql', 'ONGOING');

ALTER TABLE Movie
  ADD COLUMN IF NOT EXISTS angle float COMMENT 'unit: degrees relative to perpendicular to beam',
  ADD COLUMN IF NOT EXISTS fluence float COMMENT 'accumulated electron fluence from start to end of acquisition of this movie (commonly, but incorrectly, referred to as ‘dose’)',
  ADD COLUMN IF NOT EXISTS numberOfFrames int(11) unsigned COMMENT 'number of frames per movie. This should be equivalent to the number of MotionCorrectionDrift entries, but the latter is a property of data analysis, whereas the number of frames is an intrinsic property of acquisition.';

CREATE TABLE Tomogram (
  tomogramId int(11) unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
  dataCollectionId int(11) unsigned COMMENT 'FK to DataCollection table',
  autoProcProgramId int(10) unsigned COMMENT 'FK, gives processing times/status and software information',
  volumeFile varchar(255) COMMENT '.mrc file representing the reconstructed tomogram volume',
  stackFile varchar(255) NULL DEFAULT NULL COMMENT '.mrc file containing the motion corrected images ordered by angle used as input for the reconstruction',
  sizeX int(11) unsigned COMMENT 'unit: pixels',
  sizeY int(11) unsigned COMMENT 'unit: pixels',
  sizeZ int(11) unsigned COMMENT 'unit: pixels',
  pixelSpacing float COMMENT 'Angstrom/pixel conversion factor',
  residualErrorMean float COMMENT 'Alignment error, unit: nm',
  residualErrorSD float COMMENT 'Standard deviation of the alignment error, unit: nm',
  xAxisCorrection float COMMENT 'X axis angle (etomo), unit: degrees',
  tiltAngleOffset float COMMENT 'tilt Axis offset (etomo), unit: degrees',
  zShift float COMMENT 'shift to center volumen in Z (etomo)',
  CONSTRAINT FOREIGN KEY Tomogram_fk_dataCollectionId (dataCollectionId) REFERENCES DataCollection (dataCollectionId) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FOREIGN KEY Tomogram_fk_autoProcProgramId (autoProcProgramId) REFERENCES AutoProcProgram (autoProcProgramId) ON DELETE SET NULL ON UPDATE CASCADE
)
COMMENT 'For storing per-sample, per-position data analysis results (reconstruction)';

CREATE TABLE TiltImageAlignment (
  movieId int(11) unsigned NOT NULL COMMENT 'FK to Movie table',
  tomogramId int(11) unsigned NOT NULL COMMENT 'FK to Tomogram table; tuple (movieID, tomogramID) is unique',
  defocusU float COMMENT 'unit: Angstroms',
  defocusV float COMMENT 'unit: Angstroms',
  psdFile varchar(255),
  resolution float COMMENT 'unit: Angstroms',
  fitQuality float,
  refinedMagnification float NULL DEFAULT NULL COMMENT 'unitless',
  refinedTiltAngle float COMMENT 'units: degrees',
  refinedTiltAxis float COMMENT 'units: degrees',
  residualError float COMMENT 'Residual error, unit: nm',
  PRIMARY KEY (movieId, tomogramId),
  CONSTRAINT FOREIGN KEY TiltImageAlignment_fk_movieId (movieId) REFERENCES Movie (movieId) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FOREIGN KEY TiltImageAlignment_fk_tomogramId (tomogramId) REFERENCES Tomogram (tomogramId) ON DELETE CASCADE ON UPDATE CASCADE
)
COMMENT 'For storing per-movie analysis results (reconstruction)';

ALTER TABLE BeamLineSetup
  ADD amplitudeContrast float COMMENT 'Needed for cryo-ET';

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2022_06_22_cryo-ET_tables.sql';
