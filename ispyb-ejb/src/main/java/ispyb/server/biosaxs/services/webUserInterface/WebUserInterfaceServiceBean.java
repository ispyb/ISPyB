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

package ispyb.server.biosaxs.services.webUserInterface;

import ispyb.server.biosaxs.services.core.ExperimentScope;
import ispyb.server.biosaxs.services.core.experiment.Experiment3ServiceLocal;
import ispyb.server.biosaxs.services.core.measurement.Measurement3Service;
import ispyb.server.biosaxs.services.core.measurementToDataCollection.MeasurementToDatacollection3ServiceLocal;
import ispyb.server.biosaxs.services.core.specimen.Specimen3Service;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Measurement3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Specimen3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Sampleplate3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Sampleplateposition3VO;
import ispyb.server.biosaxs.vos.datacollection.MeasurementTodataCollection3VO;
import ispyb.server.biosaxs.vos.datacollection.SaxsDataCollection3VO;
import ispyb.server.biosaxs.vos.utils.comparator.SaxsDataCollectionComparator;
import ispyb.server.biosaxs.vos.utils.comparator.SpecimenComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * The goal of this class is to merge all the logic coming from BiosaxsDataAdapter
 * 
 */
@Stateless
public class WebUserInterfaceServiceBean implements WebUserInterfaceService, WebUserInterfaceServiceLocal {

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	/** The measurement to data collection3 service. */
	@EJB
	private MeasurementToDatacollection3ServiceLocal measurementToDataCollection3Service;

	@EJB
	private Experiment3ServiceLocal experiment3ServiceLocal;

	@EJB
	private Measurement3Service measurement3Service;

	@EJB
	private Specimen3Service specimen3Service;

	/**
	 * This method only will remove measurements associated to a macromolecule and will remove data collection
	 * 
	 * 1.- Look for MeasurementToDataCollection for the measurement. If it is linked to several we will throw an exception as any
	 * sample can be associated to several data collections.... 2.- Look for the SaxsDataCollection3VO associated to the measurement
	 * 3.- Look for the other MeasurementTodataCollection3VO associated to this SaxsDataCollection3VO 4.- For each
	 * MeasurementTodataCollection3VO if MeasurementTodataCollection3VO belongs to another SaxsDataCollection3VO we will remove
	 * MeasurementTodataCollection3VO object but not Measurement3VO otherwise we will remove MeasurementTodataCollection3VO and
	 * Measurement3VO
	 * 
	 * 5.- If measurement's specimen has no other measurement we will remove it
	 * 
	 * 
	 * @throws Exception
	 */
	@Override
	public void removeDataCollectionByMeasurement(Measurement3VO measurement3vo) throws Exception {
		List<MeasurementTodataCollection3VO> measurementToDatacollection = measurementToDataCollection3Service
				.findByMeasurementId(measurement3vo.getMeasurementId());
		/** It is a sample so we can proceed **/
		if (measurementToDatacollection.size() == 1) {
			MeasurementTodataCollection3VO measurementToDataCollection = measurementToDatacollection.get(0);
			/** We get all the measurements of the data collection **/
			SaxsDataCollection3VO dataCollection = measurementToDataCollection3Service
					.findDataCollectionById(measurementToDataCollection.getDataCollectionId());
			/** Removing measurementToDatacollection and Measurement3VO **/
			List<MeasurementTodataCollection3VO> measurementToDatacollectionMeasurements = measurementToDataCollection3Service
					.findMeasurementToDataCollectionByDataCollectionId(dataCollection.getDataCollectionId());
			Iterator<MeasurementTodataCollection3VO> iterator = measurementToDatacollectionMeasurements.iterator();
			while (iterator.hasNext()) {
				MeasurementTodataCollection3VO measurementTodataCollection3VO = iterator.next();
				List<MeasurementTodataCollection3VO> measurementToDataCollectionsForThatMeasurement = measurementToDataCollection3Service
						.findByMeasurementId(measurementTodataCollection3VO.getMeasurementId());
				this.entityManager.remove(measurementTodataCollection3VO);
				if (measurementToDataCollectionsForThatMeasurement.size() == 1) {
					/** We remove the measurement as any DataCollection3VO is using it **/
					Measurement3VO measurement3VO = entityManager.find(Measurement3VO.class,
							measurementTodataCollection3VO.getMeasurementId());
					Specimen3VO specimen = this.entityManager.find(Specimen3VO.class, measurement3VO.getSpecimenId());
					Iterator<Measurement3VO> it = specimen.getMeasurements().iterator();
					while (it.hasNext()) {
						Measurement3VO m = it.next();
						if (m.getMeasurementId() == measurement3VO.getMeasurementId()) {
							this.entityManager.remove(m);
						}
					}
				}
			}

			/** Removing SaxsDataCollection **/
			this.entityManager.remove(dataCollection);
		} else {
			throw new Exception("Measurement3VO with id: " + measurement3vo.getMeasurementId()
					+ " can not be deleted because belongs to " + measurementToDatacollection.size() + " data collections");
		}
	}

