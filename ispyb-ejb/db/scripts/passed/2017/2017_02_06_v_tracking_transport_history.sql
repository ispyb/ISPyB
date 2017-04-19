

insert into SchemaStatus (scriptName, schemaStatus) values ('2017_02_06_v_tracking_transport_history.sql','ONGOING');

drop view if exists v_tracking_shipment_history;

CREATE 
    ALGORITHM = MERGE    
VIEW `v_tracking_shipment_history` AS
    SELECT 
        `Dewar`.`dewarId` AS `Dewar_dewarId`,
        `Dewar`.`code` AS `Dewar_code`,
        `Dewar`.`comments` AS `Dewar_comments`,
        `Dewar`.`dewarStatus` AS `Dewar_dewarStatus`,
        `Dewar`.`barCode` AS `Dewar_barCode`,
        `Dewar`.`firstExperimentId` AS `Dewar_firstExperimentId`,
        `Dewar`.`trackingNumberToSynchrotron` AS `Dewar_trackingNumberToSynchrotron`,
        `Dewar`.`trackingNumberFromSynchrotron` AS `Dewar_trackingNumberFromSynchrotron`,
        `Dewar`.`type` AS `Dewar_type`,
        `Shipping`.`shippingId` AS `Shipping_shippingId`,
        `Shipping`.`proposalId` AS `Shipping_proposalId`,
        `Shipping`.`shippingName` AS `Shipping_shippingName`,
        `Shipping`.`deliveryAgent_agentName` AS `deliveryAgent_agentName`,
        `Shipping`.`deliveryAgent_shippingDate` AS `Shipping_deliveryAgent_shippingDate`,
        `Shipping`.`deliveryAgent_deliveryDate` AS `Shipping_deliveryAgent_deliveryDate`,
        `Shipping`.`shippingStatus` AS `Shipping_shippingStatus`,
        `Shipping`.`returnCourier` AS `Shipping_returnCourier`,
        `Shipping`.`dateOfShippingToUser` AS `Shipping_dateOfShippingToUser`,
        `DewarTransportHistory`.`DewarTransportHistoryId` AS `DewarTransportHistory_DewarTransportHistoryId`,
        `DewarTransportHistory`.`dewarStatus` AS `DewarTransportHistory_dewarStatus`,
        `DewarTransportHistory`.`storageLocation` AS `DewarTransportHistory_storageLocation`,
        `DewarTransportHistory`.`arrivalDate` AS `DewarTransportHistory_arrivalDate`
    FROM
        ((`Shipping`
        LEFT JOIN `Dewar` ON ((`Dewar`.`shippingId` = `Shipping`.`shippingId`)))
        LEFT JOIN `DewarTransportHistory` ON ((`DewarTransportHistory`.`dewarId` = `Dewar`.`dewarId`)));
        

update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_02_06_v_tracking_transport_history.sql';
