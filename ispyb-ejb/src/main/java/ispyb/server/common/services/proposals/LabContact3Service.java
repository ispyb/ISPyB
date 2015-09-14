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

import ispyb.server.common.vos.proposals.LabContact3VO;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface LabContact3Service {

	/**
	 * Create new LabContact3.
	 * 
	 * @param vo
	 *            the entity to persist
	 * @return the persisted entity
	 */
	public LabContact3VO create(final LabContact3VO vo) throws Exception;

	/**
	 * Update the LabContact3 data.
	 * 
	 * @param vo
	 *            the entity data to update
	 * @return the updated entity
	 */
	public LabContact3VO update(final LabContact3VO vo) throws Exception;

	/**
	 * Remove the LabContact3 from its pk.
	 * 
	 * @param vo
	 *            the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the LabContact3.
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final LabContact3VO vo) throws Exception;

	/**
	 * Finds a LabContact3 entity by its primary key and set linked value objects if necessary.
	 * 
	 * @param pk
	 *            the primary key
	 * @return the Scientist value object
	 */
	public LabContact3VO findByPk(final Integer pk) throws Exception;

	/**
	 * Find all LabContact3 and set linked value objects if necessary.
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	public List<LabContact3VO> findAll() throws Exception;

	public List<LabContact3VO> findFiltered(final Integer proposalId, final String cardName) throws Exception;

	public List<LabContact3VO> findByPersonIdAndProposalId(final Integer personId, final Integer proposalId) throws Exception;

	public List<LabContact3VO> findByCardName(final String cardNameWithNum) throws Exception;
	
	public List<LabContact3VO> findByProposalId(Integer proposalId) throws Exception;
	/**
	 * returns the nb of shipment linked to the given labcontact
	 * @param labContactId
	 * @return
	 * @throws Exception
	 */
	public Integer hasShipping(final Integer labContactId) throws Exception;


}