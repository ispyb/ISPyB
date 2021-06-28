/** This file is part of ISPyB.
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

package ispyb.ws.soap.em;

import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.em.services.collections.EM3Service;
import ispyb.server.em.vos.CTF;
import ispyb.server.em.vos.MotionCorrection;
import ispyb.server.em.vos.Movie;
import ispyb.server.em.vos.ParticlePicker;
import ispyb.server.em.vos.ParticleClassification;
import ispyb.server.em.vos.ParticleClassificationGroup;
import ispyb.server.mx.services.collections.DataCollection3Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ws.api.annotation.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

@WebService(name = "ToolsForEMWebService", serviceName = "ispybWS", targetNamespace = "http://ispyb.ejb3.webservices.em")
@SOAPBinding(style = Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Stateless
@RolesAllowed({ "WebService", "User", "Industrial" })
@SecurityDomain("ispyb")
@WebContext(authMethod = "BASIC", secureWSDLAccess = false, transportGuarantee = "NONE")
public class ToolsForEMDataCollection{

	protected Logger log = LoggerFactory.getLogger(ToolsForEMDataCollection.class);

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	/**
	 * 
	 * @param proposal
	 * @param sampleAcronym
	 * @param movieDirectory
	 * @param movieFullPath
	 * @param movieNumber
	 * @param micrographFullPath
	 * @param micrographSnapshotFullPath
	 * @param xmlMetaDataFullPath
	 * @param voltage STORED ON DATACOLLECTION.WAVELENGTH
	 * @param sphericalAberration STORED ON BEAMLINESETUP.CS
	 * @param amplitudeContrast 
	 * @param magnification STORED ON DATACOLLECTION.MAGNIFICATION
	 * @param scannedPixelSize
	 * @param imagesCount
	 * @param dosePerImage
	 * @param positionX
	 * @param positionY
	 * @param beamlineName
	 * @param gridSquareSnapshotFullPath
	 * @return
	 */
	@WebMethod(operationName = "addMovie")
	public Movie addMovie(
			@WebParam(name = "proposal") String proposal,
			@WebParam(name = "proteinAcronym") String proteinAcronym,
			@WebParam(name = "sampleAcronym") String sampleAcronym,
			@WebParam(name = "movieDirectory") String movieDirectory,
			@WebParam(name = "movieFullPath") String movieFullPath,
			@WebParam(name = "movieNumber") String movieNumber,
			@WebParam(name = "micrographFullPath") String micrographFullPath,
			@WebParam(name = "micrographSnapshotFullPath") String micrographSnapshotFullPath,
			@WebParam(name = "xmlMetaDataFullPath") String xmlMetaDataFullPath,
			@WebParam(name = "voltage") String voltage,
			@WebParam(name = "sphericalAberration") String sphericalAberration,
			@WebParam(name = "amplitudeContrast") String amplitudeContrast,
			@WebParam(name = "magnification") String magnification,
			@WebParam(name = "scannedPixelSize") String scannedPixelSize,
			@WebParam(name = "imagesCount") String imagesCount,
			@WebParam(name = "dosePerImage") String dosePerImage,
			@WebParam(name = "positionX") String positionX,
			@WebParam(name = "positionY") String positionY,
			@WebParam(name = "beamlineName") String beamlineName,
			@WebParam(name = "gridSquareSnapshotFullPath") String gridSquareSnapshotFullPath,
			@WebParam(name = "startTime") String stringStartTime
			
			)	
	{
		Date startTime = Calendar.getInstance().getTime();
		try{
			SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss"); 
			startTime = dt.parse(stringStartTime); 
		}
		catch(Exception exp){
			/** No action to be done **/
		}
		try {
			log.info("addMovie. technique=EM proposal={} proteinAcronym={} sampleAcronym={} movieDirectory={} moviePath={} movieNumber={} micrographPath={} thumbnailMicrographPath={} xmlMetaDataPath={} voltage={} sphericalAberration={} magnification={} scannedPixelSize={} imagesCount={} dosePerImage={} positionX={} positionY={} beamLineName={} startTime={} gridSquareSnapshotFullPath={}", proposal, proteinAcronym, sampleAcronym, movieDirectory, movieFullPath, movieNumber, micrographFullPath, micrographSnapshotFullPath, xmlMetaDataFullPath, voltage,sphericalAberration,magnification,scannedPixelSize,imagesCount,dosePerImage,positionX, positionY,beamlineName, startTime, gridSquareSnapshotFullPath);
			EM3Service service = (EM3Service) ejb3ServiceLocator.getLocalService(EM3Service.class);
			return service.addMovie(proposal, proteinAcronym, sampleAcronym, movieDirectory, movieFullPath, movieNumber, micrographFullPath, micrographSnapshotFullPath, xmlMetaDataFullPath, voltage, sphericalAberration, amplitudeContrast, magnification, scannedPixelSize, imagesCount, dosePerImage, positionX, positionY, beamlineName.toUpperCase(),startTime, gridSquareSnapshotFullPath);
		} catch (Exception exp) {
			exp.printStackTrace();
			log.error("Error addMovie: {}. technique=EM proposal={} proteinAcronym={}  sampleAcronym={} movieDirectory={} moviePath={} movieNumber={} micrographPath={} thumbnailMicrographPath={} xmlMetaDataPath={} voltage={} sphericalAberration={} magnification={} scannedPixelSize={} imagesCount={} dosePerImage={} positionX={} positionY={} beamLineName={} startTime={} gridSquareSnapshotFullPath={} cause={}",exp.getMessage(),  proposal, proteinAcronym, sampleAcronym, movieDirectory, movieFullPath, movieNumber, micrographFullPath, micrographSnapshotFullPath, xmlMetaDataFullPath, voltage,sphericalAberration,magnification,scannedPixelSize,imagesCount,dosePerImage,positionX, positionY,beamlineName, startTime, gridSquareSnapshotFullPath, exp.getCause());
		}
		return null;
	}
	
	
	@WebMethod(operationName = "addMotionCorrection")
	public MotionCorrection addMotionCorrection(
			@WebParam(name = "proposal") String proposal,
			@WebParam(name = "movieFullPath") String movieFullPath,
			@WebParam(name = "firstFrame") String firstFrame,
			@WebParam(name = "lastFrame") String lastFrame,
			@WebParam(name = "dosePerFrame") String dosePerFrame,
			@WebParam(name = "doseWeight") String doseWeight,
			@WebParam(name = "totalMotion") String totalMotion,
			@WebParam(name = "averageMotionPerFrame") String averageMotionPerFrame,
			@WebParam(name = "driftPlotFullPath") String driftPlotFullPath,
			@WebParam(name = "micrographFullPath") String micrographFullPath,
			@WebParam(name = "micrographSnapshotFullPath") String micrographSnapshotFullPath,
			@WebParam(name = "correctedDoseMicrographFullPath") String correctedDoseMicrographFullPath,
			@WebParam(name = "logFileFullPath") String logFileFullPath
			
			)	
	{
		try {
			log.info("addMotionCorrection. technique=EM "
					+ "proposal={} movieFullPath={} firstFrame={} lastFrame={} dosePerFrame={} doseWeight={} totalMotion={} averageMotionPerFrame={}  driftPlotFullPath={} micrographFullPath={} micrographSnapshotFullPath={} correctedDoseMicrographFullPath={} logFileFullPath={}", 
					proposal, movieFullPath, firstFrame, lastFrame, dosePerFrame, doseWeight, totalMotion, averageMotionPerFrame, driftPlotFullPath, micrographFullPath,
					micrographSnapshotFullPath, correctedDoseMicrographFullPath, logFileFullPath);
			EM3Service service = (EM3Service) ejb3ServiceLocator.getLocalService(EM3Service.class);
			return service.addMotionCorrection(proposal, movieFullPath, firstFrame, lastFrame, dosePerFrame, doseWeight, totalMotion, averageMotionPerFrame, driftPlotFullPath, micrographFullPath,
					micrographSnapshotFullPath, correctedDoseMicrographFullPath, logFileFullPath);
		} catch (Exception exp) {
			exp.printStackTrace();
			log.info("addMotionCorrection. technique=EM "
					+ "proposal={} movieFullPath={} firstFrame={} lastFrame={} dosePerFrame={} doseWeight={} totalMotion={} averageMotionPerFrame={}  driftPlotFullPath={} micrographFullPath={} micrographSnapshotFullPath={} correctedDoseMicrographFullPath={} logFileFullPath={} cause={}", 
					proposal, movieFullPath, firstFrame, lastFrame, dosePerFrame, doseWeight, totalMotion, averageMotionPerFrame, driftPlotFullPath, micrographFullPath,
					micrographSnapshotFullPath, correctedDoseMicrographFullPath, logFileFullPath, exp.getCause());
		}
		return null;
	}
	
	@WebMethod(operationName = "addCTF")
	public CTF addCTF(
			@WebParam(name = "proposal") String proposal,
			@WebParam(name = "movieFullPath") String movieFullPath,
			@WebParam(name = "spectraImageSnapshotFullPath") String spectraImageSnapshotFullPath,
			@WebParam(name = "spectraImageFullPath") String spectraImageFullPath,
			@WebParam(name = "defocusU") String defocusU,
			@WebParam(name = "defocusV") String defocusV,
			@WebParam(name = "angle") String angle,
			@WebParam(name = "crossCorrelationCoefficient") String crossCorrelationCoefficient,
			@WebParam(name = "resolutionLimit") String resolutionLimit,
			@WebParam(name = "estimatedBfactor") String estimatedBfactor,
			@WebParam(name = "logFilePath") String logFilePath	
			)	
	{
		try {
			log.info("addCTF. technique=EM proposal={} movieFullPath={} spectraImageSnapshotFullPath={} spectraImageFullPath={} defocusU={} defocusV={} angle={} crossCorrelationCoefficient={} resolutionLimit={} estimatedBfactor={} logFilePath={}", proposal, movieFullPath, spectraImageSnapshotFullPath, 
					defocusU, defocusV, angle, crossCorrelationCoefficient, resolutionLimit, estimatedBfactor, logFilePath);
			EM3Service service = (EM3Service) ejb3ServiceLocator.getLocalService(EM3Service.class);
			return service.addCTF(proposal, movieFullPath, spectraImageSnapshotFullPath, spectraImageFullPath, defocusU, defocusV, angle, crossCorrelationCoefficient, resolutionLimit, estimatedBfactor, logFilePath);
		} catch (Exception exp) {
			exp.printStackTrace();
			log.info("addCTF. technique=EM proposal={} movieFullPath={} spectraImageSnapshotFullPath={} spectraImageFullPath={} defocusU={} defocusV={} angle={} crossCorrelationCoefficient={} resolutionLimit={} estimatedBfactor={} logFilePath={} cause={}", proposal, movieFullPath, spectraImageSnapshotFullPath, 
					defocusU, defocusV, angle, crossCorrelationCoefficient, resolutionLimit, estimatedBfactor, logFilePath, exp.getCause());
		}
		return null;
	}
	
	@WebMethod(operationName = "addParticlePicker")
	public ParticlePicker addParticlePicker(
			@WebParam(name = "proposal") String proposal,
			@WebParam(name = "firstMovieFullPath") String firstMovieFullPath,
			@WebParam(name = "lastMovieFullPath") String lastMovieFullPath,
			@WebParam(name = "pickingProgram") String pickingProgram,
			@WebParam(name = "particlePickingTemplate") String particlePickingTemplate,
			@WebParam(name = "particleDiameter") String particleDiameter,
			@WebParam(name = "numberOfParticles") String numberOfParticles,
			@WebParam(name = "fullPathToParticleFile") String fullPathToParticleFile
			)	
	{
		try {
			log.info("addParticlePicker. technique=EM proposal={} firstMovieFullPath={} lastMovieFullPath={} pickingProgram={} particlePickingTemplate={} particleDiameter={} numberOfParticles={} fullPathToParticleFile={}", 
					proposal, firstMovieFullPath, lastMovieFullPath, pickingProgram, particlePickingTemplate, particleDiameter, numberOfParticles,fullPathToParticleFile);
			EM3Service service = (EM3Service) ejb3ServiceLocator.getLocalService(EM3Service.class);
			return service.addParticlePicker(proposal, firstMovieFullPath, lastMovieFullPath, pickingProgram, particlePickingTemplate, particleDiameter, numberOfParticles, fullPathToParticleFile);
		} catch (Exception exp) {
			exp.printStackTrace();
			log.info("addParticlePicker. technique=EM proposal={} firstMovieFullPath={} lastMovieFullPath={} pickingProgram={} particlePickingTemplate={} particleDiameter={} numberOfParticles={} fullPathToParticleFile={} cause={}", 
					proposal, firstMovieFullPath, lastMovieFullPath, pickingProgram, particlePickingTemplate, particleDiameter, numberOfParticles, fullPathToParticleFile, exp.getCause());
		}
		return null;
	}

	@WebMethod(operationName = "addParticleClassificationGroup")
	public ParticleClassificationGroup addParticleClassificationGroup(
			@WebParam(name = "particlePickerId") String particlePickerId,
			@WebParam(name = "type") String type,
			@WebParam(name = "batchNumber") String batchNumber,
			@WebParam(name = "numberOfParticlesPerBatch") String numberOfParticlesPerBatch,
			@WebParam(name = "numberOfClassesPerBatch") String numberOfClassesPerBatch,
			@WebParam(name = "symmetry") String symmetry,
			@WebParam(name = "classificationProgram") String classificationProgram
			)	
	{
		try {
			log.info("addParticleClassificationGroup. technique=EM particlePickerId={} type={} batchNumber={} " + 
					 "numberOfParticlesPerBatch={} numberOfClassesPerBatch={} symmetry={} " +
					 "classificationProgram={}", 
					 particlePickerId, type, batchNumber, numberOfParticlesPerBatch, numberOfClassesPerBatch, 
					 symmetry, classificationProgram);
			EM3Service service = (EM3Service) ejb3ServiceLocator.getLocalService(EM3Service.class);
			return service.addParticleClassificationGroup(particlePickerId, type, batchNumber, numberOfParticlesPerBatch, numberOfClassesPerBatch, 
					 symmetry, classificationProgram);
		} catch (Exception exp) {
			exp.printStackTrace();
			log.info("addParticleClassificationGroup. technique=EM particlePickerId={} type={} batchNumber={} " + 
					 "numberOfParticlesPerBatch={} numberOfClassesPerBatch={} symmetry={} " +
					 "classificationProgram={} cause={}", 
					 particlePickerId, type, batchNumber, numberOfParticlesPerBatch, numberOfClassesPerBatch, 
					 symmetry, classificationProgram, exp.getCause());
		}
		return null;
	}
	@WebMethod(operationName = "addParticleClassification")
	public ParticleClassification addParticleClassification(
			@WebParam(name = "particleClassificationGroupId") String particleClassificationGroupId,
			@WebParam(name = "classNumber") String classNumber,
			@WebParam(name = "classImageFullPath") String classImageFullPath,
			@WebParam(name = "particlesPerClass") String particlesPerClass,
			@WebParam(name = "classDistribution") String classDistribution,
			@WebParam(name = "rotationAccuracy") String rotationAccuracy,
			@WebParam(name = "translationAccuracy") String translationAccuracy,
			@WebParam(name = "estimatedResolution") String estimatedResolution,
			@WebParam(name = "overallFourierCompleteness") String overallFourierCompleteness
			)	
	{
		try {
			log.info("addParticleClassification. technique=EM particleClassificationGroupId={} classNumber={} " + 
					 "classImageFullPath={} particlesPerClass={} classDistribution={} rotationAccuracy={} translationAccuracy={} estimatedResolution={} " + 
					 "overallFourierCompleteness={}", 
					 particleClassificationGroupId, classNumber, classImageFullPath, particlesPerClass, classDistribution, rotationAccuracy, 
					 translationAccuracy, estimatedResolution, overallFourierCompleteness);
			EM3Service service = (EM3Service) ejb3ServiceLocator.getLocalService(EM3Service.class);
			return service.addParticleClassification(particleClassificationGroupId, classNumber, classImageFullPath,
					 particlesPerClass, classDistribution, classDistribution=rotationAccuracy, translationAccuracy, estimatedResolution, overallFourierCompleteness);
		} catch (Exception exp) {
			exp.printStackTrace();
			log.info("addParticleClassification. technique=EM particleClassificationGroupId={} classNumber={} " + 
					 "classImageFullPath={} particlesPerClass={} classDistribution={} rotationAccuracy={} translationAccuracy={} estimatedResolution={} " + 
					 "overallFourierCompleteness={} cause={}", 
					 particleClassificationGroupId, classNumber, classImageFullPath, particlesPerClass, classDistribution, rotationAccuracy, 
					 translationAccuracy, estimatedResolution, overallFourierCompleteness, exp.getCause());
		}
		return null;
	}

	@WebMethod(operationName = "getDataCollectionsByWorkingFolder")
	public CTF getDataCollectionsByWorkingFolder(
			@WebParam(name = "proposal") String proposal,
			@WebParam(name = "workingFolder") String workingFolder
			)	
	{
		try {
			throw new Exception("Not implemented yet");
		} catch (Exception exp) {
			exp.printStackTrace();
			log.error("getDataCollectionsByWorkingFolder. technique=EM proposal={} workingFolder={} ", proposal, workingFolder);
		}
		return null;
	}

	
}