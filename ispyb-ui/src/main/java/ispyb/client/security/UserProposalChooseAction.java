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
 ******************************************************************************************************************************/

package ispyb.client.security;

import ispyb.client.biosaxs.dataAdapter.BiosaxsDataAdapterCommon;
import ispyb.client.common.menu.MenuContext;
import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.server.common.vos.proposals.Proposal3VO;

import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.google.gson.GsonBuilder;

/**
 * @struts.action name="UserProposalChooseAction" path="/user/userproposalchooseaction"
 *                type="ispyb.client.security.UserProposalChooseAction" validate="false" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="success" path="user.welcome.page"
 */
public class UserProposalChooseAction extends org.apache.struts.actions.DispatchAction {
	private final static Logger LOGGER = Logger.getLogger(UserProposalChooseAction.class);

	protected static Calendar NOW = GregorianCalendar.getInstance();

	private void initServices() {

	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	/**
	 * getProposalList
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward getProposalList(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {

			/** Updating from SMIS **/
			HttpSession session = request.getSession();
			// @SuppressWarnings("unchecked")
			List<Proposal3VO> proposals = (List<Proposal3VO>) session.getAttribute(Constants.PROPOSALS);
			String json = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create().toJson(proposals);
			BiosaxsDataAdapterCommon.sendResponseToclient(response, json);
		} catch (Exception exp) {
			response.getWriter().write(exp.getMessage() + "  " + exp.getCause());
			response.setStatus(500);
		}
		return null;

	}

	/**
	 * goToProposal
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward goToProposal(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			int proposalId = Integer.parseInt(request.getParameter("proposalId"));
			String proposalType = request.getParameter("userProposalType");
			HttpSession session = request.getSession();
			List<Proposal3VO> proposals = (List<Proposal3VO>) session.getAttribute("Proposals");

			for (Proposal3VO proposal3vo : proposals) {
				if (proposal3vo.getProposalId().equals(proposalId)) {
					session.setAttribute(Constants.PROPOSAL_ID, proposal3vo.getProposalId());
					session.setAttribute(Constants.PROPOSAL_NUMBER, proposal3vo.getNumber());
					session.setAttribute(Constants.PROPOSAL_CODE, proposal3vo.getCode());

					RoleDO role = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
					MenuContext menu = new MenuContext(role.getId(), proposalType);
					session.setAttribute(Constants.MENU, menu);
					session.setAttribute(Constants.TECHNIQUE, proposalType);

					// send redirect to welcome page of the unique role
					// response.sendRedirect(request.getContextPath() + role.getValue());
					return mapping.findForward("success");
				}
			}

		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return null;

	}
}
