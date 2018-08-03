package ispyb.server.biosaxs.services.utils.reader.pdb;

import ispyb.server.biosaxs.services.core.analysis.abInitioModelling.AbInitioModelling3Service;
import ispyb.server.biosaxs.vos.datacollection.Model3VO;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModelReaderFactory extends PDBAbstractReaderFactory {
	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
	
	
	@Override
	public List<HashMap<String, Object>> getDataXYZ(List<HashMap<String, String>> propertiesList) throws Exception {
	
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String,Object>>();
		
		AbInitioModelling3Service abInitioModelling3Service = (AbInitioModelling3Service) ejb3ServiceLocator.getLocalService(AbInitioModelling3Service.class);
		for (int i = 0; i < propertiesList.size(); i++) {
			HashMap<String, Object> record = new HashMap<String, Object>();
			
			record.put("fileName", null);
			record.put("id", null);
			
			Integer modelId = Integer.parseInt(propertiesList.get(i).get("modelId"));
			if (modelId != null){
				Model3VO model = abInitioModelling3Service.getModelById(modelId);
				if (model != null) {
					record.put("id", model.getModelId());
					String filePath = model.getPdbFile();
					if (filePath != null){
						record.put("fileName", filePath);
						record.put("XYZ", PDBParser.getPDBContent(filePath, propertiesList.get(i)));
					}
				}
				
				result.add(record);
			}
		}
		return result;
	}


	@Override
	List<HashMap<String, Object>> getData(List<Integer> idList) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
