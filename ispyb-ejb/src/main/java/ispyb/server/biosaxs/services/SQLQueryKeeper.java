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

package ispyb.server.biosaxs.services;

public class SQLQueryKeeper {
	public static String getCrystalTable() {
		return  "Crystal.crystalId as Crystal_crystalId,\r\n" +  
				"Crystal.diffractionPlanId as Crystal_diffractionPlanId,\r\n" + 
				"Crystal.proteinId as Crystal_proteinId,\r\n" + 
				"Crystal.crystalUUID as Crystal_crystalUUID,\r\n" + 
				"Crystal.name as Crystal_name,\r\n" + 
				"Crystal.spaceGroup as Crystal_spaceGroup,\r\n" + 
				"Crystal.morphology as Crystal_morphology,\r\n" + 
				"Crystal.color as Crystal_color,\r\n" + 
				"Crystal.size_X as Crystal_size_X,\r\n" + 
				"Crystal.size_Y as Crystal_size_Y,\r\n" + 
				"Crystal.size_Z as Crystal_size_Z,\r\n" + 
				"Crystal.cell_a as Crystal_cell_a,\r\n" + 
				"Crystal.cell_b as Crystal_cell_b,\r\n" + 
				"Crystal.cell_c as Crystal_cell_c,\r\n" + 
				"Crystal.cell_alpha as Crystal_cell_alpha,\r\n" + 
				"Crystal.cell_beta as Crystal_cell_beta,\r\n" + 
				"Crystal.cell_gamma as Crystal_cell_gamma,\r\n" + 
				"Crystal.comments as Crystal_comments,\r\n" + 
				"Crystal.pdbFileName as Crystal_pdbFileName,\r\n" + 
				"Crystal.pdbFilePath as Crystal_pdbFilePath,\r\n" + 
				"Crystal.recordTimeStamp as Crystal_recordTimeStamp\r\n";
	}
	
	
	public static String getExperimentListByExperimentId(Integer proposalId,
			Integer experimentId) {
		StringBuilder sb = new StringBuilder(SQLQueryKeeper.getExperimentListByProposalId(proposalId));
		sb.append(" and e.experimentId = :experimentId ");
		return sb.toString();
	}
	

	
	public static String getProteinTable() {
		return  " Protein.proteinId as Protein_proteinId,\r\n" + 
				" Protein.proposalId as Protein_proposalId,\r\n" + 
				" Protein.name as Protein_name,\r\n" + 
				" Protein.acronym as Protein_acronym,\r\n" + 
				" Protein.molecularMass as Protein_molecularMass,\r\n" + 
				" Protein.proteinType as Protein_proteinType,\r\n" + 
				" Protein.sequence as Protein_sequence,\r\n" + 
				" Protein.personId as Protein_personId,\r\n" + 
				" Protein.bltimeStamp as Protein_bltimeStamp,\r\n" + 
				" Protein.isCreatedBySampleSheet as Protein_isCreatedBySampleSheet\r\n";
	}

//	public static String getImageTable() {
//		return  "Image.imageId as Image_imageId,\r\n" + 
//				"Image.dataCollectionId as Image_dataCollectionId,\r\n" + 
//				"Image.motorPositionId as Image_motorPositionId,\r\n" + 
//				"Image.imageNumber as Image_imageNumber,\r\n" + 
//				"Image.fileName as Image_fileName,\r\n" + 
//				"Image.fileLocation as Image_fileLocation,\r\n" + 
//				"Image.measuredIntensity as Image_measuredIntensity,\r\n" + 
//				"Image.jpegFileFullPath as Image_jpegFileFullPath,\r\n" + 
//				"Image.jpegThumbnailFileFullPath as Image_jpegThumbnailFileFullPath,\r\n" + 
//				"Image.temperature as Image_temperature,\r\n" + 
//				"Image.cumulativeIntensity as Image_cumulativeIntensity,\r\n" + 
//				"Image.synchrotronCurrent as Image_synchrotronCurrent,\r\n" + 
//				"Image.comments as Image_comments,\r\n" + 
//				"Image.machineMessage as Image_machineMessage,\r\n" + 
//				"Image.recordTimeStamp as Image_recordTimeStamp\r\n";
//	}

	public static String getBLSampleTable() {
		return  "BLSample.blSampleId as BLSample_blSampleId,\r\n" + 
				"BLSample.diffractionPlanId as BLSample_diffractionPlanId,\r\n" + 
				"BLSample.crystalId as BLSample_crystalId,\r\n" + 
				"BLSample.containerId as BLSample_containerId,\r\n" + 
				"BLSample.name as BLSample_name,\r\n" + 
				"BLSample.code as BLSample_code,\r\n" + 
				"BLSample.location as BLSample_location,\r\n" + 
				"BLSample.holderLength as BLSample_holderLength,\r\n" + 
				"BLSample.loopLength as BLSample_loopLength,\r\n" + 
				"BLSample.loopType as BLSample_loopType,\r\n" + 
				"BLSample.wireWidth as BLSample_wireWidth,\r\n" + 
				"BLSample.comments as BLSample_comments,\r\n" + 
				"BLSample.completionStage as BLSample_completionStage,\r\n" + 
				"BLSample.structureStage as BLSample_structureStage,\r\n" + 
				"BLSample.publicationStage as BLSample_publicationStage,\r\n" + 
				"BLSample.publicationComments as BLSample_publicationComments,\r\n" + 
				"BLSample.blSampleStatus as BLSample_blSampleStatus,\r\n" + 
				"BLSample.isInSampleChanger as BLSample_isInSampleChanger,\r\n" + 
				"BLSample.lastKnownCenteringPosition as BLSample_lastKnownCenteringPosition,\r\n" + 
				"BLSample.recordTimeStamp as BLSample_recordTimeStamp,\r\n" + 
				"BLSample.SMILES as BLSample_SMILES \r\n";
	}
	
	
	public static String getDataCollectionByProposal() {
		return "select "
				+ SQLQueryKeeper.getProposalTable() + ","  
				+ SQLQueryKeeper.getCrystalTable() + ","  
				+ SQLQueryKeeper.getProteinTable() + "," 
				+ SQLQueryKeeper.getImageTable() + "," 
				+ SQLQueryKeeper.getBLSampleTable() 
				+ " from Proposal "
				+ " LEFT JOIN Protein on Proposal.proposalId = Protein.proposalId\r\n "
				+ " LEFT JOIN Crystal on Protein.proteinId = Crystal.proteinId\r\n"
				+ " LEFT JOIN BLSample on Crystal.crystalId = BLSample.crystalId\r\n"
				+ " LEFT JOIN DataCollectionGroup on BLSample.blSampleId = DataCollectionGroup.blSampleId\r\n"
				+ " LEFT JOIN DataCollection on DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId\r\n"
				+ " LEFT JOIN Image on Image.dataCollectionId = DataCollection.dataCollectionGroupId\r\n"
				+ " where proposalNumber = :proposalNumber and proposalCode=:proposalCode";

	}

	public static String getExperimentDescription() {
		return "SELECT Buffer.acronym as buffer, Macromolecule.acronym as macromolecule, Specimen.*, Measurement.*, SamplePlatePosition.*, SamplePlate.*  FROM \r\n"
				+ "Experiment experiment\r\n"
				+ "LEFT JOIN Specimen on Specimen.experimentId = experiment.experimentId\r\n"
				+ "LEFT JOIN Macromolecule on Macromolecule.macromoleculeId = Specimen.macromoleculeId\r\n"
				+ "LEFT JOIN Buffer on Buffer.bufferId = Specimen.bufferId\r\n"
				+ "LEFT JOIN Measurement on Measurement.specimenId = Specimen.specimenId\r\n"
				+ "LEFT JOIN SamplePlatePosition on Specimen.samplePlatePositionId = SamplePlatePosition.samplePlatePositionId\r\n"
				+ "LEFT JOIN SamplePlate on SamplePlate.samplePlateId = SamplePlatePosition.samplePlateId ";
	}

	public static String getAnalysisByMacromoleculeId(int macromoleculeId,
			int proposalId) {
		StringBuilder sb = new StringBuilder(SQLQueryKeeper.getAnalysisQuery());
		sb.append(" and s.macromoleculeId = " + macromoleculeId);
		sb.append(" and p.proposalId = " + proposalId);
		return sb.toString();
	}

