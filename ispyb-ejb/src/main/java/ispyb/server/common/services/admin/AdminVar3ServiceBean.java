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
package ispyb.server.common.services.admin;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.vos.admin.AdminVar3VO;

/**
 * <p>
 * This session bean handles ISPyB AdminVar3.
 * </p>
 */
@Stateless
public class AdminVar3ServiceBean implements AdminVar3Service, AdminVar3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(AdminVar3ServiceBean.class);

	// Generic HQL request to find instances of AdminVar3 by pk
	private static final String FIND_BY_PK() {
		return "from AdminVar3VO vo where vo.adminVarId = :pk";
	}

	// Generic HQL request to find all instances of AdminVar3
	private static final String FIND_ALL() {
		return "from AdminVar3VO vo ";
	}

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public AdminVar3ServiceBean() {
	};

	/**
	 * Create new AdminVar3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public AdminVar3VO create(final AdminVar3VO vo) throws Exception {
		
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}
	
	/**
	 * Update the AdminVar3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public AdminVar3VO update(final AdminVar3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();	
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the AdminVar3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		
		checkCreateChangeRemoveAccess();
		AdminVar3VO vo = findByPk(pk);
		delete(vo);	
	}

	/**
	 * Remove the AdminVar3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final AdminVar3VO vo) throws Exception {
		
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
	 * @return the AdminVar3 value object
	 */
	public AdminVar3VO findByPk(final Integer pk) throws Exception {
		
		checkCreateChangeRemoveAccess();
		try {
			return (AdminVar3VO) entityManager.createQuery(FIND_BY_PK()).setParameter("pk", pk).getSingleResult();
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
	public AdminVar3VO loadEager(AdminVar3VO vo) throws Exception {
		
		AdminVar3VO newVO = this.findByPk(vo.getAdminVarId());
		return newVO;
	}

	/**
	 * Find all AdminVar3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<AdminVar3VO> findAll() throws Exception {
		
		List<AdminVar3VO> foundEntities = entityManager.createQuery(FIND_ALL()).getResultList();
		return foundEntities;
	}

	@SuppressWarnings("unchecked")
	public List<AdminVar3VO> findByName(final String name) throws Exception {
		
		Session session = (Session) this.entityManager.getDelegate();
		Criteria crit = session.createCriteria(AdminVar3VO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (name != null && !name.isEmpty()) {
			String n = name.toLowerCase();
			crit.add(Restrictions.like("name", n));
		}
		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<AdminVar3VO> findByAction(final String statusLogon) throws Exception {
		
		Session session = (Session) this.entityManager.getDelegate();
		Criteria crit = session.createCriteria(AdminVar3VO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (statusLogon != null && !statusLogon.isEmpty()) {
			String v = statusLogon.toLowerCase();
			crit.add(Restrictions.like("value", v));
		}
		return crit.list();
	}

	/**
	 * Check if user has access rights to create, change and remove AdminVar3 entities. If not set rollback only and
	 * throw AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
		
				// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
				// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
				// to the one checking the needed access rights
				// autService.checkUserRightToChangeAdminData();
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
	private void checkAndCompleteData(AdminVar3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getAdminVarId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getAdminVarId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}