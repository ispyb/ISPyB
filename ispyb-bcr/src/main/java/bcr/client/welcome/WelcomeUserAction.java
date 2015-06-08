package bcr.client.welcome;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @struts.action path="/user/welcomeUser"
 * 
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 * @struts.action-forward name="success" path="user.welcome.page"
 */
public class WelcomeUserAction extends Action {

	private static Logger LOG = Logger.getLogger(WelcomeUserAction.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		LOG.debug("WelcomeUserAction");

		// return (mapping.findForward("error"));
		return mapping.findForward("success");
	}

}
