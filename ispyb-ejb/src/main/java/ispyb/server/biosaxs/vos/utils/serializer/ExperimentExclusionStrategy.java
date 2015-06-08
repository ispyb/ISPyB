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

package ispyb.server.biosaxs.vos.utils.serializer;

import ispyb.server.biosaxs.services.core.ExperimentScope;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;


public class ExperimentExclusionStrategy implements ExclusionStrategy {
//    private final Class<?> typeToSkip;

	private ExperimentScope experimentScope = ExperimentScope.MINIMAL;
	public String[] MINIMAL_VALUES_TO_EXCLUDE = new String[] {"buffer3VOs", "macromoleculeregion3VOs", "samplePlate3VOs", "stockSolution3VOs", "samples3VOs", "substraction3VOs" };
	public Set<String> MINIMAL_EXCLUDE = new HashSet<String>(Arrays.asList(MINIMAL_VALUES_TO_EXCLUDE));
	
	
	public String[] MEDIUM_VALUES_TO_EXCLUDE = new String[] {  "bufferhasadditive3VOs", "stockSolution3VOs", "macromoleculeregion3VOs", "frameSet3VOs" };
	public Set<String> MEDIUM_EXCLUDE = new HashSet<String>(Arrays.asList(MEDIUM_VALUES_TO_EXCLUDE));
	
	public String[] FULL_VALUES_TO_EXCLUDE = new String[] {"frameSet3VOs", "macromoleculeregion3VOs"  };
	public Set<String> FULL_EXCLUDE = new HashSet<String>(Arrays.asList(FULL_VALUES_TO_EXCLUDE));
	
    public ExperimentExclusionStrategy(ExperimentScope experimentScope) {
      this.experimentScope = experimentScope;
    }

    public boolean shouldSkipClass(Class<?> clazz) {
      return false;//(clazz == typeToSkip);
    }

    public boolean shouldSkipField(FieldAttributes f) {
		if (this.experimentScope.equals(ExperimentScope.MINIMAL)) {
			if (this.MINIMAL_EXCLUDE.contains(f.getName())) {
				return true;
			}
		}
		
		if (this.experimentScope.equals(ExperimentScope.MEDIUM)) {
			if (this.MEDIUM_EXCLUDE.contains(f.getName())) {
				return true;
			}
		}
		
		if (this.experimentScope.equals(ExperimentScope.MEDIUM)) {
			if (this.FULL_EXCLUDE.contains(f.getName())) {
				return true;
			}
		}
		
		return false;
    }
  }


//
///**
// * @author DEMARIAA
// *
// */
//public class ExperimentExclusionStrategy implements ExclusionStrategy {
//
////	private ExperimentScope experimentScope;
//
////	public String[] MINIMAL_VALUES = null;
////	public Set<String> MINIMAL_EXCLUDE = null;
//	
////	public ExperimentExclusionStrategy(ExperimentScope scope){
////		this.experimentScope = ExperimentScope.MINIMAL;
////		this.MINIMAL_VALUES = new String[] {"buffer3VOs", "samplePlate3VOs", "stockSolution3VOs", "samples3VOs", "dataCollections"  };
////		Arrays.asList(MINIMAL_VALUES);
////		this.MINIMAL_EXCLUDE = new HashSet<String>(Arrays.asList(MINIMAL_VALUES));
//		
////	}
//	
//    public boolean shouldSkipClass(Class<?> arg0) {
//        return false;
//    }
//
//    public boolean shouldSkipField(FieldAttributes f) {
//    	System.out.println(f.getName());
////    	if (this.experimentScope.equals(ExperimentScope.MINIMAL)){
//    		System.out.println("MINIMAL");
////    		if (this.MINIMAL_EXCLUDE.contains(f.getName())){
//    			return true;
////    		}
////    	}
////
////    	return false;
////        return (f.getDeclaringClass() == Experiment3VO.class && f.getName().equals("firstName"))||
////        (f.getDeclaringClass() == Experiment3VO.class && f.getName().equals("name"));
//    }
//
//	/* (non-Javadoc)
//	 * @see com.google.gson.ExclusionStrategy#shouldSkipField(com.google.gson.FieldAttributes)
//	 */
//}