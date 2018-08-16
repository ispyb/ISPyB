package ispyb.ws.rest.saxs;

import ispyb.server.biosaxs.services.core.analysis.abInitioModelling.AbInitioModelling3Service;
import ispyb.server.biosaxs.services.utils.reader.zip.SAXSZipper;
import ispyb.server.biosaxs.vos.datacollection.Merge3VO;
import ispyb.server.biosaxs.vos.datacollection.Subtraction3VO;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;

@Path("/")
public class SubtractionRestWebService extends SaxsRestWebService {

	private final static Logger logger = Logger.getLogger(SubtractionRestWebService.class);

	private Subtraction3VO getSubtraction(String subtractionId) throws Exception {
		return getPrimaryDataProcessing3Service().getSubstractionById(Integer.parseInt(subtractionId));
	}

	

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/subtraction/{subtractionId}/download")
	@Produces("text/plain")
	public Response downloadSubtraction(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("subtractionId") int subtractionId) throws Exception {

		String methodName = "downloadSubtraction";
		long start = this.logInit(methodName, logger, token, subtractionId);
		try {
			Subtraction3VO subtraction = getPrimaryDataProcessing3Service().getSubstractionById(subtractionId);
			if (subtraction != null) {
				String filePath = subtraction.getSubstractedFilePath();
				this.logFinish(methodName, start, logger);
				return this.downloadFileAsAttachment(filePath);
			}
			
			throw new Exception("Subtraction does not exist");
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/subtraction/{subtractionId}/sampleaverage/download")
	@Produces("text/plain")
	public Response downloadSampleAverage(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("subtractionId") int subtractionId) throws Exception {

		String methodName = "downloadSampleAverage";
		long start = this.logInit(methodName, logger, token, subtractionId);
		try {
			Subtraction3VO subtraction = getPrimaryDataProcessing3Service().getSubstractionById(subtractionId);
			if (subtraction != null) {
				String filePath = subtraction.getSampleAverageFilePath();
				File file = new File(filePath);
				if (file.exists()) {
					ResponseBuilder response = Response.ok((Object) file);
					response.header("Content-Disposition", "attachment; filename=" + file.getName());
					return response.build();
				}
			}
			this.logFinish(methodName, start, logger);
			return Response.noContent().build();
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/subtraction/{subtractionId}/bufferaverage/download")
	@Produces("text/plain")
	public Response downloadBufferAverage(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("subtractionId") int subtractionId) throws Exception {

		String methodName = "downloadBufferAverage";
		long start = this.logInit(methodName, logger, token, subtractionId);
		try {
			Subtraction3VO subtraction = getPrimaryDataProcessing3Service().getSubstractionById(subtractionId);
			if (subtraction != null) {
				String filePath = subtraction.getBufferAverageFilePath();
				File file = new File(filePath);
				if (file.exists()) {
					ResponseBuilder response = Response.ok((Object) file);
					response.header("Content-Disposition", "attachment; filename=" + file.getName());
					return response.build();
				}
			}
			this.logFinish(methodName, start, logger);
			return Response.noContent().build();
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/subtraction/{subtractionIdList}/zip")
	@Produces("application/x-octet-stream")
	public Response zip(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("subtractionIdList") String subtractionIdList) throws Exception {

		String methodName = "zip";
		long start = this.logInit(methodName, logger, token, subtractionIdList);
		try {
			List<Integer> ids = this.parseToInteger(subtractionIdList);
			List<Subtraction3VO> subtractions = new ArrayList<Subtraction3VO>();

			for (Integer id : ids) {
				Subtraction3VO sub = getPrimaryDataProcessing3Service().getSubstractionById(id);
				if (sub != null) {
					subtractions.add(sub);
				}
			}
			
			byte[] bytes = SAXSZipper.zip(new ArrayList<Merge3VO>(), subtractions);
			this.logFinish(methodName, start, logger);
			ResponseBuilder response = Response.ok((Object) bytes);
			response.header("Content-Disposition", "attachment; filename=" + UUID.randomUUID().toString().substring(0, 10) +".zip");
			return response.build();
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/subtraction/{subtractionIdList}/list")
	@Produces({ "application/json" })
	public Response list(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("subtractionIdList") String subtractionIdList) throws Exception {
		String methodName = "list";
		long start = this.logInit(methodName, logger, token, subtractionIdList);
		try {
			List<Subtraction3VO> result = getAbinitioModelsBySubtractionId(proposal, subtractionIdList);
			this.logFinish(methodName, start, logger);
			return sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/subtraction/{subtractionId}/image/scattering")
	@Produces("image/png")
	public Response getScattering(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("subtractionId") String subtractionId) throws Exception {

		Subtraction3VO subtraction = this.getSubtraction(subtractionId);
		if (subtraction != null) {
			return sendImage(subtraction.getScatteringFilePath());
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/subtraction/{subtractionId}/image/kratky")
	@Produces("image/png")
	public Response getKratky(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("subtractionId") String subtractionId) throws Exception {
		Subtraction3VO subtraction = this.getSubtraction(subtractionId);
		if (subtraction != null) {
			return sendImage(subtraction.getKratkyFilePath());
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/subtraction/{subtractionId}/image/density")
	@Produces("image/png")
	public Response getDensity(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("subtractionId") String subtractionId) throws Exception {

		Subtraction3VO subtraction = this.getSubtraction(subtractionId);
		if (subtraction != null) {
			return sendImage(subtraction.getGnomFilePath());
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/subtraction/{subtractionId}/image/guinier")
	@Produces("image/png")
	public Response getGuinier(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("subtractionId") String subtractionId) throws Exception {

		Subtraction3VO subtraction = this.getSubtraction(subtractionId);
		if (subtraction != null) {
			return sendImage(subtraction.getGuinierFilePath());
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	
}
