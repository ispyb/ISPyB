package ispyb.server.biosaxs.services.sql;

public class SqlTableMapper {
	public static String getCrystalTable() {
		return "Crystal.crystalId as Crystal_crystalId,\r\n"
				+ "Crystal.diffractionPlanId as Crystal_diffractionPlanId,\r\n"
				+ "Crystal.proteinId as Crystal_proteinId,\r\n"
				+ "Crystal.crystalUUID as Crystal_crystalUUID,\r\n"
				+ "Crystal.name as Crystal_name,\r\n"
				+ "Crystal.spaceGroup as Crystal_spaceGroup,\r\n"
				+ "Crystal.morphology as Crystal_morphology,\r\n"
				+ "Crystal.color as Crystal_color,\r\n"
				+ "Crystal.size_X as Crystal_size_X,\r\n"
				+ "Crystal.size_Y as Crystal_size_Y,\r\n"
				+ "Crystal.size_Z as Crystal_size_Z,\r\n"
				+ "Crystal.cell_a as Crystal_cell_a,\r\n"
				+ "Crystal.cell_b as Crystal_cell_b,\r\n"
				+ "Crystal.cell_c as Crystal_cell_c,\r\n"
				+ "Crystal.cell_alpha as Crystal_cell_alpha,\r\n"
				+ "Crystal.cell_beta as Crystal_cell_beta,\r\n"
				+ "Crystal.cell_gamma as Crystal_cell_gamma,\r\n"
				+ "Crystal.comments as Crystal_comments,\r\n"
				+ "Crystal.pdbFileName as Crystal_pdbFileName,\r\n"
				+ "Crystal.pdbFilePath as Crystal_pdbFilePath,\r\n"
				+ "Crystal.recordTimeStamp as Crystal_recordTimeStamp\r\n";
	}

	public static String getBLSampleTable() {
		return "BLSample.blSampleId as BLSample_blSampleId,\r\n"
				+ "BLSample.diffractionPlanId as BLSample_diffractionPlanId,\r\n"
				+ "BLSample.crystalId as BLSample_crystalId,\r\n"
				+ "BLSample.containerId as BLSample_containerId,\r\n"
				+ "BLSample.name as BLSample_name,\r\n"
				+ "BLSample.code as BLSample_code,\r\n"
				+ "BLSample.location as BLSample_location,\r\n"
				+ "BLSample.holderLength as BLSample_holderLength,\r\n"
				+ "BLSample.loopLength as BLSample_loopLength,\r\n"
				+ "BLSample.loopType as BLSample_loopType,\r\n"
				+ "BLSample.wireWidth as BLSample_wireWidth,\r\n"
				+ "BLSample.comments as BLSample_comments,\r\n"
				+ "BLSample.completionStage as BLSample_completionStage,\r\n"
				+ "BLSample.structureStage as BLSample_structureStage,\r\n"
				+ "BLSample.publicationStage as BLSample_publicationStage,\r\n"
				+ "BLSample.publicationComments as BLSample_publicationComments,\r\n"
				+ "BLSample.blSampleStatus as BLSample_blSampleStatus,\r\n"
				+ "BLSample.isInSampleChanger as BLSample_isInSampleChanger,\r\n"
				+ "BLSample.lastKnownCenteringPosition as BLSample_lastKnownCenteringPosition,\r\n"
				+ "BLSample.recordTimeStamp as BLSample_recordTimeStamp,\r\n"
				+ "BLSample.SMILES as BLSample_SMILES \r\n";
	}

	public static String getProteinTable() {
		return " Protein.proteinId as Protein_proteinId,\r\n"
				+ " Protein.proposalId as Protein_proposalId,\r\n"
				+ " Protein.name as Protein_name,\r\n"
				+ " Protein.acronym as Protein_acronym,\r\n"
				+ " Protein.molecularMass as Protein_molecularMass,\r\n"
				+ " Protein.proteinType as Protein_proteinType,\r\n"
				+ " Protein.sequence as Protein_sequence,\r\n"
				+ " Protein.personId as Protein_personId,\r\n"
				+ " Protein.bltimeStamp as Protein_bltimeStamp,\r\n"
				+ " Protein.isCreatedBySampleSheet as Protein_isCreatedBySampleSheet\r\n";
	}

