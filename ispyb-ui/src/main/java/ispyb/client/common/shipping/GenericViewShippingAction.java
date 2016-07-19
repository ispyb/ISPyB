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

import ispyb.client.security.roles.RoleDO;
import ispyb.client.common.util.QueryBuilder;
import ispyb.common.util.Constants;
import ispyb.common.util.IspybDateUtils;
import ispyb.common.util.StringUtils;
import ispyb.server.common.services.shipping.Shipping3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.shipping.Shipping3VO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

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
 * @struts.action name="genericViewShippingForm" path="/reader/genericShippingAction"
 *                type="ispyb.client.common.shipping.GenericViewShippingAction" validate="false" parameter="reqCode"
 *                scope="request"
 * 
 * @struts.action-forward name="shippingUserViewPage" path="user.shipping.view.page"
 * @struts.action-forward name="shippingStoreViewPage" path="store.shipping.view.page"
 * @struts.action-forward name="shippingBlomViewPage" path="blom.shipping.view.page"
 * @struts.action-forward name="shippingFedexmanagerViewPage" path="fedexmanager.shipping.view.page"
 * @struts.action-forward name="shippingLocalcontactViewPage" path="localcontact.shipping.view.page"
 * @struts.action-forward name="shippingManagerViewPage" path="manager.shipping.view.page"
 * 
 * @struts.action-forward name="shippingUserSearchPage" path="user.shipping.page"
 * @struts.action-forward name="shippingStoreSearchPage" path="store.shipping.page"
 * @struts.action-forward name="shippingBlomSearchPage" path="blom.shipping.page"
 * @struts.action-forward name="shippingFedexmanagerSearchPage" path="fedexmanager.shipping.page"
 * @struts.action-forward name="shippingLocalcontactSearchPage" path="localcontact.shipping.page"
 * @struts.action-forward name="shippingManagerSearchPage" path="manager.shipping.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 */

