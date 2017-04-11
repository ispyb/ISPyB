package ispyb.ws.rest.saxs;

import ispyb.common.util.PathUtils;
import ispyb.server.biosaxs.services.core.ExperimentScope;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.common.hdf5.HDF5FileReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;


@Path("/")
public class HPLCRestWebService extends SaxsRestWebService {
	private final static Logger logger = Logger.getLogger(BufferRestWebService.class);





	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposalId}/saxs/experiment/{experimentId}/hplc/overview")
	@Produces("text/plain")
	public Response overview(@PathParam("token") String token, @PathParam("proposalId") String proposal,
			@PathParam("experimentId") int experimentId) throws Exception {
		
		String methodName = "overview";
		long start = this.logInit(methodName, logger, token, proposal, experimentId);
		
		try {
			String params = ("I0,I0_Stdev,sum_I,Rg,Rg_Stdev,Vc,Vc_Stdev,Qr,Qr_Stdev,mass,mass_Stdev,quality");
			List<String> parameters = Arrays.asList(params.split(","));
			int proposalId = this.getProposalId(proposal);
			String filePath = getH5FilePathByExperimentId(experimentId, proposalId);
			if (filePath != null) {
				HDF5FileReader reader = new HDF5FileReader(filePath);
				HashMap<String, float[]> result = reader.getH5ParametersByExperimentId(parameters);
				this.logFinish(methodName, start, logger);
				return this.sendResponse(result);
			}

		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
		return null;
	}
	
	

	private String get(String y, String error, String operation) {
		if (Float.valueOf(y) == 0)
			return y;
		if (operation.equals("log")) {
			double minus = Math.log(Math.abs(Float.valueOf(y) - Float.valueOf(error)));
			return String.valueOf((float) (Math.log(Float.valueOf(y)) - minus));
		}
		return error;
	}

	private String get(String point, String operation) {
		if (Float.valueOf(point) == 0)
			return point;
		if (operation.equals("log")) {
			return String.valueOf((float) Math.log(Float.valueOf(point)));
		}
		return point;
	}

	private String getLine(
			List<Integer> ids, 
			HashMap<Integer, HashMap<String, ArrayList<String>>> result, int index,
			String operation) {
		StringBuilder sb = new StringBuilder();
		if (ids.size() > 0) {
			/** Getting q **/
			String q = result.get(ids.get(0)).get("q").get(index);
			sb.append(q).append(",");
			for (Integer frameNumber : ids) {
				sb.append(get(result.get(frameNumber).get("scattering_I").get(index), operation));
				sb.append(",");
				sb.append(get(result.get(frameNumber).get("scattering_I").get(index),
						result.get(frameNumber).get("scattering_Stdev").get(index), operation));
				sb.append(",");
				sb.append(get(result.get(frameNumber).get("subtracted_I").get(index), operation));
				sb.append(",");
				sb.append(get(result.get(frameNumber).get("subtracted_I").get(index),
						result.get(frameNumber).get("subtracted_Stdev").get(index), operation));
				sb.append(",");
				sb.append(get(result.get(frameNumber).get("buffer_I").get(index), operation));
				sb.append(",");
				sb.append(get(result.get(frameNumber).get("buffer_I").get(index),
						result.get(frameNumber).get("buffer_Stdev").get(index), operation));
				sb.append(",");
			}
		}
		/** Removing last , **/
		sb = sb.deleteCharAt(sb.lastIndexOf(","));
		return sb.append("\n").toString();

	}
	
	private String checkFilePathForDevelopment(String filePath) {
		return PathUtils.getPath(filePath);
	}
	
	private String getH5FilePathByExperimentId(Integer experimentId, Integer proposalId) throws NamingException {
		Experiment3VO experiment = this.getExperiment3Service().findById(experimentId, ExperimentScope.MINIMAL, proposalId);
		if (experiment != null) {
			String filePath = experiment.getDataAcquisitionFilePath();
			return this.checkFilePathForDevelopment(filePath);
		}
		return null;
	}
	
	private byte[] getZipFileH5ByFramesRange(Integer experimentId, Integer proposalId, Integer startFrame, Integer endFrame)
			throws Exception {
		String filePath = this.getH5FilePathByExperimentId(experimentId, proposalId);
		HDF5FileReader reader = new HDF5FileReader(filePath);
		return reader.getH5ZipFileByteArrayByFrameRange(startFrame, endFrame);
	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/experiment/{experimentId}/hplc/frame/{start}/{end}/zip")
	@Produces("application/x-octet-stream")
	public Response getZipH5Frames(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("experimentId") int experimentId, 
			@PathParam("start") int start,
			@PathParam("end") int end) throws Exception {

		String methodName = "getZipH5Frames";
		long id = this.logInit(methodName, logger, token, proposal, experimentId, start, end);
		
		try {

			byte[] bytes = this.getZipFileH5ByFramesRange(experimentId, this.getProposalId(proposal), start, end);
			String fileName = new File(this.getH5FilePathByExperimentId(experimentId, this.getProposalId(proposal))).getName();
			this.logFinish(methodName, id, logger);
			return this.downloadFile(bytes,fileName + ".zip" );

		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
	}
	
	
	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/experiment/{experimentId}/hplc/download")
	@Produces("application/x-octet-stream")
	public Response downloadHDF5(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("experimentId") int experimentId) throws Exception {
		
		String methodName = "downloadHDF5";
		long start = this.logInit(methodName, logger, token, proposal, experimentId);
		
		try {
			String filePath = getH5FilePathByExperimentId(experimentId, this.getProposalId(proposal));
			return this.downloadFileAsAttachment(filePath);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	
	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposalId}/saxs/experiment/{experimentId}/hplc/frame/{frameId}/get")
	@Produces("text/plain")
	public Response getFrame(@PathParam("token") String token, @PathParam("proposalId") String proposalId,
			@PathParam("experimentId") int experimentId, @PathParam("frameId") String frameNumberList,
			@QueryParam("operation") String operation) throws Exception {

		String methodName = "getFrame";
		long start = this.logInit(methodName, logger, token, proposalId, experimentId, frameNumberList, operation);
		
		try {

			String filePath = this.getH5FilePathByExperimentId(experimentId, this.getProposalId(proposalId));
			HDF5FileReader reader = new HDF5FileReader(filePath);
			boolean includeSubtraction = true;
			boolean includeBufferAverage = true;

			List<Integer> ids = this.parseToInteger(frameNumberList);
			HashMap<Integer, HashMap<String, ArrayList<String>>> result = new HashMap<Integer, HashMap<String, ArrayList<String>>>();

			int pointsCount = 0;
			for (Integer id : ids) {
				result.put(id, reader.getH5FrameScattering(id, includeSubtraction, includeBufferAverage));
				pointsCount = result.get(id).get("q").size();
			}

			StringBuilder sb = new StringBuilder();
			sb.append("q,");
			for (Integer id : ids) {
				String key = "#" + id;
				sb.append(key).append(",");
				sb.append(key + "_sub").append(",");
				sb.append(key + "_buffer_ave").append(",");
			}
			/** Removing last , **/
			sb = sb.deleteCharAt(sb.lastIndexOf(","));
			sb.append("\n");

			if (operation == null) {
				operation = "None";
			}
			for (int i = 0; i < pointsCount; i++) {
				sb.append(getLine(ids, result, i, operation));
			}

			this.logFinish(methodName, start, logger);
			return this.sendResponse(sb.toString());

		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

}
