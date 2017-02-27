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
 * ViewSampleAction.java
 */

package ispyb.client.common.shipping;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.common.util.Constants;
import ispyb.common.util.Formatter;
import ispyb.common.util.PropertyLoader;
import ispyb.server.common.services.shipping.Shipping3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.vos.collections.DataCollection3VO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
 * @struts.action name="viewShippingForm" path="/user/viewShippingAction" type="ispyb.client.common.shipping.ViewShippingAction"
 *                validate="false" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="shippingViewPage" path="/reader/genericShippingAction.do?reqCode=display"
 * @struts.action-forward name="shippingFXViewPage" path="fedexmanager.fxshipping.view.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 * 
 */
public class ViewShippingAction extends DispatchAction {
	private final Logger LOG = Logger.getLogger(ViewShippingAction.class);

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Shipping3Service shippingService;

	private DataCollection3Service dcService;

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws CreateException, NamingException {

		this.shippingService = (Shipping3Service) ejb3ServiceLocator.getLocalService(Shipping3Service.class);
		this.dcService = (DataCollection3Service) ejb3ServiceLocator.getLocalService(DataCollection3Service.class);

	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	/**
	 * To display all the Shipments for fedexManager.
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward displayAllFX(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();
		try {

			ViewShippingForm form = (ViewShippingForm) actForm;
			form.setMachineRunId(new Integer(1));

			List<Shipping3VO> shipList = this.shippingService.findByProposalCode(Constants.PROPOSAL_CODE_FX, true);

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
			return mapping.findForward("shippingFXViewPage");
	}

	/**
	 * To display the Shipments for fedexManager by run.
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward displayFXByRun(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();
		try {

			// Get the form
			ViewShippingForm form = (ViewShippingForm) actForm;

			// Get the range of dates corresponding to the selected machine run
			java.sql.Date[] runDates = getDatesFromRun(form.getMachineRunId());
			List<Shipping3VO> shipList = null;
			if (runDates != null && runDates.length > 0) {
				// Retrieve shipments from DB
				// old shipList = (List) _shippings.findByProposalCodeAndDates(Constants.PROPOSAL_CODE_FX, runDates[0],
				// runDates[1]);
				shipList = this.shippingService.findByProposalCodeAndDates(Constants.PROPOSAL_CODE_FX, runDates[0], runDates[1]);
			}

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
			return mapping.findForward("shippingFXViewPage");
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

			// Delete
			List<DataCollection3VO> dcs = dcService.findByShippingId(shippingId);
			// TODO check with bioSaxs
			if (dcs == null || dcs.isEmpty()) {
				shippingService.deleteByPk(shippingId);
			} else {
				throw new Exception("Can not delete shipment because datacollections are attached");
			}

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return mapping.findForward("shippingViewPage");
	}

	/**
	 * Parse a machine run id and return the corresponding dates range
	 * 
	 * @param machineRunId
	 * @return
	 */
	private java.sql.Date[] getDatesFromRun(Integer machineRunId) {
		if (machineRunId == null)
			return null;

		try {
			ArrayList<String> runEndDates = getRunEndDates();
			if (runEndDates == null || runEndDates.size() == 0)
				return null;

			java.sql.Date[] dates = new java.sql.Date[2];
			SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT_DDMMYYYY);
			int thisYear = Formatter.getYear();
			int yearForStartDate = thisYear;
			int yearForEndDate = thisYear;
			for (int i = 0; i < runEndDates.size(); i++) {
				if ((machineRunId.intValue() - 1) == i) {
					String dayMonthForStartDate = "";
					String dayMonthForEndDate = runEndDates.get(i);

					if (i == 0) {
						dayMonthForStartDate = runEndDates.get(runEndDates.size() - 1);
						yearForStartDate = thisYear - 1;
					} else {
						dayMonthForStartDate = runEndDates.get(i - 1);
						yearForStartDate = thisYear;
					}

					if ((dayMonthForStartDate == null || dayMonthForStartDate.length() == 0)
							|| (dayMonthForEndDate == null || dayMonthForEndDate.length() == 0))
						continue;

					dates[0] = new java.sql.Date(df.parse(dayMonthForStartDate + "/" + yearForStartDate).getTime());
					dates[1] = new java.sql.Date(df.parse(dayMonthForEndDate + "/" + yearForEndDate).getTime());
					break;
				}
			}
			return dates;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Get the list of the machine run end dates in properties
	 * 
	 * @return a Map
	 */
	private ArrayList<String> getRunEndDates() {
		Properties ispybProp = PropertyLoader.loadProperties("ISPyB");
		if (ispybProp == null || ispybProp.size() == 0)
			return null;

		ArrayList<String> runEndDates = new ArrayList<String>();
		try {
			boolean finished = false;
			int index = 1;
			while (!finished) {
				String currentProp = ispybProp.getProperty("end.run." + index);
				if (currentProp == null || currentProp.length() == 0)
					finished = true;
				else
					runEndDates.add(currentProp);
				index++;
			}
		} catch (Exception ex) {
			LOG.error(ex.toString());
			return null;
		}

		if (runEndDates == null || runEndDates.size() == 0)
			return null;

		return runEndDates;
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
