/*************************************************************************************************
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
 ****************************************************************************************************/
package ispyb.client.mx.ranking.autoProcRanking;

import ispyb.common.util.Constants;

public class AutoProcRankingCriteriaVO {
	private Integer weightOverallRFactor = Constants.AUTOPROC_RANKING_DEFAULT_WEIGHT_OVERALL_RFACTOR;
    private Integer weightHighestResolution = Constants.AUTOPROC_RANKING_DEFAULT_WEIGHT_HIGHEST_RESOLUTION;
    private Integer weightCompleteness = Constants.AUTOPROC_RANKING_DEFAULT_WEIGHT_COMPLETENESS;
	
	public Integer getWeightOverallRFactor() {
		return weightOverallRFactor;
	}
	public void setWeightOverallRFactor(Integer weightOverallRFactor) {
		this.weightOverallRFactor = weightOverallRFactor;
	}
	public Integer getWeightHighestResolution() {
		return weightHighestResolution;
	}
	public void setWeightHighestResolution(Integer weightHighestResolution) {
		this.weightHighestResolution = weightHighestResolution;
	}
	public Integer getWeightCompleteness() {
		return weightCompleteness;
	}
	public void setWeightCompleteness(Integer weightCompleteness) {
		this.weightCompleteness = weightCompleteness;
	}
    
    
}
