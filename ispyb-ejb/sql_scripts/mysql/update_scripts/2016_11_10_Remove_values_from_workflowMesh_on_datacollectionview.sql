USE `pydb`;

drop view v_datacollection;

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
        `pydb`.`ScreeningOutput`.`rankingResolution` AS `rankingResolution`
        #`pydb`.`WorkflowMesh`.`value1` AS `WorkflowMesh_value1`,
        #`pydb`.`WorkflowMesh`.`value2` AS `WorkflowMesh_value2`,
        #`pydb`.`WorkflowMesh`.`value3` AS `WorkflowMesh_value3`,
        #`pydb`.`WorkflowMesh`.`value4` AS `WorkflowMesh_value4`
    FROM
        (((((`pydb`.`DataCollection`
        LEFT JOIN `pydb`.`DataCollectionGroup` ON ((`pydb`.`DataCollectionGroup`.`dataCollectionGroupId` = `pydb`.`DataCollection`.`dataCollectionGroupId`)))
        LEFT JOIN `pydb`.`Workflow` ON ((`pydb`.`DataCollectionGroup`.`workflowId` = `pydb`.`Workflow`.`workflowId`)))
        #LEFT JOIN `pydb`.`WorkflowMesh` ON ((`pydb`.`WorkflowMesh`.`workflowId` = `pydb`.`Workflow`.`workflowId`)))
        LEFT JOIN `pydb`.`BLSession` ON ((`pydb`.`BLSession`.`sessionId` = `pydb`.`DataCollectionGroup`.`sessionId`)))
        LEFT JOIN `pydb`.`Screening` ON ((`pydb`.`Screening`.`dataCollectionId` = `pydb`.`DataCollection`.`dataCollectionId`)))
        LEFT JOIN `pydb`.`ScreeningOutput` ON ((`pydb`.`ScreeningOutput`.`screeningId` = `pydb`.`Screening`.`screeningId`)));
