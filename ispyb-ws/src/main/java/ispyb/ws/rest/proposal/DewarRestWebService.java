package ispyb.ws.rest.proposal;

import ispyb.common.util.StringUtils;
import ispyb.common.util.Constants;
import ispyb.common.util.PDFFormFiller;
import ispyb.server.biosaxs.vos.dataAcquisition.StockSolution3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Sampleplate3VO;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Laboratory3VO;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.DewarTransportHistory3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.services.ws.rest.dewar.DewarRestWsService;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.ws.rest.RestWebService;
import ispyb.server.common.vos.proposals.ProposalWS3VO;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

@Path("/")
public class DewarRestWebService extends RestWebService {
	private final static Logger logger = Logger
			.getLogger(DewarRestWebService.class);

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/shipping/{shippingId}/dewar/{dewarId}/label")
	@Produces({ "application/json" })
	public Response labelDewar(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("shippingId") int shippingId,
			@PathParam("dewarId") int dewarId) throws NamingException {
		return Response.serverError().build();
	}
	
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/shipping/{shippingId}/dewar/{dewarId}/labels")
	@Produces({ "application/pdf" })
	public Response getLabels(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("shippingId") int shippingId,
			@PathParam("dewarId") int dewarId) throws NamingException {

		long start = this.logInit("getLabels", logger, token, proposal,
				shippingId, dewarId);

		try {
			byte[] pdf = this.getLabels(dewarId);
			return this.downloadFile(pdf, "parcel_" + dewarId +".pdf");
		} catch (Exception e) {
			return this.logError("getLabels", e, start, logger);
		}
	}
	
