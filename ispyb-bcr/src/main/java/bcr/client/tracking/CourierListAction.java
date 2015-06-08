/*
 * CourierListAction.java
 * @author patrice.brenchereau@esrf.fr
 * July 8, 2008
 */

package bcr.client.tracking;

import ispyb.server.common.services.shipping.DewarAPIService;

import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.Timestamp;
import java.util.ArrayList;

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
 * @struts.action name="courierForm" path="/user/courierList" type="bcr.client.tracking.CourierListAction"
 *                input="site.tracking.courierList.page" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="courierListPage" path="bcr.tracking.courierList.page"
 * 
 * @struts.action-forward name="logoffPage" path="/logoff"
 * 
 * @struts.action-forward name="success" path="bcr.tracking.courier_ok.page"
 * 
 * @struts.action-forward name="error" path="bcr.tracking.courier_error.page"
 * 
 */
public class CourierListAction extends DispatchAction {

	private static Logger LOG = Logger.getLogger(CourierListAction.class);

	private static final String recordDelimiter = "-";

	private static final String shippingLocation = "";

	private static final Ejb3ServiceLocatorBCR ejb3ServiceLocator = Ejb3ServiceLocatorBCR.getInstance();

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		// Test...
		LOG.debug("UNSPECIFIED !!!!");

		return (mapping.findForward("courierListPage"));
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
		CourierForm myForm = (CourierForm) actForm;
		String courierName = myForm.getCourierName().toUpperCase();
		LOG.debug("Dewars at '" + courierName + "'");

		return (mapping.findForward("courierListPage"));
	}

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward addDewar(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
		CourierForm myForm = (CourierForm) actForm;
		String courierName = myForm.getCourierName().toUpperCase();
		String currentDewarNumber = myForm.getCurrentDewarNumber().toUpperCase();
		String currentTrackingNumber = myForm.getCurrentTrackingNumber().toUpperCase();
		String processedTrackingNumber = "";
		int nbDewars = myForm.getNbDewars();

		try {
			// Remote API
			DewarAPIService dewarAPIService = (DewarAPIService) ejb3ServiceLocator.getRemoteService(DewarAPIService.class);

			// Check if dewar is valid
			if (!TrackingUtils.isvalidDewarBarcode(currentDewarNumber)) {
				// Error on dewar number
				LOG.debug("Not adding dewar '" + currentDewarNumber + "'");
				myForm.setCurrentDewarNumber("");
				myForm.setCurrentTrackingNumber("");
				myForm.setMessage("Dewar '" + currentDewarNumber + "' non valide");
			}
			// Check if dewar exists
			else if (!dewarAPIService.dewarExists(currentDewarNumber)) {
				LOG.debug("Unknown dewar '" + currentDewarNumber + "'");
				myForm.setCurrentDewarNumber("");
				myForm.setCurrentTrackingNumber("");
				myForm.setMessage("Dewar '" + currentDewarNumber + "' inconnu");
			}
			// Check if tracking number is not empty
			else if (TrackingUtils.isemptyCourierBarcode(currentTrackingNumber)) {
				// Do nothing
			} else {
				// Check and parse tracking number
				processedTrackingNumber = TrackingUtils.processTrackingNumber(courierName, currentTrackingNumber);
				if (processedTrackingNumber.equals("")) {
					// Error on Tracking Number
					LOG.debug("Not adding tracking number '" + currentTrackingNumber + "'");
					myForm.setCurrentDewarNumber("");
					myForm.setCurrentTrackingNumber("");
					myForm.setMessage("TN '" + currentTrackingNumber + "' non valide");
				} else {
					// Insert into list
					String record = currentDewarNumber + recordDelimiter + processedTrackingNumber;
					LOG.debug("Adding dewar '" + record + "'");
					myForm.setDewarList(myForm.getDewarList() + "\n" + record);
					nbDewars++;
					myForm.setNbDewars(nbDewars);
					myForm.setCurrentDewarNumber("");
					myForm.setCurrentTrackingNumber("");
				}
			}

		} catch (Exception e) {
			LOG.error(e.toString());
			e.printStackTrace();
			myForm.setCurrentDewarNumber("");
			myForm.setCurrentTrackingNumber("");
			myForm.setMessage("Probleme de connexion a la base...");
		}

		return (mapping.findForward("courierListPage"));
	}

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward resetList(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
		CourierForm myForm = (CourierForm) actForm;
		LOG.debug("Reset list");
		myForm.setDewarList("");
		myForm.setNbDewars(0);
		myForm.setCurrentDewarNumber("");
		myForm.setCurrentTrackingNumber("");

		return (mapping.findForward("courierListPage"));
	}

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward sendList(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
		// // For testing
		// String a = "";
		// a = "Before";
		// System.out.println("Sleep start");
		// try{
		// Thread.currentThread().sleep(5000);
		// }
		// catch(Exception ie){
		// }
		// System.out.println("Sleep stop");
		// if ( !a.equals("Before") ) System.out.println("a = "+a+" !!!!!!!!!!!!!!!!!");
		// else System.out.println("a = "+a+" (OK)");
		// a = "After";

		ActionMessages errors = new ActionMessages();

		HttpSession session = request.getSession();
		String username = (String) session.getAttribute(Constants.PERSON_LOGIN);
		CourierForm myForm = (CourierForm) actForm;
		String courierName = TrackingUtils.normalizeCourierName(myForm.getCourierName());
		String dewarList = myForm.getDewarList();
		Timestamp dateTime = getDateTime();

		// Get store and mxind email addresses
		String emailAddressStores = Constants.getProperty("mail.stores.to");
		String emailAddressMxInd = Constants.getProperty("mail.dewarTracking.to");

		String serverName = request.getServerName().toLowerCase();
		boolean inTest = LocalizationListAction.inTest(serverName);

		if (inTest) {
			emailAddressStores = Constants.getProperty("mail.stores.to.test");
			emailAddressMxInd = Constants.getProperty("mail.dewarTracking.to.test");
		}

		// Check if list is not empty
		if (dewarList == null || dewarList.length() == 0) {
			return (mapping.findForward("courierListPage"));
		}

		// Insert event list into db
		ArrayList myList = new ArrayList();
		int nbDewars = 0;
		try {
			StringReader sr = new StringReader(dewarList);
			BufferedReader lnr = new BufferedReader(sr);
			String record;
			while ((record = lnr.readLine()) != null) {
				if (record.length() != 0) {

					// One more dewar
					nbDewars++;
					String dewarId = record.substring(0, record.indexOf(recordDelimiter));
					String trackingNumber = record.substring(record.indexOf(recordDelimiter) + recordDelimiter.length(),
							record.length());

					// Create remote dewar API
					DewarAPIService dewarAPIService = (DewarAPIService) Ejb3ServiceLocatorBCR.getInstance().getRemoteService(
							DewarAPIService.class);

					// Add dewar event in table
					dewarAPIService.addDewarLocation(dewarId, username, dateTime, shippingLocation, courierName, trackingNumber);

					// Update dewar info (Dewar, Shipping, DewarTransportHistory)
					if (!dewarAPIService.updateDewar(dewarId, "", courierName, trackingNumber)) {
						// Send email to stores on error
						TrackingEmail.sendErrorCannotFind(emailAddressStores, "", dewarId, dateTime);
					}
					// Send email to lab contact
					else if (!TrackingEmail.sendShippingEmailToLabContact(inTest, emailAddressStores, emailAddressMxInd, dewarId,
							dateTime, courierName, trackingNumber)) {
						// Send email to stores on error
						TrackingEmail.sendErrorCannotEmail(emailAddressStores, "", dewarId, dateTime);
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
