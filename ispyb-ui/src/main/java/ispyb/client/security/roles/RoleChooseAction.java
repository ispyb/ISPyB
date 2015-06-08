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
package ispyb.client.security.roles;

import ispyb.client.common.menu.MenuContext;
import ispyb.client.security.LogonUtils;
import ispyb.common.util.Constants;

import java.util.ArrayList;
import java.util.Iterator;

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
 * RoleChooseAction
 * 
 * @struts.action name="rolesChooseForm" path="/rolesChoose" input="site.role.choose.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 */
public class RoleChooseAction extends Action {

	private final static Logger LOG = Logger.getLogger(RoleChooseAction.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		LOG.debug("Logon Action");

		ActionMessages errors = new ActionMessages();

		try {

			Integer roleId = (Integer) ((DynaValidatorForm) form).get("value");

			RoleDO role = getRole(request, roleId.intValue());

			HttpSession session = request.getSession();
			session.setAttribute(Constants.CURRENT_ROLE, role);

			String userName = request.getUserPrincipal().toString();
			String proposalType = LogonUtils.getProposalType(userName);
			MenuContext menu = new MenuContext(role.getId(), proposalType);
			session.setAttribute(Constants.MENU, menu);

			// DLS ######
			if (Constants.SITE_IS_DLS()) {
				ArrayList<RoleDO> listRoles = (ArrayList<RoleDO>) session.getAttribute(Constants.ROLES);
				listRoles.remove(role);
				listRoles.add(0, role);
				session.setAttribute(Constants.ROLES, listRoles);
			}

			// send redirect to welcome page of the unique role
			if (role.getName().equals(Constants.ROLE_USER)) {
				response.sendRedirect(request.getContextPath() + "/user/welcomePerson.do");
			} else {
				response.sendRedirect(request.getContextPath() + role.getValue());
			}

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			e.printStackTrace();
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		return null;

	}

	/**
	 * Return a Role (by id) assigned to this session
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	private RoleDO getRole(HttpServletRequest request, int id) {

		HttpSession session = request.getSession();
		ArrayList<RoleDO> listRoles = (ArrayList<RoleDO>) session.getAttribute(Constants.ROLES);
		if (listRoles == null)
			return null;
		Iterator<RoleDO> it = listRoles.iterator();

		while (it.hasNext()) {
			RoleDO role = it.next();
			if (role.getId() == id) {
				return role;
			}
		}
		return null;
	}
}
