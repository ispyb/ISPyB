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
package ispyb.client.mx.collection;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
import com.google.gson.reflect.TypeToken;

import ispyb.client.ParentIspybAction;
import ispyb.client.SiteSpecific;
import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.util.Confidentiality;
import ispyb.client.common.util.DBConstants;
import ispyb.client.common.util.GSonUtils;
import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.common.util.PathUtils;
import ispyb.common.util.StringUtils;
import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.services.proposals.Person3Service;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.services.collections.DataCollectionGroup3Service;
import ispyb.server.mx.services.collections.EnergyScan3Service;
import ispyb.server.mx.services.collections.XFEFluorescenceSpectrum3Service;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.services.sample.Protein3Service;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.EnergyScan3VO;
import ispyb.server.mx.vos.collections.IspybCrystalClass3VO;
import ispyb.server.mx.vos.collections.IspybReference3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.collections.XFEFluorescenceSpectrum3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;

/**
 * @struts.action name="viewDataCollectionGroupForm" path="/user/viewDataCollectionGroup"
 *                input="user.collection.viewSession.page" parameter="reqCode" scope="request" validate="false"
 * 
 * @struts.action-forward name="successAll" path="user.collection.viewDataCollectionGroupAll.page"
 * @struts.action-forward name="managerSuccessAll" path="manager.collection.viewDataCollectionGroupAll.page"
 * @struts.action-forward name="fedexmanagerSuccessAll" path="fedexmanager.collection.viewDataCollectionGroupAll.page" *
 * @struts.action-forward name="localContactSuccessAll" path="localcontact.collection.viewDataCollectionGroupAll.page"
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 */

public class ViewDataCollectionGroupAction extends ParentIspybAction {

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Session3Service sessionService;

	private BLSample3Service sampleService;

	private Proposal3Service proposalService;

	protected Person3Service personService;

	protected Protein3Service proteinService;

	private XFEFluorescenceSpectrum3Service xfeService;

	private EnergyScan3Service energyScanService;

	private DataCollectionGroup3Service dataCollectionGroupService;

