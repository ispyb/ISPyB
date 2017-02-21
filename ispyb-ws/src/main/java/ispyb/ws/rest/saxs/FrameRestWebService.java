package ispyb.ws.rest.saxs;

import ispyb.server.biosaxs.services.core.analysis.primaryDataProcessing.PrimaryDataProcessing3Service;
import ispyb.server.biosaxs.services.utils.reader.dat.DatFile;
import ispyb.server.biosaxs.services.utils.reader.dat.DatFilePlotter;
import ispyb.server.biosaxs.services.utils.reader.dat.DatFilePlotter.Operation;
import ispyb.server.biosaxs.services.utils.reader.dat.FactoryProducer;
import ispyb.server.biosaxs.services.utils.reader.dat.MergeDatFilePlotter;
import ispyb.server.biosaxs.services.utils.reader.zip.SAXSZipper;
import ispyb.server.biosaxs.vos.dataAcquisition.Measurement3VO;
import ispyb.server.biosaxs.vos.datacollection.Frame3VO;
import ispyb.server.biosaxs.vos.datacollection.FrameSet3VO;
import ispyb.server.biosaxs.vos.datacollection.Framelist3VO;
import ispyb.server.biosaxs.vos.datacollection.Frametolist3VO;
import ispyb.server.biosaxs.vos.datacollection.Merge3VO;
import ispyb.server.biosaxs.vos.datacollection.Subtraction3VO;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;

@Path("/")
public class FrameRestWebService extends SaxsRestWebService {
	private final static Logger logger = Logger.getLogger(FrameRestWebService.class);

	public class FrameComparator implements Comparator<Frame3VO> {
		@Override
		public int compare(Frame3VO o1, Frame3VO o2) {
			return o1.getFrameId() - o2.getFrameId();
		}
	}

	private HashMap<String, Object> getFramesByMerge(Merge3VO merge3vo) throws Exception {
		HashMap<String, Object> result = new HashMap<String, Object>();
		List<Frame3VO> frames = new ArrayList<Frame3VO>();
		Framelist3VO frameList = merge3vo.getFramelist3VO();
		Set<Frametolist3VO> frameToList = frameList.getFrametolist3VOs();
		for (Frametolist3VO frametolist3vo : frameToList) {
			frames.add(frametolist3vo.getFrame3VO());
		}

		Collections.sort(frames, new FrameComparator());
		result.put("frames", frames);
		result.put("average", merge3vo.getAverageFilePath());
		result.put("framesTotal", merge3vo.getFramesCount());
		result.put("framesAveraged", merge3vo.getFramesMerge());
		result.put("averageId", merge3vo.getMergeId());

		return result;
	}

	private HashMap<String, Object> getFramesBySubtraction(Subtraction3VO subtraction) throws Exception {
		HashMap<String, Object> result = new HashMap<String, Object>();

		List<Frame3VO> buffers = new ArrayList<Frame3VO>();
		for (Frametolist3VO frametolist3vo : subtraction.getBufferOneDimensionalFiles().getFrametolist3VOs()) {
			buffers.add(frametolist3vo.getFrame3VO());
		}

		List<Frame3VO> samples = new ArrayList<Frame3VO>();
		for (Frametolist3VO frametolist3vo : subtraction.getSampleOneDimensionalFiles().getFrametolist3VOs()) {
			samples.add(frametolist3vo.getFrame3VO());
		}

		result.put("buffers", buffers);
		result.put("samples", samples);
		result.put("bufferAverage", subtraction.getBufferAverageFilePath());
		result.put("sampleAverage", subtraction.getSampleAverageFilePath());
		result.put("subtraction", subtraction.getSubstractedFilePath());
		result.put("subtractionId", subtraction.getSubtractionId());

		return result;
	}

