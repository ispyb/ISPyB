package ispyb.ws.rest;

import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;

import java.lang.reflect.Modifier;
import java.util.List;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RestWebService {
	protected Gson getGson(){
		return new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create();
	}
	
	protected Response sendResponse(String response){
		return Response.ok(response).header("Access-Control-Allow-Origin", "*").build();
	}
	
	protected Response sendResponse(Object response){
		return Response.ok(getGson().toJson(response)).header("Access-Control-Allow-Origin", "*").build();
	}
	
	protected Response unauthorizedResponse(){
		return Response.status(401).build();
	}
	
	protected int getProposalId(String proposal) throws Exception{
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		SaxsProposal3Service saxsProposalService = (SaxsProposal3Service) ejb3ServiceLocator.getLocalService(SaxsProposal3Service.class);
		List<Proposal3VO> proposals = saxsProposalService.findProposalByLoginName(proposal);
		if (proposals != null){
			if (proposals.size() > 0){
				return proposals.get(0).getProposalId();
			}
		}
		throw new Exception("No proposal found.");
	}
}
