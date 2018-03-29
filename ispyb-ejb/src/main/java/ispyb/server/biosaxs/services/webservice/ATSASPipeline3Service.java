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

package ispyb.server.biosaxs.services.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Additive3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Measurement3VO;
import ispyb.server.biosaxs.vos.datacollection.Model3VO;

import javax.ejb.Remote;


@Remote
public interface ATSASPipeline3Service {

	public Experiment3VO createEmptyExperiment(String proposalCode, String number, String name) throws Exception;
	
	public Measurement3VO appendMeasurementToExperiment(String experimentId, String runNumber, String type, String plate, String row, String well, String name, String bufferName,
			String concentration, String sEUtemperature, String viscosity, String volume, String volumeToLoad, String waitTime, String transmission, String comments);
	
	public void addAveraged(String measurementId, String averaged, String discarded, String averageFile, String visitorFilePath);

	void addAveraged(String measurementId, String averaged, String discarded,
			String averageFile);
	
	public void addSubtraction(String experimentId, String runNumberList, String rgStdev, String i0, String i0Stdev, String firstPointUsed, String lastPointUsed, String quality,
			String isagregated, String rgGuinier, String rgGnom, String dmax, String total, String volume, String sampleOneDimensionalFiles,
			String bufferOneDimensionalFiles, String sampleAverageFilePath, String bestBufferFilePath, String subtractedFilePath, String experimentalDataPlotFilePath,
			String densityPlotFilePath, String guinierPlotFilePath, String kratkyPlotFilePath, String gnomOutputFilePath) throws Exception;

	public void addAbinitioModelling(String experimentId, String runNumber,
			ArrayList<Model3VO> models3vo, Model3VO referenceModel,
			Model3VO refinedModel) throws Exception;

	public Model3VO getModel(String experimentId, String runNumber,
			String pdbfilepath) throws Exception;

	public Model3VO merge(Model3VO model);

	public List<Macromolecule3VO> getMacromoleculeByAcronym(String proposal,
			String acronym);

	/** Used for ToolsForAssembly WS **/
	public void addSubtraction(String measurementId, String rgStdev, String i0, String i0Stdev, String firstPointUsed,
			String lastPointUsed, String quality, String isagregated, String rgGuinier, String rgGnom, String dmax, String total,
			String volume, String sampleOneDimensionalFiles, String bufferOneDimensionalFiles, String sampleAverageFilePath,
			String bestBufferFilePath, String subtractedFilePath, String experimentalDataPlotFilePath, String densityPlotFilePath,
			String guinierPlotFilePath, String kratkyPlotFilePath, String gnomOutputFilePath);
//
//
//	public void addSubtraction(int dataCollectionId, String rgStdev, String i0, String i0Stdev, String firstPointUsed,
//			String lastPointUsed, String quality, String isagregated, String rgGuinier, String rgGnom, String dmax, String total,
//			String volume, String sampleOneDimensionalFiles, String bufferOneDimensionalFiles, String sampleAverageFilePath,
//			String bestBufferFilePath, String subtractedFilePath, String experimentalDataPlotFilePath, String densityPlotFilePath,
//			String guinierPlotFilePath, String kratkyPlotFilePath, String gnomOutputFilePath);

	public void addMixture(String experimentId, String runNumber, String fitFilePath, String pdb) throws Exception;

	public void addRigidBodyModeling(String experimentId, String runNumber, String fitFilePath, String rigidBodyModelFilePath,
			String logFilePath, String curveConfigFilePath, String subunitConfigFilePath, String crosscorrConfigFilePath,
			String contactConditionsFilePath, String masterSymmetry)  throws Exception;

	public void addSuperposition(String experimentId, String runNumber, String abitioModelPdbFilePath, String aprioriPdbFilePath,
			String alignedPdbFilePath)throws Exception ;

	public HashMap<String, Object> getAprioriInformationByRunNumber(String proposal, String acronym) throws Exception;

	public List<Macromolecule3VO> getMacromoleculesByProposal(String code, String number) throws Exception;

	public List<Buffer3VO> getBuffersByProposal(String code, String number) throws Exception;
	
	public List<Additive3VO> getBufferAdditives(String code, String number, String buffer) throws Exception;


}