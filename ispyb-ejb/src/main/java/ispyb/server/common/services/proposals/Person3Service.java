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

import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.proposals.PersonWS3VO;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface Person3Service {
		
	public abstract Person3VO merge(Person3VO detachedInstance);

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
	 * Find a Person for a specified sessionId
	 * 
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public PersonWS3VO findForWSPersonBySessionId(final Integer sessionId) throws Exception;
	
	public Person3VO findByPk(Integer pk) ;
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

	public Person3VO findByLogin(final String personLogin) throws Exception;
	
	public Person3VO findBySiteId(final String siteId) throws Exception;
	
	public Person3VO findPersonByProteinAcronym(Integer proposalId, String acronym) throws Exception;
	
	public PersonWS3VO findForWSPersonByLastNameAndFirstNameLetter(final String lastName, final String firstNameLetter) throws Exception;

	public PersonWS3VO findForWSPersonByLogin(String lowerCase) throws Exception;
	
	/**
	 * Find a Person for a specified protein acronym
	 * 
	 * @param proposalId
	 * @param acronym
	 * @return
	 * @throws Exception
	 */
	public PersonWS3VO findForWSPersonByProteinAcronym(final Integer proposalId, final String acronym) throws Exception;

	

}