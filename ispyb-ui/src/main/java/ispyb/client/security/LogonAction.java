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
 ****************************************************************************************************/
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
import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.common.services.config.MenuGroup3Service;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.config.MenuGroup3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.services.autoproc.IspybAutoProcAttachment3Service;
import ispyb.server.mx.services.autoproc.SpaceGroup3Service;
import ispyb.server.mx.services.collections.IspybCrystalClass3Service;
import ispyb.server.mx.services.collections.IspybReference3Service;
import ispyb.server.mx.vos.autoproc.IspybAutoProcAttachment3VO;
import ispyb.server.mx.vos.autoproc.SpaceGroup3VO;
import ispyb.server.mx.vos.collections.IspybCrystalClass3VO;
import ispyb.server.mx.vos.collections.IspybReference3VO;
import ispyb.server.security.DatabaseLoginModuleHelper;
import ispyb.server.security.EmployeeVO;
import ispyb.server.security.LdapConnection;

/**
 * When this page is called the JBoss Authentification has been successfull
 * 
 * @struts.action path="/security/logon"
 * 
 * @struts.action-forward name="guestPage" path="/welcomeGuestPage.do"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 * @struts.action-forward name="unauthorised" path="site.logon.error.page"
 * 
 */
public class LogonAction extends Action {

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private MenuGroup3Service menuGroupService;

	private IspybAutoProcAttachment3Service ispybAutoProcAttachmentService;

	private Proposal3Service proposalService;

	private IspybCrystalClass3Service ispybCrystalClassService;

	private IspybReference3Service ispybReferenceService;

	private SpaceGroup3Service spaceGroupService;

