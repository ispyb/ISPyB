CREATE 
    ALGORITHM = MERGE 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `pydb`.`v_datacollection_summary_screening` AS
    SELECT 
        `pydb`.`Screening`.`screeningId` AS `Screening_screeningId`,
        `pydb`.`Screening`.`dataCollectionId` AS `Screening_dataCollectionId`,
        `pydb`.`ScreeningOutput`.`strategySuccess` AS `ScreeningOutput_strategySuccess`,
        `pydb`.`ScreeningOutput`.`indexingSuccess` AS `ScreeningOutput_indexingSuccess`,
        `pydb`.`ScreeningOutput`.`rankingResolution` AS `ScreeningOutput_rankingResolution`,
        `pydb`.`ScreeningOutput`.`mosaicityEstimated` AS `ScreeningOutput_mosaicityEstimated`,
        `pydb`.`ScreeningOutput`.`mosaicity` AS `ScreeningOutput_mosaicity`,
        `pydb`.`ScreeningOutputLattice`.`spaceGroup` AS `ScreeningOutputLattice_spaceGroup`,
        `pydb`.`ScreeningOutputLattice`.`unitCell_a` AS `ScreeningOutputLattice_unitCell_a`,
        `pydb`.`ScreeningOutputLattice`.`unitCell_b` AS `ScreeningOutputLattice_unitCell_b`,
        `pydb`.`ScreeningOutputLattice`.`unitCell_c` AS `ScreeningOutputLattice_unitCell_c`,
        `pydb`.`ScreeningOutputLattice`.`unitCell_alpha` AS `ScreeningOutputLattice_unitCell_alpha`,
        `pydb`.`ScreeningOutputLattice`.`unitCell_beta` AS `ScreeningOutputLattice_unitCell_beta`,
        `pydb`.`ScreeningOutputLattice`.`unitCell_gamma` AS `ScreeningOutputLattice_unitCell_gamma`
    FROM
        ((`pydb`.`Screening`
        LEFT JOIN `pydb`.`ScreeningOutput` ON ((`pydb`.`Screening`.`screeningId` = `pydb`.`ScreeningOutput`.`screeningId`)))
        LEFT JOIN `pydb`.`ScreeningOutputLattice` ON ((`pydb`.`ScreeningOutput`.`screeningOutputId` = `pydb`.`ScreeningOutputLattice`.`screeningOutputId`)));
        
