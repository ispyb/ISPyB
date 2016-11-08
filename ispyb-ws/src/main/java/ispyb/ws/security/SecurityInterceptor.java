package ispyb.ws.security;

import ispyb.server.common.services.login.Login3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.login.Login3VO;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.apache.log4j.Logger;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;

/**
 * This SecurityInterceptor verify the access permissions for a user based on user name and method annotations
 * 
 * */
@Provider
public class SecurityInterceptor implements javax.ws.rs.container.ContainerRequestFilter {
	private final static Logger logger = Logger.getLogger(SecurityInterceptor.class);
	
	private static final ServerResponse ACCESS_DENIED = new ServerResponse("Access denied for this resource", 401,new Headers<Object>());
	private static final ServerResponse ACCESS_FORBIDDEN = new ServerResponse("Nobody can access this resource", 403,new Headers<Object>());

	
//	private Response getUnauthorizedResponse(){
//		return Response.status(401) 
//				.header("Access-Control-Allow-Origin", "*").build();
//	}
	
	@Override
	public void filter(ContainerRequestContext requestContext) {
		ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) requestContext.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
		Method method = methodInvoker.getMethod();

		/** Allowing cross-domain **/
		ArrayList<String> header = new ArrayList<String>();
		header.add("*");
		requestContext.getHeaders().put("Access-Control-Allow-Origin", header);
		
		if (method.isAnnotationPresent(PermitAll.class)) {
			logger.info("PermitAll");
			return;
		}

		/**  Access denied for all **/
		if (method.isAnnotationPresent(DenyAll.class)) {
			requestContext.abortWith(ACCESS_DENIED);
			return;
		}

		if (method.isAnnotationPresent(RolesAllowed.class)) {
			RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
			Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
			//TODO remove later
			/** Forcing only manager to connect **/
//			rolesSet.clear();
//			rolesSet.add("Manager");
//			rolesSet.add("Localcontact");
						
			String token = requestContext.getUriInfo().getPathParameters().get("token").get(0);

			Login3VO login = this.getLogin(token);
			
			if (login != null) {
				if (login.checkRoles(rolesSet)){
					if (login.isValid()) {
						// if role manager or localcontact ok
						// TODO: later the localcontact should have access only to the sessions on her/his beamlines
						// so that a check has to be done among the proposals attached to the beamlines of the local contact.
						if (login.isManager() || login.isLocalContact()) {
							return;
						}
						// if role user have to check in request the proposal name to match the username
						// TODO: now it is ok because the username is the proposalname, 
						// but later the username will give access to several proposals so that a check has to be done among the proposals belonging to the user.
						
						if (login.isUser()){
							// special case to display the list of proposal, with no proposalname present in the url
							if (requestContext.getUriInfo().getPathParameters().get("proposal") == null )
								return;
							
							String proposalname = requestContext.getUriInfo().getPathParameters().get("proposal").get(0);
							if (login.getUsername().toUpperCase().equals(proposalname.toUpperCase())) {
								return;
							}
							else {
								logger.info("Proposal not allowed for this user");
								requestContext.abortWith(ACCESS_DENIED);
							}
						}
					} else {
						logger.info("Token Expired");
						requestContext.abortWith(ACCESS_FORBIDDEN);
					}
				}
				else{
					logger.info("Roles not valid");
					requestContext.abortWith(ACCESS_FORBIDDEN);
				}
			} 
			requestContext.abortWith(ACCESS_FORBIDDEN);
		}

	}

	private Login3VO getLogin(String token) {
		try {
			Login3Service service = (Login3Service) Ejb3ServiceLocator.getInstance().getLocalService(Login3Service.class);
			return service.findByToken(token);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return null;
	}

}