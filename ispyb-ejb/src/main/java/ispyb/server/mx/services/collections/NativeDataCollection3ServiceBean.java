/*******************************************************************************
 * This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ISPyB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
 ******************************************************************************************************************************/

package ispyb.server.mx.services.collections;

import ispyb.server.biosaxs.services.sql.SQLQueryKeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

/** Formatting columns 
   INPUT: SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS   WHERE table_name = 'BLSession';
   SCRIPT : sed -i '/^$/d' test.txt > text.txt && sed 's/|//g' test.txt > tmp.txt && sed 's/ //g' tmp.txt && awk '{ print "\"ScreeningStrategy."  $1" as ScreeningStrategy_"$1",\\r\\n \" +"}' tmp.txt > result.txt && cat result.txt
 * 
 * @author ademaria
 *
 */

@Stateless
public class NativeDataCollection3ServiceBean implements NativeDataCollection3Service, NativeDataCollection3ServiceLocal {
	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	/** Query for DataCollectionGroup by session **/
	// select * from DataCollectionGroup DcGroup
	// LEFT JOIN DataCollection Dc on Dc.dataCollectionGroupId =
	// DcGroup.dataCollectionGroupId
	// LEFT JOIN Screening Sc on Sc.dataCollectionId = Dc.dataCollectionGroupId
	// LEFT JOIN ScreeningOutput So on So.screeningId = Sc.screeningId
	// LEFT JOIN ScreeningStrategy Sct on Sct.screeningOutputId =
	// So.screeningOutputId
	// LEFT JOIN ScreeningStrategyWedge Sctw on Sctw.screeningStrategyId =
	// Sct.screeningStrategyId
	// LEFT JOIN ScreeningStrategySubWedge SctwSub on
	// SctwSub.screeningStrategyWedgeId = Sctw.screeningStrategyWedgeId
	// LEFT JOIN BLSample bl on bl.blSampleId = DcGroup.blSampleId
	// LEFT JOIN Crystal cr on cr.crystalId = bl.crystalId
	// LEFT JOIN Protein pr on pr.proteinId = cr.proteinId

	private static String getByQUERY = "select "
			+ "(select count(*) from Image where Image.dataCollectionId = DataCollection.dataCollectionId) as nbStoredImages, "
			+ "(select min(Image.imageId) from Image where Image.dataCollectionId = DataCollection.dataCollectionId) as firstImageId,"
			+ "(select max(Image.imageId) from Image where Image.dataCollectionId = DataCollection.dataCollectionId) as lastImageId,"
			+ NativeDataCollection3ServiceBean.getDataCollectionGroupColumns()
			+ ","  
			+ NativeDataCollection3ServiceBean.getScreeningColumns()
			+ ","  
			+ NativeDataCollection3ServiceBean.getScreeningOutputColumns()
			+ "," 
			+ NativeDataCollection3ServiceBean.getScreeningStrategyColumns()
			+ "," 
			+ NativeDataCollection3ServiceBean.getBLSampleColumns()
			+ "," 
			+ NativeDataCollection3ServiceBean.getCrystalColumns()
			+ "," 
			+ NativeDataCollection3ServiceBean.getSessionColumns()
			+ "," 
			+ NativeDataCollection3ServiceBean.getProteinColumns()
			+ "," 
			+ NativeDataCollection3ServiceBean.getDataCollectionColumns()
			+ "," 
			+ NativeDataCollection3ServiceBean.getDetectorColumns()
			+ " from DataCollectionGroup  "
			+ " LEFT JOIN DataCollection on DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId"
			+ " LEFT JOIN Detector on DataCollection.detectorId = Detector.detectorId"
			+ " LEFT JOIN Screening on Screening.dataCollectionId = DataCollection.dataCollectionGroupId"
			+ " LEFT JOIN ScreeningOutput on ScreeningOutput.screeningId = Screening.screeningId"
			+ " LEFT JOIN ScreeningStrategy  on ScreeningStrategy.screeningOutputId = ScreeningOutput.screeningOutputId"
			+ " LEFT JOIN BLSession  on BLSession.sessionId = DataCollectionGroup.sessionId"
			+ " LEFT JOIN BLSample on BLSample.blSampleId = DataCollectionGroup.blSampleId"
			+ " LEFT JOIN Crystal  on Crystal.crystalId = BLSample.crystalId" 
			+ " LEFT JOIN Protein  on Protein.proteinId = Crystal.proteinId";
	
