package ispyb.server.userportal;

import java.util.Iterator;
import java.util.List;

import generated.ws.smis.ExpSessionInfoLightVO;
import generated.ws.smis.ProposalParticipantInfoLightVO;
import generated.ws.smis.SampleSheetInfoLightVO;

public class TestUserPortalUtils {
	
	private static String jsonSamples = "[{\'acronym\':\'tryp\',\'categoryCode\':\'MX\',\'categoryCounter\':415,\'description\':\'Trypsin\',\'labAddress\':[null,\'71 avenue des Martyrs\',\'CS 40220\',null,\'38043\'],\'labCity\':\'GRENOBLE\',\'labCountryCode\':\'ESRF\',\'labName\':\'ESRF\',\'mainProposerEmail\':\'monaco@esrf.fr\',\'mainProposerFirstName\':\'Stéphanie\',\'mainProposerName\':\'MONACO\',\'mainProposerPk\':225239,\'mainProposerTitle\':\'Dr\',\'opmodePk\':1,\'pk\':148,\'proposalGroup\':103,\'proposalPk\':31529,\'proposalType\':3,\'spaceGroup\':\'\',\'userEmail\':\'mccarthy@esrf.fr\',\'userName\':\'Joanne MCCARTHY\',\'userPhone\':\'24-21\'}]";
	private static String jsonSessions = "[{'pk':79910,'experimentPk':115363,'startDate':{'year':2017,'month':4,'dayOfMonth':12,'hourOfDay':9,'minute':30,'second':0},'endDate':{'year':2017,'month':4,'dayOfMonth':13,'hourOfDay':8,'minute':0,'second':0},'shifts':3,'lostShifts':0,'reimbursedDays':4,'startShift':1,'beamlinePk':341,'beamlineName':'ID29','proposalPk':31529,'proposalSubmissionPk':1,'categCode':'MX','categCounter':415,'proposalTitle':'TEST','proposalOpmodePk':1,'proposalOpmodeCode':'Green','proposalType':3,'proposalTypeCode':'PROPSBM_MX_NON_BAG','proposalGroup':103,'proposalGroupCode':'Crystallography','runCode':'Run 2/17','runStartDate':{'year':2017,'month':2,'dayOfMonth':31,'hourOfDay':0,'minute':0,'second':0},'runEndDate':{'year':2017,'month':5,'dayOfMonth':8,'hourOfDay':0,'minute':0,'second':0},'isBagProposal':false,'isLongtermProposal':false,'hasAform':false,'sampleCount':0,'firstLocalContact':{'name':'MONACO','realName':'MONACO','firstName':'Stéphanie','email':'monaco@esrf.fr','phone':'+33 (0)4 76 88 20 84','title':'Dr','scientistPk':225239,'siteId':17074},'localContacts':[{'name':'MONACO','realName':'MONACO','firstName':'Stéphanie','email':'monaco@esrf.fr','phone':'+33 (0)4 76 88 20 84','title':'Dr','scientistPk':225239,'siteId':17074}],'mainProposer':{'name':'MONACO','realName':'MONACO','firstName':'Stéphanie','email':'monaco@esrf.fr','phone':'+33 (0)4 76 88 20 84','title':'Dr','scientistPk':225239,'siteId':17074},'cancelled':false,'finished':false,'jointExperiment':false,'localContactEmails':'monaco@esrf.fr','name':'MX/415 ID29 12-05-2017/13-05-2017','physicalBeamlineName':'ID29'}]";
	private static String jsonProposers = "[{'categoryCode':'MX','categoryCounter':415,'labAddress':[null,'71 avenue des Martyrs','CS 40220',null,'38043'],'labAddress1':'71 avenue des Martyrs','labAddress2':'CS 40220','labCity':'GRENOBLE','labCountryCode':'ESRF','labName':'ESRF','labPostalCode':'38043','laboratoryPk':200450,'proposalPk':31529,'proposalTitle':'TEST','scientistEmail':'monaco@esrf.fr','scientistFirstName':'Stéphanie','scientistName':'MONACO','scientistPk':225239,'siteId':17074}]";
	private static String jsonLabContacts = "[{'categoryCode':'MX','categoryCounter':415,'labAddress':[null,'71 avenue des Martyrs','CS 40220',null,'38043'],'labAddress1':'71 avenue des Martyrs','labAddress2':'CS 40220','labCity':'GRENOBLE','labCountryCode':'ESRF','labName':'ESRF','labPostalCode':'38043','laboratoryPk':200450,'mainProposer':false,'pk':330938,'proposalPk':31529,'proposalTitle':'TEST','proposalType':3,'proposer':false,'scientistEmail':'solange.delageniere@esrf.fr','scientistFirstName':'Solange','scientistName':'DELAGENIERE','scientistPk':284191,'scientistTitle':'Dr','siteId':91481,'user':true},{'categoryCode':'MX','categoryCounter':415,'labAddress':[null,'71 avenue des Martyrs','CS 40220',null,'38043'],'labAddress1':'71 avenue des Martyrs','labAddress2':'CS 40220','labCity':'GRENOBLE','labCountryCode':'ESRF','labName':'ESRF','labPostalCode':'38043','laboratoryPk':200450,'mainProposer':false,'pk':428429,'proposalGroup':103,'proposalPk':31529,'proposalTitle':'TEST','proposalType':3,'proposer':false,'scientistEmail':'sarah.guillot@esrf.fr','scientistFirstName':'Sarah','scientistName':'GUILLOT','scientistPk':282441,'scientistTitle':'Mme','siteId':190200}]";


