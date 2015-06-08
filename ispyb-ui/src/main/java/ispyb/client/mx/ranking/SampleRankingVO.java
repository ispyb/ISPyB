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
 * SampleRankingValVO.java
 * @author patrice.brenchereau@esrf.fr
 * March 09, 2009
 */

package ispyb.client.mx.ranking;

import ispyb.client.mx.ranking.SampleScreeningVO.ScreeningOutputLatticeVO;
import ispyb.client.mx.ranking.SampleScreeningVO.ScreeningOutputVO;
import ispyb.client.mx.ranking.SampleScreeningVO.ScreeningStrategyVO;
import ispyb.client.mx.ranking.SampleScreeningVO.ScreeningVO;


public class SampleRankingVO {
	
	/**
	 * DataCollection values
	 */
	
	private Integer dataCollectionId;
	private String imagePrefix;
	private Integer dataCollectionNumber;
	private java.sql.Timestamp startTime;
	private String spaceGroup;
	private Double unitCell_a;
	private Double unitCell_b;
	private Double unitCell_c;
	
	private ScreeningVO					value_ScreeningVO;
	private ScreeningOutputVO			value_ScreeningOutputVO;
	private ScreeningOutputLatticeVO	value_ScreeningOutputLatticeVO;
	private ScreeningStrategyVO			value_ScreeningStrategyVO;
	
	/**
	 * DataCollection ranking values
	 * 
	 * - xxxValue: value of data xxx
	 * - xxxScore: absolute score (can be the 'value', '-value' or '(optimum value) - value' or ...
	 * - xxxRank:  relative ranking (compared to scores of other items)
	 */
	
	// theoreticalResolution
	private Double 	theoreticalResolutionValue;
	private Double 	theoreticalResolutionScore;
	private Integer theoreticalResolutionRank;
	
	// totalExposureTime
	private Double 	totalExposureTimeValue;
	private Double 	totalExposureTimeScore;
	private Integer totalExposureTimeRank;
	
	// mosaicity
	private Double 	mosaicityValue;
	private Double 	mosaicityScore;
	private Integer mosaicityRank;
	
	// numberOfSpotsIndexed
	private Integer numberOfSpotsIndexedValue;
	private Double numberOfSpotsIndexedScore;
	private Integer numberOfSpotsIndexedRank;
	
	// numberOfImages
	private Integer numberOfImagesValue;
	private Double numberOfImagesScore;
	private Integer numberOfImagesRank;
	
	// total
	private Double totalScore;
	private Integer totalRank;
	
	/**
	 * Getters and Setters
	 */
	
