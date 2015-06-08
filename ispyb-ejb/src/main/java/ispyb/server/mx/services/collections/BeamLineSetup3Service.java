/*******************************************************************************
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
 ******************************************************************************************************************************/

package ispyb.server.mx.services.collections;

import java.util.List;

import ispyb.server.mx.vos.collections.BeamLineSetup3VO;

import javax.ejb.Remote;
import javax.jws.WebMethod;

@Remote
public interface BeamLineSetup3Service {

	/**
	 * Create new BeamLineSetup.
	 * 
	 * @param vo
	 *            the entity to persist
	 * @return the persisted entity
	 */
	public BeamLineSetup3VO create(final BeamLineSetup3VO vo) throws Exception;

	/**
	 * Update the BeamLineSetup data.
	 * 
	 * @param vo
	 *            the entity data to update
	 * @return the updated entity
	 */
	public BeamLineSetup3VO update(final BeamLineSetup3VO vo) throws Exception;

	/**
	 * Remove the BeamLineSetup from its pk.
	 * 
	 * @param vo
	 *            the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the BeamLineSetup.
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final BeamLineSetup3VO vo) throws Exception;

	/**
	 * Finds a BeamLineSetup entity by its primary key and set linked value objects if necessary.
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Scientist value object
	 */
	@WebMethod
	public BeamLineSetup3VO findByPk(final Integer pk) throws Exception;

	/**
	 * Find all BeamLineSetup and set linked value objects if necessary.
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	public List<BeamLineSetup3VO> findAll() throws Exception;
	
	/**
	 * Find a beamLineSetup  for a given screeningInput
	 * @param screeningInputId
	 * @param detachLight
	 * @return
	 * @throws Exception
	 */
	public BeamLineSetup3VO findByScreeningInputId(final Integer screeningInputId, final boolean detachLight) throws Exception;

}