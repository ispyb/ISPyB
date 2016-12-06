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


(SELECT GROUP_CONCAT(PhasingProgramAttachment.phasingProgramAttachmentId)
FROM `PhasingProgramAttachment` 
WHERE ((`PhasingProgramAttachment`.`phasingProgramRunId` = `PhasingProgramRun_phasingProgramRunId`) AND (`PhasingProgramAttachment`.`fileName` LIKE '%.png%')) 
ORDER BY `PhasingProgramAttachment`.`phasingProgramAttachmentId`) AS `png`


from v_phasing 