package ispyb.ws.rest.mx;

import java.io.File;
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
import org.apache.poi.util.StringUtil;

import ispyb.server.mx.vos.autoproc.GeometryClassname3VO;
import ispyb.server.mx.vos.autoproc.SpaceGroup3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.Protein3VO;

@Path("/")
public class CrystalRestWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(CrystalRestWebService.class);

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
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
	
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
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
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
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
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/crystal/geometryclass/{spacegroup}/list")
	@Produces({ "application/json" })
	public Response getGeometryClassBySpaceGroup(@PathParam("token") String token,
			@PathParam("spacegroup") String spaceGroup) {

		String methodName = "getGeometryClassBySpaceGroup";
		long start = this.logInit(methodName, logger, token, spaceGroup);
		try {
			List<SpaceGroup3VO> res = this.getSpaceGroup3Service().findBySpaceGroupShortName(spaceGroup);
			this.logFinish(methodName, start, logger);
			return this.sendResponse(res);			
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}	
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@POST
	@Path("{token}/proposal/{proposal}/mx/crystal/proteinid/{proteinId}/save")
	@Produces({ "application/json" })
	public Response saveCrystalForm(
			@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("proteinId") Integer proteinId,
			@FormParam("crystalId") String stringCrystalId,
			@FormParam("name") String name,
			@FormParam("spaceGroup") String spaceGroup,
			@FormParam("cellA") Double cellA,
			@FormParam("cellB") Double cellB,
			@FormParam("cellC") Double cellC,
			@FormParam("cellAlpha") Double cellAlpha,
			@FormParam("cellBeta") Double cellBeta,
			@FormParam("cellGamma") Double cellGamma,
			@FormParam("comments") String comments
			
			) {

		String methodName = "saveCrystalForm";
		long start = this.logInit(methodName, logger, token, spaceGroup);
		try {
			List<Crystal3VO> crystalForms = this.getCrystal3Service().findByProposalId(this.getProposalId(proposal));
			
			Crystal3VO crystal = new Crystal3VO();
			
			Integer crystalId = null;
			try{
				crystalId = Integer.valueOf(stringCrystalId);
			}
			catch(Exception e){
				crystalId = null;
			}
			
			if (crystalId != null){
				/** Update **/
				for (Crystal3VO crystal3vo : crystalForms) {
					if (crystal3vo.getCrystalId().equals(crystalId)){
						crystal = crystal3vo;
					}
				}
			}			

			if (proteinId == null){
				throw new Exception("ProteinId must not be null");
			}
			else{
				crystal.setComments(comments);
				crystal.setName(name);
				crystal.setSpaceGroup(spaceGroup);
				crystal.setCellA(cellA);
				crystal.setCellB(cellB);
				crystal.setCellC(cellC);
				crystal.setCellAlpha(cellAlpha);
				crystal.setCellBeta(cellBeta);
				crystal.setCellGamma(cellGamma);
					
				/** We only update if the protein is in the list of the proteins of the proposal **/
				List<Protein3VO> proteins = this.getProtein3Service().findByProposalId(this.getProposalId(proposal));
				for (Protein3VO protein3vo : proteins) {
					if (protein3vo.getProteinId().equals(proteinId)){
						crystal.setProteinVO(protein3vo);
						/** Merge that creates and updates **/
						logger.info("Updating crystal form");
						crystal = this.getCrystal3Service().update(crystal);
						this.logFinish(methodName, start, logger);
						return this.sendResponse(crystal);	
					}
				}
				
			}
			
					
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
		return null;
	}			


}
