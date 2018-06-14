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
import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.services.sample.Crystal3Service;
import ispyb.server.mx.services.sample.DiffractionPlan3Service;
import ispyb.server.mx.services.sample.ExperimentKindDetails3Service;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.DiffractionPlan3VO;
import ispyb.server.mx.vos.sample.ExperimentKindDetails3VO;

import java.util.ArrayList;
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
 * @struts.action name="viewSampleForm" path="/user/editSampleAction" type="ispyb.client.mx.sample.EditSampleAction"
 *                input="user.sample.create.page" validate="false" parameter="reqCode" scope="request"
 * 
 * 
 * @struts.action-forward name="sampleCreatePage" path="user.sample.create.page"
 * @struts.action-forward name="managerSampleCreatePage" path="manager.sample.create.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 * 
 */
public class EditSampleAction extends AbstractSampleAction {
	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private BLSample3Service sampleService;

	private Crystal3Service crystalService;

	private DiffractionPlan3Service difPlanService;

	private ExperimentKindDetails3Service experimentKindService;

	private final static Logger LOG = Logger.getLogger(EditSampleAction.class);

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws CreateException, NamingException {

		this.sampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
		this.crystalService = (Crystal3Service) ejb3ServiceLocator.getLocalService(Crystal3Service.class);
		this.difPlanService = (DiffractionPlan3Service) ejb3ServiceLocator
				.getLocalService(DiffractionPlan3Service.class);
		this.experimentKindService = (ExperimentKindDetails3Service) ejb3ServiceLocator
				.getLocalService(ExperimentKindDetails3Service.class);

	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	/**
	 * Edit the sample form
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward editSample(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

		LOG.info("originator for editSample: " + BreadCrumbsForm.getIt(request).getFromPage());

		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			Integer blsampleId;
			ViewSampleForm form = (ViewSampleForm) actForm;

			if (request.getParameter(Constants.BLSAMPLE_ID) != null)
				blsampleId = new Integer(request.getParameter(Constants.BLSAMPLE_ID));
			else
				blsampleId = BreadCrumbsForm.getIt(request).getSelectedSample().getBlSampleId();
			// ---------------------------------------------------------------------------------------------------
			BLSample3VO slv = sampleService.findByPk(blsampleId, false, false, false);

			form.setEditMode(new Integer(1));

			// ---------- Populate diffraction plan info ----------
			Integer diffPlanId = slv.getDiffractionPlanVOId();
			if (diffPlanId != null) {
				DiffractionPlan3VO difPlanInfo = difPlanService.findByPk(diffPlanId, false, false);

				BLSample3VO selectedSample = sampleService.findByPk(blsampleId, false, false, false);
				Crystal3VO selectedCrystal = selectedSample.getCrystalVO();
				Integer crystalId = selectedCrystal.getCrystalId();

				// ---------- No XML Diffraction plan try to get one from the Crystal -------------------
				if (difPlanInfo.getDiffractionPlanId() == null ) {
					if (selectedCrystal.getDiffractionPlanVO() != null) {
						DiffractionPlan3VO foundDiffPlan = difPlanService.findByPk(
								selectedCrystal.getDiffractionPlanVOId(), false, false);
						if (difPlanInfo.getDiffractionPlanId() == null)
							difPlanInfo = foundDiffPlan;
					}
				}

				// ----- Populate ExperimentKindDetail -----
				// Get ExperimentKindDetails from diffInfo First
				List<ExperimentKindDetails3VO> listDetail = new ArrayList<ExperimentKindDetails3VO>(
						difPlanInfo.getExperimentKindVOs());
				int nbDetails = listDetail.size();
				if (nbDetails > Constants.NB_MAX_EXPERIMENT_KIND_DETAILS) {
					nbDetails = Constants.NB_MAX_EXPERIMENT_KIND_DETAILS;
				}
				int nbBlankDetails = Constants.NB_MAX_EXPERIMENT_KIND_DETAILS - nbDetails;
				for (int i = 0; i < nbDetails; i++) {
					form.getListExperimentKindDetails().add(listDetail.get(i));
				}
				// Fill in Blank Details
				for (int i = 0; i < nbBlankDetails; i++) {
					ExperimentKindDetails3VO detail = new ExperimentKindDetails3VO();
					form.getListExperimentKindDetails().add(detail);
				}
				form.setExperimentKindDetail_1(form.getListExperimentKindDetails().get(0));
				form.setExperimentKindDetail_2(form.getListExperimentKindDetails().get(1));
				form.setExperimentKindDetail_3(form.getListExperimentKindDetails().get(2));
				form.setNumberOfWaveLength(new Integer(nbDetails));
				// ---------------------------------- Get list of Samples --------------------------------------------
				LOG.debug("calculate the list of samples for a crystal");
				List<BLSample3VO> mList = new ArrayList<BLSample3VO>();

				mList = sampleService.findByCrystalId(crystalId);

				// Populate form in case of existing diffraction plan
				form.setListInfo(mList);
				// use a difPlan Info light to use number formatter
				form.setDifPlanInfo(difPlanInfo);

			} else // No DiffractionPlan
			{
				// Fill in Blank Details
				int nbDetails = 3;
				for (int i = 0; i < nbDetails; i++) {
					ExperimentKindDetails3VO detail = new ExperimentKindDetails3VO();
					form.getListExperimentKindDetails().add(detail);
				}
				form.setExperimentKindDetail_1(form.getListExperimentKindDetails().get(0));
				form.setExperimentKindDetail_2(form.getListExperimentKindDetails().get(1));
				form.setExperimentKindDetail_3(form.getListExperimentKindDetails().get(2));
				form.setNumberOfWaveLength(new Integer(0));
			}

			// Populate Form
			form.setInfo(slv);
			form.setTheCrystalId(slv.getCrystalVO().getCrystalId());

			// Populate list of Crystals + Proteins
			List<Crystal3VO> crystalInfoList = populateCrystalInfoList(proposalId);
			form.setListCrystal(crystalInfoList);

			// Populate DM Codes in SC
			this.populateDMCodesinSC(form, request);

			// -----------------------------------------

			BreadCrumbsForm.getIt(request).setSelectedSample(slv);
			FormUtils.setFormDisplayMode(request, actForm, FormUtils.EDIT_MODE);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}
		// redirect to the role view page
		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();
		if (role.equals(Constants.ROLE_MANAGER)) {
			return mapping.findForward("managerSampleCreatePage");
		}else
			return (mapping.findForward("sampleCreatePage"));
	}

	/**
	 * Update sample parameters
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward save(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages messages = new ActionMessages();
		ActionMessages errors = new ActionMessages();

		// Get the caller page
		String originator = BreadCrumbsForm.getIt(request).getFromPage();
		LOG.info("originator page for update: " + originator);

		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			ViewSampleForm form = (ViewSampleForm) actForm;
			form.setEditMode(new Integer(1));

			Integer sampleId = form.getInfo().getBlSampleId();
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			if (sampleId == null)
				sampleId = BreadCrumbsForm.getIt(request).getSelectedSample().getBlSampleId();

			BLSample3VO updatedSample = form.getInfo();

			// retrieve the object, change the fields and save it again

			BLSample3VO slvFromDB = sampleService.findByPk(sampleId, false, false, false);
			Crystal3VO crystalFromDB = crystalService.findByPk(form.getTheCrystalId(), false);
			slvFromDB.setCrystalVO(crystalFromDB);
			slvFromDB.setName(updatedSample.getName());
			slvFromDB.setCode(updatedSample.getCode());
			slvFromDB.setLocation(updatedSample.getLocation());
			slvFromDB.setHolderLength(updatedSample.getHolderLength());
			slvFromDB.setLoopType(updatedSample.getLoopType());
			slvFromDB.setComments(updatedSample.getComments());
			slvFromDB.setBlSampleStatus(updatedSample.getBlSampleStatus());
			slvFromDB.setIsInSampleChanger(updatedSample.getIsInSampleChanger());

			// Check Required Fields are populated with correct values
			this.mReference_SampleId = slvFromDB.getBlSampleId();
			if (!this.CheckFormFields(form, errors, request, false, true, true, true, true, true)) {
				LOG.debug("crystalId not selected, or sample name not selected or sample name already exists in form");
				saveErrors(request, errors);

				// Populate list of Crystals + Proteins
				List<Crystal3VO> crystalInfoList = populateCrystalInfoList(proposalId);
				form.setListCrystal(crystalInfoList);

				// Populate DM Codes in SC
				this.populateDMCodesinSC(form, request);

				// Test if display is called from a page where a crystal has been selected
				if (form.getTheCrystalId() != null && form.getTheCrystalId().intValue() > 0) {
					Integer crystalId;
					crystalId = form.getTheCrystalId();
					form.setTheCrystalId(crystalId);

					List<BLSample3VO> mList = new ArrayList<BLSample3VO>();
					mList = sampleService.findByCrystalId(crystalId);
					// Populate form in case of exisiting diffraction plan
					form.setListInfo(mList);

				}

				return (mapping.findForward("sampleCreatePage"));
			} else if (!this.checkSampleInformations(form, errors, request)) {
				saveErrors(request, errors);
				// Populate list of Crystals + Proteins
				List<Crystal3VO> crystalInfoList = populateCrystalInfoList(proposalId);
				form.setListCrystal(crystalInfoList);

				// Populate DM Codes in SC
				this.populateDMCodesinSC(form, request);

				// Test if display is called from a page where a crystal has been selected
				if (form.getTheCrystalId() != null && form.getTheCrystalId().intValue() > 0) {
					Integer crystalId;
					crystalId = form.getTheCrystalId();
					form.setTheCrystalId(crystalId);

					List<BLSample3VO> mList = new ArrayList<BLSample3VO>();
					mList = sampleService.findByCrystalId(crystalId);
					// Populate form in case of exisiting diffraction plan
					form.setListInfo(mList);

				}

				return (mapping.findForward("sampleCreatePage"));
			} else {
				sampleService.update(slvFromDB);

				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.inserted", "Sample parameters"));

				// ---------- Populate diffraction plan info ----------
				Integer diffPlanId = slvFromDB.getDiffractionPlanVOId();
				DiffractionPlan3VO newDiffractionPlan;

				if (diffPlanId != null && diffPlanId.intValue() != 0) {
					newDiffractionPlan = form.getDifPlanInfo();
					// --- Keep old things ---
					DiffractionPlan3VO oldDiffractionPlan = difPlanService.findByPk(form.getDifPlanInfo()
							.getDiffractionPlanId(), false, false);

					// --- Update ---
					difPlanService.update(newDiffractionPlan);

					form.setDifPlanInfo(newDiffractionPlan);
				} else
				// ------------------------------- Insert ---------------------------------------------
				{
					DiffractionPlan3VO newDifPlan = new DiffractionPlan3VO();
					newDifPlan = form.getDifPlanInfo();
					newDifPlan.setDiffractionPlanId(null);
					newDiffractionPlan = difPlanService.create(newDifPlan);
					form.setDifPlanInfo(newDiffractionPlan);

					if (sampleId.intValue() > 0) { // Sample
						BLSample3VO selectedSample = sampleService.findByPk(sampleId, false, false, false);
						selectedSample.setDiffractionPlanVO(newDiffractionPlan);
						sampleService.update(selectedSample);

					}

				}

				// --- Attach Other Samples to the Diffraction Plan ---
				this.assignDiffractionPlanToSamples(newDiffractionPlan, form.getSelectedSamplesList());

				// ---------------------------------------------------------------------------------------------------------------
				// ----------------------------- Attach ExperimentKindDetails to Diffraction Plan
				// -------------------------------
				// ---------------------------------------------------------------------------------------------------------------
				Integer _nbExperimentKindDetails = form.getNumberOfWaveLength();
				int nbExperimentKindDetails = 0;
				try {
					nbExperimentKindDetails = _nbExperimentKindDetails.intValue();
				} catch (Exception e) {
				}

				// --- To make sure correct number of details present, delete "old" ones ---
				if (diffPlanId != null) {
					DiffractionPlan3VO oldDifPlanInfo = difPlanService.findByPk(diffPlanId, false, false);
					List<ExperimentKindDetails3VO> expKind = new ArrayList<ExperimentKindDetails3VO>(
							oldDifPlanInfo.getExperimentKindVOs());
					for (int i = 0; i < expKind.size(); i++) {
						ExperimentKindDetails3VO ed = expKind.get(i);
						experimentKindService.delete(ed);
					}
				}

				// --- Assign new ExperimentKindDetails ---
				ArrayList<ExperimentKindDetails3VO> listNewExperimentKind = new ArrayList<ExperimentKindDetails3VO>();
				listNewExperimentKind.add(form.getExperimentKindDetail_1());
				listNewExperimentKind.add(form.getExperimentKindDetail_2());
				listNewExperimentKind.add(form.getExperimentKindDetail_3());
				for (int i = 0; i < nbExperimentKindDetails; i++) {
					ExperimentKindDetails3VO ed = listNewExperimentKind.get(i);
					ed.setExposureIndex(new Integer(i + 1));
					ed.setDiffractionPlanId(newDiffractionPlan.getDiffractionPlanId());
					experimentKindService.create(ed);
				}

				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.inserted",
						"Diffraction plan & Experiment Details"));

				form.setListInfo(new ArrayList<BLSample3VO>()); // to clean list and force recalculation
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

		return null;

	}

	/**
	 * Assign the DiffractionPlan to a list of Samples
	 * 
	 * @param sampleList
	 */
	private void assignDiffractionPlanToSamples(DiffractionPlan3VO diffractionPlan, ArrayList<String> sampleList) {
		String _sampleId;
		Integer sampleId;
		try {

			for (int i = 0; i < sampleList.size(); i++) // Browse through Samples
			{
				_sampleId = sampleList.get(i);

				if (!_sampleId.equals("")) {
					sampleId = new Integer(_sampleId);
					BLSample3VO selectedSample = sampleService.findByPk(sampleId, false, false, false);
					if (selectedSample != null) {
						selectedSample.setDiffractionPlanVO(diffractionPlan);
						sampleService.update(selectedSample);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
