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
 * 
 * 
 */
package ispyb.client.mx.results;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.DataAdapterCommon;
import ispyb.client.common.util.Confidentiality;
import ispyb.client.common.util.FileUtil;
import ispyb.client.common.util.FileUtil.FileCleaner;
import ispyb.client.common.util.GSonUtils;
import ispyb.client.common.util.UrlUtils;
import ispyb.client.mx.collection.AutoProcShellWrapper;
import ispyb.client.mx.collection.DataCollectionBean;
import ispyb.client.mx.collection.ViewDataCollectionAction;
import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.common.util.HashMapToZip;
import ispyb.common.util.PathUtils;
import ispyb.common.util.StringUtils;
import ispyb.common.util.beamlines.ESRFBeamlineEnum;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.autoproc.AutoProc3Service;
import ispyb.server.mx.services.autoproc.AutoProcIntegration3Service;
import ispyb.server.mx.services.autoproc.AutoProcProgram3Service;
import ispyb.server.mx.services.autoproc.AutoProcProgramAttachment3Service;
import ispyb.server.mx.services.autoproc.AutoProcScalingStatistics3Service;
import ispyb.server.mx.services.collections.BeamLineSetup3Service;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.services.collections.DataCollectionGroup3Service;
import ispyb.server.mx.services.collections.Image3Service;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.services.screening.ScreeningRankSet3Service;
import ispyb.server.mx.services.screening.ScreeningStrategy3Service;
import ispyb.server.mx.services.screening.ScreeningStrategySubWedge3Service;
import ispyb.server.mx.services.screening.ScreeningStrategyWedge3Service;
import ispyb.server.mx.vos.autoproc.AutoProc3VO;
import ispyb.server.mx.vos.autoproc.AutoProcIntegration3VO;
import ispyb.server.mx.vos.autoproc.AutoProcProgramAttachment3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScalingStatistics3VO;
import ispyb.server.mx.vos.autoproc.AutoProcStatus3VO;
import ispyb.server.mx.vos.autoproc.IspybAutoProcAttachment3VO;
import ispyb.server.mx.vos.collections.BeamLineSetup3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.Detector3VO;
import ispyb.server.mx.vos.collections.Image3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.Protein3VO;
import ispyb.server.mx.vos.screening.Screening3VO;
import ispyb.server.mx.vos.screening.ScreeningOutput3VO;
import ispyb.server.mx.vos.screening.ScreeningOutputLattice3VO;
import ispyb.server.mx.vos.screening.ScreeningRank3VO;
import ispyb.server.mx.vos.screening.ScreeningRankSet3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategy3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategySubWedge3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategyWedge3VO;

/**
 * Clss to handle results from beamline datacollections
 * 
 * @struts.action name="viewResultsForm" path="/user/viewResults" input="user.collection.viewDataCollection.page" validate="false"
 *                parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * @struts.action-forward name="success" path="user.results.view.page"
 * @struts.action-forward name="managerResultsViewPage" path="manager.results.view.page"
 * @struts.action-forward name="fedexmanagerResultsViewPage" path="fedexmanager.results.view.page"
 * @struts.action-forward name="localcontactResultsViewPage" path="localcontact.results.view.page"
 * @struts.action-forward name="viewImageApplet" path="user.results.viewImageApplet.page"
 * @struts.action-forward name="viewDNAImages" path="user.results.DNAImages.page" *
 * @struts.action-forward name="viewEDNAPages" path="user.results.EDNAPages.page"
 * @struts.action-forward name="viewEDNAImage" path = "user.results.EDNAImage.page"
 * @struts.action-forward name="viewDNAFiles" path="user.results.DNAFiles.page"
 * @struts.action-forward name="viewOtherDNAFiles" path="user.results.otherDNAFiles.page"
 * @struts.action-forward name="viewDNATextFiles" path="user.results.DNATextFiles.page"
 * @struts.action-forward name="viewDenzoImages" path="user.results.DenzoImages.page"
 * @struts.action-forward name="viewJpegImage" path="user.results.viewJpegImage.page"
 * @struts.action-forward name="localcontactviewJpegImage" path="localcontact.results.viewJpegImage.page"
 * @struts.action-forward name="managerviewJpegImage" path="manager.results.viewJpegImage.page"
 * @struts.action-forward name="viewTextFile" path="user.results.textFile.page"
 * @struts.action-forward name="viewImageThumbnails" path="user.results.viewImageThumbnails.page"
 * @struts.action-forward name="viewAutoProcAttachments" path="user.results.AutoProcAttachments.page"
 * @struts.action-forward name="viewHTMLFile" path="user.results.HTMLFile.page"
 */
public class ViewResultsAction extends DispatchAction {

	private String dataCollectionIdst;

	private String screeningRankSetIdst;

	ActionMessages errors = new ActionMessages();

	private static final String DENZO_HTML_INDEX = "index.html";

	private static final int SNAPSHOT_EXPECTED_NUMBER = 4;

	protected static final String EDNA_FILES_INDEX_FILE = Constants.SITE_IS_DLS() ? "summary.html" : "index_edna.html";

	private final static Logger LOG = Logger.getLogger(ViewResultsAction.class);
	
	private final static String TMP_DIR = "tmp";
	
	private final double DEFAULT_RMERGE = 50.0;

	private final double DEFAULT_ISIGMA = 1.0;

	// TODO erase this method when DNA no more used, only EDNA and index file name is changed
	protected static String getEdna_index_file(DataCollection3VO dataCollectionVO) throws Exception {
		String fullDNAPath = PathUtils.getFullDNAPath(dataCollectionVO);

		if (new File(fullDNAPath + EDNA_FILES_INDEX_FILE).exists())
			return EDNA_FILES_INDEX_FILE;
		else
			return Constants.DNA_FILES_INDEX_FILE;
	}

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private AutoProc3Service apService;

	private AutoProcProgramAttachment3Service appaService;

	private AutoProcProgram3Service appService;

	private AutoProcScalingStatistics3Service apssService;

	private AutoProcIntegration3Service autoProcIntegrationService;

	private Session3Service sessionService;

	private BLSample3Service sampleService;

	private BeamLineSetup3Service beamLineSetupService;

	private DataCollection3Service dataCollectionService;
	
	private DataCollectionGroup3Service dataCollectionGroupService;

	private Image3Service imageService;

	private ScreeningRankSet3Service screeningRankSetService;

	private ScreeningStrategy3Service screeningStrategyService;

	private ScreeningStrategyWedge3Service screeningStrategyWedgeService;

