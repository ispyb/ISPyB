package ispyb.ws.rest.mx;

import ispyb.server.biosaxs.vos.assembly.Structure3VO;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
@Path("/")
public class StructureRestWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(StructureRestWebService.class);

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/structures/{datacollectionid}/list")
	@Produces({ "application/json" })
	public Response getStructuresJSON(@PathParam("token") String token,
			@PathParam("datacollectionid") Integer dataCollectionId) {

		try {
			List<Structure3VO> structures = this.getStruture3Service().getStructuresByDataCollectionId(dataCollectionId);
			return this.sendResponse(structures);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/structures/{datacollectionid}/csv")
	@Produces({ "application/json" })
	public Response getStructuresCSV(@PathParam("token") String token,
			@PathParam("datacollectionid") Integer dataCollectionId) {

		try {
			StringBuilder sb = new StringBuilder();
			List<Structure3VO> structures = this.getStruture3Service().getStructuresByDataCollectionId(dataCollectionId);
			for (Structure3VO structure3vo : structures) {
				sb.append(structure3vo.getGroupName());
				sb.append(",");
				sb.append(structure3vo.getType());
				sb.append(",");
				sb.append(structure3vo.getFilePath());
				sb.append(",");
				sb.append(structure3vo.getMultiplicity());
				sb.append(",");
				sb.append(structure3vo.getUniprotId());
				sb.append("\n");
			}
			return this.sendResponse(sb.toString());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
}
