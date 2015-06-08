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

import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.proposals.PersonWS3VO;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface Person3Service {

	/**
	 * Create new Person3.
	 * 
	 * @param vo
	 *            the entity to persist
	 * @return the persisted entity
	 */
	public Person3VO create(final Person3VO vo) throws Exception;

	/**
	 * Update the Person3 data.
	 * 
	 * @param vo
	 *            the entity data to update
	 * @return the updated entity
	 */
	public Person3VO update(final Person3VO vo) throws Exception;

	/**
	 * Remove the Person3 from its pk.
	 * 
	 * @param vo
	 *            the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the Person3.
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Person3VO vo) throws Exception;

	/**
	 * Finds a Person3 entity by its primary key and set linked value objects if necessary.
	 * 
	 * @param pk
	 *            the primary key
	 * @return the value object
	 */
	public Person3VO findByPk(final Integer pk, final boolean withProposals) throws Exception;

	/**
	 * Find all Person3 and set linked value objects if necessary.
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	public List<Person3VO> findAll(final boolean withLink1, final boolean withLink2) throws Exception;

	/**
	 * Find a Person for a specified sessionId
	 * 
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public PersonWS3VO findForWSPersonBySessionId(final Integer sessionId) throws Exception;
	
	

	/**
	 * Find a Person for a specified code and proposal number
	 * 
	 * @param code
	 * @param number
	 * @param detachLight
	 * @return
	 * @throws Exception
	 */
	public PersonWS3VO findForWSPersonByProposalCodeAndNumber(final String code, final String number) throws Exception;

	public List<Person3VO> findByFamilyAndGivenName(final String familyName, final String givenName) throws Exception;

	public List<Person3VO> findByLogin(final String personLogin) throws Exception;
	
	public PersonWS3VO findForWSPersonByLastNameAndFirstNameLetter(final String lastName, final String firstNameLetter) throws Exception;

	public PersonWS3VO findForWSPersonByLogin(String lowerCase) throws Exception;
	

}