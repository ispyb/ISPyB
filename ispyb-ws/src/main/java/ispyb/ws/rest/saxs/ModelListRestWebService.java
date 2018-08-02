package ispyb.ws.rest.saxs;

import ispyb.server.biosaxs.services.core.analysis.abInitioModelling.AbInitioModelling3Service;
import ispyb.server.biosaxs.services.utils.reader.zip.SAXSZipper;
import ispyb.server.biosaxs.vos.datacollection.Merge3VO;
import ispyb.server.biosaxs.vos.datacollection.ModelList3VO;
import ispyb.server.biosaxs.vos.datacollection.Subtraction3VO;
import ispyb.server.biosaxs.vos.datacollection.SubtractiontoAbInitioModel3VO;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;

@Path("/")
public class ModelListRestWebService extends SaxsRestWebService {

	private final static Logger logger = Logger.getLogger(ModelListRestWebService.class);

	@RolesAllowed({ "User", "Manager", "Industrial", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/saxs/subtraction/{subtractionId}/modellist/{modelListId}/nsd")
	@Produces("image/png")
	public Response getNSDFile(@PathParam("token") String token, @PathParam("proposal") String proposal, @PathParam("subtractionId") String subtractionId,
			@PathParam("modelListId") String modelListId) throws Exception {
		logger.info(String.format("getNSDFile. proposal=%s, subtractionId=%s, modelListId=%s", proposal, subtractionId, modelListId));		

		try {
			ModelList3VO modelList = this.getModelList3VOBySubtractionId(proposal, subtractionId);
			if (modelList != null) {
				if (modelList.getNsdFilePath() != null) {
					if (new File(modelList.getNsdFilePath()).exists()) {
						return sendImage(modelList.getNsdFilePath());
					}
				}
			}
		} catch (Exception e) {
			return sendError(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).build();
	}

	private ModelList3VO getModelList3VOBySubtractionId(String proposal, String subtractionId) throws Exception {
		List<Subtraction3VO> subtractions;
		try {
			subtractions = getAbinitioModelsBySubtractionId(proposal, subtractionId);

			if (subtractions != null) {
				if (subtractions.size() > 0) {
					Subtraction3VO subtraction = subtractions.get(0);
					if (subtraction.getSubstractionToAbInitioModel3VOs() != null) {
						if (subtraction.getSubstractionToAbInitioModel3VOs().size() > 0) {
							SubtractiontoAbInitioModel3VO subtractiontoAbInitioModel3VO = (SubtractiontoAbInitioModel3VO) subtraction
									.getSubstractionToAbInitioModel3VOs().toArray()[0];
							if (subtractiontoAbInitioModel3VO.getAbinitiomodel3VO() != null) {
								if (subtractiontoAbInitioModel3VO.getAbinitiomodel3VO().getModelList3VO() != null) {
									if (subtractiontoAbInitioModel3VO.getAbinitiomodel3VO().getModelList3VO().getNsdFilePath() != null) {
										if (new File(subtractiontoAbInitioModel3VO.getAbinitiomodel3VO().getModelList3VO().getNsdFilePath()).exists()) {
											return subtractiontoAbInitioModel3VO.getAbinitiomodel3VO().getModelList3VO();
										}
									}

								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		throw new Exception("ModelList not found");

	}

	@RolesAllowed({ "User", "Manager", "Industrial", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/saxs/subtraction/{subtractionId}/modellist/{modelListId}/chi2rg")
	@Produces("image/png")
	public Response getChi2rg(@PathParam("token") String token, @PathParam("proposal") String proposal, @PathParam("subtractionId") String subtractionId,
			@PathParam("modelListId") String modelListId) throws Exception {
		logger.info(String.format("getChi2rg. proposal=%s, subtractionId=%s, modelListId=%s", proposal, subtractionId, modelListId));

		try {
			ModelList3VO modelList = this.getModelList3VOBySubtractionId(proposal, subtractionId);
			if (modelList != null) {
				if (modelList.getChi2rgFilePath() != null) {
					if (new File(modelList.getChi2rgFilePath()).exists()) {
						return sendImage(modelList.getChi2rgFilePath());
					}
				}
			}
		} catch (Exception e) {
			return sendError(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).build();
	}

}
