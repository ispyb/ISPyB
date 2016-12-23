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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.Holder;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ws.api.annotation.WebContext;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.biosaxs.services.BiosaxsServices;
import ispyb.server.biosaxs.services.core.ExperimentScope;
import ispyb.server.biosaxs.services.core.experiment.Experiment3Service;
import ispyb.server.biosaxs.services.webservice.ATSASPipeline3Service;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.datacollection.MeasurementTodataCollection3VO;
import ispyb.server.biosaxs.vos.datacollection.Model3VO;
import ispyb.server.biosaxs.vos.datacollection.SaxsDataCollection3VO;
import ispyb.server.common.util.ISPyBRuntimeException;
import ispyb.server.common.util.LoggerFormatter;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.ws.ResultCode;
import ispyb.ws.soap.common.WSUtils;

@WebService(name = "ToolsForAssemblyWithResultCodeWebService", serviceName = "ispybWS", targetNamespace = "http://ispyb.ejb3.webservices.biosaxs")
@SOAPBinding(style = Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Stateless
@RolesAllowed({ "WebService", "User", "Industrial" })
// allow special access roles
@SecurityDomain("ispyb")
@WebContext(authMethod = "BASIC", secureWSDLAccess = false, transportGuarantee = "NONE")
public class ToolsForAssemblyWithResultCodeWebService {

	private final static Logger LOGGER = Logger.getLogger(ToolsForAssemblyWithResultCodeWebService.class);
	
	protected final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private long now;

	@WebMethod(operationName = "echo")
	@WebResult(name = "echo")
	public String echo() {
		return "echo from server...";
	}

	/***********************************************
	 * HPLC
	 ***********************************************/
	@WebMethod(operationName = "createHPLC")
	@WebResult(name = "experimentId")
	public Integer createHPLC(@WebParam(name = "proposalCode") String proposalCode,
			@WebParam(name = "proposalNumber") String proposalNumber, @WebParam(name = "name") String name, 
			@WebParam(name = "resultCode", mode = WebParam.Mode.OUT) Holder<String> resultCode,
			@WebParam(name = "resultDescription", mode = WebParam.Mode.OUT) Holder<String> resultDescription) {
		/** Logging params **/
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("proposalCode", String.valueOf(proposalCode));
		params.put("proposalNumber", String.valueOf(proposalNumber));
		params.put("name", String.valueOf(name));
		long start = this.logInit("createHPLC", new Gson().toJson(params));
		
		LOGGER.info("-----------------------");
		LOGGER.info(" proposalCode:\t '" + proposalCode + "'");
		LOGGER.info(" proposalNumber:\t" + proposalNumber);
		LOGGER.info(" name:\t" + name);
		
		try {
			// Retrieve proposals from SUN set or SMIS
			if (Constants.SITE_IS_SOLEIL()) {
				WSUtils.UpdateProposal(proposalCode, proposalNumber);
			}
			
			BiosaxsServices biosaxsWebServiceActions = new BiosaxsServices();
			Integer experimentId = biosaxsWebServiceActions.createHPLC(proposalCode, proposalNumber, name);
			if (experimentId == null) {
				resultCode.value = ResultCode.UNKNOWN_PROPOSAL.getCode();
				resultDescription.value = ResultCode.UNKNOWN_PROPOSAL.getDescription(proposalCode, proposalNumber);
			}
			this.logFinish("createHPLC", start);
			return experimentId;
		} catch (Exception e) {
			e.printStackTrace();
			resultCode.value = ResultCode.UNKNOWN_ERROR.getCode();
			resultDescription.value = ResultCode.UNKNOWN_ERROR.getDescription(e.getMessage());
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "createHPLC", start, System.currentTimeMillis(),
					e.getMessage(), e);
		}
		
		return null;
	}

	@WebMethod(operationName = "storeHPLC")
	@WebResult(name = "experimentId")
	public Integer storeHPLC(@XmlElement(required=true) @WebParam(name = "experimentId") String experimentId,
			@WebParam(name = "sourceFilePath") String h5FilePath, @WebParam(name = "json") String jsonFilePath, 
			@WebParam(name = "resultCode", mode = WebParam.Mode.OUT) Holder<String> resultCode,
			@WebParam(name = "resultDescription", mode = WebParam.Mode.OUT) Holder<String> resultDescription) {

		/** Logging params **/
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("experimentId", String.valueOf(experimentId));
		params.put("sourceFilePath", String.valueOf(h5FilePath));
		params.put("json", String.valueOf(jsonFilePath));
		long start = this.logInit("storeHPLC", new Gson().toJson(params));
		
		LOGGER.info("-----------------------");
		LOGGER.info(" experimentId:\t '" + experimentId + "'");
		LOGGER.info(" h5FilePath:\t" + h5FilePath);
		LOGGER.info(" jsonFilePath:\t" + jsonFilePath);
		
		try {
			BiosaxsServices biosaxsWebServiceActions = new BiosaxsServices();
			Integer id = biosaxsWebServiceActions.storeHPLC(experimentId, h5FilePath, jsonFilePath);
			if (id == null) {
				resultCode.value = ResultCode.UNKNOWN_EXPERIMENT.getCode();
				resultDescription.value = ResultCode.UNKNOWN_EXPERIMENT.getDescription(experimentId);
			}
			this.logFinish("storeHPLC", start);
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			resultCode.value = ResultCode.UNKNOWN_ERROR.getCode();
			resultDescription.value = ResultCode.UNKNOWN_ERROR.getDescription(e.getMessage());
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "storeHPLC", start, System.currentTimeMillis(),
					e.getMessage(), e);
		}
		return null;
	}

	/**
	 * It add all the models, damaver, damfilt and dammin to a measurementId which should already have got a subtraction
	 * 
	 * @param measurementId
	 * @param models
	 * @param dammaver
	 * @param dammif
	 * @param damming
	 * @param nsdPlot
	 * @param chi2plot
	 */
	@WebMethod(operationName = "storeHPLCAbInitioModelsByPeakNumber")
	@WebResult(name = "storeHPLCAbInitioModelsByPeakNumber")
	public void storeHPLCAbInitioModelsByPeakNumber(@WebParam(name = "id") String experimentId,
			@WebParam(name = "peakNumber") String peakNumber, @WebParam(name = "models") String models,
			@WebParam(name = "dammaver") String dammaver, @WebParam(name = "dammif") String dammif,
			@WebParam(name = "damming") String damming, @WebParam(name = "nsdPlot") String nsdPlot,
			@WebParam(name = "chi2plot") String chi2plot) {
		long start = this.logInit("storeHPLCAbInitioModelsByPeakNumber");
		LOGGER.info("-----------------------");
		LOGGER.info(" storeAbInitioModels");
		LOGGER.info(" experimentId:\t" + experimentId);
		LOGGER.info(" peakNumber:\t" + peakNumber);
		LOGGER.info(" models:\t" + models);
		LOGGER.info(" dammaver:\t" + dammaver);
		LOGGER.info(" dammif:\t" + dammif);
		LOGGER.info(" damming:\t" + damming);
		LOGGER.info(" nsdPlot:\t" + nsdPlot);
		LOGGER.info(" chi2plot:\t" + chi2plot);
		
		Experiment3VO experiment = getExperimentById(Integer.valueOf(experimentId), new Holder<String>(), new Holder<String>());

		StringBuffer measurementIds = new StringBuffer();
		measurementIds.append("[");
		for (SaxsDataCollection3VO data : experiment.getDataCollections()) {
			for (MeasurementTodataCollection3VO measurement : data.getMeasurementtodatacollection3VOs()) {
				measurementIds.append((measurementIds.length() == 1 ? "\"" : "\",\"") + measurement.getMeasurementId());
			}
		}		
		measurementIds.append("\"]");
		storeAbInitioModels(measurementIds.toString(), models, dammaver, dammif, damming, nsdPlot, chi2plot, new Holder<String>(), new Holder<String>());
		this.logFinish("storeHPLCAbInitioModelsByPeakNumber", start);
	}

	/** STORE RESULTS FROM EDNA PIPELINE **/
	@WebMethod(operationName = "storeHPLCDataAnalysisResult")
	@WebResult(name = "measurementId")
	public Integer storeHPLCDataAnalysisResult(
			@XmlElement(required=true) @WebParam(name = "experimentId") String experimentId,
			/** XSDataAutoRg **/
			@WebParam(name = "filename") String filename, // Deprecated
			@WebParam(name = "rg") String rg, @WebParam(name = "rgStdev") String rgStdev, @WebParam(name = "i0") String i0,
			@WebParam(name = "i0Stdev") String i0Stdev, @WebParam(name = "firstPointUsed") String firstPointUsed,
			@WebParam(name = "lastPointUsed") String lastPointUsed, @WebParam(name = "quality") String quality,
			@WebParam(name = "isagregated") String isagregated,
			/** XSDataBiosaxsSample **/
			@WebParam(name = "code") String code, @WebParam(name = "concentration") String concentration,
			/** XSDataGnom **/
			@WebParam(name = "gnomFile") String gnomFile, @WebParam(name = "rgGuinier") String rgGuinier,
			@WebParam(name = "rgGnom") String rgGnom, @WebParam(name = "dmax") String dmax, @WebParam(name = "total") String total,
			/** Volume **/
			@WebParam(name = "volume") String volume,
			/** Frames **/
			@WebParam(name = "frameStart") String frameStart, @WebParam(name = "frameEnd") String frameEnd,
			/** Curves array **/
			@WebParam(name = "curvesFilePathArray") String curveParam,
			/** Some extra params **/
			@WebParam(name = "bestBufferFilePath") String bestBufferFilePath,
			@WebParam(name = "scatterFilePath") String scatteringFilePath, @WebParam(name = "guinierFilePath") String guinierFilePath,
			@WebParam(name = "kratkyFilePath") String kratkyFilePath, @WebParam(name = "densityPlot") String densityPlot,
			@WebParam(name = "samples") String samples, 
			@WebParam(name = "resultCode", mode = WebParam.Mode.OUT) Holder<String> resultCode,
			@WebParam(name = "resultDescription", mode = WebParam.Mode.OUT) Holder<String> resultDescription) {

		/** Logging params **/
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("experimentId", String.valueOf(experimentId));
		params.put("filename", String.valueOf(filename));
		params.put("rg", String.valueOf(rg));
		params.put("rgStdev", String.valueOf(rgStdev));
		params.put("i0", String.valueOf(i0));
		params.put("i0Stdev", String.valueOf(i0Stdev));
		params.put("firstPointUsed", String.valueOf(firstPointUsed));
		params.put("lastPointUsed", String.valueOf(lastPointUsed));
		params.put("quality", String.valueOf(quality));
		params.put("isagregated", String.valueOf(isagregated));
		params.put("code", String.valueOf(code));
		params.put("concentration", String.valueOf(concentration));
		params.put("gnomFile", String.valueOf(gnomFile));
		params.put("rgGuinier", String.valueOf(rgGuinier));
		params.put("rgGnom", String.valueOf(rgGnom));
		params.put("dmax", String.valueOf(dmax));
		params.put("total", String.valueOf(total));
		params.put("volume", String.valueOf(volume));
		params.put("frameStart", String.valueOf(frameStart));
		params.put("frameEnd", String.valueOf(frameEnd));
		params.put("curveParam", String.valueOf(curveParam));
		params.put("bestBufferFilePath", String.valueOf(bestBufferFilePath));
		params.put("scatteringFilePath", String.valueOf(scatteringFilePath));
		params.put("guinierFilePath", String.valueOf(guinierFilePath));
		params.put("kratkyFilePath", String.valueOf(kratkyFilePath));
		params.put("densityPlot", String.valueOf(densityPlot));
		params.put("samples", String.valueOf(samples));
		long start = this.logInit("storeHPLCDataAnalysisResult", new Gson().toJson(params));
		
		
		LOGGER.info("-----------------------");
		LOGGER.info(" storeHPLCDataAnalysisResult");
		LOGGER.info(" experimentId:\t '" + experimentId + "'");
		LOGGER.info(" experimentId Integer:\t" + Integer.valueOf(experimentId));
		LOGGER.info(" filename:\t" + filename);
		LOGGER.info(" rg:\t" + rg);
		LOGGER.info(" rgStdev:\t" + rgStdev);
		LOGGER.info(" i0:\t" + i0);
		LOGGER.info(" i0Stdev:\t" + i0Stdev);
		LOGGER.info(" firstPointUsed:\t" + firstPointUsed);
		LOGGER.info(" lastPointUsed:\t" + lastPointUsed);
		LOGGER.info(" quality:\t" + quality);
		LOGGER.info(" isagregated:\t" + isagregated);
		LOGGER.info(" code:\t" + code);
		LOGGER.info(" concentration:\t" + concentration);
		LOGGER.info(" gnomFile:\t" + gnomFile);
		LOGGER.info(" rgGuinier:\t" + rgGuinier);
		LOGGER.info(" rgGnom:\t" + rgGnom);
		LOGGER.info(" dmax:\t" + dmax);
		LOGGER.info(" total:\t" + total);
		LOGGER.info(" volume:\t" + volume);
		LOGGER.info(" frameStart:\t" + frameStart);
		LOGGER.info(" frameEnd:\t" + frameEnd);
		LOGGER.info(" curveParam:\t" + curveParam);
		LOGGER.info(" bestBufferFilePath:\t" + bestBufferFilePath);
		LOGGER.info(" scatteringFilePath:\t" + scatteringFilePath);
		LOGGER.info(" guinierFilePath:\t" + guinierFilePath);
		LOGGER.info(" kratkyFilePath:\t" + kratkyFilePath);
		LOGGER.info(" densityPlot:\t" + densityPlot);
		LOGGER.info(" samples:\t '" + samples + "'");
		
		try {
			/**
			 * Parsing samples in json format to ArrayList<HashMap<String, String>>
			 **/
			Gson gson = new Gson();
			Type mapType = new TypeToken<ArrayList<HashMap<String, String>>>() {}.getType();
			ArrayList<HashMap<String, String>> json = gson.fromJson(samples, mapType);
			
			BiosaxsServices biosaxsWebServiceActions = new BiosaxsServices();
			Integer expId = experimentId != null && experimentId.length() > 0 ? Integer.valueOf(experimentId) : 5500 ;
			LOGGER.info(" experimentId:\t '" + experimentId + "'");
			Integer measurementId = biosaxsWebServiceActions.saveHPLCCurveAnalysis(expId,
					Integer.parseInt(frameStart), Integer.parseInt(frameEnd), curveParam, rg, rgStdev, i0, i0Stdev, firstPointUsed,
					lastPointUsed, quality, isagregated, code, concentration, gnomFile, rgGuinier, rgGnom, dmax, total, volume,
					scatteringFilePath, guinierFilePath, kratkyFilePath, densityPlot, json);

			this.logFinish("storeHPLCDataAnalysisResult", start);
			return measurementId;
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "storeHPLCDataAnalysisResult", start,
					System.currentTimeMillis(), e.getMessage(), e);
			resultCode.value = ResultCode.UNKNOWN_ERROR.getCode();
			resultDescription.value = ResultCode.UNKNOWN_ERROR.getDescription(e.getMessage());
		}
		return null;
	}

	/**
	 * It add all the models, damaver, damfilt and dammin to a measurementId which should already have got a subtraction
	 * 
	 * @param measurementId
	 * @param models
	 * @param dammaver
	 * @param dammif
	 * @param dammin
	 * @param nsdPlot
	 * @param chi2plot
	 */
	@WebMethod(operationName = "storeAbInitioModels")
	@WebResult(name = "storeAbInitioModels")
	public void storeAbInitioModels(@WebParam(name = "measurementId") String measurementId, @WebParam(name = "models") String models,
			@WebParam(name = "dammaver") String dammaver, @WebParam(name = "dammif") String dammif,
			@WebParam(name = "dammin") String dammin, @WebParam(name = "nsdPlot") String nsdPlot,
			@WebParam(name = "chi2plot") String chi2plot, 
			@WebParam(name = "resultCode", mode = WebParam.Mode.OUT) Holder<String> resultCode,
			@WebParam(name = "resultDescription", mode = WebParam.Mode.OUT) Holder<String> resultDescription) {

		/** Logging params **/
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("measurementId", String.valueOf(measurementId));
		params.put("models", String.valueOf(models));
		params.put("dammaver", String.valueOf(dammaver));
		params.put("dammif", String.valueOf(dammif));
		params.put("dammin", String.valueOf(dammin));
		params.put("nsdPlot", String.valueOf(nsdPlot));
		params.put("chi2plot", String.valueOf(chi2plot));
		
		
		LOGGER.info("-----------------------");
		LOGGER.info(" storeAbInitioModels");
		LOGGER.info(" measurementId:\t '" + measurementId + "'");
		LOGGER.info(" models:\t '" + models + "'");
		LOGGER.info(" dammaver:\t '" + dammaver + "'");
		LOGGER.info(" dammif:\t '" + dammif + "'");
		LOGGER.info(" dammin:\t '" + dammin + "'");
		LOGGER.info(" nsdPlot:\t '" + nsdPlot + "'");
		LOGGER.info(" chi2plot:\t '" + chi2plot + "'");
		
		long start = this.logInit("storeAbInitioModels", new Gson().toJson(params));
		try {
			Type measurementIdType = new TypeToken<ArrayList<Integer>>() {}.getType();
			ArrayList<Integer> measurementIds = new Gson().fromJson(measurementId, measurementIdType);

			Type mapType = new TypeToken<ArrayList<HashMap<String, String>>>() {}.getType();
			ArrayList<HashMap<String, String>> modelList = new Gson().fromJson(models, mapType);
			Type mapTypeModel = new TypeToken<HashMap<String, String>>() {}.getType();
			
			HashMap<String, String> dammaverModelHash = new Gson().fromJson(dammaver, mapTypeModel);
			HashMap<String, String> dammifModelHash = new Gson().fromJson(dammif, mapTypeModel);
			HashMap<String, String> dammingModelHash = new Gson().fromJson(dammin, mapTypeModel);

			ArrayList<Model3VO> models3VO = new ArrayList<Model3VO>();
			for (HashMap<String, String> model : modelList) {
				models3VO.add(Model3VO.deserialize(model));
			}

			Model3VO dammaverModel = Model3VO.deserialize(dammaverModelHash);
			Model3VO dammifModel = Model3VO.deserialize(dammifModelHash);
			Model3VO damminModel = Model3VO.deserialize(dammingModelHash);

			BiosaxsServices biosaxsWebServiceActions = new BiosaxsServices();
			biosaxsWebServiceActions.addAbinitioModeling(measurementIds, models3VO, dammaverModel, dammifModel, damminModel, nsdPlot, chi2plot);
			this.logFinish("storeAbInitioModels", start);
			
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof ISPyBRuntimeException) {
				resultCode.value = ResultCode.RUNTIME_ERROR.getCode();
				resultDescription.value = ResultCode.RUNTIME_ERROR.getDescription(e.getMessage());
			} else {
				resultCode.value = ResultCode.UNKNOWN_ERROR.getCode();
				resultDescription.value = ResultCode.UNKNOWN_ERROR.getDescription(e.getMessage());
			}
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "storeAbInitioModels", start,
					System.currentTimeMillis(), e.getMessage(), e);
		}
	}

	@WebResult(name = "addAveraged")
	public void addAveraged(@WebParam(name = "measurementId") String measurementId,
			@WebParam(name = "dataCollectionOrder") String dataCollectionOrder, @WebParam(name = "averaged") String averaged,
			@WebParam(name = "discarded") String discarded, @WebParam(name = "averageFile") String averageFile, 
			@WebParam(name = "resultCode", mode = WebParam.Mode.OUT) Holder<String> resultCode,
			@WebParam(name = "resultDescription", mode = WebParam.Mode.OUT) Holder<String> resultDescription) throws Exception {

		/** Logging **/
		long id = 0;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("measurementId", String.valueOf(measurementId));
			params.put("dataCollectionOrder", String.valueOf(dataCollectionOrder));
			params.put("averaged", String.valueOf(averaged));
			params.put("discarded", String.valueOf(discarded));
			params.put("averageFile", String.valueOf(averageFile));
			
			
			LOGGER.info("-----------------------");
			LOGGER.info(" addAveraged");
			LOGGER.info(" measurementId:\t '" + measurementId + "'");
			LOGGER.info(" averaged:\t '" + averaged + "'");
			LOGGER.info(" discarded:\t '" + discarded + "'");
			LOGGER.info(" averageFile:\t '" + averageFile + "'");
			
			id = this.logInit("addAveraged", new Gson().toJson(params));
		} catch (Exception exp) {
			id = this.logInit("addAveraged");
			exp.printStackTrace();
		}

		try {
			BiosaxsServices biosaxsWebServiceActions = new BiosaxsServices();
			biosaxsWebServiceActions.addAveraged(measurementId, dataCollectionOrder, averaged, discarded, averageFile);

			logFinish("addAveraged", id);
		} catch (Exception e) {
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "addAveraged", id, System.currentTimeMillis(),
					e.getMessage(), e);
			e.printStackTrace();
			resultCode.value = ResultCode.UNKNOWN_ERROR.getCode();
			resultDescription.value = ResultCode.UNKNOWN_ERROR.getDescription(e.getMessage());
		}
	}

	@WebMethod(operationName = "addSubtraction")
	public void addSubtraction(@XmlElement(required=true) @WebParam(name = "measurementId") String measurementId,
			/** XSDataAutoRg **/
			@WebParam(name = "rgGuinier") String rgGuinier, @WebParam(name = "rgStdev") String rgStdev, @WebParam(name = "i0") String i0,
			@WebParam(name = "i0Stdev") String i0Stdev,
			@WebParam(name = "firstPointUsed") String firstPointUsed, @WebParam(name = "lastPointUsed") String lastPointUsed,
			@WebParam(name = "quality") String quality, @WebParam(name = "isagregated") String isagregated,
			/** XSDataGnom **/
			@WebParam(name = "rgGnom") String rgGnom, @WebParam(name = "dmax") String dmax, @WebParam(name = "total") String total,
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
			@WebParam(name = "gnomOutputFilePath") String gnomOutputFilePath, 
			@WebParam(name = "resultCode", mode = WebParam.Mode.OUT) Holder<String> resultCode,
			@WebParam(name = "resultDescription", mode = WebParam.Mode.OUT) Holder<String> resultDescription)
	throws Exception {

		/** Logging **/
		long id = 0;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("measurementId", String.valueOf(measurementId));
			params.put("rgGuinier", String.valueOf(rgGuinier));
			params.put("rgStdev", String.valueOf(rgStdev));
			params.put("i0", String.valueOf(i0));
			params.put("i0Stdev", String.valueOf(i0Stdev));
			params.put("firstPointUsed", String.valueOf(firstPointUsed));
			params.put("lastPointUsed", String.valueOf(lastPointUsed));
			params.put("quality", String.valueOf(quality));
			params.put("isagregated", String.valueOf(isagregated));
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
			
			LOGGER.info("-----------------------");
			LOGGER.info(" addSubtraction");
			LOGGER.info(" measurementId:\t '" + measurementId + "'");
			LOGGER.info(" rgGuinier:\t '" + rgGuinier + "'");
			LOGGER.info(" rgStdev:\t '" + rgStdev + "'");
			LOGGER.info(" i0:\t '" + i0 + "'");
			LOGGER.info(" i0Stdev:\t '" + i0Stdev + "'");
			LOGGER.info(" firstPointUsed:\t '" + firstPointUsed + "'");
			LOGGER.info(" lastPointUsed:\t '" + lastPointUsed + "'");
			LOGGER.info(" quality:\t '" + quality + "'");
			LOGGER.info(" isagregated:\t '" + isagregated + "'");
			LOGGER.info(" rgGnom:\t '" + rgGnom + "'");
			LOGGER.info(" dmax:\t '" + dmax + "'");
			LOGGER.info(" total:\t '" + total + "'");
			LOGGER.info(" volume:\t '" + volume + "'");
			LOGGER.info(" sampleOneDimensionalFiles:\t '" + sampleOneDimensionalFiles + "'");
			LOGGER.info(" bufferOneDimensionalFiles:\t '" + bufferOneDimensionalFiles + "'");
			LOGGER.info(" sampleAverageFilePath:\t '" + sampleAverageFilePath + "'");
			LOGGER.info(" bestBufferFilePath:\t '" + bestBufferFilePath + "'");
			LOGGER.info(" subtractedFilePath:\t '" + subtractedFilePath + "'");
			LOGGER.info(" experimentalDataPlotFilePath:\t '" + experimentalDataPlotFilePath + "'");
			LOGGER.info(" densityPlotFilePath:\t '" + densityPlotFilePath + "'");
			LOGGER.info(" guinierPlotFilePath:\t '" + guinierPlotFilePath + "'");
			LOGGER.info(" kratkyPlotFilePath:\t '" + kratkyPlotFilePath + "'");
			LOGGER.info(" gnomOutputFilePath:\t '" + gnomOutputFilePath + "'");
			
			id = this.logInit("addSubtraction", new Gson().toJson(params));
		} catch (Exception exp) {
			id = this.logInit("addSubtraction");
			exp.printStackTrace();
		}

		try {
			ATSASPipeline3Service atsasPipeline3Service = (ATSASPipeline3Service) ejb3ServiceLocator
					.getLocalService(ATSASPipeline3Service.class);
			atsasPipeline3Service.addSubtraction(measurementId, rgStdev, i0, i0Stdev, firstPointUsed, lastPointUsed, quality,
					isagregated, rgGuinier, rgGnom, dmax, total, volume, sampleOneDimensionalFiles, bufferOneDimensionalFiles,
					sampleAverageFilePath, bestBufferFilePath, subtractedFilePath, experimentalDataPlotFilePath, densityPlotFilePath,
					guinierPlotFilePath, kratkyPlotFilePath, gnomOutputFilePath);

			this.logFinish("addSubtraction", id);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "addSubtraction", id, System.currentTimeMillis(),
					e.getMessage(), e);
			resultCode.value = ResultCode.UNKNOWN_ERROR.getCode();
			resultDescription.value = ResultCode.UNKNOWN_ERROR.getDescription(e.getMessage());
		}
	}

	/** STORE RESULTS FROM EDNA PIPELINE **/
	@Deprecated
	@WebMethod(operationName = "storeDataAnalysisResultByMeasurementId")
	@WebResult(name = "storeDataAnalysisResultByMeasurementId")
	public void storeDataAnalysisResultByMeasurementId(
			@WebParam(name = "measurementId") String measurementId,
			/** XSDataAutoRg **/
			@WebParam(name = "filename") String filename, // /Deprecated
			@WebParam(name = "rg") String rg, @WebParam(name = "rgStdev") String rgStdev, @WebParam(name = "i0") String i0,
			@WebParam(name = "i0Stdev") String i0Stdev, @WebParam(name = "firstPointUsed") String firstPointUsed,
			@WebParam(name = "lastPointUsed") String lastPointUsed, @WebParam(name = "quality") String quality,
			@WebParam(name = "isagregated") String isagregated,
			/** XSDataBiosaxsSample **/
			@WebParam(name = "code") String code, @WebParam(name = "concentration") String concentration,
			/** XSDataGnom **/
			@WebParam(name = "gnomFile") String gnomFile, @WebParam(name = "rgGuinier") String rgGuinier,
			@WebParam(name = "rgGnom") String rgGnom, @WebParam(name = "dmax") String dmax, @WebParam(name = "total") String total,
			/** Volume **/
			@WebParam(name = "volume") String volume,
			@WebParam(name = "framesMerged") String framesCount, @WebParam(name = "framesAverage") String framesAverage,
			/**
			 * Curves array Ex: "/data/pyarch/bm29/opd29/600/1d/" + str(measurementId) +
			 * "_00001.dat, /data/pyarch/bm29/opd29/600/1d/A_001_001.dat, /data/pyarch/bm29/opd29/600/1d/A_001_002.dat, /data/pyarch/bm29/opd29/600/1d/A_001_ave.dat"
			 * Files containing _ave.dat will be treated as results of the averaging.
			 * 
			 * **/
			@WebParam(name = "curvesFilePathArray") String curveParam,
			/**
			 * Data Collection order is the measurement index of the datacollection equivalent to the data collection order in the data
			 * base
			 **/
			@WebParam(name = "dataCollectionOrder") String dataCollectionOrderParam,
			/** Some extra params **/
			@WebParam(name = "bestBufferFilePath") String bestBufferFilePath,
			@WebParam(name = "scatterFilePath") String scatteringFilePath, @WebParam(name = "guinierFilePath") String guinierFilePath,
			@WebParam(name = "kratkyFilePath") String kratkyFilePath, @WebParam(name = "densityPlot") String densityPlot)
			throws Exception {

		long id = this.logInit("storeDataAnalysisResultByMeasurementId");
		
		LOGGER.info("-----------------------");
		LOGGER.info(" measurementId:\t '" + measurementId + "'");
		LOGGER.info(" measurementId Integer:\t" + Integer.valueOf(measurementId));
		LOGGER.info(" filename:\t" + filename);
		LOGGER.info(" rg:\t" + rg);
		LOGGER.info(" rgStdev:\t" + rgStdev);
		LOGGER.info(" i0:\t" + i0);
		LOGGER.info(" i0Stdev:\t" + i0Stdev);
		LOGGER.info(" firstPointUsed:\t" + firstPointUsed);
		LOGGER.info(" lastPointUsed:\t" + lastPointUsed);
		LOGGER.info(" quality:\t" + quality);
		LOGGER.info(" isagregated:\t" + isagregated);
		LOGGER.info(" code:\t" + code);
		LOGGER.info(" concentration:\t" + concentration);
		LOGGER.info(" gnomFile:\t" + gnomFile);
		LOGGER.info(" rgGuinier:\t" + rgGuinier);
		LOGGER.info(" rgGnom:\t" + rgGnom);
		LOGGER.info(" dmax:\t" + dmax);
		LOGGER.info(" total:\t" + total);
		LOGGER.info(" volume:\t" + volume);
		LOGGER.info(" frameStart:\t" + framesCount);
		LOGGER.info(" frameEnd:\t" + framesAverage);
		LOGGER.info(" curveParam:\t" + curveParam);
		LOGGER.info(" dataCollectionOrderParam:\t" + dataCollectionOrderParam);
		LOGGER.info(" bestBufferFilePath:\t" + bestBufferFilePath);
		LOGGER.info(" scatteringFilePath:\t" + scatteringFilePath);
		LOGGER.info(" guinierFilePath:\t" + guinierFilePath);
		LOGGER.info(" kratkyFilePath:\t" + kratkyFilePath);
		LOGGER.info(" densityPlot:\t" + densityPlot);
		
		try {
			BiosaxsServices biosaxsWebServiceActions = new BiosaxsServices();
			Integer measId = measurementId != null && measurementId.length() > 0 ? Integer.valueOf(measurementId) : 0;
			Integer dataCol = dataCollectionOrderParam != null && dataCollectionOrderParam.length() > 0 ? Integer.valueOf(dataCollectionOrderParam) : 0;
			int framesAv = framesAverage != null && framesAverage.length() > 0 ? Integer.parseInt(framesAverage) : 0;
			int framesCount_ = framesCount != null && framesCount.length() > 0 ?Integer.valueOf(framesCount) : 0;
			
			LOGGER.info(" measurementId:\t '" + measId + "'");
			LOGGER.info(" dataCollectionOrderParam:\t '" + dataCol + "'");
			LOGGER.info(" framesAverage:\t '" + framesAv + "'");
			
			biosaxsWebServiceActions.saveCurveAnalysis(
					measId, 
					dataCol,
					framesAv, framesCount_, curveParam, rg, rgStdev, i0, i0Stdev,
					firstPointUsed, lastPointUsed, quality, isagregated, code, concentration, gnomFile, rgGuinier, rgGnom, dmax,
					total, volume, scatteringFilePath, guinierFilePath, kratkyFilePath, densityPlot);
			
//			saveCurveAnalysis(Integer measurementId, 
//					Integer dataCollectionOrderParam, 
//					int framesAverage,	int framesCount, 
//					String curveParam,	String rg, 
//					String rgStdev, String i0, String i0Stdev, 
//					String firstPointUsed, String lastPointUsed, String quality, 
//					String isagregated, String code, String concentration, String gnomFile, String rgGuinier, 
//					String rgGnom, String dmax, String total, String volume, String scatteringFilePath, 
//					String guinierFilePath, String kratkyFilePath, String densityPlot)
			
			this.logFinish("storeDataAnalysisResultByMeasurementId", id);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "storeDataAnalysisResultByMeasurementId", id,
					System.currentTimeMillis(), e.getMessage(), e);

		}
	}

	/** FIND ALL THE EXPERIMENT BY PROPOSAL CODE AND NUMBER for example: MX 1453 **/
	@WebMethod(operationName = "findExperimentByProposalCode")
	@WebResult(name = "Experiment")
	public String findExperimentByProposalCode(@XmlElement(required=true) @WebParam(name = "code") String code, @XmlElement(required=true) @WebParam(name = "number") String number, 
			@WebParam(name = "resultCode", mode = WebParam.Mode.OUT) Holder<String> resultCode,
			@WebParam(name = "resultDescription", mode = WebParam.Mode.OUT) Holder<String> resultDescription)
			throws Exception {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("code", String.valueOf(code));
		params.put("number", String.valueOf(number));
		
		
		LOGGER.info("-----------------------");
		LOGGER.info(" addAveraged");
		LOGGER.info(" code:\t '" + code + "'");
		LOGGER.info(" number:\t '" + number + "'");
		
		long id = this.logInit("findExperimentByProposalCode", new Gson().toJson(params));
		try {
			BiosaxsServices biosaxsWebServiceActions = new BiosaxsServices();
			List<Map<String, Object>> result = biosaxsWebServiceActions.findExperimentInformationByProposal(StringUtils.getProposalCode(code), number, "TEMPLATE");
			if (result == null) {
				resultCode.value = ResultCode.UNKNOWN_PROPOSAL.getCode();
				resultDescription.value = ResultCode.UNKNOWN_PROPOSAL.getDescription(code, number);
			} else if (result.isEmpty()) {
				resultCode.value = ResultCode.NO_EXPERIMENTS_FOUND.getCode();
				resultDescription.value = ResultCode.NO_EXPERIMENTS_FOUND.getDescription(code, number);
			}
			this.logFinish("findExperimentByProposalCode", id);
			return new Gson().toJson(result);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "findExperimentByProposalCode", id,
					System.currentTimeMillis(), e.getMessage(), e);
			resultCode.value = ResultCode.UNKNOWN_ERROR.getCode();
			resultDescription.value = ResultCode.UNKNOWN_ERROR.getDescription(e.getMessage());
		}
		return null;
	}

	@WebMethod(operationName = "getRobotByExperimentId")
	@WebResult(name = "Experiment")
	public String getRobotByExperimentId(@XmlElement(required=true) @WebParam(name = "experimentId") Integer experimentId, 
			@WebParam(name = "resultCode", mode = WebParam.Mode.OUT) Holder<String> resultCode,
			@WebParam(name = "resultDescription", mode = WebParam.Mode.OUT) Holder<String> resultDescription) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("experimentId", String.valueOf(experimentId));
		
		
		LOGGER.info("-----------------------");
		LOGGER.info(" addAveraged");
		LOGGER.info(" experimmentId:\t '" + experimentId + "'");
		
		long id = this.logInit("getRobotByExperimentId", new Gson().toJson(params));
		try {
			BiosaxsServices biosaxsWebServiceActions = new BiosaxsServices();
			String robotXML = biosaxsWebServiceActions.toRobotXML(experimentId);
			if (robotXML == null) {
				resultCode.value = ResultCode.UNKNOWN_EXPERIMENT.getCode();
				resultDescription.value = ResultCode.UNKNOWN_EXPERIMENT.getDescription(experimentId);
			}
			this.logFinish("getRobotByExperimentId", id);
			return robotXML;
		} catch (Exception e) {
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "getRobotByExperimentId", id,
					System.currentTimeMillis(), e.getMessage(), e);
			resultCode.value = ResultCode.UNKNOWN_ERROR.getCode();
			resultDescription.value = ResultCode.UNKNOWN_ERROR.getDescription(e.getMessage());
		}
		return null;
	}

	@WebMethod(operationName = "saveFrame")
	@WebResult(name = "Experiment")
	public void saveFrame(@XmlElement(required=true) @WebParam(name = "mode") String mode, @XmlElement(required=true) @WebParam(name = "experimentId") int experimentId,
			@XmlElement(required=true) @WebParam(name = "measurementId") String measurementId, @XmlElement(required=true) @WebParam(name = "runNumber") String runNumber,
			@WebParam(name = "exposureTemperature") String exposureTemperature,
			@WebParam(name = "storageTemperature") String storageTemperature, @WebParam(name = "timePerFrame") String timePerFrame,
			@WebParam(name = "timeStart") String timeStart, @WebParam(name = "timeEnd") String timeEnd,
			@WebParam(name = "energy") String energy, @WebParam(name = "detectorDistance") String detectorDistance,
			@WebParam(name = "edfFileArray") String edfFileArray, @WebParam(name = "snapshotCapillary") String snapshotCapillary,
			@WebParam(name = "currentMachine") String currentMachine, @WebParam(name = "tocollect") String tocollect,
			@WebParam(name = "pars") String pars, @WebParam(name = "beamCenterX") String beamCenterX,
			@WebParam(name = "beamCenterY") String beamCenterY, @WebParam(name = "radiationRelative") String radiationRelative,
			@WebParam(name = "radiationAbsolute") String radiationAbsolute, @WebParam(name = "pixelSizeX") String pixelSizeX,
			@WebParam(name = "pixelSizeY") String pixelSizeY, @WebParam(name = "normalization") String normalization,
			@WebParam(name = "transmission") String transmission, 
			@WebParam(name = "resultCode", mode = WebParam.Mode.OUT) Holder<String> resultCode,
			@WebParam(name = "resultDescription", mode = WebParam.Mode.OUT) Holder<String> resultDescription) throws Exception {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("mode", String.valueOf(mode));
		params.put("experimentId", String.valueOf(experimentId));
		params.put("measurementId", String.valueOf(measurementId));
		params.put("runNumber", String.valueOf(runNumber));
		params.put("exposureTemperature", String.valueOf(exposureTemperature));
		params.put("storageTemperature", String.valueOf(storageTemperature));
		params.put("timePerFrame", String.valueOf(timePerFrame));
		params.put("timeStart", String.valueOf(timeStart));
		params.put("timeEnd", String.valueOf(timeEnd));
		params.put("energy", String.valueOf(energy));
		params.put("detectorDistance", String.valueOf(detectorDistance));
		params.put("edfFileArray", String.valueOf(edfFileArray));
		params.put("snapshotCapillary", String.valueOf(snapshotCapillary));
		params.put("currentMachine", String.valueOf(currentMachine));
		params.put("tocollect", String.valueOf(tocollect));
		params.put("pars", String.valueOf(pars));
		params.put("beamCenterX", String.valueOf(beamCenterX));
		params.put("beamCenterY", String.valueOf(beamCenterY));
		params.put("radiationRelative", String.valueOf(radiationRelative));
		params.put("radiationAbsolute", String.valueOf(radiationAbsolute));
		params.put("pixelSizeX", String.valueOf(pixelSizeX));
		params.put("pixelSizeY", String.valueOf(pixelSizeY));
		params.put("normalization", String.valueOf(normalization));
		params.put("transmission", String.valueOf(transmission));
		
		
		LOGGER.info("-----------------------");
		LOGGER.info(" saveFrame");
		LOGGER.info(" mode:\t '" + measurementId + "'");
		LOGGER.info(" experimentId:\t '" + experimentId + "'");
		LOGGER.info(" measurementId:\t '" + measurementId + "'");
		LOGGER.info(" runNumber:\t '" + runNumber + "'");
		LOGGER.info(" exposureTemperature:\t '" + exposureTemperature + "'");
		LOGGER.info(" storageTemperature:\t '" + storageTemperature + "'");
		LOGGER.info(" timePerFrame:\t '" + timePerFrame + "'");
		LOGGER.info(" timeStart:\t '" + timeStart + "'");
		LOGGER.info(" timeEnd:\t '" + timeEnd + "'");
		LOGGER.info(" energy:\t '" + energy + "'");
		LOGGER.info(" detectorDistance:\t '" + detectorDistance + "'");
		LOGGER.info(" edfFileArray:\t '" + edfFileArray + "'");
		LOGGER.info(" snapshotCapillary:\t '" + snapshotCapillary + "'");
		LOGGER.info(" currentMachine:\t '" + currentMachine + "'");
		LOGGER.info(" tocollect:\t '" + tocollect + "'");
		LOGGER.info(" pars:\t '" + pars + "'");
		LOGGER.info(" beamCenterX:\t '" + beamCenterX + "'");
		LOGGER.info(" beamCenterY:\t '" + beamCenterY + "'");
		LOGGER.info(" radiationRelative:\t '" + radiationRelative + "'");
		LOGGER.info(" radiationAbsolute:\t '" + radiationAbsolute + "'");
		LOGGER.info(" pixelSizeX:\t '" + pixelSizeX + "'");
		LOGGER.info(" pixelSizeY:\t '" + pixelSizeY + "'");
		LOGGER.info(" normalization:\t '" + normalization + "'");
		LOGGER.info(" transmission:\t '" + transmission + "'");
		

		long id = this.logInit("saveFrame", new Gson().toJson(params));

		try {
			BiosaxsServices biosaxsWebServiceActions = new BiosaxsServices();
			biosaxsWebServiceActions.saveFrame(mode, experimentId, measurementId, runNumber, exposureTemperature, storageTemperature,
					timePerFrame, timeStart, timeEnd, energy, detectorDistance, edfFileArray, snapshotCapillary, currentMachine,
					tocollect, pars, beamCenterX, beamCenterY, radiationRelative, radiationAbsolute, pixelSizeX, pixelSizeY,
					normalization, transmission);

			this.logFinish("saveFrame", id);
		} catch (Exception e) {
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "saveFrame", id, System.currentTimeMillis(),
					e.getMessage(), e);
			resultCode.value = ResultCode.UNKNOWN_ERROR.getCode();
			resultDescription.value = ResultCode.UNKNOWN_ERROR.getDescription(e.getMessage());
		}
	}

	/**
	 * CREATES A NEW EXPERIMENT FROM BSXCUBE WHERE code: mx number: 1438 samples: [{'plate': '2', 'enable': True, 'title': 'P2-1:9',
	 * 'transmission': 20.0, 'well': '9', 'recuperate': False, 'SEUtemperature': 4.0, 'viscosity': 'Low', 'flow': False, 'comments':
	 * 'test', 'volume': 20, 'buffername': 'BSA', 'code': 'bsa', 'typen': 0, 'waittime': 0.0, 'concentration': 0.0, 'type': 'Buffer',
	 * 'row': 1}, {'plate': '2', 'enable': True, 'buffer': '', 'flow': True, 'recuperate': False, 'viscosity': 'Low', 'typen': 1,
	 * 'volume': 20, 'buffername': 'BSA', 'code': 's1', 'concentration': 1.0, 'row': 1, 'waittime': 0.0, 'title': 'P2-1:1',
	 * 'transmission': 20.0, 'SEUtemperature': 20.0, 'well': '1', 'comments': 'hello', 'type': 'Sample'}, {'plate': '2', 'enable':
	 * True, 'buffer': '', 'flow': True, 'recuperate': False, 'viscosity': 'Low', 'typen': 1, 'volume': 21, 'buffername': 'BSA',
	 * 'code': 's2', 'concentration': 1.5, 'row': 1, 'waittime': 0.0, 'title': 'P2-1:2', 'transmission': 20.0, 'SEUtemperature': 20.0,
	 * 'well': '2', 'comments': 'hi', 'type': 'Sample'}] storageTemperature: 23 mode: BeforeAndAfter extraFlowTime: 10 Other example of
	 * sample[{'plate': '2', 'enable': true, 'title': 'P2-1:9', 'transmission': 20.0, 'well': '9', 'recuperate': false,
	 * 'SEUtemperature': 4.0, 'viscosity': 'Low', 'flow': false, 'comments': 'test', 'volume': 20, 'buffername': 'BSA', 'code': 'bsa',
	 * 'typen': 0, 'waittime': 0.0, 'concentration': 0.0, 'type': 'Buffer', 'row': 1}, {'plate': '2', 'enable': true, 'buffer': '',
	 * 'flow': true, 'recuperate': false, 'viscosity': 'Low', 'typen': 1, 'volume': 20, 'buffername': 'BSA', 'code': 's1',
	 * 'concentration': 1.0, 'row': 1, 'waittime': 0.0, 'title': 'P2-1:1', 'transmission': 20.0, 'SEUtemperature': 20.0, 'well': '1',
	 * 'comments': 'hello', 'type': 'Sample'}, {'plate': '2', 'enable': true, 'buffer': '', 'flow': true, 'recuperate': false,
	 * 'viscosity': 'Low', 'typen': 1, 'volume': 21, 'buffername': 'BSA', 'code': 's2', 'concentration': 1.5, 'row': 1, 'waittime':
	 * 0.0, 'title': 'P2-1:2', 'transmission': 20.0, 'SEUtemperature': 20.0, 'well': '2', 'comments': 'hi', 'type': 'Sample'}]
	 * 
	 * TODO add also the sessionID
	 * **/
	@WebMethod(operationName = "createExperiment")
	@WebResult(name = "experiment")
	public Experiment3VO createExperiment(@XmlElement(required=true) @WebParam(name = "code") String code, @XmlElement(required=true) @WebParam(name = "number") String number,
			@WebParam(name = "samples") String samples, @WebParam(name = "storageTemperature") String storageTemperature,
			@WebParam(name = "mode") String mode, @WebParam(name = "extraFlowTime") String extraFlowTime,
			@WebParam(name = "type") String type, @WebParam(name = "sourceFilePath") String sourceFilePath,
			@WebParam(name = "name") String name, 
			@WebParam(name = "resultCode", mode = WebParam.Mode.OUT) Holder<String> resultCode,
			@WebParam(name = "resultDescription", mode = WebParam.Mode.OUT) Holder<String> resultDescription) {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("code", String.valueOf(code));
		params.put("number", String.valueOf(number));
		params.put("samples", String.valueOf(samples));
		params.put("storageTemperature", String.valueOf(storageTemperature));
		params.put("mode", String.valueOf(mode));
		params.put("extraFlowTime", String.valueOf(extraFlowTime));
		params.put("type", String.valueOf(type));
		params.put("sourceFilePath", String.valueOf(sourceFilePath));
		params.put("name", String.valueOf(name));
		
		
		LOGGER.info("-----------------------");
		LOGGER.info(" createExperiment");
		LOGGER.info(" code:\t '" + code + "'");
		LOGGER.info(" number:\t '" + number + "'");
		LOGGER.info(" samples:\t '" + samples + "'");
		LOGGER.info(" storageTemperature:\t '" + storageTemperature + "'");
		LOGGER.info(" mode:\t '" + mode + "'");
		LOGGER.info(" extraFlowTime:\t '" + extraFlowTime + "'");
		LOGGER.info(" type:\t '" + type + "'");
		LOGGER.info(" sourceFilePath:\t '" + sourceFilePath + "'");
		LOGGER.info(" name:\t '" + name + "'");

		long id = this.logInit("createExperiment", new Gson().toJson(params));
		try {
			// Update proposal from SUN set or SMIS
			if (Constants.SITE_IS_SOLEIL()) {
				WSUtils.UpdateProposal(code, number);
			}
			
			/**
			 * Parsing samples in json format to ArrayList<HashMap<String, String>>
			 **/
			Gson gson = new Gson();
			Type mapType = new TypeToken<ArrayList<HashMap<String, String>>>() {}.getType();
			ArrayList<HashMap<String, String>> json = gson.fromJson(samples, mapType);

			String comments = "";
			
			BiosaxsServices biosaxsWebServiceActions = new BiosaxsServices();
			Experiment3VO experiment = biosaxsWebServiceActions.createExperiment(StringUtils.getProposalCode(code), number, json,
					mode, storageTemperature, extraFlowTime, type, sourceFilePath, name, comments);
			if (experiment == null) {
				resultCode.value = ResultCode.UNKNOWN_PROPOSAL.getCode();
				resultDescription.value = ResultCode.UNKNOWN_PROPOSAL.getDescription(code, number);
			}
			logFinish("createExperiment", id);
			return experiment;
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "createExperiment", id, System.currentTimeMillis(),
					e.getMessage(), e);
			resultCode.value = ResultCode.UNKNOWN_ERROR.getCode();
			resultDescription.value = ResultCode.UNKNOWN_ERROR.getDescription(e.getMessage());
		}
		return null;
	}

	@WebMethod(operationName = "getExperimentById")
	@WebResult(name = "experiment")
	public Experiment3VO getExperimentById(@XmlElement(required=true) @WebParam(name = "experimentId") int experimentId, 
			@WebParam(name = "resultCode", mode = WebParam.Mode.OUT) Holder<String> resultCode,
			@WebParam(name = "resultDescription", mode = WebParam.Mode.OUT) Holder<String> resultDescription) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("experimentId", String.valueOf(experimentId));
		
		
		LOGGER.info("-----------------------");
		LOGGER.info(" getExperimentById");
		LOGGER.info(" experimentId:\t '" + experimentId + "'");
		
		long id = this.logInit("getExperimentById", new Gson().toJson(params));
		try {
			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Experiment3Service experiment3Service = (Experiment3Service) ejb3ServiceLocator.getLocalService(Experiment3Service.class);
			Experiment3VO experiment = experiment3Service.findById(experimentId, ExperimentScope.MEDIUM);
			if (experiment == null) {
				resultCode.value = ResultCode.UNKNOWN_EXPERIMENT.getCode();
				resultDescription.value = ResultCode.UNKNOWN_EXPERIMENT.getDescription(experimentId);
			}
			logFinish("getExperimentById", id);
			return experiment;
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "getExperimentById", id, System.currentTimeMillis(),
					e.getMessage(), e);
			resultCode.value = ResultCode.UNKNOWN_ERROR.getCode();
			resultDescription.value = ResultCode.UNKNOWN_ERROR.getDescription(e.getMessage());
		}
		return null;
	}

	/**
	 * It is needed to register when users click on Abort in the middle of a data collection, so expeirment will be registered as
	 * aborted
	 * 
	 * @param experimentId
	 * @throws Exception
	 * 
	 *             USE UPDATE STATUS INSTEAD
	 */
	@Deprecated
	@WebMethod(operationName = "setExperimentAborted")
	public void setExperimentAborted(@WebParam(name = "experimentId") Integer experimentId) throws Exception {
		long id = this.logInit("setExperimentAborted");
		this.updateStatus(experimentId, "ABORTED", new Holder<String>(), new Holder<String>());
		this.logFinish("setExperimentAborted", id);
	}

	@WebMethod(operationName = "updateStatus")
	public void updateStatus(@WebParam(name = "experimentId") Integer experimentId, @WebParam(name = "status") String status, 
			@WebParam(name = "resultCode", mode = WebParam.Mode.OUT) Holder<String> resultCode,
			@WebParam(name = "resultDescription", mode = WebParam.Mode.OUT) Holder<String> resultDescription)
			throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("experimentId", String.valueOf(experimentId));
		params.put("status", String.valueOf(status));
		
		long start_ = this.logInit("updateStatus");
		
		LOGGER.info("-----------------------");
		LOGGER.info(" updateStatus");
		LOGGER.info(" experimentId:\t '" + experimentId + "'");
		LOGGER.info(" status:\t '" + status + "'");
		this.logFinish("updateStatus", start_);
		
		long id = this.logInit("updateStatus", new Gson().toJson(params));
		try {
			BiosaxsServices biosaxsWebServiceActions = new BiosaxsServices();
			biosaxsWebServiceActions.updateStatus(experimentId, status);
			this.logFinish("updateStatus", id);
		} catch (Exception e) {
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "updateStatus", id, System.currentTimeMillis(),
					e.getMessage(), e);
		}
	}

	/**
	 * It was used for pointing to a zip file with all the frames but now ISPyB is generating it automatically It can be used for
	 * pointing to a nexus file
	 * 
	 * @param experimentId
	 * @param dataAcquisitionFilePath
	 * @throws Exception
	 */
	@Deprecated
	@WebMethod(operationName = "setDataAcquisitionFilePath")
	public void setDataAcquisitionFilePath(@WebParam(name = "experimentId") Integer experimentId,
			@WebParam(name = "dataAcquisitionFilePath") String dataAcquisitionFilePath) throws Exception {
		long id = this.logInit("setDataAcquisitionFilePath");
		
		long start_ = this.logInit("setDataAcquisitionFilePath");
		
		LOGGER.info("-----------------------");
		LOGGER.info(" setDataAcquisitionFilePath");
		LOGGER.info(" experimentId:\t '" + experimentId + "'");
		LOGGER.info(" dataAcquisitionFilePath:\t '" + dataAcquisitionFilePath + "'");
		this.logFinish("setDataAcquisitionFilePath", start_);
		
		try {
			LOGGER.info("\texperimentId:" + experimentId);
			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Experiment3Service experiment3Service = (Experiment3Service) ejb3ServiceLocator.getLocalService(Experiment3Service.class);
			Experiment3VO experiment = experiment3Service.findById(experimentId, ExperimentScope.MINIMAL);
			experiment.setDataAcquisitionFilePath(dataAcquisitionFilePath);
			experiment3Service.merge(experiment);
			this.logFinish("setDataAcquisitionFilePath", id);
		} catch (Exception e) {
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "setDataAcquisitionFilePath", id,
					System.currentTimeMillis(), e.getMessage(), e);
		}
	}

	/** Loggers **/
	private long logInit(String methodName) {
		LOGGER.info("-----------------------");
		this.now = System.currentTimeMillis();
		LOGGER.info(methodName.toUpperCase());
		LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS, methodName, System.currentTimeMillis(),
				System.currentTimeMillis(), "");
		return this.now;
	}

	private void logFinish(String methodName, long id) {
		LOGGER.debug("### [" + methodName.toUpperCase() + "] Execution time was " + (System.currentTimeMillis() - this.now) + " ms.");
		LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS, methodName, id, System.currentTimeMillis(),
				System.currentTimeMillis() - this.now);

	}

	protected long logInit(String methodName, String params) {
		LOGGER.info("-----------------------");
		this.now = System.currentTimeMillis();
		LOGGER.info(methodName.toUpperCase());
		LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS, methodName, System.currentTimeMillis(),
				System.currentTimeMillis(), params);
		return this.now;
	}
}
