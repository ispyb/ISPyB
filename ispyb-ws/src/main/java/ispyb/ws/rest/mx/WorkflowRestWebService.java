package ispyb.ws.rest.mx;

import ispyb.server.mx.vos.collections.WorkflowStep3VO;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.google.gson.stream.JsonReader;

@Path("/")
public class WorkflowRestWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(WorkflowRestWebService.class);

	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/workflow/step/{workflowStepIds}/list")
	@Produces("text/plain")
	public Response getWorkflowStepByIds(
			@PathParam("token") String token,
			@PathParam("proposal") String proposal, 
			@PathParam("workflowStepIds") String workflowStepIds) {

		String methodName = "getWorkflowStepByIds";
		long start = this.logInit(methodName, logger, token, proposal, workflowStepIds);
		try {
			
			List<Integer> ids = this.parseToInteger(workflowStepIds);
			List<WorkflowStep3VO> workflowStepList = new ArrayList<WorkflowStep3VO>();
			for (Integer id : ids) {
				workflowStepList.add(this.getWorkflowStep3Service().findById(id));
			}
			return this.sendResponse(workflowStepList);

		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	
	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/workflow/step/{workflowStepId}/image")
	@Produces("image/png")
	public Response getWorkflowStepImageById(
			@PathParam("token") String token,
			@PathParam("proposal") String proposal, 
			@PathParam("workflowStepId") int workflowStepId) {

		String methodName = "getWorkflowStepImageById";
		long start = this.logInit(methodName, logger, token, proposal, workflowStepId);
		try {
			WorkflowStep3VO workflowStep = this.getWorkflowStep3Service().findById(workflowStepId);
			this.logFinish(methodName, start, logger);
			if (workflowStep != null){
				if (workflowStep.getImageResultFilePath() != null){
					if (new File(workflowStep.getImageResultFilePath()).exists()){
						return this.sendImage(workflowStep.getImageResultFilePath());
					}
				}
				throw new Exception("File " + workflowStep.getImageResultFilePath() + " does not exit");
			}
			throw new Exception("WorkflowStep with id " + workflowStepId + " does not exit");

		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/workflow/step/{workflowStepId}/html")
	@Produces({ "text/html" })
	public String getWorkflowStepHTMLById(
			@PathParam("token") String token,
			@PathParam("proposal") String proposal, 
			@PathParam("workflowStepId") int workflowStepId) {

		String methodName = "getWorkflowStepHTMLById";
		long start = this.logInit(methodName, logger, token, proposal, workflowStepId);
		try {
			WorkflowStep3VO workflowStep = this.getWorkflowStep3Service().findById(workflowStepId);
			this.logFinish(methodName, start, logger);
			if (workflowStep != null){
				if (workflowStep.getHtmlResultFilePath() != null){
					if (new File(workflowStep.getHtmlResultFilePath()).exists()){
						return new String(Files.readAllBytes(Paths.get(workflowStep.getHtmlResultFilePath())));
					}
				}
				throw new Exception("File " + workflowStep.getHtmlResultFilePath() + " does not exit");
			}
			throw new Exception("WorkflowStep with id " + workflowStepId + " does not exit");

		} catch (Exception e) {
			return this.logError(methodName, e, start, logger).toString();
		}
	}
	
	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/workflow/step/{workflowStepId}/result")
	@Produces({ "text/html" })
	public Response getWorkflowStepResultById(
			@PathParam("token") String token,
			@PathParam("proposal") String proposal, 
			@PathParam("workflowStepId") int workflowStepId) {

		String methodName = "getWorkflowStepResultById";
		long start = this.logInit(methodName, logger, token, proposal, workflowStepId);
		try {
			WorkflowStep3VO workflowStep = this.getWorkflowStep3Service().findById(workflowStepId);
			this.logFinish(methodName, start, logger);
			if (workflowStep != null){
				if (workflowStep.getResultFilePath() != null){
					if (new File(workflowStep.getResultFilePath()).exists()){
						byte[] encoded = Files.readAllBytes(Paths.get(workflowStep.getResultFilePath()));
						return this.sendResponse(new String(encoded));
					}
				}
				throw new Exception("File " + workflowStep.getResultFilePath() + " does not exit");
			}
			throw new Exception("WorkflowStep with id " + workflowStepId + " does not exit");

		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
		

}