	private static String getBySessionQUERY = getByQUERY
			+ " where DataCollectionGroup.sessionId = :sessionId";
	
	private static String getByDataCollectionId = getByQUERY
			+ " where DataCollection.dataCollectionId = :dataCollectionId";
	

	@Override
	public List<Map<String, Object>> getDataCollectionBySessionId(int sessionId) {
		String mySQLQuery = getBySessionQUERY;
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("sessionId", sessionId);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> aliasToValueMapList = query.list();
		return aliasToValueMapList;
	}

	private static String getSessionColumns() {
		return 	"BLSession.sessionId as BLSession_sessionId,\r\n " +
				"BLSession.expSessionPk as BLSession_expSessionPk,\r\n " +
				"BLSession.beamLineSetupId as BLSession_beamLineSetupId,\r\n " +
				"BLSession.proposalId as BLSession_proposalId,\r\n " +
				"BLSession.projectCode as BLSession_projectCode,\r\n " +
				"BLSession.startDate as BLSession_startDate,\r\n " +
				"BLSession.endDate as BLSession_endDate,\r\n " +
				"BLSession.beamLineName as BLSession_beamLineName,\r\n " +
				"BLSession.scheduled as BLSession_scheduled,\r\n " +
				"BLSession.nbShifts as BLSession_nbShifts,\r\n " +
				"BLSession.comments as BLSession_comments,\r\n " +
				"BLSession.beamLineOperator as BLSession_beamLineOperator,\r\n " +
				"BLSession.visit_number as BLSession_visit_number,\r\n " +
				"BLSession.bltimeStamp as BLSession_bltimeStamp,\r\n " +
				"BLSession.usedFlag as BLSession_usedFlag,\r\n " +
				"BLSession.sessionTitle as BLSession_sessionTitle,\r\n " +
				"BLSession.structureDeterminations as BLSession_structureDeterminations,\r\n " +
				"BLSession.dewarTransport as BLSession_dewarTransport,\r\n " +
				"BLSession.databackupFrance as BLSession_databackupFrance,\r\n " +
				"BLSession.databackupEurope as BLSession_databackupEurope,\r\n " +
				"BLSession.operatorSiteNumber as BLSession_operatorSiteNumber,\r\n " +
				"BLSession.lastUpdate as BLSession_lastUpdate,\r\n " +
				"BLSession.protectedData as BLSession_protectedData";


	}
	
	private static String getCrystalColumns() {
		return 	"Crystal.crystalId as Crystal_crystalId,\r\n " +
				"Crystal.diffractionPlanId as Crystal_diffractionPlanId,\r\n " +
				"Crystal.proteinId as Crystal_proteinId,\r\n " +
				"Crystal.crystalUUID as Crystal_crystalUUID,\r\n " +
				"Crystal.name as Crystal_name,\r\n " +
				"Crystal.spaceGroup as Crystal_spaceGroup,\r\n " +
				"Crystal.morphology as Crystal_morphology,\r\n " +
				"Crystal.color as Crystal_color,\r\n " +
				"Crystal.size_X as Crystal_size_X,\r\n " +
				"Crystal.size_Y as Crystal_size_Y,\r\n " +
				"Crystal.size_Z as Crystal_size_Z,\r\n " +
				"Crystal.cell_a as Crystal_cell_a,\r\n " +
				"Crystal.cell_b as Crystal_cell_b,\r\n " +
				"Crystal.cell_c as Crystal_cell_c,\r\n " +
				"Crystal.cell_alpha as Crystal_cell_alpha,\r\n " +
				"Crystal.cell_beta as Crystal_cell_beta,\r\n " +
				"Crystal.cell_gamma as Crystal_cell_gamma,\r\n " +
				"Crystal.comments as Crystal_comments,\r\n " +
				"Crystal.pdbFileName as Crystal_pdbFileName,\r\n " +
				"Crystal.pdbFilePath as Crystal_pdbFilePath,\r\n " +
				"Crystal.recordTimeStamp as Crystal_recordTimeStamp";

	}
	

