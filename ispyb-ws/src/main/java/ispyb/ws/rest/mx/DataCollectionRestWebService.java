package ispyb.ws.rest.mx;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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

import org.apache.log4j.Logger;
import org.jboss.resteasy.annotations.GZIP;

import ispyb.common.util.export.ExiPdfRtfExporter;
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
			byte[] byteToExport = this.getPdfRtf(sessionId, proposal, nbRows, false);
			this.logFinish(methodName, start, logger);
			Session3VO ses = this.getSession3Service().findByPk(new Integer(sessionId), false, false, false);
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
	@Path("{token}/proposal/{proposal}/mx/datacollection/session/{sessionId}/report/rtf")
	@Produces({ "application/rtf" })
	public Response getDataCollectionsReportBySessionIdRTF(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("sessionId") String sessionId, @QueryParam("nbRows") String nbRows) throws NamingException {

		String methodName = "getDataCollectionReportyBySessionIdRtf";
		long start = this.logInit(methodName, logger, token, proposal, sessionId);
		try {
			byte[] byteToExport = this.getPdfRtf(sessionId, proposal, nbRows, true);
			this.logFinish(methodName, start, logger);
			Session3VO ses = this.getSession3Service().findByPk(new Integer(sessionId), false, false, false);
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
			byte[] byteToExport = this.getAnalysisPdfRtf(sessionId, proposal, nbRows, false);
			this.logFinish(methodName, start, logger);
			Session3VO ses = this.getSession3Service().findByPk(new Integer(sessionId), false, false, false);
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
	@Path("{token}/proposal/{proposal}/mx/datacollection/session/{sessionId}/analysisreport/rtf")
	@Produces({ "application/rtf" })
	public Response getDataCollectionsAnalysisReportBySessionIdRTF(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("sessionId") String sessionId, @QueryParam("nbRows") String nbRows) throws NamingException {

		String methodName = "getDataCollectionReportyBySessionIdRtf";
		long start = this.logInit(methodName, logger, token, proposal, sessionId);
		try {
			byte[] byteToExport = this.getAnalysisPdfRtf(sessionId, proposal, nbRows, true);
			this.logFinish(methodName, start, logger);
			Session3VO ses = this.getSession3Service().findByPk(new Integer(sessionId), false, false, false);
			if (ses !=null)
				return this.downloadFile(byteToExport, "AnalysisReport_" + proposal + "_"+ ses.getBeamlineName()+ "_" + ses.getStartDate() + ".rtf");
			else
				return this.downloadFile(byteToExport, "No_session.pdf");		
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	
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

		String methodName = "getViewDataCollectionByWorkflowStepId";
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
	
	private byte [] getPdfRtf(String sessionId, String proposal, String nbRows, boolean isRtf) throws NamingException, Exception {
		
		Integer id = new Integer(sessionId);
		
		List<Map<String, Object>> dataCollections = 
				this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionBySessionId(this.getProposalId(proposal), id);
		
		Integer nbRowsMax = dataCollections.size();
				
		if (nbRows != null && !nbRows.isEmpty()) {
			nbRowsMax = new Integer(nbRows);
		}
		
		ExiPdfRtfExporter pdf = new ExiPdfRtfExporter(this.getProposalId(proposal), proposal, id , dataCollections, nbRowsMax);
		
		byte [] byteToExport = pdf.exportDataCollectionReport(isRtf).toByteArray();

		return byteToExport;
	}
	
	private byte [] getAnalysisPdfRtf(String sessionId, String proposal, String nbRows, boolean isRtf) throws NamingException, Exception {
		
		Integer id = new Integer(sessionId);
		
		List<Map<String, Object>> dataCollections = 
				this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionBySessionId(this.getProposalId(proposal), id);

		Integer nbRowsMax = dataCollections.size();
		
		if (nbRows != null && !nbRows.isEmpty()) {
			nbRowsMax = new Integer(nbRows);
		}
		
		ExiPdfRtfExporter pdf = new ExiPdfRtfExporter(this.getProposalId(proposal), proposal, id , dataCollections, nbRowsMax);
		
		byte [] byteToExport = pdf.exportDataCollectionAnalysisReport(isRtf).toByteArray();

		return byteToExport;
	}
}
