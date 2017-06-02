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

import ispyb.client.security.roles.RoleDO;
import ispyb.client.common.BreadCrumbsForm;
import ispyb.common.util.DBTools;
import ispyb.common.util.Constants;
import ispyb.server.common.services.proposals.LabContact3Service;
import ispyb.server.common.services.proposals.Laboratory3Service;
import ispyb.server.common.services.proposals.Person3Service;
import ispyb.server.common.services.shipping.Dewar3Service;
import ispyb.server.common.services.shipping.DewarTransportHistory3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Laboratory3VO;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.DewarTransportHistory3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;

import java.util.List;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * @struts.action name="viewDewarHistoryForm" path="/reader/viewDewarHistoryAction"
 *                type="ispyb.client.common.shipping.ViewDewarHistoryAction" validate="false" parameter="reqCode"
 *                scope="request"
 * 
 * @struts.action-forward name="dewarHistoryPage" path="user.shipping.dewar.history.page"
 * 
 * @struts.action-forward name="dewarBlomHistoryPage" path="blom.shipping.dewar.history.page"
 * 
 * @struts.action-forward name="dewarManagerHistoryPage" path="manager.shipping.dewar.history.page"
 * 
 * @struts.action-forward name="dewarFedexmanagerHistoryPage" path="fedexmanager.shipping.dewar.history.page"
 * 
 * @struts.action-forward name="dewarStoreHistoryPage" path="store.shipping.dewar.history.page"
 * 
 * @struts.action-forward name="dewarLocalcontactHistoryPage" path="localcontact.shipping.dewar.history.page"
 * 
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 */

public class ViewDewarHistoryAction extends org.apache.struts.actions.DispatchAction {
	private final Logger LOG = Logger.getLogger(ViewDewarHistoryAction.class);
	
	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Person3Service person3Service;

	private Laboratory3Service laboratory3Service;

	private LabContact3Service labContact3Service;

	private DewarTransportHistory3Service dewarTransportHistory3Service;

	private void initServices() throws CreateException, NamingException {

		this.labContact3Service = (LabContact3Service) ejb3ServiceLocator.getLocalService(LabContact3Service.class);
		this.person3Service = (Person3Service) ejb3ServiceLocator.getLocalService(Person3Service.class);
		this.laboratory3Service = (Laboratory3Service) ejb3ServiceLocator.getLocalService(Laboratory3Service.class);
		this.dewarTransportHistory3Service = (DewarTransportHistory3Service) ejb3ServiceLocator
				.getLocalService(DewarTransportHistory3Service.class);

	}

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

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
		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			ViewDewarHistoryForm form = (ViewDewarHistoryForm) actForm;
			String dewarId = request.getParameter(Constants.DEWAR_ID); // Parameters submited by link
			// ---------------------------------------------------------------------------------------------------

			// dewar
			Dewar3VO selectedDewar = DBTools.getSelectedDewar(new Integer(dewarId));
			// shipping
			Shipping3VO selectedShipping = selectedDewar.getShippingVO();

			// Populate the BreadCrumbsForm -----------------------
			BreadCrumbsForm.getItClean(request).setSelectedShipping(selectedShipping);
			BreadCrumbsForm.getIt(request).setSelectedDewar(selectedDewar);

			// Populate the Form ----------------------------------
			// dewar
			form.setDewar(selectedDewar);

			// dewarEvents
			List<DewarTransportHistory3VO> dewarEvents = this.dewarTransportHistory3Service.findByDewarId(new Integer(
					dewarId));
			form.setDewarEvents(dewarEvents);

			// shipping
			form.setShipping(selectedShipping);

			Dewar3Service dewarService = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);
			List<Dewar3VO> listDewars = dewarService.findFiltered(null, selectedShipping.getShippingId(), null, null,
					null, null, null, null, null, false, false);
			form.setParcelsNumber(listDewars.size());

			// labcontact
			LabContact3VO sendingLabContact = this.labContact3Service.findByPk(selectedShipping
					.getSendingLabContactId());
			form.setSendingLabContact(sendingLabContact);
			LabContact3VO returnLabContact = this.labContact3Service.findByPk(selectedShipping.getReturnLabContactId());
			form.setReturnLabContact(returnLabContact);

			// person
			Person3VO sendingPerson = null;

			if (sendingLabContact != null)
				sendingPerson = this.person3Service.findByPk(sendingLabContact.getPersonVOId());
			form.setSendingPerson(sendingPerson);

			Person3VO returnPerson = null;
			if (returnLabContact != null)
				returnPerson = this.person3Service.findByPk(returnLabContact.getPersonVOId());
			form.setReturnPerson(returnPerson);

			// laboratory
			Laboratory3VO sendingLaboratory = null;
			if (sendingPerson != null)
				sendingLaboratory = this.laboratory3Service.findByPk(sendingPerson.getLaboratoryVOId());
			form.setSendingLaboratory(sendingLaboratory);

			Laboratory3VO returnLaboratory = null;
			if (returnPerson != null)
				returnLaboratory = this.laboratory3Service.findByPk(returnPerson.getLaboratoryVOId());
			form.setReturnLaboratory(returnLaboratory);

			// redirect to the role view page -----------------------
			RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
			String role = roleObject.getName();

			if (role.equals(Constants.ROLE_STORE)) {
				return mapping.findForward("dewarStoreHistoryPage");
			} else if (role.equals(Constants.ROLE_BLOM)) {
				return mapping.findForward("dewarBlomHistoryPage");
			} else if (role.equals(Constants.FXMANAGE_ROLE_NAME)) {
				return mapping.findForward("dewarFedexmanagerHistoryPage");
			} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
				return mapping.findForward("dewarLocalcontactHistoryPage");
			} else if (role.equals(Constants.ROLE_MANAGER)) {
				return mapping.findForward("dewarManagerHistoryPage");
			} else {// DEFAULT : User view
				return mapping.findForward("dewarHistoryPage");
			}
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
	}

}