	private static String getProteinColumns() {
		return 	"Protein.proteinId as Protein_proteinId,\r\n " +
				"Protein.proposalId as Protein_proposalId,\r\n " +
				"Protein.name as Protein_name,\r\n " +
				"Protein.acronym as Protein_acronym,\r\n " +
				"Protein.molecularMass as Protein_molecularMass,\r\n " +
				"Protein.proteinType as Protein_proteinType,\r\n " +
				"Protein.sequence as Protein_sequence,\r\n " +
				"Protein.personId as Protein_personId,\r\n " +
				"Protein.bltimeStamp as Protein_bltimeStamp,\r\n " +
				"Protein.isCreatedBySampleSheet as Protein_isCreatedBySampleSheet";

	}
	
	
	private static String getBLSampleColumns() {
		return 	"BLSample.blSampleId as BLSample_blSampleId,\r\n " +
				"BLSample.diffractionPlanId as BLSample_diffractionPlanId,\r\n " +
				"BLSample.crystalId as BLSample_crystalId,\r\n " +
				"BLSample.containerId as BLSample_containerId,\r\n " +
				"BLSample.name as BLSample_name,\r\n " +
				"BLSample.code as BLSample_code,\r\n " +
				"BLSample.location as BLSample_location,\r\n " +
				"BLSample.holderLength as BLSample_holderLength,\r\n " +
				"BLSample.loopLength as BLSample_loopLength,\r\n " +
				"BLSample.loopType as BLSample_loopType,\r\n " +
				"BLSample.wireWidth as BLSample_wireWidth,\r\n " +
				"BLSample.comments as BLSample_comments,\r\n " +
				"BLSample.completionStage as BLSample_completionStage,\r\n " +
				"BLSample.structureStage as BLSample_structureStage,\r\n " +
				"BLSample.publicationStage as BLSample_publicationStage,\r\n " +
				"BLSample.publicationComments as BLSample_publicationComments,\r\n " +
				"BLSample.blSampleStatus as BLSample_blSampleStatus,\r\n " +
				"BLSample.isInSampleChanger as BLSample_isInSampleChanger,\r\n " +
				"BLSample.lastKnownCenteringPosition as BLSample_lastKnownCenteringPosition,\r\n " +
				"BLSample.recordTimeStamp as BLSample_recordTimeStamp,\r\n " +
				"BLSample.SMILES as BLSample_SMILES";
	}
	
	private static String getScreeningStrategyColumns() {
		return 		"ScreeningStrategy.screeningStrategyId as ScreeningStrategy_screeningStrategyId,\r\n " +
					"ScreeningStrategy.screeningOutputId as ScreeningStrategy_screeningOutputId,\r\n " +
					"ScreeningStrategy.phiStart as ScreeningStrategy_phiStart,\r\n " +
					"ScreeningStrategy.phiEnd as ScreeningStrategy_phiEnd,\r\n " +
					"ScreeningStrategy.rotation as ScreeningStrategy_rotation,\r\n " +
					"ScreeningStrategy.exposureTime as ScreeningStrategy_exposureTime,\r\n " +
					"ScreeningStrategy.resolution as ScreeningStrategy_resolution,\r\n " +
					"ScreeningStrategy.completeness as ScreeningStrategy_completeness,\r\n " +
					"ScreeningStrategy.multiplicity as ScreeningStrategy_multiplicity,\r\n " +
					"ScreeningStrategy.anomalous as ScreeningStrategy_anomalous,\r\n " +
					"ScreeningStrategy.program as ScreeningStrategy_program,\r\n " +
					"ScreeningStrategy.rankingResolution as ScreeningStrategy_rankingResolution,\r\n " +
					"ScreeningStrategy.transmission as ScreeningStrategy_transmission";


	}
	
