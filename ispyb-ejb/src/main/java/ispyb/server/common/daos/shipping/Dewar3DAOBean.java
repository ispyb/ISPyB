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

import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.shipping.Dewar3VO;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * <p>
 * The data access object for Dewar3 objects (rows of table TableName).
 * </p>
 * 
 * @see {@link Dewar3DAO}
 */
@Stateless
public class Dewar3DAOBean implements Dewar3DAO {

	public static final String NOT_AT_STORES = "!STORES%";

	public static final String LOCATION_EMPTY = "EMPTY"; // to encode URL parameters values

	private final Logger LOG = Logger.getLogger(Dewar3DAOBean.class);

	// Generic HQL request to find instances of Dewar3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK(boolean fetchContainers, boolean fetchDewarTransportHitory) {
		return "from Dewar3VO vo " + (fetchContainers ? "left join fetch vo.containerVOs " : "")
				+ (fetchDewarTransportHitory ? "left join fetch vo.dewarTransportHistoryVOs " : "")
				+ "where vo.dewarId = :pk";
	}

	// Generic HQL request to find all instances of Dewar3
	// TODO choose between left/inner join
	private static final String FIND_ALL(boolean fetchContainers, boolean fetchDewarTransportHitory) {
		return "from Dewar3VO vo " + (fetchContainers ? "left join fetch vo.containerVOs " : "")
				+ (fetchDewarTransportHitory ? "left join fetch vo.dewarTransportHistoryVOs " : "");
	}

	private final static String COUNT_DEWAR_SAMPLE = "SELECT " + " count(DISTINCT(bls.blSampleId)) samplesNumber "
			+ "FROM Shipping s  " + " LEFT JOIN Dewar d ON (d.shippingId=s.shippingId) "
			+ "  LEFT JOIN Container c ON c.dewarId = d.dewarId "
			+ "	 LEFT JOIN BLSample bls ON bls.containerId = c.containerId "
			+ "WHERE s.shippingId = d.shippingId AND d.dewarId = :dewarId GROUP BY d.dewarId ";

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(Dewar3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 */
	public Dewar3VO update(Dewar3VO vo) throws Exception {
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
	public void delete(Dewar3VO vo) {
		entityManager.remove(vo);
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the Dewar3VO instance matching the given primary key.
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
	public Dewar3VO findByPk(Integer pk, boolean fetchContainers, boolean fetchDewarTransportHitory) {
		try {
			return (Dewar3VO) entityManager.createQuery(FIND_BY_PK(fetchContainers, fetchDewarTransportHitory))
					.setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * <p>
	 * Returns the Dewar3VO instances.
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
	public List<Dewar3VO> findAll(boolean fetchContainers, boolean fetchDewarTransportHitory) {
		return entityManager.createQuery(FIND_ALL(fetchContainers, fetchDewarTransportHitory)).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Dewar3VO> findByCustomQuery(Integer proposalId, String dewarName, String comments, String barCode,
			String dewarStatus, String storageLocation, Date experimentDateStart, Date experimentDateEnd,
			String operatorSiteNumber) throws SQLException {

		String query = "";
		query += "SELECT";
		query += "  d.* ";
		query += "FROM";
		query += "  Dewar d, BLSession se, Shipping s, Proposal p ";
		query += "WHERE";
		query += "  d.shippingId = s.shippingId";
		query += " AND s.proposalId = p.proposalId";
		query += " AND d.firstExperimentId = se.sessionId";

		// proposalId
		if (proposalId != null) {
			query += " AND p.proposalId = '" + proposalId + "'";
		}
		// dewarName
		if (!StringUtils.isEmpty(dewarName)) {
			dewarName = dewarName.replace('*', '%');
			query += " AND d.code LIKE '" + dewarName + "'";
		}
		// comments
		if (!StringUtils.isEmpty(comments)) {
			comments = comments.replace('*', '%');
			query += " AND d.comments LIKE '" + comments + "'";
		}
		// barCode
		if (!StringUtils.isEmpty(barCode)) {
			query += " AND d.barCode = '" + barCode + "'";
		}

		// dewarStatus
		if (!StringUtils.isEmpty(dewarStatus)) {
			query += " AND d.dewarStatus = '" + dewarStatus + "'";
		}
		// storageLocation
		if (!StringUtils.isEmpty(storageLocation)) {
			if (storageLocation.equals(LOCATION_EMPTY)) {
				query += " AND (d.storageLocation = '' OR d.storageLocation IS NULL)";
			} else if (storageLocation.equals(NOT_AT_STORES)) {
				query += " AND d.storageLocation NOT LIKE 'STORES%'";
			} else {
				query += " AND d.storageLocation = '" + storageLocation + "'";
			}
		}

		// experiment date
		if (experimentDateStart != null) {
			if (Constants.DATABASE_IS_ORACLE()) {
				// Number of days between 01.01.1970 and creationDateStart = msecs divided by the number of msecs per
				// day
				String days = String.valueOf(experimentDateStart.getTime() / (24 * 60 * 60 * 1000));
				query += " AND se.startDate >= to_date('19700101', 'yyyymmdd') + " + days;
			} else if (Constants.DATABASE_IS_MYSQL())
				query += " AND se.startDate >= '" + experimentDateStart + "'";
			else
				LOG.error("Database type not set.");
		}
		if (experimentDateEnd != null) {
			if (Constants.DATABASE_IS_ORACLE()) {
				// Number of days between 01.01.1970 and creationDateEnd = msecs divided by the number of msecs per day
				String days = String.valueOf(experimentDateEnd.getTime() / (24 * 60 * 60 * 1000));
				query += " AND se.startDate <= to_date('19700101', 'yyyymmdd') + " + days;
			} else if (Constants.DATABASE_IS_MYSQL())
				query += " AND se.startDate <= '" + experimentDateEnd + "'";
			else
				LOG.error("Database type not set.");
		}

		// beamlineOperator
		if (operatorSiteNumber != null) {
			query += " AND se.operatorSiteNumber = '" + operatorSiteNumber + "'";
		}

		// Sort by date
		query += " ORDER BY s.creationDate DESC, d.dewarId DESC ";

		List<Dewar3VO> listVOs = this.entityManager.createNativeQuery(query, "dewarNativeQuery").getResultList();
		return listVOs;
	}

	/**
	 * 
	 * @param proposalId
	 * @param shippingId
	 * @param type
	 * @param code
	 * @param comments
	 * @param date1
	 * @param date2
	 * @param dewarStatus
	 * @param storageLocation
	 * @return
	 */
	public List<Dewar3VO> findFiltered(Integer proposalId, Integer shippingId, String type, String code,
			String comments, Date date1, Date date2, String dewarStatus, String storageLocation) {

		return this.findFiltered(proposalId, shippingId, type, code, comments, date1, date2, dewarStatus,
				storageLocation, null);
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
	private void checkAndCompleteData(Dewar3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getDewarId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getDewarId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Dewar3VO> findFiltered(Integer proposalId, Integer shippingId, String type, String code,
			String barCode, String comments, Date date1, Date date2, String dewarStatus, String storageLocation,
			Integer dewarId, Integer experimentId, boolean fetchSession, boolean fetchDewarHistory, boolean fetchContainer) {
		try {
			Session session = (Session) this.entityManager.getDelegate();
			Criteria criteria = session.createCriteria(Dewar3VO.class);

			if (dewarId != null) {
				criteria.add(Restrictions.eq("dewarId", dewarId));
			}

			if (experimentId != null) {
				Criteria  sessionCriteria = criteria.createCriteria("sessionVO");
				sessionCriteria.add(Restrictions.eq("sessionId", experimentId));
			}
			if (proposalId != null || shippingId != null || (date1 != null) || (date2 != null)) {

				Criteria shippingCriteria = criteria.createCriteria("shippingVO");
				if (proposalId != null) {

					Criteria proposalCriteria = shippingCriteria.createCriteria("proposalVO");
					proposalCriteria.add(Restrictions.eq("proposalId", proposalId));
				}

				if (shippingId != null) {
					shippingCriteria.add(Restrictions.eq("shippingId", shippingId));
				}

				if ((date1 != null) || (date2 != null)) {
					if (date1 != null)
						shippingCriteria.add(Restrictions.ge("creationDate", date1));
					if (date2 != null)
						shippingCriteria.add(Restrictions.le("creationDate", date2));
				}
			}

			if (type != null && !type.isEmpty()) {
				criteria.add(Restrictions.like("type", type));
			}

			if (code != null && !code.isEmpty()) {
				criteria.add(Restrictions.like("code", code));
			}

			if (barCode != null && !barCode.isEmpty()) {
				criteria.add(Restrictions.like("barCode", barCode));
			}

			if (comments != null && !comments.isEmpty()) {
				criteria.add(Restrictions.like("comments", comments));
			}

			if (dewarStatus != null && !dewarStatus.isEmpty()) {
				criteria.add(Restrictions.like("dewarStatus", dewarStatus));
			}

			if (storageLocation != null && !storageLocation.isEmpty()) {
				criteria.add(Restrictions.like("storageLocation", storageLocation));
			}

			if (fetchSession) {
				criteria.setFetchMode("sessionVO", FetchMode.JOIN);
				criteria.createCriteria("sessionVO");
			}

			if (fetchDewarHistory) {
				criteria.setFetchMode("dewarTransportHistoryVOs", FetchMode.JOIN);
			}

			if (fetchContainer) {
				criteria.setFetchMode("containerVOs", FetchMode.JOIN);
			}

			criteria.addOrder(Order.desc("dewarId"));

			return criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
		} catch (Exception exp) {
			exp.printStackTrace();
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public List<Dewar3VO> findByDateWithHistory(java.sql.Date firstDate) {

		Session session = (Session) this.entityManager.getDelegate();
		Criteria criteria = session.createCriteria(Dewar3VO.class);
		Criteria shippingCriteria = criteria.createCriteria("shippingVO");

		if (firstDate != null)
			shippingCriteria.add(Restrictions.ge("creationDate", firstDate));

		criteria.addOrder(Order.desc("dewarId"));
		criteria.setFetchMode("dewarTransportHistoryVOs", FetchMode.JOIN);

		List<Dewar3VO> dewars = criteria.list();
		// dewars = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

		return dewars;
	}

	@Override
	public List<Dewar3VO> findFiltered(Integer proposalId, Integer shippingId, String type, String code,
			String comments, Date date1, Date date2, String dewarStatus, String storageLocation, Integer dewarId) {
		return this.findFiltered(proposalId, shippingId, type, code, null, comments, date1, date2, dewarStatus,
				storageLocation, dewarId, null, false, false, false);

	}

	public Integer countDewarSamples(Integer dewarId) {
		Query query = entityManager.createNativeQuery(COUNT_DEWAR_SAMPLE).setParameter("dewarId", dewarId);
		try{
			BigInteger res = (BigInteger) query.getSingleResult();

			return new Integer(res.intValue());
		}catch(NoResultException e){
			System.out.println("ERROR in countDewarSamples - NoResultException: "+dewarId);
			e.printStackTrace();
			return 0;
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
}