	/**
	 * Remove the specimen and its sample plate position
	 */
	@Override
	public void remove(Specimen3VO specimen) {
		specimen = this.entityManager.find(Specimen3VO.class, specimen.getSpecimenId());
		if (specimen != null) {
			if (specimen.getMeasurements().size() == 0) {
				/** Removing sample plate position **/
				if (specimen.getSampleplateposition3VO() != null) {
					this.entityManager.remove(specimen.getSampleplateposition3VO());
				}
				/** Removing specimen **/
				this.entityManager.remove(specimen);
			}
		}
	}

	@Override
	public List<Specimen3VO> getSpecimenByDataCollectionId(int dataColectionId) {
		List<Specimen3VO> specimens = new ArrayList<Specimen3VO>();

		HashSet<Integer> specimenIds = new HashSet<Integer>();
		List<MeasurementTodataCollection3VO> measurementToDcs = this.measurementToDataCollection3Service
				.findMeasurementToDataCollectionByDataCollectionId(dataColectionId);
		for (MeasurementTodataCollection3VO measurementToDc : measurementToDcs) {
			Measurement3VO measurement = this.entityManager.find(Measurement3VO.class, measurementToDc.getMeasurementId());
			Specimen3VO specimen = this.entityManager.find(Specimen3VO.class, measurement.getSpecimenId());
			if (!specimenIds.contains(specimen.getSpecimenId())) {
				specimens.add(specimen);
			}
		}
		return specimens;

	}

	@Override
	public Experiment3VO setPriorities(int experimentId, int proposalId, final SaxsDataCollectionComparator... multipleOptions) {
		this.experiment3ServiceLocal.setPriorities(experimentId, proposalId, multipleOptions);
		return this.experiment3ServiceLocal.findById(experimentId, ExperimentScope.MEDIUM, proposalId);
	}

	@Override
	public Measurement3VO merge(Measurement3VO measurementSample) throws Exception {
		List<MeasurementTodataCollection3VO> measurementToDatacollectionList = this.measurementToDataCollection3Service
				.findByMeasurementId(measurementSample.getMeasurementId());
		System.out.println("\tmeasurementSample: " + measurementSample.getMeasurementId());
		/** It is supposed to be only in one data collection as it should be only the sample **/
		if (measurementToDatacollectionList.size() == 1) {
			SaxsDataCollection3VO mtc = this.measurementToDataCollection3Service
					.findDataCollectionById(measurementToDatacollectionList.get(0).getDataCollectionId());
			System.out.println("\t\tdataCollection: " + mtc.getDataCollectionId());
			List<MeasurementTodataCollection3VO> mm = this.measurementToDataCollection3Service
					.findMeasurementToDataCollectionByDataCollectionId(mtc.getDataCollectionId());
			for (MeasurementTodataCollection3VO measurementTodataCollection3VO : mm) {
				Measurement3VO measurement = this.entityManager.find(Measurement3VO.class,
						measurementTodataCollection3VO.getMeasurementId());
				System.out.println("\t\t\tmeasurement: " + measurement.getMeasurementId());
				measurement.setExposureTemperature(measurementSample.getExposureTemperature());
				measurement.setVolumeToLoad(measurementSample.getVolumeToLoad());
				measurement.setTransmission(measurementSample.getTransmission());
				measurement.setFlow(measurementSample.getFlow());
				measurement.setWaitTime(measurementSample.getWaitTime());
				measurement.setViscosity(measurementSample.getViscosity());
				measurement = this.entityManager.merge(measurement);
			}
			return this.entityManager.merge(measurementSample);
		} else {
			throw new Exception("Measurement  " + measurementSample.getMeasurementId()
					+ " was expected to be in only one data collection and found in: " + measurementToDatacollectionList.size());
		}
	}

