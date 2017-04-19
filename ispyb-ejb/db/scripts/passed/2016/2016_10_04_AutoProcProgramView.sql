USE `pydb`;

drop view v_datacollection_summary_autoprocintegration;

CREATE 
     OR REPLACE ALGORITHM = MERGE 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `v_datacollection_summary_autoprocintegration` AS
    SELECT 
        `AutoProcIntegration`.`dataCollectionId` AS `AutoProcIntegration_dataCollectionId`,
        `AutoProcIntegration`.`cell_a` AS `cell_a`,
        `AutoProcIntegration`.`cell_b` AS `cell_b`,
        `AutoProcIntegration`.`cell_c` AS `cell_c`,
        `AutoProcIntegration`.`cell_alpha` AS `cell_alpha`,
        `AutoProcIntegration`.`cell_beta` AS `cell_beta`,
        `AutoProcIntegration`.`cell_gamma` AS `cell_gamma`,
        `AutoProcIntegration`.`autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`,
        `AutoProcProgram`.`processingPrograms` AS `v_datacollection_summary_autoprocintegration_processingPrograms`,
        `AutoProcProgram`.`autoProcProgramId` AS `AutoProcProgram_autoProcProgramId`,
        `AutoProcProgram`.`processingStatus` AS `v_datacollection_summary_autoprocintegration_processingStatus`,
        `AutoProcIntegration`.`dataCollectionId` AS `AutoProcIntegration_phasing_dataCollectionId`,
        `PhasingStep`.`phasingStepType` AS `PhasingStep_phasing_phasingStepType`,
        `SpaceGroup`.`spaceGroupShortName` AS `SpaceGroup_spaceGroupShortName`,
        `AutoProc`.`autoProcId` AS `autoProcId`,
        `AutoProc`.`spaceGroup` AS `AutoProc_spaceGroup`,
        `AutoProcScalingStatistics`.`scalingStatisticsType` AS `scalingStatisticsType`,
        `AutoProcScalingStatistics`.`resolutionLimitHigh` AS `resolutionLimitHigh`,
        `AutoProcScalingStatistics`.`resolutionLimitLow` AS `resolutionLimitLow`,
        `AutoProcScalingStatistics`.`rMerge` AS `rMerge`,
        `AutoProcScalingStatistics`.`completeness` AS `completeness`,
        `AutoProcScaling`.`autoProcScalingId` AS `autoProcScalingId`
    FROM
        ((((((((`AutoProcIntegration`
        LEFT JOIN `AutoProcProgram` ON ((`AutoProcIntegration`.`autoProcProgramId` = `AutoProcProgram`.`autoProcProgramId`)))
        LEFT JOIN `AutoProcScaling_has_Int` ON ((`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `AutoProcIntegration`.`autoProcIntegrationId`)))
        LEFT JOIN `AutoProcScaling` ON ((`AutoProcScaling`.`autoProcScalingId` = `AutoProcScaling_has_Int`.`autoProcScalingId`)))
        LEFT JOIN `AutoProcScalingStatistics` ON ((`AutoProcScaling`.`autoProcScalingId` = `AutoProcScalingStatistics`.`autoProcScalingId`)))
        LEFT JOIN `AutoProc` ON ((`AutoProc`.`autoProcId` = `AutoProcScaling`.`autoProcId`)))
        LEFT JOIN `Phasing_has_Scaling` ON ((`Phasing_has_Scaling`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`)))
        LEFT JOIN `PhasingStep` ON ((`PhasingStep`.`autoProcScalingId` = `Phasing_has_Scaling`.`autoProcScalingId`)))
        LEFT JOIN `SpaceGroup` ON ((`SpaceGroup`.`spaceGroupId` = `PhasingStep`.`spaceGroupId`)));

        
drop view v_datacollection_summary_screening;

CREATE 
    ALGORITHM = MERGE 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `v_datacollection_summary_screening` AS
    SELECT 
        `Screening`.`screeningId` AS `Screening_screeningId`,
        `Screening`.`dataCollectionId` AS `Screening_dataCollectionId`,
        `Screening`.`dataCollectionGroupId` AS `Screening_dataCollectionGroupId`,
        `ScreeningOutput`.`strategySuccess` AS `ScreeningOutput_strategySuccess`,
        `ScreeningOutput`.`indexingSuccess` AS `ScreeningOutput_indexingSuccess`,
        `ScreeningOutput`.`rankingResolution` AS `ScreeningOutput_rankingResolution`,
        `ScreeningOutput`.`mosaicityEstimated` AS `ScreeningOutput_mosaicityEstimated`,
        `ScreeningOutput`.`mosaicity` AS `ScreeningOutput_mosaicity`,
        `ScreeningOutputLattice`.`spaceGroup` AS `ScreeningOutputLattice_spaceGroup`,
        `ScreeningOutputLattice`.`unitCell_a` AS `ScreeningOutputLattice_unitCell_a`,
        `ScreeningOutputLattice`.`unitCell_b` AS `ScreeningOutputLattice_unitCell_b`,
        `ScreeningOutputLattice`.`unitCell_c` AS `ScreeningOutputLattice_unitCell_c`,
        `ScreeningOutputLattice`.`unitCell_alpha` AS `ScreeningOutputLattice_unitCell_alpha`,
        `ScreeningOutputLattice`.`unitCell_beta` AS `ScreeningOutputLattice_unitCell_beta`,
        `ScreeningOutputLattice`.`unitCell_gamma` AS `ScreeningOutputLattice_unitCell_gamma`
    FROM
        ((`Screening`
        LEFT JOIN `ScreeningOutput` ON ((`Screening`.`screeningId` = `ScreeningOutput`.`screeningId`)))
        LEFT JOIN `ScreeningOutputLattice` ON ((`ScreeningOutput`.`screeningOutputId` = `ScreeningOutputLattice`.`screeningOutputId`)));
        
        
drop view v_datacollection_summary;

CREATE 
     OR REPLACE ALGORITHM = UNDEFINED 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `v_datacollection_summary` AS
    SELECT 
        `DataCollectionGroup`.`dataCollectionGroupId` AS `DataCollectionGroup_dataCollectionGroupId`,
        `DataCollectionGroup`.`blSampleId` AS `DataCollectionGroup_blSampleId`,
        `DataCollectionGroup`.`sessionId` AS `DataCollectionGroup_sessionId`,
        `DataCollectionGroup`.`workflowId` AS `DataCollectionGroup_workflowId`,
        `DataCollectionGroup`.`experimentType` AS `DataCollectionGroup_experimentType`,
        `DataCollectionGroup`.`startTime` AS `DataCollectionGroup_startTime`,
        `DataCollectionGroup`.`endTime` AS `DataCollectionGroup_endTime`,
        `DataCollectionGroup`.`comments` AS `DataCollectionGroup_comments`,
        `DataCollectionGroup`.`actualSampleBarcode` AS `DataCollectionGroup_actualSampleBarcode`,
        `DataCollectionGroup`.`xtalSnapshotFullPath` AS `DataCollectionGroup_xtalSnapshotFullPath`,
        `BLSample`.`blSampleId` AS `BLSample_blSampleId`,
        `BLSample`.`crystalId` AS `BLSample_crystalId`,
        `BLSample`.`name` AS `BLSample_name`,
        `BLSample`.`code` AS `BLSample_code`,
        `BLSession`.`sessionId` AS `BLSession_sessionId`,
        `BLSession`.`proposalId` AS `BLSession_proposalId`,
        `BLSession`.`protectedData` AS `BLSession_protectedData`,
        `Protein`.`proteinId` AS `Protein_proteinId`,
        `Protein`.`name` AS `Protein_name`,
        `Protein`.`acronym` AS `Protein_acronym`,
        `DataCollection`.`dataCollectionId` AS `DataCollection_dataCollectionId`,
        `DataCollection`.`dataCollectionGroupId` AS `DataCollection_dataCollectionGroupId`,
        `DataCollection`.`startTime` AS `DataCollection_startTime`,
        `DataCollection`.`endTime` AS `DataCollection_endTime`,
        `DataCollection`.`runStatus` AS `DataCollection_runStatus`,
        `DataCollection`.`numberOfImages` AS `DataCollection_numberOfImages`,
        `DataCollection`.`startImageNumber` AS `DataCollection_startImageNumber`,
        `DataCollection`.`numberOfPasses` AS `DataCollection_numberOfPasses`,
        `DataCollection`.`exposureTime` AS `DataCollection_exposureTime`,
        `DataCollection`.`imageDirectory` AS `DataCollection_imageDirectory`,
        `DataCollection`.`wavelength` AS `DataCollection_wavelength`,
        `DataCollection`.`resolution` AS `DataCollection_resolution`,
        `DataCollection`.`detectorDistance` AS `DataCollection_detectorDistance`,
        `DataCollection`.`xBeam` AS `DataCollection_xBeam`,
        `DataCollection`.`transmission` AS `transmission`,
        `DataCollection`.`yBeam` AS `DataCollection_yBeam`,
        `DataCollection`.`imagePrefix` AS `DataCollection_imagePrefix`,
        `DataCollection`.`comments` AS `DataCollection_comments`,
        `DataCollection`.`xtalSnapshotFullPath1` AS `DataCollection_xtalSnapshotFullPath1`,
        `DataCollection`.`xtalSnapshotFullPath2` AS `DataCollection_xtalSnapshotFullPath2`,
        `DataCollection`.`xtalSnapshotFullPath3` AS `DataCollection_xtalSnapshotFullPath3`,
        `DataCollection`.`xtalSnapshotFullPath4` AS `DataCollection_xtalSnapshotFullPath4`,
        `DataCollection`.`phiStart` AS `DataCollection_phiStart`,
        `DataCollection`.`kappaStart` AS `DataCollection_kappaStart`,
        `DataCollection`.`omegaStart` AS `DataCollection_omegaStart`,
        `DataCollection`.`flux` AS `DataCollection_flux`,
        `DataCollection`.`flux_end` AS `DataCollection_flux_end`,
        `DataCollection`.`resolutionAtCorner` AS `DataCollection_resolutionAtCorner`,
        `DataCollection`.`bestWilsonPlotPath` AS `DataCollection_bestWilsonPlotPath`,
        `DataCollection`.`dataCollectionNumber` AS `DataCollection_dataCollectionNumber`,
        `DataCollection`.`axisRange` AS `DataCollection_axisRange`,
        `DataCollection`.`axisStart` AS `DataCollection_axisStart`,
        `DataCollection`.`axisEnd` AS `DataCollection_axisEnd`,
        `DataCollection`.`rotationAxis` AS `DataCollection_rotationAxis`,
        `Workflow`.`workflowTitle` AS `Workflow_workflowTitle`,
        `Workflow`.`workflowType` AS `Workflow_workflowType`,
        `Workflow`.`status` AS `Workflow_status`,
        `Workflow`.`workflowId` AS `Workflow_workflowId`,
        `v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_dataCollectionId` AS `AutoProcIntegration_dataCollectionId`,
        `v_datacollection_summary_autoprocintegration`.`autoProcScalingId` AS `autoProcScalingId`,
        `v_datacollection_summary_autoprocintegration`.`cell_a` AS `cell_a`,
        `v_datacollection_summary_autoprocintegration`.`cell_b` AS `cell_b`,
        `v_datacollection_summary_autoprocintegration`.`cell_c` AS `cell_c`,
        `v_datacollection_summary_autoprocintegration`.`cell_alpha` AS `cell_alpha`,
        `v_datacollection_summary_autoprocintegration`.`cell_beta` AS `cell_beta`,
        `v_datacollection_summary_autoprocintegration`.`cell_gamma` AS `cell_gamma`,
        `v_datacollection_summary_autoprocintegration`.`scalingStatisticsType` AS `scalingStatisticsType`,
        `v_datacollection_summary_autoprocintegration`.`resolutionLimitHigh` AS `resolutionLimitHigh`,
        `v_datacollection_summary_autoprocintegration`.`resolutionLimitLow` AS `resolutionLimitLow`,
        `v_datacollection_summary_autoprocintegration`.`completeness` AS `completeness`,
        `v_datacollection_summary_autoprocintegration`.`AutoProc_spaceGroup` AS `AutoProc_spaceGroup`,
        `v_datacollection_summary_autoprocintegration`.`autoProcId` AS `autoProcId`,
        `v_datacollection_summary_autoprocintegration`.`rMerge` AS `rMerge`,
        `v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`,
        `v_datacollection_summary_autoprocintegration`.`v_datacollection_summary_autoprocintegration_processingPrograms` AS `AutoProcProgram_processingPrograms`,
        `v_datacollection_summary_autoprocintegration`.`v_datacollection_summary_autoprocintegration_processingStatus` AS `AutoProcProgram_processingStatus`,
        v_datacollection_summary_autoprocintegration.AutoProcProgram_autoProcProgramId as AutoProcProgram_autoProcProgramId,
        `v_datacollection_summary_screening`.`Screening_screeningId` AS `Screening_screeningId`,
        `v_datacollection_summary_screening`.`Screening_dataCollectionId` AS `Screening_dataCollectionId`,
        `v_datacollection_summary_screening`.`Screening_dataCollectionGroupId` AS `Screening_dataCollectionGroupId`,
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
        `v_datacollection_summary_screening`.`ScreeningOutputLattice_unitCell_gamma` AS `ScreeningOutputLattice_unitCell_gamma`,
        `Shipping`.`shippingId` AS `shippingId`,
        `DiffractionPlan`.`diffractionPlanId` AS `diffractionPlanId`,
        `DiffractionPlan`.`experimentKind` AS `experimentKind`,
        `DiffractionPlan`.`observedResolution` AS `observedResolution`,
        `DiffractionPlan`.`minimalResolution` AS `minimalResolution`,
        `DiffractionPlan`.`exposureTime` AS `exposureTime`,
        `DiffractionPlan`.`oscillationRange` AS `oscillationRange`,
        `DiffractionPlan`.`maximalResolution` AS `maximalResolution`,
        `DiffractionPlan`.`screeningResolution` AS `screeningResolution`,
        `DiffractionPlan`.`radiationSensitivity` AS `radiationSensitivity`,
        `DiffractionPlan`.`anomalousScatterer` AS `anomalousScatterer`,
        `DiffractionPlan`.`preferredBeamSizeX` AS `preferredBeamSizeX`,
        `DiffractionPlan`.`preferredBeamSizeY` AS `preferredBeamSizeY`,
        `DiffractionPlan`.`preferredBeamDiameter` AS `preferredBeamDiameter`,
        `DiffractionPlan`.`comments` AS `DiffractipnPlan_comments`,
        `DiffractionPlan`.`aimedCompleteness` AS `aimedCompleteness`,
        `DiffractionPlan`.`aimedIOverSigmaAtHighestRes` AS `aimedIOverSigmaAtHighestRes`,
        `DiffractionPlan`.`aimedMultiplicity` AS `aimedMultiplicity`,
        `DiffractionPlan`.`aimedResolution` AS `aimedResolution`,
        `DiffractionPlan`.`anomalousData` AS `anomalousData`,
        `DiffractionPlan`.`complexity` AS `complexity`,
        `DiffractionPlan`.`estimateRadiationDamage` AS `estimateRadiationDamage`,
        `DiffractionPlan`.`forcedSpaceGroup` AS `forcedSpaceGroup`,
        `DiffractionPlan`.`requiredCompleteness` AS `requiredCompleteness`,
        `DiffractionPlan`.`requiredMultiplicity` AS `requiredMultiplicity`,
        `DiffractionPlan`.`requiredResolution` AS `requiredResolution`,
        `DiffractionPlan`.`strategyOption` AS `strategyOption`,
        `DiffractionPlan`.`kappaStrategyOption` AS `kappaStrategyOption`,
        `DiffractionPlan`.`numberOfPositions` AS `numberOfPositions`,
        `DiffractionPlan`.`minDimAccrossSpindleAxis` AS `minDimAccrossSpindleAxis`,
        `DiffractionPlan`.`maxDimAccrossSpindleAxis` AS `maxDimAccrossSpindleAxis`,
        `DiffractionPlan`.`radiationSensitivityBeta` AS `radiationSensitivityBeta`,
        `DiffractionPlan`.`radiationSensitivityGamma` AS `radiationSensitivityGamma`,
        `DiffractionPlan`.`minOscWidth` AS `minOscWidth`
    FROM
        ((((((((((((`DataCollectionGroup`
        LEFT JOIN `DataCollection` ON (((`DataCollection`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`)
            AND (`DataCollection`.`dataCollectionId` = (SELECT 
                MAX(`dc2`.`dataCollectionId`)
            FROM
                `DataCollection` `dc2`
            WHERE
                (`dc2`.`dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`))))))
        LEFT JOIN `BLSession` ON ((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`)))
        LEFT JOIN `BLSample` ON ((`BLSample`.`blSampleId` = `DataCollectionGroup`.`blSampleId`)))
        LEFT JOIN `DiffractionPlan` ON ((`DiffractionPlan`.`diffractionPlanId` = `BLSample`.`diffractionPlanId`)))
        LEFT JOIN `Container` ON ((`Container`.`containerId` = `BLSample`.`containerId`)))
        LEFT JOIN `Dewar` ON ((`Container`.`dewarId` = `Dewar`.`dewarId`)))
        LEFT JOIN `Shipping` ON ((`Shipping`.`shippingId` = `Dewar`.`shippingId`)))
        LEFT JOIN `Crystal` ON ((`Crystal`.`crystalId` = `BLSample`.`crystalId`)))
        LEFT JOIN `Protein` ON ((`Protein`.`proteinId` = `Crystal`.`proteinId`)))
        LEFT JOIN `Workflow` ON ((`DataCollectionGroup`.`workflowId` = `Workflow`.`workflowId`)))
        LEFT JOIN `v_datacollection_summary_autoprocintegration` ON ((`v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_dataCollectionId` = `DataCollection`.`dataCollectionId`)))
        LEFT JOIN `v_datacollection_summary_screening` ON ((`v_datacollection_summary_screening`.`Screening_dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`)));
      
        