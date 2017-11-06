package ispyb.server.webservice.smis.util;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import generated.ws.smis.Exception_Exception;
import generated.ws.smis.ExpSessionInfoLightVO;
import generated.ws.smis.FinderException_Exception;
import generated.ws.smis.InnerScientistVO;
import generated.ws.smis.ProposalParticipantInfoLightVO;
import generated.ws.smis.SMISWebService;
import generated.ws.smis.SampleSheetInfoLightVO;
import ispyb.common.util.Constants;
import ispyb.server.smis.UpdateFromSMIS;

public class MAXIVWebService implements SMISWebService {

	private static final Logger LOG = Logger.getLogger(UpdateFromSMIS.class);
	
	private String serverUrl = "";
	
	public MAXIVWebService() {
		this.serverUrl = Constants.getProperty("userportal.url");
	}
	
	public List<Long> findNewMXProposalPKs (String startDateStr, String endDateStr) {
		List<Long> pks = new ArrayList<Long>();
        
		StringBuilder url = new StringBuilder("https://").append(this.serverUrl).append("/api/proposals?access_token=").append(this.getToken())
				.append("&filter=").append(this.getFilter(startDateStr, endDateStr));
		
		JSONArray jsonProposals = readJsonArrayFromUrl(url.toString());
		
		int len = jsonProposals.length();
		for (int i=0; i<len; i++){ 
			JSONObject jsonProposal = (JSONObject)jsonProposals.get(i);
			
			Long proposalId = null;
			try {
				proposalId = (Long.valueOf(((Integer)jsonProposal.get("propid"))).longValue());
				LOG.info("Proposal found: " + proposalId);
			} catch(Exception ex){
				//TODO: Handle exception
				ex.printStackTrace();
			}
			pks.add(proposalId);
		}
		
		System.out.println("Number of proposals found : " + pks.size());
		LOG.info("Number of proposals found : " + pks.size());
	
		return pks;
	}
	
	public List<ProposalParticipantInfoLightVO> findMainProposersForProposal(Long propId) {
		List<ProposalParticipantInfoLightVO> proposers = new ArrayList<ProposalParticipantInfoLightVO>();
		ProposalParticipantInfoLightVO proposer = new ProposalParticipantInfoLightVO();
		
		try{
			JSONObject jsonProposal = getProposalForId(propId);
			
			Integer proposerId = (Integer)jsonProposal.get("proposer");
			JSONObject jsonProposer = getUserForId(proposerId);
			
			proposer.setCategoryCode("MX");//TODO Need to check for internal proposals
			proposer.setCategoryCounter(propId.intValue());
			proposer.setBllogin((String)jsonProposer.get("username"));
			
			Integer labId = (Integer)jsonProposer.get("institute");
			JSONObject jsonLab = getLabForId(labId);
			proposer.setLabAddress1((String)jsonLab.get("address"));
			proposer.setLabCity((String)jsonLab.get("city"));
			proposer.setLabDeparment((String)jsonLab.get("department"));
			String labname = (String)jsonLab.get("name");
			if(labname.length() >= 45){
				labname = labname.substring(0,40).concat("...");
				System.out.println("Truncated lab name :" + labname);
			}
			proposer.setLabName(labname);
			
			proposer.setProposalPk(propId);
			String title = (String)jsonProposal.get("title");
			if(title.length() >= 200){
				title = title.substring(0,195).concat("...");
				System.out.println("Truncated Tilte :" + title);
			}
			proposer.setProposalTitle(title);
			proposer.setScientistFirstName((String)jsonProposer.get("firstname"));
			proposer.setScientistName((String)jsonProposer.get("firstname") + " " + (String)jsonProposer.get("lastname"));
		} catch(Exception ex){
			//TODO: Handle exception
			ex.printStackTrace();
		}
			
		proposers.add(proposer);
		LOG.info("Proposers found : " + proposers.size());
		
		return proposers;
	}
	
