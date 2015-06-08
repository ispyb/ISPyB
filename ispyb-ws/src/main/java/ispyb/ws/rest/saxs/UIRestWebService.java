package ispyb.ws.rest.saxs;

import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.services.collections.Session3Service;
import ispyb.server.mx.vos.collections.Session3VO;
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

@Path("/")
public class UIRestWebService extends RestWebService {
	
	
	@PermitAll
	@GET
	@Path("{cookie}/saxs/{proposal}/ui/list")
	@Produces({ "application/json" })
	public Response list(
			@PathParam("cookie") String cookie, 
			@PathParam("proposal") String proposal) throws Exception {
		
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		
		int proposalId = this.getProposalId(proposal);
		
		/** Getting sessions **/
		Session3Service session3Service = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
		List<Session3VO> sessions = session3Service.findFiltered(proposalId, null, null, null, null, null, false, null);
		
		SaxsProposal3Service saxsProposal3Service = (SaxsProposal3Service) ejb3ServiceLocator.getLocalService(SaxsProposal3Service.class);
		List<Macromolecule3VO> macromolecules = saxsProposal3Service.findMacromoleculesByProposalId(proposalId);
		
		HashMap<String, List<?>> results = new HashMap<String, List<?>>();
		
		results.put("sessions", sessions);
		results.put("macromolecules", macromolecules);
		
		return sendResponse(results);
		
	}
}
