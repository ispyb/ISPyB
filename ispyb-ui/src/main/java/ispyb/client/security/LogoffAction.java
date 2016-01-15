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

import ispyb.common.util.Constants;
import ispyb.server.common.util.AdminUtils;

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

/**
 * 
 * @struts.action path="/logoff"
 * 
 * @struts.action-forward name="success" path="/welcomeGuestPage.do"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 * 
 */
public class LogoffAction extends Action {

	private final static Logger LOG = Logger.getLogger(LogoffAction.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		LOG.debug("execute()");

		ActionMessages errors = new ActionMessages();

		HttpSession session = request.getSession();

		// Track user activity
		String username = "";
		if (request.getUserPrincipal() != null)
			username = request.getUserPrincipal().getName();
		if (username != null && !username.equals(""))
			AdminUtils.logActivity(username, Constants.STATUS_LOGOFF, request.getRequestURI());

		LOG.info("Logoff by '" + username + "'.");

		try {
			// Process this user logoff
			session.removeAttribute(Constants.ROLES);
			session.removeAttribute(Constants.CURRENT_ROLE);
			session.removeAttribute(Constants.MENU);
			session.invalidate();

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			e.printStackTrace();
			session.invalidate();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		if (Constants.SITE_IS_SOLEIL()) {
			return new ActionForward(Constants.USER_PORTAL_URL, true);
		}
		
		return mapping.findForward("success");
	}
}
