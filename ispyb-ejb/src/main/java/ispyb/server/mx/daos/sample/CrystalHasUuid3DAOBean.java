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

import ispyb.server.mx.vos.sample.CrystalHasUuid3VO;

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
 * The data access object for CrystalHasUuid3 objects (rows of table TableName).
 * </p>
 * 
 * @see {@link CrystalHasUuid3DAO}
 */
@Stateless
public class CrystalHasUuid3DAOBean implements CrystalHasUuid3DAO {

	private final static Logger LOG = Logger.getLogger(CrystalHasUuid3DAOBean.class);

	// Generic HQL request to find instances of CrystalHasUuid3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK(boolean fetchLink1, boolean fetchLink2) {
		return "from CrystalHasUuid3VO vo " + (fetchLink1 ? "<inner|left> join fetch vo.link1 " : "")
				+ (fetchLink2 ? "<inner|left> join fetch vo.link2 " : "") + "where vo.crystalHasUuidId = :pk";
	}

	// Generic HQL request to find all instances of CrystalHasUuid3
	// TODO choose between left/inner join
	private static final String FIND_ALL(boolean fetchLink1, boolean fetchLink2) {
		return "from CrystalHasUuid3VO vo " + (fetchLink1 ? "<inner|left> join fetch vo.link1 " : "")
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
	public void create(CrystalHasUuid3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 */
	public CrystalHasUuid3VO update(CrystalHasUuid3VO vo) throws Exception {
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
	public void delete(CrystalHasUuid3VO vo) {
		entityManager.remove(vo);
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the CrystalHasUuid3VO instance matching the given primary key.
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
	public CrystalHasUuid3VO findByPk(Integer pk, boolean fetchRelation1, boolean fetchRelation2) {
		try{
		return (CrystalHasUuid3VO) entityManager.createQuery(FIND_BY_PK(fetchRelation1, fetchRelation2))
				.setParameter("pk", pk).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

	/**
	 * <p>
	 * Returns the CrystalHasUuid3VO instances.
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
	public List<CrystalHasUuid3VO> findAll(boolean fetchRelation1, boolean fetchRelation2) {
		return entityManager.createQuery(FIND_ALL(fetchRelation1, fetchRelation2)).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrystalHasUuid3VO> findFiltered(Integer crystalId, String crystalUUID) {
		Session session = (Session) this.entityManager.getDelegate();

		Criteria criteria = session.createCriteria(CrystalHasUuid3VO.class);

		if (crystalId != null) {
			criteria.add(Restrictions.eq("crystalId", crystalId));

		}
		if (crystalUUID != null) {
			criteria.add(Restrictions.eq("uuid", crystalUUID));
		}
		return criteria.list();
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
	private void checkAndCompleteData(CrystalHasUuid3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getCrystalHasUuidId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getCrystalHasUuidId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

}
