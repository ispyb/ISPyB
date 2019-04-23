select 
Protein.proposalId,
Protein.proteinId,
Protein.name,
Protein.acronym,
(select count(*) from Crystal where proteinId = Protein.proteinId) as CrystalCount,

(select count(*) from BLSample 
LEFT JOIN Crystal ON Crystal.crystalId = BLSample.crystalId where Crystal.proteinId = Protein.proteinId) as BLSampleCount,

(select count(*) from DataCollectionGroup 
LEFT JOIN BLSample ON DataCollectionGroup.blSampleId = BLSample.blSampleId
LEFT JOIN Crystal ON Crystal.crystalId = BLSample.crystalId where Crystal.proteinId = Protein.proteinId
) as DataCollectionGroupCount,


(select count(*) from DataCollection
LEFT JOIN  DataCollectionGroup ON DataCollectionGroup.dataCollectionGroupId = DataCollection.dataCollectionGroupId
LEFT JOIN BLSample ON DataCollectionGroup.blSampleId = BLSample.blSampleId
LEFT JOIN Crystal ON Crystal.crystalId = BLSample.crystalId 
where Crystal.proteinId = Protein.proteinId and DataCollection.numberOfImages > 4
) as DataCollectionCount,

(select count(*) from EnergyScan
LEFT JOIN BLSample ON EnergyScan.blSampleId = BLSample.blSampleId
LEFT JOIN Crystal ON Crystal.crystalId = BLSample.crystalId where Crystal.proteinId = Protein.proteinId
) as EnergyScanCount,

(select group_concat(distinct(EnergyScan.sessionId)) from EnergyScan
LEFT JOIN BLSample ON EnergyScan.blSampleId = BLSample.blSampleId
LEFT JOIN Crystal ON Crystal.crystalId = BLSample.crystalId where Crystal.proteinId = Protein.proteinId
) as EnergyScanSessionIdList,

(select count(*) from XFEFluorescenceSpectrum
LEFT JOIN BLSample ON XFEFluorescenceSpectrum.blSampleId = BLSample.blSampleId
LEFT JOIN Crystal ON Crystal.crystalId = BLSample.crystalId where Crystal.proteinId = Protein.proteinId
) as XFEFluorescenceSpectrumCount,

(select  group_concat(distinct(XFEFluorescenceSpectrum.sessionId)) from XFEFluorescenceSpectrum
LEFT JOIN BLSample ON XFEFluorescenceSpectrum.blSampleId = BLSample.blSampleId
LEFT JOIN Crystal ON Crystal.crystalId = BLSample.crystalId where Crystal.proteinId = Protein.proteinId
) as XFEFluorescenceSessionIdList,

(select max(DataCollection.dataCollectionId) from DataCollection 
LEFT JOIN  DataCollectionGroup ON DataCollectionGroup.dataCollectionGroupId = DataCollection.dataCollectionGroupId
LEFT JOIN BLSample ON DataCollectionGroup.blSampleId = BLSample.blSampleId
LEFT JOIN Crystal ON Crystal.crystalId = BLSample.crystalId 
where Crystal.proteinId = Protein.proteinId and DataCollection.numberOfImages < 5
) as latestDataCollectionId,

(select startTime from DataCollection where DataCollection.dataCollectionId = latestDataCollectionId) as latestDataCollectionTime,
(select DataCollectionGroup.sessionId from DataCollectionGroup, DataCollection where DataCollectionGroup.dataCollectionGroupId = DataCollection.dataCollectionGroupId and DataCollection.dataCollectionId = latestDataCollectionId) as latestSessionId,

(select count(distinct(BLSession.sessionId)) from BLSession
LEFT JOIN  DataCollectionGroup ON DataCollectionGroup.sessionId = BLSession.sessionId
LEFT JOIN BLSample ON DataCollectionGroup.blSampleId = BLSample.blSampleId
LEFT JOIN Crystal ON Crystal.crystalId = BLSample.crystalId 
where Crystal.proteinId = Protein.proteinId 
) as SessionCount,


