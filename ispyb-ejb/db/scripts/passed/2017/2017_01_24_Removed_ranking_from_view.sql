

-- first line of script
insert into SchemaStatus (scriptName, schemaStatus) values ('2017_01_24_Removed_ranking_from_view.sql','ONGOING');
USE `pydb`;

drop view if exists v_datacollection;

CREATE 
     OR REPLACE ALGORITHM = MERGE 
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
        `DataCollectionGroup`.`workflowId` AS `workflowId`
    FROM
        (((`DataCollection`
        LEFT JOIN `DataCollectionGroup` ON ((`DataCollectionGroup`.`dataCollectionGroupId` = `DataCollection`.`dataCollectionGroupId`)))
        LEFT JOIN `Workflow` ON ((`DataCollectionGroup`.`workflowId` = `Workflow`.`workflowId`)))
        LEFT JOIN `BLSession` ON ((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`)));
        
          -- last line of script  
  update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_01_24_Removed_ranking_from_view.sql'; 
  
  
 
