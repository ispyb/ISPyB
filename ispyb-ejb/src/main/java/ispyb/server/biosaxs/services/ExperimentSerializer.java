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

package ispyb.server.biosaxs.services;

import ispyb.server.biosaxs.services.core.ExperimentScope;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.utils.serializer.ExperimentExclusionStrategy;

import java.lang.reflect.Modifier;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author DEMARIAA
 *
 */
public class ExperimentSerializer {
	
	public static String serialize(List<Experiment3VO> experiments){
		return serialize(experiments, ExperimentScope.MINIMAL);
	}

	/**
	 * @param experiments
	 * @param medium
	 * @return
	 */
	public static String serialize(List<Experiment3VO> experiments, ExperimentScope scope) {
		Gson gson =  new GsonBuilder().setExclusionStrategies(new ExperimentExclusionStrategy(scope))
				.excludeFieldsWithModifiers(Modifier.PRIVATE)
				.serializeNulls()
				.create();
		
		return  gson.toJson(experiments);
	}

	/**
	 * @param experiment
	 * @param medium
	 * @return
	 */
	public static String serializeExperiment(Experiment3VO experiment, ExperimentScope scope) {
		Gson gson =  new GsonBuilder().setExclusionStrategies(new ExperimentExclusionStrategy(scope))
				.excludeFieldsWithModifiers(Modifier.PRIVATE)
				.serializeNulls()
				.create();
		
		return  gson.toJson(experiment);
	}

}