	public static String getAnalysisByMacromoleculeId(int macromoleculeId,
			int bufferId, int proposalId) {
		StringBuilder sb = new StringBuilder(
				SQLQueryKeeper.getAnalysisByMacromoleculeId(macromoleculeId,
						proposalId));
		sb.append(" and bu.bufferId = " + bufferId);
		return sb.toString();
	}

	public static String getAnalysisByProposalId(int proposalId) {
		StringBuilder sb = new StringBuilder(SQLQueryKeeper.getAnalysisQuery());
		sb.append(" and p.proposalId = " + proposalId);
		return sb.toString();
	}

	public static String getAnalysisByExperimentId(int experimentId) {
		StringBuilder sb = new StringBuilder(SQLQueryKeeper.getAnalysisQuery());
		sb.append(" and exp.experimentId = " + experimentId);
		return sb.toString();
	}

	public static String getAnalysisCalibrationByProposalId(int proposalId) {
		StringBuilder sb = new StringBuilder(SQLQueryKeeper.getAnalysisQuery());
		sb.append("  and exp.experimentType = 'CALIBRATION' and p.proposalId = "
				+ proposalId);
		return sb.toString();
	}

	
	public static String getSingleAbinitioModelQuery() {
		return "SELECT DISTINCT(abInitioModel3VO) FROM AbInitioModel3VO abInitioModel3VO, Subtraction3VO su, SaxsDataCollection3VO dc, SubtractiontoAbInitioModel3VO subs "
				+ " LEFT JOIN FETCH abInitioModel3VO.averagedModel averagedModel "
				+ " LEFT JOIN FETCH abInitioModel3VO.rapidShapeDeterminationModel rapidShapeDeterminationModel "
				+ " LEFT JOIN FETCH abInitioModel3VO.shapeDeterminationModel shapeDeterminationModel"
				+ " LEFT JOIN FETCH abInitioModel3VO.modelList3VO modelList "
				+ " LEFT JOIN FETCH modelList.modeltolist3VOs modelToList "
				+ " LEFT JOIN FETCH modelToList.model3VO "
				+ " WHERE su.dataCollectionId = dc.dataCollectionId and "
				+ " subs.subtraction3VO.subtractionId = su.subtractionId ";
	}
	
	/**
	 * Get the query for all the information about Abinitio modeling including
	 * modelList and all the models
	 */
	public static String getAbinitioModelQuery() {
		return "SELECT DISTINCT(subtraction) FROM Subtraction3VO subtraction "
				+ " LEFT JOIN FETCH subtraction.substractionToAbInitioModel3VOs subtractiontoAbInitioModel3VO"
				+ " LEFT JOIN FETCH subtractiontoAbInitioModel3VO.abinitiomodel3VO  abInitioModel3VO"
				+ " LEFT JOIN FETCH abInitioModel3VO.averagedModel averagedModel "
				+ " LEFT JOIN FETCH abInitioModel3VO.averagedModel averagedModel "
				+ " LEFT JOIN FETCH abInitioModel3VO.rapidShapeDeterminationModel rapidShapeDeterminationModel "
				+ " LEFT JOIN FETCH abInitioModel3VO.shapeDeterminationModel shapeDeterminationModel"
				+ " LEFT JOIN FETCH abInitioModel3VO.modelList3VO modelList "
				+ " LEFT JOIN FETCH modelList.modeltolist3VOs modelToList "
				+ " LEFT JOIN FETCH modelToList.model3VO ";
	}

	private static String getMeasurementToDataCollection() {
		return "SELECT DISTINCT(measurementToDataCollection) FROM MeasurementTodataCollection3VO measurementToDataCollection ";
	}

	public static String getMeasurementToDataCollectionByMeasurementId(
			int measurementId) {
		StringBuilder sb = new StringBuilder(
				SQLQueryKeeper.getMeasurementToDataCollection());
		sb.append(" WHERE measurementToDataCollection.measurementId = "
				+ measurementId);
		return sb.toString();
	}

	/**
	 * @param dataCollectionId
	 * @return
	 */
	public static String findMeasurementToDataCollectionByDataCollectionId(
			Integer dataCollectionId) {
		StringBuilder sb = new StringBuilder(
				SQLQueryKeeper.getMeasurementToDataCollection());
		sb.append(" WHERE measurementToDataCollection.dataCollectionId = "
				+ dataCollectionId);
		return sb.toString();
	}

	public static String getFrame3VOByFilePath(String filepath) {
		return "SELECT frame FROM Frame3VO frame where frame.filePath = '"
				+ filepath + "'";
	}

	public static String getMeasurementById(int measurementId) {
		return "SELECT measurement FROM Measurement3VO measurement where measurement.measurementId = "
				+ measurementId;
	}

	public static String getSpecimenById(int specimenId) {
		return "SELECT specimen FROM Specimen3VO specimen where specimen.specimenId = "
				+ specimenId;
	}

	public static String getExperimentListByProposalId(int proposalId,
			String experimentType) {
		StringBuilder sb = new StringBuilder(
				SQLQueryKeeper.getExperimentListByProposalId(proposalId));
		sb.append(" and e.experimentType = :experimentType ");
		return sb.toString();
	}

	public static String getExperimentListBySessionId(Integer proposalId,
			Integer sessionId) {
		StringBuilder sb = new StringBuilder(
				SQLQueryKeeper.getExperimentListByProposalId(proposalId));
		sb.append(" and e.sessionId = :sessionId ");
		return sb.toString();
	}

	public static String getAnalysis(int limit) {
		StringBuilder sb = new StringBuilder(SQLQueryKeeper.getAnalysisQuery());
		sb.append("  order by exp.experimentId DESC limit " + limit);
		return sb.toString();
	}

	public static String getModelQuery(int modelId) {
		return "SELECT model FROM Model3VO model where model.modelId = "
				+ modelId;
	}

	// public static String getSamplePlatesByBoxId(String dewarId) {
	// return "SELECT plate FROM Sampleplate3VO plate where plate.boxId = " +
	// dewarId ;
	// }

	// public static String getSamplePlatesByProposalId(int proposalId) {
	// return
	// "SELECT DISTINCT(plate) FROM Sampleplate3VO plate LEFT JOIN Experiment3VO exp where plate.experimentId = exp.experimentId and exp.proposalId ="
	// + proposalId;
	// }

	public static String getStockSolutionsByBoxId(String dewarId) {
		return "SELECT st FROM StockSolution3VO st where st.boxId = " + dewarId;
	}

	public static String getMergesByIdsList() {
		StringBuilder ejbQLQuery = new StringBuilder();
		ejbQLQuery.append("SELECT DISTINCT(Merge) FROM Merge3VO Merge ");
		ejbQLQuery.append("LEFT JOIN FETCH Merge.framelist3VO frameList ");
		ejbQLQuery
				.append("LEFT JOIN FETCH frameList.frametolist3VOs frametolist3VOs ");
		ejbQLQuery.append("LEFT JOIN FETCH frametolist3VOs.frame3VO ");
		ejbQLQuery.append("where Merge.mergeId IN :mergeIdList");
		return ejbQLQuery.toString();
	}

	private static String SELECT_EXPERIMENTS = ""
			+ "select *, "
			+ " ( "
			+ "  select count(*) "
			+ " from  Specimen s "
			+ " where s.experimentId = e.experimentId "
			+ ") as specimenCount, "
			+ "( "
			+ "  select count(*) "
			+ "  from  Measurement m, Specimen s "
			+ "  where s.experimentId = e.experimentId and m.specimenId = s.specimenId "
			+ ") as measurementCount, "
			+ "( "
			+ "  select count(*) "
			+ "  from  Measurement m, Specimen s "
			+ "  where s.experimentId = e.experimentId and m.specimenId = s.specimenId and m.runId is not null "
			+ ") as measurementDoneCount, "
			+ "( "
			+ "  select count(*) "
			+ "  from  SaxsDataCollection sdc "
			+ "  where sdc.experimentId = e.experimentId "
			+ ") as dataCollectionCount, "
			+ "( "
			+ "  select count(*) "
			+ "  from  SaxsDataCollection sdc, Subtraction sub "
			+ "  where sdc.experimentId = e.experimentId and sub.dataCollectionId = sdc.dataCollectionId "
			+ ") as dataCollectionDoneCount, "
			+ "( "
			+ "  select count(*) "
			+ "  from  Measurement m, Specimen s, Merge me "
			+ "  where s.experimentId = e.experimentId and m.specimenId = s.specimenId and me.measurementId = m.measurementId "
			+ ") as measurementAveragedCount, "
			+ "(  "
			+ "	    select group_concat(distinct(acronym) separator ', ')  "
			+ "	    from Macromolecule ma, Specimen sp, Experiment exp "
			+ "	    where ma.macromoleculeId = sp.macromoleculeId and sp.experimentId = e.experimentId "
			+ "	) as macromolecules ";

