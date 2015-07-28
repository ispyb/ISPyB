package ispyb.ws.rest.proposal;

import ispyb.common.util.Constants;
import ispyb.server.biosaxs.vos.dataAcquisition.StockSolution3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Sampleplate3VO;
import ispyb.server.common.util.LoggerFormatter;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.ws.rest.RestWebService;

import java.util.HashMap;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.naming.NamingException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

@Path("/")
public class DewarRestWebService extends RestWebService {
    private final static Logger LOGGER = Logger.getLogger(DewarRestWebService.class);

    @PermitAll
    @GET
    @Path("{cookie}/proposal/{proposal}/shipping/{shippingId}/dewar/{dewarId}/label")
    @Produces({ "application/json" })
    public Response addDewar(@PathParam("cookie") String cookie, @PathParam("proposal") String proposal,
	    @PathParam("shippingId") int shippingId,
	    @PathParam("dewarId") int dewarId) throws NamingException {
		return Response.serverError().build();
    }

//    @PermitAll
//    @GET
//    @Path("{cookie}/proposal/{proposal}/shipping/{shippingId}/dewar/add")
//    @Produces({ "application/json" })
//    public Response addDewar(
//	    @PathParam("cookie") String cookie, 
//	    @PathParam("proposal") String proposal,
//	    @PathParam("shippingId") int shippingId) throws NamingException {
//
//	HashMap<String, String> params = new HashMap<String, String>();
//	params.put("cookie", String.valueOf(cookie));
//	params.put("proposal", String.valueOf(proposal));
//	params.put("shippingId", String.valueOf(shippingId));
//	long start = this.logInit("addDewar", new Gson().toJson(params), LOGGER);
//	try {
//	    Shipping3VO shipping = getShipping3Service().findByPk(shippingId, false);
//	    Dewar3VO dewar3VO = new Dewar3VO();
//	    dewar3VO.setShippingVO(shipping);
//	    dewar3VO.setType("Dewar");
//	    dewar3VO.setDewarStatus(Constants.SHIPPING_STATUS_OPENED);
//	    Dewar3VO dewar = getDewar3Service().create(dewar3VO);
//	    dewar.setBarCode(dewar.getDewarId().toString());
//	    getDewar3Service().update(dewar);
//	    return this.sendResponse(getShipping3Service().findByPk(shippingId, true));
//	} catch (Exception e) {
//	    e.printStackTrace();
//	    LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "addDewar", start,
//		    System.currentTimeMillis(), e.getMessage(), e);
//
//	}
//	return null;
//    }

