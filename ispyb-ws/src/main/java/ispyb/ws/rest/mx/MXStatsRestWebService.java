package ispyb.ws.rest.mx;

import java.io.StringWriter;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
	public Response getAutoprocessingStatisticsByDate(@PathParam("type") String autoprocStatisticsType, @PathParam("start") Date startDate,
			@PathParam("end") Date endDate) {

		String methodName = "getAutoprocessingStatisticsByDate";

		long id = this.logInit(methodName, logger, autoprocStatisticsType, startDate, endDate);
		try {

			return this.sendResponse(this.getStatsWSService().getAutoprocStatsByDate(autoprocStatisticsType, startDate, endDate));
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
	}

	@RolesAllowed({ "Manager", "LocalContact" })
	@GET
	@Path("{token}/stats/autoprocstatistics/{type}/{start}/{end}/csv")
	@Produces({ "application/csv" })
	public Response getCSVAutoprocessingStatisticsByDate(@PathParam("type") String autoprocStatisticsType,
			@PathParam("start") Date startDate, @PathParam("end") Date endDate) {

		String methodName = "getCSVAutoprocessingStatisticsByDate";
		long id = this.logInit(methodName, logger, autoprocStatisticsType, startDate, endDate);
		try {
			List<Map<String, Object>> list = this.getStatsWSService().getAutoprocStatsByDate(autoprocStatisticsType, startDate, endDate);
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
