use pydb;

DROP VIEW `pydb`.`v_datacollection_summary_phasing`;

CREATE 
    ALGORITHM = MERGE 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `v_datacollection_summary_phasing` AS
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
        LEFT JOIN `SpaceGroup` ON ((`SpaceGroup`.`spaceGroupId` = `PhasingStep`.`spaceGroupId`)));
        
        
CREATE 
    ALGORITHM = MERGE 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `v_datacollection_phasing_program_run` AS
    SELECT 
        `PhasingStep`.`phasingStepId` AS `phasingStepId`,
        `PhasingStep`.`previousPhasingStepId` AS `previousPhasingStepId`,
        `PhasingStep`.`phasingAnalysisId` AS `phasingAnalysisId`,
        `AutoProcIntegration`.`autoProcIntegrationId` AS `autoProcIntegrationId`,
        `AutoProcIntegration`.`dataCollectionId` AS `dataCollectionId`,
        `AutoProc`.`autoProcId` AS `autoProcId`,
        `PhasingStep`.`phasingStepType` AS `phasingStepType`,
        `PhasingStep`.`method` AS `method`,
        `AutoProcScaling`.`autoProcScalingId` AS `autoProcScalingId`,
        `SpaceGroup`.`spaceGroupShortName` AS `spaceGroupShortName`,
        `PhasingProgramRun`.`phasingPrograms` AS `phasingPrograms`,
        `PhasingProgramRun`.`phasingStatus` AS `phasingStatus`,
        `DataCollectionGroup`.`sessionId` AS `sessionId`,
        `BLSession`.`proposalId` AS `proposalId`,
        `BLSample`.`blSampleId` AS `blSampleId`,
        `BLSample`.`name` AS `name`,
        `BLSample`.`code` AS `code`,
        `Protein`.`acronym` AS `acronym`,
        `Protein`.`proteinId` AS `proteinId`,
        `PhasingProgramAttachment`.`phasingProgramAttachmentId` AS `phasingProgramAttachmentId`,
        `PhasingProgramAttachment`.`fileType` AS `fileType`,
        `PhasingProgramAttachment`.`fileName` AS `fileName`,
        `PhasingProgramAttachment`.`filePath` AS `filePath`
    FROM
        ((((((((((((((`AutoProcIntegration`
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
        LEFT JOIN `PhasingProgramAttachment` ON ((`PhasingProgramAttachment`.`phasingProgramRunId` = `PhasingProgramRun`.`phasingProgramRunId`)))
        LEFT JOIN `SpaceGroup` ON ((`SpaceGroup`.`spaceGroupId` = `PhasingStep`.`spaceGroupId`)));