package ispyb.ws.rest.mx;

import ispyb.common.util.HashMapToZip;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.ws.rest.phasing.PhasingRestWsService;
import ispyb.server.mx.vos.autoproc.PhasingProgramAttachment3VO;
import ispyb.server.mx.vos.autoproc.PhasingStepVO;
import ispyb.server.mx.vos.collections.DataCollection3VO;

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

import org.apache.commons.compress.archivers.zip.ZipMethod;
import org.apache.log4j.Logger;
import org.jboss.resteasy.annotations.GZIP;

@Path("/")
public class PhasingRestWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(PhasingRestWebService.class);

	protected PhasingRestWsService getPhasingWSService() throws NamingException {
		return (PhasingRestWsService) Ejb3ServiceLocator.getInstance().getLocalService(PhasingRestWsService.class);
	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/phasing/autoprocintegrationid/{autoprocIntegrationId}/list")
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
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/phasing/datacollectionid/{dataCollectionIds}/list")
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
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/phasing/datacollectiongroupid/{dataCollectionGroupIds}/list")
	@Produces({ "application/json" })
	public Response getPhasingViewByDataCollectionGroupId(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("dataCollectionGroupIds") String dataCollectionGroupIds) {

		String methodName = "getPhasingViewByDataCollectionGroupId";
		long start = this.logInit(methodName, logger, token, proposal, dataCollectionGroupIds);
		try {
			List<Integer> ids = this.parseToInteger(dataCollectionGroupIds);
			List<List<Map<String, Object>>> list = new ArrayList<List<Map<String,Object>>>();
			for (Integer id : ids) {
				list.add(this.getPhasingWSService().getPhasingViewByDataCollectionGroupId(id, this.getProposalId(proposal)));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(list);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/phasing/sampleid/{sampleIds}/list")
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
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/phasing/proteinid/{proteinIds}/list")
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
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/phasing/sessionid/{sessionIds}/list")
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
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/phasing/phasingstepid/{phasingStepIds}/list")
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
	
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/phasing/phasingstepid/{phasingStepIds}/files")
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
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Produces("text/plain")
	@Path("{token}/proposal/{proposal}/mx/phasing/phasingstepid/{phasingStepIds}/download")
	public Response downloadPhasingFilesByPhasingStepId(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("phasingStepIds") String phasingStepIds) {

		String methodName = "downloadPhasingFilesByPhasingStepId";
		long start = this.logInit(methodName, logger, token, proposal, phasingStepIds);
		try {
			List<Integer> ids = this.parseToInteger(phasingStepIds);
			HashMap<String, String> files = new HashMap<String, String>();
			
			String firstId = "phasing";
			for (Integer id : ids) {
				firstId = id.toString();
				List<Map<String, Object>> phasingFiles = this.getPhasingWSService().getPhasingFilesViewByStepId(id, this.getProposalId(proposal));
				PhasingStepVO phasing = this.getPhasingStep3Service().findById(id);
				if (phasing != null){
					if (phasingFiles != null){
						if (phasingFiles.size() > 0){
							for (Map<String, Object> map : phasingFiles) {
								if (map.get("fileName") != null){
									String fileName = map.get("fileName").toString();
									if (map.get("filePath") != null){
										String filePath = map.get("filePath").toString();
										files.put(phasing.getSpaceGroupVO().getSpaceGroupShortName() + "/" + phasing.getPhasingStepType() + "_" + phasing.getPhasingStepId() + "/" +  fileName, filePath);
									}
								}
							}
						}
					}
				}
			}
			
			byte[] bytes = HashMapToZip.doZip(files);
			this.logFinish(methodName, start, logger);
			return this.downloadFile(bytes, firstId + ".zip");
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/phasing/phasingprogramattachmentid/{phasingProgramAttachmentId}/image")
	@Produces("image/png")
	public Response getPhasingFilesByPhasingProgramAttachmentIdAsImage(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("phasingProgramAttachmentId") String phasingProgramAttachmentId) {

		String methodName = "getPhasingFilesByPhasingProgramAttachmentIdAsImage";
		long start = this.logInit(methodName, logger, token, proposal, phasingProgramAttachmentId);
		try {
			List<Integer> ids = this.parseToInteger(phasingProgramAttachmentId);
			List<List<Map<String, Object>>> list = new ArrayList<List<Map<String,Object>>>();
			for (Integer id : ids) {
				list.add(this.getPhasingWSService().getPhasingFilesViewByPhasingProgramAttachmentId(id, this.getProposalId(proposal)));
			}
			
			System.out.println(this.getGson().toJson(list));
			
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
						return this.downloadFileAsAttachment(list.get(0).get(0).get("filePath").toString());
					}
				}
			}
			
			this.logFinish(methodName, start, logger);
			return this.sendResponse(list);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/phasing/phasingprogramattachmentid/{phasingProgramAttachmentId}/download")
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
			
			System.out.println(this.getGson().toJson(list));
			
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
						return this.downloadFileAsAttachment(list.get(0).get(0).get("filePath").toString());
					}
				}
			}
			
			this.logFinish(methodName, start, logger);
			return this.sendResponse(list);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/phasing/phasingprogramattachmentid/{phasingProgramAttachmentId}/csv")
 	@Produces("text/plain")
	public Response getCSVPhasingFileByPhasingProgramAttachmentId(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("phasingProgramAttachmentId") int phasingProgramAttachmentId) {

		String methodName = "getCSVPhasingFileByPhasingProgramAttachmentId";
		long start = this.logInit(methodName, logger, token, proposal, phasingProgramAttachmentId);
		try {
			PhasingProgramAttachment3VO attachment = this.getPhasingProgramAttachment3Service().findByPk(phasingProgramAttachmentId);
			if (attachment != null){
				if (attachment.getFilePath() != null){
					if (new File(attachment.getFilePath()).exists()){
						this.logFinish(methodName, start, logger);
						return this.downloadFileAsAttachment(attachment.getFilePath());
					}
					else{
						throw new Exception("File " +  attachment.getFilePath() + " does not exist");
					}
				}
			}
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
		return null;
	}
	
	
}
