/****************************************************************************************************
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
 *****************************************************************************************************/
package ispyb.client.mx.collection;

import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;

/**
 * information for a dataCollectionGroup to be displayed
 * @author BODIN
 *
 */
public class DataCollectionGroup extends DataCollectionGroup3VO{
	private static final long serialVersionUID = 1L;
	
	protected Integer runNumber;
	
	protected String proteinAcronym;
	
	protected String sampleName;
	
	protected String imagePrefix;
	
	protected String sampleNameProtein;
	
	public DataCollectionGroup() {
		super();
	}

	public DataCollectionGroup(DataCollectionGroup3VO vo, Integer runNumber,  
			String proteinAcronym, String sampleName, String imagePrefix) {
		super(vo);
		this.dataCollectionVOs = vo.getDataCollectionVOs();
		this.runNumber = runNumber;
		this.proteinAcronym = proteinAcronym;
		this.sampleName = sampleName;
		this.imagePrefix = imagePrefix;
		this.sampleNameProtein = "";
		if (proteinAcronym != null && sampleName != null && !proteinAcronym.isEmpty() && !sampleName.isEmpty()){
			this.sampleNameProtein = proteinAcronym+"-"+sampleName;
		}
	}

	public Integer getRunNumber() {
		return runNumber;
	}

	public void setRunNumber(Integer runNumber) {
		this.runNumber = runNumber;
	}

	public String getProteinAcronym() {
		return proteinAcronym;
	}

	public void setProteinAcronym(String proteinAcronym) {
		this.proteinAcronym = proteinAcronym;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	public String getImagePrefix() {
		return imagePrefix;
	}

	public void setImagePrefix(String imagePrefix) {
		this.imagePrefix = imagePrefix;
	}

	public String getSampleNameProtein() {
		return sampleNameProtein;
	}

	public void setSampleNameProtein(String sampleNameProtein) {
		this.sampleNameProtein = sampleNameProtein;
	}


}
