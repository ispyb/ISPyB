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

package ispyb.server.mx.services.ws.rest.energyscan;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;


@Stateless
public class EnergyScanRestWsServiceBean implements EnergyScanRestWsService, EnergyScanRestWsServiceLocal {
	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;


	private String BySessionId = "SELECT * FROM v_energyScan WHERE sessionId = :sessionId and BLSession_proposalId=:proposalId";
	private String ById = "SELECT * FROM v_energyScan WHERE energyScanId = :energyScanId and BLSession_proposalId=:proposalId";
	
	@Override
	public List<Map<String, Object>> getViewBySessionId(int proposalId, int sessionId) {

		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(BySessionId);
		
		query.setParameter("proposalId", proposalId);
		query.setParameter("sessionId", sessionId);
		
		return executeSQLQuery(query);
	}
	
	private List<Map<String, Object>> executeSQLQuery(SQLQuery query ){
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		List<Map<String, Object>> aliasToValueMapList = query.list();
		return aliasToValueMapList;
	}

	@Override
	public List<Map<String, Object>> getViewById(int proposalId, int energyScanId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(ById);
		
		query.setParameter("proposalId", proposalId);
		query.setParameter("energyScanId", energyScanId);
		
		return executeSQLQuery(query);
	}
}
