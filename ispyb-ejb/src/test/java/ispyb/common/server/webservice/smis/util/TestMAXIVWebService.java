package ispyb.common.server.webservice.smis.util;

import ispyb.server.webservice.smis.util.MAXIVWebService;

public class TestMAXIVWebService {

	public static void main(String args[]) {
		//testAuthenticate();
		//testFindNewMXProposalPKs();
		//testFindMainProposersForProposal();
		//testFindParticipantsForProposal();
		testFindRecentSessionsInfoLightForProposalPkAndDays();
	}
	
	public static void testAuthenticate()
	{
		MAXIVWebService service = new MAXIVWebService();
		service.getToken();
	}
	
	public static void testFindNewMXProposalPKs()
	{
		MAXIVWebService service = new MAXIVWebService();
		service.findNewMXProposalPKs("01/01/2017", "30/07/2017");
	}
	
	public static void testFindMainProposersForProposal()
	{
		MAXIVWebService service = new MAXIVWebService();
		service.findMainProposersForProposal(new Long(20110001));
	}
	
	public static void testFindParticipantsForProposal()
	{
		MAXIVWebService service = new MAXIVWebService();
		try{
			service.findParticipantsForProposal(new Long(20110044));
		}catch(Exception ex){
			
		}
	}
	
	public static void testFindRecentSessionsInfoLightForProposalPkAndDays()
	{
		MAXIVWebService service = new MAXIVWebService();
		try{
			service.findRecentSessionsInfoLightForProposalPkAndDays(new Long(20110044),1);
		}catch(Exception ex){
			
		}
	}
}
