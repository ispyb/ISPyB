package ispyb.ws.rest.mx;

import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.utils.reader.AutoProcProgramaAttachmentFileReader;
import ispyb.server.mx.services.ws.rest.AutoProcessingIntegration.AutoProcessingIntegrationService;
import ispyb.server.mx.vos.autoproc.AutoProcIntegration3VO;
import ispyb.server.mx.vos.autoproc.AutoProcProgram3VO;
import ispyb.server.mx.vos.autoproc.AutoProcProgramAttachment3VO;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/")
public class AutoprocintegrationRestWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(AutoprocintegrationRestWebService.class);

//	@RolesAllowed({ "User", "Manager", "LocalContact" })
//	@GET
//	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/{autoProcIntegrationId}/analysis/list")
//	@Produces({ "application/json" })
//	public Response getAnalysisByAutoProcIntegrationId(@PathParam("token") String token,
//			@PathParam("proposal") String proposal, @PathParam("autoProcIntegrationId") int autoProcIntegrationId) {
//
//		String methodName = "getAnalysisByAutoProcIntegrationId";
//		long start = this.logInit(methodName, logger, token, proposal);
//		try {
//			External3Service external3Service = this.getExternal3Service();
//			List<Map<String, Object>> result = external3Service.getPhasingAnalysisByDataCollectionIdListQuery(autoProcIntegrationId);
//			this.logFinish(methodName, start, logger);
//			return this.sendResponse(result);
//		} catch (Exception e) {
//			return this.logError(methodName, e, start, logger);
//		}
//	}