	public List<ProposalParticipantInfoLightVO> findParticipantsForProposal(Long propId) throws Exception_Exception {
		List<ProposalParticipantInfoLightVO> participants = new ArrayList<ProposalParticipantInfoLightVO>();
		
		JSONObject jsonProposal = getProposalForId(propId);
		
		//Get all co-proposer ids
		ArrayList<Integer> cowriters = new ArrayList<Integer>();
		if(jsonProposal.get("cowriter1") != JSONObject.NULL)
			cowriters.add((Integer)jsonProposal.get("cowriter1"));
		if(jsonProposal.get("cowriter2") != JSONObject.NULL)
			cowriters.add((Integer)jsonProposal.get("cowriter2"));
		if(jsonProposal.get("cowriter3") != JSONObject.NULL)
			cowriters.add((Integer)jsonProposal.get("cowriter3"));
		if(jsonProposal.get("cowriter4") != JSONObject.NULL)
			cowriters.add((Integer)jsonProposal.get("cowriter4"));
		if(jsonProposal.get("cowriter5") != JSONObject.NULL)
			cowriters.add((Integer)jsonProposal.get("cowriter5"));
		if(jsonProposal.get("cowriter6") != JSONObject.NULL)
			cowriters.add((Integer)jsonProposal.get("cowriter6"));
		if(jsonProposal.get("cowriter7") != JSONObject.NULL)
			cowriters.add((Integer)jsonProposal.get("cowriter7"));
		if(jsonProposal.get("cowriter8") != JSONObject.NULL)
			cowriters.add((Integer)jsonProposal.get("cowriter8"));
		if(jsonProposal.get("cowriter9") != JSONObject.NULL)
			cowriters.add((Integer)jsonProposal.get("cowriter9"));
		if(jsonProposal.get("cowriter10") != JSONObject.NULL)
			cowriters.add((Integer)jsonProposal.get("cowriter10"));
		
		
		ArrayList<JSONObject> jsonSessions = getSessionsForProposal(propId);

		for(JSONObject jsonSession : jsonSessions){
			ArrayList<JSONObject> jsonParticipants = getParticipantsForSession((Integer)jsonSession.get("sessionid"));
			for(JSONObject jsonParticipant : jsonParticipants){
				cowriters.add((Integer)jsonParticipant.get("userid"));
			}
		}
		
		for(Integer cowriterId : cowriters) {
			JSONObject jsonParticipant = getUserForId(cowriterId);
			
			ProposalParticipantInfoLightVO participant = new ProposalParticipantInfoLightVO();
			try{
				participant.setCategoryCode("MX");
				participant.setCategoryCounter(propId.intValue());
				participant.setBllogin((String)jsonParticipant.get("username"));
				
				Integer labId = (Integer)jsonParticipant.get("institute");
				JSONObject jsonLab = getLabForId(labId);
				participant.setLabAddress1((String)jsonLab.get("address") + "," + (String)jsonLab.get("country"));
				participant.setLabCity((String)jsonLab.get("city"));
				participant.setScientistEmail((String)jsonParticipant.get("email"));
				if(jsonLab.get("department") != JSONObject.NULL)
					participant.setLabDeparment((String)jsonLab.get("department"));
				String labname = (String)jsonLab.get("name");
				if(labname.length() >= 45){
					labname = labname.substring(0,40).concat("...");
					System.out.println("Truncated lab name :" + labname);
				}
				participant.setLabName(labname);
				participant.setProposalPk(propId);
				String title = (String)jsonProposal.get("title");
				if(title.length() >= 200){
					title = title.substring(0,195).concat("...");
					System.out.println("Truncated Tilte :" + title);
				}
				participant.setProposalTitle(title);
				participant.setScientistFirstName((String)jsonParticipant.get("firstname"));
				participant.setScientistName((String)jsonParticipant.get("lastname"));
			} catch(Exception ex){
				//TODO: Handle exception
				ex.printStackTrace();
			}
			
			participants.add(participant);
		}
		
		LOG.info("Participants found : " + participants.size());
		
		return participants;
	}
	
