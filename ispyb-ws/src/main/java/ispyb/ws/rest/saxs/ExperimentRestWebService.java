package ispyb.ws.rest.saxs;

import ispyb.server.biosaxs.services.ExperimentSerializer;
import ispyb.server.biosaxs.services.core.ExperimentScope;
import ispyb.server.biosaxs.services.core.analysis.Analysis3Service;
import ispyb.server.biosaxs.services.utils.BiosaxsZipper;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.utils.comparator.SaxsDataCollectionComparator;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.test.services.ZipperTest;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
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
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


@Path("/")
public class ExperimentRestWebService extends SaxsRestWebService {
	private final static Logger logger = Logger.getLogger(BufferRestWebService.class);

	private List<Map<String, Object>> getExperimentListByProposal(String proposal) throws Exception {
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		Proposal3Service proposalService = this.getProposal3Service();
		List<Proposal3VO> proposals = proposalService.findProposalByLoginName(proposal);

		Analysis3Service analysis3Service = (Analysis3Service) ejb3ServiceLocator
				.getLocalService(Analysis3Service.class);
		List<Map<String, Object>> experiments = new ArrayList<Map<String, Object>>();

		for (Proposal3VO proposal3vo : proposals) {
			experiments.addAll(analysis3Service.getExperimentListByProposalId(proposal3vo.getProposalId()));
		}
		return experiments;

	}

