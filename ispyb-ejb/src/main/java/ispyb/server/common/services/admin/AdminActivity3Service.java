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

import ispyb.server.common.vos.admin.AdminActivity3VO;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface AdminActivity3Service {

	/**
	 * Create new AdminActivity3.
	 * 
	 * @param vo
	 *            the entity to persist
	 * @return the persisted entity
	 */
	public AdminActivity3VO create(final AdminActivity3VO vo) throws Exception;

	/**
	 * Update the AdminActivity3 data.
	 * 
	 * @param vo
	 *            the entity data to update
	 * @return the updated entity
	 */
	public AdminActivity3VO update(final AdminActivity3VO vo) throws Exception;

	/**
	 * Remove the AdminActivity3 from its pk.
	 * 
	 * @param vo
	 *            the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the AdminActivity3.
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final AdminActivity3VO vo) throws Exception;

	/**
	 * Finds a AdminActivity3 entity by its primary key and set linked value objects if necessary.
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Scientist value object
	 */
	public AdminActivity3VO findByPk(final Integer pk, final boolean withLink1, final boolean withLink2)
			throws Exception;

	/**
	 * loads the vo with all the linked object in eager fetch mode
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public AdminActivity3VO loadEager(AdminActivity3VO vo) throws Exception;

	/**
	 * Find all AdminActivity3 and set linked value objects if necessary.
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	public List<AdminActivity3VO> findAll() throws Exception;

	/**
	 * Find AdminActivity3 according to the following parameters use an hibernate criteria, if the param is null, not
	 * taken into account
	 * 
	 * @param action
	 */
	public List<AdminActivity3VO> findByAction(final String action) throws Exception;

	public List<AdminActivity3VO> findByUsername(final String username) throws Exception;
}