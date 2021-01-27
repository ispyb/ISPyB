INSERT IGNORE INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2020_10_08_experiment_statistics_view.sql', 'ONGOING');

CREATE
    ALGORITHM = UNDEFINED
    DEFINER = `pxadmin`@`%`
    SQL SECURITY DEFINER
VIEW `v_mx_experiment_stats` AS
    SELECT
        `DC`.`startTime` AS `startTime`,
        `DC`.`numberOfImages` AS `Images`,
        `DC`.`transmission` AS `Transmission`,
        `DC`.`resolution` AS `Res. (corner)`,
        `DC`.`wavelength` AS `En. (Wave.)`,
        `DC`.`omegaStart` AS `Omega start (total)`,
        `DC`.`exposureTime` AS `Exposure Time`,
        `DC`.`flux` AS `Flux`,
        `DC`.`flux_end` AS `Flux End`,
        `DC`.`detectorDistance` AS `Detector Distance`,
        `DC`.`xBeam` AS `X Beam`,
        `DC`.`yBeam` AS `Y Beam`,
        `DC`.`kappaStart` AS `Kappa`,
        `DC`.`phiStart` AS `Phi`,
        `DC`.`axisStart` AS `Axis Start`,
        `DC`.`axisEnd` AS `Axis End`,
        `DC`.`axisRange` AS `Axis Range`,
        `DC`.`beamSizeAtSampleX` AS `Beam Size X`,
        `DC`.`beamSizeAtSampleY` AS `Beam Size Y`,
        `BLS`.`beamLineName` AS `beamLineName`,
        `DCG`.`comments` AS `comments`,
        `P`.`proposalNumber` AS `proposalNumber`
    FROM
        (((`DataCollection` `DC`
        JOIN `DataCollectionGroup` `DCG` ON ((`DCG`.`dataCollectionGroupId` = `DC`.`dataCollectionGroupId`)))
        JOIN `BLSession` `BLS` ON ((`BLS`.`sessionId` = `DCG`.`sessionId`)))
        JOIN `Proposal` `P` ON ((`P`.`proposalId` = `BLS`.`proposalId`)));

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2020_10_08_experiment_statistics_view.sql';