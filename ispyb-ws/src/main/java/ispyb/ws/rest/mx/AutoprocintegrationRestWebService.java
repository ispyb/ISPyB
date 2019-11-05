package ispyb.ws.rest.mx;

import ispyb.common.util.HashMapToZip;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.utils.reader.AutoProcProgramaAttachmentFileReader;
import ispyb.server.mx.services.ws.rest.autoprocessingintegration.AutoProcessingIntegrationService;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.jboss.resteasy.annotations.GZIP;

@Path("/")
public class AutoprocintegrationRestWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(AutoprocintegrationRestWebService.class);
	private static final String NOT_ALLOWED = "You don't have access to this resource";

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
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
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
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
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
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
	
	
	
	
	private boolean checkProposalByAutoProcProgramId(int proposalId, int autoProcProgramId) throws NamingException, Exception{
		return this.getSession3Service().findByAutoProcProgramId(autoProcProgramId).getProposalVOId().equals(proposalId);
	}
	
	
	/**
	 * AutoProcProgramAttachment has not AutoProcProgramId mapped in the EJB object
	 * so it is necessary to keep separately the possible list of ids in order
	 * to identify in the client the list of files linked to a sinble autoProcProgram
	 * 
	 * So, if autoprocattachmentids contains n different ids then the response will be an n-array with the list of files for each id
	 **/
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Produces("text/plain")
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/attachment/autoprocprogramid/{autoprocattachmentids}/download")
	public Response downloadAttachments(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("autoprocattachmentids") String autoprocattachmentids,
			@QueryParam("forceFilename") String forceFilename) {

		String methodName = "downloadAttachments";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<Integer> autoProcProgramIds = this.parseToInteger(autoprocattachmentids);
			List<List<AutoProcProgramAttachment3VO>> list = new ArrayList<List<AutoProcProgramAttachment3VO>>();
			HashMap<String, String> filePaths = new HashMap<String, String>();
			String filename = "download.zip";
			
			for (Integer autoProcProgramId : autoProcProgramIds) {
				/** Check that id correspond to the proposal **/
				if (!this.checkProposalByAutoProcProgramId(this.getProposalId(proposal), autoProcProgramId)){
					throw new Exception(NOT_ALLOWED);
				}
			
				
				AutoProcProgram3VO autoProcProgram3VO = this.getAutoProcProgram3Service().findByPk(autoProcProgramId, true);
				
				/** Prefix for the name of the file and the internal structure if many results are retrieved **/
				String prefix = String.format("%s_%s", autoProcProgram3VO.getProcessingPrograms(), autoProcProgram3VO.getAutoProcProgramId());
				
				list.add(autoProcProgram3VO.getAttachmentListVOs());
				ArrayList<AutoProcProgramAttachment3VO> listAttachments = autoProcProgram3VO.getAttachmentListVOs();
				for (AutoProcProgramAttachment3VO auto : listAttachments) {
					String filePath = auto.getFilePath() + "/" + auto.getFileName();
					if (new File(filePath).exists()){
						if (new File(filePath).isFile()){
							if (autoProcProgramIds.size() > 1){
								String zipNameFile = prefix + "/" + auto.getFileName();
								filePaths.put(zipNameFile, filePath);
							}
							else{
								filePaths.put(auto.getFileName(), filePath);
							}
						}
					}
				}
				
				/** If it is a single result then filename is the name of the program and the ID **/
				if (autoProcProgramIds.size() == 1){					
					filename = prefix + ".zip";
				}
				
				/** If forceFilename is filled then it will be used as filename **/ 
				if (forceFilename != null){
					if (forceFilename.length() > 0){
						filename = forceFilename; 
					}
				}
				
			}
			this.logFinish(methodName, start, logger);
			return this.downloadFile(HashMapToZip.doZip(filePaths), filename);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
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

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
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

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
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

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
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

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
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
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
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

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
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
	
	private boolean checkProposalByAutoProcProgramAttachmentId(int proposalId, int autoProcProgramAttachmentId) throws NamingException, Exception{
		return this.getSession3Service().findByAutoProcProgramAttachmentId(autoProcProgramAttachmentId).getProposalVOId().equals(proposalId);
	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/autoprocattachmentid/{autoProcAttachmentId}/download")
	@Produces({ "application/json" })
	public Response downloadAutoProcAttachment(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("autoProcAttachmentId") int autoProcAttachmentId) {

		String methodName = "downloadAutoProcAttachment";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			/** Checking that attachment is linked to the proposal **/
			if (this.checkProposalByAutoProcProgramAttachmentId(this.getProposalId(proposal), autoProcAttachmentId)){
				AutoProcProgramAttachment3VO attachment = this.getAutoProcProgramAttachment3Service().findByPk(autoProcAttachmentId);
				this.logFinish(methodName, start, logger);
				File file = new File(attachment.getFilePath() + "/" + attachment.getFileName());
				this.logFinish(methodName, start, logger);
				return this.downloadFileAsAttachment(file.getAbsolutePath());
			}
			else{
				throw new Exception(NOT_ALLOWED);
			}
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/autoprocattachmentid/{autoProcAttachmentId}/get")
	@Produces("text/plain")
	public Response getAutoProcAttachment(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("autoProcAttachmentId") int autoProcAttachmentId) {
		return this.getFile(token, proposal, autoProcAttachmentId);
	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/autoprocattachmentid/{autoProcAttachmentId}/getPdf")
	@Produces("application/pdf")
	public Response getAutoProcAttachmentPdf(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("autoProcAttachmentId") int autoProcAttachmentId) {
		return this.getFile(token, proposal, autoProcAttachmentId);
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/autoprocattachmentid/{autoProcAttachmentId}/getHtml")
	@Produces("text/html")
	public Response getAutoProcAttachmentHtml(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("autoProcAttachmentId") int autoProcAttachmentId) {
		return this.getFile(token, proposal, autoProcAttachmentId);
	}
	
	private Response getFile(String token, String proposal,  int autoProcAttachmentId) {
		String methodName = "getFile";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			/** Checking that attachment is linked to the proposal **/
			if (this.checkProposalByAutoProcProgramAttachmentId(this.getProposalId(proposal), autoProcAttachmentId)){
				AutoProcProgramAttachment3VO attachment = this.getAutoProcProgramAttachment3Service().findByPk(autoProcAttachmentId);
				File file = new File(attachment.getFilePath() + "/" + attachment.getFileName());
				this.logFinish(methodName, start, logger);
				return this.downloadFile(file.getAbsolutePath());
			}
			else{
				throw new Exception(NOT_ALLOWED);
			}
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/{autoProcIntegrationListId}/fastdp/cc2")
	@Produces("text/plain")
	public Response getFastDPCC2(@PathParam("token") String token, @PathParam("proposal") String proposal,
								 @PathParam("autoProcIntegrationListId") String autoProcIntegrationListId) {

		String methodName = "getFastDPCC2";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			String result = this.getFastDPParserByAutoProcIntegrationListId(
					this.parseToInteger(autoProcIntegrationListId)).parsecc2();
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/{autoProcIntegrationListId}/fastdp/rfactor")
	@Produces("text/plain")
	public Response getFastDPRfactor(@PathParam("token") String token, @PathParam("proposal") String proposal,
									 @PathParam("autoProcIntegrationListId") String autoProcIntegrationListId) {

		String methodName = "getFastDPRfactor";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			String result = this.getFastDPParserByAutoProcIntegrationListId(
					this.parseToInteger(autoProcIntegrationListId)).parseRfactor();
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/{autoProcIntegrationListId}/fastdp/completeness")
	@Produces("text/plain")
	public Response getFastDPCompleteness(@PathParam("token") String token, @PathParam("proposal") String proposal,
										  @PathParam("autoProcIntegrationListId") String autoProcIntegrationListId) {

		String methodName = "getFastDPCompleteness";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			String result = this.getFastDPParserByAutoProcIntegrationListId(this.parseToInteger(autoProcIntegrationListId)).parseCompleteness();
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/{autoProcIntegrationListId}/fastdp/isigma")
	@Produces("text/plain")
	public Response getFastDPISigma(@PathParam("token") String token, @PathParam("proposal") String proposal,
									@PathParam("autoProcIntegrationListId") String autoProcIntegrationListId) {

		String methodName = "getFastDPISigma";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			String result = this.getFastDPParserByAutoProcIntegrationListId(
					this.parseToInteger(autoProcIntegrationListId)).parseISigma();
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
}
