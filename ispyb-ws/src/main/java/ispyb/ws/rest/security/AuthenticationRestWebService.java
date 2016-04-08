package ispyb.ws.rest.security;

import ispyb.server.common.vos.login.Login3VO;
import ispyb.ws.rest.RestWebService;
import ispyb.ws.rest.security.login.EMBLLoginModule;
import ispyb.ws.rest.security.login.LoginModule;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/")
public class AuthenticationRestWebService extends RestWebService {
	private final static Logger logger = Logger.getLogger(AuthenticationRestWebService.class);
	
	private String generateRamdomUUID() throws NoSuchAlgorithmException, UnsupportedEncodingException{
		MessageDigest crypt = MessageDigest.getInstance("SHA-1");
		crypt.reset();
		crypt.update( UUID.randomUUID().toString().getBytes("UTF-8"));
		return byteToHex(crypt.digest());
	}
	
	@PermitAll
	@POST
	@Path("/authenticate")
	@Produces({ "application/json" })
	public Response authenticate(
			@FormParam("login") String login, 
			@FormParam("password") String password,
			@QueryParam("site") String site) throws Exception {
		String methodName = "authenticate";
		long id = this.logInit(methodName, logger, login, site);
		try {
			List<String> roles = new ArrayList<String>();
			
			if (site != null){
				if (site.equals("EMBL")){
					logger.info("Logging as EMBL");
					roles = EMBLLoginModule.authenticate(login, password);
					logger.info(roles);
				}
				if (site.equals("ESRF")){
					roles = LoginModule.authenticate(login, password);
				}
			}
			else{
				roles = LoginModule.authenticate(login, password);
			}

//			roles.add("User");
			String token = generateRamdomUUID();
			
			HashMap<String, Object> cookie = new HashMap<String, Object>();
			cookie.put("token", token);
			cookie.put("roles", roles);
			
			/** Creating the login token on database **/
			Login3VO login3vo = new Login3VO();
			login3vo.setToken(token);
			login3vo.setUsername(login);
			login3vo.setRoles(roles.toString());
			
			/** Calculating expiration time **/
			Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
			calendar.add(Calendar.HOUR, 3);
			Date expirationTime = calendar.getTime();
			
			login3vo.setExpirationTime(expirationTime);
			System.out.println(this.getGson().toJson(login3vo));
			/** Saving login on database **/
			this.getLogin3Service().persist(login3vo);
			return sendResponse(cookie);
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
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
