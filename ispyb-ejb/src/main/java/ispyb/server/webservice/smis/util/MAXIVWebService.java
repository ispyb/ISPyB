package ispyb.server.webservice.smis.util;

import generated.ws.smis.SampleSheetInfoLightVO;
import ispyb.common.util.Constants;
import ispyb.server.smis.UpdateFromSMIS;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import generated.ws.smis.SMISWebService;

import generated.ws.smis.Exception_Exception;
import generated.ws.smis.ExpSessionInfoLightVO;
import generated.ws.smis.FinderException_Exception;
import generated.ws.smis.ProposalParticipantInfoLightVO;

import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

public class MAXIVWebService implements SMISWebService{
	
	private static final Logger LOG = Logger.getLogger(UpdateFromSMIS.class);
	
	private String serverUrl = "";
	private String token = "";
	
	public MAXIVWebService()
	{
		this.serverUrl = Constants.getProperty("userportal.url");
		this.getToken();
	}
	
	public List<Long> findNewMXProposalPKs (String startDateStr, String endDateStr) {
		StringBuilder url = new StringBuilder("https://").append(this.serverUrl).append("/duo/api/v1/proposals/pbd?startdate=")
				.append(startDateStr).append("&enddate=").append(endDateStr).append("&beamline=MX");
		
		JSONObject response = readJsonDataFromUrl(url.toString());
		
		List<Long> pks = new ArrayList<Long>();
		JSONArray jsonArray = response.getJSONArray("proposals");
		int len = jsonArray.length();
		for (int i=0; i<len; i++){ 
			Long proposalId = null;
			try {
				proposalId = (Long.valueOf(((Integer)jsonArray.get(i))).longValue());
				LOG.info("Proposal found: " + proposalId);
			} catch(Exception ex){
				//TODO: Handle exception
				ex.printStackTrace();
			}
			pks.add(proposalId);
		}
		
		LOG.info("No of proposals found : " + pks.size());
		
		return pks;
	}
	
	public List<ProposalParticipantInfoLightVO> findMainProposersForProposal(Long propId) {
		List<ProposalParticipantInfoLightVO> proposers = new ArrayList<ProposalParticipantInfoLightVO>();
		
		StringBuilder url = new StringBuilder("https://").append(this.serverUrl).append("/duo/api/v1/proposals/").append(propId).append("/proposers");
		JSONObject response = readJsonDataFromUrl(url.toString());
		
		JSONArray jsonProposers = response.getJSONArray("proposers");
		
		int len = jsonProposers.length();
		for (int i=0; i<len; i++){ 
			ProposalParticipantInfoLightVO proposer = new ProposalParticipantInfoLightVO();
			JSONObject jsonProposer = ((JSONObject)jsonProposers.get(i)).getJSONObject("proposer");
			
			try{
				proposer.setCategoryCode((String)jsonProposer.get("category_code"));
				proposer.setCategoryCounter((Integer)jsonProposer.get("category_counter"));
				proposer.setBllogin((String)jsonProposer.get("bl_login"));
				proposer.setLabAddress1((String)jsonProposer.get("lab_address_1"));
				proposer.setLabCity((String)jsonProposer.get("lab_city"));
				proposer.setLabDeparment((String)jsonProposer.get("lab_department"));
				String labname = (String)jsonProposer.get("lab_name");
				if(labname.length() >= 45){
					labname = labname.substring(0,40).concat("...");
					System.out.println("Truncated lab name :" + labname);
				}
				proposer.setLabName(labname);
				proposer.setProposalPk(Long.valueOf(((Integer)jsonProposer.get("proposal_pk")).longValue()));
				String title = (String)jsonProposer.get("proposal_title");
				if(title.length() >= 200){
					title = title.substring(0,195).concat("...");
					System.out.println("Truncated Tilte :" + title);
				}
				proposer.setProposalTitle(title);
				proposer.setScientistFirstName((String)jsonProposer.get("scientist_firstname"));
				proposer.setScientistName((String)jsonProposer.get("scientist_name"));
			} catch(Exception ex){
				//TODO: Handle exception
				ex.printStackTrace();
			}
			
			proposers.add(proposer);
		} 
		
		LOG.info("Proposers found : " + proposers.size());
		
		return proposers;
	}

