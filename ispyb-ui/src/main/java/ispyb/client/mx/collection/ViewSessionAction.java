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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.google.gson.Gson;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.client.ParentIspybAction;
import ispyb.client.SiteSpecific;
import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.help.SendReportAction;
import ispyb.client.common.util.Confidentiality;
import ispyb.client.common.util.DBConstants;
import ispyb.client.common.util.GSonUtils;
import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.common.util.Formatter;
import ispyb.common.util.beamlines.EMBLBeamlineEnum;
import ispyb.common.util.beamlines.ESRFBeamlineEnum;
import ispyb.common.util.beamlines.MAXIVBeamlineEnum;
import ispyb.common.util.beamlines.SOLEILBeamlineEnum;
import ispyb.common.util.beamlines.ALBABeamlineEnum;
import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.services.collections.DataCollectionGroup3Service;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.Session3VO;

/**
 * 
 * 
 * @struts.action name="viewSessionForm" path="/user/viewSession" input="user.collection.viewSession.page" validate="false"
 *                parameter="reqCode" scope="request"
 * @struts.action-forward name="error" path="site.default.error.page"
 * @struts.action-forward name="viewSession" path="user.collection.viewSession.page"
 * @struts.action-forward name="viewSessionJs" path="user.collection.viewSessionJs.page"
 * @struts.action-forward name="viewSessionManager" path="manager.collection.viewSession.page"
 * @struts.action-forward name="viewSessionFedexManager" path="fedexmanager.collection.viewSession.page" *
 * @struts.action-forward name="viewSessionLocalContact" path="localContact.collection.viewSession.page"
 * 
 */
public class ViewSessionAction extends ParentIspybAction {

	private Session3Service sessionService;

	private DataCollectionGroup3Service dataCollectionGroupService;

	private Proposal3Service proposalService;

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private final Logger LOG = Logger.getLogger(ViewSessionAction.class);

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {

		this.sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
		this.proposalService = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
		this.dataCollectionGroupService = (DataCollectionGroup3Service) ejb3ServiceLocator
				.getLocalService(DataCollectionGroup3Service.class);
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
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();

		Integer proposalId = null;
		boolean isManager = Confidentiality.isManager(request);
		boolean isLocalContact = Confidentiality.isLocalContact(request);

		Calendar today = Calendar.getInstance();
		// Set time to beginning of the day in order to get sessions that started today
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);

