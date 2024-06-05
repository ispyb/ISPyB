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
/*
 * CreateShippingAction.java
 * @author ludovic.launer@esrf.fr
 * Feb 9, 2005
 */

package ispyb.client.common.shipping;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.proposal.ProposalUtils;
import ispyb.client.common.util.DBConstants;
import ispyb.common.util.Constants;
import ispyb.common.util.SendMailUtils;
import ispyb.server.common.services.proposals.LabContact3Service;
import ispyb.server.common.services.proposals.Person3Service;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.services.shipping.Dewar3Service;
import ispyb.server.common.services.shipping.Shipping3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.vos.collections.Session3VO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;
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

/**
 * @struts.action name="viewShippingForm" path="/user/createShippingAction"
 *                type="ispyb.client.common.shipping.CreateShippingAction" input="user.shipping.create.page" validate="false"
 *                parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="shippingCreatePage" path="user.shipping.create.page"
 * 
 * @struts.action-forward name="shippingViewPage" path="/reader/genericShippingAction.do?reqCode=display"
 * 
 * @struts.action-forward name="dewarViewPage" path="/user/viewDewarAction.do?reqCode=search"
 * @struts.action-forward name="fillShipmentPage" path="user.shipping.fillShipment.page"
 * 
 * @struts.action-forward name="labcontactLoginPage" path="user.labcontact.login.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 */

public class CreateShippingAction extends org.apache.struts.actions.DispatchAction {

	private final Logger LOG = Logger.getLogger(CreateShippingAction.class);
	

	private final String DEWAR_TYPE = "Dewar";

	private final String TOOLBOX_TYPE = "Toolbox";

	// Session Attributes
	// Instance and static variables should not be used in an Action class to store
	// information related to the state of a particular request.
	// (Bug: it can create shipments in other proposals!)
	// Integer mProposalId = null;

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Session3Service sessionService;

	private Dewar3Service dewarService;

	private LabContact3Service labCService;

	private Shipping3Service shippingService;

	private Proposal3Service proposalService;

	private Person3Service person3Service;

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() {
		try {
			this.sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
			this.labCService = (LabContact3Service) ejb3ServiceLocator.getLocalService(LabContact3Service.class);
			this.proposalService = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
			this.dewarService = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);
			this.shippingService = (Shipping3Service) ejb3ServiceLocator.getLocalService(Shipping3Service.class);
			this.person3Service = (Person3Service) ejb3ServiceLocator.getLocalService(Person3Service.class);

		} catch (NamingException e) {
			LOG.error(e);
		}
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
		ActionMessages errors = new ActionMessages();
		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			ViewShippingForm form = (ViewShippingForm) actForm; // Parameters submited by form

			// this.RetrieveSessionAttributes(request);
			Integer mProposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			BreadCrumbsForm.getItClean(request);

			// ---------------------------------------------------------------------------------------------------
			// Build Query
			Shipping3VO info = new Shipping3VO();

			// init creation date
			info.setCreationDate(new Date());
			info.setShippingStatus(Constants.SHIPPING_STATUS_OPENED);

			/*
			 * Suggest des personnes pour les lab-contacts
			 */
			// Get an object list.
			// Retrieve information from DB
			List<LabContact3VO> listLabContacts = labCService.findFiltered(mProposalId, null);
			List<Session3VO> listSessions = ProposalUtils.getSessionsAvailable(request);

			// decores the sessions list
			List<CustomSessionBean> availableSessionsDecoratedList = decoreSessionList(listSessions);
			Collections.sort(availableSessionsDecoratedList);
			availableSessionsDecoratedList = new ArrayList<CustomSessionBean>(availableSessionsDecoratedList);

			// lab contact info for return
			LabContact3VO labContact = new LabContact3VO();

			// Populate with Info
			form.setNbOtherComponents(0);
			form.setListLabContacts(listLabContacts);
			form.setListSessions(availableSessionsDecoratedList);
			form.setInfo(info);

			form.setLabContact(labContact);

