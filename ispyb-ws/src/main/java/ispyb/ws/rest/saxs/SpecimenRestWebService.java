package ispyb.ws.rest.saxs;

import ispyb.server.biosaxs.services.ExperimentSerializer;
import ispyb.server.biosaxs.services.core.ExperimentScope;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Specimen3VO;
import ispyb.server.common.util.LoggerFormatter;
import ispyb.ws.rest.RestWebService;

import java.util.HashMap;

import javax.annotation.security.PermitAll;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

@Path("/")
public class SpecimenRestWebService extends RestWebService {
    	private final static Logger LOGGER = Logger.getLogger(SpecimenRestWebService.class);
	
    	
	@PermitAll
	@POST
	@Path("{cookie}/proposal/{proposal}/saxs/specimen/save")
	@Produces({ "application/json" })
	public Response saveSpecimen(
			@PathParam("cookie") String cookie, 
			@PathParam("proposal") String proposal,
			@FormParam("specimen") String specimen) throws Exception {
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("cookie", String.valueOf(cookie));
		params.put("proposal", String.valueOf(proposal));
		params.put("specimen", String.valueOf(specimen));
		
		long start = this.logInit("saveSpecimen", new Gson().toJson(params), LOGGER);
		try {
			Specimen3VO specimen3VO = getGson().fromJson(specimen, Specimen3VO.class);
			specimen3VO = getWebUserInterfaceService().merge(specimen3VO);
			this.logFinish("saveSpecimen", start, LOGGER);
			return Response.ok(getWithoutExposeAnnotationGson()
				.toJson(specimen3VO))
				.header("Access-Control-Allow-Origin", "*")
				.build();
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "saveSpecimen", start, System.currentTimeMillis(),
					e.getMessage(), e);
		}
		return null;
	}
	
	
	@PermitAll
	@POST
	@Path("{cookie}/proposal/{proposal}/saxs/specimen/merge")
	@Produces({ "application/json" })
	public Response mergeSpecimens(
			@PathParam("cookie") String cookie, 
			@PathParam("proposal") String proposal,
			@FormParam("experimentId") String experimentId,
			@FormParam("sourceSpecimenId") int sourceSpecimenId,
			@FormParam("targetSpecimenId") int targetSpecimenId) throws Exception {
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("cookie", String.valueOf(cookie));
		params.put("proposal", String.valueOf(proposal));
		params.put("sourceSpecimenId", String.valueOf(sourceSpecimenId));
		params.put("targetSpecimenId", String.valueOf(targetSpecimenId));
		
		long start = this.logInit("mergeSpecimens", new Gson().toJson(params), LOGGER);
		try {
			
			Specimen3VO specimen = getWebUserInterfaceService().mergeSpecimens(targetSpecimenId, sourceSpecimenId);
			Experiment3VO experiment = getExperiment3Service().findById(specimen.getExperimentId(), ExperimentScope.MEDIUM);
			String json = ExperimentSerializer.serializeExperiment(experiment, ExperimentScope.MEDIUM);
			this.logFinish("mergeSpecimens", start, LOGGER);
			return this.sendResponse(json);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "mergeSpecimens", start, System.currentTimeMillis(),
					e.getMessage(), e);
		}
		return null;
	}
}
