/*
 * LdapLoginModule
 *  
 * Created on Nov 20, 2004
 *
 * Ricardo LEAL
 * ESRF - European Synchrotron Radiation Facility
 * B.P. 220
 * 38043 Grenoble Cedex - France
 * Phone: 00 33 (0)4 38 88 19 59
 * Fax: 00 33 (0)4 76 88 25 42
 * ricardo.leal@esrf.fr
 * 
 */
package ispyb.server.security;

import ispyb.common.util.Constants;

import java.util.ArrayList;
import java.util.Properties;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import org.apache.log4j.Logger;

/**
 * 
 * @author Alberto Nardella alberto.nardella@maxlab.lu.se
 * @version
 */
public class LdapSearchModule {

	private final Logger LOG = Logger.getLogger(LdapSearchModule.class);

	public LdapSearchModule() {

	}
	
	public ArrayList<String> getUserGroups(String username) throws NamingException {
		ArrayList<String> result = new ArrayList<String>();

		Properties env = new Properties();
		// Map all option into the JNDI InitialLdapContext env
		env.put("principalDNSuffix", "," +Constants.LDAP_people);
		env.put("principalDNPrefix", "uid=");
		env.put("allowEmptyPasswords", true);
		env.put("java.naming.security.authentication", "simple");
		env.put("groupCtxDN", Constants.LDAP_base);
		env.put("java.naming.provider.url", Constants.LDAP_Employee_Resource);
		env.put("groupUniqueMember", "uniqueMember");
		env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		env.put("groupAttributeID", "cn");

		// build user
		String principalDNPrefix = "uid=";
		String principalDNSuffix = "," +Constants.LDAP_people;
		String userDN = principalDNPrefix + username + principalDNSuffix;
		
		// Connects to server
		LOG.debug("Logging into LDAP server");
		InitialLdapContext ctx = new InitialLdapContext(env, null);
		LOG.debug("Logged into LDAP server");

		// Search for Groups/Roles the user belongs
		String groupCtxDN = Constants.LDAP_base;

		// Search for any roles associated with the user
		if (groupCtxDN != null) {

			String groupUniqueMemberName = "uniqueMember";
			String groupAttrName = "cn";

			try {

				// Set up criteria to search on
				// e.g. (&(objectClass=groupOfUniqueNames)(uniqueMember=uid=ifx999,ou=People,dc=esrf,dc=fr))
				String filter = new StringBuffer().append("(&").append("(objectClass=groupOfUniqueNames)")
						.append("(" + groupUniqueMemberName + "=").append(userDN).append(")").append(")").toString();
				// Set up search constraints
				SearchControls cons = new SearchControls();
				cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
				// Search
				LOG.debug("Searching for groups in LDAP");
				NamingEnumeration<SearchResult> answer = ctx.search(groupCtxDN, filter, cons);
				while (answer.hasMore()) {
					SearchResult sr = (SearchResult) answer.next();
					Attributes attrs = sr.getAttributes();

					Attribute groups = attrs.get(groupAttrName);
					for (int r = 0; r < groups.size(); r++) {
						Object value = groups.get(r);
						String groupName = null;
						groupName = value.toString();
						// fill groups array
						if (groupName != null && (groupName.startsWith(Constants.LOGIN_PREFIX_MX) || groupName.startsWith(Constants.LOGIN_PREFIX_IFX))) {
							LOG.debug("Add "  + groupName +" to the group list");
							result.add(groupName);
						}
					}
				}
				
				LOG.debug("Search finished");
				
			} catch (NamingException e) {
				LOG.debug("Failed to locate groups", e);
			}

		}
		// Close the context to release the connection
		ctx.close();
		
		return result;
	}
}