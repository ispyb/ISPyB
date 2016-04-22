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
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import ispyb.common.util.Constants;
import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3ServiceLocal;
import ispyb.server.common.services.sessions.Session3ServiceLocal;
import ispyb.server.common.vos.proposals.Proposal3VO;
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
	
	@EJB
	private SaxsProposal3ServiceLocal proposalService;
	
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
		
		if (vo == null) 
			throw new Exception("no session has been found");
		
		if (!Constants.isAuthorisationActive() )
			return;
					
		if (isUserAdminOrLc() ) 
			return;
			
		boolean hasAccess = false;
		String user = this.getLoggedUser();		
		List<Proposal3VO> proposals = proposalService.findProposalByLoginName(user, Constants.SITE_ESRF);
		
		for (Iterator<Proposal3VO> iterator = proposals.iterator(); iterator.hasNext();) {
			Proposal3VO proposal3vo = (Proposal3VO) iterator.next();
			if ( vo.getProposalVOId().equals(proposal3vo.getProposalId()))
				hasAccess = true;
		}
		
		if (hasAccess)
			return;
		else {
			throw new AccessDeniedException("Access not authorised to session:" + vo.toString() + " for user:"+ user);
		}	

	}
	
	public String getLoggedUser() throws Exception {
		String user = "guest";
		
			LOG.debug("Authorisation : getLoggedUser: context.getCallerPrincipal=" + this.context.getCallerPrincipal().getName());
			user = this.context.getCallerPrincipal().getName();
					
		return user;
	}
	
	public boolean isUserAdminOrLc() throws Exception {

		if (this.context.isCallerInRole(Constants.ALL_MANAGE_ROLE_NAME) 
				|| this.context.isCallerInRole(Constants.ROLE_MANAGER) 
						|| this.context.isCallerInRole(Constants.ROLE_BLOM) || this.context.isCallerInRole(Constants.ROLE_LOCALCONTACT))
			return true ;
		
		return false;
	}
	

}