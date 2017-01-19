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

package ispyb.client.biosaxs.dataAdapter;

import ispyb.client.biosaxs.pdf.PDFReportType;
import ispyb.client.common.util.Confidentiality;
import ispyb.client.common.util.FileUtil;
import ispyb.server.biosaxs.services.ExperimentSerializer;
import ispyb.server.biosaxs.services.core.ExperimentScope;
import ispyb.server.biosaxs.services.utils.reader.dat.FactoryProducer;
import ispyb.server.biosaxs.services.utils.reader.dat.ScatteringCurvesParser;
import ispyb.server.biosaxs.services.utils.reader.pdb.PDBFactoryProducer;
import ispyb.server.biosaxs.vos.advanced.FitStructureToExperimentalData3VO;
import ispyb.server.biosaxs.vos.advanced.RigidBodyModeling3VO;
import ispyb.server.biosaxs.vos.advanced.Superposition3VO;
import ispyb.server.biosaxs.vos.assembly.Assembly3VO;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.assembly.Stoichiometry3VO;
import ispyb.server.biosaxs.vos.assembly.Structure3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Measurement3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Specimen3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.StockSolution3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Platetype3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Sampleplate3VO;
import ispyb.server.biosaxs.vos.datacollection.AbInitioModel3VO;
import ispyb.server.biosaxs.vos.datacollection.Frame3VO;
import ispyb.server.biosaxs.vos.datacollection.Merge3VO;
import ispyb.server.biosaxs.vos.datacollection.Subtraction3VO;
import ispyb.server.biosaxs.vos.datacollection.SubtractiontoAbInitioModel3VO;
import ispyb.server.biosaxs.vos.utils.comparator.SaxsDataCollectionComparator;
import ispyb.server.common.util.LoggerFormatter;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.vos.collections.InputParameterWorkflow;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.collections.Workflow3VO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * @struts.action name="BiosaxsDataAdapterAction" path="/user/dataadapter"
 *                type="ispyb.client.biosaxs.dataAdapter.BiosaxsDataAdapterAction"
 *                validate="false" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="goToEditProject"
 *                        path="/user/editProjectJS.do?reqCode=display"
 * 
 */
public class BiosaxsDataAdapterAction extends org.apache.struts.actions.DispatchAction {
	private final static Logger LOGGER = Logger.getLogger(BiosaxsDataAdapterAction.class);

	protected static Calendar NOW = GregorianCalendar.getInstance();

	/** This class contains all the business logic **/
	BiosaxsActions biosaxsActions = new BiosaxsActions();

