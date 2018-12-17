USE pydb;
INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2018_12_06_still_data_collection.sql','ONGOING');

ALTER TABLE `pydb`.`DataCollectionGroup`
CHANGE  `experimentType`  `experimentType` ENUM(  'EM',  'SAD',  'SAD - Inverse Beam',  'OSC',  'Collect - Multiwedge',  'MAD',  'Helical',  'Multi-positional',  'Mesh', 'Burn',  'MAD - Inverse Beam',  'Characterization',  'Dehydration',  'Still' ) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT  'Experiment type flag';

update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2018_12_06_still_data_collection.sql';
