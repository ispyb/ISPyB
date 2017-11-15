package ispyb.ws.rest.mx;

import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.ws.soap.em.ToolsForEMDataCollection;

import java.io.File;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.GZIP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
public class DataCollectionGroupRestWebService extends MXRestWebService {


	protected Logger log = LoggerFactory.getLogger(ToolsForEMDataCollection.class);
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@POST
	@Path("{token}/proposal/{proposal}/mx/datacollectiongroup/{dataCollectionGroupId}/comments/save")
	@Produces("image/png")
	public Response saveDataCollectionComments(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("dataCollectionGroupId") int dataCollectionGroupId,
			@FormParam("comments") String comments) {
		
		try {
			DataCollectionGroup3VO dataCollectionGroup = this.getDataCollectionGroup3Service().findByPk(dataCollectionGroupId, false, false);
			dataCollectionGroup.setComments(comments);
			this.getDataCollectionGroup3Service().update(dataCollectionGroup);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.sendResponse(true);
	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/datacollectiongroup/{dataCollectionGroupId}/xtal/thumbnail")
	@Produces("image/png")
	public Response getXtalThumbnailByDataCollectionGroupId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionGroupId") int dataCollectionGroupId) throws Exception {

		DataCollectionGroup3VO dataCollectionGroup = this.getDataCollectionGroup3Service().findByPk(dataCollectionGroupId, false, false);
		if (dataCollectionGroup != null){
			if (new File(dataCollectionGroup.getXtalSnapshotFullPath()).exists()){
				return this.sendImage(dataCollectionGroup.getXtalSnapshotFullPath());
			}
			else{
				log.error("getXtalThumbnailByDataCollectionGroupId Path {} does not exist. technique=EM ", dataCollectionGroup.getXtalSnapshotFullPath());
			}
		}
		return null;

	}

}
