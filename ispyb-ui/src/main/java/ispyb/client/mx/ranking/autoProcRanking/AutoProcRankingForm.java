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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="autoProcRankingForm"
 */
public class AutoProcRankingForm extends ActionForm implements Serializable {
	private static final long serialVersionUID = 1668355568628041576L;

	private List<AutoProcRankingVO> autoProcRankingValue = new ArrayList<AutoProcRankingVO>();
	
	private Set<Integer> selectedItems = new HashSet<Integer>();
	
	private String sortOrder = Constants.SORT_NoOrder;
	
	private Integer weightOverallRFactor = Constants.AUTOPROC_RANKING_DEFAULT_WEIGHT_OVERALL_RFACTOR;
	private Integer weightHighestResolution = Constants.AUTOPROC_RANKING_DEFAULT_WEIGHT_HIGHEST_RESOLUTION;
	private Integer weightCompleteness = Constants.AUTOPROC_RANKING_DEFAULT_WEIGHT_COMPLETENESS;
	
	private String reportDataUrl;

	private String chartTitle;

	public List<AutoProcRankingVO> getAutoProcRankingValue() {
		return autoProcRankingValue;
	}

	public void setAutoProcRankingValue(List<AutoProcRankingVO> autoProcRankingValue) {
		this.autoProcRankingValue = autoProcRankingValue;
	}

	public Set<Integer> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(Set<Integer> selectedItems) {
		this.selectedItems = selectedItems;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

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
	public String getSelectedItems(int index) {
		if (this.selectedItems.contains(index))
			return "on";
		else
			return null;
	}

	public void setSelectedItems(int index, String value) {
		if (value.equals("on"))
			this.selectedItems.add(index);
		else
			this.selectedItems.remove(index);
	}

	public String getReportDataUrl() {
		return reportDataUrl;
	}

	public void setReportDataUrl(String reportDataUrl) {
		this.reportDataUrl = reportDataUrl;
	}

	public String getChartTitle() {
		return chartTitle;
	}

	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
	}

}
