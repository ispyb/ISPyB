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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.util.FileUtil;
import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.common.util.PathUtils;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.collections.BeamLineSetup3Service;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.services.collections.DataCollectionGroup3Service;
import ispyb.server.mx.services.screening.Screening3Service;
import ispyb.server.mx.services.screening.ScreeningOutput3Service;
import ispyb.server.mx.services.screening.ScreeningStrategy3Service;
import ispyb.server.mx.services.screening.ScreeningStrategySubWedge3Service;
import ispyb.server.mx.services.screening.ScreeningStrategyWedge3Service;
import ispyb.server.mx.vos.collections.BeamLineSetup3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
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
 * @struts.action name="viewResultsForm" path="/user/viewImageWall" input="user.results.viewImageWall.page"
 *                validate="false" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * @struts.action-forward name="successImageWall" path="user.results.viewImageWall.page"
 * @struts.action-forward name="localcontactsuccessImageWall" path="localcontact.results.viewImageWall.page"
 * @struts.action-forward name="managersuccessImageWall" path="manager.results.viewImageWall.page"
 * @struts.action-forward name="viewImageApplet" path="user.results.viewImageApplet.page"
 * @struts.action-forward name="viewDNAImages" path="user.results.DNAImages.page"
 * @struts.action-forward name="viewDNAFiles" path="user.results.DNAFiles.page"
 * @struts.action-forward name="viewOtherDNAFiles" path="user.results.otherDNAFiles.page"
 * @struts.action-forward name="viewDNATextFiles" path="user.results.DNATextFiles.page"
 * @struts.action-forward name="viewDenzoImages" path="user.results.DenzoImages.page"
 * @struts.action-forward name="viewJpegImage" path="user.results.viewJpegImage.page"
 * @struts.action-forward name="localcontactviewJpegImage" path="localcontact.results.viewJpegImage.page"
 * @struts.action-forward name="managerviewJpegImage" path="manager.results.viewJpegImage.page"
 * @struts.action-forward name="viewImageThumbnails" path="user.results.viewImageThumbnails.page"
 */
public class ViewImageWallAction extends DispatchAction {

	private String dataCollectionIdst;

	ActionMessages errors = new ActionMessages();

	private static final String DENZO_HTML_INDEX = "index.html";

	private static final int SNAPSHOT_EXPECTED_NUMBER = 4;


	
	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();


	
	
	private BeamLineSetup3Service beamLineSetupService;
	
	private DataCollection3Service dataCollectionService;
	
	private DataCollectionGroup3Service dataCollectionGroupService;
	
	private Screening3Service screeningService;
	
	private ScreeningOutput3Service screeningOutputService;
	
	private ScreeningStrategy3Service screeningStrategyService;
	
	private ScreeningStrategyWedge3Service screeningStrategyWedgeService;
	
