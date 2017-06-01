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

import ispyb.server.mx.vos.collections.EnergyScan3VO;

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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * <p>
 *  This session bean handles ISPyB EnergyScan3.
 * </p>
 */
@Stateless
public class EnergyScan3ServiceBean implements EnergyScan3Service,
		EnergyScan3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(EnergyScan3ServiceBean.class);


	// Generic HQL request to find instances of EnergyScan3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK() {
		return "from EnergyScan3VO vo " + "where vo.energyScanId = :pk";
	}

	// Generic HQL request to find all instances of EnergyScan3
	// TODO choose between left/inner join
	private static final String FIND_ALL() {
		return "from EnergyScan3VO vo ";
	}

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@Resource
	private SessionContext context;

	public EnergyScan3ServiceBean() {
	};

	/**
	 * Create new EnergyScan3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public EnergyScan3VO create(final EnergyScan3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the EnergyScan3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public EnergyScan3VO update(final EnergyScan3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the EnergyScan3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		
		checkCreateChangeRemoveAccess();
		EnergyScan3VO vo = findByPk(pk);
		// TODO Edit this business code				
		delete(vo);
	}

	/**
	 * Remove the EnergyScan3
	 * @param vo the entity to remove.
	 */
	public void delete(final EnergyScan3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the EnergyScan3 value object
	 */
	public EnergyScan3VO findByPk(final Integer pk) throws Exception {
	
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		try{
			return (EnergyScan3VO) entityManager.createQuery(FIND_BY_PK()).setParameter("pk", pk).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}


	// TODO remove following method if not adequate
	/**
	 * Find all EnergyScan3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<EnergyScan3VO> findAll() throws Exception {

		List<EnergyScan3VO> foundEntities = entityManager.createQuery(FIND_ALL()).getResultList();
		return foundEntities;
	}


	/**
	 * Check if user has access rights to create, change and remove EnergyScan3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {

//				AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator
//						.getInstance().getService(
//								AuthorizationServiceLocalHome.class); // TODO change method to the one checking the needed access rights
//				autService.checkUserRightToChangeAdminData();

	}

	
	
	@SuppressWarnings("unchecked")
	public List<EnergyScan3VO> findFiltered(final Integer sessionId , final Integer sampleId) throws Exception{
	
		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(EnergyScan3VO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (sessionId != null) {
			Criteria subCritSess = crit.createCriteria("sessionVO");
			subCritSess.add(Restrictions.eq("sessionId", sessionId));
		}
		
		if (sampleId != null) {
			Criteria subCritSample = crit.createCriteria("blSampleVO");
			subCritSample.add(Restrictions.eq("blSampleId", sampleId));
		}

		crit.addOrder(Order.desc("energyScanId"));

		List<EnergyScan3VO> foundEntities = crit.list();
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
	private void checkAndCompleteData(EnergyScan3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getEnergyScanId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getEnergyScanId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}