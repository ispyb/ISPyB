select *, 
(select GROUP_CONCAT(workflowStepId) from WorkflowStep where WorkflowStep.workflowId = v_datacollection_summary.Workflow_workflowId order by  WorkflowStep.workflowStepId DESC) as WorkflowStep_workflowStepId,
(select GROUP_CONCAT(workflowStepType) from WorkflowStep where WorkflowStep.workflowId = v_datacollection_summary.Workflow_workflowId) as WorkflowStep_workflowStepType,
(select GROUP_CONCAT(status) from WorkflowStep where WorkflowStep.workflowId = v_datacollection_summary.Workflow_workflowId) as WorkflowStep_status,		
				
GROUP_CONCAT(`AutoProcProgram_processingPrograms` SEPARATOR ', ') AS `processingPrograms`,
GROUP_CONCAT(`AutoProcProgram_processingStatus` SEPARATOR ', ') AS `processingStatus`,
GROUP_CONCAT(`AutoProcIntegration_autoProcIntegrationId` SEPARATOR ', ') AS `autoProcIntegrationId`, 

GROUP_CONCAT(`cell_a` SEPARATOR ', ') AS `Autoprocessing_cell_a`, 
GROUP_CONCAT(`cell_b` SEPARATOR ', ') AS `Autoprocessing_cell_b`, 
GROUP_CONCAT(`cell_c` SEPARATOR ', ') AS `Autoprocessing_cell_c`, 
GROUP_CONCAT(`cell_alpha` SEPARATOR ', ') AS `Autoprocessing_cell_alpha`, 
GROUP_CONCAT(`cell_beta` SEPARATOR ', ') AS `Autoprocessing_cell_beta`, 
GROUP_CONCAT(`cell_gamma` SEPARATOR ', ') AS `Autoprocessing_cell_gamma`, 

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
	where `AutoProcIntegration`.`dataCollectionId` = v_datacollection_summary.DataCollection_dataCollectionId   
) as hasPhasing,

(SELECT GROUP_CONCAT(DISTINCT(spaceGroupShortName))
	FROM `PhasingStep`  
	LEFT JOIN `SpaceGroup` ON `PhasingStep`.`spaceGroupId` = `SpaceGroup`.`spaceGroupId`     
    LEFT JOIN `AutoProcScaling` ON `AutoProcScaling`.`autoProcScalingId` = `PhasingStep`.`autoProcScalingId`     
	LEFT JOIN `AutoProcScaling_has_Int` ON `AutoProcScaling_has_Int`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`  
    LEFT JOIN `AutoProcIntegration` ON `AutoProcIntegration`.`autoProcIntegrationId` = `AutoProcScaling_has_Int`.`autoProcIntegrationId` 
	where `AutoProcIntegration`.`dataCollectionId` = v_datacollection_summary.DataCollection_dataCollectionId   
    and PhasingStep.phasingStepType = 'MODELBUILDING'
) as SpaceGroupModelResolvedByPhasing,
				
				 
(select SUM(numberOfImages) FROM DataCollection where dataCollectionGroupId = v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId) as totalNumberOfImages,
(select count(*) FROM DataCollection where dataCollectionGroupId = v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId) as totalNumberOfDataCollections,
(select MAX(imageId) FROM Image where dataCollectionId = v_datacollection_summary.DataCollection_dataCollectionId) as lastImageId,
(select MAX(imageId) FROM Image where dataCollectionId = v_datacollection_summary.DataCollection_dataCollectionId) as firstImageId
from v_datacollection_summary