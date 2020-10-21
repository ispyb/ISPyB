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
 * ExportDataCollectionUserAction.java
 */

package ispyb.client.mx.collection;

import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.util.GSonUtils;
import ispyb.client.mx.ranking.SampleRankingVO;
import ispyb.client.mx.ranking.autoProcRanking.AutoProcRankingVO;
import ispyb.common.util.Constants;
import ispyb.common.util.SendMailUtils;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.services.collections.DataCollectionGroup3Service;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.EnergyScan3VO;
import ispyb.server.mx.vos.collections.IspybCrystalClass3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.collections.XFEFluorescenceSpectrum3VO;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @struts.action name="exportDataCollectionForm" path="/user/exportDataCollection"
 *                input="userOnly.collection.viewDataCollection.page" parameter="reqCode" scope="request"
 *                validate="false"
 * 
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 * 
 */

public class ExportDataCollectionAction extends DispatchAction {

	private Session3Service sessionService;

	private Proposal3Service proposalService;
	
	private DataCollectionGroup3Service dcgService;

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private final static Logger LOG = Logger.getLogger(ExportDataCollectionAction.class);

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {

		this.sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
		this.proposalService = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
		this.dcgService = (DataCollectionGroup3Service) ejb3ServiceLocator.getLocalService(DataCollectionGroup3Service.class);

	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	public ActionForward exportAsPdf(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		try {
			ByteArrayOutputStream baos = prepareExport(mapping, actForm, request, response, false);

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

	public ActionForward exportAsRtf(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		try {
			ByteArrayOutputStream baos = prepareExport(mapping, actForm, request, response, true);

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

	public ActionForward exportAsCsv(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		try {
			ByteArrayOutputStream baos = prepareCsvExport(mapping, actForm, request, response);

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
	 * exportScreeningAsPdf
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward exportScreeningAsPdf(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();
		try {
			ByteArrayOutputStream baos = prepareScreeningExport(mapping, actForm, request, response, false, false,
					false);
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
	 * exportScreeningAsRtf
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward exportScreeningAsRtf(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();
		try {
			ByteArrayOutputStream baos = prepareScreeningExport(mapping, actForm, request, response, true, false, false);
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
	 * exportRankAsPdf
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward exportRankAsPdf(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();
		try {
			ByteArrayOutputStream baos = prepareScreeningExport(mapping, actForm, request, response, false, true, false);
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
	 * exportRankAsRtf
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward exportRankAsRtf(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();
		try {
			ByteArrayOutputStream baos = prepareScreeningExport(mapping, actForm, request, response, true, true, false);
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
	 * create the PDF report and send it by mail. Only used for MXPress experiments
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward exportAndSendAsPdf(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();
		try {
			ByteArrayOutputStream baos = prepareExport(mapping, actForm, request, response, false);
			return exportReportAndSendAsPdf(mapping, actForm, request, response, baos, "");
		
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
	 * send a PDF report  by mail. Only used for MXPress experiments
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward exportReportAndSendAsPdf(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response, ByteArrayOutputStream baos, String prefixName) {

		ActionMessages errors = new ActionMessages();
		String proposalCode = (String) request.getSession().getAttribute(Constants.PROPOSAL_CODE);
		String proposalNumber = String.valueOf(request.getSession().getAttribute(Constants.PROPOSAL_NUMBER));
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

		try {
			byte[] data = baos.toByteArray();

			Session3VO slv = BreadCrumbsForm.getIt(request).getSelectedSession();

			Proposal3VO pv = proposalService.findByPk(proposalId);

			String serverName = request.getServerName().toLowerCase();
			// String mpEmail = personService.findByPk(pv.getPersonVOId(), false).getEmailAddress();
			String mpEmail = pv.getPersonVO().getEmailAddress();
			String from = Constants.getProperty("mail.report.from.mxind");
			String bcc = null;

			SimpleDateFormat simple1 = new SimpleDateFormat("dd/MM/yyyy");
			String date = simple1.format(slv.getStartDate());
			String subject = "MXpress FX " + proposalNumber + " - " + date + " on " + slv.getBeamlineName();

			SimpleDateFormat simple = new SimpleDateFormat("ddMMyyyy");
			date = simple.format(slv.getStartDate());
			String attachName = prefixName+proposalCode + proposalNumber + "-" + date + "-" + slv.getBeamlineName() + ".pdf";
			String mimeType = "application/pdf";

			String to = Constants.getProperty("mail.report.to.test");
			String cc = Constants.getProperty("mail.report.cc.test");
			String body = Constants.getProperty("mail.report.body.test");
			LOG.debug("PDF export : server " + serverName);

			if (Constants.isServerProd(serverName)) {
				to = mpEmail;
				cc = Constants.getProperty("mail.report.cc");
				// ESRF ####
				if (Constants.SITE_IS_ESRF()) {
					bcc = Constants.getProperty("mail.report.bcc");
					if (proposalNumber.equals("12"))
						cc = cc + "," + Constants.getProperty("mail.report.cc.fx12");
				}
				body = Constants.getProperty("mail.report.body");
			}

			if (data != null) {
				SendMailUtils.sendMail(from, to, cc, bcc, subject, body, attachName, baos, mimeType, true);
				LOG.debug("PDF export : sendMail to " + to + " cc " + cc);
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

		return null;

	}

	@SuppressWarnings("unchecked")
	private ByteArrayOutputStream prepareExport(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response, boolean rtfFormat) throws Exception {

		List dataCollectionList = new ArrayList();
		String proposalCode = (String) request.getSession().getAttribute(Constants.PROPOSAL_CODE);
		String proposalNumber = String.valueOf(request.getSession().getAttribute(Constants.PROPOSAL_NUMBER));
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
		// Get data to populate the form
		BreadCrumbsForm bar = BreadCrumbsForm.getIt(request);
		// session
		Session3VO slv = null;
		Integer sessionId = null;
		// Session
		if (bar.getSelectedSession() != null) {
			sessionId = bar.getSelectedSession().getSessionId();
			slv = sessionService.findByPk(sessionId, false, false, false);
			bar.setSelectedSession(slv);
		}
		// sample name
		String name = null;
		if (bar.getSelectedSample() != null) {
			name = bar.getSelectedSample().getName();
		}
		// Protein
		else if (bar.getSelectedProtein() != null) {
			name = bar.getSelectedProtein().getAcronym();
		}

		// Filename
		String filename = getFileName(bar, proposalCode, proposalNumber, sessionId, slv, name, false, false, false);
		
		// Get DataCollection list, remove non printable DataCollections and format comments
		dataCollectionList = (ArrayList) request.getSession().getAttribute(Constants.DATACOLLECTION_LIST);
		dataCollectionList = findByPrintable(dataCollectionList);
		dataCollectionList = ViewDataCollectionAction.setDataCollectionComments(dataCollectionList);
		
		// Get EnergyScan list
		List<EnergyScan3VO> energyScanList = (ArrayList) request.getSession().getAttribute(Constants.ENERGYSCAN_LIST);
		// Get XFE list
		List<XFEFluorescenceSpectrum3VO> xfeList = (ArrayList) request.getSession().getAttribute(Constants.XFE_LIST);
		// auto proc wrapper
		AutoProcShellWrapper wrapper = getAutoProcWrapper(actForm, dataCollectionList);

		// Get an object list.
		List<IspybCrystalClass3VO> listOfCrystalClass = (List<IspybCrystalClass3VO>) request.getSession().getAttribute(
				Constants.ISPYB_CRYSTAL_CLASS_LIST);
		// nb crystal per classes
		List<Integer> listOfNbCrystalPerClass = getListOfNbCrystalPerClass(listOfCrystalClass, proposalCode,
				dataCollectionList, energyScanList, xfeList);
		// sampleRankingVO
		List<SampleRankingVO> sampleRankingVOList = (ArrayList) request.getSession().getAttribute(
				Constants.SAMPLE_RANKING_LIST);
		// autoProcRankingVO
		List<AutoProcRankingVO> autoProcRankingVOList = (ArrayList) request.getSession().getAttribute(
				Constants.AUTOPROC_RANKING_LIST);

		ByteArrayOutputStream baos;
		PdfRtfExporter exporter = new PdfRtfExporter(name, proposalCode, proposalNumber, slv, dataCollectionList,
				energyScanList, xfeList, listOfCrystalClass, listOfNbCrystalPerClass, wrapper, sampleRankingVOList,
				autoProcRankingVOList, null, null);
		baos = exporter.exportDataCollectionReport(rtfFormat);

		// setting some response headers
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		// setting the content type
		String fileExtension = (rtfFormat) ? ".rtf" : ".pdf";
		String mimeType = (rtfFormat) ? "application/rtf" : "application/pdf";
		response.setContentType(mimeType);
		filename += fileExtension;
		response.setHeader("Content-Disposition", "attachment; filename=" + filename);
		// the contentlength is needed for MSIE!!!
		response.setContentLength(baos.size());
		// write ByteArrayOutputStream to the ServletOutputStream
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		out.flush();
		out.close();

		BreadCrumbsForm.setIt(request, bar);

		return baos;
	}

	private ByteArrayOutputStream prepareCsvExport(ActionMapping mapping, ActionForm actForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		List aList = new ArrayList();
		Integer sessionId;
		String name = null;
		Session3VO slv = new Session3VO();
		String proposalCode = (String) request.getSession().getAttribute(Constants.PROPOSAL_CODE);
		String proposalNumber = String.valueOf(request.getSession().getAttribute(Constants.PROPOSAL_NUMBER));

		// Filename
		String filename = proposalCode + proposalNumber + "_";

		// Get data to populate the form
		BreadCrumbsForm bar = BreadCrumbsForm.getIt(request);

		// Get DataCollection list
		aList = (ArrayList) request.getSession().getAttribute(Constants.DATACOLLECTION_LIST);
		aList = findByPrintable(aList);

		// By Session
		if (bar.getSelectedSession() != null) {
			sessionId = bar.getSelectedSession().getSessionId();
			slv = sessionService.findByPk(sessionId, false, false, false);
			bar.setSelectedSession(slv);
			// aList = (ArrayList) dataCollection.findBySessionIdAndPrintableLight(sessionId, new Byte((byte)0x1));
			// aList = (ArrayList) dataCollection.findBySessionIdAndPrintable(sessionId, new Byte((byte)0x1));
			filename += "Session_";
			filename += slv.getBeamlineName() + "_";
			SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyyMMdd");
			filename += formater.format(slv.getStartDate());
			LOG.debug("CSV export: " + filename + " (size: " + aList.size() + ")");
		}
		// By Sample
		else if (bar.getSelectedSample() != null) {
			name = bar.getSelectedSample().getName();
			// aList = (ArrayList) dataCollection.findByNameAndPrintable(name, new Byte((byte)0x1), proposalId);
			filename += "Sample_" + name;
			LOG.debug("CSV export: " + filename + " (size: " + aList.size() + ")");
		}
		// By Protein
		else if (bar.getSelectedProtein() != null) {
			name = bar.getSelectedProtein().getAcronym();
			// aList = (ArrayList) dataCollection.findByAcronymAndPrintableLight(name, new Byte((byte)0x1), proposalId);
			// aList = (ArrayList) dataCollection.findByAcronymAndPrintable(name, new Byte((byte)0x1), proposalId);
			filename += "Protein_" + name;
			LOG.debug("CSV export: " + filename + " (size: " + aList.size() + ")");
		}
		// Custom query
		else {
			// ViewDataCollectionForm form = (ViewDataCollectionForm) actForm;
			// int maxRecords = Integer.valueOf(Constants.MAX_RETRIEVED_DATACOLLECTIONS);
			// if ( StringUtils.isInteger(form.getMaxRecords()) ) maxRecords = Integer.parseInt(form.getMaxRecords());
			// aList = (ArrayList) dataCollection.findByCustomQuery(
			// proposalId,
			// form.getSampleName(),
			// form.getProteinAcronym(),
			// form.getBeamlineName(),
			// QueryBuilder.toDate(form.getExperimentDateStart()),
			// QueryBuilder.toDate(form.getExperimentDateEnd()),
			// true,
			// Integer.valueOf(maxRecords)
			// );
			filename += "CustomQuery_";
			SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyyMMdd");
			Calendar cal = Calendar.getInstance();
			filename += formater.format(cal.getTime());
			LOG.debug("CSV export: " + filename + " (size: " + aList.size() + ")");
		}

		// Generate export
		filename = filename.replaceAll(" ", "_");
		CsvExporter csv = new CsvExporter(aList, slv, name, proposalCode, proposalNumber);
		ByteArrayOutputStream baos = csv.exportAsCsvUserOnly();

		// Setting some response headers
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		// Setting the content type
		response.setContentType("application/excel");
		filename += ".csv";
		response.setHeader("Content-Disposition", "attachment; filename=" + filename);
		// The contentlength is needed for MSIE!!!
		response.setContentLength(baos.size());
		// Write ByteArrayOutputStream to the ServletOutputStream
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		out.flush();
		out.close();

		BreadCrumbsForm.setIt(request, bar);

		return baos;
	}

	/**
	 * Keep only printable DataCollections
	 * 
	 * @param aList
	 * @return
	 */
	private List<DataCollection3VO> findByPrintable(List<DataCollection3VO> aList) {

		ArrayList<DataCollection3VO> res = new ArrayList<DataCollection3VO>();
		if (aList == null)
			return res;
		Iterator<DataCollection3VO> it = aList.iterator();
		while (it.hasNext()) {
			DataCollection3VO dataCollection = (DataCollection3VO) it.next();
			if (dataCollection.getPrintableForReport() != null && dataCollection.getPrintableForReport() == 1) {
				res.add(dataCollection);
			}
		}
		return res;
	}

	/**
	 * Returns the index of the crystal class in the list (same code), -1 if the crystal class is not in the list
	 * 
	 * @param list
	 * @param crystalClass
	 * @return
	 */
	private int getCrystalClassIndex(List<IspybCrystalClass3VO> list, String crystalClass) {
		int nb = list.size();
		for (int i = 0; i < nb; i++) {
			if (crystalClass != null && list.get(i).getCrystalClassCode().equals(crystalClass.toUpperCase().trim())) {
				return i;
			}
		}
		return -1;
	}

	private String getFileName(BreadCrumbsForm bar, String proposalCode, String proposalNumber, Integer sessionId,
			Session3VO slv, String name, boolean detailed, boolean ranking, boolean workflow) {
		// Filename
		String filename = proposalCode + proposalNumber + "_";
		if (ranking) {
			filename += "ranking_";
		} else if (detailed) {
			filename += "detailed_";
		}else if (workflow) {
			filename += "mxpresseo";
		}

		// Session
		if (bar.getSelectedSession() != null) {
			filename += "Session_";
			filename += slv.getBeamlineName() + "_";
			SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyyMMdd");
			filename += formater.format(slv.getStartDate());
		}
		// Sample
		else if (bar.getSelectedSample() != null) {
			filename += "Sample_" + name;
		}
		// Protein
		else if (bar.getSelectedProtein() != null) {
			filename += "Protein_" + name;
		}
		// Custom query
		else {
			filename += "CustomQuery_";
			SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyyMMdd");
			Calendar cal = Calendar.getInstance();
			filename += formater.format(cal.getTime());
		}
		filename = filename.replaceAll(" ", "_");
		return filename;
	}

	private AutoProcShellWrapper getAutoProcWrapper(ActionForm actForm, List<DataCollection3VO> dataCollectionList)
			throws Exception {
		ExportDataCollectionForm form = (ExportDataCollectionForm) actForm;
		double rMerge = 10.0;
		double iSigma = 1.0;

		if (form.getRmergeCutoff() != null)
			rMerge = Double.parseDouble(form.getRmergeCutoff());
		if (form.getIsigmaCutoff() != null)
			iSigma = Double.parseDouble(form.getIsigmaCutoff());

		AutoProcShellWrapper wrapper = ViewDataCollectionAction.getAutoProcStatistics(dataCollectionList, rMerge,
				iSigma);
		return wrapper;
	}

	private List<Integer> getListOfNbCrystalPerClass(List<IspybCrystalClass3VO> listOfCrystalClass,
			String proposalCode, List<DataCollection3VO> dataCollectionList, List<EnergyScan3VO> energyScanList,
			List<XFEFluorescenceSpectrum3VO> xfeList) {
		List<Integer> listOfNbCrystalPerClass = new ArrayList<Integer>(); // number of crystals / crystal classes
		String crystalClass;

		if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX)) {
			// for MXPress users, the table with crystal class will be displayed
			// calculate number of Crystals of class 1,2, ... 5, T
			// Browse dataCollections
			int nbCC = listOfCrystalClass.size();
			for (int i = 0; i < nbCC; i++) {
				listOfNbCrystalPerClass.add(0);
			}
			// count per dataCollectionGroup
			List<DataCollectionGroup3VO> listDataCollectionGroup = new ArrayList<DataCollectionGroup3VO>();
			List<Integer> listDataCollectionGroupIds = new ArrayList<Integer>();
			for (Iterator<DataCollection3VO> it = dataCollectionList.iterator(); it.hasNext();) {
				DataCollectionGroup3VO dcg = it.next().getDataCollectionGroupVO();
				Integer gId =dcg.getDataCollectionGroupId();
				if (!listDataCollectionGroupIds.contains(gId)){
					listDataCollectionGroupIds.add(gId);
					listDataCollectionGroup.add(dcg);
				}
			}
			for (Iterator<DataCollectionGroup3VO> it = listDataCollectionGroup.iterator(); it.hasNext();) {
				// crystalClass = ((DataCollectionLightValue)it.next()).getCrystalClass();
				crystalClass = (it.next()).getCrystalClass();
				int idCc = getCrystalClassIndex(listOfCrystalClass, crystalClass);
				if (idCc != -1) {
					listOfNbCrystalPerClass.set(idCc, listOfNbCrystalPerClass.get(idCc) + 1);
				}
			}
			// Browse energyScans
			if (energyScanList != null) {
				for (Iterator<EnergyScan3VO> it = energyScanList.iterator(); it.hasNext();) {
					crystalClass = (it.next()).getCrystalClass();
					int idCc = getCrystalClassIndex(listOfCrystalClass, crystalClass);
					if (idCc != -1 && crystalClass != null && crystalClass.toUpperCase().trim().equals("E")) {
						listOfNbCrystalPerClass.set(idCc, listOfNbCrystalPerClass.get(idCc) + 1);
					}
				}
			}
			// Browse XRF Spectra
			if (xfeList != null) {
				for (Iterator<XFEFluorescenceSpectrum3VO> it = xfeList.iterator(); it.hasNext();) {
					crystalClass = (it.next()).getCrystalClass();
					int idCc = getCrystalClassIndex(listOfCrystalClass, crystalClass);
					if (idCc != -1 && crystalClass != null && crystalClass.toUpperCase().trim().equals("X")) {
						listOfNbCrystalPerClass.set(idCc, listOfNbCrystalPerClass.get(idCc) + 1);
					}
				}
			}
		}
		return listOfNbCrystalPerClass;
	}

	private ByteArrayOutputStream prepareScreeningExport(ActionMapping mapping, ActionForm actForm,
			HttpServletRequest request, HttpServletResponse response, boolean rtfFormat, boolean ranking,
			boolean rankAutoProc) throws Exception {

		List dataCollectionList = new ArrayList();
		String proposalCode = (String) request.getSession().getAttribute(Constants.PROPOSAL_CODE);
		String proposalNumber = String.valueOf(request.getSession().getAttribute(Constants.PROPOSAL_NUMBER));
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
		// Get data to populate the form
		BreadCrumbsForm bar = BreadCrumbsForm.getIt(request);
		// session
		Session3VO slv = new Session3VO();
		Integer sessionId = null;
		// Session
		if (bar.getSelectedSession() != null) {
			sessionId = bar.getSelectedSession().getSessionId();
			slv = sessionService.findByPk(sessionId, false, false, false);
			bar.setSelectedSession(slv);
		}
		// sample name
		String name = null;
		if (bar.getSelectedSample() != null) {
			name = bar.getSelectedSample().getName();
		}
		// Protein
		else if (bar.getSelectedProtein() != null) {
			name = bar.getSelectedProtein().getAcronym();
		}

		// Filename
		String filename = getFileName(bar, proposalCode, proposalNumber, sessionId, slv, name, true, ranking, false);
		// Get DataCollection list and remove non printable DataCollections
		dataCollectionList = (ArrayList) request.getSession().getAttribute(Constants.DATACOLLECTION_LIST);
		dataCollectionList = findByPrintable(dataCollectionList);
		dataCollectionList = ViewDataCollectionAction.setDataCollectionComments(dataCollectionList);
		
		// Get EnergyScan list
		List<EnergyScan3VO> energyScanList = (ArrayList) request.getSession().getAttribute(Constants.ENERGYSCAN_LIST);
		// Get XFE list
		List<XFEFluorescenceSpectrum3VO> xfeList = (ArrayList) request.getSession().getAttribute(Constants.XFE_LIST);
		// auto proc wrapper
		AutoProcShellWrapper wrapper = getAutoProcWrapper(actForm, dataCollectionList);

		// Get an object list.
		List<IspybCrystalClass3VO> listOfCrystalClass = (List<IspybCrystalClass3VO>) request.getSession().getAttribute(
				Constants.ISPYB_CRYSTAL_CLASS_LIST);
		// nb crystal per classes
		List<Integer> listOfNbCrystalPerClass = getListOfNbCrystalPerClass(listOfCrystalClass, proposalCode,
				dataCollectionList, energyScanList, xfeList);
		// sampleRankingVO
		List<SampleRankingVO> sampleRankingVOList = (ArrayList) request.getSession().getAttribute(
				Constants.SAMPLE_RANKING_LIST);
		// autoProcRankingVO
		List<AutoProcRankingVO> autoProcRankingVOList = (ArrayList) request.getSession().getAttribute(
				Constants.AUTOPROC_RANKING_LIST);
		ByteArrayOutputStream baos;
		PdfRtfExporter exporter = new PdfRtfExporter(name, proposalCode, proposalNumber, slv, dataCollectionList,
				energyScanList, xfeList, listOfCrystalClass, listOfNbCrystalPerClass, wrapper, sampleRankingVOList,
				autoProcRankingVOList, null, null);
		if (ranking) {
			baos = exporter.exportRankingAsPDF(rtfFormat, request);
		} else if (rankAutoProc) {
			baos = exporter.exportAutoProcRankingAsPDF(rtfFormat, request);
		} else {
			baos = exporter.exportScreeningAsPDF(rtfFormat, request);
		}

		// setting some response headers
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		// setting the content type
		String fileExtension = (rtfFormat) ? ".rtf" : ".pdf";
		String mimeType = (rtfFormat) ? "application/rtf" : "application/pdf";
		response.setContentType(mimeType);
		filename += fileExtension;
		response.setHeader("Content-Disposition", "attachment; filename=" + filename);
		// the contentlength is needed for MSIE!!!
		response.setContentLength(baos.size());
		// write ByteArrayOutputStream to the ServletOutputStream
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		out.flush();
		out.close();

		BreadCrumbsForm.setIt(request, bar);

		return baos;
	}

	/**
	 * exportAutoProcRankAsPdf
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward exportAutoProcRankAsPdf(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();
		try {
			ByteArrayOutputStream baos = prepareScreeningExport(mapping, actForm, request, response, false, false, true);
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
	 * exportAutoProcRankAsRtf
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward exportAutoProcRankAsRtf(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();
		try {
			ByteArrayOutputStream baos = prepareScreeningExport(mapping, actForm, request, response, true, false, true);
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
	 * export the MXPRessO/MXPressE report as PDF
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward exportMXPressOWorkflowAsPdf(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		try {
			ByteArrayOutputStream baos = prepareExportMXPressOWorkflow(mapping, actForm, request, response, false);

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
	 * export the MXPressO/MXPressE report as RTF
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward exportMXPressOWorkflowAsRtf(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		try {
			ByteArrayOutputStream baos = prepareExportMXPressOWorkflow(mapping, actForm, request, response, true);

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
	 * prepare data for MXPressO/MXpressE report
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @param rtfFormat
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private ByteArrayOutputStream prepareExportMXPressOWorkflow(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response, boolean rtfFormat) throws Exception {

		List<DataCollection3VO> dataCollectionList = new ArrayList<DataCollection3VO>();
		String proposalCode = (String) request.getSession().getAttribute(Constants.PROPOSAL_CODE);
		String proposalNumber = String.valueOf(request.getSession().getAttribute(Constants.PROPOSAL_NUMBER));
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
		// Get data to populate the form
		BreadCrumbsForm bar = BreadCrumbsForm.getIt(request);
		// session
		Session3VO slv = null;
		Integer sessionId = null;
		// Session
		if (bar.getSelectedSession() != null) {
			sessionId = bar.getSelectedSession().getSessionId();
			slv = sessionService.findByPk(sessionId, false, false, false);
			bar.setSelectedSession(slv);
		}
		// sample name
		String name = null;

		// Filename
		String filename = getFileName(bar, proposalCode, proposalNumber, sessionId, slv, name, false, false, true);
		// Get DataCollection list and remove non printable DataCollections
		dataCollectionList = (ArrayList<DataCollection3VO>) request.getSession().getAttribute(Constants.DATACOLLECTION_LIST);
		dataCollectionList = findByPrintable(dataCollectionList);
		dataCollectionList = ViewDataCollectionAction.setDataCollectionComments(dataCollectionList);
		
		// auto proc wrapper
		AutoProcShellWrapper wrapper = getAutoProcWrapper(actForm, dataCollectionList);
		// dataCollectionGroupList
		List<DataCollectionGroup3VO> dataCollectionGroupList = new ArrayList<DataCollectionGroup3VO>();
		dataCollectionGroupList = (ArrayList<DataCollectionGroup3VO>) request.getSession().getAttribute(Constants.DATACOLLECTIONGROUP_LIST);
		// sort dataCollectionGroupList by startTime
		List<DataCollectionGroup3VO> sortedList = new ArrayList<DataCollectionGroup3VO>();
		for (Iterator<DataCollectionGroup3VO> iterator = dataCollectionGroupList.iterator(); iterator.hasNext();) {
			DataCollectionGroup3VO dataCollectionGroup3VO = (DataCollectionGroup3VO) iterator.next();
			Date startTime = dataCollectionGroup3VO.getStartTime();
			int index = -1 ;
			int nb = sortedList.size();
			for (int i=0; i<nb; i++){
				Date st = sortedList.get(i).getStartTime();
				if (st!= null && st.after(startTime)){
					index = i;
					break;
				}
			}
			if (index == -1){
				sortedList.add(dataCollectionGroup3VO);
			}else{
				sortedList.add(index, dataCollectionGroup3VO);
			}
		}
		dataCollectionGroupList = sortedList;
		ByteArrayOutputStream baos;
		PdfRtfExporter exporter = new PdfRtfExporter(name, proposalCode, proposalNumber, slv, dataCollectionList,
				null, null, null, null, wrapper, null,
				null, dataCollectionGroupList, null);
		baos = exporter.exportMXPressOWorkflowReport(rtfFormat, request);

		// setting some response headers
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		// setting the content type
		String fileExtension = (rtfFormat) ? ".rtf" : ".pdf";
		String mimeType = (rtfFormat) ? "application/rtf" : "application/pdf";
		response.setContentType(mimeType);
		filename += fileExtension;
		response.setHeader("Content-Disposition", "attachment; filename=" + filename);
		// the contentlength is needed for MSIE!!!
		response.setContentLength(baos.size());
		// write ByteArrayOutputStream to the ServletOutputStream
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		out.flush();
		out.close();

		BreadCrumbsForm.setIt(request, bar);

		return baos;
	}
	
	/**
	 * export the new screening session report as PDF
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward exportSessionAsPdf(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		try {
			ByteArrayOutputStream baos = prepareSessionExport(mapping, actForm, request, response, false);

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
	 * export the new screening session report as PDF and send it by mail
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward exportSessionAndSendAsPdf(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		try {
			ByteArrayOutputStream baos = prepareSessionExport(mapping, actForm, request, response, false);
			return exportReportAndSendAsPdf(mapping, actForm, request, response, baos, "session_");

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
	 * export the new screening session report as RTF
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward exportSessionAsRtf(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		try {
			ByteArrayOutputStream baos = prepareSessionExport(mapping, actForm, request, response, true);

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
	 * prepare data for the new screening session report, based on sessionData object
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @param rtfFormat
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private ByteArrayOutputStream prepareSessionExport(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response, boolean rtfFormat) throws Exception {

		List dataCollectionList = new ArrayList();
		String proposalCode = (String) request.getSession().getAttribute(Constants.PROPOSAL_CODE);
		String proposalNumber = String.valueOf(request.getSession().getAttribute(Constants.PROPOSAL_NUMBER));
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
		// Get data to populate the form
		BreadCrumbsForm bar = BreadCrumbsForm.getIt(request);
		// session
		Session3VO slv = null;
		Integer sessionId = null;
		// Session
		if (bar.getSelectedSession() != null) {
			sessionId = bar.getSelectedSession().getSessionId();
			slv = sessionService.findByPk(sessionId, false, false, false);
			bar.setSelectedSession(slv);
		}
		// sample name
		String name = null;
		if (bar.getSelectedSample() != null) {
			name = bar.getSelectedSample().getName();
		}
		// Protein
		else if (bar.getSelectedProtein() != null) {
			name = bar.getSelectedProtein().getAcronym();
		}

		// Filename
		String filename = getFileName(bar, proposalCode, proposalNumber, sessionId, slv, name, false, false, false);
		// Get DataCollection list and remove non printable DataCollections
		dataCollectionList = (ArrayList) request.getSession().getAttribute(Constants.DATACOLLECTION_LIST);
		dataCollectionList = findByPrintable(dataCollectionList);
		dataCollectionList = ViewDataCollectionAction.setDataCollectionComments(dataCollectionList);
		
		// Get EnergyScan list
		List<EnergyScan3VO> energyScanList = (ArrayList) request.getSession().getAttribute(Constants.ENERGYSCAN_LIST);
		// Get XFE list
		List<XFEFluorescenceSpectrum3VO> xfeList = (ArrayList) request.getSession().getAttribute(Constants.XFE_LIST);
		// auto proc wrapper
		AutoProcShellWrapper wrapper = getAutoProcWrapper(actForm, dataCollectionList);

		// Get an object list.
		List<IspybCrystalClass3VO> listOfCrystalClass = (List<IspybCrystalClass3VO>) request.getSession().getAttribute(
				Constants.ISPYB_CRYSTAL_CLASS_LIST);
		// nb crystal per classes
		List<Integer> listOfNbCrystalPerClass = getListOfNbCrystalPerClass(listOfCrystalClass, proposalCode,
				dataCollectionList, energyScanList, xfeList);
		// sampleRankingVO
		List<SampleRankingVO> sampleRankingVOList = (ArrayList) request.getSession().getAttribute(
				Constants.SAMPLE_RANKING_LIST);
		// autoProcRankingVO
		List<AutoProcRankingVO> autoProcRankingVOList = (ArrayList) request.getSession().getAttribute(
				Constants.AUTOPROC_RANKING_LIST);
		
		// sessionDataObjectVO
		List<SessionDataObjectInformation> sessionDataObjectList = (ArrayList) request.getSession().getAttribute(
						Constants.SESSION_DATA_OBJECT_LIST);

		ByteArrayOutputStream baos;
		PdfRtfExporter exporter = new PdfRtfExporter(name, proposalCode, proposalNumber, slv, dataCollectionList,
				energyScanList, xfeList, listOfCrystalClass, listOfNbCrystalPerClass, wrapper, sampleRankingVOList,
				autoProcRankingVOList, null, sessionDataObjectList);
		baos = exporter.exportDetails(rtfFormat, request);

		// setting some response headers
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		// setting the content type
		String fileExtension = (rtfFormat) ? ".rtf" : ".pdf";
		String mimeType = (rtfFormat) ? "application/rtf" : "application/pdf";
		response.setContentType(mimeType);
		filename += fileExtension;
		response.setHeader("Content-Disposition", "attachment; filename=" + filename);
		// the contentlength is needed for MSIE!!!
		response.setContentLength(baos.size());
		// write ByteArrayOutputStream to the ServletOutputStream
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		out.flush();
		out.close();

		BreadCrumbsForm.setIt(request, bar);

		return baos;
	}
	
	/**
	 * save the images as jpg coming from the autoproc graph and to be included in the new screening session report
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void saveImageForExport(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		LOG.debug("saveImageForExport");
		String proposalCode = (String) request.getSession().getAttribute(Constants.PROPOSAL_CODE);
		String proposalNumber = String.valueOf(request.getSession().getAttribute(Constants.PROPOSAL_NUMBER));
		
		String sessionIdS = request.getParameter("sessionId");
		String listSvgS = request.getParameter("listSvg");
		String listIdS = request.getParameter("listId");
		
		Integer sessionId = null;
		try{
			sessionId = Integer.parseInt(sessionIdS);
		}catch(NumberFormatException e){
			
		}
		
		List<String> listSvg = new ArrayList<String>();
		List<Integer> listId = new ArrayList<Integer>();
		try{
			Gson gson = GSonUtils.getGson("dd-MM-yyyy");
			Type mapType = new TypeToken<ArrayList<String>>() {}.getType();
			listSvg = gson.fromJson(listSvgS, mapType);
			
			mapType = new TypeToken<ArrayList<Integer>>() {}.getType();
			listId = gson.fromJson(listIdS, mapType);
		}catch(Exception e){
			
		}
		if (sessionId != null && listSvg != null && listSvg.size() > 0 && listId != null && listId.size() > 0){
			LOG.debug("saveImageForExport with sessionId = "+sessionId +" & listSvg size = "+listSvg.size());
			int id= 0;
			for (Iterator<String> iterator = listSvg.iterator(); iterator.hasNext();) {
				//read the input svg doc into Transcoder Input
				String svg = (String) iterator.next();
				Integer autoProcProgramAttachmentId = listId.get(id);
				id++;
				String fileName = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + proposalCode
						+ proposalNumber + "_chart_" + autoProcProgramAttachmentId + ".png";
				String jpgPath = request.getRealPath(fileName);
				
				File chartFile = new File(jpgPath);
				svg = svg.replaceAll("data:image/png;base64,","");
				 // remove data:image/png;base64, and then take rest sting
				byte[] decodedBytes = DatatypeConverter.parseBase64Binary(svg );
				FileOutputStream fs = new FileOutputStream(chartFile);
				BufferedOutputStream  bs = new BufferedOutputStream(fs);
			    bs.write(decodedBytes);
			    bs.flush();
			    bs.close();
			    bs = null;
	        
			}
		}
	}
	
	
	
	/**
	 * prepare data for the new general report 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @param rtfFormat
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private ByteArrayOutputStream prepareIndustrialReport(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response, boolean rtfFormat) throws Exception {

		List dataCollectionList = new ArrayList();
		String proposalCode = (String) request.getSession().getAttribute(Constants.PROPOSAL_CODE);
		String proposalNumber = String.valueOf(request.getSession().getAttribute(Constants.PROPOSAL_NUMBER));
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
		// Get data to populate the form
		BreadCrumbsForm bar = BreadCrumbsForm.getIt(request);
		// session
		Session3VO slv = null;
		Integer sessionId = null;
		// Session
		if (bar.getSelectedSession() != null) {
			sessionId = bar.getSelectedSession().getSessionId();
			slv = sessionService.findByPk(sessionId, false, false, false);
			bar.setSelectedSession(slv);
		}
		// sample name
		String name = null;
		if (bar.getSelectedSample() != null) {
			name = bar.getSelectedSample().getName();
		}
		// Protein
		else if (bar.getSelectedProtein() != null) {
			name = bar.getSelectedProtein().getAcronym();
		}

		// Filename
		String filename = getFileName(bar, proposalCode, proposalNumber, sessionId, slv, name, false, false, false);
		// Get DataCollection list and remove non printable DataCollections and format comments
		dataCollectionList = (ArrayList) request.getSession().getAttribute(Constants.DATACOLLECTION_LIST);
		dataCollectionList = findByPrintable(dataCollectionList);
		dataCollectionList = ViewDataCollectionAction.setDataCollectionComments(dataCollectionList);
		
		// Get EnergyScan list
		List<EnergyScan3VO> energyScanList = (ArrayList) request.getSession().getAttribute(Constants.ENERGYSCAN_LIST);
		// Get XFE list
		List<XFEFluorescenceSpectrum3VO> xfeList = (ArrayList) request.getSession().getAttribute(Constants.XFE_LIST);
		// auto proc wrapper
		AutoProcShellWrapper wrapper = getAutoProcWrapper(actForm, dataCollectionList);

		// Get an object list.
		List<IspybCrystalClass3VO> listOfCrystalClass = (List<IspybCrystalClass3VO>) request.getSession().getAttribute(
				Constants.ISPYB_CRYSTAL_CLASS_LIST);
		// nb crystal per classes
		List<Integer> listOfNbCrystalPerClass = getListOfNbCrystalPerClass(listOfCrystalClass, proposalCode,
				dataCollectionList, energyScanList, xfeList);
		// sampleRankingVO
		List<SampleRankingVO> sampleRankingVOList = (ArrayList) request.getSession().getAttribute(
				Constants.SAMPLE_RANKING_LIST);
		// autoProcRankingVO
		List<AutoProcRankingVO> autoProcRankingVOList = (ArrayList) request.getSession().getAttribute(
				Constants.AUTOPROC_RANKING_LIST);

		// sessionDataObjectVO
		List<SessionDataObjectInformation> sessionDataObjectList = (ArrayList) request.getSession().getAttribute(
						Constants.SESSION_DATA_OBJECT_LIST);
				
		ByteArrayOutputStream baos;
		PdfRtfExporter exporter = new PdfRtfExporter(name, proposalCode, proposalNumber, slv, dataCollectionList,
				energyScanList, xfeList, listOfCrystalClass, listOfNbCrystalPerClass, wrapper, sampleRankingVOList,
				autoProcRankingVOList, null, sessionDataObjectList);
		baos = exporter.exportNewDataCollectionReport(rtfFormat);

		// setting some response headers
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		// setting the content type
		String fileExtension = (rtfFormat) ? ".rtf" : ".pdf";
		String mimeType = (rtfFormat) ? "application/rtf" : "application/pdf";
		response.setContentType(mimeType);
		filename += fileExtension;
		response.setHeader("Content-Disposition", "attachment; filename=" + filename);
		// the contentlength is needed for MSIE!!!
		response.setContentLength(baos.size());
		// write ByteArrayOutputStream to the ServletOutputStream
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		out.flush();
		out.close();

		BreadCrumbsForm.setIt(request, bar);

		return baos;
	}

	
	/**
	 * create the new general report as PDF and send it by email
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward exportIndustrialReportAndSendAsPdf(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();
		try {
			ByteArrayOutputStream baos = prepareIndustrialReport(mapping, actForm, request, response, false);
			return exportReportAndSendAsPdf(mapping, actForm, request, response, baos, "");
		
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
	 * create the new general report as PDF
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward exportIndustrialReportAsPdf(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		try {
			ByteArrayOutputStream baos = prepareIndustrialReport(mapping, actForm, request, response, false);

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
	 * create the new general report as RTF
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward exportIndustrialReportAsRtf(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		try {
			ByteArrayOutputStream baos = prepareIndustrialReport(mapping, actForm, request, response, true);

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
}
