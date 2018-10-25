package ispyb.ws.rest.saxs;

import java.util.ArrayList;
import java.util.List;

import ispyb.server.biosaxs.services.core.analysis.Analysis3Service;
import ispyb.server.biosaxs.services.core.analysis.abInitioModelling.AbInitioModelling3Service;
import ispyb.server.biosaxs.services.core.analysis.primaryDataProcessing.PrimaryDataProcessing3Service;
import ispyb.server.biosaxs.services.core.experiment.Experiment3Service;
import ispyb.server.biosaxs.services.core.measurement.Measurement3Service;
import ispyb.server.biosaxs.services.core.measurementToDataCollection.MeasurementToDataCollection3Service;
import ispyb.server.biosaxs.services.core.robot.Robot3Service;
import ispyb.server.biosaxs.services.stats.Stats3Service;
import ispyb.server.biosaxs.services.webUserInterface.WebUserInterfaceService;
import ispyb.server.biosaxs.services.ws.rest.datacollection.SaxsDataCollectionRestWsService;
import ispyb.server.biosaxs.vos.datacollection.Subtraction3VO;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.ws.rest.RestWebService;

import javax.naming.NamingException;

public class SaxsRestWebService extends RestWebService{

	protected SaxsDataCollectionRestWsService getDataCollectionRestWsService() throws NamingException {
		return (SaxsDataCollectionRestWsService) Ejb3ServiceLocator.getInstance().getLocalService(SaxsDataCollectionRestWsService.class);
	}
	
	protected Stats3Service getStats3Service() throws NamingException {
		return (Stats3Service) Ejb3ServiceLocator.getInstance().getLocalService(Stats3Service.class);
	}
	
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
	
	protected Measurement3Service getMeasurementService() throws NamingException {
		return (Measurement3Service) Ejb3ServiceLocator.getInstance().getLocalService(
				Measurement3Service.class);
	}

	protected Robot3Service getRobot3Service() throws NamingException {
		return (Robot3Service) Ejb3ServiceLocator.getInstance().getLocalService(Robot3Service.class);
	}

	
	protected PrimaryDataProcessing3Service getPrimaryDataProcessing3Service() throws NamingException {
		return (PrimaryDataProcessing3Service) Ejb3ServiceLocator.getInstance().getLocalService(
				PrimaryDataProcessing3Service.class);
	}
	
	protected AbInitioModelling3Service getAbInitioModelling3Service() throws NamingException {
		return (AbInitioModelling3Service) Ejb3ServiceLocator.getInstance().getLocalService(AbInitioModelling3Service.class);
	}
	
	
	
	protected WebUserInterfaceService getWebUserInterfaceService() throws NamingException {
		return (WebUserInterfaceService) Ejb3ServiceLocator.getInstance()
				.getLocalService(WebUserInterfaceService.class);
	}
	
	protected List<Subtraction3VO> getAbinitioModelsBySubtractionId(String proposal, String subtractionIdList)
			throws NamingException {
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		AbInitioModelling3Service abInitioModelling3Service = (AbInitioModelling3Service) ejb3ServiceLocator
				.getLocalService(AbInitioModelling3Service.class);

		List<Integer> list = parseToInteger(subtractionIdList);
		List<Subtraction3VO> result = new ArrayList<Subtraction3VO>();
		if (subtractionIdList != null) {
			for (Integer subtractionId : list) {
				result.addAll(abInitioModelling3Service.getAbinitioModelsBySubtractionId(subtractionId));
			}
		}
		return result;
	}
}
