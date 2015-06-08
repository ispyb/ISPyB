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
 ******************************************************************************/
package ispyb.server.mx.daos.collections;

import ispyb.server.mx.vos.collections.Session3VO;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

/**
 * <p>
 * The data access object for Session3 objects (rows of table Session).
 * </p>
 */
@Local
public interface Session3DAO {

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(Session3VO vo) throws Exception;

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 * 
	 * @throws VOValidateException
	 *             if the value object data integrity is not ok.
	 */
	public Session3VO update(Session3VO vo) throws Exception;

	/* Deletion methods ----------------------------------------------------- */

	/**
	 * <p>
	 * Deletes the given value object.
	 * </p>
	 * 
	 * @param vo
	 *            the value object to delete.
	 */
	public void delete(Session3VO vo);

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the Session3VO instance matching the given primary key.
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
	public Session3VO findByPk(Integer pk, boolean fetchDataCollectionGroup, boolean fetchEnergyScan,
			boolean fetchXFESpectrum);

	/**
	 * <p>
	 * Returns the Session3VO instances.
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
	public List<Session3VO> findAll(boolean fetchDataCollectionGroup, boolean fetchEnergyScan, boolean fetchXFESpectrum);

	public Integer updateUsedSessionsFlag(Integer proposalId) throws Exception;

	public Integer hasDataCollectionGroups(Integer sessionId) throws Exception;

	public List<Session3VO> findFiltered(Integer proposalId, Integer nbMax, String beamline, Date date1, Date date2,
			Date endDate, boolean usedFlag, Integer nbShifts, String operatorSiteNumber);

	public List<Session3VO> findByShippingId(Integer shippingId);

	/**
	 * returns the session for a specified proposal with endDate > today or null
	 * 
	 * @param code
	 * @param number
	 * @return
	 */
	public List<Session3VO> findSessionByProposalCodeAndNumber(String code, String number, String beamLineName);

	/**
	 * update the proposalId, returns the nb of rows updated
	 * 
	 * @param newProposalId
	 * @param oldProposalId
	 * @return
	 */
	public Integer updateProposalId(Integer newProposalId, Integer oldProposalId);

	/**
	 * 
	 * @param expSessionPk
	 * @return
	 */
	public List<Session3VO> findByExpSessionPk(Long expSessionPk);

	/**
	 * find the sessions to be protected
	 * 
	 * @return
	 */
	public List<Session3VO> findSessionToBeProtected(Integer delay, Integer window);

	public Session3VO findByAutoProcScalingId(Integer autoProcScalingId);

	public List<Session3VO> findSessionNotProtectedToBeProtected(Date date1, Date date2);

	public Integer getNbOfCollects(Integer sessionId) throws Exception;

	public Integer getNbOfTests(Integer sessionId) throws Exception;

}
