package ispyb.ws.rest.proposal;

import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.StockSolution3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Platetype3VO;
import ispyb.server.common.vos.login.Login3VO;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.Protein3VO;
import ispyb.ws.rest.mx.MXRestWebService;

import java.util.ArrayList;
import java.util.HashMap;
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
			Login3VO login3VO = this.getLogin3Service().findByToken(token);
			if (login3VO != null){
				if (login3VO.isValid()){
					List<Map<String, Object>> proposals = new ArrayList<Map<String,Object>>(); 
					if (login3VO.isLocalContact() || login3VO.isManager()){
						proposals = this.getProposal3Service().findProposals();
					}
					else{
						proposals = this.getProposal3Service().findProposals(login3VO.getUsername());
					}
					this.logFinish(methodName, id, logger);
					return this.sendResponse(proposals);
				}
			}
			return this.sendError("Token is not valid");
		} catch (Exception e) {
			return this.logError("getProposals", e, id, logger);
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
			results.put("proposal", proposals);
			results.put("crystals", crystals);
			results.put("plateTypes", plateTypes);
			results.put("macromolecules", macromolecules);
			results.put("buffers", buffers);
			results.put("stockSolutions", stockSolutions);
			results.put("labcontacts", labContacts);
			results.put("proteins", proteins);

			multiple.add(results);
			this.logFinish("listProposal", id, logger);

			return this.sendResponse(multiple);

		} catch (Exception e) {
			return this.logError("listProposal", e, id, logger);
		}
	}
}
