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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
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

import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.util.DBConstants;
import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.common.services.shipping.Container3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.services.collections.EnergyScan3Service;
import ispyb.server.mx.services.collections.XFEFluorescenceSpectrum3Service;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.services.sample.Crystal3Service;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.EnergyScan3VO;
import ispyb.server.mx.vos.collections.XFEFluorescenceSpectrum3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;

/*
 * Abstract class AbstractSampleAction
 * the classes EditSampleAction 
 * and CreateSampleAction 
 * and EditDiffrationPlanAction inherit from.
 * To factorize common functions
 * 
 */

public abstract class AbstractSampleAction extends DispatchAction {

	protected Integer mReference_SampleId = null;

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private BLSample3Service sampleService;

	private Crystal3Service crystalService;
	
	private XFEFluorescenceSpectrum3Service xrfSpectraService;
	
	private EnergyScan3Service energyScanService ;

	private final static Logger LOG = Logger.getLogger(AbstractSampleAction.class);

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws CreateException, NamingException {

		this.sampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
		this.crystalService = (Crystal3Service) ejb3ServiceLocator.getLocalService(Crystal3Service.class);
		this.xrfSpectraService = (XFEFluorescenceSpectrum3Service) ejb3ServiceLocator.getLocalService(XFEFluorescenceSpectrum3Service.class);
		this.energyScanService = (EnergyScan3Service) ejb3ServiceLocator.getLocalService(EnergyScan3Service.class);

	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	/**
	 * CheckFormFields Checks that Required Fields are there and correctly formated.
	 * 
	 * @param form
	 *            The form to get Fields from.
	 * 
	 */
	protected boolean CheckFormFields(ViewSampleForm form, ActionMessages l_ActionMessages, HttpServletRequest request,
			boolean crystalId_selected, boolean name_entered, boolean name_does_not_exist, boolean container_capacity,
			boolean dm_code_does_not_exist, boolean container_location_unique) throws Exception {

		boolean formFieldsOK = true;
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
		BLSample3VO selSample = BreadCrumbsForm.getIt(request).getSelectedSample();
		if (selSample != null)
			mReference_SampleId = selSample.getBlSampleId();

		if (crystalId_selected)
			// ----------------------------------------------------------------------- CrystalId has been selected -----
			if (form.getTheCrystalId() == null || form.getTheCrystalId().intValue() == 0) {
				formFieldsOK = false;
				ActionMessage l_ActionMessage = new ActionMessage("errors.required", "Acronym + space group");
				l_ActionMessages.add("theCrystalId", l_ActionMessage);
				form.setTheCrystalId(null);
			}

		if (name_entered)
			// ----------------------------------------------------------------------------- Name has been entered -----
			if (form.getInfo().getName() == null || form.getInfo().getName().length() == 0) {
				formFieldsOK = false;
				ActionMessage l_ActionMessageName = new ActionMessage("errors.required", "Sample Name");
				l_ActionMessages.add("info.name", l_ActionMessageName);
			} else if (!form.getInfo().getName().matches(Constants.MASK_BASIC_CHARACTERS_WITH_DASH_UNDERSCORE_NO_SPACE)) {
				formFieldsOK = false;
				ActionMessage l_ActionMessageName = new ActionMessage("errors.format", "Sample Name");
				l_ActionMessages.add("info.name", l_ActionMessageName);
			}
		// ------------------------------------------------------------------Acronym + Name already exists -----
		if (name_does_not_exist && form.getTheCrystalId() != null && form.getTheCrystalId() != 0) {
			Integer proteinId = crystalService.findByPk(form.getTheCrystalId(), false).getProteinVOId();
			boolean errorDetected = false;
			Integer shippingId = null;
			if (BreadCrumbsForm.getIt(request).getSelectedShipping() != null)
				shippingId = BreadCrumbsForm.getIt(request).getSelectedShipping().getShippingId();
			List<BLSample3VO> samplesWithSameName = sampleService.findByNameAndProteinId(form.getInfo().getName(),
					proteinId, shippingId);

			if (!samplesWithSameName.isEmpty()) {
				for (int s = 0; s < samplesWithSameName.size(); s++) {
					BLSample3VO currentSample;
					currentSample = samplesWithSameName.get(s);
					if (!currentSample.getBlSampleId().equals(mReference_SampleId)) {
						errorDetected = true;
						formFieldsOK = false;
					}
				}

				if (errorDetected) {
					ActionMessage l_ActionMessageName = new ActionMessage("errors.alreadyExist",
							"[acronym + sample name]");
					l_ActionMessages.add("info.name", l_ActionMessageName);
				}
			}
		}

		if (container_capacity)
			// ---------------------------------------------------------------------- Container capacity exceeeded -----
			if (BreadCrumbsForm.getIt(request).getSelectedContainer() != null) {
				LOG.debug("check capacity start ");
				Container3VO selectedContainer = BreadCrumbsForm.getIt(request).getSelectedContainer();
				List<BLSample3VO> samplesInContainer = populateSampleListByContainer(selectedContainer.getContainerId());

				if (selectedContainer.getCapacity().intValue() < samplesInContainer.size() + 1) {
					LOG.debug("errors.containerCapacityExceeded");
					formFieldsOK = false;
					ActionMessage l_ActionMessageName = new ActionMessage("errors.containerCapacityExceeded",
							selectedContainer.getCapacity());
					l_ActionMessages.add("", l_ActionMessageName);
				}

			}
		if (dm_code_does_not_exist && form.getInfo().getCode() != null && form.getInfo().getCode().length() > 0) {
			List<BLSample3VO> samplesWithSameDM;
			String dmCode = form.getInfo().getCode();
			Integer shippingId = null;
			boolean errorDetected = false;

			if (BreadCrumbsForm.getIt(request).getSelectedShipping() != null)
				shippingId = BreadCrumbsForm.getIt(request).getSelectedShipping().getShippingId();

			if (shippingId != null) {
				samplesWithSameDM = sampleService.findByCodeAndShippingId(dmCode, shippingId);
			} else {
				samplesWithSameDM = sampleService.findByCodeAndProposalId(dmCode, proposalId);
			}

			if (dmCode.length() != 0 && !samplesWithSameDM.isEmpty()) {
				for (int s = 0; s < samplesWithSameDM.size(); s++) {
					BLSample3VO currentSample;
					currentSample = samplesWithSameDM.get(s);
					if (!currentSample.getBlSampleId().equals(mReference_SampleId)) {
						formFieldsOK = false;
						errorDetected = true;
						break;
					}
				}

				if (errorDetected) {
					ActionMessage l_ActionMessageName = new ActionMessage("errors.alreadyExist", "DataMatrix Code");
					l_ActionMessages.add("info.code", l_ActionMessageName);
				}
			}
			if (!form.getInfo().getCode().matches(Constants.MASK_BASIC_CHARACTERS_NO_SPACE)) {
				formFieldsOK = false;
				ActionMessage l_ActionMessageName = new ActionMessage("errors.format", "DataMatrix Code");
				l_ActionMessages.add("info.code", l_ActionMessageName);
			}

		}
		if (container_location_unique)
		// ----------------------- Location in Container must be unique within a container ------------------------
		{
			String referenceLocation = form.getInfo().getLocation();
			if (BreadCrumbsForm.getIt(request).getSelectedContainer() != null
					&& !referenceLocation.trim().equalsIgnoreCase("")) {
				Integer containerId = BreadCrumbsForm.getIt(request).getSelectedContainer().getContainerId();
				BLSample3VO selectedSample = BreadCrumbsForm.getIt(request).getSelectedSample();

				List<BLSample3VO> samplesInSameContainer;

				samplesInSameContainer = sampleService.findByContainerId(containerId);
				for (int s = 0; s < samplesInSameContainer.size(); s++) {
					BLSample3VO currentSample;
					currentSample = samplesInSameContainer.get(s);
					boolean currentSampleIsSelectedSample = false;
					if (selectedSample != null) // Make sure not checking against Selected Sample
					{
						if (selectedSample.getBlSampleId().equals(currentSample.getBlSampleId())) {
							currentSampleIsSelectedSample = true;
						}
					}

					if (currentSample.getLocation() != null)
						if (currentSample.getLocation().compareToIgnoreCase(referenceLocation) == 0
								&& !currentSampleIsSelectedSample) {
							formFieldsOK = false;
							ActionMessage l_ActionMessageName = new ActionMessage("error.user.sample.location",
									"Location");
							l_ActionMessages.add("info.location", l_ActionMessageName);
							break;
						}
				}
			}

			Container3VO container = BreadCrumbsForm.getIt(request).getSelectedContainer();
			if (container != null && container.getCapacity() != null) {
				int containerCapacity = container.getCapacity();
				if (referenceLocation != null && !referenceLocation.equals("")) {
					if (Integer.parseInt(referenceLocation) > containerCapacity
							|| Integer.parseInt(referenceLocation) <= 0) {
						formFieldsOK = false;
						ActionMessage l_ActionMessageName = new ActionMessage("error.user.sample.locationrange",
								"Location");
						l_ActionMessages.add("info.location", l_ActionMessageName);
					}
				}
			}

		}

		return formFieldsOK;
	}

	/**
	 * checks if the lab contact in the form are correct and well formated, returns false if at least one field is
	 * incorrect
	 * 
	 * @param form
	 * @return
	 */
	protected boolean checkSampleInformations(ViewSampleForm form, ActionMessages l_ActionMessages,
			HttpServletRequest request) {
		boolean isOk = true;
		// card name
		if (form.getInfo().getName() != null && form.getInfo().getName().length() > DBConstants.MAX_LENGTH_SAMPLE_NAME) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Sample name",
					DBConstants.MAX_LENGTH_SAMPLE_NAME);
			l_ActionMessages.add("info.name", l_ActionMessageLabel);
		}
		// datamatrix code
		if (form.getInfo().getCode() != null && form.getInfo().getCode().length() > DBConstants.MAX_LENGTH_SAMPLE_CODE) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Sample code",
					DBConstants.MAX_LENGTH_SAMPLE_CODE);
			l_ActionMessages.add("info.code", l_ActionMessageLabel);
		}
		// location in container
		if (form.getInfo().getLocation() != null
				&& form.getInfo().getLocation().length() > DBConstants.MAX_LENGTH_SAMPLE_LOCATION) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Location in container",
					DBConstants.MAX_LENGTH_SAMPLE_LOCATION);
			l_ActionMessages.add("info.location", l_ActionMessageLabel);
		}
		// comments
		if (form.getInfo().getComments() != null
				&& form.getInfo().getComments().length() > DBConstants.MAX_LENGTH_SAMPLE_COMMENTS) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Comments",
					DBConstants.MAX_LENGTH_SAMPLE_COMMENTS);
			l_ActionMessages.add("info.comments", l_ActionMessageLabel);
		}
		// sample status
		if (form.getInfo().getBlSampleStatus() != null
				&& form.getInfo().getBlSampleStatus().length() > DBConstants.MAX_LENGTH_SAMPLE_BLSAMPLESTATUS) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Sample status",
					DBConstants.MAX_LENGTH_SAMPLE_BLSAMPLESTATUS);
			l_ActionMessages.add("info.blSampleStatus", l_ActionMessageLabel);
		}
		// sample name
				if (form.getInfo().getName() != null
						&& !StringUtils.isStringOkForName(form.getInfo().getName())) {
					isOk = false;
					ActionMessage l_ActionMessageLabel = new ActionMessage("errors.format", "Sample name");
					l_ActionMessages.add("info.name", l_ActionMessageLabel);
				}
		// diffraction plan: observed resolution
		if (form.getDifPlanInfo() != null
				&& form.getDifPlanInfo().getAnomalousScatterer() != null
				&& form.getDifPlanInfo().getAnomalousScatterer().length() > DBConstants.MAX_LENGTH_DIFFRACTIONPLAN_ANOMALOUSSCATTERRER) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Anomalous Scatterer",
					DBConstants.MAX_LENGTH_DIFFRACTIONPLAN_ANOMALOUSSCATTERRER);
			l_ActionMessages.add("difPlanInfo.anomalousScatterer", l_ActionMessageLabel);
		}
		// diffraction plan: comments
		if (form.getDifPlanInfo() != null && form.getDifPlanInfo().getComments() != null
				&& form.getDifPlanInfo().getComments().length() > DBConstants.MAX_LENGTH_DIFFRACTIONPLAN_COMMENTS) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Comments",
					DBConstants.MAX_LENGTH_DIFFRACTIONPLAN_COMMENTS);
			l_ActionMessages.add("difPlanInfo.comments", l_ActionMessageLabel);
		}
		if (!isOk) {
			request.setAttribute("org.apache.struts.action.ERROR", l_ActionMessages);
		}
		return isOk;
	}

	/**
	 * Handles the Exception for the various dispatchAction methods
	 * 
	 * @param e
	 */
	protected ActionMessages HandlesException(Exception e) {
		ActionMessages mErrors = new ActionMessages();
		mErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.sample.viewDiffractionPlan"));
		mErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
		e.printStackTrace();
		return mErrors;
	}

	// **************************************** Lists
	// ********************************************************************

	/**
	 * Return the list of samples within a container
	 * 
	 * @param containerId
	 *            Id of the container to find its samples for
	 * @return ArrayList
	 */
	protected List<BLSample3VO> populateSampleListByContainer(Integer containerId) throws Exception {
		List<BLSample3VO> res = new ArrayList<BLSample3VO>();
		res = sampleService.findByContainerId(containerId);
		return res;
	}

	/**
	 * Return the list of crystalFulls for a proposal
	 * 
	 * @param proposalId
	 *            Id of the proposal to find the crystals for
	 * @return ArrayList
	 */
	protected List<Crystal3VO> populateCrystalInfoList(Integer proposalId) {
		List<Crystal3VO> res1 = new ArrayList<Crystal3VO>();
		List<Crystal3VO> res2 = new ArrayList<Crystal3VO>();
		try {
			res1 = crystalService.findByProposalId(proposalId);

			// Eliminate duplicates-- problem: we can lose the link between sample & crystal
			for (int i = 0; i < res1.size(); i++) {
				Crystal3VO currentCrystal1 = res1.get(i);
				// Look for currentCrystal1 in res2 list
				boolean found = false;
				int j = 0;
				while (j < res2.size() && !found) {
					Crystal3VO currentCrystal2 = res2.get(j);
					if (currentCrystal1.getDesignation().equals(currentCrystal2.getDesignation()))
						found = true;
					j++;
				}
				if (!found)
					res2.add(currentCrystal1);
			}
		} catch (Exception e) {
		}

		return res2;
	}

	/**
	 * Return the list of unassigned samples
	 * 
	 * @param proposalId
	 *            Id of the proposal to find the samples for
	 * @return ArrayList
	 */
	protected List<BLSample3VO> populateSampleListFree(Integer proposalId) {
		List<BLSample3VO> res = new ArrayList<BLSample3VO>();
		try {
			res = sampleService.findByProposalIdAndDewarNull(proposalId);
		} catch (Exception e) {
		}
		return res;
	}

	/**
	 * build Lists used to display the already free created samples and the samples already created in the container if
	 * there is one
	 * 
	 * saves the created lists in the form
	 * 
	 * @param form
	 * @param request
	 * @return ArrayList
	 */
	protected void populateSampleListsAndBreadCrumbs(ViewSampleForm form, HttpServletRequest request) {
		List<BLSample3VO> sampleList = new ArrayList<BLSample3VO>();

		try {
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

			// fill the sampleFreeList
			List<BLSample3VO> sampleFreeList = populateSampleListFree(proposalId);
			form.setFreeSampleList(sampleFreeList);

			Integer crystalId = form.getTheCrystalId();
			BreadCrumbsForm.getIt(request).setSelectedCrystalId(crystalId);
			Integer containerId = form.getTheContainerId();

			if (containerId != null && containerId.intValue() > 0) {
				Container3Service containerService = (Container3Service) ejb3ServiceLocator
						.getLocalService(Container3Service.class);
				Container3VO clv = containerService.findByPk(containerId, false);

				BreadCrumbsForm.getIt(request).setSelectedContainer(clv);
				BreadCrumbsForm.getIt(request).setSelectedContainerId(containerId);

				sampleList = populateSampleListByContainer(containerId);

			} else {

				sampleList = sampleFreeList;

			}

			// Look for data collection and snapshot files
			long startTime = System.currentTimeMillis();
			boolean[] hasSnapshot = new boolean[sampleList.size()];
			boolean[] hasDataCollection = new boolean[sampleList.size()];
			boolean[] hasDataCollectionGroup = new boolean[sampleList.size()];
			DataCollection3Service dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
					.getLocalService(DataCollection3Service.class);

			int i = 0;
			for (Iterator<BLSample3VO> mySamples = sampleList.iterator(); mySamples.hasNext();) {
				// Get sample
				BLSample3VO mySample = mySamples.next();
				List<DataCollection3VO> dataCollectionList = dataCollectionService.findBySample(
						mySample.getBlSampleId(), null, null, proposalId);
				// Browse dataCollections
				boolean snapShotFound = false;
				for (Iterator<DataCollection3VO> myDataCollections = dataCollectionList.iterator(); myDataCollections
						.hasNext();) {
					// Look for snapshot file
					DataCollection3VO myDataCollection = myDataCollections.next();
					String snapshotFullPath = myDataCollection.getXtalSnapshotFullPath1();
					if (snapshotFullPath != null && !snapshotFullPath.equals("")
							&& (new File(snapshotFullPath)).exists()) {
						snapShotFound = true;
						break;
					}
				}
				boolean dataCollectionFound = false;
				if (dataCollectionList.size() != 0) {
					dataCollectionFound = true;
				}
				
				List<XFEFluorescenceSpectrum3VO> listXrfSpectra = xrfSpectraService.findFiltered(null, mySample.getBlSampleId(), null);
				List<EnergyScan3VO> listEnergyScan = energyScanService.findFiltered(null, mySample.getBlSampleId());
				boolean energyScanFound = listEnergyScan != null && listEnergyScan.size() > 0;
				boolean xrfSpectraFound = listXrfSpectra != null && listXrfSpectra.size() > 0;
				
				
				// Save result
				hasDataCollection[i] = dataCollectionFound;
				hasDataCollectionGroup[i] = dataCollectionFound || energyScanFound || xrfSpectraFound;
				hasSnapshot[i] = snapShotFound;
				// Next sample
				i = i + 1;
			}
			// Trace computing time (debug)
			long deltatTime = System.currentTimeMillis() - startTime;
			if (i != 0)
				LOG.debug("Check DataCollection for " + i + " samples: " + deltatTime + " ms (" + deltatTime / i
						+ " ms/item) ");
			// Populate the form with Info
			form.setHasDataCollection(hasDataCollection);
			form.setHasDataCollectionGroup(hasDataCollectionGroup);
			form.setHasSnapshot(hasSnapshot);

			form.setListInfo(sampleList);
		} catch (Exception e) {

		}
	}

	/**
	 * Build the list of DM codes in the sample changer
	 * 
	 * @param form
	 * @return ArrayList
	 */
	protected void populateDMCodesinSC(ViewSampleForm form, HttpServletRequest request) {

		ActionMessages errors = new ActionMessages();
		String selectedBeamline = form.getSelectedBeamline();
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
		List listBeamlines = new ArrayList();
		List listDMCodes = new ArrayList();
		List listDMCodesFull = new ArrayList();

		try {
			// Retrieve the list of beamlines for the current proposal having datamatrix codes scanned by SC

			if (selectedBeamline == null) {
				selectedBeamline = BreadCrumbsForm.getIt(request).getSelectedBeamline();
			}

			if (selectedBeamline != null) {
				form.setSelectedBeamline(selectedBeamline);
			}

		} catch (Exception e)

		{
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}
	}
}
