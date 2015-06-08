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
package ispyb.client.biosaxs.form;

import ispyb.client.biosaxs.dataAdapter.BiosaxsActions;
import ispyb.client.biosaxs.dataAdapter.BiosaxsDataAdapterCommon;
import ispyb.common.util.Constants;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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
 * @struts.action name="uploadPdbFileFormSAXS" path="/user/uploadPdbFileSAXS" type="ispyb.client.biosaxs.form.UploadPdbFileActionSAXS"
 *                validate="false" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="viewUploadPdbFileSAXS" path="/tiles/bodies/biosaxs/uploadPdbFileSAXS.jsp"
 * @struts.action-forward name="viewDisplayPdbFile" path="/tiles/bodies/biosaxs/uploadPdbFileSAXS.jsp"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 */

public class UploadPdbFileActionSAXS extends DispatchAction {
	private final static Logger LOG = Logger.getLogger(UploadPdbFileActionSAXS.class);

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws CreateException, NamingException {
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
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
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {

		UploadPdbFileFormSAXS form = (UploadPdbFileFormSAXS) actForm;
		try {
			form.setMacromoleculeId(Integer.parseInt(request.getParameter("macromoleculeId")));
			form.setType(request.getParameter("type"));

		} catch (Exception e) {
			e.printStackTrace();
			return (mapping.findForward("error"));
		}

		return (mapping.findForward("viewUploadPdbFileSAXS"));
	}

	/**
	 * It builds the folder file path based on /{DATA_PDB_FILEPATH_START}/ProposalCodeProposalNumber/macromoleculeId
	 */
	private String getTargetFolder(HttpServletRequest request, FormFile uploadedFile, Integer macromoleculeId) throws Exception {
		BiosaxsActions actions = new BiosaxsActions();

		/**
		 * Gettting proposal code and number
		 */
		int proposalId = BiosaxsDataAdapterCommon.getProposalId(request);
		Proposal3VO proposal = actions.getProposalById(proposalId);

		String proposalName = proposal.getCode().toLowerCase() + proposal.getNumber();
		if (Constants.SITE_IS_EMBL()) {
			proposalName = proposal.getNumber();
		}

		return Constants.DATA_PDB_FILEPATH_START + proposalName + "/";
	}

	private String copyFileToDisk(HttpServletRequest request, UploadPdbFileFormSAXS form, Integer macromoleculeId) throws Exception {
		/**
		 * Copying file to the file system
		 */
		File folder = new File(this.getTargetFolder(request, form.getRequestFile(), macromoleculeId));
		LOG.info("Target Folder :" + folder.getAbsolutePath());

		boolean isDirCreated = folder.mkdirs();
		LOG.info("pdb, directory to create: " + folder.getAbsolutePath() + " = " + isDirCreated);

		/**
		 * Trying to copy with the same name if it doesn't exist yet
		 */
//		File file = new File(folder.getAbsolutePath() + "/" + form.getRequestFile().getFileName());
//		int i = 1;
//		while (file.exists()) {
//			file = new File(file.getAbsolutePath() + "_" + i);
//			i++;
//		}
		LOG.info( new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
		LOG.info(  form.getRequestFile().getFileName());
		LOG.info(  new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + "_" + form.getRequestFile().getFileName());
		String fileName = folder.getAbsolutePath() + "/"  + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + "_" + form.getRequestFile().getFileName();
		File file = new File(fileName);
		// write file
		System.out.println("File: " + file.getAbsolutePath());
		FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath());
		fileOut.write(form.getRequestFile().getFileData());
		fileOut.close();

		return file.getAbsolutePath();

	}

	public ActionForward uploadPdbFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		LOG.info("upload pdb file...");
		ActionMessages messages = new ActionMessages();

		/**
		 * Getting parameters from the form
		 */
		UploadPdbFileFormSAXS form = (UploadPdbFileFormSAXS) actForm;
		Integer macromoleculeId = form.getMacromoleculeId();
		String type = form.getType().toUpperCase();

		String fileName = form.getRequestFile().getFileName();
		try {
			/**
			 * Copying file to file system under /X/X/ProposalCodeProposalNumber/MacromoleculeId
			 */
			String targetFilePath = this.copyFileToDisk(request, form, macromoleculeId);

			BiosaxsActions actions = new BiosaxsActions();
			/** Storing into the data base **/
			if (type.equals("PDB") || type.equals("SEQUENCE")) {
				actions.saveStructure(macromoleculeId, fileName, targetFilePath, type);
			}

			if (type.equals("CONTACTS")) {
				LOG.info("Contacts");
				/** Saving contacts description **/
				Macromolecule3VO macromolecule3vo = actions.findMacromoleculesById(macromoleculeId);
				macromolecule3vo.setContactsDescriptionFilePath(targetFilePath);
				actions.saveMacromolecule(macromolecule3vo);
				LOG.info("Saved");
			}

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		LOG.info("upload pdb file complete");

		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free", "The file was successfully uploaded."));
		saveMessages(request, messages);

		return (mapping.findForward("viewUploadPdbFileSAXS"));
	}

	public ActionForward displayPdbFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		LOG.info("display pdb file...");
		return mapping.findForward("viewDisplayPdbFileSAXS");
	}

}
