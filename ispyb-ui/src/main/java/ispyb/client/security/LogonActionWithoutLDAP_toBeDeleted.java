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
package ispyb.client.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ispyb.client.common.menu.MenuContext;
import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.common.services.config.MenuGroup3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.config.MenuGroup3VO;
import ispyb.server.mx.services.autoproc.IspybAutoProcAttachment3Service;
import ispyb.server.mx.services.autoproc.SpaceGroup3Service;
import ispyb.server.mx.services.collections.IspybCrystalClass3Service;
import ispyb.server.mx.vos.autoproc.IspybAutoProcAttachment3VO;
import ispyb.server.mx.vos.autoproc.SpaceGroup3VO;
import ispyb.server.mx.vos.collections.IspybCrystalClass3VO;

/**
 * When this page is called the JBoss Authentification has been successfull
 * 
 * @struts.action path="/security/logonWithoutLDAP"
 * 
 * @struts.action-forward name="guestPage" path="/welcomeGuestPage.do"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 */
public class LogonActionWithoutLDAP_toBeDeleted extends Action {

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private MenuGroup3Service menuGroupService;

	private IspybAutoProcAttachment3Service ispybAutoProcAttachmentService;

	private IspybCrystalClass3Service ispybCrystalClassService;
	
	private SpaceGroup3Service spaceGroupService;

	private final Logger LOG = Logger.getLogger(LogonActionWithoutLDAP_toBeDeleted.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		LOG.info("Logon ");

		ActionMessages errors = new ActionMessages();

		try {
			// --- Log ---
			String userName = request.getUserPrincipal().toString();

			// EmployeeVO employee = LdapConnection.findByUniqueIdentifier(userName);
			// String userGivenName = employee.getGivenName() == null || employee.getGivenName().trim().equals("") ?
			// "no given name"
			// : employee.getGivenName();
			// String userLastName = employee.getSn() == null || employee.getSn().trim().equals("") ? "no last name"
			// : employee.getSn();
			// String userEmail = employee.getMail();
			// String userSiteNumber = employee.getSiteNumber();
			//
			// LOG.info("Logon: user = " + userName + " (" + userGivenName + " " + userLastName + ") at " + userEmail);

			String proposalCode = this.getProposalCode(userName);

			String proposalType = LogonUtils.getProposalType(userName);
			if (proposalType == null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail",
						"This proposal is not a valid proposal: " + userName));
				saveErrors(request, errors);
//				request.logout();
				return (mapping.findForward("error"));
			}

			LOG.debug("ProposalCode: " + proposalCode);
			LOG.debug("ProposalType: " + proposalType);

			// -----------
			HttpSession session = request.getSession();
			ArrayList<RoleDO> userRoles = getUserRoles(request);
			session.setAttribute(Constants.ROLES, userRoles);
			// session.setAttribute(Constants.LDAP_GivenName, userGivenName);
			// session.setAttribute(Constants.LDAP_LastName, userLastName);
			// session.setAttribute(Constants.LDAP_Email, userEmail);
			// session.setAttribute(Constants.LDAP_siteNumber, userSiteNumber);

			// -------------------------------
			// the list of crystal class is stored in session because it is fixed during the session time
			this.ispybCrystalClassService = (IspybCrystalClass3Service) ejb3ServiceLocator
					.getLocalService(IspybCrystalClass3Service.class);
			List<IspybCrystalClass3VO> listOfCrystalClass = ispybCrystalClassService.findAll();
			session.setAttribute(Constants.ISPYB_CRYSTAL_CLASS_LIST, listOfCrystalClass);

			// -------------
			// the list of autoproc attachment are loaded and store in session because they are used everywhere in
			// results.
			this.ispybAutoProcAttachmentService = (IspybAutoProcAttachment3Service) ejb3ServiceLocator
					.getLocalService(IspybAutoProcAttachment3Service.class);
			List<IspybAutoProcAttachment3VO> listOfAutoProcAttachment = ispybAutoProcAttachmentService.findAll();
			session.setAttribute(Constants.ISPYB_AUTOPROC_ATTACH_LIST, listOfAutoProcAttachment);
			
