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
import ispyb.common.util.Constants;
import ispyb.server.common.services.shipping.Container3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.services.sample.Crystal3Service;
import ispyb.server.mx.services.sample.DiffractionPlan3Service;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.DiffractionPlan3VO;

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
 * @struts.action name="viewSampleForm" path="/user/createSampleAction" type="ispyb.client.mx.sample.CreateSampleAction"
 *                input="user.sample.create.page" validate="false" parameter="reqCode" scope="request"
 * 
 * 
 * @struts.action-forward name="sampleCreatePage" path="user.sample.create.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 */
public class CreateSampleAction extends AbstractSampleAction

{
	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private BLSample3Service sampleService;

	private Crystal3Service crystalService;

	private Container3Service containerService;

	private DiffractionPlan3Service difPlanService;

	private final static Logger LOG = Logger.getLogger(CreateSampleAction.class);

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws CreateException, NamingException {

		this.sampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
		this.crystalService = (Crystal3Service) ejb3ServiceLocator.getLocalService(Crystal3Service.class);
		this.containerService = (Container3Service) ejb3ServiceLocator.getLocalService(Container3Service.class);
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
		// Retrieve Attributes

		LOG.debug("create sample : display from menu");
		BreadCrumbsForm.getItClean(request);

		// Save Originator for return path
		BreadCrumbsForm.getIt(request).setFromPage(request.getContextPath() + Constants.PAGE_SAMPLE_CREATE_MENU);

		return this.display(mapping, actForm, request, response);
	}

