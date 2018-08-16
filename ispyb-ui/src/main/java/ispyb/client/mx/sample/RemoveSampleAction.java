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
import ispyb.server.mx.vos.sample.BLSample3VO;

import java.util.ArrayList;

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

/**
 * @struts.action name="viewSampleForm" path="/user/removeSampleAction" type="ispyb.client.mx.sample.RemoveSampleAction"
 *                input="user.sample.create.page" validate="false" parameter="reqCode" scope="request"
 * 
 * 
 * @struts.action-forward name="displayForBreadCrumbs" path=
 *                        "/menuSelected.do?leftMenuId=11&amp;topMenuId=10&amp;targetUrl=%2Fuser%2FviewSample.do%3FreqCode%3DdisplayForBreadCrumbs"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 */

public class RemoveSampleAction extends DispatchAction {

	private BLSample3Service sampleService;

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private final static Logger LOG = Logger.getLogger(RemoveSampleAction.class);

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws CreateException, NamingException {

		this.sampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	/**
	 * Delete a sample
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return Actionforward
	 */
	public ActionForward deleteSample(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();

		String originator = BreadCrumbsForm.getIt(request).getFromPage();
		LOG.debug("delete sample : originator = " + originator);

		try {
			LOG.debug("Deleting Sample....");

			Integer blsampleId = new Integer(request.getParameter(Constants.BLSAMPLE_ID));
			sampleService.deleteByPk(blsampleId);

			// ----- Return to Originator -----

			if (originator != null) {
				BreadCrumbsForm.getIt(request).setFromPage(null);
				response.sendRedirect(originator);
				return null;
			}

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.sample.delete"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}
		return mapping.findForward("displayForBreadCrumbs");

	}

	/**
	 * Remove a sample from a container = unassign the sample
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return actionForward
	 */
	public ActionForward removeSampleFromContainer(ActionMapping mapping, ActionForm actForm,
			HttpServletRequest request, HttpServletResponse response) {

		ActionMessages messages = new ActionMessages();
		ActionMessages errors = new ActionMessages();

		String originator = BreadCrumbsForm.getIt(request).getFromPage();
		LOG.debug("removesample from container : originator = " + originator);

		try {

			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			ViewSampleForm form = (ViewSampleForm) actForm;
			String _sampleId = request.getParameter(Constants.BLSAMPLE_ID);
			Integer sampleId = new Integer(_sampleId);

			// retrieve the object, change the fields and save it again
			BLSample3VO currentSampleFromDB = sampleService.findByPk(sampleId, false, false, false);

			currentSampleFromDB.setContainerVO(null);

			sampleService.update(currentSampleFromDB);

			form.setListInfo(new ArrayList()); // to clean list and force recalculation

			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.inserted", "Sample parameters"));
			saveMessages(request, messages);

			// ----- Return to Originator -----

			if (originator != null) {
				BreadCrumbsForm.getIt(request).setFromPage(null);
				response.sendRedirect(originator);
				return null;
			}
			// ---------------------------------

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}
		return mapping.findForward("displayForBreadCrumbs");

	}

}
