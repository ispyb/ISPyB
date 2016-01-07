/*************************************************************************************************
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
 ****************************************************************************************************/

package ispyb.client.common.shipping;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.proposal.ProposalUtils;
import ispyb.common.util.DBTools;
import ispyb.client.common.util.QueryBuilder;
import ispyb.common.util.Constants;
import ispyb.server.common.services.proposals.LabContact3Service;
import ispyb.server.common.services.shipping.Dewar3Service;
import ispyb.server.common.services.shipping.DewarTransportHistory3Service;
import ispyb.server.common.services.shipping.Shipping3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.DewarTransportHistory3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.vos.collections.Session3VO;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * @struts.action name="submitShippingForm" path="/user/submitShippingAction"
 *                type="ispyb.client.common.shipping.SubmitShippingAction" input="user.shipping.submit.page" validate="false"
 *                parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="submitShippingPage" path="user.shipping.submit.page"
 * 
 * @struts.action-forward name="dewarViewPage" path="/user/viewDewarAction.do?reqCode=search"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 */

public class SubmitShippingAction extends org.apache.struts.actions.DispatchAction {
	private final Logger LOG = Logger.getLogger(SubmitShippingAction.class);

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	// Session Attributes
	Integer mProposalId = null;

	private Shipping3Service shippingService;

	private Dewar3Service dewarService;

	private LabContact3Service labContactService;

	private DewarTransportHistory3Service dewarTransportHistoryService;

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {

		this.dewarService = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);
		this.shippingService = (Shipping3Service) ejb3ServiceLocator.getLocalService(Shipping3Service.class);
		this.labContactService = (LabContact3Service) ejb3ServiceLocator.getLocalService(LabContact3Service.class);
		this.dewarTransportHistoryService = (DewarTransportHistory3Service) ejb3ServiceLocator
				.getLocalService(DewarTransportHistory3Service.class);

	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	/**
	 * display
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		System.out.println("display");
		ActionMessages errors = new ActionMessages();
		// ActionMessages messages = new ActionMessages();
		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			SubmitShippingForm form = (SubmitShippingForm) actForm; // Parameters submited by form

			this.RetrieveSessionAttributes(request); // Session parameters

			Integer shippingId = new Integer(request.getParameter(Constants.SHIPPING_ID));

			Shipping3VO shipping = DBTools.getSelectedShipping(shippingId);

			BreadCrumbsForm.getItClean(request).setSelectedShipping(shipping);
			BreadCrumbsForm.getIt(request).setSelectedDewar(null);
			// ---------------------------------------------------------------------------------------------------

			form.setShipping(shipping);

			// Retrieve the sending labContact
			Integer returnLabContactId = shipping.getReturnLabContactId();
			LabContact3VO labContact = labContactService.findByPk(returnLabContactId);
			// =>Fill the form with the courrier company from the sending labContact
			form.getShipping().setDeliveryAgentAgentName(labContact.getDefaultCourrierCompany());

			// dates from shipping
			SimpleDateFormat dateStandard = new SimpleDateFormat("dd-MM-yyyy");
			if (shipping.getDeliveryAgentShippingDate() != null) {
				form.setSendingDate(dateStandard.format(shipping.getDeliveryAgentShippingDate()));
			}
			// else {
			// form.setSendingDate(dateStandard.format(new Date()));
			// }
			if (shipping.getDeliveryAgentDeliveryDate() != null)
				form.setExpectedEsrfArrivalDate(dateStandard.format(shipping.getDeliveryAgentDeliveryDate()));

			// dewars - tracking numbers
			List<Dewar3VO> dewars = dewarService.findByShippingId(shipping.getShippingId());
			if(dewars == null)
				dewars = new ArrayList<Dewar3VO>();
			// Dewar3VO[] tab = dewars.toArray(new Dewar3VO[dewars.size()]);

			form.setListDewars(dewars);

			// Check dewars (sessions and labels)
			boolean shippingSessionsOk = true;
			boolean shippingLabelOk = true;
			Iterator<Dewar3VO> iter = dewars.iterator();
			String[] trackingNumber = new String[dewars.size()];
			int i = 0;
			while (iter.hasNext()) {
				Dewar3VO currentDewar = iter.next();
				if (currentDewar.getSessionVO() == null)
					shippingSessionsOk = false;
				if (currentDewar.getDewarStatus() == null || currentDewar.getDewarStatus().equals("")
						|| currentDewar.getDewarStatus().equals(Constants.SHIPPING_STATUS_OPENED))
					shippingLabelOk = false;
				trackingNumber[i] = currentDewar.getTrackingNumberToSynchrotron();
			}
			form.setTrackingNumbers(trackingNumber);
			// Check if dewars have session (only if proposal has sessions)
			List<Session3VO> listSessions = ProposalUtils.getSessionsAvailable(request);
			if (listSessions != null && listSessions.size() != 0 && !shippingSessionsOk) {
				String errorMessage = "All dewars should have an experiment date and a beamline";
				LOG.debug("Error: " + errorMessage);
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", errorMessage));
				saveErrors(request, errors);
				return mapping.findForward("dewarViewPage");
			}

			// Check if dewars labels have been printed (at least viewed)
			if (!shippingLabelOk) {
				String errorMessage = "All dewar labels should have been printed";
				LOG.debug("Error: " + errorMessage);
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", errorMessage));
				saveErrors(request, errors);
				return mapping.findForward("dewarViewPage");
			}

			// Forward page
			FormUtils.setFormDisplayMode(request, actForm, FormUtils.CREATE_MODE);
			return mapping.findForward("submitShippingPage");
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			e.printStackTrace();
			return mapping.findForward("error");
		}
	}

	/**
	 * submit shipping
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward submit(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_response) {
		System.out.println("submit");
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();

		// if (BreadCrumbsForm.getIt(request).getSelectedShipping()!=null) // Redirect to the update
		// return this.update(mapping, actForm, request, in_response);

		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			SubmitShippingForm form = (SubmitShippingForm) actForm; // Parameters submited by form

			// ---------------------------------------------------------------------------------------------------

			// Check Required fields populated
			if (!this.CheckRequiredFieldsPopulated(form, request)) {

				return mapping.findForward("submitShippingPage");
			}

			/*
			 * Shipping ------------------------------------------------------------------------
			 */
			// shipping from form
			Shipping3VO shippingFromForm = form.getShipping();
			// //Retrieve the selected shipping
			// ShippingLightValue shippingSelected = BreadCrumbsForm.getIt(request).getSelectedShipping();
			// reload shipping from the database
			Shipping3VO shippingFromBD = shippingService.findByPk(shippingFromForm.getShippingId(), true);

