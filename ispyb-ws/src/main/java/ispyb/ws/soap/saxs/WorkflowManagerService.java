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

import ispyb.common.util.Constants;
import ispyb.server.biosaxs.services.core.analysis.Analysis3Service;
import ispyb.server.biosaxs.services.core.analysis.advanced.AdvancedAnalysis3Service;
import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.vos.advanced.FitStructureToExperimentalData3VO;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.common.util.LoggerFormatter;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.collections.Workflow3Service;
import ispyb.server.mx.vos.collections.InputParameterWorkflow;
import ispyb.server.mx.vos.collections.Workflow3VO;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

@WebService(name = "WorkflowManagerService", serviceName = "ispybWS", targetNamespace = "http://ispyb.ejb3.webservices.biosaxs")
@SOAPBinding(style = Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Stateless
@SecurityDomain("ispyb")
@WebContext(authMethod = "BASIC", secureWSDLAccess = false, transportGuarantee = "NONE")
public class WorkflowManagerService {

	private final static Logger LOG = Logger.getLogger("Biosaxs Webservice");

	private final static Logger LOGGER = Logger.getLogger(WorkflowManagerService.class);

	private long now;

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Gson getGson() {
		return new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create();
	}
	
	protected static Calendar NOW;

	private Date getNow(){
		 NOW = GregorianCalendar.getInstance();
		 return NOW.getTime();
	}

	@WebMethod(operationName = "getWorkflowByStatus")
	@WebResult(name = "workflows")
	public String getWorkflowByStatus(@WebParam(name = "status") String status) {
		long start = this.logInit("getWorkflowByStatus");
		try {

			/** Getting all workflows which status is = status **/
			Workflow3Service workflow3Service = (Workflow3Service) ejb3ServiceLocator.getLocalService(Workflow3Service.class);
			List<Workflow3VO> workflows = workflow3Service.findAllByStatus(status);

			/** Data Structure to be returned **/
			HashMap<String, Object> workFlowDataStructure = new HashMap<String, Object>();
			workFlowDataStructure.put("WORKFLOW", getGson().toJson(selectWorkFlow(workflows)));

			/** Parsing the input and if there are subtraction getting their information **/
			List<InputParameterWorkflow> input = new ArrayList<InputParameterWorkflow>();

			List<Map<String, Object>> subtractions = new ArrayList<Map<String, Object>>();
			List<Macromolecule3VO> macromolecules = new ArrayList<Macromolecule3VO>();
			
			
			

			if (workflows.size() > 0) {
				input = workflow3Service.findInputParametersByWorkflowId(selectWorkFlow(workflows).getWorkflowId());
				workFlowDataStructure.put("INPUT",input);
				if (input.size() > 0) {
					for (InputParameterWorkflow inputParameterWorkflow : input) {
						if (inputParameterWorkflow.getName().toUpperCase().equals("SUBTRACTIONID")) {
							List<Map<String, Object>> aux = getInfoBySubtractionId(inputParameterWorkflow.getValue());
							/** There are as many as measurements on that datacollection **/
							subtractions.add(aux.get(0));
							/** Automatically takes macromolecule information of the sample measurement **/
							for (Map<String, Object> map : aux) {
								if (map.get("macromoleculeId") != null) {
									macromolecules.add(getMacromoleculeById(Integer.parseInt(map.get("macromoleculeId").toString())));
								}
							}

						}

						// if (inputParameterWorkflow.getName().toUpperCase().equals("MACROMOLECULEID")){
						// macromolecules.add(getMacromoleculeById(Integer.parseInt(inputParameterWorkflow.getValue())));
						// }

					}
				}
			}
			/** Adding subtraction to the result **/
			workFlowDataStructure.put("SUBTRACTIONS", subtractions);
			workFlowDataStructure.put("MACROMOLECULES", macromolecules);

			this.logFinish("getWorkflowByStatus", start);

			return getGson().toJson(workFlowDataStructure);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WORKFLOW_ERROR, "getWorkflowByStatus", start,
					System.currentTimeMillis(), e.getMessage(), e);
		}
		return null;
	}

	private Macromolecule3VO getMacromoleculeById(Integer macromoleculeId) throws NamingException {
//		Experiment3Service experiment3Service = (Experiment3Service) ejb3ServiceLocator.getLocalService(Experiment3Service.class);
//		Macromolecule3VO macromolecule = experiment3Service.findMacromoleculeById(macromoleculeId);
//		return macromolecule;
		SaxsProposal3Service saxsproposalService = (SaxsProposal3Service) ejb3ServiceLocator.getLocalService(SaxsProposal3Service.class);
		return saxsproposalService.findMacromoleculesById(macromoleculeId);
	}

	/**
	 * Workflow selected. It can envisaged to add priorities
	 * 
	 * @param workflows
	 * @return
	 */
	public Workflow3VO selectWorkFlow(List<Workflow3VO> workflows) {
		if (workflows.size() > 0) {
			return workflows.get(0);
		}
		return null;
	}

	private List<Map<String, Object>> getInfoBySubtractionId(String subtractionId) throws NamingException {
		Analysis3Service analysis3Service = (Analysis3Service) ejb3ServiceLocator.getLocalService(Analysis3Service.class);
		List<Map<String, Object>> result = analysis3Service.getCompactAnalysisBySubtractionId(subtractionId);
		return result;
	}

	@WebMethod(operationName = "getSubtractionById")
	@WebResult(name = "result")
	public String getSubtractionById(@WebParam(name = "subtractionId") String subtractionId) {
		long start = this.logInit("getSubtractionById");
		try {
			HashMap<String, Object> result = new HashMap<String, Object>();
			/** Getting subtractions **/
			Analysis3Service analysis3Service = (Analysis3Service) ejb3ServiceLocator.getLocalService(Analysis3Service.class);
			result.put("SUBTRACTION", getInfoBySubtractionId(subtractionId));

			this.logFinish("getSubtractionById", start);
			return getGson().toJson(result);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WORKFLOW_ERROR, "getAllBySubtractionId", start,
			System.currentTimeMillis(), e.getMessage(), e);
		}
		return null;

	}

	@WebMethod
	public byte[] getFile(String filePath) {
		long start = this.logInit("getFile");
		try {
			byte[] result =  ispyb.common.util.IspybFileUtils.getFile(filePath.trim());
			this.logFinish("getFile", start);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WORKFLOW_ERROR, "getFile", start,
			System.currentTimeMillis(), e.getMessage(), e);
		}
		return null;
	}

	/**
	 * /data/pyarch/bm29/mx1525/2252/1d/ND_046_00002.dat
	 * /data/pyarch/bm29/mx1525/workflows/{workflowId}/
	 * 
	 * @param workflowId
	 * @return
	 * @throws Exception
	 */
	private String getDestinationFolder(int workflowId) throws Exception{
		Workflow3Service workflow3Service = (Workflow3Service) ejb3ServiceLocator.getLocalService(Workflow3Service.class);
		Workflow3VO workflow = workflow3Service.findByPk(workflowId);
		if (workflow != null){
			//TODO extract the correct folder from another possibility because proposalId has been removed
			// from session for example
			//Proposal3Service proposal3Service = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
			//Proposal3VO proposal = proposal3Service.findByPk(workflow.getProposalId());
			String folder = "test"; //proposal.getCode().toLowerCase() + proposal.getNumber();
			return Constants.DATA_PDB_FILEPATH_START +  folder + "/workflows/" + workflow.getWorkflowId().toString();
		}
		return null;
	}
	
	
	@WebMethod
	public void upload(
			@WebParam(name = "workflowId") int workflowId,
			@WebParam(name = "fileName") String fileName, 
			@WebParam(name = "bytes") byte[] bytes) {
		
		long start = this.logInit("upload");
		LOG.info("fileName: " + fileName);
		
		try {
			String folder = this.getDestinationFolder(workflowId);
			/** Getting destination folder **/
			String filePath = folder + "/" + fileName;
			
			File destination = new File(folder);
			if (! destination.exists()){
				destination.mkdirs();
			}
			/** Copying bytes to disk **/
			FileOutputStream fos = new FileOutputStream(filePath);
			BufferedOutputStream outputStream = new BufferedOutputStream(fos);
			outputStream.write(bytes);
			outputStream.close();
			
			System.out.println("Received file: " + filePath);
			this.logFinish("upload", start);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WORKFLOW_ERROR, "upload", start,
					System.currentTimeMillis(), e.getMessage(), e);
		}
	}

	@WebMethod(operationName = "setWorkFlowStatus")
	public void setWorkFlowStatus(@WebParam(name = "workflowId") String workflowId, @WebParam(name = "status") String status) {
		long start = this.logInit("setWorkFlowStatus");
		try {
			LOG.info("workFlowId: " + workflowId);
			LOG.info("status: " + status);

			Workflow3Service workflow3Service = (Workflow3Service) ejb3ServiceLocator.getLocalService(Workflow3Service.class);

			Workflow3VO workflow3vo = workflow3Service.findByPk(Integer.parseInt(workflowId));
			workflow3vo.setStatus(status);
			workflow3Service.update(workflow3vo);
			this.logFinish("setWorkFlowStatus", start);

		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WORKFLOW_ERROR, "setWorkFlowStatus", start,
					System.currentTimeMillis(), e.getMessage(), e);
		}
	}
	
	
	@WebMethod(operationName = "uploadFitStructureToData")
	public void uploadFitStructureToData(@WebParam(name = "workflowId") String workflowId,
									@WebParam(name = "subtractionId") String subtractionId,
									@WebParam(name = "structureId") String structureId,
									@WebParam(name = "summaryFile") String summaryFile,
									@WebParam(name = "fitFile") String fitFile,
									@WebParam(name = "logFile") String logFile) {
		long start = this.logInit("uploadFitStructureToData");
		try {
			LOG.info("workFlowId: " + workflowId);
			LOG.info("subtractionId: " + subtractionId);
			LOG.info("structureId: " + structureId);
			LOG.info("summaryFile: " + summaryFile);
			LOG.info("fitFile: " + fitFile);
			LOG.info("logFile: " + logFile);
			
			/** Changing relative path **/
			LOG.info("logFile: " + this.getDestinationFolder(Integer.parseInt(workflowId)) + "/" + logFile);
			LOG.info("fitFile: " + this.getDestinationFolder(Integer.parseInt(workflowId)) + "/" + fitFile);
			LOG.info("summaryFile: " + this.getDestinationFolder(Integer.parseInt(workflowId)) + "/" + summaryFile);
			logFile = this.getDestinationFolder(Integer.parseInt(workflowId)) + "/" + logFile;
			fitFile = this.getDestinationFolder(Integer.parseInt(workflowId)) + "/" + fitFile;
			summaryFile = this.getDestinationFolder(Integer.parseInt(workflowId)) + "/" + summaryFile;
			
			/** Creating FitStructure **/
			AdvancedAnalysis3Service advancedAnalysis3Service = (AdvancedAnalysis3Service) ejb3ServiceLocator.getLocalService(AdvancedAnalysis3Service.class);
			
			FitStructureToExperimentalData3VO fitStructure = advancedAnalysis3Service.getFitStructureToExperimentDatalByWorkflowId(Integer.parseInt(workflowId));
			if (fitStructure == null){
				fitStructure = new FitStructureToExperimentalData3VO();
			}
			
			fitStructure.setFitFilePath(fitFile);
			fitStructure.setLogFilePath(logFile);
			fitStructure.setOutputFilePath(summaryFile);
			fitStructure.setStructureId(Integer.parseInt(structureId));
			fitStructure.setSubtractionId(Integer.parseInt(subtractionId));
			fitStructure.setWorkflowId(Integer.parseInt(workflowId));
			fitStructure.setComments("Finished at " + getNow());
			fitStructure = advancedAnalysis3Service.merge(fitStructure);
			
			
			this.logFinish("uploadFitStructureToData", start);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WORKFLOW_ERROR, "uploadFitStructureToData", start,
					System.currentTimeMillis(), e.getMessage(), e);
		}
	}

	/** Loggers **/
	private long logInit(String methodName) {
		LOG.info("-----------------------");
		this.now = System.currentTimeMillis();
		LOG.info(methodName.toUpperCase());
		LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WORKFLOW, methodName, System.currentTimeMillis(),
				System.currentTimeMillis(), "");
		return this.now;
	}

	private long logInit(String methodName, LoggerFormatter.Package packageName) {
		LOG.info("-----------------------");
		this.now = System.currentTimeMillis();
		LOG.info(methodName.toUpperCase());
		LoggerFormatter.log(LOGGER, packageName, methodName, System.currentTimeMillis(), System.currentTimeMillis(), "");
		return this.now;

	}

	private void logFinish(String methodName, long id) {
		LOG.debug("### [" + methodName.toUpperCase() + "] Execution time was " + (System.currentTimeMillis() - this.now) + " ms.");
		LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WORKFLOW, methodName, id, System.currentTimeMillis(),
				System.currentTimeMillis() - this.now);

	}
}
