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

package ispyb.server.common.services.admin;

import ispyb.server.common.vos.admin.AdminVar3VO;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface AdminVar3Service {

	/**
	 * Create new AdminVar3.
	 * 
	 * @param vo
	 *            the entity to persist
	 * @return the persisted entity
	 */
	public AdminVar3VO create(final AdminVar3VO vo) throws Exception;

	/**
	 * Update the AdminVar3 data.
	 * 
	 * @param vo
	 *            the entity data to update
	 * @return the updated entity
	 */
	public AdminVar3VO update(final AdminVar3VO vo) throws Exception;

	/**
	 * Remove the AdminVar3 from its pk.
	 * 
	 * @param vo
	 *            the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the AdminVar3.
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final AdminVar3VO vo) throws Exception;

	/**
	 * Finds a AdminVar3 entity by its primary key and set linked value objects if necessary.
	 * 
	 * @param pk
	 *            the primary key
	 * @return the Scientist value object
	 */
	public AdminVar3VO findByPk(final Integer pk) throws Exception;

	/**
	 * loads the vo with all the linked object in eager fetch mode
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public AdminVar3VO loadEager(AdminVar3VO vo) throws Exception;

	/**
	 * Find all AdminVar3 and set linked value objects if necessary.
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	public List<AdminVar3VO> findAll() throws Exception;

	/**
	 * Find all AdminVar3 and set linked value objects if necessary.
	 * 
	 * @param name
	 */
	public List<AdminVar3VO> findByName(final String name) throws Exception;

	public List<AdminVar3VO> findByAction(final String statusLogon) throws Exception;

}