	public static String getBLSessionTable() {
		return "BLSession.sessionId as BLSession_sessionId,\r\n"
				+ "BLSession.expSessionPk as BLSession_expSessionPk,\r\n"
				+ "BLSession.beamLineSetupId as BLSession_beamLineSetupId,\r\n"
				+ "BLSession.proposalId as BLSession_proposalId,\r\n"
				+ "BLSession.projectCode as BLSession_projectCode,\r\n"
				+ "BLSession.startDate as BLSession_startDate,\r\n"
				+ "BLSession.endDate as BLSession_endDate,\r\n"
				+ "BLSession.beamLineName as BLSession_beamLineName,\r\n"
				+ "BLSession.scheduled as BLSession_scheduled,\r\n"
				+ "BLSession.nbShifts as BLSession_nbShifts,\r\n"
				+ "BLSession.comments as BLSession_comments,\r\n"
				+ "BLSession.beamLineOperator as BLSession_beamLineOperator,\r\n"
				+ "BLSession.visit_number as BLSession_visit_number,\r\n"
				+ "BLSession.bltimeStamp as BLSession_bltimeStamp,\r\n"
				+ "BLSession.usedFlag as BLSession_usedFlag,\r\n"
				+ "BLSession.sessionTitle as BLSession_sessionTitle,\r\n"
				+ "BLSession.structureDeterminations as BLSession_structureDeterminations,\r\n"
				+ "BLSession.dewarTransport as BLSession_dewarTransport,\r\n"
				+ "BLSession.databackupFrance as BLSession_databackupFrance,\r\n"
				+ "BLSession.databackupEurope as BLSession_databackupEurope,\r\n"
				+ "BLSession.operatorSiteNumber as BLSession_operatorSiteNumber,\r\n"
				+ "BLSession.lastUpdate as BLSession_lastUpdate,\r\n"
				+ "BLSession.nbReimbDewars as BLSession_nbReimbDewars,\r\n"
				+ "BLSession.protectedData as BLSession_protectedData";
	}

	public static String getProposalTable() {
		return "	Proposal.proposalId as Proposal_proposalId,\r\n"
				+ "	Proposal.personId as Proposal_personId,\r\n"
				+ "	Proposal.title as Proposal_title,\r\n"
				+ "	Proposal.proposalCode as Proposal_proposalCode,\r\n"
				+ "	Proposal.proposalNumber as Proposal_proposalNumber,\r\n"
				+ "	Proposal.proposalType as Proposal_proposalType ";
	}

	public static String getShippingTable() {
		return "Shipping.shippingId as Shipping_shippingId,\r\n"
				+ "Shipping.proposalId as Shipping_proposalId,\r\n"
				+ "Shipping.shippingName as Shipping_shippingName,\r\n"
				+ "Shipping.deliveryAgent_agentName as Shipping_deliveryAgent_agentName,\r\n"
				+ "Shipping.deliveryAgent_shippingDate as Shipping_deliveryAgent_shippingDate,\r\n"
				+ "Shipping.deliveryAgent_deliveryDate as Shipping_deliveryAgent_deliveryDate,\r\n"
				+ "Shipping.deliveryAgent_agentCode as Shipping_deliveryAgent_agentCode,\r\n"
				+ "Shipping.deliveryAgent_flightCode as Shipping_deliveryAgent_flightCode,\r\n"
				+ "Shipping.shippingStatus as Shipping_shippingStatus,\r\n"
				+ "Shipping.bltimeStamp as Shipping_bltimeStamp,\r\n"
				+ "Shipping.laboratoryId as Shipping_laboratoryId,\r\n"
				+ "Shipping.isStorageShipping as Shipping_isStorageShipping,\r\n"
				+ "Shipping.creationDate as Shipping_creationDate,\r\n"
				+ "Shipping.comments as Shipping_comments,\r\n"
				+ "Shipping.sendingLabContactId as Shipping_sendingLabContactId,\r\n"
				+ "Shipping.returnLabContactId as Shipping_returnLabContactId,\r\n"
				+ "Shipping.returnCourier as Shipping_returnCourier,\r\n"
				+ "Shipping.dateOfShippingToUser as Shipping_dateOfShippingToUser,\r\n"
				+ "Shipping.shippingType as Shipping_shippingType\r\n";
	}

