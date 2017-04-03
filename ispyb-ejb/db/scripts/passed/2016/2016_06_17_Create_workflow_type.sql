DROP TABLE `pydb`.`WorkflowType`;

CREATE TABLE `pydb`.`WorkflowType` (
  `workflowTypeId` INT NOT NULL AUTO_INCREMENT,
  `workflowTypeName` VARCHAR(45) NULL,
  `comments` VARCHAR(2048) NULL,
  `recordTimeStamp` TIMESTAMP NULL,
  PRIMARY KEY (`workflowTypeId`));

  
INSERT INTO `pydb`.`WorkflowType` (`workflowTypeId`, `workflowTypeName`, `comments`, `recordTimeStamp`) 
 VALUES 
 (NULL, 'Undefined', NULL, NOW()), 
 (NULL, 'BioSAXS Post Processing', NULL, NOW()), 
 (NULL, 'EnhancedCharacterisation', NULL, NOW()),
 (NULL, 'LineScan', NULL, NOW()),
  (NULL, 'MeshScan', NULL, NOW()),
 (NULL, 'Dehydration', NULL, NOW()),
  (NULL, 'KappaReorientation', NULL, NOW()),
   (NULL, 'BurnStrategy', NULL, NOW()),
   (NULL, 'XrayCentering', NULL, NOW()),
  (NULL, 'DiffractionTomography', NULL, NOW()),
  (NULL, 'TroubleShooting', NULL, NOW()),
  (NULL, 'VisualReorientation', NULL, NOW()),  
  (NULL, 'HelicalCharacterisation', NULL, NOW()),
  (NULL, 'GroupedProcessing', NULL, NOW()),
  (NULL, 'MXPressE', NULL, NOW()),
  (NULL, 'MXPressO', NULL, NOW()),
  (NULL, 'MXPressL', NULL, NOW()),
  (NULL, 'MXScore', NULL, NOW()),
  (NULL, 'MXPressI', NULL, NOW()),
  (NULL, 'MXPressM', NULL, NOW()),
  (NULL, 'MXPressA', NULL, NOW()),
  (NULL, 'CollectAndSpectra', NULL, NOW()),
  (NULL, 'LowDoseDC', NULL, NOW()) ;
  
ALTER TABLE `Workflow`  ADD `workflowTypeId` INT NULL AFTER `workflowType`;  

update WorkflowType WFT , Workflow WF 
set WF.workflowTypeId  = WFT.WorkflowTypeId 
where WFT.workflowTypeName = WF.workflowType;