	public static String getExperimentListByProposalId(int proposalId) {
		return SELECT_EXPERIMENTS
				+ " from Experiment e where e.proposalId = :proposalId ";
	}

	private static String getSelectClause() {
		return " select   Subtraction.volume as volumePorod, "
				+ "Run.creationDate as runCreationDate, "
				+ "Measurement.code as measurementCode, "
				+ "Macromolecule.acronym as macromoleculeAcronym, "
				+ "Buffer.acronym as bufferAcronym, "
				+ "exp.*, "
				+ "Specimen.specimenId , "
				// + "Specimen.experimentId as specimen_experimentId, "
				+ "Specimen.safetyLevelId, "
				// + "Specimen.stockSolutionId as specimen_stockSolutionId, "
				// + "Specimen.code as code, "
				+ "Specimen.concentration as concentration, "
				+ "Specimen.volume as volume, "
				+ "Specimen.comments as specimen_comments, "
				//
				+ "Buffer.bufferId as buffer_bufferId, "
				//
				+ "Macromolecule.macromoleculeId, "

				+ "Measurement.measurementId, "
				+ "Measurement.code, "
				+ "Measurement.priorityLevelId, "
				+ "Measurement.exposureTemperature, "
				+ "Measurement.viscosity, "
				+ "Measurement.flow, "
				+ "Measurement.extraFlowTime, "
				+ "Measurement.volumeToLoad, "
				+ "Measurement.waitTime, "
				+ "Measurement.transmission, "
				+ "Measurement.comments as measurement_comments, "

				+ "MeasurementToDataCollection.measurementToDataCollectionId, "
				+ "MeasurementToDataCollection.dataCollectionOrder, "

				+ "SaxsDataCollection.dataCollectionId, "

				+ "Subtraction.subtractionId, "
				+ "Subtraction.rg, "
				+ "Subtraction.rgStdev, "
				+ "Subtraction.I0, "
				+ "Subtraction.I0Stdev, "
				+ "Subtraction.firstPointUsed, "
				+ "Subtraction.lastPointUsed, "
				+ "Subtraction.quality, "
				+ "Subtraction.isagregated, "

				+ "Subtraction.gnomFilePath, "
				+ "Subtraction.rgGuinier, "
				+ "Subtraction.rgGnom, "
				+ "Subtraction.dmax, "
				+ "Subtraction.total, "
				+ "Subtraction.volume as subtraction_volume, "
				+ "Subtraction.creationTime as subtraction_creationTime, "
				+ "Subtraction.kratkyFilePath, "
				+ "Subtraction.scatteringFilePath, "
				+ "Subtraction.guinierFilePath, "
				+ "Subtraction.substractedFilePath, "
				+ "Subtraction.gnomFilePathOutput, "
				+ "Subtraction.sampleOneDimensionalFiles, "
				+ "Subtraction.bufferOnedimensionalFiles, "
				+ "Subtraction.sampleAverageFilePath, "
				+ "Subtraction.bufferAverageFilePath, "

				+ "Merge.mergeId, "
				+ "Merge.discardedFrameNameList, "
				+ "Merge.averageFilePath, "
				+ "Merge.framesCount, "
				+ "Merge.framesMerge, "

				+ " (select count(*) from FitStructureToExperimentalData as f where f.subtractionId = Subtraction.subtractionId) as fitCount,\r\n"
				+ "(select count(*) from Superposition as f where f.subtractionId = Subtraction.subtractionId) as superposisitionCount,\r\n"
				+ "(select count(*) from RigidBodyModeling as f where f.subtractionId = Subtraction.subtractionId) as rigidbodyCount,\r\n"
				+ "(select count(*) from SubtractionToAbInitioModel as f where f.subtractionId = Subtraction.subtractionId) as abinitioCount ";
	}

	private static String getFromClause() {
		return "  from Experiment exp\r\n"
				+ "  LEFT JOIN Specimen on Specimen.experimentId = exp.experimentId\r\n"
				+ "  LEFT JOIN Buffer on Buffer.bufferId = Specimen.bufferId\r\n"
				+ "  LEFT JOIN Macromolecule on Macromolecule.macromoleculeId = Specimen.macromoleculeId\r\n"
				+ "  LEFT JOIN Measurement on Measurement.specimenId = Specimen.specimenId\r\n"
				+ "  LEFT JOIN Run on Measurement.runId = Run.runId\r\n"
				+ "  LEFT JOIN Merge on Merge.measurementId = Measurement.measurementId\r\n"
				+ "  LEFT JOIN SaxsDataCollection on SaxsDataCollection.experimentId = exp.experimentId\r\n"
				+ "  LEFT JOIN MeasurementToDataCollection on MeasurementToDataCollection.dataCollectionId = SaxsDataCollection.dataCollectionId and MeasurementToDataCollection.measurementId = Measurement.measurementId  \r\n"
				+ "  LEFT JOIN Subtraction on Subtraction.dataCollectionId = SaxsDataCollection.dataCollectionId\r\n";
		// "				 LEFT JOIN SubtractionToAbInitioModel on SubtractionToAbInitioModel.subtractionId = Subtraction.subtractionId\r\n"
		// +
		// "                LEFT JOIN AbInitioModel on AbInitioModel.abInitioModelId = SubtractionToAbInitioModel.abInitioId\r\n"
		// +
		// "                LEFT JOIN Model reference on reference.modelId = AbInitioModel.averagedModelId\r\n"
		// +
		// "                LEFT JOIN Model refined on refined.modelId = AbInitioModel.shapeDeterminationModelId\r\n"
		// +
		// "				 LEFT JOIN FitStructureToExperimentalData on FitStructureToExperimentalData.subtractionId = Subtraction.subtractionId and FitStructureToExperimentalData.fitStructureToExperimentalDataId in (select max(f2.fitStructureToExperimentalDataId) from FitStructureToExperimentalData f2  where f2.subtractionId = Subtraction.subtractionId)  ";
	}

	private static String getAnalysisCompactQuery() {
		return SQLQueryKeeper.getSelectClause()
				+ SQLQueryKeeper.getFromClause();

	}

	public static String getAnalysisCompactQueryByMacromoleculeId() {
		StringBuilder sb = new StringBuilder(SQLQueryKeeper.getSelectClause()
				+ SQLQueryKeeper.getFromClause());
		sb.append(" where SaxsDataCollection.dataCollectionId in (select dc.dataCollectionId from SaxsDataCollection dc, MeasurementToDataCollection mtd, Measurement m, Specimen s where m.specimenId = s.specimenId and mtd.measurementId = m.measurementId and dc.dataCollectionId = mtd.dataCollectionId and s.macromoleculeId = :macromoleculeId ) and exp.proposalId = :proposalId and  SaxsDataCollection.dataCollectionId = MeasurementToDataCollection.dataCollectionId and exp.experimentType != \"TEMPLATE\" \r\n");
		sb.append(" order by exp.experimentId DESC, Measurement.priorityLevelId DESC, Merge.mergeId DESC\r\n");
		return sb.toString();
	}

	public static String getCountAnalysisCompactQueryByProposalId(
			Integer proposalId) {
		StringBuilder sb = new StringBuilder("Select Count(*) "
				+ SQLQueryKeeper.getFromClause());
		sb.append(" where exp.proposalId = :proposalId and  SaxsDataCollection.dataCollectionId = MeasurementToDataCollection.dataCollectionId and exp.experimentType != \"TEMPLATE\" \r\n");
		sb.append(" order by exp.experimentId DESC, Measurement.priorityLevelId DESC, Merge.mergeId DESC\r\n");
		return sb.toString();
	}

	public static String getAnalysisCompactQueryByProposalId(Integer limit) {
		StringBuilder sb = new StringBuilder(getAnalysisCompactQuery());
		sb.append(" where exp.proposalId = :proposalId and  SaxsDataCollection.dataCollectionId = MeasurementToDataCollection.dataCollectionId and exp.experimentType != \"TEMPLATE\" \r\n");
		sb.append(" order by exp.experimentId DESC, Measurement.priorityLevelId DESC, Merge.mergeId DESC\r\n");
		sb.append(" limit " + limit + "\r\n");
		return sb.toString();
	}

