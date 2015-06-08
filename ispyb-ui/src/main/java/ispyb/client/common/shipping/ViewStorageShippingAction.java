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
package ispyb.client.common.shipping;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.client.common.BreadCrumbsForm;
import ispyb.server.common.services.shipping.Shipping3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.shipping.Shipping3VO;

import java.sql.Date;
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
 * @struts.action name="viewShippingForm" path="/user/viewStorageShippingAction"
 *                type="ispyb.client.common.shipping.ViewStorageShippingAction" validate="false" parameter="reqCode"
 *                scope="request"
 * 
 * @struts.action-forward name="storageShippingViewPage" path="user.storageShipping.view.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 * 
 */

public class ViewStorageShippingAction extends ViewShippingAction {
	private final Logger LOG = Logger.getLogger(ViewStorageShippingAction.class);
	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
	private Shipping3Service shipping3Service;

	
	private void initServices() throws CreateException, NamingException {
		this.shipping3Service = (Shipping3Service) ejb3ServiceLocator.getLocalService(Shipping3Service.class);
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}
	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
//	@Override
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();
		Date dateOut;
		try {
			// Clean BreadCrumbsForm
			BreadCrumbsForm.deleteIt(request);

			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			ViewShippingForm form = (ViewShippingForm) actForm; // Parameters submited by form
			String shippingName = form.getShippingName();
			String shippingDateStart = form.getShippingDateStart();
			String shippingDateEnd = form.getShippingDateEnd();
			String deliveryDate = form.getDeliveryDate();

			// ---------------------------------------------------------------------------------------------------
			List<Shipping3VO> listInfo = this.shipping3Service.findByIsStorageShipping(true);
			// Populate with Info
			form.setListInfo(listInfo);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.EDIT_MODE);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return mapping.findForward("storageShippingViewPage");
	}

}
