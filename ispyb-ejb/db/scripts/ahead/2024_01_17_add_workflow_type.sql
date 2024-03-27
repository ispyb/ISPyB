use pydb;

INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2024_01_17_add_workflow_type.sql', 'ONGOING');


ALTER TABLE `pydb`.`Workflow`
CHANGE COLUMN `workflowType` `workflowType` ENUM('Characterisation','Undefined','BioSAXS Post Processing','EnhancedCharacterisation','LineScan','MeshScan','Dehydration','KappaReorientation','BurnStrategy','XrayCentering','DiffractionTomography','TroubleShooting','VisualReorientation','HelicalCharacterisation','GroupedProcessing','MXPressE','MXPressO','MXPressL','MXScore','MXPressI','MXPressM','MXPressA','CollectAndSpectra','LowDoseDC','EnergyInterleavedMAD','MXPressF','MXPressH','MXPressP','MXPressP_SAD','MXPressR','MXPressR_180','MXPressR_dehydration','MeshAndCollect','MeshAndCollectFromFile',  'CalibrateKappa', 'CenterRotationAxis', 'CentreBeam', 'CentrePin', 'ChipX_collect', 'ChipX_collect_sections', 'ChipX_left_position', 'ChipX_right_position', 'DistanceCalibration', 'IceRingsMeshScan', 'MeshAndBurn', 'MXPressA_globalp', 'MXPressA_globalp_full', 'MXPressA_globalp_minimal', 'MXPressA_globalp_quick', 'MXPressA_HTX', 'MXPressF_twoMeshes', 'MXPressK', 'MXPressO_540', 'MXPressS', 'TroubleShootingWithDialog'
) NULL DEFAULT NULL ;

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2024_01_17_add_workflow_type.sql';