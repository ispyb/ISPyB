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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.acl.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.kerberos.KerberosKey;
import javax.security.auth.kerberos.KerberosTicket;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;

public class DLSAuthorisationLoginModule implements LoginModule {

	private final Logger LOG = Logger.getLogger(DLSAuthorisationLoginModule.class);

	// initialize(..) parameters
	protected Subject subject;

	protected CallbackHandler callbackHandler;

	protected Map sharedState;

	protected Map options;

	protected static final String PRINCIPAL_DN_PREFIX_OPT = "principalDNPrefix";

	protected static final String PRINCIPAL_DN_SUFFIX_OPT = "principalDNSuffix";

	protected static final String OBJECT_CLASS_OPT = "objectClass";

	protected static final String GROUP_CTX_DN_OPT = "groupCtxDN";

	protected static final String GROUP_ATTRIBUTE_ID_OPT = "groupAttributeID";

	protected static final String PERSON_PRINCIPAL_DN_PREFIX_OPT = "personPrincipalDNPrefix";

	protected static final String PERSON_OBJECT_CLASS_OPT = "personObjectClass";

	protected static final String PEOPLE_CTX_DN_OPT = "peopleCtxDN";

	protected static final String PEOPLE_ATTRIBUTE_ID_OPT = "peopleAttributeID";

	protected transient SimpleGroup userRoles = new SimpleGroup("Roles");

	protected StringBuffer krb5PrincName = null;

	protected String username;

	protected static final String NAME = "javax.security.auth.login.name";

	protected static final java.util.ResourceBundle rb = java.util.ResourceBundle
			.getBundle("sun.security.util.AuthResources");

	protected boolean succeeded = false;

	protected boolean commitSucceeded = false;

	// Module options
	protected boolean debug = false;

	protected boolean storeKey = false;

	protected boolean useTicketCache = false;

	protected String ticketCacheName = null;

	protected boolean useFirstPass = false;

	protected boolean tryFirstPass = false;

	protected boolean storePass = false;

	protected boolean clearPass = false;

	/** The JNDI name of the DataSource to use */
	protected String dsJndiName = "java:/oracle/ispyb_db";

	/** The sql query to obtain the user roles */
	protected String visitsQuery = "select lower(i.visit_id), 'Roles' " + "from investigation@icatdls33 i "
			+ "inner join investigator@icatdls33 itor on i.id = itor.investigation_id "
			+ "inner join facility_user@icatdls33 fu on fu.facility_user_id = itor.facility_user_id "
			+ "where fu.federal_id=?";

	/**
	 * Initialise DLSAuthorisationLoginModule.
	 * 
	 * @param subject
	 *            the Subject to be authenticated.
	 * @param callbackHandler
	 *            a CallbackHandler for prompting the end user e.g. for usernames and passwords.
	 * @param sharedState
	 *            shared LoginModule state.
	 * @param options
	 *            options specified in the login configuration for DLSAuthorisationLoginModule.
	 */
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {

		this.subject = subject;
		this.callbackHandler = callbackHandler;
		this.sharedState = sharedState;
		this.options = options;

		// Initialise configured options
		debug = "true".equalsIgnoreCase((String) options.get("debug"));
		storeKey = "true".equalsIgnoreCase((String) options.get("storeKey"));
		useTicketCache = "true".equalsIgnoreCase((String) options.get("useTicketCache"));
		ticketCacheName = (String) options.get("ticketCache");
		tryFirstPass = "true".equalsIgnoreCase((String) options.get("tryFirstPass"));
		useFirstPass = "true".equalsIgnoreCase((String) options.get("useFirstPass"));
		storePass = "true".equalsIgnoreCase((String) options.get("storePass"));
		clearPass = "true".equalsIgnoreCase((String) options.get("clearPass"));

		Object tmp = options.get("dsJndiName");
		if (tmp != null)
			dsJndiName = tmp.toString();
		tmp = options.get("visitsQuery");
		if (tmp != null)
			visitsQuery = tmp.toString();

		if (debug) {
			LOG.debug("Initialize parameters: ");
			LOG.debug("  subject: " + subject.toString());
			LOG.debug("  callbackHandler: " + callbackHandler.toString());
			LOG.debug("  sharedState: " + sharedState.toString());
			LOG.debug("  options: " + " debug: " + debug + ", storeKey: " + storeKey + ", useTicketCache: "
					+ useTicketCache + ", ticketCache: " + ticketCacheName + ", tryFirstPass: " + tryFirstPass
					+ ", useFirstPass: " + useFirstPass + ", storePass: " + storePass + ", clearPass: " + clearPass
					+ ", dsJndiName: " + dsJndiName + ", visitsQuery: " + visitsQuery + "\n");
		}
	}

