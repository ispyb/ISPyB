package ispyb.ws.rest.mx;

import ispyb.server.biosaxs.services.stats.Stats3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

import java.sql.Date;

import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;


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
	public Response getAutoprocessingStatisticsByDate(
			@PathParam("type") String autoprocStatisticsType, 
			@PathParam("start") Date startDate,
			@PathParam("end") Date endDate) {

		String methodName = "getAutoprocessingStatisticsByDate";

		long id = this.logInit(methodName, logger, autoprocStatisticsType, startDate, endDate);
		try {
	        
			return this.sendResponse(this.getStatsWSService().getAutoprocStatsByDate(autoprocStatisticsType, startDate, endDate));
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
	}

}
