package ispyb.server.biosaxs.services.utils.reader.dat;

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
		case SUBTRACTION:
			return new SubtractionReaderFactory();
		case MODEL:
			return new ModelReaderFactory();
		case RIGIDBODYMODELING:
			return new RigidBodyReaderFactory();
		default:
			break;
		}
		return null;
	}
	
	
	private static HashMap<String, List<HashMap<String, Object>>> get(List<Integer> frames, List<Integer> subtractions ,List<Integer> models,List<Integer> fits, List<Integer> rigids) throws Exception{
		HashMap<String, List<HashMap<String, Object>>> result = new HashMap<String, List<HashMap<String,Object>>>();
		
		if (frames != null){
			result.put("frames", FactoryProducer.getFactory(FactoryReaderType.FRAME).getData(frames));
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
		
		return result;
	}
	

	public static String getJSON(List<Integer> frames, List<Integer> subtractions ,List<Integer> models,List<Integer> fits, List<Integer> rigids) throws Exception{
		return new Gson().toJson(FactoryProducer.get(frames, subtractions, models, fits, rigids));
	}
}