	private ScreeningStrategySubWedge3Service screeningStrategySubWedgeService;

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {

		this.sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
		this.sampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
		this.beamLineSetupService = (BeamLineSetup3Service) ejb3ServiceLocator.getLocalService(BeamLineSetup3Service.class);
		this.dataCollectionService = (DataCollection3Service) ejb3ServiceLocator.getLocalService(DataCollection3Service.class);
		this.dataCollectionGroupService = (DataCollectionGroup3Service) ejb3ServiceLocator.getLocalService(DataCollectionGroup3Service.class);

		this.imageService = (Image3Service) ejb3ServiceLocator.getLocalService(Image3Service.class);
		this.screeningRankSetService = (ScreeningRankSet3Service) ejb3ServiceLocator.getLocalService(ScreeningRankSet3Service.class);
		this.screeningStrategyService = (ScreeningStrategy3Service) ejb3ServiceLocator
				.getLocalService(ScreeningStrategy3Service.class);
		this.screeningStrategyWedgeService = (ScreeningStrategyWedge3Service) ejb3ServiceLocator
				.getLocalService(ScreeningStrategyWedge3Service.class);
		this.screeningStrategySubWedgeService = (ScreeningStrategySubWedge3Service) ejb3ServiceLocator
				.getLocalService(ScreeningStrategySubWedge3Service.class);
		this.apService = (AutoProc3Service) ejb3ServiceLocator.getLocalService(AutoProc3Service.class);
		this.appaService = (AutoProcProgramAttachment3Service) ejb3ServiceLocator
				.getLocalService(AutoProcProgramAttachment3Service.class);
		this.appService = (AutoProcProgram3Service) ejb3ServiceLocator.getLocalService(AutoProcProgram3Service.class);
		this.apssService = (AutoProcScalingStatistics3Service) ejb3ServiceLocator
				.getLocalService(AutoProcScalingStatistics3Service.class);
		this.autoProcIntegrationService = (AutoProcIntegration3Service) ejb3ServiceLocator
				.getLocalService(AutoProcIntegration3Service.class);
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	/**
	 * To display all the parameters linked to a dataCollectionId.
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse in_reponse) {

		ActionMessages errors = new ActionMessages();
		boolean redirectToError = true;
		boolean displayOutputParam = false;

		try {

			dataCollectionIdst = request.getParameter(Constants.DATA_COLLECTION_ID);
			Integer dataCollectionId = null;
			if (dataCollectionIdst != null) {
				try {
					dataCollectionId = new Integer(dataCollectionIdst);
				} catch (NumberFormatException e) {

				}
			}
			if (dataCollectionId == null && BreadCrumbsForm.getIt(request).getSelectedDataCollection() != null) {
				dataCollectionId = BreadCrumbsForm.getIt(request).getSelectedDataCollection().getDataCollectionId();
			}

			request.getSession().setAttribute(Constants.DATA_COLLECTION_ID, dataCollectionId);
			ViewResultsForm form = (ViewResultsForm) actForm;
			form.setDataCollectionId(dataCollectionId);

			DataCollection3VO dc = dataCollectionService.findByPk(dataCollectionId, false, true);
			DataCollectionGroup3VO dataCollectionGroup = null;
			if (dc != null) {
				dataCollectionGroup = dataCollectionGroupService.findByPk(dc.getDataCollectionGroupVOId(), false, true);
			}

			Screening3VO[] screeningList = null;

			Integer sessionId = null;
			if (dataCollectionGroup != null)
				sessionId = new Integer(dataCollectionGroup.getSessionVOId());
			Session3VO sessionlv = null;
			if (sessionId != null)
				sessionlv = sessionService.findByPk(sessionId, false, false, false);
			form.setSession(sessionlv);

			if (sessionlv == null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "No session retrieved"));
				saveErrors(request, errors);
				return (mapping.findForward("error"));
			}

			// Confidentiality (check if object proposalId and session proposalId match)
			if (!Confidentiality.isAccessAllowed(request, sessionlv.getProposalVO().getProposalId())) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Access denied"));
				saveErrors(request, errors);
				return (mapping.findForward("error"));
			}

			if (sessionlv.getBeamLineSetupVOId() != null && sessionlv.getBeamLineSetupVOId().intValue() != 0) {
				Integer beamLineId = new Integer(sessionlv.getBeamLineSetupVOId());
				BeamLineSetup3VO beamLinelv = beamLineSetupService.findByPk(beamLineId);
				form.setBeamLine(beamLinelv);
				form.setUndulatorTypes(beamLinelv.getUndulatorType1(), beamLinelv.getUndulatorType2(), beamLinelv.getUndulatorType3());
			}

			String rMerge = request.getParameter(Constants.RSYMM);
			String iSigma = request.getParameter(Constants.ISIGMA);
			request.getSession().setAttribute(Constants.RSYMM, rMerge);
			request.getSession().setAttribute(Constants.ISIGMA, iSigma);

			Screening3VO[] screenings = dataCollectionGroup.getScreeningsTab(); 

			if (screenings.length > 0) {

				displayOutputParam = true;// there is at least 1 screening so we display the output params

				int length = screenings.length;
				screeningList = screenings;
				// if many screenings, only use the last one

				ScreeningRank3VO srlv = new ScreeningRank3VO();
				ScreeningOutput3VO sov = new ScreeningOutput3VO();
				ScreeningRankSet3VO srsv = new ScreeningRankSet3VO();
				ScreeningOutputLattice3VO solav = new ScreeningOutputLattice3VO();

				ScreeningStrategy3VO[] screeningStrategyList;
				List<ScreeningStrategyValueInfo> screeningInfoList = new ArrayList<ScreeningStrategyValueInfo>();

				// ScreeningValue sv = screening.findByPrimaryKey(screeningId);
				Screening3VO sv = screeningList[length - 1];
				// sv = screeningService.loadEager(sv);

				ScreeningRank3VO[] screeningRanks = sv.getScreeningRanksTab();
				if (screeningRanks.length > 0) {
					srlv = screeningRanks[0];
					srsv = srlv.getScreeningRankSetVO();
				}

				ScreeningOutput3VO[] screeningOutputs = sv.getScreeningOutputsTab();
				if (screeningOutputs.length > 0) {
					sov = screeningOutputs[0];
					// sov = screeningOutputService.loadEager(sov);
				}

				ScreeningOutputLattice3VO[] screeningOutputLattices = sov.getScreeningOutputLatticesTab();
				if (screeningOutputLattices != null && screeningOutputLattices.length > 0) {

					solav = screeningOutputLattices[0];
					form.setScreeningOutputLattice(solav);
				}
				ScreeningStrategy3VO[] screeningStrategys = sov.getScreeningStrategysTab();
				if (screeningStrategys.length > 0) {

					screeningStrategyList = screeningStrategys;
					List<ScreeningStrategyWedge3VO> wedgeList = new ArrayList<ScreeningStrategyWedge3VO>();

					for (int j = 0; j < screeningStrategyList.length; j++) {
						ScreeningStrategy3VO ss = screeningStrategyService.findByPk(screeningStrategyList[j].getScreeningStrategyId(),
								true);
						ScreeningStrategyValueInfo ssvi = new ScreeningStrategyValueInfo(ss);
						ssvi.setProgramLog(dc);
						screeningInfoList.add(ssvi);

						ArrayList<ScreeningStrategyWedge3VO> list = ss.getScreeningStrategyWedgesList();
						if (list != null)
							wedgeList.addAll(list);
					}
					form.setScreeningStrategyList(screeningStrategyList);
					form.setListStrategiesInfo(screeningInfoList);

					// strategy wedge
					ScreeningStrategyWedge3VO[] screeningStrategyWedgeList;
					List<ScreeningStrategyWedgeValueInfo> screeningWedgeInfoList;

					int nb = wedgeList.size();
					screeningStrategyWedgeList = new ScreeningStrategyWedge3VO[nb];
					for (int j = 0; j < nb; j++) {
						screeningStrategyWedgeList[j] = wedgeList.get(j);
					}

					screeningWedgeInfoList = new ArrayList<ScreeningStrategyWedgeValueInfo>();
					for (int k = 0; k < screeningStrategyWedgeList.length; k++) {
						ScreeningStrategyWedgeValueInfo sw = new ScreeningStrategyWedgeValueInfo(
								screeningStrategyWedgeService.findByPk(screeningStrategyWedgeList[k].getScreeningStrategyWedgeId(),
										true));
						screeningWedgeInfoList.add(sw);
					}
					form.setScreeningStrategyWedgeList(screeningStrategyWedgeList);
					form.setListStrategiesWedgeInfo(screeningWedgeInfoList);

					// strategy sub wedge
					ScreeningStrategySubWedge3VO[][] screeningStrategySubWedgeListAll;
					List<ScreeningStrategySubWedgeValueInfo>[] screeningSubWedgeInfoListAll;
					screeningStrategySubWedgeListAll = new ScreeningStrategySubWedge3VO[screeningStrategyWedgeList.length][];
					screeningSubWedgeInfoListAll = new ArrayList[screeningStrategyWedgeList.length];
					for (int j = 0; j < screeningStrategyWedgeList.length; j++) {
						ScreeningStrategySubWedge3VO[] screeningStrategysSubWedge = screeningStrategyWedgeService.findByPk(
								screeningStrategyWedgeList[j].getScreeningStrategyWedgeId(), true).getScreeningStrategySubWedgesTab();

						screeningStrategySubWedgeListAll[j] = screeningStrategysSubWedge;
						screeningSubWedgeInfoListAll[j] = new ArrayList<ScreeningStrategySubWedgeValueInfo>();
						if (screeningStrategySubWedgeListAll[j] != null) {
							for (int k = 0; k < screeningStrategySubWedgeListAll[j].length; k++) {
								ScreeningStrategySubWedgeValueInfo ssw = new ScreeningStrategySubWedgeValueInfo(
										screeningStrategySubWedgeService.findByPk(screeningStrategySubWedgeListAll[j][k]
												.getScreeningStrategySubWedgeId()));
								screeningSubWedgeInfoListAll[j].add(ssw);
							}
						}
					}
					form.setScreeningStrategySubWedgeListAll(screeningStrategySubWedgeListAll);
					form.setListStrategiesSubWedgeInfoAll(screeningSubWedgeInfoListAll);

					// strategy sub wedge
					String screeningStrategyWedgeSelIdst = request.getParameter("screeningStrategyWedgeSel");
					Integer screeningStrategyWedgeSelId = -1;
					try {
						screeningStrategyWedgeSelId = new Integer(screeningStrategyWedgeSelIdst);
					} catch (NumberFormatException ex) {

					}
					ScreeningStrategySubWedge3VO[] screeningStrategySubWedgeList;
					screeningStrategySubWedgeList = new ScreeningStrategySubWedge3VO[0];
					List<ScreeningStrategySubWedgeValueInfo> screeningSubWedgeInfoList;
					screeningSubWedgeInfoList = new ArrayList<ScreeningStrategySubWedgeValueInfo>();
					if (screeningStrategyWedgeSelId != -1) {
						ScreeningStrategySubWedge3VO[] screeningStrategysSubWedge = new ScreeningStrategyWedgeValueInfo(
								screeningStrategyWedgeService.findByPk(
										screeningStrategyWedgeList[screeningStrategyWedgeSelId].getScreeningStrategyWedgeId(), true))
								.getScreeningStrategySubWedgesTab();

						screeningStrategySubWedgeList = screeningStrategysSubWedge;
						for (int k = 0; k < screeningStrategySubWedgeList.length; k++) {
							ScreeningStrategySubWedgeValueInfo sw = new ScreeningStrategySubWedgeValueInfo(
									screeningStrategySubWedgeService.findByPk(screeningStrategySubWedgeList[k]
											.getScreeningStrategySubWedgeId()));
							screeningSubWedgeInfoList.add(sw);
						}
					}
					form.setScreeningStrategySubWedgeList(screeningStrategySubWedgeList);
					form.setListStrategiesSubWedgeInfo(screeningSubWedgeInfoList);

				}

				// Populate form
				form.setScreeningRank(srlv);
				form.setScreeningRankSet(srsv);
				form.setScreeningOutput(sov);

			}
			if (dataCollectionGroup.getBlSampleVOId() != null && dataCollectionGroup.getBlSampleVOId().intValue() != 0) {
				BLSample3VO bslv = sampleService.findByPk(dataCollectionGroup.getBlSampleVOId(), false, false, false);
				BreadCrumbsForm.getIt(request).setSelectedSample(bslv);

				Crystal3VO clv = bslv.getCrystalVO();

				Protein3VO plv = clv.getProteinVO();

				form.setCrystal(clv);
				form.setProtein(plv);
				form.setSample(bslv);

			} else {
				BreadCrumbsForm.getIt(request).setSelectedSample(null);
			}
			// --- Get Image List
			LOG.debug("get image list");
			List<ImageValueInfo> imageList = FileUtil.GetImageList(dataCollectionId, null, null, null, null, request);
			LOG.debug("get image list done");

			// Snapshot Image present ?
			// DataCollection3VO dcValue = dataCollectionService.findByPk(dataCollectionId, false, false, false, false);
			List<SnapshotInfo> listSnapshots = FileUtil.GetFullSnapshotPath(dc);
			String expectedSnapshotPath = (listSnapshots.get(SNAPSHOT_EXPECTED_NUMBER)).getFileLocation();
			form.setListSnapshots(listSnapshots);
			form.setExpectedSnapshotPath(expectedSnapshotPath);

			// --- Populate Form ---

			form.setDisplayOutputParam(displayOutputParam);

			form.setDataCollectionId(dataCollectionId);
			form.setDataCollection(dc);
			form.setListInfo(imageList);
			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

			// Fill the BreadCrumbs
			BreadCrumbsForm.getIt(request).setSelectedImage(null);
			BreadCrumbsForm.getIt(request).setSelectedDataCollection(dc);
			BreadCrumbsForm.getIt(request).setSelectedDataCollectionGroup(dc.getDataCollectionGroupVO());
			if (dc.getDataCollectionGroupVO().getWorkflowVO() != null) {
				BreadCrumbsForm.getIt(request).setSelectedWorkflow(dc.getDataCollectionGroupVO().getWorkflowVO());
			}

			displayEDNA(mapping, actForm, request, in_reponse, errors);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.view"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty() && redirectToError) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		} else {
			// return mapping.findForward("success");
			RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
			String role = roleObject.getName();
			if (role.equals(Constants.FXMANAGE_ROLE_NAME)) {
				return mapping.findForward("fedexmanagerResultsViewPage");
			} else if (role.equals(Constants.ROLE_MANAGER)) {
				return mapping.findForward("managerResultsViewPage");
			} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
				return mapping.findForward("localcontactResultsViewPage");
			} else {
				return mapping.findForward("success");
			}
		}
	}

	/**
	 * forward to the page with autoproc results
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 * 
	 *         If the filename ends in .lp or .log we display it inline, else it is offered as a download
	 */
	public ActionForward displayAutoProcProgAttachment(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();
		ViewResultsForm form = (ViewResultsForm) actForm;
		Integer autoProcProgramAttachmentId;
		try {
			autoProcProgramAttachmentId = Integer.parseInt(form.getAutoProcProgramAttachmentId());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewAutoProcAttachments"));
			return this.display(mapping, actForm, request, response);
		}

		String filename = "";
		String fullFilePath = "";

		try {

			AutoProcProgramAttachment3VO appalv = appaService.findByPk(autoProcProgramAttachmentId);

			filename = appalv.getFileName();
			String filetype = filename.substring(filename.lastIndexOf('.'));

			fullFilePath = appalv.getFilePath().trim() + "/" + filename;
			fullFilePath = PathUtils.FitPathToOS(fullFilePath);

			if (filetype.toUpperCase().equals(new String(".log").toUpperCase())
					|| filetype.toUpperCase().equals(new String(".lp").toUpperCase())) { // display inline
				// inline
				String outFile = FileUtil.fileToString(fullFilePath);

				if (outFile == null || outFile.equals("") || outFile.equals("nofile")) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Unable to open file: "
							+ fullFilePath));
					saveErrors(request, errors);
					return mapping.findForward("error");
				}
				Integer dataCollectionId = form.getDataCollectionId();
				if (BreadCrumbsForm.getIt(request).getSelectedDataCollection() != null) {
					dataCollectionId = BreadCrumbsForm.getIt(request).getSelectedDataCollection().getDataCollectionId();
				}
				form.setAutoProcAttachmentContent(outFile);
				form.setAutoProcAttachmentName(filename);
				form.setAutoProcProgramAttachmentId(appalv.getAutoProcProgramAttachmentId().toString());
				form.setDataCollectionId(dataCollectionId);

				return mapping.findForward("viewAutoProcAttachments");

			} else { // offer for download

				// catch the file not found error to change the name of the file accordingly
				// xxx.mtz.gz becomes xxx.mtz or xxx.mtz becomes xxx.mtz.gz
				// xxx.sca.gz becomes xxx.sca or xxx.sca becomes xxx.sca.gz
				HashMap<String, String> filesToZip = new HashMap<String, String>();
				File f;
				String finalFilePath= fullFilePath;	
				try {
					f = new File(fullFilePath);
					if (!f.canRead()) {
						String otherPath = fullFilePath;
						if (filename.contains(".mtz")) {
							if (filename.contains(".mtz.gz")) {
								otherPath = fullFilePath.substring(0, fullFilePath.length() - 3);
								filename = filename.substring(0, filename.length() - 3);
							} else {
								otherPath = fullFilePath + ".gz";
								filename = filename + ".gz";
							}

						} else if (filename.contains(".sca")) {
							if (filename.contains(".sca.gz")) {
								otherPath = fullFilePath.substring(0, fullFilePath.length() - 3);
								filename = filename.substring(0, filename.length() - 3);
							} else {
								otherPath = fullFilePath + ".gz";
								filename = filename + ".gz";
							}
						}
						finalFilePath= otherPath;	
						f = new File(otherPath);						
					}
					
					if (f.canRead()) {
						
						filesToZip.put(f.getName(), finalFilePath);		
		
						String info = "";
						if (filename != null && !filename.contains("_run")) {
							Integer dataCollectionId = form.getDataCollectionId();
							if (BreadCrumbsForm.getIt(request).getSelectedDataCollection() != null) {
								dataCollectionId = BreadCrumbsForm.getIt(request).getSelectedDataCollection().getDataCollectionId();
							}
							if (dataCollectionId != null) {
								DataCollection3VO dc = dataCollectionService.findByPk(dataCollectionId, false, false);
								if (dc != null) {
									info = dc.getImagePrefix() + "_run" + dc.getDataCollectionNumber() + "_";
								}
							}
						}
										
						String newfilename = info + filename + ".zip";
						byte[] zippedFiles = HashMapToZip.doZip(filesToZip);
		
						response.setContentType("application/zip");
						response.setHeader("Content-Disposition", "attachment;filename=" + newfilename);
						OutputStream output;
						try {
							output = response.getOutputStream();
							output.write(zippedFiles);
							output.close();				
						} catch (java.io.IOException e) {
								errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Unable to open file: " + fullFilePath));
								saveErrors(request, errors);
								e.printStackTrace();
								return mapping.findForward("error");								
						}
					}
							
				} catch (Exception e) {
						e.printStackTrace();
						return mapping.findForward("error");
				}
			}
				
			} catch (Exception e) {
				e.printStackTrace();
				return mapping.findForward("error");
		}
		return null;
	}

	public ActionForward displayCorrectionFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();

		String filename = "";
		String fullFilePath = "";

		try {
			if (request.getParameter("fileName") != null)
				filename = new String(request.getParameter("fileName"));
			Integer dataCollectionId = null;
			if (BreadCrumbsForm.getIt(request).getSelectedDataCollection() != null)
				dataCollectionId = BreadCrumbsForm.getIt(request).getSelectedDataCollection().getDataCollectionId();
			else {
				dataCollectionId = new Integer(request.getParameter(Constants.SESSION_ID));
			}
			if (dataCollectionId != null) {
				DataCollection3VO dataCollection = dataCollectionService.findByPk(dataCollectionId, false, false);
				String beamLineName = dataCollection.getDataCollectionGroupVO().getSessionVO().getBeamlineName();
				String[] correctionFiles = ESRFBeamlineEnum.retrieveCorrectionFilesNameWithName(beamLineName);
				if (correctionFiles != null) {
					for (int k = 0; k < correctionFiles.length; k++) {
						String correctionFileName = correctionFiles[k];
						if (correctionFileName != null && correctionFileName.equals(filename)) {
							String dir = ESRFBeamlineEnum.retrieveDirectoryNameWithName(beamLineName);
							if (dir != null) {
								String correctionFilePath = "/data/pyarch/" + dir + "/" + correctionFileName;
								fullFilePath = PathUtils.FitPathToOS(correctionFilePath);
								java.io.FileInputStream in;
								in = new java.io.FileInputStream(new File(fullFilePath));
								response.setContentType("application/octet-stream");
								response.setHeader("Content-Disposition", "attachment;filename=" + filename);

								ServletOutputStream out = response.getOutputStream();

								byte[] buf = new byte[4096];
								int bytesRead;

								while ((bytesRead = in.read(buf)) != -1) {
									out.write(buf, 0, bytesRead);
								}
								in.close();
								out.flush();
								out.close();
								return null;
							}
						}
					}
				}
			}

		} catch (java.io.IOException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Unable to download file: " + fullFilePath));
			saveErrors(request, errors);
			e.printStackTrace();
			return mapping.findForward("error");
		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward("error");
		}
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Unable to download file: " + filename));
		saveErrors(request, errors);
		return mapping.findForward("error");
	}

	/**
	 * forward to the page with dna results
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayDNAFiles(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		boolean dataProcessingContentPresent = false;
		boolean integrationContentPresent = false;
		boolean strategyContentPresent = false;
		boolean dna_logContentPresent = false;
		boolean mosflm_triclintContentPresent = false;
		boolean scala_logContentPresent = false;
		boolean pointlessContentPresent = false;

		try {
			ViewResultsForm form = (ViewResultsForm) actForm;
			dataCollectionIdst = request.getParameter(Constants.DATA_COLLECTION_ID);
			Integer dataCollectionId = new Integer(dataCollectionIdst);
			DataCollection3VO dc = dataCollectionService.findByPk(dataCollectionId, false, false);

			String fullDNAPath = PathUtils.getFullDNAPath(dc);
			String fullDNAIndexPath = fullDNAPath + "index.html";
			String fullLogPath = PathUtils.GetFullLogPath(dc);
			String fullDNAurl = request.getContextPath() + "/user/imageDownload.do?reqCode=getImageDNA";
			LOG.debug("displayDNAFiles: fullDNAurl= " + fullDNAurl);
			String hrefDNAurl = request.getContextPath() + "/user/viewResults.do?reqCode=viewJpegImageFromFile";

			String fullDNAFileContent = FileUtil.fileToString(fullDNAIndexPath);

			// Case where the file is not found
			if (fullDNAFileContent == null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewDNA"));
				return this.display(mapping, actForm, request, response);
			}

			// Format the file: change the a and img tags
			String fullDNAFileContentChanged = UrlUtils.formatImageURL(fullDNAFileContent, fullDNAurl, hrefDNAurl, fullDNAPath);
			String fullHtmlFileContentChangedNoLink = StringUtils.deleteIndexLinks(fullDNAFileContentChanged);

			// DNA Files Content present ?
			String fullDataProcessingPath = PathUtils.GetFullDataProcessingPath(dc) + Constants.DNA_FILES_DATA_PROC_FILE;
			dataProcessingContentPresent = (new File(fullDataProcessingPath)).exists();
			String fullIntegrationPath = PathUtils.GetFullIntegrationPath(dc) + Constants.DNA_FILES_INTEGRATION_FILE;
			integrationContentPresent = (new File(fullIntegrationPath)).exists();
			String fullStrategyPath = PathUtils.GetFullStrategyPath(dc) + Constants.DNA_FILES_STRATEGY_FILE;
			strategyContentPresent = (new File(fullStrategyPath)).exists();

			dna_logContentPresent = (new File(fullLogPath + Constants.DNA_FILES_LOG_FILE)).exists();
			mosflm_triclintContentPresent = (new File(fullLogPath + Constants.DNA_FILES_MOSFLM_TRICLINT_FILE)).exists();
			scala_logContentPresent = (new File(fullLogPath + Constants.DNA_FILES_SCALA_FILE)).exists();
			pointlessContentPresent = (new File(fullLogPath + Constants.DNA_FILES_POINTLESS_FILE)).exists();

			// Populate form
			form.setDNAContent(fullHtmlFileContentChangedNoLink);
			form.setDataProcessingContentPresent(dataProcessingContentPresent);
			form.setIntegrationContentPresent(integrationContentPresent);
			form.setStrategyContentPresent(strategyContentPresent);
			form.setScala_logContentPresent(scala_logContentPresent);
			form.setDna_logContentPresent(dna_logContentPresent);
			form.setMosflm_triclintContentPresent(mosflm_triclintContentPresent);
			form.setPointlessContentPresent(pointlessContentPresent);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewDNA"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		} else
			return mapping.findForward("viewDNAImages");
	}

	public ActionForward displayDNA_LOG(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ViewResultsForm form = (ViewResultsForm) actForm;
		form.setDNASelectedFile(Constants.DNA_FILES_LOG_FILE);

		return this.displayDNALogFiles(mapping, actForm, request, response);
	}

	public ActionForward displayDNA_MOSFLM_TRI(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ViewResultsForm form = (ViewResultsForm) actForm;
		form.setDNASelectedFile(Constants.DNA_FILES_MOSFLM_TRICLINT_FILE);

		return this.displayDNALogFiles(mapping, actForm, request, response);
	}

	public ActionForward displayDNA_SCALA_LOG(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ViewResultsForm form = (ViewResultsForm) actForm;
		form.setDNASelectedFile(Constants.DNA_FILES_SCALA_FILE);

		return this.displayDNALogFiles(mapping, actForm, request, response);
	}

	public ActionForward displayDNA_POINTLESS_LOG(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ViewResultsForm form = (ViewResultsForm) actForm;
		form.setDNASelectedFile(Constants.DNA_FILES_POINTLESS_FILE);

		return this.displayDNALogFiles(mapping, actForm, request, response);
	}

	/**
	 * forward to the page with dna files present in xxx_dnafiles
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayDNALogFiles(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		boolean contentPresent = false;

		try {
			ViewResultsForm form = (ViewResultsForm) actForm;
			dataCollectionIdst = request.getParameter(Constants.DATA_COLLECTION_ID);
			Integer dataCollectionId = new Integer(dataCollectionIdst);
			DataCollection3VO dc = dataCollectionService.findByPk(dataCollectionId, false, false);

			String fileName = form.getDNASelectedFile();
			String fullLogPath = PathUtils.GetFullLogPath(dc) + fileName;
			LOG.debug("displayDNALogFiles: " + fullLogPath);

			// DNA Files Content present ?
			contentPresent = (new File(fullLogPath)).exists();
			if (!contentPresent) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewDNA"));
				return this.display(mapping, actForm, request, response);
			}

			String fullDNAFileContent = FileUtil.fileToString(fullLogPath);

			// Populate form
			form.setDNAContent(fullDNAFileContent);
			form.setDNAContentPresent(contentPresent);
			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewDNA"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		} else
			return mapping.findForward("viewDNATextFiles");
	}

	/**
	 * Foward to the page with Denzo results
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayDenzoFiles(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();

		try {
			ViewResultsForm form = (ViewResultsForm) actForm;
			dataCollectionIdst = request.getParameter(Constants.DATA_COLLECTION_ID);
			Integer dataCollectionId = new Integer(dataCollectionIdst);

			DataCollection3VO dcValue = dataCollectionService.findByPk(dataCollectionId, true, true);

			String fullDenzoPath = FileUtil.GetFullDenzoPath(dcValue);
			String fullDenzoFilePath = fullDenzoPath + DENZO_HTML_INDEX;

			LOG.debug("displayDenzoFiles: full Denzo file path = " + fullDenzoFilePath);

			// reformat the dnafiles/index/index.html file
			String fullDenzoUrl = request.getContextPath() + "/user/imageDownload.do?reqCode=getImageDNA";

			LOG.debug("displayDenzoFiles : full Denzo url = " + fullDenzoUrl);

			String fullDenzoFileContent = FileUtil.fileToString(fullDenzoFilePath);

			// File is not found
			if (fullDenzoFileContent == null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewDNA"));
				return this.display(mapping, actForm, request, response);
			}

			// -----------------------------------------------------------------------
			String fullDenzoFileContentChanged = UrlUtils.formatImageURL_Denzo(fullDenzoFileContent, fullDenzoUrl, fullDenzoPath);

			// Populate form
			form.setDenzoContent(fullDenzoFileContentChanged);
			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewDNA"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		} else
			return mapping.findForward("viewDenzoImages");
	}

	/**
	 * View a simple jpeg image
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward viewJpegImage(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		try {
			// -------------------------------------------------------------------------------------------------------
			ViewResultsForm form = (ViewResultsForm) actForm;
			Integer targetImageNumber = form.getTargetImageNumber();
			Integer imageId = (request.getParameter(Constants.IMAGE_ID) != null) ? new Integer(
					request.getParameter(Constants.IMAGE_ID)) : null;

			if (imageId == null && form.getCurrentImageId() != null) {
				// the image number has been entered in the number box, not by a click on next/previous
				imageId = new Integer(form.getCurrentImageId());
			}
			Image3VO refImage = imageService.findByPk(imageId);
			Integer dataCollectionId = refImage.getDataCollectionVOId();

			DataCollection3VO refDataCollection = dataCollectionService.findByPk(refImage.getDataCollectionVOId(), true, true);
			DataCollectionGroup3VO refDataCollectionGroup = refDataCollection.getDataCollectionGroupVO();
			Session3VO refSession = sessionService.findByPk(refDataCollectionGroup.getSessionVOId(), false, false, false);

			// Confidentiality (check if object proposalId and session proposalId match)
			if (!Confidentiality.isAccessAllowed(request, refSession.getProposalVO().getProposalId())) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Access denied"));
				saveErrors(request, errors);
				return (mapping.findForward("error"));
			}

			List<ImageValueInfo> imageFetchedList = FileUtil.GetImageList(dataCollectionId, imageId, targetImageNumber, null, null,
					request);

			if (imageFetchedList.size() != 0) {
				ImageValueInfo imageToDisplay = imageFetchedList.get(0);
				form.setImage(imageToDisplay);
				form.setTargetImageNumber(imageToDisplay.getImageNumberInfo());
				form.setTotalImageNumber(new Integer(imageFetchedList.size() - 1));
				if (imageId == null && targetImageNumber == null)
					form.setTotalImageNumber(new Integer(imageFetchedList.size()));
				BreadCrumbsForm.getIt(request).setSelectedImage(imageFetchedList.get(0));
			} else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewImageId", imageId));
				LOG.warn("List fetched has a size != 1!!");
			}

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.general.image"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		} else {
			RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
			String role = roleObject.getName();
			if (role.equals(Constants.ROLE_LOCALCONTACT)) {
				return mapping.findForward("localcontactviewJpegImage");
			} else if (role.equals(Constants.ROLE_MANAGER)) {
				return mapping.findForward("managerviewJpegImage");
			} else
				return mapping.findForward("viewJpegImage");
		}
	}

	/**
	 * View a simple jpeg image
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward viewJpegImageFromFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		try {
			ViewResultsForm form = (ViewResultsForm) actForm;

			String imageFileName = FileUtil.getRequestParameter(request, Constants.IMG_SNAPSHOT_URL_PARAM);
			// String imageFileName = request.getParameter(Constants.IMG_SNAPSHOT_URL_PARAM);

			if (imageFileName != null) {
				form.setImage(null);
				SnapshotInfo snapshotInfo = new SnapshotInfo(imageFileName);
				form.setSnapshotInfo(snapshotInfo);
			}

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.general.image"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		} else {
			RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
			String role = roleObject.getName();
			if (role.equals(Constants.ROLE_LOCALCONTACT)) {
				return mapping.findForward("localcontactviewJpegImage");
			} else if (role.equals(Constants.ROLE_MANAGER)) {
				return mapping.findForward("managerviewJpegImage");
			} else
				return mapping.findForward("viewJpegImage");
		}
	}
	
	/**
	 * View a simple jpeg image
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward getJpegImageFromFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String imageFileName = FileUtil.getRequestParameter(request, Constants.IMG_SNAPSHOT_URL_PARAM);
			DataAdapterCommon.sendImageToClient(imageFileName, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	

	/**
	 * getDataFromFile
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward getDataFromFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		boolean isWindows = (System.getProperty("os.name").indexOf("Win") != -1) ? true : false;
		String tmpDirectory = ((isWindows) ? Constants.BZIP2_PATH_W : Constants.BZIP2_PATH_OUT);

		try {
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			Integer imageId = new Integer(request.getParameter(Constants.IMAGE_ID));

			List<Image3VO> imageFetchedList = imageService.findByImageIdAndProposalId(imageId, proposalId);

			if (imageFetchedList.size() == 1) {
				Image3VO selectedImage = (imageFetchedList.get(0));
				// --- Create File Names ---
				String _sourceFileName = selectedImage.getFileLocation() + "/" + selectedImage.getFileName();
				_sourceFileName = (isWindows) ? "C:" + _sourceFileName : _sourceFileName;

				String _destinationFileName = selectedImage.getFileName();
				String _destinationfullFilename = tmpDirectory + "/" + _destinationFileName;
				String _bz2FileName = _destinationFileName + ".bz2";
				String _bz2FullFileName = _destinationfullFilename + ".bz2";

				// --- Copy Files ---
				File source = new File(_sourceFileName);
				File destination = new File(_destinationfullFilename);
				File bz2File = new File(_bz2FullFileName);
				FileUtils.copyFile(source, destination, false);

				// --- BZIP2 File ---
				String cmd = ((isWindows) ? Constants.BZIP2_PATH_W : Constants.BZIP2_PATH);
				String argument = Constants.BZIP2_ARGS;
				argument = " " + argument + " " + _destinationfullFilename;
				cmd = cmd + argument;
				this.CmdExec(cmd, false);

				// --- Active Wait for the .bz2 File to be created + timeout ---
				Date now = new Date();
				long startTime = now.getTime();
				long timeNow = now.getTime();
				long timeOut = 60000;
				boolean filePresent = false;

				while (!filePresent && (timeNow - startTime) < timeOut) {
					Date d2 = new Date();
					timeNow = d2.getTime();
					filePresent = bz2File.exists();
				}
				if (filePresent)
					Thread.sleep(10000);

				// --- Write Image to output ---
				byte[] imageBytes = FileUtil.readBytes(_bz2FullFileName);
				response.setContentLength(imageBytes.length);
				response.setHeader("Content-Disposition", "attachment; filename=" + _bz2FileName);
				response.setContentType("application/x-bzip");
				ServletOutputStream out = response.getOutputStream();

				out.write(imageBytes);
				out.flush();
				out.close();

				// --- Clean up things ---
				FileCleaner fileCleaner = new FileCleaner(60000, _bz2FullFileName);
				fileCleaner.start();

				return null;
			} else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewImageId", imageId));
				LOG.warn("List fetched has a size != 1!!");
			}

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.general.image"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		} else
			return mapping.findForward("viewJpegImage");

	}

	// ---------------------------------------------------------------------------------------------------

	/**
	 * foward to the page with dna results
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayDNAHtmlFiles(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		try {
			ViewResultsForm form = (ViewResultsForm) actForm;
			dataCollectionIdst = request.getParameter(Constants.DATA_COLLECTION_ID);
			String selectedFile = request.getParameter(Constants.DNA_SELECTED_FILE);
			Integer dataCollectionId = new Integer(dataCollectionIdst);
			DataCollection3VO dc = dataCollectionService.findByPk(dataCollectionId, false, false);

			String htmlFilePath;
			String htmlFullFilePath;
			if (selectedFile.equals(Constants.DNA_FILES_DATA_PROC)) {
				htmlFilePath = PathUtils.GetFullDataProcessingPath(dc);
				htmlFullFilePath = PathUtils.GetFullDataProcessingPath(dc) + "dpm_log.html";
			} else if (selectedFile.equals(Constants.DNA_FILES_INTEGRATION)) {
				htmlFilePath = PathUtils.GetFullIntegrationPath(dc);
				htmlFullFilePath = PathUtils.GetFullIntegrationPath(dc) + "index.html";
			} else if (selectedFile.equals(Constants.DNA_FILES_STRATEGY)) {
				htmlFilePath = PathUtils.GetFullStrategyPath(dc);
				htmlFullFilePath = PathUtils.GetFullStrategyPath(dc) + "index.html";
			} else {
				htmlFilePath = "";
				htmlFullFilePath = "";
			}

			LOG.debug("displayDNAHtmlFiles: htmlFullFilePath= " + htmlFullFilePath);

			String fullDNAUrl = request.getContextPath() + "/user/imageDownload.do?reqCode=getImageDNA";
			LOG.debug("displayDNAHtmlFiles: fullDNAUrl= " + fullDNAUrl);
			String hrefDNAurl = request.getContextPath() + "/user/viewResults.do?reqCode=viewJpegImageFromFile";

			String fullHtmlFileContent = FileUtil.fileToString(htmlFullFilePath);

			// Case where the file is not found
			if (fullHtmlFileContent == null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewDNA"));
				return this.display(mapping, actForm, request, response);
			}

			// Format the file: change the a and img tags
			String fullHtmlFileContentChanged = UrlUtils.formatImageURL(fullHtmlFileContent, fullDNAUrl, hrefDNAurl, htmlFilePath);
			String fullHtmlFileContentChangedNoLink = StringUtils.deleteIndexLinks(fullHtmlFileContentChanged);

			// Populate form
			form.setDataCollectionId(dataCollectionId);
			form.setDNAContent(fullHtmlFileContentChangedNoLink);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewDNA", e.toString()));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));

			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		} else
			return mapping.findForward("viewDNAFiles");
	}

	/**
	 * foward to the page with dna results
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayDNARankingFiles(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		try {
			ViewResultsForm form = (ViewResultsForm) actForm;
			dataCollectionIdst = request.getParameter(Constants.DATA_COLLECTION_ID);
			screeningRankSetIdst = request.getParameter(Constants.SCREENING_RANK_SET_ID);
			String selectedFile = request.getParameter(Constants.DNA_RANKING_SELECTED_FILE);
			Integer dataCollectionId = new Integer(dataCollectionIdst);
			Integer screeningRankSetId = new Integer(screeningRankSetIdst);

			ScreeningRankSet3VO srsv = new ScreeningRankSet3VO();
			srsv = screeningRankSetService.findByPk(screeningRankSetId, true);

			String htmlFilePath;
			String htmlFullFilePath;
			if (selectedFile.equals(Constants.DNA_FILES_RANKING_SUMMARY)) {
				htmlFilePath = PathUtils.GetFullDNARankingPath(dataCollectionId);
				htmlFullFilePath = htmlFilePath + srsv.getRankingSummaryFileName();
			} else {
				htmlFilePath = "";
				htmlFullFilePath = "";
			}

			LOG.debug("displayDNARankingFiles: htmlFullFilePath= " + htmlFullFilePath);

			// Reformat the dna html file
			String fullHtmlFileContent = FileUtil.fileToString(htmlFullFilePath);

			// Case where the file is not found
			if (fullHtmlFileContent == null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewDNA"));
				return this.display(mapping, actForm, request, response);
			}

			// Populate form
			LOG.debug("selectedFile = " + selectedFile);
			LOG.debug("fullHtmlFileContent = " + fullHtmlFileContent);

			form.setDNASelectedFile(selectedFile);
			form.setDataCollectionId(dataCollectionId);
			form.setScreeningRankSet(srsv);
			form.setDNAContent(fullHtmlFileContent);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewDNA", e.toString()));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));

			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		} else
			return mapping.findForward("viewDNATextFiles");
	}

	/**
	 * foward to the page with dna results
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayProgramLogFiles(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		try {
			ViewResultsForm form = (ViewResultsForm) actForm;
			dataCollectionIdst = request.getParameter(Constants.DATA_COLLECTION_ID);
			Integer dataCollectionId = new Integer(dataCollectionIdst);
			DataCollection3VO dc = dataCollectionService.findByPk(dataCollectionId, false, false);
			String programLogPath = PathUtils.GetFullLogPath(dc);
			String programLogFilePath = programLogPath + Constants.DNA_FILES_BEST_FILE;

			LOG.debug("displayProgramLogFiles: programLogFilePath= " + programLogFilePath);

			// reformat the dna html file
			String fullFileContent = FileUtil.fileToString(programLogFilePath);

			// case where the file is not found
			if (fullFileContent == null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewDNA"));
				return this.display(mapping, actForm, request, response);
			}

			// Populate form
			form.setDNAContent(fullFileContent);
			form.setDNASelectedFile(Constants.DNA_FILES_BEST_FILE);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewDNA", e.toString()));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));

			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		} else
			return mapping.findForward("viewDNATextFiles");
	}

	/**
	 * Display EDNA results content on the page
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward displayEDNAPagesContent(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ActionMessages errors = new ActionMessages();

		dataCollectionIdst = request.getParameter(Constants.DATA_COLLECTION_ID);
		Integer dataCollectionId = new Integer(dataCollectionIdst);
		DataCollection3VO dc = dataCollectionService.findByPk(dataCollectionId, false,  false);
		String archivePath = Constants.SITE_IS_DLS() ? PathUtils.getFullEDNAPath(dc) : PathUtils.getFullDNAPath(dc);
		// String fullEDNAPath = archivePath + Constants.EDNA_FILES_SUFIX;
		if (Constants.SITE_IS_EMBL()) {
			String[] archivePathDir = archivePath.split("/");
			String beamLineName = dc.getDataCollectionGroupVO().getSessionVO().getBeamlineName().toLowerCase();
			archivePath = Constants.DATA_FILEPATH_START + beamLineName + "/";
			for (int k = 4; k < archivePathDir.length; k++) {
				archivePath = archivePath + archivePathDir[k] + "/";
			}
		}

		if (Constants.SITE_IS_ALBA()) {
			LOG.debug("In ALBA block");
			String[] archivePathDir = archivePath.split("/");
			String beamLineName = dc.getDataCollectionGroupVO().getSessionVO().getBeamlineName().toLowerCase();
			archivePath = "/beamlines/ispyb/bl13/";
			// archivePath = "/beamlines/ispyb/" + beamLineName + "/";

			for (int k = 5; k < archivePathDir.length; k++) {
				if (k != 7) {
					archivePath = archivePath + archivePathDir[k] + "/";
				}
			}
			LOG.debug("generated archivePath: " + archivePath);
		}
		
		boolean isFileExist = new File(archivePath + Constants.EDNA_FILES_SUFIX).exists();
		String fullEDNAPath = archivePath;
		if (Constants.SITE_IS_DLS() || (isFileExist)) {
			fullEDNAPath += Constants.EDNA_FILES_SUFIX;
		}
		String indexPath = Constants.SITE_IS_DLS() ? archivePath + EDNA_FILES_INDEX_FILE : archivePath + getEdna_index_file(dc);
	
		LOG.debug("Check if the Characterisation results index file " + indexPath + " exists... ");
		boolean isFileExist2 = new File(indexPath).exists();
		String fileContent = null;
		if (isFileExist2)
			fileContent = FileUtil.fileToString(indexPath);
		else {
			fileContent = "Sorry, but no EDNA files can be retrieved for this data collection.";
		}
		// new path to view images
		// String pathImageUrl = request.getContextPath() + "/user/imageDownload.do?reqCode=getEDNAImage";
		String pathImageUrl = "imageDownload.do?reqCode=getEDNAImage";

		// String hrefImageUrl = request.getContextPath() + "/user/viewResults.do?reqCode=viewEDNAImage";
		String hrefImageUrl = "viewResults.do?reqCode=viewEDNAImage";

		String hrefFileUrl = "viewResults.do?reqCode=displayEDNAFile";

		// Case where the file is not found
		if (fileContent == null) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewDNA"));
			return this.display(mapping, actForm, request, response);
		}

		// Format the file: change the <a href and <img src tags
		byte[] fileContentChanged = (UrlUtils.formatEDNApageURL(fileContent, pathImageUrl, hrefImageUrl, hrefFileUrl, fullEDNAPath))
				.getBytes();

		// --- Write Image to output ---
		response.setContentType("text/html");
		try {
			ServletOutputStream out = response.getOutputStream();
			out.write(fileContentChanged);
			out.flush();
			out.close();
		} catch (IOException ioe) {
			LOG.error("Unable to write to outputStream. (IOException)");
			ioe.printStackTrace();
			return mapping.findForward("error");
		}

		return null;
	}

	/**
	 * Display EDNA results content on the page
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward displayEDNA(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response, ActionMessages errors) throws Exception {

		ViewResultsForm form = (ViewResultsForm) actForm;
		dataCollectionIdst = request.getParameter(Constants.DATA_COLLECTION_ID);
		try {
			// Integer dataCollectionId = new Integer(dataCollectionIdst);
			Integer dataCollectionId = BreadCrumbsForm.getIt(request).getSelectedDataCollection().getDataCollectionId();
			DataCollection3VO dc = dataCollectionService.findByPk(dataCollectionId, false,  false);
			String archivePath = Constants.SITE_IS_DLS() ? PathUtils.getFullEDNAPath(dc) : PathUtils.getFullDNAPath(dc);
			LOG.debug("archivePath:"+ archivePath);
			// String fullEDNAPath = archivePath + Constants.EDNA_FILES_SUFIX;

			boolean isFileExist = new File(archivePath + Constants.EDNA_FILES_SUFIX).exists();
			String fullEDNAPath = archivePath;
			if (Constants.SITE_IS_DLS() || (isFileExist)) {
				fullEDNAPath += Constants.EDNA_FILES_SUFIX;
			}

			String indexPath = Constants.SITE_IS_DLS() ? archivePath + EDNA_FILES_INDEX_FILE : archivePath + getEdna_index_file(dc);

			boolean isFileExist2 = new File(indexPath).exists();
			String fileContent = "";
			if (isFileExist2)
				fileContent = FileUtil.fileToString(indexPath);

			// Case where the file is not found
			if (fileContent == null) {
				// errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewDNA"));
				form.setDNAContent("Sorry, but no EDNA files can be retrieved for this data collection.");
				return null;
			}
			//
			// String fullDNAFileContent = FileUtil.fileToString(indexPath);

			// Populate form
			form.setDataCollectionId(dataCollectionId);
			form.setDNAContent("<iframe scrolling='yes' frameborder='0' width='790' height='600' src='" + request.getContextPath()
					+ "/user/viewResults.do?reqCode=displayEDNAPagesContent&dataCollectionId=" + dataCollectionId.toString() + "'>"
					+ fileContent + "</iframe>");
			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

			return null;
		} catch (NumberFormatException e) {
			form.setDNAContent("Sorry, an error occurs while retrieving the EDNA file:" + dataCollectionIdst);
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * forward to the page with dna results
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayEDNAPages(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		try {
			ViewResultsForm form = (ViewResultsForm) actForm;
			dataCollectionIdst = request.getParameter(Constants.DATA_COLLECTION_ID);
			Integer dataCollectionId = new Integer(dataCollectionIdst);
			DataCollection3VO dc = dataCollectionService.findByPk(dataCollectionId, false,  false);
			String archivePath = Constants.SITE_IS_DLS() ? PathUtils.getFullEDNAPath(dc) : PathUtils.getFullDNAPath(dc);
			// String fullEDNAPath = archivePath + Constants.EDNA_FILES_SUFIX;
			boolean isFileExist = new File(archivePath + Constants.EDNA_FILES_SUFIX).exists();
			String fullEDNAPath = archivePath;
			if (Constants.SITE_IS_DLS() || (isFileExist)) {
				fullEDNAPath += Constants.EDNA_FILES_SUFIX;
			}

			LOG.debug("displayEDNAPages: fullEDNAPath= " + fullEDNAPath);

			String indexPath = Constants.SITE_IS_DLS() ? archivePath + EDNA_FILES_INDEX_FILE : archivePath + getEdna_index_file(dc);

			String fileContent = FileUtil.fileToString(indexPath);

			// new path to view images
			// String pathImageUrl = request.getContextPath() + "/user/imageDownload.do?reqCode=getEDNAImage";
			String pathImageUrl = "imageDownload.do?reqCode=getEDNAImage";

			// String hrefImageUrl = request.getContextPath() + "/user/viewResults.do?reqCode=viewEDNAImage";
			String hrefImageUrl = "viewResults.do?reqCode=viewEDNAImage";

			String hrefFileUrl = "viewResults.do?reqCode=displayEDNAFile";

			// Case where the file is not found
			if (fileContent == null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewDNA"));
				return this.display(mapping, actForm, request, response);
			}

			// Format the file: change the <a href and <img src tags
			String fileContentChanged = UrlUtils.formatEDNApageURL(fileContent, pathImageUrl, hrefImageUrl, hrefFileUrl, fullEDNAPath);

			// Populate form
			if (Constants.SITE_IS_DLS())
				form.setDNAContent("<iframe width=\"100%\" height=\"500px\">" + fileContentChanged + "</iframe>");
			else
				// form.setDNAContent(fileContentChanged);
				form.setDNAContent("<iframe width=\"100%\" height=\"500px\">" + fileContentChanged + "</iframe>");

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewDNA"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		} else
			return mapping.findForward("viewEDNAPages");
	}

	/**
	 * forward to the page displaying a edna file
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayEDNAFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		try {
			String filePath = request.getParameter(Constants.EDNA_FILE_PATH);

			LOG.debug("displayEDNAFile: " + filePath);

			if (Constants.SITE_IS_DLS()) {
				byte[] fileContent = FileUtil.fileToString(filePath).getBytes();
				// --- Write Image to output ---
				response.setContentType("text/plain");
				try {
					ServletOutputStream out = response.getOutputStream();
					out.write(fileContent);
					out.flush();
					out.close();
				} catch (IOException ioe) {
					LOG.error("Unable to write to outputStream. (IOException)");
					ioe.printStackTrace();
					return mapping.findForward("error");
				}
			} else {
				// String fileContent = FileUtil.fileToString(filePath);
				//
				// // Populate form
				// form.setDNAContent(fileContent);
				// FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);
				byte[] fileContent = FileUtil.fileToString(filePath).getBytes();
				// --- Write Image to output ---
				response.setContentType("text/plain");
				try {
					ServletOutputStream out = response.getOutputStream();
					out.write(fileContent);
					out.flush();
					out.close();
				} catch (IOException ioe) {
					LOG.error("Unable to write to outputStream. (IOException)");
					ioe.printStackTrace();
					return mapping.findForward("error");
				}
			}
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewDNA"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		} else
			// return Constants.SITE_IS_DLS() ? null : mapping.findForward("viewTextFile");
			return null;
	}

	/**
	 * View a simple jpeg image for EDNA
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward viewEDNAImage(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		try {
			// -------------------------------------------------------------------------------------------------------
			ViewResultsForm form = (ViewResultsForm) actForm;
			String imagePath = request.getParameter(Constants.EDNA_IMAGE_PATH);

			// Confidentiality (check if object proposalId and file path proposal name match)
			if (!Confidentiality.isAccessAllowed(request, imagePath)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Access denied"));
				saveErrors(request, errors);
				return (mapping.findForward("error"));
			}

			form.setImagePath(imagePath);
			LOG.debug("viewEDNAImage: imagePath= " + imagePath);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.general.image"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		} else
			return mapping.findForward("viewEDNAImage");
	}

	// -----------------------------------------------------------------------

	public static boolean DisplayDenzoContent(DataCollection3VO dataCollectionVO) throws Exception {
		boolean displayDenzoContent = false;

		try {
			// DataCollection3VO dcValue = dataCollectionService.findByPk(dataCollectionId, false, false, false, false);
			// Session3VO sessionValue = sessionService.findByPk(dcValue.getSessionVOId(), false, false, false);
			Session3VO sessionValue = dataCollectionVO.getDataCollectionGroupVO().getSessionVO();

			String beamlineName = sessionValue.getBeamlineName().toLowerCase();

			if (beamlineName.indexOf(Constants.BEAMLINE_NAME_BM14) != -1)
				displayDenzoContent = true;
		} catch (Exception e) {
		}

		return displayDenzoContent;
	}

	/**
	 * CmdExec
	 * 
	 * @param cmdline
	 * @return
	 */
	private String CmdExec(String cmdline, boolean captureOutput) {
		String output = new String();
		// System.out.println("COMMAND LINE=\n" + cmdline);
		try {
			String line;
			Process p = Runtime.getRuntime().exec(cmdline);
			if (captureOutput) {
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = input.readLine()) != null) {
					output += line;
					// System.out.println(line);
				}
				input.close();
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		// System.out.println("OUTPUT=\n" + output);
		return output;
	}

	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward downloadAttachment(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		LOG.debug("download files...");
		ViewResultsForm form = (ViewResultsForm) actForm;
		// attachments to download
		ActionMessages errors = new ActionMessages();
		Integer autoProcProgramId = null;
		List<File> listFilesToDownload = new ArrayList<File>();
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
		HashMap<String, String> filesToZip = new HashMap<String, String>();

		try {
			// auto proc attachment
			// Get an object list.
			// List<IspybAutoProcAttachment3VO> listOfAutoProcAttachment = ispybAutoProcAttachmentService.findAll();
			List<IspybAutoProcAttachment3VO> listOfAutoProcAttachment = (List<IspybAutoProcAttachment3VO>) request.getSession()
					.getAttribute(Constants.ISPYB_AUTOPROC_ATTACH_LIST);

			Integer autoProcId = Integer.parseInt(request.getParameter("autoProcId").toString());
			AutoProc3VO apv = apService.findByPk(autoProcId);

			if (apv != null)
				autoProcProgramId = apv.getAutoProcProgramVOId();
			if (!Confidentiality.isAccessAllowedToAutoProcProgram(request, autoProcProgramId)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Access denied"));
				saveErrors(request, errors);
				return (mapping.findForward("error"));
			}
			List<AutoProcProgramAttachment3VO> attachments = null;
			AutoProcAttachmentWebBean[] attachmentWebBeans = null;
			
			if (autoProcProgramId != null) {
				attachments = appService.findByPk(autoProcProgramId, true).getAttachmentListVOs();
				if (!attachments.isEmpty()) {
					attachmentWebBeans = new AutoProcAttachmentWebBean[attachments.size()];
					int i = 0;
					for (Iterator<AutoProcProgramAttachment3VO> iterator = attachments.iterator(); iterator.hasNext();) {
						AutoProcProgramAttachment3VO att = iterator.next();
						AutoProcAttachmentWebBean attBean = new AutoProcAttachmentWebBean(att);
						// gets the ispyb auto proc attachment file
						IspybAutoProcAttachment3VO aAutoProcAttachment = getAutoProcAttachment(attBean.getFileName(),
								listOfAutoProcAttachment);
						attBean.setIspybAutoProcAttachment(aAutoProcAttachment);
						attachmentWebBeans[i] = attBean;
						LOG.debug("attBean = " + attBean.toString());
						String fullFilePath = attBean.getFilePath().trim() + "/" + attBean.getFileName();
						fullFilePath = PathUtils.FitPathToOS(fullFilePath);
						File f = new File(fullFilePath);
						if (f.canRead()) {
							listFilesToDownload.add(f);
							filesToZip.put(f.getName(), fullFilePath);
							LOG.debug("Put in zip : " + f.getName() + " file: "+ fullFilePath);
						}
						i = i + 1;
					}

				} else
					LOG.debug("attachments is empty");
			}
			// Issue 1507: Correction files for ID29 & ID23-1
			if (Constants.SITE_IS_ESRF()) {
				Integer dataCollectionId = null;
				if (BreadCrumbsForm.getIt(request).getSelectedDataCollection() != null)
					dataCollectionId = BreadCrumbsForm.getIt(request).getSelectedDataCollection().getDataCollectionId();
				if (dataCollectionId != null) {
					DataCollection3VO dataCollection = dataCollectionService.findByPk(dataCollectionId, false, false);
					String beamLineName = dataCollection.getDataCollectionGroupVO().getSessionVO().getBeamlineName();
					String[] correctionFiles = ESRFBeamlineEnum.retrieveCorrectionFilesNameWithName(beamLineName);
					if (correctionFiles != null) {
						for (int k = 0; k < correctionFiles.length; k++) {
							String correctionFileName = correctionFiles[k];
							String dir = ESRFBeamlineEnum.retrieveDirectoryNameWithName(beamLineName);
							if (dir != null) {
								String correctionFilePath = "/data/pyarch/" + dir + "/" + correctionFileName;
								String fullFilePath = PathUtils.FitPathToOS(correctionFilePath);
								File f = new File(fullFilePath);
								if (f.canRead()) {
									listFilesToDownload.add(f);
									filesToZip.put(f.getName(), fullFilePath);
									LOG.debug("Put in zip : " + f.getName() + " file: "+ fullFilePath);
								}
							}
						}
					}
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewResults"));
			return this.display(mapping, actForm, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewResults"));
			return this.display(mapping, actForm, request, response);
		}

		// create zip
		if (autoProcProgramId != null) {
			String genericFilename = "autoProcessingFiles.zip";
			
			byte[] zippedFiles = HashMapToZip.doZip(filesToZip);

			String info = "";
			Integer dataCollectionId = BreadCrumbsForm.getIt(request).getSelectedDataCollection().getDataCollectionId();
			if (dataCollectionId != null) {
				try {
					DataCollection3VO dc = dataCollectionService.findByPk(dataCollectionId, false, false);
					if (dc != null) {
						info = dc.getImagePrefix() + "_run" + dc.getDataCollectionNumber() + "_";
					}
				} catch (Exception e) {
					e.printStackTrace();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewResults"));
					return this.display(mapping, actForm, request, response);
				}
			}
			String newfilename = info + genericFilename;			
			response.setContentType("application/zip");
			response.setHeader("Content-Disposition", "attachment;filename=" + newfilename);
			OutputStream output;
			try {
				output = response.getOutputStream();
				output.write(zippedFiles);
				output.close();
						
			} catch (IOException e) {
				e.printStackTrace();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewResults"));
				return this.display(mapping, actForm, request, response);
			}
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public void relaunchProcessing(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
	//public ActionForward relaunchProcessing(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
	//		HttpServletResponse response) {
		Integer collectionId = (Integer) request.getSession().getAttribute(Constants.DATA_COLLECTION_ID);
		//DataCollection3VO dataCollection = dataCollectionService.findByPk(collectionId, false, false);
		//String beamLineName = dataCollection.getDataCollectionGroupVO().getSessionVO().getBeamlineName();
		String beamLineName = "p13";
		LOG.debug("Starting processing for data collection: " + collectionId);
		try {
			String realXDSInputFilePath = PathUtils.FitPathToOS(Constants.DATA_XDS_INPUT_FILEPATH_START + "p13/xds_input_files/" + collectionId.toString() + "/XDS.INP");
			LOG.debug("Path to XDS.INP file: " + realXDSInputFilePath);
			String commands[] = new String[]{Constants.REPROCESSING_SCRIPT_PATH, realXDSInputFilePath, beamLineName};
 
		    Runtime rt = Runtime.getRuntime();
			Process process = null;
			try{
			    process = rt.exec(commands);
			    //process.waitFor();
			    }catch(Exception e){
				      e.printStackTrace();
			}
			LOG.debug("New task submitted to the beamline " + beamLineName + "processing queue.");			
			}
		catch (Exception e) {
				e.printStackTrace();
			}
		//return null;
		//return mapping.findForward("success");
	}
	
	// returns the auto proc attachment linked to the specified filename - null if not found
	public static IspybAutoProcAttachment3VO getAutoProcAttachment(String fileName,
			List<IspybAutoProcAttachment3VO> listOfAutoProcAttachment) {
		if (listOfAutoProcAttachment == null)
			return null;
		for (Iterator<IspybAutoProcAttachment3VO> i = listOfAutoProcAttachment.iterator(); i.hasNext();) {
			IspybAutoProcAttachment3VO att = i.next();
			if (att.getFileName().equalsIgnoreCase(fileName)) {
				return att;
			}
		}

		for (Iterator<IspybAutoProcAttachment3VO> i = listOfAutoProcAttachment.iterator(); i.hasNext();) {
			IspybAutoProcAttachment3VO att = i.next();
			if (fileName.contains(att.getFileName())) {
				return att;
			}
		}

		if (fileName.contains("_run")) {
			// edna file: remove the prefix (image + run) to add edna
			int id1 = fileName.indexOf("_run");
			String s = fileName.substring(id1 + 1);
			int id2 = s.indexOf("_");
			if (id2 >= 0 && id2 < s.length() - 1) {
				s = s.substring(id2 + 1);
				s = "edna_" + s;
				for (Iterator<IspybAutoProcAttachment3VO> i = listOfAutoProcAttachment.iterator(); i.hasNext();) {
					IspybAutoProcAttachment3VO att = i.next();
					if (att.getFileName().equalsIgnoreCase(s)) {
						return att;
					}
				}
			}
		}
		LOG.debug("No AutoProc Attachment found for: " + fileName);
		return null;
	}

	@SuppressWarnings("unchecked")
	public void getAutoProcessingData(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		LOG.debug("getAutoProcessingData");
		List<String> errors = new ArrayList<String>();
		try {
			// get the autoProc and the file
			String id = request.getParameter("autoProcProgramAttachmentId");
			Integer autoProcProgramAttachmentId = null;

			boolean xscaleFile = false;
			boolean truncateLog = false;
			List<AutoProcessingData> listAutoProcessingData = new ArrayList<AutoProcessingData>();

			try {
				autoProcProgramAttachmentId = Integer.parseInt(id);
			} catch (NumberFormatException e) {

			}
			if (autoProcProgramAttachmentId != null) {
				AutoProcProgramAttachment3VO attachment = appaService.findByPk(autoProcProgramAttachmentId);
				// o[0] is a boolean xscaleFile
				// o[1] is a boolean truncateLog
				// o[2] is List<AutoProcessingData>
				try {
					Object[] o = ViewResultsAction.readAttachment(attachment);
					xscaleFile = (Boolean) o[0];
					truncateLog = (Boolean) o[1];
					listAutoProcessingData = (List<AutoProcessingData>) o[2];

				} catch (Exception e) {
					e.printStackTrace();
					errors.add("Error while reading the file: " + e);
					HashMap<String, Object> data = new HashMap<String, Object>();
					data.put("errors", errors);
					// data => Gson
					GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
					return;
				}
			}

			HashMap<String, Object> data = new HashMap<String, Object>();
			// context path
			data.put("contextPath", request.getContextPath());
			// autoProc data
			data.put("autoProcessingData", listAutoProcessingData);
			// type data
			data.put("xscaleLpData", xscaleFile);
			data.put("truncateLogData", truncateLog);
			// data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// o[0] is a boolean xscaleFile
	// o[1] is a boolean truncateLog
	// o[2] is List<AutoProcessingData>
	public static Object[] readAttachment(AutoProcProgramAttachment3VO attachment) throws Exception {
		boolean xscaleFile = false;
		boolean truncateLog = false;
		List<AutoProcessingData> listAutoProcessingData = new ArrayList<AutoProcessingData>();

		if (attachment != null) {
			String fileName = attachment.getFileName();
			xscaleFile = (fileName != null && fileName.toLowerCase().endsWith("xscale.lp"));
			truncateLog = (fileName != null && fileName.toLowerCase().endsWith(".log") && fileName.toLowerCase().contains("truncate"));

			if (xscaleFile || truncateLog) {
				// parse the file
				String sourceFileName = PathUtils.FitPathToOS(attachment.getFilePath() + "/" + fileName);
				BufferedReader inFile = null;
				String output = new String();// = null;

				try {
					// 1. Reading input by lines:
					boolean startToRead = false;
					boolean startToRead2 = false;
					inFile = new BufferedReader(new FileReader(sourceFileName));
					String s = new String();
					while ((s = inFile.readLine()) != null) {
						String line = s;
						output += line + "\n";
						if (xscaleFile) {
							if (line.contains("SUBSET OF INTENSITY DATA WITH SIGNAL/NOISE")) {
								startToRead = true;
							} else if (startToRead) {
								if (!line.contains("RESOLUTION") && !line.isEmpty() && !line.contains("LIMIT")) {
									String[] values = line.split(" ");
									String[] val = new String[14];
									int i = 0;
									for (int k = 0; k < values.length; k++) {
										if (i <= 13 && !values[k].isEmpty()) {
											val[i] = values[k];
											if (values[k].endsWith("%") || values[k].endsWith("*"))
												val[i] = values[k].substring(0, values[k].length() - 1);
											i++;
										}
									}
									try {
										AutoProcessingData d = new AutoProcessingData(attachment.getAutoProcProgramAttachmentId(),
												Double.parseDouble(val[0]), Double.parseDouble(val[4]), Double.parseDouble(val[5]),
												Double.parseDouble(val[8]), Double.parseDouble(val[10]), Double.parseDouble(val[12]),
												Integer.parseInt(val[11]));
										// int index=0;
										// for (int j=0; j<listAutoProcessingData.size(); j++){
										// if (d.getResolutionLimit() >=
										// listAutoProcessingData.get(j).getResolutionLimit() ){
										// index = j;
										// break;
										// }
										// }
										//
										// listAutoProcessingData.add(index, d);
										listAutoProcessingData.add(d);
									} catch (Exception e) {

									}
								}
							}
							if (line.contains("STATISTICS OF INPUT DATA SET")) {
								startToRead = false;
							}
						} else if (truncateLog) {
							if (line.contains("$TABLE: Wilson Plot")) {
								startToRead = true;
							} else if (startToRead) {
								try {
									String[] values = line.split(" ");
									String[] val = new String[10];
									int i = 0;
									for (int k = 0; k < values.length; k++) {
										if (i <= 9 && !values[k].isEmpty()) {
											val[i] = values[k];
											if (values[k].endsWith("."))
												val[i] = values[k].substring(0, values[k].length() - 1);
											i++;
										}
									}

									AutoProcessingData d = new AutoProcessingData(attachment.getAutoProcProgramAttachmentId(),
											Double.parseDouble(val[5]), Double.parseDouble(val[7]));
									listAutoProcessingData.add(d);
								} catch (Exception e) {

								}
							} else if (line.contains("$TABLE: Cumulative intensity distribution")) {
								startToRead2 = true;
							} else if (startToRead2) {
								try {
									String[] values = line.split(" ");
									String[] val = new String[6];
									int i = 0;
									for (int k = 0; k < values.length; k++) {
										if (i <= 9 && !values[k].isEmpty()) {
											val[i] = values[k];
											if (values[k].endsWith("."))
												val[i] = values[k].substring(0, values[k].length() - 1);
											i++;
										}
									}

									AutoProcessingData d = new AutoProcessingData(attachment.getAutoProcProgramAttachmentId(),
											Double.parseDouble(val[0]), Double.parseDouble(val[1]), Double.parseDouble(val[2]),
											Double.parseDouble(val[3]), Double.parseDouble(val[4]), Double.parseDouble(val[5]));
									listAutoProcessingData.add(d);
								} catch (Exception e) {

								}
							}
							if (line.contains("<hr>")) {
								startToRead = false;
							}

						}
					}
					inFile.close();

				} catch (Exception e) {
					throw e;
				} finally {
					if (inFile != null) {
						try {
							inFile.close();
						} catch (IOException ioex) {
							// ignore
							output = "nofile";
						}
					}

				}
			}
		}
		// o[0] is a boolean xscaleFile
		// o[1] is a boolean truncateLog
		// o[2] is List<AutoProcessingData>
		Object[] o = new Object[3];
		o[0] = xscaleFile;
		o[1] = truncateLog;
		o[2] = listAutoProcessingData;
		return o;
	}

	public void getAutoprocessingInfo(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		LOG.debug("getAutoprocessingInfo");
		try {
			List<AutoProcessingInformation> autoProcList = new ArrayList<AutoProcessingInformation>();
			BreadCrumbsForm bar = BreadCrumbsForm.getIt(request);

			String rMerge = (String) request.getSession().getAttribute("rMerge");
			String iSigma = (String) request.getSession().getAttribute("iSigma");

			double rMerge_d = DEFAULT_RMERGE;
			double iSigma_d = DEFAULT_ISIGMA;
			int nbRemoved = 0;

			try {
				if (rMerge != null && !rMerge.equals("undefined") && !rMerge.equals(""))
					rMerge_d = Double.parseDouble(rMerge);
				if (iSigma != null && !iSigma.equals("undefined") && !iSigma.equals(""))
					iSigma_d = Double.parseDouble(iSigma);
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}
			Integer dataCollectionId = null;
			List<List<AutoProcStatus3VO>> interruptedAutoProcEvents = new ArrayList<List<AutoProcStatus3VO>>();
			// Just one of them could be visible on the bar
			if (bar.getSelectedDataCollection() != null) {
				dataCollectionId = bar.getSelectedDataCollection().getDataCollectionId();
				for (int i = 0; i < 2; i++) {
					boolean anomalous = (i > 0);
					List<AutoProc3VO> autoProcsAnomalous = apService.findByAnomalousDataCollectionIdAndOrderBySpaceGroupNumber(
							dataCollectionId, anomalous);
					if (autoProcsAnomalous != null) {
						LOG.debug("..nbAutoProc " + anomalous + " found before rMerge =" + autoProcsAnomalous.size());
						nbRemoved = 0;
						for (Iterator<AutoProc3VO> a = autoProcsAnomalous.iterator(); a.hasNext();) {
							AutoProc3VO apv = a.next();

							List<AutoProcScalingStatistics3VO> scalingStatistics = apssService.findByAutoProcId(apv.getAutoProcId(),
									"innerShell");
							boolean existsUnderRmergeAndOverSigma = false;

							for (Iterator<AutoProcScalingStatistics3VO> j = scalingStatistics.iterator(); j.hasNext();) {
								AutoProcScalingStatistics3VO stats = j.next();
								if (stats.getRmerge() != null && stats.getRmerge() < rMerge_d && stats.getMeanIoverSigI() > iSigma_d)
									existsUnderRmergeAndOverSigma = true;
							}
							
							if (!existsUnderRmergeAndOverSigma) {
								a.remove();
								nbRemoved = nbRemoved +1;
							}
						}
						LOG.debug("..nbAutoProc " + anomalous + " found=" + autoProcsAnomalous.size());
						for (Iterator<AutoProc3VO> iterator = autoProcsAnomalous.iterator(); iterator.hasNext();) {
							AutoProc3VO o = iterator.next();
							String cmdLine = "";
							if (o.getAutoProcProgramVO() != null && o.getAutoProcProgramVO().getProcessingPrograms() != null) {
								cmdLine = o.getAutoProcProgramVO().getProcessingPrograms();
							}
							float refinedCellA = ((float) ((int) (o.getRefinedCellA() * 10))) / 10; // round to 1 dp
							float refinedCellB = ((float) ((int) (o.getRefinedCellB() * 10))) / 10; // round to 1 dp
							float refinedCellC = ((float) ((int) (o.getRefinedCellC() * 10))) / 10; // round to 1 dp
							float refinedCellAlpha = ((float) ((int) (o.getRefinedCellAlpha() * 10))) / 10; // round to
																											// 1 dp
							float refinedCellBeta = ((float) ((int) (o.getRefinedCellBeta() * 10))) / 10; // round to 1
																											// dp
							float refinedCellGamma = ((float) ((int) (o.getRefinedCellGamma() * 10))) / 10; // round to
																											// 1 dp

							String anoTxt = "Anomalous pairs merged (false)";
							if (anomalous) {
								anoTxt = "Anomalous pairs unmerged (true)";
							}
							// String anomalousS = anomalous+"= "+anoTxt;
							AutoProcessingInformation info = new AutoProcessingInformation(o.getAutoProcId(), cmdLine,
									o.getSpaceGroup(), anoTxt, refinedCellA, refinedCellB, refinedCellC, refinedCellAlpha,
									refinedCellBeta, refinedCellGamma);
							autoProcList.add(info);
						}
					}
				}
				// interrupted autoProc
				DataCollection3VO dc = dataCollectionService.findByPk(dataCollectionId, false, false);
				if (dc != null) {
					List<AutoProcIntegration3VO> autoProcIntegrationList = dc.getAutoProcIntegrationsList();
					if (autoProcIntegrationList != null) {
						for (Iterator<AutoProcIntegration3VO> au = autoProcIntegrationList.iterator(); au.hasNext();) {
							AutoProcIntegration3VO autoProcIntegration = au.next();
							if (autoProcIntegration.getAutoProcProgramVO() == null
									&& autoProcIntegration.getAutoProcStatusList() != null) {
								List<AutoProcStatus3VO> events = autoProcIntegration.getAutoProcStatusList();
								interruptedAutoProcEvents.add(events);
							}
						}
					}
				}
			}
			Integer autoProcIdSelected = (Integer) request.getSession().getAttribute("lastAutoProcIdSelected");
			// check if autoProcIdSelected is in the list
			boolean idExists = false;
			if (autoProcIdSelected != null) {
				for (Iterator<AutoProcessingInformation> iterator = autoProcList.iterator(); iterator.hasNext();) {
					AutoProcessingInformation info = iterator.next();
					if (info.getAutoProcId().equals(autoProcIdSelected)) {
						idExists = true;
						break;
					}
				}
			}
			if (!idExists) {
				autoProcIdSelected = null;
			}

			//
			HashMap<String, Object> data = new HashMap<String, Object>();
			// context path
			data.put("contextPath", request.getContextPath());
			// autoProcList
			data.put("autoProcList", autoProcList);
			// autoProcIdSelected
			data.put("autoProcIdSelected", autoProcIdSelected);
			// rMerge & iSigma
			data.put("rMerge", rMerge);
			data.put("iSigma", iSigma);
			data.put("nbRemoved", nbRemoved);
			data.put("dataCollectionId", dataCollectionId);
			// interrupted autoProc
			data.put("interruptedAutoProcEvents", interruptedAutoProcEvents);
			// data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void getAutoprocessingDetails(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		LOG.debug("getAutoprocessingDetails");
		try {
			String autoProcIdS = request.getParameter("autoProcId");
			AutoProcessingDetail autoProcDetail = new AutoProcessingDetail();
			try {
				DecimalFormat df1 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
				df1.applyPattern("#####0.0");
				Integer autoProcId = Integer.parseInt(autoProcIdS);
				AutoProc3VO apv = apService.findByPk(autoProcId);
				autoProcDetail.setAutoProcId(autoProcId);

				AutoProcScalingStatistics3VO apssv_overall = apssService.getBestAutoProcScalingStatistic(apssService.findByAutoProcId(
						autoProcId, "overall"));
				AutoProcScalingStatistics3VO apssv_outer = apssService.getBestAutoProcScalingStatistic(apssService.findByAutoProcId(
						autoProcId, "outerShell"));

				if (apssv_overall != null) {
					autoProcDetail.setOverallCompleteness("" + apssv_overall.getCompleteness() + "%");
					autoProcDetail.setOverallResolution("" + apssv_overall.getResolutionLimitLow() + "-"
							+ apssv_overall.getResolutionLimitHigh() + " &#8491;");
					autoProcDetail.setOverallIOverSigma("" + apssv_overall.getMeanIoverSigI());
					if (apssv_overall.getRmerge() == null)
						autoProcDetail.setOverallRsymm("");
					else {
						autoProcDetail.setOverallRsymm("" + apssv_overall.getRmerge() + "%");
					}
					autoProcDetail
							.setOverallMultiplicity(""
									+ (apssv_overall.getMultiplicity() == null ? "" : new Double(df1.format(apssv_overall
											.getMultiplicity()))));
				}

				if (apssv_outer != null) {
					autoProcDetail.setOuterCompleteness("" + apssv_outer.getCompleteness() + "%");
					autoProcDetail.setOuterResolution("" + apssv_outer.getResolutionLimitLow() + "-"
							+ apssv_overall.getResolutionLimitHigh() + " &#8491;");
					autoProcDetail.setOuterIOverSigma("" + apssv_outer.getMeanIoverSigI());
					autoProcDetail.setOuterRsymm("" + apssv_outer.getRmerge() + "%");
					autoProcDetail.setOuterMultiplicity(""
							+ (apssv_outer.getMultiplicity() == null ? "" : (new Double(df1.format(apssv_outer.getMultiplicity())))));
				}

				double refinedCellA = ((double) ((int) (apv.getRefinedCellA() * 10))) / 10;
				double refinedCellB = ((double) ((int) (apv.getRefinedCellB() * 10))) / 10;
				double refinedCellC = ((double) ((int) (apv.getRefinedCellC() * 10))) / 10;

				autoProcDetail.setUnitCellA("" + refinedCellA + " &#8491;"); // angstrom symbol
				autoProcDetail.setUnitCellB("" + refinedCellB + " &#8491;");
				autoProcDetail.setUnitCellC("" + refinedCellC + " &#8491;");

				autoProcDetail.setUnitCellAlpha("" + apv.getRefinedCellAlpha() + " &#176;"); // degree symbol
				autoProcDetail.setUnitCellBeta("" + apv.getRefinedCellBeta() + " &#176;");
				autoProcDetail.setUnitCellGamma("" + apv.getRefinedCellGamma() + " &#176;");

				//
				List<AutoProcStatus3VO> autoProcEvents = new ArrayList<AutoProcStatus3VO>();
				if (autoProcId != null) {
					List<AutoProcIntegration3VO> autoProcIntegrations = autoProcIntegrationService.findByAutoProcId(autoProcId);
					if (!autoProcIntegrations.isEmpty()) {
						autoProcEvents = (autoProcIntegrations.iterator().next()).getAutoProcStatusList();
					}
				}
				autoProcDetail.setAutoProcEvents(autoProcEvents);

				// attachments
				List<IspybAutoProcAttachment3VO> listOfAutoProcAttachment = (List<IspybAutoProcAttachment3VO>) request.getSession()
						.getAttribute(Constants.ISPYB_AUTOPROC_ATTACH_LIST);
				Integer autoProcProgramId = null;
				if (apv != null)
					autoProcProgramId = apv.getAutoProcProgramVOId();

				if (autoProcId != null) {
					List<AutoProcIntegration3VO> autoProcIntegrations = autoProcIntegrationService.findByAutoProcId(autoProcId);

					if (!autoProcIntegrations.isEmpty()) {
						autoProcProgramId = (autoProcIntegrations.iterator().next()).getAutoProcProgramVOId();
					}
				}

				List<AutoProcProgramAttachment3VO> attachments = null;
				List<AutoProcAttachmentWebBean> autoProcProgAttachmentsWebBeans = new ArrayList<AutoProcAttachmentWebBean>();
				LOG.debug("autoProcProgramId = " + autoProcProgramId);

				if (autoProcProgramId != null) {
					attachments = new ArrayList<AutoProcProgramAttachment3VO>(appService.findByPk(autoProcProgramId, true)
							.getAttachmentVOs());

					if (!attachments.isEmpty()) {
						// attachmentWebBeans = new AutoProcAttachmentWebBean[attachments.size()];

						LOG.debug("nb attachments = " + attachments.size());
						for (Iterator<AutoProcProgramAttachment3VO> iterator = attachments.iterator(); iterator.hasNext();) {
							AutoProcProgramAttachment3VO att = iterator.next();
							AutoProcAttachmentWebBean attBean = new AutoProcAttachmentWebBean(att);
							// gets the ispyb auto proc attachment file
							IspybAutoProcAttachment3VO aAutoProcAttachment = getAutoProcAttachment(attBean.getFileName(),
									listOfAutoProcAttachment);
							if (aAutoProcAttachment == null) {
								// by default in XDS tab and output files
								aAutoProcAttachment = new IspybAutoProcAttachment3VO(null, attBean.getFileName(), "", "XDS", "output",
										false);
							}
							attBean.setIspybAutoProcAttachment(aAutoProcAttachment);
							autoProcProgAttachmentsWebBeans.add(attBean);
						}

					} else
						LOG.debug("attachments is empty");

				}

				// Issue 1507: Correction files for ID29 & ID23-1
				if (Constants.SITE_IS_ESRF()) {
					Integer dataCollectionId = null;
					if (BreadCrumbsForm.getIt(request).getSelectedDataCollection() != null)
						dataCollectionId = BreadCrumbsForm.getIt(request).getSelectedDataCollection().getDataCollectionId();
					if (dataCollectionId != null) {
						DataCollection3VO dataCollection = dataCollectionService.findByPk(dataCollectionId, false, false);
						String beamLineName = dataCollection.getDataCollectionGroupVO().getSessionVO().getBeamlineName();
						String[] correctionFiles = ESRFBeamlineEnum.retrieveCorrectionFilesNameWithName(beamLineName);
						if (correctionFiles != null) {
							for (int k = 0; k < correctionFiles.length; k++) {
								String correctionFileName = correctionFiles[k];
								String dir = ESRFBeamlineEnum.retrieveDirectoryNameWithName(beamLineName);
								if (dir != null) {
									String correctionFilePath = "/data/pyarch/" + dir + "/" + correctionFileName;
									String fullFilePath = PathUtils.FitPathToOS(correctionFilePath);
									File f = new File(fullFilePath);
									if (f != null && f.exists()) {
										// fake attachment
										AutoProcProgramAttachment3VO att = new AutoProcProgramAttachment3VO(-1, null,
												"Correction File", correctionFileName, correctionFilePath, null);
										AutoProcAttachmentWebBean attBean = new AutoProcAttachmentWebBean(att);
										IspybAutoProcAttachment3VO aAutoProcAttachment = new IspybAutoProcAttachment3VO(null,
												correctionFileName, "correction file", "XDS", "correction", false);
										attBean.setIspybAutoProcAttachment(aAutoProcAttachment);
										autoProcProgAttachmentsWebBeans.add(attBean);
										attachments.add(attBean);
									}
								}
							}// end for
						}
					}
				}
				autoProcDetail.setAutoProcProgAttachmentsWebBeans(autoProcProgAttachmentsWebBeans);
			} catch (NumberFormatException e) {

			}
			request.getSession().setAttribute("lastAutoProcIdSelected", autoProcDetail.getAutoProcId());
			//
			HashMap<String, Object> data = new HashMap<String, Object>();
			// context path
			data.put("contextPath", request.getContextPath());
			// autoProcDetail
			data.put("autoProcDetail", autoProcDetail);
			// data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ActionForward displayHtmlFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		try {
			ViewResultsForm form = (ViewResultsForm) actForm;
			String filePath = request.getParameter("HTML_FILE");
			String fileFullPath = "";
			if (filePath != null) {
				int id = filePath.lastIndexOf("/");
				int n = filePath.length();
				if (id != -1 && id != (n - 1) && id != (n - 2)) {
					fileFullPath = filePath.substring(0, id + 1);
				}
			}

			LOG.debug("displayWorkflowResultPages: fullPath= " + filePath);
			String fileContent = FileUtil.fileToString(filePath);

			// new path to view images
			// String pathImageUrl = request.getContextPath() + "/user/imageDownload.do?reqCode=getEDNAImage";
			String pathImageUrl = "imageDownload.do?reqCode=getEDNAImage";

			// String hrefImageUrl = request.getContextPath() + "/user/viewResults.do?reqCode=viewEDNAImage";
			String hrefImageUrl = "viewResults.do?reqCode=viewEDNAImage";

			String hrefFileUrl = "viewResults.do?reqCode=displayEDNAFile";

			// Case where the file is not found
			if (fileContent == null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewDNA"));
				return this.display(mapping, actForm, request, response);
			}

			// Format the file: change the <a href and <img src tags
			String fileContentChanged = UrlUtils.formatEDNApageURL(fileContent, pathImageUrl, hrefImageUrl, hrefFileUrl, fileFullPath);
			// form.setHtmlFileContent("<iframe width=\"100%\" height=\"700px\">" + fileContentChanged + "</iframe>");

			form.setHtmlFileContent(fileContentChanged);
			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewDNA"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		} else
			return mapping.findForward("viewHTMLFile");
	}

	public void getResultData(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
		LOG.debug("getResultData");
		List<String> errors = new ArrayList<String>();
		try {
			List<AutoProcessingInformation> autoProcList = new ArrayList<AutoProcessingInformation>();
			BreadCrumbsForm bar = BreadCrumbsForm.getIt(request);

			boolean displayOutputParam = false;
			boolean displayDenzoContent = false;

			// booleans to fix which tab will be selected by default
			boolean isEDNACharacterisation = false;
			boolean isAutoprocessing = false;
			boolean hasReprocessing = false;

			String rMerge = (String) request.getSession().getAttribute(Constants.RSYMM);
			String iSigma = (String) request.getSession().getAttribute(Constants.ISIGMA);

			double rMerge_d = DEFAULT_RMERGE;
			double iSigma_d = DEFAULT_ISIGMA;
			
			int nbRemoved = 0;

			try {
				if (rMerge != null && !rMerge.equals("undefined") && !rMerge.equals(""))
					rMerge_d = Double.parseDouble(rMerge);
				if (iSigma != null && !iSigma.equals("undefined") && !iSigma.equals(""))
					iSigma_d = Double.parseDouble(iSigma);
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}
			Integer dataCollectionId = null;
			List<List<AutoProcStatus3VO>> interruptedAutoProcEvents1 = new ArrayList<List<AutoProcStatus3VO>>();
			DataCollection3VO dc = null;
			// Just one of them could be visible on the bar
			if (bar.getSelectedDataCollection() != null) {
				dataCollectionId = bar.getSelectedDataCollection().getDataCollectionId();
			}
			if (dataCollectionId == null && request.getParameter(Constants.DATA_COLLECTION_ID) != null)
				dataCollectionId = new Integer(request.getParameter(Constants.DATA_COLLECTION_ID));
			if (dataCollectionId == null && request.getSession().getAttribute(Constants.DATA_COLLECTION_ID) != null)
				dataCollectionId = new Integer((Integer) request.getSession().getAttribute(Constants.DATA_COLLECTION_ID));

			if (dataCollectionId == null) {
				errors.add("dataCollectionId is null");
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				// data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return;
			}
			dc = dataCollectionService.findByPk(dataCollectionId, false, true);

			// interrupted autoProc
			if (dc != null) {
				List<AutoProcIntegration3VO> autoProcIntegrationList = dc.getAutoProcIntegrationsList();
				if (autoProcIntegrationList != null) {
					for (Iterator<AutoProcIntegration3VO> au = autoProcIntegrationList.iterator(); au.hasNext();) {
						AutoProcIntegration3VO autoProcIntegration = au.next();
						if (autoProcIntegration.getAutoProcProgramVO() == null && autoProcIntegration.getAutoProcStatusList() != null) {
							List<AutoProcStatus3VO> events = autoProcIntegration.getAutoProcStatusList();
							interruptedAutoProcEvents1.add(events);
						}
					}
				}
			}

			for (int i = 0; i < 2; i++) {
				boolean anomalous = (i > 0);
				List<AutoProc3VO> autoProcsAnomalous = apService.findByAnomalousDataCollectionIdAndOrderBySpaceGroupNumber(
						dataCollectionId, anomalous);
				if (autoProcsAnomalous != null) {
					nbRemoved=0;
					LOG.debug("..nbAutoProc " + anomalous + " found before rMerge =" + autoProcsAnomalous.size());
					for (Iterator<AutoProc3VO> a = autoProcsAnomalous.iterator(); a.hasNext();) {
						AutoProc3VO apv = a.next();

						List<AutoProcScalingStatistics3VO> scalingStatistics = apssService.findByAutoProcId(apv.getAutoProcId(),
								"innerShell");
						boolean existsUnderRmergeAndOverSigma = false;

						for (Iterator<AutoProcScalingStatistics3VO> j = scalingStatistics.iterator(); j.hasNext();) {
							AutoProcScalingStatistics3VO stats = j.next();
							if (stats.getRmerge() != null && stats.getRmerge() < rMerge_d && stats.getMeanIoverSigI() > iSigma_d)
								existsUnderRmergeAndOverSigma = true;
						}
						
						if (!existsUnderRmergeAndOverSigma){
							a.remove();
							nbRemoved = nbRemoved+1;
						}
					}
					LOG.debug("..nbAutoProc " + anomalous + " found=" + autoProcsAnomalous.size());
					for (Iterator<AutoProc3VO> iterator = autoProcsAnomalous.iterator(); iterator.hasNext();) {
						AutoProc3VO o = iterator.next();
						String cmdLine = "";
						if (o.getAutoProcProgramVO() != null && o.getAutoProcProgramVO().getProcessingPrograms() != null) {
							cmdLine = o.getAutoProcProgramVO().getProcessingPrograms();
						}
						float refinedCellA = ((float) ((int) (o.getRefinedCellA() * 10))) / 10; // round to 1 dp
						float refinedCellB = ((float) ((int) (o.getRefinedCellB() * 10))) / 10; // round to 1 dp
						float refinedCellC = ((float) ((int) (o.getRefinedCellC() * 10))) / 10; // round to 1 dp
						float refinedCellAlpha = ((float) ((int) (o.getRefinedCellAlpha() * 10))) / 10; // round to 1 dp
						float refinedCellBeta = ((float) ((int) (o.getRefinedCellBeta() * 10))) / 10; // round to 1 dp
						float refinedCellGamma = ((float) ((int) (o.getRefinedCellGamma() * 10))) / 10; // round to 1 dp

						String anoTxt = "OFF (Friedel pairs merged)"; // false
						if (anomalous) {
							anoTxt = "ON (Friedel pairs unmerged)"; // true
						}
						// String anomalousS = anomalous+"= "+anoTxt;
						AutoProcessingInformation info = new AutoProcessingInformation(o.getAutoProcId(), cmdLine, o.getSpaceGroup(),
								anoTxt, refinedCellA, refinedCellB, refinedCellC, refinedCellAlpha, refinedCellBeta, refinedCellGamma);
						autoProcList.add(info);
					}
				}

			}
			Integer autoProcIdSelected = (Integer) request.getSession().getAttribute("lastAutoProcIdSelected");
			// check if autoProcIdSelected is in the list
			boolean idExists = false;
			if (autoProcIdSelected != null) {
				for (Iterator<AutoProcessingInformation> iterator = autoProcList.iterator(); iterator.hasNext();) {
					AutoProcessingInformation info = iterator.next();
					if (info.getAutoProcId().equals(autoProcIdSelected)) {
						idExists = true;
						break;
					}
				}
			}
			if (!idExists) {
				autoProcIdSelected = null;
			}

			List<DataCollection3VO> listLastCollectVO = new ArrayList<DataCollection3VO>();
			if (dc != null)
				listLastCollectVO.add(dc);
			AutoProcShellWrapper wrapper = ViewDataCollectionAction.getAutoProcStatistics(listLastCollectVO, rMerge_d, iSigma_d);
			int dcIndex = 0;
			// is autoproc ?
			List<AutoProc3VO> autoProcs = apService.findByDataCollectionId(dataCollectionId);
			if (autoProcs != null && autoProcs.size() > 0)
				isAutoprocessing = true;

			String beamLineName = "";
			String proposal = "";
			String proteinAcronym = "";
			String pdbFileName = "";
			String experimentType = "";
			if (dc != null) {
				beamLineName = dc.getDataCollectionGroupVO().getSessionVO().getBeamlineName();
				proposal = dc.getDataCollectionGroupVO().getSessionVO().getProposalVO().getCode()
						+ dc.getDataCollectionGroupVO().getSessionVO().getProposalVO().getNumber();

				BLSample3VO sample = dc.getDataCollectionGroupVO().getBlSampleVO();
				if (sample != null && sample.getCrystalVO() != null && sample.getCrystalVO().getProteinVO() != null)
					proteinAcronym = sample.getCrystalVO().getProteinVO().getAcronym();

				if (sample != null && sample.getCrystalVO() != null && sample.getCrystalVO().hasPdbFile()) {
					pdbFileName = sample.getCrystalVO().getPdbFileName();
				}
				experimentType = dc.getDataCollectionGroupVO().getExperimentType();
			}

			AutoProc3VO autoProc = null;
			AutoProcScalingStatistics3VO autoProcStatisticsOverall = null;
			AutoProcScalingStatistics3VO autoProcStatisticsInner = null;
			AutoProcScalingStatistics3VO autoProcStatisticsOuter = null;
			ScreeningOutputLattice3VO lattice = null;
			ScreeningOutput3VO screeningOutput = null;

			String snapshotFullPath = "";
			boolean hasSnapshot = false;
			Screening3VO[] tabScreening = null;
			if (dc != null) {
				snapshotFullPath = dc.getXtalSnapshotFullPath1();
				snapshotFullPath = PathUtils.FitPathToOS(snapshotFullPath);

				if (snapshotFullPath != null)
					hasSnapshot = (new File(snapshotFullPath)).exists();

				autoProc = wrapper.getAutoProcs()[dcIndex];
				autoProcStatisticsOverall = wrapper.getScalingStatsOverall()[dcIndex];
				autoProcStatisticsInner = wrapper.getScalingStatsInner()[dcIndex];
				autoProcStatisticsOuter = wrapper.getScalingStatsOuter()[dcIndex];
				DataCollectionGroup3VO dcGroup = dataCollectionGroupService.findByPk(dc.getDataCollectionGroupVOId(), false, true);
				tabScreening = dcGroup.getScreeningsTab();
			}

			if (tabScreening != null && tabScreening.length > 0) {
				displayOutputParam = true;// there is at least 1 screening so we display the output params
				Screening3VO screeningVO = tabScreening[0];
				ScreeningOutput3VO[] screeningOutputTab = screeningVO.getScreeningOutputsTab();
				if (screeningOutputTab != null && screeningOutputTab.length > 0) {
					if (screeningOutputTab[0].getScreeningOutputLatticesTab() != null
							&& screeningOutputTab[0].getScreeningOutputLatticesTab().length > 0) {
						lattice = screeningOutputTab[0].getScreeningOutputLatticesTab()[0];
					}
					screeningOutput = screeningOutputTab[0];
				}
			}

			String autoprocessingStatus = "";
			String autoprocessingStep = "";
			if (wrapper != null && wrapper.getAutoProcs() != null && wrapper.getAutoProcs().length > dcIndex) {
				AutoProcIntegration3VO autoProcIntegration = wrapper.getAutoProcIntegrations()[dcIndex];
				if (autoProcIntegration != null) {
					List<AutoProcStatus3VO> autoProcEvents = autoProcIntegration.getAutoProcStatusList();
					if (autoProcEvents != null && autoProcEvents.size() > 0) {
						AutoProcStatus3VO st = autoProcEvents.get(autoProcEvents.size() - 1);
						autoprocessingStatus = st.getStatus();
						autoprocessingStep = st.getStep();
					}
				}
			}

			boolean hasAutoProcAttachment = false;
			if (wrapper != null && wrapper.getAutoProcs() != null) {

				for (int a = 0; a < autoProcs.size(); a++) {
					Integer autoProcProgramId = autoProcs.get(a).getAutoProcProgramVOId();
					if (autoProcProgramId != null) {
						List<AutoProcProgramAttachment3VO> attachments = appService.findByPk(autoProcProgramId, true)
								.getAttachmentListVOs();
						if (attachments != null && attachments.size() > 0) {
							hasAutoProcAttachment = true;
							break;
						}
					}
				}
			}

			DataCollectionBean dataCollection = null;
			if (dc != null) {
				dataCollection = new DataCollectionBean(dc, beamLineName, proposal, proteinAcronym, pdbFileName, experimentType,
						hasSnapshot, autoProc, autoProcStatisticsOverall, autoProcStatisticsInner, autoProcStatisticsOuter,
						screeningOutput, lattice, autoprocessingStatus, autoprocessingStep, hasAutoProcAttachment);
			}

			BeamLineSetup3VO beamline = null;
			Session3VO session = null;
			Detector3VO detector = null;

			String fullDenzoPath = null;
			boolean DenzonContentPresent = false;

			if (dc != null) {
				// beamline setup
				beamline = dc.getDataCollectionGroupVO().getSessionVO().getBeamLineSetupVO();
				// Session
				session = dc.getDataCollectionGroupVO().getSessionVO();
				// Detector
				detector = dc.getDetectorVO();

				// energy
				DecimalFormat df3 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
				df3.applyPattern("#####0.000");
				Double energy = null;
				if (dc.getWavelength() != null && dc.getWavelength().compareTo(new Double(0)) != 0)
					energy = Constants.WAVELENGTH_TO_ENERGY_CONSTANT / dc.getWavelength();
				if (energy != null)
					dataCollection.setEnergy(new Double(df3.format(energy)));
				else
					dataCollection.setEnergy(null);
				// axis start label
				String axisStartLabel = dc.getRotationAxis() == null ? "" : dc.getRotationAxis() + " start";
				dataCollection.setAxisStartLabel(axisStartLabel);
				// totalExposureTime
				if (dc.getExposureTime() != null && dc.getNumberOfImages() != null) {
					dataCollection.setTotalExposureTime(new Double(df3.format(dataCollection.getExposureTime()
							* dc.getNumberOfImages())));
				}
				// kappa
				Double kappa = dc.getKappaStart();
				String kappaStr = "";
				if (kappa == null || kappa.equals(Constants.SILLY_NUMBER))
					kappaStr = new String("0");
				else
					kappaStr = new String(kappa.toString());
				dataCollection.setKappa(kappaStr);
				// phi
				Double phi = dc.getPhiStart();
				String phiStr = "";
				if (phi == null || phi.equals(Constants.SILLY_NUMBER))
					phiStr = new String("0");
				else
					phiStr = new String(phi.toString());
				dataCollection.setPhi(phiStr);
				// undulatorGaps
				DecimalFormat df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
				// DecimalFormat df2 = new DecimalFormat("##0.##");
				df2.applyPattern("##0.##");
				StringBuffer buf = new StringBuffer();
				// if no type then there is no meaningful value
				// if no undulator 1 then no 2 and no 3
				if (beamline.getUndulatorType1() != null && beamline.getUndulatorType1().length() > 0) {
					if (dc.getUndulatorGap1() != null && !dc.getUndulatorGap1().equals(Constants.SILLY_NUMBER)) {
						Double gap1 = new Double(df2.format(dc.getUndulatorGap1()));
						buf.append(gap1.toString()).append(" mm ");
					}
					if (beamline.getUndulatorType2() != null && beamline.getUndulatorType2().length() > 0) {
						if (dc.getUndulatorGap2() != null && !dc.getUndulatorGap2().equals(Constants.SILLY_NUMBER)) {
							Double gap2 = new Double(df2.format(dc.getUndulatorGap2()));
							buf.append(gap2.toString()).append(" mm ");
						}
						if (beamline.getUndulatorType3() != null && beamline.getUndulatorType3().length() > 0) {
							if (dc.getUndulatorGap3() != null && !dc.getUndulatorGap3().equals(Constants.SILLY_NUMBER)) {
								Double gap3 = new Double(df2.format(dc.getUndulatorGap3()));
								buf.append(gap3.toString()).append(" mm ");
							}
						}
					}
				}
				String undulatorGaps = buf.toString();
				dataCollection.setUndulatorGaps(undulatorGaps);

				DecimalFormat nf1 = (DecimalFormat) NumberFormat.getInstance(Locale.UK);
				nf1.applyPattern("#");
				DecimalFormat df5 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
				df5.applyPattern("#####0.00000");

				// slitGapHorizontalMicro
				Integer slitGapHorizontalMicro = null;
				if (dc.getSlitGapHorizontal() != null) {
					// in DB beamsize unit is mm, display is in micrometer => conversion
					slitGapHorizontalMicro = new Integer(nf1.format(dc.getSlitGapHorizontal().doubleValue() * 1000));
				}
				dataCollection.setSlitGapHorizontalMicro(slitGapHorizontalMicro);
				// slitGapVerticalMicro
				Integer slitGapVerticalMicro = null;
				if (dc.getSlitGapVertical() != null) {
					// in DB beamsize unit is mm, display is in micrometer => conversion
					slitGapVerticalMicro = new Integer(nf1.format(dc.getSlitGapVertical().doubleValue() * 1000));
				}
				dataCollection.setSlitGapVerticalMicro(slitGapVerticalMicro);
				// detectorPixelSizeHorizontalMicro
				Double detectorPixelSizeHorizontalMicro = null;
				if (detector != null && detector.getDetectorPixelSizeHorizontal() != null) {
					// in DB pixel size unit is mm,
					detectorPixelSizeHorizontalMicro = new Double(df5.format(detector.getDetectorPixelSizeHorizontal()));
				}
				dataCollection.setDetectorPixelSizeHorizontalMicro(detectorPixelSizeHorizontalMicro);
				// detectorPixelSizeHorizontalMicro
				Double detectorPixelSizeVerticalMicro = null;
				if (detector != null && detector.getDetectorPixelSizeVertical() != null) {
					// in DB pixel size unit is mm,
					detectorPixelSizeVerticalMicro = new Double(df5.format(detector.getDetectorPixelSizeVertical()));
				}
				dataCollection.setDetectorPixelSizeVerticalMicro(detectorPixelSizeVerticalMicro);
				// beamSizeAtSampleXMicro
				Integer beamSizeAtSampleXMicro = null;
				if (dc.getBeamSizeAtSampleX() != null) {
					// in DB beamsize unit is mm, display is in micrometer => conversion
					beamSizeAtSampleXMicro = new Integer(nf1.format(dc.getBeamSizeAtSampleX().doubleValue() * 1000));
				}
				dataCollection.setBeamSizeAtSampleXMicro(beamSizeAtSampleXMicro);
				// beamSizeAtSampleYMicro
				Integer beamSizeAtSampleYMicro = null;
				if (dc.getBeamSizeAtSampleY() != null) {
					// in DB beamsize unit is mm, display is in micrometer => conversion
					beamSizeAtSampleYMicro = new Integer(nf1.format(dc.getBeamSizeAtSampleY().doubleValue() * 1000));
				}
				dataCollection.setBeamSizeAtSampleYMicro(beamSizeAtSampleYMicro);
				// beamDivergenceHorizontalInt
				Integer beamDivergenceHorizontalInt = null;
				if (beamline.getBeamDivergenceHorizontal() != null) {
					beamDivergenceHorizontalInt = new Integer(nf1.format(beamline.getBeamDivergenceHorizontal()));
				}
				dataCollection.setBeamDivergenceHorizontalInt(beamDivergenceHorizontalInt);
				// beamDivergenceVerticalInt
				Integer beamDivergenceVerticalInt = null;
				if (beamline.getBeamDivergenceVertical() != null) {
					beamDivergenceVerticalInt = new Integer(nf1.format(beamline.getBeamDivergenceVertical()));
				}
				dataCollection.setBeamDivergenceVerticalInt(beamDivergenceVerticalInt);
				// DNA or EDNA Content present ?
				String fullDNAPath = PathUtils.getFullDNAPath(dc);
				String fullEDNAPath = PathUtils.getFullEDNAPath(dc);
				boolean EDNAContentPresent = (new File(fullEDNAPath + EDNA_FILES_INDEX_FILE)).exists()
						|| (new File(fullDNAPath + Constants.DNA_FILES_INDEX_FILE)).exists();
				isEDNACharacterisation = EDNAContentPresent;

				// Denzo Content present ?

				if (Constants.DENZO_ENABLED) {
					fullDenzoPath = FileUtil.GetFullDenzoPath(dc);
					DenzonContentPresent = (new File(fullDenzoPath)).exists();
					displayDenzoContent = DisplayDenzoContent(dc);

					if (DenzonContentPresent) // Check html file present
					{
						File denzoIndex = new File(fullDenzoPath + DENZO_HTML_INDEX);
						if (!denzoIndex.exists()) {
							errors.add("Denzo File does not exist " + denzoIndex);
							DenzonContentPresent = false;
						}
					}
				}
				
				if (Constants.SITE_IS_EMBL()) {
					hasReprocessing = true;
				}
			}

			//
			HashMap<String, Object> data = new HashMap<String, Object>();
			// context path
			data.put("contextPath", request.getContextPath());
			// autoProcList
			data.put("autoProcList", autoProcList);
			// autoProcIdSelected
			data.put("autoProcIdSelected", autoProcIdSelected);
			// rMerge & iSigma
			data.put("rMerge", rMerge);
			data.put("iSigma", iSigma);
			data.put("nbRemoved", nbRemoved);
			data.put("dataCollectionId", dataCollectionId);
			data.put("dataCollection", dataCollection);
			// beamlinesetup
			data.put("beamline", beamline);
			// session
			data.put("session", session);
			// detector
			data.put("detector", detector);
			// interrupted autoProc
			data.put("interruptedAutoProcEvents", interruptedAutoProcEvents1);
			// displayOutputParam
			data.put("displayOutputParam", displayOutputParam);
			// isEDNACharacterisation
			data.put("isEDNACharacterisation", isEDNACharacterisation);
			// isAutoprocessing
			data.put("isAutoprocessing", isAutoprocessing);
			// displayDenzoContent
			data.put("displayDenzoContent", displayDenzoContent);
			// DenzonContentPresent
			data.put("DenzonContentPresent", DenzonContentPresent);
			// fullDenzoPath
			data.put("fullDenzoPath", fullDenzoPath);
			// reprocessing enabled
			data.put("hasReprocessing", hasReprocessing);
			// data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
