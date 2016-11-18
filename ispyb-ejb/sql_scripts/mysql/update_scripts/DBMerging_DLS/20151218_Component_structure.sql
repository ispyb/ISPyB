drop table Component_has_SubType;
drop table BLSampleType_has_Component;
drop table ComponentSubType;

alter table Protein drop foreign key protein_fk3;
alter table Protein drop foreign key protein_fk4;
alter table Protein drop column componentTypeId;
alter table Protein drop column concentrationTypeId;

drop table ConcentrationType;
drop table ComponentType;

create table ComponentType (
  componentTypeId int(11) unsigned NOT NULL AUTO_INCREMENT,
  name varchar(31) NOT NULL,
  PRIMARY KEY (`componentTypeId`)
);

create table ConcentrationType (
  concentrationTypeId int(11) unsigned NOT NULL AUTO_INCREMENT,
  name varchar(31) NOT NULL,
  symbol varchar(8) NOT NULL,
  PRIMARY KEY (`concentrationTypeId`)
);


-- FKs in Protein i.e. Component
alter table Protein add componentTypeId int(11) unsigned;
alter table Protein add concentrationTypeId int(11) unsigned;
alter table Protein add constraint protein_fk3 FOREIGN KEY (componentTypeId) REFERENCES ComponentType(componentTypeId) ON DELETE CASCADE ON UPDATE CASCADE;
alter table Protein add constraint protein_fk4 FOREIGN KEY (concentrationTypeId) REFERENCES ConcentrationType(concentrationTypeId) ON DELETE CASCADE ON UPDATE CASCADE;

create table ComponentSubType (
  componentSubTypeId int(11) unsigned NOT NULL PRIMARY KEY,
  name varchar(31) NOT NULL,
  hasPh tinyint(1) DEFAULT 0
);


create table BLSampleType_has_Component (
  blSampleTypeId int(10) unsigned NOT NULL,
  componentId int(10) unsigned NOT NULL,
  abundance float,
  primary key (blSampleTypeId, componentId),
  CONSTRAINT `blSampleType_has_Component_fk1` FOREIGN KEY (`blSampleTypeId`) REFERENCES `Crystal`(`crystalId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `blSampleType_has_Component_fk2` FOREIGN KEY (`componentId`) REFERENCES `Protein`(`proteinId`) ON DELETE CASCADE ON UPDATE CASCADE
);

create table Component_has_SubType (
  componentId int(10) unsigned NOT NULL,
  componentSubTypeId int(11) unsigned NOT NULL,
  primary key (componentId, componentSubTypeId),
  constraint `component_has_SubType_fk1` FOREIGN KEY (`componentId`) REFERENCES `Protein`(`proteinId`) ON DELETE CASCADE,
  constraint `component_has_SubType_fk2` FOREIGN KEY (`componentSubTypeId`) REFERENCES `ComponentSubType`(`componentSubTypeId`) ON DELETE CASCADE ON UPDATE CASCADE
);


