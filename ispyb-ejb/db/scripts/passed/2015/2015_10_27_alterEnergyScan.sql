ALTER TABLE `EnergyScan` ADD `blSampleId` INT( 10 ) NULL AFTER `sessionId` ;

update EnergyScan, BLSample_has_EnergyScan
set EnergyScan.blSampleId = BLSample_has_EnergyScan.blSampleId
  where EnergyScan.energyScanId = BLSample_has_EnergyScan.energyScanId ;