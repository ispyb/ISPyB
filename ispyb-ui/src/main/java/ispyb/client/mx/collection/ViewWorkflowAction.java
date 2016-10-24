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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

import ispyb.client.SiteSpecific;
import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.reference.ViewReferenceAction;
import ispyb.client.common.util.Confidentiality;
import ispyb.client.common.util.FileUtil;
import ispyb.client.common.util.GSonUtils;
import ispyb.client.common.util.UrlUtils;
import ispyb.client.mx.results.ImageValueInfo;
import ispyb.client.mx.results.SnapshotInfo;
import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.common.util.IspybFileUtils;
import ispyb.common.util.PathUtils;
import ispyb.common.util.StringUtils;
import ispyb.common.util.beamlines.EMBLBeamlineEnum;
import ispyb.common.util.beamlines.ESRFBeamlineEnum;
import ispyb.common.util.beamlines.MAXIVBeamlineEnum;
import ispyb.common.util.beamlines.SOLEILBeamlineEnum;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.autoproc.ImageQualityIndicators3Service;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.services.collections.DataCollectionGroup3Service;
import ispyb.server.mx.services.collections.GridInfo3Service;
import ispyb.server.mx.services.collections.Image3Service;
import ispyb.server.mx.services.collections.Workflow3Service;
import ispyb.server.mx.services.collections.WorkflowMesh3Service;
import ispyb.server.mx.vos.autoproc.ImageQualityIndicators3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.GridInfo3VO;
import ispyb.server.mx.vos.collections.Image3VO;
import ispyb.server.mx.vos.collections.IspybCrystalClass3VO;
import ispyb.server.mx.vos.collections.IspybReference3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.collections.Workflow3VO;
import ispyb.server.mx.vos.collections.WorkflowMesh3VO;

/**
 * 
 * 
 * @struts.action name="viewWorkflowForm" path="/user/viewWorkflow" input="user.collection.viewWorkflow.page"
 *                validate="false" parameter="reqCode" scope="request"
 * @struts.action-forward name="error" path="site.default.error.page"
 * @struts.action-forward name="viewWorkflow" path="user.collection.viewWorkflow.page"
 * @struts.action-forward name="viewWorkflowManager" path="manager.collection.viewWorkflow.page"
 * @struts.action-forward name="viewWorkflowFedexManager" path="fedexmanager.collection.viewWorkflow.page" *
 * @struts.action-forward name="viewWorkflowLocalContact" path="localContact.collection.viewWorkflow.page"
 * 
 */
public class ViewWorkflowAction extends DispatchAction {
	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private final Logger LOG = Logger.getLogger(ViewWorkflowAction.class);

	protected DataCollectionGroup3Service dataCollectionGroupService;

	protected DataCollection3Service dataCollectionService;

	protected Session3Service sessionService;

	protected Workflow3Service workflowService;

	protected GridInfo3Service gridInfoService;

	protected WorkflowMesh3Service workflowMeshService;

