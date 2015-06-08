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

import ispyb.server.biosaxs.vos.dataAcquisition.Measurement3VO;

import java.util.Comparator;


public enum MeasurementComparator implements Comparator<Measurement3VO> {
	PRIOTIRY_SORT_ASC {
        public int compare(Measurement3VO o1, Measurement3VO o2) {
        	try{
        		return Float.valueOf(Float.valueOf(o1.getPriority())).compareTo(Float.valueOf(o2.getPriority()));
        	}
        	catch(Exception exp){
        		return -1;
        	}
        }},
    PRIORITY_SORT_DESC {
        public int compare(Measurement3VO o1, Measurement3VO o2) {
        	try{
        		return -1*Float.valueOf(Float.valueOf(o1.getPriority())).compareTo(Float.valueOf(o2.getPriority()));
        	}
        	catch(Exception exp){
        		return -1;
        	}
        }},
        
//	CONCENTRATION_SORT_ASC {
//        public int compare(Measurement3VO o1, Measurement3VO o2) {
//        	try{
//        		return Float.valueOf(Float.valueOf(o1.getConcentration())).compareTo(Float.valueOf(o2.getConcentration()));
//        	}
//        	catch(Exception exp){
//        		return -1;
//        	}
//        }},
//    CONCENTRATION_SORT_DESC {
//        public int compare(Measurement3VO o1, Measurement3VO o2) {
//        	try{
//        		return -1*Float.valueOf(Float.valueOf(o1.getConcentration())).compareTo(Float.valueOf(o2.getConcentration()));
//        	}
//        	catch(Exception exp){
//        		return -1;
//        	}
//        }},
        
   EXPOSURE_TEMPERATURE_SORT_DESC {
            public int compare(Measurement3VO o1, Measurement3VO o2) {
            	try{
            		return -1*Float.valueOf(o1.getExposureTemperature()).compareTo(Float.valueOf(o2.getExposureTemperature()));
            	}
            	catch(Exception exp){
            		return -1;
            	}
            }},
    EXPOSURE_TEMPERATURE_SORT_ASC {
        public int compare(Measurement3VO o1, Measurement3VO o2) {
        	try{
            return Float.valueOf(o1.getExposureTemperature()).compareTo(Float.valueOf(o2.getExposureTemperature()));
        	}
        	catch(Exception exp){
        			return -1;
        	}
        }};

            

	public static Comparator<Measurement3VO> compare(final Comparator<Measurement3VO> other) {
        return new Comparator<Measurement3VO>() {
            public int compare(Measurement3VO o1, Measurement3VO o2) {
                return other.compare(o1, o2);
            }
        };
    }

    public static Comparator<Measurement3VO> getComparator(final MeasurementComparator... multipleOptions) {
        return new Comparator<Measurement3VO>() {
            public int compare(Measurement3VO o1, Measurement3VO o2) {
                for (MeasurementComparator option : multipleOptions) {
                    int result = option.compare(o1, o2);
                    if (result != 0) {
                        return result;
                    }
                }
                return 0;
            }
        };
    }
    
//    public static Comparator<Measurement3VO> getComparator(List<Macromolecule3VO> macromolecules, final MeasurementComparator... multipleOptions) {
//    	macromoleculeList = macromolecules;
//        return new Comparator<Measurement3VO>() {
//            public int compare(Measurement3VO o1, Measurement3VO o2) {
//                for (MeasurementComparator option : multipleOptions) {
//                    int result = option.compare(o1, o2);
//                    if (result != 0) {
//                        return result;
//                    }
//                }
//                return 0;
//            }
//        };
//    }
}
