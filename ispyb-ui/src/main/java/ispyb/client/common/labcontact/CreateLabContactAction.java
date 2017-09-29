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
package ispyb.client.common.labcontact;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import fr.improve.struts.taglib.layout.util.FormUtils;
import generated.ws.smis.ProposalParticipantInfoLightVO;
import ispyb.client.common.util.Confidentiality;
import ispyb.client.common.util.DBConstants;
import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.common.services.proposals.LabContact3Service;
import ispyb.server.common.services.proposals.Laboratory3Service;
import ispyb.server.common.services.proposals.Person3Service;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Laboratory3VO;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.smis.ScientistsFromSMIS;

/**
 * @struts.action name="viewLabContactForm" path="/user/createLabContactAction"
 *                type="ispyb.client.common.labcontact.CreateLabContactAction" input="user.labcontact.create.page" validate="false"
 *                parameter="reqCode" scope="request"
 * 
 * @struts.action name="viewLabContactForm" path="/user/loginLabContactAction"
 *                type="ispyb.client.common.labcontact.CreateLabContactAction" input="user.labcontact.login.page" validate="false"
 *                parameter="reqCode" scope="request"
 * 
 * @struts.action name="viewLabContactForm" path="/reader/createLabContactAction"
 *                type="ispyb.client.common.labcontact.CreateLabContactAction" validate="false" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="labContactLoginPage" path="user.labcontact.login.page"
 * 
 * @struts.action-forward name="labContactCreatePage" path="user.labcontact.create.page"
 * 
 * @struts.action-forward name="labContactStoreCreatePage" path="store.labcontact.create.page"
 * 
 * @struts.action-forward name="labContactLocalcontactCreatePage" path="localcontact.labcontact.create.page"
 * 
 * @struts.action-forward name="labContactBlomCreatePage" path="blom.labcontact.create.page"
 * 
 * @struts.action-forward name="labContactManagerCreatePage" path="manager.labcontact.create.page"
 * 
 * @struts.action-forward name="labContactFedexmanagerCreatePage" path="fedexmanager.labcontact.create.page"
 * 
 * @struts.action-forward name="shippingCreatePage" path="/user/createShippingAction.do?reqCode=backFromLabContact"
 * 
 * @struts.action-forward name="labContactViewPage" path="/user/viewLabContactAction.do?reqCode=display"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 */

public class CreateLabContactAction extends org.apache.struts.actions.DispatchAction {

	private final static Logger LOG = Logger.getLogger(CreateLabContactAction.class);

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Proposal3Service proposalService;

	private LabContact3Service labCService;

	private Person3Service personService;