	private static String getScreeningColumns() {
		return 		"Screening.screeningId as Screening_screeningId,\r\n " +
					"Screening.diffractionPlanId as Screening_diffractionPlanId,\r\n " +
					"Screening.dataCollectionId as Screening_dataCollectionId,\r\n " +
					"Screening.bltimeStamp as Screening_bltimeStamp,\r\n " +
					"Screening.programVersion as Screening_programVersion,\r\n " +
					"Screening.comments as Screening_comments,\r\n " +
					"Screening.shortComments as Screening_shortComments,\r\n " +
					"Screening.xmlSampleInformation as Screening_xmlSampleInformation";

	}
	
	private static String getScreeningOutputColumns() {
		return 		"ScreeningOutput.screeningOutputId as ScreeningOutput_screeningOutputId,\r\n " +
					"ScreeningOutput.screeningId as ScreeningOutput_screeningId,\r\n " +
					"ScreeningOutput.statusDescription as ScreeningOutput_statusDescription,\r\n " +
					"ScreeningOutput.rejectedReflections as ScreeningOutput_rejectedReflections,\r\n " +
					"ScreeningOutput.resolutionObtained as ScreeningOutput_resolutionObtained,\r\n " +
					"ScreeningOutput.spotDeviationR as ScreeningOutput_spotDeviationR,\r\n " +
					"ScreeningOutput.spotDeviationTheta as ScreeningOutput_spotDeviationTheta,\r\n " +
					"ScreeningOutput.beamShiftX as ScreeningOutput_beamShiftX,\r\n " +
					"ScreeningOutput.beamShiftY as ScreeningOutput_beamShiftY,\r\n " +
					"ScreeningOutput.numSpotsFound as ScreeningOutput_numSpotsFound,\r\n " +
					"ScreeningOutput.numSpotsUsed as ScreeningOutput_numSpotsUsed,\r\n " +
					"ScreeningOutput.numSpotsRejected as ScreeningOutput_numSpotsRejected,\r\n " +
					"ScreeningOutput.mosaicity as ScreeningOutput_mosaicity,\r\n " +
					"ScreeningOutput.iOverSigma as ScreeningOutput_iOverSigma,\r\n " +
					"ScreeningOutput.diffractionRings as ScreeningOutput_diffractionRings,\r\n " +
					"ScreeningOutput.strategySuccess as ScreeningOutput_strategySuccess,\r\n " +
					"ScreeningOutput.mosaicityEstimated as ScreeningOutput_mosaicityEstimated,\r\n " +
					"ScreeningOutput.rankingResolution as ScreeningOutput_rankingResolution,\r\n " +
					"ScreeningOutput.program as ScreeningOutput_program,\r\n " +
					"ScreeningOutput.doseTotal as ScreeningOutput_doseTotal,\r\n " +
					"ScreeningOutput.totalExposureTime as ScreeningOutput_totalExposureTime,\r\n " +
					"ScreeningOutput.totalRotationRange as ScreeningOutput_totalRotationRange,\r\n " +
					"ScreeningOutput.totalNumberOfImages as ScreeningOutput_totalNumberOfImages,\r\n " +
					"ScreeningOutput.rFriedel as ScreeningOutput_rFriedel,\r\n " +
					"ScreeningOutput.indexingSuccess as ScreeningOutput_indexingSuccess";

	}
	
