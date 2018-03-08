insert into `pydb`.SchemaStatus (scriptName, schemaStatus) values ('2017_09_21_new_diffractionplan_types.sql','ONGOING');

ALTER TABLE `pydb`.`DiffractionPlan` 
CHANGE COLUMN `experimentKind` `experimentKind` ENUM('Default','MXPressE','MXPressO','MXPressP', 'MXPressP_SAD','MXPressI','MXPressE_SAD','MXScore','MXPressM','MAD','SAD','Fixed','Ligand binding','Refinement','OSC','MAD - Inverse Beam','SAD - Inverse Beam') NULL DEFAULT NULL ;


update `pydb`.SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_09_21_new_diffractionplan_types.sql';  
