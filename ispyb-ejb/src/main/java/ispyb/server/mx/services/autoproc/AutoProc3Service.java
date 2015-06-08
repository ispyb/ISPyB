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

import ispyb.server.mx.vos.autoproc.AutoProc3VO;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface AutoProc3Service {

	/**
	 * Create new AutoProc3.
	 * 
	 * @param vo
	 *            the entity to persist
	 * @return the persisted entity
	 */
	public AutoProc3VO create(final AutoProc3VO vo) throws Exception;

	/**
	 * Update the AutoProc3 data.
	 * 
	 * @param vo
	 *            the entity data to update
	 * @return the updated entity
	 */
	public AutoProc3VO update(final AutoProc3VO vo) throws Exception;

	/**
	 * Remove the AutoProc3 from its pk.
	 * 
	 * @param vo
	 *            the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the AutoProc3.
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final AutoProc3VO vo) throws Exception;

	/**
	 * Finds a AutoProc3 entity by its primary key and set linked value objects if necessary.
	 * 
	 * @param pk
	 *            the primary key
	 * @param fetchAttachments
	 * @param withLink2
	 * @return the Scientist value object
	 */
	public AutoProc3VO findByPk(final Integer pk) throws Exception;

	/**
	 * Find all AutoProc3 and set linked value objects if necessary.
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	public List<AutoProc3VO> findAll() throws Exception;

	public List<AutoProc3VO> findByDataCollectionId(final Integer dataCollectionId) throws Exception;

	/**
	 * Load the autoProc for a specified dataCollection and a specified anomalous mode, and sort them by the space group
	 * number
	 * 
	 * @param dataCollectionId
	 * @param anomalous
	 * @return
	 * @throws Exception
	 */
	public List<AutoProc3VO> findByAnomalousDataCollectionIdAndOrderBySpaceGroupNumber(final Integer dataCollectionId,
			final boolean anomalous) throws Exception;

}
