package ispyb.ws.rest.mx;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import ispyb.server.mx.vos.sample.BLSample3VO;

@Path("/")
public class SampleRestWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(SampleRestWebService.class);

	
	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/sample/{crystalId}/list")
	@Produces({ "application/json" })
	public Response getSamplesByCrystalId(@PathParam("token") String token,
			@PathParam("proposal") String proposal, @PathParam("crystalId") String crystalId) {

		String methodName = "getSamplesByCrystalId";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<BLSample3VO> samples = this.getBLSample3Service().findByCrystalId(new Integer(crystalId));
			this.logFinish(methodName, start, logger);
			return this.sendResponse(samples);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
		

}
