package ispyb.ws.rest.saxs;

import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.ws.rest.RestWebService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/")
public class MacromoleculeRestWebService extends RestWebService {
	private final static Logger logger = Logger.getLogger(BufferRestWebService.class);

	@PermitAll
	@GET
	@Path("{token}/proposal/{proposal}/saxs/macromolecule/list")
	@Produces({ "application/json" })
	public Response getMacromoleculeList(@PathParam("token") String token, @PathParam("proposal") String proposal)
			throws Exception {

		String methodName = "getMacromoleculeList";
		long id = this.logInit(methodName, logger, token, proposal);
		try {
			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			SaxsProposal3Service saxsProposalService = (SaxsProposal3Service) ejb3ServiceLocator
					.getLocalService(SaxsProposal3Service.class);
			List<Macromolecule3VO> macromolecules = new ArrayList<Macromolecule3VO>();
			macromolecules.addAll(saxsProposalService.findMacromoleculesByProposalId(this.getProposalId(proposal)));
			this.logFinish(methodName, id, logger);
			return sendResponse(macromolecules);
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
	}

	@PermitAll
	@POST
	@Path("{token}/proposal/{proposal}/saxs/macromolecule/save")
	@Produces({ "application/json" })
	public Response saveMacromolecule(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@FormParam("macromolecule") String macromolecule) throws Exception {

		String methodName = "saveMacromolecule";
		long id = this.logInit(methodName, logger, token, proposal, macromolecule);
		try {
			SaxsProposal3Service saxsProposalService = this.getSaxsProposal3Service();
			Macromolecule3VO macromolecule3VO = saxsProposalService.merge(this.getGson().fromJson(macromolecule, Macromolecule3VO.class));
			this.logFinish(methodName, id, logger);
			return this.sendResponse(macromolecule3VO);
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
	}
}
