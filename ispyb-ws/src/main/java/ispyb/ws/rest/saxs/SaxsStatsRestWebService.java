package ispyb.ws.rest.saxs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.annotation.security.PermitAll;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/")
public class SaxsStatsRestWebService extends SaxsRestWebService {
	
	private final static Logger logger = Logger.getLogger(SaxsStatsRestWebService.class);
	
	private  HashMap<String, Object> getStats(String start, String end) throws NamingException{
		HashMap<String, Object> record = new HashMap<String, Object>();
		record.put("STATIC",  getStats3Service().getExperimentsBy("STATIC", (start), (end)));
		record.put("HPLC",  getStats3Service().getExperimentsBy("HPLC", (start), (end)));
		record.put("CALIBRATION",  getStats3Service().getExperimentsBy("CALIBRATION", (start), (end)));
		record.put("SAMPLE",  getStats3Service().getSamplesBy((start), (end)));
		record.put("FRAME",  getStats3Service().getFramesBy((start), (end)));
		record.put("SESSION",  getStats3Service().getSessionsBy((start), (end)));
		return record;
	}
	
	private  HashMap<String, Object> getStats(Date start, Date end) throws NamingException{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return this.getStats(format.format(start), format.format(end));
	}
	
	private  ArrayList<HashMap<String, Object>> getStatsByYear(int year) throws NamingException{
		ArrayList<HashMap<String, Object>> dates = new ArrayList<HashMap<String, Object>>();
		for (int month = 0; month < 12; month++) {
			 Calendar gc = new GregorianCalendar();
		     gc.set(Calendar.MONTH, month);
		     gc.set(Calendar.DAY_OF_MONTH, 1);
		     gc.set(Calendar.YEAR, year);
		     Date monthStart = gc.getTime();
		     gc.add(Calendar.MONTH, 1);
		     gc.add(Calendar.DAY_OF_MONTH, -1);
		     gc.set(Calendar.YEAR, year);
		     Date monthEnd = gc.getTime();
		     HashMap<String, Object> entry = new HashMap<String, Object>();   
		     entry.put(new SimpleDateFormat("yyyy-MM").format(monthStart), getStats(monthStart, monthEnd));
		     dates.add(entry);
		}
		return dates;
	}
	
	private  String getCSVStatsByYear(int year) throws NamingException{
		StringBuilder sb = new StringBuilder();
		
		sb.append("date, static, hplc, calibration, template, sample, frame, session").append("\n");
		for (int month = 0; month < 12; month++) {
			 Calendar gc = new GregorianCalendar();
		     gc.set(Calendar.MONTH, month);
		     gc.set(Calendar.DAY_OF_MONTH, 1);
		     gc.set(Calendar.YEAR, year);
		     Date monthStart = gc.getTime();
		     gc.add(Calendar.MONTH, 1);
		     gc.add(Calendar.DAY_OF_MONTH, -1);
		     gc.set(Calendar.YEAR, year);
		     Date monthEnd = gc.getTime();
		     SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		     String start = format.format(monthStart);
		     String end = format.format(monthEnd);
		     
		     sb.append(new SimpleDateFormat("yyyy-MM").format(monthStart)).append("\t");
		     sb.append(getStats3Service().getExperimentsBy("STATIC", (start), (end))).append("\t");
		     sb.append(getStats3Service().getExperimentsBy("HPLC", (start), (end))).append("\t");
		     sb.append(getStats3Service().getExperimentsBy("CALIBRATION", (start), (end))).append("\t");
		     sb.append(getStats3Service().getExperimentsBy("TEMPLATE", (start), (end))).append("\t");
		     sb.append(getStats3Service().getSamplesBy((start), (end))).append("\t");
		     sb.append(getStats3Service().getFramesBy((start), (end))).append("\t");
		     sb.append(getStats3Service().getSessionsBy((start), (end))).append("\t");
		     
		     sb.append("\n");
		     
		}
		return sb.toString().replaceAll("\\[", "").replace("]", "");
	}
	
	@PermitAll
	@GET
	@Path("stats/experiment/{year}/csv")
	@Produces("text/plain")
	public String getStatsCSV(@PathParam("year") int year) {
		
		StringBuilder sb = new StringBuilder();
		String methodName = "getStatsCSV";
		long id = this.logInit(methodName, logger, year);
		try{
			return this.getCSVStatsByYear(year);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	@PermitAll
	@GET
	@Path("stats/experiment/{year}/count")
	@Produces({ "application/json" })
	public Response getStats(
			@PathParam("year") int year) {
		String methodName = "getStats";
		long id = this.logInit(methodName, logger, year);
		try{
			return this.sendResponse(this.getStatsByYear(year));
		}
		catch(Exception e){
			return this.logError(methodName, e, id, logger);
		}
	}
	
	@PermitAll
	@GET
	@Path("stats/experiment/{start}/{end}/count")
	@Produces({ "application/json" })
	public Response getStatsByDates(
			@PathParam("start") String start,
			@PathParam("end") String end) {
		String methodName = "getStatsByDates";
		long id = this.logInit(methodName, logger, start, end);
		try{
			return this.sendResponse(this.getStats(start, end));
		}
		catch(Exception e){
			return this.logError(methodName, e, id, logger);
		}
	}
	
		
}
