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
 * AdminSiteAction.java
 * @author patrice.brenchereau@esrf.fr
 * August 18, 2008
 */

package ispyb.client.common.admin;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.server.common.services.admin.AdminActivity3Service;
import ispyb.server.common.services.admin.AdminVar3Service;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.admin.AdminActivity3VO;
import ispyb.server.common.vos.admin.AdminVar3VO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
 * @struts.action name="adminSiteForm" path="/manager/adminSite" input="manager.admin.site.page"
 *                type="ispyb.client.common.admin.AdminSiteAction" validate="false" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="successMessage" path="manager.admin.message.page"
 * 
 * @struts.action-forward name="successUser" path="manager.admin.user.page"
 * @struts.action-forward name="successUserBlom" path="blom.admin.user.page"
 * 
 * @struts.action-forward name="successChart" path="manager.admin.chart.page"
 * @struts.action-forward name="successChartBlom" path="blom.admin.chart.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 * @struts.action-forward name="viewAdminActionManager" path="manager.admin.site.page"
 * @struts.action-forward name="viewAdminActionLocalContact" path="localcontact.admin.site.page"
 * @struts.action-forward name="viewAdminActionBlom" path="blom.admin.site.page"
 * 
 */

public class AdminSiteAction extends DispatchAction {

	protected Proposal3Service proposalService;

	protected Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private AdminVar3Service adminVarService;

	private AdminActivity3Service adminActivityService;

