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
package ispyb.client.mx.collection;

/**
 * data coming from EDNA output once a gradient has finished
 * @author BODIN
 *
 */
public class DehydrationData {
	protected Double time;
	protected Double relativeHumidity;
	protected Double labelitR1;
	protected Double labelitR2;
	protected Double numberOfSpots;
	protected Double braggPeaks;
	protected Double totalIntegratedSignal;
	protected Double cellEdgeA;
	protected Double cellEdgeB;
	protected Double cellEdgeC;
	protected Double mosaicSpread;
	protected Double rankingResolution;
	
	public DehydrationData() {
		super();
	}

	public DehydrationData(Double time, Double relativeHumidity, Double labelitR1,
			Double labelitR2, Double numberOfSpots, Double braggPeaks,
			Double totalIntegratedSignal, Double cellEdgeA, Double cellEdgeB,
			Double cellEdgeC, Double mosaicSpread, Double rankingResolution) {
		super();
		this.time = time;
		this.relativeHumidity = relativeHumidity;
		this.labelitR1 = labelitR1;
		this.labelitR2 = labelitR2;
		this.numberOfSpots = numberOfSpots;
		this.braggPeaks = braggPeaks;
		this.totalIntegratedSignal = totalIntegratedSignal;
		this.cellEdgeA = cellEdgeA;
		this.cellEdgeB = cellEdgeB;
		this.cellEdgeC = cellEdgeC;
		this.mosaicSpread = mosaicSpread;
		this.rankingResolution = rankingResolution;
	}

	public Double getTime() {
		return time;
	}

	public void setTime(Double time) {
		this.time = time;
	}

	public Double getRelativeHumidity() {
		return relativeHumidity;
	}

	public void setRelativeHumidity(Double relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}

	public Double getLabelitR1() {
		return labelitR1;
	}

	public void setLabelitR1(Double labelitR1) {
		this.labelitR1 = labelitR1;
	}

	public Double getLabelitR2() {
		return labelitR2;
	}

	public void setLabelitR2(Double labelitR2) {
		this.labelitR2 = labelitR2;
	}

	public Double getNumberOfSpots() {
		return numberOfSpots;
	}

	public void setNumberOfSpots(Double numberOfSpots) {
		this.numberOfSpots = numberOfSpots;
	}

	public Double getBraggPeaks() {
		return braggPeaks;
	}

	public void setBraggPeaks(Double braggPeaks) {
		this.braggPeaks = braggPeaks;
	}

	public Double getTotalIntegratedSignal() {
		return totalIntegratedSignal;
	}

	public void setTotalIntegratedSignal(Double totalIntegratedSignal) {
		this.totalIntegratedSignal = totalIntegratedSignal;
	}

	public Double getCellEdgeA() {
		return cellEdgeA;
	}

	public void setCellEdgeA(Double cellEdgeA) {
		this.cellEdgeA = cellEdgeA;
	}

	public Double getCellEdgeB() {
		return cellEdgeB;
	}

	public void setCellEdgeB(Double cellEdgeB) {
		this.cellEdgeB = cellEdgeB;
	}

	public Double getCellEdgeC() {
		return cellEdgeC;
	}

	public void setCellEdgeC(Double cellEdgeC) {
		this.cellEdgeC = cellEdgeC;
	}

	public Double getMosaicSpread() {
		return mosaicSpread;
	}

	public void setMosaicSpread(Double mosaicSpread) {
		this.mosaicSpread = mosaicSpread;
	}

	public Double getRankingResolution() {
		return rankingResolution;
	}

	public void setRankingResolution(Double rankingResolution) {
		this.rankingResolution = rankingResolution;
	}
	
	
}