	private void initServices() {

	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	/**
	 * updateDataBaseFromSMIS
	 */
	public ActionForward updateDataBaseFromSMIS(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("updateDataBaseFromSMIS", request);
		try {
			/** Updating from SMIS **/
			Integer proposalId = this.getProposalBySessionId(request);
			this.biosaxsActions.updateSMISDataBase(proposalId);

			String json = BiosaxsDataAdapterCommon.getGson("yyyy MM dd").toJson(this.getProposalData(proposalId));
			this.logEnd("updateDataBaseFromSMIS", start);
			BiosaxsDataAdapterCommon.sendResponseToclient(response, json);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("updateDataBaseFromSMIS", start, exp);
			response.getWriter().write(exp.getMessage() + "  " + exp.getCause());
			response.setStatus(500);
		}
		return null;

	}

	/**
	 * mergeSpecimens
	 * 
	 * Given two specimens with same content (buffer and macromolecule), one is
	 * filled with the content of the another
	 * 
	 * 1) Check specimens are the same (same buffer, macromolecule,
	 * concentration) 2) Add specimen's volume to the target's specimen 3) Set
	 * measurements of specimen to the target 4) Remove specimen
	 * 
	 * @param specimenTargetId
	 * @param specimenOriginId
	 * 
	 * @return json(experiment)
	 */
	public ActionForward mergeSpecimens(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("mergeSpecimens", request);
		try {
			Integer targetId = Integer.parseInt(request.getParameter("specimenTargetId"));
			Integer specimenId = Integer.parseInt(request.getParameter("specimenOriginId"));
			Experiment3VO experiment = this.biosaxsActions.mergeSpecimens(targetId, specimenId);
			String json = ExperimentSerializer.serializeExperiment(experiment, ExperimentScope.MEDIUM);
			BiosaxsDataAdapterCommon.sendResponseToclient(response, json);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("mergeSpecimens", start, exp);
			response.getWriter().write(exp.getMessage() + "  " + exp.getCause());
			response.setStatus(500);
		}
		return null;

	}

	/**
	 * createTemplate
	 * 
	 * @param name
	 * @param measurements
	 *            json array with the samples [
	 *            {"SEUtemperature":"","buffer":"AA"
	 *            ,"buffername":"AA","code":"AA","comments":"","concentration"
	 *            :0,"enable":true,"flow":
	 *            true,"recuperate":false,"title":"","transmission"
	 *            :100,"macromolecule":"AA","type":"Buffer","typen"
	 *            :1,"viscosity"
	 *            :"low","volume":80,"volumeToLoad":40,"waittime":0
	 *            ,"plate":2,"row":1,"well":9}, {"SEUtemperature":4,"buffer"
	 *            :"AA"
	 *            ,"buffername":"AA","code":"A","comments":"","concentration"
	 *            :1,"enable":true,"flow":true ,"recuperate":false,"title"
	 *            :"","transmission"
	 *            :100,"macromolecule":"A","type":"Sample","typen"
	 *            :1,"viscosity":"low","volume"
	 *            :40,"volumeToLoad":40,"waittime":0,"plate":2,"row":1,"well":1}
	 *            ]
	 */
	public ActionForward createTemplate(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("createTemplate", request);
		try {
			/** Parameters **/
			Integer proposalId = BiosaxsDataAdapterCommon.getProposalId(request);
			String name = request.getParameter("name");
			String measurements = request.getParameter("measurements");

			Gson gson = new Gson();
			Type mapType = new TypeToken<ArrayList<HashMap<String, String>>>() {
			}.getType();
			ArrayList<HashMap<String, String>> samples = gson.fromJson(measurements, mapType);

			if (!request.getParameterMap().containsKey("experimentId")) {
				Experiment3VO experiment = this.biosaxsActions.createTemplate(proposalId, name, samples);
				this.logEnd("createTemplate", start);
				String json = ExperimentSerializer.serializeExperiment(experiment, ExperimentScope.MEDIUM);
				BiosaxsDataAdapterCommon.sendResponseToclient(response, json);
			} else {
				Integer experimentId = Integer.parseInt(request.getParameter("experimentId"));
				Experiment3VO experiment = this.biosaxsActions.addMeasurementsToExperiment(experimentId, proposalId, samples);
				this.logEnd("createTemplate", start);

				String json = ExperimentSerializer.serializeExperiment(experiment, ExperimentScope.MEDIUM);
				BiosaxsDataAdapterCommon.sendResponseToclient(response, json);
			}

		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("createTemplate", start, exp);
			response.getWriter().write(exp.getMessage() + "  " + exp.getCause());
			response.setStatus(500);
		}
		return null;

	}

	/**
	 * getExperiment
	 * 
	 * @param experimentId
	 * @param scope
	 *            MINIMUM, MEDIUM, MAXIMUM
	 * @return json(experiment)
	 */
	public ActionForward getExperimentById(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("getExperiment", request);
		try {
			String experimentId = request.getParameter("experimentId");
			String scope = request.getParameter("scope");
			Integer proposalId = this.getProposalBySessionId(request);
			ExperimentScope experimentScope = ExperimentScope.MINIMAL;
			if (scope != null) {

				if (scope.equals("MEDIUM")) {
					experimentScope = ExperimentScope.MEDIUM;
				}
				if (scope.equals("FULL")) {
					experimentScope = ExperimentScope.MEDIUM;
				}
			}

			/** RETRIEVING PROJECT **/
			Experiment3VO experiment = this.biosaxsActions.getExperimentById(Integer.parseInt(experimentId), experimentScope, proposalId);
			String json = "";
			if (experiment != null)
				json = ExperimentSerializer.serializeExperiment(experiment, experimentScope);
			
			System.out.println(json);
			BiosaxsDataAdapterCommon.sendResponseToclient(response, json);
			this.logEnd("getExperiment", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getExperimentById", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	public ActionForward getRigidBodyModelingBySubtractionId(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("getRigidBodyModelingBySubtractionId", request);
		try {
			int subtractionId = Integer.parseInt(request.getParameter("subtractionId"));

			List<RigidBodyModeling3VO> rigidBody = this.biosaxsActions.getRigidBodyModelingBySubtractionId(subtractionId);

			BiosaxsDataAdapterCommon.sendResponseToclient(response, BiosaxsDataAdapterCommon.getGson().toJson(rigidBody));
			this.logEnd("getRigidBodyModelingBySubtractionId", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getRigidBodyModelingBySubtractionId", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	public ActionForward getSuperpositionBySubtractionId(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("getSuperpositionBySubtractionId", request);
		try {
			int subtractionId = Integer.parseInt(request.getParameter("subtractionId"));

			List<Superposition3VO> rigidBody = this.biosaxsActions.getSuperpositionBySubtractionId(subtractionId);

			BiosaxsDataAdapterCommon.sendResponseToclient(response, BiosaxsDataAdapterCommon.getGson().toJson(rigidBody));
			this.logEnd("getSuperpositionBySubtractionId", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getRigidBodyModelingBySubtractionId", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	/**
	 * @param string
	 * @param start
	 */
	private void logEnd(String methodName, long start) {
		long time = System.currentTimeMillis();
		LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_UI, methodName, start, time, time - start);
	}

	/**
	 * getMergesByIdsList
	 * 
	 * @param mergeIdsList
	 * @return json(merges)
	 */
	public ActionForward getMergesByIdsList(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
		long start = BiosaxsDataAdapterCommon.logMethod("getMergesByIdsList", request);
		try {
			List<Integer> mergeIdList = BiosaxsDataAdapterCommon.parseToInteger(request.getParameter("mergeIdsList"));
			if (mergeIdList.size() > 30) {
				throw new Exception("Too much files to parse. Max. number of files is 30.");
			}
			List<Merge3VO> merges = this.biosaxsActions.getMergeByIds(mergeIdList);
			String json = BiosaxsDataAdapterCommon.getGson("yyyy MM dd").toJson(merges);
			this.logEnd("getMergesByIdsList", start);
			BiosaxsDataAdapterCommon.sendResponseToclient(response, json);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getMergesByIdsList", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}

		return null;
	}

	/**
	 * setMeasurementParameters
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws IOException
	 */
	public ActionForward setMeasurementParameters(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
		long start = BiosaxsDataAdapterCommon.logMethod("setMeasurementParameters", request);
		try {
			List<Integer> measurementIds = BiosaxsDataAdapterCommon.parseToInteger(request.getParameter("measurementIds"));
			/**
			 * parameter: ["Exp. Temp.", "Volume To Load", "Transmission",
			 * "Flow", "Viscosity"]
			 **/
			String parameter = request.getParameter("parameter");
			String value = request.getParameter("value");

			this.biosaxsActions.setMeasurementParameters(measurementIds, parameter, value);
			this.logEnd("setMeasurementParameters", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("setMeasurementParameters", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}

		return null;
	}

	private String readFile(String path) throws IOException {
		FileInputStream stream = new FileInputStream(new File(path));
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			/* Instead of using default, pass in a decoder. */
			return Charset.defaultCharset().decode(bb).toString();
		} finally {
			stream.close();
		}
	}

	/**
	 * getSourceFile
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws IOException
	 */
	public ActionForward getSourceFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
		long start = BiosaxsDataAdapterCommon.logMethod("getSourceFile", request);
		try {
			Integer proposalId = BiosaxsDataAdapterCommon.getProposalId(request);
			Integer experimentId = Integer.parseInt(request.getParameter("experimentId"));
			Experiment3VO experiment = this.biosaxsActions.getExperimentById(experimentId, ExperimentScope.MINIMAL, proposalId);

			String filePath = experiment.getSourceFilePath();
			filePath = BiosaxsActions.checkFilePathForDevelopment(filePath);

			File file = new File(filePath);
			if (file.exists()) {
				this.sendCSVFileReponse(file.getName(), this.readFile(filePath), response);
				return null;
			}

			this.logEnd("getSourceFile", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getSourceFile", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}

		return null;
	}

	/**
	 * @param result
	 * @param response
	 */
	private ActionForward sendCSVFileReponse(String fileName, String result, HttpServletResponse response) {
		try {
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.setContentType("text/plain");
			response.getWriter().flush();
			response.getWriter().print(result);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param result
	 * @param response
	 */
	private ActionForward sendPDFFileReponse(String fileName, ByteArrayOutputStream result, HttpServletResponse response) {
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		ByteArrayOutputStream baos = result;
		response.setContentLength(baos.size());
		ServletOutputStream sos;
		try {
			sos = response.getOutputStream();
			baos.writeTo(sos);
			sos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param result
	 * @param response
	 */
	private ActionForward sendZipFileReponse(String fileName, byte[] result, HttpServletResponse response) {
		response.setContentType("application/zip");
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		OutputStream output;
		try {
			output = response.getOutputStream();
			output.write(result);
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * getZipFileByMacromoleculeId
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws IOException
	 */
	public ActionForward getZipFileByMacromoleculeId(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
		long start = BiosaxsDataAdapterCommon.logMethod("getZipFileByMacromoleculeId", request);
		try {
			Integer proposalId = BiosaxsDataAdapterCommon.getProposalId(request);
			Integer macromoleculeId = Integer.parseInt(request.getParameter("macromoleculeId"));
			String fileName = request.getParameter("fileName");
			byte[] bytes = this.biosaxsActions.getZipFileByMacromoleculeId(macromoleculeId, proposalId);
			this.sendZipFileReponse(fileName + ".zip", bytes, response);
			this.logEnd("getZipFileByMacromoleculeId", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getZipFileByMacromoleculeId", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	public ActionForward getSubtractionByIdList(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
		long start = BiosaxsDataAdapterCommon.logMethod("getSubtractionByIdList", request);
		try {
			List<Integer> subtractionIds = BiosaxsDataAdapterCommon.parseToInteger(request.getParameter("subtractionIdList"));
			List<Subtraction3VO> subtractions = this.biosaxsActions.getSubtractionByIds(subtractionIds);
			Gson gson = BiosaxsDataAdapterCommon.getGson();
			BiosaxsDataAdapterCommon.sendResponseToclient(response, gson.toJson(subtractions));
			this.logEnd("getSubtractionByIdList", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getSubtractionByIdList", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	/**
	 * getZipFileByAverageListId
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws IOException
	 */
	public ActionForward getZipFileByAverageListId(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
		long start = BiosaxsDataAdapterCommon.logMethod("getZipFileByAverageListId", request);
		try {

			List<Merge3VO> merges = new ArrayList<Merge3VO>();
			if (request.getParameter("mergeIdsList") != null) {
				List<Integer> mergeIdsList = BiosaxsDataAdapterCommon.parseToInteger(request.getParameter("mergeIdsList"));
				merges = this.biosaxsActions.getMergeByIds(mergeIdsList);
			}

			/**
			 * This subtractionIds get the subtracted file path
			 */
			List<Subtraction3VO> subtractions = new ArrayList<Subtraction3VO>();
			if (request.getParameter("subtractionIdList") != null) {
				List<Integer> subtractionIds = BiosaxsDataAdapterCommon.parseToInteger(request.getParameter("subtractionIdList"));
				subtractions = this.biosaxsActions.getSubtractionByIds(subtractionIds);
			}

			String fileName = request.getParameter("fileName");
			byte[] bytes = this.biosaxsActions.getZipFileByAverageList(merges, subtractions);
			this.sendZipFileReponse(fileName + ".zip", bytes, response);
			this.logEnd("getZipFileByAverageListId", start);

		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getZipFileByAverageListId", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	public ActionForward getH5FrameScattering(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
		long start = BiosaxsDataAdapterCommon.logMethod("getH5FrameScattering", request);
		try {
			Integer proposalId = BiosaxsDataAdapterCommon.getProposalId(request);
			List<Integer> frames = BiosaxsDataAdapterCommon.parseToInteger(request.getParameter("frames"));
			String experimentId = request.getParameter("experimentId");
			HashMap<Integer, HashMap<String, ArrayList<String>>> result = new HashMap<Integer, HashMap<String, ArrayList<String>>>();
			for (Integer frame : frames) {
				result.put(frame, this.biosaxsActions.getH5FrameScattering(Integer.parseInt(experimentId), frame, proposalId));
			}
			Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
			BiosaxsDataAdapterCommon.sendResponseToclient(response, gson.toJson(result));
			this.logEnd("getH5FrameScattering", start);

		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getH5FrameScattering", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	public ActionForward getH5FramesMerge(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
		long start = BiosaxsDataAdapterCommon.logMethod("getH5FramesMerge", request);
		try {
			String experimentId = request.getParameter("experimentId");
			Integer proposalId = BiosaxsDataAdapterCommon.getProposalId(request);
			HashMap<String, List<List<String>>> merges = this.biosaxsActions.getSampleAverages(Integer.parseInt(experimentId), proposalId);
			Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
			BiosaxsDataAdapterCommon.sendResponseToclient(response, gson.toJson(merges));
			this.logEnd("getH5FramesMerge", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getH5FramesMerge", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	public ActionForward getH5File(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
		long start = BiosaxsDataAdapterCommon.logMethod("getH5File", request);
		try {
			Integer experimentId = Integer.parseInt(request.getParameter("experimentId"));
			Integer proposalId = BiosaxsDataAdapterCommon.getProposalId(request);
			String filePath = this.biosaxsActions.getH5FilePathByExperimentId(experimentId, proposalId);
			BiosaxsDataAdapterCommon.sendFileToClient(filePath, response, "application/octet-stream");
			this.logEnd("getH5File", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getH5File", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	public ActionForward getH5fileParameters(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
		long start = BiosaxsDataAdapterCommon.logMethod("getH5fileParameters", request);
		try {
			List<String> parameters = BiosaxsDataAdapterCommon.parseToString(request.getParameter("parameters"));
			String experimentId = request.getParameter("experimentId");
			Integer proposalId = BiosaxsDataAdapterCommon.getProposalId(request);
			HashMap<String, float[]> params = this.biosaxsActions.getH5ParametersByExperimentId(Integer.parseInt(experimentId), parameters, proposalId);
			Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
			BiosaxsDataAdapterCommon.sendResponseToclient(response, gson.toJson(params));
			this.logEnd("getH5fileParameters", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getH5fileParameters", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}

		return null;
	}

	/**
	 * getZipFileByExperimentId
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws IOException
	 */
	public ActionForward getZipFileByExperimentId(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
		long start = BiosaxsDataAdapterCommon.logMethod("getZipFileByExperimentId", request);
		try {
			Integer proposalId = BiosaxsDataAdapterCommon.getProposalId(request);
			Integer experimentId = Integer.parseInt(request.getParameter("experimentId"));
			String fileName = request.getParameter("fileName");
			byte[] bytes = this.biosaxsActions.getZipFileByExperimentId(experimentId, proposalId);
			this.sendZipFileReponse(fileName + ".zip", bytes, response);
			this.logEnd("getZipFileByExperimentId", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getZipFileByExperimentId", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}

		return null;
	}

	/**
	 * getZipFileByFramesRange
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws IOException
	 */
	public ActionForward getZipFileH5ByFramesRange(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
		long start = BiosaxsDataAdapterCommon.logMethod("getZipFileH5ByFramesRange", request);
		try {
			Integer proposalId = BiosaxsDataAdapterCommon.getProposalId(request);
			Integer experimentId = Integer.parseInt(request.getParameter("experimentId"));
			Integer startFrame = Integer.parseInt(request.getParameter("start"));
			Integer endFrame = Integer.parseInt(request.getParameter("end"));

			byte[] bytes = this.biosaxsActions.getZipFileH5ByFramesRange(experimentId, proposalId, startFrame, endFrame);
			String fileName = new File(this.biosaxsActions.getH5FilePathByExperimentId(experimentId, proposalId)).getName();
			this.sendZipFileReponse(fileName + ".zip", bytes, response);
			this.logEnd("getZipFileH5ByFramesRange", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getZipFileH5ByFramesRange", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}

		return null;
	}

	/**
	 * getReport
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws IOException
	 */
	public ActionForward getReport(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
		long start = BiosaxsDataAdapterCommon.logMethod("getReport", request);
		try {
			Integer proposalId = getProposalBySessionId(request);
			Integer experimentId = Integer.parseInt(request.getParameter("experimentId"));
			String format = request.getParameter("format");
			String filename = request.getParameter("filename");

			BiosaxsActions actions = new BiosaxsActions();
			if (format.equals("PDF")) {
				ByteArrayOutputStream result = actions.getPDFReport(proposalId, experimentId, PDFReportType.DATA_ACQUISITION);
				this.logEnd("getReport", start);
				this.sendPDFFileReponse(filename + ".pdf", result, response);
				return null;
			}

			// if (format.equals("CSV")) {
			// String result = actions.getCSVReport(proposalId, experimentId,
			// CSVReportType.DATA_ACQUISITION);
			// this.sendCSVFileReponse(filename + ".txt", result, response);
			// return null;
			// }

			this.logEnd("getReport", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getReport", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}

		return null;
	}

	/**
	 * @param getAnalysisByProposalId
	 */
	public ActionForward getAnalysisByProposalId(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
		long start = BiosaxsDataAdapterCommon.logMethod("getAnalysisByProposalId", request);
		try {
			List<Map<String, Object>> data = biosaxsActions.getAllInformationByProposalId(BiosaxsDataAdapterCommon.getProposalId(request));

			String json = BiosaxsDataAdapterCommon.getGson().toJson(data);
			this.logEnd("getAnalysisByProposalId", start);
			BiosaxsDataAdapterCommon.sendResponseToclient(response, json);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getAnalysisByProposalId", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	/**
	 * @param getInformationByMacromoleculeId
	 */
	public ActionForward getInformationByMacromoleculeId(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
		long start = BiosaxsDataAdapterCommon.logMethod("getInformationByMacromoleculeId", request);
		try {
			String specimensIds = request.getParameter("data");
			Type mapType = new TypeToken<ArrayList<HashMap<String, String>>>() {
			}.getType();
			ArrayList<HashMap<String, String>> specimensArrayIds = new Gson().fromJson(specimensIds, mapType);

			List<Map<String, Object>> data = biosaxsActions.getAllInformationByMacromoleculeId(specimensArrayIds, this.getProposalBySessionId(request));

			String json = BiosaxsDataAdapterCommon.getGson().toJson(data);
			this.logEnd("getInformationByMacromoleculeId", start);
			BiosaxsDataAdapterCommon.sendResponseToclient(response, json);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getInformationByMacromoleculeId", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	/**
	 * @param getAnalysisInformationByExperimentId
	 */
	public ActionForward getAnalysisInformationByExperimentId(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		long start = BiosaxsDataAdapterCommon.logMethod("getAnalysisInformationByExperimentId", request);
		try {
			Integer experimentId = Integer.parseInt(request.getParameter("experimentId"));
			List<Map<String, Object>> data = biosaxsActions.getAnalysisInformationByExperimentId(experimentId);

			String json = BiosaxsDataAdapterCommon.getGson().toJson(data);
			this.logEnd("getAnalysisInformationByExperimentId", start);
			BiosaxsDataAdapterCommon.sendResponseToclient(response, json);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getAnalysisInformationByExperimentId", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	/**
	 * @param getCompactAnalysisByExperimentId
	 */
	public ActionForward getCompactAnalysisByExperimentId(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
		long start = BiosaxsDataAdapterCommon.logMethod("getCompactAnalysisByExperimentId", request);
		try {
			Integer experimentId = Integer.parseInt(request.getParameter("experimentId"));
			List<Map<String, Object>> data = biosaxsActions.getCompactAnalysisInformationByExperimentId(experimentId);

			String json = BiosaxsDataAdapterCommon.getGson().toJson(data);
			this.logEnd("getCompactAnalysisByExperimentId", start);
			BiosaxsDataAdapterCommon.sendResponseToclient(response, json);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getCompactAnalysisByExperimentId", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	/**
	 * @param getCompactAnalysisBySubtractionId
	 */
	public ActionForward getCompactAnalysisBySubtractionId(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
		long start = BiosaxsDataAdapterCommon.logMethod("getCompactAnalysisBySubtractionId", request);
		try {
			Integer subtractionId = Integer.parseInt(request.getParameter("subtractionId"));
			List<Map<String, Object>> data = biosaxsActions.getCompactAnalysisBySubtractionId(subtractionId.toString());

			String json = BiosaxsDataAdapterCommon.getGson().toJson(data);
			this.logEnd("getCompactAnalysisBySubtractionId", start);
			BiosaxsDataAdapterCommon.sendResponseToclient(response, json);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getCompactAnalysisBySubtractionId", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	/**
	 * @param getPagingCompactAnalysisByProposalId
	 */
	public ActionForward getPagingCompactAnalysisByProposalId(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		long id = BiosaxsDataAdapterCommon.logMethod("getPagingCompactAnalysisByProposalId", request);
		try {
			Integer limit = Integer.parseInt(request.getParameter("limit"));
			Integer start = Integer.parseInt(request.getParameter("start"));

			List<Map<String, Object>> data = biosaxsActions.getCompactAnalysisByProposalId(this.getProposalBySessionId(request), start, limit);
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("success", "true");
			result.put("results", biosaxsActions.getCountCompactAnalysisByProposalId(this.getProposalBySessionId(request)));
			result.put("rows", data);
			String json = BiosaxsDataAdapterCommon.getGson().toJson(result);
			this.logEnd("getPagingCompactAnalysisByProposalId", id);
			BiosaxsDataAdapterCommon.sendResponseToclient(response, json);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getPagingCompactAnalysisByProposalId", id, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	/**
	 * @param getPagingCompactAnalysisByExperimentId
	 */
	public ActionForward getPagingCompactAnalysisByExperimentId(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		long id = BiosaxsDataAdapterCommon.logMethod("getPagingCompactAnalysisByExperimentId", request);
		try {
			Integer experimentId = Integer.parseInt(request.getParameter("experimentId"));

			List<Map<String, Object>> data = biosaxsActions.getCompactAnalysisInformationByExperimentId(experimentId);
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("success", "true");
			result.put("results", data.size());
			result.put("rows", data);
			String json = BiosaxsDataAdapterCommon.getGson().toJson(result);
			this.logEnd("getPagingCompactAnalysisByExperimentId", id);
			BiosaxsDataAdapterCommon.sendResponseToclient(response, json);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getPagingCompactAnalysisByExperimentId", id, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	/**
	 * @param getCompactAnalysisByProposalId
	 */
	public ActionForward getCompactAnalysisByProposalId(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
		long start = BiosaxsDataAdapterCommon.logMethod("getCompactAnalysisByProposalId", request);
		try {
			Integer limit = 100;
			if (request.getParameter("limit") != null) {
				limit = Integer.parseInt(request.getParameter("limit"));
			}
			List<Map<String, Object>> data = biosaxsActions.getCompactAnalysisByProposalId(this.getProposalBySessionId(request), limit);

			String json = BiosaxsDataAdapterCommon.getGson().toJson(data);
			this.logEnd("getCompactAnalysisByProposalId", start);
			BiosaxsDataAdapterCommon.sendResponseToclient(response, json);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getCompactAnalysisByProposalId", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	/**
	 * @param getAnalysisCalibrationByProposalId
	 */
	public ActionForward getAnalysisCalibrationByProposalId(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
		long start = BiosaxsDataAdapterCommon.logMethod("getAnalysisCalibrationByProposalId", request);
		try {
			List<Map<String, Object>> data = biosaxsActions.getAnalysisCalibrationByProposalId(getProposalBySessionId(request));

			String json = BiosaxsDataAdapterCommon.getGson().toJson(data);
			this.logEnd("getAnalysisCalibrationByProposalId", start);
			BiosaxsDataAdapterCommon.sendResponseToclient(response, json);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getAnalysisCalibrationByProposalId", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	public ActionForward getDataPDB(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {

		long start = BiosaxsDataAdapterCommon.logMethod("getDataPDB", request);
		try {

			/**
			 * [{modelId=5034, color=0xFF6600, opacity=0.5}{modelId=5036,
			 * color=0x00CC00, opacity=0.5}]
			 **/
			String models = request.getParameter("models");
			List<Integer> superpositions = BiosaxsDataAdapterCommon.parseToInteger(request.getParameter("superpositions"));

			Type mapType = new TypeToken<ArrayList<HashMap<String, String>>>() {}.getType();
			ArrayList<HashMap<String, String>> modelList = new Gson().fromJson(models, mapType);

			HashMap<String, List<HashMap<String, Object>>> map = PDBFactoryProducer.get(modelList, superpositions);
			String json = BiosaxsDataAdapterCommon.getGson().toJson(map);
			this.logEnd("getDataPDB", start);
			BiosaxsDataAdapterCommon.sendResponseToclient(response, json);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getDataPDB", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;

	}

//	public ActionForward getPDBContentByModelList(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
//		long start = BiosaxsDataAdapterCommon.logMethod("getPDBContentByModelList", request);
//		try {
//
//			/**
//			 * [{modelId=5034, color=0xFF6600, opacity=0.5}{modelId=5036,
//			 * color=0x00CC00, opacity=0.5}]
//			 **/
//			String properties = request.getParameter("properties");
//
//			Type mapType = new TypeToken<ArrayList<HashMap<String, String>>>() {
//			}.getType();
//			ArrayList<HashMap<String, String>> propertiesList = new Gson().fromJson(properties, mapType);
//
//			HashMap<String, String> pdf = new HashMap<String, String>();
//			pdf.put("XYZ", biosaxsActions.getPDBContentByModelList(propertiesList));
//			String json = BiosaxsDataAdapterCommon.getGson().toJson(pdf);
//			this.logEnd("getPDBContentByModelList", start);
//			BiosaxsDataAdapterCommon.sendResponseToclient(response, json);
//		} catch (Exception exp) {
//			BiosaxsDataAdapterCommon.logError("getPDBContentByModelList", start, exp);
//			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
//			response.setStatus(500);
//			return null;
//		}
//		return null;
//	}

//	public ActionForward getAbinitioPDBContentBySuperpositionList(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response)
//			throws IOException {
//		long start = BiosaxsDataAdapterCommon.logMethod("getAbinitioPDBContentBySuperpositionList", request);
//		try {
//
//			/**
//			 * [{superpositionId=5034, color=0xFF6600,
//			 * opacity=0.5}{superpositionId=5036, color=0x00CC00, opacity=0.5}]
//			 **/
//			String properties = request.getParameter("properties");
//
//			Type mapType = new TypeToken<ArrayList<HashMap<String, String>>>() {
//			}.getType();
//			ArrayList<HashMap<String, String>> propertiesList = new Gson().fromJson(properties, mapType);
//
//			HashMap<String, String> pdf = new HashMap<String, String>();
//			pdf.put("XYZ", biosaxsActions.getAbinitioPDBContentBySuperpositionList(propertiesList));
//			String json = BiosaxsDataAdapterCommon.getGson().toJson(pdf);
//			this.logEnd("getAbinitioPDBContentBySuperpositionList", start);
//			BiosaxsDataAdapterCommon.sendResponseToclient(response, json);
//		} catch (Exception exp) {
//			BiosaxsDataAdapterCommon.logError("getAbinitioPDBContentBySuperpositionList", start, exp);
//			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
//			response.setStatus(500);
//			return null;
//		}
//		return null;
//	}

//	public ActionForward getAlignedPDBContentBySuperpositionList(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response)
//			throws IOException {
//		long start = BiosaxsDataAdapterCommon.logMethod("getAlignedPDBContentBySuperpositionList", request);
//		try {
//
//			/**
//			 * [{superpositionId=5034, color=0xFF6600,
//			 * opacity=0.5}{superpositionId=5036, color=0x00CC00, opacity=0.5}]
//			 **/
//			String properties = request.getParameter("properties");
//
//			Type mapType = new TypeToken<ArrayList<HashMap<String, String>>>() {
//			}.getType();
//			ArrayList<HashMap<String, String>> propertiesList = new Gson().fromJson(properties, mapType);
//
//			HashMap<String, String> pdf = new HashMap<String, String>();
//			pdf.put("XYZ", biosaxsActions.getAlignedPDBContentBySuperpositionList(propertiesList));
//			String json = BiosaxsDataAdapterCommon.getGson().toJson(pdf);
//			this.logEnd("getAlignedPDBContentBySuperpositionList", start);
//			BiosaxsDataAdapterCommon.sendResponseToclient(response, json);
//		} catch (Exception exp) {
//			BiosaxsDataAdapterCommon.logError("getAlignedPDBContentBySuperpositionList", start, exp);
//			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
//			response.setStatus(500);
//			return null;
//		}
//		return null;
//	}

	/**
	 * @param getExperimentInformationByProposalId
	 * @throws Exception
	 */
	public ActionForward getExperimentInformationByProposalId(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		long start = BiosaxsDataAdapterCommon.logMethod("getExperimentInformationByProposalId", request);
		try {
			Integer proposalId = this.getProposalBySessionId(request);
			if (proposalId != null) {
				List<Map<String, Object>> data = biosaxsActions.getExperimentInformationByProposalId(proposalId);
				String json = BiosaxsDataAdapterCommon.getGson().toJson(data);
				this.logEnd("getExperimentInformationByProposalId", start);
				BiosaxsDataAdapterCommon.sendResponseToclient(response, json);
			}
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getInformationByProposalId", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	private Integer getProposalBySessionId(HttpServletRequest request) throws Exception {
		String sessionId = request.getParameter("sessionId");

		if (sessionId != null) {
			/** Access from manager account by session Id **/
			Proposal3VO proposal = this.biosaxsActions.getProposalBySessionId(Integer.parseInt(sessionId));
			if (proposal != null) {
				if (Confidentiality.isAccessAllowed(request, proposal.getProposalId())) {
					return proposal.getProposalId();
				} else {
					throw new Exception("Access denied");
				}
			} else {
				throw new Exception("Access denied");
			}
		} else {
			return BiosaxsDataAdapterCommon.getProposalId(request);
		}
	}

	private HashMap<String, List<?>> getProposalData(int proposalId, String scope) throws Exception {
		if (scope != null) {
			HashMap<String, List<?>> results = new HashMap<String, List<?>>();
			List<Macromolecule3VO> macromolecules = this.biosaxsActions.findMacromoleculesByProposalId(proposalId);
			List<Buffer3VO> buffers = this.biosaxsActions.findBuffersByProposalId(proposalId);
			results.put("macromolecules", macromolecules);
			results.put("buffers", buffers);
			return results;
		}
		return this.getProposalData(proposalId);
	}

	private HashMap<String, List<?>> getProposalData(int proposalId) throws Exception {
		HashMap<String, List<?>> results = new HashMap<String, List<?>>();
		List<Macromolecule3VO> macromolecules = this.biosaxsActions.findMacromoleculesByProposalId(proposalId);
		List<Buffer3VO> buffers = this.biosaxsActions.findBuffersByProposalId(proposalId);
		List<StockSolution3VO> stockSolutions = this.biosaxsActions.findStockSolutionsByProposalId(proposalId);
		List<Platetype3VO> plateTypes = this.biosaxsActions.findPlateTypes();

		List<Session3VO> sessions = this.biosaxsActions.getSessionByProposalId(proposalId);
		List<Assembly3VO> assemblies = this.biosaxsActions.findAssembliesByProposalId(proposalId);
		List<LabContact3VO> labContacts = this.biosaxsActions.findLabContactsByProposal(proposalId);
		List<Shipping3VO> shippings = this.biosaxsActions.findShipmentsByProposal(proposalId);

		results.put("plateTypes", plateTypes);
		results.put("sessions", sessions);
		results.put("macromolecules", macromolecules);
		results.put("buffers", buffers);
		results.put("stockSolutions", stockSolutions);
		results.put("assemblies", assemblies);
		results.put("labcontacts", labContacts);
		results.put("shippings", shippings);
		return results;
	}

	/**
	 * getProposal
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws IOException
	 */
	public ActionForward getProposal(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
		long start = BiosaxsDataAdapterCommon.logMethod("getProposal", request);
		try {
			Integer proposalId = this.getProposalBySessionId(request);
			String scope = null;
			if (request.getParameterMap().containsKey("scope")) {
				scope = request.getParameter("scope");
			}
			String json = BiosaxsDataAdapterCommon.getGson("yyyy MM dd").toJson(this.getProposalData(proposalId, scope));
			this.logEnd("getProposal", start);
			BiosaxsDataAdapterCommon.sendResponseToclient(response, json);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getProposal", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}

		return null;
	}

	/**
	 * updateSession, it is called in order to update constantly the information
	 * of the experiments in the session. Usually it is done by the client each
	 * t time. In order to avoid overloads if the last updated was less of 30
	 * seconds, it will skip the update.
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws IOException
	 */
	// public ActionForward updateSession(ActionMapping mapping, ActionForm
	// actForm, HttpServletRequest request,
	// HttpServletResponse response) throws IOException {
	// long start = BiosaxsDataAdapterCommon.logMethod("updateSession",
	// request);
	// try{
	// if (BiosaxsDataAdapterCommon.getTicket(request) != null){
	// if (System.currentTimeMillis() -
	// BiosaxsDataAdapterCommon.getTicket(request).LAST_UPDATE_TIME > 30000){
	// /** this method is called from javascript and update the values of
	// experiments **/
	// Integer proposalId = BiosaxsDataAdapterCommon.getProposalId(request);
	// List<Experiment3VO> experiments =
	// biosaxsActions.findExperimentsByProposalId(proposalId,
	// ExperimentScope.MINIMAL);
	// BiosaxsDataAdapterCommon.setExperimentsOnSession(request,
	// ExperimentScope.MINIMAL, experiments);
	// experiments = biosaxsActions.findExperimentsByProposalId(proposalId,
	// ExperimentScope.MEDIUM);
	// BiosaxsDataAdapterCommon.setExperimentsOnSession(request,
	// ExperimentScope.MEDIUM, experiments);
	// }
	// }
	// this.logEnd("updateSession", start);
	// }
	// catch(Exception exp){
	// BiosaxsDataAdapterCommon.logError("updateSession", start, exp);
	// response.getWriter().write(
	// BiosaxsDataAdapterCommon.getErrorMessage(exp));
	// response.setStatus(500);
	// return null;
	// }
	// return null;
	// }

	/**
	 * getExperimentsByProposalId
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws IOException
	 */
	public ActionForward getExperimentsByProposalId(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			long start = BiosaxsDataAdapterCommon.logMethod("getExperimentsByProposalId", request);
			Integer proposalId = BiosaxsDataAdapterCommon.getProposalId(request);
			String scope = request.getParameter("scope");

			ExperimentScope experimentScope = ExperimentScope.MINIMAL;
			if (scope.equals("MEDIUM")) {
				experimentScope = ExperimentScope.MEDIUM;
			}
			if (scope.equals("FULL")) {
				experimentScope = ExperimentScope.MEDIUM;
			}

			String json = null;
			List<Experiment3VO> experiments = null;
			/** Is it on session? **/
			// if (BiosaxsDataAdapterCommon.getExperimentsOnSession(request,
			// experimentScope) != null){
			// experiments =
			// BiosaxsDataAdapterCommon.getExperimentsOnSession(request,
			// experimentScope);
			// }
			// else{
			experiments = this.biosaxsActions.findExperimentsByProposalId(proposalId, experimentScope);
			// BiosaxsDataAdapterCommon.setExperimentsOnSession(request,
			// experimentScope, experiments);
			// }
			json = ExperimentSerializer.serialize(experiments, experimentScope);
			this.logEnd("getExperimentsByProposalId", start);
			BiosaxsDataAdapterCommon.sendResponseToclient(response, json);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getExperimentsByProposalId", exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	/**
	 * getScatteringCurveByFrameIdsList
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward getScatteringCurveByFrameIdsList(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("getScatteringCurveByFrameIdsList", request);
		try {
			List<Integer> frameIdList = BiosaxsDataAdapterCommon.parseToInteger(request.getParameter("frameIdsList"));
			/**
			 * This mergeIdsList get the averageFilePath file
			 */
			List<Integer> mergeIdsList = BiosaxsDataAdapterCommon.parseToInteger(request.getParameter("mergeIdsList"));
			/**
			 * This subtractionIds get the subtracted file path
			 */
			List<Integer> subtractionIds = BiosaxsDataAdapterCommon.parseToInteger(request.getParameter("subtractionIds"));

			List<Integer> fitIds = BiosaxsDataAdapterCommon.parseToInteger(request.getParameter("fitToExperimentDataIds"));

			List<Frame3VO> frames = this.biosaxsActions.getFramesByIds(frameIdList);
			List<Merge3VO> merges = this.biosaxsActions.getMergeByIds(mergeIdsList);
			List<Subtraction3VO> subtractions = this.biosaxsActions.getSubtractionByIds(subtractionIds);
			List<FitStructureToExperimentalData3VO> fits = this.biosaxsActions.getFitStructureToExperimentalDataByIds(fitIds);

			BiosaxsDataAdapterCommon.sendResponseToclient(response, ScatteringCurvesParser.curveParser(frames, merges, subtractions, fits));
			this.logEnd("getScatteringCurveByFrameIdsList", start);

		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getScatteringCurveByFrameIdsList", start, exp);
			response.getWriter().write(exp.getMessage() + "  " + exp.getCause());
			response.setStatus(500);
		}
		return null;
	}

	/**
	 * getScatteringCurveByFrameIdsList
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward getDataPlot(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("getDataPlot", request);
		try {
			List<Integer> frames = BiosaxsDataAdapterCommon.parseToInteger(request.getParameter("frames"));
			List<Integer> subtractions = BiosaxsDataAdapterCommon.parseToInteger(request.getParameter("subtractions"));
			List<Integer> models = BiosaxsDataAdapterCommon.parseToInteger(request.getParameter("models"));
			List<Integer> fits = BiosaxsDataAdapterCommon.parseToInteger(request.getParameter("fits"));
			List<Integer> rigids = BiosaxsDataAdapterCommon.parseToInteger(request.getParameter("rbm"));
			
			BiosaxsDataAdapterCommon.sendResponseToclient(response, FactoryProducer.getJSON(frames, null,subtractions, models, fits, rigids));
			this.logEnd("getDataPlot", start);

		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getDataPlot", start, exp);
			response.getWriter().write(exp.getMessage() + "  " + exp.getCause());
			response.setStatus(500);
		}
		return null;
	}

	public ActionForward getAbinitioModelsBySubtractionId(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
		long start = BiosaxsDataAdapterCommon.logMethod("getAbinitioModelsBySubtractionId", request);
		try {
			List<Integer> subtractionIds = BiosaxsDataAdapterCommon.parseToInteger(request.getParameter("subtractionIdList"));
			List<Subtraction3VO> abinitios = this.biosaxsActions.getAbinitioModelsBySubtractionId(subtractionIds);
			Gson gson = BiosaxsDataAdapterCommon.getGson();
			BiosaxsDataAdapterCommon.sendResponseToclient(response, gson.toJson(abinitios));
			this.logEnd("getAbinitioModelsBySubtractionId", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getAbinitioModelsBySubtractionId", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	/**
	 * getAbinitioById
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward getAbinitioByIdsList(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("getAbinitioListByIds", request);
		try {
			List<Integer> abinitioIds = BiosaxsDataAdapterCommon.parseToInteger(request.getParameter("abinitioIdsList"));
			List<AbInitioModel3VO> abinitio = this.biosaxsActions.getAbinitioById(abinitioIds);
			BiosaxsDataAdapterCommon.sendResponseToclient(response, BiosaxsDataAdapterCommon.getGson().toJson(abinitio));
			this.logEnd("getAbinitioListByIds", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getAbinitioListByIds", start, exp);
			response.getWriter().write(exp.getMessage() + "  " + exp.getCause());
			response.setStatus(500);
		}
		return null;
	}

	/**
	 * getAbinitioModelsByExperimentId
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward getAbinitioModelsByExperimentId(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("getAbinitioModelsByExperimentId", request);
		try {
			Integer experimentId = Integer.parseInt(request.getParameter("experimentId"));
			List<AbInitioModel3VO> abinitios = this.biosaxsActions.getAbinitioModelsByExperimentId(experimentId);
			BiosaxsDataAdapterCommon.sendResponseToclient(response, BiosaxsDataAdapterCommon.getGson().toJson(abinitios));
			this.logEnd("getAbinitioModelsByExperimentId", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getAbinitioModelsByExperimentId", start, exp);
			response.getWriter().write(exp.getMessage() + "  " + exp.getCause());
			response.setStatus(500);
		}
		return null;
	}

	/**
	 * saveExperiment
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveExperiment(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("saveExperiment", request);
		try {

			Integer experimentId = Integer.parseInt(request.getParameter("experimentId"));
			String name = request.getParameter("name");
			String type = request.getParameter("type");
			String comments = request.getParameter("comments");

			this.biosaxsActions.saveExperiment(experimentId, name, type, comments, this.getProposalBySessionId(request));
			this.logEnd("saveExperiment", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("saveExperiment", start, exp);
			response.getWriter().write(exp.getMessage() + "  " + exp.getCause());
			response.setStatus(500);
			return null;
		}
		return this.getProposal(mapping, actForm, request, response);
	}

	/**
	 * saveBuffer
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveBuffer(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("saveBuffer", request);
		try {
			Buffer3VO buffer3VO = BiosaxsDataAdapterCommon.getGson("yyyy MM dd").fromJson(request.getParameter("buffer"), Buffer3VO.class);
			buffer3VO.setProposalId(BiosaxsDataAdapterCommon.getProposalId(request));
			this.biosaxsActions.saveBuffer(buffer3VO);
			this.logEnd("saveBuffer", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("saveBuffer", start, exp);
			response.getWriter().write(exp.getMessage() + "  " + exp.getCause());
			response.setStatus(500);
			return null;
		}
		return this.getProposal(mapping, actForm, request, response);
	}

	/**
	 * saveMacromolecule
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveMacromolecule(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("saveMacromolecule", request);
		try {
			Macromolecule3VO macromolecule3VO = BiosaxsDataAdapterCommon.getGson("yyyy MM dd").fromJson(request.getParameter("macromolecule"), Macromolecule3VO.class);
			macromolecule3VO.setProposalId(BiosaxsDataAdapterCommon.getProposalId(request));
			this.biosaxsActions.saveMacromolecule(macromolecule3VO);
			this.logEnd("saveMacromolecule", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("saveMacromolecule", start, exp);
			response.getWriter().write(exp.getMessage() + "  " + exp.getCause());
			response.setStatus(500);
			return null;
		}
		return this.getProposal(mapping, actForm, request, response);
	}

	public ActionForward saveStructure(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("saveStructure", request);
		try {
			Structure3VO structure3VO = BiosaxsDataAdapterCommon.getGson("yyyy MM dd").fromJson(request.getParameter("structure"), Structure3VO.class);
			this.biosaxsActions.saveStructre(structure3VO);
			this.logEnd("saveMacromolecule", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("saveMacromolecule", start, exp);
			response.getWriter().write(exp.getMessage() + "  " + exp.getCause());
			response.setStatus(500);
			return null;
		}
		return this.getProposal(mapping, actForm, request, response);
	}

	/**
	 * removeStructure
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward removeStructure(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("removeStructure", request);
		try {
			int structureId = Integer.parseInt(request.getParameter("structureId"));
			this.biosaxsActions.removeStructure(structureId);

			this.logEnd("removeStructure", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("removeStructure", start, exp);
			response.getWriter().write(exp.getMessage() + "  " + exp.getCause());
			response.setStatus(500);
			return null;
		}
		return this.getProposal(mapping, actForm, request, response);
	}

	/**
	 * saveStoichiometry
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveStoichiometry(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("saveStoichiometry", request);
		try {

			Stoichiometry3VO stoichiometry = new Stoichiometry3VO();
			int macromoleculeId = Integer.parseInt(request.getParameter("macromoleculeId"));
			int hostmacromoleculeId = Integer.parseInt(request.getParameter("hostmacromoleculeId"));
			String ratio = request.getParameter("ratio");
			String comments = request.getParameter("comments");

			this.biosaxsActions.saveStoichiometry(macromoleculeId, hostmacromoleculeId, ratio, comments);

			this.logEnd("saveStoichiometry", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("saveStoichiometry", start, exp);
			response.getWriter().write(exp.getMessage() + "  " + exp.getCause());
			response.setStatus(500);
			return null;
		}
		return this.getProposal(mapping, actForm, request, response);
	}

	/**
	 * removeStoichiometry
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward removeStoichiometry(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("removeStoichiometry", request);
		try {
			int stoichiometryId = Integer.parseInt(request.getParameter("stoichiometryId"));
			this.biosaxsActions.removeStoichiometry(stoichiometryId);

			this.logEnd("removeStoichiometry", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("removeStoichiometry", start, exp);
			response.getWriter().write(exp.getMessage() + "  " + exp.getCause());
			response.setStatus(500);
			return null;
		}
		return this.getProposal(mapping, actForm, request, response);
	}

	/**
	 * saveAssembly
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveAssembly(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("saveAssembly", request);
		try {
			Integer assemblyId = Integer.parseInt(request.getParameter("assemblyId"));
			List<Integer> macromoleculeIds = BiosaxsDataAdapterCommon.parseToInteger(request.getParameter("macromoleculeIds"));
			this.biosaxsActions.createAssembly(assemblyId, macromoleculeIds);
			this.logEnd("saveAssembly", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("saveBuffer", start, exp);
			response.getWriter().write(exp.getMessage() + "  " + exp.getCause());
			response.setStatus(500);
			return null;
		}

		return this.getProposal(mapping, actForm, request, response);
	}

	private ActionForward sendFileToClient(String filePath, HttpServletResponse response, String fileName) throws Exception {
		try {
			filePath = BiosaxsActions.checkFilePathForDevelopment(filePath);
			if (new File(filePath).exists()) {
				byte[] bytes = FileUtil.readBytes(filePath);
				response.setContentLength(bytes.length);
				ServletOutputStream out = response.getOutputStream();
				response.setContentType("application/octet-stream");
				response.setHeader("Content-disposition", "attachment; filename=" + fileName);
				out.write(bytes);
				out.close();
			}
		} catch (Exception exp) {
			throw exp;
		}
		return null;

	}

	/**
	 * getTemplateSourceFile: it returns the robot.xml file
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward getTemplateSourceFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("getTemplateSourceFile", request);
		try {
			String experimentId = request.getParameter("experimentId");
			Integer proposalId = this.getProposalBySessionId(request);

			// SaxsDataCollectionComparator[] options = {
			// SaxsDataCollectionComparator.defaultComparator };

			// String xml =
			// this.biosaxsActions.getTemplateRobotFile(Integer.parseInt(experimentId),
			// proposalId, options);
			// String xml =
			// this.biosaxsActions.getTemplateRobotFile(Integer.parseInt(experimentId),
			// proposalId, SaxsDataCollectionComparator.defaultComparator);
			String xml = this.biosaxsActions.getTemplateRobotFile(Integer.parseInt(experimentId), proposalId);

			this.sendCSVFileReponse("robot.xml", xml, response);
			this.logEnd("getTemplateSourceFile", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getTemplateSourceFile", start, exp);
			response.setStatus(500);
			return null;
		}
		return null;
	};

	/**
	 * sortMeasurements: it returns the robot.xml file
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward sortMeasurements(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("sortMeasurements", request);
		try {
			String experimentId = request.getParameter("experimentId");
			Integer proposalId = this.getProposalBySessionId(request);
			String sortBy = request.getParameter("sortBy");

			Experiment3VO experiment = null;
			if (sortBy.equals("FIFO")) {
				SaxsDataCollectionComparator[] options = { SaxsDataCollectionComparator.BY_MEASUREMENT_ID };
				experiment = this.biosaxsActions.setPriorities(Integer.parseInt(experimentId), proposalId, options);
			} else {
				SaxsDataCollectionComparator[] options = SaxsDataCollectionComparator.defaultComparator;
				experiment = this.biosaxsActions.setPriorities(Integer.parseInt(experimentId), proposalId, options);
			}

			BiosaxsDataAdapterCommon.sendResponseToclient(response, BiosaxsDataAdapterCommon.getGson().toJson(experiment));

			this.logEnd("sortMeasurements", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("sortMeasurements", start, exp);
			response.setStatus(500);
			return null;
		}
		return null;
	};

	/**
	 * getPdbFiles
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward getPdbFiles(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("getPdbFiles", request);
		try {
			String type = request.getParameter("type");
			String abInitioModelId = request.getParameter("abInitioModelId");
			String filePath = this.biosaxsActions.getAbnitioPDBFile(type, Integer.parseInt(abInitioModelId));

			filePath = BiosaxsActions.checkFilePathForDevelopment(filePath);
			if (new File(filePath).exists()) {
				this.sendFileToClient(filePath, response, new File(filePath).getName());
			}
			this.logEnd("getPdbFiles", start);
		} catch (Exception exp) {
			exp.printStackTrace();
			BiosaxsDataAdapterCommon.logError("getPdbFiles", start, exp);
			response.setStatus(500);
			return null;
		}
		return null;
	};

//	/**
//	 * getStructureFile
//	 * 
//	 * @param mapping
//	 * @param actForm
//	 * @param request
//	 * @param in_reponse
//	 * @return
//	 * @throws Exception
//	 */
//	public ActionForward getStructureFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		long start = BiosaxsDataAdapterCommon.logMethod("getStructureFile", request);
//		try {
//			Integer structureId = Integer.parseInt(request.getParameter("structureId"));
//			Structure3VO structure3VO = this.biosaxsActions.getStructureById(structureId);
//
//			String filePath = BiosaxsActions.checkFilePathForDevelopment(structure3VO.getFilePath());
//			File file = new File(filePath);
//			if (file.exists()) {
//				// this.sendFileToClient(filePath, response, new
//				// File(filePath).getName());
//				String content = FileUtil.fileToString(file);
//				BiosaxsDataAdapterCommon.sendTextToclient(response, content);
//
//			}
//			this.logEnd("getStructureFile", start);
//		} catch (Exception exp) {
//			BiosaxsDataAdapterCommon.logError("getStructureFile", start, exp);
//			exp.printStackTrace();
//			response.setStatus(500);
//			return null;
//		}
//		return null;
//	};

	public ActionForward addWorkflow(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("addWorkflow", request);
		try {
			String workflowString = request.getParameter("workflow");
			String inputParameters = request.getParameter("inputParameters");

			Workflow3VO workflow = new Gson().fromJson(workflowString, Workflow3VO.class);
			workflow.setStatus("PENDING");
			//workflow.setProposalId(this.getProposalBySessionId(request));
			workflow.setWorkflowType("BioSAXS Post Processing");
			Type mapType = new TypeToken<ArrayList<InputParameterWorkflow>>() {
			}.getType();
			ArrayList<InputParameterWorkflow> inputs = BiosaxsDataAdapterCommon.getGson().fromJson(inputParameters, mapType);
			workflow = this.biosaxsActions.save(workflow, inputs);
			this.logEnd("addWorkflow", start);
			BiosaxsDataAdapterCommon.sendResponseToclient(response, new Gson().toJson(workflow));
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("addWorkflow", start, exp);
			exp.printStackTrace();
			response.setStatus(500);
			return null;
		}
		return null;
	};

	/**
	 * getFitStructureToExperimentalData
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward getFitStructureToExperimentalDataFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("getFitStructureToExperimentalDataFile", request);
		try {
			// TODO: Partially implemented
			int fitStructureToExperimentalDataId = Integer.parseInt(request.getParameter("fitStructureToExperimentalDataId"));
			/** Type may be: log, output or fit **/
			String type = request.getParameter("type");
			/** Type may be: text, json **/
			String format = request.getParameter("format");
			FitStructureToExperimentalData3VO fit = this.biosaxsActions.getFitStructureToExperimentDatal(fitStructureToExperimentalDataId);

			if (type.toUpperCase().equals("LOG") || (type.toUpperCase().equals("OUTPUT"))) {
				String filepath = "";
				if (type.toUpperCase().equals("OUTPUT")) {
					filepath = fit.getOutputFilePath();
				} else {
					filepath = fit.getLogFilePath();
				}
				if (new File(filepath).exists()) {
					String content = this.readFile(filepath);
					this.logEnd("getFitStructureToExperimentalDataFile", start);
					BiosaxsDataAdapterCommon.sendTextToclient(response, content);
				} else {
					this.logEnd("getFitStructureToExperimentalDataFile", start);
					BiosaxsDataAdapterCommon.sendTextToclient(response, "File: " + filepath + " does not exist");
				}

			}
			// if (type.toUpperCase().equals("OUTPUT")){
			// if (new File(fit.getOutputFilePath()).exists()){
			// String content = this.readFile(fit.getOutputFilePath());
			// this.logEnd("getFitStructureToExperimentalData", start);
			// BiosaxsDataAdapterCommon.sendTextToclient(response, content);
			// }
			// else{
			// System.out.println("Not found");
			// this.logEnd("getFitStructureToExperimentalData", start);
			// BiosaxsDataAdapterCommon.sendTextToclient(response, "File: " +
			// fit.getOutputFilePath() + " does not exist");
			// }
			// }
			if (type.toUpperCase().equals("FIT")) {
				/** get the fit with the scattering curve associated **/
				List<String> filePathList = new ArrayList<String>();
				filePathList.add(fit.getFitFilePath());
				this.logEnd("getFitStructureToExperimentalDataFile", start);
				BiosaxsDataAdapterCommon.sendResponseToclient(response, ScatteringCurvesParser.curveParserByFilePath(filePathList, 3));
			}

		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getFitStructureToExperimentalDataFile", start, exp);
			response.getWriter().write(exp.getMessage() + "  " + exp.getCause());
			response.setStatus(500);
		}
		return null;
	}

	/**
	 * addFitStructureData
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward addFitStructureData(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("addFitStructureData", request);
		try {
			// TODO: Partially implemented
			String fitStructureToExperimentalDataString = request.getParameter("fitStructureToExperimentalData");
			FitStructureToExperimentalData3VO fitStructureToExperimentalData = new Gson().fromJson(fitStructureToExperimentalDataString, FitStructureToExperimentalData3VO.class);

			fitStructureToExperimentalData = this.biosaxsActions.merge(fitStructureToExperimentalData);
			this.logEnd("addFitStructureData", start);

			BiosaxsDataAdapterCommon.sendResponseToclient(response, BiosaxsDataAdapterCommon.getGson().toJson(fitStructureToExperimentalData));
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("addFitStructureData", start, exp);
			response.getWriter().write(exp.getMessage() + "  " + exp.getCause());
			response.setStatus(500);
		}
		return null;
	}

	public ActionForward getFitStructureToExperimentalDataBySubtractionId(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("getFitStructureToExperimentalDataBySubtractionId", request);
		try {
			Integer subtractionId = Integer.parseInt(request.getParameter("subtractionId"));

			List<FitStructureToExperimentalData3VO> fitStructureToExperimentalDataList = this.biosaxsActions.getFitStructureToExperimentalDataBySubtractionId(subtractionId);
			this.logEnd("getFitStructureToExperimentalDataBySubtractionId", start);

			BiosaxsDataAdapterCommon.sendResponseToclient(response, BiosaxsDataAdapterCommon.getGson().toJson(fitStructureToExperimentalDataList));
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getFitStructureToExperimentalDataBySubtractionId", start, exp);
			response.getWriter().write(exp.getMessage() + "  " + exp.getCause());
			response.setStatus(500);
		}
		return null;
	}

	/**
	 * getCompressedDataAcquisition
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward getCompressedDataAcquisition(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("getCompressedDataAcquisition", request);
		try {
			Integer experimentId = Integer.parseInt(request.getParameter("experimentId"));
			Experiment3VO experiment3VO = this.biosaxsActions.getExperimentById(experimentId, ExperimentScope.MINIMAL, BiosaxsDataAdapterCommon.getProposalId(request));
			if (experiment3VO != null) {
				if (experiment3VO.getDataAcquisitionFilePath() != null) {
					String filePath = BiosaxsActions.checkFilePathForDevelopment(experiment3VO.getDataAcquisitionFilePath());
					if (new File(filePath).exists()) {
						this.sendFileToClient(filePath, response, experiment3VO.getName() + "_" + experiment3VO.getExperimentId() + ".zip");
					}
				}
			}
			this.logEnd("getCompressedDataAcquisition", start);
		} catch (Exception exp) {
			response.setStatus(500);
			return null;
		}
		return null;
	};

	private ActionForward sendImageToClient(String filePath, HttpServletResponse response) {
		return BiosaxsDataAdapterCommon.sendImageToClient(filePath, response);
	}

	/**
	 * getImage
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward getImage(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// long start = BiosaxsDataAdapterCommon.logMethod("getImage", request);
		try {
			String type = request.getParameter("type");
			String subtractionId = request.getParameter("subtractionId");
			String filePath = this.biosaxsActions.getSubstractedFileById(type, Integer.parseInt(subtractionId));
			if (filePath != null) {
				this.sendImageToClient(filePath, response);
			}
			// this.logEnd("getImage", start);
		} catch (Exception exp) {
			response.setStatus(500);
			return null;
		}
		return null;
	};

	/**
	 * getAbinitioImage
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward getAbinitioImage(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// long start = BiosaxsDataAdapterCommon.logMethod("getAbinitioImage",
		// request);
		try {
			String type = request.getParameter("type");
			String modelListId = request.getParameter("modelListId");
			String filePath = this.biosaxsActions.getModelListFileById(type, Integer.parseInt(modelListId));

			if (filePath != null) {
				this.sendImageToClient(filePath, response);
			}
			// this.logEnd("getAbinitioImage", start);
		} catch (Exception exp) {
			response.setStatus(500);
			return null;
		}
		return null;
	};

	/**
	 * getModelFile
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward getModelFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("getModelFile", request);
		try {
			String type = request.getParameter("type");
			String modelId = request.getParameter("modelId");
			String format = request.getParameter("format");
			String filePath = this.biosaxsActions.getModelFileById(type, Integer.parseInt(modelId));
			if (filePath != null) {
				String content = this.biosaxsActions.getContentFileByPath(filePath);
				if (format.equals("text")) {
					BiosaxsDataAdapterCommon.sendResponseToclient(response, content);
				} else {
					BiosaxsDataAdapterCommon.sendResponseToclient(response, new Gson().toJson(content));
				}
			}
			this.logEnd("getModelFile", start);
		} catch (Exception exp) {
			exp.printStackTrace();
			response.setStatus(500);
			BiosaxsDataAdapterCommon.logError("getModelFile", start, exp);
		}
		return null;
	};

	/**
	 * saveShipment
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveShipment(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("saveShipment", request);
		try {
			String shippingId = request.getParameter("shippingId");
			String name = request.getParameter("name");
			String comments = request.getParameter("comments");
			String sendingLabContactId = request.getParameter("sendingLabContactId");
			String returnLabContactId = request.getParameter("returnLabContactId");
			String returnCourier = request.getParameter("returnCourier");
			String courierAccount = request.getParameter("courierAccount");
			String BillingReference = request.getParameter("BillingReference");
			String dewarAvgCustomsValue = request.getParameter("dewarAvgCustomsValue");
			String dewarAvgTransportValue = request.getParameter("dewarAvgTransportValue");
			Shipping3VO shipping = this.biosaxsActions.saveShipment(shippingId, name, comments, sendingLabContactId, returnLabContactId, returnCourier, courierAccount,
					BillingReference, dewarAvgCustomsValue, dewarAvgTransportValue, BiosaxsDataAdapterCommon.getProposalId(request));
			BiosaxsDataAdapterCommon.sendResponseToclient(response, BiosaxsDataAdapterCommon.getGson().toJson(shipping));
			this.logEnd("saveShipment", start);

		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("saveShipment", start, exp);
			response.getWriter().write(exp.getMessage() + "  " + exp.getCause());
			response.setStatus(500);
		}
		return null;
	};

	/**
	 * saveStockSolution
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveStockSolution(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("saveStockSolution", request);
		try {
			String stockSolution = request.getParameter("stockSolution");
			StockSolution3VO stockSolution3VO = BiosaxsDataAdapterCommon.getGson("yyyy MM dd").fromJson(stockSolution, StockSolution3VO.class);
			stockSolution3VO.setProposalId(BiosaxsDataAdapterCommon.getProposalId(request));
			stockSolution3VO = this.biosaxsActions.saveStockSolution(stockSolution3VO);

			BiosaxsDataAdapterCommon.sendResponseToclient(response, BiosaxsDataAdapterCommon.getGson().toJson(stockSolution3VO));
			this.logEnd("saveStockSolution", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("saveStockSolution", start, exp);
			response.getWriter().write(exp.getMessage() + "  " + exp.getCause());
			response.setStatus(500);
		}
		return null;
	}

	/**
	 * removeExperiment
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward removeExperiment(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("removeExperiment", request);
		try {
			String experimentId = request.getParameter("experimentId");
			/** RETRIEVING PROJECT **/
			this.biosaxsActions.removeExperiment(Integer.parseInt(experimentId));
			this.logEnd("removeExperiment", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("removeExperiment", start, exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	public String getExperimentSerialized(int experimentId, int proposalId) {
		Experiment3VO experiment = this.biosaxsActions.getExperimentById(experimentId, ExperimentScope.MEDIUM, proposalId);
		return ExperimentSerializer.serializeExperiment(experiment, ExperimentScope.MEDIUM);
	}

	public String getExperimentSerialized(Integer experimentId, Integer proposalId, ExperimentScope experimentScope) {
		Experiment3VO experiment = this.biosaxsActions.getExperimentById(experimentId, experimentScope, proposalId);
		String json = ExperimentSerializer.serializeExperiment(experiment, experimentScope);
		return json;
	}

	/**
	 * saveSpecimen
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveSpecimen(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			long start = BiosaxsDataAdapterCommon.logMethod("saveSpecimen", request);
			String specimen = request.getParameter("specimen");
			// TODO: Security check has to be done here checking that I am
			// allowed to change this specimen (belongs to
			// me)
			Gson gson = BiosaxsDataAdapterCommon.getGson();
			Specimen3VO specimen3VO = gson.fromJson(specimen, Specimen3VO.class);
			specimen3VO = this.biosaxsActions.merge(specimen3VO);

			gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			System.out.println("json: " + gson.toJson(specimen3VO));
			BiosaxsDataAdapterCommon.sendResponseToclient(response, gson.toJson(specimen3VO));

			// BiosaxsDataAdapterCommon.sendResponseToclient(response,
			// gson.toJson(specimen3VO));
			this.logEnd("getExperimentsByProposalId", start);
		} catch (Exception exp) {
			exp.printStackTrace();
			BiosaxsDataAdapterCommon.logError("saveSpecimen", exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	/**
	 * saveMeasurement
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveMeasurement(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			long start = BiosaxsDataAdapterCommon.logMethod("saveMeasurement", request);
			String measurement = request.getParameter("measurement");
			Gson gson = BiosaxsDataAdapterCommon.getGson();
			Measurement3VO measurement3VO = gson.fromJson(measurement, Measurement3VO.class);
			this.biosaxsActions.merge(measurement3VO);
			this.logEnd("saveMeasurement", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("saveMeasurement", exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	/**
	 * removeMeasurement
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward removeMeasurement(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			/**
			 * Only measurements pointing to samples can be removed and the
			 * whole data collection (buffer-sample-buffer) will be removed If
			 * in the data collection some buffer measurement is shared this
			 * will be not removed
			 */
			long start = BiosaxsDataAdapterCommon.logMethod("removeMeasurement", request);
			String measurement = request.getParameter("measurementId");
			Gson gson = BiosaxsDataAdapterCommon.getGson();
			Measurement3VO measurement3VO = gson.fromJson(measurement, Measurement3VO.class);
			this.biosaxsActions.remove(measurement3VO);
			this.logEnd("removeMeasurement", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("removeMeasurement", exp);
			response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}

	/**
	 * savePlate
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward savePlate(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			long start = BiosaxsDataAdapterCommon.logMethod("savePlate", request);
			String plates = request.getParameter("plates");
			Type mapType = new TypeToken<ArrayList<Sampleplate3VO>>() {
			}.getType();
			ArrayList<Sampleplate3VO> samplePlate3VOs = BiosaxsDataAdapterCommon.getGson().fromJson(plates, mapType);
			this.biosaxsActions.merge(samplePlate3VOs);
			this.logEnd("savePlate", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("savePlate", exp);
			response.setStatus(500);
			return null;
		}
		if (request.getParameterMap().containsKey("experimentId")) {
			return this.getExperimentById(mapping, actForm, request, response);
		}
		return null;// this.getExperimentById(mapping, actForm, request,
					// response);

	}

	/**
	 * emptyPlate
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward emptyPlate(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			BiosaxsDataAdapterCommon.logMethod("emptyPlate", request);
			String samplePlateId = request.getParameter("sampleplateId");
			String experimentId = request.getParameter("experimentId");
			this.biosaxsActions.emptyPlate(Integer.parseInt(samplePlateId), Integer.parseInt(experimentId));
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("emptyPlate", exp);
			response.setStatus(500);
			return null;
		}
		return this.getExperimentById(mapping, actForm, request, response);

	}

	/**
	 * autoFillPlate
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward autoFillPlate(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			BiosaxsDataAdapterCommon.logMethod("autoFillPlate", request);
			String samplePlateId = request.getParameter("sampleplateId");
			String experimentId = request.getParameter("experimentId");
			this.biosaxsActions.autoFillPlate(Integer.parseInt(samplePlateId), Integer.parseInt(experimentId));
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("autoFillPlate", exp);
			response.setStatus(500);
			return null;
		}
		return this.getExperimentById(mapping, actForm, request, response);

	}

	/**
	 * removeShipment
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward removeShipment(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = BiosaxsDataAdapterCommon.logMethod("removeShipment", request);
		try {
			String shippingId = request.getParameter("shippingId");
			this.biosaxsActions.removeShippingById(Integer.parseInt(shippingId));
			this.logEnd("removeShipment", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("removeShipment", start, exp);
			response.setStatus(500);
			return null;
		}
		return null;
	};

	public ActionForward addCase(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			long start = BiosaxsDataAdapterCommon.logMethod("addCase", request);
			String shippingId = request.getParameter("shippingId");
			this.biosaxsActions.createDewar(Integer.parseInt(shippingId));
			this.getShipmentById(mapping, actForm, request, response);
			this.logEnd("addCase", start);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("addCase", exp);
			response.setStatus(500);
			return null;
		}
		return null;
	};

	/**
	 * removeCase
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward removeCase(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			long start = BiosaxsDataAdapterCommon.logMethod("removeCase", request);
			String dewarId = request.getParameter("dewarId");
			String shippingId = request.getParameter("shippingId");

			Integer proposalId = BiosaxsDataAdapterCommon.getProposalId(request);
			this.biosaxsActions.removeCase(proposalId, dewarId, shippingId);

			this.logEnd("removeCase", start);
			this.getShipmentById(mapping, actForm, request, response);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("removeCase", exp);
			response.setStatus(500);
			return null;
		}
		return null;
	};

	/**
	 * saveCase
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveCase(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			long start = BiosaxsDataAdapterCommon.logMethod("saveCase", request);

			String dewarId = request.getParameter("dewarId");
			String dewar = request.getParameter("dewar");

			final Type type = new TypeToken<HashMap<String, String>>() {
			}.getType();
			final Map<String, String> dewarMap = BiosaxsDataAdapterCommon.getGson().fromJson(dewar, type);
			this.biosaxsActions.saveCase(dewarId, dewarMap);
			this.logEnd("saveCase", start);
			this.getShipmentById(mapping, actForm, request, response);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("saveCase", exp);
			response.setStatus(500);
			return null;
		}
		return null;
	};

	/**
	 * getShipmentById
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward getShipmentById(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			long start = BiosaxsDataAdapterCommon.logMethod("getShipmentById", request);

			String shippingId = request.getParameter("shippingId");

			Shipping3VO shipping3VO = this.biosaxsActions.getShipmentById(Integer.parseInt(shippingId));

			String json = BiosaxsDataAdapterCommon.getGson().toJson(shipping3VO, Shipping3VO.class);
			this.logEnd("getShipmentById", start);
			BiosaxsDataAdapterCommon.sendResponseToclient(response, json);
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("getShipmentById", exp);
			response.setStatus(500);
			return null;
		}
		return null;
	};

	/**
	 * removeStockSolution
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward removeStockSolution(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			long start = BiosaxsDataAdapterCommon.logMethod("removeStockSolution", request);
			String stockSolutionId = request.getParameter("stockSolutionId");
			StockSolution3VO stock = this.biosaxsActions.getStockSolutionById(Integer.parseInt(stockSolutionId));
			if (stock.getProposalId() == this.getProposalBySessionId(request)) {
				this.biosaxsActions.remove(stock);
				this.logEnd("removeStockSolution", start);
				return null;
			}

			throw new Exception("Invalid proposal");
		} catch (Exception exp) {
			BiosaxsDataAdapterCommon.logError("removeStockSolution", exp);
			response.setStatus(500);
		}

		return null;
	};

}
