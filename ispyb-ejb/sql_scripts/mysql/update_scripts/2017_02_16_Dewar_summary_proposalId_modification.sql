USE `pydb`;

insert into SchemaStatus (scriptName, schemaStatus) values ('2017_02_16_Dewar_summary_proposalId_modification.sql','ONGOING');

drop view v_dewar_summary;

CREATE 
    ALGORITHM = MERGE 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `v_dewar_summary` AS
    SELECT 
        `Shipping`.`shippingName` AS `shippingName`,
        `Shipping`.`deliveryAgent_agentName` AS `deliveryAgent_agentName`,
        `Shipping`.`deliveryAgent_shippingDate` AS `deliveryAgent_shippingDate`,
        `Shipping`.`deliveryAgent_deliveryDate` AS `deliveryAgent_deliveryDate`,
        `Shipping`.`deliveryAgent_agentCode` AS `deliveryAgent_agentCode`,
        `Shipping`.`deliveryAgent_flightCode` AS `deliveryAgent_flightCode`,
        `Shipping`.`shippingStatus` AS `shippingStatus`,
        `Shipping`.`bltimeStamp` AS `bltimeStamp`,
        `Shipping`.`laboratoryId` AS `laboratoryId`,
        `Shipping`.`isStorageShipping` AS `isStorageShipping`,
        `Shipping`.`creationDate` AS `creationDate`,
        `Shipping`.`comments` AS `Shipping_comments`,
        `Shipping`.`sendingLabContactId` AS `sendingLabContactId`,
        `Shipping`.`returnLabContactId` AS `returnLabContactId`,
        `Shipping`.`returnCourier` AS `returnCourier`,
        `Shipping`.`dateOfShippingToUser` AS `dateOfShippingToUser`,
        `Shipping`.`shippingType` AS `shippingType`,
        `Dewar`.`dewarId` AS `dewarId`,
        `Dewar`.`shippingId` AS `shippingId`,
        `Dewar`.`code` AS `dewarCode`,
        `Dewar`.`comments` AS `comments`,
        `Dewar`.`storageLocation` AS `storageLocation`,
        `Dewar`.`dewarStatus` AS `dewarStatus`,
        `Dewar`.`isStorageDewar` AS `isStorageDewar`,
        `Dewar`.`barCode` AS `barCode`,
        `Dewar`.`firstExperimentId` AS `firstExperimentId`,
        `Dewar`.`customsValue` AS `customsValue`,
        `Dewar`.`transportValue` AS `transportValue`,
        `Dewar`.`trackingNumberToSynchrotron` AS `trackingNumberToSynchrotron`,
        `Dewar`.`trackingNumberFromSynchrotron` AS `trackingNumberFromSynchrotron`,
        `Dewar`.`type` AS `type`,
        `BLSession`.`sessionId` AS `sessionId`,
        `BLSession`.`beamLineName` AS `beamlineName`,
        `BLSession`.`startDate` AS `sessionStartDate`,
        `BLSession`.`endDate` AS `sessionEndDate`,
        `BLSession`.`beamLineOperator` AS `beamLineOperator`,
        `Proposal`.`proposalId` AS `proposalId`,
        `Container`.`containerId` AS `containerId`,
        `Container`.`containerType` AS `containerType`,
        `Container`.`capacity` AS `capacity`,
        `Container`.`beamlineLocation` AS `beamlineLocation`,
        `Container`.`sampleChangerLocation` AS `sampleChangerLocation`,
        `Container`.`containerStatus` AS `containerStatus`,
        `Container`.`code` AS `containerCode`
    FROM
        (((((`Dewar`
        LEFT JOIN `Container` ON ((`Container`.`dewarId` = `Dewar`.`dewarId`)))
        LEFT JOIN `Shipping` ON ((`Shipping`.`shippingId` = `Dewar`.`shippingId`)))
        LEFT JOIN `ShippingHasSession` ON ((`Shipping`.`shippingId` = `ShippingHasSession`.`shippingId`)))
        LEFT JOIN `BLSession` ON ((`ShippingHasSession`.`sessionId` = `BLSession`.`sessionId`)))
        LEFT JOIN `Proposal` ON ((`Proposal`.`proposalId` = `Shipping`.`proposalId`)))
    ORDER BY `Dewar`.`dewarId` DESC;

update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_02_16_Dewar_summary_proposalId_modification.sql';