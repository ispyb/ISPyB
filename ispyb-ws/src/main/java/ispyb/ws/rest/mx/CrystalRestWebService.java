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

import ispyb.server.mx.vos.autoproc.GeometryClassname3VO;
import ispyb.server.mx.vos.autoproc.SpaceGroup3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;

@Path("/")
public class CrystalRestWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(CrystalRestWebService.class);

	@RolesAllowed({ "User", "Manager", "Localcontact" })
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
	
	
	@RolesAllowed({ "User", "Manager", "Localcontact" })
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
	
	@RolesAllowed({ "User", "Manager", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/crystal/list")
	@Produces({ "application/json" })
	public Response getCrystalList(@PathParam("token") String token,@PathParam("proposal") String proposal) {

		String methodName = "getCrystalById";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<Crystal3VO> crystals = this.getCrystal3Service().findByProposalId(this.getProposalId(proposal));
			return this.sendResponse(crystals);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({ "User", "Manager", "Localcontact" })
	@GET
	@Path("{token}/mx/geometryclass/{spacegroup}/get")
	@Produces({ "application/json" })
	public Response getGeometryClassBySpaceGroup(@PathParam("token") String token,
			@PathParam("spacegroup") String spaceGroup) {

		String methodName = "getGeometryClassBySpaceGroup";
		long start = this.logInit(methodName, logger, token, spaceGroup);
		try {
			List<SpaceGroup3VO> res = this.getSpaceGroup3Service().findBySpaceGroupShortName(spaceGroup);
			if (res == null || res.isEmpty() ){
				return null;
			} else if (res.size() >1) {
				logger.debug("more than 1 space group has been found");
				return null;
			} else {
				GeometryClassname3VO geoClass = res.get(0).getGeometryClassnameVO();
				logger.debug("res="+ geoClass.getGeometryClassname());
				return this.sendResponse(geoClass);
			}
			
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}			

}
