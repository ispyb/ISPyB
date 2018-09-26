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

package ispyb.server.biosaxs.services.core.proposal;

import ispyb.server.biosaxs.vos.assembly.Assembly3VO;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.StockSolution3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Additive3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

@Remote
public interface SaxsProposal3Service {

	/** MACROMOLECULES **/
	public abstract Macromolecule3VO merge(Macromolecule3VO macromolecule3vo);

	public abstract List<Macromolecule3VO> findMacromoleculesByProposalId(int proposalId);

	public List<Macromolecule3VO> findMacromoleculesBy(String acronym, int proposalId);

	/** BUFFER **/
	public abstract List<Buffer3VO> findBuffersByProposalId(int proposalId);

	//public abstract List<Additive3VO> findAdditivesByProposalId(int proposalId);
	
	public abstract List<Additive3VO> findAdditivesByBufferId(int proposalId);

	public abstract Buffer3VO merge(Buffer3VO buffer3VO);

	public abstract Additive3VO merge(Additive3VO additive3VO);

	/** ASSEMBLY **/
	public abstract void createAssembly(int assemblyId, List<Integer> macromolecules);

	public abstract List<Assembly3VO> findAssembliesByProposalId(Integer proposalId);

	public abstract List<Assembly3VO> test(String queryString);

	/** STOCK SOLUTION **/
	public abstract StockSolution3VO merge(StockSolution3VO stockSolution3VO);

	public abstract StockSolution3VO findStockSolutionById(int stockSolutionId);

	public abstract void remove(StockSolution3VO stockSolution3VO);

	public abstract List<StockSolution3VO> findStockSolutionsByBoxId(String dewarId);

	public abstract List<StockSolution3VO> findStockSolutionsByProposalId(Integer proposalId);

	public Macromolecule3VO findMacromoleculesById(Integer macromoleculeId);	
	

}