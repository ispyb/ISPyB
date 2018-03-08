insert into `pydb`.SchemaStatus (scriptName, schemaStatus) values ('2017_08_31_new_workflow_types.sql','ONGOING');

ALTER TABLE `pydb`.`Workflow` 
CHANGE COLUMN `workflowType` `workflowType` ENUM('Characterisation', 'Undefined', 'BioSAXS Post Processing', 'EnhancedCharacterisation', 'LineScan', 'MeshScan', 'Dehydration', 'KappaReorientation', 'BurnStrategy', 'XrayCentering', 'DiffractionTomography', 'TroubleShooting', 'VisualReorientation', 'HelicalCharacterisation', 'GroupedProcessing', 'MXPressE', 'MXPressO', 'MXPressL', 'MXScore', 'MXPressI', 'MXPressM', 'MXPressA', 'CollectAndSpectra', 'LowDoseDC', 'EnergyInterleavedMAD', 'MXPressH', 'MXPressP', 'MXPressP_SAD', 'MXPressR', 'MXPressR_180', 'MXPressR_dehydration', 'MeshAndCollect', 'MeshAndCollectFromFile') NULL DEFAULT NULL ;


update `pydb`.SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_08_31_new_workflow_types.sql';  
