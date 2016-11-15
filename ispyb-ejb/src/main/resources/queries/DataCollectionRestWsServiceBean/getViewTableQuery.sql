select 
*,
(SELECT count(*) 
	FROM `PhasingStep`    
    LEFT JOIN `AutoProcScaling` ON `AutoProcScaling`.`autoProcScalingId` = `PhasingStep`.`autoProcScalingId`     
	LEFT JOIN `AutoProcScaling_has_Int` ON `AutoProcScaling_has_Int`.`autoProcScalingId` = `AutoProcScaling`.`autoProcScalingId`  
    LEFT JOIN `AutoProcIntegration` ON `AutoProcIntegration`.`autoProcIntegrationId` = `AutoProcScaling_has_Int`.`autoProcIntegrationId` 
	LEFT JOIN `DataCollection` ON `DataCollection`.`dataCollectionId` = `AutoProcIntegration`.`dataCollectionId` 
	where `DataCollection`.`dataCollectionId` = v_datacollection.dataCollectionId
) as hasPhasing
 from v_datacollection 