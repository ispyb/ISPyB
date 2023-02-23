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
package ispyb.server.common.services.proposals;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.proposals.PersonWS3VO;
	

/**
 * <p>
 * This session bean handles ISPyB Person3.
 * </p>
 */
@Stateless
public class Person3ServiceBean implements Person3Service, Person3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Person3ServiceBean.class);
	

	// Generic HQL request to find instances of Person3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK() {
		return "from Person3VO vo  where vo.personId = :pk";
	}

	private static final String FIND_BY_SITE_ID() {
		return "from Person3VO vo where vo.siteId = :siteId order by vo.personId desc";
	}

	private static String SELECT_PERSON = "SELECT p.personId, p.laboratoryId, p.siteId, p.personUUID, "
			+ "p.familyName, p.givenName, p.title, p.emailAddress, p.phoneNumber, p.login, p.faxNumber, p.externalId ";

	private static String FIND_BY_SESSION = SELECT_PERSON + " FROM Person p, Proposal pro, BLSession ses "
			+ "WHERE p.personId = pro.personId AND pro.proposalId = ses.proposalId AND ses.sessionId = :sessionId ";

	private static String FIND_BY_PROPOSAL_CODE_NUMBER = SELECT_PERSON + " FROM Person p, Proposal pro "
			+ "WHERE p.personId = pro.personId AND pro.proposalCode like :code AND pro.proposalNumber = :number ";
	
	private static String FIND_BY_LOGIN = SELECT_PERSON + " FROM Person p "
			+ "WHERE p.login = :login ORDER BY p.personId DESC";
	
	private static String FIND_BY_PROTEIN = SELECT_PERSON + " FROM Person p, Protein prot "
			+ "WHERE p.personId = prot.personId AND prot.proposalId = :proposalId AND prot.acronym = :acronym ";

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;
	
	public Person3ServiceBean() {
	};


	@Override
	public Person3VO merge(Person3VO detachedInstance) {
		try {
			Person3VO result = entityManager.merge(detachedInstance);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	/**
	 * Create new Person3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public Person3VO create(final Person3VO vo)  {
		
		return this.merge(vo);
	}


	/**
	 * Remove the Person3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		
		Person3VO vo = findByPk(pk);
		checkCreateChangeRemoveAccess();
		delete(vo);
	}

	/**
	 * Remove the Person3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Person3VO vo) throws Exception {
		entityManager.remove(vo);
	}

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
	 */
	public Person3VO findByPk(Integer pk) {
		try {
			return (Person3VO) entityManager.createQuery(FIND_BY_PK()).setParameter("pk", pk)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
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
	public Person3VO findPersonByProteinAcronym(Integer proposalId, String acronym) {
		String query = FIND_BY_PROTEIN;
		List<Person3VO> listVOs = this.entityManager.createNativeQuery(query, "personNativeQuery")
				.setParameter("proposalId", proposalId).setParameter("acronym", acronym).getResultList();
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
		criteria.addOrder(Order.desc("personId"));

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
	
	@SuppressWarnings("unchecked")	
	public Person3VO findBySiteId(String siteId) {
			List<Person3VO> listVOs = entityManager.createQuery(FIND_BY_SITE_ID()).setParameter("siteId", siteId)
					.getResultList();
			if (listVOs == null || listVOs.isEmpty())
						return null;
			return (Person3VO) listVOs.toArray()[0];
	}

	@Override
	public Person3VO findByLogin(String login) {
		String query = FIND_BY_LOGIN;
		try {
			List<Person3VO> listVOs =  this.entityManager.createNativeQuery(query, "personNativeQuery")
				.setParameter("login", login).getResultList();
			if (listVOs == null || listVOs.isEmpty())
				return null;
			return (Person3VO) listVOs.toArray()[0];
		} catch (NoResultException e) {
			return null;
		}
	}
	

	/**
	 * Find a Person for a specified sessionId
	 * 
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public PersonWS3VO findForWSPersonBySessionId(final Integer sessionId) throws Exception {
		Person3VO foundEntities = findPersonBySessionId(sessionId);
				PersonWS3VO personWS = getWSPersonVO(foundEntities);
				return personWS;
	}
	
	/**
	 * Find a Person for a specified protein acronym
	 * 
	 * @param proposalId
	 * @param acronym
	 * @return
	 * @throws Exception
	 */
	public PersonWS3VO findForWSPersonByProteinAcronym(final Integer proposalId, final String acronym) throws Exception {
		Person3VO foundEntities = findPersonByProteinAcronym(proposalId, acronym);
				PersonWS3VO personWS = getWSPersonVO(foundEntities);
				return personWS;
	}
	
	/**
	 * Find a Person for a specified code and proposal number
	 * 
	 * @param code
	 * @param number
	 * @param detachLight
	 * @return
	 * @throws Exception
	 */
	public PersonWS3VO findForWSPersonByProposalCodeAndNumber(final String code, final String number) throws Exception {
		Person3VO foundEntities = findPersonByProposalCodeAndNumber(code, number);
				PersonWS3VO personWS = getWSPersonVO(foundEntities);
				return personWS;
	}

	/**
	 * Find a Person for a specified code and proposal number
	 * 
	 * @param code
	 * @param number
	 * @param detachLight
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Person3VO> findByFamilyAndGivenName(final String familyName, final String givenName) throws Exception {
		return findFiltered(familyName, givenName, null);
	}
	
	/**
	 * Find a Person for a specified code and proposal number
	 * 
	 * @param code
	 * @param number
	 * @param detachLight
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PersonWS3VO> findPersonWS3VOByLogin(final String login) throws Exception {
		List<Person3VO> foundEntities = findFiltered(null, null, login);
		List<PersonWS3VO> foundEntitiesWS = new ArrayList<PersonWS3VO>();
		for (Person3VO person : foundEntities){
					PersonWS3VO personWS = getWSPersonVO(person);
					foundEntitiesWS.add(personWS);
		}
		return foundEntitiesWS;
	}
	
	public PersonWS3VO findForWSPersonByLastNameAndFirstNameLetter(final String lastName, final String firstNameLetter) throws Exception{
		Person3VO foundEntities = findPersonByLastNameAndFirstNameLetter(lastName, firstNameLetter);
				PersonWS3VO personWS = getWSPersonVO(foundEntities);
				return personWS;
	}
	
	public PersonWS3VO findForWSPersonByLogin(final String login) throws Exception {
		Person3VO foundEntities = findByLogin(login);
				PersonWS3VO personWS = getWSPersonVO(foundEntities);
				return personWS;
	}
	
	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private Person3VO getLightPersonVO(Person3VO vo) throws CloneNotSupportedException {
		if (vo == null)
			return null;
		Person3VO otherVO = (Person3VO) vo.clone();
		otherVO.setProposalVOs(null);
		return otherVO;
	}
	
	private PersonWS3VO getWSPersonVO(Person3VO vo) throws CloneNotSupportedException {
		if(vo == null)
			return null;
		Person3VO otherVO = getLightPersonVO(vo);
		Integer laboratoryId = null;
		laboratoryId = otherVO.getLaboratoryVOId();
		otherVO.setLaboratoryVO(null);
		PersonWS3VO wsPerson = new PersonWS3VO(otherVO);
		wsPerson.setLaboratoryId(laboratoryId);
		return wsPerson;
	}


	
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
	 * Check if user has access rights to create, change and remove Person3 entities. If not set rollback only and throw
	 * AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
		
				// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
				// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
				// to the one checking the needed access rights
				// autService.checkUserRightToChangeAdminData();
	}


}