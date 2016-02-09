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
package ispyb.client.common.welcome;

import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.menu.MenuContext;
import ispyb.client.common.menu.MenuItem;
import ispyb.client.common.proposal.ViewProposalForm;
import ispyb.common.util.Constants;
import ispyb.server.common.services.proposals.Person3Service;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;

import java.security.acl.Group;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.naming.ldap.InitialLdapContext;
import javax.security.auth.Subject;
import javax.security.jacc.PolicyContext;
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
import org.jboss.security.SubjectSecurityManager;

/**
 * 
 * @struts.action path="/admin/welcomeAdmin" name="viewProposalForm" validate="false"
 * 
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 * @struts.action-forward name="success" path="admin.welcome.page"
 * 
 * @struts.action-forward name="viewSessionPage"
 *                        path="/menuSelected.do?topMenuId=16&amp;targetUrl=%2Fuser%2FviewSession.do%3FreqCode%3Ddisplay"
 * 
 */
public class WelcomeAdminAction extends Action {
	private final Logger LOG = Logger.getLogger(WelcomeAdminAction.class);
	protected Proposal3Service proposalService;
	private Person3Service personService;

	protected Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {
		this.personService = (Person3Service) ejb3ServiceLocator.getLocalService(Person3Service.class);
		this.proposalService = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm actForm,
			final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		this.initServices();
		ActionMessages errors = new ActionMessages();

		String personLogin = request.getUserPrincipal().toString();
		request.getSession().setAttribute(Constants.PERSON_LOGIN, personLogin);

		Integer personId = new Integer(0);

		try {

			Person3VO personValue = personService.findByLogin(personLogin);

			if (personValue != null ) {
					personId = personValue.getPersonId();
					request.getSession().setAttribute(Constants.PERSON_ID, personId);
			} else {
				personId = new Integer(0);
				request.getSession().setAttribute(Constants.PERSON_ID, personId);

			}

			// retrieve all proposals

			List<Proposal3VO> proposalValues = proposalService.findAll(false);

			Integer proposalId = new Integer(-1);
			// Was the proposal ID sent as a parameter? If so, pick it up ...
			String proposalIdStr = request.getParameter(Constants.PROPOSAL_ID);
			if (proposalIdStr != null) {
				proposalId = new Integer(proposalIdStr);
			}
			// If not, try to pick it up from the session's attribs. (Meaning it's been set already.)
			else {
				Object pObj = request.getSession().getAttribute(Constants.PROPOSAL_ID);
				if (pObj instanceof Integer) {
					proposalId = (Integer) pObj;
				}
				// If not set already, try to use the first proposal in the list
				else if (proposalValues.size() > 0) {
					Proposal3VO value = proposalValues.get(0);
					if (value == null)
						return (mapping.findForward("error"));
					proposalId = value.getProposalId();
				} else {// If list is empty, then give up and return error
					LOG.debug("List is empty!");
					return (mapping.findForward("error"));
				}
			}

			// Find the proposalId in the proposalValues list
			Proposal3VO value = null;
			Proposal3VO p = null;
			for (int i = 0; i < proposalValues.size(); i++) {
				p = proposalValues.get(i);
				if (p.getProposalId().intValue() == proposalId.intValue()) {
					value = p;
					break;
				}
			}
			if (value == null)
				value = p;

			// Return error if not found in list
			if (value == null) {
				LOG.debug("proposalId " + proposalId.toString() + " not found in proposal list!");
				return (mapping.findForward("error"));
			}

			LOG.debug(
					"Setting session attribute proposal_id=" + value.getProposalId() + ", proposal_code="
							+ value.getCode() + ", proposal_number=" + value.getNumber());

			Proposal3VO vo = proposalService.findByPk(proposalId);

			BreadCrumbsForm.getIt(request).setSelectedProposal(vo);

			request.getSession().setAttribute(Constants.PROPOSAL_ID, value.getProposalId());
			request.getSession().setAttribute(Constants.PROPOSAL_CODE, value.getCode());
			request.getSession().setAttribute(Constants.PROPOSAL_NUMBER, value.getNumber());

			String name = getNameOfUser();
			if (name != null) {
				request.getSession().setAttribute(Constants.FULLNAME, name);
				BreadCrumbsForm.getIt(request).setFullname(name);
				LOG.debug("SpecificActionServlet: setting Constants.FULLNAME: " + name);
			}

			// Set the selected top menu to be the "Proposal" menu
			setSelectedTopMenu("Proposal", request.getSession());

			ViewProposalForm form = (ViewProposalForm) actForm;
			List listInfo = Arrays.asList(proposalValues);
			form.setListInfo(listInfo);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.welcome.username"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		if (request.getParameter(Constants.PROPOSAL_ID) != null)
			return mapping.findForward("viewSessionPage");
		else
			return mapping.findForward("success");
	}

	private Subject getAuthenticatedSubject(SubjectSecurityManager mgr) throws Exception {
		// First get the JACC Subject
		String SUBJECT_CONTEXT_KEY = "javax.security.auth.Subject.container";
		Subject subject = (Subject) PolicyContext.getContext(SUBJECT_CONTEXT_KEY);

		// Fallback
		if (subject == null && mgr != null) {
			subject = mgr.getActiveSubject();
		}
		return subject;
	}

	private String getNameOfUser() throws Exception {
		// Get user's name from list of roles sent from the Login Module

		InitialLdapContext context = new InitialLdapContext();
		SubjectSecurityManager manager = (SubjectSecurityManager) context.lookup("java:comp/env/security/securityMgr");
		Subject subject = this.getAuthenticatedSubject(manager);

		String name = null;
		Set princSet = subject.getPrincipals();
		Object princArray[] = princSet.toArray();

		for (int i = 0; i < princArray.length; i++) {
			Object o = princArray[i];
			if (o instanceof Group) {
				Group group = (Group) o;

				if (group.getName().equals("Roles")) {
					java.util.Enumeration en = group.members();
					while (en.hasMoreElements()) {
						String r = en.nextElement().toString();
						if (r.startsWith("name:")) {
							name = r.substring(5);
							return name;
						}
					}
				}
			}
		}
		return name;
	}

	private void setSelectedTopMenu(String menuName, HttpSession session) {
		// Set the selected top menu to be the "Proposal" menu
		MenuContext menu = (MenuContext) session.getAttribute(Constants.MENU);
		String menuTopId = null;
		Iterator it = menu.fetchTopMenu().iterator();
		while (it.hasNext()) {
			MenuItem menuItem = (MenuItem) it.next();
			if (menuItem.getName().equals("Proposal")) {
				menuTopId = Integer.toString(menuItem.getId());
				session.setAttribute(Constants.TOP_MENU_ID, menuTopId);
				menu.setActiveTopMenu((new Integer(menuTopId)).intValue());
				break;
			}
		}

	}

}
