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
package ispyb.client.mx.prepare;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.mx.sample.ViewSampleForm;
import ispyb.common.util.Constants;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.vos.sample.BLSample3VO;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * @struts.action name="viewSampleForm" path="/user/prepareExpSample" type="ispyb.client.mx.prepare.PrepareExpSample"
 *                validate="false" parameter="reqCode" scope="request"
 * @struts.action-forward name="success" path="user.prepare.sample.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 */
public class PrepareExpSample extends org.apache.struts.actions.DispatchAction {

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private BLSample3Service sampleService;

	private final static Logger LOG = Logger.getLogger(PrepareExpSample.class);

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {

		this.sampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	public ActionForward selectSample(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		List<BLSample3VO> freeSampleList = new ArrayList<BLSample3VO>();
		ActionMessages errors = new ActionMessages();
		try {
			// Clean BreadCrumbsForm
			BreadCrumbsForm.getItClean(request);

			// Retrieve Attributes
			ViewSampleForm form = (ViewSampleForm) actForm; // Parameters submited by form
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

			// Retrieve information from DB
			freeSampleList = sampleService.findByProposalIdAndDewarNull(proposalId);

			LOG.debug("prepare sample for proposal: " + proposalId);

			// Populate with Info
			form.setFreeSampleList(freeSampleList);
			FormUtils.setFormDisplayMode(request, actForm, FormUtils.EDIT_MODE);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return mapping.findForward("success");
	}

	public ActionForward updateSampleStatus(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();
		ViewSampleForm form = (ViewSampleForm) actForm;

		try {
			List<String> sampleList = form.getSelectedSamplesList();
			Integer blSampleId;
			String sampleIdstr = "";

			for (int i = 0; i < sampleList.size(); i++) {
				// Browse through selected Samples

				sampleIdstr = sampleList.get(i);
				if (!sampleIdstr.equals("")) {

					blSampleId = new Integer(sampleIdstr);
					LOG.debug("prepare sample : set sample in processing state with sampleId =" + blSampleId);
					BLSample3VO info = sampleService.findByPk(blSampleId, false, false, false);
					info.setBlSampleStatus(Constants.SHIPPING_STATUS_PROCESS);
					sampleService.update(info);
				}
			}

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			saveErrors(request, errors);
			e.printStackTrace();
			return mapping.findForward("error");

		}

		return this.selectSample(mapping, actForm, request, in_reponse);
	}

	public ActionForward cleanSampleStatus(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();
		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
		String status = Constants.SHIPPING_STATUS_PROCESS;

		try {
			// Retrieve information from DB

			List<BLSample3VO> listInfo = sampleService.findByProposalIdAndBlSampleStatus(proposalId, status);

			for (int i = 0; i < listInfo.size(); i++) {
				// Browse through Samples
				BLSample3VO blv = listInfo.get(i);

				blv.setBlSampleStatus("");
				sampleService.update(blv);
			}
			LOG.debug("prepare sample : reset sampleStatus");

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			saveErrors(request, errors);
			e.printStackTrace();
			return mapping.findForward("error");

		}

		return this.selectSample(mapping, actForm, request, in_reponse);
	}

}
