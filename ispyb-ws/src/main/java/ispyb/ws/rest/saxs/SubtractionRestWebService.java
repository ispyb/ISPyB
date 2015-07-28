package ispyb.ws.rest.saxs;

import ispyb.server.biosaxs.services.core.analysis.abInitioModelling.AbInitioModelling3Service;
import ispyb.server.biosaxs.services.core.analysis.primaryDataProcessing.PrimaryDataProcessing3Service;
import ispyb.server.biosaxs.vos.datacollection.Subtraction3VO;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.ws.rest.RestWebService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

@Path("/")
public class SubtractionRestWebService extends RestWebService {
	private Subtraction3VO getSubtraction(String subtractionId) throws Exception{
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		PrimaryDataProcessing3Service primaryDataProcessing3Service = (PrimaryDataProcessing3Service) ejb3ServiceLocator.getLocalService(PrimaryDataProcessing3Service.class);
		return primaryDataProcessing3Service.getSubstractionById(Integer.parseInt(subtractionId));
	}
	
	private List<Subtraction3VO> getAbinitioModelsBySubtractionId(String proposal, String subtractionIdList) throws NamingException {
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		AbInitioModelling3Service abInitioModelling3Service = (AbInitioModelling3Service) ejb3ServiceLocator.getLocalService(AbInitioModelling3Service.class);
		
		List<Integer> list = parseToInteger(subtractionIdList);
		List<Subtraction3VO> result = new ArrayList<Subtraction3VO>();
		if (subtractionIdList != null){
			for (Integer subtractionId : list) {
				result.addAll(abInitioModelling3Service.getAbinitioModelsBySubtractionId(subtractionId));
			}
		}
		return result;
	}

	@PermitAll
	@GET
	@Path("{cookie}/proposal/{proposal}/saxs/subtraction/{subtractionId}/download")
	@Produces("text/plain")
	public Response downloadSubtraction(@PathParam("cookie") String cookie,
			@PathParam("proposal") int proposal,
			@PathParam("subtractionId") int subtractionId) throws Exception {

		Subtraction3VO subtraction = getPrimaryDataProcessing3Service().getSubstractionById(subtractionId);
		if (subtraction != null) {
				String filePath = subtraction.getSubstractedFilePath();
				File file = new File(filePath);
				if (file.exists()) {
					ResponseBuilder response = Response.ok((Object) file);
					response.header("Content-Disposition",
							"attachment; filename=" + file.getName());
					return response.build();
				}
		}
		return Response.noContent().build();
	}
	
	@PermitAll
	@GET
	@Path("{cookie}/proposal/{proposal}/saxs/subtraction/{subtractionIdList}/list")
	@Produces({ "application/json" })
	public Response list(
			@PathParam("cookie") String cookie, 
			@PathParam("proposal") String proposal,
			@PathParam("subtractionIdList") String subtractionIdList) throws Exception {
		return sendResponse(getAbinitioModelsBySubtractionId(proposal, subtractionIdList));
	}
	
	@PermitAll
	@GET
	@Path("{cookie}/proposal/{proposal}/saxs/subtraction/{subtractionId}/image/scattering")
	@Produces("image/png")
	public Response getScattering(
			@PathParam("cookie") String cookie, 
			@PathParam("proposal") String proposal,
			@PathParam("subtractionId") String subtractionId) throws Exception {

		Subtraction3VO subtraction = this.getSubtraction(subtractionId);
		if (subtraction != null){
				return sendImage(subtraction.getScatteringFilePath());
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	@PermitAll
	@GET
	@Path("{cookie}/proposal/{proposal}/saxs/subtraction/{subtractionId}/image/kratky")
	@Produces("image/png")
	public Response getKratky(
			@PathParam("cookie") String cookie, 
			@PathParam("proposal") String proposal,
			@PathParam("subtractionId") String subtractionId) throws Exception {
		Subtraction3VO subtraction = this.getSubtraction(subtractionId);
		if (subtraction != null){
				return sendImage(subtraction.getKratkyFilePath());
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	@PermitAll
	@GET
	@Path("{cookie}/proposal/{proposal}/saxs/subtraction/{subtractionId}/image/density")
	@Produces("image/png")
	public Response getDensity(
			@PathParam("cookie") String cookie, 
			@PathParam("proposal") String proposal,
			@PathParam("subtractionId") String subtractionId) throws Exception {

		Subtraction3VO subtraction = this.getSubtraction(subtractionId);
		if (subtraction != null){
				return sendImage(subtraction.getGnomFilePath());
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	@PermitAll
	@GET
	@Path("{cookie}/proposal/{proposal}/saxs/subtraction/{subtractionId}/image/guinier")
	@Produces("image/png")
	public Response getGuinier(
			@PathParam("cookie") String cookie, 
			@PathParam("proposal") String proposal,
			@PathParam("subtractionId") String subtractionId) throws Exception {

		Subtraction3VO subtraction = this.getSubtraction(subtractionId);
		if (subtraction != null){
				return sendImage(subtraction.getGuinierFilePath());
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	
	private Response sendImage(String filePath){
		if (filePath!= null){
			if (new File(filePath).exists()){
				File file = new File(filePath);
				ResponseBuilder response = Response.ok((Object) file);
				response.header("Content-Disposition", "attachment; filename=image_from_server.png");
				return response.build();
			}
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
}
