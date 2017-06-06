package ispyb.common.server.webservice.smis.util;

import ispyb.server.webservice.smis.util.MAXIVWebService;

public class TestMAXIVWebService {

	public static void main(String args[]) {
		testAuthenticate();
	}
	
	public static void testAuthenticate()
	{
		MAXIVWebService service = new MAXIVWebService();
		service.getToken();
	}
	
}
