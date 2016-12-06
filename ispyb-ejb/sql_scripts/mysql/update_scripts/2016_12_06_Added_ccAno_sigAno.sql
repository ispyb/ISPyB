ALTER TABLE `pydb`.`AutoProcScalingStatistics` 
ADD COLUMN `ccAno` FLOAT NULL DEFAULT NULL AFTER `ccHalf`,
ADD COLUMN `sigAno` VARCHAR(45) NULL AFTER `ccAno`;
