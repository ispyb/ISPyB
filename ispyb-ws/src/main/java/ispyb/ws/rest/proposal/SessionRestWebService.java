package ispyb.ws.rest.proposal;

import ispyb.ws.rest.RestWebService;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/")
public class SessionRestWebService extends RestWebService {
	
	
	@PermitAll
	@GET
	@Path("{cookie}/proposal/{proposal}/session/list")
	@Produces({ "application/json" })
	public Response list(
			@PathParam("cookie") String cookie, 
			@PathParam("proposal") String proposal) throws Exception {
		return sendResponse( getSession3Service().findFiltered(this.getProposalId(proposal), null, null, null, null, null, false, null));
	}
}
