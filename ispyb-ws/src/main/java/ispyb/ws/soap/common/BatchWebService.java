/*******************************************************************************
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
 ******************************************************************************************************************************/

package ispyb.ws.soap.common;

import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.jboss.ws.api.annotation.WebContext;

import ispyb.server.common.services.admin.AdminVar3Service;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.admin.AdminVar3VO;
import ispyb.server.mx.vos.collections.SessionWS3VO;


/**
 * This session bean handles ISPyB Batch jobs.
 * */

@WebService(name = "BatchWebService", serviceName = "ispybWS", targetNamespace = "http://ispyb.ejb3.webservices.batch")
@SOAPBinding(style = Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Stateless
@RolesAllowed({ "WebService", "User", "Industrial" })
// allow special access roles
@SecurityDomain("ispyb")
@WebContext(authMethod = "BASIC", secureWSDLAccess = false, transportGuarantee = "NONE")
public class BatchWebService {

	private final static Logger LOG = Logger.getLogger(BatchWebService.class);

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private final Integer CONFIDENTIALITY_DELAY_PK = 10;

	private final Integer CONFIDENTIALITY_WINDOW_PK = 11;

	@WebMethod(operationName = "echo")
	@WebResult(name = "echo")
	public String echo() {
		return "echo from server...";
	}

	/**
	 * returns the sessions which are to be protected
	 * 
	 * @return
	 * @throws Exception
	 */
	@WebMethod
	@WebResult(name = "Sessions")
	public SessionWS3VO[] findSessionsToBeProtected() throws Exception {

		try {
			LOG.debug("findSessionsToBeProtected");
			Session3Service sessionService = (Session3Service) ejb3ServiceLocator
					.getLocalService(Session3Service.class);

			Integer delay = null;
			Integer window = null;

			AdminVar3Service adminVarService = (AdminVar3Service) ejb3ServiceLocator
					.getLocalService(AdminVar3Service.class);

			AdminVar3VO adminVar = adminVarService.findByPk(CONFIDENTIALITY_DELAY_PK);
			if (adminVar != null)
				delay = new Integer(adminVar.getValue());

			adminVar = adminVarService.findByPk(CONFIDENTIALITY_WINDOW_PK);
			if (adminVar != null)
				window = new Integer(adminVar.getValue());

			SessionWS3VO[] ret = sessionService.findForWSToBeProtected(delay, window);

			if (ret == null || ret.length < 1)
				return null;

			// return ret[0];
			LOG.debug("findSessionsToBeProtected finished with delay = " + delay + " and window = " + window);
			return ret;
		} catch (Exception e) {
			LOG.error("WS ERROR: findSessionsToBeProtected ");
			throw e;
		}
	}

	/**
	 * returns the sessions not protected which are to be protected
	 * 
	 * @return
	 * @throws Exception
	 */
	@WebMethod
	@WebResult(name = "Sessions")
	public SessionWS3VO[] findSessionsNotProtectedToBeProtected() throws Exception {

		try {
			LOG.debug("findSessionsNotProtectedToBeProtected");
			Session3Service sessionService = (Session3Service) ejb3ServiceLocator
					.getLocalService(Session3Service.class);

			SessionWS3VO[] ret = sessionService.findForWSNotProtectedToBeProtected(null, null);

			if (ret == null || ret.length < 1)
				return null;

			// return ret[0];
			LOG.debug("findSessionsNotProtectedToBeProtected finished.");
			return ret;
		} catch (Exception e) {
			LOG.error("WS ERROR: findSessionsNotProtectedToBeProtected ");
			throw e;
		}
	}
	
	/**
	 * returns the sessions not protected which are to be protected
	 * 
	 * @return
	 * @throws Exception
	 */
	@WebMethod
	@WebResult(name = "Sessions")
	public SessionWS3VO[] findSessionsNotProtectedToBeProtectedByDates(Date date1, Date date2) throws Exception {

		try {
			LOG.debug("findSessionsNotProtectedToBeProtectedByDates");
			Session3Service sessionService = (Session3Service) ejb3ServiceLocator
					.getLocalService(Session3Service.class);

			SessionWS3VO[] ret = sessionService.findForWSNotProtectedToBeProtected(date1, date2);

			if (ret == null || ret.length < 1)
				return null;

			// return ret[0];
			LOG.debug("findSessionsNotProtectedToBeProtectedByDates finished.");
			return ret;
		} catch (Exception e) {
			LOG.error("WS ERROR: findSessionsNotProtectedToBeProtectedByDates ");
			throw e;
		}
	}

	@WebMethod
	@TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
	@TransactionTimeout(3600)
	public void protectSession(Integer sessionId) throws Exception {

		try {
			if (sessionId != null) {
				Session3Service sessionService = (Session3Service) ejb3ServiceLocator
						.getLocalService(Session3Service.class);

				LOG.debug("session to be protected = " + sessionId);
				sessionService.protectSession(sessionId);
			}
		} catch (Exception e) {
			//
			LOG.error("WS ERROR: getDataToBeProtected " + sessionId);
			e.printStackTrace();
		}
	}
}