	protected ImageQualityIndicators3Service imageQualityIndicatorsService;

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {
		this.sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
		this.workflowService = (Workflow3Service) ejb3ServiceLocator.getLocalService(Workflow3Service.class);
		this.dataCollectionGroupService = (DataCollectionGroup3Service) ejb3ServiceLocator.getLocalService(DataCollectionGroup3Service.class);
		this.dataCollectionService = (DataCollection3Service) ejb3ServiceLocator.getLocalService(DataCollection3Service.class);
		this.imageQualityIndicatorsService = (ImageQualityIndicators3Service) ejb3ServiceLocator.getLocalService(ImageQualityIndicators3Service.class);
		this.gridInfoService = (GridInfo3Service) ejb3ServiceLocator.getLocalService(GridInfo3Service.class);
		this.workflowMeshService = (WorkflowMesh3Service) ejb3ServiceLocator.getLocalService(WorkflowMesh3Service.class);

	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	/**
	 * To display all the Sessions belonging to a proposalId.
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();
		Integer workflowId = null;
		ViewWorkflowForm form = (ViewWorkflowForm) actForm;
		BreadCrumbsForm.getIt(request).setSelectedDataCollectionGroup(null);
		if (request.getParameter(Constants.WORKFLOW_ID) != null)
			workflowId = new Integer(request.getParameter(Constants.WORKFLOW_ID));
		try{
			String rMerge = request.getParameter(Constants.RSYMM);
			String iSigma = request.getParameter(Constants.ISIGMA);
			request.getSession().setAttribute(Constants.RSYMM, rMerge);
			request.getSession().setAttribute(Constants.ISIGMA, iSigma);

			if (workflowId != null){
				form.setWorkflowId(workflowId);
				request.getSession().setAttribute(Constants.WORKFLOW_ID, workflowId);
				Workflow3VO workflowVO = workflowService.findByPk(workflowId);
				if (dataCollectionGroupService.findByWorkflow(workflowId) != null) {
					Session3VO ses = dataCollectionGroupService.findByWorkflow(workflowId).get(0).getSessionVO();
					BreadCrumbsForm.getIt(request).setSelectedSession(ses);
				}
				BreadCrumbsForm.getIt(request).setSelectedWorkflow(workflowVO);
				RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
				String role = roleObject.getName();

				if (role.equals(Constants.FXMANAGE_ROLE_NAME)) {
					return mapping.findForward("viewWorkflowFedexManager");
				} else if (role.equals(Constants.ROLE_MANAGER)) {
					return mapping.findForward("viewWorkflowManager");
				} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
					return mapping.findForward("viewWorkflowLocalContact");
				} else {
					return mapping.findForward("viewWorkflow");
				}
			}else{
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "workflowId is null"));
				saveErrors(request, errors);
				return (mapping.findForward("error"));
			}

		}catch(Exception e){
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.collection.viewWorkflow"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();	
		}
		return (mapping.findForward("error"));
	}

	@SuppressWarnings("unchecked")
	public void  getWorkflow(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response){

		Integer workflowId = null;
		boolean isIndus = SiteSpecific.isIndustrial(request);
		boolean isManager = Confidentiality.isManager(request);
		List<String> errors = new ArrayList<String>(); 

		try {
			// Search first in request then in BreadCrumbs
			if (request.getParameter(Constants.WORKFLOW_ID) != null)
				workflowId = new Integer(request.getParameter(Constants.WORKFLOW_ID));
			else if (request.getSession().getAttribute(Constants.WORKFLOW_ID) != null){
				workflowId =(Integer)request.getSession().getAttribute(Constants.WORKFLOW_ID);
			}else {
				ViewWorkflowForm form = (ViewWorkflowForm) actForm;
				workflowId = form.getWorkflowId();
			}

			if (workflowId == null){
				errors.add("No workflow selected");
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				//data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return;
			}
			Workflow3VO workflowVO = workflowService.findByPk(workflowId);

			Integer sessionId = null;
			if (BreadCrumbsForm.getIt(request).getSelectedSession() != null) {
				sessionId =BreadCrumbsForm.getIt(request).getSelectedSession().getSessionId();
			}
			if (sessionId == null){
				errors.add("No session found");
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				//data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return;
			}
			Session3VO slv = sessionService.findByPk(sessionId, false/* withCollections */, false, false);
			// Confidentiality (check if object proposalId matches)
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			if (!Confidentiality.isAccessAllowed(request, slv.getProposalVO().getProposalId())) {
				errors.add("Access denied");
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				//data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return;
			}

			LOG.debug("displayWorkflow: proposalId/workflowId = " + proposalId + "/" + workflowId);

			// Fill the information bar
			BreadCrumbsForm.getItClean(request);
			BreadCrumbsForm.getIt(request).setSelectedSession(slv);
			BreadCrumbsForm.getIt(request).setSelectedWorkflow(workflowVO);


