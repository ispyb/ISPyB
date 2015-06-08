package ispyb.ws.rest.saxs;

import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.ws.rest.RestWebService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/")
public class BufferRestWebService extends RestWebService {
	
	
	@PermitAll
	@GET
	@Path("{cookie}/saxs/{login}/buffer/list")
	@Produces({ "application/json" })
	public String list(
			@PathParam("cookie") String cookie, 
			@PathParam("login") String login) throws Exception {
		
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		SaxsProposal3Service saxsProposalService = (SaxsProposal3Service) ejb3ServiceLocator.getLocalService(SaxsProposal3Service.class);
		List<Proposal3VO> proposals = saxsProposalService.findProposalByLoginName(login);
		List<Buffer3VO> buffers = new ArrayList<Buffer3VO>();
		for (Proposal3VO proposal3vo : proposals) {
			buffers.addAll(saxsProposalService.findBuffersByProposalId(proposal3vo.getProposalId()));
		}
		return getGson().toJson(buffers);
	}
}
