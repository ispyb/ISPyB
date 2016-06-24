ALTER TABLE `GridInfo` (
  MODIFY `workflowMeshId` int(11) unsigned DEFAULT NULL,
  ADD `orientation` enum('vertical','horizontal') DEFAULT 'horizontal',
  ADD `dataCollectionGroupId` int(11) DEFAULT NULL,
  ADD `pixelspermicronX` float DEFAULT NULL,
  ADD `pixelspermicronY` float DEFAULT NULL,
  ADD `snapshot_offsetxpixel` float DEFAULT NULL,
  ADD `snapshot_offsetypixel` float DEFAULT NULL,
  ADD CONSTRAINT `GridInfo_ibfk_2` FOREIGN KEY (`dataCollectionGroupId`) REFERENCES `DataCollectionGroup` (`dataCollectionGroupId`);
