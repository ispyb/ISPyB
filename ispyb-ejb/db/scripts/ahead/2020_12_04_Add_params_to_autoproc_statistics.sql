INSERT IGNORE INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2020_12_04_Add_params_to_autoproc_statistics.sql', 'ONGOING');

-- database changes there

USE `pydb`;
CREATE
     OR REPLACE ALGORITHM = MERGE
    DEFINER = `pxadmin`@`%`
    SQL SECURITY DEFINER
VIEW `pydb`.`v_datacollection_summary_autoprocintegration` AS
    SELECT
        `pydb`.`AutoProcIntegration`.`dataCollectionId` AS `AutoProcIntegration_dataCollectionId`,
        `pydb`.`AutoProcIntegration`.`cell_a` AS `cell_a`,
        `pydb`.`AutoProcIntegration`.`cell_b` AS `cell_b`,
        `pydb`.`AutoProcIntegration`.`cell_c` AS `cell_c`,
        `pydb`.`AutoProcIntegration`.`cell_alpha` AS `cell_alpha`,
        `pydb`.`AutoProcIntegration`.`cell_beta` AS `cell_beta`,
        `pydb`.`AutoProcIntegration`.`cell_gamma` AS `cell_gamma`,
        `pydb`.`AutoProcIntegration`.`anomalous` AS `anomalous`,
        `pydb`.`AutoProcIntegration`.`autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`,
        `pydb`.`AutoProcProgram`.`processingPrograms` AS `v_datacollection_summary_autoprocintegration_processingPrograms`,
        `pydb`.`AutoProcProgram`.`autoProcProgramId` AS `AutoProcProgram_autoProcProgramId`,
        `pydb`.`AutoProcProgram`.`processingStatus` AS `v_datacollection_summary_autoprocintegration_processingStatus`,
        `pydb`.`AutoProcIntegration`.`dataCollectionId` AS `AutoProcIntegration_phasing_dataCollectionId`,
        `pydb`.`PhasingStep`.`phasingStepType` AS `PhasingStep_phasing_phasingStepType`,
        `pydb`.`SpaceGroup`.`spaceGroupShortName` AS `SpaceGroup_spaceGroupShortName`,
        `pydb`.`AutoProc`.`autoProcId` AS `autoProcId`,
        `pydb`.`AutoProc`.`spaceGroup` AS `AutoProc_spaceGroup`,
        `pydb`.`AutoProcScalingStatistics`.`scalingStatisticsType` AS `scalingStatisticsType`,
        `pydb`.`AutoProcScalingStatistics`.`resolutionLimitHigh` AS `resolutionLimitHigh`,
        `pydb`.`AutoProcScalingStatistics`.`resolutionLimitLow` AS `resolutionLimitLow`,
        `pydb`.`AutoProcScalingStatistics`.`rMerge` AS `rMerge`,
        `pydb`.`AutoProcScalingStatistics`.`meanIOverSigI` AS `meanIOverSigI`,
        `pydb`.`AutoProcScalingStatistics`.`ccHalf` AS `ccHalf`,
        `pydb`.`AutoProcScalingStatistics`.`completeness` AS `completeness`,
        `pydb`.`AutoProcScaling`.`autoProcScalingId` AS `autoProcScalingId`
    FROM
        ((((((((`pydb`.`AutoProcIntegration`
        LEFT JOIN `pydb`.`AutoProcProgram` ON ((`pydb`.`AutoProcIntegration`.`autoProcProgramId` = `pydb`.`AutoProcProgram`.`autoProcProgramId`)))
        LEFT JOIN `pydb`.`AutoProcScaling_has_Int` ON ((`pydb`.`AutoProcScaling_has_Int`.`autoProcIntegrationId` = `pydb`.`AutoProcIntegration`.`autoProcIntegrationId`)))
        LEFT JOIN `pydb`.`AutoProcScaling` ON ((`pydb`.`AutoProcScaling`.`autoProcScalingId` = `pydb`.`AutoProcScaling_has_Int`.`autoProcScalingId`)))
        LEFT JOIN `pydb`.`AutoProcScalingStatistics` ON ((`pydb`.`AutoProcScaling`.`autoProcScalingId` = `pydb`.`AutoProcScalingStatistics`.`autoProcScalingId`)))
        LEFT JOIN `pydb`.`AutoProc` ON ((`pydb`.`AutoProc`.`autoProcId` = `pydb`.`AutoProcScaling`.`autoProcId`)))
        LEFT JOIN `pydb`.`Phasing_has_Scaling` ON ((`pydb`.`Phasing_has_Scaling`.`autoProcScalingId` = `pydb`.`AutoProcScaling`.`autoProcScalingId`)))
        LEFT JOIN `pydb`.`PhasingStep` ON ((`pydb`.`PhasingStep`.`autoProcScalingId` = `pydb`.`Phasing_has_Scaling`.`autoProcScalingId`)))
        LEFT JOIN `pydb`.`SpaceGroup` ON ((`pydb`.`SpaceGroup`.`spaceGroupId` = `pydb`.`PhasingStep`.`spaceGroupId`)));


