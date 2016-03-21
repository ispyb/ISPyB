CREATE 
    ALGORITHM = MERGE 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `v_datacollection_summary_phasing` AS
    SELECT 
        `PhasingStep`.`phasingStepId` AS `v_datacollection_summary_phasing_phasingStepId`,
        `PhasingStep`.`previousPhasingStepId` AS `v_datacollection_summary_phasing_previousPhasingStepId`,
        `AutoProcIntegration`.`autoProcIntegrationId` AS `v_datacollection_summary_phasing_autoProcIntegrationId`,
        `AutoProcIntegration`.`dataCollectionId` AS `v_datacollection_summary_phasing_dataCollectionId`,
        `PhasingStep`.`phasingStepType` AS `v_datacollection_summary_phasing_phasingStepType`,
        `PhasingStep`.`method` AS `v_datacollection_summary_phasing_method`,
        `PhasingStep`.`solventContent` AS `v_datacollection_summary_phasing_solventContent`,
        `PhasingStep`.`enantiomorph` AS `v_datacollection_summary_phasing_enantiomorph`,
        `PhasingStep`.`lowRes` AS `v_datacollection_summary_phasing_lowRes`,
        `PhasingStep`.`highRes` AS `v_datacollection_summary_phasing_highRes`,
        `AutoProcScaling`.`autoProcScalingId` AS `v_datacollection_summary_phasing_autoProcScalingId`,
        `SpaceGroup`.`spaceGroupShortName` AS `v_datacollection_summary_phasing_spaceGroupShortName`,
        `AutoProcProgram`.`processingPrograms` AS `v_datacollection_summary_phasing_processingPrograms`,
        `AutoProcProgram`.`processingStatus` AS `v_datacollection_summary_phasing_processingStatus`,
        `PhasingProgramRun`.`phasingPrograms` AS `v_datacollection_summary_phasing_phasingPrograms`,
        `PhasingProgramRun`.`phasingStatus` AS `v_datacollection_summary_phasing_phasingStatus`,
        `PhasingProgramRun`.`phasingStartTime` AS `v_datacollection_summary_phasing_phasingStartTime`,
        `PhasingProgramRun`.`phasingEndTime` AS `v_datacollection_summary_phasing_phasingEndTime`
    FROM
        ((((((`AutoProcIntegration`
        LEFT JOIN `AutoProcScaling_has_Int` ON ((`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`)))
        LEFT JOIN `AutoProcScaling` ON ((`AutoProcScaling_has_Int`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`)))
        LEFT JOIN `AutoProcProgram` ON ((`AutoProcProgram`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`)))
        LEFT JOIN `PhasingStep` ON ((`PhasingStep`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`)))
        LEFT JOIN `PhasingProgramRun` ON ((`PhasingProgramRun`.`phasingProgramRunId` = `PhasingStep`.`programRunId`)))
        LEFT JOIN `SpaceGroup` ON ((`SpaceGroup`.`spaceGroupId` = `PhasingStep`.`spaceGroupId`)))