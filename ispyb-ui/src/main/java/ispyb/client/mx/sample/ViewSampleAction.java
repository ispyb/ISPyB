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

package ispyb.client.mx.sample;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.util.Confidentiality;
import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.common.util.DBTools;
import ispyb.common.util.export.PdfExporterSample;
import ispyb.server.common.services.proposals.Laboratory3Service;
import ispyb.server.common.services.proposals.Person3Service;
import ispyb.server.common.services.shipping.Container3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Laboratory3VO;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.services.collections.EnergyScan3Service;
import ispyb.server.mx.services.collections.XFEFluorescenceSpectrum3Service;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.services.sample.Crystal3Service;
import ispyb.server.mx.services.sample.DiffractionPlan3Service;
import ispyb.server.mx.services.sample.Protein3Service;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.EnergyScan3VO;
import ispyb.server.mx.vos.collections.XFEFluorescenceSpectrum3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.DiffractionPlan3VO;
import ispyb.server.mx.vos.sample.Protein3VO;

/**
 * @struts.action name="viewSampleForm" path="/user/viewSample" type="ispyb.client.mx.sample.ViewSampleAction"
 *                validate="false" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="viewSamples" path="user.sample.viewSample.page"
 * @struts.action-forward name="viewSamplesManager" path="manager.sample.viewSample.page"
 * 
 * @struts.action-forward name="searchSample" path="user.sample.search.sample.page"
 * 
 * @struts.action-forward name="viewSampleAllDetails" path="user.sample.viewSampleAllDetails.page"
 * @struts.action-forward name="managerViewSampleAllDetails" path="manager.sample.viewSampleAllDetails.page"
 * 
 * @struts.action-forward name="localContactViewSampleAllDetails"
 *                        path="localcontact.sample.viewSampleAllDetails.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 * 
 */

public class ViewSampleAction extends DispatchAction {
	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Protein3Service proteinService;

	private Crystal3Service crystalService;

	private BLSample3Service sampleService;

	private DataCollection3Service dataCollectionService;

	private Laboratory3Service laboratoryService;

	private Person3Service personService;

	private DiffractionPlan3Service difPlanService;
	
	private XFEFluorescenceSpectrum3Service xrfSpectraService;
	
	private EnergyScan3Service energyScanService ;

