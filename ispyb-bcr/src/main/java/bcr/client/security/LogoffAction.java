/*
 * LogOffAction.java
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

import bcr.common.util.Constants;

/**
 * 
 * @struts.action path="/logoff"
 * 
 * @struts.action-forward name="success" path="/welcomeGuestPage"
 * 
 * @struts.action-forward name="error" path="bcr.default.error.page"
 * 
 * 
 */
public class LogoffAction extends Action {

	private static Logger LOG = Logger.getLogger(LogoffAction.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		LOG.info("Logout Action");

		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession();
		try {
			session.removeAttribute(Constants.PERSON_LOGIN);
			session.removeAttribute(Constants.CURRENT_ROLE);
			session.invalidate();
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Erreur de deconnexion"));
			// errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			e.printStackTrace();
			session.invalidate();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		return mapping.findForward("success");
	}
}
