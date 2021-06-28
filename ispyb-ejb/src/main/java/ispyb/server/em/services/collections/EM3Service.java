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


import ispyb.server.em.vos.CTF;
import ispyb.server.em.vos.MotionCorrection;
import ispyb.server.em.vos.Movie;
import ispyb.server.em.vos.ParticlePicker;
import ispyb.server.em.vos.ParticleClassification;
import ispyb.server.em.vos.ParticleClassificationGroup;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;


@Remote
public interface EM3Service {


	Movie addMovie(String proposal, String proteinAcronym, String sampleAcronym, String movieDirectory,
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
			String correctedDoseMicrographFullPath, String logFileFullPath) throws Exception;

	CTF addCTF(String proposal, String moviePath,
			String spectraImageSnapshotFullPath, String spectraImageFullPath, String defocusU,
			String defocusV, String angle, String crossCorrelationCoefficient,
			String resolutionLimit, String estimatedBfactor, String logFilePath);

	ParticlePicker addParticlePicker(String proposal, String firstMoviePath, String lastMoviePath,
			String pickingProgram, String particlePickingTemplate, String particleDiameter, 
			String numberOfParticles, String fullPathToParticleFile);

	ParticleClassificationGroup addParticleClassificationGroup(String particlePickerId, String type,
			String batchNumber, String numberOfParticlesPerBatch, String numberOfClassesPerBatch, 
			String symmetry, String classificationProgram);
	
	ParticleClassification addParticleClassification(String particleClassificationGroupId, String classNumber, 
			String classImageFullPath, String particlesPerClass, String classDistribution, String rotationAccuracy,
			String translationAccuracy, String estimatedResolution, String overallFourierCompleteness);

	List<String> getDoseByDataCollectionId(int proposalId, int dataCollectionId) throws Exception;

	List<Movie> getMoviesByDataCollectionId(int proposalId, int dataCollectionId) throws Exception;

	List<Map<String, Object>> getMoviesDataByDataCollectionId(int proposalId, int dataCollectionId);

	Movie getMovieByDataCollectionId(int proposalId, int dataCollectionId, int movieId) throws Exception;

	MotionCorrection getMotionCorrectionByMovieId(int proposalId, int dataCollectionId, int movieId) throws Exception;

	CTF getCTFByMovieId(int proposalId, int dataCollectionId, int movieId) throws Exception;

	List<Map<String, Object>> getStatsByDataCollectionIds(int proposalId, String dataCollectionIdList);

	Collection<? extends Map<String, Object>> getStatsByDataDataCollectionGroupId(Integer id);

	List<Map<String, Object>> getStatsBySessionId(int proposalId, int parseInt);

	List<Map<String, Object>> getClassificationBySessionId(int proposalId, int parseInt);
	
	ParticleClassification getClassificationByClassificationId(int proposalId, int particleClassificationId);
}
