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

package ispyb.server.mx.services.collections;

import javax.ejb.Remote;

import ispyb.server.mx.vos.collections.DataCollectionFileAttachment3VO;

@Remote
public interface DataCollectionFileAttachment3Service {

	/**
	 * Create new DataCollection3.
	 * 
	 * @param vo
	 *            the entity to persist
	 * @return the persisted entity
	 */
	public DataCollectionFileAttachment3VO create(final DataCollectionFileAttachment3VO vo) throws Exception;

	/**
	 * Update the DataCollectionFileAttachment3VO data.
	 * 
	 * @param vo
	 *            the entity data to update
	 * @return the updated entity
	 */
	public DataCollectionFileAttachment3VO update(final DataCollectionFileAttachment3VO vo) throws Exception;

	/**
	 * Remove the DataCollectionFileAttachment3VO from its pk.
	 * 
	 * @param vo
	 *            the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the DataCollectionFileAttachment3VO.
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final DataCollectionFileAttachment3VO vo) throws Exception;

	/**
	 * Finds a DataCollectionFileAttachment3VO entity by its primary key 
	 * 
	 * @param pk
	 * @return the DataCollectionFileAttachment3VO value object
	 */
	public DataCollectionFileAttachment3VO findByPk(final Integer pk) throws Exception;
	
	public DataCollectionFileAttachment3VO findDataCollectionFileAttachmentByDataCollectionId(Integer dcId) ;

	public DataCollectionFileAttachment3VO merge(DataCollectionFileAttachment3VO detachedInstance);

}
