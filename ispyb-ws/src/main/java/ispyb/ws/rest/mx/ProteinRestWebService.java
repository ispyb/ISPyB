package ispyb.ws.rest.mx;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import ispyb.server.mx.vos.sample.Protein3VO;

@Path("/")
public class ProteinRestWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(ProteinRestWebService.class);

	
	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/protein/list")
	@Produces({ "application/json" })
	public Response getProteinByProposalId(@PathParam("token") String token,
			@PathParam("proposal") String proposal) {

		String methodName = "getProteinByProposalId";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<Protein3VO> proteins = this.getProtein3Service().findByProposalId(this.getProposalId(proposal));
			return this.sendResponse(proteins);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
		

}
