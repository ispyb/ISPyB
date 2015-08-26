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

import ispyb.server.common.daos.proposals.Person3DAO;
import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.proposals.PersonWS3VO;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;

/**
 * <p>
 * This session bean handles ISPyB Person3.
 * </p>
 */
@Stateless
public class Person3ServiceBean implements Person3Service, Person3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Person3ServiceBean.class);

	@EJB
	private Person3DAO dao;

	@Resource
	private SessionContext context;

	public Person3ServiceBean() {
	};

	/**
	 * Create new Person3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public Person3VO create(final Person3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Person3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the Person3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public Person3VO update(final Person3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Person3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the Person3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				Person3VO vo = findByPk(pk, false);
				// TODO Edit this business code
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the Person3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Person3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.delete(vo);
				return vo;
			}

		});
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Person3 value object
	 */
	@WebMethod
	public Person3VO findByPk(final Integer pk, final boolean withProposals) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Person3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				Person3VO found = dao.findByPk(pk, withProposals);
				return found;
			}

		});
	}

	// TODO remove following method if not adequate
	/**
	 * Find all Person3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<Person3VO> findAll(final boolean withLink1, final boolean withLink2) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Person3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Person3VO> foundEntities = dao.findAll(withLink1, withLink2);
				return foundEntities;
			}

		});
	}

	/**
	 * Check if user has access rights to create, change and remove Person3 entities. If not set rollback only and throw
	 * AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
				// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
				// to the one checking the needed access rights
				// autService.checkUserRightToChangeAdminData();
				return null;
			}

		});
	}

	/**
	 * Find a Person for a specified sessionId
	 * 
	 * @param sessionId
	 * @param detachLight
	 * @return
	 * @throws Exception
	 */
	public PersonWS3VO findForWSPersonBySessionId(final Integer sessionId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				Person3VO foundEntities = dao.findPersonBySessionId(sessionId);
				PersonWS3VO personWS = getWSPersonVO(foundEntities);
				return personWS;
			};
		};
		PersonWS3VO ret = (PersonWS3VO) template.execute(callBack);

		return ret;
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
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				Person3VO foundEntities = dao.findPersonByProposalCodeAndNumber(code, number);
				PersonWS3VO personWS = getWSPersonVO(foundEntities);
				return personWS;
			};
		};
		PersonWS3VO ret = (PersonWS3VO) template.execute(callBack);

		return ret;
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
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Person3VO>) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				List<Person3VO> foundEntities = dao.findFiltered(familyName, givenName, null);
				return foundEntities;
			}

		});

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
	public List<Person3VO> findByLogin(final String login) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Person3VO>) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				List<Person3VO> foundEntities = dao.findFiltered(null, null, login);
				return foundEntities;
			}

		});

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
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<PersonWS3VO>) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				List<Person3VO> foundEntities = dao.findFiltered(null, null, login);
				List<PersonWS3VO> foundEntitiesWS = new ArrayList<PersonWS3VO>();
				for (Person3VO person : foundEntities){
					PersonWS3VO personWS = getWSPersonVO(person);
					foundEntitiesWS.add(personWS);
				}
				return foundEntitiesWS;
			}

		});

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
	
	public PersonWS3VO findForWSPersonByLastNameAndFirstNameLetter(final String lastName, final String firstNameLetter) throws Exception{
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				Person3VO foundEntities = dao.findPersonByLastNameAndFirstNameLetter(lastName, firstNameLetter);
				PersonWS3VO personWS = getWSPersonVO(foundEntities);
				return personWS;
			};
		};
		PersonWS3VO ret = (PersonWS3VO) template.execute(callBack);

		return ret;
	}
	
	public PersonWS3VO findForWSPersonByLogin(final String login) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				Person3VO foundEntities = dao.findPersonByLogin(login);
				PersonWS3VO personWS = getWSPersonVO(foundEntities);
				return personWS;
			};
		};
		PersonWS3VO ret = (PersonWS3VO) template.execute(callBack);

		return ret;
	}

}