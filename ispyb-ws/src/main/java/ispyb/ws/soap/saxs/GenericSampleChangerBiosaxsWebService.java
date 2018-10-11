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

package ispyb.ws.soap.saxs;

import java.lang.reflect.Type;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ws.api.annotation.WebContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import ispyb.common.util.StringUtils;
import ispyb.server.biosaxs.services.BiosaxsServices;
import ispyb.server.biosaxs.services.ExperimentSerializer;
import ispyb.server.biosaxs.services.core.ExperimentScope;
import ispyb.server.biosaxs.services.core.analysis.primaryDataProcessing.PrimaryDataProcessing3Service;
import ispyb.server.biosaxs.services.core.experiment.Experiment3Service;
import ispyb.server.biosaxs.services.core.measurement.Measurement3Service;
import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.services.webservice.ATSASPipeline3Service;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.assembly.Structure3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Additive3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Measurement3VO;
import ispyb.server.biosaxs.vos.datacollection.Model3VO;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.util.LoggerFormatter;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

@WebService(name = "GenericSampleChangerBiosaxsWebService", serviceName = "ispybWS", targetNamespace = "http://ispyb.ejb3.webservices.biosaxs")
@SOAPBinding(style = Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Stateless
@RolesAllowed({ "WebService", "User", "Industrial" })
@SecurityDomain("ispyb")
@WebContext(authMethod = "BASIC", secureWSDLAccess = false, transportGuarantee = "NONE")
public class GenericSampleChangerBiosaxsWebService {

	protected Logger log = Logger.getLogger(GenericSampleChangerBiosaxsWebService.class);

	protected long now;

	protected final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	@WebMethod(operationName = "getExperimentById")
	@WebResult(name = "experiment")
	public Experiment3VO getExperimentById(@WebParam(name = "experimentId") int experimentId) {
		long id = 0;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("experimentId", String.valueOf(experimentId));
			id = this.logInit("getExperimentById", new Gson().toJson(params));
		} catch (Exception exp) {
			id = this.logInit("getExperimentById");
			exp.printStackTrace();
		}

		try {

			Experiment3Service experiment3Service = (Experiment3Service) ejb3ServiceLocator.getLocalService(Experiment3Service.class);
			Experiment3VO experiment = experiment3Service.findById(experimentId, ExperimentScope.MEDIUM);
			logFinish("getExperimentById", id);
			return experiment;
		} catch (Exception e) {
			LoggerFormatter.log(this.log, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "getExperimentById", id,
					System.currentTimeMillis(), e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Creates a new empty experiment with no specimens and no measurements. Every experiment is linked to a proposal composed by a
	 * number and a code.
	 * <p>
	 * For instance: proposal mx1557 is composed by code: mx and number 1557
	 *
	 * @param <B>code </B> proposal code: MX or SAXS (for instance)
	 * @param <B>number </B> proposal number. It may be a number or a string
	 * @param <B>name </B> Name given to the experiment
	 * @return the experiment
	 * @throws Exception
	 */
	@WebResult(name = "experiment")
	public String createEmptyExperiment(@WebParam(name = "code") String code, @WebParam(name = "number") String number,
			@WebParam(name = "name") String name) throws Exception {
		/** Logging **/
		long id = 0;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("code", String.valueOf(code));
			params.put("number", String.valueOf(number));
			params.put("name", String.valueOf(name));
			id = this.logInit("createEmptyExperiment", new Gson().toJson(params));
		} catch (Exception exp) {
			id = this.logInit("createEmptyExperiment");
			exp.printStackTrace();
		}

		try {
			ATSASPipeline3Service connector = (ATSASPipeline3Service) ejb3ServiceLocator.getLocalService(ATSASPipeline3Service.class);
			Experiment3VO experiment = connector.createEmptyExperiment(StringUtils.getProposalCode(code), number, name);
			logFinish("createExperiment", id);
			return ExperimentSerializer.serializeExperiment(experiment, ExperimentScope.MINIMAL);
		} catch (Exception e) {
			LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "createEmptyExperiment", id,
					System.currentTimeMillis(), e.getMessage(), e);
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Appends a new measurement to an existing experiment identified by experimentId
	 *
	 * @param <B>experimentId </B> Integer. It is the id returned by createEmptyExperiment
	 * @param <B>runNumber </B> The run number. Together with the experimentId, they should be an identifier of the measurement.
	 * @param <B>type </B> Values: SAMPLE | BUFFER. <i> Type determines if ISPyB will create an specimen linked to a macromolecule or
	 *        just a buffer</i>
	 * @param <B>plate </B> Integer. It indicates the plate number (1,2,3, etc..)
	 * @param <B>row </B> Integer. It indicates the row number (1,2,3, etc..)
	 * @param <B>well </B> Integer. It indicates the row number (1,2,3, etc..)
	 * @param <B>macromolecule </B> Acronym of the macromolecule (it will be ignored if type is BUFFER). If there is not any
	 *        macromolecule with that acronym, ISpyB will create a new one.
	 * @param <B>buffer </B> Buffer acronym. If there is not any buffer with that acronym, ISpyB will create a new one.
	 * @param <i>concentration </i> Concentration of the macromolecule and it will be ignored if type is BUFFER
	 * @param <i>viscosity </i> Values : LOW, MEDIUM, HIGH
	 * @param <i>volume </i> Volume in well
	 * @param <i>volumeToLoad </i> Volume to load for the sample changer
	 * @param <i>waitTime </i>
	 * @param <i>transmission </i>
	 * @param <i>comments </i>
	 * @return the measurement
	 * @throws Exception
	 */
	@WebResult(name = "measurement")
	public Measurement3VO appendMeasurementToExperiment(@WebParam(name = "experimentId") String experimentId,
			@WebParam(name = "runNumber") String runNumber, @WebParam(name = "type") String type,
			@WebParam(name = "plate") String plate, @WebParam(name = "row") String row, @WebParam(name = "well") String well,
			@WebParam(name = "macromolecule") String macromolecule, @WebParam(name = "bufferName") String bufferName,
			@WebParam(name = "concentration") String concentration, @WebParam(name = "SEUtemperature") String SEUtemperature,
			@WebParam(name = "viscosity") String viscosity, @WebParam(name = "volume") String volume,
			@WebParam(name = "volumeToLoad") String volumeToLoad, @WebParam(name = "waitTime") String waitTime,
			@WebParam(name = "transmission") String transmission, @WebParam(name = "comments") String comments) throws Exception {

		/** Logging **/
		long id = 0;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("experimentId", String.valueOf(experimentId));
			params.put("runNumber", String.valueOf(runNumber));
			params.put("type", String.valueOf(type));
			params.put("plate", String.valueOf(plate));
			params.put("row", String.valueOf(row));
			params.put("well", String.valueOf(well));
			params.put("macromolecule", String.valueOf(macromolecule));
			params.put("bufferName", String.valueOf(bufferName));
			params.put("concentration", String.valueOf(concentration));
			params.put("SEUtemperature", String.valueOf(SEUtemperature));
			params.put("viscosity", String.valueOf(viscosity));
			params.put("volume", String.valueOf(volume));
			params.put("volumeToLoad", String.valueOf(volumeToLoad));
			params.put("waitTime", String.valueOf(waitTime));
			params.put("transmission", String.valueOf(transmission));
			params.put("comments", String.valueOf(comments));
			id = this.logInit("appendMeasurementToExperiment", new Gson().toJson(params));
		} catch (Exception exp) {
			id = this.logInit("appendMeasurementToExperiment");
			exp.printStackTrace();
		}

		try {

			System.out.println("ExperimentId: " + experimentId);
			System.out.println("runNumber: " + runNumber);
			System.out.println("type: " + type);
			System.out.println("plate: " + plate);
			System.out.println("row: " + row);
			System.out.println("well:" + well);
			System.out.println("name: " + macromolecule);
			System.out.println("bufferName: " + bufferName);
			System.out.println("concentration: " + concentration);
			System.out.println("SEUtemperature: " + SEUtemperature);
			System.out.println("viscosity: " + viscosity);
			System.out.println("volume: " + volume);
			System.out.println("volume: " + volumeToLoad);
			System.out.println("volumeToLoad: " + volume);
			System.out.println("waitTime: " + waitTime);
			System.out.println("transmission: " + transmission);
			System.out.println("comments: " + comments);
			ATSASPipeline3Service atsasPipeline3Service = (ATSASPipeline3Service) ejb3ServiceLocator
					.getLocalService(ATSASPipeline3Service.class);
			Measurement3VO measurement = atsasPipeline3Service.appendMeasurementToExperiment(experimentId, runNumber, type, plate,
					row, well, macromolecule, bufferName, concentration, SEUtemperature, viscosity, volume, volumeToLoad, waitTime,
					transmission, comments);

			logFinish("appendMeasurementToExperiment", id);
			return measurement;
		} catch (Exception e) {
			LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "appendMeasurementToExperiment", id,
					System.currentTimeMillis(), e.getMessage(), e);
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Appends a new run to the measurement idetified by experimentId and run Number
	 *
	 * @param <B>experimentId </B> Integer. It is the id returned by createEmptyExperiment
	 * @param <B>runNumber </B> The run number. Together with the experimentId, they should be an identifier of the measurement.
	 * @param <i>exposureTemperature </i>
	 * @param <i>storageTemperature </i>
	 * @param <i>timePerFrame </i>
	 * @param <i>timeStart </i>
	 * @param <i>timeEnd </i>
	 * @param <i>energy </i>
	 * @param <i>detectorDistance </i>
	 * @param <i>snapshotCapillary </i>
	 * @param <i>currentMachine </i>
	 * @param <i>beamCenterX </i>
	 * @param <i>beamCenterY </i>
	 * @param <i>radiationRelative </i>
	 * @param <i>radiationAbsolute </i>
	 * @param <i>pixelSizeX </i>
	 * @param <i>pixelSizeY </i>
	 * @param <i>normalization </i>
	 * @param <i>transmission </i>
	 * @return the measurement
	 * @throws Exception
	 */
	@WebResult(name = "addRun")
	public void addRun(@WebParam(name = "experimentId") String experimentId, @WebParam(name = "runNumber") String runNumber,
			@WebParam(name = "exposureTemperature") String exposureTemperature,
			@WebParam(name = "storageTemperature") String storageTemperature, @WebParam(name = "timePerFrame") String timePerFrame,
			@WebParam(name = "timeStart") String timeStart, @WebParam(name = "timeEnd") String timeEnd,
			@WebParam(name = "energy") String energy, @WebParam(name = "detectorDistance") String detectorDistance,
			@WebParam(name = "snapshotCapillary") String snapshotCapillary,
			@WebParam(name = "currentMachine") String currentMachine,
			@WebParam(name = "beamCenterX") String beamCenterX,
			@WebParam(name = "beamCenterY") String beamCenterY,
			@WebParam(name = "radiationRelative") String radiationRelative,
			@WebParam(name = "radiationAbsolute") String radiationAbsolute, @WebParam(name = "pixelSizeX") String pixelSizeX,
			@WebParam(name = "pixelSizeY") String pixelSizeY, @WebParam(name = "normalization") String normalization,
			@WebParam(name = "transmission") String transmission) throws Exception {

		/** Logging **/
		long id = 0;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("experimentId", String.valueOf(experimentId));
			params.put("runNumber", String.valueOf(runNumber));
			params.put("exposureTemperature", String.valueOf(exposureTemperature));
			params.put("storageTemperature", String.valueOf(storageTemperature));
			params.put("timePerFrame", String.valueOf(timePerFrame));
			params.put("timeStart", String.valueOf(timeStart));
			params.put("timeEnd", String.valueOf(timeEnd));
			params.put("energy", String.valueOf(energy));
			params.put("detectorDistance", String.valueOf(detectorDistance));
			params.put("snapshotCapillary", String.valueOf(snapshotCapillary));
			params.put("currentMachine", String.valueOf(currentMachine));
			params.put("beamCenterX", String.valueOf(beamCenterX));
			params.put("beamCenterY", String.valueOf(beamCenterY));
			params.put("radiationRelative", String.valueOf(radiationRelative));
			params.put("radiationAbsolute", String.valueOf(radiationAbsolute));
			params.put("pixelSizeX", String.valueOf(pixelSizeX));
			params.put("pixelSizeY", String.valueOf(pixelSizeY));
			params.put("normalization", String.valueOf(normalization));
			params.put("transmission", String.valueOf(transmission));

			id = this.logInit("addRun", new Gson().toJson(params));
		} catch (Exception exp) {
			id = this.logInit("addRun");
			exp.printStackTrace();
		}

		try {
			System.out.println("experimentId: " + experimentId);
			System.out.println("runNumber: " + runNumber);
			System.out.println("exposureTemperature: " + exposureTemperature);
			System.out.println("storageTemperature: " + storageTemperature);
			System.out.println("timePerFrame: " + timePerFrame);
			System.out.println("timeStart: " + timeStart);
			System.out.println("timeEnd: " + timeEnd);
			System.out.println("energy: " + energy);
			System.out.println("detectorDistance: " + detectorDistance);
			System.out.println("snapshotCapillary: " + snapshotCapillary);
			System.out.println("beamCenterX: " + beamCenterX);
			System.out.println("beamCenterY: " + beamCenterY);
			System.out.println("radiationRelative: " + radiationRelative);
			System.out.println("radiationAbsolute: " + radiationAbsolute);
			System.out.println("pixelSizeX: " + pixelSizeX);
			System.out.println("pixelSizeY: " + pixelSizeY);
			System.out.println("normalization: " + normalization);
			System.out.println("transmission: " + transmission);

			Measurement3Service measurement3Service = (Measurement3Service) ejb3ServiceLocator
					.getLocalService(Measurement3Service.class);
			Measurement3VO measurement = measurement3Service.findMeasurementByCode(Integer.parseInt(experimentId), runNumber);

			if (measurement != null) {
				PrimaryDataProcessing3Service primaryDataProcessing3Service = (PrimaryDataProcessing3Service) ejb3ServiceLocator
						.getLocalService(PrimaryDataProcessing3Service.class);
				primaryDataProcessing3Service.addRun(Integer.parseInt(experimentId), measurement.getMeasurementId(),
						exposureTemperature, storageTemperature, timePerFrame, timeStart, timeEnd, energy, detectorDistance,
						snapshotCapillary, currentMachine, beamCenterX, beamCenterY, radiationRelative, radiationAbsolute, pixelSizeX,
						pixelSizeY, normalization, transmission);
			} else {
				log.error("No measurement found for runNumber: " + runNumber + " and experimentId: " + experimentId);
			}

			logFinish("addRun", id);
		} catch (Exception e) {
			LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "addRun", id, System.currentTimeMillis(),
					e.getMessage(), e);
			e.printStackTrace();
			throw e;
		}
	}

	@WebResult(name = "addAveraged")
	public void addAveraged(@WebParam(name = "experimentId") String experimentId, @WebParam(name = "runNumber") String runNumber,
			@WebParam(name = "averaged") String averaged, @WebParam(name = "discarded") String discarded,
			@WebParam(name = "averageFile") String averageFile) throws Exception {

		/** Logging **/
		long id = 0;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("experimentId", String.valueOf(experimentId));
			params.put("runNumber", String.valueOf(runNumber));
			params.put("averaged", String.valueOf(averaged));
			params.put("discarded", String.valueOf(discarded));
			params.put("averageFile", String.valueOf(averageFile));
			id = this.logInit("addAveraged", new Gson().toJson(params));
		} catch (Exception exp) {
			id = this.logInit("addAveraged");
			exp.printStackTrace();
		}

		try {

			System.out.println("-----------------------");
			System.out.println(" addAveraged");
			System.out.println(" experimentId:\t" + experimentId);
			System.out.println(" runNumber:\t" + runNumber);
			System.out.println(" averaged:\t" + averaged);
			System.out.println(" discarded:\t" + discarded);
			System.out.println(" averageFile:\t" + averageFile);

			Measurement3Service measurement3Service = (Measurement3Service) ejb3ServiceLocator.getLocalService(Measurement3Service.class);
			Measurement3VO measurement = measurement3Service.findMeasurementByCode(Integer.parseInt(experimentId), runNumber);

			if (measurement != null) {
				ATSASPipeline3Service atsasPipeline3Service = (ATSASPipeline3Service) ejb3ServiceLocator
						.getLocalService(ATSASPipeline3Service.class);
				atsasPipeline3Service.addAveraged(measurement.getMeasurementId().toString(), averaged, discarded, averageFile);
			} else {
				log.error("No measurement found for runNumber: " + runNumber + " and experimentId: " + experimentId);
			}
			logFinish("addAveraged", id);
		} catch (Exception e) {
			LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "addAveraged", id, System.currentTimeMillis(),
					e.getMessage(), e);
			e.printStackTrace();
			throw e;
		}
	}

	@WebMethod(operationName = "addSubtraction")
	public void addSubtraction(@WebParam(name = "experimentId") String experimentId,
			@WebParam(name = "runNumberList") String runNumberList,

			/** XSDataAutoRg **/
			@WebParam(name = "rgGuinier") String rgGuinier, @WebParam(name = "rgStdev") String rgStdev,
			@WebParam(name = "i0") String i0, @WebParam(name = "i0Stdev") String i0Stdev,

			@WebParam(name = "firstPointUsed") String firstPointUsed,
			@WebParam(name = "lastPointUsed") String lastPointUsed,
			@WebParam(name = "quality") String quality,
			@WebParam(name = "isagregated") String isagregated,

			/** XSDataGnom **/
			@WebParam(name = "rgGnom") String rgGnom,
			@WebParam(name = "dmax") String dmax,
			@WebParam(name = "total") String total,

			/** Volume **/
			@WebParam(name = "volume") String volume,
			/** Some extra params **/
			@WebParam(name = "sampleOneDimensionalFiles") String sampleOneDimensionalFiles,
			@WebParam(name = "bufferOneDimensionalFiles") String bufferOneDimensionalFiles,
			@WebParam(name = "sampleAverageFilePath") String sampleAverageFilePath,
			@WebParam(name = "bestBufferFilePath") String bestBufferFilePath,
			@WebParam(name = "subtractedFilePath") String subtractedFilePath,

			@WebParam(name = "experimentalDataPlotFilePath") String experimentalDataPlotFilePath,
			@WebParam(name = "densityPlotFilePath") String densityPlotFilePath,
			@WebParam(name = "guinierPlotFilePath") String guinierPlotFilePath,
			@WebParam(name = "kratkyPlotFilePath") String kratkyPlotFilePath,

			@WebParam(name = "gnomOutputFilePath") String gnomOutputFilePath)

	throws Exception {

		/** Logging **/
		long id = 0;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("experimentId", String.valueOf(experimentId));
			params.put("runNumberList", String.valueOf(runNumberList));
			params.put("rgGuinier", String.valueOf(rgGuinier));
			params.put("rgStdev", String.valueOf(rgStdev));
			params.put("i0", String.valueOf(i0));
			params.put("i0Stdev", String.valueOf(i0Stdev));
			params.put("firstPointUsed", String.valueOf(firstPointUsed));
			params.put("lastPointUsed", String.valueOf(lastPointUsed));
			params.put("quality", String.valueOf(quality));
			params.put("isagregated", String.valueOf(isagregated));
			// params.put("concentration", String.valueOf(concentration));
			params.put("rgGnom", String.valueOf(rgGnom));
			params.put("dmax", String.valueOf(dmax));
			params.put("total", String.valueOf(total));
			params.put("volume", String.valueOf(volume));
			params.put("sampleOneDimensionalFiles", String.valueOf(sampleOneDimensionalFiles));
			params.put("bufferOneDimensionalFiles", String.valueOf(bufferOneDimensionalFiles));
			params.put("sampleAverageFilePath", String.valueOf(sampleAverageFilePath));
			params.put("bestBufferFilePath", String.valueOf(bestBufferFilePath));
			params.put("subtractedFilePath", String.valueOf(subtractedFilePath));
			params.put("experimentalDataPlotFilePath", String.valueOf(experimentalDataPlotFilePath));
			params.put("densityPlotFilePath", String.valueOf(densityPlotFilePath));
			params.put("guinierPlotFilePath", String.valueOf(guinierPlotFilePath));
			params.put("kratkyPlotFilePath", String.valueOf(kratkyPlotFilePath));
			params.put("gnomOutputFilePath", String.valueOf(gnomOutputFilePath));

			id = this.logInit("addSubtraction", new Gson().toJson(params));
		} catch (Exception exp) {
			id = this.logInit("addSubtraction");
			exp.printStackTrace();
		}

		try {

			System.out.println("-----------------------");
			System.out.println(" addSubtraction");
			System.out.println(" runNumberList:\t" + runNumberList);
			System.out.println(" rgGuinier:\t" + rgGuinier);
			System.out.println(" rgStdev:\t" + rgStdev);
			System.out.println(" i0:\t" + i0);
			System.out.println(" i0Stdev:\t" + i0Stdev);
			System.out.println(" firstPointUsed:\t" + firstPointUsed);
			System.out.println(" lastPointUsed:\t" + lastPointUsed);
			System.out.println(" quality:\t" + quality);
			System.out.println(" isagregated:\t" + isagregated);
			// System.out.println(" concentration:\t" + concentration);
			System.out.println(" rgGnom:\t" + rgGnom);
			System.out.println(" dmax:\t" + dmax);
			System.out.println(" total:\t" + total);
			System.out.println(" volume:\t" + volume);
			System.out.println(" sampleOneDimensionalFiles:\t" + sampleOneDimensionalFiles);
			System.out.println(" bufferOneDimensionalFiles:\t" + bufferOneDimensionalFiles);
			System.out.println(" sampleAverageFilePath:\t" + sampleAverageFilePath);
			System.out.println(" bestBufferFilePath:\t" + bestBufferFilePath);
			System.out.println(" subtractedFilePath:\t" + subtractedFilePath);
			System.out.println(" experimentalDataPlotFilePath:\t" + experimentalDataPlotFilePath);
			System.out.println(" densityPlotFilePath:\t" + densityPlotFilePath);
			System.out.println(" guinierPlotFilePath:\t" + guinierPlotFilePath);
			System.out.println(" kratkyPlotFilePath:\t" + kratkyPlotFilePath);
			System.out.println(" gnomOutputFilePath:\t" + gnomOutputFilePath);

			ATSASPipeline3Service atsasPipeline3Service = (ATSASPipeline3Service) ejb3ServiceLocator.getLocalService(ATSASPipeline3Service.class);

			atsasPipeline3Service.addSubtraction(experimentId, runNumberList, rgStdev, i0, i0Stdev, firstPointUsed, lastPointUsed,
					quality, isagregated, rgGuinier, rgGnom, dmax, total, volume, sampleOneDimensionalFiles,
					bufferOneDimensionalFiles, sampleAverageFilePath, bestBufferFilePath, subtractedFilePath,
					experimentalDataPlotFilePath, densityPlotFilePath, guinierPlotFilePath, kratkyPlotFilePath, gnomOutputFilePath);

			this.logFinish("addSubtraction", id);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "addSubtraction", id, System.currentTimeMillis(),
					e.getMessage(), e);
			throw e;
		}
	}

	@WebResult(name = "addAbinitioModelling")
	public void addAbinitioModelling(@WebParam(name = "experimentId") String experimentId,
			@WebParam(name = "runNumber") String runNumber, @WebParam(name = "models") String models,
			@WebParam(name = "reference") String reference, @WebParam(name = "refined") String refined) throws Exception {

		/** Logging **/
		long id = 0;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("experimentId", String.valueOf(experimentId));
			params.put("runNumber", String.valueOf(runNumber));
			params.put("models", String.valueOf(models));
			params.put("reference", String.valueOf(reference));
			params.put("refined", String.valueOf(refined));
			id = this.logInit("addAbinitioModelling", new Gson().toJson(params));
		} catch (Exception exp) {
			id = this.logInit("addAbinitioModelling");
			exp.printStackTrace();
		}

		try {

			ATSASPipeline3Service atsasPipeline3Service = (ATSASPipeline3Service) ejb3ServiceLocator
					.getLocalService(ATSASPipeline3Service.class);

			Type mapType = new TypeToken<ArrayList<HashMap<String, String>>>() {
			}.getType();
			ArrayList<HashMap<String, String>> modelList = new Gson().fromJson(models, mapType);

			Type mapTypeModel = new TypeToken<HashMap<String, String>>() {
			}.getType();
			HashMap<String, String> referenceModelObject = new Gson().fromJson(reference, mapTypeModel);
			HashMap<String, String> refinedModelObject = new Gson().fromJson(refined, mapTypeModel);

			ArrayList<Model3VO> models3VO = new ArrayList<Model3VO>();
			for (HashMap<String, String> model : modelList) {
				models3VO.add(Model3VO.deserialize(model));
			}

			Model3VO referenceModel = Model3VO.deserialize(referenceModelObject);
			Model3VO refinedModel = Model3VO.deserialize(refinedModelObject);

			atsasPipeline3Service.addAbinitioModelling(experimentId, runNumber, models3VO, referenceModel, refinedModel);

			logFinish("addAbinitioModelling", id);
		} catch (Exception e) {
			LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "addAbinitioModelling", id, System.currentTimeMillis(),
					e.getMessage(), e);
			e.printStackTrace();
			throw e;
		}
	}

	@Deprecated
	@WebResult(name = "getMacromoleculeByAcronym")
	public List<Macromolecule3VO> getMacromoleculeByAcronym(
			@WebParam(name = "code") String code,
			@WebParam(name = "number") String number,
			@WebParam(name = "macromolecule") String acronym) {

		/** Logging **/
		long id = 0;
		String proposal = code + number;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("proposal", String.valueOf(proposal));
			params.put("macromolecule", String.valueOf(acronym));
			id = this.logInit("getMacromoleculeByAcronym", new Gson().toJson(params));
		} catch (Exception exp) {
			id = this.logInit("getMacromoleculeByAcronym");
			exp.printStackTrace();
		}

		try {
			ATSASPipeline3Service atsasPipeline3Service = (ATSASPipeline3Service) ejb3ServiceLocator.getLocalService(ATSASPipeline3Service.class);
			List<Macromolecule3VO> macromolecules = atsasPipeline3Service.getMacromoleculeByAcronym(proposal, acronym);
			logFinish("getMacromoleculeByAcronym", id);
			return macromolecules;
		} catch (Exception e) {
			LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "addAbinitioModelling", id, System.currentTimeMillis(),
					e.getMessage(), e);
			e.printStackTrace();
		}
		return null;
	}


	@WebResult(name = "macromolecules")
	public String getMacromoleculesByProposal(
			@WebParam(name = "code") String code,
			@WebParam(name = "number") String number) {

		/** Logging **/
		long id = 0;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("code", String.valueOf(code));
			params.put("number", String.valueOf(number));
			id = this.logInit("getMacromoleculeByAcronym", new Gson().toJson(params));
		} catch (Exception exp) {
			id = this.logInit("getMacromoleculeByAcronym");
			exp.printStackTrace();
		}

		try {
			ATSASPipeline3Service atsasPipeline3Service = (ATSASPipeline3Service) ejb3ServiceLocator.getLocalService(ATSASPipeline3Service.class);
			List<Macromolecule3VO> macromolecules = atsasPipeline3Service.getMacromoleculesByProposal(code, number);
			logFinish("getMacromoleculesByProposal", id);
			return new GsonBuilder().serializeNulls().create().toJson(macromolecules);
		} catch (Exception e) {
			LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "getMacromoleculesByProposal", id, System.currentTimeMillis(),
					e.getMessage(), e);
			e.printStackTrace();
		}
		return null;
	}

	@WebResult(name = "buffers")
	public String getBuffersByProposal(
			@WebParam(name = "code") String code,
			@WebParam(name = "number") String number) {

		/** Logging **/
		long id = 0;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("code", String.valueOf(code));
			params.put("number", String.valueOf(number));
			id = this.logInit("getBuffersByProposal", new Gson().toJson(params));
		} catch (Exception exp) {
			id = this.logInit("getBuffersByProposal");
			exp.printStackTrace();
		}

		try {
			ATSASPipeline3Service atsasPipeline3Service = (ATSASPipeline3Service) ejb3ServiceLocator.getLocalService(ATSASPipeline3Service.class);
			List<Buffer3VO> buffers = atsasPipeline3Service.getBuffersByProposal(code, number);

			GsonBuilder builder = new GsonBuilder();
			builder.setExclusionStrategies(new ExclusionStrategy() {
				@Override
				public boolean shouldSkipField(FieldAttributes f) {
			        return (f.getDeclaringClass() == Buffer3VO.class && f.getName().equals("bufferhasadditive3VOs"));
			    }
			 
			    public boolean shouldSkipClass(Class<?> clazz) {
			        return false;
			    }
			});
			builder.setPrettyPrinting();
			Gson gson = builder.serializeNulls().create();
			/*Serialize to JSON */
	        String jsonString = gson.toJson(buffers); 
			return jsonString;
		} catch (Exception e) {
			LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "getBuffersByProposal", id, System.currentTimeMillis(),
					e.getMessage(), e);
			e.printStackTrace();
		}
		logFinish("getBuffersByProposal", id);
		return null;

	}

	@WebResult(name = "buffers")
	public String getBufferAdditives(
			@WebParam(name = "code") String code,
			@WebParam(name = "number") String number,
			@WebParam(name = "buffer") String buffer) {

		/** Logging **/
		long id = 0;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("code", String.valueOf(code));
			params.put("number", String.valueOf(number));
			params.put("buffer", String.valueOf(buffer));
			id = this.logInit("getBufferAdditives", new Gson().toJson(params));
		} catch (Exception exp) {
			id = this.logInit("getBufferAdditives");
			exp.printStackTrace();
		}

		try {
			ATSASPipeline3Service atsasPipeline3Service = (ATSASPipeline3Service) ejb3ServiceLocator.getLocalService(ATSASPipeline3Service.class);
			List<Additive3VO> additives = atsasPipeline3Service.getBufferAdditives(code, number, buffer);
			logFinish("getBufferAdditives", id);
			GsonBuilder builder = new GsonBuilder();
			builder.setExclusionStrategies(new ExclusionStrategy() {
				@Override
				public boolean shouldSkipField(FieldAttributes f) {
			        return (f.getDeclaringClass() == Additive3VO.class && f.getName().equals("bufferhasadditive3VOs"));
			    }
			 
			    public boolean shouldSkipClass(Class<?> clazz) {
			        return false;
			    }
			});
			builder.setPrettyPrinting();
			Gson gson = builder.serializeNulls().create();
			/*Serialize to JSON */
	        String jsonString = gson.toJson(additives); 
			return jsonString;
		} catch (Exception e) {
			LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "getBufferAdditives", id, System.currentTimeMillis(),
					e.getMessage(), e);
			e.printStackTrace();
		}
		return null;

	}


	@WebResult(name = "getAprioriInformationByAcronym")
	public String getAprioriInformationByAcronym(
			@WebParam(name = "code") String code,
			@WebParam(name = "number") String number,
			@WebParam(name = "macromolecule") String acronym) {

		/** Logging **/
		long id = 0;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("code", String.valueOf(code));
			params.put("number", String.valueOf(number));
			params.put("acronym", String.valueOf(acronym));
			id = this.logInit("getAprioriInformationByAcronym", new Gson().toJson(params));
		} catch (Exception exp) {
			id = this.logInit("getAprioriInformationByAcronym");
			exp.printStackTrace();
		}

		try {
			String proposal = code + number;
			ATSASPipeline3Service atsasPipeline3Service = (ATSASPipeline3Service) ejb3ServiceLocator.getLocalService(ATSASPipeline3Service.class);
			HashMap<String, Object> info = atsasPipeline3Service.getAprioriInformationByRunNumber(proposal, acronym);


			logFinish("getAprioriInformationByAcronym", id);
			return new GsonBuilder().serializeNulls().create().toJson(info);
		} catch (Exception e) {
			LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "getAprioriInformationByAcronym", id, System.currentTimeMillis(),
					e.getMessage(), e);
			e.printStackTrace();
		}
		return "{}";
	}

	@SuppressWarnings("unchecked")
	public List<String> getStructure(List<Macromolecule3VO> macromolecules, String type) {
		ArrayList<String> result = new ArrayList<String>();
		for (Macromolecule3VO macromolecule3vo : macromolecules) {
			for (Structure3VO structure : macromolecule3vo.getStructure3VOs()) {
				if (structure.getType().toUpperCase().equals(type.toUpperCase())) {
					result.add(structure.getFilePath());
				}
			}
		}
		return result;
	}

	@Deprecated
	@WebResult(name = "getContactDescriptioFilePathByAcronym")
	public String getContactDescriptioFilePathByAcronym(
			@WebParam(name = "code") String code,
			@WebParam(name = "number") String number,
			@WebParam(name = "macromolecule") String acronym) {

		/** Logging **/
		long id = 0;
		String proposal = code + number;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("proposal", String.valueOf(proposal));
			params.put("number", String.valueOf(number));
			params.put("macromolecule", String.valueOf(acronym));
			id = this.logInit("getContactDescriptioFilePathByAcronym", new Gson().toJson(params));
		} catch (Exception exp) {
			id = this.logInit("getContactDescriptioFilePathByAcronym");
			exp.printStackTrace();
		}

		try {

			ATSASPipeline3Service atsasPipeline3Service = (ATSASPipeline3Service) ejb3ServiceLocator.getLocalService(ATSASPipeline3Service.class);
			List<Macromolecule3VO> macromolecules = atsasPipeline3Service.getMacromoleculeByAcronym(proposal, acronym);

			List<String> result = new ArrayList<String>();
			for (Macromolecule3VO macromolecule : macromolecules) {
				if (macromolecule.getContactsDescriptionFilePath() != null){
					if (macromolecule.getContactsDescriptionFilePath().length() > 0){
						result.add(macromolecule.getContactsDescriptionFilePath());
					}
				}
			}

			logFinish("getContactDescriptioFilePathByAcronym", id);
			if (result.size() > 0) {
				/** Parsing results as a comma separated string **/
				StringBuilder sb = new StringBuilder();
				for (String filePath : result) {
					sb.append(filePath);
					sb.append(";");
				}
				return sb.toString().substring(0, sb.toString().length() - 1);
			}

			return "";
		} catch (Exception e) {
			LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "getContactDescriptioFilePathByAcronym", id, System.currentTimeMillis(),
					e.getMessage(), e);
			e.printStackTrace();
		}
		return null;
	}

	@Deprecated
	@WebResult(name = "getPDBFilePathByAcronym")
	public String getStructureFilePathByAcronym(@WebParam(name = "code") String code, @WebParam(name = "number") String number,
			@WebParam(name = "macromolecule") String acronym, @WebParam(name = "type") String type) { // type: ["PDB" || "FASTA"]

		/** Logging **/
		long id = 0;
		String proposal = code + number;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("proposal", String.valueOf(proposal));
			params.put("acronym", String.valueOf(acronym));
			id = this.logInit("getPDBFilePathByAcronym", new Gson().toJson(params));
		} catch (Exception exp) {
			id = this.logInit("getPDBFilePathByAcronym");
			exp.printStackTrace();
		}

		try {

			ATSASPipeline3Service atsasPipeline3Service = (ATSASPipeline3Service) ejb3ServiceLocator
					.getLocalService(ATSASPipeline3Service.class);
			List<Macromolecule3VO> macromolecules = atsasPipeline3Service.getMacromoleculeByAcronym(proposal, acronym);
			List<String> result = this.getStructure(macromolecules, type);
			logFinish("getPDBFilePathByAcronym", id);
			if (result.size() > 0) {
				/** Parsing results as a comma separated string **/
				StringBuilder sb = new StringBuilder();
				for (String filePath : result) {
					sb.append(filePath);
					sb.append(";");
				}
				return sb.toString().substring(0, sb.toString().length() - 1);
			}
			return "";
		} catch (Exception e) {
			LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "addAbinitioModelling", id, System.currentTimeMillis(),
					e.getMessage(), e);
			e.printStackTrace();
		}
		return null;
	}

	@WebResult(name = "addModel")
	public void addModel(
			@WebParam(name = "experimentId") String experimentId,
			@WebParam(name = "runNumber") String runNumber,
			@WebParam(name = "type") String type, // type : "Model"||"Reference"||"Refined"
			@WebParam(name = "pdbFilePath") String pdbfilepath, @WebParam(name = "firFilePath") String firfilepath,
			@WebParam(name = "logFilePath") String logfilepath, @WebParam(name = "rg") String rg,
			@WebParam(name = "dmax") String dmax, @WebParam(name = "volume") String volume, @WebParam(name = "weight") String weight)
			throws Exception {

		/** Logging **/
		long id = 0;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("experimentId", String.valueOf(experimentId));
			params.put("runNumber", String.valueOf(runNumber));
			params.put("type", String.valueOf(type));
			params.put("pdbfilepath", String.valueOf(pdbfilepath));
			params.put("firfilepath", String.valueOf(firfilepath));
			params.put("logfilepath", String.valueOf(logfilepath));
			params.put("rg", String.valueOf(rg));
			params.put("dmax", String.valueOf(dmax));
			params.put("volume", String.valueOf(volume));
			params.put("weight", String.valueOf(weight));
			id = this.logInit("addModel", new Gson().toJson(params));
		} catch (Exception exp) {
			id = this.logInit("addModel");
			exp.printStackTrace();
		}
		try {
			/** To be added on the database **/
			// model.setWeight(weight);
			ATSASPipeline3Service atsasPipeline3Service = (ATSASPipeline3Service) ejb3ServiceLocator
					.getLocalService(ATSASPipeline3Service.class);
			/**
			 * First we should check that for this subtraction and runNumber this model doesn't exist yet otherwise we will update it
			 */
			Model3VO model = atsasPipeline3Service.getModel(experimentId, runNumber, pdbfilepath);
			/** It is an update of the model **/
			if (model != null) {
				model.setPdbFile(pdbfilepath);
				model.setFirFile(firfilepath);
				model.setLogFile(logfilepath);
				model.setRg(rg);
				model.setDmax(dmax);
				model.setVolume(volume);
				atsasPipeline3Service.merge(model);
			} else {
				/** It is a new model and maybe the abinitio modelling doesnt exist yet **/
				model = new Model3VO();
				model.setPdbFile(pdbfilepath);
				model.setFirFile(firfilepath);
				model.setLogFile(logfilepath);
				model.setRg(rg);
				model.setDmax(dmax);
				model.setVolume(volume);
			}
			ArrayList<Model3VO> models3VO = new ArrayList<Model3VO>();
			if (type.toUpperCase().equals("MODEL")) {
				models3VO.add(model);
				atsasPipeline3Service.addAbinitioModelling(experimentId, runNumber, models3VO, null, null);
			}

			if (type.toUpperCase().equals("REFERENCE")) {
				atsasPipeline3Service.addAbinitioModelling(experimentId, runNumber, models3VO, model, null);
			}

			if (type.toUpperCase().equals("REFINED")) {
				atsasPipeline3Service.addAbinitioModelling(experimentId, runNumber, models3VO, null, model);
			}

			logFinish("addModel", id);
		} catch (Exception e) {
			LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "addModel", id, System.currentTimeMillis(),
					e.getMessage(), e);
			e.printStackTrace();
			throw e;
		}

	}

	@WebResult(name = "addMixtureAnalysis")
	public void addMixtureAnalysis(
			@WebParam(name = "experimentId") String experimentId,
			@WebParam(name = "runNumber") String runNumber,
			@WebParam(name = "fitFilePath") String fitFilePath,/* {filePath:'/data1.pdb'}*/
			@WebParam(name = "aprioriPdbFilePath") String pdb /* {filePath: "dimer.pdb", volumeFraction: "0.2"} */
	) throws Exception {

		/** Logging **/
		long id = 0;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("experimentId", String.valueOf(experimentId));
			params.put("runNumber", String.valueOf(runNumber));
			params.put("fitFilePath", String.valueOf(fitFilePath));
			params.put("aprioriPdbFilePath", String.valueOf(pdb));

			id = this.logInit("addMixtureAnalysis", new Gson().toJson(params));

			ATSASPipeline3Service atsasPipeline3Service = (ATSASPipeline3Service) ejb3ServiceLocator.getLocalService(ATSASPipeline3Service.class);
			atsasPipeline3Service.addMixture(experimentId, runNumber, fitFilePath, pdb);
			logFinish("addMixtureAnalysis", id);

		} catch (Exception e) {
			LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "addMixtureAnalysis", id, System.currentTimeMillis(),
					e.getMessage(), e);
			e.printStackTrace();
			throw e;
		}

	}

	@WebResult(name = "addSuperposition")
	public void addSuperposition(
			@WebParam(name = "experimentId") String experimentId,
			@WebParam(name = "runNumber") String runNumber,
			@WebParam(name = "abinitioModelPdbFilePath") String abitioModelPdbFilePath,
			@WebParam(name = "aprioriPdbFilePath") String aprioriPdbFilePath,
			@WebParam(name = "alignedPdbFilePath") String alignedPdbFilePath) throws Exception {

		/** Logging **/
		long id = 0;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("experimentId", String.valueOf(experimentId));
			params.put("runNumber", String.valueOf(runNumber));
			params.put("abinitioModelPdbFilePath", String.valueOf(abitioModelPdbFilePath));
			params.put("aprioriPdbFilePath", String.valueOf(aprioriPdbFilePath));
			params.put("alignedPdbFilePath", String.valueOf(alignedPdbFilePath));
			id = this.logInit("addSuperposition", new Gson().toJson(params));

			ATSASPipeline3Service atsasPipeline3Service = (ATSASPipeline3Service) ejb3ServiceLocator.getLocalService(ATSASPipeline3Service.class);
			atsasPipeline3Service.addSuperposition(experimentId, runNumber, abitioModelPdbFilePath, aprioriPdbFilePath, alignedPdbFilePath);

			logFinish("addSuperposition", id);
		} catch (Exception e) {
			LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "addSuperposition", id, System.currentTimeMillis(),
					e.getMessage(), e);
			e.printStackTrace();
			throw e;
		}
	}

	@WebResult(name = "addRigidBodyModel")
	public void addRigidBodyModel(
			@WebParam(name = "experimentId") String experimentId,
			@WebParam(name = "runNumber") String runNumber,
			@WebParam(name = "fitFilePath") String fitFilePath,
			@WebParam(name = "rigidBodyModelFilePath") String rigidBodyModelFilePath,
			@WebParam(name = "logFilePath") String logFilePath,
			@WebParam(name = "curveConfigFilePath") String curveConfigFilePath,
			@WebParam(name = "subunitConfigFilePath") String subunitConfigFilePath,
			@WebParam(name = "crosscorrConfigFilePath") String crosscorrConfigFilePath,
			@WebParam(name = "contactConditionsFilePath") String contactConditionsFilePath,
			@WebParam(name = "masterSymmetry") String masterSymmetry
			) throws Exception {

		/** Logging **/
		long id = 0;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("experimentId", String.valueOf(experimentId));
			params.put("runNumber", String.valueOf(runNumber));
			params.put("fitFilePath", String.valueOf(fitFilePath));
			params.put("rigidBodyModelFilePath", String.valueOf(rigidBodyModelFilePath));
			params.put("logFilePath", String.valueOf(logFilePath));
			params.put("curveConfigFilePath", String.valueOf(curveConfigFilePath));
			params.put("subunitConfigFilePath", String.valueOf(subunitConfigFilePath));
			params.put("crosscorrConfigFilePath", String.valueOf(crosscorrConfigFilePath));
			params.put("contactConditionsFilePath", String.valueOf(contactConditionsFilePath));
			params.put("masterSymmetry", String.valueOf(masterSymmetry));

			id = this.logInit("addRigidBodyModel", new Gson().toJson(params));

			ATSASPipeline3Service atsasPipeline3Service = (ATSASPipeline3Service) ejb3ServiceLocator.getLocalService(ATSASPipeline3Service.class);
			atsasPipeline3Service.addRigidBodyModeling(experimentId, runNumber, fitFilePath, rigidBodyModelFilePath, logFilePath, curveConfigFilePath,
					subunitConfigFilePath, crosscorrConfigFilePath, contactConditionsFilePath, masterSymmetry);

			logFinish("addRigidBodyModel", id);

		} catch (Exception e) {
			LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "addRigidBodyModel", id, System.currentTimeMillis(),
					e.getMessage(), e);
			e.printStackTrace();
			throw e;
		}
	}


	@WebResult(name = "getProposalsByLoginName")
	public String getProposalsByLoginName(
			@WebParam(name = "userName") String userName
			) throws Exception {

		/** Logging **/
		long id = 0;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("userName", String.valueOf(userName));
			id = this.logInit("getProposalsByLoginName", new Gson().toJson(params));
			Proposal3Service service = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
			List<Proposal3VO> proposals = service.findProposalByLoginName(userName);

			ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

			for (Proposal3VO proposal3vo : proposals) {
				HashMap<String, String> entry = new HashMap<String, String>();
				entry.put("title", proposal3vo.getTitle());
				entry.put("code", proposal3vo.getCode());
				entry.put("number", proposal3vo.getNumber());
				entry.put("type", proposal3vo.getType());
				entry.put("proposalId", proposal3vo.getProposalId().toString());
				result.add(entry);
			}

			logFinish("getProposalsByLoginName", id);
			return new Gson().toJson(result);
		} catch (Exception e) {
			LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "getProposalsByLoginName", id, System.currentTimeMillis(),
					e.getMessage(), e);
			e.printStackTrace();
			throw e;
		}
	}


	@WebResult(name = "getExperimentListByProposal")
	public String getExperimentListByProposal(
			@WebParam(name = "code") String code, @WebParam(name = "number") String number
			) throws Exception {

		/** Logging **/
		long id = 0;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("code", String.valueOf(code));
			params.put("number", String.valueOf(number));
			id = this.logInit("getExperimentListByProposal", new Gson().toJson(params));

			String loginname = code + number;

			Proposal3Service proposal3Service = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
			List<Proposal3VO> proposals = proposal3Service.findProposalByLoginName(loginname);

			ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
			if (proposals.size() > 0){
				Experiment3Service service = (Experiment3Service) ejb3ServiceLocator.getLocalService(Experiment3Service.class);
				List<Experiment3VO> experiments = service.findByProposalId(Integer.valueOf(proposals.get(0).getProposalId()), ExperimentScope.MINIMAL);
				for (Experiment3VO experiment : experiments) {
					if (experiment.getType().toUpperCase().equals("TEMPLATE")){
						HashMap<String, String> entry = new HashMap<String, String>();
						entry.put("name", experiment.getName());
						entry.put("creationDate", experiment.getCreationDate().toString());
						entry.put("experimentId", experiment.getExperimentId().toString());
						result.add(entry);
					}
				}
			}

			logFinish("getExperimentListByProposal", id);
			return new Gson().toJson(result);
		} catch (Exception e) {
			LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "getExperimentListByProposal", id, System.currentTimeMillis(),
					e.getMessage(), e);
			e.printStackTrace();
			throw e;
		}
	}


	@WebResult(name = "getExperimentListByProposalId")
	public String getExperimentListByProposalId(
			@WebParam(name = "proposalId") String proposalId
			) throws Exception {

		/** Logging **/
		long id = 0;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("proposalId", String.valueOf(proposalId));
			id = this.logInit("getExperimentListByProposalId", new Gson().toJson(params));

			Experiment3Service service = (Experiment3Service) ejb3ServiceLocator.getLocalService(Experiment3Service.class);
			List<Experiment3VO> experiments = service.findByProposalId(Integer.valueOf(proposalId), ExperimentScope.MINIMAL);
			ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

			for (Experiment3VO experiment : experiments) {
				if (experiment.getType().toUpperCase().equals("TEMPLATE")){
					HashMap<String, String> entry = new HashMap<String, String>();
					entry.put("name", experiment.getName());
					entry.put("creationDate", experiment.getCreationDate().toString());
					entry.put("experimentId", experiment.getExperimentId().toString());
					result.add(entry);
				}
			}

			logFinish("getExperimentListByProposalId", id);
			return new Gson().toJson(result);
		} catch (Exception e) {
			LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "getExperimentListByProposalId", id, System.currentTimeMillis(),
					e.getMessage(), e);
			e.printStackTrace();
			throw e;
		}
	}


	@WebMethod(operationName = "getExperimentByProposalCode")
	public String getExperimentByProposalCode(@WebParam(name = "code") String code, @WebParam(name = "number") String number)
			throws Exception {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("code", String.valueOf(code));
		params.put("number", String.valueOf(number));
		long id = this.logInit("getExperimentByProposalCode", new Gson().toJson(params));
		try {
			BiosaxsServices biosaxsWebServiceActions = new BiosaxsServices();
			List<Map<String, Object>> result = biosaxsWebServiceActions.findExperimentInformationByProposal(StringUtils.getProposalCode(code), number, "TEMPLATE");
			this.logFinish("getExperimentByProposalCode", id);
			return new Gson().toJson(result);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "getExperimentByProposalCode", id,
					System.currentTimeMillis(), e.getMessage(), e);
		}
		return null;
	}

	@WebMethod(operationName = "getDescriptionByExperimentId")
	@WebResult(name = "Experiment")
	public String getDescriptionByExperimentId(@WebParam(name = "experimentId") Integer experimentId) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("experimentId", String.valueOf(experimentId));
		long id = this.logInit("getDescriptionByExperimentId", new Gson().toJson(params));
		try {
			Experiment3Service service = (Experiment3Service) ejb3ServiceLocator.getLocalService(Experiment3Service.class);
			List<Map<String, Object>> robotXMLList = service.getExperimentDescription(experimentId);
			String robotXML = new GsonBuilder().serializeNulls().create().toJson(robotXMLList);
			this.logFinish("getDescriptionByExperimentId", id);
			return robotXML;
		} catch (Exception e) {
			LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "getDescriptionByExperimentId", id,
					System.currentTimeMillis(), e.getMessage(), e);
		}
		return null;
	}


	/** Loggers **/
	protected long logInit(String methodName) {
		log.info("-----------------------");
		this.now = System.currentTimeMillis();
		log.info(methodName.toUpperCase());
		LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS, methodName, System.currentTimeMillis(),
				System.currentTimeMillis(), "");
		return this.now;
	}

	/** Loggers **/
	protected long logInit(String methodName, String params) {
		log.info("-----------------------");
		this.now = System.currentTimeMillis();
		log.info(methodName.toUpperCase());
		LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS, methodName, System.currentTimeMillis(),
				System.currentTimeMillis(), params);
		return this.now;
	}

	protected long logInit(String methodName, LoggerFormatter.Package packageName) {
		log.info("-----------------------");
		this.now = System.currentTimeMillis();
		log.info(methodName.toUpperCase());
		LoggerFormatter.log(log, packageName, methodName, System.currentTimeMillis(), System.currentTimeMillis(), "");
		return this.now;

	}

	protected void logFinish(String methodName, long id) {
		log.debug("### [" + methodName.toUpperCase() + "] Execution time was " + (System.currentTimeMillis() - this.now) + " ms.");
		LoggerFormatter.log(log, LoggerFormatter.Package.BIOSAXS_WS, methodName, id, System.currentTimeMillis(),
				System.currentTimeMillis() - this.now);

	}
}
