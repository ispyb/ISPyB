-- first line of script
insert into SchemaStatus (scriptName, schemaStatus) values ('2017_02_10_New_PhasingStep_types.sql','ONGOING');

ALTER TABLE `pydb`.`PhasingStep` 
CHANGE COLUMN `phasingStepType` `phasingStepType` ENUM('PREPARE', 'SUBSTRUCTUREDETERMINATION', 'PHASING', 'MODELBUILDING', 'REFINEMENT', 'LIGAND_FIT') NULL DEFAULT NULL ;


          -- last line of script  
  update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_02_10_New_PhasingStep_types.sql'; 
