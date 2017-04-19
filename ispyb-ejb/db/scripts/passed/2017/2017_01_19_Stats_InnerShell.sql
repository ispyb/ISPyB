insert into SchemaStatus (scriptName, schemaStatus) values ('2017_01_19_Stats_InnerShell.sql','ONGOING');

drop view if exists v_mx_autoprocessing_stats;
CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `v_mx_autoprocessing_stats` AS
    SELECT 
        `AutoProcScalingStatistics`.`autoProcScalingStatisticsId` AS `autoProcScalingStatisticsId`,
        `AutoProcScalingStatistics`.`autoProcScalingId` AS `autoProcScalingId`,
        `AutoProcScalingStatistics`.`scalingStatisticsType` AS `scalingStatisticsType`,
        `AutoProcScalingStatistics`.`resolutionLimitLow` AS `resolutionLimitLow`,
        `AutoProcScalingStatistics`.`resolutionLimitHigh` AS `resolutionLimitHigh`,
        `AutoProcScalingStatistics`.`rMerge` AS `rMerge`,
        `AutoProcScalingStatistics`.`rMeasWithinIPlusIMinus` AS `rMeasWithinIPlusIMinus`,
        `AutoProcScalingStatistics`.`rMeasAllIPlusIMinus` AS `rMeasAllIPlusIMinus`,
        `AutoProcScalingStatistics`.`rPimWithinIPlusIMinus` AS `rPimWithinIPlusIMinus`,
        `AutoProcScalingStatistics`.`rPimAllIPlusIMinus` AS `rPimAllIPlusIMinus`,
        `AutoProcScalingStatistics`.`fractionalPartialBias` AS `fractionalPartialBias`,
        `AutoProcScalingStatistics`.`nTotalObservations` AS `nTotalObservations`,
        `AutoProcScalingStatistics`.`nTotalUniqueObservations` AS `nTotalUniqueObservations`,
        `AutoProcScalingStatistics`.`meanIOverSigI` AS `meanIOverSigI`,
        `AutoProcScalingStatistics`.`completeness` AS `completeness`,
        `AutoProcScalingStatistics`.`multiplicity` AS `multiplicity`,
        `AutoProcScalingStatistics`.`anomalousCompleteness` AS `anomalousCompleteness`,
        `AutoProcScalingStatistics`.`anomalousMultiplicity` AS `anomalousMultiplicity`,
        `AutoProcScalingStatistics`.`recordTimeStamp` AS `recordTimeStamp`,
        `AutoProcScalingStatistics`.`anomalous` AS `anomalous`,
        `AutoProcScalingStatistics`.`ccHalf` AS `ccHalf`,
        `AutoProcScalingStatistics`.`ccAno` AS `ccAno`,
        `AutoProcScalingStatistics`.`sigAno` AS `sigAno`,
        `DataCollection`.`dataCollectionId` AS `dataCollectionId`,
        `DataCollection`.`strategySubWedgeOrigId` AS `strategySubWedgeOrigId`,
        `DataCollection`.`detectorId` AS `detectorId`,
        `DataCollection`.`blSubSampleId` AS `blSubSampleId`,
        `DataCollection`.`dataCollectionNumber` AS `dataCollectionNumber`,
        `DataCollection`.`startTime` AS `startTime`,
        `DataCollection`.`endTime` AS `endTime`,
        `BLSession`.`sessionId` AS `sessionId`,
        `BLSession`.`proposalId` AS `proposalId`,
        `BLSession`.`beamLineName` AS `beamLineName`
    FROM
        ((((((`AutoProcScalingStatistics`
        LEFT JOIN `AutoProcScaling` ON ((`AutoProcScalingStatistics`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`)))
        LEFT JOIN `AutoProcScaling_has_Int` ON ((`AutoProcScaling_has_Int`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`)))
        LEFT JOIN `AutoProcIntegration` ON ((`AutoProcIntegration`.`autoProcIntegrationId` = `AutoProcScaling_has_Int`.`autoProcIntegrationId`)))
        LEFT JOIN `DataCollection` ON ((`DataCollection`.`dataCollectionId` = `AutoProcIntegration`.`dataCollectionId`)))
        LEFT JOIN `DataCollectionGroup` ON ((`DataCollectionGroup`.`dataCollectionGroupId` = `DataCollection`.`dataCollectionGroupId`)))
        LEFT JOIN `BLSession` ON ((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`)));
        
update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_01_19_Stats_InnerShell.sql'; 
