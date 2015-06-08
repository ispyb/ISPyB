package ispyb.ws.rest.security;

import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.ws.rest.RestWebService;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.annotation.security.PermitAll;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/")
public class AuthenticationRestWebService extends RestWebService {

	@PermitAll
	@GET
	@Path("{login}/{password}/authenticate")
	@Produces({ "application/json" })
	public Response list(@PathParam("login") String login,
			@PathParam("password") String password) throws Exception {
		String sha1 = null;
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(password.getBytes("UTF-8"));
			sha1 = byteToHex(crypt.digest());
			
			System.out.println("Validating: " + login + " " + password);
			if (this.validateLogin(login, password)){
				HashMap<String, String> cookie = new HashMap<String, String>();
				cookie.put("cookie", sha1);
				return sendResponse(cookie);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return unauthorizedResponse();
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
	
	private boolean validateLogin(String username, String password){
		
		
		Properties env = new Properties();
		env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		env.put("principalDNPrefix", "uid=");
		env.put("java.naming.security.principal", "uid=" + username + ",ou=People,dc=esrf,dc=fr");
		env.put("groupAttributeID", "cn");
		env.put("groupCtxDN", "ou=Pxwebgroups,dc=esrf,dc=fr");
		env.put("principalDNSuffix", ",ou=People,dc=esrf,dc=fr");
		
		env.put("allowEmptyPasswords", "false");
		env.put("groupUniqueMember", "uniqueMember");
		env.put("jboss.security.security_domain", "ispyb");
		env.put("java.naming.provider.url", "ldap://ldap.esrf.fr:389/");
		env.put("java.naming.security.authentication", "simple");
		env.put("java.naming.security.credentials", password);
		
		try {
			InitialLdapContext ctx = new InitialLdapContext(env, null);
			return true;
		} catch (NamingException e) {
			e.printStackTrace();
			return false;
		}
	}
	
//	java.naming.factory.initial : com.sun.jndi.ldap.LdapCtxFactory
//	principalDNPrefix : uid=
//	java.naming.security.principal : uid=opd29,ou=People,dc=esrf,dc=fr
//	groupAttributeID : cn
//	groupCtxDN : ou=Pxwebgroups,dc=esrf,dc=fr
//	principalDNSuffix : ,ou=People,dc=esrf,dc=fr
//	allowEmptyPasswords : false
//	groupUniqueMember : uniqueMember
//	jboss.security.security_domain : ispyb
//	java.naming.provider.url : ldap://ldap.esrf.fr:389/
//	java.naming.security.authentication : simple
//	java.naming.security.credentials : sdfsdfs

}