	public List<ExpSessionInfoLightVO> findRecentSessionsInfoLightForProposalPkAndDays(Long propId, Integer days)
			throws FinderException_Exception {
		List<ExpSessionInfoLightVO> sessions = new ArrayList<ExpSessionInfoLightVO>();
		
		JSONObject jsonProposal = getProposalForId(propId);
		ArrayList<JSONObject> jsonSessions = getSessionsForProposal(propId);

		for(JSONObject jsonSession : jsonSessions){
			ExpSessionInfoLightVO session = new ExpSessionInfoLightVO();
			
			try{
				ArrayList<JSONObject> jsonShifts = getShiftsForSession((Integer)jsonSession.get("sessionid"));
				if(jsonShifts.size() != 0){
					session.setBeamlineName((String)jsonSession.get("beamline"));
					session.setBeamlinePk(Long.valueOf(12345));//TODO Verify. We dont have a Long id
					session.setProposalGroup(0);//TODO Need to check for industrial proposals
					session.setProposalGroupCode(propId.toString());
					session.setProposalPk(propId);
					session.setProposalSubmissionPk(propId);
					session.setExperimentPk(propId);
					session.setComment("Created by DUO");
					session.setPk(new Long((int)jsonSession.get("sessionid")));
					InnerScientistVO localContact = new InnerScientistVO();
					localContact.setName("Muller");
					localContact.setFirstName("Uwe");
					session.setFirstLocalContact(localContact);
					String title = (String)jsonProposal.get("title");
					if(title.length() >= 200){
						title = title.substring(0,195).concat("...");
						System.out.println("Truncated Tilte :" + title);
					}
					session.setProposalTitle(title);
					session.setCategCode("MX");
					session.setCategCounter(propId.intValue());
					String shiftStartDate[] = ((String) jsonShifts.get(0).get("shift_start")).split("-|\\s|:");
					String shiftEndDate[] = ((String) jsonShifts.get(0).get("shift_end")).split("-|\\s|:");
					Calendar startDate = new GregorianCalendar(Integer.parseInt(shiftStartDate[0]),Integer.parseInt(shiftStartDate[1]) - 1,Integer.parseInt(shiftStartDate[2]), Integer.parseInt(shiftStartDate[3]), 0);
					Calendar endDate = new GregorianCalendar(Integer.parseInt(shiftEndDate[0]),Integer.parseInt(shiftEndDate[1]) - 1,Integer.parseInt(shiftEndDate[2]), Integer.parseInt(shiftEndDate[3]), 0);
					
					
					for(JSONObject jsonShift : jsonShifts){
						shiftStartDate = ((String) jsonShift.get("shift_start")).split("-|\\s|:");
						shiftEndDate = ((String) jsonShift.get("shift_end")).split("-|\\s|:");
						Calendar tmpStartDate = new GregorianCalendar(Integer.parseInt(shiftStartDate[0]),Integer.parseInt(shiftStartDate[1]) - 1,Integer.parseInt(shiftStartDate[2]), Integer.parseInt(shiftStartDate[3]), 0);
						Calendar tmpEndDate = new GregorianCalendar(Integer.parseInt(shiftEndDate[0]),Integer.parseInt(shiftEndDate[1]) - 1,Integer.parseInt(shiftEndDate[2]), Integer.parseInt(shiftEndDate[3]), 0);
						
						if(0 < startDate.compareTo(tmpStartDate)){
							startDate = tmpStartDate;
						}
	
						if(0 > endDate.compareTo(tmpEndDate)){
							endDate = tmpEndDate;	
						}
					}

					session.setStartDate(startDate);
					session.setEndDate(endDate);
	
					session.setStartShift(Integer.valueOf(1));//TODO Verify. What should this be?
					session.setShifts(jsonShifts.size());
					
					session.setCancelled(false);//TODO: Check what this means
					
					sessions.add(session);
				}
			} catch(Exception ex){
				//TODO: Handle exception
				ex.printStackTrace();
			}
			
		}
		
		return sessions;
	}
	
	public List<String> findProposalsFromBeamlineLogin(String arg0) throws Exception_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ExpSessionInfoLightVO> findRecentSessionsInfoLightForProposalPk(Long arg0)
			throws FinderException_Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<SampleSheetInfoLightVO> findSamplesheetInfoLightForProposalPk(Long arg0) throws Exception_Exception {
		List<SampleSheetInfoLightVO> sampleSheets = new ArrayList<SampleSheetInfoLightVO>();
		SampleSheetInfoLightVO sampleSheet = new SampleSheetInfoLightVO();
		// TODO Auto-generated method stub
		return sampleSheets;
	}

