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
 * SendMailAction.java
 * @author ludovic.launer@esrf.fr
 * Oct 19, 2005
 */

package ispyb.client.common.help;

import ispyb.client.common.menu.MenuContext;
import ispyb.client.common.util.Confidentiality;
import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.common.util.PropertyLoader;
import ispyb.common.util.StringUtils;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * @struts.action name="sendMailForm" path="/user/SendMailAction" type="ispyb.client.common.help.SendMailAction" validate="false"
 *                parameter="reqCode" scope="request"
 * @struts.action-forward name="FeedBackPage" path="guest.help.feedback.page"
 * @struts.action-forward name="ManagerFeedBackPage" path="manager.help.feedback.page"
 * @struts.action-forward name="localcontactFeedBackPage" path="localcontact.help.feedback.page"
 * @struts.action-forward name="HelpPage" path="guest.help.main.page"
 * @struts.action-forward name="ManagerHelpPage" path="guest.help.manager.page"
 * @struts.action-forward name="localcontactHelpPage" path="guest.help.localcontact.page"
 * @struts.action-forward name="error" path="site.default.error.page"
 */

public class SendMailAction extends org.apache.struts.actions.DispatchAction {
	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private final static Logger LOG = Logger.getLogger(SendMailAction.class);

	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
		// redirection page according to the Role ------------------------
		// Issue 1363: feedback for manager
		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();

		if (role.equals(Constants.ROLE_MANAGER)) {
			return mapping.findForward("ManagerFeedBackPage");
		} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
			return mapping.findForward("localcontactFeedBackPage");
		} else {
			return mapping.findForward("FeedBackPage");
		}
	}

	public ActionForward sendMail(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();
		// redirection page according to the Role ------------------------
		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();

		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			SendMailForm form = (SendMailForm) actForm; // Parameters submited by form
			String senderEmail = form.getSenderEmail();
			String body = form.getBody();

			// check sender email
			if (!checkFeedbackInformation(form, request)) {
				if (role.equals(Constants.ROLE_MANAGER)) {
					return mapping.findForward("ManagerFeedBackPage");
				} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
					return mapping.findForward("localcontactFeedBackPage");
				} else {
					return mapping.findForward("FeedBackPage");
				}
			}

			Integer mProposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID); // Session
																										// parameters
			// ------------ Retrieve info from DB ----------------------------------------------------------------
			Proposal3Service proposalService = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
			Proposal3VO proposal = proposalService.findByPk(mProposalId);
			// ---------------------------------

			String host = Constants.MAIL_HOST;
			if (host == null)
				host = "localhost";
			String from = Constants.MAIL_FROM;
			if (from == null)
				from = "ispyb@embl-grenoble.fr";
			String to = Constants.MAIL_TO;
			if (to == null)
				to = "root@localhost";
			String cc = Constants.MAIL_CC;
			if (cc == null)
				cc = "root@localhost";

			// Get system properties
			Properties props = System.getProperties();

			// Setup mail server
			props.put("mail.smtp.host", host);

			// Get session
			Session session = Session.getDefaultInstance(props, null);

			// Define message
			MimeMessage message = new MimeMessage(session);

			/** From is the Sender of the email so it is the user otherwise JIRA gets crazy */
			if (Constants.SITE_IS_ESRF()){
				from = senderEmail;
			}
			
			
			LOG.info("Feedback sent from: " + from);
			LOG.info("Feedback sent to: " + to);
			LOG.info("Feedback cc senderEmail: " + senderEmail);
			
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.addRecipient(Message.RecipientType.CC, new InternetAddress(senderEmail));
			String subject = " ";
			if (proposal != null) {
				MenuContext menu = (MenuContext) request.getSession().getAttribute(Constants.MENU);
				if (menu != null) {
					String proposalType = menu.getProposalType();
					if (proposalType != null && proposalType.equals(Constants.PROPOSAL_BIOSAXS)) {
						proposalType = "BioSAXS";
					}
					subject += proposalType;
				}
				subject += " " + proposal.getCode() + proposal.getNumber();

			} else {
				// manager?
				if (Confidentiality.isManager(request)) {
					subject += "manager";
				} else if (Confidentiality.isLocalContact(request)) {
					subject += "localContact";
				}
			}
			message.setSubject(subject);
			message.setText(senderEmail + "\r\r" + body);

			// Send message
			Transport.send(message);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}

		if (role.equals(Constants.ROLE_MANAGER)) {
			return mapping.findForward("ManagerHelpPage");
		} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
			return mapping.findForward("localcontactHelpPage");
		} else {
			return mapping.findForward("HelpPage");
		}
	}

	/**
	 * checks the sender mail, returns false if the email format is incorrect
	 * 
	 * @param form
	 * @return
	 */
	private boolean checkFeedbackInformation(SendMailForm form, HttpServletRequest request) {
		ActionMessages l_ActionMessages = new ActionMessages();
		boolean isOk = true;
		// sender email
		if (form.getSenderEmail() != null && form.getSenderEmail().trim().length() == 0) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.required", "Your email");
			l_ActionMessages.add("senderEmail", l_ActionMessageLabel);
		}
		if (form.getSenderEmail() != null && form.getSenderEmail().trim().length() > 0) {
			if (!StringUtils.isValidEmailAdress(form.getSenderEmail())) {
				isOk = false;
				ActionMessage l_ActionMessageLabel = new ActionMessage("errors.email", "Your email");
				l_ActionMessages.add("senderEmail", l_ActionMessageLabel);
			}
		}
		// body
		if (form.getBody() != null && form.getBody().trim().length() == 0) {
			isOk = false;
			ActionMessage l_ActionMessageLabel = new ActionMessage("errors.required", "Your feedback");
			l_ActionMessages.add("body", l_ActionMessageLabel);
		}
		if (!isOk) {
			request.setAttribute("org.apache.struts.action.ERROR", l_ActionMessages);
		}
		return isOk;
	}
}