	private List<HashMap<String, Object>> getFramesByMergeId(List<Integer> mergeIdList) throws Exception {
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		PrimaryDataProcessing3Service primaryDataProcessing3Service = (PrimaryDataProcessing3Service) ejb3ServiceLocator
				.getLocalService(PrimaryDataProcessing3Service.class);
		List<Merge3VO> merges = primaryDataProcessing3Service.getMergesByIdsList(mergeIdList);

		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		for (Merge3VO merge3vo : merges) {
			result.add(this.getFramesByMerge(merge3vo));
		}
		return result;
	}

	private List<HashMap<String, Object>> getFramesByMeasurementId(List<Integer> measurementIdList) throws Exception {

		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();

		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		PrimaryDataProcessing3Service primaryDataProcessing3Service = (PrimaryDataProcessing3Service) ejb3ServiceLocator
				.getLocalService(PrimaryDataProcessing3Service.class);

		List<Integer> mergeIdList = new ArrayList<Integer>();
		for (Integer measurementId : measurementIdList) {
			List<Merge3VO> mergesByMeasurement = primaryDataProcessing3Service.findByMeasurementId(measurementId);
			if (mergesByMeasurement != null) {
				if (mergesByMeasurement.size() > 0) {
					mergeIdList.add(mergesByMeasurement.get(mergesByMeasurement.size() - 1).getMergeId());
				}
			}
		}

		List<Merge3VO> merges = primaryDataProcessing3Service.getMergesByIdsList(mergeIdList);
		for (Merge3VO merge3vo : merges) {
			result.add(this.getFramesByMerge(merge3vo));
		}

		return result;
	}