			// Get data collection group
			List<DataCollectionGroup3VO> listDataCollectionGroup = dataCollectionGroupService.findByWorkflow(workflowId);
			List<List<DataCollection3VO>> listOfCollect = new ArrayList<List<DataCollection3VO>>();
			List<List<ImageValueInfo>> listOfImages = new ArrayList<List<ImageValueInfo>>();
			List<SnapshotInfo> listOfSnapshot = new ArrayList<SnapshotInfo>();
			for (Iterator<DataCollectionGroup3VO> group = listDataCollectionGroup.iterator(); group.hasNext();){
				// Get data collections
				DataCollectionGroup3VO dcg = group.next();
				List<DataCollection3VO> dataCollectionList = new ArrayList<DataCollection3VO>();
				if (isIndus)
					dataCollectionList = dataCollectionService.findFiltered(null, null, null, null, new Byte("1"), dcg.getDataCollectionGroupId());
				else
					dataCollectionList = dataCollectionService.findFiltered(null, null, null, null, null, dcg.getDataCollectionGroupId());
				listOfCollect.add(dataCollectionList);
				// iamgeList
				List<ImageValueInfo> imageList = FileUtil.getImageListForDataCollectionGroup(dcg.getDataCollectionGroupId(), null, null, null, null, request);
				listOfImages.add(imageList);
				// snapshot info
				String fullSnapshotPath = dcg.getXtalSnapshotFullPath();
				boolean fileExists = false;
				if (fullSnapshotPath != null){
					fileExists = PathUtils.fileExists(fullSnapshotPath) == 1;
				}
				fullSnapshotPath = PathUtils.FitPathToOS(fullSnapshotPath);
				SnapshotInfo snapshot = new SnapshotInfo(fullSnapshotPath, fileExists);
				listOfSnapshot.add(snapshot);

			}
			// autoproc
			double rSymmCutoff = 10;
			double iSigmaCutoff = 1;
			String rMerge = (String) request.getSession().getAttribute(Constants.RSYMM);
			String iSigma = (String) request.getSession().getAttribute(Constants.ISIGMA);

			try {
				if (rMerge != null && !rMerge.equals("undefined") && !rMerge.equals(""))
					rSymmCutoff = Double.parseDouble(rMerge);
				if (iSigma != null && !iSigma.equals("undefined") && !iSigma.equals(""))
					iSigmaCutoff = Double.parseDouble(iSigma);
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}

			List<List<DataCollectionBean>> collectionList =new ArrayList<List<DataCollectionBean>>();
			for (Iterator<List<DataCollection3VO>> iterator = listOfCollect.iterator(); iterator.hasNext();) {
				List<DataCollection3VO> list = (List<DataCollection3VO>) iterator.next();
				List<DataCollectionBean> listdcb = ViewDataCollectionAction.getDataCollectionBeanList(list, rSymmCutoff, iSigmaCutoff);
				collectionList.add(listdcb);
			}
			LOG.debug("ViewDataCollection:findByDataCollectionGroupId finished");
			int[] displayWorkflowStatus = ViewWorkflowAction.getDisplayWorkflowStatus(workflowVO);
			int displayImage = displayWorkflowStatus[0];
			int displayWorkflow = displayWorkflowStatus[1];
			int displayMesh = displayWorkflowStatus[2];
			int displayDehydration = displayWorkflowStatus[3];

			List<IspybCrystalClass3VO> listOfCrystalClass = (List<IspybCrystalClass3VO>) request.getSession().getAttribute(Constants.ISPYB_CRYSTAL_CLASS_LIST);

			// list of references
			List<IspybReference3VO> listOfReferences = new ArrayList<IspybReference3VO>();
			List<IspybReference3VO> allRef = (List<IspybReference3VO>) request.getSession().getAttribute(Constants.ISPYB_REFERENCE_LIST);
			List<String> listOfBeamlines = new ArrayList<String>();
			for(Iterator<List<DataCollection3VO>> it = listOfCollect.iterator(); it.hasNext();){
				List<DataCollection3VO> dataCollectionList= it.next();
				List<String> listBL = ViewDataCollectionAction.getListOfBeamlines(dataCollectionList);
				listOfBeamlines.addAll(listBL);
			}

			listOfReferences = ViewReferenceAction.getReferences(listOfBeamlines, allRef, displayMesh);
			String referenceText = ViewReferenceAction.getReferenceText(listOfBeamlines);
			
			// workflow info
			Workflow workflow = ViewWorkflowAction.getWorkflowInfo(workflowVO);
			

			// dehydration 
			List<DehydrationData> dehydrationDataValuesAll = new ArrayList<DehydrationData>();
			FileInformation dataFile = null;
			int dataFileExists = 0;
			String dataFileFullPath = "";
			
