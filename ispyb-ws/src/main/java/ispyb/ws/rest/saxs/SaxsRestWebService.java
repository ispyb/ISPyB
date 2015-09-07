package ispyb.ws.rest.saxs;

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
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.ws.rest.RestWebService;

public class SaxsRestWebService extends RestWebService{

	
	
	
	protected Analysis3Service getAnalysis3Service() throws NamingException {
		return (Analysis3Service) Ejb3ServiceLocator.getInstance().getLocalService(Analysis3Service.class);
	}

	protected Experiment3Service getExperiment3Service() throws NamingException {
		return (Experiment3Service) Ejb3ServiceLocator.getInstance().getLocalService(Experiment3Service.class);
	}

	protected MeasurementToDataCollection3Service getMeasurementToDataCollectionService() throws NamingException {
		return (MeasurementToDataCollection3Service) Ejb3ServiceLocator.getInstance().getLocalService(
				MeasurementToDataCollection3Service.class);
	}

	protected Robot3Service getRobot3Service() throws NamingException {
		return (Robot3Service) Ejb3ServiceLocator.getInstance().getLocalService(Robot3Service.class);
	}

	

	protected PrimaryDataProcessing3Service getPrimaryDataProcessing3Service() throws NamingException {
		return (PrimaryDataProcessing3Service) Ejb3ServiceLocator.getInstance().getLocalService(
				PrimaryDataProcessing3Service.class);
	}
	
	

	protected WebUserInterfaceService getWebUserInterfaceService() throws NamingException {
		return (WebUserInterfaceService) Ejb3ServiceLocator.getInstance()
				.getLocalService(WebUserInterfaceService.class);
	}
}
