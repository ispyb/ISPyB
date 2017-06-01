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

import ispyb.server.common.vos.shipping.Dewar3VO;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;

/**
 * <p>
 * The data access object for Dewar3 objects (rows of table Dewar).
 * </p>
 */
@Local
public interface Dewar3DAO {

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(Dewar3VO vo) throws Exception;

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 * 
	 * @throws VOValidateException
	 *             if the value object data integrity is not ok.
	 */
	public Dewar3VO update(Dewar3VO vo) throws Exception;

	/* Deletion methods ----------------------------------------------------- */

	/**
	 * <p>
	 * Deletes the given value object.
	 * </p>
	 * 
	 * @param vo
	 *            the value object to delete.
	 */
	public void delete(Dewar3VO vo);

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the Dewar3VO instance matching the given primary key.
	 * </p>
	 * <p>
	 * <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u> if the value object has to be
	 * used out the EJB container.
	 * </p>
	 * 
	 * @param pk
	 *            the primary key of the object to load.
	 * @param fetchContainers
	 *            if true, the linked instances by the relation "relation1" will be set.
	 */
	public Dewar3VO findByPk(Integer pk, boolean fetchContainers, boolean fetchDewarTransportHitory);
	
	public Dewar3VO findByPk(Integer pk, boolean fetchContainers, boolean fetchDewarTransportHitory, boolean fecthSamples);
	
	

	/**
	 * <p>
	 * Returns the Dewar3VO instances.
	 * </p>
	 * <p>
	 * <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u> if the value object has to be
	 * used out the EJB container.
	 * </p>
	 * 
	 */
	public List<Dewar3VO> findAll(boolean fetchContainers, boolean fetchDewarTransportHitory);

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
	 */

	public List<Dewar3VO> findByCustomQuery(Integer proposalId, String dewarName, String comments, String barCode,
			String dewarStatus, String storageLocation, Date experimentDateStart, Date experimentDateEnd,
			String operatorSiteNumber) throws SQLException;

	public List<Dewar3VO> findFiltered(Integer proposalId, Integer shippingId, String type, String code,
			String comments, Date date1, Date date2, String dewarStatus, String storageLocation);

	public List<Dewar3VO> findFiltered(Integer proposalId, Integer shippingId, String type, String code,
			String comments, Date date1, Date date2, String dewarStatus, String storageLocation, Integer dewarId);

	public List<Dewar3VO> findFiltered(Integer proposalId, Integer shippingId, String type, String code,
			String barCode, String comments, Date date1, Date date2, String dewarStatus, String storageLocation,
			Integer dewarId, Integer experimentId,  boolean fetchSession, boolean fetchDewarHistory, boolean fetchContainer);

	public Integer countDewarSamples(Integer dewarId);

	public List<Dewar3VO> findByDateWithHistory(java.sql.Date firstDate);
}
