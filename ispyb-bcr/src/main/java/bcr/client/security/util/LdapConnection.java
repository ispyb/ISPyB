package bcr.client.security.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.apache.log4j.Logger;

public class LdapConnection {

	private final static Logger LOG = Logger.getLogger(LdapConnection.class);

	private static final String LDAP_Employee_Identifier = "uid";

	private static final String LDAP_Employee_Resource = "ldap://ldap.esrf.fr:389/";

	private static final String LDAP_people = "ou=people,dc=esrf,dc=fr";

	private static final String LDAP_pxwebgroups = "ou=pxwebgroups,dc=esrf,dc=fr";

	private static final String LDAP_people_AllEmployeefilter = "persCategory=S";

	private static final String LDAP_people_AllOnSitefilter = "(&amp;(objectClass=esrfPerson)  (!(persCategory=B)) )";
	
	/**
	 * @param intMat
	 * @return
	 * @throws Exception
	 */
	public static EmployeeVO findByUniqueIdentifier(String intMat) throws Exception {
		String searchAttribute = LDAP_Employee_Identifier;
		return findOneEmployee(intMat, searchAttribute);
	}

	/**
	 * @param result
	 * @return
	 * @throws NamingException
	 */
	private static Map getAttributesMap(SearchResult result) throws NamingException {
		Map<String, String> attributesMap = new HashMap();
		NamingEnumeration attributes = result.getAttributes().getAll();
		try {
			while (attributes.hasMore()) {
				Attribute current = (Attribute) attributes.next();
				attributesMap.put(current.getID(), current.get().toString());
				current.getID();
				current.get();
			}
		} catch (NamingException e) {
			LOG.error(e);
			throw e;
		}
		return attributesMap;
	}

	/**
	 * @param value
	 * @param searchAttribute
	 * @return
	 * @throws Exception
	 */
	private static EmployeeVO findOneEmployee(String value, String searchAttribute) throws Exception {
		String ldapEmployeeDirectory = LDAP_Employee_Resource;
		LdapContext ldapCtx;
		try {
			InitialContext iniCtx = new InitialContext();
			ldapCtx = (LdapContext) iniCtx.lookup(ldapEmployeeDirectory);
			String people = LDAP_people;
			Attributes matchAttrs = new BasicAttributes(true);
			matchAttrs.put(new BasicAttribute(searchAttribute, value));
			NamingEnumeration answer = ldapCtx.search(people, matchAttrs);
			EmployeeVO emp = null;
			if (answer.hasMoreElements()) {
				SearchResult result = (SearchResult) answer.next();
				Map attributesMap = getAttributesMap(result);
				emp = new EmployeeVO(attributesMap);
			} else {
				Map attributesMap = new HashMap();
				attributesMap.put("cn", value + " (Employee not found in LDAP directory)");
				emp = new EmployeeVO(attributesMap);
				emp.setObsolete(true);
			}
			answer.close();
			ldapCtx.close();
			iniCtx.close();
			return emp;
		} catch (NamingException e) {
			LOG.error("LdapContext " + ldapEmployeeDirectory + " not bound");
			throw new Exception(e);
		}
	}

	/**
	 * @param lastName
	 * @param fisrtName
	 * @return
	 * @throws Exception
	 */
	public static EmployeeVO findOneEmployeeByNames(String lastName, String firstName) throws Exception {

		String ldapEmployeeDirectory = LDAP_Employee_Resource;
		LdapContext ldapCtx;
		try {
			InitialContext iniCtx = new InitialContext();
			ldapCtx = (LdapContext) iniCtx.lookup(ldapEmployeeDirectory);
			String people = LDAP_people;
			lastName = "*" + lastName + "*";
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
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public static List<String> getGroups(String uid) throws Exception {

		List<String> groupList = new ArrayList<String>();
		String ldapEmployeeDirectory = LDAP_Employee_Resource;
		LdapContext ldapCtx;
		try {
			InitialContext iniCtx = new InitialContext();
			ldapCtx = (LdapContext) iniCtx.lookup(ldapEmployeeDirectory);
			String people = LDAP_pxwebgroups;
			String filter = "(uniqueMember=uid=" + uid + ",ou=people,dc=esrf,dc=fr)";
			NamingEnumeration answer = ldapCtx.search(people, filter, null);
			while (answer.hasMoreElements()) {
				SearchResult result = (SearchResult) answer.next();
				NamingEnumeration attributes = result.getAttributes().getAll();
				while (attributes.hasMore()) {
					Attribute current = (Attribute) attributes.next();
					if (current.getID().equals("cn"))
						groupList.add(current.get().toString());
				}
			}
			answer.close();
			ldapCtx.close();
			iniCtx.close();
			return groupList;
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

				LOG.error("Cannot find user in ldap " + lastName + "/" + firstName);
			}
		} catch (Exception e) {
			LOG.error("Cannot access to LDAP");
		}
		res = emailAddress;
		return res;
	}

}
