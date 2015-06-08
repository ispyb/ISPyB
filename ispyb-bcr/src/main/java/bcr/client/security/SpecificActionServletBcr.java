package bcr.client.security;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionServlet;

import bcr.common.util.Constants;

public class SpecificActionServletBcr extends ActionServlet {

	public static String contextPath = null;

	private static Logger LOG = Logger.getLogger(SpecificActionServletBcr.class);

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

			// Username
			String username = "";
			if (request.getUserPrincipal() != null)
				username = request.getUserPrincipal().toString();

			// Role
			String myRole = "";
			String currentRole = (String) session.getAttribute(Constants.CURRENT_ROLE);
			if (currentRole != null)
				myRole = currentRole;
			else
				myRole = "none";

			// Trace
			LOG.debug("Request page: " + request.getRequestURI() + " by '" + username + "' as '" + myRole + "'.");

			// Role management
			if (session.getAttribute(Constants.CURRENT_ROLE) == null) {
				LOG.debug("First Guest access...");
				session.setAttribute(Constants.CURRENT_ROLE, Constants.DEFAULT_ROLE_NAME);
				response.sendRedirect(request.getContextPath() + "/welcomeGuestPage.do");
			}

			super.process(request, response);

			// //LOG.debug("Request page: " + request.getRequestURI());
			// HttpSession session = request.getSession();
			// String username = (String) session.getAttribute(Constants.PERSON_LOGIN);
			// LOG.debug("Request page: " + request.getRequestURI() + " by '"+username+"'.");
			//
			// super.process(request, response);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}