	public Integer getDataCollectionId() {
		return dataCollectionId;
	}
	public void setDataCollectionId(Integer dataCollectionId) {
		this.dataCollectionId = dataCollectionId;
	}
	public String getImagePrefix() {
		return imagePrefix;
	}
	public void setImagePrefix(String imagePrefix) {
		this.imagePrefix = imagePrefix;
	}
	public Integer getDataCollectionNumber() {
		return dataCollectionNumber;
	}
	public void setDataCollectionNumber(Integer dataCollectionNumber) {
		this.dataCollectionNumber = dataCollectionNumber;
	}
	public java.sql.Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(java.sql.Timestamp startTime) {
		this.startTime = startTime;
	}
	public String getSpaceGroup() {
		return spaceGroup;
	}
	public void setSpaceGroup(String spaceGroup) {
		this.spaceGroup = spaceGroup;
	}
	public Double getUnitCell_a() {
		return unitCell_a;
	}
	public void setUnitCell_a(Double unitCell_a) {
		this.unitCell_a = unitCell_a;
	}
	public Double getUnitCell_b() {
		return unitCell_b;
	}
	public void setUnitCell_b(Double unitCell_b) {
		this.unitCell_b = unitCell_b;
	}
	public Double getUnitCell_c() {
		return unitCell_c;
	}
	public void setUnitCell_c(Double unitCell_c) {
		this.unitCell_c = unitCell_c;
	}
	public Double getTheoreticalResolutionValue() {
		return theoreticalResolutionValue;
	}
	public void setTheoreticalResolutionValue(Double theoreticalResolutionValue) {
		this.theoreticalResolutionValue = theoreticalResolutionValue;
	}
	public Double getTheoreticalResolutionScore() {
		return theoreticalResolutionScore;
	}
	public void setTheoreticalResolutionScore(Double theoreticalResolutionScore) {
		this.theoreticalResolutionScore = theoreticalResolutionScore;
	}
	public Integer getTheoreticalResolutionRank() {
		return theoreticalResolutionRank;
	}
	public void setTheoreticalResolutionRank(Integer theoreticalResolutionRank) {
		this.theoreticalResolutionRank = theoreticalResolutionRank;
	}
	public Double getTotalExposureTimeValue() {
		return totalExposureTimeValue;
	}
	public void setTotalExposureTimeValue(Double totalExposureTimeValue) {
		this.totalExposureTimeValue = totalExposureTimeValue;
	}
	public Double getTotalExposureTimeScore() {
		return totalExposureTimeScore;
	}
	public void setTotalExposureTimeScore(Double totalExposureTimeScore) {
		this.totalExposureTimeScore = totalExposureTimeScore;
	}
	public Integer getTotalExposureTimeRank() {
		return totalExposureTimeRank;
	}
	public void setTotalExposureTimeRank(Integer totalExposureTimeRank) {
		this.totalExposureTimeRank = totalExposureTimeRank;
	}
	public Double getMosaicityValue() {
		return mosaicityValue;
	}
	public void setMosaicityValue(Double mosaicityValue) {
		this.mosaicityValue = mosaicityValue;
	}
	public Double getMosaicityScore() {
		return mosaicityScore;
	}
	public void setMosaicityScore(Double mosaicityScore) {
		this.mosaicityScore = mosaicityScore;
	}
	public Integer getMosaicityRank() {
		return mosaicityRank;
	}
	public void setMosaicityRank(Integer mosaicityRank) {
		this.mosaicityRank = mosaicityRank;
	}
	public Integer getNumberOfSpotsIndexedValue() {
		return numberOfSpotsIndexedValue;
	}
	public void setNumberOfSpotsIndexedValue(Integer numberOfSpotsIndexedValue) {
		this.numberOfSpotsIndexedValue = numberOfSpotsIndexedValue;
	}
	public Double getNumberOfSpotsIndexedScore() {
		return numberOfSpotsIndexedScore;
	}
	public void setNumberOfSpotsIndexedScore(Double numberOfSpotsIndexedScore) {
		this.numberOfSpotsIndexedScore = numberOfSpotsIndexedScore;
	}
	public Integer getNumberOfSpotsIndexedRank() {
		return numberOfSpotsIndexedRank;
	}
	public void setNumberOfSpotsIndexedRank(Integer numberOfSpotsIndexedRank) {
		this.numberOfSpotsIndexedRank = numberOfSpotsIndexedRank;
	}
	public Integer getNumberOfImagesValue() {
		return numberOfImagesValue;
	}
	public void setNumberOfImagesValue(Integer numberOfImagesValue) {
		this.numberOfImagesValue = numberOfImagesValue;
	}
	public Double getNumberOfImagesScore() {
		return numberOfImagesScore;
	}
	public void setNumberOfImagesScore(Double numberOfImagesScore) {
		this.numberOfImagesScore = numberOfImagesScore;
	}
	public Integer getNumberOfImagesRank() {
		return numberOfImagesRank;
	}
	public void setNumberOfImagesRank(Integer numberOfImagesRank) {
		this.numberOfImagesRank = numberOfImagesRank;
	}
	public Double getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Double totalScore) {
		this.totalScore = totalScore;
	}
	public Integer getTotalRank() {
		return totalRank;
	}
	public void setTotalRank(Integer totalRank) {
		this.totalRank = totalRank;
	}
	public ScreeningVO getValue_ScreeningVO() {
		return value_ScreeningVO;
	}
	public void setValue_ScreeningVO(ScreeningVO value_ScreeningVO) {
		this.value_ScreeningVO = value_ScreeningVO;
	}
	public ScreeningOutputVO getValue_ScreeningOutputVO() {
		return value_ScreeningOutputVO;
	}
	public void setValue_ScreeningOutputVO(ScreeningOutputVO value_ScreeningOutputVO) {
		this.value_ScreeningOutputVO = value_ScreeningOutputVO;
	}
	public ScreeningOutputLatticeVO getValue_ScreeningOutputLatticeVO() {
		return value_ScreeningOutputLatticeVO;
	}
	public void setValue_ScreeningOutputLatticeVO(
			ScreeningOutputLatticeVO value_ScreeningOutputLatticeVO) {
		this.value_ScreeningOutputLatticeVO = value_ScreeningOutputLatticeVO;
	}
	public ScreeningStrategyVO getValue_ScreeningStrategyVO() {
		return value_ScreeningStrategyVO;
	}
	public void setValue_ScreeningStrategyVO(
			ScreeningStrategyVO value_ScreeningStrategyVO) {
		this.value_ScreeningStrategyVO = value_ScreeningStrategyVO;
	}

	

	
	
}
