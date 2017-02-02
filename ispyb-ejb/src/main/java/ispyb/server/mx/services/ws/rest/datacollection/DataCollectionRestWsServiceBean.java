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

package ispyb.server.mx.services.ws.rest.datacollection;

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
public class DataCollectionRestWsServiceBean extends WsServiceBean implements DataCollectionRestWsService, DataCollectionRestWsServiceLocal {
	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	private String getViewTableQuery(){
		return this.getQueryFromResourceFile("/queries/DataCollectionRestWsServiceBean/getViewTableQuery.sql");
	}
	
	@Override
	public Collection<? extends Map<String, Object>> getViewDataCollectionsByWorkflowId(int proposalId, Integer workflowId) {
		String mySQLQuery = this.getViewTableQuery() + " where proposalId = :proposalId and workflowId = :workflowId  group by v_datacollection.dataCollectionId";
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("proposalId", proposalId);
		query.setParameter("workflowId", workflowId);
		return executeSQLQuery(query);
	}
	

	@Override
	public Collection<? extends Map<String, Object>> getDataCollectionByDataCollectionGroupId(int proposalId, Integer dataCollectionGroupId) {
		String mySQLQuery = this.getViewTableQuery() + " where proposalId = :proposalId and dataCollectionGroupId = :dataCollectionGroupId  group by v_datacollection.dataCollectionId";
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("proposalId", proposalId);
		query.setParameter("dataCollectionGroupId", dataCollectionGroupId);
		return executeSQLQuery(query);
	}
	
}