	private final static Logger LOG = Logger.getLogger(ViewSampleAction.class);

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws CreateException, NamingException {

		this.proteinService = (Protein3Service) ejb3ServiceLocator.getLocalService(Protein3Service.class);
		this.crystalService = (Crystal3Service) ejb3ServiceLocator.getLocalService(Crystal3Service.class);
		this.sampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
		this.dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
				.getLocalService(DataCollection3Service.class);

		this.laboratoryService = (Laboratory3Service) ejb3ServiceLocator.getLocalService(Laboratory3Service.class);
		this.personService = (Person3Service) ejb3ServiceLocator.getLocalService(Person3Service.class);
		this.difPlanService = (DiffractionPlan3Service) ejb3ServiceLocator
				.getLocalService(DiffractionPlan3Service.class);
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
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages mErrors = new ActionMessages();

		// Save Originator for return path
		BreadCrumbsForm.getIt(request).setFromPage(request.getContextPath() + Constants.PAGE_SAMPLE_VIEW + "display");

		// ---------------------------------------------------------------------------------------------------
		// Retrieve Attributes
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

		try {
			ViewSampleForm form = (ViewSampleForm) actForm;
			List<BLSample3VO> blSamples3VOList = sampleService.findByProposalId(proposalId);

			// Look for data collection and snapshot files
			long startTime = System.currentTimeMillis();
			boolean[] hasSnapshot = new boolean[blSamples3VOList.size()];
			boolean[] hasDataCollection = new boolean[blSamples3VOList.size()];
			boolean[] hasDataCollectionGroup = new boolean[blSamples3VOList.size()];
			int i = 0;
			for (Iterator<BLSample3VO> mySamples = blSamples3VOList.iterator(); mySamples.hasNext();) {
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
			form.setListInfo(blSamples3VOList);
			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);
		} catch (Exception e) {
			mErrors.add(this.HandlesException(e));
		}

		if (!mErrors.isEmpty()) {
			saveErrors(request, mErrors);
			return (mapping.findForward("error"));
		}

		return mapping.findForward("viewSamples");
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
		// reset the breadcrumbs
		LOG.debug("view sample : display from menu");
		BreadCrumbsForm.getItClean(request);

		return this.display(mapping, actForm, request, response);
	}

	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayForName(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		LOG.debug("calculate the list of samples for a name");
		String name;
		String code;
		// old List mList = new ArrayList();
		List<BLSample3VO> blSample3VOList = new ArrayList<BLSample3VO>();

		ActionMessages mErrors = new ActionMessages();
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

		try {
			BLSample3VO blSample = new BLSample3VO();
			ViewSampleForm form = (ViewSampleForm) actForm;

			// Save Originator for return path
			BreadCrumbsForm.getItClean(request).setFromPage(
					request.getContextPath() + Constants.PAGE_SAMPLE_VIEW + "display");

			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			name = form.getName();
			name = name.replace('*', '%'); // Replace SQL wildcard
			code = form.getCode();
			code = code.replace('*', '%'); // Replace SQL wildcard
			// ---------------------------------------------------------------------------------------------------

			// if (name.equals(null) || name.equals(""))
			// name = "%";
			// if (code.equals(null) || code.equals(""))
			// code = "%";

			blSample.setName(name);
			blSample3VOList = sampleService.findByNameAndCodeAndProposalId(name.trim(), code.trim(), proposalId);

			if (blSample3VOList != null && !blSample3VOList.isEmpty()) {
				if (blSample3VOList.size() < 2) {
					BLSample3VO bfv = blSample3VOList.get(0);
					// blSample = sampleService.findByPk(bfv.getBlSampleId(), false);
					blSample = bfv;
					BreadCrumbsForm.getIt(request).setSelectedSample(blSample);
				}
			}

		} catch (Exception e) {
			mErrors.add(this.HandlesException(e));
		}
		return this.displayFor(proposalId, blSample3VOList, mErrors, mapping, actForm, request, response);
	}

	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayForBreadCrumbs(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		LOG.debug("Display for BreadCrumbs");
		ActionMessages mErrors = new ActionMessages();
		Container3VO selectedContainer = BreadCrumbsForm.getIt(request).getSelectedContainer();
		Dewar3VO selectedDewar = BreadCrumbsForm.getIt(request).getSelectedDewar();
		Shipping3VO selectedShipping = BreadCrumbsForm.getIt(request).getSelectedShipping();

		try {
			if (selectedContainer != null)
				return this.displayForContainer(mapping, actForm, request, response);
			else if (selectedDewar != null)
				return this.displayForDewar(mapping, actForm, request, response);
			else if (selectedShipping != null)
				return this.displayForShipping(mapping, actForm, request, response);

		} catch (Exception e) {
			mErrors.add(this.HandlesException(e));
		}
		return mapping.findForward("viewSamples");
	}

	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayForShipping(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		LOG.debug("calculate the list of samples for shipping");
		List<BLSample3VO> mList = new ArrayList<BLSample3VO>();
		Integer shippingId;
		ActionMessages mErrors = new ActionMessages();
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

		try {
			String shippingIdst = request.getParameter(Constants.SHIPPING_ID);
			if (shippingIdst != null)
				shippingId = new Integer(shippingIdst);
			else
				shippingId = BreadCrumbsForm.getIt(request).getSelectedShipping().getShippingId();

			// Confidentiality (check if proposalId matches)
			if (!Confidentiality.isAccessAllowedToShipping(request, shippingId)) {
				mErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Access denied"));
				saveErrors(request, mErrors);
				return (mapping.findForward("error"));
			}

			// Get a list
			mList = sampleService.findByShippingId(shippingId, null);

			// Populate BreadCrumbs
			Shipping3VO selectedShipping = DBTools.getSelectedShipping(shippingId);
			BreadCrumbsForm.getItClean(request).setSelectedShipping(selectedShipping);

			// Save Originator for return path
			BreadCrumbsForm.getIt(request).setFromPage(
					request.getContextPath() + Constants.PAGE_SAMPLE_VIEW + "displayForShipping&shippingId="
							+ shippingId.toString());

		} catch (Exception e) {
			mErrors.add(this.HandlesException(e));
		}
		return this.displayFor(proposalId, mList, mErrors, mapping, actForm, request, response);
	}

	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayForDewar(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		LOG.debug("calculate the list of samples for a dewar");
		List<BLSample3VO> mList = new ArrayList<BLSample3VO>();
		ActionMessages mErrors = new ActionMessages();
		Integer dewarId;
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

		try {
			String dewarIdst = request.getParameter(Constants.DEWAR_ID);

			if (dewarIdst != null)
				dewarId = new Integer(dewarIdst);
			else
				dewarId = BreadCrumbsForm.getIt(request).getSelectedDewar().getDewarId();

			// Confidentiality (check if proposalId matches)
			if (!Confidentiality.isAccessAllowedToDewar(request, dewarId)) {
				mErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Access denied"));
				saveErrors(request, mErrors);
				return (mapping.findForward("error"));
			}

			mList = sampleService.findByDewarId(dewarId, null);

			// Populate BreadCrumbs
			Dewar3VO selectedDewar = DBTools.getSelectedDewar(dewarId);

			BreadCrumbsForm.getIt(request).setSelectedDewar(selectedDewar);

			// Save Originator for return path
			BreadCrumbsForm.getIt(request).setFromPage(
					request.getContextPath() + Constants.PAGE_SAMPLE_VIEW + "displayForDewar&dewarId="
							+ dewarId.toString());

		} catch (Exception e) {
			mErrors.add(this.HandlesException(e));
		}
		return this.displayFor(proposalId, mList, mErrors, mapping, actForm, request, response);
	}

	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayForContainer(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		LOG.debug("Calculate the list of samples for a container");
		List<BLSample3VO> mList = new ArrayList<BLSample3VO>();
		ActionMessages mErrors = new ActionMessages();
		Integer containerId;
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

		try {
			String containerIdst = request.getParameter(Constants.CONTAINER_ID);

			if (containerIdst != null)
				containerId = new Integer(containerIdst);
			else
				containerId = BreadCrumbsForm.getIt(request).getSelectedContainer().getContainerId();

			// Confidentiality (check if proposalId matches)
			if (!Confidentiality.isAccessAllowedToContainer(request, containerId)) {
				mErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Access denied"));
				saveErrors(request, mErrors);
				return (mapping.findForward("error"));
			}
			mList = sampleService.findByContainerId(containerId);

			// Populate BreadCrumbs
			Container3VO selectedContainer = DBTools.getSelectedContainer(containerId);
			BreadCrumbsForm.getIt(request).setSelectedContainer(selectedContainer);
			BreadCrumbsForm.getIt(request).setSelectedContainerId(containerId);

			// Save Originator for return path
			BreadCrumbsForm.getIt(request).setFromPage(
					request.getContextPath() + Constants.PAGE_SAMPLE_VIEW + "displayForContainer&containerId="
							+ containerId.toString());

		} catch (Exception e) {

			mErrors.add(this.HandlesException(e));
		}
		return this.displayFor(proposalId, mList, mErrors, mapping, actForm, request, response);
	}

	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayForProtein(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		LOG.debug("calculate the list of samples for a protein");
		List<BLSample3VO> mList = new ArrayList<BLSample3VO>();
		Integer proteinId;
		ActionMessages mErrors = new ActionMessages();
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

		try {
			// if no proteinID coming from request, get it from current display
			String proteinIdst = request.getParameter(Constants.PROTEIN_ID);

			if (proteinIdst != null)
				proteinId = new Integer(proteinIdst);
			else
				proteinId = BreadCrumbsForm.getIt(request).getSelectedProtein().getProteinId();

			// Confidentiality (check if proposalId matches)
			if (!Confidentiality.isAccessAllowedToProtein(request, proteinId)) {
				mErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Access denied"));
				saveErrors(request, mErrors);
				return (mapping.findForward("error"));
			}

			if (proteinId != null) {

				mList = sampleService.findByProteinId(proteinId);
				// Populate BreadCrumbs information bar
				// fill the information bar

				Protein3VO selectedProtein = proteinService.findByPk(proteinId, false);
				BreadCrumbsForm.getItClean(request).setSelectedProtein(selectedProtein);

				// Save Originator for return path
				BreadCrumbsForm.getIt(request).setFromPage(
						request.getContextPath() + Constants.PAGE_SAMPLE_VIEW + "displayForProtein&proteinId="
								+ proteinId.toString());

			}

		} catch (Exception e) {

			mErrors.add(this.HandlesException(e));
		}
		return this.displayFor(proposalId, mList, mErrors, mapping, actForm, request, response);
	}

	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayForCrystal(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		LOG.debug("calculate the list of samples for a crystal");
		List<BLSample3VO> mList = new ArrayList<BLSample3VO>();
		Integer crystalId;
		ActionMessages mErrors = new ActionMessages();
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

		try {
			// if no crystalID coming from request, get it from current display

			String crystalIdst = request.getParameter(Constants.CRYSTAL_ID);

			if (crystalIdst != null) {
				crystalId = new Integer(crystalIdst);
				LOG.debug("calculate the list of samples for a crystalID = (request) " + crystalId);
			} else {
				crystalId = BreadCrumbsForm.getIt(request).getSelectedCrystal().getCrystalId();
				LOG.debug("calculate the list of samples for a crystalID = (breadCrumbs) " + crystalId);
			}
			mList = sampleService.findByCrystalId(crystalId);

			// Confidentiality (check if proposalId matches)
			if (!Confidentiality.isAccessAllowedToCrystal(request, crystalId)) {
				mErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Access denied"));
				saveErrors(request, mErrors);
				return (mapping.findForward("error"));
			}

			// Populate BreadCrumbs information bar
			BreadCrumbsForm.deleteIt(request);

			Crystal3VO selectedCrystal = crystalService.findByPk(crystalId, false);
			Protein3VO selectedProtein = selectedCrystal.getProteinVO();

			BreadCrumbsForm.getIt(request).setSelectedProtein(selectedProtein);
			BreadCrumbsForm.getIt(request).setSelectedCrystal(selectedCrystal);

			// Save Originator for return path
			BreadCrumbsForm.getIt(request).setFromPage(
					request.getContextPath() + Constants.PAGE_SAMPLE_VIEW + "displayForCrystal&crystalId="
							+ crystalId.toString());

		} catch (Exception e) {

			mErrors.add(this.HandlesException(e));
		}
		return this.displayFor(proposalId, mList, mErrors, mapping, actForm, request, response);
	}

	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayFree(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		LOG.debug("calculate the list of samples which are not linked to a dewar/shipment");
		List<BLSample3VO> mList = new ArrayList<BLSample3VO>();
		ActionMessages mErrors = new ActionMessages();
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

		// Save Originator for return path
		BreadCrumbsForm.getItClean(request).setFromPage(
				request.getContextPath() + Constants.PAGE_SAMPLE_VIEW + "displayFree");

		try {
			mList = sampleService.findByProposalIdAndDewarNull(proposalId);

		} catch (Exception e) {

			mErrors.add(this.HandlesException(e));
		}
		return this.displayFor(proposalId, mList, mErrors, mapping, actForm, request, response);
	}

	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayForSampleChanger(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		LOG.debug("calculate the list of samples in the sample changer");
		List<BLSample3VO> mList = new ArrayList<BLSample3VO>();
		ActionMessages mErrors = new ActionMessages();
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

		try {
			mList = sampleService.findByProposalIdAndIsInSampleChanger(proposalId, new Byte("1"));

			BreadCrumbsForm.deleteIt(request);
			BreadCrumbsForm.getIt(request).setSamplesInSampleChanger(Constants.SAMPLE_IN_SC);

		} catch (Exception e) {

			mErrors.add(this.HandlesException(e));
		}
		return this.displayFor(proposalId, mList, mErrors, mapping, actForm, request, response);
	}

