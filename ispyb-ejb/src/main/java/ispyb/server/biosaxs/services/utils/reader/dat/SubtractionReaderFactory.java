package ispyb.server.biosaxs.services.utils.reader.dat;

import ispyb.server.biosaxs.services.core.analysis.primaryDataProcessing.PrimaryDataProcessing3Service;
import ispyb.server.biosaxs.vos.datacollection.Subtraction3VO;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubtractionReaderFactory extends AbstractReaderFactory {
	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
	
	
	@Override
	public List<HashMap<String, Object>> getData(List<Integer> idList) throws Exception {
		PrimaryDataProcessing3Service primaryDataProcessingService = (PrimaryDataProcessing3Service) ejb3ServiceLocator.getLocalService(PrimaryDataProcessing3Service.class);
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String,Object>>();
		for (Integer subtractionId : idList) {
			Subtraction3VO subtraction = primaryDataProcessingService.getSubstractionById(subtractionId);
			if (subtraction != null){
				HashMap<String, Object> subtractionIndividualResult = new HashMap<String, Object>();
				HashMap<String, Object> buffer = new OneDimensionalFileReader(subtraction.getSubtractionId(), subtraction.getBufferAverageFilePath()).readFile();
				HashMap<String, Object> sample = new OneDimensionalFileReader(subtraction.getSubtractionId(), subtraction.getSampleAverageFilePath()).readFile();
				HashMap<String, Object> subs = new OneDimensionalFileReader(subtraction.getSubtractionId(), subtraction.getSubstractedFilePath()).readFile();
				subtractionIndividualResult.put("bufferAvg", buffer);
				subtractionIndividualResult.put("sampleAvg", sample);
				subtractionIndividualResult.put("subtraction", subs);
				subtractionIndividualResult.put("id", subtractionId);
//				HashMap<String, Object> record = new HashMap<String, Object>();
//				record.put(subtraction.getSubtractionId().toString(), subtractionIndividualResult);
				result.add(subtractionIndividualResult);
			}
		}
		return result;
	}

}
