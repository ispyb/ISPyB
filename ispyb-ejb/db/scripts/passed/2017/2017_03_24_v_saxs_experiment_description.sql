USE `pydb`;

insert into SchemaStatus (scriptName, schemaStatus) values ('2017_03_24_v_saxs_experiment_description.sql','ONGOING');

drop view if exists v_saxs_experiment_description;

CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `pxadmin`@`%` 
    SQL SECURITY DEFINER
VIEW `v_saxs_experiment_description` AS
    SELECT 
        `experiment`.`experimentId` AS `experimentId`,
        `experiment`.`sessionId` AS `sessionId`,
        `experiment`.`proposalId` AS `proposalId`,
        `experiment`.`name` AS `name`,
        `experiment`.`creationDate` AS `creationDate`,
        `experiment`.`experimentType` AS `experimentType`,
        `experiment`.`sourceFilePath` AS `sourceFilePath`,
        `experiment`.`dataAcquisitionFilePath` AS `dataAcquisitionFilePath`,
        `experiment`.`status` AS `status`,
        `experiment`.`comments` AS `Experiment_comments`,
        `SamplePlatePosition`.`samplePlateId` AS `samplePlateId`,
        `SamplePlatePosition`.`rowNumber` AS `rowNumber`,
        `SamplePlatePosition`.`columnNumber` AS `columnNumber`,
        `SamplePlatePosition`.`volume` AS `volume`,
        `Measurement`.`measurementId` AS `measurementId`,
        `Measurement`.`specimenId` AS `Measurement_specimenId`,
        `Measurement`.`runId` AS `runId`,
        `Measurement`.`code` AS `code`,
        `Measurement`.`priorityLevelId` AS `priorityLevelId`,
        `Measurement`.`exposureTemperature` AS `exposureTemperature`,
        `Measurement`.`viscosity` AS `viscosity`,
        `Measurement`.`flow` AS `flow`,
        `Measurement`.`extraFlowTime` AS `extraFlowTime`,
        `Measurement`.`volumeToLoad` AS `volumeToLoad`,
        `Measurement`.`waitTime` AS `waitTime`,
        `Measurement`.`transmission` AS `transmission`,
        `Measurement`.`comments` AS `comments`,
        `Buffer`.`acronym` AS `buffer`,
        `Macromolecule`.`acronym` AS `macromolecule`,
        `Specimen`.`specimenId` AS `specimenId`,
        `Specimen`.`bufferId` AS `bufferId`,
        `Specimen`.`macromoleculeId` AS `macromoleculeId`,
        `Specimen`.`samplePlatePositionId` AS `samplePlatePositionId`,
        `Specimen`.`safetyLevelId` AS `safetyLevelId`,
        `Specimen`.`stockSolutionId` AS `stockSolutionId`,
        `Specimen`.`code` AS `Specimen_code`,
        `Specimen`.`concentration` AS `concentration`,
        `Specimen`.`volume` AS `Specimen_volume`,
        `Specimen`.`comments` AS `Specimen_comments`,
        `SamplePlate`.`plateGroupId` AS `plateGroupId`,
        `SamplePlate`.`plateTypeId` AS `plateTypeId`,
        `SamplePlate`.`instructionSetId` AS `instructionSetId`,
        `SamplePlate`.`boxId` AS `boxId`,
        `SamplePlate`.`name` AS `SamplePlate_name`,
        `SamplePlate`.`slotPositionRow` AS `slotPositionRow`,
        `SamplePlate`.`slotPositionColumn` AS `slotPositionColumn`,
        `SamplePlate`.`storageTemperature` AS `storageTemperature`
    FROM
        ((((((`Experiment` `experiment`
        LEFT JOIN `Specimen` ON ((`Specimen`.`experimentId` = `experiment`.`experimentId`)))
        LEFT JOIN `Macromolecule` ON ((`Macromolecule`.`macromoleculeId` = `Specimen`.`macromoleculeId`)))
        LEFT JOIN `Buffer` ON ((`Buffer`.`bufferId` = `Specimen`.`bufferId`)))
        LEFT JOIN `Measurement` ON ((`Measurement`.`specimenId` = `Specimen`.`specimenId`)))
        LEFT JOIN `SamplePlatePosition` ON ((`Specimen`.`samplePlatePositionId` = `SamplePlatePosition`.`samplePlatePositionId`)))
        LEFT JOIN `SamplePlate` ON ((`SamplePlate`.`samplePlateId` = `SamplePlatePosition`.`samplePlateId`)));

update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_03_24_v_saxs_experiment_description.sql';
