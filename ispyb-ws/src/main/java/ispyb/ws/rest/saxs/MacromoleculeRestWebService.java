package ispyb.ws.rest.saxs;

import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.common.util.LoggerFormatter;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
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
public class MacromoleculeRestWebService extends RestWebService{
    	private final static Logger LOGGER = Logger.getLogger(BufferRestWebService.class);
    	
	@PermitAll
	@GET
	@Path("{cookie}/proposal/{proposal}/saxs/macromolecule/list")
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
	
	
	
	@PermitAll
	@POST
	@Path("{cookie}/proposal/{proposal}/saxs/macromolecule/save")
	@Produces({ "application/json" })
	public Response saveMacromolecule(
			@PathParam("cookie") String cookie, 
			@PathParam("proposal") String proposal,
			@FormParam("macromolecule") String macromolecule) throws Exception {
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("cookie", String.valueOf(cookie));
		params.put("proposal", String.valueOf(proposal));
		params.put("macromolecule", String.valueOf(macromolecule));
		
		long start = this.logInit("saveMacromolecule", new Gson().toJson(params), LOGGER);
		try {
			SaxsProposal3Service saxsProposalService = this.getSaxsProposal3Service();
			Macromolecule3VO macromolecule3VO = this.getGson().fromJson(macromolecule, Macromolecule3VO.class);
			this.logFinish("saveMacromolecule", start, LOGGER);
			return this.sendResponse(saxsProposalService.merge(macromolecule3VO));
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "saveMacromolecule", start, System.currentTimeMillis(),
					e.getMessage(), e);
		}
		return null;
	}
}
