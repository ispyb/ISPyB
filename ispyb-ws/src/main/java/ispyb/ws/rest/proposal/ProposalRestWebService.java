package ispyb.ws.rest.proposal;

import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.StockSolution3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Platetype3VO;
import ispyb.server.common.vos.login.Login3VO;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.Protein3VO;
import ispyb.ws.rest.RestWebService;
import ispyb.ws.rest.mx.MXRestWebService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/")
public class ProposalRestWebService extends MXRestWebService{

	private final static Logger logger = Logger.getLogger(ProposalRestWebService.class);

	@RolesAllowed({"User", "Manager", "LocalContact"})
	@GET
	@Path("{token}/proposal/list")
	@Produces({ "application/json" })
	public Response getProposals(@PathParam("token") String token) throws Exception {
		
		long id = this.logInit("getProposals", logger, token);
		
		long startTime = System.currentTimeMillis();
		System.out.println("1 -- " + startTime);
		try {
			Login3VO login3VO = this.getLogin3Service().findByToken(token);
			long second = System.currentTimeMillis();
			System.out.println("2 -- " + second + "   " + (second - startTime));
			if (login3VO != null){
				if (login3VO.isValid()){
					List<Map<String, Object>> proposals = new ArrayList<Map<String,Object>>(); 
					if (login3VO.isLocalContact() || login3VO.isManager()){
						proposals = this.getSaxsProposal3Service().findProposals();
						
					}
					else{
						proposals = this.getSaxsProposal3Service().findProposals(login3VO.getUsername());
					}
					
					long third = System.currentTimeMillis();
					System.out.println("3 -- " + third + "   " + (third - second));
					long total = System.currentTimeMillis();
					System.out.println("4 -- " + total + "   " + (total - startTime));
					return this.sendResponse(proposals);
					
				}
			}
			throw new Exception("Token is not valid");

		} catch (Exception e) {
			return this.logError("getProposals", e, id, logger);
		}
	}
	
	
	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/shipping/dewar/list")
	@Produces({ "application/json" })
	public Response getDewarsByProposalId(@PathParam("token") String token,
			@PathParam("proposal") String proposal) throws NamingException {
		long id = this.logInit("getDewarsByProposalId", logger, token, proposal);
		try {
			List<Shipping3VO> shipments = this.getShipping3Service().findByProposal(this.getProposalId(proposal), true);
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			
			for (Shipping3VO shipping3vo : shipments) {
				result.addAll(this.getShipping3Service().getShippingById(shipping3vo.getShippingId()));
			}
			
			this.logFinish("getDewarsByProposalId", id, logger);
			return sendResponse(result);
		} catch (Exception e) {
			return this.logError("getDewarsByProposalId", e, id, logger);
		}
	}
	
	
	@RolesAllowed({"User", "Manager", "LocalContact"})
	@GET
	@Path("{token}/proposal/session/{sessionId}/list")
	@Produces({ "application/json" })
	public Response getProposalsBySessionId(
			@PathParam("token") String token,
			@PathParam("sessionId") int sessionId) throws Exception {
		String methodName = "getProposalsBySessionId";
		long id = this.logInit(methodName, logger, token);
		try {
			
			Session3VO sessions = this.getSession3Service().findByPk(sessionId, false, false, false);
			List<Map<String, Object>> proposal = this.getSaxsProposal3Service().findProposalById(sessions.getProposalVOId());
			
			
			
			this.logFinish(methodName, id, logger);
			return this.sendResponse(proposal);
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
	}
	

	@RolesAllowed({"User", "Manager", "LocalContact"})
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
			proposals.add(this.getSaxsProposal3Service().findProposalById(proposalId));
			
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
