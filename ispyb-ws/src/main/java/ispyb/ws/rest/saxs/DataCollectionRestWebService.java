package ispyb.ws.rest.saxs;

import ispyb.ws.rest.RestWebService;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/")
public class DataCollectionRestWebService extends RestWebService {
	
	private final static Logger log = Logger.getLogger(DataCollectionRestWebService.class);
	
	    
	@PermitAll
	@GET
	@Path("{cookie}/proposal/{proposal}/saxs/datacollection/list")
	@Produces({ "application/json" })
	public Response list(
			@PathParam("cookie") String cookie, 
			@PathParam("proposal") String proposal) throws Exception {
		return sendResponse(this.getAnalysis3Service().getCompactAnalysisByProposalId(getProposalId(proposal), 10000));
	}
	
	@PermitAll
	@GET
	@Path("{cookie}/proposal/{proposal}/saxs/datacollection/{datacollectionIdList}/list")
	@Produces({ "application/json" })
	public Response getDc(
			@PathParam("cookie") String cookie, 
			@PathParam("proposal") String proposal,
			@PathParam("datacollectionIdList") String datacollectionIdList) throws Exception {
		return sendResponse(this.getAnalysis3Service().getDataCollections(this.parseToInteger(datacollectionIdList)));
	}

	@PermitAll
	@GET
	@Path("{cookie}/proposal/{proposal}/saxs/datacollection/{key}/{value}/list")
	@Produces({ "application/json" })
	public Response list(
			@PathParam("cookie") String cookie, 
			@PathParam("proposal") String proposal,
			@PathParam("key") String key,
			@PathParam("value") String value) throws Exception {
		
		List<String> dataCollectionIdList = this.parseToString(value);
		return sendResponse(filter(this.getAnalysis3Service().getCompactAnalysisByProposalId(getProposalId(proposal), 10000), key, dataCollectionIdList));
	}
	
}
