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

import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.proposals.ProposalWS3VO;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;
import javax.jws.WebMethod;

@Remote
public interface Proposal3Service {

	/**
	 * @param vo
	 *            the entity to persist
	 * @return the persisted entity
	 */
	public Proposal3VO create(final Proposal3VO vo) throws Exception;

	/**
	 * Update the Proposal3 data.
	 * 
	 * @param vo
	 *            the entity data to update
	 * @return the updated entity
	 */
	public Proposal3VO update(final Proposal3VO vo) throws Exception;

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
	public void delete(final Proposal3VO vo) throws Exception;

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
	public Proposal3VO findByPk(final Integer pk) throws Exception;

	/**
	 * Finds a Proposal3 entity by its primary key and set linked value objects if necessary.
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Scientist value object
	 */
	public Proposal3VO findByPk(final Integer pk, final boolean fetchSessions, final boolean fetchProteins,
			final boolean fetchShippings) throws Exception;

	@WebMethod
	public Proposal3VO findWithParticipantsByPk(final Integer pk) throws Exception;
	
	/**
	 * Find all Proposal3 and set linked value objects if necessary.
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	public List<Proposal3VO> findAll(final boolean detachLight) throws Exception;

	public List<Proposal3VO> findByCodeAndNumber(final String code, final String number, final boolean fetchSessions,
			final boolean fetchProteins, final boolean detachLight) throws Exception;

	public List<Proposal3VO> findByLoginName(final String loginName) throws Exception;
	
	public ProposalWS3VO findForWSByCodeAndNumber(final String code, final String number) throws Exception;

	/**
	 * update the proposalId, returns the nb of rows updated of table BLSession, Shipping, Protein
	 * 
	 * @param newProposalId
	 * @param oldProposalId
	 * @return
	 * @throws Exception
	 */
	public Integer[] updateProposalFromIds(final Integer newProposalId, final Integer oldProposalId) throws Exception;

	public Integer[] updateProposalFromDesc(String newPropDesc, String oldPropDesc) throws Exception;

	/**
	 * Finds a Proposal by its code and number and title (if title is null only search by code and number).
	 * 
	 * @param code
	 * @param number
	 * @param title
	 * @return
	 */
	@WebMethod
	public List<Proposal3VO> findFiltered(final String code, final String number, final String title) throws Exception;

	public List<Proposal3VO> findProposalByLoginName(String loginName, String site);

	public List<Proposal3VO> findProposalByLoginName(String loginName);
	
	public List<String> findProposalNamesByLoginName(String loginName, String site);
	
	public Proposal3VO findProposalById(int proposalId);
	
	public List<Proposal3VO> findAllProposals();
	
	public List<Map<String, Object>> findProposals();
	
	public List<Map<String, Object>> findProposals(String loginName);

	public List<Map<String, Object>> findProposalById(Integer proposalId);

}