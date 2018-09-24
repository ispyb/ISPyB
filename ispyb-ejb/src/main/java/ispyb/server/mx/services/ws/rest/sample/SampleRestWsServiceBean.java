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

package ispyb.server.mx.services.ws.rest.sample;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;


@Stateless
public class SampleRestWsServiceBean implements SampleRestWsService, SampleRestWsServiceLocal {
	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	
	private String ByProposalId = getDewarViewTableQuery() + " where v_mx_sample.Protein_proposalId = :proposalId";
	private String BySessionId = getDewarViewTableQuery() + " where v_mx_sample.DataCollectionGroup_sessionId = :sessionId and v_mx_sample.Protein_proposalId = :proposalId";
	private String ByContainerId = getDewarViewTableQuery() + " where v_mx_sample.Container_containerId = :containerId and v_mx_sample.Protein_proposalId = :proposalId";
	private String ByShippingId = getDewarViewTableQuery() + " where v_mx_sample.Shipping_shippingId = :shipingId and v_mx_sample.Protein_proposalId = :proposalId";
	private String ByDewarId = getDewarViewTableQuery() + " where v_mx_sample.Dewar_dewarId = :dewarId and v_mx_sample.Protein_proposalId = :proposalId";
	
	private String getDataCollectionIdQuery(){
		return  "  (SELECT \n" + 
				"            MAX(`DataCollectionGroup`.`dataCollectionGroupId`)\n" + 
				"        FROM\n" + 
				"            `DataCollectionGroup`\n" + 
				"        WHERE\n" + 
				"            (`DataCollectionGroup`.`blSampleId` = `v_mx_sample`.`BLSample_blSampleId`)) AS `DataCollectionGroup_dataCollectionGroupId`";
	}
  
            
	private String getDewarViewTableQuery(){
		return "select *, " + getDataCollectionIdQuery() + " from v_mx_sample";
	}

	
	
	private List<Map<String, Object>> executeSQLQuery(SQLQuery query ){
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		List<Map<String, Object>> aliasToValueMapList = query.list();
		return aliasToValueMapList;
	}


	@Override
	public List<Map<String, Object>> getSamplesByProposalId(int proposalId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(ByProposalId);
		query.setParameter("proposalId", proposalId);
		return executeSQLQuery(query);
	}
	
	@Override
	public List<Map<String, Object>> getSamplesBySessionId(int proposalId,int sessionId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(BySessionId);
		query.setParameter("sessionId", sessionId);
		query.setParameter("proposalId", proposalId);
		return executeSQLQuery(query);
	}
	
	@Override
	public List<Map<String, Object>> getSamplesByContainerId(int proposalId,int containerId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(ByContainerId);
		query.setParameter("containerId", containerId);
		query.setParameter("proposalId", proposalId);
		return executeSQLQuery(query);
	}

	@Override
	public List<Map<String, Object>> getSamplesByShipmentId(int proposalId,int shipingId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(ByShippingId);
		
		query.setParameter("shipingId", shipingId);
		query.setParameter("proposalId", proposalId);
		return executeSQLQuery(query);
	}
	
	@Override
	public List<Map<String, Object>> getSamplesByDewarId(int proposalId, int dewarId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(ByDewarId);
		query.setParameter("dewarId", dewarId);
		query.setParameter("proposalId", proposalId);
		return executeSQLQuery(query);
	}

}
