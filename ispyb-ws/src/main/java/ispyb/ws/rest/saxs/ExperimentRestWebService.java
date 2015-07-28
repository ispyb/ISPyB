package ispyb.ws.rest.saxs;

import ispyb.server.biosaxs.services.ExperimentSerializer;
import ispyb.server.biosaxs.services.core.ExperimentScope;
import ispyb.server.biosaxs.services.core.analysis.Analysis3Service;
import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.utils.comparator.SaxsDataCollectionComparator;
import ispyb.server.common.hdf5.HDF5FileReader;
import ispyb.server.common.util.LoggerFormatter;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.ws.rest.RestWebService;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Path("/")
public class ExperimentRestWebService extends RestWebService {
    	private final static Logger LOGGER = Logger.getLogger(BufferRestWebService.class);
    	
	private List<Map<String, Object>> getExperimentListByProposal(String proposal) throws NamingException{
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		SaxsProposal3Service saxsProposalService = (SaxsProposal3Service) ejb3ServiceLocator.getLocalService(SaxsProposal3Service.class);
		List<Proposal3VO> proposals = saxsProposalService.findProposalByLoginName(proposal);

		Analysis3Service analysis3Service = (Analysis3Service) ejb3ServiceLocator.getLocalService(Analysis3Service.class);
		List<Map<String, Object>>  experiments = new ArrayList<Map<String, Object>>();
		
		for (Proposal3VO proposal3vo : proposals) {
			experiments.addAll(analysis3Service.getExperimentListByProposalId(proposal3vo.getProposalId()));
		}
		return experiments;
		
	}
	
	private List<Map<String, Object>> getExperimentListBySessionId(
			String proposal, int sessionId) throws NamingException {
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		SaxsProposal3Service saxsProposalService = (SaxsProposal3Service) ejb3ServiceLocator.getLocalService(SaxsProposal3Service.class);
		List<Proposal3VO> proposals = saxsProposalService.findProposalByLoginName(proposal);

		Analysis3Service analysis3Service = (Analysis3Service) ejb3ServiceLocator.getLocalService(Analysis3Service.class);
		List<Map<String, Object>>  experiments = new ArrayList<Map<String, Object>>();
		
		for (Proposal3VO proposal3vo : proposals) {
			experiments.addAll(analysis3Service.getExperimentListBySessionId(proposal3vo.getProposalId(), sessionId));
		}
		return experiments;
	}
	
	
	private List<Map<String, Object>> getExperimentListExperimentId(
			String proposal, int sessionId) throws NamingException {
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		SaxsProposal3Service saxsProposalService = (SaxsProposal3Service) ejb3ServiceLocator.getLocalService(SaxsProposal3Service.class);
		List<Proposal3VO> proposals = saxsProposalService.findProposalByLoginName(proposal);

		Analysis3Service analysis3Service = (Analysis3Service) ejb3ServiceLocator.getLocalService(Analysis3Service.class);
		List<Map<String, Object>>  experiments = new ArrayList<Map<String, Object>>();
		
		for (Proposal3VO proposal3vo : proposals) {
			experiments.addAll(analysis3Service.getExperimentListByExperimentId(proposal3vo.getProposalId(), sessionId));
		}
		return experiments;
	}
	
	
	
	@PermitAll
	@GET
	@Path("{cookie}/proposal/{proposalId}/saxs/experiment/list")
	@Produces({ "application/json" })
	public Response list(
			@PathParam("cookie") String cookie, 
			@PathParam("proposalId") String proposalId) throws Exception {
		return sendResponse(getExperimentListByProposal(proposalId));
	}
	
