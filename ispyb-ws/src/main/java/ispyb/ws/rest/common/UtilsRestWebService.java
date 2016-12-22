package ispyb.ws.rest.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import ispyb.server.common.vos.admin.SchemaStatusVO;
import ispyb.ws.rest.RestWebService;

@Path("/")
public class UtilsRestWebService extends RestWebService{

	private final static Logger logger = Logger.getLogger(UtilsRestWebService.class);
	
	//private String pathToScripts = "R:%2Fworkspaces_neon%2Fwks_ispyb%2Fispyb-ejb3%2Fsql_scripts%2Fmysql%2Fupdate_scripts";

	@RolesAllowed({"Manager"})
	@GET
	@Path("{token}/schemastatus/list")
	@Produces({ "application/json" })
	public Response getScriptsDone(@PathParam("token") String token) throws Exception {
		String methodName = "getScriptsDone";
		long id = this.logInit(methodName, logger, token);
		try {
			List<SchemaStatusVO> schemaStatuses = getSchemaStatusService().findAll();
			return this.sendResponse(schemaStatuses);

		} catch (Exception e) {
			return this.logError("getScriptsDone", e, id, logger);
		}
	}
	
	@RolesAllowed({"Manager"})
	@GET
	@Path("{token}/schemastatus/scriptsDone/list")
	@Produces({ "application/json" })
	public Response getScriptsDoneNames(@PathParam("token") String token) throws Exception {
		String methodName = "getScriptsDoneNames";
		long id = this.logInit(methodName, logger, token);
		try {
			List<SchemaStatusVO> schemaStatuses = getSchemaStatusService().findAll();
			List<String> results = this.getScriptNames(schemaStatuses);
			return this.sendResponse(results);

		} catch (Exception e) {
			return this.logError("getScriptsDone", e, id, logger);
		}
	}
	
	@RolesAllowed({"Manager"})
	@GET
	@Path("{token}/schemastatus/scriptsNotDone/pathToScript/{path}/get")
	@Produces({ "application/json" })
	public Response getScriptsNotDone(@PathParam("token") String token, @PathParam("path") String pathToScript)
			throws Exception {
		long id = this.logInit("scriptsNotDone", logger, token);
		try {
			List<String> doneInDB = this.getScriptNames(getSchemaStatusService().findAll());
			List<String> foundInFolder = this.findUpdateScripts(pathToScript);
			foundInFolder.removeAll(doneInDB);
			List<String> results = foundInFolder;
			this.logFinish("scriptsNotDone", id, logger);
			return this.sendResponse(results);

		} catch (Exception e) {
			return this.logError("scriptsNotDone", e, id, logger);
		}
	}
	
	private List<String> findUpdateScripts(String pathToScript) {
		List<String> results = new ArrayList<String>();

		File[] files = new File(pathToScript).listFiles();
		//If this pathname does not denote a directory, then listFiles() returns null. 

		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					results.add(file.getName().replaceAll(".sql", ""));
				}
			}	
		}
		return results;
	}
				
	private List<String> getScriptNames(List<SchemaStatusVO> listVOs){
		
		List<String> names = new ArrayList<String>();
		for (Iterator<SchemaStatusVO> iterator2 = listVOs.iterator(); iterator2.hasNext();) {
			SchemaStatusVO o = (SchemaStatusVO) iterator2.next();
			names.add(o.getScriptName().replaceAll(".sql", ""));
		}
		return names;
	}


}
