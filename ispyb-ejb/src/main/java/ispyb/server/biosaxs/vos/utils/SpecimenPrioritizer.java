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

package ispyb.server.biosaxs.vos.utils;

import ispyb.server.biosaxs.services.core.experiment.Experiment3Service;
import ispyb.server.biosaxs.services.core.measurement.Measurement3Service;
import ispyb.server.biosaxs.services.core.measurementToDataCollection.MeasurementToDataCollection3Service;
import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;

public class SpecimenPrioritizer {

	private SaxsProposal3Service macromolecule3Service;
	private Experiment3Service experiment3Service;
	private Measurement3Service specimen3Service;
	private MeasurementToDataCollection3Service measurementToDataCollection3Service;
	
	public SpecimenPrioritizer( SaxsProposal3Service macromolecule3Service,
	 							Measurement3Service specimen3Service,
	 							Experiment3Service experiment3Service,
	 							MeasurementToDataCollection3Service measurementToDataCollection3Service){
		
		this.macromolecule3Service = macromolecule3Service;
		this.experiment3Service = experiment3Service;
		this.specimen3Service = specimen3Service;
		this.measurementToDataCollection3Service = measurementToDataCollection3Service;
		
	}
//	
//	public void doPriorities(Experiment3VO experiment, int proposalId, List<Integer> macromoleculeIds, List<Integer> samplePlateIds, SpecimenComparator temperature, SpecimenComparator concentration) throws Exception {
//	
//		/** HashMap samplePlateIds **/
//		HashSet<Integer> plateIds = new HashSet<Integer>();
//		for (Integer id : samplePlateIds) {
//			plateIds.add(id);
//		}
//		
//		
//		List<Measurement3VO> specimenByExperimentList = experiment.getMeasurements();
//
//		
//		/** Filtering by plates **/
////		if (samplePlateIds.size() > 0){
//			specimenByExperimentList = new ArrayList<Measurement3VO>();
//			for (Measurement3VO specimen3vo : experiment.getMeasurements()) {
//				Specimen3VO sample = experiment.getSampleById(specimen3vo.getSpecimenId());
////				if (sample.getSampleplateposition3VO() != null){
////					if (samplePlateIds.contains(sample.getSampleplateposition3VO().getSamplePlateId())){
//						specimenByExperimentList.add(specimen3vo);
////					}
////				}
//			}
////		}
//
//			
//		List<Measurement3VO> specimensForPriorityMacromolecules = new ArrayList<Measurement3VO>();
//		/** Macromolecules with high priority **/
//		List<Macromolecule3VO> priorityMacromoles = new ArrayList<Macromolecule3VO>();
//		for (Integer macromoleculeId : macromoleculeIds) {
//			priorityMacromoles.add(this.getMacromoleculeById(proposalId, macromoleculeId));
//			
//		}
//		
//		/** Comparators **/
//		SpecimenComparator temperatureComparator = temperature;//SpecimenComparator.EXPOSURE_TEMPERATURE_SORT_ASC;
//		SpecimenComparator concentrationComparator = concentration;//SpecimenComparator.CONCENTRATION_SORT_ASC;
//		
//		
//		ArrayList<Measurement3VO> prioritySpecimens = new ArrayList<Measurement3VO>();
//
//		Iterator<Macromolecule3VO> priorityMacromoleculesIterator = priorityMacromoles.iterator();
//		while (priorityMacromoleculesIterator.hasNext()) {
//			Macromolecule3VO macromolecule3VO = priorityMacromoleculesIterator.next();
//			Iterator<Measurement3VO> specimenIterator = specimenByExperimentList.iterator();
//			while (specimenIterator.hasNext()) {
//				Measurement3VO specimen = specimenIterator.next();
//				Specimen3VO sample = experiment.getSampleById(specimen.getSpecimenId());
//				if (sample.getMacromolecule3VO() != null) {
//					if (sample.getMacromolecule3VO().getMacromoleculeId().equals(macromolecule3VO.getMacromoleculeId())) {
//						prioritySpecimens.add(specimen);
//						specimenIterator.remove();
//					}
//				}
//				Collections.sort(prioritySpecimens, SpecimenComparator.compare(SpecimenComparator.getComparator(temperatureComparator,concentrationComparator)));
//			}
//			for (Measurement3VO specimen : prioritySpecimens) {
//				specimensForPriorityMacromolecules.add(specimen);
//			}
//			/** removing this array for sorting later on **/
//			prioritySpecimens = new ArrayList<Measurement3VO>();
//		}
//	
//		Collections.sort(specimenByExperimentList, SpecimenComparator.compare(SpecimenComparator.getComparator(temperatureComparator, concentrationComparator)));
//		
//		System.out.println(specimensForPriorityMacromolecules.size());
//		for (Measurement3VO specimen3vo : specimensForPriorityMacromolecules) {
//			System.out.println(specimen3vo);
//		}
//		
//		specimensForPriorityMacromolecules.addAll(specimenByExperimentList);
//		
//		/** I have sorted the specimens in the specimenByExperimentList but I want the datacollections **/
//		List<SaxsDataCollection3VO> dataCollectionList = new ArrayList<SaxsDataCollection3VO>();
//		for (Measurement3VO specimen3vo : specimensForPriorityMacromolecules) {
//			SaxsDataCollection3VO dataCollection = experiment.getDataCollectionByMeasurementId(specimen3vo.getSpecimenId());
//			boolean found = false;
//			for (SaxsDataCollection3VO saxsDataCollection3VO : dataCollectionList) {
//				if (dataCollection.getDataCollectionId().equals(saxsDataCollection3VO.getDataCollectionId())){
//					found = true;
//				}
//			}
//			if (!found){
//				dataCollectionList.add(dataCollection);
//			}
//		}
//		setSpecimensListPriorities(experiment, dataCollectionList, 1);
//}
//
//public static Comparator<MeasurementTodataCollection3VO> MeasurementTodataCollectionComparatorOrder = new Comparator<MeasurementTodataCollection3VO>()
//{
//    public int compare(MeasurementTodataCollection3VO o1, MeasurementTodataCollection3VO o2) {
//        return o1.getDataCollectionOrder() - o2.getDataCollectionOrder();
//    }
//};
//	  
//private List<SaxsDataCollection3VO> resetAllPriorities(Experiment3VO experiment, List<SaxsDataCollection3VO> dataCollectionList, int priority) {
//	/**Reset all priorities **/
//	for (SaxsDataCollection3VO dataCollection : dataCollectionList) {
//		/** Getting measurements for each data collection**/
//		Set<MeasurementTodataCollection3VO> measurements = dataCollection.getMeasurementtodatacollection3VOs();
//		List<MeasurementTodataCollection3VO> measurementsList = Arrays.asList(measurements.toArray(new MeasurementTodataCollection3VO[measurements.size()]));
//		/** Sorting list by order **/
//		Collections.sort(measurementsList, MeasurementTodataCollectionComparatorOrder);
//		for (MeasurementTodataCollection3VO measurementTodataCollection3VO : measurementsList) {
//			System.out.println(measurementTodataCollection3VO.toString());
//			Measurement3VO specimen = experiment.getSpecimenById(measurementTodataCollection3VO.getMeasurementId());
//			specimen.setPriority(null);
//			this.specimen3Service.merge(specimen);
//		}
//	}
//	return dataCollectionList;
//}
//
//public int optimizeDatacollectionByRemovingDuplicatedBuffers(Experiment3VO experiment, List<SaxsDataCollection3VO> dataCollectionList, int priority) {
//	/** Setting priorities **/
//	Measurement3VO previousSpecimen = null;
//	for (SaxsDataCollection3VO dataCollection : dataCollectionList) {
//		/** Getting measurements for each data collection**/
//		Set<MeasurementTodataCollection3VO> measurements = dataCollection.getMeasurementtodatacollection3VOs();
//		List<MeasurementTodataCollection3VO> measurementsList = Arrays.asList(measurements.toArray(new MeasurementTodataCollection3VO[measurements.size()]));
//		/** Sorting list by order **/
//		Collections.sort(measurementsList, MeasurementTodataCollectionComparatorOrder);
//		for (MeasurementTodataCollection3VO measurementTodataCollection3VO : measurementsList) {
//			Measurement3VO currentSpecimen = experiment.getSpecimenById(measurementTodataCollection3VO.getMeasurementId());
//			System.out.println(currentSpecimen);
//			
//			if (currentSpecimen.getPriority() != null){
//				System.out.println("\t\t|--> This specimens has been already used, so it has been optimized");
//				System.out.println("\t\t|--> Duplicated: " + previousSpecimen.getSpecimenId());
//				if (this.compare(currentSpecimen, previousSpecimen)){
//					System.out.println("\t\t|--> It is the previous one, so no problem then");
//				}
//				else{
//					System.out.println("\t\t|--> Duplicating specimen");
//					currentSpecimen.setSpecimenId(null);
//					currentSpecimen = this.specimen3Service.merge(currentSpecimen);
//					System.out.println("\t\t|--> New id is " + currentSpecimen.getSpecimenId());
//					System.out.println("\t\t|--> Updating Measurement: " + previousSpecimen.getSpecimenId());
//					measurementTodataCollection3VO.setSpecimenId( currentSpecimen.getSpecimenId());
//					measurementToDataCollection3Service.merge(measurementTodataCollection3VO);
//					
//					System.out.println("\t\t|--> Setting priority ");
//					currentSpecimen.setPriority(priority);
//					priority++;
//					currentSpecimen = this.specimen3Service.merge(currentSpecimen);
//					previousSpecimen = currentSpecimen;
//					experiment = this.experiment3Service.findAllById(experiment.getExperimentId());
//				}
//				continue;
//			}
//			if (previousSpecimen != null){
//				if (this.compare(currentSpecimen, previousSpecimen)){
//					System.out.println("\t\t|--> This specimens could be optimized");
//					System.out.println("\t\t|--> Previous Specimen Id: " + previousSpecimen.getSpecimenId());
//					System.out.println("\t\t|--> Current Specimen Id: " + currentSpecimen.getSpecimenId());
//					System.out.println("\t\t|--> Updating Measurement: " + previousSpecimen.getSpecimenId());
//					measurementTodataCollection3VO.setSpecimenId(previousSpecimen.getSpecimenId());
//					measurementToDataCollection3Service.merge(measurementTodataCollection3VO);
//						System.out.println("\t\t|--> Removing Specimen Id: " + currentSpecimen.getSpecimenId());
//						try{
//							this.specimen3Service.remove(currentSpecimen);
//						}
//						catch(Exception exp){
//							System.out.println(experiment.getDataCollectionListByMeasurementId(currentSpecimen.getSpecimenId()).size());
//							System.out.println("******************************  couldnt be removed");
//						}
//					continue;
//				}
//			}
//			
//			currentSpecimen.setPriority(priority);
//			priority++;
//			currentSpecimen = this.specimen3Service.merge(currentSpecimen);
//			previousSpecimen = currentSpecimen;
//		}
//	}
//	return priority;
//	
//}
//
//public int setSpecimensListPriorities(Experiment3VO experiment, List<SaxsDataCollection3VO> dataCollectionList, int priority) {
//	dataCollectionList = this.resetAllPriorities(experiment, dataCollectionList, priority);
//	experiment = this.experiment3Service.findAllById(experiment.getExperimentId());
//	return this.optimizeDatacollectionByRemovingDuplicatedBuffers(experiment, dataCollectionList, priority);
//}
//
//
//private boolean compare(Measurement3VO specimen, Measurement3VO specimenAfter){
//	if (specimen.getSampleId() != specimenAfter.getSampleId()){
//		return false;
//	} 
//	if (!specimen.getTransmission().equals(specimenAfter.getTransmission())){
//		return false;
//	} 
//	if (!specimen.getViscosity().equals(specimenAfter.getViscosity())){
//		return false;
//	} 
//	if (!specimen.getFlow().equals(specimenAfter.getFlow())){
//		return false;
//	} 
//	if (!specimen.getExtraFlowTime().equals(specimenAfter.getExtraFlowTime())){
//		return false;
//	} 
//	
//	if (!specimen.getExposureTemperature().equals(specimenAfter.getExposureTemperature())){
//		return false;
//	} 
//	
////	if (!specimen.getConcentration().equals(specimenAfter.getConcentration())){
////		return false;
////	} 
//	
//	if (!specimen.getVolumeToLoad().equals(specimenAfter.getVolumeToLoad())){
//		return false;
//	} 
//	
//	if (!specimen.getWaitTime().equals(specimenAfter.getWaitTime())){
//		return false;
//	} 
//	return true;
//}
//
//
//private Macromolecule3VO getMacromoleculeById(int proposalId, int macromoleculeId){
//	List<Macromolecule3VO> macromolecules = this.macromolecule3Service.findMacromoleculesByProposalId(proposalId);
//	for (Macromolecule3VO macromolecule3vo : macromolecules) {
//		if (macromolecule3vo.getMacromoleculeId().equals(macromoleculeId)){
//			return macromolecule3vo;
//		}
//	}
//	return null;
//}
//
//

}