	@PermitAll
	@GET
	@Path("{cookie}/proposal/{proposalId}/saxs/experiment/{experimentId}/get")
	@Produces({ "application/json" })
	public Response getExperimentById(
			@PathParam("cookie") String cookie, 
			@PathParam("proposalId") String proposalId,
			@PathParam("experimentId") int experimentId) throws Exception {
	    
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("cookie", String.valueOf(cookie));
		params.put("proposal", String.valueOf(proposalId));
		params.put("experimentId", String.valueOf(experimentId));
		long start = this.logInit("getExperimentById", new Gson().toJson(params), LOGGER);
		try {
		    
        	    	Experiment3VO experiment = this.getExperiment3Service().findById(experimentId, ExperimentScope.MEDIUM, this.getProposalId(proposalId));
        	    	List<Experiment3VO> list = new ArrayList<Experiment3VO>();
        	    	list.add(experiment);
        	    	this.logFinish("getExperimentById", start, LOGGER);
        		return sendResponse(ExperimentSerializer.serialize(list, ExperimentScope.MEDIUM));
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "getExperimentById", start, System.currentTimeMillis(),
					e.getMessage(), e);
		}
		return null;
	}
	
	@PermitAll
	@GET
	@Path("{cookie}/proposal/{proposalId}/saxs/experiment/{experimentId}/samplechanger/type/{type}/template")
	@Produces({ "application/json" })
	public Response getTemplateSourceFile(
			@PathParam("cookie") String cookie, 
			@PathParam("proposalId") String proposalId,
			@PathParam("type") String type,
			@PathParam("experimentId") int experimentId) throws Exception {
	    
	    	HashMap<String, String> params = new HashMap<String, String>();
		params.put("cookie", String.valueOf(cookie));
		params.put("proposal", String.valueOf(proposalId));
		params.put("type", String.valueOf(type));
		long start = this.logInit("getTemplateSourceFile", new Gson().toJson(params), LOGGER);
		try {
	    		String templateFile = this.getExperiment3Service().toRobotXML(experimentId, this.getProposalId(proposalId));
	    		this.logFinish("getTemplateSourceFile", start, LOGGER);
	    		return Response.ok(templateFile).header("Access-Control-Allow-Origin", "*")
	    			.header("Content-Disposition", "attachment;filename=template.xml")
	    			.type("text/plain")
				.build();
			
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "getTemplateSourceFile", start, System.currentTimeMillis(),
					e.getMessage(), e);
		}
		return null;
	}
	
	private Experiment3VO saveExperiment(int proposalId, String name, ArrayList<HashMap<String, String>> samples ) throws NamingException, Exception{
	    	Boolean optimize = false;
		// TODO replace by correct sessionId
		Integer sessionId = null;
		Experiment3VO experiment = this.getRobot3Service().createExperimentFromRobotParams(samples, sessionId, proposalId,
				"BeforeAndAfter", "0", "0", "TEMPLATE", null, name, optimize);
		return this.getWebUserInterfaceService().setPriorities(experiment.getExperimentId(), proposalId,
				SaxsDataCollectionComparator.defaultComparator);
	}
	
		
	@PermitAll
	@POST
	@Path("{cookie}/proposal/{proposal}/saxs/experiment/save")
	@Produces({ "application/json" })
	public Response saveExperiment(
			@PathParam("cookie") String cookie, 
			@PathParam("proposal") String proposal,
			@FormParam("name") String name,
			@FormParam("comments") String comments,
			@FormParam("experimentId") Integer experimentId,
			@FormParam("measurements") String measurements
			) throws Exception {
	    
	    
	    	HashMap<String, String> params = new HashMap<String, String>();
		params.put("cookie", String.valueOf(cookie));
		params.put("proposal", String.valueOf(proposal));
		params.put("name", String.valueOf(name));
		params.put("comments", String.valueOf(comments));
		params.put("experimentId", String.valueOf(experimentId));
		params.put("measurements", String.valueOf(measurements));
		
		long start = this.logInit("saveExperiment", new Gson().toJson(params), LOGGER);
		try {
		    	Integer proposalId = this.getProposalId(proposal);

			Gson gson = new Gson();
			Type mapType = new TypeToken<ArrayList<HashMap<String, String>>>() {
			}.getType();
			ArrayList<HashMap<String, String>> samples = gson.fromJson(measurements, mapType);

			Experiment3VO experiment = null;
			if (experimentId == null) {
				experiment = this.saveExperiment(proposalId, name, samples);
			} else {
				experiment = getRobot3Service().addMeasurementsToExperiment(experimentId, proposalId, samples);
			}
			String json = ExperimentSerializer.serializeExperiment(experiment, ExperimentScope.MEDIUM);
			this.logFinish("saveExperiment", start, LOGGER);
			return this.sendResponse(json);

		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "saveExperiment", start, System.currentTimeMillis(),
					e.getMessage(), e);
		}
		return null;
	}
	
	@PermitAll
	@POST
	@Path("{cookie}/proposal/{proposal}/saxs/experiment/{experimentId}/save")
	@Produces({ "application/json" })
	public Response saveExperiment(
			@PathParam("cookie") String cookie, 
			@PathParam("proposal") String proposal,
			@PathParam("experimentId") int experimentId,
			@FormParam("name") String name,
			@FormParam("comments") String comments
			) throws Exception {
	    
	    
	    	HashMap<String, String> params = new HashMap<String, String>();
		params.put("cookie", String.valueOf(cookie));
		params.put("proposal", String.valueOf(proposal));
		params.put("name", String.valueOf(name));
		params.put("comments", String.valueOf(comments));
		params.put("experimentId", String.valueOf(experimentId));
		
		long start = this.logInit("saveExperiment", new Gson().toJson(params), LOGGER);
		try {
		    	Experiment3VO experiment = getExperiment3Service().findById(experimentId, ExperimentScope.MINIMAL);
		    	experiment.setName(name);
		    	experiment.setComments(comments);
		    	experiment = getExperiment3Service().merge(experiment);
		    	String json = ExperimentSerializer.serializeExperiment(experiment, ExperimentScope.MINIMAL);
			this.logFinish("saveExperiment", start, LOGGER);
			return this.sendResponse(json);
			
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "saveExperiment", start, System.currentTimeMillis(),
					e.getMessage(), e);
		}
		return null;
	}
	
	@PermitAll
	@GET
	@Path("{cookie}/proposal/{proposal}/saxs/experiment/session/{sessionId}/list")
	@Produces({ "application/json" })
	public Response list(
			@PathParam("cookie") String cookie, 
			@PathParam("proposal") String proposal,
			@PathParam("sessionId") int sessionId) throws Exception {
		return sendResponse(getExperimentListBySessionId(proposal, sessionId));
	}
	
	@PermitAll
	@GET
	@Path("{cookie}/proposal/{proposal}/saxs/experiment/{key}/{value}/list")
	@Produces({ "application/json" })
	public Response list(
			@PathParam("cookie") String cookie, 
			@PathParam("proposal") String proposal,
			@PathParam("key") String key,
			@PathParam("value") String value) throws Exception {
		return sendResponse(filter(getExperimentListByProposal(proposal), key, value));
	}
	
	
	private String getH5FilePathByExperimentId(Integer experimentId, Integer proposalId) throws NamingException {
		Experiment3VO experiment = this.getExperiment3Service().findById(experimentId, ExperimentScope.MINIMAL, proposalId);
		if (experiment != null) {
			return experiment.getDataAcquisitionFilePath();
		}
		return null;
	}
	
	@PermitAll
	@GET
	@Path("{cookie}/proposal/{proposalId}/saxs/experiment/{experimentId}/hplc/overview")
	@Produces("text/plain")
	public Response overview(
			@PathParam("cookie") String cookie, 
			@PathParam("proposalId") String proposal, 
			@PathParam("experimentId") int experimentId) throws Exception {
		try {
			String params = ("I0,I0_Stdev,sum_I,Rg,Rg_Stdev,Vc,Vc_Stdev,Qr,Qr_Stdev,mass,mass_Stdev,quality");
			List<String> parameters =  Arrays.asList(params.split(","));
			int proposalId = this.getProposalId(proposal);
			String filePath = getH5FilePathByExperimentId(experimentId, proposalId);
			if (filePath != null){
				HDF5FileReader reader = new HDF5FileReader(filePath);
				return this.sendResponse(reader.getH5ParametersByExperimentId(parameters));
			}
			
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return null;
	}
	
	private String get(String y, String error, String operation) {
		if (Float.valueOf(y) == 0)
			return y;
		if (operation.equals("log")){
			double minus = Math.log(Math.abs(Float.valueOf(y) - Float.valueOf(error)));
			return String.valueOf((float)(Math.log(Float.valueOf(y)) - minus));
		}
		return error;
	}
	
	private String get(String point, String operation){
		if (Float.valueOf(point) == 0)
			return point;
		if (operation.equals("log")){
			return String.valueOf((float)Math.log(Float.valueOf(point)));
		}
		return point;
	}
	
	private String getLine(List<Integer> ids, HashMap<Integer, HashMap<String, ArrayList<String>>> result, int index, String operation){
		StringBuilder sb = new StringBuilder();
		if (ids.size() > 0){
			/** Getting q **/
			String q = result.get(ids.get(0)).get("q").get(index);
			sb.append(q).append(",");
			for (Integer frameNumber : ids) {
				sb.append(get(result.get(frameNumber).get("scattering_I").get(index), operation));
				sb.append(",");
				sb.append(get(result.get(frameNumber).get("scattering_I").get(index), result.get(frameNumber).get("scattering_Stdev").get(index), operation));
				sb.append(",");
				sb.append(get(result.get(frameNumber).get("subtracted_I").get(index), operation));
				sb.append(",");
				sb.append(get(result.get(frameNumber).get("subtracted_I").get(index), result.get(frameNumber).get("subtracted_Stdev").get(index), operation));
				sb.append(",");
				sb.append(get(result.get(frameNumber).get("buffer_I").get(index), operation));
				sb.append(",");
				sb.append(get(result.get(frameNumber).get("buffer_I").get(index), result.get(frameNumber).get("buffer_Stdev").get(index), operation));
				sb.append(",");
			}
		}
		/** Removing last , **/
		sb = sb.deleteCharAt(sb.lastIndexOf(","));
		return sb.append("\n").toString();
		
	}
	
	

	@PermitAll
	@GET
	@Path("{cookie}/proposal/{proposalId}/saxs/experiment/{experimentId}/hplc/frame/{frameId}/get")
	@Produces("text/plain")
	public Response getFrame(
			@PathParam("cookie") String cookie,
			@PathParam("proposalId") String proposalId,
			@PathParam("experimentId") int experimentId,
			@PathParam("frameId") String frameNumberList,
			@QueryParam("operation") String operation 
			) throws Exception {
		
		try {
			
			String filePath = this.getH5FilePathByExperimentId(experimentId, this.getProposalId(proposalId));
			HDF5FileReader reader = new HDF5FileReader(filePath);
			boolean includeSubtraction = true;
			boolean includeBufferAverage = true;
			
			List<Integer> ids = this.parseToInteger(frameNumberList);
			HashMap<Integer, HashMap<String, ArrayList<String>>> result = new HashMap<Integer, HashMap<String, ArrayList<String>>>();
			
			int pointsCount = 0;
			for (Integer id : ids) {
				result.put(id, reader.getH5FrameScattering(id, includeSubtraction, includeBufferAverage));
				pointsCount = result.get(id).get("q").size();
			}

			
			StringBuilder sb = new StringBuilder();
			sb.append("q,");
			for (Integer id : ids) {
				String key = "#" + id;
				sb.append(key).append(",");
				sb.append(key + "_sub").append(",");
				sb.append(key + "_buffer_ave").append(",");
			}
			/** Removing last , **/
			sb = sb.deleteCharAt(sb.lastIndexOf(","));
			sb.append("\n");
			
			if (operation == null){
				operation = "None";
			}
			for (int i = 0; i < pointsCount; i++) {
//				sb.append(q.get(i)).append(",");
//				sb.append(get(scattering_I.get(i), operation)).append(",");
//				sb.append(scattering_Stdev.get(i)).append(",");
//				sb.append(get(subtracted_I.get(i), operation)).append(",");
//				sb.append(subtracted_Stdev.get(i)).append(",");
//				sb.append(get(buffer_I.get(i), operation)).append(",");
//				sb.append(buffer_Stdev.get(i));
//				sb.append("\n");
				sb.append(getLine(ids, result, i, operation));
			}
			
			
			
			return this.sendResponse(sb.toString());
			
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return null;
	}

}