(select group_concat(concat(BLSession.sessionId,"/", BLSession.beamlinename,"/",BLSession.startDate)) from BLSession 
LEFT JOIN  DataCollectionGroup ON DataCollectionGroup.sessionId = BLSession.sessionId
LEFT JOIN BLSample ON DataCollectionGroup.blSampleId = BLSample.blSampleId
LEFT JOIN Crystal ON Crystal.crystalId = BLSample.crystalId 
where Crystal.proteinId = Protein.proteinId 
) as SessionValuesList,


(select count(*) from DataCollection 
LEFT JOIN  DataCollectionGroup ON DataCollectionGroup.dataCollectionGroupId = DataCollection.dataCollectionGroupId
LEFT JOIN BLSample ON DataCollectionGroup.blSampleId = BLSample.blSampleId
LEFT JOIN Crystal ON Crystal.crystalId = BLSample.crystalId 
where Crystal.proteinId = Protein.proteinId and DataCollection.numberOfImages < 5
) as TestDataCollectionCount,


(select group_concat(DataCollection.dataCollectionId) from DataCollection
where DataCollection.dataCollectionGroupId in (select distinct(DataCollectionGroup.dataCollectionGroupId) from DataCollectionGroup 
LEFT JOIN BLSample ON DataCollectionGroup.blSampleId = BLSample.blSampleId
LEFT JOIN Crystal ON Crystal.crystalId = BLSample.crystalId where Crystal.proteinId = Protein.proteinId and DataCollection.numberOfImages < 5)) as TestDataCollectionIdList,

(select count(*) from AutoProcIntegration
LEFT JOIN DataCollection ON DataCollection.dataCollectionId = AutoProcIntegration.dataCollectionId
LEFT JOIN  DataCollectionGroup ON DataCollectionGroup.dataCollectionGroupId = DataCollection.dataCollectionGroupId
LEFT JOIN BLSample ON DataCollectionGroup.blSampleId = BLSample.blSampleId
LEFT JOIN Crystal ON Crystal.crystalId = BLSample.crystalId 
where Crystal.proteinId = Protein.proteinId 
) as AutoProcIntegrationCount,

(select count(*) from PhasingStep
LEFT JOIN AutoProcScaling ON PhasingStep.autoProcScalingId = AutoProcScaling.autoProcScalingId
LEFT JOIN AutoProcScaling_has_Int ON AutoProcScaling_has_Int.autoProcScalingId = AutoProcScaling.autoProcScalingId
LEFT JOIN AutoProcIntegration ON AutoProcIntegration.autoProcIntegrationId = AutoProcScaling_has_Int.autoProcIntegrationId
LEFT JOIN DataCollection ON DataCollection.dataCollectionId = AutoProcIntegration.dataCollectionId
LEFT JOIN  DataCollectionGroup ON DataCollectionGroup.dataCollectionGroupId = DataCollection.dataCollectionGroupId
LEFT JOIN BLSample ON DataCollectionGroup.blSampleId = BLSample.blSampleId
LEFT JOIN Crystal ON Crystal.crystalId = BLSample.crystalId 
where Crystal.proteinId = Protein.proteinId 
) as PhasingStepCount,

(select count(*) from PhasingStep
LEFT JOIN AutoProcScaling ON PhasingStep.autoProcScalingId = AutoProcScaling.autoProcScalingId
LEFT JOIN AutoProcScaling_has_Int ON AutoProcScaling_has_Int.autoProcScalingId = AutoProcScaling.autoProcScalingId
LEFT JOIN AutoProcIntegration ON AutoProcIntegration.autoProcIntegrationId = AutoProcScaling_has_Int.autoProcIntegrationId
LEFT JOIN DataCollection ON DataCollection.dataCollectionId = AutoProcIntegration.dataCollectionId
LEFT JOIN  DataCollectionGroup ON DataCollectionGroup.dataCollectionGroupId = DataCollection.dataCollectionGroupId
LEFT JOIN BLSample ON DataCollectionGroup.blSampleId = BLSample.blSampleId
LEFT JOIN Crystal ON Crystal.crystalId = BLSample.crystalId 
where Crystal.proteinId = Protein.proteinId AND  PhasingStep.method = 'SAD'
) as SADPhasingStepCount,

