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
 * createDewar.java
 * @author ludovic.launer@esrf.fr
 * Feb 24, 2005
 */

package ispyb.client.common.shipping;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.proposal.ProposalUtils;
import ispyb.client.security.roles.RoleDO;
import ispyb.client.common.util.DBConstants;
import ispyb.common.util.DBTools;
import ispyb.common.util.Constants;
import ispyb.common.util.SendMailUtils;
import ispyb.server.common.services.proposals.LabContact3Service;
import ispyb.server.common.services.proposals.Person3Service;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.services.shipping.Dewar3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.vos.collections.Session3VO;

import java.sql.Timestamp;
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
 * 
 * @struts.action name="viewDewarForm" path="/user/createDewarAction" type="ispyb.client.common.shipping.CreateDewarAction"
 *                input="user.shipping.dewar.create.page" validate="false" parameter="reqCode" scope="session"
 * 
 * @struts.action-forward name="userDewarCreatePage" path="user.shipping.dewar.create.page"
 * @struts.action-forward name="blomDewarCreatePage" path="blom.shipping.dewar.create.page"
 * @struts.action-forward name="fedexmanagerDewarCreatePage" path="fedexmanager.shipping.dewar.create.page"
 * @struts.action-forward name="storeDewarCreatePage" path="store.shipping.dewar.create.page"
 * @struts.action-forward name="managerDewarCreatePage" path="manager.shipping.dewar.create.page"
 * @struts.action-forward name="localcontactDewarCreatePage" path="localcontact.shipping.dewar.create.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 * @struts.action-forward name="dewarViewPage" path="/user/viewDewarAction.do?reqCode=search"
 * 
 * @struts.action-forward name="dewarViewPageDisplaySlave" path=
 *                        "/menuSelected.do?leftMenuId=11&amp;topMenuId=10&amp;targetUrl=%2Fuser%2FviewDewarAction.do%3FreqCode%3DdisplaySlave"
 * 
 * 
 */
public class CreateDewarAction extends org.apache.struts.actions.DispatchAction {
	private final Logger LOG = Logger.getLogger(CreateDewarAction.class);

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Session3Service sessionService;

	private Proposal3Service proposalService;

	private Dewar3Service dewarService;

	private Person3Service person3Service;

	private LabContact3Service labContact3Service;

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {

		this.sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
		this.proposalService = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
		this.dewarService = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);
		this.person3Service = (Person3Service) ejb3ServiceLocator.getLocalService(Person3Service.class);
		this.labContact3Service = (LabContact3Service) ejb3ServiceLocator.getLocalService(LabContact3Service.class);

	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	// Session Attributes
	// Instance and static variables should not be used in an Action class to store
	// information related to the state of a particular request.
	// (Bug: it can create dewars in other proposals!)
	// Integer mProposalId = null;

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
			ViewDewarForm form = (ViewDewarForm) actForm; // Parameters submited by form

			// this.RetrieveSessionAttributes(request); // Session parameters
			Integer mProposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

			BreadCrumbsForm.getIt(request).setSelectedDewar(null);
			Integer shippingId = null;
			// ---------------------------------------------------------------------------------------------------

			Shipping3VO selecteShipping = null;

			String shippingIdStr = request.getParameter(Constants.SHIPPING_ID);
			if (shippingIdStr != null) // Request parameter
				shippingId = new Integer(shippingIdStr);
			else if (BreadCrumbsForm.getIt(request).getSelectedShipping() != null) // BreadCrumbForm
				shippingId = BreadCrumbsForm.getIt(request).getSelectedShipping().getShippingId();

			LOG.debug("viewDewarAction - display: " + shippingId);

