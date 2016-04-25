package ispyb.ws.rest.mx;

import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.ws.rest.xfefluorescencespectrum.XFEFluorescenSpectrumService;
import ispyb.ws.rest.RestWebService;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/")
public class XFEFluorescenceSpectrumRestWebService extends RestWebService {
	 private final static Logger logger = Logger.getLogger(XFEFluorescenceSpectrumRestWebService.class);
	
	 
    @Path("{token}/proposal/{proposal}/mx/xrfscan/session/{sessionId}/list")
	@RolesAllowed({"User", "Manager", "LocalContact"})
	@GET
	@Produces({ "application/json" })
	public Response getEnergyScanBySessionId(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("sessionId") int sessionId) throws Exception {
		
		String methodName = "getEnergyScanBySessionId";
		long id = this.logInit(methodName, logger, token, proposal, sessionId);
		try{
			List<Map<String, Object>> result = getXFEFluorescenSpectrumService().getViewBySessionId(this.getProposalId(proposal), sessionId);
			this.logFinish(methodName, id, logger);
			return sendResponse(result );
		}
		catch(Exception e){
			return this.logError(methodName, e, id, logger);
		}
	}
	
    @Path("{token}/proposal/{proposal}/mx/xrfscan/xrfscanId/{xrfscanId}/image/{imageType}/get")
   	@RolesAllowed({"User", "Manager", "LocalContact"})
   	@GET
   	@Produces("image/png")
   	public Response getImage(
   			@PathParam("token") String token, 
   			@PathParam("proposal") String proposal,
   			@PathParam("xrfscanId") int xrfscanId,
   			@PathParam("imageType") String imageType) throws Exception {
   		
   		String methodName = "getImage";
   		long id = this.logInit(methodName, logger, token, proposal, xrfscanId);
   		try{
   			List<Map<String, Object>> result = getXFEFluorescenSpectrumService().getViewById(this.getProposalId(proposal), xrfscanId);
   			if (result != null){
   				if (result.size()> 0){
   					if (result.get(0).containsKey(imageType)){
   						String imageFilePath = result.get(0).get(imageType).toString();
   						if (imageFilePath != null){
   							if (new File(imageFilePath).exists()){
   								this.logFinish(methodName, id, logger);
   								return this.sendImage(imageFilePath);
   							}
   						}
   					}
   				}
   			}
   		}
   		catch(Exception e){
   			return this.logError(methodName, e, id, logger);
   		}
   		return null;
   	}
   	
    @Path("{token}/proposal/{proposal}/mx/xrfscan/xrfscanId/{xrfscanId}/file/{imageType}/get")
   	@RolesAllowed({"User", "Manager", "LocalContact"})
   	@GET
   	@Produces("text/plain")
   	public Response getFile(
   			@PathParam("token") String token, 
   			@PathParam("proposal") String proposal,
   			@PathParam("xrfscanId") int xrfscanId,
   			@PathParam("imageType") String imageType) throws Exception {
   		
   		String methodName = "getFile";
   		long id = this.logInit(methodName, logger, token, proposal, xrfscanId);
   		try{
   			List<Map<String, Object>> result = getXFEFluorescenSpectrumService().getViewById(this.getProposalId(proposal), xrfscanId);
   			if (result != null){
   				if (result.size()> 0){
   					if (result.get(0).containsKey(imageType)){
   						String imageFilePath = result.get(0).get(imageType).toString();
   						if (imageFilePath != null){
   							if (new File(imageFilePath).exists()){
   								this.logFinish(methodName, id, logger);
   								return this.sendImage(imageFilePath);
   							}
   						}
   					}
   				}
   			}
   		}
   		catch(Exception e){
   			return this.logError(methodName, e, id, logger);
   		}
   		return null;
   	}
	
	private XFEFluorescenSpectrumService getXFEFluorescenSpectrumService() throws NamingException {
		return (XFEFluorescenSpectrumService) Ejb3ServiceLocator.getInstance().getLocalService(XFEFluorescenSpectrumService.class);
	}
	

	
}
