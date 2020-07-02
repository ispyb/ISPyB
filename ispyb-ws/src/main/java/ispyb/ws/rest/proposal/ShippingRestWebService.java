package ispyb.ws.rest.proposal;

import generated.ws.smis.ProposalParticipantInfoLightVO;
import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.smis.ScientistsFromSMIS;
import ispyb.ws.rest.mx.MXRestWebService;

import java.lang.reflect.Type;
import java.util.*;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.reflect.TypeToken;

@Path("/")
public class ShippingRestWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(ShippingRestWebService.class);
	

	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/shipping/labcontact/smis/list")
	@Produces({ "application/json" })
	public Response getSMISLabContact(@PathParam("token") String token, @PathParam("proposal") String proposal) {
		String methodName = "getSMISLabContact";
		long id = this.logInit(methodName, logger, token, proposal);
		try {

			int proposalId = this.getProposalId(proposal);
			Proposal3VO proposal3VO = this.getProposal3Service().findByPk(proposalId);
			ProposalParticipantInfoLightVO[] scientists = ScientistsFromSMIS.findScientistsForProposalByNameAndFirstName(StringUtils.getUoCode(proposal3VO.getCode()), Long.parseLong(proposal3VO.getNumber()), null, null);
			this.logFinish(methodName, id, logger);
			return sendResponse(scientists);
		} catch (Exception e) {
			e.printStackTrace();
			return this.logError(methodName, e, id, logger);
		}
	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/shipping/labcontact/list")
	@Produces({ "application/json" })
	public Response getLabContact(@PathParam("token") String token, @PathParam("proposal") String proposal) {
		String methodName = "getLabContact";
		long id = this.logInit(methodName, logger, token, proposal);
		try {

			int proposalId = this.getProposalId(proposal);
			List<LabContact3VO> labcontacts = this.getLabContact3Service().findByProposalId(proposalId);
			this.logFinish(methodName, id, logger);
			return sendResponse(labcontacts);
		} catch (Exception e) {
			e.printStackTrace();
			return this.logError(methodName, e, id, logger);
		}
	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/shipping/labcontact/{labcontactId}/get")
	@Produces({ "application/json" })
	public Response getLabContactByPk(@PathParam("token") String token, @PathParam("proposal") String proposal, @PathParam("labcontactId") int labcontactId) {
		String methodName = "getLabContactByPk";
		long id = this.logInit(methodName, logger, token, proposal);
		try {
			LabContact3VO labcontacts = this.getLabContact3Service().findByPk(labcontactId);
			this.logFinish(methodName, id, logger);
			return sendResponse(labcontacts);
		} catch (Exception e) {
			e.printStackTrace();
			return this.logError(methodName, e, id, logger);
		}
	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@POST
	@Path("{token}/proposal/{proposal}/shipping/labcontact/save")
	@Produces({ "application/json" })
	public Response saveLabContact(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal, 
			@FormParam("labcontact") String labContactJson) {
		String methodName = "saveLabContact";
		long id = this.logInit(methodName, logger, token, proposal, labContactJson);
		try {
			LabContact3VO labContact = this.getGson().fromJson(labContactJson, LabContact3VO.class);
			/** Update Person **/
			
			Person3VO person = this.getPerson3Service().findByPk(labContact.getPersonVO().getPersonId());
			person.setEmailAddress(labContact.getPersonVO().getEmailAddress());			
			person.setFamilyName(labContact.getPersonVO().getFamilyName());
			person.setFaxNumber(labContact.getPersonVO().getFaxNumber());
			person.setGivenName(labContact.getPersonVO().getGivenName());
			person.setPhoneNumber(labContact.getPersonVO().getPhoneNumber());
			person.setTitle(labContact.getPersonVO().getTitle());
			person = this.getPerson3Service().merge(person);
			
			person.getLaboratoryVO().setAddress(labContact.getPersonVO().getLaboratoryVO().getAddress());
			person.getLaboratoryVO().setName(labContact.getPersonVO().getLaboratoryVO().getName());
			this.getLaboratory3Service().merge(person.getLaboratoryVO());
			
			labContact.setPersonVO(person);
			/** Update LabContact **/
			labContact.setProposalVO(this.getProposal3Service().findByPk(this.getProposalId(proposal)));
			
			
			
			labContact = this.getLabContact3Service().update(labContact);
			this.logFinish(methodName, id, logger);
			return sendResponse(labContact);
		} catch (Exception e) {
			e.printStackTrace();
			return this.logError(methodName, e, id, logger);
		}
	}
	
	
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/shipping/list")
	@Produces({ "application/json" })
	public Response listShipping(@PathParam("token") String token, @PathParam("proposal") String proposal) {
		long id = this.logInit("listShipping", logger, token, proposal);
		try {
			// TODO: This query has to be replaced by a view!!
			List<Map<String, Object>> result = this.getShipping3Service().getShippingByProposalId(this.getProposalId(proposal));
			this.logFinish("listShipping", id, logger);
			return sendResponse(result);
		} catch (Exception e) {
			return this.logError("listShipping", e, id, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/shipping/{shippingId}/get")
	@Produces({ "application/json" })
	public Response getShipping(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("shippingId") Integer shippingId) throws Exception {

		long id = this.logInit("getShipping", logger, token, proposal, shippingId);
		try {
			//Shipping3VO result = this.getShipping3Service().findByPk(shippingId, true, true, true);
			Shipping3VO result = this.getShipping3Service().findByPk(shippingId, true, true, true, true);
			this.logFinish("getShipping", id, logger);
			return sendResponse(result);
		} catch (Exception e) {
			return this.logError("getShipping", e, id, logger);
		}

	}

	/*@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/shipping/find/")
	@Produces({ "application/json" })
	public Response getShippingByProposalAndName(@PathParam("token") String token, @PathParam("proposal") String proposal,
								@FormParam("name") String name, ) throws Exception {

		long id = this.logInit("getShippingByProposalAndName", logger, token, proposal, name);
		try {
			List<Shipping3VO> result = this.getShipping3Service().findFiltered(null, name, proposal, null, null, null, null, null, false);
			this.logFinish("getShippingByProposalAndName", id, logger);
			return sendResponse(result);
		} catch (Exception e) {
			return this.logError("getShipping", e, id, logger);
		}

	}*/

	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/shipping/{shippingId}/status/{status}/update")
	@Produces({ "application/json" })
	public Response setShippingStatus(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("shippingId") Integer shippingId,
			@PathParam("status") String status) throws Exception {

		long id = this.logInit("setShippingStatus", logger, token, proposal, shippingId);
		try {
			Shipping3VO result = this.getShipping3Service().findByPk(shippingId, true,true, false);
			logger.info("Updating shipping status " + result.getShippingId() + " from " + result.getShippingStatus() + " to " + status);
			result.setShippingStatus(status);
			this.getShipping3Service().update(result);
			/**
			 * Issue 69 status of shipment also should update dewars status
			 * https://github.com/ispyb/ISPyB/issues/69
			 **/
			for (Dewar3VO dewar : result.getDewars()) {
				logger.info("\t Updating dewar status " + dewar.getDewarId() + " from " + dewar.getDewarStatus() + " to " + status);
				dewar.setDewarStatus(status);
				
				this.getDewar3Service().update(dewar);
				for (Container3VO container : dewar.getContainerVOs()) {
					logger.info("\t\tUpdating container status " + container.getContainerId() + " from " + container.getContainerStatus() + " to " + status);
					container.setContainerStatus(status);
					this.getContainer3Service().update(container);
				}
			}
			
			
			
			this.logFinish("setShippingStatus", id, logger);
			HashMap<String, String> response = new HashMap<String, String>();
			response.put("setShippingStatus", "ok");
			return sendResponse(response);
		} catch (Exception e) {
			return this.logError("setShippingStatus", e, id, logger);
		}

	}
	
	
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/shipping/{shippingId}/dewar/{dewarId}/puck/add")
	@Produces({ "application/json" })
	public Response addPuck(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("shippingId") Integer shippingId, @PathParam("dewarId") Integer dewarId) throws Exception {

		long id = this.logInit("addPuck", logger, token, proposal, shippingId);
		try {
			Container3VO container = new Container3VO();
			container.setDewarVO(this.getDewar3Service().findByPk(dewarId, false, false));
			container.setContainerType("Puck");
			container.setCapacity(Constants.BASKET_SAMPLE_CAPACITY);
			container.setTimeStamp(StringUtils.getCurrentTimeStamp());
			container = this.getContainer3Service().create(container);
			this.logFinish("addPuck", id, logger);
			return sendResponse(container);
		} catch (Exception e) {
			return this.logError("addPuck", e, id, logger);
		}

	}	
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/shipping/{shippingId}/dewar/{dewarId}/containerType/{containerType}/capacity/{capacity}/container/add")
	@Produces({ "application/json" })
	public Response addContainer(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("shippingId") Integer shippingId, @PathParam("dewarId") Integer dewarId, @PathParam("containerType") String type, @PathParam("capacity") Integer capacity) throws Exception {

		long id = this.logInit("addPuck", logger, token, proposal, shippingId, type, capacity);
		try {
			Container3VO container = new Container3VO();
			container.setDewarVO(this.getDewar3Service().findByPk(dewarId, false, false));
			container.setContainerType(type);
			container.setCapacity(capacity);			
			
			container.setTimeStamp(StringUtils.getCurrentTimeStamp());
			container = this.getContainer3Service().create(container);
			this.logFinish("addContainer", id, logger);
			return sendResponse(container);
		} catch (Exception e) {
			return this.logError("addContainer", e, id, logger);
		}

	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/shipping/{shippingId}/dewar/{dewarId}/puck/{containerId}/get")
	@Produces({ "application/json" })
	public Response getPuckById(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("shippingId") Integer shippingId, @PathParam("dewarId") Integer dewarId, @PathParam("containerId") Integer containerId) throws Exception {

		long id = this.logInit("getPuckById", logger, token, proposal, shippingId);
		try {
			Container3VO container = this.getContainer3Service().findByPk(containerId, true);
			this.logFinish("getPuckById", id, logger);
			return sendResponse(container);
		} catch (Exception e) {
			return this.logError("getPuckById", e, id, logger);
		}
	}
	
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/shipping/{shippingId}/history")
	@Produces({ "application/json" })
	public Response getShipmentHistory(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("shippingId") Integer shippingId) throws Exception {
		long id = this.logInit("getShipmentHistory", logger, token, proposal, shippingId);
		try {
			
			List<Map<String, Object>> result = this.getShipmentWsService().getShipmentHistoryByShipmentId(this.getProposalId(proposal), shippingId);
			this.logFinish("getShipmentHistory", id, logger);
			return sendResponse(result);
		} catch (Exception e) {
			return this.logError("getShipmentHistory", e, id, logger);
		}
	}
	
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@POST
	@Path("{token}/proposal/{proposal}/shipping/{shippingId}/dewar/{dewarId}/puck/{containerId}/save")
	@Produces({ "application/json" })
	public Response savePuck(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("shippingId") Integer shippingId,
			@PathParam("dewarId") Integer dewarId,
			@PathParam("containerId") Integer containerId,
			@FormParam("puck") String puck) throws Exception {

		long id = this.logInit("savePuck", logger, token, proposal, shippingId, containerId,puck);
		try {
			this.getContainer3Service().saveContainer(this.getGson().fromJson(puck, Container3VO.class), this.getProposalId(proposal));
			this.logFinish("savePuck", id, logger);
			return sendResponse(this.getContainer3Service().findByPk(containerId, true));
		} catch (Exception e) {
			return this.logError("savePuck", e, id, logger);
		}
	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@POST
	@Path("/{token}/proposal/{proposal}/shipping/{shippingId}/dewars/add")
	@Produces({ "application/json" })
	public Response addDewarToShipment(
			@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("shippingId") Integer shippingId,
			@FormParam("dewars") String dewars) throws Exception {

		try {
			Type listType = new TypeToken<List<Dewar3VO>>(){}.getType();
			List<Dewar3VO> dewars3vo = this.getGson().fromJson(dewars, listType);
			logger.info(dewars3vo);
			for (Dewar3VO dewar3vo : dewars3vo) {
				logger.info(String.format("\t %s %s", dewar3vo.getCode(), dewar3vo.getContainerVOs().size()));
				for (Container3VO container : dewar3vo.getContainerVOs()) {
					logger.info(String.format("\t\t %s %s", container.getCode(), container.getSampleVOs().size()));
					for (BLSample3VO sample : container.getSampleVOs()) {
						logger.info(String.format("\t\t\t %s %s", sample.getName(),sample.getCrystalVO().getCellA(), sample.getCrystalVO().getCellB()));
					}
				}
			}
			logger.info("Storing dewars");
			this.getContainer3Service().saveDewarList(dewars3vo, this.getProposalId(proposal), shippingId);
		} catch (Exception e) {
			e.printStackTrace();
			return this.sendError(e.getMessage());
			
		}
		return this.sendOk();
	}
	
	
	
	
	

	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/shipping/{shippingId}/dewar/{dewarId}/puck/{containerId}/remove")
	@Produces({ "application/json" })
	public Response removePuck(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("shippingId") Integer shippingId,
			@PathParam("dewarId") Integer dewarId,
			@PathParam("containerId") Integer containerId) throws Exception {

		long id = this.logInit("removePuck", logger, token, proposal, shippingId, containerId);
		try {
			Container3VO container = this.getContainer3Service().findByPk(containerId, true);
			this.getContainer3Service().delete(container);
			this.logFinish("removePuck", id, logger);
			return sendResponse(this.getShipping3Service().getShippingById(shippingId));
		} catch (Exception e) {
			return this.logError("removePuck", e, id, logger);
		}

	}

	@RolesAllowed({"Manager"})
	@GET
	@Path("{token}/proposal/{proposal}/shipping/{shippingId}/remove")
	@Produces({ "application/json" })
	public Response removeShipment(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("shippingId") Integer shippingId) throws Exception {

		long id = this.logInit("removeShipment", logger, token, proposal, shippingId);
		try {
			this.getShipping3Service().deleteAllSamplesAndContainersForShipping(shippingId);
			this.getShipping3Service().deleteByPk(shippingId);
			this.logFinish("removeShipment", id, logger);
			return sendResponse(true);
			//return sendResponse(this.getShipping3Service().getShippingById(shippingId));
		} catch (Exception e) {
			return this.logError("removeShipment", e, id, logger);
		}

	}


	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/shipping/{shippingId}/datacollecitons/list")
	@Produces({ "application/json" })
	public Response getDatacollectionsByShippingId(@PathParam("token") String token, @PathParam("proposal") String proposal,
							   @PathParam("shippingId") Integer shippingId) throws Exception {

		long id = this.logInit("getDatacollectionsByShippingId", logger, token, proposal, shippingId);
		try {
			List<DataCollection3VO> dcs = this.getDataCollection3Service().findByShippingId(shippingId);
			this.logFinish("getDatacollectionsByShippingId", id, logger);
			return sendResponse(dcs);
		} catch (Exception e) {
			return this.logError("getDatacollectionsByShippingId", e, id, logger);
		}

	}

	
	
	/** 
	 * 
	 * @param token
	 * @param proposal
	 * @param shippingId
	 * @param name
	 * @param comments
	 * @param billingReference
	 * @param courierAccount
	 * @param dewarAvgCustomsValue
	 * @param dewarAvgTransportValue
	 * @param returnCourier
	 * @param sendingLabContactId
	 * @param returnLabContactId value = 0 if there is not any return requested
	 * 							 value = -1 if receiver and sender are the same person
	 * @return
	 */
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@POST
	@Path("{token}/proposal/{proposal}/shipping/save")
	@Produces({ "application/json" })
	
	public Response saveShipping(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@FormParam("shippingId") String shippingId, @FormParam("name") String name,
			@FormParam("comments") String comments, 
			@FormParam("billingReference") String billingReference,
			@FormParam("courierAccount") String courierAccount,
			@FormParam("dewarAvgCustomsValue") String dewarAvgCustomsValue,
			@FormParam("dewarAvgTransportValue") String dewarAvgTransportValue,
			@FormParam("returnCourier") String returnCourier,
			@FormParam("sendingLabContactId") int sendingLabContactId,
			@FormParam("returnLabContactId") int returnLabContactId,
			@FormParam("sessionId") String sessionId) {

		long id = this.logInit("saveShipping", logger, token, proposal, shippingId, name, comments, billingReference, courierAccount,  dewarAvgCustomsValue, dewarAvgTransportValue, returnCourier, sendingLabContactId, returnLabContactId, sessionId );
		try {

			Shipping3VO shipping3VO = new Shipping3VO();

			if ((shippingId != null) && (!"".equals(shippingId))) {
				try{
					shipping3VO = this.getShipping3Service().findByPk(Integer.parseInt(shippingId), true);
				}
				catch(Exception e){
					System.out.println("shipping Id is not a number");
				}
			}

			if (Constants.SITE_IS_MAXIV()) {
				try {
					String proposalCode = proposal.substring(0, 2);
					String proposalNumber = proposal.substring(proposal.length() - 8);
					List<Shipping3VO> results = this.getShipping3Service().findFiltered(null, name, proposalCode, proposalNumber, null, null, null, null, false);
					if (results != null && !results.isEmpty()) {
						if ((shippingId != null) && (!"".equals(shippingId))) {
							for (int i=0;i<results.size();i++) {
								Shipping3VO ship = results.get(i);
								if (!shippingId.equals(ship.getShippingId().toString()) && ship.getShippingName().equals(name)) {
									this.logFinish("saveShipping", id, logger);
									return this.sendError("There is another shipment with the same name for this proposal");
								}
							}
						} else {
							this.logFinish("saveShipping", id, logger);
							return this.sendError("There is another shipment with the same name for this proposal");
						}
					}
				} catch (Exception e) {
					return this.logError("saveShipping", e, id, logger);
				}
			}

			shipping3VO.setShippingName(name);
			shipping3VO.setShippingStatus(Constants.SHIPPING_STATUS_OPENED);
			shipping3VO.setComments(comments);
			shipping3VO.setProposalVO(getProposal3Service().findByPk(this.getProposalId(proposal)));
			shipping3VO.setCreationDate(new Date());
			shipping3VO.setTimeStamp(new Date());
			shipping3VO.setShippingType(Constants.DEWAR_TRACKING_SHIPPING_TYPE);

			/** Lab contacts **/
			LabContact3VO sendingLabContact = this.getLabContact3Service().findByPk(sendingLabContactId);
			shipping3VO.setSendingLabContactVO(sendingLabContact);

			if ((returnLabContactId != 0)&&(returnLabContactId != -1)){
				LabContact3VO returnLabContact = this.getLabContact3Service().findByPk(returnLabContactId);
				returnLabContact.setBillingReference(billingReference);
				returnLabContact.setCourierAccount(courierAccount);
				returnLabContact.setDewarAvgCustomsValue(Integer.parseInt(dewarAvgCustomsValue));
				returnLabContact.setDewarAvgTransportValue(Integer.parseInt(dewarAvgTransportValue));
				returnLabContact.setDefaultCourrierCompany(returnCourier);
				getLabContact3Service().update(returnLabContact);
				shipping3VO.setReturnLabContactVO(returnLabContact);
			}
			
			/** No return requested **/
			if (returnLabContactId == 0){
				shipping3VO.setReturnLabContactVO(null);
			}
			
			/** Same person **/
			if (returnLabContactId == -1){
				sendingLabContact.setBillingReference(billingReference);
				sendingLabContact.setCourierAccount(courierAccount);
				sendingLabContact.setDewarAvgCustomsValue(Integer.parseInt(dewarAvgCustomsValue));
				sendingLabContact.setDewarAvgTransportValue(Integer.parseInt(dewarAvgTransportValue));
				sendingLabContact.setDefaultCourrierCompany(returnCourier);
				getLabContact3Service().update(sendingLabContact);
				shipping3VO.setReturnLabContactVO(sendingLabContact);
			}
			
			/** Sessions **/
			System.out.println(shipping3VO.getSessions());
			if (shipping3VO.getSessions() != null){
				shipping3VO.getSessions().clear();
			}
			
			/** Is session a number **/
			String regex = "\\d+";
			if (sessionId.matches(regex)){
				/** It is a number **/
				//shipping3VO.getSessions().add(this.getSession3Service().findByPk(Integer.valueOf(sessionId), false, false, false));	
				shipping3VO.addSession(this.getSession3Service().findByPk(Integer.valueOf(sessionId), false, false, false));
			}

			if (shipping3VO.getShippingId() != null) {
				getShipping3Service().update(shipping3VO);
			} else {
				shipping3VO = getShipping3Service().create(shipping3VO);
			}
			this.logFinish("saveShipping", id, logger);
			return this.getShipping(token, proposal, shipping3VO.getShippingId());
		} catch (Exception e) {
			return this.logError("saveShipping", e, id, logger);
		}
	}

}
