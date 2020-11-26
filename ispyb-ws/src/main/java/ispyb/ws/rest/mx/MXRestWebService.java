package ispyb.ws.rest.mx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import ispyb.server.biosaxs.services.core.experiment.Experiment3Service;
import ispyb.server.biosaxs.services.core.structure.Structure3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.login.Login3VO;
import ispyb.server.mx.services.autoproc.AutoProc3Service;
import ispyb.server.mx.services.autoproc.AutoProcIntegration3Service;
import ispyb.server.mx.services.autoproc.AutoProcProgram3Service;
import ispyb.server.mx.services.autoproc.AutoProcProgramAttachment3Service;
import ispyb.server.mx.services.autoproc.AutoProcScalingStatistics3Service;
import ispyb.server.mx.services.autoproc.ModelBuilding3Service;
import ispyb.server.mx.services.autoproc.Phasing3Service;
import ispyb.server.mx.services.autoproc.PhasingHasScaling3Service;
import ispyb.server.mx.services.autoproc.PhasingProgramAttachment3Service;
import ispyb.server.mx.services.autoproc.PhasingProgramRun3Service;
import ispyb.server.mx.services.autoproc.PhasingStatistics3Service;
import ispyb.server.mx.services.autoproc.PreparePhasingData3Service;
import ispyb.server.mx.services.autoproc.SpaceGroup3Service;
import ispyb.server.mx.services.autoproc.SubstructureDetermination3Service;
import ispyb.server.mx.services.autoproc.phasingStep.PhasingStep3Service;
import ispyb.server.mx.services.collections.Workflow3Service;
import ispyb.server.mx.services.collections.workflowStep.WorkflowStep3Service;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.services.sample.Crystal3Service;
import ispyb.server.mx.services.utils.reader.AutoProcProgramaAttachmentFileReader;
import ispyb.server.mx.services.utils.reader.AutoProcessingData;
import ispyb.server.mx.services.utils.reader.AutoProcessingDataParser;
import ispyb.server.mx.services.ws.rest.datacollection.DataCollectionRestWsService;
import ispyb.server.mx.services.ws.rest.datacollectiongroup.DataCollectionGroupRestWsService;
import ispyb.server.mx.services.ws.rest.energyscan.EnergyScanRestWsService;
import ispyb.server.mx.services.ws.rest.xfefluorescencespectrum.XFEFluorescenSpectrumRestWsService;
import ispyb.server.mx.vos.autoproc.AutoProcIntegration3VO;
import ispyb.server.mx.vos.autoproc.AutoProcProgramAttachment3VO;
import ispyb.ws.rest.RestWebService;

public class MXRestWebService extends RestWebService{

	
	
	protected AutoProcessingDataParser getAutoprocessingParserByAutoProcIntegrationListId(List<Integer> ids) throws NamingException, Exception {
		List<List<AutoProcessingData>> lists = new ArrayList<List<AutoProcessingData>>();
		for (Integer id : ids) {
			AutoProcIntegration3VO autoProcIntegration3VO = this.getAutoProcIntegration3Service().findByPk(id);
			List<AutoProcProgramAttachment3VO> xscaleAttachmentList = this.getAutoProcProgramAttachment3Service().findXScale(autoProcIntegration3VO.getAutoProcProgramVOId());
			for (AutoProcProgramAttachment3VO autoProcProgramAttachment3VO : xscaleAttachmentList) {
				List<AutoProcessingData> data = AutoProcProgramaAttachmentFileReader.getAutoProcessingDataFromAttachemt(autoProcProgramAttachment3VO);
				lists.add(data);
			}
		}
		return new AutoProcessingDataParser(lists);
	}

