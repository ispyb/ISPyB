use pydb;
insert into SchemaStatus (scriptName, schemaStatus) values ('2017_12_05_fluorescence_maps.sql','ONGOING');

CREATE TABLE XRFFluorescenceMappingROI (
    xrfFluorescenceMappingROIId int(11) unsigned auto_increment PRIMARY KEY,
    startEnergy float NOT NULL,
    endEnergy float NOT NULL,
    element varchar(2),
    edge varchar(2) COMMENT 'In future may be changed to enum(K, L)',
    r tinyint unsigned COMMENT 'R colour component',
    g tinyint unsigned COMMENT 'G colour component',
    b tinyint unsigned COMMENT 'B colour component'
);

CREATE TABLE XRFFluorescenceMapping (
    xrfFluorescenceMappingId int(11) unsigned auto_increment PRIMARY KEY,
    xrfFluorescenceMappingROIId int(11) unsigned NOT NULL,
    dataCollectionId int(11) unsigned NOT NULL,
    imageNumber int(10) unsigned NOT NULL,
    counts int(10) unsigned NOT NULL,
    CONSTRAINT XRFFluorescenceMapping_ibfk1
        FOREIGN KEY (xrfFluorescenceMappingROIId) REFERENCES XRFFluorescenceMappingROI(xrfFluorescenceMappingROIId) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT XRFFluorescenceMapping_ibfk2
        FOREIGN KEY (dataCollectionId) REFERENCES DataCollection(dataCollectionId) ON DELETE CASCADE ON UPDATE CASCADE
);

update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_12_05_fluorescence_maps.sql';
