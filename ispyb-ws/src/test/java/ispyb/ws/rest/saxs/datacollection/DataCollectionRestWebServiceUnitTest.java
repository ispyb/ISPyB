package ispyb.ws.rest.saxs.datacollection;

import javax.naming.NamingException;

import ispyb.server.common.services.shipping.external.External3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.ws.rest.RestWebService;
import ispyb.ws.rest.WebServiceUnitTest;
import ispyb.ws.rest.saxs.DataCollectionRestWebService;

import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RMISecurityManager;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

public class DataCollectionRestWebServiceUnitTest extends WebServiceUnitTest{

//	@Test
//
	public void listWebMethod() {
//		this.run("/{token}/proposal/{proposal}/saxs/datacollection/list", DataCollectionRestWebService.class);
		this.run("/configuration", DataCollectionRestWebService.class);
	}
	
//	@Test
	public void authentication() {
//		this.run("/{token}/proposal/{proposal}/saxs/datacollection/list", DataCollectionRestWebService.class);
		System.out.println(this.authenticate("mx415", ""));
	}
	
}
