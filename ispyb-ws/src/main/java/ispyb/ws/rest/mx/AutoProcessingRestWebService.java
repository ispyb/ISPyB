package ispyb.ws.rest.mx;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.autoproc.AutoProc3Service;
import ispyb.server.mx.services.autoproc.AutoProcIntegration3Service;
import ispyb.server.mx.services.autoproc.AutoProcProgram3Service;
import ispyb.server.mx.services.autoproc.AutoProcProgramAttachment3Service;
import ispyb.server.mx.services.autoproc.AutoProcScalingStatistics3Service;
import ispyb.server.mx.services.autoproc.ModelBuilding3Service;
import ispyb.server.mx.services.autoproc.Phasing3Service;
import ispyb.server.mx.services.autoproc.PhasingHasScaling3Service;
import ispyb.server.mx.services.autoproc.PhasingStatistics3Service;
import ispyb.server.mx.services.autoproc.PreparePhasingData3Service;
import ispyb.server.mx.services.autoproc.SubstructureDetermination3Service;
import ispyb.server.mx.services.utils.reader.AutoProcProgramaAttachmentFileReader;
import ispyb.server.mx.services.utils.reader.AutoProcessingData;
import ispyb.server.mx.services.utils.reader.AutoProcessingDataParser;
import ispyb.server.mx.vos.autoproc.AutoProcIntegration3VO;
import ispyb.server.mx.vos.autoproc.AutoProcProgramAttachment3VO;
import ispyb.ws.rest.RestWebService;

public class AutoProcessingRestWebService extends RestWebService {

	
	protected AutoProcessingDataParser getAutoprocessingParserByAutoProcIntegrationListId(List<Integer> ids) throws NamingException, Exception {
		List<List<AutoProcessingData>> lists = new ArrayList<List<AutoProcessingData>>();
		for (Integer id : ids) {
			AutoProcIntegration3VO autoProcIntegration3VO = this.getAutoProcIntegration3Service().findByPk(id);
			List<AutoProcProgramAttachment3VO> xscaleAttachmentList = this.getAutoProcProgramAttachment3Service()
					.findXScale(autoProcIntegration3VO.getAutoProcProgramVOId());
			for (AutoProcProgramAttachment3VO autoProcProgramAttachment3VO : xscaleAttachmentList) {
				List<AutoProcessingData> data = AutoProcProgramaAttachmentFileReader
						.getAutoProcessingDataFromAttachemt(autoProcProgramAttachment3VO);
				lists.add(data);
			}
		}
		return new AutoProcessingDataParser(lists);
	}
	
	
	protected AutoProcProgramAttachment3Service getAutoProcProgramAttachment3Service() throws NamingException {
		return (AutoProcProgramAttachment3Service) Ejb3ServiceLocator.getInstance().getLocalService(AutoProcProgramAttachment3Service.class);
	}
	
	protected AutoProc3Service getAutoProc3Service() throws NamingException {
		return (AutoProc3Service) Ejb3ServiceLocator.getInstance().getLocalService(AutoProc3Service.class);
	}
	
	
	protected AutoProcScalingStatistics3Service getAutoProcScalingStatistics3Service() throws NamingException {
		return (AutoProcScalingStatistics3Service) Ejb3ServiceLocator.getInstance().getLocalService(AutoProcScalingStatistics3Service.class);
	}
	
	protected AutoProcProgram3Service getAutoProcProgram3Service() throws NamingException {
		return (AutoProcProgram3Service) Ejb3ServiceLocator.getInstance().getLocalService(AutoProcProgram3Service.class);
	}
	
	
	protected AutoProcIntegration3Service getAutoProcIntegration3Service() throws NamingException {
		return (AutoProcIntegration3Service) Ejb3ServiceLocator.getInstance().getLocalService(AutoProcIntegration3Service.class);
	}
	
	protected PhasingHasScaling3Service getPhasingHasScaling3Service() throws NamingException {
		return (PhasingHasScaling3Service) Ejb3ServiceLocator.getInstance().getLocalService(PhasingHasScaling3Service.class);
	}
	
	protected PreparePhasingData3Service getPreparePhasingData3Service() throws NamingException {
		return (PreparePhasingData3Service) Ejb3ServiceLocator.getInstance().getLocalService(PreparePhasingData3Service.class);
	}
	
	protected SubstructureDetermination3Service getSubstructureDetermination3Service() throws NamingException {
		return (SubstructureDetermination3Service) Ejb3ServiceLocator.getInstance().getLocalService(SubstructureDetermination3Service.class);
	}
	
	protected Phasing3Service getPhasing3Service() throws NamingException {
		return (Phasing3Service) Ejb3ServiceLocator.getInstance().getLocalService(Phasing3Service.class);
	}
	
	protected ModelBuilding3Service getModelBuilding3Service() throws NamingException {
		return (ModelBuilding3Service) Ejb3ServiceLocator.getInstance().getLocalService(ModelBuilding3Service.class);
	}
	
	protected PhasingStatistics3Service getPhasingStatistics3Service() throws NamingException {
		return (PhasingStatistics3Service) Ejb3ServiceLocator.getInstance().getLocalService(PhasingStatistics3Service.class);
	}
	
}
