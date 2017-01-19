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

package ispyb.server.biosaxs.services.stats;


import ispyb.server.mx.services.ws.rest.WsServiceBean;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.Session;



@Stateless
public class Stats3ServiceBean extends WsServiceBean implements Stats3Service, Stats3ServiceLocal {
	
	private String GET_EXPERIMENT_COUNT_BY_DATE = " select count(*) from Experiment "
			+ "										where experimentType = :TYPE and creationDate > :START and creationDate < :END";
	
	private String GET_FRAMES_COUNT_BY_DATE = " select count(*) from Frame "
			+ "										where creationDate > :START and creationDate < :END";
	
	
	private String GET_SAMPLES_COUNT_BY_DATE = "select count(*) from Specimen spe, Experiment exp where exp.experimentId = spe.experimentId and exp.creationDate > :START and exp.creationDate < :END";
	
	private String GET_SESSIONS = "select count(distinct(sessionId)) from Experiment where  creationDate > :START and creationDate < :END";
	
	
	private String AUTOPROCSTATS_QUERY = "select * from v_mx_autoprocessing_stats where startTime >= START and startTime <= :END and scalingStatisticsType = :TYPE";
	
	
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;


	
	
	
	@Override
	public List getSamplesBy(String start, String end) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(GET_SAMPLES_COUNT_BY_DATE);
		query.setParameter("START", start);
		query.setParameter("END", end);
		return 	query.list();
	}
	
	@Override
	public List getSessionsBy(String start, String end) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(GET_SESSIONS);
		query.setParameter("START", start);
		query.setParameter("END", end);
		return 	query.list();
	}
	
	@Override
	public List getExperimentsBy(String type, String start, String end) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(GET_EXPERIMENT_COUNT_BY_DATE);
		query.setParameter("TYPE", type);
		query.setParameter("START", start);
		query.setParameter("END", end);
		return 	query.list();
	}
	
	@Override
	public List getFramesBy( String start, String end) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(GET_FRAMES_COUNT_BY_DATE);
		query.setParameter("START", start);
		query.setParameter("END", end);
		return 	query.list();
	}
		
	@Override
	public List<Map<String, Object>> getAutoprocStatsByDate(String autoprocStatisticsType, Date startDate, Date endDate) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(AUTOPROCSTATS_QUERY);
		
		query.setParameter("START", startDate);
		query.setParameter("END", endDate);
		query.setParameter("TYPE", autoprocStatisticsType);
		return executeSQLQuery(query);
	}
}
