package ispyb.ws.rest.mx;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import ispyb.server.biosaxs.services.core.analysis.Analysis3Service;
import ispyb.server.biosaxs.services.core.analysis.primaryDataProcessing.PrimaryDataProcessing3Service;
import ispyb.server.biosaxs.services.core.experiment.Experiment3Service;
import ispyb.server.biosaxs.services.core.measurementToDataCollection.MeasurementToDataCollection3Service;
import ispyb.server.biosaxs.services.core.plateType.PlateType3Service;
import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.services.core.robot.Robot3Service;
import ispyb.server.biosaxs.services.core.samplePlate.Sampleplate3Service;
import ispyb.server.biosaxs.services.webUserInterface.WebUserInterfaceService;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.shipping.Dewar3Service;
import ispyb.server.common.services.shipping.external.External3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
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
import ispyb.server.mx.services.autoproc.phasingStep.PhasingStep3ServiceBean;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.services.collections.NativeDataCollection3Service;
import ispyb.server.mx.services.collections.workflowStep.WorkflowStep3Service;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.services.sample.Crystal3Service;
import ispyb.server.mx.services.utils.reader.AutoProcProgramaAttachmentFileReader;
import ispyb.server.mx.services.utils.reader.AutoProcessingData;
import ispyb.server.mx.services.utils.reader.AutoProcessingDataParser;
import ispyb.server.mx.vos.autoproc.AutoProcIntegration3VO;
import ispyb.server.mx.vos.autoproc.AutoProcProgramAttachment3VO;
import ispyb.server.mx.vos.autoproc.PhasingProgramRun3VO;
import ispyb.server.mx.vos.autoproc.PhasingStepVO;
import ispyb.ws.rest.RestWebService;

public class MXRestWebService extends RestWebService{

	
	
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
	
	protected Crystal3Service getCrystal3Service() throws NamingException {
		return (Crystal3Service) Ejb3ServiceLocator.getInstance().getLocalService(Crystal3Service.class);
	}
	
	protected WorkflowStep3Service getWorkflowStep3Service() throws NamingException {
		return (WorkflowStep3Service) Ejb3ServiceLocator.getInstance().getLocalService(WorkflowStep3Service.class);
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
	
	protected NativeDataCollection3Service getNativeDataCollection3Service() throws NamingException {
		return (NativeDataCollection3Service) Ejb3ServiceLocator.getInstance().getLocalService(NativeDataCollection3Service.class);
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
}
