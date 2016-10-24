
-- 

USE `pydb`;

ALTER TABLE `BLSample` ADD `lastImageURL` varchar(255);

ALTER TABLE `EnergyScan` MODIFY `blSampleId` INT(10) UNSIGNED;

ALTER TABLE `EnergyScan`
  ADD CONSTRAINT ES_ibfk_2 FOREIGN KEY (blSampleId) REFERENCES BLSample (blSampleId) 
  ON DELETE NO ACTION ON UPDATE NO ACTION ;
  
ALTER TABLE EnergyScan
  ADD blSubSampleId int(11) unsigned,
  ADD CONSTRAINT ES_ibfk_3 FOREIGN KEY (blSubSampleId) REFERENCES BLSubSample (blSubSampleId) 
  ON DELETE NO ACTION ON UPDATE NO ACTION ;

ALTER TABLE XFEFluorescenceSpectrum
  ADD blSubSampleId int(11) unsigned,
  ADD CONSTRAINT XFE_ibfk_3 FOREIGN KEY (blSubSampleId) REFERENCES BLSubSample (blSubSampleId) 
  ON DELETE NO ACTION ON UPDATE NO ACTION ;
  
DROP TABLE BLSample_has_EnergyScan;