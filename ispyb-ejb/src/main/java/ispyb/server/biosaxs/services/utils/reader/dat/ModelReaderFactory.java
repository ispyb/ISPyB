package ispyb.server.biosaxs.services.utils.reader.dat;

import ispyb.server.biosaxs.services.core.analysis.abInitioModelling.AbInitioModelling3Service;
import ispyb.server.biosaxs.services.core.analysis.primaryDataProcessing.PrimaryDataProcessing3Service;
import ispyb.server.biosaxs.vos.datacollection.Frame3VO;
import ispyb.server.biosaxs.vos.datacollection.Model3VO;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

public class ModelReaderFactory extends AbstractReaderFactory {
	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
	
	
//	@Override
//	public List<HashMap<String, Object>> getData(List<Integer> idList) throws Exception {
//		AbInitioModelling3Service abInitioModelling3Service = (AbInitioModelling3Service) ejb3ServiceLocator.getLocalService(AbInitioModelling3Service.class);
//		List<HashMap<String, Object>> result = new ArrayList<HashMap<String,Object>>();
//		for (Integer modelId : idList) {
//			Model3VO model = abInitioModelling3Service.getModelById(modelId);
//			if (model != null){
//				HashMap<String, Object> individualResult = new HashMap<String, Object>();
//				HashMap<String, Object> fir = new OneDimensionalFileReader(model.getModelId(), model.getFirFile()).readFile();
//				HashMap<String, Object> fit= new OneDimensionalFileReader(model.getModelId(), model.getFitFile()).readFile();
//				individualResult.put("fir", fir);
//				individualResult.put("fit", fit);
//				HashMap<String, Object> record = new HashMap<String, Object>();
//				record.put(model.getModelId().toString(), individualResult);
//				result.add(record);
//			}
//		}
//		return result;
//	}

	@Override
	public List<HashMap<String, Object>> getData(List<Integer> idList) throws Exception {
		AbInitioModelling3Service abInitioModelling3Service = (AbInitioModelling3Service) ejb3ServiceLocator.getLocalService(AbInitioModelling3Service.class);
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String,Object>>();
		for (Integer modelId : idList) {
			Model3VO model = abInitioModelling3Service.getModelById(modelId);
			if (model != null){
				result.add(new OneDimensionalFileReader(model.getModelId(), model.getFirFile()).readFile());
			}
		}
		return result;
	}
	
}