	public List<ProposalParticipantInfoLightVO> findParticipantsForProposal(Long propId) throws Exception_Exception {
		List<ProposalParticipantInfoLightVO> participants = new ArrayList<ProposalParticipantInfoLightVO>();
		
		StringBuilder url = new StringBuilder("https://").append(this.serverUrl).append("/duo/api/v1/proposals/").append(propId).append("/participants");
		JSONObject response = readJsonDataFromUrl(url.toString());
		
		JSONArray jsonParticipants = response.getJSONArray("participants");
		
		int len = jsonParticipants.length();
		for (int i=0; i<len; i++){ 
			ProposalParticipantInfoLightVO participant = new ProposalParticipantInfoLightVO();
			JSONObject jsonParticipant = ((JSONObject)jsonParticipants.get(i)).getJSONObject("participant");
			
			try{
				participant.setCategoryCode((String)jsonParticipant.get("category_code"));
				participant.setCategoryCounter((Integer)jsonParticipant.get("category_counter"));
				participant.setBllogin((String)jsonParticipant.get("bl_login"));
				participant.setLabAddress1((String)jsonParticipant.get("lab_address_1"));
				participant.setLabCity((String)jsonParticipant.get("lab_city"));
				participant.setLabDeparment((String)jsonParticipant.get("lab_department"));//Check for NULL
				String labname = (String)jsonParticipant.get("lab_name");
				if(labname.length() >= 45){
					labname = labname.substring(0,40).concat("...");
					System.out.println("Truncated lab name :" + labname);
				}
				participant.setLabName(labname);
				participant.setProposalPk(Long.valueOf(((Integer)jsonParticipant.get("proposal_pk")).longValue()));
				String title = (String)jsonParticipant.get("proposal_title");
				if(title.length() >= 200){
					title = title.substring(0,195).concat("...");
				}
				participant.setProposalTitle(title);
				participant.setScientistFirstName((String)jsonParticipant.get("scientist_firstname"));
				participant.setScientistName((String)jsonParticipant.get("scientist_name"));
				participant.setProposer(true);
			} catch(Exception ex){
				//TODO: Handle exception
				ex.printStackTrace();
			}
			
			participants.add(participant);
		}
		
		LOG.info("Participants found : " + participants.size());
		
		return participants;
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

	public List<ExpSessionInfoLightVO> findRecentSessionsInfoLightForProposalPkAndDays(Long propId, Integer days)
			throws FinderException_Exception {
		List<ExpSessionInfoLightVO> sessList = new ArrayList<ExpSessionInfoLightVO>();
		
		StringBuilder url = new StringBuilder("https://").append(this.serverUrl).append("/duo/api/v1/proposals/").append(propId).append("/sessions/");
		JSONObject response = readJsonDataFromUrl(url.toString());
		
		JSONArray jsonSessions = response.getJSONArray("sessions");
		
		int len = jsonSessions.length();
		for (int i=0; i<len; i++){ 
			ExpSessionInfoLightVO session= new ExpSessionInfoLightVO();
			JSONObject jsonSession = ((JSONObject)jsonSessions.get(i)).getJSONObject("session");
			
			try{
				session.setBeamlineName((String)jsonSession.get("beamline_name"));
				//session.setBeamlinePk(Long.valueOf(((Integer)jsonSession.get("beamline_pk")).longValue()));
				session.setBeamlinePk(Long.valueOf(12345));
				session.setProposalGroup((Integer)jsonSession.get("proposal_group"));
				session.setProposalGroupCode((String)jsonSession.get("proposal_group_code"));
				session.setProposalPk(Long.valueOf(((Integer)jsonSession.get("proposal_pk")).longValue()));
				session.setProposalSubmissionPk(Long.valueOf(((Integer)jsonSession.get("proposal_submission_pk")).longValue()));
				String title = (String)jsonSession.get("proposal_title");
				if(title.length() >= 200){
					title = title.substring(0,195).concat("...");
				}
				session.setProposalTitle(title);
				session.setCategCode((String)jsonSession.get("category_code"));
				session.setCategCounter((Integer)jsonSession.get("category_counter"));
				String startDate[] = ((String)jsonSession.get("start_date")).split("-");
				session.setStartDate(new GregorianCalendar(Integer.parseInt(startDate[0]),Integer.parseInt(startDate[1]),Integer.parseInt(startDate[2])));
				String endDate[] = ((String)jsonSession.get("end_date")).split("-");
				session.setEndDate(new GregorianCalendar(Integer.parseInt(endDate[0]),Integer.parseInt(endDate[1]),Integer.parseInt(endDate[2])));
				//session.setStartShift((Integer)jsonSession.get("start_shitf"));
				session.setStartShift(Integer.valueOf(1));
				session.setShifts((Integer)jsonSession.get("shifts"));
				sessList.add(session);
			} catch(Exception ex){
				//TODO: Handle exception
				ex.printStackTrace();
			}
		}

		return sessList;
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
		return 0;
	}
	
	public void getToken() {
		
        HttpHost target = new HttpHost(this.serverUrl, 443, "https");
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(target.getHostName(), target.getPort()),
                new UsernamePasswordCredentials("ws_user", "1453996998"));
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider).build();
        
        try {

            // Create AuthCache instance
            AuthCache authCache = new BasicAuthCache();
            // Generate BASIC scheme object and add it to the local
            // auth cache
            BasicScheme basicAuth = new BasicScheme();
            authCache.put(target, basicAuth);

            // Add AuthCache to the execution context
            HttpClientContext localContext = HttpClientContext.create();
            localContext.setAuthCache(authCache);

            StringBuilder url = new StringBuilder("https://").append(this.serverUrl).append("/duo/api/v1/token");
            HttpGet httpget = new HttpGet(url.toString());

            CloseableHttpResponse response = httpclient.execute(target, httpget, localContext);
            try {
                this.token = EntityUtils.toString(response.getEntity());
                this.token = this.token.substring(1, this.token.length()-1);
                System.out.println("Token received " + this.token);
            } finally {
                response.close();
            }
        } catch(Exception ex){
        	ex.printStackTrace();
        }
        finally {
        	try{
        		httpclient.close();
            }catch(Exception ex){
            	ex.printStackTrace();
            }
        }
    }

	public JSONObject readJsonDataFromUrl(String url){
		JSONObject jsonObj = null;
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url.toString());
		httpGet.setHeader("Authorization", "Bearer " + this.token);
		
		try{
			CloseableHttpResponse response = httpclient.execute(httpGet);
			System.out.println(response.getStatusLine());
			String jsonStr = IOUtils.toString(new InputStreamReader(
                    (response.getEntity().getContent())));

			jsonObj = new JSONObject(jsonStr);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return jsonObj;
	}
	
}