	public static String getAnalysisCompactQueryByProposalId(Integer start,
			Integer limit) {
		StringBuilder sb = new StringBuilder(getAnalysisCompactQuery());
		sb.append(" where exp.proposalId = :proposalId and  SaxsDataCollection.dataCollectionId = MeasurementToDataCollection.dataCollectionId and exp.experimentType != \"TEMPLATE\" \r\n");
		sb.append(" order by exp.experimentId DESC, Measurement.priorityLevelId DESC, Merge.mergeId DESC\r\n");
		sb.append(" limit " + start + "," + limit + "\r\n");
		return sb.toString();
	}

	public static String getAnalysisCompactQueryBySubtractionId() {
		StringBuilder sb = new StringBuilder(getAnalysisCompactQuery());
		sb.append(" where Subtraction.subtractionId = :subtractionId and  SaxsDataCollection.dataCollectionId = MeasurementToDataCollection.dataCollectionId \r\n");
		sb.append(" order by exp.experimentId DESC, Measurement.priorityLevelId DESC, Merge.mergeId DESC\r\n");
		return sb.toString();

	}

	public static String getAnalysisCompactQueryByExperimentId() {
		StringBuilder sb = new StringBuilder(getAnalysisCompactQuery());
		sb.append(" where exp.experimentId = :experimentId and  SaxsDataCollection.dataCollectionId = MeasurementToDataCollection.dataCollectionId \r\n");
		sb.append(" order by exp.experimentId ASC, Measurement.priorityLevelId ASC\r\n");
		return sb.toString();

	}

