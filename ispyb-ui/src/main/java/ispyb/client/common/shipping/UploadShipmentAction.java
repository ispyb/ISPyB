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
 * UploadShipmentAction.java
 * 
 * @author ludovic.launer@esrf.fr
 * May 3, 2005
 * @updated 29/10/2009 - PBU - Code formatting
 * @updated 20/06/2011 - SD - Code formatting + renaming
 */

package ispyb.client.common.shipping;

import java.io.File;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.util.FileUtil;
import ispyb.common.util.Constants;
import ispyb.common.util.DBTools;
import ispyb.common.util.upload.ISPyBParser;
import ispyb.common.util.upload.ShippingInformation;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.services.sample.Protein3Service;
import ispyb.server.mx.vos.sample.Protein3VO;

/**
 * @struts.action name="uploadForm" path="/user/uploadShipment" type="ispyb.client.common.shipping.UploadShipmentAction"
 *                validate="false" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="uploadShipmentPage" path="user.shipping.uploadShipment.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 * @struts.action-forward name="shippingViewPage" path=
 *                        "/menuSelected.do?leftMenuId=7&amp;topMenuId=5&amp;targetUrl=%2Fuser%2FviewShippingAction.do%3FreqCode%3Ddisplay"
 * 
 * @struts.action-forward name="uploadFilePage" path="user.shipping.upload.page"
 */

// public class UploadShipmentAction extends AbstractSampleAction {
public class UploadShipmentAction extends DispatchAction {
	private final static Logger LOG = Logger.getLogger(UploadShipmentAction.class);

	private String mFileType = Constants.TEMPLATE_FILE_TYPE_EXPORT_SHIPPING;

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private void initServices() throws CreateException, NamingException {
		
	}

