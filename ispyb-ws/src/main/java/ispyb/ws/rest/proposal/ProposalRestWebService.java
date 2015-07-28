package ispyb.ws.rest.proposal;

import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.StockSolution3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Platetype3VO;
import ispyb.server.common.util.LoggerFormatter;
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

    private final static Logger LOGGER = Logger.getLogger(ProposalRestWebService.class);

//    @RolesAllowed({ "WebService", "User", "Industrial" })
//    @GET
//    @Path("{cookie}/group/list")
//    @Produces({ "application/json" })
//    public Response getGroupList(@PathParam("cookie") String cookie) throws Exception {
//	MenuGroup3Service menuGroupService = getMenuGroup3Service();
//	List<MenuGroup3VO> groupList = menuGroupService.findAll(false);
//	return this.sendResponse(groupList);
//    }
    
    @PermitAll
    @GET
    @Path("{cookie}/proposal/user/{login}/list")
    @Produces({ "application/json" })
    public Response getProposals(@PathParam("cookie") String cookie, @PathParam("login") String login) throws Exception {
	HashMap<String, String> params = new HashMap<String, String>();
	params.put("cookie", String.valueOf(cookie));
	params.put("login", String.valueOf(login));
	long id = this.logInit("getProposals", new Gson().toJson(params), LOGGER);
	try {
	    List<Proposal3VO> proposals = this.getSaxsProposal3Service().findProposalByLoginName(login);
	    this.logFinish("getProposals", id, LOGGER);
	    return this.sendResponse(proposals);
	    
	} catch (Exception e) {
	    e.printStackTrace();
	    LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "getProposals", id,
		    System.currentTimeMillis(), e.getMessage(), e);
	}
	return null;
    }

    @PermitAll
    @GET
    @Path("{cookie}/proposal/{proposal}/technique/{technique}/get")
    @Produces({ "application/json" })
    public Response listProposal(@PathParam("cookie") String cookie, @PathParam("proposal") String login) throws Exception {
	HashMap<String, String> params = new HashMap<String, String>();
	params.put("cookie", String.valueOf(cookie));
	params.put("login", String.valueOf(login));
	long id = this.logInit("listProposal", new Gson().toJson(params), LOGGER);
	
	try {
	    	ArrayList<HashMap<String, List<?>>> multiple = new ArrayList<HashMap<String, List<?>>>();
	    	int proposalId = this.getProposalId(login);
	    	
	    	HashMap<String, List<?>> results = new HashMap<String, List<?>>();
	    	
	    	long time = System.currentTimeMillis();
	    	
		List<Macromolecule3VO> macromolecules = this.getSaxsProposal3Service().findMacromoleculesByProposalId(proposalId);
		
		System.out.println("macromolecules: " + (System.currentTimeMillis() - time));
		time = System.currentTimeMillis();
		List<Buffer3VO> buffers = this.getSaxsProposal3Service().findBuffersByProposalId(proposalId);
		
		System.out.println("buffers: " + (System.currentTimeMillis() - time));
		time = System.currentTimeMillis();
		
		List<StockSolution3VO> stockSolutions = this.getSaxsProposal3Service().findStockSolutionsByProposalId(proposalId);
		System.out.println("stockSolutions: " + (System.currentTimeMillis() - time));
		time = System.currentTimeMillis();
		List<Platetype3VO> plateTypes = this.getPlateType3Service().findAll();
		System.out.println("plateTypes: " + (System.currentTimeMillis() - time));
		time = System.currentTimeMillis();
		List<Proposal3VO> proposals = new ArrayList<Proposal3VO>();
		proposals.add(this.getSaxsProposal3Service().findProposalById(proposalId));
		System.out.println("findProposalById: " + (time - System.currentTimeMillis())/1000);
		time = System.currentTimeMillis();
//		List<Session3VO> sessions = this.getSession3Service().findFiltered(proposalId, null, null, null, null, null, false, null);
//		List<Assembly3VO> assemblies = this.getSaxsProposal3Service().findAssembliesByProposalId(proposalId);
		List<LabContact3VO> labContacts = this.getLabContact3Service().findFiltered(proposalId, null);
//		List<Shipping3VO> shippings = this.getShipping3Service().findByProposal(proposalId, true);

		results.put("proposal", proposals);
		results.put("plateTypes", plateTypes);
//		results.put("sessions", sessions);
		results.put("macromolecules", macromolecules);
		results.put("buffers", buffers);
		results.put("stockSolutions", stockSolutions);
//		results.put("assemblies", assemblies);
		results.put("labcontacts", labContacts);
//		results.put("shippings", shippings);
		
		multiple.add(results);
		this.logFinish("listProposal", id, LOGGER);
		
	    return this.sendResponse(multiple);
	    
	} catch (Exception e) {
	    e.printStackTrace();
	    LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "listProposal", id,
		    System.currentTimeMillis(), e.getMessage(), e);
	}
	return null;
    }
    	
}