	public static String getDewarTable() {
		return "Dewar.dewarId  as Dewar_dewarId,  \r\n"
				+ "Dewar.shippingId as Dewar_shippingId,                 \r\n"
				+ "Dewar.code as Dewar_code,                \r\n"
				+ "Dewar.comments as Dewar_comments,                      \r\n"
				+ "Dewar.storageLocation as Dewar_storageLocation,               \r\n"
				+ "Dewar.dewarStatus as Dewar_dewarStatus,                   \r\n"
				+ "Dewar.bltimeStamp as Dewar_bltimeStamp,                    \r\n"
				+ "Dewar.isStorageDewar as Dewar_isStorageDewar,                \r\n"
				+ "Dewar.barCode as Dewar_barCode,                       \r\n"
				+ "Dewar.firstExperimentId as Dewar_firstExperimentId,\r\n"
				+ "Dewar.customsValue as Dewar_customsValue,                  \r\n"
				+ "Dewar.transportValue as Dewar_transportValue,               \r\n"
				+ "Dewar.trackingNumberToSynchrotron as Dewar_trackingNumberToSynchrotron,  \r\n"
				+ "Dewar.trackingNumberFromSynchrotron as Dewar_trackingNumberFromSynchrotron, \r\n"
				+ "Dewar.isReimbursed as Dewar_isReimbursed, \r\n"
				+ "Dewar.type as Dewar_type ";
	}

	public static String getContainerTable() {
		return "Container.containerId as Container_containerId,\r\n"
				+ "Container.dewarId as Container_dewarId,\r\n"
				+ "Container.code as Container_code,\r\n"
				+ "Container.containerType as Container_containerType,\r\n"
				+ "Container.capacity as Container_capacity,\r\n"
				+ "Container.beamlineLocation as Container_beamlineLocation,\r\n"
				+ "Container.sampleChangerLocation as Container_sampleChangerLocation,\r\n"
				+ "Container.containerStatus as Container_containerStatus,\r\n"
				+ "Container.bltimeStamp as Container_bltimeStamp ";
	}

	public static String getDataCollectionGroupTable() {
		return "DataCollectionGroup.dataCollectionGroupId as DataCollectionGroup_dataCollectionGroupId,\r\n"
				+ "DataCollectionGroup.blSampleId as DataCollectionGroup_blSampleId,\r\n"
				+ "DataCollectionGroup.sessionId as DataCollectionGroup_sessionId,\r\n"
				+ "DataCollectionGroup.workflowId as DataCollectionGroup_workflowId,\r\n"
				+ "DataCollectionGroup.experimentType as DataCollectionGroup_experimentType,\r\n"
				+ "DataCollectionGroup.startTime as DataCollectionGroup_startTime,\r\n"
				+ "DataCollectionGroup.endTime as DataCollectionGroup_endTime,\r\n"
				+ "DataCollectionGroup.crystalClass as DataCollectionGroup_crystalClass,\r\n"
				+ "DataCollectionGroup.comments as DataCollectionGroup_comments,\r\n"
				+ "DataCollectionGroup.detectorMode as DataCollectionGroup_detectorMode,\r\n"
				+ "DataCollectionGroup.actualSampleBarcode as DataCollectionGroup_actualSampleBarcode,\r\n"
				+ "DataCollectionGroup.actualSampleSlotInContainer as DataCollectionGroup_actualSampleSlotInContainer,\r\n"
				+ "DataCollectionGroup.actualContainerBarcode as DataCollectionGroup_actualContainerBarcode,\r\n"
				+ "DataCollectionGroup.actualContainerSlotInSC as DataCollectionGroup_actualContainerSlotInSC,\r\n"
				+ "DataCollectionGroup.xtalSnapshotFullPath as DataCollectionGroup_xtalSnapshotFullPath ";
	}

