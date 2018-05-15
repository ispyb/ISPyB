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

import ispyb.client.common.BreadCrumbsForm;
import ispyb.common.util.Constants;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.services.sample.DiffractionPlan3Service;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.DiffractionPlan3VO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * @struts.action name="viewSampleForm" path="/user/editDiffractionPlan"
 *                type="ispyb.client.mx.sample.EditDiffractionPlanAction" input="user.sampleForContainer.create.page"
 *                validate="false" parameter="reqCode" scope="request"
 * 
 * 
 * @struts.action-forward name="sampleForContainerCreatePage" path="user.sample.create.page"
 * 
 * @struts.action-forward name="editSampleForCreate" path="user.sample.create.edit.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 * 
 */
public class EditDiffrationPlanAction extends AbstractSampleAction {

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward saveDifPlan(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages messages = new ActionMessages();
		ActionMessages mErrors = new ActionMessages();
		String successPage = new String("sampleForContainerCreatePage");
		Integer blsampleId = new Integer(0);
		DiffractionPlan3VO newDiffractionPlan;

		try {
			ViewSampleForm form = (ViewSampleForm) actForm;

			if (BreadCrumbsForm.getIt(request).getSelectedSample() != null)
				blsampleId = BreadCrumbsForm.getIt(request).getSelectedSample().getBlSampleId();

			// Set sample info
			BLSample3Service sampleService = (BLSample3Service) ejb3ServiceLocator
					.getLocalService(BLSample3Service.class);
			DiffractionPlan3Service difPlanService = (DiffractionPlan3Service) ejb3ServiceLocator
					.getLocalService(DiffractionPlan3Service.class);

			BLSample3VO slv = sampleService.findByPk(blsampleId, false, false, false);
			form.setInfo(slv);
			form.setTheCrystalId(slv.getCrystalVO().getCrystalId());
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			List crystalInfoList = populateCrystalInfoList(proposalId);
			form.setListCrystal(crystalInfoList);
			BreadCrumbsForm.getIt(request).setSelectedSample(slv);

			// ------------------------------- Update ---------------------------------------------
			if (form.getDifPlanInfo().getDiffractionPlanId() != null
					&& form.getDifPlanInfo().getDiffractionPlanId().intValue() != 0) {
				newDiffractionPlan = form.getDifPlanInfo();
				difPlanService.update(newDiffractionPlan);

				form.setDifPlanInfo(newDiffractionPlan);
			} else
			// ------------------------------- Insert ---------------------------------------------
			{
				DiffractionPlan3VO newDifPlan = new DiffractionPlan3VO();
				newDifPlan = form.getDifPlanInfo();
				newDiffractionPlan = difPlanService.create(newDifPlan);
				form.setDifPlanInfo(newDiffractionPlan);

				if (blsampleId.intValue() > 0) {
					// Sample
					BLSample3VO selectedSample = sampleService.findByPk(blsampleId, false, false, false);
					selectedSample.setDiffractionPlanVO(newDiffractionPlan);
					sampleService.update(selectedSample);

				}
			}

			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.inserted",
					"Diffraction plan parameters"));
			saveMessages(request, messages);
		} catch (Exception e) {
			mErrors.add(this.HandlesException(e));
		}

		if (!mErrors.isEmpty()) {
			saveErrors(request, mErrors);
			return (mapping.findForward("error"));
		}
		return mapping.findForward(successPage);
	}

}