	private static String getDataCollectionGroupColumns() {
		return 		"DataCollectionGroup.dataCollectionGroupId as DataCollectionGroup_dataCollectionGroupId,\r\n " +
					"DataCollectionGroup.blSampleId as DataCollectionGroup_blSampleId,\r\n " +
					"DataCollectionGroup.sessionId as DataCollectionGroup_sessionId,\r\n " +
					"DataCollectionGroup.workflowId as DataCollectionGroup_workflowId,\r\n " +
					"DataCollectionGroup.experimentType as DataCollectionGroup_experimentType,\r\n " +
					"DataCollectionGroup.startTime as DataCollectionGroup_startTime,\r\n " +
					"DataCollectionGroup.endTime as DataCollectionGroup_endTime,\r\n " +
					"DataCollectionGroup.crystalClass as DataCollectionGroup_crystalClass,\r\n " +
					"DataCollectionGroup.comments as DataCollectionGroup_comments,\r\n " +
					"DataCollectionGroup.detectorMode as DataCollectionGroup_detectorMode,\r\n " +
					"DataCollectionGroup.actualSampleBarcode as DataCollectionGroup_actualSampleBarcode,\r\n " +
					"DataCollectionGroup.actualSampleSlotInContainer as DataCollectionGroup_actualSampleSlotInContainer,\r\n " +
					"DataCollectionGroup.actualContainerBarcode as DataCollectionGroup_actualContainerBarcode,\r\n " +
					"DataCollectionGroup.actualContainerSlotInSC as DataCollectionGroup_actualContainerSlotInSC,\r\n " +
					"DataCollectionGroup.xtalSnapshotFullPath as DataCollectionGroup_xtalSnapshotFullPath";
	}

