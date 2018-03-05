

use pydb;
insert into SchemaStatus (scriptName, schemaStatus) values ('2018_02_23_AutoProcintegrationView.sql','ONGOING');
CREATE 
     OR REPLACE ALGORITHM = MERGE 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `pydb`.`v_datacollection_autoprocintegration` AS
    SELECT 
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
        `pydb`.`AutoProcScaling`.`autoProcScalingId` AS `v_datacollection_summary_phasing_autoProcScalingId`,
        `pydb`.`AutoProcProgram`.`processingPrograms` AS `v_datacollection_processingPrograms`,
        `pydb`.`AutoProcProgram`.`autoProcProgramId` AS `v_datacollection_summary_phasing_autoProcProgramId`,
        `pydb`.`AutoProcProgram`.`processingStatus` AS `v_datacollection_processingStatus`,
        `pydb`.`AutoProcProgram`.`processingStartTime` AS `v_datacollection_processingStartTime`,
        `pydb`.`AutoProcProgram`.`processingEndTime` AS `v_datacollection_processingEndTime`,
        `pydb`.`BLSession`.`sessionId` AS `v_datacollection_summary_session_sessionId`,
        `pydb`.`BLSession`.`proposalId` AS `v_datacollection_summary_session_proposalId`,
        `pydb`.`AutoProcIntegration`.`dataCollectionId` AS `AutoProcIntegration_dataCollectionId`,
        `pydb`.`AutoProcIntegration`.`autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`,
        `pydb`.`PhasingStep`.`phasingStepType` AS `PhasingStep_phasing_phasingStepType`,
        `pydb`.`SpaceGroup`.`spaceGroupShortName` AS `SpaceGroup_spaceGroupShortName`,
        `pydb`.`Protein`.`proteinId` AS `Protein_proteinId`,
        `pydb`.`Protein`.`acronym` AS `Protein_acronym`,
        `pydb`.`BLSample`.`name` AS `BLSample_name`,
        `pydb`.`DataCollection`.`dataCollectionNumber` AS `DataCollection_dataCollectionNumber`,
        `pydb`.`DataCollection`.`imagePrefix` AS `DataCollection_imagePrefix`
    FROM
        (((((((((((((`pydb`.`AutoProcIntegration`
        LEFT JOIN `pydb`.`DataCollection` ON ((`pydb`.`DataCollection`.`dataCollectionId` = `pydb`.`AutoProcIntegration`.`dataCollectionId`)))
        LEFT JOIN `pydb`.`DataCollectionGroup` ON ((`pydb`.`DataCollection`.`dataCollectionGroupId` = `pydb`.`DataCollectionGroup`.`dataCollectionGroupId`)))
        LEFT JOIN `pydb`.`BLSession` ON ((`pydb`.`BLSession`.`sessionId` = `pydb`.`DataCollectionGroup`.`sessionId`)))
        LEFT JOIN `pydb`.`AutoProcScaling_has_Int` ON ((`pydb`.`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `pydb`.`AutoProcIntegration`.`autoProcIntegrationId`)))
        LEFT JOIN `pydb`.`AutoProcScaling` ON ((`pydb`.`AutoProcScaling_has_Int`.`autoProcScalingId` = `pydb`.`AutoProcScaling`.`autoProcScalingId`)))
        LEFT JOIN `pydb`.`AutoProcProgram` ON ((`pydb`.`AutoProcProgram`.`autoProcProgramId` = `pydb`.`AutoProcIntegration`.`autoProcProgramId`)))
        LEFT JOIN `pydb`.`Phasing_has_Scaling` ON ((`pydb`.`Phasing_has_Scaling`.`autoProcScalingId` = `pydb`.`AutoProcScaling`.`autoProcScalingId`)))
        LEFT JOIN `pydb`.`PhasingStep` ON ((`pydb`.`PhasingStep`.`autoProcScalingId` = `pydb`.`Phasing_has_Scaling`.`autoProcScalingId`)))
        LEFT JOIN `pydb`.`SpaceGroup` ON ((`pydb`.`SpaceGroup`.`spaceGroupId` = `pydb`.`PhasingStep`.`spaceGroupId`)))
        LEFT JOIN `pydb`.`AutoProc` ON ((`pydb`.`AutoProc`.`autoProcProgramId` = `pydb`.`AutoProcIntegration`.`autoProcProgramId`)))
        LEFT JOIN `pydb`.`BLSample` ON ((`pydb`.`BLSample`.`blSampleId` = `pydb`.`DataCollectionGroup`.`blSampleId`)))
        LEFT JOIN `pydb`.`Crystal` ON ((`pydb`.`Crystal`.`crystalId` = `pydb`.`BLSample`.`crystalId`)))
        LEFT JOIN `pydb`.`Protein` ON ((`pydb`.`Protein`.`proteinId` = `pydb`.`Crystal`.`proteinId`)));

update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2018_02_23_AutoProcintegrationView.sql';
