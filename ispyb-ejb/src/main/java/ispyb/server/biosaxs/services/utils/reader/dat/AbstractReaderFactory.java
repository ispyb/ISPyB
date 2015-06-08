package ispyb.server.biosaxs.services.utils.reader.dat;

import java.util.HashMap;
import java.util.List;

public abstract class AbstractReaderFactory {
	abstract List<HashMap<String, Object>> getData(List<Integer> idList) throws Exception;
}
