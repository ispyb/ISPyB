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
 * SubmitPocketSampleInformationAction.java
 * 
 * @author ludovic.launer@esrf.fr
 * May 3, 2005
 * @updated 29/10/2009 - PBU - Code formatting
 */

package ispyb.client.common.shipping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

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

import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.util.FileUtil;
import ispyb.client.mx.sample.AbstractSampleAction;
import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.common.util.DBTools;
import ispyb.common.util.upload.ISPyBParser;
import ispyb.common.util.upload.ShippingInformation;
import ispyb.common.util.upload.UploadShipmentUtils;
import ispyb.common.util.upload.XlsUploadException;
import ispyb.server.common.services.shipping.Shipping3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;

/**
 * @struts.action name="uploadForm" path="/user/submitPocketSampleInformationAction"
 *                type="ispyb.client.common.shipping.SubmitPocketSampleInformationAction" validate="false" parameter="reqCode"
 *                scope="request"
 * 
 * @struts.action-forward name="submitPocketSampleInformationPage"
 *                        path="user.shipping.submitPocketSampleInformation.page"
 * @struts.action-forward name="storeSubmitPocketSampleInformationPage"
 *                        path="store.shipping.submitPocketSampleInformation.page"
 * @struts.action-forward name="blomSubmitPocketSampleInformationPage"
 *                        path="blom.shipping.submitPocketSampleInformation.page"
 * @struts.action-forward name="localcontactSubmitPocketSampleInformationPage"
 *                        path="localcontact.shipping.submitPocketSampleInformation.page"
 * @struts.action-forward name="managerSubmitPocketSampleInformationPage"
 *                        path="manager.shipping.submitPocketSampleInformation.page"
 * @struts.action-forward name="fedexmanagerSubmitPocketSampleInformationPage"
 *                        path="fedexmanager.shipping.submitPocketSampleInformation.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 * @struts.action-forward name="shippingViewPage" path=
 *                        "/menuSelected.do?leftMenuId=7&amp;topMenuId=5&amp;targetUrl=%2Fuser%2FviewShippingAction.do%3FreqCode%3Ddisplay"
 * 
 * @struts.action-forward name="uploadFilePage" path="user.shipping.upload.page"
 */

public class SubmitPocketSampleInformationAction extends AbstractSampleAction {
	private final Logger LOG = Logger.getLogger(CreateDewarAction.class);

