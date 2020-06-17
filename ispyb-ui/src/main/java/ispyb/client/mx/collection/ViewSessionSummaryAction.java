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
package ispyb.client.mx.collection;

import ispyb.client.SiteSpecific;
import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.util.GSonUtils;
import ispyb.client.mx.results.AutoProcessingData;
import ispyb.client.mx.results.ViewResultsAction;
import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.common.util.PathUtils;
import ispyb.common.util.StringUtils;
import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.services.autoproc.AutoProcProgramAttachment3Service;
import ispyb.server.mx.services.autoproc.ImageQualityIndicators3Service;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.services.collections.DataCollectionGroup3Service;
import ispyb.server.mx.services.collections.EnergyScan3Service;
import ispyb.server.mx.services.collections.GridInfo3Service;
import ispyb.server.mx.services.collections.XFEFluorescenceSpectrum3Service;
import ispyb.server.mx.vos.autoproc.AutoProc3VO;
import ispyb.server.mx.vos.autoproc.AutoProcProgramAttachment3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScalingStatistics3VO;
import ispyb.server.mx.vos.autoproc.ImageQualityIndicators3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.EnergyScan3VO;
import ispyb.server.mx.vos.collections.GridInfo3VO;
import ispyb.server.mx.vos.collections.Image3VO;
import ispyb.server.mx.vos.collections.IspybCrystalClass3VO;
import ispyb.server.mx.vos.collections.IspybReference3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.collections.Workflow3VO;
import ispyb.server.mx.vos.collections.WorkflowMesh3VO;
import ispyb.server.mx.vos.collections.XFEFluorescenceSpectrum3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @struts.action name="viewSessionSummaryForm" path="/user/viewSessionSummary"
 *                input="user.collection.viewSession.page" parameter="reqCode"
 *                scope="request" validate="false"
 * 
 * @struts.action-forward name="successAll"
 *                        path="user.collection.viewSessionSummary.page"
 * @struts.action-forward name="managerSuccessAll"
 *                        path="manager.collection.viewSessionSummary.page"
 * @struts.action-forward name="fedexmanagerSuccessAll"
 *                        path="fedexmanager.collection.viewSessionSummary.page"
 *                        *
 * @struts.action-forward name="localContactSuccessAll"
 *                        path="localcontact.collection.viewSessionSummary.page"
 * @struts.action-forward name="error" path="site.default.error.page"
 * @struts.action-forward name="noPermission" path="site.permission.error.page"
 * 
 */

public class ViewSessionSummaryAction extends DispatchAction {

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Proposal3Service proposalService;

	private Session3Service sessionService;

	private DataCollection3Service dataCollectionService;

	private DataCollectionGroup3Service dataCollectionGroupService;

	private XFEFluorescenceSpectrum3Service xfeService;

	private EnergyScan3Service energyScanService;

	private final static Logger LOG = Logger.getLogger(ViewSessionSummaryAction.class);

	public final static String KEY_ENERGY = "energy";

	public final static String KEY_ELEMENT = "element";

	public final static String KEY_EXPOSURE_TIME = "exposureTime";

	public final static String KEY_TOTAL_EXPOSURE_TIME = "totExposureTime";

	public final static String KEY_BEAMSIZE_HOR = "beamSizeHor";

	public final static String KEY_BEAMSIZE_VERT = "beamSizeVert";

	public final static String KEY_NB_IMAGES = "nbImages";

	public final static String KEY_NB_TOT_IMAGES = "nbTotImages";

	public final static String KEY_PHI_RANGE = "phiRange";

	public final static String KEY_FLUX = "flux";

	public final static String KEY_DETECTOR_RESOLUTION = "detectorResolution";

	public final static String KEY_TRANSMISSION = "transmission";

	public final static String KEY_WAVELENGTH = "wavelength";

	public final static String KEY_MESH_SIZE = "meshSize";

	public final static String KEY_BEST_POSITION_MESH = "bestPositionImageNumber";

	public final static String KEY_MESH_SIGNAL = "meshSignal";

	public final static String KEY_BEST_POSITION_LINE = "lineBestPositionImageNumber";

	public final static String KEY_LINE_SIGNAL = "lineSignal";

	public final static String KEY_V1 = "v1";

	public final static String KEY_V2 = "v2";

	public final static String KEY_N = "n";

	public final static String KEY_INFLECTION_ENERGY = "inflectionEnergy";

	public final static String KEY_INFLECTION_F_PRIME = "inflectionFPrime";

	public final static String KEY_INFLECTION_F_DOUBLE_PRIME = "inflectionFDoublePrime";

	public final static String KEY_PEAK_ENERGY = "peakEnergy";

	public final static String KEY_PEAK_ENERGY_PRIME = "peakEnergyPrime";

	public final static String KEY_PEAK_ENERGY_DOUBLE_PRIME = "peakEnergyDoublePrime";

	public final static String KEY_REMOTE_ENERGY = "remoteEnergy";

	public final static String KEY_REMOTE_ENERGY_PRIME = "remoteEnergyPrime";

	public final static String KEY_REMOTE_ENERGY_DOUBLE_PRIME = "remoteEnergyDoublePrime";

	public final static String KEY_UNIT_CELL = "unitCell";

	public final static String KEY_UNIT_CELL_TITLE = "unitCellTitle";

	public final static String KEY_UNIT_CELL2 = "unitCell2";

	public final static String KEY_MOSAICITY = "mosaicity";

	public final static String KEY_RANKING_RESOLUTION = "rankingResolution";

	public final static String KEY_SPACE_GROUP = "spaceGroup";

	public final static String KEY_COMPLETENESS = "completeness";

	public final static String KEY_RESOLUTION = "resolution";

	public final static String KEY_RSYMM = "rsymm";

