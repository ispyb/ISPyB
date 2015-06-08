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

package ispyb.server.common.test.services;


import ispyb.server.biosaxs.services.core.ExperimentScope;
import ispyb.server.biosaxs.services.core.experiment.Experiment3Service;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Measurement3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Specimen3VO;
import ispyb.server.biosaxs.vos.datacollection.MeasurementTodataCollection3VO;
import ispyb.server.biosaxs.vos.datacollection.SaxsDataCollection3VO;
import ispyb.server.biosaxs.vos.utils.comparator.SaxsDataCollectionComparator;
import ispyb.server.common.test.SaxsEJB3Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class ComparatorTest extends SaxsEJB3Test {

	/** PARAMETERS **/
	private Experiment3Service experiment3Service;
	
	@Before
	public void setUp() throws Exception {
		experiment3Service = (Experiment3Service) serviceLocator.getRemoteService(Experiment3Service.class, properties);
	}

	@Test
	public void getEx(){
		this.experiment3Service.findById(4643, ExperimentScope.MEDIUM);
		
	}
//	@Test
//	public void test() throws Exception{
//		
//		String properties =  "[{\"modelId\":5034,\"color\":\"0xFF6600\",\"opacity\":0.5},{\"modelId\":5036,\"color\":\"0x00CC00\",\"opacity\":0.5}]";
//		
//		Type mapType = new TypeToken<ArrayList<HashMap<String, String>>>() {}.getType();
//		ArrayList<HashMap<String, String>> propertiesList = new Gson().fromJson(properties, mapType );
//		
//		
//		for (HashMap<String, String> property : propertiesList) {
//			System.out.println(property);
//		}
//	}
	
	@SuppressWarnings("unchecked")
//	@Test
	public void Sorting() throws Exception{
		try{
			int experimentId = 3425;
			int proposalId = 10;
			
			Experiment3VO experiment = this.experiment3Service.findById(experimentId, ExperimentScope.MEDIUM, proposalId);
			Iterator<SaxsDataCollection3VO> iterator = experiment.getDataCollections().iterator();
			
			while (iterator.hasNext()){
				SaxsDataCollection3VO dc = iterator.next();
				ArrayList<MeasurementTodataCollection3VO> list = new ArrayList<MeasurementTodataCollection3VO>(); 
				list.addAll(dc.getMeasurementtodatacollection3VOs());
				Collections.sort(list, MeasurementTodataCollectionComparatorOrder);
				Measurement3VO measurement = experiment.getMeasurementById(list.get(0).getMeasurementId());
			}
			
			List<SaxsDataCollection3VO> dc = experiment.getDataCollectionList();
			Collections.sort(dc, SaxsDataCollectionComparator.compare(experiment, 
					SaxsDataCollectionComparator.getComparator(
							SaxsDataCollectionComparator.defaultComparator)));
			print(dc, experiment);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void Sorting2() throws Exception{
		try{
			int experimentId = 3425;
			int proposalId = 3352;
			
			this.experiment3Service.setPriorities(experimentId, proposalId, SaxsDataCollectionComparator.defaultComparator);
			
			Experiment3VO experiment = this.experiment3Service.findById(experimentId, ExperimentScope.MEDIUM, proposalId);
			Iterator<SaxsDataCollection3VO> iterator = experiment.getDataCollections().iterator();
			
			while (iterator.hasNext()){
				SaxsDataCollection3VO dc = iterator.next();
				ArrayList<MeasurementTodataCollection3VO> list = new ArrayList<MeasurementTodataCollection3VO>(); 
				list.addAll(dc.getMeasurementtodatacollection3VOs());
				Collections.sort(list, MeasurementTodataCollectionComparatorOrder);
				Measurement3VO measurement = experiment.getMeasurementById(list.get(0).getMeasurementId());
			}
			
			
			List<SaxsDataCollection3VO> dc = experiment.getDataCollectionList();
			Collections.sort(dc, SaxsDataCollectionComparator.compare(experiment, 
					SaxsDataCollectionComparator.getComparator(
							SaxsDataCollectionComparator.defaultComparator)));
			print(dc, experiment);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void print (List saxsDatacolelction, Experiment3VO experiment){
		for (Object object : saxsDatacolelction) {
			SaxsDataCollection3VO dc = (SaxsDataCollection3VO)object;
			ArrayList<MeasurementTodataCollection3VO> list = new ArrayList<MeasurementTodataCollection3VO>(); 
			list.addAll(dc.getMeasurementtodatacollection3VOs());
			Collections.sort(list, MeasurementTodataCollectionComparatorOrder);
			Measurement3VO measurement = experiment.getMeasurementById(list.get(1).getMeasurementId());
			Specimen3VO specimen = experiment.getSampleById(measurement.getSpecimenId());
			System.out.println("Buffer: "  + specimen.getBufferId() + " Temp:" + measurement.getExposureTemperature()  + " Macro: " + specimen.getMacromolecule3VO()  + " Conc: " + specimen.getConcentration());
		}
	}
	
	
	public static Comparator<MeasurementTodataCollection3VO> MeasurementTodataCollectionComparatorOrder = new Comparator<MeasurementTodataCollection3VO>()
			{
			    public int compare(MeasurementTodataCollection3VO o1, MeasurementTodataCollection3VO o2) {
			        return o1.getDataCollectionOrder() - o2.getDataCollectionOrder();
			    }
			};
}