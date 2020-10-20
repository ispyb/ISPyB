INSERT IGNORE INTO SchemaStatus (scriptName, schemaStatus) VALUES ('2020_09_09_Protein_new_cols.sql', 'ONGOING');

ALTER TABLE Protein
    ADD `description` TEXT NULL DEFAULT NULL 
        COMMENT 'A description/summary using words and sentences' AFTER `acronym`,
    ADD `hazardGroup` tinyint unsigned DEFAULT 1 NOT NULL 
        COMMENT 'A.k.a. risk group' AFTER `description`,
    ADD `containmentLevel` tinyint unsigned DEFAULT 1 NOT NULL
        COMMENT 'A.k.a. biosafety level, which indicates the level of containment required' AFTER `hazardGroup`;

UPDATE SchemaStatus SET schemaStatus = 'DONE' WHERE scriptName = '2020_09_09_Protein_new_cols.sql';
