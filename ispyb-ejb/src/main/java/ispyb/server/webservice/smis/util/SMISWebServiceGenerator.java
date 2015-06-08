package ispyb.server.webservice.smis.util;

import generated.ws.smis.SMISWebService;
import generated.ws.smis.SMISWebService_Service;
import ispyb.common.util.Constants;

import javax.xml.ws.BindingProvider;
import java.util.Map;

public class SMISWebServiceGenerator {
	
	public static SMISWebService getSMISWebService()  {
		
		SMISWebService_Service sws = new SMISWebService_Service();	
		SMISWebService wsPort = sws.getSMISWebServiceBeanPort();

		//SMISWebService ws=service.get
		BindingProvider bindingProvider = (BindingProvider)wsPort;
		Map requestContext = bindingProvider.getRequestContext();
		String usern = Constants.SMIS_WS_USERNAME;
		String userp = Constants.SMIS_WS_PWD;
		
		//usern="*****";
		//userp="*****";
		
		requestContext.put(BindingProvider.USERNAME_PROPERTY, usern);
		requestContext.put(BindingProvider.PASSWORD_PROPERTY, userp);
		
		return wsPort;
	}

	public static SMISWebService getWs() {
		return getSMISWebService();
	}

}
