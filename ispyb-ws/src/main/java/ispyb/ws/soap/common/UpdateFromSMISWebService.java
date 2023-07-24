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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.jboss.ws.api.annotation.WebContext;

import ispyb.common.util.Constants;
import ispyb.server.common.services.admin.AdminVar3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.admin.AdminVar3VO;
import ispyb.server.mx.vos.collections.SessionWS3VO;
import ispyb.server.smis.UpdateFromSMIS;
import ispyb.ws.ParentWebService;

/**
 * Web services for update from SMIS
 * 
 * @author BODIN
 * 
 */
@WebService(name = "UpdateFromSMISWebService", serviceName = "ispybWS", targetNamespace = "http://ispyb.ejb3.webservices.batchUpdate")
@SOAPBinding(style = Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Stateless
@RolesAllowed({ "User", "WebService" })
// allow special access roles
@SecurityDomain("ispyb")
@WebContext(authMethod = "BASIC", secureWSDLAccess = false, transportGuarantee = "NONE")

public class UpdateFromSMISWebService extends ParentWebService{
	private final static Logger logger = Logger.getLogger(UpdateFromSMISWebService.class);
	
	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	static String DATE_FORMAT = "dd/MM/yyyy";
	
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
	@TransactionTimeout(5000)
	public void updateFromSMIS() throws Exception {
		String methodName = "updateFromSMIS";
		long id = this.logInit(methodName, logger);
		try {
			Calendar cal = Calendar.getInstance();
			Date today = cal.getTime();
			Integer nbDays = 4;
			
			AdminVar3Service adminVarService = (AdminVar3Service) ejb3ServiceLocator
					.getLocalService(AdminVar3Service.class);

			AdminVar3VO adminVar = adminVarService.findByPk(Constants.UPDATE_DAILY_NB_DAYS_PK);
			if (adminVar != null)
				nbDays = new Integer(adminVar.getValue());
			
			cal.add(Calendar.DATE, -nbDays);
			Date startDate = cal.getTime();

			SimpleDateFormat simple = new SimpleDateFormat("dd/MM/yyyy");
			UpdateFromSMIS.updateFromSMIS(simple.format(startDate), simple.format(today));
			
		} catch (Exception e) {
			this.logError(methodName, e, id, logger);
		}
	}
	
	/**
	 * update from SMIS
	 * 
	 * @throws Exception
	 */
	@WebMethod
	@TransactionTimeout(1000)
	public void updateProposalFromSMIS(@WebParam(name = "code") String code, 
			@WebParam(name = "number") String number) throws Exception {
			
		String methodName = "updateProposalFromSMIS";
		long id = this.logInit(methodName, logger);
		try {
			UpdateFromSMIS.updateProposalFromSMIS(code, number);
			
		} catch (Exception e) {
			this.logError(methodName, e, id, logger);
		}
	}

	
	@WebMethod
	public void updateFromSMISByDate(String start, String end) throws Exception {
		String methodName = "updateFromSMISByDate";
		long id = this.logInit(methodName, logger, start, end);
		 try {
			Date startDate = new SimpleDateFormat(DATE_FORMAT).parse(start);
			Date endDate = new SimpleDateFormat(DATE_FORMAT).parse(end);
			UpdateFromSMIS.updateFromSMIS(new SimpleDateFormat(DATE_FORMAT).format(startDate), new SimpleDateFormat(DATE_FORMAT).format(endDate));
			
		} catch (ParseException e) {
			this.logError(methodName, e, id, logger);
		}
	}
}
