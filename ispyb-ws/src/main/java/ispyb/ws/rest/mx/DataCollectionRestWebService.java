package ispyb.ws.rest.mx;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import ispyb.common.util.export.ExiCsvExporter;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.collections.DataCollection3Service;
import org.apache.log4j.Logger;
import org.jboss.resteasy.annotations.GZIP;

import ispyb.common.util.Constants;
import ispyb.common.util.SendMailUtils;
import ispyb.common.util.export.ExiPdfRtfExporter;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.Session3VO;

@Path("/")
public class DataCollectionRestWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(DataCollectionRestWebService.class);

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/datacollection/{dataCollectionIdList}/list")
	@Produces({ "application/json" })
	public Response getDataCollectionById(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionIdList") String dataCollectionIdList) {

		String methodName = "getDataCollectionById";
		long start = this.logInit(methodName, logger, token, proposal, dataCollectionIdList);
		try {
			
			List<Integer> ids = this.parseToInteger(dataCollectionIdList);
			List<Map<String, Object>> dataCollections = new ArrayList<Map<String, Object>>();

			for (Integer id : ids) {
				int propId = this.getProposalId(proposal);
				dataCollections.addAll(this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionByDataCollectionId(
						propId, id));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(dataCollections, false);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/{dataCollectionId}/wilson")
	@Produces("image/png")
	public Response getWilsonPlot(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId) {

		String methodName = "getWilsonPlot";
		long start = this.logInit(methodName, logger, token, proposal, dataCollectionId);
		try {
			DataCollection3VO dataCollection = this.getDataCollection3Service().findByPk(dataCollectionId, false, false);
			this.logFinish(methodName, start, logger);
			if (dataCollection != null) {
				return this.sendImage(dataCollection.getBestWilsonPlotPath());
			}

		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
		return null;
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/{dataCollectionId}/qualityindicatorplot")
	@Produces("image/png")
	public Response getQualityIndicatorsPlot(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId) {
		try {
			DataCollection3VO dataCollection = this.getDataCollection3Service().findByPk(dataCollectionId, false, false);
			if (dataCollection != null) {
				return this.sendImage(dataCollection.getImageQualityIndicatorsPlotPath());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@POST
	@Path("{token}/proposal/{proposal}/mx/datacollection/{dataCollectionId}/comments/save")
	@Produces("image/png")
	public Response saveDataCollectionComments(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId,
			@FormParam("comments") String comments) {
		
		String methodName = "saveDataCollectionComments";
		long id = this.logInit(methodName, logger, token, proposal, dataCollectionId, comments);
		
		try {
			DataCollection3VO dataCollection = this.getDataCollection3Service().findByPk(dataCollectionId, false, false);
			dataCollection.setComments(comments);
			this.getDataCollection3Service().update(dataCollection);

		} catch (Exception e) {
			e.printStackTrace();
			return this.logError(methodName, e, id, logger);
		}
		return this.sendResponse(true);
	}
	
	
	

	@Path("{token}/proposal/{proposal}/mx/xrfscan/xrfscanId/{xrfscanId}/qualityindicatorcsv")
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Produces("text/plain")
	public Response getCSVFile(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId) {

		String methodName = "getQualityIndicatorsCSV";
		long id = this.logInit(methodName, logger, token, proposal, dataCollectionId);
		try {
			DataCollection3VO dataCollection = this.getDataCollection3Service().findByPk(dataCollectionId, false, false);
			if (dataCollection != null) {
				if (dataCollection.getImageQualityIndicatorsCSVPath() != null) {
					/** Converting to csv **/
					if (new File(dataCollection.getImageQualityIndicatorsCSVPath()).exists()) {
						this.logFinish(methodName, id, logger);
						return this.sendResponse(new String(
								Files.readAllBytes(Paths.get(dataCollection.getImageQualityIndicatorsCSVPath()))));
					}
				}

			}

		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
		return null;

	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/{dataCollectionId}/crystalsnaphot/{id}/get")
	@Produces("image/png")
	public Response getCrystalSnapshot(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId, @PathParam("id") int id) {

//		String methodName = "getCrystalSnapshot";
//		long start = this.logInit(methodName, logger, token, proposal, dataCollectionId, id);
		try {
			DataCollection3VO dataCollection = this.getDataCollection3Service().findByPk(dataCollectionId, false, false);
//			this.logFinish(methodName, start, logger);
			if (dataCollection != null) {
				if (id == 1) {
					return this.sendImage(dataCollection.getXtalSnapshotFullPath1());
				}
				if (id == 2) {
					return this.sendImage(dataCollection.getXtalSnapshotFullPath2());
				}
				if (id == 3) {
					return this.sendImage(dataCollection.getXtalSnapshotFullPath3());
				}
				if (id == 4) {
					return this.sendImage(dataCollection.getXtalSnapshotFullPath4());
				}
			}

		} catch (Exception e) {
//			return this.logError(methodName, e, start, logger);
			e.printStackTrace();
		}
		return null;
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/datacollection/session/{sessionIdList}/list")
	@Produces({ "application/json" })
	public Response getViewDataCollectionBySessionId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("sessionIdList") String sessionIdList) {

		String methodName = "getDataCollectionNativelyBySessionId";
		long start = this.logInit(methodName, logger, token, proposal, sessionIdList);
		try {
			List<Integer> ids = this.parseToInteger(sessionIdList);
			List<Map<String, Object>> dataCollections = new ArrayList<Map<String, Object>>();

			for (Integer id : ids) {
				dataCollections.addAll(this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionBySessionId(
						this.getProposalId(proposal), id));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(dataCollections, false);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	/** reports section **/
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/session/{sessionId}/report/pdf")
	@Produces({ "application/pdf" })
	public Response getDataCollectionsReportBySessionIdPDF(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("sessionId") String sessionId, @QueryParam("nbRows") String nbRows) throws NamingException {

		String methodName = "getDataCollectionReportyBySessionIdPdf";
		long start = this.logInit(methodName, logger, token, proposal, sessionId);
		try {
			byte[] byteToExport = this.getPdfRtf(sessionId, proposal, nbRows, false, false).toByteArray();
			this.logFinish(methodName, start, logger);
			Session3VO ses = this.getSession3Service().findByPk(Integer.valueOf(sessionId), false, false, false);
			if (ses != null)
				return this.downloadFile(byteToExport, "Report_" + proposal + "_"+ ses.getBeamlineName()+ "_" + ses.getStartDate() + ".pdf");
			else
				return this.downloadFile(byteToExport, "No_session.pdf");
						
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/filterParam/{filterParam}/report/pdf")
	@Produces({ "application/pdf" })
	public Response getDataCollectionsReportByfilterParamPDF(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("filterParam") String filterParam) throws NamingException {

		String methodName = "getDataCollectionReportyByfilterParamPdf";
		long start = this.logInit(methodName, logger, token, proposal, filterParam);
		try {
			byte[] byteToExport = this.getPdfRtf(filterParam, proposal, false, false);
			this.logFinish(methodName, start, logger);
			
			if (filterParam != null)
				return this.downloadFile(byteToExport, "Report_" + proposal + "_"+ filterParam + ".pdf");
			else 
				return this.downloadFile(byteToExport, "No_data.pdf");
						
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/filterParam/{filterParam}/report/csv")
	@Produces({ "application/csv" })
	public Response getDataCollectionsReportByfilterParamCsv(@PathParam("token") String token,
															 @PathParam("proposal") String proposal,
															 @PathParam("filterParam") String filterParam) throws NamingException {

		String methodName = "getDataCollectionReportyByfilterParamCsv";
		long start = this.logInit(methodName, logger, token, proposal, filterParam);
		try {
			byte[] byteToExport = this.getCsv(filterParam, proposal).toByteArray();
			this.logFinish(methodName, start, logger);

			if (filterParam != null)
				return this.downloadFile(byteToExport, "Report_" + proposal + "_"+ filterParam + ".csv");
			else
				return this.downloadFile(byteToExport, "No_data.csv");

		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/filterParam/{filterParam}/report/rtf")
	@Produces({ "application/rtf" })
	public Response getDataCollectionsReportByfilterParamRTF(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("filterParam") String filterParam) throws NamingException {

		String methodName = "getDataCollectionReportyByfilterParamPdf";
		long start = this.logInit(methodName, logger, token, proposal, filterParam);
		try {
			byte[] byteToExport = this.getPdfRtf(filterParam, proposal, true, false);
			this.logFinish(methodName, start, logger);
			
			if (filterParam != null)
				return this.downloadFile(byteToExport, "Report_" + proposal + "_"+ filterParam + ".rtf");
			else 
				return this.downloadFile(byteToExport, "No_data.rtf");
						
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/session/{sessionId}/report/rtf")
	@Produces({ "application/rtf" })
	public Response getDataCollectionsReportBySessionIdRTF(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("sessionId") String sessionId, @QueryParam("nbRows") String nbRows) throws NamingException {

		String methodName = "getDataCollectionReportyBySessionIdRtf";
		long start = this.logInit(methodName, logger, token, proposal, sessionId);
		try {
			byte[] byteToExport = this.getPdfRtf(sessionId, proposal, nbRows, true, false).toByteArray();
			this.logFinish(methodName, start, logger);
			Session3VO ses = this.getSession3Service().findByPk(Integer.valueOf(sessionId), false, false, false);
			if (ses != null)
				return this.downloadFile(byteToExport, "Report_" + proposal + "_"+ ses.getBeamlineName()+ "_" + ses.getStartDate() + ".rtf");
			else
				return this.downloadFile(byteToExport, "No_session.pdf");
						
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/session/{sessionId}/analysisreport/pdf")
	@Produces({ "application/pdf" })
	public Response getDataCollectionsAnalysisReportBySessionIdPDF(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("sessionId") String sessionId, @QueryParam("nbRows") String nbRows) throws NamingException {

		String methodName = "getDataCollectionAnalysisReportyBySessionIdPdf";
		long start = this.logInit(methodName, logger, token, proposal, sessionId);
		try {
			byte[] byteToExport = this.getPdfRtf(sessionId, proposal, nbRows, false, true).toByteArray();
			this.logFinish(methodName, start, logger);
			Session3VO ses = this.getSession3Service().findByPk(Integer.valueOf(sessionId), false, false, false);
			if (ses !=null)
				return this.downloadFile(byteToExport, "AnalysisReport_" + proposal + "_"+ ses.getBeamlineName()+ "_" + ses.getStartDate() + ".pdf");
			else
				return this.downloadFile(byteToExport, "No_session.pdf");
						
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/filterParam/{filterParam}/analysisreport/pdf")
	@Produces({ "application/pdf" })
	public Response getDataCollectionsAnalysisReportByFilterParamPDF(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("filterParam") String filterParam)  throws NamingException {

		String methodName = "getDataCollectionAnalysisReportyByFilterParamPdf";
		long start = this.logInit(methodName, logger, token, proposal, filterParam);
		try {
			byte[] byteToExport = this.getPdfRtf(filterParam, proposal, false, true);
			this.logFinish(methodName, start, logger);
			if (filterParam !=null)
				return this.downloadFile(byteToExport, "AnalysisReport_" + proposal + "_"+ filterParam + ".pdf");
			else
				return this.downloadFile(byteToExport, "No_data.pdf");
						
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/filterParam/{filterParam}/analysisreport/rtf")
	@Produces({ "application/rtf" })
	public Response getDataCollectionsAnalysisReportByFilterParamRTF(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("filterParam") String filterParam) throws NamingException {

		String methodName = "getDataCollectionReportyByFilterParamRtf";
		long start = this.logInit(methodName, logger, token, proposal, filterParam);
		try {
			byte[] byteToExport = this.getPdfRtf(filterParam, proposal, true, true);
			this.logFinish(methodName, start, logger);

			if (filterParam !=null)
				return this.downloadFile(byteToExport, "AnalysisReport_" + proposal + "_"+ filterParam + ".rtf");
			else
				return this.downloadFile(byteToExport, "No_data.rtf");		
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/session/{sessionId}/analysisreport/rtf")
	@Produces({ "application/rtf" })
	public Response getDataCollectionsAnalysisReportBySessionIdRTF(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("sessionId") String sessionId, @QueryParam("nbRows") String nbRows) throws NamingException {

		String methodName = "getDataCollectionReportyBySessionIdRtf";
		long start = this.logInit(methodName, logger, token, proposal, sessionId);
		try {
			byte[] byteToExport = this.getPdfRtf(sessionId, proposal, nbRows, true, true).toByteArray();
			this.logFinish(methodName, start, logger);
			Session3VO ses = this.getSession3Service().findByPk(Integer.valueOf(sessionId), false, false, false);
			if (ses !=null)
				return this.downloadFile(byteToExport, "AnalysisReport_" + proposal + "_"+ ses.getBeamlineName()+ "_" + ses.getStartDate() + ".rtf");
			else
				return this.downloadFile(byteToExport, "No_session.rtf");		
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	/** end of reports section **/
		
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/datacollection/workflowstep/{workflowstepId}/list")
	@Produces({ "application/json" })
	public Response getViewDataCollectionByWorkflowStepId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("workflowstepId") String workflowstepId) {

		String methodName = "getViewDataCollectionByWorkflowStepId";
		long start = this.logInit(methodName, logger, token, proposal, workflowstepId);
		try {
			List<Integer> ids = this.parseToInteger(workflowstepId);
			List<Map<String, Object>> dataCollections = new ArrayList<Map<String, Object>>();

			for (Integer id : ids) {
				dataCollections.addAll(this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionByWorkflowId(this.getProposalId(proposal), id));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(dataCollections, false);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/datacollection/datacollectiongroupid/{datacollectiongroupids}/list")
	@Produces({ "application/json" })
	public Response getViewDataCollectionByDataCollectionId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("datacollectiongroupids") String datacollectiongroupids) {

		String methodName = "getViewDataCollectionByDataCollectionId";
		long start = this.logInit(methodName, logger, token, proposal, datacollectiongroupids);
		try {
			List<Integer> ids = this.parseToInteger(datacollectiongroupids);
			List<Map<String, Object>> dataCollections = new ArrayList<Map<String, Object>>();

			for (Integer id : ids) {
				dataCollections.addAll(this.getWebServiceDataCollection3Service().getDataCollectionByDataCollectionGroupId(this.getProposalId(proposal), id));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(dataCollections, false);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/datacollection/workflow/{workflowIdList}/list")
	@Produces({ "application/json" })
	public Response getDataCollectionsByWorkflowId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("workflowIdList") String workflowIdList) {

		String methodName = "getDataCollectionsByWorkflowId";
		long start = this.logInit(methodName, logger, token, proposal, workflowIdList);
		try {
			List<Integer> ids = this.parseToInteger(workflowIdList);
			List<Map<String, Object>> dataCollections = new ArrayList<Map<String, Object>>();

			for (Integer id : ids) {
				dataCollections.addAll(this.getWebServiceDataCollection3Service().getViewDataCollectionsByWorkflowId(
						this.getProposalId(proposal), id));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(dataCollections, false);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/datacollection/protein_acronym/{protein_acronyms}/list")
	@Produces({ "application/json" })
	public Response getViewDataCollectionByProteinAcronym(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("protein_acronyms") String proteinAcronyms) {

		String methodName = "getViewDataCollectionByProteinAcronym";
		long start = this.logInit(methodName, logger, token, proposal, proteinAcronyms);
		try {
			List<String> acronyms = this.parseToString(proteinAcronyms);
			List<Map<String, Object>> dataCollections = new ArrayList<Map<String, Object>>();

			for (String acronym : acronyms) {
				dataCollections.addAll(this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionByProteinAcronym(
						this.getProposalId(proposal), acronym));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(dataCollections, false);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/datacollection/sample/{sampleId}/list")
	@Produces({ "application/json" })
	public Response getViewDataCollectionBySampleId(@PathParam("token") String token, @PathParam("proposal") String proposal,
														  @PathParam("sampleId") Integer sampleId) {

		String methodName = "getViewDataCollectionBySampleId";
		long start = this.logInit(methodName, logger, token, proposal, sampleId);
		try {
			List<Map<String, Object>> dataCollections = new ArrayList<Map<String, Object>>();

			dataCollections.addAll(this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionBySampleId(
						this.getProposalId(proposal), sampleId));
			this.logFinish(methodName, start, logger);
			return this.sendResponse(dataCollections, false);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/session/{sessionId}/report/send/pdf")
	@Produces({ "application/pdf" })
	public void exportReportAndSendAsPdf(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("sessionId") String sessionId) throws NamingException {

		String methodName = "exportReportAndSendAsPdf";
		long start = this.logInit(methodName, logger, token, proposal, sessionId);
		try {
			
			ByteArrayOutputStream baos = this.getPdfRtf(sessionId, proposal, null, false, false);
			this.logFinish(methodName, start, logger);
			
			if (sessionId != null) {
							
				Session3VO ses = this.getSession3Service().findByPk(Integer.valueOf(sessionId), false, false, false);

					Proposal3VO pv = ses.getProposalVO();
					// String mpEmail = personService.findByPk(pv.getPersonVOId(), false).getEmailAddress();
					String mpEmail = pv.getPersonVO().getEmailAddress();
					String from = Constants.getProperty("mail.report.from.mxind");
					String bcc = null;

					SimpleDateFormat simple1 = new SimpleDateFormat("dd/MM/yyyy");
					String date = simple1.format(ses.getStartDate());
					String subject = "MXpress FX " + proposal + " - " + date + " on " + ses.getBeamlineName();

					SimpleDateFormat simple = new SimpleDateFormat("ddMMyyyy");
					date = simple.format(ses.getStartDate());
					String attachName = proposal + "-" + date + "-" + ses.getBeamlineName() + ".pdf";
					String mimeType = "application/pdf";

					String to = Constants.getProperty("mail.report.to.test");
					String cc = Constants.getProperty("mail.report.cc.test");
					String body = Constants.getProperty("mail.report.body.test");

					if (Constants.IS_INDUSTRY_MAILING_IN_PROD()) {
						to = mpEmail;
						cc = Constants.getProperty("mail.report.cc");
						// ESRF ####
						if (Constants.SITE_IS_ESRF()) {
							bcc = Constants.getProperty("mail.report.bcc");
							if (proposal.endsWith("12"))
								cc = cc + "," + Constants.getProperty("mail.report.cc.fx12");
						}
						body = Constants.getProperty("mail.report.body");
					}

					if (baos != null) {
						SendMailUtils.sendMail(from, to, cc, bcc, subject, body, attachName, baos, mimeType, true);
						this.logFinish(methodName, start, logger);						
					}	
			}
						
		} catch (Exception e) {
			this.logError(methodName, e, start, logger);
		}	
		return;
	}
	
	private ByteArrayOutputStream getPdfRtf(String sessionId, String proposal, String nbRows, boolean isRtf, boolean isAnalysis) throws NamingException, Exception {
		
		Integer id = Integer.valueOf(sessionId);
		
		List<Map<String, Object>> dataCollections = 
				this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionBySessionIdHavingImages(this.getProposalId(proposal), id);
		
		List<Map<String, Object>> energyScans = this.getWebServiceEnergyScan3Service().getViewBySessionId(this.getProposalId(proposal), id);
		
		List<Map<String, Object>> xrfSpectrums = this.getWebServiceXFEFluorescenSpectrum3Service().getViewBySessionId(this.getProposalId(proposal), id);
		
		Integer nbRowsMax = dataCollections.size();
				
		if (nbRows != null && !nbRows.isEmpty()) {
			nbRowsMax = Integer.valueOf(nbRows);
		}
		 
		ExiPdfRtfExporter pdf = new ExiPdfRtfExporter(this.getProposalId(proposal), proposal, id , dataCollections, energyScans, xrfSpectrums, nbRowsMax);

		ByteArrayOutputStream baosToExport = null;
		
		if (isAnalysis)
			baosToExport = pdf.exportDataCollectionAnalysisReport(isRtf);
		else
			baosToExport = pdf.exportDataCollectionReport(isRtf);
		
		return baosToExport;
	}

	private ByteArrayOutputStream getCsv(String sessionId, String proposal) throws NamingException, Exception {

		Integer id = Integer.valueOf(sessionId);
		Session3VO slv = new Session3VO();
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		Session3Service sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
		Proposal3Service proposalService = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
		DataCollection3Service dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
				.getLocalService(DataCollection3Service.class);


		Proposal3VO pv = proposalService.findByPk(this.getProposalId(proposal));

		slv = sessionService.findByPk(id, false, false, false);
		List<DataCollection3VO>  dataCollectionList = dataCollectionService.findFiltered(null, null, null, id, null, null);
		ExiCsvExporter csv = new ExiCsvExporter(pv.getProposalId(), pv.getCode(), slv.getSessionId(), dataCollectionList);

		ByteArrayOutputStream baosToExport = csv.exportDataCollectionReport();

		return baosToExport;
	}
	
	private byte [] getPdfRtf(String filterParam, String proposal, boolean isRtf, boolean isAnalysis) throws NamingException, Exception {
		
		
		List<Map<String, Object>> dataCollections = 
				this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionByProteinAcronym(this.getProposalId(proposal), filterParam);
		
		if (dataCollections == null || dataCollections.isEmpty()) {
			dataCollections = 
				this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionBySampleName(this.getProposalId(proposal), filterParam);
		}
		
		if (dataCollections == null || dataCollections.isEmpty()) {
			dataCollections = 
				this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionByImagePrefix(this.getProposalId(proposal), filterParam);
		}
		
		List<Map<String, Object>> energyScans = null;
		
		List<Map<String, Object>> xrfSpectrums = null;
		
		Integer nbRowsMax = dataCollections.size();
		
		Integer id = null;
						 
		ExiPdfRtfExporter pdf = new ExiPdfRtfExporter(this.getProposalId(proposal), proposal, id , filterParam, dataCollections, energyScans, xrfSpectrums, nbRowsMax);
		byte [] byteToExport;
		
		if (isAnalysis)
			byteToExport = pdf.exportDataCollectionAnalysisReport(isRtf).toByteArray();
		else
			byteToExport = pdf.exportDataCollectionReport(isRtf).toByteArray();
		
		return byteToExport;
	}
}
