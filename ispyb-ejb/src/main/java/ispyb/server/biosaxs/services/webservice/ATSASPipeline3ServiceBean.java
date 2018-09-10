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

package ispyb.server.biosaxs.services.webservice;

import ispyb.common.util.Constants;
import ispyb.server.biosaxs.services.core.ExperimentScope;
import ispyb.server.biosaxs.services.core.analysis.abInitioModelling.AbInitioModelling3Service;
import ispyb.server.biosaxs.services.core.analysis.advanced.AdvancedAnalysis3Service;
import ispyb.server.biosaxs.services.core.analysis.primaryDataProcessing.PrimaryDataProcessing3Service;
import ispyb.server.biosaxs.services.core.experiment.Experiment3Service;
import ispyb.server.biosaxs.services.core.measurement.Measurement3Service;
import ispyb.server.biosaxs.services.core.measurementToDataCollection.MeasurementToDataCollection3Service;
import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.services.core.robot.Robot3Service;
import ispyb.server.biosaxs.services.core.samplePlate.Sampleplate3Service;
import ispyb.server.biosaxs.services.core.specimen.Specimen3Service;
import ispyb.server.biosaxs.vos.advanced.FitStructureToExperimentalData3VO;
import ispyb.server.biosaxs.vos.advanced.MixtureToStructure;
import ispyb.server.biosaxs.vos.advanced.RigidBodyModeling3VO;
import ispyb.server.biosaxs.vos.advanced.Superposition3VO;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.assembly.Structure3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Additive3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Measurement3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Specimen3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.PlateGroup3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Sampleplate3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Sampleplateposition3VO;
import ispyb.server.biosaxs.vos.datacollection.Framelist3VO;
import ispyb.server.biosaxs.vos.datacollection.MeasurementTodataCollection3VO;
import ispyb.server.biosaxs.vos.datacollection.Merge3VO;
import ispyb.server.biosaxs.vos.datacollection.Model3VO;
import ispyb.server.biosaxs.vos.datacollection.SaxsDataCollection3VO;
import ispyb.server.biosaxs.vos.datacollection.Subtraction3VO;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.LoggerFormatter;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.proposals.ProposalWS3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.collections.SessionWS3VO;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

@Stateless
public class ATSASPipeline3ServiceBean implements ATSASPipeline3Service, DesySampleChangerConnectorServiceLocal {
	private final static Logger LOG = Logger.getLogger("ATSASPipeline3ServiceBean");

	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@EJB
	private Proposal3Service proposalService;

	@EJB
	private SaxsProposal3Service saxsProposal3Service;
	
	@EJB
	private AbInitioModelling3Service AbInitioModelling3Service;

	@EJB
	private Experiment3Service experiment3Service;

	@EJB
	private Measurement3Service measurement3Service;

	@EJB
	private Specimen3Service specimen3Service;
	
	@EJB
	private Sampleplate3Service sampleplate3Service;

	@EJB
	private Robot3Service robot3Service;

	@EJB
	private MeasurementToDataCollection3Service measurementToDataCollection3Service;

	@EJB
	private PrimaryDataProcessing3Service primaryDataProcessing3Service;

	@EJB
	private AdvancedAnalysis3Service advancedAnalysis3Service;

	private long now;

	@EJB
	private Session3Service sessionService;
	
	/** The now. */
	protected static Calendar NOW;

