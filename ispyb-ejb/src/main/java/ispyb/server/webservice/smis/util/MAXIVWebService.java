package ispyb.server.webservice.smis.util;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Exception;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.log4j.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import generated.ws.smis.BeamlineScientistsVO;
import generated.ws.smis.Exception_Exception;
import generated.ws.smis.ExpSessionInfoLightVO;
import generated.ws.smis.FinderException_Exception;
import generated.ws.smis.InnerScientistVO;
import generated.ws.smis.ProposalInfoLightVO;
import generated.ws.smis.ProposalParticipantInfoLightVO;
import generated.ws.smis.SMISWebService;
import generated.ws.smis.SampleSheetInfoLightVO;
import generated.ws.smis.Training;
import generated.ws.smis.UserDataAccessLightVO;
import generated.ws.smis.UserLightVO;
import ispyb.common.util.Constants;
import ispyb.server.smis.UpdateFromSMIS;

public class MAXIVWebService implements SMISWebService {

	private static final Logger LOG = Logger.getLogger(UpdateFromSMIS.class);
	
	private String serverUrl = "";
	
	public void init() {
		if ("".equals(this.serverUrl)) {
			this.serverUrl = Constants.getProperty("userportal.url");
		}
	}

	public List<Long> findNewMXProposalPKs_OLD (String startDateStr, String endDateStr) {
		List<Long> pks = new ArrayList<Long>();

        StringBuilder url = new StringBuilder("https://").append(this.serverUrl).append("/api/Beamlines/BioMAX/proposals/?access_token=")
                .append(this.getToken())
                .append("&filter=").append(this.getFilter(startDateStr, endDateStr));

        JSONArray jsonProposals = readJsonArrayFromUrl(url.toString());

        int len = jsonProposals.length();
        for (int i = 0; i < len; i++) {
            JSONObject jsonProposal = (JSONObject) jsonProposals.get(i);

            Long proposalId = null;
            try {
                proposalId = (Long.valueOf(((Integer) jsonProposal.get("propid"))).longValue());
                LOG.info("Proposal found: " + proposalId);
            } catch (Exception ex) {
                //TODO: Handle exception
                ex.printStackTrace();
            }
            pks.add(proposalId);
        }

		
		System.out.println("Number of proposals found : " + pks.size());
		LOG.info("Number of proposals found : " + pks.size());
	
		return pks;
	}

