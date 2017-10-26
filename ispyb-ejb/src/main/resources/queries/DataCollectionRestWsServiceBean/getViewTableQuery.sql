select 
*,
GROUP_CONCAT(`AutoProcProgram_processingPrograms` SEPARATOR ', ') AS `processingPrograms`,
GROUP_CONCAT(`AutoProcProgram_processingStatus` SEPARATOR ', ') AS `processingStatus`,
GROUP_CONCAT(`AutoProcIntegration_autoProcIntegrationId` SEPARATOR ', ') AS `autoProcIntegrationId`, 
GROUP_CONCAT(`cell_a` SEPARATOR ', ') AS `Autoprocessing_cell_a`, 
GROUP_CONCAT(`cell_b` SEPARATOR ', ') AS `Autoprocessing_cell_b`, 
GROUP_CONCAT(`cell_c` SEPARATOR ', ') AS `Autoprocessing_cell_c`, 
GROUP_CONCAT(`cell_alpha` SEPARATOR ', ') AS `Autoprocessing_cell_alpha`, 
GROUP_CONCAT(`cell_beta` SEPARATOR ', ') AS `Autoprocessing_cell_beta`, 
GROUP_CONCAT(`cell_gamma` SEPARATOR ', ') AS `Autoprocessing_cell_gamma`,
GROUP_CONCAT(`anomalous` SEPARATOR ', ') AS `Autoprocessing_anomalous`,

GROUP_CONCAT(`autoProcId` SEPARATOR ', ') AS `autoProcIds`,
GROUP_CONCAT(`scalingStatisticsType` SEPARATOR ', ') AS `scalingStatisticsTypes`, 
GROUP_CONCAT(`resolutionLimitHigh` SEPARATOR ', ') AS `resolutionsLimitHigh`,  
GROUP_CONCAT(`resolutionLimitLow` SEPARATOR ', ') AS `resolutionsLimitLow`, 
GROUP_CONCAT(`rMerge` SEPARATOR ', ') AS `rMerges`,
GROUP_CONCAT(`completeness` SEPARATOR ', ') AS `completenessList`,
GROUP_CONCAT(`AutoProc_spaceGroup` SEPARATOR ', ') AS `AutoProc_spaceGroups`,

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