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
 * 
 */
package ispyb.client.common.proposal;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.common.util.Constants;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

/**
 * 
 * 
 * @struts.action name="viewProposalForm" path="/fedexmanager/viewProposal" input="fedexmanager.welcome.page"
 *                validate="false" parameter="reqCode" scope="request"
 * @struts.action-forward name="error" path="site.default.error.page"
 * @struts.action-forward name="success" path="fedexmanager.proposal.view.page"
 * 
 * 
 */
public class ViewFXProposalAction extends DispatchAction {

	/**
	 * To display all the Sessions belonging to a proposalId.
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {

		ActionMessages errors = new ActionMessages();

		try {

			ViewProposalForm form = (ViewProposalForm) actForm;

			// Retrieve proposals from DB
			Proposal3Service proposalService = (Proposal3Service) Ejb3ServiceLocator.getInstance().getLocalService(
					Proposal3Service.class);

			List<Proposal3VO> proposalList = proposalService.findByCodeAndNumber(Constants.PROPOSAL_CODE_FX, null,
					false/* fetchSessions */, false/* fetchProteins */, false/* detachLight */);

			// Populate with Info
			form.setListInfo(proposalList);
			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.fedexmanager.proposal.view"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		} else
			return mapping.findForward("success");
	}
}
