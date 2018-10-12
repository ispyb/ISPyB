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

package ispyb.server.biosaxs.services.core.robot;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import com.google.gson.GsonBuilder;

import ispyb.common.util.Constants;
import ispyb.server.biosaxs.services.core.ExperimentScope;
import ispyb.server.biosaxs.services.core.experiment.Experiment3ServiceLocal;
import ispyb.server.biosaxs.services.core.measurement.Measurement3ServiceLocal;
import ispyb.server.biosaxs.services.core.measurementToDataCollection.MeasurementToDatacollection3ServiceLocal;
import ispyb.server.biosaxs.services.core.plateType.PlateType3ServiceLocal;
import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3ServiceLocal;
import ispyb.server.biosaxs.services.core.samplePlate.Sampleplate3ServiceLocal;
import ispyb.server.biosaxs.services.core.specimen.Specimen3Service;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Measurement3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Specimen3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.PlateGroup3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Platetype3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Sampleplate3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Sampleplateposition3VO;
import ispyb.server.biosaxs.vos.datacollection.MeasurementTodataCollection3VO;
import ispyb.server.biosaxs.vos.datacollection.SaxsDataCollection3VO;

@Stateless
public class Robot3ServiceBean implements Robot3Service, Robot3ServiceLocal {

	private final static Logger log = Logger.getLogger(Robot3ServiceBean.class);

	/** The specimen3 service local. */
	@EJB
	private Measurement3ServiceLocal measurement3ServiceLocal;

	/** The experiment3 service local. */
	@EJB
	private Experiment3ServiceLocal experiment3ServiceLocal;

	/** The macromolecule3 service. */
	@EJB
	private SaxsProposal3ServiceLocal proposal3Service;

	/** The sample plate3 service. */
	@EJB
	private Sampleplate3ServiceLocal samplePlate3Service;

	/** The plate type3 service. */
	@EJB
	private PlateType3ServiceLocal plateType3Service;

	/** The measurement to data collection3 service. */
	@EJB
	private MeasurementToDatacollection3ServiceLocal measurementToDataCollection3Service;

	@EJB
	private Specimen3Service specimen3Service;

	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	/** The now. */
	protected static Calendar NOW;

	/**
	 * Gets the now.
	 * 
	 * @return the now
	 */
	private Date getNow() {
		NOW = GregorianCalendar.getInstance();
		return NOW.getTime();
	}

	/**
	 * Gets the buffers. It returns a hashmap <bufferName, buffer3VO>
	 * 
	 * @param samples
	 *            the samples
	 * @return the buffers
	 */
	private HashMap<String, Buffer3VO> parseBuffers(ArrayList<HashMap<String, String>> samples, int proposalId) {
		List<Buffer3VO> buffersProposal = proposal3Service.findBuffersByProposalId(proposalId);
		Robot3ServiceBean.log.info("----------------- parseBuffers ------------ ");
		HashMap<String, Buffer3VO> bufferNameToBuffer3VO = new HashMap<String, Buffer3VO>();

		/** Store the name of the macromolecules <BufferName, Acronym> **/
		HashMap<String, String> bufferNameToBufferAcronym = new HashMap<String, String>();

		/**
		 * It is possible that users make a mistake and for the same specimen in the same position they set a different
		 * buffer's name
		 */
		HashMap<String, String> bufferPositionToBufferAcronym = new HashMap<String, String>();

		for (HashMap<String, String> sample : samples) {
			if (sample.get("type").equals("Buffer")) {
				Buffer3VO buffer = new Buffer3VO();
				buffer.setName(sample.get("macromolecule").trim());
				buffer.setAcronym(sample.get("macromolecule").trim());
				buffer.setComment(sample.get("comments"));

				Robot3ServiceBean.log.info("sample: " + sample.toString());
				Robot3ServiceBean.log.info("buffername: " + sample.get("buffername"));

				String positionCode = this.getSamplePositionKey(sample);
				if (bufferPositionToBufferAcronym.get(positionCode) == null) {
					bufferNameToBufferAcronym.put(sample.get("buffername").trim(), sample.get("macromolecule").trim());
					bufferPositionToBufferAcronym.put(positionCode, sample.get("macromolecule").trim());
				} else {
					Robot3ServiceBean.log.info("Buffers detected in the same position with different macromolecule name");
					log.warn("Buffers detected in the same position with different macromolecule name");
					bufferNameToBufferAcronym.put(sample.get("buffername").trim(),
							bufferPositionToBufferAcronym.get(positionCode));
				}
			}
		}

		for (String bufferName : bufferNameToBufferAcronym.keySet()) {
			Buffer3VO bufferExists = this
					.getBufferByAcronym(buffersProposal, bufferNameToBufferAcronym.get(bufferName));
			if (bufferExists != null) {
				bufferNameToBuffer3VO.put(bufferName, bufferExists);
			} else {
				Buffer3VO buffer = new Buffer3VO();
				buffer.setAcronym(bufferNameToBufferAcronym.get(bufferName));
				buffer.setName(bufferNameToBufferAcronym.get(bufferName));
				bufferNameToBuffer3VO.put(bufferName, buffer);
			}
		}

		return bufferNameToBuffer3VO;
	}

