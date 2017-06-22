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

import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;

import ispyb.server.mx.vos.collections.Detector3VO;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * <p>
 *  This session bean handles ISPyB Detector3.
 * </p>
 */
@Stateless
public class Detector3ServiceBean implements Detector3Service,
		Detector3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(Detector3ServiceBean.class);

	// Generic HQL request to find instances of Detector3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK() {
		return "from Detector3VO vo " + "where vo.detectorId = :pk";
	}

	// Generic HQL request to find all instances of Detector3
	// TODO choose between left/inner join
	private static final String FIND_ALL() {
		return "from Detector3VO vo ";
	}

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@Resource
	private SessionContext context;

	public Detector3ServiceBean() {
	};

	/**
	 * Create new Detector3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public Detector3VO create(final Detector3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the Detector3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public Detector3VO update(final Detector3VO vo) throws Exception {
	
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the Detector3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		Detector3VO vo = findByPk(pk);
		// TODO Edit this business code				
		delete(vo);
	}

	/**
	 * Remove the Detector3
	 * @param vo the entity to remove.
	 */
	public void delete(final Detector3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Detector3 value object
	 */
	public Detector3VO findByPk(final Integer pk) throws Exception {
	
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		try {
			return (Detector3VO) entityManager.createQuery(FIND_BY_PK()).setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * loads the vo with all the linked object in eager fetch mode
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public Detector3VO loadEager(Detector3VO vo) throws Exception {
		Detector3VO newVO = this.findByPk(vo.getDetectorId());
		return newVO;
	}

	// TODO remove following method if not adequate
	/**
	 * Find all Detector3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<Detector3VO> findAll()
			throws Exception {

		List<Detector3VO> foundEntities = entityManager.createQuery(FIND_ALL()).getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove Detector3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
	}

	/**
	 * Find a dataCollectionGroup by its primary key -- webservices object
	 * @param pk
	 * @param withLink1
	 * @param withLink2
	 * @return
	 * @throws Exception
	 */
	public Detector3VO findForWSByPk(final Integer pk) throws Exception{
		
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		try {
			return (Detector3VO) entityManager.createQuery(FIND_BY_PK()).setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * returns the detector with the given characteristics -- null otherwise
	 * @param detectorType
	 * @param detectorManufacturer
	 * @param detectorModel
	 * @param detectorPixelSizeHorizontal
	 * @param detectorPixelSizeVertical
	 * @return
	 * @throws Exception
	 */
	public Detector3VO findByCharacteristics(final String detectorType, final String detectorManufacturer, 
			final String detectorModel, final Double detectorPixelSizeHorizontal, 
			final Double detectorPixelSizeVertical) throws Exception {
	
		checkCreateChangeRemoveAccess();
		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(Detector3VO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (detectorType != null) {
			crit.add(Restrictions.like("detectorType", detectorType));
		}

		if (detectorManufacturer != null) {
			crit.add(Restrictions.like("detectorManufacturer", detectorManufacturer));
		}

		if (detectorModel != null) {
			crit.add(Restrictions.like("detectorModel", detectorModel));
		}

		/* problem with comparison of floats for equality */
		if (detectorPixelSizeHorizontal != null)
			crit.add(Restrictions.like("detectorPixelSizeHorizontal", detectorPixelSizeHorizontal));

		if (detectorPixelSizeVertical != null)
			crit.add(Restrictions.like("detectorPixelSizeVertical", detectorPixelSizeVertical));

		List<Detector3VO> vos = crit.list();
		if (vos == null || vos.size() == 0)
			return null;
		return vos.get(0);
	}

	/**
	 * returns the detector with the given characteristics -- null otherwise
	 * @param detectorType
	 * @param detectorManufacturer
	 * @param detectorModel
	 * @param detectorMode
	 * @return
	 * @throws Exception
	 */
	public Detector3VO findDetector(final String detectorType, final String detectorManufacturer, 
			final String detectorModel, final String detectorMode) throws Exception{
	
		checkCreateChangeRemoveAccess();
		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(Detector3VO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (detectorType != null) {
			crit.add(Restrictions.like("detectorType", detectorType));
		}

		if (detectorManufacturer != null) {
			crit.add(Restrictions.like("detectorManufacturer", detectorManufacturer));
		}

		if (detectorModel != null) {
			crit.add(Restrictions.like("detectorModel", detectorModel));
		}

		if (detectorMode != null && !detectorMode.equals("")) {
			crit.add(Restrictions.like("detectorMode", detectorMode));
		}

		List<Detector3VO> vos = crit.list();
		if (vos == null || vos.size() == 0)
			return null;
		return vos.get(0);
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
	private void checkAndCompleteData(Detector3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getDetectorId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getDetectorId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
}