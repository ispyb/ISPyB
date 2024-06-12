package ispyb.ws.rest.security.login;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import ispyb.common.util.Constants;

public class MAXIVLoginModule {
	
	private	static final String groupUniqueMemberName = "member";
	private	static final String principalDNSuffix = "";
	private	static final String groupCtxDN = Constants.LDAP_people;
	private	static final String principalDNPrefix = Constants.LDAP_prefix;
	private	static final String groupAttributeID = "cn";
	private	static final String server = Constants.LDAP_Employee_Resource;


	protected static Properties getConnectionProperties(String username, String password) {
        Properties env = new Properties();

        env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        env.put("principalDNPrefix", principalDNPrefix);
        env.put("groupAttributeID", groupAttributeID);
        env.put("groupCtxDN", groupCtxDN);
        env.put("principalDNSuffix", principalDNSuffix);
        env.put("allowEmptyPasswords", "false");
        env.put("groupUniqueMember", groupUniqueMemberName);
        env.put("jboss.security.security_domain", "ispyb");
        env.put("java.naming.provider.url", server);
        env.put("java.naming.security.authentication", "simple");
        //env.put("java.naming.provider.url", "url");
        String userDN = principalDNPrefix + username;
        String userDomain = username +"@maxlab.lu.se";
        //env.setProperty("java.naming.security.principal", userDN);
        env.put("java.naming.security.principal", userDomain);
        env.put("java.naming.security.credentials", password);


        return env;
    }
	
	public static List<String> authenticate(String username, String password)
			throws Exception {
		
		List<String> myRoles = new ArrayList<String>();
		if (!password.isEmpty()){
			InitialLdapContext ctx = new InitialLdapContext(getConnectionProperties(username, password), null);

            String filter;
			// Search
            try {

                SearchControls constraints = new SearchControls();
                constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
                String[] attrIDs = { "distinguishedName"};
                constraints.setReturningAttributes(attrIDs);
                //First input parameter is search bas, it can be "CN=Users,DC=YourDomain,DC=com"
                //Second Attribute can be uid=username
                NamingEnumeration answerCN = ctx.search(Constants.LDAP_people, "sAMAccountName=" + username, constraints);
                if (answerCN.hasMore()) {
                    Attributes attrs = ((SearchResult) answerCN.next()).getAttributes();
                    String dn = attrs.get("distinguishedName").toString().replace("distinguishedName: ", "");
                    // (&(objectClass=group)(member=CN=Alberto Nardella,CN=Users,dc=maxlab,dc=lu,dc=se))
                    filter = new StringBuffer().append("(&").append("(objectClass=group)").append("(" + groupUniqueMemberName + "=").append(dn).append(")").append(")").toString();

                    // Set up search constraints
                    SearchControls cons = new SearchControls();
                    cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
                    // Search roles
                    //NamingEnumeration<SearchResult> answer = ctx.search(groupCtxDN, filter, cons);
                    NamingEnumeration<SearchResult> answer1 = ctx.search("CN=Users," +groupCtxDN, filter, cons);
                    NamingEnumeration<SearchResult> answer2 = ctx.search("OU=DUOactive," +groupCtxDN, filter, cons);
                    NamingEnumeration<SearchResult> answer3 = ctx.search("OU=DUO," +groupCtxDN, filter, cons);
                    while (answer1.hasMore()) {
                        SearchResult sr = answer1.next();
                        attrs = sr.getAttributes();
                        System.out.println(attrs.toString());
                        Attribute roles = attrs.get(groupAttributeID);

                        for (int r = 0; r < roles.size(); r++) {
                            Object value = roles.get(r);
                            String roleName = null;
                            roleName = value.toString();
                            // fill roles array
                            if (roleName != null) {
								if (roleName.equals("ispyb-manager") || roleName.equals("ispyb-biomax-contacts") ||
                                		roleName.equals("Information Management") || roleName.equals("biomax")) {
									myRoles.add(Constants.ROLE_MANAGER);
                                    myRoles.add(Constants.ROLE_LOCALCONTACT);
                                    myRoles.add(Constants.ROLE_ADMIN);
                                    myRoles.add(Constants.ROLE_BLOM);
                                    myRoles.add(Constants.ROLE_INDUSTRIAL);
                                    myRoles.add(Constants.ROLE_STORE);
                                    myRoles.add("User");
                                }
                                else if (roleName.contains("-group")){
                                    myRoles.add("User");
                                }
                            }
                        }
                    }
                    while (answer2.hasMore()) {
                        SearchResult sr = answer2.next();
                        attrs = sr.getAttributes();
                        System.out.println(attrs.toString());
                        Attribute roles = attrs.get(groupAttributeID);

                        for (int r = 0; r < roles.size(); r++) {
                            Object value = roles.get(r);
                            String roleName = null;
                            roleName = value.toString();
                            // fill roles array
                            if (roleName != null) {
                                if (roleName.equals("ispyb-manager") || roleName.equals("ispyb-biomax-contacts") ||
                                        roleName.equals("Information Management") || roleName.equals("biomax")) {
                                    myRoles.add(Constants.ROLE_MANAGER);
                                    myRoles.add(Constants.ROLE_LOCALCONTACT);
                                    myRoles.add(Constants.ROLE_ADMIN);
                                    myRoles.add(Constants.ROLE_BLOM);
                                    myRoles.add(Constants.ROLE_INDUSTRIAL);
                                    myRoles.add(Constants.ROLE_STORE);
                                    myRoles.add("User");
                                }
                                else if (roleName.contains("-group")){
                                    myRoles.add("User");
                                }
                            }
                        }
                    }

                    while (answer3.hasMore()) {
                        SearchResult sr = answer3.next();
                        attrs = sr.getAttributes();
                        System.out.println(attrs.toString());
                        Attribute roles = attrs.get(groupAttributeID);

                        for (int r = 0; r < roles.size(); r++) {
                            Object value = roles.get(r);
                            String roleName = null;
                            roleName = value.toString();
                            // fill roles array
                            if (roleName != null) {
                                if (roleName.equals("ispyb-manager") || roleName.equals("ispyb-biomax-contacts") ||
                                        roleName.equals("Information Management") || roleName.equals("biomax")) {
                                    myRoles.add(Constants.ROLE_MANAGER);
                                    myRoles.add(Constants.ROLE_LOCALCONTACT);
                                    myRoles.add(Constants.ROLE_ADMIN);
                                    myRoles.add(Constants.ROLE_BLOM);
                                    myRoles.add(Constants.ROLE_INDUSTRIAL);
                                    myRoles.add(Constants.ROLE_STORE);
                                    myRoles.add("User");
                                }
                                else if (roleName.contains("-group")){
                                    myRoles.add("User");
                                }
                            }
                        }
                    }

                    /** Any validated user is in role User */
                    if (myRoles.size() == 0){
                        myRoles.add("User");
                    }
                }else{
                    throw new Exception("Invalid User");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }



		}
		else{
			throw new Exception("Empty passwords are not allowed");
		}
		return myRoles;
	}
}
