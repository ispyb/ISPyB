ALTER TABLE `pydb`.`DataCollection` 
ADD COLUMN `imageQualityIndicatorsPlotPath` VARCHAR(512) NULL DEFAULT NULL AFTER `bestWilsonPlotPath`,
ADD COLUMN `imageQualityIndicatorsCSVPath` VARCHAR(512) NULL AFTER `imageQualityIndicatorsPlotPath`;
