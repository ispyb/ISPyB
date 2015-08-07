package ispyb.ws.rest.proposal;

import ispyb.server.biosaxs.vos.dataAcquisition.StockSolution3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Sampleplate3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.ws.rest.RestWebService;
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

@Path("/")
public class DewarRestWebService extends RestWebService {
	private final static Logger logger = Logger.getLogger(DewarRestWebService.class);

	@PermitAll
	@GET
	@Path("{token}/proposal/{proposal}/shipping/{shippingId}/dewar/{dewarId}/label")
	@Produces({ "application/json" })
	public Response labelDewar(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("shippingId") int shippingId, @PathParam("dewarId") int dewarId) throws NamingException {
		return Response.serverError().build();
	}

	@PermitAll
	@GET
	@Path("{token}/proposal/{proposal}/shipping/{shippingId}/dewar/{dewarId}/remove")
	@Produces({ "application/json" })
	public Response removeDewar(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("shippingId") int shippingId, @PathParam("dewarId") int dewarId) throws NamingException {

		long start = this.logInit("removeDewar", logger, token, proposal, shippingId, dewarId);

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
			this.logFinish("removeDewar", start, logger);
			return this.sendResponse(getShipping3Service().findByPk(shippingId, true));
		} catch (Exception e) {
			return this.logError("removeDewar", e, start, logger);
		}
	}

	@PermitAll
	@POST
	@Path("{token}/proposal/{proposal}/shipping/{shippingId}/dewar/save")
	@Produces({ "application/json" })
	public Response saveDewar(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("shippingId") int shippingId, @FormParam("sessionId") Integer sessionId,
			@FormParam("dewarId") Integer dewarId, @FormParam("code") String code,
			@FormParam("comments") String comments, @FormParam("storageLocation") String storageLocation,
			@FormParam("dewarStatus") String dewarStatus, @FormParam("blTimeStamp") String blTimeStamp,
			@FormParam("isStorageDewar") String isStorageDewar, @FormParam("barCode") String barCode,
			@FormParam("customValue") String customValue, @FormParam("transportValue") String transportValue,
			@FormParam("trackingNumberFromSynchrotron") String trackingNumberFromSynchrotron,
			@FormParam("trackingNumberToSynchrotron") String trackingNumberToSynchrotron) throws Exception {

		long start = this.logInit("saveDewar", logger, token, proposal, shippingId, dewarId, code, comments, storageLocation, dewarStatus, blTimeStamp, isStorageDewar, barCode, customValue, transportValue, trackingNumberFromSynchrotron, trackingNumberToSynchrotron);
		
		try {

			Dewar3VO dewar3vo = new Dewar3VO();
			if (dewarId == null) {
				dewar3vo.setType("Dewar");
				dewar3vo.setShippingVO(this.getShipping3Service().findByPk(shippingId, true));
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
			this.logFinish("saveDewar", start, logger);
			return this.sendResponse(getShipping3Service().findByPk(shippingId, true));
		} catch (Exception e) {
			this.logError("saveDewar", e, start, logger);
		}
		return null;
	}
}
