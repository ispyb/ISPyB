/*******************************************************************************
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
 ******************************************************************************/
package ispyb.server.userportal;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import generated.ws.smis.ExpSessionInfoLightVO;
import generated.ws.smis.ProposalParticipantInfoLightVO;
import generated.ws.smis.SampleSheetInfoLightVO;

public class UserPortalUtils {
	
	private static String jsonSamples ="[{'acronym':'tryp','categoryCode':'MX','categoryCounter':415,'description':'Trypsin','labAddress':[null,'71 avenue des Martyrs','CS 40220',null,'38043'],'labCity':'GRENOBLE','labCountryCode':'ESRF','labName':'ESRF','mainProposerEmail':'monaco@esrf.fr','mainProposerFirstName':'Stéphanie','mainProposerName':'MONACO','mainProposerPk':225239,'mainProposerTitle':'Dr','opmodePk':1,'pk':148,'proposalGroup':103,'proposalPk':31529,'proposalType':3,'spaceGroup':'','userEmail':'mccarthy@esrf.fr','userName':'Joanne MCCARTHY','userPhone':'24-21'}]";
	private static String jsonSessions = "[{'pk':79910,'experimentPk':115363,'startDate':{'year':2017,'month':4,'dayOfMonth':12,'hourOfDay':9,'minute':30,'second':0},'endDate':{'year':2017,'month':4,'dayOfMonth':13,'hourOfDay':8,'minute':0,'second':0},'shifts':3,'lostShifts':0,'reimbursedDays':4,'startShift':1,'beamlinePk':341,'beamlineName':'ID29','proposalPk':31529,'proposalSubmissionPk':1,'categCode':'MX','categCounter':415,'proposalTitle':'TEST','proposalOpmodePk':1,'proposalOpmodeCode':'Green','proposalType':3,'proposalTypeCode':'PROPSBM_MX_NON_BAG','proposalGroup':103,'proposalGroupCode':'Crystallography','runCode':'Run 2/17','runStartDate':{'year':2017,'month':2,'dayOfMonth':31,'hourOfDay':0,'minute':0,'second':0},'runEndDate':{'year':2017,'month':5,'dayOfMonth':8,'hourOfDay':0,'minute':0,'second':0},'isBagProposal':false,'isLongtermProposal':false,'hasAform':false,'sampleCount':0,'firstLocalContact':{'name':'MONACO','realName':'MONACO','firstName':'Stéphanie','email':'monaco@esrf.fr','phone':'+33 (0)4 76 88 20 84','title':'Dr','scientistPk':225239,'siteId':17074},'localContacts':[{'name':'MONACO','realName':'MONACO','firstName':'Stéphanie','email':'monaco@esrf.fr','phone':'+33 (0)4 76 88 20 84','title':'Dr','scientistPk':225239,'siteId':17074}],'mainProposer':{'name':'MONACO','realName':'MONACO','firstName':'Stéphanie','email':'monaco@esrf.fr','phone':'+33 (0)4 76 88 20 84','title':'Dr','scientistPk':225239,'siteId':17074},'cancelled':false,'finished':false,'jointExperiment':false,'localContactEmails':'monaco@esrf.fr','name':'MX/415 ID29 12-05-2017/13-05-2017','physicalBeamlineName':'ID29'}]";
	private static String jsonProposers ="[{'categoryCode':'MX','categoryCounter':415,'labAddress':[null,'71 avenue des Martyrs','CS 40220',null,'38043'],'labAddress1':'71 avenue des Martyrs','labAddress2':'CS 40220','labCity':'GRENOBLE','labCountryCode':'ESRF','labName':'ESRF','labPostalCode':'38043','laboratoryPk':200450,'proposalPk':31529,'proposalTitle':'TEST','scientistEmail':'monaco@esrf.fr','scientistFirstName':'Stéphanie','scientistName':'MONACO','scientistPk':225239,'siteId':17074}]";
	private static String jsonLabContacts = "[{'categoryCode':'MX','categoryCounter':415,'labAddress':[null,'71 avenue des Martyrs','CS 40220',null,'38043'],'labAddress1':'71 avenue des Martyrs','labAddress2':'CS 40220','labCity':'GRENOBLE','labCountryCode':'ESRF','labName':'ESRF','labPostalCode':'38043','laboratoryPk':200450,'mainProposer':false,'pk':330938,'proposalPk':31529,'proposalTitle':'TEST','proposalType':3,'proposer':false,'scientistEmail':'solange.delageniere@esrf.fr','scientistFirstName':'Solange','scientistName':'DELAGENIERE','scientistPk':284191,'scientistTitle':'Dr','siteId':91481,'user':true},{'categoryCode':'MX','categoryCounter':415,'labAddress':[null,'71 avenue des Martyrs','CS 40220',null,'38043'],'labAddress1':'71 avenue des Martyrs','labAddress2':'CS 40220','labCity':'GRENOBLE','labCountryCode':'ESRF','labName':'ESRF','labPostalCode':'38043','laboratoryPk':200450,'mainProposer':false,'pk':428429,'proposalGroup':103,'proposalPk':31529,'proposalTitle':'TEST','proposalType':3,'proposer':false,'scientistEmail':'sarah.guillot@esrf.fr','scientistFirstName':'Sarah','scientistName':'GUILLOT','scientistPk':282441,'scientistTitle':'Mme','siteId':190200}]";

