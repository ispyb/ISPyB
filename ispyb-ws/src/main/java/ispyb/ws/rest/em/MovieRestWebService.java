package ispyb.ws.rest.em;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.em.services.collections.EM3Service;
import ispyb.server.em.vos.Movie;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.ws.rest.RestWebService;
import ispyb.ws.soap.em.ToolsForEMDataCollection;

import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.GZIP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
public class MovieRestWebService extends RestWebService {

	protected Logger log = LoggerFactory.getLogger(ToolsForEMDataCollection.class);

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private EM3Service getEMService() throws NamingException {
		return (EM3Service) ejb3ServiceLocator.getLocalService(EM3Service.class);

	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/em/datacollection/{dataCollectionId}/dose")
	@Produces("text/plain")
	public Response getDoseByDataCollectionId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId) throws Exception {

		log.info("getDoseByMovieId. technique=EM proposal={} dataCollectionId={}", proposal, dataCollectionId);
		List<String> doses = getEMService().getDoseByDataCollectionId(this.getProposalId(proposal), dataCollectionId);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < doses.size(); i++) {
			sb.append(i).append(",").append(doses.get(i)).append("\n");
		}
		return this.sendResponse(sb.toString());

	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/em/datacollection/{dataCollectionId}/movie")
	@Produces({ "application/json" })
	public Response getMoviesByDataCollectionId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId) throws Exception {

		log.info("getMoviesByDataCollectionId. technique=EM proposal={} dataCollectionId={}", proposal, dataCollectionId);
		return this.sendResponse(getEMService().getMoviesByDataCollectionId(this.getProposalId(proposal), dataCollectionId));

	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/em/datacollection/{dataCollectionId}/movie/all")
	@Produces({ "application/json" })
	public Response getMoviesDataByDataCollectionId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId) throws Exception {

		log.info("getMoviesDataByDataCollectionId. technique=EM proposal={} dataCollectionId={}", proposal, dataCollectionId);
		return this.sendResponse(getEMService().getMoviesDataByDataCollectionId(this.getProposalId(proposal), dataCollectionId), false);

	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/em/datacollection/{dataCollectionId}/movie/{movieId}/thumbnail")
	@Produces("image/png")
	public Response getMovieMicrographThumbnail(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId, @PathParam("movieId") int movieId) throws NamingException, Exception {

		log.info("getMovieMicrographThumbnail. technique=EM proposal={} dataCollectionId={} movieId={}", proposal, dataCollectionId, movieId);
		Movie movie = getEMService().getMovieByDataCollectionId(this.getProposalId(proposal), dataCollectionId, movieId);
		if (movie != null) {
			if (new File(movie.getThumbnailMicrographPath()).exists()) {
				return this.sendImage(movie.getThumbnailMicrographPath());
			} else {
				log.error("getMovieMicrographThumbnail Path {} does not exist. technique=EM ", movie.getThumbnailMicrographPath());
			}
		}
		return null;
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/em/datacollection/{dataCollectionId}/movie/{movieId}/metadata/xml")
	@Produces({ "application/json" })
	public Response getMovieMetadataXML(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId, @PathParam("movieId") int movieId) throws NamingException, Exception {

		log.info("getMovieMetadataXML. technique=EM proposal={} dataCollectionId={} movieId={}", proposal, dataCollectionId, movieId);
		Movie movie = getEMService().getMovieByDataCollectionId(this.getProposalId(proposal), dataCollectionId, movieId);
		if (movie != null) {
			if (new File(movie.getThumbnailMicrographPath()).exists()) {
				return this.downloadFileAsAttachment(movie.getXmlMetaDataPath());
			} else {
				log.error("getMovieMetadataXML Path {} does not exist. technique=EM ", movie.getThumbnailMicrographPath());
			}
		}
		return null;
	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/em/datacollection/{dataCollectionId}/movie/{movieId}/mrc")
	@Produces({ "application/json" })
	public Response getMovieMRC(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId, @PathParam("movieId") int movieId) throws NamingException, Exception {

		log.info("getMovieMetadataXML. technique=EM proposal={} dataCollectionId={} movieId={}", proposal, dataCollectionId, movieId);
		Movie movie = getEMService().getMovieByDataCollectionId(this.getProposalId(proposal), dataCollectionId, movieId);
		if (movie != null) {
			if (new File(movie.getMicrographPath()).exists()) {
				return this.downloadFileAsAttachment(movie.getMicrographPath());
			} else {
				log.error("getMovieMetadataXML Path {} does not exist. technique=EM ", movie.getMicrographPath());
			}
		}
		return null;
	}
	

}