	@Deprecated
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/shipping/{shippingId}/dewar/list")
	@Produces({ "application/json" })
	public Response getContainers(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("shippingId") Integer shippingId) throws Exception {

		long id = this.logInit("getContainers", logger, token, proposal, shippingId);
		try {
			List<Map<String, Object>> result = this.getShipping3Service().getShippingById(shippingId);
			this.logFinish("getContainers", id, logger);
			return sendResponse(result);
		} catch (Exception e) {
			return this.logError("getContainers", e, id, logger);
		}

	}
	
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/dewar/list")
	@Produces({ "application/json" })
	public Response getDewars(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal
			) throws Exception {

		long id = this.logInit("getDewars", logger, token, proposal);
		try {
			//List<Dewar3VO> dewars = this.getDewar3Service().findByProposalId(this.getProposalId(proposal));
			List<Map<String, Object>> dewars = this.getDewarRestWebService().getDewarViewByProposalId(this.getProposalId(proposal));
			this.logFinish("getDewars", id, logger);
			return sendResponse(dewars);
		} catch (Exception e) {
			return this.logError("getContainers", e, id, logger);
		}
	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/dewar/status/{status}/list")
	@Produces({ "application/json" })
	public Response getDewarsByStatus(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal, 
			@PathParam("status") String status) throws Exception {

		long id = this.logInit("getDewarsByStatus", logger, token, proposal);
		try {
			List<Dewar3VO> dewars = this.getDewar3Service().findByProposalId(this.getProposalId(proposal));
			List<Dewar3VO> result = new ArrayList<Dewar3VO>();
			for (Dewar3VO dewar3vo : dewars) {
				if (dewar3vo.getDewarStatus() != null){
					if (dewar3vo.getDewarStatus().toUpperCase().equals(status.toUpperCase())){
						result.add(dewar3vo);
					}
				}
			}
			this.logFinish("getDewarsByStatus", id, logger);
			return sendResponse(result);
		} catch (Exception e) {
			return this.logError("getDewarsByStatus", e, id, logger);
		}
	}
	
	
	protected DewarRestWsService getDewarRestWebService() throws NamingException {
		return (DewarRestWsService) Ejb3ServiceLocator.getInstance().getLocalService(DewarRestWsService.class);
	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/dewar/session/{sessionId}/list")
	@Produces({ "application/json" })
	public Response getDewarsBySession(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal, 
			@PathParam("sessionId") int sessionId) throws Exception {

		long id = this.logInit("getDewarsBySession", logger, token, proposal);
		try {
			List<Map<String, Object>> dewars = this.getDewarRestWebService().getDewarViewBySessionId(sessionId, this.getProposalId(proposal));
			this.logFinish("getDewarsBySession", id, logger);
			return sendResponse(dewars);
		} catch (Exception e) {
			return this.logError("getDewarsBySession", e, id, logger);
		}
	}
	
	
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/shipping/{shippingId}/dewar/{dewarId}/remove")
	@Produces({ "application/json" })
	public Response removeDewar(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("shippingId") int shippingId,
			@PathParam("dewarId") int dewarId) throws NamingException {

		long start = this.logInit("removeDewar", logger, token, proposal,
				shippingId, dewarId);

		try {
			List<Sampleplate3VO> sampleplate3VOs = getSamplePlate3Service()
					.getSamplePlatesByBoxId(String.valueOf(dewarId));
			for (Sampleplate3VO plate : sampleplate3VOs) {
				if (plate.getBoxId() != null) {
					if (plate.getBoxId() == dewarId) {
						plate.setBoxId(null);
						getSamplePlate3Service().merge(plate);
					}
				}
			}
			List<StockSolution3VO> stockSolution3VOs = getSaxsProposal3Service()
					.findStockSolutionsByBoxId(String.valueOf(dewarId));
			for (StockSolution3VO stockSolution3VO : stockSolution3VOs) {
				if (stockSolution3VO.getBoxId() != null) {
					if (stockSolution3VO.getBoxId() == dewarId) {
						stockSolution3VO.setBoxId(null);
						getSaxsProposal3Service().merge(stockSolution3VO);
					}
				}
			}
			this.getDewar3Service().deleteByPk(dewarId);
			this.logFinish("removeDewar", start, logger);
			return this.sendResponse(getShipping3Service().findByPk(shippingId,
					true));
		} catch (Exception e) {
			return this.logError("removeDewar", e, start, logger);
		}
	}

	/**
	 * getDateTime
	 * 
	 * @return
	 */
	private Timestamp getDateTime() {
		java.util.Date today = new java.util.Date();
		return (new java.sql.Timestamp(today.getTime()));
	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@POST
	@Path("{token}/proposal/{proposal}/shipping/{shippingId}/dewar/save")
	@Produces({ "application/json" })
	public Response saveDewar(
			@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("shippingId") int shippingId,
			@FormParam("sessionId") Integer sessionId,
			@FormParam("dewarId") Integer dewarId,
			@FormParam("code") String code,
			@FormParam("comments") String comments,
			@FormParam("storageLocation") String storageLocation,
			@FormParam("dewarStatus") String dewarStatus,
			@FormParam("blTimeStamp") String blTimeStamp,
			@FormParam("isStorageDewar") String isStorageDewar,
			@FormParam("barCode") String barCode,
			@FormParam("customValue") String customValue,
			@FormParam("transportValue") String transportValue,
			@FormParam("isReimbursed") Boolean isReimbursed,
			@FormParam("trackingNumberFromSynchrotron") String trackingNumberFromSynchrotron,
			@FormParam("trackingNumberToSynchrotron") String trackingNumberToSynchrotron)
			throws Exception {

		long start = this.logInit("saveDewar", logger, token, proposal,
				shippingId, dewarId, code, comments, storageLocation,
				dewarStatus, blTimeStamp, isStorageDewar, barCode, customValue,
				transportValue, trackingNumberFromSynchrotron,
				trackingNumberToSynchrotron);

		try {

			Dewar3VO dewar3vo = new Dewar3VO();
			if (dewarId == null) {
				dewar3vo.setType("Dewar");
				Shipping3VO shipping = this.getShipping3Service().findByPk(shippingId, true);
				dewar3vo.setShippingVO(shipping);
				if (shipping.getSessions() != null){
					if (shipping.getSessions().size() > 0){
						Set<Session3VO> set = shipping.getSessions();
						for (Session3VO session3vo : set) {
							if (session3vo != null) {
								dewar3vo.setSessionVO(session3vo);
								break;
							}
						}
						
					}
				}
				if (Constants.SITE_IS_MAXIV()) {
					ArrayList<String> p = StringUtils.GetProposalNumberAndCode(proposal);
					String proposalCode = p.get(0);
					String proposalNumber = p.get(2);
					ProposalWS3VO proposalws3vo = getProposal3Service().findForWSByCodeAndNumber(proposalCode, proposalNumber);
					Integer proposalId = proposalws3vo.getProposalId();
					List<Dewar3VO> dewar3vos = getDewar3Service().findFiltered(proposalId, shippingId, null,
					code, null, null, null, null, null, null, false, false);
					if (dewar3vos != null && dewar3vos.size() > 0){
						return this.sendError("You cannot have 2 dewars with the same name in the same shipment at MAXIV");
					}
				}
				dewar3vo = getDewar3Service().create(dewar3vo);
			} else {
				dewar3vo = getDewar3Service().findByPk(dewarId, false, false);
			}
			dewar3vo.setComments(comments);
			dewar3vo.setCode(code);
			dewar3vo.setStorageLocation(storageLocation);
			if (customValue != null) {
				if (!customValue.isEmpty()) {
					dewar3vo.setCustomsValue(Integer.parseInt(customValue));
				} else {
					dewar3vo.setCustomsValue(null);
				}
			} else {
				dewar3vo.setCustomsValue(null);
			}
			if (transportValue != null) {
				if (!transportValue.isEmpty()) {
					dewar3vo.setTransportValue(Integer.parseInt(transportValue));
				} else {
					dewar3vo.setTransportValue(null);
				}
			} else {
				dewar3vo.setTransportValue(null);
			}
			if (isReimbursed != null) {			
				dewar3vo.setIsReimbursed(isReimbursed);				
			} else {
				dewar3vo.setIsReimbursed(false);	
			}
			dewar3vo.setTrackingNumberFromSynchrotron(trackingNumberFromSynchrotron);
			dewar3vo.setTrackingNumberToSynchrotron(trackingNumberToSynchrotron);
			//TODO : if sessionId != null set the new session ? possible to update the session ??
			if (dewar3vo.getSessionVO() == null ) {
				dewar3vo.setSessionVO(getSession3Service().findByPk(sessionId, false, false, false));
			}
			getDewar3Service().update(dewar3vo);
			this.logFinish("saveDewar", start, logger);
			
			return new ShippingRestWebService().getShipping(token, proposal, shippingId);
		} catch (Exception e) {
			this.logError("saveDewar", e, start, logger);
		}
		return null;
	}


	@RolesAllowed({"Manager", "Localcontact" })
	@POST
	@Path("{token}/dewar/updateStatus")
	@Produces({ "application/json" })
	public Response updateStatus(
			@PathParam("token") String token,
			@FormParam("location") String location,
			@FormParam("barCode") String barCode,
			@FormParam("username") String username)
			throws Exception {

		long start = this.logInit("updateStatus", logger, token, location,
				barCode, username);

		try {
			Timestamp dateTime = getDateTime();
			// Add dewar event in table
			getDewarAPIService().addDewarLocation(barCode, username, dateTime, location, "", "");

			// Update dewar info (Dewar, Shipping, DewarTransportHistory)
			if (!getDewarAPIService().updateDewar(barCode, location, "", "")) {
				throw new Exception("Cannot update the dewar status");
			}
			this.logFinish("updateStatus", start, logger);

			return this.sendResponse(Status.OK);
		} catch (Exception e) {
			this.logError("updateStatus", e, start, logger);
			return this.sendResponse(Response.Status.NOT_FOUND);
		}
	}


	public byte[] getLabels(int dewarId) throws NamingException, Exception {
		
		Dewar3VO dewar = this.getDewar3Service().findByPk(dewarId, true, true);

		// Retrieve shipment object ------------------------
		Shipping3VO shipping = dewar.getShippingVO();
		shipping = this.getShipping3Service().loadEager(shipping);
		
		// Retrieve dewar object ---------------------------
		//TODO understand why sessionVO is null in dewarVO
		
//		Integer sessionId = null;
		
		Session3VO session = null;
		if (dewar.getSessionVO() != null) {
//			sessionId = dewar.getSessionVO().getSessionId();
			session = dewar.getSessionVO();
		}
		else{
//			if (shipping.getSessions() != null && shipping.getSessions().size() > 0) {
//			Set<Session3VO> set = shipping.getSessions();
//			for (Session3VO session3vo : set) {
//				if (session3vo != null) {
//					dewar.setSessionVO(session3vo);
//					session =session3vo;
//					break;
//				}
//			}
			List<Session3VO> sessions = this.getSession3Service().findByShippingId(shipping.getShippingId());
			if (sessions.size() > 0){
				session = sessions.get(0);
			}
		}	
		if (session == null){
			logger.warn("Session is null for dewar: " + dewarId);
		}
		else{
		    dewar.setSessionVO(session);
		}

		// Retrieve SENDING labcontact object ---------------
		Integer sendingLabContactId = shipping.getSendingLabContactId();
		LabContact3VO sendingLabContact = this.getLabContact3Service().findByPk(sendingLabContactId);

		// Retrieve SENDING person object
		Integer sendingPersonId = sendingLabContact.getPersonVOId();
		Person3VO sendingPerson = this.getPerson3Service().findByPk(sendingPersonId);

		// Retrieve SENDING laboratory object
		Integer sendingLaboratoryId = sendingPerson.getLaboratoryVOId();
		Laboratory3VO sendingLaboratory = this.getLaboratory3Service().findByPk(sendingLaboratoryId);
		// Retrieve RETURN labcontact object ----------------
		Integer returnLabContactId = shipping.getReturnLabContactId();
		LabContact3VO returnLabContact = this.getLabContact3Service().findByPk(returnLabContactId);

		// Retrieve RETURN person object
		Integer returnPersonId = returnLabContact.getPersonVOId();
		Person3VO returnPerson = this.getPerson3Service().findByPk(returnPersonId);

		// Retrieve RETURN laboratory object
		Integer returnLaboratoryId = returnPerson.getLaboratoryVOId();
		Laboratory3VO returnLaboratory = this.getLaboratory3Service().findByPk(returnLaboratoryId);

		// ---------------------------------------------------------------------------------------------------
		// PDF Labels generation
		PDFFormFiller pdfFormFiller = new PDFFormFiller();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				
		//TODO put in properties or find a way to read correctly the pdf
		
		String path= Constants.TEMPLATE_PDF_PARCEL_LABELS_WORLDCOURIER_PYARCH_PATH;
		
		if (returnLabContact.getDefaultCourrierCompany() != null && returnLabContact.getDefaultCourrierCompany().equals(Constants.SHIPPING_DELIVERY_AGENT_NAME_WORLDCOURIER)) {
			path= Constants.TEMPLATE_PDF_PARCEL_LABELS_WORLDCOURIER_PYARCH_PATH;
		} else {
			path= Constants.TEMPLATE_PDF_PARCEL_LABELS_PYARCH_PATH;
		}
		pdfFormFiller.init(path,outputStream);

		// Date format
		SimpleDateFormat dateStandard = new SimpleDateFormat(
				Constants.DATE_FORMAT);

		Proposal3VO proposal = shipping.getProposalVO();

		// Properties to fill in the labels
		Map<String, String> fieldNamesAndValues = new HashMap<String, String>();
		fieldNamesAndValues.put("TF_parcelLabel", dewar.getCode());
		fieldNamesAndValues.put("TF_parcelBarcode", "*" + dewar.getBarCode()
				+ "*");
		fieldNamesAndValues.put("TF_shipmentName", shipping.getShippingName());
		fieldNamesAndValues.put("TF_parcelsNumber",
				Integer.toString(shipping.getDewarVOs().size()));
		fieldNamesAndValues.put("TF_proposalNumber", proposal.getCode() + "-"
				+ proposal.getNumber());

		// Session values (only if they exist)
		if (session != null) {
			fieldNamesAndValues.put("TF_beamline", session.getBeamlineName());
			fieldNamesAndValues.put("TF_experimentDate", dateStandard
					.format(new Date(session.getStartDate().getTime())));
			fieldNamesAndValues.put("TF_localContactName",
					session.getBeamlineOperator());
		} else {
			fieldNamesAndValues.put("TF_beamline", "unknown");
			fieldNamesAndValues.put("TF_experimentDate", "unknown");
			fieldNamesAndValues.put("TF_localContactName", "unknown");
		}

		fieldNamesAndValues.put("TF_sendingLabContactName",
				sendingPerson.getFamilyName().toUpperCase() + " "
						+ sendingPerson.getGivenName());

		// Phone (only if exists)
		String sendingPhone = "unknown";
		if (sendingPerson.getPhoneNumber() != null)
			sendingPhone = sendingPerson.getPhoneNumber();
		fieldNamesAndValues.put("TF_sendingLabContactTel", sendingPhone);

		// Fax (only if exists)
		String sendingFax = "unknown";
		if (sendingPerson.getFaxNumber() != null)
			sendingFax = sendingPerson.getFaxNumber();
		fieldNamesAndValues.put("TF_sendingLabContactFax", sendingFax);

		fieldNamesAndValues.put("TF_sendingLaboratoryName",
				sendingLaboratory.getName());
		fieldNamesAndValues.put("TF_sendingLaboratoryAddress",
				sendingLaboratory.getAddress() + " "  + sendingLaboratory.getCity());

		fieldNamesAndValues.put(
				"TF_returnLabContactName",
				returnPerson.getFamilyName().toUpperCase() + " "
						+ returnPerson.getGivenName());

		// Phone (only if exists)
		String returnPhone = "unknown";
		if (sendingPerson.getPhoneNumber() != null)
			returnPhone = sendingPerson.getPhoneNumber();
		fieldNamesAndValues.put("TF_returnLabContactTel", returnPhone);

		// Fax (only if exists)
		String returnFax = "unknown";
		if (sendingPerson.getFaxNumber() != null)
			returnFax = sendingPerson.getFaxNumber();
		fieldNamesAndValues.put("TF_returnLabContactFax", returnFax);

		fieldNamesAndValues.put("TF_returnLaboratoryName",
				returnLaboratory.getName());
		fieldNamesAndValues.put("TF_returnLaboratoryAddress",
				returnLaboratory.getAddress() + " "  + returnLaboratory.getCity());

		// default courier company (only if exists)
		String defaultCourrierCompany = "unknown";
		
		
		if (returnLabContact.getDefaultCourrierCompany() != null)
			defaultCourrierCompany = returnLabContact
					.getDefaultCourrierCompany();
		
		fieldNamesAndValues.put("TF_returnCourierCompany",
				defaultCourrierCompany);

		fieldNamesAndValues.put("TF_returnCourierAccount",
				returnLabContact.getCourierAccount());
		fieldNamesAndValues.put("TF_returnBillingReference",
				returnLabContact.getBillingReference());
		fieldNamesAndValues.put("TF_returnCustomsValue",
				Integer.toString(returnLabContact.getDewarAvgCustomsValue()));
		fieldNamesAndValues.put("TF_returnTransportValue",
				Integer.toString(returnLabContact.getDewarAvgTransportValue()));
		
		// reimbursed dewars
		if (dewar.getIsReimbursed() != null && dewar.getIsReimbursed().booleanValue() == true) {
			fieldNamesAndValues.put("TF_returnCourierCompany", Constants.SHIPPING_DELIVERY_AGENT_NAME_FEDEX);
			fieldNamesAndValues.put("TF_returnCourierAccount", Constants.SHIPPING_DELIVERY_AGENT_FEDEX_ACCOUNT);
		}

		pdfFormFiller.setFields(fieldNamesAndValues);

		try {
			pdfFormFiller.render();
		} catch (Exception e) {
			System.out.println("Erreur pendant 'render' : " + e.getMessage());
		}

		// Change dewar status if opened
		String dewarStatus = dewar.getDewarStatus();
		System.out.println("dewarStatus= " + dewarStatus);
		if (dewarStatus == null || dewarStatus.equals("")
				|| dewarStatus.equals(Constants.SHIPPING_STATUS_OPENED)) {
			// Update status
			dewar.setDewarStatus(Constants.SHIPPING_STATUS_READY_TO_GO);
			this.getDewar3Service().update(dewar);
			// Refresh dewarFullFacade
			// old DewarFullFacadeLocal dewarFullFacade =
			// DewarFullFacadeUtil.getLocalHome().create();
			// old DewarFullValue dewarFull =
			// dewarFullFacade.findByPrimaryKey(dewarId);
			Dewar3VO dewarFull = this.getDewar3Service().findByPk(dewarId, false,
					false);
			dewarFull.setDewarStatus(Constants.SHIPPING_STATUS_READY_TO_GO);
		}

		// Add event to history
		Timestamp dateTime = getDateTime();
		// old DewarTransportHistoryFacadeLocal _dewarTransportHistoryFacade =
		// DewarTransportHistoryFacadeUtil.getLocalHome().create();
		// old DewarTransportHistoryLightValue newHistory = new
		// DewarTransportHistoryLightValue();
		DewarTransportHistory3VO newHistory = new DewarTransportHistory3VO();
		newHistory.setDewarStatus(Constants.SHIPPING_STATUS_READY_TO_GO);
		newHistory.setStorageLocation("");
		newHistory.setArrivalDate(dateTime);
		// old newHistory.setDewarId(dewarId);
		newHistory.setDewarVO(dewar);
		// old _dewarTransportHistoryFacade.create(newHistory);
		this.getDewarTransportHistory3Service().create(newHistory);

		// Utilisation du stream PDF dans l'action struts
		byte[] pdfContent = outputStream.toByteArray();
		return pdfContent;
		// MISServletUtils.sendToBrowser(in_response/* HttpServletResponse */,
		// new ByteArrayInputStream(pdfContent),
		// new Integer(pdfContent.length), "application/pdf", dewar.getCode() +
		// ".pdf"/* fileName */, // !!!
		// // =>
		// // Parcel
		// // name
		// // !
		// false/* inLine */, true/* forceAttachment */);

//		return null;
	}

	

}
