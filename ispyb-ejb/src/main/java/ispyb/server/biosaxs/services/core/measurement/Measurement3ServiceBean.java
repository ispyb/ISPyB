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

package ispyb.server.biosaxs.services.core.measurement;



import ispyb.server.biosaxs.services.core.measurementToDataCollection.MeasurementToDataCollection3Service;
import ispyb.server.biosaxs.services.sql.SQLQueryKeeper;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Measurement3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Specimen3VO;
import ispyb.server.biosaxs.vos.datacollection.MeasurementTodataCollection3VO;
import ispyb.server.biosaxs.vos.datacollection.SaxsDataCollection3VO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class Specimen3VO.
 * 
 * @see ispyb.server.biosaxs.vos.Measurement3VO.project.Specimen3VO
 * @author Hibernate Tools
 */
@Stateless
public class Measurement3ServiceBean implements Measurement3Service, Measurement3ServiceLocal {

	private final static Logger log = Logger.getLogger(Measurement3ServiceBean.class);

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;
	
	@EJB
	private MeasurementToDataCollection3Service measurementToDataCollection3Service;

	public Specimen3VO findSpecimenById(int specimenId) {
		String query = SQLQueryKeeper.getSpecimenById(specimenId);
		Query EJBQuery = this.entityManager.createQuery(query);
		Specimen3VO Specimen3VO = (Specimen3VO) EJBQuery.getSingleResult();	
		return Specimen3VO;
	}
	
	@Override
	public List<Measurement3VO> findMeasurementBySpecimenId(int specimenId) {
		Specimen3VO specimen = this.findSpecimenById(specimenId);
		List<Measurement3VO> measurements = new ArrayList<Measurement3VO>();
		measurements.addAll(specimen.getMeasurements());
		return measurements;
	}
	
