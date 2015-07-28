package ispyb.server.biosaxs.services.utils.reader.dat;

import ispyb.server.biosaxs.services.core.analysis.primaryDataProcessing.PrimaryDataProcessing3Service;
import ispyb.server.biosaxs.vos.datacollection.Merge3VO;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class AverageReaderFactory extends AbstractReaderFactory {
	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
	
	@Override
	public List<HashMap<String, Object>> getData(List<Integer> idList) throws Exception {
		PrimaryDataProcessing3Service primaryDataProcessingService = (PrimaryDataProcessing3Service) ejb3ServiceLocator.getLocalService(PrimaryDataProcessing3Service.class);
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String,Object>>();
		if (idList != null){
			if (idList.size() > 0){
				List<Merge3VO> averages = primaryDataProcessingService.getMergesByIdsList(idList);
				for (Merge3VO average : averages) {
					result.add(new OneDimensionalFileReader(average.getMergeId(), average.getAverageFilePath()).readFile());
				}
			}
		}
		return result;
		
	}

}
