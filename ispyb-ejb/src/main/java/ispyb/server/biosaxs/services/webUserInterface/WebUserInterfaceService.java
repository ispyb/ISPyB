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

import java.util.List;

import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Measurement3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Specimen3VO;
import ispyb.server.biosaxs.vos.utils.comparator.SaxsDataCollectionComparator;
import ispyb.server.biosaxs.vos.utils.comparator.SpecimenComparator;

import javax.ejb.Remote;


@Remote
public interface WebUserInterfaceService {

	/**
	 * This method only will remove measurements associated to a macromolecule and will remove data collection
	 * 
	 * 1.- Look for MeasurementToDataCollection for the measurement. If it is linked to several we will throw an exception as any sample can be associated to several data collections....
	 * 2.- Look for the SaxsDataCollection3VO associated to the measurement
	 * 3.- Look for the other MeasurementTodataCollection3VO associated to this SaxsDataCollection3VO
	 * 4.- For each MeasurementTodataCollection3VO 
	 * 				if MeasurementTodataCollection3VO belongs to another SaxsDataCollection3VO we will remove MeasurementTodataCollection3VO object but not Measurement3VO
	 * 						otherwise we will remove MeasurementTodataCollection3VO and Measurement3VO
	 * 
	 * 5.- If measurement's specimen has no other measurement we will remove it
	 * 	
	 * 
	 * @throws Exception 
	 */
	public void removeDataCollectionByMeasurement(Measurement3VO measurement3vo) throws Exception;

	/**
	 * 
	 * @param dataColectionId
	 * @return the list of specimens that belong to that data collection
	 */
	public List<Specimen3VO> getSpecimenByDataCollectionId(int dataCollectionId);

	/**
	 * Remove the specimen and the sample plate position
	 */
	public void remove(Specimen3VO specimen3vo);

	/**
	 * @param measurement3vo
	 * @return 
	 * @throws Exception 
	 */
	public Measurement3VO merge(Measurement3VO measurement3vo) throws Exception;

	/**
	 * For a given array list of measurement Id set the parameter to value
	 * @param measurementIds
	 * @param parameter
	 * @param value
	 */
	public void setMeasurementParameters(List<Integer> measurementIds,String parameter, String value);

	/**
	 * @param targetId
	 * @param specimenId
	 */
	public Specimen3VO mergeSpecimens(int targetId, int specimenId);

	/**
	 * @param samplePlateId
	 * @param experimentId
	 */
	public void emptyPlate(int samplePlateId, int experimentId);

	/**
	 * @param samplePlateId
	 * @param experimentId
	 */
	public void autoFillPlate(int samplePlateId, int experimentId);

	/**
	 * @param samplePlateId
	 * @param experimentId
	 * @param multipleOptions
	 */
	void autoFillPlate(int samplePlateId, int experimentId, final SpecimenComparator... multipleOptions);

	/**
	 * @param findById
	 * @throws Exception 
	 */
	public void removeMeasurementsByExperimentId(int experimentId) throws Exception;

	/**
	 * @param experimentId
	 */
	public void removeSpecimensByExperimentId(int experimentId);

	/**
	 * @param experiment
	 */
	void removePlates(int experimentId);

	/**
	 * @param experimentId
	 * @param proposalId
	 * @param multipleOptions
	 * @return 
	 */
	Experiment3VO setPriorities(int experimentId, int proposalId,  final SaxsDataCollectionComparator... multipleOptions);

	public Specimen3VO merge(Specimen3VO specimen3vo);

	/**
	 * @param experiment
	 */
//	public void removeDataCollections(Experiment3VO experiment);

	/**
	 * @param experiment
	 */
//	public void removeMeasurements(Experiment3VO experiment);

	/**
	 * @param experiment
	 */
//	public void removeSpecimens(Experiment3VO experiment);

	/**
	 * @param experiment
	 */
//	public void removePlatesByExperiment(Experiment3VO experiment);


//	public EntityManager getEntityManager();
}