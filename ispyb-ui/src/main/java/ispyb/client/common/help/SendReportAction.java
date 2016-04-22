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
 * SendReportAction.java
 * @author ludovic.launer@esrf.fr
 * Jan 9, 2006
 */

package ispyb.client.common.help;

import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.util.FileUtil;
import ispyb.common.util.Constants;
import ispyb.common.util.PropertyLoader;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.vos.collections.Session3VO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

/**
 * @struts.action name="sendReportForm" path="/user/SendReportAction" type="ispyb.client.common.help.SendReportAction"
 *                validate="false" parameter="reqCode" scope="request"
 * @struts.action-forward name="ReportPage" path="guest.help.feedback.report.page"
 * @struts.action-forward name="HelpPage" path="guest.help.main.page"
 * @struts.action-forward name="error" path="site.default.error.page"
 */

public class SendReportAction extends org.apache.struts.actions.DispatchAction {

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private static final  Logger LOG = Logger.getLogger(SendReportAction.class);

	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();
		// ---------------------------------------------------------------------------------------------------
		// Retrieve Attributes
		Integer sessionId = new Integer(request.getParameter(Constants.SESSION_ID)); // Request parameters

		try {
			SendReportForm form = (SendReportForm) actForm;
			// --- Populate BreadCrumbs ---
			Session3Service sessionService = (Session3Service) ejb3ServiceLocator
					.getLocalService(Session3Service.class);

			Session3VO selectedSession = sessionService.findByPk(sessionId, false, false, false);
			BreadCrumbsForm.getIt(request).setSelectedSession(selectedSession);

			// --- Retrieve existing file ---
			Proposal3VO proposal = selectedSession.getProposalVO();
			String proposalName = proposal.getCode() + proposal.getNumber();
			String proposalFileName = GetReportName(proposalName, sessionId);

			File reportPath = new File(Constants.UPLOAD_PATH + File.separator);

			File filesOnDisk[] = reportPath.listFiles(new ReportNameFilter(proposalFileName));
			if (filesOnDisk != null){
				for (int f = 0; f < filesOnDisk.length; f++) {
					File reportOnDisk = filesOnDisk[f];
					String reportURL = reportOnDisk.getName();
					form.setFileFullPath(reportURL);
				}
			}

			// Hack: for testing #########
			// SendReminders();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Save Originator for return path
		BreadCrumbsForm.getIt(request).setFromPage(request.getRequestURL() + "?" + request.getQueryString());

		return mapping.findForward("ReportPage");
	}

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward GetPDF(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		// ---------------------------------------------------------------------------------------------------
		// Retrieve Attributes
		// Request parameters
		String fileName = request.getParameter("fileFullPath"); 

		try {
			String fileFullPath = GetReportPath() + fileName;
			byte[] imageBytes = FileUtil.readBytes(fileFullPath);
			response.setContentLength(imageBytes.length);
			ServletOutputStream out = response.getOutputStream();
			response.setContentType("application/pdf");

			out.write(imageBytes);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapping.findForward("ReportPage");
	}

	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward sendReport(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		ActionMessage resultMessage;

		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			SendReportForm form = (SendReportForm) actForm; // Parameters submited by form

			Integer mProposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID); // Session
																										// parameters
			Session3VO session = BreadCrumbsForm.getIt(request).getSelectedSession();
			Integer sessionId = null;
			if (session != null)
				sessionId = session.getSessionId();

			// ------------ Retrieve info from DB ----------------------------------------------------------------
			Proposal3Service proposalService = (Proposal3Service) ejb3ServiceLocator
					.getLocalService(Proposal3Service.class);
			Proposal3VO proposal = proposalService.findByPk(mProposalId);
			// ---------------------------------

			FormFile uploadedFile = form.getUploadedFile();
			String contentType = uploadedFile.getContentType();
			String fileName = uploadedFile.getFileName();
			int fileSize = uploadedFile.getFileSize();
			byte[] fileData = uploadedFile.getFileData();

			// --- Check File is there ---
			if (uploadedFile.getFileName().equals("") || uploadedFile.getFileSize() == 0) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.selectFile"));
				saveErrors(request, errors);
				return mapping.findForward("ReportPage");
			}
			// ----------------------------
			String fileExtension = fileName.substring(fileName.lastIndexOf("."));
			String proposalName = proposal.getCode() + proposal.getNumber();
			String reportFullPath = GetReportFileName(proposalName, sessionId, fileExtension);

			if (fileExtension.toLowerCase().compareTo(".pdf") != 0) {
				// resultMessage = new ActionMessage("message.pdf", GetReportName(proposalName, sessionId) +
				// fileExtension);
				errors.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("message.pdf", GetReportName(proposalName, sessionId) + fileExtension));
				saveErrors(request, errors);
				return mapping.findForward("ReportPage");
			} else {
				FileOutputStream fileOut = new FileOutputStream(reportFullPath);
				fileOut.write(fileData);
				fileOut.close();
				resultMessage = new ActionMessage("message.inserted", GetReportName(proposalName, sessionId)
						+ fileExtension);
				messages.add(ActionMessages.GLOBAL_MESSAGE, resultMessage);
			}

			// --- Acknowledge action ---
			saveMessages(request, messages);

			// ----- Return to Originator -----
			String originator = BreadCrumbsForm.getIt(request).getFromPage();
			if (originator != null) {
				BreadCrumbsForm.getIt(request).setFromPage(null);
				response.sendRedirect(originator);
			}
			// ---------------------------------

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}

		return mapping.findForward("ReportPage");
	}

	/**
	 * IsAllowedToSubmitReport
	 * 
	 * @return true if the proposal is Allowed to submit a report
	 */
	public static boolean IsAllowedToSubmitReport(String proposalCode) {
		if (proposalCode == null){
			return false;
		}
		boolean isAllowedToSubmitReport = false;
		String[] allowedProposalCodes = {"bm14u", "bm14"};

		for (int p = 0; p < allowedProposalCodes.length; p++) {
			if (proposalCode.equalsIgnoreCase(allowedProposalCodes[p]))
				isAllowedToSubmitReport = true;
		}

		return isAllowedToSubmitReport;
	}

	private static String GetReportFileName(String proposalName, Integer sessionId, String fileExtension) {
		String reportFileName = GetReportPath() + GetReportName(proposalName, sessionId) + fileExtension;
		return reportFileName;
	}

	private static String GetReportName(String proposalName, Integer sessionId) {
		String reportName = new String();

		reportName = proposalName + "_" + sessionId;
		return reportName;
	}

	private static String GetReportPath() {

		String fullPath = new String();
		File reportPath = new File(Constants.UPLOAD_PATH + File.separator);
		reportPath.mkdirs();
		try {
			fullPath = reportPath.getCanonicalPath();
		} catch (Exception e) {
		}
		return fullPath + File.separator;
	}

	class ReportNameFilter implements FilenameFilter {
		String mReportFileName;

		ReportNameFilter(String reportFileName) {
			this.mReportFileName = reportFileName;
		}

		public boolean accept(File file, String name) {
			if (name.toLowerCase().indexOf(mReportFileName.toLowerCase()) != -1)
				return true;
			return false;
		}
	}
}
