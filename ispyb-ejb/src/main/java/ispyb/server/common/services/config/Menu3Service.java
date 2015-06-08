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

package ispyb.server.common.services.config;

import java.util.List;

import ispyb.server.common.vos.config.Menu3VO;

import javax.ejb.Remote;

@Remote
public interface Menu3Service {

	/**
	 * Create new Menu3.
	 * @param vo the entity to persist
	 * @return the persisted entity
	 */
	public Menu3VO create(final Menu3VO vo) throws Exception;

	/**
	 * Update the Menu3 data.
	 * @param vo the entity data to update
	 * @return the updated entity
	 */
	public Menu3VO update(final Menu3VO vo) throws Exception;

	/**
	 * Remove the Menu3 from its pk.
	 * @param vo the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the Menu3.
	 * @param vo the entity to remove.
	 */
	public void delete(final Menu3VO vo) throws Exception;

	/**
	 * Finds a Menu3 entity by its primary key and set linked value objects if necessary.
	 * @param pk the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Scientist value object
	 */
	public Menu3VO findByPk(final Integer pk, final boolean withMenuGroup) throws Exception;

	/**
	 * loads the vo with all the linked object in eager fetch mode
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public Menu3VO loadEager(Menu3VO vo) throws Exception;

	/**
	 * Find all Menu3 and set linked value objects if necessary.
	 * @param withLink1
	 * @param withLink2
	 */
	public List<Menu3VO> findAll(final boolean withMenuGroup,
			 final boolean detachLight)
			throws Exception;
	
	public List<Menu3VO> findFiltered(final Integer parentId, final Integer menuGroupId, String proposalCode)throws Exception;

}