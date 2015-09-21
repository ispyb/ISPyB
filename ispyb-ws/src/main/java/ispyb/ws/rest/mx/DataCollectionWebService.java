package ispyb.ws.rest.mx;

import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.Image3VO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/")
public class DataCollectionWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(DataCollectionWebService.class);

	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/session/{sessionId}/old")
	@Produces({ "application/json" })
	public Response getDataCollectionBySessionId(@PathParam("token") String token,
			@PathParam("proposal") String proposal, 
			@PathParam("sessionId") int sessionId) {

		String methodName = "getDataCollectionBySessionId";
		long start = this.logInit(methodName, logger, token, proposal, sessionId);
		try {
			List<DataCollection3VO> dataCollections = this.getDataCollection3Service().findFiltered(null, null, null, sessionId, null, true, null);
			this.logFinish(methodName, start, logger);
			return this.sendResponse(dataCollections);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/{dataCollectionId}/crystalsnaphot/{id}/get")
	@Produces({ "application/json" })
	public Response getCrystalSnapshot(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId,
			@PathParam("id") int id) {

		String methodName = "getCrystalSnapshot";
		long start = this.logInit(methodName, logger, token, proposal, dataCollectionId, id);
		try {
			DataCollection3VO dataCollection = this.getDataCollection3Service().findByPk(dataCollectionId, false, false, false);
			this.logFinish(methodName, start, logger);
			if (dataCollection != null){
				if (id == 1){
					return this.sendImage(dataCollection.getXtalSnapshotFullPath1());
				}
				if (id == 2){
					return this.sendImage(dataCollection.getXtalSnapshotFullPath2());
				}
				if (id == 3){
					return this.sendImage(dataCollection.getXtalSnapshotFullPath3());
				}
				if (id == 4){
					return this.sendImage(dataCollection.getXtalSnapshotFullPath4());
				}
			}
			
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
		return null;
	}
	
	
	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/session/{sessionIdList}/list")
	@Produces({ "application/json" })
	public Response getDataCollectionNativelyBySessionId(@PathParam("token") String token,
			@PathParam("proposal") String proposal, 
			@PathParam("sessionIdList") String sessionIdList) {

		String methodName = "getDataCollectionNativelyBySessionId";
		long start = this.logInit(methodName, logger, token, proposal, sessionIdList);
		try {
			List<Integer> ids = this.parseToInteger(sessionIdList);
			List<Map<String, Object>> dataCollections = new ArrayList<Map<String,Object>>();
			
			for (Integer id : ids) {
				dataCollections.addAll(this.getNativeDataCollection3Service().getDataCollectionBySessionId(id));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(dataCollections);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
		

}
