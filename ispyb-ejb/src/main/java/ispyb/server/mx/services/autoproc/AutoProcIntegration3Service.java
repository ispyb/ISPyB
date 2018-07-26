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

package ispyb.server.mx.services.autoproc;

import java.util.List;

import ispyb.server.mx.vos.autoproc.AutoProcIntegration3VO;

import javax.ejb.Remote;

@Remote
public interface AutoProcIntegration3Service {

	/**
	 * Create new AutoProcIntegration3.
	 * @param vo the entity to persist
	 * @return the persisted entity
	 */
	public AutoProcIntegration3VO create(final AutoProcIntegration3VO vo) throws Exception;

	/**
	 * Update the AutoProcIntegration3 data.
	 * @param vo the entity data to update
	 * @return the updated entity
	 */
	public AutoProcIntegration3VO update(final AutoProcIntegration3VO vo) throws Exception;

	/**
	 * Remove the AutoProcIntegration3 from its pk.
	 * @param vo the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the AutoProcIntegration3.
	 * @param vo the entity to remove.
	 */
	public void delete(final AutoProcIntegration3VO vo) throws Exception;

	/**
	 * Finds a AutoProcIntegration3 entity by its primary key and set linked value objects if necessary.
	 * @param pk the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Scientist value object
	 */
	public AutoProcIntegration3VO findByPk(final Integer pk) throws Exception;

	/**
	 * Find all AutoProcIntegration3 and set linked value objects if necessary.
	 * @param withLink1
	 * @param withLink2
	 */
	public List<AutoProcIntegration3VO> findAll()
			throws Exception;

	/**
	 * 
	 * @param autoProcId
	 * @return
	 * @throws Exception
	 */
	public List<AutoProcIntegration3VO> findByAutoProcId(final Integer autoProcId) throws Exception;
	
	/**
	 * get the autoProcStatus for a given dataCollectionId and a given processingProgram (fastProc or parallelProc or edna-fastproc)
	 * true if we can find at least one autoProc with XSCALE file
	 * @param dataCollectionId
	 * @param processingProgram
	 * @return
	 * @throws Exception
	 */
	public Boolean getAutoProcStatus(final Integer dataCollectionId, final String processingProgram) throws Exception;

	/**
	 * get the autoProcStatus for XIA2_DIALS
	 * @param dataCollectionId
	 * @param processingProgram
	 * @return
	 * @throws Exception
	 */
	public Boolean getAutoProcXia2DialsStatus(final Integer dataCollectionId, final String processingProgram) throws Exception;

	/**
	 * get the autoProcStatus for Fast DP
	 * @param dataCollectionId
	 * @param processingProgram
	 * @return
	 * @throws Exception
	 */
	public Boolean getAutoProcFastDPStatus(final Integer dataCollectionId, final String processingProgram) throws Exception;
}