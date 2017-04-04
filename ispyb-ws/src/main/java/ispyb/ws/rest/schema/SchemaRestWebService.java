package ispyb.ws.rest.schema;

import ispyb.ws.rest.RestWebService;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/")
public class SchemaRestWebService extends RestWebService{

	private final static Logger logger = Logger.getLogger(SchemaRestWebService.class);
		
	@PermitAll
	@GET
	@Path("/schema/status")
	@Produces({ "application/json" })
	public Response getScriptsDone() throws Exception {
		String methodName = "getScriptsDone";
		long id = this.logInit(methodName, logger);
		try {
			return this.sendResponse(getSchemaStatusService().findAll());
		} catch (Exception e) {
			return this.logError("getScriptsDone", e, id, logger);
		}
	}
	
//	@RolesAllowed({"Manager"})
//	@GET
//	@Path("{token}/schemastatus/scriptsNotDone/get")
//	@Produces({ "application/json" })
//	public Response getScriptsNotDone(@PathParam("token") String token)
//			throws Exception {
//		long id = this.logInit("scriptsNotDone", logger, token);
//		try {
//			List<String> results = getSchemaStatusService().findScriptsNotDone();
//			this.logFinish("scriptsNotDone", id, logger);
//			return this.sendResponse(results);
//
//		} catch (Exception e) {
//			return this.logError("scriptsNotDone", e, id, logger);
//		}
//	}
}