	@Override
	public void setMeasurementParameters(List<Integer> measurementIds, String parameter, String value) {
		for (Integer measurementId : measurementIds) {
			Measurement3VO measurement = this.entityManager.find(Measurement3VO.class, measurementId);
			/** Parameter: ["Exp. Temp.", "Volume To Load", "Transmission", "Flow", "Viscosity"] **/
			if (parameter.equals("Exp. Temp.")) {
				measurement.setExposureTemperature(value);
			}
			if (parameter.equals("Volume To Load")) {
				measurement.setVolumeToLoad(value);
			}
			if (parameter.equals("Transmission")) {
				measurement.setTransmission(value);
			}
			if (parameter.equals("Wait Time")) {
				measurement.setWaitTime(value);
			}
			if (parameter.equals("Viscosity")) {
				measurement.setViscosity(value);
			}
			if (parameter.equals("macromoleculeId")) {
				Specimen3VO specimen = this.entityManager.find(Specimen3VO.class, measurement.getSpecimenId());
				Macromolecule3VO macromolecule = this.entityManager.find(Macromolecule3VO.class, Integer.parseInt(value));
				specimen.setMacromolecule3VO(macromolecule);
			}

			if (parameter.equals("bufferId")) {
				Specimen3VO specimen = this.entityManager.find(Specimen3VO.class, measurement.getSpecimenId());
				Buffer3VO buffer = this.entityManager.find(Buffer3VO.class, Integer.parseInt(value));
				specimen.setBufferId(buffer.getBufferId());
			}
		}
	}

	/**
	 * Given two specimens with same content, one is filled with the content of the another
	 * 
	 * 1) Check specimens are the same (same buffer, macromolecule, concentration) 2) Add specimen's volume to the target's specimen 3)
	 * Set measurements of specimen to the target 4) Remove specimen
	 */
	@Override
	public Specimen3VO mergeSpecimens(int targetId, int specimenId) {
		Specimen3VO target = specimen3Service.findSpecimenById(targetId);
		Specimen3VO specimen = specimen3Service.findSpecimenById(specimenId);
		if ((target != null) && (specimen != null)) {
			/** Checking we can merge that specimen **/
			/** Same buffer **/
			if (target.getBufferId().equals(specimen.getBufferId())) {
				if (((target.getMacromolecule3VO() == null) && (specimen.getMacromolecule3VO() == null))
						|| ((specimen.getMacromolecule3VO().getMacromoleculeId().equals(target.getMacromolecule3VO()
								.getMacromoleculeId())))) {

					if (specimen.getConcentration().equals(target.getConcentration())) {

						int volume = Integer.parseInt(specimen.getVolume()) + Integer.parseInt(target.getVolume());
						/** We set new volume **/
						target.setVolume(String.valueOf(volume));

						/** All measurements of the specimen will be linked to the target **/
						for (Measurement3VO measurement : specimen.getMeasurements()) {
							measurement.setSpecimenId(target.getSpecimenId());
							// this.experiment3ServiceLocal.merge(measurement);
							this.measurement3Service.merge(measurement);
						}

						target = this.specimen3Service.merge(target);
						if (specimen.getSampleplateposition3VO() != null) {
							this.entityManager.remove(this.entityManager.merge(specimen.getSampleplateposition3VO()));
						}
						/** Removing specimen **/
						this.entityManager.remove(this.entityManager.merge(specimen));
					}
				}
			}
		}
		return target;
	}

	@Override
	public void emptyPlate(int samplePlateId, int experimentId) {
		Sampleplate3VO samplePlate = this.entityManager.find(Sampleplate3VO.class, samplePlateId);
		Experiment3VO experiment = this.entityManager.find(Experiment3VO.class, experimentId);
		for (Specimen3VO sample : experiment.getSamples()) {
			if (sample.getSampleplateposition3VO() != null) {
				if (sample.getSampleplateposition3VO().getSamplePlateId().equals(samplePlateId)) {
					sample.setSampleplateposition3VO(null);
					this.entityManager.merge(sample);
				}
			}
		}

		Iterator<Sampleplateposition3VO> iterator = samplePlate.getSampleplateposition3VOs().iterator();
		while (iterator.hasNext()) {
			Sampleplateposition3VO s = iterator.next(); // must be called before you can call i.remove()
			this.entityManager.remove(s);
		}

	}

