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
package ispyb.client.mx.sample;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.util.FileUtil;
import ispyb.common.util.Constants;
import ispyb.common.util.PathUtils;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.sample.Crystal3Service;
import ispyb.server.mx.services.sample.DiffractionPlan3Service;
import ispyb.server.mx.services.sample.Protein3Service;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.DiffractionPlan3VO;
import ispyb.server.mx.vos.sample.Protein3VO;

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
 * @struts.action name="uploadPdbFileForm" path="/user/uploadPdbFile" type="ispyb.client.mx.sample.UploadPdbFileAction" validate="false"
 *                parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="viewUploadPdbFile" path="user.sample.viewUploadPdbFile.page"
 * @struts.action-forward name="viewDisplayPdbFile" path="user.sample.viewDisplayPdbFile.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 */

public class UploadPdbFileAction extends DispatchAction {
	private final static Logger LOG = Logger.getLogger(UploadPdbFileAction.class);

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Crystal3Service crystalService;

	private Protein3Service proteinService;

	private DiffractionPlan3Service difPlanService;

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws CreateException, NamingException {
		crystalService = (Crystal3Service) ejb3ServiceLocator.getLocalService(Crystal3Service.class);
		proteinService = (Protein3Service) ejb3ServiceLocator.getLocalService(Protein3Service.class);
		this.difPlanService = (DiffractionPlan3Service) ejb3ServiceLocator.getLocalService(DiffractionPlan3Service.class);
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
		ActionMessages errors = new ActionMessages();

		// Retrieve Attributes
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
		Integer crystalId = null;
		try {
			crystalId = new Integer(request.getParameter(Constants.CRYSTAL_ID));
		} catch (NumberFormatException e) {
			// LOG.error(e.toString());
		}
		String proteinAcronym = null;
		try {
			proteinAcronym = new String(request.getParameter(Constants.PROTEIN_ACRONYM));
		} catch (Exception e) {
			// LOG.error(e.toString());
		}
		UploadPdbFileForm form = (UploadPdbFileForm) actForm;
		form.setCrystalId(crystalId);
		form.setProteinAcronym(proteinAcronym);
		String pdbFileFullPath = null;
		try {
			if (crystalId != null) {
				// Retrieve from DB
				Crystal3VO crystal = crystalService.findByPk(crystalId, false);
				BreadCrumbsForm.getIt(request).setSelectedCrystal(crystal);
				String pdbFileName = "";
				String pdbFilePath = "";

				if (crystal != null) {
					pdbFileName = crystal.getPdbFileName();
					pdbFilePath = crystal.getPdbFilePath();
					if (pdbFileName != null && pdbFilePath != null)
						pdbFileFullPath = pdbFilePath + pdbFileName;
				}
			}
			if (proteinAcronym != null) {
				List<Protein3VO> listProtein = proteinService.findByAcronymAndProposalId(proposalId, proteinAcronym);
				Protein3VO protein = null;
				if (listProtein != null && listProtein.size() > 0)
					protein = listProtein.get(0);
				BreadCrumbsForm.getIt(request).setSelectedProtein(protein);
			}
			form.setFileFullPath(pdbFileFullPath);
			form.setUpdateCellValues("1");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Error while displaying upload pdf file: " + e.toString());
			e.printStackTrace();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		return (mapping.findForward("viewUploadPdbFile"));
	}

	public ActionForward uploadPdbFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		LOG.info("upload pdb file...");
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();

		// proposal
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
		String proposalCode = (String) request.getSession().getAttribute(Constants.PROPOSAL_CODE);
		String proposalNumber = String.valueOf(request.getSession().getAttribute(Constants.PROPOSAL_NUMBER));
		String proposalName = proposalCode.toLowerCase() + proposalNumber.toString();

		// pdb file
		UploadPdbFileForm form = (UploadPdbFileForm) actForm;
		FormFile uploadedFile = form.getRequestFile();
		String uploadedFileName = uploadedFile.getFileName();

		int fileSize = uploadedFile.getFileSize();
		byte[] fileData = uploadedFile.getFileData();

		// --- Check File is there ---
		if (uploadedFile.getFileName().equals("") || fileSize == 0) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.upload.file"));
			saveErrors(request, errors);
			return (mapping.findForward("viewUploadPdbFile"));
		}
		String fileExtension = uploadedFileName.substring(uploadedFileName.lastIndexOf("."));
		// --- Check Extension ---
		if (fileExtension.toLowerCase().compareTo(".pdb") != 0) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.pdb", uploadedFileName));
			saveErrors(request, errors);
			return (mapping.findForward("viewUploadPdbFile"));
		}

		// crystal
		// Retrieve Attributes
		Integer crystalId = null;
		try {
			crystalId = new Integer(request.getParameter(Constants.CRYSTAL_ID));
		} catch (NumberFormatException e) {
			// LOG.error(e.toString());
			// errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free",
			// "Error while uploading the pdb file "+e.toString()));
			// saveErrors(request, errors);
			// return (mapping.findForward("error"));
		}
		boolean crystalCreation = (crystalId == null);
		String proteinAcronym = null;
		try {
			proteinAcronym = new String(request.getParameter(Constants.PROTEIN_ACRONYM));
		} catch (Exception e) {
			// LOG.error(e.toString());
		}
		if (crystalId == null && proteinAcronym == null) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free",
					"Error while uploading the pdb file -- no protein found "));
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}
		// Retrieve from DB
		Crystal3VO crystal = null;
		if (crystalId != null) {
			crystal = crystalService.findByPk(crystalId, false);
		}
		boolean isok = true;
		// delete the actual pdb if needed
		if (crystal != null && crystal.getPdbFileName() != null && crystal.getPdbFilePath() != null) {
			isok = deleteCrystalPdb(crystal, form, request, errors);
		}

		// write pdb on pyarch
		String pdbFilePath = Constants.DATA_PDB_FILEPATH_START + proposalName + "/";
		String pdbFullFilePath = pdbFilePath + uploadedFileName;
		String realPdbFilePath = PathUtils.FitPathToOS(pdbFullFilePath);
		try {
			// create proposal directory if needed
			File fDir = new File(PathUtils.FitPathToOS(pdbFilePath));
			boolean isDirCreated = fDir.mkdirs();
			LOG.info("pdb, directory to create: " + pdbFilePath + " = " + isDirCreated);
			// file already exist?
			File f = new File(realPdbFilePath);
			if (f.exists()) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free", "This file already exists."
						+ realPdbFilePath));
				saveErrors(request, errors);
				return (mapping.findForward("viewUploadPdbFile"));
			}
			// write file
			FileOutputStream fileOut = new FileOutputStream(realPdbFilePath);
			fileOut.write(fileData);
			fileOut.close();

		} catch (IOException e) {
			e.printStackTrace();
			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("message.free", "Error while uploading the pdb file " + e.toString()));
			saveErrors(request, errors);
			return (mapping.findForward("viewUploadPdbFile"));
		} catch (SecurityException e) {
			e.printStackTrace();
			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("message.free", "Error while uploading the pdb file " + e.toString()));
			saveErrors(request, errors);
			return (mapping.findForward("viewUploadPdbFile"));
		} catch (Exception e) {
			e.printStackTrace();
			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("message.free", "Error while uploading the pdb file " + e.toString()));
			saveErrors(request, errors);
			return (mapping.findForward("viewUploadPdbFile"));
		}
		LOG.info("upload pdb file complete");

		// create crystal if needed
		if (crystalCreation) {
			// protein
			Protein3VO protein = null;
			if (proteinAcronym != null) {
				List<Protein3VO> listProtein = proteinService.findByAcronymAndProposalId(proposalId, proteinAcronym);
				if (listProtein != null && listProtein.size() > 0)
					protein = listProtein.get(0);
			}
			// dif plan
			DiffractionPlan3VO difPlan = new DiffractionPlan3VO();
			difPlan.setExperimentKind(Constants.LIST_EXPERIMENT_KIND[0]);
			difPlanService.create(difPlan);
			// crystal creation
			crystal = new Crystal3VO();
			String crystalID = UUID.randomUUID().toString();
			crystal.setProteinVO(protein);
			crystal.setCrystalUUID(crystalID);
			crystal.setName(crystalID);
			crystal.setDiffractionPlanVO(difPlan);
			crystal = crystalService.create(crystal);
			form.setCrystalId(crystal.getCrystalId());
		}
		// update db
		crystal.setPdbFileName(uploadedFileName);
		crystal.setPdbFilePath(pdbFilePath);
		// update cell_* values
		boolean updateCell = form.getUpdateCellValues().equals("1");
		if (crystalCreation)
			updateCell = true;
		if (updateCell) {
			try {
				Object[] o = getCrystalInfo(realPdbFilePath);
				String spaceGroup = (String) o[0];
				Double[] cells = (Double[]) o[1];
				int c = 0;
				crystal.setCellA(cells[c++]);
				crystal.setCellB(cells[c++]);
				crystal.setCellC(cells[c++]);
				crystal.setCellAlpha(cells[c++]);
				crystal.setCellBeta(cells[c++]);
				crystal.setCellGamma(cells[c++]);
				if (crystalCreation)
					crystal.setSpaceGroup(spaceGroup);
			} catch (Exception e) {
				e.printStackTrace();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free",
						"Error while updating the cell information " + e.toString()));
				saveErrors(request, errors);
				return (mapping.findForward("viewUploadPdbFile"));
			}
		}

		crystalService.update(crystal);

		form.setFileFullPath(pdbFullFilePath);
		form.setRequestFile(null);
		if (isok) {
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free", "The file was successfully uploaded."));
			saveMessages(request, messages);
		}
		return (mapping.findForward("viewUploadPdbFile"));
	}

	public ActionForward displayPdbFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		LOG.info("display pdb file...");
		ActionMessages errors = new ActionMessages();
		UploadPdbFileForm form = (UploadPdbFileForm) actForm;
		// ---------------------------------------------------------------------------------------------------
		// Retrieve Attributes
		String fileName = request.getParameter("fileFullPath"); // Request parameters

		try {
			String fileFullPath = PathUtils.FitPathToOS(fileName);
			boolean contentPresent = (new File(fileFullPath)).exists();
			if (!contentPresent) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free",
						"Error while displaying the pdb file, the file does not exist "));
				saveErrors(request, errors);
				return this.display(mapping, actForm, request, response);
			}

			String fullFileContent = FileUtil.fileToString(fileFullPath);

			// Populate form
			form.setFileContent(fullFileContent);
			form.setFileContentPresent(contentPresent);
			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.toString());
			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("message.free", "Error while displaying the pdb file " + e.toString()));
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}
		return mapping.findForward("viewDisplayPdbFile");
	}

	public ActionForward deletePdbFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		LOG.info("delete pdb file...");
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		boolean delIsOk = true;
		try {
			UploadPdbFileForm form = (UploadPdbFileForm) actForm;
			// crystal
			// Retrieve Attributes
			Integer crystalId = null;
			try {
				crystalId = new Integer(request.getParameter(Constants.CRYSTAL_ID));
			} catch (NumberFormatException e) {
				LOG.error(e.toString());
				errors.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("message.free", "Error while deleting the pdb file " + e.toString()));
				saveErrors(request, errors);
				delIsOk = false;
				return (mapping.findForward("error"));
			}
			// Retrieve from DB
			Crystal3VO crystal = crystalService.findByPk(crystalId, false);

			delIsOk = deleteCrystalPdb(crystal, form, request, errors);

			if (delIsOk) {
				messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("message.free", "The pdb file was successfully removed."));
				saveMessages(request, messages);
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.toString());
			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("message.free", "Error while deleting the pdb file " + e.toString()));
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}
		return mapping.findForward("viewUploadPdbFile");
	}

	private boolean deleteCrystalPdb(Crystal3VO crystal, UploadPdbFileForm form, HttpServletRequest request, ActionMessages errors) {
		boolean isOk = true;
		try {
			// delete the file on pyarch
			String realPdbFilePath = PathUtils.FitPathToOS(crystal.getPdbFilePath() + "/" + crystal.getPdbFileName());
			try {
				File f = new File(realPdbFilePath);
				boolean isDel = f.delete();
				if (!isDel) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free",
							"Error while deleting the pdb file, the file has not been deleted from the file system "));
					saveErrors(request, errors);
					isOk = false;
				}
			} catch (SecurityException e) {
				e.printStackTrace();
				LOG.error(e.toString());
				errors.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("message.free", "Error while deleting the pdb file " + e.toString()));
				saveErrors(request, errors);
				isOk = false;
			}

			crystal.setPdbFileName(null);
			crystal.setPdbFilePath(null);
			crystalService.update(crystal);

			form.setFileFullPath(null);
			form.setRequestFile(null);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.toString());
			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("message.free", "Error while deleting the pdb file " + e.toString()));
			saveErrors(request, errors);
			isOk = false;
		}
		return isOk;
	}

	public Object[] getCrystalInfo(String pdbFileName) throws Exception {
		Double[] cells = new Double[6];
		String spaceGroup = "";
		InputStream ips = new FileInputStream(pdbFileName);
		InputStreamReader ipsr = new InputStreamReader(ips);
		BufferedReader br = new BufferedReader(ipsr);
		String line;
		while ((line = br.readLine()) != null) {
			if (line.startsWith("CRYST1")) { // CRYST1 contains the unit cell parameters & spaceGroup
				Double cellA = Double.parseDouble(line.substring(7, 15));
				cells[0] = cellA;
				Double cellB = Double.parseDouble(line.substring(16, 24));
				cells[1] = cellB;
				Double cellC = Double.parseDouble(line.substring(25, 33));
				cells[2] = cellC;
				Double cellAlpha = Double.parseDouble(line.substring(34, 40));
				cells[3] = cellAlpha;
				Double cellBeta = Double.parseDouble(line.substring(41, 47));
				cells[4] = cellBeta;
				Double cellGamma = Double.parseDouble(line.substring(48, 54));
				cells[5] = cellGamma;
				spaceGroup = line.substring(55, 66);
			}
		}
		br.close();
		ipsr.close();
		ips.close();
		Object[] o = new Object[2];
		o[0] = spaceGroup;
		o[1] = cells;
		return o;
	}
}