	public static String getImageTable() {
		return "Image.imageId as Image_imageId,\r\n"
				+ "Image.dataCollectionId as Image_dataCollectionId,\r\n"
				+ "Image.imageNumber as Image_imageNumber,\r\n"
				+ "Image.fileName as Image_fileName,\r\n"
				+ "Image.fileLocation as Image_fileLocation,\r\n"
				+ "Image.measuredIntensity as Image_measuredIntensity,\r\n"
				+ "Image.jpegFileFullPath as Image_jpegFileFullPath,\r\n"
				+ "Image.jpegThumbnailFileFullPath as Image_jpegThumbnailFileFullPath,\r\n"
				+ "Image.temperature as Image_temperature,\r\n"
				+ "Image.cumulativeIntensity as Image_cumulativeIntensity,\r\n"
				+ "Image.synchrotronCurrent as Image_synchrotronCurrent,\r\n"
				+ "Image.comments as Image_comments,\r\n"
				+ "Image.machineMessage as Image_machineMessage,\r\n"
				+ "Image.recordTimeStamp as Image_recordTimeStamp ";
	}

	public static String getWorkflowTable() {
		return "Workflow.workflowId as Workflow_workflowId,\r\n"
				+ "Workflow.proposalId as Workflow_proposalId,\r\n"
				+ "Workflow.workflowTitle as Workflow_workflowTitle,\r\n"
				+ "Workflow.workflowType as Workflow_workflowType,\r\n"
				+ "Workflow.comments as Workflow_comments,\r\n"
				+ "Workflow.status as Workflow_status,\r\n"
				+ "Workflow.resultFilePath as Workflow_resultFilePath,\r\n"
				+ "Workflow.logFilePath as Workflow_logFilePath,\r\n"
				+ "Workflow.recordTimeStamp as Workflow_recordTimeStamp ";
	}

	public static String getWorkflowMeshTable() {
		return "WorkflowMesh.workflowMeshId as WorkflowMesh_workflowMeshId,\r\n"
				+ "WorkflowMesh.workflowId as WorkflowMesh_workflowId,\r\n"
				+ "WorkflowMesh.bestPositionId as WorkflowMesh_bestPositionId,\r\n"
				+ "WorkflowMesh.bestImageId as WorkflowMesh_bestImageId,\r\n"
				+ "WorkflowMesh.value1 as WorkflowMesh_value1,\r\n"
				+ "WorkflowMesh.value2 as WorkflowMesh_value2,\r\n"
				+ "WorkflowMesh.value3 as WorkflowMesh_value3,\r\n"
				+ "WorkflowMesh.value4 as WorkflowMesh_value4,\r\n"
				+ "WorkflowMesh.cartographyPath as WorkflowMesh_cartographyPath,\r\n"
				+ "WorkflowMesh.recordTimeStamp as WorkflowMesh_recordTimeStamp ";
	}

