package bcr.client.security.util;

import java.security.acl.Group;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.auth.spi.UsernamePasswordLoginModule;

/**
 * An implementation of LoginModule that authenticates against an LDAP server using JNDI, based on the configuration
 * properties.
 * <p>
 * The LoginModule options include whatever options your LDAP JNDI provider supports. Examples of standard property
 * names are:
 * <ul>
 * <li>
 * <code>Context.INITIAL_CONTEXT_FACTORY = "java.naming.factory.initial"</code>
 * <li><code>Context.SECURITY_PROTOCOL = "java.naming.security.protocol"</code>
 * <li><code>Context.PROVIDER_URL = "java.naming.provider.url"</code>
 * <li>
 * <code>Context.SECURITY_AUTHENTICATION = "java.naming.security.authentication"</code>
 * </ul>
 * <p>
 * 
 * @author LEAL
 * @version
 */
public class LdapLoginModule extends UsernamePasswordLoginModule {

	private static Logger LOG = Logger.getLogger(LdapLoginModule.class);

	// default role. If a user has a valid password and no Pxgroup assigned,
	// so it will be a User. This might be changed on the future.
	private static final String DEFAULT_GROUP = "User";

	private static final String PRINCIPAL_DN_PREFIX_OPT = "principalDNPrefix";

	private static final String PRINCIPAL_DN_SUFFIX_OPT = "principalDNSuffix";

	private static final String ALLOW_EMPTY_PASSWORDS_OPT = "allowEmptyPasswords";

	// ou=Pxwebgroups,dc=esrf,dc=fr
	private static final String GROUP_CTX_DN_OPT = "groupCtxDN";

	// cn
	private static final String GROUP_ATTRIBUTE_ID_OPT = "groupAttributeID";

	// uniqueMember
	private static final String GROUP_UNIQUE_MEMBER_OPT = "groupUniqueMember";

	private transient SimpleGroup userRoles = new SimpleGroup("Roles");

	public LdapLoginModule() {

	}

	/**
	 * Overriden to return an empty password string as typically one cannot obtain a user's password. We also override
	 * the validatePassword so this is ok.
	 * 
	 * @return and empty password String
	 */
	@Override
	protected String getUsersPassword() throws LoginException {
		return "";
	}

	/**
	 * Overriden by subclasses to return the Groups that correspond to the to the role sets assigned to the user.
	 * Subclasses should create at least a Group named "Roles" that contains the roles assigned to the user. A second
	 * common group is "CallerPrincipal" that provides the application identity of the user rather than the security
	 * domain identity.
	 * 
	 * @return Group[] containing the sets of roles
	 */
	@Override
	protected Group[] getRoleSets() throws LoginException {
		Group[] roleSets = { userRoles };
		return roleSets;
	}

	/**
	 * Validate the inputPassword by creating a ldap InitialContext with the SECURITY_CREDENTIALS set to the password.
	 * 
	 * @param inputPassword
	 *            the password to validate.
	 * @param expectedPassword
	 *            ignored
	 */
	@Override
	protected boolean validatePassword(String inputPassword, String expectedPassword) {

		boolean isValid = false;

		if (inputPassword != null) {
			// See if this is an empty password that should be disallowed
			if (inputPassword.length() == 0) {
				// Check for an allowEmptyPasswords option
				boolean allowEmptyPasswords = true;
				String flag = (String) options.get(ALLOW_EMPTY_PASSWORDS_OPT);
				if (flag != null)
					allowEmptyPasswords = Boolean.valueOf(flag).booleanValue();
				if (allowEmptyPasswords == false) {
					LOG.debug("Rejecting empty password due to allowEmptyPasswords");
					return false;
				}
			}

			try {
				// Validate the password by trying to create an initial context
				String username = getUsername();
				createLdapInitContext(username, inputPassword);
				isValid = true;
			} catch (NamingException e) {
				LOG.error("Failed to validate password", e);
			}
		}
		return isValid;
	}

	/**
	 * Create LDAP context, connects to server and fill the Roles/Groups array
	 * 
	 * @param username
	 * @param credential
	 * @throws NamingException
	 */
	private void createLdapInitContext(String username, Object credential) throws NamingException {

		Properties env = new Properties();
		// Map all option into the JNDI InitialLdapContext env
		Iterator iter = options.entrySet().iterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			env.put(entry.getKey(), entry.getValue());
		}

		// build user
		String principalDNPrefix = (String) options.get(PRINCIPAL_DN_PREFIX_OPT);
		String principalDNSuffix = (String) options.get(PRINCIPAL_DN_SUFFIX_OPT);
		String userDN = principalDNPrefix + username + principalDNSuffix;

		env.setProperty(Context.SECURITY_PRINCIPAL, userDN);
		env.put(Context.SECURITY_CREDENTIALS, credential);

		// Connects to server
		// Avoid having user password in logs... ServerLogger.getInstance().debug("Logging into LDAP server, env=" +
		// env);
		LOG.debug("Logging into LDAP server");
		InitialLdapContext ctx = new InitialLdapContext(env, null);
		LOG.debug("Logged into LDAP server");

		// Search for Groups/Roles the user belongs
		String groupCtxDN = (String) options.get(GROUP_CTX_DN_OPT);

		// Search for any roles associated with the user
		if (groupCtxDN != null) {

			String groupUniqueMemberName = (String) options.get(GROUP_UNIQUE_MEMBER_OPT);
			String groupAttrName = (String) options.get(GROUP_ATTRIBUTE_ID_OPT);

			try {

				// Set up criteria to search on
				// e.g. (&(objectClass=groupOfUniqueNames)(uniqueMember=uid=ifx999,ou=People,dc=esrf,dc=fr))
				String filter = new StringBuffer().append("(&").append("(objectClass=groupOfUniqueNames)")
						.append("(" + groupUniqueMemberName + "=").append(userDN).append(")").append(")").toString();
				// Set up search constraints
				SearchControls cons = new SearchControls();
				cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
				// Search
				NamingEnumeration answer = ctx.search(groupCtxDN, filter, cons);
				while (answer.hasMore()) {
					SearchResult sr = (SearchResult) answer.next();
					Attributes attrs = sr.getAttributes();
					Attribute roles = attrs.get(groupAttrName);
					for (int r = 0; r < roles.size(); r++) {
						Object value = roles.get(r);
						String roleName = null;
						roleName = value.toString();
						// fill roles array
						if (roleName != null) {
							LOG.debug("Assign user to role " + roleName);
							userRoles.addMember(new SimplePrincipal(roleName));
						}

					}
				}
				// if there's no Role for this username, so it will be a User
				if (!userRoles.members().hasMoreElements())
					userRoles.addMember(new SimplePrincipal(DEFAULT_GROUP));
			} catch (NamingException e) {
				LOG.debug("Failed to locate roles", e);
			}

		}
		// Close the context to release the connection
		ctx.close();
	}
}