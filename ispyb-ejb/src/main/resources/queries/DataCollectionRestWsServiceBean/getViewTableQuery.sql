select 
*,
GROUP_CONCAT(ifnull(AutoProcProgram_processingPrograms,'null') SEPARATOR ', ') AS `processingPrograms`,
GROUP_CONCAT(ifnull(AutoProcProgram_processingStatus,'null') SEPARATOR ', ') AS `processingStatus`,
GROUP_CONCAT(ifnull(AutoProcIntegration_autoProcIntegrationId,'null') SEPARATOR ', ') AS `autoProcIntegrationId`,
GROUP_CONCAT(ifnull(cell_a,'null') SEPARATOR ', ') AS `Autoprocessing_cell_a`,
GROUP_CONCAT(ifnull(cell_b,'null') SEPARATOR ', ') AS `Autoprocessing_cell_b`,
GROUP_CONCAT(ifnull(cell_c,'null') SEPARATOR ', ') AS `Autoprocessing_cell_c`,
GROUP_CONCAT(ifnull(cell_alpha,'null') SEPARATOR ', ') AS `Autoprocessing_cell_alpha`,
GROUP_CONCAT(ifnull(cell_beta,'null') SEPARATOR ', ') AS `Autoprocessing_cell_beta`,
GROUP_CONCAT(ifnull(cell_gamma,'null') SEPARATOR ', ') AS `Autoprocessing_cell_gamma`,
GROUP_CONCAT(ifnull(anomalous,'null') SEPARATOR ', ') AS `Autoprocessing_anomalous`,
GROUP_CONCAT(ifnull(autoProcId,'null') SEPARATOR ', ') AS `autoProcIds`,
GROUP_CONCAT(ifnull(scalingStatisticsType,'null') SEPARATOR ', ') AS `scalingStatisticsTypes`,
GROUP_CONCAT(ifnull(resolutionLimitHigh,'null') SEPARATOR ', ') AS `resolutionsLimitHigh`,
GROUP_CONCAT(ifnull(resolutionLimitLow,'null') SEPARATOR ', ') AS `resolutionsLimitLow`,
GROUP_CONCAT(ifnull(completeness,'null') SEPARATOR ', ') AS `completenessList`,
GROUP_CONCAT(ifnull(AutoProc_spaceGroup,'null') SEPARATOR ', ') AS `AutoProc_spaceGroups`,
GROUP_CONCAT(ifnull(meanIOverSigI,'null') SEPARATOR ', ') AS `meanIOverSigIList`,
GROUP_CONCAT(ifnull(rMerge,'null') SEPARATOR ', ') AS `rMerges`,
GROUP_CONCAT(ifnull(ccHalf,'null') SEPARATOR ', ') AS `ccHalfList`,

(SELECT count(*) 
	FROM `PhasingStep`    
    LEFT JOIN `AutoProcScaling` ON `AutoProcScaling`.`autoProcScalingId` = `PhasingStep`.`autoProcScalingId`     
	LEFT JOIN `AutoProcScaling_has_Int` ON `AutoProcScaling_has_Int`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`  
    LEFT JOIN `AutoProcIntegration` ON `AutoProcIntegration`.`autoProcIntegrationId` = `AutoProcScaling_has_Int`.`autoProcIntegrationId` 
	LEFT JOIN `DataCollection` ON `DataCollection`.`dataCollectionId` = `AutoProcIntegration`.`dataCollectionId` 
	where `DataCollection`.`dataCollectionId` = v_datacollection.dataCollectionId
) as hasPhasing,
(SELECT GROUP_CONCAT(value1) FROM WorkflowMesh
	LEFT JOIN `Workflow` ON `Workflow`.`workflowId` = `WorkflowMesh`.`workflowId`   
    LEFT JOIN `DataCollectionGroup` ON `Workflow`.`workflowId` = `DataCollectionGroup`.`workflowId`   
	where `DataCollectionGroup`.`dataCollectionGroupId` = v_datacollection.dataCollectionGroupId
) as value1,
(SELECT GROUP_CONCAT(value2) FROM WorkflowMesh
	LEFT JOIN `Workflow` ON `Workflow`.`workflowId` = `WorkflowMesh`.`workflowId`   
    LEFT JOIN `DataCollectionGroup` ON `Workflow`.`workflowId` = `DataCollectionGroup`.`workflowId`   
	where `DataCollectionGroup`.`dataCollectionGroupId` = v_datacollection.dataCollectionGroupId
) as value2,
(SELECT GROUP_CONCAT(value3) FROM WorkflowMesh
	LEFT JOIN `Workflow` ON `Workflow`.`workflowId` = `WorkflowMesh`.`workflowId`   
    LEFT JOIN `DataCollectionGroup` ON `Workflow`.`workflowId` = `DataCollectionGroup`.`workflowId`   
	where `DataCollectionGroup`.`dataCollectionGroupId` = v_datacollection.dataCollectionGroupId
) as value3,
(SELECT GROUP_CONCAT(value4) FROM WorkflowMesh
	LEFT JOIN `Workflow` ON `Workflow`.`workflowId` = `WorkflowMesh`.`workflowId`   
    LEFT JOIN `DataCollectionGroup` ON `Workflow`.`workflowId` = `DataCollectionGroup`.`workflowId`   
	where `DataCollectionGroup`.`dataCollectionGroupId` = v_datacollection.dataCollectionGroupId
) as value4,
(select count(*) from AutoProcIntegration where AutoProcIntegration.dataCollectionId = v_datacollection.dataCollectionId) as hasAutoProcessing
 from v_datacollection 
 