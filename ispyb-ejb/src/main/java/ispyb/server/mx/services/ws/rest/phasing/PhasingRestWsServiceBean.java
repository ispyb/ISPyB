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

package ispyb.server.mx.services.ws.rest.phasing;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;


@Stateless
public class PhasingRestWsServiceBean implements PhasingRestWsService, PhasingRestWsServiceLocal {
	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	
	private String ByDataCollectionId = getPhasingViewTableQuery() + " where dataCollectionId = :dataCollectionId and proposalId = :proposalId";
	private String ByAutoProcIntegrationId = getPhasingViewTableQuery() + " where autoprocIntegrationId = :autoprocIntegrationId and proposalId = :proposalId";
	private String ByPhasingStepId = getPhasingViewTableQuery() + " where phasingStepId = :phasingStepId and proposalId = :proposalId";
	
	
	private String BySampleId = getPhasingViewTableQuery()  + " where blSampleId = :blSampleId and proposalId = :proposalId";
	private String ByProteinId = getPhasingViewTableQuery() + " where proteinId = :proteinId and proposalId = :proposalId";
	private String BySessionId = getPhasingViewTableQuery() + " where sessionId = :sessionId and proposalId = :proposalId";
	
	private String getPhasingViewTableQuery(){
		return "select *,\n" + 
				"(select GROUP_CONCAT(metric) from PhasingStatistics where phasingStepId = v_datacollection_phasing.phasingStepId) as metric,\n" + 
				"(select GROUP_CONCAT(statisticsValue) from PhasingStatistics where phasingStepId = v_datacollection_phasing.phasingStepId) as statisticsValue\n" + 
				//"(select GROUP_CONCAT(binNumber) from PhasingStatistics where phasingStepId = v_datacollection_phasing.phasingStepId) as binNumber,\n" + 
				//"(select GROUP_CONCAT(nReflections) from PhasingStatistics where phasingStepId = v_datacollection_phasing.phasingStepId) as nReflections,\n" + 
				//"(select GROUP_CONCAT(numberOfBins) from PhasingStatistics where phasingStepId = v_datacollection_phasing.phasingStepId) as numberOfBins\n" + 
				"from v_datacollection_phasing";
	}

	@Override
	public List<Map<String, Object>> getPhasingViewByDataCollectionId(int dataCollectionId, int proposalId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(ByDataCollectionId);
		query.setParameter("dataCollectionId", dataCollectionId);
		query.setParameter("proposalId", proposalId);
		return executeSQLQuery(query);
	}
	
	@Override
	public List<Map<String, Object>> getPhasingViewByAutoProcIntegrationId(int autoprocIntegrationId, int proposalId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(ByAutoProcIntegrationId);
		query.setParameter("autoprocIntegrationId", autoprocIntegrationId);
		query.setParameter("proposalId", proposalId);
		return executeSQLQuery(query);
	}
	
	@Override
	public List<Map<String, Object>> getPhasingViewByBlSampleId(int blSampleId, int proposalId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(BySampleId);
		query.setParameter("blSampleId", blSampleId);
		query.setParameter("proposalId", proposalId);
		return executeSQLQuery(query);
	}
	
	@Override
	public List<Map<String, Object>> getPhasingViewByProteinId(int proteinId,
			int proposalId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(ByProteinId);
		query.setParameter("proteinId", proteinId);
		query.setParameter("proposalId", proposalId);
		return executeSQLQuery(query);
	}

	@Override
	public List<Map<String, Object>> getPhasingViewBySessionId(int sessionId, int proposalId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(BySessionId);
		query.setParameter("sessionId", sessionId);
		query.setParameter("proposalId", proposalId);
		return executeSQLQuery(query);
	}
	
	@Override
	public List<Map<String, Object>> getPhasingViewByStepId(int phasingStepId, int proposalId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(ByPhasingStepId);
		query.setParameter("phasingStepId", phasingStepId);
		query.setParameter("proposalId", proposalId);
		return executeSQLQuery(query);
	}
	
	
	
	
	
	
	
	private String getPhasingFilesViewTableQuery(){
		return "select * from v_datacollection_phasing_program_run ";
	}
	
	private String FilesByPhasingStepId = getPhasingFilesViewTableQuery() + " where phasingStepId = :phasingStepId and proposalId = :proposalId";
	
	
	private String FilesByPhasingProgramAttachmentId = getPhasingFilesViewTableQuery() + " where phasingProgramAttachmentId = :phasingProgramAttachmentId and proposalId = :proposalId";
	
	@Override
	public List<Map<String, Object>> getPhasingFilesViewByStepId(int phasingStepId, int proposalId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(FilesByPhasingStepId);
		query.setParameter("phasingStepId", phasingStepId);
		query.setParameter("proposalId", proposalId);
		return executeSQLQuery(query);
	}
	
	@Override
	public List<Map<String, Object>> getPhasingFilesViewByPhasingProgramAttachmentId(int phasingProgramAttachmentId, int proposalId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(FilesByPhasingProgramAttachmentId);
		System.out.println("SQL ----------------");
		System.out.println(FilesByPhasingProgramAttachmentId);
		System.out.println(String.valueOf(phasingProgramAttachmentId));
		query.setParameter("phasingProgramAttachmentId", phasingProgramAttachmentId);
		query.setParameter("proposalId", proposalId);
		return executeSQLQuery(query);
	}
	
	
	private List<Map<String, Object>> executeSQLQuery(SQLQuery query ){
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		List<Map<String, Object>> aliasToValueMapList = query.list();
		return aliasToValueMapList;
	}


}
