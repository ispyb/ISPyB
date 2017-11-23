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
import ispyb.server.mx.services.collections.DataCollection3Service;

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
	 * @param sphericalAberration STORED ON BEAMLINESETIP.CS
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
			@WebParam(name = "gridSquareSnapshotFullPath") String gridSquareSnapshotFullPath
			
			)	
	{
		Date startTime = Calendar.getInstance().getTime();
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