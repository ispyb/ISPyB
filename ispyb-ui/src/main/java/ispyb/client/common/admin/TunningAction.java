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
 * TunningAction
 * @author ludovic.launer@esrf.fr
 * 2008
 */

package ispyb.client.common.admin;

import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.proposals.Proposal3VO;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @struts.action name="tunningForm" path="/user/tunningAction" type="ispyb.client.common.admin.TunningAction" input="user.admin.tunning.page"
 *                validate="false" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="tunningPage" path="user.admin.tunning.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 */

public class TunningAction extends org.apache.struts.actions.DispatchAction {

	static Category Log = Category.getInstance(TunningAction.class.getName()); // Initialize a Category for Log4J

	/**
	 * display
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse in_reponse) {
		if (!request.getUserPrincipal().toString().equalsIgnoreCase("ehtpx1"))
			return null;
		return mapping.findForward("tunningPage");
	}

	public ActionForward changeCurrentProposal(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			TunningForm form = (TunningForm) actForm; // Parameters submited by form
			String newProposalCode = form.getProposalCode();
			String newProposalNumber = form.getProposalNumber();
			// ---------------------------------------------------------------------------------------------------

			request.getSession().setAttribute(Constants.PROPOSAL_CODE, newProposalCode);
			request.getSession().setAttribute(Constants.PROPOSAL_NUMBER, newProposalNumber);

			ArrayList authenticationInfo = StringUtils.GetProposalNumberAndCode(newProposalCode + newProposalNumber);
			String auth_proposalCode = (String) authenticationInfo.get(0);
			String auth_prefix = (String) authenticationInfo.get(1);
			String auth_proposalNumber = (String) authenticationInfo.get(2);

			Proposal3VO proposal = ispyb.common.util.DBTools.getProposal(auth_proposalCode, auth_proposalNumber);
			Integer proposalId = proposal.getProposalId();

			request.getSession().setAttribute(Constants.PROPOSAL_ID, proposalId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapping.findForward("tunningPage");
	}

}
