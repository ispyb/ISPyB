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

	/** SQL common clauses **/
	private String dateClause = "((BLSession_startDate >= :startDate and BLSession_startDate <= :endDate) "
			+ "or "
			+ " (BLSession_endDate >= :startDate and BLSession_endDate <= :endDate)"
			+ "or "
			+ " (BLSession_endDate >= :endDate and BLSession_startDate <= :startDate)"
			+ "or "
			+ " (BLSession_endDate <= :endDate and BLSession_startDate >= :startDate))";
	                            
	/** SQL QUERIES **/
	private  String BySessionId = getSessionViewTable() + " where v_session.sessionId = :sessionId and proposalId = :proposalId order by v_session.sessionId DESC"; 
	private  String ByProposalId = getSessionViewTable() + " where v_session.proposalId = :proposalId order by v_session.sessionId DESC";
	private  String ByDates = getSessionViewTable() + " where " + dateClause + " order by v_session.sessionId DESC";
	
	private  String ByDatesAndSiteId = getSessionViewTable() + " where " + dateClause + " and v_session.operatorSiteNumber=:siteId order by v_session.sessionId DESC";
	
	private  String ByProposalAndDates = getSessionViewTable() + " where v_session.proposalId = :proposalId and " + dateClause + " order by v_session.sessionId DESC";
	
	
	private  String ByBeamlineOperator = getSessionViewTable() + " where v_session.beamLineOperator LIKE :beamlineOperator order by v_session.sessionId DESC";
	

	/**
	 * Query from the view v_session
	 * @return
	 */
	private  String getSessionViewTable(){
		return "select *,\n" + 
				"(select count(*) from EnergyScan where EnergyScan.sessionId = v_session.sessionId) as energyScanCount,\n"
				+ " (select count(distinct(blSampleId)) from DataCollectionGroup where DataCollectionGroup.sessionId = v_session.sessionId) as sampleCount,"
				+ " (select sum(DataCollection.numberOfImages) from DataCollectionGroup, DataCollection where DataCollectionGroup.sessionId = v_session.sessionId and DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId) as imagesCount,"
				+ " (select count(*) from DataCollectionGroup, DataCollection where DataCollectionGroup.sessionId = v_session.sessionId and DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId and DataCollection.numberOfImages < 5) as testDataCollectionGroupCount,"
				+ " (select count(*) from DataCollectionGroup, DataCollection where DataCollectionGroup.sessionId = v_session.sessionId and DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId and DataCollection.numberOfImages > 4) as dataCollectionGroupCount," 
				+ " (select count(*) from XFEFluorescenceSpectrum where XFEFluorescenceSpectrum.sessionId = v_session.sessionId) as xrfSpectrumCount,\n"  
				+ " (select count(*) from Experiment exp1 where v_session.sessionId = exp1.sessionId and exp1.experimentType='HPLC') as hplcCount,"
				+ " (select count(*) from Experiment exp2 where v_session.sessionId = exp2.sessionId and exp2.experimentType='STATIC') as sampleChangerCount,"
				+ " (select count(*) from Experiment exp3 where v_session.sessionId = exp3.sessionId and exp3.experimentType='CALIBRATION') as calibrationCount,"
				+ " (select experimentType from DataCollectionGroup where DataCollectionGroup.dataCollectionGroupId = (select max(dataCollectionGroupId) from DataCollectionGroup dg2 where  dg2.sessionId = v_session.sessionId))  as lastExperimentDataCollectionGroup,\n"  
				+ " (select endTime from DataCollectionGroup where DataCollectionGroup.dataCollectionGroupId = (select max(dataCollectionGroupId) from DataCollectionGroup dg2 where  dg2.sessionId = v_session.sessionId))  as lastEndTimeDataCollectionGroup\n"  
				+ "from v_session";
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
		System.out.println(query.getQueryString());
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
	
	private List<Map<String, Object>> executeSQLQuery(SQLQuery query ){
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		List<Map<String, Object>> aliasToValueMapList = query.list();
		return aliasToValueMapList;
	}

	@Override
	public List<Map<String, Object>> getSessionViewByProposalAndDates(int proposalId, String startDate, String endDate) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(ByProposalAndDates);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("proposalId", proposalId);
		System.out.println(query.getQueryString());
		return executeSQLQuery(query);
	}

	@Override
	public List<Map<String, Object>> getSessionViewByBeamlineOperator( String beamlineOperator) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(ByBeamlineOperator);
		query.setParameter("beamlineOperator", "%" +  beamlineOperator + "%");
		System.out.println(query.getQueryString());
		return executeSQLQuery(query);
	}

	@Override
	public List<Map<String, Object>> getSessionViewByDates(String startDate, String endDate, String siteId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(ByDatesAndSiteId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("siteId", siteId);
		return executeSQLQuery(query);
	}

}