			// courrier
			String courrier = shippingFromForm.getDeliveryAgentAgentName();
			shippingFromBD.setDeliveryAgentAgentName(courrier);

			// dates
			shippingFromBD.setDeliveryAgentShippingDate(QueryBuilder.toDate(form.getSendingDate()));
			LOG.debug("DeliveryAgentShippingDate=" + shippingFromForm.getDeliveryAgentShippingDate());

			shippingFromBD.setDeliveryAgentDeliveryDate(QueryBuilder.toDate(form.getExpectedEsrfArrivalDate()));
			LOG.debug("DeliveryAgentDeliveryDate=" + shippingFromForm.getDeliveryAgentDeliveryDate());

			// new status !
			shippingFromBD.setShippingStatus(shippingFromForm.getShippingStatus());

			// UPDATE shipping into the DATABASE
			shippingFromBD.setTimeStamp(new Timestamp(new Date().getTime()));
			shippingService.update(shippingFromBD);

			// reset the dewar loaded from the database into the Form
			form.setShipping(shippingFromBD);
			// reset the selected shipping
			BreadCrumbsForm.getIt(request).setSelectedShipping(shippingFromBD);

			/*
			 * dewars --------------------------------------------------------------------------
			 */
			// dewars tracking numbers

			List<Dewar3VO> dewarsList = new ArrayList<Dewar3VO>(shippingFromBD.getDewarVOs());
			for (int i = 0; i < dewarsList.size(); i++) {
				Dewar3VO dewarFromBD = dewarsList.get(i);
				dewarFromBD.setTrackingNumberToSynchrotron(form.getTrackingNumbers(i));
				dewarFromBD.setDewarStatus(Constants.SHIPPING_STATUS_SENT_TO_ESRF);
				// s.deliveryAgent_agentName
				// dewarFromBD.setCourierToESRF(courrier);
				dewarFromBD.getShippingVO().setDeliveryAgentAgentName(courrier);

				// SAVE new values into the DATABASE
				dewarFromBD.setTimeStamp(new Timestamp(new Date().getTime()));
				dewarService.update(dewarFromBD);
				shippingService.update(dewarFromBD.getShippingVO());

				// Add event to history
				Timestamp dateTime = getDateTime();
				DewarTransportHistory3VO newHistory = new DewarTransportHistory3VO();
				newHistory.setDewarStatus(Constants.SHIPPING_STATUS_SENT_TO_ESRF);
				newHistory.setStorageLocation("");
				newHistory.setArrivalDate(dateTime);
				newHistory.setDewarVO(dewarFromBD);
				dewarTransportHistoryService.create(newHistory);

				// reset the dewar loaded dewar loaded from the database into the Form
				dewarsList.set(i, dewarFromBD);

			}

