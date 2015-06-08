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

import ispyb.server.mx.vos.sample.Crystal3VO;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface Crystal3Service {

	/**
	 * Create new Crystal3.
	 * 
	 * @param vo
	 *            the entity to persist
	 * @return the persisted entity
	 */
	public Crystal3VO create(final Crystal3VO vo) throws Exception;

	/**
	 * Update the Crystal3 data.
	 * 
	 * @param vo
	 *            the entity data to update
	 * @return the updated entity
	 */
	public Crystal3VO update(final Crystal3VO vo) throws Exception;

	/**
	 * Remove the Crystal3 from its pk.
	 * 
	 * @param vo
	 *            the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the Crystal3.
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Crystal3VO vo) throws Exception;

	/**
	 * Finds a Crystal3 entity by its primary key and set linked value objects if necessary.
	 * 
	 * @param pk
	 *            the primary key
	 * @param fetchSamples
	 * @return the value object
	 */
	public Crystal3VO findByPk(final Integer pk, final boolean fetchSamples) throws Exception;

	/**
	 * loads an entity with a eager fetch to set linked value objects.
	 * 
	 * @return the value object
	 */
	public Crystal3VO loadEager(Crystal3VO vo) throws Exception;

	/**
	 * Find all Crystal3 and set linked value objects if necessary.
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	public List<Crystal3VO> findAll(final boolean withLink1, final boolean withLink2) throws Exception;

	/**
	 * 
	 * @param proposalId
	 * @param proteinId
	 * @param acronym
	 * @param spaceGroup
	 * @return
	 * @throws Exception
	 */
	public List<Crystal3VO> findFiltered(final Integer proposalId, final Integer proteinId, final String acronym,
			final String spaceGroup) throws Exception;

	/**
	 * 
	 * @param acronym
	 * @param currentCrystal
	 * @param proposalId
	 * @return
	 * @throws Exception
	 */
	public Crystal3VO findByAcronymAndCellParam(String acronym, Crystal3VO currentCrystal, Integer proposalId)
			throws Exception;

	public List<Crystal3VO> findByProposalId(final Integer proposalId) throws Exception;

	public List<Crystal3VO> findByProteinId(final Integer proteinId) throws Exception;
	
	public Integer countSamples(final Integer crystalId)throws Exception;
	
	/**
	 * returns the pdb full path for a given acronym/spaceGroup
	 * @param proteinAcronym
	 * @param spaceGroup
	 * @return
	 * @throws Exception
	 */
	public String findPdbFullPath(final String proteinAcronym, final String spaceGroup) throws Exception;

}
