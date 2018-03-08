

use pydb;
insert into SchemaStatus (scriptName, schemaStatus) values ('217_12_19_RankingResolution.sql','ONGOING');
CREATE or REPLACE
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
        `v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_autoProcIntegrationId` AS `AutoProcIntegration_autoProcIntegrationId`,
        `v_datacollection_summary_autoprocintegration`.`v_datacollection_summary_autoprocintegration_processingPrograms` AS `AutoProcProgram_processingPrograms`,
        `v_datacollection_summary_autoprocintegration`.`v_datacollection_summary_autoprocintegration_processingStatus` AS `AutoProcProgram_processingStatus`,
        `v_datacollection_summary_autoprocintegration`.`AutoProcProgram_autoProcProgramId` AS `AutoProcProgram_autoProcProgramId`,
        `ScreeningOutput`.`rankingResolution` AS `ScreeningOutput_rankingResolution`
    FROM
        ((((((`DataCollection`
        LEFT JOIN `DataCollectionGroup` ON ((`DataCollectionGroup`.`dataCollectionGroupId` = `DataCollection`.`dataCollectionGroupId`)))
        LEFT JOIN `Screening` ON ((`Screening`.`dataCollectionGroupId` = `DataCollection`.`dataCollectionGroupId`)))
        LEFT JOIN `ScreeningOutput` ON ((`Screening`.`screeningId` = `ScreeningOutput`.`screeningId`)))
        LEFT JOIN `Workflow` ON ((`DataCollectionGroup`.`workflowId` = `Workflow`.`workflowId`)))
        LEFT JOIN `BLSession` ON ((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`)))
        LEFT JOIN `v_datacollection_summary_autoprocintegration` ON ((`v_datacollection_summary_autoprocintegration`.`AutoProcIntegration_dataCollectionId` = `DataCollection`.`dataCollectionId`)));
 
update SchemaStatus set schemaStatus = 'DONE' where scriptName = '217_12_19_RankingResolution.sql';
