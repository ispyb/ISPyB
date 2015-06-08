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

package ispyb.server.biosaxs.services.core.analysis.advanced;

import ispyb.server.biosaxs.vos.advanced.FitStructureToExperimentalData3VO;
import ispyb.server.biosaxs.vos.advanced.RigidBodyModeling3VO;
import ispyb.server.biosaxs.vos.advanced.Superposition3VO;
import ispyb.server.mx.vos.collections.InputParameterWorkflow;
import ispyb.server.mx.vos.collections.Workflow3VO;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;


@Remote
public interface AdvancedAnalysis3Service {

	FitStructureToExperimentalData3VO merge(FitStructureToExperimentalData3VO fitStructure);

	FitStructureToExperimentalData3VO getFitStructureToExperimentDatal(int fitStructureToExperimentalDataId);

	Workflow3VO merge(Workflow3VO workflow, ArrayList<InputParameterWorkflow> inputs);

	FitStructureToExperimentalData3VO getFitStructureToExperimentDatalByWorkflowId(int parseInt);

	List<FitStructureToExperimentalData3VO> getFitStructureToExperimentalDataBySubtractionId(Integer subtractionId);

	List<RigidBodyModeling3VO> getRigidBodyModelingBySubtractionId(int subtractionId);

	List<Superposition3VO> getSuperpositionBySubtractionId(int subtractionId);

	List<FitStructureToExperimentalData3VO> getFitStructureToExperimentalDataByIds(List<Integer> fitIds);

	RigidBodyModeling3VO getRigidBodyById(int id);

	Superposition3VO getSuperpositionById(Integer superpositionId);



}