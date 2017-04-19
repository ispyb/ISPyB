use pydb;

ALTER TABLE `pydb`.`PhasingStatistics` 
CHANGE COLUMN `metric` `metric` ENUM('Rcullis','Average Fragment Length','Chain Count','Residues Count','CC','PhasingPower','FOM','<d"/sig>','Best CC','CC(1/2)','Weak CC','CFOM','Pseudo_free_CC','CC of partial model') NULL DEFAULT NULL COMMENT 'metric' ;

ALTER TABLE `pydb`.`PhasingProgramAttachment` 
CHANGE COLUMN `fileType` `fileType` ENUM('Map','Logfile','PDB','CSV','INS', 'RES', 'TXT') NULL DEFAULT NULL COMMENT 'file type' ;


ALTER TABLE `pydb`.`XFEFluorescenceSpectrum` 
ADD COLUMN `workingDirectory` VARCHAR(512) NULL AFTER `flux_end`;

ALTER TABLE `pydb`.`EnergyScan` 
ADD COLUMN `workingDirectory` VARCHAR(45) NULL AFTER `flux_end`;

DROP VIEW IF EXISTS v_datacollection_phasing;

CREATE 
    ALGORITHM = MERGE 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `v_datacollection_phasing` AS
    SELECT 
        `PhasingStep`.`phasingStepId` AS `phasingStepId`,
        `PhasingStep`.`previousPhasingStepId` AS `previousPhasingStepId`,
        `PhasingStep`.`phasingAnalysisId` AS `phasingAnalysisId`,
        `AutoProcIntegration`.`autoProcIntegrationId` AS `autoProcIntegrationId`,
        `AutoProcIntegration`.`dataCollectionId` AS `dataCollectionId`,
        `AutoProcIntegration`.`anomalous` AS `anomalous`,
        `AutoProc`.`spaceGroup` AS `spaceGroup`,
        `AutoProc`.`autoProcId` AS `autoProcId`,
        `PhasingStep`.`phasingStepType` AS `phasingStepType`,
        `PhasingStep`.`method` AS `method`,
        `PhasingStep`.`solventContent` AS `solventContent`,
        `PhasingStep`.`enantiomorph` AS `enantiomorph`,
        `PhasingStep`.`lowRes` AS `lowRes`,
        `PhasingStep`.`highRes` AS `highRes`,
        `AutoProcScaling`.`autoProcScalingId` AS `autoProcScalingId`,
        `SpaceGroup`.`spaceGroupShortName` AS `spaceGroupShortName`,
        `AutoProcProgram`.`processingPrograms` AS `processingPrograms`,
        `AutoProcProgram`.`processingStatus` AS `processingStatus`,
        `PhasingProgramRun`.`phasingPrograms` AS `phasingPrograms`,
        `PhasingProgramRun`.`phasingStatus` AS `phasingStatus`,
        `PhasingProgramRun`.`phasingStartTime` AS `phasingStartTime`,
        `PhasingProgramRun`.`phasingEndTime` AS `phasingEndTime`,
        `DataCollectionGroup`.`sessionId` AS `sessionId`,
        `BLSession`.`proposalId` AS `proposalId`,
        `BLSample`.`blSampleId` AS `blSampleId`,
        `BLSample`.`name` AS `name`,
        `BLSample`.`code` AS `code`,
        `Protein`.`acronym` AS `acronym`,
        `Protein`.`proteinId` AS `proteinId`
    FROM
        (((((((((((((`AutoProcIntegration`
        LEFT JOIN `AutoProcScaling_has_Int` ON ((`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`)))
        LEFT JOIN `AutoProcScaling` ON ((`AutoProcScaling_has_Int`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`)))
        LEFT JOIN `AutoProcProgram` ON ((`AutoProcProgram`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`)))
        LEFT JOIN `AutoProc` ON ((`AutoProc`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`)))
        LEFT JOIN `PhasingStep` ON ((`PhasingStep`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`)))
        LEFT JOIN `PhasingProgramRun` ON ((`PhasingProgramRun`.`phasingProgramRunId` = `PhasingStep`.`programRunId`)))
        LEFT JOIN `DataCollection` ON ((`DataCollection`.`dataCollectionId` = `AutoProcIntegration`.`dataCollectionId`)))
        LEFT JOIN `DataCollectionGroup` ON ((`DataCollectionGroup`.`dataCollectionGroupId` = `DataCollection`.`dataCollectionGroupId`)))
        LEFT JOIN `BLSample` ON ((`BLSample`.`blSampleId` = `DataCollectionGroup`.`blSampleId`)))
        LEFT JOIN `BLSession` ON ((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`)))
        LEFT JOIN `Crystal` ON ((`Crystal`.`crystalId` = `BLSample`.`crystalId`)))
        LEFT JOIN `Protein` ON ((`Crystal`.`proteinId` = `Protein`.`proteinId`)))
        LEFT JOIN `SpaceGroup` ON ((`SpaceGroup`.`spaceGroupId` = `PhasingStep`.`spaceGroupId`)))