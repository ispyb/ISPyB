use pydb;
ALTER TABLE `Dewar`  ADD `facilityCode` VARCHAR(20) NULL COMMENT 'Unique barcode assigned to each dewar' AFTER `trackingNumberFromSynchrotron`;
