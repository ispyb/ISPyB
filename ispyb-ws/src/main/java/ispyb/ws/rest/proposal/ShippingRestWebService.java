package ispyb.ws.rest.proposal;

import generated.ws.smis.ProposalParticipantInfoLightVO;
import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.smis.ScientistsFromSMIS;
import ispyb.ws.rest.RestWebService;

import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/")
public class ShippingRestWebService extends RestWebService {

	private final static Logger logger = Logger.getLogger(ShippingRestWebService.class);

	
	

	@RolesAllowed({"User", "Manager", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/shipping/labcontact/smis/list")
	@Produces({ "application/json" })
	public Response getSMISLabContact(@PathParam("token") String token, @PathParam("proposal") String proposal) {
		String methodName = "getSMISLabContact";
		long id = this.logInit(methodName, logger, token, proposal);
		try {

			int proposalId = this.getProposalId(proposal);
			Proposal3VO proposal3VO = this.getProposal3Service().findByPk(proposalId);
			ProposalParticipantInfoLightVO[] scientists = ScientistsFromSMIS.findScientistsForProposalByNameAndFirstName(StringUtils.getUoCode(proposal3VO.getCode()), Integer.parseInt(proposal3VO.getNumber()), null, null);
			this.logFinish(methodName, id, logger);
			return sendResponse(scientists);
		} catch (Exception e) {
			e.printStackTrace();
			return this.logError(methodName, e, id, logger);
		}
	}
	
	@RolesAllowed({"User", "Manager", "LocalContact"})
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
	
	
	@RolesAllowed({"User", "Manager", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/shipping/list")
	@Produces({ "application/json" })
	public Response listShipping(@PathParam("token") String token, @PathParam("proposal") String proposal) {
		long id = this.logInit("listShipping", logger, token, proposal);
		try {
			List<Shipping3VO> result = this.getShipping3Service().findByProposal(this.getProposalId(proposal), true);
			this.logFinish("listShipping", id, logger);
			return sendResponse(result);
		} catch (Exception e) {
			return this.logError("listShipping", e, id, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/shipping/{shippingId}/get")
	@Produces({ "application/json" })
	public Response getShipping(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("shippingId") Integer shippingId) throws Exception {

		long id = this.logInit("listShipping", logger, token, proposal, shippingId);
		try {
			Shipping3VO result = this.getShipping3Service().findByPk(shippingId, true);
			this.logFinish("getShipping", id, logger);
			return sendResponse(result);
		} catch (Exception e) {
			return this.logError("getShipping", e, id, logger);
		}

	}

	@RolesAllowed({"User", "Manager", "LocalContact"})
	@POST
	@Path("{token}/proposal/{proposal}/shipping/save")
	@Produces({ "application/json" })
	public Response saveShipping(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@FormParam("shippingId") String shippingId, @FormParam("name") String name,
			@FormParam("comments") String comments, @FormParam("billingReference") String billingReference,
			@FormParam("courierAccount") String courierAccount,
			@FormParam("dewarAvgCustomsValue") String dewarAvgCustomsValue,
			@FormParam("dewarAvgTransportValue") String dewarAvgTransportValue,
			@FormParam("returnCourier") String returnCourier,
			@FormParam("sendingLabContactId") int sendingLabContactId,
			@FormParam("returnLabContactId") int returnLabContactId) {

		long id = this.logInit("saveShipping", logger, token, proposal, shippingId, name, comments, billingReference, courierAccount,  dewarAvgCustomsValue, dewarAvgTransportValue, returnCourier, sendingLabContactId, returnLabContactId );
		try {

			Shipping3VO shipping3VO = new Shipping3VO();

			System.out.println("ShippingId " + shippingId);
			if ((shippingId != null) && (shippingId != "")) {
				try{
				shipping3VO = this.getShipping3Service().findByPk(Integer.parseInt(shippingId), true);
				}
				catch(Exception e){
					System.out.println("shipping Id is not a number");
				}
			}

			shipping3VO.setShippingName(name);
			shipping3VO.setShippingStatus(Constants.SHIPPING_STATUS_OPENED);
			shipping3VO.setComments(comments);
			shipping3VO.setProposalVO(getProposal3Service().findByPk(this.getProposalId(proposal)));
			shipping3VO.setCreationDate(new Date());
			shipping3VO.setTimeStamp(new Date());
			shipping3VO.setShippingType(Constants.DEWAR_TRACKING_SHIPPING_TYPE);
			// shipping3VO.setReturnCourier(returnCourier);

			/** Lab contacts **/
			LabContact3VO sendingLabContact = this.getLabContact3Service().findByPk(sendingLabContactId);
			shipping3VO.setSendingLabContactVO(sendingLabContact);

			LabContact3VO returnLabContact = this.getLabContact3Service().findByPk(returnLabContactId);
			returnLabContact.setBillingReference(billingReference);
			returnLabContact.setCourierAccount(courierAccount);
			returnLabContact.setDewarAvgCustomsValue(Integer.parseInt(dewarAvgCustomsValue));
			returnLabContact.setDewarAvgTransportValue(Integer.parseInt(dewarAvgTransportValue));

			returnLabContact.setDefaultCourrierCompany(returnCourier);
			getLabContact3Service().update(returnLabContact);

			shipping3VO.setReturnLabContactVO(returnLabContact);

			if (shipping3VO.getShippingId() != null) {
				getShipping3Service().update(shipping3VO);
			} else {
				shipping3VO = getShipping3Service().create(shipping3VO);
			}
			
			Shipping3VO result = getShipping3Service().findByPk(shipping3VO.getShippingId(), true);
			this.logFinish("saveShipping", id, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError("saveShipping", e, id, logger);
		}
	}

}
