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

package ispyb.server.mx.services.collections.workflowStep;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import ispyb.server.biosaxs.services.core.measurement.Measurement3ServiceLocal;
import ispyb.server.mx.services.ws.rest.datacollectiongroup.DataCollectionGroupRestWsServiceLocal;
import ispyb.server.mx.vos.collections.Workflow3VO;
import ispyb.server.mx.vos.collections.WorkflowStep3VO;


@Stateless
public class WorkflowStep3ServiceBean implements WorkflowStep3Service, WorkflowStep3ServiceLocal {
	
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;
	
	@EJB
	private DataCollectionGroupRestWsServiceLocal dataCollectionRestWsServiceLocal;
	
	@Override
	public WorkflowStep3VO merge(WorkflowStep3VO workflowStep3VO) {
		try {
			WorkflowStep3VO newVO = entityManager.merge(workflowStep3VO);
			return newVO;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	

	@Override
	public WorkflowStep3VO findById(Integer workflowStepId, Integer proposalId) throws Exception {
		try {

			if (proposalId != null){
				WorkflowStep3VO wfs =  entityManager.find(WorkflowStep3VO.class, workflowStepId);
				/** Check that this workflowStepId belongs to such proposal **/
				List<Map<String, Object>> datacollections = dataCollectionRestWsServiceLocal.getViewDataCollectionByWorkflowId(proposalId, wfs.getWorkflowId());
				if (datacollections.size() > 0){
					return wfs;
				}
				else{
					throw new Exception("Workflow step " + workflowStepId +" does not correspond to proposalId " + proposalId);
				}
			}
		} catch (Exception re) {
			throw re;
		}
		return null;
		
	}


}
