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

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ispyb.server.mx.vos.collections.WorkflowStep3VO;


@Stateless
public class WorkflowStep3ServiceBean implements WorkflowStep3Service, WorkflowStep3ServiceLocal {
	
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;
	
	@Override
	public void merge(WorkflowStep3VO workflowStep3VO) {
		try {
			entityManager.merge(workflowStep3VO);
		} catch (RuntimeException re) {
			throw re;
		}
	}

}
