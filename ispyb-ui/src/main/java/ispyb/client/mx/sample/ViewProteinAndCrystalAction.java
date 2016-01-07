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
import ispyb.client.common.util.DBConstants;
import ispyb.common.util.DBTools;
import ispyb.common.util.Constants;
import ispyb.common.util.PathUtils;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.sample.Crystal3Service;
import ispyb.server.mx.services.sample.DiffractionPlan3Service;
import ispyb.server.mx.services.sample.Protein3Service;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.DiffractionPlan3VO;
import ispyb.server.mx.vos.sample.Protein3VO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

/**
 * 
 * @struts.action name="viewCrystalForm" path="/user/viewProteinAndCrystal" input="user.sample.viewSampleList.page"
 *                type="ispyb.client.mx.sample.ViewProteinAndCrystalAction" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 * @struts.action-forward name="viewProteinAndCrystal" path="user.sample.viewProteinAndCrystal.page"
 * @struts.action-forward name="crystalEditPage" path="user.crystal.edit.page"
 * 
 * 
 * 
 */
public class ViewProteinAndCrystalAction extends org.apache.struts.actions.DispatchAction {

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Protein3Service proteinService;

	private Crystal3Service crystalService;

	private DiffractionPlan3Service difPlanService;

	private final static Logger LOG = Logger.getLogger(ViewProteinAndCrystalAction.class);

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws CreateException, NamingException {

		this.proteinService = (Protein3Service) ejb3ServiceLocator.getLocalService(Protein3Service.class);
		this.crystalService = (Crystal3Service) ejb3ServiceLocator.getLocalService(Crystal3Service.class);
		this.difPlanService = (DiffractionPlan3Service) ejb3ServiceLocator
				.getLocalService(DiffractionPlan3Service.class);

	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
		LOG.debug("Calculate the list of proteins for acronym or for proposal #" + proposalId);
		ActionMessages errors = new ActionMessages();

		// Save Originator for return path
		BreadCrumbsForm.getItClean(request).setFromPage(
				request.getContextPath() + "/user/viewProteinAndCrystal.do?reqCode=display");

		try {
			// Retrieve Acronym from form
			ViewCrystalForm form = (ViewCrystalForm) actForm;
			String acronym = form.getAcronym();

			Protein3VO plv = new Protein3VO();
			plv.setAcronym(acronym);
			List<Protein3VO> proteinList = new ArrayList<Protein3VO>();

			// Get an object list
			if (acronym == null || acronym.length() == 0) // If no acronym selected on the form
			{
				proteinList = proteinService.findByProposalId(proposalId, true, false);
			} else {
				acronym = acronym.replace('*', '%');
				proteinList = proteinService.findByAcronymAndProposalId(proposalId, acronym, true, false);
				if (proteinList.size() == 1)
					plv = proteinList.get(0);
			}

			BreadCrumbsForm.getIt(request).setSelectedProtein(plv);

			form.setListProtein(proteinList);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.sample.viewProtein"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		return mapping.findForward("viewProteinAndCrystal");

	}

	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward editCrystal(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		LOG.debug("Edit a crystal");

		ActionMessages errors = new ActionMessages();
		Integer crystalId = null;
		Integer diffractionPlanId = null;

		try {
			crystalId = new Integer(request.getParameter(Constants.CRYSTAL_ID));

			ViewCrystalForm form = (ViewCrystalForm) actForm;

			// Retrieve crystal
			Crystal3VO clv = crystalService.findByPk(crystalId, false);

			// Retrieve proteins

			if (clv.getDiffractionPlanVO() != null) {
				diffractionPlanId = clv.getDiffractionPlanVOId();
				DiffractionPlan3VO difPlanInfo = difPlanService.findByPk(diffractionPlanId, false, false);
				form.setDifPlanInfo(difPlanInfo);
			}

			// Populate with Info
			form.setInfo(clv);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.CREATE_MODE);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return mapping.findForward("crystalEditPage");
	}

	public ActionForward update(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();

		LOG.debug("update the crystal parameters");

		try {
			DiffractionPlan3VO newDiffractionPlan = new DiffractionPlan3VO();

			ViewCrystalForm form = (ViewCrystalForm) actForm;

			Crystal3VO info = form.getInfo();
			Integer crystalId = info.getCrystalId();

			if (!checkCrystalInformations(form, request)) {
				return mapping.findForward("crystalEditPage");
			}

			// retrieve the crystal parameters

			Crystal3VO clvFromDB = crystalService.findByPk(crystalId, false);

			clvFromDB.setCellA(info.getCellA());
			clvFromDB.setCellB(info.getCellB());
			clvFromDB.setCellC(info.getCellC());
			clvFromDB.setCellAlpha(info.getCellAlpha());
			clvFromDB.setCellBeta(info.getCellBeta());
			clvFromDB.setCellGamma(info.getCellGamma());

			clvFromDB.setComments(info.getComments());

			// update DB and BMP BlSampleFull

			crystalService.update(clvFromDB);
			// TODO fix EJB3
			// blsampleFull.updateCrystal3VOs((CrystalLightValue) clvFromDB);
			// warning if cell values are empty
			if (!clvFromDB.hasCellInfo()){
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free", "Warning: the unit cell paramters are not filled!"));
				saveMessages(request, messages);
			}

			LOG.debug("update the crystal parameters in sampleFull and DB");

			// retrieve difPlan parameters
			String nullString = null;
			if (form.getDifPlanInfo().getExperimentKind() != null
					&& form.getDifPlanInfo().getExperimentKind().length() == 0) {
				DiffractionPlan3VO tempDifPlan = form.getDifPlanInfo();
				tempDifPlan.setExperimentKind(nullString);
				form.setDifPlanInfo(tempDifPlan);
			}

			if (form.getDifPlanInfo().getDiffractionPlanId() != null
					&& form.getDifPlanInfo().getDiffractionPlanId().intValue() != 0) {
				// Update

				newDiffractionPlan = form.getDifPlanInfo();
				difPlanService.update(newDiffractionPlan);

				form.setDifPlanInfo(newDiffractionPlan);
			} else {
				// Insert

				DiffractionPlan3VO newDifPlan = form.getDifPlanInfo();
				if (newDifPlan.getDiffractionPlanId() != null && newDifPlan.getDiffractionPlanId() < 1)
					newDifPlan.setDiffractionPlanId(null);
				newDiffractionPlan = difPlanService.create(newDifPlan);

				form.setDifPlanInfo(newDiffractionPlan);

				Crystal3VO selectedCrystal = crystalService.findByPk(crystalId, false);
				selectedCrystal.setDiffractionPlanVO(newDiffractionPlan);
				crystalService.update(selectedCrystal);

			}
			// Acknowledge action

			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.update", info.getSpaceGroup()));
			saveMessages(request, messages);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.getMessage()));
			e.printStackTrace();
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		//return this.display(mapping, actForm, request, response);
		return mapping.findForward("crystalEditPage");

	}

