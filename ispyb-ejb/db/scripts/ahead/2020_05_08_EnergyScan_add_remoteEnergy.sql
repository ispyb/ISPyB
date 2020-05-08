INSERT IGNORE INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2020_05_08_EnergyScan_add_remoteEnergy.sql', 'ONGOING');

ALTER TABLE `pydb`.`EnergyScan`
ADD COLUMN `remoteEnergy` FLOAT NULL DEFAULT NULL AFTER `blSubSampleId`,
ADD COLUMN `remoteFPrime` FLOAT NULL DEFAULT NULL AFTER `remoteEnergy`,
ADD COLUMN `remoteFDoublePrime` FLOAT NULL DEFAULT NULL AFTER `remoteFPrime`;

USE `pydb`;
CREATE
     OR REPLACE ALGORITHM = MERGE
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
        `EnergyScan`.`remoteEnergy` AS `remoteEnergy`,
        `EnergyScan`.`remoteFPrime` AS `remoteFPrime`,
        `EnergyScan`.`remoteFDoublePrime` AS `remoteFDoublePrime`,
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

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2020_05_08_EnergyScan_add_remoteEnergy.sql';