package ispyb.ws.rest.mx;

import ispyb.common.util.HashMapToZip;
import ispyb.server.common.test.services.ZipperTest;
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
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.jboss.resteasy.annotations.GZIP;

@Path("/")
public class AutoprocintegrationRestWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(AutoprocintegrationRestWebService.class);

	@RolesAllowed({ "User", "Manager", "Localcontact" })
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
	
	@RolesAllowed({ "User", "Manager", "Localcontact" })
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
	@RolesAllowed({ "User", "Manager", "Localcontact" })
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
	
	
	/**
	 * AutoProcProgramAttachment has not AutoProcProgramId mapped in the EJB object
	 * so it is necessary to keep separately the possible list of ids in order
	 * to identify in the client the list of files linked to a sinble autoProcProgram
	 * 
	 * So, if autoprocattachmentids contains n different ids then the response will be an n-array with the list of files for each id
	 **/
	@RolesAllowed({ "User", "Manager", "Localcontact" })
	@GET
	@GZIP
	@Produces("text/plain")
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/attachment/autoprocprogramid/{autoprocattachmentids}/download")
	public Response downloadAttachments(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("autoprocattachmentids") String autoprocattachmentids) {

		String methodName = "downloadAttachments";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<Integer> ids = this.parseToInteger(autoprocattachmentids);
			List<List<AutoProcProgramAttachment3VO>> list = new ArrayList<List<AutoProcProgramAttachment3VO>>();
			HashMap<String, String> filePaths = new HashMap<String, String>();
			String filename = "download.zip";
			for (Integer id : ids) {
				AutoProcProgram3VO autoProcProgram3VO = this.getAutoProcProgram3Service().findByPk(id, true);
				list.add(autoProcProgram3VO.getAttachmentListVOs());
				ArrayList<AutoProcProgramAttachment3VO> listAttachments = autoProcProgram3VO.getAttachmentListVOs();
				for (AutoProcProgramAttachment3VO auto : listAttachments) {
					System.out.println("------------");
					System.out.println(auto.getFileName());
					System.out.println(auto.getFilePath());
					String filePah = auto.getFilePath() + "/" + auto.getFileName();
					if (new File(filePah).exists()){
						if (new File(filePah).isFile()){
							filePaths.put(auto.getFileName(), filePah);
						}
					}
				}
				filename = autoProcProgram3VO.getProcessingPrograms() + "_" +autoProcProgram3VO.getAutoProcProgramId() +".zip";
			}
			this.logFinish(methodName, start, logger);
			return this.downloadFile(HashMapToZip.doZip(filePaths), filename);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	

	@RolesAllowed({ "User", "Manager", "Localcontact" })
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

	@RolesAllowed({ "User", "Manager", "Localcontact" })
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

	@RolesAllowed({ "User", "Manager", "Localcontact" })
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

	@RolesAllowed({ "User", "Manager", "Localcontact" })
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

	@RolesAllowed({ "User", "Manager", "Localcontact" })
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
	
	@RolesAllowed({ "User", "Manager", "Localcontact" })
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

	@RolesAllowed({ "User", "Manager", "Localcontact" })
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
	
	@RolesAllowed({ "User", "Manager", "Localcontact" })
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
			this.logFinish(methodName, start, logger);
			return this.downloadFile(file.getAbsolutePath());
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	

}
