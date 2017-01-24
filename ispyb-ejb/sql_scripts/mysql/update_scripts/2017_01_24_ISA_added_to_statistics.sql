
-- first line of script
insert into SchemaStatus (scriptName, schemaStatus) values ('2017_01_24_ISA_added_to_statistics.sql','ONGOING');

ALTER TABLE `pydb`.`AutoProcScalingStatistics` 
ADD COLUMN `isa` VARCHAR(45) NULL AFTER `sigAno`;

  -- last line of script  
  update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_01_24_ISA_added_to_statistics.sql'; 

