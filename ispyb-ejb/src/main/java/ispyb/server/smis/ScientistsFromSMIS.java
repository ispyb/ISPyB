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

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import generated.ws.smis.ProposalParticipantInfoLightVO;
import generated.ws.smis.SMISWebService;
import ispyb.common.util.Constants;
import ispyb.common.util.Constants.SITE;
import ispyb.server.common.vos.proposals.Laboratory3VO;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.userportal.UserPortalUtils;
import ispyb.server.webservice.smis.util.SMISWebServiceGenerator;


public class ScientistsFromSMIS {

	private static final Logger LOG = Logger.getLogger(ScientistsFromSMIS.class);

	public static String sayHello() {
		return "Hello";
	}

	public static ProposalParticipantInfoLightVO[] findScientistsByNameAndFirstName(String name, String firstName, int maxResults)
			throws Exception {
		LOG.debug("WS created looking for scientist with name/firstname =  " + name + "/" + firstName);

		List<ProposalParticipantInfoLightVO> scientists_ = null;

		if (Constants.SITE_USERPORTAL_LINK_IS_SMIS()) {
			// Get the service
			SMISWebService ws = SMISWebServiceGenerator.getWs();		
			scientists_ = ws.findScientistsByNameAndFirstName(name, firstName, maxResults);
		} else {
			// find a way to retrieve the list of scientists in the form of json files
			//scientists_ = UserPortalUtils.jsonToScientistsList(json);
			String proposal = "all";
			scientists_ = UserPortalUtils.getScientists(proposal);
		}
		
		ProposalParticipantInfoLightVO[] scientists = new ProposalParticipantInfoLightVO[scientists_.size()];
		scientists = scientists_.toArray(scientists);

		LOG.debug("Number of scientists found = " + scientists.length);

		return scientists;
	}

	public static ProposalParticipantInfoLightVO[] findScientistsForProposalByNameAndFirstName(String proposalCode,
			Long proposalNumber, String name, String firstName) throws Exception {
		LOG.debug("WS created looking for scientist with name/firstname =  " + name + "/" + firstName);

		Long proposalPk = null;
		List<ProposalParticipantInfoLightVO> scientists_ = null;

		if (Constants.SITE_USERPORTAL_LINK_IS_SMIS()) {
			// Get the service
			SMISWebService ws = SMISWebServiceGenerator.getWs();
		
			proposalPk = ws.getProposalPK(proposalCode, proposalNumber);
			LOG.debug("for proposal : " + proposalCode + " " + proposalNumber + "   proposalPk = " + proposalPk);
			scientists_ = ws.findScientistsForProposalByNameAndFirstName(proposalPk, name,
					firstName);
		 
			if (name == null && firstName == null && !Constants.SITE_IS_SOLEIL()) {
				scientists_ = ws.findParticipantsForProposal(proposalPk);
			}

		} else {
			// find a way to retrieve the list of scientists in the form of json files
			//scientists_ = UserPortalUtils.jsonToScientistsList(json);
			String proposal="all";
			scientists_ = UserPortalUtils.getScientists(proposal);	
		}
		
		ProposalParticipantInfoLightVO[] scientists = new ProposalParticipantInfoLightVO[scientists_.size()];
		scientists = scientists_.toArray(scientists);

		if (scientists != null)
			LOG.debug("Number of scientists found = " + scientists.length);
		else
			LOG.debug("No scientists found");

		return scientists;
	}

	public static ProposalParticipantInfoLightVO findUniqueScientist(String name, String firstName) throws Exception {
		LOG.debug("WS created looking for scientist with name/firstname =  " + name + "/" + firstName);

		List<ProposalParticipantInfoLightVO> scientists_ = null;

		if (Constants.SITE_USERPORTAL_LINK_IS_SMIS()) {
			// Get the service
			SMISWebService ws = SMISWebServiceGenerator.getWs();
			scientists_ = ws.findScientistsByNameAndFirstName(name, firstName, 2);
			
		} else {
			// find a way to retrieve the list of scientists in the form of json files
			//scientists_ = UserPortalUtils.jsonToScientistsList(json);
			String proposal="all";
			scientists_ = UserPortalUtils.getScientists(proposal);		
		}
		ProposalParticipantInfoLightVO[] scientists = new ProposalParticipantInfoLightVO[scientists_.size()];
		scientists = scientists_.toArray(scientists);

		LOG.debug("Number of scientists found = " + scientists.length);

		// check unique result
		if (scientists.length == 0) {
			throw new Exception("This scientist (" + name + ", " + firstName + ") does not exist in the SMIS database");
		} else if (scientists.length > 1) {
			throw new Exception("More than one scientists exist in the SMIS database with the follwing name/firstname : (" + name
					+ ", " + firstName + ")");
		}

		// return the unique scientist found
		return scientists[0];
	}

	/**
	 * Fill personLightValue object with ProposalParticipantInfoLightVO object infos
	 * 
	 * @param scientist
	 * @return
	 */
	public static Person3VO extractPersonInfo(ProposalParticipantInfoLightVO scientist) {
		Person3VO person = new Person3VO();

		person.setFamilyName(scientist.getScientistName());
		person.setGivenName(scientist.getScientistFirstName());

		person.setFaxNumber(scientist.getScientistFax());
		person.setEmailAddress(scientist.getScientistEmail());

		person.setPhoneNumber("");

		return person;
	}

	/**
	 * Fill laboratoryLightValue object with ProposalParticipantInfoLightVO object infos
	 * 
	 * @param scientist
	 * @return
	 */
	public static Laboratory3VO extractLaboratoryInfo(ProposalParticipantInfoLightVO scientist) {
		Laboratory3VO laboratory = new Laboratory3VO();

		// name
		laboratory.setName(scientist.getLabName());
		// city
		laboratory.setCity(scientist.getLabCity());
		// country
		laboratory.setCountry(scientist.getLabCountryCode());
		
		laboratory.setLaboratoryExtPk(scientist.getLaboratoryPk());
		// address
		String address = "";
		List<String> labs = scientist.getLabAddress();
		for (Iterator iterator = labs.iterator(); iterator.hasNext();) {
			String ligne = (String) iterator.next();
			if (ligne != null)
				address += ligne + "\n";
		}
		switch (Constants.getSite()) {
			case MAXIV:
				laboratory.setAddress(scientist.getLabAddress1());
				break;
			default:
				laboratory.setAddress(address);
		}


		return laboratory;
	}

}