			// load the allowed space groups
			this.spaceGroupService = (SpaceGroup3Service) ejb3ServiceLocator.getLocalService(SpaceGroup3Service.class);
			List<SpaceGroup3VO> listOfSpaceGroup = this.spaceGroupService.findAllowedSpaceGroups();
			List<String> spaceGroups = new ArrayList<String>();
			if (listOfSpaceGroup != null ){
				for (Iterator<SpaceGroup3VO> iter = listOfSpaceGroup.iterator(); iter.hasNext();){
					SpaceGroup3VO sp = iter.next();
					spaceGroups.add(sp.getSpaceGroupShortName());
				}
			}
			session.setAttribute(Constants.ISPYB_ALLOWED_SPACEGROUPS_LIST, spaceGroups);

			LOG.debug("SessionId: " + session.getId());

			if (userRoles.size() > 1) {
				// forward to page where user can select which Role he want to use
				response.sendRedirect(request.getContextPath() + "/roleChoosePage.do");

			} else {
				// create the menu and
				// forward to Welcome page belonging to the unique role

				RoleDO currentRole = userRoles.get(0);
				session.setAttribute(Constants.CURRENT_ROLE, userRoles.get(0));

				if (proposalType.equals(Constants.PROPOSAL_MX_BX)) {
					// forward to page where user can select which experiment he wants to use
					response.sendRedirect(request.getContextPath() + "/userProposalTypeChoosePage.do");
				} else {
					MenuContext menu = new MenuContext(currentRole.getId(), proposalType);
					session.setAttribute(Constants.MENU, menu);

					// send redirect to welcome page of the unique role
					LOG.info("user role is : " + currentRole.getName());
					LOG.debug("server name is : " + request.getServerName());
					response.sendRedirect(request.getContextPath() + currentRole.getValue());
				}
			}

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			e.printStackTrace();
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		return (mapping.findForward("guestPage"));
	}

	private String getProposalCode(String userName) {
		ArrayList<String> authenticationInfo = StringUtils.GetProposalNumberAndCode(userName);
		String proposalCode = authenticationInfo.get(0);
		return proposalCode;
	}

	/**
	 * Get all Groups from DB
	 * 
	 * @return
	 */
	private ArrayList<RoleDO> getGroups() {

		try {
			this.menuGroupService = (MenuGroup3Service) ejb3ServiceLocator.getLocalService(MenuGroup3Service.class);
			List<MenuGroup3VO> groupList = menuGroupService.findAll(false);
			// ArrayList groupList = (ArrayList) menuGroup.findAll();

			Iterator<MenuGroup3VO> it = groupList.iterator();
			ArrayList<RoleDO> returnList = new ArrayList<RoleDO>();

			while (it.hasNext()) {
				RoleDO role = new RoleDO();
				MenuGroup3VO menuGroupValue = it.next();
				role.setName(menuGroupValue.getName());
				role.setValue(menuGroupValue.getWelcomePage());
				role.setId(menuGroupValue.getMenuGroupId().intValue());
				returnList.add(role);
			}
			return returnList;

		} catch (Exception e) {
			LOG.error(e.toString());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get All roles which this user belongs
	 * 
	 * @param request
	 * @return
	 */
	private ArrayList<RoleDO> getUserRoles(HttpServletRequest request) {

		ArrayList<RoleDO> groups = getGroups();
		Iterator<RoleDO> it = groups.iterator();

		ArrayList<RoleDO> userRoles = new ArrayList<RoleDO>();
		while (it.hasNext()) {
			RoleDO role = it.next();

			if (request.isUserInRole(role.getName())) {
				userRoles.add(role);

			}
		}
		return userRoles;
	}

}
