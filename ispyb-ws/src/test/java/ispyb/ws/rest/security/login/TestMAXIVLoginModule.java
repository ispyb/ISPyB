package ispyb.ws.rest.security.login;

import ispyb.ws.rest.security.login.MAXIVLoginModule;

public class TestMAXIVLoginModule {

	public static void main(String args[]) {
		testAuthenticate();
	}
	
	public static void testAuthenticate()
	{
		try{
			MAXIVLoginModule.authenticate("hogbom","PassWord!");
		}
		catch(Exception ex){
			
		}
	}
	
}