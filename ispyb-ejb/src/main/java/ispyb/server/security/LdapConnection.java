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
package ispyb.server.security;

import ispyb.common.util.Constants;
import ispyb.server.common.vos.proposals.Person3VO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.log4j.Logger;

public class LdapConnection {

	private final static Logger LOG = Logger.getLogger(LdapConnection.class);

	private static final String LDAP_Employee_Identifier = Constants.LDAP_Employee_Identifier;

	private static final String LDAP_Employee_Resource = Constants.LDAP_Employee_Resource;

	private static final String LDAP_people = Constants.LDAP_people;

	private static final String LDAP_people_AllEmployeefilter = "persCategory=S";

	private static final String LDAP_people_AllOnSitefilter = "(&amp;(objectClass=esrfPerson)  (!(persCategory=B)) )";

	private static final String LDAP_prefix = Constants.LDAP_prefix;
	
	private static final String LDAP_username = Constants.LDAP_username;
	
	private static final String LDAP_credential = Constants.LDAP_credential;
	
	public static EmployeeVO findByUniqueIdentifier(String intMat) throws Exception {
		String searchAttribute = LDAP_Employee_Identifier;
		return findOneEmployee(intMat, searchAttribute);
	}

	public static EmployeeVO findOneEmployee(String value, String searchAttribute) {
		String ldapEmployeeDirectory = LDAP_Employee_Resource;
		LdapContext ldapCtx;
		EmployeeVO emp = null;
		try {
			ldapCtx = getLDAPContext();
			String people = LDAP_people;
			Attributes matchAttrs = new BasicAttributes(true);
			matchAttrs.put(new BasicAttribute(searchAttribute, value));
			NamingEnumeration answer = ldapCtx.search(people, matchAttrs);
			if (answer.hasMoreElements()) {
				SearchResult result = (SearchResult) answer.next();
				Map<String, String> attributesMap = getAttributesMap(result);
				emp = new EmployeeVO(attributesMap);
			} else {
				Map<String, String> attributesMap = new HashMap();
				attributesMap.put("cn", value + " (Employee not found in LDAP directory)");
				emp = new EmployeeVO(attributesMap);
				emp.setObsolete(true);
			}
			answer.close();
			ldapCtx.close();

		} catch (NamingException e) {
			LOG.error("LdapContext " + ldapEmployeeDirectory + " not bound. Exception: " + e.toString());
			// ESRF ####
			if (Constants.SITE_IS_ESRF() || Constants.SITE_IS_MAXIV()) {
				try {
					throw new Exception(e);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			// DLS ####
			if (Constants.SITE_IS_DLS()) {
				return null;
			}

		}
		return emp;
	}

	public static Collection<Person3VO> findEmployees(String value, String searchAttribute) {
		String ldapEmployeeDirectory = LDAP_Employee_Resource;
		Collection<Person3VO> matchingPersons = new ArrayList<Person3VO>();
		LdapContext ldapCtx;
		try {
			InitialContext iniCtx = new InitialContext();
			ldapCtx = (LdapContext) iniCtx.lookup(ldapEmployeeDirectory);
			String people = LDAP_people;
			Attributes matchAttrs = new BasicAttributes(true);
			matchAttrs.put(new BasicAttribute(searchAttribute, value));
			NamingEnumeration<SearchResult> answer = ldapCtx.search(people, matchAttrs);
			EmployeeVO emp = null;

			if (!answer.hasMoreElements()) {
				Map<String, String> attributesMap = new HashMap();
				attributesMap.put("cn", value + " (Employee not found in LDAP directory)");
				emp = new EmployeeVO(attributesMap);
				emp.setObsolete(true);
			}

			while (answer.hasMoreElements()) {
				SearchResult result = answer.next();
				Map<String, String> attributesMap = getAttributesMap(result);

				Person3VO p = new Person3VO();
				p.setFamilyName(attributesMap.get("sn"));
				p.setGivenName(attributesMap.get("givenName"));
				p.setEmailAddress(attributesMap.get("mail"));
				matchingPersons.add(p);
			}

			answer.close();
			ldapCtx.close();
			iniCtx.close();
			return matchingPersons;
		} catch (NamingException e) {
			LOG.error("LdapContext " + ldapEmployeeDirectory + " not bound. Exception: " + e.toString());
			return null;
		}
	}

	private static Map<String, String> getAttributesMap(SearchResult result) throws NamingException {
		Map<String, String> attributesMap = new HashMap<String, String>();
		NamingEnumeration<? extends Attribute> attributes = result.getAttributes().getAll();
		try {
			while (attributes.hasMore()) {
				Attribute current = attributes.next();
				attributesMap.put(current.getID(), current.get().toString());
				// ESRF ####
				if (Constants.SITE_IS_ESRF() || Constants.SITE_IS_MAXIV()) {
					current.getID();
					current.get();
				}
			}
		} catch (NamingException e) {
			LOG.error(e);
			throw e;
		}
		return attributesMap;
	}

	/**
	 * @param lastName
	 * @param fisrtName
	 * @return
	 * @throws Exception
	 */
	public static EmployeeVO findOneEmployeeByNames(String lastName, String firstName) throws Exception {
		String origName = lastName;

		EmployeeVO emp = findOneEmployeeByNameAndFirstname(lastName,firstName);
		if (emp != null){
			return emp;
		} else {
			// try with wildcards *
			lastName = origName + "*";
			emp = findOneEmployeeByNameAndFirstname(lastName,firstName);
			if (emp != null){
				return emp;
			} else {
				// try with wildcards *
				lastName = "*" + origName;
				emp = findOneEmployeeByNameAndFirstname(lastName,firstName);
			}
		}
		return emp;
	}
	
	/**
	 * @param lastName
	 * @param fisrtName
	 * @return
	 * @throws Exception
	 */
	private static EmployeeVO findOneEmployeeByNameAndFirstname(String lastName, String firstName) throws Exception {

		String ldapEmployeeDirectory = LDAP_Employee_Resource;
		LdapContext ldapCtx;
		try {
			InitialContext iniCtx = new InitialContext();
			ldapCtx = (LdapContext) iniCtx.lookup(ldapEmployeeDirectory);
			String people = LDAP_people;
			String filter = "(&(sn=" + lastName + ")(givenName=" + firstName + "))";
			NamingEnumeration answer = ldapCtx.search(people, filter, null);
			EmployeeVO emp = null;
			if (answer.hasMoreElements()) {
				SearchResult result = (SearchResult) answer.next();
				if (answer.hasMoreElements()) {
					// Not unique answer
					LOG.error("findOneEmployeeByNames: employee " + lastName + "/" + firstName + " is not unique.");
					emp = null;
				} else {
					// Ok
					Map attributesMap = getAttributesMap(result);
					emp = new EmployeeVO(attributesMap);
				}
			} else {
				// Not found
				LOG.error("findOneEmployeeByNames employee " + lastName + "/" + firstName + " not found.");	
					emp = null;
			}
			answer.close();
			ldapCtx.close();
			iniCtx.close();
			return emp;
		} catch (NamingException e) {
			LOG.error("LdapContext " + ldapEmployeeDirectory + " not bound");
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	/**
	 * @param lastName
	 * @param firstName
	 * @return
	 */
	public static String getLocalContactEmail(String lastName, String firstName) {

		EmployeeVO me = null;
		String uid = "";
		String emailAddress = "";
		String res = null;

		try {
			me = findOneEmployeeByNames(lastName, firstName);
			if (me != null) {
				// Get uid and email address
				uid = me.getUid();
				emailAddress = me.getMail();
				// TODO : get rid of this later : apparently local contact group is not really maintained ???
				// Check if has LocalContact role
				// if (getGroups(uid).contains(Constants.LOCALCONTACT_ROLE_NAME)) {
				// res = emailAddress;
				// } else {
				// LOG.error("User " + lastName + "/" + firstName + " is not in LocalContact group in ldap");
				//
				// }
			} else {

				LOG.error("Cannot find user in ldap with lastname= " + lastName + " and firstname= " + firstName);
			}
		} catch (Exception e) {
			LOG.error("Cannot access to LDAP");
		}
		res = emailAddress;
		return res;
	}
	
	private static LdapContext getLDAPContext() throws NamingException{
		LdapContext ldapContext = null;
		
		if(Constants.SITE_IS_MAXIV()){
			Properties env = new Properties();
			
			env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
			env.put("java.naming.provider.url", LDAP_Employee_Resource);
			env.put(Context.SECURITY_AUTHENTICATION,"simple");
			String userDN = LDAP_prefix + LDAP_username;
			env.setProperty(Context.SECURITY_PRINCIPAL, userDN);
			env.put(Context.SECURITY_CREDENTIALS, LDAP_credential);
			
			ldapContext = new InitialLdapContext(env, null);
			
		} else {
			InitialContext iniCtx = new InitialContext();
			ldapContext = (LdapContext) iniCtx.lookup(LDAP_Employee_Resource);
		}
		
		return ldapContext;
	}
}