	@Override
	public void persist(Measurement3VO transientInstance) {
		log.debug("persisting Specimen3VO instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	
	@Override
	public void remove(Measurement3VO persistentInstance) {
		log.debug("removing Specimen3VO instance");
		try {
			Measurement3VO instance = entityManager.find(Measurement3VO.class, persistentInstance.getMeasurementId());
			entityManager.remove(instance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	@Override
	public Measurement3VO merge(Measurement3VO detachedInstance) {
		log.debug("merging Specimen3VO instance");
		try {
			Measurement3VO result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	@Override
	public Measurement3VO findById(Integer id) {
		log.debug("getting Specimen3VO instance with id: " + id);
		try {
			Measurement3VO instance = entityManager.find(Measurement3VO.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<MeasurementTodataCollection3VO> test(String ejbQL) {
		try {
			TypedQuery<MeasurementTodataCollection3VO> query = entityManager.createQuery(ejbQL, MeasurementTodataCollection3VO.class);
			return query.getResultList();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}


	@Override
	public Measurement3VO findMeasurementByCode(int experimentId, String code) {
		Experiment3VO experiment = entityManager.find(Experiment3VO.class, experimentId);
		List<Measurement3VO> measurements = experiment.getMeasurements();
		for (Measurement3VO measurement3vo : measurements) {
			if (measurement3vo.getCode().trim().toLowerCase().equals(code.trim().toLowerCase())){
				return measurement3vo;
			}
		}
		return null;
	}
	
	
	/**
	 * Optimize datacollection by removing duplicated buffers. Set priorities starting from the priority integer passed as parameter
	 * 
	 * @param experiment
	 *            the experiment
	 * @param dataCollectionList
	 *            the data collection list, this is the order that the priority will be assigned
	 * @param priority
	 *            the priority, the starting priority.
	 * @return the int
	 */
	@Override
	public int optimizeDatacollectionByRemovingDuplicatedBuffers(Experiment3VO experiment, List<SaxsDataCollection3VO> dataCollectionList, int priority) {
		return optimizeDatacollectionByRemovingDuplicatedBuffers(experiment, dataCollectionList, priority, false);
	}
	
	private int optimizeDatacollectionByRemovingDuplicatedBuffers(Experiment3VO experiment, List<SaxsDataCollection3VO> dataCollectionList, int priority, boolean specimenDistinction) {
		/** Setting priorities **/
		Measurement3VO previousMeasurement = null;
		for (SaxsDataCollection3VO dataCollection : dataCollectionList) {
			/** Getting measurements for each data collection**/
			Set<MeasurementTodataCollection3VO> measurements = dataCollection.getMeasurementtodatacollection3VOs();
			List<MeasurementTodataCollection3VO> measurementsList = Arrays.asList(measurements.toArray(new MeasurementTodataCollection3VO[measurements.size()]));
			/** Sorting list by order **/
			Collections.sort(measurementsList, MeasurementTodataCollectionComparatorOrder);
			
			for (MeasurementTodataCollection3VO measurementTodataCollection3VO : measurementsList) {
				Measurement3VO measurement = experiment.getMeasurementById(measurementTodataCollection3VO.getMeasurementId());
				if (measurement.getPriority() != null){
					log.debug("\t\t|--> This specimens has been already used, so it has been optimized");
					log.debug("\t\t|--> Duplicated: " + previousMeasurement.getMeasurementId());

					if (this.compare(measurement, previousMeasurement, experiment, specimenDistinction)){
						log.debug("\t\t|--> It is the previous one, so no problem then");
					}
					else{
						log.debug("\t\t|--> Duplicating specimen");
						measurement.setMeasurementId(null);
						
						/** DONE **/
						measurement = this.merge(measurement);
						experiment.getSampleById(measurement.getSpecimenId()).getMeasurements().add(measurement);
						
						log.debug("\t\t|--> New id is " + measurement.getMeasurementId());
						log.debug("\t\t|--> Updating Measurement: " + previousMeasurement.getMeasurementId());
						measurementTodataCollection3VO.setMeasurementId( measurement.getMeasurementId());
						
						/** --- **/
						measurementTodataCollection3VO = measurementToDataCollection3Service.merge(measurementTodataCollection3VO);
						
						log.debug("\t\t|--> Setting priority ");
						measurement.setPriority(priority);
						priority++;
						
						measurement = this.merge(measurement);
						previousMeasurement = measurement;
						
					}
					continue;
				}
				if (previousMeasurement != null){
					if (this.compare(measurement, previousMeasurement, experiment, specimenDistinction)){
						log.debug("\t\t|--> This specimens could be optimized");
						log.debug("\t\t|--> Previous Specimen Id: " + previousMeasurement.getMeasurementId());
						log.debug("\t\t|--> Current Specimen Id: " + measurement.getMeasurementId());
						log.debug("\t\t|--> Updating Measurement: " + previousMeasurement.getMeasurementId());
						measurementTodataCollection3VO.setMeasurementId(previousMeasurement.getMeasurementId());
						/** --- **/
						measurementTodataCollection3VO = measurementToDataCollection3Service.merge(measurementTodataCollection3VO);
							log.debug("\t\t|--> Removing Specimen Id: " + measurement.getMeasurementId());
							try{
								this.remove(measurement);
								experiment.getSampleById(measurement.getSpecimenId()).getMeasurements().remove(measurement);
							}
							catch(Exception exp){
								log.warn(experiment.getDataCollectionListByMeasurementId(measurement.getMeasurementId()).size());
								log.warn("******************************  couldn't be removed. Exception catched");
							}
						continue;
					}
				}
				
				measurement.setPriority(priority);
				priority++;
				
				/** --- **/
				measurement = this.merge(measurement);
				previousMeasurement = measurement;
			}
		}
		return priority;
		
		}
	
	
	/**
	 * Reset all priorities. Set all the priorities of all the specimen of the data collection to null
	 * 
	 * @param experiment
	 *            the experiment
	 * @param dataCollectionList
	 *            the data collection list
	 * @return the list
	 */
	@Override
	public List<SaxsDataCollection3VO> resetAllPriorities(Experiment3VO experiment, List<SaxsDataCollection3VO> dataCollectionList) {
		/**Reset all priorities **/
		for (SaxsDataCollection3VO dataCollection : dataCollectionList) {
			/** Getting measurements for each data collection**/
			Set<MeasurementTodataCollection3VO> measurements = dataCollection.getMeasurementtodatacollection3VOs();
			List<MeasurementTodataCollection3VO> measurementsList = Arrays.asList(measurements.toArray(new MeasurementTodataCollection3VO[measurements.size()]));
			/** Sorting list by order **/
			Collections.sort(measurementsList, MeasurementTodataCollectionComparatorOrder);
			for (MeasurementTodataCollection3VO measurementTodataCollection3VO : measurementsList) {
				Measurement3VO specimen = experiment.getMeasurementById(measurementTodataCollection3VO.getMeasurementId());
				specimen.setPriority(null);
				//specimen = this.specimen3ServiceLocal.merge(specimen);
				specimen = this.entityManager.merge(specimen);
			}
		}
		return dataCollectionList;
	}
	
	/** Sort a data collection by its dataCollectionOrder field **/
	private static Comparator<MeasurementTodataCollection3VO> MeasurementTodataCollectionComparatorOrder = new Comparator<MeasurementTodataCollection3VO>()
	{
	    public int compare(MeasurementTodataCollection3VO o1, MeasurementTodataCollection3VO o2) {
	        return o1.getDataCollectionOrder() - o2.getDataCollectionOrder();
	    }
	};
	

	/**
	 * @param currentSpecimen
	 * @param previousSpecimen
	 * @param experiment
	 * @param specimenDistinction
	 * @return
	 */
	private boolean compare(Measurement3VO currentSpecimen,Measurement3VO previousSpecimen, Experiment3VO experiment,boolean specimenDistinction) {
		if (!specimenDistinction){
			return this.compare(currentSpecimen, previousSpecimen);
		}
		return this.compare(currentSpecimen, previousSpecimen, experiment);
	}

	/**
	 * Compare. To be used with Collect using robot WS because we measurements are linked to specimens so two specimens will be different if specimenId is different
	 * even if actually they contain the same substance
	 * 
	 * @param specimen
	 *            the specimen
	 * @param specimenAfter
	 *            the specimen after
	 * @return true, if successful
	 */
	private boolean compare(Measurement3VO specimen, Measurement3VO specimenAfter){
		if (specimen.getSpecimenId() != specimenAfter.getSpecimenId()){
			return false;
		} 
		if (!specimen.getTransmission().equals(specimenAfter.getTransmission())){
			return false;
		} 
		if (!specimen.getViscosity().equals(specimenAfter.getViscosity())){
			return false;
		} 
		if (!specimen.getFlow().equals(specimenAfter.getFlow())){
			return false;
		} 
		if (!specimen.getExtraFlowTime().equals(specimenAfter.getExtraFlowTime())){
			return false;
		} 
		
		if (!specimen.getExposureTemperature().equals(specimenAfter.getExposureTemperature())){
			return false;
		} 
		
		if (!specimen.getVolumeToLoad().equals(specimenAfter.getVolumeToLoad())){
			return false;
		} 
		
		if (!specimen.getWaitTime().equals(specimenAfter.getWaitTime())){
			return false;
		} 
		return true;
	}


	/**
	 * Compare. This method doesn't return true if the samples Id are different but they are pointing to a specimen with the same macromolecule and buffer
	 * Use for the use interface and NOT when collect using robot interface with BsxCube
	 * 
	 * @param specimen
	 *            the specimen
	 * @param measurementAfter
	 *            the specimen after
	 * @param experiment 
	 * @return true, if successful
	 */
	private boolean compare(Measurement3VO measurement, Measurement3VO measurementPrevious, Experiment3VO experiment){
		Specimen3VO previousSpecimen = experiment.getSampleById(measurementPrevious.getSpecimenId());
		Specimen3VO current = experiment.getSampleById(measurement.getSpecimenId());
		
		if (previousSpecimen.getBufferId().intValue() != current.getBufferId().intValue()){
			return false;
		}
			
		if ((previousSpecimen.getMacromolecule3VO() == null) && (current.getMacromolecule3VO() != null)){
			return false;
		}
		
		if ((previousSpecimen.getMacromolecule3VO() != null) && (current.getMacromolecule3VO() == null)){
			return false;
		}
		
		if ((previousSpecimen.getMacromolecule3VO() != null) && (current.getMacromolecule3VO() != null)){
			if ((previousSpecimen.getMacromolecule3VO().getMacromoleculeId() != current.getMacromolecule3VO().getMacromoleculeId())){
				return false;
			}
		}
		
		if (!measurement.getExposureTemperature().equals(measurementPrevious.getExposureTemperature())){
			return false;
		} 
		
		return true;
	}

	private Macromolecule3VO findMacromoleculeBySpecimenId(int specimenId){
		String query = "SELECT specimen FROM Specimen3VO specimen LEFT JOIN specimen.macromolecule3VO where specimen.specimenId = " + specimenId;
		Query EJBQuery = this.entityManager.createQuery(query);
		Specimen3VO specimen3VO = (Specimen3VO) EJBQuery.getSingleResult();	
		return specimen3VO.getMacromolecule3VO();
	}
	
	
	@Override
	public Macromolecule3VO findMacromoleculeByCode(int experimentId, String runNumber) throws Exception {
		Measurement3VO measurement = this.findMeasurementByCode(experimentId, runNumber);
		if (measurement != null){
			Macromolecule3VO macromolecule = this.findMacromoleculeBySpecimenId(measurement.getSpecimenId());
			if (macromolecule != null){
				return macromolecule;
			}
			else{
				throw new Exception("macromolecule not found");
			}
			
			
		}
		throw new Exception("Measurement not found");
		
	}


}
