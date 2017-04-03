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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import generated.ws.smis.ExpSessionInfoLightVO;
import generated.ws.smis.ProposalParticipantInfoLightVO;
import generated.ws.smis.SampleSheetInfoLightVO;
import ispyb.common.util.Constants;
import ispyb.common.util.PathUtils;
import ispyb.server.smis.UpdateFromSMIS;

public class UserPortalUtils {
	
	private static final Logger LOG = Logger.getLogger(UserPortalUtils.class);

	
	private static String jsonSamples = Constants.getProperty("userportal.json.samples");
	private static String jsonSessions = Constants.getProperty("userportal.json.sessions");
	private static String jsonProposers = Constants.getProperty("userportal.json.proposers");
	private static String jsonLabContacts = Constants.getProperty("userportal.json.labcontacts");
	private static String jsonScientists = Constants.getProperty("userportal.json.scientists");
	private static String filepathPrefix = Constants.getProperty("ispyb.upload.folder.json");
	
	// the filepath is defined by a custom property in pom.xml + name of the proposal in small letters + word to define the type of extraction
		
	public static List<ProposalParticipantInfoLightVO> getMainProposers(String proposalName) throws IOException {
		String filepath = filepathPrefix + proposalName + "_json_proposers";
		jsonProposers = loadJsonFile(filepath);
		return jsonToScientistsList(jsonProposers);
	}

	public static List<ExpSessionInfoLightVO> getSessions(String proposalName) throws IOException {
		String filepath = filepathPrefix + proposalName + "_json_sessions";
		jsonSessions = loadJsonFile(filepath);
		return jsonToSessionsList(jsonSessions);
	}

	public static List<SampleSheetInfoLightVO> getSamples(String proposalName) throws IOException {
		String filepath = filepathPrefix + proposalName + "_json_samples"; 
		jsonSamples = loadJsonFile(filepath);
		return jsonToSamplesList(jsonSamples);
	}
	
	public static List<ProposalParticipantInfoLightVO> getLabContacts(String proposalName) throws IOException {
		String filepath = filepathPrefix + proposalName + "_json_labcontacts";
		jsonLabContacts  = loadJsonFile(filepath);
		return jsonToScientistsList(jsonLabContacts);
	}

	public static List<ProposalParticipantInfoLightVO> getScientists(String proposalName) throws IOException {
		String filepath = filepathPrefix + proposalName + "_json_scientists";
		jsonScientists= loadJsonFile(filepath);
		return jsonToScientistsList(jsonScientists);
	}
		
	public static List<SampleSheetInfoLightVO> jsonToSamplesList(String json){   
	    Gson gson = new Gson();
	    Type listType = new TypeToken<List<SampleSheetInfoLightVO>>() {}.getType();
	    List<SampleSheetInfoLightVO> sampleList = gson.fromJson(json , listType);
	    return sampleList;
	}
	
	public static List<ExpSessionInfoLightVO> jsonToSessionsList(String json){   
	    Gson gson = new Gson();
	    Type listType = new TypeToken<List<ExpSessionInfoLightVO>>() {}.getType();
	    List<ExpSessionInfoLightVO> sampleList = gson.fromJson(json , listType);
	    return sampleList;
	}
	
	public static List<ProposalParticipantInfoLightVO> jsonToScientistsList(String json){   
	    Gson gson = new Gson();
	    Type listType = new TypeToken<List<ProposalParticipantInfoLightVO>>() {}.getType();
	    List<ProposalParticipantInfoLightVO> scientistList = gson.fromJson(json , listType);
	    return scientistList;
	}
	
	private static String loadJsonFile(String filepath) {
		String json = "";
		filepath = PathUtils.FitPathToOS(filepath);
		LOG.debug("path to json files : " + filepath);
		File file = new File(filepath);
		try {
			json = FileUtils.readFileToString(file);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	
}
