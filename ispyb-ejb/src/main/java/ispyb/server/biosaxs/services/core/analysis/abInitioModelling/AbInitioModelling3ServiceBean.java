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

package ispyb.server.biosaxs.services.core.analysis.abInitioModelling;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import ispyb.server.biosaxs.services.sql.SQLQueryKeeper;
import ispyb.server.biosaxs.vos.datacollection.AbInitioModel3VO;
import ispyb.server.biosaxs.vos.datacollection.Model3VO;
import ispyb.server.biosaxs.vos.datacollection.ModelList3VO;
import ispyb.server.biosaxs.vos.datacollection.ModelToList3VO;
import ispyb.server.biosaxs.vos.datacollection.Subtraction3VO;
import ispyb.server.biosaxs.vos.datacollection.SubtractiontoAbInitioModel3VO;
import ispyb.server.common.util.ISPyBRuntimeException;



@Stateless
public class AbInitioModelling3ServiceBean implements AbInitioModelling3Service, AbInitioModelling3ServiceLocal {
	private final static Logger LOG = Logger.getLogger("AbInitioModelling3ServiceBean");
	
	
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	/** The now. */
	protected static Calendar NOW;

	private Date getNow(){
		 NOW = GregorianCalendar.getInstance();
		 return NOW.getTime();
	}
	
	@Override
	public ModelList3VO getModelListById(int modelListId) {
		String query = "SELECT modelList FROM ModelList3VO modelList where modelList.modelListId = :modelListId" ;
		Query EJBQuery = this.entityManager.createQuery(query).setParameter("modelListId", modelListId);
		return (ModelList3VO) EJBQuery.getSingleResult();	
	}
	