	public static String getDataCollectionTable() {
		return "DataCollection.dataCollectionId as DataCollection_dataCollectionId,\r\n"
				+ "DataCollection.dataCollectionGroupId as DataCollection_dataCollectionGroupId,\r\n"
				+ "DataCollection.strategySubWedgeOrigId as DataCollection_strategySubWedgeOrigId,\r\n"
				+ "DataCollection.detectorId as DataCollection_detectorId,\r\n"
				+ "DataCollection.blSubSampleId as DataCollection_blSubSampleId,\r\n"
				+ "DataCollection.dataCollectionNumber as DataCollection_dataCollectionNumber,\r\n"
				+ "DataCollection.startTime as DataCollection_startTime,\r\n"
				+ "DataCollection.endTime as DataCollection_endTime,\r\n"
				+ "DataCollection.runStatus as DataCollection_runStatus,\r\n"
				+ "DataCollection.axisStart as DataCollection_axisStart,\r\n"
				+ "DataCollection.axisEnd as DataCollection_axisEnd,\r\n"
				+ "DataCollection.axisRange as DataCollection_axisRange,\r\n"
				+ "DataCollection.overlap as DataCollection_overlap,\r\n"
				+ "DataCollection.numberOfImages as DataCollection_numberOfImages,\r\n"
				+ "DataCollection.startImageNumber as DataCollection_startImageNumber,\r\n"
				+ "DataCollection.numberOfPasses as DataCollection_numberOfPasses,\r\n"
				+ "DataCollection.exposureTime as DataCollection_exposureTime,\r\n"
				+ "DataCollection.imageDirectory as DataCollection_imageDirectory,\r\n"
				+ "DataCollection.imagePrefix as DataCollection_imagePrefix,\r\n"
				+ "DataCollection.imageSuffix as DataCollection_imageSuffix,\r\n"
				+ "DataCollection.fileTemplate as DataCollection_fileTemplate,\r\n"
				+ "DataCollection.wavelength as DataCollection_wavelength,\r\n"
				+ "DataCollection.resolution as DataCollection_resolution,\r\n"
				+ "DataCollection.detectorDistance as DataCollection_detectorDistance,\r\n"
				+ "DataCollection.xBeam as DataCollection_xBeam,\r\n"
				+ "DataCollection.yBeam as DataCollection_yBeam,\r\n"
				+ "DataCollection.comments as DataCollection_comments,\r\n"
				+ "DataCollection.printableForReport as DataCollection_printableForReport,\r\n"
				+ "DataCollection.slitGapVertical as DataCollection_slitGapVertical,\r\n"
				+ "DataCollection.slitGapHorizontal as DataCollection_slitGapHorizontal,\r\n"
				+ "DataCollection.transmission as DataCollection_transmission,\r\n"
				+ "DataCollection.synchrotronMode as DataCollection_synchrotronMode,\r\n"
				+ "DataCollection.xtalSnapshotFullPath1 as DataCollection_xtalSnapshotFullPath1,\r\n"
				+ "DataCollection.xtalSnapshotFullPath2 as DataCollection_xtalSnapshotFullPath2,\r\n"
				+ "DataCollection.xtalSnapshotFullPath3 as DataCollection_xtalSnapshotFullPath3,\r\n"
				+ "DataCollection.xtalSnapshotFullPath4 as DataCollection_xtalSnapshotFullPath4,\r\n"
				+ "DataCollection.rotationAxis as DataCollection_rotationAxis,\r\n"
				+ "DataCollection.phiStart as DataCollection_phiStart,\r\n"
				+ "DataCollection.kappaStart as DataCollection_kappaStart,\r\n"
				+ "DataCollection.omegaStart as DataCollection_omegaStart,\r\n"
				+ "DataCollection.resolutionAtCorner as DataCollection_resolutionAtCorner,\r\n"
				+ "DataCollection.detector2Theta as DataCollection_detector2Theta,\r\n"
				+ "DataCollection.undulatorGap1 as DataCollection_undulatorGap1,\r\n"
				+ "DataCollection.undulatorGap2 as DataCollection_undulatorGap2,\r\n"
				+ "DataCollection.undulatorGap3 as DataCollection_undulatorGap3,\r\n"
				+ "DataCollection.beamSizeAtSampleX as DataCollection_beamSizeAtSampleX,\r\n"
				+ "DataCollection.beamSizeAtSampleY as DataCollection_beamSizeAtSampleY,\r\n"
				+ "DataCollection.centeringMethod as DataCollection_centeringMethod,\r\n"
				+ "DataCollection.averageTemperature as DataCollection_averageTemperature,\r\n"
				+ "DataCollection.actualCenteringPosition as DataCollection_actualCenteringPosition,\r\n"
				+ "DataCollection.beamShape as DataCollection_beamShape,\r\n"
				+ "DataCollection.flux as DataCollection_flux,\r\n"
				+ "DataCollection.flux_end as DataCollection_flux_end,\r\n"
				+ "DataCollection.totalAbsorbedDose as DataCollection_totalAbsorbedDose,\r\n"
				+ "DataCollection.bestWilsonPlotPath as DataCollection_bestWilsonPlotPath ";
	}

}
