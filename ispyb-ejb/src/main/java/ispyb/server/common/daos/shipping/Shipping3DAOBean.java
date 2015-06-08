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

package ispyb.server.common.daos.shipping;

//import ispyb.common.util.ServerConstants;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;

import java.math.BigInteger;
import java.sql.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * <p>
 * The data access object for Shipping3 objects (rows of table Shipping).
 * </p>
 * 
 * @see {@link Shipping3DAO}
 */
@Stateless
public class Shipping3DAOBean implements Shipping3DAO {

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

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(Shipping3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 */
	public Shipping3VO update(Shipping3VO vo) throws Exception {
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
	public void delete(Shipping3VO vo) {
		entityManager.remove(vo);
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the Shipping3VO instance matching the given primary key.
	 * </p>
	 * <p>
	 * <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u> if the value object has to be
	 * used out the EJB container.
	 * </p>
	 * 
	 * @param pk
	 *            the primary key of the object to load.
	 * @param fetchRelation1
	 *            if true, the linked instances by the relation "relation1" will be set.
	 * @param fetchRelation2
	 *            if true, the linked instances by the relation "relation2" will be set.
	 */
	public Shipping3VO findByPk(Integer pk, boolean fetchRelation1) {
		try {
			return (Shipping3VO) entityManager.createQuery(FIND_BY_PK(fetchRelation1)).setParameter("pk", pk)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Shipping3VO findByPk(Integer pk, boolean dewars, boolean sessions) {
		try {
			if (sessions){
				return (Shipping3VO) entityManager.createQuery(FIND_BY_PK(dewars, sessions)).setParameter("pk", pk).getSingleResult();
			}
			return (Shipping3VO) entityManager.createQuery(FIND_BY_PK(dewars)).setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * <p>
	 * Returns the Shipping3VO instances.
	 * </p>
	 * <p>
	 * <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u> if the value object has to be
	 * used out the EJB container.
	 * </p>
	 * 
	 * @param fetchRelation1
	 *            if true, the linked instances by the relation "relation1" will be set.
	 * @param fetchRelation2
	 *            if true, the linked instances by the relation "relation2" will be set.
	 */
	@SuppressWarnings("unchecked")
	public List<Shipping3VO> findAll() {
		return entityManager.createQuery(FIND_ALL()).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Shipping3VO> findFiltered(Integer proposalId, String type) {

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

		return crit.list();
	}

	/* Private methods ------------------------------------------------------ */

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

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findByStatus(String status, boolean fetchDewars) {
		Session session = (Session) this.entityManager.getDelegate();
		Criteria criteria = session.createCriteria(Shipping3VO.class);
		if (status != null && !status.isEmpty()) {
			criteria.add(Restrictions.like("shippingStatus", status));
		}
		if (fetchDewars) {
			criteria.setFetchMode("dewarVOs", FetchMode.JOIN);
		}
		criteria.addOrder(Order.desc("shippingId")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT
																											// RESULTS !

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findByBeamLineOperator(String operatorSiteNumber, boolean fetchDewars) {
		Session session = (Session) this.entityManager.getDelegate();
		Criteria criteria = session.createCriteria(Shipping3VO.class);

		if (operatorSiteNumber != null) {
			Criteria sessionCriteria = criteria.createCriteria("sessions").add(
					Restrictions.eq("operatorSiteNumber", operatorSiteNumber));
			// .addOrder(Order.desc(propertyName))
			if (fetchDewars) {
				criteria.setFetchMode("dewarVOs", FetchMode.JOIN);
			}
			criteria.addOrder(Order.desc("creationDate"));
			criteria.addOrder(Order.desc("shippingId"));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return criteria.list();
		} else
			return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findFiltered(Integer proposalId, String shippingName, String proposalCode,
			Integer proposalNumber, String mainProposer, Date dateStart, Date dataEnd, String operatorSiteNumber, String type,
			boolean withDewars) {

		Session session = (Session) this.entityManager.getDelegate();
		Criteria criteria = session.createCriteria(Shipping3VO.class);

		if ((shippingName != null) && (!shippingName.isEmpty())) {
			criteria.add(Restrictions.like("shippingName", shippingName));
		}

		if (dateStart != null) {
			criteria.add(Restrictions.ge("creationDate", dateStart));
		}

		if (dataEnd != null) {
			criteria.add(Restrictions.le("creationDate", dataEnd));
		}

		if (type != null) {
			criteria.add(Restrictions.like("shippingType", type));
		}

		if (proposalId != null || proposalCode != null || mainProposer != null) {
			Criteria subCritProposal = criteria.createCriteria("proposalVO");
			if (proposalId != null) {
				subCritProposal.add(Restrictions.eq("proposalId", proposalId));
			}
			if (proposalId == null && proposalCode != null && !proposalCode.isEmpty()) {
				subCritProposal.add(Restrictions.like("code", proposalCode).ignoreCase());
			}
			if (proposalId == null && proposalNumber != null )
				subCritProposal.add(Restrictions.like("number", proposalNumber).ignoreCase());
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

	/**
	 * update the proposalId, returns the nb of rows updated
	 * 
	 * @param newProposalId
	 * @param oldProposalId
	 * @return
	 */
	public Integer updateProposalId(Integer newProposalId, Integer oldProposalId) {
		int nbUpdated = 0;
		Query query = entityManager.createNativeQuery(UPDATE_PROPOSALID_STATEMENT)
				.setParameter("newProposalId", newProposalId).setParameter("oldProposalId", oldProposalId);
		nbUpdated = query.executeUpdate();

		return new Integer(nbUpdated);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findByIsStorageShipping(Boolean isStorageShipping) {
		Session session = (Session) this.entityManager.getDelegate();
		Criteria criteria = session.createCriteria(Shipping3VO.class);
		criteria.add(Restrictions.eq("isStorageShipping", isStorageShipping));
		return criteria.list();
	}

	@SuppressWarnings("rawtypes")
	public Integer[] countShippingInfo(Integer shippingId) {
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

}