	@Deprecated
	public static String getAnalysisQuery() {
		return "select \r\n"
				+ "    exp.experimentType as experimentType,\r\n"
				+ "    exp.sessionId as sessionId,\r\n"
				+ "    exp.creationDate as experimentCreationDate,\r\n"
				+ "    (select \r\n"
				+ "        timeStart\r\n"
				+ "    from\r\n"
				+ "        Run r\r\n"
				+ "    where\r\n"
				+ "        r.runId = m.runId) as timeStart,\r\n"
				+ "    p.proposalCode,\r\n"
				+ "    p.proposalNumber,\r\n"
				+ "    m.priorityLevelId,\r\n"
				+ "    m.code,\r\n"
				+ "    m.exposureTemperature,\r\n"
				+ "    m.transmission,\r\n"
				+ "    m.comments as measurementComments,\r\n"
				+ "    exp.comments as expermientComments,\r\n"
				+ "    exp.experimentId,\r\n"
				+ "    s.concentration as conc,\r\n"
				+ "    bu.acronym as bufferAcronym,\r\n"
				+ "    ma.acronym as macromoleculeAcronym,\r\n"
				+ "    bu.bufferId as bufferId,\r\n"
				+ "    ma.macromoleculeId as macromoleculeId,\r\n"
				+ "    (select \r\n"
				+ "        su.substractedFilePath\r\n"
				+ "    from\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.subtractionId = (select \r\n"
				+ "            max(su2.subtractionId)\r\n"
				+ "        from\r\n"
				+ "            Subtraction su2\r\n"
				+ "        where\r\n"
				+ "            su2.dataCollectionId = dc.dataCollectionId)) as subtractedFilePath,\r\n"
				+ "    (select \r\n"
				+ "        su.rgGuinier\r\n"
				+ "    from\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.subtractionId = (select \r\n"
				+ "            max(su2.subtractionId)\r\n"
				+ "        from\r\n"
				+ "            Subtraction su2\r\n"
				+ "        where\r\n"
				+ "            su2.dataCollectionId = dc.dataCollectionId)) as rgGuinier,\r\n"
				+ "    (select \r\n"
				+ "        su.firstPointUsed\r\n"
				+ "    from\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.subtractionId = (select \r\n"
				+ "            max(su2.subtractionId)\r\n"
				+ "        from\r\n"
				+ "            Subtraction su2\r\n"
				+ "        where\r\n"
				+ "            su2.dataCollectionId = dc.dataCollectionId)) as firstPointUsed,\r\n"
				+ "    (select \r\n"
				+ "        su.lastPointUsed\r\n"
				+ "    from\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.subtractionId = (select \r\n"
				+ "            max(su2.subtractionId)\r\n"
				+ "        from\r\n"
				+ "            Subtraction su2\r\n"
				+ "        where\r\n"
				+ "            su2.dataCollectionId = dc.dataCollectionId)) as lastPointUsed,\r\n"
				+ "    (select \r\n"
				+ "        su.I0\r\n"
				+ "    from\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.subtractionId = (select \r\n"
				+ "            max(su2.subtractionId)\r\n"
				+ "        from\r\n"
				+ "            Subtraction su2\r\n"
				+ "        where\r\n"
				+ "            su2.dataCollectionId = dc.dataCollectionId)) as I0,\r\n"
				+ "    (select \r\n"
				+ "        su.isagregated\r\n"
				+ "    from\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.subtractionId = (select \r\n"
				+ "            max(su2.subtractionId)\r\n"
				+ "        from\r\n"
				+ "            Subtraction su2\r\n"
				+ "        where\r\n"
				+ "            su2.dataCollectionId = dc.dataCollectionId)) as isagregated,\r\n"
				+ "    (select \r\n"
				+ "        su.subtractionId\r\n"
				+ "    from\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.subtractionId = (select \r\n"
				+ "            max(su2.subtractionId)\r\n"
				+ "        from\r\n"
				+ "            Subtraction su2\r\n"
				+ "        where\r\n"
				+ "            su2.dataCollectionId = dc.dataCollectionId)) as subtractionId,\r\n"
				+ "    (select \r\n"
				+ "        su.rgGnom\r\n"
				+ "    from\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.subtractionId = (select \r\n"
				+ "            max(su2.subtractionId)\r\n"
				+ "        from\r\n"
				+ "            Subtraction su2\r\n"
				+ "        where\r\n"
				+ "            su2.dataCollectionId = dc.dataCollectionId)) as rgGnom,\r\n"
				+ "    (select \r\n"
				+ "        su.total\r\n"
				+ "    from\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.subtractionId = (select \r\n"
				+ "            max(su2.subtractionId)\r\n"
				+ "        from\r\n"
				+ "            Subtraction su2\r\n"
				+ "        where\r\n"
				+ "            su2.dataCollectionId = dc.dataCollectionId)) as total,\r\n"
				+ "    (select \r\n"
				+ "        su.dmax\r\n"
				+ "    from\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.subtractionId = (select \r\n"
				+ "            max(su2.subtractionId)\r\n"
				+ "        from\r\n"
				+ "            Subtraction su2\r\n"
				+ "        where\r\n"
				+ "            su2.dataCollectionId = dc.dataCollectionId)) as dmax,\r\n"
				+ "    (select \r\n"
				+ "        su.volume\r\n"
				+ "    from\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.subtractionId = (select \r\n"
				+ "            max(su2.subtractionId)\r\n"
				+ "        from\r\n"
				+ "            Subtraction su2\r\n"
				+ "        where\r\n"
				+ "            su2.dataCollectionId = dc.dataCollectionId)) as volume,\r\n"
				+ "    (select \r\n"
				+ "        su.i0stdev\r\n"
				+ "    from\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.subtractionId = (select \r\n"
				+ "            max(su2.subtractionId)\r\n"
				+ "        from\r\n"
				+ "            Subtraction su2\r\n"
				+ "        where\r\n"
				+ "            su2.dataCollectionId = dc.dataCollectionId)) as i0stdev,\r\n"
				+ "    (select \r\n"
				+ "        su.quality\r\n"
				+ "    from\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.subtractionId = (select \r\n"
				+ "            max(su2.subtractionId)\r\n"
				+ "        from\r\n"
				+ "            Subtraction su2\r\n"
				+ "        where\r\n"
				+ "            su2.dataCollectionId = dc.dataCollectionId)) as quality,\r\n"
				+ "    (select \r\n"
				+ "        su.creationTime\r\n"
				+ "    from\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.subtractionId = (select \r\n"
				+ "            max(su2.subtractionId)\r\n"
				+ "        from\r\n"
				+ "            Subtraction su2\r\n"
				+ "        where\r\n"
				+ "            su2.dataCollectionId = dc.dataCollectionId)) as substractionCreationTime,\r\n"
				+ "    (select \r\n"
				+ "        mtd2.measurementId\r\n"
				+ "    from\r\n"
				+ "        MeasurementToDataCollection mtd2\r\n"
				+ "    where\r\n"
				+ "        mtd.dataCollectionId = mtd2.dataCollectionId and mtd2.dataCollectionOrder = 1) as bufferBeforeMeasurementId,\r\n"
				+ "    (select \r\n"
				+ "        mtd2.measurementId\r\n"
				+ "    from\r\n"
				+ "        MeasurementToDataCollection mtd2\r\n"
				+ "    where\r\n"
				+ "        mtd.dataCollectionId = mtd2.dataCollectionId and mtd2.dataCollectionOrder = 3) as bufferAfterMeasurementId,\r\n"
				+ "    (select \r\n"
				+ "        framesMerge\r\n"
				+ "    from\r\n"
				+ "        Merge m2\r\n"
				+ "    where\r\n"
				+ "        m2.measurementId = bufferBeforeMeasurementId and m2.mergeId in (select \r\n"
				+ "            max(m3.mergeId)\r\n"
				+ "        from\r\n"
				+ "            Merge m3\r\n"
				+ "        where\r\n"
				+ "            m3.measurementId = bufferBeforeMeasurementId)) as bufferBeforeFramesMerged,\r\n"
				+ "    (select \r\n"
				+ "        framesCount\r\n"
				+ "    from\r\n"
				+ "        Merge m2\r\n"
				+ "    where\r\n"
				+ "        m2.measurementId = bufferBeforeMeasurementId and m2.mergeId in (select \r\n"
				+ "            max(m3.mergeId)\r\n"
				+ "        from\r\n"
				+ "            Merge m3\r\n"
				+ "        where\r\n"
				+ "            m3.measurementId = bufferBeforeMeasurementId)) as bufferBeforeFramesCount,\r\n"
				+ "    (select \r\n"
				+ "        mergeId\r\n"
				+ "    from\r\n"
				+ "        Merge m2\r\n"
				+ "    where\r\n"
				+ "        m2.measurementId = bufferBeforeMeasurementId and m2.mergeId in (select \r\n"
				+ "            max(m3.mergeId)\r\n"
				+ "        from\r\n"
				+ "            Merge m3\r\n"
				+ "        where\r\n"
				+ "            m3.measurementId = bufferBeforeMeasurementId)) as bufferBeforeMergeId,\r\n"
				+ "    (select \r\n"
				+ "        averageFilePath\r\n"
				+ "    from\r\n"
				+ "        Merge m2\r\n"
				+ "    where\r\n"
				+ "        m2.measurementId = bufferBeforeMeasurementId and m2.mergeId in (select \r\n"
				+ "            max(m3.mergeId)\r\n"
				+ "        from\r\n"
				+ "            Merge m3\r\n"
				+ "        where\r\n"
				+ "            m3.measurementId = bufferBeforeMeasurementId)) as bufferBeforeAverageFilePath,\r\n"
				+ "    (select \r\n"
				+ "        mtd2.measurementId\r\n"
				+ "    from\r\n"
				+ "        MeasurementToDataCollection mtd2\r\n"
				+ "    where\r\n"
				+ "        mtd.dataCollectionId = mtd2.dataCollectionId and mtd2.dataCollectionOrder = 2) as sampleMeasurementId,\r\n"
				+ "    (select \r\n"
				+ "        mergeId\r\n"
				+ "    from\r\n"
				+ "        Merge m2\r\n"
				+ "    where\r\n"
				+ "        m2.measurementId = sampleMeasurementId and m2.mergeId in (select \r\n"
				+ "            max(m3.mergeId)\r\n"
				+ "        from\r\n"
				+ "            Merge m3\r\n"
				+ "        where\r\n"
				+ "            m3.measurementId = sampleMeasurementId)) as sampleMergeId,\r\n"
				+ "    (select \r\n"
				+ "        averageFilePath\r\n"
				+ "    from\r\n"
				+ "        Merge m2\r\n"
				+ "    where\r\n"
				+ "        m2.measurementId = sampleMeasurementId and m2.mergeId in (select \r\n"
				+ "            max(m3.mergeId)\r\n"
				+ "        from\r\n"
				+ "            Merge m3\r\n"
				+ "        where\r\n"
				+ "            m3.measurementId = sampleMeasurementId)) as averageFilePath,\r\n"
				+ "    (select \r\n"
				+ "        framesMerge\r\n"
				+ "    from\r\n"
				+ "        Merge m2\r\n"
				+ "    where\r\n"
				+ "        m2.measurementId = sampleMeasurementId and m2.mergeId in (select \r\n"
				+ "            max(m3.mergeId)\r\n"
				+ "        from\r\n"
				+ "            Merge m3\r\n"
				+ "        where\r\n"
				+ "            m3.measurementId = sampleMeasurementId)) as framesMerge,\r\n"
				+ "    (select \r\n"
				+ "        m2.framesCount\r\n"
				+ "    from\r\n"
				+ "        Merge m2\r\n"
				+ "    where\r\n"
				+ "        m2.measurementId = sampleMeasurementId and m2.mergeId in (select \r\n"
				+ "            max(m3.mergeId)\r\n"
				+ "        from\r\n"
				+ "            Merge m3\r\n"
				+ "        where\r\n"
				+ "            m3.measurementId = sampleMeasurementId)) as framesCount,\r\n"
				+ "    (select \r\n"
				+ "        framesMerge\r\n"
				+ "    from\r\n"
				+ "        Merge m2\r\n"
				+ "    where\r\n"
				+ "        m2.measurementId = bufferAfterMeasurementId and m2.mergeId in (select \r\n"
				+ "            max(m3.mergeId)\r\n"
				+ "        from\r\n"
				+ "            Merge m3\r\n"
				+ "        where\r\n"
				+ "            m3.measurementId = bufferAfterMeasurementId)) as bufferAfterFramesMerged,\r\n"
				+ "    (select \r\n"
				+ "        framesCount\r\n"
				+ "    from\r\n"
				+ "        Merge m2\r\n"
				+ "    where\r\n"
				+ "        m2.measurementId = bufferAfterMeasurementId and m2.mergeId in (select \r\n"
				+ "            max(m3.mergeId)\r\n"
				+ "        from\r\n"
				+ "            Merge m3\r\n"
				+ "        where\r\n"
				+ "            m3.measurementId = bufferAfterMeasurementId)) as bufferAfterFramesCount,\r\n"
				+ "    (select \r\n"
				+ "        mergeId\r\n"
				+ "    from\r\n"
				+ "        Merge m2\r\n"
				+ "    where\r\n"
				+ "        m2.measurementId = bufferAfterMeasurementId and m2.mergeId in (select \r\n"
				+ "            max(m3.mergeId)\r\n"
				+ "        from\r\n"
				+ "            Merge m3\r\n"
				+ "        where\r\n"
				+ "            m3.measurementId = bufferAfterMeasurementId)) as bufferAfterMergeId,\r\n"
				+ "    (select \r\n"
				+ "        m2.averageFilePath\r\n"
				+ "    from\r\n"
				+ "        Merge m2\r\n"
				+ "    where\r\n"
				+ "        m2.measurementId = bufferAfterMeasurementId and m2.mergeId in (select \r\n"
				+ "            max(m3.mergeId)\r\n"
				+ "        from\r\n"
				+ "            Merge m3\r\n"
				+ "        where\r\n"
				+ "            m3.measurementId = bufferAfterMeasurementId)) as bufferAfterAverageFilePath,\r\n"
				+ "    (select \r\n"
				+ "        modelList.modelListId\r\n"
				+ "    from\r\n"
				+ "        ModelList modelList,\r\n"
				+ "        AbInitioModel ab,\r\n"
				+ "        SubtractionToAbInitioModel subToAb,\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.dataCollectionId = dc.dataCollectionId and modelList.modelListId = ab.modelListId and ab.abInitioModelId = subToAb.abInitioId and subToAb.subtractionId and subToAb.abInitioId = (select \r\n"
				+ "            max(subToAb2.abInitioId)\r\n"
				+ "        from\r\n"
				+ "            SubtractionToAbInitioModel subToAb2\r\n"
				+ "        where\r\n"
				+ "            subToAb2.subtractionId = su.subtractionId)) as modelListId,\r\n"
				+ "    (select \r\n"
				+ "        nsdFilePath\r\n"
				+ "    from\r\n"
				+ "        ModelList modelList,\r\n"
				+ "        AbInitioModel ab,\r\n"
				+ "        SubtractionToAbInitioModel subToAb,\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.dataCollectionId = dc.dataCollectionId and modelList.modelListId = ab.modelListId and ab.abInitioModelId = subToAb.abInitioId and subToAb.subtractionId and subToAb.abInitioId = (select \r\n"
				+ "            max(subToAb2.abInitioId)\r\n"
				+ "        from\r\n"
				+ "            SubtractionToAbInitioModel subToAb2\r\n"
				+ "        where\r\n"
				+ "            subToAb2.subtractionId = su.subtractionId)) as nsdFilePath,\r\n"
				// commented because the query is duplicated
				// + "    (select \r\n"
				// + "        modelList.modelListId\r\n"
				// + "    from\r\n"
				// + "        ModelList modelList,\r\n"
				// + "        AbInitioModel ab,\r\n"
				// + "        SubtractionToAbInitioModel subToAb,\r\n"
				// + "        Subtraction su\r\n"
				// + "    where\r\n"
				// +
				// "        su.dataCollectionId = dc.dataCollectionId and modelList.modelListId = ab.modelListId and ab.abInitioModelId = subToAb.abInitioId and subToAb.subtractionId and subToAb.abInitioId = (select \r\n"
				// + "            max(subToAb2.abInitioId)\r\n"
				// + "        from\r\n"
				// + "            SubtractionToAbInitioModel subToAb2\r\n"
				// + "        where\r\n"
				// +
				// "            subToAb2.subtractionId = su.subtractionId)) as modelListId,\r\n"
				+ "    (select \r\n"
				+ "        chi2RgFilePath\r\n"
				+ "    from\r\n"
				+ "        ModelList modelList,\r\n"
				+ "        AbInitioModel ab,\r\n"
				+ "        SubtractionToAbInitioModel subToAb,\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.dataCollectionId = dc.dataCollectionId and modelList.modelListId = ab.modelListId and ab.abInitioModelId = subToAb.abInitioId and subToAb.abInitioId = (select \r\n"
				+ "            max(subToAb2.abInitioId)\r\n"
				+ "        from\r\n"
				+ "            SubtractionToAbInitioModel subToAb2\r\n"
				+ "        where\r\n"
				+ "            subToAb2.subtractionId = su.subtractionId)) as chi2RgFilePath,\r\n"
				+ "    (select \r\n"
				+ "        pdbFile\r\n"
				+ "    from\r\n"
				+ "        AbInitioModel ab,\r\n"
				+ "        SubtractionToAbInitioModel subToAb,\r\n"
				+ "        Model model,\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.dataCollectionId = dc.dataCollectionId and model.modelId = ab.averagedModelId and ab.abInitioModelId = subToAb.abInitioId and subToAb.abInitioId = (select \r\n"
				+ "            max(subToAb2.abInitioId)\r\n"
				+ "        from\r\n"
				+ "            SubtractionToAbInitioModel subToAb2\r\n"
				+ "        where\r\n"
				+ "            subToAb2.subtractionId = su.subtractionId)) as averagedModel,\r\n"
				+ "    (select \r\n"
				+ "        modelId\r\n"
				+ "    from\r\n"
				+ "        AbInitioModel ab,\r\n"
				+ "        SubtractionToAbInitioModel subToAb,\r\n"
				+ "        Model model,\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.dataCollectionId = dc.dataCollectionId and model.modelId = ab.averagedModelId and ab.abInitioModelId = subToAb.abInitioId and subToAb.abInitioId = (select \r\n"
				+ "            max(subToAb2.abInitioId)\r\n"
				+ "        from\r\n"
				+ "            SubtractionToAbInitioModel subToAb2\r\n"
				+ "        where\r\n"
				+ "            subToAb2.subtractionId = su.subtractionId)) as averagedModelId,\r\n"
				+ "    (select \r\n"
				+ "        pdbFile\r\n"
				+ "    from\r\n"
				+ "        AbInitioModel ab,\r\n"
				+ "        SubtractionToAbInitioModel subToAb,\r\n"
				+ "        Model model,\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.dataCollectionId = dc.dataCollectionId and model.modelId = ab.rapidShapeDeterminationModelId and ab.abInitioModelId = subToAb.abInitioId and subToAb.abInitioId = (select \r\n"
				+ "            max(subToAb2.abInitioId)\r\n"
				+ "        from\r\n"
				+ "            SubtractionToAbInitioModel subToAb2\r\n"
				+ "        where\r\n"
				+ "            subToAb2.subtractionId = su.subtractionId)) as rapidShapeDeterminationModel,\r\n"
				+ "    (select \r\n"
				+ "        modelId\r\n"
				+ "    from\r\n"
				+ "        AbInitioModel ab,\r\n"
				+ "        SubtractionToAbInitioModel subToAb,\r\n"
				+ "        Model model,\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.dataCollectionId = dc.dataCollectionId and model.modelId = ab.rapidShapeDeterminationModelId and ab.abInitioModelId = subToAb.abInitioId and subToAb.abInitioId = (select \r\n"
				+ "            max(subToAb2.abInitioId)\r\n"
				+ "        from\r\n"
				+ "            SubtractionToAbInitioModel subToAb2\r\n"
				+ "        where\r\n"
				+ "            subToAb2.subtractionId = su.subtractionId)) as rapidShapeDeterminationModelId,\r\n"
				+ "    (select \r\n"
				+ "        pdbFile\r\n"
				+ "    from\r\n"
				+ "        AbInitioModel ab,\r\n"
				+ "        SubtractionToAbInitioModel subToAb,\r\n"
				+ "        Model model,\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.dataCollectionId = dc.dataCollectionId and model.modelId = ab.shapeDeterminationModelId and ab.abInitioModelId = subToAb.abInitioId and subToAb.abInitioId = (select \r\n"
				+ "            max(subToAb2.abInitioId)\r\n"
				+ "        from\r\n"
				+ "            SubtractionToAbInitioModel subToAb2\r\n"
				+ "        where\r\n"
				+ "            subToAb2.subtractionId = su.subtractionId)) as shapeDeterminationModel,\r\n"
				+ "    (select \r\n"
				+ "        modelId\r\n"
				+ "    from\r\n"
				+ "        AbInitioModel ab,\r\n"
				+ "        SubtractionToAbInitioModel subToAb,\r\n"
				+ "        Model model,\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.dataCollectionId = dc.dataCollectionId and model.modelId = ab.shapeDeterminationModelId and ab.abInitioModelId = subToAb.abInitioId and subToAb.abInitioId = (select \r\n"
				+ "            max(subToAb2.abInitioId)\r\n"
				+ "        from\r\n"
				+ "            SubtractionToAbInitioModel subToAb2\r\n"
				+ "        where\r\n"
				+ "            subToAb2.subtractionId = su.subtractionId)) as shapeDeterminationModelId,\r\n"
				+ "    (select \r\n"
				+ "        ab.abInitioModelId\r\n"
				+ "    from\r\n"
				+ "        AbInitioModel ab,\r\n"
				+ "        SubtractionToAbInitioModel subToAb,\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.dataCollectionId = dc.dataCollectionId and ab.abInitioModelId = subToAb.abInitioId and subToAb.abInitioId = (select \r\n"
				+ "            max(subToAb2.abInitioId)\r\n"
				+ "        from\r\n"
				+ "            SubtractionToAbInitioModel subToAb2\r\n"
				+ "        where\r\n"
				+ "            subToAb2.subtractionId = su.subtractionId)) as abInitioModelId,\r\n"
				+ "    (select \r\n"
				+ "        comments\r\n"
				+ "    from\r\n"
				+ "        AbInitioModel ab,\r\n"
				+ "        SubtractionToAbInitioModel subToAb,\r\n"
				+ "        Subtraction su\r\n"
				+ "    where\r\n"
				+ "        su.dataCollectionId = dc.dataCollectionId and ab.abInitioModelId = subToAb.abInitioId and subToAb.abInitioId = (select \r\n"
				+ "            max(subToAb2.abInitioId)\r\n"
				+ "        from\r\n"
				+ "            SubtractionToAbInitioModel subToAb2\r\n"
				+ "        where\r\n"
				+ "            subToAb2.subtractionId = su.subtractionId)) as comments\r\n"
				+ "from\r\n"
				+ "    Experiment exp,\r\n"
				+ "    Buffer bu,\r\n"
				+ "    SaxsDataCollection dc,\r\n"
				+ "    Macromolecule ma,\r\n"
				+ "    MeasurementToDataCollection mtd,\r\n"
				+ "    Specimen s,\r\n"
				+ "    Measurement m,\r\n"
				+ "    Proposal p\r\n"
				+ "where\r\n"
				+ "    bu.bufferId = s.bufferId and p.proposalId = exp.proposalId and m.specimenId = s.specimenId "
				+ " and dc.dataCollectionId = mtd.dataCollectionId and mtd.measurementId = m.measurementId and s.macromoleculeId = ma.macromoleculeId and s.experimentId = exp.experimentId "
				+ " and exp.experimentType != 'TEMPLATE' ";

	}
	public static String getBLSessionTable() {
		return
"BLSession.sessionId as BLSession_sessionId,\r\n" + 
"BLSession.expSessionPk as BLSession_expSessionPk,\r\n" + 
"BLSession.beamLineSetupId as BLSession_beamLineSetupId,\r\n" + 
"BLSession.proposalId as BLSession_proposalId,\r\n" + 
"BLSession.projectCode as BLSession_projectCode,\r\n" + 
"BLSession.startDate as BLSession_startDate,\r\n" + 
"BLSession.endDate as BLSession_endDate,\r\n" + 
"BLSession.beamLineName as BLSession_beamLineName,\r\n" + 
"BLSession.scheduled as BLSession_scheduled,\r\n" + 
"BLSession.nbShifts as BLSession_nbShifts,\r\n" + 
"BLSession.comments as BLSession_comments,\r\n" + 
"BLSession.beamLineOperator as BLSession_beamLineOperator,\r\n" + 
"BLSession.visit_number as BLSession_visit_number,\r\n" + 
"BLSession.bltimeStamp as BLSession_bltimeStamp,\r\n" + 
"BLSession.usedFlag as BLSession_usedFlag,\r\n" + 
"BLSession.sessionTitle as BLSession_sessionTitle,\r\n" + 
"BLSession.structureDeterminations as BLSession_structureDeterminations,\r\n" + 
"BLSession.dewarTransport as BLSession_dewarTransport,\r\n" + 
"BLSession.databackupFrance as BLSession_databackupFrance,\r\n" + 
"BLSession.databackupEurope as BLSession_databackupEurope,\r\n" + 
"BLSession.operatorSiteNumber as BLSession_operatorSiteNumber,\r\n" + 
"BLSession.lastUpdate as BLSession_lastUpdate,\r\n" + 
"BLSession.protectedData as BLSession_protectedData";
}
	
