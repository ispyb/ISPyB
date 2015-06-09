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

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.util.DBConstants;
import ispyb.common.util.Constants;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.services.sample.Crystal3Service;
import ispyb.server.mx.services.sample.DiffractionPlan3Service;
import ispyb.server.mx.services.sample.Protein3Service;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.DiffractionPlan3VO;
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

/**
 * @struts.action name="viewCrystalForm" path="/user/createCrystal" type="ispyb.client.mx.sample.CreateCrystalAction"
 *                input="user.crystal.create.page" validate="false" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="crystalCreatePage" path="user.crystal.create.page"
 * @struts.action-forward name="viewProteinAndCrystal" path="user.sample.viewProteinAndCrystal.page"
 * @struts.action-forward name="error" path="site.default.error.page"
 */

public class CreateCrystalAction extends org.apache.struts.actions.DispatchAction {

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Protein3Service proteinService;

	private Crystal3Service crystalService;

	private DiffractionPlan3Service difPlanService;

	private final static Logger LOG = Logger.getLogger(CreateCrystalAction.class);

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws CreateException, NamingException {

		this.proteinService = (Protein3Service) ejb3ServiceLocator.getLocalService(Protein3Service.class);
		this.crystalService = (Crystal3Service) ejb3ServiceLocator.getLocalService(Crystal3Service.class);
		this.difPlanService = (DiffractionPlan3Service) ejb3ServiceLocator
				.getLocalService(DiffractionPlan3Service.class);

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
	 * @param response
	 * @return
	 */
	public ActionForward displayFromMenu(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		LOG.debug("create crystal : display from menu");
		BreadCrumbsForm.getItClean(request);

		return this.display(mapping, actForm, request, response);
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
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
		boolean IsAllowedToCreateProtein = CreateCrystalAction.CheckIsAllowedToCreateProtein(proposalId);

		Integer proteinId;
		String originator = BreadCrumbsForm.getIt(request).getFromPage();
		LOG.debug("create crystal : display : originator = " + originator);

		try {

			// Retrieve Attributes
			ViewCrystalForm form = (ViewCrystalForm) actForm;

			// if coming from viewProteinAndCrystal
			String proteinIdstr = request.getParameter(Constants.PROTEIN_ID);
			if (proteinIdstr != null) {
				proteinId = new Integer(proteinIdstr);
				BreadCrumbsForm.getIt(request).setSelectedProtein(proteinService.findByPk(proteinId, true));
			}

			// Retrieve list of Proteins
			List<Protein3VO> fullProteinList = proteinService.findByAcronymAndProposalId(proposalId, null, false, true);

			// Populate with Info
			form.setListProtein(fullProteinList);
			form.setAllowedTocreateProtein(IsAllowedToCreateProtein);
			request.setAttribute("listGeometryClass", form.getListGeometryClass()); // SpaceGroup info

			// if a protein has been selected
			if (BreadCrumbsForm.getIt(request).getSelectedProtein() != null) {
				form.setTheProteinId(BreadCrumbsForm.getIt(request).getSelectedProtein().getProteinId());
			}

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.CREATE_MODE);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return mapping.findForward("crystalCreatePage");
	}

	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward newProtein(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();

		try {
			response.sendRedirect(request.getContextPath() + "/user/createProtein.do?reqCode=display");

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.sample.createProtein"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}
		return null;

	}

	/**
	 * save
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return proposalId Save the Crystal into the DB
	 */
	public ActionForward save(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
		String originator = BreadCrumbsForm.getIt(request).getFromPage();

		LOG.debug("save crystal : originator = " + originator);

		try {

			// Retrieve list of Proteins
			List<Protein3VO> fullProteinList = proteinService.findByAcronymAndProposalId(proposalId, null);

			ViewCrystalForm form = (ViewCrystalForm) actForm;
			Crystal3VO info = form.getInfo();

			request.setAttribute("listGeometryClass", form.getListGeometryClass());

			form.setListProtein(fullProteinList);

			LOG.debug("Creating Spacegroup " + form.getSpaceGroup() + " for proteinId " + form.getTheProteinId()
					+ "(proposalId=" + proposalId + ")");

			// ----------------------------------------------------------------------------------------------------
			// Check Required fields populated and crystal type not already
			// exist
			if (!this.CheckRequiredFieldsPopulated(form, request)) {
				errors.add("theProteinId", new ActionMessage("errors.required", "Protein"));
				saveErrors(request, errors);
				return mapping.findForward("crystalCreatePage");
			}

			if (!this.checkCrystalInformations(form, request)) {
				return mapping.findForward("crystalCreatePage");
			}

			// Test if crystal form already exists with same cell parameters
			Crystal3VO currentCrystal = new Crystal3VO();
			currentCrystal.setSpaceGroup(form.getSpaceGroup());
			currentCrystal.setCellA(form.getInfo().getCellA());
			currentCrystal.setCellB(form.getInfo().getCellB());
			currentCrystal.setCellC(form.getInfo().getCellC());
			currentCrystal.setCellAlpha(form.getInfo().getCellAlpha());
			currentCrystal.setCellBeta(form.getInfo().getCellBeta());
			currentCrystal.setCellGamma(form.getInfo().getCellGamma());
			Protein3VO currentProtein = proteinService.findByPk(form.getTheProteinId(), false);
			Crystal3VO foundCrystal = crystalService.findByAcronymAndCellParam(currentProtein.getAcronym(),
					currentCrystal, proposalId);
			if (foundCrystal != null) {
				errors.add("spaceGroup", new ActionMessage("errors.alreadyExist", "Space Group"));
				saveErrors(request, errors);
				return mapping.findForward("crystalCreatePage");
			}

			// Populate with info from form and insert into DB
			Protein3VO plv = proteinService.findByPk(form.getTheProteinId(), false);
			info.setProteinVO(plv);
			info.setSpaceGroup(form.getSpaceGroup());

			// Populate with new empty diffraction plan

			DiffractionPlan3VO newDifPlan = new DiffractionPlan3VO();
			newDifPlan.setExperimentKind(Constants.LIST_EXPERIMENT_KIND[0]);
			newDifPlan = difPlanService.create(newDifPlan);

			info.setDiffractionPlanVO(newDifPlan);

			// insert into DB

			Crystal3VO newValue = crystalService.create(info);

			// update breadCrumbsForm

			Integer crystalId = newValue.getCrystalId();

			BreadCrumbsForm.getIt(request).setSelectedCrystal(newValue);
			BreadCrumbsForm.getIt(request).setSelectedCrystalId(crystalId);
			BreadCrumbsForm.getIt(request).setSelectedProtein(plv);
			
			// warning if cell values are empty
			if (!newValue.hasCellInfo()){
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free", "Warning: the unit cell parameters are not filled!"));
				saveMessages(request, messages);
			}

			// ---------------------------------------------------------------------------------------------------
			// Acknowledge action
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.inserted", info.getSpaceGroup()));
			saveMessages(request, messages);

			// Populate with new value
			form.setInfo(newValue);
			request.setAttribute("listGeometryClass", form.getListGeometryClass()); // SpaceGroup

			// go back to createSample in the case it is called from createSampleAction
			// but not if called from viewProteinAndCrystal

			if ((originator != null && originator.toLowerCase().indexOf("viewproteinandcrystal") == -1)) {
				response.sendRedirect(request.getContextPath() + "/user/createSampleAction.do?reqCode=display");
				return null;
			}
			//response.sendRedirect(request.getContextPath() + "/user/viewProteinAndCrystal.do?reqCode=display");
			//return mapping.findForward("viewProteinAndCrystal");
			//return null;
			return mapping.findForward("crystalCreatePage");
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.getMessage()));
			e.printStackTrace();
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
	}

	/**
	 * CheckRequiredFieldsPopulated
	 * 
	 * @param form
	 *            The form to get Fields from. Checks that Required Fields are there.
	 */
	public boolean CheckRequiredFieldsPopulated(ViewCrystalForm form, HttpServletRequest request) {
		boolean requiredFieldsPresent = true;
		ActionMessages l_ActionMessages = new ActionMessages();

		if (form.getTheProteinId() == null || form.getTheProteinId().intValue() == 0) // Protein
		{
			requiredFieldsPresent = false;

			request.setAttribute("org.apache.struts.action.ERROR", l_ActionMessages);

			ActionMessage l_ActionMessagePassword = new ActionMessage("errors.required", "Protein");
			l_ActionMessages.add("theProteinId", l_ActionMessagePassword);

		}

		return requiredFieldsPresent;
	}

	public static boolean CheckIsAllowedToCreateProtein(Integer proposalId) {
		// Do various checks there if you want to allow certain proposals to create proteins
		if (Constants.SITE_IS_ESRF() || Constants.SITE_IS_ESRF()){
			try{
				Ejb3ServiceLocator localejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
				Proposal3Service proposalService = (Proposal3Service) localejb3ServiceLocator.getLocalService(Proposal3Service.class);
				Proposal3VO proposalVO = proposalService.findByPk(proposalId);
				if (proposalVO != null ){
					String proposalCode =  proposalVO.getCode().toLowerCase();
					for (int i=0; i<Constants.PROPOSAL_CAN_CREATE_PROTEIN.length; i++){
						if (proposalCode.equals(Constants.PROPOSAL_CAN_CREATE_PROTEIN[i])){
							return true;
						}
					}
					// allows also mx415
					if (proposalCode.equalsIgnoreCase("mx") && proposalVO.getNumber().equalsIgnoreCase("415")){
						return true;
					}
					return false;
				}
				return Constants.ALLOWED_TO_CREATE_PROTEINS;
			}catch (Exception e){
				e.printStackTrace();
				return Constants.ALLOWED_TO_CREATE_PROTEINS;
			}
		}else
			return Constants.ALLOWED_TO_CREATE_PROTEINS;
	}

	/**
	 * checks if the crystal in the form are correct and well formated, returns false if at least one field is incorrect
	 * 
	 * @param form
	 * @return
	 */
	private boolean checkCrystalInformations(ViewCrystalForm form, HttpServletRequest request) {
		ActionMessages l_ActionMessages = new ActionMessages();
		boolean isOk = true;
		// morphology
		if (form.getInfo().getMorphology() != null
				&& form.getInfo().getMorphology().length() > DBConstants.MAX_LENGTH_CRYSTAL_MORPHOLOGY) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Morphology",
					DBConstants.MAX_LENGTH_CRYSTAL_MORPHOLOGY);
			l_ActionMessages.add("info.morphology", l_ActionMessageLabel);
		}
		// color
		if (form.getInfo().getColor() != null
				&& form.getInfo().getColor().length() > DBConstants.MAX_LENGTH_CRYSTAL_COLOR) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Color",
					DBConstants.MAX_LENGTH_CRYSTAL_COLOR);
			l_ActionMessages.add("info.color", l_ActionMessageLabel);
		}
		// comments
		if (form.getInfo().getComments() != null
				&& form.getInfo().getComments().length() > DBConstants.MAX_LENGTH_CRYSTAL_COMMENTS) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Comments",
					DBConstants.MAX_LENGTH_CRYSTAL_COMMENTS);
			l_ActionMessages.add("info.comments", l_ActionMessageLabel);
		}
		if (!isOk) {
			request.setAttribute("org.apache.struts.action.ERROR", l_ActionMessages);
		}
		return isOk;
	}

}
