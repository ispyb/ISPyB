package ispyb.server.biosaxs.services.utils.reader.dat;

import ispyb.server.biosaxs.services.core.analysis.advanced.AdvancedAnalysis3Service;
import ispyb.server.biosaxs.vos.advanced.RigidBodyModeling3VO;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RigidBodyReaderFactory extends AbstractReaderFactory {
	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
	
	
	@Override
	public List<HashMap<String, Object>> getData(List<Integer> idList) throws Exception {
		AdvancedAnalysis3Service advancedAnalysis3Service = (AdvancedAnalysis3Service) ejb3ServiceLocator.getLocalService(AdvancedAnalysis3Service.class);
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String,Object>>();
		for (Integer id : idList) {
			RigidBodyModeling3VO rigid = advancedAnalysis3Service.getRigidBodyById(id);
			if (rigid != null){
				HashMap<String, Object> individualResult = new HashMap<String, Object>();
				HashMap<String, Object> fit = new OneDimensionalFileReader(rigid.getRigidBodyModelingId(), rigid.getFitFilePath()).readFile();
				individualResult.put("fit", fit);
				HashMap<String, Object> record = new HashMap<String, Object>();
				record.put(rigid.getRigidBodyModelingId().toString(), individualResult);
				result.add(record);
			}
		}
		return result;
	}

}
