package ispyb.ws.rest.mx;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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

import ispyb.common.util.export.PdfExporterSample;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.services.ws.rest.sample.SampleRestWsService;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.SampleInfo;

@Path("/")
public class SampleRestWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(SampleRestWebService.class);

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/sample/crystalId/{crystalId}/list")
	@Produces({ "application/json" })
	public Response getSamplesByCrystalId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("crystalId") String crystalId) {

		String methodName = "getSamplesByCrystalId";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<BLSample3VO> samples = this.getBLSample3Service().findByCrystalId(new Integer(crystalId));
			this.logFinish(methodName, start, logger);
			return this.sendResponse(samples);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/sampleinfo/crystalId/{crystalId}/list")
	@Produces({ "application/json" })
	public Response getSampleInfoByCrystalId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("crystalId") String crystalId) {

		String methodName = "getSampleInfoByCrystalId";
		long start = this.logInit(methodName, logger, token, proposal);
		try {

			SampleInfo[] samples = this.getBLSample3Service().findForWSSampleInfoLight(null, new Integer(crystalId), null, null);
			this.logFinish(methodName, start, logger);
			List<SampleInfo> sampleinfo = Arrays.asList(samples);
			return this.sendResponse(sampleinfo);
		}
		catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@Deprecated
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/sampleinfo/list")
	@Produces({ "application/json" })
	public Response getSampleInfoByProposalId(@PathParam("token") String token, @PathParam("proposal") String proposal) {
		
		String methodName = "getSampleInfoByProposalId";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			int proposalId = this.getProposalId(proposal);
			SampleInfo[] samples = this.getBLSample3Service().findForWSSampleInfoLight(proposalId, null, null, null);
			this.logFinish(methodName, start, logger);
			return this.sendResponse(samples);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	
	protected SampleRestWsService getSampleRestWsService() throws NamingException {
		return (SampleRestWsService) Ejb3ServiceLocator.getInstance().getLocalService(SampleRestWsService.class);
	}
	
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/sample/list")
	@Produces({ "application/json" })
	public Response getSampleByProposalId(@PathParam("token") String token, @PathParam("proposal") String proposal) {
		
		String methodName = "getSampleByProposalId";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<Map<String, Object>> samples = this.getSampleRestWsService().getSamplesByProposalId(this.getProposalId(proposal));
			this.logFinish(methodName, start, logger);
			return this.sendResponse(samples);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/sample/dewarid/{dewarid}/list")
	@Produces({ "application/json" })
	public Response getSamplesByDewarId(@PathParam("token") String token, @PathParam("proposal") String proposal, @PathParam("dewarId") int dewarId) {
		String methodName = "getSamplesByDewarId";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<Map<String, Object>> samples = this.getSampleRestWsService().getSamplesByDewarId(this.getProposalId(proposal), dewarId);
			this.logFinish(methodName, start, logger);
			return this.sendResponse(samples);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/sample/containerid/{containerid}/list")
	@Produces({ "application/json" })
	public Response getSamplesByContainerId(@PathParam("token") String token, @PathParam("proposal") String proposal, @PathParam("containerid") String containerid) {
		String methodName = "getSamplesByContainerId";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<Integer> ids = this.parseToInteger(containerid);
			List<Map<String, Object>> samples = new ArrayList<Map<String,Object>>();
			for (Integer id : ids) {
				samples.addAll(this.getSampleRestWsService().getSamplesByContainerId(this.getProposalId(proposal), id));	
			}
			
			this.logFinish(methodName, start, logger);
			return this.sendResponse(samples);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/sample/sessionid/{sessionid}/list")
	@Produces({ "application/json" })
	public Response getSamplesBySessionId(@PathParam("token") String token, @PathParam("proposal") String proposal, @PathParam("sessionid") int sessionid) {
		String methodName = "getSamplesBySessionId";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<Map<String, Object>> samples = this.getSampleRestWsService().getSamplesBySessionId(this.getProposalId(proposal), sessionid);
			this.logFinish(methodName, start, logger);
			return this.sendResponse(samples);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/sample/shipmentid/{shipmentid}/list")
	@Produces({ "application/json" })
	public Response getSamplesByShipmentId(@PathParam("token") String token, @PathParam("proposal") String proposal, @PathParam("shipmentid") int shipmentid) {
		String methodName = "getSamplesByShipmentId";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<Map<String, Object>> samples = this.getSampleRestWsService().getSamplesByShipmentId(this.getProposalId(proposal), shipmentid);
			this.logFinish(methodName, start, logger);
			return this.sendResponse(samples);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	/**
	 * 
	 * @param token
	 * @param proposal
	 * @param acronym
	 * @param sortView can be 1:default or 2: in the case of view sorted by dewar/container, a page break is added after each container
	 * @return
	 * @throws NamingException
	 */
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/sample/acronym/{acronym}/sortView/{sortView}/list/pdf")
	@Produces({ "application/pdf" })
	public Response getSamplesListByAcronymPDF(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("acronym") String acronym,
			@PathParam("sortView") String sortView) throws NamingException {

		long start = this.logInit("getSamplesListByAcronymPDF", logger, token, proposal,
				acronym);
		String sortType = "name";
		if (sortView == null || sortView.isEmpty()) {
			sortView = "1";
		} else if (sortView.equals("2") ){
			sortType = "container";
		}
		try {
			List<BLSample3VO> sampleList = this.getBLSample3Service().findByAcronymAndProposalId(acronym, this.getProposalId(proposal), sortType);
			String viewName = "Sample list for acronym "+ acronym;
			PdfExporterSample pdf = new PdfExporterSample(sampleList, viewName, sortView, proposal);

			byte [] byteToExport = pdf.exportAsPdf().toByteArray();
			return this.downloadFile(byteToExport, "Sample_list_for_" + acronym +".pdf");
			
		} catch (Exception e) {
			return this.logError("getLabels", e, start, logger);
		}
	}
	
	/**
	 * 
	 * @param token
	 * @param proposal
	 * @param acronym
	 * @param sortView can be 1:default or 2: in the case of view sorted by dewar/container, a page break is added after each container
	 * @return
	 * @throws NamingException
	 */
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/sample/dewar/{dewarIdList}/sortView/{sortView}/list/pdf")
	@Produces({ "application/pdf" })
	public Response getSamplesListByDewarIdPDF(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("dewarIdList") String dewarIdList,
			@PathParam("sortView") String sortView) throws NamingException {

		long start = this.logInit("getSamplesListByDewarIdPDF", logger, token, proposal,
				dewarIdList);
		try {
			List<Integer> ids = this.parseToInteger(dewarIdList);	
			
			List<BLSample3VO> sampleList = this.getBLSample3Service().findByDewarId(ids, new Integer(sortView));
		
			String viewName = "Sample list for dewars "+ dewarIdList;
			PdfExporterSample pdf = new PdfExporterSample(sampleList, viewName, sortView.toString(), proposal);

			byte [] byteToExport = pdf.exportAsPdf().toByteArray();
			return this.downloadFile(byteToExport, "Sample_list_for_dewars.pdf");
			
		} catch (Exception e) {
			return this.logError("getLabels", e, start, logger);
		}
	}

}
