package ispyb.ws.rest.mx;

import ispyb.server.mx.vos.autoproc.ModelBuilding3VO;
import ispyb.server.mx.vos.autoproc.Phasing3VO;
import ispyb.server.mx.vos.autoproc.PhasingHasScaling3VO;
import ispyb.server.mx.vos.autoproc.PhasingStatistics3VO;
import ispyb.server.mx.vos.autoproc.PreparePhasingData3VO;
import ispyb.server.mx.vos.autoproc.SubstructureDetermination3VO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/")
public class PhasingAutoProcessingRestWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(PhasingAutoProcessingRestWebService.class);

	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/phasing/autoprocid/{autoProcListId}/list")
	@Produces({ "application/json" })
	public Response getAnalysisByAutoProcId(@PathParam("token") String token,
			@PathParam("proposal") String proposal, @PathParam("autoProcListId") String autoProcListId) {

		String methodName = "getAnalysisByAutoProcId";
		long start = this.logInit(methodName, logger, token, proposal, autoProcListId);
		try {
			List<Integer> autoProcIds = this.parseToInteger(autoProcListId);
			List<HashMap<String, Object>> result = new ArrayList<HashMap<String,Object>>();
			for (Integer autoProcId : autoProcIds) {
				HashMap<String, Object> record = new HashMap<String, Object>();
				List<PhasingHasScaling3VO> phasingHasScaling = this.getPhasingHasScaling3Service().findByAutoProc(autoProcId);
				record.put("phasinghasscaling", phasingHasScaling);
				
				List<PhasingStatistics3VO> statistics = this.getPhasingStatistics3Service().findFiltered(autoProcId, null, true);
				record.put("statistics", statistics);
				
				List<HashMap<String, Object>> phasingRecords = new ArrayList<HashMap<String, Object>>();
				if (phasingHasScaling != null){
					for (PhasingHasScaling3VO phasingHasScaling3VO : phasingHasScaling) {
						HashMap<String, Object> phasingRecord = new HashMap<String, Object>();
						int phasingAnalysisId = phasingHasScaling3VO.getPhasingAnalysisVO().getPhasingAnalysisId();
						List<PreparePhasingData3VO> prepare = this.getPreparePhasingData3Service().findFiltered(phasingAnalysisId);
						phasingRecord.put("preparephasingdata", prepare);
						
						List<SubstructureDetermination3VO> substructure = this.getSubstructureDetermination3Service().findFiltered(phasingAnalysisId);
						phasingRecord.put("substructureDetermination3VO", substructure);
						
						List<Phasing3VO> phasing = this.getPhasing3Service().findFiltered(phasingAnalysisId);
						phasingRecord.put("phasing", phasing);
						
						List<ModelBuilding3VO> modelBuilding3VO = this.getModelBuilding3Service().findFiltered(phasingAnalysisId);
						phasingRecord.put("modelbuilding", modelBuilding3VO);
						phasingRecords.add(phasingRecord);
					}
					record.put("phasinganalysis", phasingRecords);
				}
				result.add(record);
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	
	@RolesAllowed({ "User", "Manager", "LocalContact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/phasing/datacollection/{dataCollectionIdList}/view")
	@Produces({ "application/json" })
	public Response getPhasingByDatacollectionId(@PathParam("token") String token,
			@PathParam("proposal") String proposal, @PathParam("dataCollectionIdList") String dataCollectionIdList) {

		String methodName = "getPhasingByDatacollectionId";
		long start = this.logInit(methodName, logger, token, proposal, dataCollectionIdList);
		
		
		try {
			List<Integer> dataCollectionIds = this.parseToInteger(dataCollectionIdList);
			List<List<Map<String, Object>>> result = new ArrayList<List<Map<String,Object>>>();
			for (Integer dataCollectionId : dataCollectionIds) {
				result.add(this.getNativeDataCollection3Service().getPhasingViewByDataCollectionId(dataCollectionId));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

}
