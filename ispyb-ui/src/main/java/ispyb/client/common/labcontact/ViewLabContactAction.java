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
/**
 * ViewSampleAction.java
 */

package ispyb.client.common.labcontact;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.common.util.Constants;
import ispyb.server.common.services.proposals.LabContact3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.LabContact3VO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

/**
 * User view
 * 
 * @struts.action name="viewLabContactForm" path="/user/viewLabContactAction"
 *                type="ispyb.client.common.labcontact.ViewLabContactAction" validate="false" parameter="reqCode"
 *                scope="request"
 * 
 *                Store view
 * @struts.action name="viewLabContactForm" path="/store/viewLabContactAction"
 *                type="ispyb.client.common.labcontact.ViewLabContactAction" validate="false" parameter="reqCode"
 *                scope="request"
 * 
 * @struts.action-forward name="labContactViewPage" path="user.labcontact.view.page"
 * 
 * @struts.action-forward name="labContactStoreViewPage" path="store.labcontact.view.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 * 
 */

public class ViewLabContactAction extends DispatchAction {

	private final static Logger LOG = Logger.getLogger(ViewLabContactAction.class);

	// Session Attributes
	Integer mProposalId = null;

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private LabContact3Service labCService;

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() {
		try {
			this.labCService = (LabContact3Service) ejb3ServiceLocator.getLocalService(LabContact3Service.class);

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
			ViewLabContactForm form = (ViewLabContactForm) actForm; // Parameters submitted by form

			this.RetrieveSessionAttributes(request); // Session parameters

			// ---------------------------------------------------------------------------------------------------

			// Get an object list.
			List<LabContact3VO> listOfLabContacts = labCService.findFiltered(this.mProposalId, null);

			// load all labcontacts infos from the DataBase (LabContact -> Person -> Laboratory)
			// for (Iterator<LabContact3VO> iterator = listOfLabContacts.iterator(); iterator.hasNext();) {
			// LabContact3VO labContact = iterator.next();
			// // load person infos
			// Person3VO person = personService.findByPk(labContact.getPersonVOId(), false);
			// labContact.setPersonVO(person);
			// }
			List<Integer> listLabContactDeleted = new ArrayList<Integer>();
			for (Iterator<LabContact3VO> iterator = listOfLabContacts.iterator(); iterator.hasNext();) {
				LabContact3VO labContact = iterator.next();
				int nbShipping = labCService.hasShipping(labContact.getLabContactId());
				listLabContactDeleted.add(nbShipping);
			}

			// Populate with Info
			form.setListOfLabContacts(listOfLabContacts);
			form.setListLabContactDeleted(listLabContactDeleted);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.EDIT_MODE);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return mapping.findForward("labContactViewPage");
	}

	/**
	 * Display all LabContact cards Used for the Store view
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward displayAll(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();
		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			ViewLabContactForm form = (ViewLabContactForm) actForm; // Parameters submited by form

			// ---------------------------------------------------------------------------------------------------
			// Get an object list.
			List<LabContact3VO> listOfLabContacts = labCService.findAll();

			// load all labcontacts infos from the DataBase (LabContact -> Person -> Laboratory)
			// for (Iterator<LabContact3VO> iterator = listOfLabContacts.iterator(); iterator.hasNext();) {
			// LabContact3VO labContact = iterator.next();
			// // load person infos
			// Person3VO person = personService.findByPk(labContact.getPersonVOId(), false);
			// labContact.setPersonVO(person);
			// // load proposal infos
			// Proposal3VO proposal = proposalService.findByPk(labContact.getProposalVOId());
			// labContact.setProposalVO(proposal);
			// }
			List<Integer> listLabContactDeleted = new ArrayList<Integer>();
			for (Iterator<LabContact3VO> iterator = listOfLabContacts.iterator(); iterator.hasNext();) {
				LabContact3VO labContact = iterator.next();
				int nbShipping = labCService.hasShipping(labContact.getLabContactId());
				listLabContactDeleted.add(nbShipping);
			}

			// Populate with Info
			form.setListOfLabContacts(listOfLabContacts);
			form.setListLabContactDeleted(listLabContactDeleted);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return mapping.findForward("labContactStoreViewPage");
	}

	/**
	 * Close the LabContact. Cannot be edited when closed
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward deleteLabContact(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_response) {
		ActionMessages errors = new ActionMessages();
		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			Integer labContactId = new Integer(request.getParameter(Constants.LABCONTACT_ID));

			// Delete
			labCService.deleteByPk(labContactId);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return this.display(mapping, actForm, request, in_response);
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
}
