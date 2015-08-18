package ispyb.ws.rest.proposal;

import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.StockSolution3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Platetype3VO;
import ispyb.server.common.vos.login.Login3VO;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.ws.rest.RestWebService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/")
public class ProposalRestWebService extends RestWebService {

	private final static Logger logger = Logger.getLogger(ProposalRestWebService.class);

	@RolesAllowed({"User", "Manager", "LocalContact"})
	@GET
	@Path("{token}/proposal/list")
	@Produces({ "application/json" })
	public Response getProposals(@PathParam("token") String token) throws Exception {
		
		long id = this.logInit("getProposals", logger, token);
		try {
			Login3VO login3VO = this.getLogin3Service().findByToken(token);
			if (login3VO != null){
				if (login3VO.isValid()){
					List<Proposal3VO> proposals = new ArrayList<Proposal3VO>();
					if (login3VO.isLocalContact() || login3VO.isManager()){
						proposals = this.getSaxsProposal3Service().findAllProposals();
					}
					else{
						proposals = this.getSaxsProposal3Service().findProposalByLoginName(login3VO.getUsername());
					}
					this.logFinish("getProposals", id, logger);
					return this.sendResponse(proposals);
				}
			}
			throw new Exception("Token is not valid");

		} catch (Exception e) {
			return this.logError("getProposals", e, id, logger);
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

			List<Macromolecule3VO> macromolecules = this.getSaxsProposal3Service().findMacromoleculesByProposalId(
					proposalId);
			List<Buffer3VO> buffers = this.getSaxsProposal3Service().findBuffersByProposalId(proposalId);

			List<StockSolution3VO> stockSolutions = this.getSaxsProposal3Service().findStockSolutionsByProposalId(
					proposalId);
			List<Platetype3VO> plateTypes = this.getPlateType3Service().findAll();
			List<Proposal3VO> proposals = new ArrayList<Proposal3VO>();
			proposals.add(this.getSaxsProposal3Service().findProposalById(proposalId));
			// List<Session3VO> sessions =
			// this.getSession3Service().findFiltered(proposalId, null, null,
			// null, null, null, false, null);
			// List<Assembly3VO> assemblies =
			// this.getSaxsProposal3Service().findAssembliesByProposalId(proposalId);
			List<LabContact3VO> labContacts = this.getLabContact3Service().findFiltered(proposalId, null);
			// List<Shipping3VO> shippings =
			// this.getShipping3Service().findByProposal(proposalId, true);

			results.put("proposal", proposals);
			results.put("plateTypes", plateTypes);
			// results.put("sessions", sessions);
			results.put("macromolecules", macromolecules);
			results.put("buffers", buffers);
			results.put("stockSolutions", stockSolutions);
			// results.put("assemblies", assemblies);
			results.put("labcontacts", labContacts);
			// results.put("shippings", shippings);

			multiple.add(results);
			this.logFinish("listProposal", id, logger);

			return this.sendResponse(multiple);

		} catch (Exception e) {
			return this.logError("listProposal", e, id, logger);
		}
	}

}
