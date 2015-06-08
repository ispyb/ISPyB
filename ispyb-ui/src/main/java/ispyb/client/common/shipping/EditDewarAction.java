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
 * editDewar.java
 */

package ispyb.client.common.shipping;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.client.common.BreadCrumbsForm;
import ispyb.common.util.Constants;
import ispyb.server.common.services.shipping.Dewar3Service;
import ispyb.server.common.services.shipping.Shipping3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;

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
 * @struts.action name="viewDewarForm" path="/user/editDewar" type="ispyb.client.common.shipping.dewar.EditDewarAction"
 *                input="fedexmanager.fxshipping.view.page" validate="false" parameter="reqCode" scope="session"
 * @struts.action-forward name="dewarEditPageFX" path="fedexmanager.fxshipping.dewar.edit.page"
 * @struts.action-forward name="error" path="site.default.error.page"
 * @struts.action-forward name="shippingFXViewPage" path="fedexmanager.fxshipping.view.page"
 * 
 * 
 */
public class EditDewarAction extends org.apache.struts.actions.DispatchAction {
	private final Logger LOG = Logger.getLogger(EditDewarAction.class);
	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	/**
	 * display
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();

		try {
			Dewar3Service dewarService = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);
			Shipping3Service shippingService = (Shipping3Service) ejb3ServiceLocator
					.getLocalService(Shipping3Service.class);

			Integer dewarId = new Integer(request.getParameter(Constants.DEWAR_ID));

			Dewar3VO info = dewarService.findByPk(dewarId, false, false);
			Integer shippingId = info.getShippingVO().getShippingId();

			Shipping3VO ship = shippingService.findByPk(shippingId, false);

			// Populate BreadCrumbs
			BreadCrumbsForm.getItClean(request).setSelectedShipping(ship);
			BreadCrumbsForm.getIt(request).setSelectedDewar(info);

			// populate form
			ViewDewarForm form = (ViewDewarForm) actForm;
			form.setInfo(info);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.EDIT_MODE);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return mapping.findForward("dewarEditPageFX");
	}

	public ActionForward save(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		try {
			Dewar3Service dewarService = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);

			ViewDewarForm form = (ViewDewarForm) actForm;

			Dewar3VO info = form.getInfo();

			dewarService.update(info);

			response.sendRedirect("/user/viewShippingAction.do?reqCode=displayAllFX");

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.fedexmanager.shipping.view"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		} else

			return mapping.findForward("shippingFXViewPage");
	}

}