public class GenericViewShippingAction extends DispatchAction {
	private final Logger LOG = Logger.getLogger(GenericViewShippingAction.class);

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Shipping3Service shipping3Service;

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() {
		try {
			this.shipping3Service = (Shipping3Service) ejb3ServiceLocator.getLocalService(Shipping3Service.class);

		} catch (NamingException e) {
			LOG.error(e);
		}
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
	 * @param in_reponse
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		LOG.info("Display viewshippingaction");
		ActionMessages errors = new ActionMessages();
		try {
			GenericViewShippingForm form = (GenericViewShippingForm) actForm;

			// retrieves role from session
			RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
			String role = roleObject.getName();
			form.setRole(role);
			form.setUserOrIndus(false);

			// retrieves shippings list filtered according to the role permission
			List<Shipping3VO> listShippings = new ArrayList<Shipping3VO>();
			LOG.info("Role: " + role);

			String shipmentMsg = "";
			// All of them (last 30 days only)

			if (role.equals(Constants.ROLE_STORE)) {
				Date today = new Date();
				Date queryDate = IspybDateUtils.rollDateByDay(today, -30);
				// status = send to esrf
				listShippings = this.shipping3Service.findByStatus(Constants.SHIPPING_STATUS_SENT_TO_ESRF, queryDate, true);
				// + status = at esrf
				listShippings.addAll(this.shipping3Service.findByStatus(Constants.SHIPPING_STATUS_AT_ESRF, queryDate, true));
				shipmentMsg = "Shipments with the status '" + Constants.SHIPPING_STATUS_SENT_TO_ESRF + "' or '"
						+ Constants.SHIPPING_STATUS_AT_ESRF + "'";
			} else if (role.equals(Constants.ROLE_BLOM) || role.equals(Constants.FXMANAGE_ROLE_NAME)
					|| role.equals(Constants.ROLE_MANAGER)) {
				
				GregorianCalendar cal = new GregorianCalendar();
				cal.add(Calendar.DATE, -30);
				java.sql.Date firstDate = new java.sql.Date(cal.getTimeInMillis());
				listShippings = this.shipping3Service.findByCreationDate(firstDate, true);

				shipmentMsg = "Search on the last 30 days only";
			} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
				String beamlineOperatorSiteNumber = (String) request.getSession().getAttribute(
						Constants.LDAP_siteNumber);
				LOG.debug("Get shipments for LocalContact '" + beamlineOperatorSiteNumber + "'");
				listShippings = this.shipping3Service.findByBeamLineOperator(beamlineOperatorSiteNumber, true);
			} else if (role.equals(Constants.ROLE_USER) || role.equals(Constants.ROLE_INDUSTRIAL)
					|| (role.equals(Constants.ROLE_ADMIN) && Constants.SITE_IS_DLS())) {
				Integer userProposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
				listShippings = this.shipping3Service.findByProposal(userProposalId, true);
				form.setUserOrIndus(true);
			}
			if (listShippings == null) {
				listShippings = new ArrayList<Shipping3VO>();
			}
			// load dewar
			// and count nb dewarHistory & samples
			List<Integer> listNbSamplesPerShipping = new ArrayList<Integer>();
			List<Integer> listNbDewarHistoryPerShipping = new ArrayList<Integer>();
			// int i=0;
			for (Iterator<Shipping3VO> s = listShippings.iterator(); s.hasNext();) {
				Shipping3VO shipping = s.next();
				// shipping = shipping3Service.loadEager(shipping);
				// listShippings.set(i, shipping);
				// i++;
				Integer nbSample = 0;
				Integer nbDewarHistory = 0;
				Integer[] tab = shipping3Service.countShippingInfo(shipping.getShippingId());
				nbDewarHistory = tab[0];
				nbSample = tab[1];
				listNbSamplesPerShipping.add(nbSample);
				listNbDewarHistoryPerShipping.add(nbDewarHistory);
			}
			form.setListNbDewarHistoryPerShipping(listNbDewarHistoryPerShipping);
			form.setListNbSamplesPerShipping(listNbSamplesPerShipping);
			form.setListShippings(listShippings);
			form.setShipmentMsg(shipmentMsg);

			return redirectPageFromRole(role, mapping, request);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			e.printStackTrace();
			return mapping.findForward("error");
		}
	}

	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward initSearch(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();
		try {
			GenericViewShippingForm form = (GenericViewShippingForm) actForm;

			// retrieves role from session -----------------------------------
			RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
			String role = roleObject.getName();
			form.setRole(role);

			// return mapping.findForward("shippingSearchPage");

			try {
				if (role.equals(Constants.ROLE_STORE)) {
					return mapping.findForward("shippingStoreSearchPage");
				} else if (role.equals(Constants.ROLE_BLOM)) {
					return mapping.findForward("shippingBlomSearchPage");
				} else if (role.equals(Constants.FXMANAGE_ROLE_NAME)) {
					return mapping.findForward("shippingFedexmanagerSearchPage");
				} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
					return mapping.findForward("shippingLocalcontactSearchPage");
				} else if (role.equals(Constants.ROLE_USER) || role.equals(Constants.ROLE_INDUSTRIAL)) {
					return mapping.findForward("shippingUserSearchPage");
				} else if (role.equals(Constants.ROLE_MANAGER)) {
					return mapping.findForward("shippingManagerSearchPage");
				} else {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Role (" + role
							+ ") not found"));
					LOG.error("Role (" + role + ") not found");
					saveErrors(request, errors);
					return mapping.findForward("error");
				}
			} catch (Exception e) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
				LOG.error(e.toString());
				saveErrors(request, errors);
				return mapping.findForward("error");
			}

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
	}

	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward search(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		LOG.info("Search GenericViewShippingAction");
		ActionMessages errors = new ActionMessages();
		try {
			GenericViewShippingForm form = (GenericViewShippingForm) actForm;

			// retrieves role from session -----------------------------------
			RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
			String role = roleObject.getName();
			form.setRole(role);

			// Build Query --------------------------------------------------
			List<Shipping3VO> listShippings = new ArrayList<Shipping3VO>();

			Integer mProposalId = null;

			if (role.equals(Constants.ROLE_USER)) {
				// USER MUST NOT SEE OTHERS SHIPMENTS !!!
				mProposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			}

			String operatorSiteNumber = null;
			if (role.equals(Constants.ROLE_LOCALCONTACT)) {
				operatorSiteNumber = (String) request.getSession().getAttribute(Constants.LDAP_siteNumber);
				LOG.debug("Get shipments for LocalContact '" + operatorSiteNumber + "'");

			}
			String proposalCodeNumber = form.getProposalCodeNumber();
			String proposalCode = proposalCodeNumber;
			if (proposalCodeNumber != null && !proposalCodeNumber.isEmpty()) {
				ArrayList<String> authenticationInfo = StringUtils.GetProposalNumberAndCode(proposalCodeNumber);
				proposalCode = authenticationInfo.get(0);
				proposalCodeNumber = authenticationInfo.get(2);
			}
			String shippingName = form.getShippingName();
			if (shippingName != null) {
				shippingName = shippingName.replace('*', '%');
			}
			String mainProposerName = form.getMainProposer();
			if (mainProposerName != null) {
				mainProposerName = mainProposerName.replace('*', '%');
			}
			listShippings = this.shipping3Service.findByCustomQuery(mProposalId, shippingName, proposalCode,
					proposalCodeNumber, mainProposerName, QueryBuilder.toDate(form.getCreationDateStart()),
					QueryBuilder.toDate(form.getCreationDateEnd()), operatorSiteNumber);
			// load dewar
			// and count nb dewarHistory & samples
			List<Integer> listNbSamplesPerShipping = new ArrayList<Integer>();
			List<Integer> listNbDewarHistoryPerShipping = new ArrayList<Integer>();
			int i = 0;
			for (Iterator<Shipping3VO> s = listShippings.iterator(); s.hasNext();) {
				Shipping3VO shipping = s.next();
				shipping = shipping3Service.loadEager(shipping);
				listShippings.set(i, shipping);
				i++;
				Integer nbSample = 0;
				Integer nbDewarHistory = 0;
				Integer[] tab = shipping3Service.countShippingInfo(shipping.getShippingId());
				nbDewarHistory = tab[0];
				nbSample = tab[1];
				listNbSamplesPerShipping.add(nbSample);
				listNbDewarHistoryPerShipping.add(nbDewarHistory);
			}
			form.setListNbDewarHistoryPerShipping(listNbDewarHistoryPerShipping);
			form.setListNbSamplesPerShipping(listNbSamplesPerShipping);
			form.setListShippings(listShippings);
			return redirectPageFromRole(role, mapping, request);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
	}

	private ActionForward redirectPageFromRole(String role, ActionMapping mapping, HttpServletRequest request) {
		// redirection page according to the Role ------------------------

		ActionMessages errors = new ActionMessages();

		if (role.equals(Constants.ROLE_STORE)) {
			return mapping.findForward("shippingStoreViewPage");
		} else if (role.equals(Constants.ROLE_BLOM)) {
			return mapping.findForward("shippingBlomViewPage");
		} else if (role.equals(Constants.FXMANAGE_ROLE_NAME)) {
			return mapping.findForward("shippingFedexmanagerViewPage");
		} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
			return mapping.findForward("shippingLocalcontactViewPage");
		} else if (role.equals(Constants.ROLE_USER) || role.equals(Constants.ROLE_INDUSTRIAL)) {
			return mapping.findForward("shippingUserViewPage");
		} else if (role.equals(Constants.ROLE_MANAGER)) {
			return mapping.findForward("shippingManagerViewPage");
		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Role (" + role
					+ ") not found"));
			LOG.error("Role (" + role + ") not found");
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
	}
}
