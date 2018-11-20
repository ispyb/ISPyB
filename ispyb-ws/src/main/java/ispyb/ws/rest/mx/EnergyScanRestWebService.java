package ispyb.ws.rest.mx;

import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.collections.EnergyScan3Service;
import ispyb.server.mx.services.ws.rest.energyscan.EnergyScanRestWsService;
import ispyb.server.mx.vos.collections.EnergyScan3VO;
import ispyb.ws.rest.RestWebService;

import java.io.File;
import java.util.ArrayList;
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
public class EnergyScanRestWebService extends RestWebService {
	 private final static Logger logger = Logger.getLogger(EnergyScanRestWebService.class);
	
	 
    @Path("{token}/proposal/{proposal}/mx/energyscan/session/{sessionId}/list")
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Produces({ "application/json" })
	public Response getEnergyScanBySessionId(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("sessionId") String sessionIdList) throws Exception {
		try{
			List<Integer> ids = this.parseToInteger(sessionIdList);
			List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
			for (Integer sessionId : ids) {
				result.addAll(this.getEnergyScanService().getViewBySessionId(this.getProposalId(proposal), sessionId));
			}
			return sendResponse(result );
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
    
    private EnergyScan3VO getEnergyById(int energyscanId, String proposal) throws NamingException, Exception{
    	List<Map<String, Object>> result = getEnergyScanService().getViewById(this.getProposalId(proposal), energyscanId);
		if (result.size() > 0){
			EnergyScan3Service energyScan3Service = (EnergyScan3Service) Ejb3ServiceLocator.getInstance().getLocalService(EnergyScan3Service.class);
			return energyScan3Service.findByPk(Integer.parseInt(result.get(0).get("energyScanId").toString()));
		}
		return null;
    }
    @Path("{token}/proposal/{proposal}/mx/energyscan/energyscanId/{energyscanId}/scanfile")
   	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
   	@GET
   	@Produces("text/plain")
   	public Response getScanFileFullPath(
   			@PathParam("token") String token, 
   			@PathParam("proposal") String proposal,
   			@PathParam("energyscanId") int energyscanId) throws Exception {
   		
   		String methodName = "getScanFileFullPath";
   		long start = this.logInit(methodName, logger, token, proposal, energyscanId);
   		try{
   			EnergyScan3VO energyScan = this.getEnergyById(energyscanId, proposal);
   			if (energyScan != null){
	   			if (new File(energyScan.getScanFileFullPath()).exists()){
	   				this.logFinish(methodName, start, logger);
	   				return this.downloadFileAsAttachment(energyScan.getScanFileFullPath());
				}
   			}
   		}
   		catch(Exception e){
   			return this.logError(methodName, e, start, logger);
   		}
		return null;
   	}
    
    @Path("{token}/proposal/{proposal}/mx/energyscan/energyscanId/{energyscanId}/chooch")
   	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
   	@GET
	@Produces("text/plain")
   	public Response getChooch(
   			@PathParam("token") String token, 
   			@PathParam("proposal") String proposal,
   			@PathParam("energyscanId") int energyscanId) throws Exception {
   		
   		String methodName = "getChooch";
   		long id = this.logInit(methodName, logger, token, proposal, energyscanId);
   		try{
   			
   			EnergyScan3VO energyScan = this.getEnergyById(energyscanId, proposal);
   			if (energyScan != null){
   				if (energyScan.getChoochFileFullPath() != null){
	   				if (new File(energyScan.getChoochFileFullPath()).exists()){
	   					logger.info("Energy scan found " + energyScan.getEnergyScanId());
	   	   				logger.info("File: " + energyScan.getScanFileFullPath());
	   	   			    logger.info("Downloading: " + energyScan.getChoochFileFullPath());
	   	   			    this.logFinish(methodName, id, logger);
	   					return this.downloadFileAsAttachment(energyScan.getChoochFileFullPath());
	   				}
   				}
   			}
   		}
   		catch(Exception e){
   			return this.logError(methodName, e, id, logger);
   		}
		return null;
   	}
    
    @Path("{token}/proposal/{proposal}/mx/energyscan/energyscanId/{energyscanId}/jpegchooch")
   	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
   	@GET
   	@Produces("image/png")
   	public Response getjpegchooch(
   			@PathParam("token") String token, 
   			@PathParam("proposal") String proposal,
   			@PathParam("energyscanId") int energyscanId) throws Exception {
   		
   		String methodName = "getjpegchooch";
   		long id = this.logInit(methodName, logger, token, proposal, energyscanId);
   		try{
   			
   			EnergyScan3VO energyScan = this.getEnergyById(energyscanId, proposal);
   			if (energyScan != null){
   				if (new File(energyScan.getJpegChoochFileFullPath()).exists()){
   					logger.info("Energy scan found " + energyScan.getEnergyScanId());
   	   				logger.info("File: " + energyScan.getJpegChoochFileFullPath());
   	   			    logger.info("Downloading: " + energyScan.getJpegChoochFileFullPath());
   	   			    this.logFinish(methodName, id, logger);
   					return this.sendImage(energyScan.getJpegChoochFileFullPath());
   				}
   			}
   		}
   		catch(Exception e){
   			return this.logError(methodName, e, id, logger);
   		}
		return null;
   	}

	
	private EnergyScanRestWsService getEnergyScanService() throws NamingException {
		return (EnergyScanRestWsService) Ejb3ServiceLocator.getInstance().getLocalService(EnergyScanRestWsService.class);
	}
	

	
}
