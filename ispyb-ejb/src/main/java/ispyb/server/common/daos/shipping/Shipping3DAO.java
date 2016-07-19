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

package ispyb.server.common.daos.shipping;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import ispyb.server.common.vos.shipping.Shipping3VO;

/**
 * <p>
 * The data access object for Shipping3 objects (rows of table Shipping).
 * </p>
 */
@Local
public interface Shipping3DAO {

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(Shipping3VO vo) throws Exception;

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 * 
	 * @throws VOValidateException
	 *             if the value object data integrity is not ok.
	 */
	public Shipping3VO update(Shipping3VO vo) throws Exception;

	/* Deletion methods ----------------------------------------------------- */

	/**
	 * <p>
	 * Deletes the given value object.
	 * </p>
	 * 
	 * @param vo
	 *            the value object to delete.
	 */
	public void delete(Shipping3VO vo);

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the Shipping3VO instance matching the given primary key.
	 * </p>
	 * <p>
	 * <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u> if the value object has to be
	 * used out the EJB container.
	 * </p>
	 * 
	 * @param pk
	 *            the primary key of the object to load.
	 * @param fetchRelation1
	 *            if true, the linked instances by the relation "relation1" will be set.
	 */
	public Shipping3VO findByPk(Integer pk, boolean fetchDewars);
	
	public Shipping3VO findByPk(Integer pk, boolean fetchDewars, boolean fetchContainers, boolean fetchSamples);

	/**
	 * <p>
	 * Returns the Shipping3VO instances.
	 * </p>
	 * <p>
	 * <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u> if the value object has to be
	 * used out the EJB container.
	 * </p>
	 * 
	 */
	public List<Shipping3VO> findAll();

	public List<Shipping3VO> findFiltered(Integer proposalId, String type);

	public List<Shipping3VO> findByStatus(String type, java.util.Date dateStart, boolean withDewars);

	public List<Shipping3VO> findByBeamLineOperator(String beamlineOperatorSiteNumber, boolean withDewars);

	public List<Shipping3VO> findByIsStorageShipping(Boolean isStorageShipping);

	public List<Shipping3VO> findFiltered(Integer proposalId, String shippingName, String proposalCode, Integer proposalNumber,
			String mainProposer, Date date, Date date2, String operatorSiteNumber, String type, boolean withDewars);

	/**
	 * update the proposalId, returns the nb of rows updated
	 * 
	 * @param newProposalId
	 * @param oldProposalId
	 * @return
	 */
	public Integer updateProposalId(Integer newProposalId, Integer oldProposalId);

	public Integer[] countShippingInfo(Integer shippingId);

	/**
	 * @param pk
	 * @param withDewars
	 * @param withSession
	 * @return
	 */
	public Shipping3VO findByPk(Integer pk, boolean withDewars,boolean withSession);

	public List<Map<String, Object>> getShippingById(int shippingId);

	public List<Shipping3VO> findByProposalId(Integer proposalId, boolean fetchDewars, boolean fetchContainers,
			boolean feacthSamples);

	public List<Map<String, Object>> getShippingByProposalId(int proposalId);

}
