use `pydb`;
insert into SchemaStatus (scriptName, schemaStatus) values ('2017_07_07_view_autoproc.sql','ONGOING');

drop view if exists v_datacollection_autoprocintegration;
CREATE 
    ALGORITHM = MERGE 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `v_datacollection_autoprocintegration` AS
    SELECT 
        `AutoProcIntegration`.`autoProcIntegrationId` AS `v_datacollection_summary_phasing_autoProcIntegrationId`,
        `AutoProcIntegration`.`dataCollectionId` AS `v_datacollection_summary_phasing_dataCollectionId`,
        `AutoProcIntegration`.`cell_a` AS `v_datacollection_summary_phasing_cell_a`,
        `AutoProcIntegration`.`cell_b` AS `v_datacollection_summary_phasing_cell_b`,
        `AutoProcIntegration`.`cell_c` AS `v_datacollection_summary_phasing_cell_c`,
        `AutoProcIntegration`.`cell_alpha` AS `v_datacollection_summary_phasing_cell_alpha`,
        `AutoProcIntegration`.`cell_beta` AS `v_datacollection_summary_phasing_cell_beta`,
        `AutoProcIntegration`.`cell_gamma` AS `v_datacollection_summary_phasing_cell_gamma`,
        `AutoProcIntegration`.`anomalous` AS `v_datacollection_summary_phasing_anomalous`,
        `AutoProc`.`spaceGroup` AS `v_datacollection_summary_phasing_autoproc_space_group`,
        `AutoProc`.`autoProcId` AS `v_datacollection_summary_phasing_autoproc_autoprocId`,
        `AutoProcScaling`.`autoProcScalingId` AS `v_datacollection_summary_phasing_autoProcScalingId`,
        `AutoProcProgram`.`processingPrograms` AS `v_datacollection_processingPrograms`,
        `AutoProcProgram`.`autoProcProgramId` AS `v_datacollection_summary_phasing_autoProcProgramId`,
        `AutoProcProgram`.`processingStatus` AS `v_datacollection_processingStatus`,
        `BLSession`.`sessionId` AS `v_datacollection_summary_session_sessionId`,
        `BLSession`.`proposalId` AS `v_datacollection_summary_session_proposalId`,
        `AutoProcIntegration`.`dataCollectionId` AS `AutoProcIntegration_dataCollectionId`,
        `AutoProcIntegration`.`autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`,
        `PhasingStep`.`phasingStepType` AS `PhasingStep_phasing_phasingStepType`,
        `SpaceGroup`.`spaceGroupShortName` AS `SpaceGroup_spaceGroupShortName`,
        `Protein`.`proteinId` AS `Protein_proteinId`,
        `Protein`.`acronym` AS `Protein_acronym`,
        `BLSample`.`name` AS `BLSample_name`,
        `DataCollection`.`dataCollectionNumber` AS `DataCollection_dataCollectionNumber`,
        `DataCollection`.`imagePrefix` AS `DataCollection_imagePrefix`
    FROM
        (((((((((((((`AutoProcIntegration`
        LEFT JOIN `DataCollection` ON ((`DataCollection`.`dataCollectionId` = `AutoProcIntegration`.`dataCollectionId`)))
        LEFT JOIN `DataCollectionGroup` ON ((`DataCollection`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`)))
        LEFT JOIN `BLSession` ON ((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`)))
        LEFT JOIN `AutoProcScaling_has_Int` ON ((`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`)))
        LEFT JOIN `AutoProcScaling` ON ((`AutoProcScaling_has_Int`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`)))
        LEFT JOIN `AutoProcProgram` ON ((`AutoProcProgram`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`)))
        LEFT JOIN `Phasing_has_Scaling` ON ((`Phasing_has_Scaling`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`)))
        LEFT JOIN `PhasingStep` ON ((`PhasingStep`.`autoProcScalingId` = `Phasing_has_Scaling`.`autoProcScalingId`)))
        LEFT JOIN `SpaceGroup` ON ((`SpaceGroup`.`spaceGroupId` = `PhasingStep`.`spaceGroupId`)))
        LEFT JOIN `AutoProc` ON ((`AutoProc`.`autoProcProgramId` = `AutoProcIntegration`.`autoProcProgramId`)))
        LEFT JOIN `BLSample` ON ((`BLSample`.`blSampleId` = `DataCollectionGroup`.`blSampleId`)))
        LEFT JOIN `Crystal` ON ((`Crystal`.`crystalId` = `BLSample`.`crystalId`)))
        LEFT JOIN `Protein` ON ((`Protein`.`proteinId` = `Crystal`.`proteinId`)));

        
update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_07_07_view_autoproc.sql';