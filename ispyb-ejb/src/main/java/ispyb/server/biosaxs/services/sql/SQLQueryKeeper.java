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

package ispyb.server.biosaxs.services.sql;

public class SQLQueryKeeper {

	
	
	public static String getExperimentListByExperimentId(Integer proposalId,
			Integer experimentId) {
		StringBuilder sb = new StringBuilder(SQLQueryKeeper.getExperimentListByProposalId(proposalId));
		sb.append(" and e.experimentId = :experimentId ");
		return sb.toString();
	}
		
	public static String getDataCollectionByProposal() {
		return "select "
				+ SqlTableMapper.getProposalTable() + ","  
				+ SqlTableMapper.getCrystalTable() + ","  
				+ SqlTableMapper.getProteinTable() + "," 
				+ SqlTableMapper.getImageTable() + "," 
				+ SqlTableMapper.getBLSampleTable() 
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
	
	public static String getSessionByCodeAndNumber() {
		return "select " + 
				SqlTableMapper.getBLSessionTable() + "," +
				SqlTableMapper.getProposalTable()
				+ " from BLSession, Proposal where Proposal.proposalId = BLSession.proposalId and Proposal.proposalCode= :proposalCode  and Proposal.proposalNumber= :proposalNumber";
	}

	

}
