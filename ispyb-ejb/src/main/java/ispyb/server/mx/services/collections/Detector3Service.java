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

import java.util.List;

import javax.ejb.Remote;

import ispyb.server.mx.vos.collections.Detector3VO;

@Remote
public interface Detector3Service {

	/**
	 * Create new Detector3.
	 * @param vo the entity to persist
	 * @return the persisted entity
	 */
	public Detector3VO create(final Detector3VO vo) throws Exception;

	/**
	 * Update the Detector3 data.
	 * @param vo the entity data to update
	 * @return the updated entity
	 */
	public Detector3VO update(final Detector3VO vo) throws Exception;

	/**
	 * Remove the Detector3 from its pk.
	 * @param vo the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the Detector3.
	 * @param vo the entity to remove.
	 */
	public void delete(final Detector3VO vo) throws Exception;

	/**
	 * Finds a Detector3 entity by its primary key and set linked value objects if necessary.
	 * @param pk the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Scientist value object
	 */
	public Detector3VO findByPk(final Integer pk) throws Exception;

	/**
	 * loads the vo with all the linked object in eager fetch mode
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public Detector3VO loadEager(Detector3VO vo) throws Exception;

	/**
	 * Find all Detector3 and set linked value objects if necessary.
	 * @param withLink1
	 * @param withLink2
	 */
	public List<Detector3VO> findAll()
			throws Exception;
	
	/**
	 * Find a dataCollectionGroup by its primary key -- webservices object
	 * @param pk
	 * @param withLink1
	 * @param withLink2
	 * @return
	 * @throws Exception
	 */
	public Detector3VO findForWSByPk(final Integer pk) throws Exception;
	
	/**
	 * returns the detector with the given characteristics -- null otherwise
	 * @param detectorType
	 * @param detectorManufacturer
	 * @param detectorModel
	 * @param detectorPixelSizeHorizontal
	 * @param detectorPixelSizeVertical
	 * @return
	 * @throws Exception
	 */
	public Detector3VO findByCharacteristics(final String detectorType, final String detectorManufacturer, 
			final String detectorModel, final Double detectorPixelSizeHorizontal, 
			final Double detectorPixelSizeVertical) throws Exception ;
	
	/**
	 * returns the detector with the given characteristics -- null otherwise
	 * @param detectorType
	 * @param detectorManufacturer
	 * @param detectorModel
	 * @param detectorMode
	 * @return
	 * @throws Exception
	 */
	public Detector3VO findDetector(final String detectorType, final String detectorManufacturer, 
			final String detectorModel, final String detectorMode) throws Exception ;
	

}