	/**
	 * Last thing done by all displayForxxx methods TODO factorise with displayFor and delete when BLSampleFullBMP
	 * updated
	 * 
	 * @param errors
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayFor(int proposalId, List<BLSample3VO> sampleList, ActionMessages errors,
			ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {

		ViewSampleForm form = (ViewSampleForm) actForm;
		LOG.debug("displayFor : fill the form with the list of samples calculated");

		// Look for DataCollection and Snapshot files
		boolean[] hasSnapshot = new boolean[sampleList.size()];
		boolean[] hasDataCollection = new boolean[sampleList.size()];
		boolean[] hasDataCollectionGroup = new boolean[sampleList.size()];
		try {
			long startTime = System.currentTimeMillis();
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
		} catch (Exception e) {
			errors.add(this.HandlesException(e));
		}

		// Populate the form with Info
		form.setHasDataCollection(hasDataCollection);
		form.setHasDataCollectionGroup(hasDataCollectionGroup);
		form.setHasSnapshot(hasSnapshot);
		form.setListInfo(sampleList);
		FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}
		return redirectPageFromRole(mapping, request);
	}
	
	private ActionForward redirectPageFromRole(ActionMapping mapping, HttpServletRequest request) {
		// redirection page according to the Role ------------------------

		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();

		if (role.equals(Constants.ROLE_MANAGER)) {
			return mapping.findForward("viewSamplesManager");
		} else {
			return mapping.findForward("viewSamples");
		}
	}

	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayAllDetails(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages mErrors = new ActionMessages();

		try {
			Integer blsampleId;
			BLSample3VO blsampleLightValue = new BLSample3VO();
			LOG.debug("Fecthing all details for a Sample");
			ViewSampleForm form = (ViewSampleForm) actForm;

			// if no sampleID coming from request, get it from current display
			String sampleIdst = request.getParameter(Constants.BLSAMPLE_ID);

			if (sampleIdst != null)
				blsampleId = new Integer(sampleIdst);
			else
				blsampleId = BreadCrumbsForm.getIt(request).getSelectedSample().getBlSampleId();

			// Confidentiality (check if proposalId matches)
			if (!Confidentiality.isAccessAllowedToSample(request, blsampleId)) {
				mErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Access denied"));
				saveErrors(request, mErrors);
				return (mapping.findForward("error"));
			}

			if (sampleIdst != null) {

				blsampleLightValue = sampleService.findByPk(blsampleId, false, false, false);

				BreadCrumbsForm.getIt(request).setSelectedSample(blsampleLightValue);

				if (blsampleLightValue.getContainerVO() != null) {
					Container3VO containerVO = blsampleLightValue.getContainerVO();
					form.setContainerVO(containerVO);

					if (containerVO.getDewarVO() != null) {
						Dewar3VO dewarVO = containerVO.getDewarVO();
						form.setDewarVO(dewarVO);

						if (dewarVO.getShippingVO() != null) {
							Shipping3VO shippingVO = dewarVO.getShippingVO();
							form.setShippingVO(shippingVO);
							Laboratory3VO laboratoryLightValue = new Laboratory3VO();
							if (shippingVO.getLaboratoryId() != null && shippingVO.getLaboratoryId() > 0)
								laboratoryLightValue = laboratoryService.findByPk(shippingVO.getLaboratoryId());
							form.setLaboratoryLightValue(laboratoryLightValue);
							form.setLaboratoryAddress(laboratoryLightValue.getName(),
									laboratoryLightValue.getAddress(), laboratoryLightValue.getCity(),
									laboratoryLightValue.getCountry());
						}
					}
				}

				if (blsampleLightValue.getCrystalVO() != null) {
					Crystal3VO crystalVO = blsampleLightValue.getCrystalVO();
					form.setCrystalVO(crystalVO);

					if (crystalVO.getProteinVO() != null) {
						Protein3VO proteinVO = crystalVO.getProteinVO();
						form.setProteinVO(proteinVO);
						Person3VO personLightValue = new Person3VO();
						if (proteinVO.getPersonId() != null) {
							personLightValue = personService.findByPk(proteinVO.getPersonId());
						}
						form.setPersonLightValue(personLightValue);
						form.setResponsibleAddress(personLightValue.getGivenName(), personLightValue.getFamilyName(),
								personLightValue.getEmailAddress(), personLightValue.getPhoneNumber());
					}
				}

				if (blsampleLightValue.getDiffractionPlanVOId() != null) {
					DiffractionPlan3VO dpv = difPlanService.findByPk(blsampleLightValue.getDiffractionPlanVOId(),
							false, false);
					form.setDifPlanInfo(dpv);

				}
				form.setInfo(blsampleLightValue);
				FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);

			}

		} catch (Exception e) {
			this.HandlesException(e);
		}
		// return mapping.findForward("successAllLarge");
		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();
		if (role.equals(Constants.ROLE_LOCALCONTACT)) {
			return mapping.findForward("localContactViewSampleAllDetails");
		}else if (role.equals(Constants.ROLE_MANAGER)) {
			return mapping.findForward("managerViewSampleAllDetails");
		}  else {
			return mapping.findForward("viewSampleAllDetails");
		}
	}

	public ActionForward displayForProteinSearch(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
		LOG.debug("Calculate the list of proteins for acronym or for proposal #" + proposalId);
		ActionMessages errors = new ActionMessages();

		List<BLSample3VO> fullSampleList;
		List<Protein3VO> proteinList;

		try {
			// Retrieve Acronym from form
			ViewSampleForm form = (ViewSampleForm) actForm;
			String acronym = form.getAcronym();

			// Save Originator for return path
			BreadCrumbsForm.getItClean(request).setFromPage(
					request.getContextPath() + Constants.PAGE_SAMPLE_VIEW + "displayForProteinSearch");

			// Get an object list
			if (acronym == null || acronym.length() == 0) // If no acronym selected on the form
			{
				fullSampleList = sampleService.findByProposalId(proposalId);
				// Populate with Info
				form.setListInfo(fullSampleList);
			} else {
				acronym = acronym.replace('*', '%');
				fullSampleList = sampleService.findByAcronymAndProposalId(acronym, proposalId, null);
				form.setListInfo(fullSampleList);
			}

			proteinList = proteinService.findByAcronymAndProposalId(proposalId, acronym);
			if (proteinList.size() == 1) {
				Protein3VO selectedProtein = proteinList.get(0);
				BreadCrumbsForm.getIt(request).setSelectedProtein(selectedProtein);
				return this.displayForProtein(mapping, actForm, request, response);
			}

			// Look for DataCollection and Snapshot files
			boolean[] hasSnapshot = new boolean[fullSampleList.size()];
			boolean[] hasDataCollection = new boolean[fullSampleList.size()];
			boolean[] hasDataCollectionGroup = new boolean[fullSampleList.size()];
			try {
				int i = 0;
				for (Iterator<BLSample3VO> mySamples = fullSampleList.iterator(); mySamples.hasNext();) {
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
			} catch (Exception e) {
				errors.add(this.HandlesException(e));
			}

			// Populate the form with Info
			form.setHasDataCollection(hasDataCollection);
			form.setHasDataCollectionGroup(hasDataCollectionGroup);
			form.setHasSnapshot(hasSnapshot);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.sample.viewProtein"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}
		return mapping.findForward("viewSamples");
	}

	public ActionForward exportAsPdf(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		List<BLSample3VO> aList = new ArrayList<BLSample3VO>();

		// parameter to decide which pdf to display
		String sortView = "1";
		if (request.getParameter(Constants.SORT_VIEW) != null)
			sortView = request.getParameter(Constants.SORT_VIEW);

		String viewName = null;
		String proposalCode = (String) request.getSession().getAttribute(Constants.PROPOSAL_CODE);
		String proposalNumber = String.valueOf(request.getSession().getAttribute(Constants.PROPOSAL_NUMBER));
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
		Integer id;

		try {
			// get data to populate the form
			BreadCrumbsForm bar = BreadCrumbsForm.getIt(request);

			if (bar.getSelectedContainer() != null) {
				id = bar.getSelectedContainer().getContainerId();
				aList = sampleService.findByContainerId(id);
				viewName = "Dewar: " + bar.getSelectedDewar().getCode() + " --- Container: "
						+ bar.getSelectedContainer().getCode();
				LOG.debug("PDF export for container");
			} else if (bar.getSelectedDewar() != null) {
				id = bar.getSelectedDewar().getDewarId();
				aList = sampleService.findByDewarId(id, new Integer(sortView));
				LOG.debug("PDF export for dewar : sortView = " + sortView);

				viewName = "Dewar: " + bar.getSelectedDewar().getCode();
				if (bar.getSelectedDewar().getComments() != null)
					viewName = viewName + " --- " + bar.getSelectedDewar().getComments();

			} else if (bar.getSelectedShipping() != null) {
				id = bar.getSelectedShipping().getShippingId();
				aList = sampleService.findByShippingIdOrder(id, new Integer(sortView));
				LOG.debug("PDF export for shipment: sortView = " + sortView);
				viewName = "Shipment: " + bar.getSelectedShipping().getShippingName();

			} else if (bar.getSelectedProtein() != null) {
				id = bar.getSelectedProtein().getProteinId();
				aList = sampleService.findByProteinId(id);
				viewName = "Protein: " + bar.getSelectedProtein().getName();
				LOG.debug("PDF export for protein");
			} else {
				aList = sampleService.findByProposalIdAndDewarNull(proposalId);
				LOG.debug("PDF export for free samples");
				viewName = "Non assigned samples";

			}

			PdfExporterSample pdf = new PdfExporterSample(aList, viewName, sortView, proposalCode+proposalNumber);
			ByteArrayOutputStream baos = pdf.exportAsPdf();

			String filename = proposalCode + proposalNumber + "exportSampleList.pdf";

			// setting some response headers
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			// setting the content type
			response.setContentType("application/pdf");
			// the contentlength is needed for MSIE!!!
			response.setContentLength(baos.size());
			// write ByteArrayOutputStream to the ServletOutputStream
			ServletOutputStream out = response.getOutputStream();
			baos.writeTo(out);
			out.flush();
			out.close();

		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.sample.viewSample"));
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
	 * Handles the Exception for the various dispatchAction methods
	 * 
	 * @param e
	 */
	private ActionMessages HandlesException(Exception e) {

		ActionMessages mErrors = new ActionMessages();
		mErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.sample.viewSample"));
		mErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
		e.printStackTrace();
		return mErrors;
	}

