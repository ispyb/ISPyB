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
package ispyb.client.common.proposal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.smis.UpdateFromSMIS;

/**
 * @struts.action name="proposalAndSessionAndProteinForm" path="/updateDB"
 *                type="ispyb.client.common.proposal.UpdateProposalAndSessionAndProteinFromWS" validate="false" parameter="reqCode"
 *                scope="request"
 * 
 * @struts.action-forward name="updateISPYBdbPage" path="updateISPYBdb.page"
 * @struts.action-forward name="updateFedexManagerISPYBdbPage" path="fedexmanager.updateISPYBdb.page"
 * @struts.action-forward name="updateManagerISPYBdbPage" path="manager.updateISPYBdb.page"
 * @struts.action-forward name="updateLocalContactISPYBdbPage" path="localcontact.updateISPYBdb.page"
 * 
 * @struts.action-forward name="userUpdateISPYBdbPage" path="user.updateISPYBdb.page"
 * 
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 */

public class UpdateProposalAndSessionAndProteinFromWS extends org.apache.struts.actions.DispatchAction {

	private final static Logger LOG = Logger.getLogger(UpdateProposalAndSessionAndProteinFromWS.class);

	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {

		// return mapping.findForward("updateISPYBdbPage");
		return redirectPageFromRole(mapping, request);
	}

	public ActionForward userDisplay(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		return mapping.findForward("userUpdateISPYBdbPage");
		// return redirectPageFromRole(mapping, request);
	}

	public ActionForward updateProposal(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();

		String proposalCode = "";
		String proposalNumber = null;

		ProposalAndSessionAndProteinForm form = (ProposalAndSessionAndProteinForm) actForm;

		if (form.getProposalCode() != null && form.getProposalNumberSt() != null) {
			// Update is called from fxmanage pages, the form is informed
			proposalCode = form.getProposalCode();
			if (form.getProposalNumberSt() == null || form.getProposalNumberSt().length() == 0)
				proposalNumber = null;
			else
				proposalNumber = form.getProposalNumberSt();
			form.setUserIsManager(true);
		} else {
			// Update is called from the user pages, the proposal info is stored in Session
			if (request.getSession().getAttribute(Constants.PROPOSAL_CODE) != null) {
				proposalCode = ((String) request.getSession().getAttribute(Constants.PROPOSAL_CODE)).toUpperCase();
				proposalNumber = ((String) request.getSession().getAttribute(Constants.PROPOSAL_NUMBER));
			}
			form.setUserIsManager(false);
		}

		if (proposalCode == null || proposalCode.length() == 0)
			proposalCode = "";

		updateThisProposal(errors, messages, proposalCode, proposalNumber);

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		if (!messages.isEmpty())
			saveMessages(request, messages);
		LOG.debug("Update of ISPyB for proposal is finished");
		// return mapping.findForward("updateISPYBdbPage");
		return redirectPageFromRole(mapping, request);
	}

	/**
	 * 
	 * @param errors
	 * @param messages
	 * @param proposalCode
	 * @param proposalNumber
	 */
	public static void updateThisProposal(ActionMessages errors, ActionMessages messages, String proposalCode, String proposalNumber) {

		String uoCode = StringUtils.getUoCode(proposalCode);

		LOG.debug("update ISPyB database for proposal: " + proposalCode + proposalNumber + " --- uoCode: " + uoCode);

		try {

			if (proposalNumber == null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Please fill the proposal number"));
			} else {
				Long proposalNumberInt = null;
				try {
					proposalNumberInt = Long.parseLong(proposalNumber);
				} catch (NumberFormatException e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.detail", "The proposal number is not a number"));
				}
				if (proposalNumberInt != null) {
					UpdateFromSMIS.updateProposalFromSMIS(uoCode,proposalNumber);
					messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.inserted", "proposal data"));
				}
			}

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward update(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();

		try {
			ProposalAndSessionAndProteinForm form = (ProposalAndSessionAndProteinForm) actForm;

			Date today = Calendar.getInstance().getTime();
			SimpleDateFormat simple = new SimpleDateFormat();
			simple.applyPattern("dd/MM/yyyy");

			String startDateStr = form.getStartDate();
			if (startDateStr == null || startDateStr.length() == 0) {
				startDateStr = simple.format(today);
			}
			String endDateStr = form.getEndDate();
			if (endDateStr == null || endDateStr.length() == 0) {
				endDateStr = simple.format(today);
			}

			// retrieve all new proposals
			
			LOG.debug("update ISPyB database between startDate = " + startDateStr + "  and endDate =" + endDateStr);
			UpdateFromSMIS.updateFromSMIS(startDateStr, endDateStr);

			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.inserted", " new sessions + proteins"));

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.sample.viewProteinFromSS"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}
		if (!messages.isEmpty())
			saveMessages(request, messages);

		LOG.info("Update of ISPyB is finished");
		// return mapping.findForward("updateISPYBdbPage");
		return redirectPageFromRole(mapping, request);
	}

	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward launchBatchUpdate(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();

		try {
			UpdateFromSMIS.updateFromSMIS();

			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.inserted", " new sessions + proteins"));

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.sample.viewProteinFromSS"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}
		if (!messages.isEmpty())
			saveMessages(request, messages);

		LOG.info("Update of ISPyB is finished");
		// return mapping.findForward("updateISPYBdbPage");
		return redirectPageFromRole(mapping, request);
	}
	private ActionForward redirectPageFromRole(ActionMapping mapping, HttpServletRequest request) {
		// redirection page according to the Role ------------------------
		// retrieves role from session -----------------------------------
		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();
		if (role.equals(Constants.FXMANAGE_ROLE_NAME)) {
			return mapping.findForward("updateFedexManagerISPYBdbPage");
		} else if (role.equals(Constants.ROLE_MANAGER)) {
			return mapping.findForward("updateManagerISPYBdbPage");
		} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
			return mapping.findForward("updateLocalContactISPYBdbPage");
		} else
			return mapping.findForward("updateISPYBdbPage");

	}
}
