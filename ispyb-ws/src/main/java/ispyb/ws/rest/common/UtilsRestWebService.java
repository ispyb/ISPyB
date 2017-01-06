package ispyb.ws.rest.common;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import ispyb.ws.rest.RestWebService;

@Path("/")
public class UtilsRestWebService extends RestWebService{

	private final static Logger logger = Logger.getLogger(UtilsRestWebService.class);
		
	@RolesAllowed({"Manager"})
	@GET
	@Path("{token}/schemastatus/scriptsDone/get")
	@Produces({ "application/json" })
	public Response getScriptsDone(@PathParam("token") String token) throws Exception {
		String methodName = "getScriptsDone";
		long id = this.logInit(methodName, logger, token);
		try {
			
			List<String> results = getSchemaStatusService().findScriptsDone();
			return this.sendResponse(results);

		} catch (Exception e) {
			return this.logError("getScriptsDone", e, id, logger);
		}
	}
	
	@RolesAllowed({"Manager"})
	@GET
	@Path("{token}/schemastatus/scriptsNotDone/get")
	@Produces({ "application/json" })
	public Response getScriptsNotDone(@PathParam("token") String token)
			throws Exception {
		long id = this.logInit("scriptsNotDone", logger, token);
		try {
			List<String> results = getSchemaStatusService().findScriptsNotDone();
			this.logFinish("scriptsNotDone", id, logger);
			return this.sendResponse(results);

		} catch (Exception e) {
			return this.logError("scriptsNotDone", e, id, logger);
		}
	}
}
