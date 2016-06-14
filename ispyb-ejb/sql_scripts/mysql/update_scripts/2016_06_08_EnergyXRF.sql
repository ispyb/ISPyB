DROP VIEW IF EXISTS v_energyScan;

CREATE 
    ALGORITHM = MERGE 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `v_energyScan` AS
    SELECT 
        `EnergyScan`.`energyScanId` AS `energyScanId`,
        `EnergyScan`.`sessionId` AS `sessionId`,
        `EnergyScan`.`blSampleId` AS `blSampleId`,
        `EnergyScan`.`fluorescenceDetector` AS `fluorescenceDetector`,
        `EnergyScan`.`scanFileFullPath` AS `scanFileFullPath`,
        `EnergyScan`.`choochFileFullPath` AS `choochFileFullPath`,
        `EnergyScan`.`jpegChoochFileFullPath` AS `jpegChoochFileFullPath`,
        `EnergyScan`.`element` AS `element`,
        `EnergyScan`.`startEnergy` AS `startEnergy`,
        `EnergyScan`.`endEnergy` AS `endEnergy`,
        `EnergyScan`.`transmissionFactor` AS `transmissionFactor`,
        `EnergyScan`.`exposureTime` AS `exposureTime`,
        `EnergyScan`.`synchrotronCurrent` AS `synchrotronCurrent`,
        `EnergyScan`.`temperature` AS `temperature`,
        `EnergyScan`.`peakEnergy` AS `peakEnergy`,
        `EnergyScan`.`peakFPrime` AS `peakFPrime`,
        `EnergyScan`.`peakFDoublePrime` AS `peakFDoublePrime`,
        `EnergyScan`.`inflectionEnergy` AS `inflectionEnergy`,
        `EnergyScan`.`inflectionFPrime` AS `inflectionFPrime`,
        `EnergyScan`.`inflectionFDoublePrime` AS `inflectionFDoublePrime`,
        `EnergyScan`.`xrayDose` AS `xrayDose`,
        `EnergyScan`.`startTime` AS `startTime`,
        `EnergyScan`.`endTime` AS `endTime`,
        `EnergyScan`.`edgeEnergy` AS `edgeEnergy`,
        `EnergyScan`.`filename` AS `filename`,
        `EnergyScan`.`beamSizeVertical` AS `beamSizeVertical`,
        `EnergyScan`.`beamSizeHorizontal` AS `beamSizeHorizontal`,
        `EnergyScan`.`crystalClass` AS `crystalClass`,
        `EnergyScan`.`comments` AS `comments`,
        `EnergyScan`.`flux` AS `flux`,
        `EnergyScan`.`flux_end` AS `flux_end`,
        `BLSample`.`blSampleId` AS `BLSample_sampleId`,
        `BLSample`.`name` AS `name`,
        `BLSample`.`code` AS `code`,
        `Protein`.`acronym` AS `acronym`,
        `BLSession`.`proposalId` AS `BLSession_proposalId`
    FROM
        ((((`EnergyScan`
        LEFT JOIN `BLSample` ON ((`BLSample`.`blSampleId` = `EnergyScan`.`blSampleId`)))
        LEFT JOIN `Crystal` ON ((`Crystal`.`crystalId` = `BLSample`.`crystalId`)))
        LEFT JOIN `Protein` ON ((`Protein`.`proteinId` = `Crystal`.`proteinId`)))
        LEFT JOIN `BLSession` ON ((`BLSession`.`sessionId` = `EnergyScan`.`sessionId`)));
        
DROP VIEW IF EXISTS v_xfeFluorescenceSpectrum;
CREATE 
    ALGORITHM = MERGE 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `v_xfeFluorescenceSpectrum` AS
    SELECT 
        `XFEFluorescenceSpectrum`.`xfeFluorescenceSpectrumId` AS `xfeFluorescenceSpectrumId`,
        `XFEFluorescenceSpectrum`.`sessionId` AS `sessionId`,
        `XFEFluorescenceSpectrum`.`blSampleId` AS `blSampleId`,
        `XFEFluorescenceSpectrum`.`fittedDataFileFullPath` AS `fittedDataFileFullPath`,
        `XFEFluorescenceSpectrum`.`scanFileFullPath` AS `scanFileFullPath`,
        `XFEFluorescenceSpectrum`.`jpegScanFileFullPath` AS `jpegScanFileFullPath`,
        `XFEFluorescenceSpectrum`.`startTime` AS `startTime`,
        `XFEFluorescenceSpectrum`.`endTime` AS `endTime`,
        `XFEFluorescenceSpectrum`.`filename` AS `filename`,
        `XFEFluorescenceSpectrum`.`energy` AS `energy`,
        `XFEFluorescenceSpectrum`.`exposureTime` AS `exposureTime`,
        `XFEFluorescenceSpectrum`.`beamTransmission` AS `beamTransmission`,
        `XFEFluorescenceSpectrum`.`annotatedPymcaXfeSpectrum` AS `annotatedPymcaXfeSpectrum`,
        `XFEFluorescenceSpectrum`.`beamSizeVertical` AS `beamSizeVertical`,
        `XFEFluorescenceSpectrum`.`beamSizeHorizontal` AS `beamSizeHorizontal`,
        `XFEFluorescenceSpectrum`.`crystalClass` AS `crystalClass`,
        `XFEFluorescenceSpectrum`.`comments` AS `comments`,
        `XFEFluorescenceSpectrum`.`flux` AS `flux`,
        `XFEFluorescenceSpectrum`.`flux_end` AS `flux_end`,
        `BLSample`.`blSampleId` AS `BLSample_sampleId`,
        `BLSample`.`name` AS `name`,
        `BLSample`.`code` AS `code`,
        `Protein`.`acronym` AS `acronym`,
        `BLSession`.`proposalId` AS `BLSession_proposalId`
    FROM
        ((((`XFEFluorescenceSpectrum`
        LEFT JOIN `BLSample` ON ((`BLSample`.`blSampleId` = `XFEFluorescenceSpectrum`.`blSampleId`)))
        LEFT JOIN `Crystal` ON ((`Crystal`.`crystalId` = `BLSample`.`crystalId`)))
        LEFT JOIN `Protein` ON ((`Protein`.`proteinId` = `Crystal`.`proteinId`)))
        LEFT JOIN `BLSession` ON ((`BLSession`.`sessionId` = `XFEFluorescenceSpectrum`.`sessionId`)))        
