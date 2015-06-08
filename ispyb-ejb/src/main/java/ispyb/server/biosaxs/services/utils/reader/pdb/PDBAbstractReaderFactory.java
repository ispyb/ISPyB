package ispyb.server.biosaxs.services.utils.reader.pdb;

import java.util.HashMap;
import java.util.List;

public abstract class PDBAbstractReaderFactory {
	abstract List<HashMap<String, Object>> getData(List<Integer> idList) throws Exception;
	abstract List<HashMap<String, Object>> getDataXYZ(List<HashMap<String, String>> propertiesList) throws Exception;
}
