package ispyb.ws.rest.saxs;

import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.vos.dataAcquisition.Additive3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.vos.proposals.Proposal3VO;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/")
public class BufferAdditiveRestWebService extends SaxsRestWebService {
	
	private final static Logger logger = Logger.getLogger(BufferAdditiveRestWebService.class);
	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/buffer/{buffer}/additive/list")
	@Produces({ "application/json" })
	public Response getAdditives(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("buffer") String buffer) {

		String methodName = "getBufferAdditives";
		long start = this.logInit(methodName, logger, token, proposal, buffer);
		try{
			SaxsProposal3Service saxsProposalService = this.getSaxsProposal3Service();
			Proposal3Service proposalService = this.getProposal3Service();
			List<Proposal3VO> proposals = proposalService.findProposalByLoginName(proposal);
			List<Buffer3VO> buffers = new ArrayList<Buffer3VO>();
			/*for (Proposal3VO proposal3vo : proposals) {
				buffers.addAll(saxsProposalService.findBuffersByProposalId(proposal3vo.getProposalId()));
			}*/
			List<Additive3VO> additives = new ArrayList<Additive3VO>();
			additives = saxsProposalService.findAdditivesByBufferId(Integer.parseInt(buffer));

			this.logFinish(methodName, start, logger);
			return this.sendResponse(additives);
		}
		catch(Exception e){
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@POST
	@Path("{token}/proposal/{proposal}/saxs/buffer/{buffer}/additive/save")
	@Produces({ "application/json" })
	public Response saveBuffer(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("buffer") String buffer,
			@FormParam("additive") String additive) throws Exception {
		
		String methodName = "saveBufferAdditive";
		long start = this.logInit(methodName, logger, token, proposal, buffer, additive);
		try {
			SaxsProposal3Service saxsProposalService = this.getSaxsProposal3Service();
			Additive3VO additive3VO = this.getGson().fromJson(additive, Additive3VO.class);
			this.logFinish(methodName, start, logger);
			return this.sendResponse(saxsProposalService.merge(additive3VO));
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	
}