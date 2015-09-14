package ispyb.ws.rest.proposal.shipping;

import ispyb.server.common.services.login.Login3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

import java.util.HashMap;

import javax.naming.NamingException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.util.GenericType;
import org.junit.Test;


public class ShippingUnitTest  {
	
	protected Login3Service getLogin3Service() throws NamingException {
		return (Login3Service) Ejb3ServiceLocator.getInstance().getLocalService(
				Login3Service.class);
	}
	@Test
	public void testAuthenticate(){
		 	DefaultHttpClient httpclient = new DefaultHttpClient();
		    try {
		      String login = "mx415";
		      String password="pimx415";
		      
		      String baseUri = "http://ispyvalid.esrf.fr:8080/ispyb-ws/rest/authenticate";
		      
		      ClientRequest request = new ClientRequest(baseUri);
		      request.formParameter("login", login).formParameter("password", password);

		      ClientResponse<HashMap> result = request.post(HashMap.class);
		      System.out.println("executing request to authenticate with : " + baseUri);
		      
		      if (result != null) {		 
		    	  System.out.println("----------------------------------------");
		    	  System.out.println(result.getStatusInfo());
		    	  System.out.println("----------------------------------------");
		    	  String token = this.getLogin3Service().findBylastByLogin(login).getToken();
		    	  System.out.println(token);
		      }
		 
		    } catch (Exception e) {
		      e.printStackTrace();
		    } finally {
		      // When HttpClient instance is no longer needed,
		      // shut down the connection manager to ensure
		      // immediate deallocation of all system resources
		      httpclient.getConnectionManager().shutdown();
		    }
	}

	@Test
	public void test(){
		 	DefaultHttpClient httpclient = new DefaultHttpClient();
		    try {
		      // specify the host, protocol, and port
		      HttpHost target = new HttpHost("ispyvalid.esrf.fr", 8080, "http");
		       
		      // specify the get request
		      HttpGet getRequest = new HttpGet("/ispyb-ws/rest/61dabe6a40a7b0e73b3551fcca642f5df386b061/proposal/opd29/shipping/list");
		 
		      System.out.println("executing request to " + target);
		 
		      HttpResponse httpResponse = httpclient.execute(target, getRequest);
		      HttpEntity entity = httpResponse.getEntity();
		 
		      System.out.println("----------------------------------------");
		      System.out.println(httpResponse.getStatusLine());
		      Header[] headers = httpResponse.getAllHeaders();
		      for (int i = 0; i < headers.length; i++) {
		        System.out.println(headers[i]);
		      }
		      System.out.println("----------------------------------------");
		 
		      if (entity != null) {
		        System.out.println(EntityUtils.toString(entity));
		      }
		 
		    } catch (Exception e) {
		      e.printStackTrace();
		    } finally {
		      // When HttpClient instance is no longer needed,
		      // shut down the connection manager to ensure
		      // immediate deallocation of all system resources
		      httpclient.getConnectionManager().shutdown();
		    }
	}
}
