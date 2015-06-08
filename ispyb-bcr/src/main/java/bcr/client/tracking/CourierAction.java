/*
 * CourierAction.java
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

/**
 * @struts.action name="courierForm" path="/user/courier" type="bcr.client.tracking.CourierAction"
 *                input="site.tracking.courier.page" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="courierPage" path="bcr.tracking.courier.page"
 * 
 * @struts.action-forward name="success" path="bcr.tracking.courierList.page"
 * 
 * @struts.action-forward name="error" path="bcr.tracking.error.page"
 * 
 */
public class CourierAction extends DispatchAction {

	private static Logger LOG = Logger.getLogger(CourierAction.class);

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute(Constants.PERSON_LOGIN);
		LOG.debug("Courier made by: '" + username + "'");

		CourierForm myForm = (CourierForm) actForm;
		myForm.setCourierName("");

		return (mapping.findForward("courierPage"));
	}

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward courierList(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		CourierForm myForm = (CourierForm) actForm;
		String courierName = myForm.getCourierName().toUpperCase();
		myForm.setCourierName(courierName);

		try {

			// Check if courierName is not empty
			if (courierName == null || courierName.length() == 0) {
				LOG.debug("CourierName is empty");
				return (mapping.findForward("courierPage"));
			}

			// Check if courierName is valid
			ArrayList myCouriers = TrackingUtils.getCouriers();
			if (!myCouriers.contains(courierName)) {
				LOG.debug("Courier '" + courierName + "' not valid");
				myForm.setMessage("Transporteur '" + courierName + "' non valide");
				myForm.setCourierName("");
				return (mapping.findForward("courierPage"));
			}

		} catch (Exception e) {
			LOG.debug("Cannot get valid courier list");
			e.printStackTrace();
			myForm.setMessage("Probleme de validation des transporteurs");
			myForm.setCourierName("");
			return (mapping.findForward("courierPage"));
		}

		// Jump to courier list
		return (mapping.findForward("success"));

	}

}
