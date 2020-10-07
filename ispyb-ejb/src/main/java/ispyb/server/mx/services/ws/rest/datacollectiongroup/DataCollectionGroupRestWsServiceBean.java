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

package ispyb.server.mx.services.ws.rest.datacollectiongroup;

import ispyb.server.mx.services.ws.rest.WsServiceBean;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;


@Stateless
public class DataCollectionGroupRestWsServiceBean extends WsServiceBean implements DataCollectionGroupRestWsService, DataCollectionGroupRestWsServiceLocal  {
	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;


	private String getViewTableQuery(){
		return this.getQueryFromResourceFile("/queries/DataCollectionGroupRestWsServiceBean/getViewTableQuery.sql");
	}
	
	@Override
	public List<Map<String, Object>> getViewDataCollectionBySessionId(int proposalId, int sessionId) {
		String mySQLQuery = getViewTableQuery() + " where DataCollectionGroup_sessionId = :sessionId and BLSession_proposalId = :proposalId ";
		mySQLQuery = mySQLQuery + " group by v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId order by DataCollection_startTime desc ";
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("sessionId", sessionId);
		query.setParameter("proposalId", proposalId);
		return executeSQLQuery(query);
	}
	
	@Override
	public List<Map<String, Object>> getViewDataCollectionBySessionIdHavingImages(int proposalId, int sessionId) {
		String mySQLQuery = getViewTableQuery() + " where DataCollectionGroup_sessionId = :sessionId and BLSession_proposalId = :proposalId ";
		mySQLQuery = mySQLQuery + " and DataCollection_numberOfImages is not null ";
		mySQLQuery = mySQLQuery + " group by v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId order by DataCollection_startTime desc ";
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("sessionId", sessionId);
		query.setParameter("proposalId", proposalId);
		return executeSQLQuery(query);
	}
	
	@Override
	public List<Map<String, Object>> getViewDataCollectionByProteinAcronym(int proposalId, String proteinAcronym) {
		String mySQLQuery = getViewTableQuery() + " where BLSession_proposalId = :proposalId and Protein_acronym = :proteinAcronym";
		mySQLQuery = mySQLQuery + " group by v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId, v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId";
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("proposalId", proposalId);
		query.setParameter("proteinAcronym", proteinAcronym);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		return executeSQLQuery(query);
	}

	@Override
	public List<Map<String, Object>> getViewDataCollectionBySampleId(int proposalId, int sampleId) {
		String mySQLQuery = getViewTableQuery() + " where BLSession_proposalId = :proposalId and DataCollectionGroup_blSampleId = :sampleId";
		mySQLQuery = mySQLQuery + " group by v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId, v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId";
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("proposalId", proposalId);
		query.setParameter("sampleId", sampleId);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		return executeSQLQuery(query);
	}

	@Override
	public List<Map<String, Object>> getViewDataCollectionBySampleName(int proposalId, String name) {
		String mySQLQuery = getViewTableQuery() + " where BLSession_proposalId = :proposalId and BLSample_name = :name";
		mySQLQuery = mySQLQuery + " group by v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId, v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId";
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("proposalId", proposalId);
		query.setParameter("name", name);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		return executeSQLQuery(query);
	}
	
	@Override
	public List<Map<String, Object>> getViewDataCollectionByImagePrefix(int proposalId, String prefix) {
		String mySQLQuery = getViewTableQuery() + " where BLSession_proposalId = :proposalId and DataCollection_imagePrefix = :prefix";
		mySQLQuery = mySQLQuery + " group by v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId, v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId";
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("proposalId", proposalId);
		query.setParameter("prefix", prefix);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		return executeSQLQuery(query);
	}

	@Override
	public Collection<? extends Map<String, Object>> getViewDataCollectionByDataCollectionId(int proposalId, int dataCollectionId) {
		String mySQLQuery = getViewTableQuery() + " where DataCollection_dataCollectionId = :dataCollectionId and BLSession_proposalId = :proposalId ";
		mySQLQuery = mySQLQuery + " group by v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId, v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId";
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("dataCollectionId", dataCollectionId);
		query.setParameter("proposalId", proposalId);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		return executeSQLQuery(query);
	}

	@Override
	public List<Map<String, Object>> getViewDataCollectionByWorkflowId(Integer proposalId, Integer workflowId) {
		String mySQLQuery = getViewTableQuery() + " where Workflow_workflowId = :Workflow_workflowId and BLSession_proposalId = :proposalId ";
		mySQLQuery = mySQLQuery + " group by v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId, v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId";
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("Workflow_workflowId", workflowId);
		query.setParameter("proposalId", proposalId);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		return executeSQLQuery(query);
	}

	
	

	
}
