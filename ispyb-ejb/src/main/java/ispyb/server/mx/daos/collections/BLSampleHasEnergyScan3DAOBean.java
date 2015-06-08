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

package ispyb.server.mx.daos.collections;

import ispyb.server.mx.vos.collections.BLSampleHasEnergyScan3VO;

import java.util.List;

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
 * The data access object for BLSampleHasEnergyScan3 objects (rows of table BLSample_has_EnergyScan).
 * </p>
 * 
 * @see {@link BLSampleHasEnergyScan3DAO}
 */
@Stateless
public class BLSampleHasEnergyScan3DAOBean implements BLSampleHasEnergyScan3DAO {

	private final static Logger LOG = Logger.getLogger(BLSampleHasEnergyScan3DAOBean.class);

	// Generic HQL request to find instances of BLSampleHasEnergyScan3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK(boolean fetchLink1, boolean fetchLink2) {
		return "from BLSampleHasEnergyScan3VO vo " + (fetchLink1 ? "<inner|left> join fetch vo.link1 " : "")
				+ (fetchLink2 ? "<inner|left> join fetch vo.link2 " : "") + "where vo.blSampleHasEnergyScanId = :pk";
	}

	// Generic HQL request to find all instances of BLSampleHasEnergyScan3
	// TODO choose between left/inner join
	private static final String FIND_ALL(boolean fetchLink1, boolean fetchLink2) {
		return "from BLSampleHasEnergyScan3VO vo " + (fetchLink1 ? "<inner|left> join fetch vo.link1 " : "")
				+ (fetchLink2 ? "<inner|left> join fetch vo.link2 " : "");
	}

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(BLSampleHasEnergyScan3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 */
	public BLSampleHasEnergyScan3VO update(BLSampleHasEnergyScan3VO vo) throws Exception {
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
	public void delete(BLSampleHasEnergyScan3VO vo) {
		entityManager.remove(vo);
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the BLSampleHasEnergyScan3VO instance matching the given primary key.
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
	public BLSampleHasEnergyScan3VO findByPk(Integer pk, boolean fetchRelation1, boolean fetchRelation2) {
		try{
			return (BLSampleHasEnergyScan3VO) entityManager.createQuery(FIND_BY_PK(fetchRelation1, fetchRelation2))
				.setParameter("pk", pk).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

	/**
	 * <p>
	 * Returns the BLSampleHasEnergyScan3VO instances.
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
	public List<BLSampleHasEnergyScan3VO> findAll(boolean fetchRelation1, boolean fetchRelation2) {
		return entityManager.createQuery(FIND_ALL(fetchRelation1, fetchRelation2)).getResultList();
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
	private void checkAndCompleteData(BLSampleHasEnergyScan3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getBlSampleHasEnergyScanId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getBlSampleHasEnergyScanId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

	@SuppressWarnings("unchecked")
	public List<BLSampleHasEnergyScan3VO> findFiltered(Integer sampleId) {
		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(BLSampleHasEnergyScan3VO.class);
		Criteria subCrit = crit.createCriteria("blSampleVO");

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (sampleId != null) {
			subCrit.add(Restrictions.eq("blSampleId", sampleId));
		}

		return crit.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<BLSampleHasEnergyScan3VO> findByEnergyScan(Integer energyScanId ){
		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(BLSampleHasEnergyScan3VO.class);
		Criteria subCrit = crit.createCriteria("energyScanVO");

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (energyScanId != null) {
			subCrit.add(Restrictions.eq("energyScanId", energyScanId));
		}

		return crit.list();
	}
}
