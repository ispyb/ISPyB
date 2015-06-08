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

import ispyb.server.common.vos.proposals.Person3VO;

import java.util.List;

import javax.ejb.Local;

/**
 * <p>
 * The data access object for Person3 objects (rows of table Person).
 * </p>
 */
@Local
public interface Person3DAO {

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(Person3VO vo) throws Exception;

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 * 
	 * @throws VOValidateException
	 *             if the value object data integrity is not ok.
	 */
	public Person3VO update(Person3VO vo) throws Exception;

	/* Deletion methods ----------------------------------------------------- */

	/**
	 * <p>
	 * Deletes the given value object.
	 * </p>
	 * 
	 * @param vo
	 *            the value object to delete.
	 */
	public void delete(Person3VO vo);

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
	 */
	public Person3VO findByPk(Integer pk, boolean withProposals);

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
	public List<Person3VO> findAll(boolean withProposals, boolean fetchRelation2);

	/**
	 * find a person with a given sessionId
	 * 
	 * @param sessionId
	 * @param detachLight
	 * @return
	 */
	public Person3VO findPersonBySessionId(Integer sessionId);
	
	

	/**
	 * find a person for a proposal with a given code and number
	 * 
	 * @param code
	 * @param number
	 * @return
	 */
	public Person3VO findPersonByProposalCodeAndNumber(String code, String number);

	/**
	 * find a person for a proposal with a familyName and givenName and a login
	 * 
	 * @param familyName
	 * @param givenName
	 * @param login
	 * @return
	 */
	public List<Person3VO> findFiltered(String familyName, String givenName, String login);
	
	public Person3VO findPersonByLastNameAndFirstNameLetter(String lastName, String firstNameLetter);

	public Person3VO findPersonByLogin(String login);
}
