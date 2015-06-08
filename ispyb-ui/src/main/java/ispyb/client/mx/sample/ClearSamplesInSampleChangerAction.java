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
 * ClearSamplesInSampleChanger.java
 */

package ispyb.client.mx.sample;

import ispyb.common.util.Constants;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.sample.BLSample3Service;

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
 * @struts.action name="viewSampleForm" path="/user/clearSamplesInSampleChanger" parameter="reqCode" scope="request"
 * @struts.action-forward name="success" path="user.sample.viewSample.page"
 * @struts.action-forward name="error" path="site.default.error.page"
 */

public class ClearSamplesInSampleChangerAction extends DispatchAction {

	private final static Logger LOG = Logger.getLogger(ClearSamplesInSampleChangerAction.class);

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward clearSampleChanger(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		LOG.debug("clear samples in SC");

		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();

		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

		try {
			BLSample3Service sampleService = (BLSample3Service) Ejb3ServiceLocator.getInstance().getLocalService(
					BLSample3Service.class);

			sampleService.resetIsInSampleChanger(proposalId);

			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.reset"));
			saveMessages(request, messages);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.collection.viewDataCollection"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		return mapping.findForward("success");
	}

}
