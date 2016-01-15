package ispyb.server.common.test.services;

import generated.ws.smis.ExpSessionInfoLightVO;
import generated.ws.smis.ProposalParticipantInfoLightVO;
import generated.ws.smis.SMISWebService;
import generated.ws.smis.SMISWebService_Service;
import generated.ws.smis.SampleSheetInfoLightVO;

import java.util.Map;

import javax.xml.ws.BindingProvider;

public class SmisWebServiceTest {

	protected static SMISWebService wsPort;

	public SmisWebServiceTest() throws Exception {
		super();
		initWebService();
	}

	private static void initWebService() throws Exception {
		// Get the services for ISPyB
		SMISWebService_Service sws = new SMISWebService_Service();	
		wsPort = sws.getSMISWebServiceBeanPort();

		//SMISWebService ws=service.get
		BindingProvider bindingProvider = (BindingProvider)wsPort;
		Map requestContext = bindingProvider.getRequestContext();
		
		requestContext.put(BindingProvider.USERNAME_PROPERTY, "****");
		requestContext.put(BindingProvider.PASSWORD_PROPERTY, "****");

	}

	public static void main(String args[]) {
		try {

			System.out.println("*************** testSmisWebServices ***************");
			initWebService();
			 getProposalPk();
			// findMainProposersForProposal();
			//findSessionsForProposal();
			// findNewMXProposalPks();
			// findSamplesheetsForProposal();
			// findMainProposersForProposalNames();

		} catch (Exception e) {
			System.err.println(e.toString());
			e.printStackTrace();
		}
	}

	private static void getProposalPk() throws Exception {
		System.out.println("*************** test getProposalPk() ***************");
		String code = "MX";
		Long number = 415L;
		Long pk = wsPort.getProposalPK(code, number);

		System.out.println("This is proposalPk : " + pk);
	}

//	private static void findMainProposersForProposal() throws Exception {
//		System.out.println("*************** test findMainProposersForProposal() ***************");
//		String code = "MX";
//		Integer number = new Integer(415);
//		Long pk = wsPort.getProposalPK(code, number);
//		ProposalParticipantInfoLightVO[] vos = wsPort.findMainProposersForProposal(pk);
//		for (int i = 0; i < vos.length; i++) {
//			System.out.println("This is labo : " + vos[i].getLabName());
//			System.out.println("\n");
//		}
//
//		System.out.println("This is proposalPk : " + pk);
//	}
//
//	private static void findSessionsForProposal() throws Exception {
//		System.out.println("*************** test findSessionsForProposal() ***************");
//		String code = "IX";
//		Integer number = new Integer(5);
//		Long pk = ws.getProposalPK(code, number);
//		ExpSessionInfoLightVO[] vos = ws.findSessionsInfoLightForProposalPk(pk);
//		for (int i = 0; i < vos.length; i++) {
//			System.out.println("This is session : " + vos[i].getBeamlineName() + "startDate : " + vos[i].getStartDate().getTime());
//			System.out.println("\n");
//		}
//		System.out.println(pk);
//	}
//
//	private static void findSamplesheetsForProposal() throws Exception {
//		System.out.println("*************** test findSamplesheetsForProposal() ***************");
//		String code = "MX";
//		Integer number = new Integer(415);
//		Long pk = ws.getProposalPK(code, number);
//		SampleSheetInfoLightVO[] vos = ws.findSamplesheetInfoLightForProposalPk(pk);
//		for (int i = 0; i < vos.length; i++) {
//			System.out.println("This is samplesheet : " + vos[i].getAcronym());
//			System.out.println("\n");
//		}
//
//	}
//
//	private static void findNewMXProposalPks() throws Exception {
//		System.out.println("*************** test findNewMXProposalPks() ***************");
//		String startDateStr = "25/03/2015";
//		String endDateStr = "06/04/2015";
//		long[] pks = ws.findNewMXProposalPKs(startDateStr, endDateStr);
//		if (pks == null || pks.length < 1) {
//			System.out.println("No new proposals");
//		} else {
//			for (int i = 0; i < pks.length; i++) {
//				System.out.println("This is proposal : " + pks[i]);
//				System.out.println("\n");
//			}
//		}
//
//	}
//
//	private static void findMainProposersForProposalNames() throws Exception {
//		System.out.println("*************** test findMainProposersForProposal() ***************");
//		String code = "MX";
//		Integer number = new Integer(415);
//		Long pk = ws.getProposalPK(code, number);
//		String name = "Monaco";
//		String firstName = "s%";
//		ProposalParticipantInfoLightVO[] vos = ws.findScientistsForProposalByNameAndFirstName(pk, name, firstName);
//		if (vos != null) {
//			for (int i = 0; i < vos.length; i++) {
//				System.out.println("This is labo : " + vos[i].getLabName());
//				System.out.println("\n");
//			}
//		} else {
//			System.out.println("no proposers with this name : " + name);
//		}
//
//	}
}
