/** This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ISPyB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
 ****************************************************************************************************/

package ispyb.ws.soap.em;

import ispyb.server.common.util.LoggerFormatter;

import java.util.HashMap;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ws.api.annotation.WebContext;
import com.google.gson.Gson;

@WebService(name = "ToolsForEMWebService", serviceName = "ispybWS", targetNamespace = "http://ispyb.ejb3.webservices.em")
@SOAPBinding(style = Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Stateless
@RolesAllowed({ "WebService", "User", "Industrial" })
@SecurityDomain("ispyb")
@WebContext(authMethod = "BASIC", secureWSDLAccess = false, transportGuarantee = "NONE")
public class ToolsForEMDataCollection extends EMDataCollection{
	
	protected Logger log = LoggerFactory.getLogger(ToolsForEMDataCollection.class);
	

	@WebMethod(operationName = "addMovie")
	public void addMovie(
			@WebParam(name = "proposal") String proposal,
			@WebParam(name = "sampleAcronym") String sampleAcronym,
			@WebParam(name = "imageDirectory") String imageDirectory,
			@WebParam(name = "jpeg") String jpeg,
			@WebParam(name = "mrc") String mrc,
			@WebParam(name = "xml") String xml
			)	
	{
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("proposal", String.valueOf(proposal));
			params.put("sampleAcronym", String.valueOf(sampleAcronym));
			params.put("imageDirectory", String.valueOf(imageDirectory));
			params.put("jpeg", String.valueOf(jpeg));
			params.put("mrc", String.valueOf(mrc));
			params.put("xml", String.valueOf(xml));
			
			log.info("addMovie. parameters={} ", new Gson().toJson(params));
			
		} catch (Exception exp) {
			log.error("Error. parameters={} ", exp.getMessage());
			exp.printStackTrace();
		}

//		try {
//
//		} catch (Exception e) {
//			LoggerFormatter.log(this.log, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "addMovie", id,
//					System.currentTimeMillis(), e.getMessage(), e);
//		}
	}

	
}