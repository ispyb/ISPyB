use pydb;
insert into `pydb`.SchemaStatus (scriptName, schemaStatus) values ('2018_05_02_added_MXPRESSF.sql','ONGOING');

ALTER TABLE `pydb`.`Workflow` 
CHANGE COLUMN `workflowType` `workflowType` ENUM('Characterisation','Undefined','BioSAXS Post Processing','EnhancedCharacterisation','LineScan','MeshScan','Dehydration','KappaReorientation','BurnStrategy','XrayCentering','DiffractionTomography','TroubleShooting','VisualReorientation','HelicalCharacterisation','GroupedProcessing','MXPressE','MXPressO','MXPressL','MXScore','MXPressI','MXPressM','MXPressA','CollectAndSpectra','LowDoseDC','EnergyInterleavedMAD','MXPressF', 'MXPressH','MXPressP','MXPressP_SAD','MXPressR','MXPressR_180','MXPressR_dehydration','MeshAndCollect','MeshAndCollectFromFile') NULL DEFAULT NULL ;

update `pydb`.SchemaStatus set schemaStatus = 'DONE' where scriptName = '2018_05_02_added_MXPRESSF.sql';  