	public final static Integer DEFAULT_NB_ITEMS = 10;

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {
		this.proposalService = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
		this.sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
		this.dataCollectionGroupService = (DataCollectionGroup3Service) ejb3ServiceLocator
				.getLocalService(DataCollectionGroup3Service.class);
		this.dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
				.getLocalService(DataCollection3Service.class);
		this.xfeService = (XFEFluorescenceSpectrum3Service) ejb3ServiceLocator
				.getLocalService(XFEFluorescenceSpectrum3Service.class);
		this.energyScanService = (EnergyScan3Service) ejb3ServiceLocator.getLocalService(EnergyScan3Service.class);
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	/**
	 * display dispatcher
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();

		try {
			// Search first in request then in BreadCrumbs
			Integer sessionId;
			Integer nbOfItems = DEFAULT_NB_ITEMS;

			if (request.getParameter(Constants.SESSION_ID) != null)
				sessionId = new Integer(request.getParameter(Constants.SESSION_ID));
			else if (BreadCrumbsForm.getIt(request).getSelectedSession() != null)
				sessionId = BreadCrumbsForm.getIt(request).getSelectedSession().getSessionId();
			else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "sessionId is null"));
				saveErrors(request, errors);
				return (mapping.findForward("error"));
			}
			ViewSessionSummaryForm form = (ViewSessionSummaryForm) actForm;

			if (request.getParameter(Constants.NB_DATA_SESSION_OBJECTS) != null)
				nbOfItems = new Integer(request.getParameter(Constants.NB_DATA_SESSION_OBJECTS));

			form.setNbOfItems(nbOfItems);

			Session3VO slv = sessionService.findByPk(sessionId, false, true, true);
			// Fill the information bar
			BreadCrumbsForm.getItClean(request).setSelectedSession(slv);

		} catch (AccessDeniedException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.info(Constants.ACCESS_DENIED);
			return (mapping.findForward("noPermission"));

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("error.user.collection.viewDataCollectionGroup"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}
		// redirect according role
		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();
		if (role.equals(Constants.FXMANAGE_ROLE_NAME)) {
			return mapping.findForward("fedexmanagerSuccessAll");
		} else if (role.equals(Constants.ROLE_MANAGER)) {
			return mapping.findForward("managerSuccessAll");
		} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
			return mapping.findForward("localContactSuccessAll");
		} else {
			return mapping.findForward("successAll");
		}
	}

	public ActionForward displayAll(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();

		try {
			// Search first in request then in BreadCrumbs
			Integer sessionId;
			Integer nbOfItems = 0;

			if (request.getParameter(Constants.SESSION_ID) != null)
				sessionId = new Integer(request.getParameter(Constants.SESSION_ID));
			else if (BreadCrumbsForm.getIt(request).getSelectedSession() != null)
				sessionId = BreadCrumbsForm.getIt(request).getSelectedSession().getSessionId();
			else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "sessionId is null"));
				saveErrors(request, errors);
				return (mapping.findForward("error"));
			}
			ViewSessionSummaryForm form = (ViewSessionSummaryForm) actForm;
			form.setNbOfItems(nbOfItems);

			Session3VO slv = sessionService.findByPk(sessionId, false, true, true);
			// Fill the information bar
			BreadCrumbsForm.getItClean(request).setSelectedSession(slv);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("error.user.collection.viewDataCollectionGroup"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}
		// redirect according role
		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();
		if (role.equals(Constants.FXMANAGE_ROLE_NAME)) {
			return mapping.findForward("fedexmanagerSuccessAll");
		} else if (role.equals(Constants.ROLE_MANAGER)) {
			return mapping.findForward("managerSuccessAll");
		} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
			return mapping.findForward("localContactSuccessAll");
		} else {
			return mapping.findForward("successAll");
		}
	}

	public void getSessionSummary(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		// set the number of sessiondataInformationObjects to display
		Integer nbOfItems = null;
		ViewSessionSummaryForm form = (ViewSessionSummaryForm) actForm;
		if (form.getNbOfItems() != null)
			nbOfItems = form.getNbOfItems();

		if (nbOfItems < 1)
			nbOfItems = null;
		this.getSessionSummaryList(mapping, actForm, request, response, nbOfItems);

	}

	@SuppressWarnings("unchecked")
	private void getSessionSummaryList(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response, Integer nbOfItems) {

		List<DataCollectionGroup3VO> dataCollectionGroupList = new ArrayList<DataCollectionGroup3VO>();
		List<EnergyScan3VO> energyScanList = new ArrayList<EnergyScan3VO>();
		List<XFEFluorescenceSpectrum3VO> xfeList = new ArrayList<XFEFluorescenceSpectrum3VO>();

		List<String> errors = new ArrayList<String>();
		boolean isIndus = SiteSpecific.isIndustrial(request);

		String proposalCode = null;
		if (request.getSession().getAttribute(Constants.PROPOSAL_CODE) != null) {
			proposalCode = (String) (request.getSession().getAttribute(Constants.PROPOSAL_CODE));
		}
		String proposalNumber = null;
		if (request.getSession().getAttribute(Constants.PROPOSAL_NUMBER) != null) {
			proposalNumber = (String) (request.getSession().getAttribute(Constants.PROPOSAL_NUMBER));
		}

		try {
			// Search first in request then in BreadCrumbs
			Integer sessionId = null;
			if (request.getParameter(Constants.SESSION_ID) != null)
				sessionId = new Integer(request.getParameter(Constants.SESSION_ID));
			else if (BreadCrumbsForm.getIt(request).getSelectedSession() != null)
				sessionId = BreadCrumbsForm.getIt(request).getSelectedSession().getSessionId();
			else {
				errors.add("sessionId is null");
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				// data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return;
			}

			Session3VO slv = sessionService.findByPk(sessionId, false, true, true);
			// Fill the information bar
			BreadCrumbsForm.getItClean(request).setSelectedSession(slv);

			// Confidentiality (check if object proposalId matches)
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			//Proposal3VO pv = proposalService.findByPk(proposalId);
			Proposal3VO pv = proposalService.findWithParticipantsByPk(proposalId);
			
			// crystal classes
			// Get an object list.
			List<IspybCrystalClass3VO> listOfCrystalClass = (List<IspybCrystalClass3VO>) request.getSession()
					.getAttribute(Constants.ISPYB_CRYSTAL_CLASS_LIST);

			// session information
			// String mpEmail = null;
			// if (pv != null)
			// mpEmail = pv.getPersonVO().getEmailAddress();
			// String structureDeterminations = slv.getStructureDeterminations()
			// == null || slv.getStructureDeterminations() == 0 ? ""
			// : slv.getStructureDeterminations().toString();
			// String dewarTransport = slv.getDewarTransport() == null ||
			// slv.getDewarTransport() == 0 ? "" : slv.getDewarTransport()
			// .toString();
			// String dataBackupFrance = slv.getDatabackupFrance() == null ||
			// slv.getDatabackupFrance() == 0 ? "" : slv
			// .getDatabackupFrance().toString();
			// String dataBackupEurope = slv.getDatabackupEurope() == null ||
			// slv.getDatabackupEurope() == 0 ? "" : slv
			// .getDatabackupEurope().toString();
			// // gets the email address for the beamline operator
			// Get localContact email
			String beamLineOperatorEmail = slv.getBeamLineOperatorEmail();

			// we load only the last items which have got datacollections linked
			dataCollectionGroupList = dataCollectionGroupService.findFiltered(sessionId, true, false);
			List<DataCollectionGroup3VO> aListTmp = new ArrayList<DataCollectionGroup3VO>();
			for (Iterator<DataCollectionGroup3VO> myDataCollectionGroups = dataCollectionGroupList.iterator(); myDataCollectionGroups
					.hasNext();) {
				DataCollectionGroup3VO myDataCollectionGroup = myDataCollectionGroups.next();
				int nbCol = myDataCollectionGroup.getDataCollectionsList().size();
				// if ((nbCol > 0) || !selectedNbCollect.equals("WithCollect"))
				// {
				if ((nbCol > 0)) {
					aListTmp.add(myDataCollectionGroup);
				}
			}
			dataCollectionGroupList = aListTmp;

			if (nbOfItems != null && aListTmp != null && nbOfItems < aListTmp.size()) {
				List<DataCollectionGroup3VO> resultListLight = new ArrayList<DataCollectionGroup3VO>(nbOfItems);
				for (int i = 0; i < nbOfItems; i++) {
					resultListLight.add(aListTmp.get(i));
				}
				dataCollectionGroupList = resultListLight;
			}

			energyScanList = slv.getEnergyScansList();
			xfeList = slv.getXfeSpectrumsList();

			SessionInformation sessionInformation = new SessionInformation(slv, beamLineOperatorEmail,
					dataCollectionGroupList.size() > 0);
			// Set DataCollection list (for export)
			List<DataCollection3VO> dataCollectionList = new ArrayList<DataCollection3VO>();
			for (Iterator<DataCollectionGroup3VO> iterator = dataCollectionGroupList.iterator(); iterator.hasNext();) {
				DataCollectionGroup3VO dataCollectionGroup3VO = iterator.next();
				dataCollectionList.addAll(dataCollectionGroup3VO.getDataCollectionVOs());
			}
			List<DataCollection3VO> tmpDataCollectionList = new ArrayList<DataCollection3VO>();
			for (Iterator<DataCollection3VO> iterator = dataCollectionList.iterator(); iterator.hasNext();) {
				DataCollection3VO dataCollection3VO = iterator.next();
				DataCollection3VO collect = dataCollectionService.findByPk(dataCollection3VO.getDataCollectionId(),
						false, false);
				tmpDataCollectionList.add(collect);
			}
			dataCollectionList = tmpDataCollectionList;

			// additional information
			List<DataCollectionGroup> listOfGroups = new ArrayList<DataCollectionGroup>();
			for (Iterator<DataCollectionGroup3VO> it = dataCollectionGroupList.iterator(); it.hasNext();) {
				DataCollectionGroup3VO vo = it.next();
				Integer runNumber = null;
				if (vo.getDataCollectionsList() != null && vo.getDataCollectionsList().size() == 1) {
					runNumber = vo.getDataCollectionsList().get(0).getDataCollectionNumber();
				}
				String proteinAcronym = "";
				String sampleName = "";
				String imagePrefix = "";
				if (vo.getBlSampleVO() != null) {
					sampleName = vo.getBlSampleVO().getName();
					if (vo.getBlSampleVO().getCrystalVO() != null
							&& vo.getBlSampleVO().getCrystalVO().getProteinVO() != null) {
						proteinAcronym = vo.getBlSampleVO().getCrystalVO().getProteinVO().getAcronym();
					}
				}
				if (vo.getDataCollectionsList() != null && vo.getDataCollectionsList().size() > 0) {
					imagePrefix = vo.getDataCollectionsList().get(0).getImagePrefix();
				}
				DataCollectionGroup group = new DataCollectionGroup(vo, runNumber, proteinAcronym, sampleName,
						imagePrefix);
				listOfGroups.add(group);
			}

			// Issue 1763: set the group comments to dc
			// dataCollectionList =
			// ViewDataCollectionAction.setDataCollectionComments(dataCollectionList);
			request.getSession().setAttribute(Constants.DATACOLLECTION_LIST, dataCollectionList);
			// Set EnergyScan list (for export)
			request.getSession().setAttribute(Constants.ENERGYSCAN_LIST, energyScanList);
			// Set XFE list (for export)
			request.getSession().setAttribute(Constants.XFE_LIST, xfeList);

			List<XFESpectra> xrfSpectraList = new ArrayList<XFESpectra>();
			for (Iterator<XFEFluorescenceSpectrum3VO> iterator = xfeList.iterator(); iterator.hasNext();) {
				XFEFluorescenceSpectrum3VO xfe = iterator.next();
				int jpegScanExists = PathUtils.fileExists(xfe.getJpegScanFileFullPath());
				String jpegScanFileFullPathParser = PathUtils.FitPathToOS(xfe.getJpegScanFileFullPath());
				int annotatedPymcaXfeSpectrumExists = PathUtils.fileExists(xfe.getAnnotatedPymcaXfeSpectrum());
				String shortFileNameAnnotatedPymcaXfeSpectrum = StringUtils.getShortFilename(xfe
						.getAnnotatedPymcaXfeSpectrum());
				String sampleName = "";
				if (xfe.getBlSampleVO() != null) {
					sampleName = xfe.getBlSampleVO().getName();
				}
				XFESpectra spectra = new XFESpectra(xfe, jpegScanExists == 1, jpegScanFileFullPathParser,
						annotatedPymcaXfeSpectrumExists == 1, shortFileNameAnnotatedPymcaXfeSpectrum, sampleName);
				spectra.setAnnotatedPymcaXfeSpectrum(PathUtils.FitPathToOS(xfe.getAnnotatedPymcaXfeSpectrum()));
				xrfSpectraList.add(spectra);
			}

			List<EnergyScan> energyScans = new ArrayList<EnergyScan>();
			for (Iterator<EnergyScan3VO> iterator = energyScanList.iterator(); iterator.hasNext();) {
				EnergyScan3VO energyScan = iterator.next();
				int scanFileFullPathExists = PathUtils.fileExists(energyScan.getScanFileFullPath());
				String shortFileName = StringUtils.getShortFilename(energyScan.getScanFileFullPath());
				int choochExists = PathUtils.fileExists(energyScan.getJpegChoochFileFullPath());
				String jpegChoochFileFitPath = PathUtils.FitPathToOS(energyScan.getJpegChoochFileFullPath());
				String sampleName = "";
				if (energyScan.getBlSampleVO() != null) {
					sampleName = energyScan.getBlSampleVO().getName();
				}
				EnergyScan es = new EnergyScan(energyScan, scanFileFullPathExists == 1, shortFileName,
						choochExists == 1, jpegChoochFileFitPath, sampleName);
				energyScans.add(es);
			}

			// list of references
			List<IspybReference3VO> listOfReferences = new ArrayList<IspybReference3VO>();
			List<IspybReference3VO> allRef = (List<IspybReference3VO>) request.getSession().getAttribute(
					Constants.ISPYB_REFERENCE_LIST);
			List<String> listOfBeamlines = new ArrayList<String>();
			boolean isXFE = (xfeList != null && xfeList.size() > 0);
			if (isXFE) {
				for (Iterator<DataCollectionGroup3VO> iterator = dataCollectionGroupList.iterator(); iterator.hasNext();) {
					DataCollectionGroup3VO dataCollectionGroup3VO = iterator.next();
					String bl = dataCollectionGroup3VO.getSessionVO().getBeamlineName();
					if (!listOfBeamlines.contains(bl)) {
						listOfBeamlines.add(bl);
					}
				}
				for (Iterator<XFEFluorescenceSpectrum3VO> iterator = xfeList.iterator(); iterator.hasNext();) {
					XFEFluorescenceSpectrum3VO xfe3VO = iterator.next();
					String bl = xfe3VO.getSessionVO().getBeamlineName();
					if (!listOfBeamlines.contains(bl)) {
						listOfBeamlines.add(bl);
					}
				}

				for (Iterator<IspybReference3VO> iterator = allRef.iterator(); iterator.hasNext();) {
					IspybReference3VO ispybReference3VO = iterator.next();
					if (ispybReference3VO.getBeamline().equals(Constants.REFERENCE_ALL_XRF)) {
						listOfReferences.add(ispybReference3VO);
					}
					if (ispybReference3VO.getBeamline().equals(Constants.REFERENCE_XRF)) {
						listOfReferences.add(ispybReference3VO);
					} else {
						String beamline = ispybReference3VO.getBeamline();
						boolean isBeamlineInvolved = listOfBeamlines.contains(beamline);
						if (isBeamlineInvolved) {
							listOfReferences.add(ispybReference3VO);
						}
					}
				}
			}
			String referenceText = "When reporting the results of XRF analysis<br/>please cite the reference below";
			request.getSession().setAttribute(Constants.REFERENCES_LIST, listOfReferences);

			// has MXPressO/MXPressE workflows?
			boolean sessionHasMXpressOWF = false;
			for (Iterator<DataCollectionGroup> ite = listOfGroups.iterator(); ite.hasNext();) {
				DataCollectionGroup dcg = ite.next();
				if (dcg.getWorkflowVO()!=null && dcg.getWorkflowVO().isMXPressEOIA()){
					sessionHasMXpressOWF = true;
				}
			}
			// stats
			if (sessionInformation != null) {
				sessionInformation.setNbEnergyScans(energyScans.size());
				sessionInformation.setNbXRFSpectra(xrfSpectraList.size());
				sessionInformation.setNbCollect(sessionService.getNbOfCollects(sessionId));
				sessionInformation.setNbTest(sessionService.getNbOfTests(sessionId));
			}

			request.getSession().setAttribute(Constants.DATACOLLECTIONGROUP_LIST, listOfGroups);

			// view session summary
			List<SessionDataObjectInformation> listSessionDataObjectInformation = new ArrayList<SessionDataObjectInformation>();
			try {
				listSessionDataObjectInformation = buildSessionDataInformation(dataCollectionList, energyScanList,
						xfeList, proposalCode, proposalNumber, request);

				request.getSession().setAttribute(Constants.SESSION_DATA_OBJECT_LIST, listSessionDataObjectInformation);
			} catch (Exception e) {
				e.printStackTrace();
				errors.add("Error while building summary: " + e);
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				// data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return;
			}

			List<CrystalClassSummary> listCrystal = new ArrayList<CrystalClassSummary>();
			// User information
			if (!isIndus) {
				// Perform only for IFX proposal in case of MXPress experiment
				if (proposalCode != null && proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX)) {
					// for MXPress users, calculate number of Crystals of class
					// 1(C),2(SC), 3(PS), T, E
					// initialization
					List<Integer> listOfNbCrystalPerClass = new ArrayList<Integer>();
					int nbCC = listOfCrystalClass.size();
					for (int i = 0; i < nbCC; i++) {
						listOfNbCrystalPerClass.add(0);
					}
					String crystalClass;
					// Browse Data Collections
					for (Iterator<DataCollectionGroup3VO> it = dataCollectionGroupList.iterator(); it.hasNext();) {
						crystalClass = it.next().getCrystalClass();
						int idCc = ViewDataCollectionGroupAction.getCrystalClassIndex(listOfCrystalClass, crystalClass);
						if (idCc != -1) {
							listOfNbCrystalPerClass.set(idCc, listOfNbCrystalPerClass.get(idCc) + 1);
						}
					}
					// Browse Energy Scans
					for (Iterator<EnergyScan3VO> it = energyScanList.iterator(); it.hasNext();) {
						crystalClass = it.next().getCrystalClass();
						int idCc = ViewDataCollectionGroupAction.getCrystalClassIndex(listOfCrystalClass, crystalClass);
						if (idCc != -1 && crystalClass != null && crystalClass.toUpperCase().trim().equals("E")) {
							listOfNbCrystalPerClass.set(idCc, listOfNbCrystalPerClass.get(idCc) + 1);
						}
					}
					// Browse XRF Spectra
					for (Iterator<XFEFluorescenceSpectrum3VO> it = xfeList.iterator(); it.hasNext();) {
						crystalClass = it.next().getCrystalClass();
						int idCc = ViewDataCollectionGroupAction.getCrystalClassIndex(listOfCrystalClass, crystalClass);
						if (idCc != -1 && crystalClass != null && crystalClass.toUpperCase().trim().equals("X")) {
							listOfNbCrystalPerClass.set(idCc, listOfNbCrystalPerClass.get(idCc) + 1);
						}
					}
					for (int i = 0; i < nbCC; i++) {
						if (listOfNbCrystalPerClass.get(i) > 0) {
							listCrystal.add(new CrystalClassSummary(listOfCrystalClass.get(i).getCrystalClassCode(),
									listOfCrystalClass.get(i).getCrystalClassName(), listOfNbCrystalPerClass.get(i)));
						}
					}
				}
			}

			// can edit and save comments
			boolean canSave = !isIndus;

			Integer currentPageNumber = 1;
			Integer forSessionId = -1;
			String pageNumber = (String) request.getSession().getAttribute("sessionReportCurrentPageNumber");
			String forSessionIdS = (String) request.getSession().getAttribute("forSessionId");
			if (pageNumber != null) {
				try {
					currentPageNumber = Integer.parseInt(pageNumber);
					forSessionId = Integer.parseInt(forSessionIdS);
				} catch (NumberFormatException e) {

				}
			}
			if (!forSessionId.equals(sessionId)) {
				currentPageNumber = 1;
			}

			HashMap<String, Object> data = new HashMap<String, Object>();
			// context path
			data.put("contextPath", request.getContextPath());
			// isFX
			boolean isFx = false;
			if (request.getSession().getAttribute(Constants.PROPOSAL_CODE) != null)
				isFx = request.getSession().getAttribute(Constants.PROPOSAL_CODE).toString().toLowerCase()
						.equals(Constants.PROPOSAL_CODE_FX);
			data.put("isFx", isFx);
			// isIX
			boolean isIx = false;
			if (request.getSession().getAttribute(Constants.PROPOSAL_CODE) != null)
				isIx = request.getSession().getAttribute(Constants.PROPOSAL_CODE).toString().toLowerCase()
						.equals(Constants.PROPOSAL_CODE_IX);
			data.put("isIx", isIx);
			// isIndus
			data.put("isIndus", isIndus);
			// sessionId
			data.put("sessionId", sessionId);
			// session
			data.put("session", sessionInformation);
			// list of references
			data.put("listOfReferences", listOfReferences);
			// referenceText
			data.put("referenceText", referenceText);
			// listSessionDataObjectInformation
			data.put("listSessionDataObjectInformation", listSessionDataObjectInformation);
			// sessionHasMXpressOWF
			data.put("sessionHasMXpressOWF", sessionHasMXpressOWF);
			// sessionSummary
			data.put("sessionSummary", true);
			// crystal list
			data.put("listCrystal", listCrystal);
			// listOfCrystal
			data.put("listOfCrystalClass", listOfCrystalClass);
			// can edit comments
			data.put("canSave", canSave);
			// page Number
			data.put("startPageNumber", currentPageNumber);
			// nbOfItems
			data.put("nbOfItems", nbOfItems);
			// data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");

		} catch (Exception e) {
			// errors.add(ActionMessages.GLOBAL_MESSAGE,
			// new
			// ActionMessage("error.user.collection.viewDataCollectionGroup"));
			// errors.add(ActionMessages.GLOBAL_MESSAGE, new
			// ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
			errors.add(e.toString());
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("errors", errors);
			// data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
			return;
		}

	}

	private static Integer getDataCollectionIndex(List<DataCollection3VO> dataCollectionList, Integer dataCollectionId) {
		Integer idDc = -1;
		for (int k = 0; k < dataCollectionList.size(); k++) {
			DataCollection3VO dcVo = dataCollectionList.get(k);
			if (dcVo.getDataCollectionId().equals(dataCollectionId)) {
				idDc = k;
				break;
			}
		}
		return idDc;
	}

	/**
	 * build the sessionDataObjectsList from the different list of collects,
	 * energy scan and xrfSpectra This list is then sorted by time asc
	 * 
	 * @param dataCollectionList
	 * @param listEnergyScan
	 * @param listXRFSpectra
	 * @return
	 */
	public static List<SessionDataObject> getSessionDataObjects(List<DataCollection3VO> dataCollectionList,
			List<EnergyScan3VO> listEnergyScan, List<XFEFluorescenceSpectrum3VO> listXRFSpectra) {
		List<SessionDataObject> listSessionDataObject = new ArrayList<SessionDataObject>();
		// add Collect and workflows
		List<Integer> listDCStored = new ArrayList<Integer>();
		int id = 0;
		int nbCollect = dataCollectionList.size();
		for (Iterator<DataCollection3VO> iterator = dataCollectionList.iterator(); iterator.hasNext();) {
			DataCollection3VO collect = iterator.next();
			if (collect.getDataCollectionGroupVO().getWorkflowVO() == null) {
				listSessionDataObject.add(new SessionDataObject(collect));
				listDCStored.add(collect.getDataCollectionId());
			} else {
				Workflow3VO workflow = collect.getDataCollectionGroupVO().getWorkflowVO();
				if (listDCStored.indexOf(collect.getDataCollectionId()) == -1) {
					List<DataCollection3VO> listDataCollection = new ArrayList<DataCollection3VO>();
					listDataCollection.add(collect);
					listDCStored.add(collect.getDataCollectionId());
					for (int k = id + 1; k < nbCollect; k++) {
						DataCollection3VO c = dataCollectionList.get(k);
						if (c.getDataCollectionGroupVO().getWorkflowVO() != null
								&& c.getDataCollectionGroupVO().getWorkflowVO().getWorkflowId()
										.equals(workflow.getWorkflowId())) {
							listDataCollection.add(c);
							listDCStored.add(c.getDataCollectionId());
						}
					}
					List<DataCollection3VO> sortedList = ViewSessionSummaryAction
							.getSortedDataCollectionList(listDataCollection);
					listSessionDataObject.add(new SessionDataObject(workflow, sortedList));
				}
			}
			id++;
		}
		// add EnergyScan
		if (listEnergyScan != null) {
			for (Iterator<EnergyScan3VO> iterator = listEnergyScan.iterator(); iterator.hasNext();) {
				EnergyScan3VO energyScan = iterator.next();
				listSessionDataObject.add(new SessionDataObject(energyScan));
			}
		}
		// add xrfSpectra
		if (listXRFSpectra != null) {
			for (Iterator<XFEFluorescenceSpectrum3VO> iterator = listXRFSpectra.iterator(); iterator.hasNext();) {
				XFEFluorescenceSpectrum3VO xrfSpectra = iterator.next();
				listSessionDataObject.add(new SessionDataObject(xrfSpectra));
			}
		}
		// sort list by dataTime
		listSessionDataObject = sortByTime(listSessionDataObject);
		return listSessionDataObject;
	}

	/**
	 * sort
	 * 
	 * @param listSessionDataObject
	 * @return
	 */
	public static List<SessionDataObject> sortByTime(List<SessionDataObject> listSessionDataObject) {
		List<SessionDataObject> listSorted = new ArrayList<SessionDataObject>();
		int nb = listSessionDataObject.size();
		for (int i = 0; i < nb; i++) {
			SessionDataObject o = listSessionDataObject.get(i);
			Date dataTime = o.getDataTime();
			int nbS = listSorted.size();
			int index = -1;
			for (int j = 0; j < nbS; j++) {
				if (listSorted.get(j).getDataTime().after(dataTime)) {
					index = j;
					break;
				}
			}
			if (index == -1) {
				listSorted.add(o);
			} else {
				listSorted.add(index, o);
			}

		}
		return listSorted;
	}

	public static List<DataCollection3VO> getSortedDataCollectionList(List<DataCollection3VO> listCollect) {
		List<DataCollection3VO> sortedListCollect = new ArrayList<DataCollection3VO>();
		if (listCollect != null && listCollect.size() > 0) {
			// sort by dataCollectionId DESC
			int nbC = listCollect.size();
			for (int k = 0; k < nbC; k++) {
				DataCollection3VO c = listCollect.get(k);
				Integer dcId = c.getDataCollectionId();
				int id = -1;
				for (int l = 0; l < sortedListCollect.size(); l++) {
					if (dcId > sortedListCollect.get(l).getDataCollectionId()) {
						id = l;
						break;
					}
				}
				if (id == -1) {
					sortedListCollect.add(c);
				} else {
					sortedListCollect.add(id, c);
				}
			}
		}
		return sortedListCollect;
	}

	private static SessionDataObjectInformation getSessionDataObjectForEnergyScan(SessionDataObject sessionDataObject) {
		SessionDataObjectInformation info = new SessionDataObjectInformation(sessionDataObject);
		EnergyScan3VO energyScan = sessionDataObject.getEnergyScan();
		// experimentType
		info.setExperimentType("Energy Scan");
		if (Constants.SITE_IS_MAXIV()){
			info.setImagePrefix(energyScan.getFilename());
		} else {
			info.setImagePrefix("");
		}

		info.setImageThumbnailPath("");
		info.setCrystalSnapshotPath("");
		// parameters
		List<Param> listParameters = new ArrayList<Param>();
		listParameters.add(new Param(KEY_ELEMENT, "Element", energyScan.getElement(), false));
		listParameters.add(new Param(KEY_EXPOSURE_TIME, "Exposure time", (energyScan.getExposureTime() == null ? ""
				: ("" + energyScan.getExposureTime() + " s")), false));
		listParameters.add(new Param(KEY_TRANSMISSION, "Transmission factor",
				(energyScan.getTransmissionFactor() == null ? "" : ("" + energyScan.getTransmissionFactor() + " %")),
				false));
		if (!Constants.SITE_IS_MAXIV()) {
			listParameters.add(new Param(KEY_BEAMSIZE_HOR, "Beam size Hor",
					(energyScan.getBeamSizeHorizontal() == null ? "" : ("" + energyScan.getBeamSizeHorizontal() + " Î¼m")),
					false));
			listParameters.add(new Param(KEY_BEAMSIZE_VERT, "Beam size Vert",
					(energyScan.getBeamSizeVertical() == null ? "" : ("" + energyScan.getBeamSizeVertical() + " Î¼m")),
					false));
		}
		info.setListParameters(listParameters);
		// comments
		info.setComments(energyScan.getComments());
		// crystal class
		info.setCrystalClass(energyScan.getCrystalClass());
		
		// protein and sampleName
		info.setProteinAcronym("");
		info.setSampleName("");
		info.setSampleNameProtein("");
		if (energyScan.getBlSampleVO() != null && !Constants.SITE_IS_MAXIV()) {
			setSampleAndProteinNames(info, energyScan.getBlSampleVO());
		} 

		// graph
		int choochExists = PathUtils.fileExists(energyScan.getJpegChoochFileFullPath());
		String jpegChoochFileFitPath = PathUtils.FitPathToOS(energyScan.getJpegChoochFileFullPath());
		info.setGraphExist(choochExists == 1);
		info.setGraphPath(jpegChoochFileFitPath);
		// list results
		info.setResult("  ");
		info.setResultStatus("");
		List<Param> listResults = new ArrayList<Param>();
		listResults.add(new Param(KEY_INFLECTION_ENERGY, "Inflection Energy", ""
				+ (energyScan.getInflectionEnergy() == null ? "" : energyScan.getInflectionEnergy() + " keV"), false));
		listResults.add(new Param(KEY_INFLECTION_F_PRIME, "Inflection f'", ""
				+ (energyScan.getInflectionFPrime() == null ? "" : energyScan.getInflectionFPrime() + ""), false));
		listResults.add(new Param(KEY_INFLECTION_F_DOUBLE_PRIME, "Inflection f''", ""
				+ (energyScan.getInflectionFDoublePrime() == null ? "" : energyScan.getInflectionFDoublePrime() + ""),
				false));
		listResults.add(new Param(KEY_PEAK_ENERGY, "Peak Energy", ""
				+ (energyScan.getPeakEnergy() == null ? "" : energyScan.getPeakEnergy() + " keV"), false));
		listResults.add(new Param(KEY_PEAK_ENERGY_PRIME, "Peak f'", ""
				+ (energyScan.getPeakFPrime() == null ? "" : energyScan.getPeakFPrime() + ""), false));
		listResults.add(new Param(KEY_PEAK_ENERGY_DOUBLE_PRIME, "Peak f''", ""
				+ (energyScan.getPeakFDoublePrime() == null ? "" : energyScan.getPeakFDoublePrime() + ""), false));
		listResults.add(new Param(KEY_REMOTE_ENERGY, "Remote Energy", ""
				+ (energyScan.getRemoteEnergy() == null ? "" : energyScan.getRemoteEnergy() + " keV"), false));
		listResults.add(new Param(KEY_REMOTE_ENERGY_PRIME, "Remote f'", ""
				+ (energyScan.getRemoteFPrime() == null ? "" : energyScan.getRemoteFPrime() + ""), false));
		listResults.add(new Param(KEY_REMOTE_ENERGY_DOUBLE_PRIME, "Remote f''", ""
				+ (energyScan.getRemoteFDoublePrime() == null ? "" : energyScan.getRemoteFDoublePrime() + ""), false));
		info.setListResults(listResults);

		return info;
	}

	private static SessionDataObjectInformation getSessionDataObjectForXRFSpectra(SessionDataObject sessionDataObject) {
		SessionDataObjectInformation info = new SessionDataObjectInformation(sessionDataObject);
		XFEFluorescenceSpectrum3VO xrfSpectrum = sessionDataObject.getXrfSpectra();
		// experimentType
		info.setExperimentType("XRFSpectrum");
		if (Constants.SITE_IS_MAXIV()){
			info.setImagePrefix(xrfSpectrum.getFilename());
		} else {
			info.setImagePrefix("");
        }
		info.setImagePrefix("");
		info.setImageThumbnailPath("");
		info.setCrystalSnapshotPath("");
		Float energy = xrfSpectrum.getEnergy();
		String energyValue = Float.toString(energy) +" keV";
		if (Constants.SITE_IS_MAXIV()){
			DecimalFormat decimalFormat = new DecimalFormat("#.##");
			energyValue = decimalFormat.format(energy) +" keV";
		}

		// parameters
		List<Param> listParameters = new ArrayList<Param>();
		listParameters.add(new Param(KEY_ENERGY, "Energy", xrfSpectrum.getEnergy() == null ? "" : energyValue, false));
		listParameters.add(new Param(KEY_EXPOSURE_TIME, "Exposure time", (xrfSpectrum.getExposureTime() == null ? ""
				: ("" + xrfSpectrum.getExposureTime() + " s")), false));
		if (!Constants.SITE_IS_MAXIV()) {
			listParameters
					.add(new Param(KEY_BEAMSIZE_HOR, "Beam size Hor", (xrfSpectrum.getBeamSizeHorizontal() == null ? ""
							: ("" + xrfSpectrum.getBeamSizeHorizontal() + " Î¼m")), false));
			listParameters.add(new Param(KEY_BEAMSIZE_VERT, "Beam size Vert",
					(xrfSpectrum.getBeamSizeVertical() == null ? "" : ("" + xrfSpectrum.getBeamSizeVertical() + " Î¼m")),
					false));
		}
		info.setListParameters(listParameters);
		// comments
		info.setComments(xrfSpectrum.getComments());
		// crystal class
		info.setCrystalClass(xrfSpectrum.getCrystalClass());
		
		// protein and sampleName
		info.setProteinAcronym("");
		info.setSampleName("");
		info.setSampleNameProtein("");
		if (xrfSpectrum.getBlSampleVO() != null && !Constants.SITE_IS_MAXIV()) {
			setSampleAndProteinNames(info, xrfSpectrum.getBlSampleVO());
		} 

		// graph
		int jpegScanExists = PathUtils.fileExists(xrfSpectrum.getJpegScanFileFullPath());
		String jpegScanFileFullPathParser = PathUtils.FitPathToOS(xrfSpectrum.getJpegScanFileFullPath());
		info.setGraphExist(jpegScanExists == 1);
		info.setGraphPath(jpegScanFileFullPathParser);
		// results
		String result = "";
		int annotatedPymcaXfeSpectrumExists = PathUtils.fileExists(xrfSpectrum.getAnnotatedPymcaXfeSpectrum());
		String shortFileNameAnnotatedPymcaXfeSpectrum = StringUtils.getShortFilename(xrfSpectrum
				.getAnnotatedPymcaXfeSpectrum());
		if (annotatedPymcaXfeSpectrumExists == 0 && !Constants.SITE_IS_MAXIV()) {
			result = "Html report not found: " + shortFileNameAnnotatedPymcaXfeSpectrum;
		} else if (annotatedPymcaXfeSpectrumExists == 1) {
			result = shortFileNameAnnotatedPymcaXfeSpectrum;
			result = "<a  href='viewResults.do?reqCode=displayHtmlFile&HTML_FILE="
					+ PathUtils.FitPathToOS(xrfSpectrum.getAnnotatedPymcaXfeSpectrum()) + "'  styleClass='LIST'>"
					+ shortFileNameAnnotatedPymcaXfeSpectrum + "</a>";
		}
		info.setResult(result);
		info.setResultStatus("");
		return info;
	}

	private static SessionDataObjectInformation getSessionDataObjectForWorkflow(SessionDataObject sessionDataObject,
			String proposalCode, String proposalNumber, HttpServletRequest request,
			List<DataCollection3VO> dataCollectionList, AutoProcShellWrapper wrapper) throws Exception {
		SessionDataObjectInformation info = new SessionDataObjectInformation(sessionDataObject);
		Workflow3VO workflow = sessionDataObject.getWorkflow();

		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		GridInfo3Service gridInfoService = (GridInfo3Service) ejb3ServiceLocator
				.getLocalService(GridInfo3Service.class);
		ImageQualityIndicators3Service imageQualityIndicatorsService = (ImageQualityIndicators3Service) ejb3ServiceLocator
				.getLocalService(ImageQualityIndicators3Service.class);

		DecimalFormat df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df2.applyPattern("#####0.00");
		DecimalFormat df3 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df3.applyPattern("#####0.000");

		// experimentType
		info.setExperimentType(workflow.getWorkflowType());
		// lastCollect
		List<DataCollection3VO> listCollectInWF = sessionDataObject.getListDataCollection();
		int nbCollectInWF = 0;
		if (listCollectInWF != null) {
			nbCollectInWF = listCollectInWF.size();
		}
		DataCollection3VO lastCollect = null;
		// DataCollection3VO nextToLastCollectChar = null;
		if (nbCollectInWF > 0) {
			lastCollect = listCollectInWF.get(0);
		}
		// if (nbCollectInWF > 1) {
		// nextToLastCollectChar = listCollectInWF.get(1);
		// }
		DataCollectionInformation dcInfo = null;
		if (lastCollect != null) {
			DataCollectionExporter dcExporter = new DataCollectionExporter(df2, df3, proposalCode, proposalNumber,
					request);
			dcInfo = dcExporter.getDataCollectionInformation(lastCollect, null, null);

		}
		int nbTotImages = 0;
		for (Iterator<DataCollection3VO> it = listCollectInWF.iterator(); it.hasNext();) {
			DataCollection3VO dataCollection3VO = it.next();
			nbTotImages += dataCollection3VO.getNumberOfImages();

		}
		Workflow wf = ViewWorkflowAction.getWorkflowInfo(workflow);
		List<WorkflowMesh3VO> listWorkflowMesh = ViewWorkflowAction.getWorkflowMesh(wf);
		// image prefix and run number comments
		if (dcInfo != null) {
			info.setImagePrefix(dcInfo.getImagePrefix());
			info.setDataCollectionId(lastCollect.getDataCollectionId());
			info.setRunNumber(dcInfo.getDataCollectionNumber());
			// we display the comment of the datacollection group
			if (lastCollect.getDataCollectionGroupVO().getComments() != null) {
				info.setComments(lastCollect.getDataCollectionGroupVO().getComments());
			}
			// crystal class
			info.setCrystalClass(lastCollect.getDataCollectionGroupVO().getCrystalClass());
			
			// protein and sampleName
			info.setProteinAcronym("");
			info.setSampleName("");
			info.setSampleNameProtein("");
			if (lastCollect.getDataCollectionGroupVO().getBlSampleVO() != null) {
				setSampleAndProteinNames(info, lastCollect.getDataCollectionGroupVO().getBlSampleVO());
			} 
			
			// parameters
			List<Param> listParameters = new ArrayList<Param>();
			String barcode = lastCollect.getDataCollectionGroupVO().getActualSampleBarcode();
			info.setBarcode(barcode);
			String samplePosition = lastCollect.getSamplePosition();
			info.setSamplePosition(samplePosition);
			listParameters.add(new Param(KEY_NB_TOT_IMAGES, "Nb tot images", Integer.toString(nbTotImages), false));
			if (workflow.isMXPressEOIA())
				listParameters.add(new Param(KEY_NB_IMAGES, "Nb images", Integer.toString(nbTotImages), false));
			listParameters.add(new Param(KEY_EXPOSURE_TIME, "Exp. time", (dcInfo.getExposureTime().isEmpty() ? ""
					: dcInfo.getExposureTime() + " s"), false));
			listParameters.add(new Param(KEY_PHI_RANGE, "Phi range", (dcInfo.getAxisRange().isEmpty() ? "" : dcInfo
					.getAxisRange() + " " + Constants.DEGREE), false));
			listParameters.add(new Param(KEY_FLUX, "Flux", (dcInfo.getFlux().isEmpty() ? "" : dcInfo.getFlux()
					+ " ph/sec"), false));
			listParameters.add(new Param(KEY_DETECTOR_RESOLUTION, "Detector resolution", (dcInfo.getResolution()
					.isEmpty() ? "" : dcInfo.getResolution() + " " + Constants.ANGSTROM), false));
			listParameters.add(new Param(KEY_TRANSMISSION, "Transmission", (lastCollect.getTransmission() == null ? ""
					: df2.format(lastCollect.getTransmission())), false));
			listParameters.add(new Param(KEY_WAVELENGTH, "Wavelength", (dcInfo.getWavelength().isEmpty() ? "" : dcInfo
					.getWavelength() + " " + Constants.ANGSTROM), false));
			listParameters.add(new Param(KEY_TOTAL_EXPOSURE_TIME, "Total expo time", (dcInfo.getTotalExposure()
					.isEmpty() ? "" : dcInfo.getTotalExposure() + " s"), false));
			if (lastCollect.getDataCollectionGroupVO().getExperimentType()
					.equals(Constants.EXPERIMENT_TYPE_CHARACTERIZATION)) {
				// As characterizations are now recorded as workflows we have to handle them separately here
				info.setListParameters(listParameters);
				// comments
				info.setComments(dcInfo.getComments());
				// first image thumbnail
				int imageThumbnailExist = PathUtils.fileExists(dcInfo.getPathDiffractionImg1());
				String imageThumbnailPath = PathUtils.FitPathToOS(dcInfo.getPathDiffractionImg1());
				info.setImageThumbnailExist(imageThumbnailExist == 1);
				info.setImageThumbnailPath(imageThumbnailPath);
				info.setImageId(dcInfo.getDiffractionImgId1());
				// first Snapshot
				int crystalSnapshotExist = PathUtils.fileExists(dcInfo.getPathjpgCrystal());
				String crystalSnapshotPath = PathUtils.FitPathToOS(dcInfo.getPathjpgCrystal());
				info.setCrystalSnapshotExist(crystalSnapshotExist == 1);
				info.setCrystalSnapshotPath(crystalSnapshotPath);
				// Characterization info
				info = buildInfoForCharacterization(info, dcInfo);
			} else {
				if (workflow.isMeshMXpressM()) {
					String meshSize = "";
					// gridinfo dx_mm and dy_mm
					if (listWorkflowMesh != null && listWorkflowMesh.size() > 0) {
						WorkflowMesh3VO workflowMesh = listWorkflowMesh.get(0);
						if (workflowMesh != null) {
							List<GridInfo3VO> list = gridInfoService.findByWorkflowMeshId(workflowMesh.getWorkflowMeshId());
							if (list != null && list.size() > 0) {
								GridInfo3VO gridInfo = list.get(0);
								meshSize = gridInfo.getDx_mm() + " " + gridInfo.getDy_mm();
							}
						}
					}
					listParameters.add(new Param(KEY_MESH_SIZE, "Mesh size", meshSize, false));
				}
				info.setListParameters(listParameters);
	
				// third image thumbnail
				int imageThumbnailExist = PathUtils.fileExists(dcInfo.getPathDiffractionImg1());
				String imageThumbnailPath = PathUtils.FitPathToOS(dcInfo.getPathDiffractionImg1());
				info.setImageThumbnailExist(imageThumbnailExist == 1);
				info.setImageThumbnailPath(imageThumbnailPath);
				info.setImageId(dcInfo.getDiffractionImgId1());
				// first Snapshot
				int crystalSnapshotExist = PathUtils.fileExists(dcInfo.getPathJpgCrystal3());
				String crystalSnapshotPath = PathUtils.FitPathToOS(dcInfo.getPathJpgCrystal3());
				info.setCrystalSnapshotExist(crystalSnapshotExist == 1);
				info.setCrystalSnapshotPath(crystalSnapshotPath);
	
				// if mesh Best Image: WorkflowMesh.bestImage
				if (listWorkflowMesh != null && listWorkflowMesh.size() > 0) {
					// in case of XRayCentring: Line bestImage
					WorkflowMesh3VO workflowMesh = listWorkflowMesh.get(0);
					boolean wfWith2Mesh = (workflow.getWorkflowType().equals(Constants.WORKFLOW_XRAYCENTERING) || workflow
							.isMXPressEOIA()) && listWorkflowMesh.size() > 1;
	
					if (wfWith2Mesh) {
						workflowMesh = listWorkflowMesh.get(1);
					}
					if (workflowMesh != null) {
						Image3VO bestImage = workflowMesh.getBestImageVO();
						// TODO understand why the thumbnail is replaced by the one
						// of the best image from the mesh ???
						// because the jpg image is not replaced
						// for now replace it only if no thumbnail
						// TODO remove this later because it is disturbing that the
						// image is not pointing to the same than the thumbnail ?
						// TODO have more info and examples : bug #2515
						if (bestImage != null && !info.isImageThumbnailExist()) {
							String jpgThumbFullPath = bestImage.getJpegThumbnailFileFullPath();
							jpgThumbFullPath = PathUtils.FitPathToOS(jpgThumbFullPath);
							imageThumbnailExist = PathUtils.fileExists(jpgThumbFullPath);
							imageThumbnailPath = jpgThumbFullPath;
							if (imageThumbnailExist == 1) {
								info.setImageThumbnailExist(imageThumbnailExist == 1);
								info.setImageThumbnailPath(imageThumbnailPath);
							}
						}
						workflowMesh = listWorkflowMesh.get(0);
						// graph
						int graphExist = PathUtils.fileExists(workflowMesh.getCartographyPath());
						String graphPath = PathUtils.FitPathToOS(workflowMesh.getCartographyPath());
						info.setGraphExist(graphExist == 1);
						info.setGraphPath(graphPath);
	
						if (wfWith2Mesh) {
							workflowMesh = listWorkflowMesh.get(1);
							int graph2Exist = PathUtils.fileExists(workflowMesh.getCartographyPath());
							String graph2Path = PathUtils.FitPathToOS(workflowMesh.getCartographyPath());
							info.setGraph2Exist(graph2Exist == 1);
							info.setGraph2Path(graph2Path);
						}
					}
				}
				// results
				String imgFailed = "../images/Sphere_Red_12.png";
				String imgSuccess = "../images/Sphere_Green_12.png";
				String imgLaunched = "../images/Sphere_Orange_12.png";
				String img = "../images/Sphere_White_12.png";
				String wfStatus = workflow.getStatus();
				if (wfStatus != null) {
					if (wfStatus.equals("Failure")) {
						img = imgFailed;
					} else if (wfStatus.equals("Started") || wfStatus.equals("Launched")) {
						img = imgLaunched;
					} else if (wfStatus.equals("Success")) {
						img = imgSuccess;
					}
				}
	
				String result = workflow.getWorkflowType() + " " + "<img src='" + img + "'  border='0' />";
				String resultStatus = "";
				info.setResult(result);
				info.setResultStatus(resultStatus);
				List<Param> listResults = new ArrayList<Param>();
				info.setListResults(listResults);
				if (workflow.isMXPressEOIA()) {
					// autoProcessing Result
					info = getAutoProcResult(info, lastCollect, dcInfo, dataCollectionList, wrapper, proposalCode,
							proposalNumber, request, false);
				} else {
					// first: Mesh
					// second :Line
					if (listWorkflowMesh != null && listWorkflowMesh.size() > 0) {
						WorkflowMesh3VO workflowMesh = listWorkflowMesh.get(0);
	
						if (workflowMesh != null) {
							listResults
									.add(new Param(KEY_V1, "V1",
											""
													+ (workflowMesh.getValue1() == null ? "" : df2.format(workflowMesh
															.getValue1())), false));
							listResults
									.add(new Param(KEY_V2, "V2",
											""
													+ (workflowMesh.getValue2() == null ? "" : df2.format(workflowMesh
															.getValue2())), false));
							listResults
									.add(new Param(KEY_N, "N",
											""
													+ (workflowMesh.getValue3() == null ? "" : df2.format(workflowMesh
															.getValue3())), false));
							// best position
							if (workflowMesh.getBestImageVO() != null) {
								Image3VO bestImage = workflowMesh.getBestImageVO();
								listResults.add(new Param(KEY_BEST_POSITION_MESH, "Mesh Best position image#", ""
										+ bestImage.getImageNumber(), false));
								// imageQualityIndicators
								ImageQualityIndicators3VO quality = null;
								List<ImageQualityIndicators3VO> listIQ = imageQualityIndicatorsService
										.findByImageId(bestImage.getImageId());
								if (listIQ != null && listIQ.size() > 0) {
									quality = listIQ.get(0);
								}
								String meshSig1 = "";
								String meshSig2 = "";
								if (quality != null) {
									meshSig1 = "" + (quality.getDozor_score() == null ? "" : quality.getDozor_score());
									meshSig2 = ""
											+ (quality.getTotalIntegratedSignal() == null ? "" : quality
													.getTotalIntegratedSignal());
								}
								// listResults.add(new Param("meshSignal1",
								// "Mesh Signal 1", meshSig1, false));
								// listResults.add(new Param("meshSignal2",
								// "Mesh Signal 2", meshSig2, false));
								listResults.add(new Param(KEY_MESH_SIGNAL, "Mesh Signal 1 / Signal2", meshSig1 + " / "
										+ meshSig2, false));
							}
						}
						// line
						if (listWorkflowMesh.size() > 1) {
							WorkflowMesh3VO workflowMeshLine = listWorkflowMesh.get(1);
							if (workflowMeshLine.getBestImageVO() != null) {
								Image3VO bestImageLine = workflowMeshLine.getBestImageVO();
								listResults.add(new Param(KEY_BEST_POSITION_LINE, "Line Best position image#", ""
										+ bestImageLine.getImageNumber(), false));
								// imageQualityIndicators
								ImageQualityIndicators3VO quality = null;
								List<ImageQualityIndicators3VO> listIQ = imageQualityIndicatorsService
										.findByImageId(bestImageLine.getImageId());
								if (listIQ != null && listIQ.size() > 0) {
									quality = listIQ.get(0);
								}
								String lineSig1 = "";
								String lineSig2 = "";
								if (quality != null) {
									lineSig1 = "" + (quality.getDozor_score() == null ? "" : quality.getDozor_score());
									lineSig2 = ""
											+ (quality.getTotalIntegratedSignal() == null ? "" : quality
													.getTotalIntegratedSignal());
								}
								// listResults.add(new Param("lineSigna1",
								// "Line Signal 1", lineSig1, false));
								// listResults.add(new Param("lineSignal2",
								// "Line Signal 2",lineSig2, false));
								listResults.add(new Param(KEY_LINE_SIGNAL, "Line Signal 1 / Signal 2", lineSig1 + " / "
										+ lineSig2, false));
							}
						}
					}
					info.setListResults(listResults);
				}
			}

		}

		return info;
	}

	private static SessionDataObjectInformation getSessionDataObjectForCollect(SessionDataObject sessionDataObject,
			String proposalCode, String proposalNumber, HttpServletRequest request,
			List<DataCollection3VO> dataCollectionList, AutoProcShellWrapper wrapper) throws Exception {

		SessionDataObjectInformation info = new SessionDataObjectInformation(sessionDataObject);
		DataCollection3VO dataCollection = sessionDataObject.getDataCollection();

		DecimalFormat df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df2.applyPattern("#####0.00");
		DecimalFormat df3 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df3.applyPattern("#####0.000");

		DataCollectionExporter dcExporter = new DataCollectionExporter(df2, df3, proposalCode, proposalNumber, request);
		DataCollectionInformation dcInfo = dcExporter.getDataCollectionInformation(dataCollection, null, null);
		// experimentType
		info.setExperimentType(dataCollection.getDataCollectionGroupVO().getExperimentType());
		// image prefix and run number
		info.setImagePrefix(dcInfo.getImagePrefix());
		info.setDataCollectionId(dataCollection.getDataCollectionId());
		info.setRunNumber(dcInfo.getDataCollectionNumber());
		
		// protein and sampleName
		info.setProteinAcronym("");
		info.setSampleName("");
		info.setSampleNameProtein("");
		if (dataCollection.getDataCollectionGroupVO().getBlSampleVO() != null) {
			setSampleAndProteinNames(info, dataCollection.getDataCollectionGroupVO().getBlSampleVO());
		} 
		
		// parameters
		List<Param> listParameters = new ArrayList<Param>();
		String barcode = dataCollection.getDataCollectionGroupVO().getActualSampleBarcode();
		info.setBarcode(barcode);
		String samplePosition = dataCollection.getSamplePosition();
		info.setSamplePosition(samplePosition);
		listParameters.add(new Param(KEY_NB_IMAGES, "Nb images", dcInfo.getNumberOfImages(), false));
		listParameters.add(new Param(KEY_EXPOSURE_TIME, "Exp. time", (dcInfo.getExposureTime().isEmpty() ? "" : dcInfo
				.getExposureTime() + " s"), false));
		listParameters.add(new Param(KEY_PHI_RANGE, "Phi range", (dcInfo.getAxisRange().isEmpty() ? "" : dcInfo
				.getAxisRange() + " " + Constants.DEGREE), false));
		listParameters.add(new Param(KEY_FLUX, "Flux",
				(dcInfo.getFlux().isEmpty() ? "" : dcInfo.getFlux() + " ph/sec"), false));
		listParameters.add(new Param(KEY_DETECTOR_RESOLUTION, "Detector resolution",
				(dcInfo.getResolution().isEmpty() ? "" : dcInfo.getResolution() + " " + Constants.ANGSTROM), false));
		listParameters.add(new Param(KEY_TRANSMISSION, "Transmission", (dataCollection.getTransmission() == null ? ""
				: df2.format(dataCollection.getTransmission())), false));
		listParameters.add(new Param(KEY_WAVELENGTH, "Wavelength", (dcInfo.getWavelength().isEmpty() ? "" : dcInfo
				.getWavelength() + " " + Constants.ANGSTROM), false));
		listParameters.add(new Param(KEY_TOTAL_EXPOSURE_TIME, "Total expo time",
				(dcInfo.getTotalExposure().isEmpty() ? "" : dcInfo.getTotalExposure() + " s"), false));
		info.setListParameters(listParameters);
		// comments
		info.setComments(dcInfo.getComments());
		// crystal class
		info.setCrystalClass(dataCollection.getDataCollectionGroupVO().getCrystalClass());
		// first image thumbnail
		int imageThumbnailExist = PathUtils.fileExists(dcInfo.getPathDiffractionImg1());
		String imageThumbnailPath = PathUtils.FitPathToOS(dcInfo.getPathDiffractionImg1());
		info.setImageThumbnailExist(imageThumbnailExist == 1);
		info.setImageThumbnailPath(imageThumbnailPath);
		info.setImageId(dcInfo.getDiffractionImgId1());
		// first Snapshot
		int crystalSnapshotExist = PathUtils.fileExists(dcInfo.getPathjpgCrystal());
		String crystalSnapshotPath = PathUtils.FitPathToOS(dcInfo.getPathjpgCrystal());
		info.setCrystalSnapshotExist(crystalSnapshotExist == 1);
		info.setCrystalSnapshotPath(crystalSnapshotPath);
		// graph, results depending of the type of the object
		// CHARACTERIZATION
		if (dataCollection.getDataCollectionGroupVO().getExperimentType()
				.equals(Constants.EXPERIMENT_TYPE_CHARACTERIZATION)) {
			info = buildInfoForCharacterization(info, dcInfo);
		} else {// if
				// (dataCollection.getDataCollectionGroupVO().getExperimentType().equalsIgnoreCase("OSC")
				// ||
				// dataCollection.getDataCollectionGroupVO().getExperimentType().equals("SAD")
				// ||
				// dataCollection.getDataCollectionGroupVO().getExperimentType().equals("Helical")){
				// // OSC or SAD or
				// Helical
			info = buildInfoWithAutoProcResults(info, dcInfo, dataCollection, dataCollectionList, wrapper,
					proposalCode, proposalNumber, request);

		}

		return info;
	}

	private List<SessionDataObjectInformation> buildSessionDataInformation(List<DataCollection3VO> dataCollectionList,
			List<EnergyScan3VO> energyScanList, List<XFEFluorescenceSpectrum3VO> xfeList, String proposalCode,
			String proposalNumber, HttpServletRequest request) throws Exception {
		// auto proc wrapper
		double rMerge = 10.0;
		double iSigma = 1.0;
		AutoProcShellWrapper wrapper = ViewDataCollectionAction.getAutoProcStatistics(dataCollectionList, rMerge,
				iSigma);

		List<SessionDataObject> listSessionDataObject = ViewSessionSummaryAction.getSessionDataObjects(
				dataCollectionList, energyScanList, xfeList);

		List<SessionDataObjectInformation> listSessionDataObjectInformation = new ArrayList<SessionDataObjectInformation>();

		for (Iterator<SessionDataObject> iterator = listSessionDataObject.iterator(); iterator.hasNext();) {
			SessionDataObject sessionDataObject = iterator.next();
			SessionDataObjectInformation info = new SessionDataObjectInformation(sessionDataObject);

			if (sessionDataObject.isDataCollection()) { // COLLECT
				info = ViewSessionSummaryAction.getSessionDataObjectForCollect(sessionDataObject, proposalCode,
						proposalNumber, request, dataCollectionList, wrapper);
			} else if (sessionDataObject.isWorkflow()) { // WORKFLOW
				info = ViewSessionSummaryAction.getSessionDataObjectForWorkflow(sessionDataObject, proposalCode,
						proposalNumber, request, dataCollectionList, wrapper);
			} else if (sessionDataObject.isEnergyScan()) { // ENERGY SCAN
				info = ViewSessionSummaryAction.getSessionDataObjectForEnergyScan(sessionDataObject);
			} else if (sessionDataObject.isXRFSpectra()) { // XRF SPECTRA
				info = ViewSessionSummaryAction.getSessionDataObjectForXRFSpectra(sessionDataObject);
			}

			listSessionDataObjectInformation.add(info);
		}

		return listSessionDataObjectInformation;
	}

	private static SessionDataObjectInformation buildInfoForCharacterization(SessionDataObjectInformation info,
			DataCollectionInformation dcInfo) {
		int graphExist = PathUtils.fileExists(dcInfo.getPathEdnaGraph());
		String graphPath = PathUtils.FitPathToOS(dcInfo.getPathEdnaGraph());
		info.setGraphExist(graphExist == 1);
		info.setGraphPath(graphPath);

		// status for EDNA
		String result = "";
		String resultStatus = "";
		String imgFailed = "../images/Sphere_Red_12.png";
		String imgSuccess = "../images/Sphere_Green_12.png";
		String img = imgFailed;

		if (dcInfo.getDataCollectionId() == 1299919) {
			int i = 0;
		}
		if (dcInfo.getScreeningIndexingSuccess() == 1) {
			img = imgSuccess;
		}
		result += "Indexing " + "<img src='" + img + "'  border='0' />";
		img = imgFailed;
		if (dcInfo.getScreeningStrategySuccess() == 1) {
			img = imgSuccess;
		}
		result += " Strategy " + "<img src='" + img + "'  border='0' />";

		info.setResult(result);
		info.setResultStatus(resultStatus);
		List<Param> listResults = new ArrayList<Param>();
		listResults.add(new Param("spaceGroup", "Space Group", dcInfo.getSpacegroup(), false));
		String unitCells = "";
		if (dcInfo.getCellA() != null && !dcInfo.getCellA().isEmpty()) {
			unitCells = dcInfo.getCellA() + ", " + dcInfo.getCellB() + ", " + dcInfo.getCellC();
		}
		String unitCells2 = "";
		if (dcInfo.getCellAlpha() != null && !dcInfo.getCellAlpha().isEmpty()) {
			unitCells2 = dcInfo.getCellAlpha() + ", " + dcInfo.getCellBeta() + ", " + dcInfo.getCellGamma();
		}
		listResults.add(new Param(KEY_UNIT_CELL_TITLE, "Unit Cell", "", false));
		listResults.add(new Param(KEY_UNIT_CELL, "a, b, c", unitCells, false));
		listResults.add(new Param(KEY_UNIT_CELL2, Constants.ALPHA + " " + Constants.BETA + " " + Constants.GAMMA,
				unitCells2, false));
		listResults.add(new Param(KEY_MOSAICITY, "Mosaicity", (dcInfo.getMosaicity().isEmpty() ? "" : dcInfo
				.getMosaicity() + " " + Constants.DEGREE), false));
		listResults.add(new Param(KEY_RANKING_RESOLUTION, "Ranking Resolution", (dcInfo.getResObserved().isEmpty() ? ""
				: dcInfo.getResObserved() + " " + Constants.ANGSTROM), false));
		info.setListResults(listResults);

		return info;
	}

	private static SessionDataObjectInformation buildInfoWithAutoProcResults(SessionDataObjectInformation info,
			DataCollectionInformation dcInfo, DataCollection3VO dataCollection,
			List<DataCollection3VO> dataCollectionList, AutoProcShellWrapper wrapper, String proposalCode,
			String proposalNumber, HttpServletRequest request) throws Exception {

		info.setGraphExist(false);
		info.setGraphPath(null);

		info = getAutoProcResult(info, dataCollection, dcInfo, dataCollectionList, wrapper, proposalCode,
				proposalNumber, request, true);

		return info;
	}

	@SuppressWarnings("unchecked")
	private static SessionDataObjectInformation getAutoProcResult(SessionDataObjectInformation info,
			DataCollection3VO dataCollection, DataCollectionInformation dcInfo,
			List<DataCollection3VO> dataCollectionList, AutoProcShellWrapper wrapper, String proposalCode,
			String proposalNumber, HttpServletRequest request, boolean withGraph) throws Exception {
		String result = "";
		if (info.getResult() != null && !info.getResult().isEmpty()) {
			result = info.getResult() + "<br />";
		}
		String resultStatus = "";
		List<Param> listResults = new ArrayList<Param>();
		if (info.getListResults() != null && info.getListResults().size() > 0) {
			listResults = info.getListResults();
		}

		DecimalFormat df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df2.applyPattern("#####0.00");

		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		AutoProcProgramAttachment3Service autoProcProgramAttachmentService = (AutoProcProgramAttachment3Service) ejb3ServiceLocator
				.getLocalService(AutoProcProgramAttachment3Service.class);

		Integer idDc = getDataCollectionIndex(dataCollectionList, dataCollection.getDataCollectionId());
		if (idDc != -1) {
			AutoProc3VO[] autoProcs = wrapper.getAutoProcs();
			AutoProcScalingStatistics3VO[] autoProcsOverall = wrapper.getScalingStatsOverall();
			AutoProcScalingStatistics3VO[] autoProcsInner = wrapper.getScalingStatsInner();
			AutoProcScalingStatistics3VO[] autoProcsOuter = wrapper.getScalingStatsOuter();

			AutoProc3VO autoProcValue = autoProcs[idDc];
			AutoProcScalingStatistics3VO autoProcOverall = autoProcsOverall[idDc];
			AutoProcScalingStatistics3VO autoProcInner = autoProcsInner[idDc];
			AutoProcScalingStatistics3VO autoProcOuter = autoProcsOuter[idDc];

			if (autoProcValue == null) {
				result += "No autoprocessing results found";
			} else {
				// best autoProc , find the autoprocAttachment xscale.lp
				List<AutoProcProgramAttachment3VO> listAutoProcProgramAttachment = autoProcProgramAttachmentService
						.findXScale(autoProcValue.getAutoProcProgramVOId());
				if (listAutoProcProgramAttachment != null && listAutoProcProgramAttachment.size() > 0) {
					AutoProcProgramAttachment3VO xscaleAtt = listAutoProcProgramAttachment.get(0);
					// o[0] is a boolean xscaleFile
					// o[1] is a boolean truncateLog
					// o[2] is List<AutoProcessingData>
					try {
						Object[] o = ViewResultsAction.readAttachment(xscaleAtt);
						boolean xscaleFile = (Boolean) o[0];
						// boolean truncateLog = (Boolean)o[1];
						List<AutoProcessingData> listAutoProcessingData = (List<AutoProcessingData>) o[2];
						if (xscaleFile && withGraph) {
							info.setGraphData(listAutoProcessingData);
							info.setGraphPath(request.getRealPath(Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH
									+ proposalCode + proposalNumber + "_chart_"
									+ xscaleAtt.getAutoProcProgramAttachmentId() + ".png"));
						}

					} catch (Exception e) {
						LOG.debug("Error while reading file for " + xscaleAtt.getAutoProcProgramAttachmentId());
						e.printStackTrace();
					}
				}

				// status for autoprocessing
				String imgFailed = "../images/Sphere_Red_12.png";
				String imgSuccess = "../images/Sphere_Green_12.png";

				// edna
				if (dcInfo != null && dcInfo.getAutoProcEdnaStatus() != null) {
					String img = imgFailed;
					if (dcInfo.getAutoProcEdnaStatus().contains("Green")) {
						img = imgSuccess;
					}
					result += "EDNA_proc " + "<img src='" + img + "'  border='0' />";
				}

				if (Constants.SITE_IS_ESRF() || Constants.SITE_IS_SOLEIL()) {
					// fastproc
					if (dcInfo != null && dcInfo.getAutoProcFastStatus() != null) {
						String img = imgFailed;
						if (dcInfo.getAutoProcFastStatus().contains("Green")) {
							img = imgSuccess;
						}
						result += " grenades_fastproc " + "<img src='" + img + "'  border='0' />";
					}

					// parallelproc
					if (dcInfo != null && dcInfo.getAutoProcParallelStatus() != null) {
						String img = imgFailed;
						if (dcInfo.getAutoProcParallelStatus().contains("Green")) {
							img = imgSuccess;
						}
						result += " grenades_parallelproc " + "<img src='" + img + "'  border='0' />";
					}
				}
				
				if (Constants.SITE_IS_ESRF() || Constants.SITE_IS_EMBL() || Constants.SITE_IS_MAXIV()
						|| Constants.SITE_IS_ALBA()) {
					// Only show autoPROC status if successful
					if (dcInfo.getAutoProcAutoPROCStatus().contains("Green")) {
						result += " autoPROC " + "<img src='" + imgSuccess + "'  border='0' />";					
					}
					// Only show XIA2_DIALS status if successful
					if (dcInfo.getAutoProcXia2DialsStatus().contains("Green")) {
						result += " XIA2_DIALS " + "<img src='" + imgSuccess + "'  border='0' />";					
					}
				}

				if (Constants.SITE_IS_MAXIV()) {
					// Only show FAST DP status if successful
					if (dcInfo.getAutoProcFastDPStatus().contains("Green")) {
						result += " FASTDP " + "<img src='" + imgSuccess + "'  border='0' />";
					}
				}

				listResults.add(new Param(KEY_SPACE_GROUP, "Space Group", autoProcValue.getSpaceGroup(), false));
				// completeness, rsymm, processed resolution
				String completenessString = new String();
				String rSymmString = new String();
				String resolutionString = new String();
				if (autoProcOverall != null && autoProcInner != null && autoProcOuter != null) {
					completenessString += (autoProcInner.getCompleteness() == null ? "" : df2.format(autoProcInner.getCompleteness())) + ","
							+ (autoProcOuter.getCompleteness() == null ? "" : df2.format(autoProcOuter.getCompleteness())) + ","
							+ (autoProcOverall.getCompleteness() == null ? "" : df2.format(autoProcOverall.getCompleteness()));
					rSymmString += (autoProcInner.getRmerge() == null ? "" : df2.format(autoProcInner.getRmerge()))
							+ "<br/>"
							+ (autoProcOuter.getRmerge() == null ? "" : df2.format(autoProcOuter.getRmerge()))
							+ "<br/>"
							+ (autoProcOverall.getRmerge() == null ? "" : df2.format(autoProcOverall.getRmerge()));
					resolutionString += autoProcInner.getResolutionLimitLow() + " - "
							+ autoProcInner.getResolutionLimitHigh() + "<br/>" + autoProcOuter.getResolutionLimitLow()
							+ " - " + autoProcOuter.getResolutionLimitHigh() + "<br/>"
							+ autoProcOverall.getResolutionLimitLow() + " - "
							+ autoProcOverall.getResolutionLimitHigh();
				}
				// unit cell
				String unitCell = "";
				if (autoProcValue != null && autoProcValue.getSpaceGroup() != null) {
					unitCell = autoProcValue.getRefinedCellA() + ", " + autoProcValue.getRefinedCellB() + ", "
							+ autoProcValue.getRefinedCellC() + " <br/>" + autoProcValue.getRefinedCellAlpha() + ", "
							+ autoProcValue.getRefinedCellBeta() + ", " + autoProcValue.getRefinedCellGamma();
				}
				listResults.add(new Param(KEY_COMPLETENESS, "Completeness", completenessString, false));
				listResults.add(new Param(KEY_RESOLUTION, "Resolution", resolutionString, true));
				listResults.add(new Param(KEY_RSYMM, "Rsymm", rSymmString, true));
				listResults.add(new Param(KEY_UNIT_CELL, "Unit cell", unitCell, true));
			}
		}

		info.setResult(result);
		info.setResultStatus(resultStatus);

		info.setListResults(listResults);

		return info;

	}

	/**
	 * save comments and crystal class
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void saveAllData(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String comments;
			String crystalClass = null;
			String proposalCode = (String) request.getSession().getAttribute(Constants.PROPOSAL_CODE);
			String dataList = request.getParameter("listData");
			Integer nbOfItems = null;
			if (request.getParameter("nbOfItems") != null) {
				nbOfItems = new Integer(request.getParameter("nbOfItems"));
			}
			Gson gson = GSonUtils.getGson("dd-MM-yyyy");
			Type mapType = new TypeToken<ArrayList<SessionDataObjectInformation>>() {
			}.getType();
			ArrayList<SessionDataObjectInformation> sessionObjects = gson.fromJson(dataList, mapType);
			if (sessionObjects != null) {
				int nb = sessionObjects.size();
				for (int i = 0; i < nb; i++) {
					SessionDataObjectInformation o = sessionObjects.get(i);
					if (o.isEnergyScan()) {
						EnergyScan3VO oFromDB = energyScanService.findByPk(o.getEnergyScan().getEnergyScanId());
						comments = o.getComments();
						if (comments == null) {
							comments = "";
						}
						oFromDB.setComments(comments);
						if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX)) {
							crystalClass = o.getCrystalClass();
							oFromDB.setCrystalClass(crystalClass);
						}
						energyScanService.update(oFromDB);
						LOG.debug("update energyScan: ID=" + o.getEnergyScan().getEnergyScanId() + "   COMMENTS="
								+ comments);
					} else if (o.isXRFSpectra()) {
						XFEFluorescenceSpectrum3VO oFromDB = xfeService.findByPk(o.getXrfSpectra()
								.getXfeFluorescenceSpectrumId());
						comments = o.getComments();
						if (comments == null) {
							comments = "";
						}
						oFromDB.setComments(comments);
						if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX)) {
							crystalClass = o.getCrystalClass();
							oFromDB.setCrystalClass(crystalClass);
						}
						xfeService.update(oFromDB);
						LOG.debug("update xrfSpectra: ID=" + o.getXrfSpectra().getXfeFluorescenceSpectrumId()
								+ "   COMMENTS=" + comments);
					} else if (o.isDataCollection()) {
						DataCollection3VO oFromDB = dataCollectionService.findByPk(o.getDataCollection()
								.getDataCollectionId(), false, false);
						comments = o.getComments();
						if (comments != null) {
							oFromDB.setComments(comments);
							dataCollectionService.update(oFromDB);
						}

						DataCollectionGroup3VO dcGroupVO = oFromDB.getDataCollectionGroupVO();
						if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX)) {
							crystalClass = o.getCrystalClass();
							dcGroupVO.setCrystalClass(crystalClass);
						}
						dataCollectionGroupService.update(dcGroupVO);
						LOG.debug("update dataCollectionGroup: ID="
								+ o.getDataCollection().getDataCollectionGroupVOId() + "   COMMENTS=" + comments);
					} else if (o.isDataCollectionGroup()) {
						DataCollectionGroup3VO oFromDB = dataCollectionGroupService.findByPk(o.getDataCollectionGroup()
								.getDataCollectionGroupId(), true, false);
						comments = o.getComments();
						if (comments == null) {
							comments = "";
						}
						oFromDB.setComments(comments);

						if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX)) {
							crystalClass = o.getCrystalClass();
							oFromDB.setCrystalClass(crystalClass);
						}
						dataCollectionGroupService.update(oFromDB);
						LOG.debug("update dataCollectionGroup: ID="
								+ o.getDataCollectionGroup().getDataCollectionGroupId() + "   COMMENTS=" + comments);

					} else if (o.isWorkflow()) {
						List<DataCollectionGroup3VO> listGroup = dataCollectionGroupService.findByWorkflow(o
								.getWorkflow().getWorkflowId());
						DataCollectionGroup3VO oFromDB = null;
						if (listGroup != null && listGroup.size() > 0) {
							oFromDB = listGroup.get(0);
						}
						comments = o.getComments();
						if (comments == null) {
							comments = "";
						}
						if (oFromDB != null) {
							oFromDB.setComments(comments);
						}
						if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX)) {
							crystalClass = o.getCrystalClass();
							oFromDB.setCrystalClass(crystalClass);
						}
						dataCollectionGroupService.update(oFromDB);
						LOG.debug("update wf dataCollectionGroup: ID=" + oFromDB.getDataCollectionGroupId()
								+ "   COMMENTS=" + comments);
					}
				}

			}
			getSessionSummaryList(mapping, actForm, request, response, nbOfItems);
		} catch (Exception exp) {
			exp.printStackTrace();
			response.getWriter().write(GSonUtils.getErrorMessage(exp));
			response.setStatus(500);
		}
	}

	private static void setSampleAndProteinNames(SessionDataObjectInformation info, BLSample3VO sampleVO){
		
		String proteinAcronym = "";
		String sampleName = "";
	
		sampleName = sampleVO.getName();
		if (sampleVO.getCrystalVO() != null
					&& sampleVO.getCrystalVO().getProteinVO() != null) {
				proteinAcronym = sampleVO.getCrystalVO()
						.getProteinVO().getAcronym();
		}
		
		info.setProteinAcronym(proteinAcronym);
		info.setSampleName(sampleName);
		String sampleNameProtein = "";
		if (proteinAcronym != null && sampleName != null && !proteinAcronym.isEmpty() && !sampleName.isEmpty()) {
			sampleNameProtein = proteinAcronym + "-" + sampleName;
		}
		info.setSampleNameProtein(sampleNameProtein);
	}
	public void changeCurrentPageNumber(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		String pageNumber = request.getParameter("sessionReportCurrentPageNumber");
		request.getSession().setAttribute("sessionReportCurrentPageNumber", pageNumber);
		request.getSession().setAttribute("forSessionId", request.getParameter("sessionId"));
	}	
}
