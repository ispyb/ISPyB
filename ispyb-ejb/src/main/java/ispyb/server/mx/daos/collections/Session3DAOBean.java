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
package ispyb.server.mx.daos.collections;

import ispyb.common.util.Constants;
import ispyb.server.mx.vos.collections.Session3VO;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * <p>
 * The data access object for Session3 objects (rows of table BLSession).
 * </p>
 * 
 * @see {@link Session3DAO}
 */
@Stateless
public class Session3DAOBean implements Session3DAO {

	private final static Logger LOG = Logger.getLogger(Session3DAOBean.class);

	// Generic HQL request to find instances of Session3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK(boolean fetchDataCollectionGroup, boolean fetchEnergyScan, boolean fetchXFESpectrum) {
		return "from Session3VO vo " + (fetchDataCollectionGroup ? "left join fetch vo.dataCollectionGroupVOs " : "")
				+ (fetchEnergyScan ? "left join fetch vo.energyScanVOs " : "")
				+ (fetchXFESpectrum ? "left join fetch vo.xfeSpectrumVOs " : "") + "where vo.sessionId = :pk";
	}

	// Generic HQL request to find all instances of Session3
	// TODO choose between left/inner join
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

	private static String getProposalCodeNumberQuery() {
		String query = "select * " + " FROM BLSession ses, Proposal pro "
				+ "WHERE ses.proposalId = pro.proposalId AND pro.proposalCode like :code AND pro.proposalNumber = :number "
				+ "AND ses.beamLineName like :beamLineName " + "AND ses.endDate >= " + Constants.MYSQL_ORACLE_CURENT_DATE + " "
				+ "AND DATE(ses.startDate) <= DATE(" + Constants.MYSQL_ORACLE_CURENT_DATE + ")  ORDER BY startDate DESC ";

		return query;
	}

	private static String getProposalCodeNumberOldQuery() {
		String query = "select * "
				+ " FROM BLSession ses, Proposal pro "
				+ "WHERE ses.proposalId = pro.proposalId AND pro.proposalCode like :code AND pro.proposalNumber = :number "
				// +
				// "AND ses.startDate <= " + now + " AND (ses.endDate >= " + now +
				// " OR ses.endDate IS NULL) ORDER BY sessionId DESC ";
				+ "AND ses.endDate >= " + Constants.MYSQL_ORACLE_CURENT_DATE + "  AND ses.startDate <= "
				+ Constants.MYSQL_ORACLE_CURENT_DATE + "  ORDER BY sessionId DESC ";

		return query;
	}

	private final static String NB_OF_COLLECTS = "SELECT count(*) FROM DataCollection, DataCollectionGroup "
			+ " WHERE DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId"
			+ " and DataCollection.numberOfImages >4 and DataCollectionGroup.sessionId  = :sessionId ";

	private final static String NB_OF_TESTS = "SELECT count(*) FROM DataCollection, DataCollectionGroup "
			+ " WHERE DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId"
			+ " and DataCollection.numberOfImages <=4 and DataCollectionGroup.sessionId  = :sessionId ";
	
	private final String[] beamlinesToProtect = { "ID29", "ID23-1", "ID23-2", "ID30A-1", "ID30A-2","ID30A-3", "ID30B" };
	
	private final String[] account_not_to_protect = { "OPID", "OPD", "MXIHR" };
	

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(Session3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 */
	public Session3VO update(Session3VO vo) throws Exception {
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/* Deletion methods ----------------------------------------------------- */

	/**
	 * <p>
	 * Deletes the given value object.
	 * </p>
	 * 
	 * @param vo
	 *            the value object to delete.
	 */
	public void delete(Session3VO vo) {
		// entityManager.remove(vo) is not allowed, need to reattach the entity before remove it
		entityManager.remove(entityManager.merge(vo));
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the Session3VO instance matching the given primary key.
	 * </p>
	 * <p>
	 * <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u> if the value object has to be used out the
	 * EJB container.
	 * </p>
	 * 
	 * @param pk
	 *            the primary key of the object to load.
	 * @param fetchRelation1
	 *            if true, the linked instances by the relation "relation1" will be set.
	 * @param fetchRelation2
	 *            if true, the linked instances by the relation "relation2" will be set.
	 */
	public Session3VO findByPk(Integer pk, boolean fetchDataCollectionGroup, boolean fetchEnergyScan, boolean fetchXFESpectrum) {
		try {
			return (Session3VO) entityManager.createQuery(FIND_BY_PK(fetchDataCollectionGroup, fetchEnergyScan, fetchXFESpectrum))
					.setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * <p>
	 * Returns the Session3VO instances.
	 * </p>
	 * <p>
	 * <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u> if the value object has to be used out the
	 * EJB container.
	 * </p>
	 * 
	 * @param fetchRelation1
	 *            if true, the linked instances by the relation "relation1" will be set.
	 * @param fetchRelation2
	 *            if true, the linked instances by the relation "relation2" will be set.
	 */
	@SuppressWarnings("unchecked")
	public List<Session3VO> findAll(boolean fetchDataCollectionGroup, boolean fetchEnergyScan, boolean fetchXFESpectrum) {
		return entityManager.createQuery(FIND_ALL(fetchDataCollectionGroup, fetchEnergyScan, fetchXFESpectrum)).getResultList();
	}

	/* Other methods ------------------------------------------------------ */

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
	public List<Session3VO> findFiltered(Integer proposalId, Integer nbMax, String beamline, Date date1, Date date2, Date dateEnd,
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

		// usedFlag =1 or endDate > today
		if (usedFlag)
			crit.add(Restrictions.sqlRestriction("(usedFlag = 1 OR endDate >= " + Constants.MYSQL_ORACLE_CURENT_DATE + " )"));

		if (nbMax != null)
			crit.setMaxResults(nbMax);

		if (nbShifts != null) {
			crit.add(Restrictions.eq("nbShifts", nbShifts));
		}

		if (operatorSiteNumber != null) {
			crit.add(Restrictions.eq("operatorSiteNumber", operatorSiteNumber));
		}

		crit.addOrder(Order.desc("startDate"));

		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<Session3VO> findByShippingId(Integer shippingId) {
		String query = FIND_BY_SHIPPING_ID;
		List<Session3VO> col = this.entityManager.createNativeQuery(query, "sessionNativeQuery")
				.setParameter("shippingId", shippingId).getResultList();
		return col;
	}

	@SuppressWarnings("unchecked")
	public List<Session3VO> findSessionToBeProtected(Integer delay, Integer window) {
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

		crit.add(Restrictions.in("beamlineName", beamlinesToProtect));

		// account not to protect: opid*, opd*, mxihr*
		Criteria subCrit = crit.createCriteria("proposalVO");

		subCrit.add(Restrictions.not(Restrictions.in("code", account_not_to_protect)));

		crit.addOrder(Order.asc("lastUpdate"));

		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<Session3VO> findSessionNotProtectedToBeProtected(Date date1, Date date2) {
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

	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * 
	 * @param vo
	 *            the data to check
	 * @param create
	 *            should be true if the value object is just being created in the DB, this avoids some checks like testing the primary
	 *            key
	 * @exception VOValidateException
	 *                if data is not correct
	 */
	private void checkAndCompleteData(Session3VO vo, boolean create) throws Exception {
		if (create) {
			if (vo.getSessionId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getSessionId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
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
	 * update the proposalId, returns the nb of rows updated
	 * 
	 * @param newProposalId
	 * @param oldProposalId
	 * @return
	 */
	public Integer updateProposalId(Integer newProposalId, Integer oldProposalId) {
		int nbUpdated = 0;
		Query query = entityManager.createNativeQuery(UPDATE_PROPOSALID_STATEMENT).setParameter("newProposalId", newProposalId)
				.setParameter("oldProposalId", oldProposalId);
		nbUpdated = query.executeUpdate();

		return new Integer(nbUpdated);
	}

	/**
	 * 
	 * @param expSessionPk
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Session3VO> findByExpSessionPk(Long expSessionPk) {
		Session session = (Session) this.entityManager.getDelegate();
		Criteria crit = session.createCriteria(Session3VO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (expSessionPk != null)
			crit.add(Restrictions.eq("expSessionPk", expSessionPk));

		crit.addOrder(Order.desc("startDate"));
		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public Session3VO findByAutoProcScalingId(Integer autoProcScalingId) {
		String query = FIND_BY_AUTOPROCSCALING_ID;
		try {
			List<Session3VO> col = this.entityManager.createNativeQuery(query, "sessionNativeQuery")
					.setParameter("autoProcScalingId", autoProcScalingId).getResultList();
			if (col != null && col.size() > 0) {
				return col.get(0);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

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

	@Override
	public List<Session3VO> findFiltered(Integer nbMax, String beamline, Date date1, Date date2, Date dateEnd,
			boolean usedFlag, Object object, String operatorSiteNumber) {
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

		// usedFlag =1 or endDate > today
		if (usedFlag)
			crit.add(Restrictions.sqlRestriction("(usedFlag = 1 OR endDate >= " + Constants.MYSQL_ORACLE_CURENT_DATE + " )"));

		if (nbMax != null)
			crit.setMaxResults(nbMax);


		if (operatorSiteNumber != null) {
			crit.add(Restrictions.eq("operatorSiteNumber", operatorSiteNumber));
		}

		crit.addOrder(Order.desc("startDate"));

		return crit.list();
	}

}
