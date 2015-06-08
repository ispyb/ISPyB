package ispyb.server.biosaxs.services.utils.reader.pdb;

import java.util.HashMap;
import java.util.List;

public class PDBFactoryProducer {
    
	private static PDBAbstractReaderFactory getFactory(FactoryPDBType type){
		switch (type) {
		case MODEL:
			 return new ModelReaderFactory();
		case SUPERPOSITION:
			 return new SuperpositionModelReaderFactory();
		default:
			break;
		}
		return null;
	}
	
	
	public static HashMap<String, List<HashMap<String, Object>>> get(List<HashMap<String, String>> models, List<Integer> superpositions) throws Exception{
		HashMap<String, List<HashMap<String, Object>>> result = new HashMap<String, List<HashMap<String,Object>>>();
		
		if (models != null){
			if (models.size() > 0){
				result.put("models", PDBFactoryProducer.getFactory(FactoryPDBType.MODEL).getDataXYZ(models));
			}
		}
//		
		if (superpositions != null){
			result.put("superpositions", PDBFactoryProducer.getFactory(FactoryPDBType.SUPERPOSITION).getData(superpositions));
		}
//		
//		if (models != null){
//			result.put("models", PDBFactoryProducer.getFactory(FactoryPDBType.MODEL).getData(models));
//		}
//		
//		if (fits != null){
//			result.put("fits", PDBFactoryProducer.getFactory(FactoryPDBType.FIT).getData(fits));
//		}
//		
//		if (rigids != null){
//			result.put("rbm", PDBFactoryProducer.getFactory(FactoryPDBType.RIGIDBODYMODELING).getData(rigids));
//		}
//		
		return result;
	}
	

//	public static String getJSON(List<Integer> frames, List<Integer> subtractions ,List<Integer> models,List<Integer> fits, List<Integer> rigids) throws Exception{
//		return new Gson().toJson(PDBFactoryProducer.get(frames, subtractions, models, fits, rigids));
//	}
}
