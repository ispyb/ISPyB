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

import java.nio.file.AccessDeniedException;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import ispyb.server.common.services.sessions.Session3ServiceLocal;
import ispyb.server.mx.vos.collections.Session3VO;


/**
 * <p>
 * This session bean handles authorisation access.
 * </p>
 */
@Stateless
public class AuthorisationServiceBean implements AuthorisationService, AuthorisationServiceLocal {

	private final static Logger LOG = Logger.getLogger(AuthorisationServiceBean.class);

	@Resource
	private SessionContext context;
	
	@EJB
	private Session3ServiceLocal sessionService;
	
	/**
	 * 
	 * @param sessionVO
	 * @return
	 * @throws AccessDeniedException
	 *             if the user has no access right
	 * @throws Exception
	 */
	public void checkUserRightToAccessSession( Session3VO vo )
			throws Exception{
//		Integer userPk = this.getUserPk();		
//		if (vo == null) throw new Exception("no session has been found");
//		if (! vo.getProposalVOId().equals(userPk)){
//			throw new Exception("Access not authorised to session:" + vo.toString() + " for user:"+ userPk);
//		}		
		return;
	}
	
	@PermitAll
	// needed e.g. for unlocking BAG report when session ends
	public Integer getUserPk() throws Exception {
		Integer userPk = new Integer(0);
		try {
			LOG.debug("Authorisation : getUserPk: context.getCallerPrincipal=" + this.context.getCallerPrincipal().getName());
			userPk = Integer.valueOf(this.context.getCallerPrincipal().getName());
		} catch (NumberFormatException e) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("getUserPk: context.getCallerPrincipal=" + this.context.getCallerPrincipal().getName()
						+ " can NOT be parsed as primary key!");
			}
		}
		return userPk;
	}
	

}