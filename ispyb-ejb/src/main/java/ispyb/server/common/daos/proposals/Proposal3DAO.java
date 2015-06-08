/*******************************************************************************
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
 ******************************************************************************************************************************/
package ispyb.server.common.daos.proposals;

import ispyb.server.common.vos.proposals.Proposal3VO;

import java.util.List;

import javax.ejb.Local;

/**
 * <p>
 * The data access object for Proposal objects (rows of table Proposal).
 * </p>
 */
@Local
public interface Proposal3DAO {

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 * 
	 * @throws VOValidateException
	 *             if the value object data integrity is not ok.
	 */
	public void create(Proposal3VO vo) throws Exception;

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 * 
	 * @throws VOValidateException
	 *             if the value object data integrity is not ok.
	 */
	public Proposal3VO update(Proposal3VO vo) throws Exception;

	/* Deletion methods ----------------------------------------------------- */

	/**
	 * <p>
	 * Deletes the given value object.
	 * </p>
	 * 
	 * @param vo
	 *            the value object to delete.
	 */
	public void delete(Proposal3VO vo);

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the ProposalVO instance matching the given primary key.
	 * </p>
	 * <p>
	 * <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u> if the value object has to be
	 * used out the EJB container.
	 * </p>
	 * 
	 * @param pk
	 *            the primary key of the object to load.
	 */
	public Proposal3VO findByPk(Integer pk, boolean fetchSessions, boolean fetchProteins, boolean fetchShippings);

	/**
	 * <p>
	 * Returns the ProposalVO instances.
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
	public List<Proposal3VO> findAll();

	public List<Proposal3VO> findByCodeAndNumber(String code, String number, boolean fetchSessions,
			boolean fetchProteins);

	/**
	 * Finds a Proposal entity by its code and number and title (if title is null only search by code and number).
	 * 
	 * @param code
	 * @param number
	 * @param title
	 * @return
	 */
	public List<Proposal3VO> findFiltered(String code, String number, String title);

	/**
	 * update the proposalId, returns the nb of rows updated of table BLSession, Shipping, Protein
	 * 
	 * @param newProposalId
	 * @param oldProposalId
	 * @return
	 */
	public Integer[] updateProposalId(Integer newProposalId, Integer oldProposalId);

}
