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

import java.util.List;

import javax.ejb.Remote;

import ispyb.server.mx.vos.sample.BLSampleImage3VO;

@Remote
public interface BLSampleImage3Service {

	/**
	 * Create new BLSampleImage3.
	 * 
	 * @param vo
	 *            the entity to persist
	 * @return the persisted entity
	 */
	public BLSampleImage3VO create(final BLSampleImage3VO vo) throws Exception;

	/**
	 * Update the BLSample3 data.
	 * 
	 * @param vo
	 *            the entity data to update
	 * @return the updated entity
	 */
	public BLSampleImage3VO update(final BLSampleImage3VO vo) throws Exception;

	/**
	 * Remove the BLSample3 from its pk.
	 * 
	 * @param vo
	 *            the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the BLSample3.
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final BLSampleImage3VO vo) throws Exception;

	/**
	 * Finds a BLSampleImage3 entity by the sampleId.
	 * 
	 * @param blsampleid
	 * @return the BLSampleImage3 value object
	 */
	public List<BLSampleImage3VO> findByBlSampleId(final Integer blsampleId) throws Exception;

	public BLSampleImage3VO findByPk(Integer blSampleImageId)  throws Exception;


}
