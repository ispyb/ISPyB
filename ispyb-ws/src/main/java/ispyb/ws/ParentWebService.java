package ispyb.ws;

import file.FileUploadForm;
import ispyb.common.util.Constants;
import ispyb.common.util.PropertyLoader;
import ispyb.server.biosaxs.services.core.plateType.PlateType3Service;
import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.services.core.samplePlate.Sampleplate3Service;
import ispyb.server.common.services.admin.SchemaStatusService;
import ispyb.server.common.services.config.MenuGroup3Service;
import ispyb.server.common.services.login.Login3Service;
import ispyb.server.common.services.proposals.LabContact3Service;
import ispyb.server.common.services.proposals.Laboratory3Service;
import ispyb.server.common.services.proposals.Person3Service;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.services.shipping.Container3Service;
import ispyb.server.common.services.shipping.Dewar3Service;
import ispyb.server.common.services.shipping.DewarTransportHistory3Service;
import ispyb.server.common.services.shipping.Shipping3Service;
import ispyb.server.common.services.shipping.external.External3Service;
import ispyb.server.common.services.ws.rest.shipment.ShipmentRestWsService;
import ispyb.server.common.util.LoggerFormatter;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.services.collections.DataCollectionGroup3Service;
import ispyb.server.mx.services.collections.Image3Service;
import ispyb.server.mx.services.sample.Protein3Service;
import ispyb.server.common.services.shipping.DewarAPIService;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.naming.NamingException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Path("/")
public  class ParentWebService {
	protected long now;
	private final static Logger log = Logger.getLogger(ParentWebService.class);
		
	protected Response sendImage(String filePath) {
		if (filePath != null) {
			if (new File(filePath).exists())
				try {
					{
						File file = new File(filePath);
						ResponseBuilder response = Response.ok((Object) file);
						response.header("Content-Disposition", "attachment; filename=" + new File(filePath).getName());
						return response.header("Access-Control-Allow-Origin", "*").build();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		return Response.status(Response.Status.NOT_FOUND).header("Access-Control-Allow-Origin", "*").build();
	}
	
	protected Response downloadFileAsAttachment(String filePath) throws Exception {
		File file = new File(filePath);
		if (file.exists()) {
			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename=" + file.getName());
			return response.header("Access-Control-Allow-Origin", "*").build();
		}
		else{
			throw new Exception("File " + file.getAbsolutePath() + " does not exist");
		}
	}
	
	protected Response downloadFile(String filePath) throws Exception {
		File file = new File(filePath);
		if (file.exists()) {
			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "filename=" + file.getName());
			return response.header("Access-Control-Allow-Origin", "*").build();
		}
		else{
			throw new Exception("File " + file.getAbsolutePath() + " does not exist");
		}
	}
	
	
	
	/**
	 * File name can not contain commas!!!
	 * @param bs
	 * @param fileName
	 * @return
	 */
	protected Response downloadFile(byte[] bs, String fileName) {
		ResponseBuilder response = Response.ok((Object) bs);
		response.header("Content-Disposition", "attachment; filename=" + fileName);
		return response.header("Access-Control-Allow-Origin", "*").build();
	}
		
	/** TODO: it does not work when retrieving using Constants class **/
	protected String getFolderForUploads() {
		Properties mProp3 = PropertyLoader.loadProperties("ISPyB");
		return mProp3.getProperty("ISPyB.uploaded.root.folder");
	}
	
	/** Folder where the pdb and all the other apriori information files will be uploaded **/
	protected String getTargetFolder(int proposalId) throws Exception {
		Proposal3VO proposal = this.getProposal3Service().findProposalById(proposalId);
		String proposalName = proposal.getCode().toLowerCase() + proposal.getNumber();
		if (Constants.SITE_IS_EMBL()) {
			proposalName = proposal.getNumber();
		}
		return  this.getFolderForUploads() +  proposalName;
	}
	
	protected String copyFileToDisk(String proposal, FileUploadForm form) throws Exception {
		return this.copyFileToDisk(proposal, form, form.getFileName());
		
	}
	
	protected String copyFileToDisk(String proposal, FileUploadForm form, String fileName) throws Exception {
		int proposalId = this.getProposalId(proposal);
		String filePath = this.getTargetFolder(proposalId) + "/" + fileName;
		/** If folder does not exist then it will be created **/
		if (!new File(this.getTargetFolder(proposalId)).exists()){
			java.nio.file.Path path = Paths.get(this.getTargetFolder(proposalId));
			log.info("Folder does not exist and will be created. folder=" + path);
			Files.createDirectory(path);
		}
		log.info("Copying file " + fileName + " to " + filePath );
		File file = new File(filePath);
		FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath());
		
		fileOut.write(IOUtils.toByteArray(form.getInputStream()));
		fileOut.close();
		log.info("File has been copied on " + filePath);
		return filePath;
		
	}
	
	protected Gson getGson() {
		return new GsonBuilder().serializeNulls().excludeFieldsWithModifiers(Modifier.PRIVATE).serializeSpecialFloatingPointValues()
				.create();
	}
	
	protected Gson getGson(boolean serializeNull) {
		if (serializeNull)
			return this.getGson();
					
		return new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).serializeSpecialFloatingPointValues()
				.create();
	}

