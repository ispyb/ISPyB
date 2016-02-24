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

package ispyb.server.biosaxs.services.core.robot;

import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Sampleplate3VO;

import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.Remote;

@Remote
public interface Robot3Service {

	/** This method is used on the webservices **/
	public abstract Experiment3VO createExperimentFromRobotParams(
			ArrayList<HashMap<String, String>> samples,
			Integer sessionId, 
			int proposalId, 
			String mode, 
			String storageTemperature, 
			String extraFlowTime,
			String type, 
			String sourceFilePath, 
			String name,
			String comments) throws Exception;


	/**
	 * This method is used in the UI
	 * @param samples
	 * @param proposalId
	 * @param mode
	 * @param storageTemperature
	 * @param extraFlowTime
	 * @param type
	 * @param sourceFilePath
	 * @param name
	 * @param optimize
	 * @return
	 * @throws Exception
	 */
	public Experiment3VO createExperimentFromRobotParams(
			ArrayList<HashMap<String, String>> samples, 
			Integer sessionId,
			int proposalId,
			String mode,
			String storageTemperature, 
			String extraFlowTime, 
			String type,
			String sourceFilePath, 
			String name, 
			Boolean optimize,
			String comments) throws Exception;

	public Experiment3VO addMeasurementsToExperiment(int experimentId, int proposalId, ArrayList<HashMap<String, String>> samples) throws Exception;
	
	HashMap<String, Sampleplate3VO> getDefaultSampleChangerPlateconfiguration(String storageTemperature);

	public ArrayList<Macromolecule3VO> createOrUpdateMacromolecule(ArrayList<HashMap<String, String>> samples, int proposalId) throws Exception;
	
	public ArrayList<Buffer3VO> createOrUpdateBuffer(ArrayList<HashMap<String, String>> samples, int proposalId) throws Exception;
}