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
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Laboratory3VO;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
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
 * @struts.action name="viewShippingHistoryForm" path="/reader/viewShippingHistoryAction"
 *                type="ispyb.client.common.shipping.ViewShippingHistoryAction" validate="false" parameter="reqCode"
 *                scope="request"
 * 
 * @struts.action-forward name="shippingHistoryPage" path="user.shipping.history.page"
 * 
 * @struts.action-forward name="shippingBlomHistoryPage" path="blom.shipping.history.page"
 * 
 * @struts.action-forward name="shippingManagerHistoryPage" path="manager.shipping.history.page"
 * 
 * @struts.action-forward name="shippingFedexmanagerHistoryPage" path="fedexmanager.shipping.history.page"
 * 
 * @struts.action-forward name="shippingStoreHistoryPage" path="store.shipping.history.page"
 * 
 * @struts.action-forward name="shippingLocalcontactHistoryPage" path="localcontact.shipping.history.page"
 * 
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 */

public class ViewShippingHistoryAction extends org.apache.struts.actions.DispatchAction {
	private final Logger LOG = Logger.getLogger(ViewShippingHistoryAction.class);
	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
	private Person3Service person3Service;
	private Laboratory3Service laboratory3Service;
	private LabContact3Service labContact3Service;

	
	private void initServices() throws CreateException, NamingException {

		this.labContact3Service = (LabContact3Service) ejb3ServiceLocator.getLocalService(LabContact3Service.class);
		this.person3Service = (Person3Service) ejb3ServiceLocator.getLocalService(Person3Service.class);
		this.laboratory3Service = (Laboratory3Service) ejb3ServiceLocator.getLocalService(Laboratory3Service.class);

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
	 * @param in_response
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_response) {
		ActionMessages errors = new ActionMessages();
		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			ViewShippingHistoryForm form = (ViewShippingHistoryForm) actForm;
			String shippingId = request.getParameter(Constants.SHIPPING_ID); // Parameters submited by link
			// ---------------------------------------------------------------------------------------------------

			// shipping
			Shipping3VO selectedShipping = DBTools.getSelectedShipping(new Integer(shippingId));

			// Populate the BreadCrumbsForm -----------------------
			BreadCrumbsForm.getItClean(request).setSelectedShipping(selectedShipping);
			BreadCrumbsForm.getIt(request).setSelectedDewar(null);

			// Populate the Form ----------------------------------

			// shipping
			form.setShipping(selectedShipping);

			Dewar3Service dewarService = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);
			List<Dewar3VO> listDewars = dewarService.findFiltered(null, selectedShipping.getShippingId(), null, null, null,
					null, null, null, null, true, false);
			form.setParcelsNumber(listDewars.size());
//			int i=0;
//			for (Iterator<Dewar3VO> d = listDewars.iterator();d.hasNext();){
//				Dewar3VO dewar = d.next();
//				dewar = dewarService.loadEager(dewar);
//				listDewars.set(i, dewar);
//				i++;
//			}
			// dewars
			form.setDewars(listDewars);

			// labcontact
			LabContact3VO sendingLabContact = this.labContact3Service.findByPk(selectedShipping.getSendingLabContactId());
			form.setSendingLabContact(sendingLabContact);
			LabContact3VO returnLabContact = this.labContact3Service.findByPk(selectedShipping.getReturnLabContactId());
			form.setReturnLabContact(returnLabContact);

			// person
			Person3VO  sendingPerson = this.person3Service.findByPk(sendingLabContact.getPersonVOId());
			form.setSendingPerson(sendingPerson);
			Person3VO  returnPerson = this.person3Service.findByPk(returnLabContact.getPersonVOId());
			form.setReturnPerson(returnPerson);

			// laboratory
			Laboratory3VO  sendingLaboratory = this.laboratory3Service.findByPk(sendingPerson.getLaboratoryVOId());
			form.setSendingLaboratory(sendingLaboratory);
			Laboratory3VO returnLaboratory = this.laboratory3Service.findByPk(returnPerson.getLaboratoryVOId());
			form.setReturnLaboratory(returnLaboratory);

			// redirect to the role view page -----------------------
			RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
			String role = roleObject.getName();

			if (role.equals(Constants.ROLE_STORE)) {
				return mapping.findForward("shippingStoreHistoryPage");
			} else if (role.equals(Constants.ROLE_BLOM)) {
				return mapping.findForward("shippingBlomHistoryPage");
			} else if (role.equals(Constants.FXMANAGE_ROLE_NAME)) {
				return mapping.findForward("shippingFedexmanagerHistoryPage");
			} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
				return mapping.findForward("shippingLocalcontactHistoryPage");
			} else if (role.equals(Constants.ROLE_MANAGER)) {
				return mapping.findForward("shippingManagerHistoryPage");
			} else {// DEFAULT : User view
				return mapping.findForward("shippingHistoryPage");
			}
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
	}

}
