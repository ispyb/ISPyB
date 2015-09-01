package ispyb.server.biosaxs.services.utils.reader.dat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

public class FactoryProducer {
    
	private static AbstractReaderFactory getFactory(FactoryReaderType type){
		switch (type) {
		case FIT:
			 return new FitReaderFactory();
		case FRAME:
			 return new FramesReaderFactory();
		case AVERAGE:
			 return new AverageReaderFactory();
		case SUBTRACTION:
			return new SubtractionReaderFactory();
		case MODEL:
			return new ModelReaderFactory();
		case RIGIDBODYMODELING:
			return new RigidBodyReaderFactory();
		case SUBTRACTED:
			return new SubtractedReaderFactory();
		case SAMPLEAVERAGE:
			return new SampleAverageReaderFactory();
		case BUFFERAVERAGE:
			return new BufferAverageReaderFactory();
		default:
			break;
		}
		return null;
	}
	
	
	private static HashMap<String, List<HashMap<String, Object>>> get(List<Integer> frames, List<Integer> averages, List<Integer> subtractions ,List<Integer> models,List<Integer> fits, List<Integer> rigids, 
			List<Integer> subtracteds, List<Integer> sampleaverages, List<Integer> bufferaverages) throws Exception{
		
		HashMap<String, List<HashMap<String, Object>>> result = new HashMap<String, List<HashMap<String,Object>>>();
		
		if (frames != null){
			result.put("frames", FactoryProducer.getFactory(FactoryReaderType.FRAME).getData(frames));
		}
		
		if (averages != null){
			result.put("averages", FactoryProducer.getFactory(FactoryReaderType.AVERAGE).getData(averages));
		}
		
		if (subtractions != null){
			result.put("subtractions", FactoryProducer.getFactory(FactoryReaderType.SUBTRACTION).getData(subtractions));
		}
		
		if (models != null){
			result.put("models", FactoryProducer.getFactory(FactoryReaderType.MODEL).getData(models));
		}
		
		if (fits != null){
			result.put("fits", FactoryProducer.getFactory(FactoryReaderType.FIT).getData(fits));
		}
		
		if (rigids != null){
			result.put("rbm", FactoryProducer.getFactory(FactoryReaderType.RIGIDBODYMODELING).getData(rigids));
		}
		
		if (subtracteds != null){
			result.put("subtracteds", FactoryProducer.getFactory(FactoryReaderType.SUBTRACTED).getData(subtracteds));
		}
		
		if (sampleaverages != null){
			result.put("sampleaverages", FactoryProducer.getFactory(FactoryReaderType.SAMPLEAVERAGE).getData(sampleaverages));
		}
		
		if (bufferaverages != null){
			result.put("bufferaverages", FactoryProducer.getFactory(FactoryReaderType.BUFFERAVERAGE).getData(bufferaverages));
		}
		return result;
	}
	

	


	private static HashMap<String, String> getKeys(HashMap<String, Object> map, String type){
		if (map.containsKey("id")){
			HashMap<String, String> keys = new HashMap<String, String>();
			keys.put("id", map.get("id").toString());
			keys.put("fileName", map.get("fileName").toString());
			keys.put("type", type);
			return keys;
		}
		return null;
	}
	