	private final static Logger LOG = Logger.getLogger(ViewDataCollectionGroupAction.class);

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {

		this.sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
		this.sampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
		this.dataCollectionGroupService = (DataCollectionGroup3Service) ejb3ServiceLocator
				.getLocalService(DataCollectionGroup3Service.class);
		this.personService = (Person3Service) ejb3ServiceLocator.getLocalService(Person3Service.class);
		this.proteinService = (Protein3Service) ejb3ServiceLocator.getLocalService(Protein3Service.class);

		this.sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
		this.proposalService = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
		this.xfeService = (XFEFluorescenceSpectrum3Service) ejb3ServiceLocator
				.getLocalService(XFEFluorescenceSpectrum3Service.class);
		this.energyScanService = (EnergyScan3Service) ejb3ServiceLocator.getLocalService(EnergyScan3Service.class);

	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	/**
	 * display dispatcher
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();

		try {
			// Search first in request then in BreadCrumbs
			Integer sessionId;
			if (request.getParameter(Constants.SESSION_ID) != null)
				sessionId = new Integer(request.getParameter(Constants.SESSION_ID));
			else if (BreadCrumbsForm.getIt(request).getSelectedSession() != null)
				sessionId = BreadCrumbsForm.getIt(request).getSelectedSession().getSessionId();
			else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "sessionId is null"));
				saveErrors(request, errors);
				return (mapping.findForward("error"));
			}
			ViewDataCollectionGroupForm form = (ViewDataCollectionGroupForm) actForm;
			form.setSessionId(sessionId);

			Session3VO slv = sessionService.findByPk(sessionId, false, true, true);
			// Fill the information bar
			BreadCrumbsForm.getItClean(request).setSelectedSession(slv);

		} catch (AccessDeniedException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			return accessDeniedPage(mapping, actForm, request, response);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("error.user.collection.viewDataCollectionGroup"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}
		// redirect according role
		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();
		if (role.equals(Constants.FXMANAGE_ROLE_NAME)) {
			return mapping.findForward("fedexmanagerSuccessAll");
		} else if (role.equals(Constants.ROLE_MANAGER)) {
			return mapping.findForward("managerSuccessAll");
		} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
			return mapping.findForward("localContactSuccessAll");
		} else {
			return mapping.findForward("successAll");
		}
	}

	public ActionForward displayForSample(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();

		try {
			// Search first in request then in BreadCrumbs
			Integer sampleId;
			if (request.getParameter(Constants.BLSAMPLE_ID) != null)
				sampleId = new Integer(request.getParameter(Constants.BLSAMPLE_ID));
			else if (BreadCrumbsForm.getIt(request).getSelectedSample() != null)
				sampleId = BreadCrumbsForm.getIt(request).getSelectedSample().getBlSampleId();
			else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "sampleId is null"));
				saveErrors(request, errors);
				return (mapping.findForward("error"));
			}
			ViewDataCollectionGroupForm form = (ViewDataCollectionGroupForm) actForm;
			form.setSessionId(null);
			BLSample3VO sample = sampleService.findByPk(sampleId, false, false, false);
			// Fill the information bar
			BreadCrumbsForm.getItClean(request).setSelectedSample(sample);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("error.user.collection.viewDataCollectionGroup"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}
		// redirect according role
		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();
		if (role.equals(Constants.FXMANAGE_ROLE_NAME)) {
			return mapping.findForward("fedexmanagerSuccessAll");
		} else if (role.equals(Constants.ROLE_MANAGER)) {
			return mapping.findForward("managerSuccessAll");
		} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
			return mapping.findForward("localContactSuccessAll");
		} else {
			return mapping.findForward("successAll");
		}
	}

	/**
	 * Save a comment for one session
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward saveSessionComment(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();

		try {
			// Retrieve updated information from form
			ViewDataCollectionGroupForm form = (ViewDataCollectionGroupForm) actForm;
			boolean isok = checkSessionInformation(form, request);
			if (!isok) {
				return display(mapping, actForm, request, response);
			}

			Integer sessionId = form.getSessionId();
			String comments = form.getSessionComments();
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
			Session3VO slv = sessionService.findByPk(sessionId, false, false, false);
			slv.setComments(comments);
			slv.setBeamlineOperator(form.getSessionBeamLineOperator());
			slv.setSessionTitle(sessionTitle);
			slv.setStructureDeterminations(structureDeterminations);
			slv.setDewarTransport(dewarTransport);
			slv.setDatabackupFrance(dataBackupFrance);
			slv.setDatabackupEurope(dataBackupEurope);
			sessionService.update(slv);

			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.inserted", "Data"));
			saveMessages(request, messages);
			
		} catch (AccessDeniedException e) {
			return accessDeniedPage(mapping, actForm, request, response);

		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.collection.viewDataCollection"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		// return mapping.findForward("success");
		return this.display(mapping, actForm, request, response);
	}

	/**
	 * checks if the session information in the form are correct and well formated, returns false if at least one field
	 * is incorrect
	 * 
	 * @param form
	 * @return
	 */
	private boolean checkSessionInformation(ViewDataCollectionGroupForm form, HttpServletRequest request) {
		ActionMessages l_ActionMessages = new ActionMessages();
		boolean isOk = true;
		// beamline operator
		if (form.getSessionBeamLineOperator() != null
				&& form.getSessionBeamLineOperator().length() > DBConstants.MAX_LENGTH_SESSION_BEAMLINEOPERATOR) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "BeamLine Operator",
					DBConstants.MAX_LENGTH_SESSION_BEAMLINEOPERATOR);
			l_ActionMessages.add("sessionBeamLineOperator", l_ActionMessageLabel);
		}
		// beamline comments
		if (form.getSessionComments() != null
				&& form.getSessionComments().length() > DBConstants.MAX_LENGTH_SESSION_COMMENTS) {
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

	/**
	 * returns the index of the specified crystal class in the specified list (-1 otherwise)
	 * 
	 * @param list
	 * @param crystalClass
	 * @return
	 */
	public static int getCrystalClassIndex(List<IspybCrystalClass3VO> list, String crystalClass) {
		int nb = list.size();
		for (int i = 0; i < nb; i++) {
			if (crystalClass != null && list.get(i).getCrystalClassCode().equals(crystalClass.toUpperCase().trim())) {
				return i;
			}
		}
		return -1;
	}

	public void getDataCollectionGroupBySession(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		List<String> errors = new ArrayList<String>();

		List<DataCollectionGroup3VO> dataCollectionGroupList = new ArrayList<DataCollectionGroup3VO>();
		List<EnergyScan3VO> energyScanList = new ArrayList<EnergyScan3VO>();
		List<XFEFluorescenceSpectrum3VO> xfeList = new ArrayList<XFEFluorescenceSpectrum3VO>();

		try {
			// Search first in request then in BreadCrumbs
			Integer sessionId = null;
			if (request.getParameter(Constants.SESSION_ID) != null)
				sessionId = new Integer(request.getParameter(Constants.SESSION_ID));
			else if (BreadCrumbsForm.getIt(request).getSelectedSession() != null)
				sessionId = BreadCrumbsForm.getIt(request).getSelectedSession().getSessionId();
			else {
				errors.add("sessionId is null");
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				// data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return;
			}

			Session3VO slv = sessionService.findByPk(sessionId, false, true, true);
			// Fill the information bar
			BreadCrumbsForm.getItClean(request).setSelectedSession(slv);

			// Confidentiality (check if object proposalId matches)
			// Integer proposalId = slv.getProposalId();
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			if (!Confidentiality.isAccessAllowed(request, slv.getProposalVOId())) {
				errors.add("Access denied");
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				// data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return;
			}
			Proposal3VO pv = proposalService.findByPk(proposalId);
			// session information
			String mpEmail = null;
			if (pv != null)
				mpEmail = pv.getPersonVO().getEmailAddress();
			String structureDeterminations = slv.getStructureDeterminations() == null
					|| slv.getStructureDeterminations() == 0 ? "" : slv.getStructureDeterminations().toString();
			String dewarTransport = slv.getDewarTransport() == null || slv.getDewarTransport() == 0 ? "" : slv
					.getDewarTransport().toString();
			String dataBackupFrance = slv.getDatabackupFrance() == null || slv.getDatabackupFrance() == 0 ? "" : slv
					.getDatabackupFrance().toString();
			String dataBackupEurope = slv.getDatabackupEurope() == null || slv.getDatabackupEurope() == 0 ? "" : slv
					.getDatabackupEurope().toString();
			// gets the email address for the beamline operator
			// Get localContact email
			String beamLineOperatorEmail = slv.getBeamLineOperatorEmail();
			// Get data collection groups
			dataCollectionGroupList = dataCollectionGroupService.findFiltered(sessionId, true, false);
			List<DataCollectionGroup3VO> aListTmp = new ArrayList<DataCollectionGroup3VO>();
			for (Iterator<DataCollectionGroup3VO> myDataCollectionGroups = dataCollectionGroupList.iterator(); myDataCollectionGroups
					.hasNext();) {
				DataCollectionGroup3VO myDataCollectionGroup = myDataCollectionGroups.next();
				int nbCol = myDataCollectionGroup.getDataCollectionsList().size();
				// if ((nbCol > 0) || !selectedNbCollect.equals("WithCollect")) {
				if ((nbCol > 0)) {
					aListTmp.add(myDataCollectionGroup);
				}
			}
			dataCollectionGroupList = aListTmp;

			energyScanList = slv.getEnergyScansList();
			xfeList = slv.getXfeSpectrumsList();

			SessionInformation sessionInformation = new SessionInformation(slv, beamLineOperatorEmail,
					dataCollectionGroupList.size() > 0);
			// Set DataCollection list (for export)
			List<DataCollection3VO> dataCollectionList = new ArrayList<DataCollection3VO>();
			for (Iterator<DataCollectionGroup3VO> iterator = dataCollectionGroupList.iterator(); iterator.hasNext();) {
				DataCollectionGroup3VO dataCollectionGroup3VO = iterator.next();
				dataCollectionList.addAll(dataCollectionGroup3VO.getDataCollectionVOs());
			}

			getDataCollectionGroupForAll(actForm, request, response, dataCollectionGroupList, energyScanList, xfeList,
					sessionId, sessionInformation, null);

		} catch (Exception e) {
			e.printStackTrace();
			errors.add(e.toString());
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("errors", errors);
			// data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
			return;
		}

	}

	public void getDataCollectionGroupBySample(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		List<String> errors = new ArrayList<String>();

		List<DataCollectionGroup3VO> dataCollectionGroupList = new ArrayList<DataCollectionGroup3VO>();
		List<EnergyScan3VO> energyScanList = new ArrayList<EnergyScan3VO>();
		List<XFEFluorescenceSpectrum3VO> xfeList = new ArrayList<XFEFluorescenceSpectrum3VO>();

		try {
			// Search first in request then in BreadCrumbs
			Integer sampleId = null;
			if (request.getParameter(Constants.BLSAMPLE_ID) != null)
				sampleId = new Integer(request.getParameter(Constants.BLSAMPLE_ID));
			else if (BreadCrumbsForm.getIt(request).getSelectedSample() != null)
				sampleId = BreadCrumbsForm.getIt(request).getSelectedSample().getBlSampleId();
			else if (request.getSession().getAttribute(Constants.BLSAMPLE_ID) != null) {
				sampleId = (Integer) request.getSession().getAttribute(Constants.BLSAMPLE_ID);
			}
			if (sampleId == null) {
				errors.add("sampleId is null");
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				// data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return;
			}
			request.getSession().setAttribute(Constants.BLSAMPLE_ID, sampleId);
			BLSample3VO sample = sampleService.findByPk(sampleId, false, false, false);
			// Fill the information bar
			BreadCrumbsForm.getItClean(request).setSelectedSample(sample);

			// Confidentiality (check if object proposalId matches)
			// Integer proposalId = slv.getProposalId();
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			Integer sampleProposalId = null;
			if (sample.getContainerVO() != null && sample.getContainerVO().getDewarVO() != null
					&& sample.getContainerVO().getDewarVO().getShippingVO() != null) {
				sampleProposalId = sample.getContainerVO().getDewarVO().getShippingVO().getProposalVOId();
			}
			if (sampleProposalId != null && !Confidentiality.isAccessAllowed(request, sampleProposalId)) {
				errors.add("Access denied");
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				// data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return;
			}
			Proposal3VO pv = proposalService.findByPk(proposalId);
			// session information
			// Get data collection groups
			dataCollectionGroupList = dataCollectionGroupService.findBySampleId(sampleId, true, false);
			List<DataCollectionGroup3VO> aListTmp = new ArrayList<DataCollectionGroup3VO>();
			for (Iterator<DataCollectionGroup3VO> myDataCollectionGroups = dataCollectionGroupList.iterator(); myDataCollectionGroups
					.hasNext();) {
				DataCollectionGroup3VO myDataCollectionGroup = myDataCollectionGroups.next();
				int nbCol = myDataCollectionGroup.getDataCollectionsList().size();
				// if ((nbCol > 0) || !selectedNbCollect.equals("WithCollect")) {
				if ((nbCol > 0)) {
					aListTmp.add(myDataCollectionGroup);
				}
			}
			dataCollectionGroupList = aListTmp;
			energyScanList = energyScanService.findFiltered(null, sampleId);
			xfeList = xfeService.findFiltered(null, sampleId, null);

			SessionInformation sessionInformation = null;
			// Set DataCollection list (for export)
			List<DataCollection3VO> dataCollectionList = new ArrayList<DataCollection3VO>();
			for (Iterator<DataCollectionGroup3VO> iterator = dataCollectionGroupList.iterator(); iterator.hasNext();) {
				DataCollectionGroup3VO dataCollectionGroup3VO = iterator.next();
				dataCollectionList.addAll(dataCollectionGroup3VO.getDataCollectionVOs());
			}

			getDataCollectionGroupForAll(actForm, request, response, dataCollectionGroupList, energyScanList, xfeList,
					null, sessionInformation, sampleId);

		} catch (Exception e) {
			e.printStackTrace();
			errors.add(e.toString());
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("errors", errors);
			// data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
			return;
		}

	}

	@SuppressWarnings("unchecked")
	public void getDataCollectionGroupForAll(ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response, List<DataCollectionGroup3VO> dataCollectionGroupList,
			List<EnergyScan3VO> energyScanList, List<XFEFluorescenceSpectrum3VO> xfeList, Integer sessionId,
			SessionInformation sessionInformation, Integer blSampleId) {

		ViewDataCollectionGroupForm form = (ViewDataCollectionGroupForm) actForm;
		List<String> errors = new ArrayList<String>();
		boolean isIndus = SiteSpecific.isIndustrial(request);

		if (!isIndus)
			form.setIsUser(true);
		if (isIndus) {
			form.setEditSkipAndComments(false);
		} else
			form.setEditSkipAndComments(true);
		String proposalCode = null;
		if (request.getSession().getAttribute(Constants.PROPOSAL_CODE) != null) {
			proposalCode = (String) (request.getSession().getAttribute(Constants.PROPOSAL_CODE));
		}

		try {

			// Confidentiality (check if object proposalId matches)
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			Proposal3VO pv = proposalService.findByPk(proposalId);
			// crystal classes
			// Get an object list.
			List<IspybCrystalClass3VO> listOfCrystalClass = (List<IspybCrystalClass3VO>) request.getSession()
					.getAttribute(Constants.ISPYB_CRYSTAL_CLASS_LIST);
			form.setListOfCrystalClass(listOfCrystalClass);
			// additional information
			List<DataCollectionGroup> listOfGroups = new ArrayList<DataCollectionGroup>();
			for (Iterator<DataCollectionGroup3VO> it = dataCollectionGroupList.iterator(); it.hasNext();) {
				DataCollectionGroup3VO vo = it.next();
				Integer runNumber = null;
				if (vo.getDataCollectionsList() != null && vo.getDataCollectionsList().size() == 1) {
					runNumber = vo.getDataCollectionsList().get(0).getDataCollectionNumber();
				}
				String proteinAcronym = "";
				String sampleName = "";
				String imagePrefix = "";
				if (vo.getBlSampleVO() != null) {
					sampleName = vo.getBlSampleVO().getName();
					if (vo.getBlSampleVO().getCrystalVO() != null
							&& vo.getBlSampleVO().getCrystalVO().getProteinVO() != null) {
						proteinAcronym = vo.getBlSampleVO().getCrystalVO().getProteinVO().getAcronym();
					}
				}
				if (vo.getDataCollectionsList() != null && vo.getDataCollectionsList().size() > 0) {
					imagePrefix = vo.getDataCollectionsList().get(0).getImagePrefix();
				}
				DataCollectionGroup group = new DataCollectionGroup(vo, runNumber, proteinAcronym, sampleName,
						imagePrefix);
				listOfGroups.add(group);
			}

			List<CrystalClassSummary> listCrystal = new ArrayList<CrystalClassSummary>();
			// User information
			if (!isIndus) {
				// Perform only for IFX proposal in case of MXPress experiment
				if (proposalCode != null && proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX)) {
					// for MXPress users, calculate number of Crystals of class 1(C),2(SC), 3(PS), T, E
					// initialization
					List<Integer> listOfNbCrystalPerClass = new ArrayList<Integer>();
					int nbCC = listOfCrystalClass.size();
					for (int i = 0; i < nbCC; i++) {
						listOfNbCrystalPerClass.add(0);
					}
					String crystalClass;
					// Browse Data Collections
					for (Iterator<DataCollectionGroup3VO> it = dataCollectionGroupList.iterator(); it.hasNext();) {
						crystalClass = it.next().getCrystalClass();
						int idCc = getCrystalClassIndex(listOfCrystalClass, crystalClass);
						if (idCc != -1) {
							listOfNbCrystalPerClass.set(idCc, listOfNbCrystalPerClass.get(idCc) + 1);
						}
					}
					// Browse Energy Scans
					for (Iterator<EnergyScan3VO> it = energyScanList.iterator(); it.hasNext();) {
						crystalClass = it.next().getCrystalClass();
						int idCc = getCrystalClassIndex(listOfCrystalClass, crystalClass);
						if (idCc != -1 && crystalClass != null && crystalClass.toUpperCase().trim().equals("E")) {
							listOfNbCrystalPerClass.set(idCc, listOfNbCrystalPerClass.get(idCc) + 1);
						}
					}
					// Browse XRF Spectra
					for (Iterator<XFEFluorescenceSpectrum3VO> it = xfeList.iterator(); it.hasNext();) {
						crystalClass = it.next().getCrystalClass();
						int idCc = getCrystalClassIndex(listOfCrystalClass, crystalClass);
						if (idCc != -1 && crystalClass != null && crystalClass.toUpperCase().trim().equals("X")) {
							listOfNbCrystalPerClass.set(idCc, listOfNbCrystalPerClass.get(idCc) + 1);
						}
					}
					for (int i = 0; i < nbCC; i++) {
						if (listOfNbCrystalPerClass.get(i) > 0) {
							listCrystal.add(new CrystalClassSummary(listOfCrystalClass.get(i).getCrystalClassCode(),
									listOfCrystalClass.get(i).getCrystalClassName(), listOfNbCrystalPerClass.get(i)));
						}
					}
				}
			}
			// Set DataCollection list (for export)
			List<DataCollection3VO> dataCollectionList = new ArrayList<DataCollection3VO>();
			for (Iterator<DataCollectionGroup3VO> iterator = dataCollectionGroupList.iterator(); iterator.hasNext();) {
				DataCollectionGroup3VO dataCollectionGroup3VO = iterator.next();
				dataCollectionList.addAll(dataCollectionGroup3VO.getDataCollectionVOs());
			}
			request.getSession().setAttribute(Constants.DATACOLLECTION_LIST, dataCollectionList);
			// Set EnergyScan list (for export)
			request.getSession().setAttribute(Constants.ENERGYSCAN_LIST, energyScanList);
			// Set XFE list (for export)
			request.getSession().setAttribute(Constants.XFE_LIST, xfeList);

			List<XFESpectra> xrfSpectraList = new ArrayList<XFESpectra>();
			for (Iterator<XFEFluorescenceSpectrum3VO> iterator = xfeList.iterator(); iterator.hasNext();) {
				XFEFluorescenceSpectrum3VO xfe = iterator.next();
				int jpegScanExists = PathUtils.fileExists(xfe.getJpegScanFileFullPath());
				String jpegScanFileFullPathParser = PathUtils.FitPathToOS(xfe.getJpegScanFileFullPath());
				int annotatedPymcaXfeSpectrumExists = PathUtils.fileExists(xfe.getAnnotatedPymcaXfeSpectrum());
				String shortFileNameAnnotatedPymcaXfeSpectrum = StringUtils.getShortFilename(xfe
						.getAnnotatedPymcaXfeSpectrum());
				String sampleName = "";
				if (xfe.getBlSampleVO() != null) {
					sampleName = xfe.getBlSampleVO().getName();
				}
				XFESpectra spectra = new XFESpectra(xfe, jpegScanExists == 1, jpegScanFileFullPathParser,
						annotatedPymcaXfeSpectrumExists == 1, shortFileNameAnnotatedPymcaXfeSpectrum, sampleName);
				spectra.setAnnotatedPymcaXfeSpectrum(PathUtils.FitPathToOS(xfe.getAnnotatedPymcaXfeSpectrum()));
				xrfSpectraList.add(spectra);
			}

			List<EnergyScan> energyScans = new ArrayList<EnergyScan>();
			for (Iterator<EnergyScan3VO> iterator = energyScanList.iterator(); iterator.hasNext();) {
				EnergyScan3VO energyScan = iterator.next();
				int scanFileFullPathExists = PathUtils.fileExists(energyScan.getScanFileFullPath());
				String shortFileName = StringUtils.getShortFilename(energyScan.getScanFileFullPath());
				int choochExists = PathUtils.fileExists(energyScan.getJpegChoochFileFullPath());
				String jpegChoochFileFitPath = PathUtils.FitPathToOS(energyScan.getJpegChoochFileFullPath());
				String sampleName = "";
				if (energyScan.getBlSampleVO() != null) {
					sampleName = energyScan.getBlSampleVO().getName();
				}

				EnergyScan es = new EnergyScan(energyScan, scanFileFullPathExists == 1, shortFileName,
						choochExists == 1, jpegChoochFileFitPath, sampleName);
				energyScans.add(es);
			}

			// list of references
			List<IspybReference3VO> listOfReferences = new ArrayList<IspybReference3VO>();
			List<IspybReference3VO> allRef = (List<IspybReference3VO>) request.getSession().getAttribute(
					Constants.ISPYB_REFERENCE_LIST);
			List<String> listOfBeamlines = new ArrayList<String>();
			boolean isXFE = (xfeList != null && xfeList.size() > 0);
			if (isXFE) {
				for (Iterator<DataCollectionGroup3VO> iterator = dataCollectionGroupList.iterator(); iterator.hasNext();) {
					DataCollectionGroup3VO dataCollectionGroup3VO = iterator.next();
					String bl = dataCollectionGroup3VO.getSessionVO().getBeamlineName();
					if (!listOfBeamlines.contains(bl)) {
						listOfBeamlines.add(bl);
					}
				}
				for (Iterator<XFEFluorescenceSpectrum3VO> iterator = xfeList.iterator(); iterator.hasNext();) {
					XFEFluorescenceSpectrum3VO xfe3VO = iterator.next();
					String bl = xfe3VO.getSessionVO().getBeamlineName();
					if (!listOfBeamlines.contains(bl)) {
						listOfBeamlines.add(bl);
					}
				}

				for (Iterator<IspybReference3VO> iterator = allRef.iterator(); iterator.hasNext();) {
					IspybReference3VO ispybReference3VO = iterator.next();
					if (ispybReference3VO.getBeamline().equals(Constants.REFERENCE_ALL_XRF)) {
						listOfReferences.add(ispybReference3VO);
					}
					if (ispybReference3VO.getBeamline().equals(Constants.REFERENCE_XRF)) {
						listOfReferences.add(ispybReference3VO);
					} else {
						String beamline = ispybReference3VO.getBeamline();
						boolean isBeamlineInvolved = listOfBeamlines.contains(beamline);
						if (isBeamlineInvolved) {
							listOfReferences.add(ispybReference3VO);
						}
					}
				}
			}
			String referenceText = "When reporting the results of XRF analysis<br/>please cite the reference below";
			request.getSession().setAttribute(Constants.REFERENCES_LIST, listOfReferences);

			// stats
			int nbCollect = 0;
			int nbTest = 0;
			// has MXPressO/MXPressE/I/M workflows?
			boolean sessionHasMXpressOWF = false;
			for (Iterator<DataCollectionGroup> ite = listOfGroups.iterator(); ite.hasNext();) {
				DataCollectionGroup dcg = ite.next();
				if (dcg.getWorkflowVO()!=null && dcg.getWorkflowVO().isMXPressEOIA()){
					sessionHasMXpressOWF = true;
				}
				
				List<DataCollection3VO> listCollect = dcg.getDataCollectionsList();
				for (Iterator<DataCollection3VO> iterator = listCollect.iterator(); iterator.hasNext();) {
					DataCollection3VO dataCollection3VO = iterator.next();
					if (dataCollection3VO.getNumberOfImages() <= 4) {
						nbTest++;
					} else {
						nbCollect++;
					}

				}
			}
			if (sessionInformation != null) {
				sessionInformation.setNbEnergyScans(energyScans.size());
				sessionInformation.setNbXRFSpectra(xrfSpectraList.size());
				sessionInformation.setNbCollect(nbCollect);
				sessionInformation.setNbTest(nbTest);
			}

			request.getSession().setAttribute(Constants.DATACOLLECTIONGROUP_LIST, listOfGroups);

			// can edit and save comments
			boolean canSave = !isIndus;

			HashMap<String, Object> data = new HashMap<String, Object>();
			// context path
			data.put("contextPath", request.getContextPath());
			// listOfCrystal
			data.put("listOfCrystalClass", listOfCrystalClass);
			// dataCollectionGroupList
			data.put("dataCollectionGroupList", listOfGroups);
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
			// isIndus
			data.put("isIndus", isIndus);
			// sessionId
			data.put("sessionId", sessionId);
			// session
			data.put("session", sessionInformation);
			// xrfSpectra
			data.put("xrfSpectraList", xrfSpectraList);
			// energyScans
			data.put("energyScansList", energyScans);
			// list of references
			data.put("listOfReferences", listOfReferences);
			// referenceText
			data.put("referenceText", referenceText);
			// listCrystal
			data.put("listCrystal", listCrystal);
			// can edit comments
			data.put("canSave", canSave);
			// sessionHasMXpressOWF
			data.put("sessionHasMXpressOWF", sessionHasMXpressOWF);
			// sessionSummary
			data.put("sessionSummary", false);
			// data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");

		} catch (Exception e) {
			// errors.add(ActionMessages.GLOBAL_MESSAGE,
			// new ActionMessage("error.user.collection.viewDataCollectionGroup"));
			// errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
			errors.add(e.toString());
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("errors", errors);
			// data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
			return;
		}

	}