	/**
	 * Returns the list of main proposers from a json file
	 * 
	 * @return
	 */
	public static List<ProposalParticipantInfoLightVO> getMainProposers(List<Map<String, Object>> proposersJson) {

		List<ProposalParticipantInfoLightVO> mainProposers = new ArrayList();
		
		for (Map<String, Object> map : proposersJson) {
			ProposalParticipantInfoLightVO proposer = new ProposalParticipantInfoLightVO();

			proposer.setBllogin(getParam(map, "bllogin"));
			proposer.setCategoryCode(getParam(map, "categoryCode"));
			proposer.setCategoryCounter(getParamInt(map, "categoryCounter"));
			proposer.setLabAddress1(getParam(map, "labAddress1"));
			proposer.setLabAddress2(getParam(map, "labAddress2"));
			proposer.setLabAddress3(getParam(map, "labAddress3"));
			proposer.setLabCity(getParam(map, "labCity"));
			proposer.setLabDeparment(getParam(map, "labDeparment"));
			proposer.setLabName(getParam(map, "labName"));
			proposer.setLabCountryCode(getParam(map, "labCountryCode"));
			proposer.setLabPostalCode(getParam(map, "labPostalCode"));
			proposer.setScientistEmail(getParam(map, "scientistEmail"));
			proposer.setScientistName(getParam(map, "scientistName"));
			proposer.setScientistFirstName(getParam(map, "scientistFirstName"));
			proposer.setSiteId(getParamInt(map, "siteId"));
		
			mainProposers.add(proposer);
		}
		return mainProposers;

	}
	
	/**
	 * Returns the list of sessions from a json file
	 * 
	 * @return
	 */
	public static List<ExpSessionInfoLightVO> getSessions(List<Map<String, Object>> jsonFile) {

		List<ExpSessionInfoLightVO> smisSessions = new ArrayList();
		
		for (Map<String, Object> map : jsonFile) {
			ExpSessionInfoLightVO session = new ExpSessionInfoLightVO();
			
			session.setBeamlineName(getParam(map, "beamlineName"));
			session.setExperimentPk(getParamInt(map, "experimentPk").longValue());
			session.setEndDate(getParamDate(map,"endDate"));
			session.setComment(getParam(map,"comment"));
			//session.setFirstLocalContact(value);

		//TODO
			smisSessions.add(session);
		}
		return smisSessions;

	}
	
	/**
	 * Returns the list of sessions from a json file
	 * 
	 * @return
	 */
	public static List<SampleSheetInfoLightVO> getSamples(List<Map<String, Object>> jsonFile) {

		List<SampleSheetInfoLightVO> smisSamples = new ArrayList();
		
		for (Map<String, Object> map : jsonFile) {
			SampleSheetInfoLightVO sample = new SampleSheetInfoLightVO();
		//TODO
			
			smisSamples.add(sample);
		}
		return smisSamples;

	}
	
	/**
	 * Returns the list of lab contacts from a json file
	 * 
	 * @return
	 */
	public static List<ProposalParticipantInfoLightVO> getLabContacts(List<Map<String, Object>> proposersJson) {

		List<ProposalParticipantInfoLightVO> labContacts = new ArrayList();
		
		for (Map<String, Object> map : proposersJson) {
			ProposalParticipantInfoLightVO labContact = new ProposalParticipantInfoLightVO();

			labContact.setBllogin(getParam(map, "bllogin"));
			labContact.setCategoryCode(getParam(map, "categoryCode"));
			labContact.setCategoryCounter(getParamInt(map, "categoryCounter"));
			labContact.setLabAddress1(getParam(map, "labAddress1"));
			labContact.setLabAddress2(getParam(map, "labAddress2"));
			labContact.setLabAddress3(getParam(map, "labAddress3"));
			labContact.setLabCity(getParam(map, "labCity"));
			labContact.setLabDeparment(getParam(map, "labDeparment"));
			labContact.setLabName(getParam(map, "labName"));
			labContact.setLabCountryCode(getParam(map, "labCountryCode"));
			labContact.setLabPostalCode(getParam(map, "labPostalCode"));
			labContact.setScientistEmail(getParam(map, "scientistEmail"));
			labContact.setScientistName(getParam(map, "scientistName"));
			labContact.setScientistFirstName(getParam(map, "scientistFirstName"));
			labContact.setSiteId(getParamInt(map, "siteId"));
		
			labContacts.add(labContact);
		}
		return labContacts;

	}
	
	public static List<ProposalParticipantInfoLightVO> getMainProposers() {
		return getMainProposers(jsonToListmap(jsonProposers));
	}

	public static List<ExpSessionInfoLightVO> getSessions() {
		return getSessions(jsonToListmap(jsonSessions));
	}

	public static List<SampleSheetInfoLightVO> getSamples() {
		return getSamples(jsonToListmap(jsonSamples));
	}
	
	public static List<ProposalParticipantInfoLightVO> getLabContacts(){
		return getLabContacts(jsonToListmap(jsonLabContacts));
	}

	
	public static List<Map<String, Object>> jsonToListmap(String json){   
	    Gson gson = new Gson();
	    Type mapType = new TypeToken<ArrayList<String>>() {}.getType();
	    return gson.fromJson(json , mapType);
	}

	private static String getParam(Map<String, Object> map, String key){
		String param = "";
		
		if (map.get(key) != null)
			param = map.get(key).toString();		
		return param;		
	}
	
	private static Integer getParamInt(Map<String, Object> map, String key){
		Integer param = null;
		
		if (map.get(key) != null) {
				param = new Integer(map.get(key).toString());
		}
		
		return param;		
	}

	private static Calendar getParamDate(Map<String, Object> map, String key){
		Date param = null;
		Calendar cal = Calendar.getInstance();
		
		if (map.get(key) != null) {
				param = new Date(map.get(key).toString());
		}
		
		cal.setTime(param);
		return cal;
	}

}
