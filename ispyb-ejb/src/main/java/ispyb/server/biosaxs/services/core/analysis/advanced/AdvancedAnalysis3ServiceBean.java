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

package ispyb.server.biosaxs.services.core.analysis.advanced;


import ispyb.server.biosaxs.services.core.experiment.Experiment3ServiceBean;
import ispyb.server.biosaxs.vos.advanced.FitStructureToExperimentalData3VO;
import ispyb.server.biosaxs.vos.advanced.RigidBodyModeling3VO;
import ispyb.server.biosaxs.vos.advanced.Superposition3VO;
import ispyb.server.biosaxs.vos.assembly.Structure3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.mx.vos.collections.InputParameterWorkflow;
import ispyb.server.mx.vos.collections.Workflow3VO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;



@Stateless
public class AdvancedAnalysis3ServiceBean implements AdvancedAnalysis3Service, AdvancedAnalysis3ServiceLocal {
	
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	/** The now. */
	protected static Calendar NOW;

	private Date getNow(){
		 NOW = GregorianCalendar.getInstance();
		 return NOW.getTime();
	}

	@Override
	public FitStructureToExperimentalData3VO merge(FitStructureToExperimentalData3VO fitStructure) {
		fitStructure.setCreationDate(getNow());
		return this.entityManager.merge(fitStructure);
	}

	@Override
	public FitStructureToExperimentalData3VO getFitStructureToExperimentDatal(int fitStructureToExperimentalDataId) {
		return this.entityManager.find(FitStructureToExperimentalData3VO.class, fitStructureToExperimentalDataId);
	}

	@Override
	public Workflow3VO merge(Workflow3VO workflow, ArrayList<InputParameterWorkflow> inputs) {
		workflow = this.entityManager.merge(workflow);
		workflow.setRecordTimeStamp(getNow());
		for (InputParameterWorkflow inputParameterWorkflow : inputs) {
			inputParameterWorkflow.setWorkflowId(workflow.getWorkflowId());
			this.entityManager.merge(inputParameterWorkflow);
		}
		return workflow;
	}

	@Override
	public FitStructureToExperimentalData3VO getFitStructureToExperimentDatalByWorkflowId(int workflowId) {
		String ejbQLQuery ="SELECT f FROM FitStructureToExperimentalData f where f.workflowId = :workflowId";
		TypedQuery<FitStructureToExperimentalData3VO> query = entityManager.createQuery(ejbQLQuery.toString(), FitStructureToExperimentalData3VO.class).setParameter("workflowId", workflowId);
		return query.getSingleResult();
	}
	@Override
	public List<FitStructureToExperimentalData3VO> getFitStructureToExperimentalDataBySubtractionId(Integer subtractionId) {
		String ejbQLQuery ="SELECT f FROM FitStructureToExperimentalData f where f.subtractionId = :subtractionId";
		TypedQuery<FitStructureToExperimentalData3VO> query = entityManager.createQuery(ejbQLQuery.toString(), FitStructureToExperimentalData3VO.class).setParameter("subtractionId", subtractionId);
		return query.getResultList();
	}

	@Override
	public List<RigidBodyModeling3VO> getRigidBodyModelingBySubtractionId(int subtractionId) {
		String ejbQLQuery ="SELECT f FROM RigidBodyModeling f where f.subtractionId = :subtractionId";
		TypedQuery<RigidBodyModeling3VO> query = entityManager.createQuery(ejbQLQuery.toString(), RigidBodyModeling3VO.class).setParameter("subtractionId", subtractionId);
		return query.getResultList();
	}

	@Override
	public List<Superposition3VO> getSuperpositionBySubtractionId(int subtractionId) {
		String ejbQLQuery ="SELECT f FROM Superposition f where f.subtractionId = :subtractionId";
		TypedQuery<Superposition3VO> query = entityManager.createQuery(ejbQLQuery.toString(), Superposition3VO.class).setParameter("subtractionId", subtractionId);
		return query.getResultList();
	}

	@Override
	public List<FitStructureToExperimentalData3VO> getFitStructureToExperimentalDataByIds(List<Integer> fitIds) {
		if (fitIds != null){
			if (fitIds.size() > 0){
				String ejbQLQuery ="SELECT f FROM FitStructureToExperimentalData3VO f where f.fitStructureToExperimentalDataId IN :fitIds";
				TypedQuery<FitStructureToExperimentalData3VO> query = entityManager.createQuery(ejbQLQuery.toString(), FitStructureToExperimentalData3VO.class).setParameter("fitIds", fitIds);
				return query.getResultList();
			}
		}
		return new ArrayList<FitStructureToExperimentalData3VO>();
	}
	
	@Override
	public RigidBodyModeling3VO getRigidBodyById(int id) {
		return this.entityManager.find(RigidBodyModeling3VO.class, id);
	}

	@Override
	public Superposition3VO getSuperpositionById(Integer superpositionId) {
		return this.entityManager.find(Superposition3VO.class, superpositionId);
	}
	
}