	public static String getProposalTable() {
		return "	Proposal.proposalId as Proposal_proposalId,\r\n" + 
				"	Proposal.personId as Proposal_personId,\r\n" + 
				"	Proposal.title as Proposal_title,\r\n" + 
				"	Proposal.proposalCode as Proposal_proposalCode,\r\n" + 
				"	Proposal.proposalNumber as Proposal_proposalNumber,\r\n" + 
				"	Proposal.proposalType as Proposal_proposalType ";
	}
	public static String getSessionByCodeAndNumber() {
		return "select " + 
				SQLQueryKeeper.getBLSessionTable() + "," +
				SQLQueryKeeper.getProposalTable()
				+ " from BLSession, Proposal where Proposal.proposalId = BLSession.proposalId and Proposal.proposalCode= :proposalCode  and Proposal.proposalNumber= :proposalNumber";
	}

	public static String getShippingTable() {
		return  "Shipping.shippingId as Shipping_shippingId,\r\n" + 
				"Shipping.proposalId as Shipping_proposalId,\r\n" + 
				"Shipping.shippingName as Shipping_shippingName,\r\n" + 
				"Shipping.deliveryAgent_agentName as Shipping_deliveryAgent_agentName,\r\n" + 
				"Shipping.deliveryAgent_shippingDate as Shipping_deliveryAgent_shippingDate,\r\n" + 
				"Shipping.deliveryAgent_deliveryDate as Shipping_deliveryAgent_deliveryDate,\r\n" + 
				"Shipping.deliveryAgent_agentCode as Shipping_deliveryAgent_agentCode,\r\n" + 
				"Shipping.deliveryAgent_flightCode as Shipping_deliveryAgent_flightCode,\r\n" + 
				"Shipping.shippingStatus as Shipping_shippingStatus,\r\n" + 
				"Shipping.bltimeStamp as Shipping_bltimeStamp,\r\n" + 
				"Shipping.laboratoryId as Shipping_laboratoryId,\r\n" + 
				"Shipping.isStorageShipping as Shipping_isStorageShipping,\r\n" + 
				"Shipping.creationDate as Shipping_creationDate,\r\n" + 
				"Shipping.comments as Shipping_comments,\r\n" + 
				"Shipping.sendingLabContactId as Shipping_sendingLabContactId,\r\n" + 
				"Shipping.returnLabContactId as Shipping_returnLabContactId,\r\n" + 
				"Shipping.returnCourier as Shipping_returnCourier,\r\n" + 
				"Shipping.dateOfShippingToUser as Shipping_dateOfShippingToUser,\r\n" + 
				"Shipping.shippingType as Shipping_shippingType\r\n";
	}

