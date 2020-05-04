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
 * ViewDewarAction.java
 * @author ludovic.launer@esrf.fr
 * Dec 17, 2004
 */

package ispyb.client.common.shipping;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ispyb.common.util.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.util.Confidentiality;
import ispyb.client.common.util.MISServletUtils;
import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.common.util.DBTools;
import ispyb.common.util.PDFFormFiller;
import ispyb.server.common.services.proposals.LabContact3Service;
import ispyb.server.common.services.proposals.Laboratory3Service;
import ispyb.server.common.services.proposals.Person3Service;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.services.shipping.Container3Service;
import ispyb.server.common.services.shipping.Dewar3Service;
import ispyb.server.common.services.shipping.DewarTransportHistory3Service;
import ispyb.server.common.services.shipping.Shipping3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Laboratory3VO;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.DewarTransportHistory3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.vos.collections.Session3VO;

/**
 * @struts.action name="viewDewarForm" path="/user/viewDewarAction" type="ispyb.client.common.shipping.ViewDewarAction"
 *                validate="false" parameter="reqCode" scope="request"
 * 
 * @struts.action name="viewDewarForm" path="/reader/viewDewarAction" type="ispyb.client.common.shipping.ViewDewarAction"
 *                validate="false" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="dewarViewOldPage" path="user.dewar.view.old.page"
 * 
 * @struts.action-forward name="dewarViewPage" path="user.shipping.dewar.view.page"
 * 
 * @struts.action-forward name="dewarReimbursePage" path="user.shipping.dewar.reimburse.page"
 * 
 * @struts.action-forward name="dewarBlomViewPage" path="blom.shipping.dewar.view.page"
 * 
 * @struts.action-forward name="dewarFedexmanagerViewPage" path="fedexmanager.shipping.dewar.view.page"
 * 
 * @struts.action-forward name="dewarStoreViewPage" path="store.shipping.dewar.view.page"
 * 
 * @struts.action-forward name="dewarLocalcontactViewPage" path="localcontact.shipping.dewar.view.page"
 * 
 * @struts.action-forward name="dewarLabelsPage" path="user.shipping.dewar.labels.page"
 * 
 * @struts.action-forward name="dewarManagerViewPage" path="manager.shipping.dewar.view.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 */

public class ViewDewarAction extends org.apache.struts.actions.DispatchAction {

	private final Logger LOG = Logger.getLogger(ViewDewarAction.class);

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Session3Service sessionService;

	private Dewar3Service dewar3Service;

	private Shipping3Service shippingService;

	private Container3Service containerService;

	private LabContact3Service labContact3Service;

	private Person3Service person3Service;

	private Laboratory3Service laboratory3Service;

	private DewarTransportHistory3Service dewarTransportHistory3Service;

	private SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

	/**
	 * Initialize the needed services.
	 *
	 * @throws Exception
	 */
	private void initServices() throws CreateException, NamingException {

		this.sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
		this.dewar3Service = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);
		this.containerService = (Container3Service) ejb3ServiceLocator.getLocalService(Container3Service.class);
		this.shippingService = (Shipping3Service) ejb3ServiceLocator.getLocalService(Shipping3Service.class);
		this.labContact3Service = (LabContact3Service) ejb3ServiceLocator.getLocalService(LabContact3Service.class);
		this.person3Service = (Person3Service) ejb3ServiceLocator.getLocalService(Person3Service.class);
		this.laboratory3Service = (Laboratory3Service) ejb3ServiceLocator.getLocalService(Laboratory3Service.class);
		this.dewarTransportHistory3Service = (DewarTransportHistory3Service) ejb3ServiceLocator
				.getLocalService(DewarTransportHistory3Service.class);

	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
								 final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	// Session Attributes
	// Don't use local variable in struts actions
	// Integer mProposalId = null;

