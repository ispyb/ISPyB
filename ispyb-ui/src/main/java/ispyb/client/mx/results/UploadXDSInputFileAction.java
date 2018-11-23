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
package ispyb.client.mx.results;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.util.FileUtil;
import ispyb.common.util.Constants;
import ispyb.common.util.PathUtils;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;

/**
 * @struts.action name="uploadXDSInputFileForm" path="/user/uploadXDSInputFile" type="ispyb.client.mx.results.UploadXDSInputFileAction" validate="false"
 *                parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="viewUploadXDSInputFile" path="user.sample.viewUploadXDSInputFile.page"
 * @struts.action-forward name="viewDisplayXDSInputFile" path="user.sample.viewDisplayXDSInputFile.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 */

public class UploadXDSInputFileAction extends DispatchAction {
	private final static Logger LOG = Logger.getLogger(UploadXDSInputFileAction.class);

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();


	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */


	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		return super.execute(mapping, form, request, response);
	}

	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
		public ActionForward uploadXDSInputFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		LOG.info("upload xds input file...");

		//ActionMessages messages = new ActionMessages();
		//UploadXDSInputFileForm form = (UploadXDSInputFileForm) actForm;
		//String type = form.getType().toUpperCase();
		//String fileName = form.getRequestFile().getFileName();
		//LOG.info(fileName);
		
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();

		// xds input file
		UploadXDSInputFileForm form = (UploadXDSInputFileForm) actForm;
		FormFile uploadedFile = form.getRequestFile();
		String uploadedFileName = uploadedFile.getFileName();

		int fileSize = uploadedFile.getFileSize();
		byte[] fileData = uploadedFile.getFileData();

		// --- Check File is there ---
		if (uploadedFile.getFileName().equals("") || fileSize == 0) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.upload.file"));
			saveErrors(request, errors);
			return (mapping.findForward("viewUploadXDSInputFile"));
		}
		
		// --- Check Extension ---
		if (uploadedFileName.compareTo("XDS.INP") != 0) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.xds", uploadedFileName));
			saveErrors(request, errors);
			return (mapping.findForward("viewUploadXDSInputFile"));
		}
		
		
		
		// write pdb on pyarch
		Integer collectionId = (Integer) request.getSession().getAttribute(Constants.DATA_COLLECTION_ID);
		//String beamline = (String) request.getSession().getAttribute(Constants.BEAMLINENAME);
		String xdsInputFilePath = Constants.DATA_XDS_INPUT_FILEPATH_START + "p13/xds_input_files/" + collectionId.toString() + "/";
		String xdsInputFullFilePath = xdsInputFilePath + uploadedFileName;
		String realXDSInputFilePath = PathUtils.FitPathToOS(xdsInputFullFilePath);
		try {
			// create proposal directory if needed
			File fDir = new File(PathUtils.FitPathToOS(xdsInputFilePath));
			boolean isDirCreated = fDir.mkdirs();
			LOG.info("xds input, directory to create: " + xdsInputFilePath + " = " + isDirCreated);
			// file already exist?
			File f = new File(realXDSInputFilePath);
			// write file
			FileOutputStream fileOut = new FileOutputStream(realXDSInputFilePath);
			fileOut.write(fileData);
			fileOut.close();

		} catch (IOException e) {
			e.printStackTrace();
			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("message.free", "Error while uploading the XDS.INP file " + e.toString()));
			saveErrors(request, errors);
			return (mapping.findForward("viewUploadXDSInputFile"));
		}
		
		
		LOG.info("upload xds input file complete");

		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free", "XDS.INP file was successfully uploaded."));
		saveMessages(request, messages);
		return (mapping.findForward("viewUploadXDSInputFile"));
	}
		
		
		public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {

			UploadXDSInputFileForm form = (UploadXDSInputFileForm) actForm;
			try {
				form.setType(request.getParameter("type"));

			} catch (Exception e) {
				e.printStackTrace();
				return (mapping.findForward("error"));
			}

			return (mapping.findForward("viewUploadXDSInputFile"));
		}


                public ActionForward processingTest(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

                return null;

        }

		
		public ActionForward displayXDSInputFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			LOG.info("display xds input file...");
			return mapping.findForward("viewDisplayXDSInputFile");
		}
		
}
