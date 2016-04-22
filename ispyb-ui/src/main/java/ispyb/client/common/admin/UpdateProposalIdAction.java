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
/*
 * 
 */
package ispyb.client.common.admin;

import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.services.shipping.Shipping3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.sample.Protein3Service;

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
 * 
 * 
 * @struts.action name="updateProposalIdForm" path="/manager/updateProposalId" input="manager.welcome.page"
 *                validate="false" parameter="reqCode" scope="request"
 * @struts.action-forward name="error" path="site.default.error.page"
 * @struts.action-forward name="success" path="manager.admin.updateProposalId.page"
 * 
 */
public class UpdateProposalIdAction extends DispatchAction {

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Session3Service sessionService;

	private Protein3Service proteinService;

	private Shipping3Service shippingService;

	private final static Logger LOG = Logger.getLogger(UpdateProposalIdAction.class);

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {
		this.proteinService = (Protein3Service) ejb3ServiceLocator.getLocalService(Protein3Service.class);
		this.shippingService = (Shipping3Service) ejb3ServiceLocator.getLocalService(Shipping3Service.class);
		this.sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {

		return mapping.findForward("success");
	}

	public ActionForward update(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {

		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();

		UpdateProposalIdForm form = (UpdateProposalIdForm) actForm;
		Integer newPropId = form.getNewPropId();
		Integer oldPropId = form.getOldPropId();

		int nbUpdated = 0;

		try {
			nbUpdated = sessionService.updateProposalId(newPropId, oldPropId);
			nbUpdated = shippingService.updateProposalId(newPropId, oldPropId);
			nbUpdated = proteinService.updateProposalId(newPropId, oldPropId);

			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.updated", "proposalId"));

		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.collection.viewDataCollection"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();

			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return (mapping.findForward("error"));
			}

			if (!messages.isEmpty())
				saveMessages(request, messages);
			LOG.debug("Update of proposalId is finished");
		}

		return mapping.findForward("success");
	}

}
