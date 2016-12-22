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

package ispyb.server.biosaxs.services.ws.rest.datacollection;

import ispyb.server.mx.services.ws.rest.WsServiceBean;

import java.util.Collection;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.Session;


@Stateless
public class SaxsDataCollectionRestWsServiceBean extends WsServiceBean implements SaxsDataCollectionRestWsService, SaxsDataCollectionRestWsServiceLocal {
	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	private String ByExperimentId = this.getViewTableQuery() + " where Experiment_proposalId = :proposalId and Experiment_experimentId = :experimentId group by MeasurementToDataCollection_measurementToDataCollectionId order by Measurement_measurementId DESC;";
	
	private String BySessionId = this.getViewTableQuery() + " where Experiment_proposalId = :proposalId and Experiment_sessionId = :sessionId group by MeasurementToDataCollection_measurementToDataCollectionId order by Measurement_measurementId DESC;";
	
	private String ByDataCollectionId = this.getViewTableQuery() + " where Experiment_proposalId = :proposalId and MeasurementToDataCollection_dataCollectionId = :dataCollectionId group by MeasurementToDataCollection_measurementToDataCollectionId order by Measurement_measurementId DESC;";
	
	private String ByMacromoleculeId = this.getViewTableQuery() + "  where Experiment_proposalId = :proposalId and MeasurementToDataCollection_dataCollectionId IN (select MeasurementToDataCollection_dataCollectionId from v_saxs_datacollection where Macromolecule_macromoleculeId =:macromoleculeId) group by MeasurementToDataCollection_measurementToDataCollectionId order by Measurement_measurementId DESC;";
	
	
	
	private String getViewTableQuery(){
		return this.getQueryFromResourceFile("/queries/biosaxs/DataCollectionRestWsServiceBean/getViewTableQuery.sql");
	}
	
	@Override
	public Collection<? extends Map<String, Object>> getDataCollectionByExperimentId(int proposalId, Integer experimentId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(ByExperimentId);
		query.setParameter("proposalId", proposalId);
		query.setParameter("experimentId", experimentId);
		return executeSQLQuery(query);
	}
	
	@Override
	public Collection<? extends Map<String, Object>> getDataCollectionBySessionId(int proposalId, Integer sessionId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(BySessionId);
		query.setParameter("proposalId", proposalId);
		query.setParameter("sessionId", sessionId);
		return executeSQLQuery(query);
	}
	
	@Override
	public Collection<? extends Map<String, Object>> getDataCollectionByMacromoleculeId(int proposalId, Integer macromoleculeId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(ByMacromoleculeId);
		query.setParameter("proposalId", proposalId);
		query.setParameter("macromoleculeId", macromoleculeId);
		System.out.println(query);
		return executeSQLQuery(query);
	}
	
	@Override
	public Collection<? extends Map<String, Object>> getDataCollectionByDataCollectionId(int proposalId, Integer dataCollectionId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(ByDataCollectionId);
		query.setParameter("proposalId", proposalId);
		query.setParameter("dataCollectionId", dataCollectionId);
		return executeSQLQuery(query);
	}
	

	
}
