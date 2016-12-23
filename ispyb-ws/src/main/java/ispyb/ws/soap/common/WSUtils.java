package ispyb.ws.soap.common;

import generated.ws.smis.SMISWebService;
import ispyb.server.common.util.LoggerFormatter;
import ispyb.server.smis.UpdateFromSMIS;
import ispyb.server.webservice.smis.util.SMISWebServiceGenerator;

import org.apache.log4j.Logger;

public class WSUtils {
	
	private final static Logger LOGGER = Logger.getLogger(WSUtils.class);
	
	public static void UpdateProposal(String proposalCode, String proposalNumber) {
		
		try {
			// To be sure that SMIS or SUN set proposals, samples ... are synch with ISPyB
			// we setup an update from SMISWebService for specific WS Client calls 
			// instead of setting up an additional scheduler to retrieve every x days for a limited number of proposals
			Long pk = new Long(1);
			SMISWebService wsSOLEIL = SMISWebServiceGenerator.getWs();
			pk = wsSOLEIL.getProposalPK(proposalCode, Long.parseLong(proposalNumber));
			UpdateFromSMIS.updateThisProposalFromSMISPk(pk);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "UpdateProposal", System.currentTimeMillis(), System.currentTimeMillis(), e.getMessage(), e);
		}
	}

}
