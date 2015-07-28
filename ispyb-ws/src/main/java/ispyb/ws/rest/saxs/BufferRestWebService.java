package ispyb.ws.rest.saxs;

import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.common.util.LoggerFormatter;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.ws.rest.RestWebService;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.google.gson.Gson;

@Path("/")
public class BufferRestWebService extends RestWebService {
	
	private final static Logger LOGGER = Logger.getLogger(BufferRestWebService.class);
	@PermitAll
	@GET
	@Path("{cookie}/proposal/{proposal}/saxs/buffer/list")
	@Produces({ "application/json" })
	public Response getBuffers(
			@PathParam("cookie") String cookie, 
			@PathParam("proposal") String proposal) throws Exception {
		SaxsProposal3Service saxsProposalService = this.getSaxsProposal3Service();
		List<Proposal3VO> proposals = saxsProposalService.findProposalByLoginName(proposal);
		List<Buffer3VO> buffers = new ArrayList<Buffer3VO>();
		for (Proposal3VO proposal3vo : proposals) {
			buffers.addAll(saxsProposalService.findBuffersByProposalId(proposal3vo.getProposalId()));
		}
		return this.sendResponse(buffers);
	}
	
	@PermitAll
	@POST
	@Path("{cookie}/proposal/{proposal}/saxs/buffer/save")
	@Produces({ "application/json" })
	public Response saveBuffer(
			@PathParam("cookie") String cookie, 
			@PathParam("proposal") String proposal,
			@FormParam("buffer") String buffer) throws Exception {
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("cookie", String.valueOf(cookie));
		params.put("proposal", String.valueOf(proposal));
		params.put("buffer", String.valueOf(buffer));
		
		long start = this.logInit("saveBuffer", new Gson().toJson(params), LOGGER);
		try {
			SaxsProposal3Service saxsProposalService = this.getSaxsProposal3Service();
			Buffer3VO buffer3VO = this.getGson().fromJson(buffer, Buffer3VO.class);
			this.logFinish("saveBuffer", start, LOGGER);
			return this.sendResponse(saxsProposalService.merge(buffer3VO));
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "saveBuffer", start, System.currentTimeMillis(),
					e.getMessage(), e);
		}
		return null;
	}
	
	
}
