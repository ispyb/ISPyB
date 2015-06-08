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

import java.io.Serializable;

public class AutoProcRankingVO implements Serializable{
	private static final long serialVersionUID = -1541342631907883857L;
	
	protected Integer dataCollectionId;
	protected String imagePrefix;
	protected Integer dataCollectionNumber;
	protected java.sql.Timestamp startTime;
	protected String spaceGroup;
	protected Float unitCell_a;
	protected Float unitCell_b;
	protected Float unitCell_c;
	
	// overall R-factor
	protected Float 	overallRFactorValue;
	protected Float 	overallRFactorScore;
	protected Integer overallRFactorRank; 
	
	// highest resolution
	protected Float 	highestResolutionValue;
	protected Float 	highestResolutionScore;
	protected Integer highestResolutionRank; 
	
	// completeness
	protected Float 	completenessValue;
	protected Float 	completenessScore;
	protected Integer completenessRank;
	
	// total
	protected Float totalScore;
	protected Integer totalRank;
	
	
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
	public Float getUnitCell_a() {
		return unitCell_a;
	}
	public void setUnitCell_a(Float unitCell_a) {
		this.unitCell_a = unitCell_a;
	}
	public Float getUnitCell_b() {
		return unitCell_b;
	}
	public void setUnitCell_b(Float unitCell_b) {
		this.unitCell_b = unitCell_b;
	}
	public Float getUnitCell_c() {
		return unitCell_c;
	}
	public void setUnitCell_c(Float unitCell_c) {
		this.unitCell_c = unitCell_c;
	}
	public Float getOverallRFactorValue() {
		return overallRFactorValue;
	}
	public void setOverallRFactorValue(Float overallRFactorValue) {
		this.overallRFactorValue = overallRFactorValue;
	}
	public Float getOverallRFactorScore() {
		return overallRFactorScore;
	}
	public void setOverallRFactorScore(Float overallRFactorScore) {
		this.overallRFactorScore = overallRFactorScore;
	}
	public Integer getOverallRFactorRank() {
		return overallRFactorRank;
	}
	public void setOverallRFactorRank(Integer overallRFactorRank) {
		this.overallRFactorRank = overallRFactorRank;
	}
	public Float getHighestResolutionValue() {
		return highestResolutionValue;
	}
	public void setHighestResolutionValue(Float highestResolutionValue) {
		this.highestResolutionValue = highestResolutionValue;
	}
	public Float getHighestResolutionScore() {
		return highestResolutionScore;
	}
	public void setHighestResolutionScore(Float highestResolutionScore) {
		this.highestResolutionScore = highestResolutionScore;
	}
	public Integer getHighestResolutionRank() {
		return highestResolutionRank;
	}
	public void setHighestResolutionRank(Integer highestResolutionRank) {
		this.highestResolutionRank = highestResolutionRank;
	}
	public Float getCompletenessValue() {
		return completenessValue;
	}
	public void setCompletenessValue(Float completenessValue) {
		this.completenessValue = completenessValue;
	}
	public Float getCompletenessScore() {
		return completenessScore;
	}
	public void setCompletenessScore(Float completenessScore) {
		this.completenessScore = completenessScore;
	}
	public Integer getCompletenessRank() {
		return completenessRank;
	}
	public void setCompletenessRank(Integer completenessRank) {
		this.completenessRank = completenessRank;
	}
	public Float getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Float totalScore) {
		this.totalScore = totalScore;
	}
	public Integer getTotalRank() {
		return totalRank;
	}
	public void setTotalRank(Integer totalRank) {
		this.totalRank = totalRank;
	} 
	
	
}