CREATE 
    ALGORITHM = MERGE 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `pydb`.`v_datacollection_summary_phasing` AS
    SELECT 
        `pydb`.`PhasingStep`.`phasingStepId` AS `v_datacollection_summary_phasing_phasingStepId`,
        `pydb`.`PhasingStep`.`previousPhasingStepId` AS `v_datacollection_summary_phasing_previousPhasingStepId`,
        `pydb`.`AutoProcIntegration`.`autoProcIntegrationId` AS `v_datacollection_summary_phasing_autoProcIntegrationId`,
        `pydb`.`AutoProcIntegration`.`dataCollectionId` AS `v_datacollection_summary_phasing_dataCollectionId`,
        `pydb`.`PhasingStep`.`phasingStepType` AS `v_datacollection_summary_phasing_phasingStepType`,
        `pydb`.`PhasingStep`.`method` AS `v_datacollection_summary_phasing_method`,
        `pydb`.`PhasingStep`.`solventContent` AS `v_datacollection_summary_phasing_solventContent`,
        `pydb`.`PhasingStep`.`enantiomorph` AS `v_datacollection_summary_phasing_enantiomorph`,
        `pydb`.`PhasingStep`.`lowRes` AS `v_datacollection_summary_phasing_lowRes`,
        `pydb`.`PhasingStep`.`highRes` AS `v_datacollection_summary_phasing_highRes`,
        `pydb`.`AutoProcScaling`.`autoProcScalingId` AS `v_datacollection_summary_phasing_autoProcScalingId`,
        `pydb`.`SpaceGroup`.`spaceGroupShortName` AS `v_datacollection_summary_phasing_spaceGroupShortName`
    FROM
        (((((`pydb`.`AutoProcIntegration`
        LEFT JOIN `pydb`.`AutoProcScaling_has_Int` ON ((`pydb`.`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `pydb`.`AutoProcIntegration`.`autoProcIntegrationId`)))
        LEFT JOIN `pydb`.`AutoProcScaling` ON ((`pydb`.`AutoProcScaling`.`autoProcScalingId` = `pydb`.`AutoProcScaling_has_Int`.`autoProcScalingId`)))
        LEFT JOIN `pydb`.`Phasing_has_Scaling` ON ((`pydb`.`Phasing_has_Scaling`.`autoProcScalingId` = `pydb`.`AutoProcScaling`.`autoProcScalingId`)))
        LEFT JOIN `pydb`.`PhasingStep` ON ((`pydb`.`PhasingStep`.`autoProcScalingId` = `pydb`.`Phasing_has_Scaling`.`autoProcScalingId`)))
        LEFT JOIN `pydb`.`SpaceGroup` ON ((`pydb`.`SpaceGroup`.`spaceGroupId` = `pydb`.`PhasingStep`.`spaceGroupId`)));
        
        
CREATE 
    ALGORITHM = MERGE 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `pydb`.`v_datacollection_summary_datacollectiongroup` AS
    SELECT 
        `pydb`.`DataCollectionGroup`.`dataCollectionGroupId` AS `DataCollectionGroup_dataCollectionGroupId`,
        `pydb`.`DataCollectionGroup`.`blSampleId` AS `DataCollectionGroup_blSampleId`,
        `pydb`.`DataCollectionGroup`.`sessionId` AS `DataCollectionGroup_sessionId`,
        `pydb`.`DataCollectionGroup`.`workflowId` AS `DataCollectionGroup_workflowId`,
        `pydb`.`DataCollectionGroup`.`experimentType` AS `DataCollectionGroup_experimentType`,
        `pydb`.`DataCollectionGroup`.`startTime` AS `DataCollectionGroup_startTime`,
        `pydb`.`DataCollectionGroup`.`endTime` AS `DataCollectionGroup_endTime`,
        `pydb`.`DataCollectionGroup`.`comments` AS `DataCollectionGroup_comments`,
        `pydb`.`DataCollectionGroup`.`actualSampleBarcode` AS `DataCollectionGroup_actualSampleBarcode`,
        `pydb`.`DataCollectionGroup`.`xtalSnapshotFullPath` AS `DataCollectionGroup_xtalSnapshotFullPath`,
        `pydb`.`BLSample`.`blSampleId` AS `BLSample_blSampleId`,
        `pydb`.`BLSample`.`crystalId` AS `BLSample_crystalId`,
        `pydb`.`BLSample`.`name` AS `BLSample_name`,
        `pydb`.`BLSample`.`code` AS `BLSample_code`,
        `pydb`.`BLSession`.`sessionId` AS `BLSession_sessionId`,
        `pydb`.`BLSession`.`proposalId` AS `BLSession_proposalId`,
        `pydb`.`BLSession`.`protectedData` AS `BLSession_protectedData`,
        `pydb`.`Protein`.`proteinId` AS `Protein_proteinId`,
        `pydb`.`Protein`.`name` AS `Protein_name`,
        `pydb`.`Protein`.`acronym` AS `Protein_acronym`,
        `pydb`.`DataCollection`.`dataCollectionId` AS `DataCollection_dataCollectionId`,
        `pydb`.`DataCollection`.`dataCollectionGroupId` AS `DataCollection_dataCollectionGroupId`,
        `pydb`.`DataCollection`.`startTime` AS `DataCollection_startTime`,
        `pydb`.`DataCollection`.`endTime` AS `DataCollection_endTime`,
        `pydb`.`DataCollection`.`runStatus` AS `DataCollection_runStatus`,
        `pydb`.`DataCollection`.`numberOfImages` AS `DataCollection_numberOfImages`,
        `pydb`.`DataCollection`.`startImageNumber` AS `DataCollection_startImageNumber`,
        `pydb`.`DataCollection`.`numberOfPasses` AS `DataCollection_numberOfPasses`,
        `pydb`.`DataCollection`.`exposureTime` AS `DataCollection_exposureTime`,
        `pydb`.`DataCollection`.`imageDirectory` AS `DataCollection_imageDirectory`,
        `pydb`.`DataCollection`.`wavelength` AS `DataCollection_wavelength`,
        `pydb`.`DataCollection`.`resolution` AS `DataCollection_resolution`,
        `pydb`.`DataCollection`.`detectorDistance` AS `DataCollection_detectorDistance`,
        `pydb`.`DataCollection`.`xBeam` AS `DataCollection_xBeam`,
        `pydb`.`DataCollection`.`yBeam` AS `DataCollection_yBeam`,
        `pydb`.`DataCollection`.`comments` AS `DataCollection_comments`,
        `pydb`.`DataCollection`.`xtalSnapshotFullPath1` AS `DataCollection_xtalSnapshotFullPath1`,
        `pydb`.`DataCollection`.`xtalSnapshotFullPath2` AS `DataCollection_xtalSnapshotFullPath2`,
        `pydb`.`DataCollection`.`xtalSnapshotFullPath3` AS `DataCollection_xtalSnapshotFullPath3`,
        `pydb`.`DataCollection`.`xtalSnapshotFullPath4` AS `DataCollection_xtalSnapshotFullPath4`,
        `pydb`.`DataCollection`.`phiStart` AS `DataCollection_phiStart`,
        `pydb`.`DataCollection`.`kappaStart` AS `DataCollection_kappaStart`,
        `pydb`.`DataCollection`.`omegaStart` AS `DataCollection_omegaStart`,
        `pydb`.`DataCollection`.`resolutionAtCorner` AS `DataCollection_resolutionAtCorner`,
        `pydb`.`DataCollection`.`bestWilsonPlotPath` AS `DataCollection_bestWilsonPlotPath`,
        `pydb`.`Workflow`.`workflowTitle` AS `Workflow_workflowTitle`,
        `pydb`.`Workflow`.`workflowType` AS `Workflow_workflowType`,
        `pydb`.`Workflow`.`status` AS `Workflow_status`
    FROM
        ((((((`pydb`.`DataCollectionGroup`
        LEFT JOIN `pydb`.`DataCollection` ON (((`pydb`.`DataCollection`.`dataCollectionGroupId` = `pydb`.`DataCollectionGroup`.`dataCollectionGroupId`)
            AND (`pydb`.`DataCollection`.`dataCollectionId` = (SELECT 
                MAX(`dc2`.`dataCollectionId`)
            FROM
                `pydb`.`DataCollection` `dc2`
            WHERE
                (`dc2`.`dataCollectionGroupId` = `pydb`.`DataCollectionGroup`.`dataCollectionGroupId`))))))
        LEFT JOIN `pydb`.`BLSession` ON ((`pydb`.`BLSession`.`sessionId` = `pydb`.`DataCollectionGroup`.`sessionId`)))
        LEFT JOIN `pydb`.`BLSample` ON ((`pydb`.`BLSample`.`blSampleId` = `pydb`.`DataCollectionGroup`.`blSampleId`)))
        LEFT JOIN `pydb`.`Crystal` ON ((`pydb`.`Crystal`.`crystalId` = `pydb`.`BLSample`.`crystalId`)))
        LEFT JOIN `pydb`.`Protein` ON ((`pydb`.`Protein`.`proteinId` = `pydb`.`Crystal`.`proteinId`)))
        LEFT JOIN `pydb`.`Workflow` ON ((`pydb`.`DataCollectionGroup`.`workflowId` = `pydb`.`Workflow`.`workflowId`)));


CREATE 
    ALGORITHM = MERGE 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `pydb`.`v_datacollection_summary_autoprocintegration` AS
    SELECT 
        `pydb`.`AutoProcIntegration`.`dataCollectionId` AS `AutoProcIntegration_dataCollectionId`,
        `pydb`.`AutoProcIntegration`.`autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`,
        `pydb`.`AutoProcProgram`.`processingPrograms` AS `v_datacollection_summary_autoprocintegration_processingPrograms`,
        `pydb`.`AutoProcProgram`.`processingStatus` AS `v_datacollection_summary_autoprocintegration_processingStatus`,
        `pydb`.`AutoProcIntegration`.`dataCollectionId` AS `AutoProcIntegration_phasing_dataCollectionId`,
        `pydb`.`PhasingStep`.`phasingStepType` AS `PhasingStep_phasing_phasingStepType`,
        `pydb`.`SpaceGroup`.`spaceGroupShortName` AS `SpaceGroup_spaceGroupShortName`
    FROM
        ((((((`pydb`.`AutoProcIntegration`
        LEFT JOIN `pydb`.`AutoProcProgram` ON ((`pydb`.`AutoProcIntegration`.`autoProcProgramId` = `pydb`.`AutoProcProgram`.`autoProcProgramId`)))
        LEFT JOIN `pydb`.`AutoProcScaling_has_Int` ON ((`pydb`.`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `pydb`.`AutoProcIntegration`.`autoProcIntegrationId`)))
        LEFT JOIN `pydb`.`AutoProcScaling` ON ((`pydb`.`AutoProcScaling`.`autoProcScalingId` = `pydb`.`AutoProcScaling_has_Int`.`autoProcScalingId`)))
        LEFT JOIN `pydb`.`Phasing_has_Scaling` ON ((`pydb`.`Phasing_has_Scaling`.`autoProcScalingId` = `pydb`.`AutoProcScaling`.`autoProcScalingId`)))
        LEFT JOIN `pydb`.`PhasingStep` ON ((`pydb`.`PhasingStep`.`autoProcScalingId` = `pydb`.`Phasing_has_Scaling`.`autoProcScalingId`)))
        LEFT JOIN `pydb`.`SpaceGroup` ON ((`pydb`.`SpaceGroup`.`spaceGroupId` = `pydb`.`PhasingStep`.`spaceGroupId`)));
        

CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `pydb`.`v_datacollection_summary` AS
    SELECT 
        `pydb`.`DataCollectionGroup`.`dataCollectionGroupId` AS `DataCollectionGroup_dataCollectionGroupId`,
        `pydb`.`DataCollectionGroup`.`blSampleId` AS `DataCollectionGroup_blSampleId`,
        `pydb`.`DataCollectionGroup`.`sessionId` AS `DataCollectionGroup_sessionId`,
        `pydb`.`DataCollectionGroup`.`workflowId` AS `DataCollectionGroup_workflowId`,
        `pydb`.`DataCollectionGroup`.`experimentType` AS `DataCollectionGroup_experimentType`,
        `pydb`.`DataCollectionGroup`.`startTime` AS `DataCollectionGroup_startTime`,
        `pydb`.`DataCollectionGroup`.`endTime` AS `DataCollectionGroup_endTime`,
        `pydb`.`DataCollectionGroup`.`comments` AS `DataCollectionGroup_comments`,
        `pydb`.`DataCollectionGroup`.`actualSampleBarcode` AS `DataCollectionGroup_actualSampleBarcode`,
        `pydb`.`DataCollectionGroup`.`xtalSnapshotFullPath` AS `DataCollectionGroup_xtalSnapshotFullPath`,
        `pydb`.`BLSample`.`blSampleId` AS `BLSample_blSampleId`,
        `pydb`.`BLSample`.`crystalId` AS `BLSample_crystalId`,
        `pydb`.`BLSample`.`name` AS `BLSample_name`,
        `pydb`.`BLSample`.`code` AS `BLSample_code`,
        `pydb`.`BLSession`.`sessionId` AS `BLSession_sessionId`,
        `pydb`.`BLSession`.`proposalId` AS `BLSession_proposalId`,
        `pydb`.`BLSession`.`protectedData` AS `BLSession_protectedData`,
        `pydb`.`Protein`.`proteinId` AS `Protein_proteinId`,
        `pydb`.`Protein`.`name` AS `Protein_name`,
        `pydb`.`Protein`.`acronym` AS `Protein_acronym`,
        `pydb`.`DataCollection`.`dataCollectionId` AS `DataCollection_dataCollectionId`,
        `pydb`.`DataCollection`.`dataCollectionGroupId` AS `DataCollection_dataCollectionGroupId`,
        `pydb`.`DataCollection`.`startTime` AS `DataCollection_startTime`,
        `pydb`.`DataCollection`.`endTime` AS `DataCollection_endTime`,
        `pydb`.`DataCollection`.`runStatus` AS `DataCollection_runStatus`,
        `pydb`.`DataCollection`.`numberOfImages` AS `DataCollection_numberOfImages`,
        `pydb`.`DataCollection`.`startImageNumber` AS `DataCollection_startImageNumber`,
        `pydb`.`DataCollection`.`numberOfPasses` AS `DataCollection_numberOfPasses`,
        `pydb`.`DataCollection`.`exposureTime` AS `DataCollection_exposureTime`,
        `pydb`.`DataCollection`.`imageDirectory` AS `DataCollection_imageDirectory`,
        `pydb`.`DataCollection`.`wavelength` AS `DataCollection_wavelength`,
        `pydb`.`DataCollection`.`resolution` AS `DataCollection_resolution`,
        `pydb`.`DataCollection`.`detectorDistance` AS `DataCollection_detectorDistance`,
        `pydb`.`DataCollection`.`xBeam` AS `DataCollection_xBeam`,
        `pydb`.`DataCollection`.`yBeam` AS `DataCollection_yBeam`,
        `pydb`.`DataCollection`.`imagePrefix` AS `DataCollection_imagePrefix`,
        `pydb`.`DataCollection`.`comments` AS `DataCollection_comments`,
        `pydb`.`DataCollection`.`xtalSnapshotFullPath1` AS `DataCollection_xtalSnapshotFullPath1`,
        `pydb`.`DataCollection`.`xtalSnapshotFullPath2` AS `DataCollection_xtalSnapshotFullPath2`,
        `pydb`.`DataCollection`.`xtalSnapshotFullPath3` AS `DataCollection_xtalSnapshotFullPath3`,
        `pydb`.`DataCollection`.`xtalSnapshotFullPath4` AS `DataCollection_xtalSnapshotFullPath4`,
        `pydb`.`DataCollection`.`phiStart` AS `DataCollection_phiStart`,
        `pydb`.`DataCollection`.`kappaStart` AS `DataCollection_kappaStart`,
        `pydb`.`DataCollection`.`omegaStart` AS `DataCollection_omegaStart`,
        `pydb`.`DataCollection`.`flux` AS `DataCollection_flux`,
        `pydb`.`DataCollection`.`flux_end` AS `DataCollection_flux_end`,
        `pydb`.`DataCollection`.`resolutionAtCorner` AS `DataCollection_resolutionAtCorner`,
        `pydb`.`DataCollection`.`bestWilsonPlotPath` AS `DataCollection_bestWilsonPlotPath`,
        `pydb`.`Workflow`.`workflowTitle` AS `Workflow_workflowTitle`,
        `pydb`.`Workflow`.`workflowType` AS `Workflow_workflowType`,
        `pydb`.`Workflow`.`status` AS `Workflow_status`,
        `pydb`.`Workflow`.`workflowId` AS `Workflow_workflowId`,
        `v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_dataCollectionId` AS `AutoProcIntegration_dataCollectionId`,
        `v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`,
        `v_datacollection_summary_autoprocintegration`.`v_datacollection_summary_autoprocintegration_processingPrograms` AS `AutoProcProgram_processingPrograms`,
        `v_datacollection_summary_autoprocintegration`.`v_datacollection_summary_autoprocintegration_processingStatus` AS `AutoProcProgram_processingStatus`,
        `v_datacollection_summary_screening`.`Screening_screeningId` AS `Screening_screeningId`,
        `v_datacollection_summary_screening`.`Screening_dataCollectionId` AS `Screening_dataCollectionId`,
        `v_datacollection_summary_screening`.`ScreeningOutput_strategySuccess` AS `ScreeningOutput_strategySuccess`,
        `v_datacollection_summary_screening`.`ScreeningOutput_indexingSuccess` AS `ScreeningOutput_indexingSuccess`,
        `v_datacollection_summary_screening`.`ScreeningOutput_rankingResolution` AS `ScreeningOutput_rankingResolution`,
        `v_datacollection_summary_screening`.`ScreeningOutput_mosaicity` AS `ScreeningOutput_mosaicity`,
        `v_datacollection_summary_screening`.`ScreeningOutputLattice_spaceGroup` AS `ScreeningOutputLattice_spaceGroup`,
        `v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_a` AS `ScreeningOutputLattice_unitCell_a`,
        `v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_b` AS `ScreeningOutputLattice_unitCell_b`,
        `v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_c` AS `ScreeningOutputLattice_unitCell_c`,
        `v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_alpha` AS `ScreeningOutputLattice_unitCell_alpha`,
        `v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_beta` AS `ScreeningOutputLattice_unitCell_beta`,
        `v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_gamma` AS `ScreeningOutputLattice_unitCell_gamma`
    FROM
        ((((((((`pydb`.`DataCollectionGroup`
        LEFT JOIN `pydb`.`DataCollection` ON (((`pydb`.`DataCollection`.`dataCollectionGroupId` = `pydb`.`DataCollectionGroup`.`dataCollectionGroupId`)
            AND (`pydb`.`DataCollection`.`dataCollectionId` = (SELECT 
                MAX(`dc2`.`dataCollectionId`)
            FROM
                `pydb`.`DataCollection` `dc2`
            WHERE
                (`dc2`.`dataCollectionGroupId` = `pydb`.`DataCollectionGroup`.`dataCollectionGroupId`))))))
        LEFT JOIN `pydb`.`BLSession` ON ((`pydb`.`BLSession`.`sessionId` = `pydb`.`DataCollectionGroup`.`sessionId`)))
        LEFT JOIN `pydb`.`BLSample` ON ((`pydb`.`BLSample`.`blSampleId` = `pydb`.`DataCollectionGroup`.`blSampleId`)))
        LEFT JOIN `pydb`.`Crystal` ON ((`pydb`.`Crystal`.`crystalId` = `pydb`.`BLSample`.`crystalId`)))
        LEFT JOIN `pydb`.`Protein` ON ((`pydb`.`Protein`.`proteinId` = `pydb`.`Crystal`.`proteinId`)))
        LEFT JOIN `pydb`.`Workflow` ON ((`pydb`.`DataCollectionGroup`.`workflowId` = `pydb`.`Workflow`.`workflowId`)))
        LEFT JOIN `pydb`.`v_datacollection_summary_autoprocintegration` ON ((`v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_dataCollectionId` = `pydb`.`DataCollection`.`dataCollectionId`)))
        LEFT JOIN `pydb`.`v_datacollection_summary_screening` ON ((`v_datacollection_summary_screening`.`Screening_dataCollectionId` = `pydb`.`DataCollection`.`dataCollectionId`)))  ;     
        