	private List<Map<String, Object>> getAll(String mySQLQuery) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.list();
		return 	aliasToValueMapList;
	}
	
	@Override
	public List<Map<String, Object>> getAllByProposalId(int proposalId) {
		return this.getAll(SQLQueryKeeper.getAnalysisByProposalId(proposalId));
	}
	
	@Override
	public List<Map<String, Object>> getAnalysisInformationByExperimentId(int experimentId) {
		return this.getAll(SQLQueryKeeper.getAnalysisByExperimentId(experimentId));
	}
	
	@Override
	public List<Map<String, Object>> getAnalysisCalibrationByProposalId(int experimentId) {
		String mySQLQuery = SQLQueryKeeper.getAnalysisCalibrationByProposalId(experimentId);
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.list();
		return 	aliasToValueMapList;
	}
	
	
	@Override
	public List<Map<String, Object>> getAnalysisInformation(int limit) {
		String mySQLQuery = SQLQueryKeeper.getAnalysis(limit);
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.list();
		return 	aliasToValueMapList;
	}
	
	private AbInitioModel3VO getAbInitioModel3VOBySubstractionList(List<Subtraction3VO> substractions){
		/** Getting last subtraction **/
		Subtraction3VO subtraction3VO = substractions.get(substractions.size() - 1);
		if (subtraction3VO.getSubstractionToAbInitioModel3VOs().size() > 0){
			LOG.info("subtraction3VO.getSubstractionToAbInitioModel3VOs().size() = " + subtraction3VO.getSubstractionToAbInitioModel3VOs().size());
			Iterator<SubtractiontoAbInitioModel3VO> iterator = subtraction3VO.getSubstractionToAbInitioModel3VOs().iterator();
			LOG.info("subtraction3VO.getSubstractionToAbInitioModel3VOs().size() = " + subtraction3VO.getSubstractionToAbInitioModel3VOs().size());
			if (iterator.hasNext()){
				SubtractiontoAbInitioModel3VO subtractiontoAbInitioModel3VO = iterator.next();
				LOG.info("Iterating over: " + subtractiontoAbInitioModel3VO.getAbinitiomodel3VO());
				if (subtractiontoAbInitioModel3VO.getAbinitiomodel3VO() != null){
					LOG.info("Abinitio model 3VO found: " + subtractiontoAbInitioModel3VO.getAbinitiomodel3VO().getAbInitioModelId());
					return subtractiontoAbInitioModel3VO.getAbinitiomodel3VO();
				}
			}
		}
		/** Abinitio not found so we create it **/
		LOG.info(" Abinitio not found so we create it");
		AbInitioModel3VO abInitioModel3VO = new AbInitioModel3VO();
		abInitioModel3VO.setCreationTime(getNow());
		abInitioModel3VO = this.merge(abInitioModel3VO);
//		for (Subtraction3VO subtraction : substractions) {
			SubtractiontoAbInitioModel3VO subtractiontoAbInitioModel3VO = new SubtractiontoAbInitioModel3VO();
			subtractiontoAbInitioModel3VO.setAbinitiomodel3VO(abInitioModel3VO);
			subtractiontoAbInitioModel3VO.setSubtraction3VO(subtraction3VO);
			this.merge(subtractiontoAbInitioModel3VO);
//		}
		return abInitioModel3VO;
	}
	
	
	/******************************************************************************************
	 * ABINITIO MODELING
	 * 
	 * AbInitio Modeling method to store all the data coming from the abInitio analysis
	 ******************************************************************************************/
	@Override
	public void addAbinitioModeling(ArrayList<Integer> measurementIds,ArrayList<Model3VO> models3vo, Model3VO dammaverModel,
			Model3VO dammifModel, Model3VO damminModel, String nsdPlot,String chi2plot) {
		/** 
		 *  Looking for subtraction by measurement Id 
		 * 	An AbInitio model could be the results of the analysis of several subtracted curves (see several concentrations of the same molecule)
		 * 
		 **/
		List<Subtraction3VO> substractions =  this.getSubtractionsByMeasurementList(measurementIds);
		LOG.info(Arrays.asList(measurementIds).toString());

		/** If there are subtractions associated to the measurements, it should be as we need a subtracted curve at least **/
		if (substractions.size() > 0){
			LOG.info("Subtractions found: " + substractions.size());
			AbInitioModel3VO abInitioModel3VO = this.getAbInitioModel3VOBySubstractionList(substractions);//new AbInitioModel3VO();
			if (models3vo != null){
				ModelList3VO modelList = this.storeModelList(models3vo, nsdPlot, chi2plot);
				if (modelList != null){
					abInitioModel3VO.setModelList3VO(modelList);
				}
			}
			if (dammaverModel != null){
				abInitioModel3VO.setAveragedModel(this.entityManager.merge(dammaverModel));
			}
			if (dammifModel != null){
				abInitioModel3VO.setRapidShapeDeterminationModel(this.entityManager.merge(dammifModel));
			}
			if(damminModel != null){
				abInitioModel3VO.setShapeDeterminationModel(this.entityManager.merge(damminModel));
			}
			abInitioModel3VO = this.merge(abInitioModel3VO);
		} else {
			throw new ISPyBRuntimeException("At least one substraction should be available but none found.");
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Subtraction3VO> getSubtractionsByMeasurementList(ArrayList<Integer> measurementIdList) {
		String query = "SELECT DISTINCT(subtraction) FROM Subtraction3VO subtraction, SaxsDataCollection3VO da, MeasurementTodataCollection3VO me  " +
				" WHERE da.dataCollectionId = me.dataCollectionId and " +
				" subtraction.dataCollectionId = da.dataCollectionId and " +
				" me.measurementId in :measurementIdList" ;
		Query EJBQuery = this.entityManager.createQuery(query).setParameter("measurementIdList", measurementIdList);
		return (List<Subtraction3VO>)EJBQuery.getResultList();
	}
	
	
	/**
	 * Add a new abinitiomodel if it does't exist yet for such measurementsIds
	 */
//	@Override
//	public void addModelsByMeasurementId(ArrayList<Integer> measurementIds, ArrayList<Model3VO> models3vo) {
//		List<Subtraction3VO> substractions =  this.getSubtractionsByMeasurementList(measurementIds);
//		/** Getting last subtraction **/
//		Subtraction3VO subtraction3VO = substractions.get(substractions.size() - 1);
//		if (subtraction3VO.getSubstractionToAbInitioModel3VOs() != null){
//			
//			
//		}
//		
//	}
	
	/** Given n models will create the model list which adinitio model analysis starts **/
	private ModelList3VO storeModelList(ArrayList<Model3VO> models3vo, String nsdFilePath, String chi2rgFilePath) {
		List<Model3VO> models = new ArrayList<Model3VO>();
		for (Model3VO model3vo : models3vo) {
			models.add(this.merge(model3vo));
		}
		
		ModelList3VO modelList = new ModelList3VO();
		modelList.setChi2rgFilePath(chi2rgFilePath);
		modelList.setNsdFilePath(nsdFilePath);
		modelList = this.merge(modelList);
		
		for (Model3VO model3vo : models) {
			ModelToList3VO modelToList = new ModelToList3VO();
			modelToList.setModel3VO(model3vo);
			modelToList.setModelList3VO(modelList);
			this.merge(modelToList);
		}
		return modelList;
	}
	
	@Override
	public Model3VO merge(Model3VO model) {
		return this.entityManager.merge(model);
	}
	
	@Override
	public ModelList3VO merge(ModelList3VO modelList3VO) {
		return this.entityManager.merge(modelList3VO);
	}
	
	@Override
	public AbInitioModel3VO merge(AbInitioModel3VO abInitioModel3VO) {
		return this.entityManager.merge(abInitioModel3VO);
	}
	
	@Override
	public SubtractiontoAbInitioModel3VO merge(SubtractiontoAbInitioModel3VO subtractiontoAbInitioModel3VO) {
		return this.entityManager.merge(subtractiontoAbInitioModel3VO);
	}
	
	@Override
	public ModelToList3VO merge(ModelToList3VO modelToList3VO) {
		return this.entityManager.merge(modelToList3VO);
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<AbInitioModel3VO> getAbinitioModelsByExperimentId(int experimentId) {
		String query = SQLQueryKeeper.getAbinitioModelQuery();
		query = query + " and dc.experimentId = " + experimentId;	
		
		LOG.info(query);
		Query EJBQuery = this.entityManager.createQuery(query);
		return (List<AbInitioModel3VO>)EJBQuery.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List test(String query) {
		LOG.info(query);
		Query EJBQuery = this.entityManager.createQuery(query);
		return (List)EJBQuery.getResultList();
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<Subtraction3VO> getAbinitioModelsBySubtractionId(int subtractionId) {
		String query = SQLQueryKeeper.getAbinitioModelQuery();
		query = query + " WHERE subtraction.subtractionId = " + subtractionId;	
		Query EJBQuery = this.entityManager.createQuery(query);
		return (List<Subtraction3VO>)EJBQuery.getResultList();
	}
	

	@Override
	public AbInitioModel3VO getAbinitioModelsById(int abinitioModelId) {
		String query = SQLQueryKeeper.getSingleAbinitioModelQuery();
		query = query + " and abInitioModel3VO.abInitioModelId= " + abinitioModelId;	
		Query EJBQuery = this.entityManager.createQuery(query);
		return (AbInitioModel3VO)EJBQuery.getSingleResult();
	}

	@Override
	public Model3VO getModelById(int modelId) {
		String query = SQLQueryKeeper.getModelQuery(modelId);
		Query EJBQuery = this.entityManager.createQuery(query);
		return (Model3VO)EJBQuery.getSingleResult();
	}

	public void addAbinitioModeling(ArrayList<Integer> measurementIds,
			ArrayList<Model3VO> models3vo, Model3VO referenceModel, 
			Model3VO refinedModel) {
		/** 
		 *  Looking for subtraction by measurement Id 
		 * 	An AbInitio model could be the results of the analysis of several subtracted curves (see several concentrations of the same molecule)
		 * 
		 **/
		List<Subtraction3VO> substractions =  this.getSubtractionsByMeasurementList(measurementIds);

		/** If there are subtractions associated to the measurements, it should be as we need a subtracted curve at least **/
		if (substractions.size() > 0){
			
			
			AbInitioModel3VO abInitioModel3VO = this.getAbInitioModel3VOBySubstractionList(substractions);//new AbInitioModel3VO();
			if (models3vo != null){
				if (models3vo.size() > 0){
					ModelList3VO modelList = new ModelList3VO();
					/** Adding new models **/
					if (abInitioModel3VO.getModelList3VO() != null){
						modelList = abInitioModel3VO.getModelList3VO();
						/** adding new model **/
						for (Model3VO model3vo : models3vo) {
							ModelToList3VO modelToList = new ModelToList3VO();
							modelToList.setModel3VO(this.merge(model3vo));
							modelToList.setModelList3VO(modelList);
							this.merge(modelToList);
						}
					}
					else{
						modelList = this.storeModelList(models3vo, null, null);
					}
					
					if (modelList != null){
						abInitioModel3VO.setModelList3VO(modelList);
					}
					abInitioModel3VO = this.merge(abInitioModel3VO);
				}
			}
			if (referenceModel != null){
				if (abInitioModel3VO.getAveragedModel() != null){
					LOG.info("Updating REFEREnce model");
					/** Updating the existing model **/
					Model3VO averageModel = abInitioModel3VO.getAveragedModel();
					averageModel.setDmax(referenceModel.getDmax());
					averageModel.setChiSqrt(referenceModel.getChiSqrt());
					averageModel.setFirFile(referenceModel.getFirFile());
					averageModel.setFitFile(referenceModel.getFitFile());
					averageModel.setLogFile(referenceModel.getLogFile());
					averageModel.setName(referenceModel.getName());
					averageModel.setPdbFile(referenceModel.getPdbFile());
					averageModel.setRfactor(referenceModel.getRfactor());
					averageModel.setRg(referenceModel.getRg());
					averageModel.setVolume(referenceModel.getVolume());
					this.merge(averageModel);
				}
				else{
					/** It doesn't exist yet then we create a new model **/
					abInitioModel3VO.setAveragedModel(this.merge(referenceModel));
					abInitioModel3VO = this.merge(abInitioModel3VO);
				}
			}
			if(refinedModel != null){
				if (abInitioModel3VO.getShapeDeterminationModel() != null){
					/** Updating the existing model **/
					Model3VO shapeModel = abInitioModel3VO.getShapeDeterminationModel();
					shapeModel.setDmax(refinedModel.getDmax());
					shapeModel.setChiSqrt(refinedModel.getChiSqrt());
					shapeModel.setFirFile(refinedModel.getFirFile());
					shapeModel.setFitFile(refinedModel.getFitFile());
					shapeModel.setLogFile(refinedModel.getLogFile());
					shapeModel.setName(refinedModel.getName());
					shapeModel.setPdbFile(refinedModel.getPdbFile());
					shapeModel.setRfactor(refinedModel.getRfactor());
					shapeModel.setRg(refinedModel.getRg());
					shapeModel.setVolume(refinedModel.getVolume());
					this.merge(shapeModel);
				}
				else{
					/** It doesn't exist yet then we create a new model **/
					abInitioModel3VO.setShapeDeterminationModel(this.merge(refinedModel));
					abInitioModel3VO = this.merge(abInitioModel3VO);
				}
			}
//			abInitioModel3VO = this.merge(abInitioModel3VO);
		}
		
	}

	private Boolean isSamePdbFile(Model3VO model, String pdbFilePath){
		if (model != null){
			if (model.getPdbFile() != null){
				if (pdbFilePath != null){
					if (model.getPdbFile().equals(pdbFilePath)){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public Model3VO getModel(Integer measurementId, String pdbfilepath) {
		
		ArrayList<Integer> measurementIds = new ArrayList<Integer>();
		measurementIds.add(measurementId);
		List<Subtraction3VO> substractions =  this.getSubtractionsByMeasurementList(measurementIds);
		if (substractions != null){
			if (substractions.size() > 0){
				/** If there is not abinitio modelling yet it means that the model doesn't not exist yet **/
				if (substractions.get(substractions.size()-1).getSubstractionToAbInitioModel3VOs().size() > 0 ){
					AbInitioModel3VO abInitioModel3VO = this.getAbInitioModel3VOBySubstractionList(substractions);//new AbInitioModel3VO();
					if (isSamePdbFile(abInitioModel3VO.getRapidShapeDeterminationModel(), pdbfilepath)){
						return abInitioModel3VO.getRapidShapeDeterminationModel();
					}
					
					if (isSamePdbFile(abInitioModel3VO.getAveragedModel(), pdbfilepath)){
						return abInitioModel3VO.getAveragedModel();
					}
					
					if (isSamePdbFile(abInitioModel3VO.getShapeDeterminationModel(), pdbfilepath)){
						return abInitioModel3VO.getShapeDeterminationModel();
					}
					
					/** Comparing models **/
					if (abInitioModel3VO.getModelList3VO() != null){
						if ( abInitioModel3VO.getModelList3VO().getModeltolist3VOs() != null){
							for (ModelToList3VO modelToList : abInitioModel3VO.getModelList3VO().getModeltolist3VOs()) {
								if (modelToList.getModel3VO() != null){
									if (isSamePdbFile(modelToList.getModel3VO(), pdbfilepath)){
										return modelToList.getModel3VO();
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public Model3VO findModelById(Integer modelId) {
		return this.entityManager.find(Model3VO.class, modelId);
	}
}
