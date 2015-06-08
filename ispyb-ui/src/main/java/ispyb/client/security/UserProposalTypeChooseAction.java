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
 * Created on Nov 30, 2004
 *
 * Ricardo LEAL
 * ESRF - European Synchrotron Radiation Facility
 * B.P. 220
 * 38043 Grenoble C�dex - France
 * Phone: 00 33 (0)4 38 88 19 59
 * Fax: 00 33 (0)4 76 88 25 42
 * ricardo.leal@esrf.fr
 */
package ispyb.client.security;

import ispyb.client.common.menu.MenuContext;
import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * UserProposalTypeChooseAction
 * 
 * @struts.action name="userProposalTypeChooseForm" path="/userProposalTypeChoose" input="site.proposalType.choose.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 */
public class UserProposalTypeChooseAction extends Action {

	private final static Logger LOG = Logger.getLogger(UserProposalTypeChooseAction.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		LOG.debug("UserProposalTypeChooseAction");

		ActionMessages errors = new ActionMessages();

		try {
			HttpSession session = request.getSession();
			String userProposalType = (String) ((DynaValidatorForm) form).get("value");
			RoleDO role = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
			MenuContext menu = new MenuContext(role.getId(), userProposalType);
			session.setAttribute(Constants.MENU, menu);

			session.setAttribute(Constants.TECHNIQUE, userProposalType);
			
			// send redirect to welcome page of the unique role
			LOG.debug("user role is : " + role.getName());
			LOG.debug("server name is : " + request.getServerName());
			response.sendRedirect(request.getContextPath() + role.getValue());

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			e.printStackTrace();
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		return null;

	}

}
