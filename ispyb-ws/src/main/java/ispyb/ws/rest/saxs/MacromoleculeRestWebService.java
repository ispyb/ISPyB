package ispyb.ws.rest.saxs;

import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.ws.rest.RestWebService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/")
public class MacromoleculeRestWebService extends RestWebService{
	
	@PermitAll
	@GET
	@Path("{cookie}/saxs/{proposal}/macromolecule/list")
	@Produces({ "application/json" })
	public Response list(
			@PathParam("cookie") String cookie, 
			@PathParam("proposal") String proposal) throws Exception {
		
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		SaxsProposal3Service saxsProposalService = (SaxsProposal3Service) ejb3ServiceLocator.getLocalService(SaxsProposal3Service.class);
		List<Macromolecule3VO> macromolecules = new ArrayList<Macromolecule3VO>();
		macromolecules.addAll(saxsProposalService.findMacromoleculesByProposalId(this.getProposalId(proposal)));
		
		return sendResponse(macromolecules);
	}
}
