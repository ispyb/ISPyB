ALTER TABLE `Container`
  ADD `screenId` int(11) unsigned DEFAULT NULL,
  ADD `scheduleId` int(11) unsigned DEFAULT NULL,
  ADD `barcode` varchar(45) DEFAULT NULL,
  ADD `imagerId` int(11) unsigned DEFAULT NULL,
  ADD `sessionId` int(10) unsigned DEFAULT NULL,
  ADD CONSTRAINT `Container_ibfk2` FOREIGN KEY (`screenId`) REFERENCES `Screen` (`screenId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `Container_ibfk3` FOREIGN KEY (`scheduleId`) REFERENCES `Schedule` (`scheduleId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `Container_ibfk4` FOREIGN KEY (`imagerId`) REFERENCES `Imager` (`imagerId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `Container_ibfk6` FOREIGN KEY (`sessionId`) REFERENCES `BLSession` (`sessionId`);
