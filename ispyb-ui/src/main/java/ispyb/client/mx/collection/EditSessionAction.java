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
package ispyb.client.mx.collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.client.ParentIspybAction;
import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.util.DBConstants;
import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.vos.collections.Session3VO;

/**
 * 
 * 
 * @struts.action name="viewSessionForm" path="/user/editSession" input="user.collection.viewSession.page"
 *                validate="false" parameter="reqCode" scope="request"
 * @struts.action-forward name="error" path="site.default.error.page"
 * @struts.action-forward name="success" path="user.collection.editSession.page"
 * @struts.action-forward name="fedexmanagerEditSessionPage" path="fedexmanager.collection.editSession.page"
 * @struts.action-forward name="managerEditSessionPage" path="manager.collection.editSession.page"
 * @struts.action-forward name="localContactEditSessionPage" path="localContact.collection.editSession.page"
 * 
 * 
 */
public class EditSessionAction extends ParentIspybAction {

	private Session3Service sessionService;

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private final static Logger LOG = Logger.getLogger(EditSessionAction.class);

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

	/**
	 * To display all the Sessions belonging to a proposalId.
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {

		ActionMessages errors = new ActionMessages();

		try {
			// Integer sessionId = new Integer(request.getParameter(Constants.SESSION_ID));
			Integer sessionId;
			if (request.getParameter(Constants.SESSION_ID) != null) {
				sessionId = new Integer(request.getParameter(Constants.SESSION_ID));
			} else {
				// gets the session after a cancel on an editSession
				ViewSessionForm form = (ViewSessionForm) actForm;
				sessionId = form.getTheSessionId();
			}

			ViewSessionForm form = (ViewSessionForm) actForm;

			Session3VO sv = sessionService.findByPk(sessionId, false, false, false);

			BreadCrumbsForm.getItClean(request).setSelectedSession(sv);

			String structureDeterminations = sv.getStructureDeterminations() == null
					|| sv.getStructureDeterminations() == 0 ? "" : sv.getStructureDeterminations().toString();
			String dewarTransport = sv.getDewarTransport() == null || sv.getDewarTransport() == 0 ? "" : sv
					.getDewarTransport().toString();
			String dataBackupFrance = sv.getDatabackupFrance() == null || sv.getDatabackupFrance() == 0 ? "" : sv
					.getDatabackupFrance().toString();
			String dataBackupEurope = sv.getDatabackupEurope() == null || sv.getDatabackupEurope() == 0 ? "" : sv
					.getDatabackupEurope().toString();

			String beamLineOperator = sv.getBeamlineOperator();
			String comments = sv.getComments();

			// gets the email address for the beamline operator
			// Get localContact email
			String beamLineOperatorEmail = sv.getBeamLineOperatorEmail();
			// Populate with Info
			form.setBeamLineOperator(beamLineOperator);
			form.setBeamLineOperatorEmail(beamLineOperatorEmail);
			form.setComments(comments);
			form.setSessionTitle(sv.getSessionTitle());
			form.setStructureDeterminations(structureDeterminations);
			form.setDewarTransport(dewarTransport);
			form.setDataBackupFrance(dataBackupFrance);
			form.setDataBackupEurope(dataBackupEurope);
			form.setTheSessionId(sessionId);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

		} catch (AccessDeniedException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			return accessDeniedPage(mapping, actForm, request, in_reponse);


		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.collection.viewSession"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		} else
			// return mapping.findForward("success");
			return redirectPageFromRole(mapping, request);
	}

	public ActionForward updateSession(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		try {
			LOG.debug("update session parameters");

			ViewSessionForm form = (ViewSessionForm) actForm;

			Integer sessionId = form.getTheSessionId();
			LOG.debug("sessionId = " + sessionId);

			Session3VO sv = sessionService.findByPk(sessionId, false, false, false);
			// SessionLightAndPersonValue spv = session.findByPrimaryKeyLightAndPerson(sessionId);

			boolean isok = checkSessionInformation(form, request);
			if (!isok) {
				// return mapping.findForward("success");
				return redirectPageFromRole(mapping, request);
			}

			sv.setBeamlineOperator(form.getBeamLineOperator());
			sv.setComments(form.getComments());
			// spv.setBeamLineOperator(form.getBeamLineOperator());
			// spv.setComments(form.getComments());

			String sessionTitle = form.getSessionTitle();
			String st = form.getStructureDeterminations();
			float structureDeterminations = 0;
			if (st != null && !st.trim().equals("")) {
				try {
					structureDeterminations = new Float(st);
				} catch (NumberFormatException e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
				}
			}
			String dt = form.getDewarTransport();
			float dewarTransport = 0;
			if (dt != null && !dt.trim().equals("")) {
				try {
					dewarTransport = new Float(dt);
				} catch (NumberFormatException e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
				}
			}
			String dbf = form.getDataBackupFrance();
			float dataBackupFrance = 0;
			if (dbf != null && !dbf.trim().equals("")) {
				try {
					dataBackupFrance = new Float(dbf);
				} catch (NumberFormatException e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
				}
			}
			String dbe = form.getDataBackupEurope();
			float dataBackupEurope = 0;
			if (dbe != null && !dbe.trim().equals("")) {
				try {
					dataBackupEurope = new Float(dbe);
				} catch (NumberFormatException e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
				}
			}
			sv.setSessionTitle(sessionTitle);
			sv.setStructureDeterminations(structureDeterminations);
			sv.setDewarTransport(dewarTransport);
			sv.setDatabackupFrance(dataBackupFrance);
			sv.setDatabackupEurope(dataBackupEurope);

			sessionService.update(sv);
			LOG.debug("update session parameters:done");

			// redirect to view the last sessions
			response.sendRedirect(request.getContextPath() + "/user/viewSession.do?reqCode=displayLast");

		} catch (AccessDeniedException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.info(Constants.ACCESS_DENIED);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}

		return null;
	}

	/**
	 * checks if the session information in the form are correct and well formated, returns false if at least one field
	 * is incorrect
	 * 
	 * @param form
	 * @return
	 */
	private boolean checkSessionInformation(ViewSessionForm form, HttpServletRequest request) {
		LOG.debug("checkSessionInformatio");
		ActionMessages l_ActionMessages = new ActionMessages();
		boolean isOk = true;
		// beamline operator
		if (form.getBeamLineOperator() != null
				&& form.getBeamLineOperator().length() > DBConstants.MAX_LENGTH_SESSION_BEAMLINEOPERATOR) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "BeamLine Operator",
					DBConstants.MAX_LENGTH_SESSION_BEAMLINEOPERATOR);
			l_ActionMessages.add("sessionBeamLineOperator", l_ActionMessageLabel);
		}
		// beamline comments
		if (form.getComments() != null && form.getComments().length() > DBConstants.MAX_LENGTH_SESSION_COMMENTS) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Comments",
					DBConstants.MAX_LENGTH_SESSION_COMMENTS);
			l_ActionMessages.add("sessionComments", l_ActionMessageLabel);
		}
		// session title
		if (form.getSessionTitle() != null
				&& form.getSessionTitle().length() > DBConstants.MAX_LENGTH_SESSION_SESSIONTITLE) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Session title",
					DBConstants.MAX_LENGTH_SESSION_SESSIONTITLE);
			l_ActionMessages.add("sessionTitle", l_ActionMessageLabel);
		}
		// structure determinations
		if (form.getStructureDeterminations() != null && form.getStructureDeterminations().trim().length() > 0) {
			try {
				float f = Float.parseFloat(form.getStructureDeterminations());
			} catch (NumberFormatException ex) {
				isOk = false;
				ActionMessage l_ActionMessageLabel = new ActionMessage("errors.float", "Structure determinations");
				l_ActionMessages.add("structureDeterminations", l_ActionMessageLabel);
			}
		}
		// dewar transport
		if (form.getDewarTransport() != null && form.getDewarTransport().trim().length() > 0) {
			try {
				float f = Float.parseFloat(form.getDewarTransport());
			} catch (NumberFormatException ex) {
				isOk = false;
				ActionMessage l_ActionMessageLabel = new ActionMessage("errors.float", "Dewar transport");
				l_ActionMessages.add("dewarTransport", l_ActionMessageLabel);
			}
		}
		// data backup france
		if (form.getDataBackupFrance() != null && form.getDataBackupFrance().trim().length() > 0) {
			try {
				float f = Float.parseFloat(form.getDataBackupFrance());
			} catch (NumberFormatException ex) {
				isOk = false;
				ActionMessage l_ActionMessageLabel = new ActionMessage("errors.float",
						"Data backup & express delivery France");
				l_ActionMessages.add("dataBackupFrance", l_ActionMessageLabel);
			}
		}
		// data backup europe
		if (form.getDataBackupEurope() != null && form.getDataBackupEurope().trim().length() > 0) {
			try {
				float f = Float.parseFloat(form.getDataBackupEurope());
			} catch (NumberFormatException ex) {
				isOk = false;
				ActionMessage l_ActionMessageLabel = new ActionMessage("errors.float",
						"Data backup & express delivery Europe");
				l_ActionMessages.add("dataBackupEurope", l_ActionMessageLabel);
			}
		}
		if (!isOk) {
			request.setAttribute("org.apache.struts.action.ERROR", l_ActionMessages);
		}
		return isOk;
	}

	private ActionForward redirectPageFromRole(ActionMapping mapping, HttpServletRequest request) {
		// redirection page according to the Role ------------------------

		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();

		if (role.equals(Constants.FXMANAGE_ROLE_NAME)) {
			return mapping.findForward("fedexmanagerEditSessionPage");
		} else if (role.equals(Constants.ROLE_MANAGER)) {
			return mapping.findForward("managerEditSessionPage");
		} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
			return mapping.findForward("localContactEditSessionPage");
		} else {
			return mapping.findForward("success");
		}
	}
	
	
}
