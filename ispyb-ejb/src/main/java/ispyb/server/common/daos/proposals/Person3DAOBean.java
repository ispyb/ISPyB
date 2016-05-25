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
import ispyb.server.common.vos.proposals.Person3VO;

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
 * The data access object for Person3 objects (rows of table Person).
 * </p>
 * 
 * @see {@link Person3DAO}
 */
@Stateless
public class Person3DAOBean implements Person3DAO {

	private final static Logger LOG = Logger.getLogger(Person3DAOBean.class);

	// Generic HQL request to find instances of Person3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK(boolean withProposals) {
		return "from Person3VO vo " + (withProposals ? "left join fetch vo.proposalVOs " : "")
				+ "where vo.personId = :pk";
	}

	// Generic HQL request to find all instances of Person3
	// TODO choose between left/inner join
	private static final String FIND_ALL(boolean withProposals, boolean fetchLink2) {
		return "from Person3VO vo " + (withProposals ? "left join fetch vo.proposalVOs " : "")
				+ (fetchLink2 ? "<inner|left> join fetch vo.link2 " : "");
	}

	private static String SELECT_PERSON = "SELECT p.personId, p.laboratoryId, p.personUUID, "
			+ "p.familyName, p.givenName, p.title, p.emailAddress, p.phoneNumber, p.login, p.faxNumber ";

	private static String FIND_BY_SESSION = SELECT_PERSON + " FROM Person p, Proposal pro, BLSession ses "
			+ "WHERE p.personId = pro.personId AND pro.proposalId = ses.proposalId AND ses.sessionId = :sessionId ";

	private static String FIND_BY_PROPOSAL_CODE_NUMBER = SELECT_PERSON + " FROM Person p, Proposal pro "
			+ "WHERE p.personId = pro.personId AND pro.proposalCode like :code AND pro.proposalNumber = :number ";
	
	private static String FIND_BY_LOGIN = SELECT_PERSON + " FROM Person p "
			+ "WHERE p.login = :login ";

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(Person3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 */
	public Person3VO update(Person3VO vo) throws Exception {
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
	public void delete(Person3VO vo) {
		entityManager.remove(vo);
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the Person3VO instance matching the given primary key.
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
	public Person3VO findByPk(Integer pk, boolean withProposals) {
		try {
			return (Person3VO) entityManager.createQuery(FIND_BY_PK(withProposals)).setParameter("pk", pk)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * <p>
	 * Returns the Person3VO instances.
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
	public List<Person3VO> findAll(boolean fetchRelation1, boolean fetchRelation2) {
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
	private void checkAndCompleteData(Person3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getPersonId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getPersonId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

	/**
	 * find a Person with a sessionId
	 */
	@SuppressWarnings("unchecked")
	public Person3VO findPersonBySessionId(Integer sessionId) {
		String query = FIND_BY_SESSION;
		List<Person3VO> listVOs = this.entityManager.createNativeQuery(query, "personNativeQuery")
				.setParameter("sessionId", sessionId).getResultList();
		if (listVOs == null || listVOs.isEmpty())
			return null;
		return (Person3VO) listVOs.toArray()[0];
	}

	@SuppressWarnings("unchecked")
	public Person3VO findPersonByProposalCodeAndNumber(String code, String number) {
		String query = FIND_BY_PROPOSAL_CODE_NUMBER;
		List<Person3VO> listVOs = this.entityManager.createNativeQuery(query, "personNativeQuery")
				.setParameter("code", code).setParameter("number", number).getResultList();
		if (listVOs == null || listVOs.isEmpty())
			return null;
		return (Person3VO) listVOs.toArray()[0];
	}

	@SuppressWarnings("unchecked")
	public List<Person3VO> findFiltered(String familyName, String givenName, String login) {
		Session session = (Session) this.entityManager.getDelegate();

		Criteria criteria = session.createCriteria(Person3VO.class);

		if (givenName != null) {
			criteria.add(Restrictions.like("givenName", givenName));
		}

		if (familyName != null) {
			criteria.add(Restrictions.like("familyName", familyName));
		}

		if (!StringUtils.isEmpty(login)) {
			criteria.add(Restrictions.like("login", login));
		}

		return criteria.list();

	}

	@SuppressWarnings("unchecked")
	public Person3VO findPersonByLastNameAndFirstNameLetter(String lastName, String firstNameLetter) {
		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(Person3VO.class);
		if (lastName != null) {
			crit.add(Restrictions.like("familyName", lastName));
		}
		if (firstNameLetter != null) {
			firstNameLetter = firstNameLetter.replace('*', '%');
			crit.add(Restrictions.like("givenName", firstNameLetter));
		}
		List<Person3VO> list = crit.list();
		if (list == null || list.isEmpty())
			return null;
		return list.get(0);
	}

	@Override
	public Person3VO findPersonByLogin(String login) {
		String query = FIND_BY_LOGIN;
		return (Person3VO) this.entityManager.createNativeQuery(query, "personNativeQuery")
				.setParameter("login", login).getSingleResult();
	}
}
