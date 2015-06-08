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

package ispyb.server.biosaxs.services.core.analysis.hplc;


import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3ServiceLocal;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Measurement3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Specimen3VO;
import ispyb.server.biosaxs.vos.datacollection.MeasurementTodataCollection3VO;
import ispyb.server.biosaxs.vos.datacollection.SaxsDataCollection3VO;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;



@Stateless
public class HPLCDataProcessing3ServiceBean implements HPLCDataProcessing3Service, HPLCDataProcessing3ServiceLocal {

	private final static Logger LOG = Logger.getLogger("Analysis3ServiceBean");
	
	/** The experiment3 service local. */
	
	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@EJB
	private SaxsProposal3ServiceLocal saxsProposal3ServiceLocal;
	
	/** The now. */
	protected static Calendar NOW;

	private Date getNow(){
		 NOW = GregorianCalendar.getInstance();
		 return NOW.getTime();
	}

	private Macromolecule3VO createMacromolecule(String macromoleculeAcronym, int proposalId ){
		List<Macromolecule3VO> macromolecules = saxsProposal3ServiceLocal.findMacromoleculesBy(macromoleculeAcronym, proposalId);
		Macromolecule3VO macromolecule = new Macromolecule3VO();
		if (macromolecules.size() == 0){
			/** Creating macromolecule **/
			macromolecule.setAcronym(macromoleculeAcronym);
			macromolecule.setName(macromoleculeAcronym);
			macromolecule.setProposalId(proposalId);
			macromolecule = entityManager.merge(macromolecule);
		}
		else{
			macromolecule = macromolecules.get(0);
		}
		
		return macromolecule;
	}
	
	/**
	 * @param bufferAcronym
	 * @param proposalId
	 * @return
	 */
	private Buffer3VO createBuffer(String bufferAcronym, int proposalId) {
		List<Buffer3VO> buffers = saxsProposal3ServiceLocal.findBuffersByProposalId(proposalId);
		Buffer3VO buffer = null;
		for (Buffer3VO buffer3vo : buffers) {
			if (buffer3vo.getAcronym().equals(bufferAcronym)){
				return buffer3vo;
			}
		}
		
		buffer = new Buffer3VO();
		/** Creating macromolecule **/
		buffer.setAcronym(bufferAcronym);
		buffer.setName(bufferAcronym);
		buffer.setProposalId(proposalId);
		buffer = entityManager.merge(buffer);
		return buffer;
	}
	
	
	
	@Override
	public SaxsDataCollection3VO createDatacollection(Experiment3VO experiment, String macromoleculeAcronym, String bufferAcronym, int proposalId) {
		
		Macromolecule3VO macromolecule = this.createMacromolecule(macromoleculeAcronym, proposalId);
		Buffer3VO buffer = this.createBuffer(bufferAcronym, proposalId);
		
		/** Creating specimens **/
		Specimen3VO specimenBuffer = new Specimen3VO();
		specimenBuffer.setBufferId(buffer.getBufferId());
		specimenBuffer.setConcentration("0");
		specimenBuffer.setExperimentId(experiment.getExperimentId());
		specimenBuffer = entityManager.merge(specimenBuffer);
		
		Specimen3VO specimenSample = new Specimen3VO();
		specimenSample.setBufferId(buffer.getBufferId());
		specimenSample.setMacromolecule3VO(macromolecule);
		specimenSample.setConcentration("0");
		specimenSample.setExperimentId(experiment.getExperimentId());
		specimenSample = entityManager.merge(specimenSample);
		
		/** Creating Datacollections **/
		SaxsDataCollection3VO dataCollection = new SaxsDataCollection3VO();
		dataCollection.setExperimentId(experiment.getExperimentId());
		dataCollection = entityManager.merge(dataCollection);
		
		/** Creating measurements **/
		Measurement3VO measurementBufferBefore = new Measurement3VO();
		measurementBufferBefore.setSpecimenId(specimenBuffer.getSpecimenId());
		measurementBufferBefore = entityManager.merge(measurementBufferBefore);
		
		Measurement3VO measurementSample = new Measurement3VO();
		measurementSample.setSpecimenId(specimenSample.getSpecimenId());
		measurementSample = entityManager.merge(measurementSample);
		
		Measurement3VO measurementBufferAfter = new Measurement3VO();
		measurementBufferAfter.setSpecimenId(specimenBuffer.getSpecimenId());
		measurementBufferAfter = entityManager.merge(measurementBufferAfter);
		
		
		MeasurementTodataCollection3VO measurementTodataCollectionBufferBefore = new MeasurementTodataCollection3VO();
		measurementTodataCollectionBufferBefore.setDataCollectionOrder(1);
		measurementTodataCollectionBufferBefore.setDataCollectionId(dataCollection.getDataCollectionId());
		measurementTodataCollectionBufferBefore.setMeasurementId(measurementBufferBefore.getMeasurementId());
		measurementTodataCollectionBufferBefore = this.entityManager.merge(measurementTodataCollectionBufferBefore);
		
		MeasurementTodataCollection3VO measurementTodataCollectionSample = new MeasurementTodataCollection3VO();
		measurementTodataCollectionSample.setDataCollectionOrder(2);
		measurementTodataCollectionSample.setDataCollectionId(dataCollection.getDataCollectionId());
		measurementTodataCollectionSample.setMeasurementId(measurementSample.getMeasurementId());
		measurementTodataCollectionSample = this.entityManager.merge(measurementTodataCollectionSample);
		
		MeasurementTodataCollection3VO measurementTodataCollectionBufferAfter = new MeasurementTodataCollection3VO();
		measurementTodataCollectionBufferAfter.setDataCollectionOrder(3);
		measurementTodataCollectionBufferAfter.setDataCollectionId(dataCollection.getDataCollectionId());
		measurementTodataCollectionBufferAfter.setMeasurementId(measurementBufferAfter.getMeasurementId());
		measurementTodataCollectionBufferAfter = this.entityManager.merge(measurementTodataCollectionBufferAfter);
		
		dataCollection.getMeasurementtodatacollection3VOs().add(measurementTodataCollectionBufferBefore);
		dataCollection.getMeasurementtodatacollection3VOs().add(measurementTodataCollectionSample);
		dataCollection.getMeasurementtodatacollection3VOs().add(measurementTodataCollectionBufferAfter);
		
		experiment.getDataCollections().add(dataCollection);
		dataCollection = entityManager.merge(dataCollection);
		return dataCollection;
	}

	
	
	
}
