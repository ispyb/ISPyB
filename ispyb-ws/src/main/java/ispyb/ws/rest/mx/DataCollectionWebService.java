package ispyb.ws.rest.mx;

import ispyb.server.mx.vos.collections.DataCollection3VO;

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

	@RolesAllowed({"User", "Manager", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/acronyms/{acronyms}/list")
	@Produces({ "application/json" })
	public Response list(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("acronyms") String acronyms) throws Exception {

		String methodName = "getDataCollections";
		long id = this.logInit(methodName, logger, token, proposal);
		try {
			List<Map<String, Object>> result = this.getNativeDataCollection3Service().getByProteinAcronymList(this.getProposalId(proposal), this.parseToString(acronyms));
			this.logFinish(methodName, id, logger);
			return sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}

	}
	
	//TODO: /get should be changed by list
	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/{dataCollectionIdList}/get")
	@Produces({ "application/json" })
	public Response getDataCollectionById(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("dataCollectionIdList") String dataCollectionIdList) {

		String methodName = "getDataCollectionById";
		long start = this.logInit(methodName, logger, token, proposal, dataCollectionIdList);
		try {
			List<Integer> ids = this.parseToInteger(dataCollectionIdList);
			
			List<Map<String, Object>> result = this.getNativeDataCollection3Service().getDataCollectionById(ids);
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
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