	/** Retrun all samples of the experiment which samplePlatePosition is null **/
	private List<Specimen3VO> getUnlocatedSpecimensByExperimentId(Experiment3VO experiment) {
		List<Specimen3VO> unallocatedSpecimens = new ArrayList<Specimen3VO>();

		for (Specimen3VO specimen : experiment.getSamples()) {
			System.out.println(specimen);
			System.out.println(specimen.getSampleplateposition3VO());
			if (specimen.getSampleplateposition3VO() == null) {
				unallocatedSpecimens.add(specimen);
			}
		}
		return unallocatedSpecimens;
	}

	/**
	 * It sets a sample plate position to all samples of the experiments which are unassigned
	 * 
	 */
	@Override
	public void autoFillPlate(int samplePlateId, int experimentId, final SpecimenComparator... multipleOptions) {
		Sampleplate3VO samplePlate = this.entityManager.find(Sampleplate3VO.class, samplePlateId);
		Experiment3VO experiment = this.entityManager.find(Experiment3VO.class, experimentId);
		List<Specimen3VO> specimensToBeAllocated = this.getUnlocatedSpecimensByExperimentId(experiment);

		/** Getting which positions are already occupied **/
		HashSet<String> nonFreePositions = new HashSet<String>();
		for (Specimen3VO specimen : experiment.getSamples()) {
			if (specimen.getSampleplateposition3VO() != null) {
				/** Specimen is in the same plate, we store its position in the hashmap **/
				if (specimen.getSampleplateposition3VO().getSamplePlateId().equals(samplePlateId)) {
					nonFreePositions.add(specimen.getSampleplateposition3VO().getRowNumber() + "_"
							+ specimen.getSampleplateposition3VO().getColumnNumber());
				}
			}
		}
		Collections.sort(specimensToBeAllocated, SpecimenComparator.compare(SpecimenComparator.getComparator(multipleOptions)));
		if (specimensToBeAllocated.size() > 0) {
			int positionAssignedCount = 0;
			for (int i = 1; i <= samplePlate.getPlatetype3VO().getRowCount(); i++) {
				for (int j = 1; j <= samplePlate.getPlatetype3VO().getColumnCount(); j++) {
					if (positionAssignedCount < specimensToBeAllocated.size()) {
						if (!nonFreePositions.contains(i + "_" + j)) {
							/** Creating sample plate position **/
							Sampleplateposition3VO position = new Sampleplateposition3VO();
							position.setSamplePlateId(samplePlateId);
							position.setRowNumber(i);
							position.setColumnNumber(j);
							specimensToBeAllocated.get(positionAssignedCount).setSampleplateposition3VO(
									this.entityManager.merge(position));
							this.entityManager.merge(specimensToBeAllocated.get(positionAssignedCount));
							positionAssignedCount++;
						}
					}
				}
			}
		}
	}

	@Override
	public void autoFillPlate(int samplePlateId, int experimentId) {
		this.autoFillPlate(samplePlateId, experimentId, SpecimenComparator.MACROMOLECULE, SpecimenComparator.BUFFER,
				SpecimenComparator.CONCENTRATION);
	}