USE `pydb`;
CREATE
     OR REPLACE ALGORITHM = MERGE
    DEFINER = `pxadmin`@`%`
    SQL SECURITY DEFINER
VIEW `pydb`.`v_datacollection` AS
    SELECT
        `pydb`.`DataCollection`.`dataCollectionId` AS `dataCollectionId`,
        `pydb`.`DataCollection`.`dataCollectionGroupId` AS `dataCollectionGroupId`,
        `pydb`.`DataCollection`.`strategySubWedgeOrigId` AS `strategySubWedgeOrigId`,
        `pydb`.`DataCollection`.`detectorId` AS `detectorId`,
        `pydb`.`DataCollection`.`blSubSampleId` AS `blSubSampleId`,
        `pydb`.`DataCollection`.`dataCollectionNumber` AS `dataCollectionNumber`,
        `pydb`.`DataCollection`.`startTime` AS `startTime`,
        `pydb`.`DataCollection`.`endTime` AS `endTime`,
        `pydb`.`DataCollection`.`runStatus` AS `runStatus`,
        `pydb`.`DataCollection`.`axisStart` AS `axisStart`,
        `pydb`.`DataCollection`.`axisEnd` AS `axisEnd`,
        `pydb`.`DataCollection`.`axisRange` AS `axisRange`,
        `pydb`.`DataCollection`.`overlap` AS `overlap`,
        `pydb`.`DataCollection`.`numberOfImages` AS `numberOfImages`,
        `pydb`.`DataCollection`.`startImageNumber` AS `startImageNumber`,
        `pydb`.`DataCollection`.`numberOfPasses` AS `numberOfPasses`,
        `pydb`.`DataCollection`.`exposureTime` AS `exposureTime`,
        `pydb`.`DataCollection`.`imageDirectory` AS `imageDirectory`,
        `pydb`.`DataCollection`.`imagePrefix` AS `imagePrefix`,
        `pydb`.`DataCollection`.`imageSuffix` AS `imageSuffix`,
        `pydb`.`DataCollection`.`fileTemplate` AS `fileTemplate`,
        `pydb`.`DataCollection`.`wavelength` AS `wavelength`,
        `pydb`.`DataCollection`.`resolution` AS `resolution`,
        `pydb`.`DataCollection`.`detectorDistance` AS `detectorDistance`,
        `pydb`.`DataCollection`.`xBeam` AS `xBeam`,
        `pydb`.`DataCollection`.`yBeam` AS `yBeam`,
        `pydb`.`DataCollection`.`xBeamPix` AS `xBeamPix`,
        `pydb`.`DataCollection`.`yBeamPix` AS `yBeamPix`,
        `pydb`.`DataCollection`.`comments` AS `comments`,
        `pydb`.`DataCollection`.`printableForReport` AS `printableForReport`,
        `pydb`.`DataCollection`.`slitGapVertical` AS `slitGapVertical`,
        `pydb`.`DataCollection`.`slitGapHorizontal` AS `slitGapHorizontal`,
        `pydb`.`DataCollection`.`transmission` AS `transmission`,
        `pydb`.`DataCollection`.`synchrotronMode` AS `synchrotronMode`,
        `pydb`.`DataCollection`.`xtalSnapshotFullPath1` AS `xtalSnapshotFullPath1`,
        `pydb`.`DataCollection`.`xtalSnapshotFullPath2` AS `xtalSnapshotFullPath2`,
        `pydb`.`DataCollection`.`xtalSnapshotFullPath3` AS `xtalSnapshotFullPath3`,
        `pydb`.`DataCollection`.`xtalSnapshotFullPath4` AS `xtalSnapshotFullPath4`,
        `pydb`.`DataCollection`.`rotationAxis` AS `rotationAxis`,
        `pydb`.`DataCollection`.`phiStart` AS `phiStart`,
        `pydb`.`DataCollection`.`kappaStart` AS `kappaStart`,
        `pydb`.`DataCollection`.`omegaStart` AS `omegaStart`,
        `pydb`.`DataCollection`.`resolutionAtCorner` AS `resolutionAtCorner`,
        `pydb`.`DataCollection`.`detector2Theta` AS `detector2Theta`,
        `pydb`.`DataCollection`.`undulatorGap1` AS `undulatorGap1`,
        `pydb`.`DataCollection`.`undulatorGap2` AS `undulatorGap2`,
        `pydb`.`DataCollection`.`undulatorGap3` AS `undulatorGap3`,
        `pydb`.`DataCollection`.`beamSizeAtSampleX` AS `beamSizeAtSampleX`,
        `pydb`.`DataCollection`.`beamSizeAtSampleY` AS `beamSizeAtSampleY`,
        `pydb`.`DataCollection`.`centeringMethod` AS `centeringMethod`,
        `pydb`.`DataCollection`.`averageTemperature` AS `averageTemperature`,
        `pydb`.`DataCollection`.`actualCenteringPosition` AS `actualCenteringPosition`,
        `pydb`.`DataCollection`.`beamShape` AS `beamShape`,
        `pydb`.`DataCollection`.`flux` AS `flux`,
        `pydb`.`DataCollection`.`flux_end` AS `flux_end`,
        `pydb`.`DataCollection`.`totalAbsorbedDose` AS `totalAbsorbedDose`,
        `pydb`.`DataCollection`.`bestWilsonPlotPath` AS `bestWilsonPlotPath`,
        `pydb`.`DataCollection`.`imageQualityIndicatorsPlotPath` AS `imageQualityIndicatorsPlotPath`,
        `pydb`.`DataCollection`.`imageQualityIndicatorsCSVPath` AS `imageQualityIndicatorsCSVPath`,
        `pydb`.`BLSession`.`sessionId` AS `sessionId`,
        `pydb`.`BLSession`.`proposalId` AS `proposalId`,
        `pydb`.`DataCollectionGroup`.`workflowId` AS `workflowId`,
        `v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_dataCollectionId` AS `AutoProcIntegration_dataCollectionId`,
        `v_datacollection_summary_autoprocintegration`.`autoProcScalingId` AS `autoProcScalingId`,
        `v_datacollection_summary_autoprocintegration`.`cell_a` AS `cell_a`,
        `v_datacollection_summary_autoprocintegration`.`cell_b` AS `cell_b`,
        `v_datacollection_summary_autoprocintegration`.`cell_c` AS `cell_c`,
        `v_datacollection_summary_autoprocintegration`.`cell_alpha` AS `cell_alpha`,
        `v_datacollection_summary_autoprocintegration`.`cell_beta` AS `cell_beta`,
        `v_datacollection_summary_autoprocintegration`.`cell_gamma` AS `cell_gamma`,
        `v_datacollection_summary_autoprocintegration`.`anomalous` AS `anomalous`,
        `v_datacollection_summary_autoprocintegration`.`scalingStatisticsType` AS `scalingStatisticsType`,
        `v_datacollection_summary_autoprocintegration`.`resolutionLimitHigh` AS `resolutionLimitHigh`,
        `v_datacollection_summary_autoprocintegration`.`resolutionLimitLow` AS `resolutionLimitLow`,
        `v_datacollection_summary_autoprocintegration`.`completeness` AS `completeness`,
        `v_datacollection_summary_autoprocintegration`.`AutoProc_spaceGroup` AS `AutoProc_spaceGroup`,
        `v_datacollection_summary_autoprocintegration`.`autoProcId` AS `autoProcId`,
        `v_datacollection_summary_autoprocintegration`.`rMerge` AS `rMerge`,
        `v_datacollection_summary_autoprocintegration`.`ccHalf` AS `ccHalf`,
        `v_datacollection_summary_autoprocintegration`.`meanIOverSigI` AS `meanIOverSigI`,
        `v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`,
        `v_datacollection_summary_autoprocintegration`.`v_datacollection_summary_autoprocintegration_processingPrograms` AS `AutoProcProgram_processingPrograms`,
        `v_datacollection_summary_autoprocintegration`.`v_datacollection_summary_autoprocintegration_processingStatus` AS `AutoProcProgram_processingStatus`,
        `v_datacollection_summary_autoprocintegration`.`AutoProcProgram_autoProcProgramId` AS `AutoProcProgram_autoProcProgramId`,
        `pydb`.`ScreeningOutput`.`rankingResolution` AS `ScreeningOutput_rankingResolution`
    FROM
        ((((((`pydb`.`DataCollection`
        LEFT JOIN `pydb`.`DataCollectionGroup` ON ((`pydb`.`DataCollectionGroup`.`dataCollectionGroupId` = `pydb`.`DataCollection`.`dataCollectionGroupId`)))
        LEFT JOIN `pydb`.`Screening` ON ((`pydb`.`Screening`.`dataCollectionGroupId` = `pydb`.`DataCollection`.`dataCollectionGroupId`)))
        LEFT JOIN `pydb`.`ScreeningOutput` ON ((`pydb`.`Screening`.`screeningId` = `pydb`.`ScreeningOutput`.`screeningId`)))
        LEFT JOIN `pydb`.`Workflow` ON ((`pydb`.`DataCollectionGroup`.`workflowId` = `pydb`.`Workflow`.`workflowId`)))
        LEFT JOIN `pydb`.`BLSession` ON ((`pydb`.`BLSession`.`sessionId` = `pydb`.`DataCollectionGroup`.`sessionId`)))
        LEFT JOIN `pydb`.`v_datacollection_summary_autoprocintegration` ON ((`v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_dataCollectionId` = `pydb`.`DataCollection`.`dataCollectionId`)));


UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2020_12_04_Add_params_to_autoproc_statistics.sql';
