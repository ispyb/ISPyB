package ispyb.ws.rest.proposal;

import generated.ws.smis.ExpSessionInfoLightVO;
import generated.ws.smis.ProposalParticipantInfoLightVO;
import generated.ws.smis.SampleSheetInfoLightVO;
import ispyb.server.smis.UpdateFromSMIS;
import ispyb.server.userportal.UserPortalUtils;
import ispyb.ws.rest.RestWebService;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/")
public class UserPortalRestWebService extends RestWebService{

	private final static Logger logger = Logger.getLogger(UserPortalRestWebService.class);
		
	/**
	 * This method will update from a JSON file which file path is in the pom.xml
	 */
//	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
//	@GET
//	@Path("{token}/proposal/{proposal}/userportal/update")
//	@Produces({ "application/json" })
//	public Response updateProposalFromUserPortal(@PathParam("token") String token, @PathParam("proposal") String login) throws Exception {
//		String methodName = "updateProposalFromUserPortal";
//		long id = this.logInit(methodName, logger, token);
//		try {
//			
//			UpdateFromSMIS.updateProposalFromJsonFiles(login);
//			String results= "success";
//			return this.sendResponse(results);
//
//		} catch (Exception e) {
//			return this.logError("updateProposalFromUserPortal", e, id, logger);
//		}
//	}
	
	/**
	 * Examples of JSON can be found on src/main/resources/userportal
	 * 
	 * @param proposers JSON 
	 * @param samples JSON
	 * @param sessions JSON
	 * @param labcontacts JSON
	 * @return
	 * @throws Exception
	 */
	@RolesAllowed({"Manager"})
	@POST
	@Path("{token}/userportal/ingest")
	@Produces({ "application/json" })
	public Response updateProposalFromJSON(
			@FormParam("proposers") String proposers, 
			@FormParam("samples") String samples,
			@FormParam("sessions") String sessions,
			@FormParam("labcontacts") String labcontacts)
					throws Exception {
		
		String methodName = "updateProposalFromJSON";
		long id = this.logInit(methodName, logger, proposers,samples,sessions, labcontacts );
		try {
			System.out.println(proposers);
			List<ProposalParticipantInfoLightVO> proposerList = UserPortalUtils.jsonToScientistsList(proposers);
			List<SampleSheetInfoLightVO> sampleList = UserPortalUtils.jsonToSamplesList(samples);
			List<ProposalParticipantInfoLightVO> labcontactList = UserPortalUtils.jsonToScientistsList(labcontacts);
			List<ExpSessionInfoLightVO> sessionList = UserPortalUtils.jsonToSessionsList(sessions);
			Long userPortalPk = (long) 0;
			UpdateFromSMIS.updateThisProposalFromLists(sessionList, proposerList, sampleList, labcontactList, userPortalPk);
			this.logFinish(methodName, id, logger);
		} catch (Exception e) {
			return this.logError("updateProposalFromUserPortal", e, id, logger);
		}
		return null;
	}


	/**
	 * Examples of JSON can be found on src/main/resources/userportal
	 *
	 * @param startDate String
	 * @param endDate String
	 * @return
	 * @throws Exception
	 */
	@RolesAllowed({"Manager"})
	@POST
	@Path("{token}/userportal/updatebydates")
	@Produces({ "application/json" })
	public Response updateProposalByDates(
			@FormParam("startDate") String startDate,
			@FormParam("endDate") String endDate)
			throws Exception {

		String methodName = "updateProposalByDates";
		long id = this.logInit(methodName, logger, startDate, endDate);
		try {
			System.out.println("Updating proposals with a session between " +startDate +" and " +endDate);
			UpdateFromSMIS.updateFromSMIS(startDate, endDate);
			this.logFinish(methodName, id, logger);
		} catch (Exception e) {
			return this.logError("updateProposalFromUserPortal", e, id, logger);
		}
		return null;
	}
	


}
