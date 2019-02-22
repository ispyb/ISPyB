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
import ispyb.common.util.Constants;
import ispyb.common.util.IspybDateUtils;
import ispyb.server.common.services.shipping.Dewar3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.shipping.Dewar3VO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
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
 * @struts.action name="genericViewDewarForm" path="/reader/genericDewarAction"
 *                type="ispyb.client.common.shipping.GenericViewDewarAction" validate="false" parameter="reqCode"
 *                scope="request"
 * 
 * @struts.action-forward name="dewarUserViewPage" path="user.dewar.view.page"
 * @struts.action-forward name="dewarStoreViewPage" path="store.dewar.view.page"
 * @struts.action-forward name="dewarBlomViewPage" path="blom.dewar.view.page"
 * @struts.action-forward name="dewarFedexmanagerViewPage" path="fedexmanager.dewar.view.page"
 * @struts.action-forward name="dewarLocalcontactViewPage" path="localcontact.dewar.view.page"
 * @struts.action-forward name="dewarManagerViewPage" path="manager.dewar.view.page"
 * 
 * @struts.action-forward name="dewarUserSearchPage" path="user.shipping.dewar.page"
 * @struts.action-forward name="dewarStoreSearchPage" path="store.shipping.dewar.page"
 * @struts.action-forward name="dewarBlomSearchPage" path="blom.shipping.dewar.page"
 * @struts.action-forward name="dewarFedexmanagerSearchPage" path="fedexmanager.shipping.dewar.page"
 * @struts.action-forward name="dewarLocalcontactSearchPage" path="localcontact.shipping.dewar.page"
 * @struts.action-forward name="dewarManagerSearchPage" path="manager.shipping.dewar.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 */

public class GenericViewDewarAction extends DispatchAction {

	private final Logger LOG = Logger.getLogger(GenericViewDewarAction.class);
	
	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Dewar3Service dewarService;

	private void initServices() throws CreateException, NamingException {
		this.dewarService = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);
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

		ActionMessages errors = new ActionMessages();
		try {
			GenericViewDewarForm form = (GenericViewDewarForm) actForm;

			// retrieves role from session
			RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
			String role = roleObject.getName();
			form.setRole(role);
			form.setUserOrIndus(false);

			// retrieves dewars list filtered according to the role permission

			List<Dewar3VO> listDewars = null;
			String dewarMsg = "";

			if (role.equals(Constants.ROLE_STORE)) {
				// dates = last 2 months
				SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
				Date today = new Date();
				Date date1 = IspybDateUtils.rollDateByDay(today, -60);

				// status = send to esrf + status = at esrf
				listDewars = dewarService.findFiltered(null, null, null, null, null, date1, null,
						Constants.SHIPPING_STATUS_SENT_TO_ESRF, null, true, false);
				List<Dewar3VO> dewarTab2 = dewarService.findFiltered(null, null, null, null, null, date1, null,
						Constants.SHIPPING_STATUS_AT_ESRF, null, true, false);
				dewarMsg = "Dewars with the status '" + Constants.SHIPPING_STATUS_SENT_TO_ESRF + "' or '"
						+ Constants.SHIPPING_STATUS_AT_ESRF + "'";
				listDewars.addAll(dewarTab2);

			} else if (role.equals(Constants.ROLE_BLOM) || role.equals(Constants.FXMANAGE_ROLE_NAME)
					|| role.equals(Constants.ROLE_MANAGER)) {
				// All of them (last 30 days only)
				GregorianCalendar cal = new GregorianCalendar();
				cal.add(Calendar.DATE, -30);
				java.sql.Date firstDate = new java.sql.Date(cal.getTimeInMillis());
				listDewars = dewarService.findByDateWithHistory(firstDate);
				// listDewars = dewarService.findFiltered(null, null, null, null, null, firstDate, null, null, null,
				// true,
				// false);

				dewarMsg = "Search on the last 30 days only.";

			} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
				String beamlineOperatorSiteNumber = (String) request.getSession().getAttribute(
						Constants.LDAP_siteNumber);
				// TODO
				LOG.debug("Get dewars for LocalContact '" + beamlineOperatorSiteNumber + "'");
				listDewars = dewarService.findByCustomQuery(null, null, null, null, null, null, null, null,
						beamlineOperatorSiteNumber);
				int i = 0;
				for (Iterator<Dewar3VO> iterator = listDewars.iterator(); iterator.hasNext();) {
					Dewar3VO dewar3vo = iterator.next();
					dewar3vo = dewarService.loadEager(dewar3vo);
					listDewars.set(i, dewar3vo);
					i++;
				}

			} else if (role.equals(Constants.ROLE_USER) || role.equals(Constants.ROLE_INDUSTRIAL)) {
				Integer userProposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
				listDewars = dewarService.findFiltered(userProposalId, null, null, null, null, null, null, null, null,
						true, false);

				form.setUserOrIndus(true);

			}
			// load history
			// and count nb samples
			List<Integer> listNbSamplesPerDewar = new ArrayList<Integer>();
			for (Iterator<Dewar3VO> iterator = listDewars.iterator(); iterator.hasNext();) {
				Dewar3VO dewar3vo = iterator.next();
				// dewar3vo = dewarService.loadEager(dewar3vo);
				// listDewars.set(i, dewar3vo);
				// i++;
				Integer nbSample = 0;
				nbSample = dewarService.countDewarSamples(dewar3vo.getDewarId());
				listNbSamplesPerDewar.add(nbSample);
			}
			form.setListDewars(listDewars);
			form.setListNbSamplesPerDewar(listNbSamplesPerDewar);
			form.setDewarMsg(dewarMsg);

