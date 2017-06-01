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

package ispyb.server.common.daos.config;

import ispyb.server.common.vos.config.Menu3VO;

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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * <p>
 * The data access object for Menu3 objects (rows of table Menu).
 * </p>
 * 
 * @see {@link Menu3DAO}
 */
@Stateless
public class Menu3DAOBean implements Menu3DAO {

	private final static Logger LOG = Logger.getLogger(Menu3DAOBean.class);

	// Generic HQL request to find instances of Menu3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK(boolean fetchMenuGroup) {
		return "from Menu3VO vo " + (fetchMenuGroup ? "left join fetch vo.menuGroupVOs " : "") + "where vo.menuId = :pk";
	}

	// Generic HQL request to find all instances of Menu3
	// TODO choose between left/inner join
	private static final String FIND_ALL(boolean fetchMenuGroup) {
		return "from Menu3VO vo " + (fetchMenuGroup ? "left join fetch vo.menuGroupVOs " : "");
	}

	@PersistenceContext(unitName = "ispyb_config")
	private EntityManager entityManager;

	@PersistenceUnit(unitName = "ispyb_config")
	private EntityManagerFactory entitymanagerFactory;

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(Menu3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 */
	public Menu3VO update(Menu3VO vo) throws Exception {
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
	public void delete(Menu3VO vo) {
		entityManager.remove(vo);
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the Menu3VO instance matching the given primary key.
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
	public Menu3VO findByPk(Integer pk, boolean fetchMenuGroup) {
		try {
			entityManager = entitymanagerFactory.createEntityManager();
			return (Menu3VO) entityManager.createQuery(FIND_BY_PK(fetchMenuGroup)).setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
			entityManager.close();
		}
	}

	/**
	 * <p>
	 * Returns the Menu3VO instances.
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
	public List<Menu3VO> findAll(boolean fetchMenuGroup) {
		entityManager = entitymanagerFactory.createEntityManager();
		try {
			return entityManager.createQuery(FIND_ALL(fetchMenuGroup)).getResultList();
		} finally {
			entityManager.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Menu3VO> findFiltered(Integer parentId, Integer menuGroupId, String proposalType) {
		entityManager = entitymanagerFactory.createEntityManager();
		try {
			Session session = (Session) entityManager.getDelegate();

			Criteria criteriaMenu = session.createCriteria(Menu3VO.class);
			Criteria criteriaMenuGroup = criteriaMenu.createCriteria("menuGroupVOs");

			criteriaMenu.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

			if (proposalType != null) {
				criteriaMenu.add(Restrictions.or(Restrictions.eq("expType", "MB"), Restrictions.eq("expType", proposalType)));
			}

			if (menuGroupId != null) {
				criteriaMenuGroup.add(Restrictions.eq("menuGroupId", menuGroupId));
			}

			if (parentId != null) {
				criteriaMenu.add(Restrictions.eq("parentId", parentId));
			}
			criteriaMenu.addOrder(Order.asc("parentId"));
			criteriaMenu.addOrder(Order.asc("sequence"));

			return criteriaMenu.list();
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
	 *            should be true if the value object is just being created in the DB, this avoids some checks like testing the primary
	 *            key
	 * @exception VOValidateException
	 *                if data is not correct
	 */
	private void checkAndCompleteData(Menu3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getMenuId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getMenuId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
}
