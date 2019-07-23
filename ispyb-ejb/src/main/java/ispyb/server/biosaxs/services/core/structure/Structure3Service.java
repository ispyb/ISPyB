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

package ispyb.server.biosaxs.services.core.structure;



import ispyb.server.biosaxs.vos.assembly.Structure3VO;

import java.util.List;

import javax.ejb.Remote;


@Remote
public interface Structure3Service {
	
	public abstract List<Structure3VO> getStructuresByDataCollectionId(Integer dataCollectionId) throws Exception;
	
	public abstract List<Structure3VO> getStructuresByProposalId(Integer proposalId) throws Exception;

	
	public List<Structure3VO> getStructuresByCrystalId(Integer crystalId) throws Exception;

	public List<Structure3VO> getLigandsByGroupName(Integer proposalId, String groupName) throws Exception;

	List<Structure3VO> getLigandsByDataCollectionId(Integer dataCollectionId) throws Exception;

}