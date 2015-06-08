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

package ispyb.server.biosaxs.services.core.analysis.primaryDataProcessing;

import ispyb.server.biosaxs.vos.datacollection.Frame3VO;
import ispyb.server.biosaxs.vos.datacollection.Framelist3VO;
import ispyb.server.biosaxs.vos.datacollection.Merge3VO;
import ispyb.server.biosaxs.vos.datacollection.Subtraction3VO;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;


@Remote
public interface PrimaryDataProcessing3Service {

	public abstract void addMerge(
			int measurementId,
			int framesAverage,
			int framesMerge,
			String curves,
			String discardedCurves
			);

	public abstract Framelist3VO addFrameList(List<String> curveList);

	@SuppressWarnings("rawtypes")
	public abstract List test(String query);
	
	public abstract List<Frame3VO> getFramesByIdList(List<Integer> frameIdList);

	public abstract void addRun(int experimentId, String measurementId,
			String sampleCode, String exposureTemperature,
			String storageTemperature, String timePerFrame, String timeStart,
			String timeEnd, String energy, String detectorDistance,
			String edfFileArray, String snapshotCapillary,
			String currentMachine, String tocollect, String pars,
			String beamCenterX, String beamCenterY, String radiationRelative,
			String radiationAbsolute, String pixelSizeX, String pixelSizeY,
			String normalization, String transmission, ArrayList<String> curves);


	public abstract List<Merge3VO> getMergesByIdsList(List<Integer> mergeIdList);

	public abstract Subtraction3VO getSubstractionById(int substractionId);
	
	public abstract List<Subtraction3VO> getSubstractionsByMeasurementId(int measurementId);

	void addSubstraction(int dataCollectionId, String rg,
			String rgStdev, String i0, String i0Stdev, String firstPointUsed,
			String lastPointUsed, String quality, String isagregated,
			String code, String concentration, String gnomFile,
			String rgGuinier, String rgGnom, String dmax, String total,
			String volume, String scatteringFilePath, String guinierFilePath,
			String kratkyFilePath, String densityPlot,
			String substractedFilePath, String gnomFilePathOutput);


	List<Merge3VO> findByMeasurementId(int measurementId);

	void addMerge(int measurementId, int averagedCount, int framesCount, String oneDimensionalDataFilePathArray,
			String discardedCurves, String averageFilePath);

	void addRun(String measurementId, String exposureTemperature, String storageTemperature, String timePerFrame, String timeStart,
			String timeEnd, String energy, String detectorDistance, String snapshotCapillary, String currentMachine,
			String beamCenterX, String beamCenterY, String radiationRelative, String radiationAbsolute, String pixelSizeX,
			String pixelSizeY, String normalization, String transmission);

	void addRun(int experimentId, int measurementId, String exposureTemperature, String storageTemperature, String timePerFrame,
			String timeStart, String timeEnd, String energy, String detectorDistance, String snapshotCapillary, String currentMachine,
			String beamCenterX, String beamCenterY, String radiationRelative, String radiationAbsolute, String pixelSizeX,
			String pixelSizeY, String normalization, String transmission);

	void addRun(Integer experimentId, String measurementId, String exposureTemperature, String storageTemperature,
			String timePerFrame, String timeStart, String timeEnd, String energy, String detectorDistance, String edfFileArray,
			String snapshotCapillary, String currentMachine, String beamCenterX, String beamCenterY, String radiationRelative,
			String radiationAbsolute, String pixelSizeX, String pixelSizeY, String normalization, String transmission,
			ArrayList<String> curves);

}