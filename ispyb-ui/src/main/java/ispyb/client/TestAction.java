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
/**
 * Test
 */
package ispyb.client;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import edu.yale.its.tp.cas.client.ServiceTicketValidator;
import edu.yale.its.tp.cas.proxy.ProxyTicketReceptor;

/**
 * @struts.action name="testForm" path="/user/testAction" type="ispyb.client.TestAction" validate="false"
 *                parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="testViewPage" path="user.test.view.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 */

public class TestAction extends DispatchAction {

	private final ArrayList<Integer> listLudo = new ArrayList();

	private final static Logger LOG = Logger.getLogger(TestAction.class);

	public static void main(String[] args) {

	}

	// ###FLAVOR_ESRF###
	public void dummy() {
	}

	// ###END#FLAVOR_ESRF###

	/*
	 * ###FLAVOR_DLS### public void dummy() { } //###END#FLAVOR_DLS###
	 */

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_response) {
		// ---------------------------------------------------------------------------------------------------
		// Retrieve Attributes
		String myURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getRequestURI();
		String casServerLoginURL = "https://auth.diamond.ac.uk/cas/login?service=" + myURL;
		String casValidateURL = "https://auth.diamond.ac.uk/cas/serviceValidate";

		try {
			String user = null;
			String errorCode = null;
			String errorMessage = null;
			String xmlResponse = null;

			/* instantiate a new ServiceTicketValidator */
			ServiceTicketValidator sv = new ServiceTicketValidator();

			/* set its parameters */
			sv.setCasValidateUrl(casValidateURL);
			sv.setService(myURL);
			if (request.getParameter("ticket") != null) // Returning from CAS server
				sv.setServiceTicket(request.getParameter("ticket"));
			else // Authentication needed = redirect to CAS login page
			{
				LOG.debug("CAS Authentication: NOT AUTHENTICATED. redirecting to :" + casServerLoginURL);
				in_response.sendRedirect(casServerLoginURL);
				return null;
			}

			// /*
			// * If we want to be able to acquire proxy tickets (requires callback servlet to be set up
			// * in web.xml - see below)
			// */
			//
			// String urlOfProxyCallbackServlet = "https://localhost/cas/CasProxyServlet";
			//
			// sv.setProxyCallbackUrl(urlOfProxyCallbackServlet);

			/* contact CAS and validate */
			sv.validate();

			/* if we want to look at the raw response, we can use getResponse() */
			xmlResponse = sv.getResponse();

			/* read the response */

			// Yes, this method is misspelled in this way
			// in the ServiceTicketValidator implementation.
			// Sorry.
			TestForm form = (TestForm) actForm;

			if (sv.isAuthenticationSuccesful()) {
				user = sv.getUser();

				form.setUsername(user);
			} else {
				errorCode = sv.getErrorCode();
				errorMessage = sv.getErrorMessage();

				/* handle the error */
				form.setUsername(errorMessage);
			}

			/* The user is now authenticated. */
			/* If we did set the proxy callback url, we can get proxy tickets with: */
			String proxyTicket = ProxyTicketReceptor.getProxyTicket(sv.getPgtIou(), myURL);
			String ticket = null;
			if (proxyTicket != null)
				ticket = proxyTicket.toString();
		} catch (IllegalStateException is) {
			LOG.debug("CAS Authentication: something is missing in the call...");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapping.findForward("testViewPage");
	}

}
