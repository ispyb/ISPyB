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
 * Created on Aug 27, 2004 - 2:53:20 PM
 *
 * Ricardo LEAL
 * ESRF - The European Synchrotron Radiation Facility
 * BP220 - 38043 Grenoble Cedex - France
 * Phone: 00 33 (0)4 38 88 19 59
 * ricardo.leal@esrf.fr
 *
 */
package ispyb.client.security;

import ispyb.client.common.menu.MenuContext;
import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.server.common.services.config.MenuGroup3Service;
import ispyb.server.common.util.AdminUtils;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.config.MenuGroup3VO;

import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.struts.action.ActionServlet;

/**
 */
public class SpecificActionServlet extends ActionServlet {
	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private MenuGroup3Service menuGroupService;

	public static String contextPath = null;

	private final static Logger LOG = Logger.getLogger(SpecificActionServlet.class);
	
	private static final String LOG_USER_ID = "userId";

	/**
	 * Process a servlet request.
	 * 
	 * @param request
	 *            The HTTP request we are processing.
	 * @param response
	 *            The HTTP response we are creating.
	 */
	@Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			java.io.IOException {

		try {
			HttpSession session = request.getSession();

			// Track user activity
			String username = "";
			if (request.getUserPrincipal() != null)
				username = request.getUserPrincipal().toString();
			if (username != null && !username.equals("")
					&& !request.getRequestURI().equals("/ispyb/ispyb/welcomeGuestPage.do"))
				AdminUtils.logActivity(username, Constants.STATUS_LOGON, request.getRequestURI());

			String myRole = "";
			RoleDO currentRole = (RoleDO) session.getAttribute(Constants.CURRENT_ROLE);
			if (currentRole != null)
				myRole = currentRole.getName();
			else
				myRole = "none";

			// LOG.debug("Request page: " + request.getRequestURI() + " by '" + username + "' as '" + myRole + "'.");
			// Roles and menus
			if (session.getAttribute(Constants.CURRENT_ROLE) == null) {
				LOG.debug("First Guest access...");

				RoleDO role = getRoleDO(Constants.DEFAULT_ROLE_NAME);

				// guest user
				if (session.getAttribute(Constants.MENU) == null) {
					LOG.debug("Creating menu for guest...");

					// build menu if it hasn't been created
					MenuContext menu;
					if (username.isEmpty()) {
						menu = new MenuContext(role.getId(), null);
					} else {
						String proposalType = LogonUtils.getProposalType(username);

						menu = new MenuContext(role.getId(), proposalType);
					}
					session.setAttribute(Constants.MENU, menu);
				}
				session.setAttribute(Constants.CURRENT_ROLE, role);


				if (request.getRequestURI().equals("/ispyb/ispyb/welcomeUnauthorizedPage.do"))
					// Unauthorized page
					response.sendRedirect(request.getRequestURI());
				else
					// Welcome page
					response.sendRedirect(request.getContextPath() + role.getValue());
			}
			MDC.put(LOG_USER_ID, username);
			super.process(request, response);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param name
	 * @return
	 */
	private RoleDO getRoleDO(String name) {
		try {
			this.menuGroupService = (MenuGroup3Service) ejb3ServiceLocator.getLocalService(MenuGroup3Service.class);
			List<MenuGroup3VO> list = menuGroupService.findFiltered(name);

			// ArrayList list = (ArrayList) menuGroup.findByName(name);

			Iterator it = list.iterator();

			RoleDO role = new RoleDO();
			if (it.hasNext()) {
				MenuGroup3VO menuGroupValue = (MenuGroup3VO) it.next();
				role.setId(menuGroupValue.getMenuGroupId().intValue());
				role.setName(menuGroupValue.getName());
				role.setValue(menuGroupValue.getWelcomePage());
			}

			return role;
		} catch (Exception e) {
			LOG.error(e.toString());
			e.printStackTrace();
			return null;
		}
	}

}
