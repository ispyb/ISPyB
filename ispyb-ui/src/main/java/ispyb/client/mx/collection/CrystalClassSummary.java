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
 * summary of the number of collects for each crystalClass
 * @author BODIN
 *
 */
public class CrystalClassSummary {
	protected String crystalClassCode;
	protected String crystalClassName;
	protected Integer numberOfCrystals;
	
	
	public CrystalClassSummary() {
		super();
	}
	
	public CrystalClassSummary(String crystalClassCode,
			String crystalClassName, Integer numberOfCrystals) {
		super();
		this.crystalClassCode = crystalClassCode;
		this.crystalClassName = crystalClassName;
		this.numberOfCrystals = numberOfCrystals;
	}
	
	public String getCrystalClassCode() {
		return crystalClassCode;
	}
	
	public void setCrystalClassCode(String crystalClassCode) {
		this.crystalClassCode = crystalClassCode;
	}
	
	public String getCrystalClassName() {
		return crystalClassName;
	}
	
	public void setCrystalClassName(String crystalClassName) {
		this.crystalClassName = crystalClassName;
	}
	
	public Integer getNumberOfCrystals() {
		return numberOfCrystals;
	}
	
	public void setNumberOfCrystals(Integer numberOfCrystals) {
		this.numberOfCrystals = numberOfCrystals;
	}
	
	
}
