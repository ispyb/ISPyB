package ispyb.ws.rest.proposal;

import ispyb.server.common.services.ws.rest.session.SessionService;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.login.Login3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.ws.rest.RestWebService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.jboss.resteasy.annotations.GZIP;

@Path("/")
public class SessionRestWebService extends RestWebService {
	 private final static Logger logger = Logger.getLogger(SessionRestWebService.class);
	
	@RolesAllowed({"User", "Manager", "Localcontact"})
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/session/list")
	@Produces({ "application/json" })
	public Response getSessionByProposalId(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal) throws Exception {
		
		String methodName = "getSessionList";
		long id = this.logInit(methodName, logger, token, proposal);
		try{
			List<Map<String, Object>> result = getSessionService().getSessionViewByProposalId(this.getProposalId(proposal));
			this.logFinish(methodName, id, logger);
			return sendResponse(result );
		}
		catch(Exception e){
			return this.logError(methodName, e, id, logger);
		}
	}
	
	@RolesAllowed({"User", "Manager", "Localcontact"})
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/session/{sessionId}/list")
	@Produces({ "application/json" })
	public Response getSessionById(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("sessionId") int sessionId) throws Exception {
		
		String methodName = "getSessionById";
		long id = this.logInit(methodName, logger, token, proposal, sessionId);
		try{
			List<Map<String, Object>> result = null;
			if (isProposalnameMatchingToken(token, proposal)) {
				result = getSessionService().getSessionViewBySessionId(this.getProposalId(proposal), sessionId);			
			} else {
				unauthorizedResponse();
			}
			this.logFinish(methodName, id, logger);
			return sendResponse(result );
		}
		catch(Exception e){
			return this.logError(methodName, e, id, logger);
		}
	}
	
	
	private SessionService getSessionService() throws NamingException {
		return (SessionService) Ejb3ServiceLocator.getInstance().getLocalService(SessionService.class);
	}
	
	@RolesAllowed({"Manager", "Localcontact"})
	@GET
	@GZIP
	@Path("{token}/proposal/session/{startdate}/{enddate}/list")
	@Produces({ "application/json" })
	public Response getSessionsByDate(
			@PathParam("token") String token, 
			@PathParam("startdate") String start,
			@PathParam("enddate") String end,
			@QueryParam("beamline") String beamline) throws Exception {
		
		String methodName = "getSessionsByDate";
		long id = this.logInit(methodName, logger, token, start, end, beamline);
		try{
			List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
			/*Date startDate = null; 
			Date endDate = null; 
			if (start != null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				startDate = sdf.parse(start);
			}
			if (end != null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				endDate = sdf.parse(end);
			}		
			List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
			
			
			if (beamline != null){
				result = getSessionService().getSessionViewByDates(startDate, endDate, beamline);
			}
			else{*/
				result = getSessionService().getSessionViewByDates(start, end);
//			}
			this.logFinish(methodName, id, logger);
			return sendResponse(result);
		}
		catch(Exception e){
			return this.logError(methodName, e, id, logger);
		}
	}
	
}
