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

import ispyb.server.biosaxs.vos.dataAcquisition.Specimen3VO;

import java.util.Comparator;


public enum SpecimenComparator implements Comparator<Specimen3VO> {
	
	BUFFER {
        public int compare(Specimen3VO o1, Specimen3VO o2) {
        	try{
        		if (o1.getBufferId() != null){
        			if (o2.getBufferId() != null){
        				return Integer.valueOf(Integer.valueOf(o1.getBufferId())).compareTo(Integer.valueOf(o2.getBufferId()));
        			}
        			else{
        				return -1;
        			}
        		}
        		return 1;
        	}
        	catch(Exception exp){
        		return 1;
        	}
        }
	},
	 CONCENTRATION {
	        public int compare(Specimen3VO o1, Specimen3VO o2) {
	        	try{
	        			String conc1 = o1.getConcentration().replace("?", "");
	        			String conc2 = o2.getConcentration().replace("?", "");
	        			return Integer.valueOf(Integer.valueOf(conc1)).compareTo(Integer.valueOf(conc2));
	        	}
	        	catch(Exception exp){
	        		return 1;
	        	}
	        }
	 },
	 MACROMOLECULE {
	        public int compare(Specimen3VO o1, Specimen3VO o2) {
	        	try{
	        		if (o1.getMacromolecule3VO() != null){
	        			if (o2.getMacromolecule3VO() != null){
	        				return Integer.valueOf(Integer.valueOf(o1.getMacromolecule3VO().getMacromoleculeId())).compareTo(Integer.valueOf(o2.getMacromolecule3VO().getMacromoleculeId()));
	        			}
	        			else{
	        				return -1;
	        			}
	        		}
	        		return 1;
	        	}
	        	catch(Exception exp){
	        		return 1;
	        	}
	        }
    },
    SPECIMEN_ID {
        public int compare(Specimen3VO o1, Specimen3VO o2) {
        	try{
        		return Integer.valueOf(Integer.valueOf(o1.getSpecimenId())).compareTo(Integer.valueOf(o2.getSpecimenId()));
        	}
        	catch(Exception exp){
        		return -1;
        	}
        }};        

	public static Comparator<Specimen3VO> compare(final Comparator<Specimen3VO> other) {
        return new Comparator<Specimen3VO>() {
            public int compare(Specimen3VO o1, Specimen3VO o2) {
            	 int result = other.compare(o1, o2);
                 if (result != 0) {
                     return result;
                 }
                 return 0;
            }
        };
    }

    public static Comparator<Specimen3VO> getComparator(final SpecimenComparator... multipleOptions) {
        return new Comparator<Specimen3VO>() {
            public int compare(Specimen3VO o1, Specimen3VO o2) {
                for (SpecimenComparator option : multipleOptions) {
                    int result = option.compare(o1, o2);
                    if (result != 0) {
                        return result;
                    }
                }
                return 0;
            }
        };
    }
}