(select count(*) from PhasingStep
LEFT JOIN AutoProcScaling ON PhasingStep.autoProcScalingId = AutoProcScaling.autoProcScalingId
LEFT JOIN AutoProcScaling_has_Int ON AutoProcScaling_has_Int.autoProcScalingId = AutoProcScaling.autoProcScalingId
LEFT JOIN AutoProcIntegration ON AutoProcIntegration.autoProcIntegrationId = AutoProcScaling_has_Int.autoProcIntegrationId
LEFT JOIN DataCollection ON DataCollection.dataCollectionId = AutoProcIntegration.dataCollectionId
LEFT JOIN  DataCollectionGroup ON DataCollectionGroup.dataCollectionGroupId = DataCollection.dataCollectionGroupId
LEFT JOIN BLSample ON DataCollectionGroup.blSampleId = BLSample.blSampleId
LEFT JOIN Crystal ON Crystal.crystalId = BLSample.crystalId 
where Crystal.proteinId = Protein.proteinId AND  PhasingStep.method = 'MR'
) as MRPhasingStepCount,


(select count(*) from PhasingStep
LEFT JOIN AutoProcScaling ON PhasingStep.autoProcScalingId = AutoProcScaling.autoProcScalingId
LEFT JOIN AutoProcScaling_has_Int ON AutoProcScaling_has_Int.autoProcScalingId = AutoProcScaling.autoProcScalingId
LEFT JOIN AutoProcIntegration ON AutoProcIntegration.autoProcIntegrationId = AutoProcScaling_has_Int.autoProcIntegrationId
LEFT JOIN DataCollection ON DataCollection.dataCollectionId = AutoProcIntegration.dataCollectionId
LEFT JOIN  DataCollectionGroup ON DataCollectionGroup.dataCollectionGroupId = DataCollection.dataCollectionGroupId
LEFT JOIN BLSample ON DataCollectionGroup.blSampleId = BLSample.blSampleId
LEFT JOIN Crystal ON Crystal.crystalId = BLSample.crystalId 
where Crystal.proteinId = Protein.proteinId AND  PhasingStep.phasingStepType = 'MODELBUILDING'
) as ModelBuildingPhasingStepCount,

(select group_concat(distinct(spaceGroupShortName)) from SpaceGroup
LEFT JOIN PhasingStep ON PhasingStep.spaceGroupId = SpaceGroup.spaceGroupId
LEFT JOIN AutoProcScaling ON PhasingStep.autoProcScalingId = AutoProcScaling.autoProcScalingId
LEFT JOIN AutoProcScaling_has_Int ON AutoProcScaling_has_Int.autoProcScalingId = AutoProcScaling.autoProcScalingId
LEFT JOIN AutoProcIntegration ON AutoProcIntegration.autoProcIntegrationId = AutoProcScaling_has_Int.autoProcIntegrationId
LEFT JOIN DataCollection ON DataCollection.dataCollectionId = AutoProcIntegration.dataCollectionId
LEFT JOIN  DataCollectionGroup ON DataCollectionGroup.dataCollectionGroupId = DataCollection.dataCollectionGroupId
LEFT JOIN BLSample ON DataCollectionGroup.blSampleId = BLSample.blSampleId
LEFT JOIN Crystal ON Crystal.crystalId = BLSample.crystalId 
where Crystal.proteinId = Protein.proteinId AND  PhasingStep.phasingStepType = 'MODELBUILDING'
) as SpaceGroupModelBuildingPhasingStep,

