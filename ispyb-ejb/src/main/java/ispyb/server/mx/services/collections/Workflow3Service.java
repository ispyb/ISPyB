/*************************************************************************************************
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
 ****************************************************************************************************/

package ispyb.server.mx.services.collections;

import javax.ejb.Remote;

import java.util.List;

import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;
import ispyb.server.mx.vos.collections.InputParameterWorkflow;
import ispyb.server.mx.vos.collections.Workflow3VO;

@Remote
public interface Workflow3Service {

	/**
	 * Create new Workflow3.
	 * @param vo the entity to persist
	 * @return the persisted entity
	 */
	public Workflow3VO create(final Workflow3VO vo) throws Exception;

	/**
	 * Update the Workflow3 data.
	 * @param vo the entity data to update
	 * @return the updated entity
	 */
	public Workflow3VO update(final Workflow3VO vo) throws Exception;

	/**
	 * Remove the Workflow3 from its pk.
	 * @param vo the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the Workflow3.
	 * @param vo the entity to remove.
	 */
	public void delete(final Workflow3VO vo) throws Exception;

	/**
	 * Finds a Workflow3 entity by its primary key and set linked value objects if necessary.
	 * @param pk the primary key
	 * @return the Scientist value object
	 */
	public Workflow3VO findByPk(final Integer pk) throws Exception;

	

	/**
	 * Find all Workflow3 and set linked value objects if necessary.
	 */
	public List<Workflow3VO> findAll() throws Exception;
	
	public Integer countWF(final String year, final String beamline, final String workflowType, final String status) throws Exception;

	public List getWorkflowResult(final String year, final String beamline, final String workflowType)throws Exception;
	
	public List<Workflow3VO> findAllByStatus(final String status) throws Exception;
	
	public List<InputParameterWorkflow> findInputParametersByWorkflowId(final int workflowId) throws Exception;
}