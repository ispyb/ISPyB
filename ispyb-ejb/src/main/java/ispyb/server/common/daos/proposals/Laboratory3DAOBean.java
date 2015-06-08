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

package ispyb.server.common.daos.proposals;

import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.proposals.Laboratory3VO;

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
 * The data access object for Laboratory3 objects (rows of table Laboratory).
 * </p>
 * 
 * @see {@link Laboratory3DAO}
 */
@Stateless
public class Laboratory3DAOBean implements Laboratory3DAO {

	private final static Logger LOG = Logger.getLogger(Laboratory3DAOBean.class);

	private static String SELECT_LABORATORY = "SELECT l.laboratoryId, l.laboratoryUUID, l.name, l.address, "
			+ "l.city, l.country, l.url, l.organization  ";

	private static String FIND_BY_PROPOSAL_CODE_NUMBER = SELECT_LABORATORY
			+ " FROM Laboratory l, Person p, Proposal pro "
			+ "WHERE l.laboratoryId = p.laboratoryId AND p.personId = pro.personId AND pro.proposalCode like :code AND pro.proposalNumber = :number ";

	// Generic HQL request to find instances of Laboratory3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK(boolean fetchLink1, boolean fetchLink2) {
		return "from Laboratory3VO vo " + (fetchLink1 ? "<inner|left> join fetch vo.link1 " : "")
				+ (fetchLink2 ? "<inner|left> join fetch vo.link2 " : "") + "where vo.laboratoryId = :pk";
	}

	// Generic HQL request to find all instances of Laboratory3
	// TODO choose between left/inner join
	private static final String FIND_ALL(boolean fetchLink1, boolean fetchLink2) {
		return "from Laboratory3VO vo " + (fetchLink1 ? "<inner|left> join fetch vo.link1 " : "")
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
	public void create(Laboratory3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 */
	public Laboratory3VO update(Laboratory3VO vo) throws Exception {
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
	public void delete(Laboratory3VO vo) {
		entityManager.remove(vo);
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the Laboratory3VO instance matching the given primary key.
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
	public Laboratory3VO findByPk(Integer pk, boolean fetchRelation1, boolean fetchRelation2) {
		try {
			return (Laboratory3VO) entityManager.createQuery(FIND_BY_PK(fetchRelation1, fetchRelation2))
					.setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * <p>
	 * Returns the Laboratory3VO instances.
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
	public List<Laboratory3VO> findAll(boolean fetchRelation1, boolean fetchRelation2) {
		return entityManager.createQuery(FIND_ALL(fetchRelation1, fetchRelation2)).getResultList();
	}

	@SuppressWarnings("unchecked")
	public Laboratory3VO findLaboratoryByProposalCodeAndNumber(String code, String number) {
		String query = FIND_BY_PROPOSAL_CODE_NUMBER;
		List<Laboratory3VO> listVOs = this.entityManager.createNativeQuery(query, "laboratoryNativeQuery")
				.setParameter("code", code).setParameter("number", number).getResultList();
		if (listVOs == null || listVOs.isEmpty())
			return null;
		return (Laboratory3VO) listVOs.toArray()[0];
	}

	@SuppressWarnings("unchecked")
	public List<Laboratory3VO> findFiltered(String laboratoryName, String city, String country) {

		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(Laboratory3VO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (!StringUtils.isEmpty(laboratoryName))
			crit.add(Restrictions.like("name", laboratoryName));
		if (!StringUtils.isEmpty(city))
			crit.add(Restrictions.like("city", city));
		if (!StringUtils.isEmpty(country))
			crit.add(Restrictions.like("country", country));

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
	private void checkAndCompleteData(Laboratory3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getLaboratoryId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getLaboratoryId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

}
