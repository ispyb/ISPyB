/*************************************************************************************************
 * This file is part of ISPyB.
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
package ispyb.ws.soap.common;

import ispyb.server.smis.UpdateFromSMIS;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ws.api.annotation.WebContext;

/**
 * Web services for update from SMIS
 * 
 * @author BODIN
 * 
 */
@WebService(name = "UpdateFromSMISWebService", serviceName = "ispybWS", targetNamespace = "http://ispyb.ejb3.webservices.batchUpdate")
@SOAPBinding(style = Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Stateless
@RolesAllowed({ "WebService" })
// allow special access roles
@SecurityDomain("ispyb")
@WebContext(authMethod = "BASIC", secureWSDLAccess = false, transportGuarantee = "NONE")
public class UpdateFromSMISWebService {
	private final static Logger LOG = Logger.getLogger(UpdateFromSMISWebService.class);

	@WebMethod(operationName = "echo")
	@WebResult(name = "echo")
	public String echo() {
		return "echo from server...";
	}

	/**
	 * update from SMIS
	 * 
	 * @throws Exception
	 */
	@WebMethod
	public void updateFromSMIS() throws Exception {

		Date today = Calendar.getInstance().getTime();

		// better to do it over more than 1 day, to be sure to recover all
		// that's why a day is 26h long !

		long yesterdayL = today.getTime() - (26 * 3600 * 1000);
		Date yesterday = new Date(yesterdayL);

		SimpleDateFormat simple = new SimpleDateFormat("dd/MM/yyyy");
		String endDateStr = simple.format(today);
		String startDateStr = simple.format(yesterday);

		if (startDateStr == null || startDateStr.length() == 0) {
			startDateStr = simple.format(today);
		}
		UpdateFromSMIS.updateFromSMIS(startDateStr, endDateStr);
	}

}