	/**
	 * Removes an experiment if type is TEMPLATE
	 * 
	 * @throws Exception
	 **/
	@Override
	public void removeMeasurementsByExperimentId(int experimentId) throws Exception {
		Experiment3VO experiment = this.entityManager.find(Experiment3VO.class, experimentId);// this.experiment3ServiceLocal.findById(experimentId,
																								// ExperimentScope.MEDIUM);
		if (experiment.getType().equals("TEMPLATE")) {

			/** Removing SaxsDataCollection and MeasurementToDatacollection **/
			for (SaxsDataCollection3VO dataCollection : experiment.getDataCollections()) {
				for (MeasurementTodataCollection3VO measurement : dataCollection.getMeasurementtodatacollection3VOs()) {
					this.entityManager.remove(measurement);
				}
				this.entityManager.remove(dataCollection);
			}

			List<Integer> measurementsId = new ArrayList<Integer>();
			Iterator<Specimen3VO> specimenIterator = experiment.getSamples().iterator();
			while (specimenIterator.hasNext()) {
				Specimen3VO specimen = specimenIterator.next();
				Iterator<Measurement3VO> measurementIterator = specimen.getMeasurements().iterator();
				while (measurementIterator.hasNext()) {
					Measurement3VO measurement = measurementIterator.next();
					// measurementIterator.remove();
					// this.entityManager.remove(measurement);
					measurementsId.add(measurement.getMeasurementId());
				}
				// System.out.println(specimen.getMeasurements().size());
				// this.entityManager.remove(specimen);
			}
			System.out.println(measurementsId);
			for (Integer measurementId : measurementsId) {
				Measurement3VO measuremnet = this.entityManager.find(Measurement3VO.class, measurementId);
				this.entityManager.remove(measuremnet);
			}
			//
			// specimenIterator = experiment.getSamples().iterator();
			// while (specimenIterator.hasNext()) {
			// Specimen3VO specimen = specimenIterator.next();
			// specimen.setMeasurements(null);
			// // System.out.println(specimen.getMeasurements().size());
			// this.entityManager.remove(specimen);
			// }
			//
			// this.removeDataCollections(experiment);
			/** Removing measurements **/
			// this.removeMeasurements(experiment);
			/** Removing specimens **/
			// this.removeSpecimens(experiment);
			/** Removing plates **/
			// this.removePlates(experiment);
			// this.experiment3ServiceLocal.remove(experiment);
		} else {
			throw new Exception("Only experiment of type TEMPLATE can be removed from database");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ispyb.server.biosaxs.services.webUserInterface.WebUserInterfaceService#removeDataCollections(ispyb.server.biosaxs.vos.
	 * dataAcquisition.Experiment3VO)
	 */
	public void removeDataCollections(Experiment3VO experiment) {
		Set<SaxsDataCollection3VO> dataCollections = experiment.getDataCollections();
		for (Iterator<SaxsDataCollection3VO> iterator = dataCollections.iterator(); iterator.hasNext();) {
			SaxsDataCollection3VO dataCollection = iterator.next();
			Set<MeasurementTodataCollection3VO> measurementToDataCollections = dataCollection.getMeasurementtodatacollection3VOs();
			for (Iterator<MeasurementTodataCollection3VO> iterator1 = measurementToDataCollections.iterator(); iterator1.hasNext();) {
				MeasurementTodataCollection3VO measurementToDataColletion = iterator1.next();
				this.measurementToDataCollection3Service.remove(measurementToDataColletion);
				iterator1.remove();
			}
			// this.experiment3ServiceLocal.remove(dataCollection);
			this.measurementToDataCollection3Service.remove(dataCollection);

			iterator.remove();
		}
	}

	public void removeMeasurements(Experiment3VO experiment) {
		Set<Specimen3VO> specimens = experiment.getSamples();
		for (Iterator<Specimen3VO> iterator = specimens.iterator(); iterator.hasNext();) {
			Specimen3VO specimen = iterator.next();
			for (Iterator<Measurement3VO> mIterator = specimen.getMeasurements().iterator(); mIterator.hasNext();) {
				System.out.println("Measurements: " + specimen.getSpecimenId() + " : " + specimen.getMeasurements().size());
				Measurement3VO measurement = mIterator.next();
				mIterator.remove();
				// this.experiment3ServiceLocal.remove(measurement);
				this.measurement3Service.remove(measurement);
			}
		}

		for (Iterator<Specimen3VO> iterator = specimens.iterator(); iterator.hasNext();) {
			Specimen3VO specimen = iterator.next();
			System.out.println("Measurements remainig for specimen: " + specimen.getSpecimenId() + " : "
					+ specimen.getMeasurements().size());
		}
	}

	// public void removeSpecimens(Experiment3VO experiment) {
	// System.out.println("Specimens: " + experiment.getSamples().size());
	// Set<Specimen3VO> specimens = experiment.getSamples();
	// for (Iterator<Specimen3VO> iterator = specimens.iterator(); iterator.hasNext(); ) {
	// Specimen3VO specimen = iterator.next();
	//
	// iterator.remove();
	// this.remove(specimen);
	// }
	//
	// System.out.println("Specimens remaining: " + experiment.getSamples().size());
	// }

	@Override
	public void removePlates(int experimentId) {
		Experiment3VO experiment = this.entityManager.find(Experiment3VO.class, experimentId);// this.experiment3ServiceLocal.findById(experimentId,
																								// ExperimentScope.MEDIUM);
		Set<Sampleplate3VO> plates = experiment.getSamplePlate3VOs();
		List<Integer> samplePlatePositionIds = new ArrayList<Integer>();
		List<Integer> plateIds = new ArrayList<Integer>();
		for (Iterator<Sampleplate3VO> iterator = plates.iterator(); iterator.hasNext();) {
			Sampleplate3VO plate = iterator.next();
			// for (Iterator<Sampleplateposition3VO> iteratorPosition = plate.getSampleplateposition3VOs().iterator();
			// iteratorPosition.hasNext(); ) {
			// Sampleplateposition3VO position = iteratorPosition.next();
			// samplePlatePositionIds.add(position.getSamplePlatePositionId());
			// this.experiment3ServiceLocal.remove(iteratorPosition.next());
			// }
			plateIds.add(plate.getSamplePlateId());
		}
		// iterator.remove();
		// this.experiment3ServiceLocal.remove(plate);
		// }
		// for (int i = 0; i < samplePlatePositionIds.size(); i++) {
		// Sampleplateposition3VO sampleplateposition3VO = this.entityManager.find(Sampleplateposition3VO.class,
		// samplePlatePositionIds.get(i));
		// this.entityManager.remove(sampleplateposition3VO);
		// }

		for (int i = 0; i < plateIds.size(); i++) {
			Sampleplate3VO plate = this.entityManager.find(Sampleplate3VO.class, plateIds.get(i));
			this.entityManager.remove(plate);
		}
	}

	@Override
	public void removeSpecimensByExperimentId(int experimentId) {
		Experiment3VO experiment = this.entityManager.find(Experiment3VO.class, experimentId);// this.experiment3ServiceLocal.findById(experimentId,
																								// ExperimentScope.MEDIUM);
		Set<Specimen3VO> specimens = experiment.getSamples();
		List<Integer> measurementsId = new ArrayList<Integer>();
		for (Iterator<Specimen3VO> iterator = specimens.iterator(); iterator.hasNext();) {
			Specimen3VO specimen = iterator.next();
			measurementsId.add(specimen.getSpecimenId());
		}
		System.out.println(measurementsId);
		for (Integer measurementId : measurementsId) {
			Specimen3VO measuremnet = this.entityManager.find(Specimen3VO.class, measurementId);
			this.remove(measuremnet);
		}
		System.out.println("Specimens remaining: " + experiment.getSamples().size());

		Set<Sampleplate3VO> plates = experiment.getSamplePlate3VOs();
		List<Integer> samplePlatePositionIds = new ArrayList<Integer>();
		for (Iterator<Sampleplate3VO> iterator = plates.iterator(); iterator.hasNext();) {
			Sampleplate3VO plate = iterator.next();
			for (Iterator<Sampleplateposition3VO> iteratorPosition = plate.getSampleplateposition3VOs().iterator(); iteratorPosition
					.hasNext();) {
				Sampleplateposition3VO position = iteratorPosition.next();
				samplePlatePositionIds.add(position.getSamplePlatePositionId());
			}
		}
		for (int i = 0; i < samplePlatePositionIds.size(); i++) {
			try {
				Sampleplateposition3VO sampleplateposition3VO = this.entityManager.find(Sampleplateposition3VO.class,
						samplePlatePositionIds.get(i));
				if (sampleplateposition3VO != null) {
					this.entityManager.remove(sampleplateposition3VO);
				}
			} catch (Exception exp) {
				System.out.println("Sample plate position not found");
			}

		}
	}

	@Override
	public Specimen3VO merge(Specimen3VO specimen3vo) {
		Sampleplateposition3VO samplePlatePosition = null;
		if (specimen3vo.getSampleplateposition3VO() != null) {
			samplePlatePosition = this.entityManager.merge(specimen3vo.getSampleplateposition3VO());
			specimen3vo.setSampleplateposition3VO(samplePlatePosition);
		}
		specimen3vo = this.specimen3Service.merge(specimen3vo);
		specimen3vo.setSampleplateposition3VO(samplePlatePosition);
		return specimen3vo;
	}

}
