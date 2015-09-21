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

package ispyb.server.biosaxs.services.core.analysis;


import ispyb.server.biosaxs.services.sql.SQLQueryKeeper;
import ispyb.server.biosaxs.vos.datacollection.SaxsDataCollection3VO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;



@Stateless
public class Analysis3ServiceBean implements Analysis3Service, Analysis3ServiceLocal {
	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;


	private List<Map<String, Object>> getAll(String mySQLQuery) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.list();
		return 	aliasToValueMapList;
	}
	
	@Override
	public List<Map<String,Object>> getAllByMacromolecule(int macromoleculeId, int poposalId) {
		return this.getAll(SQLQueryKeeper.getAnalysisByMacromoleculeId(macromoleculeId, poposalId));
	}
	
	@Override
	public List<Map<String,Object>> getAllByMacromolecule(int macromoleculeId, int bufferId, int poposalId) {
		return this.getAll(SQLQueryKeeper.getAnalysisByMacromoleculeId(macromoleculeId, bufferId, poposalId));
	}
	
	@Override
	public List<Map<String,Object>> getExperimentListByProposalId(int proposalId) {
		String mySQLQuery = SQLQueryKeeper.getExperimentListByProposalId(proposalId);
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("proposalId", proposalId);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.list();
		return 	aliasToValueMapList;
	}
	
	@Override
	public List<Map<String,Object>> getExperimentListByProposalId(int proposalId, String experimentType) {
		String mySQLQuery = SQLQueryKeeper.getExperimentListByProposalId(proposalId, experimentType);
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("proposalId", proposalId);
		query.setParameter("experimentType", experimentType);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		@SuppressWarnings("unchecked")   
		List<Map<String,Object>> aliasToValueMapList= query.list();
		return 	aliasToValueMapList;
	}
	

	@Override
	public List<Map<String, Object>> getExperimentListBySessionId(
			Integer proposalId, Integer sessionId) {
		String mySQLQuery = SQLQueryKeeper.getExperimentListBySessionId(proposalId, sessionId);
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("proposalId", proposalId);
		query.setParameter("sessionId", sessionId);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		@SuppressWarnings("unchecked")   
		List<Map<String,Object>> aliasToValueMapList= query.list();
		return 	aliasToValueMapList;
	}
	
	@Override
	public List<Map<String, Object>> getExperimentListByExperimentId(
			Integer proposalId, Integer experimentId) {
		String mySQLQuery = SQLQueryKeeper.getExperimentListByExperimentId(proposalId, experimentId);
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("proposalId", proposalId);
		query.setParameter("experimentId", experimentId);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		@SuppressWarnings("unchecked")   
		List<Map<String,Object>> aliasToValueMapList= query.list();
		return 	aliasToValueMapList;
	}
	
	@Override
	public List<Map<String,Object>> getCompactAnalysisByExperimentId(int experimentId) {
		String mySQLQuery = SQLQueryKeeper.getAnalysisCompactQueryByExperimentId();
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("experimentId", experimentId);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		@SuppressWarnings("unchecked")   
		List<Map<String,Object>> aliasToValueMapList= query.list();
		return 	aliasToValueMapList;
	}
	
	@Override
	public List<Map<String, Object>> getCompactAnalysisByProposalId(Integer proposalId, Integer limit) {
		StringBuilder mySQLQuery = new StringBuilder(SQLQueryKeeper.getAnalysisCompactQueryByProposalId(limit));
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery.toString());
		query.setParameter("proposalId", proposalId);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		@SuppressWarnings("unchecked")   
		List<Map<String,Object>> aliasToValueMapList= query.list();
		return 	aliasToValueMapList;
	}
	
	@Override
	public List<Map<String, Object>> getCompactAnalysisByProposalId(Integer proposalId, Integer start, Integer limit) {
		StringBuilder mySQLQuery = new StringBuilder(SQLQueryKeeper.getAnalysisCompactQueryByProposalId(start, limit));
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery.toString());
		query.setParameter("proposalId", proposalId);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		@SuppressWarnings("unchecked")   
		List<Map<String,Object>> aliasToValueMapList= query.list();
		return 	aliasToValueMapList;
	}
	
	@Override
	public  java.math.BigInteger getCountCompactAnalysisByExperimentId(Integer proposalId) {
		StringBuilder mySQLQuery = new StringBuilder(SQLQueryKeeper.getCountAnalysisCompactQueryByProposalId(proposalId));
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery.toString());
		query.setParameter("proposalId", proposalId);
		return 	( java.math.BigInteger)query.uniqueResult();
	}
	
	@Override
	public List<Map<String, Object>> getCompactAnalysisBySubtractionId(String subtractionId) {
		StringBuilder mySQLQuery = new StringBuilder(SQLQueryKeeper.getAnalysisCompactQueryBySubtractionId());
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery.toString());
		query.setParameter("subtractionId", subtractionId);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		@SuppressWarnings("unchecked")   
		List<Map<String,Object>> aliasToValueMapList= query.list();
		return 	aliasToValueMapList;
	}
	
	
	
	@Override
	public List<Map<String, Object>> getAllAnalysisInformation() {
		return this.getAll(SQLQueryKeeper.getAnalysisQuery());
	}

	@Override
	public List<Map<String, Object>> getCompactAnalysisByMacromoleculeId(Integer proposalId, Integer macromoleculeId) {
		StringBuilder mySQLQuery = new StringBuilder(SQLQueryKeeper.getAnalysisCompactQueryByMacromoleculeId());
		System.out.println(mySQLQuery.toString());
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery.toString());
		query.setParameter("proposalId", proposalId);
		query.setParameter("macromoleculeId", macromoleculeId);
		
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		@SuppressWarnings("unchecked")   
		List<Map<String,Object>> aliasToValueMapList= query.list();
		return 	aliasToValueMapList;
	}
	
	@Override
	public SaxsDataCollection3VO getDataCollection(int dataCollectionId) {
		StringBuilder ejbQLQuery = new StringBuilder();
		ejbQLQuery.append("SELECT DISTINCT(datacollection) FROM SaxsDataCollection3VO  datacollection ");
		ejbQLQuery.append(" LEFT JOIN FETCH datacollection.measurementtodatacollection3VOs measurementtodatacollection3VOs ");
		ejbQLQuery.append(" LEFT JOIN FETCH datacollection.substraction3VOs substraction3VOs ");
		ejbQLQuery.append(" LEFT JOIN FETCH substraction3VOs.sampleOneDimensionalFiles sampleOneDimensionalFiles ");
		ejbQLQuery.append(" LEFT JOIN FETCH sampleOneDimensionalFiles.frametolist3VOs sampleFrametolist3VOs ");
		ejbQLQuery.append(" LEFT JOIN FETCH substraction3VOs.bufferOneDimensionalFiles bufferOneDimensionalFiles ");
		ejbQLQuery.append(" LEFT JOIN FETCH bufferOneDimensionalFiles.frametolist3VOs bufferFrametolist3VOs ");
		ejbQLQuery.append(" WHERE datacollection.dataCollectionId = :dataCollectionId");
		TypedQuery<SaxsDataCollection3VO> query = entityManager.createQuery(ejbQLQuery.toString(), SaxsDataCollection3VO.class).setParameter("dataCollectionId", dataCollectionId);
		return query.getSingleResult();
	}
	
	@Override
	public List<SaxsDataCollection3VO> getDataCollections(List<Integer> dataCollectionIdList) {
		 List<SaxsDataCollection3VO> result = new ArrayList<SaxsDataCollection3VO>();
		 for (Integer id : dataCollectionIdList) {
			result.add(this.getDataCollection(id));
		}
		 return result;
	}
}
