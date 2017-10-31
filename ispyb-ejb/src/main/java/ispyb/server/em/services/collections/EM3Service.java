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

package ispyb.server.em.services.collections;


import java.util.Date;

import ispyb.server.em.vos.CTF;
import ispyb.server.em.vos.MotionCorrection;
import ispyb.server.em.vos.Movie;

import javax.ejb.Remote;


@Remote
public interface EM3Service {


	Movie addMovie(String proposal, String sampleAcronym, String movieDirectory,
			String moviePath, String movieNumber, String micrographPath,
			String thumbnailMicrographPath, String xmlMetaDataPath,
			String voltage, String sphericalAberration,
			String amplitudeContrast, String magnification,
			String scannedPixelSize, String noImages, String dosePerImage,
			 String positionX, String positionY,String beamlineName, Date startTime, String gridSquareSnapshotFullPath) throws Exception;

	MotionCorrection addMotionCorrection(String proposal, String movieFullPath,
			String firstFrame, String lastFrame, String dosePerFrame,
			String doseWeight, String totalMotion,
			String averageMotionPerFrame, String driftPlotFullPath,
			String micrographFullPath, String micrographSnapshotFullPath,
			String correctedDoseMicrographFullPath, String logFileFullPath);

	CTF addCTF(String proposal, String moviePath,
			String spectraImageSnapshotFullPath, String spectraImageFullPath, String defocusU,
			String defocusV, String angle, String crossCorrelationCoefficient,
			String resolutionLimit, String estimatedBfactor, String logFilePath);
}