	public ActionForward deleteCrystal(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();

		try {

			ViewCrystalForm form = (ViewCrystalForm) actForm;

			Crystal3VO info = form.getInfo();
			Integer crystalId = new Integer(request.getParameter(Constants.CRYSTAL_ID));

			// Retrieve the Crystal and delete
			Crystal3VO clvFromDB = crystalService.findByPk(crystalId, false);
			
			// delete the file on pyarch
			if(clvFromDB != null && clvFromDB.getPdbFilePath() != null && clvFromDB.getPdbFileName() != null){
				String realPdbFilePath = PathUtils.FitPathToOS(clvFromDB.getPdbFilePath()+"/"+clvFromDB.getPdbFileName());
				try{
					File f = new File(realPdbFilePath);
					boolean isDel = f.delete();
					if (!isDel){
						messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free", "Error while deleting the pdb file, the file has not been deleted from the file system "));
						saveMessages(request, messages);
					}
					LOG.debug("PDB file deleted crystalId=" + crystalId);
				}catch (SecurityException e){
					e.printStackTrace();
					LOG.error(e.toString());
					messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free", "Error while deleting the pdb file "+e.toString()));
					saveMessages(request, messages);
				}
			}
			
			if(clvFromDB != null){
				crystalService.delete(clvFromDB);
			}

			LOG.debug("Crystal Deleted. crystalId=" + crystalId);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.getMessage()));
			e.printStackTrace();
			saveErrors(request, errors);
			return mapping.findForward("error");
		}

		return this.display(mapping, actForm, request, response);
	}

	/**
	 * crystalHasSample
	 * 
	 * @param crystalId
	 * @return
	 */
	public static int crystalHasSample(int crystalId) {
		int crystalHasSample = 0;
		try {
			if (DBTools.crystalHasSample(crystalId) > 0)
				crystalHasSample = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return crystalHasSample;
	}

	public static int proteinIsCreatedBySampleSheet(int proteinId) {
		int isCreatedBySampleSheet = 0;
		try {
			if (DBTools.proteinIsCreatedBySampleSheet(proteinId) > 0)
				isCreatedBySampleSheet = 1;
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return isCreatedBySampleSheet;
	}

	/**
	 * checks if the crystal in the form are correct and well formated, returns false if at least one field is incorrect
	 * 
	 * @param form
	 * @return
	 */
	private boolean checkCrystalInformations(ViewCrystalForm form, HttpServletRequest request) {
		ActionMessages l_ActionMessages = new ActionMessages();
		boolean isOk = true;
		// comments
		if (form.getInfo().getComments() != null
				&& form.getInfo().getComments().length() > DBConstants.MAX_LENGTH_CRYSTAL_COMMENTS) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.length", "Comments",
					DBConstants.MAX_LENGTH_CRYSTAL_COMMENTS);
			l_ActionMessages.add("info.comments", l_ActionMessageLabel);
		}
		if (!isOk) {
			request.setAttribute("org.apache.struts.action.ERROR", l_ActionMessages);
		}
		return isOk;
	}

}
