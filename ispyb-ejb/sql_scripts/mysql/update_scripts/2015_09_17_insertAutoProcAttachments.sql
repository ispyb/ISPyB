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
'53', 'edna_dimple.png', 'image for dimple', 'DIMPLE', 'output', '0'
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
'54', 'edna_dimple.log', 'log for dimple', 'DIMPLE', 'log', '0'
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
'55', 'edna_dimple.pdf', 'log for dimple', 'DIMPLE', 'log', '0'
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
'56', 'edna_dimple.mtz', 'mtz file for dimple', 'DIMPLE', 'output', '0'
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
'57', 'edna_unmerged_noanom_pointless_multirecord.mtz.gz', 'unmerged multirecord from pointless with noanom', 'SCALA', 'output', '0'
);

