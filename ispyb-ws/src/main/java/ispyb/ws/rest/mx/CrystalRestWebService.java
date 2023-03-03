package ispyb.ws.rest.mx;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.apache.poi.util.StringUtil;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import file.FileUploadForm;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.assembly.Structure3VO;
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
					return this.downloadFileAsAttachment(filePath);
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
	@Path("{token}/proposal/{proposal}/mx/crystal/proteinid/{proteinid}/list")
	@Produces({ "application/json" })
	public Response getCrystalByProteinId(@PathParam("token") String token,@PathParam("proposal") String proposal,@PathParam("proteinid") Integer proteinId) {

		String methodName = "getCrystalByProteinId";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<Crystal3VO> crystals = this.getCrystal3Service().findByProposalId(this.getProposalId(proposal));
			List<Crystal3VO> filtered =  new ArrayList<Crystal3VO>();
			for (Crystal3VO crystal3vo : crystals) {
				if (crystal3vo.getProteinVO().getProteinId().equals(proteinId)){
					filtered.add(crystal3vo);
				}
			}
			return this.sendResponse(filtered);
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


	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@POST
	@Path("{token}/proposal/{proposal}/mx/crystal/{crystalid}/structure/save")
	@Consumes("multipart/form-data")	
	@Produces({ "application/json" })
	public Response saveStructure(
			@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("crystalid") Integer crystalId,
			@MultipartForm FileUploadForm form) throws IllegalStateException, IOException{
				
		try {
			if (form.getInputStream() != null){
				String filePath = null;
				if (! form.getFileName().equals("") ) {
					filePath = this.copyFileToDisk(proposal, form);
					logger.info("saveStructure. Copying to disk. filepath=" + filePath);
				}
				Crystal3VO crystal = this.getCrystal3Service().findByPk(crystalId, true);
				if (crystal != null){
					Structure3VO structure = new Structure3VO();
					structure.setCrystalId(crystalId);
					structure.setType(form.getType());
					structure.setGroupName(form.getGroupName());
					if (filePath != null) {
						structure.setFilePath(filePath);
						structure.setName(new File(filePath).getName());
					}
					structure.setMultiplicity(form.getMultiplicity());
					structure.setUniprotId(form.getUniprotId());
					this.getExperiment3Service().saveStructure(structure);
				}
				else{
					throw new Exception("Crystal with id: " + crystalId + " does not exist");
				}
				return this.sendResponse(Status.OK);
			}
			else{
				throw new Exception("File is empty");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(String.format("saveStructure. ", e.getMessage()));
			return this.sendError(e.getMessage());
		}		
	}
	
	
	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/crystal/{crystalid}/structure/{structureId}/delete")
	@Consumes("multipart/form-data")	
	@Produces({ "application/json" })
	public Response removeStructure(
			@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("crystalid") Integer crystalId,
			@PathParam("structureId") Integer structureId) throws IllegalStateException, IOException{
				
		try {
				logger.info(String.format("removeStructure. crystalId=%s structureId=%s token=%s", crystalId, structureId, token));
				Crystal3VO crystal = this.getCrystal3Service().findByPk(crystalId, true);
				if (crystal != null){
										
					Structure3VO structure = this.getExperiment3Service().findStructureById(structureId);
					/** Check that structure belongs to crystal **/
					if (structure.getCrystalId().equals(crystal.getCrystalId())){
						/** Checks that proposal corresponds to crystal **/
						if (crystal.getProteinVO().getProposalVOId().equals(this.getProposalId(proposal))){
							this.getExperiment3Service().removeStructure(structureId);
							logger.info(String.format("Structure has been removed. crystalId=%s structureId=%s token=%s", crystalId, structureId, token));
						}
					}
				}
				else{
					throw new Exception("Crystal with id: " + crystalId + " does not exist");
				}
				return this.sendResponse(Status.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(String.format("saveStructure. ", e.getMessage()));
			return this.sendError(e.getMessage());
		}		
	}
}
