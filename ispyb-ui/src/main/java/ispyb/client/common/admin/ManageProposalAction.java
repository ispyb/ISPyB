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
/*
 * ManageProposalAction
 * @author ludovic.launer@esrf.fr
 * Nov 13, 2006
 */

package ispyb.client.common.admin;

import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * @struts.action name="manageProposalForm" path="/user/manageProposalAction"
 *                type="ispyb.client.common.admin.ManageProposalAction" input="user.admin.proposal.manage.page"
 *                validate="false" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="manageProposalPage" path="user.admin.proposal.manage.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 */
public class ManageProposalAction extends org.apache.struts.actions.DispatchAction {

	static Category Log = Category.getInstance(ManageProposalAction.class.getName()); // Initialize a Category for Log4J

	/**
	 * display
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		return mapping.findForward("manageProposalPage");
	}

	public ActionForward reassignProposals(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		ActionMessages messages = new ActionMessages();
		ActionMessages errors = new ActionMessages();
		try {

			Proposal3Service propService = (Proposal3Service) Ejb3ServiceLocator.getInstance().getLocalService(
					Proposal3Service.class);

			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			ManageProposalForm form = (ManageProposalForm) actForm; // Parameters submitted by form
			String oldProposals = form.getOldProposals();
			String newProposals = form.getNewProposals();
			// ---------------------------------------------------------------------------------------------------
			// Parse Proposal
			Pattern hunter = Pattern.compile("\r\n", Pattern.CASE_INSENSITIVE);
			String[] partsOldNames = hunter.split(oldProposals);
			String[] partsNewNames = hunter.split(newProposals);

			if (partsOldNames.length != partsNewNames.length)
				throw new Exception("Number of old names <> number of new names.");
			try {
				for (int p = 0; p < partsOldNames.length; p++) {
					String oldProposalName = partsOldNames[p];
					String newProposalName = partsNewNames[p];

					Integer[] nbUp = propService.updateProposalFromDesc(newProposalName, oldProposalName);

					messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free", oldProposalName
							+ " --> " + newProposalName));
				}
			} catch (Exception e) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free", e.getMessage()));
			}

			saveMessages(request, messages);
			saveErrors(request, errors);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.getMessage()));
			e.printStackTrace();
			saveErrors(request, errors);
			return mapping.findForward("error");
		}

		return mapping.findForward("manageProposalPage");
	}

}
