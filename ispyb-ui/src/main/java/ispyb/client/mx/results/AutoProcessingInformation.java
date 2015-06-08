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
package ispyb.client.mx.results;

/**
 * General autoprocessing information for 1 dataCollection (to build the autoprocessing table)
 * @author BODIN
 *
 */
public class AutoProcessingInformation {
	
	protected Integer autoProcId;
	
	protected String name;
	
	protected String spaceGroup;
	
	protected String anomalous;
	
	protected Float cellA;
	
	protected Float cellB;
	
	protected Float cellC;
	
	protected Float cellAlpha;
	
	protected Float cellBeta;
	
	protected Float cellGamma;

	public AutoProcessingInformation() {
		super();
	}

	public AutoProcessingInformation(Integer autoProcId, String name, String spaceGroup,
			String anomalous, Float cellA, Float cellB, Float cellC,
			Float cellAlpha, Float cellBeta, Float cellGamma) {
		super();
		this.autoProcId = autoProcId;
		this.name = name;
		this.spaceGroup = spaceGroup;
		this.anomalous = anomalous;
		this.cellA = cellA;
		this.cellB = cellB;
		this.cellC = cellC;
		this.cellAlpha = cellAlpha;
		this.cellBeta = cellBeta;
		this.cellGamma = cellGamma;
	}

	public Integer getAutoProcId() {
		return autoProcId;
	}

	public void setAutoProcId(Integer autoProcId) {
		this.autoProcId = autoProcId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpaceGroup() {
		return spaceGroup;
	}

	public void setSpaceGroup(String spaceGroup) {
		this.spaceGroup = spaceGroup;
	}

	public String getAnomalous() {
		return anomalous;
	}

	public void setAnomalous(String anomalous) {
		this.anomalous = anomalous;
	}

	public Float getCellA() {
		return cellA;
	}

	public void setCellA(Float cellA) {
		this.cellA = cellA;
	}

	public Float getCellB() {
		return cellB;
	}

	public void setCellB(Float cellB) {
		this.cellB = cellB;
	}

	public Float getCellC() {
		return cellC;
	}

	public void setCellC(Float cellC) {
		this.cellC = cellC;
	}

	public Float getCellAlpha() {
		return cellAlpha;
	}

	public void setCellAlpha(Float cellAlpha) {
		this.cellAlpha = cellAlpha;
	}

	public Float getCellBeta() {
		return cellBeta;
	}

	public void setCellBeta(Float cellBeta) {
		this.cellBeta = cellBeta;
	}

	public Float getCellGamma() {
		return cellGamma;
	}

	public void setCellGamma(Float cellGamma) {
		this.cellGamma = cellGamma;
	}
	
	
	
	
}
