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
 ******************************************************************************/
/**
 * SampleRankingCriteriaVO.java
 * @author patrice.brenchereau@esrf.fr
 * April 07, 2009
 */

package ispyb.client.mx.ranking;

import ispyb.common.util.Constants;

public class SampleRankingCriteriaVO {

		private Integer weightRankingResolution = Constants.SAMPLE_RANKING_DEFAULT_WEIGHT_RANKING_RESOLUTION;
	    private Integer weightExposureTime = Constants.SAMPLE_RANKING_DEFAULT_WEIGHT_EXPOSURE_TIME;
	    private Integer weightMosaicity = Constants.SAMPLE_RANKING_DEFAULT_WEIGHT_MOSAICITY;
	    private Integer weightNumberOfSpots = Constants.SAMPLE_RANKING_DEFAULT_WEIGHT_NUMBER_OF_SPOTS;
	    private Integer weightNumberOfImages = Constants.SAMPLE_RANKING_DEFAULT_WEIGHT_NUMBER_OF_IMAGES;    
	    
		public Integer getWeightRankingResolution() {
			return weightRankingResolution;
		}
		public void setWeightRankingResolution(Integer weightRankingResolution) {
			this.weightRankingResolution = weightRankingResolution;
		}
		public Integer getWeightExposureTime() {
			return weightExposureTime;
		}
		public void setWeightExposureTime(Integer weightExposureTime) {
			this.weightExposureTime = weightExposureTime;
		}
		public Integer getWeightMosaicity() {
			return weightMosaicity;
		}
		public void setWeightMosaicity(Integer weightMosaicity) {
			this.weightMosaicity = weightMosaicity;
		}
		public Integer getWeightNumberOfSpots() {
			return weightNumberOfSpots;
		}
		public void setWeightNumberOfSpots(Integer weightNumberOfSpots) {
			this.weightNumberOfSpots = weightNumberOfSpots;
		}
		public Integer getWeightNumberOfImages() {
			return weightNumberOfImages;
		}
		public void setWeightNumberOfImages(Integer weightNumberOfImages) {
			this.weightNumberOfImages = weightNumberOfImages;
		}


}