	/** If the acronym exists in the list the macromolecule is returned **/
	private Macromolecule3VO getMacromoleculeByAcronym(List<Macromolecule3VO> macromolecules, String acronym) {
		for (Macromolecule3VO macromolecule : macromolecules) {
			if (macromolecule.getAcronym().toLowerCase().equals(acronym.toLowerCase())) {
				return macromolecule;
			}
		}
		return null;
	}

	/** If the acronym exists in the list the buffer is returned **/
	private Buffer3VO getBufferByAcronym(List<Buffer3VO> buffers, String acronym) {
		for (Buffer3VO buffer : buffers) {
			if (buffer.getAcronym().toLowerCase().equals(acronym.toLowerCase())) {
				return buffer;
			}
		}
		return null;
	}

	/**
	 * Gets the macromolecules. If there are two with the same name it means that they are the same macromolecules
	 * 
	 * @param samples
	 *            the samples
	 * @return the macromolecules
	 */
	private HashMap<String, Macromolecule3VO> parseMacromolecules(ArrayList<HashMap<String, String>> samples,
			int proposalId) {
		List<Macromolecule3VO> macromoleculesProposalList = proposal3Service.findMacromoleculesByProposalId(proposalId);
		Robot3ServiceBean.log.info("----------------- parseMacromolecules ------------ ");
		HashMap<String, Macromolecule3VO> acronymToMacromolecule3VO = new HashMap<String, Macromolecule3VO>();
		/** Store the name of the macromolecules **/
		HashSet<String> macromoleculeAcronyms = new HashSet<String>();
		for (HashMap<String, String> sample : samples) {
			if (sample.get("type").equals("Sample")) {
				/** If not macromolecule is defined we use the code **/
				if (sample.get("macromolecule") == null) {
					macromoleculeAcronyms.add(sample.get("code").trim());
				} else {
					macromoleculeAcronyms.add(sample.get("macromolecule").trim());
				}
			}
		}

		for (String acronym : macromoleculeAcronyms) {
			Macromolecule3VO macromoleculeExists = this.getMacromoleculeByAcronym(macromoleculesProposalList, acronym);
			if (macromoleculeExists != null) {
				acronymToMacromolecule3VO.put(acronym, macromoleculeExists);
			} else {
				Macromolecule3VO macromolecule = new Macromolecule3VO();
				macromolecule.setAcronym(acronym);
				macromolecule.setName(acronym);
				acronymToMacromolecule3VO.put(acronym, macromolecule);
			}
		}
		return acronymToMacromolecule3VO;
	}

	private String getSamplePositionKey(HashMap<String, String> sample) {
		return sample.get("plate") + "_" + sample.get("row") + "_" + sample.get("well");
	}

	/**
	 * Gets the specimens.
	 * 
	 * @param samples
	 *            the samples
	 * @param acronymToMacromolecule3VO
	 *            the macromolecules
	 * @param bufferNameToBuffer3VO
	 *            the buffers
	 * @param plateIndexToSamplePlate3VO
	 *            the plates
	 * @return HashMap<String, Specimen3VO> where String is bufferName for the buffers and position for the Samples
	 */
	private HashMap<String, Specimen3VO> getSpecimens(ArrayList<HashMap<String, String>> samples,
			HashMap<String, Macromolecule3VO> acronymToMacromolecule3VO,
			HashMap<String, Buffer3VO> bufferNameToBuffer3VO, HashMap<String, Sampleplate3VO> plateIndexToSamplePlate3VO) {

		HashMap<String, Specimen3VO> specimenIdToSpecimen3VO = new HashMap<String, Specimen3VO>();

		/** Looking for positions **/
		for (HashMap<String, String> sample : samples) {
			// String positionCode = sample.get("plate") + "_" + sample.get("row") + "_" + sample.get("well");
			String positionCode = this.getSamplePositionKey(sample);
			Specimen3VO specimen = null;

			if (specimenIdToSpecimen3VO.get(positionCode) == null) {
				/** Creating new Sample for the experiment **/
				specimen = new Specimen3VO();
				/** Buffer **/
				Buffer3VO buffer = bufferNameToBuffer3VO.get(sample.get("buffername").trim());
				if (buffer == null) {
					log.error("Buffer not found with buffer name: " + sample.get("buffername"));
				}
				specimen.setBufferId(buffer.getBufferId());

				/** Macromolecule **/
				if (sample.get("type").equals("Sample")) {
					Macromolecule3VO macromolecule3VO = acronymToMacromolecule3VO.get(sample.get("macromolecule"));
					specimen.setMacromolecule3VO(macromolecule3VO);
				}

				/** Position **/
				if (plateIndexToSamplePlate3VO != null) {
					Sampleplate3VO plate = plateIndexToSamplePlate3VO.get(sample.get("plate"));
					if (plate != null) {
						Sampleplateposition3VO position = new Sampleplateposition3VO();
						position.setSamplePlateId(plate.getSamplePlateId());
						position.setRowNumber(Integer.parseInt(sample.get("row")));
						position.setColumnNumber(Integer.parseInt(sample.get("well")));
						position = entityManager.merge(position);
						specimen.setSampleplateposition3VO(position);
					}
				}
				/** Specimen properties **/
				specimen.setConcentration(sample.get("concentration"));
				specimen.setVolume(sample.get("volume"));
//				specimen.setCode(sample.get("macromolecule"));
			} else {
				/** Updating volume to load **/
				specimen = specimenIdToSpecimen3VO.get(positionCode);
				specimen.setVolume(String.valueOf(Integer.parseInt(specimen.getVolume())
						+ Integer.parseInt(sample.get("volume"))));
			}

			/** it is a buffer we keep the code of the buffername **/
			if (specimen.getMacromolecule3VO() == null) {
				specimenIdToSpecimen3VO.put(sample.get("buffername"), specimen);
			} else {
				specimenIdToSpecimen3VO.put(positionCode, specimen);
			}
		}
		return specimenIdToSpecimen3VO;
	}


