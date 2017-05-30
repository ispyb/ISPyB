package ispyb.server.webservice.smis.util;

import java.util.Map;

import javax.xml.ws.BindingProvider;

import generated.ws.smis.SMISWebService;
import generated.ws.smis.SMISWebService_Service;
import ispyb.common.util.Constants;

public class SMISWebServiceGenerator {
	
	public static SMISWebService getSMISWebService()  {
		
		SMISWebService_Service sws = new SMISWebService_Service();	
		SMISWebService wsPort = sws.getSMISWebServiceBeanPort();

		//SMISWebService ws=service.get
		BindingProvider bindingProvider = (BindingProvider)wsPort;
		Map requestContext = bindingProvider.getRequestContext();
		
		//usern="*****";
		//userp="*****";
		requestContext.put(BindingProvider.USERNAME_PROPERTY, Constants.getUserSmisLoginName());
		requestContext.put(BindingProvider.PASSWORD_PROPERTY, Constants.getUserSmisPassword());
		
		return wsPort;
	}

	public static SMISWebService getWs() {
		if (Constants.SITE_IS_MAXIV()) {
			return new MAXIVWebService();
		} else {
			return getSMISWebService();
		}
	}

}