    public List<Long> findNewMXProposalPKs (String startDateStr, String endDateStr) {
        List<Long> pks = new ArrayList<Long>();
		init();
        try {
            StringBuilder url = new StringBuilder("https://").append(this.serverUrl).append("/api/Sessions/?access_token=")
                .append(this.getToken())
                .append("&filter=").append(this.getFilter(startDateStr, endDateStr, "BioMAX"));

            JSONArray jsonSessions = readJsonArrayFromUrl(url.toString());

            int len = jsonSessions.length();
            for (int i=0; i<len; i++){
                JSONObject jsonSession = (JSONObject)jsonSessions.get(i);

                Long proposalId = null;
                Long sessionId = null;
                try {
                    proposalId = (Long.valueOf(((Integer)jsonSession.get("propid"))).longValue());
                    sessionId = (Long.valueOf(((Integer)jsonSession.get("sessionid"))).longValue());
                    LOG.info("Session " +sessionId +" for Proposal " +proposalId + " found");
                } catch(Exception ex){
                    //TODO: Handle exception
                    ex.printStackTrace();
                }

                if (pks.contains(Long.valueOf(proposalId)) == false){
                    pks.add(proposalId);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        System.out.println("Number of proposals found : " + pks.size());
        LOG.info("Number of proposals found : " + pks.size());

        return pks;
    }
	
	public List<ProposalParticipantInfoLightVO> findMainProposersForProposal(Long propId) {
		List<ProposalParticipantInfoLightVO> proposers = new ArrayList<ProposalParticipantInfoLightVO>();

		ArrayList<JSONObject>  jsonProposalAuthors = new ArrayList<JSONObject>();
		try {
			jsonProposalAuthors = getProposalAuthorsForPropid(propId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		JSONObject jsonProposal = getProposalForId(propId);

		//Get all co-proposer ids
		ArrayList<Integer> writers = new ArrayList<Integer>();
		for (JSONObject jsonProposalAuthor : jsonProposalAuthors) {
			writers.add((Integer)jsonProposalAuthor.get("userid"));
		}

		for(Integer writerId : writers) {
			JSONObject jsonParticipant = getUserForId(writerId);
			
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
			
			proposers.add(participant);
		}

		LOG.info("Proposers found : " + proposers.size());
		
		return proposers;
	}
	
	public List<ProposalParticipantInfoLightVO> findParticipantsForProposal(Long propId) {
		List<ProposalParticipantInfoLightVO> participants = new ArrayList<ProposalParticipantInfoLightVO>();
		
		try {
            JSONObject jsonProposal = getProposalForId(propId);

            //Get all co-proposer ids
            ArrayList<Integer> visitors = new ArrayList<Integer>();
            ArrayList<JSONObject> jsonSessions = new ArrayList<JSONObject>();
            try {
                jsonSessions = getSessionsForProposal(propId);
            } catch (Exception ex){
                ex.printStackTrace();
                throw new Exception(ex.getMessage());
            }

            for (JSONObject jsonSession : jsonSessions) {

                ArrayList<JSONObject> jsonParticipants = getParticipantsForSession((Integer) jsonSession.get("sessionid"));
                for (JSONObject jsonParticipant : jsonParticipants) {
                    visitors.add((Integer) jsonParticipant.get("userid"));
                }
            }

            for (Integer visitorId : visitors) {
                JSONObject jsonParticipant = getUserForId(visitorId);

                ProposalParticipantInfoLightVO participant = new ProposalParticipantInfoLightVO();
                try {
                    participant.setCategoryCode("MX");
                    participant.setCategoryCounter(propId.intValue());
                    participant.setBllogin((String) jsonParticipant.get("username"));

                    Integer labId = (Integer) jsonParticipant.get("institute");
                    JSONObject jsonLab = getLabForId(labId);
                    participant.setLabAddress1((String) jsonLab.get("address") + "," + (String) jsonLab.get("country"));
                    participant.setLabCity((String) jsonLab.get("city"));
                    participant.setScientistEmail((String) jsonParticipant.get("email"));
                    if (jsonLab.get("department") != JSONObject.NULL)
                        participant.setLabDeparment((String) jsonLab.get("department"));
                    String labname = (String) jsonLab.get("name");
                    if (labname.length() >= 45) {
                        labname = labname.substring(0, 40).concat("...");
                        System.out.println("Truncated lab name :" + labname);
                    }
                    participant.setLabName(labname);
                    participant.setProposalPk(propId);
                    String title = (String) jsonProposal.get("title");
                    if (title.length() >= 200) {
                        title = title.substring(0, 195).concat("...");
                        System.out.println("Truncated Tilte :" + title);
                    }
                    participant.setProposalTitle(title);
                    participant.setScientistFirstName((String) jsonParticipant.get("firstname"));
                    participant.setScientistName((String) jsonParticipant.get("lastname"));
                } catch (Exception ex) {
                    //TODO: Handle exception
                    ex.printStackTrace();
                }

                participants.add(participant);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		
		LOG.info("Participants found : " + participants.size());
		
		return participants;
	}

	@Override
	public String createUserName(Long aLong) {
		return null;
	}

	public List<ExpSessionInfoLightVO> findRecentSessionsInfoLightForProposalPkAndDays(Long propId, Integer days){
		List<ExpSessionInfoLightVO> sessions = new ArrayList<ExpSessionInfoLightVO>();
		init();
		JSONObject jsonProposal = getProposalForId(propId);
        ArrayList<JSONObject> jsonSessions = new ArrayList<JSONObject>();
		ArrayList<JSONObject> jsonLocalContacts = new ArrayList<JSONObject>();
		InnerScientistVO localContact = new InnerScientistVO();

        try {
            jsonSessions = getSessionsForProposal(propId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

		try {
			jsonLocalContacts = getProposalLocalContactForPropid(propId);
			localContact.setName((String) jsonLocalContacts.get(0).get("lastname"));
			localContact.setFirstName((String) jsonLocalContacts.get(0).get("firstname"));
			localContact.setPhone((String) jsonLocalContacts.get(0).get("phone"));
			localContact.setEmail((String) jsonLocalContacts.get(0).get("email"));
			//localContact.setTitle((String) jsonLocalContacts.get(0).get("title"));
		} catch (Exception ex) {
			ex.printStackTrace();
			localContact.setFirstName("Manager");
			localContact.setName("BioMAX");
		}
		for(JSONObject jsonSession : jsonSessions){
			ExpSessionInfoLightVO session = new ExpSessionInfoLightVO();


			try{
				Integer sessionId =  (Integer)jsonSession.get("sessionid");
				ArrayList<JSONObject> jsonShifts = getShiftsForSession(sessionId);
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
					session.setName(((Integer)jsonSession.get("sessionnum")).toString());
					session.setFirstLocalContact(localContact);
					String title = (String)jsonProposal.get("title");
					if(title.length() >= 200){
						title = title.substring(0,195).concat("...");
						System.out.println("Truncated Title :" + title);
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
				} else {
					LOG.info("Empty session found for DUO sessionid"  + sessionId.toString());
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
	
	public void addDoiToSessionBySessionPk(final Long expSessionPk, final String doi) throws Exception_Exception {
			// TODO Auto-generated method stub
			
		}
		
	public void addDoiToSessionByProposalBeamlineAndDate(String arg0, Integer arg1, String arg2, Calendar arg3,
			String arg4) throws Exception_Exception {
		// TODO Auto-generated method stub		
	}
	
	private JSONObject getProposalForId(Long propId){
		JSONObject proposal = null;
		init();
		
		StringBuilder url = new StringBuilder("https://").append(this.serverUrl).append("/api/Proposals/")
				.append(propId).append("?access_token=").append(this.getToken());
		
		proposal = readJsonObjectFromUrl(url.toString());
		
		return proposal;
	}

	private ArrayList<JSONObject> getProposalLocalContactForPropid(Long propId)throws Exception {
		ArrayList<JSONObject> localContacts = new ArrayList<JSONObject>();
		init();

		StringBuilder url = new StringBuilder("https://").append(this.serverUrl).append("/api/Proposals/")
				.append(propId).append("/localcontacts").append("?access_token=").append(this.getToken());

		JSONArray jsonLocalContacts = readJsonArrayFromUrl(url.toString());

		int len = jsonLocalContacts.length();
		for (int i = 0; i < len; i++) {
			JSONObject jsonLocalContact = (JSONObject) jsonLocalContacts.get(i);
			localContacts.add(jsonLocalContact);
		}

		return localContacts;
	}

	private ArrayList<JSONObject> getProposalAuthorsForPropid(Long propId)throws Exception {
		ArrayList<JSONObject> authors = new ArrayList<JSONObject>();
		init();

		StringBuilder url = new StringBuilder("https://").append(this.serverUrl).append("/api/Proposals/")
				.append(propId).append("/authors").append("?access_token=").append(this.getToken());

		JSONArray jsonAuthors = readJsonArrayFromUrl(url.toString());

		int len = jsonAuthors.length();
		for (int i = 0; i < len; i++) {
			JSONObject jsonAuthor = (JSONObject) jsonAuthors.get(i);
			authors.add(jsonAuthor);
		}

		return authors;
	}
	
	private ArrayList<JSONObject> getBeamlinesForProposal(Long propId){
		ArrayList<JSONObject> beamlines = new ArrayList<JSONObject>();
		init();
		
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
	
	private ArrayList<JSONObject> getSessionsForProposal(Long propId) throws Exception {
		ArrayList<JSONObject> sessions = new ArrayList<JSONObject>();
		init();

        StringBuilder url = new StringBuilder("https://").append(this.serverUrl).append("/api/Proposals/")
                .append(propId).append("/sessions/").append("?access_token=").append(this.getToken());


        JSONArray jsonSessions = readJsonArrayFromUrl(url.toString());

        int len = jsonSessions.length();
        for (int i = 0; i < len; i++) {
            JSONObject jsonSession = (JSONObject) jsonSessions.get(i);
            sessions.add(jsonSession);
        }
		
		return sessions;
	}
	
	private ArrayList<JSONObject> getShiftsForSession(Integer sessionId){
		ArrayList<JSONObject> shifts = new ArrayList<JSONObject>();
		init();
		
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
		init();
		
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
		init();
		
		StringBuilder url = new StringBuilder("https://").append(this.serverUrl).append("/api/Clients/")
				.append(userId).append("?access_token=").append(this.getToken());
		
		user = readJsonObjectFromUrl(url.toString());
		
		return user;
	}
	
	private JSONObject getLabForId(Integer labId){
		JSONObject lab = null;
		init();
		
		StringBuilder url = new StringBuilder("https://").append(this.serverUrl).append("/api/Institutes/")
				.append(labId).append("?access_token=").append(this.getToken());

		lab = readJsonObjectFromUrl(url.toString());
		
		return lab;
	}
	
	private JSONObject readJsonObjectFromUrl(String url){
		JSONObject jsonObj = null;

		CloseableHttpClient httpclient = getHttpClient();
		HttpGet httpGet = new HttpGet(url.toString());
		
		try{
			CloseableHttpResponse response = httpclient.execute(httpGet);
			//System.out.println(response.getStatusLine());
			String jsonStr = IOUtils.toString(new InputStreamReader((response.getEntity().getContent()), "UTF-8"));
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

		CloseableHttpClient httpclient = getHttpClient();
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
		
		CloseableHttpClient httpclient = getHttpClient();
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


    private String getFilter(String startDateStr, String endDateStr, String beamline) throws Exception {
        StringBuilder filter = new StringBuilder();
        String filterStr = "";

        SimpleDateFormat sdfin = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfout = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = sdfin.parse(startDateStr);
        String startDate=sdfout.format(convertedDate);
        convertedDate = sdfin.parse(endDateStr);
        String endDate=sdfout.format(convertedDate);

        JSONObject jsonField1 = new JSONObject();
        jsonField1.put("beamline", beamline);

        /*JSONObject jsonFieldStart = new JSONObject();
        jsonFieldStart.put("gte", startDate);
        JSONObject jsonFieldEnd = new JSONObject();
        jsonFieldEnd.put("lte", endDate);
        JSONArray jsonArrayDates = new JSONArray();
        jsonArrayDates.put(jsonFieldStart);
        jsonArrayDates.put(jsonFieldEnd);
        JSONObject jsonFieldBetween = new JSONObject();
        jsonFieldBetween.put("and", jsonArrayDates);
        JSONObject jsonField2 = new JSONObject();
        jsonField2.put("shifts.startDate", jsonFieldBetween);*/

        JSONObject jsonFieldStart = new JSONObject();
        jsonFieldStart.put("gte", startDate);
        JSONObject jsonField2 = new JSONObject();
        jsonField2.put("shifts.startDate", jsonFieldStart);

        JSONObject jsonFieldEnd = new JSONObject();
        jsonFieldEnd.put("lte", endDate);
        JSONObject jsonField3 = new JSONObject();
        jsonField3.put("shifts.startDate", jsonFieldEnd);

        JSONObject jsonFieldNeq = new JSONObject();
        jsonFieldNeq.put("neq", JSONObject.NULL);
        JSONObject jsonField4 = new JSONObject();
        jsonField4.put("submitted", jsonFieldNeq);

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonField1);
        jsonArray.put(jsonField2);
        jsonArray.put(jsonField3);
        jsonArray.put(jsonField4);

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
	
	private String getFilter(String startDateStr, String endDateStr){
		StringBuilder filter = new StringBuilder();
		String filterStr = "";
		
		String startYear = startDateStr.substring(6, 10);
		String endYear = endDateStr.substring(6, 10);
		StringBuilder regexp = new StringBuilder("/^").append(startYear).append("|^").append(endYear).append("/");
		JSONObject jsonExp = new JSONObject();
		jsonExp.put("regexp", regexp.toString());
		JSONObject jsonField1 = new JSONObject();
        jsonField1.put("propid", jsonExp);
        JSONObject jsonField2 = new JSONObject();
        jsonField2.put("status", "A");
		/*JSONObject jsonField3 = new JSONObject();
		jsonField3.put("beamline_assigned", "BioMAX");*/
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonField1);
        jsonArray.put(jsonField2);
		//jsonArray.put(jsonField3);
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

	private static SSLContext buildSSLContext()
			throws NoSuchAlgorithmException, KeyManagementException,
			KeyStoreException {
		SSLContext sslcontext = SSLContexts.custom()
				.setSecureRandom(new SecureRandom())
				.loadTrustMaterial(null, new TrustStrategy() {

					public boolean isTrusted(X509Certificate[] chain, String authType)
							throws CertificateException {
						return true;
					}
				})
				.build();
		return sslcontext;
	}

	private CloseableHttpClient getHttpClient(){
		CloseableHttpClient httpclient = null;
		try {
			// Trust all certs
			SSLContext sslcontext = buildSSLContext();

			// Allow TLSv1 protocol only
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslcontext,
					new String[]{"TLSv1"},
					null,
					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);


			httpclient = HttpClients.custom()
					.setSSLSocketFactory(sslsf)
					.build();
		} catch (Exception ex) {
			LOG.debug("readJsonObjectFromUrl: "+ ex.getMessage());
			httpclient = HttpClients.createDefault();
		}
		return httpclient;
	}

	@Override
	public ProposalParticipantInfoLightVO findLatestProposalParticipantByScientistPk(Long arg0)
			throws Exception_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProposalInfoLightVO findProposalInfoLightByProposalPk(Long arg0) throws Exception_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SampleSheetInfoLightVO> findSamplesheetInfoLightForProposalPk(Long arg0, boolean arg1)
			throws Exception_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SampleSheetInfoLightVO> findSamplesheetInfoLightForSessionPk(Long arg0) throws Exception_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserDataAccessLightVO> getAclsForSession(String s, Integer integer, String s1, Calendar calendar) {
		return null;
	}

	@Override
	public List<ExpSessionInfoLightVO> findSessionsInfoLightInRange(Calendar arg0, Calendar arg1)
			throws FinderException_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserLightVO findUserByScientistPk(Long arg0) throws Exception_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserDataAccessLightVO> getAcls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserDataAccessLightVO> getAclsWithAllDelays(double arg0, double arg1, double arg2, double arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserDataAccessLightVO> getAclsForProposal(String s, Integer integer) {
		return null;
	}

	@Override
	public List<UserDataAccessLightVO> getAclsWithEndDelay(double arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BeamlineScientistsVO> getScientistsWithDataAccessForAllBeamlines() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isTrainingValidForDate(String arg0, Training arg1, Calendar arg2) throws Exception_Exception {
		// TODO Auto-generated method stub
		// added for MAXIV (but not used) because this method should be overridden.
		return false;
	}

}
