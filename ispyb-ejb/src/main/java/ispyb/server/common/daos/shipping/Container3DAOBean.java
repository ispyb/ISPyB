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

import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.shipping.Container3VO;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * <p>
 * The data access object for Container3 objects (rows of table Container).
 * </p>
 * 
 * @see {@link Container3DAO}
 */
@Stateless
public class Container3DAOBean implements Container3DAO {

	// Generic HQL request to find instances of Container3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK(boolean fetchSamples) {
		return "from Container3VO vo " + (fetchSamples ? "left join fetch vo.sampleVOs " : "")
				+ "where vo.containerId = :pk";
	}

	// Generic HQL request to find all instances of Container3
	// TODO choose between left/inner join
	private static final String FIND_ALL() {
		return "from Container3VO vo ";
	}

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(Container3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 */
	public Container3VO update(Container3VO vo) throws Exception {
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
	public void delete(Container3VO vo) {
		entityManager.remove(entityManager.merge(vo));
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the Container3VO instance matching the given primary key.
	 * </p>
	 * <p>
	 * <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u> if the value object has to be
	 * used out the EJB container.
	 * </p>
	 * 
	 * @param pk
	 *            the primary key of the object to load.
	 * @param fetchSamples
	 *            if true, the linked instances by the relation "relation1" will be set.
	 */
	public Container3VO findByPk(Integer pk, boolean fetchSamples) {
		try {
			return (Container3VO) entityManager.createQuery(FIND_BY_PK(fetchSamples)).setParameter("pk", pk)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * <p>
	 * Returns the Container3VO instances.
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
	public List<Container3VO> findAll() {
		return entityManager.createQuery(FIND_ALL()).getResultList();
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
	private void checkAndCompleteData(Container3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getContainerId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getContainerId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

	@SuppressWarnings("unchecked")
	public List<Container3VO> findFiltered(Integer proposalId, Integer dewarId, String status, String code) {

		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(Container3VO.class);
		Criteria subCrit = crit.createCriteria("dewarVO");

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (dewarId != null) {
			subCrit.add(Restrictions.eq("dewarId", dewarId));
		}

		if (proposalId != null) {
			Criteria subSubCrit = subCrit.createCriteria("shippingVO");
			Criteria subSubSubCrit = subSubCrit.createCriteria("proposalVO");
			subSubSubCrit.add(Restrictions.eq("proposalId", proposalId));
		}

		if (!StringUtils.isEmpty(status)) {
			crit.add(Restrictions.like("containerStatus", status));
		}
		
		if (!StringUtils.isEmpty(code)) {
			crit.add(Restrictions.like("code", code));
		}

		crit.addOrder(Order.desc("containerId"));

		return crit.list();
	}
}
