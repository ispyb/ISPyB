package ispyb.server.webservice.smis.util;

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
