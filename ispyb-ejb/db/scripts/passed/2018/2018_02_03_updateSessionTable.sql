insert into `pydb`.SchemaStatus (scriptName, schemaStatus) values ('2018_02_03_updateSessionTable.sql','ONGOING');

ALTER TABLE `pydb`.`BLSession` 
ADD COLUMN `nbReimbDewars` int(10) NULL DEFAULT NULL ;


update `pydb`.SchemaStatus set schemaStatus = 'DONE' where scriptName = '2018_02_03_updateSessionTable.sql';  
