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

package ispyb.server.biosaxs.vos.utils.comparator;

import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Measurement3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Specimen3VO;
import ispyb.server.biosaxs.vos.datacollection.MeasurementTodataCollection3VO;
import ispyb.server.biosaxs.vos.datacollection.SaxsDataCollection3VO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public enum SaxsDataCollectionComparator implements Comparator<SaxsDataCollection3VO> {
    BY_BUFFER {
        public int compare(SaxsDataCollection3VO o1, SaxsDataCollection3VO o2) {
        	try{
        		if (o1.getMeasurementtodatacollection3VOs().size() > 0){
            		if (o2.getMeasurementtodatacollection3VOs().size() > 0){
            			MeasurementTodataCollection3VO mtd1 = o1.getMeasurementtodatacollection3VOs().iterator().next();
            			MeasurementTodataCollection3VO mtd2 = o2.getMeasurementtodatacollection3VOs().iterator().next();
            			
            			Measurement3VO m1 = EXPERIMENT.getMeasurementById(mtd1.getMeasurementId());
            			Measurement3VO m2 = EXPERIMENT.getMeasurementById(mtd2.getMeasurementId());
            			
            			Specimen3VO s1 = EXPERIMENT.getSampleById(m1.getSpecimenId());
            			Specimen3VO s2 = EXPERIMENT.getSampleById(m2.getSpecimenId());
            			
            			return (s2.getBufferId() - s1.getBufferId());
            		}
            	}
        		return 0;
        	}
        	catch(Exception exp){
        		return -1;
        	}
        }
      },

    /**
     * 
     * Compares its two arguments for order. 
     * Returns a negative integer, zero or a positive integer as the first argument is less than, equal to or greater than the second.
     * 
     **/
   BY_EXPOSURE_TEMPERATURE {
        public int compare(SaxsDataCollection3VO o1, SaxsDataCollection3VO o2) {
        	try{
        		if (o1.getMeasurementtodatacollection3VOs().size() > 0){
            		if (o2.getMeasurementtodatacollection3VOs().size() > 0){
            			MeasurementTodataCollection3VO mtd1 = o1.getMeasurementtodatacollection3VOs().iterator().next();
            			MeasurementTodataCollection3VO mtd2 = o2.getMeasurementtodatacollection3VOs().iterator().next();
            			
            			Measurement3VO m1 = EXPERIMENT.getMeasurementById(mtd1.getMeasurementId());
            			Measurement3VO m2 = EXPERIMENT.getMeasurementById(mtd2.getMeasurementId());
            			
            			Float exp1 = Float.parseFloat(m1.getExposureTemperature());
            			return exp1.compareTo(Float.parseFloat(m2.getExposureTemperature()));
            		}
            	}
        		return 0;
        	}
        	catch(Exception exp){
        		return -1;
        	}
       }
      
    }, 
    
    
    BY_MACROMOLECULE {
        public int compare(SaxsDataCollection3VO o1, SaxsDataCollection3VO o2) {
        	try{
        		if (o1.getMeasurementtodatacollection3VOs().size() > 0){
            		if (o2.getMeasurementtodatacollection3VOs().size() > 0){
            			
            			List<MeasurementTodataCollection3VO> listMeasurement1 = new ArrayList<MeasurementTodataCollection3VO>();
            			listMeasurement1.addAll(o1.getMeasurementtodatacollection3VOs());
            			Collections.sort(listMeasurement1, MeasurementTodataCollectionComparatorOrder);
            			
            			List<MeasurementTodataCollection3VO> listMeasurement2 = new ArrayList<MeasurementTodataCollection3VO>();
            			listMeasurement2.addAll(o2.getMeasurementtodatacollection3VOs());
            			Collections.sort(listMeasurement2, MeasurementTodataCollectionComparatorOrder);
            			
            			
            			MeasurementTodataCollection3VO mtd1 = listMeasurement1.get(1);
            			MeasurementTodataCollection3VO mtd2 = listMeasurement2.get(1);
            			
            			Measurement3VO m1 = EXPERIMENT.getMeasurementById(mtd1.getMeasurementId());
            			Measurement3VO m2 = EXPERIMENT.getMeasurementById(mtd2.getMeasurementId());
            			
            			Specimen3VO s1 = EXPERIMENT.getSampleById(m1.getSpecimenId());
            			Specimen3VO s2 = EXPERIMENT.getSampleById(m2.getSpecimenId());
            			
            			if (s1.getMacromolecule3VO() != null){
            				if (s2.getMacromolecule3VO() != null){
            					return s1.getMacromolecule3VO().getMacromoleculeId() - s2.getMacromolecule3VO().getMacromoleculeId();  
            				}
            			}
            		}
            	}
        		return 0;
        	}
        	catch(Exception exp){
        		exp.printStackTrace();
        		return -1;
        	}
       }
      
    }, 
    BY_CONCENTRATION{
        public int compare(SaxsDataCollection3VO o1, SaxsDataCollection3VO o2) {
        	try{
        		if (o1.getMeasurementtodatacollection3VOs().size() > 0){
            		if (o2.getMeasurementtodatacollection3VOs().size() > 0){
            			
            			List<MeasurementTodataCollection3VO> listMeasurement1 = new ArrayList<MeasurementTodataCollection3VO>();
            			listMeasurement1.addAll(o1.getMeasurementtodatacollection3VOs());
            			Collections.sort(listMeasurement1, MeasurementTodataCollectionComparatorOrder);
            			
            			List<MeasurementTodataCollection3VO> listMeasurement2 = new ArrayList<MeasurementTodataCollection3VO>();
            			listMeasurement2.addAll(o2.getMeasurementtodatacollection3VOs());
            			Collections.sort(listMeasurement2, MeasurementTodataCollectionComparatorOrder);
            			
            			
            			MeasurementTodataCollection3VO mtd1 = listMeasurement1.get(1);
            			MeasurementTodataCollection3VO mtd2 = listMeasurement2.get(1);
            			
            			Measurement3VO m1 = EXPERIMENT.getMeasurementById(mtd1.getMeasurementId());
            			Measurement3VO m2 = EXPERIMENT.getMeasurementById(mtd2.getMeasurementId());
            			
            			Specimen3VO s1 = EXPERIMENT.getSampleById(m1.getSpecimenId());
            			Specimen3VO s2 = EXPERIMENT.getSampleById(m2.getSpecimenId());
            			
            			
            			if ((s1.getConcentration() != null)&&(s2.getConcentration() != null)){
            				if (Float.parseFloat(s1.getConcentration()) > Float.parseFloat(s2.getConcentration())){
            					return 1;
            				}
            				if (Float.parseFloat(s1.getConcentration()) == Float.parseFloat(s2.getConcentration())){
            					/**
            					 * In order to avoid random sorting measurementId is used
            					 */
            					return m1.getMeasurementId() - m2.getMeasurementId();
            				}
            				
            				return -1;
            			}
            		}
            	}
        		return 0;
        	}
        	catch(Exception exp){
        		exp.printStackTrace();
        		return -1;
        	}
       }
      
    }, 
    BY_TRANSMISSION {
        public int compare(SaxsDataCollection3VO o1, SaxsDataCollection3VO o2) {
        	try{
        		if (o1.getMeasurementtodatacollection3VOs().size() > 0){
            		if (o2.getMeasurementtodatacollection3VOs().size() > 0){
            			MeasurementTodataCollection3VO mtd1 = o1.getMeasurementtodatacollection3VOs().iterator().next();
            			MeasurementTodataCollection3VO mtd2 = o2.getMeasurementtodatacollection3VOs().iterator().next();
            			
            			Measurement3VO m1 = EXPERIMENT.getMeasurementById(mtd1.getMeasurementId());
            			Measurement3VO m2 = EXPERIMENT.getMeasurementById(mtd2.getMeasurementId());
            			
            			return (int) (Float.parseFloat(m1.getTransmission()) - Float.parseFloat(m2.getTransmission()));
            		}
            	}
        		return 0;
        	}
        	catch(Exception exp){
        		exp.printStackTrace();
        		return -1;
        	}
       }
      
    },  BY_VOLUME_TO_LOAD{
        public int compare(SaxsDataCollection3VO o1, SaxsDataCollection3VO o2) {
        	try{
        		if (o1.getMeasurementtodatacollection3VOs().size() > 0){
            		if (o2.getMeasurementtodatacollection3VOs().size() > 0){
            			MeasurementTodataCollection3VO mtd1 = o1.getMeasurementtodatacollection3VOs().iterator().next();
            			MeasurementTodataCollection3VO mtd2 = o2.getMeasurementtodatacollection3VOs().iterator().next();
            			
            			Measurement3VO m1 = EXPERIMENT.getMeasurementById(mtd1.getMeasurementId());
            			Measurement3VO m2 = EXPERIMENT.getMeasurementById(mtd2.getMeasurementId());
            			
            			
            			return (int) (Float.parseFloat(m1.getVolumeToLoad()) - Float.parseFloat(m2.getVolumeToLoad()));
            		}
            	}
        		return 0;
        	}
        	catch(Exception exp){
        		return -1;
        	}
      }
    },
    BY_MEASUREMENT_ID{
        public int compare(SaxsDataCollection3VO o1, SaxsDataCollection3VO o2) {
        	System.out.println("BY_MEASUREMENT_ID");
        	try{
        		if (o1.getMeasurementtodatacollection3VOs().size() > 0){
            		if (o2.getMeasurementtodatacollection3VOs().size() > 0){
            			MeasurementTodataCollection3VO mtd1 = o1.getMeasurementtodatacollection3VOs().iterator().next();
            			MeasurementTodataCollection3VO mtd2 = o2.getMeasurementtodatacollection3VOs().iterator().next();
            			System.out.println("comparing: " + mtd1.getMeasurementId() +  "   " + mtd2.getMeasurementId());
            			System.out.println(mtd1.getMeasurementId() - mtd2.getMeasurementId());
            			return  (mtd1.getMeasurementId() - mtd2.getMeasurementId());
            		}
            	}
        		System.out.println("BY_MEASUREMENT_ID 0");
        		return 0;
        	}
        	catch(Exception exp){
        		System.out.println("BY_MEASUREMENT_ID -1");
        		return -1;
        	}
      }
    },
    BY_FLOW{
        public int compare(SaxsDataCollection3VO o1, SaxsDataCollection3VO o2) {
        	try{
        		if (o1.getMeasurementtodatacollection3VOs().size() > 0){
            		if (o2.getMeasurementtodatacollection3VOs().size() > 0){
            			MeasurementTodataCollection3VO mtd1 = o1.getMeasurementtodatacollection3VOs().iterator().next();
            			MeasurementTodataCollection3VO mtd2 = o2.getMeasurementtodatacollection3VOs().iterator().next();
            			
            			Measurement3VO m1 = EXPERIMENT.getMeasurementById(mtd1.getMeasurementId());
            			Measurement3VO m2 = EXPERIMENT.getMeasurementById(mtd2.getMeasurementId());
            			
            			
            			return SaxsDataCollectionComparator.getIntegerFlow(m1.getFlow()) - SaxsDataCollectionComparator.getIntegerFlow(m2.getFlow());
            		}
            	}
        		return 0;
        	}
        	catch(Exception exp){
        		return -1;
        	}
      }
    };

    public static int getIntegerFlow(boolean flow){
    	if (flow){
    		return 0;
    	}
   		return 1;
    	
    }
    
    public static int getIntegerByViscosity(String viscosity){
    	if (viscosity.toLowerCase().equals("low")){
    		return 0;
    	}
    	if (viscosity.toLowerCase().equals("medium")){
    		return 1;
    	}
    	if (viscosity.toLowerCase().equals("high")){
    		return 2;
    	}
    	return -1;
    	
    }
    
    public static Experiment3VO EXPERIMENT = null;
    
    
	public static Comparator<SaxsDataCollection3VO> compare(Experiment3VO experiment, final Comparator<SaxsDataCollection3VO> other) {
		SaxsDataCollectionComparator.EXPERIMENT = experiment;
        return new Comparator<SaxsDataCollection3VO>() {
            public int compare(SaxsDataCollection3VO o1, SaxsDataCollection3VO o2) {
            	 int result = other.compare(o1, o2);
                 if (result != 0) {
                     return result;
                 }
                 return 0;
            }
        };
    }

	public static SaxsDataCollectionComparator[] defaultComparator = {
			SaxsDataCollectionComparator.BY_EXPOSURE_TEMPERATURE, 
			SaxsDataCollectionComparator.BY_BUFFER, 
			SaxsDataCollectionComparator.BY_MACROMOLECULE,
//			SaxsDataCollectionComparator.BY_MEASUREMENT_ID,
			SaxsDataCollectionComparator.BY_CONCENTRATION
			/**
			 * By measurement id avoid "random" sorting when measurement are similar  
			 */
	};
	
    public static Comparator<SaxsDataCollection3VO> getComparator(final SaxsDataCollectionComparator... multipleOptions) {
        return new Comparator<SaxsDataCollection3VO>() {
            public int compare(SaxsDataCollection3VO o1, SaxsDataCollection3VO o2) {
                for (SaxsDataCollectionComparator option : multipleOptions) {
                    int result = option.compare(o1, o2);
                    if (result != 0) {
                        return result;
                    }
                }
                return 0;
            }
        };
    }
    
    
	private static Comparator<MeasurementTodataCollection3VO> MeasurementTodataCollectionComparatorOrder = new Comparator<MeasurementTodataCollection3VO>()
			{
			    public int compare(MeasurementTodataCollection3VO o1, MeasurementTodataCollection3VO o2) {
			        return o1.getDataCollectionOrder() - o2.getDataCollectionOrder();
			    }
			};
}
