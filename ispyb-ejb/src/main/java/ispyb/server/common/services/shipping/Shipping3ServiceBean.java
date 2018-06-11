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
package ispyb.server.common.services.shipping;

import ispyb.server.biosaxs.services.sql.SqlTableMapper;

import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;

import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * <p>
 * This session bean handles ISPyB Shipping3.
 * </p>
 */
@Stateless
public class Shipping3ServiceBean implements Shipping3Service, Shipping3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Shipping3ServiceBean.class);

	// Generic HQL request to find instances of Shipping3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK(boolean fetchDewars) {
		return "from Shipping3VO vo " + (fetchDewars ? "left join fetch vo.dewarVOs " : "")
				+ "where vo.shippingId = :pk";

	}

	private static final String FIND_BY_PK(boolean fetchDewars, boolean fetchSessions) {
		if (fetchDewars){
			return "FROM Shipping3VO vo LEFT JOIN FETCH vo.dewarVOs dewars " + (fetchSessions ? " LEFT JOIN FETCH dewars.sessionVO " : "")
					+ " WHERE vo.shippingId = :pk";
		}
		return FIND_BY_PK(fetchDewars);
	}
		
	private static final String FIND_BY_PK(boolean fetchDewars, boolean fetchContainers, boolean fetchSamples) {
		if (fetchDewars){
			return "FROM Shipping3VO vo LEFT JOIN FETCH vo.dewarVOs dewars " 
					+ (fetchContainers ? " LEFT JOIN FETCH dewars.containerVOs co " : "")
					+ (fetchSamples ? " LEFT JOIN FETCH co.sampleVOs " : "")
					+ " WHERE vo.shippingId = :pk";
		}
		return FIND_BY_PK(false);
	}
	
	private static final String FIND_BY_PK(boolean fetchDewars, boolean fetchContainers, boolean fetchSamples, boolean fetchSubSamples) {
		if (fetchDewars){
			return "FROM Shipping3VO vo LEFT JOIN FETCH vo.dewarVOs dewars " 
					+ (fetchContainers ? " LEFT JOIN FETCH dewars.containerVOs co " : "")
					//+ (fetchSamples ? " LEFT JOIN FETCH co.sampleVOs sa " : "")
					+ (fetchSamples ? " LEFT JOIN FETCH co.sampleVOs sa LEFT JOIN FETCH sa.blsampleImageVOs " : "")
					+ (fetchSubSamples ? " LEFT JOIN FETCH sa.blSubSampleVOs " : "")
					+ " WHERE vo.shippingId = :pk";
		}
		return FIND_BY_PK(fetchDewars);
	}
		
	private static final String FIND_BY_PROPOSAL_ID(boolean fetchDewars, boolean fetchContainers, boolean feacthSamples) {
		if (fetchDewars){
			return "FROM Shipping3VO vo LEFT JOIN FETCH vo.dewarVOs dewars " 
					+ (fetchContainers ? " LEFT JOIN FETCH dewars.containerVOs co " : "")
					+ (feacthSamples ? " LEFT JOIN FETCH co.sampleVOs sa LEFT JOIN FETCH sa.blsampleImageVOs LEFT JOIN FETCH sa.blSubSampleVOs " : " ")
					+  " LEFT JOIN FETCH vo.sessions se "
					+  " LEFT JOIN FETCH se.proposalVO proposal "
					+ " WHERE proposal.proposalId = :proposalId";
		}
		return FIND_BY_PK(fetchDewars);
	}
		
	private static final String FIND_BY_SHIPPING_ID() {
		return "select  " +
				SqlTableMapper.getShippingTable() + ", (select count(*) from BLSample where BLSample.containerId = Container.containerId) as sampleCount, " +
				SqlTableMapper.getContainerTable() + " , (select count(*) from StockSolution where Dewar.dewarId = StockSolution.boxId) as stockSolutionCount, " +
				SqlTableMapper.getDewarTable()  +
				" from Shipping \r\n"
				+ " left join Dewar on Dewar.shippingId = Shipping.shippingId \r\n"
				+ " left join Container on Dewar.dewarId = Container.dewarId \r\n"
				+ " where Shipping.shippingId = :shippingId";
	}
		
	private static final String FIND_BY_PROPOSAL_ID() {
		return "select  " +
				SqlTableMapper.getShippingTable() + ", (select count(*) from BLSample where BLSample.containerId = Container.containerId) as sampleCount, " +
				SqlTableMapper.getContainerTable() + " , (select count(*) from StockSolution where Dewar.dewarId = StockSolution.boxId) as stockSolutionCount, " +
				SqlTableMapper.getDewarTable()  +
				" from Shipping \r\n"
				+ " left join Dewar on Dewar.shippingId = Shipping.shippingId \r\n"
				+ " left join Container on Dewar.dewarId = Container.dewarId \r\n"
				+ " where Shipping.proposalId = :proposalId";
	}
		

		// Generic HQL request to find all instances of Shipping3
		// TODO choose between left/inner join
	private static final String FIND_ALL() {
		return "from Shipping3VO vo ";
	}

	private final static String UPDATE_PROPOSALID_STATEMENT = " update Shipping  set proposalId = :newProposalId "
			+ " WHERE proposalId = :oldProposalId"; // 2 old value to be replaced

	private final static String COUNT_SHIPPING_INFO = "SELECT COUNT(DISTINCT(histo.dewarTransportHistoryId)) eventsNumber, "
			+ " count(DISTINCT(bls.blSampleId)) samplesNumber "
			+ "FROM Shipping s  "
			+ " LEFT JOIN Dewar d ON (d.shippingId=s.shippingId) "
			+ "  LEFT JOIN Container c ON c.dewarId = d.dewarId "
			+ "	 LEFT JOIN BLSample bls ON bls.containerId = c.containerId "
			+ "  LEFT JOIN DewarTransportHistory histo ON (histo.dewarId = d.dewarId) "
			+
			// TODO use the Constants -- problem while deploying app.
			// "AND (histo.dewarStatus='"+ Constants.SHIPPING_STATUS_AT_ESRF + "' " +
			// " OR histo.dewarStatus='" + Constants.SHIPPING_STATUS_SENT_TO_USER + "')" +
			"AND (histo.dewarStatus='atESRF' "
			+ "OR histo.dewarStatus='sent to User') "
			+ "WHERE s.shippingId = :shippingId GROUP BY s.shippingId ";

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public Shipping3ServiceBean() {
	};

	/**
	 * Create new Shipping3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public Shipping3VO create(final Shipping3VO vo) throws Exception {
				
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the Shipping3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public Shipping3VO update(final Shipping3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the Shipping3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		Shipping3VO vo = findByPk(pk, true);
		delete(vo);
	}

	/**
	 * Remove the Shipping3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Shipping3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Shipping3 value object
	 */
	public Shipping3VO findByPk(final Integer pk, final boolean withDewars) throws Exception {
	
		checkCreateChangeRemoveAccess();
		try {
			return (Shipping3VO) entityManager.createQuery(FIND_BY_PK(withDewars)).setParameter("pk", pk)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public Shipping3VO findByPk(final Integer pk, final boolean withDewars, final boolean withcontainers, final boolean withSamples) throws Exception {
		
		checkCreateChangeRemoveAccess();
		try {
			return (Shipping3VO) entityManager.createQuery(FIND_BY_PK(withDewars, withcontainers, withSamples)).setParameter("pk", pk)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}		
	}

	public String findSerialShippingByPk(final Integer pk, final boolean withDewars, final boolean withcontainers, final boolean withSamples, final boolean withSubSamples) throws Exception {
		
		checkCreateChangeRemoveAccess();
		String st = null;
		Shipping3VO vo = this.findByPk(pk, withDewars, withcontainers, withSamples, withSubSamples);
		if (vo != null ) {
			st = serialize( vo);			
		}
		return st;
	}

	public Shipping3VO findByPk(final Integer pk, final boolean withDewars, final boolean withcontainers, final boolean withSamples, final boolean withSubSamples) throws Exception {
		
		checkCreateChangeRemoveAccess();
		try {
			return (Shipping3VO) entityManager.createQuery(FIND_BY_PK(withDewars, withcontainers, withSamples, withSubSamples)).setParameter("pk", pk)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}		
	}
	
	@Override
	public List<Map<String, Object>> getShippingById(final Integer shippingId) throws Exception {
		
		checkCreateChangeRemoveAccess();
		String mySQLQuery = FIND_BY_SHIPPING_ID();
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("shippingId", shippingId);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		List<Map<String, Object>> aliasToValueMapList = query.list();
		return aliasToValueMapList;
	}
	
	
	@Override
	public List<Map<String, Object>> getShippingByProposalId(final Integer proposalId) throws Exception {
	
		checkCreateChangeRemoveAccess();
		String mySQLQuery = FIND_BY_PROPOSAL_ID();
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("proposalId", proposalId);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		List<Map<String, Object>> aliasToValueMapList = query.list();
		return aliasToValueMapList;
	}

	// TODO remove following method if not adequate
	/**
	 * Find all Shipping3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<Shipping3VO> findAll() throws Exception {
	
		List<Shipping3VO> foundEntities = entityManager.createQuery(FIND_ALL()).getResultList();
		return foundEntities;
	}
	
	@SuppressWarnings("unchecked")
	public List<Shipping3VO> findFiltered(final Integer proposalId, final String type) throws Exception {

		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(Proposal3VO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (type != null && !type.isEmpty()) {
			crit.add(Restrictions.like("shippingType", type));
		}

		if (proposalId != null) {
			crit.createCriteria("proposalVO");
			crit.add(Restrictions.eq("proposalId", proposalId));
		}

		crit.addOrder(Order.desc("shippingId"));

		List<Shipping3VO> foundEntities = crit.list();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove Shipping3 entities. If not set rollback only and throw
	 * AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
				// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
				// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
				// to the one checking the needed access rights
				// autService.checkUserRightToChangeAdminData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findByStatus(final String status, final java.util.Date dateStart,final boolean withDewars) throws Exception {
	
		Session session = (Session) this.entityManager.getDelegate();
		Criteria criteria = session.createCriteria(Shipping3VO.class);
		if (status != null && !status.isEmpty()) {
			criteria.add(Restrictions.like("shippingStatus", status));
		}
		
		if (dateStart != null) {
			criteria.add(Restrictions.ge("creationDate", dateStart));
		}

		if (withDewars) {
			criteria.setFetchMode("dewarVOs", FetchMode.JOIN);
		}
		criteria.addOrder(Order.desc("shippingId")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT
																											// RESULTS !

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findByProposal(final Integer userProposalId, final boolean withDewars) throws Exception {
		
		Session session = (Session) this.entityManager.getDelegate();
		Criteria criteria = session.createCriteria(Shipping3VO.class);

		if (userProposalId != null) {
			Criteria subCritProposal = criteria.createCriteria("proposalVO");
			subCritProposal.add(Restrictions.eq("proposalId", userProposalId));
		}

		if (withDewars) {
			criteria.setFetchMode("dewarVOs", FetchMode.JOIN);
		}
		criteria.addOrder(Order.desc("creationDate"));
		criteria.addOrder(Order.desc("shippingId"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findByProposal(final Integer proposalId,final boolean fetchDewars, final boolean fetchContainers, final boolean fetchSamples) throws Exception {
		
		try {
			return  entityManager.createQuery(FIND_BY_PROPOSAL_ID(fetchDewars, fetchContainers, fetchSamples))
					.setParameter("proposalId", proposalId)
					.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findByCreationDate(final Date firstDate, final boolean withDewars) throws Exception {
		
		Session session = (Session) this.entityManager.getDelegate();
		Criteria criteria = session.createCriteria(Shipping3VO.class);

		if (firstDate != null) {
			criteria.add(Restrictions.ge("creationDate", firstDate));
		}

		if (withDewars) {
			criteria.setFetchMode("dewarVOs", FetchMode.JOIN);
		}
		criteria.addOrder(Order.desc("creationDate"));
		criteria.addOrder(Order.desc("shippingId"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findByCreationDateInterval(final Date firstDate, final Date endDate) throws Exception {
		
		Session session = (Session) this.entityManager.getDelegate();
		Criteria criteria = session.createCriteria(Shipping3VO.class);

		if (firstDate != null) {
			criteria.add(Restrictions.ge("creationDate", firstDate));
		}

		if (endDate != null) {
			criteria.add(Restrictions.le("creationDate", endDate));
		}

		criteria.addOrder(Order.desc("creationDate"));
		criteria.addOrder(Order.desc("shippingId"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findByBeamLineOperator(final String beamlineOperatorSiteNumber, final boolean withDewars)
			throws Exception {
		
		Session session = (Session) this.entityManager.getDelegate();
		Criteria criteria = session.createCriteria(Shipping3VO.class);

		if (beamlineOperatorSiteNumber != null) {
			Criteria sessionCriteria = criteria.createCriteria("sessions").add(
					Restrictions.eq("operatorSiteNumber", beamlineOperatorSiteNumber));
			// .addOrder(Order.desc(propertyName))
			if (withDewars) {
				criteria.setFetchMode("dewarVOs", FetchMode.JOIN);
			}
			criteria.addOrder(Order.desc("creationDate"));
			criteria.addOrder(Order.desc("shippingId"));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return criteria.list();
		} else
			return null;
	}

	@Override
	public List<Shipping3VO> findByProposalCode(final String proposalCode, final boolean withDewars) throws Exception {
		
		return this.findFiltered(null, null, proposalCode, null, null, null, null, null, withDewars);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findFiltered(final Integer proposalId, final String shippingName, final String proposalCode,
			final String proposalNumber, final String mainProposer, final Date date, final Date date2,
			final String operatorSiteNumber, final boolean withDewars) throws Exception {
		
		Session session = (Session) this.entityManager.getDelegate();
		Criteria criteria = session.createCriteria(Shipping3VO.class);

		if ((shippingName != null) && (!shippingName.isEmpty())) {
			criteria.add(Restrictions.like("shippingName", shippingName));
		}

		if (date != null) {
			criteria.add(Restrictions.ge("creationDate", date));
		}

		if (date2 != null) {
			criteria.add(Restrictions.le("creationDate", date2));
		}

		if (proposalId != null || proposalCode != null || mainProposer != null) {
			Criteria subCritProposal = criteria.createCriteria("proposalVO");
			if (proposalId != null) {
				subCritProposal.add(Restrictions.eq("proposalId", proposalId));
			}
			if (proposalId == null && proposalCode != null && !proposalCode.isEmpty()) {
				subCritProposal.add(Restrictions.like("code", proposalCode).ignoreCase());
			}
			if ((mainProposer != null) && (!mainProposer.isEmpty())) {
				Criteria subCritPerson = subCritProposal.createCriteria("personVO");
				subCritPerson.add(Restrictions.like("familyName", mainProposer).ignoreCase());
			}

		}

		if (operatorSiteNumber != null) {
			criteria.createAlias("sessions", "se");
			criteria.add(Restrictions.eq("se.operatorSiteNumber", operatorSiteNumber));
		}

		if (withDewars) {
			criteria.setFetchMode("dewarVOs", FetchMode.JOIN);
		}
		criteria.addOrder(Order.desc("creationDate"));
		criteria.addOrder(Order.desc("shippingId"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<Shipping3VO> findByCustomQuery(final Integer proposalId, final String shippingName, final String proposalCode,
			final String proposalNumber, final String mainProposer, final Date date, final Date date2, final String operatorSiteNumber)
			throws Exception {

		return this.findFiltered(proposalId, shippingName, proposalCode, proposalNumber, mainProposer, date, date2,
				operatorSiteNumber, false);
	}

	/**
	 * update the proposalId, returns the nb of rows updated
	 * 
	 * @param newProposalId
	 * @param oldProposalId
	 * @return
	 * @throws Exception
	 */
	public Integer updateProposalId(final Integer newProposalId, final Integer oldProposalId) throws Exception {
		
		int nbUpdated = 0;
		Query query = entityManager.createNativeQuery(UPDATE_PROPOSALID_STATEMENT)
				.setParameter("newProposalId", newProposalId).setParameter("oldProposalId", oldProposalId);
		nbUpdated = query.executeUpdate();

		return new Integer(nbUpdated);
	}

	public int deleteAllSamplesAndContainersForShipping(Integer shippingId) throws Exception {
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		Dewar3Service dewarService = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);
		Container3Service containerService = (Container3Service) ejb3ServiceLocator.getLocalService(Container3Service.class);

		int nb = 0;
		Shipping3VO ship = this.findByPk(shippingId, true);
		Set<Dewar3VO> dewars = ship.getDewarVOs();
		for (Dewar3VO dewar3vo : dewars) {
			Dewar3VO dewar = dewarService.findByPk(dewar3vo.getDewarId(), true, true);
			Set<Container3VO> containers = dewar.getContainerVOs();
			for (Container3VO container3vo : containers) {
				containerService.deleteByPk(container3vo.getContainerId());
				nb = nb + 1;
			}
		}
		return nb;
	}

	@Override
	public List<Shipping3VO> findByProposalType(String proposalType) throws Exception {
		return this.findFiltered(null, null, proposalType, null, null, null, null, null, false);
	}

	@Override
	public List<Shipping3VO> findByProposalCodeAndDates(String proposalType, Date firstDate, Date endDate) throws Exception {
		return this.findFiltered(null, null, proposalType, null, null, firstDate, endDate, null, false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findByIsStorageShipping(final Boolean isStorageShipping) throws Exception {
		
		Session session = (Session) this.entityManager.getDelegate();
		Criteria criteria = session.createCriteria(Shipping3VO.class);
		criteria.add(Restrictions.eq("isStorageShipping", isStorageShipping));
		return criteria.list();
	}

	public Shipping3VO loadEager(Shipping3VO vo) throws Exception {
		Shipping3VO newVO = this.findByPk(vo.getShippingId(), true);
		return newVO;
	}

	public Integer[] countShippingInfo(final Integer shippingId) throws Exception {
		Query query = entityManager.createNativeQuery(COUNT_SHIPPING_INFO).setParameter("shippingId", shippingId);
		List orders = query.getResultList();
		int nb = orders.size();
		Integer nbSamples = 0;
		Integer nbDewarHistory = 0;
		Integer[] tab = new Integer[2];
		if (nb > 0) {
			Object[] o = (Object[]) orders.get(0);
			nbDewarHistory = ((BigInteger) o[0]).intValue();
			nbSamples = ((BigInteger) o[1]).intValue();
		}
		tab[0] = nbDewarHistory;
		tab[1] = nbSamples;
		return tab;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ispyb.server.common.services.shipping.Shipping3Service#findByPk(java.lang.Integer, boolean, boolean)
	 */
	@Override
	public Shipping3VO findByPk(final Integer pk, final boolean withDewars, final boolean withSession) throws Exception {
		
		checkCreateChangeRemoveAccess();
		try {
			if (withSession){
				return (Shipping3VO) entityManager.createQuery(FIND_BY_PK(withDewars, withSession)).setParameter("pk", pk).getSingleResult();
			}
			return (Shipping3VO) entityManager.createQuery(FIND_BY_PK(withDewars)).setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/* Private methods ------------------------------------------------------ */

	public static String serialize(Shipping3VO shipping) {
		Gson gson =  new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).serializeNulls().create();		
		return  gson.toJson(shipping);
	}

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * 
	 * @param vo
	 *            the data to check
	 * @param create
	 *            should be true if the value object is just being created in the DB, this avoids some checks like
	 *            testing the primary key
	 * @exception VOValidateException
	 *                if data is not correct
	 */
	private void checkAndCompleteData(Shipping3VO vo, boolean create) throws Exception {
		if (create) {
			if (vo.getShippingId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getShippingId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}