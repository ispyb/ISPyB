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

import ispyb.server.mx.vos.sample.XmlSchema3VO;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface XmlSchema3Service {

	/**
	 * Create new XmlSchema3.
	 * 
	 * @param vo
	 *            the entity to persist
	 * @return the persisted entity
	 */
	public XmlSchema3VO create(final XmlSchema3VO vo) throws Exception;

	/**
	 * Update the XmlSchema3 data.
	 * 
	 * @param vo
	 *            the entity data to update
	 * @return the updated entity
	 */
	public XmlSchema3VO update(final XmlSchema3VO vo) throws Exception;

	/**
	 * Remove the XmlSchema3 from its pk.
	 * 
	 * @param vo
	 *            the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the XmlSchema3.
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final XmlSchema3VO vo) throws Exception;

	/**
	 * Finds a XmlSchema3 entity by its primary key and set linked value objects if necessary.
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Scientist value object
	 */
	public XmlSchema3VO findByPk(final Integer pk, final boolean withLink1, final boolean withLink2) throws Exception;

	/**
	 * Find all XmlSchema3 and set linked value objects if necessary.
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	public List<XmlSchema3VO> findAll(final boolean withLink1, final boolean withLink2) throws Exception;

}