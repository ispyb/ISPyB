USE `pydb`;
CREATE 
     OR REPLACE ALGORITHM = MERGE 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `pydb`.`v_xfeFluorescenceSpectrum` AS
    SELECT 
        `pydb`.`XFEFluorescenceSpectrum`.`xfeFluorescenceSpectrumId` AS `xfeFluorescenceSpectrumId`,
        `pydb`.`XFEFluorescenceSpectrum`.`sessionId` AS `sessionId`,
        `pydb`.`XFEFluorescenceSpectrum`.`blSampleId` AS `blSampleId`,
        `pydb`.`XFEFluorescenceSpectrum`.`fittedDataFileFullPath` AS `fittedDataFileFullPath`,
        `pydb`.`XFEFluorescenceSpectrum`.`scanFileFullPath` AS `scanFileFullPath`,
        `pydb`.`XFEFluorescenceSpectrum`.`jpegScanFileFullPath` AS `jpegScanFileFullPath`,
        `pydb`.`XFEFluorescenceSpectrum`.`startTime` AS `startTime`,
        `pydb`.`XFEFluorescenceSpectrum`.`endTime` AS `endTime`,
        `pydb`.`XFEFluorescenceSpectrum`.`filename` AS `filename`,
        `pydb`.`XFEFluorescenceSpectrum`.`energy` AS `energy`,
        `pydb`.`XFEFluorescenceSpectrum`.`exposureTime` AS `exposureTime`,
        `pydb`.`XFEFluorescenceSpectrum`.`beamTransmission` AS `beamTransmission`,
        `pydb`.`XFEFluorescenceSpectrum`.`annotatedPymcaXfeSpectrum` AS `annotatedPymcaXfeSpectrum`,
        `pydb`.`XFEFluorescenceSpectrum`.`beamSizeVertical` AS `beamSizeVertical`,
        `pydb`.`XFEFluorescenceSpectrum`.`beamSizeHorizontal` AS `beamSizeHorizontal`,
        `pydb`.`XFEFluorescenceSpectrum`.`crystalClass` AS `crystalClass`,
        `pydb`.`XFEFluorescenceSpectrum`.`comments` AS `comments`,
        `pydb`.`XFEFluorescenceSpectrum`.`flux` AS `flux`,
        `pydb`.`XFEFluorescenceSpectrum`.`flux_end` AS `flux_end`,
        `pydb`.`XFEFluorescenceSpectrum`.`workingDirectory` AS `workingDirectory`,
        `pydb`.`BLSample`.`blSampleId` AS `BLSample_sampleId`,
        `pydb`.`BLSession`.`proposalId` AS `BLSession_proposalId`
    FROM
        ((`pydb`.`XFEFluorescenceSpectrum`
        LEFT JOIN `pydb`.`BLSample` ON ((`pydb`.`BLSample`.`blSampleId` = `pydb`.`XFEFluorescenceSpectrum`.`blSampleId`)))
        LEFT JOIN `pydb`.`BLSession` ON ((`pydb`.`BLSession`.`sessionId` = `pydb`.`XFEFluorescenceSpectrum`.`sessionId`)));

ALTER TABLE `pydb`.`PhasingProgramRun` 
CHANGE COLUMN `recordTimeStamp` `recordTimeStamp` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time' ;

ALTER TABLE `pydb`.`PhasingProgramAttachment` 
CHANGE COLUMN `recordTimeStamp` `recordTimeStamp` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time' ;


ALTER TABLE `pydb`.`PhasingStatistics` 
CHANGE COLUMN `recordTimeStamp` `recordTimeStamp` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation or last update date/time' ;
