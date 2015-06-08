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

package ispyb.server.mx.daos.autoproc;

import ispyb.server.mx.vos.autoproc.IspybAutoProcAttachment3VO;

import java.util.List;

import javax.ejb.Local;

/**
 * <p>
 * 	The data access object for IspybAutoProcAttachment3 objects (rows of table
 *  IspybAutoProcAttachment).
 * </p>
 */
@Local
public interface IspybAutoProcAttachment3DAO {

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 *  Insert the given value object.
	 * 	TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(IspybAutoProcAttachment3VO vo) throws Exception;

	/**
	 * <p>
	 *  Update the given value object.
	 * 	TODO update this comment for update details.
	 * </p>
	 * @throws VOValidateException if the value object data integrity is not ok.
	 */
	public IspybAutoProcAttachment3VO update(IspybAutoProcAttachment3VO vo) throws Exception;

	/* Deletion methods ----------------------------------------------------- */

	/**
	 * <p>
	 * 	Deletes the given value object.
	 * </p>
	 * @param vo the value object to delete.
	 */
	public void delete(IspybAutoProcAttachment3VO vo);

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * 	Returns the IspybAutoProcAttachment3VO instance matching the given primary key.
	 * </p>
	 * <p>
	 *  <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u>
	 *  if the value object has to be used out the EJB container.
	 * </p>
	 * @param pk the primary key of the object to load.
	 */
	public IspybAutoProcAttachment3VO findByPk(Integer pk);

	/**
	 * <p>
	 * 	Returns the IspybAutoProcAttachment3VO instances.
	 * </p>
	 * <p>
	 *  <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u>
	 *  if the value object has to be used out the EJB container.
	 * </p>
	 */
	public List<IspybAutoProcAttachment3VO> findAll();

}

