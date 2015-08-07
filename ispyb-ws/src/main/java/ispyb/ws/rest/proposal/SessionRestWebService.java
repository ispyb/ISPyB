package ispyb.ws.rest.proposal;

import ispyb.server.mx.vos.collections.Session3VO;
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
public class SessionRestWebService extends RestWebService {
	 private final static Logger logger = Logger.getLogger(SessionRestWebService.class);
	
	@PermitAll
	@GET
	@Path("{token}/proposal/{proposal}/session/list")
	@Produces({ "application/json" })
	public Response getSessionList(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal) throws Exception {
		
		String methodName = "getSessionList";
		long id = this.logInit(methodName, logger, token, proposal);
		try{
			List<Session3VO> result = getSession3Service().findFiltered(this.getProposalId(proposal), null, null, null, null, null, false, null);
			this.logFinish(methodName, id, logger);
			return sendResponse(result );
		}
		catch(Exception e){
			return this.logError(methodName, e, id, logger);
		}
	}

	
}