			// ---------------------------------------------------------------------------------------------------
			// Acknowledge action
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.update"));
			saveMessages(request, messages);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.EDIT_MODE);
			// WF: go to List of dewar instead of staying on the update page
			// return mapping.findForward("submitShippingPage");
			BreadCrumbsForm.getIt(request).setSelectedDewar(null);
			return mapping.findForward("dewarViewPage");
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
	}

	/**
	 * RetrieveSessionAttributes
	 * 
	 * @param request
	 *            The Request to retrieve the Session Attributes from Populates the class Attributes with Attributes
	 *            stored in the Session
	 */
	public void RetrieveSessionAttributes(HttpServletRequest request) {
		this.mProposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
	}

	/**
	 * CheckRequiredFieldsPopulated
	 * 
	 * @param form
	 *            The form to get Fields from. Checks that Required Fields are there. Redirect to
	 */
	public boolean CheckRequiredFieldsPopulated(SubmitShippingForm form, HttpServletRequest request) {
		System.out.println("CheckRequiredFieldsPopulated");
		boolean requiredFieldsPresent = true;
		ActionMessages l_ActionMessages = new ActionMessages();
		// Sending Date
		if (form.getSendingDate().length() == 0) {
			requiredFieldsPresent = false;

			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.required", "Sending Date Label");
			l_ActionMessages.add("sendingDate", l_ActionMessageLabel);
		}
		// Expected ESRF arrival date
		if (form.getExpectedEsrfArrivalDate().length() == 0) {
			requiredFieldsPresent = false;

			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.required", "Expected date Label");
			l_ActionMessages.add("expectedEsrfArrivalDate", l_ActionMessageLabel);
		}

		// Checks dates are coherent
		if (form.getSendingDate().length() != 0 && form.getExpectedEsrfArrivalDate().length() != 0) {
			SimpleDateFormat dateStandard = new SimpleDateFormat(Constants.DATE_FORMAT);
			Date expectedEsrfArrivalDate = null;
			Date sendingDate = null;
			boolean datesFormatIsValid = true;
			try {
				expectedEsrfArrivalDate = dateStandard.parse(form.getExpectedEsrfArrivalDate());
			} catch (ParseException e) {
				requiredFieldsPresent = false;
				datesFormatIsValid = false;
				ActionMessage l_ActionMessageLabel = new ActionMessage("errors.required", "Correct date format");
				l_ActionMessages.add("expectedEsrfArrivalDate", l_ActionMessageLabel);
			}

			try {
				sendingDate = dateStandard.parse(form.getSendingDate());
			} catch (ParseException e) {
				requiredFieldsPresent = false;
				datesFormatIsValid = false;
				ActionMessage l_ActionMessageLabel = new ActionMessage("errors.required", "Correct date format");
				l_ActionMessages.add("sendingDate", l_ActionMessageLabel);
			}

			if (datesFormatIsValid == true && expectedEsrfArrivalDate.before(sendingDate)) {
				requiredFieldsPresent = false;

				ActionMessage l_ActionMessageLabel = new ActionMessage("errors.required", "Coherent dates");
				l_ActionMessages.add("expectedEsrfArrivalDate", l_ActionMessageLabel);
				l_ActionMessages.add("sendingDate", l_ActionMessageLabel);
			}
		}

		if (!requiredFieldsPresent) {
			request.setAttribute("org.apache.struts.action.ERROR", l_ActionMessages);
		}

		return requiredFieldsPresent;
	}

	/**
	 * getDateTime
	 * 
	 * @return
	 */
	private Timestamp getDateTime() {

		java.util.Date today = new java.util.Date();
		return (new java.sql.Timestamp(today.getTime()));
	}

}
