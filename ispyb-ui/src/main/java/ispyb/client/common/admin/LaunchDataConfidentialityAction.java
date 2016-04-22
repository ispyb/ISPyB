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
 ******************************************************************************/
/*
 * 
 */
package ispyb.client.common.admin;

import ispyb.client.common.util.GSonUtils;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.vos.collections.SessionWS3VO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

/**
 * @struts.action name="launchDataConfidentialityForm" path="/manager/launchDataConfidentiality" input="manager.welcome.page"
 *                type="ispyb.client.common.admin.LaunchDataConfidentialityAction" validate="false" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * @struts.action-forward name="success" path="manager.admin.launchDataConfidentiality.page"
 * 
 */
public class LaunchDataConfidentialityAction extends DispatchAction {

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Session3Service sessionService;

	private final static Logger LOG = Logger.getLogger(LaunchDataConfidentialityAction.class);

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {
		this.sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse in_reponse) {

		return mapping.findForward("success");
	}

	public ActionForward launch(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse in_reponse) {

		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();

		LaunchDataConfidentialityForm form = (LaunchDataConfidentialityForm) actForm;

		SessionWS3VO[] sessionsToProtect;

		try {

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			Date date1 = formatter.parse(form.getDateFrom());
			Date date2 = formatter.parse(form.getDateTo());

			sessionsToProtect = sessionService.findForWSNotProtectedToBeProtected(date1, date2);
			if (sessionsToProtect != null) {
				for (int i = 0; i < sessionsToProtect.length; i++) {
					Integer sessionId = sessionsToProtect[i].getSessionId();
					sessionService.protectSession(sessionId);
					messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.updated", "sessionId"));
				}
			}

		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.manager.launchDataConfidentiality"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();

			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return (mapping.findForward("error"));
			}

			if (!messages.isEmpty())
				saveMessages(request, messages);
			LOG.debug("Launch data confidentiality is finished");
		}

		return mapping.findForward("success");
	}

	public ActionForward launchJs(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {

		List<String> errors = new ArrayList<String>();
		List<Integer> sessionIds = new ArrayList<Integer>();
		HashMap<String, Object> data = new HashMap<String, Object>();

		SessionWS3VO[] sessionsToProtect;

		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		df.setLenient(false);

		Date date1 = null;
		Date date2 = null;
		Integer sesId = null;

		String dateFrom = request.getParameter("dateFrom");
		String dateTo = request.getParameter("dateTo");
		String sesIdStr = request.getParameter("sesId");

		if (dateFrom != null && !dateFrom.equals("")) {
			try {
				date1 = df.parse(dateFrom);
			} catch (Exception e) {
				LOG.debug("Launch data confidentiality : error in parsing date");
			}
		}

		if (dateTo != null && !dateTo.equals("")) {
			try {
				date2 = df.parse(dateTo);
			} catch (Exception e) {
				LOG.debug("Launch data confidentiality : error in parsing date");
			}
		}

		if (sesIdStr != null)
			try {
				sesId = new Integer(sesIdStr);
			} catch (Exception e) {
				LOG.debug("Launch data confidentiality : error in parsing sessionId");
			}

		try {

			// if there is a valid sessionId protect only the session
			if (sesId != null) {
				sessionService.protectSession(sesId);
				sessionIds.add(sesId);
				LOG.debug("Launch data confidentiality is finished for session : " + sesId);
			} else {
				sessionsToProtect = sessionService.findForWSNotProtectedToBeProtected(date1, date2);
				if (sessionsToProtect != null) {
					for (int i = 0; i < sessionsToProtect.length; i++) {
						Integer sessionId = sessionsToProtect[i].getSessionId();
						sessionService.protectSession(sessionId);
						sessionIds.add(sessionId);
						LOG.debug("Launch data confidentiality is finished for session : " + sessionId);
					}
				}
			}

		} catch (Exception e) {
			errors.add(e.toString());
			data.put("errors", errors);
			// data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
			return null;
		}
		data.put("sessionIds", sessionIds);
		// data => Gson
		GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
		LOG.debug("Launch data confidentiality is finished");
		return null;
	}
}
