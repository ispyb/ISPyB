ALTER TABLE `DiffractionPlan` 
CHANGE `experimentKind` 
`experimentKind` ENUM('Default','MXPressE','MXPressO','MXPressE_SAD','MXScore','MXPressM'
 ,'MAD','SAD','Fixed','Ligand binding','Refinement','OSC','MAD - Inverse Beam','SAD - Inverse Beam');
 
 ALTER TABLE `DiffractionPlan`  ADD `preferredBeamDiameter` FLOAT NULL AFTER `preferredBeamSizeY`;