select 
* ,
GROUP_CONCAT(`PhasingStatistics_phasingStatisticsId` SEPARATOR ', ') AS `PhasingStatistics_phasingStatisticsIds`,
GROUP_CONCAT(`PhasingStatistics_metric` SEPARATOR ', ') AS `metric`,
GROUP_CONCAT(`PhasingStatistics_statisticsValue` SEPARATOR ', ') AS `statisticsValue`,
(SELECT GROUP_CONCAT(PhasingProgramAttachment.phasingProgramAttachmentId)
FROM `PhasingProgramAttachment` 
WHERE ((`PhasingProgramAttachment`.`phasingProgramRunId` = `PhasingProgramRun_phasingProgramRunId`) AND (`PhasingProgramAttachment`.`fileName` LIKE '%.csv%')) 
ORDER BY `PhasingProgramAttachment`.`phasingProgramAttachmentId`) AS `csv`,

(SELECT GROUP_CONCAT(PhasingProgramAttachment.phasingProgramAttachmentId)
FROM `PhasingProgramAttachment` 
WHERE ((`PhasingProgramAttachment`.`phasingProgramRunId` = `PhasingProgramRun_phasingProgramRunId`) AND (`PhasingProgramAttachment`.`fileName` LIKE '%.map%')) 
ORDER BY `PhasingProgramAttachment`.`phasingProgramAttachmentId`) AS `map` , 

(SELECT GROUP_CONCAT(PhasingProgramAttachment.phasingProgramAttachmentId)
FROM `PhasingProgramAttachment` 
WHERE ((`PhasingProgramAttachment`.`phasingProgramRunId` = `PhasingProgramRun_phasingProgramRunId`) AND (`PhasingProgramAttachment`.`fileName` LIKE '%.pdb%')) 
ORDER BY `PhasingProgramAttachment`.`phasingProgramAttachmentId`) AS `pdb`,

(SELECT GROUP_CONCAT(PhasingProgramAttachment.fileName)
FROM `PhasingProgramAttachment` 
WHERE ((`PhasingProgramAttachment`.`phasingProgramRunId` = `PhasingProgramRun_phasingProgramRunId`) AND (`PhasingProgramAttachment`.`fileName` LIKE '%.map%')) 
ORDER BY `PhasingProgramAttachment`.`phasingProgramAttachmentId`) AS `mapFileName` , 

(SELECT GROUP_CONCAT(PhasingProgramAttachment.fileName)
FROM `PhasingProgramAttachment` 
WHERE ((`PhasingProgramAttachment`.`phasingProgramRunId` = `PhasingProgramRun_phasingProgramRunId`) AND (`PhasingProgramAttachment`.`fileName` LIKE '%.pdb%')) 
ORDER BY `PhasingProgramAttachment`.`phasingProgramAttachmentId`) AS `pdbFileName` , 

(SELECT GROUP_CONCAT(PhasingProgramAttachment.fileName)
FROM `PhasingProgramAttachment` 
WHERE ((`PhasingProgramAttachment`.`phasingProgramRunId` = `PhasingProgramRun_phasingProgramRunId`) AND (`PhasingProgramAttachment`.`fileName` LIKE '%.csv%')) 
ORDER BY `PhasingProgramAttachment`.`phasingProgramAttachmentId`) AS `csvFileName` , 

(SELECT GROUP_CONCAT(PhasingProgramAttachment.phasingProgramAttachmentId)
FROM `PhasingProgramAttachment` 
WHERE ((`PhasingProgramAttachment`.`phasingProgramRunId` = `PhasingProgramRun_phasingProgramRunId`) AND (`PhasingProgramAttachment`.`fileName` LIKE '%.png%')) 
ORDER BY `PhasingProgramAttachment`.`phasingProgramAttachmentId`) AS `png`,

(SELECT GROUP_CONCAT(PhasingProgramAttachment.phasingProgramAttachmentId)
FROM `PhasingProgramAttachment` 
WHERE ((`PhasingProgramAttachment`.`phasingProgramRunId` = `PhasingProgramRun_phasingProgramRunId`) AND (`PhasingProgramAttachment`.`fileName` LIKE '%.png%')) 
ORDER BY `PhasingProgramAttachment`.`phasingProgramAttachmentId`) AS `phasingProgramAttachmentId`,

(SELECT GROUP_CONCAT(PhasingProgramAttachment.fileType)
FROM `PhasingProgramAttachment` 
WHERE ((`PhasingProgramAttachment`.`phasingProgramRunId` = `PhasingProgramRun_phasingProgramRunId`) AND (`PhasingProgramAttachment`.`fileName` LIKE '%.png%')) 
ORDER BY `PhasingProgramAttachment`.`phasingProgramAttachmentId`) AS `fileType`

from v_phasing 