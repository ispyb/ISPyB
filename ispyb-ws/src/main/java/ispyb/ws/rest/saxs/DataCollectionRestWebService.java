package ispyb.ws.rest.saxs;

import ispyb.server.biosaxs.vos.datacollection.SaxsDataCollection3VO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/")
public class DataCollectionRestWebService extends SaxsRestWebService {

	private final static Logger logger = Logger.getLogger(DataCollectionRestWebService.class);

	
	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/datacollection/list")
	@Produces({ "application/json" })
	public Response list(@PathParam("token") String token, @PathParam("proposal") String proposal) throws Exception {
		String methodName = "list";
		long id = this.logInit(methodName, logger, token, proposal);
		try {
			List<Map<String, Object>> result = this.getAnalysis3Service().getCompactAnalysisByProposalId(
					getProposalId(proposal), 10000);
			this.logFinish(methodName, id, logger);
			return sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}

	}

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/datacollection/dataCollectionId/{datacollectionIdList}/bean")
	@Produces({ "application/json" })
	public Response getDataCollections(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("datacollectionIdList") String datacollectionIdList) throws Exception {

		String methodName = "getDataCollections";
		long id = this.logInit(methodName, logger, token, proposal);
		try {
			List<SaxsDataCollection3VO> result = this.getAnalysis3Service().getDataCollections(this.parseToInteger(datacollectionIdList));
			this.logFinish(methodName, id, logger);
			return sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}

	}

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/datacollection/{key}/{value}/list")
	@Produces({ "application/json" })
	public Response listByKey(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("key") String key, @PathParam("value") String value) throws Exception {

		String methodName = "getDataCollections";
		long id = this.logInit(methodName, logger, token, proposal);
		try {
			List<String> dataCollectionIdList = this.parseToString(value);
			List<Map<String, Object>> result = filter(
					this.getAnalysis3Service().getCompactAnalysisByProposalId(getProposalId(proposal), 10000), key,
					dataCollectionIdList);
			this.logFinish(methodName, id, logger);
			return sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}

	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/datacollection/experiment/{experimentId}/list")
	@Produces({ "application/json" })
	public Response getDataCollectionByExperimentId(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("experimentId") Integer experimentId) throws Exception {

		String methodName = "getDataCollectionByExperimentId";
		long id = this.logInit(methodName, logger, token, proposal, experimentId);
		try {
			Collection<? extends Map<String, Object>> result = this.getDataCollectionRestWsService().getDataCollectionByExperimentId(this.getProposalId(proposal), experimentId);
			this.logFinish(methodName, id, logger);
			return sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}

	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/datacollection/macromolecule/{macromoleculeId}/list")
	@Produces({ "application/json" })
	public Response getDataCollectionByMacromoleculeId(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("macromoleculeId") Integer macromoleculeId) throws Exception {

		String methodName = "getDataCollectionByExperimentId";
		long id = this.logInit(methodName, logger, token, proposal, macromoleculeId);
		try {
			Collection<? extends Map<String, Object>> result = this.getDataCollectionRestWsService().getDataCollectionByMacromoleculeId(this.getProposalId(proposal), macromoleculeId);
			this.logFinish(methodName, id, logger);
			return sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}

	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/datacollection/session/{sessionId}/list")
	@Produces({ "application/json" })
	public Response getDataCollectionBySessionId(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("sessionId") Integer sessionId) throws Exception {

		String methodName = "getDataCollectionByExperimentId";
		long id = this.logInit(methodName, logger, token, proposal, sessionId);
		try {
			Collection<? extends Map<String, Object>> result = this.getDataCollectionRestWsService().getDataCollectionBySessionId(this.getProposalId(proposal), sessionId);
			this.logFinish(methodName, id, logger);
			return sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}

	}
	

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/datacollection/{datacollectionId}/list")
	@Produces({ "application/json" })
	public Response getDataCollectionByDataCollectionId(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("datacollectionId") String datacollectionId) throws Exception {

		String methodName = "getDataCollectionByExperimentId";
		long start = this.logInit(methodName, logger, token, proposal, datacollectionId);
		try {
			ArrayList<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			List<Integer> dataCollectionList = this.parseToInteger(datacollectionId);
			for (Integer id : dataCollectionList) {
				result.addAll(this.getDataCollectionRestWsService().getDataCollectionByDataCollectionId(this.getProposalId(proposal), id));
			}
			this.logFinish(methodName, start, logger);
			return sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}

	}
	

}
