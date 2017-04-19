 ALTER TABLE `IspybAutoProcAttachment` CHANGE `step` `step` ENUM( 'XDS', 'XSCALE', 'SCALA', 'SCALEPACK', 'TRUNCATE', 'DIMPLE' ) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT 'XDS' COMMENT 'step where the file is generated'; 
 
 INSERT INTO `pydb`.`IspybAutoProcAttachment` (
`autoProcAttachmentId` ,
`fileName` ,
`description` ,
`step` ,
`fileCategory` ,
`hasGraph`
)
VALUES (
'53', 'dimple.png', 'image for dimple', 'DIMPLE', 'output', '0'
);

INSERT INTO `pydb`.`IspybAutoProcAttachment` (
`autoProcAttachmentId` ,
`fileName` ,
`description` ,
`step` ,
`fileCategory` ,
`hasGraph`
)
VALUES (
'54', 'dimple.log', 'log for dimple', 'DIMPLE', 'log', '0'
);

INSERT INTO `pydb`.`IspybAutoProcAttachment` (
`autoProcAttachmentId` ,
`fileName` ,
`description` ,
`step` ,
`fileCategory` ,
`hasGraph`
)
VALUES (
'55', 'dimple.pdf', 'log for dimple', 'DIMPLE', 'log', '0'
);

INSERT INTO `pydb`.`IspybAutoProcAttachment` (
`autoProcAttachmentId` ,
`fileName` ,
`description` ,
`step` ,
`fileCategory` ,
`hasGraph`
)
VALUES (
'56', 'dimple.mtz', 'mtz file for dimple', 'DIMPLE', 'output', '0'
);

INSERT INTO `pydb`.`IspybAutoProcAttachment` (
`autoProcAttachmentId` ,
`fileName` ,
`description` ,
`step` ,
`fileCategory` ,
`hasGraph`
)
VALUES (
'57', 'pointless_multirecord.mtz.gz', 'multirecord from pointless', 'SCALA', 'output', '0'
);

INSERT INTO `pydb`.`IspybAutoProcAttachment` (
`autoProcAttachmentId` ,
`fileName` ,
`description` ,
`step` ,
`fileCategory` ,
`hasGraph`
)
VALUES (
'58', 'dimple.pdb', 'pdb file for dimple', 'DIMPLE', 'output', '0'
);

INSERT INTO `pydb`.`IspybAutoProcAttachment` (
`autoProcAttachmentId` ,
`fileName` ,
`description` ,
`step` ,
`fileCategory` ,
`hasGraph`
)
VALUES (
'59', 'xtriage_noanom.log', 'log file for xtriage', 'SCALEPACK', 'log', '0'
);

