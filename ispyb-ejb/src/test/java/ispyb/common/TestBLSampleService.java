package ispyb.common;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.autoproc.SpaceGroup3Service;
import ispyb.server.mx.services.sample.Crystal3Service;
import ispyb.server.mx.vos.autoproc.SpaceGroup3VO;


public class TestBLSampleService {

	public TestBLSampleService() throws Exception {
		super();
		initWebService();
	}

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private static Crystal3Service crystal;

	private static  SpaceGroup3Service spaceGroupService;
	
	private static InitialContext ctx;
	
	private static void initWebService() throws Exception {
		
	    final Properties env = new Properties();  
	    env.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");  
	    
	    env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");  
	    env.put(Context.PROVIDER_URL, "remote://localhost:8080");  
	    //env.put(Context.SECURITY_PRINCIPAL, "");  
	    //env.put(Context.SECURITY_CREDENTIALS, "");  
	    env.put("jboss.naming.client.ejb.context", true);  
	  
	    ctx = new InitialContext(env);  
	    System.out.println("context initialized successfully.");  

		System.out.println("-----------------------------------------------------------");
		crystal = (Crystal3Service) ejb3ServiceLocator.getLocalService(Crystal3Service.class);
		spaceGroupService = (SpaceGroup3Service) ejb3ServiceLocator.getLocalService(SpaceGroup3Service.class);
	}

	public static void main(String args[]) {
		try {
			System.out.println("*************** testBLSampleWebServices ***************");
			initWebService();
			testFindBySpaceGroupShortName();

		} catch (Exception e) {
			System.err.println(e.toString());
			e.printStackTrace();
		}
	}
	
	public static void testFindBySpaceGroupShortName() throws Exception {
		System.out.println("*************** testfindBySpaceGroupShortName ***************");
		String currSpaceGroup = "P1";
		
		long start = System.currentTimeMillis();
		List<SpaceGroup3VO> sps = spaceGroupService.findBySpaceGroupShortName(currSpaceGroup);
		if (sps != null) {
			System.out.println("Space groups = " + sps.toString() + "\n");
		} else
			System.out.println("This is what I got as a response : NULL \n");
		long end = System.currentTimeMillis();
		System.out.println("testGetSampleInfo in " + (end - start) + " ms.");
	}

}
