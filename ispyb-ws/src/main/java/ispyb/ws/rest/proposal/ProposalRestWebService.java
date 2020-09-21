package ispyb.ws.rest.proposal;

import file.FileUploadForm;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.assembly.Structure3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.StockSolution3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Platetype3VO;
import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.vos.login.Login3VO;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.Protein3VO;
import ispyb.server.smis.UpdateFromSMIS;
import ispyb.ws.rest.mx.MXRestWebService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

@Path("/")
public class ProposalRestWebService extends MXRestWebService{

	private final static Logger logger = Logger.getLogger(ProposalRestWebService.class);

	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/list")
	@Produces({ "application/json" })
	public Response getProposals(@PathParam("token") String token) throws Exception {
		String methodName = "getProposals";
		long id = this.logInit(methodName, logger, token);
		try {
			List<Map<String, Object>> proposals = this.getProposalsFromToken(token);
			this.logFinish(methodName, id, logger);
			return this.sendResponse(proposals);
		} catch (Exception e) {
			return this.logError("getProposals", e, id, logger);
		}				
	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/info/get")
	@Produces({ "application/json" })
	public Response getProposaInfos(@PathParam("token") String token, @PathParam("proposal") String proposal)
				throws Exception {
		String methodName = "getProposaInfos";
		long id = this.logInit(methodName, logger, token, proposal);
		try {
			ArrayList<HashMap<String, List<?>>> multiple = new ArrayList<HashMap<String, List<?>>>();				
			HashMap<String, List<?>> results = new HashMap<String, List<?>>();
			
			if (proposal == null || proposal.isEmpty()) {					
				List<Map<String, Object>> proposals = this.getProposalsFromToken(token);
				results.put("proposal", proposals);
				
			} else {
						
				int proposalId = this.getProposalId(proposal);

				List<Macromolecule3VO> macromolecules = this.getSaxsProposal3Service().findMacromoleculesByProposalId(proposalId);
				List<Buffer3VO> buffers = this.getSaxsProposal3Service().findBuffersByProposalId(proposalId);

				List<StockSolution3VO> stockSolutions = this.getSaxsProposal3Service().findStockSolutionsByProposalId(
						proposalId);
				List<Platetype3VO> plateTypes = this.getPlateType3Service().findAll();
				List<Proposal3VO> proposals = new ArrayList<Proposal3VO>();
				proposals.add(this.getProposal3Service().findProposalById(proposalId));
				
				List<Protein3VO> proteins = this.getProtein3Service().findByProposalId(proposalId);
				List<Crystal3VO> crystals = this.getCrystal3Service().findByProposalId(proposalId);
				List<Structure3VO> ligands = this.getStruture3Service().getStructuresByProposalId(proposalId);
				
				List<LabContact3VO> labContacts = this.getLabContact3Service().findFiltered(proposalId, null);
				results.put("proposal", proposals);
				results.put("crystals", crystals);
				results.put("plateTypes", plateTypes);
				results.put("macromolecules", macromolecules);
				results.put("buffers", buffers);
				results.put("stockSolutions", stockSolutions);
				results.put("labcontacts", labContacts);
				results.put("proteins", proteins);
				results.put("ligands", ligands);
			}

			multiple.add(results);
			this.logFinish(methodName, id, logger);

			return this.sendResponse(multiple);

		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
	}
	
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/session/{sessionId}/list")
	@Produces({ "application/json" })
	public Response getProposalsBySessionId(
			@PathParam("token") String token,
			@PathParam("sessionId") int sessionId) throws Exception {
		String methodName = "getProposalsBySessionId";
		long id = this.logInit(methodName, logger, token);
		try {
			Session3VO session = this.getSession3Service().findByPk(sessionId, false, false, false);
			List<Map<String, Object>> proposal = this.getProposal3Service().findProposalById(session.getProposalVOId());
			this.logFinish(methodName, id, logger);
			return this.sendResponse(proposal);
		} catch (AccessDeniedException e) {
			return this.sendError(methodName + " unauthorized user");
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
	}
	

	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/technique/{technique}/get")
	@Produces({ "application/json" })
	public Response listProposal(@PathParam("token") String token, @PathParam("proposal") String login)
			throws Exception {
		//TODO remove this method if above getProposaInfos is sufficient
		long id = this.logInit("listProposal", logger, token, login);
		try {
			ArrayList<HashMap<String, List<?>>> multiple = new ArrayList<HashMap<String, List<?>>>();
			int proposalId = this.getProposalId(login);
			HashMap<String, List<?>> results = new HashMap<String, List<?>>();

			List<Macromolecule3VO> macromolecules = this.getSaxsProposal3Service().findMacromoleculesByProposalId(proposalId);
			List<Buffer3VO> buffers = this.getSaxsProposal3Service().findBuffersByProposalId(proposalId);

			List<StockSolution3VO> stockSolutions = this.getSaxsProposal3Service().findStockSolutionsByProposalId(
					proposalId);
			List<Platetype3VO> plateTypes = this.getPlateType3Service().findAll();
			List<Proposal3VO> proposals = new ArrayList<Proposal3VO>();
			proposals.add(this.getProposal3Service().findProposalById(proposalId));
			
			List<Protein3VO> proteins = this.getProtein3Service().findByProposalId(proposalId);
			List<Crystal3VO> crystals = this.getCrystal3Service().findByProposalId(proposalId);
			
			List<LabContact3VO> labContacts = this.getLabContact3Service().findFiltered(proposalId, null);
			
			List<Structure3VO> ligands = this.getStruture3Service().getStructuresByProposalId(proposalId);
			
			results.put("proposal", proposals);
			results.put("crystals", crystals);
			results.put("plateTypes", plateTypes);
			results.put("macromolecules", macromolecules);
			results.put("buffers", buffers);
			results.put("stockSolutions", stockSolutions);
			results.put("labcontacts", labContacts);
			results.put("proteins", proteins);
			results.put("ligands", ligands);

			multiple.add(results);
			this.logFinish("listProposal", id, logger);

			return this.sendResponse(multiple);

		} catch (Exception e) {
			return this.logError("listProposal", e, id, logger);
		}
	}
	
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/update")
	@Produces({ "application/json" })
	public Response updateProposal(@PathParam("token") String token, @PathParam("proposal") String proposal)
			throws Exception {
		
		long id = this.logInit("updateProposal", logger, token, proposal);
		int proposalId = this.getProposalId(proposal);
		try {
			logger.info("Updating " + proposal + ":" + proposalId);
			UpdateFromSMIS.updateProposalFromSMIS(proposalId);
			this.logFinish("updateProposal", id, logger);
			HashMap<String, String> response = new HashMap<String, String>();
			response.put("Status", "Done");
			return this.sendResponse(response);
		}
		catch(Exception e){
			return this.logError("updateProposal", e, id, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/updateProposalsByToken")
	@Produces({ "application/json" })
	public Response updateProposalsByToken(@PathParam("token") String token)
			throws Exception {

		long id = this.logInit("updateProposalsByToken", logger, token);
		try {
			Login3VO login3VO = this.getLogin3Service().findByToken(token);
			logger.info("Updating proposals for the user: " + login3VO.getUsername());
			List<Proposal3VO> proposals = this.getProposal3Service().findProposalByLoginName(login3VO.getUsername());
			for (Proposal3VO proposal : proposals) {
				UpdateFromSMIS.updateProposalFromSMIS(proposal.getProposalId());
			}

			this.logFinish("updateProposalsByToken", id, logger);
			HashMap<String, String> response = new HashMap<String, String>();
			response.put("Status", "Done");
			return this.sendResponse(response);
		}
		catch(Exception e){
			return this.logError("updateProposalsByToken", e, id, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/proposalId")
	@Produces({ "application/json" })
	public Response getProposalByCodeAndNumber(@PathParam("token") String token,
											   @FormParam("proposalNumber") String proposalNumber,
											   @FormParam("proposalCode") String proposalCode) throws Exception {
		String methodName = "getProposalByCodeAndNumber";
		long id = this.logInit(methodName, logger, token);
		Integer proposalId = 0;
		try {
			Login3VO login3VO = this.getLogin3Service().findByToken(token);
			Proposal3VO proposal = this.getProposal3Service().findForWSByCodeAndNumber(proposalCode, proposalNumber);
			if (proposal != null){
				proposalId = proposal.getProposalId();
			}
			this.logFinish(methodName, id, logger);
			return this.sendResponse(proposalId.toString());
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
	}
	
	
	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/ligands/list")	
	@Produces({ "application/json" })
	public Response getLigandsByProposalId(
			@PathParam("token") String token,
			@PathParam("proposal") String proposal) throws IllegalStateException, IOException{
				
		try {
			logger.info("getLigandsByProposalId for proposal " + proposal);
			return this.sendResponse(this.getStruture3Service().getStructuresByProposalId(this.getProposalId(proposal)));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(String.format("getLigandsByProposalId. ", e.getMessage()));
			return this.sendError(e.getMessage());
		}		
	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@POST
	@Path("{token}/proposal/{proposal}/structure/save")
	@Consumes("multipart/form-data")	
	@Produces({ "application/json" })
	public Response saveStructure(
			@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@MultipartForm FileUploadForm form) throws IllegalStateException, IOException{
				
		try {
			logger.info("saveStructure for proposal " + proposal);
			if (form.getInputStream() != null){
				String filePath = this.copyFileToDisk(proposal, form);
				logger.info("saveStructure. Copying to disk. filepath=" + filePath);				
				
				Structure3VO structure = new Structure3VO();
				structure.setProposalId(this.getProposalId(proposal));
				structure.setType(form.getType());
				structure.setGroupName(form.getGroupName());
				structure.setFilePath(filePath);
				structure.setName(new File(filePath).getName());					
				structure = this.getExperiment3Service().saveStructure(structure);
				
				return this.sendResponse(structure);
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
	

}