	/**
	 * display
	 *
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_response
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
								 HttpServletResponse in_response) {
		ActionMessages errors = new ActionMessages();
		// ---------------------------------------------------------------------------------------------------
		// Retrieve Attributes
		String strShippingId = request.getParameter(Constants.SHIPPING_ID); // Parameters submited by link
		String shippingIdFromContainerId = request.getParameter(Constants.SHIPPING_ID_FROM_CONTAINER_ID);
		try {
			Shipping3VO selectedShipping = null;
			if (shippingIdFromContainerId != null) {
				Integer shippingId = DBTools.getShippingIdFromContainerId(new Integer(shippingIdFromContainerId));

				// selectedShipping = DBTools.getSelectedShipping(shippingId);
				selectedShipping = this.shippingService.findByPk(shippingId, true);
				BreadCrumbsForm.getItClean(request).setSelectedShipping(selectedShipping);
			}

			if (strShippingId != null) // Selected Shipping
			{
				selectedShipping = DBTools.getSelectedShipping(new Integer(strShippingId));
				BreadCrumbsForm.getItClean(request).setSelectedShipping(selectedShipping);
			}
			BreadCrumbsForm.getIt(request).setSelectedDewar(null);

			// Confidentiality (check if object proposalId matches)
			if (selectedShipping != null
					&& !Confidentiality.isAccessAllowed(request, selectedShipping.getProposalVO().getProposalId())) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Access denied"));
				saveErrors(request, errors);
				return (mapping.findForward("error"));
			}

		} catch (Exception e) {
			LOG.error(e.toString());
			e.printStackTrace();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			saveErrors(request, errors);
			return (mapping.findForward("error"));

		}
		return this.displaySlave(mapping, actForm, request, in_response);
	}

	/**
	 * readOnly
	 *
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_response
	 * @return
	 */
	public ActionForward readOnly(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
								  HttpServletResponse in_response) {
		ActionMessages errors = new ActionMessages();
		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			ViewDewarForm form = (ViewDewarForm) actForm;
			String shippingId = request.getParameter(Constants.SHIPPING_ID); // Parameters submited by link

			// redirect to the role view page
			RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
			String role = roleObject.getName();

			form.setRole(role);
			// ---------------------------------------------------------------------------------------------------

			if (shippingId != null) // Selected Shipping
			{
				Shipping3VO selectedShipping = DBTools.getSelectedShipping(new Integer(shippingId));
				BreadCrumbsForm.getItClean(request).setSelectedShipping(selectedShipping);
			}
			BreadCrumbsForm.getIt(request).setSelectedDewar(null);

			Integer proposalId = null;

			// proposalId is retrieved from the shipping for the others roles
			// and stored in session
			if (role.equals(Constants.ROLE_STORE) || role.equals(Constants.ROLE_BLOM)
					|| role.equals(Constants.FXMANAGE_ROLE_NAME) || role.equals(Constants.ROLE_LOCALCONTACT)
					|| role.equals(Constants.ROLE_MANAGER)) {
				proposalId = BreadCrumbsForm.getIt(request).getSelectedShipping().getProposalVO().getProposalId();
				request.getSession().setAttribute(Constants.PROPOSAL_ID, proposalId);
				if (proposalId != null) {
				}

			} else {// User Role
				proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			}

			// Confidentiality (check if object proposalId matches)
			if (!Confidentiality.isAccessAllowedToShipping(request, Integer.valueOf(shippingId))) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Access denied"));
				saveErrors(request, errors);
				return (mapping.findForward("error"));
			}

			List<Dewar3VO> listInfo = searchDewars(mapping, "", "", proposalId, shippingId, null);

			// -----------------------------------------------------
			// Populate form
			populateForm(listInfo, form);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

