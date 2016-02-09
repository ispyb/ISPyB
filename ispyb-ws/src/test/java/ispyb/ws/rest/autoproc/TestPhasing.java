/*******************************************************************************
 * Copyright (c) 2004-2015
 * Contributors: L. Armanet, C. Borges, M. Camerlenghi, L. Cardonne,
 *               S. Delageniere, L. Duparchy, A. Madeira, S. Ohlsson,
 *               P. Pascal, N. Salgueiro, I. Schneider, S.Schulze,
 *               I. Tocquet, F. Torres
 * 
 * This file is part of the ESRF User Portal.
 * 
 * The ESRF User Portal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * The ESRF User Portal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with the ESRF User Portal. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package ispyb.ws.rest.autoproc;

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

import ispyb.server.common.services.login.Login3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

public class TestPhasing {
	
	protected static Login3Service getLogin3Service() throws NamingException {
		return (Login3Service) Ejb3ServiceLocator.getInstance().getLocalService(
				Login3Service.class);
	}
	
	private final static String PREFIX = "/ispyb/ispyb-ws/rest/";
	
	public static void main(String args[]) {
		try {
			System.out.println("*************** testAutoprocessingWebServices ***************");
			//String token = getToken();
			String token = "53175c55148771801ff02fff67c3ae1be060efc9";
			System.out.println("----------------------------------------");
			testShippingList(token);
			//testStorePhasingStep();

		} catch (Exception e) {
			System.err.println(e.toString());
			e.printStackTrace();
		}
	}
		
		public static String getToken(){
			 	DefaultHttpClient httpclient = new DefaultHttpClient();
			 	String token = null;
			    try {
			      String login = "mx415";
			      String password="pimx415";			      			      
			      String baseUri = "http://ispyvalid.esrf.fr:8080/ispyb/ispyb-ws/rest/authenticate";
			      
			      ClientRequest request = new ClientRequest(baseUri);
			      request.formParameter("login", login).formParameter("password", password);

			      ClientResponse<HashMap> result = request.post(HashMap.class);
			      System.out.println("executing request to authenticate with : " + baseUri);
			      
			      if (result != null) {		 
			    	  System.out.println("----------------------------------------");
			    	  System.out.println(result.getStatusInfo());
			    	  System.out.println("----------------------------------------");
			    	  token = getLogin3Service().findBylastByLogin(login).getToken();
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
			    return token;
		}

		public static void testShippingList(String token){
			 	DefaultHttpClient httpclient = new DefaultHttpClient();
			    try {
			      // specify the host, protocol, and port
			      HttpHost target = new HttpHost("ispyvalid.esrf.fr", 8080, "http");
			      String request = "/proposal/opd29/shipping/list";
			      // specify the get request
			      HttpGet getRequest = new HttpGet(PREFIX + token + request);
			 
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
		
		public static void testStorePhasingStep(String token){
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
