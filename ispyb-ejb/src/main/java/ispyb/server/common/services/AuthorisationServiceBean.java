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

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import ispyb.common.util.Constants;
import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.services.proposals.Proposal3ServiceLocal;
import ispyb.server.common.services.sessions.Session3ServiceLocal;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.services.sample.BLSample3ServiceLocal;
import ispyb.server.mx.services.sample.Protein3ServiceLocal;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.Protein3VO;


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
	private Proposal3ServiceLocal proposalService;
	
	@EJB
	private Protein3ServiceLocal proteinService;
	
	@EJB
	private BLSample3ServiceLocal sampleService;
	
	/**
	 * 
	 * @param sessionVO
	 * @return
	 * @throws AccessDeniedException
	 *             if the user has no access right
	 * @throws Exception
	 */
	public void checkUserRightToAccessSession( Session3VO vo )
			throws AccessDeniedException, NullPointerException{
				
		if (vo == null) 
			throw new NullPointerException("no session has been found");
		
		if (!Constants.isAuthorisationActive() )
			return;
		
		if (isUserAdminOrLcOrWs() )
			return;
								
		boolean hasAccess = false;
		String user = this.getLoggedUser();
		
		//TODO	
		//find a way to authorise calls from rest webservices here we authorise anonymous !!!
		if (user.equalsIgnoreCase("anonymous"))
			return;
		
		List<Proposal3VO> proposals = proposalService.findProposalByLoginName(user, Constants.SITE_ESRF);
		
		if (Constants.SITE_IS_EMBL())
		{
			proposals = proposalService.findProposalByLoginName(user, Constants.SITE_EMBL);
		}
		
		for (Iterator<Proposal3VO> iterator = proposals.iterator(); iterator.hasNext();) {
			Proposal3VO proposal3vo = (Proposal3VO) iterator.next();
			if ( vo.getProposalVOId().equals(proposal3vo.getProposalId()))
				hasAccess = true;
		}
				
		if (hasAccess)
			return;
		else
			throw new AccessDeniedException("Access not authorised to session:" + vo.toString() + " for user:"+ user);
	}
	
	public void checkUserRightToAccessProposal( Proposal3VO vo)
			throws AccessDeniedException, NullPointerException{
				
		if (vo == null) 
			throw new NullPointerException("no proposal selected ");
		
		if (!Constants.isAuthorisationActive() )
			return;
		
		if (isUserAdminOrLcOrWs() )
			return;
								
		boolean hasAccess = false;
		String user = this.getLoggedUser();
						
		//TODO	
		//find a way to authorise calls from rest webservices here we authorise anonymous !!!
		if (user.equalsIgnoreCase("anonymous"))
			return;
		
		List<Proposal3VO> proposals = proposalService.findProposalByLoginName(user, Constants.SITE_ESRF);
		
		for (Iterator<Proposal3VO> iterator = proposals.iterator(); iterator.hasNext();) {
			Proposal3VO proposal3vo = (Proposal3VO) iterator.next();
			if ( vo.getProposalId().equals(proposal3vo.getProposalId()))
				hasAccess = true;
		}
				
		if (hasAccess)
			return;
		else
			throw new AccessDeniedException("Access not authorised to proposal:" + vo.toString() + " for user:"+ user);
	}

	public void checkUserRightToAccessProtein( Protein3VO vo)
			throws AccessDeniedException, NullPointerException{
				
		if (vo == null) 
			throw new NullPointerException("no protein selected ");
		
		if (!Constants.isAuthorisationActive() )
			return;
		
		if (isUserAdminOrLcOrWs() )
			return;
								
		boolean hasAccess = false;
		String user = this.getLoggedUser();
						
		//TODO	
		//find a way to authorise calls from rest webservices here we authorise anonymous !!!
		if (user.equalsIgnoreCase("anonymous"))
			return;
		
		List<Proposal3VO> proposals = proposalService.findProposalByLoginName(user, Constants.SITE_ESRF);
		
		for (Iterator<Proposal3VO> iterator = proposals.iterator(); iterator.hasNext();) {
			Proposal3VO proposal3vo = (Proposal3VO) iterator.next();
			if ( vo.getProposalVOId().equals(proposal3vo.getProposalId()))
				hasAccess = true;
		}
				
		if (hasAccess)
			return;
		else
			throw new AccessDeniedException("Access not authorised to protein:" + vo.toString() + " for user:"+ user);
	}
	
	public String getLoggedUser()  {
		String user = "guest";
		
			LOG.debug("Authorisation : getLoggedUser: context.getCallerPrincipal=" + this.context.getCallerPrincipal().getName());
			user = this.context.getCallerPrincipal().getName();
					
		return user;
	}
	
	public boolean isUserAdminOrLcOrWs() {
				
		if (this.context.isCallerInRole(Constants.ALL_MANAGE_ROLE_NAME) 
				|| this.context.isCallerInRole(Constants.ROLE_MANAGER) 
						|| this.context.isCallerInRole(Constants.ROLE_BLOM) || this.context.isCallerInRole(Constants.ROLE_LOCALCONTACT)
						|| this.context.isCallerInRole(Constants.ROLE_WS) || this.context.isCallerInRole(Constants.ROLE_STORE))
			return true ;
		
		return false;
	}
	

}