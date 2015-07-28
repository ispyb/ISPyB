package ispyb.ws.rest.security;

import ispyb.ws.rest.RestWebService;
import ispyb.ws.security.login.LoginModule;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.annotation.security.PermitAll;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/")
public class AuthenticationRestWebService extends RestWebService {

    @PermitAll
    @POST
    @Path("/authenticate")
    @Produces({ "application/json" })
    public Response list(@FormParam("login") String login, @FormParam("password") String password) throws Exception {
	String sha1 = null;
	try {
	    MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	    crypt.reset();
	    crypt.update(password.getBytes("UTF-8"));
	    sha1 = byteToHex(crypt.digest());

	    List<String> roles = LoginModule.authenticate(login, password);
	    HashMap<String, Object> cookie = new HashMap<String, Object>();
	    cookie.put("cookie", sha1);
	    cookie.put("roles", roles);
	    return sendResponse(cookie);
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

    
}
