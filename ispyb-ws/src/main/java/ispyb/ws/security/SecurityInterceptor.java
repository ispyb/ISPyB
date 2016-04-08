package ispyb.ws.security;

import ispyb.server.common.services.login.Login3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.login.Login3VO;
import ispyb.ws.rest.saxs.MacromoleculeRestWebService;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.SecureCacheResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;
import org.hibernate.mapping.Array;
//
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.util.Base64;

import com.google.gson.Gson;

/**
 * This interceptor verify the access permissions for a user based on username
 * and passowrd provided in request
 * */
@Provider
public class SecurityInterceptor implements javax.ws.rs.container.ContainerRequestFilter {
	private final static Logger logger = Logger.getLogger(SecurityInterceptor.class);
//	private static final ServerResponse SESSION_EXPIRED = new ServerResponse("Session has expired", 401,new Headers<Object>());
	private static final ServerResponse ACCESS_DENIED = new ServerResponse("Access denied for this resource", 401,
			new Headers<Object>());
	private static final ServerResponse ACCESS_FORBIDDEN = new ServerResponse("Nobody can access this resource", 403,
			new Headers<Object>());
	private static final ServerResponse SERVER_ERROR = new ServerResponse("INTERNAL SERVER ERROR", 500, new Headers<Object>());

	
	private Response getUnauthorizedResponse(){
		return Response.status(401) 
				.header("Access-Control-Allow-Origin", "*")
				.allow("OPTIONS").build();
	}
	
	@Override
	public void filter(ContainerRequestContext requestContext) {
		ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) requestContext.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
		Method method = methodInvoker.getMethod();

		/** Allowing cross-domain **/
		ArrayList<String> header = new ArrayList<String>();
		header.add("*");
		requestContext.getHeaders().put("Access-Control-Allow-Origin", header);
		
		logger.info("-------SecurityInterceptor----");
		if (method.isAnnotationPresent(PermitAll.class)) {
			logger.info("PermitAll");
			return;
		}

		/**  Access denied for all **/
		if (method.isAnnotationPresent(DenyAll.class)) {
			logger.info("DenyAll");
			requestContext.abortWith(ACCESS_FORBIDDEN);
			return;
		}

		if (method.isAnnotationPresent(RolesAllowed.class)) {
			RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
			Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
			/** Forcing only manager to connect **/
			rolesSet.clear();
			rolesSet.add("Manager");
			rolesSet.add("LocalContact");
			
			
			logger.info("---- ROLES allowed -----");
			String token = requestContext.getUriInfo().getPathParameters().get("token").get(0);
			logger.info(token);
			logger.info(rolesSet);

			Login3VO login = this.getLogin(token);
			logger.info("User roles: " + login.getRoles());
			logger.info("Roles allowed: " + rolesSet);
			
			if (login != null) {
				if (login.checkRoles(rolesSet)){
					logger.info("Valid: " + login.isValid());
					if (login.isValid()) {
						return;
					} else {
						logger.info("Expired");
						requestContext.abortWith(this.getUnauthorizedResponse());
					}
				}
				else{
					logger.info("Roles not valid");
					requestContext.abortWith(this.getUnauthorizedResponse());
				}
			} 
			requestContext.abortWith(this.getUnauthorizedResponse());
		}

	}

	private Login3VO getLogin(String token) {
		try {
			logger.info("authenticateToken " + token);
			Login3Service service = (Login3Service) Ejb3ServiceLocator.getInstance().getLocalService(Login3Service.class);
			return service.findByToken(token);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return null;
	}

}