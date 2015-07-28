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

package ispyb.server.biosaxs.services.core.analysis;

import ispyb.server.biosaxs.vos.datacollection.SaxsDataCollection3VO;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;


@Remote
public interface Analysis3Service {

	public List<Map<String,Object>> getAllByMacromolecule(int macromoleculeId, int proposalId);
	
	public List<Map<String,Object>> getAllAnalysisInformation();

	public List<Map<String, Object>> getAllByMacromolecule(int macromoleculeId, int bufferId, int proposalId);
	
	public List<Map<String, Object>> getExperimentListByProposalId(int proposalId);

	public List<Map<String, Object>> getExperimentListByProposalId(int proposalId, String experimentType);

	public List<Map<String, Object>> getCompactAnalysisByExperimentId(int experimentId);

	public List<Map<String, Object>> getCompactAnalysisByProposalId(Integer proposalId, Integer limit);

	public List<Map<String, Object>> getCompactAnalysisBySubtractionId(String subtractionId);

	public List<Map<String, Object>> getCompactAnalysisByProposalId(Integer proposalId, Integer start, Integer limit);

	public BigInteger getCountCompactAnalysisByExperimentId(Integer proposalId);

	public List<Map<String, Object>> getCompactAnalysisByMacromoleculeId(Integer proposalId, Integer macromoleculeId);

	public List<Map<String, Object>> getExperimentListByExperimentId(
			Integer proposalId, Integer experimentId);

	public List<Map<String, Object>> getExperimentListBySessionId(Integer proposalId,
			Integer sessionId);

	public List<SaxsDataCollection3VO> getDataCollections(
			List<Integer> dataCollectionIdList);

	SaxsDataCollection3VO getDataCollection(int dataCollectionId);



}