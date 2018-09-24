package ispyb.ws.rest.saxs;

import java.io.File;
import java.nio.file.Files;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

@Path("/")
public class ModelRestWebService extends SaxsRestWebService {

	private final static Logger logger = Logger.getLogger(ModelRestWebService.class);

	@RolesAllowed({ "User", "Manager", "Industrial", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/saxs/subtraction/{subtractionId}/model/{modelId}/pdb")
	@Produces("text/plain")
	public Response getPDBByModelId(@PathParam("token") String token, @PathParam("proposal") String proposal, @PathParam("subtractionId") String subtractionId,
			@PathParam("modelId") Integer modelId) throws Exception {
		logger.info(String.format("getPDBByModelId. proposal=%s, subtractionId=%s, modelId=%s", proposal, subtractionId, modelId));		
		try {
			return downloadFileAsAttachment(this.getAbInitioModelling3Service().findModelById(modelId).getPdbFile());
		} catch (Exception e) {
			return sendError(e.getMessage());
		}
	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/saxs/subtraction/{subtractionId}/model/{modelId}/fir")
	@Produces("text/plain")
	public Response getFirByModelId(@PathParam("token") String token, @PathParam("proposal") String proposal, @PathParam("subtractionId") String subtractionId,
			@PathParam("modelId") Integer modelId) throws Exception {
		logger.info(String.format("getFirByModelId. proposal=%s, subtractionId=%s, modelId=%s", proposal, subtractionId, modelId));		
		try {
			return downloadFile(this.getAbInitioModelling3Service().findModelById(modelId).getFirFile());
		} catch (Exception e) {
			return sendError(e.getMessage());
		}
	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/saxs/subtraction/{subtractionId}/model/{modelId}/fir/content")
	@Produces("text/plain")
	public Response getFirContentByModelId(@PathParam("token") String token, @PathParam("proposal") String proposal, @PathParam("subtractionId") String subtractionId,
			@PathParam("modelId") Integer modelId) throws Exception {
		logger.info(String.format("getFirContentByModelId. proposal=%s, subtractionId=%s, modelId=%s", proposal, subtractionId, modelId));		
		try {
			return sendResponse(FileUtils.readFileToString(new File(this.getAbInitioModelling3Service().findModelById(modelId).getFirFile())));
		} catch (Exception e) {
			return sendError(e.getMessage());
		}
	}
	
	
	@RolesAllowed({ "User", "Manager", "Industrial", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/saxs/subtraction/{subtractionId}/model/{modelId}/fit")
	@Produces("text/plain")
	public Response getFitByModelId(@PathParam("token") String token, @PathParam("proposal") String proposal, @PathParam("subtractionId") String subtractionId,
			@PathParam("modelId") Integer modelId) throws Exception {
		logger.info(String.format("getFitByModelId. proposal=%s, subtractionId=%s, modelId=%s", proposal, subtractionId, modelId));		
		try {
			return downloadFile(this.getAbInitioModelling3Service().findModelById(modelId).getFitFile());
		} catch (Exception e) {
			return sendError(e.getMessage());
		}
	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/saxs/subtraction/{subtractionId}/model/{modelId}/fit/content")
	@Produces("text/plain")
	public Response getFitContentByModelId(@PathParam("token") String token, @PathParam("proposal") String proposal, @PathParam("subtractionId") String subtractionId,
			@PathParam("modelId") Integer modelId) throws Exception {
		logger.info(String.format("getFitContentByModelId. proposal=%s, subtractionId=%s, modelId=%s", proposal, subtractionId, modelId));		
		try {
			return sendResponse(FileUtils.readFileToString(new File(this.getAbInitioModelling3Service().findModelById(modelId).getFitFile())));
		} catch (Exception e) {
			return sendError(e.getMessage());
		}
	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/saxs/subtraction/{subtractionId}/model/{modelId}/log")
	@Produces("text/plain")
	public Response getLogByModelId(@PathParam("token") String token, @PathParam("proposal") String proposal, @PathParam("subtractionId") String subtractionId,
			@PathParam("modelId") Integer modelId) throws Exception {
		logger.info(String.format("getLogByModelId. proposal=%s, subtractionId=%s, modelId=%s", proposal, subtractionId, modelId));		
		try {
			return downloadFile(this.getAbInitioModelling3Service().findModelById(modelId).getLogFile());
		} catch (Exception e) {
			return sendError(e.getMessage());
		}
	}


}
