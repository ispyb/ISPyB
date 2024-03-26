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

import java.math.BigInteger;
import java.util.ArrayList;
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

import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.vos.shipping.Dewar3VO;

/**
 * <p>
 * This session bean handles ISPyB Dewar3.
 * </p>
 */
@Stateless
public class Dewar3ServiceBean implements Dewar3Service, Dewar3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Dewar3ServiceBean.class);

	public static final String NOT_AT_STORES = "!STORES%";

	public static final String LOCATION_EMPTY = "EMPTY"; // to encode URL parameters values

	// Generic HQL request to find instances of Dewar3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK(boolean fetchContainers, boolean fetchDewarTransportHitory) {
		return "from Dewar3VO vo " + (fetchContainers ? "left join fetch vo.containerVOs " : "")
				+ (fetchDewarTransportHitory ? "left join fetch vo.dewarTransportHistoryVOs " : "")
				+ "where vo.dewarId = :pk";
	}
	
	private static final String FIND_BY_PK(boolean fetchContainers, boolean fetchDewarTransportHitory, boolean fetchSample) {
		return "from Dewar3VO vo " + (fetchContainers ? "left join fetch vo.containerVOs co" : "")
				+ (fetchDewarTransportHitory ? "left join fetch vo.dewarTransportHistoryVOs " : "")
				+ (fetchSample ? "left join fetch co.sampleVOs " : "")
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

	public Dewar3ServiceBean() {
	};

	/**
	 * Create new Dewar3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public Dewar3VO create(final Dewar3VO vo) throws Exception {
				
		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);

		// generate and add the bar code to the vo
		if (Constants.SITE_IS_ESRF()) {
			vo.initBarcode();
			this.update(vo);
		}
		//IK TODO
		else if (Constants.SITE_IS_EMBL()) {
			String barCode = "EMBL";
			if (vo.getDewarId() < 1000000)
				barCode = barCode + "0";
			barCode = barCode + vo.getDewarId().toString();
			vo.setBarCode(barCode);
			this.update(vo);
		}
		else if (Constants.SITE_IS_MAXIV()) {
			String barCode = "MAXIV";
			if (vo.getDewarId() < 1000000)
				barCode = barCode + "0";
			barCode = barCode + vo.getDewarId().toString();
			vo.setBarCode(barCode);
			this.update(vo);
		}
		else if (Constants.SITE_IS_SOLEIL()) {
			String barCode = Constants.SITE_NAME;
			if (vo.getDewarId() < 1000000)
				barCode = barCode + "0";
			barCode = barCode + vo.getDewarId().toString();
			vo.setBarCode(barCode);
			this.update(vo);
		}
		else  {
			String barCode = Constants.SITE_NAME;
			if (vo.getDewarId() < 1000000)
				barCode = barCode + "0";
			barCode = barCode + vo.getDewarId().toString();
			vo.setBarCode(barCode);
			this.update(vo);
		}

		return vo;
	}

	/**
	 * Update the Dewar3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public Dewar3VO update(final Dewar3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the Dewar3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		
		checkCreateChangeRemoveAccess();
		Dewar3VO vo = findByPk(pk, false, false);
		delete(vo);
	}

	/**
	 * Remove the Dewar3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Dewar3VO vo) throws Exception {
		
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
	 * @return the Dewar3 value object
	 */
	public Dewar3VO findByPk(final Integer pk, final boolean withContainers, final boolean withDewarTransportHistory) throws Exception {
		
		checkCreateChangeRemoveAccess();
		try {
			return (Dewar3VO) entityManager.createQuery(FIND_BY_PK(withContainers, withDewarTransportHistory)).setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Dewar3VO findByPk(final Integer pk, final boolean withContainers, final boolean withDewarTransportHistory, final boolean withSamples)
			throws Exception {
		
		checkCreateChangeRemoveAccess();
		try {
			return (Dewar3VO) entityManager.createQuery(FIND_BY_PK(withContainers, withDewarTransportHistory, withSamples))
					.setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Find all Dewar3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<Dewar3VO> findAll(final boolean withContainers, final boolean withDewarTransportHistory) throws Exception {
		
		List<Dewar3VO> foundEntities = entityManager.createQuery(FIND_ALL(withContainers, withDewarTransportHistory)).getResultList();
		return foundEntities;
	}

	public List<Dewar3VO> findFiltered(final Integer proposalId, final Integer shippingId, final String type,
			final String code, final String comments, final Date date1, final Date date2, final String dewarStatus,
			final String storageLocation, final boolean withDewarHistory, final boolean withContainer) throws Exception {
		
		return this.findFiltered(proposalId, shippingId, type, code, comments, date1, date2, dewarStatus,
				storageLocation, null, withDewarHistory, withContainer);
	}

	public List<Dewar3VO> findFiltered(final Integer proposalId, final Integer shippingId, final String type,
		   final String code, final String barCode, final String comments, final Date date1, final Date date2, final String dewarStatus,
		   final String storageLocation, final boolean withDewarHistory, final boolean withContainer) throws Exception {

		return this.findFiltered(proposalId, shippingId, type, code, barCode, comments, date1, date2, dewarStatus,
				storageLocation, null, null, false, withDewarHistory, withContainer);
	}

	@Deprecated
	@SuppressWarnings("unchecked")
	public List<Dewar3VO> findByCustomQuery(final Integer proposalId, final String dewarName, final String comments,
			final String barCode, final String dewarStatus, final String storageLocation,
			final Date experimentDateStart, final Date experimentDateEnd, final String operatorSiteNumber)
			throws Exception {
		
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
			String d = dewarName.replace('*', '%');
			query += " AND d.code LIKE '" + d + "'";
		}
		// comments
		if (!StringUtils.isEmpty(comments)) {
			String com = comments.replace('*', '%');
			query += " AND d.comments LIKE '" + com + "'";
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

	// findFiltered(Integer proposalId, Integer shippingId, String type, String code,
	// String comments, Date date1, Date date2, String dewarStatus, String storageLocation)
	@Override
	public List<Dewar3VO> findByProposalId(final Integer proposalId) throws Exception {
		return this.findFiltered(proposalId, null, null, null, null, null, null, null, null, false, false);
	}

	@Override
	public List<Dewar3VO> findByShippingId(int shippindId) throws Exception {
		return this.findFiltered(null, shippindId, null, null, null, null, null, null, null, false, false);
	}

	@Override
	public List<Dewar3VO> findByType(String type) throws Exception {
		return this.findFiltered(null, null, type, null, null, null, null, null, null, false, false);
	}

	@Override
	public List<Dewar3VO> findByCode(String code) throws Exception {
		return this.findFiltered(null, null, null, code, null, null, null, null, null, false, false);
	}

	@Override
	public List<Dewar3VO> findByComments(String comments) throws Exception {
		return this.findFiltered(null, null, null, null, comments, null, null, null, null, false, false);
	}

	@Override
	public List<Dewar3VO> findByExperimentDate(Date experimentDateStart) throws Exception {
		return this.findFiltered(null, null, null, null, null, experimentDateStart, experimentDateStart, null, null,
				false, false);
	}

	@Override
	public List<Dewar3VO> findByExperimentDate(Date experimentDateStart, Date experimentDateEnd) throws Exception {
		return this.findFiltered(null, null, null, null, null, experimentDateStart, experimentDateEnd, null, null,
				false, false);
	}

	@Override
	public List<Dewar3VO> findByStatus(String status) throws Exception {
		return this.findFiltered(null, null, null, null, null, null, null, status, null, false, false);
	}

	@Override
	public List<Dewar3VO> findByStorageLocation(String storageLocation) throws Exception {
		return this.findFiltered(null, null, null, null, null, null, null, null, storageLocation, false, false);
	}

	/**
	 * Check if user has access rights to create, change and remove Dewar3 entities. If not set rollback only and throw
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

	/**
	 * Get all Dewar3 entity VOs from a collection of Dewar3 local entities.
	 * 
	 * @param localEntities
	 * @return
	 */
	@SuppressWarnings({ "unused" })
	private Dewar3VO[] getDewar3VOs(List<Dewar3VO> entities) {
		ArrayList<Dewar3VO> results = new ArrayList<Dewar3VO>(entities.size());
		for (Dewar3VO vo : entities) {
			results.add(vo);
		}
		Dewar3VO[] tmpResults = new Dewar3VO[results.size()];
		return results.toArray(tmpResults);
	}

	@Override
	public List<Dewar3VO> findFiltered(final Integer proposalId, final Integer shippingId, final String type,
			final String code, final String comments, final Date date1, final Date date2, final String dewarStatus,
			final String storageLocation, final Integer dewarId, final boolean withDewarHistory,
			final boolean withContainer) throws Exception {
		return this.findFiltered(proposalId, shippingId, type, code, null, comments, date1, date2, dewarStatus,
				storageLocation, dewarId, null, false, withDewarHistory, withContainer);
	}

	/*@Override
	public List<Dewar3VO> findFiltered(final Integer proposalId, final Integer shippingId, final String type,
		   final String code, final String barCode, final String comments, final Date date1, final Date date2, final String dewarStatus,
		   final String storageLocation, final Integer dewarId, final boolean withDewarHistory,
		   final boolean withContainer) throws Exception {
		return this.findFiltered(proposalId, shippingId, type, code, barCode, comments, date1, date2, dewarStatus,
				storageLocation, dewarId, null, false, withDewarHistory, withContainer);
	}*/

	@SuppressWarnings("unchecked")
	@Override
	public List<Dewar3VO> findFiltered(final Integer proposalId, final Integer shippingId, final String type,
			final String code, final String barCode, final String comments, final Date date1, final Date date2,
			final String dewarStatus, final String storageLocation, final Integer dewarId, final Integer firstExperimentId, final boolean fetchSession,
			final boolean withDewarHistory, final boolean withContainer) throws Exception {

		try {
			Session session = (Session) this.entityManager.getDelegate();
			Criteria criteria = session.createCriteria(Dewar3VO.class);

			if (dewarId != null) {
				criteria.add(Restrictions.eq("dewarId", dewarId));
			}

			if (firstExperimentId != null) {
				Criteria  sessionCriteria = criteria.createCriteria("sessionVO");
				sessionCriteria.add(Restrictions.eq("sessionId", firstExperimentId));
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

			if (withDewarHistory) {
				criteria.setFetchMode("dewarTransportHistoryVOs", FetchMode.JOIN);
			}

			if (withContainer) {
				criteria.setFetchMode("containerVOs", FetchMode.JOIN);
			}

			criteria.addOrder(Order.desc("dewarId"));

			return criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
		} catch (Exception exp) {
			exp.printStackTrace();
			return null;
		}

	}

	public Dewar3VO loadEager(Dewar3VO vo) throws Exception {
		Dewar3VO newVO = this.findByPk(vo.getDewarId(), true, true);
		return newVO;
	}

	@Override
	public List<Dewar3VO> findByBarCode(String barCode) throws Exception {
		return this.findFiltered(null, null, null, null, barCode, null, null, null, null, null, null,null,  false, false,
				false);
	}
	
	public List<Dewar3VO> findByExperiment(final Integer experimentId, final String dewarStatus) throws Exception{
		return this.findFiltered(null, null, null, null, null, null, null, null, dewarStatus, null, null, experimentId, false, false, false);
	}

	public List<Dewar3VO> findByDateWithHistory(final java.sql.Date firstDate) throws Exception {
		
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

	public Integer countDewarSamples(final Integer dewarId) throws Exception {
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
	
}