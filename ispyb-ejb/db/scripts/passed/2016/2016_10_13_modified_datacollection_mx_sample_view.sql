USE `pydb`;

drop view v_datacollection;

CREATE 
    ALGORITHM = MERGE 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `v_datacollection` AS
    SELECT 
        `DataCollection`.`dataCollectionId` AS `dataCollectionId`,
        `DataCollection`.`dataCollectionGroupId` AS `dataCollectionGroupId`,
        `DataCollection`.`strategySubWedgeOrigId` AS `strategySubWedgeOrigId`,
        `DataCollection`.`detectorId` AS `detectorId`,
        `DataCollection`.`blSubSampleId` AS `blSubSampleId`,
        `DataCollection`.`dataCollectionNumber` AS `dataCollectionNumber`,
        `DataCollection`.`startTime` AS `startTime`,
        `DataCollection`.`endTime` AS `endTime`,
        `DataCollection`.`runStatus` AS `runStatus`,
        `DataCollection`.`axisStart` AS `axisStart`,
        `DataCollection`.`axisEnd` AS `axisEnd`,
        `DataCollection`.`axisRange` AS `axisRange`,
        `DataCollection`.`overlap` AS `overlap`,
        `DataCollection`.`numberOfImages` AS `numberOfImages`,
        `DataCollection`.`startImageNumber` AS `startImageNumber`,
        `DataCollection`.`numberOfPasses` AS `numberOfPasses`,
        `DataCollection`.`exposureTime` AS `exposureTime`,
        `DataCollection`.`imageDirectory` AS `imageDirectory`,
        `DataCollection`.`imagePrefix` AS `imagePrefix`,
        `DataCollection`.`imageSuffix` AS `imageSuffix`,
        `DataCollection`.`fileTemplate` AS `fileTemplate`,
        `DataCollection`.`wavelength` AS `wavelength`,
        `DataCollection`.`resolution` AS `resolution`,
        `DataCollection`.`detectorDistance` AS `detectorDistance`,
        `DataCollection`.`xBeam` AS `xBeam`,
        `DataCollection`.`yBeam` AS `yBeam`,
        `DataCollection`.`xBeamPix` AS `xBeamPix`,
        `DataCollection`.`yBeamPix` AS `yBeamPix`,
        `DataCollection`.`comments` AS `comments`,
        `DataCollection`.`printableForReport` AS `printableForReport`,
        `DataCollection`.`slitGapVertical` AS `slitGapVertical`,
        `DataCollection`.`slitGapHorizontal` AS `slitGapHorizontal`,
        `DataCollection`.`transmission` AS `transmission`,
        `DataCollection`.`synchrotronMode` AS `synchrotronMode`,
        `DataCollection`.`xtalSnapshotFullPath1` AS `xtalSnapshotFullPath1`,
        `DataCollection`.`xtalSnapshotFullPath2` AS `xtalSnapshotFullPath2`,
        `DataCollection`.`xtalSnapshotFullPath3` AS `xtalSnapshotFullPath3`,
        `DataCollection`.`xtalSnapshotFullPath4` AS `xtalSnapshotFullPath4`,
        `DataCollection`.`rotationAxis` AS `rotationAxis`,
        `DataCollection`.`phiStart` AS `phiStart`,
        `DataCollection`.`kappaStart` AS `kappaStart`,
        `DataCollection`.`omegaStart` AS `omegaStart`,
        `DataCollection`.`resolutionAtCorner` AS `resolutionAtCorner`,
        `DataCollection`.`detector2Theta` AS `detector2Theta`,
        `DataCollection`.`undulatorGap1` AS `undulatorGap1`,
        `DataCollection`.`undulatorGap2` AS `undulatorGap2`,
        `DataCollection`.`undulatorGap3` AS `undulatorGap3`,
        `DataCollection`.`beamSizeAtSampleX` AS `beamSizeAtSampleX`,
        `DataCollection`.`beamSizeAtSampleY` AS `beamSizeAtSampleY`,
        `DataCollection`.`centeringMethod` AS `centeringMethod`,
        `DataCollection`.`averageTemperature` AS `averageTemperature`,
        `DataCollection`.`actualCenteringPosition` AS `actualCenteringPosition`,
        `DataCollection`.`beamShape` AS `beamShape`,
        `DataCollection`.`flux` AS `flux`,
        `DataCollection`.`flux_end` AS `flux_end`,
        `DataCollection`.`totalAbsorbedDose` AS `totalAbsorbedDose`,
        `DataCollection`.`bestWilsonPlotPath` AS `bestWilsonPlotPath`,
        `DataCollection`.`imageQualityIndicatorsPlotPath` AS `imageQualityIndicatorsPlotPath`,
        `DataCollection`.`imageQualityIndicatorsCSVPath` AS `imageQualityIndicatorsCSVPath`,
        `BLSession`.`sessionId` AS `sessionId`,
        `BLSession`.`proposalId` AS `proposalId`,
        `DataCollectionGroup`.`workflowId` AS `workflowId`,
        `ScreeningOutput`.`rankingResolution` AS `rankingResolution`,
        `WorkflowMesh`.`value1` AS `WorkflowMesh_value1`,
        `WorkflowMesh`.`value2` AS `WorkflowMesh_value2`,
        `WorkflowMesh`.`value3` AS `WorkflowMesh_value3`,
        `WorkflowMesh`.`value4` AS `WorkflowMesh_value4`
    FROM
        ((((((`DataCollection`
        LEFT JOIN `DataCollectionGroup` ON ((`DataCollectionGroup`.`dataCollectionGroupId` = `DataCollection`.`dataCollectionGroupId`)))
        LEFT JOIN `Workflow` ON ((`DataCollectionGroup`.`workflowId` = `Workflow`.`workflowId`)))
        LEFT JOIN `WorkflowMesh` ON ((`WorkflowMesh`.`workflowId` = `Workflow`.`workflowId`)))
        LEFT JOIN `BLSession` ON ((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`)))
        LEFT JOIN `Screening` ON ((`Screening`.`dataCollectionId` = `DataCollection`.`dataCollectionId`)))
        LEFT JOIN `ScreeningOutput` ON ((`ScreeningOutput`.`screeningId` = `Screening`.`screeningId`)));
        
CREATE 
    ALGORITHM = MERGE 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `v_mx_sample` AS
    SELECT 
        `BLSample`.`blSampleId` AS `BLSample_blSampleId`,
        `BLSample`.`diffractionPlanId` AS `BLSample_diffractionPlanId`,
        `BLSample`.`crystalId` AS `BLSample_crystalId`,
        `BLSample`.`containerId` AS `BLSample_containerId`,
        `BLSample`.`name` AS `BLSample_name`,
        `BLSample`.`code` AS `BLSample_code`,
        `BLSample`.`location` AS `BLSample_location`,
        `BLSample`.`holderLength` AS `BLSample_holderLength`,
        `BLSample`.`loopLength` AS `BLSample_loopLength`,
        `BLSample`.`loopType` AS `BLSample_loopType`,
        `BLSample`.`wireWidth` AS `BLSample_wireWidth`,
        `BLSample`.`comments` AS `BLSample_comments`,
        `BLSample`.`completionStage` AS `BLSample_completionStage`,
        `BLSample`.`structureStage` AS `BLSample_structureStage`,
        `BLSample`.`publicationStage` AS `BLSample_publicationStage`,
        `BLSample`.`publicationComments` AS `BLSample_publicationComments`,
        `BLSample`.`blSampleStatus` AS `BLSample_blSampleStatus`,
        `BLSample`.`isInSampleChanger` AS `BLSample_isInSampleChanger`,
        `BLSample`.`lastKnownCenteringPosition` AS `BLSample_lastKnownCenteringPosition`,
        `BLSample`.`recordTimeStamp` AS `BLSample_recordTimeStamp`,
        `BLSample`.`SMILES` AS `BLSample_SMILES`,
        `Protein`.`proteinId` AS `Protein_proteinId`,
        `Protein`.`name` AS `Protein_name`,
        `Protein`.`acronym` AS `Protein_acronym`,
        `Protein`.`proteinType` AS `Protein_proteinType`,
        `Protein`.`proposalId` AS `Protein_proposalId`,
        `Person`.`personId` AS `Person_personId`,
        `Person`.`familyName` AS `Person_familyName`,
        `Person`.`givenName` AS `Person_givenName`,
        `Person`.`emailAddress` AS `Person_emailAddress`,
        `Container`.`containerId` AS `Container_containerId`,
        `Container`.`code` AS `Container_code`,
        `Container`.`containerType` AS `Container_containerType`,
        `Container`.`containerStatus` AS `Container_containerStatus`,
        `Dewar`.`code` AS `Dewar_code`,
        `Dewar`.`dewarId` AS `Dewar_dewarId`,
        `Dewar`.`storageLocation` AS `Dewar_storageLocation`,
        `Dewar`.`dewarStatus` AS `Dewar_dewarStatus`,
        `Dewar`.`barCode` AS `Dewar_barCode`,
        `Shipping`.`shippingId` AS `Shipping_shippingId`
    FROM
        ((((((`BLSample`
        LEFT JOIN `Crystal` ON ((`Crystal`.`crystalId` = `BLSample`.`crystalId`)))
        LEFT JOIN `Protein` ON ((`Protein`.`proteinId` = `Crystal`.`proteinId`)))
        LEFT JOIN `Person` ON ((`Person`.`personId` = `Protein`.`personId`)))
        LEFT JOIN `Container` ON ((`BLSample`.`containerId` = `Container`.`containerId`)))
        LEFT JOIN `Dewar` ON ((`Dewar`.`dewarId` = `Container`.`dewarId`)))
        LEFT JOIN `Shipping` ON ((`Dewar`.`shippingId` = `Shipping`.`shippingId`)));
        
drop view v_datacollection_summary;
CREATE 
    ALGORITHM = MERGE 
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
        `BLSample`.`location` AS `BLSample_location`,
        `BLSample`.`blSampleStatus` AS `BLSample_blSampleStatus`,
        `BLSample`.`comments` AS `BLSample_comments`,
        `Container`.`containerId` AS `Container_containerId`,
        `BLSession`.`sessionId` AS `BLSession_sessionId`,
        `BLSession`.`proposalId` AS `BLSession_proposalId`,
        `BLSession`.`protectedData` AS `BLSession_protectedData`,
        `Dewar`.`dewarId` AS `Dewar_dewarId`,
        `Dewar`.`code` AS `Dewar_code`,
        `Dewar`.`storageLocation` AS `Dewar_storageLocation`,
        `Container`.`containerType` AS `Container_containerType`,
        `Container`.`capacity` AS `Container_capacity`,
        `Container`.`beamlineLocation` AS `Container_beamlineLocation`,
        `Container`.`sampleChangerLocation` AS `Container_sampleChangerLocation`,
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
        `v_datacollection_summary_autoprocintegration`.`AutoProcProgram_autoProcProgramId` AS `AutoProcProgram_autoProcProgramId`,
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
        `Shipping`.`shippingId` AS `Shipping_shippingId`,
        `Shipping`.`shippingName` AS `Shipping_shippingName`,
        `Shipping`.`shippingStatus` AS `Shipping_shippingStatus`,
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
        LEFT JOIN `v_datacollection_summary_screening` ON ((`v_datacollection_summary_screening`.`Screening_dataCollectionGroupId` = `DataCollectionGroup`.`dataCollectionGroupId`)))

        