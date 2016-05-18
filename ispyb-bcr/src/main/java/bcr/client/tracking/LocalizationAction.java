/*
 * LocalizationAction.java
 * @author patrice.brenchereau@esrf.fr
 * July 8, 2008
 */

package bcr.client.tracking;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import bcr.common.util.Constants;
import bcr.common.util.Ejb3ServiceLocatorBCR;
import ispyb.server.common.services.shipping.DewarAPIService;

/**
 * @struts.action name="localizationForm" path="/user/localization" type="bcr.client.tracking.LocalizationAction"
 *                input="site.tracking.localization.page" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="localizationPage" path="bcr.tracking.localization.page"
 * 
 * @struts.action-forward name="success" path="bcr.tracking.localizationList.page"
 * 
 * @struts.action-forward name="error" path="bcr.tracking.error.page"
 * 
 */
public class LocalizationAction extends DispatchAction {

	private static final Ejb3ServiceLocatorBCR ejb3ServiceLocator = Ejb3ServiceLocatorBCR.getInstance();

	private static Logger LOG = Logger.getLogger(LocalizationAction.class);

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute(Constants.PERSON_LOGIN);
		LOG.debug("Localization made by: '" + username + "'");

		LocalizationForm myForm = (LocalizationForm) actForm;
		myForm.setLocation("");

		return (mapping.findForward("localizationPage"));
	}

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward localizationList(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		LocalizationForm myForm = (LocalizationForm) actForm;
		String location = myForm.getLocation().toUpperCase();
		myForm.setLocation(location);

		try {

			// Check if location is not empty
			if (location == null || location.length() == 0) {
				LOG.debug("Location is empty");
				return (mapping.findForward("localizationPage"));
			}

			// Check if location is valid
			DewarAPIService dewarAPIService = (DewarAPIService) ejb3ServiceLocator.getRemoteService(DewarAPIService.class);

			ArrayList myLocations = dewarAPIService.getDewarLocations();
			if (!myLocations.contains(location)) {
				LOG.debug("Location '" + location + "' not valid");
				myForm.setMessage("Emplacement '" + location + "' non valide");
				myForm.setLocation("");
				return (mapping.findForward("localizationPage"));
			}

		} catch (Exception e) {
			LOG.debug("Cannot get valid location list");
			e.printStackTrace();
			myForm.setMessage("Probleme de validation des emplacements");
			myForm.setLocation("");
			return (mapping.findForward("localizationPage"));
		}

		// Jump to dewar list
		return (mapping.findForward("success"));

	}

}