//	@RolesAllowed({ "User", "Manager", "LocalContact" })
//	@GET
//	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/{autoProcIntegrationListId}/xscale/list")
//	@Produces({ "application/json" })
//	public Response getXScaleByAutoProcProgramId(@PathParam("token") String token,
//			@PathParam("proposal") String proposal,
//			@PathParam("autoProcIntegrationListId") String autoProcIntegrationListId) {
//
//		String methodName = "getAnalysisByAutoProcIntegrationId";
//		long start = this.logInit(methodName, logger, token, proposal);
//		try {
//			List<Integer> ids = this.parseToInteger(autoProcIntegrationListId);
//			HashMap<Integer, List<AutoProcProgramAttachment3VO>> result = new HashMap<Integer, List<AutoProcProgramAttachment3VO>>();
//			for (Integer id : ids) {
//				AutoProcIntegration3VO autoProcIntegration3VO = this.getAutoProcIntegration3Service().findByPk(id);
//				List<AutoProcProgramAttachment3VO> xscaleAttachmentList = this.getAutoProcProgramAttachment3Service()
//						.findXScale(autoProcIntegration3VO.getAutoProcProgramVOId());
//				result.put(id, xscaleAttachmentList);
//			}
//			this.logFinish(methodName, start, logger);
//			return this.sendResponse(result);
//		} catch (Exception e) {
//			return this.logError(methodName, e, start, logger);
//		}
//	}

	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/{autoProcIntegrationListId}/xscale/plot")
	@Produces({ "application/json" })
	public Response getXScalePlotByAutoProcProgramId(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("autoProcIntegrationListId") String autoProcIntegrationListId) {

		String methodName = "getXScalePlotByAutoProcProgramId";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<Integer> ids = this.parseToInteger(autoProcIntegrationListId);
			HashMap<Integer, List<HashMap<String, Object>>> result = new HashMap<Integer, List<HashMap<String, Object>>>();
			for (Integer id : ids) {
				AutoProcIntegration3VO autoProcIntegration3VO = this.getAutoProcIntegration3Service().findByPk(id);
				List<AutoProcProgramAttachment3VO> xscaleAttachmentList = this.getAutoProcProgramAttachment3Service()
						.findXScale(autoProcIntegration3VO.getAutoProcProgramVOId());

				List<HashMap<String, Object>> attachmentData = new ArrayList<HashMap<String, Object>>();
				for (AutoProcProgramAttachment3VO autoProcProgramAttachment3VO : xscaleAttachmentList) {
					HashMap<String, Object> data = AutoProcProgramaAttachmentFileReader
							.readAttachment(autoProcProgramAttachment3VO);
					attachmentData.add(data);
				}
				result.put(id, attachmentData);
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	protected AutoProcessingIntegrationService getAutoprocessingServiceBean() throws NamingException {
		return (AutoProcessingIntegrationService) Ejb3ServiceLocator.getInstance().getLocalService(AutoProcessingIntegrationService.class);
	}
	
	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/datacollection/{dataCollectionIdList}/view")
	@Produces({ "application/json" })
	public Response getByDatacollectionId(@PathParam("token") String token,
			@PathParam("proposal") String proposal, @PathParam("dataCollectionIdList") String dataCollectionIdList) {

		String methodName = "getByDatacollectionId";
		long start = this.logInit(methodName, logger, token, proposal, dataCollectionIdList);
		try {
			List<Integer> dataCollectionIds = this.parseToInteger(dataCollectionIdList);
			List<List<Map<String, Object>>> result = new ArrayList<List<Map<String,Object>>>();
			for (Integer dataCollectionId : dataCollectionIds) {
				result.add(this.getAutoprocessingServiceBean().getViewByDataCollectionId(this.getProposalId(proposal), dataCollectionId));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	/**
	 * AutoProcProgramAttachment has not AutoProcProgramId mapped in the EJB object
	 * so it is necessary to keep separately the possible list of ids in order
	 * to identify in the client the list of files linked to a sinble autoProcProgram
	 * 
	 * So, if autoprocattachmentids contains n different ids then the response will be an n-array with the list of files for each id
	 **/
	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/attachment/autoprocprogramid/{autoprocattachmentids}/list")
	@Produces({ "application/json" })
	public Response getAttachments(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("autoprocattachmentids") String autoprocattachmentids) {

		String methodName = "getAttachments";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<Integer> ids = this.parseToInteger(autoprocattachmentids);
			List<List<AutoProcProgramAttachment3VO>> list = new ArrayList<List<AutoProcProgramAttachment3VO>>();
			for (Integer id : ids) {
				AutoProcProgram3VO autoProcProgram3VO = this.getAutoProcProgram3Service().findByPk(id, true);
				list.add(autoProcProgram3VO.getAttachmentListVOs());
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(list);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
//	@RolesAllowed({ "User", "Manager", "LocalContact" })
//	@GET
//	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/attachment/autoProcIntegrationListId/{autoProcIntegrationListId}/list")
//	@Produces("text/plain")
//	public Response getAttachments(@PathParam("token") String token, @PathParam("proposal") String proposal,
//			@PathParam("autoProcIntegrationListId") String autoProcIntegrationListId) {
//
//		String methodName = "getAttachments";
//		long start = this.logInit(methodName, logger, token, proposal);
//		try {
//			List<Integer> ids = this.parseToInteger(autoProcIntegrationListId);
//			List<AutoProcProgramAttachment3VO> list = new ArrayList<AutoProcProgramAttachment3VO>();
//			for (Integer id : ids) {
//				AutoProcIntegration3VO autoProcIntegration3VO = this.getAutoProcIntegration3Service().findByPk(id);
//				System.out.println("AutoprocIntegration Id: " + autoProcIntegration3VO.getAutoProcIntegrationId());
//				System.out.println("AutoprocProgram Id: " + autoProcIntegration3VO.getAutoProcProgramVOId());
//
//				List<AutoProcProgramAttachment3VO> xscaleAttachmentList = this.getAutoProcProgramAttachment3Service()
//						.findXScale(autoProcIntegration3VO.getAutoProcProgramVOId());
//				for (AutoProcProgramAttachment3VO autoProcProgramAttachment3VO : xscaleAttachmentList) {
//					list.add(autoProcProgramAttachment3VO);
//				}
//			}
//			this.logFinish(methodName, start, logger);
//			return this.sendResponse(list);
//		} catch (Exception e) {
//			return this.logError(methodName, e, start, logger);
//		}
//	}

//	private static IspybAutoProcAttachment3VO getAutoProcAttachment(String fileName,
//			List<IspybAutoProcAttachment3VO> listOfAutoProcAttachment) {
//		if (listOfAutoProcAttachment == null)
//			return null;
//		for (Iterator<IspybAutoProcAttachment3VO> i = listOfAutoProcAttachment.iterator(); i.hasNext();) {
//			IspybAutoProcAttachment3VO att = i.next();
//			if (att.getFileName().equalsIgnoreCase(fileName)) {
//				return att;
//			}
//		}
//
//		// IK testing autoproc attachments with any name containing names in DB
//		if (Constants.SITE_IS_EMBL()) {
//			for (Iterator<IspybAutoProcAttachment3VO> i = listOfAutoProcAttachment.iterator(); i.hasNext();) {
//				IspybAutoProcAttachment3VO att = i.next();
//				if (fileName.contains(att.getFileName())) {
//					return att;
//				}
//			}
//		}
//
//		if (fileName.contains("_run")) {
//			// edna file: remove the prefix (image + run) to add edna
//			int id1 = fileName.indexOf("_run");
//			String s = fileName.substring(id1 + 1);
//			int id2 = s.indexOf("_");
//			if (id2 >= 0 && id2 < s.length() - 1) {
//				s = s.substring(id2 + 1);
//				s = "edna_" + s;
//				for (Iterator<IspybAutoProcAttachment3VO> i = listOfAutoProcAttachment.iterator(); i.hasNext();) {
//					IspybAutoProcAttachment3VO att = i.next();
//					if (att.getFileName().equalsIgnoreCase(s)) {
//						return att;
//					}
//				}
//			}
//		}
//		System.out.println("No AutoProc Attachment found for: " + fileName);
//		return null;
//	}

//	private AutoProcessingDetail getAutoProcessingDetailBy(String autoProcIdS, Integer dataCollectionId)
//			throws NamingException, Exception {
//		AutoProcessingDetail autoProcDetail = new AutoProcessingDetail();
//		AutoProc3Service apService = this.getAutoProc3Service();
//		AutoProcScalingStatistics3Service apssService = this.getAutoProcScalingStatistics3Service();
//
//		DecimalFormat df1 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		df1.applyPattern("#####0.0");
//		Integer autoProcId = Integer.parseInt(autoProcIdS);
//
//		AutoProc3VO apv = apService.findByPk(autoProcId);
//		autoProcDetail.setAutoProcId(autoProcId);
//
//		AutoProcScalingStatistics3VO apssv_overall = apssService.getBestAutoProcScalingStatistic(apssService
//				.findByAutoProcId(autoProcId, "overall"));
//		AutoProcScalingStatistics3VO apssv_outer = apssService.getBestAutoProcScalingStatistic(apssService
//				.findByAutoProcId(autoProcId, "outerShell"));
//
//		if (apssv_overall != null) {
//			autoProcDetail.setOverallCompleteness("" + apssv_overall.getCompleteness() + "%");
//			autoProcDetail.setOverallResolution("" + apssv_overall.getResolutionLimitLow() + "-"
//					+ apssv_overall.getResolutionLimitHigh() + " &#8491;");
//			autoProcDetail.setOverallIOverSigma("" + apssv_overall.getMeanIoverSigI());
//			if (apssv_overall.getRmerge() == null)
//				autoProcDetail.setOverallRsymm("");
//			else {
//				autoProcDetail.setOverallRsymm("" + apssv_overall.getRmerge() + "%");
//			}
//			autoProcDetail.setOverallMultiplicity(""
//					+ (apssv_overall.getMultiplicity() == null ? "" : new Double(df1.format(apssv_overall
//							.getMultiplicity()))));
//		}
//
//		if (apssv_outer != null) {
//			autoProcDetail.setOuterCompleteness("" + apssv_outer.getCompleteness() + "%");
//			autoProcDetail.setOuterResolution("" + apssv_outer.getResolutionLimitLow() + "-"
//					+ apssv_overall.getResolutionLimitHigh() + " &#8491;");
//			autoProcDetail.setOuterIOverSigma("" + apssv_outer.getMeanIoverSigI());
//			autoProcDetail.setOuterRsymm("" + apssv_outer.getRmerge() + "%");
//			autoProcDetail.setOuterMultiplicity(""
//					+ (apssv_outer.getMultiplicity() == null ? "" : (new Double(df1.format(apssv_outer
//							.getMultiplicity())))));
//		}
//
//		double refinedCellA = ((double) ((int) (apv.getRefinedCellA() * 10))) / 10;
//		double refinedCellB = ((double) ((int) (apv.getRefinedCellB() * 10))) / 10;
//		double refinedCellC = ((double) ((int) (apv.getRefinedCellC() * 10))) / 10;
//
//		autoProcDetail.setUnitCellA("" + refinedCellA + " &#8491;"); // angstrom
//																		// symbol
//		autoProcDetail.setUnitCellB("" + refinedCellB + " &#8491;");
//		autoProcDetail.setUnitCellC("" + refinedCellC + " &#8491;");
//
//		autoProcDetail.setUnitCellAlpha("" + apv.getRefinedCellAlpha() + " &#176;"); // degree
//																						// symbol
//		autoProcDetail.setUnitCellBeta("" + apv.getRefinedCellBeta() + " &#176;");
//		autoProcDetail.setUnitCellGamma("" + apv.getRefinedCellGamma() + " &#176;");
//
//		//
//		List<AutoProcStatus3VO> autoProcEvents = new ArrayList<AutoProcStatus3VO>();
//		if (autoProcId != null) {
//			List<AutoProcIntegration3VO> autoProcIntegrations = this.getAutoProcIntegration3Service().findByAutoProcId(
//					autoProcId);
//			if (!autoProcIntegrations.isEmpty()) {
//				autoProcEvents = (autoProcIntegrations.iterator().next()).getAutoProcStatusList();
//			}
//		}
//		autoProcDetail.setAutoProcEvents(autoProcEvents);
//
//		// attachments
//		// List<IspybAutoProcAttachment3VO> listOfAutoProcAttachment =
//		// (List<IspybAutoProcAttachment3VO>)
//		// request.getSession().getAttribute(Constants.ISPYB_AUTOPROC_ATTACH_LIST);
//		List<IspybAutoProcAttachment3VO> listOfAutoProcAttachment = new ArrayList<IspybAutoProcAttachment3VO>();
//		Integer autoProcProgramId = null;
//		if (apv != null)
//			autoProcProgramId = apv.getAutoProcProgramVOId();
//
//		if (autoProcId != null) {
//			List<AutoProcIntegration3VO> autoProcIntegrations = this.getAutoProcIntegration3Service().findByAutoProcId(
//					autoProcId);
//
//			if (!autoProcIntegrations.isEmpty()) {
//				autoProcProgramId = (autoProcIntegrations.iterator().next()).getAutoProcProgramVOId();
//			}
//		}
//
//		List<AutoProcProgramAttachment3VO> attachments = null;
//		List<AutoProcAttachmentWebBean> autoProcProgAttachmentsWebBeans = new ArrayList<AutoProcAttachmentWebBean>();
//		// LOG.debug("autoProcProgramId = " + autoProcProgramId);
//
//		if (autoProcProgramId != null) {
//			attachments = new ArrayList<AutoProcProgramAttachment3VO>(this.getAutoProcProgram3Service()
//					.findByPk(autoProcProgramId, true).getAttachmentVOs());
//
//			if (!attachments.isEmpty()) {
//				// attachmentWebBeans = new
//				// AutoProcAttachmentWebBean[attachments.size()];
//
//				System.out.println("nb attachments = " + attachments.size());
//				for (Iterator<AutoProcProgramAttachment3VO> iterator = attachments.iterator(); iterator.hasNext();) {
//					AutoProcProgramAttachment3VO att = iterator.next();
//					AutoProcAttachmentWebBean attBean = new AutoProcAttachmentWebBean(att);
//					// gets the ispyb auto proc attachment file
//					IspybAutoProcAttachment3VO aAutoProcAttachment = getAutoProcAttachment(attBean.getFileName(),
//							listOfAutoProcAttachment);
//					if (aAutoProcAttachment == null) {
//						// by default in XDS tab and output files
//						aAutoProcAttachment = new IspybAutoProcAttachment3VO(null, attBean.getFileName(), "", "XDS",
//								"output", false);
//					}
//					attBean.setIspybAutoProcAttachment(aAutoProcAttachment);
//					autoProcProgAttachmentsWebBeans.add(attBean);
//				}
//
//			} else
//				System.out.println("attachments is empty");
//
//		}
//
//		// Issue 1507: Correction files for ID29 & ID23-1
//		if (Constants.SITE_IS_ESRF()) {
//			// Integer dataCollectionId = null;
//			// if
//			// (BreadCrumbsForm.getIt(request).getSelectedDataCollection()
//			// != null)
//			// dataCollectionId =
//			// BreadCrumbsForm.getIt(request).getSelectedDataCollection().getDataCollectionId();
//			if (dataCollectionId != null) {
//				DataCollection3VO dataCollection = this.getDataCollection3Service().findByPk(dataCollectionId, false,
//						false, false);
//				String beamLineName = dataCollection.getDataCollectionGroupVO().getSessionVO().getBeamlineName();
//				String[] correctionFiles = ESRFBeamlineEnum.retrieveCorrectionFilesNameWithName(beamLineName);
//				if (correctionFiles != null) {
//					for (int k = 0; k < correctionFiles.length; k++) {
//						String correctionFileName = correctionFiles[k];
//						String dir = ESRFBeamlineEnum.retrieveDirectoryNameWithName(beamLineName);
//						if (dir != null) {
//							String correctionFilePath = "/data/pyarch/" + dir + "/" + correctionFileName;
//							String fullFilePath = PathUtils.FitPathToOS(correctionFilePath);
//							File f = new File(fullFilePath);
//							if (f != null && f.exists()) {
//								// fake attachment
//								AutoProcProgramAttachment3VO att = new AutoProcProgramAttachment3VO(-1, null,
//										"Correction File", correctionFileName, correctionFilePath, null);
//								AutoProcAttachmentWebBean attBean = new AutoProcAttachmentWebBean(att);
//								IspybAutoProcAttachment3VO aAutoProcAttachment = new IspybAutoProcAttachment3VO(null,
//										correctionFileName, "correction file", "XDS", "correction", false);
//								attBean.setIspybAutoProcAttachment(aAutoProcAttachment);
//								autoProcProgAttachmentsWebBeans.add(attBean);
//								attachments.add(attBean);
//							}
//						}
//					}// end for
//				}
//			}
//		}
//		autoProcDetail.setAutoProcProgAttachmentsWebBeans(autoProcProgAttachmentsWebBeans);
//		return autoProcDetail;
//	}

	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/{autoProcIntegrationListId}/xscale/completeness")
	@Produces("text/plain")
	public Response getXScaleCompleteness(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("autoProcIntegrationListId") String autoProcIntegrationListId) {

		String methodName = "getXScaleCompleteness";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			String result = this.getAutoprocessingParserByAutoProcIntegrationListId(this.parseToInteger(autoProcIntegrationListId)).parseCompleteness();
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/{autoProcIntegrationListId}/xscale/cc2")
	@Produces("text/plain")
	public Response getXScaleCC2(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("autoProcIntegrationListId") String autoProcIntegrationListId) {

		String methodName = "getXScaleCC2";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			String result = this.getAutoprocessingParserByAutoProcIntegrationListId(
					this.parseToInteger(autoProcIntegrationListId)).parsecc2();
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/{autoProcIntegrationListId}/xscale/isigma")
	@Produces("text/plain")
	public Response getXScaleISigma(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("autoProcIntegrationListId") String autoProcIntegrationListId) {

		String methodName = "getXScaleISigma";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			String result = this.getAutoprocessingParserByAutoProcIntegrationListId(
					this.parseToInteger(autoProcIntegrationListId)).parseISigma();
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/{autoProcIntegrationListId}/xscale/rfactor")
	@Produces("text/plain")
	public Response getXScaleRfactor(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("autoProcIntegrationListId") String autoProcIntegrationListId) {

		String methodName = "getXScaleRfactor";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			String result = this.getAutoprocessingParserByAutoProcIntegrationListId(
					this.parseToInteger(autoProcIntegrationListId)).parseRfactor();
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/{autoProcIntegrationListId}/xscale/sigmaano")
	@Produces("text/plain")
	public Response getXScaleSigmaAno(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("autoProcIntegrationListId") String autoProcIntegrationListId) {

		String methodName = "getXScaleSigmaAno";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			String result = this.getAutoprocessingParserByAutoProcIntegrationListId(
					this.parseToInteger(autoProcIntegrationListId)).parseSigmaAno();
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/{autoProcIntegrationListId}/xscale/anomcorr")
	@Produces("text/plain")
	public Response getXScaleAnomCorr(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("autoProcIntegrationListId") String autoProcIntegrationListId) {

		String methodName = "getXScaleAnomCorr";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			String result = this.getAutoprocessingParserByAutoProcIntegrationListId(
					this.parseToInteger(autoProcIntegrationListId)).parseAnomCorrection();
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/{autoProcIntegrationListId}/xscale/wilson")
	@Produces("text/plain")
	public Response getXScaleWilson(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("autoProcIntegrationListId") String autoProcIntegrationListId) {

		String methodName = "getXScaleWilson";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			String result = this.getAutoprocessingParserByAutoProcIntegrationListId(
					this.parseToInteger(autoProcIntegrationListId)).parseWilson();
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	
//	@RolesAllowed({ "User", "Manager", "LocalContact" })
//	@GET
//	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/datacollection/{dataCollectionIdList}/list")
//	@Produces({ "application/json" })
//	public Response getPhasingAnalysisById(@PathParam("token") String token, @PathParam("proposal") String proposal,
//			@PathParam("dataCollectionIdList") String dataCollectionIds) {
//
//		String methodName = "getPhasingAnalysisById";
//		long start = this.logInit(methodName, logger, token, proposal);
//		try {
//			ArrayList<Integer> ids = (ArrayList<Integer>) this.parseToInteger(dataCollectionIds);
//			List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
//			for (Integer dataCollectionId : ids) {
//				List<AutoProc3VO> autoprocs = this.getAutoprocByDataCollectionId(dataCollectionId);
//				for (AutoProc3VO autoProc3VO : autoprocs) {
//					List<AutoProcIntegration3VO> autoIntegrations = this.getAutoProcIntegration3Service().findByAutoProcId(autoProc3VO.getAutoProcId());
//					HashMap<String, Object> recordautoIntegrationsProgram = new HashMap<String,Object>();
//					for (AutoProcIntegration3VO autoProcIntegration3VO : autoIntegrations) {
////						recordautoIntegrationsProgram.put("datacollection", this.getDataCollection3Service().findByPk(autoProcIntegration3VO.getDataCollectionVOId(), false, false, false));
//						recordautoIntegrationsProgram.put("autoproc", autoProc3VO);
//						recordautoIntegrationsProgram.put("details", this.getAutoProcessingDetailBy(autoProc3VO.getAutoProcId().toString(), dataCollectionId));
//						recordautoIntegrationsProgram.put("autointegration", autoProcIntegration3VO);
//						recordautoIntegrationsProgram.put("autoprocprogram", this.getAutoProcProgram3Service().findByPk(autoProcIntegration3VO.getAutoProcProgramVOId(), true));
//					}
//					result.add(recordautoIntegrationsProgram);
//				}
//			}
//			this.logFinish(methodName, start, logger);
//			return this.sendResponse(result);
//		} catch (Exception e) {
//			return this.logError(methodName, e, start, logger);
//		}
//	}
	

//	@RolesAllowed({ "User", "Manager", "LocalContact" })
//	@GET
//	@Path("{token}/proposal/{proposal}/mx/autoproc/{autoProcId}/list")
//	@Produces("text/plain")
//	public Response getDetails(@PathParam("token") String token, @PathParam("proposal") String proposal,
//			@PathParam("autoProcId") String autoProcIdS, @QueryParam("dataCollectionId") Integer dataCollectionId) {
//
//		String methodName = "getDetails";
//		long start = this.logInit(methodName, logger, token, autoProcIdS, dataCollectionId);
//
//		try {
//			AutoProcessingDetail autoProcDetail = this.getAutoProcessingDetailBy(autoProcIdS, dataCollectionId);
//			return this.sendResponse(autoProcDetail);
//		} catch (Exception e) {
//			return this.logError(methodName, e, start, logger);
//		}
//	}

//	private List<AutoProc3VO> getAutoprocByDataCollectionId(int dataCollectionId) throws NamingException, Exception{
//		return this.getAutoProc3Service().findByDataCollectionId(dataCollectionId);
//	}
//	
//	@RolesAllowed({ "User", "Manager", "LocalContact" })
//	@GET
//	@Path("{token}/proposal/{proposal}/mx/autoproc/datacollection/{dataCollectionIdList}/list")
//	@Produces({ "application/json" })
//	public Response getAutprocByDataCollectionId(@PathParam("token") String token, @PathParam("proposal") String proposal,
//			@PathParam("dataCollectionIdList") String dataCollectionIds) {
//
//		String methodName = "getAutprocByDataCollectionId";
//		long start = this.logInit(methodName, logger, token, proposal);
//		try {
//			
//			ArrayList<Integer> ids = (ArrayList<Integer>) this.parseToInteger(dataCollectionIds);
//			List<AutoProc3VO> autoprocs = new ArrayList<AutoProc3VO>();
//			for (Integer dataCollectionId : ids) {
//				List<AutoProc3VO> result = this.getAutoprocByDataCollectionId(dataCollectionId);	
//				autoprocs.addAll(result);
//			}
//			this.logFinish(methodName, start, logger);
//			return this.sendResponse(autoprocs);
//		} catch (Exception e) {
//			return this.logError(methodName, e, start, logger);
//		}
//	}
	
	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/autoprocattachmentid/{autoProcAttachmentId}/download")
	@Produces({ "application/json" })
	public Response downloadAutoProcAttachment(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("autoProcAttachmentId") int autoProcAttachmentId) {

		String methodName = "downloadAutoProcAttachment";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			AutoProcProgramAttachment3VO attachment = this.getAutoProcProgramAttachment3Service().findByPk(autoProcAttachmentId);
		
			this.logFinish(methodName, start, logger);
			File file = new File(attachment.getFilePath() + "/" + attachment.getFileName());
			if (file.exists()) {
				this.logFinish(methodName, start, logger);
				return this.downloadFile(file.getAbsolutePath());
			}
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
		return Response.noContent().build();
	}
	

}
