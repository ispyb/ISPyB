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

package ispyb.ws.soap.crims;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
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

import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.shipping.Shipping3Service;
import ispyb.server.common.services.shipping.external.External3Service;
import ispyb.server.common.util.LoggerFormatter;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.proposals.ProposalWS3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.services.collections.Image3Service;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.Image3VO;
import ispyb.server.mx.vos.sample.Protein3VO;
import ispyb.server.smis.UpdateFromSMIS;

/**
 * Web services for BLSample
 * 
 * @author BODIN
 * 
 */
@WebService(name = "CrimsWebService", serviceName = "ispybWS", targetNamespace = "http://ispyb.ejb3.webservices.crims")
@SOAPBinding(style = Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Stateless
@RolesAllowed({ "WebService", "User", "Industrial" })
@SecurityDomain("ispyb")
@WebContext(authMethod = "BASIC", secureWSDLAccess = false, transportGuarantee = "NONE")
public class CrimsWebService {
	private final static Logger LOG = Logger.getLogger(CrimsWebService.class);

	private long now;

	@Resource
	private SessionContext sessionContext;

	@WebMethod(operationName = "echo")
	@WebResult(name = "echo")
	public String echo() {
		return "echo from server...";
	}

	private SaxsProposal3Service getSaxsProposal3Service() throws NamingException{
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		return (SaxsProposal3Service) ejb3ServiceLocator.getLocalService(SaxsProposal3Service.class);
	}
	
	private Proposal3Service getProposal3Service() throws NamingException{
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		return (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
		
	}
	
	private Shipping3Service getShipping3Service() throws NamingException{
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		return (Shipping3Service) ejb3ServiceLocator.getLocalService(Shipping3Service.class);
		
	}
	
	/**
	 * Method to be called with user credentials and will get all the proteins linked to that user
	 * @return
	 * @throws Exception
	 */
	@WebMethod
	@WebResult(name = "listProteinAcronyms")
	public String findProteinAcronyms() throws Exception {
		long id = 0;
		try {
			String userName = sessionContext.getCallerPrincipal().getName();
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("userName", String.valueOf(userName));
			id = this.logInit("findProteinAcronyms", new Gson().toJson(params));
			
			List<Proposal3VO> proposals = getProposal3Service().findProposalByLoginName(userName);
			HashMap<String, List<String>> listProteinAcronyms = new HashMap<String, List<String>>();
			
			for (Proposal3VO proposal3vo : proposals) {
				List<Proposal3VO> filled = getProposal3Service().findByCodeAndNumber(proposal3vo.getCode(), proposal3vo.getNumber(), false, true, false);
				if (filled.size() > 0){
					for (Proposal3VO proposal3vo2 : filled) {
						listProteinAcronyms.put(proposal3vo2.getCode() + proposal3vo2.getNumber(), this.getProteinAcronymByProposal(proposal3vo2));
					}
				}
			}
			
			this.logFinish("findProteinAcronyms", id);
			return getGson().toJson(listProteinAcronyms);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOG, LoggerFormatter.Package.CRIMS_WS_ERROR, "findProteinAcronyms", id,
					System.currentTimeMillis(), e.getMessage(), e);
			throw e;
		}
	}
	
	private List<String> getProteinAcronymByProposal(Proposal3VO proposal){
		List<String> listProteinAcronyms = new ArrayList<String>();
		Set<Protein3VO> proteinVOs = proposal.getProteinVOs();
		if (proteinVOs != null) {
			List<Protein3VO> listProtein = new ArrayList<Protein3VO>(Arrays.asList(proteinVOs
					.toArray(new Protein3VO[proteinVOs.size()])));
			for (Iterator<Protein3VO> p = listProtein.iterator(); p.hasNext();) {
				listProteinAcronyms.add(p.next().getAcronym());
			}
		}
		return listProteinAcronyms;
	}
	
//	private HashMap<String, List<String>> getProteinAcronymByProposals(List<Proposal3VO> proposals){
//		HashMap<String, List<String>> listAcronyms = new HashMap<String, List<String>>();
//		if (proposals != null && proposals.size() > 0) {
//			for (Proposal3VO proposal3vo : proposals) {
//				listAcronyms.put(proposal3vo.getCode() + proposal3vo.getNumber(), this.getProteinAcronymByProposal(proposal3vo));	
//			}
//		}
//		return listAcronyms;
//	}
//	
	
	@WebMethod
	@WebResult(name = "listProteinAcronyms")
	public String findProteinAcronymsForProposal(
			@WebParam(name = "proposalCode")String proposalCode, @WebParam(name = "proposalNumber")
	String proposalNumber) throws Exception {
		long id = 0;
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("proposalCode", String.valueOf(proposalCode));
			params.put("proposalNumber", String.valueOf(proposalNumber));
			
			LOG.info("params = " + params.toString());

			id = this.logInit("findProteinAcronymsForProposal", new Gson().toJson(params));
			
			//checkUserIsCrims();
			
			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Proposal3Service proposalService = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);

			List<String> listProteinAcronyms = new ArrayList<String>();

			List<Proposal3VO> proposals = proposalService.findByCodeAndNumber(proposalCode, proposalNumber, false, true, false);
			if (proposals != null && proposals.size() > 0) {
				Proposal3VO proposal3VO = proposals.get(0);
				LOG.info("proposal = " + proposal3VO.toString());
				Set<Protein3VO> proteinVOs = proposal3VO.getProteinVOs();
				LOG.info("proteinsVOs = " + proteinVOs.toString());
				if (proteinVOs != null) {
					List<Protein3VO> listProtein = new ArrayList<Protein3VO>(Arrays.asList(proteinVOs
							.toArray(new Protein3VO[proteinVOs.size()])));
					for (Iterator<Protein3VO> p = listProtein.iterator(); p.hasNext();) {
						listProteinAcronyms.add(p.next().getAcronym());
						LOG.info("listProteinAcronyms = " + listProteinAcronyms.toString());
					}
				}
			}
			this.logFinish("findProteinAcronymsForProposal", id);
			return getGson().toJson(listProteinAcronyms);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOG, LoggerFormatter.Package.CRIMS_WS_ERROR, "findProteinAcronymsForProposal", id,
					System.currentTimeMillis(), e.getMessage(), e);
			throw e;
		}
	}

	private Gson getGson(){
		return new GsonBuilder().serializeNulls().create();
	}
	
	/**
	 * EXTERNAL WEBSERVICES FOR CRIMS
	 * 
	 * @param proposalCode
	 * @param proposalNumber
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@WebMethod
	public String storeShipping(@WebParam(name = "proposalCode")
	String proposalCode, @WebParam(name = "proposalNumber")
	String proposalNumber, @WebParam(name = "shipping")
	String json) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("proposalCode", String.valueOf(proposalCode));
		params.put("proposalNumber", String.valueOf(proposalNumber));
		params.put("shipping", String.valueOf(json));
		long id = this.logInit("storeShipping", new Gson().toJson(params));
		
		try {
//			checkUserIsCrims();
			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			External3Service external3Service = (External3Service) ejb3ServiceLocator.getLocalService(External3Service.class);
			Shipping3VO shipping = new Gson().fromJson(json, Shipping3VO.class);
			Shipping3VO shipping3VO = external3Service.storeShipping(proposalCode, proposalNumber, shipping);
			if (shipping3VO != null) {
				String result = getGson().toJson(external3Service.getDataCollectionFromShippingId(shipping3VO.getShippingId()));
				this.logFinish("storeShipping", id);
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOG, LoggerFormatter.Package.CRIMS_WS_ERROR, "storeShipping", id,
					System.currentTimeMillis(), e.getMessage(), e);
			throw e;
		}
		return null;
	}
	
	// store also the Position of BLSubSamples
	@WebMethod
	public String storeShippingFull(@WebParam(name = "proposalCode")
	String proposalCode, @WebParam(name = "proposalNumber")
	String proposalNumber, @WebParam(name = "shipping")
	String json) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("proposalCode", String.valueOf(proposalCode));
		params.put("proposalNumber", String.valueOf(proposalNumber));
		params.put("shipping", String.valueOf(json));
		long id = this.logInit("storeShippingFull", new Gson().toJson(params));
		
		try {
//			checkUserIsCrims();
			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			External3Service external3Service = (External3Service) ejb3ServiceLocator.getLocalService(External3Service.class);
			LOG.debug("jso= " + json);
			Shipping3VO shipping = new Gson().fromJson(json, Shipping3VO.class);
			LOG.debug("ship= " + shipping);
			Shipping3VO shipping3VO = external3Service.storeShippingFull(proposalCode, proposalNumber, shipping);
			if (shipping3VO != null) {
				String result = getGson().toJson(external3Service.getDataCollectionFromShippingId(shipping3VO.getShippingId()));
				this.logFinish("storeShipping", id);
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOG, LoggerFormatter.Package.CRIMS_WS_ERROR, "storeShipping", id,
					System.currentTimeMillis(), e.getMessage(), e);
			throw e;
		}
		return null;
	}
	
	@WebMethod
	public String getDataCollectionByProposal(@WebParam(name = "proposalCode")
	String proposalCode, @WebParam(name = "proposalNumber")
	String proposalNumber) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("proposalCode", String.valueOf(proposalCode));
		params.put("proposalNumber", String.valueOf(proposalNumber));
		long id = this.logInit("getDataCollectionByProposal", new Gson().toJson(params));
		try {
			checkUserIsCrims();
			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			External3Service external3Service = (External3Service) ejb3ServiceLocator.getLocalService(External3Service.class);
			String result = getGson().toJson(external3Service.getDataCollectionByProposal(proposalCode, proposalNumber));
			this.logFinish("getDataCollectionByProposal", id);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOG, LoggerFormatter.Package.CRIMS_WS_ERROR, "getDataCollectionByProposal", id,
					System.currentTimeMillis(), e.getMessage(), e);
			throw e;
		}
	}

	@WebMethod
	public String getSessionByProposalCodeAndNumber(@WebParam(name = "proposalCode")
	String proposalCode, @WebParam(name = "proposalNumber")
	String proposalNumber) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("proposalCode", String.valueOf(proposalCode));
		params.put("proposalNumber", String.valueOf(proposalNumber));
		long id = this.logInit("getSessionByProposalCodeAndNumber", new Gson().toJson(params));
		try {
//			checkUserIsCrims();
			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			External3Service external3Service = (External3Service) ejb3ServiceLocator.getLocalService(External3Service.class);
			String result = getGson().toJson(external3Service.getSessionsByProposalCodeAndNumber(proposalCode, proposalNumber));
			this.logFinish("getSessionByProposalCodeAndNumber", id);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOG, LoggerFormatter.Package.CRIMS_WS_ERROR, "getSessionByProposalCodeAndNumber", id,
					System.currentTimeMillis(), e.getMessage(), e);
			throw e;
		}
	}

	/** It checks that user is CRIMS **/
	private void checkUserIsCrims() throws Exception{
		if (!sessionContext.getCallerPrincipal().getName().equals("crims")){
			throw new Exception("Access not authorized to user: " + sessionContext.getCallerPrincipal().getName());
		}
		
	}
	@WebMethod
	public String getDataCollectionFromShippingId(@WebParam(name = "shippingId")
	String shippingId) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("shippingId", String.valueOf(shippingId));
		long id = this.logInit("getDataCollectionFromShippingId", new Gson().toJson(params));
		try {
			checkUserIsCrims();

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			External3Service external3Service = (External3Service) ejb3ServiceLocator.getLocalService(External3Service.class);
			String result = getGson().toJson(external3Service.getDataCollectionFromShippingId(Integer.parseInt(shippingId)));
			this.logFinish("getDataCollectionFromShippingId", id);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOG, LoggerFormatter.Package.CRIMS_WS_ERROR, "getDataCollectionFromShippingId", id,
					System.currentTimeMillis(), e.getMessage(), e);
			throw e;
		}
	}

	@WebMethod
	public String getAllDataCollectionFromShippingId(@WebParam(name = "shippingId")
	String shippingId) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("shippingId", String.valueOf(shippingId));
		long id = this.logInit("getAllDataCollectionFromShippingId", new Gson().toJson(params));
		try {
			checkUserIsCrims();
			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			External3Service external3Service = (External3Service) ejb3ServiceLocator.getLocalService(External3Service.class);
			String result = getGson().toJson(external3Service.getAllDataCollectionFromShippingId(Integer.parseInt(shippingId)));
			this.logFinish("getAllDataCollectionFromShippingId", id);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOG, LoggerFormatter.Package.CRIMS_WS_ERROR, "getAllDataCollectionFromShippingId", id,
					System.currentTimeMillis(), e.getMessage(), e);
			throw e;
		}
	}

	@WebMethod
	public byte[] getFileByImageId(@WebParam(name = "imageId")
	String imageId) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("imageId", String.valueOf(imageId));
		long id = this.logInit("getFileByImageId", new Gson().toJson(params));
		try {
			checkUserIsCrims();
			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Image3Service image3Service = (Image3Service) ejb3ServiceLocator.getLocalService(Image3Service.class);
			try {
				Image3VO image3VO = image3Service.findByPk(Integer.parseInt(imageId));
				if (image3VO != null) {
					if (image3VO.getFileLocation() != null) {
						byte[] result = ispyb.common.util.IspybFileUtils.getFile(image3VO.getJpegThumbnailFileFullPath().trim());
						this.logFinish("getFileByImageId", id);
						return result;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				LoggerFormatter.log(LOG, LoggerFormatter.Package.CRIMS_WS_ERROR, "getFileByImageId", id,
						System.currentTimeMillis(), e.getMessage(), e);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOG, LoggerFormatter.Package.CRIMS_WS_ERROR, "getFileByImageId", id,
					System.currentTimeMillis(), e.getMessage(), e);
			throw e;
		}
	}

	
	@WebMethod
	public byte[] getSnapshotFileByDataCollectionId(
			@WebParam(name = "dataCollectionId") String dataCollectionId,
			@WebParam(name = "index") int index) throws Exception {
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("dataCollectionId", String.valueOf(dataCollectionId));
		params.put("index", String.valueOf(index));
		
		long id = this.logInit("getSnapshotFileByDataCollectionId", new Gson().toJson(params));
		try {
			checkUserIsCrims();
			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			DataCollection3Service dataCollection3Service = (DataCollection3Service) ejb3ServiceLocator.getLocalService(DataCollection3Service.class);
			try {
				DataCollection3VO dc = dataCollection3Service.findByPk(Integer.parseInt(dataCollectionId), false, false);
				if (dc != null) {
					String filePath = null;
					switch (index) {
					case 1:
						 filePath = dc.getXtalSnapshotFullPath1();
						 break;
					case 2:
						 filePath = dc.getXtalSnapshotFullPath2();
						 break;
					case 3:
						 filePath = dc.getXtalSnapshotFullPath3();
						 break;
					case 4:
						 filePath = dc.getXtalSnapshotFullPath4();
						 break;
					default:
						throw new Exception("Index should be in the range [1-4]");
					}
					
					System.out.println(filePath);
					if (filePath != null) {
						if (new File(filePath.trim()).exists()){
							byte[] result = ispyb.common.util.IspybFileUtils.getFile(filePath.trim());
							this.logFinish("getSnapshotFileByDataCollectionId", id);
							return result;
						}
						else{
							throw new Exception(filePath.trim() + " not found");
						}
					}
					else{
						throw new Exception("File path is null");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				LoggerFormatter.log(LOG, LoggerFormatter.Package.CRIMS_WS_ERROR, "getSnapshotFileByDataCollectionId", id,
						System.currentTimeMillis(), e.getMessage(), e);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOG, LoggerFormatter.Package.CRIMS_WS_ERROR, "getSnapshotFileByDataCollectionId", id,
					System.currentTimeMillis(), e.getMessage(), e);
			throw e;
		}
	}
	
	
	@WebMethod
	public void updateDataBaseByProposal(@WebParam(name = "proposalCode")
	String proposalCode, @WebParam(name = "proposalNumber")
	String proposalNumber) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("proposalCode", String.valueOf(proposalCode));
		params.put("proposalNumber", String.valueOf(proposalNumber));
		
		long id = this.logInit("updateDataBaseByProposal", new Gson().toJson(params));
		try {
			checkUserIsCrims();
			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Proposal3Service proposal3Service = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
			ProposalWS3VO proposal = proposal3Service.findForWSByCodeAndNumber(proposalCode, proposalNumber);
			if (proposal != null) {
				UpdateFromSMIS.updateProposalFromSMIS(proposalCode, proposalNumber);
			} else {
				throw new Exception("Proposal: " + proposalCode + proposalNumber + " not found");
			}

			this.logFinish("updateDataBaseByProposal", id);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOG, LoggerFormatter.Package.CRIMS_WS_ERROR, "updateDataBaseByProposal", id,
					System.currentTimeMillis(), e.getMessage(), e);
			throw e;
		}
	}
	
	@WebMethod
	public String getAutoprocResultByDataCollectionIdList(@WebParam(name = "dataCollectionIdList" ) String dataCollectionIdList) throws Exception {
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("dataCollectionIdList", String.valueOf(dataCollectionIdList));
		long id = this.logInit("getAutoprocResultByDataCollectionIdList", new Gson().toJson(params));
		try {
			checkUserIsCrims();
			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			External3Service external3Service = (External3Service) ejb3ServiceLocator.getLocalService(External3Service.class);
			
			Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
		    ArrayList<Integer> inList = getGson().fromJson(dataCollectionIdList, type);

			String result = getGson().toJson(external3Service.getAutoprocResultByDataCollectionIdList(inList));
			this.logFinish("getAutoprocResultByDataCollectionIdList", id);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			LoggerFormatter.log(LOG, LoggerFormatter.Package.CRIMS_WS_ERROR, "getAutoprocResultByDataCollectionIdList", id,
					System.currentTimeMillis(), e.getMessage(), e);
			throw e;
		}
	}
	

	/** Loggers **/
	protected long logInit(String methodName, String params) {
		this.now = System.currentTimeMillis();
		LoggerFormatter.log(LOG, LoggerFormatter.Package.CRIMS_WS, methodName, System.currentTimeMillis(),
				System.currentTimeMillis(), params);
		return this.now;
	}

	private void logFinish(String methodName, long id) {
		LOG.debug("### [" + methodName.toUpperCase() + "] Execution time was " + (System.currentTimeMillis() - this.now) + " ms.");
		LoggerFormatter.log(LOG, LoggerFormatter.Package.CRIMS_WS, methodName, id, System.currentTimeMillis(),
				System.currentTimeMillis() - this.now);

	}

}