	protected AutoProcessingDataParser getFastDPParserByAutoProcIntegrationListId(List<Integer> ids) throws NamingException, Exception {
		List<List<AutoProcessingData>> lists = new ArrayList<List<AutoProcessingData>>();
		for (Integer id : ids) {
			AutoProcIntegration3VO autoProcIntegration3VO = this.getAutoProcIntegration3Service().findByPk(id);
			List<AutoProcProgramAttachment3VO> aimlessAttachmentList = this.getAutoProcProgramAttachment3Service().findAimless(autoProcIntegration3VO.getAutoProcProgramVOId());
			for (AutoProcProgramAttachment3VO autoProcProgramAttachment3VO : aimlessAttachmentList) {
				List<AutoProcessingData> data = AutoProcProgramaAttachmentFileReader.getAutoProcessingDataFromAttachemt(autoProcProgramAttachment3VO);
				lists.add(data);
			}
		}
		return new AutoProcessingDataParser(lists);
	}
	
	
	protected Experiment3Service getExperiment3Service() throws NamingException {
		return (Experiment3Service) Ejb3ServiceLocator.getInstance().getLocalService(Experiment3Service.class);
	}
	
	
	
	protected Crystal3Service getCrystal3Service() throws NamingException {
		return (Crystal3Service) Ejb3ServiceLocator.getInstance().getLocalService(Crystal3Service.class);
	}
	
	protected WorkflowStep3Service getWorkflowStep3Service() throws NamingException {
		return (WorkflowStep3Service) Ejb3ServiceLocator.getInstance().getLocalService(WorkflowStep3Service.class);
	}
	
	protected Workflow3Service getWorkflow3Service() throws NamingException {
		return (Workflow3Service) Ejb3ServiceLocator.getInstance().getLocalService(Workflow3Service.class);
	}
	
	
	protected PhasingStep3Service getPhasingStep3Service() throws NamingException {
		return (PhasingStep3Service) Ejb3ServiceLocator.getInstance().getLocalService(PhasingStep3Service.class);
	}
	
	protected SpaceGroup3Service getSpaceGroup3Service() throws NamingException {
		return (SpaceGroup3Service) Ejb3ServiceLocator.getInstance().getLocalService(SpaceGroup3Service.class);
	}
	
	protected PhasingProgramRun3Service getPhasingProgramRun3Service() throws NamingException {
		return (PhasingProgramRun3Service) Ejb3ServiceLocator.getInstance().getLocalService(PhasingProgramRun3Service.class);
	}
	
	protected PhasingProgramAttachment3Service getPhasingProgramAttachment3Service() throws NamingException {
		return (PhasingProgramAttachment3Service) Ejb3ServiceLocator.getInstance().getLocalService(PhasingProgramAttachment3Service.class);
	}
	
	protected DataCollectionGroupRestWsService getWebServiceDataCollectionGroup3Service() throws NamingException {
		return (DataCollectionGroupRestWsService) Ejb3ServiceLocator.getInstance().getLocalService(DataCollectionGroupRestWsService.class);
	}
	
	protected EnergyScanRestWsService getWebServiceEnergyScan3Service() throws NamingException {
		return (EnergyScanRestWsService) Ejb3ServiceLocator.getInstance().getLocalService(EnergyScanRestWsService.class);		
	}
	
	protected XFEFluorescenSpectrumRestWsService getWebServiceXFEFluorescenSpectrum3Service() throws NamingException {
		return (XFEFluorescenSpectrumRestWsService) Ejb3ServiceLocator.getInstance().getLocalService(XFEFluorescenSpectrumRestWsService.class);		
	}

	protected DataCollectionRestWsService getWebServiceDataCollection3Service() throws NamingException {
		return (DataCollectionRestWsService) Ejb3ServiceLocator.getInstance().getLocalService(DataCollectionRestWsService.class);
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
	
	protected BLSample3Service getBLSample3Service() throws NamingException {
		return (BLSample3Service) Ejb3ServiceLocator.getInstance().getLocalService(BLSample3Service.class);
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
	
	protected Structure3Service getStruture3Service() throws NamingException {
		return (Structure3Service) Ejb3ServiceLocator.getInstance().getLocalService(Structure3Service.class);
	}
}
