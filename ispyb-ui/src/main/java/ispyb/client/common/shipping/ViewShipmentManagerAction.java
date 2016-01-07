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
import ispyb.common.util.DBTools;
import ispyb.common.util.Constants;
import ispyb.server.common.services.shipping.Shipping3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.shipping.Shipping3VO;

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
import org.apache.struts.actions.DispatchAction;

/**
 * @struts.action name="viewShippingForm" path="/manager/viewShipmentManager" input="manager.welcome.page"
 *                validate="false" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="shippingFXViewPage" path="fedexmanager.fxshipping.view.page"
 * 
 * @struts.action-forward name="success" path="manager.shipping.view.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 * 
 */

public class ViewShipmentManagerAction extends DispatchAction {
	private final Logger LOG = Logger.getLogger(ViewShipmentManagerAction.class);
	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Shipping3Service shippingService;


	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws CreateException, NamingException {

		this.shippingService = (Shipping3Service) ejb3ServiceLocator.getLocalService(Shipping3Service.class);

	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	/**
	 * To display all the Shipments for Manager.
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward displayAll(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();
		try {
			ViewShippingForm form = (ViewShippingForm) actForm;
			form.setMachineRunId(new Integer(1));

			// Retrieve shipments from DB
			List shipList = shippingService.findAll();

			// Populate with Info
			form.setListInfo(shipList);
			FormUtils.setFormDisplayMode(request, actForm, FormUtils.EDIT_MODE);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.fedexmanager.shipping.view"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		} else
			return mapping.findForward("success");
	}

	/**
	 * Close the Shipping. Cannot be edited when closed
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward deleteShipping(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_response) {
		ActionMessages errors = new ActionMessages();
		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			Integer shippingId = new Integer(request.getParameter(Constants.SHIPPING_ID));

			Shipping3VO shipping = DBTools.getSelectedShipping(shippingId);

			// Delete
			shippingService.delete(shipping);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return this.displayAll(mapping, actForm, request, in_response);
	}

	/**
	 * Retrieves the delivery agent name regardless of how it was spelt in the DB
	 * 
	 * @param agentName
	 *            The agent name in the DB
	 * @param targetAgentName
	 *            The agent name to compare against
	 * @return the agentName if it's the expexted one. Dummy value otherwise
	 */
	public static String retrieveDeliveryAgentName(String agentName, String targetAgentName) {
		String deliveryAgentName = "#UNKNOWN#";
		if (agentName.compareToIgnoreCase(targetAgentName) == 0)
			deliveryAgentName = agentName;

		return deliveryAgentName;
	}
}
