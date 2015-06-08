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
package ispyb.client.mx.sample;

import ispyb.client.common.BreadCrumbsForm;
import ispyb.common.util.Constants;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.services.sample.Protein3Service;
import ispyb.server.mx.vos.sample.Protein3VO;

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
import org.apache.struts.actions.DispatchAction;

/**
 * @struts.action name="viewCrystalForm" path="/user/createProtein" type="ispyb.client.mx.sample.CreateProteinAction"
 *                input="user.protein.create.page" validate="false" parameter="reqCode" scope="request"
 * @struts.action-forward name="proteinCreatePage" path="user.protein.create.page"
 * @struts.action-forward name="crystalCreatePage" path="user.crystal.create.page"
 * @struts.action-forward name="error" path="site.default.error.page"
 */

public class CreateProteinAction extends DispatchAction {
	
	private final static Logger LOG = Logger.getLogger(CreateProteinAction.class);

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Protein3Service proteinService;

	private Proposal3Service proposalService;

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws CreateException, NamingException {

		this.proteinService = (Protein3Service) ejb3ServiceLocator.getLocalService(Protein3Service.class);
		this.proposalService = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);

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

		return mapping.findForward("proteinCreatePage");
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
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();

		// RIC messages queue:
		ActionMessages messages = new ActionMessages();

		try {

			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			if (!CreateCrystalAction.CheckIsAllowedToCreateProtein(proposalId)){
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Not Authorized to create a protein"));
				saveErrors(request, errors);
				return mapping.findForward("error");
			}
			ViewCrystalForm form = (ViewCrystalForm) actForm;

			// Check Required fields populated
			if (!this.CheckRequiredFieldsPopulated(form, request))
				return mapping.findForward("proteinCreatePage");

			// Populate with info from form

			Protein3VO plv = form.getProteinInfo();

			// test if acronym already exists
			List<Protein3VO> res = proteinService.findByAcronymAndProposalId(proposalId, plv.getAcronym());
			if (res != null && res.size() > 0) {
				errors.add("proteinInfo.acronym", new ActionMessage("errors.alreadyExist", "Acronym"));
				saveErrors(request, errors);
				return mapping.findForward("proteinCreatePage");
			}

			// if name is null, then name = acronym
			if (plv.getName() == null || plv.getName().equals("")) {
				plv.setName(plv.getAcronym());
			}
			Proposal3VO proposal = proposalService.findByPk(proposalId);
			plv.setProposalVO(proposal);

			// Insert into DB and retrieve primary key
			Protein3VO newValue = proteinService.create(plv);
			Integer proteinId = newValue.getProteinId();

			form.setTheProteinId(proteinId);
			
			LOG.debug("create Protein = " + plv.getAcronym() +" for proposal = "+proposal.getProposalAccount());

			// fill breadscrumbs
			BreadCrumbsForm.getIt(request).setSelectedProtein(newValue);

			// save list of geometry in request
			request.setAttribute("listGeometryClass", form.getListGeometryClass()); // SpaceGroup
																					// info

			// ---------------------------------------------------------------------------------------------------
			// Acknowledge action
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.inserted", plv.getAcronym()));
			saveMessages(request, messages);

			// save protein info in form
			form.setProteinInfo(plv);
			// form.setSelectedProteinId(plv.getProteinId());
			form.setAcronym(plv.getAcronym());

			// redirect to crystal form
			response.sendRedirect(request.getContextPath() + "/user/createCrystal.do?reqCode=display");
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return null;
	}

	/**
	 * CheckRequiredFieldsPopulated
	 * 
	 * @param form
	 *            The form to get Fields from. Checks that Required Fields are there with the correct formatting.
	 */
	public boolean CheckRequiredFieldsPopulated(ViewCrystalForm form, HttpServletRequest request) {

		ActionMessages l_ActionMessages = new ActionMessages();
		request.setAttribute("org.apache.struts.action.ERROR", l_ActionMessages);

		boolean requiredFieldsPresent = true;

		if (form.getProteinInfo().getAcronym() == null || form.getProteinInfo().getAcronym().equals(""))

		{
			requiredFieldsPresent = false;

			ActionMessage l_ActionMessageAcronym = new ActionMessage("errors.required", "Acronym"); // Acronym
			l_ActionMessages.add("proteinInfo.acronym", l_ActionMessageAcronym);
		} else if (!form.getProteinInfo().getAcronym().matches(Constants.MASK_BASIC_CHARACTERS_WITH_DASH_NO_SPACE)) {
			requiredFieldsPresent = false;

			ActionMessage l_ActionMessageAcronym = new ActionMessage("errors.format", "Acronym");
			l_ActionMessages.add("proteinInfo.acronym", l_ActionMessageAcronym);
		}

		return requiredFieldsPresent;
	}

}
