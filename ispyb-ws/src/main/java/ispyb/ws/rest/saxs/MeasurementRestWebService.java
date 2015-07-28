package ispyb.ws.rest.saxs;

import ispyb.server.biosaxs.services.ExperimentSerializer;
import ispyb.server.biosaxs.services.core.ExperimentScope;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Measurement3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Specimen3VO;
import ispyb.server.biosaxs.vos.datacollection.MeasurementTodataCollection3VO;
import ispyb.server.biosaxs.vos.utils.comparator.SaxsDataCollectionComparator;
import ispyb.server.common.util.LoggerFormatter;
import ispyb.ws.rest.RestWebService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

@Path("/")
public class MeasurementRestWebService extends RestWebService {
    	private final static Logger LOGGER = Logger.getLogger(MeasurementRestWebService.class);
	
	
    	@PermitAll
	@GET
	@Path("{cookie}/proposal/{proposal}/saxs/measurement/{measurementId}/remove")
	@Produces()
	public Response removeMeasurement(
			@PathParam("cookie") String cookie, 
			@PathParam("proposal") String proposal,
			@PathParam("measurementId") int measurementId) throws Exception {
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("cookie", String.valueOf(cookie));
		params.put("proposal", String.valueOf(proposal));
		params.put("measurementId", String.valueOf(measurementId));
		
		long start = this.logInit("removeMeasurement", new Gson().toJson(params), LOGGER);
		try {
		    	Experiment3VO experiment = getExperiment3Service().findByMeasurementId(measurementId);
		    	Measurement3VO measurement3VO = experiment.getMeasurementById(measurementId);
//		    	Measurement3VO measurement3VO = getGson().fromJson(measurement, Measurement3VO.class);
			List<MeasurementTodataCollection3VO> measurementsDC = getMeasurementToDataCollectionService().findByMeasurementId(measurement3VO.getMeasurementId());
			
			/** Getting specimens involved in this datacollection **/
			List<Specimen3VO> specimens = new ArrayList<Specimen3VO>();
			for (MeasurementTodataCollection3VO measurementTodataCollection3VO : measurementsDC) {
				specimens = getWebUserInterfaceService().getSpecimenByDataCollectionId(measurementTodataCollection3VO.getDataCollectionId());
			}
			/** Removing data collection, MeasurementtoDc and Measurement **/
			getWebUserInterfaceService().removeDataCollectionByMeasurement(measurement3VO);

			/** Removing specimen if possible measurementCount == 0 **/
			for (Specimen3VO specimen3vo : specimens) {
			    getWebUserInterfaceService().remove(specimen3vo);
			}
			
			this.logFinish("removeMeasurement", start, LOGGER);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "removeMeasurement", start, System.currentTimeMillis(),
					e.getMessage(), e);
		}
		return this.sendResponse("Ok");
	}
    	
    	
	@PermitAll
	@POST
	@Path("{cookie}/proposal/{proposal}/saxs/measurement/save")
	@Produces({ "application/json" })
	public Response saveMeasurement(
			@PathParam("cookie") String cookie, 
			@PathParam("proposal") String proposal,
			@FormParam("measurement") String measurement) throws Exception {
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("cookie", String.valueOf(cookie));
		params.put("proposal", String.valueOf(proposal));
		params.put("measurement", String.valueOf(measurement));
		
		long start = this.logInit("saveMeasurement", new Gson().toJson(params), LOGGER);
		try {
			Measurement3VO measurement3VO = getGson().fromJson(measurement, Measurement3VO.class);
			measurement3VO = getWebUserInterfaceService().merge(measurement3VO);
			this.logFinish("saveMeasurement", start, LOGGER);
			return this.sendResponse(measurement3VO);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "saveMeasurement", start, System.currentTimeMillis(),
					e.getMessage(), e);
		}
		return null;
	}
	
	
	@PermitAll
	@GET
	@Path("{cookie}/proposal/{proposalId}/saxs/measurement/experiment/{experimentId}/type/{type}/sort")
	@Produces({ "application/json" })
	public Response sortMeasurements(
			@PathParam("cookie") String cookie, 
			@PathParam("proposalId") String proposal,
			@PathParam("experimentId") String experimentId,
			@PathParam("type") String type) throws Exception {
	    
	    

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("cookie", String.valueOf(cookie));
		params.put("proposal", String.valueOf(proposal));
		params.put("experimentId", String.valueOf(experimentId));
		params.put("type", String.valueOf(type));
		
		long start = this.logInit("sortMeasurements", new Gson().toJson(params), LOGGER);
		try {
			Integer proposalId = this.getProposalId(proposal);

			Experiment3VO experiment = null;
			if (type.equals("FIFO")) {
				SaxsDataCollectionComparator[] options = { SaxsDataCollectionComparator.BY_MEASUREMENT_ID };
				experiment = getExperiment3Service().setPriorities(Integer.parseInt(experimentId), proposalId, options);
			} else {
				SaxsDataCollectionComparator[] options = SaxsDataCollectionComparator.defaultComparator;
				experiment = getExperiment3Service().setPriorities(Integer.parseInt(experimentId), proposalId, options);
			}
			
			String json = ExperimentSerializer.serializeExperiment(experiment, ExperimentScope.MEDIUM);
			this.logFinish("sortMeasurements", start, LOGGER);
			return this.sendResponse(json);
			
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "sortMeasurements", start, System.currentTimeMillis(),
					e.getMessage(), e);
		}
		return null;
	    
	}
	
	
	

	
}