	/**
	 * Parses the specimen.
	 * 
	 * @param sample
	 *            the sample
	 * @return the specimen3 vo
	 */
	private Measurement3VO parseSpecimen(HashMap<String, String> sample) {
		String waittime = sample.get("waittime");

		/**
		 * Problem is that from BsxCube there is not any parameters for volume, volume in this case means volumeToLoad
		 * however from WUI there are both for indicating in buffers specially the volume and the volumeToLoad
		 * **/
		String volumeToLoad = sample.get("volume");
		if (sample.containsKey("volumeToLoad")) {
			volumeToLoad = sample.get("volumeToLoad");
		}
		String comments = sample.get("comments");
		String transmission = sample.get("transmission");
		String viscosity = sample.get("viscosity");
		String flow = sample.get("flow");

		Measurement3VO measurement = new Measurement3VO();
		measurement.setWaitTime(waittime);
		measurement.setVolumeToLoad(volumeToLoad);
		measurement.setComment(comments);
		measurement.setTransmission(transmission);
		measurement.setViscosity(viscosity);
		measurement.setFlow(Boolean.valueOf(flow));

		return measurement;
	}

	/**
	 * Creates the measurements.
	 * 
	 * @param samples
	 *            the samples
	 * @param sample3vOs
	 *            the sample3v os
	 * @param experiment
	 *            the experiment
	 * @param buffers
	 *            the buffers
	 * @param mode
	 *            the mode
	 * @param extraFlowTime
	 *            the extra flow time
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<SaxsDataCollection3VO> createMeasurements(ArrayList<HashMap<String, String>> samples,
			HashMap<String, Specimen3VO> sample3vOs, Experiment3VO experiment, HashMap<String, Buffer3VO> buffers,
			String mode, String extraFlowTime) throws Exception {
		List<SaxsDataCollection3VO> dataCollections = new ArrayList<SaxsDataCollection3VO>();

		/** Creating for measurements **/
		for (HashMap<String, String> sample : samples) {
			String positionCode = sample.get("plate") + "_" + sample.get("row") + "_" + sample.get("well");
			if (sample.get("type").equals("Sample")) {

				Specimen3VO specimen = sample3vOs.get(positionCode);
				String exposureTemperature = sample.get("SEUtemperature");
				Measurement3VO measurement = this.parseSpecimen(sample);
				measurement.setSpecimenId(specimen.getSpecimenId());
				measurement.setExtraFlowTime(extraFlowTime);
				measurement.setExposureTemperature(exposureTemperature);
				

				/** Buffer **/
				Specimen3VO buffer = sample3vOs.get(sample.get("buffername"));

				if (buffer == null) {
					throw new Exception("Buffer not found: " + sample.get("buffername"));
				}

				/** MODE: Before and After **/
				if (mode.equals("BeforeAndAfter")) {
					for (HashMap<String, String> sampleBuffer : samples) {
						if (sampleBuffer.get("type").equals("Buffer")) {
							if (sampleBuffer.get("buffername").equals(sample.get("buffername"))) {

								/** Measurement Before **/
								Measurement3VO measurementBufferBefore = this.parseSpecimen(sampleBuffer);
								measurementBufferBefore.setSpecimenId(buffer.getSpecimenId());
								measurementBufferBefore.setExtraFlowTime(extraFlowTime);
								measurementBufferBefore.setExposureTemperature(exposureTemperature);

								/** Copying parameters of the sample measurement **/
								/** Update, buffer's parameter may be different **/
								measurementBufferBefore.setVolumeToLoad(measurement.getVolumeToLoad());
								measurementBufferBefore.setTransmission(measurement.getTransmission());
								measurementBufferBefore.setFlow(measurement.getFlow());
								measurementBufferBefore.setViscosity(measurement.getViscosity());
								measurementBufferBefore.setWaitTime(measurement.getWaitTime());

								/** But not exposure temperature **/
								measurementBufferBefore.setExposureTemperature(measurement.getExposureTemperature());

								measurementBufferBefore = this.measurement3ServiceLocal.merge(measurementBufferBefore);
								buffer.getMeasurements().add(measurementBufferBefore);

								/** Sample **/
								measurement = this.measurement3ServiceLocal.merge(measurement);
								specimen.getMeasurements().add(measurement);

								/** After **/
								Measurement3VO specimenBufferAfter = this.parseSpecimen(sampleBuffer);
								specimenBufferAfter.setSpecimenId(buffer.getSpecimenId());
								specimenBufferAfter.setExtraFlowTime(extraFlowTime);
								specimenBufferAfter.setExposureTemperature(exposureTemperature);
								/** Specimen After Code **/
								// specimenBufferAfter.setCode(experiment.getCodeSpecimen(buffer, specimenBufferAfter));

								/** Copying parameters of the sample measurement **/
								specimenBufferAfter.setVolumeToLoad(measurement.getVolumeToLoad());
								specimenBufferAfter.setTransmission(measurement.getTransmission());
								specimenBufferAfter.setFlow(measurement.getFlow());
								specimenBufferAfter.setViscosity(measurement.getViscosity());
								specimenBufferAfter.setWaitTime(measurement.getWaitTime());

								specimenBufferAfter.setExposureTemperature(measurement.getExposureTemperature());

								specimenBufferAfter = this.measurement3ServiceLocal.merge(specimenBufferAfter);
								buffer.getMeasurements().add(specimenBufferAfter);

								/** Creating Data Collections **/
								List<Measurement3VO> dataCollectionSpecimens = new ArrayList<Measurement3VO>();
								dataCollectionSpecimens.add(measurementBufferBefore);
								dataCollectionSpecimens.add(measurement);
								dataCollectionSpecimens.add(specimenBufferAfter);
								dataCollections.add(this.createDataCollection(dataCollectionSpecimens, experiment));
							}
						}
					}
				}

				if (mode.equals("After")) {
					for (HashMap<String, String> sampleBuffer : samples) {
						if (sampleBuffer.get("type").equals("Buffer")) {
							if (sampleBuffer.get("buffername").equals(sample.get("buffername"))) {

								/** Sample **/
								measurement = this.measurement3ServiceLocal.merge(measurement);

								/** After **/
								Measurement3VO measurementBufferAfter = this.parseSpecimen(sampleBuffer);
								measurementBufferAfter.setSpecimenId(buffer.getSpecimenId());
								measurementBufferAfter.setExtraFlowTime(extraFlowTime);
								measurementBufferAfter.setExposureTemperature(exposureTemperature);

								/** Specimen code **/
								// measurementBufferAfter.setCode(experiment.getCodeSpecimen(buffer,
								// measurementBufferAfter));
								measurementBufferAfter = this.measurement3ServiceLocal.merge(measurementBufferAfter);

								/** Creating Data Collections **/
								List<Measurement3VO> dataCollectionSpecimens = new ArrayList<Measurement3VO>();
								dataCollectionSpecimens.add(measurement);
								dataCollections.add(this.createDataCollection(dataCollectionSpecimens, experiment));
							}
						}
					}
				}

				if (mode.equals("Before")) {
					for (HashMap<String, String> sampleBuffer : samples) {
						if (sampleBuffer.get("type").equals("Buffer")) {
							if (sampleBuffer.get("buffername").equals(sample.get("buffername"))) {
								Measurement3VO specimenBufferBefore = this.parseSpecimen(sampleBuffer);
								specimenBufferBefore.setSpecimenId(buffer.getSpecimenId());
								specimenBufferBefore.setExtraFlowTime(extraFlowTime);
								specimenBufferBefore.setExposureTemperature(exposureTemperature);

								/** Specimen Before **/
								// specimenBufferBefore.setCode(experiment.getCodeSpecimen(buffer,
								// specimenBufferBefore));
								specimenBufferBefore = this.measurement3ServiceLocal.merge(specimenBufferBefore);
								buffer.getMeasurements().add(specimenBufferBefore);

								/** Sample **/
								measurement = this.measurement3ServiceLocal.merge(measurement);
								specimen.getMeasurements().add(measurement);

								/** Creating Data Collections **/
								List<Measurement3VO> dataCollectionSpecimens = new ArrayList<Measurement3VO>();
								dataCollectionSpecimens.add(specimenBufferBefore);
								dataCollectionSpecimens.add(measurement);
								dataCollections.add(this.createDataCollection(dataCollectionSpecimens, experiment));
							}
						}
					}
				}

				if (mode.equals("None")) {
					/** Single sample with no data collection **/
					measurement = this.measurement3ServiceLocal.merge(measurement);
					specimen.getMeasurements().add(measurement);

				}
			}
		}
		return dataCollections;
	}

	/**
	 * Creates the data collection.
	 * 
	 * @param specimens
	 *            the specimens
	 * @param experiment
	 *            the experiment
	 * @return the saxs data collection3 vo
	 */
	private SaxsDataCollection3VO createDataCollection(List<Measurement3VO> specimens, Experiment3VO experiment) {
		SaxsDataCollection3VO dc = new SaxsDataCollection3VO();
		dc.setExperimentId(experiment.getExperimentId());
		dc = this.entityManager.merge(dc);

		int order = 1;
		for (Measurement3VO specimen3vo : specimens) {
			MeasurementTodataCollection3VO mc = new MeasurementTodataCollection3VO();
			mc.setDataCollectionId(dc.getDataCollectionId());
			mc.setMeasurementId(specimen3vo.getMeasurementId());
			mc.setDataCollectionOrder(order);
			dc.getMeasurementtodatacollection3VOs().add(this.measurementToDataCollection3Service.merge(mc));
			order++;

		}
		return dc;
	}

	private Specimen3VO isAlreadyCreatedByPosition(Specimen3VO specimen, List<Specimen3VO> specimens) {
		if (specimen.getSampleplateposition3VO() != null) {
			/** Checking for position of the specimen **/
			for (Specimen3VO existingSpecimen : specimens) {
				if (existingSpecimen.getSampleplateposition3VO() != null) {
					if (existingSpecimen.getSampleplateposition3VO().getSamplePlateId()
							.equals(specimen.getSampleplateposition3VO().getSamplePlateId())) {
						if (existingSpecimen.getSampleplateposition3VO().getColumnNumber() == specimen
								.getSampleplateposition3VO().getColumnNumber()) {
							if (existingSpecimen.getSampleplateposition3VO().getRowNumber() == specimen
									.getSampleplateposition3VO().getRowNumber()) {
								return existingSpecimen;
							}
						}
					}
				}
			}
		}
		return null;
	}

	private Experiment3VO createExperimentFromRobotParams(Experiment3VO experiment,
			ArrayList<HashMap<String, String>> samples, int proposalId, String mode, String storageTemperature,
			String extraFlowTime, Boolean optimize) throws Exception {

		try {
			ArrayList<Buffer3VO> bufferList = new ArrayList<Buffer3VO>();
			HashMap<String, Buffer3VO> bufferNameToBuffer3VO = parseBuffers(samples, proposalId);
			HashMap<Integer, Buffer3VO> bufferIdToBuffer3VO = new HashMap<Integer, Buffer3VO>();
			for (String key : bufferNameToBuffer3VO.keySet()) {
				Buffer3VO buffer3VO = bufferNameToBuffer3VO.get(key);
				buffer3VO.setProposalId(proposalId);
				buffer3VO = this.proposal3Service.merge(buffer3VO);
				bufferNameToBuffer3VO.put(key, buffer3VO);
				bufferIdToBuffer3VO.put(buffer3VO.getBufferId(), buffer3VO);
				bufferList.add(buffer3VO);
			}

			HashMap<String, Macromolecule3VO> acronymToMacromolecule3VO = parseMacromolecules(samples, proposalId);
			log.debug("----------------- MACROMOLECULES ------------");
			log.debug(acronymToMacromolecule3VO.toString());
			for (String key : acronymToMacromolecule3VO.keySet()) {
				Macromolecule3VO macromolecule3VO = acronymToMacromolecule3VO.get(key);
				macromolecule3VO.setProposalId(proposalId);
				macromolecule3VO = this.proposal3Service.merge(macromolecule3VO);
				acronymToMacromolecule3VO.put(key, macromolecule3VO);
			}

			/** There is only one plate group **/
			PlateGroup3VO plateGroup = new PlateGroup3VO();
			plateGroup.setName("BsxCube Group 1");
			plateGroup.setStorageTemperature(storageTemperature);
			plateGroup = this.samplePlate3Service.merge(plateGroup);

			/** Plate index is the position in the plate in the sample changer: 1,2,3 for bm29 **/
//			HashMap<String, Sampleplate3VO> plateIndexToSamplePlate3VO = parsePlates(samples, storageTemperature);
			HashMap<String, Sampleplate3VO> plateIndexToSamplePlate3VO = getDefaultSampleChangerPlateconfiguration(storageTemperature);
			System.out.println(new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create().toJson(plateIndexToSamplePlate3VO));
			
			/** Creating plates **/
			for (String key : plateIndexToSamplePlate3VO.keySet()) {
				Sampleplate3VO plate = plateIndexToSamplePlate3VO.get(key);
				plate.setExperimentId(experiment.getExperimentId());
				plate.setStorageTemperature(storageTemperature);
				plate.setPlategroup3VO(plateGroup);
				plate = this.samplePlate3Service.merge(plate);
				plateIndexToSamplePlate3VO.put(key, plate);
				experiment.getSamplePlate3VOs().add(plate);
			}

			log.debug("----------------- buffers ------------");
			log.debug(bufferNameToBuffer3VO.toString());

			log.debug("----------------- samples ------------");
			log.debug(samples.toString());

			HashMap<String, Specimen3VO> specimens = getSpecimens(samples, acronymToMacromolecule3VO,
					bufferNameToBuffer3VO, plateIndexToSamplePlate3VO);
			log.debug("----------------- specimens ------------");
			log.debug(specimens.toString());

			HashMap<String, Specimen3VO> sampleBuffersVOs = new HashMap<String, Specimen3VO>();

			List<Specimen3VO> specimensCreated = new ArrayList<Specimen3VO>();
			/**
			 * Key: For buffers is the buffer name For samples is the position
			 */
			for (String key : specimens.keySet()) {
				Specimen3VO specimen = specimens.get(key);
				if (specimen.getSampleplateposition3VO() != null) {
					specimen.setSampleplateposition3VO(entityManager.merge(specimen.getSampleplateposition3VO()));
				}
				specimen.setExperimentId(experiment.getExperimentId());

				/**
				 * Feature when samples contains macromolecule and buffername is the backgroundId Before doing the merge
				 * we have to check that this is not the case: BUFFERNAME PLATE ROW WELL BUFFER 1 1 1 1 BBSA 2 1 1 1
				 * BBSA
				 * **/
				Specimen3VO existingSpecimenAtSamePosition = isAlreadyCreatedByPosition(specimen, specimensCreated);
				if (existingSpecimenAtSamePosition == null) {
					specimen = specimen3Service.merge(specimen);
					specimensCreated.add(specimen);
					experiment.getSamples().add(specimen);
				} else {
					specimen = existingSpecimenAtSamePosition;
				}

				specimens.put(key, specimen);
				if (specimen.getMacromolecule3VO() == null) {
					sampleBuffersVOs.put(bufferIdToBuffer3VO.get(specimen.getBufferId()).getAcronym(), specimen);
				}
			}

			List<SaxsDataCollection3VO> dataCollectionList = createMeasurements(samples, specimens, experiment,
					bufferNameToBuffer3VO, mode, extraFlowTime);

			/** Set the priorities but also optimize the buffers **/
			if (optimize) {
				// measurement3ServiceLocal.optimizeAndPrioritize(experiment, dataCollections, 1);
				measurement3ServiceLocal.resetAllPriorities(experiment, dataCollectionList);
				measurement3ServiceLocal.optimizeDatacollectionByRemovingDuplicatedBuffers(experiment,
						dataCollectionList, 1);
			}
			return experiment;
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * createExperimentFromRobotParams
	 * 
	 * Create the macromolecule, buffer, plates, samples, measurement and executes the prioritizer in order to optimize
	 * the buffers duplicated When a data collection is: buffer1 sample1 buffer1 buffer1 sample2 buffer1
	 * 
	 * Because there are two buffer1 together the prioritizer will remove one like: buffer1 sample1 buffer1 sample2
	 * buffer1
	 * 
	 * @param samples
	 *            ArrayList<HashMap<String, String>> containing the hashmaps with all the values required to generate an
	 *            experiment samples = "[{'code': 'Buff1', 'plate': '2', 'enable': True, 'title': 'P2-1:9',
	 *            'transmission': 20.0, 'viscosity': 'Low', 'recuperate': False, 'SEUtemperature': 4.0, 'flow': False,
	 *            'comments': 'test', 'volume': 20, 'buffername': 'testot', 'typen': 0, 'waittime': 0.0,
	 *            'concentration': 0.0, 'type': 'Buffer', 'well': '9', 'row': 1}]
	 * 
	 *            Coming form WUI we add a new parameter called volumeToLoad in order to distinguish both specially for
	 *            buffer measurements
	 * 
	 * @param proposalId
	 *            the proposal id
	 * @param mode
	 *            the mode: "BeforeAndAfter", "None", "Before" and "After"
	 * @param storageTemperature
	 *            the storage temperature
	 * @param extraFlowTime
	 *            the extra flow time
	 * @return the experiment3VO
	 * 
	 *         TODO: Mode should replaced by enumeration
	 */
	@Override
	public Experiment3VO createExperimentFromRobotParams(ArrayList<HashMap<String, String>> samples, Integer sessionId,
			int proposalId, String mode, String storageTemperature, String extraFlowTime, String type,
			String sourceFilePath, String name, String comments) throws Exception {
		return this.createExperimentFromRobotParams(samples, sessionId, proposalId, mode, storageTemperature,
				extraFlowTime, type, sourceFilePath, name, true, comments);
	}

	@Override
	public Experiment3VO createExperimentFromRobotParams(ArrayList<HashMap<String, String>> samples, Integer sessionId,
			int proposalId, String mode, String storageTemperature, String extraFlowTime, String type,
			String sourceFilePath, String name, Boolean optimize, String comments) throws Exception {

		Experiment3VO experiment = new Experiment3VO();
		experiment = this.experiment3ServiceLocal.initPlates(experiment);
		experiment.setType(type);
		experiment.setSourceFilePath(sourceFilePath);
		experiment.setCreationDate(getNow());
		experiment.setName(name);
		experiment.setProposalId(proposalId);
		experiment.setSessionId(sessionId);
		experiment.setComments(comments);
		experiment = this.experiment3ServiceLocal.merge(experiment);

		if (sourceFilePath != null) {
			/** Modifying the path if contains __ID__ then we replace __ID__ by the real id of the experiment **/
			experiment.setSourceFilePath(experiment.getSourceFilePath().replace("__ID__",
					String.valueOf(experiment.getExperimentId())));
			experiment = this.experiment3ServiceLocal.merge(experiment);
		}

		/** Triming all values **/
		for (HashMap<String, String> hashMap : samples) {
			for (String key : hashMap.keySet()) {
				/** It may be null when creating template and plate = null **/
				if (hashMap.get(key) != null) {
					hashMap.put(key, hashMap.get(key).trim());
				}
			}
		}

		/** Dealing with macromolecule **/
		for (HashMap<String, String> hashMap : samples) {
			if (!hashMap.containsKey("macromolecule")) {
				hashMap.put("macromolecule", hashMap.get("code"));
			}
		}

		experiment = this.createExperimentFromRobotParams(experiment, samples, proposalId, mode, storageTemperature,
				extraFlowTime, optimize);
		return experiment;
	}

	@Override
	public Experiment3VO addMeasurementsToExperiment(int experimentId, int proposalId,
			ArrayList<HashMap<String, String>> samples) throws Exception {
		System.out.println("addMeasurementsToExperiment");
		this.log.info("addMeasurementsToExperiment");
//		Experiment3VO experiment = this.experiment3ServiceLocal.findById(experimentId, ExperimentScope.PREPARE_EXPERIMENT, proposalId);
		Experiment3VO experiment = this.experiment3ServiceLocal.findById(experimentId, ExperimentScope.MINIMAL, proposalId);
		System.out.println("PREPARE_EXPERIMENT");
		this.log.info("PREPARE_EXPERIMENT");
		/** Triming all values **/
		for (HashMap<String, String> hashMap : samples) {
			for (String key : hashMap.keySet()) {
				/** It may be null when creating template and plate = null **/
				if (hashMap.get(key) != null) {
					hashMap.put(key, hashMap.get(key).trim());
				}
			}
		}

		HashMap<String, Buffer3VO> buffers = this.parseBuffers(samples, proposalId);
		for (String key : buffers.keySet()) {
			System.out.println(buffers.get(key));
		}

		HashMap<String, Macromolecule3VO> macromolecules = this.parseMacromolecules(samples, proposalId);
		for (String key : macromolecules.keySet()) {
			System.out.println(macromolecules.get(key));
		}

		HashMap<String, Specimen3VO> specimens = this.getSpecimens(samples, macromolecules, buffers);
		for (String key : specimens.keySet()) {
			System.out.println(specimens.get(key));
		}

		for (String key : specimens.keySet()) {
			Specimen3VO sample = specimens.get(key);
			sample.setExperimentId(experiment.getExperimentId());
			sample.setSampleplateposition3VO(null);
			sample = specimen3Service.merge(sample);
			experiment.getSamples().add(sample);
			specimens.put(key, sample);
		}
		this.createMeasurements(samples, specimens, experiment, buffers, "BeforeAndAfter", "0");
		experiment = this.experiment3ServiceLocal.findById(experimentId, ExperimentScope.MEDIUM, proposalId);
		return experiment;
	}

	private HashMap<String, Specimen3VO> getSpecimens(ArrayList<HashMap<String, String>> samples,
			HashMap<String, Macromolecule3VO> macromolecules, HashMap<String, Buffer3VO> buffers) {

		HashMap<String, Specimen3VO> sample3VOs = new HashMap<String, Specimen3VO>();

		/** Looking for positions **/
		for (HashMap<String, String> sample : samples) {
			String positionCode = sample.get("plate") + "_" + sample.get("row") + "_" + sample.get("well");
			Specimen3VO sample3VO = null;

			if (sample3VOs.get(positionCode) == null) {
				/** Creating new Sample for the experiment **/
				sample3VO = new Specimen3VO();

				/** Buffer **/
				Buffer3VO buffer = buffers.get(sample.get("buffername").trim());
				if (buffer == null) {
					log.error("Buffer not found with buffer name: " + sample.get("buffername"));
				}
				sample3VO.setBufferId(buffer.getBufferId());

				/** Macromolecule **/
				if (sample.get("type").equals("Sample")) {
					Macromolecule3VO macromolecule3VO = macromolecules.get(sample.get("macromolecule"));
					sample3VO.setMacromolecule3VO(macromolecule3VO);
				}

				/** Sample properties **/
				sample3VO.setConcentration(sample.get("concentration"));
				sample3VO.setVolume(sample.get("volume"));
//				sample3VO.setCode(sample.get("macromolecule"));

			} else {
				/** Updating volume to load **/
				sample3VO = sample3VOs.get(positionCode);
				sample3VO.setVolume(String.valueOf(Integer.parseInt(sample3VO.getVolume())
						+ Integer.parseInt(sample.get("volume"))));
			}

			/** it is a buffer we keep the code of the buffername **/
			if (sample3VO.getMacromolecule3VO() == null) {
				sample3VOs.put(sample.get("buffername"), sample3VO);
			} else {
				sample3VOs.put(positionCode, sample3VO);
			}

		}
		return sample3VOs;
	}
	

	/**
	 * Gets the plates. This should be changed depending on the bm configuration
	 * 
	 * @param samples
	 *            the samples
	 * @param storageTemperature
	 *            the storage temperature
	 * @return the plates
	 */
	@Override
	public  HashMap<String, Sampleplate3VO> getDefaultSampleChangerPlateconfiguration( String storageTemperature) {
		/** There is always three Plates **/
		HashMap<String, Sampleplate3VO> platePositionToSamplePlate3VO = new HashMap<String, Sampleplate3VO>();

		/** Plate 1 **/
		Platetype3VO type = this.plateType3Service.findById(1);
		Sampleplate3VO samplePlate_1 = new Sampleplate3VO();
		samplePlate_1.setStorageTemperature(storageTemperature);
		samplePlate_1.setName(type.getName());
		samplePlate_1.setSlotPositionRow("1");
		samplePlate_1.setSlotPositionColumn("1");
		samplePlate_1.setPlatetype3VO(type);
		platePositionToSamplePlate3VO.put("1", samplePlate_1);

		/** Plate 2 **/
		Platetype3VO type_2 = this.plateType3Service.findById(2);
		Sampleplate3VO samplePlate_2 = new Sampleplate3VO();
		samplePlate_2.setStorageTemperature(storageTemperature);
		samplePlate_2.setName(type_2.getName());
		samplePlate_2.setPlatetype3VO(type_2);
		samplePlate_2.setSlotPositionRow("1");
		samplePlate_2.setSlotPositionColumn("2");
		platePositionToSamplePlate3VO.put("2", samplePlate_2);

		/** Plate 3 **/
		Platetype3VO type_3 = this.plateType3Service.findById(4);
		Sampleplate3VO samplePlate_3 = new Sampleplate3VO();
		samplePlate_3.setStorageTemperature(storageTemperature);
		samplePlate_3.setName(type_3.getName());
		samplePlate_3.setSlotPositionRow("1");
		samplePlate_3.setSlotPositionColumn("3");
		samplePlate_3.setPlatetype3VO(type_3);
		platePositionToSamplePlate3VO.put("3", samplePlate_3);

		/** At EMBL Layout is vertical instead of horizontal **/
		if (Constants.SITE_IS_EMBL()){
			samplePlate_1.setSlotPositionRow("1");
			samplePlate_1.setSlotPositionColumn("1");
			samplePlate_2.setSlotPositionRow("2");
			samplePlate_2.setSlotPositionColumn("1");
			samplePlate_3.setSlotPositionRow("3");
			samplePlate_3.setSlotPositionColumn("1");
		}
		return platePositionToSamplePlate3VO;
	}

	public ArrayList<Macromolecule3VO> createOrUpdateMacromolecule(ArrayList<HashMap<String, String>> samples, int proposalId) throws Exception {
		Robot3ServiceBean.log.info("createOrUpdateMacromolecule");
		
		ArrayList<Macromolecule3VO> macromoleculeList = new ArrayList<Macromolecule3VO>();
		
		/** Triming all values **/
		for (HashMap<String, String> hashMap : samples) {
			for (String key : hashMap.keySet()) {
				/** It may be null when creating template and plate = null **/
				if (hashMap.get(key) != null) {
					hashMap.put(key, hashMap.get(key).trim());
				}
			}
		}
		
		HashMap<String, Macromolecule3VO> macromolecules = this.parseMacromolecules(samples, proposalId);
		for (String key : macromolecules.keySet()) {
			Robot3ServiceBean.log.info("createOrUpdateMacromolecule: " + macromolecules.get(key));
		}
		
		/** Macromolecule **/
		for (HashMap<String, String> sample : samples) {
			if (sample.get("type").equals("Sample")) {
				Macromolecule3VO macromolecule3VO = macromolecules.get(sample.get("macromolecule"));
				Robot3ServiceBean.log.info("createOrUpdateMacromolecule macromolecule.acronym = " + macromolecule3VO.getAcronym());
				if (macromolecule3VO != null) {
					macromolecule3VO.setProposalId(proposalId);
					macromolecule3VO = this.proposal3Service.merge(macromolecule3VO);
					macromoleculeList.add(macromolecule3VO);
				}
			}
		}
		Robot3ServiceBean.log.info("createOrUpdateMacromolecule size = " + macromoleculeList.size());
		
		return macromoleculeList;
	}
	
	public ArrayList<Buffer3VO> createOrUpdateBuffer(ArrayList<HashMap<String, String>> samples, int proposalId) throws Exception {
		Robot3ServiceBean.log.info("createOrUpdateBuffer");
		
		ArrayList<Buffer3VO> bufferList = new ArrayList<Buffer3VO>();
		
		/** Triming all values **/
		for (HashMap<String, String> hashMap : samples) {
			for (String key : hashMap.keySet()) {
				/** It may be null when creating template and plate = null **/
				if (hashMap.get(key) != null) {
					hashMap.put(key, hashMap.get(key).trim());
				}
			}
		}

		HashMap<String, Buffer3VO> buffers = this.parseBuffers(samples, proposalId);
		for (String key : buffers.keySet()) {
			Robot3ServiceBean.log.info("createOrUpdateBuffer: key= " + key + " | value= " + buffers.get(key));
		}
		
		/** Buffer **/
		for (HashMap<String, String> sample : samples) {
			Buffer3VO buffer3VO = buffers.get(sample.get("buffername").trim());
			if (buffer3VO == null) {
				log.error("Buffer not found with buffer name: " + sample.get("buffername"));
			}
			
			if (buffer3VO != null) {
				buffer3VO.setProposalId(proposalId);
				buffer3VO = this.proposal3Service.merge(buffer3VO);
				bufferList.add(buffer3VO);
			}
		}
		Robot3ServiceBean.log.info("createOrUpdateBuffer size = " + bufferList.size());
		
		return bufferList;
	}
}
