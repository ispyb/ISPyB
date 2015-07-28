package ispyb.ws.rest.proposal;

import ispyb.common.util.Constants;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Laboratory3VO;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.DewarTransportHistory3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.ws.rest.RestWebService;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.PermitAll;
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

    private final static Logger LOGGER = Logger.getLogger(ShippingRestWebService.class);

    @PermitAll
    @GET
    @Path("{cookie}/proposal/{proposal}/shipping/list")
    @Produces({ "application/json" })
    public Response listShipping(@PathParam("cookie") String cookie, @PathParam("proposal") String proposal)
	    throws Exception {
	return sendResponse(this.getShipping3Service().findByProposal(this.getProposalId(proposal), true));
    }

    @PermitAll
    @GET
    @Path("{cookie}/proposal/{proposal}/shipping/{shippindId}/get")
    @Produces({ "application/json" })
    public Response getShipping(@PathParam("cookie") String cookie, @PathParam("proposal") String proposal,
	    @PathParam("shippindId") Integer shippindId) throws Exception {
	return sendResponse(this.getShipping3Service().findByPk(shippindId, true));
    }

    

    @PermitAll
    @POST
    @Path("{cookie}/proposal/{proposal}/shipping/save")
    @Produces({ "application/json" })
    public Response saveShipping(@PathParam("cookie") String cookie, @PathParam("proposal") String proposal,
	    @FormParam("shippingId") String shippingId, @FormParam("name") String name,
	    @FormParam("comments") String comments, @FormParam("billingReference") String billingReference,
	    @FormParam("courierAccount") String courierAccount,
	    @FormParam("dewarAvgCustomsValue") String dewarAvgCustomsValue,
	    @FormParam("dewarAvgTransportValue") String dewarAvgTransportValue,
	    @FormParam("returnCourier") String returnCourier,
	    @FormParam("sendingLabContactId") int sendingLabContactId,
	    @FormParam("returnLabContactId") int returnLabContactId) throws Exception {
	Shipping3VO shipping3VO = new Shipping3VO();

	if ((!shippingId.equals("null"))) {
	    shipping3VO = this.getShipping3Service().findByPk(Integer.parseInt(shippingId), true);
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

	if (!shippingId.equals("null")) {
	    getShipping3Service().update(shipping3VO);
	} else {
	    shipping3VO = getShipping3Service().create(shipping3VO);
	}
	return this.sendResponse(getShipping3Service().findByPk(shipping3VO.getShippingId(), true));
    }

  

}
