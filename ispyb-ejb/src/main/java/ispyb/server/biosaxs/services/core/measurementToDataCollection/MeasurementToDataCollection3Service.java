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

package ispyb.server.biosaxs.services.core.measurementToDataCollection;


import ispyb.server.biosaxs.vos.datacollection.MeasurementTodataCollection3VO;
import ispyb.server.biosaxs.vos.datacollection.SaxsDataCollection3VO;

import java.util.List;

import javax.ejb.Remote;


@Remote
public interface MeasurementToDataCollection3Service {

	public abstract void persist(MeasurementTodataCollection3VO transientInstance);

	public abstract MeasurementTodataCollection3VO merge(MeasurementTodataCollection3VO detachedInstance);
	
	public abstract void remove(MeasurementTodataCollection3VO detachedInstance);

	public abstract MeasurementTodataCollection3VO findById(int id);

	public abstract List<MeasurementTodataCollection3VO> findByMeasurementId(int measurementId);

	public abstract SaxsDataCollection3VO findDataCollectionById(int dataCollectionId);

	public abstract List<MeasurementTodataCollection3VO> findMeasurementToDataCollectionByDataCollectionId(Integer dataCollectionId);

	void remove(SaxsDataCollection3VO dataCollection);
}