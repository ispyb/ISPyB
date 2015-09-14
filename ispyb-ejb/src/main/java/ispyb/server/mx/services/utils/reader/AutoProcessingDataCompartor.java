package ispyb.server.mx.services.utils.reader;

import java.util.Comparator;

public class AutoProcessingDataCompartor implements Comparator<AutoProcessingData> {
	 	@Override
	    public int compare(AutoProcessingData o1, AutoProcessingData o2) {
	        return o1.getResolutionLimit().compareTo(o2.getResolutionLimit());
	    }
	 	
}

