package ispyb.ws.rest.mx;

import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.ws.rest.phasing.PhasingRestWsService;

import java.util.ArrayList;
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
public class PhasingRestWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(PhasingRestWebService.class);

	protected PhasingRestWsService getPhasingWSService() throws NamingException {
		return (PhasingRestWsService) Ejb3ServiceLocator.getInstance().getLocalService(PhasingRestWsService.class);
	}
	
	@RolesAllowed({ "User", "Manager", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/phasing/autoprocIntegrationId/{autoprocIntegrationId}/list")
	@Produces({ "application/json" })
	public Response getPhasingViewByAutoProcIntegrationId(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("autoprocIntegrationId") String autoprocIntegrationId) {

		String methodName = "getPhasingViewByAutoProcIntegrationId";
		long start = this.logInit(methodName, logger, token, proposal, autoprocIntegrationId);
		try {
			List<Integer> ids = this.parseToInteger(autoprocIntegrationId);
			List<List<Map<String, Object>>> list = new ArrayList<List<Map<String,Object>>>();
			for (Integer id : ids) {
				list.add(this.getPhasingWSService().getPhasingViewByAutoProcIntegrationId(id, this.getProposalId(proposal)));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(list);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({ "User", "Manager", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/phasing/dataCollectionId/{dataCollectionIds}/list")
	@Produces({ "application/json" })
	public Response getPhasingViewByDataCollectionId(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("dataCollectionIds") String dataCollectionIds) {

		String methodName = "getPhasingViewByDataCollectionId";
		long start = this.logInit(methodName, logger, token, proposal, dataCollectionIds);
		try {
			List<Integer> ids = this.parseToInteger(dataCollectionIds);
			List<List<Map<String, Object>>> list = new ArrayList<List<Map<String,Object>>>();
			for (Integer id : ids) {
				list.add(this.getPhasingWSService().getPhasingViewByDataCollectionId(id, this.getProposalId(proposal)));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(list);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	

	@RolesAllowed({ "User", "Manager", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/phasing/sampleId/{sampleIds}/list")
	@Produces({ "application/json" })
	public Response getPhasingViewBySampleId(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("sampleIds") String sampleIds) {

		String methodName = "getPhasingViewBySampleId";
		long start = this.logInit(methodName, logger, token, proposal, sampleIds);
		try {
			List<Integer> ids = this.parseToInteger(sampleIds);
			List<List<Map<String, Object>>> list = new ArrayList<List<Map<String,Object>>>();
			for (Integer id : ids) {
				list.add(this.getPhasingWSService().getPhasingViewByBlSampleId(id, this.getProposalId(proposal)));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(list);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({ "User", "Manager", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/phasing/proteinId/{proteinIds}/list")
	@Produces({ "application/json" })
	public Response getPhasingViewByProteinId(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("proteinIds") String proteinIds) {

		String methodName = "getPhasingViewByProteinId";
		long start = this.logInit(methodName, logger, token, proposal, proteinIds);
		try {
			List<Integer> ids = this.parseToInteger(proteinIds);
			List<List<Map<String, Object>>> list = new ArrayList<List<Map<String,Object>>>();
			for (Integer id : ids) {
				list.add(this.getPhasingWSService().getPhasingViewByProteinId(id, this.getProposalId(proposal)));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(list);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({ "User", "Manager", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/phasing/sessionId/{sessionIds}/list")
	@Produces({ "application/json" })
	public Response getPhasingViewBySessionId(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("sessionIds") String sessionIds) {

		String methodName = "getPhasingViewBySessionId";
		long start = this.logInit(methodName, logger, token, proposal, sessionIds);
		try {
			List<Integer> ids = this.parseToInteger(sessionIds);
			List<List<Map<String, Object>>> list = new ArrayList<List<Map<String,Object>>>();
			for (Integer id : ids) {
				list.add(this.getPhasingWSService().getPhasingViewBySessionId(id, this.getProposalId(proposal)));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(list);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({ "User", "Manager", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/phasing/phasingStepId/{phasingStepIds}/list")
	@Produces({ "application/json" })
	public Response getPhasingViewByPhasingStepId(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("phasingStepIds") String phasingStepIds) {

		String methodName = "getPhasingViewByPhasingStepId";
		long start = this.logInit(methodName, logger, token, proposal, phasingStepIds);
		try {
			List<Integer> ids = this.parseToInteger(phasingStepIds);
			List<List<Map<String, Object>>> list = new ArrayList<List<Map<String,Object>>>();
			for (Integer id : ids) {
				list.add(this.getPhasingWSService().getPhasingViewByStepId(id, this.getProposalId(proposal)));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(list);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	
	@RolesAllowed({ "User", "Manager", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/phasing/phasingStepId/{phasingStepIds}/files")
	@Produces({ "application/json" })
	public Response getPhasingFilesByPhasingStepId(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("phasingStepIds") String phasingStepIds) {

		String methodName = "getPhasingFilesByPhasingStepId";
		long start = this.logInit(methodName, logger, token, proposal, phasingStepIds);
		try {
			List<Integer> ids = this.parseToInteger(phasingStepIds);
			List<List<Map<String, Object>>> list = new ArrayList<List<Map<String,Object>>>();
			for (Integer id : ids) {
				list.add(this.getPhasingWSService().getPhasingFilesViewByStepId(id, this.getProposalId(proposal)));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(list);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({ "User", "Manager", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/phasing/phasingProgramAttachmentId/{phasingProgramAttachmentId}/download")
	@Produces({ "application/json" })
	public Response getPhasingFilesByPhasingProgramAttachmentId(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("phasingProgramAttachmentId") String phasingProgramAttachmentId) {

		String methodName = "getPhasingFilesByPhasingProgramAttachmentId";
		long start = this.logInit(methodName, logger, token, proposal, phasingProgramAttachmentId);
		try {
			List<Integer> ids = this.parseToInteger(phasingProgramAttachmentId);
			List<List<Map<String, Object>>> list = new ArrayList<List<Map<String,Object>>>();
			for (Integer id : ids) {
				list.add(this.getPhasingWSService().getPhasingFilesViewByPhasingProgramAttachmentId(id, this.getProposalId(proposal)));
			}
			
			/** If single file it returns the file otherwise it returns a zip file **/
			if (list != null){
				if (list.size() > 0){
					if (list.size() > 1){
						/** Do zip **/
						System.out.println("Do zip");
						for (List<Map<String, Object>> record : list) {
							System.out.println(record.get(0).get("filePath"));
						}
					}
					else{
						/** Returns file **/
						System.out.println("Single file");
						System.out.println(list.get(0).get(0).get("filePath"));
						return this.downloadFile(list.get(0).get(0).get("filePath").toString());
					}
				}
			}
			
			this.logFinish(methodName, start, logger);
			return this.sendResponse(list);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	
}
