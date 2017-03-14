package ispyb.ws.soap.common;

import org.apache.log4j.Logger;

import ispyb.server.common.util.LoggerFormatter;
import ispyb.server.smis.UpdateFromSMIS;

public class WSUtils {
	
	private final static Logger LOGGER = Logger.getLogger(WSUtils.class);
	
	public static void UpdateProposal(String proposalCode, String proposalNumber) {
		
		try {		
			UpdateFromSMIS.updateProposalFromSMIS(proposalCode, proposalNumber);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "UpdateProposal", System.currentTimeMillis(), System.currentTimeMillis(), e.getMessage(), e);
		}
	}

}
