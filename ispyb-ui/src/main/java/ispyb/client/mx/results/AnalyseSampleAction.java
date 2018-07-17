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
 * AnalyseSampleAction.java
 */

package ispyb.client.mx.results;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.client.common.BreadCrumbsForm;
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
import org.apache.struts.actions.DispatchAction;

/**
 * @struts.action name="analyseSampleForm" path="/user/analyseSample" type="ispyb.client.mx.sample.ViewSampleAction"
 *                validate="false" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="analyseSample" path="user.results.sample.analyse.page"
 * 
 * @struts.action-forward name="editSample" path="user.results.sample.edit.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 * 
 */

public class AnalyseSampleAction extends DispatchAction {

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private BLSample3Service sampleService;

	private final static Logger LOG = Logger.getLogger(AnalyseSampleAction.class);

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

	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayFromMenu(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		BreadCrumbsForm.getItClean(request);

		return this.display(mapping, actForm, request, response);

	}

	/**
	 * Display the list of samples to analyse : after experiment add some more comments about these samples the comments
	 * could be the ones of the BAG report for example
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		List mList = new ArrayList();

		ActionMessages mErrors = new ActionMessages();

		LOG.debug("calculate the list of samples for a proposal");

		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

		try {

			AnalyseSampleForm form = (AnalyseSampleForm) actForm;

			mList = sampleService.findByProposalId(proposalId);

			// Populate the form with Info
			form.setListInfo(mList);
			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

		} catch (Exception e) {

			mErrors.add(this.HandlesException(e));
		}

		if (!mErrors.isEmpty()) {
			saveErrors(request, mErrors);
			return (mapping.findForward("error"));
		}
		return mapping.findForward("analyseSample");
	}

	/**
	 * Retrieve details from a sample and put it on the form to edit sample after experiment.
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward editSample(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages mErrors = new ActionMessages();

		try {

			Integer blsampleId = new Integer(request.getParameter(Constants.BLSAMPLE_ID));

			BLSample3VO slv = sampleService.findByPk(blsampleId, false, false, false);

			AnalyseSampleForm form = (AnalyseSampleForm) actForm;
			form.setInfo(slv);
			FormUtils.setFormDisplayMode(request, actForm, FormUtils.EDIT_MODE);

		} catch (Exception e) {

			mErrors.add(this.HandlesException(e));
		}

		if (!mErrors.isEmpty()) {
			saveErrors(request, mErrors);
			return (mapping.findForward("error"));
		}

		return (mapping.findForward("editSample"));

	}

	/**
	 * Update sample parameters
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward updateSample(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages messages = new ActionMessages();

		ActionMessages mErrors = new ActionMessages();

		try {

			// Retrieve updated information from form
			AnalyseSampleForm form = (AnalyseSampleForm) actForm;

			// retrieve the object, change the fields and save it again
			BLSample3VO slvFromDB = sampleService.findByPk(form.getInfo().getBlSampleId(), false, false, false);

			slvFromDB.setCompletionStage(form.getInfo().getCompletionStage());
			slvFromDB.setStructureStage(form.getInfo().getStructureStage());
			slvFromDB.setPublicationStage(form.getInfo().getPublicationStage());
			slvFromDB.setPublicationComments(form.getInfo().getPublicationComments());
			slvFromDB.setBlSampleStatus(form.getInfo().getBlSampleStatus());

			sampleService.update(slvFromDB);

			// update also BMP to have a correct view
			BLSample3VO fullValue = sampleService.findByPk(form.getInfo().getBlSampleId(), false, false, false);

			fullValue.setCompletionStage(form.getInfo().getCompletionStage());
			fullValue.setStructureStage(form.getInfo().getStructureStage());
			fullValue.setPublicationStage(form.getInfo().getPublicationStage());
			fullValue.setPublicationComments(form.getInfo().getPublicationComments());
			fullValue.setBlSampleStatus(form.getInfo().getBlSampleStatus());

			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.inserted", "Sample parameters"));
			saveMessages(request, messages);

		} catch (Exception e) {

			mErrors.add(this.HandlesException(e));
		}

		if (!mErrors.isEmpty()) {
			saveErrors(request, mErrors);
			return (mapping.findForward("error"));
		}
		return this.display(mapping, actForm, request, response);

	}

	/**
	 * Handles the Exception for the various dispatchAction methods
	 * 
	 * @param e
	 */
	private ActionMessages HandlesException(Exception e) {

		ActionMessages mErrors = new ActionMessages();
		mErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.sample.viewSample"));
		mErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
		e.printStackTrace();
		return mErrors;
	}

}
