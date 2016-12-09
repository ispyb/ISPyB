/*
 * LocalizationListAction.java
 * @author patrice.brenchereau@esrf.fr
 * July 8, 2008
 */

package bcr.client.tracking;

import ispyb.server.common.services.shipping.DewarAPIService;

import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

import bcr.common.util.Constants;
import bcr.common.util.Ejb3ServiceLocatorBCR;

/**
 * @struts.action name="localizationForm" path="/user/localizationList" type="bcr.client.tracking.LocalizationListAction"
 *                input="site.tracking.localizationList.page" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="localizationListPage" path="bcr.tracking.localizationList.page"
 * 
 * @struts.action-forward name="logoffPage" path="/logoff"
 * 
 * @struts.action-forward name="success" path="bcr.tracking.localization_ok.page"
 * 
 * @struts.action-forward name="error" path="bcr.tracking.localization_error.page"
 * 
 */
public class LocalizationListAction extends DispatchAction {

	private static final String ESRF_START_BC = "ESRF";

	private static final Ejb3ServiceLocatorBCR ejb3ServiceLocator = Ejb3ServiceLocatorBCR.getInstance();

	private static Logger LOG = Logger.getLogger(LocalizationListAction.class);

	private DewarAPIService dewarAPIService;

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws CreateException, NamingException {

		this.dewarAPIService = (DewarAPIService) ejb3ServiceLocator.getRemoteService(DewarAPIService.class);

	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		// Test...
		LOG.debug("UNSPECIFIED !!!!");

		return (mapping.findForward("localizationListPage"));
	}

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
		LocalizationForm myForm = (LocalizationForm) actForm;
		String location = myForm.getLocation().toUpperCase();
		LOG.debug("Dewars at '" + location + "'");