	/**
	 * Displays the sample
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		String logPrefix = new String("create sample");
		String fromPage = new String(Constants.PAGE_SAMPLE_CREATE_CONTAINER);
		ActionMessages errors = new ActionMessages();
		String success = new String("sampleCreatePage");
		;
		Integer containerId = null;
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
		Integer crystalId;

		String originator = BreadCrumbsForm.getIt(request).getFromPage();
		LOG.debug(logPrefix + " : display : originator = " + originator);

		try {
			
			// Reset the boolean editMode to false (used in the jsp page)
			ViewSampleForm form = (ViewSampleForm) actForm;
			form.setEditMode(new Integer(0));

			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes

			// Retrieve containerId
			String _containerId = request.getParameter(Constants.CONTAINER_ID);
			if (_containerId != null) // Request parameter
				containerId = new Integer(_containerId);
			else if (BreadCrumbsForm.getIt(request).getSelectedContainer() != null) // BreadCrumbForm
				containerId = BreadCrumbsForm.getIt(request).getSelectedContainer().getContainerId();

			if (containerId != null) {
				form.setTheContainerId(containerId);
				BreadCrumbsForm.getIt(request).setSelectedContainerId(containerId);

				// Save Originator for return path
				originator = request.getContextPath() + fromPage + containerId.toString();
				BreadCrumbsForm.getIt(request).setFromPage(originator);

				LOG.debug(logPrefix + " for container : display : originator = " + originator);
			}

			// Test if display is called from a page where a crystal has been selected
			if (form.getTheCrystalId() != null && form.getTheCrystalId().intValue() > 0) {
				crystalId = form.getTheCrystalId();
				BreadCrumbsForm.getIt(request).setSelectedCrystalId(crystalId);
			}

			if (BreadCrumbsForm.getIt(request).getSelectedCrystalId() != null) {
				crystalId = BreadCrumbsForm.getIt(request).getSelectedCrystalId();
				form.setTheCrystalId(crystalId);
			}

			// populate list of Crystals + Proteins
			List<Crystal3VO> crystalInfoList = populateCrystalInfoList(proposalId);
			form.setListCrystal(crystalInfoList);

			// populate sample lists, form and breadCrumbsForm
			populateSampleListsAndBreadCrumbs(form, request);

			// Populate DM Codes in SC
			populateDMCodesinSC(form, request);
			
			boolean isAllowedToCreateProtein = CreateCrystalAction.CheckIsAllowedToCreateProtein(proposalId);
			form.setIsAllowedToCreateProtein(isAllowedToCreateProtein);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.CREATE_MODE);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
			saveErrors(request, errors);
			return mapping.findForward("error");
		}

		return mapping.findForward(success);

	}

	/**
	 * Save the data and go back to the page defined by the "originator"
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward save(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();
		String success = new String("sampleCreatePage");

		String originator = BreadCrumbsForm.getIt(request).getFromPage();
		LOG.info("==> originator page for save: " + originator);

		ActionMessages messages = new ActionMessages();
		ViewSampleForm form = (ViewSampleForm) actForm;
		form.setEditMode(new Integer(0));
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

		BLSample3VO info = form.getInfo();

		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			// --- Retrieve containerId ---
			Integer containerId = form.getTheContainerId();
			Integer blSampleId = form.getSelectedSampleId();
			Integer crystalId = form.getTheCrystalId();

			if (containerId == null && BreadCrumbsForm.getIt(request).getSelectedContainer() != null)
				containerId = BreadCrumbsForm.getIt(request).getSelectedContainer().getContainerId();

			if (containerId != null) {
				Container3VO container = containerService.findByPk(containerId, false);
				info.setContainerVO(container);
				form.setTheContainerId(containerId);

				// Save Originator for return path
				originator = request.getContextPath() + Constants.PAGE_SAMPLE_CREATE_CONTAINER + containerId.toString();

			}

			// Populate DM Codes in SC
			populateDMCodesinSC(form, request);

			LOG.debug("create sample : check fields");

			// Check Required Fields are populated with correct values

			boolean sampleExist = (blSampleId != null) ? true : false;
			boolean crystalId_selected = !sampleExist;
			boolean name_entered = !sampleExist;
			boolean name_does_not_exist = true;
			boolean container_capacity = true;
			boolean dm_code_does_not_exist = (containerId != null) ? true : false;
			boolean container_location_unique = true;

			if (!this.CheckFormFields(form, errors, request, crystalId_selected, name_entered, name_does_not_exist,
					container_capacity, dm_code_does_not_exist, container_location_unique)) {
				LOG.debug("crystalId not selected, or sample name not selected or sample name already exists in form");
				saveErrors(request, errors);
			} else if (!this.checkSampleInformations(form, errors, request)) {
				saveErrors(request, errors);
			} else {
				// ----- Update or Create Sample -----

				// Assign already existing Sample
				if (blSampleId != null) {
					LOG.debug("create sample : assign already existing sample with sampleId = " + blSampleId);
					info = sampleService.findByPk(blSampleId, false, false, false);
					Container3VO clv = containerService.findByPk(containerId, false);
					info.setContainerVO(clv);
					sampleService.update(info);
				}
				// Create new Sample
				else {
					Crystal3VO clv = crystalService.findByPk(crystalId, false);
					info.setCrystalVO(clv);

					info = sampleService.create(info);
					LOG.debug("create sample : create new sample done ...");

					// Retrieve diffractionPlan linked to crystalType to create and
					// attach a diffractionPlan with same parameters to blsample
					if (clv.getDiffractionPlanVO() != null) {
						LOG.debug("create sample : diffraction plan attached saved in info ...");
						DiffractionPlan3VO dplv = difPlanService.findByPk(clv.getDiffractionPlanVOId(), false, false);
						info.setDiffractionPlanVO(dplv);
						sampleService.update(info);
					}

				}

				// Acknowledge action
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.inserted", info.getName()));
				saveMessages(request, messages);

				// ----- Return to Originator -----
				if (originator != null) {
					BreadCrumbsForm.getIt(request).setFromPage(null);
					return new ActionForward(originator.replaceFirst(request.getContextPath(), ""), false);
				}
				// ---------------------------------

			}
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			saveErrors(request, errors);
			e.printStackTrace();
			return mapping.findForward("error");
		}

		// Populate lists for Display
		// Populate list if crystal = protein
		List<Crystal3VO> crystalInfoList = populateCrystalInfoList(proposalId);
		form.setListCrystal(crystalInfoList);

		// populate sample lists, form and breadCrumbsForm
		populateSampleListsAndBreadCrumbs(form, request);

		return mapping.findForward(success);
	}

	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward saveExisting(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		String success = new String("sampleCreatePage");
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		ViewSampleForm form = (ViewSampleForm) actForm;
		form.setEditMode(new Integer(0));
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

		LOG.debug("create sample : save existing");

		try {
			List<String> sampleList = form.getSelectedSamplesList();
			Integer containerId = form.getTheContainerId();
			Integer blSampleId;
			String sampleIdstr;

			if (containerId == null && BreadCrumbsForm.getIt(request).getSelectedContainer() != null) {
				containerId = BreadCrumbsForm.getIt(request).getSelectedContainer().getContainerId();
				form.setTheContainerId(containerId);
			}

			for (int i = 0; i < sampleList.size(); i++) // Browse through selected Samples
			{
				sampleIdstr = sampleList.get(i);
				if (!sampleIdstr.equals("")) {

					blSampleId = new Integer(sampleIdstr);
					LOG.debug("create sample : assign already existing sample with sampleId =" + blSampleId);
					BLSample3VO info = sampleService.findByPk(blSampleId, false, false, false);
					// set the location to null not to have several samples at the same location
					// later edit the sample to give a correct location to it
					info.setLocation(null);
					Container3VO clv = containerService.findByPk(containerId, false);
					info.setContainerVO(clv);

					if (!this.CheckFormFields(form, errors, request, false, false, false, true, false, false)) {
						LOG.debug("container capacity exceeded");
						saveErrors(request, errors);
						break;
					} else {
						sampleService.update(info);
						// Acknowledge action
						messages.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage("message.inserted", info.getName()));
						saveMessages(request, messages);
					}

				}

			}

			LOG.debug("errors : " + errors);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			saveErrors(request, errors);
			e.printStackTrace();
			return mapping.findForward("error");

		}

		// Populate lists for Display
		List<Crystal3VO> crystalInfoList = populateCrystalInfoList(proposalId);
		form.setListCrystal(crystalInfoList);

		// populate sample lists, form and breadCrumbsForm
		populateSampleListsAndBreadCrumbs(form, request);

		return mapping.findForward(success);
	}

}