	public static String getDewarTable() {
		return "Dewar.dewarId  as Dewar_dewarId,  \r\n" + 
				"Dewar.shippingId as Dewar_shippingId,                 \r\n" + 
				"Dewar.code as Dewar_code,                \r\n" + 
				"Dewar.comments as Dewar_comments,                      \r\n" + 
				"Dewar.storageLocation as Dewar_storageLocation,               \r\n" + 
				"Dewar.dewarStatus as Dewar_dewarStatus,                   \r\n" + 
				"Dewar.bltimeStamp as Dewar_bltimeStamp,                    \r\n" + 
				"Dewar.isStorageDewar as Dewar_isStorageDewar,                \r\n" + 
				"Dewar.barCode as Dewar_barCode,                       \r\n" + 
				"Dewar.firstExperimentId as Dewar_firstExperimentId,\r\n" + 
				"Dewar.customsValue as Dewar_customsValue,                  \r\n" + 
				"Dewar.transportValue as Dewar_transportValue,               \r\n" + 
				"Dewar.trackingNumberToSynchrotron as Dewar_trackingNumberToSynchrotron,  \r\n" + 
				"Dewar.trackingNumberFromSynchrotron as Dewar_trackingNumberFromSynchrotron, \r\n" + 
				"Dewar.type as Dewar_type " ;
	}

	public static String getContainerTable() {
		return  "Container.containerId as Container_containerId,\r\n" + 
				"Container.dewarId as Container_dewarId,\r\n" + 
				"Container.code as Container_code,\r\n" + 
				"Container.containerType as Container_containerType,\r\n" + 
				"Container.capacity as Container_capacity,\r\n" + 
				"Container.beamlineLocation as Container_beamlineLocation,\r\n" + 
				"Container.sampleChangerLocation as Container_sampleChangerLocation,\r\n" + 
				"Container.containerStatus as Container_containerStatus,\r\n" + 
				"Container.bltimeStamp as Container_bltimeStamp " ;
	}

	public static String getDataCollectionGroupTable() {
		return "DataCollectionGroup.dataCollectionGroupId as DataCollectionGroup_dataCollectionGroupId,\r\n" + 
				"DataCollectionGroup.blSampleId as DataCollectionGroup_blSampleId,\r\n" + 
				"DataCollectionGroup.sessionId as DataCollectionGroup_sessionId,\r\n" + 
				"DataCollectionGroup.workflowId as DataCollectionGroup_workflowId,\r\n" + 
				"DataCollectionGroup.experimentType as DataCollectionGroup_experimentType,\r\n" + 
				"DataCollectionGroup.startTime as DataCollectionGroup_startTime,\r\n" + 
				"DataCollectionGroup.endTime as DataCollectionGroup_endTime,\r\n" + 
				"DataCollectionGroup.crystalClass as DataCollectionGroup_crystalClass,\r\n" + 
				"DataCollectionGroup.comments as DataCollectionGroup_comments,\r\n" + 
				"DataCollectionGroup.detectorMode as DataCollectionGroup_detectorMode,\r\n" + 
				"DataCollectionGroup.actualSampleBarcode as DataCollectionGroup_actualSampleBarcode,\r\n" + 
				"DataCollectionGroup.actualSampleSlotInContainer as DataCollectionGroup_actualSampleSlotInContainer,\r\n" + 
				"DataCollectionGroup.actualContainerBarcode as DataCollectionGroup_actualContainerBarcode,\r\n" + 
				"DataCollectionGroup.actualContainerSlotInSC as DataCollectionGroup_actualContainerSlotInSC,\r\n" + 
				"DataCollectionGroup.xtalSnapshotFullPath as DataCollectionGroup_xtalSnapshotFullPath ";
	}