	/**
	 * getShipping
	 * 
	 * @param containerId
	 * @return
	 */
	public static Shipping3VO getShipping(Integer containerId) {
		Shipping3VO shippingResult = new Shipping3VO();
		try {
			Container3Service containerService = (Container3Service) ejb3ServiceLocator
					.getLocalService(Container3Service.class);
			Shipping3VO targetShipping = containerService.findByPk(containerId, false).getDewarVO().getShippingVO();

			shippingResult = targetShipping;

		} catch (Exception e) {
			// e.printStackTrace();
		}

		return shippingResult;
	}

	/**
	 * getCrystalImageURL
	 * 
	 * @param crystalId
	 * @return
	 */
	public static String getCrystalImageURL(Integer crystalId) {
		String crystalImageURL = null;

		return crystalImageURL;
	}

	/**
	 * isCrystalImageURLOK
	 * 
	 * @param crystalId
	 * @return 1 if the Crystal imageURL is OK
	 */
	public static int isCrystalImageURLOK(Integer crystalId) {
		boolean crystalImageOK = false;
		try {
			String imageURL = getCrystalImageURL(crystalId);
			if (imageURL == null || imageURL.trim().equals(""))
				crystalImageOK = false;
			else
				crystalImageOK = true;
		} catch (Exception e) {
			// e.printStackTrace();
		}

		return (crystalImageOK) ? 1 : 0;
	}

}
