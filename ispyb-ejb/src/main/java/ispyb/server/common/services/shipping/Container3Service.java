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

import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import java.util.List;
import javax.ejb.Remote;
import ispyb.server.common.vos.shipping.Container3VO;

@Remote
public interface Container3Service {

	/**
	 * Create new Container3.
	 * 
	 * @param vo
	 *            the entity to persist
	 * @return the persisted entity
	 */
	public Container3VO create(final Container3VO vo) throws Exception;

	/**
	 * Update the Container3 data.
	 * 
	 * @param vo
	 *            the entity data to update
	 * @return the updated entity
	 */
	public Container3VO update(final Container3VO vo) throws Exception;

	/**
	 * Remove the Container3 from its pk.
	 * 
	 * @param vo
	 *            the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the Container3.
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Container3VO vo) throws Exception;

	/**
	 * Finds a Container3 entity by its primary key and set linked value objects if necessary.
	 * 
	 * @param pk
	 *            the primary key
	 * @param fetchSamples
	 * @return the value object
	 */
	public Container3VO findByPk(final Integer pk, final boolean fetchSamples) throws Exception;

	/**
	 * Find all Container3 and set linked value objects if necessary.
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	public List<Container3VO> findAll() throws Exception;

	public List<Container3VO> findByDewarId(final Integer dewarId) throws Exception;
	
	public List<Container3VO> findByProposalIdAndStatus(final Integer proposalId, final String containerStatusProcess)
			throws Exception;

	public List<Container3VO> findByProposalIdAndCode(final Integer proposalId, final String containerCode)
			throws Exception;
	
	public List<Container3VO> findByCode(final Integer dewarId, final String code)throws Exception;

	/**
	 * It stores a container that already exist on the database
	 * @param container3vo
	 * @param proposalId
	 * @return
	 * @throws Exception
	 */
	public Container3VO saveContainer(Container3VO container3vo, int proposalId) throws Exception;

	/**
	 * Saves a list of containers
	 * @param dewars3vo
	 * @param proposalId
	 * @return
	 * @throws Exception
	 */
	public void saveDewarList(List<Dewar3VO> dewars3vo, int proposalId, Integer shippingId) throws Exception;

	
}
