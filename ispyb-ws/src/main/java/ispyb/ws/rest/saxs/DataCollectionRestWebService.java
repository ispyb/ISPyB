package ispyb.ws.rest.saxs;

import ispyb.server.biosaxs.vos.datacollection.SaxsDataCollection3VO;
import ispyb.ws.rest.RestWebService;

import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/")
public class DataCollectionRestWebService extends RestWebService {

	private final static Logger logger = Logger.getLogger(DataCollectionRestWebService.class);

	@PermitAll
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

	@PermitAll
	@GET
	@Path("{token}/proposal/{proposal}/saxs/datacollection/{datacollectionIdList}/list")
	@Produces({ "application/json" })
	public Response getDataCollections(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("datacollectionIdList") String datacollectionIdList) throws Exception {

		String methodName = "getDataCollections";
		long id = this.logInit(methodName, logger, token, proposal);
		try {
			List<SaxsDataCollection3VO> result = this.getAnalysis3Service().getDataCollections(
					this.parseToInteger(datacollectionIdList));
			this.logFinish(methodName, id, logger);
			return sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}

	}

	@PermitAll
	@GET
	@Path("{token}/proposal/{proposal}/saxs/datacollection/{key}/{value}/list")
	@Produces({ "application/json" })
	public Response list(@PathParam("token") String token, @PathParam("proposal") String proposal,
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

}
