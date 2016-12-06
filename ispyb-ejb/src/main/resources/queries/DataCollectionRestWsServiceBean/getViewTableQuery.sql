select 
*,
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
) as value4
 from v_datacollection 