	protected Gson getWithoutExposeAnnotationGson() {
		return new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).serializeSpecialFloatingPointValues()
				.excludeFieldsWithoutExposeAnnotation().create();
	}

	protected Response sendResponse(String response) {
		return Response.ok(response).header("Access-Control-Allow-Origin", "*").build();
	}

	protected Response sendResponse(Object response) {
		return Response.ok(getGson().toJson(response)).header("Access-Control-Allow-Origin", "*").build();
	}
	
	protected Response sendError(String message) {
		return Response.serverError().entity(message).header("Access-Control-Allow-Origin", "*").build();
	}
	
	protected Response sendResponse(Object response, boolean serializeNulls) {
		return Response.ok(getGson(serializeNulls).toJson(response)).header("Access-Control-Allow-Origin", "*").build();
	}

	protected Response sendOk() {
		return Response.ok().header("Access-Control-Allow-Origin", "*").build();
	}
	
	protected External3Service getExternal3Service() throws NamingException {
		return (External3Service) Ejb3ServiceLocator.getInstance().getLocalService(External3Service.class);
	}
	
	protected DewarTransportHistory3Service getDewarTransportHistory3Service() throws NamingException {
		return (DewarTransportHistory3Service) Ejb3ServiceLocator.getInstance().getLocalService(DewarTransportHistory3Service.class);
	}
	
	protected Protein3Service getProtein3Service() throws NamingException {
		return (Protein3Service) Ejb3ServiceLocator.getInstance().getLocalService(Protein3Service.class);
	}
	
	protected Container3Service getContainer3Service() throws NamingException {
		return (Container3Service) Ejb3ServiceLocator.getInstance().getLocalService(Container3Service.class);
	}
	
	
	protected ShipmentRestWsService getShipmentWsService() throws NamingException {
		return (ShipmentRestWsService) Ejb3ServiceLocator.getInstance().getLocalService(ShipmentRestWsService.class);
	}
		
	protected Sampleplate3Service getSamplePlate3Service() throws NamingException {
		return (Sampleplate3Service) Ejb3ServiceLocator.getInstance().getLocalService(Sampleplate3Service.class);
	}
	
	protected DataCollection3Service getDataCollection3Service() throws NamingException {
		return (DataCollection3Service) Ejb3ServiceLocator.getInstance().getLocalService(DataCollection3Service.class);
	}
	
	protected DataCollectionGroup3Service getDataCollectionGroup3Service() throws NamingException {
		return (DataCollectionGroup3Service) Ejb3ServiceLocator.getInstance().getLocalService(DataCollectionGroup3Service.class);
	}
	
	protected Image3Service getImage3Service() throws NamingException {
		return (Image3Service) Ejb3ServiceLocator.getInstance().getLocalService(Image3Service.class);
	}
	
	protected MenuGroup3Service getMenuGroup3Service() throws NamingException {
		return (MenuGroup3Service) Ejb3ServiceLocator.getInstance().getLocalService(MenuGroup3Service.class);
	}

	
	protected Session3Service getSession3Service() throws NamingException {
		return (Session3Service) Ejb3ServiceLocator.getInstance().getLocalService(Session3Service.class);
	}

	protected PlateType3Service getPlateType3Service() throws NamingException {
		return (PlateType3Service) Ejb3ServiceLocator.getInstance().getLocalService(PlateType3Service.class);
	}

	protected Login3Service getLogin3Service() throws NamingException {
		return (Login3Service) Ejb3ServiceLocator.getInstance().getLocalService(
				Login3Service.class);
	}
	
	protected LabContact3Service getLabContact3Service() throws NamingException {
		return (LabContact3Service) Ejb3ServiceLocator.getInstance().getLocalService(LabContact3Service.class);
	}
	
	protected Laboratory3Service getLaboratory3Service() throws NamingException {
		return (Laboratory3Service) Ejb3ServiceLocator.getInstance().getLocalService(Laboratory3Service.class);
	}
	
	protected Person3Service getPerson3Service() throws NamingException {
		return (Person3Service) Ejb3ServiceLocator.getInstance().getLocalService(Person3Service.class);
	}

	protected Shipping3Service getShipping3Service() throws NamingException {
		return (Shipping3Service) Ejb3ServiceLocator.getInstance().getLocalService(Shipping3Service.class);
	}

	protected SaxsProposal3Service getSaxsProposal3Service() throws NamingException {
		return (SaxsProposal3Service) Ejb3ServiceLocator.getInstance().getLocalService(SaxsProposal3Service.class);
	}
	
	protected Proposal3Service getProposal3Service() throws NamingException {
		return (Proposal3Service) Ejb3ServiceLocator.getInstance().getLocalService(Proposal3Service.class);
	}

	protected Dewar3Service getDewar3Service() throws NamingException {
		return (Dewar3Service) Ejb3ServiceLocator.getInstance().getLocalService(Dewar3Service.class);
	}

	protected DewarAPIService getDewarAPIService() throws NamingException {
		return (DewarAPIService) Ejb3ServiceLocator.getInstance().getLocalService(DewarAPIService.class);
	}
	
	protected SchemaStatusService getSchemaStatusService() throws NamingException {
		return (SchemaStatusService) Ejb3ServiceLocator.getInstance().getLocalService(SchemaStatusService.class);
	}

	/**
	 * Gets proposal Id by login name
	 * 
	 * @param proposal
	 * @return
	 * @throws Exception
	 */
	protected int getProposalId(String proposal) throws Exception {
		List<Proposal3VO> proposals = this.getProposal3Service().findProposalByLoginName(proposal);
		if (proposals != null) {
			if (proposals.size() > 0) {
				return proposals.get(0).getProposalId();
			}
		}
		throw new Exception("No proposal found.");
	}

	protected List<Map<String, Object>> filter(List<Map<String, Object>> elements, String key, String value)
			throws Exception {
		List<Map<String, Object>> filtered = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> element : elements) {
			if (element.containsKey(key)) {
				if (element.get(key) != null) {
					if (element.get(key).toString().trim().equals(value.trim())) {
						filtered.add(element);
					}
				}
			}
		}
		return filtered;
	}

	protected List<Map<String, Object>> filter(List<Map<String, Object>> elements, String key, List<String> values)
			throws Exception {
		log.info("values: " + values.toString());

		Set<String> hash = new HashSet<String>();
		for (String value : values) {
			log.info("value: " + value);
			hash.add(value.trim());
		}
		List<Map<String, Object>> filtered = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> element : elements) {
			if (element.containsKey(key)) {
				if (element.get(key) != null) {

					if (hash.contains(element.get(key).toString().trim())) {
						filtered.add(element);
					}
				}
			}
		}
		return filtered;
	}

	/**
	 * Form a comma separated string returns a list of integers
	 * 
	 * @param commaSeparated
	 * @return
	 */
	protected List<Integer> parseToInteger(String commaSeparated) {
		Set<String> keys = new HashSet<String>();
		if (commaSeparated != null) {
			List<String> myList = Arrays.asList(commaSeparated.split(","));
			ArrayList<Integer> intList = new ArrayList<Integer>();
			if (!myList.equals("")) {
				for (String string : myList) {
					try {
						/** We remove repeated ids **/
						if (!keys.contains(string)) {
							if (string.matches("\\d+")) {
								intList.add(Integer.valueOf(string));
								keys.add(string);
							}
						}
					} catch (Exception e) {
						/** No parseable value ***/
						log.info(e.getMessage());
					}
				}
			}
			return intList;
		}
		return null;
	}

	protected List<String> parseToString(String commaSeparated) {
		if (commaSeparated != null) {
			return Arrays.asList(commaSeparated.split(","));
		}
		return null;
	}

	protected long logInit(String methodName, Logger logger, Object... args) {
		logger.info("-----------------------");
		this.now = System.currentTimeMillis();
		ArrayList<String> params = new ArrayList<String>();
		for (Object object : args) {
			if (object != null){
				params.add(object.toString());
			}
			else{
				params.add("null");
			}
		}
		logger.info(methodName.toUpperCase());
		LoggerFormatter.log(logger, LoggerFormatter.Package.ISPyB_API, methodName, System.currentTimeMillis(),
				System.currentTimeMillis(), this.getGson().toJson(params));
		return this.now;
	}

	protected long logInit(String methodName, String params, Logger logger) {
		logger.info("-----------------------");
		this.now = System.currentTimeMillis();
		logger.info(methodName.toUpperCase());
		LoggerFormatter.log(logger, LoggerFormatter.Package.ISPyB_API, methodName, System.currentTimeMillis(),
				System.currentTimeMillis(), params);
		return this.now;
	}

	protected void logFinish(String methodName, long start, Logger logger) {
		logger.debug("### [" + methodName.toUpperCase() + "] Execution time was "
				+ (System.currentTimeMillis() - this.now) + " ms.");
		LoggerFormatter.log(logger, LoggerFormatter.Package.ISPyB_API, methodName, start, System.currentTimeMillis(),
				System.currentTimeMillis() - this.now);

	}

	protected Response logError(String methodName, Exception e, long start, Logger logger) {
		return this.logError( methodName,  e,  start,  logger, LoggerFormatter.Package.ISPyB_API_ERROR);
	}
	
	protected Response logError(String methodName, Exception e, long start, Logger logger,  LoggerFormatter.Package myPackage) {
		e.printStackTrace();
		LoggerFormatter.log(logger, myPackage, methodName, start,System.currentTimeMillis(), e.getMessage(), e);
		return this.sendResponse(e.getMessage());
	}

}