    @PermitAll
    @GET
    @Path("{cookie}/proposal/{proposal}/shipping/{shippingId}/dewar/{dewarId}/remove")
    @Produces({ "application/json" })
    public Response removeDewar(
	    @PathParam("cookie") String cookie, 
	    @PathParam("proposal") String proposal,
	    @PathParam("shippingId") int shippingId, 
	    @PathParam("dewarId") int dewarId) throws NamingException {

	HashMap<String, String> params = new HashMap<String, String>();
	params.put("cookie", String.valueOf(cookie));
	params.put("proposal", String.valueOf(proposal));
	params.put("shippingId", String.valueOf(shippingId));
	params.put("dewarId", String.valueOf(dewarId));

	long start = this.logInit("removeDewar", new Gson().toJson(params), LOGGER);

	try {
	    List<Sampleplate3VO> sampleplate3VOs = getSamplePlate3Service().getSamplePlatesByBoxId(
		    String.valueOf(dewarId));
	    for (Sampleplate3VO plate : sampleplate3VOs) {
		if (plate.getBoxId() != null) {
		    if (plate.getBoxId() == dewarId) {
			plate.setBoxId(null);
			getSamplePlate3Service().merge(plate);
		    }
		}
	    }
	    List<StockSolution3VO> stockSolution3VOs = getSaxsProposal3Service().findStockSolutionsByBoxId(
		    String.valueOf(dewarId));
	    for (StockSolution3VO stockSolution3VO : stockSolution3VOs) {
		if (stockSolution3VO.getBoxId() != null) {
		    if (stockSolution3VO.getBoxId() == dewarId) {
			stockSolution3VO.setBoxId(null);
			getSaxsProposal3Service().merge(stockSolution3VO);
		    }
		}
	    }
	    this.getDewar3Service().deleteByPk(dewarId);
	    this.logFinish("removeDewar", start, LOGGER);
	    return this.sendResponse(getShipping3Service().findByPk(shippingId, true));
	} catch (Exception e) {
	    e.printStackTrace();
	    LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "removeDewar", start,
		    System.currentTimeMillis(), e.getMessage(), e);

	}
	return null;
    }

    @PermitAll
    @POST
    @Path("{cookie}/proposal/{proposal}/shipping/{shippingId}/dewar/save")
    @Produces({ "application/json" })
    public Response saveDewar(@PathParam("cookie") String cookie, @PathParam("proposal") String proposal,
	    @PathParam("shippingId") int shippingId, @FormParam("sessionId") Integer sessionId,
	    @FormParam("dewarId") Integer dewarId, @FormParam("code") String code,
	    @FormParam("comments") String comments, @FormParam("storageLocation") String storageLocation,
	    @FormParam("dewarStatus") String dewarStatus, @FormParam("blTimeStamp") String blTimeStamp,
	    @FormParam("isStorageDewar") String isStorageDewar, @FormParam("barCode") String barCode,
	    @FormParam("customValue") String customValue, @FormParam("transportValue") String transportValue,
	    @FormParam("trackingNumberFromSynchrotron") String trackingNumberFromSynchrotron,
	    @FormParam("trackingNumberToSynchrotron") String trackingNumberToSynchrotron) throws Exception {

	HashMap<String, String> params = new HashMap<String, String>();
	params.put("cookie", String.valueOf(cookie));
	params.put("proposal", String.valueOf(proposal));
	params.put("shippingId", String.valueOf(shippingId));
	params.put("sessionId", String.valueOf(sessionId));
	params.put("dewarId", String.valueOf(dewarId));
	params.put("code", String.valueOf(code));
	params.put("comments", String.valueOf(comments));
	params.put("storageLocation", String.valueOf(storageLocation));
	params.put("dewarStatus", String.valueOf(dewarStatus));
	params.put("blTimeStamp", String.valueOf(blTimeStamp));
	params.put("isStorageDewar", String.valueOf(isStorageDewar));
	params.put("barCode", String.valueOf(barCode));
	params.put("customValue", String.valueOf(customValue));
	params.put("transportValue", String.valueOf(transportValue));
	params.put("trackingNumberToSynchrotron", String.valueOf(trackingNumberToSynchrotron));
	params.put("trackingNumberFromSynchrotron", String.valueOf(trackingNumberFromSynchrotron));
	long start = this.logInit("saveDewar", new Gson().toJson(params), LOGGER);
	try {

	    Dewar3VO dewar3vo = new Dewar3VO();
	    if (dewarId == null) {
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
	    dewar3vo.setTrackingNumberFromSynchrotron(trackingNumberFromSynchrotron);
	    dewar3vo.setTrackingNumberToSynchrotron(trackingNumberToSynchrotron);
	    dewar3vo.setSessionVO(getSession3Service().findByPk(sessionId, false, false, false));
	    getDewar3Service().update(dewar3vo);
	    this.logFinish("saveDewar", start, LOGGER);
	    return this.sendResponse(getShipping3Service().findByPk(shippingId, true));
	} catch (Exception e) {
	    e.printStackTrace();
	    LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_WS_ERROR, "saveDewar", start,
		    System.currentTimeMillis(), e.getMessage(), e);
	}
	return null;
    }
}
