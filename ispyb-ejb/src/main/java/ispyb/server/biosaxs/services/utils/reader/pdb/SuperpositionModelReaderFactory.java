package ispyb.server.biosaxs.services.utils.reader.pdb;

import ispyb.server.biosaxs.services.core.analysis.abInitioModelling.AbInitioModelling3Service;
import ispyb.server.biosaxs.services.core.analysis.advanced.AdvancedAnalysis3Service;
import ispyb.server.biosaxs.vos.advanced.Superposition3VO;
import ispyb.server.biosaxs.vos.datacollection.Model3VO;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SuperpositionModelReaderFactory extends PDBAbstractReaderFactory {
	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private HashMap<String, String> getModel() {
		HashMap<String, String> model = new HashMap<String, String>();
		model.put("id", null);
		model.put("fileName", null);
		return model;
	}

	@Override
	List<HashMap<String, Object>> getData(List<Integer> idList) throws Exception {
//		ArrayList<HashMap<String, HashMap<String, String>>> result = new ArrayList<HashMap<String, HashMap<String, String>>>();
		ArrayList<HashMap<String,Object>> result = new ArrayList<HashMap<String, Object>>();
		
		AdvancedAnalysis3Service advancedAnalysis = (AdvancedAnalysis3Service) ejb3ServiceLocator.getLocalService(AdvancedAnalysis3Service.class);
		
		for (Integer superpositionId : idList) {
			HashMap<String, HashMap<String, String>> record = new HashMap<String, HashMap<String, String>>();
			
			Superposition3VO superposition = advancedAnalysis.getSuperpositionById(superpositionId);
			if (superposition != null){
				
				/** Abinitio **/
				record.put("abinitio",getModel());
				if (superposition.getAbinitioModelPdbFilePath() != null){
					record.get("abinitio").put("id", superposition.getSuperpositionId().toString());
					if (new File(superposition.getAbinitioModelPdbFilePath()).exists()){
						record.get("abinitio").put("fileName", superposition.getAbinitioModelPdbFilePath());
						record.get("abinitio").put("XYZ", PDBParser.getPDBContent(superposition.getAbinitioModelPdbFilePath()));
					}
				}
				
				/** Aligned **/
				record.put("aligned",getModel());
				if (superposition.getAlignedPdbFilePath() != null){
					record.get("aligned").put("id", superposition.getSuperpositionId().toString());
					if (new File(superposition.getAlignedPdbFilePath()).exists()){
						record.get("aligned").put("fileName", superposition.getAlignedPdbFilePath());
						record.get("aligned").put("XYZ", PDBParser.getPDBContent(superposition.getAlignedPdbFilePath()));
					}
				}
				
				/** Apriori **/
				record.put("apriori",getModel());
				String filePath = superposition.getAprioriPdbFilePath();
				if (filePath != null){
					record.get("apriori").put("id", superposition.getSuperpositionId().toString());
					if (new File(filePath).exists()){
						record.get("apriori").put("fileName", filePath);
						record.get("apriori").put("data", PDBParser.fileToString(filePath));
					}
				}
				
				
				HashMap<String, Object> x = new HashMap<String, Object>();
				x.put(superposition.getSuperpositionId().toString(), record);
				result.add(x);
			}
		}
		return result;
	}

	@Override
	List<HashMap<String, Object>> getDataXYZ(List<HashMap<String, String>> propertiesList) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