	public static String getPlot(List<Integer> frames,
			List<Integer> averages,
			List<Integer> subtractions, 
			List<Integer> models,
			List<Integer> fits, 
			List<Integer> rigids,
			List<Integer> subtracteds, 
			List<Integer> sampleaverages,
			List<Integer> bufferaverages
			) throws Exception {
		
		HashMap<String, List<HashMap<String, Object>>> result = FactoryProducer.get(frames, averages, subtractions, models, fits, rigids,subtracteds,sampleaverages,bufferaverages);
		List<HashMap<String, String>> X = new ArrayList<HashMap<String, String>>();
		
		List<List<List<Float>>> data = new ArrayList<List<List<Float>>>();
		
		if (result.containsKey("frames")){
			List<HashMap<String, Object>> myFrames = result.get("frames");
			for (HashMap<String, Object> map : myFrames) {
				HashMap<String, String> keys = getKeys(map, "frame");
				if (keys != null){
					X.add(keys);
				}
				if (map.containsKey("data")){
					data.add((List<List<Float>>)map.get("data"));
				}
			}
		}
		
		if (result.containsKey("averages")){
			List<HashMap<String, Object>> myAvg = result.get("averages");
			for (HashMap<String, Object> map : myAvg) {
				HashMap<String, String> keys = getKeys(map, "average");
				if (keys != null){
					X.add(keys);
				}
				if (map.containsKey("data")){
					data.add((List<List<Float>>)map.get("data"));
				}
			}
		}
		
		if (result.containsKey("subtracteds")){
			List<HashMap<String, Object>> myAvg = result.get("subtracteds");
			for (HashMap<String, Object> map : myAvg) {
				HashMap<String, String> keys = getKeys(map, "subtraction");
				if (keys != null){
					X.add(keys);
				}
				if (map.containsKey("data")){
					data.add((List<List<Float>>)map.get("data"));
				}
			}
		}
		if (result.containsKey("sampleaverages")){
			List<HashMap<String, Object>> myAvg = result.get("sampleaverages");
			for (HashMap<String, Object> map : myAvg) {
				HashMap<String, String> keys = getKeys(map, "sampleaverage");
				if (keys != null){
					X.add(keys);
				}
				if (map.containsKey("data")){
					data.add((List<List<Float>>)map.get("data"));
				}
			}
		}
		
		if (result.containsKey("models")){
			List<HashMap<String, Object>> myAvg = result.get("models");
			for (HashMap<String, Object> map : myAvg) {
				HashMap<String, String> keys = getKeys(map, "model");
				if (keys != null){
					X.add(keys);
				}
				if (map.containsKey("data")){
					data.add((List<List<Float>>)map.get("data"));
				}
			}
		}
		
		HashMap<String, Object> parsed = new HashMap<String, Object>();
		parsed.put("X", X);
		parsed.put("data",data);
//		parsed.put("parsed", getPoints(data));
		return new Gson().toJson(parsed);
	}

	
	public static List<DatFile> getDatPlot(List<Integer> frames,
			List<Integer> averages,
			List<Integer> subtractions, 
			List<Integer> models,
			List<Integer> fits, 
			List<Integer> rigids,
			List<Integer> subtracteds, 
			List<Integer> sampleaverages,
			List<Integer> bufferaverages
			) throws Exception {
		
		HashMap<String, List<HashMap<String, Object>>> result = FactoryProducer.get(frames, averages, subtractions, models, fits, rigids,subtracteds,sampleaverages,bufferaverages);
		
		List<DatFile> files = new ArrayList<DatFile>();
		
		List<HashMap<String, String>> X = new ArrayList<HashMap<String, String>>();
		
		List<List<List<Float>>> data = new ArrayList<List<List<Float>>>();
		
		/** Parsing Frames **/
		if (result.containsKey("frames")){
			List<HashMap<String, Object>> myFrames = result.get("frames");
			for (HashMap<String, Object> map : myFrames) {
				HashMap<String, String> keys = getKeys(map, "frame");
				files.add(new DatFile(keys.get("id"), keys.get("fileName"), "frame", (List<List<Float>>)map.get("data")));
			}
		}
		
		if (result.containsKey("averages")){
			List<HashMap<String, Object>> myAvg = result.get("averages");
			for (HashMap<String, Object> map : myAvg) {
				HashMap<String, String> keys = getKeys(map, "average");
				files.add(new DatFile(keys.get("id"), keys.get("fileName"), "average", (List<List<Float>>)map.get("data")));
			}
		}
		
		if (result.containsKey("subtracteds")){
			List<HashMap<String, Object>> myAvg = result.get("subtracteds");
			for (HashMap<String, Object> map : myAvg) {
				HashMap<String, String> keys = getKeys(map, "subtraction");
				files.add(new DatFile(keys.get("id"), keys.get("fileName"), "subtraction", (List<List<Float>>)map.get("data")));
			}
		}
		if (result.containsKey("sampleaverages")){
			List<HashMap<String, Object>> myAvg = result.get("sampleaverages");
			for (HashMap<String, Object> map : myAvg) {
				HashMap<String, String> keys = getKeys(map, "sampleaverage");
				files.add(new DatFile(keys.get("id"), keys.get("fileName"), "sampleaverage", (List<List<Float>>)map.get("data")));
			}
		}
		
		if (result.containsKey("bufferaverages")){
			List<HashMap<String, Object>> myAvg = result.get("bufferaverages");
			for (HashMap<String, Object> map : myAvg) {
				HashMap<String, String> keys = getKeys(map, "bufferaverage");
				files.add(new DatFile(keys.get("id"), keys.get("fileName"), "bufferaverage", (List<List<Float>>)map.get("data")));
			}
		}
		
		if (result.containsKey("frames")){
			List<HashMap<String, Object>> myFrames = result.get("frames");
			for (HashMap<String, Object> map : myFrames) {
				HashMap<String, String> keys = getKeys(map, "frame");
				files.add(new DatFile(keys.get("id"), keys.get("fileName"), "frame", (List<List<Float>>)map.get("data")));
			}
		}
		
		if (result.containsKey("models")){
			List<HashMap<String, Object>> myFrames = result.get("models");
			for (HashMap<String, Object> map : myFrames) {
				HashMap<String, String> keys = getKeys(map, "model");
				files.add(new DatFile(keys.get("id"), keys.get("fileName"), "model", (List<List<Float>>)map.get("data")));
			}
		}
		
		
		return files;
	}

	
	public static List<DatFile> getDatPlot(
			List<Integer> subtracted
			) throws Exception {
		
		HashMap<String, List<HashMap<String, Object>>> result = FactoryProducer.get(null, null, null, null, null, null,subtracted,null,null);
		
		List<DatFile> files = new ArrayList<DatFile>();
		
		List<HashMap<String, String>> X = new ArrayList<HashMap<String, String>>();
		
		List<List<List<Float>>> data = new ArrayList<List<List<Float>>>();
		
		if (result.containsKey("subtracteds")){
			List<HashMap<String, Object>> myAvg = result.get("subtracteds");
			for (HashMap<String, Object> map : myAvg) {
				HashMap<String, String> keys = getKeys(map, "subtraction");
				files.add(new DatFile(keys.get("id"), keys.get("fileName"), "subtraction", (List<List<Float>>)map.get("data")));
			}
		}
		return files;
	}

	

//	private static List<ArrayList<Float>> getPoints(List<List<List<Float>>> data) {
//		List<ArrayList<Float>> points = new ArrayList<ArrayList<Float>>();
//		int filesCount = data.size();
//		
//		List<Integer> index = new ArrayList<Integer>();
//		for (int i = 0; i < filesCount; i++) {
//			index.add(0);
//		}
//		
//		return points;
//	}

	public static String getJSON(List<Integer> frames, List<Integer> averages, List<Integer> subtractions ,List<Integer> models,List<Integer> fits, List<Integer> rigids) throws Exception{
		return new Gson().toJson(FactoryProducer.get(frames, averages, subtractions, models, fits, rigids, null, null, null));
	}
	
	public static String getJSON(List<Integer> frames, List<Integer> averages,
			List<Integer> subtractions, List<Integer> models,
			List<Integer> fits, List<Integer> rigids,
			List<Integer> subtracteds, List<Integer> sampleaverages,
			List<Integer> bufferaverages) throws Exception {
		
		return new Gson().toJson(FactoryProducer.get(frames, averages, null, null, null, null, subtracteds, sampleaverages, bufferaverages));
	}
}