	private Date getNow(){
		 NOW = GregorianCalendar.getInstance();
		 return NOW.getTime();
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
	
	@Override
	public Experiment3VO createEmptyExperiment(String proposalCode, String proposalNumber, String name) throws Exception {
		ProposalWS3VO proposal = proposalService.findForWSByCodeAndNumber(proposalCode, proposalNumber);
		
		Experiment3VO experiment = new Experiment3VO();
		experiment = this.experiment3Service.initPlates(experiment);
		experiment.setName(name);
		experiment.setType("STATIC");
		try{
			experiment.setSessionId(getSessionIdForExperiment(proposal));
		}
		catch(Exception exp){
			/** Non-blocking exception **/
			exp.printStackTrace();
		}
		experiment.setCreationDate(GregorianCalendar.getInstance().getTime());

		
		experiment.setProposalId(proposal.getProposalId());
		experiment = this.experiment3Service.merge(experiment);
		HashMap<String, Sampleplate3VO> plates = robot3Service.getDefaultSampleChangerPlateconfiguration("20");

		PlateGroup3VO plateGroup = new PlateGroup3VO();
		plateGroup.setStorageTemperature("20");
		plateGroup = sampleplate3Service.merge(plateGroup);

		for (String key : plates.keySet()) {
			Sampleplate3VO plate = plates.get(key);
			plate.setExperimentId(experiment.getExperimentId());
			plate.setPlategroup3VO(plateGroup);
			plate = sampleplate3Service.merge(plate);
			sampleplate3Service.merge(plate);
		}
		
		/** Creating a session **/
		Integer sessionId = null;
		
		
		return experiment;
	}

	@Override
	public Measurement3VO appendMeasurementToExperiment(String experimentId, 
			String runNumber, String type, String plate, String row, String well, String name, String bufferName,
			String concentration, String sEUtemperature, String viscosity, String volume, String volumeToLoad, String waitTime, String transmission, String comments) {

		// return experiment3Service.appendMeasurementToExperiment(experimentId,
		// runNumber, type, plate, row, well, name, bufferName, concentration,
		// sEUtemperature, viscosity, volume, volumeToLoad, waitTime,
		// transmission, comments);
		Experiment3VO experiment = experiment3Service.findById(Integer.parseInt(experimentId), ExperimentScope.MEDIUM);
		System.out.println("Getting Experiment:" + experiment.getExperimentId());
		System.out.println("NAME:" + name);
		System.out.println("proposal:" + experiment.getProposalId());

		List<Macromolecule3VO> macromolecules = new ArrayList<Macromolecule3VO>();
		try {
			List<Macromolecule3VO> macromoleculeList = saxsProposal3Service.findMacromoleculesBy(name, experiment.getProposalId());
			System.out.println(macromoleculeList);
			if (macromoleculeList != null) {
				for (Macromolecule3VO macromolecule3vo : macromoleculeList) {
					if (macromolecule3vo.getAcronym() != null) {
						if (macromolecule3vo.getAcronym().toLowerCase().trim().equals(name.toLowerCase().trim())) {
							macromolecules.add(macromolecule3vo);
							System.out.println("Found");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			macromolecules = new ArrayList<Macromolecule3VO>();
		}

		Specimen3VO specimen = new Specimen3VO();

		/**
		 * 
		 * We don't check location of the specimen for ATSAS
		 * http://forge.epn-campus.eu/issues/2095
		 * 
		 */
		Set<Sampleplate3VO> plates = experiment.getSamplePlate3VOs();
		
		
		

		specimen.setConcentration(concentration);
		if (type.toUpperCase().equals("SAMPLE")) {
			Macromolecule3VO macromolecule = null;
			if (macromolecules.size() == 0) {
				if (name == null) {
					name = "";
				}
				Macromolecule3VO m = new Macromolecule3VO(experiment.getProposalId(), name, name, "");
				m.setCreationDate(GregorianCalendar.getInstance().getTime());
				macromolecule = entityManager.merge(m);
			} else {
				macromolecule = macromolecules.get(0);
			}
			specimen.setMacromolecule3VO(macromolecule);
		}

		List<Buffer3VO> buffers = saxsProposal3Service.findBuffersByProposalId(experiment.getProposalId());
		for (Buffer3VO buffer3vo : buffers) {
			if (buffer3vo.getAcronym() != null) {
				if (buffer3vo.getAcronym().equals(bufferName)) {
					specimen.setBufferId(buffer3vo.getBufferId());
				}
			}
		}
		/** If buffer not found **/
		if (specimen.getBufferId() == null) {
			Buffer3VO buffer3VO = new Buffer3VO();
			if (bufferName == null) {
				bufferName = "";
			}
			buffer3VO.setAcronym(bufferName);
			buffer3VO.setName(bufferName);
			buffer3VO.setProposalId(experiment.getProposalId());
			buffer3VO = entityManager.merge(buffer3VO);
			specimen.setBufferId(buffer3VO.getBufferId());
		}

		/** Sample plate position **/
		LOG.info("getSampleplateposition3VO: " + specimen.getSampleplateposition3VO());
		LOG.info("plates: " + plates.size());
		if (specimen.getSampleplateposition3VO() == null) {
			/** this specimen is new because has no position **/
			for (Sampleplate3VO samplePlate : plates) {
				if (samplePlate.getSlotPositionColumn().equals(plate)) {
					Sampleplateposition3VO position = new Sampleplateposition3VO();
					position.setColumnNumber(Integer.parseInt(well));
					position.setRowNumber(Integer.parseInt(row));
					position.setSamplePlateId(samplePlate.getSamplePlateId());
					position = entityManager.merge(position);
					specimen.setSampleplateposition3VO(position);
				}
			}
		}

		specimen.setExperimentId(experiment.getExperimentId());
		specimen.setVolume(volume);

		specimen = entityManager.merge(specimen);

		Measurement3VO measurement = new Measurement3VO();
		measurement.setCode(runNumber);
		measurement.setSpecimenId(specimen.getSpecimenId());
		measurement.setExposureTemperature(sEUtemperature);
		measurement.setTransmission(transmission);
		measurement.setComment(comments);
		measurement.setViscosity(viscosity);
		measurement.setVolumeToLoad(volumeToLoad);
		measurement.setWaitTime(waitTime);
		measurement.setPriority(experiment.getMeasurements().size() + 1);
		measurement = entityManager.merge(measurement);
		return measurement;
	}
	
	public void updateAverage(int measurementId, int averagedCount, int framesCount, String oneDimensionalDataFilePathArray, String discardedCurves, String averageFilePath) {
		List<String> curveList = Arrays.asList(oneDimensionalDataFilePathArray.split(","));
		List<Merge3VO> mergeList = this.primaryDataProcessing3Service.findByMeasurementId(measurementId);
		if (mergeList != null){
			if (mergeList.size() > 0){
				Merge3VO merge = mergeList.get(0);
				if (Integer.parseInt(merge.getFramesCount()) >= framesCount){
					LOG.info("Same number of frames count. No update");
					return;
				}
				else{
					this.primaryDataProcessing3Service.addMerge(measurementId, averagedCount, framesCount, oneDimensionalDataFilePathArray, null, averageFilePath);
				}
			}
		}
	}
	
	@Override
	public void addAveraged(String measurementId, String averaged, String discarded, String averageFile) {
		this.addAveraged(measurementId, averaged, discarded, averageFile, null);
	}
	
	@Override
	public void addAveraged(String measurementId, String averaged, String discarded, String averageFile, String visitorFilePath) {

		Type listType = new TypeToken<List<HashMap<String, String>>>() {
		}.getType();
		List<HashMap<String, String>> averageFrames = new Gson().fromJson(averaged, listType);
		List<HashMap<String, String>> discardeFrames = new Gson().fromJson(discarded, listType);
		

		ArrayList<String> oneDimensionalFiles = new ArrayList<String>();
		LOG.info("########### Averaged Frames ############# ");
		for (HashMap<String, String> frame : averageFrames) {
			if (frame.containsKey("filePath")) {
				oneDimensionalFiles.add(frame.get("filePath"));
				LOG.info("\t" + frame.get("filePath"));
			}
		}
		
		LOG.info("########### Discarded Frames ############# ");
		for (HashMap<String, String> frame : discardeFrames) {
			if (frame.containsKey("filePath")) {
				oneDimensionalFiles.add(frame.get("filePath"));
				LOG.info("\t" + frame.get("filePath"));
			}
		}

		/** Does it contains already a average **/
		List<Merge3VO> merges = primaryDataProcessing3Service.findByMeasurementId(Integer.parseInt(measurementId));
		
		try{
			Measurement3VO measurement = this.measurement3Service.findById(Integer.parseInt(measurementId));
			measurement.setImageDirectory(visitorFilePath);
			this.measurement3Service.merge(measurement);
		}
		catch(Exception exp){
			exp.printStackTrace();
		}
		
		LOG.info("------ Number of Averages by measurementId --------");
		LOG.info(merges.size());

		if (merges.size() == 0) {
			LOG.info("\t Creating a new average");
			/** Not any average found so we create a new one **/
			this.primaryDataProcessing3Service.addMerge(Integer.parseInt(measurementId), averageFrames.size(), oneDimensionalFiles.size(),
					oneDimensionalFiles.toString().replace("[", "").replace("]", ""), null, averageFile);
		} else {
			LOG.info("\t Updating a new average");
			/** It is an update of the average **/
			
			this.updateAverage(Integer.parseInt(measurementId), averageFrames.size(), oneDimensionalFiles.size(),
					oneDimensionalFiles.toString().replace("[", "").replace("]", ""), null, averageFile);
			
		}
}
//	private void removeFrameListById(int frameListId) {
//		Framelist3VO frameList = this.entityManager.find(Framelist3VO.class, frameListId);
//		/** Looking for FrameToListObjects **/
//		
//		String query = "select f from Frametolist3VO f where f.frameListId=:frameListId";
//		Query EJBQuery = this.entityManager.createQuery(query).setParameter("frameListId", frameListId);
//		List<Frametolist3VO> frameToListList =  (List<Frametolist3VO>)EJBQuery.getResultList();
//		for (Frametolist3VO frametolist3vo : frameToListList) {
//			System.out.println("Found " + frametolist3vo.getFrameToListId());
//		}
//		
//	}

	/**
	 * True if a datacollection contains all the measurementIds otherwise false
	 * 
	 * @param dataCollection
	 * @param measurementIds
	 * @return
	 */
	private Boolean containsSameMeasurements(SaxsDataCollection3VO dataCollection, List<Integer> measurementIds) {
		Iterator<MeasurementTodataCollection3VO> mIterator = dataCollection.getMeasurementtodatacollection3VOs().iterator();
		HashSet<Integer> measurementDataCollectionSet = new HashSet<Integer>();
		while (mIterator.hasNext()) {
			MeasurementTodataCollection3VO measurementToDataCollection = mIterator.next();
			measurementDataCollectionSet.add(measurementToDataCollection.getMeasurementId());
		}
		for (Integer measurementId : measurementIds) {
			if (!measurementDataCollectionSet.contains(measurementId)) {
				return false;
			}
		}
		return true;
	}

	private SaxsDataCollection3VO getDataCollectionByMeasurementsId(Experiment3VO experiment, List<Integer> measurementIds) {
		Iterator<SaxsDataCollection3VO> iterator = experiment.getDataCollections().iterator();
		while (iterator.hasNext()) {
			SaxsDataCollection3VO dataCollection = iterator.next();
			if (this.containsSameMeasurements(dataCollection, measurementIds)) {
				/**
				 * Returning the existing one
				 */
				return dataCollection;
			}
		}
		return null;
	}
	/**
	 * Returns the measurement id corresponding to a sample or null if all measurementIds are buffers
	 * @param experiment
	 * @param measurementIds
	 * @return
	 */
	private Integer getSampleFromMeasurementList(Experiment3VO experiment, List<Integer> measurementIds) {
		for (Integer measurementId : measurementIds) {
			Specimen3VO specimen = experiment.getSampleById(experiment.getMeasurementById(measurementId).getSpecimenId());
			if (specimen.getMacromolecule3VO() != null){
				return measurementId;
			}
		}
		return null;
	}
	
	/**
	 * return a existing datacollection if found containing the same
	 * measurements or create a new one
	 * 
	 * @param experiment
	 * @param measurementIds
	 * @return
	 */
	private SaxsDataCollection3VO getSaxsDataCollectionByMeasurementIds(Experiment3VO experiment, List<Integer> measurementIds) {
//		SaxsDataCollection3VO datacollection = this.getDataCollectionByMeasurementsId(experiment, measurementIds);
		SaxsDataCollection3VO datacollection = null;
		LOG.info("Getting data collection");
		LOG.info("Getting data collection: " + measurementIds.toString());
		Integer sampleMeasurementId = this.getSampleFromMeasurementList(experiment, measurementIds);
		LOG.info("sampleMeasurementId: " + sampleMeasurementId);
		if (sampleMeasurementId == null){
			datacollection = this.getDataCollectionByMeasurementsId(experiment, measurementIds);
		}
		else{
			/** Look for a subtraction already containing this sample **/
			datacollection = experiment.getDataCollectionByMeasurementId(sampleMeasurementId);
			
			if (datacollection != null){
				/** Adding extra measurements **/
				Set<MeasurementTodataCollection3VO> measurementToDatacollectionList = datacollection.getMeasurementtodatacollection3VOs();
				Set<Integer> measurementKeys = new HashSet<Integer>();
				for (MeasurementTodataCollection3VO measurementToDatacollection : measurementToDatacollectionList) {
					measurementKeys.add(measurementToDatacollection.getMeasurementId());
				}
				LOG.info("measurementKeys:" + measurementKeys.toString());
				for (Integer measurementId : measurementIds) {
					LOG.info("measurementKeys:" + measurementId + " " + measurementKeys.toString());
					if(!measurementKeys.contains(measurementId)){
						/** This measurement does not belong to the data collection yet **/
						MeasurementTodataCollection3VO measurementToDataCollection = new MeasurementTodataCollection3VO();
						measurementToDataCollection.setDataCollectionId(datacollection.getDataCollectionId());
						measurementToDataCollection.setMeasurementId(measurementId);
						measurementToDataCollection.setDataCollectionOrder(datacollection.getMeasurementtodatacollection3VOs().size() + 1);
						LOG.info("setDataCollectionOrder:" + datacollection.getMeasurementtodatacollection3VOs().size() + 1);
						this.entityManager.merge(measurementToDataCollection);
					}
				}
			}
		}
		
		if (datacollection != null) {
			return datacollection;
		} else {
			/** We create a new one **/
			SaxsDataCollection3VO dataCollection = new SaxsDataCollection3VO();
			dataCollection.setExperimentId(experiment.getExperimentId());
			dataCollection = entityManager.merge(dataCollection);
			int dataCollectionOrder = 1;
			for (Integer measurementId : measurementIds) {
				MeasurementTodataCollection3VO measurementToDataCollection = new MeasurementTodataCollection3VO();
				measurementToDataCollection.setDataCollectionId(dataCollection.getDataCollectionId());
				measurementToDataCollection.setMeasurementId(measurementId);
				measurementToDataCollection.setDataCollectionOrder(dataCollectionOrder);
				this.entityManager.merge(measurementToDataCollection);
				dataCollectionOrder++;
			}
			return dataCollection;
		}
	}

	private Framelist3VO createFrames(List<String> sampleOneDimensionalFilesList){
		if (sampleOneDimensionalFilesList.size() > 0){
			return this.primaryDataProcessing3Service.addFrameList(sampleOneDimensionalFilesList);
		}
		return null;
	}
	
	private void addSubtraction(Integer dataCollectionId, String rgStdev, String i0, String i0Stdev, String firstPointUsed,
			String lastPointUsed, String quality, String isagregated, String rgGuinier, String rgGnom, String dmax, String total,
			String volume, String sampleOneDimensionalFiles, String bufferOneDimensionalFiles, String sampleAverageFilePath,
			String bestBufferFilePath, String subtractedFilePath, String experimentalDataPlotFilePath, String densityPlotFilePath,
			String guinierPlotFilePath, String kratkyPlotFilePath, String gnomOutputFilePath, Subtraction3VO subtraction) {
		
		HashMap<String, String> params = new HashMap<String, String>();
		
		long start = this.logInit("addSubtraction_CP_3.1", new Gson().toJson(params));

		/** Getting the frames **/
		Type typeOfHash = new TypeToken<List<HashMap<String, String>>>() {}.getType();
		List<HashMap<String, String>> sampleOneDimensionalFilesListObject = new Gson().fromJson(sampleOneDimensionalFiles, typeOfHash);
		List<HashMap<String, String>> bufferOneDimensionalFilesListObject = new Gson().fromJson(bufferOneDimensionalFiles, typeOfHash);
		
		List<String> sampleOneDimensionalFilesList = new ArrayList<String>();
		List<String> bufferOneDimensionalFilesList = new ArrayList<String>();
		
		for (HashMap<String, String> fileObject : sampleOneDimensionalFilesListObject) {
			if (fileObject.get("filePath")!= null){
				sampleOneDimensionalFilesList.add(fileObject.get("filePath"));
			}
		}
		
		for (HashMap<String, String> fileObject : bufferOneDimensionalFilesListObject) {
			if (fileObject.get("filePath")!= null){
				bufferOneDimensionalFilesList.add(fileObject.get("filePath"));
			}
		}
		
		LOG.info(sampleOneDimensionalFilesList);
		Framelist3VO sampleFiles = this.createFrames(sampleOneDimensionalFilesList);
		this.logFinish("addSubtraction_CP_3.1", start);
		
		start = this.logInit("addSubtraction_CP_3.2", new Gson().toJson(params));
		Framelist3VO bufferFiles = this.createFrames(bufferOneDimensionalFilesList);
		this.logFinish("addSubtraction_CP_3.2", start);
		
		
		start = this.logInit("addSubtraction_CP_3.3", new Gson().toJson(params));
		subtraction.setDataCollectionId(dataCollectionId);
		subtraction.setRg(rgGuinier);
		subtraction.setRgStdev(rgStdev);
		subtraction.setI0(i0);
		subtraction.setI0stdev(i0Stdev);
		subtraction.setFirstPointUsed(firstPointUsed);
		subtraction.setLastPointUsed(lastPointUsed);
		subtraction.setQuality(quality);
		subtraction.setIsagregated(isagregated);
		subtraction.setGnomFilePath(densityPlotFilePath);
		subtraction.setRgGuinier(rgGuinier);
		subtraction.setRgGnom(rgGnom);
		subtraction.setDmax(dmax);
		subtraction.setTotal(total);
		subtraction.setVolume(volume);
		subtraction.setKratkyFilePath(kratkyPlotFilePath);
		subtraction.setScatteringFilePath(experimentalDataPlotFilePath);
		subtraction.setGuinierFilePath(guinierPlotFilePath);
		subtraction.setSubstractedFilePath(subtractedFilePath);
		subtraction.setGnomFilePathOutput(gnomOutputFilePath);
		subtraction.setCreationTime(getNow());
		
		if (sampleFiles != null){
			subtraction.setSampleOneDimensionalFiles(sampleFiles);
		}
		if (bufferFiles != null){
			subtraction.setBufferOneDimensionalFiles(bufferFiles);
		}
		subtraction.setSampleAverageFilePath(sampleAverageFilePath);
		subtraction.setBufferAverageFilePath(bestBufferFilePath);
		
		this.entityManager.merge(subtraction);
		this.logFinish("addSubtraction_CP_3.3", start);
		
	}
	
	private void addSubtraction(int dataCollectionId, String rgStdev, String i0, String i0Stdev, String firstPointUsed,
			String lastPointUsed, String quality, String isagregated, String rgGuinier, String rgGnom, String dmax, String total,
			String volume, String sampleOneDimensionalFiles, String bufferOneDimensionalFiles, String sampleAverageFilePath,
			String bestBufferFilePath, String subtractedFilePath, String experimentalDataPlotFilePath, String densityPlotFilePath,
			String guinierPlotFilePath, String kratkyPlotFilePath, String gnomOutputFilePath) {
		
		Subtraction3VO subtraction = new Subtraction3VO();
		this.addSubtraction(dataCollectionId, rgStdev, i0, i0Stdev, firstPointUsed, lastPointUsed, quality, isagregated, rgGuinier, rgGnom, dmax, total, volume, sampleOneDimensionalFiles, bufferOneDimensionalFiles, sampleAverageFilePath, bestBufferFilePath, subtractedFilePath, experimentalDataPlotFilePath, densityPlotFilePath, guinierPlotFilePath, kratkyPlotFilePath, gnomOutputFilePath, subtraction);
		
	}
	
	
	@Override
	public void addSubtraction(String sampleMeasurementId, String rgStdev, String i0, String i0Stdev, String firstPointUsed,
			String lastPointUsed, String quality, String isagregated, String rgGuinier, String rgGnom, String dmax, String total,
			String volume, String sampleOneDimensionalFiles, String bufferOneDimensionalFiles, String sampleAverageFilePath,
			String bestBufferFilePath, String subtractedFilePath, String experimentalDataPlotFilePath, String densityPlotFilePath,
			String guinierPlotFilePath, String kratkyPlotFilePath, String gnomOutputFilePath) {
		


		HashMap<String, String> params = new HashMap<String, String>();
		long start = this.logInit("addSubtraction_CP_1", new Gson().toJson(params));
		if (sampleMeasurementId != null){
			// TODO: To be changed, performace problems!!!
			Experiment3VO experiment = experiment3Service.findByMeasurementId(Integer.parseInt(sampleMeasurementId));
			this.logFinish("addSubtraction_CP_1", start);
			
			
			if (experiment != null) {
				start = this.logInit("addSubtraction_CP_2", new Gson().toJson(params));
				int dataCollectionId = experiment.getDataCollectionByMeasurementId(Integer.parseInt(sampleMeasurementId)).getDataCollectionId();
				this.logFinish("addSubtraction_CP_2", start);
				
				
				start = this.logInit("addSubtraction_CP_3", new Gson().toJson(params));
				this.addSubtraction(dataCollectionId, rgStdev, i0, i0Stdev, firstPointUsed, lastPointUsed, quality, isagregated, rgGuinier, rgGnom, dmax, total, volume, sampleOneDimensionalFiles, bufferOneDimensionalFiles, sampleAverageFilePath, bestBufferFilePath, subtractedFilePath, experimentalDataPlotFilePath, densityPlotFilePath, guinierPlotFilePath, kratkyPlotFilePath, gnomOutputFilePath);
				this.logFinish("addSubtraction_CP_3", start);
				
			}
		}
	}
	
	
	/**
	 * Adds a new subtraction to several measurements To note: there are not any data collection yet
	 */
	public void addSubtraction(
			String experimentId, 
			String runNumberList, 
			String rgStdev, 
			String i0, 
			String i0Stdev, 
			String firstPointUsed, 
			String lastPointUsed, 
			String quality,
			String isagregated, 
			String rgGuinier, 
			String rgGnom, 
			String dmax, 
			String total, 
			String volume, 
			String sampleOneDimensionalFiles,
			String bufferOneDimensionalFiles, 
			String sampleAverageFilePath, 
			String bestBufferFilePath, 
			String subtractedFilePath, 
			String experimentalDataPlotFilePath,
			String densityPlotFilePath, 
			String guinierPlotFilePath, 
			String kratkyPlotFilePath, 
			String gnomOutputFilePath) throws Exception {

		Type typeOfList = new TypeToken<List<String>>() {}.getType();
		List<String> ids = new Gson().fromJson(runNumberList, typeOfList);

		/** Finding measurement by experimentId and run number **/
		List<Integer> measurementIds = new ArrayList<Integer>();
		for (String runNumber : ids) {
			Measurement3VO measurement = measurement3Service.findMeasurementByCode(Integer.parseInt(experimentId), runNumber.toString());
			if (measurement != null) {
				measurementIds.add(measurement.getMeasurementId());
			} else {
				throw new Exception("No measurement found for runNumber: " + runNumber + " and experimentId: " + experimentId);
			}
		}

		System.out.println("Adding data collection and subtraction");

		/** Adding data collection and subtraction **/
		if (measurementIds != null) {
			if (measurementIds.size() > 0) {
				Experiment3VO experiment = experiment3Service.findById(Integer.parseInt(experimentId), ExperimentScope.MEDIUM);
				if (experiment != null) {
					/** This method creates the data collection if it does not exist yet **/
					SaxsDataCollection3VO dataCollection = getSaxsDataCollectionByMeasurementIds(experiment, measurementIds);
					
					if (dataCollection.getSubstraction3VOs().size() == 0){
						this.addSubtraction(dataCollection.getDataCollectionId(), rgStdev, i0, i0Stdev, firstPointUsed, lastPointUsed, quality, isagregated, rgGuinier, rgGnom, dmax, total, volume, sampleOneDimensionalFiles, bufferOneDimensionalFiles, sampleAverageFilePath, bestBufferFilePath, subtractedFilePath, experimentalDataPlotFilePath, densityPlotFilePath, guinierPlotFilePath, kratkyPlotFilePath, gnomOutputFilePath);
					}
					else{
						/**
						 * Upgrading subtraction
						 */
						Subtraction3VO subtraction = (Subtraction3VO) Arrays.asList(dataCollection.getSubstraction3VOs().toArray()).get(0);
						this.addSubtraction(dataCollection.getDataCollectionId(), rgStdev, i0, i0Stdev, firstPointUsed, lastPointUsed, quality, isagregated, rgGuinier, rgGnom, dmax, total, volume, sampleOneDimensionalFiles, bufferOneDimensionalFiles, sampleAverageFilePath, bestBufferFilePath, subtractedFilePath, experimentalDataPlotFilePath, densityPlotFilePath, guinierPlotFilePath, kratkyPlotFilePath, gnomOutputFilePath, subtraction);
					}
				}
			}
		}
	}

	

	@Override
	public void addAbinitioModelling(String experimentId, String runNumber,
			ArrayList<Model3VO> models3vo, Model3VO referenceModel,
			Model3VO refinedModel) throws Exception {
		
		Measurement3VO measurement = measurement3Service.findMeasurementByCode(Integer.parseInt(experimentId), (runNumber));
		if (measurement != null){
			ArrayList<Integer> measurementIds = new ArrayList<Integer>();
			measurementIds.add(measurement.getMeasurementId());
			AbInitioModelling3Service.addAbinitioModeling(measurementIds, 
																models3vo, 
																referenceModel, 
																refinedModel); 
			
		}
		else{
			throw new Exception("No measurement found for " + experimentId + " and " + runNumber);
		}
	}

	@Override
	public Model3VO getModel(String experimentId, String runNumber, String pdbfilepath) throws Exception {
		Measurement3VO measurement = measurement3Service.findMeasurementByCode(Integer.parseInt(experimentId), (runNumber));
		if (measurement != null){
			return AbInitioModelling3Service.getModel(measurement.getMeasurementId(), pdbfilepath);
		}
		else{
			throw new Exception("Not measurement found for " + experimentId + " and " + runNumber);
		}
	}

	@Override
	public Model3VO merge(Model3VO model) {
		return this.entityManager.merge(model);
	}

	@Override
	public List<Macromolecule3VO>  getMacromoleculeByAcronym(String proposal, String acronym) {
		List<Proposal3VO> proposal3VOs = this.proposalService.findProposalByLoginName(proposal);
		if (proposal3VOs != null){
			if (proposal3VOs.size() > 0){
				Proposal3VO proposal3VO = proposal3VOs.get(0);
				List<Macromolecule3VO> macromolecules = this.saxsProposal3Service.findMacromoleculesBy(acronym, proposal3VO.getProposalId());
				return macromolecules;
			}
		}
		return new ArrayList<Macromolecule3VO>();
	}

	public List<Subtraction3VO> getSubtractionByCode(String experimentId, String runNumber) {
		Measurement3VO measurement = measurement3Service.findMeasurementByCode(Integer.valueOf(experimentId), runNumber);
		System.out.println("measurement " + measurement);
		if (measurement != null){
			return primaryDataProcessing3Service.getSubstractionsByMeasurementId(measurement.getMeasurementId());
		}
		System.out.println("measurement not found ");
		return null;
	}
	
	public Macromolecule3VO getMacromoleculeByCode(String experimentId, String runNumber) {
		Measurement3VO measurement = measurement3Service.findMeasurementByCode(Integer.valueOf(experimentId), runNumber);
		if (measurement != null){
			Specimen3VO specimen = entityManager.find(Specimen3VO.class, measurement.getSpecimenId());
			if (specimen != null){
				return saxsProposal3Service.findMacromoleculesById(specimen.getMacromolecule3VO().getMacromoleculeId());
//				return experiment3Service.findMacromoleculeById(specimen.getMacromolecule3VO().getMacromoleculeId());
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param experimentId
	 * @param runNumber
	 * @param filePath {filePath:"/filepath.dat"}
	 * @return
	 */
	public Structure3VO getStructuresByCode(String experimentId, String runNumber, String filePath) {
		Macromolecule3VO macromolecule = this.getMacromoleculeByCode(experimentId, runNumber);
		for (Structure3VO structure : macromolecule.getStructure3VOs()) {
			if (structure.getFilePath().trim().equals(filePath.trim())){
				return structure;
			}
		}
		
		return null;
	}
	
	@Override
	public void addMixture(String experimentId, String runNumber, String fitFilePath, String pdb) throws Exception {
		List<Subtraction3VO> subtractions = this.getSubtractionByCode(experimentId, runNumber);
		System.out.println("+++addMixture " + experimentId + " " + runNumber);
		if (subtractions != null){
			if (subtractions.size() > 0){
				/** Getting last one are they should come by order **/
				Subtraction3VO subtraction = subtractions.get(subtractions.size() - 1);
				
				System.out.println("+++Subtraction Found");
				/** Getting Structure **/
				
				Type ListType = new TypeToken<List<HashMap<String, String>>>() {}.getType();
				
				/** Reading pdbs **/
				List<HashMap<String, String>> pdbFilePath = new Gson().fromJson(pdb, ListType);
				
				List<MixtureToStructure> mixtures = new ArrayList<MixtureToStructure>();
				if (pdbFilePath.size() > 0){
					for (HashMap<String, String> pdbHash : pdbFilePath) {
						MixtureToStructure mixture = new MixtureToStructure();
						
						if (pdbHash.containsKey("filePath")){
							/** Finding the structue linked to such filePath */
							Structure3VO structure = this.getStructuresByCode(experimentId, runNumber, pdbHash.get("filePath"));
							/** If structure does not exist we create a new one **/
							if (structure == null){
								structure = new Structure3VO();
								Macromolecule3VO macromolecule = this.getMacromoleculeByCode(experimentId, runNumber);
								System.out.println("Macromolecule found");
								if (macromolecule != null){
									structure.setMacromoleculeId(macromolecule.getMacromoleculeId());
									structure.setFilePath(pdbHash.get("filePath"));
									if (pdbHash.get("filePath") != null){
										String name = new File(pdbHash.get("filePath")).getName();
										structure.setName(name);
									}
									structure.setType("PDB");
									structure.setCreationDate(getNow());
									structure = this.entityManager.merge(structure);
									System.out.println("Structure created");
								}
								else{
									throw new Exception("Macromolecule not found for experimentId " + experimentId + " and runNumber " + runNumber );
								}
							} 
							
							/** Structure exists here **/
							mixture.setStructureId(structure.getStructureId());
							mixture.setCreationDate(getNow());
							
						}
						
						if (pdbHash.containsKey("volumeFraction")){
							System.out.println("+++" +"Volume: "  + pdbHash.get("volumeFraction"));
							mixture.setVolumeFraction(pdbHash.get("volumeFraction"));
						}
						
						mixtures.add(mixture);
					}
					
					System.out.println( "+++Mixtures --- " + mixtures.size());
					System.out.println("+++" +fitFilePath + " --- " + fitFilePath);
					
					
					/** Creating FitStructureToExperimentalData **/
					FitStructureToExperimentalData3VO fit = new FitStructureToExperimentalData3VO();
					fit.setSubtractionId(subtraction.getSubtractionId());
					fit.setCreationDate(getNow());
					fit.setFitFilePath(fitFilePath);
					fit = entityManager.merge(fit);
					System.out.println("+++Merging fit " + fit.getFitFilePath() + " " + fit.getFitStructureToExperimentalDataId());
					
					/** Creating MixtureToStructures **/
					for (MixtureToStructure mixtureToStructure : mixtures) {
						mixtureToStructure.setMixtureId(fit.getFitStructureToExperimentalDataId());
						mixtureToStructure = this.entityManager.merge(mixtureToStructure);
						System.out.println("+++Merging mixturetoStructure " + mixtureToStructure.getVolumeFraction() + " " + mixtureToStructure.getStructureId() + " " + mixtureToStructure.getFitToStructureId());
					}
				}
				
				/** Reading fitFilePath **/
//				Type HashType = new TypeToken<HashMap<String, String>>() {}.getType();
//				HashMap<String, String> fitFilePathHash = new Gson().fromJson(fitFilePath, HashType);
//				if (fitFilePathHash.containsKey("filePath")){
//					fitFilePath = fitFilePathHash.get("filePath");
//				}
				
				
			}
			else{
				throw new Exception("0 subtractions has been found for experimentId= " + experimentId + " and runNumber= " + runNumber);
			}
			
		}
		else{
			throw new Exception("No subtraction has been found for experimentId= " + experimentId + " and runNumber= " + runNumber);
		}
//		if (fitStructure == null){
//			fitStructure = new FitStructureToExperimentalData();
//		}
//		
//		fitStructure.setFitFilePath(fitFilePath);
////		fitStructure.setLogFilePath(logFile);
//		fitStructure.setOutputFilePath(summaryFile);
////		fitStructure.setStructureId(Integer.parseInt(structureId));
//		fitStructure.setSubtractionId(Integer.parseInt(subtractionId));
//		fitStructure.setWorkflowId(Integer.parseInt(workflowId));
//		fitStructure.setComments("Finished at " + getNow());
//		fitStructure = advancedAnalysis3Service.merge(fitStructure);
		
	}

	@Override
	public void addRigidBodyModeling(String experimentId, String runNumber, String fitFilePath, String rigidBodyModelFilePath,
			String logFilePath, String curveConfigFilePath, String subunitConfigFilePath, String crosscorrConfigFilePath,
			String contactConditionsFilePath, String masterSymmetry) throws Exception {
		
		List<Subtraction3VO> subtractions = this.getSubtractionByCode(experimentId, runNumber);
		if (subtractions != null){
			if (subtractions.size() > 0){
				RigidBodyModeling3VO rigid = new RigidBodyModeling3VO();
				rigid.setSubtractionId(subtractions.get(subtractions.size() - 1).getSubtractionId());
				rigid.setFitFilePath(fitFilePath);
				rigid.setRigidBodyModelFilePath(rigidBodyModelFilePath);
				rigid.setLogFilePath(logFilePath);
				rigid.setCurveConfigFilePath(curveConfigFilePath);
				rigid.setSubUnitConfigFilePath(subunitConfigFilePath);
				rigid.setCrossCorrConfigFilePath(crosscorrConfigFilePath);
				rigid.setContactDescriptionFilePath(contactConditionsFilePath);
				rigid.setSymmetry(masterSymmetry);
				rigid.setCreationDate(getNow());
				this.entityManager.merge(rigid);
			}
			else{
				throw new Exception("No subtraction found for " + experimentId + " " + runNumber);
			}
		}
		else{
			throw new Exception("No subtraction found for " + experimentId + " " + runNumber + "(null)");
		}
	}

	@Override
	public void addSuperposition(String experimentId, String runNumber, String abinitioModelPdbFilePath, String aprioriPdbFilePath,
			String alignedPdbFilePath) throws Exception {
		
		List<Subtraction3VO> subtractions = this.getSubtractionByCode(experimentId, runNumber);
		if (subtractions != null){
				Superposition3VO superposition = new Superposition3VO();
				superposition.setSubtractionId(subtractions.get(subtractions.size() - 1).getSubtractionId());
				superposition.setAbinitioModelPdbFilePath(abinitioModelPdbFilePath);
				superposition.setAprioriPdbFilePath(aprioriPdbFilePath);
				superposition.setAlignedPdbFilePath(alignedPdbFilePath);
				superposition.setCreationDate(getNow());
				this.entityManager.merge(superposition);
		}
		else{
			throw new Exception("No subtraction found for " + experimentId + " " + runNumber + "(null)");
		}
		
		
	}

	@Override
	public HashMap<String, Object> getAprioriInformationByRunNumber(String proposal, String acronym) throws Exception {
		HashMap<String, Object> info = new HashMap<String, Object>();
		List<Macromolecule3VO> macromolecules = this.getMacromoleculeByAcronym(proposal, acronym);
		if (macromolecules.size() == 1){
			Macromolecule3VO macromolecule = macromolecules.get(0);
			if (macromolecule != null){
						info.put("ACRONYM", macromolecule.getAcronym());
						List<HashMap<String, String>> pdb = new ArrayList<HashMap<String, String>>();
						List<HashMap<String, String>> fasta = new ArrayList<HashMap<String, String>>();
						List<HashMap<String, String>> rbms = new ArrayList<HashMap<String, String>>();
						if (macromolecule.getStructure3VOs() != null){
							for (Structure3VO structure : macromolecule.getStructure3VOs()) {
								if (structure.getType().equals("PDB")){
									HashMap<String, String> pdbEntry = new HashMap<String, String>();
									pdbEntry.put("FILEPATH", structure.getFilePath());
									pdbEntry.put("SYMMETRY", structure.getSymmetry());
									pdbEntry.put("MULTIPLICITY", structure.getMultiplicity());
									pdb.add(pdbEntry);
								}
								if (structure.getType().equals("SEQUENCE")){
									HashMap<String, String> fastaEntry = new HashMap<String, String>();
									fastaEntry.put("FILEPATH", structure.getFilePath());
									fasta.add(fastaEntry);
								}
							}
						}
						
						HashMap<String, String> rbm = new HashMap<String, String>();
						rbm.put("FILEPATH", macromolecule.getContactsDescriptionFilePath());
						rbm.put("SYMMETRY", macromolecule.getSymmetry());
						rbms.add(rbm);
						
						info.put("PDB", pdb);
						info.put("SEQUENCE", fasta);
						info.put("RIGIDBODY", rbms);
						return info;
			}
			else{
				throw new Exception("macromolecule not found");
			}
		}
		else{
			throw new Exception(macromolecules.size() + " macromolecules found");
		}
	}
	
	
	private void logFinish(String methodName, long id) {
		LOG.debug("### [" + methodName.toUpperCase() + "] Execution time was " + (System.currentTimeMillis() - this.now) + " ms.");
		LoggerFormatter.log(LOG, LoggerFormatter.Package.BIOSAXS_WS, methodName, id, System.currentTimeMillis(),
				System.currentTimeMillis() - this.now);

	}

	protected long logInit(String methodName, String params) {
		LOG.info("-----------------------");
		this.now = System.currentTimeMillis();
		LOG.info(methodName.toUpperCase());
		LoggerFormatter.log(LOG, LoggerFormatter.Package.BIOSAXS_WS, methodName, System.currentTimeMillis(),
				System.currentTimeMillis(), params);
		return this.now;
	}

	@Override
	public List<Macromolecule3VO> getMacromoleculesByProposal(String code, String number) throws Exception {
		ProposalWS3VO proposal = proposalService.findForWSByCodeAndNumber(code, number);
		return this.saxsProposal3Service.findMacromoleculesByProposalId(proposal.getProposalId());
	}
	
	@Override
	public List<Buffer3VO> getBuffersByProposal(String code, String number) throws Exception {
		ProposalWS3VO proposal = proposalService.findForWSByCodeAndNumber(code, number);
		return this.saxsProposal3Service.findBuffersByProposalId(proposal.getProposalId());
	}
	
	@Override
	public List<Additive3VO> getBufferAdditives(String code, String number, String buffer) throws Exception {
		ProposalWS3VO proposal = proposalService.findForWSByCodeAndNumber(code, number);
		List<Additive3VO> additives = new ArrayList<Additive3VO>();
		additives = saxsProposal3Service.findAdditivesByBufferId(Integer.parseInt(buffer));
		return additives;
		//return this.saxsProposal3Service.findBuffersByProposalId(proposal.getProposalId());
	}
}
