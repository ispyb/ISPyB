use pydb;
insert into SchemaStatus (scriptName, schemaStatus) values ('2017_12_08_sample_mx_view_update.sql','ONGOING');

CREATE OR REPLACE 
    ALGORITHM = MERGE 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `v_mx_sample` AS
    SELECT 
        `BLSample`.`blSampleId` AS `BLSample_blSampleId`,
        `BLSample`.`diffractionPlanId` AS `BLSample_diffractionPlanId`,
        `BLSample`.`crystalId` AS `BLSample_crystalId`,
        `BLSample`.`containerId` AS `BLSample_containerId`,
        `BLSample`.`name` AS `BLSample_name`,
        `BLSample`.`code` AS `BLSample_code`,
        `BLSample`.`location` AS `BLSample_location`,
        `BLSample`.`holderLength` AS `BLSample_holderLength`,
        `BLSample`.`loopLength` AS `BLSample_loopLength`,
        `BLSample`.`loopType` AS `BLSample_loopType`,
        `BLSample`.`wireWidth` AS `BLSample_wireWidth`,
        `BLSample`.`comments` AS `BLSample_comments`,
        `BLSample`.`completionStage` AS `BLSample_completionStage`,
        `BLSample`.`structureStage` AS `BLSample_structureStage`,
        `BLSample`.`publicationStage` AS `BLSample_publicationStage`,
        `BLSample`.`publicationComments` AS `BLSample_publicationComments`,
        `BLSample`.`blSampleStatus` AS `BLSample_blSampleStatus`,
        `BLSample`.`isInSampleChanger` AS `BLSample_isInSampleChanger`,
        `BLSample`.`lastKnownCenteringPosition` AS `BLSample_lastKnownCenteringPosition`,
        `BLSample`.`recordTimeStamp` AS `BLSample_recordTimeStamp`,
        `BLSample`.`SMILES` AS `BLSample_SMILES`,
        `Protein`.`proteinId` AS `Protein_proteinId`,
        `Protein`.`name` AS `Protein_name`,
        `Protein`.`acronym` AS `Protein_acronym`,
        `Protein`.`proteinType` AS `Protein_proteinType`,
        `Shipping`.`proposalId` AS `Protein_proposalId`,
        `Person`.`personId` AS `Person_personId`,
        `Person`.`familyName` AS `Person_familyName`,
        `Person`.`givenName` AS `Person_givenName`,
        `Person`.`emailAddress` AS `Person_emailAddress`,
        `Container`.`containerId` AS `Container_containerId`,
        `Container`.`code` AS `Container_code`,
        `Container`.`containerType` AS `Container_containerType`,
        `Container`.`containerStatus` AS `Container_containerStatus`,
        `Container`.`beamlineLocation` AS `Container_beamlineLocation`,
        `Container`.`sampleChangerLocation` AS `Container_sampleChangerLocation`,
        `Dewar`.`code` AS `Dewar_code`,
        `Dewar`.`dewarId` AS `Dewar_dewarId`,
        `Dewar`.`storageLocation` AS `Dewar_storageLocation`,
        `Dewar`.`dewarStatus` AS `Dewar_dewarStatus`,
        `Dewar`.`barCode` AS `Dewar_barCode`,
        `Shipping`.`shippingId` AS `Shipping_shippingId`,
        `BLSession`.`sessionId` AS `sessionId`,
        `BLSession`.`startDate` AS `BLSession_startDate`,
        `BLSession`.`beamLineName` AS `BLSession_beamLineName`
    FROM
        ((((((((`BLSample`
        LEFT JOIN `Crystal` ON ((`Crystal`.`crystalId` = `BLSample`.`crystalId`)))
        LEFT JOIN `Protein` ON ((`Protein`.`proteinId` = `Crystal`.`proteinId`)))
        LEFT JOIN `Person` ON ((`Person`.`personId` = `Protein`.`personId`)))
        LEFT JOIN `Container` ON ((`BLSample`.`containerId` = `Container`.`containerId`)))
        LEFT JOIN `Dewar` ON ((`Dewar`.`dewarId` = `Container`.`dewarId`)))
        LEFT JOIN `Shipping` ON ((`Dewar`.`shippingId` = `Shipping`.`shippingId`)))
        LEFT JOIN `DataCollectionGroup` ON ((`DataCollectionGroup`.`blSampleId` = `BLSample`.`blSampleId`)))
        LEFT JOIN `BLSession` ON ((`BLSession`.`sessionId` = `DataCollectionGroup`.`sessionId`)));
        
 
update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_12_08_sample_mx_view_update.sql';
