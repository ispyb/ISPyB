package ispyb.ws.rest.proposal;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.json.Json;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import ispyb.common.util.IspybFileUtils;
import ispyb.server.smis.UpdateFromSMIS;
import ispyb.ws.rest.RestWebService;

@Path("/")
public class UserPortalRestWebService extends RestWebService{

	private final static Logger logger = Logger.getLogger(UserPortalRestWebService.class);
		
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/userportal/update")
	@Produces({ "application/json" })
	public Response updateProposalFromUserPortal(@PathParam("token") String token, @PathParam("proposal") String login) throws Exception {
		String methodName = "updateProposalFromUserPortal";
		long id = this.logInit(methodName, logger, token);
		try {
			
			UpdateFromSMIS.updateProposalFromJsonFiles(login);
			String results= "success";
			return this.sendResponse(results);

		} catch (Exception e) {
			return this.logError("updateProposalFromUserPortal", e, id, logger);
		}
	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/userportal/filepath/{filepath}/upload")
	@Produces({ "application/json" })
	public Response getJsonFromFilepath(@PathParam("token") String token, @PathParam("proposal") String login, 
			@PathParam("filepath") String filepath) throws Exception {
		String methodName = "getJsonFromFilepath";
		long id = this.logInit(methodName, logger, token);
		try {
			
			byte[] json = IspybFileUtils.getFile(filepath);
			for (int i = 0; i < json.length; i++) {
				
			}
			String results= new String(json);
			return this.sendResponse(results);

		} catch (Exception e) {
			return this.logError("getJsonFromFilepath", e, id, logger);
		}
	}
	


}
