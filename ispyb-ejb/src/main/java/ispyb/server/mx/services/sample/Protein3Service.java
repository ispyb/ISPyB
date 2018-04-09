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

package ispyb.server.mx.services.sample;

import ispyb.server.mx.vos.sample.Protein3VO;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

@Remote
public interface Protein3Service {

	/**
	 * Create new Protein3.
	 * 
	 * @param vo
	 *            the entity to persist
	 * @return the persisted entity
	 */
	public Protein3VO create(final Protein3VO vo) throws Exception;

	/**
	 * Update the Protein3 data.
	 * 
	 * @param vo
	 *            the entity data to update
	 * @return the updated entity
	 */
	public Protein3VO update(final Protein3VO vo) throws Exception;

	/**
	 * Remove the Protein3 from its pk.
	 * 
	 * @param vo
	 *            the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the Protein3.
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Protein3VO vo) throws Exception;

	/**
	 * Finds a Protein3 entity by its primary key and set linked value objects if necessary.
	 * 
	 * @param pk
	 *            the primary key
	 * @param withCrystals
	 * @return the Scientist value object
	 */
	public Protein3VO findByPk(final Integer pk, final boolean withCrystals) throws Exception;

	/**
	 * loads the vo with all the linked object in eager fetch mode
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public Protein3VO loadEager(Protein3VO vo) throws Exception;

	/**
	 * Find all Protein3 and set linked value objects if necessary.
	 * 
	 * @param withCrystals
	 */
	public List<Protein3VO> findAll(final boolean withCrystals) throws Exception;

	public List<Protein3VO> findByAcronymAndProposalId(final Integer proposalId, final String acronym) throws Exception;
	public List<Protein3VO> findByAcronymAndProposalId(final Integer proposalId, final String acronym, final boolean withCrystal, final boolean sortByAcronym) throws Exception;
	
	public List<Protein3VO> findByProposalId(final Integer proposalId) throws Exception;
	public List<Protein3VO> findByProposalId(final Integer proposalId, final boolean withCrystal, boolean sortByAcronym) throws Exception;
	
	/**
	 * update the proposalId, returns the nb of rows updated
	 * 
	 * @param newProposalId
	 * @param oldProposalId
	 * @return
	 * @throws Exception
	 */
	public Integer updateProposalId(final Integer newProposalId, final Integer oldProposalId) throws Exception;

	public List<Map<String, Object>> getStatsByProposal(int proposalId);

}
