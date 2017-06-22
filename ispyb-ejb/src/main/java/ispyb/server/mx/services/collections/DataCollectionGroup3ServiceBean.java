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
package ispyb.server.mx.services.collections;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import ispyb.server.common.exceptions.AccessDeniedException;

import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroupWS3VO;

/**
 * <p>
 * This session bean handles ISPyB DataCollectionGroup3.
 * </p>
 */
@Stateless
public class DataCollectionGroup3ServiceBean implements DataCollectionGroup3Service, DataCollectionGroup3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(DataCollectionGroup3ServiceBean.class);
	
	// Generic HQL request to find instances of DataCollectionGroup3 by pk
	private static final String FIND_BY_PK(boolean fetchDataCollection, boolean fetchScreening) {
		return "from DataCollectionGroup3VO vo " + (fetchDataCollection ? "left join fetch vo.dataCollectionVOs " : "")
				+ (fetchScreening ? "left join fetch vo.screeningVOs " : "")
				+ "where vo.dataCollectionGroupId = :pk";
	}

	// Generic HQL request to find all instances of DataCollectionGroup3
	private static final String FIND_ALL(boolean fetchDataCollection) {
		return "from DataCollectionGroup3VO vo " + (fetchDataCollection ? "left join fetch vo.dataCollectionVOs " : "");
	}

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@Resource
	private SessionContext context;

	public DataCollectionGroup3ServiceBean() {
	};

	/**
	 * Create new DataCollectionGroup3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public DataCollectionGroup3VO create(final DataCollectionGroup3VO vo) throws Exception {
	
		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}


	/**
	 * Update the DataCollectionGroup3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public DataCollectionGroup3VO update(final DataCollectionGroup3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the DataCollectionGroup3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
	
		checkCreateChangeRemoveAccess();
		DataCollectionGroup3VO vo = findByPk(pk, false, false);
		// TODO Edit this business code
		delete(vo);
	}

	/**
	 * Remove the DataCollectionGroup3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final DataCollectionGroup3VO vo) throws Exception {
	
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
	 * @return the DataCollectionGroup3 value object
	 */
	public DataCollectionGroup3VO findByPk(final Integer pk, final boolean withDataCollection, final boolean withScreening) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		try {
			return (DataCollectionGroup3VO) entityManager.createQuery(FIND_BY_PK(withDataCollection, withScreening))
					.setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Find a dataCollectionGroup by its primary key -- webservices object
	 * 
	 * @param pk
	 * @param withLink1
	 * @param withLink2
	 * @return
	 * @throws Exception
	 */
	public DataCollectionGroupWS3VO findForWSByPk(final Integer pk, final boolean withDataCollection, final boolean withScreening) throws Exception {
	
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		try {
			DataCollectionGroup3VO found = (DataCollectionGroup3VO) entityManager.createQuery(FIND_BY_PK(withDataCollection, withScreening))
					.setParameter("pk", pk).getSingleResult();
			DataCollectionGroupWS3VO sesLight = getWSDataCollectionGroupVO(found);
			return sesLight;
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private DataCollectionGroup3VO getLightDataCollectionGroup3VO(DataCollectionGroup3VO vo)
			throws CloneNotSupportedException {
		DataCollectionGroup3VO otherVO = (DataCollectionGroup3VO) vo.clone();
		otherVO.setDataCollectionVOs(null);
		otherVO.setScreeningVOs(null);
		return otherVO;
	}

	private DataCollectionGroupWS3VO getWSDataCollectionGroupVO(DataCollectionGroup3VO vo)
			throws CloneNotSupportedException {
		DataCollectionGroup3VO otherVO = getLightDataCollectionGroup3VO(vo);
		Integer sessionId = null;
		Integer blSampleId = null;
		sessionId = otherVO.getSessionVOId();
		blSampleId = otherVO.getBlSampleVOId();
		otherVO.setSessionVO(null);
		otherVO.setBlSampleVO(null);
		DataCollectionGroupWS3VO wsDataCollectionGroup = new DataCollectionGroupWS3VO(otherVO);
		wsDataCollectionGroup.setSessionId(sessionId);
		wsDataCollectionGroup.setBlSampleId(blSampleId);
		return wsDataCollectionGroup;
	}

	// TODO remove following method if not adequate
	/**
	 * Find all DataCollectionGroup3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollectionGroup3VO> findAll(final boolean withDataCollection) throws Exception {

		List<DataCollectionGroup3VO> foundEntities = entityManager.createQuery(FIND_ALL(withDataCollection)).getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove DataCollectionGroup3 entities. If not set rollback
	 * only and throw AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {

				// AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator
				// .getInstance().getService(
				// AuthorizationServiceLocalHome.class); // TODO change method to the one checking the needed access
				// rights
				// autService.checkUserRightToChangeAdminData();
	}


	/**
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public DataCollectionGroup3VO loadEager(DataCollectionGroup3VO vo) throws Exception {
		DataCollectionGroup3VO newVO = this.findByPk(vo.getDataCollectionGroupId(), true, true);
		return newVO;
	}

	@SuppressWarnings("unchecked")
	public List<DataCollectionGroup3VO> findFiltered(final Integer sessionId, final boolean withDataCollection, final boolean withScreenings)
			throws Exception {

		List<DataCollectionGroup3VO> resultList;

		Session session = (Session) this.entityManager.getDelegate();
		Criteria crit = session.createCriteria(DataCollectionGroup3VO.class);
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !
		if (sessionId != null) {
			Criteria subCritSession = crit.createCriteria("sessionVO");
			subCritSession.add(Restrictions.eq("sessionId", sessionId));
		}
		if (withDataCollection) {
			crit.setFetchMode("dataCollectionVOs", FetchMode.JOIN);
		}
		if (withScreenings) {
			crit.setFetchMode("screeningVOs", FetchMode.JOIN);
		}

		crit.addOrder(Order.desc("startTime"));

		resultList = crit.list();
		// TODO understand why crit.setMaxResults does not work ???

		List<DataCollectionGroup3VO> foundEntities = crit.list();
		return foundEntities;
	}

	/**
	 * finds groups for a given workflow
	 * 
	 * @param workflowId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollectionGroup3VO> findByWorkflow(final Integer workflowId) throws Exception {

		Session session = (Session) this.entityManager.getDelegate();
		Criteria crit = session.createCriteria(DataCollectionGroup3VO.class);
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (workflowId != null) {
			Criteria subCritWorkflow = crit.createCriteria("workflowVO");
			subCritWorkflow.add(Restrictions.eq("workflowId", workflowId));
		}

		crit.addOrder(Order.desc("startTime"));

		// TODO understand why crit.setMaxResults does not work ???

		List<DataCollectionGroup3VO> foundEntities = crit.list();
		return foundEntities;
	}


	/**
	 * find groups for a given blSampleId
	 * 
	 * @param sampleId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollectionGroup3VO> findBySampleId(final Integer sampleId, final boolean withDataCollection, final boolean withScreenings)
			throws Exception {

		Session session = (Session) this.entityManager.getDelegate();
		Criteria crit = session.createCriteria(DataCollectionGroup3VO.class);
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !
	
		if (withDataCollection) {
			crit.setFetchMode("dataCollectionVOs", FetchMode.JOIN);
		}
		if (withScreenings) {
			crit.setFetchMode("screeningVOs", FetchMode.JOIN);
		}

		if (sampleId != null) {
			Criteria subCritSample = crit.createCriteria("blSampleVO");
			subCritSample.add(Restrictions.eq("blSampleId", sampleId));
		}
		crit.addOrder(Order.desc("startTime"));

		// TODO understand why crit.setMaxResults does not work ???

		List<DataCollectionGroup3VO> foundEntities = crit.list();
		return foundEntities;
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
	private void checkAndCompleteData(DataCollectionGroup3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getDataCollectionGroupId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getDataCollectionGroupId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}