package ispyb.ws.security.login;

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

public class LoginModule {

    public static List<String> authenticate(String username, String password) throws NamingException{
	List<String> myRoles = new ArrayList<String>();
	String groupUniqueMemberName = "uniqueMember";
	String principalDNSuffix = ",ou=People,dc=esrf,dc=fr";
	String groupCtxDN = "ou=Pxwebgroups,dc=esrf,dc=fr";
	String principalDNPrefix = "uid=";
	String groupAttributeID = "cn";

	Properties env = new Properties();
	env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
	env.put("principalDNPrefix", principalDNPrefix);
	env.put("java.naming.security.principal", "uid=" + username + ",ou=People,dc=esrf,dc=fr");
	env.put("groupAttributeID", groupAttributeID);
	env.put("groupCtxDN", groupCtxDN);
	env.put("principalDNSuffix", principalDNSuffix);

	env.put("allowEmptyPasswords", "false");
	env.put("groupUniqueMember", groupUniqueMemberName);
	env.put("jboss.security.security_domain", "ispyb");
	env.put("java.naming.provider.url", "ldap://ldap.esrf.fr:389/");
	env.put("java.naming.security.authentication", "simple");
	env.put("java.naming.security.credentials", password);

	String userDN = principalDNPrefix + username + principalDNSuffix;

	InitialLdapContext ctx = new InitialLdapContext(env, null);

	String filter = new StringBuffer().append("(&").append("(objectClass=groupOfUniqueNames)")
		.append("(" + groupUniqueMemberName + "=").append(userDN).append(")").append(")").toString();
	// Set up search constraints
	SearchControls cons = new SearchControls();
	cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
	// Search
	NamingEnumeration<SearchResult> answer = ctx.search(groupCtxDN, filter, cons);
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
	return myRoles;
	
	
    }
}
