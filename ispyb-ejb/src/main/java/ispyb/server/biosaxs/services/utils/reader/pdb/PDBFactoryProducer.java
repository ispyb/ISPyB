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
		if (superpositions != null){
			result.put("superpositions", PDBFactoryProducer.getFactory(FactoryPDBType.SUPERPOSITION).getData(superpositions));
		}
		return result;
	}
	

}
