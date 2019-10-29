package ispyb.ws.rest.mx;

import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import ispyb.server.mx.vos.sample.Protein3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;

@Path("/")
public class ProteinRestWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(ProteinRestWebService.class);

	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/protein/list")
	@Produces({ "application/json" })
	public Response getProteinByProposalId(@PathParam("token") String token,
			@PathParam("proposal") String proposal) {

		String methodName = "getProteinByProposalId";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<Protein3VO> proteins = this.getProtein3Service().findByProposalId(this.getProposalId(proposal));
			return this.sendResponse(proteins);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/protein/stats")
	@Produces({ "application/json" })
	public Response getStatsByProposal(@PathParam("token") String token,
			@PathParam("proposal") String proposal) throws Exception {

		try {
			return this.sendResponse(this.getProtein3Service().getStatsByProposal(this.getProposalId(proposal)));
		} catch (Exception e) {
			throw e;
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@POST
	@Path("/{token}/proposal/{proposal}/mx/protein/save")
	@Produces({ "application/json" })
	public Response saveProtein(
			@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@FormParam("proteinId") Integer proteinId,
			@FormParam("name") String name,
			@FormParam("acronym") String acronym) throws Exception {

			long start = this.logInit("saveProtein", logger, token, proposal,
					proteinId, name, acronym);

			try {

				Protein3VO protein3vo = new Protein3VO();

				if (proteinId == null || proteinId == 0) {
					protein3vo.setProposalVO(this.getProposal3Service().findByPk(this.getProposalId(proposal)));
					protein3vo.setName(name);
					protein3vo.setAcronym(acronym);
					protein3vo = this.getProtein3Service().create(protein3vo);
				} else {
					protein3vo = this.getProtein3Service().findByPk(proteinId, false);
					protein3vo.setName(name);
					protein3vo.setAcronym(acronym);
				}

				protein3vo = this.getProtein3Service().update(protein3vo);
				this.logFinish("saveProtein", start, logger);

				return sendResponse(protein3vo);
			} catch (Exception e) {
				this.logError("saveProtein", e, start, logger);
			}
			return null;
	}
	
}
