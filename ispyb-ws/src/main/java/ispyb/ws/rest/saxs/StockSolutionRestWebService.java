package ispyb.ws.rest.saxs;

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

import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.vos.dataAcquisition.StockSolution3VO;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.vos.proposals.Proposal3VO;

@Path("/")
public class StockSolutionRestWebService extends SaxsRestWebService {
	
	private final static Logger logger = Logger.getLogger(StockSolutionRestWebService.class);
	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposalId}/saxs/stocksolution/list")
	@Produces({ "application/json" })
	public Response getStockSolutions(
			@PathParam("token") String token, 
			@PathParam("proposalId") String login) throws Exception {
	    
		String methodName = "getStockSolutions";
		long start = this.logInit(methodName, logger, token, login);
		try {
        		SaxsProposal3Service saxsProposalService = this.getSaxsProposal3Service();
        		Proposal3Service proposalService = this.getProposal3Service();
        			List<Proposal3VO> proposals = proposalService.findProposalByLoginName(login);
        		List<StockSolution3VO> stockSolution3VO = new ArrayList<StockSolution3VO>();
        		for (Proposal3VO proposal3vo : proposals) {
        		    stockSolution3VO.addAll(saxsProposalService.findStockSolutionsByProposalId(proposal3vo.getProposalId()));
        		}
        		this.logFinish(methodName, start, logger);
        		return this.sendResponse(stockSolution3VO);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@POST
	@Path("{token}/proposal/{proposalId}/saxs/stocksolution/save")
	@Produces({ "application/json" })
	public Response saveStockSolution(
			@PathParam("token") String token, 
			@PathParam("proposalId") String proposalId,
			@FormParam("stocksolution") String stocksolution) throws Exception {
		
		
		String methodName = "saveStockSolution";
		long start = this.logInit(methodName, logger, token, stocksolution);
		try {
			SaxsProposal3Service saxsProposalService = this.getSaxsProposal3Service();
			StockSolution3VO stockSolution3VO = this.getGson().fromJson(stocksolution, StockSolution3VO.class);
			this.logFinish(methodName, start, logger);
			return this.sendResponse(saxsProposalService.merge(stockSolution3VO));
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	
}
