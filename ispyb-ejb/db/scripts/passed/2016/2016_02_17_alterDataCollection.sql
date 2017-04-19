ALTER TABLE `DataCollection` 
ADD `xBeamPix` float NULL COMMENT 'Beam size in pixels' AFTER `yBeam` ,
ADD `yBeamPix` float NULL COMMENT 'Beam size in pixels' AFTER `xbeamPix` 