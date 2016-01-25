package ispyb.ws.rest.mx;

import java.io.File;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import ispyb.server.mx.vos.sample.Crystal3VO;

@Path("/")
public class CrystalRestWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(CrystalRestWebService.class);

	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/crystal/datacollection/{dataCollectionId}/pdb/download")
	@Produces("text/plain")
	public Response downloadPdbByDataCollectionId(@PathParam("token") String token,
			@PathParam("proposal") String proposal, 
			@PathParam("dataCollectionId") int dataCollectionId) {

		String methodName = "downloadPdbByDataCollectionId";
		long start = this.logInit(methodName, logger, token, proposal, dataCollectionId);
		try {
			String filePath = this.getDataCollection3Service().findPdbFullPath(dataCollectionId);
			System.out.println(filePath);
			this.logFinish(methodName, start, logger);
			if (filePath != null){
				if (new File(filePath).exists()){
					return this.downloadFile(filePath);
				}
			}
			throw new Exception("File " + filePath + " does not exit");
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	
	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/crystal/{crystalId}/get")
	@Produces({ "application/json" })
	public Response getCrystalById(@PathParam("token") String token,
			@PathParam("proposal") String proposal, 
			@PathParam("crystalId") int crystalId) {

		String methodName = "getCrystalById";
		long start = this.logInit(methodName, logger, token, crystalId);
		try {
			return this.sendResponse(this.getCrystal3Service().findByPk(crystalId, true));
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/crystal/list")
	@Produces({ "application/json" })
	public Response getCrystalById(@PathParam("token") String token,
			@PathParam("proposal") String proposal) {

		String methodName = "getCrystalById";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<Crystal3VO> crystals = this.getCrystal3Service().findByProposalId(this.getProposalId(proposal));
			System.out.println("ProposalId: " + this.getProposalId(proposal));
			System.out.println("Crystals: " + crystals.size());
			return this.sendResponse(crystals);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
		

}
