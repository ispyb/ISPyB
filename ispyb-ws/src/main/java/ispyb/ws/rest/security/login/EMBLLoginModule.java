package ispyb.ws.rest.security.login;

import ispyb.ws.rest.security.AuthenticationRestWebService;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import org.apache.log4j.Logger;

public class EMBLLoginModule{
	private final static Logger logger = Logger.getLogger(EMBLLoginModule.class);
	
	private static String groupUniqueMemberName = "member";
	private static String principalDNSuffix = ",ou=Internal,ou=People,dc=embl-hamburg,dc=de";
	private  static String groupCtxDN = "ou=Pxwebgroups,dc=embl-hamburg,dc=de";
	private  static String principalDNPrefix = "uid=";
	private  static String groupAttributeID = "cn";
	private  static String server = "ldaps://services:636/";
	private  static String socketFactory = "ispyb.ws.rest.security.login.SmisSSLSocketFactory";
	
	private static Properties getConnectionProperties(String username, String password){
		Properties env = new Properties();
		env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		env.put("useTls", "true");
		env.put("java.naming.security.protocol", "ssl");
		env.put("tlsHostnameVerifierClass", "ispyb.ws.rest.security.login.IgnoringHostnameVerifier");
		env.put("sharedAttributeID", "uidNumber");
		env.put("java.naming.ldap.factory.socket", socketFactory);
		env.put("principalDNPrefix", principalDNPrefix);
		env.put("groupAttributeID", groupAttributeID);
		env.put("groupCtxDN", groupCtxDN);
		env.put("principalDNSuffix", principalDNSuffix);
		env.put("allowEmptyPasswords", "false");
		env.put("groupUniqueMember", groupUniqueMemberName);
		env.put("jboss.security.security_domain", "ispyb");
		env.put("java.naming.provider.url", server);
		env.put("java.naming.security.authentication", "simple");
		env.put("java.naming.security.credentials", password);
		return env;
	}
	
	private static Properties getInternalConnectionProperties(String username, String password){
			Properties env = EMBLLoginModule.getConnectionProperties(username, password);
			env.put("java.naming.security.principal", "uid=" + username + ",ou=Internal,ou=People,dc=embl-hamburg,dc=de");
			return env;
	}
	
	private static Properties getVisitorConnectionProperties(String username, String password){
		Properties env = EMBLLoginModule.getConnectionProperties(username, password);
		env.put("java.naming.security.principal", "uid=" + username + ",ou=Visitor,ou=People,dc=embl-hamburg,dc=de");
		env.put("principalDNSuffix", ",ou=visitor,ou=people,dc=embl-hamburg,dc=de");
		env.put("useTls", "false");
		return env;
	}
	
	
	public static String getFilter(String username){
		String userDN = principalDNPrefix + username + principalDNSuffix;
		return new StringBuffer().append("(&").append("(objectClass=groupOfNames)").append("(" + groupUniqueMemberName + "=").append(userDN).append(")").append(")").toString();
		
	}
	
		
	private static List<String> authenticateAA(String username, String password, Properties properties) throws NamingException {
		List<String> myRoles = new ArrayList<String>();
//		InitialLdapContext ctx = new InitialLdapContext(getConnectionProperties(username, password), null);
		InitialLdapContext ctx = new InitialLdapContext(properties, null);
		// Set up search constraints
		SearchControls cons = new SearchControls();
		cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
		// Search
		NamingEnumeration<SearchResult> answer = ctx.search(groupCtxDN, getFilter(username),cons);
		while (answer.hasMore()) {
			SearchResult sr = answer.next();
			Attributes attrs = sr.getAttributes();
			Attribute roles = attrs.get(groupAttributeID);
			for (int r = 0; r < roles.size(); r++) {
				Object value = roles.get(r);
				String roleName = null;
				roleName = value.toString();
				// fill roles array
				if (roleName != null) {
					myRoles.add(roleName);
				}

			}
		}
		if (myRoles.size() == 0){
			myRoles.add("User");
		}
		
		return myRoles;
	}
	
	public static List<String> authenticate(String username, String password) throws NamingException {
		try{
			logger.info("Trying to authenticate as staff (internal)");
			return authenticateAA(username, password, EMBLLoginModule.getInternalConnectionProperties(username, password));
		}
		catch(Exception e){
			logger.info("Failed to authenticate as staff (internal");
			//race();
		}
		logger.info("Trying to authenticate as user");
		try {
			return authenticateAA(username, password, EMBLLoginModule.getVisitorConnectionProperties(username, password));
		}
		catch(Exception e){
			logger.info("Failed to authenticate as staff (internal");
			//e.printStackTrace();
		}
		return null;
	}
}

