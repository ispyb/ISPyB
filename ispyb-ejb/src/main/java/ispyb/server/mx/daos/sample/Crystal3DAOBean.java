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

import ispyb.server.mx.vos.sample.Crystal3VO;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.axis.utils.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * <p>
 * The data access object for Crystal3 objects (rows of table Crystal).
 * </p>
 * 
 * @see {@link Crystal3DAO}
 */
@Stateless
public class Crystal3DAOBean implements Crystal3DAO {

	private final static Logger LOG = Logger.getLogger(Crystal3DAOBean.class);

	// Generic HQL request to find instances of Crystal3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK(boolean fetchSamples) {
		return "from Crystal3VO vo " + (fetchSamples ? "left join fetch vo.sampleVOs " : "")
				+ "where vo.crystalId = :pk";
	}

	// Generic HQL request to find all instances of Crystal3
	// TODO choose between left/inner join
	private static final String FIND_ALL(boolean fetchLink1, boolean fetchLink2) {
		return "from Crystal3VO vo " + (fetchLink1 ? "<inner|left> join fetch vo.link1 " : "")
				+ (fetchLink2 ? "<inner|left> join fetch vo.link2 " : "");
	}

	private final static String COUNT_SAMPLE = "SELECT " + " count(DISTINCT(bls.blSampleId)) samplesNumber "
			+ "FROM Crystal c  " + "	 LEFT JOIN BLSample bls ON bls.crystalId = c.crystalId "
			+ "WHERE c.crystalId = :crystalId ";

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(Crystal3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 */
	public Crystal3VO update(Crystal3VO vo) throws Exception {
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
	public void delete(Crystal3VO vo) {
		entityManager.remove(entityManager.merge(vo));
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the Crystal3VO instance matching the given primary key.
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
	public Crystal3VO findByPk(Integer pk, boolean fetchSamples) {
		try {
			return (Crystal3VO) entityManager.createQuery(FIND_BY_PK(fetchSamples)).setParameter("pk", pk)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * <p>
	 * Returns the Crystal3VO instances.
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
	public List<Crystal3VO> findAll(boolean fetchRelation1, boolean fetchRelation2) {
		return entityManager.createQuery(FIND_ALL(fetchRelation1, fetchRelation2)).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Crystal3VO> findFiltered(Integer proposalId, Integer proteinId, String acronym, String spaceGroup) {

		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(Crystal3VO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (proteinId != null) {
			crit.add(Restrictions.eq("proteinId", proteinId));
		}

		if (acronym != null && !spaceGroup.isEmpty()) {
			crit.add(Restrictions.like("spaceGroup", spaceGroup));
		}

		Criteria subCrit = crit.createCriteria("proteinVO");

		if (acronym != null && !acronym.isEmpty()) {

			subCrit.add(Restrictions.like("acronym", acronym.toUpperCase()));
		}

		if (proposalId != null) {
			Criteria subSubCrit = subCrit.createCriteria("proposalVO");
			subSubCrit.add(Restrictions.eq("proposalId", proposalId));
		}
		subCrit.addOrder(Order.asc("acronym"));
		crit.addOrder(Order.desc("crystalId"));

		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<Crystal3VO> findByAcronymAndCellParam(Integer proposalId, String acronym, Crystal3VO currentCrystal) {

		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(Crystal3VO.class);
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		Criteria subCrit = crit.createCriteria("proteinVO");

		if (acronym != null && !acronym.isEmpty()) {

			subCrit.add(Restrictions.like("acronym", acronym.toUpperCase()));
		}

		if (proposalId != null) {
			Criteria subSubCrit = subCrit.createCriteria("proposalVO");
			subSubCrit.add(Restrictions.eq("proposalId", proposalId));
		}

		if (!StringUtils.isEmpty(currentCrystal.getSpaceGroup())) {
			crit.add(Restrictions.like("spaceGroup", currentCrystal.getSpaceGroup()));
		}
		if (currentCrystal.getCellA() != null) {
			crit.add(Restrictions.eq("cellA", currentCrystal.getCellA()));
		}
		if (currentCrystal.getCellB() != null) {
			crit.add(Restrictions.eq("cellB", currentCrystal.getCellB()));
		}
		if (currentCrystal.getCellC() != null) {
			crit.add(Restrictions.eq("cellC", currentCrystal.getCellC()));
		}
		if (currentCrystal.getCellAlpha() != null) {
			crit.add(Restrictions.eq("cellAlpha", currentCrystal.getCellAlpha()));
		}
		if (currentCrystal.getCellBeta() != null) {
			crit.add(Restrictions.eq("cellBeta", currentCrystal.getCellBeta()));
		}
		if (currentCrystal.getCellGamma() != null) {
			crit.add(Restrictions.eq("cellGamma", currentCrystal.getCellGamma()));
		}
		crit.addOrder(Order.desc("crystalId"));

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
	private void checkAndCompleteData(Crystal3VO vo, boolean create) throws Exception {

//		if (create) {
//			if (vo.getCrystalId() != null) {
//				throw new IllegalArgumentException(
//						"Primary key is already set! This must be done automatically. Please, set it to null!");
//			}
//		} else {
//			if (vo.getCrystalId() == null) {
//				throw new IllegalArgumentException("Primary key is not set for update!");
//			}
//		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

	public Integer countSamples(Integer crystalId) {
		Query query = entityManager.createNativeQuery(COUNT_SAMPLE).setParameter("crystalId", crystalId);
		try{
			BigInteger res = (BigInteger) query.getSingleResult();
			return new Integer(res.intValue());
		}catch(NoResultException e){
			System.out.println("ERROR in countSamples - NoResultException: "+crystalId);
			e.printStackTrace();
			return 0;
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
}
