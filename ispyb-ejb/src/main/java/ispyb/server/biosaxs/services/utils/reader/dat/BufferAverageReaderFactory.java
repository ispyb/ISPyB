package ispyb.server.biosaxs.services.utils.reader.dat;

import ispyb.server.biosaxs.services.core.analysis.primaryDataProcessing.PrimaryDataProcessing3Service;
import ispyb.server.biosaxs.vos.datacollection.Subtraction3VO;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BufferAverageReaderFactory extends AbstractReaderFactory {
	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
	@Override
	List<HashMap<String, Object>> getData(List<Integer> idList)
			throws Exception {
		PrimaryDataProcessing3Service primaryDataProcessingService = (PrimaryDataProcessing3Service) ejb3ServiceLocator.getLocalService(PrimaryDataProcessing3Service.class);
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String,Object>>();
		for (Integer subtractionId : idList) {
			Subtraction3VO subtraction = primaryDataProcessingService.getSubstractionById(subtractionId);
			if (subtraction != null){
				result.add(new OneDimensionalFileReader(subtraction.getSubtractionId(), subtraction.getBufferAverageFilePath()).readFile());
			}
		}
		return result;
	}

}