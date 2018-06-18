package ispyb.ws.rest.em;

import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.em.services.collections.EM3Service;
import ispyb.ws.rest.RestWebService;
import ispyb.ws.rest.mx.MXRestWebService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.GZIP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
public class StatsWebService extends MXRestWebService {

	protected Logger log = LoggerFactory.getLogger(MovieRestWebService.class);

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private EM3Service getEMService() throws NamingException {
		return (EM3Service) ejb3ServiceLocator.getLocalService(EM3Service.class);

	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/em/datacollection/{dataCollectionIds}/stats")
	@Produces({ "application/json" })
	public Response getStatsByDataCollectionIds(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("dataCollectionIds") String dataCollectionIds) throws Exception {

		log.info("getStatsByDataCollectionIds. technique=EM proposal={} dataCollectionIds={}", proposal, dataCollectionIds);
		List<Integer> dataCollectionIdList = this.parseToInteger(dataCollectionIds);
		List<Map<String, Object>> result = getEMService().getStatsByDataCollectionIds(this.getProposalId(proposal), dataCollectionIds);

		return this.sendResponse(result);

	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/em/datacollection/session/{sessionIdList}/list")
	@Produces({ "application/json" })
	public Response getViewDataCollectionBySessionId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("sessionIdList") String sessionIdList) {

		try {
			List<Integer> ids = this.parseToInteger(sessionIdList);
			List<Map<String, Object>> dataCollections = new ArrayList<Map<String, Object>>();

			for (Integer id : ids) {
				List<Map<String, Object>> result = this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionBySessionId(this.getProposalId(proposal), id);
				result.get(0).put("stats", getEMService().getStatsByDataSessionIds(this.getProposalId(proposal), id));
				dataCollections.addAll(result);
			}
			return this.sendResponse(dataCollections, false);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}
	
}
