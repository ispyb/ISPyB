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
package ispyb.server.common.services.sessions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jboss.ejb3.annotation.TransactionTimeout;

import ispyb.common.util.Constants;
import ispyb.common.util.beamlines.ESRFBeamlineEnum;
import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.services.AuthorisationServiceLocal;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.collections.SessionWS3VO;

/**
 * <p>
 * This session bean handles ISPyB Session3.
 * </p>
 */
@Stateless
@TransactionTimeout(3600)
public class Session3ServiceBean implements Session3Service, Session3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Session3ServiceBean.class);
	
	// Generic HQL request to find instances of Session3 by pk
	private static final String FIND_BY_PK(boolean fetchDataCollectionGroup, boolean fetchEnergyScan, boolean fetchXFESpectrum) {
		return "from Session3VO vo " + (fetchDataCollectionGroup ? "left join fetch vo.dataCollectionGroupVOs " : "")
				+ (fetchEnergyScan ? "left join fetch vo.energyScanVOs " : "")
				+ (fetchXFESpectrum ? "left join fetch vo.xfeSpectrumVOs " : "") + "where vo.sessionId = :pk";
	}

	// Generic HQL request to find all instances of Session3
	private static final String FIND_ALL(boolean fetchDataCollectionGroup, boolean fetchEnergyScan, boolean fetchXFESpectrum) {
		return "from Session3VO vo " + (fetchDataCollectionGroup ? "left join fetch vo.dataCollectionGroupVOs " : "")
				+ (fetchEnergyScan ? "left join fetch vo.energyScanVOs " : "")
				+ (fetchXFESpectrum ? "left join fetch vo.xfeSpectrumVOs " : "");
	}

	private final static String SET_USED_SESSION_STATEMENT = " UPDATE BLSession SET usedFlag = 1 WHERE BLSession.proposalId = :proposalId"
			+ " and (BLSession.usedFlag is null OR BLSession.usedFlag = 0) "
			+ " and (BLSession.sessionId IN (select c.sessionId from DataCollectionGroup c) "
			+ " or BLSession.sessionId IN (select e.sessionId from EnergyScan e) "
			+ " or BLSession.sessionId IN (select x.sessionId from XFEFluorescenceSpectrum x)) ";// 1

	private final static String HAS_SESSION_DATACOLLECTIONGROUP = "SELECT COUNT(*) FROM DataCollectionGroup WHERE sessionId = :sessionId ";

	private static final String FIND_BY_SHIPPING_ID = "select * from BLSession, ShippingHasSession "
			+ " where BLSession.sessionId =  ShippingHasSession.sessionId " + " and ShippingHasSession.shippingId = :shippingId ";

	// Be careful, when JBoss starts, the property file is not loaded, and it tries to initialize the class and fails.
	// private static String FIND_BY_PROPOSAL_CODE_NUMBER = getProposalCodeNumberQuery();

	// private static String FIND_BY_PROPOSAL_CODE_NUMBER_OLD = getProposalCodeNumberOldQuery();

	private final static String UPDATE_PROPOSALID_STATEMENT = " update BLSession  set proposalId = :newProposalId "
			+ " WHERE proposalId = :oldProposalId"; // 2 old value to be replaced

	private static final String FIND_BY_AUTOPROCSCALING_ID = "select s.* from BLSession s, "
			+ " DataCollectionGroup g, DataCollection c, AutoProcIntegration api, AutoProcScaling_has_Int apshi, AutoProcScaling aps "
			+ " where s.sessionId = g.sessionId and  " + " g.dataCollectionGroupId = c.dataCollectionGroupId and "
			+ " c.dataCollectionId = api.dataCollectionId and " + " api.autoProcIntegrationId = apshi.autoProcIntegrationId and "
			+ " apshi.autoProcScalingId = aps.autoProcScalingId and " + " aps.autoProcScalingId = :autoProcScalingId ";
	
	private static final String FIND_BY_AUTOPROCPROGRAMATTACHMENT_ID = "select s.* from BLSession s, "
			+ " DataCollectionGroup g, DataCollection c, AutoProcIntegration api, AutoProcProgram autoprocProgram, AutoProcProgramAttachment autoProcProgramAttachment"
			+ " where s.sessionId = g.sessionId and  g.dataCollectionGroupId = c.dataCollectionGroupId and autoprocProgram.autoProcProgramId = api.autoProcProgramId"
			+ " and c.dataCollectionId = api.dataCollectionId and autoprocProgram.autoProcProgramId = autoProcProgramAttachment.autoProcProgramId "
			+ " and autoProcProgramAttachment.autoProcProgramAttachmentId = :autoProcProgramAttachmentId ";
	
	
	private static final String FIND_BY_AUTOPROCPROGRAM_ID = "select s.* from BLSession s, "
			+ " DataCollectionGroup g, DataCollection c, AutoProcIntegration api, AutoProcProgram autoprocProgram "
			+ " where s.sessionId = g.sessionId and  g.dataCollectionGroupId = c.dataCollectionGroupId and autoprocProgram.autoProcProgramId = api.autoProcProgramId"
			+ " and c.dataCollectionId = api.dataCollectionId and autoprocProgram.autoProcProgramId = :autoProcProgramId ";
	

	private static String getProposalCodeNumberQuery() {
		String query = "select * " + " FROM BLSession ses, Proposal pro "
				+ "WHERE ses.proposalId = pro.proposalId AND pro.proposalCode like :code AND pro.proposalNumber = :number "
				+ "AND ses.beamLineName like :beamLineName " + "AND ses.endDate >= " + Constants.MYSQL_ORACLE_CURRENT_DATE + " "
				+ "AND DATE(ses.startDate) <= DATE(" + Constants.MYSQL_ORACLE_CURRENT_DATE + ")  ORDER BY startDate DESC ";

		return query;
	}

	private static String getProposalCodeNumberOldQuery() {
		String query = "select * "
				+ " FROM BLSession ses, Proposal pro "
				+ "WHERE ses.proposalId = pro.proposalId AND pro.proposalCode like :code AND pro.proposalNumber = :number "
				+ "AND ses.endDate >= " + Constants.MYSQL_ORACLE_CURRENT_DATE + "  AND ses.startDate <= "
				+ Constants.MYSQL_ORACLE_CURRENT_DATE + "  ORDER BY sessionId DESC ";

		return query;
	}

	private final static String NB_OF_COLLECTS = "SELECT count(*) FROM DataCollection, DataCollectionGroup "
			+ " WHERE DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId"
			+ " and DataCollection.numberOfImages >4 and DataCollectionGroup.sessionId  = :sessionId ";

	private final static String NB_OF_TESTS = "SELECT count(*) FROM DataCollection, DataCollectionGroup "
			+ " WHERE DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId"
			+ " and DataCollection.numberOfImages <=4 and DataCollectionGroup.sessionId  = :sessionId ";
	
	//private final String[] beamlinesToProtect = { "ID29", "ID23-1", "ID23-2", "ID30A-1", "ID30A-2","ID30A-3", "ID30B" };
	private final String[] beamlinesToProtect = ESRFBeamlineEnum.getBeamlineNamesToBeProtected();
	
	private final String[] account_not_to_protect = { "OPID", "OPD", "MXIHR" };
	
	
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;
	
	@EJB
	private AuthorisationServiceLocal autService;

	@Resource
	private SessionContext context;

	public Session3ServiceBean() {
	};

	/**
	 * Create new Session3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public Session3VO create(final Session3VO vo) throws Exception {
		entityManager.persist(vo);
		return vo;

	}

	/**
	 * Update the Session3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public Session3VO update(final Session3VO vo) throws AccessDeniedException, Exception {
		checkChangeRemoveAccess(vo);
		entityManager.merge(vo);
		return vo;
	}

	/**
	 * Remove the Session3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws AccessDeniedException,Exception {
		Session3VO vo = this.findByPk(pk, false, false, false);
		entityManager.remove(vo);

	}

	/**
	 * Remove the Session3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Session3VO vo) throws AccessDeniedException,Exception {
		checkChangeRemoveAccess(vo);
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Session3 value object
	 */
	public Session3VO findByPk(Integer pk, boolean fetchDataCollectionGroup, boolean fetchEnergyScan, boolean fetchXFESpectrum) throws AccessDeniedException,Exception {
		try {
			Session3VO vo = (Session3VO) entityManager.createQuery(FIND_BY_PK(fetchDataCollectionGroup, fetchEnergyScan, fetchXFESpectrum))
					.setParameter("pk", pk).getSingleResult();
			checkChangeRemoveAccess(vo);
			return vo;
		} catch (NoResultException e) {
			return null;
		}
	}

	public SessionWS3VO findForWSByPk(final Integer pk, final boolean withDataCollectionGroup, final boolean withEnergyScan,
			final boolean withXFESpectrum) throws Exception {
		Session3VO found = findByPk(pk, withDataCollectionGroup, withEnergyScan, withXFESpectrum);
		SessionWS3VO sesLight = getWSSessionVO(found);
		return sesLight;
	}

	/**
	 * Find all Session3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<Session3VO> findAll(boolean fetchDataCollectionGroup, boolean fetchEnergyScan, boolean fetchXFESpectrum)
			throws Exception {
		return entityManager.createQuery(FIND_ALL(fetchDataCollectionGroup, fetchEnergyScan, fetchXFESpectrum)).getResultList();
	}

	public Integer updateUsedSessionsFlag(Integer proposalId) throws Exception {

			int nbUpdated = 0;
			Query query = entityManager.createNativeQuery(SET_USED_SESSION_STATEMENT).setParameter("proposalId", proposalId);
			nbUpdated = query.executeUpdate();

			return new Integer(nbUpdated);
	}

	public Integer hasDataCollectionGroups(Integer sessionId) throws Exception {

		Query query = entityManager.createNativeQuery(HAS_SESSION_DATACOLLECTIONGROUP).setParameter("sessionId", sessionId);
		try {
			BigInteger res = (BigInteger) query.getSingleResult();

			return new Integer(res.intValue());
		} catch (NoResultException e) {
			System.out.println("ERROR in hasDataCollectionGroups - NoResultException: " + sessionId);
			e.printStackTrace();
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	@WebMethod
	public List<Session3VO> findFiltered(Integer proposalId, Integer nbMax, String beamline, Date date1, Date date2, Date dateEnd,
			boolean usedFlag,  String operatorSiteNumber) {

		return findFiltered(proposalId, nbMax, beamline,  date1, date2,  dateEnd,
				usedFlag, null,  operatorSiteNumber);
	}
	


	@SuppressWarnings("unchecked")
	public List<Session3VO> findByShippingId(Integer shippingId) {
		String query = FIND_BY_SHIPPING_ID;
		List<Session3VO> col = this.entityManager.createNativeQuery(query, "sessionNativeQuery")
				.setParameter("shippingId", shippingId).getResultList();
		return col;
	}

	@SuppressWarnings("unchecked")
	public List<Session3VO> findByStartDateAndBeamLineNameAndNbShifts(final Integer proposalId, final Date startDateBegin,
			final Date startDateEnd, final String beamlineName, final Integer nbShifts) throws Exception {
		List<Session3VO> foundEntities = findFiltered(proposalId, null/* nbMax */, beamlineName, startDateBegin,
						startDateEnd, null/* endDate */, false/* usedFlag */, nbShifts, null);
		return foundEntities;
	}
	
	@SuppressWarnings("unchecked")
	public List<Session3VO> findSessionByDateProposalAndBeamline(int proposalId, String beamlineName, Date date) {
		List<Session3VO> sessions = new ArrayList<Session3VO>();
		sessions.addAll(this.findFiltered(proposalId, null, beamlineName, null, date, date, false, null));
		sessions.addAll(this.findFiltered(proposalId, null, beamlineName, null, date, date, true, null));
		return sessions;
	}
	
	@SuppressWarnings("unchecked")
	public List<Session3VO> findFiltered(Integer nbMax, String beamline, Date date1, Date date2, Date dateEnd,
			boolean usedFlag, String operatorSiteNumber) {
		Session session = (Session) this.entityManager.getDelegate();
		Criteria crit = session.createCriteria(Session3VO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (beamline != null)
			crit.add(Restrictions.like("beamlineName", beamline));

		if (date1 != null)
			crit.add(Restrictions.ge("startDate", date1));
		if (date2 != null)
			crit.add(Restrictions.le("startDate", date2));

		if (dateEnd != null)
			crit.add(Restrictions.ge("endDate", dateEnd));

		// usedFlag =1 or endDate > yesterday
		if (usedFlag)
			crit.add(Restrictions.sqlRestriction("(usedFlag = 1 OR endDate >= " + Constants.MYSQL_ORACLE_YESTERDAY + " )"));

		if (nbMax != null)
			crit.setMaxResults(nbMax);


		if (operatorSiteNumber != null) {
			crit.add(Restrictions.eq("operatorSiteNumber", operatorSiteNumber));
		}

		crit.addOrder(Order.desc("startDate"));

		return crit.list();
	}

	/**
	 * returns the session for a specified proposal with endDate > today and startDate <= today
	 * 
	 * @param code
	 * @param number
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Session3VO> findSessionByProposalCodeAndNumber(String code, String number, String beamLineName) {
		String query = null;
		List<Session3VO> listVOs = null;
		if (beamLineName == null || beamLineName.equals("")) {
			query = getProposalCodeNumberOldQuery();
			listVOs = this.entityManager.createNativeQuery(query, "sessionNativeQuery").setParameter("code", code)
					.setParameter("number", number).getResultList();
		} else {
			query = getProposalCodeNumberQuery();
			listVOs = this.entityManager.createNativeQuery(query, "sessionNativeQuery").setParameter("code", code)
					.setParameter("number", number).setParameter("beamLineName", beamLineName).getResultList();
		}
		if (listVOs == null || listVOs.isEmpty())
			return null;
		return listVOs;
	}
	
	/**
	 * returns the session for a specified proposal with endDate > today or null
	 * 
	 * @param code
	 * @param number
	 * @param detachLight
	 * @return
	 * @throws Exception
	 */
	public SessionWS3VO[] findForWSByProposalCodeAndNumber(final String code, final String number, final String beamLineName)
			throws Exception {
		List<Session3VO> foundEntities = findSessionByProposalCodeAndNumber(code, number, beamLineName);
		SessionWS3VO[] ret = getWSSessionVOs(foundEntities);
		LOG.info("findForWSByProposalCodeAndNumber : code= " + code + ", number= " + number + ", beamlineName= " + beamLineName);
		return ret;
	}

	/**
	 * returns the list of sessions which have to be protected
	 * 
	 * @return
	 * @throws Exception
	 */
	public SessionWS3VO[] findForWSToBeProtected(final Integer delay, final Integer window) throws Exception {

		List<Session3VO> foundEntities = findSessionToBeProtected(delay, window);
		if (foundEntities == null)
			return null;
		SessionWS3VO[] ret;
		ret = getWSSessionVOs(foundEntities);
		String listSessionIds = "";
		if (ret != null) {
			for (int i = 0; i < ret.length; i++) {
				listSessionIds = ret[i].getSessionId() + ", ";
			}
		}
		LOG.info("findForWSToBeProtected : " + listSessionIds);
		return ret;
	}
	

	/**
	 * returns the list of sessions which have to be protected
	 * 
	 * @return
	 * @throws Exception
	 */
	public SessionWS3VO[] findForWSNotProtectedToBeProtected(final Date date1, final Date date2) throws Exception {
		LOG.info("findForWSNotProtectedToBeProtected");		
		List<Session3VO> foundEntities = findSessionNotProtectedToBeProtected(date1, date2);
		if (foundEntities == null)
			return null;
		SessionWS3VO[] ret = getWSSessionVOs(foundEntities);
		return ret;
	}
	
	/**
	 * get the number of datcollections which have more then 4 images
	 * 
	 * @param sesId
	 * @return
	 * @throws Exception
	 */
	public Integer getNbOfCollects(Integer sessionId) throws Exception {

		Query query = entityManager.createNativeQuery(NB_OF_COLLECTS).setParameter("sessionId", sessionId);
		try {
			BigInteger res = (BigInteger) query.getSingleResult();

			return new Integer(res.intValue());
		} catch (NoResultException e) {
			System.out.println("ERROR in getNbOfCollects - NoResultException: " + sessionId);
			e.printStackTrace();
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * get the number of datacollections which have less/or 4 images
	 * 
	 * @param dcgId
	 * @return
	 * @throws Exception
	 */
	public Integer getNbOfTests(Integer sessionId) throws Exception {

		Query query = entityManager.createNativeQuery(NB_OF_TESTS).setParameter("sessionId", sessionId);
		try {
			BigInteger res = (BigInteger) query.getSingleResult();

			return new Integer(res.intValue());
		} catch (NoResultException e) {
			System.out.println("ERROR in getNbOfTests - NoResultException: " + sessionId);
			e.printStackTrace();
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * update the proposalId, returns the nb of rows updated
	 * 
	 * @param newProposalId
	 * @param oldProposalId
	 * @return
	 * @throws Exception
	 */
	public Integer updateProposalId(Integer newProposalId, Integer oldProposalId) throws Exception {
			int nbUpdated = 0;
			Query query = entityManager.createNativeQuery(UPDATE_PROPOSALID_STATEMENT).setParameter("newProposalId", newProposalId)
					.setParameter("oldProposalId", oldProposalId);
			nbUpdated = query.executeUpdate();

			return new Integer(nbUpdated);
	}

	/**
	 * returns the session with the given expSessionPk
	 * 
	 * @param expSessionPk
	 * @return
	 */
	public Session3VO findByExpSessionPk(final Long expSessionPk) throws Exception {
		Session session = (Session) this.entityManager.getDelegate();
		Criteria crit = session.createCriteria(Session3VO.class);
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !
		
		if (expSessionPk != null)
			crit.add(Restrictions.eq("expSessionPk", expSessionPk));
		
		crit.addOrder(Order.desc("startDate"));
		
		List<Session3VO> foundEntities = crit.list();
			if (foundEntities == null || foundEntities.size() == 0) {
					return null;
			} else {
					return foundEntities.get(0);
			}
		
	}

	/**
	 * returns the session linked to the given autoProcScaling
	 * 
	 * @param autoProcScalingId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Session3VO findByAutoProcScalingId(final Integer autoProcScalingId) throws Exception {
		String query = FIND_BY_AUTOPROCSCALING_ID;
		List<Session3VO> col = this.entityManager.createNativeQuery(query, "sessionNativeQuery")
					.setParameter("autoProcScalingId", autoProcScalingId).getResultList();
		if (col != null && col.size() > 0) {
				return col.get(0);
		}
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public Session3VO findByAutoProcProgramAttachmentId(final Integer autoProcProgramAttachmentId) throws Exception {
		String query = FIND_BY_AUTOPROCPROGRAMATTACHMENT_ID;
		List<Session3VO> col = this.entityManager.createNativeQuery(query, "sessionNativeQuery")
					.setParameter("autoProcProgramAttachmentId", autoProcProgramAttachmentId).getResultList();
		if (col != null && col.size() > 0) {
				return col.get(0);
		}
		return null;
	}
	
	
	@Override
	public Session3VO findByAutoProcProgramId(int autoProcProgramId) {
		String query = FIND_BY_AUTOPROCPROGRAM_ID;
		@SuppressWarnings("unchecked")
		List<Session3VO> col = this.entityManager.createNativeQuery(query, "sessionNativeQuery")
					.setParameter("autoProcProgramId", autoProcProgramId).getResultList();
		if (col != null && col.size() > 0) {
				return col.get(0);
		}
		return null;
	}
	
	
	/**
	 * launch the data confidentiality for the specified session
	 */
	public void protectSession(Integer sessionId) throws Exception {
		if (sessionId != null) {

			Session3VO sessionVO = this.findByPk(sessionId, false, false, false);
			LOG.info("session to be protected = " + sessionId);			

			// Check if the session exists
			if (sessionVO == null) {
				LOG.info("session does not exist");				
			} 
			// Check if the beamline shall be protected
			else if (ESRFBeamlineEnum.retrieveBeamlineWithName(sessionVO.getBeamlineName()) == null ) {				
				LOG.info("beamline shall not be protected :  " + sessionVO.getBeamlineName());
			}
			// Check if the beamline shall be protected
			else if (!ESRFBeamlineEnum.retrieveBeamlineWithName(sessionVO.getBeamlineName()).isToBeProtected()) {				
				LOG.info("beamline shall not be protected :  " + sessionVO.getBeamlineName());
			}
			// Check if the session is already protected
			else if (sessionVO.getProtectedData() != null && sessionVO.getProtectedData().equals("OK")) {
				LOG.info("session is already protected :  " + sessionVO.getProtectedData());
			} else {				

				// Check the minimum delay to protect : 2 hours
				Date lastUpdate = sessionVO.getLastUpdate();
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				Integer delayToTrigger = 2;
				cal.add(Calendar.HOUR_OF_DAY, -delayToTrigger);
				Date limitDate = cal.getTime();

				if (lastUpdate.before(limitDate)) {

					try {
						// proposal account
						String proposalAccount = sessionVO.getProposalVO().getProposalAccount();
						// beamline
						ESRFBeamlineEnum abl = ESRFBeamlineEnum.retrieveBeamlineWithName(sessionVO.getBeamlineName());
						String beamline = abl == null ? "" : abl.getDirectoryName();
						// mx
						String isMx = "true";
						// directory
						Date folderDate = sessionVO.getStartDate();
						SimpleDateFormat dt = new SimpleDateFormat("yyyyMMdd");
						String directory = "";
						if (folderDate != null)
							directory = dt.format(folderDate);
						// call data protection tool
						//TODO put back when runtime error found
						HttpClient client = new DefaultHttpClient();
						List<NameValuePair> qparams = new ArrayList<NameValuePair>();
						qparams.add(new BasicNameValuePair("user", proposalAccount));
						qparams.add(new BasicNameValuePair("bl", beamline));
						qparams.add(new BasicNameValuePair("dir", directory));
						qparams.add(new BasicNameValuePair("mx", isMx));
						LOG.debug("post user = " + proposalAccount + ", beamline = " + beamline + ", directory = " + directory);
						URI uri = URIUtils.createURI("http", "dch.esrf.fr", -1, "/protect.php",
								URLEncodedUtils.format(qparams, "UTF-8"), null);
						HttpPost post = new HttpPost(uri);
						HttpResponse response = client.execute(post);
						BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
						String line = "";
						String protectedData = "";
						while ((line = rd.readLine()) != null) {
							System.out.println(line);
							protectedData += line;
						}

						// result to log into ispyb
						if (protectedData.length() > 1024)
							protectedData = protectedData.substring(0, 1024);
						sessionVO.setProtectedData(protectedData);
						this.update(sessionVO);
						
						LOG.info("end of session protection");
						
						if (client != null && client.getConnectionManager() != null)
					        client.getConnectionManager().closeExpiredConnections();
						
					} catch (IOException e) {
						//
						LOG.error("WS ERROR IOException: getDataToBeProtected " + sessionVO.getSessionId());
						e.printStackTrace();
					} catch (Exception e) {
						//
						LOG.error("WS ERROR: getDataToBeProtected " + sessionVO.getSessionId());
						e.printStackTrace();
					} 

				} else {
					LOG.info("session not protected because too recent");
				}
			}
		}
	}
	
	
	//******************************     PRIVATE METHODS  ********************************************************

	private List<Session3VO> findSessionToBeProtected(Integer delay, Integer window) {
		Session session = (Session) this.entityManager.getDelegate();
		Criteria crit = session.createCriteria(Session3VO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !
		Integer delayToTrigger = 26;
		Integer windowForTrigger = 24;
		if (delay != null)
			delayToTrigger = delay;
		if (window != null)
			windowForTrigger = window;

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR_OF_DAY, -delayToTrigger);

		Date date2 = cal.getTime();
		cal.add(Calendar.HOUR_OF_DAY, -windowForTrigger);
		Date date1 = cal.getTime();

		if (date1 != null)
			crit.add(Restrictions.ge("lastUpdate", date1));
		if (date2 != null)
			crit.add(Restrictions.le("lastUpdate", date2));
		
		String[] beamlinesToProtect = ESRFBeamlineEnum.getBeamlineNamesToBeProtected();
		
		if (LOG.isDebugEnabled()) {
			String beamlines = "";
			for (String beamline: beamlinesToProtect) { 
				beamlines = beamlines + " " + beamline;
			};
			LOG.debug("beamlinesToProtect: " + beamlines);
		}
		
		
		crit.add(Restrictions.in("beamlineName", beamlinesToProtect));

		// account not to protect: opid*, opd*, mxihr*
		Criteria subCrit = crit.createCriteria("proposalVO");

		subCrit.add(Restrictions.not(Restrictions.in("code", account_not_to_protect)));

		crit.addOrder(Order.asc("lastUpdate"));

		return crit.list();
	}
	
	private List<Session3VO> findSessionNotProtectedToBeProtected(Date date1, Date date2) {
		Session session = (Session) this.entityManager.getDelegate();
		Criteria crit = session.createCriteria(Session3VO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		Integer delayToTrigger = 8;
		Integer windowForTrigger = 14 * 24;

		cal.add(Calendar.HOUR_OF_DAY, -delayToTrigger);
		// launch the protection of sessions which have not been protected during the last 14 days.
		if (date2 == null)
			date2 = cal.getTime();

		if (date1 == null) {
			cal.setTime(date2);
			cal.add(Calendar.HOUR_OF_DAY, -windowForTrigger);
			date1 = cal.getTime();
		}

		if (date1 != null)
			crit.add(Restrictions.ge("lastUpdate", date1));
		if (date2 != null)
			crit.add(Restrictions.le("lastUpdate", date2));

		crit.add(Restrictions.in("beamlineName", beamlinesToProtect));

		crit.add(Restrictions.isNull("protectedData"));

		// account not to protect: opid*, opd*, mxihr*
		Criteria subCrit = crit.createCriteria("proposalVO");

		subCrit.add(Restrictions.not(Restrictions.in("code", account_not_to_protect)));

		crit.addOrder(Order.asc("lastUpdate"));

		List<Session3VO> listNotProtected = crit.list();
		LOG.info("find not protected sessions between " + date1 + " and  " + date2);
		if (listNotProtected != null) {
			String sessionsIds = "";
			for (Iterator iterator = listNotProtected.iterator(); iterator.hasNext();) {
				Session3VO session3vo = (Session3VO) iterator.next();
				sessionsIds = sessionsIds + ", " + session3vo.getSessionId();
			}
			LOG.info(listNotProtected.size() + " sessions found : " + sessionsIds);
		}

		return listNotProtected;
	}
	
	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private Session3VO getLightSession3VO(Session3VO vo) throws CloneNotSupportedException {
		if (vo == null)
			return null;
		Session3VO otherVO = (Session3VO) vo.clone();
		otherVO.setDataCollectionGroupVOs(null);
		otherVO.setEnergyScanVOs(null);
		otherVO.setXfeSpectrumVOs(null);
		return otherVO;
	}

	private SessionWS3VO[] getWSSessionVOs(List<Session3VO> entities) throws CloneNotSupportedException {
		if (entities == null)
			return null;
		ArrayList<SessionWS3VO> results = new ArrayList<SessionWS3VO>(entities.size());
		for (Session3VO vo : entities) {
			SessionWS3VO otherVO = getWSSessionVO(vo);
			if (otherVO != null)
				results.add(otherVO);
		}
		SessionWS3VO[] tmpResults = new SessionWS3VO[results.size()];
		return results.toArray(tmpResults);
	}

	private SessionWS3VO getWSSessionVO(Session3VO vo) throws CloneNotSupportedException {
		if (vo == null)
			return null;
		Session3VO otherVO = getLightSession3VO(vo);
		Integer beamLineSetupId = null;
		Integer proposalId = null;
		String proposalName = null;
		if (vo.getProposalVO() != null) {
			proposalName = vo.getProposalVO().getProposalAccount();
		}
		beamLineSetupId = otherVO.getBeamLineSetupVOId();
		proposalId = otherVO.getProposalVOId();
		otherVO.setBeamLineSetupVO(null);
		otherVO.setProposalVO(null);
		SessionWS3VO wsSession = new SessionWS3VO(otherVO);
		wsSession.setBeamLineSetupId(beamLineSetupId);
		wsSession.setProposalId(proposalId);
		wsSession.setProposalName(proposalName);
		return wsSession;
	}
	
	@SuppressWarnings("unchecked")
	@WebMethod
	private List<Session3VO> findFiltered(Integer proposalId, Integer nbMax, String beamline, Date date1, Date date2, Date dateEnd,
			boolean usedFlag, Integer nbShifts, String operatorSiteNumber) {

		Session session = (Session) this.entityManager.getDelegate();
		Criteria crit = session.createCriteria(Session3VO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (proposalId != null) {
			Criteria subCrit = crit.createCriteria("proposalVO");
			subCrit.add(Restrictions.eq("proposalId", proposalId));
		}
		if (beamline != null)
			crit.add(Restrictions.like("beamlineName", beamline));

		if (date1 != null)
			crit.add(Restrictions.ge("startDate", date1));
		if (date2 != null)
			crit.add(Restrictions.le("startDate", date2));

		if (dateEnd != null)
			crit.add(Restrictions.ge("endDate", dateEnd));

		// usedFlag =1 or endDate >= yesterday
		
		if (usedFlag) {
			crit.add(Restrictions.sqlRestriction("(usedFlag = 1 OR endDate >= " + Constants.MYSQL_ORACLE_CURRENT_DATE + " )"));
		}

		if (nbMax != null)
			crit.setMaxResults(nbMax);

		if (nbShifts != null) {
			crit.add(Restrictions.eq("nbShifts", nbShifts));
		}

		if (operatorSiteNumber != null) {
			crit.add(Restrictions.eq("operatorSiteNumber", operatorSiteNumber));
		}

		crit.addOrder(Order.desc("startDate"));
		List<Session3VO> ret = crit.list();
		return ret;
	}
	
	/**
	 * Check if user has access rights to change and remove Session3 entities. If not set rollback only and throw
	 * AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkChangeRemoveAccess(Session3VO vo) throws AccessDeniedException {
		if (vo == null) return;
		autService.checkUserRightToAccessSession(vo);				
	}







}