package ispyb.ws.security;

import ispyb.server.common.services.login.Login3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.login.Login3VO;

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
		
		System.out.println("-------SecurityInterceptor----");
		if (method.isAnnotationPresent(PermitAll.class)) {
			System.out.println("PermitAll");
			return;
		}

		// Access denied for all
		if (method.isAnnotationPresent(DenyAll.class)) {
			System.out.println("DenyAll");
			requestContext.abortWith(ACCESS_FORBIDDEN);
			return;
		}

		if (method.isAnnotationPresent(RolesAllowed.class)) {
			RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
			Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
			/** Forcing only manager to connect **/
			rolesSet.clear();
			rolesSet.add("Manager");
			
			
			System.out.println("---- ROLES allowed -----");
			String token = requestContext.getUriInfo().getPathParameters().get("token").get(0);
			System.out.println(token);
			System.out.println(rolesSet);

			Login3VO login = this.getLogin(token);
			System.out.println("User roles: " + login.getRoles());
			System.out.println("Roles allowed: " + rolesSet);
			
			if (login != null) {
				System.out.println("Valid: " + login.isValid());
				if (login.isValid()) {
					return;
				} else {
					System.out.println("Expired");
					requestContext.abortWith(this.getUnauthorizedResponse());
				}
			} else {
				requestContext.abortWith(this.getUnauthorizedResponse());
			}
		}

	}

	private Login3VO getLogin(String token) {
		try {
			System.out.println("authenticateToken " + token);
			Login3Service service = (Login3Service) Ejb3ServiceLocator.getInstance().getLocalService(Login3Service.class);
			return service.findByToken(token);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return null;
	}

}