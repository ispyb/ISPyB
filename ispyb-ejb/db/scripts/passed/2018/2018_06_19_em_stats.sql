use pydb;
INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2018_07_19_em_stats.sql', 'ONGOING');

CREATE 
     OR REPLACE ALGORITHM = MERGE 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `pydb`.`v_em_stats` AS
    SELECT 
         `Proposal`.`proposalId` AS `proposalId`,
        `BLSession`.`sessionId` AS `sessionId`,
        `DataCollection`.`imageDirectory` AS `imageDirectory`,
        `Movie`.`movieId` AS `movieId`,
        `Movie`.`movieNumber` AS `movieNumber`,
        `Movie`.`createdTimeStamp` AS `createdTimeStamp`,
        `MotionCorrection`.`motionCorrectionId` AS `motionCorrectionId`,
        `DataCollection`.`dataCollectionId` AS `dataCollectionId`,
        `MotionCorrection`.`totalMotion` AS `totalMotion`,
        `MotionCorrection`.`averageMotionPerFrame` AS `averageMotionPerFrame`,
        `MotionCorrection`.`lastFrame` AS `lastFrame`,
        `MotionCorrection`.`dosePerFrame` AS `dosePerFrame`,
        `CTF`.`defocusU` AS `defocusU`,
        `CTF`.`defocusV` AS `defocusV`,
        `CTF`.`resolutionLimit` AS `resolutionLimit`,
        `CTF`.`estimatedBfactor` AS `estimatedBfactor`,
        `CTF`.`angle` AS `angle`
    FROM
        ((((((`BLSession`
        JOIN `Proposal` ON ((`Proposal`.`proposalId` = `BLSession`.`proposalId`)))
        JOIN `DataCollectionGroup` ON ((`DataCollectionGroup`.`sessionId` = `BLSession`.`sessionId`)))
        JOIN `DataCollection` ON ((`DataCollection`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`)))
        JOIN `Movie` ON ((`Movie`.`dataCollectionId` = `DataCollection`.`dataCollectionId`)))
        JOIN `MotionCorrection` ON ((`MotionCorrection`.`movieId` = `Movie`.`movieId`)))
        JOIN `CTF` ON ((`CTF`.`motionCorrectionId` = `MotionCorrection`.`motionCorrectionId`)));

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2018_07_19_em_stats.sql';