	public List<ProposalParticipantInfoLightVO> findScientistsByNameAndFirstName(String arg0, String arg1, int arg2)
			throws Exception_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ProposalParticipantInfoLightVO> findScientistsForProposalByNameAndFirstName(Long arg0, String arg1,
			String arg2) throws Exception_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public ExpSessionInfoLightVO findSessionByProposalBeamlineAndDate(String arg0, Integer arg1, String arg2,
			Calendar arg3) throws FinderException_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ExpSessionInfoLightVO> findSessionsByBeamlineAndDates(String arg0, Calendar arg1, Calendar arg2)
			throws FinderException_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ExpSessionInfoLightVO> findSessionsInfoLightForProposalPk(Long arg0) throws FinderException_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ProposalParticipantInfoLightVO> findUsersByExpSession(Long arg0) throws Exception_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public long getProposalPK(String arg0, Long arg1) throws FinderException_Exception {
		// TODO Auto-generated method stub
		return arg1;
	}
	
	private JSONObject getProposalForId(Long propId){
		JSONObject proposal = null;
		
		StringBuilder url = new StringBuilder("https://").append(this.serverUrl).append("/api/Proposals/")
				.append(propId).append("?access_token=").append(this.getToken());
		
		proposal = readJsonObjectFromUrl(url.toString());
		
		return proposal;
	}
	
	private ArrayList<JSONObject> getBeamlinesForProposal(Long propId){
		ArrayList<JSONObject> beamlines = new ArrayList<JSONObject>();
		
		StringBuilder url = new StringBuilder("https://").append(this.serverUrl).append("/api/Proposals/")
				.append(propId).append("/beamlines").append("?access_token=").append(this.getToken());
		
		JSONArray jsonBeamlines = readJsonArrayFromUrl(url.toString());
		
		int len = jsonBeamlines.length();
		for (int i=0; i<len; i++){ 
			JSONObject jsonBeamline = (JSONObject)jsonBeamlines.get(i);
			beamlines.add(jsonBeamline);
		}
		
		return beamlines;
	}
	
	private ArrayList<JSONObject> getSessionsForProposal(Long propId){
		ArrayList<JSONObject> sessions = new ArrayList<JSONObject>();
		
		StringBuilder url = new StringBuilder("https://").append(this.serverUrl).append("/api/Proposals/")
				.append(propId).append("/sessions").append("?access_token=").append(this.getToken());
		
		JSONArray jsonSessions = readJsonArrayFromUrl(url.toString());
		
		int len = jsonSessions.length();
		for (int i=0; i<len; i++){ 
			JSONObject jsonSession = (JSONObject)jsonSessions.get(i);
			sessions.add(jsonSession);
		}
		
		return sessions;
	}
	
	private ArrayList<JSONObject> getShiftsForSession(Integer sessionId){
		ArrayList<JSONObject> shifts = new ArrayList<JSONObject>();
		
		StringBuilder url = new StringBuilder("https://").append(this.serverUrl).append("/api/Sessions/")
				.append(sessionId).append("/shifts").append("?access_token=").append(this.getToken());
		
		JSONArray jsonSessions = readJsonArrayFromUrl(url.toString());
		
		int len = jsonSessions.length();
		for (int i=0; i<len; i++){ 
			JSONObject jsonSession = (JSONObject)jsonSessions.get(i);
			shifts.add(jsonSession);
		}
		
		return shifts;
	}
	
	private ArrayList<JSONObject> getParticipantsForSession(Integer sessionId){
		ArrayList<JSONObject> participants = new ArrayList<JSONObject>();
		
		StringBuilder url = new StringBuilder("https://").append(this.serverUrl).append("/api/Sessions/")
				.append(sessionId).append("/participants").append("?access_token=").append(this.getToken());
		
		JSONArray jsonSessions = readJsonArrayFromUrl(url.toString());
		
		int len = jsonSessions.length();
		for (int i=0; i<len; i++){ 
			JSONObject jsonSession = (JSONObject)jsonSessions.get(i);
			participants.add(jsonSession);
		}
		
		return participants;
	}
	
	private JSONObject getUserForId(Integer userId){
		JSONObject user = null;
		
		StringBuilder url = new StringBuilder("https://").append(this.serverUrl).append("/api/Clients/")
				.append(userId).append("?access_token=").append(this.getToken());
		
		user = readJsonObjectFromUrl(url.toString());
		
		return user;
	}
	
