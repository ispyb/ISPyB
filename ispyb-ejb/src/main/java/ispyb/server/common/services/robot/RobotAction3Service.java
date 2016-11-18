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

package ispyb.server.common.services.robot;

import java.util.List;

import javax.ejb.Remote;

import ispyb.server.common.vos.robot.RobotAction3VO;

@Remote
public interface RobotAction3Service {
	
	public abstract RobotAction3VO merge(RobotAction3VO detachedInstance);

	/**
	 * Remove the RobotAction3 from its pk.
	 * 
	 * @param vo
	 *            the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the RobotAction3.
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final RobotAction3VO vo) throws Exception;

	/**
	 * Finds a RobotAction3 entity by its primary key and set linked value objects if necessary.
	 * 
	 * @param pk
	 *            the primary key
	 * @return the value object
	 */
	public RobotAction3VO findByPk(final Integer pk) throws Exception;
	
	/**
	 * Find a list of RobotAction3s for a specified sessionId
	 * 
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public List<RobotAction3VO> findBySession(Integer sessionId) throws Exception ;
	

}