	public static String getImageTable() {
		return  "Image.imageId as Image_imageId,\r\n" + 
				"Image.dataCollectionId as Image_dataCollectionId,\r\n" + 
				"Image.motorPositionId as Image_motorPositionId,\r\n" + 
				"Image.imageNumber as Image_imageNumber,\r\n" + 
				"Image.fileName as Image_fileName,\r\n" + 
				"Image.fileLocation as Image_fileLocation,\r\n" + 
				"Image.measuredIntensity as Image_measuredIntensity,\r\n" + 
				"Image.jpegFileFullPath as Image_jpegFileFullPath,\r\n" + 
				"Image.jpegThumbnailFileFullPath as Image_jpegThumbnailFileFullPath,\r\n" + 
				"Image.temperature as Image_temperature,\r\n" + 
				"Image.cumulativeIntensity as Image_cumulativeIntensity,\r\n" + 
				"Image.synchrotronCurrent as Image_synchrotronCurrent,\r\n" + 
				"Image.comments as Image_comments,\r\n" + 
				"Image.machineMessage as Image_machineMessage,\r\n" + 
				"Image.recordTimeStamp as Image_recordTimeStamp ";
	}

	public static String getWorkflowTable() {
		return 	"Workflow.workflowId as Workflow_workflowId,\r\n" + 
				"Workflow.proposalId as Workflow_proposalId,\r\n" + 
				"Workflow.workflowTitle as Workflow_workflowTitle,\r\n" + 
				"Workflow.workflowType as Workflow_workflowType,\r\n" + 
				"Workflow.comments as Workflow_comments,\r\n" + 
				"Workflow.status as Workflow_status,\r\n" + 
				"Workflow.resultFilePath as Workflow_resultFilePath,\r\n" + 
				"Workflow.logFilePath as Workflow_logFilePath,\r\n" + 
				"Workflow.recordTimeStamp as Workflow_recordTimeStamp " ;
	}

	public static String getWorkflowMeshTable() {
		return "WorkflowMesh.workflowMeshId as WorkflowMesh_workflowMeshId,\r\n" + 
				"WorkflowMesh.workflowId as WorkflowMesh_workflowId,\r\n" + 
				"WorkflowMesh.bestPositionId as WorkflowMesh_bestPositionId,\r\n" + 
				"WorkflowMesh.bestImageId as WorkflowMesh_bestImageId,\r\n" + 
				"WorkflowMesh.value1 as WorkflowMesh_value1,\r\n" + 
				"WorkflowMesh.value2 as WorkflowMesh_value2,\r\n" + 
				"WorkflowMesh.value3 as WorkflowMesh_value3,\r\n" + 
				"WorkflowMesh.value4 as WorkflowMesh_value4,\r\n" + 
				"WorkflowMesh.cartographyPath as WorkflowMesh_cartographyPath,\r\n" + 
				"WorkflowMesh.recordTimeStamp as WorkflowMesh_recordTimeStamp " ;
	}

	public static String getDataCollectionTable() {
		return "DataCollection.dataCollectionId as DataCollection_dataCollectionId,\r\n" + 
				"DataCollection.dataCollectionGroupId as DataCollection_dataCollectionGroupId,\r\n" + 
				"DataCollection.strategySubWedgeOrigId as DataCollection_strategySubWedgeOrigId,\r\n" + 
				"DataCollection.detectorId as DataCollection_detectorId,\r\n" + 
				"DataCollection.blSubSampleId as DataCollection_blSubSampleId,\r\n" + 
				"DataCollection.startPositionId as DataCollection_startPositionId,\r\n" + 
				"DataCollection.endPositionId as DataCollection_endPositionId,\r\n" + 
				"DataCollection.dataCollectionNumber as DataCollection_dataCollectionNumber,\r\n" + 
				"DataCollection.startTime as DataCollection_startTime,\r\n" + 
				"DataCollection.endTime as DataCollection_endTime,\r\n" + 
				"DataCollection.runStatus as DataCollection_runStatus,\r\n" + 
				"DataCollection.axisStart as DataCollection_axisStart,\r\n" + 
				"DataCollection.axisEnd as DataCollection_axisEnd,\r\n" + 
				"DataCollection.axisRange as DataCollection_axisRange,\r\n" + 
				"DataCollection.overlap as DataCollection_overlap,\r\n" + 
				"DataCollection.numberOfImages as DataCollection_numberOfImages,\r\n" + 
				"DataCollection.startImageNumber as DataCollection_startImageNumber,\r\n" + 
				"DataCollection.numberOfPasses as DataCollection_numberOfPasses,\r\n" + 
				"DataCollection.exposureTime as DataCollection_exposureTime,\r\n" + 
				"DataCollection.imageDirectory as DataCollection_imageDirectory,\r\n" + 
				"DataCollection.imagePrefix as DataCollection_imagePrefix,\r\n" + 
				"DataCollection.imageSuffix as DataCollection_imageSuffix,\r\n" + 
				"DataCollection.fileTemplate as DataCollection_fileTemplate,\r\n" + 
				"DataCollection.wavelength as DataCollection_wavelength,\r\n" + 
				"DataCollection.resolution as DataCollection_resolution,\r\n" + 
				"DataCollection.detectorDistance as DataCollection_detectorDistance,\r\n" + 
				"DataCollection.xBeam as DataCollection_xBeam,\r\n" + 
				"DataCollection.yBeam as DataCollection_yBeam,\r\n" + 
				"DataCollection.comments as DataCollection_comments,\r\n" + 
				"DataCollection.printableForReport as DataCollection_printableForReport,\r\n" + 
				"DataCollection.slitGapVertical as DataCollection_slitGapVertical,\r\n" + 
				"DataCollection.slitGapHorizontal as DataCollection_slitGapHorizontal,\r\n" + 
				"DataCollection.transmission as DataCollection_transmission,\r\n" + 
				"DataCollection.synchrotronMode as DataCollection_synchrotronMode,\r\n" + 
				"DataCollection.xtalSnapshotFullPath1 as DataCollection_xtalSnapshotFullPath1,\r\n" + 
				"DataCollection.xtalSnapshotFullPath2 as DataCollection_xtalSnapshotFullPath2,\r\n" + 
				"DataCollection.xtalSnapshotFullPath3 as DataCollection_xtalSnapshotFullPath3,\r\n" + 
				"DataCollection.xtalSnapshotFullPath4 as DataCollection_xtalSnapshotFullPath4,\r\n" + 
				"DataCollection.rotationAxis as DataCollection_rotationAxis,\r\n" + 
				"DataCollection.phiStart as DataCollection_phiStart,\r\n" + 
				"DataCollection.kappaStart as DataCollection_kappaStart,\r\n" + 
				"DataCollection.omegaStart as DataCollection_omegaStart,\r\n" + 
				"DataCollection.resolutionAtCorner as DataCollection_resolutionAtCorner,\r\n" + 
				"DataCollection.detector2Theta as DataCollection_detector2Theta,\r\n" + 
				"DataCollection.undulatorGap1 as DataCollection_undulatorGap1,\r\n" + 
				"DataCollection.undulatorGap2 as DataCollection_undulatorGap2,\r\n" + 
				"DataCollection.undulatorGap3 as DataCollection_undulatorGap3,\r\n" + 
				"DataCollection.beamSizeAtSampleX as DataCollection_beamSizeAtSampleX,\r\n" + 
				"DataCollection.beamSizeAtSampleY as DataCollection_beamSizeAtSampleY,\r\n" + 
				"DataCollection.centeringMethod as DataCollection_centeringMethod,\r\n" + 
				"DataCollection.averageTemperature as DataCollection_averageTemperature,\r\n" + 
				"DataCollection.actualCenteringPosition as DataCollection_actualCenteringPosition,\r\n" + 
				"DataCollection.beamShape as DataCollection_beamShape,\r\n" + 
				"DataCollection.flux as DataCollection_flux,\r\n" + 
				"DataCollection.flux_end as DataCollection_flux_end,\r\n" + 
				"DataCollection.totalAbsorbedDose as DataCollection_totalAbsorbedDose,\r\n" + 
				"DataCollection.bestWilsonPlotPath as DataCollection_bestWilsonPlotPath ";
	}

	

}
