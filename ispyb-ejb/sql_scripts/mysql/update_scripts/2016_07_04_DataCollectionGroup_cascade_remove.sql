 
ALTER TABLE `pydb`.`DataCollectionGroup` 
DROP FOREIGN KEY `DataCollectionGroup_ibfk_1`;
ALTER TABLE `pydb`.`DataCollectionGroup` 
ADD CONSTRAINT `DataCollectionGroup_ibfk_1`
  FOREIGN KEY (`blSampleId`)
  REFERENCES `pydb`.`BLSample` (`blSampleId`)
  ON DELETE NO ACTION
  ON UPDATE CASCADE;
