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
package ispyb.server.common.services.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.vos.config.MenuGroup3VO;

/**
 * <p>
 *  This session bean handles ISPyB MenuGroup3.
 * </p>
 */
@Stateless
public class MenuGroup3ServiceBean implements MenuGroup3Service,
		MenuGroup3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(MenuGroup3ServiceBean.class);
	
	// Generic HQL request to find instances of MenuGroup3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK() {
		return "from MenuGroup3VO vo "  + "where vo.menuGroupId = :pk";
	}

	// Generic HQL request to find all instances of MenuGroup3
	// TODO choose between left/inner join
	private static final String FIND_ALL() {
		return "from MenuGroup3VO vo " ;
	}

	@PersistenceContext(unitName = "ispyb_config")
	private EntityManager entityManager;

	@PersistenceUnit(unitName = "ispyb_config")
	private EntityManagerFactory entitymanagerFactory;

	public MenuGroup3ServiceBean() {
	};

	/**
	 * Create new MenuGroup3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public MenuGroup3VO create(final MenuGroup3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the MenuGroup3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public MenuGroup3VO update(final MenuGroup3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the MenuGroup3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		
		checkCreateChangeRemoveAccess();
		MenuGroup3VO vo = findByPk(pk);			
		delete(vo);
	}

	/**
	 * Remove the MenuGroup3
	 * @param vo the entity to remove.
	 */
	public void delete(final MenuGroup3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the MenuGroup3 value object
	 */
	public MenuGroup3VO findByPk(final Integer pk) throws Exception {
	
		checkCreateChangeRemoveAccess();
		try {
			entityManager = entitymanagerFactory.createEntityManager();
			return (MenuGroup3VO) entityManager.createQuery(FIND_BY_PK())
					.setParameter("pk", pk).getSingleResult();
		}catch(NoResultException e){
			return null;
		} finally {
			entityManager.close();
		}
	}

	/**
	 * loads the vo with all the linked object in eager fetch mode
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public MenuGroup3VO loadEager(MenuGroup3VO vo) throws Exception {
		MenuGroup3VO newVO = this.findByPk(vo.getMenuGroupId());
		return newVO;
	}

	// TODO remove following method if not adequate
	/**
	 * Find all MenuGroup3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<MenuGroup3VO> findAll(final boolean detachLight) throws Exception {
		
		try {
			entityManager = entitymanagerFactory.createEntityManager();
			Collection<MenuGroup3VO> foundEntities = entityManager.createQuery(FIND_ALL()).getResultList();
			List<MenuGroup3VO> vos;
			if (detachLight)
				vos = getLightMenuGroup3VOs(foundEntities);
			else
				vos = getMenuGroup3VOs(foundEntities);
			return vos;
		} finally {
			entityManager.close();
		}
	}

	/**
	 * Check if user has access rights to create, change and remove MenuGroup3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
		
				//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
				//autService.checkUserRightToChangeAdminData();
	}

	/**
	 * Get all MenuGroup3 entity VOs from a collection of MenuGroup3 local entities.
	 * @param localEntities
	 * @return
	 */
	private List<MenuGroup3VO> getMenuGroup3VOs(Collection<MenuGroup3VO> entities) {
		List<MenuGroup3VO> results = new ArrayList<MenuGroup3VO>(entities.size());
		for (MenuGroup3VO vo : entities) {
			results.add(vo);
		}
		return results;
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private List<MenuGroup3VO> getLightMenuGroup3VOs(Collection<MenuGroup3VO> entities)
			throws CloneNotSupportedException {
		List<MenuGroup3VO> results = new ArrayList<MenuGroup3VO>(entities.size());
		for (MenuGroup3VO vo : entities) {
			MenuGroup3VO otherVO = getLightMenuGroup3VO(vo);
			results.add(otherVO);
		}
		return results;
	}

	/**
	 * Get a clone of an entity witout linked collections
	 * used for webservices
	 * 
	 * @param localEntity
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private MenuGroup3VO getLightMenuGroup3VO(MenuGroup3VO vo)
			throws CloneNotSupportedException {
		MenuGroup3VO otherVO = vo.clone();
		//otherVO.setXxxxxxx(null);
		//otherVO.setYyyyyyy(null);
		return otherVO;
	}
	
	@SuppressWarnings("unchecked")
	public List<MenuGroup3VO> findFiltered(final String name)throws Exception{
		
		entityManager = entitymanagerFactory.createEntityManager();
		try {
			Session session = (Session) entityManager.getDelegate();
			Criteria crit = session.createCriteria(MenuGroup3VO.class);

			crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

			if (name != null) {
				crit.add(Restrictions.ilike("name", name));
			}

			List<MenuGroup3VO> foundEntities = crit.list();
			List<MenuGroup3VO> vos = getMenuGroup3VOs(foundEntities);
			return vos;
		} finally {
			entityManager.close();
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
	private void checkAndCompleteData(MenuGroup3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getMenuGroupId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getMenuGroupId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}