	public TestUserPortalUtils() throws Exception {
		super();
	}

	public static void main(String args[]) {
		
		List<ExpSessionInfoLightVO> smisSessions_;
		List<ProposalParticipantInfoLightVO> mainProposers_ ;
		List<ProposalParticipantInfoLightVO> labContacts_;
		List<SampleSheetInfoLightVO> smisSamples_;		

		try {
			System.out.println("*************** testUserPortalUtils ***************");
			
			String proposal = "mx415";
			smisSessions_ = UserPortalUtils.getSessions(proposal);
			
			System.out.println("Importing sessions = "+ smisSessions_.size());
			int i=0;
			for (Iterator<ExpSessionInfoLightVO> iterator = smisSessions_.iterator(); iterator.hasNext();) {
				ExpSessionInfoLightVO vo = (ExpSessionInfoLightVO) iterator.next();
				System.out.println("session : " + i +  vo.getBeamlineName() + vo.getStartDate() );
				i++;
			}

			smisSessions_ = UserPortalUtils.jsonToSessionsList(jsonSessions);
			
			System.out.println("Importing sessions = "+ smisSessions_.size());
			i=0;
			for (Iterator<ExpSessionInfoLightVO> iterator = smisSessions_.iterator(); iterator.hasNext();) {
				ExpSessionInfoLightVO vo = (ExpSessionInfoLightVO) iterator.next();
				System.out.println("session : " + i +  vo.getBeamlineName() + vo.getStartDate() );
				i++;
			}
			
			//smisSamples_ = UserPortalUtils.jsonToSamplesList(jsonSamples);
			smisSamples_ = UserPortalUtils.getSamples(proposal);
			
			System.out.println("Importing samples = "+ smisSamples_.size());
			i=0;
			for (Iterator<SampleSheetInfoLightVO> iterator = smisSamples_.iterator(); iterator.hasNext();) {
				SampleSheetInfoLightVO vo = (SampleSheetInfoLightVO) iterator.next();
				System.out.println("sample : " + i +  vo.getAcronym() + vo.getDescription() );
				i++;
			}
		
			//mainProposers_ = UserPortalUtils.jsonToProposersList(jsonProposers);
			mainProposers_ = UserPortalUtils.getMainProposers(proposal);
			
			System.out.println("Importing proposers = "+ mainProposers_.size());
			i=0;
			for (Iterator<ProposalParticipantInfoLightVO> iterator = mainProposers_.iterator(); iterator.hasNext();) {
				ProposalParticipantInfoLightVO vo = (ProposalParticipantInfoLightVO) iterator.next();
				System.out.println("proposer : " + i +  vo.getScientistName() + vo.getScientistFirstName() );
				i++;
			}

			
			//labContacts_ = UserPortalUtils.jsonToProposersList(jsonLabContacts);
			labContacts_ = UserPortalUtils.getLabContacts(proposal);
			
			System.out.println("Importing lab contacts = "+ labContacts_.size());
			i=0;
			for (Iterator<ProposalParticipantInfoLightVO> iterator = labContacts_.iterator(); iterator.hasNext();) {
				ProposalParticipantInfoLightVO vo = (ProposalParticipantInfoLightVO) iterator.next();
				System.out.println("lab contact : " + i +  vo.getScientistName() + vo.getScientistFirstName() + vo.getLabName() + vo.getLabAddress1() );
				i++;
			}
			
		} catch (Exception e) {
			System.err.println(e.toString());
			e.printStackTrace();
		}
	}

}