	private final static Logger LOG = Logger.getLogger(AdminSiteAction.class);

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {

		this.proposalService = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
		this.adminVarService = (AdminVar3Service) ejb3ServiceLocator.getLocalService(AdminVar3Service.class);
		this.adminActivityService = (AdminActivity3Service) ejb3ServiceLocator.getLocalService(AdminActivity3Service.class);
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
		LOG.debug("Admin site administration");
		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();

		if (role.equals(Constants.ROLE_MANAGER)) {
			return mapping.findForward("viewAdminActionManager");
		} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
			return mapping.findForward("viewAdminActionLocalContact");
		} else if (role.equals(Constants.ROLE_BLOM)) {
			return mapping.findForward("viewAdminActionBlom");
		}
		return null;
	}

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayMessages(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		LOG.debug("Admin messages");
		ActionMessages errors = new ActionMessages();

		String warningMessage = "unknown var";
		String infoMessage = "unknown var";

		try {

			List<AdminVar3VO> var;

			// Warning message
			var = adminVarService.findByName(Constants.MESSAGE_WARNING);
			if (var.size() != 0 && var.get(0) != null) {
				warningMessage = var.get(0).getValue();
			} else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Unknown var "
						+ Constants.MESSAGE_WARNING));
				saveErrors(request, errors);
			}

			// Welcome message
			var = adminVarService.findByName(Constants.MESSAGE_INFO);
			if (var.size() != 0 && var.get(0) != null) {
				infoMessage = var.get(0).getValue();
			} else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Unknown var " + Constants.MESSAGE_INFO));
				saveErrors(request, errors);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}

		// Set form values
		AdminSiteForm myForm = (AdminSiteForm) actForm;
		myForm.setWarningMessage(warningMessage);
		myForm.setInfoMessage(infoMessage);

		return (mapping.findForward("successMessage"));
	}

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward submitMessages(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		LOG.debug("submitMessages");
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();

		AdminSiteForm myForm = (AdminSiteForm) actForm;
		String warningMessage = myForm.getWarningMessage();
		String welcomeMessage = myForm.getInfoMessage();

		// LOG.debug("Warning message = "+warningMessage);
		// LOG.debug("Welcome message = "+welcomeMessage);

		try {
			List<AdminVar3VO> var;
			AdminVar3VO myVar;

			// Warning message
			var = adminVarService.findByName(Constants.MESSAGE_WARNING);
			if (var.size() != 0 && var.get(0) != null) {
				myVar = var.get(0);
				myVar.setValue(warningMessage);
				adminVarService.update(myVar);
			} else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Unknown var "
						+ Constants.MESSAGE_WARNING));
				saveErrors(request, errors);
			}

			// Welcome message
			var = adminVarService.findByName(Constants.MESSAGE_INFO);
			if (var.size() != 0 && var.get(0) != null) {
				myVar = var.get(0);
				myVar.setValue(welcomeMessage);
				adminVarService.update(myVar);
			} else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Unknown var " + Constants.MESSAGE_INFO));
				saveErrors(request, errors);
			}

		} catch (Exception e) {
			e.printStackTrace();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}

		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.update"));
		saveMessages(request, messages);
		return (mapping.findForward("successMessage"));
	}

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayOnlineUsers(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		LOG.debug("Admin users");

		ActionMessages errors = new ActionMessages();

		try {
			AdminSiteForm form = (AdminSiteForm) actForm;

			// Retrieve LOGON users from DB
			List<AdminActivity3VO> userListDb1 = adminActivityService.findByAction(Constants.STATUS_LOGON);

			// Select online users
			long lastAccessTime1 = System.currentTimeMillis() - Constants.USER_ONLINE_MIN * 60 * 1000;
			List<AdminActivity3VO> userList1 = new ArrayList<AdminActivity3VO>();
			for (int i = 0; i < userListDb1.size(); i++) {
				AdminActivity3VO user = userListDb1.get(i);
				Date dateTime = user.getDateTime();
				long accessTime = dateTime.getTime();
				if (accessTime > lastAccessTime1) {
					userList1.add(user);
				}
			}
			// Populate with Info
			form.setListInfo1(userList1);

			// Retrieve users from DB
			List<AdminActivity3VO> userListDb2 = adminActivityService.findAll();

			// Select logons of the day
			Date now = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(now);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			long lastAccessTime2 = cal.getTimeInMillis();
			// System.out.println("Time: "+lastAccessTime2+" : "+new
			// Date(lastAccessTime2)+"/"+System.currentTimeMillis());
			ArrayList<AdminActivity3VO> userList2 = new ArrayList<AdminActivity3VO>();
			for (int i = 0; i < userListDb2.size(); i++) {
				AdminActivity3VO user = userListDb2.get(i);
				Date dateTime = user.getDateTime();
				long accessTime = dateTime.getTime();
				if (accessTime > lastAccessTime2) {
					userList2.add(user);
				}
			}
			// Populate with Info
			form.setListInfo2(userList2);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();
		if (role.equals(Constants.ROLE_MANAGER)) {
			return mapping.findForward("successUser");
		} else if (role.equals(Constants.ROLE_BLOM)) {
			return mapping.findForward("successUserBlom");
		}
		return mapping.findForward("successUser");
	}

	/**
	 * Display a chart based on menu action Example: /manager/adminSite.do ?reqCode=displayChart &chartView=v_logonByWeek //name of the
	 * view in the database that gives the chart data &chartType=bar // column, bar, area, line, stacked column, stacked area, pie,...
	 * &chartTitle=Number of logons per week // chart title to be displayed on the chart
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displayChart(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		String chartView = request.getParameter(Constants.CHART_VIEW);
		String chartType = request.getParameter(Constants.CHART_TYPE);
		String chartTitle = request.getParameter(Constants.CHART_TITLE);
		String chartUnit = request.getParameter(Constants.CHART_UNIT);

		if (chartType == null)
			chartType = "";
		if (chartTitle == null)
			chartTitle = "";
		if (chartUnit == null)
			chartUnit = "";

		// Parse chart types (delimiter is +)
		String patternStr = ",";
		String[] chartTypes = chartType.split(patternStr);

		LOG.debug("Admin chart: " + chartView + " (" + chartType + ")");

		ActionMessages errors = new ActionMessages();

		try {
			AdminSiteForm form = (AdminSiteForm) actForm;
			AdminChart myReport = new AdminChart();

			// TODO select only a view
			String fileUrl = myReport.getReport(request, chartView, chartTypes, chartTitle, chartUnit, Constants.CHART_TEMP_DIR);
			if (fileUrl == null || fileUrl.equals("")) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail",
						"No Report File, it could be a connexion problem"));
				saveErrors(request, errors);
				return mapping.findForward("error");
			}
			form.setReportDataUrl(fileUrl);
			form.setChartTitle(chartTitle);
		} catch (Exception e) {
			e.printStackTrace();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}

		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();
		if (role.equals(Constants.ROLE_MANAGER)) {
			return mapping.findForward("successChart");
		} else if (role.equals(Constants.ROLE_BLOM)) {
			return mapping.findForward("successChartBlom");
		}
		return (mapping.findForward("successChart"));
	}

}