	private ScreeningStrategySubWedge3Service screeningStrategySubWedgeService;
	

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {

		this.beamLineSetupService = (BeamLineSetup3Service)ejb3ServiceLocator.getLocalService(BeamLineSetup3Service.class);
		this.dataCollectionService = (DataCollection3Service)ejb3ServiceLocator.getLocalService(DataCollection3Service.class);
		this.dataCollectionGroupService = (DataCollectionGroup3Service)ejb3ServiceLocator.getLocalService(DataCollectionGroup3Service.class);
		this.screeningService = (Screening3Service) ejb3ServiceLocator.getLocalService(Screening3Service.class);
		this.screeningOutputService = (ScreeningOutput3Service) ejb3ServiceLocator.getLocalService(ScreeningOutput3Service.class);
		this.screeningStrategyService = (ScreeningStrategy3Service) ejb3ServiceLocator.getLocalService(ScreeningStrategy3Service.class);
		this.screeningStrategyWedgeService = (ScreeningStrategyWedge3Service) ejb3ServiceLocator.getLocalService(ScreeningStrategyWedge3Service.class);
		this.screeningStrategySubWedgeService = (ScreeningStrategySubWedge3Service) ejb3ServiceLocator.getLocalService(ScreeningStrategySubWedge3Service.class);
		
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
	@SuppressWarnings("unchecked")
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();
		boolean redirectToError = true;
		boolean displayOutputParam = false;

		try {
			ViewResultsForm form = (ViewResultsForm) actForm;

			boolean DNAContentPresent = false;
			boolean DNARankingProjectFilePresent = false;
			boolean DNARankingSummaryFilePresent = false;
			boolean DenzonContentPresent = false;
			boolean displayDenzoContent = false;

			boolean dna_logContentPresent = false;
			boolean mosflm_triclintContentPresent = false;
			boolean scala_logContentPresent = false;
			boolean pointlessContentPresent = false;

			Integer nbImagesHorizontal = form.getNbImagesHorizontal();
			Integer nbImagesVertical = form.getNbImagesVertical();

			Integer dataCollectionId = null;
			dataCollectionIdst = request.getParameter(Constants.DATA_COLLECTION_ID);
			if (dataCollectionIdst != null) {
				dataCollectionId = new Integer(dataCollectionIdst);
			} else {
				dataCollectionId = BreadCrumbsForm.getIt(request).getSelectedDataCollection().getDataCollectionId();
			}

			String rankingProjectFileName = null;
			String rankingSummaryFileName = null;

			DataCollection3VO  dataCollectionVO = dataCollectionService.findByPk(dataCollectionId, true, true);

			Screening3VO[] screeningList = null;

			DataCollectionGroup3VO dataCollectionGroup = dataCollectionGroupService.findByPk(dataCollectionVO.getDataCollectionGroupVOId(), false, true);
			
			Session3VO sessionlv = dataCollectionGroup.getSessionVO();
			form.setSession(sessionlv);

			if (sessionlv.getBeamLineSetupVOId() != null && sessionlv.getBeamLineSetupVOId().intValue() != 0) {
				Integer beamLineId = new Integer(sessionlv.getBeamLineSetupVOId());
				BeamLineSetup3VO beamLinelv = beamLineSetupService.findByPk(beamLineId);
				form.setBeamLine(beamLinelv);
				form.setUndulatorTypes(beamLinelv.getUndulatorType1(), beamLinelv.getUndulatorType2(),
						beamLinelv.getUndulatorType3());
			}
			Screening3VO[] screenings = dataCollectionGroup.getScreeningsTab();
			if (screenings.length > 0) {
				displayOutputParam = true;// there is at least 1 screening so we display the output params


				int length = screenings.length;
				screeningList = screenings;
				// if many screenings, only use the last one

				ScreeningRank3VO srlv = new ScreeningRank3VO();
				// ScreeningRankSetLightValue srslv = new ScreeningRankSetLightValue();
				ScreeningOutput3VO sov = new ScreeningOutput3VO();
				ScreeningRankSet3VO srsv = new ScreeningRankSet3VO();
				ScreeningOutputLattice3VO solav = new ScreeningOutputLattice3VO();

				ScreeningStrategy3VO[] screeningStrategyList;
				List<ScreeningStrategyValueInfo> screeningInfoList = new ArrayList<ScreeningStrategyValueInfo>();


				//Screening3VO sv = screening.findByPrimaryKey(screeningId);
				Screening3VO sv = screeningList[length - 1];
				sv = screeningService.loadEager(sv);

				ScreeningRank3VO[] screeningRanks = sv.getScreeningRanksTab();
				if (screeningRanks.length > 0) {
					srlv = screeningRanks[0];
					//srsv = screeningRankSet.findByPrimaryKey(srlv.getScreeningRankSetId());
					rankingProjectFileName = srsv.getRankingProjectFileName();
					rankingSummaryFileName = srsv.getRankingSummaryFileName();
				}

				ScreeningOutput3VO[] screeningOutputs = sv.getScreeningOutputsTab();
				if (screeningOutputs.length > 0) {
					sov = screeningOutputs[0];
					sov = screeningOutputService.loadEager(sov);
					//sov = screeningOutput.findByPrimaryKey(solv.getPrimaryKey());
				}

				ScreeningOutputLattice3VO[] screeningOutputLattices = sov.getScreeningOutputLatticesTab();
				if (screeningOutputLattices.length > 0) {

					solav = screeningOutputLattices[0];
					form.setScreeningOutputLattice(solav);
				}

				ScreeningStrategy3VO[] screeningStrategys = sov.getScreeningStrategysTab();
				if (screeningStrategys.length > 0) {

					screeningStrategyList = screeningStrategys;

					List<ScreeningStrategyWedge3VO> wedgeList= new ArrayList<ScreeningStrategyWedge3VO>();
					
					for (int j = 0; j < screeningStrategyList.length; j++) {
						ScreeningStrategyValueInfo ssvi = new ScreeningStrategyValueInfo(
								screeningStrategyService.findByPk(screeningStrategyList[j].getScreeningStrategyId(),
										true));
						ssvi.setProgramLog(dataCollectionVO);
						screeningInfoList.add(ssvi);
						
						ArrayList<ScreeningStrategyWedge3VO> list = ssvi.getScreeningStrategyWedgesList();
						if(list != null)
							wedgeList.addAll(list);
					}
					form.setScreeningStrategyList(screeningStrategyList);
					form.setListStrategiesInfo(screeningInfoList);

					// strategy wedge
					ScreeningStrategyWedge3VO[] screeningStrategyWedgeList;
					List<ScreeningStrategyWedgeValueInfo> screeningWedgeInfoList;
					
					int nb = wedgeList.size();
					screeningStrategyWedgeList = new ScreeningStrategyWedge3VO[nb];
					for(int j=0; j<nb; j++){
						screeningStrategyWedgeList[j] = wedgeList.get(j);
					}
					
					screeningWedgeInfoList = new ArrayList<ScreeningStrategyWedgeValueInfo>();
					for (int k = 0; k < screeningStrategyWedgeList.length; k++) {
						ScreeningStrategyWedgeValueInfo sw = new ScreeningStrategyWedgeValueInfo(
								screeningStrategyWedgeService.findByPk(screeningStrategyWedgeList[k].getScreeningStrategyWedgeId(), true));
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
						ScreeningStrategySubWedge3VO[] screeningStrategysSubWedge = new ScreeningStrategyWedgeValueInfo(
								screeningStrategyWedgeService.findByPk(screeningStrategyWedgeList[j].getScreeningStrategyWedgeId(), true))
								.getScreeningStrategySubWedgesTab();

						screeningStrategySubWedgeListAll[j] = screeningStrategysSubWedge;
						screeningSubWedgeInfoListAll[j] = new ArrayList<ScreeningStrategySubWedgeValueInfo>();
						for (int k = 0; k < screeningStrategySubWedgeListAll[j].length; k++) {
							ScreeningStrategySubWedgeValueInfo ssw = new ScreeningStrategySubWedgeValueInfo(
									screeningStrategySubWedgeService.findByPk(screeningStrategySubWedgeListAll[j][k]
											.getScreeningStrategySubWedgeId()));
							screeningSubWedgeInfoListAll[j].add(ssw);
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
								screeningStrategyWedgeService.findByPk(screeningStrategyWedgeList[screeningStrategyWedgeSelId]
										.getScreeningStrategyWedgeId(), true)).getScreeningStrategySubWedgesTab();

						screeningStrategySubWedgeList = screeningStrategysSubWedge;
						for (int k = 0; k < screeningStrategySubWedgeList.length; k++) {
							ScreeningStrategySubWedgeValueInfo sw = new ScreeningStrategySubWedgeValueInfo(
									screeningStrategySubWedgeService.findByPk(screeningStrategySubWedgeList[k].getScreeningStrategySubWedgeId()));
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

			if (dataCollectionVO.getDataCollectionGroupVO().getBlSampleVO() != null) {
				//BLSample3VO bslv = sampleService.findByPk(dclv.getBlSampleId(), false);
				BLSample3VO bslv =dataCollectionVO.getDataCollectionGroupVO().getBlSampleVO();
				BreadCrumbsForm.getIt(request).setSelectedSample(bslv);

				Crystal3VO clv = bslv.getCrystalVO();

				Protein3VO plv = clv.getProteinVO();

				form.setCrystal(clv);
				form.setProtein(plv);
				form.setSample(bslv);

			}
			// --- Get Image List
			List<ImageValueInfo> imageList = FileUtil.GetImageList(dataCollectionId, null, null, nbImagesHorizontal, nbImagesVertical,
					request);

			// DNA Content present ?
			String fullDNAPath = PathUtils.getFullDNAPath(dataCollectionVO);
			DNAContentPresent = (new File(fullDNAPath + Constants.DNA_FILES_INDEX_FILE)).exists();
			String fullDNARankingPath = PathUtils.GetFullDNARankingPath(dataCollectionId);
			DNARankingProjectFilePresent = (new File(fullDNARankingPath + rankingProjectFileName).exists());
			DNARankingSummaryFilePresent = (new File(fullDNARankingPath + rankingSummaryFileName).exists());

			String fullLogPath = PathUtils.GetFullLogPath(dataCollectionVO);
			dna_logContentPresent = (new File(fullLogPath + Constants.DNA_FILES_LOG_FILE)).exists();
			mosflm_triclintContentPresent = (new File(fullLogPath + Constants.DNA_FILES_MOSFLM_TRICLINT_FILE)).exists();
			scala_logContentPresent = (new File(fullLogPath + Constants.DNA_FILES_SCALA_FILE)).exists();
			pointlessContentPresent = (new File(fullLogPath + Constants.DNA_FILES_POINTLESS_FILE)).exists();

			// Denzo Content present ?
			//DataCollectionLightValue dcValue = dataCollection.findByPrimaryKey(dataCollectionId);
			String fullDenzoPath = FileUtil.GetFullDenzoPath(dataCollectionVO);
			DenzonContentPresent = (new File(fullDenzoPath)).exists();
			displayDenzoContent = ViewResultsAction.DisplayDenzoContent(dataCollectionVO);

			// Snapshot Image present ?
			ArrayList<SnapshotInfo> listSnapshots = FileUtil.GetFullSnapshotPath(dataCollectionVO);
			String expectedSnapshotPath = ((SnapshotInfo) listSnapshots.get(SNAPSHOT_EXPECTED_NUMBER))
					.getFileLocation();

			if (DenzonContentPresent) // Check html file present
			{
				File denzoIndex = new File(fullDenzoPath + DENZO_HTML_INDEX);
				if (!denzoIndex.exists()) {
					redirectToError = false;
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "File does not exist "
							+ denzoIndex));
					saveErrors(request, errors);
					DenzonContentPresent = false;
				}
			}

			// --- Populate Form ---
			form.setDisplayOutputParam(displayOutputParam);
			form.setDNAContentPresent(DNAContentPresent);
			form.setScala_logContentPresent(scala_logContentPresent);
			form.setDna_logContentPresent(dna_logContentPresent);
			form.setMosflm_triclintContentPresent(mosflm_triclintContentPresent);
			form.setPointlessContentPresent(pointlessContentPresent);

			form.setDNARankingProjectFilePresent(DNARankingProjectFilePresent);
			form.setDNARankingSummaryFilePresent(DNARankingSummaryFilePresent);
			form.setDenzonContentPresent(DenzonContentPresent);
			form.setExpectedDenzoPath(fullDenzoPath);
			form.setDisplayDenzoContent(displayDenzoContent);
			form.setDataCollectionId(dataCollectionId);
			form.setDataCollection(dataCollectionVO);
			form.setEnergy(dataCollectionVO.getWavelength());
			form.setUndulatorGaps(dataCollectionVO.getUndulatorGap1(), dataCollectionVO.getUndulatorGap2(), dataCollectionVO.getUndulatorGap3());
			form.setAxisStartLabel(dataCollectionVO.getRotationAxis());
			form.setScreeningList(screeningList);
			form.setListSnapshots(listSnapshots);
			form.setExpectedSnapshotPath(expectedSnapshotPath);

			form.setListInfo(imageList);
			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

			// Fill the BreadCrumbs
			BreadCrumbsForm.getIt(request).setSelectedImage(null);
			BreadCrumbsForm.getIt(request).setSelectedDataCollection(dataCollectionVO);
			
			displayEDNA(mapping, actForm, request, in_reponse, errors);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.view"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty() && redirectToError) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		} else{
			RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
			String role = roleObject.getName();
			if (role.equals(Constants.ROLE_LOCALCONTACT)) {
				return mapping.findForward("localcontactsuccessImageWall");
			}else if (role.equals(Constants.ROLE_MANAGER)) {
				return mapping.findForward("managersuccessImageWall");
			}else
				return mapping.findForward("successImageWall");
		}
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
			// String fullEDNAPath = archivePath + Constants.EDNA_FILES_SUFIX;

			boolean isFileExist = new File(archivePath + Constants.EDNA_FILES_SUFIX).exists();
			String fullEDNAPath = archivePath;
			if (Constants.SITE_IS_DLS() || (isFileExist)) {
				fullEDNAPath += Constants.EDNA_FILES_SUFIX;
			}

			String indexPath = Constants.SITE_IS_DLS() ? archivePath + ViewResultsAction.EDNA_FILES_INDEX_FILE : archivePath + ViewResultsAction.getEdna_index_file(dc);

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

}
