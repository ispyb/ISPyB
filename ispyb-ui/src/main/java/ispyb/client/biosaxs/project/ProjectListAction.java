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

package ispyb.client.biosaxs.project;

import ispyb.client.common.menu.MenuContext;
import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.server.biosaxs.services.core.experiment.Experiment3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @struts.action name="viewProjectForm" path="/user/viewProjectList" type="ispyb.client.biosaxs.project.ProjectListAction"
 *                input="user.project.create.page" validate="false" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="ProjectList" path="user.project.create.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 */

public class ProjectListAction extends org.apache.struts.actions.DispatchAction {

	private final static Logger LOG = Logger.getLogger(ProjectListAction.class);

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Experiment3Service experiment3Service;

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() {
		try {
			this.experiment3Service = (Experiment3Service) ejb3ServiceLocator.getLocalService(Experiment3Service.class);
		} catch (NamingException e) {
			LOG.error(e);
		}
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	/**
	 * display
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws IOException
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		// TODO switch to the biosaxs environment if not biosaxs, set the correct menu
		String proposalType = (String) request.getSession().getAttribute(Constants.TECHNIQUE);
		if (proposalType != null && !proposalType.equals(Constants.PROPOSAL_BIOSAXS)) {
			HttpSession session = request.getSession();
			RoleDO role = (RoleDO) session.getAttribute(Constants.CURRENT_ROLE);
			MenuContext menu = new MenuContext(role.getId(), proposalType);
			session.setAttribute(Constants.MENU, menu);
			session.setAttribute(Constants.TECHNIQUE, proposalType);
			// redirect to bx acquistions list
			response.sendRedirect(request.getContextPath()
					+ "/user/userproposalchooseaction.do?reqCode=goToProposal&userProposalType=BX&proposalId="
					+ session.getAttribute(Constants.PROPOSAL_ID));
		}

		return mapping.findForward("ProjectList");
	}
}