	private List<Map<String, Object>> getExperimentListBySessionId(String proposal, int sessionId)
			throws Exception {
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		
		Proposal3Service proposalService = this.getProposal3Service();
		List<Proposal3VO> proposals = proposalService.findProposalByLoginName(proposal);

		Analysis3Service analysis3Service = (Analysis3Service) ejb3ServiceLocator
				.getLocalService(Analysis3Service.class);
		List<Map<String, Object>> experiments = new ArrayList<Map<String, Object>>();

		for (Proposal3VO proposal3vo : proposals) {
			experiments.addAll(analysis3Service.getExperimentListBySessionId(proposal3vo.getProposalId(), sessionId));
		}
		return experiments;
	}

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"}) 
	@GET
	@Path("{token}/proposal/{proposalId}/saxs/experiment/list")
	@Produces({ "application/json" })
	public Response list(@PathParam("token") String token, @PathParam("proposalId") String proposalId)
			throws Exception {

		String methodName = "list";
		long id = this.logInit(methodName, logger, token, proposalId);
		try {
			List<Map<String, Object>> result = getExperimentListByProposal(proposalId);
			this.logFinish(methodName, id, logger);
			return sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);

		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposalId}/saxs/experiment/{experimentId}/get")
	@Produces({ "application/json" })
	public Response getExperimentById(@PathParam("token") String token, @PathParam("proposalId") String proposalId,
			@PathParam("experimentId") int experimentId) throws Exception {

		String methodName = "getExperimentById";
		long id = this.logInit(methodName, logger, token, proposalId, experimentId);
		try {
			Experiment3VO experiment = this.getExperiment3Service().findById(experimentId, ExperimentScope.MEDIUM,
					this.getProposalId(proposalId));
			List<Experiment3VO> list = new ArrayList<Experiment3VO>();
			list.add(experiment);
			this.logFinish(methodName, id, logger);
			return sendResponse(ExperimentSerializer.serialize(list, ExperimentScope.MEDIUM));
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
	}
	
	
	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposalId}/saxs/experiment/{experimentId}/download")
	@Produces("application/x-octet-stream")
	public Response downloadExperiment(@PathParam("token") String token, @PathParam("proposalId") String proposalId,
			@PathParam("experimentId") int experimentId) throws Exception {

		String methodName = "downloadExperiment";
		long id = this.logInit(methodName, logger, token, proposalId, experimentId);
		try {
			Experiment3VO experiment = this.getExperiment3Service().findById(experimentId, ExperimentScope.MEDIUM, this.getProposalId(proposalId));
			if (experiment != null){
				BiosaxsZipper zipper = new BiosaxsZipper(this.getAnalysis3Service(), this.getAbInitioModelling3Service(), this.getPrimaryDataProcessing3Service());
				this.logFinish(methodName, id, logger);
				return this.downloadFile(zipper.getFilesByExperimentId(experiment.getExperimentId()), experiment.getName() + ".zip");
			}
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
		return null;
	}

	
	

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposalId}/saxs/experiment/{experimentId}/samplechanger/type/{type}/template")
	@Produces({ "application/json" })
	public Response getTemplateSourceFile(@PathParam("token") String token,
			@PathParam("proposalId") String proposalId, @PathParam("type") String type,
			@PathParam("experimentId") int experimentId) throws Exception {

		String methodName = "getTemplateSourceFile";
		long start = this.logInit("getTemplateSourceFile", logger, token, proposalId, type, experimentId);
		try {
			String templateFile = this.getExperiment3Service().toRobotXML(experimentId, this.getProposalId(proposalId));
			this.logFinish(methodName, start, logger);
			return Response.ok(templateFile).header("Access-Control-Allow-Origin", "*")
					.header("Content-Disposition", "attachment;filename=template.xml").type("text/plain").build();

		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	private Experiment3VO saveExperiment(int proposalId, String name, ArrayList<HashMap<String, String>> samples, String comments)
			throws NamingException, Exception {
		Boolean optimize = false;
		// TODO replace by correct sessionId
		Integer sessionId = null;
		Experiment3VO experiment = this.getRobot3Service().createExperimentFromRobotParams(samples, sessionId,
				proposalId, "BeforeAndAfter", "0", "0", "TEMPLATE", null, name, optimize, comments);
		return this.getWebUserInterfaceService().setPriorities(experiment.getExperimentId(), proposalId,
				SaxsDataCollectionComparator.defaultComparator);
	}

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@POST
	@Path("{token}/proposal/{proposal}/saxs/experiment/save")
	@Produces({ "application/json" })
	public Response createExperiment(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@FormParam("name") String name,
			@FormParam("comments") String comments,
			@FormParam("experimentId") Integer experimentId, 
			@FormParam("measurements") String measurements)
			throws Exception {
		
		String methodName = "createExperiment";
		long start = this.logInit(methodName, logger, token, proposal, name, comments, experimentId, measurements);
		try {
			Integer proposalId = this.getProposalId(proposal);

			Gson gson = new Gson();
			Type mapType = new TypeToken<ArrayList<HashMap<String, String>>>() {
			}.getType();
			ArrayList<HashMap<String, String>> samples = gson.fromJson(measurements, mapType);

			Experiment3VO experiment = null;
			if (experimentId == null) {
				experiment = this.saveExperiment(proposalId, name, samples, comments);
			} else {
				experiment = getRobot3Service().addMeasurementsToExperiment(experimentId, proposalId, samples);
			}
			String json = ExperimentSerializer.serializeExperiment(experiment, ExperimentScope.MEDIUM);
			this.logFinish(methodName, start, logger);
			return this.sendResponse(json);

		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@POST
	@Path("{token}/proposal/{proposal}/saxs/experiment/{experimentId}/save")
	@Produces({ "application/json" })
	public Response saveExperiment(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("experimentId") int experimentId, 
			@FormParam("name") String name,
			@FormParam("comments") String comments) throws Exception {

		String methodName = "saveExperiment";
		long start = this.logInit(methodName, logger, token, proposal, name, comments, experimentId);
		try {
			Experiment3VO experiment = getExperiment3Service().findById(experimentId, ExperimentScope.MINIMAL);
			experiment.setName(name);
			experiment.setComments(comments);
			experiment = getExperiment3Service().merge(experiment);
			String json = ExperimentSerializer.serializeExperiment(experiment, ExperimentScope.MINIMAL);
			this.logFinish(methodName, start, logger);
			return this.sendResponse(json);

		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/experiment/session/{sessionId}/list")
	@Produces({ "application/json" })
	public Response list(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("sessionId") int sessionId) {
		
		String methodName = "list";
		long start = this.logInit(methodName, logger, token, proposal, sessionId);
		try {
			List<Map<String, Object>> result = getExperimentListBySessionId(proposal, sessionId);
			this.logFinish(methodName, start, logger);
			return sendResponse(result);
		}
		catch(Exception e){
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/experiment/{key}/{value}/list")
	@Produces({ "application/json" })
	public Response list(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("key") String key, @PathParam("value") String value) throws Exception {
		
		String methodName = "list";
		long start = this.logInit(methodName, logger, token, proposal, key, value);
		try {
			List<Map<String, Object>> result = filter(getExperimentListByProposal(proposal), key, value);
			this.logFinish(methodName, start, logger);
			return sendResponse(result);
		}
		catch(Exception e){
			return this.logError(methodName, e, start, logger);
		}
		
	}

	private String getH5FilePathByExperimentId(Integer experimentId, Integer proposalId) throws NamingException {
		Experiment3VO experiment = this.getExperiment3Service().findById(experimentId, ExperimentScope.MINIMAL,
				proposalId);
		if (experiment != null) {
			return experiment.getDataAcquisitionFilePath();
		}
		return null;
	}



}