	private List<HashMap<String, Object>> getSubtractionById(List<Integer> idList) throws Exception {
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		PrimaryDataProcessing3Service primaryDataProcessing3Service = (PrimaryDataProcessing3Service) ejb3ServiceLocator
				.getLocalService(PrimaryDataProcessing3Service.class);

		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		for (Integer id : idList) {
			try {
				result.add(this.getFramesBySubtraction(primaryDataProcessing3Service.getSubstractionById(id)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@PermitAll
	@GET
	@Path("{token}/proposal/{proposal}/saxs/frame/average/{mergeIdList}/list")
	@Produces({ "application/json" })
	public Response list(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("mergeIdList") String mergeIdList) {

		String methodName = "list";
		long start = this.logInit(methodName, logger, token, proposal, mergeIdList);
		try {
			List<Integer> idList = this.parseToInteger(mergeIdList);
			List<HashMap<String, Object>> result = getFramesByMergeId(idList);
			this.logFinish(methodName, start, logger);
			return sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}

	}

	@PermitAll
	@GET
	@Path("{token}/proposal/{proposal}/saxs/frame/measurement/{measurementIdList}/list")
	@Produces({ "application/json" })
	public Response listByMeasurement(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("measurementIdList") String measurementIdList) throws Exception {

		String methodName = "listByMeasurement";
		long start = this.logInit(methodName, logger, token, proposal, measurementIdList);
		try {
			List<Integer> idList = this.parseToInteger(measurementIdList);
			List<HashMap<String, Object>> result = getFramesByMeasurementId(idList);
			this.logFinish(methodName, start, logger);
			return sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}

	}

	@PermitAll
	@GET
	@Path("{token}/proposal/{proposal}/saxs/frame/subtraction/{subtractionIdList}/list")
	@Produces({ "application/json" })
	public Response get(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("subtractionIdList") String subtractionIdList) throws Exception {

		String methodName = "get";
		long start = this.logInit(methodName, logger, token, proposal, subtractionIdList);
		try {
			List<Integer> idList = this.parseToInteger(subtractionIdList);
			List<HashMap<String, Object>> result = getSubtractionById(idList);
			this.logFinish(methodName, start, logger);
			return sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}

	}


	@PermitAll
	@GET
	@Path("{token}/proposal/{proposal}/saxs/frame/datplot")
	@Produces("text/plain")
	public Response datplot(@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@QueryParam("frame") String frame, 
			@QueryParam("average") String average,
			@QueryParam("subtracted") String subtracted, 
			@QueryParam("models") String modelsIdList,
			@QueryParam("sampleaverage") String sampleaverage,
			@QueryParam("bufferaverage") String bufferaverage,
			@QueryParam("operation") String operation) // Operation = "LOG,	LINEAL" 
					throws Exception {

		String methodName = "datplot";
		long start = this.logInit(methodName, logger, token, proposal, frame, average, subtracted, sampleaverage,
				bufferaverage);
		try {
			List<Integer> frames = new ArrayList<Integer>();
			List<Integer> averages = new ArrayList<Integer>();
			List<Integer> subtracteds = new ArrayList<Integer>();
			List<Integer> sampleaverages = new ArrayList<Integer>();
			List<Integer> bufferaverages = new ArrayList<Integer>();
			
			List<Integer> models = new ArrayList<Integer>();
	
			if (frame != null) {
				frames = parseToInteger(frame);
			}
			if (average != null) {
				averages = parseToInteger(average);
			}
			if (subtracted != null) {
				subtracteds = parseToInteger(subtracted);
			}
			if (sampleaverage != null) {
				sampleaverages = parseToInteger(sampleaverage);
			}
	
			if (bufferaverage != null) {
				bufferaverages = parseToInteger(bufferaverage);
			}
			if (modelsIdList != null) {
				models = parseToInteger(modelsIdList);
			}
			
			
			List<Integer> subtractions = new ArrayList<Integer>();
//			List<Integer> models = new ArrayList<Integer>();
			List<Integer> fits = new ArrayList<Integer>();
			List<Integer> rigids = new ArrayList<Integer>();
	
			List<DatFile> files = FactoryProducer.getDatPlot(frames, averages, subtractions, models, fits, rigids, subtracteds, sampleaverages, bufferaverages);
			logger.info("Files");
			for (DatFile datFile : files) {
				logger.info(datFile);
				
			}
			
			DatFilePlotter datFilePlotter = null;
			if (operation != null){
				if (operation.toUpperCase().equals("LINEAL")){
					datFilePlotter = new DatFilePlotter(files, Operation.LINEAL);
				}
				else{
					datFilePlotter = new DatFilePlotter(files, Operation.LOG);
				}
			}
			else{
				datFilePlotter = new DatFilePlotter(files, Operation.LOG);
			}
			
			String result = datFilePlotter.getCSV();
			this.logFinish(methodName, start, logger);
			return sendResponse(result);
			
		}catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	private List<Float> parseQueryParam(String queryParam) {
		List<String> params = Arrays.asList(queryParam.split(",", -1));
		List<Float> result = new ArrayList<Float>();
		for (String string : params) {
			if (!(string.isEmpty()) && (string.matches("^([+-]?\\d*\\.?\\d*)$"))) {
				result.add(Float.valueOf(string));
			} else {
				result.add(null);
			}
		}
		return result;
	}

	@PermitAll
	@GET
	@Path("{token}/saxs/{proposal}/frame/subtractionId/{subtracted}/datplotmerge")
	@Produces("text/plain")
	public Response datplotmerge(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("subtracted") String subtracted, @QueryParam("from") String from, @QueryParam("to") String to,
			@QueryParam("scale") String scale) throws Exception {

		String methodName = "datplotmerge";
		long start = this.logInit(methodName, logger, token, proposal, subtracted, from, to, scale);
		try {
			List<Integer> subtracteds = new ArrayList<Integer>();
	
			if (subtracted != null) {
				subtracteds = parseToInteger(subtracted);
			}
			List<Float> fromList = new ArrayList<Float>(subtracteds.size());
			List<Float> toList = new ArrayList<Float>(subtracteds.size());
			List<Float> scaleList = new ArrayList<Float>(subtracteds.size());
			if (from != null) {
				fromList = parseQueryParam(from);
			}
	
			if (to != null) {
				toList = parseQueryParam(to);
			}
	
			if (scale != null) {
				scaleList = parseQueryParam(scale);
			}
	
			List<DatFile> files = FactoryProducer.getDatPlot(subtracteds);
			MergeDatFilePlotter datFilePlotter = new MergeDatFilePlotter(files, fromList, toList, scaleList);
			String result = datFilePlotter.getCSV();
			this.logFinish(methodName, start, logger);
			return sendResponse(result);
		}catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@PermitAll
	@GET
	@Path("{token}/saxs/{proposal}/frame/subtractionId/{subtracted}/merge")
	@Produces("text/plain")
	public Response merge(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("subtracted") String subtracted, @QueryParam("from") String from, @QueryParam("to") String to,
			@QueryParam("scale") String scale) throws Exception {

		String methodName = "datplotmerge";
		long start = this.logInit(methodName, logger, token, proposal, subtracted, from, to, scale);
		try {
			List<Integer> subtracteds = new ArrayList<Integer>();
	
			if (subtracted != null) {
				subtracteds = parseToInteger(subtracted);
			}
			List<Float> fromList = new ArrayList<Float>(subtracteds.size());
			List<Float> toList = new ArrayList<Float>(subtracteds.size());
			List<Float> scaleList = new ArrayList<Float>(subtracteds.size());
			if (from != null) {
				fromList = parseQueryParam(from);
			}
	
			if (to != null) {
				toList = parseQueryParam(to);
			}
	
			if (scale != null) {
				scaleList = parseQueryParam(scale);
			}
	
			List<DatFile> files = FactoryProducer.getDatPlot(subtracteds);
			MergeDatFilePlotter datFilePlotter = new MergeDatFilePlotter(files, fromList, toList, scaleList);
			String result = datFilePlotter.merge();
			this.logFinish(methodName, start, logger);
			return sendResponse(result);
		}catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/frame/{mergeIds}/zip")
	@Produces("application/x-octet-stream")
	public Response zip(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("mergeIds") String mergeIds) throws Exception {

		String methodName = "zip";
		long start = this.logInit(methodName, logger, token, mergeIds);
		try {
			List<Integer> ids = this.parseToInteger(mergeIds);
			byte[] bytes = SAXSZipper.zip(getPrimaryDataProcessing3Service().getMergesByIdsList(ids), new ArrayList<Subtraction3VO>() );
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
	@Path("{token}/proposal/{proposal}/saxs/frame/average/{mergeIds}/bean")
	@Produces("text/plain")
	public Response getListByAveragedId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("mergeIds") String mergeIds) throws Exception {

		String methodName = "zip";
		long start = this.logInit(methodName, logger, token, mergeIds);
		try {
			List<Integer> ids = this.parseToInteger(mergeIds);
			List<Merge3VO> merges = getPrimaryDataProcessing3Service().getMergesByIdsList(ids);
			this.logFinish(methodName, start, logger);
			return sendResponse(merges);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	
	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/saxs/{proposal}/frame/{frameId}/download")
	@Produces("text/plain")
	public Response downloadFrame(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("frameId") String frameId) throws Exception {
		String methodName = "downloadFrame";
		long start = this.logInit(methodName, logger, token, frameId);
		try {
			List<Frame3VO> frames = this.getPrimaryDataProcessing3Service().getFramesByIdList(parseToInteger(frameId));
			if (frames != null) {
				if (frames.size() > 0) {
					String filePath = frames.get(0).getFilePath();
					File file = new File(filePath);
					if (file.exists()) {
						ResponseBuilder response = Response.ok((Object) file);
						response.header("Content-Disposition", "attachment; filename=" + file.getName());
						this.logFinish(methodName, start, logger);
						return response.build();
					}
				}
			}
			return Response.noContent().build();
		}catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
}
