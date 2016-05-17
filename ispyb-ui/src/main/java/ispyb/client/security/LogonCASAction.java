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
package ispyb.client.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

import ispyb.client.common.menu.MenuContext;
import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.server.common.services.config.MenuGroup3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.config.MenuGroup3VO;
import ispyb.server.security.EmployeeVO;
import ispyb.server.security.LdapConnection;

/**
 * When this page is called the JBoss Authentification has been successfull
 * 
 * @struts.action path="/security/logonCAS"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 */
public class LogonCASAction extends Action {

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private MenuGroup3Service menuGroupService;

	private final static Logger LOG = Logger.getLogger(LogonCASAction.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		LOG.debug("LogonCAS Action");

		ActionMessages errors = new ActionMessages();

		try {
			// --- Log ---
			String userName = request.getUserPrincipal().toString();
			EmployeeVO employee = LdapConnection.findByUniqueIdentifier(userName);
			String userGivenName = employee.getGivenName();
			String userLastName = employee.getSn();
			String userEmail = employee.getMail();

			LOG.debug("Logon: user = " + userName + " (" + userGivenName + " " + userLastName + ") at " + userEmail);

			// -----------
			HttpSession session = request.getSession();
			ArrayList userRoles = getUserRoles(request);
			session.setAttribute(Constants.ROLES, userRoles);
			session.setAttribute(Constants.LDAP_GivenName, userGivenName);
			session.setAttribute(Constants.LDAP_LastName, userLastName);
			session.setAttribute(Constants.LDAP_Email, userEmail);

			LOG.debug("SessionId: " + session.getId());

			if (userRoles.size() > 1) {
				// forward to page where user can select which Role he want to use
				response.sendRedirect(request.getContextPath() + "/roleChoosePage.do");

			} else {
				// create the menu and
				// forward to Welcome page belonging to the unique role

				RoleDO currentRole = (RoleDO) userRoles.get(0);
				session.setAttribute(Constants.CURRENT_ROLE, userRoles.get(0));
				String proposalType = LogonUtils.getProposalType(userName);

				MenuContext menu = new MenuContext(currentRole.getId(), proposalType);
				session.setAttribute(Constants.MENU, menu);

				// send redirect to welcome page of the unique role
				LOG.debug("user role is : " + currentRole.getName());
				LOG.debug("server name is : " + request.getServerName());
				response.sendRedirect(request.getContextPath() + currentRole.getValue());

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
	 * Get all Groups from DB
	 * 
	 * @return
	 */
	protected ArrayList getGroups() {

		try {

			this.menuGroupService = (MenuGroup3Service) ejb3ServiceLocator.getLocalService(MenuGroup3Service.class);
			List<MenuGroup3VO> groupList = menuGroupService.findAll(false);

			Iterator it = groupList.iterator();
			ArrayList returnList = new ArrayList();

			while (it.hasNext()) {
				RoleDO role = new RoleDO();
				MenuGroup3VO menuGroupValue = (MenuGroup3VO) it.next();
				role.setName(menuGroupValue.getName());
				role.setValue(menuGroupValue.getWelcomePage());
				role.setId(menuGroupValue.getMenuGroupId().intValue());
				returnList.add(role);
			}
			return returnList;

		} catch (Exception e) {
			LOG.error(e.toString());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get All roles which this user belongs
	 * 
	 * @param request
	 * @return
	 */
	protected ArrayList getUserRoles(HttpServletRequest request) {

		ArrayList groups = getGroups();
		Iterator it = groups.iterator();

		ArrayList userRoles = new ArrayList();
		while (it.hasNext()) {
			RoleDO role = (RoleDO) it.next();

			if (request.isUserInRole(role.getName())) {
				userRoles.add(role);

			}
		}
		return userRoles;
	}

}
