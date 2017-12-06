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

package ispyb.server.common.services.proposals;

import java.util.List;

import javax.ejb.Remote;
import javax.jws.WebMethod;

import ispyb.server.common.vos.proposals.ProposalHasPerson3VO;

@Remote
public interface ProposalHasPerson3Service {

	/**
	 * @param vo
	 *            the entity to persist
	 * @return the persisted entity
	 */
	public ProposalHasPerson3VO create(final ProposalHasPerson3VO vo) throws Exception;
	
	public ProposalHasPerson3VO create(final Integer proposalId, final Integer personId) throws Exception;

	/**
	 * Update the Proposal3 data.
	 * 
	 * @param vo
	 *            the entity data to update
	 * @return the updated entity
	 */
	public ProposalHasPerson3VO update(final ProposalHasPerson3VO vo) throws Exception;

	/**
	 * Remove the Proposal3 from its pk.
	 * 
	 * @param vo
	 *            the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the BeamLineSetup.
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final ProposalHasPerson3VO vo) throws Exception;

	/**
	 * Finds a Proposal3 entity by its primary key and set linked value objects if necessary.
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Scientist value object
	 */
	@WebMethod
	public ProposalHasPerson3VO findByPk(final Integer pk) throws Exception;

	public List<ProposalHasPerson3VO> findByProposalPk(final Integer proposalPk) throws Exception;
	
	public void removeByProposalPk(final Integer proposalPk) throws Exception;

	public List<ProposalHasPerson3VO> findByPersonPk(final Integer personPk) throws Exception;
	
	public List<ProposalHasPerson3VO> findByProposalAndPersonPk(final Integer proposalId, final Integer personId) throws Exception;
}