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

package ispyb.server.common.services.ws.rest.session;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;


@Stateless
public class SessionServiceBean implements SessionService, SessionServiceLocal {

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	
	/** SQL QUERIES **/
	private  String BySessionId = getSessionViewTable() + " where v_session.sessionId = :sessionId and proposalId = :proposalId order by v_session.sessionId DESC"; 
	private  String ByProposalId = getSessionViewTable() + " where v_session.proposalId = :proposalId order by v_session.sessionId DESC";
	private  String ByDates = getSessionViewTable() + " where (BLSession_startDate >= :startDate and BLSession_startDate <= :endDate) or (BLSession_endDate >= :startDate and BLSession_endDate <= :endDate) order by v_session.sessionId DESC";
	private  String ByDatesBeamline = getSessionViewTable() + " where ((BLSession_startDate >= :startDate and BLSession_startDate <= :endDate) or (BLSession_endDate >= :startDate and BLSession_endDate <= :endDate)) and beamlineName = :beamlineName order by v_session.sessionId DESC";

	/**
	 * Query from the view v_session
	 * @return
	 */
	private  String getSessionViewTable(){
		return "select *,\n" + 
				"(select count(*) from EnergyScan where EnergyScan.sessionId = v_session.sessionId) as energyScanCount,\n"
				+ " (select count(distinct(blSampleId)) from DataCollectionGroup where DataCollectionGroup.sessionId = v_session.sessionId) as sampleCount,"
				+ " (select sum(DataCollection.numberOfImages) from DataCollectionGroup, DataCollection where DataCollectionGroup.sessionId = v_session.sessionId and DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId) as imagesCount,"
				+ "(select count(*) from DataCollectionGroup, DataCollection where DataCollectionGroup.sessionId = v_session.sessionId and DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId and DataCollection.numberOfImages < 5) as testDataCollectionGroupCount,"
				+ " (select count(*) from DataCollectionGroup, DataCollection where DataCollectionGroup.sessionId = v_session.sessionId and DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId and DataCollection.numberOfImages > 4) as dataCollectionGroupCount," + 
				"(select count(*) from XFEFluorescenceSpectrum where XFEFluorescenceSpectrum.sessionId = v_session.sessionId) as xrfSpectrumCount,\n" + 
				"(select experimentType from DataCollectionGroup where DataCollectionGroup.dataCollectionGroupId = (select max(dataCollectionGroupId) from DataCollectionGroup dg2 where  dg2.sessionId = v_session.sessionId))  as lastExperimentDataCollectionGroup,\n" + 
				"(select endTime from DataCollectionGroup where DataCollectionGroup.dataCollectionGroupId = (select max(dataCollectionGroupId) from DataCollectionGroup dg2 where  dg2.sessionId = v_session.sessionId))  as lastEndTimeDataCollectionGroup\n" + 
				"from v_session";
	}
	
	@Override
	public List<Map<String, Object>> getSessionViewBySessionId(int proposalId, int sessionId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(BySessionId);
		
		/** Setting the parameters **/
		query.setParameter("sessionId", sessionId);
		query.setParameter("proposalId", proposalId);
		
		return executeSQLQuery(query);
	}
	
	
	@Override
	public List<Map<String, Object>> getSessionViewByProposalId(int proposalId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(ByProposalId);
		
		/** Setting the parameters **/
		query.setParameter("proposalId", proposalId);
		return executeSQLQuery(query);
	}
	
	@Override
	public List<Map<String, Object>> getSessionViewByDates(String startDate, String endDate) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(ByDates);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		
		return executeSQLQuery(query);
	}
	
	@Override
	public List<Map<String, Object>> getSessionViewByDates(Date startDate, Date endDate, String beamlineName) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(ByDatesBeamline);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("beamlineName", beamlineName);
		
		return executeSQLQuery(query);
	}
	
	private List<Map<String, Object>> executeSQLQuery(SQLQuery query ){
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		List<Map<String, Object>> aliasToValueMapList = query.list();
		return aliasToValueMapList;
	}
}
