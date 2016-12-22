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

package ispyb.server.mx.services.ws.rest.autoprocessingintegration;

import ispyb.server.mx.services.ws.rest.WsServiceBean;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.Session;


@Stateless
public class AutoProcessingIntegrationServiceBean extends WsServiceBean implements AutoProcessingIntegrationService, AutoProcessingIntegrationServiceLocal {
	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	
	private String ByDataCollectionId = getViewTableQuery() + " where v_datacollection_summary_phasing_dataCollectionId = :dataCollectionId and v_datacollection_summary_session_proposalId=:proposalId";

	private String getViewTableQuery(){
		return this.getQueryFromResourceFile("/queries/AutoProcessingIntegrationServiceBean/getViewTableQuery.sql");
	}
	
	@Override
	public List<Map<String, Object>> getViewByDataCollectionId(int proposalId, int dataCollectionId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(ByDataCollectionId);
		query.setParameter("dataCollectionId", dataCollectionId);
		query.setParameter("proposalId", proposalId);
		System.out.println(query.toString());
		return executeSQLQuery(query);
	}
	
}
