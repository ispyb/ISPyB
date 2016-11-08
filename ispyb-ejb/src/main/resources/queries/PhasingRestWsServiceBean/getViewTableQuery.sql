select 
*,
(SELECT GROUP_CONCAT(`PhasingStatistics`.`metric` SEPARATOR ',')
    FROM
        `PhasingStatistics`
    WHERE
        (`PhasingStatistics`.`phasingStepId` = `PhasingStep_phasingStepId`)) AS `metric`,
(SELECT 
        GROUP_CONCAT(`PhasingStatistics`.`statisticsValue` SEPARATOR ',')
    FROM
        `PhasingStatistics`
    WHERE
        (`PhasingStatistics`.`phasingStepId` = `PhasingStep_phasingStepId`)) AS `statisticsValue`,
(SELECT 
        `PhasingProgramAttachment`.`phasingProgramAttachmentId`
    FROM
        `PhasingProgramAttachment`
    WHERE
        ((`PhasingProgramAttachment`.`phasingProgramRunId` = `PhasingProgramRun_phasingProgramRunId`)
            AND (`PhasingProgramAttachment`.`fileName` LIKE '%csv%'))
    ORDER BY `PhasingProgramAttachment`.`phasingProgramAttachmentId`) AS `csv`
from v_phasing
