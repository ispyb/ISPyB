-- first line of script
insert into SchemaStatus (scriptName, schemaStatus) values ('2016_12_06_Added_ccAno_sigAno.sql','ONGOING');


ALTER TABLE `pydb`.`AutoProcScalingStatistics` 
ADD COLUMN `ccAno` FLOAT NULL DEFAULT NULL AFTER `ccHalf`,
ADD COLUMN `sigAno` VARCHAR(45) NULL AFTER `ccAno`;

-- last line of script
update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2016_12_06_Added_ccAno_sigAno.sql';
