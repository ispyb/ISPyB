USE `pydb`;

INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2018_06_28_add_EM_xpixel.sql', 'ONGOING');

CREATE 
     OR REPLACE ALGORITHM = MERGE 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `pydb`.`v_em_movie` AS
    SELECT 
        `pydb`.`Movie`.`movieId` AS `Movie_movieId`,
        `pydb`.`Movie`.`dataCollectionId` AS `Movie_dataCollectionId`,
        `pydb`.`Movie`.`movieNumber` AS `Movie_movieNumber`,
        `pydb`.`Movie`.`movieFullPath` AS `Movie_movieFullPath`,
        `pydb`.`Movie`.`positionX` AS `Movie_positionX`,
        `pydb`.`Movie`.`positionY` AS `Movie_positionY`,
        `pydb`.`Movie`.`micrographFullPath` AS `Movie_micrographFullPath`,
        `pydb`.`Movie`.`micrographSnapshotFullPath` AS `Movie_micrographSnapshotFullPath`,
        `pydb`.`Movie`.`xmlMetaDataFullPath` AS `Movie_xmlMetaDataFullPath`,
        `pydb`.`Movie`.`dosePerImage` AS `Movie_dosePerImage`,
        `pydb`.`Movie`.`createdTimeStamp` AS `Movie_createdTimeStamp`,
        `pydb`.`MotionCorrection`.`motionCorrectionId` AS `MotionCorrection_motionCorrectionId`,
        `pydb`.`MotionCorrection`.`movieId` AS `MotionCorrection_movieId`,
        `pydb`.`MotionCorrection`.`firstFrame` AS `MotionCorrection_firstFrame`,
        `pydb`.`MotionCorrection`.`lastFrame` AS `MotionCorrection_lastFrame`,
        `pydb`.`MotionCorrection`.`dosePerFrame` AS `MotionCorrection_dosePerFrame`,
        `pydb`.`MotionCorrection`.`doseWeight` AS `MotionCorrection_doseWeight`,
        `pydb`.`MotionCorrection`.`totalMotion` AS `MotionCorrection_totalMotion`,
        `pydb`.`MotionCorrection`.`averageMotionPerFrame` AS `MotionCorrection_averageMotionPerFrame`,
        `pydb`.`MotionCorrection`.`driftPlotFullPath` AS `MotionCorrection_driftPlotFullPath`,
        `pydb`.`MotionCorrection`.`micrographFullPath` AS `MotionCorrection_micrographFullPath`,
        `pydb`.`MotionCorrection`.`micrographSnapshotFullPath` AS `MotionCorrection_micrographSnapshotFullPath`,
        `pydb`.`MotionCorrection`.`correctedDoseMicrographFullPath` AS `MotionCorrection_correctedDoseMicrographFullPath`,
        `pydb`.`MotionCorrection`.`patchesUsed` AS `MotionCorrection_patchesUsed`,
        `pydb`.`MotionCorrection`.`logFileFullPath` AS `MotionCorrection_logFileFullPath`,
        `pydb`.`CTF`.`CTFid` AS `CTF_CTFid`,
        `pydb`.`CTF`.`motionCorrectionId` AS `CTF_motionCorrectionId`,
        `pydb`.`CTF`.`spectraImageThumbnailFullPath` AS `CTF_spectraImageThumbnailFullPath`,
        `pydb`.`CTF`.`spectraImageFullPath` AS `CTF_spectraImageFullPath`,
        `pydb`.`CTF`.`defocusU` AS `CTF_defocusU`,
        `pydb`.`CTF`.`defocusV` AS `CTF_defocusV`,
        `pydb`.`CTF`.`angle` AS `CTF_angle`,
        `pydb`.`CTF`.`crossCorrelationCoefficient` AS `CTF_crossCorrelationCoefficient`,
        `pydb`.`CTF`.`resolutionLimit` AS `CTF_resolutionLimit`,
        `pydb`.`CTF`.`estimatedBfactor` AS `CTF_estimatedBfactor`,
        `pydb`.`CTF`.`logFilePath` AS `CTF_logFilePath`,
        `pydb`.`CTF`.`createdTimeStamp` AS `CTF_createdTimeStamp`,
        `pydb`.`DataCollection`.`xBeamPix` as `DataCollection_xBeamPix`,
        `pydb`.`Proposal`.`proposalId` AS `Proposal_proposalId`,
        `pydb`.`BLSession`.`sessionId` AS `BLSession_sessionId`
    FROM
        ((((((`pydb`.`Movie`
        LEFT JOIN `pydb`.`MotionCorrection` ON ((`pydb`.`MotionCorrection`.`movieId` = `pydb`.`Movie`.`movieId`)))
        LEFT JOIN `pydb`.`DataCollection` ON ((`pydb`.`DataCollection`.`dataCollectionId` = `pydb`.`Movie`.`dataCollectionId`)))
        LEFT JOIN `pydb`.`DataCollectionGroup` ON ((`pydb`.`DataCollectionGroup`.`dataCollectionGroupId` = `pydb`.`DataCollection`.`dataCollectionGroupId`)))
        LEFT JOIN `pydb`.`BLSession` ON ((`pydb`.`BLSession`.`sessionId` = `pydb`.`DataCollectionGroup`.`sessionId`)))
        LEFT JOIN `pydb`.`Proposal` ON ((`pydb`.`Proposal`.`proposalId` = `pydb`.`BLSession`.`proposalId`)))
        LEFT JOIN `pydb`.`CTF` ON ((`pydb`.`CTF`.`motionCorrectionId` = `pydb`.`MotionCorrection`.`motionCorrectionId`)));
        
UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2018_06_28_add_EM_xpixel.sql';