		try {
			BreadCrumbsForm.getItClean(request);
			proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			// ----------------------------------------------------------------------------------
			// for manager views, the proposalId can be given in request and is then stored in session
			if (isManager) {
				// if in request, use the request value
				String proposalIdst = request.getParameter(Constants.PROPOSAL_ID);

				// if not in request use the session value
				if (proposalIdst == null || proposalIdst.length() == 0)
					proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
				else
					proposalId = new Integer(proposalIdst);

				if (proposalId != null) {
					Proposal3VO plv = proposalService.findByPk(proposalId);

					request.getSession().setAttribute(Constants.PROPOSAL_ID, proposalId);

					BreadCrumbsForm.getIt(request).setSelectedProposal(plv);
				}
			}
			// ----------------------------------------------------------------------------------

			// update used flag to be able to retrieve only used sessions
			if (proposalId != null)
				sessionService.updateUsedSessionsFlag(proposalId);
			ViewSessionForm form = (ViewSessionForm) actForm;

			// ------------------------------- Retrieve parameter from form ---------------------
			String beamLineName = form.getBeamLineName();
			String beamLineNameFromList = form.getBeamLineNameFromList();
			if (beamLineName == null || beamLineName.trim().length() == 0) {
				beamLineName = beamLineNameFromList;
			}
			String startDateSt = form.getStartDatest();
			Timestamp startDate = Formatter.stringToTimestamp(startDateSt);
			String endDateSt = form.getEndDatest();
			Timestamp endDate = Formatter.stringToTimestamp(endDateSt);

			List<Session3VO> sessionList = null;

			// -------------------------------- Search ------------------------------------------

			if (isLocalContact) {
				String beamlineOperatorSiteNumber = (String) request.getSession().getAttribute(Constants.LDAP_siteNumber);
				if (beamLineName == null || beamLineName.length() == 0)
					beamLineName = null;
				Integer nbMax = null;
				if (form.getNbSessionsToDisplay() == Constants.NB_SESSIONS_TO_DISPLAY) {
					nbMax = Constants.NB_SESSIONS_TO_DISPLAY;
				}
				if (startDate == null && endDate == null) {
					sessionList = sessionService.findFiltered(null, nbMax, beamLineName, null, null, null, false,
							beamlineOperatorSiteNumber);
				} else {
					sessionList = sessionService.findFiltered(null, nbMax, beamLineName, null, startDate, endDate, false,
							beamlineOperatorSiteNumber);
				}
			} else if (proposalId == null && isManager) {
				if (beamLineName == null || beamLineName.length() == 0)
					beamLineName = null;
				if (startDate == null && endDate == null) {
					Date currentDate = Calendar.getInstance().getTime();
					sessionList = sessionService.findFiltered(null, null, beamLineName, null, currentDate, currentDate, false, null);
				} else {
					sessionList = sessionService.findFiltered(null, null, beamLineName, startDate, endDate, null, false, null);
				}
			} else {
				// for search by date
				// no name
				if (beamLineName == null || beamLineName.length() == 0) {
					// no dates
					if (startDate == null && endDate == null)
						if (Constants.SITE_IS_DLS() || Constants.SITE_IS_MAXIV()) {
							sessionList = sessionService.findFiltered(proposalId, null, null, null, null, null, true, null);
						} else {
							// TODO : limited number, only used + future sessions (including today)
							if (form.getNbSessionsToDisplay() == Constants.NB_SESSIONS_TO_DISPLAY) {
								sessionList = sessionService.findFiltered(proposalId, Constants.NB_SESSIONS_TO_DISPLAY, null, null,
										null, null, true, null);
								LOG.debug("sessions displayed limited : nb sessions = " + sessionList.size());
							} else {
								sessionList = sessionService.findFiltered(proposalId, null, null, null, null, null, true, null);
								LOG.debug("sessionsUsedForProposal: nb sessions = " + sessionList.size());
							}
						}
					else {
						sessionList = sessionService.findFiltered(proposalId, null, null, startDate, endDate, null, true, null);
					}

				}
				// name
				else {
					// Replace SQL wildcard
					beamLineName = beamLineName.replace('*', '%');
					// ID14 1 or ID14-1
					beamLineName = beamLineName.replace('-', '%');
					beamLineName = beamLineName.replace(' ', '%');

					sessionList = sessionService.findFiltered(proposalId, null, beamLineName, startDate, endDate, null, true, null);
				}
			}
			// TODO add several finders for session and sessionLight

			// ----------------------------- Populate the form -----------------------------
			// DLS ####
			if (Constants.SITE_IS_DLS()) {
				// If not manager etc., then user shouldn't see all sessions for this proposal:
				RoleDO role = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
				if (role.getName().equals("User")) {
					// !isManager && !request.isUserInRole("dls_dasc") && !request.isUserInRole("mx_staff")){
					String proposalCode = request.getSession().getAttribute(Constants.PROPOSAL_CODE).toString();
					String proposalNumber = String.valueOf(request.getSession().getAttribute(Constants.PROPOSAL_NUMBER));
					String proposal = proposalCode + proposalNumber;

					// Loop backwards through sessionList so that sessions can be removed safely:
					for (int i = sessionList.size() - 1; i >= 0; i--) {
						Object o = sessionList.get(i);
						Session3VO value = (Session3VO) o;

						String visit = proposal + "_" + value.getVisit_number();
						if (!request.isUserInRole(visit)) {
							sessionList.remove(i);
						}
					}
				}
			}
			// beamLine operator email

			List<SessionInformation> listSessionInformation = new ArrayList<SessionInformation>();
			if (Constants.SITE_IS_ALBA() || Constants.SITE_IS_ESRF() || Constants.SITE_IS_EMBL() || Constants.SITE_IS_MAXIV() || Constants.SITE_IS_SOLEIL()) { // connection to ldap only for the
																										// esrf
				for (Iterator<Session3VO> i = sessionList.iterator(); i.hasNext();) {
					Object info = i.next();
					Session3VO value = (Session3VO) info;
					boolean hasDataCollectionGroup = false;
					Integer res = sessionService.hasDataCollectionGroups(value.getSessionId());
					if (res != null && res > 0)
						hasDataCollectionGroup = true;
					// Get localContact email
					String emailLocalContact = value.getBeamLineOperatorEmail();
					listSessionInformation.add(new SessionInformation(value, emailLocalContact, hasDataCollectionGroup));
				}
			}

			form.setBeamLineList();
			form.setListSessionInformation(listSessionInformation);
			form.setListInfo(sessionList);

			if (!isManager) {
				String proposalCode = null;
				if (request.getSession().getAttribute(Constants.PROPOSAL_CODE) != null)
					proposalCode = request.getSession().getAttribute(Constants.PROPOSAL_CODE).toString().toUpperCase();
				boolean allowedToSubmitReport = SendReportAction.IsAllowedToSubmitReport(proposalCode);
				form.setAllowedToSubmitReport(allowedToSubmitReport);
			}
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
		}
		return redirectPageFromRole(mapping, request);
	}

	/**
	 * To display the last Sessions belonging to a proposalId.
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward displayLast(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ViewSessionForm form = (ViewSessionForm) actForm;
		form.setNbSessionsToDisplay(Constants.NB_SESSIONS_TO_DISPLAY);
		form.setSessionsTitle("Last " + Constants.NB_SESSIONS_TO_DISPLAY + " " + Constants.SESSIONS);

		return this.display(mapping, actForm, request, response);

	}

	public ActionForward deleteSession(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		try {
			Integer sessionId = new Integer(request.getParameter(Constants.SESSION_ID));

			Session3VO ses = sessionService.findByPk(sessionId, true, false, false);
			List<DataCollectionGroup3VO> listDcg = ses.getDataCollectionGroupsList();
			for (Iterator<DataCollectionGroup3VO> it = listDcg.iterator(); it.hasNext();) {
				DataCollectionGroup3VO vo = it.next();
				// screening delete
				// image
				// scaling
				dataCollectionGroupService.delete(vo);
			}
			sessionService.delete(ses);
			
		} catch (AccessDeniedException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			return accessDeniedPage(mapping, actForm, request, response);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return this.display(mapping, actForm, request, response);
	}

	private ActionForward redirectPageFromRole(ActionMapping mapping, HttpServletRequest request) {
		// redirection page according to the Role ------------------------

		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();

		if (role.equals(Constants.FXMANAGE_ROLE_NAME)) {
			return mapping.findForward("viewSessionFedexManager");
		} else if (role.equals(Constants.ROLE_MANAGER)) {
			return mapping.findForward("viewSessionManager");
		} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
			return mapping.findForward("viewSessionLocalContact");
		} else {
			return mapping.findForward("viewSession");
		}
	}

	public ActionForward displaySessions(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ViewSessionForm form = (ViewSessionForm) actForm;
		form.setNbSessionsToDisplay(Constants.NB_SESSIONS_TO_DISPLAY);
		return mapping.findForward("viewSessionJs");
	}

	public ActionForward getSessions(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<SessionInformation> listSessionInformation = new ArrayList<SessionInformation>();
		List<String> errors = new ArrayList<String>();
		ViewSessionForm form = (ViewSessionForm) actForm;
		int nbSessionsToDisplay = Constants.NB_SESSIONS_TO_DISPLAY;
		try {
			nbSessionsToDisplay = new Integer(request.getParameter("nbSessionsToDisplay"));
		} catch (NumberFormatException e) {
			LOG.error(e.toString());
		}
		form.setNbSessionsToDisplay(Constants.NB_SESSIONS_TO_DISPLAY);
		// form.setSessionsTitle("Last " + Constants.NB_SESSIONS_TO_DISPLAY + " " + Constants.SESSIONS);
		Integer proposalId = null;
		boolean isManager = Confidentiality.isManager(request);
		boolean isLocalContact = Confidentiality.isLocalContact(request);

		try {
			BreadCrumbsForm.getItClean(request);
			proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			// ----------------------------------------------------------------------------------
			// for manager views, the proposalId can be given in request and is then stored in session
			if (isManager) {
				// if in request, use the request value
				String proposalIdst = request.getParameter(Constants.PROPOSAL_ID);

				// if not in request use the session value
				if (proposalIdst == null || proposalIdst.length() == 0)
					proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
				else
					proposalId = new Integer(proposalIdst);

				if (proposalId != null) {
					Proposal3VO plv = proposalService.findByPk(proposalId);

					request.getSession().setAttribute(Constants.PROPOSAL_ID, proposalId);

					BreadCrumbsForm.getIt(request).setSelectedProposal(plv);
				}
			}
			// ----------------------------------------------------------------------------------

			// update used flag to be able to retrieve only used sessions
			if (proposalId != null)
				sessionService.updateUsedSessionsFlag(proposalId);

			// ------------------------------- Retrieve parameter from form ---------------------

			String beamLineName = form.getBeamLineName();
			String beamLineNameFromList = form.getBeamLineNameFromList();
			if (beamLineName == null || beamLineName.trim().length() == 0) {
				beamLineName = beamLineNameFromList;
			}
			String startDateSt = form.getStartDatest();
			Timestamp startDate = Formatter.stringToTimestamp(startDateSt);
			String endDateSt = form.getEndDatest();
			Timestamp endDate = Formatter.stringToTimestamp(endDateSt);

			List<Session3VO> sessionList = null;

			// -------------------------------- Search ------------------------------------------
			if (isLocalContact) {
				String beamlineOperatorSiteNumber = (String) request.getSession().getAttribute(Constants.LDAP_siteNumber);
				if (beamLineName == null || beamLineName.length() == 0)
					beamLineName = null;
				Integer nbMax = null;
				if (nbSessionsToDisplay == Constants.NB_SESSIONS_TO_DISPLAY) {
					nbMax = Constants.NB_SESSIONS_TO_DISPLAY;
				}
				if (startDate == null && endDate == null) {
					sessionList = sessionService.findFiltered(null, nbMax, beamLineName, null, null, null, false,
							beamlineOperatorSiteNumber);
				} else {
					sessionList = sessionService.findFiltered(null, nbMax, beamLineName, null, startDate, endDate, false,
							beamlineOperatorSiteNumber);
				}
			} else if (proposalId == null && isManager) {
				if (beamLineName == null || beamLineName.length() == 0)
					beamLineName = null;
				if (startDate == null && endDate == null) {
					Date currentDate = Calendar.getInstance().getTime();
					sessionList = sessionService.findFiltered(null, null, beamLineName, null, currentDate, currentDate, false, null);
				} else {
					sessionList = sessionService.findFiltered(null, null, beamLineName, startDate, endDate, null, false, null);
				}
			} else {
				// for search by date
				// no name
				if (beamLineName == null || beamLineName.length() == 0) {
					// no dates
					if (startDate == null && endDate == null)
						if (Constants.SITE_IS_DLS()) {
							sessionList = sessionService.findFiltered(proposalId, null, null, null, null, null, true, null);
						} else {
							// TODO : limited number, only used + future sessions (including today)
							if (nbSessionsToDisplay == Constants.NB_SESSIONS_TO_DISPLAY) {
								sessionList = sessionService.findFiltered(proposalId, Constants.NB_SESSIONS_TO_DISPLAY, null, null,
										null, null, true, null);
							} else {
								sessionList = sessionService.findFiltered(proposalId, null, null, null, null, null, true, null);
								LOG.debug("sessionsUsedForProposal: nb sessions = " + sessionList.size());
							}
						}
					else {
						sessionList = sessionService.findFiltered(proposalId, null, null, startDate, endDate, null, true, null);
					}

				}
				// name
				else {
					// Replace SQL wildcard
					beamLineName = beamLineName.replace('*', '%');
					// ID14 1 or ID14-1
					beamLineName = beamLineName.replace('-', '%');
					beamLineName = beamLineName.replace(' ', '%');

					sessionList = sessionService.findFiltered(proposalId, null, beamLineName, startDate, endDate, null, true, null);
				}
			}
			// TODO add several finders for session and sessionLight

			// ----------------------------- Populate the form -----------------------------
			// DLS ####
			if (Constants.SITE_IS_DLS()) {
				// If not manager etc., then user shouldn't see all sessions for this proposal:
				RoleDO role = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
				if (role.getName().equals("User")) {
					// !isManager && !request.isUserInRole("dls_dasc") && !request.isUserInRole("mx_staff")){
					String proposalCode = request.getSession().getAttribute(Constants.PROPOSAL_CODE).toString();
					String proposalNumber = String.valueOf(request.getSession().getAttribute(Constants.PROPOSAL_NUMBER));
					String proposal = proposalCode + proposalNumber;

					// Loop backwards through sessionList so that sessions can be removed safely:
					for (int i = sessionList.size() - 1; i >= 0; i--) {
						Object o = sessionList.get(i);
						Session3VO value = (Session3VO) o;

						String visit = proposal + "_" + value.getVisit_number();
						if (!request.isUserInRole(visit)) {
							sessionList.remove(i);
						}
					}
				}
			}

			// beamLine operator email

			if (Constants.SITE_IS_ALBA() || Constants.SITE_IS_ESRF() || Constants.SITE_IS_EMBL() || Constants.SITE_IS_MAXIV() || Constants.SITE_IS_SOLEIL()) { // connection to ldap only for the
																										// esrf
				for (Iterator<Session3VO> i = sessionList.iterator(); i.hasNext();) {
					Object info = i.next();
					Session3VO value = (Session3VO) info;
					boolean hasDataCollectionGroup = false;
					Integer res = sessionService.hasDataCollectionGroups(value.getSessionId());
					if (res != null && res > 0)
						hasDataCollectionGroup = true;
					// Get localContact email
					String emailLocalContact = value.getBeamLineOperatorEmail();
					listSessionInformation.add(new SessionInformation(value, emailLocalContact, hasDataCollectionGroup));
				}
			}

			form.setBeamLineList();
			form.setListSessionInformation(listSessionInformation);
			form.setListInfo(sessionList);

			if (!isManager) {
				String proposalCode = request.getSession().getAttribute(Constants.PROPOSAL_CODE).toString().toUpperCase();
				boolean allowedToSubmitReport = SendReportAction.IsAllowedToSubmitReport(proposalCode);
				form.setAllowedToSubmitReport(allowedToSubmitReport);
			}
			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

		} catch (Exception e) {
			e.printStackTrace();
			errors.add(e.toString());
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("errors", errors);
			// data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
			return null;
		}

		HashMap<String, Object> data = new HashMap<String, Object>();
		// context path
		data.put("contextPath", request.getContextPath());
		// isManager
		data.put("isManager", isManager);
		// isLocalContact
		data.put("isLocalContact", isLocalContact);
		// isIndustrial
		boolean isIndustrial = SiteSpecific.isIndustrial(request);
		data.put("isIndustrial", isIndustrial);
		// allowedToSubmitReport
		boolean allowedToSubmitReport = false;
		if (!isManager) {
			String proposalCode = request.getSession().getAttribute(Constants.PROPOSAL_CODE).toString().toUpperCase();
			allowedToSubmitReport = SendReportAction.IsAllowedToSubmitReport(proposalCode);
		}
		data.put("allowedToSubmitReport", allowedToSubmitReport);
		// proposalIsBM14
		boolean proposalIsBM14 = false;
		if (request.getSession().getAttribute(Constants.PROPOSAL_CODE) != null)
			proposalIsBM14 = request.getSession().getAttribute(Constants.PROPOSAL_CODE).toString().toUpperCase()
					.equals(Constants.PROPOSAL_CODE_BM14);
		data.put("proposalIsBM14", proposalIsBM14);
		// isFX
		boolean isFx = false;
		if (request.getSession().getAttribute(Constants.PROPOSAL_CODE) != null)
			isFx = request.getSession().getAttribute(Constants.PROPOSAL_CODE).toString().toLowerCase()
					.equals(Constants.PROPOSAL_CODE_FX);
		data.put("isFx", isFx);
		// isIX
		boolean isIx = false;
		if (request.getSession().getAttribute(Constants.PROPOSAL_CODE) != null)
			isIx = request.getSession().getAttribute(Constants.PROPOSAL_CODE).toString().toLowerCase()
					.equals(Constants.PROPOSAL_CODE_IX);
		data.put("isIx", isIx);
		// title
		String title = "All Sessions";
		if (nbSessionsToDisplay == Constants.NB_SESSIONS_TO_DISPLAY) {
			title = "Last " + Constants.NB_SESSIONS_TO_DISPLAY + " sessions";
		}
		data.put("title", title);
		// list sessions
		data.put("listSessionInformation", listSessionInformation);
		// beamlines names (search)
		String[] beamlineList = Constants.BEAMLINE_LOCATION;
		if (Constants.SITE_IS_ALBA()) {
			beamlineList = ALBABeamlineEnum.getBeamlineNames();
		}
		if (Constants.SITE_IS_ESRF()) {
			beamlineList = ESRFBeamlineEnum.getBeamlineNames();
		}
		if (Constants.SITE_IS_EMBL()) {
			beamlineList = EMBLBeamlineEnum.getBeamlineNames();
		}
		if (Constants.SITE_IS_MAXIV()) {
			beamlineList = MAXIVBeamlineEnum.getBeamlineNames();
		}
		if(Constants.SITE_IS_SOLEIL()){
			beamlineList = SOLEILBeamlineEnum.getBeamlineNames();
		}
		data.put("beamlines", beamlineList);
		GSonUtils.sendToJs(response, data, "dd-MM-yyyy");
		return null;
	}

	public ActionForward saveSession(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String session = request.getParameter("session");
			Gson gson = GSonUtils.getGson("dd-MM-yyyy");
			Session3VO session3vo = gson.fromJson(session, Session3VO.class);
			if (session3vo == null) {
				LOG.error("can not save session: null value");
				return getSessions(mapping, actForm, request, response);
			}
			saveSession(session3vo);
			return getSessions(mapping, actForm, request, response);
			
		} catch (AccessDeniedException e) {
			return accessDeniedPage(mapping, actForm, request, response);

		} catch (Exception exp) {
			exp.printStackTrace();
			response.getWriter().write(GSonUtils.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
	}

	public static Session3VO saveSession(Session3VO session3vo) throws Exception {

		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Session3Service sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
			Session3VO sv = sessionService.findByPk(session3vo.getSessionId(), false, false, false);

			boolean isok = checkSessionInformation(session3vo);
			if (!isok) {
				return null;
			}

			sv.setBeamlineOperator(session3vo.getBeamlineOperator());
			sv.setComments(session3vo.getComments());
			// spv.setBeamLineOperator(form.getBeamLineOperator());
			// spv.setComments(form.getComments());

			String sessionTitle = session3vo.getSessionTitle();
			Float structureDeterminations = session3vo.getStructureDeterminations();
			Float dewarTransport = session3vo.getDewarTransport();
			Float dataBackupFrance = session3vo.getDatabackupFrance();
			Float dataBackupEurope = session3vo.getDatabackupEurope();
			sv.setSessionTitle(sessionTitle);
			sv.setStructureDeterminations(structureDeterminations);
			sv.setDewarTransport(dewarTransport);
			sv.setDatabackupFrance(dataBackupFrance);
			sv.setDatabackupEurope(dataBackupEurope);

			sessionService.update(sv);
			return sv;
			
	}

	/**
	 * checks if the session information in the form are correct and well formated, returns false if at least one field is incorrect
	 * 
	 * @param form
	 * @return
	 */
	private static boolean checkSessionInformation(Session3VO session) {
		ActionMessages l_ActionMessages = new ActionMessages();
		boolean isOk = true;
		// beamline operator
		if (session.getBeamlineOperator() != null
				&& session.getBeamlineOperator().length() > DBConstants.MAX_LENGTH_SESSION_BEAMLINEOPERATOR) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "BeamLine Operator",
					DBConstants.MAX_LENGTH_SESSION_BEAMLINEOPERATOR);
			l_ActionMessages.add("sessionBeamLineOperator", l_ActionMessageLabel);
		}
		// beamline comments
		if (session.getComments() != null && session.getComments().length() > DBConstants.MAX_LENGTH_SESSION_COMMENTS) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Comments",
					DBConstants.MAX_LENGTH_SESSION_COMMENTS);
			l_ActionMessages.add("sessionComments", l_ActionMessageLabel);
		}
		// session title
		if (session.getSessionTitle() != null && session.getSessionTitle().length() > DBConstants.MAX_LENGTH_SESSION_SESSIONTITLE) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Session title",
					DBConstants.MAX_LENGTH_SESSION_SESSIONTITLE);
			l_ActionMessages.add("sessionTitle", l_ActionMessageLabel);
		}
		return isOk;
	}

	public ActionForward removeSession(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String session = request.getParameter("session");
			Gson gson = GSonUtils.getGson("dd-MM-yyyy");
			Session3VO session3vo = gson.fromJson(session, Session3VO.class);

			Session3VO sv = sessionService.findByPk(session3vo.getSessionId(), true, false, false);
			List<DataCollectionGroup3VO> listDcg = sv.getDataCollectionGroupsList();
			for (Iterator<DataCollectionGroup3VO> it = listDcg.iterator(); it.hasNext();) {
				DataCollectionGroup3VO vo = it.next();
				dataCollectionGroupService.delete(vo);
			}
			sessionService.delete(sv);
			return getSessions(mapping, actForm, request, response);
			
		} catch (AccessDeniedException e) {
			return accessDeniedPage(mapping, actForm, request, response);
	
		} catch (Exception exp) {
			exp.printStackTrace();
			response.getWriter().write(GSonUtils.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
	}

	public ActionForward submitReport(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String session = request.getParameter("session");
			Gson gson = GSonUtils.getGson("dd-MM-yyyy");
			Session3VO session3vo = gson.fromJson(session, Session3VO.class);

			Session3VO sv = sessionService.findByPk(session3vo.getSessionId(), false, false, false);

			return getSessions(mapping, actForm, request, response);
			
		} catch (AccessDeniedException e) {
			return accessDeniedPage(mapping, actForm, request, response);

		} catch (Exception exp) {
			exp.printStackTrace();
			response.getWriter().write(GSonUtils.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
	}

}
