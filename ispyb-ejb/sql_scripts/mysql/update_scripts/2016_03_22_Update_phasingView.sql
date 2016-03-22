CREATE 
    ALGORITHM = MERGE 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `pydb`.`v_datacollection_summary_phasing` AS
    SELECT 
        `pydb`.`PhasingStep`.`phasingStepId` AS `v_datacollection_summary_phasing_phasingStepId`,
        `pydb`.`PhasingStep`.`previousPhasingStepId` AS `v_datacollection_summary_phasing_previousPhasingStepId`,
        `pydb`.`PhasingStep`.`phasingAnalysisId` AS `v_datacollection_summary_phasing_phasingAnalysisId`,
        `pydb`.`AutoProcIntegration`.`autoProcIntegrationId` AS `v_datacollection_summary_phasing_autoProcIntegrationId`,
        `pydb`.`AutoProcIntegration`.`dataCollectionId` AS `v_datacollection_summary_phasing_dataCollectionId`,
        `pydb`.`AutoProcIntegration`.`cell_a` AS `v_datacollection_summary_phasing_cell_a`,
        `pydb`.`AutoProcIntegration`.`cell_b` AS `v_datacollection_summary_phasing_cell_b`,
        `pydb`.`AutoProcIntegration`.`cell_c` AS `v_datacollection_summary_phasing_cell_c`,
        `pydb`.`AutoProcIntegration`.`cell_alpha` AS `v_datacollection_summary_phasing_cell_alpha`,
        `pydb`.`AutoProcIntegration`.`cell_beta` AS `v_datacollection_summary_phasing_cell_beta`,
        `pydb`.`AutoProcIntegration`.`cell_gamma` AS `v_datacollection_summary_phasing_cell_gamma`,
        `pydb`.`AutoProcIntegration`.`anomalous` AS `v_datacollection_summary_phasing_anomalous`,
        `pydb`.`AutoProc`.`spaceGroup` AS `v_datacollection_summary_phasing_autoproc_space_group`,
        `pydb`.`AutoProc`.`autoProcId` AS `v_datacollection_summary_phasing_autoproc_autoprocId`,
        `pydb`.`PhasingStep`.`phasingStepType` AS `v_datacollection_summary_phasing_phasingStepType`,
        `pydb`.`PhasingStep`.`method` AS `v_datacollection_summary_phasing_method`,
        `pydb`.`PhasingStep`.`solventContent` AS `v_datacollection_summary_phasing_solventContent`,
        `pydb`.`PhasingStep`.`enantiomorph` AS `v_datacollection_summary_phasing_enantiomorph`,
        `pydb`.`PhasingStep`.`lowRes` AS `v_datacollection_summary_phasing_lowRes`,
        `pydb`.`PhasingStep`.`highRes` AS `v_datacollection_summary_phasing_highRes`,
        `pydb`.`AutoProcScaling`.`autoProcScalingId` AS `v_datacollection_summary_phasing_autoProcScalingId`,
        `pydb`.`SpaceGroup`.`spaceGroupShortName` AS `v_datacollection_summary_phasing_spaceGroupShortName`,
        `pydb`.`AutoProcProgram`.`processingPrograms` AS `v_datacollection_summary_phasing_processingPrograms`,
        `pydb`.`AutoProcProgram`.`processingStatus` AS `v_datacollection_summary_phasing_processingStatus`,
        `pydb`.`PhasingProgramRun`.`phasingPrograms` AS `v_datacollection_summary_phasing_phasingPrograms`,
        `pydb`.`PhasingProgramRun`.`phasingStatus` AS `v_datacollection_summary_phasing_phasingStatus`,
        `pydb`.`PhasingProgramRun`.`phasingStartTime` AS `v_datacollection_summary_phasing_phasingStartTime`,
        `pydb`.`PhasingProgramRun`.`phasingEndTime` AS `v_datacollection_summary_phasing_phasingEndTime`
    FROM
        (((((((`pydb`.`AutoProcIntegration`
        LEFT JOIN `pydb`.`AutoProcScaling_has_Int` ON ((`pydb`.`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `pydb`.`AutoProcIntegration`.`autoProcIntegrationId`)))
        LEFT JOIN `pydb`.`AutoProcScaling` ON ((`pydb`.`AutoProcScaling_has_Int`.`autoProcScalingId` = `pydb`.`AutoProcScaling`.`autoProcScalingId`)))
        LEFT JOIN `pydb`.`AutoProcProgram` ON ((`pydb`.`AutoProcProgram`.`autoProcProgramId` = `pydb`.`AutoProcIntegration`.`autoProcProgramId`)))
        LEFT JOIN `pydb`.`AutoProc` ON ((`pydb`.`AutoProc`.`autoProcProgramId` = `pydb`.`AutoProcIntegration`.`autoProcProgramId`)))
        LEFT JOIN `pydb`.`PhasingStep` ON ((`pydb`.`PhasingStep`.`autoProcScalingId` = `pydb`.`AutoProcScaling`.`autoProcScalingId`)))
        LEFT JOIN `pydb`.`PhasingProgramRun` ON ((`pydb`.`PhasingProgramRun`.`phasingProgramRunId` = `pydb`.`PhasingStep`.`programRunId`)))
        LEFT JOIN `pydb`.`SpaceGroup` ON ((`pydb`.`SpaceGroup`.`spaceGroupId` = `pydb`.`PhasingStep`.`spaceGroupId`)))