	private JSONObject getLabForId(Integer labId){
		JSONObject lab = null;
		
		StringBuilder url = new StringBuilder("https://").append(this.serverUrl).append("/api/Institutes/")
				.append(labId).append("?access_token=").append(this.getToken());

		lab = readJsonObjectFromUrl(url.toString());
		
		return lab;
	}
	
	private JSONObject readJsonObjectFromUrl(String url){
		JSONObject jsonObj = null;
				
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url.toString());
		
		try{
			CloseableHttpResponse response = httpclient.execute(httpGet);
			//System.out.println(response.getStatusLine());
			String jsonStr = IOUtils.toString(new InputStreamReader((response.getEntity().getContent())));
			LOG.debug("readJsonObjectFromUrl: JSON string: " + jsonStr);
			jsonObj = new JSONObject(jsonStr);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw new RuntimeException("Error reading json. " + ex.getMessage());
		}
		
		return jsonObj;
	}
	
	private JSONArray readJsonArrayFromUrl(String url){
		JSONArray jsonArr = null;
				
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url.toString());
		
		try{
			CloseableHttpResponse response = httpclient.execute(httpGet);
			//System.out.println(response.getStatusLine());
			String jsonStr = IOUtils.toString(new InputStreamReader((response.getEntity().getContent())));
			LOG.debug("readJsonArrayFromUrl: JSON string: " + jsonStr);
			jsonArr = new JSONArray(jsonStr);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw new RuntimeException("Error reading json. " + ex.getMessage());
		}
		
		return jsonArr;
	}
	
	public String getToken()
	{
		String token = "";
		
		StringBuilder url = new StringBuilder("https://").append(this.serverUrl).append("/api/Users/login");
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url.toString());
		
		try{
			httpPost.addHeader("Accept", "application/json");
			httpPost.addHeader("Content-Type", "application/json");
			
			JSONObject body = new JSONObject();
            body.put("email", "ispyb@maxiv.lu.se");
            body.put("password", "Test1234!");
            
            String parameterStr = body.toString();
            StringEntity entity = new StringEntity(parameterStr);
            httpPost.setEntity(entity);
			
			CloseableHttpResponse response = httpclient.execute(httpPost);
			//System.out.println(response.getStatusLine());
			String jsonStr = IOUtils.toString(new InputStreamReader((response.getEntity().getContent())));
			JSONObject jsonObj = new JSONObject(jsonStr);
			token = (String)jsonObj.get("id");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return token;
	}
	
	private String getFilter(String startDateStr, String endDateStr){
		StringBuilder filter = new StringBuilder();
		String filterStr = "";
		
		String startYear = startDateStr.substring(0, 4);
		String endYear = endDateStr.substring(0, 4);
		StringBuilder regexp = new StringBuilder("/^").append(startYear).append("|^").append(endYear).append("/");
		JSONObject jsonExp = new JSONObject();
		jsonExp.put("regexp", regexp.toString());
		JSONObject jsonField1 = new JSONObject();
		jsonField1.put("propid", jsonExp);
		JSONObject jsonField2 = new JSONObject();
		jsonField2.put("status", "A");
		JSONObject jsonField3 = new JSONObject();
		jsonField3.put("beamline_assigned", "BioMAX");
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(jsonField1);
		jsonArray.put(jsonField2);
		jsonArray.put(jsonField3);
		JSONObject jsonFieldAnd = new JSONObject();
		jsonFieldAnd.put("and", jsonArray);
		JSONObject jsonFilter = new JSONObject();
		jsonFilter.put("where", jsonFieldAnd);
		
		filterStr = jsonFilter.toString();
		
		Pattern p = Pattern.compile("[a-zA-Z0-9]");
	    
		for (int i = 0; i < filterStr.length(); i++) {
			Matcher m = p.matcher(String.valueOf(filterStr.charAt(i)));
	        if (m.find()){
	        	filter.append(filterStr.charAt(i));
	        } else {
	        	filter.append("%").append(String.format("%h", (filterStr.charAt(i))));
	        }
	    }
		
		return filter.toString();
	}
}
