
SELECT 
    *,
    (SELECT 
            GROUP_CONCAT(phasingStepType)
        FROM
            PhasingStep
        WHERE
            v_datacollection_summary_phasing_autoProcScalingId = PhasingStep.autoProcScalingId) AS phasingStepType,
    (SELECT 
            GROUP_CONCAT(spaceGroupId)
        FROM
            PhasingStep
        WHERE
            v_datacollection_summary_phasing_autoProcScalingId = PhasingStep.autoProcScalingId) AS spaceGroupIds,
    (SELECT 
            GROUP_CONCAT(spaceGroupShortName)
        FROM
            SpaceGroup
        WHERE
            spaceGroupId IN (SELECT 
                    spaceGroupId
                FROM
                    PhasingStep
                WHERE
                    v_datacollection_summary_phasing_autoProcScalingId = PhasingStep.autoProcScalingId)) AS spaceGroupShortName,
    (SELECT 
            GROUP_CONCAT(scalingStatisticsType)
        FROM
            AutoProcScalingStatistics
        WHERE
            v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId) AS scalingStatisticsType,
    (SELECT 
            GROUP_CONCAT(resolutionLimitLow)
        FROM
            AutoProcScalingStatistics
        WHERE
            v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId) AS resolutionLimitLow,
    (SELECT 
            GROUP_CONCAT(resolutionLimitHigh)
        FROM
            AutoProcScalingStatistics
        WHERE
            v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId) AS resolutionLimitHigh,
    (SELECT 
            GROUP_CONCAT(completeness)
        FROM
            AutoProcScalingStatistics
        WHERE
            v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId) AS completeness,
    (SELECT 
            GROUP_CONCAT(multiplicity)
        FROM
            AutoProcScalingStatistics
        WHERE
            v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId) AS multiplicity,
    (SELECT 
            GROUP_CONCAT(anomalousMultiplicity)
        FROM
            AutoProcScalingStatistics
        WHERE
            v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId) AS anomalousMultiplicity,
    (SELECT 
            GROUP_CONCAT(ccHalf)
        FROM
            AutoProcScalingStatistics
        WHERE
            v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId) AS ccHalf,
    (SELECT 
            GROUP_CONCAT(meanIOverSigI)
        FROM
            AutoProcScalingStatistics
        WHERE
            v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId) AS meanIOverSigI,
    (SELECT 
            GROUP_CONCAT(anomalousCompleteness)
        FROM
            AutoProcScalingStatistics
        WHERE
            v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId) AS anomalousCompleteness,
    (SELECT 
            GROUP_CONCAT(rMerge)
        FROM
            AutoProcScalingStatistics
        WHERE
            v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId) AS rMerge,
    (SELECT 
            GROUP_CONCAT(rMeasWithinIPlusIMinus)
        FROM
            AutoProcScalingStatistics
        WHERE
            v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId) AS rMeasWithinIPlusIMinus,
    (SELECT 
            GROUP_CONCAT(rMeasAllIPlusIMinus)
        FROM
            AutoProcScalingStatistics
        WHERE
            v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId) AS rMeasAllIPlusIMinus,
    (SELECT 
            GROUP_CONCAT(rPimWithinIPlusIMinus)
        FROM
            AutoProcScalingStatistics
        WHERE
            v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId) AS rPimWithinIPlusIMinus,
    (SELECT 
            GROUP_CONCAT(rPimAllIPlusIMinus)
        FROM
            AutoProcScalingStatistics
        WHERE
            v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId) AS rPimAllIPlusIMinus,
    (SELECT 
            GROUP_CONCAT(fractionalPartialBias)
        FROM
            AutoProcScalingStatistics
        WHERE
            v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId) AS fractionalPartialBias,
    (SELECT 
            GROUP_CONCAT(nTotalObservations)
        FROM
            AutoProcScalingStatistics
        WHERE
            v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId) AS nTotalObservations,
    (SELECT 
            GROUP_CONCAT(nTotalUniqueObservations)
        FROM
            AutoProcScalingStatistics
        WHERE
            v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId) AS nTotalUniqueObservations,
    (SELECT 
            GROUP_CONCAT(anomalous)
        FROM
            AutoProcScalingStatistics
        WHERE
            v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId) AS anomalous,
    (SELECT 
            GROUP_CONCAT(sigAno)
        FROM
            AutoProcScalingStatistics
        WHERE
            v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId) AS sigAno,
     (SELECT 
            GROUP_CONCAT(ccAno)
        FROM
            AutoProcScalingStatistics
        WHERE
            v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId) AS ccAno,
            
            
               (SELECT 
            GROUP_CONCAT(isa)
        FROM
            AutoProcScalingStatistics
        WHERE
            v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId) AS isa
            
FROM
    v_datacollection_autoprocintegration