			if (shippingId != null) {
				// Populate BreadCrumbs
				selecteShipping = DBTools.getSelectedShipping(shippingId);
				BreadCrumbsForm.getIt(request).setSelectedShipping(selecteShipping);

				// Retrieve list of session associated to this shipping

				List<Session3VO> listSessions = sessionService.findByShippingId(shippingId);
				form.setListSessions(CreateShippingAction.decoreSessionList(listSessions));

				// Fill the dewar bean
				Dewar3VO infoDewar = new Dewar3VO();

				infoDewar.setShippingVO(selecteShipping);

				infoDewar.setType(Constants.PARCEL_DEWAR_TYPE);
				infoDewar.setDewarStatus(Constants.SHIPPING_STATUS_OPENED);

				// old LabContactFacadeLocal _labContactFacade = LabContactFacadeUtil.getLocalHome().create();
				// old LabContactLightValue sendingLabContact =
				// _labContactFacade.findByPrimaryKey(selecteShipping.getSendingLabContactId());

				LabContact3VO sendingLabContact = this.labContact3Service.findByPk(selecteShipping
						.getSendingLabContactId());
				if (sendingLabContact != null){
					infoDewar.setCustomsValue(sendingLabContact.getDewarAvgCustomsValue());
					infoDewar.setTransportValue(sendingLabContact.getDewarAvgTransportValue());
				}

				form.setInfo(infoDewar);

				FormUtils.setFormDisplayMode(request, actForm, FormUtils.CREATE_MODE);
			} else {
				LOG.error("ShippingId shouldn't be null");
			}

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			e.printStackTrace();
			return mapping.findForward("error");
		}
		// return mapping.findForward("dewarCreatePage");
		return redirectPageFromRole(mapping, request);
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
			ViewDewarForm form = (ViewDewarForm) actForm; // Parameters submited by form
			String dewarId = request.getParameter(Constants.DEWAR_ID);
			BreadCrumbsForm.getItClean(request);
			// this.RetrieveSessionAttributes(request);
			Integer mProposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			// ---------------------------------------------------------------------------------------------------

			// Retrieve Dewar information
			Dewar3VO selectedDewar = DBTools.getSelectedDewar(new Integer(dewarId));
			Dewar3VO info = selectedDewar;

			form.setInfo(info);
			BreadCrumbsForm.getItClean(request).setSelectedDewar(selectedDewar);
			Shipping3VO selecteShipping = BreadCrumbsForm.getIt(request).getSelectedShipping();
			BreadCrumbsForm.getIt(request).setSelectedDewar(info);

			// Retrieve list of session associated to this shipping
			List<Session3VO> listSessions = sessionService.findByShippingId(selecteShipping.getShippingId());

			form.setListSessions(CreateShippingAction.decoreSessionList(listSessions));
			FormUtils.setFormDisplayMode(request, actForm, FormUtils.EDIT_MODE);

			// retrieves role from session
			RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
			String role = roleObject.getName();
			form.setRole(role);

			// Issue 1076: dewar status
			String[] listDewarStatus = new String[6];
			int i = 0;
			listDewarStatus[i++] = Constants.SHIPPING_STATUS_AT_ESRF;
			listDewarStatus[i++] = Constants.SHIPPING_STATUS_CLOSED;
			listDewarStatus[i++] = Constants.SHIPPING_STATUS_OPENED;
			listDewarStatus[i++] = Constants.SHIPPING_STATUS_READY_TO_GO;
			listDewarStatus[i++] = Constants.SHIPPING_STATUS_SENT_TO_ESRF;
			listDewarStatus[i++] = Constants.SHIPPING_STATUS_SENT_TO_USER;

			form.setListDewarStatus(listDewarStatus);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			e.printStackTrace();
			return mapping.findForward("error");
		}
		// return mapping.findForward("dewarCreatePage");
		return redirectPageFromRole(mapping, request);
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
			// Retrieve Attributes
			ViewDewarForm form = (ViewDewarForm) actForm; // Parameters submited by form
			// ---------------------------------------------------------------------------------------------------

			Integer mProposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

			// Retrieve Dewar information
			Dewar3VO updatedDewar = form.getInfo();
			if (updatedDewar.getBarCode() == null || updatedDewar.getBarCode().equals(""))
				updatedDewar.setBarCode(null);
			Dewar3VO dbDewar = dewarService.findByPk(updatedDewar.getDewarId(), false, false);

			ArrayList<String> fieldsChanged = new ArrayList<String>();
			boolean dewarHasChanged = false;

			// Checks modifications
			// label/code
			if (!updatedDewar.getCode().equals(dbDewar.getCode())) {
				dewarHasChanged = true;
				dbDewar.setCode(updatedDewar.getCode());
				fieldsChanged.add("Label changed to: '" + updatedDewar.getCode() + "'");
			}

			// firstExperiment
			// old if (updatedDewar.getFirstExperimentId() != null) {
			if (updatedDewar.getSessionVO() != null) {
				// old if (!updatedDewar.getFirstExperimentId().equals(dbDewar.getFirstExperimentId())) {
				if (!updatedDewar.getSessionVO().getSessionId().equals(dbDewar.getSessionVO().getSessionId())) {
					dewarHasChanged = true;
					// old dbDewar.setFirstExperimentId(updatedDewar.getFirstExperimentId());
					dbDewar.setSessionVO(updatedDewar.getSessionVO());
					// old Session3VO session = sessionService.findByPk(updatedDewar.getFirstExperimentId(), false,
					// false, false);
					Session3VO session = updatedDewar.getSessionVO();

					SimpleDateFormat dateStandard = new SimpleDateFormat(Constants.DATE_FORMAT);
					String dateFormated = dateStandard.format(session.getStartDate().getTime());

					// old dbDewarFull.setFirstExperimentDate(session.getStartDate());
					// old dbDewarFull.setBeamline(session.getBeamlineName());
					dbDewar.getSessionVO().setStartDate(session.getStartDate());
					dbDewar.getSessionVO().setBeamlineName(session.getBeamlineName());

					fieldsChanged.add("First experiment performed changed to: '" + dateFormated + " "
							+ session.getBeamlineName() + "'");
				}
			}
			// comments
			if (dbDewar.getComments() != null || !updatedDewar.getComments().equals("")) {
				if (!updatedDewar.getComments().equals(dbDewar.getComments())) {
					dewarHasChanged = true;
					dbDewar.setComments(updatedDewar.getComments());
					fieldsChanged.add("Comments changed to: '" + updatedDewar.getComments() + "'");
				}
			}
			// transport
			if (!updatedDewar.getTransportValue().equals(dbDewar.getTransportValue())) {
				dewarHasChanged = true;
				dbDewar.setTransportValue(updatedDewar.getTransportValue());
			}
			// customs
			if (!updatedDewar.getCustomsValue().equals(dbDewar.getCustomsValue())) {
				dewarHasChanged = true;
				dbDewar.setCustomsValue(updatedDewar.getCustomsValue());
			}
			// type
			if (!updatedDewar.getType().equals(dbDewar.getType())) {
				dewarHasChanged = true;
				dbDewar.setType(updatedDewar.getType());
			}
			// status
			if (!updatedDewar.getDewarStatus().equals(dbDewar.getDewarStatus())) {
				dewarHasChanged = true;
				dbDewar.setDewarStatus(updatedDewar.getDewarStatus());
			}
			if (dbDewar.getDewarStatus() != null && !dbDewar.getDewarStatus().equals(Constants.SHIPPING_STATUS_OPENED)
					&& !dbDewar.getDewarStatus().equals(Constants.SHIPPING_STATUS_READY_TO_GO)) {
				// tracking number TO Esrf
				if (updatedDewar.getTrackingNumberToSynchrotron() != null
						&& !updatedDewar.getTrackingNumberToSynchrotron().equals(
								dbDewar.getTrackingNumberToSynchrotron())) {
					// if (
					// !dbDewar.getTrackingNumberToSynchrotron().equals(updatedDewar.getTrackingNumberToSynchrotron()) )
					// {
					dewarHasChanged = true;
					dbDewar.setTrackingNumberToSynchrotron(updatedDewar.getTrackingNumberToSynchrotron());
					fieldsChanged.add("Courier tracking number TO " + Constants.SITE_NAME + " changed to: '"
							+ updatedDewar.getTrackingNumberToSynchrotron() + "'");
				}
			}

			// Check if information has changed /!\
			if (dewarHasChanged) {
				// Update information ---------------------
				updatedDewar.setTimeStamp(new Timestamp(new Date().getTime()));
				dewarService.update(updatedDewar);
				// BreadCrumbsForm.getIt(request).setSelectedDewar(null);

				// send an email report if shipping is already send to the ESRF
				if (dbDewar.getDewarStatus() != null
						&& (dbDewar.getDewarStatus().equals(Constants.SHIPPING_STATUS_SENT_TO_ESRF)
								|| dbDewar.getDewarStatus().equals(Constants.SHIPPING_STATUS_AT_ESRF) || dbDewar
								.getDewarStatus().equals(Constants.SHIPPING_STATUS_SENT_TO_USER))) {
					// send email ----------------------
					// old ProposalLightValue proposal = null;
					Proposal3VO proposal = null;
					// old PersonValue mainProposer = null;
					Person3VO mainProposer = null;
					if (mProposalId != null) {
						// old ProposalFacadeLocal _proposalFacade = ProposalFacadeUtil.getLocalHome().create();
						// old proposal = _proposalFacade.findByPrimaryKeyLight(mProposalId);
						proposal = this.proposalService.findByPk(mProposalId);

						// old PersonFacadeLocal _personFacade = PersonFacadeUtil.getLocalHome().create();
						// old mainProposer = _personFacade.findByPrimaryKey(proposal.getPersonId());
						mainProposer = this.person3Service.findByPk(proposal.getPersonVOId());
					}
					Session3VO dewarFirstExp = null;
					// old if (dbDewar.getFirstExperimentId() != null) {
					if (dbDewar.getSessionVO() != null) {
						// old dewarFirstExp = sessionService.findByPk(dbDewar.getFirstExperimentId(), false, false,
						// false);
						dewarFirstExp = sessionService.findByPk(dbDewar.getSessionVO().getSessionId(), false, false,
								false);
					}
					Shipping3VO selectedShipping = BreadCrumbsForm.getIt(request).getSelectedShipping();
					reportDewarModificationByMail(request, proposal, mainProposer, selectedShipping, dbDewar,
							dewarFirstExp, fieldsChanged);
					// confirmation message
					messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free",
							"Notification mail sent to the stores"));

				}
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.update"));

			}
			saveMessages(request, messages);
			// WF: go to List of dewar instead of staying on the update page
			// BreadCrumbsForm.getIt(request).setSelectedDewar(null);
			// return mapping.findForward("dewarViewPage");
			return redirectPageFromRole(mapping, request);
		} catch (Exception e) {
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
		ActionMessages errors = new ActionMessages();

		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			ViewDewarForm form = (ViewDewarForm) actForm; // Parameters submited by form

			if (BreadCrumbsForm.getIt(request).getSelectedShipping() == null)
				throw new Exception("Trying to create a Dewar no shippingId ");

			// ---------------------------------------------------------------------------------------------------
			// Check Required fields populated
			if (!this.CheckRequiredFieldsPopulated(form, request))
				// return mapping.findForward("dewarCreatePage");
				return redirectPageFromRole(mapping, request);

			// checks fields
			if (!this.checkDewarInformations(form, request)) {
				// return mapping.findForward("dewarCreatePage");
				return redirectPageFromRole(mapping, request);
			}

			// Redirect to the update
			if (BreadCrumbsForm.getIt(request).getSelectedDewar() != null)
				return this.update(mapping, actForm, request, in_response);

			// Populate with info from form
			Dewar3VO info = form.getInfo();
			System.out.println("First Experiment ID: " + form.getFirstExperimentId());

			String firstExperimentIdS = form.getFirstExperimentId();
			if (firstExperimentIdS != null && !firstExperimentIdS.trim().equals("")) {
				try {
					Integer firstExperimentId = Integer.parseInt(firstExperimentIdS);
					info.setSessionVO(this.sessionService.findByPk(firstExperimentId, false, false, false));
				} catch (Exception e) {

				}
			}

			info.setShippingVO(BreadCrumbsForm.getIt(request).getSelectedShipping());
			info.setTimeStamp(new Timestamp(new Date().getTime()));
			if (info.getBarCode().equals(""))
				info.setBarCode(null);

			// Check that the dewar code for the current shipment is unique
			if (CheckFormFields(info.getCode(), errors, request)) {
				// Create the dewar
				info = dewarService.create(info);

				form.setInfo(new Dewar3VO());
				BreadCrumbsForm.getIt(request).setSelectedDewar(info);
			} else {
				saveErrors(request, errors);
				// return mapping.findForward("dewarCreatePage");
				return redirectPageFromRole(mapping, request);
			}
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			e.printStackTrace();
			return mapping.findForward("error");
		}

		return mapping.findForward("dewarViewPageDisplaySlave");
	}

	/**
	 * Delete the Dewar
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_response) {

		ActionMessages errors = new ActionMessages();
		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			Integer dewarId = new Integer(request.getParameter(Constants.DEWAR_ID));

			Dewar3VO dewar = DBTools.getSelectedDewar(dewarId);

			// Delete
			dewarService.deleteByPk(dewar.getDewarId());

			// Clean up things a bit
			if (BreadCrumbsForm.getIt(request).getSelectedDewar() != null
					&& BreadCrumbsForm.getIt(request).getSelectedDewar().getDewarId().intValue() == dewarId.intValue()) {
				BreadCrumbsForm.getIt(request).setSelectedDewar(null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return mapping.findForward("dewarViewPage");
	}

	// /**
	// * RetrieveSessionAttributes
	// *
	// * @param request
	// * The Request to retrieve the Session Attributes from Populates the class Attributes with Attributes
	// * stored in the Session
	// */
	// public void RetrieveSessionAttributes(HttpServletRequest request)
	// {
	// this.mProposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
	// }

	/**
	 * CheckRequiredFieldsPopulated
	 * 
	 * @param form
	 *            The form to get Fields from. Checks that Required Fields are there. Redirect to
	 */

	public boolean CheckRequiredFieldsPopulated(ViewDewarForm form, HttpServletRequest request) {
		boolean requiredFieldsPresent = true;
		try {
			ActionMessages l_ActionMessages = new ActionMessages();
			// parcel name
			if (form.getInfo().getCode().length() == 0) {
				requiredFieldsPresent = false;

				ActionMessage l_ActionMessageLabel = new ActionMessage("errors.required", "Parcel name Label"); // Shipping
																												// Label
				l_ActionMessages.add("info.code", l_ActionMessageLabel);
			}
			// first experiment (only if proposal has sessions)
			List<Session3VO> listSessions = ProposalUtils.getSessionsAvailable(request);
			if (listSessions != null && listSessions.size() != 0) {
				// old if (form.getInfo().getFirstExperimentId() == null) {
				// not too old if (form.getInfo().getSessionVO() == null) {
				if (form.getFirstExperimentId().isEmpty()) {
					requiredFieldsPresent = false;
					ActionMessage l_ActionMessageLabel = new ActionMessage("errors.required", "First experiment Label"); // Shipping
					l_ActionMessages.add("info.firstExperimentId", l_ActionMessageLabel);
				}
			}

			if (!requiredFieldsPresent) {
				request.setAttribute("org.apache.struts.action.ERROR", l_ActionMessages);
			}
		} catch (Exception e) {
			requiredFieldsPresent = false;
			e.printStackTrace();
		}

		return requiredFieldsPresent;
	}

	/**
	 * 
	 * @param dewarCode
	 * @param l_ActionMessages
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private boolean CheckFormFields(String dewarCode, ActionMessages l_ActionMessages, HttpServletRequest request)
			throws Exception {
		boolean formFieldsOK = true;
		Integer shippingId = BreadCrumbsForm.getIt(request).getSelectedShipping().getShippingId();
		// DewarFacadeLocal dewarFL = DewarFacadeUtil.getLocalHome().create();
		// old Collection dewars = dewarFL.findByCodeAndShippingId(dewarCode.trim(), shippingId);
		List<Dewar3VO> dewars = this.dewarService.findFiltered(null, shippingId, null, dewarCode.trim(), null, null,
				null, null, null, null, false, false);
		if (dewars.size() > 0) {
			ActionMessage l_ActionMessageName = new ActionMessage("errors.alreadyExist",
					"Label or Bar Code for this shipment");
			l_ActionMessages.add("info.code", l_ActionMessageName);
			formFieldsOK = false;
		}
		return formFieldsOK;
	}

	/**
	 * Reports dewar modification to the stores, when the status of the dewar is : 'send to ESRF', 'at ESRF' or 'sent to
	 * user'
	 * 
	 * @param proposalCode
	 * @param proposalNumber
	 * @param shippingName
	 * @param experimentDate
	 * @param beamlineName
	 * @param fieldsChanged
	 */
	private void reportDewarModificationByMail(HttpServletRequest request, Proposal3VO proposal,
			Person3VO mainProposer, Shipping3VO shipping, Dewar3VO dewar, Session3VO dewarFirstExp,
			List<String> fieldsChanged) {
		try {

			SimpleDateFormat dateStandard = new SimpleDateFormat(Constants.DATE_FORMAT);
			String experimentDateFormated = "";
			if (dewarFirstExp != null) {
				experimentDateFormated = dateStandard.format(dewarFirstExp.getStartDate());
			}

			String ispybMailAddress = Constants.getProperty("mail.from");

			String toAddress = "";
			if (dewar.getDewarStatus().equals(Constants.SHIPPING_STATUS_SENT_TO_ESRF)) {
				// STORE
				// Send to storeAddress (or to storeAddress.test if not in production environment)
				toAddress = Constants.getProperty("mail.storeAddress.test");
				String serverName = request.getServerName().toLowerCase();
				if (Constants.isServerProd(serverName)) {
					toAddress = Constants.getProperty("mail.storeAddress");
				}
			} else if (dewar.getDewarStatus().equals(Constants.SHIPPING_STATUS_AT_ESRF)) {
				// LABCONTACT FOR SENDING
				// old LabContactValue labContact =
				// _labContactFacade.findByPrimaryKey(shipping.getSendingLabContactId());
				LabContact3VO labContact = labContact3Service.findByPk(shipping.getSendingLabContactId());
				toAddress = this.person3Service.findByPk(labContact.getPersonVOId()).getEmailAddress();
			} else if (dewar.getDewarStatus().equals(Constants.SHIPPING_STATUS_SENT_TO_USER)) {
				// LABCONTACT FOR RETURN
				// old LabContactValue labContact =
				// _labContactFacade.findByPrimaryKey(shipping.getReturnLabContactId());
				LabContact3VO labContact = this.labContact3Service.findByPk(shipping.getReturnLabContactId());
				toAddress = this.person3Service.findByPk(labContact.getPersonVOId()).getEmailAddress();
			}

			String mainProposerFamilyName = mainProposer == null ? "" : mainProposer.getFamilyName();
			mainProposerFamilyName = (mainProposerFamilyName != null) ? mainProposerFamilyName : "";
			String mainProposerFirstName = mainProposer == null ? "" : mainProposer.getGivenName();
			mainProposerFirstName = (mainProposerFirstName != null) ? mainProposerFirstName : "";

			String subject = proposal == null ? "" : (proposal.getCode() + proposal.getNumber());
			subject += " - " + shipping.getShippingName() + " - " + dewar.getCode();
			String body = "Shipment name: " + shipping.getShippingName() + "\n" + "Dewar name: " + dewar.getCode()
					+ "\n" + "Main proposer name: " + mainProposerFamilyName + " " + mainProposerFirstName + "\n"
					+ "Proposal number: " + proposal.getCode() + proposal.getNumber() + "\n" + "Exp. date: "
					+ experimentDateFormated + " & Beamline:" + dewarFirstExp.getBeamlineName() + "\n"
					+ "---------------------------------------------------\n" + "Information changed by the user: \n";
			for (Iterator<String> iterator = fieldsChanged.iterator(); iterator.hasNext();) {
				String field = (String) iterator.next();
				body += "  * " + field + "\n";
			}

			SendMailUtils.sendMail(ispybMailAddress, toAddress, "", "", subject, body);
		} catch (Exception e) {
			LOG.error("Error during the sending of mail report: " + e.getMessage());
		}
	}

	/**
	 * checks if the dewar informations in the form are correct and well formated, returns false if at least one field
	 * is incorrect
	 * 
	 * @param form
	 * @return
	 */
	private boolean checkDewarInformations(ViewDewarForm form, HttpServletRequest request) {
		ActionMessages l_ActionMessages = new ActionMessages();
		boolean isOk = true;
		// component label
		if (form.getInfo().getCode() != null && form.getInfo().getCode().length() > DBConstants.MAX_LENGTH_DEWAR_CODE) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Component label",
					DBConstants.MAX_LENGTH_DEWAR_CODE);
			l_ActionMessages.add("info.code", l_ActionMessageLabel);
		}
		// courier tracking number to synchrotron
		if (form.getInfo().getTrackingNumberToSynchrotron() != null
				&& form.getInfo().getTrackingNumberToSynchrotron().length() > DBConstants.MAX_LENGTH_DEWAR_TRACKINGNUMBERTOSYNCHROTRON) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Courier tracking number",
					DBConstants.MAX_LENGTH_DEWAR_TRACKINGNUMBERTOSYNCHROTRON);
			l_ActionMessages.add("info.trackingNumberToSynchrotron", l_ActionMessageLabel);
		}
		if (!isOk) {
			request.setAttribute("org.apache.struts.action.ERROR", l_ActionMessages);
		}
		return isOk;
	}

	private ActionForward redirectPageFromRole(ActionMapping mapping, HttpServletRequest request) {
		// redirection page according to the Role ------------------------

		ActionMessages errors = new ActionMessages();
		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();

		if (role.equals(Constants.ROLE_STORE)) {
			return mapping.findForward("storeDewarCreatePage");
		} else if (role.equals(Constants.ROLE_BLOM)) {
			return mapping.findForward("blomDewarCreatePage");
		} else if (role.equals(Constants.FXMANAGE_ROLE_NAME)) {
			return mapping.findForward("fedexmanagerDewarCreatePage");
		} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
			return mapping.findForward("localcontactDewarCreatePage");
		} else if (role.equals(Constants.ROLE_USER) || role.equals(Constants.ROLE_INDUSTRIAL)) {
			return mapping.findForward("userDewarCreatePage");
		} else if (role.equals(Constants.ROLE_MANAGER)) {
			return mapping.findForward("managerDewarCreatePage");
		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Role (" + role
					+ ") not found"));
			LOG.error("Role (" + role + ") not found");
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
	}
}
