INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2018_02_27_membranes_update.sql','ONGOING');

USE pydb;
ALTER TABLE Macromolecule ADD electronDensity FLOAT(7,5);
ALTER TABLE Buffer ADD electronDensity FLOAT(7,5);
ALTER TABLE Additive ADD chemFormulaHead VARCHAR (25) default '';
ALTER TABLE Additive ADD chemFormulaTail VARCHAR (25) default ''; 

update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2018_02_27_membranes_update.sql';
