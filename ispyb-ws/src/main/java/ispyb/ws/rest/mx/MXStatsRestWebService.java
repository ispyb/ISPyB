package ispyb.ws.rest.mx;

import ispyb.server.biosaxs.services.stats.Stats3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

import java.io.StringWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;


@Path("/")
public class MXStatsRestWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(MXStatsRestWebService.class);

	protected Stats3Service getStatsWSService() throws NamingException {
		return (Stats3Service) Ejb3ServiceLocator.getInstance().getLocalService(Stats3Service.class);
	}


	@RolesAllowed({ "Manager", "LocalContact" })
	@GET
	@Path("{token}/stats/autoprocstatistics/{type}/{start}/{end}/json")
	@Produces({ "application/json" })
	public Response getAutoprocessingStatisticsByDate(@PathParam("type") String autoprocStatisticsType,
			@PathParam("start") Date startDate, @PathParam("end") Date endDate, @QueryParam("beamlinenames") String beamlinesName) {

		String methodName = "getCSVAutoprocessingStatisticsByDate";
		long id = this.logInit(methodName, logger, autoprocStatisticsType, startDate, endDate);
		try {
			
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			
			if (beamlinesName != null){
				List<String> beamlines = this.parseToString(beamlinesName);
				if (beamlines.size() > 0){
					for (String beamline : beamlines) {
						list.addAll(this.getStatsWSService().getAutoprocStatsByDate(autoprocStatisticsType, startDate, endDate, beamline));
					}
				}
			}
			else{
				list.addAll(this.getStatsWSService().getAutoprocStatsByDate(autoprocStatisticsType, startDate, endDate));
			}
			
			return this.sendResponse(list);

		} catch (Exception e) {
			this.logError(methodName, e, id, logger);
		}
		return null;
	}
	
	
	@RolesAllowed({ "Manager", "LocalContact" })
	@GET
	@Path("{token}/stats/autoprocstatistics/{type}/{start}/{end}/csv")
	@Produces({ "application/csv" })
	public Response getCSVAutoprocessingStatisticsByDate(@PathParam("type") String autoprocStatisticsType,
			@PathParam("start") Date startDate, @PathParam("end") Date endDate, @QueryParam("beamlinenames") String beamlinesName) {

		String methodName = "getCSVAutoprocessingStatisticsByDate";
		long id = this.logInit(methodName, logger, autoprocStatisticsType, startDate, endDate);
		try {
			
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			
			if (beamlinesName != null){
				List<String> beamlines = this.parseToString(beamlinesName);
				if (beamlines.size() > 0){
					for (String beamline : beamlines) {
						list.addAll(this.getStatsWSService().getAutoprocStatsByDate(autoprocStatisticsType, startDate, endDate, beamline));
					}
				}
			}
			else{
				list.addAll(this.getStatsWSService().getAutoprocStatsByDate(autoprocStatisticsType, startDate, endDate));
			}
			
			return this.sendResponse(this.parseListToCSV(list));

		} catch (Exception e) {
			this.logError(methodName, e, id, logger);
		}
		return null;
	}

	@RolesAllowed({ "Manager", "LocalContact" })
	@GET
	@Path("{token}/stats/datacollectionstatistics/{imageslimit}/{start}/{end}/{beamlinenames}/json")
	@Produces({ "application/json" })
	public Response getDatacollectionStatisticsByDate(@PathParam("imageslimit") String datacollectionImages,
													  @PathParam("start") Date startDate,
													  @PathParam("end") Date endDate,
													  @PathParam("beamlinenames") String beamlinesName,
													  @QueryParam("testproposals")String datacollectionTestProposals
													 ) {

		String methodName = "getDatacollectionStatisticsByDate";
		long id = this.logInit(methodName, logger, datacollectionImages, startDate, endDate, beamlinesName, datacollectionTestProposals);
		try {
			String[] testProposals = null;
			if (datacollectionTestProposals != null && datacollectionTestProposals != ""){
				testProposals = datacollectionTestProposals.split(",");
			}

			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();

			if (beamlinesName != null && !beamlinesName.equals("0")){
				List<String> beamlines = this.parseToString(beamlinesName);
				if (beamlines.size() > 0){
					for (String beamline : beamlines) {
						list.addAll(this.getStatsWSService().getDatacollectionStatsByDate(datacollectionImages, startDate, endDate, testProposals, beamline));
					}
				}
			}
			else{
				list.addAll(this.getStatsWSService().getDatacollectionStatsByDate(datacollectionImages, startDate, endDate, testProposals));
			}

			return this.sendResponse(list);

		} catch (Exception e) {
			this.logError(methodName, e, id, logger);
		}
		return null;
	}


	@RolesAllowed({ "Manager", "LocalContact" })
	@GET
	@Path("{token}/stats/datacollectionstatistics/{imageslimit}/{start}/{end}/{beamlinenames}/csv")
	@Produces({ "application/csv" })
	public Response getCSVDatacollectionStatisticsByDate(@PathParam("imageslimit") String datacollectionImages,
														 @PathParam("start") Date startDate,
														 @PathParam("end") Date endDate,
														 @PathParam("beamlinenames") String beamlinesName,
														 @QueryParam("testproposals") String datacollectionTestProposals
														 ) {

		String methodName = "getCSVDatacollectionStatisticsByDate";
		long id = this.logInit(methodName, logger, datacollectionImages, startDate, endDate, beamlinesName, datacollectionTestProposals);
		try {
			String[] testProposals = null;
			if (datacollectionTestProposals != null && datacollectionTestProposals != ""){
				testProposals = datacollectionTestProposals.split(",");
			}
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();

			if (beamlinesName != null && !beamlinesName.equals("0")){
				List<String> beamlines = this.parseToString(beamlinesName);
				if (beamlines.size() > 0){
					for (String beamline : beamlines) {
						list.addAll(this.getStatsWSService().getDatacollectionStatsByDate(datacollectionImages, startDate, endDate, testProposals, beamline));
					}
				}
			}
			else{
				list.addAll(this.getStatsWSService().getDatacollectionStatsByDate(datacollectionImages, startDate, endDate, testProposals));
			}

			return this.sendResponse(this.parseListToCSV(list));

		} catch (Exception e) {
			this.logError(methodName, e, id, logger);
		}
		return null;
	}

	@RolesAllowed({ "Manager", "LocalContact" })
	@GET
	@Path("{token}/stats/experimentstatistics/{start}/{end}/{beamlinenames}/json")
	@Produces({ "application/json" })
	public Response getExperimentStatisticsByDate(@PathParam("start") Date startDate,
													  @PathParam("end") Date endDate,
													  @PathParam("beamlinenames") String beamlinesName,
													  @QueryParam("testproposals")String datacollectionTestProposals
	) {

		String methodName = "getExperimentStatisticsByDate";
		long id = this.logInit(methodName, logger, startDate, endDate, beamlinesName, datacollectionTestProposals);
		try {
			String[] testProposals = null;
			if (datacollectionTestProposals != null && datacollectionTestProposals != ""){
				testProposals = datacollectionTestProposals.split(",");
			}

			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();

			if (beamlinesName != null && !beamlinesName.equals("0")){
				List<String> beamlines = this.parseToString(beamlinesName);
				if (beamlines.size() > 0){
					for (String beamline : beamlines) {
						list.addAll(this.getStatsWSService().getExperimentStatsByDate(startDate, endDate, testProposals, beamline));
					}
				}
			}
			else{
				list.addAll(this.getStatsWSService().getExperimentStatsByDate(startDate, endDate, testProposals));
			}

			return this.sendResponse(list);

		} catch (Exception e) {
			this.logError(methodName, e, id, logger);
		}
		return null;
	}

	@RolesAllowed({ "Manager", "LocalContact" })
	@GET
	@Path("{token}/stats/experimentstatistics/{start}/{end}/{beamlinenames}/csv")
	@Produces({ "application/csv" })
	public Response getCSVDatacollectionStatisticsByDate(@PathParam("start") Date startDate,
														 @PathParam("end") Date endDate,
														 @PathParam("beamlinenames") String beamlinesName,
														 @QueryParam("testproposals") String datacollectionTestProposals
	) {

		String methodName = "getCSVExperimentStatisticsByDate";
		long id = this.logInit(methodName, logger, startDate, endDate, beamlinesName, datacollectionTestProposals);
		try {
			String[] testProposals = null;
			if (datacollectionTestProposals != null && datacollectionTestProposals != ""){
				testProposals = datacollectionTestProposals.split(",");
			}
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();

			if (beamlinesName != null && !beamlinesName.equals("0")){
				List<String> beamlines = this.parseToString(beamlinesName);
				if (beamlines.size() > 0){
					for (String beamline : beamlines) {
						list.addAll(this.getStatsWSService().getExperimentStatsByDate(startDate, endDate, testProposals, beamline));
					}
				}
			}
			else{
				list.addAll(this.getStatsWSService().getExperimentStatsByDate( startDate, endDate, testProposals));
			}

			return this.sendResponse(this.parseListToCSV(list));

		} catch (Exception e) {
			this.logError(methodName, e, id, logger);
		}
		return null;
	}

	public String parseListToCSV(List<Map<String, Object>> list) {
		StringWriter output = new StringWriter();

		if (list != null && !list.isEmpty()) {
			ICsvMapWriter mapWriter = null;
			try {
				mapWriter = new CsvMapWriter(output, CsvPreference.STANDARD_PREFERENCE);

				// write the header
				String[] keys = list.get(0).keySet().toArray(new String[list.get(0).keySet().size()]);
				mapWriter.writeHeader(keys);

				// write the values
				for (Map<String, Object> map : list) {
					mapWriter.write(map, keys);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				if (mapWriter != null) {
					try {
						mapWriter.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		return output.toString();
	}

}