(select group_concat(distinct(DataCollection.dataCollectionId)) from SpaceGroup
LEFT JOIN PhasingStep ON PhasingStep.spaceGroupId = SpaceGroup.spaceGroupId
LEFT JOIN AutoProcScaling ON PhasingStep.autoProcScalingId = AutoProcScaling.autoProcScalingId
LEFT JOIN AutoProcScaling_has_Int ON AutoProcScaling_has_Int.autoProcScalingId = AutoProcScaling.autoProcScalingId
LEFT JOIN AutoProcIntegration ON AutoProcIntegration.autoProcIntegrationId = AutoProcScaling_has_Int.autoProcIntegrationId
LEFT JOIN DataCollection ON DataCollection.dataCollectionId = AutoProcIntegration.dataCollectionId
LEFT JOIN  DataCollectionGroup ON DataCollectionGroup.dataCollectionGroupId = DataCollection.dataCollectionGroupId
LEFT JOIN BLSample ON DataCollectionGroup.blSampleId = BLSample.blSampleId
LEFT JOIN Crystal ON Crystal.crystalId = BLSample.crystalId 
where Crystal.proteinId = Protein.proteinId AND  PhasingStep.phasingStepType = 'MODELBUILDING'
) as ModelBuildingPhasingStepDataCollectionIdList,


(select count(*) from PhasingProgramRun
LEFT JOIN PhasingStep ON PhasingProgramRun.phasingProgramRunId = PhasingStep.programRunId
LEFT JOIN AutoProcScaling ON PhasingStep.autoProcScalingId = AutoProcScaling.autoProcScalingId
LEFT JOIN AutoProcScaling_has_Int ON AutoProcScaling_has_Int.autoProcScalingId = AutoProcScaling.autoProcScalingId
LEFT JOIN AutoProcIntegration ON AutoProcIntegration.autoProcIntegrationId = AutoProcScaling_has_Int.autoProcIntegrationId
LEFT JOIN DataCollection ON DataCollection.dataCollectionId = AutoProcIntegration.dataCollectionId
LEFT JOIN  DataCollectionGroup ON DataCollectionGroup.dataCollectionGroupId = DataCollection.dataCollectionGroupId
LEFT JOIN BLSample ON DataCollectionGroup.blSampleId = BLSample.blSampleId
LEFT JOIN Crystal ON Crystal.crystalId = BLSample.crystalId 
where Crystal.proteinId = Protein.proteinId
) as PhasingProgramRunCount,

(select count(*) from PhasingProgramAttachment
LEFT JOIN PhasingProgramRun ON PhasingProgramAttachment.phasingProgramRunId = PhasingProgramRun.phasingProgramRunId
LEFT JOIN PhasingStep ON PhasingProgramRun.phasingProgramRunId = PhasingStep.programRunId
LEFT JOIN AutoProcScaling ON PhasingStep.autoProcScalingId = AutoProcScaling.autoProcScalingId
LEFT JOIN AutoProcScaling_has_Int ON AutoProcScaling_has_Int.autoProcScalingId = AutoProcScaling.autoProcScalingId
LEFT JOIN AutoProcIntegration ON AutoProcIntegration.autoProcIntegrationId = AutoProcScaling_has_Int.autoProcIntegrationId
LEFT JOIN DataCollection ON DataCollection.dataCollectionId = AutoProcIntegration.dataCollectionId
LEFT JOIN  DataCollectionGroup ON DataCollectionGroup.dataCollectionGroupId = DataCollection.dataCollectionGroupId
LEFT JOIN BLSample ON DataCollectionGroup.blSampleId = BLSample.blSampleId
LEFT JOIN Crystal ON Crystal.crystalId = BLSample.crystalId 
where Crystal.proteinId = Protein.proteinId
) as PhasingProgramAttachementCount,

