/*
 * LogonAction.java
 * @author patrice.brenchereau@esrf.fr
 * July 8, 2008
 */

package bcr.client.security;

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

import bcr.client.util.ClientLogger;
import bcr.common.util.Constants;

/**
 * When this page is called the JBoss Authentification has been successfull
 * 
 * @struts.action path="/security/logon"
 * 
 * @struts.action-forward name="error" path="bcr.default.error.page"
 * 
 * @struts.action-forward name="logonError" path="bcr.logon.error.page"
 * 
 * @struts.action-forward name="roleError" path="bcr.role.error.page"
 * 
 * @struts.action-forward name="logoffPage" path="/logoff"
 * 
 */
public class LogonAction extends Action {

	private static Logger LOG = Logger.getLogger(LogonAction.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		LOG.info("Logon Action");
		ActionMessages errors = new ActionMessages();

		try {
			String username = request.getUserPrincipal().toString();
			//ClientLogger.Log4Stat("LOGON", username, "");
			HttpSession session = request.getSession();
			String userRole = "";
			;

			if (request.isUserInRole("Store")) {
				userRole = "Store";
			} else {
				// Wrong role : disconnect
				session.removeAttribute(Constants.PERSON_LOGIN);
				session.removeAttribute(Constants.CURRENT_ROLE);
				session.invalidate();
				return (mapping.findForward("roleError"));

			}

			session.setAttribute(Constants.PERSON_LOGIN, username);
			session.setAttribute(Constants.CURRENT_ROLE, userRole);

			LOG.info("Username 	: " + username);
			LOG.debug("Role 		: " + userRole);
			LOG.debug("SessionId	: " + session.getId());

			response.sendRedirect(request.getContextPath() + "/user/chooseappli.do");

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
