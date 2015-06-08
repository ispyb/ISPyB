package ispyb.server.biosaxs.services.utils.reader.dat;

import ispyb.server.biosaxs.services.core.analysis.primaryDataProcessing.PrimaryDataProcessing3Service;
import ispyb.server.biosaxs.vos.datacollection.Frame3VO;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FramesReaderFactory extends AbstractReaderFactory {
	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
	
	
	@Override
	public List<HashMap<String, Object>> getData(List<Integer> idList) throws Exception {
		PrimaryDataProcessing3Service primaryDataProcessingService = (PrimaryDataProcessing3Service) ejb3ServiceLocator.getLocalService(PrimaryDataProcessing3Service.class);
		List<Frame3VO> frames = primaryDataProcessingService.getFramesByIdList(idList);
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String,Object>>();
		for (Frame3VO frame : frames) {
			result.add(new OneDimensionalFileReader(frame.getFrameId(), frame.getFilePath()).readFile());
		}
		return result;
	}

}
