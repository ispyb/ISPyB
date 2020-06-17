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
 ******************************************************************************************************************************/


/**
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
import ispyb.common.util.PropertyLoader;

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

import static ispyb.common.util.Constants.SITE_IS_MAXIV;
import static ispyb.common.util.Constants.getSite;

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

	private static final String LDAP_FACTORY_SOCKET = "socketFactory";
	
	private final Logger LOG = Logger.getLogger(LdapLoginModule.class);

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
				
				/** if there's no Role for this username, so it will be a User **/
				if (!userRoles.members().hasMoreElements())
						userRoles.addMember(new SimplePrincipal(DEFAULT_GROUP));
				return true;
				
			} catch (NamingException e) {
				
				LOG.error("Failed to validate password", e);
			}
		}
		return false;
	}

	/**
	 * validateCredentials Validate the inputPassword by creating a ldap InitialContext with the SECURITY_CREDENTIALS
	 * set to the password.
	 * 
	 * @param password
	 *            the password to validate.
	 */
	public boolean validateCredentials(String username, String password) {

		boolean isValid = false;
		try {
			if (password != null) {
				// See if this is an empty password that should be disallowed
				if (password.length() == 0) {
					// Check for an allowEmptyPasswords option
					boolean allowEmptyPasswords = true;
					String flag = (String) options.get(ALLOW_EMPTY_PASSWORDS_OPT);
					if (flag != null)
						allowEmptyPasswords = Boolean.valueOf(flag).booleanValue();
					if (allowEmptyPasswords == false) {
						LOG.debug("[validateCredentials] Rejecting empty password due to allowEmptyPasswords");
						return false;
					}
				}

				// --------------------- Validate the password by trying to create an initial context
				// -----------------------
				Properties mProp = PropertyLoader.loadProperties("ispyb/server/webservice/WebService/conf/ISPyB");
				Properties env = new Properties();
				// Map all option into the JNDI InitialLdapContext env
				env.put("principalDNSuffix", mProp.getProperty("principalDNSuffix"));
				env.put("principalDNPrefix", mProp.getProperty("principalDNPrefix"));
				env.put("allowEmptyPasswords", mProp.getProperty("allowEmptyPasswords"));
				env.put("java.naming.security.authentication", mProp.getProperty("java.naming.security.authentication"));
				env.put("groupCtxDN", mProp.getProperty("groupCtxDN"));
				env.put("java.naming.provider.url", mProp.getProperty("java.naming.provider.url"));
				env.put("groupUniqueMember", mProp.getProperty("groupUniqueMember"));
				env.put("java.naming.factory.initial", mProp.getProperty("java.naming.factory.initial"));
				env.put("groupAttributeID", mProp.getProperty("groupAttributeID"));

				
				// build user
				String principalDNPrefix = mProp.getProperty("principalDNPrefix");
				String principalDNSuffix = mProp.getProperty("principalDNSuffix");
				String userDN = principalDNPrefix + username + principalDNSuffix;
				
				
				env.setProperty(Context.SECURITY_PRINCIPAL, userDN);
				env.put(Context.SECURITY_CREDENTIALS, password);

				/** Added socketFactory for authentication by SSL **/
				String socketFactory = (String) options.get(LDAP_FACTORY_SOCKET);
				if (socketFactory != null) {
					env.put("java.naming.ldap.factory.socket", socketFactory);
				}
				
				// Connects to server
				// Avoid having user password in logs... LOG.debug("Logging into LDAP server, env=" + env);
				LOG.debug("Logging into LDAP server");
				InitialLdapContext ctx = new InitialLdapContext(env, null);
				LOG.debug("Logged into LDAP server");
				ctx.close();
				// ----------------------------------------------------------------------------------------------------------
				isValid = true;
			}

		} catch (NamingException e) {
			LOG.error("[validateCredentials] Failed to validate Credentials", e);
			isValid = false;
		} catch (Exception e) {
			isValid = false;
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
		String optionsStr = options.toString();

		env.setProperty(Context.SECURITY_PRINCIPAL, userDN);
		env.put(Context.SECURITY_CREDENTIALS, credential);
		if (Constants.SITE_IS_MAXIV()){
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put("jboss.security.security_domain", "ispyb");
			env.put("allowEmptyPasswords", "false");
			//LOG.info("Env:" + env);
		}
		

		// Connects to server
		// Avoid having user password in logs... LOG.debug("Logging into LDAP server, env=" + env);
		InitialLdapContext ctx = null;
		LOG.debug("Logging into LDAP server");
		try {
			ctx = new InitialLdapContext(env, null);
		}catch (Exception ex){
			if (Constants.SITE_IS_MAXIV() && username.equals("ispyb")) {
				LOG.info("Env:" + env);
			}
			throw ex;
		}
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

				String filter = new StringBuffer().append("(&").append("(objectClass=groupOfUniqueNames)").append("(" + groupUniqueMemberName + "=").append(userDN).append(")").append(")").toString();
				
				switch (Constants.getSite()) {
				case EMBL:
					filter = new StringBuffer().append("(&").append("(objectClass=groupOfNames)").append("(" + groupUniqueMemberName + "=").append(userDN).append(")").append(")").toString();
					break;
				case ALBA:
					filter = new StringBuffer().append("(&").append("(objectClass=posixGroup)").append("(" + groupUniqueMemberName + "=").append(username).append(")").append(")").toString();
				case MAXIV:
						try {

							SearchControls constraints = new SearchControls();
							constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
							String[] attrIDs = { "distinguishedName"};
							constraints.setReturningAttributes(attrIDs);
							//First input parameter is search bas, it can be "CN=Users,DC=YourDomain,DC=com"
							//Second Attribute can be uid=username
							NamingEnumeration answer = ctx.search(Constants.LDAP_people, "sAMAccountName="
									+ username, constraints);
							if (answer.hasMore()) {
								Attributes attrs = ((SearchResult) answer.next()).getAttributes();
								String dn = attrs.get("distinguishedName").toString().replace("distinguishedName: ", "");
								// (&(objectClass=group)(member=CN=Alberto Nardella,CN=Users,dc=maxlab,dc=lu,dc=se))
								filter = new StringBuffer().append("(&").append("(objectClass=group)").append("(" + groupUniqueMemberName + "=").append(dn).append(")").append(")").toString();
							}else{
								throw new Exception("Invalid User");
							}

						} catch (Exception ex) {
							ex.printStackTrace();
						}

						break;
				default:
					break;
				}
				
				// Set up search constraints
				SearchControls cons = new SearchControls();
				cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
				// Search
				if (Constants.SITE_IS_MAXIV()) {
					NamingEnumeration<SearchResult> answer1 = ctx.search("CN=Users," +groupCtxDN, filter, cons);
					NamingEnumeration<SearchResult> answer2 = ctx.search("OU=DUOactive," +groupCtxDN, filter, cons);
					NamingEnumeration<SearchResult> answer3 = ctx.search("OU=DUO," +groupCtxDN, filter, cons);

					while (answer1.hasMore()) {
						SearchResult sr = answer1.next();
						Attributes attrs = sr.getAttributes();
						Attribute roles = attrs.get(groupAttrName);

						for (int r = 0; r < roles.size(); r++) {
							Object value = roles.get(r);
							String roleName = null;
							roleName = value.toString();
							// fill roles array
							if (roleName != null) {
								LOG.info("Role found for " +username +":" +roleName);
								if (roleName.equals("ispyb-manager") || roleName.equals("ispyb-biomax-contacts") ||
										roleName.equals("Information Management") || roleName.equals("biomax")) {
									userRoles.addMember(new SimplePrincipal(Constants.ROLE_MANAGER));
									userRoles.addMember(new SimplePrincipal(Constants.ROLE_LOCALCONTACT));
									userRoles.addMember(new SimplePrincipal(Constants.ROLE_ADMIN));
									userRoles.addMember(new SimplePrincipal(Constants.ROLE_BLOM));
									userRoles.addMember(new SimplePrincipal(Constants.ROLE_INDUSTRIAL));
									userRoles.addMember(new SimplePrincipal(Constants.ROLE_STORE));
									userRoles.addMember(new SimplePrincipal(DEFAULT_GROUP));
								} else if (roleName.contains("-group")) {
									userRoles.addMember(new SimplePrincipal("mx" + roleName.replace("-group", "")));
									userRoles.addMember(new SimplePrincipal(DEFAULT_GROUP));
								}

								userRoles.addMember(new SimplePrincipal(roleName));
							}

						}
					}

					while (answer2.hasMore()) {
						SearchResult sr = answer2.next();
						Attributes attrs = sr.getAttributes();
						Attribute roles = attrs.get(groupAttrName);

						for (int r = 0; r < roles.size(); r++) {

							Object value = roles.get(r);
							String roleName = null;
							roleName = value.toString();
							// fill roles array
							if (roleName != null) {
								if (roleName.equals("Staff")) {
									userRoles.addMember(new SimplePrincipal(DEFAULT_GROUP));
								} else if (roleName.equals("ispyb-manager")
										|| roleName.equals("Information Management") || roleName.equals("biomax")) {
									userRoles.addMember(new SimplePrincipal(Constants.ALL_MANAGE_ROLE_NAME));
									userRoles.addMember(new SimplePrincipal(Constants.ROLE_LOCALCONTACT));
									userRoles.addMember(new SimplePrincipal(Constants.ROLE_ADMIN));
									userRoles.addMember(new SimplePrincipal(Constants.ROLE_BLOM));
									userRoles.addMember(new SimplePrincipal(Constants.ROLE_INDUSTRIAL));
									userRoles.addMember(new SimplePrincipal(Constants.ROLE_STORE));
								} else if (roleName.equals("ispyb-biomax-contacts")) {
									userRoles.addMember(new SimplePrincipal(Constants.ROLE_LOCALCONTACT));
									userRoles.addMember(new SimplePrincipal(Constants.ALL_MANAGE_ROLE_NAME));
								} else if (roleName.contains("-group")) {
									userRoles.addMember(new SimplePrincipal("mx" + roleName.replace("-group", "")));
									userRoles.addMember(new SimplePrincipal(DEFAULT_GROUP));
								}

								userRoles.addMember(new SimplePrincipal(roleName));
							}

						}
					}

					while (answer3.hasMore()) {
						SearchResult sr = answer3.next();
						Attributes attrs = sr.getAttributes();
						Attribute roles = attrs.get(groupAttrName);

						for (int r = 0; r < roles.size(); r++) {

							Object value = roles.get(r);
							String roleName = null;
							roleName = value.toString();
							// fill roles array
							if (roleName != null) {
								if (roleName.equals("Staff")) {
									userRoles.addMember(new SimplePrincipal(DEFAULT_GROUP));
								} else if (roleName.equals("ispyb-manager")
										|| roleName.equals("Information Management") || roleName.equals("biomax")) {
									userRoles.addMember(new SimplePrincipal(Constants.ALL_MANAGE_ROLE_NAME));
									userRoles.addMember(new SimplePrincipal(Constants.ROLE_LOCALCONTACT));
									userRoles.addMember(new SimplePrincipal(Constants.ROLE_ADMIN));
									userRoles.addMember(new SimplePrincipal(Constants.ROLE_BLOM));
									userRoles.addMember(new SimplePrincipal(Constants.ROLE_INDUSTRIAL));
									userRoles.addMember(new SimplePrincipal(Constants.ROLE_STORE));
								} else if (roleName.equals("ispyb-biomax-contacts")) {
									userRoles.addMember(new SimplePrincipal(Constants.ROLE_LOCALCONTACT));
									userRoles.addMember(new SimplePrincipal(Constants.ALL_MANAGE_ROLE_NAME));
								} else if (roleName.contains("-group")) {
									userRoles.addMember(new SimplePrincipal("mx" + roleName.replace("-group", "")));
									userRoles.addMember(new SimplePrincipal(DEFAULT_GROUP));
								}

								userRoles.addMember(new SimplePrincipal(roleName));
							}

						}
					}

				} else {
					NamingEnumeration<SearchResult> answer = ctx.search(groupCtxDN, filter, cons);

					while (answer.hasMore()) {
						SearchResult sr = answer.next();
						Attributes attrs = sr.getAttributes();

						Attribute roles = attrs.get(groupAttrName);


						for (int r = 0; r < roles.size(); r++) {

							Object value = roles.get(r);
							String roleName = null;
							roleName = value.toString();
							// fill roles array
							if (roleName != null) {
								userRoles.addMember(new SimplePrincipal(roleName));
							}

						}
					}
				}
			} catch (NamingException e) {
				LOG.debug("Failed to locate roles", e);
			}
			

		}
		// Close the context to release the connection
		ctx.close();
	}
}