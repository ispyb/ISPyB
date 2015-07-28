package ispyb.ws.rest.saxs;

import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.vos.dataAcquisition.StockSolution3VO;
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
public class StockSolutionRestWebService extends RestWebService {
	
	private final static Logger LOGGER = Logger.getLogger(StockSolutionRestWebService.class);
	@PermitAll
	@GET
	@Path("{cookie}/proposal/{proposalId}/saxs/stocksolution/list")
	@Produces({ "application/json" })
	public Response getStockSolutions(
			@PathParam("cookie") String cookie, 
			@PathParam("proposalId") String login) throws Exception {
	    
	    	HashMap<String, String> params = new HashMap<String, String>();
		params.put("cookie", String.valueOf(cookie));
		params.put("login", String.valueOf(login));
		long start = this.logInit("getStockSolutions", new Gson().toJson(params), LOGGER);
		try {
        		SaxsProposal3Service saxsProposalService = this.getSaxsProposal3Service();
        			List<Proposal3VO> proposals = saxsProposalService.findProposalByLoginName(login);
        		List<StockSolution3VO> stockSolution3VO = new ArrayList<StockSolution3VO>();
        		for (Proposal3VO proposal3vo : proposals) {
        		    stockSolution3VO.addAll(saxsProposalService.findStockSolutionsByProposalId(proposal3vo.getProposalId()));
        		}
        		this.logFinish("getStockSolutions", start, LOGGER);
        		return this.sendResponse(stockSolution3VO);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "getStockSolutions", start, System.currentTimeMillis(),
					e.getMessage(), e);
		}
		return null;
	}
	

	@PermitAll
	@POST
	@Path("{cookie}/proposal/{proposalId}/saxs/stocksolution/save")
	@Produces({ "application/json" })
	public Response saveStockSolution(
			@PathParam("cookie") String cookie, 
			@PathParam("proposalId") String proposalId,
			@FormParam("stocksolution") String stocksolution) throws Exception {
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("cookie", String.valueOf(cookie));
		params.put("proposalId", String.valueOf(proposalId));
		params.put("stocksolution", String.valueOf(stocksolution));
		
		long start = this.logInit("saveStockSolution", new Gson().toJson(params), LOGGER);
		try {
			SaxsProposal3Service saxsProposalService = this.getSaxsProposal3Service();
			StockSolution3VO stockSolution3VO = this.getGson().fromJson(stocksolution, StockSolution3VO.class);
			this.logFinish("saveStockSolution", start, LOGGER);
			return this.sendResponse(saxsProposalService.merge(stockSolution3VO));
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "saveStockSolution", start, System.currentTimeMillis(),
					e.getMessage(), e);
		}
		return null;
	}
	
	
}