	private final Logger LOG = Logger.getLogger(LogonAction.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		LOG.info("Logon ");
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		String site = null;

		try {
			// --- Log ---
			String userName = request.getUserPrincipal().getName();

			String userGivenName = "";
			String userLastName = "";
			String userSiteNumber = "";
			
			/**************************************
			 * AUTH using properties files
			 **************************************/
			if (Constants.SITE_AUTHENTICATION_METHOD.toString().trim().toUpperCase().equals("SIMPLE") ) {
				
				String userRolesNames = "Roles for user logged in: ";

				// -----------
				HttpSession session = request.getSession();
				ArrayList<RoleDO> userRoles = getUserRoles(request);

				for (RoleDO roleDO : userRoles) {
					userRolesNames = userRolesNames + roleDO.getName() + roleDO.getValue() + ", ";
				}
				session.setAttribute(Constants.ROLES, userRoles);
				LOG.debug(userRolesNames);
			}

			/**************************************
			 * AUTH USING LDAP
			 **************************************/
			if (Constants.SITE_AUTHENTICATION_METHOD.toString().trim().toUpperCase().equals("LDAP") && !Constants.SITE_IS_MAXIV() && !Constants.SITE_IS_SOLEIL() && !Constants.SITE_IS_EMBL()) {
				EmployeeVO employee = LdapConnection.findByUniqueIdentifier(userName);
				userGivenName = employee.getGivenName() == null || employee.getGivenName().trim().equals("") ? "no given name"
						: employee.getGivenName();
				userLastName = employee.getSn() == null || employee.getSn().trim().equals("") ? "no last name" : employee.getSn();
				userSiteNumber = employee.getSiteNumber();
			}

			LOG.info(Constants.SITE_AUTHENTICATION_METHOD.toString() + " Logon: user = " + userName + " (" + userGivenName + " "
					+ userLastName + ")  ");

			HttpSession session = request.getSession();
			ArrayList<RoleDO> userRoles = getUserRoles(request);
			session.setAttribute(Constants.ROLES, userRoles);
			session.setAttribute(Constants.LDAP_GivenName, userGivenName);
			session.setAttribute(Constants.LDAP_LastName, userLastName);
			session.setAttribute(Constants.LDAP_siteNumber, userSiteNumber);
			
			/**************************************
			 * TODO: MX initialization to be moved
			 **************************************/
			this.initializeMX(session);

			LOG.debug("SessionId: " + session.getId());

			if (Constants.SITE_IS_SOLEIL()) {
				session.setAttribute(Constants.FULLNAME, DatabaseLoginModuleHelper.doGetUserFullName());
				LOG.info("fullname: " + session.getAttribute(Constants.FULLNAME));
			}

			// load the proposal list in session
			if (Constants.SITE_IS_ESRF()) {
				site = Constants.SITE_ESRF;
			}
			
			if (Constants.SITE_IS_MAXIV()) {
				site = Constants.SITE_MAXIV;
			}
			
			if (Constants.SITE_IS_SOLEIL()) {
				site = Constants.SITE_SOLEIL;
			}
			
			this.proposalService = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
			List<Proposal3VO> proposals = this.proposalService.findProposalByLoginName(userName, site);

			if (proposals == null || proposals.isEmpty()) {
				if (Constants.SITE_IS_ESRF() || Constants.SITE_IS_SOLEIL()) {
					// if the list of proposals is empty we try to update from SMIS
					proposals = getProposalsFromUserPortal(errors, messages, userName);
				}
			}
			session.setAttribute(Constants.PROPOSALS, proposals);

			if (userRoles.size() > 1) {
				// forward to page where user can select which Role he want to use
				response.sendRedirect(request.getContextPath() + "/roleChoosePage.do");

			} else {
				// create the menu and
				// forward to Welcome page belonging to the unique role
				// for a person it can be either WelcomePerson or WelcomeUser if only 1 proposal exists

				RoleDO currentRole = userRoles.get(0);
				session.setAttribute(Constants.CURRENT_ROLE, userRoles.get(0));

				// set the menu context
				MenuContext menu = (MenuContext) request.getSession().getAttribute(Constants.MENU);
				menu = new MenuContext(currentRole.getId(), null);
				session.setAttribute(Constants.MENU, menu);

				if (proposals == null || proposals.size() == 0) {
					// redirect to the page of the unique role
					// we are in the case different from a proposal
					/** A single role that is user and no proposals then it is unauthorised **/
					if (userRoles.get(0).getName().equals("User") && (userRoles.size() == 1)){
						return (mapping.findForward("unauthorised"));
					}
					else{
						response.sendRedirect(request.getContextPath() + currentRole.getValue());
					}

				} else if (proposals.size() == 1) {

					// there is only 1 proposal possible, forward directly to the page of the proposal
					session.setAttribute(Constants.PROPOSAL_CODE, proposals.get(0).getCode());
					session.setAttribute(Constants.PROPOSAL_NUMBER, proposals.get(0).getNumber());
					session.setAttribute(Constants.PROPOSAL_ID, proposals.get(0).getProposalId());

					if (proposals.get(0).getType().toString().equals(Constants.PROPOSAL_MX_BX)) {

						// redirect to proposals list because 1 proposal with several techniques
						response.sendRedirect(request.getContextPath() + "/user/welcomePerson.do");
					} else {
						// only 1 proposal with 1 technique
						menu = new MenuContext(currentRole.getId(), proposals.get(0).getType());
						session.setAttribute(Constants.MENU, menu);
						session.setAttribute(Constants.TECHNIQUE, proposals.get(0).getType());
						response.sendRedirect(request.getContextPath() + "/user/welcomeUser.do");
					}

				} else {

					// redirect to proposals list
					response.sendRedirect(request.getContextPath() + "/user/welcomePerson.do");

				}
				LOG.info("user role is : " + currentRole.getName());
				LOG.debug("server name is : " + request.getServerName());

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

	
//	private String getProposalCode(String userName) {
//		ArrayList<String> authenticationInfo = StringUtils.GetProposalNumberAndCode(userName);
//		String proposalCode = authenticationInfo.get(0);
//		return proposalCode;
//	}

	private List<Proposal3VO> getProposalsFromUserPortal(ActionMessages errors, ActionMessages messages, String loginName) throws Exception {
		// Case of proposal existing in ldap but not updated in ISPyB :launch updateProposal, then
		// retrieve proposalId

		String proposalCode = StringUtils.GetProposalNumberAndCode(loginName).get(0);
		String proposalNumber = StringUtils.GetProposalNumberAndCode(loginName).get(2);

		ispyb.client.common.proposal.UpdateProposalAndSessionAndProteinFromWS.updateThisProposal(errors, messages, proposalCode,
				proposalNumber);

		List<Proposal3VO> proposals = this.proposalService.findProposalByLoginName(loginName, Constants.SITE_ESRF);

		return proposals;

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

	private void initializeMX(HttpSession session) throws Exception {

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

		// load the references list
		this.ispybReferenceService = (IspybReference3Service) ejb3ServiceLocator.getLocalService(IspybReference3Service.class);
		List<IspybReference3VO> listOfReference = ispybReferenceService.findAll();
		session.setAttribute(Constants.ISPYB_REFERENCE_LIST, listOfReference);

		// load the allowed space groups
		this.spaceGroupService = (SpaceGroup3Service) ejb3ServiceLocator.getLocalService(SpaceGroup3Service.class);
		List<SpaceGroup3VO> listOfSpaceGroup = this.spaceGroupService.findAllowedSpaceGroups();
		List<String> spaceGroups = new ArrayList<String>();
		if (listOfSpaceGroup != null) {
			for (Iterator<SpaceGroup3VO> iter = listOfSpaceGroup.iterator(); iter.hasNext();) {
				SpaceGroup3VO sp = iter.next();
				spaceGroups.add(sp.getSpaceGroupShortName());
			}
		}
		session.setAttribute(Constants.ISPYB_ALLOWED_SPACEGROUPS_LIST, spaceGroups);

	}

}
