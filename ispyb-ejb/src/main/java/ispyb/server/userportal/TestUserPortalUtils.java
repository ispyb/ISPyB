package ispyb.server.userportal;

import java.util.List;

import generated.ws.smis.ExpSessionInfoLightVO;
import generated.ws.smis.ProposalParticipantInfoLightVO;
import generated.ws.smis.SampleSheetInfoLightVO;

public class TestUserPortalUtils {

	public TestUserPortalUtils() throws Exception {
		super();
	}

	public static void main(String args[]) {
		
		List<ExpSessionInfoLightVO> smisSessions_;
		List<ProposalParticipantInfoLightVO> mainProposers_ ;
		List<SampleSheetInfoLightVO> smisSamples_;
		List<ProposalParticipantInfoLightVO> labContacts_;

		try {
			System.out.println("*************** testUserPortalUtils ***************");
			smisSamples_ = UserPortalUtils.getSamples();
			mainProposers_ = UserPortalUtils.getMainProposers();
			smisSessions_ = UserPortalUtils.getSessions();
			labContacts_ = UserPortalUtils.getLabContacts();

		} catch (Exception e) {
			System.err.println(e.toString());
			e.printStackTrace();
		}
	}

}
