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

package ispyb.server.common.services;

import javax.ejb.Remote;

import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.sample.Protein3VO;

@Remote
public interface AuthorisationService {
	
	/**
	 * 
	 * @param sessionVO
	 * @return
	 * @throws AccessDeniedException
	 *             if the user has no access right
	 * @throws Exception
	 */
	public void checkUserRightToAccessSession( Session3VO vo )
			throws AccessDeniedException;
	
	public void checkUserRightToAccessProposal( Proposal3VO vo )
			throws AccessDeniedException;
	
	public void checkUserRightToAccessProtein( Protein3VO vo )
			throws AccessDeniedException;

}