	private String mFileType = Constants.TEMPLATE_FILE_TYPE_EXPORT_SHIPPING;

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Shipping3Service shippingService;

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() {
		try {
			this.shippingService = (Shipping3Service) ejb3ServiceLocator.getLocalService(Shipping3Service.class);

		} catch (Exception e) {
			LOG.error(e);
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

		this.initServices();
		// Retrieve Attributes
		String reqCode = request.getParameter("reqCode");

		if (reqCode != null) {
			if (reqCode.equals("display"))
				return this.display(mapping, actForm, request, in_response);
			if (reqCode.equals("displayAfterDewarTracking"))
				return this.displayAfterDewarTracking(mapping, actForm, request, in_response);
			if (reqCode.equals("uploadFile"))
				return this.uploadFile(mapping, actForm, request, in_response);
			if (reqCode.equals("downloadFile"))
				return this.DownloadFile(mapping, actForm, request, in_response);
			if (reqCode.equals("exportShipping"))
				return this.exportShipping(mapping, actForm, request, in_response);
		}
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
			String populatedTemplatePath = UploadShipmentUtils.PopulateTemplate(request, false, false, false, null,
					null, false, 0, true, shippingId);
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
		String populatedTemplatePath = UploadShipmentUtils.PopulateTemplate(request, false, false, false, null, null,
				false, 0, false, 0);
		if (selectedBeamlineName != null) {
			String populatedTemplateAdvancedPath = UploadShipmentUtils.PopulateTemplate(request, false, false, true,
					selectedBeamlineName, null, false, 0, false, 0);
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

		return redirectPageFromRole(mapping, request);
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
			String populatedTemplatePath = UploadShipmentUtils.PopulateTemplate(request, false, false, false, null,
					null, true, nbSheetsInInfo, false, 0);
			populatedTemplatePath = UploadShipmentUtils.PopulateTemplate(request, true, false, false, null, null, true,
					nbSheetsInInfo, false, 0);
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
	 * uploadFile
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_response
	 * @return
	 */
	public ActionForward uploadFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_response) {

		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		UploadForm form = (UploadForm) actForm;
		Integer shippingId = null;

		try {
			ISPyBParser parser = new ISPyBParser();
			String proposalCode;
			String proposalNumber;
			String proposalName;
			String uploadedFileName;
			String realXLSPath;
			boolean errorFile = false;

			if (request != null) {

				shippingId = BreadCrumbsForm.getIt(request).getSelectedShipping().getShippingId();

				// Values
				proposalCode = (String) request.getSession().getAttribute(Constants.PROPOSAL_CODE);
				proposalNumber = String.valueOf(request.getSession().getAttribute(Constants.PROPOSAL_NUMBER));
				proposalName = proposalCode + proposalNumber.toString();
				uploadedFileName = form.getRequestFile().getFileName();
				String fileName = proposalName + "_" + uploadedFileName;
				if (uploadedFileName.equals("")) {
					parser.getValidationErrors().add(new XlsUploadException("You have to specify a file."));
					errorFile = true;
				}
				if (!uploadedFileName.endsWith(".xls")) {
					parser.getValidationErrors().add(new XlsUploadException("The file must be a xls file."));
					errorFile = true;
				}
				realXLSPath = request.getRealPath("/") + "/tmp/" + fileName;
				// Write the received file to tmp directory
				FormFile f = form.getRequestFile();
				InputStream in = f.getInputStream();
				File outputFile = new File(realXLSPath);
				if (outputFile.exists())
					outputFile.delete();
				FileOutputStream out = new FileOutputStream(outputFile);
				while (in.available() != 0) {
					out.write(in.read());
					out.flush();
				}
				out.flush();
				out.close();

				if (!errorFile) {
					// received file
					FileInputStream inFile = new FileInputStream(realXLSPath);
					// delete All Samples or Update
					boolean deleteAllShipment = !(form.isDeleteAllShipment());
					LOG.debug("uploadFile for shippingId " + shippingId + ", deleteSample=" + deleteAllShipment);
					// Existing Shipment (DewarTracking) : Delete Samples
					if (shippingId != null) {
						if (deleteAllShipment) {
							LOG.debug(" ---[uploadFile] Upload for Existing Shipment (DewarTRacking): Deleting Samples from Shipment :");
							int nbSamplesContainers = shippingService
									.deleteAllSamplesAndContainersForShipping(shippingId);
							// put only key, not the message elsewhere bean message is null
							if (nbSamplesContainers > 0)
								messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
										"message.upload.shipping.delete.old"));
							else
								messages.add(ActionMessages.GLOBAL_MESSAGE,
										new ActionMessage("message.upload.shipping"));
						}
					}

					// Integrate xls file
					List<String> allowedSpaceGroups = (List<String>) request.getSession().getAttribute(
							Constants.ISPYB_ALLOWED_SPACEGROUPS_LIST);
					String[] msg = UploadShipmentUtils.importFromXls(inFile, shippingId, deleteAllShipment,
							allowedSpaceGroups);
					String msgError = msg[0];
					String msgWarning = msg[1];
					if (msgError != null && msgError.length() > 0) {
						LOG.error(msgError);
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.upload", msgError));
						saveErrors(request, errors);
					}
					if (msgWarning != null && msgWarning.length() > 0) {
						messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free", msgWarning));
						saveMessages(request, messages);
					}
				}

			}

		} catch (Exception e) {
			// Set up errors
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			e.printStackTrace();
			saveErrors(request, errors);
			return mapping.findForward("error");
		}

		if (!messages.isEmpty())
			saveMessages(request, messages);
		// return mapping.findForward(returnPage);
		return redirectPageFromRole(mapping, request);
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
		ActionMessages messages = new ActionMessages();
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
				targetUpload = UploadShipmentUtils.PopulateTemplate(request, true, false, false, null, null, false, 0,
						false, 0);
				attachmentFilename = UploadShipmentUtils.PopulateTemplate(request, false, true, false, null, null,
						false, 0, false, 0);
			}
			if (fileType.equals(Constants.TEMPLATE_FILE_TYPE_POPULATED_TEMPLATE_ADVANCED)) {
				targetUpload = UploadShipmentUtils.PopulateTemplate(request, true, false, true, null, null, false, 0,
						false, 0);
				attachmentFilename = UploadShipmentUtils.PopulateTemplate(request, false, true, true, null, null,
						false, 0, false, 0);
			}
			if (fileType.equals(Constants.TEMPLATE_FILE_TYPE_EXPORT_SHIPPING)) {
				targetUpload = UploadShipmentUtils.PopulateTemplate(request, true, false, false, null, null, false, 0,
						false, 0);
				attachmentFilename = UploadShipmentUtils.PopulateTemplate(request, false, true, false, null, null,
						false, 0, false, 0);
			}
			if (fileType.equals(Constants.TEMPLATE_FILE_TYPE_POPULATED_TEMPLATE_FROM_SHIPMENT)) {
				Integer _shippingId = Integer.decode(request.getParameter(Constants.SHIPPING_ID));
				int shippingId = (_shippingId != null) ? _shippingId.intValue() : 0;
				targetUpload = UploadShipmentUtils.PopulateTemplate(request, true, false, false, null, null, false, 0,
						true, shippingId);
				attachmentFilename = UploadShipmentUtils.PopulateTemplate(request, false, true, false, null, null,
						false, 0, true, shippingId);
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
			saveMessages(request, messages);
			return mapping.findForward("error");
		}

		return null;
	}

	private ActionForward redirectPageFromRole(ActionMapping mapping, HttpServletRequest request) {
		// redirection page according to the Role ------------------------

		ActionMessages errors = new ActionMessages();
		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();

		if (role.equals(Constants.ROLE_STORE)) {
			return mapping.findForward("storeSubmitPocketSampleInformationPage");
		} else if (role.equals(Constants.ROLE_BLOM)) {
			return mapping.findForward("blomSubmitPocketSampleInformationPage");
		} else if (role.equals(Constants.FXMANAGE_ROLE_NAME)) {
			return mapping.findForward("fedexmanagerSubmitPocketSampleInformationPage");
		} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
			return mapping.findForward("localcontactSubmitPocketSampleInformationPage");
		} else if (role.equals(Constants.ROLE_USER) || role.equals(Constants.ROLE_INDUSTRIAL)) {
			return mapping.findForward("submitPocketSampleInformationPage");
		} else if (role.equals(Constants.ROLE_MANAGER)) {
			return mapping.findForward("managerSubmitPocketSampleInformationPage");
		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Role (" + role
					+ ") not found"));
			LOG.error("Role (" + role + ") not found");
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
	}

}
