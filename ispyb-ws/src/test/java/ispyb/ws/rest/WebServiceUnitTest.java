package ispyb.ws.rest;

import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.ws.rest.security.AuthenticationRestWebService;

import java.io.File;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.rmi.RMISecurityManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.security.auth.login.LoginContext;
import javax.servlet.http.HttpServletResponse;

import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.plugins.server.resourcefactory.POJOResourceFactory;
import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

public class WebServiceUnitTest {

	protected static Properties properties;
	protected static Ejb3ServiceLocator serviceLocator;
	
	protected static String RANDOM_ACRONYM;
	protected static SimpleDateFormat DATEFORMAT;
	protected static Calendar NOW;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
//		Properties ispybProperties = new Properties();
//		ispybProperties.load(new FileReader(new File("build.properties")));
//		String jbossPath =  ispybProperties.getProperty("jboss.home") + "/server/default/conf/testJAAS.config";
//		String jbossPolicy =  ispybProperties.getProperty("jboss.home") + "/server/default/conf/standaloneClient.policy";
		
//		System.out.println("Setting environment up. Fix path is required " + jbossPath);
		
//		System.setProperty("java.security.auth.login.config", jbossPath);
//        UsernamePasswordHandler handler = null;
//        String username = "mx9999";
//        String password = "mxtest";
//        handler = new UsernamePasswordHandler(username, password);
//        LoginContext lc = new LoginContext("testEJB", handler);
//        lc.login();
////        System.setProperty("java.security.policy", jbossPolicy);
//        System.setSecurityManager(new RMISecurityManager());
//        properties = new Properties();
//		properties.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
//        properties.put("java.naming.factory.url.pkgs", "=org.jboss.naming:org.jnp.interfaces");
//        properties.put("java.naming.provider.url", "localhost:1099");
//        serviceLocator = Ejb3ServiceLocator.getInstance(); 
//        
//        
//        DATEFORMAT = new SimpleDateFormat("hh:mm:ss");
//		NOW = GregorianCalendar.getInstance();
		
	}
	
	
	private String lengthfyLine(int line, int numMax){
		return lengthfyLine(String.valueOf(line), numMax);
	}
	
	private String lengthfyLine(String line, int numMax){
		if (line.length() <  numMax){
			while (line.length() <  numMax) {
				line = line + " ";
			}
		}
		return line;
		
	}
	@Before
	public void before(){
		System.out.println("");
	}
	
	@AfterClass
	public static void afterClass(){
		System.out.println("");
	}
	
	
	
//	@Test
	public String authenticate(String user, String password){
		String path = "/authenticate";
		
		Dispatcher dispatcher = MockDispatcherFactory.createDispatcher();
		POJOResourceFactory noDefaults = new POJOResourceFactory(AuthenticationRestWebService.class);
		dispatcher.getRegistry().addResourceFactory(noDefaults);

		MockHttpRequest request;
		try {
			request = MockHttpRequest.post(path);
			
			request.addFormHeader("login", user);
			request.addFormHeader("password", password);
			request.addFormHeader("site", "ESRF");

			MockHttpResponse response = new MockHttpResponse();
			dispatcher.invoke(request, response);
			
			return response.getContentAsString();
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return "";
		
	}
	
	
	
	protected String run(String path, Class<?> clazz){
		Dispatcher dispatcher = MockDispatcherFactory.createDispatcher();
		POJOResourceFactory noDefaults = new POJOResourceFactory(clazz);
		dispatcher.getRegistry().addResourceFactory(noDefaults);

		MockHttpRequest request;
		try {
			request = MockHttpRequest.get(path);
			MockHttpResponse response = new MockHttpResponse();

			dispatcher.invoke(request, response);
			System.out.print(lengthfyLine(request.getHttpMethod(), 10) + "|" + lengthfyLine(request.getUri().getPath(), 150));
			Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
			System.out.print("|" + lengthfyLine(response.getStatus(), 10) + "PASSED");
			
			return response.getContentAsString();
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return "";
		
	}
}