			if (displayDehydration == 1 && dataFileExists == 1) {
				dehydrationDataValuesAll = ViewWorkflowAction.getDehydrationData(dataFileFullPath);
			}
			
			// mesh data
			List<List<MeshData>> listOfMeshData= new ArrayList<List<MeshData>>();
			WorkflowMesh3VO workflowMesh = null;
			List<WorkflowMesh3VO> listWFM = ViewWorkflowAction.getWorkflowMesh(workflow);
			if(listWFM != null && listWFM.size() > 0){
				workflowMesh = listWFM.get(0);
			}
			
			workflow.setWorkflowMesh(workflowMesh);
			if (displayMesh == 1){
				for(Iterator<List<DataCollection3VO>> it = listOfCollect.iterator(); it.hasNext();){
					List<MeshData> meshData= new ArrayList<MeshData>();
					List<DataCollection3VO> dataCollectionList= it.next();
					meshData = ViewWorkflowAction.getMeshData(dataCollectionList, workflowMesh);
					
					listOfMeshData.add(meshData);
				}
			}




			HashMap<String, Object> data = new HashMap<String, Object>();
			//context path
			data.put("contextPath", request.getContextPath());
			// listOfCrystal
			data.put("listOfCrystalClass", listOfCrystalClass);
			// workflow
			data.put("workflowVO", workflow);
			// dataCollectionList
			data.put("listOfCollect", collectionList);
			// beamlines names (search)
			String[] beamlineList = Constants.BEAMLINE_LOCATION;
			if(Constants.SITE_IS_ESRF()){
				beamlineList = ESRFBeamlineEnum.getBeamlineNames();
			}
			if(Constants.SITE_IS_EMBL()){
				beamlineList = EMBLBeamlineEnum.getBeamlineNames();
			}
			if(Constants.SITE_IS_MAXIV()){
				beamlineList = MAXIVBeamlineEnum.getBeamlineNames();
			}
			if (Constants.SITE_IS_SOLEIL()) {
				beamlineList = SOLEILBeamlineEnum.getBeamlineNames();
			}
			data.put("beamlines", beamlineList);
			// displayImage
			data.put("displayImage", displayImage);
			// displayWorflow
			data.put("displayWorkflow", displayWorkflow);
			// displayMesh
			data.put("displayMesh", displayMesh);
			// displayDehydration
			data.put("displayDehydration", displayDehydration);
			// snapshot
			data.put("listOfSnapshot", listOfSnapshot);
			// images list
			data.put("listOfImages", listOfImages);
			// dehydration
			data.put("dehydrationValues", dehydrationDataValuesAll);
			//isManager
			data.put("isManager", isManager);
			// lastCollect
			data.put("displayLastCollect", false);
			// mesh data
			data.put("listOfMeshData", listOfMeshData);
			// list of references
			data.put("listOfReferences", listOfReferences);
			// referenceText
			data.put("referenceText", referenceText);
			// isIndustrial
			data.put("isIndustrial", isIndus);
			//data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
		}catch(Exception e){
			e.printStackTrace();
			errors.add(e.toString());
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("errors", errors);
			//data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
			return;
		}
	}

	public static List<WorkflowMesh3VO> getWorkflowMesh(Workflow workflow) throws Exception{
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		WorkflowMesh3Service workflowMeshService = (WorkflowMesh3Service) ejb3ServiceLocator.getLocalService(WorkflowMesh3Service.class);

		
		List<WorkflowMesh3VO> listw  = new ArrayList<WorkflowMesh3VO>();
		if (workflow != null)
			listw = workflowMeshService.findByWorkflowId(workflow.getWorkflowId()) ;
		return listw;
	}

	public static List<MeshData> getMeshData(List<DataCollection3VO> dataCollectionList, WorkflowMesh3VO workflowMesh) throws Exception{
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		GridInfo3Service gridInfoService =  (GridInfo3Service) ejb3ServiceLocator.getLocalService(GridInfo3Service.class);
		ImageQualityIndicators3Service imageQualityIndicatorsService = (ImageQualityIndicators3Service) ejb3ServiceLocator.getLocalService(ImageQualityIndicators3Service.class);
		Image3Service imageService = (Image3Service) ejb3ServiceLocator.getLocalService(Image3Service.class);
		
		List<MeshData> meshData= new ArrayList<MeshData>();
		
		for (Iterator<DataCollection3VO> dcList= dataCollectionList.iterator();dcList.hasNext();){
			DataCollection3VO dc = dcList.next();
			Integer dataCollectionId = dc.getDataCollectionId();

			GridInfo3VO gridInfo = null;
			if (dc.getDataCollectionGroupVO().getWorkflowVO() != null){

				if (workflowMesh != null){
					List<GridInfo3VO> list  = gridInfoService.findByWorkflowMeshId(workflowMesh.getWorkflowMeshId()) ;
					if (list != null && list.size() > 0){
						gridInfo  = list.get(0);
					}
				}
			}
			// load  Images & ImageQualityIndicator
			List<Image3VO> listImage = imageService.findByDataCollectionId(dataCollectionId);
			if (listImage != null){
				for (Iterator<Image3VO> iterator = listImage.iterator(); iterator.hasNext();) {
					Image3VO image3vo = (Image3VO) iterator.next();
					String imageFileLocation = PathUtils.FitPathToOS(image3vo.getJpegFileFullPath());
					boolean imageFilePresent = (PathUtils.fileExists(image3vo.getJpegFileFullPath()) ==1);
					List<ImageQualityIndicators3VO> listImageQI = imageQualityIndicatorsService.findByImageId(image3vo.getImageId());
						
					Integer imageQualityIndicatorId = null;
					Integer spotTotal = null;
					Float methodRes1 = null;
					Float methodRes2 = null;
					Integer goodBraggCandidates = null;
					Double totalIntegratedSignal = null;
					Double dozor_score = null;
					ImageQualityIndicators3VO imgQualityIndic = null;
					if (listImageQI != null && listImageQI.size() > 0){
						// should be only one
						imgQualityIndic = listImageQI.get(0);
						imageQualityIndicatorId = imgQualityIndic.getImageQualityIndicatorsId();
						spotTotal =  imgQualityIndic.getSpotTotal();
						methodRes1 =  imgQualityIndic.getMethod1Res();
						methodRes2 =  imgQualityIndic.getMethod2Res();
						goodBraggCandidates =  imgQualityIndic.getGoodBraggCandidates();
						totalIntegratedSignal =  imgQualityIndic.getTotalIntegratedSignal();
						dozor_score = imgQualityIndic.getDozor_score();
					}
						
					
						MeshData md= new MeshData(dataCollectionId,  image3vo.getImageId(),
								imageQualityIndicatorId,  imageFileLocation,
								imageFilePresent,  dc.getImagePrefix(), dc.getDataCollectionNumber(), 
								gridInfo, 
								spotTotal,  goodBraggCandidates,  
								methodRes1,
								methodRes2,  totalIntegratedSignal, dozor_score);
						meshData.add(md);
					
						
				}
				
			}
		}
		return meshData;

	}
	
	public  static List<DehydrationData>  getDehydrationData(String dataFileFullPath){
		List<DehydrationData> dehydrationDataValuesAll = new ArrayList<DehydrationData>();
		// read dehydration file
		BufferedReader inFile = null;
		try {
			// 1. Reading input by lines:
			inFile = new BufferedReader(new FileReader(dataFileFullPath));
			String s = new String();
			while ((s = inFile.readLine()) != null) {
				String line = s;
				String[] values = line.split(" ");
				int k = 0;
				try {
					DehydrationData d = new DehydrationData(Double.parseDouble(values[k++]),
								Double.parseDouble(values[k++]), Double.parseDouble(values[k++]),
								Double.parseDouble(values[k++]), Double.parseDouble(values[k++]),
								Double.parseDouble(values[k++]), Double.parseDouble(values[k++]),
								Double.parseDouble(values[k++]), Double.parseDouble(values[k++]),
								Double.parseDouble(values[k++]), Double.parseDouble(values[k++]),
								Double.parseDouble(values[k++]));

					dehydrationDataValuesAll.add(d);
				} catch (Exception e) {

				}
			}
			inFile.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inFile != null) {
				try {
					inFile.close();
				} catch (IOException ioex) {
					// ignore
				}
			}

		}
		return dehydrationDataValuesAll;
	}

	
	public static int[] getDisplayWorkflowStatus(Workflow3VO workflow){
		int displayImage = 0;
		int displayWorkflow = 0;
		int displayMesh = 0;
		int displayDehydration = 0;
		String experimentType = null;
		if (workflow != null){
			experimentType =workflow.getWorkflowType();
		}
		if (experimentType != null){
			if (experimentType.startsWith("Mesh") || experimentType.startsWith("XrayCentering") || experimentType.startsWith("MXPressO")){
				displayImage = 1;
				displayMesh =1 ;
			}else if (experimentType.equals("Dehydration")){
				displayImage = 1;
				displayDehydration =1 ;
			}
		}
		if (workflow != null){
			displayWorkflow = 1;
		}
		
		int[] displayWorkflowStatus = new int[4];
		displayWorkflowStatus[0] = displayImage;
		displayWorkflowStatus[1] = displayWorkflow;
		displayWorkflowStatus[2] = displayMesh;
		displayWorkflowStatus[3] = displayDehydration;
		
		return displayWorkflowStatus;
	}
	
	public static Workflow getWorkflowInfo(Workflow3VO workflowVO){
		// workflow info
		//String resultFileName = "";
		int resultFileExists = 0;
		String resultFileFullPath = "";
		String logFileName = "";
		int logFileExists = 0;
		String logFileFullPath = "";
		String fullLogFileContent = "";
		Workflow workflow =null;
		if (workflowVO != null){
			//resultFileName = StringUtils.getFileName(workflowVO.getResultFilePath());
			resultFileExists = PathUtils.directoryExists(workflowVO.getResultFilePath());
			resultFileFullPath = PathUtils.FitPathToOS(workflowVO.getResultFilePath());
			logFileName = StringUtils.getFileName(workflowVO.getLogFilePath());
			logFileExists = PathUtils.fileExists(workflowVO.getLogFilePath());
			logFileFullPath = PathUtils.FitPathToOS(workflowVO.getLogFilePath());
			fullLogFileContent = "";
			if (logFileExists == 1)
				fullLogFileContent = FileUtil.fileToString(logFileFullPath);
			String directoryFileName = StringUtils.getDirectoryFilename(logFileFullPath);
			FileInformation logFile = new FileInformation(logFileName, logFileExists == 1,  logFileFullPath, fullLogFileContent, directoryFileName); 
			workflow = new Workflow(workflowVO, logFile);
		}
		if (resultFileExists == 1){
			// for each directory in resultFileFullPath, display index.html
			List<FileInformation> listFileInformation = new ArrayList<FileInformation>();
			File resultDirectory = new File(resultFileFullPath);
			List<File> resultFilesList =IspybFileUtils.getListFile(resultDirectory,Constants.INDEX_FILE_NAME_WORKFLOW_RESULT);
			for (Iterator<File> iterator = resultFilesList.iterator(); iterator.hasNext();) {
				File file = (File) iterator.next();
				String path = file.getPath();
				String fileName = StringUtils.getFileName(path);
				String fileFullPath = PathUtils.FitPathToOS(path);
				boolean existFile = (PathUtils.fileExists(path)) == 1;
				String parent = file.getParent();
				if (parent != null){
					String dirFullPath = PathUtils.FitPathToOS(parent);
					dirFullPath = dirFullPath.replace("\\", "/");
					String fullFileContent = "";
					if (existFile){
						fullFileContent = FileUtil.fileToString(fileFullPath);
						String pathImageUrl = "imageDownload.do?reqCode=getEDNAImage";
						String hrefFileUrl = "viewResults.do?reqCode=displayHtmlFile";
						fullFileContent = UrlUtils.formatWorkflowResultpageURL(fullFileContent, pathImageUrl, hrefFileUrl, dirFullPath);
					}
					String directoryFileName = StringUtils.getDirectoryFilename(fileFullPath);
					FileInformation fileInformation = new FileInformation(fileName, existFile,
							fileFullPath, fullFileContent, directoryFileName);
					listFileInformation.add(fileInformation);
				}
			}
			workflow.setResultFiles(listFileInformation);
		}
		return workflow;
	}
}
