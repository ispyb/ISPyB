package ispyb.ws.rest.proposal;

import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.StockSolution3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Platetype3VO;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.ws.rest.RestWebService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

@Path("/")
public class ProposalRestWebService extends RestWebService {

	private final static Logger logger = Logger.getLogger(ProposalRestWebService.class);

	@PermitAll
	@GET
	@Path("{cookie}/proposal/user/{login}/list")
	@Produces({ "application/json" })
	public Response getProposals(@PathParam("cookie") String cookie, @PathParam("login") String login) throws Exception {
		
		long id = this.logInit("getProposals", logger, cookie, login);
		try {
			List<Proposal3VO> proposals = this.getSaxsProposal3Service().findProposalByLoginName(login);
			this.logFinish("getProposals", id, logger);
			return this.sendResponse(proposals);

		} catch (Exception e) {
			return this.logError("getProposals", e, id, logger);
		}
	}

	@PermitAll
	@GET
	@Path("{cookie}/proposal/{proposal}/technique/{technique}/get")
	@Produces({ "application/json" })
	public Response listProposal(@PathParam("cookie") String cookie, @PathParam("proposal") String login)
			throws Exception {
		long id = this.logInit("listProposal", logger, cookie, login);
		try {
			ArrayList<HashMap<String, List<?>>> multiple = new ArrayList<HashMap<String, List<?>>>();
			int proposalId = this.getProposalId(login);
			HashMap<String, List<?>> results = new HashMap<String, List<?>>();
			long time = System.currentTimeMillis();
			List<Macromolecule3VO> macromolecules = this.getSaxsProposal3Service().findMacromoleculesByProposalId(
					proposalId);
			List<Buffer3VO> buffers = this.getSaxsProposal3Service().findBuffersByProposalId(proposalId);

			List<StockSolution3VO> stockSolutions = this.getSaxsProposal3Service().findStockSolutionsByProposalId(
					proposalId);
			List<Platetype3VO> plateTypes = this.getPlateType3Service().findAll();
			time = System.currentTimeMillis();
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
