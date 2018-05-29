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

import ispyb.server.common.vos.shipping.Shipping3VO;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

@Remote
public interface Shipping3Service {

	/**
	 * Create new Shipping3.
	 * 
	 * @param vo
	 *            the entity to persist
	 * @return the persisted entity
	 */
	public Shipping3VO create(final Shipping3VO vo) throws Exception;

	/**
	 * Update the Shipping3 data.
	 * 
	 * @param vo
	 *            the entity data to update
	 * @return the updated entity
	 */
	public Shipping3VO update(final Shipping3VO vo) throws Exception;

	/**
	 * Remove the Shipping3 from its pk.
	 * 
	 * @param vo
	 *            the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the Shipping3.
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Shipping3VO vo) throws Exception;

	/**
	 * Finds a Shipping3 entity by its primary key and set linked value objects if necessary.
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @return the Scientist value object
	 */
	public Shipping3VO findByPk(final Integer pk, final boolean withDewars) throws Exception;
	/** Fetch session relation in dewars **/
	public Shipping3VO findByPk(final Integer pk, final boolean withDewars, final boolean withSession) throws Exception;
	
	public Shipping3VO findByPk(final Integer pk, final boolean withDewars, final boolean withContainers, final boolean withSamples) throws Exception;
	
	public Shipping3VO findByPk(final Integer pk, final boolean withDewars, final boolean withcontainers, final boolean withSamples, final boolean withSubSamples) throws Exception ;

	public String findSerialShippingByPk(final Integer pk, final boolean withDewars, final boolean withcontainers, final boolean withSamples, final boolean withSubSamples) throws Exception ;

	/**
	 * Find all Shipping3 and set linked value objects if necessary.
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	public List<Shipping3VO> findAll() throws Exception;

	public List<Shipping3VO> findFiltered(final Integer proposalId, final String type) throws Exception;

	public List<Shipping3VO> findByStatus(final String status, final java.util.Date dateStart, final boolean withDewars) throws Exception;

	public List<Shipping3VO> findByProposal(final Integer userProposalId, final boolean withDewars) throws Exception;

	public List<Shipping3VO> findByProposalType(final String proposalType) throws Exception;

	public List<Shipping3VO> findByProposalCodeAndDates(final String proposalType, Date firstDate, Date endDate)
			throws Exception;

	public List<Shipping3VO> findByCreationDate(Date firstDate, final boolean withDewars) throws Exception;

	public List<Shipping3VO> findByIsStorageShipping(Boolean isStorageShipping) throws Exception;

	public List<Shipping3VO> findByBeamLineOperator(String beamlineOperatorSiteNumber, final boolean withDewars)
			throws Exception;

	public List<Shipping3VO> findByProposalCode(final String proposalCode, final boolean withDewars) throws Exception;

	public List<Shipping3VO> findByCustomQuery(Integer mProposalId, String shippingName, String proposalCode, String proposalNumber,
			String mainProposer, Date date, Date date2, String operatorSiteNumber) throws Exception;

	public List<Shipping3VO> findByCreationDateInterval(Date firstDate, Date endDate) throws Exception;

	/**
	 * update the proposalId, returns the nb of rows updated
	 * 
	 * @param newProposalId
	 * @param oldProposalId
	 * @return
	 * @throws Exception
	 */
	public Integer updateProposalId(final Integer newProposalId, final Integer oldProposalId) throws Exception;

	public int deleteAllSamplesAndContainersForShipping(Integer shippingId) throws Exception;

	public List<Shipping3VO> findFiltered(Integer proposalId, String shippingName, String proposalCodeNumber,
			String proposalNumber, String mainProposer, Date date, Date date2, String operatorSiteNumber, final boolean withDewars)
			throws Exception;

	public Shipping3VO loadEager(Shipping3VO vo) throws Exception;

	public Integer[] countShippingInfo(final Integer shippingId) throws Exception;

	public List<Map<String, Object>> getShippingById(Integer shippingId)
			throws Exception;

	public List<Shipping3VO> findByProposal(Integer proposalId, boolean fetchDewars, boolean fetchContainers,
			boolean feacthSamples) throws Exception;

	public List<Map<String, Object>> getShippingByProposalId(Integer proposalId) throws Exception;

}