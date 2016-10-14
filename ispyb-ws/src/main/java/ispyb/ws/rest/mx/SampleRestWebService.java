package ispyb.ws.rest.mx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.services.ws.rest.sample.SampleRestWsService;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.SampleInfo;

@Path("/")
public class SampleRestWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(SampleRestWebService.class);

	@RolesAllowed({ "User", "Manager", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/sample/crystalId/{crystalId}/list")
	@Produces({ "application/json" })
	public Response getSamplesByCrystalId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("crystalId") String crystalId) {

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
	public Response getSampleInfoByCrystalId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("crystalId") String crystalId) {

		String methodName = "getSampleInfoByCrystalId";
		long start = this.logInit(methodName, logger, token, proposal);
		try {

			SampleInfo[] samples = this.getBLSample3Service().findForWSSampleInfoLight(null, new Integer(crystalId), null, null);
			this.logFinish(methodName, start, logger);
			List<SampleInfo> sampleinfo = Arrays.asList(samples);
			return this.sendResponse(sampleinfo);
		}
		catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@Deprecated
	@RolesAllowed({ "User", "Manager", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/sampleinfo/list")
	@Produces({ "application/json" })
	public Response getSampleInfoByProposalId(@PathParam("token") String token, @PathParam("proposal") String proposal) {
		
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
	
	
	protected SampleRestWsService getSampleRestWsService() throws NamingException {
		return (SampleRestWsService) Ejb3ServiceLocator.getInstance().getLocalService(SampleRestWsService.class);
	}
	
	
	@RolesAllowed({ "User", "Manager", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/sample/list")
	@Produces({ "application/json" })
	public Response getSampleByProposalId(@PathParam("token") String token, @PathParam("proposal") String proposal) {
		
		String methodName = "getSampleByProposalId";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<Map<String, Object>> samples = this.getSampleRestWsService().getSamplesByProposalId(this.getProposalId(proposal));
			this.logFinish(methodName, start, logger);
			return this.sendResponse(samples);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	
	@RolesAllowed({ "User", "Manager", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/sample/dewarid/{dewarid}/list")
	@Produces({ "application/json" })
	public Response getSamplesByDewarId(@PathParam("token") String token, @PathParam("proposal") String proposal, @PathParam("dewarId") int dewarId) {
		String methodName = "getSamplesByDewarId";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<Map<String, Object>> samples = this.getSampleRestWsService().getSamplesByDewarId(this.getProposalId(proposal), dewarId);
			this.logFinish(methodName, start, logger);
			return this.sendResponse(samples);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({ "User", "Manager", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/sample/containerid/{containerid}/list")
	@Produces({ "application/json" })
	public Response getSamplesByContainerId(@PathParam("token") String token, @PathParam("proposal") String proposal, @PathParam("containerid") String containerid) {
		String methodName = "getSamplesByContainerId";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<Integer> ids = this.parseToInteger(containerid);
			List<Map<String, Object>> samples = new ArrayList<Map<String,Object>>();
			for (Integer id : ids) {
				samples.addAll(this.getSampleRestWsService().getSamplesByContainerId(this.getProposalId(proposal), id));	
			}
			
			this.logFinish(methodName, start, logger);
			return this.sendResponse(samples);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({ "User", "Manager", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/sample/sessionid/{sessionid}/list")
	@Produces({ "application/json" })
	public Response getSamplesBySessionId(@PathParam("token") String token, @PathParam("proposal") String proposal, @PathParam("sessionid") int sessionid) {
		String methodName = "getSamplesBySessionId";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<Map<String, Object>> samples = this.getSampleRestWsService().getSamplesBySessionId(this.getProposalId(proposal), sessionid);
			this.logFinish(methodName, start, logger);
			return this.sendResponse(samples);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({ "User", "Manager", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/sample/shipmentid/{shipmentid}/list")
	@Produces({ "application/json" })
	public Response getSamplesByShipmentId(@PathParam("token") String token, @PathParam("proposal") String proposal, @PathParam("shipmentid") int shipmentid) {
		String methodName = "getSamplesByShipmentId";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<Map<String, Object>> samples = this.getSampleRestWsService().getSamplesByShipmentId(this.getProposalId(proposal), shipmentid);
			this.logFinish(methodName, start, logger);
			return this.sendResponse(samples);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	

}
