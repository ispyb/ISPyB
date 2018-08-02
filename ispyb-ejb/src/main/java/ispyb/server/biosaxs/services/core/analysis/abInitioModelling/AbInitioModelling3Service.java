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

package ispyb.server.biosaxs.services.core.analysis.abInitioModelling;

import ispyb.server.biosaxs.vos.datacollection.AbInitioModel3VO;
import ispyb.server.biosaxs.vos.datacollection.Model3VO;
import ispyb.server.biosaxs.vos.datacollection.ModelList3VO;
import ispyb.server.biosaxs.vos.datacollection.ModelToList3VO;
import ispyb.server.biosaxs.vos.datacollection.Subtraction3VO;
import ispyb.server.biosaxs.vos.datacollection.SubtractiontoAbInitioModel3VO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;


@Remote
public interface AbInitioModelling3Service {

	public abstract List<Subtraction3VO> getSubtractionsByMeasurementList(ArrayList<Integer> measurementIds);
	
	public Model3VO merge(Model3VO model);

	public Model3VO findModelById(Integer modelId);
	
	public ModelList3VO merge(ModelList3VO modelList3VO);

	public AbInitioModel3VO merge(AbInitioModel3VO abInitioModel3VO);

	public SubtractiontoAbInitioModel3VO merge(SubtractiontoAbInitioModel3VO subtractiontoAbInitioModel3VO);

	public ModelToList3VO merge(ModelToList3VO modelToList3VO);

	public abstract void addAbinitioModeling(ArrayList<Integer> measurementIds, ArrayList<Model3VO> models3vo, Model3VO dammaverModel,
											Model3VO dammifModel, Model3VO damminModel, String nsdPlot, String chi2plot);

	public abstract List<AbInitioModel3VO> getAbinitioModelsByExperimentId(int experimentId);

	public abstract ModelList3VO getModelListById(int modelListId);

	public abstract AbInitioModel3VO getAbinitioModelsById(int abinitioModelId);

	public abstract List<Map<String, Object>> getAnalysisInformationByExperimentId(int macromoleculeId);
	
	public abstract List<Map<String, Object>> getAnalysisInformation(int limit);

	public abstract Model3VO getModelById(int modelId);

	List<Map<String, Object>> getAnalysisCalibrationByProposalId(int experimentId);

	public abstract List<Map<String, Object>> getAllByProposalId(int proposalId);

	public abstract void addAbinitioModeling(ArrayList<Integer> measurementIds,
			ArrayList<Model3VO> models3vo, Model3VO referenceModel,
			Model3VO refinedModel);

	public abstract Model3VO getModel(Integer measurementId, String pdbfilepath);
 
	public List test(String query);

	List<Subtraction3VO> getAbinitioModelsBySubtractionId(int subtractionId);

//	public abstract void addModelsByMeasurementId(ArrayList<Integer> measurementIds, ArrayList<Model3VO> models3vo);

}