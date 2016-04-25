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

package ispyb.server.mx.services.ws.rest.AutoProcessingIntegration;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;


@Stateless
public class AutoProcessingIntegrationServiceBean implements AutoProcessingIntegrationService, AutoProcessingIntegrationServiceLocal {
	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	
	private String ByDataCollectionId = getViewTableQuery() + " where v_datacollection_summary_phasing_dataCollectionId = :dataCollectionId and v_datacollection_summary_session_proposalId=:proposalId";

	private String getViewTableQuery(){
		return "select *,\n" + 
				" (  \n" + 
				"select GROUP_CONCAT(phasingStepType) from PhasingStep   \n" + 
				"where  v_datacollection_summary_phasing_autoProcScalingId = PhasingStep.autoProcScalingId  \n" + 
				") as phasingStepType,\n" + 
				" (  \n" + 
				"select GROUP_CONCAT(spaceGroupId) from PhasingStep   \n" + 
				"where  v_datacollection_summary_phasing_autoProcScalingId = PhasingStep.autoProcScalingId  \n" + 
				") as spaceGroupIds,\n" + 
				"(  \n" + 
				"select GROUP_CONCAT(spaceGroupShortName) from SpaceGroup   \n" + 
				"where  spaceGroupId in\n" + 
				"(select spaceGroupId from PhasingStep   \n" + 
				"where  v_datacollection_summary_phasing_autoProcScalingId = PhasingStep.autoProcScalingId   ) \n" + 
				") as spaceGroupShortName,\n" + 
				"(  \n" + 
				"select GROUP_CONCAT(PhasingPrograms) from PhasingProgramRun  \n" + 
				"where phasingProgramRunId  in\n" + 
				"(select programRunId from PhasingStep where  v_datacollection_summary_phasing_autoProcScalingId = PhasingStep.autoProcScalingId ) \n" + 
				") as PhasingPrograms,\n" + 
				" (  \n" + 
				"select GROUP_CONCAT(scalingStatisticsType) from AutoProcScalingStatistics   \n" + 
				"where v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId  \n" + 
				") as scalingStatisticsType,  \n" + 
				"  \n" + 
				"(  \n" + 
				"select GROUP_CONCAT(resolutionLimitLow) from AutoProcScalingStatistics   \n" + 
				"where v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId  \n" + 
				") as resolutionLimitLow,  \n" + 
				"  \n" + 
				"(  \n" + 
				"select GROUP_CONCAT(resolutionLimitHigh) from AutoProcScalingStatistics   \n" + 
				"where v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId  \n" + 
				") as resolutionLimitHigh,  \n" + 
				"  \n" + 
				"(  \n" + 
				"select GROUP_CONCAT(completeness) from AutoProcScalingStatistics   \n" + 
				"where v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId  \n" + 
				") as completeness,  \n" + 
				"  \n" + 
				"(  \n" + 
				"select GROUP_CONCAT(multiplicity) from AutoProcScalingStatistics   \n" + 
				"where v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId  \n" + 
				") as multiplicity,  \n" + 
				"  \n" + 
				"(  \n" + 
				"select GROUP_CONCAT(meanIOverSigI) from AutoProcScalingStatistics   \n" + 
				"where v_datacollection_summary_phasing_autoProcScalingId = AutoProcScalingStatistics.autoProcScalingId  \n" + 
				") as meanIOverSigI "
				+ " from v_datacollection_summary_phasing ";
	}
	
	@Override
	public List<Map<String, Object>> getViewByDataCollectionId(int proposalId, int dataCollectionId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(ByDataCollectionId);
		query.setParameter("dataCollectionId", dataCollectionId);
		query.setParameter("proposalId", proposalId);
		return executeSQLQuery(query);
	}
	
	private List<Map<String, Object>> executeSQLQuery(SQLQuery query ){
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		List<Map<String, Object>> aliasToValueMapList = query.list();
		return aliasToValueMapList;
	}
}
