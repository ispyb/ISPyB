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

package ispyb.server.common.services.sessions;

import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.collections.SessionWS3VO;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

@Remote
public interface Session3Service {

	/**
	 * Create new Session3.
	 * 
	 * @param vo
	 *            the entity to persist
	 * @return the persisted entity
	 */
	public Session3VO create(final Session3VO vo) throws Exception;

	/**
	 * Update the Session3 data.
	 * 
	 * @param vo
	 *            the entity data to update
	 * @return the updated entity
	 */
	public Session3VO update(final Session3VO vo) throws Exception;

	/**
	 * Remove the Session3 from its pk.
	 * 
	 * @param vo
	 *            the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the Session3.
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Session3VO vo) throws Exception;

	/**
	 * Finds a Session3 entity by its primary key and set linked value objects if necessary.
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Scientist value object
	 */
	public Session3VO findByPk(final Integer pk, final boolean withDataCollectionGroup, final boolean withEnergyScan,
			final boolean withXFESpectrum) throws Exception;

	/**
	 * Find a session by its primary key -- webservices object
	 * 
	 * @param pk
	 * @param withLink1
	 * @param withLink2
	 * @return
	 * @throws Exception
	 */
	public SessionWS3VO findForWSByPk(final Integer pk, final boolean withDataCollectionGroup, boolean withEnergyScan,
			boolean withXFESpectrum) throws Exception;

	/**
	 * Find all Session3 and set linked value objects if necessary.
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	public List<Session3VO> findAll(final boolean withDataCollectionGroup, final boolean withEnergyScan,
			final boolean withhXFESpectrum) throws Exception;

	public Integer updateUsedSessionsFlag(final Integer proposalId) throws Exception;

	public Integer hasDataCollectionGroups(final Integer sessionId) throws Exception;

	/**
	 * 
	 * @param proposalId
	 * @param nbMax
	 * @param beamline
	 * @param date1
	 * @param date2
	 * @return
	 * @throws Exception
	 */
	public List<Session3VO> findFiltered(final Integer proposalId, final Integer nbMax, final String beamline,
			final Date date1, final Date date2, final Date endDate, final boolean usedFlag, 
			final String operatorSiteNumber) throws Exception;

	public List<Session3VO> findFiltered(final Integer nbMax, final String beamline,
			final Date date1, final Date date2, final Date endDate, final boolean usedFlag,
			final String operatorSiteNumber) throws Exception;
	
	
	public List<Session3VO> findByShippingId(final Integer shippingId) throws Exception;

	/**
	 * returns the session for a specified proposal with endDate > today or null
	 * 
	 * @param code
	 * @param number
	 * @param detachLight
	 * @return
	 * @throws Exception
	 */
	public SessionWS3VO[] findForWSByProposalCodeAndNumber(final String code, final String number,
			final String beamLineName) throws Exception;

	/**
	 * update the proposalId, returns the nb of rows updated
	 * 
	 * @param newProposalId
	 * @param oldProposalId
	 * @return
	 * @throws Exception
	 */
	public Integer updateProposalId(final Integer newProposalId, final Integer oldProposalId) throws Exception;

	public List<Session3VO> findByStartDateAndBeamLineNameAndNbShifts(final Integer proposalId,
			final Date startDateBegin, final Date startDateEnd, final String beamlineName, final Integer nbShifts)
			throws Exception;

	/**
	 * returns the session with the given expSessionPk
	 * 
	 * @param expSessionPk
	 * @return
	 */
	public Session3VO findByExpSessionPk(final Long expSessionPk) throws Exception;

	/**
	 * returns the list of sessions which have to be protected
	 * 
	 * @return
	 * @throws Exception
	 */
	public SessionWS3VO[] findForWSToBeProtected(final Integer delay, final Integer window) throws Exception;

	public SessionWS3VO[] findForWSNotProtectedToBeProtected(final Date date1, final Date date2) throws Exception;

	/**
	 * returns the session linked to the given autoProcScaling
	 * 
	 * @param autoProcScalingId
	 * @return
	 * @throws Exception
	 */
	public Session3VO findByAutoProcScalingId(final Integer autoProcScalingId) throws Exception;

	public Session3VO findByAutoProcProgramAttachmentId(final Integer autoProcProgramAttachmentId) throws Exception;
	
	public void protectSession(Integer sessionId) throws Exception;

	/**
	 * get the number of datcollections which have more then 4 images
	 * 
	 * @param sesId
	 * @return
	 * @throws Exception
	 */
	public Integer getNbOfCollects(final Integer sesId) throws Exception;

	/**
	 * get the number of datacollections which haveless 4 images
	 * 
	 * @param sesId
	 * @return
	 * @throws Exception
	 */
	public Integer getNbOfTests(final Integer sesId) throws Exception;

	public List<Session3VO> findSessionByDateProposalAndBeamline(int proposalId, String beamlineName, Date date);

	public Session3VO findByAutoProcProgramId(int autoProcProgramId);

	

}