(select group_concat(distinct(PhasingProgramAttachment.phasingProgramAttachmentId)) from PhasingProgramAttachment
LEFT JOIN PhasingProgramRun ON PhasingProgramAttachment.phasingProgramRunId = PhasingProgramRun.phasingProgramRunId
LEFT JOIN PhasingStep ON PhasingProgramRun.phasingProgramRunId = PhasingStep.programRunId
LEFT JOIN AutoProcScaling ON PhasingStep.autoProcScalingId = AutoProcScaling.autoProcScalingId
LEFT JOIN AutoProcScaling_has_Int ON AutoProcScaling_has_Int.autoProcScalingId = AutoProcScaling.autoProcScalingId
LEFT JOIN AutoProcIntegration ON AutoProcIntegration.autoProcIntegrationId = AutoProcScaling_has_Int.autoProcIntegrationId
LEFT JOIN DataCollection ON DataCollection.dataCollectionId = AutoProcIntegration.dataCollectionId
LEFT JOIN  DataCollectionGroup ON DataCollectionGroup.dataCollectionGroupId = DataCollection.dataCollectionGroupId
LEFT JOIN BLSample ON DataCollectionGroup.blSampleId = BLSample.blSampleId
LEFT JOIN Crystal ON Crystal.crystalId = BLSample.crystalId 
where Crystal.proteinId = Protein.proteinId and PhasingProgramAttachment.fileType='IMAGE' AND fileName like '%.png'
) as ImagesPhasingProgramAttachementIds,

(select group_concat(distinct(PhasingProgramRun.phasingProgramRunId)) from PhasingProgramRun
LEFT JOIN PhasingStep ON PhasingProgramRun.phasingProgramRunId = PhasingStep.programRunId
LEFT JOIN AutoProcScaling ON PhasingStep.autoProcScalingId = AutoProcScaling.autoProcScalingId
LEFT JOIN AutoProcScaling_has_Int ON AutoProcScaling_has_Int.autoProcScalingId = AutoProcScaling.autoProcScalingId
LEFT JOIN AutoProcIntegration ON AutoProcIntegration.autoProcIntegrationId = AutoProcScaling_has_Int.autoProcIntegrationId
LEFT JOIN DataCollection ON DataCollection.dataCollectionId = AutoProcIntegration.dataCollectionId
LEFT JOIN  DataCollectionGroup ON DataCollectionGroup.dataCollectionGroupId = DataCollection.dataCollectionGroupId
LEFT JOIN BLSample ON DataCollectionGroup.blSampleId = BLSample.blSampleId
LEFT JOIN Crystal ON Crystal.crystalId = BLSample.crystalId 
where Crystal.proteinId = Protein.proteinId  AND  PhasingStep.phasingStepType = 'MODELBUILDING'
) as ModelBuildingPhasingProgramIdList,

(select group_concat(distinct(DataCollection.dataCollectionId)) from PhasingProgramRun
LEFT JOIN PhasingStep ON PhasingProgramRun.phasingProgramRunId = PhasingStep.programRunId
LEFT JOIN AutoProcScaling ON PhasingStep.autoProcScalingId = AutoProcScaling.autoProcScalingId
LEFT JOIN AutoProcScaling_has_Int ON AutoProcScaling_has_Int.autoProcScalingId = AutoProcScaling.autoProcScalingId
LEFT JOIN AutoProcIntegration ON AutoProcIntegration.autoProcIntegrationId = AutoProcScaling_has_Int.autoProcIntegrationId
LEFT JOIN DataCollection ON DataCollection.dataCollectionId = AutoProcIntegration.dataCollectionId
LEFT JOIN  DataCollectionGroup ON DataCollectionGroup.dataCollectionGroupId = DataCollection.dataCollectionGroupId
LEFT JOIN BLSample ON DataCollectionGroup.blSampleId = BLSample.blSampleId
LEFT JOIN Crystal ON Crystal.crystalId = BLSample.crystalId 
where Crystal.proteinId = Protein.proteinId  AND  PhasingStep.phasingStepType = 'MODELBUILDING'
) as ModelBuildingPhasingDataColletionIdList
from Protein
