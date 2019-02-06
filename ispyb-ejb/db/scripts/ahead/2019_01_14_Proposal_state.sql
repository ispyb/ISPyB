INSERT INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2019_01_14_Proposal_state.sql', 'ONGOING');

ALTER TABLE Proposal
    ADD state enum('Open', 'Closed', 'Cancelled') NULL DEFAULT 'Open';

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2019_01_14_Proposal_state.sql';
