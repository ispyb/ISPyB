/*************************************************************************************************
 * This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ISPyB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
 ****************************************************************************************************/

package ispyb.server.smis;

import generated.ws.smis.ExpSessionInfoLightVO;
import generated.ws.smis.ProposalParticipantInfoLightVO;
import generated.ws.smis.SMISWebService;
import generated.ws.smis.SampleSheetInfoLightVO;
import ispyb.common.util.Constants;
import ispyb.common.util.Constants.SITE;
import ispyb.common.util.StringUtils;
import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.common.services.proposals.LabContact3Service;
import ispyb.server.common.services.proposals.Laboratory3Service;
import ispyb.server.common.services.proposals.Person3Service;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.util.LoggerFormatter;
import ispyb.server.common.util.LoggerFormatter.Package;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Laboratory3VO;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.services.collections.BeamLineSetup3Service;
import ispyb.server.mx.services.collections.Session3Service;
import ispyb.server.mx.services.sample.Crystal3Service;
import ispyb.server.mx.services.sample.Protein3Service;
import ispyb.server.mx.vos.collections.BeamLineSetup3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.Protein3VO;
import ispyb.server.webservice.smis.util.SMISWebServiceGenerator;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class UpdateFromSMIS {

	private static final Logger LOG = Logger.getLogger(UpdateFromSMIS.class);

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private static Proposal3Service proposal;
	private static Laboratory3Service lab;
	private static Person3Service person;
	private static Session3Service session;
	private static Protein3Service protein;
	private static Crystal3Service crystal;
	private static BeamLineSetup3Service setup;
	private static LabContact3Service labContactService;

	public void updateFromSMIS() throws Exception {

		Date today = Calendar.getInstance().getTime();

		// better to do it over more than 1 day, to be sure to recover all
		// that's why a day is 26h long !

		long yesterdayL = today.getTime() - (26 * 3600 * 1000);
		Date yesterday = new Date(yesterdayL);

		SimpleDateFormat simple = new SimpleDateFormat("dd/MM/yyyy");
		String endDateStr = simple.format(today);
		String startDateStr = simple.format(yesterday);

		if (startDateStr == null || startDateStr.length() == 0) {
			startDateStr = simple.format(today);
		}
		updateFromSMIS(startDateStr, endDateStr);
	}

	private static void initServices() throws Exception {

		proposal = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
		lab = (Laboratory3Service) ejb3ServiceLocator.getLocalService(Laboratory3Service.class);
		person = (Person3Service) ejb3ServiceLocator.getLocalService(Person3Service.class);
		session = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
		protein = (Protein3Service) ejb3ServiceLocator.getLocalService(Protein3Service.class);
		crystal = (Crystal3Service) ejb3ServiceLocator.getLocalService(Crystal3Service.class);
		setup = (BeamLineSetup3Service) ejb3ServiceLocator.getLocalService(BeamLineSetup3Service.class);
		labContactService = (LabContact3Service) ejb3ServiceLocator.getLocalService(LabContact3Service.class);
	}

	public static void updateFromSMIS(String startDateStr, String endDateStr) throws Exception {

		LOG.debug("update ISPyB database start date = " + startDateStr + " end date = " + endDateStr);

		// Get the service
		SMISWebService wsInit = SMISWebServiceGenerator.getWs();

		LOG.debug("getting SMIS WS");

		// retrieve all new proposals
		List<Long> newProposalPks = null;
		newProposalPks = wsInit.findNewMXProposalPKs(startDateStr, endDateStr);

		int nbFoundESRF = 0;
		if (newProposalPks != null && newProposalPks.size() > 0) {

			LOG.debug("Nb of new proposals found : " + newProposalPks.size());

			for (Iterator<Long> iterator = newProposalPks.iterator(); iterator.hasNext();) {
				Long pk = (Long) iterator.next();

				if (Constants.SITE_IS_ESRF()) {
					// in case of ESRF we do not want old proposals
					if (pk.longValue() > 20000) {
						updateThisProposalFromSMISPk(pk);
						nbFoundESRF = nbFoundESRF + 1;
					} else {
						LOG.debug("proposal with pk = "+ pk.toString() + " is an old one, not updated ");
					}
				} else
					updateThisProposalFromSMISPk(pk);

			}
		}
		LOG.info("Update of ISPyB is finished, nbFound for ESRF = " + nbFoundESRF);
	}

	public static void updateThisProposalFromSMISPk(Long pk) throws Exception {
		System.out.println("updateThisProposalFromSMISPk " + pk);

		Integer nbDays = 365;

		initServices();

		LOG.debug("--------------------- ws created looking for proposalPk =  " + pk);

		List<ExpSessionInfoLightVO> smisSessions_;

		// Get the service
		SMISWebService sws = SMISWebServiceGenerator.getWs();

		switch (Constants.getSite()) {
		case ESRF:
			// only sessions WITH local contacts are retrieved
			smisSessions_ = sws.findRecentSessionsInfoLightForProposalPkAndDays(pk, nbDays);
			break;
		case EMBL:
			smisSessions_ = sws.findRecentSessionsInfoLightForProposalPk(pk);
			break;
		default:
		case SOLEIL:
			smisSessions_ = sws.findRecentSessionsInfoLightForProposalPkAndDays(pk, nbDays);
			break;
		}

		List<ProposalParticipantInfoLightVO> mainProposers_ = sws.findMainProposersForProposal(pk);
		ProposalParticipantInfoLightVO[] mainProposers = new ProposalParticipantInfoLightVO[mainProposers_.size()];
		mainProposers = mainProposers_.toArray(mainProposers);

		ExpSessionInfoLightVO[] smisSessions = new ExpSessionInfoLightVO[smisSessions_.size()];
		smisSessions = smisSessions_.toArray(smisSessions);

		List<SampleSheetInfoLightVO> smisSamples_ = sws.findSamplesheetInfoLightForProposalPk(pk);
		SampleSheetInfoLightVO[] smisSamples = new SampleSheetInfoLightVO[smisSamples_.size()];
		smisSamples = smisSamples_.toArray(smisSamples);

		List<ProposalParticipantInfoLightVO> labContacts_ = sws.findParticipantsForProposal(pk);
		ProposalParticipantInfoLightVO[] labContacts = new ProposalParticipantInfoLightVO[labContacts_.size()];
		labContacts = labContacts_.toArray(labContacts);

		LOG.info("Nb of proposers found : " + mainProposers.length);
		LOG.info("Nb of sessions found : " + smisSessions.length);
		LOG.info("Nb of samplesheets found : " + smisSamples.length);

		initServices();

		Integer proposalId = null;

		// -----------------------------------------------------------------------------------
		// create the proposal + person + labo if it does not exist
		// -----------------------------------------------------------------------------------
		ArrayList<ProposalParticipantInfoLightVO> mxProposers = new ArrayList<ProposalParticipantInfoLightVO>();
		ArrayList<ProposalParticipantInfoLightVO> bxProposers = new ArrayList<ProposalParticipantInfoLightVO>();
		if (Constants.SITE_IS_SOLEIL()) {
			if (mainProposers != null && mainProposers.length > 0) {
				for (ProposalParticipantInfoLightVO proposer : mainProposers) {
					if (proposer.getCategoryCode().equalsIgnoreCase("mx")) {
						mxProposers.add(proposer);
						LOG.debug(" mx proposers for propos_no = " + pk + " | size = " + mxProposers.size());
					} else if (proposer.getCategoryCode().equalsIgnoreCase("bx")) {
						bxProposers.add(proposer);
						LOG.debug(" bx proposers for propos_no = " + pk + " | size = " + bxProposers.size());
					}
				}
			}
		}
		
		if (!Constants.SITE_IS_SOLEIL()) {
			loadProposers(mainProposers);
		} else if (Constants.SITE_IS_SOLEIL()) {
			if (mxProposers != null && !mxProposers.isEmpty()) {
				LOG.debug(" search for mx sessions for propos_no = " + pk + " | size = " + mxProposers.size());
				mainProposers = new ProposalParticipantInfoLightVO[mxProposers.size()];
				mainProposers = mxProposers.toArray(mainProposers);
				loadProposers(mainProposers);
			}
			if (bxProposers != null && !bxProposers.isEmpty()) {
				LOG.debug(" search for bx sessions for propos_no = " + pk);
				mainProposers = new ProposalParticipantInfoLightVO[bxProposers.size()];
				mainProposers = bxProposers.toArray(mainProposers);
				loadProposers(mainProposers);
			}
		}

		// -----------------------------------------------------------------------------------
		// the proposal is created : load samples and sessions
		// -----------------------------------------------------------------------------------
		ArrayList<ExpSessionInfoLightVO> mxSessions = new ArrayList<ExpSessionInfoLightVO>();
		ArrayList<ExpSessionInfoLightVO> bxSessions = new ArrayList<ExpSessionInfoLightVO>();
		if (Constants.SITE_IS_SOLEIL()) {
			if (smisSessions != null && smisSessions.length > 0) {
				for (ExpSessionInfoLightVO session : smisSessions) {
					if (session.getCategCode().equalsIgnoreCase("mx")) {
						mxSessions.add(session);
					} else if (session.getCategCode().equalsIgnoreCase("bx")) {
						bxSessions.add(session);
					}
				}
			}
		}
		
		if (!Constants.SITE_IS_SOLEIL()) {
			loadSessions(smisSessions);
		} else if (Constants.SITE_IS_SOLEIL()) {
			if (mxSessions != null && !mxSessions.isEmpty()) {
				LOG.debug(" search for mx sessions for propos_no = " + pk);
				smisSessions = new ExpSessionInfoLightVO[mxSessions.size()];
				smisSessions = mxSessions.toArray(smisSessions);
				loadSessions(smisSessions);
			}
			if (bxSessions != null && !bxSessions.isEmpty()) {
				LOG.debug(" search for bx sessions for propos_no = " + pk);
				smisSessions = new ExpSessionInfoLightVO[bxSessions.size()];
				smisSessions = bxSessions.toArray(smisSessions);
				loadSessions(smisSessions);
			}
		}
		
		ArrayList<SampleSheetInfoLightVO> mxSamples = new ArrayList<SampleSheetInfoLightVO>();
		ArrayList<SampleSheetInfoLightVO> bxSamples = new ArrayList<SampleSheetInfoLightVO>();
		if (Constants.SITE_IS_SOLEIL()) {
			if (smisSamples != null && smisSamples.length > 0) {
				for (SampleSheetInfoLightVO sample : smisSamples) {
					if (sample.getCategoryCode().equalsIgnoreCase("mx")) {
						mxSamples.add(sample);
					} else if (sample.getCategoryCode().equalsIgnoreCase("bx")) {
						bxSamples.add(sample);
					}
				}
			}
		}
		
		if (!Constants.SITE_IS_SOLEIL()) {
			loadSamples(smisSamples);
		} else if (Constants.SITE_IS_SOLEIL()) {
			if (mxSamples != null && !mxSamples.isEmpty()) {
				LOG.debug(" search for mx samples for propos_no = " + pk);
				smisSamples = new SampleSheetInfoLightVO[mxSamples.size()];
				smisSamples = mxSamples.toArray(smisSamples);
				loadSamples(smisSamples);
			}
			if (bxSamples != null && !bxSamples.isEmpty()) {
				LOG.debug(" search for bx samples for propos_no = " + pk);
				smisSamples = new SampleSheetInfoLightVO[bxSamples.size()];
				smisSamples = bxSamples.toArray(smisSamples);
				loadSamples(smisSamples);
			}
		}

		// -----------------------------------------------------------------------------------
		// the proposal, samples and sessions are created: load labcontacts
		// -----------------------------------------------------------------------------------
		if (labContacts != null && labContacts.length > 0) {
			for (int i = 0; i < labContacts.length; i++) {
				ProposalParticipantInfoLightVO labContact = labContacts[i];
				labContact.getCategoryCode();
				String uoCode = labContact.getCategoryCode();
				String proposalNumber = labContact.getCategoryCounter() != null ? labContact.getCategoryCounter().toString() : "";
				String proposalCode = StringUtils.getProposalCode(uoCode, proposalNumber);

				LOG.debug("Proposal found : " + proposalCode + proposalNumber + " uoCode = " + uoCode);
				// create or update the person and his/her laboratory
				getProposal(labContact, lab, person, proposalNumber, proposalCode);

				// retrieve person to get it 'personId' for persistence of the
				// labContact
				String familyName = labContacts[i].getScientistName();
				String givenName = labContacts[i].getScientistFirstName();
				List<Person3VO> persons = person.findByFamilyAndGivenName(familyName, givenName);
				Person3VO currentPerson = null;
				if (persons != null && !persons.isEmpty()) {
					currentPerson = persons.get(0);
					LOG.debug("currentPerson Id : " + currentPerson.getPersonId() + " inside ISPyB db");
				}

				// retrieve proposal to get 'proposalId' for persistence of the
				// labContact
				Proposal3VO currentProposal = proposal
						.findForWSByCodeAndNumber(proposalCode, proposalNumber.toString());
				if (currentProposal == null) {
					LOG.debug("proposal not found for:" + proposalCode + proposalNumber.toString());
					continue;
				}
				LOG.debug("currentProposal Id : " + currentProposal.getProposalId() + " inside ISPyB db");

				List<LabContact3VO> labContactsList = null;
				if (currentPerson != null)
					labContactsList = labContactService.findByPersonIdAndProposalId(currentPerson.getPersonId(),
							currentProposal.getProposalId());
				if (labContactsList != null && !labContactsList.isEmpty()) {
					LOG.debug("labContact already exists");
					continue;
				}

				// fill the laboratory
				Laboratory3VO currentLabo = ScientistsFromSMIS.extractLaboratoryInfo(labContacts[i]);
				//
				LabContact3VO labContact3VO = new LabContact3VO();
				labContact3VO.setPersonVO(currentPerson);
				// generate labContact infos
				if (currentPerson != null & currentLabo != null) {
					labContact3VO.setCardName(generateCardName(currentPerson, currentLabo,
							currentProposal.getProposalId()));
				}
				labContact3VO.setProposalVO(currentProposal);
				labContact3VO.setDewarAvgCustomsValue(0);
				labContact3VO.setDewarAvgTransportValue(0);
				labContactService.create(labContact3VO);
				LOG.debug("inserted a new labcontact : " + labContact3VO.getCardName() + " for proposal " + proposalId
						+ " inside ISPyB db");
			}
		}

	}

	@SuppressWarnings("unused")
	private static void loadProposers(ProposalParticipantInfoLightVO[] mainProposers) throws Exception {
		String proposalNumber = null;
		
		if (mainProposers != null && mainProposers.length > 0) {

			ProposalParticipantInfoLightVO mainProp = mainProposers[0];
			mainProp.getCategoryCode();
			String uoCode = mainProp.getCategoryCode();
			proposalNumber = mainProp.getCategoryCounter() != null ? mainProp.getCategoryCounter().toString() : "";
			String proposalCode = StringUtils.getProposalCode(uoCode, proposalNumber);

			LOG.debug("Proposal found : " + proposalCode + proposalNumber + " uoCode = " + uoCode);
			LOG.debug("Bllogin : " + mainProp.getBllogin());

			List<Proposal3VO> listProposals = proposal.findByCodeAndNumber(proposalCode, proposalNumber, false, false, false);
			if (listProposals.isEmpty()) {
				// the proposal does not belong yet to ISPyB database

				LOG.debug("proposal is new");

				Proposal3VO propv = getProposal(mainProp, lab, person, proposalNumber, proposalCode);

				Integer proposalId = proposal.create(propv).getProposalId();

				LOG.debug("inserted a new proposal inside ISPyB db:" + proposalCode + proposalNumber);
			} else {
				// proposal already exists: update information for the main
				// proposer and the laboratory if needed
				// Issue 1656
				LOG.debug("proposal already exists");
				Proposal3VO proposalVO = listProposals.get(0);
				Person3VO currentPerson = proposalVO.getPersonVO();
				String currentFamilyName = currentPerson.getFamilyName();
				String currentGivenName = currentPerson.getGivenName();
				// main proposer
				String familyName = mainProp.getScientistName();
				String givenName = mainProp.getScientistFirstName();

				if (Constants.getSite().equals(SITE.EMBL)) {
					if (!StringUtils.matchString(mainProp.getBllogin(), currentPerson.getLogin())) {
						currentPerson.setLogin(mainProp.getBllogin());
						person.update(currentPerson);
						LOG.debug("Update person");
					}
				}

				if (!StringUtils.matchString(currentFamilyName, familyName)
						|| !StringUtils.matchString(currentGivenName, givenName)) {
					// proposer has changed
					Integer oldPersonId = proposalVO.getPersonVOId();
					Proposal3VO propv = getProposal(mainProp, lab, person, proposalNumber, proposalCode);
					proposalVO.setPersonVO(propv.getPersonVO());
					proposalVO.setTitle(propv.getTitle());
					proposalVO.setType(propv.getType());
					proposal.update(proposalVO);
					LOG.debug("proposal updated " + proposalVO.getProposalId() + " (" + oldPersonId + " -> "
							+ propv.getPersonVOId() + ")");
				}
			}
		} else {
			LOG.debug("No main proposers found for propos_no = " + proposalNumber);
		}
	}
	
	private static void loadSessions(ExpSessionInfoLightVO[] smisSessions) throws Exception {
		String proposalNumber = null;
		
		if (smisSessions != null && smisSessions.length > 0) {

			String uoCode = smisSessions[0].getCategCode();
			proposalNumber = smisSessions[0].getCategCounter() != null ? smisSessions[0].getCategCounter().toString() : "";
			String proposalCode = StringUtils.getProposalCode(uoCode, proposalNumber);

			LOG.debug("Proposal found : " + proposalCode + proposalNumber + " uoCode = " + uoCode);

			List<Proposal3VO> existingProposalList = proposal.findByCodeAndNumber(proposalCode, proposalNumber, false, false, false);

			if (existingProposalList.size() > 1)
				LOG.debug("error ! duplicate code and number in ISPyB database");
			Proposal3VO proplv = existingProposalList.get(0);
			Integer proposalId = proplv.getProposalId();
			LOG.debug("proposal is not new, proposalId = " + proposalId);

			// retrieve all new sessions
			for (int k = 0; k < smisSessions.length; k++) {
				ExpSessionInfoLightVO sessionVO = smisSessions[k];

				retrieveSession(proplv, sessionVO);

			}

		} else {
			LOG.debug(" no sessions found for propos_no = " + proposalNumber);

		}	
	}
	
	private static void loadSamples(SampleSheetInfoLightVO[] smisSamples) throws Exception {
		String proposalNumber = null;
		
		if (smisSamples != null && smisSamples.length > 0) {

			String uoCode = smisSamples[0].getCategoryCode();
			proposalNumber = smisSamples[0].getCategoryCounter() != null ? smisSamples[0].getCategoryCounter().toString() : "";
			String proposalCode = StringUtils.getProposalCode(uoCode, proposalNumber);
System.out.println("UpdateFromSMS proposalCode = " + proposalCode + " | proposalNumber = " + proposalNumber);
			LOG.debug("Proposal found : " + proposalCode + proposalNumber + " uoCode = " + uoCode);

			List<Proposal3VO> existingProposalList = proposal.findByCodeAndNumber(proposalCode, proposalNumber, false, false, false);
System.out.println("UpdateFromSMS existingProposalList.size = " + existingProposalList + " | existingProposalList = " + existingProposalList);
			if (existingProposalList.size() > 1)
				LOG.debug("error ! duplicate code and number in ISPyB database");
			Proposal3VO proplv = existingProposalList.get(0);
			Integer proposalId = proplv.getProposalId();
			LOG.debug("proposal is not new, proposalId = " + proposalId);

			// retrieve all new samplesheets

			LOG.debug("searching for new samplesheets");

			/**
			 * For Biosaxs, copying proteins to macromolecule table if they
			 * don't exist yet
			 **/
			if (proplv.getType().equals("MB") || proplv.getType().equals("BX")) {
				for (SampleSheetInfoLightVO sampleSheetInfoLightVO : smisSamples) {
					try {
						SaxsProposal3Service saxsProposal3Service = (SaxsProposal3Service) ejb3ServiceLocator
								.getLocalService(SaxsProposal3Service.class);
						/**
						 * If there is not any macromolecule with that acronym
						 * and proposalId it will be created
						 **/
						if (saxsProposal3Service.findMacromoleculesBy(sampleSheetInfoLightVO.getAcronym(), proposalId)
								.size() == 0) {
							saxsProposal3Service.merge(new Macromolecule3VO(proposalId, sampleSheetInfoLightVO
									.getAcronym(), sampleSheetInfoLightVO.getAcronym(), sampleSheetInfoLightVO
									.getDescription()));
						}
					} catch (Exception e) {
						e.printStackTrace();
						LoggerFormatter.log(LOG, Package.BIOSAXS_DB, "updateThisProposalFromSMISPk",
								System.currentTimeMillis(), System.currentTimeMillis(), e.getMessage(), e);
					}
				}
			}

			for (int j = 0; j < smisSamples.length; j++) {

				SampleSheetInfoLightVO value = smisSamples[j];
				retrieveSampleSheet(proplv, value);

			}
		} else {
			LOG.debug(" no samples found for propos_no = " + proposalNumber);
		}
	}
	
	private static void retrieveSampleSheet(Proposal3VO proplv, SampleSheetInfoLightVO value) throws Exception {

		Protein3VO plv = new Protein3VO();
		Crystal3VO clv = new Crystal3VO();

		initServices();

		List<Protein3VO> protlist = protein.findByAcronymAndProposalId(proplv.getProposalId(), value.getAcronym());

		if (protlist.isEmpty()) {

			plv.setAcronym(value.getAcronym());
			plv.setProposalVO(proplv);
			// SV
			Person3VO persv = new Person3VO();

			if (person.findByFamilyAndGivenName(value.getUserName(), null).isEmpty()) {
				persv.setFamilyName(value.getUserName());
				persv.setEmailAddress(value.getUserEmail());
				persv.setPhoneNumber(value.getUserPhone());
				persv = person.create(persv);
				LOG.debug("getting SMIS WS person created");
			} else {
				persv = person.findByFamilyAndGivenName(value.getUserName(), null).get(0);
				persv.setPhoneNumber(value.getUserPhone());
				persv = person.update(persv);
				LOG.debug("getting SMIS WS person already exists personId = " + persv.getPersonId()
						+ " | for proposalId = " + proplv.getProposalId());
			}

			plv.setPersonId(persv.getPersonId());
			String desc = value.getDescription();

			if (desc.length() > 254)
				desc = desc.substring(0, 254);
			plv.setName(desc);
			plv.setIsCreatedBySampleSheet((byte) 1);

			clv.setSpaceGroup(value.getSpaceGroup());
			clv.setCellA(value.getCellA());
			clv.setCellB(value.getCellB());
			clv.setCellC(value.getCellC());
			clv.setCellAlpha(value.getCellAlpha());
			clv.setCellBeta(value.getCellBeta());
			clv.setCellGamma(value.getCellGamma());

			plv = protein.create(plv);
			clv.setProteinVO(plv);
			crystal.create(clv);

			LOG.debug("inserted a new protein+crystal inside ISPyB db:" + value.getAcronym());
		} else {
			Protein3VO prot = protlist.get(0);
			prot = protein.findByPk(prot.getProteinId(), true);
			
			if (prot != null && prot.getCrystalVOs() != null && prot.getCrystalVOs().isEmpty()) { 

				clv.setSpaceGroup(value.getSpaceGroup());
				clv.setCellA(value.getCellA());
				clv.setCellB(value.getCellB());
				clv.setCellC(value.getCellC());
				clv.setCellAlpha(value.getCellAlpha());
				clv.setCellBeta(value.getCellBeta());
				clv.setCellGamma(value.getCellGamma());
				clv.setProteinVO(prot);
				crystal.create(clv);

				LOG.debug("inserted a new crystal for an existing protein inside ISPyB db:" + value.getAcronym());

			}
		}
	}

	private static void retrieveSession(Proposal3VO proplv, ExpSessionInfoLightVO sessionVO) throws Exception {
		// local contact name + 1st letter of firstname is retrieved

		initServices();

		Integer proposalId = proplv.getProposalId();

		String beamLineO = "";
		if (sessionVO.getLocalContactName() != null) {
			beamLineO = sessionVO.getLocalContactName() + "  " + sessionVO.getLocalContactFirstName().substring(0, 1);
		}
		// site number is retrieved
		String siteNumber = null;
		if (sessionVO.getLocalContactSiteNumber() != null) {
			siteNumber = sessionVO.getLocalContactSiteNumber().toString();
		}
		Calendar startDateCal = Calendar.getInstance();
		startDateCal.setTime(sessionVO.getStartDate().getTime());
		Timestamp startDate = new Timestamp(startDateCal.getTimeInMillis());

		// for SOLEIL
		Integer visit_number = sessionVO.getStartShift(); // we use startShift
															// for the session
															// number because we
															// always start
															// with 0
		Integer nbShifts = sessionVO.getShifts();
		Integer startShift = sessionVO.getStartShift(); // startShift equals 1,
														// 2 or 3 and stands for
														// 8:30am,
														// 4:30pm or 00:30am
		startShift = Constants.SITE_IS_SOLEIL() ? 0 : startShift;
		Integer daysToAdd = nbShifts / 3 + 1;
		if ((startShift == 1 && nbShifts % 3 == 2) || (startShift == 2 && nbShifts % 3 != 0))
			daysToAdd++;

		// only new sessions are retrieved
		String beamlineName = sessionVO.getBeamlineName();
		Calendar endDateCal = Calendar.getInstance();
		endDateCal.setTime(sessionVO.getEndDate().getTime());
		Timestamp endDate = new Timestamp(endDateCal.getTimeInMillis());

		// the finder should be replaced by a finder on the smis session Pk,
		// instead of the startDate
		// startDate finder is a problem with a session scheduled on
		// 08/11-09/11, 1 on 09/11-10/11
		// the first one is not inserted in ispby (cf. mail David vS)
		// List<Session3VO> col =
		// session.findByStartDateAndBeamLineNameAndNbShifts(proposalId,
		// startDateBegin,
		// startDateEnd, beamlineName, nbShifts);
		Session3VO sessFromDB = session.findByExpSessionPk(sessionVO.getPk());

		List<Session3VO> sessFromDBs = null;
		if (!Constants.SITE_IS_SOLEIL()) {
			sessFromDB = session.findByExpSessionPk(sessionVO.getPk());
		} else if (Constants.SITE_IS_SOLEIL()) {
			sessFromDBs = session.findByStartDateAndBeamLineNameAndNbShifts(proposalId, startDate, endDate,
					beamlineName, nbShifts);
			LOG.debug("look for session for proposalId = " + proposalId + " | startDate = " + startDate
					+ " | endDate = " + endDate + " | beamlineName = " + beamlineName + " | nbShifts = " + nbShifts);
		}

		// if (col == null || col.isEmpty()) {
		if (sessFromDB == null || (Constants.SITE_IS_SOLEIL() && sessFromDBs != null && sessFromDBs.size() == 0)) {

			Session3VO sesv = new Session3VO();
			BeamLineSetup3VO setupv = new BeamLineSetup3VO();
			setupv = setup.create(setupv);
			sesv.setBeamLineSetupVO(setupv);

			sesv.setNbShifts(new Integer(sessionVO.getShifts()));
			sesv.setScheduled(new Byte("1"));
			LOG.debug("scheduled = :" + sesv.getScheduled());

			sesv.setProposalVO(proplv);
			sesv.setStartDate(startDate);
			sesv.setEndDate(endDate);
			sesv.setLastUpdate(endDate);
			sesv.setBeamlineName(beamlineName);
			sesv.setBeamlineOperator(beamLineO);
			sesv.setExpSessionPk(sessionVO.getPk());
			sesv.setOperatorSiteNumber(siteNumber);

			if (Constants.SITE_IS_SOLEIL()) {
				sesv.setVisit_number(visit_number);
			}
			session.create(sesv);
			LOG.debug("inserted a new session inside ISPyB db: " + sessionVO.getStartDate().getTime() + " start shift="
					+ startShift + " nb shifts=" + nbShifts + " end date=" + ((Date) endDate).toString());

			// list of BioSaxs beamLine name
			ArrayList<String> beamlineNamesList = new ArrayList<String>();
			beamlineNamesList.add("BM29");
			beamlineNamesList.add("SWING");
			// If beamline=BM29 && proposalType is MX => proposalType is MB, ie
			// both BX and MX
			if (beamlineNamesList.contains(beamlineName) && proplv.getType().equals(Constants.PROPOSAL_MX)) {
				proplv.setType(Constants.PROPOSAL_MX_BX);
				proplv = proposal.update(proplv);
			}
		} else {
			Boolean isSoleil = Constants.SITE_IS_SOLEIL() && sessFromDBs != null && sessFromDBs.size() > 0;
			Session3VO ispybSession = isSoleil ? sessFromDBs.get(0) : sessFromDB;
			// update the coming session if needed -
			if (ispybSession != null && ispybSession.getStartDate().after(new Date())) {
				boolean changeSession = false;
				String changeTxt = "updated session in ISPyB db: ";
				String ispybBeamLineOperator = ispybSession.getBeamlineOperator();
				if (beamLineO != null && ispybBeamLineOperator != null && !beamLineO.equals(ispybBeamLineOperator)) {
					changeTxt += ispybBeamLineOperator + " => " + beamLineO;
					ispybSession.setBeamlineOperator(beamLineO);
					ispybSession.setTimeStamp(StringUtils.getCurrentTimeStamp());
					ispybSession.setOperatorSiteNumber(siteNumber);
					changeSession = true;
				}
				if (startDate != null && ispybSession.getStartDate() != null
						&& !startDate.equals(ispybSession.getStartDate())) {
					changeTxt += ", startDate " + ispybSession.getStartDate() + " => " + startDate;
					ispybSession.setStartDate(startDate);
					changeSession = true;
				}
				if (endDate != null && ispybSession.getEndDate() != null && !endDate.equals(ispybSession.getEndDate())) {
					changeTxt += ", endDate " + ispybSession.getEndDate() + " => " + endDate;
					ispybSession.setEndDate(endDate);
					ispybSession.setLastUpdate(endDate);
					changeSession = true;
				}
				if (beamlineName != null && ispybSession.getBeamlineName() != null
						&& !beamlineName.equals(ispybSession.getBeamlineName())) {
					changeTxt += ", beamline " + ispybSession.getBeamlineName() + " => " + beamlineName;
					ispybSession.setBeamlineName(beamlineName);
					changeSession = true;
				}
				if (sessionVO.getShifts() != null && ispybSession.getNbShifts() != null
						&& !sessionVO.getShifts().equals(ispybSession.getNbShifts())) {
					changeTxt += ", nbShifts " + ispybSession.getNbShifts() + " => " + sessionVO.getShifts();
					ispybSession.setNbShifts(new Integer(sessionVO.getShifts()));
					changeSession = true;
				}
				if (isSoleil) {
					if (sessionVO.getStartShift() != null && ispybSession.getVisit_number() != null
							&& !sessionVO.getStartShift().equals(ispybSession.getVisit_number())) {
						changeTxt += ", visit_number " + ispybSession.getVisit_number() + " => "
								+ sessionVO.getStartShift();
						ispybSession.setVisit_number(new Integer(sessionVO.getStartShift()));
						changeSession = true;
					}
				}
				if (changeSession) {
					changeTxt += " (with sessionId = " + ispybSession.getSessionId() + ")";
					session.update(ispybSession);
					LOG.debug(changeTxt);
				}

			}
		}

	}

	@SuppressWarnings({ "unused", "rawtypes" })
	private static Proposal3VO getProposal(ProposalParticipantInfoLightVO mainProp, Laboratory3Service lab,
			Person3Service person, String proposalNumber, String proposalCode) throws Exception {
		// main proposer
		String familyName = mainProp.getScientistName();
		String givenName = mainProp.getScientistFirstName();
		String laboratoryName = mainProp.getLabName();
		String email = mainProp.getScientistEmail();
		String title = mainProp.getScientistTitle();
		String faxNumber = mainProp.getScientistFax();

		// labo
		Integer labId;
		Laboratory3VO labv = new Laboratory3VO();
		labv.setName(mainProp.getLabName());
		labv.setCity(mainProp.getLabCity());
		labv.setCountry(mainProp.getLabCountryCode());
		String address = "";

		List<String> smisAddress = mainProp.getLabAddress();
		for (Iterator iterator = smisAddress.iterator(); iterator.hasNext();) {
			String ligne = (String) iterator.next();
			if (ligne != null)
				address += ligne + "\n";
		}
		labv.setAddress(address);

		if (lab.findByNameAndCityAndCountry(laboratoryName, labv.getCity(), labv.getCountry()).isEmpty()) {
			labv = lab.create(labv);
			labId = labv.getLaboratoryId();
			LOG.debug("getting SMIS WS labo created");
		} else {
			List<Laboratory3VO> labList = lab.findByNameAndCityAndCountry(laboratoryName, null, null);
			labv = labList.get(0);
			labId = labv.getLaboratoryId();
			LOG.debug("getting SMIS WS labo already exists");
		}

		Person3VO persv = new Person3VO();
		Integer persId;

		List<Person3VO> listPerson = person.findByFamilyAndGivenName(familyName, givenName);
		if (listPerson.isEmpty()) {
			persv.setFamilyName(familyName);
			persv.setGivenName(givenName);
			persv.setEmailAddress(email);
			persv.setTitle(title);
			persv.setLaboratoryVO(labv);
			persv.setFaxNumber(faxNumber);

			/** Adding the logging **/
			LOG.debug("Adding login: " + persv.getLogin());

			persv.setLogin(mainProp.getBllogin());

			persv = person.create(persv);
			persId = persv.getPersonId();

			LOG.debug("getting SMIS WS person created");
		} else {
			// person is existing but may has changed labo : we set the new
			// labId
			persv = listPerson.get(0);
			persId = persv.getPersonId();
			persv.setLaboratoryVO(labv);
			person.update(persv);
			LOG.debug("getting SMIS WS person already exists");
		}

		Proposal3VO propv = new Proposal3VO();
		propv.setCode(proposalCode);
		propv.setNumber(proposalNumber);
		propv.setTitle(mainProp.getProposalTitle());
		propv.setPersonVO(persv);

		// Proposal Type: MX, BX, MB (both: MX with sessions on BM29)
		switch (Constants.getSite()) {
		case EMBL:
			if ((mainProp.getCategoryCode() != null) && ((mainProp.getCategoryCode().toUpperCase().equals("SAXS")))) {
				propv.setType(Constants.PROPOSAL_BIOSAXS);
			} else {
				propv.setType(Constants.PROPOSAL_MX);
			}
			break;

		default:
			if (mainProp.getProposalType() != null && mainProp.getProposalGroup() != null) {
				if (mainProp.getProposalType().intValue() == Constants.PROPOSAL_BIOSAXS_TYPE
						&& mainProp.getProposalGroup().intValue() == Constants.PROPOSAL_BIOSAXS_EXPGROUP) {
					LOG.debug("proposal is BioSaxs");
					propv.setType(Constants.PROPOSAL_BIOSAXS);
				} else if (mainProp.getProposalGroup().intValue() == Constants.PROPOSAL_INDUSTRIAL_EXPGROUP) {
					// industrial can be MX or BX
					LOG.debug("proposal is MB because it is indutrial ");
					propv.setType(Constants.PROPOSAL_MX_BX);
				} else {
					// Default is MX
					LOG.debug("proposal is MX (by default)");
					propv.setType(Constants.PROPOSAL_MX);
				}
			} else {
				// Default is MX BX
				LOG.debug("proposal can be MX or BX (because of null values)");
				propv.setType(Constants.PROPOSAL_MX_BX);
			}
			break;
		}
		return propv;

	}

	/**
	 * Generates the lab-contact card name thanks to the person and laboratory
	 * names
	 * 
	 * @param person
	 * @param laboratory
	 * @return
	 * @throws Exception
	 */
	private static String generateCardName(Person3VO person, Laboratory3VO laboratory, Integer proposalId)
			throws Exception {
		int maxLength = 15;
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		LabContact3Service labCService = (LabContact3Service) ejb3ServiceLocator
				.getLocalService(LabContact3Service.class);

		String personName = person.getFamilyName();
		personName = personName.substring(0, Math.min(personName.length(), maxLength));

		String laboName = laboratory.getName();
		laboName = laboName.substring(0, Math.min(laboName.length(), maxLength));

		String cardName = personName + "-" + laboName;// max length = 15x2 + 1 =
														// 31
		// check the card name does not exist yet
		String cardNameWithNum = cardName;
		try {
			List<LabContact3VO> labContact = labCService.findFiltered(proposalId, cardNameWithNum);
			int i = 1;
			while (labContact.size() != 0) {
				cardNameWithNum = cardName + "_" + i; // max length = 31 + 1 +
														// l(i) = 32+
				labContact = labCService.findByCardName(cardNameWithNum);
				LOG.info(labContact);
				i++;
			}
		} catch (FinderException e) {
			LOG.error(e.getMessage());
		} catch (NamingException e) {
			LOG.error(e.getMessage());
		}
		// retrieves a card name which does not exist yet
		cardName = cardNameWithNum;

		LOG.debug("Generated card name is : " + cardName);
		return cardName;
	}

}