	public boolean login() throws LoginException {
		succeeded = true;
		promptForUsername(true);
		// Get roles (admin, manager, blom, etc.) from LDAP
		getUserRolesFromLDAP();
		// Get visits from ICAT through database link
		getVisitsFromICAT(krb5PrincName.toString().toLowerCase());
		return true;
	}

	protected void getVisitsFromICAT(String username) throws LoginException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			InitialContext ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(dsJndiName);
			conn = ds.getConnection();
			// Get the password
			ps = conn.prepareStatement(visitsQuery);
			ps.setString(1, username);
			rs = ps.executeQuery();

			while (rs.next()) {
				String visit = rs.getString(1);
				userRoles.addMember(new SimplePrincipal(visit));
				LOG.debug("Added role " + visit);
				int prospoalLength = visit.indexOf("_");
				if (prospoalLength == -1) {
					prospoalLength = visit.indexOf("-");
				}
				if (prospoalLength != -1) {
					String proposal = visit.substring(0, prospoalLength);
					SimplePrincipal proposalPrincipal = new SimplePrincipal(proposal);
					if (!userRoles.isMember(proposalPrincipal)) {
						userRoles.addMember(proposalPrincipal);
						LOG.debug("Added role " + proposal);
					}
				}
			}
		} catch (NamingException ex) {
			throw new LoginException(ex.toString(true));
		} catch (SQLException ex) {
			LOG.error("Query failed", ex);
			throw new LoginException(ex.toString());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
				}
			}
		}
	}

	protected void getUserRolesFromLDAP() throws LoginException {
		try {
			Properties env = new Properties();
			// Map all options into the JNDI InitialLdapContext env
			Iterator iter = options.entrySet().iterator();
			while (iter.hasNext()) {
				Entry entry = (Entry) iter.next();
				env.put(entry.getKey(), entry.getValue());
			}

			// User groups
			SimplePrincipal userGroup = new SimplePrincipal(Constants.ROLE_USER);
			SimplePrincipal adminGroup = new SimplePrincipal(Constants.ROLE_ADMIN);
			SimplePrincipal localcontactGroup = new SimplePrincipal(Constants.ROLE_LOCALCONTACT);
			SimplePrincipal managerGroup = new SimplePrincipal(Constants.ROLE_MANAGER);
			SimplePrincipal storeGroup = new SimplePrincipal(Constants.ROLE_STORE);
			SimplePrincipal fedexmanagerGroup = new SimplePrincipal(Constants.FXMANAGE_ROLE_NAME);
			SimplePrincipal industrialGroup = new SimplePrincipal(Constants.ROLE_INDUSTRIAL);
			SimplePrincipal blomGroup = new SimplePrincipal(Constants.ROLE_BLOM);
			SimplePrincipal webserviceGroup = new SimplePrincipal("WebService");

			// build user
			String principalDNPrefix = (String) options.get(PRINCIPAL_DN_PREFIX_OPT);
			String principalDNSuffix = (String) options.get(PRINCIPAL_DN_SUFFIX_OPT);
			String userDN = principalDNPrefix + krb5PrincName.toString().toLowerCase() /* username */
					+ principalDNSuffix;
			String fedid = krb5PrincName.toString().toLowerCase();

			// env.setProperty(Context.SECURITY_PRINCIPAL, userDN);
			// env.put(Context.SECURITY_CREDENTIALS, credential);

			Properties prop = new Properties();
			String userMap = "dls-users.properties";

			final String JBOSS_SERVER_CONFIG_URL = "jboss.server.config.url";
			String confDir = System.getProperty(JBOSS_SERVER_CONFIG_URL);
			if (confDir != null) {
				URL url = null;
				try {
					url = new URL(confDir + userMap);
				} catch (MalformedURLException mue) {
					LOG.error("Unable to create URL for " + confDir + userMap, mue);
				}
				try {
					// Load properties file
					prop.load(url.openStream());
					String[] usergroups = prop.getProperty(fedid).split(",");
					for (String usergroup : usergroups) {
						SimplePrincipal principal = new SimplePrincipal(usergroup);
						userRoles.addMember(principal);
						LOG.debug("Added role from " + confDir + userMap + ": " + usergroup);
					}
				} catch (IOException ioe) {
					LOG.error("Unable to load file " + confDir + userMap);
				}
			} else
				LOG.error("Unable get system property " + JBOSS_SERVER_CONFIG_URL);

			// Connects to server
			LOG.debug("Logging into LDAP server, env=" + env);
			InitialLdapContext ctx = new InitialLdapContext(env, null);
			LOG.debug("Logged into LDAP server");

			// Search for Groups/Roles to which the user belongs
			String groupCtxDN = (String) options.get(GROUP_CTX_DN_OPT);

			// Search for any roles associated with the user
			if (groupCtxDN != null) {

				String objectClass = (String) options.get(OBJECT_CLASS_OPT);
				String groupAttrName = (String) options.get(GROUP_ATTRIBUTE_ID_OPT);

				try {
					// Set up criteria on which to search
					// e.g.
					// (&(objectClass=groupOfUniqueNames)(uniqueMember=uid=ifx999,ou=People,dc=esrf,dc=fr))
					String filter = new StringBuffer().append("(&").append("(objectClass=" + objectClass + ")" + "(")
							.append(userDN).append(")").append(")").toString();

					LOG.debug("LDAP filter: " + filter);

					// Set up search constraints
					SearchControls cons = new SearchControls();
					cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
					// Search
					NamingEnumeration answer = ctx.search(groupCtxDN, filter, cons);
					// ^^ Somewhat equivalent of:
					// ldapsearch -x -LL -s one -b
					// ou=group,dc=diamond,dc=ac,dc=uk
					// "(&(objectClass=posixGroup)(memberUid=my_fedID_here))" uid

					while (answer.hasMore()) {

						SearchResult sr = (SearchResult) answer.next();
						Attributes attrs = sr.getAttributes();
						Attribute roles = attrs.get(groupAttrName);
						for (int r = 0; r < roles.size(); r++) {

							Object value = roles.get(r);
							String roleName = null;
							roleName = value.toString();
							// Fill userRoles
							if (roleName != null) {
								// Admins: dls_dasc, mx_staff
								if (roleName.equals("dls_dasc") || roleName.equals("mx_staff")) {
									userRoles.addMember(adminGroup);
									LOG.debug("Added role " + Constants.ROLE_ADMIN);

									if (!userRoles.isMember(webserviceGroup)) {
										userRoles.addMember(webserviceGroup);
										LOG.debug("Added role WebService");
									}
								}
							}
						}
					}

					if (!userRoles.isMember(userGroup)) {
						userRoles.addMember(userGroup);
						LOG.debug("Added role " + Constants.ROLE_USER);
					}

				} catch (NamingException e) {
					LOG.debug("Failed to locate roles", e);
				}
			}

			// Get full name
			// Somewhat equivalent of:
			// ldapsearch -x -LL -s sub -b ou=People,dc=diamond,dc=ac,dc=uk
			// "(&(objectClass=person)(uid=my_fedID_here))" cn

			String personPrincipalDNPrefix = (String) options.get(PERSON_PRINCIPAL_DN_PREFIX_OPT);
			String personUserDN = personPrincipalDNPrefix + krb5PrincName.toString().toLowerCase() /* username */;

			// Search for Groups/Roles to which the user belongs
			String peopleCtxDN = (String) options.get(PEOPLE_CTX_DN_OPT);

			// Search for any roles associated with the user
			if (peopleCtxDN != null) {

				String personObjectClass = (String) options.get(PERSON_OBJECT_CLASS_OPT);
				String peopleAttrName = (String) options.get(PEOPLE_ATTRIBUTE_ID_OPT);

				try {
					// Set up criteria on which to search
					// e.g.
					// (&(objectClass=groupOfUniqueNames)(uniqueMember=uid=ifx999,ou=People,dc=esrf,dc=fr))
					String filter = new StringBuffer().append("(&")
							.append("(objectClass=" + personObjectClass + ")" + "(").append(personUserDN).append(")")
							.append(")").toString();

					LOG.debug("LDAP filter: " + filter);

					// Set up search constraints
					SearchControls cons = new SearchControls();
					cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
					// Search
					NamingEnumeration answer = ctx.search(peopleCtxDN, filter, cons);

					LOG.debug("ctx.search ok");

					while (answer.hasMore()) {

						LOG.debug("ctx.search has answers");

						SearchResult sr = (SearchResult) answer.next();
						Attributes attrs = sr.getAttributes();
						Attribute names = attrs.get(peopleAttrName);
						for (int n = 0; n < names.size(); n++) {

							LOG.debug("ctx.search answers have names");

							Object value = names.get(n);
							String name = null;
							name = value.toString();

							LOG.debug("The name = " + name);

							// Fill userRoles
							if (name != null) {
								// options.put(Constants.FULLNAME, name);
								// .setAttribute(Constants.FULLNAME, name);
								userRoles.addMember(new SimplePrincipal("name:" + name)); // .replace(" ", "_")));
								LOG.debug("Added name " + name);
							}
						}
					}

				} catch (NamingException e) {
					LOG.debug("Failed to locate name", e);
				}
			}

			// Close the context to release the connection
			ctx.close();

		} catch (NamingException e) {
			LOG.error("Failed to validate password", e);
		}
	}

	/**
	 * Get the username either from shared state or prompt
	 */
	protected void promptForUsername(boolean getUsernameFromSharedState) throws LoginException {
		krb5PrincName = new StringBuffer("");
		if (getUsernameFromSharedState) {
			// use the name saved by the first module in the stack
			username = (String) sharedState.get(NAME);
			if (debug)
				LOG.debug("username from shared state is " + username + "\n");

			if (username == null)
				throw new LoginException("Username can not be obtained from sharedstate ");

			if (debug)
				LOG.debug("username from shared state is " + username + "\n");

			if (username != null && username.length() > 0) {
				krb5PrincName.insert(0, username);
				return;
			}
		}

		if (callbackHandler == null)
			throw new LoginException("No CallbackHandler " + "available " + "to garner authentication "
					+ "information from the user");
		try {
			String defUsername = System.getProperty("user.name");

			Callback[] callbacks = new Callback[1];
			MessageFormat form = new MessageFormat(rb.getString("Kerberos username [[defUsername]]: "));
			Object[] source = { defUsername };
			callbacks[0] = new NameCallback(form.format(source));
			callbackHandler.handle(callbacks);
			username = ((NameCallback) callbacks[0]).getName();
			if (username == null || username.length() == 0)
				username = defUsername;
			krb5PrincName.insert(0, username);
		} catch (java.io.IOException ioe) {
			throw new LoginException(ioe.getMessage());
		} catch (UnsupportedCallbackException uce) {
			throw new LoginException(uce.getMessage()
					+ " not available to garner authentication information from the user");
		}
	}

	/**
	 * Adds the user roles to the Subject's Principals.
	 * 
	 * @exception LoginException
	 *                if the method fails.
	 * @return true if both the login and commit methods succeeded, or false otherwise.
	 */

	public boolean commit() throws LoginException {
		// Add the Krb5 Creds to the Subject's private credentials. The
		// credentials are of type
		// KerberosKey or KerberosTicket

		if (succeeded == false)
			return false;
		else if (subject.isReadOnly())
			throw new LoginException("Subject is Readonly");

		// Add the user roles to the Subject's principal set
		Set princSet = subject.getPrincipals();
		if (!princSet.contains(userRoles))
			princSet.add(userRoles);

		commitSucceeded = true;
		if (debug)
			LOG.debug("Commit Succeeded \n");
		return true;
	}

	/**
	 * This method is called if the LoginContext's overall authentication failed. (the relevant REQUIRED, REQUISITE,
	 * SUFFICIENT and OPTIONAL LoginModules did not succeed).
	 * 
	 * If this LoginModule's own authentication attempt succeeded (checked by retrieving the private state saved by the
	 * login and commit methods), then this method cleans up any state that was originally saved.
	 * 
	 * @exception LoginException
	 *                if the abort fails.
	 * @return false if this LoginModule's own login and/or commit attempts failed, and true otherwise.
	 */

	public boolean abort() throws LoginException {
		if (succeeded == false) {
			return false;
		} else if (succeeded == true && commitSucceeded == false) {
			// login succeeded but overall authentication failed
			succeeded = false;
		} else {
			// overall authentication succeeded and commit succeeded,
			// but someone else's commit failed
			logout();
		}
		return true;
	}

	/**
	 * Logout the user. This method removes the user roles that were added by the commit method.
	 * 
	 * @exception LoginException
	 *                if the logout fails.
	 * @return true in all cases since this LoginModule should not be ignored.
	 */
	public boolean logout() throws LoginException {
		if (subject.isReadOnly()) {
			throw new LoginException("Subject is Readonly");
		}

		// Remove all Kerberos credentials stored in the Subject
		Iterator it = subject.getPrivateCredentials().iterator();
		while (it.hasNext()) {
			Object o = it.next();

			if (o instanceof KerberosTicket || o instanceof KerberosKey || (o instanceof Group && o.equals(userRoles))) {
				it.remove();
			}
		}

		Set princSet = subject.getPrincipals();
		princSet.remove(userRoles);

		succeeded = false;
		commitSucceeded = false;
		if (debug) {
			LOG.debug("[DLSAuthorisationLoginModule]: logged out Subject");
		}
		return true;
	}

}
