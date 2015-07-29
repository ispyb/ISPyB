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
	private final static Logger logger = Logger.getLogger(SpecimenRestWebService.class);

	@PermitAll
	@POST
	@Path("{cookie}/proposal/{proposal}/saxs/specimen/save")
	@Produces({ "application/json" })
	public Response saveSpecimen(@PathParam("cookie") String cookie, @PathParam("proposal") String proposal,
			@FormParam("specimen") String specimen) throws Exception {

		String methodName = "sortMeasurements";
		long start = this.logInit(methodName, logger, cookie, proposal, specimen);
		try {
			Specimen3VO specimen3VO = getGson().fromJson(specimen, Specimen3VO.class);
			specimen3VO = getWebUserInterfaceService().merge(specimen3VO);
			this.logFinish("saveSpecimen", start, logger);
			return Response.ok(getWithoutExposeAnnotationGson().toJson(specimen3VO))
					.header("Access-Control-Allow-Origin", "*").build();
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@PermitAll
	@POST
	@Path("{cookie}/proposal/{proposal}/saxs/specimen/merge")
	@Produces({ "application/json" })
	public Response mergeSpecimens(@PathParam("cookie") String cookie, @PathParam("proposal") String proposal,
			@FormParam("experimentId") String experimentId, @FormParam("sourceSpecimenId") int sourceSpecimenId,
			@FormParam("targetSpecimenId") int targetSpecimenId) throws Exception {

		String methodName = "sortMeasurements";
		long start = this.logInit(methodName, logger, cookie, proposal, experimentId, sourceSpecimenId,
				targetSpecimenId);
		try {

			Specimen3VO specimen = getWebUserInterfaceService().mergeSpecimens(targetSpecimenId, sourceSpecimenId);
			Experiment3VO experiment = getExperiment3Service().findById(specimen.getExperimentId(),
					ExperimentScope.MEDIUM);
			String json = ExperimentSerializer.serializeExperiment(experiment, ExperimentScope.MEDIUM);
			this.logFinish("mergeSpecimens", start, logger);
			return this.sendResponse(json);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
}