	private static String getDetectorColumns(){
		return  "Detector.detectorId as Detector_detectorId,\r\n " +
				"Detector.detectorType as Detector_detectorType,\r\n " +
				"Detector.detectorManufacturer as Detector_detectorManufacturer,\r\n " +
				"Detector.detectorModel as Detector_detectorModel,\r\n " +
				"Detector.detectorPixelSizeHorizontal as Detector_detectorPixelSizeHorizontal,\r\n " +
				"Detector.detectorPixelSizeVertical as Detector_detectorPixelSizeVertical,\r\n " +
				"Detector.detectorSerialNumber as Detector_detectorSerialNumber,\r\n " +
				"Detector.detectorDistanceMin as Detector_detectorDistanceMin,\r\n " +
				"Detector.detectorDistanceMax as Detector_detectorDistanceMax,\r\n " +
				"Detector.trustedPixelValueRangeLower as Detector_trustedPixelValueRangeLower,\r\n " +
				"Detector.trustedPixelValueRangeUpper as Detector_trustedPixelValueRangeUpper,\r\n " +
				"Detector.sensorThickness as Detector_sensorThickness,\r\n " +
				"Detector.overload as Detector_overload,\r\n " +
				"Detector.XGeoCorr as Detector_XGeoCorr,\r\n " +
				"Detector.YGeoCorr as Detector_YGeoCorr,\r\n " +
				"Detector.detectorMode as Detector_detectorMode";
		
		
	}
	private static String getDataCollectionColumns() {
		return  "DataCollection.dataCollectionId as DataCollection_dataCollectionId,\r\n " +
				"DataCollection.dataCollectionGroupId as DataCollection_dataCollectionGroupId,\r\n " +
				"DataCollection.strategySubWedgeOrigId as DataCollection_strategySubWedgeOrigId,\r\n " +
				"DataCollection.detectorId as DataCollection_detectorId,\r\n " +
				"DataCollection.blSubSampleId as DataCollection_blSubSampleId,\r\n " +
				"DataCollection.startPositionId as DataCollection_startPositionId,\r\n " +
				"DataCollection.endPositionId as DataCollection_endPositionId,\r\n " +
				"DataCollection.dataCollectionNumber as DataCollection_dataCollectionNumber,\r\n " +
				"DataCollection.startTime as DataCollection_startTime,\r\n " +
				"DataCollection.endTime as DataCollection_endTime,\r\n " +
				"DataCollection.runStatus as DataCollection_runStatus,\r\n " +
				"DataCollection.axisStart as DataCollection_axisStart,\r\n " +
				"DataCollection.axisEnd as DataCollection_axisEnd,\r\n " +
				"DataCollection.axisRange as DataCollection_axisRange,\r\n " +
				"DataCollection.overlap as DataCollection_overlap,\r\n " +
				"DataCollection.numberOfImages as DataCollection_numberOfImages,\r\n " +
				"DataCollection.startImageNumber as DataCollection_startImageNumber,\r\n " +
				"DataCollection.numberOfPasses as DataCollection_numberOfPasses,\r\n " +
				"DataCollection.exposureTime as DataCollection_exposureTime,\r\n " +
				"DataCollection.imageDirectory as DataCollection_imageDirectory,\r\n " +
				"DataCollection.imagePrefix as DataCollection_imagePrefix,\r\n " +
				"DataCollection.imageSuffix as DataCollection_imageSuffix,\r\n " +
				"DataCollection.fileTemplate as DataCollection_fileTemplate,\r\n " +
				"DataCollection.wavelength as DataCollection_wavelength,\r\n " +
				"DataCollection.resolution as DataCollection_resolution,\r\n " +
				"DataCollection.detectorDistance as DataCollection_detectorDistance,\r\n " +
				"DataCollection.xBeam as DataCollection_xBeam,\r\n " +
				"DataCollection.yBeam as DataCollection_yBeam,\r\n " +
				"DataCollection.comments as DataCollection_comments,\r\n " +
				"DataCollection.printableForReport as DataCollection_printableForReport,\r\n " +
				"DataCollection.slitGapVertical as DataCollection_slitGapVertical,\r\n " +
				"DataCollection.slitGapHorizontal as DataCollection_slitGapHorizontal,\r\n " +
				"DataCollection.transmission as DataCollection_transmission,\r\n " +
				"DataCollection.synchrotronMode as DataCollection_synchrotronMode,\r\n " +
				"DataCollection.xtalSnapshotFullPath1 as DataCollection_xtalSnapshotFullPath1,\r\n " +
				"DataCollection.xtalSnapshotFullPath2 as DataCollection_xtalSnapshotFullPath2,\r\n " +
				"DataCollection.xtalSnapshotFullPath3 as DataCollection_xtalSnapshotFullPath3,\r\n " +
				"DataCollection.xtalSnapshotFullPath4 as DataCollection_xtalSnapshotFullPath4,\r\n " +
				"DataCollection.rotationAxis as DataCollection_rotationAxis,\r\n " +
				"DataCollection.phiStart as DataCollection_phiStart,\r\n " +
				"DataCollection.kappaStart as DataCollection_kappaStart,\r\n " +
				"DataCollection.omegaStart as DataCollection_omegaStart,\r\n " +
				"DataCollection.resolutionAtCorner as DataCollection_resolutionAtCorner,\r\n " +
				"DataCollection.detector2Theta as DataCollection_detector2Theta,\r\n " +
				"DataCollection.undulatorGap1 as DataCollection_undulatorGap1,\r\n " +
				"DataCollection.undulatorGap2 as DataCollection_undulatorGap2,\r\n " +
				"DataCollection.undulatorGap3 as DataCollection_undulatorGap3,\r\n " +
				"DataCollection.beamSizeAtSampleX as DataCollection_beamSizeAtSampleX,\r\n " +
				"DataCollection.beamSizeAtSampleY as DataCollection_beamSizeAtSampleY,\r\n " +
				"DataCollection.centeringMethod as DataCollection_centeringMethod,\r\n " +
				"DataCollection.averageTemperature as DataCollection_averageTemperature,\r\n " +
				"DataCollection.actualCenteringPosition as DataCollection_actualCenteringPosition,\r\n " +
				"DataCollection.beamShape as DataCollection_beamShape,\r\n " +
				"DataCollection.flux as DataCollection_flux,\r\n " +
				"DataCollection.flux_end as DataCollection_flux_end,\r\n " +
				"DataCollection.totalAbsorbedDose as DataCollection_totalAbsorbedDose,\r\n " +
				"DataCollection.bestWilsonPlotPath as DataCollection_bestWilsonPlotPath";


	}

	@Override
	public List<Map<String, Object>> getDataCollectionById(int dataCollectionId) {
		String mySQLQuery = getByDataCollectionId;
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("dataCollectionId", dataCollectionId);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> aliasToValueMapList = query.list();
		return aliasToValueMapList;
	}

	@Override
	public List<Map<String, Object>> getDataCollectionById(List<Integer> ids) {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		for (Integer id : ids) {
			result.addAll(this.getDataCollectionById(id));
		}
		return result;
	}

}