	public void saveAllDataCollectionGroups(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String comments;
			String crystalClass = null;
			Integer dataCollectionGroupId;
			String proposalCode = (String) request.getSession().getAttribute(Constants.PROPOSAL_CODE);
			String dataCollectionGroupList = request.getParameter("dataCollectionGroupList");
			Gson gson = GSonUtils.getGson("dd-MM-yyyy");
			Type mapType = new TypeToken<ArrayList<DataCollectionGroup3VO>>() {
			}.getType();
			ArrayList<DataCollectionGroup3VO> dataCollectionGroups = gson.fromJson(dataCollectionGroupList, mapType);
			if (dataCollectionGroups != null) {
				int nb = dataCollectionGroups.size();
				for (int i = 0; i < nb; i++) {
					dataCollectionGroupId = dataCollectionGroups.get(i).getDataCollectionGroupId();
					DataCollectionGroup3VO dclgvFromDB = dataCollectionGroupService.findByPk(dataCollectionGroupId,
							false, false);

					comments = dataCollectionGroups.get(i).getComments();
					dclgvFromDB.setComments(comments);
					if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX)) {
						crystalClass = dataCollectionGroups.get(i).getCrystalClass();
						dclgvFromDB.setCrystalClass(crystalClass);
					}
					dataCollectionGroupService.update(dclgvFromDB);

					LOG.debug("update datacollectionGroup: ID=" + dataCollectionGroupId + "   COMMENTS=" + comments);
				}
			}
			getDataCollectionGroupBySession(mapping, actForm, request, response);
		} catch (Exception exp) {
			exp.printStackTrace();
			response.getWriter().write(GSonUtils.getErrorMessage(exp));
			response.setStatus(500);
		}
	}

	public void saveAllXFE(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String comments;
			String crystalClass = null;
			Integer xfeId;
			String proposalCode = (String) request.getSession().getAttribute(Constants.PROPOSAL_CODE);
			String xfeSpectraList = request.getParameter("listXFESpectra");
			Gson gson = GSonUtils.getGson("dd-MM-yyyy");
			Type mapType = new TypeToken<ArrayList<XFEFluorescenceSpectrum3VO>>() {
			}.getType();
			ArrayList<XFEFluorescenceSpectrum3VO> xfeSpectra = gson.fromJson(xfeSpectraList, mapType);
			if (xfeSpectra != null) {
				int nb = xfeSpectra.size();
				for (int i = 0; i < nb; i++) {
					xfeId = xfeSpectra.get(i).getXfeFluorescenceSpectrumId();
					XFEFluorescenceSpectrum3VO xfeFromDB = xfeService.findByPk(xfeId);

					comments = xfeSpectra.get(i).getComments();
					xfeFromDB.setComments(comments);
					if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX)) {
						crystalClass = xfeSpectra.get(i).getCrystalClass();
						xfeFromDB.setCrystalClass(crystalClass);
					}
					xfeService.update(xfeFromDB);

					LOG.debug("update xfeSpectra: ID=" + xfeId + "   COMMENTS=" + comments);
				}
			}
			getDataCollectionGroupBySession(mapping, actForm, request, response);
		} catch (Exception exp) {
			exp.printStackTrace();
			response.getWriter().write(GSonUtils.getErrorMessage(exp));
			response.setStatus(500);
		}
	}

	public void saveAllEnergyScan(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("saveAllEnergyScan");
		try {
			String comments;
			String crystalClass = null;
			Integer energyScanId;
			String proposalCode = (String) request.getSession().getAttribute(Constants.PROPOSAL_CODE);
			String energyScanList = request.getParameter("listAllEnergyScan");
			Gson gson = GSonUtils.getGson("dd-MM-yyyy");
			Type mapType = new TypeToken<ArrayList<EnergyScan3VO>>() {
			}.getType();
			ArrayList<EnergyScan3VO> energyScans = gson.fromJson(energyScanList, mapType);
			if (energyScans != null) {
				int nb = energyScans.size();
				for (int i = 0; i < nb; i++) {
					energyScanId = energyScans.get(i).getEnergyScanId();
					EnergyScan3VO energyScanFromDB = energyScanService.findByPk(energyScanId);

					comments = energyScans.get(i).getComments();
					energyScanFromDB.setComments(comments);
					if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX)) {
						crystalClass = energyScans.get(i).getCrystalClass();
						energyScanFromDB.setCrystalClass(crystalClass);
					}
					energyScanService.update(energyScanFromDB);

					LOG.debug("update energyScan: ID=" + energyScanId + "   COMMENTS=" + comments);
				}
			}
			getDataCollectionGroupBySession(mapping, actForm, request, response);
		} catch (Exception exp) {
			exp.printStackTrace();
			response.getWriter().write(GSonUtils.getErrorMessage(exp));
			response.setStatus(500);
		}
	}

	public ActionForward saveSession(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String session = request.getParameter("session");
			Gson gson = GSonUtils.getGson("dd-MM-yyyy");
			Session3VO session3vo = gson.fromJson(session, Session3VO.class);
			if (session3vo == null) {
				LOG.error("can not save session: null value");
				return null;
			}
			session3vo = ViewSessionAction.saveSession(session3vo);
			return null;
			
		} catch (AccessDeniedException e) {
			return accessDeniedPage(mapping, actForm, request, response);

		} catch (Exception exp) {
			exp.printStackTrace();
			response.getWriter().write(GSonUtils.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
	}

	public void getSessionInformation(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			Integer sessionId = null;
			if (request.getParameter(Constants.SESSION_ID) != null)
				sessionId = new Integer(request.getParameter(Constants.SESSION_ID));
			else if (BreadCrumbsForm.getIt(request).getSelectedSession() != null)
				sessionId = BreadCrumbsForm.getIt(request).getSelectedSession().getSessionId();
			Session3VO slv = sessionService.findByPk(sessionId, true, true, true);
			String beamLineOperatorEmail = slv.getBeamLineOperatorEmail();
			SessionInformation sessionInformation = new SessionInformation(slv, beamLineOperatorEmail, slv
					.getDataCollectionGroupsList().size() > 0);
			HashMap<String, Object> data = new HashMap<String, Object>();
			// session
			data.put("session", sessionInformation);
			// data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
		} catch (Exception exp) {
			exp.printStackTrace();
			response.getWriter().write(GSonUtils.getErrorMessage(exp));
			response.setStatus(500);
		}
	}

}
