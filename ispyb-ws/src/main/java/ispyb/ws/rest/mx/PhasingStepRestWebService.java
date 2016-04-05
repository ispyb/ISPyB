package ispyb.ws.rest.mx;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.reflect.TypeToken;

import ispyb.server.mx.services.autoproc.PhasingProgramAttachment3Service;
import ispyb.server.mx.vos.autoproc.PhasingProgramAttachment3VO;
import ispyb.server.mx.vos.autoproc.PhasingProgramRun3VO;
import ispyb.server.mx.vos.autoproc.PhasingStepVO;
import ispyb.server.mx.vos.autoproc.SpaceGroup3VO;

@Path("/")
public class PhasingStepRestWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(PhasingStepRestWebService.class);

	@Deprecated
	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@POST
	@Path("{token}/proposal/{proposal}/mx/phasingStep")
	@Produces("text/plain")
	public Response test(@PathParam("token") String token,
			@PathParam("proposal") String proposal, 
			@FormParam("phasingStep") String json) {

		String methodName = "PhasingStep";
		System.out.println("testing phasing Step");
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			PhasingStepVO phasingStepVO = this.getGson().fromJson(json, PhasingStepVO.class);
			phasingStepVO = this.getPhasingStep3Service().findById(1);
			return this.sendResponse(phasingStepVO);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@Deprecated
	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@POST
	@Path("{token}/proposal/{proposal}/mx/program")
	@Produces("text/plain")
	public Response test2(@PathParam("token") String token,
			@PathParam("proposal") String proposal, 
			@FormParam("phasingStep") String json) {

		String methodName = "PhasingStep";
		System.out.println("testing phasing Step");
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			
			
			
			return this.sendResponse(this.getAutoProcProgramAttachment3Service().findByPk(1));
			
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@Deprecated
	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@POST
	@Path("{token}/proposal/{proposal}/mx/save")
	@Produces("text/plain")
	public Response save(@PathParam("token") String token,
			@PathParam("proposal") String proposal, 
			@FormParam("phasingStep") String phasingStep,
			@FormParam("spaceGroup") String spaceGroup,
			@FormParam("run") String run,
			@FormParam("attachments") String attachments) {

		String methodName = "PhasingStep";
		System.out.println("testing phasing Step");
		long start = this.logInit(methodName, logger, token, proposal, phasingStep, spaceGroup, run, attachments );
		try {
			
			PhasingStepVO phasingStepVO = this.getGson().fromJson(phasingStep, PhasingStepVO.class);
			SpaceGroup3VO spaceGroup3VO = this.getGson().fromJson(spaceGroup, SpaceGroup3VO.class);
			List<SpaceGroup3VO> spaceGroups = this.getSpaceGroup3Service().findBySpaceGroupShortName(spaceGroup3VO.getSpaceGroupShortName());
			
			if (spaceGroups.size() > 0){
				phasingStepVO.setSpaceGroupVO(spaceGroups.get(0));
			}
			else{
				/* Creating Space Group **/
				phasingStepVO.setSpaceGroupVO(this.getSpaceGroup3Service().create(spaceGroup3VO));
			}

			List<PhasingProgramAttachment3VO> phasingProgramAttachment3VOs = new ArrayList<PhasingProgramAttachment3VO>();
			if (attachments != null){
				Type listType = new TypeToken<ArrayList<PhasingProgramAttachment3VO>>() {}.getType();
				phasingProgramAttachment3VOs = this.getGson().fromJson(attachments, listType);
			}
			System.out.println(phasingProgramAttachment3VOs.size());
			
			PhasingProgramRun3VO phasingProgramRun3VO =  this.getGson().fromJson(run, PhasingProgramRun3VO.class);
			PhasingProgramRun3VO program = this.getPhasingProgramRun3Service().create(phasingProgramRun3VO);
			phasingStepVO.setPhasingProgramRunVO(program);
			
			for (PhasingProgramAttachment3VO phasingProgramAttachment3VO : phasingProgramAttachment3VOs) {
				phasingProgramAttachment3VO.setPhasingProgramRunVO(program);
				System.out.println("creating");
				this.getPhasingProgramAttachment3Service().create(phasingProgramAttachment3VO);
			}
			
			phasingStepVO = this.getPhasingStep3Service().merge(phasingStepVO);
			return this.sendResponse(phasingStepVO);
			
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	

}