			//
			List<String> listDefaultCourierCompany = new ArrayList<String>();
			List<String> listCourierAccount = new ArrayList<String>();
			List<String> listBillingReference = new ArrayList<String>();
			List<String> listDewarAvgCustomsValue = new ArrayList<String>();
			List<String> listDewarAvgTransportValue = new ArrayList<String>();
			for (Iterator<LabContact3VO> c = listLabContacts.iterator(); c.hasNext();) {
				LabContact3VO lb = c.next();
				listDefaultCourierCompany.add(lb.getDefaultCourrierCompany() == null ? "''" : "'"
						+ lb.getDefaultCourrierCompany() + "'");
				listCourierAccount.add(lb.getCourierAccount() == null ? "''" : "'" + lb.getCourierAccount() + "'");
				listBillingReference
						.add(lb.getBillingReference() == null ? "''" : "'" + lb.getBillingReference() + "'");
				listDewarAvgCustomsValue.add(Integer.toString(lb.getDewarAvgCustomsValue()));
				listDewarAvgTransportValue.add(Integer.toString(lb.getDewarAvgTransportValue()));
			}
			form.setListDefaultCourierCompany(listDefaultCourierCompany);
			form.setListCourierAccount(listCourierAccount);
			form.setListBillingReference(listBillingReference);
			form.setListDewarAvgCustomsValue(listDewarAvgCustomsValue);
			form.setListDewarAvgTransportValue(listDewarAvgTransportValue);
			form.setSendingLabContactId(info.getSendingLabContactId());
			form.setReturnLabContactId(info.getReturnLabContactId());

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.CREATE_MODE);
		} catch (Exception e) {
			e.printStackTrace();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return mapping.findForward("shippingCreatePage");
	}

	/**
	 * updateDisplay
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward updateDisplay(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();

		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			ViewShippingForm form = (ViewShippingForm) actForm; // Parameters submitted by form
			// this.RetrieveSessionAttributes(request);
			Integer mProposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			String shippingIdStr = request.getParameter(Constants.SHIPPING_ID);
			int shippingId = new Integer(shippingIdStr);
			// ---------------------------------------------------------------------------------------------------

			// Retrieve Shipping information
			Shipping3VO selectedShipping = shippingService.findByPk(shippingId, true);
			Shipping3VO info = selectedShipping;

			/*
			 * Suggest des personnes pour les lab-contacts
			 */
			// Get an object list.
			// Retrieve information from DB
			List<LabContact3VO> listLabContacts = labCService.findFiltered(mProposalId, null);

			// list of all available sessions for this proposal
			List<Session3VO> availableSessionsList = ProposalUtils.getSessionsAvailable(request);
			// decores the sessions list
			List<CustomSessionBean> availableSessionsDecoratedList = decoreSessionList(availableSessionsList);

			// Retrieve list of session associated to this shipping
			List<Session3VO> shippingSessions = sessionService.findByShippingId(shippingId);

			// list of sessionIds linked to the current shipping
			Integer[] selectedSessionsIdList = new Integer[shippingSessions.size()];
			int i = 0;
			for (Iterator<Session3VO> iterator = shippingSessions.iterator(); iterator.hasNext();) {
				Session3VO shippingSession = iterator.next();
				CustomSessionBean shippingSessionDecorated = new CustomSessionBean(shippingSession);
				// adds sessionId to the selection IDs list
				selectedSessionsIdList[i] = shippingSession.getSessionId();

				// Add sessions already selected for this shipment, if they are not found in the availableSessionList
				if (!availableSessionsDecoratedList.contains(shippingSessionDecorated)) {
					availableSessionsDecoratedList.add(shippingSessionDecorated);
				}
				i++;
			}
			// Sort the available sessions list
			Collections.sort(availableSessionsDecoratedList);
			availableSessionsDecoratedList = new ArrayList<CustomSessionBean>(availableSessionsDecoratedList);

			// labcontact info for return
			LabContact3VO labContact = new LabContact3VO();
			Integer returnLabContactId = info.getReturnLabContactId();
			if (returnLabContactId != null) {
				labContact = labCService.findByPk(returnLabContactId);
			}
			form.setLabContact(labContact);

			//
			List<String> listDefaultCourierCompany = new ArrayList<String>();
			List<String> listCourierAccount = new ArrayList<String>();
			List<String> listBillingReference = new ArrayList<String>();
			List<String> listDewarAvgCustomsValue = new ArrayList<String>();
			List<String> listDewarAvgTransportValue = new ArrayList<String>();
			for (Iterator<LabContact3VO> c = listLabContacts.iterator(); c.hasNext();) {
				LabContact3VO lb = c.next();
				listDefaultCourierCompany.add(lb.getDefaultCourrierCompany() == null ? "''" : "'"
						+ lb.getDefaultCourrierCompany() + "'");
				listCourierAccount.add(lb.getCourierAccount() == null ? "''" : "'" + lb.getCourierAccount() + "'");
				listBillingReference
						.add(lb.getBillingReference() == null ? "''" : "'" + lb.getBillingReference() + "'");
				listDewarAvgCustomsValue.add(Integer.toString(lb.getDewarAvgCustomsValue()));
				listDewarAvgTransportValue.add(Integer.toString(lb.getDewarAvgTransportValue()));
			}
			form.setListDefaultCourierCompany(listDefaultCourierCompany);
			form.setListCourierAccount(listCourierAccount);
			form.setListBillingReference(listBillingReference);
			form.setListDewarAvgCustomsValue(listDewarAvgCustomsValue);
			form.setListDewarAvgTransportValue(listDewarAvgTransportValue);

			// Populate the Form
			if (info.getCreationDate() == null)
				info.setCreationDate(new Date());
			form.setInfo(info);
			form.setSendingLabContactId(info.getSendingLabContactId());
			form.setReturnLabContactId(info.getReturnLabContactId());

			form.setExperimentsScheduled(selectedSessionsIdList);

			form.setListLabContacts(listLabContacts);
			form.setListSessions(availableSessionsDecoratedList);

			if (info.getReturnLabContactId() != null){
				if (info.getSendingLabContactId() != null){
					form.setIsIdenticalReturnAddress(info.getSendingLabContactId().equals(info.getReturnLabContactId()));
				}
			}

			List<Dewar3VO> listDewars = dewarService.findFiltered(null, shippingId, Constants.PARCEL_DEWAR_TYPE, null,
					null, null, null, null, null, false, false);
			form.setNbDewars(listDewars.size());

			List<Dewar3VO> listOtherComps = dewarService.findFiltered(null, shippingId, Constants.PARCEL_TOOLBOX_TYPE,
					null, null, null, null, null, null, false, false);
			form.setNbOtherComponents(listOtherComps.size());

			// BC Form
			BreadCrumbsForm.getItClean(request).setSelectedShipping(selectedShipping);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.EDIT_MODE);
		} catch (Exception e) {
			e.printStackTrace();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return mapping.findForward("shippingCreatePage");
	}

	/**
	 * update
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward update(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();

		try {
			// ---------------------------------------------------------------------------------------------------

			// this.RetrieveSessionAttributes(request);
			Integer mProposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

			// Retrieve Attributes
			ViewShippingForm form = (ViewShippingForm) actForm; // Parameters submited by form

			// selected shipping
			Shipping3VO selectedShipping = BreadCrumbsForm.getIt(request).getSelectedShipping();
			Integer shippingId = selectedShipping.getShippingId();
			// ---------------------------------------------------------------------------------------------------

			// Retrieve Shipping information
			Shipping3VO updatedShipping = form.getInfo();
			if (updatedShipping.getCreationDate() == null)
				updatedShipping.setCreationDate(new Date());
			
			// retrieve sending labcontact
			LabContact3VO sendingLabContact = labCService.findByPk(form.getSendingLabContactId());
			updatedShipping.setSendingLabContactVO(sendingLabContact);

			// Return Lab Contact
			LabContact3VO returnLabContact = sendingLabContact;
			if (Constants.SITE_IS_MAXIV()) {
				if (!form.getIsIdenticalReturnAddress()) {
					LOG.debug("LabContact for return is different." + " Return LabContact Id will be : " + form.getReturnLabContactId());
					returnLabContact = labCService.findByPk(form.getReturnLabContactId());
				} else {
					returnLabContact = sendingLabContact;
				}

				if (form.getLabContact().getBillingReference() != null)
					returnLabContact.setBillingReference(form.getLabContact().getBillingReference());
				if (form.getLabContact().getCourierAccount() != null)
					returnLabContact.setCourierAccount(form.getLabContact().getCourierAccount());
				if (form.getLabContact().getDefaultCourrierCompany() != null)
					returnLabContact.setDefaultCourrierCompany(form.getLabContact().getDefaultCourrierCompany());
				if (form.getLabContact().getDewarAvgCustomsValue() != null)
					returnLabContact.setDewarAvgCustomsValue(form.getLabContact().getDewarAvgCustomsValue());
				if (form.getLabContact().getDewarAvgTransportValue() != null)
					returnLabContact.setDewarAvgTransportValue(form.getLabContact().getDewarAvgTransportValue());

				returnLabContact = labCService.update(returnLabContact);
				updatedShipping.setReturnLabContactVO(returnLabContact);
			}
			else {
				if (!form.getIsIdenticalReturnAddress()) {
					LOG.debug("LabContact for return is different." + " Return LabContact Id will be : " + form.getReturnLabContactId());
					returnLabContact = labCService.findByPk(form.getReturnLabContactId());
					updatedShipping.setReturnLabContactVO(returnLabContact);
				} else {
					updatedShipping.setReturnLabContactVO(sendingLabContact);
				}
			}

			Shipping3VO dbShipping = shippingService.findByPk(shippingId, true);

			// Retrieve information from DB ---------------------------------------------------------------
			List<LabContact3VO> listLabContacts = labCService.findFiltered(mProposalId, null);

			// list of all available sessions for this proposal
			List<Session3VO> availableSessionsList = ProposalUtils.getSessionsAvailable(request);
			// decores the sessions list
			List<CustomSessionBean> availableSessionsDecoratedList = decoreSessionList(availableSessionsList);

			// Retrieve list of session associated to this shipping

			List<Session3VO> shippingSessions = sessionService.findByShippingId(shippingId);
			// list of sessionIds linked to the current shipping
			Integer[] selectedSessionsIdList = new Integer[shippingSessions.size()];
			int n = 0;
			for (Iterator<Session3VO> iterator = shippingSessions.iterator(); iterator.hasNext();) {
				Session3VO shippingSession = iterator.next();
				CustomSessionBean shippingSessionDecorated = new CustomSessionBean(shippingSession);
				// adds sessionId to the selection IDs list
				selectedSessionsIdList[n] = shippingSession.getSessionId();

				// Add sessions already selected for this shipment, if they are not found in the availableSessionList
				if (!availableSessionsDecoratedList.contains(shippingSessionDecorated)) {
					availableSessionsDecoratedList.add(shippingSessionDecorated);
				}
				n++;
			}
			// Sort the available sessions list
			Collections.sort(availableSessionsDecoratedList);
			availableSessionsDecoratedList = new ArrayList<CustomSessionBean>(availableSessionsDecoratedList);

			// Populate the Form
			form.setListLabContacts(listLabContacts);
			form.setListSessions(availableSessionsDecoratedList);

			// Check Required fields populated --------------------------------------------------------
			if (!this.CheckRequiredFieldsPopulated(form, request))
				return mapping.findForward("shippingCreatePage");

			// ------------------------------------------------------------------------------------------

			ArrayList<String> fieldsChanged = new ArrayList<String>();
			boolean shippingHasChanged = false;

			// Checks modifications
			// comments
			if (updatedShipping.getComments() != null && !updatedShipping.getComments().equals(dbShipping.getComments())) {
				shippingHasChanged = true;
				dbShipping.setComments(updatedShipping.getComments());
				fieldsChanged.add("Comments changed to: '" + updatedShipping.getComments() + "'");
			}
			// sending labcontact
			if (!form.getSendingLabContactId().equals(dbShipping.getSendingLabContactId())) {
				shippingHasChanged = true;
				dbShipping.setSendingLabContactVO(updatedShipping.getSendingLabContactVO());
				LabContact3VO lcvo = labCService.findByPk(form.getSendingLabContactId());
				dbShipping.setReturnLabContactVO(lcvo);
				fieldsChanged.add("Lab-Contact for sending changed to: '" + lcvo.getCardName() + "'");
			}
			// return labcontact
			if (form.getReturnLabContactId() != null
					&& !form.getReturnLabContactId().equals(dbShipping.getReturnLabContactId())) {
				shippingHasChanged = true;
				LabContact3VO labContact = labCService.findByPk(form.getReturnLabContactId());
				dbShipping.setReturnLabContactVO(labContact);
				fieldsChanged.add("Lab-Contact for return changed to: '" + labContact.getCardName() + "'");
			}
			// Lab Contact -> Other case
			if (form.getReturnLabContactId() == null && form.getIsIdenticalReturnAddress()
					&& !form.getSendingLabContactId().equals(dbShipping.getReturnLabContactId())) {
				shippingHasChanged = true;
				LabContact3VO labContact = labCService.findByPk(form.getSendingLabContactId());
				dbShipping.setReturnLabContactVO(labContact);
				fieldsChanged.add("Lab-Contact for return changed to: '" + labContact.getCardName() + "'");
			}

			// --> LabContact return -> courier accounts ----------------------------------------------
			if (form.getReturnLabContactId() != null) {
				LabContact3VO existingLabContact = labCService.findByPk(form.getReturnLabContactId());
				if (!form.getLabContact().getDefaultCourrierCompany()
						.equals(existingLabContact.getDefaultCourrierCompany())
						|| !form.getLabContact().getCourierAccount().equals(existingLabContact.getCourierAccount())
						|| !form.getLabContact().getBillingReference().equals(existingLabContact.getBillingReference())
						|| !form.getLabContact().getDewarAvgCustomsValue()
								.equals(existingLabContact.getDewarAvgCustomsValue())
						|| !form.getLabContact().getDewarAvgTransportValue()
								.equals(existingLabContact.getDewarAvgTransportValue())) {// the user has changed the
																							// labContact info -> update
																							// of the labContact
					existingLabContact.setDefaultCourrierCompany(form.getLabContact().getDefaultCourrierCompany());
					existingLabContact.setCourierAccount(form.getLabContact().getCourierAccount());
					existingLabContact.setBillingReference(form.getLabContact().getBillingReference());
					existingLabContact.setDewarAvgCustomsValue(form.getLabContact().getDewarAvgCustomsValue());
					existingLabContact.setDewarAvgTransportValue(form.getLabContact().getDewarAvgTransportValue());

					existingLabContact = labCService.update(existingLabContact);
					LOG.debug("LabContact updated : " + existingLabContact);
				}
			}

			// sessions list
			Integer[] newSessionsList = form.getExperimentsScheduled();
			List<Session3VO> oldSessionsList = sessionService.findByShippingId(shippingId);

			if (!sameSessionIdList(newSessionsList, oldSessionsList)) {
				shippingHasChanged = true;
				// Broke the shipping -> session links
				dbShipping.clearSessions();

				if (newSessionsList != null){
					for (int i = 0; i < newSessionsList.length; i++) {
						int sessionId = newSessionsList[i];
						Session3VO session = sessionService.findByPk(sessionId, false, false, false);
						// Build the shipping -> session links
						dbShipping.addSession(session);
					}
				}
				// Issue 1742: update also the dewar firstExperimentId 
				if (newSessionsList != null && newSessionsList.length != 0){
					Dewar3VO[] dewars = dbShipping.getDewars();
					if (dewars != null){
						Session3VO ses = this.sessionService.findByPk(form.getExperimentsScheduled()[0], false, false, false);
						for (int i=0; i<dewars.length; i++){
							Dewar3VO dewar = dewars[i];
							dewar.setSessionVO(ses);
							dewarService.update(dewar);
						}
					}
				}
				fieldsChanged.add("Experiments scheduled have changed");
				// messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free",
				// "Make sure to reflect these experiment dates on your dewars"));
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail",
						"Make sure to reflect these experiment dates on your dewars"));
				saveErrors(request, errors);
			}

			// Check if information has changed /!\
			if (shippingHasChanged) {
				// Update information ---------------------
				dbShipping.setTimeStamp(new Timestamp(new Date().getTime())); // update timestamp
				shippingService.update(dbShipping);
				BreadCrumbsForm.getItClean(request).setSelectedShipping(dbShipping);

				// send an email report if shipping is already send to the ESRF
				if (dbShipping.getShippingStatus().equals(Constants.SHIPPING_STATUS_SENT_TO_ESRF)
						|| dbShipping.getShippingStatus().equals(Constants.SHIPPING_STATUS_AT_ESRF)) {
					// send email ----------------------
					// old ProposalFacadeLocal _proposalFacade = ProposalFacadeUtil.getLocalHome().create();
					// old ProposalLightValue proposal = _proposalFacade.findByPrimaryKeyLight(mProposalId);
					Proposal3VO proposal = this.proposalService.findByPk(mProposalId);

					// old PersonFacadeLocal _personFacade = PersonFacadeUtil.getLocalHome().create();
					// old PersonValue mainProposer = _personFacade.findByPrimaryKey(proposal.getPersonId());
					Person3VO mainProposer = this.person3Service.findByPk(proposal.getPersonVOId());

					reportShippingModificationByMail(request, proposal, mainProposer, dbShipping, fieldsChanged);
					// confirmation message
					messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free",
							"Notification mail sent to the stores"));
				}
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.update"));
			}

			saveMessages(request, messages);
			// WF: go to List of shippings instead of staying on the update page
			// return mapping.findForward("shippingCreatePage");
			return mapping.findForward("shippingViewPage");
		} catch (Exception e) {
			e.printStackTrace();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			e.printStackTrace();
			return mapping.findForward("error");
		}
	}

	/**
	 * save
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward save(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_response) {
		if (BreadCrumbsForm.getIt(request).getSelectedShipping() != null) // Redirect to the UPDATE
			return this.update(mapping, actForm, request, in_response);

		ActionForward f = saveShipping(mapping, actForm, request, in_response);
		if (f == null)
			return mapping.findForward("dewarViewPage");
		return f;
		
	}
	
	/**
	 * save
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward saveAndFill(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_response) {
		if (BreadCrumbsForm.getIt(request).getSelectedShipping() != null) // Redirect to the UPDATE
			return this.update(mapping, actForm, request, in_response);
		ActionForward f = saveShipping(mapping, actForm, request, in_response);
		if (f == null)
			return mapping.findForward("fillShipmentPage");
		return f;
		
	}
	
	/**
	 * save
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward saveShipping(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_response) {
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();

		Integer mProposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			ViewShippingForm form = (ViewShippingForm) actForm; // Parameters submited by form

			// ---------------------------------------------------------------------------------------------------

			// Check Required fields populated
			// Populate the Form
			if (!this.CheckRequiredFieldsPopulated(form, request)) {
				// retrieve list from DB
				List<LabContact3VO> listLabContacts = labCService.findFiltered(mProposalId, null);
				List<Session3VO> availableSessionsList = ProposalUtils.getSessionsAvailable(request);
				// decores the sessions list
				List<CustomSessionBean> availableSessionsDecoratedList = decoreSessionList(availableSessionsList);

				// Populate suggest list
				form.setListLabContacts(listLabContacts);
				form.setListSessions(availableSessionsDecoratedList);

				return mapping.findForward("shippingCreatePage");
			}

			// Check constraints
			if (!this.CheckConstraints(form, request, errors))
				return mapping.findForward("shippingCreatePage");

			// check shipping informations
			if (!this.checkShippingInformations(form, request)) {
				return mapping.findForward("shippingCreatePage");
			}

			// Populate the info from the form
			Proposal3VO proposalVO = proposalService.findByPk(mProposalId);
			Shipping3VO info = form.getInfo();
			info.setCreationDate(new Date());
			info.setProposalVO(proposalVO);
			info.setTimeStamp(new Timestamp(new Date().getTime()));
			info.setShippingStatus(Constants.SHIPPING_STATUS_OPENED);
			info.setShippingType(Constants.DEWAR_TRACKING_SHIPPING_TYPE);

			// retrieve sending labcontact
			LabContact3VO sendingLabContact = labCService.findByPk(form.getSendingLabContactId());
			
			info.setSendingLabContactVO(sendingLabContact);

			// Return Lab Contact
			LabContact3VO returnLabContact = sendingLabContact;
			if (!form.getIsIdenticalReturnAddress()) {
				LOG.debug("LabContact for return is different." + " Return LabContact Id will be : " + form.getReturnLabContactId());
				returnLabContact = labCService.findByPk(form.getReturnLabContactId());
			}

			if (form.getLabContact().getBillingReference() != null) 
				returnLabContact.setBillingReference(form.getLabContact().getBillingReference());
			if (form.getLabContact().getCourierAccount() != null) 
				returnLabContact.setCourierAccount(form.getLabContact().getCourierAccount());
			if (form.getLabContact().getDefaultCourrierCompany() != null) 
				returnLabContact.setDefaultCourrierCompany(form.getLabContact().getDefaultCourrierCompany());
			if (form.getLabContact().getDewarAvgCustomsValue() != null) 
				returnLabContact.setDewarAvgCustomsValue(form.getLabContact().getDewarAvgCustomsValue());
			if (form.getLabContact().getDewarAvgTransportValue() != null) 
				returnLabContact.setDewarAvgTransportValue(form.getLabContact().getDewarAvgTransportValue());
			
			returnLabContact = labCService.update(returnLabContact);
			info.setReturnLabContactVO(returnLabContact);

			
			// ---------------------------------------------------------------------------------------------------
			// Create the new Shipping into the database
			Shipping3VO createdShipping = shippingService.create(info);
			LOG.debug("Shipping created : " + createdShipping);

			// Experiments Scheduled
			Integer[] experimentsSelected = form.getExperimentsScheduled();
			if (experimentsSelected != null) {
				for (int i = 0; i < experimentsSelected.length; i++) {
					int sessionId = experimentsSelected[i];
					LOG.debug("sessionId to link  :" + sessionId);
					Session3VO session = sessionService.findByPk(sessionId, false, false, false);
					LOG.debug("session loaded : " + session);

					createdShipping.addSession(session);
				}
			}
			// update sessions
			shippingService.update(createdShipping);

			// Generate default parcels and attach them to the shipping
			// DEWARS ---------------
			if (form.getNbDewars() >= 1) {

				for (int i = 1; i <= form.getNbDewars(); i++) {
					// create a dewar
					Dewar3VO infoDewar = new Dewar3VO();
					infoDewar.setTimeStamp(new Timestamp(new Date().getTime()));
					// attach the dewar to the current shipping
					infoDewar.setShippingVO(createdShipping);
					infoDewar.setDewarStatus(Constants.SHIPPING_STATUS_OPENED);
					// fill default informations
					infoDewar.setCode("Dewar" + i);
					infoDewar.setBarCode(null);
					infoDewar.setType(DEWAR_TYPE);
					if (form.getExperimentsScheduled() != null && form.getExperimentsScheduled().length != 0)
						// old infoDewar.setFirstExperimentId(form.getExperimentsScheduled()[0]);
						infoDewar.setSessionVO(this.sessionService.findByPk(form.getExperimentsScheduled()[0], false,
								false, false));
					//  set default cost found in return labcontact
					
					infoDewar.setCustomsValue(returnLabContact.getDewarAvgCustomsValue());
					infoDewar.setTransportValue(returnLabContact.getDewarAvgTransportValue());
					// save it into the database
					infoDewar = dewarService.create(infoDewar);

					LOG.debug("Default dewar (" + i + ") created : " + infoDewar);
				}
			}
			// OTHER COMPONENTS -------
			if (form.getNbOtherComponents() >= 1) {
				// retrieve sending labcontact

				for (int i = 1; i <= form.getNbOtherComponents(); i++) {
					// create a dewar
					Dewar3VO infoDewar = new Dewar3VO();
					infoDewar.setTimeStamp(new Timestamp(new Date().getTime()));
					// attach the dewar to the current shipping
					infoDewar.setShippingVO(createdShipping);
					infoDewar.setDewarStatus(Constants.SHIPPING_STATUS_OPENED);
					infoDewar.setBarCode(null);
					// fill default informations
					infoDewar.setCode("Comp" + i);
					infoDewar.setType(TOOLBOX_TYPE);
					if (form.getExperimentsScheduled() != null && form.getExperimentsScheduled().length != 0)
						// old infoDewar.setFirstExperimentId(form.getExperimentsScheduled()[0]);
						infoDewar.setSessionVO(this.sessionService.findByPk(form.getExperimentsScheduled()[0], false,
								false, false));
					// set default cost found in sending labcontact
					infoDewar.setCustomsValue(sendingLabContact.getDewarAvgCustomsValue());
					infoDewar.setTransportValue(sendingLabContact.getDewarAvgTransportValue());
					// save it into the database
					infoDewar = dewarService.create(infoDewar);

					LOG.debug("Default dewar (" + i + ") created : " + infoDewar);
				}

			}

			// Set BreadCrumb with created Shipping
			BreadCrumbsForm.getItClean(request).setSelectedShipping(createdShipping);

			// ---------------------------------------------------------------------------------------------------
			// Acknowledge action
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.inserted", info.getShippingName()));
			saveMessages(request, messages);

			form.setInfo(new Shipping3VO());
			return null;
			
		} catch (Exception e) {
			e.printStackTrace();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			e.printStackTrace();
			return mapping.findForward("error");
		}
	}

	/**
	 * Go to edit/add lab contact card
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_response
	 * @return
	 */
	public ActionForward editLabContact(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_response) {
		ActionMessages errors = new ActionMessages();

		try {
			// Save form in Session
			HttpSession session = request.getSession();
			session.setAttribute(Constants.SHIPPING_FORM, actForm);

			// Get shippingId
			Shipping3VO selectedShipping = BreadCrumbsForm.getIt(request).getSelectedShipping();
			Integer shippingId = null;
			if (selectedShipping != null)
				shippingId = selectedShipping.getShippingId();

			// return mapping.findForward("labcontactLoginPage");
			in_response.sendRedirect(request.getContextPath()
					+ "/user/loginLabContactAction.do?reqCode=loginDisplay&shippingId=" + shippingId);
			return null;

		} catch (Exception e) {
			e.printStackTrace();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			e.printStackTrace();
			return mapping.findForward("error");
		}
	}

	/**
	 * return from edit/add lab contact card
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_response
	 * @return
	 */
	public ActionForward backFromLabContact(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_response) {
		ActionMessages errors = new ActionMessages();

		Integer mProposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

		try {
			ViewShippingForm form = (ViewShippingForm) actForm;

			// Retrieve saved form
			HttpSession session = request.getSession();
			ViewShippingForm savedForm = (ViewShippingForm) session.getAttribute(Constants.SHIPPING_FORM);

			// Populate form
			Shipping3VO info = savedForm.getInfo();
			form.setInfo(info);
			form.setNbDewars(savedForm.getNbDewars());
			form.setNbOtherComponents(savedForm.getNbOtherComponents());
			form.setExperimentsScheduled(savedForm.getExperimentsScheduled());

			// Refresh labcontact list
			List<LabContact3VO> listLabContacts = labCService.findFiltered(mProposalId, null);
			form.setListLabContacts(listLabContacts);
			form.setIsIdenticalReturnAddress(form.getSendingLabContactId() == null ? false : form.getSendingLabContactId().equals(form.getReturnLabContactId()));

			// List of all available sessions for this proposal
			List<Session3VO> availableSessionsList = ProposalUtils.getSessionsAvailable(request);
			List<CustomSessionBean> availableSessionsDecoratedList = decoreSessionList(availableSessionsList);
			Collections.sort(availableSessionsDecoratedList);
			availableSessionsDecoratedList = new ArrayList<CustomSessionBean>(availableSessionsDecoratedList);
			form.setListSessions(availableSessionsDecoratedList);
			form.setExperimentsScheduled(savedForm.getExperimentsScheduled());

			// labcontact info for return
			LabContact3VO labContact = new LabContact3VO();
			Integer returnLabContactId = form.getReturnLabContactId();
			if (returnLabContactId != null) {
				labContact = labCService.findByPk(returnLabContactId);
			}
			form.setLabContact(labContact);

			//
			List<String> listDefaultCourierCompany = new ArrayList<String>();
			List<String> listCourierAccount = new ArrayList<String>();
			List<String> listBillingReference = new ArrayList<String>();
			List<String> listDewarAvgCustomsValue = new ArrayList<String>();
			List<String> listDewarAvgTransportValue = new ArrayList<String>();
			for (Iterator<LabContact3VO> c = listLabContacts.iterator(); c.hasNext();) {
				LabContact3VO lb = c.next();
				listDefaultCourierCompany.add(lb.getDefaultCourrierCompany() == null ? "''" : "'"
						+ lb.getDefaultCourrierCompany() + "'");
				listCourierAccount.add(lb.getCourierAccount() == null ? "''" : "'" + lb.getCourierAccount() + "'");
				listBillingReference
						.add(lb.getBillingReference() == null ? "''" : "'" + lb.getBillingReference() + "'");
				listDewarAvgCustomsValue.add(Integer.toString(lb.getDewarAvgCustomsValue()));
				listDewarAvgTransportValue.add(Integer.toString(lb.getDewarAvgTransportValue()));
			}
			form.setListDefaultCourierCompany(listDefaultCourierCompany);
			form.setListCourierAccount(listCourierAccount);
			form.setListBillingReference(listBillingReference);
			form.setListDewarAvgCustomsValue(listDewarAvgCustomsValue);
			form.setListDewarAvgTransportValue(listDewarAvgTransportValue);

			// Edit or create
			Shipping3VO selectedShipping = BreadCrumbsForm.getIt(request).getSelectedShipping();
			if (selectedShipping != null && selectedShipping.getShippingId() != null) {
				// Existing
				FormUtils.setFormDisplayMode(request, actForm, FormUtils.EDIT_MODE);
			} else {
				// New one
				FormUtils.setFormDisplayMode(request, actForm, FormUtils.CREATE_MODE);
			}

			return mapping.findForward("shippingCreatePage");
		} catch (Exception e) {
			e.printStackTrace();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			e.printStackTrace();
			return mapping.findForward("error");
		}
	}

	/**
	 * CheckRequiredFieldsPopulated
	 * 
	 * @param form
	 *            The form to get Fields from. Checks that Required Fields are there. Redirect to
	 */

	public boolean CheckRequiredFieldsPopulated(ViewShippingForm form, HttpServletRequest request) {
		boolean requiredFieldsPresent = true;
		try {
			ActionMessages l_ActionMessages = new ActionMessages();
			// shipping name
			if (form.getInfo().getShippingName().length() == 0) {
				requiredFieldsPresent = false;

				ActionMessage l_ActionMessageLabel = new ActionMessage("errors.required", "Shipping Label"); // Shipping
																												// Label
				l_ActionMessages.add("info.shippingName", l_ActionMessageLabel);
			}
			// sending lab contact
			if (form.getSendingLabContactId() == 0) {
				requiredFieldsPresent = false;

				ActionMessage l_ActionMessageLabel = new ActionMessage("errors.required",
						"Lab-Contact for sending Label"); // Shipping Label
				l_ActionMessages.add("info.sendingLabContactId", l_ActionMessageLabel);
			}
			// return labcontact
			if (!form.getIsIdenticalReturnAddress()) {
				if (form.getReturnLabContactId() == null || form.getReturnLabContactId() == 0) {
					requiredFieldsPresent = false;

					ActionMessage l_ActionMessageLabel = new ActionMessage("errors.required",
							"Lab-Contact for return Label"); // Shipping Label
					l_ActionMessages.add("info.returnLabContactId", l_ActionMessageLabel);
				}
			}
			// number of dewars
			if (form.getNbDewars() == null || form.getNbDewars().intValue() < 0) {
				requiredFieldsPresent = false;

				ActionMessage l_ActionMessageLabel = new ActionMessage("errors.required", "Number of dewars Label"); // Shipping
																														// Label
				l_ActionMessages.add("nbDewars", l_ActionMessageLabel);
			}
			// number of others components
			if (form.getNbOtherComponents() == null) {
				requiredFieldsPresent = false;

				ActionMessage l_ActionMessageLabel = new ActionMessage("errors.required",
						"Number of other components Label"); // Shipping Label
				l_ActionMessages.add("nbOtherComponents", l_ActionMessageLabel);
			}
			// experiments scheduled (only if proposal has sessions)
			List<Session3VO> availableSessionsList = ProposalUtils.getSessionsAvailable(request);
			if (availableSessionsList != null && availableSessionsList.size() != 0) {
				if ((form.getExperimentsScheduled() == null)
						|| (form.getExperimentsScheduled().length == 1 && form.getExperimentsScheduled()[0] == 0)) {
					requiredFieldsPresent = false;

					ActionMessage l_ActionMessageLabel = new ActionMessage("errors.required",
							"Experiments scheduled Label"); // Shipping Label
					l_ActionMessages.add("experimentsScheduled", l_ActionMessageLabel);
				}
			}

			if (!requiredFieldsPresent) {
				request.setAttribute("org.apache.struts.action.ERROR", l_ActionMessages);
			}
		} catch (Exception e) {
			e.printStackTrace();
			requiredFieldsPresent = false;
			e.printStackTrace();
		}

		return requiredFieldsPresent;
	}

	/**
	 * CheckConstraints
	 * 
	 * @param form
	 * @param request
	 * @return
	 */
	public boolean CheckConstraints(ViewShippingForm form, HttpServletRequest request, ActionMessages errors)
			throws Exception {
		boolean constraintsOK = true;
		// ---------------------------------------------------------------------------------------------------
		// Shipment Name = shippingName -> must be unique
		// shippingServiceLocal shipping = shippingServiceUtil.getLocalHome().create();
		// ---------------------------------------------------------------------------------------------------
		// Acknowledge action
		// TODO this is used ? if yes, store this error in the properties
		// PBU Wrong error message on create shipment: code under comment
		// ActionMessage l_ActionMessagePassword = new ActionMessage("errors.detail",
		// "Sorry, but there are some problems reading your proposal account");
		// errors.add(ActionMessages.GLOBAL_MESSAGE,l_ActionMessagePassword);
		// saveErrors(request, errors);

		return constraintsOK;
	}

	/**
	 * Reports shipping modification to the stores, when the status of the shipping is : 'send to ESRF'
	 * 
	 * @param proposalCode
	 * @param proposalNumber
	 * @param shippingName
	 * @param experimentDate
	 * @param beamlineName
	 * @param fieldsChanged
	 */
	private void reportShippingModificationByMail(HttpServletRequest request, Proposal3VO proposal,
			Person3VO mainProposer, Shipping3VO shipping, List<String> fieldsChanged) {
		try {
			String mainProposerFamilyName = mainProposer.getFamilyName();
			mainProposerFamilyName = (mainProposerFamilyName != null) ? mainProposerFamilyName : "";
			String mainProposerFirstName = mainProposer.getGivenName();
			mainProposerFirstName = (mainProposerFirstName != null) ? mainProposerFirstName : "";

			String ispybMailAddress = Constants.getProperty("mail.from");
			// Send to storeAddress (or to storeAddress.test if not in production environment)
			String storesMailAddress = Constants.getProperty("mail.storeAddress.test");
			String serverName = request.getServerName().toLowerCase();
			if (Constants.isServerProd(serverName)) {
				storesMailAddress = Constants.getProperty("mail.storeAddress");
			}

			String subject = proposal.getCode() + proposal.getNumber() + " - " + shipping.getShippingName();
			String body = "Shipment name: " + shipping.getShippingName() + "\n" + "Main proposer name: "
					+ mainProposerFamilyName + " " + mainProposerFirstName + "\n" + "Proposal number: "
					+ proposal.getCode() + proposal.getNumber() + "\n"
					+ "---------------------------------------------------\n" + "Information changed by the user: \n";
			for (Iterator<String> iterator = fieldsChanged.iterator(); iterator.hasNext();) {
				String field = iterator.next();
				body += "  * " + field + "\n";
			}

			SendMailUtils.sendMail(ispybMailAddress, storesMailAddress, "", "", subject, body);
		} catch (MessagingException e) {
			LOG.error("Error during the sending of mail report: " + e.getMessage());
		}
	}

	/**
	 * Returns true if sessions IDs are equals in the two lists, false elsewhere
	 * 
	 * @param sessionIdList
	 * @param sessionsList
	 * @return
	 */
	private boolean sameSessionIdList(Integer[] sessionIdList, List<Session3VO> sessionsList) {
		int nb = sessionsList.size();
		if (sessionIdList == null && (sessionsList == null || nb < 1))
			return true;
		Integer[] idListRetrievedFromObjects = new Integer[nb];

		for (int i = 0; i < nb; i++) {
			Session3VO sessionValue = sessionsList.get(i);
			idListRetrievedFromObjects[i] = sessionValue.getSessionId();
		}
		return Arrays.equals(sessionIdList, idListRetrievedFromObjects);
	}

	/**
	 * Wraps the sessions list
	 * 
	 * @param sessionList
	 * @return
	 */
	public static ArrayList<CustomSessionBean> decoreSessionList(List<Session3VO> sessionList) {
		ArrayList<CustomSessionBean> decoratedSessionList = new ArrayList<CustomSessionBean>(sessionList.size());

		for (Iterator<Session3VO> iterator = sessionList.iterator(); iterator.hasNext();) {
			Session3VO sessionLightValue = iterator.next();

			decoratedSessionList.add(new CustomSessionBean(sessionLightValue));
		}

		return decoratedSessionList;
	}

	/**
	 * checks if the shipping informations in the form are correct and well formated, returns false if at least one
	 * field is incorrect
	 * 
	 * @param form
	 * @return
	 */
	private boolean checkShippingInformations(ViewShippingForm form, HttpServletRequest request) {
		ActionMessages l_ActionMessages = new ActionMessages();
		boolean isOk = true;
		// shipment label
		if (form.getInfo().getShippingName() != null
				&& form.getInfo().getShippingName().length() > DBConstants.MAX_LENGTH_SHIPPING_SHIPPINGNAME) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Shipment label",
					DBConstants.MAX_LENGTH_SHIPPING_SHIPPINGNAME);
			l_ActionMessages.add("info.shippingName", l_ActionMessageLabel);
		}
		// comments
		if (form.getInfo().getComments() != null
				&& form.getInfo().getComments().length() > DBConstants.MAX_LENGTH_SHIPPING_COMMENTS) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Comments",
					DBConstants.MAX_LENGTH_SHIPPING_COMMENTS);
			l_ActionMessages.add("info.comments", l_ActionMessageLabel);
		}
		// courier return
		if (form.getLabContact().getDefaultCourrierCompany() != null
				&& form.getLabContact().getDefaultCourrierCompany().length() > DBConstants.MAX_LENGTH_LABCONTACT_DEFAULTCOURRIERCOMPANY) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Courier Company for return",
					DBConstants.MAX_LENGTH_LABCONTACT_DEFAULTCOURRIERCOMPANY);
			l_ActionMessages.add("labContact.defaultCourrierCompany", l_ActionMessageLabel);
		}
		// courier account
		if (form.getLabContact().getCourierAccount() != null
				&& form.getLabContact().getCourierAccount().length() > DBConstants.MAX_LENGTH_LABCONTACT_COURIERACCOUNT) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Courier account",
					DBConstants.MAX_LENGTH_LABCONTACT_COURIERACCOUNT);
			l_ActionMessages.add("labContact.courierAccount", l_ActionMessageLabel);
		}
		// billing reference
		if (form.getLabContact().getBillingReference() != null
				&& form.getLabContact().getBillingReference().length() > DBConstants.MAX_LENGTH_LABCONTACT_BILLINGREFERENCE) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Billing reference",
					DBConstants.MAX_LENGTH_LABCONTACT_BILLINGREFERENCE);
			l_ActionMessages.add("labContact.billingReference", l_ActionMessageLabel);
		}
		if (!isOk) {
			request.setAttribute("org.apache.struts.action.ERROR", l_ActionMessages);
		}
		return isOk;
	}

	// end of CreateShippingAction
}