			if (role.equals(Constants.ROLE_STORE)) {
				return mapping.findForward("dewarStoreViewPage");
			} else if (role.equals(Constants.ROLE_BLOM)) {
				return mapping.findForward("dewarBlomViewPage");
			} else if (role.equals(Constants.FXMANAGE_ROLE_NAME)) {
				return mapping.findForward("dewarFedexmanagerViewPage");
			} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
				return mapping.findForward("dewarLocalcontactViewPage");
			} else if (role.equals(Constants.ROLE_MANAGER)) {
				return mapping.findForward("dewarManagerViewPage");
			} else {// DEFAULT : User view
				return mapping.findForward("dewarViewPage");
			}
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			e.printStackTrace();
			return mapping.findForward("error");
		}
	}

	public ActionForward generateLabels(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
										HttpServletResponse in_response) {

		ActionMessages errors = new ActionMessages();
		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			String sDewarId = request.getParameter(Constants.DEWAR_ID); // Parameters submited by link

			// ---------------------------------------------------------------------------------------------------
			// Check mandatory attributes are present
			if (sDewarId == null || sDewarId.equals("")) {
				// redirect to the shipment page
				return this.search(mapping, actForm, request, in_response);
			}

			// ---------------------------------------------------------------------------------------------------
			// Retrieve dewar object ---------------------------
			Integer dewarId = new Integer(sDewarId);
			Dewar3VO dewar = dewar3Service.findByPk(dewarId, false, false);

			// Retrieve dewar object ---------------------------
			Integer sessionId = null;
			if (dewar.getSessionVO() != null)
				sessionId = dewar.getSessionVO().getSessionId();

			// SessionValue session = sessionService.findByPrimaryKey(sessionId);
			Session3VO session = null;
			if (sessionId != null)
				session = sessionService.findByPk(sessionId, false, false, false);

			// Retrieve shipment object ------------------------
			Shipping3VO shipping = dewar.getShippingVO();
			shipping = shippingService.loadEager(shipping);

			// Retrieve SENDING labcontact object ---------------
			Integer sendingLabContactId = shipping.getSendingLabContactId();
			LabContact3VO sendingLabContact = this.labContact3Service.findByPk(sendingLabContactId);

			// Retrieve SENDING person object
			Integer sendingPersonId = sendingLabContact.getPersonVOId();
			Person3VO sendingPerson = this.person3Service.findByPk(sendingPersonId);

			// Retrieve SENDING laboratory object
			Integer sendingLaboratoryId = sendingPerson.getLaboratoryVOId();
			Laboratory3VO sendingLaboratory = this.laboratory3Service.findByPk(sendingLaboratoryId);
			// Retrieve RETURN labcontact object ----------------
			Integer returnLabContactId = shipping.getReturnLabContactId();
			LabContact3VO returnLabContact = this.labContact3Service.findByPk(returnLabContactId);

			// Retrieve RETURN person object
			Integer returnPersonId = returnLabContact.getPersonVOId();
			Person3VO returnPerson = this.person3Service.findByPk(returnPersonId);

			// Retrieve RETURN laboratory object
			Integer returnLaboratoryId = returnPerson.getLaboratoryVOId();
			Laboratory3VO returnLaboratory = this.laboratory3Service.findByPk(returnLaboratoryId);

			// ---------------------------------------------------------------------------------------------------
			// PDF Labels generation
			PDFFormFiller pdfFormFiller = new PDFFormFiller();

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			// try {
			if (returnLabContact.getDefaultCourrierCompany() != null
					&& returnLabContact.getDefaultCourrierCompany().equals(
					Constants.SHIPPING_DELIVERY_AGENT_NAME_WORLDCOURIER)) {
				pdfFormFiller.init(
						request.getRealPath(Constants.TEMPLATE_PDF_PARCEL_LABELS_WORLDCOURIER_RELATIVE_PATH),
						outputStream);
			} else {
				pdfFormFiller.init(request.getRealPath(Constants.TEMPLATE_PDF_PARCEL_LABELS_RELATIVE_PATH),
						outputStream);
			}

			// } catch (Exception e) {
			// System.out.println("Erreur pendant 'init' : "+e.getMessage());
			// }

			// Date format
			SimpleDateFormat dateStandard = new SimpleDateFormat(Constants.DATE_FORMAT);

			Proposal3VO proposal = shipping.getProposalVO();

			// Properties to fill in the labels
			Map<String, String> fieldNamesAndValues = new HashMap<String, String>();
			fieldNamesAndValues.put("TF_parcelLabel", dewar.getCode());
			fieldNamesAndValues.put("TF_parcelBarcode", "*" + dewar.getBarCode() + "*");

			fieldNamesAndValues.put("TF_shipmentName", shipping.getShippingName());
			fieldNamesAndValues.put("TF_parcelsNumber", Integer.toString(shipping.getDewarVOs().size()));
			fieldNamesAndValues.put("TF_proposalNumber", proposal.getCode() + "-" + proposal.getNumber());

			// Session values (only if they exist)
			if (session != null) {
				fieldNamesAndValues.put("TF_beamline", session.getBeamlineName());
				fieldNamesAndValues.put("TF_experimentDate",
						dateStandard.format(new Date(session.getStartDate().getTime())));
				fieldNamesAndValues.put("TF_localContactName", session.getBeamlineOperator());
			} else {
				fieldNamesAndValues.put("TF_beamline", "unknown");
				fieldNamesAndValues.put("TF_experimentDate", "unknown");
				fieldNamesAndValues.put("TF_localContactName", "unknown");
			}

			fieldNamesAndValues.put("TF_sendingLabContactName", sendingPerson.getFamilyName().toUpperCase() + " "
					+ sendingPerson.getGivenName());

			// Phone (only if exists)
			String sendingPhone = "unknown";
			if (sendingPerson.getPhoneNumber() != null)
				sendingPhone = sendingPerson.getPhoneNumber();
			fieldNamesAndValues.put("TF_sendingLabContactTel", sendingPhone);

			// Fax (only if exists)
			String sendingFax = "unknown";
			if (sendingPerson.getFaxNumber() != null)
				sendingFax = sendingPerson.getFaxNumber();
			fieldNamesAndValues.put("TF_sendingLabContactFax", sendingFax);

			fieldNamesAndValues.put("TF_sendingLaboratoryName", sendingLaboratory.getName());
			/*if (Constants.SITE_IS_MAXIV()) {
				fieldNamesAndValues.put("TF_sendingLaboratoryAddress", StringUtils.breakString(sendingLaboratory.getAddress(), 30));
			} else {
				fieldNamesAndValues.put("TF_sendingLaboratoryAddress", sendingLaboratory.getAddress());
			}*/

			fieldNamesAndValues.put("TF_sendingLaboratoryAddress", sendingLaboratory.getAddress());
			fieldNamesAndValues.put("TF_returnLabContactName", returnPerson.getFamilyName().toUpperCase() + " "
					+ returnPerson.getGivenName());

			// Phone (only if exists)
			String returnPhone = "unknown";
			if (sendingPerson.getPhoneNumber() != null)
				returnPhone = sendingPerson.getPhoneNumber();
			fieldNamesAndValues.put("TF_returnLabContactTel", returnPhone);

			// Fax (only if exists)
			String returnFax = "unknown";
			if (sendingPerson.getFaxNumber() != null)
				returnFax = sendingPerson.getFaxNumber();
			fieldNamesAndValues.put("TF_returnLabContactFax", returnFax);

			fieldNamesAndValues.put("TF_returnLaboratoryName", returnLaboratory.getName());
			fieldNamesAndValues.put("TF_returnLaboratoryAddress", returnLaboratory.getAddress());

			// default courier company (only if exists)
			String defaultCourrierCompany = "unknown";
			if (returnLabContact.getDefaultCourrierCompany() != null)
				defaultCourrierCompany = returnLabContact.getDefaultCourrierCompany();
			fieldNamesAndValues.put("TF_returnCourierCompany", defaultCourrierCompany);

			fieldNamesAndValues.put("TF_returnCourierAccount", returnLabContact.getCourierAccount());
			fieldNamesAndValues.put("TF_returnBillingReference", returnLabContact.getBillingReference());
			fieldNamesAndValues.put("TF_returnCustomsValue",
					Integer.toString(returnLabContact.getDewarAvgCustomsValue()));
			fieldNamesAndValues.put("TF_returnTransportValue",
					Integer.toString(returnLabContact.getDewarAvgTransportValue()));

			// if dewar reimbursed then we replace the courier infos
			if (dewar.getIsReimbursed() != null && dewar.getIsReimbursed().equals(true)) {
				fieldNamesAndValues.put("TF_returnCourierCompany", Constants.SHIPPING_DELIVERY_AGENT_NAME_FEDEX);
				fieldNamesAndValues.put("TF_returnCourierAccount", Constants.SHIPPING_DELIVERY_AGENT_FEDEX_ACCOUNT);
			}

			pdfFormFiller.setFields(fieldNamesAndValues);

			try {
				pdfFormFiller.render();
			} catch (Exception e) {
				System.out.println("Erreur pendant 'render' : " + e.getMessage());
			}

			// Change dewar status if opened
			String dewarStatus = dewar.getDewarStatus();
			System.out.println("dewarStatus= " + dewarStatus);
			if (dewarStatus == null || dewarStatus.equals("") || dewarStatus.equals(Constants.SHIPPING_STATUS_OPENED)) {
				// Update status
				dewar.setDewarStatus(Constants.SHIPPING_STATUS_READY_TO_GO);
				dewar3Service.update(dewar);
				// Refresh dewarFullFacade
				// old DewarFullFacadeLocal dewarFullFacade = DewarFullFacadeUtil.getLocalHome().create();
				// old DewarFullValue dewarFull = dewarFullFacade.findByPrimaryKey(dewarId);
				Dewar3VO dewarFull = this.dewar3Service.findByPk(dewarId, false, false);
				dewarFull.setDewarStatus(Constants.SHIPPING_STATUS_READY_TO_GO);
			}

			// Add event to history
			Timestamp dateTime = getDateTime();
			// old DewarTransportHistoryFacadeLocal _dewarTransportHistoryFacade =
			// DewarTransportHistoryFacadeUtil.getLocalHome().create();
			// old DewarTransportHistoryLightValue newHistory = new DewarTransportHistoryLightValue();
			DewarTransportHistory3VO newHistory = new DewarTransportHistory3VO();
			newHistory.setDewarStatus(Constants.SHIPPING_STATUS_READY_TO_GO);
			newHistory.setStorageLocation("");
			newHistory.setArrivalDate(dateTime);
			// old newHistory.setDewarId(dewarId);
			newHistory.setDewarVO(dewar);
			// old _dewarTransportHistoryFacade.create(newHistory);
			this.dewarTransportHistory3Service.create(newHistory);

			// Utilisation du stream PDF dans l'action struts
			byte[] pdfContent = outputStream.toByteArray();

			MISServletUtils.sendToBrowser(in_response/* HttpServletResponse */, new ByteArrayInputStream(pdfContent),
					new Integer(pdfContent.length), "application/pdf", dewar.getCode() + ".pdf"/* fileName */, // !!!
					// =>
					// Parcel
					// name
					// !
					false/* inLine */, true/* forceAttachment */);

			return null; // !!! do not use a findForward
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			e.printStackTrace();
			return mapping.findForward("error");
		}
	}

	/**
	 * displaySlave Populate the information for the selected Basket
	 *
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward displaySlave(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
									  HttpServletResponse in_response) {
		ActionMessages errors = new ActionMessages();
		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			ViewDewarForm form = (ViewDewarForm) actForm;
			String sDewarId = request.getParameter(Constants.DEWAR_ID); // Parameters submited by link

			// Check mandatory attributes are present
			if (sDewarId == null && BreadCrumbsForm.getIt(request).getSelectedDewar() == null) {
				// Dewar3VO defaultSelectedDewar = (Dewar3VO)form.getListInfo().get(0);
				// BreadCrumbsForm.getIt(request).setSelectedDewar(defaultSelectedDewar);
				return this.search(mapping, actForm, request, in_response);
			}

			// Get attributes from Link or BreadCrumbs
			Integer dewarId = null;
			if (sDewarId != null)
				dewarId = new Integer(sDewarId);
			else
				dewarId = BreadCrumbsForm.getIt(request).getSelectedDewar().getDewarId();

			// Confidentiality (check if proposalId matches)
			if (!Confidentiality.isAccessAllowedToDewar(request, dewarId)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Access denied"));
				saveErrors(request, errors);
				return (mapping.findForward("error"));
			}

			// ---------------------------------------------------------------------------------------------------
			// Build Query
			// old ContainerFacadeLocal containers = ContainerFacadeUtil.getLocalHome().create();

			// Retrieve information from DB
			List<Container3VO> listInfoSlave = null;
			Dewar3VO selectedDewar = null;
			if (dewarId.intValue() != 0) // View Container from selected Dewar
			{
				// old listInfoSlave = (List) containers.findByDewarId(dewarId);
				listInfoSlave = this.containerService.findByDewarId(dewarId);

				selectedDewar = DBTools.getSelectedDewar(dewarId);
			} else // View Container from ALL Dewars
			{
				// old CHECK listInfoSlave = (List) containers.findByDewarId(dewarId);
				listInfoSlave = this.containerService.findByDewarId(dewarId);
				return mapping.findForward("dewarViewPage");
			}

			// Populate with Info
			form.setListInfoSlave(listInfoSlave);

			// Populate BreadCrumbs
			BreadCrumbsForm.getIt(request).setSelectedDewar(selectedDewar);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			e.printStackTrace();
			return mapping.findForward("error");
		}
		return this.search(mapping, actForm, request, in_response);
	}

	/**
	 * search
	 *
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward search(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
								HttpServletResponse in_response) {
		ActionMessages errors = new ActionMessages();
		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			ViewDewarForm form = (ViewDewarForm) actForm;
			String code = form.getCode(); // Parameters submited by form
			String comments = form.getComments();

			// this.RetrieveSessionAttributes(request); // Session parameters
			String shippingId = null;
			String dewarId = null;
			Integer mProposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

			if (BreadCrumbsForm.getIt(request).getSelectedShipping() != null)
				shippingId = BreadCrumbsForm.getIt(request).getSelectedShipping().getShippingId().toString();

			if (BreadCrumbsForm.getIt(request).getSelectedDewar() != null)
				dewarId = BreadCrumbsForm.getIt(request).getSelectedDewar().getDewarId().toString();

			// ---------------------------------------------------------------------------------------------------
			// Build Query
			// -----------------------------------------------------

			// Search Dewars
			if (mProposalId == null) {  // proposalId
				return mapping.findForward("error");
			}

			List<Dewar3VO> listInfo = searchDewars(mapping, code, comments, mProposalId, shippingId, dewarId);

			// -----------------------------------------------------
			// Populate form
			populateForm(listInfo, form);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.EDIT_MODE);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			e.printStackTrace();
			return mapping.findForward("error");
		}

		// redirect to the role view page
		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();
		if (role.equals(Constants.ROLE_STORE)) {
			return mapping.findForward("dewarStoreViewPage");
		} else if (role.equals(Constants.ROLE_BLOM)) {
			return mapping.findForward("dewarBlomViewPage");
		} else if (role.equals(Constants.FXMANAGE_ROLE_NAME)) {
			return mapping.findForward("dewarFedexmanagerViewPage");
		} else if (role.equals(Constants.ROLE_MANAGER)) {
			return mapping.findForward("dewarManagerViewPage");
		} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
			return mapping.findForward("dewarLocalcontactViewPage");
		} else {// DEFAULT : User view
			return mapping.findForward("dewarViewPage");
		}
	}

	public ActionForward getReimbursed(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
									   HttpServletResponse in_response) {
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		String dewarId = request.getParameter(Constants.DEWAR_ID);
		ViewDewarForm form = (ViewDewarForm) actForm;

		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes

			// Retrieve Dewar information
			Dewar3VO selectedDewar = DBTools.getSelectedDewar(new Integer(dewarId));
			Dewar3VO info = selectedDewar;
			form.setNbReimbursedDewars(info.getSessionVO().getNbReimbDewars());
			form.setInfo(info);
			form.setFedexCode(info.getSessionVO().getProposalVO().getCode().toUpperCase() + "-" + info.getSessionVO().getProposalVO().getNumber() + "/"
					+ info.getSessionVO().getBeamlineName() + "/" + df.format(info.getSessionVO().getStartDate()));
			BreadCrumbsForm.getIt(request).setSelectedDewar(info);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free", "According to the A-form for this experiment, you are allowed to select " + info.getSessionVO().getNbReimbDewars() + " dewars to be reimbursed by the ESRF"));

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			e.printStackTrace();
			return mapping.findForward("error");
		}
		saveMessages(request, messages);
		return mapping.findForward("dewarReimbursePage");

	}

	public ActionForward setReimbursed(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
									   HttpServletResponse in_response) {
		ActionMessages errors = new ActionMessages();
		ViewDewarForm form = (ViewDewarForm) actForm;

		try {
			// Retrieve Dewar information
			Dewar3VO selectedDewar = BreadCrumbsForm.getIt(request).getSelectedDewar();
			selectedDewar.setIsReimbursed(form.getInfo().getIsReimbursed());
			dewar3Service.update(selectedDewar);

			// we erase the selected dewar to retrieve the full search with all dewars belonging to shipment
			BreadCrumbsForm.getIt(request).setSelectedDewar(null);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			e.printStackTrace();
			return mapping.findForward("error");
		}
		return this.search(mapping, actForm, request, in_response);

	}


	private List<Dewar3VO> searchDewars(ActionMapping mapping, String code, String comments, Integer proposalId,
										String shippingId, String dewarId) throws CreateException, NamingException, FinderException {

		Integer idShipping = null;
		if (shippingId != null) {
			idShipping = new Integer(shippingId);
		}

		Integer Iddewar = null;
		if (dewarId != null) {
			Iddewar = new Integer(dewarId);
		}
		try {
			code = code.replace('*', '%');
			comments = comments.replace('*', '%');
			return dewar3Service.findFiltered(proposalId, idShipping, null, code, comments, null, null, null, null,
					Iddewar, false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void populateForm(List<Dewar3VO> listInfo, ViewDewarForm form) {
		Integer currentReimbursed = 0;
		Integer nbReimbursed = 0;

		if (!listInfo.isEmpty()) {
			for (Iterator<Dewar3VO> iterator = listInfo.iterator(); iterator.hasNext(); ) {
				Dewar3VO dewar3vo = (Dewar3VO) iterator.next();
				if (dewar3vo.getIsReimbursed() != null && dewar3vo.getIsReimbursed().equals(true))
					currentReimbursed++;
			}
			Dewar3VO defaultSelectedDewar = (Dewar3VO) listInfo.get(0);
			if (defaultSelectedDewar.getSessionVO() != null) {
				nbReimbursed = defaultSelectedDewar.getSessionVO().getNbReimbDewars();
				form.setFedexCode(defaultSelectedDewar.getSessionVO().getProposalVO().getCode().toUpperCase() + "-" + defaultSelectedDewar.getSessionVO().getProposalVO().getNumber() + "/"
						+ defaultSelectedDewar.getSessionVO().getBeamlineName() + "/" + df.format(defaultSelectedDewar.getSessionVO().getStartDate()));
			}
		}

		form.setNbReimbursedDewars(nbReimbursed);
		form.setCurrentReimbursedDewars(currentReimbursed);
		form.setRemainingReimbursed(false);
		if (nbReimbursed != null && currentReimbursed < form.getNbReimbursedDewars())
			form.setRemainingReimbursed(true);

		// -----------------------------------------------------
		// Populate with Info
		form.setListInfo(listInfo);
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
