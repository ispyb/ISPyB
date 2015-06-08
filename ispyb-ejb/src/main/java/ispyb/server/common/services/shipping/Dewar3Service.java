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

package ispyb.server.common.services.shipping;

import ispyb.server.common.vos.shipping.Dewar3VO;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

@Remote
public interface Dewar3Service {

	/**
	 * Create new Dewar3.
	 * 
	 * @param vo
	 *            the entity to persist
	 * @return the persisted entity
	 */
	public Dewar3VO create(final Dewar3VO vo) throws Exception;

	/**
	 * Update the Dewar3 data.
	 * 
	 * @param vo
	 *            the entity data to update
	 * @return the updated entity
	 */
	public Dewar3VO update(final Dewar3VO vo) throws Exception;

	/**
	 * Remove the Dewar3 from its pk.
	 * 
	 * @param vo
	 *            the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the Dewar3.
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Dewar3VO vo) throws Exception;

	/**
	 * Finds a Dewar3 entity by its primary key and set linked value objects if necessary.
	 * 
	 * @param pk
	 *            the primary key
	 * @param fetchContainers
	 * @return the Scientist value object
	 */
	public Dewar3VO findByPk(final Integer pk, final boolean withContainers, final boolean withDewarTransportHistory)
			throws Exception;

	/**
	 * Find all Dewar3 and set linked value objects if necessary.
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	public List<Dewar3VO> findAll(final boolean withContainers, final boolean withDewarTransportHistory)
			throws Exception;

	/**
	 * 
	 * @param proposalId
	 * @param shippingId
	 * @param type
	 * @param code
	 * @param comments
	 * @param date1
	 * @param date2
	 * @param dewarStatus
	 * @param storageLocation
	 * @return
	 * @throws Exception
	 */

	public List<Dewar3VO> findByCustomQuery(final Integer proposalId, final String dewarName, final String comments,
			final String barCode, final String dewarStatus, final String storageLocation,
			final Date experimentDateStart, final Date experimentDateEnd, final String operatorSiteNumber)
			throws Exception;

	public List<Dewar3VO> findByProposalId(final Integer proposalId) throws Exception;

	public List<Dewar3VO> findByStatus(final String status) throws Exception;

	public List<Dewar3VO> findByStorageLocation(final String storageLocation) throws Exception;

	public List<Dewar3VO> findByExperimentDate(final Date experimentDateStart) throws Exception;

	public List<Dewar3VO> findByExperimentDate(final Date experimentDateStart, final Date experimentDateEnd)
			throws Exception;

	public List<Dewar3VO> findByExperiment(final Integer experimentId, final String dewarStatus) throws Exception;
	
	public List<Dewar3VO> findByShippingId(int shippindId) throws Exception;

	public List<Dewar3VO> findByType(String type) throws Exception;

	public List<Dewar3VO> findByCode(String code) throws Exception;

	public List<Dewar3VO> findByComments(String comments) throws Exception;

	public List<Dewar3VO> findFiltered(final Integer proposalId, final Integer shippingId, final String type,
			final String code, final String comments, final Date date1, final Date date2, final String dewarStatus,
			final String storageLocation, boolean withDewarHistory, boolean withContainer) throws Exception;

	public List<Dewar3VO> findFiltered(final Integer proposalId, final Integer shippingId, final String type,
			final String code, final String comments, final Date date1, final Date date2, final String dewarStatus,
			final String storageLocation, Integer dewarId, final boolean withDewarHistory, final boolean withContainer)
			throws Exception;

	public List<Dewar3VO> findFiltered(final Integer proposalId, final Integer shippingId, final String type,
			final String code, final String barCode, final String comments, final Date date1, final Date date2,
			final String dewarStatus, final String storageLocation, Integer dewarId, Integer firstExperimentId, boolean fetchSession,
			boolean withDewarHistory, boolean withContainer) throws Exception;

	public Dewar3VO loadEager(Dewar3VO vo) throws Exception;

	public List<Dewar3VO> findByBarCode(final String dewarBarCode) throws Exception;

	public Integer countDewarSamples(final Integer dewarId) throws Exception;

	public List<Dewar3VO> findByDateWithHistory(final java.sql.Date firstDate) throws Exception;
}