	public static void main(String[] args) {
		try {
			UploadShipmentAction s = new UploadShipmentAction();
			// s.uploadFile(null, null, null, null);
			// s.PopulateTemplate(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * execute
	 * 
	 * @param args
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_response) {

		try {
			this.initServices();
		} catch (CreateException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}

		// Retrieve Attributes
		String reqCode = request.getParameter("reqCode");

		if (reqCode.equals("display"))
			return this.display(mapping, actForm, request, in_response);
		if (reqCode.equals("displayAfterDewarTracking"))
			return this.displayAfterDewarTracking(mapping, actForm, request, in_response);
		// if (reqCode.equals("uploadFile"))
		// return this.uploadFile(mapping, actForm, request, in_response);
		if (reqCode.equals("downloadFile"))
			return this.DownloadFile(mapping, actForm, request, in_response);
		if (reqCode.equals("exportShipping"))
			return this.exportShipping(mapping, actForm, request, in_response);

		return this.display(mapping, actForm, request, in_response);
	}

	/**
	 * displayAfterDewarTracking
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward displayAfterDewarTracking(ActionMapping mapping, ActionForm actForm,
			HttpServletRequest request, HttpServletResponse in_reponse) {

		// Retrieve Attributes
		UploadForm form = (UploadForm) actForm;
		// Populate Template for Shipment
		Integer shippingId = Integer.decode(request.getParameter(Constants.SHIPPING_ID));

		// DLS ####
		if (!Constants.SITE_IS_DLS()) {
			String populatedTemplatePath = PopulateTemplate(request, false, false, false, null, null, false, 0, true,
					shippingId);
			form.setPopulatedForShipmentTemplageURL(populatedTemplatePath);
		}

		form.setCurrentShippingId(shippingId.intValue());
		try {
			Shipping3VO selectedShipping = DBTools.getSelectedShipping(shippingId);
			BreadCrumbsForm.getIt(request).setSelectedShipping(selectedShipping);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return this.display(mapping, form, request, in_reponse);
	}

	/**
	 * display
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {

		// Show BreadCrumbsForm if shipping selected
		try {
			String shippingId = request.getParameter(Constants.SHIPPING_ID);
			if (shippingId != null && !shippingId.equals("")) {
				Shipping3VO selectedShipping = DBTools.getSelectedShipping(Integer.decode(shippingId));
				BreadCrumbsForm.getIt(request).setSelectedShipping(selectedShipping);
			} else
				BreadCrumbsForm.getIt(request).setSelectedShipping(null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Clean BreadCrumbsForm
		if (BreadCrumbsForm.getIt(request).getSelectedShipping() == null)
			BreadCrumbsForm.setIt(request, BreadCrumbsForm.getItClean(request));
		else
			BreadCrumbsForm.getIt(request).setSelectedDewar(null);

		// Retrieve Attributes
		UploadForm form = (UploadForm) actForm; // Parameters submited by form
		String selectedBeamlineName = form.getSelectedBeamline();

		// Populate Template
		String populatedTemplatePath = PopulateTemplate(request, false, false, false, null, null, false, 0, false, 0);
		if (selectedBeamlineName != null) {
			String populatedTemplateAdvancedPath = PopulateTemplate(request, false, false, true, selectedBeamlineName,
					null, false, 0, false, 0);
		}
		form.setPopulatedTemplateURL(populatedTemplatePath);

		// Retrieve the list of beamlines for the current proposal having datamatrix codes scanned by SC
		try {
			// Get Proposal
			String proposalCode = (String) request.getSession().getAttribute(Constants.PROPOSAL_CODE);
			String proposalNumber = String.valueOf(request.getSession().getAttribute(Constants.PROPOSAL_NUMBER));
			Proposal3VO proposal = DBTools.getProposal(proposalCode, proposalNumber);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mapping.findForward("uploadShipmentPage");
	}

	/**
	 * PopulateTemplate
	 * 
	 * @param request
	 * @param getTemplateFullPathOnly
	 * @param getTemplateFilenameOnly
	 * @param populateDMCodes
	 * @param selectedBeamlineName
	 * @param hashDMCodesForBeamline
	 * @param populateForExport
	 * @param nbContainersToExport
	 * @param populateForShipment
	 * @param shippingId
	 * @return
	 */
	public static String PopulateTemplate(HttpServletRequest request, boolean getTemplateFullPathOnly,
			boolean getTemplateFilenameOnly, boolean populateDMCodes, String selectedBeamlineName,
			List hashDMCodesForBeamline, boolean populateForExport, int nbContainersToExport,
			boolean populateForShipment, int shippingId) {

		String populatedTemplatePath = "";
		try {
			String xlsPath;
			String proposalCode;
			String proposalNumber;
			String populatedTemplateFileName;

			String today = ".xls";
			if (request != null) {
				xlsPath = Constants.TEMPLATE_POPULATED_RELATIVE_PATH;
				if (populateForShipment)
					xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH
							+ Constants.TEMPLATE_XLS_POPULATED_FROM_SHIPMENT;
				else if (populateForExport) {
					switch (nbContainersToExport) {
					case 1:
						xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH
								+ Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME1;
						break;
					case 2:
						xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH
								+ Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME2;
						break;
					case 3:
						xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH
								+ Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME3;
						break;
					case 4:
						xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH
								+ Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME4;
						break;
					case 5:
						xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH
								+ Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME5;
						break;
					case 6:
						xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH
								+ Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME6;
						break;
					case 7:
						xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH
								+ Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME7;
						break;
					case 8:
						xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH
								+ Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME8;
						break;
					case 9:
						xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH
								+ Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME9;
						break;
					case 10:
						xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH
								+ Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME10;
						break;
					}
				}

				proposalCode = (String) request.getSession().getAttribute(Constants.PROPOSAL_CODE);
				proposalNumber = String.valueOf(request.getSession().getAttribute(Constants.PROPOSAL_NUMBER));

				if (populateForShipment)
					populatedTemplateFileName = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + proposalCode
							+ proposalNumber + "_shipment_" + shippingId + today;
				else
					populatedTemplateFileName = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + proposalCode
							+ proposalNumber + ((populateDMCodes) ? "_#" : "") + today;

				populatedTemplatePath = request.getContextPath() + populatedTemplateFileName;

				if (getTemplateFilenameOnly && populateForShipment)
					return proposalCode + proposalNumber + "_shipment_" + shippingId + today;
				if (getTemplateFilenameOnly && !populateForShipment)
					return proposalCode + proposalNumber + ((populateDMCodes) ? "_#" : "") + today;

				xlsPath = request.getRealPath(xlsPath);
				populatedTemplateFileName = request.getRealPath(populatedTemplateFileName);
			} else {
				xlsPath = "C:/" + Constants.TEMPLATE_POPULATED_RELATIVE_PATH;
				proposalCode = "ehtpx";
				proposalNumber = "1";
				populatedTemplateFileName = "C:/" + Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + proposalCode
						+ proposalNumber + today;
			}
			if (getTemplateFullPathOnly)
				return populatedTemplateFileName;

			// Retrieve DM codes from DB
			String beamlineName = selectedBeamlineName;

			String[][] dmCodesinSC = null;

			File originalTemplate = new File(xlsPath);
			File populatedTemplate = new File(populatedTemplateFileName);
			FileUtils.copyFile(originalTemplate, populatedTemplate);
			ISPyBParser parser = new ISPyBParser();

			// Copy template to tmp folder
			File xlsTemplate = new File(xlsPath);
			File xlsPopulatedTemplate = new File(populatedTemplateFileName);
			FileUtils.copyFile(xlsTemplate, xlsPopulatedTemplate);

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Proposal3Service proposal3Service = (Proposal3Service) ejb3ServiceLocator
					.getLocalService(Proposal3Service.class);
			// Get the list of Proteins
			// old List proposals = (List) _proposal.findByCodeAndNumber(proposalCode, new Integer(proposalNumber));
			List<Proposal3VO> proposals = proposal3Service.findByCodeAndNumber(proposalCode, proposalNumber, false,
					false, false);

			// ProposalLightValue proposalLight = (ProposalLightValue) proposals.get(0);
			Protein3Service protein3Service = (Protein3Service) ejb3ServiceLocator
					.getLocalService(Protein3Service.class);
			// List listProteins = (List) protein3Service..findByProposalId(proposalLight.getPrimaryKey());
			List<Protein3VO> listProteins = protein3Service.findByProposalId(proposals.get(0).getProposalId(), true,
					true);
			LOG.info("xlsPath = "+xlsPath + "populatedTemplateFileName = " + populatedTemplateFileName);
			parser.populate(xlsPath, populatedTemplateFileName, listProteins, dmCodesinSC);

			if (populateForShipment)
				parser.populateExistingShipment(populatedTemplateFileName, populatedTemplateFileName, shippingId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return populatedTemplatePath;
	}

	/**
	 * exportShipping
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward exportShipping(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		try {
			ISPyBParser parser = new ISPyBParser();
			// Clean BreadCrumbsForm
			BreadCrumbsForm.setIt(request, BreadCrumbsForm.getItClean(request));
			// Retrieve Attributes
			String shippingId = request.getParameter(Constants.SHIPPING_ID);
			// Prepare Shipment info
			ShippingInformation shippingInformation = DBTools.getShippingInformation(new Integer(shippingId));
			int nbSheetsInInfo = DBTools.GetNumberOfContainers(shippingInformation) - 1;
			// Populate Template
			String populatedTemplatePath = PopulateTemplate(request, false, false, false, null, null, true,
					nbSheetsInInfo, false, 0);
			populatedTemplatePath = PopulateTemplate(request, true, false, false, null, null, true, nbSheetsInInfo,
					false, 0);
			// Prepare list with shipment info
			String shippingName = shippingInformation.getShipping().getShippingName();
			if (shippingName.indexOf(".xls") == -1)
				shippingName += ".xls";
			String fileName = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + shippingName;
			// Export
			parser.export(fileName, populatedTemplatePath, shippingInformation);
			// Download the File
			this.mFileType = Constants.TEMPLATE_FILE_TYPE_EXPORT_SHIPPING;
			this.DownloadFile(mapping, actForm, request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
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
	public ActionForward DownloadFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		String fileType = request.getParameter(Constants.TEMPLATE_FILE_TYPE);

		if (fileType == null)
			fileType = this.mFileType;
		try {
			String targetUpload = new String("");
			String attachmentFilename = new String("");
			if (fileType.equals(Constants.TEMPLATE_FILE_TYPE_TEMPLATE)) {
				targetUpload = Constants.TEMPLATE_RELATIVE_PATH;
				targetUpload = request.getRealPath(targetUpload);
				attachmentFilename = Constants.TEMPLATE_XLS_FILENAME;
			}
			if (fileType.equals(Constants.TEMPLATE_FILE_TYPE_POPULATED_TEMPLATE)) {
				targetUpload = PopulateTemplate(request, true, false, false, null, null, false, 0, false, 0);
				attachmentFilename = PopulateTemplate(request, false, true, false, null, null, false, 0, false, 0);
			}
			if (fileType.equals(Constants.TEMPLATE_FILE_TYPE_POPULATED_TEMPLATE_ADVANCED)) {
				targetUpload = PopulateTemplate(request, true, false, true, null, null, false, 0, false, 0);
				attachmentFilename = PopulateTemplate(request, false, true, true, null, null, false, 0, false, 0);
			}
			if (fileType.equals(Constants.TEMPLATE_FILE_TYPE_EXPORT_SHIPPING)) {
				targetUpload = PopulateTemplate(request, true, false, false, null, null, false, 0, false, 0);
				attachmentFilename = PopulateTemplate(request, false, true, false, null, null, false, 0, false, 0);
			}
			if (fileType.equals(Constants.TEMPLATE_FILE_TYPE_POPULATED_TEMPLATE_FROM_SHIPMENT)) {
				Integer _shippingId = Integer.decode(request.getParameter(Constants.SHIPPING_ID));
				int shippingId = (_shippingId != null) ? _shippingId.intValue() : 0;
				targetUpload = PopulateTemplate(request, true, false, false, null, null, false, 0, true, shippingId);
				attachmentFilename = PopulateTemplate(request, false, true, false, null, null, false, 0, true,
						shippingId);
			}

			try {

				byte[] imageBytes = FileUtil.readBytes(targetUpload);
				response.setContentLength(imageBytes.length);
				ServletOutputStream out = response.getOutputStream();
				response.setHeader("Pragma", "public");
				response.setHeader("Cache-Control", "max-age=0");
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition", "attachment; filename=" + attachmentFilename);

				out.write(imageBytes);
				out.flush();
				out.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}

		return null;
	}

}
