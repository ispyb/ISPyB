/** This file is part of ISPyB.
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

package ispyb.ws.soap.mx;

import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.security.LdapSearchModule;
import ispyb.server.common.services.proposals.Laboratory3Service;
import ispyb.server.common.services.proposals.Person3Service;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Laboratory3VO;
import ispyb.server.common.vos.proposals.PersonWS3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.proposals.ProposalWS3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.collections.SessionWS3VO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import com.google.gson.Gson;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ws.api.annotation.WebContext;

/**
 * Web services for Shipment / proposal
 * 
 * @author BODIN
 * 
 */
@WebService(name = "ToolsForShippingWebService", serviceName = "ispybWS", targetNamespace = "http://ispyb.ejb3.webservices.shipping")
@SOAPBinding(style = Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Stateless(name="ToolsForShippingWebService")
@RolesAllowed({ "WebService", "User", "Industrial"}) // allow special access roles
@SecurityDomain("ispyb")
@WebContext(authMethod="BASIC",  secureWSDLAccess=false, transportGuarantee="NONE")
public class ToolsForShippingWebService {
	private final static Logger LOG = Logger.getLogger(ToolsForShippingWebService.class);


	@WebMethod(operationName = "echo")
	@WebResult(name = "echo")
	public String echo(){
		return "echo from server...";
	}
	
	@WebMethod
	@WebResult(name = "person")
	public PersonWS3VO findPersonBySessionId(@WebParam(name = "sessionId") Integer sessionId) throws Exception {
		try {
			LOG.debug("findPersonBySessionId");
			PersonWS3VO personValue = null;

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Person3Service personService = (Person3Service) ejb3ServiceLocator.getLocalService(Person3Service.class);

			personValue = personService.findForWSPersonBySessionId(sessionId);
			if (personValue != null)
				LOG.debug("findPersonBySessionId " + personValue.getFamilyName());
			return personValue;
		} catch (Exception e) {
			LOG.error("WS ERROR: findPersonBySessionId - " + StringUtils.getCurrentDate() + " - " + sessionId);
			throw e;
		}
	}

	// 03/07/12: returns the person corresponding to the beamlineOperator of the given session
	@WebMethod
	@WebResult(name = "person")
	public PersonWS3VO findPersonBySessionIdLocalContact(@WebParam(name = "sessionId") Integer sessionId)
			throws Exception {
		try {
			LOG.debug("findPersonBySessionIdLocalContact");
			PersonWS3VO personValue = null;

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Person3Service personService = (Person3Service) ejb3ServiceLocator.getLocalService(Person3Service.class);
			Session3Service sessionService = (Session3Service) ejb3ServiceLocator
					.getLocalService(Session3Service.class);
			Session3VO session = sessionService.findByPk(sessionId, false, false, false);
			String[] tab = session.getBeamlineOperatorLastNameAndFirstNameLetter();
			if (tab != null) {
				// Get first letter of firstName + *
				String lastName = tab[0];
				String firstNameLetter = tab[1];
				if (lastName == null && firstNameLetter == null)
					return null;
				personValue = personService.findForWSPersonByLastNameAndFirstNameLetter(lastName, firstNameLetter);
				LOG.debug("findPersonBySessionIdLocalContact " + lastName);
				return personValue;

			} else
				return null;
		} catch (Exception e) {
			LOG.error("WS ERROR: findPersonBySessionIdLocalContact - " + StringUtils.getCurrentDate() + " - "
					+ sessionId);
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "proposal")
	public ProposalWS3VO findProposalByCodeAndNumber(String code, Integer number) throws Exception {
		try {
			LOG.debug("findProposalByCodeAndNumber");
			ProposalWS3VO proposalValue = null;

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Proposal3Service proposalService = (Proposal3Service) ejb3ServiceLocator
					.getLocalService(Proposal3Service.class);

			proposalValue = proposalService.findForWSByCodeAndNumber(StringUtils.getProposalCode(code), number+"");
			if (proposalValue != null)
				LOG.debug("findProposalByCodeAndNumber " + proposalValue.getProposalId());
			return proposalValue;
		} catch (Exception e) {
			LOG.error("WS ERROR: findProposalByCodeAndNumber - " + StringUtils.getCurrentDate() + " - " + code + ", "
					+ number);
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "proposal")
	public ProposalWS3VO findProposal(String code, String number) throws Exception {
		try {
			LOG.debug("findProposal");
			ProposalWS3VO proposalValue = null;

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Proposal3Service proposalService = (Proposal3Service) ejb3ServiceLocator
					.getLocalService(Proposal3Service.class);

			proposalValue = proposalService.findForWSByCodeAndNumber(StringUtils.getProposalCode(code), number);
			if (proposalValue != null)
				LOG.debug("findProposal " + proposalValue.getProposalId());
			return proposalValue;
		} catch (Exception e) {
			LOG.error("WS ERROR: findProposal - " + StringUtils.getCurrentDate() + " - " + code + ", "
					+ number);
			throw e;
		}
	}


	@WebMethod
	@WebResult(name = "person")
	public PersonWS3VO findPersonByCodeAndNumber(String code, Integer number) throws Exception {
		try {
			LOG.debug("findPersonByCodeAndNumber");
			PersonWS3VO personValue = null;

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Person3Service personService = (Person3Service) ejb3ServiceLocator.getLocalService(Person3Service.class);

			personValue = personService.findForWSPersonByProposalCodeAndNumber(StringUtils.getProposalCode(code),
					number+"");
			if (personValue != null)
				LOG.debug("findPersonByCodeAndNumber " + personValue.getFamilyName());
			return personValue;
		} catch (Exception e) {
			LOG.error("WS ERROR: findPersonByCodeAndNumber - " + StringUtils.getCurrentDate() + " - " + code + ", "
					+ number);
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "person")
	public PersonWS3VO findPersonByProposal(String code, String number) throws Exception {
		try {
			LOG.debug("findPersonByProposal");
			PersonWS3VO personValue = null;

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Person3Service personService = (Person3Service) ejb3ServiceLocator.getLocalService(Person3Service.class);

			personValue = personService.findForWSPersonByProposalCodeAndNumber(StringUtils.getProposalCode(code),
					number);
			if (personValue != null)
				LOG.debug("findPersonByProposal " + personValue.getFamilyName());
			return personValue;
		} catch (Exception e) {
			LOG.error("WS ERROR: findPersonByProposal - " + StringUtils.getCurrentDate() + " - " + code + ", "
					+ number);
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "laboratory")
	public Laboratory3VO findLaboratoryByCodeAndNumber(String code, Integer number) throws Exception {
		try {
			LOG.debug("findLaboratoryByCodeAndNumber " + code + number);
			Laboratory3VO laboratoryValue = null;

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Laboratory3Service laboratoryService = (Laboratory3Service) ejb3ServiceLocator
					.getLocalService(Laboratory3Service.class);

			laboratoryValue = laboratoryService.findLaboratoryByProposalCodeAndNumber(
					StringUtils.getProposalCode(code), number+"", true);
			if (laboratoryValue != null)
				LOG.debug("findLaboratoryByCodeAndNumber " + laboratoryValue.getName());

			return laboratoryValue;
		} catch (Exception e) {
			LOG.error("WS ERROR: findLaboratoryByCodeAndNumber - " + StringUtils.getCurrentDate() + " - " + code + ", "
					+ number);
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "laboratory")
	public Laboratory3VO findLaboratoryByProposal(String code, String number) throws Exception {
		try {
			LOG.debug("findLaboratoryByProposal " + code + number);
			Laboratory3VO laboratoryValue = null;

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Laboratory3Service laboratoryService = (Laboratory3Service) ejb3ServiceLocator
					.getLocalService(Laboratory3Service.class);

			laboratoryValue = laboratoryService.findLaboratoryByProposalCodeAndNumber(
					StringUtils.getProposalCode(code), number, true);
			if (laboratoryValue != null)
				LOG.debug("findLaboratoryByProposal " + laboratoryValue.getName());

			return laboratoryValue;
		} catch (Exception e) {
			LOG.error("WS ERROR: findLaboratoryByProposal - " + StringUtils.getCurrentDate() + " - " + code + ", "
					+ number);
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "person")
	public PersonWS3VO findPersonByLogin(String login) throws Exception {
		try {
			LOG.debug("findPersonByLogin");
			PersonWS3VO personValue = null;

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Person3Service personService = (Person3Service) ejb3ServiceLocator.getLocalService(Person3Service.class);

			personValue  = personService.findForWSPersonByLogin(login);
			if (personValue != null)
				LOG.debug("findPersonByLogin " + personValue.getFamilyName());
			return personValue;
		} catch (Exception e) {
			LOG.error("WS ERROR: findPersonByLogin - " + StringUtils.getCurrentDate() + " - " + login);
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "proposal")
	public ProposalWS3VO findProposalByLoginAndBeamline(String login, String beamline) throws Exception {
		try {
			LOG.debug("findProposalByLoginAndBeamline");
			ProposalWS3VO proposalValue = null;

			List<ProposalWS3VO> proposalValues = new ArrayList<ProposalWS3VO>();
			
			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Proposal3Service proposalService = (Proposal3Service) ejb3ServiceLocator
					.getLocalService(Proposal3Service.class);
			
			
			//get proposal groups from LDAP
			LOG.debug("findProposalByLoginAndBeamline: get proposal groups from LDAP for the user " +login +"...");
			LdapSearchModule ldapModule = new LdapSearchModule();
			List<String> groups = ldapModule.getUserGroups(login);


			//Get the proposals of the user for a specific beamline
			LOG.debug("findProposalByLoginAndBeamline: get the proposals for the " +beamline +" beamline...");
			for (String groupName : groups) {
				ArrayList<String> proposalNumberAndCode = StringUtils.GetProposalNumberAndCode(groupName);
				if (proposalNumberAndCode != null && !proposalNumberAndCode.isEmpty()){
					LOG.debug("findProposalByLoginAndBeamline: find the proposal in the DB with code = " +StringUtils.getProposalCode(proposalNumberAndCode.get(0)) +" and number = " +proposalNumberAndCode.get(2));
					ProposalWS3VO prop= proposalService.findForWSByCodeAndNumber(StringUtils.getProposalCode(proposalNumberAndCode.get(0)), proposalNumberAndCode.get(2)+"");
					proposalValues.add(prop);
				} else {
					LOG.debug("findProposalByLoginAndBeamline: no match found between group and proposal number/code");
				}
			}
			if (proposalValues != null && !proposalValues.isEmpty()) {
				LOG.debug("findProposalByLoginAndBeamline: "+proposalValues.size() +" proposals found in the ISPyB database");
				LOG.debug("findProposalByLoginAndBeamline: check if there is a proposal with a current active session (only for accademic experiments)");
				Session3Service sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
				boolean stopSearch = Boolean.FALSE;
				for (ProposalWS3VO proposal : proposalValues) {
					//check if there is a proposal with a current session if not in house or industrial
					LOG.debug("findProposalByLoginAndBeamline: Check proposal with code:" +proposal.getCode());
					if (!(proposal.getCode().startsWith(Constants.PROPOSAL_CODE_MXIHR) || proposal.getCode().startsWith(Constants.PROPOSAL_CODE_FX))){
						SessionWS3VO[] sessions = sessionService.findForWSByProposalCodeAndNumber(proposal.getCode(), proposal.getNumber(), beamline);
						if (!stopSearch) {
							if (sessions != null && sessions.length == 1) {
								// Only an active session
								stopSearch = true;
								proposalValue = proposal;
							} else if ((sessions != null && sessions.length == 0) || sessions == null) {
								//No active session
								proposalValue = proposal;
								LOG.debug("findProposalByLoginAndBeamline: The user " +login +" has no active session for the proposal " +proposal.getCode() + "-" +proposal.getNumber());
							}
						}
					} else {
						LOG.debug("findProposalByLoginAndBeamline: proposal with code " +proposal.getCode() +" is a in house research / industrial");
						proposalValue = proposal;
					}
				}
				if (proposalValue != null){
					LOG.debug("findProposalByLoginAndBeamline: return the proposal for the current session... " + proposalValue.getCode() + "-" +proposalValue.getNumber());
				}
			} else {
				LOG.debug("findProposalByLoginAndBeamline: no proposals found");
			}
			
			return proposalValue;
		} catch (Exception e) {
			LOG.error("WS ERROR: findProposalByLoginAndBeamline - " + StringUtils.getCurrentDate() + " - Login:" + login + " - Beamline:" +beamline);
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "findProposalsByLoginName")
	public String findProposalsByLoginName(
			@WebParam(name = "loginName") String loginName
			) throws Exception {

		/** Logging **/
		long id = 0;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("userName", String.valueOf(loginName));
			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Proposal3Service service = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
			List<Proposal3VO> proposals = service.findByLoginName(loginName);
			
			ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
			
			for (Proposal3VO proposal3vo : proposals) {
				HashMap<String, String> entry = new HashMap<String, String>();
				entry.put("title", proposal3vo.getTitle());
				entry.put("code", proposal3vo.getCode());
				entry.put("number", proposal3vo.getNumber());
				entry.put("type", proposal3vo.getType());
				entry.put("proposalId", proposal3vo.getProposalId().toString());
				result.add(entry);
			}
			return new Gson().toJson(result);
		} catch (Exception e) {
			LOG.error("WS ERROR: findProposalsByLoginName - " + StringUtils.getCurrentDate() + " - Login:" + loginName);
			throw e;
		}
	}
	

}
