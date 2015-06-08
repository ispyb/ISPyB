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
 ******************************************************************************/
package ispyb.client.common.welcome;

import ispyb.common.util.Constants;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * 
 * @struts.action path="/industrial/welcomeIndustrial"
 * 
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 * @struts.action-forward name="success" path="industrial.welcome.page"
 * 
 */
public class WelcomeIndustrialAction extends Action {
	private final Logger LOG = Logger.getLogger(WelcomeIndustrialAction.class);
	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
	
	private Proposal3Service proposalService;
	
	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {

		this.proposalService = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
	}

	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response)throws Exception {

		this.initServices();
		LOG.debug("retrieve proposal attached to industrial");

		ActionMessages errors = new ActionMessages();
		String strif = new String("if");

		Principal userPrincipal = request.getUserPrincipal();

		if (Constants.SITE_IS_ESRF() || Constants.SITE_IS_MAXIV() || Constants.SITE_IS_SOLEIL()) {
			String proposalCode = userPrincipal.toString().substring(0, 2);
			String proposalNumber = userPrincipal.toString().substring(2);
			if (Constants.SITE_IS_SOLEIL()) {
				proposalCode = userPrincipal.getName().substring(0, 2);
				proposalNumber = userPrincipal.getName().substring(2);
			}
			// String role = userPrincipal.

			// Case of MXpress users IFXnn

			if (proposalCode.equals(strif)) {
				proposalCode = userPrincipal.toString().substring(1, 3);
				proposalNumber = userPrincipal.toString().substring(3);
			}

			request.getSession().setAttribute(Constants.PROPOSAL_CODE, proposalCode);
			request.getSession().setAttribute(Constants.PROPOSAL_NUMBER, proposalNumber);

			try {
//				ArrayList proposalValues = (ArrayList) proposal.findByCodeAndNumber(proposalCode, new Integer(
//						proposalNumber));
				List<Proposal3VO> proposalValues = proposalService.findByCodeAndNumber(proposalCode, 
						proposalNumber, false, false, false);
				int sizeList = proposalValues.size();
				if (sizeList > 0) {
					Iterator<Proposal3VO> it = proposalValues.iterator();
					while (it.hasNext()) {
						Proposal3VO value = it.next();
						Integer proposalId = value.getProposalId();
						request.getSession().setAttribute(Constants.PROPOSAL_ID, proposalId);

					}
				} else {
					Integer proposalId = new Integer(0);
					request.getSession().setAttribute(Constants.PROPOSAL_ID, proposalId);

				}

			} catch (Exception e) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.welcome.username"));
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
				LOG.error(e.toString());
			}
		} else if (Constants.SITE_IS_DLS()) {
			List<Proposal3VO> allProposalValues = null;
			try {
				//allProposalValues = (ArrayList) proposal.findAll();
				allProposalValues = proposalService.findAll(false);
			} catch (Exception e) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.welcome.username"));
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
				LOG.error(e.toString());
			}

			List<Proposal3VO> proposalValues = new ArrayList<Proposal3VO>();

			// Generate the list of proposals to which the user has access
			Iterator<Proposal3VO> it0 = allProposalValues.iterator();
			while (it0.hasNext()) {
				Proposal3VO p = it0.next();
				if (request.isUserInRole(p.getCode() + "_" + p.getNumber().toString())) {
					proposalValues.add(p);
					LOG.debug("Added: " + p.getCode() + "_" + p.getNumber().toString());
				}
			}

			Integer proposalId = new Integer(-1);
			// Was the proposal ID sent as a parameter? If so, pick it up ...
			String proposalIdStr = request.getParameter(Constants.PROPOSAL_ID);
			if (proposalIdStr != null) {
				proposalId = new Integer(proposalIdStr);
				LOG.debug("Got parameter proposalId==" + proposalIdStr);
			}
			// If not, try to pick it up from the session's attribs. (Meaning it's been set already.)
			else {
				Object pObj = request.getSession().getAttribute(Constants.PROPOSAL_ID);
				if (pObj instanceof Integer)
					proposalId = (Integer) pObj;
				// If not set already, try to use the first proposal in the list
				else if (proposalValues.size() > 0) {
					Proposal3VO value = (Proposal3VO) proposalValues.get(0);
					proposalId = value.getProposalId();
				} else
					// If list is empty, then give up and return error
					return (mapping.findForward("error"));
			}

			// Find the proposalId in the proposalValues list
			String proposalCode = null;
			String proposalNumber = null;
			it0 = proposalValues.iterator();
			int sizeList = proposalValues.size();
			if (sizeList > 0) {
				Proposal3VO value = null;
				while (it0.hasNext()) {
					Proposal3VO p =  it0.next();
					Integer pId = p.getProposalId();

					if (pId == proposalId) {
						proposalCode = p.getCode();
						proposalNumber = p.getNumber().toString();

						request.getSession().setAttribute(Constants.PROPOSAL_ID, pId);
						request.getSession().setAttribute(Constants.PROPOSAL_CODE, proposalCode);
						request.getSession().setAttribute(Constants.PROPOSAL_NUMBER, proposalNumber);
						break;
					}
				}
			} else {
				Integer pId = new Integer(0);
				request.getSession().setAttribute(Constants.PROPOSAL_ID, pId);
			}
		}else if (Constants.SITE_IS_EMBL()) {
			String proposalCode = userPrincipal.toString().substring(0, 2);
			String proposalNumber = userPrincipal.toString().substring(2);
			// String role = userPrincipal.

			// Case of MXpress users IFXnn

			if (proposalCode.equals(strif)) {
				proposalCode = userPrincipal.toString().substring(1, 3);
				proposalNumber = userPrincipal.toString().substring(3);
			}

			request.getSession().setAttribute(Constants.PROPOSAL_CODE, proposalCode);
			request.getSession().setAttribute(Constants.PROPOSAL_NUMBER, proposalNumber);

			try {
//				ArrayList proposalValues = (ArrayList) proposal.findByCodeAndNumber(proposalCode, new Integer(
//						proposalNumber));
				List<Proposal3VO> proposalValues = proposalService.findByCodeAndNumber(proposalCode, 
						proposalNumber, false, false, false);
				int sizeList = proposalValues.size();
				if (sizeList > 0) {
					Iterator<Proposal3VO> it = proposalValues.iterator();
					while (it.hasNext()) {
						Proposal3VO value = it.next();
						Integer proposalId = value.getProposalId();
						request.getSession().setAttribute(Constants.PROPOSAL_ID, proposalId);

					}
				} else {
					Integer proposalId = new Integer(0);
					request.getSession().setAttribute(Constants.PROPOSAL_ID, proposalId);

				}

			} catch (Exception e) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.welcome.username"));
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
				LOG.error(e.toString());
			}
		}
		

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		return mapping.findForward("success");
	}

}