		return (mapping.findForward("localizationListPage"));
	}

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward addDewar(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {

		LocalizationForm myForm = (LocalizationForm) actForm;
		String location = myForm.getLocation().toUpperCase();
		String currentDewarNumber = myForm.getCurrentDewarNumber().toUpperCase();
		int nbDewars = myForm.getNbDewars();

		try {

			// Check if barcode is valid
			if (!TrackingUtils.isvalidDewarBarcode(currentDewarNumber)) {
				LOG.debug("Not adding dewar '" + currentDewarNumber + "'");
				myForm.setCurrentDewarNumber("");
				myForm.setMessage("Dewar '" + currentDewarNumber + "' non valide");
			}
			// Check if dewar exists
			else if (!dewarAPIService.dewarExists(currentDewarNumber)) {
				LOG.debug("Unknown dewar '" + currentDewarNumber + "'");
				myForm.setCurrentDewarNumber("");
				myForm.setMessage("Dewar '" + currentDewarNumber + "' inconnu");
			} else {
				// Add dewar to list
				LOG.debug("Adding dewar '" + currentDewarNumber + "' at " + location);
				myForm.setDewarList(myForm.getDewarList() + "\n" + currentDewarNumber);
				nbDewars++;
				myForm.setNbDewars(nbDewars);
			}

		} catch (Exception e) {
			LOG.error(e.toString());
			e.printStackTrace();
			myForm.setCurrentDewarNumber("");
			myForm.setMessage("Probleme de connexion � la base...");
		}

		return (mapping.findForward("localizationListPage"));
	}

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward resetList(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
		LocalizationForm myForm = (LocalizationForm) actForm;
		LOG.debug("Reset list");
		myForm.setDewarList("");
		myForm.setNbDewars(0);

		return (mapping.findForward("localizationListPage"));
	}

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward sendList(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();

		HttpSession session = request.getSession();
		String username = (String) session.getAttribute(Constants.PERSON_LOGIN);
		LocalizationForm myForm = (LocalizationForm) actForm;
		String location = myForm.getLocation().toUpperCase();
		String dewarList = myForm.getDewarList();
		Timestamp dateTime = getDateTime();

		String serverName = request.getServerName().toLowerCase();

		// Get store and mxind email addresses
		String emailAddressStores = Constants.getProperty("mail.stores.to");
		String emailAddressMxInd = Constants.getProperty("mail.dewarTracking.to");

		boolean inTest = inTest(serverName);

		LOG.debug("Dewar Tracking: server name = " + serverName + " => in test = " + inTest);

		if (inTest) {
			emailAddressStores = Constants.getProperty("mail.stores.to.test");
			emailAddressMxInd = Constants.getProperty("mail.dewarTracking.to.test");
		}

		// Check if list is valid
		if (dewarList == null || dewarList.length() == 0) {
			return (mapping.findForward("localizationListPage"));
		}

		// Insert events into db
		ArrayList myList = new ArrayList();
		int nbDewars = 0;
		try {
			StringReader sr = new StringReader(dewarList);
			BufferedReader lnr = new BufferedReader(sr);
			String dewarId;

			// Create remote dewar API

			while ((dewarId = lnr.readLine()) != null) {
				if (dewarId.length() != 0) {

					// One more dewar
					nbDewars++;

					// Add dewar event in table
					dewarAPIService.addDewarLocation(dewarId, username, dateTime, location, "", "");

					// Update dewar info (Dewar, Shipping, DewarTransportHistory)
					if (!dewarAPIService.updateDewar(dewarId, location, "", "")) {
						// Send email to stores on error
						TrackingEmail.sendErrorCannotFind(emailAddressStores, "", dewarId, dateTime);
					}
					// STORES-IN: Send email to lab contact
					else if (location.equals(Constants.ARRIVAL_LOCATION)) {
						if (!TrackingEmail.sendArrivalEmailToLabContact(inTest, emailAddressStores, emailAddressMxInd, dewarId,
								dateTime, location)) {
							// Send email to stores on error
							TrackingEmail.sendErrorCannotEmail(emailAddressStores, "", dewarId, dateTime);
						}
					}
					// STORES-OUT: Send email to lab contact
					else if (location.equals(Constants.RETURN_LOCATION)) {
						if (!TrackingEmail.sendReadyToBePickedEmailToLabContact(inTest, emailAddressStores, emailAddressMxInd, dewarId,
								dateTime, location)) {
							// Send email to stores on error
							TrackingEmail.sendErrorCannotEmail(emailAddressStores, "", dewarId, dateTime);
						}
					}
					// BEAMLINE: Send email to lab contact
					else if (!location.equals(Constants.ARRIVAL_LOCATION) && !location.equals(Constants.RETURN_LOCATION)) {
						if (!TrackingEmail.sendBeamlineEmailToLabContact(inTest, emailAddressStores, emailAddressMxInd, dewarId,
								dateTime, location)) {
							// Send email to stores on error
							TrackingEmail.sendErrorCannotEmail(emailAddressStores, "", dewarId, dateTime);
						}
					}
				}
			}
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Erreur d'insertion dans la base"));
			// errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			e.printStackTrace();
			saveErrors(request, errors);
			return mapping.findForward("error");
		}

		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", nbDewars + " dewars"));
		saveErrors(request, errors);
		return (mapping.findForward("success"));
	}

	/**
	 * return true if the application is runnning on test server
	 * 
	 * @param serverName
	 * @return
	 */
	public static boolean inTest(String serverName) {
		boolean inTest = false;
		if (serverName.equals(Constants.getProperty("ISPyB.server.shortname.test"))
				|| serverName.equals(Constants.getProperty("ISPyB.server.shortname.localhost")))
			inTest = true;

		LOG.debug("Dewar Tracking: server name = " + serverName + " => in test = " + inTest);
		return inTest;

	}

	/**
	 * @return
	 */
	private Timestamp getDateTime() {

		// DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// Date date = new Date();
		// return dateFormat.format(date);

		java.util.Date today = new java.util.Date();
		return (new java.sql.Timestamp(today.getTime()));
	}

}
