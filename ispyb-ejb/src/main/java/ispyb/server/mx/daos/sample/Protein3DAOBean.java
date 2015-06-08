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

package ispyb.server.mx.daos.sample;

import ispyb.server.mx.vos.sample.Protein3VO;

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
 * The data access object for Protein3 objects (rows of table Protein).
 * </p>
 * 
 * @see {@link Protein3DAO}
 */
@Stateless
public class Protein3DAOBean implements Protein3DAO {

	private final static Logger LOG = Logger.getLogger(Protein3DAOBean.class);

	// Generic HQL request to find instances of Protein3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK(boolean fetchCrystals) {
		return "from Protein3VO vo " + (fetchCrystals ? " left join fetch vo.crystalVOs " : "")
				+ " where vo.proteinId = :pk";
	}

	// Generic HQL request to find all instances of Protein3
	// TODO choose between left/inner join
	private static final String FIND_ALL(boolean fetchLink1) {
		return "from Protein3VO vo " + (fetchLink1 ? "left join fetch vo.crystalVOs " : "");
	}

	private final static String UPDATE_PROPOSALID_STATEMENT = " update Protein  set proposalId = :newProposalId "
			+ " WHERE proposalId = :oldProposalId"; // 2 old value to be replaced

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(Protein3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 */
	public Protein3VO update(Protein3VO vo) throws Exception {
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
	public void delete(Protein3VO vo) {
		entityManager.remove(vo);
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the Protein3VO instance matching the given primary key.
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
	public Protein3VO findByPk(Integer pk, boolean fetchRelation1) {
		try {
			Query query = entityManager.createQuery(FIND_BY_PK(fetchRelation1)).setParameter("pk", pk);
			return (Protein3VO) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * <p>
	 * Returns the Protein3VO instances.
	 * </p>
	 * <p>
	 * <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u> if the value object has to be
	 * used out the EJB container.
	 * </p>
	 * 
	 * @param fetchRelation1
	 *            if true, the linked instances by the relation "relation1" will be set.
	 */
	@SuppressWarnings("unchecked")
	public List<Protein3VO> findAll(boolean fetchRelation1) {
		return entityManager.createQuery(FIND_ALL(fetchRelation1)).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Protein3VO> findFiltered(Integer proposalId, String acronym, boolean fetchCrystal, boolean sortByAcronym) {

		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(Protein3VO.class);
		Criteria subCrit = crit.createCriteria("proposalVO");

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (proposalId != null) {
			subCrit.add(Restrictions.eq("proposalId", proposalId));
		}

		if (acronym != null && !acronym.isEmpty()) {
			crit.add(Restrictions.like("acronym", acronym.toUpperCase()));
		}

		if (fetchCrystal) {
			crit.setFetchMode("crystalVOs", FetchMode.JOIN);
		}
		if (sortByAcronym) {
			crit.addOrder(Order.asc("acronym"));
		} else
			crit.addOrder(Order.desc("proteinId"));

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
	private void checkAndCompleteData(Protein3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getProteinId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getProteinId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
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
}
