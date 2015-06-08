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

package ispyb.server.mx.services.autoproc;

import java.util.List;

import ispyb.server.mx.vos.autoproc.AutoProcScalingHasInt3VO;

import javax.ejb.Remote;

@Remote
public interface AutoProcScalingHasInt3Service {

	/**
	 * Create new AutoProcScalingHasInt3.
	 * @param vo the entity to persist
	 * @return the persisted entity
	 */
	public AutoProcScalingHasInt3VO create(final AutoProcScalingHasInt3VO vo) throws Exception;

	/**
	 * Update the AutoProcScalingHasInt3 data.
	 * @param vo the entity data to update
	 * @return the updated entity
	 */
	public AutoProcScalingHasInt3VO update(final AutoProcScalingHasInt3VO vo) throws Exception;

	/**
	 * Remove the AutoProcScalingHasInt3 from its pk.
	 * @param vo the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the AutoProcScalingHasInt3.
	 * @param vo the entity to remove.
	 */
	public void delete(final AutoProcScalingHasInt3VO vo) throws Exception;

	/**
	 * Finds a AutoProcScalingHasInt3 entity by its primary key and set linked value objects if necessary.
	 * @param pk the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Scientist value object
	 */
	public AutoProcScalingHasInt3VO findByPk(final Integer pk) throws Exception;

	/**
	 * Find all AutoProcScalingHasInt3 and set linked value objects if necessary.
	 * @param withLink1
	 * @param withLink2
	 */
	public List<AutoProcScalingHasInt3VO> findAll()
			throws Exception;
	
	
	/**
	 * returns the list of AutoProcScaling for a given autoProcIntegrationId
	 * @param autoProcIntegrationId
	 * @return
	 * @throws Exception
	 */
	public List<AutoProcScalingHasInt3VO> findFiltered(final Integer autoProcIntegrationId) throws Exception;

}