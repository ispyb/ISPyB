package ispyb.ws.rest.mx;

import java.util.Arrays;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.SampleInfo;

@Path("/")
public class SampleRestWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(SampleRestWebService.class);
	
	@RolesAllowed({ "User", "Manager", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/sample/crystalId/{crystalId}/list")
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
	
	@RolesAllowed({ "User", "Manager", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/sampleinfo/crystalId/{crystalId}/list")
	@Produces({ "application/json" })
	public Response getSampleInfoByCrystalId(@PathParam("token") String token,
			@PathParam("proposal") String proposal, @PathParam("crystalId") String crystalId) {

		String methodName = "getSampleInfoByCrystalId";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			
			SampleInfo[] samples = this.getBLSample3Service().findForWSSampleInfoLight(null, new Integer(crystalId), null, null);
			this.logFinish(methodName, start, logger);
			List<SampleInfo> sampleinfo = Arrays.asList(samples);
			return this.sendResponse(sampleinfo);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({ "User", "Manager", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/sampleinfo/list")
	@Produces({ "application/json" })
	public Response getSampleInfoByProposalId(@PathParam("token") String token,
			@PathParam("proposal") String proposal) {

		String methodName = "getSampleInfoByProposalId";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			int proposalId = this.getProposalId(proposal);
			SampleInfo[] samples = this.getBLSample3Service().findForWSSampleInfoLight(proposalId, null, null, null);
			this.logFinish(methodName, start, logger);
			return this.sendResponse(samples);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

}
