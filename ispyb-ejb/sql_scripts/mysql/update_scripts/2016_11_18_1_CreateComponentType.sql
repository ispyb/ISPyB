
-- 

USE `pydb`;
-- first line of script
insert into SchemaStatus (scriptName, schemaStatus) values ('2016_11_18_1_CreateComponentType','ONGOING');

-- body of the script

create table ComponentType (
  componentTypeId int(11) unsigned NOT NULL AUTO_INCREMENT,
  name varchar(31) NOT NULL,
  PRIMARY KEY (`componentTypeId`)
);

alter table Protein add componentTypeId int(11) unsigned;
alter table Protein add constraint protein_fk3 FOREIGN KEY (componentTypeId) REFERENCES ComponentType(componentTypeId) ON DELETE CASCADE ON UPDATE CASCADE;
INSERT INTO `ComponentType` (`componentTypeId`,`name`) VALUES (1,'Protein');
INSERT INTO `ComponentType` (`componentTypeId`,`name`) VALUES (2,'DNA');
INSERT INTO `ComponentType` (`componentTypeId`,`name`) VALUES (3,'Small Molecule');
INSERT INTO `ComponentType` (`componentTypeId`,`name`) VALUES (4,'RNA');


-- last line of script
update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2016_11_18_1_CreateComponentType';

