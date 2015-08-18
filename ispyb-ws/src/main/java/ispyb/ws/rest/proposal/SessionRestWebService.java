package ispyb.ws.rest.proposal;

import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.ws.rest.RestWebService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/")
public class SessionRestWebService extends RestWebService {
	 private final static Logger logger = Logger.getLogger(SessionRestWebService.class);
	
	@RolesAllowed({"User", "Manager", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/session/list")
	@Produces({ "application/json" })
	public Response getSessionList(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@QueryParam("startdate") Date startDate,
			@QueryParam("enddate") Date enddate,
			@QueryParam("beamline") String beamline) throws Exception {
		
		String methodName = "getSessionList";
		long id = this.logInit(methodName, logger, token, proposal);
		try{
			List<Session3VO> result = getSession3Service().findFiltered(this.getProposalId(proposal), null, beamline, null, startDate, enddate, false, null);
			this.logFinish(methodName, id, logger);
			return sendResponse(result );
		}
		catch(Exception e){
			return this.logError(methodName, e, id, logger);
		}
	}
	
	@RolesAllowed({"Manager", "LocalContact"})
	@GET
	@Path("{token}/proposal/session/list")
	@Produces({ "application/json" })
	public Response getSessionManagerList(
			@PathParam("token") String token, 
			@QueryParam("startdate") String start,
			@QueryParam("enddate") String end,
			@QueryParam("beamline") String beamline) throws Exception {
		
		String methodName = "getSessionList";
		long id = this.logInit(methodName, logger, token, start, end, beamline);
		try{
			Date startDate = null; 
			Date enddate = null; 
			if (start != null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				startDate = sdf.parse(start);
			}
			if (end != null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				enddate = sdf.parse(end);
			}
			
			List<Session3VO> result = getSession3Service().findFiltered(null, beamline, null, enddate, startDate, false, null);
			this.logFinish(methodName, id, logger);
			return sendResponse(result );
		}
		catch(Exception e){
			return this.logError(methodName, e, id, logger);
		}
	}

	
}
