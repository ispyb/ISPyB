/*******************************************************************************
 * This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ISPyB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
 ******************************************************************************************************************************/

package ispyb.server.biosaxs.services;

import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.biosaxs.services.core.ExperimentScope;
import ispyb.server.biosaxs.services.core.analysis.Analysis3Service;
import ispyb.server.biosaxs.services.core.analysis.abInitioModelling.AbInitioModelling3Service;
import ispyb.server.biosaxs.services.core.analysis.hplc.HPLCDataProcessing3Service;
import ispyb.server.biosaxs.services.core.analysis.primaryDataProcessing.PrimaryDataProcessing3Service;
import ispyb.server.biosaxs.services.core.experiment.Experiment3Service;
import ispyb.server.biosaxs.services.core.robot.Robot3Service;
import ispyb.server.biosaxs.services.webservice.ATSASPipeline3Service;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Measurement3VO;
import ispyb.server.biosaxs.vos.datacollection.MeasurementTodataCollection3VO;
import ispyb.server.biosaxs.vos.datacollection.Model3VO;
import ispyb.server.biosaxs.vos.datacollection.SaxsDataCollection3VO;
import ispyb.server.biosaxs.vos.utils.comparator.SaxsDataCollectionComparator;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.ProposalWS3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.collections.SessionWS3VO;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BiosaxsServices {

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private final static Logger LOG = Logger.getLogger("BiosaxsWebServiceActions");

	private Proposal3Service proposalService;

	private Session3Service sessionService;

	private Experiment3Service experiment3Service;

	private Analysis3Service analysis3Service;

	private AbInitioModelling3Service abInitioModelling3Service;

	private PrimaryDataProcessing3Service primaryDataProcessing3Service;

	private HPLCDataProcessing3Service hplcDataProcessing3Service;

	private ATSASPipeline3Service atsasPipeline3Service;

	private Robot3Service robotService;

	public BiosaxsServices() {
		try {
			this.proposalService = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
			this.sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
			this.experiment3Service = (Experiment3Service) ejb3ServiceLocator.getLocalService(Experiment3Service.class);
			this.analysis3Service = (Analysis3Service) ejb3ServiceLocator.getLocalService(Analysis3Service.class);
			this.atsasPipeline3Service = (ATSASPipeline3Service) ejb3ServiceLocator.getLocalService(ATSASPipeline3Service.class);
			this.abInitioModelling3Service = (AbInitioModelling3Service) ejb3ServiceLocator
					.getLocalService(AbInitioModelling3Service.class);
			this.primaryDataProcessing3Service = (PrimaryDataProcessing3Service) ejb3ServiceLocator
					.getLocalService(PrimaryDataProcessing3Service.class);
			this.hplcDataProcessing3Service = (HPLCDataProcessing3Service) ejb3ServiceLocator
					.getLocalService(HPLCDataProcessing3Service.class);
			this.robotService = (Robot3Service) ejb3ServiceLocator.getLocalService(Robot3Service.class);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * @param proposalCode
	 * @param proposalNumber
	 * @param samples
	 * @param mode
	 *            : [BeforeAndAfter, After, Before]
	 * @param storageTemperature
	 * @param extraFlowTime
	 * @param type
	 *            : [STATIC, CALIBRATION]
	 * @param sourceFilePath
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public Experiment3VO createExperiment(String proposalCode, String proposalNumber,
			ArrayList<HashMap<String, String>> samples, String mode, String storageTemperature, String extraFlowTime,
			String type, String sourceFilePath, String name, String comments) throws Exception {

		ProposalWS3VO proposal = proposalService.findForWSByCodeAndNumber(proposalCode, proposalNumber);
		Integer sessionId = null;
		if (proposal != null) {
			try {
				// retrieve the session to attach the data to
				sessionId = getSessionIdForExperiment(proposal);
				// update the lastUpdate timestamp which will be use to launch the data confidentiality protection
				if (sessionId != null) {
					Session3VO session = sessionService.findByPk(sessionId, false, false, false);
					if (session != null) {
						Calendar cal = Calendar.getInstance();
						Date today = cal.getTime();
						if (today.after(session.getEndDate())) {
							session.setLastUpdate(today);
							session.setUsedFlag((byte) 1);
							sessionService.update(session);
						}
					}
				}
			} catch (Exception exp) {
				exp.printStackTrace();
			}

			int proposalId = proposal.getProposalId();
			Robot3Service robot3Service = (Robot3Service) ejb3ServiceLocator.getLocalService(Robot3Service.class);
			return robot3Service.createExperimentFromRobotParams(samples, sessionId, proposalId, mode,
					storageTemperature, extraFlowTime, type, sourceFilePath, name, comments);
		}
		return null;
	}

	/**
	 * @param experimentId
	 * @param status
	 * @throws NamingException
	 */
	public void updateStatus(Integer experimentId, String status) throws NamingException {
		Experiment3VO experiment = this.experiment3Service.findById(experimentId, ExperimentScope.MINIMAL);
		experiment.setStatus(status);
		experiment3Service.merge(experiment);
	}

	/**
	 * @param mode
	 * @param experimentId
	 * @param measurementId
	 * @param sampleCode
	 * @param exposureTemperature
	 * @param storageTemperature
	 * @param timePerFrame
	 * @param timeStart
	 * @param timeEnd
	 * @param energy
	 * @param detectorDistance
	 * @param edfFileArray
	 * @param snapshotCapillary
	 * @param currentMachine
	 * @param tocollect
	 * @param pars
	 * @param beamCenterX
	 * @param beamCenterY
	 * @param radiationRelative
	 * @param radiationAbsolute
	 * @param pixelSizeX
	 * @param pixelSizeY
	 * @param normalization
	 * @param transmission
	 */
	public void saveFrame(String mode, int experimentId, String measurementId, String runNumber,
			String exposureTemperature, String storageTemperature, String timePerFrame, String timeStart,
			String timeEnd, String energy, String detectorDistance, String edfFileArray, String snapshotCapillary,
			String currentMachine, String tocollect, String pars, String beamCenterX, String beamCenterY,
			String radiationRelative, String radiationAbsolute, String pixelSizeX, String pixelSizeY,
			String normalization, String transmission) {

		
		
		
		/** This may be optimized **/
		if (mode.toLowerCase().equals("before")) {
			Experiment3VO experiment = this.experiment3Service.findById(experimentId, ExperimentScope.MEDIUM);
			Measurement3VO bufferBefore = experiment.getMeasurementBefore(Integer.parseInt(measurementId));
			measurementId = bufferBefore.getMeasurementId().toString();
			
		}

		if (mode.toLowerCase().equals("after")) {
			Experiment3VO experiment = this.experiment3Service.findById(experimentId, ExperimentScope.MEDIUM);
			Measurement3VO bufferAfter = experiment.getMeasurementAfter(Integer.parseInt(measurementId));
			measurementId = bufferAfter.getMeasurementId().toString();
		}

		/** Converting the array of files into an ArrayList **/
		Gson gson = new Gson();
		Type mapType = new TypeToken<ArrayList<String>>() {
		}.getType();
		ArrayList<String> files = gson.fromJson(edfFileArray, mapType);

		this.primaryDataProcessing3Service.addRun(experimentId, measurementId.toString(), runNumber,
				exposureTemperature, storageTemperature, timePerFrame, timeStart, timeEnd, energy, detectorDistance,
				edfFileArray, snapshotCapillary, currentMachine, tocollect, pars, beamCenterX, beamCenterY,
				radiationRelative, radiationAbsolute, pixelSizeX, pixelSizeY, normalization, transmission, files);
	}

	/**
	 * This method stores averages and subtractions
	 * 
	 * @param valueOf
	 * @param parseInt
	 * @param parseInt2
	 * @param curveParam
	 * @param rg
	 * @param rgStdev
	 * @param i0
	 * @param i0Stdev
	 * @param firstPointUsed
	 * @param lastPointUsed
	 * @param quality
	 * @param isagregated
	 * @param code
	 * @param concentration
	 * @param gnomFile
	 * @param rgGuinier
	 * @param rgGnom
	 * @param dmax
	 * @param total
	 * @param volume
	 * @param scatteringFilePath
	 * @param guinierFilePath
	 * @param kratkyFilePath
	 * @param densityPlot
	 * @param substractedFilePath
	 * @param gnomFilePathOutput
	 */
	public void saveCurveAnalysis(Integer measurementId, Integer dataCollectionOrderParam, int framesAverage,
			int framesCount, String curveParam, String rg, String rgStdev, String i0, String i0Stdev,
			String firstPointUsed, String lastPointUsed, String quality, String isagregated, String code,
			String concentration, String gnomFile, String rgGuinier, String rgGnom, String dmax, String total,
			String volume, String scatteringFilePath, String guinierFilePath, String kratkyFilePath, String densityPlot) {

		if (dataCollectionOrderParam != null) {
			int dataCollectionOrder = Integer.valueOf(dataCollectionOrderParam);
			/** Buffer before **/
			if (dataCollectionOrder == 0) {
				Experiment3VO experiment = experiment3Service.findByMeasurementId(measurementId);
				String measurementBufferBeforeId = this.getMeasurementBefore(experiment, measurementId);
				primaryDataProcessing3Service.addMerge(
						Integer.parseInt(measurementBufferBeforeId), 
						framesAverage,
						framesCount, 
						curveParam, "");
			}

			/** Sample **/
			if (dataCollectionOrder == 1) {
				primaryDataProcessing3Service.addMerge(measurementId, framesAverage, framesCount, curveParam, "");
			}

			/** Last buffer when collection is Sample Buffer Sample **/
			if (dataCollectionOrder == 2) {
				Experiment3VO experiment = experiment3Service.findByMeasurementId(measurementId);

				if (experiment != null) {
					int dataCollectionId = experiment.getDataCollectionByMeasurementId(measurementId).getDataCollectionId();
					String measurementBufferAferId = this.getMeasurementAfter(experiment, measurementId);
					primaryDataProcessing3Service.addMerge(Integer.parseInt(measurementBufferAferId), framesAverage, framesCount, curveParam, "");
					primaryDataProcessing3Service.addSubstraction(dataCollectionId, rg, rgStdev, i0, i0Stdev,
							firstPointUsed, lastPointUsed, quality, isagregated, code, concentration, gnomFile,
							rgGuinier, rgGnom, dmax, total, volume, scatteringFilePath, guinierFilePath,
							kratkyFilePath, densityPlot, this.getSubstractedFilePath(curveParam),
							this.getGnomFilePathOutput(curveParam));
				}
			}

		}
	}

	public void addAveraged(String sampleMeasurementId, String dataCollectionOrder, String averaged, String discarded, String averageFile) {
		this.addAveraged(sampleMeasurementId, dataCollectionOrder, averaged, discarded, averageFile, null);
	}
	
	public void addAveraged(String sampleMeasurementId, String dataCollectionOrder, String averaged, String discarded, String averageFile, String visitorFilePath) {
		String measurementId = sampleMeasurementId;
		/** Buffer before **/
		if (Integer.parseInt(dataCollectionOrder) == 0) {
			Experiment3VO experiment = experiment3Service.findByMeasurementId(Integer.parseInt(sampleMeasurementId));
			measurementId = this.getMeasurementBefore(experiment, Integer.parseInt(sampleMeasurementId));
		}
		
		/** Buffer after **/
		if (Integer.parseInt(dataCollectionOrder) == 2) {
			Experiment3VO experiment = experiment3Service.findByMeasurementId(Integer.parseInt(sampleMeasurementId));
			measurementId = this.getMeasurementAfter(experiment, Integer.parseInt(sampleMeasurementId));
		}
		atsasPipeline3Service.addAveraged(measurementId, averaged, discarded, averageFile, visitorFilePath);
		
	}
	
	
	private String getMeasurementBefore(Experiment3VO experiment, Integer measurementIdParam) {
		if (experiment != null) {
			Integer bufferbefore = experiment.getMeasurementIdBefore(measurementIdParam);
			if (bufferbefore != null) {
				return bufferbefore.toString();
			}
		} else {
			LOG.debug("Not experiment found for measurementId " + measurementIdParam);
		}
		return null;
	}

	private String getMeasurementAfter(Experiment3VO experiment, Integer measurementIdParam) {
		if (experiment != null) {
			Integer bufferbefore = experiment.getMeasurementIdAfter(measurementIdParam);
			if (bufferbefore != null) {
				return bufferbefore.toString();
			}
		} else {
			LOG.debug("Not experiment found for measurementId " + measurementIdParam);
		}
		return null;
	}

	private String getSubstractedFilePath(String curveParam) {
		try {
			List<String> curves = Arrays.asList(curveParam.split(","));
			for (String curve : curves) {
				if (curve.endsWith("_sub.dat")) {
					return curve;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String();
	}

	private String getGnomFilePathOutput(String curveParam) {
		try {
			List<String> curves = Arrays.asList(curveParam.split(","));
			for (String curve : curves) {
				if (curve.endsWith("_sub.out")) {
					return curve;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String();
	}

	/**
	 * @param measurementIds
	 * @param models3vo
	 * @param dammaverModel
	 * @param dammifModel
	 * @param damminModel
	 * @param nsdPlot
	 * @param chi2plot
	 */
	public void addAbinitioModeling(ArrayList<Integer> measurementIds, ArrayList<Model3VO> models3vo,
			Model3VO dammaverModel, Model3VO dammifModel, Model3VO damminModel, String nsdPlot, String chi2plot) {
		this.abInitioModelling3Service.addAbinitioModeling(measurementIds, models3vo, dammaverModel, dammifModel,
				damminModel, nsdPlot, chi2plot);

	}

	/**
	 * @param proposalCode
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> findExperimentInformationByProposal(String proposalCode, String number,
			String experimentType) throws Exception {
		ProposalWS3VO proposal = this.proposalService.findForWSByCodeAndNumber(
				StringUtils.getProposalCode(proposalCode), number);
		if (proposal != null) {
			return this.analysis3Service.getExperimentListByProposalId(proposal.getProposalId(), experimentType);
		}
		return null;
	}

	/**
	 * @param experimentId
	 */
	public String toRobotXML(Integer experimentId) {
		Experiment3VO experiment = this.experiment3Service.findById(experimentId, ExperimentScope.MINIMAL);
		if (experiment == null) {
			return null;
		}
		return this.experiment3Service.toRobotXML(experimentId, experiment.getProposalId(),
				SaxsDataCollectionComparator.defaultComparator);

	}

	/**
	 * For data mining purposes
	 * @return
	 */
	public List<Map<String, Object>> getAnalysisInformation(int limit) {
		return this.abInitioModelling3Service.getAnalysisInformation(limit);
	}

	/**
	 * It creates an Abinitio model object if it doesn't exist yet and link the models to such modeling
	 * 
	 * @param measurementIds
	 * @param models3vo
	 */
	// public void addModelsByMeasurementId(ArrayList<Integer> measurementIds, ArrayList<Model3VO> models3vo) {
	// this.abInitioModelling3Service.addModelsByMeasurementId(measurementIds, models3vo);
	// }

	private Date getNow() {
		Calendar NOW = GregorianCalendar.getInstance();
		return NOW.getTime();
	}

	/**
	 * @param code
	 * @param number
	 * @param h5FilePath
	 * @throws Exception
	 */
	public Integer storeHPLC(String experimentId, String h5FilePath, String jsonFilePath) throws Exception {
		Experiment3VO experiment = this.experiment3Service.findById(Integer.parseInt(experimentId),
				ExperimentScope.MINIMAL);
		LOG.info("storeHPLC: Experiment: " + experiment.toString());
		if (experiment != null) {
			experiment.setDataAcquisitionFilePath(h5FilePath);
			experiment.setSourceFilePath(jsonFilePath);
			experiment.setCreationDate(getNow());
			/** Saving experiment ?!??! **/
			LOG.info("Merging Experiment ");
			experiment = this.experiment3Service.merge(experiment);
			return experiment.getExperimentId();
		}
		return null;

	}

	/**
	 * @param code
	 * @param number
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public Integer createHPLC(String proposalCode, String proposalNumber, String name) throws Exception {
		ProposalWS3VO proposal = proposalService.findForWSByCodeAndNumber(proposalCode, proposalNumber);
		if (proposal != null) {

			Calendar cal = Calendar.getInstance();
			Date today = cal.getTime();

			Integer sessionId = getSessionIdForExperiment(proposal);

			// update the lastUpdate timestamp which will be use to launch the data confidentiality protection
			Session3VO session = sessionService.findByPk(sessionId, false, false, false);
			if (today.after(session.getEndDate())) {
				session.setLastUpdate(today);
				session.setUsedFlag((byte) 1);
				sessionService.update(session);
			}

			Experiment3VO experiment = new Experiment3VO();
			experiment = this.experiment3Service.initPlates(experiment);
			experiment.setProposalId(proposal.getProposalId());
			experiment.setName(name);
			experiment.setSessionId(sessionId);
			experiment.setCreationDate(getNow());
			experiment.setStatus("FINISHED");
			experiment.setType("HPLC");
			experiment = this.experiment3Service.merge(experiment);
			return experiment.getExperimentId();
		}
		return null;
	}

	/**
	 * This method stores averages and subtractions
	 * 
	 * @param experimentId
	 * @param frameStart
	 * @param framesEnd
	 * @param curveParam
	 * @param rg
	 * @param rgStdev
	 * @param i0
	 * @param i0Stdev
	 * @param firstPointUsed
	 * @param lastPointUsed
	 * @param quality
	 * @param isagregated
	 * @param code
	 * @param concentration
	 * @param gnomFile
	 * @param rgGuinier
	 * @param rgGnom
	 * @param dmax
	 * @param total
	 * @param volume
	 * @param scatteringFilePath
	 * @param guinierFilePath
	 * @param kratkyFilePath
	 * @param densityPlot
	 * @param samples
	 * @throws Exception 
	 */
	public Integer saveHPLCCurveAnalysis(Integer experimentId, int frameStart, int framesEnd, String curveParam,
			String rg, String rgStdev, String i0, String i0Stdev, String firstPointUsed, String lastPointUsed,
			String quality, String isagregated, String code, String concentration, String gnomFile, String rgGuinier,
			String rgGnom, String dmax, String total, String volume, String scatteringFilePath, String guinierFilePath,
			String kratkyFilePath, String densityPlot, ArrayList<HashMap<String, String>> samples) throws Exception {

		Experiment3VO experiment = experiment3Service.findById(experimentId, ExperimentScope.MEDIUM);
		SaxsDataCollection3VO dataCollection = null;
		
		if (samples == null || samples.isEmpty()) {
			dataCollection = this.hplcDataProcessing3Service.createDatacollection(experiment,
				"HPLC_M", "HPLC_B", experiment.getProposalId());
		} else {
			Integer proposalId = experiment.getProposalId();
			ArrayList<Macromolecule3VO> macromoleculeList = robotService.createOrUpdateMacromolecule(samples, proposalId);
			ArrayList<Buffer3VO> bufferList = robotService.createOrUpdateBuffer(samples, proposalId);
			dataCollection = this.hplcDataProcessing3Service.createDatacollection(experiment,
					macromoleculeList.get(0).getAcronym(), bufferList.get(0).getAcronym(), experiment.getProposalId());
		}


		LOG.warn("Dc size " + experiment.getDataCollectionList().size());
		Integer measurementId = null;
		if (dataCollection != null) {
			for (MeasurementTodataCollection3VO measurementToDatacollection : dataCollection
					.getMeasurementtodatacollection3VOs()) {
				switch (measurementToDatacollection.getDataCollectionOrder()) {
				case 1:
					LOG.warn("Adding buffer before" + measurementToDatacollection.getMeasurementId());
					primaryDataProcessing3Service.addMerge(measurementToDatacollection.getMeasurementId(), framesEnd,
							frameStart, "", "");
					break;
				case 2:
					LOG.warn("Adding subtraction" + measurementToDatacollection.getMeasurementId());
					measurementId = measurementToDatacollection.getMeasurementId();
					primaryDataProcessing3Service.addMerge(measurementToDatacollection.getMeasurementId(), framesEnd,
							frameStart, "", "");

					break;
				case 3:
					LOG.warn("Adding merge buffer After " + measurementToDatacollection.getMeasurementId());
					primaryDataProcessing3Service.addMerge(measurementToDatacollection.getMeasurementId(), framesEnd,
							frameStart, "", "");
					primaryDataProcessing3Service.addSubstraction(dataCollection.getDataCollectionId(), rg, rgStdev,
							i0, i0Stdev, firstPointUsed, lastPointUsed, quality, isagregated, code, concentration,
							gnomFile, rgGuinier, rgGnom, dmax, total, volume, scatteringFilePath, guinierFilePath,
							kratkyFilePath, densityPlot, this.getSubstractedFilePath(curveParam),
							this.getGnomFilePathOutput(curveParam));
					break;
				default:
					break;
				}

			}
		} else {
			LOG.warn("Datacollection is null");

		}

		return measurementId;
	}

	private Integer getSessionIdForExperiment(ProposalWS3VO proposal) throws Exception {

		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		cal.add(Calendar.HOUR_OF_DAY, 24);
		// by default the session created contains 3 shifts of 8hours each.
		Date endTime = cal.getTime();

		Integer sessionId = null;

		SessionWS3VO[] sessionTab = sessionService.findForWSByProposalCodeAndNumber(proposal.getCode(),
				proposal.getNumber(), Constants.getSAXSBeamline());

		if (sessionTab == null || sessionTab.length == 0) {
			// No session found we have to create one to attach the experiment data to it
			Session3VO ses = new Session3VO();
			ses.setBeamlineName(Constants.getSAXSBeamline());
			ses.setProposalVO(proposalService.findByPk(proposal.getProposalId()));
			ses.setStartDate(today);
			ses.setEndDate(endTime);
			ses.setComments("session created by ISPyBB");
			ses.setScheduled((byte) 0);
			ses.setNbShifts(3);
			ses.setUsedFlag((byte) 1);
			ses.setTimeStamp(today);
			ses.setLastUpdate(endTime);
			ses = sessionService.create(ses);
			sessionId = ses.getSessionId();
		} else {
			sessionId = sessionTab[0].getSessionId();
		}

		return sessionId;
	}
}
