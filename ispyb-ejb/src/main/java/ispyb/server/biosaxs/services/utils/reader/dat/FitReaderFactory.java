package ispyb.server.biosaxs.services.utils.reader.dat;

import ispyb.server.biosaxs.services.core.analysis.advanced.AdvancedAnalysis3Service;
import ispyb.server.biosaxs.services.core.analysis.primaryDataProcessing.PrimaryDataProcessing3Service;
import ispyb.server.biosaxs.vos.advanced.FitStructureToExperimentalData3VO;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FitReaderFactory extends AbstractReaderFactory {
	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	@Override
	public List<HashMap<String, Object>> getData(List<Integer> idList) throws Exception {
		AdvancedAnalysis3Service advancedAnalysis3Service = (AdvancedAnalysis3Service) ejb3ServiceLocator.getLocalService(AdvancedAnalysis3Service.class);

		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		List<FitStructureToExperimentalData3VO> fits = advancedAnalysis3Service.getFitStructureToExperimentalDataByIds(idList);

		if (fits != null) {
			for (FitStructureToExperimentalData3VO fit : fits) {
				HashMap<String, Object> individualResult = new HashMap<String, Object>();
				individualResult.put("fit", new OneDimensionalFileReader(fit.getFitStructureToExperimentalDataId(), fit.getFitFilePath()).readFile());
				HashMap<String, Object> record = new HashMap<String, Object>();
				record.put(fit.getFitStructureToExperimentalDataId().toString(), individualResult);
				result.add(record);
			}
		}
		return result;

	}

}
