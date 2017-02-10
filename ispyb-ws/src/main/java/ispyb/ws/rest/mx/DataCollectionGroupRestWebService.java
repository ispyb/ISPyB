package ispyb.ws.rest.mx;

import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/")
public class DataCollectionGroupRestWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(DataCollectionGroupRestWebService.class);


	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@POST
	@Path("{token}/proposal/{proposal}/mx/datacollectiongroup/{dataCollectionGroupId}/comments/save")
	@Produces("image/png")
	public Response saveDataCollectionComments(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("dataCollectionGroupId") int dataCollectionGroupId,
			@FormParam("comments") String comments) {
		
		String methodName = "saveDataCollectionComments";
		long id = this.logInit(methodName, logger, token, proposal, dataCollectionGroupId, comments);
		
		try {
			DataCollectionGroup3VO dataCollectionGroup = this.getDataCollectionGroup3Service().findByPk(dataCollectionGroupId, false, false);
			
			dataCollectionGroup.setComments(comments);
			this.getDataCollectionGroup3Service().update(dataCollectionGroup);

		} catch (Exception e) {
			e.printStackTrace();
			return this.logError(methodName, e, id, logger);
		}
		return this.sendResponse(true);
	}

}
