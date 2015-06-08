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

import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Measurement3VO;
import ispyb.server.biosaxs.vos.datacollection.MeasurementTodataCollection3VO;
import ispyb.server.biosaxs.vos.datacollection.SaxsDataCollection3VO;

import java.util.List;

import javax.ejb.Remote;


@Remote
public interface Measurement3Service {

	public abstract void persist(Measurement3VO transientInstance);

	public abstract void remove(Measurement3VO persistentInstance);

	public abstract Measurement3VO merge(Measurement3VO detachedInstance);

	public abstract Measurement3VO findById(Integer id);
	
	public abstract List<MeasurementTodataCollection3VO> test(String ejbQL);

	public abstract List<Measurement3VO> findMeasurementBySpecimenId(int specimenId);
	
	/**
	 * @param experiment
	 * @param dataCollectionList
	 * @return
	 */
	public List<SaxsDataCollection3VO> resetAllPriorities(Experiment3VO experiment, List<SaxsDataCollection3VO> dataCollectionList);

	/**
	 * @param experiment
	 * @param dataCollectionList
	 * @param priority
	 * @return
	 */
	public int optimizeDatacollectionByRemovingDuplicatedBuffers(
																	Experiment3VO experiment,
																	List<SaxsDataCollection3VO> dataCollectionList, 
																	int priority);

	public Measurement3VO findMeasurementByCode(int experimentId, String code);

	public abstract Macromolecule3VO findMacromoleculeByCode(int parseInt, String runNumber) throws Exception;
}