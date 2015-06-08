package org.jboss.as.quickstarts.rshelloworld;

import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.vos.sample.Protein3VO;
import ispyb.server.security.EmployeeVO;
import ispyb.server.security.LdapConnection;
import ispyb.server.security.LdapLoginModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.ejb3.annotation.SecurityDomain;

/**
 * A simple JAX-RS 2.0 REST service which is able to say hello to the world
 * using an injected HelloService CDI bean. The {@link javax.ws.rs.Path} class
 * annotation value is related to the
 * {@link ispyb.ws.rest.Application}'s path.
 * 
 * @author gbrey@redhat.com
 * @author Eduardo Martins
 * 
 */

@SecurityDomain("ispyb")
@Path("/")
public class HelloWorld {
	@Inject
	HelloService helloService;

	/**
	 * Retrieves a JSON hello world message. The {@link javax.ws.rs.Path} method
	 * annotation value is related to the one defined at the class level.
	 * 
	 * @return
	 * @throws Exception
	 */
	@PermitAll
	@GET
	@Path("json")
	@Produces({ "application/json" })
	public JsonObject getHelloWorldJSON() throws Exception {

		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator
				.getInstance();
		Proposal3Service proposalService = (Proposal3Service) ejb3ServiceLocator
				.getLocalService(Proposal3Service.class);

		List<String> listProteinAcronyms = new ArrayList<String>();

		List<Proposal3VO> proposals = proposalService.findByCodeAndNumber("mx",
				"415", false, true, false);
		if (proposals != null && proposals.size() > 0) {
			Proposal3VO proposal3VO = proposals.get(0);
			Set<Protein3VO> proteinVOs = proposal3VO.getProteinVOs();
			if (proteinVOs != null) {
				List<Protein3VO> listProtein = new ArrayList<Protein3VO>(
						Arrays.asList(proteinVOs
								.toArray(new Protein3VO[proteinVOs.size()])));
				for (Iterator<Protein3VO> p = listProtein.iterator(); p
						.hasNext();) {
					listProteinAcronyms.add(p.next().getAcronym());
				}
			}

		}

		return Json
				.createObjectBuilder()
				.add("result",
						helloService.createHelloMessage(listProteinAcronyms
								.toString())).build();
	}

	@PermitAll
	@GET
	@Path("/json/{password}/{properties}/authenticate")
	@Produces({ "application/json" })
	public JsonObject getCoookie(@PathParam("password") String password, @PathParam("properties") String properties) throws Exception {
		LdapLoginModule loging = new LdapLoginModule();
		System.out.println("Properties: " + properties);
		boolean v = loging.validateCredentials("demariaa", "p" + password);
		return Json
				.createObjectBuilder()
				.add("result", "VALIDARED " + v).build();
	}

	/**
	 * Retrieves a XML hello world message. The {@link javax.ws.rs.Path} method
	 * annotation value is related to the one defined at the class level.
	 * 
	 * @return
	 */
	@DenyAll
	@GET
	@Path("xml")
	@Produces({ "application/xml" })
	public String getHelloWorldXML() {
		return "<xml><result>" + helloService.createHelloMessage("World")
				+ "@DenyAll</result></xml>";
	}

}
