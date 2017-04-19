ALTER TABLE `Workflow` 
CHANGE `workflowType` `workflowType` 
ENUM('Undefined','BioSAXS Post Processing','EnhancedCharacterisation','LineScan','MeshScan','Dehydration','KappaReorientation','BurnStrategy','XrayCentering','DiffractionTomography','TroubleShooting','VisualReorientation','HelicalCharacterisation','GroupedProcessing','MXPressE','MXPressO','MXPressL','MXScore', 'MXPressI', 'MXPressM', 'MXPressA') 
CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL