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
/**
 * ViewDataCollectionAction.java
 */

package ispyb.client.mx.collection;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ejb.FinderException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.client.ParentIspybAction;
import ispyb.client.SiteSpecific;
import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.reference.ViewReferenceAction;
import ispyb.client.common.util.Confidentiality;
import ispyb.client.common.util.FileUtil;
import ispyb.client.common.util.GSonUtils;
import ispyb.client.common.util.UrlUtils;
import ispyb.client.mx.results.AutoProcAttachmentWebBean;
import ispyb.client.mx.results.ImageValueInfo;
import ispyb.client.mx.results.SnapshotInfo;
import ispyb.client.mx.results.ViewResultsAction;
import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.common.util.HashMapToZip;
import ispyb.common.util.PathUtils;
import ispyb.common.util.StringUtils;
import ispyb.common.util.beamlines.EMBLBeamlineEnum;
import ispyb.common.util.beamlines.ESRFBeamlineEnum;
import ispyb.common.util.beamlines.MAXIVBeamlineEnum;
import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.services.proposals.Person3Service;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.autoproc.AutoProc3Service;
import ispyb.server.mx.services.autoproc.AutoProcIntegration3Service;
import ispyb.server.mx.services.autoproc.AutoProcProgram3Service;
import ispyb.server.mx.services.autoproc.AutoProcScalingStatistics3Service;
import ispyb.server.mx.services.autoproc.SpaceGroup3Service;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.services.collections.DataCollectionGroup3Service;
import ispyb.server.mx.services.collections.GridInfo3Service;
import ispyb.server.mx.services.collections.WorkflowMesh3Service;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.services.sample.Protein3Service;
import ispyb.server.mx.vos.autoproc.AutoProc3VO;
import ispyb.server.mx.vos.autoproc.AutoProcIntegration3VO;
import ispyb.server.mx.vos.autoproc.AutoProcProgramAttachment3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScalingStatistics3VO;
import ispyb.server.mx.vos.autoproc.AutoProcStatus3VO;
import ispyb.server.mx.vos.autoproc.IspybAutoProcAttachment3VO;
import ispyb.server.mx.vos.autoproc.SpaceGroup3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.EnergyScan3VO;
import ispyb.server.mx.vos.collections.IspybCrystalClass3VO;
import ispyb.server.mx.vos.collections.IspybReference3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.collections.WorkflowMesh3VO;
import ispyb.server.mx.vos.collections.XFEFluorescenceSpectrum3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.Protein3VO;
import ispyb.server.mx.vos.screening.Screening3VO;
import ispyb.server.mx.vos.screening.ScreeningOutput3VO;
import ispyb.server.mx.vos.screening.ScreeningOutputLattice3VO;

/**
 * @struts.action name="viewDataCollectionForm" path="/user/viewDataCollection" input="user.collection.viewSession.page"
 *                parameter="reqCode" scope="request" validate="false"
 * 
 * @struts.action-forward name="successAllLarge" path="user.collection.viewDataCollectionAll_large.page"
 * @struts.action-forward name="viewCollectJs" path="user.collection.viewDataCollectionJs.page"
 * @struts.action-forward name="managerSuccessAllLarge" path="manager.collection.viewDataCollectionAll_large.page"
 * @struts.action-forward name="fedexmanagerSuccessAllLarge" path="fedexmanager.collection.viewDataCollectionAll_large.page"
 * @struts.action-forward name="localContactSuccessAllLarge" path="localContact.collection.viewDataCollectionAll_large.page"
 * @struts.action-forward name="error" path="site.default.error.page"
 * @struts.action-forward name="editDataCollection" path="user.collection.editDataCollection.page"
 * @struts.action-forward name="sampleRanking" path="user.ranking.sampleRanking.page"
 * @struts.action-forward name="viewHtmlFiles" path="user.collection.html.page"
 * @struts.action-forward name="dataCollectionArchiving" path="/tiles/bodies/mx/collection/viewDataCollectionArchiving.jsp"
 * @struts.action-forward name="viewLastCollect" path="manager.collection.lastCollect.page"
 * 
 */
public class ViewDataCollectionAction extends ParentIspybAction {

	private final static Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Session3Service sessionService;

	private Proposal3Service proposalService;

	private BLSample3Service sampleService;

	private DataCollection3Service dataCollectionService;

	private DataCollectionGroup3Service dataCollectionGroupService;

	protected Person3Service personService;

	protected Protein3Service proteinService;

	protected GridInfo3Service gridInfoService;

	protected WorkflowMesh3Service workflowMeshService;

	public static final String SAMPLE_TITLE = "Collection List for Sample";

	private final static Logger LOG = Logger.getLogger(ViewDataCollectionAction.class);

	private AutoProcProgram3Service appService;

	private AutoProc3Service apService;

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {
		this.personService = (Person3Service) ejb3ServiceLocator.getLocalService(Person3Service.class);
		this.proteinService = (Protein3Service) ejb3ServiceLocator.getLocalService(Protein3Service.class);

		this.sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
		this.sampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
		this.proposalService = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);

		this.dataCollectionService = (DataCollection3Service) ejb3ServiceLocator.getLocalService(DataCollection3Service.class);
		this.dataCollectionGroupService = (DataCollectionGroup3Service) ejb3ServiceLocator
				.getLocalService(DataCollectionGroup3Service.class);
		this.gridInfoService = (GridInfo3Service) ejb3ServiceLocator.getLocalService(GridInfo3Service.class);
		this.gridInfoService = (GridInfo3Service) ejb3ServiceLocator.getLocalService(GridInfo3Service.class);
		this.workflowMeshService = (WorkflowMesh3Service) ejb3ServiceLocator.getLocalService(WorkflowMesh3Service.class);
		this.appService = (AutoProcProgram3Service) ejb3ServiceLocator.getLocalService(AutoProcProgram3Service.class);
		this.apService = (AutoProc3Service) ejb3ServiceLocator.getLocalService(AutoProc3Service.class);
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
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		try {
			BreadCrumbsForm bar = BreadCrumbsForm.getIt(request);
			// Just one of them could be visible on the bar
			if (bar.getSelectedDataCollectionGroup() != null) {
				return this.displayForDataCollectionGroup(mapping, actForm, request, response);
			} else if (bar.getSelectedSession() != null) {
				return this.displayForSession(mapping, actForm, request, response);
			} else if (bar.getSelectedSample() != null) {
				return this.displayForSample(mapping, actForm, request, response);
			} else if (bar.getSelectedProtein() != null) {
				return this.displayForProtein(mapping, actForm, request, response);
			}
			// ... or custom query
			else {
				return this.displayForCustomQuery(mapping, actForm, request, response);
			}
			// // Error...
			// else {
			// errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Unknown display type"));
			// }

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.collection.viewDataCollection"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}
		return null;

	}

	/**
	 * Display for All (session, Protein, Sample,...)
	 * 
	 * @param aList
	 *            dataCollection list
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	// @SuppressWarnings("unchecked")
	public ActionForward displayForAll(List<DataCollection3VO> dataCollectionList, ActionMapping mapping, ActionForm actForm,
			HttpServletRequest request, HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		HttpSession httpSession = request.getSession();
		ViewDataCollectionForm form = (ViewDataCollectionForm) actForm;
		boolean isIndus = SiteSpecific.isIndustrial(request);
		if (!isIndus)
			form.setIsUser(true);

		try {
			// data archived
			String proposal = getProposal(request);
			String beamlineName = getBeamlineName(request, form);
			FormUtils.setFormDisplayMode(request, actForm, FormUtils.EDIT_MODE);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.collection.viewDataCollection"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}
		LOG.debug("end displayAll");
		// return mapping.findForward("successAllLarge");
		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();
		if (role.equals(Constants.FXMANAGE_ROLE_NAME)) {
			return mapping.findForward("fedexmanagerSuccessAllLarge");
		} else if (role.equals(Constants.ROLE_MANAGER)) {
			return mapping.findForward("managerSuccessAllLarge");
		} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
			return mapping.findForward("localContactSuccessAllLarge");
		} else {
			return mapping.findForward("successAllLarge");
		}
	}

	public ActionForward displayForSession(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		ViewDataCollectionForm form = (ViewDataCollectionForm) actForm;
		List<DataCollection3VO> dataCollectionList = new ArrayList<DataCollection3VO>();
		Integer sessionId;
		boolean isIndus = SiteSpecific.isIndustrial(request);
		if (isIndus) {
			form.setEditSkipAndComments(false);
		} else
			form.setEditSkipAndComments(true);

		form.setMode("session");
		try {
			// Search first in request then in BreadCrumbs
			if (request.getParameter(Constants.SESSION_ID) != null)
				sessionId = new Integer(request.getParameter(Constants.SESSION_ID));
			else
				sessionId = BreadCrumbsForm.getIt(request).getSelectedSession().getSessionId();

			// Issue 1491: npe in export if the user selects collections directly fro; the session
			Session3VO slv = sessionService.findByPk(sessionId, false/* withCollections */, true, true);
			List<EnergyScan3VO> energyScanList = slv.getEnergyScansList();
			List<XFEFluorescenceSpectrum3VO> xfeList = slv.getXfeSpectrumsList();
			// Set EnergyScan list (for export)
			request.getSession().setAttribute(Constants.ENERGYSCAN_LIST, energyScanList);
			// Set XFE list (for export)
			request.getSession().setAttribute(Constants.XFE_LIST, xfeList);

			// Confidentiality (check if object proposalId matches)
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			if (!Confidentiality.isAccessAllowed(request, slv.getProposalVO().getProposalId())) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Access denied"));
				saveErrors(request, errors);
				return (mapping.findForward("error"));
			}

			LOG.debug("displayForSession: proposalId/sessionId = " + proposalId + "/" + sessionId);

			if (proposalId != null)
				proposalService.findByPk(proposalId);

			// Fill the information bar
			BreadCrumbsForm.getItClean(request).setSelectedSession(slv);

			// Get data to populate the form
			form.setSessionId(sessionId);

			// Get data collections
			// if (isIndus)
			// dataCollectionList = dataCollectionService.findFiltered(null, null, null, sessionId, new Byte("1"),
			// true, null);
			// else
			// dataCollectionList = dataCollectionService.findFiltered(null, null, null, sessionId, null, true, null);
			LOG.debug("ViewDataCollection:findBySessionId finished");

			// User or Industrial
			form.setIsIndustrial(isIndus);

			String rMerge = request.getParameter(Constants.RSYMM);
			String iSigma = request.getParameter(Constants.ISIGMA);
			request.getSession().setAttribute(Constants.RSYMM, rMerge);
			request.getSession().setAttribute(Constants.ISIGMA, iSigma);

		} catch (AccessDeniedException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
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

		return this.displayForAll(dataCollectionList, mapping, actForm, request, response);
	}

	/**
	 * For a search (sample, protein,beamline, experiment date) , display all collections
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayForCustomQuery(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		ViewDataCollectionForm form = (ViewDataCollectionForm) actForm;
		List<DataCollection3VO> dataCollectionList = new ArrayList<DataCollection3VO>();
		boolean isIndus = SiteSpecific.isIndustrial(request);
		form.setEditSkipAndComments(false);
		form.setMode("customQuery");
		try {
			// Get ProposalId
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

			LOG.debug("displayForCustomQuery: " + form.getSampleName() + "/" + form.getProteinAcronym() + "/" + form.getBeamlineName()
					+ "/" + form.getExperimentDateStart() + "/" + form.getExperimentDateEnd() + "/" + isIndus + " max="
					+ form.getMaxRecords());

			request.getSession().setAttribute("sampleName", form.getSampleName());
			request.getSession().setAttribute("proteinAcronym", form.getProteinAcronym());
			request.getSession().setAttribute("beamline", form.getBeamlineName());
			request.getSession().setAttribute("experimentDateStart", form.getExperimentDateStart());
			request.getSession().setAttribute("experimentDateEnd", form.getExperimentDateEnd());
			request.getSession().setAttribute("maxRecords", form.getMaxRecords());
			request.getSession().setAttribute("minMumberOfImages", form.getMinNumberOfImages());
			request.getSession().setAttribute("maxNumberOfImages", form.getMaxNumberOfImages());
			request.getSession().setAttribute("imagePrefix", form.getImagePrefix());

			// Get DataCollections
			long startTime = System.currentTimeMillis();
			int maxRecords = Integer.valueOf(Constants.MAX_RETRIEVED_DATACOLLECTIONS);
			if (StringUtils.isInteger(form.getMaxRecords()))
				maxRecords = Integer.parseInt(form.getMaxRecords());
			Integer minNumberOfImages = null;
			if (StringUtils.isInteger(form.getMinNumberOfImages()))
				minNumberOfImages = Integer.parseInt(form.getMinNumberOfImages());
			Integer maxNumberOfImages = null;
			if (StringUtils.isInteger(form.getMaxNumberOfImages()))
				maxNumberOfImages = Integer.parseInt(form.getMaxNumberOfImages());
			String imagePrefix = form.getImagePrefix();
			if (imagePrefix != null)
				imagePrefix = imagePrefix.trim();

			// dataCollectionList = (ArrayList) dataCollectionFull.findByCustomQuery(proposalId, form.getSampleName(),
			// form.getProteinAcronym(), form.getBeamlineName(),
			// QueryBuilder.toDate(form.getExperimentDateStart()),
			// QueryBuilder.toDate(form.getExperimentDateEnd()), minNumberOfImages, maxNumberOfImages, true,
			// Integer.valueOf(maxRecords));
			// dataCollectionList = dataCollectionService
			// .findByCustomQuery(proposalId, form.getSampleName(), form.getProteinAcronym(),
			// form.getBeamlineName(), StringUtils.toDate(form.getExperimentDateStart()),
			// StringUtils.toDate(form.getExperimentDateEnd()), minNumberOfImages, maxNumberOfImages,
			// imagePrefix, new Byte("1"), Integer.valueOf(maxRecords), true /* withScreenings */);
			long endTime = System.currentTimeMillis();
			LOG.debug("Getting data: " + (endTime - startTime) + " ms");

			// we do not want to see the form to fill session parameters
			form.setEditSkipAndComments(false);

			// Max values
			form.setNbRecords("" + dataCollectionList.size());

			// Clean BreadCrumbsForm
			BreadCrumbsForm.getItClean(request);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.collection.viewDataCollection"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		return this.displayForAll(dataCollectionList, mapping, actForm, request, response);
	}

	/**
	 * For a Protein Acronym, display all collections
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayForProtein(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		ViewDataCollectionForm form = (ViewDataCollectionForm) actForm;
		String proteinAcronym = null;
		List<DataCollection3VO> dataCollectionList = new ArrayList<DataCollection3VO>();
		boolean isIndus = SiteSpecific.isIndustrial(request);
		form.setEditSkipAndComments(false);

		LOG.debug("displayForProtein");
		form.setMode("protein");
		try {
			// Get ProposalId
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

			// Search first in request, then in BreadCrumbs, then in form
			if (request.getParameter(Constants.PROTEIN_ACRONYM) != null)
				proteinAcronym = new String(request.getParameter(Constants.PROTEIN_ACRONYM));
			else if (BreadCrumbsForm.getIt(request).getSelectedProtein() != null)
				proteinAcronym = BreadCrumbsForm.getIt(request).getSelectedProtein().getAcronym();
			else if (form.getProteinAcronym() != null)
				proteinAcronym = form.getProteinAcronym();
			if (proteinAcronym == null || proteinAcronym.equals(""))
				proteinAcronym = "%";

			proteinAcronym = proteinAcronym.replace('*', '%');
			Protein3VO plv = new Protein3VO();
			plv.setAcronym(proteinAcronym);

			// // Get Proteins
			List<Protein3VO> plvList = proteinService.findByAcronymAndProposalId(proposalId, proteinAcronym);
			if (plvList != null && plvList.size() == 1)
				plv = (Protein3VO) plvList.toArray()[0];
			else if (plvList == null || plvList.size() == 0) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Unknown Protein " + proteinAcronym));
				saveErrors(request, errors);
				return (mapping.findForward("error"));
			}

			// Get DataCollections
			long startTime = System.currentTimeMillis();
			// if (isIndus)
			// dataCollectionList = dataCollectionService.findByProtein(proteinAcronym, new Byte((byte) 0x1),
			// proposalId, true /* withScreenings */);
			// else
			// dataCollectionList = dataCollectionService
			// .findByProtein(proteinAcronym, null, proposalId, true /* withScreenings */);

			long endTime = System.currentTimeMillis();
			LOG.debug("Getting data: " + (endTime - startTime) + " ms");

			// Populate Breadcrumbs and Keep the originator for the purpose of "stay on page" save
			String originator = BreadCrumbsForm.getIt(request).getFromPage();
			BreadCrumbsForm.getItClean(request).setSelectedProtein(plv);
			BreadCrumbsForm.getIt(request).setFromPage(originator);

			// User or Industrial
			form.setIsIndustrial(true);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.collection.viewDataCollection"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		return this.displayForAll(dataCollectionList, mapping, actForm, request, response);
	}

	/**
	 * For a Sample name, display all collections.
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayForSample(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		ViewDataCollectionForm form = (ViewDataCollectionForm) actForm;
		List<DataCollection3VO> dataCollectionList = new ArrayList<DataCollection3VO>();

		String sampleName = null;
		boolean isIndus = SiteSpecific.isIndustrial(request);
		form.setEditSkipAndComments(false);

		LOG.debug("displayForSample");
		form.setMode("sample");
		try {
			// Get ProposalId
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

			if (request.getParameter("blSampleId") != null) {
				Integer sampleId = Integer.parseInt(request.getParameter("blSampleId"));
				BLSample3VO vo = sampleService.findByPk(sampleId, false, false, false);
				BreadCrumbsForm.getItClean(request).setSelectedSample(vo);
				return this.displayForAll(dataCollectionList, mapping, actForm, request, response);
			}
			// Search first in request, then in BreadCrumbs, then in form
			if (request.getParameter(Constants.SAMPLENAME) != null)
				sampleName = request.getParameter(Constants.SAMPLENAME);
			else if (request.getParameter(Constants.NAME) != null)
				sampleName = request.getParameter(Constants.NAME);
			else if (BreadCrumbsForm.getIt(request).getSelectedSample() != null)
				sampleName = BreadCrumbsForm.getIt(request).getSelectedSample().getName();
			else if (form.getSampleName() != null)
				sampleName = form.getSampleName();
			if (sampleName == null || sampleName.equals(""))
				sampleName = "%";
			sampleName = sampleName.replace('*', '%');

			BLSample3VO blsv = new BLSample3VO();
			blsv.setName(sampleName);

			// Get Samples
			List<BLSample3VO> blsvList = sampleService.findByCrystalNameCode(null, sampleName, null);
			if (blsvList != null && blsvList.size() == 1)
				blsv = (BLSample3VO) blsvList.toArray()[0];
			else if (blsvList == null || blsvList.size() == 0) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Unknown Sample " + sampleName));
				saveErrors(request, errors);
				return (mapping.findForward("error"));
			}

			// Get DataCollections
			//

			// Keep the originator for the purpose of "stay on page" save
			String originator = BreadCrumbsForm.getIt(request).getFromPage();
			BreadCrumbsForm.getItClean(request).setSelectedSample(blsv);
			BreadCrumbsForm.getIt(request).setFromPage(originator);

			// User or Industrial
			// DLS ####
			if (Constants.SITE_IS_DLS())
				form.setIsIndustrial(isIndus);
			// ESRF ####
			else if (Constants.SITE_IS_ESRF())
				form.setIsIndustrial(true);
			else
				form.setIsIndustrial(true);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.collection.viewDataCollection"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		return this.displayForAll(dataCollectionList, mapping, actForm, request, response);
	}

	/**
	 * Display all collections for a sample.
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayForSampleById(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		ViewDataCollectionForm form = (ViewDataCollectionForm) actForm;
		List<DataCollection3VO> dataCollectionList = new ArrayList<DataCollection3VO>();

		BLSample3VO blsv = new BLSample3VO();
		// BlsampleLightAndEnergyScanValue blsLightEnergyScanValue = new BlsampleLightAndEnergyScanValue();
		boolean isIndus = SiteSpecific.isIndustrial(request);
		form.setEditSkipAndComments(false);

		LOG.debug("displayForSampleById");
		form.setMode("sampleId");
		try {
			// Get ProposalId
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			// Get SampleId
			Integer sampleId = Integer.valueOf(request.getParameter(Constants.ID));

			// Confidentiality (check if proposalId matches)
			if (!Confidentiality.isAccessAllowedToSample(request, sampleId)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Access denied"));
				saveErrors(request, errors);
				return (mapping.findForward("error"));
			}

			// Get Sample
			blsv = sampleService.findByPk(sampleId, false, false, false);
			BreadCrumbsForm.getItClean(request).setSelectedSample(blsv);
			// Get DataCollections
			// if (isIndus) dataCollectionList = dataCollectionService.findBySample(sampleId, null, new Byte((byte) 0x1),
			// proposalId, true /* withScreenings */);
			// else
			// dataCollectionList = dataCollectionService
			// .findBySample(sampleId, null, null, proposalId, true /* withScreenings */);

			// Keep the originator for the purpose of "stay on page" save
			String originator = BreadCrumbsForm.getIt(request).getFromPage();
			BreadCrumbsForm.getItClean(request).setSelectedSample(blsv);
			BreadCrumbsForm.getIt(request).setFromPage(originator);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.collection.viewDataCollection"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		return this.displayForAll(dataCollectionList, mapping, actForm, request, response);

	}

	/**
	 * Save comments in one collection done
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward saveDataCollection(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();

		try {

			// Retrieve updated information from form
			ViewDataCollectionForm form = (ViewDataCollectionForm) actForm;

			// retrieve the object, change the fields and save it again
			// DataCollectionLightValue dclvFromDB =
			// dataCollection.findByPrimaryKeyLight(form.getSelectedDataCollection()
			// .getDataCollectionId());

			DataCollection3VO dclvFromDB = dataCollectionService.findByPk(form.getSelectedDataCollection().getDataCollectionId(),
					false, false);

			String DCcomments = form.getSelectedDataCollection().getComments();

			LOG.debug("comments: " + DCcomments);

			dclvFromDB.setComments(DCcomments);

			// manage the checkbox

			if ((form.getSelectedDataCollection().getPrintableForReport()) != null) {
				// ckecked : data collection should be kept when displaying report
				dclvFromDB.setPrintableForReport(new Byte("1"));
			} else {
				// not ckecked : data collection should not be kept when displaying report
				dclvFromDB.setPrintableForReport(new Byte("0"));
			}

			dataCollectionService.update(dclvFromDB);

			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.inserted", "New Data"));
			saveMessages(request, messages);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.collection.viewDataCollection"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}
		return this.display(mapping, actForm, request, response);

	}

	/**
	 * Retrieve details from a collection and put it on the form.
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward editDataCollection(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		try {

			Integer dataCollectionId = new Integer(request.getParameter(Constants.DATA_COLLECTION_ID));

			// DataCollectionLightValue dclv = dataCollection.findByPrimaryKeyLight(dataCollectionId);
			DataCollection3VO dclv = dataCollectionService.findByPk(dataCollectionId, false, false);

			ViewDataCollectionForm form = (ViewDataCollectionForm) actForm;
			form.setSelectedDataCollection(dclv);
			FormUtils.setFormDisplayMode(request, actForm, FormUtils.EDIT_MODE);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.collection.viewDataCollection"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		return (mapping.findForward("editDataCollection"));

	}

	/**
	 * Update DataCollection (crystal class, skip and comment)
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward updateDataCollection(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		try {
			ViewDataCollectionForm form = (ViewDataCollectionForm) actForm;

			Integer dataCollectionId = form.getTheDataCollectionId();
			Boolean skip = form.getSkip();
			String comments = form.getComments();

			// retrieve the object, change the fields and save it again
			DataCollection3VO dclvFromDB = dataCollectionService.findByPk(dataCollectionId, false, false);
			if (skip == Boolean.TRUE) {
				dclvFromDB.setPrintableForReport(Byte.valueOf("0"));
			} else {
				dclvFromDB.setPrintableForReport(Byte.valueOf("1"));
			}
			dclvFromDB.setComments(comments);

			dataCollectionService.update(dclvFromDB);

			System.out.println("FORM: ID=" + dataCollectionId + "   SKIP=" + skip + "   COMMENTS=" + comments);

			request.setAttribute("view", "2");

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.collection.viewDataCollection"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		return this.displayForSession(mapping, actForm, request, response);

	}

	/**
	 * Update dispatcher
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward update(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		String submitAction = request.getParameter("actionName");
		ViewDataCollectionForm form = (ViewDataCollectionForm) actForm;

		// sortOnImagePrefix
		if (submitAction != null && submitAction.equals("sortOnImagePrefix")) {
			return this.sortOnImagePrefix(mapping, actForm, request, response);
		}
		// sortOnStartTime
		else if (submitAction != null && submitAction.equals("sortOnStartTime")) {
			return this.sortOnStartTime(mapping, actForm, request, response);
		}
		// SaveDataCollection
		else if (submitAction != null && submitAction.equals("SaveDataCollection")) {
			return this.updateAllDataCollections(mapping, actForm, request, response);
		}
		// Sample Ranking
		else if (submitAction != null && submitAction.equals("Rank")) {
			return this.rank(mapping, actForm, request, response);
		}
		// AutoProc Ranking
		else if (submitAction != null && submitAction.equals("RankAutoProc")) {
			return this.rankAutoProc(mapping, actForm, request, response);
		}
		// Sample Ranking: CheckAll
		else if (submitAction != null && submitAction.equals("CheckAll")) {
			String[] idList = form.getIdList();
			Set<Integer> rankList = form.getRankList();
			for (int i = 0; i < idList.length; i++) {
				rankList.add(Integer.parseInt(idList[i]));
			}
			form.setRankList(rankList);
		}
		// Sample Ranking: UncheckAll
		else if (submitAction != null && submitAction.equals("UncheckAll")) {
			String[] idList = form.getIdList();
			Set<Integer> rankList = form.getRankList();
			for (int i = 0; i < idList.length; i++) {
				rankList.remove(Integer.parseInt(idList[i]));
			}
			form.setRankList(rankList);
		}
		// Select protein: SelectProtein
		else if (submitAction != null && submitAction.equals("SelectProtein")) {
			form.setFirstDisplay(true);

		}
		// Select experimentType: SelectProtein
		else if (submitAction != null && submitAction.equals("SelectExperimentType")) {

		}// Download autoprocessing
		else if (submitAction != null && submitAction.equals("Download")) {
			return this.download(mapping, actForm, request, response);
		}
		// dehydration charts
		// else if (submitAction != null && submitAction.equals("dehydration")) {
		// return this.drawDehydrationChart(mapping, actForm, request, response);
		// }
		// Error...
		else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Unknown submit button: " + submitAction));
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		return this.display(mapping, actForm, request, response);
	}

	/**
	 * Rank samples
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward rank(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		ViewDataCollectionForm form = (ViewDataCollectionForm) actForm;
		String selectedRankMode = form.getSelectedRankMode();
		if (!selectedRankMode.equals("EDNA"))
			return this.rankAutoProc(mapping, actForm, request, response);
		Set<Integer> rankList = form.getRankList();
		request.getSession().setAttribute(Constants.DATACOLLECTIONID_SET, rankList);

		if (rankList.size() != 0) {
			try {
				response.sendRedirect(request.getContextPath() + "/user/sampleRanking.do?reqCode=display");
			} catch (Exception e) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
				e.printStackTrace();
			}
		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail",
					"None of the data collections have been selected for ranking"));
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		return null;

	}

	/**
	 * Rank samples
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward rankAutoProc(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		ViewDataCollectionForm form = (ViewDataCollectionForm) actForm;
		Set<Integer> rankAutoProcList = form.getRankAutoProcList();
		request.getSession().setAttribute(Constants.DATACOLLECTIONID_SET, rankAutoProcList);

		if (rankAutoProcList.size() != 0) {
			try {
				response.sendRedirect(request.getContextPath() + "/user/autoProcRanking.do?reqCode=display");
			} catch (Exception e) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
				e.printStackTrace();
			}
		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail",
					"None of the data collections have been selected for ranking"));
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		return null;

	}

	/**
	 * Update All DataCollections
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward updateAllDataCollections(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		Integer dataCollectionId;
		Byte printable;
		String comments;

		try {

			ViewDataCollectionForm form = (ViewDataCollectionForm) actForm;

			String[] idList = form.getIdList();
			String[] commentsList = form.getCommentsList();

			String[] skipList = form.getSkipList();

			// if checkbox is unchecked then the value is null
			if (skipList.length < idList.length) {
				String[] tmpArray = new String[idList.length];
				System.arraycopy(skipList, 0, tmpArray, 0, skipList.length);
				skipList = tmpArray;
			}

			for (int i = 0; i < idList.length; i++) {

				dataCollectionId = new Integer(idList[i]);
				DataCollection3VO dclvFromDB = dataCollectionService.findByPk(dataCollectionId, false, false);

				comments = commentsList[i];
				comments = ViewDataCollectionAction.getDataCollectionComment(dclvFromDB, comments);
				dclvFromDB.setComments(comments);

				if (skipList[i] != null)
					printable = new Byte(skipList[i]);
				else
					printable = new Byte("1");
				dclvFromDB.setPrintableForReport(printable);

				dataCollectionService.update(dclvFromDB);

				LOG.debug("update datacollection: ID=" + dataCollectionId + "   COMMENTS=" + comments);

			}

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.collection.viewDataCollection"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		return this.displayForSession(mapping, actForm, request, response);

	}

	/**
	 * DownloadFile
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward downloadFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		// -------------------------------------------------------------------------------------------------
		// Retrieve Attributes
		String fullFilePath = request.getParameter(Constants.FULL_FILE_PATH);
		// -------------------------------------------------------------------------------------------------
		if (fullFilePath != null) {
			try {
				String mimeType = "text/plain";
				String shortFileName = StringUtils.getShortFilename(fullFilePath);

				ispyb.client.common.util.FileUtil.DownloadFile(PathUtils.FitPathToOS(fullFilePath), mimeType, shortFileName, response);
			} catch (Exception e) {
			}
		}

		return null;
	}

	/**
	 * DownloadFile
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward downloadHtmlFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		// -------------------------------------------------------------------------------------------------
		// Retrieve Attributes
		String fullFilePath = request.getParameter(Constants.FULL_FILE_PATH);
		// -------------------------------------------------------------------------------------------------
		if (fullFilePath != null) {
			try {
				String mimeType = "text/html";
				String shortFileName = StringUtils.getShortFilename(fullFilePath);

				ispyb.client.common.util.FileUtil.DownloadFile(PathUtils.FitPathToOS(fullFilePath), mimeType, shortFileName, response);
			} catch (Exception e) {
			}
		}

		return null;
	}

	/**
	 * foward to the page with dna results
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayHtmlFiles(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		String origFilePath = request.getParameter(Constants.FULL_FILE_PATH);
		ViewDataCollectionForm form = (ViewDataCollectionForm) actForm;

		try {
			String filePath = PathUtils.getPath(origFilePath);
			LOG.debug("displayHtmlFiles: htmlFilePath= " + filePath);

			String fullHtmlFileContent = FileUtil.fileToString(filePath);

			// Case where the file is not found
			if (fullHtmlFileContent == null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.datacollection.viewFile"));
				return this.display(mapping, actForm, request, response);
			}
			// reformat url
			int fileNameIndex = filePath.lastIndexOf("/");
			String pathWithoutFile = filePath.substring(0, fileNameIndex);
			LOG.debug("displayHtmlFiles: path = " + pathWithoutFile);

			String htmlContent = UrlUtils.formatURL(fullHtmlFileContent, pathWithoutFile);
			form.setHtmlContent(htmlContent);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.datacollection.viewFile", e.toString()));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));

			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		} else
			return mapping.findForward("viewHtmlFiles");
	}

	/**
	 * sortOnImagePrefix
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward sortOnImagePrefix(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ViewDataCollectionForm form = (ViewDataCollectionForm) actForm;
		LOG.debug("sortOnImagePrefix (previous order='" + form.getSortOrder() + "')");
		if (form.getSortOrder().equals(Constants.SORT_OnImagePrefixDesc))
			form.setSortOrder(Constants.SORT_OnImagePrefixAsc);
		else if (form.getSortOrder().equals(Constants.SORT_OnImagePrefixAsc))
			form.setSortOrder(Constants.SORT_OnImagePrefixDesc);
		else
			form.setSortOrder(Constants.SORT_OnImagePrefixDesc);
		return this.display(mapping, actForm, request, response);
	}

	/**
	 * sortOnStartTime
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward sortOnStartTime(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ViewDataCollectionForm form = (ViewDataCollectionForm) actForm;
		LOG.debug("sortOnStartTime (previous order='" + form.getSortOrder() + "')");
		if (form.getSortOrder().equals(Constants.SORT_OnStartTimeDesc))
			form.setSortOrder(Constants.SORT_OnStartTimeAsc);
		else if (form.getSortOrder().equals(Constants.SORT_OnStartTimeAsc))
			form.setSortOrder(Constants.SORT_OnStartTimeDesc);
		else
			form.setSortOrder(Constants.SORT_OnStartTimeDesc);
		return this.display(mapping, actForm, request, response);
	}

	public static AutoProcShellWrapper getAutoProcStatistics(Collection<DataCollection3VO> dataCollectionList, double rSymmCutoff,
			double iSigmaCutoff) throws Exception {
		ArrayList<Integer> dataCollectionIdsList = new ArrayList<Integer>();

		for (Iterator<DataCollection3VO> i = dataCollectionList.iterator(); i.hasNext();) {
			DataCollection3VO myDataCollection = i.next();
			if (myDataCollection != null)
				dataCollectionIdsList.add(myDataCollection.getDataCollectionId());
		}
		return getAutoProcStatisticsIds(dataCollectionIdsList, rSymmCutoff, iSigmaCutoff);

	}

	public static AutoProcShellWrapper getAutoProcStatisticsIds(Collection<Integer> dataCollectionList, double rSymmCutoff,
			double iSigmaCutoff) throws Exception {

		AutoProc3Service autoProcService = (AutoProc3Service) ejb3ServiceLocator.getLocalService(AutoProc3Service.class);
		SpaceGroup3Service spaceGroupService = (SpaceGroup3Service) ejb3ServiceLocator.getLocalService(SpaceGroup3Service.class);

		AutoProcScalingStatistics3Service apssService = (AutoProcScalingStatistics3Service) ejb3ServiceLocator
				.getLocalService(AutoProcScalingStatistics3Service.class);
		AutoProcIntegration3Service autoProcIntegrationService = (AutoProcIntegration3Service) ejb3ServiceLocator
				.getLocalService(AutoProcIntegration3Service.class);
		// form elements
		AutoProc3VO[] autoProcsForm = new AutoProc3VO[dataCollectionList.size()];
		AutoProcIntegration3VO[] autoProcIntegrationsForm = new AutoProcIntegration3VO[dataCollectionList.size()];
		AutoProcScalingStatistics3VO[] autoProcStatisticsOverallForm = new AutoProcScalingStatistics3VO[dataCollectionList.size()];
		AutoProcScalingStatistics3VO[] autoProcStatisticsInnerForm = new AutoProcScalingStatistics3VO[dataCollectionList.size()];
		AutoProcScalingStatistics3VO[] autoProcStatisticsOuterForm = new AutoProcScalingStatistics3VO[dataCollectionList.size()];

		int count = 0;

		for (Iterator<Integer> i = dataCollectionList.iterator(); i.hasNext(); count++) {
			Integer dataCollectionId = i.next();

			if (dataCollectionId == null)
				continue;

			List<AutoProc3VO> autoProcs = autoProcService.findByDataCollectionId(dataCollectionId);

			AutoProc3VO bestAutoProc = null;
			Integer bestSpaceGroupNumber = null;
			AutoProcScalingStatistics3VO outerBestStatistic = null;

			/*
			 * Strategy: Choose autoproc whose space group has highest symmetry (highest number), but ignore any autoproc whose best
			 * rMerge(s) and Isigma(s) are beyond their respective thresholds.
			 */
			for (Iterator<AutoProc3VO> j = autoProcs.iterator(); j.hasNext();) {
				AutoProc3VO currAutoProc = j.next();
				String currSpaceGroup = currAutoProc.getSpaceGroup();

				if (currSpaceGroup == null)
					continue;

				currSpaceGroup = currSpaceGroup.replace(" ", "");
				List<SpaceGroup3VO> spaceGroupValues = spaceGroupService.findBySpaceGroupShortName(currSpaceGroup);

				if (spaceGroupValues.isEmpty())
					continue; // some spacegroups do not yet have values.

				List<AutoProcScalingStatistics3VO> autoProcStatistics = apssService.findByAutoProcId(currAutoProc.getAutoProcId(),
						"innerShell");

				AutoProcScalingStatistics3VO innerBestStatistic = apssService.getBestAutoProcScalingStatistic(autoProcStatistics);

				if (innerBestStatistic == null)
					continue;
				if ((innerBestStatistic.getRmerge() != null && innerBestStatistic.getRmerge() > rSymmCutoff)
						|| (innerBestStatistic.getMeanIoverSigI() != null && innerBestStatistic.getMeanIoverSigI() < iSigmaCutoff))
					continue;

				SpaceGroup3VO sglv = spaceGroupValues.iterator().next(); // first
				// one
				Integer spaceGroupNumber = sglv.getSpaceGroupNumber();

				if (bestSpaceGroupNumber == null
						|| spaceGroupNumber.compareTo(bestSpaceGroupNumber) > 0
						|| (spaceGroupNumber.compareTo(bestSpaceGroupNumber) == 0 && (innerBestStatistic.getRmerge() != null
								&& outerBestStatistic.getRmerge() != null && innerBestStatistic.getRmerge().compareTo(
								outerBestStatistic.getRmerge()) < 0))) {

					bestSpaceGroupNumber = spaceGroupNumber;
					bestAutoProc = currAutoProc;
					outerBestStatistic = innerBestStatistic;
				}

			}

			autoProcsForm[count] = bestAutoProc;
			autoProcStatisticsInnerForm[count] = outerBestStatistic;
			if (bestAutoProc != null) {
				Collection<?> autoProcsOverall = apssService.findByAutoProcId(bestAutoProc.getAutoProcId(), "overall");
				Collection<?> autoProcsOuter = apssService.findByAutoProcId(bestAutoProc.getAutoProcId(), "outerShell");

				if (!autoProcsOverall.isEmpty())
					autoProcStatisticsOverallForm[count] = (AutoProcScalingStatistics3VO) autoProcsOverall.iterator().next();
				if (!autoProcsOuter.isEmpty())
					autoProcStatisticsOuterForm[count] = (AutoProcScalingStatistics3VO) autoProcsOuter.iterator().next();
				List<AutoProcIntegration3VO> autoProcIntegrations = autoProcIntegrationService.findByAutoProcId(bestAutoProc
						.getAutoProcId());
				if (!autoProcIntegrations.isEmpty()) {
					autoProcIntegrationsForm[count] = (autoProcIntegrations.iterator().next());

				}
			}

		}

		AutoProcShellWrapper wrapper = new AutoProcShellWrapper();
		wrapper.setAutoProcs(autoProcsForm);
		wrapper.setAutoProcIntegrations(autoProcIntegrationsForm);
		wrapper.setScalingStatsOverall(autoProcStatisticsOverallForm);
		wrapper.setScalingStatsInner(autoProcStatisticsInnerForm);
		wrapper.setScalingStatsOuter(autoProcStatisticsOuterForm);
		return wrapper;

	}
	
	/**
	 * build the path for archiving: /data/pyarch/beamlineDir/proposal
	 * 
	 * @param beamlineName
	 * @param proposal
	 * @throws Exception
	 */
	private String buildPathArchiving(String beamlineName, String proposal) throws Exception {
		String beamlineDir = getBeamlineDirectory(beamlineName);
		if (beamlineDir == null)
			return null;
		String dirPath = Constants.DATA_FILEPATH_START + beamlineDir + "/" + proposal;
		String archivingPath = PathUtils.getPath(dirPath);
		return archivingPath;
	}

	/**
	 * returns the directory corresponding to the beamlineName
	 * 
	 * @param beamlineName
	 * @return
	 */
	private String getBeamlineDirectory(String beamlineName) {
		if (Constants.SITE_IS_ESRF()) {
			return ESRFBeamlineEnum.retrieveDirectoryNameWithName(beamlineName);
		}
		if (Constants.SITE_IS_EMBL()) {
			return EMBLBeamlineEnum.retrieveDirectoryNameWithName(beamlineName);
		}
		if (Constants.SITE_IS_MAXIV()) {
			return MAXIVBeamlineEnum.retrieveDirectoryNameWithName(beamlineName);
		}
		return beamlineName.toLowerCase();
	}

	private String getProposal(HttpServletRequest request) {
		// proposal
		String proposalCode = (String) request.getSession().getAttribute(Constants.PROPOSAL_CODE);
		String proposalNumber = String.valueOf(request.getSession().getAttribute(Constants.PROPOSAL_NUMBER));
		String proposal = proposalCode + proposalNumber.toLowerCase();
		return proposal;
	}

	private String getBeamlineName(HttpServletRequest request, ViewDataCollectionForm form) throws FinderException, NamingException,
			Exception {
		// beamline
		Integer sessionId = form.getSessionId();
		Session3VO slv = null;
		if (sessionId == null) {
			BreadCrumbsForm bar = BreadCrumbsForm.getIt(request);
			slv = bar.getSelectedSession();
		} else {
			slv = sessionService.findByPk(sessionId, false, false, false);
		}
		String beamlineName = "";
		if (slv != null)
			beamlineName = slv.getBeamlineName();
		return beamlineName;
	}

	/**
	 * display the dataCollections for a given dataCollectionGroup
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayForDataCollectionGroup(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		ViewDataCollectionForm form = (ViewDataCollectionForm) actForm;
		List<DataCollection3VO> dataCollectionList = new ArrayList<DataCollection3VO>();
		Integer dataCollectionGroupId;
		boolean isIndus = SiteSpecific.isIndustrial(request);
		if (isIndus) {
			form.setEditSkipAndComments(false);
		} else
			form.setEditSkipAndComments(true);
		form.setMode("dataCollectionGroup");
		try {
			// Search first in request then in BreadCrumbs
			if (request.getParameter(Constants.DATA_COLLECTION_GROUP_ID) != null)
				dataCollectionGroupId = new Integer(request.getParameter(Constants.DATA_COLLECTION_GROUP_ID));
			else
				dataCollectionGroupId = BreadCrumbsForm.getIt(request).getSelectedDataCollectionGroup().getDataCollectionGroupId();

			DataCollectionGroup3VO dcg = dataCollectionGroupService.findByPk(dataCollectionGroupId, false, false);
			Session3VO slv = dcg.getSessionVO();
			Integer sessionId = slv.getSessionId();
			form.setDataCollectionGroupId(dataCollectionGroupId);

			// Confidentiality (check if object proposalId matches)
			// Integer proposalId = slv.getProposalId();
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			if (!Confidentiality.isAccessAllowed(request, slv.getProposalVO().getProposalId())) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Access denied"));
				saveErrors(request, errors);
				return (mapping.findForward("error"));
			}

			LOG.debug("displayForDataCollectionGroup: proposalId/sessionId = " + proposalId + "/" + sessionId);

			// Fill the information bar
			BreadCrumbsForm.getItClean(request).setSelectedSession(slv);
			BreadCrumbsForm.getIt(request).setSelectedDataCollectionGroup(dcg);
			if (dcg.getWorkflowVO() != null) {
				BreadCrumbsForm.getIt(request).setSelectedWorkflow(dcg.getWorkflowVO());
			}

			// Get data to populate the form
			form.setSessionId(sessionId);
			form.setDisplayType("group");

			// Get data collections
			// if (isIndus)
			// //dataCollectionList = (ArrayList) dataCollectionFull.findBySessionIdAndPrintable(sessionId,
			// //new Byte("1"));
			// dataCollectionList = dataCollectionService.findFiltered(null, null, null, null, new Byte("1"), true,
			// dcg.getDataCollectionGroupId());
			// else
			// // dataCollectionList = (ArrayList) dataCollectionFull.findBySessionId(sessionId);
			// dataCollectionList = dataCollectionService.findFiltered(null, null, null, null, null, true,
			// dcg.getDataCollectionGroupId());
			LOG.debug("ViewDataCollection:findByDataCollectionGroupId finished");

			// User or Industrial
			form.setIsIndustrial(isIndus);

			String rMerge = request.getParameter(Constants.RSYMM);
			String iSigma = request.getParameter(Constants.ISIGMA);
			request.getSession().setAttribute(Constants.RSYMM, rMerge);
			request.getSession().setAttribute(Constants.ISIGMA, iSigma);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.collection.viewDataCollection"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		return this.displayForAll(dataCollectionList, mapping, actForm, request, response);
	}

	public ActionForward displayLastCollect(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		return mapping.findForward("viewLastCollect");
	}

	@SuppressWarnings("unchecked")
	public void getSearchCriteria(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
		LOG.debug("getSearchCriteria");
		HashMap<String, Object> searchCriteria = (HashMap<String, Object>) request.getSession().getAttribute(
				"LastCollectSearchCriteria");
		// data => Gson
		GSonUtils.sendToJs(response, searchCriteria, "dd-MM-yyyy HH:mm:ss");
	}

	@SuppressWarnings("unchecked")
	public void getLastCollect(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
		LOG.debug("getLastCollect");
		try {
			List<DataCollectionBean> dataCollectionList = new ArrayList<DataCollectionBean>();
			List<IspybCrystalClass3VO> listOfCrystalClass = (List<IspybCrystalClass3VO>) request.getSession().getAttribute(
					Constants.ISPYB_CRYSTAL_CLASS_LIST);
			String bl = request.getParameter("searchBeamline");
			String d1 = request.getParameter("startDate");
			String d2 = request.getParameter("endDate");
			String t1 = request.getParameter("startTime");
			String t2 = request.getParameter("endTime");

			LOG.debug("... on beamline " + bl + ", startDate= " + d1 + ", endDate= " + d2 + ", startTime= " + t1 + ", endTime= " + t2
					+ ".");
			HashMap<String, Object> searchCriteria = new HashMap<String, Object>();
			searchCriteria.put("searchBeamline", bl);
			searchCriteria.put("startDate", d1);
			searchCriteria.put("endDate", d2);
			searchCriteria.put("startTime", t1);
			searchCriteria.put("endTime", t2);
			request.getSession().setAttribute("LastCollectSearchCriteria", searchCriteria);

			String[] beamLineSearch = null;
			if (bl != null) {
				beamLineSearch = bl.split(",");
				if (beamLineSearch != null) {
					List<String> list = new ArrayList<String>();
					for (int i = 0; i < beamLineSearch.length; i++) {
						List<String> bls = ESRFBeamlineEnum.getAllBeamlinesForName(beamLineSearch[i]);
						if (Constants.SITE_IS_EMBL()) {
							bls = EMBLBeamlineEnum.getAllBeamlinesForName(beamLineSearch[i]);
						}

						if (bls != null)
							list.addAll(bls);
					}
					beamLineSearch = list.toArray(new String[list.size()]);
				}
			}
			// Date today = Calendar.getInstance().getTime();
			// long yesterdayL = today.getTime() - (24 * 3600 * 1000);
			// Date startDate = new Date(yesterdayL);
			Calendar date = new GregorianCalendar();
			date.set(Calendar.HOUR_OF_DAY, 7);
			date.set(Calendar.MINUTE, 0);
			date.set(Calendar.SECOND, 0);
			date.set(Calendar.MILLISECOND, 0);
			Date startDate = date.getTime();

			if (d1 != null && !d1.equals("")) {
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				df.setLenient(false);
				try {
					startDate = df.parse(d1);
				} catch (Exception e) {

				}
			}
			Date endDate = null;
			if (d2 != null && !d2.equals("")) {
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				df.setLenient(false);
				try {
					endDate = df.parse(d2);
				} catch (Exception e) {

				}
			}
			if (t1 != null && !t1.equals("") && !t1.equals("undefined")) {
				try {
					SimpleDateFormat df = new SimpleDateFormat("HH:mm");
					df.setLenient(false);
					Date st = df.parse(t1);
					Calendar c = new GregorianCalendar();
					c.setTime(st);
					int h = c.get(Calendar.HOUR_OF_DAY);
					int m = c.get(Calendar.MINUTE);
					Calendar startcal = new GregorianCalendar();
					startcal.setTime(startDate);
					startcal.set(Calendar.HOUR_OF_DAY, h);
					startcal.set(Calendar.MINUTE, m);
					startDate = startcal.getTime();
				} catch (Exception e) {

				}
			}

			if (t2 != null && !t2.equals("") && !t2.equals("undefined")) {
				try {
					SimpleDateFormat df = new SimpleDateFormat("HH:mm");
					df.setLenient(false);
					Date et = df.parse(t2);
					Calendar c = new GregorianCalendar();
					c.setTime(et);
					int h = c.get(Calendar.HOUR_OF_DAY);
					int m = c.get(Calendar.MINUTE);
					Calendar endcal = new GregorianCalendar();
					endcal.setTime(endDate);
					endcal.set(Calendar.HOUR_OF_DAY, h);
					endcal.set(Calendar.MINUTE, m);
					endDate = endcal.getTime();
				} catch (Exception e) {

				}
			}
			// load last collect
			List<DataCollection3VO> listLastCollectVO = dataCollectionService
					.findLastCollect(startDate, endDate, beamLineSearch);
			// autoproc
			AutoProcShellWrapper wrapper = getAutoProcStatistics(listLastCollectVO, 10, 1);
			int dcIndex = 0;
			DecimalFormat df3 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
			df3.applyPattern("#####0.00");

			for (Iterator<DataCollection3VO> iterator = listLastCollectVO.iterator(); iterator.hasNext();) {

				DataCollection3VO dataCollection3VO = iterator.next();
				String beamLineName = dataCollection3VO.getDataCollectionGroupVO().getSessionVO().getBeamlineName();
				String proposal = dataCollection3VO.getDataCollectionGroupVO().getSessionVO().getProposalVO().getCode()
						+ dataCollection3VO.getDataCollectionGroupVO().getSessionVO().getProposalVO().getNumber();
				String proteinAcronym = "";
				BLSample3VO sample = dataCollection3VO.getDataCollectionGroupVO().getBlSampleVO();
				if (sample != null && sample.getCrystalVO() != null && sample.getCrystalVO().getProteinVO() != null)
					proteinAcronym = sample.getCrystalVO().getProteinVO().getAcronym();
				String pdbFileName = "";
				if (sample != null && sample.getCrystalVO() != null && sample.getCrystalVO().hasPdbFile()) {
					pdbFileName = sample.getCrystalVO().getPdbFileName();
				}
				String experimentType = dataCollection3VO.getDataCollectionGroupVO().getExperimentType();
				AutoProc3VO autoProc = null;
				AutoProcScalingStatistics3VO autoProcStatisticsOverall = null;
				AutoProcScalingStatistics3VO autoProcStatisticsInner = null;
				AutoProcScalingStatistics3VO autoProcStatisticsOuter = null;
				ScreeningOutputLattice3VO lattice = null;
				ScreeningOutput3VO screeningOutput = null;

				String snapshotFullPath = dataCollection3VO.getXtalSnapshotFullPath1();
				snapshotFullPath = PathUtils.FitPathToOS(snapshotFullPath);
				boolean hasSnapshot = false;
				if (snapshotFullPath != null)
					hasSnapshot = (new File(snapshotFullPath)).exists();

				autoProc = wrapper.getAutoProcs()[dcIndex];
				autoProcStatisticsOverall = wrapper.getScalingStatsOverall()[dcIndex];
				autoProcStatisticsInner = wrapper.getScalingStatsInner()[dcIndex];
				autoProcStatisticsOuter = wrapper.getScalingStatsOuter()[dcIndex];
				
				DataCollectionGroup3VO colGroup = dataCollectionGroupService.findByPk(dataCollection3VO.getDataCollectionGroupVOId(), false, true);
				Screening3VO[] tabScreening = colGroup.getScreeningsTab();

				if (tabScreening != null && tabScreening.length > 0) {
					Screening3VO screeningVO = tabScreening[0];
					ScreeningOutput3VO[] screeningOutputTab = screeningVO.getScreeningOutputsTab();
					if (screeningOutputTab != null && screeningOutputTab.length > 0) {
						if (screeningOutputTab[0].getScreeningOutputLatticesTab() != null
								&& screeningOutputTab[0].getScreeningOutputLatticesTab().length > 0) {
							lattice = screeningOutputTab[0].getScreeningOutputLatticesTab()[0];
						}
						screeningOutput = screeningOutputTab[0];
					}
				}

				boolean hasAutoProcAttachment = hasAutoProcAttachment(dataCollection3VO.getDataCollectionId());

				String autoprocessingStatus = "";
				String autoprocessingStep = "";
				if (wrapper != null && wrapper.getAutoProcs() != null && wrapper.getAutoProcs().length > dcIndex) {
					AutoProcIntegration3VO autoProcIntegration = wrapper.getAutoProcIntegrations()[dcIndex];
					if (autoProcIntegration != null) {
						List<AutoProcStatus3VO> autoProcEvents = autoProcIntegration.getAutoProcStatusList();
						if (autoProcEvents != null && autoProcEvents.size() > 0) {
							AutoProcStatus3VO st = autoProcEvents.get(autoProcEvents.size() - 1);
							autoprocessingStatus = st.getStatus();
							autoprocessingStep = st.getStep();
						}
					}
				}
				DataCollectionBean collect = new DataCollectionBean(dataCollection3VO, beamLineName, proposal, proteinAcronym,
						pdbFileName, experimentType, hasSnapshot, autoProc, autoProcStatisticsOverall, autoProcStatisticsInner,
						autoProcStatisticsOuter, screeningOutput, lattice, autoprocessingStatus, autoprocessingStep,
						hasAutoProcAttachment);
				dataCollectionList.add(collect);
				dcIndex++;
			}

			HashMap<String, Object> data = new HashMap<String, Object>();
			// context path
			data.put("contextPath", request.getContextPath());
			// listOfCrystal
			data.put("listOfCrystalClass", listOfCrystalClass);
			// dataCollectionList
			data.put("dataCollectionList", dataCollectionList);
			// beamlines names (search)
			String[] beamlineList = Constants.BEAMLINE_LOCATION;
			if (Constants.SITE_IS_ESRF()) {
				beamlineList = ESRFBeamlineEnum.getBeamlineNames();
			}
			if (Constants.SITE_IS_EMBL()) {
				beamlineList = EMBLBeamlineEnum.getBeamlineNames();
			}
			if (Constants.SITE_IS_MAXIV()) {
				beamlineList = MAXIVBeamlineEnum.getBeamlineNames();
			}
			data.put("beamlines", beamlineList);
			// isManager
			data.put("isManager", true);
			// lastCollect
			data.put("displayLastCollect", true);
			// data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void getDataCollectionForAll(HttpServletRequest request, HttpServletResponse response, List<String> errors,
			Integer sessionId, List<DataCollection3VO> dataCollectionList, int displayImage, int displayWorkflow, int displayMesh,
			int displayDehydration, Workflow workflow, SnapshotInfo snapshot, List<ImageValueInfo> imageList,
			List<DehydrationData> dehydrationDataValuesAll, List<MeshData> meshData, Integer dataCollectionGroupId) {

		try {
			boolean isManager = Confidentiality.isManager(request);
			boolean isIndus = SiteSpecific.isIndustrial(request);
			boolean isUser = !isIndus;
			boolean isFx = false;
			String proposalCode = (String) request.getSession().getAttribute(Constants.PROPOSAL_CODE);
			isFx = proposalCode != null && proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX) && isUser;

			List<IspybCrystalClass3VO> listOfCrystalClass = (List<IspybCrystalClass3VO>) request.getSession().getAttribute(
					Constants.ISPYB_CRYSTAL_CLASS_LIST);

			// list of beamlines
			List<String> listOfBeamlines = new ArrayList<String>();
			listOfBeamlines = ViewDataCollectionAction.getListOfBeamlines(dataCollectionList);

			// list of references
			List<IspybReference3VO> listOfReferences = new ArrayList<IspybReference3VO>();
			List<IspybReference3VO> allRef = (List<IspybReference3VO>) request.getSession().getAttribute(
					Constants.ISPYB_REFERENCE_LIST);
			listOfReferences = ViewReferenceAction.getReferences(listOfBeamlines, allRef, displayMesh);
			String referenceText = ViewReferenceAction.getReferenceText(listOfBeamlines);

			// dataCollection
			// autoproc
			double rSymmCutoff = 10;
			double iSigmaCutoff = 1;
			String rMerge = (String) request.getSession().getAttribute(Constants.RSYMM);
			String iSigma = (String) request.getSession().getAttribute(Constants.ISIGMA);

			try {
				if (rMerge != null && !rMerge.equals("undefined") && !rMerge.equals(""))
					rSymmCutoff = Double.parseDouble(rMerge);
				if (iSigma != null && !iSigma.equals("undefined") && !iSigma.equals(""))
					iSigmaCutoff = Double.parseDouble(iSigma);
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}

			// Set DataCollection list (for export)
			request.getSession().setAttribute(Constants.DATACOLLECTION_LIST, dataCollectionList);

			// Issue 1763: set the group comments to dc
			dataCollectionList = ViewDataCollectionAction.setDataCollectionComments(dataCollectionList);
			List<DataCollectionBean> collectionList = getDataCollectionBeanList(dataCollectionList, rSymmCutoff, iSigmaCutoff);

			HashMap<String, Object> data = new HashMap<String, Object>();
			// context path
			data.put("contextPath", request.getContextPath());
			// isFx
			data.put("isFx", isFx);
			// sessionId
			data.put("sessionId", sessionId);
			// dataCollectionGroupId
			data.put("dataCollectionGroupId", dataCollectionGroupId);
			// listOfCrystal
			data.put("listOfCrystalClass", listOfCrystalClass);
			// workflow
			data.put("workflowVO", workflow);
			// dataCollectionList
			data.put("dataCollectionList", collectionList);
			// beamlines names (search)
			String[] beamlineList = Constants.BEAMLINE_LOCATION;
			if (Constants.SITE_IS_ESRF()) {
				beamlineList = ESRFBeamlineEnum.getBeamlineNames();
			}
			if (Constants.SITE_IS_EMBL()) {
				beamlineList = EMBLBeamlineEnum.getBeamlineNames();
			}
			if (Constants.SITE_IS_MAXIV()) {
				beamlineList = MAXIVBeamlineEnum.getBeamlineNames();
			}
			data.put("beamlines", beamlineList);
			// displayImage
			data.put("displayImage", displayImage);
			// displayWorflow
			data.put("displayWorkflow", displayWorkflow);
			// displayMesh
			data.put("displayMesh", displayMesh);
			// displayDehydration
			data.put("displayDehydration", displayDehydration);
			// snapshot
			data.put("snapshot", snapshot);
			// images list
			data.put("imageList", imageList);
			// dehydration
			data.put("dehydrationValues", dehydrationDataValuesAll);
			// isManager
			data.put("isManager", isManager);
			// lastCollect
			data.put("displayLastCollect", false);
			// mesh data
			data.put("meshData", meshData);
			// list of references
			data.put("listOfReferences", listOfReferences);
			// referenceText
			data.put("referenceText", referenceText);
			// isIndustrial
			data.put("isIndustrial", isIndus);
			// rMerge & iSigma
			data.put("rMerge", rSymmCutoff);
			data.put("iSigma", iSigmaCutoff);
			// data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
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

	public void getDataCollectionBySession(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		List<DataCollection3VO> dataCollectionList = new ArrayList<DataCollection3VO>();
		boolean isIndus = SiteSpecific.isIndustrial(request);

		Integer sessionId = null;
		List<String> errors = new ArrayList<String>();

		try {
			// Search first in request then in BreadCrumbs
			if (request.getParameter(Constants.SESSION_ID) != null)
				sessionId = new Integer(request.getParameter(Constants.SESSION_ID));
			else
				sessionId = BreadCrumbsForm.getIt(request).getSelectedSession().getSessionId();

			// Issue 1491: npe in export if the user selects collections directly from the session
			Session3VO slv = sessionService.findByPk(sessionId, false/* withCollections */, true, true);
			List<EnergyScan3VO> energyScanList = slv.getEnergyScansList();
			List<XFEFluorescenceSpectrum3VO> xfeList = slv.getXfeSpectrumsList();
			// Set EnergyScan list (for export)
			request.getSession().setAttribute(Constants.ENERGYSCAN_LIST, energyScanList);
			// Set XFE list (for export)
			request.getSession().setAttribute(Constants.XFE_LIST, xfeList);

			// Confidentiality (check if object proposalId matches)
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			if (!Confidentiality.isAccessAllowed(request, slv.getProposalVO().getProposalId())) {
				errors.add("Access denied");
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				// data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return;
			}

			LOG.debug("displayForSession: proposalId/sessionId = " + proposalId + "/" + sessionId);

			if (proposalId != null)
				proposalService.findByPk(proposalId);

			// Fill the information bar
			BreadCrumbsForm.getItClean(request).setSelectedSession(slv);

			// Get data collections
			if (isIndus)
				// dataCollectionList = (ArrayList) dataCollectionFull.findBySessionIdAndPrintable(sessionId,
				// new Byte("1"));
				dataCollectionList = dataCollectionService.findFiltered(null, null, null, sessionId, new Byte("1"), null);
			else
				// dataCollectionList = (ArrayList) dataCollectionFull.findBySessionId(sessionId);
				dataCollectionList = dataCollectionService.findFiltered(null, null, null, sessionId, null, null);
			LOG.debug("ViewDataCollection:findBySessionId finished");

			int displayImage = 0;
			int displayWorkflow = 0;
			int displayMesh = 0;
			int displayDehydration = 0;

			// workflow info
			Workflow workflow = null;

			// snapshot info
			SnapshotInfo snapshot = null;
			// iamgeList
			List<ImageValueInfo> imageList = new ArrayList<ImageValueInfo>();
			// dehydration
			List<DehydrationData> dehydrationDataValuesAll = new ArrayList<DehydrationData>();
			// mesh data
			List<MeshData> meshData = new ArrayList<MeshData>();

			getDataCollectionForAll(request, response, errors, sessionId, dataCollectionList, displayImage, displayWorkflow,
					displayMesh, displayDehydration, workflow, snapshot, imageList, dehydrationDataValuesAll, meshData, null);
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

	public void getDataCollectionByDataCollectionGroup(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		List<DataCollection3VO> dataCollectionList = new ArrayList<DataCollection3VO>();
		Integer dataCollectionGroupId = null;
		boolean isIndus = SiteSpecific.isIndustrial(request);
		List<String> errors = new ArrayList<String>();

		try {
			// Search first in request then in BreadCrumbs
			if (BreadCrumbsForm.getIt(request).getSelectedDataCollectionGroup() != null)
				dataCollectionGroupId = BreadCrumbsForm.getIt(request).getSelectedDataCollectionGroup().getDataCollectionGroupId();
			else if (request.getParameter(Constants.DATA_COLLECTION_GROUP_ID) != null)
				dataCollectionGroupId = new Integer(request.getParameter(Constants.DATA_COLLECTION_GROUP_ID));
			else if (request.getSession().getAttribute(Constants.DATA_COLLECTION_GROUP_ID) != null) {
				dataCollectionGroupId = (Integer) request.getSession().getAttribute(Constants.DATA_COLLECTION_GROUP_ID);
			} else {
				ViewDataCollectionForm form = (ViewDataCollectionForm) actForm;
				dataCollectionGroupId = form.getDataCollectionGroupId();
			}

			if (dataCollectionGroupId == null) {
				errors.add("No dataCollectionGroup selected");
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				// data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return;
			}
			DataCollectionGroup3VO dcg = dataCollectionGroupService.findByPk(dataCollectionGroupId, false, false);
			Session3VO slv = dcg.getSessionVO();
			Integer sessionId = slv.getSessionId();

			// Confidentiality (check if object proposalId matches)
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			if (!Confidentiality.isAccessAllowed(request, slv.getProposalVO().getProposalId())) {
				errors.add("Access denied");
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				// data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return;
			}

			LOG.debug("displayForDataCollectionGroup: proposalId/sessionId = " + proposalId + "/" + sessionId);

			// Fill the information bar
			BreadCrumbsForm.getItClean(request).setSelectedSession(slv);
			BreadCrumbsForm.getIt(request).setSelectedDataCollectionGroup(dcg);

			// Get data collections
			if (isIndus) {
				dataCollectionList = dataCollectionService.findFiltered(null, null, null, null, new Byte("1"), 
						dcg.getDataCollectionGroupId());
			} else {
				dataCollectionList = dataCollectionService.findFiltered(null, null, null, null, null, 
						dcg.getDataCollectionGroupId());
			}
			LOG.debug("ViewDataCollection:findByDataCollectionGroupId finished");
			int[] displayWorkflowStatus = ViewWorkflowAction.getDisplayWorkflowStatus(dcg.getWorkflowVO());
			int displayImage = displayWorkflowStatus[0];
			int displayWorkflow = displayWorkflowStatus[1];
			int displayMesh = displayWorkflowStatus[2];
			int displayDehydration = displayWorkflowStatus[3];

			// workflow info
			Workflow workflow = ViewWorkflowAction.getWorkflowInfo(dcg.getWorkflowVO());

			// snapshot info
			String fullSnapshotPath = dcg.getXtalSnapshotFullPath();
			boolean fileExists = false;
			if (fullSnapshotPath != null) {
				fileExists = PathUtils.fileExists(fullSnapshotPath) == 1;
			}
			fullSnapshotPath = PathUtils.FitPathToOS(fullSnapshotPath);
			SnapshotInfo snapshot = new SnapshotInfo(fullSnapshotPath, fileExists);

			// imageList
			List<ImageValueInfo> imageList = FileUtil.getImageListForDataCollectionGroup(dcg.getDataCollectionGroupId(), null, null,
					null, null, request);

			FileInformation dataFile = null;
			int dataFileExists = 0;
			String dataFileFullPath = "";

			// mesh data
			List<MeshData> meshData = new ArrayList<MeshData>();
			WorkflowMesh3VO workflowMesh = null;
			List<WorkflowMesh3VO> listWFM = ViewWorkflowAction.getWorkflowMesh(workflow);
			if (listWFM != null && listWFM.size() > 0) {
				workflowMesh = listWFM.get(0);
			}
			if (workflow != null)
				workflow.setWorkflowMesh(workflowMesh);

			if (displayMesh == 1) {
				meshData = ViewWorkflowAction.getMeshData(dataCollectionList, workflowMesh);
			}

			getDataCollectionForAll(request, response, errors, sessionId, dataCollectionList, displayImage, displayWorkflow,
					displayMesh, displayDehydration, workflow, snapshot, imageList, null, meshData,
					dataCollectionGroupId);
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

	public void getDataCollectionBySample(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		List<DataCollection3VO> dataCollectionList = new ArrayList<DataCollection3VO>();
		boolean isIndus = SiteSpecific.isIndustrial(request);

		String sampleName = null;
		List<String> errors = new ArrayList<String>();

		try {
			ViewDataCollectionForm form = (ViewDataCollectionForm) actForm;
			// Search first in request, then in BreadCrumbs, then in form
			if (request.getParameter(Constants.SAMPLENAME) != null)
				sampleName = request.getParameter(Constants.SAMPLENAME);
			else if (request.getParameter(Constants.NAME) != null)
				sampleName = request.getParameter(Constants.NAME);
			else if (BreadCrumbsForm.getIt(request).getSelectedSample() != null)
				sampleName = BreadCrumbsForm.getIt(request).getSelectedSample().getName();
			else if (form.getSampleName() != null)
				sampleName = form.getSampleName();
			if (sampleName == null || sampleName.equals(""))
				sampleName = "%";
			sampleName = sampleName.replace('*', '%');

			BLSample3VO blsv = new BLSample3VO();
			blsv.setName(sampleName);
			if (BreadCrumbsForm.getIt(request).getSelectedSample() != null) {
				getDataCollectionBySampleId(mapping, actForm, request, response);
				return;
			}
			// Get Samples
			List<BLSample3VO> blsvList = sampleService.findByCrystalNameCode(null, sampleName, null);
			if (blsvList != null && blsvList.size() > 0)
				blsv = (BLSample3VO) blsvList.toArray()[0];
			else if (blsvList == null || blsvList.size() == 0) {
				errors.add("Unknown Sample " + sampleName);
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				// data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return;
			}

			// Confidentiality (check if object proposalId matches)
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			if (!Confidentiality.isAccessAllowedToSample(request, blsv.getBlSampleId())) {
				errors.add("Access denied");
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				// data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return;
			}

			if (isIndus)
				dataCollectionList = dataCollectionService
						.findBySample(null, sampleName, new Byte((byte) 0x1), proposalId);
			else
				dataCollectionList = dataCollectionService.findBySample(null, sampleName, null, proposalId);

			List<EnergyScan3VO> energyScanList = new ArrayList<EnergyScan3VO>();
			List<XFEFluorescenceSpectrum3VO> xfeList = new ArrayList<XFEFluorescenceSpectrum3VO>();
			// Set EnergyScan list (for export)
			request.getSession().setAttribute(Constants.ENERGYSCAN_LIST, energyScanList);
			// Set XFE list (for export)
			request.getSession().setAttribute(Constants.XFE_LIST, xfeList);

			LOG.debug("displayForSample: proposalId/sampleName = " + proposalId + "/" + sampleName);

			if (proposalId != null)
				proposalService.findByPk(proposalId);

			// Fill the information bar
			BreadCrumbsForm.getItClean(request).setSelectedSample(blsv);

			int displayImage = 0;
			int displayWorkflow = 0;
			int displayMesh = 0;
			int displayDehydration = 0;

			// workflow info
			Workflow workflow = null;

			// snapshot info
			SnapshotInfo snapshot = null;
			// iamgeList
			List<ImageValueInfo> imageList = new ArrayList<ImageValueInfo>();
			// dehydration
			List<DehydrationData> dehydrationDataValuesAll = new ArrayList<DehydrationData>();
			// mesh data
			List<MeshData> meshData = new ArrayList<MeshData>();

			getDataCollectionForAll(request, response, errors, null, dataCollectionList, displayImage, displayWorkflow, displayMesh,
					displayDehydration, workflow, snapshot, imageList, dehydrationDataValuesAll, meshData, null);
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

	public void getDataCollectionBySampleId(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		List<DataCollection3VO> dataCollectionList = new ArrayList<DataCollection3VO>();
		boolean isIndus = SiteSpecific.isIndustrial(request);

		List<String> errors = new ArrayList<String>();

		try {
			ViewDataCollectionForm form = (ViewDataCollectionForm) actForm;

			BLSample3VO blsv = new BLSample3VO();
			// Get SampleId
			Integer sampleId = null;
			if (request.getParameter(Constants.ID) != null)
				sampleId = Integer.valueOf(request.getParameter(Constants.ID));
			else if (BreadCrumbsForm.getIt(request).getSelectedSample() != null)
				sampleId = BreadCrumbsForm.getIt(request).getSelectedSample().getBlSampleId();
			// Get ProposalId
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			// Confidentiality (check if proposalId matches)
			if (!Confidentiality.isAccessAllowedToSample(request, sampleId)) {
				errors.add("Access denied");
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				// data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return;
			}

			blsv = sampleService.findByPk(sampleId, false, false, false);

			// Get DataCollections
			if (isIndus)
				dataCollectionList = dataCollectionService
						.findBySample(sampleId, null, new Byte((byte) 0x1), proposalId);
			else
				dataCollectionList = dataCollectionService.findBySample(sampleId, null, null, proposalId);

			List<EnergyScan3VO> energyScanList = new ArrayList<EnergyScan3VO>();
			List<XFEFluorescenceSpectrum3VO> xfeList = new ArrayList<XFEFluorescenceSpectrum3VO>();
			// Set EnergyScan list (for export)
			request.getSession().setAttribute(Constants.ENERGYSCAN_LIST, energyScanList);
			// Set XFE list (for export)
			request.getSession().setAttribute(Constants.XFE_LIST, xfeList);

			LOG.debug("displayForSampleId: proposalId/sampleId = " + proposalId + "/" + sampleId);

			if (proposalId != null)
				proposalService.findByPk(proposalId);

			// Fill the information bar
			BreadCrumbsForm.getItClean(request).setSelectedSample(blsv);

			int displayImage = 0;
			int displayWorkflow = 0;
			int displayMesh = 0;
			int displayDehydration = 0;

			// workflow info
			Workflow workflow = null;

			// snapshot info
			SnapshotInfo snapshot = null;
			// iamgeList
			List<ImageValueInfo> imageList = new ArrayList<ImageValueInfo>();
			// dehydration
			List<DehydrationData> dehydrationDataValuesAll = new ArrayList<DehydrationData>();
			// mesh data
			List<MeshData> meshData = new ArrayList<MeshData>();

			getDataCollectionForAll(request, response, errors, null, dataCollectionList, displayImage, displayWorkflow, displayMesh,
					displayDehydration, workflow, snapshot, imageList, dehydrationDataValuesAll, meshData, null);
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

	public void getDataCollectionByProtein(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		List<DataCollection3VO> dataCollectionList = new ArrayList<DataCollection3VO>();
		boolean isIndus = SiteSpecific.isIndustrial(request);

		List<String> errors = new ArrayList<String>();

		try {
			ViewDataCollectionForm form = (ViewDataCollectionForm) actForm;
			String proteinAcronym = null;

			// Search first in request, then in BreadCrumbs, then in form
			if (request.getParameter(Constants.PROTEIN_ACRONYM) != null)
				proteinAcronym = new String(request.getParameter(Constants.PROTEIN_ACRONYM));
			else if (BreadCrumbsForm.getIt(request).getSelectedProtein() != null)
				proteinAcronym = BreadCrumbsForm.getIt(request).getSelectedProtein().getAcronym();
			else if (form.getProteinAcronym() != null)
				proteinAcronym = form.getProteinAcronym();
			if (proteinAcronym == null || proteinAcronym.equals(""))
				proteinAcronym = "%";

			proteinAcronym = proteinAcronym.replace('*', '%');

			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

			// Get Proteins
			List<Protein3VO> plvList = proteinService.findByAcronymAndProposalId(proposalId, proteinAcronym);
			
			if (plvList == null || plvList.size() == 0) {
				errors.add("Unknown Protein " + proteinAcronym);
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				// data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return;
			}

			// Confidentiality (check if object proposalId matches)
			for (Iterator iterator = plvList.iterator(); iterator.hasNext();) {
				Protein3VO plv = (Protein3VO) iterator.next();
				
				if (!Confidentiality.isAccessAllowedToProtein(request, plv.getProteinId())) {
					errors.add("Access denied");
					HashMap<String, Object> data = new HashMap<String, Object>();
					data.put("errors", errors);
					// data => Gson
					GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
					return;
				}
			}
			

			// Get DataCollections
			long startTime = System.currentTimeMillis();
			if (isIndus)
				dataCollectionList = dataCollectionService
						.findByProtein(proteinAcronym, new Byte((byte) 0x1), proposalId);
			else
				dataCollectionList = dataCollectionService.findByProtein(proteinAcronym, null, proposalId);

			long endTime = System.currentTimeMillis();
			LOG.debug("Getting data: " + (endTime - startTime) + " ms");

			List<EnergyScan3VO> energyScanList = new ArrayList<EnergyScan3VO>();
			List<XFEFluorescenceSpectrum3VO> xfeList = new ArrayList<XFEFluorescenceSpectrum3VO>();
			// Set EnergyScan list (for export)
			request.getSession().setAttribute(Constants.ENERGYSCAN_LIST, energyScanList);
			// Set XFE list (for export)
			request.getSession().setAttribute(Constants.XFE_LIST, xfeList);

			LOG.debug("displayForProtein: proposalId/proteinAcronym = " + proposalId + "/" + proteinAcronym);

			if (proposalId != null)
				proposalService.findByPk(proposalId);

			// Fill the information bar with the first entry
			BreadCrumbsForm.getItClean(request).setSelectedProtein(plvList.get(0));

			int displayImage = 0;
			int displayWorkflow = 0;
			int displayMesh = 0;
			int displayDehydration = 0;

			// workflow info
			Workflow workflow = null;

			// snapshot info
			SnapshotInfo snapshot = null;
			// iamgeList
			List<ImageValueInfo> imageList = new ArrayList<ImageValueInfo>();
			// dehydration
			List<DehydrationData> dehydrationDataValuesAll = new ArrayList<DehydrationData>();
			// mesh data
			List<MeshData> meshData = new ArrayList<MeshData>();

			getDataCollectionForAll(request, response, errors, null, dataCollectionList, displayImage, displayWorkflow, displayMesh,
					displayDehydration, workflow, snapshot, imageList, dehydrationDataValuesAll, meshData, null);
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

	public void getDataCollectionByCustomQuery(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		List<DataCollection3VO> dataCollectionList = new ArrayList<DataCollection3VO>();
		boolean isIndus = SiteSpecific.isIndustrial(request);

		List<String> errors = new ArrayList<String>();

		try {
			ViewDataCollectionForm form = (ViewDataCollectionForm) actForm;
			// Get ProposalId
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

			String sampleName = (String) request.getSession().getAttribute("sampleName");
			String proteinAcronym = (String) request.getSession().getAttribute("proteinAcronym");
			String beamline = (String) request.getSession().getAttribute("beamline");
			String experimentDateStart = (String) request.getSession().getAttribute("experimentDateStart");
			String experimentDateEnd = (String) request.getSession().getAttribute("experimentDateEnd");
			String maxR = (String) request.getSession().getAttribute("maxRecords");
			String minNumberImages = (String) request.getSession().getAttribute("minMumberOfImages");
			String maxNumberImages = (String) request.getSession().getAttribute("maxNumberOfImages");
			String imagePrefix = (String) request.getSession().getAttribute("imagePrefix");

			LOG.debug("displayForCustomQuery: " + sampleName + "/" + proteinAcronym + "/" + beamline + "/" + experimentDateStart + "/"
					+ experimentDateEnd + "/" + isIndus + " max=" + maxR);

			// Get DataCollections
			int maxRecords = Integer.valueOf(Constants.MAX_RETRIEVED_DATACOLLECTIONS);
			if (StringUtils.isInteger(maxR))
				maxRecords = Integer.parseInt(maxR);
			Integer minNumberOfImages = null;
			if (StringUtils.isInteger(minNumberImages))
				minNumberOfImages = Integer.parseInt(minNumberImages);
			Integer maxNumberOfImages = null;
			if (StringUtils.isInteger(maxNumberImages))
				maxNumberOfImages = Integer.parseInt(maxNumberImages);
			if (imagePrefix != null)
				imagePrefix = imagePrefix.trim();
			// Get DataCollections
			dataCollectionList = dataCollectionService.findByCustomQuery(proposalId, sampleName, proteinAcronym, beamline,
					StringUtils.toDate(experimentDateStart), StringUtils.toDate(experimentDateEnd), minNumberOfImages,
					maxNumberOfImages, imagePrefix, new Byte("1"), Integer.valueOf(maxRecords));

			List<EnergyScan3VO> energyScanList = new ArrayList<EnergyScan3VO>();
			List<XFEFluorescenceSpectrum3VO> xfeList = new ArrayList<XFEFluorescenceSpectrum3VO>();
			// Set EnergyScan list (for export)
			request.getSession().setAttribute(Constants.ENERGYSCAN_LIST, energyScanList);
			// Set XFE list (for export)
			request.getSession().setAttribute(Constants.XFE_LIST, xfeList);

			// Max values
			form.setNbRecords("" + dataCollectionList.size());

			// Clean BreadCrumbsForm
			BreadCrumbsForm.getItClean(request);

			if (proposalId != null)
				proposalService.findByPk(proposalId);

			int displayImage = 0;
			int displayWorkflow = 0;
			int displayMesh = 0;
			int displayDehydration = 0;

			// workflow info
			Workflow workflow = null;

			// snapshot info
			SnapshotInfo snapshot = null;
			// iamgeList
			List<ImageValueInfo> imageList = new ArrayList<ImageValueInfo>();
			// dehydration
			List<DehydrationData> dehydrationDataValuesAll = new ArrayList<DehydrationData>();
			// mesh data
			List<MeshData> meshData = new ArrayList<MeshData>();

			getDataCollectionForAll(request, response, errors, null, dataCollectionList, displayImage, displayWorkflow, displayMesh,
					displayDehydration, workflow, snapshot, imageList, dehydrationDataValuesAll, meshData, null);
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

	public void saveAllDataCollection(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String comments;
			boolean skipForReport = false;
			Integer dcId;
			String dataCollectionList = request.getParameter("dataCollectionList");
			Gson gson = GSonUtils.getGson("dd-MM-yyyy");
			Type mapType = new TypeToken<ArrayList<DataCollectionBean>>() {
			}.getType();
			ArrayList<DataCollectionBean> dataCollections = gson.fromJson(dataCollectionList, mapType);
			if (dataCollections != null) {
				int nb = dataCollections.size();
				for (int i = 0; i < nb; i++) {
					dcId = dataCollections.get(i).getDataCollectionId();
					DataCollection3VO dcFromDB = dataCollectionService.findByPk(dcId, false, false);
					comments = dataCollections.get(i).getComments();
					comments = ViewDataCollectionAction.getDataCollectionComment(dcFromDB, comments);
					dcFromDB.setComments(comments);
					skipForReport = dataCollections.get(i).getSkipForReport();
					if (skipForReport)
						dcFromDB.setPrintableForReport((byte) 0);
					else
						dcFromDB.setPrintableForReport((byte) 1);
					dataCollectionService.update(dcFromDB);
					LOG.debug("update dataCollection: ID=" + dcId + "   COMMENTS=" + comments);
				}
			}
			if (BreadCrumbsForm.getIt(request).getSelectedDataCollectionGroup() != null)
				getDataCollectionByDataCollectionGroup(mapping, actForm, request, response);
			else
				getDataCollectionBySession(mapping, actForm, request, response);
		} catch (Exception exp) {
			exp.printStackTrace();
			response.getWriter().write(GSonUtils.getErrorMessage(exp));
			response.setStatus(500);
		}
	}

	public ActionForward rankJs(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {

		List<String> errors = new ArrayList<String>();
		String dataCollectionList = request.getParameter("dataCollectionIdList");
		if (dataCollectionList != null) {
			Set<Integer> dataCollectionIds = new HashSet<Integer>();
			String[] listIds = dataCollectionList.split(",");
			if (listIds != null) {
				for (int i = 0; i < listIds.length; i++) {
					String dc = listIds[i];
					try {
						Integer dcId = Integer.parseInt(dc);
						dataCollectionIds.add(dcId);
					} catch (NumberFormatException e) {

					}
				}

			}
			request.getSession().setAttribute(Constants.DATACOLLECTIONID_SET, dataCollectionIds);

			if (dataCollectionIds.size() != 0) {
				try {
					response.sendRedirect(request.getContextPath() + "/user/sampleRanking.do?reqCode=display");

				} catch (Exception e) {
					e.printStackTrace();
					errors.add(e.toString());
				}
			} else {
				errors.add("None of the data collections have been selected for ranking");
			}

			if (!errors.isEmpty()) {
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				// data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return null;
			}
		}
		return null;
	}

	public ActionForward rankAutoProcJs(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		List<String> errors = new ArrayList<String>();
		String dataCollectionList = request.getParameter("dataCollectionIdList");
		if (dataCollectionList != null) {
			Set<Integer> dataCollectionIds = new HashSet<Integer>();
			String[] listIds = dataCollectionList.split(",");
			if (listIds != null) {
				for (int i = 0; i < listIds.length; i++) {
					String dc = listIds[i];
					try {
						Integer dcId = Integer.parseInt(dc);
						dataCollectionIds.add(dcId);
					} catch (NumberFormatException e) {

					}
				}

			}
			request.getSession().setAttribute(Constants.DATACOLLECTIONID_SET, dataCollectionIds);

			if (dataCollectionIds.size() != 0) {
				try {
					response.sendRedirect(request.getContextPath() + "/user/autoProcRanking.do?reqCode=display");
				} catch (Exception e) {
					e.printStackTrace();
					errors.add(e.toString());
				}
			} else {
				errors.add("None of the data collections have been selected for ranking");
			}

			if (!errors.isEmpty()) {
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				// data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return null;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public ActionForward download(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();
		String dataCollectionIdst = request.getParameter(Constants.DATA_COLLECTION_ID);
		Integer dataCollectionId = null;
		if (dataCollectionIdst != null) {
			try {
				dataCollectionId = new Integer(dataCollectionIdst);
			} catch (NumberFormatException e) {
				// TODO
			}
		}

		if (dataCollectionId != null) {
			try {
				if (!Confidentiality.isAccessAllowedToDataCollection(request, dataCollectionId)) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Access denied"));
					saveErrors(request, errors);
					return (mapping.findForward("error"));
				}
				List<IspybAutoProcAttachment3VO> listOfAutoProcAttachment = (List<IspybAutoProcAttachment3VO>) request.getSession()
						.getAttribute(Constants.ISPYB_AUTOPROC_ATTACH_LIST);
				
				List<AutoProcProgramAttachment3VO> attachments = null;
				AutoProcAttachmentWebBean[] attachmentWebBeans = null;
				HashMap<String, String> filesToZip = new HashMap<String, String>();

				List<AutoProc3VO> autoProcs = apService.findByDataCollectionId(dataCollectionId);
				if (autoProcs != null && autoProcs.size() > 0) {
					for (int a = 0; a < autoProcs.size(); a++) {
						Integer autoProcProgramId = autoProcs.get(a).getAutoProcProgramVOId();
						// LOG.debug("..."+autoProcProgramId);
						if (autoProcProgramId != null) {
							attachments = appService.findByPk(autoProcProgramId, true).getAttachmentListVOs();
							if (!attachments.isEmpty()) {
								attachmentWebBeans = new AutoProcAttachmentWebBean[attachments.size()];
								int i = 0;
								for (Iterator<AutoProcProgramAttachment3VO> iterator = attachments.iterator(); iterator.hasNext();) {
									AutoProcProgramAttachment3VO att = iterator.next();
									AutoProcAttachmentWebBean attBean = new AutoProcAttachmentWebBean(att);
									// gets the ispyb auto proc attachment file
									IspybAutoProcAttachment3VO aAutoProcAttachment = ViewResultsAction.getAutoProcAttachment(
											attBean.getFileName(), listOfAutoProcAttachment);
									attBean.setIspybAutoProcAttachment(aAutoProcAttachment);
									attachmentWebBeans[i] = attBean;
									String fullFilePath = attBean.getFilePath().trim() + "/" + attBean.getFileName();
									fullFilePath = PathUtils.FitPathToOS(fullFilePath);
									// LOG.debug(fullFilePath);
									File f = new File(fullFilePath);
									if (f.canRead()) {										
										filesToZip.put(f.getName(), fullFilePath);
									}
									i = i + 1;
								}
							} else
								LOG.debug("attachments is empty");
						}
					}

				}
				// Issue 1507: Correction files for ID29 & ID23-1
				if (Constants.SITE_IS_ESRF()) {
					Integer sessionId = null;
					if (BreadCrumbsForm.getIt(request).getSelectedSession() != null)
						sessionId = BreadCrumbsForm.getIt(request).getSelectedSession().getSessionId();
					if (sessionId != null) {
						String beamLineName = BreadCrumbsForm.getIt(request).getSelectedSession().getBeamlineName();
						String[] correctionFiles = ESRFBeamlineEnum.retrieveCorrectionFilesNameWithName(beamLineName);
						if (correctionFiles != null) {
							for (int k = 0; k < correctionFiles.length; k++) {
								String correctionFileName = correctionFiles[k];
								String dir = ESRFBeamlineEnum.retrieveDirectoryNameWithName(beamLineName);
								if (dir != null) {
									String correctionFilePath = "/data/pyarch/" + dir + "/" + correctionFileName;
									String fullFilePath = PathUtils.FitPathToOS(correctionFilePath);
									File f = new File(fullFilePath);
									if (f.canRead()) {
										filesToZip.put(f.getName(), fullFilePath);
									}
								}
							}

						}
					}
				}
				// create zip
				DataCollection3VO dc = dataCollectionService.findByPk(dataCollectionId, false, false);
				//String s = "";
				String info = "";
				if (dc != null) {
					info += dc.getImagePrefix() + "_run" + dc.getDataCollectionNumber();
				}
				String outFilename = info + "_autoProcFilesFromDC.zip";
				outFilename = outFilename.replaceAll(" ", "_");

				try {
					byte[] zippedFiles = HashMapToZip.doZip(filesToZip);
					response.setContentType("application/zip");
					response.setHeader("Content-Disposition", "attachment;filename=" + outFilename);
					OutputStream  output = response.getOutputStream();
					output.write(zippedFiles);
					output.close();			
				} catch (IOException e) {
					e.printStackTrace();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail"));
					return this.display(mapping, actForm, request, response);
				}

			} catch (Exception e) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
				e.printStackTrace();
			}
		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail",
					"None of the data collections have been selected for downloading"));
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		// return this.display(mapping, actForm, request, response);
		return null;
	}

	public static List<DataCollectionBean> getDataCollectionBeanList(List<DataCollection3VO> dataCollectionList, double rSymmCutoff,
			double iSigmaCutoff) throws Exception {

		AutoProcShellWrapper wrapper = getAutoProcStatistics(dataCollectionList, rSymmCutoff, iSigmaCutoff);
		int dcIndex = 0;
		List<DataCollectionBean> collectionList = new ArrayList<DataCollectionBean>();

		for (Iterator<DataCollection3VO> iterator = dataCollectionList.iterator(); iterator.hasNext();) {
			DataCollection3VO dataCollection3VO = iterator.next();
			String beamLineName = dataCollection3VO.getDataCollectionGroupVO().getSessionVO().getBeamlineName();
			String proposal = dataCollection3VO.getDataCollectionGroupVO().getSessionVO().getProposalVO().getCode()
					+ dataCollection3VO.getDataCollectionGroupVO().getSessionVO().getProposalVO().getNumber();
			String proteinAcronym = "";
			BLSample3VO sample = dataCollection3VO.getDataCollectionGroupVO().getBlSampleVO();

			if (sample != null && sample.getCrystalVO() != null && sample.getCrystalVO().getProteinVO() != null)
				proteinAcronym = sample.getCrystalVO().getProteinVO().getAcronym();

			String pdbFileName = "";

			if (sample != null && sample.getCrystalVO() != null && sample.getCrystalVO().hasPdbFile()) {
				pdbFileName = sample.getCrystalVO().getPdbFileName();
			}
			String experimentType = dataCollection3VO.getDataCollectionGroupVO().getExperimentType();
			AutoProc3VO autoProc = null;
			AutoProcScalingStatistics3VO autoProcStatisticsOverall = null;
			AutoProcScalingStatistics3VO autoProcStatisticsInner = null;
			AutoProcScalingStatistics3VO autoProcStatisticsOuter = null;
			ScreeningOutputLattice3VO lattice = null;
			ScreeningOutput3VO screeningOutput = null;

			String snapshotFullPath = dataCollection3VO.getXtalSnapshotFullPath1();
			snapshotFullPath = PathUtils.FitPathToOS(snapshotFullPath);
			boolean hasSnapshot = false;
			if (snapshotFullPath != null)
				hasSnapshot = (new File(snapshotFullPath)).exists();

			autoProc = wrapper.getAutoProcs()[dcIndex];
			autoProcStatisticsOverall = wrapper.getScalingStatsOverall()[dcIndex];
			autoProcStatisticsInner = wrapper.getScalingStatsInner()[dcIndex];
			autoProcStatisticsOuter = wrapper.getScalingStatsOuter()[dcIndex];
			
			boolean hasAutoProcAttachment = ViewDataCollectionAction.hasAutoProcAttachment(dataCollection3VO.getDataCollectionId());

			String autoprocessingStatus = "";
			String autoprocessingStep = "";
			if (wrapper != null && wrapper.getAutoProcs() != null && wrapper.getAutoProcs().length > dcIndex) {
				AutoProcIntegration3VO autoProcIntegration = wrapper.getAutoProcIntegrations()[dcIndex];
				if (autoProcIntegration != null) {
					List<AutoProcStatus3VO> autoProcEvents = autoProcIntegration.getAutoProcStatusList();
					if (autoProcEvents != null && autoProcEvents.size() > 0) {
						AutoProcStatus3VO st = autoProcEvents.get(autoProcEvents.size() - 1);
						autoprocessingStatus = st.getStatus();
						autoprocessingStep = st.getStep();
					}
				}
			}
			DataCollectionBean collect = new DataCollectionBean(dataCollection3VO, beamLineName, proposal, proteinAcronym,
					pdbFileName, experimentType, hasSnapshot, autoProc, autoProcStatisticsOverall, autoProcStatisticsInner,
					autoProcStatisticsOuter, screeningOutput, lattice, autoprocessingStatus, autoprocessingStep, hasAutoProcAttachment);
			collectionList.add(collect);
			dcIndex++;
		}
		return collectionList;
	}

	private static boolean hasAutoProcAttachment(Integer dataCollectionId) throws Exception {
		boolean hasAutoProcAttachment = false;
		AutoProc3Service apService = (AutoProc3Service) ejb3ServiceLocator.getLocalService(AutoProc3Service.class);
		AutoProcProgram3Service appService = (AutoProcProgram3Service) ejb3ServiceLocator
				.getLocalService(AutoProcProgram3Service.class);
		List<AutoProc3VO> autoProcs = apService.findByDataCollectionId(dataCollectionId);
		if (autoProcs != null && autoProcs.size() > 0) {
			for (int a = 0; a < autoProcs.size(); a++) {
				Integer autoProcProgramId = autoProcs.get(a).getAutoProcProgramVOId();
				if (autoProcProgramId != null) {
					List<AutoProcProgramAttachment3VO> attachments = appService.findByPk(autoProcProgramId, true)
							.getAttachmentListVOs();
					if (attachments != null && attachments.size() > 0) {
						hasAutoProcAttachment = true;
						break;
					}
				}
			}
		}
		return hasAutoProcAttachment;
	}

	public static List<String> getListOfBeamlines(List<DataCollection3VO> dataCollectionList) {
		// list of beamlines
		List<String> listOfBeamlines = new ArrayList<String>();
		for (Iterator<DataCollection3VO> iterator = dataCollectionList.iterator(); iterator.hasNext();) {
			DataCollection3VO dataCollection3VO = iterator.next();
			String bl = dataCollection3VO.getDataCollectionGroupVO().getSessionVO().getBeamlineName();
			if (!listOfBeamlines.contains(bl)) {
				listOfBeamlines.add(bl);
			}
		}
		return listOfBeamlines;
	}

	public static List<DataCollection3VO> setDataCollectionComments(List<DataCollection3VO> listDC) {
		if (listDC == null)
			return null;
		List<DataCollection3VO> list = new ArrayList<DataCollection3VO>();
		for (Iterator<DataCollection3VO> iterator = listDC.iterator(); iterator.hasNext();) {
			DataCollection3VO dataCollection3VO = iterator.next();
			DataCollectionGroup3VO group = dataCollection3VO.getDataCollectionGroupVO();
			String groupComments = group.getComments();
			if (groupComments != null && !groupComments.trim().isEmpty()) {
				String collectComments = dataCollection3VO.getComments();
				String allComment = groupComments;
				if (collectComments != null && !collectComments.isEmpty()) {
					allComment += "  " + collectComments;
				}
				dataCollection3VO.setComments(allComment);
			}
			list.add(dataCollection3VO);
		}
		return list;
	}

	public static String getDataCollectionComment(DataCollection3VO dataCollection, String newComment) {
		if (dataCollection == null)
			return newComment;
		if (newComment == null || newComment.isEmpty()) {
			return newComment;
		}
		DataCollectionGroup3VO group = dataCollection.getDataCollectionGroupVO();
		String groupComment = group.getComments();
		if (groupComment == null || groupComment.trim().isEmpty()) {
			return newComment;
		}
		String finalComment = newComment;
		if (newComment.startsWith(groupComment)) {
			finalComment = newComment.substring(groupComment.length()).trim();
		}

		return finalComment;
	}
}
