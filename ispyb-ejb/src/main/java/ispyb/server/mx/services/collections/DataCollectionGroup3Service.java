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

import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroupWS3VO;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface DataCollectionGroup3Service {

	/**
	 * Create new DataCollectionGroup3.
	 * 
	 * @param vo
	 *            the entity to persist
	 * @return the persisted entity
	 */
	public DataCollectionGroup3VO create(final DataCollectionGroup3VO vo) throws Exception;

	/**
	 * Update the DataCollectionGroup3 data.
	 * 
	 * @param vo
	 *            the entity data to update
	 * @return the updated entity
	 */
	public DataCollectionGroup3VO update(final DataCollectionGroup3VO vo) throws Exception;

	/**
	 * Remove the DataCollectionGroup3 from its pk.
	 * 
	 * @param vo
	 *            the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the DataCollectionGroup3.
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final DataCollectionGroup3VO vo) throws Exception;

	/**
	 * Finds a DataCollectionGroup3 entity by its primary key and set linked value objects if necessary.
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Scientist value object
	 */
	public DataCollectionGroup3VO findByPk(final Integer pk, final boolean withDataCollection, final boolean withScreening) throws Exception;

	/**
	 * Find a dataCollectionGroup by its primary key -- webservices object
	 * 
	 * @param pk
	 * @param withLink1
	 * @param withLink2
	 * @return
	 * @throws Exception
	 */
	public DataCollectionGroupWS3VO findForWSByPk(final Integer pk, final boolean withDataCollection, final boolean withScreening) throws Exception;

	/**
	 * Find all DataCollectionGroup3 and set linked value objects if necessary.
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	public List<DataCollectionGroup3VO> findAll(final boolean withDataCollection) throws Exception;

	/**
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public DataCollectionGroup3VO loadEager(DataCollectionGroup3VO vo) throws Exception;

	public List<DataCollectionGroup3VO> findFiltered(final Integer sessionId, final boolean withDataCollection, final boolean withScreening)
			throws Exception;

	/**
	 * finds groups for a given workflow
	 * 
	 * @param workflowId
	 * @return
	 * @throws Exception
	 */
	public List<DataCollectionGroup3VO> findByWorkflow(final Integer workflowId) throws Exception;

	/**
	 * find groups for a given blSampleId
	 * 
	 * @param sampleId
	 * @return
	 * @throws Exception
	 */
	public List<DataCollectionGroup3VO> findBySampleId(final Integer sampleId, final boolean withDataCollection, final boolean withScreening)
			throws Exception;

}