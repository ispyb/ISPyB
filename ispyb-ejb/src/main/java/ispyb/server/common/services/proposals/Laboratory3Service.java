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

package ispyb.server.common.services.proposals;

import ispyb.server.common.vos.proposals.Laboratory3VO;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface Laboratory3Service {

	/**
	 * Create new Laboratory3.
	 * 
	 * @param vo
	 *            the entity to persist
	 * @return the persisted entity
	 */
	public Laboratory3VO create(final Laboratory3VO vo) throws Exception;
	
	public Laboratory3VO merge(Laboratory3VO detachedInstance);


	/**
	 * Update the Laboratory3 data.
	 * 
	 * @param vo
	 *            the entity data to update
	 * @return the updated entity
	 */
	public Laboratory3VO update(final Laboratory3VO vo) throws Exception;

	/**
	 * Remove the Laboratory3 from its pk.
	 * 
	 * @param vo
	 *            the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the Laboratory3.
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Laboratory3VO vo) throws Exception;

	/**
	 * Finds a Laboratory3 entity by its primary key and set linked value objects if necessary.
	 * 
	 * @param pk
	 *            the primary key
	 * @return the value object
	 */
	public Laboratory3VO findByPk(final Integer pk) throws Exception;

	/**
	 * Find a Laboratory for a specified code and proposal number
	 * 
	 * @param code
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public Laboratory3VO findLaboratoryByProposalCodeAndNumber(final String code, final String number) throws Exception;

	public List<Laboratory3VO> findByNameAndCityAndCountry(final String laboratoryName, final String city,
			final String country) throws Exception;
	
	public Laboratory3VO findByLaboratoryExtPk(final Integer laboExtPk);

}