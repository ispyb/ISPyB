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

package ispyb.server.ws.mx.phasing;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;


@Stateless
public class PhasingServiceBean implements PhasingService, PhasingServiceLocal {
	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;


	private String getPhasingViewTableQuery(){
		return "select *,"
				+ "(\n" + 
				"select GROUP_CONCAT(scalingStatisticsType) from AutoProcScalingStatistics \n" + 
				"where v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId\n" + 
				") as scalingStatisticsType,\n" + 
				"\n" + 
				"(\n" + 
				"select GROUP_CONCAT(resolutionLimitLow) from AutoProcScalingStatistics \n" + 
				"where v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId\n" + 
				") as resolutionLimitLow,\n" + 
				"\n" + 
				"(\n" + 
				"select GROUP_CONCAT(resolutionLimitHigh) from AutoProcScalingStatistics \n" + 
				"where v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId\n" + 
				") as resolutionLimitHigh,\n" + 
				"\n" + 
				"(\n" + 
				"select GROUP_CONCAT(completeness) from AutoProcScalingStatistics \n" + 
				"where v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId\n" + 
				") as completeness,\n" + 
				"\n" + 
				"(\n" + 
				"select GROUP_CONCAT(multiplicity) from AutoProcScalingStatistics \n" + 
				"where v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId\n" + 
				") as multiplicity,\n" + 
				"\n" + 
				"(\n" + 
				"select GROUP_CONCAT(meanIOverSigI) from AutoProcScalingStatistics \n" + 
				"where v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId\n" + 
				") as meanIOverSigI"
				+ " from v_datacollection_summary_phasing ";
	}
	
	@Override
	public List<Map<String, Object>> getPhasingViewByDataCollectionId(int dataCollectionId) {
		String mySQLQuery = getPhasingViewTableQuery() + " where v_datacollection_summary_phasing_dataCollectionId = :dataCollectionId";
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("dataCollectionId", dataCollectionId);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> aliasToValueMapList = query.list();
		return aliasToValueMapList;
	}
	
}
