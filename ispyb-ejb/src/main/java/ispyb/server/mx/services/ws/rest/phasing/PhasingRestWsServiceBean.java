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

import ispyb.server.mx.services.ws.rest.WsServiceBean;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;


@Stateless
public class PhasingRestWsServiceBean  extends WsServiceBean implements PhasingRestWsService, PhasingRestWsServiceLocal {
	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	private String getPhasingViewTableQuery(){
		return this.getQueryFromResourceFile("/queries/PhasingRestWsServiceBean/getViewTableQuery.sql");
	}
	
	private String ByDataCollectionGroupId = getPhasingViewTableQuery() + " where DataCollection_dataCollectionGroupId = :dataCollectionGroupId and BLSession_proposalId = :proposalId group by PhasingStep_phasingStepId";
	private String ByDataCollectionId = getPhasingViewTableQuery() + " where DataCollection_dataCollectionId = :dataCollectionId and BLSession_proposalId = :proposalId group by PhasingStep_phasingStepId";
	private String ByAutoProcIntegrationId = getPhasingViewTableQuery() + " where AutoProcIntegration_autoProcIntegrationId = :autoprocIntegrationId and BLSession_proposalId = :proposalId group by PhasingStep_phasingStepId";
	private String ByPhasingStepId = getPhasingViewTableQuery() + " where PhasingStep_phasingStepId = :phasingStepId and BLSession_proposalId = :proposalId group by PhasingStep_phasingStepId";
	
	
	private String BySampleId = getPhasingViewTableQuery()  + " where BLSample_blSampleId = :blSampleId and BLSession_proposalId = :proposalId group by PhasingStep_phasingStepId";
	private String ByProteinId = getPhasingViewTableQuery() + " where Protein_proteinId = :Protein_proteinId and BLSession_proposalId = :proposalId group by PhasingStep_phasingStepId";
	private String BySessionId = getPhasingViewTableQuery() + " where BLSession_sessionId = :sessionId and BLSession_proposalId = :proposalId group by PhasingStep_phasingStepId";
	

	@Override
	public List<Map<String, Object>> getPhasingViewByDataCollectionGroupId(int dataCollectionGroupId, int proposalId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(ByDataCollectionGroupId);
		query.setParameter("dataCollectionGroupId", dataCollectionGroupId);
		query.setParameter("proposalId", proposalId);
		System.out.println(query.getQueryString());
		return executeSQLQuery(query);
	}
	
	@Override
	public List<Map<String, Object>> getPhasingViewByDataCollectionId(int dataCollectionId, int proposalId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(ByDataCollectionId);
		query.setParameter("dataCollectionId", dataCollectionId);
		query.setParameter("proposalId", proposalId);
		System.out.println(query.getQueryString());
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
	


}
