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

package ispyb.server.biosaxs.services.core.experiment;

import ispyb.server.biosaxs.services.core.ExperimentScope;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.assembly.Structure3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.utils.comparator.SaxsDataCollectionComparator;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

@Remote
public interface Experiment3Service {

	public abstract void persist(Experiment3VO transientInstance);

	public abstract Experiment3VO findById(Integer experimentId, ExperimentScope minimal);
	
	public abstract Experiment3VO findById(Integer experimentId, ExperimentScope scope, Integer proposalId);
	
	public abstract Experiment3VO findByMeasurementId(int measurementId);
	
	public abstract List<Experiment3VO> findByProposalId(int proposalId, ExperimentScope scope);
	
	public abstract Experiment3VO merge(Experiment3VO detachedInstance);
	
	public abstract List<Experiment3VO> test(String ejbQL);
	
	public abstract void remove(Experiment3VO experiment);

	public Experiment3VO setPriorities(int experimentId, int proposalId, SaxsDataCollectionComparator[] multipleOptions);
	
	public String toRobotXML(Integer experimentId, int proposalId, final SaxsDataCollectionComparator... multipleOptions);

	public String toRobotXML(Integer experimentId, int proposalId);

	public  void saveStructure(Integer macromoleculeId, String filename, String filePath, String type, String symmetry, String multiplicity);

	public void removeStructure(int structureId);

	public  Structure3VO findStructureById(int structureId);
	
	public  Structure3VO findStructureByFilePathId(String filePath, int experimentId);

	public  void removeStoichiometry(int stoichiometryId);

	public void saveStoichiometry(int macromoleculeId, int macromoleculeMolarityId, String ratio, String comments);

//	public Macromolecule3VO findMacromoleculeById(Integer macromoleculeId);

	public abstract Structure3VO saveStructure(Structure3VO structure3vo);

	public List<Map<String, Object>> getExperimentDescription(Integer experimentId);
	
	public Experiment3VO initPlates(Experiment3VO vo);


}