	private Laboratory3Service laboratoryService;

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() {
		try {
			this.labCService = (LabContact3Service) ejb3ServiceLocator.getLocalService(LabContact3Service.class);
			this.personService = (Person3Service) ejb3ServiceLocator.getLocalService(Person3Service.class);
			this.proposalService = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
			this.laboratoryService = (Laboratory3Service) ejb3ServiceLocator.getLocalService(Laboratory3Service.class);

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
	 * display create card form
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

		try {
			ViewLabContactForm form = (ViewLabContactForm) actForm; // Parameters submitted by form

			Person3VO person = new Person3VO();
			Laboratory3VO laboratory = new Laboratory3VO();

			String forward = createLabContactCard(form, person, laboratory, proposalId);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.CREATE_MODE);
			return mapping.findForward(forward);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
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
			LOG.info("Update Display");
			ViewLabContactForm form = (ViewLabContactForm) actForm; // Parameters submitted by form

			String labContactId = request.getParameter(Constants.LABCONTACT_ID);

			// Confidentiality (check if object proposalId matches)
			if (!Confidentiality.isAccessAllowedToLabContact(request, Integer.valueOf(labContactId))) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Access denied"));
				saveErrors(request, errors);
				return (mapping.findForward("error"));
			}

			// Retrieve all labcontact infos from the DataBase (LabContact -> Person -> Laboratory)
			LabContact3VO labContact = labCService.findByPk(new Integer(labContactId));
			LOG.debug("LabContact found in DB :" + labContact);

			// load person infos
			Person3VO person = personService.findByPk(labContact.getPersonVOId());
			LOG.debug("Person found in DB :" + person);
			labContact.setPersonVO(person);

			// load labo infos
			Laboratory3VO laboratory = laboratoryService.findByPk(person.getLaboratoryVOId());
			LOG.debug("Laboratory found in DB :" + laboratory);
			person.setLaboratoryVO(laboratory);

			// Populate with Info
			form.setLabContact(labContact);
			form.setPerson(person);
			form.setLaboratory(laboratory);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.EDIT_MODE);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return mapping.findForward("labContactCreatePage");
	}

	/**
	 * readOnly display
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward readOnly(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();

		try {
			ViewLabContactForm form = (ViewLabContactForm) actForm;
			String labContactId = request.getParameter(Constants.LABCONTACT_ID);

			// Confidentiality (check if object proposalId matches)
			if (!Confidentiality.isAccessAllowedToLabContact(request, Integer.valueOf(labContactId))) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Access denied"));
				saveErrors(request, errors);
				return (mapping.findForward("error"));
			}

			// Retrieve all labcontact infos from the DataBase (LabContact -> Person -> Laboratory)
			LabContact3VO labContact = labCService.findByPk(new Integer(labContactId));
			LOG.debug("LabContact found in DB :" + labContact);

			// load person infos
			Person3VO person = personService.findByPk(labContact.getPersonVOId());
			LOG.debug("Person found in DB :" + person);
			labContact.setPersonVO(person);

			// load labo infos
			Laboratory3VO laboratory = laboratoryService.findByPk(person.getLaboratoryVOId());
			LOG.debug("Laboratory found in DB :" + laboratory);
			person.setLaboratoryVO(laboratory);

			// Populate with Info
			form.setLabContact(labContact);
			form.setPerson(person);
			form.setLaboratory(laboratory);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

			RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
			String role = roleObject.getName();
			if (role.equals(Constants.ROLE_STORE)) {
				return mapping.findForward("labContactStoreCreatePage");
			} else if (role.equals(Constants.ROLE_BLOM)) {
				return mapping.findForward("labContactBlomCreatePage");
			} else if (role.equals(Constants.FXMANAGE_ROLE_NAME)) {
				return mapping.findForward("labContactFedexmanagerCreatePage");
			} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
				return mapping.findForward("labContactLocalcontactCreatePage");
			} else if (role.equals(Constants.ROLE_MANAGER)) {
				return mapping.findForward("labContactManagerCreatePage");
			}

			return mapping.findForward("labContactCreatePage");
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}

	}

	/**
	 * update the labcontact infos
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward update(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse in_reponse) {

		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();

		ViewLabContactForm form = null;

		try {
			form = (ViewLabContactForm) actForm; // Parameters submitted by form

			// Check Required fields populated
			if (!this.CheckRequiredFieldsPopulated(form, request))
				return mapping.findForward("labContactCreatePage");

			LOG.debug("Processing update of the LabContact ...");

			// Get modif from the form, and update the LabContact hierarchy :
			// LabContact -> Person -> Laboratory
			LabContact3VO labContactForm = form.getLabContact();
			Person3VO personForm = form.getPerson();
			Laboratory3VO laboratoryForm = form.getLaboratory();

			// --> Laboratory ----------------------------------------------
			Laboratory3VO existingLaboratory = laboratoryService.findByPk(laboratoryForm.getLaboratoryId());
			if (!laboratoryForm.getName().equals(existingLaboratory.getName())
					|| !laboratoryForm.getAddress().equals(existingLaboratory.getAddress())) {
				// the user has changed the laboratory infos

				// -> CREATION of a new Lab
				existingLaboratory = new Laboratory3VO();
				existingLaboratory.setName(laboratoryForm.getName());
				existingLaboratory.setAddress(laboratoryForm.getAddress());
				// Insert into DB
				existingLaboratory = laboratoryService.create(existingLaboratory);
				LOG.debug("New Laboratory created : " + existingLaboratory);
			}

			// --> Person ----------------------------------------------
			Person3VO existingPerson = personService.findByPk(personForm.getPersonId());
			if (!existingLaboratory.getLaboratoryId().equals(existingPerson.getLaboratoryVOId())
					|| // link to the new Laboratory
					!personForm.getFamilyName().equals(existingPerson.getFamilyName())
					|| !personForm.getGivenName().equals(existingPerson.getGivenName())
					|| !personForm.getPhoneNumber().equals(existingPerson.getPhoneNumber())
					|| !personForm.getFaxNumber().equals(existingPerson.getFaxNumber())
					|| !personForm.getEmailAddress().equals(existingPerson.getEmailAddress())) {// the user has changed
																								// the person infos ->
																								// update of the person
																								// re-set labId in case
																								// of lab creation
				existingPerson.setLaboratoryVO(existingLaboratory);
				existingPerson.setPhoneNumber(personForm.getPhoneNumber());
				existingPerson.setFaxNumber(personForm.getFaxNumber());
				existingPerson.setEmailAddress(personForm.getEmailAddress());

				personService.merge(existingPerson);
				LOG.debug("Existing person updated : " + existingPerson);
			}

			// --> LabContact ----------------------------------------------
			LabContact3VO existingLabContact = labCService.findByPk(labContactForm.getLabContactId());
			if (!labContactForm.getCardName().equals(existingLabContact.getCardName())
					|| !labContactForm.getDefaultCourrierCompany().equals(existingLabContact.getDefaultCourrierCompany())
					|| !labContactForm.getCourierAccount().equals(existingLabContact.getCourierAccount())
					|| !labContactForm.getBillingReference().equals(existingLabContact.getBillingReference())
					|| !labContactForm.getDewarAvgCustomsValue().equals(existingLabContact.getDewarAvgCustomsValue())
					|| !labContactForm.getDewarAvgTransportValue().equals(existingLabContact.getDewarAvgTransportValue())) {// the user
																															// has
																															// changed
																															// the
																															// labContact
																															// infos ->
																															// update
																															// of the
																															// labContact
				existingLabContact.setCardName(labContactForm.getCardName());
				existingLabContact.setDefaultCourrierCompany(labContactForm.getDefaultCourrierCompany());
				existingLabContact.setCourierAccount(labContactForm.getCourierAccount());
				existingLabContact.setBillingReference(labContactForm.getBillingReference());
				existingLabContact.setDewarAvgCustomsValue(labContactForm.getDewarAvgCustomsValue());
				existingLabContact.setDewarAvgTransportValue(labContactForm.getDewarAvgTransportValue());

				labCService.update(existingLabContact);
				LOG.debug("LabContact updated : " + existingLabContact);
			}

			// ---------------------------------------------------------------------------------------------------
			// Acknowledge action
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.update"));
			saveMessages(request, messages);

			// fill the form with updated infos
			form.setLabContact(existingLabContact);
			form.setPerson(existingPerson);
			form.setLaboratory(existingLaboratory);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.EDIT_MODE);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}

		// Go back to list or to shipment
		if (form.getShippingId() == null || form.getShippingId().equals("")) {
			// return mapping.findForward("labContactCreatePage");
			return mapping.findForward("labContactViewPage");
		} else {
			return mapping.findForward("shippingCreatePage");
		}

	}

	/**
	 * create a new labcontact
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward create(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();

		try {
			// Retrieve Attributes
			ViewLabContactForm form = (ViewLabContactForm) actForm; // Parameters submited by form

			// The LabContact does not exist yet
			LabContact3VO labContact = form.getLabContact();
			Person3VO person = form.getPerson();
			Laboratory3VO laboratory = form.getLaboratory();

			// 1)--> Laboratory ----------------------------------------------
			if (laboratory.getLaboratoryId() == null || laboratory.getLaboratoryId() == 0) {
				// The laboratory must be CREATED
				// Insert into DB
				if (laboratory.getLaboratoryId() != null) {
					laboratory.setLaboratoryId(null);
				}
				laboratory = laboratoryService.create(laboratory);
				LOG.debug("New Laboratory created =" + laboratory);
			} else {
				// the laboratory is already existing
				Laboratory3VO existingLaboratory = laboratoryService.findByPk(laboratory.getLaboratoryId());
				if (!laboratory.getName().equals(existingLaboratory.getName())
						|| !laboratory.getAddress().equals(existingLaboratory.getAddress())) {
					// the user has changed the existing laboratory infos

					// -> CREATION of a new Laboratory
					existingLaboratory = new Laboratory3VO();
					existingLaboratory.setName(laboratory.getName());
					existingLaboratory.setAddress(laboratory.getAddress());
					existingLaboratory.setLaboratoryId(null);
					// Insert into DB

					laboratory = laboratoryService.create(existingLaboratory);
					LOG.debug("New Laboratory created : " + existingLaboratory);
				}
			}

			// 2)--> Person ----------------------------------------------
			if (person.getPersonId() == null || person.getPersonId() == 0) {
				// The Person must be CREATED
				person.setLaboratoryVO(laboratory);// link to the laboratory !

				if (person.getPersonId() != null) {
					person.setPersonId(null);
				}

				// Insert into DB
				person = personService.merge(person);
				LOG.debug("New Person created =" + person);
			} else {
				// the person is already existing
				Person3VO existingPerson = personService.findByPk(person.getPersonId());
				if (!laboratory.getLaboratoryId().equals(existingPerson.getLaboratoryVOId())
						|| // link to the new Laboratory
						!person.getFamilyName().equals(existingPerson.getFamilyName())
						|| !person.getGivenName().equals(existingPerson.getGivenName())
						|| !person.getPhoneNumber().equals(existingPerson.getPhoneNumber())
						|| !person.getFaxNumber().equals(existingPerson.getFaxNumber())
						|| !person.getEmailAddress().equals(existingPerson.getEmailAddress())) {
					// the user has changed the person infos -> UPDATE of the person
					existingPerson.setLaboratoryVO(laboratory);// link to the laboratory !
					existingPerson.setPhoneNumber(person.getPhoneNumber());
					existingPerson.setFaxNumber(person.getFaxNumber());
					existingPerson.setEmailAddress(person.getEmailAddress());

					personService.merge(existingPerson);
					LOG.debug("Existing person updated : " + existingPerson);
				}
			}

			// 3)--> LabContact ----------------------------------------------
			// CREATION
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

			Proposal3VO proposal3VO = proposalService.findByPk(proposalId);
			labContact.setProposalVO(proposal3VO); // proposalId
			labContact.setPersonVO(person);

			if (labContact.getLabContactId() == 0) {
				labContact.setLabContactId(null);
			}
			labContact = labCService.create(labContact);
			LOG.debug("New LabContact created =" + labContact);

			// ---------------------------------------------------------------------------------------------------
			// Acknowledge action
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.inserted", person.getFamilyName()));
			saveMessages(request, messages);

			form.setLabContact(labContact);
			form.setPerson(person);
			form.setLaboratory(laboratory);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.EDIT_MODE);
			return mapping.findForward("labContactCreatePage");
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
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
	public ActionForward save(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse in_response) {
		ActionMessages errors = new ActionMessages();

		LOG.info("Save");
		try {
			// Retrieve Attributes
			ViewLabContactForm form = (ViewLabContactForm) actForm;

			// Check Required fields populated
			if (!this.CheckRequiredFieldsPopulated(form, request))
				return mapping.findForward("labContactCreatePage");
			if (!this.checkLabContactInformations(form, request)) {
				return mapping.findForward("labContactCreatePage");
			}

			LabContact3VO selectedLabContact = form.getLabContact();
			Integer labContactId = form.getLabContact().getLabContactId();
			if (labContactId != null && labContactId != 0) {
				LOG.debug("Search LabContact With ID=" + labContactId);
				selectedLabContact = labCService.findByPk(labContactId);
			}

			if (selectedLabContact.getLabContactId() != 0) { // Redirect to the update
				LOG.debug("LabContact exists, forward to the update function");
				return this.update(mapping, actForm, request, in_response);
			} else {
				LOG.debug("LabContact does not exist, forward to the create function");
				return this.create(mapping, actForm, request, in_response);
			}
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
	}

	/**
	 * display login form
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward loginDisplay(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();
		try {
			ViewLabContactForm form = (ViewLabContactForm) actForm;
			if (form.getShippingId() == null)
				form.setShippingId("");
			FormUtils.setFormDisplayMode(request, actForm, FormUtils.CREATE_MODE);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return mapping.findForward("labContactLoginPage");
	}

	/**
	 * display lab-contact form, from the selected person into the login page
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward displaySelectedPerson(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			ViewLabContactForm form = (ViewLabContactForm) actForm; // Parameters submited by form

			Integer personId = Integer.valueOf(request.getParameter("personId"));
			String familyName = request.getParameter("familyName");
			String givenName = request.getParameter("firstName");
			String phoneNumber = request.getParameter("phone");
			String faxNumber = request.getParameter("fax");
			String emailAddress = request.getParameter("email");

			String laboratoryName = request.getParameter("laboratoryName");
			String laboratoryAddress = request.getParameter("laboratoryAddress");

			// ---------------------------------------------------------------------------------------------------

			Person3VO selectedPerson = null;

			if (personId != 0) {// ->selection into ISPYB scientists list, the personId is known
				// person already saved in ISPYB database
				selectedPerson = personService.findByPk(personId);
				// fill the login form fields
				form.setPerson(selectedPerson);
				form.setLaboratory(laboratoryService.findByPk(selectedPerson.getLaboratoryVOId()));

				String forward = this.searchLabContactCard(form, request, errors, selectedPerson);
				return mapping.findForward(forward);
			} else {// ->selection into SMIS scientists list, the personId is NOT known
				selectedPerson = new Person3VO();
				selectedPerson.setFamilyName(familyName);
				selectedPerson.setGivenName(givenName);
				selectedPerson.setPhoneNumber(phoneNumber);
				selectedPerson.setFaxNumber(faxNumber);
				selectedPerson.setEmailAddress(emailAddress);

				Laboratory3VO laboratory = new Laboratory3VO();
				laboratory.setName(laboratoryName);
				laboratory.setAddress(laboratoryAddress);

				form.setPerson(selectedPerson);
				form.setLaboratory(laboratory);

				// checks if the person has been already saved in ISPYB
				String forward = this.searchScientistInISPYB(mapping, form, request, errors);
				if (forward != null) {
					// scientist found in ISPYB database
					return mapping.findForward(forward);
				} else {
					forward = this.createLabContactCard(form, selectedPerson, laboratory, proposalId);
					return mapping.findForward(forward);
				}
			}
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
	}

	/**
	 * display lab-contact form, from the selected person into the login page
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_response
	 * @return
	 */
	public ActionForward createDummyLabContact(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_response) {
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			ViewLabContactForm form = (ViewLabContactForm) actForm; // Parameters submited by form
			// We assume that there is only a dummy LabContact in the ISPyB database
			Person3VO dummyPerson = personService.findByFamilyAndGivenName(Constants.getDummyFamilyName(),
					Constants.getDummyGivenName()).get(0);
			Laboratory3VO dummyLaboratory = laboratoryService.findByPk(dummyPerson.getLaboratoryVOId());
			dummyPerson.setLaboratoryVO(dummyLaboratory);
			form.setPerson(dummyPerson);
			//
			// return this.create(mapping, form, request, in_response);
			String forward = searchScientistInISPYB(mapping, form, request, messages);
			if (forward != null) {
				// scientist found in ISPYB database
				form.getLabContact().setDefaultCourrierCompany("---");
				form.getLabContact().setLabContactId(new Integer(0));
				// Get the labContact list and get the first one
				LabContact3VO labContact = null;
				List<LabContact3VO> labContactlist = labCService.findByPersonIdAndProposalId(dummyPerson.getPersonId(), proposalId);
				if (labContactlist != null && !labContactlist.isEmpty()) {
					labContact = labContactlist.get(0);
				} else {
					labContact = new LabContact3VO();
					labContact.setCardName("MAX IV Lab Contact - Lund University");
					labContact.setPersonVO(dummyPerson);
					labContact.setProposalVO(proposalService.findByPk(proposalId));
					labContact.setDewarAvgCustomsValue(0);
					labContact.setDewarAvgTransportValue(0);
					labContact = labCService.create(labContact);
				}
				if (labContact != null) {
					form.getLabContact().setLabContactId(labContact.getLabContactId());
				}
			} else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail",
						"An error occurred. Please create a Lab Contact manually"));
				LOG.error("An error occurred. Please create a Lab Contact manually");
				saveErrors(request, errors);
				return mapping.findForward("error");
			}
			return mapping.findForward(forward);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
	}

	/**
	 * login form
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward login(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse in_response) {
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();

		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			ViewLabContactForm form = (ViewLabContactForm) actForm; // Parameters submited by form
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID); // session
			// ---------------------------------------------------------------------------------------------------

			// Check Required fields populated
			LOG.debug("Check required fields are populated for Login page");
			if (!this.CheckLoginRequiredFieldsPopulated(form, request))
				return mapping.findForward("labContactLoginPage");

			// ---------------------------------------------------------------------------------------------------
			// Search scientist in ISPYB
			String forward = searchScientistInISPYB(mapping, form, request, messages);
			if (forward != null) {
				// scientist found in ISPYB database
				return mapping.findForward(forward);
			} else {
				// scientist not found in Ispyb database

				// search in SMIS DB or SUNset DB
				// ESRF ####
				if (Constants.SITE_IS_ESRF() || Constants.SITE_IS_SOLEIL()) {
					forward = searchScientistInSMIS(mapping, form, request, errors, messages);
				} else {
					forward = createLabContactCard(form, null, null, proposalId);
					// forward = searchScientistInLDAP(mapping, form, request, errors, messages);
				}
				return mapping.findForward(forward);
			}
			// ----------------------------------------------------------------------------------------------------
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
	}

	/**
	 * Back to shipment page
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_response
	 * @return
	 */
	public ActionForward backToShipment(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_response) {
		ActionMessages errors = new ActionMessages();

		try {
			return mapping.findForward("shippingCreatePage");
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
	}

	// NOTE:
	// Note how you can list several values: sn, givenName and mail!
	// ldapsearch -x -LL -s sub -b ou=People,dc=diamond,dc=ac,dc=uk "(&(objectClass=person)(mail=dave.h*)(givenName=*))"
	// "sn" "givenName" "mail"
	//
	//

	/*
	 * private String searchScientistInLDAP( ActionMapping mapping, ViewLabContactForm form, HttpServletRequest request, ActionMessages
	 * errors, ActionMessages messages) throws FinderException, NamingException { String familyName = form.getPerson().getFamilyName();
	 * String givenName = form.getPerson().getGivenName(); Collection<PersonLightValue> matchingPersons = null; PersonLightValue person
	 * = null; LaboratoryLightValue laboratory = null;
	 * 
	 * if (familyName!=null && !familyName.equals("")){ matchingPersons = LdapConnection.findEmployees(familyName, "sn"); } else if
	 * (givenName!=null && !givenName.equals("")){ matchingPersons = LdapConnection.findEmployees(givenName, "givenName"); } else {
	 * matchingPersons = new ArrayList<PersonLightValue>(); }
	 * 
	 * if (matchingPersons.size() == 1) { //only one person matches -> OK ! person = (PersonLightValue)matchingPersons.toArray()[0];
	 * 
	 * // fill the form with the Person infos form.setPerson(person);
	 * 
	 * laboratory = laboratoryFacade.findByPrimaryKey(1);
	 * 
	 * String forward = createLabContactCard(form, person, laboratory);
	 * 
	 * //redirect to the lab-contact CREATE screen LOG.info("Person retrieved from LDAP."); messages.add(ActionMessages.GLOBAL_MESSAGE,
	 * new ActionMessage("message.free", "(Information retrieved from LDAP)")); saveMessages(request, messages);
	 * FormUtils.setFormDisplayMode(request, form, FormUtils.CREATE_MODE); return forward; //Search labcontact card //return
	 * searchLabContactCard(form, request, messages, person); } else if (matchingPersons.size() > 1) { //there is several matching
	 * persons
	 * 
	 * List<ScientistInfosBean> listOfScientistsInfo = new ArrayList<ScientistInfosBean>(matchingPersons.size()); laboratory =
	 * laboratoryFacade.findByPrimaryKey(1);
	 * 
	 * for (Iterator<PersonLightValue> iterator = matchingPersons.iterator(); iterator.hasNext();) { PersonLightValue currentPerson =
	 * (PersonLightValue) iterator.next(); listOfScientistsInfo.add(new ScientistInfosBean(currentPerson, laboratory)); }
	 * form.setListOfScientists(listOfScientistsInfo); //redirect to the display of the scientists list return "labContactLoginPage"; }
	 * else {//Scientist not found into the LDAP errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail",
	 * "No match found in LDAP.")); saveErrors(request, errors);
	 * 
	 * String forward = createLabContactCard(form, null, null);
	 * 
	 * //redirect to the display of the scientists list FormUtils.setFormDisplayMode(request, form, FormUtils.CREATE_MODE); return
	 * forward; //"labContactLoginPage"; } }
	 */
	private String searchScientistInISPYB(ActionMapping mapping, ViewLabContactForm form, HttpServletRequest request,
			ActionMessages messages) throws Exception {
		LOG.debug("SEARCH INFOS IN ISPYB DATABASE");
		List<Person3VO> matchingPersons = new ArrayList<Person3VO>();
		Person3VO person = null;
		Laboratory3VO laboratory = null;

		matchingPersons = personService.findByFamilyAndGivenName(form.getName(), form.getFirstName());

		LOG.info("There is " + matchingPersons.size() + " person(s) with this name/firstname found in Ispyb Database.");

		if (matchingPersons.size() == 1) {
			// only one person matches -> OK !
			person = matchingPersons.get(0);

			// fill the form with the Person infos
			form.setPerson(person);

			// Fill the form with the Labo infos
			laboratory = laboratoryService.findByPk(person.getLaboratoryVOId());
			form.setLaboratory(laboratory);

			// Search labcontact card
			return searchLabContactCard(form, request, messages, person);
		} else if (matchingPersons.size() > 1) {
			// there is several matching persons

			List<ScientistInfosBean> listOfScientistsInfo = new ArrayList<ScientistInfosBean>(matchingPersons.size());

			for (Iterator<Person3VO> iterator = matchingPersons.iterator(); iterator.hasNext();) {
				Person3VO currentPerson = iterator.next();
				Laboratory3VO currentLabo = laboratoryService.findByPk(currentPerson.getLaboratoryVOId());
				listOfScientistsInfo.add(new ScientistInfosBean(currentPerson, currentLabo));
			}

			form.setListOfScientists(listOfScientistsInfo);

			// redirect to the display of the scientists list
			return "labContactLoginPage";
		} else {// Scientist not found into the ISPYB database
			return null;
		}
	}

	private String searchLabContactCard(ViewLabContactForm form, HttpServletRequest request, ActionMessages messages, Person3VO person)
			throws Exception {
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

		List<LabContact3VO> labContacts = null;
		labContacts = labCService.findByPersonIdAndProposalId(person.getPersonId(), proposalId);

		// if (labContact != null) {
		if (labContacts.size() >= 1) {
			LabContact3VO labContact = labContacts.get(0);
			LOG.debug("Lab-contact card found in Ispyb Database: " + labContact);
			// fill the form with LabContact infos
			form.setLabContact(labContact);

			// redirect to the lab-contact EDIT screen
			LOG.info("Lab-contact card already exists");
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free",
					"(The following lab-contact card already exists)"));
			saveMessages(request, messages);
			FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
			return "labContactCreatePage";
		} else {
			// TODO: SMIS FACEDE
			// Look if ISPYB laboratory has an address
			String messageSMIS = "";
			String labAddress = "";
			Person3VO personISPYB = personService.findByPk(person.getPersonId());
			Laboratory3VO labISPYB = personISPYB.getLaboratoryVO();
			if (labISPYB != null) {
				labAddress = labISPYB.getAddress();
				if (labAddress == null || labAddress.equals("")) {
					// if not set in ISPYB, try to get it from SMIS
					LOG.info("ISPyB laboratory address is empty.........");
					try {

						Proposal3VO currentProposal = this.proposalService.findByPk(proposalId);
						Long proposalNumberInt = null;
						try {
							proposalNumberInt = Long.parseLong(currentProposal.getNumber());
						} catch (NumberFormatException e) {

						}
						ProposalParticipantInfoLightVO[] scientists = ScientistsFromSMIS.findScientistsForProposalByNameAndFirstName(
								currentProposal.getCode(), proposalNumberInt, form.getName(), form.getFirstName());

						// If 1 address found, it's OK
						if (scientists.length == 1) {
							LOG.info("Laboratory address retrieved from SMIS database.");
							Laboratory3VO laboratorySMIS = ScientistsFromSMIS.extractLaboratoryInfo(scientists[0]);
							labAddress = laboratorySMIS.getAddress();
							messageSMIS = " and SMIS";
						}
						// Otherwise give up
						else {
							LOG.info(scientists.length + " laboratory addresses retrieved from SMIS database for name/firstname = "
									+ form.getName() + "/" + form.getFirstName() + ". None taken.");
							labAddress = "";
						}
					} catch (Exception e) {
						LOG.debug("Error raises during the scientist address research");
						labAddress = "";
						e.printStackTrace();
					}
				}
			} else {
				LOG.info("No laboratory found");
				labAddress = "";
			}

			// new labContact card
			Laboratory3VO laboratory = laboratoryService.findByPk(person.getLaboratoryVOId());
			laboratory.setAddress(labAddress); // Use SMIS address if ISPYB address is empty

			String forward = createLabContactCard(form, person, laboratory, proposalId);

			// redirect to the lab-contact CREATE screen
			LOG.info("Person retrieved from ISPYB database.");
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free", "(Informations retrieved from ISPYB"
					+ messageSMIS + " database)"));
			saveMessages(request, messages);
			FormUtils.setFormDisplayMode(request, form, FormUtils.CREATE_MODE);
			return forward;
		}
	}

	private String searchScientistInSMIS(ActionMapping mapping, ViewLabContactForm form, HttpServletRequest request,
			ActionMessages errors, ActionMessages messages) throws CreateException, NamingException {
		Person3VO person;
		Laboratory3VO laboratory;
		LOG.debug("SEARCH INFOS IN SMIS DATABASE - TROUGHT WebService");
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

		try {
			Proposal3VO currentProposal = this.proposalService.findByPk(proposalId);
			Long proposalNumberInt = null;
			try {
				proposalNumberInt = Long.parseLong(currentProposal.getNumber());
			} catch (NumberFormatException e) {

			}
			ProposalParticipantInfoLightVO[] scientists = ScientistsFromSMIS.findScientistsForProposalByNameAndFirstName(StringUtils
					.getUoCode(currentProposal.getCode()), proposalNumberInt, form.getName(), form.getFirstName());

			if (scientists == null || scientists.length < 1) {
				LOG.info("No person retrieved from SMIS database: "+form.getName() + form.getFirstName());
				// Retrieves all the scientists saved in the SMIS database, for this proposal
				scientists = ScientistsFromSMIS.findScientistsForProposalByNameAndFirstName(
						StringUtils.getUoCode(currentProposal.getCode()), proposalNumberInt, null, null);
				List<ScientistInfosBean> listOfScientistsInfo = new ArrayList<ScientistInfosBean>();

				for (int j = 0; j < scientists.length; j++) {
					// fill the person and labo
					Person3VO currentPerson = ScientistsFromSMIS.extractPersonInfo(scientists[j]);
					Laboratory3VO currentLabo = ScientistsFromSMIS.extractLaboratoryInfo(scientists[j]);
					listOfScientistsInfo.add(new ScientistInfosBean(currentPerson, currentLabo));
				}

				form.setListOfScientists(listOfScientistsInfo);
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "No match found in SMIS Database."));
				saveErrors(request, errors);

				// redirect to the display of the scientists list
				return "labContactLoginPage";

			} else if (scientists.length == 1) {
				LOG.info("1 Person retrieved from SMIS database.");
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free",
						"(Informations retrieved from SMIS database)"));
				saveMessages(request, messages);

				// fill the person and labo
				person = ScientistsFromSMIS.extractPersonInfo(scientists[0]);
				laboratory = ScientistsFromSMIS.extractLaboratoryInfo(scientists[0]);
				// createLabContactCard
				return createLabContactCard(form, person, laboratory, proposalId);

			} else {
				// there is several matching persons
				LOG.info("several persons retrieved from SMIS database.");
				List<ScientistInfosBean> listOfScientistsInfo = new ArrayList<ScientistInfosBean>();

				for (int j = 0; j < scientists.length; j++) {
					// fill the person and labo
					Person3VO currentPerson = ScientistsFromSMIS.extractPersonInfo(scientists[j]);
					Laboratory3VO currentLabo = ScientistsFromSMIS.extractLaboratoryInfo(scientists[j]);
					listOfScientistsInfo.add(new ScientistInfosBean(currentPerson, currentLabo));
				}

				form.setListOfScientists(listOfScientistsInfo);

				// redirect to the display of the scientists list
				return "labContactLoginPage";
			}
		} catch (Exception e) {
			LOG.debug("Error raises during the scientist research");
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.getMessage()));
			saveErrors(request, errors);
			return "labContactLoginPage";
		}
	}

	private String createLabContactCard(ViewLabContactForm form, Person3VO person, Laboratory3VO laboratory, Integer proposalId)
			throws Exception {

		if (person != null)
			form.setPerson(person);
		if (laboratory != null)
			form.setLaboratory(laboratory);

		LabContact3VO labContact = new LabContact3VO();
		// generate labContact infos
		if (person != null & laboratory != null)
			labContact.setCardName(generateCardName(person, laboratory, proposalId));
		labContact.setDewarAvgCustomsValue(0);
		labContact.setDewarAvgTransportValue(0);
		form.setLabContact(labContact);

		if (person != null & laboratory != null)
			LOG.debug("Create lab-contact form with Person: " + person + " AND Laboratory: " + laboratory);
		else
			LOG.debug("Create lab-contact form.");

		LOG.info("createLabContactCard");
		return "labContactCreatePage";
	}

	/**
	 * Generates the lab-contact card name thanks to the person and laboratory names
	 * 
	 * @param person
	 * @param laboratory
	 * @return
	 * @throws Exception
	 */
	private String generateCardName(Person3VO person, Laboratory3VO laboratory, Integer proposalId) throws Exception {
		int maxLength = 15;

		String personName = person.getFamilyName();
		personName = personName.substring(0, Math.min(personName.length(), maxLength));

		String laboName = laboratory.getName();
		laboName = laboName.substring(0, Math.min(laboName.length(), maxLength));

		String cardName = personName + "-" + laboName;// max length = 15x2 + 1 = 31
		// check the card name does not exist yet
		String cardNameWithNum = cardName;
		try {
			List<LabContact3VO> labContact = labCService.findFiltered(proposalId, cardNameWithNum);
			int i = 1;
			while (labContact.size() != 0) {
				cardNameWithNum = cardName + "_" + i; // max length = 31 + 1 + l(i) = 32+
				labContact = labCService.findByCardName(cardNameWithNum);
				LOG.info(labContact);
				i++;
			}
		} catch (FinderException e) {
			LOG.error(e.getMessage());
		} catch (NamingException e) {
			LOG.error(e.getMessage());
		}
		// retrieves a card name which does not exist yet
		cardName = cardNameWithNum;

		LOG.debug("Generated card name is : " + cardName);
		return cardName;
	}

	/**
	 * CheckRequiredFieldsPopulated
	 * 
	 * @param form
	 *            The form to get Fields from. Checks that Required Fields are there. Redirect to
	 */
	public boolean CheckRequiredFieldsPopulated(ViewLabContactForm form, HttpServletRequest request) {
		boolean requiredFieldsPresent = true;
		ActionMessages l_ActionMessages = new ActionMessages();

		// card name
		if (form.getLabContact().getCardName().length() == 0) {
			requiredFieldsPresent = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.required", "Card Name Label");
			l_ActionMessages.add("info.LabContactName", l_ActionMessageLabel);
		}

		// person familyName
		if (form.getPerson().getFamilyName().length() == 0) {
			requiredFieldsPresent = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.required", "Family Name Label");
			l_ActionMessages.add("info.LabContactName", l_ActionMessageLabel);
		}
		// person firstName
		if (form.getPerson().getGivenName().length() == 0) {
			requiredFieldsPresent = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.required", "First Name Label");
			l_ActionMessages.add("info.LabContactName", l_ActionMessageLabel);
		}

		// lab name
		if (form.getLaboratory().getName().length() == 0) {
			requiredFieldsPresent = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.required", "Laboratory Name Label");
			l_ActionMessages.add("info.LabContactName", l_ActionMessageLabel);
		}
		// lab address
		if (form.getLaboratory().getAddress().length() == 0) {
			requiredFieldsPresent = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.required", "Laboratory Address Label");
			l_ActionMessages.add("info.LabContactName", l_ActionMessageLabel);
		}

		if (!requiredFieldsPresent)
			request.setAttribute("org.apache.struts.action.ERROR", l_ActionMessages);

		return requiredFieldsPresent;
	}

	/**
	 * CheckLoginRequiredFieldsPopulated
	 * 
	 * @param form
	 *            The form to get Fields from. Checks that Required Fields are there. Redirect to
	 */
	public boolean CheckLoginRequiredFieldsPopulated(ViewLabContactForm form, HttpServletRequest request) {
		boolean requiredFieldsPresent = true;
		ActionMessages l_ActionMessages = new ActionMessages();

		// //family name
		// if (form.getPerson().getFamilyName().length() == 0) {
		// requiredFieldsPresent = false;
		// ActionMessage l_ActionMessageLabel = new ActionMessage("errors.required","FamilyName label");
		// l_ActionMessages.add("person_familyName", l_ActionMessageLabel);
		// }
		// else if (form.getPerson().getFamilyName().length() < 3) {
		// requiredFieldsPresent = false;
		// ActionMessage l_ActionMessageLabel = new ActionMessage("errors.minlength","FamilyName label",3);
		// l_ActionMessages.add("person_familyName", l_ActionMessageLabel);
		// }
		//
		// //first name
		// if (form.getPerson().getGivenName().length() == 0) {
		// requiredFieldsPresent = false;
		// ActionMessage l_ActionMessageLabel = new ActionMessage("errors.required","FirstName label");
		// l_ActionMessages.add("person_familyName", l_ActionMessageLabel);
		// }
		// else if (form.getPerson().getGivenName().length() < 3) {
		// requiredFieldsPresent = false;
		// ActionMessage l_ActionMessageLabel = new ActionMessage("errors.minlength","FirstName label",3);
		// l_ActionMessages.add("person_familyName", l_ActionMessageLabel);
		// }

		if (!requiredFieldsPresent)
			request.setAttribute("org.apache.struts.action.ERROR", l_ActionMessages);

		return requiredFieldsPresent;
	}

	// /**
	// * CheckConstraints
	// * @param form
	// * @param request
	// * @return
	// */public boolean CheckConstraints(ViewLabContactForm form, HttpServletRequest request, ActionMessages errors)
	// throws Exception
	// {
	// boolean constraintsOK = true;
	// // ---------------------------------------------------------------------------------------------------
	// // Shipment Name = LabContactName -> must be unique
	//
	//
	// // ---------------------------------------------------------------------------------------------------
	// // Acknowledge action
	// // TODO this is used ? if yes, store this error in the properties
	//
	// ActionMessage l_ActionMessagePassword = new ActionMessage("errors.detail",
	// "Sorry, but there are some problems reading your proposal account");
	// errors.add(ActionMessages.GLOBAL_MESSAGE,l_ActionMessagePassword);
	//
	// saveErrors(request, errors);
	//
	// return constraintsOK;
	// }

	/**
	 * checks if the lab contact in the form are correct and well formated, returns false if at least one field is incorrect
	 * 
	 * @param form
	 * @return
	 */
	private boolean checkLabContactInformations(ViewLabContactForm form, HttpServletRequest request) {
		ActionMessages l_ActionMessages = new ActionMessages();
		boolean isOk = true;
		// card name
		if (form.getLabContact().getCardName() != null
				&& form.getLabContact().getCardName().length() > DBConstants.MAX_LENGTH_LABCONTACT_CARDNAME) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Card name",
					DBConstants.MAX_LENGTH_LABCONTACT_CARDNAME);
			l_ActionMessages.add("labContact.cardName", l_ActionMessageLabel);
		}
		// tel
		if (form.getPerson().getPhoneNumber() != null
				&& form.getPerson().getPhoneNumber().length() > DBConstants.MAX_LENGTH_PERSON_PHONENUMBER) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Telephone",
					DBConstants.MAX_LENGTH_PERSON_PHONENUMBER);
			l_ActionMessages.add("person.phoneNumber", l_ActionMessageLabel);
		}
		// fax
		if (form.getPerson().getFaxNumber() != null
				&& form.getPerson().getFaxNumber().length() > DBConstants.MAX_LENGTH_PERSON_FAXNUMBER) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Fax", DBConstants.MAX_LENGTH_PERSON_FAXNUMBER);
			l_ActionMessages.add("person.faxNumber", l_ActionMessageLabel);
		}
		// email
		if (form.getPerson().getEmailAddress() != null
				&& form.getPerson().getEmailAddress().length() > DBConstants.MAX_LENGTH_PERSON_EMAILADRESS) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Email", DBConstants.MAX_LENGTH_PERSON_EMAILADRESS);
			l_ActionMessages.add("person.emailAddress", l_ActionMessageLabel);
		}
		if (form.getPerson().getEmailAddress() != null && form.getPerson().getEmailAddress().trim().length() > 0) {
			if (!StringUtils.isValidEmailAdress(form.getPerson().getEmailAddress())) {
				isOk = false;
				ActionMessage l_ActionMessageLabel = new ActionMessage("errors.email", "Email");
				l_ActionMessages.add("person.emailAddress", l_ActionMessageLabel);
			}
		}
		// lab name
		if (form.getLaboratory().getName() != null && form.getLaboratory().getName().length() > DBConstants.MAX_LENGTH_LABORATORY_NAME) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Lab name", DBConstants.MAX_LENGTH_LABORATORY_NAME);
			l_ActionMessages.add("laboratory.name", l_ActionMessageLabel);
		}
		// lab address
		if (form.getLaboratory().getAddress() != null
				&& form.getLaboratory().getAddress().length() > DBConstants.MAX_LENGTH_LABORATORY_ADDRESS) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Lab address",
					DBConstants.MAX_LENGTH_LABORATORY_ADDRESS);
			l_ActionMessages.add("laboratory.address", l_ActionMessageLabel);
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

}