			// redirection page
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
			GenericViewDewarForm form = (GenericViewDewarForm) actForm;

			// retrieves role from session -----------------------------------
			RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
			String role = roleObject.getName();
			form.setRole(role);

			// return mapping.findForward("dewarSearchPage");

			try {
				if (role.equals(Constants.ROLE_STORE)) {
					return mapping.findForward("dewarStoreSearchPage");
				} else if (role.equals(Constants.ROLE_BLOM)) {
					return mapping.findForward("dewarBlomSearchPage");
				} else if (role.equals(Constants.FXMANAGE_ROLE_NAME)) {
					return mapping.findForward("dewarFedexmanagerSearchPage");
				} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
					return mapping.findForward("dewarLocalcontactSearchPage");
				} else if (role.equals(Constants.ROLE_USER) || role.equals(Constants.ROLE_INDUSTRIAL)) {
					return mapping.findForward("dewarUserSearchPage");
				} else if (role.equals(Constants.ROLE_MANAGER)) {
					return mapping.findForward("dewarManagerSearchPage");
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
		ActionMessages errors = new ActionMessages();
		try {
			GenericViewDewarForm form = (GenericViewDewarForm) actForm;

			// retrieves role from session -----------------------------------
			RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
			String role = roleObject.getName();
			form.setRole(role);

			// Build Query --------------------------------------------------
			Dewar3Service dewarService = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);

			Integer mProposalId = null;

			if (role.equals(Constants.ROLE_USER)) {
				// USER MUST NOT SEE OTHERS DEWARS !!!
				mProposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			}

			String operatorSiteNumber = null;
			if (role.equals(Constants.ROLE_LOCALCONTACT)) {
				operatorSiteNumber = (String) request.getSession().getAttribute(Constants.LDAP_siteNumber);
				LOG.debug("Get shipments for LocalContact '" + operatorSiteNumber + "'");

			}

			SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
			Date today = new Date();
			Date queryDate = IspybDateUtils.rollDateByDay(today, -30);

			Date date1 = queryDate;
			if (form.getExperimentDateStart() != null && !form.getExperimentDateStart().isEmpty())
				date1 = df.parse(form.getExperimentDateStart());
			Date date2 = null;
			if (form.getExperimentDateEnd() != null && !form.getExperimentDateEnd().isEmpty())
				date2 = df.parse(form.getExperimentDateEnd());
			String label = form.getDewarName();
			if (label != null)
				label = label.replace('*', '%');
			String comments = form.getComments();
			if (comments != null)
				comments = comments.replace('*', '%');
			String barCode = form.getBarCode();
			if (barCode != null)
				barCode = barCode.replace('*', '%');
			List<Dewar3VO> listDewars = dewarService.findFiltered(mProposalId, null, null, label, barCode,
					comments, date1, date2, form.getDewarStatus(),
					form.getStorageLocation(), null, null, false, true, false);

			// listDewars = dewarService.findByCustomQuery(mProposalId, form.getDewarName(), form.getComments(),
			// form.getBarCode(), form.getDewarStatus(), form.getStorageLocation(), date1, date2,
			// beamlineOperatorNameSqlLike);

			// we need the dewarHistory
			// load history
			// and count nb samples
			List<Integer> listNbSamplesPerDewar = new ArrayList<Integer>();
			for (Iterator<Dewar3VO> iterator = listDewars.iterator(); iterator.hasNext();) {
				Dewar3VO dewar3vo = iterator.next();
				// dewar3vo = dewarService.loadEager(dewar3vo);
				// listDewars.set(i, dewar3vo);
				// i++;
				Integer nbSample = 0;
				nbSample = dewarService.countDewarSamples(dewar3vo.getDewarId());
				listNbSamplesPerDewar.add(nbSample);
			}
			form.setListDewars(listDewars);
			form.setListNbSamplesPerDewar(listNbSamplesPerDewar);

			// redirection page according to the Role ------------------------
			return redirectPageFromRole(role, mapping, request);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}

	}

	/**
	 * Sub menu for the Stores
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward toBeReceived(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();
		try {
			GenericViewDewarForm form = (GenericViewDewarForm) actForm;

			// retrieves role from session -----------------------------------
			RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
			String role = roleObject.getName();
			form.setRole(role);

			// Query
			SimpleDateFormat dateStandard = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
			Date today = new Date();
			Date querydate = IspybDateUtils.rollDateByDay(today, -30);
			String dateStr = dateStandard.format(querydate);
			form.setExperimentDateStart(dateStr);

			form.setDewarStatus(Constants.SHIPPING_STATUS_SENT_TO_ESRF);
			form.setStorageLocation(Constants.LOCATION_EMPTY);

			// Search
			return this.search(mapping, actForm, request, in_reponse);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
	}

	/**
	 * Sub menu for the Stores
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward toBeDelivered(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();
		try {
			GenericViewDewarForm form = (GenericViewDewarForm) actForm;

			// retrieves role from session -----------------------------------
			RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
			String role = roleObject.getName();
			form.setRole(role);
			SimpleDateFormat dateStandard = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
			Date today = new Date();
			Date last2Months = IspybDateUtils.rollDateByDay(today, -60);
			String dateStr = dateStandard.format(last2Months);

			form.setExperimentDateStart(dateStr);

			form.setStorageLocation(Constants.DEWAR_STORES_IN);

			// Search
			return this.search(mapping, actForm, request, in_reponse);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
	}

	/**
	 * Sub menu for the Stores
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward toBePickedUp(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();
		try {
			GenericViewDewarForm form = (GenericViewDewarForm) actForm;

			// retrieves role from session -----------------------------------
			RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
			String role = roleObject.getName();
			form.setRole(role);

			// Query
			SimpleDateFormat dateStandard = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
			Date yesterday = new Date();
			yesterday.setDate(yesterday.getDate() - 1);

			String yesterdayStr = dateStandard.format(yesterday);
			form.setExperimentDateEnd(yesterdayStr);

			form.setDewarStatus(Constants.SHIPPING_STATUS_AT_ESRF);
			form.setStorageLocation(Constants.NOT_AT_STORES);

			// Search
			return this.search(mapping, actForm, request, in_reponse);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
	}

	/**
	 * Sub menu for the Stores
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward sentToUser(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();
		try {
			GenericViewDewarForm form = (GenericViewDewarForm) actForm;

			// retrieves role from session -----------------------------------
			RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
			String role = roleObject.getName();
			form.setRole(role);

			// Query
			SimpleDateFormat dateStandard = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
			Date today = new Date();
			String dateStr = dateStandard.format(today);
			form.setExperimentDateEnd(dateStr);

			form.setDewarStatus(Constants.SHIPPING_STATUS_SENT_TO_USER);

			// Search
			return this.search(mapping, actForm, request, in_reponse);

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

		try {
			if (role.equals(Constants.ROLE_STORE)) {
				return mapping.findForward("dewarStoreViewPage");
			} else if (role.equals(Constants.ROLE_BLOM)) {
				return mapping.findForward("dewarBlomViewPage");
			} else if (role.equals(Constants.FXMANAGE_ROLE_NAME)) {
				return mapping.findForward("dewarFedexmanagerViewPage");
			} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
				return mapping.findForward("dewarLocalcontactViewPage");
			} else if (role.equals(Constants.ROLE_USER) || role.equals(Constants.ROLE_INDUSTRIAL)) {
				return mapping.findForward("dewarUserViewPage");
			} else if (role.equals(Constants.ROLE_MANAGER)) {
				return mapping.findForward("dewarManagerViewPage");
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
	}
}
